alter table public.ai_jobs
  add column retry_count integer not null default 0,
  add column provider_input_tokens bigint,
  add column provider_output_tokens bigint,
  add column provider_total_tokens bigint;

alter table public.ai_jobs
  add constraint ai_jobs_retry_count_check check (retry_count >= 0),
  add constraint ai_jobs_provider_input_tokens_check check (provider_input_tokens is null or provider_input_tokens >= 0),
  add constraint ai_jobs_provider_output_tokens_check check (provider_output_tokens is null or provider_output_tokens >= 0),
  add constraint ai_jobs_provider_total_tokens_check check (provider_total_tokens is null or provider_total_tokens >= 0);

create or replace function public.claim_ai_syllabus_worker_job(
  p_lease_owner text,
  p_lease_seconds integer default 300
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
  if p_lease_owner is null or length(btrim(p_lease_owner)) = 0 or p_lease_seconds <= 0 then
    raise exception using errcode = '22023', message = 'INVALID_JOB_LEASE';
  end if;

  select * into v_job
  from public.ai_jobs
  where status = 'PROCESSING'
    and (lease_expires_at is null or lease_expires_at <= now())
    and source_object_path is not null
    and source_hash is not null
  order by created_at
  for update skip locked
  limit 1;

  if not found then return null; end if;

  update public.ai_jobs
  set lease_owner = p_lease_owner,
      lease_expires_at = now() + make_interval(secs => p_lease_seconds)
  where id = v_job.id
  returning * into v_job;
  return v_job;
end;
$$;

create or replace function public.record_ai_job_provider_reconciliation(
  p_job_id uuid,
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
  if coalesce(auth.role(), '') not in ('service_role', 'supabase_admin') then
    raise exception using errcode = '42501', message = 'AI_WORKER_FORBIDDEN';
  end if;
  update public.ai_jobs
  set provider_reconciled_at = now(),
      provider_result_recoverable = p_recoverable
  where id = p_job_id
  returning * into v_job;
  if not found then raise exception using errcode = 'P0001', message = 'AI_JOB_NOT_FOUND'; end if;
  return v_job;
end;
$$;

create or replace function public.increment_ai_job_retry(p_job_id uuid)
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
  update public.ai_jobs
  set retry_count = retry_count + 1,
      lease_owner = null,
      lease_expires_at = null
  where id = p_job_id and status = 'PROCESSING'
  returning * into v_job;
  if not found then raise exception using errcode = 'P0001', message = 'AI_JOB_NOT_PROCESSING'; end if;
  return v_job;
end;
$$;

create or replace function public.record_ai_job_usage(
  p_job_id uuid,
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
  if coalesce(auth.role(), '') not in ('service_role', 'supabase_admin') then
    raise exception using errcode = '42501', message = 'AI_WORKER_FORBIDDEN';
  end if;
  if p_input_tokens is not null and p_input_tokens < 0
     or p_output_tokens is not null and p_output_tokens < 0
     or p_total_tokens is not null and p_total_tokens < 0 then
    raise exception using errcode = '22023', message = 'INVALID_PROVIDER_USAGE';
  end if;
  update public.ai_jobs
  set provider_input_tokens = p_input_tokens,
      provider_output_tokens = p_output_tokens,
      provider_total_tokens = p_total_tokens
  where id = p_job_id
  returning * into v_job;
  if not found then raise exception using errcode = 'P0001', message = 'AI_JOB_NOT_FOUND'; end if;
  return v_job;
end;
$$;

revoke all on function public.claim_ai_syllabus_worker_job(text, integer) from public;
revoke all on function public.record_ai_job_provider_reconciliation(uuid, boolean) from public;
revoke all on function public.increment_ai_job_retry(uuid) from public;
revoke all on function public.record_ai_job_usage(uuid, bigint, bigint, bigint) from public;
grant execute on function public.claim_ai_syllabus_worker_job(text, integer) to service_role;
grant execute on function public.record_ai_job_provider_reconciliation(uuid, boolean) to service_role;
grant execute on function public.increment_ai_job_retry(uuid) to service_role;
grant execute on function public.record_ai_job_usage(uuid, bigint, bigint, bigint) to service_role;
