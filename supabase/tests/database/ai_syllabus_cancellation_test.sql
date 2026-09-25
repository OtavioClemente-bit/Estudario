select no_plan();

begin;

select ok(
  not has_function_privilege('authenticated', 'public.record_ai_job_cancellation_reconciliation(uuid, text, text, boolean, text)', 'EXECUTE'),
  'clients cannot forge provider cancellation reconciliation'
);
select ok(
  not has_function_privilege('authenticated', 'public.finalize_ai_job_success_after_cancellation(uuid, jsonb, jsonb, text, text, integer, text)', 'EXECUTE'),
  'clients cannot forge a completed provider result during cancellation'
);

insert into auth.users (id, aud, role, email, created_at, updated_at)
values
  ('00000000-0000-0000-0000-0000000000c2', 'authenticated', 'authenticated', 'ai-cancel-c2@example.test', now(), now()),
  ('00000000-0000-0000-0000-0000000000c3', 'authenticated', 'authenticated', 'ai-cancel-c3@example.test', now(), now())
on conflict (id) do nothing;
insert into public.profiles (user_id, beta_access)
values
  ('00000000-0000-0000-0000-0000000000c2', true),
  ('00000000-0000-0000-0000-0000000000c3', true)
on conflict (user_id) do update set beta_access = excluded.beta_access;
update public.ai_feature_flags set enabled = true;

create temporary table cancellation_jobs(label text primary key, job_id uuid not null);
grant all on cancellation_jobs to authenticated;
grant all on cancellation_jobs to service_role;

set local role authenticated;
select set_config('request.jwt.claim.role', 'authenticated', false);
select set_config('request.jwt.claim.sub', '00000000-0000-0000-0000-0000000000c2', false);

insert into cancellation_jobs(label, job_id)
select 'reserved', job_id
from public.create_or_get_ai_job_and_reserve_quota(
  'PLAN_GENERATION', 'cancel-reserved-key', 'cancel-reserved-fingerprint', '{}'::jsonb
);
select is(
  (select status::text from public.cancel_ai_job_without_provider((select job_id from cancellation_jobs where label = 'reserved'))),
  'CANCELLED',
  'RESERVED cancellation releases quota only before provider execution'
);
select is(
  (select status::text from public.ai_quota_reservations where job_id = (select job_id from cancellation_jobs where label = 'reserved')),
  'RELEASED',
  'pre-provider cancellation releases the reservation atomically'
);
select is(
  (select status::text from public.cancel_ai_job_without_provider((select job_id from cancellation_jobs where label = 'reserved'))),
  'CANCELLED',
  'repeated cancellation of a terminal job is idempotent'
);

insert into cancellation_jobs(label, job_id)
select 'reserved-started', job_id
from public.create_or_get_ai_job_and_reserve_quota(
  'CONTENT_GENERATION', 'cancel-reserved-started-key', 'cancel-reserved-started-fingerprint', '{}'::jsonb
);
set local role postgres;
update public.ai_jobs
set provider_execution_started_at = now()
where id = (select job_id from cancellation_jobs where label = 'reserved-started');
set local role authenticated;
select throws_ok(
  format('select * from public.cancel_ai_job_without_provider(%L)', (select job_id from cancellation_jobs where label = 'reserved-started')),
  'P0001',
  'CANCELLATION_RECONCILIATION_REQUIRED',
  'provider-start evidence blocks the RESERVED pre-provider cancellation path'
);
select is(
  (select reserved_count from public.ai_quota_usage where user_id = '00000000-0000-0000-0000-0000000000c2' and feature = 'CONTENT_GENERATION'),
  1,
  'provider-start evidence keeps quota reserved'
);

insert into cancellation_jobs(label, job_id)
select 'processing-lease', job_id
from public.create_or_get_ai_job_and_reserve_quota(
  'PLAN_GENERATION', 'cancel-processing-lease-key', 'cancel-processing-lease-fingerprint', '{}'::jsonb
);
set local role postgres;
update public.ai_jobs
set source_object_path = '00000000-0000-0000-0000-0000000000c2/processing-lease.pdf', source_hash = repeat('a', 64), source_bytes = 10, source_pages = 1, source_file_count = 1
where id = (select job_id from cancellation_jobs where label = 'processing-lease');
set local role service_role;
select set_config('request.jwt.claim.role', 'service_role', true);
select is(
  (select status::text from public.claim_ai_job((select job_id from cancellation_jobs where label = 'processing-lease'), 'cancel-worker', 300)),
  'PROCESSING',
  'cancellation race fixture obtains a live processing lease'
);
select throws_ok(
  format('select * from public.cancel_ai_job_without_provider(%L)', (select job_id from cancellation_jobs where label = 'processing-lease')),
  'P0001',
  'CANCELLATION_RECONCILIATION_REQUIRED',
  'live processing lease blocks cancellation before provider evidence'
);

set local role postgres;
update public.ai_jobs
set lease_expires_at = now() - interval '1 second'
where id = (select job_id from cancellation_jobs where label = 'processing-lease');
set local role service_role;
select is(
  (select status::text from public.cancel_ai_job_without_provider((select job_id from cancellation_jobs where label = 'processing-lease'))),
  'CANCELLED',
  'processing without provider evidence can cancel after its lease is no longer active'
);

insert into cancellation_jobs(label, job_id)
select 'processing-response', job_id
from public.create_or_get_ai_job_and_reserve_quota(
  'SYLLABUS_GENERATION', 'cancel-processing-response-key', 'cancel-processing-response-fingerprint', '{}'::jsonb
);
set local role postgres;
update public.ai_jobs
set source_object_path = '00000000-0000-0000-0000-0000000000c2/processing-response.pdf', source_hash = repeat('b', 64), source_bytes = 10, source_pages = 1, source_file_count = 1
where id = (select job_id from cancellation_jobs where label = 'processing-response');
set local role service_role;
select is(
  (select status::text from public.claim_ai_job((select job_id from cancellation_jobs where label = 'processing-response'), 'cancel-response-worker', 300)),
  'PROCESSING',
  'response reconciliation fixture enters PROCESSING'
);
set local role postgres;
update public.ai_jobs
set openai_response_id = 'response-cancel-1', provider_execution_started_at = now()
where id = (select job_id from cancellation_jobs where label = 'processing-response');
set local role service_role;
select is(
  (select status::text from public.record_ai_job_cancellation_reconciliation(
    (select job_id from cancellation_jobs where label = 'processing-response'),
    'response-cancel-1', 'cancelled', false, null
  )),
  'PROCESSING',
  'provider cancellation evidence is recorded before releasing quota'
);
set local role authenticated;
select is(
  (select status::text from public.release_ai_job_reservation(
    (select job_id from cancellation_jobs where label = 'processing-response'), 'CANCELLED', 'PROVIDER_CANCELLED', 'provider confirmed cancellation'
  )),
  'CANCELLED',
  'confirmed provider cancellation releases quota'
);
select is(
  (select openai_response_id from public.ai_jobs where id = (select job_id from cancellation_jobs where label = 'processing-response')),
  'response-cancel-1',
  'cancellation preserves the provider response ID'
);

insert into cancellation_jobs(label, job_id)
select 'processing-unknown', job_id
from public.create_or_get_ai_job_and_reserve_quota(
  'PLAN_GENERATION', 'cancel-processing-unknown-key', 'cancel-processing-unknown-fingerprint', '{}'::jsonb
);
set local role postgres;
update public.ai_jobs
set source_object_path = '00000000-0000-0000-0000-0000000000c2/processing-unknown.pdf', source_hash = repeat('c', 64), source_bytes = 10, source_pages = 1, source_file_count = 1
where id = (select job_id from cancellation_jobs where label = 'processing-unknown');
set local role service_role;
select is(
  (select status::text from public.claim_ai_job((select job_id from cancellation_jobs where label = 'processing-unknown'), 'cancel-unknown-worker', 300)),
  'PROCESSING',
  'unknown reconciliation fixture enters PROCESSING'
);
set local role postgres;
update public.ai_jobs
set openai_response_id = 'response-unknown-1', provider_execution_started_at = now()
where id = (select job_id from cancellation_jobs where label = 'processing-unknown');
set local role service_role;
select is(
  (select status::text from public.record_ai_job_cancellation_reconciliation(
    (select job_id from cancellation_jobs where label = 'processing-unknown'),
    'response-unknown-1', 'unknown', true, 'PROVIDER_UNAVAILABLE'
  )),
  'PROCESSING',
  'unknown provider state keeps the job recoverable'
);
set local role authenticated;
select is(
  (select reserved_count from public.ai_quota_usage where user_id = '00000000-0000-0000-0000-0000000000c2' and feature = 'PLAN_GENERATION'),
  1,
  'unknown provider state never releases quota'
);

select set_config('request.jwt.claim.sub', '00000000-0000-0000-0000-0000000000c3', false);
insert into cancellation_jobs(label, job_id)
select 'late-completion', job_id
from public.create_or_get_ai_job_and_reserve_quota(
  'SYLLABUS_GENERATION', 'cancel-late-completion-key', 'cancel-late-completion-fingerprint', '{}'::jsonb
);
set local role postgres;
update public.ai_jobs
set source_object_path = '00000000-0000-0000-0000-0000000000c3/late-completion.pdf', source_hash = repeat('d', 64), source_bytes = 10, source_pages = 1, source_file_count = 1
where id = (select job_id from cancellation_jobs where label = 'late-completion');
set local role service_role;
select is(
  (select status::text from public.claim_ai_job((select job_id from cancellation_jobs where label = 'late-completion'), 'cancel-late-worker', 300)),
  'PROCESSING',
  'late completion fixture enters PROCESSING'
);
set local role authenticated;
set local role service_role;
select is(
  (select status::text from public.finalize_ai_job_success_after_cancellation(
    (select job_id from cancellation_jobs where label = 'late-completion'),
    '{"schemaVersion":1,"subjects":[]}'::jsonb,
    '[]'::jsonb,
    'response-late-completion', 'syllabus-v1', 1, 'gpt-6-luna'
  )),
  'SUCCEEDED',
  'completed provider result wins and consumes quota'
);
set local role authenticated;
select is(
  (select status::text from public.ai_quota_reservations where job_id = (select job_id from cancellation_jobs where label = 'late-completion')),
  'CONSUMED',
  'completed provider result consumes the reservation'
);
select is(
  (select provider_result_recoverable from public.ai_jobs where id = (select job_id from cancellation_jobs where label = 'late-completion')),
  true,
  'successful late completion preserves reconciliation evidence'
);

select * from finish();
rollback;
