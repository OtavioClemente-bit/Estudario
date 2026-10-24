-- Recortes magros: tópicos que abrem só um pedaço de uma matéria e, com o filtro do recorte, ficam
-- com poucos flashcards ou sem memorização ativa. A lista é gerada por
-- scripts/biblioteca/recortes-magros.ts (a mesma montagem do servidor) e só a equipe lê, na página
-- Equipe › Cobertura, para completar as matérias à mão.

create table if not exists public.library_thin_slices (
  key text primary key,
  topic_id text not null,
  title text not null,
  chapters text not null default '',
  flashcards integer not null default 0,
  recall integer not null default 0,
  total_flashcards integer not null default 0,
  total_recall integer not null default 0,
  topics jsonb not null default '[]'::jsonb,
  topic_count integer not null default 1,
  computed_at timestamptz not null default now()
);

alter table public.library_thin_slices enable row level security;
revoke all on table public.library_thin_slices from public, anon, authenticated;
grant select, insert, update, delete on table public.library_thin_slices to service_role;

create or replace function public.admin_thin_slices()
returns setof public.library_thin_slices
language plpgsql
stable
security definer
set search_path = public
as $$
begin
  if not public.is_app_admin() then
    raise exception using errcode = '42501', message = 'FORBIDDEN';
  end if;
  return query select * from public.library_thin_slices order by topic_count desc, flashcards + recall asc;
end;
$$;
revoke all on function public.admin_thin_slices() from public, anon;
grant execute on function public.admin_thin_slices() to authenticated;
