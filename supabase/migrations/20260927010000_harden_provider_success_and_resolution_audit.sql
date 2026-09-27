-- Preserve compatibility with Edge Functions still deployed from before the
-- provider-state migration: successful finalization is the point at which a
-- valid provider response ID becomes canonical ACCEPTED state.
create function public.ai_jobs_accept_provider_before_success()
returns trigger
language plpgsql
set search_path = ''
as $$
begin
  if new.status = 'SUCCEEDED' and old.status <> 'SUCCEEDED' then
    if old.provider_quarantined_at is not null
       or old.provider_start_outcome not in ('NOT_STARTED', 'ACCEPTED')
       or new.openai_response_id is null
       or (old.openai_response_id is not null and new.openai_response_id is distinct from old.openai_response_id) then
      raise exception using errcode = '23514', message = 'AI_PROVIDER_ACCEPTANCE_REQUIRED';
    end if;
    new.provider_start_outcome := 'ACCEPTED';
    new.provider_quarantined_at := null;
    new.provider_execution_started_at := coalesce(old.provider_execution_started_at, now());
  end if;
  return new;
end;
$$;

revoke all on function public.ai_jobs_accept_provider_before_success() from public, anon, authenticated, service_role;
create trigger ai_jobs_accept_provider_before_success
before update on public.ai_jobs
for each row execute function public.ai_jobs_accept_provider_before_success();

-- The original resolver returns a full ai_jobs composite. Keep its transaction
-- and validation behavior private, but minimize both persisted audit data and
-- its public (service-role-only) RPC result to an explicit allowlist.
create function public.ai_job_provider_resolution_minimize_snapshot()
returns trigger
language plpgsql
security definer
set search_path = ''
as $$
declare
  v_snapshot jsonb := new.result_snapshot;
begin
  new.result_snapshot := jsonb_build_object(
    'job_id', coalesce(v_snapshot -> 'job_id', v_snapshot -> 'id'),
    'status', v_snapshot -> 'status',
    'openai_response_id', v_snapshot -> 'openai_response_id',
    'provider_start_outcome', v_snapshot -> 'provider_start_outcome',
    'provider_reconciled_at', v_snapshot -> 'provider_reconciled_at',
    'finished_at', v_snapshot -> 'finished_at'
  );
  return new;
end;
$$;

revoke all on function public.ai_job_provider_resolution_minimize_snapshot() from public, anon, authenticated, service_role;
create trigger ai_job_provider_resolution_minimize_snapshot
before insert or update on public.ai_job_provider_resolution_audit
for each row execute function public.ai_job_provider_resolution_minimize_snapshot();

update public.ai_job_provider_resolution_audit
set result_snapshot = jsonb_build_object(
  'job_id', coalesce(result_snapshot -> 'job_id', result_snapshot -> 'id'),
  'status', result_snapshot -> 'status',
  'openai_response_id', result_snapshot -> 'openai_response_id',
  'provider_start_outcome', result_snapshot -> 'provider_start_outcome',
  'provider_reconciled_at', result_snapshot -> 'provider_reconciled_at',
  'finished_at', result_snapshot -> 'finished_at'
);

alter function public.resolve_ai_job_provider_quarantine(uuid, uuid, text, jsonb, text, text)
  rename to resolve_ai_job_provider_quarantine_internal;
revoke all on function public.resolve_ai_job_provider_quarantine_internal(uuid, uuid, text, jsonb, text, text)
  from public, anon, authenticated, service_role;

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
  v_result jsonb;
begin
  v_result := public.resolve_ai_job_provider_quarantine_internal(
    p_job_id, p_resolution_id, p_decision, p_payload,
    p_operator_reference, p_evidence_reference
  );
  return jsonb_build_object(
    'job_id', coalesce(v_result -> 'job_id', v_result -> 'id'),
    'status', v_result -> 'status',
    'openai_response_id', v_result -> 'openai_response_id',
    'provider_start_outcome', v_result -> 'provider_start_outcome',
    'provider_reconciled_at', v_result -> 'provider_reconciled_at',
    'finished_at', v_result -> 'finished_at'
  );
end;
$$;

revoke all on function public.resolve_ai_job_provider_quarantine(uuid, uuid, text, jsonb, text, text)
  from public, anon, authenticated, service_role;
grant execute on function public.resolve_ai_job_provider_quarantine(uuid, uuid, text, jsonb, text, text)
  to service_role;
