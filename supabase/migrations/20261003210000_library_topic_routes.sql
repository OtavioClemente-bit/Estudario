-- Enciclopédia: tópico de edital → matérias da biblioteca que o cobrem, escolhidas uma vez pela IA
-- (chamada curta, sem pesquisa) e guardadas. catalog_size registra o tamanho do catálogo na escolha:
-- quando a biblioteca cresce, a rota é refeita (pode haver matéria nova que cobre o tópico).

create table public.library_topic_routes (
  topic_norm text primary key check (char_length(topic_norm) between 2 and 2000),
  subject_norm text not null default '',
  topic_ids text[] not null default '{}',
  coverage text not null check (coverage in ('FULL', 'PARTIAL', 'NONE')),
  catalog_size integer not null,
  created_at timestamptz not null default now(),
  updated_at timestamptz not null default now()
);
alter table public.library_topic_routes enable row level security;
revoke all on table public.library_topic_routes from public, anon, authenticated;
grant select, insert, update, delete on table public.library_topic_routes to service_role;

alter table public.ai_job_costs drop constraint ai_job_costs_kind_check;
alter table public.ai_job_costs add constraint ai_job_costs_kind_check
  check (kind in ('MAIN', 'REVIEW', 'TOPUP', 'BOARD_PROFILE', 'BOARD_NOTE', 'TOPIC_ROUTE'));
