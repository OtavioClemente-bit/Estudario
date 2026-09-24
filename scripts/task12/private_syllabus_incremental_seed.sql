-- Executed after a local reset capped at 202609240012.
set role postgres;
insert into auth.users (id, aud, role, email, created_at, updated_at)
values ('00000000-0000-0000-0000-0000000000f1', 'authenticated', 'authenticated', 'task12-upgrade@example.test', now(), now())
on conflict (id) do nothing;

set role service_role;
select set_config('request.jwt.claim.role', 'service_role', false);
select public.upsert_private_syllabus_atomic(
  '00000000-0000-0000-0000-0000000000f1'::uuid,
  'incremental-012-seed',
  repeat('a', 64),
  '{
    "remoteSyllabusId":"00000000-0000-4000-8000-0000000000f2",
    "title":"012 seed","position":0,"visibility":"PRIVATE","source":"IMPORTED",
    "schemaVersion":1,"status":"ACTIVE","metadata":{"seed":"012"},
    "subjects":[{
      "remoteSubjectId":"00000000-0000-4000-8000-0000000000f3","externalId":"seed-subject","name":"Seed subject","position":0,
      "suggestedPriority":"NORMAL","packageVersion":"estudo-v2","schemaVersion":1,"metadata":{"seed":true},
      "topics":[{
        "remoteTopicId":"00000000-0000-4000-8000-0000000000f4","externalId":"seed-topic","parentRemoteTopicId":null,"name":"Seed topic","position":0,
        "packageVersion":"estudo-v2","schemaVersion":1,"metadata":{"seed":true},"children":[]
      }]
    }]
  }'::jsonb
)->>'state';
