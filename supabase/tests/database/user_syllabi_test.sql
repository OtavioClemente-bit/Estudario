select no_plan();

select has_table('public', 'user_syllabus_mutations', 'private syllabus mutation ledger exists');
select has_column('public', 'user_syllabus_mutations', 'payload_hash', 'mutations preserve payload hashes');
select has_column('public', 'user_syllabus_mutations', 'response', 'mutations preserve the server acknowledgement');
select has_index('public', 'user_syllabus_mutations', 'user_syllabus_mutations_syllabus_idx', 'mutation lookup is scoped by owner and syllabus');

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

select * from finish();
