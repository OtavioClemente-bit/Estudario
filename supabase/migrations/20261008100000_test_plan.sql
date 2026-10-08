-- Plano TESTE: só para contas liberadas pelo servidor (o dono do app e quem ele escolher testar).
--
-- Tem mais que o Grátis, com teto claro, e não renova sozinho: o saldo é "por ciclo", e um novo
-- ciclo só começa quando o administrador roda public.admin_reset_test_plan. Não aparece em lugar
-- nenhum do app ou do site: não está no plan_catalog (preços) e a função ai-plan não lista o plano.
-- Para o app, a conta continua com a cara do Grátis; só o saldo é o do TESTE.

alter table public.profiles drop constraint if exists profiles_plan_tier_check;
alter table public.profiles add constraint profiles_plan_tier_check
  check (plan_tier in ('FREE', 'ESSENCIAL', 'PRO', 'TESTE'));

-- Início do ciclo atual do teste: muda a cada reset e vira o period_start do saldo.
alter table public.profiles add column if not exists test_cycle_start date;

alter table public.ai_plan_limits drop constraint if exists ai_plan_limits_plan_tier_check;
alter table public.ai_plan_limits add constraint ai_plan_limits_plan_tier_check
  check (plan_tier in ('FREE', 'ESSENCIAL', 'PRO', 'TESTE'));

-- LIFETIME: o saldo não renova por mês; renova no reset (ciclo novo).
insert into public.ai_plan_limits (plan_tier, feature, period_kind, quota_limit, max_per_request, max_per_topic_month) values
  ('TESTE', 'CONTENT_GENERATION',    'LIFETIME', 10, null, null),
  ('TESTE', 'SYLLABUS_GENERATION',   'LIFETIME', 1,  null, null),
  ('TESTE', 'SIMULATION_GENERATION', 'LIFETIME', 1,  30,   null),
  ('TESTE', 'PLAN_GENERATION',       'LIFETIME', 1,  null, null),
  ('TESTE', 'QUESTION_BATCH',        'LIFETIME', 5,  20,   60)
on conflict (plan_tier, feature, period_kind) do update
  set quota_limit = excluded.quota_limit, max_per_request = excluded.max_per_request,
      max_per_topic_month = excluded.max_per_topic_month, updated_at = now();

-- O ciclo do teste é o período do saldo: LIFETIME de quem está no TESTE começa no último reset.
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
    return date_trunc('month', now() at time zone 'America/Sao_Paulo')::date;
  end if;
  if v_tier = 'TESTE' then
    return coalesce((select test_cycle_start from public.profiles where user_id = auth.uid()), date '1970-01-02');
  end if;
  return date '1970-01-01';
end;
$$;

revoke all on function public.ai_quota_period(public.ai_feature) from public, anon;

-- ------------------------------------------------------------------ operações do administrador

-- Liga o TESTE numa conta (pelo e-mail) e começa um ciclo novo, com o saldo cheio.
create or replace function public.admin_grant_test_plan(p_email text)
returns text
language plpgsql
security definer
set search_path = public
as $$
declare
  v_user uuid;
begin
  select id into v_user from auth.users where lower(email) = lower(trim(p_email));
  if v_user is null then raise exception 'Conta não encontrada: %', p_email; end if;
  insert into public.profiles (user_id) values (v_user) on conflict (user_id) do nothing;
  update public.profiles
    set plan_tier = 'TESTE', plan_renews_at = null, beta_access = true,
        beta_access_granted_at = coalesce(beta_access_granted_at, now()),
        test_cycle_start = (now() at time zone 'America/Sao_Paulo')::date
    where user_id = v_user;
  return 'TESTE ligado para ' || p_email;
end;
$$;

-- Zera o saldo do TESTE: começa um ciclo novo (o histórico de uso continua guardado).
-- Dois resets no mesmo dia também funcionam: o ciclo anda um dia para a frente.
create or replace function public.admin_reset_test_plan(p_email text)
returns text
language plpgsql
security definer
set search_path = public
as $$
declare
  v_user uuid;
  v_today date := (now() at time zone 'America/Sao_Paulo')::date;
  v_next date;
begin
  select id into v_user from auth.users where lower(email) = lower(trim(p_email));
  if v_user is null then raise exception 'Conta não encontrada: %', p_email; end if;
  select greatest(v_today, coalesce(test_cycle_start, v_today - 1) + 1) into v_next
    from public.profiles where user_id = v_user and plan_tier = 'TESTE';
  if v_next is null then raise exception 'A conta % não está no plano TESTE.', p_email; end if;
  update public.profiles set test_cycle_start = v_next where user_id = v_user;
  return 'Saldo do TESTE zerado para ' || p_email || ' (ciclo ' || v_next || ')';
end;
$$;

-- Desliga o TESTE: a conta volta ao Grátis.
create or replace function public.admin_revoke_test_plan(p_email text)
returns text
language plpgsql
security definer
set search_path = public
as $$
declare
  v_user uuid;
begin
  select id into v_user from auth.users where lower(email) = lower(trim(p_email));
  if v_user is null then raise exception 'Conta não encontrada: %', p_email; end if;
  update public.profiles set plan_tier = 'FREE', plan_renews_at = null where user_id = v_user and plan_tier = 'TESTE';
  return 'TESTE desligado para ' || p_email;
end;
$$;

-- Saldo do ciclo atual de uma conta no TESTE.
create or replace function public.admin_test_plan_usage(p_email text)
returns table (feature text, limite integer, usado integer, reservado integer, ciclo date)
language sql
stable
security definer
set search_path = public
as $$
  select l.feature, l.quota_limit, coalesce(u.successful_count, 0), coalesce(u.reserved_count, 0), p.test_cycle_start
  from auth.users a
  join public.profiles p on p.user_id = a.id and p.plan_tier = 'TESTE'
  join public.ai_plan_limits l on l.plan_tier = 'TESTE' and l.period_kind = 'LIFETIME'
  left join public.ai_quota_usage u on u.user_id = a.id and u.feature::text = l.feature and u.period_start = p.test_cycle_start
  where lower(a.email) = lower(trim(p_email))
  order by l.feature;
$$;

revoke all on function public.admin_grant_test_plan(text) from public, anon, authenticated;
revoke all on function public.admin_reset_test_plan(text) from public, anon, authenticated;
revoke all on function public.admin_revoke_test_plan(text) from public, anon, authenticated;
revoke all on function public.admin_test_plan_usage(text) from public, anon, authenticated;
grant execute on function public.admin_grant_test_plan(text) to service_role;
grant execute on function public.admin_reset_test_plan(text) to service_role;
grant execute on function public.admin_revoke_test_plan(text) to service_role;
grant execute on function public.admin_test_plan_usage(text) to service_role;

-- Assinatura do Play que vence não derruba o TESTE (ele não depende de cobrança).
create or replace function public.expire_play_subscriptions()
returns integer
language plpgsql
security definer
set search_path = public
as $$
declare
  v_count integer;
begin
  with expired as (
    select distinct user_id from public.play_subscriptions
    where state in ('ACTIVE', 'IN_GRACE_PERIOD') and expires_at <= now() - interval '1 day'
  )
  update public.profiles p set plan_tier = 'FREE', plan_renews_at = null
  from expired e
  where p.user_id = e.user_id and p.plan_tier not in ('FREE', 'TESTE')
    and not exists (
      select 1 from public.play_subscriptions s
      where s.user_id = p.user_id and s.state in ('ACTIVE', 'IN_GRACE_PERIOD') and s.expires_at > now()
    );
  get diagnostics v_count = row_count;
  return v_count;
end;
$$;
revoke all on function public.expire_play_subscriptions() from public, anon, authenticated;
grant execute on function public.expire_play_subscriptions() to service_role;
