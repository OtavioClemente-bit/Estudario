-- Incidência de cada tópico da biblioteca nas provas de verdade: quantas questões oficiais (banca,
-- órgão, ano) caíram nele, por banca e no total. O plano usa para dar mais treino e revisão ao que
-- mais cai e, quando o edital não cabe, deixar de fora primeiro o que quase nunca cai.
--
-- Escrita só pelo script scripts/biblioteca/incidencia.ts (service role); leitura pela função
-- library-incidence, que devolve a conta por apelido de tópico (a mesma regra do library-ready).

create table if not exists public.library_topic_incidence (
  topic_id text not null references public.library_topics (id) on delete cascade,
  -- '' = todas as bancas somadas.
  board_norm text not null default '',
  real_count integer not null check (real_count >= 0),
  updated_at timestamptz not null default now(),
  primary key (topic_id, board_norm)
);
alter table public.library_topic_incidence enable row level security;
revoke all on table public.library_topic_incidence from public, anon, authenticated;

-- Para cada texto de tópico pedido: as questões oficiais na banca pedida e no total.
create or replace function public.library_incidence_norms(p_norms text[], p_board text)
returns table (norm text, board_count integer, all_count integer)
language sql
stable
security definer
set search_path = public
as $$
  with hits as (
    select a.alias_norm as norm, a.topic_id
    from public.library_topic_aliases a
    where a.alias_norm = any(p_norms)
    union
    select r.topic_norm, t.topic_id
    from public.library_topic_routes r, unnest(r.topic_ids) as t(topic_id)
    where r.topic_norm = any(p_norms) and r.coverage = 'FULL'
  )
  select h.norm,
    coalesce(sum(i.real_count) filter (where i.board_norm = coalesce(p_board, '') and coalesce(p_board, '') <> ''), 0)::integer,
    coalesce(sum(i.real_count) filter (where i.board_norm = ''), 0)::integer
  from hits h
  left join public.library_topic_incidence i on i.topic_id = h.topic_id
  group by h.norm
$$;
revoke all on function public.library_incidence_norms(text[], text) from public, anon, authenticated;
grant execute on function public.library_incidence_norms(text[], text) to service_role;
