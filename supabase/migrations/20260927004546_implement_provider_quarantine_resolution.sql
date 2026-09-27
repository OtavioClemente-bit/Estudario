alter table public.ai_job_provider_resolution_audit
  add column result_snapshot jsonb;

update public.ai_job_provider_resolution_audit a
set result_snapshot = to_jsonb(j)
from public.ai_jobs j
where j.id = a.job_id and a.result_snapshot is null;

alter table public.ai_job_provider_resolution_audit
  alter column result_snapshot set not null;

create function public.resolve_ai_job_provider_quarantine(
  p_job_id uuid,
  p_resolution_id uuid,
  p_decision text,
  p_payload jsonb,
  p_operator_reference text default null,
  p_evidence_reference text default null
)
returns jsonb
language plpgsql
security definer
set search_path = ''
as $$
declare
  v_job public.ai_jobs;
  v_reservation public.ai_quota_reservations;
  v_usage public.ai_quota_usage;
  v_existing public.ai_job_provider_resolution_audit;
  v_result_status public.ai_job_status;
  v_error_code text;
  v_provider_response_id text;
  v_provider_status text;
  v_normalized_payload jsonb;
  v_fingerprint text;
  v_result_snapshot jsonb;
  v_was_quarantined boolean;
  v_live_generation_lease boolean;
  v_live_reconciliation_lease boolean;
begin
  if coalesce(auth.role(), '') not in ('service_role', 'supabase_admin') then
    raise exception using errcode = '42501', message = 'AI_PROVIDER_RESOLUTION_FORBIDDEN';
  end if;
  if p_job_id is null or p_resolution_id is null or p_decision is null
     or p_decision not in ('CONFIRM_NOT_CREATED', 'ATTACH_RESPONSE_ID', 'CONFIRM_TERMINAL_NO_RESULT')
     or p_payload is null or jsonb_typeof(p_payload) <> 'object' then
    raise exception using errcode = '22023', message = 'INVALID_AI_PROVIDER_RESOLUTION';
  end if;
  if p_operator_reference is not null and (
    length(btrim(p_operator_reference)) not between 1 and 128
    or btrim(p_operator_reference) !~ '^[A-Za-z0-9._:@/-]+$'
  ) then
    raise exception using errcode = 'P0001', message = 'INVALID_OPERATOR_REFERENCE';
  end if;
  if p_evidence_reference is not null and (
    length(btrim(p_evidence_reference)) not between 1 and 256
    or btrim(p_evidence_reference) !~ '^[A-Za-z0-9._:@/-]+$'
    or btrim(p_evidence_reference) ~* '^[A-Za-z][A-Za-z0-9+.-]*://'
  ) then
    raise exception using errcode = 'P0001', message = 'INVALID_EVIDENCE_REFERENCE';
  end if;

  p_operator_reference := nullif(btrim(p_operator_reference), '');
  p_evidence_reference := nullif(btrim(p_evidence_reference), '');

  if p_decision = 'CONFIRM_NOT_CREATED' then
    if p_payload <> '{}'::jsonb then
      raise exception using errcode = '22023', message = 'INVALID_AI_PROVIDER_RESOLUTION_PAYLOAD';
    end if;
    if p_evidence_reference is null then
      raise exception using errcode = 'P0001', message = 'AI_RESOLUTION_EVIDENCE_REQUIRED';
    end if;
    v_normalized_payload := '{}'::jsonb;
    v_provider_status := 'not_created';
  elsif p_decision = 'ATTACH_RESPONSE_ID' then
    if p_payload - array['response_id']::text[] <> '{}'::jsonb
       or jsonb_typeof(p_payload -> 'response_id') is distinct from 'string' then
      raise exception using errcode = '22023', message = 'INVALID_AI_PROVIDER_RESOLUTION_PAYLOAD';
    end if;
    v_provider_response_id := btrim(p_payload ->> 'response_id');
    if length(v_provider_response_id) not between 1 and 256 or v_provider_response_id !~ '^[A-Za-z0-9_-]+$' then
      raise exception using errcode = '22023', message = 'INVALID_PROVIDER_RESPONSE_ID';
    end if;
    v_normalized_payload := jsonb_build_object('response_id', v_provider_response_id);
  else
    if p_payload - array['response_id', 'provider_status']::text[] <> '{}'::jsonb
       or jsonb_typeof(p_payload -> 'response_id') is distinct from 'string'
       or jsonb_typeof(p_payload -> 'provider_status') is distinct from 'string' then
      raise exception using errcode = '22023', message = 'INVALID_AI_PROVIDER_RESOLUTION_PAYLOAD';
    end if;
    v_provider_response_id := btrim(p_payload ->> 'response_id');
    v_provider_status := lower(btrim(p_payload ->> 'provider_status'));
    if length(v_provider_response_id) not between 1 and 256 or v_provider_response_id !~ '^[A-Za-z0-9_-]+$'
       or v_provider_status not in ('failed', 'cancelled', 'expired', 'incomplete') then
      raise exception using errcode = '22023', message = 'INVALID_PROVIDER_TERMINAL_EVIDENCE';
    end if;
    if p_evidence_reference is null then
      raise exception using errcode = 'P0001', message = 'AI_RESOLUTION_EVIDENCE_REQUIRED';
    end if;
    v_normalized_payload := jsonb_build_object(
      'response_id', v_provider_response_id,
      'provider_status', v_provider_status
    );
  end if;

  v_fingerprint := encode(extensions.digest(convert_to(jsonb_build_object(
    'job_id', p_job_id,
    'decision', p_decision,
    'payload', v_normalized_payload,
    'operator_reference', p_operator_reference,
    'evidence_reference', p_evidence_reference
  )::text, 'UTF8'), 'sha256'), 'hex');

  perform pg_catalog.pg_advisory_xact_lock(pg_catalog.hashtextextended(p_resolution_id::text, 0));
  select * into v_existing from public.ai_job_provider_resolution_audit
  where resolution_id = p_resolution_id;
  if found then
    if v_existing.job_id <> p_job_id or v_existing.decision <> p_decision
       or v_existing.decision_fingerprint <> v_fingerprint then
      raise exception using errcode = 'P0001', message = 'AI_RESOLUTION_ID_CONFLICT';
    end if;
    return v_existing.result_snapshot;
  end if;

  select * into v_job from public.ai_jobs where id = p_job_id for update;
  if not found then
    raise exception using errcode = 'P0001', message = 'AI_JOB_NOT_FOUND';
  end if;
  if v_job.status <> 'PROCESSING' then
    raise exception using errcode = 'P0001', message = 'AI_PROVIDER_RESOLUTION_NOT_PROCESSING';
  end if;
  v_was_quarantined := v_job.provider_quarantined_at is not null;
  v_live_generation_lease := v_job.lease_token is not null and v_job.lease_expires_at > now();
  select exists (
    select 1 from public.ai_job_provider_reconciliation_queue q
    where q.job_id = p_job_id and q.lease_token is not null and q.lease_expires_at > now()
  ) into v_live_reconciliation_lease;

  if p_decision = 'CONFIRM_NOT_CREATED' then
    if not v_was_quarantined or v_job.openai_response_id is not null then
      raise exception using errcode = 'P0001', message = 'AI_PROVIDER_RESOLUTION_NOT_IN_QUARANTINE';
    end if;
    if v_live_generation_lease or v_live_reconciliation_lease then
      raise exception using errcode = 'P0001', message = 'AI_PROVIDER_RESOLUTION_PENDING';
    end if;
    v_result_status := case when v_job.cancellation_requested_at is not null
      then 'CANCELLED'::public.ai_job_status else 'FAILED'::public.ai_job_status end;
    v_error_code := 'PROVIDER_NOT_CREATED';
  elsif p_decision = 'ATTACH_RESPONSE_ID' then
    if v_job.openai_response_id is not null and v_job.openai_response_id <> v_provider_response_id then
      raise exception using errcode = 'P0001', message = 'PROVIDER_RESPONSE_ID_CONFLICT';
    end if;
    if not v_was_quarantined and not (
      v_job.provider_start_outcome = 'ACCEPTED'
      and v_job.openai_response_id = v_provider_response_id
    ) then
      raise exception using errcode = 'P0001', message = 'AI_PROVIDER_RESOLUTION_NOT_IN_QUARANTINE';
    end if;
    v_result_status := 'PROCESSING';
  else
    if v_job.openai_response_id is distinct from v_provider_response_id
       or v_job.provider_start_outcome <> 'ACCEPTED' then
      raise exception using errcode = 'P0001', message = 'PROVIDER_RESPONSE_ID_CONFLICT';
    end if;
    v_result_status := case v_provider_status
      when 'failed' then 'FAILED'::public.ai_job_status
      when 'cancelled' then 'CANCELLED'::public.ai_job_status
      when 'expired' then 'EXPIRED'::public.ai_job_status
      else 'FAILED'::public.ai_job_status
    end;
    v_error_code := case v_provider_status
      when 'failed' then 'PROVIDER_RESULT_UNAVAILABLE'
      when 'cancelled' then 'PROVIDER_CANCELLED'
      when 'expired' then 'PROVIDER_EXPIRED'
      else 'PROVIDER_INCOMPLETE'
    end;
    -- A provider-confirmed terminal state for the same response ID is the
    -- sole exception to the live-lease guard: that response cannot change.
  end if;

  if p_decision <> 'ATTACH_RESPONSE_ID' then
    select * into v_reservation from public.ai_quota_reservations
    where job_id = p_job_id for update;
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
    update public.ai_jobs set status = v_result_status,
      provider_quarantined_at = null,
      provider_reconciled_at = now(), provider_result_recoverable = false,
      cancellation_reconciled_at = case when cancellation_requested_at is not null then now() else cancellation_reconciled_at end,
      cancellation_provider_status = case when cancellation_requested_at is not null then v_provider_status else cancellation_provider_status end,
      cancellation_error_code = case when cancellation_requested_at is not null then null else cancellation_error_code end,
      error_code = v_error_code,
      error_message = case when v_result_status = 'CANCELLED' then 'AI provider execution was confirmed terminal' else 'The AI job did not complete' end,
      finished_at = now(), lease_owner = null, lease_token = null, lease_expires_at = null,
      lease_generation = lease_generation + 1
    where id = p_job_id returning * into v_job;
    perform set_config('ai.internal_job_transition', '0', true);

    update public.ai_quota_reservations set status = 'RELEASED', released_at = now()
    where id = v_reservation.id;
    update public.ai_quota_usage set reserved_count = reserved_count - 1
    where user_id = v_usage.user_id and feature = v_usage.feature and period_start = v_usage.period_start;
  else
    update public.ai_jobs set openai_response_id = v_provider_response_id,
      provider_start_outcome = 'ACCEPTED', provider_quarantined_at = null,
      provider_execution_started_at = coalesce(provider_execution_started_at, now()),
      processing_deadline_at = case when v_was_quarantined then null else processing_deadline_at end
    where id = p_job_id returning * into v_job;
    if v_job.cancellation_requested_at is not null
       and not exists (select 1 from public.ai_job_provider_reconciliation_queue where job_id = p_job_id) then
      insert into public.ai_job_provider_reconciliation_queue (job_id, status, available_at)
      values (p_job_id, 'PENDING', now());
    end if;
  end if;

  v_result_snapshot := to_jsonb(v_job);
  insert into public.ai_job_provider_resolution_audit (
    resolution_id, job_id, decision, decision_fingerprint, technical_actor,
    operator_reference, evidence_reference, provider_response_id, provider_status,
    result_status, result_snapshot
  ) values (
    p_resolution_id, p_job_id, p_decision, v_fingerprint, coalesce(auth.role(), 'unknown'),
    p_operator_reference, p_evidence_reference, v_provider_response_id, v_provider_status,
    v_job.status, v_result_snapshot
  );
  return v_result_snapshot;
end;
$$;

revoke all on function public.resolve_ai_job_provider_quarantine(uuid, uuid, text, jsonb, text, text) from public, anon, authenticated, service_role;
grant execute on function public.resolve_ai_job_provider_quarantine(uuid, uuid, text, jsonb, text, text) to service_role;
