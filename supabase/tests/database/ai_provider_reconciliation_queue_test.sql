select no_plan();
select is(dblink_connect('recon_setup', format('host=supabase_db_estudario-local port=5432 dbname=%s user=postgres password=postgres options=-csearch_path=', current_database())), 'OK', 'fixture connection opens');
select ok(dblink_exec('recon_setup', $sql$
  delete from public.ai_jobs where user_id = '32000000-0000-0000-0000-000000000001';
  delete from auth.users where id = '32000000-0000-0000-0000-000000000001';
  insert into auth.users (id, aud, role, email, created_at, updated_at)
  values ('32000000-0000-0000-0000-000000000001', 'authenticated', 'authenticated', 'reconciliation@example.test', now(), now());
  insert into public.profiles (user_id, beta_access) values ('32000000-0000-0000-0000-000000000001', true);
  insert into public.ai_jobs (id, user_id, feature, status, idempotency_key, request_fingerprint,
    source_object_path, source_hash, cancellation_requested_at, provider_start_outcome, openai_response_id,
    provider_execution_started_at, provider_quarantined_at)
  values
    ('32000000-0000-0000-0000-000000000011', '32000000-0000-0000-0000-000000000001', 'SYLLABUS_GENERATION', 'PROCESSING', 'recon-one', 'recon-one', '32000000-0000-0000-0000-000000000001/source.pdf', repeat('a', 64), now(), 'ACCEPTED', 'resp-recon-one', now(), null),
    ('32000000-0000-0000-0000-000000000012', '32000000-0000-0000-0000-000000000001', 'SYLLABUS_GENERATION', 'PROCESSING', 'recon-race', 'recon-race', '32000000-0000-0000-0000-000000000001/source.pdf', repeat('b', 64), now(), 'ACCEPTED', 'resp-recon-race', now(), null),
    ('32000000-0000-0000-0000-000000000013', '32000000-0000-0000-0000-000000000001', 'SYLLABUS_GENERATION', 'PROCESSING', 'recon-no-id', 'recon-no-id', '32000000-0000-0000-0000-000000000001/source.pdf', repeat('c', 64), now(), 'TRANSPORT_AMBIGUOUS', null, now(), now()),
    ('32000000-0000-0000-0000-000000000014', '32000000-0000-0000-0000-000000000001', 'SYLLABUS_GENERATION', 'PROCESSING', 'recon-request', 'recon-request', '32000000-0000-0000-0000-000000000001/source.pdf', repeat('d', 64), null, 'ACCEPTED', 'resp-recon-request', now(), null);
  update public.ai_jobs set lease_owner = 'cancel-endpoint', lease_token = 'cancel-endpoint-token',
    lease_generation = 1, lease_expires_at = now() + interval '1 hour'
  where id = '32000000-0000-0000-0000-000000000014';
  insert into public.ai_quota_usage (user_id, feature, period_start, reserved_count)
  values ('32000000-0000-0000-0000-000000000001', 'SYLLABUS_GENERATION', (now() at time zone 'America/Sao_Paulo')::date, 4);
  insert into public.ai_quota_reservations (job_id, user_id, feature, period_start)
  select id, user_id, feature, (now() at time zone 'America/Sao_Paulo')::date from public.ai_jobs where id in ('32000000-0000-0000-0000-000000000011', '32000000-0000-0000-0000-000000000012', '32000000-0000-0000-0000-000000000013', '32000000-0000-0000-0000-000000000014');
  insert into public.ai_job_provider_reconciliation_queue (job_id, available_at, created_at)
  values ('32000000-0000-0000-0000-000000000011', now() - interval '2 minutes', now() - interval '2 minutes'),
    ('32000000-0000-0000-0000-000000000012', now() + interval '1 hour', now() - interval '1 minute'),
    ('32000000-0000-0000-0000-000000000013', now() - interval '3 minutes', now() - interval '3 minutes');
$sql$) is not null, 'known-ID cancellation jobs are queued with active quota reservations');
select is(dblink_disconnect('recon_setup'), 'OK', 'fixture connection closes');

select set_config('request.jwt.claim.sub', '32000000-0000-0000-0000-000000000001', false);
set role authenticated;
select is((select id from public.request_ai_job_cancellation('32000000-0000-0000-0000-000000000014')), '32000000-0000-0000-0000-000000000014'::uuid, 'owner cancellation request remains successful');
reset role;
select ok(has_function_privilege('authenticated', 'public.request_ai_job_cancellation(uuid)', 'EXECUTE') and has_function_privilege('service_role', 'public.request_ai_job_cancellation(uuid)', 'EXECUTE') and not has_function_privilege('anon', 'public.request_ai_job_cancellation(uuid)', 'EXECUTE'), 'cancellation request grants remain authenticated/service-role only');
select ok((select prosecdef and proconfig @> array['search_path=""'] from pg_proc where oid = 'public.request_ai_job_cancellation(uuid)'::regprocedure), 'updated cancellation RPC remains SECURITY DEFINER with an empty search_path');
select is((select count(*) from public.ai_job_provider_reconciliation_queue where job_id = '32000000-0000-0000-0000-000000000014'), 1::bigint, 'known response cancellation atomically enqueues durable reconciliation');
select ok((select status = 'PENDING' and lease_token is null from public.ai_job_provider_reconciliation_queue where job_id = '32000000-0000-0000-0000-000000000014'), 'new cancellation continuation is claimable by the backend worker');
select ok((select lease_token is null from public.ai_job_provider_reconciliation_queue where job_id = '32000000-0000-0000-0000-000000000013'), 'quarantine without response ID is never leased for provider reconciliation');

set request.jwt.claim.role = 'service_role';
select is((select id from public.claim_ai_job_provider_reconciliation('recon-worker', 'recon-token', 60, 900)), '32000000-0000-0000-0000-000000000011'::uuid, 'claim leases the first known-ID cancellation job');
select is((select id from public.claim_ai_job_provider_reconciliation('other-worker', 'other-token', 60, 900)), null::uuid, 'active lease prevents duplicate claim');
select ok((select lease_owner = 'recon-worker' and lease_token = 'recon-token' and lease_generation = 1 and lease_expires_at > now() and deadline_at > now() from public.ai_job_provider_reconciliation_queue where job_id = '32000000-0000-0000-0000-000000000011'), 'claim creates exclusive queue lease and bounded reconciliation deadline');
select ok((select lease_token is null from public.ai_job_provider_reconciliation_queue where job_id = '32000000-0000-0000-0000-000000000013'), 'quarantine without a known response ID is never claimed for provider reconciliation');
update public.ai_job_provider_reconciliation_queue set deadline_at = now() - interval '1 second' where job_id = '32000000-0000-0000-0000-000000000011';
select is((select status from public.fail_ai_job_provider_reconciliation('32000000-0000-0000-0000-000000000011', 'recon-worker', 'recon-token', 1, 'PROVIDER_TIMEOUT')), 'PENDING', 'ambiguous transport returns work to pending');
select ok((select available_at between now() + interval '899 seconds' and now() + interval '901 seconds' from public.ai_job_provider_reconciliation_queue where job_id = '32000000-0000-0000-0000-000000000011'), 'expired reconciliation deadline uses the capped retry delay');
select ok((select reserved_count = 4 from public.ai_quota_usage where user_id = '32000000-0000-0000-0000-000000000001' and feature = 'SYLLABUS_GENERATION'), 'retry scheduling does not release quota');
select is((select id from public.claim_ai_job_provider_reconciliation('early-worker', 'early-token', 60, 900)), null::uuid, 'bounded backoff prevents immediate retry');
update public.ai_job_provider_reconciliation_queue set available_at = now() - interval '1 second', lease_expires_at = now() - interval '1 second' where job_id = '32000000-0000-0000-0000-000000000011';
select is((select id from public.claim_ai_job_provider_reconciliation('retry-worker', 'retry-token', 60, 900)), '32000000-0000-0000-0000-000000000011'::uuid, 'expired/available work can be reclaimed');
select is((select lease_generation from public.ai_job_provider_reconciliation_queue where job_id = '32000000-0000-0000-0000-000000000011'), 2::bigint, 'reclaim advances lease generation');
select throws_ok($$select public.complete_ai_job_provider_reconciliation('32000000-0000-0000-0000-000000000011', 'retry-worker', 'retry-token', 2)$$, 'P0001', 'AI_RECONCILIATION_NOT_TERMINAL', 'queue cannot complete while the job remains processing');
select is((select status from public.fail_ai_job_provider_reconciliation('32000000-0000-0000-0000-000000000011', 'retry-worker', 'retry-token', 2, 'PROVIDER_TIMEOUT')), 'PENDING', 'unresolved work remains queued after another bounded failure');
select ok(has_function_privilege('service_role', 'public.claim_ai_job_provider_reconciliation(text, text, integer, integer)', 'EXECUTE') and has_function_privilege('service_role', 'public.complete_ai_job_provider_reconciliation(uuid, text, text, bigint)', 'EXECUTE') and has_function_privilege('service_role', 'public.fail_ai_job_provider_reconciliation(uuid, text, text, bigint, text)', 'EXECUTE'), 'all reconciliation lease RPCs are service-role executable');
select ok(not has_function_privilege('anon', 'public.claim_ai_job_provider_reconciliation(text, text, integer, integer)', 'EXECUTE') and not has_function_privilege('authenticated', 'public.claim_ai_job_provider_reconciliation(text, text, integer, integer)', 'EXECUTE') and not has_function_privilege('anon', 'public.complete_ai_job_provider_reconciliation(uuid, text, text, bigint)', 'EXECUTE') and not has_function_privilege('authenticated', 'public.complete_ai_job_provider_reconciliation(uuid, text, text, bigint)', 'EXECUTE') and not has_function_privilege('anon', 'public.fail_ai_job_provider_reconciliation(uuid, text, text, bigint, text)', 'EXECUTE') and not has_function_privilege('authenticated', 'public.fail_ai_job_provider_reconciliation(uuid, text, text, bigint, text)', 'EXECUTE'), 'all reconciliation lease RPCs stay closed to clients');
select ok((select bool_and(prosecdef and proconfig @> array['search_path=""']) from pg_proc where oid in (
  'public.claim_ai_job_provider_reconciliation(text, text, integer, integer)'::regprocedure,
  'public.complete_ai_job_provider_reconciliation(uuid, text, text, bigint)'::regprocedure,
  'public.fail_ai_job_provider_reconciliation(uuid, text, text, bigint, text)'::regprocedure
)), 'all reconciliation SECURITY DEFINER RPCs pin an empty search_path');

update public.ai_job_provider_reconciliation_queue set available_at = now() - interval '1 second' where job_id = '32000000-0000-0000-0000-000000000012';
update public.ai_jobs set lease_owner = null, lease_token = null, lease_expires_at = null where id = '32000000-0000-0000-0000-000000000012';
select is(dblink_connect('recon_first', format('host=supabase_db_estudario-local port=5432 dbname=%s user=postgres password=postgres options=-csearch_path=', current_database())), 'OK', 'first concurrent claimer connects');
select is(dblink_connect('recon_second', format('host=supabase_db_estudario-local port=5432 dbname=%s user=postgres password=postgres options=-csearch_path=', current_database())), 'OK', 'second concurrent claimer connects');
select ok(dblink_exec('recon_first', 'set request.jwt.claim.role = ''service_role''; begin') is not null, 'first concurrent claimer begins transaction');
select ok(dblink_exec('recon_second', 'set request.jwt.claim.role = ''service_role''') is not null, 'second concurrent claimer uses backend role');
select is((select id from dblink('recon_first', $$select (public.claim_ai_job_provider_reconciliation('race-first', 'race-token-first', 60, 900)).id$$) as claimed(id uuid)), '32000000-0000-0000-0000-000000000012'::uuid, 'first concurrent claimer obtains the only race lease');
select is((select id from dblink('recon_second', $$select (public.claim_ai_job_provider_reconciliation('race-second', 'race-token-second', 60, 900)).id$$) as claimed(id uuid)), null::uuid, 'second concurrent claimer cannot obtain the same active lease');
select ok(dblink_exec('recon_first', 'commit') is not null, 'first concurrent lease commits');
select is((select lease_owner from public.ai_job_provider_reconciliation_queue where job_id = '32000000-0000-0000-0000-000000000012'), 'race-first', 'exactly one concurrent claimant owns the queue row');
select is(dblink_disconnect('recon_first'), 'OK', 'first concurrent claimer disconnects');
select is(dblink_disconnect('recon_second'), 'OK', 'second concurrent claimer disconnects');
select is((select status::text from public.finalize_ai_job_failure_with_lease('32000000-0000-0000-0000-000000000012', 'CANCELLED', 'PROVIDER_CANCELLED', 'The AI job did not complete', false, 'race-first', 'race-token-first', 1)), 'CANCELLED', 'confirmed provider cancellation terminalizes through the existing lease-bound finalizer');
select is((select status from public.complete_ai_job_provider_reconciliation('32000000-0000-0000-0000-000000000012', 'race-first', 'race-token-first', 1)), 'DONE', 'terminal provider result completes its durable queue entry');
select is((select reserved_count from public.ai_quota_usage where user_id = '32000000-0000-0000-0000-000000000001' and feature = 'SYLLABUS_GENERATION'), 3, 'terminal reconciliation releases the quota reservation exactly once');
select throws_ok($$select public.finalize_ai_job_failure_with_lease('32000000-0000-0000-0000-000000000012', 'CANCELLED', 'PROVIDER_CANCELLED', 'The AI job did not complete', false, 'race-first', 'race-token-first', 1)$$, 'P0001', 'AI_JOB_LEASE_LOST', 'duplicate terminal processing cannot release quota again');
select is((select reserved_count from public.ai_quota_usage where user_id = '32000000-0000-0000-0000-000000000001' and feature = 'SYLLABUS_GENERATION'), 3, 'duplicate terminal processing leaves quota unchanged');

select is(dblink_connect('recon_cleanup', format('host=supabase_db_estudario-local port=5432 dbname=%s user=postgres password=postgres options=-csearch_path=', current_database())), 'OK', 'cleanup connection opens');
select ok(dblink_exec('recon_cleanup', $sql$delete from public.ai_jobs where user_id = '32000000-0000-0000-0000-000000000001'; delete from auth.users where id = '32000000-0000-0000-0000-000000000001'$sql$) is not null, 'reconciliation fixtures cleaned up');
select is(dblink_disconnect('recon_cleanup'), 'OK', 'cleanup connection closes');
select * from finish();
