-- A client cancellation request with a known provider response must survive
-- the HTTP request; the existing backend worker owns durable continuation.
create or replace function public.request_ai_job_cancellation(p_job_id uuid)
returns public.ai_jobs
language plpgsql
security definer
set search_path = ''
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

  if v_job.status = 'PROCESSING' and v_job.openai_response_id is not null then
    insert into public.ai_job_provider_reconciliation_queue (job_id, status, available_at)
    values (v_job.id, 'PENDING', now())
    on conflict (job_id) do nothing;
  end if;
  return v_job;
end;
$$;

revoke all on function public.request_ai_job_cancellation(uuid) from public, anon, authenticated, service_role;
grant execute on function public.request_ai_job_cancellation(uuid) to authenticated, service_role;
