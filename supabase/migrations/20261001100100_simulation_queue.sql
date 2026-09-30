-- Simulado na fila de texto (segunda parte; o valor do enum já existe). Chave de liberação e as
-- duas funções de claim do worker de texto passam a incluir SIMULATION_GENERATION. As funções são
-- as mesmas de 20260928120000 com a lista de recursos ampliada, nada mais.

create or replace function public.ai_feature_flag_key(p_feature public.ai_feature)
returns text
language sql
immutable
as $$
  select case p_feature
    when 'SYLLABUS_GENERATION'::public.ai_feature then 'SYLLABUS_AI_ENABLED'
    when 'PLAN_GENERATION'::public.ai_feature then 'PLAN_AI_ENABLED'
    when 'CONTENT_GENERATION'::public.ai_feature then 'CONTENT_AI_ENABLED'
    when 'SIMULATION_GENERATION'::public.ai_feature then 'SIMULATION_AI_ENABLED'
  end;
$$;

create or replace function public.claim_ai_text_worker_job(
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
    and feature in ('CONTENT_GENERATION', 'PLAN_GENERATION', 'SIMULATION_GENERATION')
    and cancellation_requested_at is null
    and provider_quarantined_at is null
    and provider_start_outcome in ('NOT_STARTED', 'ACCEPTED')
    and (lease_token is null or lease_expires_at is null or lease_expires_at <= now())
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

revoke all on function public.claim_ai_text_worker_job(text, text, integer, integer) from public, anon, authenticated;
grant execute on function public.claim_ai_text_worker_job(text, text, integer, integer) to service_role;


create or replace function public.claim_ai_text_job_provider_reconciliation(
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
    and j.feature in ('CONTENT_GENERATION', 'PLAN_GENERATION', 'SIMULATION_GENERATION')
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

revoke all on function public.claim_ai_text_job_provider_reconciliation(text, text, integer, integer) from public, anon, authenticated;
grant execute on function public.claim_ai_text_job_provider_reconciliation(text, text, integer, integer) to service_role;
