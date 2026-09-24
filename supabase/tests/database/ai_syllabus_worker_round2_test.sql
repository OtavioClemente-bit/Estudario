select no_plan();

select ok(
  not has_function_privilege('service_role', 'public.finalize_ai_job_success(uuid, jsonb, jsonb, text, text, integer, text)', 'EXECUTE'),
  'service_role cannot execute the legacy success finalizer'
);
select ok(
  not has_function_privilege('authenticated', 'public.finalize_ai_job_success(uuid, jsonb, jsonb, text, text, integer, text)', 'EXECUTE'),
  'authenticated cannot execute the legacy success finalizer'
);
select ok(
  not has_function_privilege('service_role', 'public.finalize_ai_job_failure(uuid, public.ai_job_status, text, text)', 'EXECUTE'),
  'service_role cannot execute the legacy failure finalizer'
);
select ok(
  not has_function_privilege('authenticated', 'public.finalize_ai_job_failure(uuid, public.ai_job_status, text, text)', 'EXECUTE'),
  'authenticated cannot execute the legacy failure finalizer'
);
select ok(
  has_function_privilege('authenticated', 'public.release_ai_job_reservation(uuid, public.ai_job_status, text, text)', 'EXECUTE'),
  'Task 6 job-finalizer retains authenticated access to pre-provider release'
);
select ok(
  not has_function_privilege('service_role', 'public.release_ai_job_reservation(uuid, public.ai_job_status, text, text)', 'EXECUTE'),
  'service_role cannot execute the legacy release path'
);

begin;

do $$
begin
  insert into auth.users (id, aud, role, email, created_at, updated_at)
  values ('00000000-0000-0000-0000-0000000000ac', 'authenticated', 'authenticated', 'ai-worker-round2@example.test', now(), now())
  on conflict (id) do nothing;
  insert into public.profiles (user_id, beta_access)
  values ('00000000-0000-0000-0000-0000000000ac', true)
  on conflict (user_id) do update set beta_access = true;
end;
$$;

set local role postgres;
insert into public.ai_jobs (
  id, user_id, feature, status, idempotency_key, request_fingerprint,
  request_payload, source_object_path, source_hash, source_bytes, source_pages, source_file_count
)
values (
  '00000000-0000-0000-0000-0000000000ad',
  '00000000-0000-0000-0000-0000000000ac',
  'SYLLABUS_GENERATION', 'RESERVED', 'worker-round2-key', 'worker-round2-fingerprint',
  '{}'::jsonb, '00000000-0000-0000-0000-0000000000ac/source.pdf', repeat('a', 64), 1024, 1, 1
);
insert into public.ai_quota_usage (user_id, feature, period_start, reserved_count)
values ('00000000-0000-0000-0000-0000000000ac', 'SYLLABUS_GENERATION', (now() at time zone 'America/Sao_Paulo')::date, 1);
insert into public.ai_quota_reservations (job_id, user_id, feature, period_start)
values ('00000000-0000-0000-0000-0000000000ad', '00000000-0000-0000-0000-0000000000ac', 'SYLLABUS_GENERATION', (now() at time zone 'America/Sao_Paulo')::date);

set local role authenticated;
select set_config('request.jwt.claim.role', 'authenticated', true);
select set_config('request.jwt.claim.sub', '00000000-0000-0000-0000-0000000000ac', true);
select is(
  (select lease_owner from public.claim_ai_job('00000000-0000-0000-0000-0000000000ad'::uuid, 'legacy-worker', 60)),
  'legacy-worker', 'the old claim path owns the job before recovery'
);
set local role postgres;
update public.ai_jobs set lease_expires_at = now() - interval '1 second' where id = '00000000-0000-0000-0000-0000000000ad';

set local role service_role;
select set_config('request.jwt.claim.role', 'service_role', true);
select is(
  (select lease_owner from public.claim_ai_syllabus_worker_job('worker-round2', 'token-round2', 60, 900)),
  'worker-round2', 'worker claims the processing job before legacy RPC attempts'
);

select throws_ok(
  $$select * from public.finalize_ai_job_success(
    '00000000-0000-0000-0000-0000000000ad'::uuid,
    '{"schemaVersion":1,"subjects":[]}'::jsonb, '[]'::jsonb, 'response-old', 'syllabus-v1', 1, 'gpt-6-luna'
  )$$,
  '42501', null, 'legacy success finalization is unavailable to the runtime role'
);
select throws_ok(
  $$select * from public.finalize_ai_job_failure(
    '00000000-0000-0000-0000-0000000000ad'::uuid, 'FAILED', 'OLD', 'legacy failure'
  )$$,
  '42501', null, 'legacy failure finalization is unavailable to the runtime role'
);
select throws_ok(
  $$select * from public.release_ai_job_reservation(
    '00000000-0000-0000-0000-0000000000ad'::uuid, 'CANCELLED', 'OLD', 'stale worker'
  )$$,
  '42501', null, 'legacy release cannot mutate PROCESSING from the runtime role'
);
select is((select status::text from public.ai_jobs where id = '00000000-0000-0000-0000-0000000000ad'), 'PROCESSING', 'legacy RPC attempts do not mutate the job');
select is((select reserved_count from public.ai_quota_usage where user_id = '00000000-0000-0000-0000-0000000000ac' and feature = 'SYLLABUS_GENERATION'), 1, 'legacy RPC attempts do not mutate quota');

select * from finish();
rollback;
