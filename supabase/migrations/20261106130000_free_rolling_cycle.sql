-- O mês do Grátis começa no dia do primeiro uso, não no dia 1. Quem usou algo dia 15 tem o saldo
-- mensal de 15 a 15. Quando o ciclo acaba, o próximo só começa no próximo uso (sem uso, não há ciclo
-- correndo). Calculado só pelo histórico (pedidos à IA e créditos de anúncio), sem estado guardado.
create or replace function public.ai_free_cycle_start(p_user uuid)
returns date
language plpgsql
stable
security definer
set search_path = public
as $$
declare
  v_today date := (now() at time zone 'America/Sao_Paulo')::date;
  v_start date;
  v_next date;
  guard integer := 0;
begin
  select min(d) into v_start from (
    select min((created_at at time zone 'America/Sao_Paulo')::date) as d from public.ai_jobs
      where user_id = p_user and status not in ('FAILED', 'EXPIRED', 'CANCELLED')
    union all
    select min((created_at at time zone 'America/Sao_Paulo')::date) from public.ad_reward_credits where user_id = p_user
  ) uses;
  if v_start is null then return v_today; end if;
  loop
    exit when (v_start + interval '1 month')::date > v_today or guard > 600;
    guard := guard + 1;
    select min(d) into v_next from (
      select min((created_at at time zone 'America/Sao_Paulo')::date) as d from public.ai_jobs
        where user_id = p_user and status not in ('FAILED', 'EXPIRED', 'CANCELLED')
          and created_at >= ((v_start + interval '1 month')::date::timestamp at time zone 'America/Sao_Paulo')
      union all
      select min((created_at at time zone 'America/Sao_Paulo')::date) from public.ad_reward_credits
        where user_id = p_user and created_at >= ((v_start + interval '1 month')::date::timestamp at time zone 'America/Sao_Paulo')
    ) uses;
    if v_next is null then return v_today; end if; -- ciclo acabou e ainda não houve uso novo
    v_start := v_next;
  end loop;
  return v_start;
end;
$$;
revoke all on function public.ai_free_cycle_start(uuid) from public, anon, authenticated;

-- A própria conta pode ver o início do seu ciclo (a tela de uso mostra quando renova).
create or replace function public.ai_my_free_cycle_start()
returns date
language sql
stable
security definer
set search_path = public
as $$ select public.ai_free_cycle_start(auth.uid()) $$;
revoke all on function public.ai_my_free_cycle_start() from public, anon;
grant execute on function public.ai_my_free_cycle_start() to authenticated;

create or replace function public.ai_quota_period(p_feature public.ai_feature)
returns date
language plpgsql
stable
security definer
set search_path = public
as $$
declare
  v_tier text := public.ai_effective_plan_tier(auth.uid());
  v_row public.ai_plan_limits := public.ai_plan_quota_row(v_tier, p_feature::text);
begin
  if v_row.period_kind = 'MONTHLY' then
    if v_tier in ('ESSENCIAL', 'PRO') then
      return public.ai_billing_cycle_start((select plan_renews_at from public.profiles where user_id = auth.uid()));
    end if;
    if v_tier = 'FREE' then
      return public.ai_free_cycle_start(auth.uid());
    end if;
    return date_trunc('month', now() at time zone 'America/Sao_Paulo')::date;
  end if;
  if v_tier = 'TESTE' then
    return coalesce((select test_cycle_start from public.profiles where user_id = auth.uid()), date '1970-01-02');
  end if;
  return date '1970-01-01';
end;
$$;
revoke all on function public.ai_quota_period(public.ai_feature) from public, anon;
