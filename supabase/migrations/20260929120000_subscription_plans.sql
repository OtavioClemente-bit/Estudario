-- Planos Grátis / Essencial / Pro e limites de IA por plano.
--
-- Todo limite que protege gasto é decidido aqui, no banco. O app só exibe. O plano da conta só
-- muda pelo backend (service_role), nunca pelo cliente.

alter table public.profiles
  add column plan_tier text not null default 'FREE'
    check (plan_tier in ('FREE', 'ESSENCIAL', 'PRO')),
  add column plan_renews_at timestamptz;

comment on column public.profiles.plan_tier is
  'Plano da conta. Alterado somente pelo backend de cobrança ou manualmente pelo administrador.';
comment on column public.profiles.plan_renews_at is
  'Fim do período pago. Nulo em plano pago significa concessão manual sem vencimento.';

-- O cliente não pode promover o próprio plano: nenhuma política de UPDATE para authenticated.
revoke update on public.profiles from anon, authenticated;

create table public.ai_plan_limits (
  plan_tier text not null check (plan_tier in ('FREE', 'ESSENCIAL', 'PRO')),
  -- Texto e não public.ai_feature: inclui cotas de recursos que ainda não passam pelo backend
  -- (QUESTION_BATCH), para a tela de planos e o futuro backend usarem a mesma tabela.
  feature text not null check (feature in (
    'SYLLABUS_GENERATION', 'PLAN_GENERATION', 'CONTENT_GENERATION', 'QUESTION_BATCH', 'AD_REWARD'
  )),
  period_kind text not null check (period_kind in ('LIFETIME', 'MONTHLY', 'DAILY')),
  quota_limit integer not null check (quota_limit >= 0),
  -- Teto por pedido (questões por lote) e por tópico no mês, quando se aplica.
  max_per_request integer check (max_per_request is null or max_per_request > 0),
  max_per_topic_month integer check (max_per_topic_month is null or max_per_topic_month > 0),
  updated_at timestamptz not null default now(),
  primary key (plan_tier, feature, period_kind)
);

alter table public.ai_plan_limits enable row level security;

-- Os limites são públicos (aparecem na tela de planos); só o backend altera.
create policy ai_plan_limits_read
on public.ai_plan_limits
for select
to authenticated
using (true);

revoke insert, update, delete on public.ai_plan_limits from anon, authenticated;

insert into public.ai_plan_limits
  (plan_tier, feature, period_kind, quota_limit, max_per_request, max_per_topic_month)
values
  ('FREE',      'SYLLABUS_GENERATION', 'LIFETIME', 1,   null, null),
  ('FREE',      'PLAN_GENERATION',     'LIFETIME', 1,   null, null),
  ('FREE',      'CONTENT_GENERATION',  'MONTHLY',  10,  null, null),
  ('FREE',      'QUESTION_BATCH',      'MONTHLY',  5,   10,   20),
  ('FREE',      'AD_REWARD',           'DAILY',    2,   null, null),
  ('FREE',      'AD_REWARD',           'MONTHLY',  10,  null, null),
  ('ESSENCIAL', 'SYLLABUS_GENERATION', 'MONTHLY',  3,   null, null),
  ('ESSENCIAL', 'PLAN_GENERATION',     'MONTHLY',  3,   null, null),
  ('ESSENCIAL', 'CONTENT_GENERATION',  'MONTHLY',  60,  null, null),
  ('ESSENCIAL', 'QUESTION_BATCH',      'MONTHLY',  30,  20,   60),
  ('PRO',       'SYLLABUS_GENERATION', 'MONTHLY',  8,   null, null),
  ('PRO',       'PLAN_GENERATION',     'MONTHLY',  8,   null, null),
  ('PRO',       'CONTENT_GENERATION',  'MONTHLY',  200, null, null),
  ('PRO',       'QUESTION_BATCH',      'MONTHLY',  100, 30,   120);

-- Plano efetivo: plano pago vencido volta a ser Grátis.
create function public.ai_effective_plan_tier(p_user_id uuid)
returns text
language sql
stable
security definer
set search_path = public
as $$
  select coalesce((
    select profile.plan_tier
    from public.profiles as profile
    where profile.user_id = p_user_id
      and (
        profile.plan_tier = 'FREE'
        or profile.plan_renews_at is null
        or profile.plan_renews_at > now()
      )
  ), 'FREE');
$$;

revoke all on function public.ai_effective_plan_tier(uuid) from public, anon, authenticated;

-- Limite principal (LIFETIME ou MONTHLY) de um recurso para o plano.
create function public.ai_plan_quota_row(p_tier text, p_feature text)
returns public.ai_plan_limits
language sql
stable
security definer
set search_path = public
as $$
  select *
  from public.ai_plan_limits
  where plan_tier = p_tier
    and feature = p_feature
    and period_kind in ('LIFETIME', 'MONTHLY')
  order by case period_kind when 'LIFETIME' then 0 else 1 end
  limit 1;
$$;

revoke all on function public.ai_plan_quota_row(text, text) from public, anon, authenticated;

-- As duas funções abaixo substituem as versões fixas (limite 1). Elas são chamadas dentro da
-- reserva de cota, que roda com o JWT da pessoa, então auth.uid() identifica a conta.
create or replace function public.ai_quota_period(p_feature public.ai_feature)
returns date
language plpgsql
stable
security definer
set search_path = public
as $$
declare
  v_row public.ai_plan_limits := public.ai_plan_quota_row(
    public.ai_effective_plan_tier(auth.uid()), p_feature::text
  );
begin
  if v_row.period_kind = 'MONTHLY' then
    return date_trunc('month', now() at time zone 'America/Sao_Paulo')::date;
  end if;
  -- LIFETIME e recurso sem linha configurada: saldo único que não renova.
  return date '1970-01-01';
end;
$$;

create or replace function public.ai_quota_limit(p_feature public.ai_feature)
returns integer
language plpgsql
stable
security definer
set search_path = public
as $$
declare
  v_row public.ai_plan_limits := public.ai_plan_quota_row(
    public.ai_effective_plan_tier(auth.uid()), p_feature::text
  );
begin
  -- Sem configuração, o recurso fica fechado (0) em vez de ilimitado.
  return coalesce(v_row.quota_limit, 0);
end;
$$;

revoke all on function public.ai_quota_period(public.ai_feature) from public, anon;
revoke all on function public.ai_quota_limit(public.ai_feature) from public, anon;

-- Interesse em anúncio recompensado. Durante o teste fechado o botão não exibe anúncio nem
-- concede crédito: só mede quantas pessoas pediriam gerações extras dessa forma.
create table public.ai_ad_reward_interest (
  user_id uuid not null references auth.users (id) on delete cascade,
  day date not null,
  taps integer not null default 0 check (taps >= 0),
  primary key (user_id, day)
);

alter table public.ai_ad_reward_interest enable row level security;

create policy ai_ad_reward_interest_select_own
on public.ai_ad_reward_interest
for select
to authenticated
using (user_id = auth.uid());

revoke insert, update, delete on public.ai_ad_reward_interest from anon, authenticated;

create function public.register_ad_reward_interest()
returns integer
language plpgsql
security definer
set search_path = public
as $$
declare
  v_user_id uuid := auth.uid();
  v_taps integer;
begin
  if v_user_id is null then
    raise exception using errcode = '42501', message = 'AI_AUTH_REQUIRED';
  end if;
  insert into public.ai_ad_reward_interest as interest (user_id, day, taps)
  values (v_user_id, (now() at time zone 'America/Sao_Paulo')::date, 1)
  on conflict (user_id, day) do update
    -- Teto para a métrica não ser inflada por toques repetidos.
    set taps = least(interest.taps + 1, 20)
  returning taps into v_taps;
  return v_taps;
end;
$$;

revoke all on function public.register_ad_reward_interest() from public, anon;
grant execute on function public.register_ad_reward_interest() to authenticated;
