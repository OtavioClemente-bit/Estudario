-- Agenda o worker de conteúdo e plano (ai-text-worker), a cada minuto, no mesmo formato do
-- agendamento do worker do edital (job "ai-syllabus-worker-every-minute", observado no projeto
-- remoto em 2026-09-28): POST com o token do worker lido do Vault.
--
-- Só agenda onde o token existe no Vault, ou seja, no projeto remoto. Num banco local sem o
-- segredo, nada é criado e ninguém chama a função de produção.
do $$
begin
  if to_regnamespace('cron') is null or to_regnamespace('vault') is null then
    return;
  end if;
  if not exists (select 1 from vault.decrypted_secrets where name = 'ai_worker_auth_token') then
    return;
  end if;
  if exists (select 1 from cron.job where jobname = 'ai-text-worker-every-minute') then
    return;
  end if;
  perform cron.schedule(
    'ai-text-worker-every-minute',
    '* * * * *',
    $job$
      select net.http_post(
        url := 'https://gaqqilzhmvkxfpvivwwv.supabase.co/functions/v1/ai-text-worker',
        headers := jsonb_build_object(
          'Content-Type', 'application/json',
          'Authorization', 'Bearer ' || (
            select decrypted_secret
            from vault.decrypted_secrets
            where name = 'ai_worker_auth_token'
            limit 1
          )
        ),
        body := '{}'::jsonb,
        timeout_milliseconds := 10000
      );
    $job$
  );
end;
$$;
