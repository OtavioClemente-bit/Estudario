-- A cota mensal dos planos pagos renova no dia do pagamento, não no dia 1 do mês.
-- O ciclo é o mês que termina na próxima cobrança (profiles.plan_renews_at, que o Play estende a cada
-- renovação paga). Quem paga dia 28 tem o saldo de 28 a 28; se não pagar, o plano vence nesse dia e
-- a conta volta ao Grátis, sem ciclo novo. Sem data de cobrança (plano dado à mão), segue o mês do
-- calendário como antes.
create or replace function public.ai_billing_cycle_start(p_renews_at timestamptz)
returns date
language plpgsql
stable
as $$
declare
  v_anchor date := (p_renews_at at time zone 'America/Sao_Paulo')::date;
  v_today date := (now() at time zone 'America/Sao_Paulo')::date;
  v_start date;
  k integer := 1;
begin
  if p_renews_at is null then
    return date_trunc('month', now() at time zone 'America/Sao_Paulo')::date;
  end if;
  loop
    v_start := (v_anchor - make_interval(months => k))::date;
    exit when v_start <= v_today or k > 240;
    k := k + 1;
  end loop;
  return v_start;
end;
$$;

revoke all on function public.ai_billing_cycle_start(timestamptz) from public, anon, authenticated;

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
    return date_trunc('month', now() at time zone 'America/Sao_Paulo')::date;
  end if;
  if v_tier = 'TESTE' then
    return coalesce((select test_cycle_start from public.profiles where user_id = auth.uid()), date '1970-01-02');
  end if;
  return date '1970-01-01';
end;
$$;

revoke all on function public.ai_quota_period(public.ai_feature) from public, anon;
