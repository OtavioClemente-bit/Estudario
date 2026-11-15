-- Pedido que a IA recusou antes de começar (PROVIDER_REJECTED, sem resposta do provedor) falha em
-- até ~1,5 minuto em vez de até 25 (14/11/2026): a pessoa vê o erro e tenta de novo. Só pega a recusa
-- definitiva; pedido que está gerando de verdade (IN_FLIGHT ou com resposta) não entra.
create or replace function public.fail_rejected_ai_jobs()
returns integer
language plpgsql
security definer
set search_path to ''
as $function$
declare
  v_job public.ai_jobs;
  v_reservation public.ai_quota_reservations;
  v_count integer := 0;
begin
  for v_job in
    select * from public.ai_jobs
    where status = 'PROCESSING'
      and provider_start_outcome = 'PROVIDER_REJECTED'
      and openai_response_id is null
      and updated_at < now() - interval '30 seconds'
    for update skip locked
  loop
    perform set_config('ai.internal_job_transition', '1', true);
    -- Recusado antes de começar: não há resposta do provedor a recuperar (conferido aqui mesmo).
    update public.ai_jobs set provider_reconciled_at = now(), provider_result_recoverable = false where id = v_job.id;
    update public.ai_jobs set status = 'FAILED', finished_at = now(), provider_quarantined_at = null, lease_owner = null, lease_expires_at = null,
      error_code = 'PROVIDER_REJECTED', error_message = 'A IA recusou o pedido. Nada foi cobrado; tente de novo.'
    where id = v_job.id;
    perform set_config('ai.internal_job_transition', '0', true);
    select * into v_reservation from public.ai_quota_reservations where job_id = v_job.id for update;
    if found and v_reservation.status = 'RESERVED' then
      update public.ai_quota_reservations set status = 'RELEASED', released_at = now() where id = v_reservation.id;
      update public.ai_quota_usage set reserved_count = greatest(reserved_count - 1, 0)
        where user_id = v_reservation.user_id and feature = v_reservation.feature and period_start = v_reservation.period_start;
    end if;
    v_count := v_count + 1;
  end loop;
  return v_count;
end;
$function$;

revoke all on function public.fail_rejected_ai_jobs() from public, anon, authenticated;
grant execute on function public.fail_rejected_ai_jobs() to service_role;

select cron.schedule('fail-rejected-ai-jobs', '* * * * *', 'select public.fail_rejected_ai_jobs()');

select public.fail_rejected_ai_jobs();
