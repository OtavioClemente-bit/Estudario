-- Catálogo de editais prontos.
--
-- Cada linha é um edital (concurso + cargo) conferido e publicado a partir de conteudo/editais pelo
-- script scripts/biblioteca (service_role). O app lê o catálogo para a pessoa achar o concurso pelo
-- nome ("pm mg", "polícia federal", "prf") e montar o edital sem IA e sem anexar PDF. Os textos dos
-- tópicos são os mesmos apelidos da biblioteca, então o material de cada tópico sai pronto do banco.
-- Leitura liberada para o app (anon e authenticated) apenas do que está publicado; escrita só pelo script.

create table public.exam_catalog (
  id text primary key check (id ~ '^[a-z0-9]+(-[a-z0-9]+)*(--[a-z0-9]+(-[a-z0-9]+)*)?$' and char_length(id) <= 120),
  -- Sigla ou nome curto como as pessoas procuram ("PMMG", "PRF", "TRT 3ª Região").
  short_name text not null check (char_length(short_name) between 2 and 80),
  -- Nome por extenso do órgão ("Polícia Militar de Minas Gerais").
  agency text not null check (char_length(agency) between 2 and 160),
  role text not null check (char_length(role) between 2 and 200),
  board text check (board is null or char_length(board) between 2 and 80),
  year integer check (year is null or year between 1990 and 2100),
  edital_ref text check (edital_ref is null or char_length(edital_ref) <= 300),
  source_url text check (source_url is null or char_length(source_url) <= 600),
  -- Aviso curto mostrado na escolha (ex.: edital antigo, conteúdo em anexo separado).
  notice text check (notice is null or char_length(notice) <= 400),
  -- Termos de busca já normalizados (minúsculas, sem acento): sigla, órgão, cargo, banca, sinônimos.
  search_norm text not null check (char_length(search_norm) between 2 and 2000),
  -- [{ "name": "Língua Portuguesa", "topics": ["Ortografia oficial.", ...] }, ...]
  subjects jsonb not null check (jsonb_typeof(subjects) = 'array'),
  subject_count integer not null default 0,
  topic_count integer not null default 0,
  -- Tópicos que já têm matéria pronta na biblioteca.
  ready_topic_count integer not null default 0,
  status text not null default 'PUBLISHED' check (status in ('DRAFT', 'PUBLISHED')),
  updated_at timestamptz not null default now()
);

create index exam_catalog_published_idx on public.exam_catalog (status, short_name, role);

alter table public.exam_catalog enable row level security;
revoke all on table public.exam_catalog from public, anon, authenticated;
grant select on table public.exam_catalog to anon, authenticated;

create policy exam_catalog_read_published on public.exam_catalog
  for select to anon, authenticated
  using (status = 'PUBLISHED');
