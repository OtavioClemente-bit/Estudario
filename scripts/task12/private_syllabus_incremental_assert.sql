set role service_role;

do $$
declare
  v_message text;
  v_node jsonb := jsonb_build_object(
    'remoteTopicId', '00000000-0000-4000-8000-000000000000',
    'externalId', 'deep-0', 'name', 'Deep 0', 'position', 0,
    'parentRemoteTopicId', null, 'packageVersion', 'estudo-v2', 'schemaVersion', 1,
    'metadata', '{}'::jsonb, 'children', '[]'::jsonb
  );
  v_payload jsonb;
  v_depth integer;
  v_root_count integer;
  v_subject_count integer;
  v_topic_count integer;
  v_ledger_count integer;
begin
  if to_regprocedure('public.upsert_private_syllabus_atomic(uuid, text, text, jsonb)') is null
     or to_regprocedure('public.upsert_private_syllabus_atomic_v012_legacy(uuid, text, text, jsonb)') is null then
    raise exception 'incremental migration did not install the 013 wrapper and preserve the 012 delegate';
  end if;

  select count(*) into v_root_count from public.user_syllabi where id = '00000000-0000-4000-8000-0000000000f2';
  select count(*) into v_subject_count from public.user_syllabus_subjects where syllabus_id = '00000000-0000-4000-8000-0000000000f2';
  select count(*) into v_topic_count from public.user_syllabus_topics where subject_id = '00000000-0000-4000-8000-0000000000f3';
  select count(*) into v_ledger_count from public.user_syllabus_mutations where owner_user_id = '00000000-0000-0000-0000-0000000000f1' and mutation_id = 'incremental-012-seed' and response->>'state' = 'SYNCED';
  if v_root_count <> 1 or v_subject_count <> 1 or v_topic_count <> 1 or v_ledger_count <> 1 then
    raise exception '012 seed was not committed before 013: %, %, %, %', v_root_count, v_subject_count, v_topic_count, v_ledger_count;
  end if;

  begin
    perform public.upsert_private_syllabus_atomic(
      '00000000-0000-0000-0000-0000000000f1'::uuid, 'incremental-invalid-shape', repeat('b', 64),
      '{"remoteSyllabusId":"00000000-0000-4000-8000-0000000000f5","subjects":[{"topics":{}}]}'::jsonb
    );
    raise exception 'invalid shape was ACKed';
  exception when others then
    get stacked diagnostics v_message = message_text;
    if v_message <> 'INVALID_SYLLABUS' then
      raise exception 'invalid shape leaked unexpected error: %', v_message;
    end if;
  end;

  for v_depth in 1..65 loop
    v_node := jsonb_build_object(
      'remoteTopicId', format('00000000-0000-4000-8000-%s', lpad(v_depth::text, 12, '0')),
      'externalId', format('deep-%s', v_depth), 'name', format('Deep %s', v_depth), 'position', 0,
      'parentRemoteTopicId', null, 'packageVersion', 'estudo-v2', 'schemaVersion', 1,
      'metadata', '{}'::jsonb, 'children', jsonb_build_array(v_node)
    );
  end loop;
  v_payload := jsonb_build_object(
    'remoteSyllabusId', '00000000-0000-4000-8000-0000000000f6',
    'title', 'too deep', 'position', 0, 'visibility', 'PRIVATE', 'source', 'IMPORTED',
    'schemaVersion', 1, 'status', 'ACTIVE', 'metadata', '{}'::jsonb,
    'subjects', jsonb_build_array(jsonb_build_object(
      'remoteSubjectId', '00000000-0000-4000-8000-0000000000f7',
      'externalId', 'deep-subject', 'name', 'Deep subject', 'position', 0,
      'suggestedPriority', 'NORMAL', 'packageVersion', 'estudo-v2', 'schemaVersion', 1,
      'metadata', '{}'::jsonb, 'topics', jsonb_build_array(v_node)
    ))
  );

  begin
    perform public.upsert_private_syllabus_atomic(
      '00000000-0000-0000-0000-0000000000f1'::uuid, 'incremental-invalid-depth', repeat('c', 64), v_payload
    );
    raise exception 'depth >64 was ACKed';
  exception when others then
    get stacked diagnostics v_message = message_text;
    if v_message <> 'INVALID_SYLLABUS' then
      raise exception 'depth guard leaked unexpected error: %', v_message;
    end if;
  end;

  if (select count(*) from public.user_syllabi where id = '00000000-0000-4000-8000-0000000000f6') <> 0
     or (select count(*) from public.user_syllabi where id = '00000000-0000-4000-8000-0000000000f2') <> 1
     or (select count(*) from public.user_syllabus_subjects where syllabus_id = '00000000-0000-4000-8000-0000000000f2') <> 1
     or (select count(*) from public.user_syllabus_topics where subject_id = '00000000-0000-4000-8000-0000000000f3') <> 1
     or (select count(*) from public.user_syllabus_mutations where owner_user_id = '00000000-0000-0000-0000-0000000000f1' and mutation_id = 'incremental-012-seed' and response->>'state' = 'SYNCED') <> 1 then
    raise exception 'invalid 013 calls changed data written by 012';
  end if;
end;
$$;

select 'incremental 012->013 upgrade preserved data and rejected malformed/deep trees' as result;
