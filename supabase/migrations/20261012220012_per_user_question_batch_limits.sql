-- Limites de quantidade de questões específicos de uma conta TESTE.
-- Preserva quota_limit (créditos de geração) e todos os outros planos/usuários.
-- A liberação individual deve ser feita pelo administrador, sem e-mails no repositório.
alter table public.ai_user_quota_overrides
  add column if not exists max_per_request integer check (max_per_request is null or max_per_request > 0),
  add column if not exists max_per_topic_month integer check (max_per_topic_month is null or max_per_topic_month > 0);

comment on column public.ai_user_quota_overrides.max_per_request is
  'Override individual de questões por pedido no plano TESTE, sem alterar a cota de geração.';
comment on column public.ai_user_quota_overrides.max_per_topic_month is
  'Override individual de questões por tópico no mês no plano TESTE.';
