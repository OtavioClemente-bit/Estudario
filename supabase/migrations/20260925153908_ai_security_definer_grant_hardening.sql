-- Security hardening only: preserve function bodies and signatures while making
-- backend/worker SECURITY DEFINER RPCs unreachable by client roles.

-- Legacy RPCs superseded by lease-bound/cancellation implementations remain
-- unavailable to every runtime role, including service_role.
revoke all on function public.claim_ai_job(uuid, text, integer) from public, anon, authenticated, service_role;
grant execute on function public.claim_ai_job(uuid, text, integer) to service_role;
revoke all on function public.release_ai_job_reservation(uuid, public.ai_job_status, text, text) from public, anon, service_role;
grant execute on function public.release_ai_job_reservation(uuid, public.ai_job_status, text, text) to authenticated;
revoke all on function public.finalize_ai_job_success(uuid, jsonb, jsonb, text, text, integer, text) from public, anon, authenticated, service_role;
revoke all on function public.finalize_ai_job_failure(uuid, public.ai_job_status, text, text) from public, anon, authenticated, service_role;
-- Source lookup/binding is performed by the trusted Edge Function path after
-- authentication; the Android client never calls these SECURITY DEFINER RPCs.
revoke all on function public.get_ai_syllabus_source_metadata(text) from public, anon, authenticated;
grant execute on function public.get_ai_syllabus_source_metadata(text) to service_role;
revoke all on function public.bind_ai_job_source(uuid, uuid, text, text, text, bigint, integer, integer, jsonb) from public, anon, authenticated;
grant execute on function public.bind_ai_job_source(uuid, uuid, text, text, text, bigint, integer, integer, jsonb) to service_role;

-- Lease, provider, usage, retry, finalization and source-cleanup RPCs are
-- worker-only. Explicit PUBLIC revokes prevent default EXECUTE inheritance.
revoke all on function public.claim_ai_syllabus_worker_job(text, text, integer, integer) from public, anon, authenticated;
grant execute on function public.claim_ai_syllabus_worker_job(text, text, integer, integer) to service_role;
revoke all on function public.assert_ai_job_lease(uuid, text, text, bigint) from public, anon, authenticated;
grant execute on function public.assert_ai_job_lease(uuid, text, text, bigint) to service_role;
revoke all on function public.mark_ai_job_provider_execution_started(uuid, text, text, bigint) from public, anon, authenticated;
grant execute on function public.mark_ai_job_provider_execution_started(uuid, text, text, bigint) to service_role;
revoke all on function public.persist_ai_job_provider_response(uuid, text, text, text, bigint) from public, anon, authenticated;
grant execute on function public.persist_ai_job_provider_response(uuid, text, text, text, bigint) to service_role;
revoke all on function public.record_ai_job_provider_reconciliation(uuid, text, text, bigint, boolean) from public, anon, authenticated;
grant execute on function public.record_ai_job_provider_reconciliation(uuid, text, text, bigint, boolean) to service_role;
revoke all on function public.increment_ai_job_retry(uuid, text, text, bigint) from public, anon, authenticated;
grant execute on function public.increment_ai_job_retry(uuid, text, text, bigint) to service_role;
revoke all on function public.record_ai_job_usage(uuid, text, text, bigint, bigint, bigint, bigint) from public, anon, authenticated;
grant execute on function public.record_ai_job_usage(uuid, text, text, bigint, bigint, bigint, bigint) to service_role;
revoke all on function public.finalize_ai_job_success_with_lease(uuid, jsonb, jsonb, text, text, integer, text, text, text, bigint) from public, anon, authenticated;
grant execute on function public.finalize_ai_job_success_with_lease(uuid, jsonb, jsonb, text, text, integer, text, text, text, bigint) to service_role;
revoke all on function public.finalize_ai_job_failure_with_lease(uuid, public.ai_job_status, text, text, boolean, text, text, bigint) from public, anon, authenticated;
grant execute on function public.finalize_ai_job_failure_with_lease(uuid, public.ai_job_status, text, text, boolean, text, text, bigint) to service_role;
revoke all on function public.prepare_ai_job_source_cleanup(uuid, text, text, bigint) from public, anon, authenticated;
grant execute on function public.prepare_ai_job_source_cleanup(uuid, text, text, bigint) to service_role;
revoke all on function public.complete_ai_job_source_cleanup(uuid, text, text, bigint) from public, anon, authenticated;
grant execute on function public.complete_ai_job_source_cleanup(uuid, text, text, bigint) to service_role;
revoke all on function public.fail_ai_job_source_cleanup(uuid, text, text, text, bigint) from public, anon, authenticated;
grant execute on function public.fail_ai_job_source_cleanup(uuid, text, text, text, bigint) to service_role;
revoke all on function public.claim_ai_job_source_cleanup(text, text, integer) from public, anon, authenticated;
grant execute on function public.claim_ai_job_source_cleanup(text, text, integer) to service_role;
revoke all on function public.record_ai_job_cancellation_reconciliation(uuid, text, text, boolean, text) from public, anon, authenticated;
grant execute on function public.record_ai_job_cancellation_reconciliation(uuid, text, text, boolean, text) to service_role;
revoke all on function public.finalize_ai_job_success_after_cancellation(uuid, jsonb, jsonb, text, text, integer, text) from public, anon, authenticated;
grant execute on function public.finalize_ai_job_success_after_cancellation(uuid, jsonb, jsonb, text, text, integer, text) to service_role;

-- Client/authenticated API remains limited to deliberate owner-scoped entry
-- points: create/reserve, cancellation request, pre-provider cancellation and
-- private-library atomic upsert/delete. No anonymous access is granted.
revoke execute on function public.create_or_get_ai_job_and_reserve_quota(public.ai_feature, text, text, jsonb) from public, anon;
grant execute on function public.create_or_get_ai_job_and_reserve_quota(public.ai_feature, text, text, jsonb) to authenticated, service_role;
revoke execute on function public.request_ai_job_cancellation(uuid) from public, anon;
grant execute on function public.request_ai_job_cancellation(uuid) to authenticated, service_role;
revoke execute on function public.cancel_ai_job_without_provider(uuid) from public, anon;
grant execute on function public.cancel_ai_job_without_provider(uuid) to authenticated, service_role;
revoke execute on function public.upsert_private_syllabus_atomic(uuid, text, text, jsonb) from public, anon;
grant execute on function public.upsert_private_syllabus_atomic(uuid, text, text, jsonb) to authenticated, service_role;
revoke execute on function public.delete_private_syllabus_atomic(uuid, text, uuid, text) from public, anon;
grant execute on function public.delete_private_syllabus_atomic(uuid, text, uuid, text) to authenticated, service_role;
