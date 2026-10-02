-- Material de tópico já gerado e revisado, reaproveitado por quem pede o mesmo tópico do mesmo
-- concurso com as mesmas opções: entrega na hora e sem custo de IA. A chave é um hash do pedido
-- (sem dado da pessoa). Quem já recebeu um material e pede de novo quer outro: ganha uma geração
-- nova, que passa a ser a versão guardada. Só o worker lê e escreve.

create table public.ai_content_cache (
  cache_key text not null,
  prompt_version text not null,
  proposal jsonb not null,
  source_job_id uuid references public.ai_jobs (id) on delete set null,
  hits integer not null default 0,
  created_at timestamptz not null default now(),
  primary key (cache_key, prompt_version)
);

create table public.ai_content_cache_served (
  cache_key text not null,
  user_id uuid not null references auth.users (id) on delete cascade,
  served_at timestamptz not null default now(),
  primary key (cache_key, user_id)
);

alter table public.ai_content_cache enable row level security;
alter table public.ai_content_cache_served enable row level security;
revoke all on table public.ai_content_cache, public.ai_content_cache_served from public, anon, authenticated;
grant select, insert, update, delete on table public.ai_content_cache, public.ai_content_cache_served to service_role;
