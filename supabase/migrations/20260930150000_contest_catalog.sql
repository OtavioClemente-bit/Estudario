-- Catálogo de concursos prontos.
--
-- Quem monta um edital (com o Estudário ou à mão) pode enviá-lo para o catálogo. O envio fica
-- PENDENTE até um administrador aprovar; aprovado, qualquer pessoa acha o concurso pelo nome no app
-- e recebe as matérias na hora, sem gerar de novo. O ano vem sempre junto, para a pessoa saber se
-- o edital pode estar desatualizado. O catálogo guarda a estrutura (matérias e tópicos) no mesmo
-- formato .estudo que o app já importa, e o link oficial do edital; o PDF não é guardado.

create extension if not exists pg_trgm with schema extensions;
create extension if not exists unaccent with schema extensions;

create table public.app_admins (
  user_id uuid primary key references auth.users (id) on delete cascade,
  created_at timestamptz not null default now()
);
alter table public.app_admins enable row level security;
revoke all on public.app_admins from public, anon, authenticated;

create function public.is_app_admin()
returns boolean
language sql
stable
security definer
set search_path = public
as $$
  select exists (select 1 from public.app_admins where user_id = auth.uid());
$$;
revoke all on function public.is_app_admin() from public, anon;
grant execute on function public.is_app_admin() to authenticated;

create table public.contest_catalog (
  id uuid primary key default gen_random_uuid(),
  name text not null check (char_length(name) between 2 and 160),
  role text not null check (char_length(role) between 2 and 160),
  board text check (board is null or char_length(board) <= 120),
  agency text check (agency is null or char_length(agency) <= 160),
  year integer not null check (year between 1990 and 2100),
  edital_url text check (edital_url is null or (char_length(edital_url) <= 2000 and edital_url ~* '^https?://')),
  estudo jsonb not null,
  subject_count integer not null default 0 check (subject_count >= 0),
  topic_count integer not null default 0 check (topic_count >= 0),
  status text not null default 'PENDING' check (status in ('PENDING', 'PUBLISHED', 'REJECTED')),
  submitted_by uuid references auth.users (id) on delete set null,
  submitted_at timestamptz not null default now(),
  reviewed_by uuid references auth.users (id) on delete set null,
  reviewed_at timestamptz,
  review_note text check (review_note is null or char_length(review_note) <= 500),
  search_text text not null default '',
  updated_at timestamptz not null default now()
);

-- Um mesmo concurso/cargo/ano publicado uma vez só.
create unique index contest_catalog_published_unique
  on public.contest_catalog (lower(name), lower(role), year) where status = 'PUBLISHED';
create index contest_catalog_search_idx
  on public.contest_catalog using gin (search_text extensions.gin_trgm_ops) where status = 'PUBLISHED';
create index contest_catalog_pending_idx on public.contest_catalog (submitted_at) where status = 'PENDING';

-- Texto de busca sem acento e em minúsculas: "PMMG soldado" acha "Soldado - Polícia Militar (PMMG)".
create function public.contest_catalog_search_text()
returns trigger
language plpgsql
set search_path = public, extensions
as $$
begin
  new.search_text := lower(extensions.unaccent(concat_ws(' ', new.name, new.role, new.board, new.agency, new.year::text)));
  new.updated_at := now();
  return new;
end;
$$;

create trigger contest_catalog_search_text
  before insert or update on public.contest_catalog
  for each row execute function public.contest_catalog_search_text();

alter table public.contest_catalog enable row level security;
revoke all on public.contest_catalog from public, anon, authenticated;
grant select on public.contest_catalog to anon, authenticated;

-- Publicados: qualquer um lê. Os próprios envios: quem enviou lê (para ver se foi aprovado).
create policy contest_catalog_read_published on public.contest_catalog
  for select to anon, authenticated using (status = 'PUBLISHED');
create policy contest_catalog_read_own on public.contest_catalog
  for select to authenticated using (submitted_by = auth.uid());

-- Busca: todas as palavras precisam aparecer; os mais recentes primeiro.
create function public.search_contest_catalog(p_query text)
returns table (
  id uuid, name text, role text, board text, agency text, year integer, edital_url text,
  subject_count integer, topic_count integer
)
language sql
stable
security definer
set search_path = public, extensions
as $$
  with terms as (
    select array_remove(regexp_split_to_array(lower(extensions.unaccent(coalesce(p_query, ''))), '\s+'), '') as words
  )
  select c.id, c.name, c.role, c.board, c.agency, c.year, c.edital_url, c.subject_count, c.topic_count
  from public.contest_catalog c, terms
  where c.status = 'PUBLISHED'
    and cardinality(terms.words) > 0
    and not exists (select 1 from unnest(terms.words) w where position(w in c.search_text) = 0)
  order by c.year desc, similarity(c.search_text, array_to_string(terms.words, ' ')) desc, c.name
  limit 30;
$$;
revoke all on function public.search_contest_catalog(text) from public;
grant execute on function public.search_contest_catalog(text) to anon, authenticated;

-- Enviar para aprovação: precisa estar logado; no máximo 10 envios pendentes por pessoa e
-- pacote de até 2 MB, para ninguém encher o banco.
create function public.submit_contest_catalog(
  p_name text, p_role text, p_board text, p_agency text, p_year integer, p_edital_url text,
  p_estudo jsonb, p_subject_count integer, p_topic_count integer
)
returns uuid
language plpgsql
security definer
set search_path = public
as $$
declare
  v_user uuid := auth.uid();
  v_id uuid;
begin
  if v_user is null then
    raise exception using errcode = '42501', message = 'AUTH_REQUIRED';
  end if;
  if (select count(*) from public.contest_catalog where submitted_by = v_user and status = 'PENDING') >= 10 then
    raise exception using errcode = 'P0001', message = 'TOO_MANY_PENDING';
  end if;
  if p_estudo is null or pg_column_size(p_estudo) > 2 * 1024 * 1024 then
    raise exception using errcode = 'P0001', message = 'INVALID_PACKAGE';
  end if;
  insert into public.contest_catalog (name, role, board, agency, year, edital_url, estudo, subject_count, topic_count, submitted_by)
  values (trim(p_name), trim(p_role), nullif(trim(p_board), ''), nullif(trim(p_agency), ''), p_year,
          nullif(trim(p_edital_url), ''), p_estudo, greatest(p_subject_count, 0), greatest(p_topic_count, 0), v_user)
  returning id into v_id;
  return v_id;
end;
$$;
revoke all on function public.submit_contest_catalog(text, text, text, text, integer, text, jsonb, integer, integer) from public, anon;
grant execute on function public.submit_contest_catalog(text, text, text, text, integer, text, jsonb, integer, integer) to authenticated;

-- Fila de aprovação: só administradores.
create function public.list_pending_contest_catalog()
returns table (
  id uuid, name text, role text, board text, agency text, year integer, edital_url text,
  subject_count integer, topic_count integer, submitted_at timestamptz, estudo jsonb
)
language plpgsql
stable
security definer
set search_path = public
as $$
begin
  if not public.is_app_admin() then
    raise exception using errcode = '42501', message = 'ADMIN_REQUIRED';
  end if;
  return query
    select c.id, c.name, c.role, c.board, c.agency, c.year, c.edital_url, c.subject_count, c.topic_count, c.submitted_at, c.estudo
    from public.contest_catalog c
    where c.status = 'PENDING'
    order by c.submitted_at
    limit 100;
end;
$$;
revoke all on function public.list_pending_contest_catalog() from public, anon;
grant execute on function public.list_pending_contest_catalog() to authenticated;

-- Aprovar ou recusar. Aprovar um concurso/cargo/ano que já existe substitui o antigo (versão nova
-- do mesmo edital), para a busca nunca mostrar dois iguais.
create function public.review_contest_catalog(p_id uuid, p_approve boolean, p_note text default null)
returns void
language plpgsql
security definer
set search_path = public
as $$
declare
  v_row public.contest_catalog;
begin
  if not public.is_app_admin() then
    raise exception using errcode = '42501', message = 'ADMIN_REQUIRED';
  end if;
  select * into v_row from public.contest_catalog where id = p_id and status = 'PENDING' for update;
  if not found then
    raise exception using errcode = 'P0001', message = 'NOT_PENDING';
  end if;
  if p_approve then
    update public.contest_catalog set status = 'REJECTED', review_note = 'Substituído por uma versão mais nova.',
      reviewed_by = auth.uid(), reviewed_at = now()
    where status = 'PUBLISHED' and lower(name) = lower(v_row.name) and lower(role) = lower(v_row.role) and year = v_row.year;
  end if;
  update public.contest_catalog
    set status = case when p_approve then 'PUBLISHED' else 'REJECTED' end,
        reviewed_by = auth.uid(), reviewed_at = now(), review_note = nullif(trim(p_note), '')
  where id = p_id;
end;
$$;
revoke all on function public.review_contest_catalog(uuid, boolean, text) from public, anon;
grant execute on function public.review_contest_catalog(uuid, boolean, text) to authenticated;

-- O dono do app é o primeiro administrador.
insert into public.app_admins (user_id)
select id from auth.users where lower(email) = 'otavioclemente17@gmail.com'
on conflict do nothing;
