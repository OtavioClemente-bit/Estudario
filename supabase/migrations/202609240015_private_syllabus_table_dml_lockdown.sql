-- Private syllabus mutations must go through the owner-scoped RPCs. Keep the
-- read path available to the existing RLS policies, but remove direct client
-- writes and preserve explicit backend DML for SECURITY DEFINER RPC support.
revoke insert, update, delete, truncate on table public.user_syllabi from public, anon, authenticated;
revoke insert, update, delete, truncate on table public.user_syllabus_subjects from public, anon, authenticated;
revoke insert, update, delete, truncate on table public.user_syllabus_topics from public, anon, authenticated;
revoke insert, update, delete, truncate on table public.user_syllabus_mutations from public, anon, authenticated;

grant insert, update, delete, truncate on table public.user_syllabi to service_role;
grant insert, update, delete, truncate on table public.user_syllabus_subjects to service_role;
grant insert, update, delete, truncate on table public.user_syllabus_topics to service_role;
grant insert, update, delete, truncate on table public.user_syllabus_mutations to service_role;
