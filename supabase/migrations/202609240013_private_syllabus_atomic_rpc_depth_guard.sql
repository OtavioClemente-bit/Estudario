-- Incremental upgrade for databases that already applied 202609240012.
-- Keep the 012 implementation available as the historical delegate; the
-- public function gains the bounded-tree preflight before it can persist.
alter function public.upsert_private_syllabus_atomic(uuid, text, text, jsonb)
  rename to upsert_private_syllabus_atomic_v012_legacy;

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
begin
  -- Validate container shapes before any jsonb_array_elements call. The 012
  -- delegate remains historical, but malformed JSON uses the public contract
  -- instead of leaking a PostgreSQL set-returning-function error.
  if jsonb_typeof(p_syllabus) <> 'object'
     or jsonb_typeof(p_syllabus->'subjects') <> 'array'
     or exists (
       select 1
       from jsonb_array_elements(p_syllabus->'subjects') subject
       where jsonb_typeof(subject) <> 'object'
          or (subject ? 'topics' and jsonb_typeof(subject->'topics') <> 'array')
     )
  then
    raise exception using message = 'INVALID_SYLLABUS', errcode = 'P0001';
  end if;

  if exists (
    with recursive topic_tree(node) as (
      select topic
      from jsonb_array_elements(p_syllabus->'subjects') subject
      cross join lateral jsonb_array_elements(
        case when jsonb_typeof(subject->'topics') = 'array'
             then subject->'topics' else '[]'::jsonb end
      ) topic
      union all
      select child
      from topic_tree
      cross join lateral jsonb_array_elements(
        case when jsonb_typeof(topic_tree.node->'children') = 'array'
             then topic_tree.node->'children' else '[]'::jsonb end
      ) child
      where jsonb_typeof(topic_tree.node) = 'object'
    )
    select 1
    from topic_tree
    where jsonb_typeof(node) <> 'object'
       or (node ? 'children' and jsonb_typeof(node->'children') <> 'array')
  ) then
    raise exception using message = 'INVALID_SYLLABUS', errcode = 'P0001';
  end if;

  -- The legacy 012 body inserts/counts one level at a time through depth 64.
  -- Reject deeper input before delegating so an upgrade cannot ACK a truncated tree.
  if exists (
    with recursive topic_tree(node, depth) as (
      select topic, 0
      from jsonb_array_elements(p_syllabus->'subjects') subject
      cross join lateral jsonb_array_elements(subject->'topics') topic
      union all
      select child, topic_tree.depth + 1
      from topic_tree
      cross join lateral jsonb_array_elements(
        case when jsonb_typeof(topic_tree.node->'children') = 'array'
             then topic_tree.node->'children' else '[]'::jsonb end
      ) child
      where topic_tree.depth < 65
    )
    select 1 from topic_tree where depth > 64
  ) then
    raise exception using message = 'INVALID_SYLLABUS', errcode = 'P0001';
  end if;

  return public.upsert_private_syllabus_atomic_v012_legacy(
    p_owner_user_id,
    p_mutation_id,
    p_payload_hash,
    p_syllabus
  );
end;
$$;

revoke all on function public.upsert_private_syllabus_atomic_v012_legacy(uuid, text, text, jsonb) from public, authenticated, service_role;
revoke all on function public.upsert_private_syllabus_atomic(uuid, text, text, jsonb) from public;
grant execute on function public.upsert_private_syllabus_atomic(uuid, text, text, jsonb) to authenticated, service_role;
