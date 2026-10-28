-- A biblioteca sai do banco e vai para o armazenamento de arquivos.
--
-- No plano grátis o banco tem 500 MB e já estava em ~496 MB, quase todo ocupado pelas matérias
-- prontas (library_topics.material) e pelos recortes de banca (library_board_notes.note): textos
-- grandes que quase nunca mudam. Agora cada matéria e cada recorte é um arquivo JSON no bucket
-- privado "library" (o armazenamento tem 1 GB separado) e o banco guarda só o caminho.
--
-- Compatível com o que já está no ar: enquanto uma linha ainda tiver o JSON no banco, ele vale;
-- com o JSON movido (material/note nulos), o worker busca o arquivo pelo caminho.

insert into storage.buckets (id, name, public, file_size_limit, allowed_mime_types)
values ('library', 'library', false, 20971520, array['application/json'])
on conflict (id) do nothing;
-- Sem políticas para anon/authenticated: só o service_role (worker e scripts) lê e grava.

alter table public.library_topics alter column material drop not null;
alter table public.library_topics add column if not exists material_path text;
-- O que a matéria cobre (material.scope.covers), usado pelo roteador de tópicos sem abrir o arquivo.
alter table public.library_topics add column if not exists covers text;
update public.library_topics set covers = material->'scope'->>'covers' where covers is null and material is not null;
alter table public.library_topics add constraint library_topics_material_or_path
  check (material is not null or material_path is not null) not valid;

alter table public.library_board_notes alter column note drop not null;
alter table public.library_board_notes add column if not exists note_path text;
alter table public.library_board_notes add constraint library_board_notes_note_or_path
  check (note is not null or note_path is not null) not valid;

drop function public.library_lookup(text[], text, text);
create function public.library_lookup(p_aliases text[], p_board text, p_role text)
returns table (topic_id text, version integer, material jsonb, note jsonb, focus jsonb, material_path text, note_path text)
language sql
stable
security definer
set search_path = public
as $$
  with hit as (
    select t.id, t.version, t.material, t.material_path, la.focus
    from unnest(p_aliases) with ordinality as a(alias_norm, ord)
    join public.library_topic_aliases la on la.alias_norm = a.alias_norm
    join public.library_topics t on t.id = la.topic_id and t.status = 'PUBLISHED'
    order by a.ord
    limit 1
  ),
  board_note as (
    select n.note, n.note_path from public.library_board_notes n, hit
    where n.topic_id = hit.id and n.board_norm = coalesce(p_board, '')
      and n.role_norm in ('', coalesce(p_role, ''))
    order by n.role_norm desc limit 1
  )
  select hit.id, hit.version, hit.material, (select note from board_note), hit.focus, hit.material_path, (select note_path from board_note)
  from hit;
$$;
revoke all on function public.library_lookup(text[], text, text) from public, anon, authenticated;
grant execute on function public.library_lookup(text[], text, text) to service_role;
