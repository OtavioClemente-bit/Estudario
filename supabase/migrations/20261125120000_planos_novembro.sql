-- Planos revistos com o dono em 25/11/2026 (docs/PLANOS_E_LIMITES.md).
-- Matérias: o plano do dia inclui 2 (Grátis), 3 (Essencial) ou 4 (Pro) por dia, prontas ou escritas na hora (o
-- aluno não vê a diferença). Além disso, a cota do mês: Essencial 5, Pro 10. Teto escondido de matérias escritas
-- na hora por conta paga: PAID_AI_MATERIALS_MONTHLY_CAP (worker, padrão 30).
-- Lotes extras de questões: 3 / 10 / 20. Simulados montados pela pessoa: 1 / 4 / 8 por mês. Edital por PDF: — / 2 / 4.

update public.ai_plan_limits set quota_limit = 5,  max_per_request = 5  where plan_tier = 'ESSENCIAL' and feature = 'CONTENT_GENERATION';
update public.ai_plan_limits set quota_limit = 10, max_per_request = 10 where plan_tier = 'PRO'       and feature = 'CONTENT_GENERATION';
update public.ai_plan_limits set quota_limit = 20 where plan_tier = 'PRO' and feature = 'QUESTION_BATCH';
update public.ai_plan_limits set quota_limit = 4  where plan_tier = 'ESSENCIAL' and feature = 'SIMULATION_GENERATION';
update public.ai_plan_limits set quota_limit = 8  where plan_tier = 'PRO'       and feature = 'SIMULATION_GENERATION';
update public.ai_plan_limits set quota_limit = 4  where plan_tier = 'PRO'       and feature = 'SYLLABUS_GENERATION';
-- Grátis: 1 simulado por mês (antes 1 na vida).
delete from public.ai_plan_limits where plan_tier = 'FREE' and feature = 'SIMULATION_GENERATION';
insert into public.ai_plan_limits (plan_tier, feature, period_kind, quota_limit, max_per_request, max_per_topic_month)
values ('FREE', 'SIMULATION_GENERATION', 'MONTHLY', 1, 30, null)
on conflict (plan_tier, feature, period_kind) do update set quota_limit = excluded.quota_limit, max_per_request = excluded.max_per_request;

-- Matérias do plano incluídas por dia, por plano (antes 4 para todo pago), e também as escritas na hora.
create or replace function public.ai_refund_plan_day_quota(p_job_id uuid)
returns boolean
language plpgsql
security definer
set search_path = public
as $$
declare
  v_job public.ai_jobs;
  v_period date;
  v_tier text;
  v_daily integer;
begin
  if coalesce(auth.role(), '') not in ('service_role', 'supabase_admin') then
    raise exception using errcode = '42501', message = 'AI_WORKER_FORBIDDEN';
  end if;
  select * into v_job from public.ai_jobs where id = p_job_id and status = 'SUCCEEDED';
  if not found
     or v_job.feature <> 'CONTENT_GENERATION'
     or coalesce(v_job.request_payload ->> 'purpose', '') not in ('PLAN_DAY', 'ADVANCE') then
    return false;
  end if;
  -- Uma conta por vez: duas matérias terminando juntas não passam as duas pelo teto.
  perform 1 from public.profiles where user_id = v_job.user_id for update;
  select case when p.plan_tier in ('ESSENCIAL', 'PRO') and (p.plan_renews_at is null or p.plan_renews_at > now()) then p.plan_tier else 'FREE' end
    into v_tier from public.profiles p where p.user_id = v_job.user_id;
  v_daily := case v_tier when 'PRO' then 4 when 'ESSENCIAL' then 3 else 2 end;
  if public.ai_plan_materials_today(v_job.user_id) >= v_daily then
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

-- Concursos ao mesmo tempo (a trava de editais salvos, conferida no servidor ao gerar): 1 / 2 / 4.
update public.ai_plan_limits set quota_limit = 1 where plan_tier = 'FREE'      and feature = 'SAVED_EDITAIS';
update public.ai_plan_limits set quota_limit = 2 where plan_tier = 'ESSENCIAL' and feature = 'SAVED_EDITAIS';
update public.ai_plan_limits set quota_limit = 4 where plan_tier = 'PRO'       and feature = 'SAVED_EDITAIS';
