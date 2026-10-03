-- Recorte automático de banca: o perfil de cada banca é pesquisado uma vez (com busca na web) e
-- o recorte de cada tópico é escrito a partir dele uma vez, ficando guardado para todo mundo.

create table public.library_board_profiles (
  board_norm text primary key check (char_length(board_norm) between 2 and 120),
  board text not null check (char_length(board) between 2 and 120),
  profile jsonb not null,
  created_at timestamptz not null default now(),
  updated_at timestamptz not null default now()
);
alter table public.library_board_profiles enable row level security;
revoke all on table public.library_board_profiles from public, anon, authenticated;
grant select, insert, update, delete on table public.library_board_profiles to service_role;

-- MANUAL: escrito e revisado em conteudo/recortes; AUTO: feito pelo worker a partir do perfil.
-- O importador refaz só os MANUAL e, quando há um MANUAL da mesma banca, ele substitui o AUTO.
alter table public.library_board_notes
  add column origin text not null default 'MANUAL' check (origin in ('MANUAL', 'AUTO'));

alter table public.ai_job_costs drop constraint ai_job_costs_kind_check;
alter table public.ai_job_costs add constraint ai_job_costs_kind_check
  check (kind in ('MAIN', 'REVIEW', 'TOPUP', 'BOARD_PROFILE', 'BOARD_NOTE'));
