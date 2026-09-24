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
  -- The legacy 012 body inserts/counts one level at a time through depth 64.
  -- Reject deeper input before delegating so an upgrade cannot ACK a truncated tree.
  if exists (
    with recursive topic_tree(node, depth) as (
      select topic, 0
      from jsonb_array_elements(coalesce(p_syllabus->'subjects', '[]'::jsonb)) subject
      cross join lateral jsonb_array_elements(coalesce(subject->'topics', '[]'::jsonb)) topic
      union all
      select child, topic_tree.depth + 1
      from topic_tree
      cross join lateral jsonb_array_elements(coalesce(topic_tree.node->'children', '[]'::jsonb)) child
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
