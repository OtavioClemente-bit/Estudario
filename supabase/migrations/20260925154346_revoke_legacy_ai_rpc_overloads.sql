-- These overloads were superseded by the lease-bound worker RPCs or the
-- rate-limited create RPC. They are not part of any current runtime contract.
revoke all on function public.claim_ai_syllabus_worker_job(text, integer) from public, anon, authenticated, service_role;
revoke all on function public.create_or_get_ai_job_and_reserve_quota_unlimited(public.ai_feature, text, text, jsonb) from public, anon, authenticated, service_role;
revoke all on function public.increment_ai_job_retry(uuid) from public, anon, authenticated, service_role;
revoke all on function public.record_ai_job_provider_reconciliation(uuid, boolean) from public, anon, authenticated, service_role;
revoke all on function public.record_ai_job_usage(uuid, bigint, bigint, bigint) from public, anon, authenticated, service_role;
