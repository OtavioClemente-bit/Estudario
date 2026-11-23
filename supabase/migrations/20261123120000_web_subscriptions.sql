-- Assinaturas feitas pelo site (Mercado Pago), para quem não tem Google Play (iPhone, computador).
--
-- Duas formas:
--   SUBSCRIPTION: assinatura com cartão que renova sozinha (preapproval do Mercado Pago). Vale até
--                 a próxima cobrança (+ folga de 3 dias para a cobrança cair). Cancelada, vale até o
--                 fim do período já pago.
--   ONE_TIME:     pagamento único (Pix, cartão ou boleto) de 1 mês ou 1 ano, sem renovar. Pagar de
--                 novo antes de acabar soma ao período que falta.
--
-- Quem grava é só o servidor (funções mp-billing e mp-webhook, com service_role), sempre depois de
-- conferir o pagamento direto na API do Mercado Pago. O plano da conta (profiles.plan_tier) passa a
-- olhar as duas fontes: Google Play e site.

create table if not exists public.web_subscriptions (
  id text primary key,                       -- id do Mercado Pago: "sub:<preapproval>" ou "pay:<pagamento>"
  user_id uuid not null references auth.users (id) on delete cascade,
  provider text not null default 'MERCADOPAGO',
  kind text not null check (kind in ('SUBSCRIPTION', 'ONE_TIME')),
  plan_tier text not null check (plan_tier in ('ESSENCIAL', 'PRO')),
  period text not null check (period in ('MONTH', 'YEAR')),
  state text not null check (state in ('PENDING', 'ACTIVE', 'CANCELLED', 'EXPIRED', 'VOIDED')),
  expires_at timestamptz,
  auto_renewing boolean not null default false,
  amount_cents integer,
  created_at timestamptz not null default now(),
  updated_at timestamptz not null default now()
);
create index if not exists web_subscriptions_user_idx on public.web_subscriptions (user_id, expires_at desc);
alter table public.web_subscriptions enable row level security;
revoke all on public.web_subscriptions from public, anon, authenticated;
grant select on public.web_subscriptions to authenticated;
drop policy if exists web_subscriptions_select_own on public.web_subscriptions;
create policy web_subscriptions_select_own on public.web_subscriptions
  for select to authenticated using (user_id = auth.uid());

-- Grava o que o Mercado Pago respondeu. Estorno (VOIDED) não volta a valer.
create or replace function public.apply_web_subscription(
  p_id text,
  p_user_id uuid,
  p_kind text,
  p_plan_tier text,
  p_period text,
  p_state text,
  p_expires_at timestamptz,
  p_auto_renewing boolean,
  p_amount_cents integer
)
returns void
language plpgsql
security definer
set search_path = public
as $$
declare
  v_owner uuid;
begin
  select user_id into v_owner from public.web_subscriptions where id = p_id;
  if v_owner is not null and v_owner <> p_user_id then
    raise exception 'purchase_belongs_to_another_account' using errcode = '42501';
  end if;
  insert into public.web_subscriptions (id, user_id, kind, plan_tier, period, state, expires_at, auto_renewing, amount_cents)
  values (p_id, p_user_id, p_kind, p_plan_tier, p_period, p_state, p_expires_at, p_auto_renewing, p_amount_cents)
  on conflict (id) do update
    set plan_tier = excluded.plan_tier, period = excluded.period,
        state = case when public.web_subscriptions.state = 'VOIDED' then 'VOIDED' else excluded.state end,
        -- cancelada guarda até quando já foi pago; nunca encurta por causa de um aviso atrasado
        expires_at = case
          when public.web_subscriptions.state = 'VOIDED' then public.web_subscriptions.expires_at
          when excluded.state = 'VOIDED' then excluded.expires_at
          when excluded.expires_at is null then public.web_subscriptions.expires_at
          else greatest(coalesce(public.web_subscriptions.expires_at, excluded.expires_at), excluded.expires_at) end,
        auto_renewing = excluded.auto_renewing,
        amount_cents = coalesce(excluded.amount_cents, public.web_subscriptions.amount_cents),
        updated_at = now();
  perform public.refresh_profile_plan(p_user_id);
end;
$$;
revoke all on function public.apply_web_subscription(text, uuid, text, text, text, text, timestamptz, boolean, integer) from public, anon, authenticated;
grant execute on function public.apply_web_subscription(text, uuid, text, text, text, text, timestamptz, boolean, integer) to service_role;

-- O plano da conta: o melhor entre Google Play e site que ainda vale.
create or replace function public.refresh_profile_plan(p_user_id uuid)
returns void
language plpgsql
security definer
set search_path = public
as $$
declare
  v_best record;
begin
  select plan_tier, expires_at into v_best
  from (
    select plan_tier, expires_at from public.play_subscriptions
    where user_id = p_user_id and state in ('ACTIVE', 'IN_GRACE_PERIOD') and expires_at > now()
    union all
    select plan_tier, expires_at from public.web_subscriptions
    where user_id = p_user_id and state in ('ACTIVE', 'CANCELLED') and expires_at > now()
  ) s
  order by case plan_tier when 'PRO' then 2 else 1 end desc, expires_at desc
  limit 1;

  if v_best.plan_tier is not null then
    update public.profiles set plan_tier = v_best.plan_tier, plan_renews_at = v_best.expires_at
    where user_id = p_user_id and plan_tier <> 'TESTE';
  else
    update public.profiles set plan_tier = 'FREE', plan_renews_at = null
    where user_id = p_user_id and plan_tier in ('ESSENCIAL', 'PRO');
  end if;
end;
$$;
revoke all on function public.refresh_profile_plan(uuid) from public, anon, authenticated;
grant execute on function public.refresh_profile_plan(uuid) to service_role;

-- A rotina diária que tira o plano de quem venceu passa a olhar as duas fontes (mesmo nome, para
-- o agendamento que já existe continuar chamando).
create or replace function public.expire_play_subscriptions()
returns integer
language plpgsql
security definer
set search_path = public
as $$
declare
  v_user uuid;
  v_count integer := 0;
begin
  update public.web_subscriptions set state = 'EXPIRED', updated_at = now()
  where state in ('ACTIVE', 'CANCELLED') and expires_at <= now() - interval '1 day';

  for v_user in
    select p.user_id from public.profiles p
    where p.plan_tier in ('ESSENCIAL', 'PRO')
      -- só quem ganhou o plano por assinatura (plano dado à mão, sem assinatura, não é mexido)
      and (exists (select 1 from public.play_subscriptions s where s.user_id = p.user_id)
           or exists (select 1 from public.web_subscriptions w where w.user_id = p.user_id))
      and not exists (select 1 from public.play_subscriptions s
                      where s.user_id = p.user_id and s.state in ('ACTIVE', 'IN_GRACE_PERIOD') and s.expires_at > now())
      and not exists (select 1 from public.web_subscriptions w
                      where w.user_id = p.user_id and w.state in ('ACTIVE', 'CANCELLED') and w.expires_at > now())
  loop
    perform public.refresh_profile_plan(v_user);
    v_count := v_count + 1;
  end loop;
  return v_count;
end;
$$;
revoke all on function public.expire_play_subscriptions() from public, anon, authenticated;
grant execute on function public.expire_play_subscriptions() to service_role;
