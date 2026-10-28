-- Painel da equipe: os números da biblioteca, das ligações com o catálogo de editais, do que as
-- pessoas pedem e não tem, dos reportes, dos simulados prontos e das contas, num JSON só. Só lê; só
-- a equipe (is_app_admin) chama. Mostrado em Equipe › Cobertura › Painel.

create or replace function public.admin_team_dashboard()
returns jsonb
language plpgsql
stable
security definer
set search_path = public
as $$
declare
  result jsonb;
begin
  if not public.is_app_admin() then
    raise exception using errcode = '42501', message = 'FORBIDDEN';
  end if;

  with lib as (
    select id, subject, title, status, question_count, hits, updated_at from public.library_topics
  ),
  pub as (select * from lib where status = 'PUBLISHED'),
  linked as (
    select distinct unnest(topic_ids) as id from public.library_topic_routes
    union
    select topic_id from public.library_topic_aliases
  ),
  unlinked as (
    select p.* from pub p where not exists (select 1 from linked k where k.id = p.id)
  ),
  cat as (select * from public.exam_catalog where status = 'PUBLISHED')
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
        'subjects', (select count(distinct lower(s->>'name')) from cat c2, jsonb_array_elements(c2.subjects) s),
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

revoke all on function public.admin_team_dashboard() from public, anon;
grant execute on function public.admin_team_dashboard() to authenticated;
