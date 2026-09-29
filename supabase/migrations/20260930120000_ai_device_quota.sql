-- Limite grátis por aparelho.
--
-- A cota de cada conta já existe (ai_quota_usage). Sem isto, quem troca de conta no mesmo celular
-- ganha o saldo grátis de novo. Aqui cada job criado registra o aparelho de onde veio (um hash
-- enviado pelo app) e, para contas no plano Grátis, o total de gerações do aparelho, somando
-- todas as contas, não pode passar do limite do Grátis. Planos pagos não são afetados.
--
-- Só a service role (Edge Functions) lê e escreve: RLS ligado e nenhuma policy.

create table public.ai_device_usage (
  job_id uuid primary key references public.ai_jobs(id) on delete cascade,
  device_hash text not null check (char_length(device_hash) between 32 and 128),
  user_id uuid not null,
  feature public.ai_feature not null,
  created_at timestamptz not null default now()
);

create index ai_device_usage_device_feature_idx on public.ai_device_usage (device_hash, feature, created_at);

alter table public.ai_device_usage enable row level security;
revoke all on public.ai_device_usage from public, anon, authenticated;

-- true quando a conta está no Grátis e o aparelho já usou todo o saldo grátis do recurso no
-- período (somando todas as contas). Jobs que falharam, expiraram ou foram cancelados não contam,
-- do mesmo jeito que a cota da conta é devolvida nesses casos.
create function public.ai_device_quota_exhausted(p_device_hash text, p_feature public.ai_feature, p_user_id uuid)
returns boolean
language plpgsql
stable
security definer
set search_path = public
as $$
declare
  v_row public.ai_plan_limits;
  v_since timestamptz;
  v_used integer;
begin
  if public.ai_effective_plan_tier(p_user_id) <> 'FREE' then
    return false;
  end if;
  v_row := public.ai_plan_quota_row('FREE', p_feature::text);
  if v_row.period_kind = 'MONTHLY' then
    v_since := date_trunc('month', now() at time zone 'America/Sao_Paulo') at time zone 'America/Sao_Paulo';
  else
    v_since := timestamptz '1970-01-01 00:00:00+00';
  end if;
  select count(*) into v_used
  from public.ai_device_usage as usage
  join public.ai_jobs as job on job.id = usage.job_id
  where usage.device_hash = p_device_hash
    and usage.feature = p_feature
    and usage.created_at >= v_since
    and job.status not in ('FAILED', 'EXPIRED', 'CANCELLED');
  return v_used >= coalesce(v_row.quota_limit, 0);
end;
$$;

create function public.ai_record_device_job(p_device_hash text, p_user_id uuid, p_feature public.ai_feature, p_job_id uuid)
returns void
language sql
security definer
set search_path = public
as $$
  insert into public.ai_device_usage (job_id, device_hash, user_id, feature)
  values (p_job_id, p_device_hash, p_user_id, p_feature)
  on conflict (job_id) do nothing;
$$;

revoke all on function public.ai_device_quota_exhausted(text, public.ai_feature, uuid) from public, anon, authenticated;
revoke all on function public.ai_record_device_job(text, uuid, public.ai_feature, uuid) from public, anon, authenticated;
grant execute on function public.ai_device_quota_exhausted(text, public.ai_feature, uuid) to service_role;
grant execute on function public.ai_record_device_job(text, uuid, public.ai_feature, uuid) to service_role;
