-- Simulado pela IA do Estudário: recurso novo na mesma fila endurecida de jobs de texto.
--
-- Um simulado é montado no app (planta da prova) e gerado em PARTES de até 30 questões, uma por
-- job. A cota conta partes: 1 parte = até 30 questões. Assim o limite de saída do modelo nunca é
-- estourado e uma parte que falha é refeita sozinha, sem perder o resto.
--
-- Este arquivo só cria o valor do enum, a chave de liberação e os limites (colunas texto). As
-- funções que comparam com o valor novo ficam no arquivo seguinte: o Postgres só deixa usar um
-- valor de enum depois que a transação que o criou termina.

alter type public.ai_feature add value if not exists 'SIMULATION_GENERATION';

alter table public.ai_feature_flags drop constraint ai_feature_flags_key_check;
alter table public.ai_feature_flags add constraint ai_feature_flags_key_check check (
  flag_key in ('AI_BETA_ENABLED', 'SYLLABUS_AI_ENABLED', 'PLAN_AI_ENABLED', 'CONTENT_AI_ENABLED', 'SIMULATION_AI_ENABLED')
);

-- Liga junto com o conteúdo: quem já pode gerar conteúdo pode gerar simulado.
insert into public.ai_feature_flags (flag_key, enabled)
select 'SIMULATION_AI_ENABLED', coalesce((select enabled from public.ai_feature_flags where flag_key = 'CONTENT_AI_ENABLED'), false)
on conflict (flag_key) do nothing;

alter table public.ai_plan_limits drop constraint ai_plan_limits_feature_check;
alter table public.ai_plan_limits add constraint ai_plan_limits_feature_check check (feature in (
  'SYLLABUS_GENERATION', 'PLAN_GENERATION', 'CONTENT_GENERATION', 'QUESTION_BATCH', 'AD_REWARD', 'SIMULATION_GENERATION'
));

-- Partes de até 30 questões (max_per_request = teto de questões por parte).
-- Grátis: 1 parte na vida (o diagnóstico de 20 questões). Essencial: 8 partes/mês (4 simulados de
-- 60). Pro: 40 partes/mês (10 simulados de 120, o tamanho de uma prova real).
insert into public.ai_plan_limits (plan_tier, feature, period_kind, quota_limit, max_per_request, max_per_topic_month)
values
  ('FREE',      'SIMULATION_GENERATION', 'LIFETIME', 1,  30, null),
  ('ESSENCIAL', 'SIMULATION_GENERATION', 'MONTHLY',  8,  30, null),
  ('PRO',       'SIMULATION_GENERATION', 'MONTHLY',  40, 30, null)
on conflict (plan_tier, feature, period_kind) do update
  set quota_limit = excluded.quota_limit, max_per_request = excluded.max_per_request, updated_at = now();
