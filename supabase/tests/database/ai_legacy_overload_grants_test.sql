begin;
select plan(15);

select ok(not has_function_privilege('anon', 'public.claim_ai_syllabus_worker_job(text, integer)', 'EXECUTE'), 'anon cannot execute legacy worker claim overload');
select ok(not has_function_privilege('authenticated', 'public.claim_ai_syllabus_worker_job(text, integer)', 'EXECUTE'), 'authenticated cannot execute legacy worker claim overload');
select ok(not has_function_privilege('service_role', 'public.claim_ai_syllabus_worker_job(text, integer)', 'EXECUTE'), 'service_role cannot execute legacy worker claim overload');

select ok(not has_function_privilege('anon', 'public.create_or_get_ai_job_and_reserve_quota_unlimited(public.ai_feature, text, text, jsonb)', 'EXECUTE'), 'anon cannot execute legacy unlimited create overload');
select ok(not has_function_privilege('authenticated', 'public.create_or_get_ai_job_and_reserve_quota_unlimited(public.ai_feature, text, text, jsonb)', 'EXECUTE'), 'authenticated cannot execute legacy unlimited create overload');
select ok(not has_function_privilege('service_role', 'public.create_or_get_ai_job_and_reserve_quota_unlimited(public.ai_feature, text, text, jsonb)', 'EXECUTE'), 'service_role cannot execute legacy unlimited create overload');

select ok(not has_function_privilege('anon', 'public.increment_ai_job_retry(uuid)', 'EXECUTE'), 'anon cannot execute legacy retry overload');
select ok(not has_function_privilege('authenticated', 'public.increment_ai_job_retry(uuid)', 'EXECUTE'), 'authenticated cannot execute legacy retry overload');
select ok(not has_function_privilege('service_role', 'public.increment_ai_job_retry(uuid)', 'EXECUTE'), 'service_role cannot execute legacy retry overload');

select ok(not has_function_privilege('anon', 'public.record_ai_job_provider_reconciliation(uuid, boolean)', 'EXECUTE'), 'anon cannot execute legacy reconciliation overload');
select ok(not has_function_privilege('authenticated', 'public.record_ai_job_provider_reconciliation(uuid, boolean)', 'EXECUTE'), 'authenticated cannot execute legacy reconciliation overload');
select ok(not has_function_privilege('service_role', 'public.record_ai_job_provider_reconciliation(uuid, boolean)', 'EXECUTE'), 'service_role cannot execute legacy reconciliation overload');

select ok(not has_function_privilege('anon', 'public.record_ai_job_usage(uuid, bigint, bigint, bigint)', 'EXECUTE'), 'anon cannot execute legacy usage overload');
select ok(not has_function_privilege('authenticated', 'public.record_ai_job_usage(uuid, bigint, bigint, bigint)', 'EXECUTE'), 'authenticated cannot execute legacy usage overload');
select ok(not has_function_privilege('service_role', 'public.record_ai_job_usage(uuid, bigint, bigint, bigint)', 'EXECUTE'), 'service_role cannot execute legacy usage overload');

select * from finish();
rollback;
