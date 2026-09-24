set role service_role;
select set_config('request.jwt.claim.role', 'service_role', false);
begin;
insert into public.user_syllabus_mutations(owner_user_id, mutation_id, remote_syllabus_id, operation, payload_hash)
values ('00000000-0000-0000-0000-0000000000f8', 'delete-concurrent', '00000000-0000-4000-8000-0000000000f9', 'DELETE', repeat('e', 64));
select pg_backend_pid() as holder_pid;
select pg_sleep(2);
select (public.delete_private_syllabus_atomic(
  '00000000-0000-0000-0000-0000000000f8'::uuid,
  'delete-concurrent', '00000000-0000-4000-8000-0000000000f9'::uuid, repeat('e', 64)
))->>'state' as holder_state;
commit;
