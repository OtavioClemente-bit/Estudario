-- Alívio para o banco depois da queda de 10/11 (06:22–06:41, sem conexões e reinício do Postgres):
-- 1) o histórico das tarefas agendadas (cron.job_run_details) nunca era limpo: 126 mil linhas, 89 MB
--    num banco de 410 MB com limite de 500 MB. Fica só a última semana, limpa todo dia.
-- 2) a atualização do painel da Equipe levava 12–14 s a cada 15 minutos, a consulta mais pesada do
--    banco. Passa a rodar de hora em hora (minuto 7, longe das tarefas de minuto cheio).

delete from cron.job_run_details where end_time < now() - interval '7 days' or (end_time is null and start_time < now() - interval '7 days');

select cron.schedule('cron-history-cleanup', '37 7 * * *',
  $$delete from cron.job_run_details where end_time < now() - interval '7 days' or (end_time is null and start_time < now() - interval '7 days')$$);

select cron.schedule('admin-dashboard-refresh', '7 * * * *', 'select public.admin_dashboard_refresh()');
