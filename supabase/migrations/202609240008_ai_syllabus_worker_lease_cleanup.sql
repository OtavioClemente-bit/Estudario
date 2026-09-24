alter table public.ai_jobs
  add column if not exists lease_token text,
  add column if not exists lease_generation bigint not null default 0,
  add column if not exists processing_deadline_at timestamptz;

alter table public.ai_jobs
  add constraint ai_jobs_lease_generation_check check (lease_generation >= 0);

create table public.ai_job_source_cleanup (
  job_id uuid primary key references public.ai_jobs (id) on delete cascade,
  source_object_path text not null,
  status text not null default 'PENDING',
  attempt_count integer not null default 0,
  lease_owner text,
  lease_token text,
  lease_generation bigint not null default 0,
  lease_expires_at timestamptz,
  last_error text,
  created_at timestamptz not null default now(),
  updated_at timestamptz not null default now(),
  constraint ai_job_source_cleanup_status_check check (status in ('PENDING', 'DELETED')),
  constraint ai_job_source_cleanup_attempt_count_check check (attempt_count >= 0),
  constraint ai_job_source_cleanup_lease_generation_check check (lease_generation >= 0)
);

create index ai_job_source_cleanup_pending_idx
  on public.ai_job_source_cleanup (status, lease_expires_at, updated_at)
  where status = 'PENDING';

alter table public.ai_job_source_cleanup enable row level security;
revoke all on public.ai_job_source_cleanup from public, anon, authenticated;

create or replace function public.claim_ai_syllabus_worker_job(
  p_lease_owner text,
  p_lease_token text,
  p_lease_seconds integer default 300,
  p_processing_seconds integer default 900
)
returns public.ai_jobs
language plpgsql
security definer
set search_path = public
as $$
declare
  v_job public.ai_jobs;
begin
  if coalesce(auth.role(), '') not in ('service_role', 'supabase_admin') then
    raise exception using errcode = '42501', message = 'AI_WORKER_FORBIDDEN';
  end if;
  if p_lease_owner is null or length(btrim(p_lease_owner)) = 0
     or p_lease_token is null or length(btrim(p_lease_token)) = 0
     or p_lease_seconds <= 0 or p_processing_seconds <= 0 then
    raise exception using errcode = '22023', message = 'INVALID_JOB_LEASE';
  end if;

  select * into v_job
  from public.ai_jobs
  where status = 'PROCESSING'
    and (lease_token is null or lease_expires_at is null or lease_expires_at <= now())
    and source_object_path is not null
    and source_hash is not null
  order by created_at
  for update skip locked
  limit 1;

  if not found then return null; end if;

  update public.ai_jobs
  set lease_owner = p_lease_owner,
      lease_token = p_lease_token,
      lease_generation = lease_generation + 1,
      lease_expires_at = now() + make_interval(secs => p_lease_seconds),
      processing_deadline_at = coalesce(processing_deadline_at, now() + make_interval(secs => p_processing_seconds))
  where id = v_job.id
  returning * into v_job;
  return v_job;
end;
$$;

create or replace function public.assert_ai_job_lease(
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
  if coalesce(auth.role(), '') not in ('service_role', 'supabase_admin') then
    raise exception using errcode = '42501', message = 'AI_WORKER_FORBIDDEN';
  end if;
  select * into v_job from public.ai_jobs where id = p_job_id for update;
  if not found then raise exception using errcode = 'P0001', message = 'AI_JOB_NOT_FOUND'; end if;
  if v_job.status <> 'PROCESSING'
     or v_job.lease_owner is distinct from p_lease_owner
     or v_job.lease_token is distinct from p_lease_token
     or v_job.lease_generation is distinct from p_lease_generation
     or v_job.lease_expires_at is null
     or v_job.lease_expires_at <= now() then
    raise exception using errcode = 'P0001', message = 'AI_JOB_LEASE_LOST';
  end if;
  return v_job;
end;
$$;

create or replace function public.persist_ai_job_provider_response(
  p_job_id uuid,
  p_response_id text,
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
  if p_response_id is null or length(btrim(p_response_id)) = 0 then
    raise exception using errcode = '22023', message = 'INVALID_PROVIDER_RESPONSE_ID';
  end if;
  select * into v_job from public.assert_ai_job_lease(p_job_id, p_lease_owner, p_lease_token, p_lease_generation);
  update public.ai_jobs
  set openai_response_id = coalesce(openai_response_id, p_response_id),
      provider_execution_started_at = coalesce(provider_execution_started_at, now())
  where id = v_job.id
  returning * into v_job;
  return v_job;
end;
$$;

create or replace function public.record_ai_job_provider_reconciliation(
  p_job_id uuid,
  p_lease_owner text,
  p_lease_token text,
  p_lease_generation bigint,
  p_recoverable boolean
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
  set provider_reconciled_at = now(), provider_result_recoverable = p_recoverable
  where id = v_job.id
  returning * into v_job;
  return v_job;
end;
$$;

create or replace function public.increment_ai_job_retry(
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
  set retry_count = retry_count + 1,
      lease_owner = null,
      lease_token = null,
      lease_expires_at = null,
      lease_generation = lease_generation + 1
  where id = v_job.id
  returning * into v_job;
  return v_job;
end;
$$;

create or replace function public.record_ai_job_usage(
  p_job_id uuid,
  p_lease_owner text,
  p_lease_token text,
  p_lease_generation bigint,
  p_input_tokens bigint,
  p_output_tokens bigint,
  p_total_tokens bigint
)
returns public.ai_jobs
language plpgsql
security definer
set search_path = public
as $$
declare
  v_job public.ai_jobs;
begin
  if (p_input_tokens is not null and p_input_tokens < 0)
     or (p_output_tokens is not null and p_output_tokens < 0)
     or (p_total_tokens is not null and p_total_tokens < 0) then
    raise exception using errcode = '22023', message = 'INVALID_PROVIDER_USAGE';
  end if;
  select * into v_job from public.assert_ai_job_lease(p_job_id, p_lease_owner, p_lease_token, p_lease_generation);
  update public.ai_jobs
  set provider_input_tokens = p_input_tokens,
      provider_output_tokens = p_output_tokens,
      provider_total_tokens = p_total_tokens
  where id = v_job.id
  returning * into v_job;
  return v_job;
end;
$$;

create or replace function public.finalize_ai_job_success_with_lease(
  p_job_id uuid,
  p_proposal jsonb,
  p_warnings jsonb,
  p_openai_response_id text,
  p_prompt_version text,
  p_schema_version integer,
  p_model_version text,
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
  v_reservation public.ai_quota_reservations;
  v_usage public.ai_quota_usage;
begin
  if p_proposal is null or jsonb_typeof(p_proposal) <> 'object' then
    raise exception using errcode = '22023', message = 'INVALID_PROPOSAL';
  end if;
  if p_warnings is null or jsonb_typeof(p_warnings) <> 'array' then
    raise exception using errcode = '22023', message = 'INVALID_WARNINGS';
  end if;
  select * into v_job from public.assert_ai_job_lease(p_job_id, p_lease_owner, p_lease_token, p_lease_generation);
  select * into v_reservation from public.ai_quota_reservations where job_id = v_job.id for update;
  if not found or v_reservation.status <> 'RESERVED' then
    raise exception using errcode = 'P0001', message = 'AI_RESERVATION_NOT_ACTIVE';
  end if;
  select * into v_usage from public.ai_quota_usage
  where user_id = v_reservation.user_id and feature = v_reservation.feature and period_start = v_reservation.period_start
  for update;
  if not found or v_usage.reserved_count <= 0 then
    raise exception using errcode = 'P0001', message = 'AI_QUOTA_RESERVATION_MISSING';
  end if;

  perform set_config('ai.internal_job_transition', '1', true);
  update public.ai_jobs
  set status = 'SUCCEEDED',
      openai_response_id = coalesce(p_openai_response_id, openai_response_id),
      provider_execution_started_at = case when p_openai_response_id is not null then coalesce(provider_execution_started_at, now()) else provider_execution_started_at end,
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

  update public.ai_quota_reservations set status = 'CONSUMED', consumed_at = now() where id = v_reservation.id;
  update public.ai_quota_usage
  set reserved_count = reserved_count - 1, successful_count = successful_count + 1
  where user_id = v_usage.user_id and feature = v_usage.feature and period_start = v_usage.period_start;
  return v_job;
end;
$$;

create or replace function public.finalize_ai_job_failure_with_lease(
  p_job_id uuid,
  p_terminal_status public.ai_job_status,
  p_error_code text,
  p_error_message text,
  p_provider_reconciled boolean,
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
  if p_terminal_status not in ('FAILED', 'EXPIRED', 'CANCELLED') then
    raise exception using errcode = '22023', message = 'INVALID_RELEASE_STATUS';
  end if;
  select * into v_job from public.assert_ai_job_lease(p_job_id, p_lease_owner, p_lease_token, p_lease_generation);
  update public.ai_jobs
  set provider_reconciled_at = now(), provider_result_recoverable = p_provider_reconciled
  where id = v_job.id;
  select * into v_job from public.release_ai_job_reservation(v_job.id, p_terminal_status, p_error_code, p_error_message);
  return v_job;
end;
$$;

create or replace function public.prepare_ai_job_source_cleanup(
  p_job_id uuid,
  p_lease_owner text,
  p_lease_token text,
  p_lease_generation bigint
)
returns public.ai_job_source_cleanup
language plpgsql
security definer
set search_path = public
as $$
declare
  v_job public.ai_jobs;
  v_cleanup public.ai_job_source_cleanup;
begin
  select * into v_job from public.assert_ai_job_lease(p_job_id, p_lease_owner, p_lease_token, p_lease_generation);
  insert into public.ai_job_source_cleanup (job_id, source_object_path)
  values (v_job.id, v_job.source_object_path)
  on conflict (job_id) do nothing;
  select * into v_cleanup from public.ai_job_source_cleanup where job_id = v_job.id for update;
  if v_cleanup.status = 'DELETED' then return v_cleanup; end if;
  update public.ai_job_source_cleanup
  set lease_owner = p_lease_owner,
      lease_token = p_lease_token,
      lease_generation = p_lease_generation,
      lease_expires_at = v_job.lease_expires_at,
      updated_at = now()
  where job_id = v_job.id
  returning * into v_cleanup;
  return v_cleanup;
end;
$$;

create or replace function public.complete_ai_job_source_cleanup(
  p_job_id uuid,
  p_lease_owner text,
  p_lease_token text,
  p_lease_generation bigint
)
returns public.ai_job_source_cleanup
language plpgsql
security definer
set search_path = public
as $$
declare
  v_cleanup public.ai_job_source_cleanup;
begin
  select * into v_cleanup from public.ai_job_source_cleanup
  where job_id = p_job_id for update;
  if not found or v_cleanup.status <> 'PENDING'
     or v_cleanup.lease_owner is distinct from p_lease_owner
     or v_cleanup.lease_token is distinct from p_lease_token
     or v_cleanup.lease_generation is distinct from p_lease_generation
     or v_cleanup.lease_expires_at is null or v_cleanup.lease_expires_at <= now() then
    raise exception using errcode = 'P0001', message = 'AI_JOB_LEASE_LOST';
  end if;
  update public.ai_job_source_cleanup
  set status = 'DELETED', lease_owner = null, lease_token = null, lease_expires_at = null, last_error = null, updated_at = now()
  where job_id = p_job_id
  returning * into v_cleanup;
  return v_cleanup;
end;
$$;

create or replace function public.fail_ai_job_source_cleanup(
  p_job_id uuid,
  p_error text,
  p_lease_owner text,
  p_lease_token text,
  p_lease_generation bigint
)
returns public.ai_job_source_cleanup
language plpgsql
security definer
set search_path = public
as $$
declare
  v_cleanup public.ai_job_source_cleanup;
begin
  select * into v_cleanup from public.ai_job_source_cleanup
  where job_id = p_job_id for update;
  if not found or v_cleanup.status <> 'PENDING'
     or v_cleanup.lease_owner is distinct from p_lease_owner
     or v_cleanup.lease_token is distinct from p_lease_token
     or v_cleanup.lease_generation is distinct from p_lease_generation
     or v_cleanup.lease_expires_at is null or v_cleanup.lease_expires_at <= now() then
    raise exception using errcode = 'P0001', message = 'AI_JOB_LEASE_LOST';
  end if;
  update public.ai_job_source_cleanup
  set attempt_count = attempt_count + 1, lease_owner = null, lease_token = null, lease_expires_at = null,
      last_error = left(coalesce(p_error, 'AI_SOURCE_CLEANUP_FAILED'), 1000), updated_at = now()
  where job_id = p_job_id
  returning * into v_cleanup;
  return v_cleanup;
end;
$$;

create or replace function public.claim_ai_job_source_cleanup(
  p_lease_owner text,
  p_lease_token text,
  p_lease_seconds integer default 300
)
returns public.ai_job_source_cleanup
language plpgsql
security definer
set search_path = public
as $$
declare
  v_cleanup public.ai_job_source_cleanup;
begin
  if coalesce(auth.role(), '') not in ('service_role', 'supabase_admin') then
    raise exception using errcode = '42501', message = 'AI_WORKER_FORBIDDEN';
  end if;
  if p_lease_owner is null or length(btrim(p_lease_owner)) = 0
     or p_lease_token is null or length(btrim(p_lease_token)) = 0 or p_lease_seconds <= 0 then
    raise exception using errcode = '22023', message = 'INVALID_JOB_LEASE';
  end if;
  select * into v_cleanup from public.ai_job_source_cleanup
  where status = 'PENDING' and (lease_expires_at is null or lease_expires_at <= now())
  order by updated_at
  for update skip locked limit 1;
  if not found then return null; end if;
  update public.ai_job_source_cleanup
  set lease_owner = p_lease_owner, lease_token = p_lease_token,
      lease_generation = lease_generation + 1,
      lease_expires_at = now() + make_interval(secs => p_lease_seconds), updated_at = now()
  where job_id = v_cleanup.job_id
  returning * into v_cleanup;
  return v_cleanup;
end;
$$;

revoke all on function public.claim_ai_syllabus_worker_job(text, integer) from public, service_role;
revoke all on function public.record_ai_job_provider_reconciliation(uuid, boolean) from public, service_role;
revoke all on function public.increment_ai_job_retry(uuid) from public, service_role;
revoke all on function public.record_ai_job_usage(uuid, bigint, bigint, bigint) from public, service_role;

revoke all on function public.claim_ai_syllabus_worker_job(text, text, integer, integer) from public;
revoke all on function public.assert_ai_job_lease(uuid, text, text, bigint) from public;
revoke all on function public.persist_ai_job_provider_response(uuid, text, text, text, bigint) from public;
revoke all on function public.record_ai_job_provider_reconciliation(uuid, text, text, bigint, boolean) from public;
revoke all on function public.increment_ai_job_retry(uuid, text, text, bigint) from public;
revoke all on function public.record_ai_job_usage(uuid, text, text, bigint, bigint, bigint, bigint) from public;
revoke all on function public.finalize_ai_job_success_with_lease(uuid, jsonb, jsonb, text, text, integer, text, text, text, bigint) from public;
revoke all on function public.finalize_ai_job_failure_with_lease(uuid, public.ai_job_status, text, text, boolean, text, text, bigint) from public;
revoke all on function public.prepare_ai_job_source_cleanup(uuid, text, text, bigint) from public;
revoke all on function public.complete_ai_job_source_cleanup(uuid, text, text, bigint) from public;
revoke all on function public.fail_ai_job_source_cleanup(uuid, text, text, text, bigint) from public;
revoke all on function public.claim_ai_job_source_cleanup(text, text, integer) from public;

grant execute on function public.claim_ai_syllabus_worker_job(text, text, integer, integer) to service_role;
grant execute on function public.assert_ai_job_lease(uuid, text, text, bigint) to service_role;
grant execute on function public.persist_ai_job_provider_response(uuid, text, text, text, bigint) to service_role;
grant execute on function public.record_ai_job_provider_reconciliation(uuid, text, text, bigint, boolean) to service_role;
grant execute on function public.increment_ai_job_retry(uuid, text, text, bigint) to service_role;
grant execute on function public.record_ai_job_usage(uuid, text, text, bigint, bigint, bigint, bigint) to service_role;
grant execute on function public.finalize_ai_job_success_with_lease(uuid, jsonb, jsonb, text, text, integer, text, text, text, bigint) to service_role;
grant execute on function public.finalize_ai_job_failure_with_lease(uuid, public.ai_job_status, text, text, boolean, text, text, bigint) to service_role;
grant execute on function public.prepare_ai_job_source_cleanup(uuid, text, text, bigint) to service_role;
grant execute on function public.complete_ai_job_source_cleanup(uuid, text, text, bigint) to service_role;
grant execute on function public.fail_ai_job_source_cleanup(uuid, text, text, text, bigint) to service_role;
grant execute on function public.claim_ai_job_source_cleanup(text, text, integer) to service_role;
