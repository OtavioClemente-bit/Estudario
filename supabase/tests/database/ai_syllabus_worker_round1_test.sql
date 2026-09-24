select no_plan();

select has_table('public', 'ai_job_source_cleanup', 'source cleanup is persisted outside worker memory');
select has_column('public', 'ai_jobs', 'lease_token', 'jobs store the lease token');
select has_column('public', 'ai_jobs', 'lease_generation', 'jobs store the lease generation');
select has_column('public', 'ai_jobs', 'processing_deadline_at', 'jobs store the processing deadline');
select ok(has_function_privilege('service_role', 'public.persist_ai_job_provider_response(uuid, text, text, text, bigint)', 'EXECUTE'), 'service_role can persist a response only through the lease-bound RPC');
select ok(
  not exists (
    select 1 from pg_proc
    where oid = to_regprocedure('public.persist_ai_job_provider_response(uuid, text, text)')
  ),
  'there is no response RPC without the lease token'
);

begin;

do $$
begin
  insert into auth.users (id, aud, role, email, created_at, updated_at)
  values ('00000000-0000-0000-0000-0000000000aa', 'authenticated', 'authenticated', 'ai-worker-round1@example.test', now(), now())
  on conflict (id) do nothing;
  insert into public.profiles (user_id, beta_access)
  values ('00000000-0000-0000-0000-0000000000aa', true)
  on conflict (user_id) do update set beta_access = true;
end;
$$;

set local role postgres;
insert into public.ai_jobs (
  id, user_id, feature, status, idempotency_key, request_fingerprint,
  request_payload, source_object_path, source_hash, source_bytes, source_pages, source_file_count
)
values (
  '00000000-0000-0000-0000-0000000000ab',
  '00000000-0000-0000-0000-0000000000aa',
  'SYLLABUS_GENERATION', 'RESERVED', 'worker-round1-key', 'worker-round1-fingerprint',
  '{}'::jsonb, '00000000-0000-0000-0000-0000000000aa/adversarial.pdf', repeat('a', 64), 1024, 1, 1
);
insert into public.ai_quota_usage (user_id, feature, period_start, reserved_count)
values ('00000000-0000-0000-0000-0000000000aa', 'SYLLABUS_GENERATION', (now() at time zone 'America/Sao_Paulo')::date, 1);
insert into public.ai_quota_reservations (job_id, user_id, feature, period_start)
values ('00000000-0000-0000-0000-0000000000ab', '00000000-0000-0000-0000-0000000000aa', 'SYLLABUS_GENERATION', (now() at time zone 'America/Sao_Paulo')::date);

select set_config('ai.internal_job_transition', '1', true);
update public.ai_jobs
set status = 'PROCESSING', processing_started_at = now()
where id = '00000000-0000-0000-0000-0000000000ab';

set local role service_role;
select set_config('request.jwt.claim.role', 'service_role', true);
create temp table worker_round1_first_lease as
select * from public.claim_ai_syllabus_worker_job('worker-a', 'token-a', 60, 900);

select is((select lease_owner from worker_round1_first_lease), 'worker-a', 'first worker owns the claimed lease');
select is((select lease_token from worker_round1_first_lease), 'token-a', 'first worker receives an opaque lease token');
select is((select lease_generation from worker_round1_first_lease), 1::bigint, 'first worker receives generation one');
select isnt((select processing_deadline_at from worker_round1_first_lease), null::timestamptz, 'claim persists a processing deadline');

set local role postgres;
update public.ai_jobs set lease_expires_at = now() - interval '1 second' where id = '00000000-0000-0000-0000-0000000000ab';
set local role service_role;
create temp table worker_round1_recovered_lease as
select * from public.claim_ai_syllabus_worker_job('worker-b', 'token-b', 60, 900);

select is((select lease_owner from worker_round1_recovered_lease), 'worker-b', 'recovery transfers ownership');
select is((select lease_generation from worker_round1_recovered_lease), 2::bigint, 'recovery increments the lease generation');
select throws_ok(
  $$select * from public.persist_ai_job_provider_response(
    '00000000-0000-0000-0000-0000000000ab'::uuid, 'response-old', 'worker-a', 'token-a', 1
  )$$,
  'P0001', 'AI_JOB_LEASE_LOST', 'the old worker cannot persist after recovery'
);
select is(
  (select openai_response_id from public.persist_ai_job_provider_response(
    '00000000-0000-0000-0000-0000000000ab'::uuid, 'response-new', 'worker-b', 'token-b', 2
  )),
  'response-new', 'the current lease can persist the provider response'
);
select is(
  (select provider_total_tokens from public.record_ai_job_usage(
    '00000000-0000-0000-0000-0000000000ab'::uuid, 'worker-b', 'token-b', 2, 10, 20, 30
  )),
  30::bigint, 'usage capture is lease-bound'
);
select is(
  (select status::text from public.finalize_ai_job_failure_with_lease(
    '00000000-0000-0000-0000-0000000000ab'::uuid, 'FAILED', 'OPENAI_API_KEY_MISSING', 'secret is not configured', false,
    'worker-b', 'token-b', 2
  )),
  'FAILED', 'pre-provider failure finalizes through the lease-bound RPC'
);
select is(
  (select status::text from public.ai_quota_reservations where job_id = '00000000-0000-0000-0000-0000000000ab'),
  'RELEASED', 'pre-provider finalization releases quota atomically'
);
select is(
  (select reserved_count from public.ai_quota_usage where user_id = '00000000-0000-0000-0000-0000000000aa' and feature = 'SYLLABUS_GENERATION'),
  0, 'pre-provider finalization restores the quota counter'
);

select * from finish();
rollback;
