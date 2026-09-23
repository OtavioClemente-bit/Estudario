begin;

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

select has_policy('public', 'profiles', 'profiles_select_own', 'profiles are readable only by their owner');
select has_policy('public', 'ai_jobs', 'ai_jobs_select_own', 'jobs are readable only by their owner');
select has_policy('public', 'ai_quota_reservations', 'ai_quota_reservations_select_own', 'reservations are readable only by their owner');
select has_policy('public', 'user_syllabi', 'user_syllabi_select_own', 'syllabi are readable only by their owner');
select has_policy('public', 'user_syllabus_subjects', 'user_syllabus_subjects_select_own', 'subjects are readable only through the owning syllabus');
select has_policy('public', 'user_syllabus_topics', 'user_syllabus_topics_select_own', 'topics are readable only through the owning syllabus');
select has_policy('storage', 'objects', 'ai_syllabus_sources_select_own', 'source objects are readable only by their owner');
select has_policy('storage', 'objects', 'ai_syllabus_sources_insert_own', 'source objects are insertable only in the owner path');
select has_policy('storage', 'objects', 'ai_syllabus_snapshots_select_own', 'snapshot objects are readable only by their owner');

select ok(
  (select not public from storage.buckets where id = 'ai-syllabus-sources'),
  'source Storage bucket is private'
);
select ok(
  (select not public from storage.buckets where id = 'ai-syllabus-snapshots'),
  'snapshot Storage bucket is private'
);

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

  update public.ai_feature_flags
  set enabled = true
  where flag_key in ('AI_BETA_ENABLED', 'SYLLABUS_AI_ENABLED', 'PLAN_AI_ENABLED', 'CONTENT_AI_ENABLED');
end;
$$;

select set_config('request.jwt.claim.role', 'authenticated', true);
select set_config('request.jwt.claim.sub', '00000000-0000-0000-0000-0000000000a1', true);
set local role authenticated;

create temp table ai_test_jobs (
  label text primary key,
  job_id uuid not null
);
grant all on ai_test_jobs to authenticated;

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

set local role postgres;
update public.ai_jobs
set source_object_path = '00000000-0000-0000-0000-0000000000a1/foundation.pdf',
    source_hash = repeat('a', 64),
    source_bytes = 1024,
    source_pages = 1
where id = (select job_id from ai_test_jobs where label = 'idempotent');
set local role authenticated;

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

select * from finish();
rollback;
