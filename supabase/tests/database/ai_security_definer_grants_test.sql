begin;
select plan(30);

select ok(has_function_privilege('authenticated', 'public.create_or_get_ai_job_and_reserve_quota(public.ai_feature, text, text, jsonb)', 'EXECUTE'), 'authenticated can create and reserve an AI job');
select ok(not has_function_privilege('anon', 'public.create_or_get_ai_job_and_reserve_quota(public.ai_feature, text, text, jsonb)', 'EXECUTE'), 'anon cannot create and reserve an AI job');
select ok(has_function_privilege('authenticated', 'public.request_ai_job_cancellation(uuid)', 'EXECUTE'), 'authenticated can request cancellation');
select ok(has_function_privilege('authenticated', 'public.cancel_ai_job_without_provider(uuid)', 'EXECUTE'), 'authenticated can cancel before provider execution');
select ok(has_function_privilege('authenticated', 'public.upsert_private_syllabus_atomic(uuid, text, text, jsonb)', 'EXECUTE'), 'authenticated can upsert private syllabus');
select ok(has_function_privilege('authenticated', 'public.delete_private_syllabus_atomic(uuid, text, uuid, text)', 'EXECUTE'), 'authenticated can delete private syllabus');
select ok(not has_function_privilege('anon', 'public.request_ai_job_cancellation(uuid)', 'EXECUTE'), 'anon cannot request cancellation');
select ok(not has_function_privilege('anon', 'public.upsert_private_syllabus_atomic(uuid, text, text, jsonb)', 'EXECUTE'), 'anon cannot mutate private syllabus');

select ok(has_function_privilege('service_role', 'public.get_ai_syllabus_source_metadata(uuid, text)', 'EXECUTE'), 'service_role can read source metadata for an explicit owner');
select ok(not has_function_privilege('anon', 'public.get_ai_syllabus_source_metadata(uuid, text)', 'EXECUTE'), 'anon cannot call source metadata backend RPC');
select ok(not has_function_privilege('authenticated', 'public.get_ai_syllabus_source_metadata(uuid, text)', 'EXECUTE'), 'authenticated cannot call source metadata backend RPC');
select ok(to_regprocedure('public.get_ai_syllabus_source_metadata(text)') is null, 'legacy source metadata signature is removed');
select ok(has_function_privilege('service_role', 'public.bind_ai_job_source(uuid, uuid, text, text, text, bigint, integer, integer, jsonb)', 'EXECUTE'), 'service_role can bind source metadata');
select ok(not has_function_privilege('authenticated', 'public.bind_ai_job_source(uuid, uuid, text, text, text, bigint, integer, integer, jsonb)', 'EXECUTE'), 'authenticated cannot call source binding backend RPC');

select ok(has_function_privilege('service_role', 'public.claim_ai_syllabus_worker_job(text, text, integer, integer)', 'EXECUTE'), 'service_role can claim worker jobs');
select ok(not has_function_privilege('authenticated', 'public.claim_ai_syllabus_worker_job(text, text, integer, integer)', 'EXECUTE'), 'authenticated cannot claim worker jobs');
select ok(has_function_privilege('service_role', 'public.assert_ai_job_lease(uuid, text, text, bigint)', 'EXECUTE'), 'service_role can assert worker leases');
select ok(not has_function_privilege('authenticated', 'public.assert_ai_job_lease(uuid, text, text, bigint)', 'EXECUTE'), 'authenticated cannot assert worker leases');
select ok(has_function_privilege('service_role', 'public.persist_ai_job_provider_response(uuid, text, text, text, bigint)', 'EXECUTE'), 'service_role can persist provider response');
select ok(not has_function_privilege('anon', 'public.persist_ai_job_provider_response(uuid, text, text, text, bigint)', 'EXECUTE'), 'anon cannot persist provider response');
select ok(has_function_privilege('service_role', 'public.record_ai_job_provider_reconciliation(uuid, text, text, bigint, boolean)', 'EXECUTE'), 'service_role can reconcile provider');
select ok(not has_function_privilege('authenticated', 'public.record_ai_job_provider_reconciliation(uuid, text, text, bigint, boolean)', 'EXECUTE'), 'authenticated cannot reconcile provider');
select ok(has_function_privilege('service_role', 'public.record_ai_job_usage(uuid, text, text, bigint, bigint, bigint, bigint)', 'EXECUTE'), 'service_role can record usage');
select ok(not has_function_privilege('authenticated', 'public.record_ai_job_usage(uuid, text, text, bigint, bigint, bigint, bigint)', 'EXECUTE'), 'authenticated cannot record usage');
select ok(has_function_privilege('service_role', 'public.finalize_ai_job_success_with_lease(uuid, jsonb, jsonb, text, text, integer, text, text, text, bigint)', 'EXECUTE'), 'service_role can finalize success');
select ok(not has_function_privilege('authenticated', 'public.finalize_ai_job_success_with_lease(uuid, jsonb, jsonb, text, text, integer, text, text, text, bigint)', 'EXECUTE'), 'authenticated cannot finalize success');
select ok(has_function_privilege('service_role', 'public.claim_ai_job_source_cleanup(text, text, integer)', 'EXECUTE'), 'service_role can claim source cleanup');
select ok(not has_function_privilege('anon', 'public.claim_ai_job_source_cleanup(text, text, integer)', 'EXECUTE'), 'anon cannot claim source cleanup');

select ok(not has_function_privilege('service_role', 'public.finalize_ai_job_success(uuid, jsonb, jsonb, text, text, integer, text)', 'EXECUTE'), 'legacy success finalizer remains blocked');
select ok(has_function_privilege('authenticated', 'public.release_ai_job_reservation(uuid, public.ai_job_status, text, text)', 'EXECUTE'), 'authenticated retains the deliberate owner-facing reservation release contract');

select * from finish();
rollback;
