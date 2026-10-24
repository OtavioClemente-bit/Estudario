-- Limites diários de matéria (25/10/2026).
--
-- Grátis: 2 matérias do plano por dia (no servidor, free-allowance.ts) e anúncio para adiantar até
-- 5 por mês. Pagos: até 4 matérias do plano por dia saem sem gastar a cota quando vêm prontas da
-- biblioteca; da 5ª em diante gastam a cota do mês. Assinante sem cota no mês ainda recebe a
-- matéria do plano pronta (pedido libraryOnly, que nunca chama a IA), dentro das 4 do dia.

-- Matérias do plano (do dia e adiantadas) recebidas hoje (horário de Brasília) sem gastar a cota.
create or replace function public.ai_plan_materials_today(p_user_id uuid)
returns integer
language sql
stable
security definer
set search_path = public
as $$
  select count(*)::integer
  from public.ai_quota_refunds r
  join public.ai_jobs j on j.id = r.job_id
  where j.user_id = p_user_id
    and coalesce(j.request_payload ->> 'purpose', '') in ('PLAN_DAY', 'ADVANCE')
    and r.created_at >= (date_trunc('day', now() at time zone 'America/Sao_Paulo') at time zone 'America/Sao_Paulo')
$$;
revoke all on function public.ai_plan_materials_today(uuid) from public, anon, authenticated;
grant execute on function public.ai_plan_materials_today(uuid) to service_role;

-- A devolução da matéria do plano: só sem IA e, no dia, até 4 (as seguintes gastam a cota do mês).
create or replace function public.ai_refund_plan_day_quota(p_job_id uuid)
returns boolean
language plpgsql
security definer
set search_path = public
as $$
declare
  v_job public.ai_jobs;
  v_period date;
begin
  if coalesce(auth.role(), '') not in ('service_role', 'supabase_admin') then
    raise exception using errcode = '42501', message = 'AI_WORKER_FORBIDDEN';
  end if;
  select * into v_job from public.ai_jobs where id = p_job_id and status = 'SUCCEEDED';
  if not found
     or v_job.feature <> 'CONTENT_GENERATION'
     or coalesce(v_job.request_payload ->> 'purpose', '') not in ('PLAN_DAY', 'ADVANCE')
     or v_job.openai_response_id is not null then
    return false;
  end if;
  -- Uma conta por vez: duas matérias terminando juntas não passam as duas pelo teto.
  perform 1 from public.profiles where user_id = v_job.user_id for update;
  if public.ai_plan_materials_today(v_job.user_id) >= 4 then
    return false;
  end if;
  select period_start into v_period from public.ai_quota_reservations where job_id = p_job_id;
  if v_period is null then
    return false;
  end if;
  insert into public.ai_quota_refunds (job_id) values (p_job_id) on conflict (job_id) do nothing;
  if not found then
    return false;
  end if;
  update public.ai_quota_usage
  set successful_count = greatest(successful_count - 1, 0)
  where user_id = v_job.user_id and feature = v_job.feature and period_start = v_period;
  return true;
end;
$$;
revoke all on function public.ai_refund_plan_day_quota(uuid) from public, anon, authenticated;
grant execute on function public.ai_refund_plan_day_quota(uuid) to service_role;

-- Anúncio para adiantar no Grátis: 2 por dia e 5 por mês (o servidor usa FREE_RULES; a tabela fica igual).
update public.ai_plan_limits set quota_limit = 5, updated_at = now()
where plan_tier = 'FREE' and feature = 'AD_REWARD' and period_kind = 'MONTHLY';

-- A reserva de cota aceita o pedido libraryOnly mesmo sem saldo (ver o comentário na conferência).
CREATE OR REPLACE FUNCTION public.create_or_get_ai_job_and_reserve_quota_unlimited(p_feature ai_feature, p_idempotency_key text, p_request_fingerprint text, p_request_payload jsonb DEFAULT '{}'::jsonb)
 RETURNS TABLE(job_id uuid, status ai_job_status, reservation_id uuid, quota_period date, quota_remaining integer, reused boolean)
 LANGUAGE plpgsql
 SECURITY DEFINER
 SET search_path TO 'public'
AS $function$
declare
  v_user_id uuid := auth.uid();
  v_job public.ai_jobs;
  v_reservation public.ai_quota_reservations;
  v_usage public.ai_quota_usage;
  v_period date := public.ai_quota_period(p_feature);
  v_limit integer := public.ai_quota_limit(p_feature);
  v_flag_key text := public.ai_feature_flag_key(p_feature);
begin
  if v_user_id is null then
    raise exception using errcode = '42501', message = 'AI_AUTH_REQUIRED';
  end if;

  select * into v_job
  from public.ai_jobs
  where user_id = v_user_id
    and feature = p_feature
    and idempotency_key = p_idempotency_key
  for update;

  if found then
    if v_job.request_fingerprint <> p_request_fingerprint then
      raise exception using errcode = 'P0001', message = 'IDEMPOTENCY_KEY_CONFLICT';
    end if;

    select * into v_reservation
    from public.ai_quota_reservations as reservation
    where reservation.job_id = v_job.id;

    if not found then
      raise exception using errcode = 'P0001', message = 'AI_JOB_RESERVATION_REQUIRED';
    end if;

    select * into v_usage
    from public.ai_quota_usage
    where user_id = v_user_id
      and feature = p_feature
      and period_start = v_period
    for update;

    return query
    select v_job.id,
           v_job.status,
           v_reservation.id,
           v_reservation.period_start,
           greatest(v_limit - v_usage.successful_count - v_usage.reserved_count, 0),
           true;
    return;
  end if;

  if not exists (
    select 1 from public.profiles where user_id = v_user_id and beta_access
  ) or not exists (
    select 1 from public.ai_feature_flags where flag_key = 'AI_BETA_ENABLED' and enabled
  ) or not exists (
    select 1 from public.ai_feature_flags where flag_key = v_flag_key and enabled
  ) then
    raise exception using errcode = '42501', message = 'AI_ACCESS_DENIED';
  end if;

  insert into public.ai_quota_usage (user_id, feature, period_start)
  values (v_user_id, p_feature, v_period)
  on conflict (user_id, feature, period_start) do nothing;

  select * into v_usage
  from public.ai_quota_usage
  where user_id = v_user_id
    and feature = p_feature
    and period_start = v_period
  for update;

  insert into public.ai_jobs (
    user_id,
    feature,
    idempotency_key,
    request_fingerprint,
    request_payload
  )
  values (
    v_user_id,
    p_feature,
    p_idempotency_key,
    p_request_fingerprint,
    coalesce(p_request_payload, '{}'::jsonb)
  )
  on conflict (user_id, feature, idempotency_key) do nothing
  returning * into v_job;

  if not found then
    select * into v_job
    from public.ai_jobs
    where user_id = v_user_id
      and feature = p_feature
      and idempotency_key = p_idempotency_key
    for update;

    if v_job.request_fingerprint <> p_request_fingerprint then
      raise exception using errcode = 'P0001', message = 'IDEMPOTENCY_KEY_CONFLICT';
    end if;

    select * into v_reservation
    from public.ai_quota_reservations as reservation
    where reservation.job_id = v_job.id;

    if not found then
      raise exception using errcode = 'P0001', message = 'AI_JOB_RESERVATION_REQUIRED';
    end if;

    return query
    select v_job.id,
           v_job.status,
           v_reservation.id,
           v_reservation.period_start,
           greatest(v_limit - v_usage.successful_count - v_usage.reserved_count, 0),
           true;
    return;
  end if;

  -- Pedido só da biblioteca (libraryOnly, posto pelo servidor: matéria do plano de assinante sem
  -- cota no mês, dentro das 4 do dia) não chama a IA: pode passar do limite, e a devolução zera.
  if v_usage.successful_count + v_usage.reserved_count >= v_limit
     and coalesce(p_request_payload ->> 'libraryOnly', '') <> 'true' then
    raise exception using errcode = 'P0001', message = 'AI_QUOTA_EXHAUSTED';
  end if;

  insert into public.ai_quota_reservations (
    job_id,
    user_id,
    feature,
    period_start
  )
  values (
    v_job.id,
    v_user_id,
    p_feature,
    v_period
  )
  returning * into v_reservation;

  update public.ai_quota_usage
  set reserved_count = reserved_count + 1
  where user_id = v_user_id
    and feature = p_feature
    and period_start = v_period;

  return query
  select v_job.id,
         v_job.status,
         v_reservation.id,
         v_period,
         greatest(v_limit - v_usage.successful_count - v_usage.reserved_count - 1, 0),
         false;
end;
$function$;
