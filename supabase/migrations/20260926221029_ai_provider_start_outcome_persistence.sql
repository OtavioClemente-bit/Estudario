-- Persist provider delivery diagnostics without changing the quarantine or quota.
create function public.record_ai_job_provider_start_outcome(
  p_job_id uuid, p_outcome text,
  p_lease_owner text, p_lease_token text, p_lease_generation bigint
)
returns public.ai_jobs
language plpgsql security definer set search_path = ''
as $$
declare v_job public.ai_jobs;
begin
  if p_outcome is null or p_outcome not in ('PROVIDER_REJECTED', 'TRANSPORT_AMBIGUOUS', 'RESPONSE_AMBIGUOUS') then
    raise exception using errcode = '22023', message = 'INVALID_PROVIDER_START_OUTCOME';
  end if;
  select * into v_job from public.assert_ai_job_lease(
    p_job_id, p_lease_owner, p_lease_token, p_lease_generation);
  if v_job.provider_start_outcome = p_outcome and v_job.provider_quarantined_at is not null
     and v_job.openai_response_id is null then
    return v_job;
  end if;
  if v_job.status <> 'PROCESSING' or v_job.provider_start_outcome <> 'IN_FLIGHT'
     or v_job.provider_quarantined_at is null or v_job.openai_response_id is not null then
    raise exception using errcode = 'P0001', message = 'AI_PROVIDER_OUTCOME_NOT_RECORDABLE';
  end if;
  update public.ai_jobs set provider_start_outcome = p_outcome
    where id = p_job_id returning * into v_job;
  return v_job;
end;
$$;

revoke all on function public.record_ai_job_provider_start_outcome(uuid, text, text, text, bigint)
  from public, anon, authenticated, service_role;
grant execute on function public.record_ai_job_provider_start_outcome(uuid, text, text, text, bigint)
  to service_role;
