-- A known provider ID can survive a failed lease-bound write or lease expiry.
create function public.recover_ai_job_provider_response(p_job_id uuid, p_response_id text)
returns public.ai_jobs
language plpgsql security definer set search_path = ''
as $$
declare v_job public.ai_jobs;
begin
  if coalesce(auth.role(), '') not in ('service_role', 'supabase_admin') then
    raise exception using errcode = '42501', message = 'AI_WORKER_FORBIDDEN';
  end if;
  if p_response_id is null or length(btrim(p_response_id)) = 0 then
    raise exception using errcode = '22023', message = 'INVALID_PROVIDER_RESPONSE_ID';
  end if;
  select * into v_job from public.ai_jobs where id = p_job_id for update;
  if not found then raise exception using errcode = 'P0001', message = 'AI_JOB_NOT_FOUND'; end if;
  if v_job.status <> 'PROCESSING' then
    raise exception using errcode = 'P0001', message = 'AI_JOB_NOT_PROCESSING';
  end if;
  if v_job.openai_response_id is not null then
    if v_job.openai_response_id <> p_response_id then
      raise exception using errcode = 'P0001', message = 'PROVIDER_RESPONSE_ID_CONFLICT';
    end if;
    if v_job.provider_start_outcome = 'ACCEPTED' and v_job.provider_quarantined_at is null then
      return v_job;
    end if;
  end if;
  if v_job.provider_start_outcome not in ('IN_FLIGHT', 'RESPONSE_AMBIGUOUS')
     or v_job.provider_quarantined_at is null then
    raise exception using errcode = 'P0001', message = 'AI_PROVIDER_RESPONSE_NOT_EXPECTED';
  end if;
  update public.ai_jobs
  set openai_response_id = p_response_id, provider_start_outcome = 'ACCEPTED',
      provider_quarantined_at = null, processing_deadline_at = null,
      provider_execution_started_at = coalesce(provider_execution_started_at, now())
  where id = p_job_id returning * into v_job;
  if v_job.cancellation_requested_at is not null then
    insert into public.ai_job_provider_reconciliation_queue (job_id, status, available_at, deadline_at)
    values (p_job_id, 'PENDING', now(), null)
    on conflict (job_id) do update set status = 'PENDING', available_at = now(),
      deadline_at = null, updated_at = now();
  end if;
  return v_job;
end;
$$;

-- The provider adapter proved fetch was never invoked. State and quota commit together.
create function public.finalize_ai_job_not_sent(
  p_job_id uuid, p_lease_owner text, p_lease_token text,
  p_lease_generation bigint, p_error_code text
)
returns public.ai_jobs
language plpgsql security definer set search_path = ''
as $$
declare
  v_job public.ai_jobs;
  v_reservation public.ai_quota_reservations;
  v_usage public.ai_quota_usage;
  v_status public.ai_job_status;
begin
  if p_error_code is null or p_error_code !~ '^[A-Z][A-Z0-9_]{2,63}$' then
    raise exception using errcode = '22023', message = 'INVALID_PROVIDER_ERROR_CODE';
  end if;
  select * into v_job from public.assert_ai_job_lease(p_job_id, p_lease_owner, p_lease_token, p_lease_generation);
  if v_job.provider_start_outcome <> 'IN_FLIGHT' or v_job.provider_quarantined_at is null
     or v_job.openai_response_id is not null then
    raise exception using errcode = 'P0001', message = 'AI_PROVIDER_NOT_SENT_NOT_ALLOWED';
  end if;
  select * into v_reservation from public.ai_quota_reservations where job_id = p_job_id for update;
  if not found or v_reservation.status <> 'RESERVED' then
    raise exception using errcode = 'P0001', message = 'AI_RESERVATION_NOT_ACTIVE';
  end if;
  select * into v_usage from public.ai_quota_usage
  where user_id = v_reservation.user_id and feature = v_reservation.feature
    and period_start = v_reservation.period_start for update;
  if not found or v_usage.reserved_count <= 0 then
    raise exception using errcode = 'P0001', message = 'AI_QUOTA_RESERVATION_MISSING';
  end if;
  v_status := case when v_job.cancellation_requested_at is null then 'FAILED'::public.ai_job_status
    else 'CANCELLED'::public.ai_job_status end;
  perform set_config('ai.internal_job_transition', '1', true);
  update public.ai_jobs
  set status = v_status, provider_start_outcome = 'NOT_SENT', provider_quarantined_at = null,
      provider_reconciled_at = now(), provider_result_recoverable = false,
      cancellation_reconciled_at = case when v_status = 'CANCELLED' then now() else cancellation_reconciled_at end,
      cancellation_provider_status = case when v_status = 'CANCELLED' then 'not_sent' else cancellation_provider_status end,
      error_code = p_error_code, error_message = 'The AI job did not complete', finished_at = now(),
      lease_owner = null, lease_token = null, lease_expires_at = null,
      lease_generation = lease_generation + 1
  where id = p_job_id returning * into v_job;
  perform set_config('ai.internal_job_transition', '0', true);
  update public.ai_quota_reservations set status = 'RELEASED', released_at = now()
    where id = v_reservation.id;
  update public.ai_quota_usage set reserved_count = reserved_count - 1
    where user_id = v_usage.user_id and feature = v_usage.feature and period_start = v_usage.period_start;
  return v_job;
end;
$$;

revoke all on function public.recover_ai_job_provider_response(uuid, text) from public, anon, authenticated, service_role;
revoke all on function public.finalize_ai_job_not_sent(uuid, text, text, bigint, text) from public, anon, authenticated, service_role;
grant execute on function public.recover_ai_job_provider_response(uuid, text) to service_role;
grant execute on function public.finalize_ai_job_not_sent(uuid, text, text, bigint, text) to service_role;

-- The audit timestamp cannot authorize or forbid a local cancellation.
create or replace function public.cancel_ai_job_without_provider(p_job_id uuid)
returns public.ai_jobs
language plpgsql security definer set search_path = ''
as $$
declare v_job public.ai_jobs;
begin
  select * into v_job from public.ai_jobs where id = p_job_id for update;
  if not found then raise exception using errcode = 'P0001', message = 'AI_JOB_NOT_FOUND'; end if;
  perform public.ai_assert_job_owner(v_job.user_id);
  if v_job.status in ('SUCCEEDED', 'FAILED', 'EXPIRED', 'CANCELLED') then return v_job; end if;
  if v_job.status not in ('RESERVED', 'PROCESSING') then
    raise exception using errcode = 'P0001', message = 'AI_JOB_NOT_CANCELLABLE';
  end if;
  if v_job.provider_start_outcome <> 'NOT_STARTED' or v_job.provider_quarantined_at is not null
     or v_job.openai_response_id is not null then
    raise exception using errcode = 'P0001', message = 'CANCELLATION_RECONCILIATION_REQUIRED';
  end if;
  if v_job.status = 'PROCESSING' and v_job.lease_expires_at is not null
     and v_job.lease_expires_at > now() then
    raise exception using errcode = 'P0001', message = 'CANCELLATION_RECONCILIATION_REQUIRED';
  end if;
  update public.ai_jobs set provider_reconciled_at = now(), provider_result_recoverable = false,
    cancellation_reconciled_at = now(), cancellation_provider_status = 'not_started', cancellation_error_code = null
  where id = v_job.id;
  select * into v_job from public.release_ai_job_reservation(
    v_job.id, 'CANCELLED', 'USER_CANCELLED', 'Cancellation confirmed before provider execution');
  return v_job;
end;
$$;

create or replace function public.release_ai_job_reservation(
  p_job_id uuid, p_terminal_status public.ai_job_status default 'FAILED',
  p_error_code text default null, p_error_message text default null
)
returns public.ai_jobs
language plpgsql security definer set search_path = ''
as $$
declare
  v_job public.ai_jobs;
  v_reservation public.ai_quota_reservations;
  v_usage public.ai_quota_usage;
begin
  if p_terminal_status not in ('FAILED', 'EXPIRED', 'CANCELLED') then
    raise exception using errcode = '22023', message = 'INVALID_RELEASE_STATUS';
  end if;
  select * into v_job from public.ai_jobs where id = p_job_id for update;
  if not found then raise exception using errcode = 'P0001', message = 'AI_JOB_NOT_FOUND'; end if;
  perform public.ai_assert_job_owner(v_job.user_id);
  if v_job.status = p_terminal_status then return v_job; end if;
  if v_job.status in ('SUCCEEDED', 'FAILED', 'EXPIRED', 'CANCELLED') then
    raise exception using errcode = 'P0001', message = 'TERMINAL_JOB_IMMUTABLE';
  end if;
  if v_job.provider_quarantined_at is not null then
    raise exception using errcode = 'P0001', message = 'CANCELLATION_RECONCILIATION_REQUIRED';
  end if;
  if p_terminal_status in ('FAILED', 'EXPIRED') and v_job.status <> 'PROCESSING' then
    raise exception using errcode = 'P0001', message = 'INVALID_FAILURE_STATE';
  end if;
  if p_terminal_status in ('FAILED', 'EXPIRED') and v_job.status = 'PROCESSING'
     and (v_job.provider_reconciled_at is null or v_job.provider_result_recoverable is not false) then
    if coalesce(v_job.provider_result_recoverable, false) then
      raise exception using errcode = 'P0001', message = 'PROVIDER_RESULT_RECOVERABLE';
    end if;
    raise exception using errcode = 'P0001', message = 'PROVIDER_RECONCILIATION_REQUIRED';
  end if;
  if p_terminal_status = 'CANCELLED' and v_job.status = 'PROCESSING'
     and (v_job.provider_reconciled_at is null or v_job.provider_result_recoverable is not false) then
    raise exception using errcode = 'P0001', message = 'CANCELLATION_RECONCILIATION_REQUIRED';
  end if;
  if p_terminal_status = 'CANCELLED' and v_job.status = 'RESERVED'
     and (v_job.provider_start_outcome <> 'NOT_STARTED' or v_job.openai_response_id is not null) then
    raise exception using errcode = 'P0001', message = 'CANCELLATION_PROVIDER_STARTED';
  end if;
  select * into v_reservation from public.ai_quota_reservations where job_id = v_job.id for update;
  if not found or v_reservation.status <> 'RESERVED' then
    raise exception using errcode = 'P0001', message = 'AI_RESERVATION_NOT_ACTIVE';
  end if;
  select * into v_usage from public.ai_quota_usage
    where user_id = v_reservation.user_id and feature = v_reservation.feature
      and period_start = v_reservation.period_start for update;
  if not found or v_usage.reserved_count <= 0 then
    raise exception using errcode = 'P0001', message = 'AI_QUOTA_RESERVATION_MISSING';
  end if;
  perform set_config('ai.internal_job_transition', '1', true);
  update public.ai_jobs set status = p_terminal_status, finished_at = now(), lease_owner = null,
    lease_expires_at = null, error_code = p_error_code, error_message = p_error_message
  where id = v_job.id returning * into v_job;
  perform set_config('ai.internal_job_transition', '0', true);
  update public.ai_quota_reservations set status = 'RELEASED', released_at = now()
    where id = v_reservation.id;
  update public.ai_quota_usage set reserved_count = reserved_count - 1
    where user_id = v_usage.user_id and feature = v_usage.feature and period_start = v_usage.period_start;
  return v_job;
end;
$$;

-- Reconciliation cannot certify a result as recoverable without an ID.
create or replace function public.record_ai_job_provider_reconciliation(
  p_job_id uuid, p_lease_owner text, p_lease_token text,
  p_lease_generation bigint, p_recoverable boolean
)
returns public.ai_jobs
language plpgsql security definer set search_path = ''
as $$
declare v_job public.ai_jobs;
begin
  select * into v_job from public.assert_ai_job_lease(
    p_job_id, p_lease_owner, p_lease_token, p_lease_generation);
  if v_job.provider_quarantined_at is not null or
     v_job.provider_start_outcome not in ('NOT_STARTED', 'ACCEPTED') then
    raise exception using errcode = 'P0001', message = 'AI_PROVIDER_QUARANTINED';
  end if;
  if p_recoverable is true and v_job.openai_response_id is null then
    raise exception using errcode = 'P0001', message = 'PROVIDER_RESPONSE_ID_REQUIRED';
  end if;
  update public.ai_jobs set provider_reconciled_at = now(), provider_result_recoverable = p_recoverable
  where id = v_job.id returning * into v_job;
  return v_job;
end;
$$;

-- Failure finalization may not manufacture a negative reconciliation for a
-- quarantined provider attempt. The dedicated NOT_SENT RPC owns that path.
create or replace function public.finalize_ai_job_failure_with_lease(
  p_job_id uuid, p_terminal_status public.ai_job_status,
  p_error_code text, p_error_message text, p_provider_reconciled boolean,
  p_lease_owner text, p_lease_token text, p_lease_generation bigint
)
returns public.ai_jobs
language plpgsql security definer set search_path = ''
as $$
declare v_job public.ai_jobs;
begin
  if p_terminal_status not in ('FAILED', 'EXPIRED', 'CANCELLED') then
    raise exception using errcode = '22023', message = 'INVALID_RELEASE_STATUS';
  end if;
  select * into v_job from public.assert_ai_job_lease(
    p_job_id, p_lease_owner, p_lease_token, p_lease_generation);
  if v_job.provider_quarantined_at is not null then
    raise exception using errcode = 'P0001', message = 'AI_PROVIDER_QUARANTINED';
  end if;
  if p_provider_reconciled is true and v_job.openai_response_id is null then
    raise exception using errcode = 'P0001', message = 'PROVIDER_RESPONSE_ID_REQUIRED';
  end if;
  update public.ai_jobs set provider_reconciled_at = now(), provider_result_recoverable = p_provider_reconciled
  where id = v_job.id;
  select * into v_job from public.release_ai_job_reservation(
    v_job.id, p_terminal_status, p_error_code, p_error_message);
  return v_job;
end;
$$;

create or replace function public.ai_job_transition_guard()
returns trigger language plpgsql
as $$
declare
  internal_transition boolean := coalesce(current_setting('ai.internal_job_transition', true), '') = '1';
  provider_backfill boolean := current_user = 'postgres'
    and coalesce(current_setting('ai.internal_provider_backfill', true), '') = '1';
begin
  new.updated_at = case when provider_backfill then old.updated_at else now() end;
  if old.status in ('SUCCEEDED', 'FAILED', 'EXPIRED', 'CANCELLED') and new is distinct from old then
    if provider_backfill
       and (to_jsonb(new) - 'provider_start_outcome' - 'provider_quarantined_at' - 'provider_result_recoverable' - 'updated_at')
         = (to_jsonb(old) - 'provider_start_outcome' - 'provider_quarantined_at' - 'provider_result_recoverable' - 'updated_at') then
      return new;
    end if;
    raise exception using errcode = 'P0001', message = 'TERMINAL_JOB_IMMUTABLE';
  end if;
  if old.status = new.status then return new; end if;
  if not internal_transition then
    raise exception using errcode = 'P0001', message = 'JOB_STATUS_TRANSITION_REQUIRES_RPC';
  end if;
  if old.status = 'RESERVED' and new.status = 'PROCESSING' then
    if new.source_object_path is null or new.source_hash is null then
      raise exception using errcode = 'P0001', message = 'SOURCE_NOT_BOUND';
    end if;
  elsif old.status = 'RESERVED' and new.status = 'CANCELLED' then
    if old.provider_start_outcome <> 'NOT_STARTED' or old.provider_quarantined_at is not null
       or old.openai_response_id is not null or new.provider_start_outcome <> 'NOT_STARTED'
       or new.provider_quarantined_at is not null or new.openai_response_id is not null then
      raise exception using errcode = 'P0001', message = 'CANCELLATION_PROVIDER_STARTED';
    end if;
  elsif old.status = 'PROCESSING' and new.status = 'SUCCEEDED' then
    if new.proposal is null or jsonb_typeof(new.proposal) <> 'object' then
      raise exception using errcode = 'P0001', message = 'SUCCESS_PROPOSAL_REQUIRED';
    end if;
  elsif old.status = 'PROCESSING' and new.status = 'CANCELLED' then
    if new.provider_reconciled_at is null or new.provider_result_recoverable is not false then
      raise exception using errcode = 'P0001', message = 'CANCELLATION_RECONCILIATION_REQUIRED';
    end if;
  elsif old.status = 'PROCESSING' and new.status in ('FAILED', 'EXPIRED') then
    if new.provider_reconciled_at is null or new.provider_result_recoverable is not false then
      if coalesce(new.provider_result_recoverable, false) then
        raise exception using errcode = 'P0001', message = 'PROVIDER_RESULT_RECOVERABLE';
      end if;
      raise exception using errcode = 'P0001', message = 'PROVIDER_RECONCILIATION_REQUIRED';
    end if;
  else
    raise exception using errcode = 'P0001', message = 'INVALID_JOB_STATE_TRANSITION';
  end if;
  if old.provider_quarantined_at is not null and new.provider_quarantined_at is not null then
    raise exception using errcode = 'P0001', message = 'CANCELLATION_RECONCILIATION_REQUIRED';
  end if;
  return new;
end;
$$;
