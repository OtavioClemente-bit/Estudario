select no_plan();

select has_table('public', 'user_syllabus_mutations', 'private syllabus mutation ledger exists');
select has_column('public', 'user_syllabus_mutations', 'payload_hash', 'mutations preserve payload hashes');
select has_column('public', 'user_syllabus_mutations', 'response', 'mutations preserve the server acknowledgement');
select has_index('public', 'user_syllabus_mutations', 'user_syllabus_mutations_syllabus_idx', 'mutation lookup is scoped by owner and syllabus');

select ok(
  has_function_privilege(
    'authenticated',
    'public.upsert_private_syllabus_atomic(uuid, text, text, jsonb)',
    'EXECUTE'
  ),
  'authenticated can call the atomic private syllabus RPC'
);

select ok(
  has_function_privilege(
    'service_role',
    'public.upsert_private_syllabus_atomic(uuid, text, text, jsonb)',
    'EXECUTE'
  ),
  'the Edge Function service role can call the atomic private syllabus RPC'
);

select ok(
  exists (
    select 1
    from pg_proc
    where oid = 'public.upsert_private_syllabus_atomic(uuid, text, text, jsonb)'::regprocedure
      and prosecdef
      and prosrc like '%pg_advisory_xact_lock%'
      and prosrc like '%user_syllabus_mutations%'
  ),
  'the upsert RPC is SECURITY DEFINER and locks the mutation ledger/root transactionally'
);

select ok(
  exists (
    select 1
    from pg_constraint
    where conrelid = 'public.user_syllabus_mutations'::regclass
      and contype = 'p'
      and pg_get_constraintdef(oid) like '%owner_user_id%mutation_id%'
  ),
  'mutation identity is owner plus mutation id'
);

select ok(
  exists (
    select 1
    from pg_policies
    where schemaname = 'public'
      and tablename = 'user_syllabus_mutations'
      and policyname = 'user_syllabus_mutations_select_own'
      and coalesce(qual, '') like '%auth.uid()%'
  ),
  'mutation ledger is owner scoped'
);

select ok(
  exists (
    select 1
    from pg_policies
    where schemaname = 'public'
      and tablename = 'user_syllabi'
      and policyname = 'user_syllabi_delete_own'
      and coalesce(qual, '') like '%auth.uid()%'
  ),
  'private syllabus deletion is owner scoped'
);

select ok(
  exists (
    select 1
    from pg_constraint
    where conrelid = 'public.user_syllabus_topics'::regclass
      and contype = 'f'
      and pg_get_constraintdef(oid) like '%parent_topic_id%'
  ),
  'topic parent relationships cannot cross subject trees'
);

begin;

set local role postgres;
insert into auth.users (id, aud, role, email, created_at, updated_at)
values ('00000000-0000-0000-0000-0000000000d1', 'authenticated', 'authenticated', 'private-syllabus-rpc@example.test', now(), now())
on conflict (id) do nothing;

set local role service_role;
select set_config('request.jwt.claim.role', 'service_role', true);
select is(
  public.upsert_private_syllabus_atomic(
    '00000000-0000-0000-0000-0000000000d1'::uuid,
    'rpc-mutation-1',
    repeat('a', 64),
    '{
      "remoteSyllabusId":"00000000-0000-4000-8000-0000000000d2",
      "title":"RPC syllabus","position":0,"visibility":"PRIVATE","source":"IMPORTED",
      "sourceJobId":null,"sourceHash":null,"schemaVersion":1,"status":"ACTIVE",
      "metadata":{"stableIdentity":"rpc-identity"},
      "subjects":[{
        "remoteSubjectId":"00000000-0000-4000-8000-0000000000d3","externalId":"subject-rpc","name":"Subject RPC","position":0,
        "suggestedPriority":"HIGH","packageVersion":"estudo-v2","schemaVersion":1,"metadata":{"subject":true},
        "topics":[{
          "remoteTopicId":"00000000-0000-4000-8000-0000000000d4","externalId":"topic-rpc","parentRemoteTopicId":null,"name":"Topic RPC","position":0,
          "packageVersion":"estudo-v2","schemaVersion":1,"metadata":{"topic":true},"children":[]
        }]
      }]
    }'::jsonb
  )->>'state',
  'SYNCED',
  'atomic RPC acknowledges only after the complete tree transaction'
);
select is((select count(*)::integer from public.user_syllabi where id = '00000000-0000-4000-8000-0000000000d2'), 1, 'RPC persists the root');
select is((select count(*)::integer from public.user_syllabus_subjects where syllabus_id = '00000000-0000-4000-8000-0000000000d2'), 1, 'RPC persists subjects atomically');
select is((select count(*)::integer from public.user_syllabus_topics where subject_id = '00000000-0000-4000-8000-0000000000d3'), 1, 'RPC persists topics atomically');
select is((select count(*)::integer from public.user_syllabus_mutations where owner_user_id = '00000000-0000-0000-0000-0000000000d1' and mutation_id = 'rpc-mutation-1'), 1, 'RPC persists the ledger after the tree');
select is(
  (public.upsert_private_syllabus_atomic(
    '00000000-0000-0000-0000-0000000000d1'::uuid, 'rpc-mutation-1', repeat('a', 64),
    '{"remoteSyllabusId":"00000000-0000-4000-8000-0000000000d2"}'::jsonb
  ))->>'state',
  'SYNCED',
  'same mutation/hash replays the committed acknowledgement without rewriting the tree'
);
select throws_ok(
  $$select public.upsert_private_syllabus_atomic(
    '00000000-0000-0000-0000-0000000000d1'::uuid, 'rpc-mutation-1', repeat('b', 64), '{"remoteSyllabusId":"00000000-0000-4000-8000-0000000000d2"}'::jsonb
  )$$,
  'P0001', 'IDEMPOTENCY_KEY_CONFLICT', 'different hash is a deterministic mutation conflict'
);

with recursive nested(depth, node) as (
  select 65,
    jsonb_build_object(
      'remoteTopicId', format('00000000-0000-4000-8000-%s', lpad('65', 12, '0')),
      'externalId', 'too-deep-65', 'name', 'Too deep 65', 'position', 0,
      'parentRemoteTopicId', null, 'packageVersion', 'estudo-v2', 'schemaVersion', 1,
      'metadata', '{}'::jsonb, 'children', '[]'::jsonb
    )
  union all
  select depth - 1,
    jsonb_build_object(
      'remoteTopicId', format('00000000-0000-4000-8000-%s', lpad((depth - 1)::text, 12, '0')),
      'externalId', format('too-deep-%s', depth - 1), 'name', format('Too deep %s', depth - 1), 'position', 0,
      'parentRemoteTopicId', null, 'packageVersion', 'estudo-v2', 'schemaVersion', 1,
      'metadata', '{}'::jsonb,
      'children', jsonb_build_array(
        jsonb_set(
          node,
          '{parentRemoteTopicId}',
          to_jsonb(format('00000000-0000-4000-8000-%s', lpad((depth - 1)::text, 12, '0'))::text),
          true
        )
      )
    )
  from nested
  where depth > 0
)
select throws_ok(
  format(
    $sql$select public.upsert_private_syllabus_atomic(
      '00000000-0000-0000-0000-0000000000d1'::uuid,
      'rpc-depth-exceeded', repeat('c', 64), %L::jsonb
    )$sql$,
    jsonb_build_object(
      'remoteSyllabusId', '00000000-0000-4000-8000-0000000000e5',
      'title', 'Too deep', 'position', 0, 'visibility', 'PRIVATE', 'source', 'IMPORTED',
      'schemaVersion', 1, 'status', 'ACTIVE', 'metadata', '{}'::jsonb,
      'subjects', jsonb_build_array(jsonb_build_object(
        'remoteSubjectId', '00000000-0000-4000-8000-0000000000e6',
        'externalId', 'subject-too-deep', 'name', 'Too deep subject', 'position', 0,
        'suggestedPriority', 'NORMAL', 'packageVersion', 'estudo-v2', 'schemaVersion', 1,
        'metadata', '{}'::jsonb,
        'topics', jsonb_build_array((select node from nested where depth = 0))
      ))
    )::text
  ),
  'P0001', 'INVALID_SYLLABUS', 'tree deeper than the bounded insertion depth is rejected before persistence'
);
select is((select count(*)::integer from public.user_syllabi where id = '00000000-0000-4000-8000-0000000000e5'), 0, 'depth rejection does not persist a partial root');

select * from finish();
rollback;
