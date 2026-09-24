select no_plan();

select has_table('public', 'profiles', 'profiles table exists');
select has_table('public', 'ai_feature_flags', 'AI feature flags table exists');
select has_table('public', 'ai_jobs', 'AI jobs table exists');
select has_table('public', 'ai_quota_reservations', 'quota reservations table exists');
select has_table('public', 'ai_quota_usage', 'quota usage table exists');
select has_table('public', 'user_syllabi', 'private syllabi table exists');
select has_table('public', 'user_syllabus_subjects', 'private syllabus subjects table exists');
select has_table('public', 'user_syllabus_topics', 'private syllabus topics table exists');

select has_type('public', 'ai_feature', 'AI feature enum exists');
select has_type('public', 'ai_job_status', 'AI job status enum exists');
select has_type('public', 'ai_quota_reservation_status', 'quota reservation status enum exists');

select has_column('public', 'ai_jobs', 'feature', 'jobs store the feature name');
select has_column('public', 'ai_jobs', 'idempotency_key', 'jobs store the idempotency key');
select has_column('public', 'ai_jobs', 'request_fingerprint', 'jobs store the request fingerprint');
select has_column('public', 'ai_jobs', 'source_object_path', 'jobs store the exact source object path');
select has_column('public', 'ai_jobs', 'source_hash', 'jobs store the source hash');
select has_column('public', 'ai_jobs', 'openai_response_id', 'jobs store the provider response ID');
select has_column('public', 'ai_jobs', 'lease_expires_at', 'jobs store the processing lease');
select has_column('public', 'user_syllabus_subjects', 'external_id', 'remote subjects preserve external IDs');
select has_column('public', 'user_syllabus_subjects', 'package_version', 'remote subjects preserve package versions');
select has_column('public', 'user_syllabus_topics', 'external_id', 'remote topics preserve external IDs');
select has_column('public', 'user_syllabus_topics', 'parent_topic_id', 'remote topics preserve parent links');
select has_column('public', 'user_syllabus_topics', 'schema_version', 'remote topics preserve schema versions');

select has_index('public', 'ai_jobs', 'ai_jobs_user_feature_idempotency_key_idx', 'job idempotency index exists');
select has_index('public', 'user_syllabus_subjects', 'user_syllabus_subjects_syllabus_external_id_idx', 'subject external ID index exists');
select has_index('public', 'user_syllabus_topics', 'user_syllabus_topics_subject_external_id_idx', 'topic external ID index exists');

select ok(
  exists (
    select 1
    from pg_constraint
    where conrelid = 'public.ai_jobs'::regclass
      and contype = 'c'
      and pg_get_constraintdef(oid) like '%idempotency_key%'
  ),
  'job idempotency key has a non-empty check constraint'
);
select ok(
  exists (
    select 1
    from pg_constraint
    where conrelid = 'public.ai_jobs'::regclass
      and contype = 'c'
      and pg_get_constraintdef(oid) like '%source_bytes%'
  ),
  'source byte count has a non-negative check constraint'
);
select ok(
  exists (
    select 1
    from pg_constraint
    where conrelid = 'public.user_syllabus_topics'::regclass
      and contype = 'f'
      and pg_get_constraintdef(oid) like '%parent_topic_id%'
  ),
  'topic parents are constrained to the same subject tree'
);

select ok(
  exists (
    select 1
    from pg_policies
    where schemaname = 'public'
      and tablename = 'profiles'
      and policyname = 'profiles_select_own'
      and cmd = 'SELECT'
      and coalesce(qual, '') like '%auth.uid()%'
  ),
  'profiles owner policy exists with an auth.uid predicate'
);
select ok(
  exists (
    select 1
    from pg_policies
    where schemaname = 'public'
      and tablename = 'ai_jobs'
      and policyname = 'ai_jobs_select_own'
      and cmd = 'SELECT'
      and coalesce(qual, '') like '%auth.uid()%'
  ),
  'jobs owner policy exists with an auth.uid predicate'
);
select ok(
  exists (
    select 1
    from pg_policies
    where schemaname = 'public'
      and tablename = 'ai_quota_reservations'
      and policyname = 'ai_quota_reservations_select_own'
      and cmd = 'SELECT'
      and coalesce(qual, '') like '%auth.uid()%'
  ),
  'reservation owner policy exists with an auth.uid predicate'
);
select ok(
  exists (
    select 1
    from pg_policies
    where schemaname = 'public'
      and tablename = 'user_syllabi'
      and policyname = 'user_syllabi_select_own'
      and cmd = 'SELECT'
      and coalesce(qual, '') like '%auth.uid()%'
  ),
  'syllabus owner policy exists with an auth.uid predicate'
);
select ok(
  exists (
    select 1
    from pg_policies
    where schemaname = 'public'
      and tablename = 'user_syllabus_subjects'
      and policyname = 'user_syllabus_subjects_select_own'
      and cmd = 'SELECT'
      and coalesce(qual, '') like '%auth.uid()%'
  ),
  'subject owner policy exists with an auth.uid predicate'
);
select ok(
  exists (
    select 1
    from pg_policies
    where schemaname = 'public'
      and tablename = 'user_syllabus_topics'
      and policyname = 'user_syllabus_topics_select_own'
      and cmd = 'SELECT'
      and coalesce(qual, '') like '%auth.uid()%'
  ),
  'topic owner policy exists with an auth.uid predicate'
);
select ok(
  exists (
    select 1
    from pg_policies
    where schemaname = 'storage'
      and tablename = 'objects'
      and policyname = 'ai_syllabus_sources_select_own'
      and cmd = 'SELECT'
      and coalesce(qual, '') like '%auth.uid()%'
  ),
  'source Storage select policy exists with an auth.uid predicate'
);
select ok(
  exists (
    select 1
    from pg_policies
    where schemaname = 'storage'
      and tablename = 'objects'
      and policyname = 'ai_syllabus_sources_insert_own'
      and cmd = 'INSERT'
      and coalesce(with_check, '') like '%auth.uid()%'
  ),
  'source Storage insert policy exists with an auth.uid predicate'
);
select ok(
  exists (
    select 1
    from pg_policies
    where schemaname = 'storage'
      and tablename = 'objects'
      and policyname = 'ai_syllabus_snapshots_select_own'
      and cmd = 'SELECT'
      and coalesce(qual, '') like '%auth.uid()%'
  ),
  'snapshot Storage select policy exists with an auth.uid predicate'
);

select ok(
  (select not public from storage.buckets where id = 'ai-syllabus-sources'),
  'source Storage bucket is private'
);
select ok(
  (select not public from storage.buckets where id = 'ai-syllabus-snapshots'),
  'snapshot Storage bucket is private'
);

select has_extension('dblink', 'dblink is available for the concurrent quota test');

select ok(
  dblink_connect(
    'race_setup',
    format(
      'host=supabase_db_estudario-local port=5432 dbname=%s user=postgres password=postgres options=-csearch_path=',
      current_database()
    )
  ) = 'OK',
  'concurrency fixture connection opens'
);
select ok(
  dblink_exec(
    'race_setup',
    $setup$
      insert into auth.users (id, aud, role, email, created_at, updated_at)
      values ('00000000-0000-0000-0000-0000000000c1', 'authenticated', 'authenticated', 'ai-foundation-race@example.test', now(), now())
      on conflict (id) do nothing;
      insert into public.profiles (user_id, beta_access)
      values ('00000000-0000-0000-0000-0000000000c1', true)
      on conflict (user_id) do update set beta_access = excluded.beta_access;
      update public.ai_feature_flags
      set enabled = true
      where flag_key in ('AI_BETA_ENABLED', 'SYLLABUS_AI_ENABLED', 'PLAN_AI_ENABLED', 'CONTENT_AI_ENABLED');
      insert into public.ai_quota_usage (user_id, feature, period_start)
      values ('00000000-0000-0000-0000-0000000000c1', 'CONTENT_GENERATION', (now() at time zone 'America/Sao_Paulo')::date)
      on conflict (user_id, feature, period_start) do nothing;
    $setup$
  ) is not null,
  'concurrency fixture is committed before the race'
);
select ok(dblink_disconnect('race_setup') = 'OK', 'concurrency fixture connection closes');

select set_config('request.jwt.claim.role', 'authenticated', false);
select set_config('request.jwt.claim.sub', '00000000-0000-0000-0000-0000000000c1', false);
select ok(
  dblink_connect(
    'race_hold',
    format(
      'host=supabase_db_estudario-local port=5432 dbname=%s user=postgres password=postgres options=-csearch_path=',
      current_database()
    )
  ) = 'OK',
  'concurrent quota connection opens'
);
select ok(
  dblink_send_query(
    'race_hold',
    $race$
      do $remote$
      begin
        perform set_config('request.jwt.claim.role', 'authenticated', false);
        perform set_config('request.jwt.claim.sub', '00000000-0000-0000-0000-0000000000c1', false);
        perform 1
        from public.ai_quota_usage
        where user_id = '00000000-0000-0000-0000-0000000000c1'
          and feature = 'CONTENT_GENERATION'
          and period_start = (now() at time zone 'America/Sao_Paulo')::date
        for update;
        perform pg_sleep(1);
        perform public.create_or_get_ai_job_and_reserve_quota(
          'CONTENT_GENERATION',
          'foundation-concurrent-key',
          'foundation-concurrent-fingerprint',
          '{}'::jsonb
        );
      end;
      $remote$;
    $race$
  ) = 1,
  'first concurrent call locks the quota row before sleeping'
);
select is(
  (select count(*) from public.create_or_get_ai_job_and_reserve_quota(
    'CONTENT_GENERATION',
    'foundation-concurrent-key',
    'foundation-concurrent-fingerprint',
    '{}'::jsonb
  )),
  1::bigint,
  'second concurrent call returns one idempotent result after the row lock'
);
select ok(
  (select count(*) from dblink_get_result('race_hold') as result(value text)) >= 0,
  'first concurrent call completes'
);
select is(
  (select count(*) from public.ai_jobs
   where user_id = '00000000-0000-0000-0000-0000000000c1'
     and feature = 'CONTENT_GENERATION'
     and idempotency_key = 'foundation-concurrent-key'),
  1::bigint,
  'concurrent calls create exactly one job'
);
select is(
  (select count(*)
   from public.ai_quota_reservations r
   join public.ai_jobs j on j.id = r.job_id
   where j.user_id = '00000000-0000-0000-0000-0000000000c1'
     and j.feature = 'CONTENT_GENERATION'
     and j.idempotency_key = 'foundation-concurrent-key'),
  1::bigint,
  'concurrent calls create exactly one quota reservation'
);
select is(
  (select reserved_count from public.ai_quota_usage
   where user_id = '00000000-0000-0000-0000-0000000000c1'
     and feature = 'CONTENT_GENERATION'
     and period_start = (now() at time zone 'America/Sao_Paulo')::date),
  1,
  'concurrent calls reserve exactly one quota unit'
);
select ok(dblink_disconnect('race_hold') = 'OK', 'concurrent quota connection closes');

begin;

do $$
declare
  user_a uuid := '00000000-0000-0000-0000-0000000000a1';
  user_b uuid := '00000000-0000-0000-0000-0000000000b1';
begin
  insert into auth.users (id, aud, role, email, created_at, updated_at)
  values
    (user_a, 'authenticated', 'authenticated', 'ai-foundation-a@example.test', now(), now()),
    (user_b, 'authenticated', 'authenticated', 'ai-foundation-b@example.test', now(), now())
  on conflict (id) do nothing;

  insert into public.profiles (user_id, beta_access)
  values (user_a, true), (user_b, true)
  on conflict (user_id) do update set beta_access = excluded.beta_access;

end;
$$;

set local role postgres;
insert into public.user_syllabi (
  id,
  owner_user_id,
  title,
  position,
  schema_version,
  visibility
)
values
  ('00000000-0000-0000-0000-0000000000d1', '00000000-0000-0000-0000-0000000000a1', 'Account A syllabus', 0, 1, 'PRIVATE'),
  ('00000000-0000-0000-0000-0000000000d2', '00000000-0000-0000-0000-0000000000b1', 'Account B syllabus', 0, 1, 'PRIVATE');

insert into public.user_syllabus_subjects (
  id,
  syllabus_id,
  external_id,
  name,
  position,
  package_version,
  schema_version
)
values
  ('00000000-0000-0000-0000-0000000000e1', '00000000-0000-0000-0000-0000000000d1', 'subject-a-1', 'Account A subject', 0, 'estudo-v1', 1),
  ('00000000-0000-0000-0000-0000000000e2', '00000000-0000-0000-0000-0000000000d2', 'subject-b-1', 'Account B subject', 0, 'estudo-v1', 1);

insert into public.user_syllabus_topics (
  id,
  subject_id,
  external_id,
  parent_topic_id,
  name,
  position,
  package_version,
  schema_version
)
values
  ('00000000-0000-0000-0000-0000000000f1', '00000000-0000-0000-0000-0000000000e1', 'topic-a-root', null, 'Account A root', 0, 'estudo-v1', 1),
  ('00000000-0000-0000-0000-0000000000f2', '00000000-0000-0000-0000-0000000000e1', 'topic-a-child', '00000000-0000-0000-0000-0000000000f1', 'Account A child', 0, 'estudo-v1', 1),
  ('00000000-0000-0000-0000-0000000000f3', '00000000-0000-0000-0000-0000000000e2', 'topic-b-root', null, 'Account B root', 0, 'estudo-v1', 1);

insert into storage.objects (bucket_id, name)
values
  ('ai-syllabus-sources', '00000000-0000-0000-0000-0000000000a1/account-a.pdf'),
  ('ai-syllabus-sources', '00000000-0000-0000-0000-0000000000b1/account-b.pdf');

update storage.objects
set owner = case
  when name like '00000000-0000-0000-0000-0000000000a1/%' then '00000000-0000-0000-0000-0000000000a1'::uuid
  else '00000000-0000-0000-0000-0000000000b1'::uuid
end,
metadata = jsonb_build_object('mimetype', 'application/pdf', 'size', 1024)
where bucket_id = 'ai-syllabus-sources';

select set_config('request.jwt.claim.role', 'authenticated', true);
select set_config('request.jwt.claim.sub', '00000000-0000-0000-0000-0000000000a1', true);
set local role authenticated;

select is(
  (select count(*) from public.user_syllabi),
  1::bigint,
  'account A sees only its own private syllabus'
);
select is(
  (select count(*) from public.user_syllabus_subjects),
  1::bigint,
  'account A sees only its own syllabus subject'
);
select is(
  (select count(*) from public.user_syllabus_topics),
  2::bigint,
  'account A sees its complete topic tree'
);
select is(
  (select count(*) from storage.objects where bucket_id = 'ai-syllabus-sources'),
  1::bigint,
  'account A sees only its own Storage source object'
);
select throws_ok(
  $$insert into public.user_syllabi (owner_user_id, title, position, schema_version, visibility)
    values ('00000000-0000-0000-0000-0000000000a1', 'Non-private syllabus', 99, 1, 'COMMUNITY_REVIEWED')$$,
  '42501',
  null,
  'authenticated owners cannot insert a non-private syllabus'
);
select throws_ok(
  $$update public.user_syllabi
    set visibility = 'VERIFIED'
    where id = '00000000-0000-0000-0000-0000000000d1'$$,
  '42501',
  null,
  'authenticated owners cannot update a syllabus away from PRIVATE'
);

select set_config('request.jwt.claim.sub', '00000000-0000-0000-0000-0000000000b1', true);
select is(
  (select count(*) from public.user_syllabi where id = '00000000-0000-0000-0000-0000000000d1'),
  0::bigint,
  'account B cannot read account A syllabus rows'
);
select is(
  (select count(*) from public.user_syllabus_subjects where id = '00000000-0000-0000-0000-0000000000e1'),
  0::bigint,
  'account B cannot read account A subject rows'
);
select is(
  (select count(*) from public.user_syllabus_topics where id in ('00000000-0000-0000-0000-0000000000f1', '00000000-0000-0000-0000-0000000000f2')),
  0::bigint,
  'account B cannot read account A topic rows'
);
select is(
  (select count(*) from storage.objects where bucket_id = 'ai-syllabus-sources'),
  1::bigint,
  'account B sees only its own Storage source object'
);
select set_config('request.jwt.claim.sub', '00000000-0000-0000-0000-0000000000a1', true);

create temp table ai_test_jobs (
  label text primary key,
  job_id uuid not null
);
grant all on ai_test_jobs to authenticated, service_role;

insert into ai_test_jobs (label, job_id)
select 'idempotent', job_id
from public.create_or_get_ai_job_and_reserve_quota(
  'SYLLABUS_GENERATION',
  'foundation-idempotency-key',
  'foundation-fingerprint-v1',
  '{}'::jsonb
);

select is(
  (select status::text from public.ai_jobs where id = (select job_id from ai_test_jobs where label = 'idempotent')),
  'RESERVED',
  'new job starts in RESERVED'
);
select is(
  (select count(*) from public.ai_jobs where user_id = '00000000-0000-0000-0000-0000000000a1' and idempotency_key = 'foundation-idempotency-key'),
  1::bigint,
  'first reservation creates exactly one job'
);
select is(
  (select count(*) from public.ai_quota_reservations where job_id = (select job_id from ai_test_jobs where label = 'idempotent')),
  1::bigint,
  'first reservation creates exactly one linked quota reservation'
);
select is(
  (select reserved_count from public.ai_quota_usage where user_id = '00000000-0000-0000-0000-0000000000a1' and feature = 'SYLLABUS_GENERATION'),
  1,
  'quota usage records the reservation atomically'
);

select is(
  (select job_id from public.create_or_get_ai_job_and_reserve_quota(
    'SYLLABUS_GENERATION',
    'foundation-idempotency-key',
    'foundation-fingerprint-v1',
    '{}'::jsonb
  )),
  (select job_id from ai_test_jobs where label = 'idempotent'),
  'same idempotency key and fingerprint return the original job'
);
select throws_ok(
  $$select * from public.create_or_get_ai_job_and_reserve_quota(
    'SYLLABUS_GENERATION',
    'foundation-idempotency-key',
    'different-fingerprint',
    '{}'::jsonb
  )$$,
  'P0001',
  'IDEMPOTENCY_KEY_CONFLICT',
  'same idempotency key with a different fingerprint is rejected'
);

select set_config('request.jwt.claim.sub', '00000000-0000-0000-0000-0000000000b1', true);
select is(
  (select count(*) from public.ai_jobs where id = (select job_id from ai_test_jobs where label = 'idempotent')),
  0::bigint,
  'another account cannot read the first account job through RLS'
);
select set_config('request.jwt.claim.sub', '00000000-0000-0000-0000-0000000000a1', true);

select ok(
  not has_function_privilege(
    'authenticated',
    'public.bind_ai_job_source(uuid, uuid, text, text, text, bigint, integer, integer, jsonb)',
    'EXECUTE'
  ),
  'authenticated cannot execute the backend-only source binding RPC'
);
select ok(
  has_function_privilege(
    'service_role',
    'public.bind_ai_job_source(uuid, uuid, text, text, text, bigint, integer, integer, jsonb)',
    'EXECUTE'
  ),
  'service_role can execute the source binding RPC'
);
select throws_ok(
  $$select * from public.bind_ai_job_source(
    '00000000-0000-0000-0000-0000000000a1'::uuid,
    (select job_id from ai_test_jobs where label = 'idempotent'),
    '00000000-0000-0000-0000-0000000000a1/account-a.pdf',
    'application/pdf', repeat('a', 64), 1024, 1, 1, '{}'::jsonb
  )$$,
  '42501',
  null,
  'authenticated cannot invoke source binding even with user input'
);

select throws_ok(
  $$select * from public.create_or_get_ai_job_and_reserve_quota(
    'SYLLABUS_GENERATION',
    'foundation-second-job',
    'foundation-second-fingerprint',
    '{}'::jsonb
  )$$,
  'P0001',
  'AI_QUOTA_EXHAUSTED',
  'a second job cannot reserve exhausted quota'
);
select is(
  (select job_id from public.create_or_get_ai_job_and_reserve_quota(
    'SYLLABUS_GENERATION',
    'foundation-idempotency-key',
    'foundation-fingerprint-v1',
    '{}'::jsonb
  )),
  (select job_id from ai_test_jobs where label = 'idempotent'),
  'idempotent retry returns the existing job after quota exhaustion'
);
select is(
  (select reserved_count from public.ai_quota_usage where user_id = '00000000-0000-0000-0000-0000000000a1' and feature = 'SYLLABUS_GENERATION'),
  1,
  'idempotent retry does not consume another quota reservation'
);

set local role service_role;
select set_config('request.jwt.claim.role', 'service_role', true);
select is(
  (select status::text from public.bind_ai_job_source(
  '00000000-0000-0000-0000-0000000000a1'::uuid,
  (select job_id from ai_test_jobs where label = 'idempotent'),
    '00000000-0000-0000-0000-0000000000a1/account-a.pdf',
    'application/pdf',
    repeat('a', 64),
    1024,
    1,
    1,
    '{"bucket":"ai-syllabus-sources"}'::jsonb
  )),
  'RESERVED',
  'source binding RPC preserves RESERVED state'
);
select is(
  (select source_object_path from public.ai_jobs where id = (select job_id from ai_test_jobs where label = 'idempotent')),
  '00000000-0000-0000-0000-0000000000a1/account-a.pdf',
  'source binding RPC stores the exact path'
);
select is(
  (select source_hash from public.ai_jobs where id = (select job_id from ai_test_jobs where label = 'idempotent')),
  repeat('a', 64),
  'source binding RPC stores the source hash'
);
select throws_ok(
  $$select * from public.bind_ai_job_source(
    '00000000-0000-0000-0000-0000000000a1'::uuid,
    (select job_id from ai_test_jobs where label = 'idempotent'),
    '00000000-0000-0000-0000-0000000000a1/other.pdf',
    'application/pdf', repeat('b', 64), 1024, 1, 1, '{}'::jsonb
  )$$,
  'P0001',
  'SOURCE_ALREADY_BOUND',
  'source binding cannot replace an existing binding'
);

select throws_ok(
  $$select * from public.bind_ai_job_source(
    '00000000-0000-0000-0000-0000000000b1'::uuid,
    (select job_id from ai_test_jobs where label = 'idempotent'),
    '00000000-0000-0000-0000-0000000000a1/foreign.pdf',
    'application/pdf', repeat('b', 64), 1024, 1, 1, '{}'::jsonb
  )$$,
  '42501',
  'AI_JOB_FORBIDDEN',
  'the backend binding RPC still validates the supplied owner'
);

set local role authenticated;
select set_config('request.jwt.claim.role', 'authenticated', true);
select set_config('request.jwt.claim.sub', '00000000-0000-0000-0000-0000000000b1', true);
select set_config('request.jwt.claim.sub', '00000000-0000-0000-0000-0000000000a1', true);

select is(
  (select status::text from public.claim_ai_job((select job_id from ai_test_jobs where label = 'idempotent'), 'foundation-lease', 60)),
  'PROCESSING',
  'RESERVED can transition to PROCESSING through the claim RPC'
);
select is(
  (select status::text from public.finalize_ai_job_success(
    (select job_id from ai_test_jobs where label = 'idempotent'),
    '{"schemaVersion":1,"subjects":[]}'::jsonb,
    '[]'::jsonb,
    'response-foundation-1',
    'syllabus-v1',
    1,
    'gpt-6-luna'
  )),
  'SUCCEEDED',
  'PROCESSING can transition to SUCCEEDED through the success RPC'
);
select is(
  (select status::text from public.ai_quota_reservations where job_id = (select job_id from ai_test_jobs where label = 'idempotent')),
  'CONSUMED',
  'successful finalization consumes the reservation'
);
select is(
  (select successful_count from public.ai_quota_usage where user_id = '00000000-0000-0000-0000-0000000000a1' and feature = 'SYLLABUS_GENERATION'),
  1,
  'successful finalization increments usage exactly once'
);

select set_config('request.jwt.claim.sub', '00000000-0000-0000-0000-0000000000b1', true);
insert into ai_test_jobs (label, job_id)
select 'account-b-quota', job_id
from public.create_or_get_ai_job_and_reserve_quota(
  'SYLLABUS_GENERATION',
  'foundation-account-b-key',
  'foundation-account-b-fingerprint',
  '{}'::jsonb
);
select is(
  (select reserved_count from public.ai_quota_usage where user_id = '00000000-0000-0000-0000-0000000000b1' and feature = 'SYLLABUS_GENERATION'),
  1,
  'account B retains its independent quota while account A is consumed'
);
select set_config('request.jwt.claim.sub', '00000000-0000-0000-0000-0000000000a1', true);

set local role postgres;
select throws_ok(
  format(
    $$update public.ai_jobs set status = 'PROCESSING' where id = %L$$,
    (select job_id from ai_test_jobs where label = 'idempotent')
  ),
  'P0001',
  'TERMINAL_JOB_IMMUTABLE',
  'terminal jobs cannot leave SUCCEEDED'
);
set local role authenticated;

insert into ai_test_jobs (label, job_id)
select 'pre-provider-cancel', job_id
from public.create_or_get_ai_job_and_reserve_quota(
  'PLAN_GENERATION',
  'foundation-pre-provider-cancel-key',
  'foundation-pre-provider-cancel-fingerprint',
  '{}'::jsonb
);
select is(
  (select status::text from public.release_ai_job_reservation(
    (select job_id from ai_test_jobs where label = 'pre-provider-cancel'),
    'CANCELLED',
    'USER_CANCELLED',
    'cancelled before provider execution'
  )),
  'CANCELLED',
  'RESERVED can transition to CANCELLED before provider execution'
);
select is(
  (select status::text from public.ai_quota_reservations where job_id = (select job_id from ai_test_jobs where label = 'pre-provider-cancel')),
  'RELEASED',
  'pre-provider cancellation releases the reservation atomically'
);
select is(
  (select reserved_count from public.ai_quota_usage where user_id = '00000000-0000-0000-0000-0000000000a1' and feature = 'PLAN_GENERATION'),
  0,
  'pre-provider cancellation restores the reserved quota unit'
);

insert into ai_test_jobs (label, job_id)
select 'processing-failure', job_id
from public.create_or_get_ai_job_and_reserve_quota(
  'PLAN_GENERATION',
  'foundation-processing-failure-key',
  'foundation-processing-failure-fingerprint',
  '{}'::jsonb
);
set local role postgres;
update public.ai_jobs
set source_object_path = '00000000-0000-0000-0000-0000000000a1/failure.pdf',
    source_hash = repeat('c', 64),
    source_bytes = 3072,
    source_pages = 3
where id = (select job_id from ai_test_jobs where label = 'processing-failure');
set local role authenticated;
select is(
  (select status::text from public.claim_ai_job((select job_id from ai_test_jobs where label = 'processing-failure'), 'failure-lease', 60)),
  'PROCESSING',
  'RESERVED can enter PROCESSING before a failed finalization'
);
set local role postgres;
update public.ai_jobs
set provider_reconciled_at = now(),
    provider_result_recoverable = false
where id = (select job_id from ai_test_jobs where label = 'processing-failure');
set local role authenticated;
select is(
  (select status::text from public.finalize_ai_job_failure(
    (select job_id from ai_test_jobs where label = 'processing-failure'),
    'FAILED',
    'PROVIDER_ERROR',
    'provider failed without a recoverable result'
  )),
  'FAILED',
  'PROCESSING can transition to FAILED through the failure RPC'
);
select is(
  (select status::text from public.ai_quota_reservations where job_id = (select job_id from ai_test_jobs where label = 'processing-failure')),
  'RELEASED',
  'failed finalization releases its quota reservation'
);
select is(
  (select reserved_count from public.ai_quota_usage where user_id = '00000000-0000-0000-0000-0000000000a1' and feature = 'PLAN_GENERATION'),
  0,
  'failed finalization decrements reserved quota'
);

insert into ai_test_jobs (label, job_id)
select 'processing-expired', job_id
from public.create_or_get_ai_job_and_reserve_quota(
  'PLAN_GENERATION',
  'foundation-processing-expired-key',
  'foundation-processing-expired-fingerprint',
  '{}'::jsonb
);
set local role postgres;
update public.ai_jobs
set source_object_path = '00000000-0000-0000-0000-0000000000a1/expired.pdf',
    source_hash = repeat('d', 64),
    source_bytes = 4096,
    source_pages = 4
where id = (select job_id from ai_test_jobs where label = 'processing-expired');
set local role authenticated;
select is(
  (select status::text from public.claim_ai_job((select job_id from ai_test_jobs where label = 'processing-expired'), 'expired-lease', 60)),
  'PROCESSING',
  'RESERVED can enter PROCESSING before an expired finalization'
);
set local role postgres;
update public.ai_jobs
set provider_reconciled_at = now(),
    provider_result_recoverable = false
where id = (select job_id from ai_test_jobs where label = 'processing-expired');
set local role authenticated;
select is(
  (select status::text from public.finalize_ai_job_failure(
    (select job_id from ai_test_jobs where label = 'processing-expired'),
    'EXPIRED',
    'LEASE_EXPIRED',
    'processing lease expired'
  )),
  'EXPIRED',
  'PROCESSING can transition to EXPIRED through the failure RPC'
);
select is(
  (select status::text from public.ai_quota_reservations where job_id = (select job_id from ai_test_jobs where label = 'processing-expired')),
  'RELEASED',
  'expired finalization releases its quota reservation'
);
select is(
  (select reserved_count from public.ai_quota_usage where user_id = '00000000-0000-0000-0000-0000000000a1' and feature = 'PLAN_GENERATION'),
  0,
  'expired finalization decrements reserved quota'
);

insert into ai_test_jobs (label, job_id)
select 'provider-started', job_id
from public.create_or_get_ai_job_and_reserve_quota(
  'CONTENT_GENERATION',
  'foundation-provider-started-key',
  'foundation-provider-started-fingerprint',
  '{}'::jsonb
);
set local role postgres;
update public.ai_jobs
set provider_execution_started_at = now(),
    openai_response_id = 'response-provider-started'
where id = (select job_id from ai_test_jobs where label = 'provider-started');
select throws_ok(
  format(
    $$select * from public.release_ai_job_reservation(%L, 'CANCELLED', 'USER_CANCELLED', 'must reconcile provider')$$,
    (select job_id from ai_test_jobs where label = 'provider-started')
  ),
  'P0001',
  'CANCELLATION_PROVIDER_STARTED',
  'RESERVED cancellation refuses to release quota after provider execution started'
);
select is(
  (select status::text from public.ai_jobs where id = (select job_id from ai_test_jobs where label = 'provider-started')),
  'RESERVED',
  'provider-started RESERVED jobs remain recoverable after rejected cancellation'
);
select is(
  (select reserved_count from public.ai_quota_usage where user_id = '00000000-0000-0000-0000-0000000000a1' and feature = 'CONTENT_GENERATION'),
  1,
  'provider-started cancellation leaves quota reserved'
);
set local role authenticated;

insert into ai_test_jobs (label, job_id)
select 'reconciliation-required', job_id
from public.create_or_get_ai_job_and_reserve_quota(
  'PLAN_GENERATION',
  'foundation-reconciliation-key',
  'foundation-reconciliation-fingerprint',
  '{}'::jsonb
);
set local role postgres;
update public.ai_jobs
set source_object_path = '00000000-0000-0000-0000-0000000000a1/reconciliation.pdf',
    source_hash = repeat('b', 64),
    source_bytes = 2048,
    source_pages = 2
where id = (select job_id from ai_test_jobs where label = 'reconciliation-required');
set local role authenticated;
select is(
  (select status::text from public.claim_ai_job((select job_id from ai_test_jobs where label = 'reconciliation-required'), 'reconciliation-lease', 60)),
  'PROCESSING',
  'RESERVED can be claimed before provider reconciliation'
);
set local role postgres;
select set_config('ai.internal_job_transition', '1', false);
select throws_ok(
  format(
    $$update public.ai_jobs set status = 'RESERVED' where id = %L$$,
    (select job_id from ai_test_jobs where label = 'reconciliation-required')
  ),
  'P0001',
  'INVALID_JOB_STATE_TRANSITION',
  'PROCESSING cannot transition back to RESERVED'
);
select set_config('ai.internal_job_transition', '0', false);
set local role authenticated;
select throws_ok(
  $$select * from public.finalize_ai_job_failure(
    (select job_id from ai_test_jobs where label = 'reconciliation-required'),
    'FAILED',
    'PROVIDER_ERROR',
    'failure requires provider reconciliation'
  )$$,
  'P0001',
  'PROVIDER_RECONCILIATION_REQUIRED',
  'PROCESSING failure cannot release quota before provider reconciliation'
);
select throws_ok(
  $$select * from public.release_ai_job_reservation(
    (select job_id from ai_test_jobs where label = 'reconciliation-required'),
    'CANCELLED',
    'USER_CANCELLED',
    'reconciliation required'
  )$$,
  'P0001',
  'CANCELLATION_RECONCILIATION_REQUIRED',
  'PROCESSING cancellation requires provider reconciliation'
);
set local role postgres;
update public.ai_jobs
set provider_reconciled_at = now(),
    provider_result_recoverable = false
where id = (select job_id from ai_test_jobs where label = 'reconciliation-required');
set local role authenticated;
select is(
  (select status::text from public.release_ai_job_reservation(
    (select job_id from ai_test_jobs where label = 'reconciliation-required'),
    'CANCELLED',
    'PROVIDER_CANCELLED',
    'provider confirmed no result exists'
  )),
  'CANCELLED',
  'reconciled PROCESSING can transition to CANCELLED'
);
select is(
  (select status::text from public.ai_quota_reservations where job_id = (select job_id from ai_test_jobs where label = 'reconciliation-required')),
  'RELEASED',
  'reconciled cancellation releases quota'
);

set local role postgres;
select set_config('ai.internal_job_transition', '1', false);
select throws_ok(
  format(
    $$update public.ai_jobs set status = 'SUCCEEDED' where id = %L$$,
    (select job_id from ai_test_jobs where label = 'provider-started')
  ),
  'P0001',
  'INVALID_JOB_STATE_TRANSITION',
  'RESERVED cannot transition directly to SUCCEEDED'
);
select throws_ok(
  format(
    $$update public.ai_jobs set status = 'FAILED' where id = %L$$,
    (select job_id from ai_test_jobs where label = 'provider-started')
  ),
  'P0001',
  'INVALID_JOB_STATE_TRANSITION',
  'RESERVED cannot transition directly to FAILED'
);
select throws_ok(
  format(
    $$update public.ai_jobs set status = 'EXPIRED' where id = %L$$,
    (select job_id from ai_test_jobs where label = 'provider-started')
  ),
  'P0001',
  'INVALID_JOB_STATE_TRANSITION',
  'RESERVED cannot transition directly to EXPIRED'
);
select set_config('ai.internal_job_transition', '0', false);

select throws_ok(
  format(
    $$update public.ai_jobs set status = 'RESERVED' where id = %L$$,
    (select job_id from ai_test_jobs where label = 'idempotent')
  ),
  'P0001',
  'TERMINAL_JOB_IMMUTABLE',
  'SUCCEEDED jobs are immutable'
);
select throws_ok(
  format(
    $$update public.ai_jobs set status = 'RESERVED' where id = %L$$,
    (select job_id from ai_test_jobs where label = 'processing-failure')
  ),
  'P0001',
  'TERMINAL_JOB_IMMUTABLE',
  'FAILED jobs are immutable'
);
select throws_ok(
  format(
    $$update public.ai_jobs set status = 'RESERVED' where id = %L$$,
    (select job_id from ai_test_jobs where label = 'processing-expired')
  ),
  'P0001',
  'TERMINAL_JOB_IMMUTABLE',
  'EXPIRED jobs are immutable'
);
select throws_ok(
  format(
    $$update public.ai_jobs set status = 'RESERVED' where id = %L$$,
    (select job_id from ai_test_jobs where label = 'reconciliation-required')
  ),
  'P0001',
  'TERMINAL_JOB_IMMUTABLE',
  'CANCELLED jobs are immutable'
);
set local role authenticated;

set local role postgres;
select ok(
  dblink_connect(
    'race_cleanup',
    format(
      'host=supabase_db_estudario-local port=5432 dbname=%s user=postgres password=postgres options=-csearch_path=',
      current_database()
    )
  ) = 'OK',
  'concurrency fixture cleanup connection opens'
);
select ok(
  dblink_exec(
    'race_cleanup',
    $cleanup$
      delete from auth.users where id = '00000000-0000-0000-0000-0000000000c1';
      update public.ai_feature_flags set enabled = false;
    $cleanup$
  ) is not null,
  'concurrency fixture cleanup completes'
);
select ok(dblink_disconnect('race_cleanup') = 'OK', 'concurrency fixture cleanup connection closes');

select * from finish();
rollback;
