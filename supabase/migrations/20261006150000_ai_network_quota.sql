-- Limite grátis por rede, para o app web e para pedidos sem identificação de aparelho.
--
-- No celular o saldo grátis é somado por aparelho (ai_device_usage). No navegador a identificação
-- do aparelho pode ser apagada com a limpeza do navegador, então cada geração do plano Grátis
-- também é somada pela rede de origem (hash do IP, nunca o IP em si). Como uma rede pode ser
-- compartilhada (casa, escola), o teto por rede é o saldo grátis multiplicado por p_multiplier.

create table public.ai_network_usage (
  job_id uuid primary key references public.ai_jobs(id) on delete cascade,
  network_hash text not null check (char_length(network_hash) between 32 and 128),
  user_id uuid not null,
  feature public.ai_feature not null,
  created_at timestamptz not null default now()
);

create index ai_network_usage_network_feature_idx on public.ai_network_usage (network_hash, feature, created_at);

alter table public.ai_network_usage enable row level security;
revoke all on public.ai_network_usage from public, anon, authenticated;

create function public.ai_network_quota_exhausted(p_network_hash text, p_feature public.ai_feature, p_user_id uuid, p_multiplier integer)
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
  from public.ai_network_usage as usage
  join public.ai_jobs as job on job.id = usage.job_id
  where usage.network_hash = p_network_hash
    and usage.feature = p_feature
    and usage.created_at >= v_since
    and job.status not in ('FAILED', 'EXPIRED', 'CANCELLED');
  return v_used >= coalesce(v_row.quota_limit, 0) * greatest(p_multiplier, 1);
end;
$$;

create function public.ai_record_network_job(p_network_hash text, p_user_id uuid, p_feature public.ai_feature, p_job_id uuid)
returns void
language sql
security definer
set search_path = public
as $$
  insert into public.ai_network_usage (job_id, network_hash, user_id, feature)
  values (p_job_id, p_network_hash, p_user_id, p_feature)
  on conflict (job_id) do nothing;
$$;

revoke all on function public.ai_network_quota_exhausted(text, public.ai_feature, uuid, integer) from public, anon, authenticated;
revoke all on function public.ai_record_network_job(text, uuid, public.ai_feature, uuid) from public, anon, authenticated;
grant execute on function public.ai_network_quota_exhausted(text, public.ai_feature, uuid, integer) to service_role;
grant execute on function public.ai_record_network_job(text, uuid, public.ai_feature, uuid) to service_role;
