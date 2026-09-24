set role service_role;
select set_config('request.jwt.claim.role', 'service_role', false);
select pg_backend_pid() as waiter_pid;
select (public.delete_private_syllabus_atomic(
  '00000000-0000-0000-0000-0000000000f8'::uuid,
  'delete-concurrent', '00000000-0000-4000-8000-0000000000f9'::uuid, repeat('e', 64)
))->>'state' as waiter_state;
