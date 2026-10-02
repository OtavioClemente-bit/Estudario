-- Questões que saíram do material (conserto, revisor, reposição) e o motivo. Serve para saber o
-- que o modelo mais erra e ajustar o pedido. Só o worker escreve; ninguém do app lê.
create table public.ai_question_drops (
  id bigint generated always as identity primary key,
  job_id uuid not null references public.ai_jobs (id) on delete cascade,
  stage text not null check (stage in ('GENERATION', 'REVIEW', 'TOPUP')),
  reason text not null,
  statement text not null,
  created_at timestamptz not null default now()
);

create index ai_question_drops_job on public.ai_question_drops (job_id);
create index ai_question_drops_reason on public.ai_question_drops (reason, created_at);

alter table public.ai_question_drops enable row level security;
revoke all on table public.ai_question_drops from public, anon, authenticated;
grant select, insert on table public.ai_question_drops to service_role;
