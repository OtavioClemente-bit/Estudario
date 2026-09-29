-- Conteúdo de tópico e plano de estudo pela mesma fila de jobs do edital.
--
-- Estes dois recursos não têm PDF: a entrada é o texto que o app envia (tópico, matérias,
-- disponibilidade), guardado em request_payload. Por isso a exigência de fonte vinculada passa a
-- valer só para SYLLABUS_GENERATION; todo o resto (reserva de cota, lease, conciliação com o
-- provedor, finalização) segue as mesmas regras endurecidas.

-- 1. Cota diária também para o plano (horário de Brasília), como o conteúdo já era.
create or replace function public.ai_quota_period(p_feature public.ai_feature)
returns date
language sql
stable
as $$
  select case
    when p_feature in ('CONTENT_GENERATION'::public.ai_feature, 'PLAN_GENERATION'::public.ai_feature)
      then (now() at time zone 'America/Sao_Paulo')::date
    else date '1970-01-01'
  end;
$$;

-- 2. Guarda de transição: fonte obrigatória só para o edital. Idêntica à versão de
-- 20260926212856 em todo o resto.
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
    if new.feature = 'SYLLABUS_GENERATION'
       and (new.source_object_path is null or new.source_hash is null) then
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

-- 3. Claim pela Edge Function do dono: mesma regra, fonte só no edital.
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
  if v_job.feature = 'SYLLABUS_GENERATION'
     and (v_job.source_object_path is null or v_job.source_hash is null) then
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

-- 4. Claim do worker de texto (conteúdo e plano). Espelho do claim do edital, sem a exigência de
-- fonte e restrito a esses dois recursos, para os dois workers nunca disputarem o mesmo job.
create function public.claim_ai_text_worker_job(
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
    and feature in ('CONTENT_GENERATION', 'PLAN_GENERATION')
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

-- 5. Reconciliação de cancelamento separada por fila: cada worker só reconcilia os jobs que sabe
-- validar. Sem isso, o worker do edital poderia pegar um job de conteúdo e aplicar o schema errado.
create or replace function public.claim_ai_job_provider_reconciliation(
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
    and j.feature = 'SYLLABUS_GENERATION'
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

create function public.claim_ai_text_job_provider_reconciliation(
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
    and j.feature in ('CONTENT_GENERATION', 'PLAN_GENERATION')
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
