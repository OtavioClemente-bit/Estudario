begin;
select no_plan();

insert into auth.users (id, aud, role, email, created_at, updated_at)
values ('30000000-0000-0000-0000-000000000001', 'authenticated', 'authenticated', 'provider-outcome@example.test', now(), now());
insert into public.profiles (user_id, beta_access)
values ('30000000-0000-0000-0000-000000000001', true);

insert into public.ai_jobs (
  id, user_id, feature, status, idempotency_key, request_fingerprint,
  source_object_path, source_hash, lease_owner, lease_token, lease_generation, lease_expires_at
)
values
  ('30000000-0000-0000-0000-000000000011', '30000000-0000-0000-0000-000000000001', 'SYLLABUS_GENERATION', 'PROCESSING', 'outcome-rejected', 'outcome-rejected', '30000000-0000-0000-0000-000000000001/source.pdf', repeat('a', 64), 'worker', 'token', 1, now() + interval '5 minutes'),
  ('30000000-0000-0000-0000-000000000012', '30000000-0000-0000-0000-000000000001', 'SYLLABUS_GENERATION', 'PROCESSING', 'outcome-transport', 'outcome-transport', '30000000-0000-0000-0000-000000000001/source.pdf', repeat('b', 64), 'worker', 'token', 1, now() + interval '5 minutes'),
  ('30000000-0000-0000-0000-000000000013', '30000000-0000-0000-0000-000000000001', 'SYLLABUS_GENERATION', 'PROCESSING', 'outcome-response', 'outcome-response', '30000000-0000-0000-0000-000000000001/source.pdf', repeat('c', 64), 'worker', 'token', 1, now() + interval '5 minutes');

select set_config('request.jwt.claim.role', 'service_role', true);
select is((select provider_start_outcome from public.mark_ai_job_provider_execution_started('30000000-0000-0000-0000-000000000011', 'worker', 'token', 1)), 'IN_FLIGHT', 'provider rejection fixture enters canonical in-flight state');
select is((select provider_start_outcome from public.record_ai_job_provider_start_outcome('30000000-0000-0000-0000-000000000011', 'PROVIDER_REJECTED', 'worker', 'token', 1)), 'PROVIDER_REJECTED', 'HTTP rejection is classified while quarantine remains');
select is((select provider_start_outcome from public.mark_ai_job_provider_execution_started('30000000-0000-0000-0000-000000000012', 'worker', 'token', 1)), 'IN_FLIGHT', 'transport fixture enters canonical in-flight state');
select is((select provider_start_outcome from public.record_ai_job_provider_start_outcome('30000000-0000-0000-0000-000000000012', 'TRANSPORT_AMBIGUOUS', 'worker', 'token', 1)), 'TRANSPORT_AMBIGUOUS', 'transport ambiguity is classified distinctly');
select is((select provider_start_outcome from public.mark_ai_job_provider_execution_started('30000000-0000-0000-0000-000000000013', 'worker', 'token', 1)), 'IN_FLIGHT', 'malformed-response fixture enters canonical in-flight state');
select is((select provider_start_outcome from public.record_ai_job_provider_start_outcome('30000000-0000-0000-0000-000000000013', 'RESPONSE_AMBIGUOUS', 'worker', 'token', 1)), 'RESPONSE_AMBIGUOUS', 'response ambiguity is classified distinctly');
select ok((select bool_and(provider_quarantined_at is not null and openai_response_id is null and status = 'PROCESSING') from public.ai_jobs where id::text like '30000000-0000-0000-0000-00000000001_'), 'classification retains quarantine and does not terminalize the job');
select is((select count(*) from public.ai_jobs where id::text like '30000000-0000-0000-0000-00000000001_' and provider_start_outcome in ('PROVIDER_REJECTED', 'TRANSPORT_AMBIGUOUS', 'RESPONSE_AMBIGUOUS')), 3::bigint, 'all three uncertain provider outcomes remain separately observable');
select throws_ok($$select public.record_ai_job_provider_start_outcome('30000000-0000-0000-0000-000000000011', 'NOT_SENT', 'worker', 'token', 1)$$, '22023', 'INVALID_PROVIDER_START_OUTCOME', 'NOT_SENT cannot be written through the ambiguous-outcome RPC');
select throws_ok($$select public.record_ai_job_provider_start_outcome('30000000-0000-0000-0000-000000000011', 'TRANSPORT_AMBIGUOUS', 'worker', 'token', 1)$$, 'P0001', 'AI_PROVIDER_OUTCOME_NOT_RECORDABLE', 'a persisted outcome cannot be reclassified');
select ok(has_function_privilege('service_role', 'public.record_ai_job_provider_start_outcome(uuid, text, text, text, bigint)', 'EXECUTE'), 'worker outcome RPC is service-role executable');
select ok(not has_function_privilege('anon', 'public.record_ai_job_provider_start_outcome(uuid, text, text, text, bigint)', 'EXECUTE') and not has_function_privilege('authenticated', 'public.record_ai_job_provider_start_outcome(uuid, text, text, text, bigint)', 'EXECUTE'), 'worker outcome RPC is not client executable');
select ok((select prosecdef and proconfig @> array['search_path=""'] from pg_proc where oid = 'public.record_ai_job_provider_start_outcome(uuid, text, text, text, bigint)'::regprocedure), 'outcome SECURITY DEFINER function pins an empty search_path');
select ok((select provider_start_outcome = 'PROVIDER_REJECTED' and provider_quarantined_at is not null and openai_response_id is null from public.record_ai_job_provider_start_outcome('30000000-0000-0000-0000-000000000011', 'PROVIDER_REJECTED', 'worker', 'token', 1)), 'replaying the same classification is idempotent');

select * from finish();
rollback;
