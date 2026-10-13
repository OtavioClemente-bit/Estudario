-- Lote de compensação: quando a matéria saiu com menos questões que o pedido (a IA entrega até 10),
-- os lotes extras até completar o número pedido não gastam cota. O servidor marca o pedido com
-- "compensated": true no request_payload; depois que o lote dá certo, o worker chama esta função,
-- que devolve 1 da cota daquele mês, uma única vez por job.

create table if not exists public.ai_quota_refunds (
  job_id uuid primary key references public.ai_jobs (id) on delete cascade,
  created_at timestamptz not null default now()
);
alter table public.ai_quota_refunds enable row level security;
revoke all on public.ai_quota_refunds from public, anon, authenticated;
grant all on public.ai_quota_refunds to service_role;

create or replace function public.ai_refund_job_quota(p_job_id uuid)
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
  if not found or coalesce(v_job.request_payload ->> 'compensated', '') <> 'true' then
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

revoke all on function public.ai_refund_job_quota(uuid) from public, anon, authenticated;
grant execute on function public.ai_refund_job_quota(uuid) to service_role;
