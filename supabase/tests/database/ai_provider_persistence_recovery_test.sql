begin;
select no_plan();
insert into auth.users (id, aud, role, email, created_at, updated_at)
values ('20000000-0000-0000-0000-000000000001', 'authenticated', 'authenticated', 'provider-recovery@example.test', now(), now());
insert into public.profiles (user_id, beta_access) values ('20000000-0000-0000-0000-000000000001', true);
insert into public.ai_jobs (id, user_id, feature, status, idempotency_key, request_fingerprint, source_object_path, source_hash,
  lease_owner, lease_token, lease_generation, lease_expires_at, processing_deadline_at)
select ('20000000-0000-0000-0000-' || lpad(n::text, 12, '0'))::uuid,
  '20000000-0000-0000-0000-000000000001'::uuid, 'SYLLABUS_GENERATION', 'PROCESSING',
  'recovery-' || n, 'recovery-' || n, 'source/file.pdf', repeat('a', 64),
  'worker', 'token', 1, now() + interval '1 minute', now() - interval '1 minute'
from generate_series(2, 7) n;
insert into public.ai_quota_usage (user_id, feature, period_start, reserved_count)
values ('20000000-0000-0000-0000-000000000001', 'SYLLABUS_GENERATION', (now() at time zone 'America/Sao_Paulo')::date, 6);
insert into public.ai_quota_reservations (job_id, user_id, feature, period_start)
select id, user_id, feature, (now() at time zone 'America/Sao_Paulo')::date
from public.ai_jobs where user_id = '20000000-0000-0000-0000-000000000001';
select set_config('request.jwt.claim.role', 'service_role', true);
create temp table original_deadline as
select processing_deadline_at from public.ai_jobs where id = '20000000-0000-0000-0000-000000000002';

select is((select provider_start_outcome from public.mark_ai_job_provider_execution_started('20000000-0000-0000-0000-000000000002', 'worker', 'token', 1)), 'IN_FLIGHT', 'mark start linearizes on active lease');
select is((select provider_start_outcome from public.persist_ai_job_provider_response('20000000-0000-0000-0000-000000000002', 'resp-normal', 'worker', 'token', 1)), 'ACCEPTED', 'normal response persistence accepts ID');
select is((select processing_deadline_at from public.ai_jobs where id = '20000000-0000-0000-0000-000000000002'),
  (select processing_deadline_at from original_deadline), 'normal persistence preserves original deadline exactly');
select is((select openai_response_id from public.persist_ai_job_provider_response('20000000-0000-0000-0000-000000000002', 'resp-normal', 'worker', 'token', 1)), 'resp-normal', 'normal persistence same ID is idempotent');
select throws_ok($$select * from public.persist_ai_job_provider_response('20000000-0000-0000-0000-000000000002', 'resp-other', 'worker', 'token', 1)$$, 'P0001', 'PROVIDER_RESPONSE_ID_CONFLICT', 'normal persistence rejects different ID');

select is((select provider_start_outcome from public.mark_ai_job_provider_execution_started('20000000-0000-0000-0000-000000000003', 'worker', 'token', 1)), 'IN_FLIGHT', 'recovery fixture starts');
update public.ai_jobs set lease_expires_at = now() - interval '1 second' where id = '20000000-0000-0000-0000-000000000003';
select is((select provider_start_outcome from public.recover_ai_job_provider_response('20000000-0000-0000-0000-000000000003', 'resp-recovered')), 'ACCEPTED', 'recovery accepts known ID without active lease');
select ok((select provider_quarantined_at is null and processing_deadline_at is null from public.ai_jobs where id = '20000000-0000-0000-0000-000000000003'), 'recovery clears quarantine and stale deadline');
select is((select openai_response_id from public.recover_ai_job_provider_response('20000000-0000-0000-0000-000000000003', 'resp-recovered')), 'resp-recovered', 'recovery same ID is idempotent');
select throws_ok($$select * from public.recover_ai_job_provider_response('20000000-0000-0000-0000-000000000003', 'resp-other')$$, 'P0001', 'PROVIDER_RESPONSE_ID_CONFLICT', 'recovery different ID conflicts');
select ok((select reserved_count = 6 and successful_count = 0 from public.ai_quota_usage where user_id = '20000000-0000-0000-0000-000000000001'), 'response recovery does not mutate quota');

select is((select provider_start_outcome from public.mark_ai_job_provider_execution_started('20000000-0000-0000-0000-000000000004', 'worker', 'token', 1)), 'IN_FLIGHT', 'NOT_SENT fixture starts');
select is((select status::text from public.finalize_ai_job_not_sent('20000000-0000-0000-0000-000000000004', 'worker', 'token', 1, 'OPENAI_API_KEY_MISSING')), 'FAILED', 'NOT_SENT terminalizes in one RPC');
select ok((select provider_start_outcome = 'NOT_SENT' and provider_quarantined_at is null and provider_reconciled_at is not null and provider_result_recoverable is false and lease_owner is null and lease_token is null from public.ai_jobs where id = '20000000-0000-0000-0000-000000000004'), 'NOT_SENT clears quarantine and lease with negative reconciliation');
select is((select status::text from public.ai_quota_reservations where job_id = '20000000-0000-0000-0000-000000000004'), 'RELEASED', 'NOT_SENT releases reservation');
select is((select reserved_count from public.ai_quota_usage where user_id = '20000000-0000-0000-0000-000000000001'), 5, 'NOT_SENT decrements reserved quota once');
select throws_ok($$select * from public.finalize_ai_job_not_sent('20000000-0000-0000-0000-000000000004', 'worker', 'token', 1, 'OPENAI_API_KEY_MISSING')$$, 'P0001', 'AI_JOB_LEASE_LOST', 'NOT_SENT cannot double-release');

select is((select provider_start_outcome from public.mark_ai_job_provider_execution_started('20000000-0000-0000-0000-000000000005', 'worker', 'token', 1)), 'IN_FLIGHT', 'cancelled recovery fixture starts');
update public.ai_jobs set provider_reconciled_at = now(), provider_result_recoverable = false where id = '20000000-0000-0000-0000-000000000005';
select throws_ok($$select * from public.release_ai_job_reservation('20000000-0000-0000-0000-000000000005', 'FAILED', 'PROVIDER_ERROR', 'The AI job did not complete')$$, 'P0001', 'CANCELLATION_RECONCILIATION_REQUIRED', 'quarantined job cannot release on stale negative reconciliation flag');
update public.ai_jobs set cancellation_requested_at = now(), lease_expires_at = now() - interval '1 second' where id = '20000000-0000-0000-0000-000000000005';
select is((select provider_start_outcome from public.recover_ai_job_provider_response('20000000-0000-0000-0000-000000000005', 'resp-cancel')), 'ACCEPTED', 'recovery permits pending cancellation');
select is((select status from public.ai_job_provider_reconciliation_queue where job_id = '20000000-0000-0000-0000-000000000005'), 'PENDING', 'recovery enqueues cancellation reconciliation');
select ok((select cancellation_requested_at is not null and processing_deadline_at is null from public.ai_jobs where id = '20000000-0000-0000-0000-000000000005'), 'recovery retains cancellation and resets stale deadline');

select is((select provider_start_outcome from public.mark_ai_job_provider_execution_started('20000000-0000-0000-0000-000000000006', 'worker', 'token', 1)), 'IN_FLIGHT', 'cancelled NOT_SENT fixture starts');
update public.ai_jobs set cancellation_requested_at = now() where id = '20000000-0000-0000-0000-000000000006';
select is((select status::text from public.finalize_ai_job_not_sent('20000000-0000-0000-0000-000000000006', 'worker', 'token', 1, 'OPENAI_API_KEY_MISSING')), 'CANCELLED', 'NOT_SENT with cancellation terminalizes cancelled');
select is((select status::text from public.ai_quota_reservations where job_id = '20000000-0000-0000-0000-000000000006'), 'RELEASED', 'cancelled NOT_SENT releases once');
select throws_ok($$select * from public.recover_ai_job_provider_response('20000000-0000-0000-0000-000000000006', 'resp-late')$$, 'P0001', 'AI_JOB_NOT_PROCESSING', 'terminal job rejects recovery');

select is((select provider_start_outcome from public.mark_ai_job_provider_execution_started('20000000-0000-0000-0000-000000000007', 'worker', 'token', 1)), 'IN_FLIGHT', 'atomic rollback fixture starts');
select is((select status::text from public.ai_quota_reservations where job_id = '20000000-0000-0000-0000-000000000007'), 'RESERVED', 'rollback fixture must enter RPC with active reservation');
create function pg_temp.fail_not_sent_quota_update() returns trigger
language plpgsql as $trigger$
begin
  if not exists (
    select 1 from public.ai_jobs
    where id = '20000000-0000-0000-0000-000000000007'
      and status = 'FAILED' and provider_start_outcome = 'NOT_SENT'
  ) or not exists (
    select 1 from public.ai_quota_reservations
    where job_id = '20000000-0000-0000-0000-000000000007' and status = 'RELEASED'
  ) then
    raise exception using errcode = 'P0001', message = 'TEST_FAILURE_BEFORE_PARTIAL_MUTATION';
  end if;
  raise exception using errcode = 'P0001', message = 'TEST_QUOTA_USAGE_FAILURE_AFTER_PARTIAL_MUTATION';
end;
$trigger$;
create trigger fail_not_sent_quota_update
before update of reserved_count on public.ai_quota_usage
for each row
when (new.user_id = '20000000-0000-0000-0000-000000000001' and old.reserved_count = 4 and new.reserved_count = 3)
execute function pg_temp.fail_not_sent_quota_update();
select throws_ok($$select * from public.finalize_ai_job_not_sent('20000000-0000-0000-0000-000000000007', 'worker', 'token', 1, 'OPENAI_API_KEY_MISSING')$$, 'P0001', 'TEST_QUOTA_USAGE_FAILURE_AFTER_PARTIAL_MUTATION', 'failed NOT_SENT transaction aborts after job and reservation updates');
select ok((select status = 'PROCESSING' and provider_start_outcome = 'IN_FLIGHT' and provider_quarantined_at is not null from public.ai_jobs where id = '20000000-0000-0000-0000-000000000007'), 'failed NOT_SENT restores quarantined job');
select is((select status::text from public.ai_quota_reservations where job_id = '20000000-0000-0000-0000-000000000007'), 'RESERVED', 'failed NOT_SENT restores reservation');
select is((select reserved_count from public.ai_quota_usage where user_id = '20000000-0000-0000-0000-000000000001'), 4, 'failed NOT_SENT preserves reserved count');

-- An old audit timestamp alone is not evidence of provider delivery.
insert into public.ai_jobs (id, user_id, feature, status, idempotency_key, request_fingerprint,
  source_object_path, source_hash, lease_owner, lease_token, lease_generation, lease_expires_at,
  provider_execution_started_at, cancellation_requested_at)
values ('20000000-0000-0000-0000-000000000008', '20000000-0000-0000-0000-000000000001',
  'SYLLABUS_GENERATION', 'PROCESSING', 'audit-only', 'audit-only', 'source/file.pdf', repeat('a', 64),
  'worker', 'token', 1, now() - interval '1 minute', now() - interval '2 minutes', now());
update public.ai_quota_usage set reserved_count = reserved_count + 1
where user_id = '20000000-0000-0000-0000-000000000001';
insert into public.ai_quota_reservations (job_id, user_id, feature, period_start)
values ('20000000-0000-0000-0000-000000000008', '20000000-0000-0000-0000-000000000001',
  'SYLLABUS_GENERATION', (now() at time zone 'America/Sao_Paulo')::date);
select is((select status::text from public.cancel_ai_job_without_provider('20000000-0000-0000-0000-000000000008')),
  'CANCELLED', 'audit timestamp alone permits definitive local cancellation');
select is((select status::text from public.ai_quota_reservations where job_id = '20000000-0000-0000-0000-000000000008'),
  'RELEASED', 'audit-only cancellation releases quota once');
select is((select reserved_count from public.ai_quota_usage where user_id = '20000000-0000-0000-0000-000000000001'),
  4, 'audit-only cancellation restores reserved count');
select * from finish();
rollback;
