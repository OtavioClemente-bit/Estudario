create or replace function public.upsert_private_syllabus_atomic(
  p_owner_user_id uuid,
  p_mutation_id text,
  p_payload_hash text,
  p_syllabus jsonb
)
returns jsonb
language plpgsql
security definer
set search_path = public, pg_temp
as $$
declare
  v_existing_mutation public.user_syllabus_mutations%rowtype;
  v_existing_owner uuid;
  v_syllabus_id uuid;
  v_subject jsonb;
  v_depth integer;
  v_expected_topics integer;
  v_actual_topics integer;
  v_response jsonb;
begin
  if p_owner_user_id is null or p_mutation_id is null or length(btrim(p_mutation_id)) = 0 or length(p_mutation_id) > 255 then
    raise exception using message = 'INVALID_REQUEST', errcode = 'P0001';
  end if;
  if p_payload_hash is null or p_payload_hash !~ '^[0-9a-f]{64}$' then
    raise exception using message = 'INVALID_REQUEST', errcode = 'P0001';
  end if;
  if jsonb_typeof(p_syllabus) <> 'object' then
    raise exception using message = 'INVALID_SYLLABUS', errcode = 'P0001';
  end if;
  if auth.uid() is not null and auth.uid() <> p_owner_user_id then
    raise exception using message = 'CROSS_ACCOUNT', errcode = 'P0001';
  end if;
  if auth.uid() is null and coalesce(auth.role(), '') <> 'service_role' then
    raise exception using message = 'AUTH_REQUIRED', errcode = 'P0001';
  end if;

  begin
    v_syllabus_id := (p_syllabus->>'remoteSyllabusId')::uuid;
  exception when invalid_text_representation then
    raise exception using message = 'INVALID_SYLLABUS', errcode = 'P0001';
  end;
  if v_syllabus_id is null then
    raise exception using message = 'INVALID_SYLLABUS', errcode = 'P0001';
  end if;

  -- The unique key is the atomic claim. A concurrent caller waits on this row,
  -- then re-reads the committed response or receives the deterministic conflict.
  insert into public.user_syllabus_mutations (
    owner_user_id, mutation_id, remote_syllabus_id, operation, payload_hash
  ) values (
    p_owner_user_id, p_mutation_id, v_syllabus_id, 'UPSERT', lower(p_payload_hash)
  ) on conflict (owner_user_id, mutation_id) do nothing;

  select * into v_existing_mutation
  from public.user_syllabus_mutations
  where owner_user_id = p_owner_user_id and mutation_id = p_mutation_id
  for update;

  if v_existing_mutation.payload_hash <> lower(p_payload_hash) then
    raise exception using message = 'IDEMPOTENCY_KEY_CONFLICT', errcode = 'P0001';
  end if;
  if v_existing_mutation.remote_syllabus_id <> v_syllabus_id then
    raise exception using message = 'IDEMPOTENCY_KEY_CONFLICT', errcode = 'P0001';
  end if;
  if v_existing_mutation.response is not null then
    return v_existing_mutation.response;
  end if;
  if coalesce(jsonb_typeof(p_syllabus->'subjects'), '') <> 'array' or jsonb_array_length(p_syllabus->'subjects') < 1 then
    raise exception using message = 'INVALID_SYLLABUS', errcode = 'P0001';
  end if;

  -- Preflight the complete submitted tree before touching the existing root.
  -- The insertion/count loops below intentionally stop at depth 64 to keep
  -- parent-FK insertion bounded; a deeper tree must fail rather than be
  -- truncated and acknowledged as SYNCED.
  if exists (
    with recursive topic_tree(subject_node, node, depth) as (
      select subject, topic, 0
      from jsonb_array_elements(p_syllabus->'subjects') subject
      cross join lateral jsonb_array_elements(coalesce(subject->'topics', '[]'::jsonb)) topic
      union all
      select topic_tree.subject_node, child, topic_tree.depth + 1
      from topic_tree
      cross join lateral jsonb_array_elements(coalesce(topic_tree.node->'children', '[]'::jsonb)) child
      where topic_tree.depth < 65
    )
    select 1 from topic_tree where depth > 64
  ) then
    raise exception using message = 'INVALID_SYLLABUS', errcode = 'P0001';
  end if;

  -- Serialize mutations for the same owner/root even when the root does not exist yet.
  perform pg_advisory_xact_lock(hashtextextended(p_owner_user_id::text || ':' || v_syllabus_id::text, 0));
  select owner_user_id into v_existing_owner
  from public.user_syllabi
  where id = v_syllabus_id
  for update;
  if v_existing_owner is not null and v_existing_owner <> p_owner_user_id then
    raise exception using message = 'CROSS_ACCOUNT', errcode = 'P0001';
  end if;

  insert into public.user_syllabi (
    id, owner_user_id, title, position, visibility, source, source_job_id,
    source_hash, schema_version, status, metadata
  ) values (
    v_syllabus_id,
    p_owner_user_id,
    p_syllabus->>'title',
    (p_syllabus->>'position')::integer,
    p_syllabus->>'visibility',
    p_syllabus->>'source',
    nullif(p_syllabus->>'sourceJobId', '')::uuid,
    nullif(p_syllabus->>'sourceHash', ''),
    (p_syllabus->>'schemaVersion')::integer,
    p_syllabus->>'status',
    coalesce(p_syllabus->'metadata', '{}'::jsonb)
  )
  on conflict (id) do update set
    title = excluded.title,
    position = excluded.position,
    visibility = excluded.visibility,
    source = excluded.source,
    source_job_id = excluded.source_job_id,
    source_hash = excluded.source_hash,
    schema_version = excluded.schema_version,
    status = excluded.status,
    metadata = excluded.metadata,
    updated_at = now();

  delete from public.user_syllabus_subjects where syllabus_id = v_syllabus_id;

  for v_subject in select value from jsonb_array_elements(coalesce(p_syllabus->'subjects', '[]'::jsonb)) loop
    insert into public.user_syllabus_subjects (
      id, syllabus_id, external_id, name, position, suggested_priority,
      package_version, schema_version, metadata
    ) values (
      (v_subject->>'remoteSubjectId')::uuid,
      v_syllabus_id,
      v_subject->>'externalId',
      v_subject->>'name',
      (v_subject->>'position')::integer,
      v_subject->>'suggestedPriority',
      v_subject->>'packageVersion',
      (v_subject->>'schemaVersion')::integer,
      coalesce(v_subject->'metadata', '{}'::jsonb)
    );

    -- Insert one depth at a time so the non-deferrable same-subject parent FK
    -- is satisfied without inventing a second tree format.
    for v_depth in 0..64 loop
      with recursive topic_tree(node, parent_remote_id, depth) as (
        select topic, null::text, 0
        from jsonb_array_elements(coalesce(v_subject->'topics', '[]'::jsonb)) topic
        union all
        select child, topic_tree.node->>'remoteTopicId', topic_tree.depth + 1
        from topic_tree
        cross join lateral jsonb_array_elements(coalesce(topic_tree.node->'children', '[]'::jsonb)) child
        where topic_tree.depth < 64
      )
      insert into public.user_syllabus_topics (
        id, subject_id, external_id, parent_topic_id, name, position,
        package_version, schema_version, metadata
      )
      select
        (node->>'remoteTopicId')::uuid,
        (v_subject->>'remoteSubjectId')::uuid,
        node->>'externalId',
        case when depth = 0 then null else parent_remote_id::uuid end,
        node->>'name',
        (node->>'position')::integer,
        node->>'packageVersion',
        (node->>'schemaVersion')::integer,
        coalesce(node->'metadata', '{}'::jsonb)
      from topic_tree
      where depth = v_depth
        and (
          (depth = 0 and nullif(node->>'parentRemoteTopicId', '') is null)
          or (depth > 0 and nullif(node->>'parentRemoteTopicId', '')::uuid = parent_remote_id::uuid)
        );
    end loop;

    with recursive topic_tree(node, depth) as (
      select topic, 0
      from jsonb_array_elements(coalesce(v_subject->'topics', '[]'::jsonb)) topic
      union all
      select child, topic_tree.depth + 1
      from topic_tree
      cross join lateral jsonb_array_elements(coalesce(topic_tree.node->'children', '[]'::jsonb)) child
      where topic_tree.depth < 64
    )
    select count(*) into v_expected_topics from topic_tree;
    select count(*) into v_actual_topics
    from public.user_syllabus_topics
    where subject_id = (v_subject->>'remoteSubjectId')::uuid;
    if v_expected_topics <> v_actual_topics then
      raise exception using message = 'INVALID_SYLLABUS', errcode = 'P0001';
    end if;
  end loop;

  v_response := jsonb_build_object(
    'remoteSyllabusId', v_syllabus_id,
    'jobId', null,
    'payloadHash', lower(p_payload_hash),
    'state', 'SYNCED',
    'attemptCount', 1,
    'nextAttemptAt', null,
    'safeError', null,
    'createdAt', now(),
    'updatedAt', now(),
    'attemptToken', null
  );
  update public.user_syllabus_mutations
  set response = v_response, updated_at = now()
  where owner_user_id = p_owner_user_id and mutation_id = p_mutation_id;
  return v_response;
exception
  when unique_violation or foreign_key_violation or check_violation or invalid_text_representation then
    raise exception using message = 'INVALID_SYLLABUS', errcode = 'P0001';
end;
$$;

revoke all on function public.upsert_private_syllabus_atomic(uuid, text, text, jsonb) from public;
grant execute on function public.upsert_private_syllabus_atomic(uuid, text, text, jsonb) to authenticated, service_role;
