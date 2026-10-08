-- Planos com preço, limites recalculados pelo custo real e assinaturas do Google Play.
--
-- Custo por geração (gpt-6-luna, dólar ~R$ 5,15): material R$ 0,35–0,70 (as buscas na web são a
-- maior parte), edital por PDF R$ 0,15–0,40, simulado R$ 0,60–1,00, lote de questões R$ 0,10–0,20.
-- O Google Play fica com 15%. Os limites abaixo deixam o PIOR caso (tudo usado) dentro do que sobra:
--   Essencial R$ 24,90 → R$ 21,17 líquidos; pior caso ≈ R$ 21.
--   Pro       R$ 49,90 → R$ 42,40 líquidos; pior caso ≈ R$ 44 (uso médio ≈ R$ 30).
--   Grátis: ≈ R$ 1,70 por conta no pior caso; edital só do catálogo (o próprio PDF é pago).
-- Preços e limites ficam nestas tabelas para mudar sem lançar versão.

-- ---------------------------------------------------------------- limites
update public.ai_plan_limits set quota_limit = 0,  period_kind = 'LIFETIME' where plan_tier = 'FREE' and feature = 'SYLLABUS_GENERATION';
update public.ai_plan_limits set quota_limit = 3  where plan_tier = 'FREE' and feature = 'CONTENT_GENERATION';
update public.ai_plan_limits set quota_limit = 3,  max_per_request = 10, max_per_topic_month = 20 where plan_tier = 'FREE' and feature = 'QUESTION_BATCH';
update public.ai_plan_limits set quota_limit = 1  where plan_tier = 'FREE' and feature = 'SIMULATION_GENERATION';

update public.ai_plan_limits set quota_limit = 1  where plan_tier = 'ESSENCIAL' and feature = 'SYLLABUS_GENERATION';
update public.ai_plan_limits set quota_limit = 1  where plan_tier = 'ESSENCIAL' and feature = 'PLAN_GENERATION';
update public.ai_plan_limits set quota_limit = 25 where plan_tier = 'ESSENCIAL' and feature = 'CONTENT_GENERATION';
update public.ai_plan_limits set quota_limit = 10, max_per_request = 20, max_per_topic_month = 60 where plan_tier = 'ESSENCIAL' and feature = 'QUESTION_BATCH';
update public.ai_plan_limits set quota_limit = 2  where plan_tier = 'ESSENCIAL' and feature = 'SIMULATION_GENERATION';

update public.ai_plan_limits set quota_limit = 3  where plan_tier = 'PRO' and feature = 'SYLLABUS_GENERATION';
update public.ai_plan_limits set quota_limit = 3  where plan_tier = 'PRO' and feature = 'PLAN_GENERATION';
update public.ai_plan_limits set quota_limit = 50 where plan_tier = 'PRO' and feature = 'CONTENT_GENERATION';
update public.ai_plan_limits set quota_limit = 20, max_per_request = 30, max_per_topic_month = 120 where plan_tier = 'PRO' and feature = 'QUESTION_BATCH';
update public.ai_plan_limits set quota_limit = 4  where plan_tier = 'PRO' and feature = 'SIMULATION_GENERATION';

-- ---------------------------------------------------------------- catálogo com preço
create table public.plan_catalog (
  plan_tier text primary key check (plan_tier in ('FREE', 'ESSENCIAL', 'PRO')),
  name text not null,
  tagline text not null,
  price_month_cents integer not null default 0 check (price_month_cents >= 0),
  price_year_cents integer not null default 0 check (price_year_cents >= 0),
  -- Ids do Play Console: produto de assinatura e os dois planos base (mensal e anual).
  play_product_id text,
  play_base_plan_month text,
  play_base_plan_year text,
  featured boolean not null default false,
  sort_order integer not null default 0
);

insert into public.plan_catalog (plan_tier, name, tagline, price_month_cents, price_year_cents, play_product_id, play_base_plan_month, play_base_plan_year, featured, sort_order) values
  ('FREE',      'Grátis',    'Para conhecer o Estudário',                         0,    0,     null,                 null,     null,    false, 0),
  ('ESSENCIAL', 'Essencial', 'Para estudar todo dia, matéria por matéria',        2490, 24900, 'estudario_essencial', 'mensal', 'anual', true,  1),
  ('PRO',       'Pro',       'Para a reta final, com folga para estudar tudo',    4990, 49900, 'estudario_pro',       'mensal', 'anual', false, 2);

alter table public.plan_catalog enable row level security;
revoke all on public.plan_catalog from public, anon, authenticated;
grant select on public.plan_catalog to anon, authenticated;
create policy plan_catalog_read on public.plan_catalog for select to anon, authenticated using (true);

-- ---------------------------------------------------------------- assinaturas do Play
create table public.play_subscriptions (
  purchase_token text primary key,
  user_id uuid not null references auth.users (id) on delete cascade,
  product_id text not null,
  base_plan_id text,
  plan_tier text not null check (plan_tier in ('ESSENCIAL', 'PRO')),
  state text not null,
  expires_at timestamptz,
  auto_renewing boolean not null default false,
  created_at timestamptz not null default now(),
  updated_at timestamptz not null default now()
);
create index play_subscriptions_user_idx on public.play_subscriptions (user_id, expires_at desc);
alter table public.play_subscriptions enable row level security;
revoke all on public.play_subscriptions from public, anon, authenticated;

-- Aplica o que o Google respondeu sobre a compra: guarda a assinatura e põe o plano no perfil.
-- Só o servidor (service_role) chama, depois de conferir o token com a API do Play.
create function public.apply_play_subscription(
  p_user_id uuid,
  p_purchase_token text,
  p_product_id text,
  p_base_plan_id text,
  p_plan_tier text,
  p_state text,
  p_expires_at timestamptz,
  p_auto_renewing boolean
)
returns void
language plpgsql
security definer
set search_path = public
as $$
declare
  v_owner uuid;
  v_active boolean := p_state in ('ACTIVE', 'IN_GRACE_PERIOD') and p_expires_at is not null and p_expires_at > now();
  v_best record;
begin
  -- Um token pertence a uma conta só: não deixa outra conta "restaurar" a compra alheia.
  select user_id into v_owner from public.play_subscriptions where purchase_token = p_purchase_token;
  if v_owner is not null and v_owner <> p_user_id then
    raise exception 'purchase_belongs_to_another_account' using errcode = '42501';
  end if;

  insert into public.play_subscriptions (purchase_token, user_id, product_id, base_plan_id, plan_tier, state, expires_at, auto_renewing)
  values (p_purchase_token, p_user_id, p_product_id, p_base_plan_id, p_plan_tier, p_state, p_expires_at, p_auto_renewing)
  on conflict (purchase_token) do update
    set product_id = excluded.product_id, base_plan_id = excluded.base_plan_id, plan_tier = excluded.plan_tier,
        state = excluded.state, expires_at = excluded.expires_at, auto_renewing = excluded.auto_renewing, updated_at = now();

  -- O perfil recebe a melhor assinatura ativa da conta (Pro vence Essencial; depois a que vence mais tarde).
  select plan_tier, expires_at into v_best
  from public.play_subscriptions
  where user_id = p_user_id and state in ('ACTIVE', 'IN_GRACE_PERIOD') and expires_at > now()
  order by case plan_tier when 'PRO' then 2 else 1 end desc, expires_at desc
  limit 1;

  if v_best.plan_tier is not null then
    update public.profiles set plan_tier = v_best.plan_tier, plan_renews_at = v_best.expires_at where user_id = p_user_id;
  else
    update public.profiles set plan_tier = 'FREE', plan_renews_at = null where user_id = p_user_id;
  end if;
end;
$$;
revoke all on function public.apply_play_subscription(uuid, text, text, text, text, text, timestamptz, boolean) from public, anon, authenticated;
grant execute on function public.apply_play_subscription(uuid, text, text, text, text, text, timestamptz, boolean) to service_role;

-- Assinaturas que venceram sem renovação confirmada voltam o perfil para Grátis (rodar por cron).
create function public.expire_play_subscriptions()
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
  where p.user_id = e.user_id and p.plan_tier <> 'FREE'
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
