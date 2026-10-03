-- Biblioteca de matérias prontas.
--
-- Matéria canônica (igual para todo concurso) gerada e conferida fora do app (Codex), importada
-- pelo script scripts/biblioteca. Por cima dela, recortes por banca e cargo ("como cai"), para quem
-- estuda para bombeiro não receber o recorte da PM. Pedido de material cujo tópico existe aqui sai
-- montado do banco, sem chamar a IA. Só o worker e o script (service_role) leem e escrevem.

create table public.library_topics (
  id text primary key check (id ~ '^[a-z0-9]+(-[a-z0-9]+)*\.[a-z0-9]+(-[a-z0-9]+)*$'),
  subject text not null check (char_length(subject) between 2 and 120),
  title text not null check (char_length(title) between 2 and 200),
  version integer not null check (version >= 1),
  status text not null default 'PUBLISHED' check (status in ('DRAFT', 'REVIEWED', 'PUBLISHED')),
  -- Matéria completa: teoria, resumo, flashcards, dicas, recordação ativa, fontes e o banco de questões.
  material jsonb not null,
  question_count integer not null default 0,
  hits integer not null default 0,
  updated_at timestamptz not null default now()
);

-- Como cada edital escreve o tópico ("Função afim", "Funções de 1º grau"), já normalizado
-- (minúsculas, sem acento, espaços simples). Um texto aponta para um tópico só.
create table public.library_topic_aliases (
  alias_norm text primary key check (char_length(alias_norm) between 2 and 400),
  topic_id text not null references public.library_topics (id) on delete cascade
);
create index library_topic_aliases_topic_idx on public.library_topic_aliases (topic_id);

-- "Como cai": tópico × banca (× cargo). role_norm '' vale para qualquer cargo daquela banca.
create table public.library_board_notes (
  topic_id text not null references public.library_topics (id) on delete cascade,
  board_norm text not null check (char_length(board_norm) between 2 and 120),
  role_norm text not null default '',
  board text not null,
  role text,
  version integer not null check (version >= 1),
  note jsonb not null,
  updated_at timestamptz not null default now(),
  primary key (topic_id, board_norm, role_norm)
);

-- Tópicos pedidos que não estavam na biblioteca: a fila do que gerar a seguir.
create table public.library_misses (
  subject_norm text not null,
  topic_norm text not null,
  board_norm text not null default '',
  subject text not null,
  topic text not null,
  board text,
  requests integer not null default 1,
  last_requested_at timestamptz not null default now(),
  primary key (subject_norm, topic_norm, board_norm)
);

alter table public.library_topics enable row level security;
alter table public.library_topic_aliases enable row level security;
alter table public.library_board_notes enable row level security;
alter table public.library_misses enable row level security;
revoke all on table public.library_topics, public.library_topic_aliases, public.library_board_notes, public.library_misses
  from public, anon, authenticated;
grant select, insert, update, delete on table public.library_topics, public.library_topic_aliases, public.library_board_notes, public.library_misses
  to service_role;

-- Busca do worker: o primeiro texto do pedido que bater num apelido de matéria publicada, com o
-- recorte da banca (o do cargo, se houver; senão o geral da banca).
create function public.library_lookup(p_aliases text[], p_board text, p_role text)
returns table (topic_id text, version integer, material jsonb, note jsonb)
language sql
stable
security definer
set search_path = public
as $$
  with hit as (
    select t.id, t.version, t.material
    from unnest(p_aliases) with ordinality as a(alias_norm, ord)
    join public.library_topic_aliases la on la.alias_norm = a.alias_norm
    join public.library_topics t on t.id = la.topic_id and t.status = 'PUBLISHED'
    order by a.ord
    limit 1
  )
  select hit.id, hit.version, hit.material,
    (select n.note from public.library_board_notes n
      where n.topic_id = hit.id and n.board_norm = coalesce(p_board, '')
        and n.role_norm in ('', coalesce(p_role, ''))
      order by n.role_norm desc limit 1)
  from hit;
$$;
revoke all on function public.library_lookup(text[], text, text) from public, anon, authenticated;
grant execute on function public.library_lookup(text[], text, text) to service_role;

create function public.library_record_hit(p_topic_id text)
returns void
language sql
security definer
set search_path = public
as $$
  update public.library_topics set hits = hits + 1 where id = p_topic_id;
$$;
revoke all on function public.library_record_hit(text) from public, anon, authenticated;
grant execute on function public.library_record_hit(text) to service_role;

create function public.library_record_miss(p_subject text, p_topic text, p_board text,
  p_subject_norm text, p_topic_norm text, p_board_norm text)
returns void
language sql
security definer
set search_path = public
as $$
  insert into public.library_misses (subject_norm, topic_norm, board_norm, subject, topic, board)
  values (p_subject_norm, p_topic_norm, coalesce(p_board_norm, ''), left(p_subject, 200), left(p_topic, 400), left(p_board, 120))
  on conflict (subject_norm, topic_norm, board_norm)
  do update set requests = library_misses.requests + 1, last_requested_at = now();
$$;
revoke all on function public.library_record_miss(text, text, text, text, text, text) from public, anon, authenticated;
grant execute on function public.library_record_miss(text, text, text, text, text, text) to service_role;
