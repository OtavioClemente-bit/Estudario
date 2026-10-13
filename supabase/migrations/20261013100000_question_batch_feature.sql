-- Lote extra de questões ("mais questões") com cota própria, separada das matérias.
--
-- Até aqui todo pedido de conteúdo, matéria completa ou só questões, gastava a cota de
-- CONTENT_GENERATION. Agora o servidor grava o pedido só de questões como QUESTION_BATCH: a matéria
-- conta em CONTENT_GENERATION (inclusive quando sai pronta da biblioteca) e o lote conta em
-- QUESTION_BATCH. Lote é sempre de até 10 questões; max_per_request de CONTENT_GENERATION passa a
-- ser o teto de questões ao criar a matéria.
--
-- Este arquivo só cria o valor do enum, a chave de liberação e os limites. As funções que comparam
-- com o valor novo ficam no arquivo seguinte (o Postgres só deixa usar o valor depois do commit).

alter type public.ai_feature add value if not exists 'QUESTION_BATCH';

alter table public.ai_feature_flags drop constraint ai_feature_flags_key_check;
alter table public.ai_feature_flags add constraint ai_feature_flags_key_check check (
  flag_key in ('AI_BETA_ENABLED', 'SYLLABUS_AI_ENABLED', 'PLAN_AI_ENABLED', 'CONTENT_AI_ENABLED', 'SIMULATION_AI_ENABLED', 'QUESTION_AI_ENABLED')
);

-- Liga junto com o conteúdo: quem já pode gerar conteúdo pode pedir mais questões.
insert into public.ai_feature_flags (flag_key, enabled)
select 'QUESTION_AI_ENABLED', coalesce((select enabled from public.ai_feature_flags where flag_key = 'CONTENT_AI_ENABLED'), false)
on conflict (flag_key) do nothing;

-- SAVED_EDITAIS: quantos editais a conta pode ter salvos (o app e o site conferem; quota_limit é o teto).
alter table public.ai_plan_limits drop constraint ai_plan_limits_feature_check;
alter table public.ai_plan_limits add constraint ai_plan_limits_feature_check check (feature in (
  'SYLLABUS_GENERATION', 'PLAN_GENERATION', 'CONTENT_GENERATION', 'QUESTION_BATCH', 'AD_REWARD', 'SIMULATION_GENERATION',
  'SAVED_EDITAIS'
));

-- Planos de 13/10/2026 (preços iguais: Essencial R$ 24,90, Pro R$ 49,90).
--   Matérias/mês (da biblioteca ou da IA): Grátis 10 (no máximo 2 pela IA, conferido no worker),
--   Essencial 25, Pro 40. Questões ao criar a matéria: 10 / 20 / 30 (da IA, sempre até 10).
--   Lotes extras de 10 questões: Grátis 3/mês e 1 por matéria (max_per_topic_month, contado na
--   vida); Essencial 10/mês; Pro 25/mês, sem limite por matéria.
--   Simulado: Grátis 1 na vida (só o diagnóstico); Essencial 3/mês; Pro 6/mês.
--   Plano de estudo: Grátis 1 na vida; Essencial 3/mês; Pro 10/mês.
--   Edital por PDF: Grátis não; Essencial 2/mês; Pro 5/mês.
--   Editais salvos: Grátis 5, Essencial 15, Pro sem limite prático.
delete from public.ai_plan_limits
where plan_tier in ('FREE', 'ESSENCIAL', 'PRO')
  and feature in ('CONTENT_GENERATION', 'QUESTION_BATCH', 'SIMULATION_GENERATION', 'PLAN_GENERATION', 'SYLLABUS_GENERATION', 'SAVED_EDITAIS');

insert into public.ai_plan_limits (plan_tier, feature, period_kind, quota_limit, max_per_request, max_per_topic_month) values
  ('FREE',      'CONTENT_GENERATION',    'MONTHLY',  10,   10,   null),
  ('FREE',      'QUESTION_BATCH',        'MONTHLY',  3,    10,   1),
  ('FREE',      'SIMULATION_GENERATION', 'LIFETIME', 1,    30,   null),
  ('FREE',      'PLAN_GENERATION',       'LIFETIME', 1,    null, null),
  ('FREE',      'SYLLABUS_GENERATION',   'LIFETIME', 0,    null, null),
  ('FREE',      'SAVED_EDITAIS',         'LIFETIME', 5,    null, null),
  ('ESSENCIAL', 'CONTENT_GENERATION',    'MONTHLY',  25,   20,   null),
  ('ESSENCIAL', 'QUESTION_BATCH',        'MONTHLY',  10,   10,   null),
  ('ESSENCIAL', 'SIMULATION_GENERATION', 'MONTHLY',  3,    30,   null),
  ('ESSENCIAL', 'PLAN_GENERATION',       'MONTHLY',  3,    null, null),
  ('ESSENCIAL', 'SYLLABUS_GENERATION',   'MONTHLY',  2,    null, null),
  ('ESSENCIAL', 'SAVED_EDITAIS',         'LIFETIME', 15,   null, null),
  ('PRO',       'CONTENT_GENERATION',    'MONTHLY',  40,   30,   null),
  ('PRO',       'QUESTION_BATCH',        'MONTHLY',  25,   10,   null),
  ('PRO',       'SIMULATION_GENERATION', 'MONTHLY',  6,    30,   null),
  ('PRO',       'PLAN_GENERATION',       'MONTHLY',  10,   null, null),
  ('PRO',       'SYLLABUS_GENERATION',   'MONTHLY',  5,    null, null),
  ('PRO',       'SAVED_EDITAIS',         'LIFETIME', 1000, null, null);

-- Contas de teste: como o Pro, para testar tudo (lote e matéria no teto), contando no ciclo de teste.
update public.ai_plan_limits set max_per_request = 30 where plan_tier = 'TESTE' and feature = 'CONTENT_GENERATION';
update public.ai_plan_limits set max_per_request = 10, max_per_topic_month = null where plan_tier = 'TESTE' and feature = 'QUESTION_BATCH';
insert into public.ai_plan_limits (plan_tier, feature, period_kind, quota_limit, max_per_request, max_per_topic_month)
values ('TESTE', 'SAVED_EDITAIS', 'LIFETIME', 15, null, null)
on conflict (plan_tier, feature, period_kind) do nothing;
