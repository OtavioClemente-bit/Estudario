begin;
select no_plan();
insert into auth.users (id, aud, role, email, created_at, updated_at)
values ('10000000-0000-0000-0000-000000000001', 'authenticated', 'authenticated', 'provider-migration@example.test', now(), now());
insert into public.profiles (user_id, beta_access)
values ('10000000-0000-0000-0000-000000000001', true);
update public.ai_feature_flags set enabled = true;

insert into public.ai_jobs (id, user_id, feature, idempotency_key, request_fingerprint, source_object_path, source_hash)
values ('10000000-0000-0000-0000-000000000005', '10000000-0000-0000-0000-000000000001', 'PLAN_GENERATION', 'new-default', 'new-default', 'new/source.pdf', repeat('d', 64));
select ok((select provider_start_outcome = 'NOT_STARTED' and provider_quarantined_at is null and openai_response_id is null from public.ai_jobs where id = '10000000-0000-0000-0000-000000000005'), 'ordinary insert receives safe default tuple');
select throws_ok($$insert into public.ai_jobs (user_id, feature, idempotency_key, request_fingerprint, provider_start_outcome) values ('10000000-0000-0000-0000-000000000001', 'PLAN_GENERATION', 'null-outcome', 'null-outcome', null)$$, '23502', null, 'NULL outcome is rejected');

select throws_ok($$update public.ai_jobs set openai_response_id = 'resp-invalid' where id = '10000000-0000-0000-0000-000000000005'$$, '23514', null, 'NOT_STARTED with response ID is rejected');
select throws_ok($$update public.ai_jobs set provider_quarantined_at = now() where id = '10000000-0000-0000-0000-000000000005'$$, '23514', null, 'NOT_STARTED with quarantine is rejected');
select throws_ok($$update public.ai_jobs set provider_start_outcome = 'IN_FLIGHT' where id = '10000000-0000-0000-0000-000000000005'$$, '23514', null, 'IN_FLIGHT without quarantine is rejected');
select throws_ok($$update public.ai_jobs set provider_start_outcome = 'TRANSPORT_AMBIGUOUS' where id = '10000000-0000-0000-0000-000000000005'$$, '23514', null, 'nonterminal ambiguous state needs quarantine');
select throws_ok($$update public.ai_jobs set provider_start_outcome = 'PROVIDER_REJECTED' where id = '10000000-0000-0000-0000-000000000005'$$, '23514', null, 'provider rejection needs quarantine while nonterminal');
select throws_ok($$update public.ai_jobs set provider_start_outcome = 'RESPONSE_AMBIGUOUS' where id = '10000000-0000-0000-0000-000000000005'$$, '23514', null, 'ambiguous response needs quarantine while nonterminal');
select throws_ok($$update public.ai_jobs set provider_start_outcome = 'LEGACY_AMBIGUOUS' where id = '10000000-0000-0000-0000-000000000005'$$, '23514', null, 'legacy ambiguity needs quarantine while nonterminal');
select throws_ok($$update public.ai_jobs set provider_start_outcome = 'IN_FLIGHT', openai_response_id = 'resp-invalid', provider_quarantined_at = now() where id = '10000000-0000-0000-0000-000000000005'$$, '23514', null, 'unaccepted quarantined attempt cannot hold a response ID');
select throws_ok($$update public.ai_jobs set provider_start_outcome = 'ACCEPTED' where id = '10000000-0000-0000-0000-000000000005'$$, '23514', null, 'ACCEPTED without ID is rejected');
select throws_ok($$update public.ai_jobs set provider_start_outcome = 'ACCEPTED', openai_response_id = 'resp-invalid', provider_quarantined_at = now() where id = '10000000-0000-0000-0000-000000000005'$$, '23514', null, 'ACCEPTED with quarantine is rejected');
select throws_ok($$insert into public.ai_jobs (user_id, feature, status, idempotency_key, request_fingerprint, provider_start_outcome) values ('10000000-0000-0000-0000-000000000001', 'PLAN_GENERATION', 'PROCESSING', 'invalid-not-sent', 'invalid-not-sent', 'NOT_SENT')$$, '23514', null, 'PROCESSING NOT_SENT without quarantine is rejected');
insert into public.ai_jobs (id, user_id, feature, status, idempotency_key, request_fingerprint, provider_start_outcome, provider_quarantined_at)
values ('10000000-0000-0000-0000-000000000007', '10000000-0000-0000-0000-000000000001', 'PLAN_GENERATION', 'FAILED', 'valid-not-sent', 'valid-not-sent', 'NOT_SENT', null);
select ok((select status = 'FAILED' and provider_start_outcome = 'NOT_SENT' and provider_quarantined_at is null and openai_response_id is null from public.ai_jobs where id = '10000000-0000-0000-0000-000000000007'), 'terminal NOT_SENT without quarantine or response ID is valid');
select throws_ok($$update public.ai_jobs set provider_start_outcome = 'UNKNOWN' where id = '10000000-0000-0000-0000-000000000005'$$, '23514', null, 'unknown outcome is rejected');
select is((select count(*) from pg_constraint where conrelid = 'public.ai_jobs'::regclass and conname in ('ai_jobs_provider_start_outcome_check', 'ai_jobs_provider_state_check', 'ai_jobs_provider_recoverable_id_check') and convalidated), 3::bigint, 'provider state constraints are validated after backfill');

select set_config('request.jwt.claim.role', 'service_role', true);
select is((select status::text from public.claim_ai_job('10000000-0000-0000-0000-000000000005'::uuid, 'default-worker', 60)), 'PROCESSING', 'retained claim promotes new job');
update public.ai_jobs set lease_expires_at = now() - interval '1 second' where id = '10000000-0000-0000-0000-000000000005';
select is((select id from public.claim_ai_syllabus_worker_job('provider-test', 'provider-token', 60, 900)), '10000000-0000-0000-0000-000000000005'::uuid, 'worker obtains a valid token lease');
select is((select provider_start_outcome from public.mark_ai_job_provider_execution_started('10000000-0000-0000-0000-000000000005'::uuid, 'provider-test', 'provider-token', 1)), 'IN_FLIGHT', 'mark-start accepts the defaulted job under valid lease');
select ok((select provider_quarantined_at is not null from public.ai_jobs where id = '10000000-0000-0000-0000-000000000005'), 'mark-start establishes quarantine before provider call');
select throws_ok($$select * from public.record_ai_job_provider_reconciliation('10000000-0000-0000-0000-000000000005'::uuid, 'provider-test', 'provider-token', 1, true)$$, '23514', null, 'ambiguous start without response ID cannot claim recoverability');
select ok((select provider_result_recoverable is null and provider_reconciled_at is null and provider_quarantined_at is not null from public.ai_jobs where id = '10000000-0000-0000-0000-000000000005'), 'rejected recoverability write leaves evidence and quarantine unchanged');
select is((select provider_start_outcome from public.persist_ai_job_provider_response('10000000-0000-0000-0000-000000000005'::uuid, 'resp-reclaim', 'provider-test', 'provider-token', 1)), 'ACCEPTED', 'persist keeps the known response ID');
update public.ai_jobs set lease_expires_at = now() - interval '1 second' where id = '10000000-0000-0000-0000-000000000005';
select is((select id from public.claim_ai_syllabus_worker_job('second-worker', 'second-token', 60, 900)), '10000000-0000-0000-0000-000000000005'::uuid, 'expired accepted job is reclaimable for retrieve');
select is((select id from public.claim_ai_syllabus_worker_job('third-worker', 'third-token', 60, 900)), null::uuid, 'active lease excludes a second claimant');

insert into public.ai_jobs (id, user_id, feature, status, idempotency_key, request_fingerprint, source_object_path, source_hash)
values ('10000000-0000-0000-0000-000000000006', '10000000-0000-0000-0000-000000000001', 'PLAN_GENERATION', 'FAILED', 'terminal-claim', 'terminal-claim', 'new/terminal.pdf', repeat('e', 64));
select throws_ok($$select * from public.claim_ai_job('10000000-0000-0000-0000-000000000006'::uuid, 'terminal-worker', 60)$$, 'P0001', 'AI_JOB_NOT_RESERVABLE', 'terminal job is excluded from retained claim');
select is((select id from public.claim_ai_syllabus_worker_job('fourth-worker', 'fourth-token', 60, 900)), null::uuid, 'terminal job is excluded from worker claim');

-- Exact four-argument request shape from the deployed Edge create path.
select set_config('request.jwt.claim.role', 'authenticated', true);
select set_config('request.jwt.claim.sub', '10000000-0000-0000-0000-000000000001', true);
create temporary table edge_create_result as
select * from public.create_or_get_ai_job_and_reserve_quota(
  'SYLLABUS_GENERATION', 'edge-shape-default', 'edge-shape-default', '{}'::jsonb
);
select ok((select j.provider_start_outcome = 'NOT_STARTED' and j.provider_quarantined_at is null and j.openai_response_id is null
           from public.ai_jobs j join edge_create_result r on r.job_id = j.id),
          'deployed Edge create argument shape receives safe provider defaults');

select * from finish();
rollback;
