-- Plano dado à mão (ESSENCIAL/PRO sem plan_renews_at) nunca é rebaixado por assinatura: a assinatura
-- só sobe o plano. Antes, uma compra de teste do Essencial trocou um PRO dado à mão por ESSENCIAL.
-- Assinatura sempre grava plan_renews_at; plano dado à mão fica com plan_renews_at nulo.
create or replace function public.refresh_profile_plan(p_user_id uuid)
returns void
language plpgsql
security definer
set search_path = public
as $$
declare
  v_best record;
  v_profile record;
begin
  select plan_tier, plan_renews_at into v_profile from public.profiles where user_id = p_user_id;
  if v_profile.plan_tier = 'TESTE' then return; end if;
  -- plano dado à mão: fica como está (só uma assinatura PRO pode subir um ESSENCIAL dado à mão)
  if v_profile.plan_tier in ('ESSENCIAL', 'PRO') and v_profile.plan_renews_at is null then
    if v_profile.plan_tier = 'PRO' then return; end if;
  end if;

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

  if v_profile.plan_tier = 'ESSENCIAL' and v_profile.plan_renews_at is null then
    -- Essencial dado à mão: só sobe para PRO com assinatura PRO válida
    if v_best.plan_tier = 'PRO' then
      update public.profiles set plan_tier = 'PRO', plan_renews_at = v_best.expires_at where user_id = p_user_id;
    end if;
    return;
  end if;

  if v_best.plan_tier is not null then
    update public.profiles set plan_tier = v_best.plan_tier, plan_renews_at = v_best.expires_at
    where user_id = p_user_id;
  else
    update public.profiles set plan_tier = 'FREE', plan_renews_at = null
    where user_id = p_user_id and plan_tier in ('ESSENCIAL', 'PRO');
  end if;
end;
$$;
revoke all on function public.refresh_profile_plan(uuid) from public, anon, authenticated;
grant execute on function public.refresh_profile_plan(uuid) to service_role;
