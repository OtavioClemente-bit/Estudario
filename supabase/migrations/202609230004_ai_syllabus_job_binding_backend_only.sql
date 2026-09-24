revoke all on function public.bind_ai_job_source(uuid, text, text, text, bigint, integer, integer, jsonb) from public, authenticated, service_role;
drop function public.bind_ai_job_source(uuid, text, text, text, bigint, integer, integer, jsonb);

create function public.bind_ai_job_source(
  p_user_id uuid,
  p_job_id uuid,
  p_source_object_path text,
  p_source_mime_type text,
  p_source_hash text,
  p_source_bytes bigint,
  p_source_pages integer,
  p_source_file_count integer,
  p_source_metadata jsonb default '{}'::jsonb
)
returns public.ai_jobs
language plpgsql
security definer
set search_path = public, storage
as $$
declare
  v_job public.ai_jobs;
begin
  if p_user_id is null then
    raise exception using errcode = '42501', message = 'AI_AUTH_REQUIRED';
  end if;
  select * into v_job
  from public.ai_jobs
  where id = p_job_id
  for update;
  if not found then
    raise exception using errcode = 'P0001', message = 'AI_JOB_NOT_FOUND';
  end if;
  if v_job.user_id is distinct from p_user_id then
    raise exception using errcode = '42501', message = 'AI_JOB_FORBIDDEN';
  end if;
  perform public.ai_assert_job_owner(v_job.user_id);
  if v_job.status <> 'RESERVED' then
    raise exception using errcode = 'P0001', message = 'AI_JOB_NOT_RESERVABLE';
  end if;
  if v_job.source_object_path is not null or v_job.source_hash is not null then
    if v_job.source_object_path = p_source_object_path and v_job.source_hash = p_source_hash then
      return v_job;
    end if;
    raise exception using errcode = 'P0001', message = 'SOURCE_ALREADY_BOUND';
  end if;

  if p_source_object_path is null
     or p_source_object_path !~ ('^' || p_user_id::text || '/[A-Za-z0-9][A-Za-z0-9._-]{0,127}\.pdf$') then
    raise exception using errcode = '22023', message = 'SOURCE_PATH_INVALID';
  end if;
  if lower(btrim(split_part(coalesce(p_source_mime_type, ''), ';', 1))) <> 'application/pdf' then
    raise exception using errcode = '22023', message = 'SOURCE_MIME_UNSUPPORTED';
  end if;
  if p_source_hash is null or p_source_hash !~ '^[0-9a-f]{64}$'
     or p_source_bytes is null or p_source_bytes < 0
     or p_source_pages is null or p_source_pages < 1
     or p_source_file_count is null or p_source_file_count < 1
     or p_source_metadata is null or jsonb_typeof(p_source_metadata) <> 'object' then
    raise exception using errcode = '22023', message = 'SOURCE_METADATA_INVALID';
  end if;
  if not exists (
    select 1
    from storage.objects as object
    where object.bucket_id = 'ai-syllabus-sources'
      and object.name = p_source_object_path
      and object.owner is not null
      and object.owner = p_user_id
      and (storage.foldername(object.name))[1] = p_user_id::text
  ) then
    raise exception using errcode = 'P0001', message = 'SOURCE_NOT_FOUND';
  end if;

  update public.ai_jobs
  set source_object_path = p_source_object_path,
      source_mime_type = lower(btrim(split_part(p_source_mime_type, ';', 1))),
      source_hash = p_source_hash,
      source_bytes = p_source_bytes,
      source_pages = p_source_pages,
      source_file_count = p_source_file_count,
      source_metadata = p_source_metadata
  where id = v_job.id
  returning * into v_job;

  return v_job;
end;
$$;

revoke all on function public.bind_ai_job_source(uuid, uuid, text, text, text, bigint, integer, integer, jsonb) from public, authenticated;
grant execute on function public.bind_ai_job_source(uuid, uuid, text, text, text, bigint, integer, integer, jsonb) to service_role;
