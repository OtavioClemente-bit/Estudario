revoke all on function public.finalize_ai_job_success(uuid, jsonb, jsonb, text, text, integer, text)
  from public, authenticated, service_role;
revoke all on function public.finalize_ai_job_failure(uuid, public.ai_job_status, text, text)
  from public, authenticated, service_role;

-- Task 6's job-finalizer uses this only while the user is authenticated. The
-- worker uses the lease-bound finalizers from 202609240008, so the runtime
-- service role must not be able to invoke this legacy release path.
revoke all on function public.release_ai_job_reservation(uuid, public.ai_job_status, text, text)
  from public, service_role;
grant execute on function public.release_ai_job_reservation(uuid, public.ai_job_status, text, text)
  to authenticated;
