-- Keep the old create/reserve INSERT compatible throughout the Edge rollout.
-- DDL and backfill commit atomically; no client observes the intermediate rows.
alter table public.ai_jobs
  add column provider_start_outcome text not null default 'NOT_STARTED',
  add column provider_quarantined_at timestamptz;

create table public.ai_job_provider_reconciliation_queue (
  job_id uuid primary key references public.ai_jobs (id) on delete cascade,
  status text not null default 'PENDING',
  attempt_count integer not null default 0,
  available_at timestamptz not null default now(),
  deadline_at timestamptz,
  lease_owner text,
  lease_token text,
  lease_generation bigint not null default 0,
  lease_expires_at timestamptz,
  last_error_code text,
  created_at timestamptz not null default now(),
  updated_at timestamptz not null default now(),
  constraint ai_job_provider_reconciliation_queue_status_check check (status in ('PENDING', 'DONE')),
  constraint ai_job_provider_reconciliation_queue_attempt_check check (attempt_count >= 0),
  constraint ai_job_provider_reconciliation_queue_generation_check check (lease_generation >= 0)
);

create table public.ai_job_provider_resolution_audit (
  resolution_id uuid primary key,
  job_id uuid not null references public.ai_jobs (id),
  decision text not null,
  decision_fingerprint text not null,
  technical_actor text not null,
  operator_reference text,
  evidence_reference text,
  provider_response_id text,
  provider_status text,
  result_status public.ai_job_status not null,
  created_at timestamptz not null default now(),
  constraint ai_job_provider_resolution_audit_decision_check check (
    decision in ('CONFIRM_NOT_CREATED', 'ATTACH_RESPONSE_ID', 'CONFIRM_TERMINAL_NO_RESULT')
  ),
  constraint ai_job_provider_resolution_audit_fingerprint_check check (length(decision_fingerprint) = 64),
  constraint ai_job_provider_resolution_audit_operator_check check (
    operator_reference is null or (length(operator_reference) between 1 and 128 and operator_reference ~ '^[A-Za-z0-9._:@/-]+$')
  ),
  constraint ai_job_provider_resolution_audit_evidence_check check (
    evidence_reference is null or (length(evidence_reference) between 1 and 256 and evidence_reference ~ '^[A-Za-z0-9._:@/-]+$')
  )
);

alter table public.ai_job_provider_reconciliation_queue enable row level security;
alter table public.ai_job_provider_resolution_audit enable row level security;

-- Preserve the existing transition rules. Permit only the maintenance
-- classifier to update the three provider evidence columns on terminal rows.
create or replace function public.ai_job_transition_guard()
returns trigger
language plpgsql
as $$
declare
  internal_transition boolean := coalesce(current_setting('ai.internal_job_transition', true), '') = '1';
  provider_backfill boolean := current_user = 'postgres'
    and coalesce(current_setting('ai.internal_provider_backfill', true), '') = '1';
begin
  new.updated_at = case when provider_backfill then old.updated_at else now() end;
  if old.status in ('SUCCEEDED', 'FAILED', 'EXPIRED', 'CANCELLED')
     and new is distinct from old then
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
    if old.provider_execution_started_at is not null or old.openai_response_id is not null
       or new.provider_execution_started_at is not null or new.openai_response_id is not null then
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

-- Owner-only maintenance function exists so pgTAP can replay legacy fixtures.
create function public.ai_backfill_provider_start_outcome()
returns void
language plpgsql
security invoker
set search_path = public
as $$
begin
  if current_user <> 'postgres' then
    raise exception using errcode = '42501', message = 'AI_PROVIDER_BACKFILL_FORBIDDEN';
  end if;

  perform set_config('ai.internal_provider_backfill', '1', true);
  update public.ai_jobs
  set provider_start_outcome = case
        when openai_response_id is not null then 'ACCEPTED'
        when provider_execution_started_at is null then 'NOT_STARTED'
        else 'LEGACY_AMBIGUOUS'
      end,
      provider_quarantined_at = case
        when openai_response_id is null
          and provider_execution_started_at is not null
          and status in ('RESERVED', 'PROCESSING')
        then coalesce(provider_quarantined_at, now())
        else null
      end,
      provider_result_recoverable = case
        when openai_response_id is not null then provider_result_recoverable
        else null
      end
  where provider_start_outcome = 'NOT_STARTED'
    and (openai_response_id is not null
      or provider_execution_started_at is not null
      or provider_quarantined_at is not null
      or provider_result_recoverable is not null);
  perform set_config('ai.internal_provider_backfill', '0', true);
end;
$$;

select public.ai_backfill_provider_start_outcome();

alter table public.ai_jobs
  add constraint ai_jobs_provider_start_outcome_check
  check (provider_start_outcome in (
    'NOT_STARTED', 'IN_FLIGHT', 'NOT_SENT', 'PROVIDER_REJECTED',
    'TRANSPORT_AMBIGUOUS', 'RESPONSE_AMBIGUOUS', 'ACCEPTED', 'LEGACY_AMBIGUOUS'
  )) not valid;
alter table public.ai_jobs
  add constraint ai_jobs_provider_state_check
  check (
    (provider_start_outcome <> 'NOT_STARTED' or (openai_response_id is null and provider_quarantined_at is null))
    and (provider_start_outcome <> 'ACCEPTED' or (openai_response_id is not null and provider_quarantined_at is null))
    and (provider_start_outcome not in ('IN_FLIGHT', 'PROVIDER_REJECTED', 'TRANSPORT_AMBIGUOUS', 'RESPONSE_AMBIGUOUS', 'LEGACY_AMBIGUOUS')
      or status in ('SUCCEEDED', 'FAILED', 'EXPIRED', 'CANCELLED')
      or (provider_quarantined_at is not null and openai_response_id is null))
    and (provider_start_outcome <> 'NOT_SENT' or (status in ('FAILED', 'CANCELLED') and provider_quarantined_at is null and openai_response_id is null))
    and (status not in ('SUCCEEDED', 'FAILED', 'EXPIRED', 'CANCELLED') or provider_quarantined_at is null)
  ) not valid;
alter table public.ai_jobs
  add constraint ai_jobs_provider_recoverable_id_check
  check (provider_result_recoverable is not true or openai_response_id is not null) not valid;
alter table public.ai_jobs validate constraint ai_jobs_provider_start_outcome_check;
alter table public.ai_jobs validate constraint ai_jobs_provider_state_check;
alter table public.ai_jobs validate constraint ai_jobs_provider_recoverable_id_check;

create index ai_job_provider_reconciliation_pending_idx
  on public.ai_job_provider_reconciliation_queue (available_at, lease_expires_at, created_at)
  where status = 'PENDING';
create index ai_job_provider_resolution_audit_job_idx
  on public.ai_job_provider_resolution_audit (job_id, created_at);

-- Old Edge calls these exact signatures. Preserve the request/return shapes
-- while preventing another provider start for uncertain or cancelled jobs.
create or replace function public.claim_ai_job(
  p_job_id uuid,
  p_lease_owner text,
  p_lease_seconds integer default 300
)
returns public.ai_jobs
language plpgsql
security definer
set search_path = public
as $$
declare v_job public.ai_jobs;
begin
  if p_lease_owner is null or length(btrim(p_lease_owner)) = 0 or p_lease_seconds <= 0 then
    raise exception using errcode = '22023', message = 'INVALID_JOB_LEASE';
  end if;
  select * into v_job from public.ai_jobs where id = p_job_id for update;
  if not found then raise exception using errcode = 'P0001', message = 'AI_JOB_NOT_FOUND'; end if;
  perform public.ai_assert_job_owner(v_job.user_id);
  if v_job.status <> 'RESERVED' then
    raise exception using errcode = 'P0001', message = 'AI_JOB_NOT_RESERVABLE';
  end if;
  if v_job.cancellation_requested_at is not null or v_job.provider_quarantined_at is not null
     or v_job.provider_start_outcome <> 'NOT_STARTED' or v_job.openai_response_id is not null then
    raise exception using errcode = 'P0001', message = 'AI_JOB_NOT_RESERVABLE';
  end if;
  if v_job.source_object_path is null or v_job.source_hash is null then
    raise exception using errcode = 'P0001', message = 'SOURCE_NOT_BOUND';
  end if;
  perform set_config('ai.internal_job_transition', '1', true);
  update public.ai_jobs
  set status = 'PROCESSING', lease_owner = p_lease_owner,
      lease_expires_at = now() + make_interval(secs => p_lease_seconds),
      processing_started_at = coalesce(processing_started_at, now())
  where id = p_job_id returning * into v_job;
  perform set_config('ai.internal_job_transition', '0', true);
  return v_job;
end;
$$;

create or replace function public.claim_ai_syllabus_worker_job(
  p_lease_owner text, p_lease_token text,
  p_lease_seconds integer default 300, p_processing_seconds integer default 900
)
returns public.ai_jobs
language plpgsql
security definer
set search_path = public
as $$
declare v_job public.ai_jobs;
begin
  if coalesce(auth.role(), '') not in ('service_role', 'supabase_admin') then
    raise exception using errcode = '42501', message = 'AI_WORKER_FORBIDDEN';
  end if;
  if p_lease_owner is null or length(btrim(p_lease_owner)) = 0
     or p_lease_token is null or length(btrim(p_lease_token)) = 0
     or p_lease_seconds <= 0 or p_processing_seconds <= 0 then
    raise exception using errcode = '22023', message = 'INVALID_JOB_LEASE';
  end if;
  select * into v_job from public.ai_jobs
  where status = 'PROCESSING'
    and cancellation_requested_at is null
    and provider_quarantined_at is null
    and provider_start_outcome in ('NOT_STARTED', 'ACCEPTED')
    and (lease_token is null or lease_expires_at is null or lease_expires_at <= now())
    and source_object_path is not null and source_hash is not null
  order by created_at for update skip locked limit 1;
  if not found then return null; end if;
  update public.ai_jobs
  set lease_owner = p_lease_owner, lease_token = p_lease_token,
      lease_generation = lease_generation + 1,
      lease_expires_at = now() + make_interval(secs => p_lease_seconds),
      processing_deadline_at = coalesce(processing_deadline_at, now() + make_interval(secs => p_processing_seconds))
  where id = v_job.id returning * into v_job;
  return v_job;
end;
$$;

create or replace function public.mark_ai_job_provider_execution_started(
  p_job_id uuid, p_lease_owner text, p_lease_token text, p_lease_generation bigint
)
returns public.ai_jobs
language plpgsql
security definer
set search_path = public
as $$
declare v_job public.ai_jobs;
begin
  select * into v_job from public.assert_ai_job_lease(p_job_id, p_lease_owner, p_lease_token, p_lease_generation);
  if v_job.cancellation_requested_at is not null then
    raise exception using errcode = 'P0001', message = 'AI_JOB_CANCELLATION_PENDING';
  end if;
  if v_job.provider_start_outcome <> 'NOT_STARTED'
     or v_job.provider_quarantined_at is not null or v_job.openai_response_id is not null then
    raise exception using errcode = 'P0001', message = 'AI_PROVIDER_START_NOT_ALLOWED';
  end if;
  update public.ai_jobs
  set provider_execution_started_at = coalesce(provider_execution_started_at, now()),
      provider_start_outcome = 'IN_FLIGHT', provider_quarantined_at = now()
  where id = v_job.id returning * into v_job;
  return v_job;
end;
$$;

create or replace function public.persist_ai_job_provider_response(
  p_job_id uuid, p_response_id text, p_lease_owner text,
  p_lease_token text, p_lease_generation bigint
)
returns public.ai_jobs
language plpgsql
security definer
set search_path = public
as $$
declare v_job public.ai_jobs;
begin
  if p_response_id is null or length(btrim(p_response_id)) = 0 then
    raise exception using errcode = '22023', message = 'INVALID_PROVIDER_RESPONSE_ID';
  end if;
  select * into v_job from public.assert_ai_job_lease(p_job_id, p_lease_owner, p_lease_token, p_lease_generation);
  if v_job.openai_response_id is not null then
    if v_job.openai_response_id = p_response_id and v_job.provider_start_outcome = 'ACCEPTED' then
      return v_job;
    end if;
    raise exception using errcode = 'P0001', message = 'PROVIDER_RESPONSE_ID_CONFLICT';
  end if;
  if v_job.provider_start_outcome not in ('IN_FLIGHT', 'RESPONSE_AMBIGUOUS')
     or v_job.provider_quarantined_at is null then
    raise exception using errcode = 'P0001', message = 'AI_PROVIDER_RESPONSE_NOT_EXPECTED';
  end if;
  update public.ai_jobs
  set openai_response_id = p_response_id,
      provider_execution_started_at = coalesce(provider_execution_started_at, now()),
      provider_start_outcome = 'ACCEPTED', provider_quarantined_at = null
  where id = v_job.id returning * into v_job;
  return v_job;
end;
$$;

-- Final ACL statements: new tables are private, active backend RPCs keep their
-- exact signatures, owner RPCs retain their deliberate authenticated grants.
revoke all on public.ai_job_provider_reconciliation_queue from public, anon, authenticated, service_role;
revoke all on public.ai_job_provider_resolution_audit from public, anon, authenticated, service_role;
revoke all on function public.ai_backfill_provider_start_outcome() from public, anon, authenticated, service_role;
revoke all on function public.claim_ai_job(uuid, text, integer) from public, anon, authenticated, service_role;
revoke all on function public.claim_ai_syllabus_worker_job(text, text, integer, integer) from public, anon, authenticated, service_role;
revoke all on function public.mark_ai_job_provider_execution_started(uuid, text, text, bigint) from public, anon, authenticated, service_role;
revoke all on function public.persist_ai_job_provider_response(uuid, text, text, text, bigint) from public, anon, authenticated, service_role;
grant execute on function public.claim_ai_job(uuid, text, integer) to service_role;
grant execute on function public.claim_ai_syllabus_worker_job(text, text, integer, integer) to service_role;
grant execute on function public.mark_ai_job_provider_execution_started(uuid, text, text, bigint) to service_role;
grant execute on function public.persist_ai_job_provider_response(uuid, text, text, text, bigint) to service_role;
revoke all on function public.claim_ai_syllabus_worker_job(text, integer) from public, anon, authenticated, service_role;
revoke all on function public.record_ai_job_provider_reconciliation(uuid, boolean) from public, anon, authenticated, service_role;
revoke all on function public.get_ai_syllabus_source_metadata(uuid, text) from public, anon, authenticated, service_role;
grant execute on function public.get_ai_syllabus_source_metadata(uuid, text) to service_role;
revoke all on function public.create_or_get_ai_job_and_reserve_quota(public.ai_feature, text, text, jsonb) from public, anon, authenticated, service_role;
grant execute on function public.create_or_get_ai_job_and_reserve_quota(public.ai_feature, text, text, jsonb) to authenticated, service_role;
revoke all on function public.request_ai_job_cancellation(uuid) from public, anon, authenticated, service_role;
grant execute on function public.request_ai_job_cancellation(uuid) to authenticated, service_role;
revoke all on function public.cancel_ai_job_without_provider(uuid) from public, anon, authenticated, service_role;
grant execute on function public.cancel_ai_job_without_provider(uuid) to authenticated, service_role;
