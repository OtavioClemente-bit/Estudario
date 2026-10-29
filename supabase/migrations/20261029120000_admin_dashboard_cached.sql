-- Equipe › Cobertura › Painel: o resultado pronto, recalculado a cada 15 minutos.
--
-- Mesmo com as consultas rápidas, no banco do plano grátis (pouca memória) as tabelas grandes da
-- biblioteca saem do cache, e cada abertura relia tudo do disco: média de 5 s e picos acima dos 8 s
-- que o site tem, com o erro "statement timeout". Agora a conta (admin_team_dashboard_compute) roda
-- pelo agendador (pg_cron, sem o limite do site) e guarda o resultado; o painel só lê o pronto, com a
-- hora em que foi calculado ("cachedAt").

create table if not exists public.admin_dashboard_cache (
  id integer primary key default 1 check (id = 1),
  value jsonb not null,
  computed_at timestamptz not null default now()
);
alter table public.admin_dashboard_cache enable row level security;
revoke all on table public.admin_dashboard_cache from public, anon, authenticated;

create or replace function public.admin_team_dashboard_compute()
returns jsonb
language plpgsql
stable
security definer
set search_path = public
as $$
declare
  result jsonb;
begin

  with lib as (
    select id, subject, title, status, question_count, hits, updated_at from public.library_topics
  ),
  pub as (select * from lib where status = 'PUBLISHED'),
  -- Pelo índice de topic_id (antes juntava todas as ligações a cada chamada: 7 s).
  unlinked as (
    select p.* from pub p
    where not exists (select 1 from public.library_topic_aliases a where a.topic_id = p.id)
      and not exists (select 1 from public.library_topic_routes r where p.id = any(r.topic_ids))
  ),
  -- Só as colunas usadas: com "*" o JSON de matérias de cada edital (48 MB) ia junto para a memória.
  cat as (select id, short_name, role, board, topic_count, ready_topic_count from public.exam_catalog where status = 'PUBLISHED')
  select jsonb_build_object(
    'generatedAt', now(),
    'library', (select jsonb_build_object(
        'materias', (select count(*) from pub),
        'drafts', (select count(*) from lib where status <> 'PUBLISHED'),
        'subjects', (select count(distinct subject) from pub),
        'questions', (select coalesce(sum(question_count), 0) from pub),
        'noQuestions', (select count(*) from pub where question_count = 0),
        'fewQuestions', (select count(*) from pub where question_count between 1 and 9),
        'hits', (select coalesce(sum(hits), 0) from pub),
        'updated7d', (select count(*) from lib where updated_at > now() - interval '7 days'),
        'neverUsed', (select count(*) from pub where hits = 0)
      )),
    'bySubject', (select coalesce(jsonb_agg(jsonb_build_object('subject', subject, 'materias', n, 'questions', q, 'hits', h) order by n desc), '[]'::jsonb)
      from (select subject, count(*) as n, coalesce(sum(question_count), 0) as q, coalesce(sum(hits), 0) as h from pub group by subject order by count(*) desc limit 40) s),
    'topHits', (select coalesce(jsonb_agg(jsonb_build_object('id', id, 'subject', subject, 'title', title, 'hits', hits, 'questions', question_count) order by hits desc), '[]'::jsonb)
      from (select * from pub order by hits desc limit 10) t),
    'routes', (select jsonb_build_object(
        'total', count(*),
        'full', count(*) filter (where coverage = 'FULL'),
        'partial', count(*) filter (where coverage = 'PARTIAL'),
        'none', count(*) filter (where coverage = 'NONE'),
        'aliases', (select count(*) from public.library_topic_aliases)
      ) from public.library_topic_routes),
    'unlinked', (select jsonb_build_object(
        'count', (select count(*) from unlinked),
        'items', (select coalesce(jsonb_agg(jsonb_build_object('id', id, 'subject', subject, 'title', title, 'questions', question_count) order by subject, title), '[]'::jsonb)
                  from (select * from unlinked order by subject, title limit 300) u)
      )),
    'catalog', (select jsonb_build_object(
        'editais', count(*),
        'topics', coalesce(sum(topic_count), 0),
        'ready', coalesce(sum(ready_topic_count), 0),
        'full', count(*) filter (where topic_count > 0 and ready_topic_count >= topic_count),
        'zero', count(*) filter (where ready_topic_count = 0),
        'boards', count(distinct board),
        -- Matérias diferentes no catálogo: pelos nomes da biblioteca (abrir o JSON de 4 mil editais levava 3 s).
        'subjects', (select count(distinct lower(subject)) from pub),
        'drafts', (select count(*) from public.exam_catalog where status <> 'PUBLISHED')
      ) from cat),
    'catalogZero', (select coalesce(jsonb_agg(jsonb_build_object('id', id, 'name', short_name, 'role', role, 'board', board, 'topics', topic_count) order by topic_count desc), '[]'::jsonb)
      from (select * from cat where ready_topic_count = 0 order by topic_count desc limit 300) z),
    'catalogLeast', (select coalesce(jsonb_agg(jsonb_build_object('id', id, 'name', short_name, 'role', role, 'board', board, 'topics', topic_count, 'ready', ready_topic_count) order by pct), '[]'::jsonb)
      from (select *, ready_topic_count::numeric / topic_count as pct from cat where topic_count > 0 and ready_topic_count > 0 order by ready_topic_count::numeric / topic_count limit 20) l),
    'misses', (select jsonb_build_object(
        'count', (select count(*) from public.library_misses),
        'requests', (select coalesce(sum(requests), 0) from public.library_misses),
        'top', (select coalesce(jsonb_agg(jsonb_build_object('subject', subject, 'topic', topic, 'board', board, 'requests', requests, 'last', last_requested_at) order by requests desc), '[]'::jsonb)
                from (select * from public.library_misses order by requests desc, last_requested_at desc limit 25) m)
      )),
    'reports', (select jsonb_build_object(
        'open', count(*) filter (where status = 'OPEN'),
        'fixed', count(*) filter (where status = 'FIXED'),
        'week', count(*) filter (where created_at > now() - interval '7 days'),
        'byKind', (select coalesce(jsonb_object_agg(kind, n), '{}'::jsonb) from (select kind, count(*) as n from public.content_reports where status = 'OPEN' group by kind) k)
      ) from public.content_reports),
    'simulados', (select jsonb_build_object(
        'count', count(*),
        'editais', count(distinct catalog_id),
        'items', coalesce(sum(item_count), 0)
      ) from public.exam_simulation where status = 'PUBLISHED'),
    'thinSlices', (select count(*) from public.library_thin_slices),
    'users', (select jsonb_build_object(
        'total', count(*),
        'new7d', count(*) filter (where created_at > now() - interval '7 days'),
        'new30d', count(*) filter (where created_at > now() - interval '30 days')
      ) from public.profiles),
    'subscriptions', (select jsonb_build_object(
        'active', count(*) filter (where expires_at > now()),
        'byTier', (select coalesce(jsonb_object_agg(coalesce(plan_tier, '?'), n), '{}'::jsonb)
                   from (select plan_tier, count(*) as n from public.play_subscriptions where expires_at > now() group by plan_tier) t)
      ) from public.play_subscriptions),
    'userSyllabi', (select jsonb_build_object(
        'total', count(*),
        'owners', count(distinct owner_user_id),
        'pendingCatalog', (select count(*) from public.contest_catalog where status = 'PENDING')
      ) from public.user_syllabi),
    'jobs7d', (select jsonb_build_object(
        'total', count(*),
        'failed', count(*) filter (where status::text ilike 'fail%'),
        'byFeature', (select coalesce(jsonb_object_agg(f, n), '{}'::jsonb)
                      from (select feature::text as f, count(*) as n from public.ai_jobs where created_at > now() - interval '7 days' group by feature::text) j)
      ) from public.ai_jobs where created_at > now() - interval '7 days')
  ) into result;

  return result;
end;
$$;

revoke all on function public.admin_team_dashboard_compute() from public, anon, authenticated;

-- Recalcula e guarda (o agendador chama; não tem o limite de 8 s do site).
create or replace function public.admin_dashboard_refresh()
returns void
language sql
security definer
set search_path = public
as $$
  insert into public.admin_dashboard_cache (id, value, computed_at)
  values (1, public.admin_team_dashboard_compute(), now())
  on conflict (id) do update set value = excluded.value, computed_at = excluded.computed_at;
$$;
revoke all on function public.admin_dashboard_refresh() from public, anon, authenticated;

-- O que o site chama: só a equipe; devolve o pronto (com "cachedAt"). Sem nada guardado ainda, calcula.
create or replace function public.admin_team_dashboard()
returns jsonb
language plpgsql
stable
security definer
set search_path = public
as $$
declare
  cached record;
begin
  if not public.is_app_admin() then
    raise exception using errcode = '42501', message = 'FORBIDDEN';
  end if;
  select value, computed_at into cached from public.admin_dashboard_cache where id = 1;
  if found then
    return cached.value || jsonb_build_object('cachedAt', cached.computed_at);
  end if;
  return public.admin_team_dashboard_compute();
end;
$$;
revoke all on function public.admin_team_dashboard() from public, anon;
grant execute on function public.admin_team_dashboard() to authenticated;

select public.admin_dashboard_refresh();
select cron.schedule('admin-dashboard-refresh', '*/15 * * * *', 'select public.admin_dashboard_refresh()');
