-- Material montado da biblioteca (ou do cache entre usuários) termina sem chamar a OpenAI, então
-- não tem openai_response_id. A trigger exigia o id em todo SUCCEEDED e recusava esses pedidos,
-- que ficavam parados até expirar. Sem provedor iniciado e sem id, o sucesso é aceito como está.
create or replace function public.ai_jobs_accept_provider_before_success()
returns trigger
language plpgsql
set search_path = ''
as $$
begin
  if new.status = 'SUCCEEDED' and old.status <> 'SUCCEEDED' then
    if new.openai_response_id is null
       and old.openai_response_id is null
       and old.provider_quarantined_at is null
       and old.provider_start_outcome = 'NOT_STARTED'
       and old.provider_execution_started_at is null then
      return new;
    end if;
    if old.provider_quarantined_at is not null
       or old.provider_start_outcome not in ('NOT_STARTED', 'ACCEPTED')
       or new.openai_response_id is null
       or (old.openai_response_id is not null and new.openai_response_id is distinct from old.openai_response_id) then
      raise exception using errcode = '23514', message = 'AI_PROVIDER_ACCEPTANCE_REQUIRED';
    end if;
    new.provider_start_outcome := 'ACCEPTED';
    new.provider_quarantined_at := null;
    new.provider_execution_started_at := coalesce(old.provider_execution_started_at, now());
  end if;
  return new;
end;
$$;

revoke all on function public.ai_jobs_accept_provider_before_success() from public, anon, authenticated, service_role;
