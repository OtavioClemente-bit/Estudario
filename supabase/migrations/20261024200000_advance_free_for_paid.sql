-- Adiantar o plano é livre nos planos pagos: a matéria adiantada que sai pronta (sem chamar o
-- provedor) também devolve a cota, como a do plano de hoje. As matérias do mês (25 no Essencial,
-- 40 no Pro) ficam para o que é fora do plano. No Grátis, adiantar continua conferido na criação
-- do pedido (anúncio ou 1 por semana); a devolução só mantém folga no teto interno.

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
     or coalesce(v_job.request_payload ->> 'purpose', '') not in ('PLAN_DAY', 'ADVANCE')
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
