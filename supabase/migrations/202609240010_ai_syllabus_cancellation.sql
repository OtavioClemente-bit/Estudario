alter table public.ai_jobs
  add column if not exists cancellation_requested_at timestamptz,
  add column if not exists cancellation_reconciled_at timestamptz,
  add column if not exists cancellation_provider_status text,
  add column if not exists cancellation_error_code text;

create or replace function public.mark_ai_job_provider_execution_started(
  p_job_id uuid,
  p_lease_owner text,
  p_lease_token text,
  p_lease_generation bigint
)
returns public.ai_jobs
language plpgsql
security definer
set search_path = public
as $$
declare
  v_job public.ai_jobs;
begin
  select * into v_job from public.assert_ai_job_lease(p_job_id, p_lease_owner, p_lease_token, p_lease_generation);
  update public.ai_jobs
  set provider_execution_started_at = coalesce(provider_execution_started_at, now())
  where id = v_job.id
  returning * into v_job;
  return v_job;
end;
$$;

create or replace function public.request_ai_job_cancellation(
  p_job_id uuid
)
returns public.ai_jobs
language plpgsql
security definer
set search_path = public
as $$
declare
  v_job public.ai_jobs;
begin
  select * into v_job from public.ai_jobs where id = p_job_id for update;
  if not found then
    raise exception using errcode = 'P0001', message = 'AI_JOB_NOT_FOUND';
  end if;
  perform public.ai_assert_job_owner(v_job.user_id);
  if v_job.status in ('SUCCEEDED', 'FAILED', 'EXPIRED', 'CANCELLED') then
    return v_job;
  end if;

  update public.ai_jobs
  set cancellation_requested_at = coalesce(cancellation_requested_at, now())
  where id = v_job.id
  returning * into v_job;
  return v_job;
end;
$$;

create or replace function public.cancel_ai_job_without_provider(
  p_job_id uuid
)
returns public.ai_jobs
language plpgsql
security definer
set search_path = public
as $$
declare
  v_job public.ai_jobs;
begin
  select * into v_job from public.ai_jobs where id = p_job_id for update;
  if not found then
    raise exception using errcode = 'P0001', message = 'AI_JOB_NOT_FOUND';
  end if;
  perform public.ai_assert_job_owner(v_job.user_id);
  if v_job.status in ('SUCCEEDED', 'FAILED', 'EXPIRED', 'CANCELLED') then
    return v_job;
  end if;
  if v_job.status not in ('RESERVED', 'PROCESSING') then
    raise exception using errcode = 'P0001', message = 'AI_JOB_NOT_CANCELLABLE';
  end if;
  if v_job.provider_execution_started_at is not null or v_job.openai_response_id is not null then
    raise exception using errcode = 'P0001', message = 'CANCELLATION_RECONCILIATION_REQUIRED';
  end if;
  if v_job.status = 'PROCESSING'
     and v_job.lease_expires_at is not null
     and v_job.lease_expires_at > now() then
    raise exception using errcode = 'P0001', message = 'CANCELLATION_RECONCILIATION_REQUIRED';
  end if;

  update public.ai_jobs
  set provider_reconciled_at = now(),
      provider_result_recoverable = false,
      cancellation_reconciled_at = now(),
      cancellation_provider_status = 'not_started',
      cancellation_error_code = null
  where id = v_job.id;

  select * into v_job from public.release_ai_job_reservation(
    v_job.id,
    'CANCELLED',
    'USER_CANCELLED',
    'Cancellation confirmed before provider execution'
  );
  return v_job;
end;
$$;

create or replace function public.record_ai_job_cancellation_reconciliation(
  p_job_id uuid,
  p_openai_response_id text,
  p_provider_status text,
  p_provider_result_recoverable boolean,
  p_error_code text default null
)
returns public.ai_jobs
language plpgsql
security definer
set search_path = public
as $$
declare
  v_job public.ai_jobs;
begin
  if p_provider_status is null or length(btrim(p_provider_status)) = 0 then
    raise exception using errcode = '22023', message = 'INVALID_PROVIDER_RECONCILIATION';
  end if;
  select * into v_job from public.ai_jobs where id = p_job_id for update;
  if not found then
    raise exception using errcode = 'P0001', message = 'AI_JOB_NOT_FOUND';
  end if;
  perform public.ai_assert_job_owner(v_job.user_id);
  if v_job.status in ('SUCCEEDED', 'FAILED', 'EXPIRED', 'CANCELLED') then
    return v_job;
  end if;
  update public.ai_jobs
  set openai_response_id = coalesce(openai_response_id, nullif(btrim(p_openai_response_id), '')),
      provider_execution_started_at = case
        when nullif(btrim(p_openai_response_id), '') is not null then coalesce(provider_execution_started_at, now())
        else provider_execution_started_at
      end,
      provider_reconciled_at = now(),
      provider_result_recoverable = p_provider_result_recoverable,
      cancellation_reconciled_at = now(),
      cancellation_provider_status = left(p_provider_status, 100),
      cancellation_error_code = left(nullif(btrim(p_error_code), ''), 100)
  where id = v_job.id
  returning * into v_job;
  return v_job;
end;
$$;

create or replace function public.finalize_ai_job_success_after_cancellation(
  p_job_id uuid,
  p_proposal jsonb,
  p_warnings jsonb,
  p_openai_response_id text,
  p_prompt_version text,
  p_schema_version integer,
  p_model_version text
)
returns public.ai_jobs
language plpgsql
security definer
set search_path = public
as $$
declare
  v_job public.ai_jobs;
  v_reservation public.ai_quota_reservations;
  v_usage public.ai_quota_usage;
begin
  if p_proposal is null or jsonb_typeof(p_proposal) <> 'object'
     or p_warnings is null or jsonb_typeof(p_warnings) <> 'array'
     or p_openai_response_id is null or length(btrim(p_openai_response_id)) = 0 then
    raise exception using errcode = '22023', message = 'INVALID_PROVIDER_RESULT';
  end if;
  select * into v_job from public.ai_jobs where id = p_job_id for update;
  if not found then
    raise exception using errcode = 'P0001', message = 'AI_JOB_NOT_FOUND';
  end if;
  perform public.ai_assert_job_owner(v_job.user_id);
  if v_job.status = 'SUCCEEDED' then return v_job; end if;
  if v_job.status in ('FAILED', 'EXPIRED', 'CANCELLED') then return v_job; end if;
  if v_job.status <> 'PROCESSING' then
    raise exception using errcode = 'P0001', message = 'AI_JOB_NOT_PROCESSING';
  end if;

  select * into v_reservation from public.ai_quota_reservations where job_id = v_job.id for update;
  if not found or v_reservation.status <> 'RESERVED' then
    raise exception using errcode = 'P0001', message = 'AI_RESERVATION_NOT_ACTIVE';
  end if;
  select * into v_usage from public.ai_quota_usage
  where user_id = v_reservation.user_id
    and feature = v_reservation.feature
    and period_start = v_reservation.period_start
  for update;
  if not found or v_usage.reserved_count <= 0 then
    raise exception using errcode = 'P0001', message = 'AI_QUOTA_RESERVATION_MISSING';
  end if;

  perform set_config('ai.internal_job_transition', '1', true);
  update public.ai_jobs
  set status = 'SUCCEEDED',
      openai_response_id = coalesce(openai_response_id, p_openai_response_id),
      provider_execution_started_at = coalesce(provider_execution_started_at, now()),
      provider_reconciled_at = coalesce(provider_reconciled_at, now()),
      provider_result_recoverable = true,
      cancellation_reconciled_at = now(),
      cancellation_provider_status = 'completed',
      cancellation_error_code = null,
      prompt_version = p_prompt_version,
      schema_version = p_schema_version,
      model_version = p_model_version,
      proposal = p_proposal,
      warnings = p_warnings,
      finished_at = now(),
      lease_owner = null,
      lease_token = null,
      lease_expires_at = null,
      lease_generation = lease_generation + 1,
      error_code = null,
      error_message = null
  where id = v_job.id
  returning * into v_job;
  perform set_config('ai.internal_job_transition', '0', true);

  update public.ai_quota_reservations
  set status = 'CONSUMED', consumed_at = now()
  where id = v_reservation.id;
  update public.ai_quota_usage
  set reserved_count = reserved_count - 1,
      successful_count = successful_count + 1
  where user_id = v_usage.user_id
    and feature = v_usage.feature
    and period_start = v_usage.period_start;
  return v_job;
end;
$$;

revoke all on function public.request_ai_job_cancellation(uuid) from public, anon;
revoke all on function public.mark_ai_job_provider_execution_started(uuid, text, text, bigint) from public, anon;
revoke all on function public.cancel_ai_job_without_provider(uuid) from public, anon;
revoke all on function public.record_ai_job_cancellation_reconciliation(uuid, text, text, boolean, text) from public, anon, authenticated;
revoke all on function public.finalize_ai_job_success_after_cancellation(uuid, jsonb, jsonb, text, text, integer, text) from public, anon, authenticated;
grant execute on function public.request_ai_job_cancellation(uuid) to authenticated, service_role;
grant execute on function public.mark_ai_job_provider_execution_started(uuid, text, text, bigint) to service_role;
grant execute on function public.cancel_ai_job_without_provider(uuid) to authenticated, service_role;
grant execute on function public.record_ai_job_cancellation_reconciliation(uuid, text, text, boolean, text) to service_role;
grant execute on function public.finalize_ai_job_success_after_cancellation(uuid, jsonb, jsonb, text, text, integer, text) to service_role;
