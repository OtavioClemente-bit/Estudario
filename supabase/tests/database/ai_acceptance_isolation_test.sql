select no_plan();
begin;

insert into auth.users (id, aud, role, email, created_at, updated_at) values
  ('00000000-0000-0000-0000-0000000000e1', 'authenticated', 'authenticated', 'acceptance-a@example.test', now(), now()),
  ('00000000-0000-0000-0000-0000000000e2', 'authenticated', 'authenticated', 'acceptance-b@example.test', now(), now());

insert into public.user_syllabi (id, owner_user_id, title, position, visibility, source, schema_version, status, metadata) values
  ('00000000-0000-4000-8000-0000000000e1', '00000000-0000-0000-0000-0000000000e1', 'Same name', 0, 'PRIVATE', 'IMPORTED', 1, 'ACTIVE', '{}'),
  ('00000000-0000-4000-8000-0000000000e2', '00000000-0000-0000-0000-0000000000e2', 'Same name', 0, 'PRIVATE', 'IMPORTED', 1, 'ACTIVE', '{}');

insert into storage.objects (bucket_id, name, owner_id) values
  ('ai-syllabus-sources', '00000000-0000-0000-0000-0000000000e1/source.pdf', '00000000-0000-0000-0000-0000000000e1'),
  ('ai-syllabus-sources', '00000000-0000-0000-0000-0000000000e2/source.pdf', '00000000-0000-0000-0000-0000000000e2'),
  ('ai-syllabus-snapshots', '00000000-0000-0000-0000-0000000000e1/snapshot.json', '00000000-0000-0000-0000-0000000000e1'),
  ('ai-syllabus-snapshots', '00000000-0000-0000-0000-0000000000e2/snapshot.json', '00000000-0000-0000-0000-0000000000e2');

set local role authenticated;
select set_config('request.jwt.claim.role', 'authenticated', true);
select set_config('request.jwt.claim.sub', '00000000-0000-0000-0000-0000000000e1', true);

select results_eq(
  $$select id::text from public.user_syllabi order by id$$,
  $$values ('00000000-0000-4000-8000-0000000000e1'::text)$$,
  'account A reads only its private syllabus even when names match'
);
select results_eq(
  $$select name from storage.objects where bucket_id = 'ai-syllabus-sources' order by name$$,
  $$values ('00000000-0000-0000-0000-0000000000e1/source.pdf'::text)$$,
  'account A reads only its source PDF'
);
select results_eq(
  $$select name from storage.objects where bucket_id = 'ai-syllabus-snapshots' order by name$$,
  $$values ('00000000-0000-0000-0000-0000000000e1/snapshot.json'::text)$$,
  'account A reads only its private snapshot'
);
select lives_ok(
  $$insert into storage.objects (bucket_id, name, owner_id) values ('ai-syllabus-sources', '00000000-0000-0000-0000-0000000000e1/new.pdf', '00000000-0000-0000-0000-0000000000e1')$$,
  'account A can insert into its own source path'
);
select throws_ok(
  $$insert into storage.objects (bucket_id, name, owner_id) values ('ai-syllabus-sources', '00000000-0000-0000-0000-0000000000e2/new.pdf', '00000000-0000-0000-0000-0000000000e1')$$,
  '42501', null,
  'account A cannot insert into account B source path'
);

select set_config('request.jwt.claim.sub', '00000000-0000-0000-0000-0000000000e2', true);
select results_eq(
  $$select id::text from public.user_syllabi order by id$$,
  $$values ('00000000-0000-4000-8000-0000000000e2'::text)$$,
  'account B reads only its private syllabus'
);
select results_eq(
  $$select name from storage.objects where bucket_id = 'ai-syllabus-sources' order by name$$,
  $$values ('00000000-0000-0000-0000-0000000000e2/source.pdf'::text)$$,
  'account B reads only its source PDF'
);

select * from finish();
rollback;
