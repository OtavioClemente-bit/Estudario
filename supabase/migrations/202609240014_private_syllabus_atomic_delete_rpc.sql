-- Delete private syllabi through the same owner-scoped mutation ledger.
create or replace function public.delete_private_syllabus_atomic(
  p_owner_user_id uuid,
  p_mutation_id text,
  p_remote_syllabus_id uuid,
  p_payload_hash text
)
returns jsonb
language plpgsql
security definer
set search_path = public, pg_temp
as $$
declare
  v_existing_mutation public.user_syllabus_mutations%rowtype;
  v_existing_owner uuid;
  v_response jsonb;
begin
  if p_owner_user_id is null or p_mutation_id is null or length(btrim(p_mutation_id)) = 0 or length(p_mutation_id) > 255 then
    raise exception using message = 'INVALID_REQUEST', errcode = 'P0001';
  end if;
  if p_remote_syllabus_id is null or p_payload_hash is null or p_payload_hash !~ '^[0-9a-f]{64}$' then
    raise exception using message = 'INVALID_REQUEST', errcode = 'P0001';
  end if;
  if auth.uid() is not null and auth.uid() <> p_owner_user_id then
    raise exception using message = 'CROSS_ACCOUNT', errcode = 'P0001';
  end if;
  if auth.uid() is null and coalesce(auth.role(), '') <> 'service_role' then
    raise exception using message = 'AUTH_REQUIRED', errcode = 'P0001';
  end if;

  insert into public.user_syllabus_mutations (
    owner_user_id, mutation_id, remote_syllabus_id, operation, payload_hash
  ) values (
    p_owner_user_id, p_mutation_id, p_remote_syllabus_id, 'DELETE', lower(p_payload_hash)
  ) on conflict (owner_user_id, mutation_id) do nothing;

  select * into v_existing_mutation
  from public.user_syllabus_mutations
  where owner_user_id = p_owner_user_id and mutation_id = p_mutation_id
  for update;

  if v_existing_mutation.operation <> 'DELETE'
     or v_existing_mutation.payload_hash <> lower(p_payload_hash)
     or v_existing_mutation.remote_syllabus_id <> p_remote_syllabus_id then
    raise exception using message = 'IDEMPOTENCY_KEY_CONFLICT', errcode = 'P0001';
  end if;
  if v_existing_mutation.response is not null then
    return v_existing_mutation.response;
  end if;

  perform pg_advisory_xact_lock(hashtextextended(p_owner_user_id::text || ':' || p_remote_syllabus_id::text, 0));
  select owner_user_id into v_existing_owner
  from public.user_syllabi
  where id = p_remote_syllabus_id
  for update;
  if v_existing_owner is null then
    raise exception using message = 'NOT_FOUND', errcode = 'P0001';
  end if;
  if v_existing_owner <> p_owner_user_id then
    raise exception using message = 'CROSS_ACCOUNT', errcode = 'P0001';
  end if;

  delete from public.user_syllabi
  where id = p_remote_syllabus_id and owner_user_id = p_owner_user_id;

  v_response := jsonb_build_object(
    'remoteSyllabusId', p_remote_syllabus_id,
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
    raise exception using message = 'INVALID_REQUEST', errcode = 'P0001';
end;
$$;

revoke all on function public.delete_private_syllabus_atomic(uuid, text, uuid, text) from public;
grant execute on function public.delete_private_syllabus_atomic(uuid, text, uuid, text) to authenticated, service_role;
