-- Custo de cada chamada à OpenAI, para saber quanto custa uma geração de verdade: a chamada
-- principal e a do revisor de fatos (que não entrava na conta), com tokens, tokens vindos do
-- cache e quantas pesquisas na web foram feitas. Só o worker escreve; ninguém do app lê.

create table public.ai_job_costs (
  id bigint generated always as identity primary key,
  job_id uuid not null references public.ai_jobs (id) on delete cascade,
  feature text not null,
  kind text not null check (kind in ('MAIN', 'REVIEW')),
  response_id text not null unique,
  model text,
  status text not null,
  input_tokens bigint,
  cached_input_tokens bigint,
  output_tokens bigint,
  web_search_calls integer not null default 0,
  created_at timestamptz not null default now()
);

create index ai_job_costs_created_at on public.ai_job_costs (created_at);
create index ai_job_costs_job on public.ai_job_costs (job_id);

alter table public.ai_job_costs enable row level security;
revoke all on table public.ai_job_costs from public, anon, authenticated;
grant select, insert on table public.ai_job_costs to service_role;

-- Resumo por recurso para consultar no painel: custo médio por geração nos últimos dias.
create or replace view public.ai_job_cost_summary
with (security_invoker = true) as
select
  c.feature,
  date_trunc('day', c.created_at) as day,
  count(distinct c.job_id) as jobs,
  round(avg(c.input_tokens) filter (where c.kind = 'MAIN')) as avg_main_input,
  round(avg(c.output_tokens) filter (where c.kind = 'MAIN')) as avg_main_output,
  round(avg(c.web_search_calls) filter (where c.kind = 'MAIN'), 1) as avg_main_searches,
  round(avg(c.input_tokens) filter (where c.kind = 'REVIEW')) as avg_review_input,
  round(avg(c.output_tokens) filter (where c.kind = 'REVIEW')) as avg_review_output,
  round(avg(c.web_search_calls) filter (where c.kind = 'REVIEW'), 1) as avg_review_searches,
  sum(c.input_tokens) as total_input,
  sum(c.cached_input_tokens) as total_cached_input,
  sum(c.output_tokens) as total_output,
  sum(c.web_search_calls) as total_searches
from public.ai_job_costs c
group by 1, 2;

revoke all on table public.ai_job_cost_summary from public, anon, authenticated;
grant select on table public.ai_job_cost_summary to service_role;
