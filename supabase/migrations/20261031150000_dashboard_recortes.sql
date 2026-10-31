-- Equipe › Cobertura: o painel passa a contar os recortes por banca. As questões que os geradores
-- (agy, ChatGPT, Gemini, Codex) acrescentam vão para os recortes, e o painel só somava as questões de
-- dentro das matérias: a pessoa publicava questões e o número não mudava. Como o recorte mora num
-- arquivo (note_path), o banco guarda a contagem de questões dele, gravada pelo publicador.

alter table public.library_board_notes add column if not exists question_count integer not null default 0 check (question_count >= 0);

create or replace function public.admin_dashboard_refresh()
returns void
language sql
security definer
set search_path = public
as $$
  insert into public.admin_dashboard_cache (id, value, computed_at)
  values (
    1,
    public.admin_team_dashboard_compute() || jsonb_build_object('recortes', (
      select jsonb_build_object(
        'count', count(*),
        'questions', coalesce(sum(question_count), 0),
        'boards', count(distinct board_norm),
        'updated7d', count(*) filter (where updated_at > now() - interval '7 days'),
        'questions7d', coalesce(sum(question_count) filter (where updated_at > now() - interval '7 days'), 0)
      ) from public.library_board_notes)),
    now()
  )
  on conflict (id) do update set value = excluded.value, computed_at = excluded.computed_at;
$$;
revoke all on function public.admin_dashboard_refresh() from public, anon, authenticated;
