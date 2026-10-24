-- Plano Grátis só com matéria pronta (24/10/2026).
--
-- O Grátis não usa mais IA em nada: matéria, lote de questões e o simulado diagnóstico saem só da
-- biblioteca (a trava fica no worker, antes de chamar o provedor; falha fechada). O plano por IA e
-- o edital por PDF são dos planos pagos.
--
-- Matérias do Grátis passam a ter três motivos, conferidos na criação do pedido (free-allowance.ts):
--   dia do plano até 3 por dia; adiantar com anúncio (2 por dia, 10 por mês) ou 1 por semana sem
--   anúncio; e 10 por mês fora do plano. A cota do banco (CONTENT_GENERATION) vira só o teto de
--   segurança da soma de tudo (também usado no limite por aparelho e por rede).

update public.ai_plan_limits set quota_limit = 120
where plan_tier = 'FREE' and feature = 'CONTENT_GENERATION' and period_kind = 'MONTHLY';

update public.ai_plan_limits set quota_limit = 0
where plan_tier = 'FREE' and feature = 'PLAN_GENERATION';

-- Crédito de anúncio premiado, gravado só quando o Google confirma o anúncio (ad-reward-ssv, com a
-- assinatura dele conferida). O id é o transaction_id do AdMob: o mesmo anúncio não vale duas vezes.
create table if not exists public.ad_reward_credits (
  id text primary key check (char_length(id) between 1 and 200),
  user_id uuid not null references auth.users (id) on delete cascade,
  ad_unit text not null default '',
  created_at timestamptz not null default now(),
  used_at timestamptz,
  used_job_id uuid references public.ai_jobs (id) on delete set null
);

create index if not exists ad_reward_credits_user_created on public.ad_reward_credits (user_id, created_at desc);

alter table public.ad_reward_credits enable row level security;
revoke all on table public.ad_reward_credits from public, anon, authenticated;
grant select, insert, update on table public.ad_reward_credits to service_role;

-- Matéria do plano de hoje que saiu pronta (biblioteca ou material já guardado, sem chamar a IA) não
-- gasta a cota em nenhum plano: nos pagos, as matérias do mês são para usar além do plano do dia.
-- Quando a IA escreve, conta normalmente. Uma vez só por job (ai_quota_refunds).
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
     or coalesce(v_job.request_payload ->> 'purpose', '') <> 'PLAN_DAY'
     or v_job.openai_response_id is not null then
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
