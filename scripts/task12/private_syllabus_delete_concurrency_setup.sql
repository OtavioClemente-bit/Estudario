set role postgres;
insert into auth.users (id, aud, role, email, created_at, updated_at)
values ('00000000-0000-0000-0000-0000000000f8', 'authenticated', 'authenticated', 'task12-delete@example.test', now(), now())
on conflict (id) do nothing;
set role service_role;
select set_config('request.jwt.claim.role', 'service_role', false);
select public.upsert_private_syllabus_atomic(
  '00000000-0000-0000-0000-0000000000f8'::uuid, 'delete-concurrency-seed', repeat('d', 64),
  '{"remoteSyllabusId":"00000000-0000-4000-8000-0000000000f9","title":"delete concurrency","position":0,"visibility":"PRIVATE","source":"IMPORTED","schemaVersion":1,"status":"ACTIVE","metadata":{},"subjects":[{"remoteSubjectId":"00000000-0000-4000-8000-0000000000fa","externalId":"delete-subject","name":"Delete subject","position":0,"suggestedPriority":"NORMAL","packageVersion":"estudo-v2","schemaVersion":1,"metadata":{},"topics":[{"remoteTopicId":"00000000-0000-4000-8000-0000000000fb","externalId":"delete-topic","parentRemoteTopicId":null,"name":"Delete topic","position":0,"packageVersion":"estudo-v2","schemaVersion":1,"metadata":{},"children":[]}]}]}'::jsonb
)->>'state';
