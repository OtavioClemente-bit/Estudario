drop function if exists public.get_ai_syllabus_source_metadata(text);

create function public.get_ai_syllabus_source_metadata(p_user_id uuid, p_path text)
returns table (
  bucket_id text,
  name text,
  owner uuid,
  metadata jsonb
)
language plpgsql
stable
security definer
set search_path = public, storage
as $$
begin
  if p_user_id is null then
    raise exception using errcode = '42501', message = 'AI_AUTH_REQUIRED';
  end if;
  if p_path is null or p_path !~ ('^' || p_user_id::text || '/[A-Za-z0-9][A-Za-z0-9._-]{0,127}\.pdf$') then
    raise exception using errcode = 'P0001', message = 'SOURCE_PATH_INVALID';
  end if;

  return query
  select object.bucket_id, object.name, object.owner, object.metadata
  from storage.objects as object
  where object.bucket_id = 'ai-syllabus-sources'
    and object.name = p_path
    and object.owner = p_user_id
    and (storage.foldername(object.name))[1] = p_user_id::text;

  if not found then
    raise exception using errcode = 'P0001', message = 'SOURCE_NOT_FOUND';
  end if;
end;
$$;

revoke all on function public.get_ai_syllabus_source_metadata(uuid, text) from public, anon, authenticated;
grant execute on function public.get_ai_syllabus_source_metadata(uuid, text) to service_role;
