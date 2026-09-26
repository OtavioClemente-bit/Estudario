begin;
select no_plan();

select ok(has_function_privilege('service_role', 'public.claim_ai_job(uuid, text, integer)', 'EXECUTE'), 'active claim overload remains service executable');
select ok(not has_function_privilege('anon', 'public.claim_ai_job(uuid, text, integer)', 'EXECUTE') and not has_function_privilege('authenticated', 'public.claim_ai_job(uuid, text, integer)', 'EXECUTE'), 'active claim overload stays closed to clients');
select ok(has_function_privilege('service_role', 'public.get_ai_syllabus_source_metadata(uuid, text)', 'EXECUTE'), 'source metadata lookup stays backend executable');
select ok(not has_function_privilege('anon', 'public.get_ai_syllabus_source_metadata(uuid, text)', 'EXECUTE') and not has_function_privilege('authenticated', 'public.get_ai_syllabus_source_metadata(uuid, text)', 'EXECUTE'), 'source metadata lookup stays closed to clients');
select ok(has_function_privilege('authenticated', 'public.create_or_get_ai_job_and_reserve_quota(public.ai_feature, text, text, jsonb)', 'EXECUTE'), 'owner create RPC retains authenticated access');
select ok(has_function_privilege('authenticated', 'public.request_ai_job_cancellation(uuid)', 'EXECUTE') and has_function_privilege('authenticated', 'public.cancel_ai_job_without_provider(uuid)', 'EXECUTE'), 'owner cancellation RPCs retain authenticated access');
select ok(not has_function_privilege('service_role', 'public.claim_ai_syllabus_worker_job(text, integer)', 'EXECUTE') and not has_function_privilege('service_role', 'public.record_ai_job_provider_reconciliation(uuid, boolean)', 'EXECUTE'), 'unused legacy overloads remain revoked');
select ok(has_function_privilege('service_role', 'public.mark_ai_job_provider_execution_started(uuid, text, text, bigint)', 'EXECUTE') and not has_function_privilege('authenticated', 'public.mark_ai_job_provider_execution_started(uuid, text, text, bigint)', 'EXECUTE'), 'mark-start is service only');
select ok(not has_function_privilege('anon', 'public.ai_backfill_provider_start_outcome()', 'EXECUTE') and not has_function_privilege('authenticated', 'public.ai_backfill_provider_start_outcome()', 'EXECUTE') and not has_function_privilege('service_role', 'public.ai_backfill_provider_start_outcome()', 'EXECUTE'), 'backfill helper has no runtime execution grant');
select ok(not has_table_privilege('anon', 'public.ai_job_provider_resolution_audit', 'SELECT') and not has_table_privilege('authenticated', 'public.ai_job_provider_resolution_audit', 'SELECT') and not has_table_privilege('service_role', 'public.ai_job_provider_resolution_audit', 'SELECT'), 'audit table has no direct runtime read');
select ok(not has_table_privilege('anon', 'public.ai_job_provider_reconciliation_queue', 'SELECT') and not has_table_privilege('authenticated', 'public.ai_job_provider_reconciliation_queue', 'SELECT') and not has_table_privilege('service_role', 'public.ai_job_provider_reconciliation_queue', 'SELECT'), 'reconciliation queue has no direct runtime read');

select * from finish();
rollback;
