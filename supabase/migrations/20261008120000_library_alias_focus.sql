-- Foco do apelido: um tópico estreito de edital ("Legítima defesa.") ligado a uma matéria inteira
-- ("Teoria do crime") abre a matéria no foco — primeiro os capítulos e as questões daquele tópico,
-- depois o resto. {"capitulos": [3], "questoes": ["q12","q31"]}; nulo = a matéria inteira.
alter table public.library_topic_aliases add column focus jsonb
  check (focus is null or (jsonb_typeof(focus) = 'object' and octet_length(focus::text) <= 4000));

-- Mesma busca, agora devolvendo o foco do apelido que bateu (o tipo de retorno muda: recria).
drop function public.library_lookup(text[], text, text);
create function public.library_lookup(p_aliases text[], p_board text, p_role text)
returns table (topic_id text, version integer, material jsonb, note jsonb, focus jsonb)
language sql
stable
security definer
set search_path = public
as $$
  with hit as (
    select t.id, t.version, t.material, la.focus
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
      order by n.role_norm desc limit 1),
    hit.focus
  from hit;
$$;
revoke all on function public.library_lookup(text[], text, text) from public, anon, authenticated;
grant execute on function public.library_lookup(text[], text, text) to service_role;
