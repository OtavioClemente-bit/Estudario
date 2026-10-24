-- Correção da migração anterior (20261024120000), já aplicada:
--
-- 1. PLAN_GENERATION do Grátis volta para 1. Esse número também é o limite do plano de estudo montado
--    no próprio app e no site (Entitlements), sem IA; com 0, o Grátis não conseguia criar o plano. O
--    plano escrito pela IA continua barrado na criação do pedido e no worker.
-- 2. library_ready_norms: quais textos de tópico têm matéria pronta (apelido direto ou rota completa
--    já guardada). A função library-ready usa para o plano do Grátis só levar tópicos prontos e para a
--    página de cobertura da equipe.

update public.ai_plan_limits set quota_limit = 1, updated_at = now()
where plan_tier = 'FREE' and feature = 'PLAN_GENERATION';

create or replace function public.library_ready_norms(p_norms text[])
returns setof text
language sql
stable
security definer
set search_path = public
as $$
  select a.alias_norm from public.library_topic_aliases a
  join public.library_topics t on t.id = a.topic_id and t.status = 'PUBLISHED'
  where a.alias_norm = any(p_norms)
  union
  select r.topic_norm from public.library_topic_routes r
  where r.topic_norm = any(p_norms) and r.coverage = 'FULL' and cardinality(r.topic_ids) > 0
$$;

revoke all on function public.library_ready_norms(text[]) from public, anon, authenticated;
grant execute on function public.library_ready_norms(text[]) to service_role;
