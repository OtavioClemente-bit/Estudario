-- Claim only durable cancellation continuations for a known provider response.
-- The job lease fields are synchronized with the queue lease so existing
-- lease-bound finalization remains safe after reconciliation completes.
insert into public.ai_job_provider_reconciliation_queue (job_id)
select id from public.ai_jobs
where status = 'PROCESSING' and cancellation_requested_at is not null and openai_response_id is not null
on conflict (job_id) do nothing;

create function public.claim_ai_job_provider_reconciliation(
  p_lease_owner text, p_lease_token text,
  p_lease_seconds integer default 300, p_reconciliation_seconds integer default 900
)
returns public.ai_jobs
language plpgsql security definer set search_path = ''
as $$
declare
  v_job_id uuid;
  v_queue_generation bigint;
  v_job_generation bigint;
  v_generation bigint;
  v_job public.ai_jobs;
begin
  if coalesce(auth.role(), '') not in ('service_role', 'supabase_admin') then
    raise exception using errcode = '42501', message = 'AI_WORKER_FORBIDDEN';
  end if;
  if p_lease_owner is null or length(btrim(p_lease_owner)) = 0
     or p_lease_token is null or length(btrim(p_lease_token)) = 0
     or p_lease_seconds <= 0 or p_reconciliation_seconds <= 0 then
    raise exception using errcode = '22023', message = 'INVALID_RECONCILIATION_LEASE';
  end if;
  update public.ai_job_provider_reconciliation_queue q
    set status = 'DONE', lease_owner = null, lease_token = null, lease_expires_at = null, updated_at = now()
    from public.ai_jobs j
    where j.id = q.job_id and q.status = 'PENDING'
      and j.status in ('SUCCEEDED', 'FAILED', 'EXPIRED', 'CANCELLED');
  select q.job_id, q.lease_generation, j.lease_generation
    into v_job_id, v_queue_generation, v_job_generation
  from public.ai_job_provider_reconciliation_queue q
  join public.ai_jobs j on j.id = q.job_id
  where q.status = 'PENDING' and q.available_at <= now()
    and (q.lease_token is null or q.lease_expires_at is null or q.lease_expires_at <= now())
    and j.status = 'PROCESSING' and j.cancellation_requested_at is not null
    and j.openai_response_id is not null
    and (j.lease_token is null or j.lease_expires_at is null or j.lease_expires_at <= now())
  order by q.available_at, q.created_at
  for update of q, j skip locked limit 1;
  if not found then return null; end if;
  v_generation := greatest(v_queue_generation, v_job_generation) + 1;
  update public.ai_job_provider_reconciliation_queue
  set lease_owner = p_lease_owner, lease_token = p_lease_token,
      lease_generation = v_generation, lease_expires_at = now() + make_interval(secs => p_lease_seconds),
      deadline_at = coalesce(deadline_at, now() + make_interval(secs => p_reconciliation_seconds)),
      attempt_count = attempt_count + 1, updated_at = now()
  where job_id = v_job_id;
  update public.ai_jobs
  set lease_owner = p_lease_owner, lease_token = p_lease_token, lease_generation = v_generation,
      lease_expires_at = now() + make_interval(secs => p_lease_seconds)
  where id = v_job_id returning * into v_job;
  return v_job;
end;
$$;

create function public.complete_ai_job_provider_reconciliation(
  p_job_id uuid, p_lease_owner text, p_lease_token text, p_lease_generation bigint
)
returns public.ai_job_provider_reconciliation_queue
language plpgsql security definer set search_path = ''
as $$
declare v_queue public.ai_job_provider_reconciliation_queue; v_status public.ai_job_status;
begin
  if coalesce(auth.role(), '') not in ('service_role', 'supabase_admin') then
    raise exception using errcode = '42501', message = 'AI_WORKER_FORBIDDEN';
  end if;
  select * into v_queue from public.ai_job_provider_reconciliation_queue
  where job_id = p_job_id for update;
  if not found or v_queue.status <> 'PENDING' or v_queue.lease_owner is distinct from p_lease_owner
     or v_queue.lease_token is distinct from p_lease_token or v_queue.lease_generation is distinct from p_lease_generation
     or v_queue.lease_expires_at is null or v_queue.lease_expires_at <= now() then
    raise exception using errcode = 'P0001', message = 'AI_RECONCILIATION_LEASE_LOST';
  end if;
  select status into v_status from public.ai_jobs where id = p_job_id for update;
  if v_status not in ('SUCCEEDED', 'FAILED', 'EXPIRED', 'CANCELLED') then
    raise exception using errcode = 'P0001', message = 'AI_RECONCILIATION_NOT_TERMINAL';
  end if;
  update public.ai_job_provider_reconciliation_queue
  set status = 'DONE', lease_owner = null, lease_token = null, lease_expires_at = null, updated_at = now()
  where job_id = p_job_id returning * into v_queue;
  return v_queue;
end;
$$;

create function public.fail_ai_job_provider_reconciliation(
  p_job_id uuid, p_lease_owner text, p_lease_token text,
  p_lease_generation bigint, p_error_code text
)
returns public.ai_job_provider_reconciliation_queue
language plpgsql security definer set search_path = ''
as $$
declare v_queue public.ai_job_provider_reconciliation_queue;
begin
  if coalesce(auth.role(), '') not in ('service_role', 'supabase_admin') then
    raise exception using errcode = '42501', message = 'AI_WORKER_FORBIDDEN';
  end if;
  if p_error_code is null or p_error_code !~ '^[A-Z][A-Z0-9_]{2,63}$' then
    raise exception using errcode = '22023', message = 'INVALID_RECONCILIATION_ERROR_CODE';
  end if;
  select * into v_queue from public.ai_job_provider_reconciliation_queue
  where job_id = p_job_id for update;
  if not found or v_queue.status <> 'PENDING' or v_queue.lease_owner is distinct from p_lease_owner
     or v_queue.lease_token is distinct from p_lease_token or v_queue.lease_generation is distinct from p_lease_generation
     or v_queue.lease_expires_at is null or v_queue.lease_expires_at <= now() then
    raise exception using errcode = 'P0001', message = 'AI_RECONCILIATION_LEASE_LOST';
  end if;
  update public.ai_job_provider_reconciliation_queue
  set available_at = now() + make_interval(secs => case
        when deadline_at is not null and deadline_at <= now() then 900
        else least(30 * power(2, least(attempt_count - 1, 5)), 900)::integer end),
      lease_owner = null, lease_token = null, lease_expires_at = null,
      last_error_code = p_error_code, updated_at = now()
  where job_id = p_job_id returning * into v_queue;
  update public.ai_jobs set lease_owner = null, lease_token = null, lease_expires_at = null
  where id = p_job_id and lease_owner = p_lease_owner and lease_token = p_lease_token
    and lease_generation = p_lease_generation;
  return v_queue;
end;
$$;

revoke all on function public.claim_ai_job_provider_reconciliation(text, text, integer, integer) from public, anon, authenticated, service_role;
revoke all on function public.complete_ai_job_provider_reconciliation(uuid, text, text, bigint) from public, anon, authenticated, service_role;
revoke all on function public.fail_ai_job_provider_reconciliation(uuid, text, text, bigint, text) from public, anon, authenticated, service_role;
grant execute on function public.claim_ai_job_provider_reconciliation(text, text, integer, integer) to service_role;
grant execute on function public.complete_ai_job_provider_reconciliation(uuid, text, text, bigint) to service_role;
grant execute on function public.fail_ai_job_provider_reconciliation(uuid, text, text, bigint, text) to service_role;
