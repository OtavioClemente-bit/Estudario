create or replace function public.set_updated_at()
returns trigger
language plpgsql
as $$
begin
  new.updated_at = now();
  return new;
end;
$$;

create trigger profiles_set_updated_at
before update on public.profiles
for each row execute function public.set_updated_at();

create trigger ai_feature_flags_set_updated_at
before update on public.ai_feature_flags
for each row execute function public.set_updated_at();

create trigger ai_quota_usage_set_updated_at
before update on public.ai_quota_usage
for each row execute function public.set_updated_at();

create trigger ai_quota_reservations_set_updated_at
before update on public.ai_quota_reservations
for each row execute function public.set_updated_at();

create trigger user_syllabi_set_updated_at
before update on public.user_syllabi
for each row execute function public.set_updated_at();

create trigger user_syllabus_subjects_set_updated_at
before update on public.user_syllabus_subjects
for each row execute function public.set_updated_at();

create trigger user_syllabus_topics_set_updated_at
before update on public.user_syllabus_topics
for each row execute function public.set_updated_at();

create function public.ai_quota_period(p_feature public.ai_feature)
returns date
language sql
stable
as $$
  select case
    when p_feature = 'CONTENT_GENERATION'::public.ai_feature
      then (now() at time zone 'America/Sao_Paulo')::date
    else date '1970-01-01'
  end;
$$;

create function public.ai_quota_limit(p_feature public.ai_feature)
returns integer
language sql
immutable
as $$
  select 1;
$$;

create function public.ai_feature_flag_key(p_feature public.ai_feature)
returns text
language sql
immutable
as $$
  select case p_feature
    when 'SYLLABUS_GENERATION'::public.ai_feature then 'SYLLABUS_AI_ENABLED'
    when 'PLAN_GENERATION'::public.ai_feature then 'PLAN_AI_ENABLED'
    when 'CONTENT_GENERATION'::public.ai_feature then 'CONTENT_AI_ENABLED'
  end;
$$;

create function public.ai_assert_job_owner(p_job_user_id uuid)
returns void
language plpgsql
stable
as $$
begin
  if auth.uid() is distinct from p_job_user_id
     and coalesce(auth.role(), '') not in ('service_role', 'supabase_admin') then
    raise exception using
      errcode = '42501',
      message = 'AI_JOB_FORBIDDEN';
  end if;
end;
$$;

create function public.ai_job_transition_guard()
returns trigger
language plpgsql
as $$
declare
  internal_transition boolean := coalesce(current_setting('ai.internal_job_transition', true), '') = '1';
begin
  new.updated_at = now();

  if old.status in ('SUCCEEDED', 'FAILED', 'EXPIRED', 'CANCELLED')
     and new is distinct from old then
    raise exception using
      errcode = 'P0001',
      message = 'TERMINAL_JOB_IMMUTABLE';
  end if;

  if old.status = new.status then
    return new;
  end if;

  if not internal_transition then
    raise exception using
      errcode = 'P0001',
      message = 'JOB_STATUS_TRANSITION_REQUIRES_RPC';
  end if;

  if old.status = 'RESERVED' and new.status = 'PROCESSING' then
    if new.source_object_path is null or new.source_hash is null then
      raise exception using
        errcode = 'P0001',
        message = 'SOURCE_NOT_BOUND';
    end if;
  elsif old.status = 'RESERVED' and new.status = 'CANCELLED' then
    if old.provider_execution_started_at is not null
       or old.openai_response_id is not null
       or new.provider_execution_started_at is not null
       or new.openai_response_id is not null then
      raise exception using
        errcode = 'P0001',
        message = 'CANCELLATION_PROVIDER_STARTED';
    end if;
  elsif old.status = 'PROCESSING' and new.status = 'SUCCEEDED' then
    if new.proposal is null or jsonb_typeof(new.proposal) <> 'object' then
      raise exception using
        errcode = 'P0001',
        message = 'SUCCESS_PROPOSAL_REQUIRED';
    end if;
  elsif old.status = 'PROCESSING' and new.status = 'CANCELLED' then
    if new.provider_reconciled_at is null
       or new.provider_result_recoverable is not false then
      raise exception using
        errcode = 'P0001',
        message = 'CANCELLATION_RECONCILIATION_REQUIRED';
    end if;
  elsif old.status = 'PROCESSING' and new.status in ('FAILED', 'EXPIRED') then
    if new.provider_reconciled_at is null
       or new.provider_result_recoverable is not false then
      if coalesce(new.provider_result_recoverable, false) then
        raise exception using
          errcode = 'P0001',
          message = 'PROVIDER_RESULT_RECOVERABLE';
      end if;
      raise exception using
        errcode = 'P0001',
        message = 'PROVIDER_RECONCILIATION_REQUIRED';
    end if;
  else
    raise exception using
      errcode = 'P0001',
      message = 'INVALID_JOB_STATE_TRANSITION';
  end if;

  return new;
end;
$$;

create trigger ai_jobs_transition_guard
before update on public.ai_jobs
for each row execute function public.ai_job_transition_guard();

create function public.ai_job_reservation_required()
returns trigger
language plpgsql
as $$
begin
  if not exists (
    select 1
    from public.ai_quota_reservations
    where job_id = new.id
  ) then
    raise exception using
      errcode = 'P0001',
      message = 'AI_JOB_RESERVATION_REQUIRED';
  end if;
  return new;
end;
$$;

create constraint trigger ai_job_reservation_required
after insert on public.ai_jobs
deferrable initially deferred
for each row execute function public.ai_job_reservation_required();

create policy profiles_select_own
on public.profiles
for select to authenticated
using (user_id = auth.uid());

create policy ai_feature_flags_select_authenticated
on public.ai_feature_flags
for select to authenticated
using (true);

create policy ai_quota_usage_select_own
on public.ai_quota_usage
for select to authenticated
using (user_id = auth.uid());

create policy ai_jobs_select_own
on public.ai_jobs
for select to authenticated
using (user_id = auth.uid());

create policy ai_quota_reservations_select_own
on public.ai_quota_reservations
for select to authenticated
using (user_id = auth.uid());

create policy user_syllabi_select_own
on public.user_syllabi
for select to authenticated
using (owner_user_id = auth.uid());

create policy user_syllabi_insert_own
on public.user_syllabi
for insert to authenticated
with check (owner_user_id = auth.uid() and visibility = 'PRIVATE');

create policy user_syllabi_update_own
on public.user_syllabi
for update to authenticated
using (owner_user_id = auth.uid())
with check (owner_user_id = auth.uid() and visibility = 'PRIVATE');

create policy user_syllabi_delete_own
on public.user_syllabi
for delete to authenticated
using (owner_user_id = auth.uid());

create policy user_syllabus_subjects_select_own
on public.user_syllabus_subjects
for select to authenticated
using (
  exists (
    select 1
    from public.user_syllabi s
    where s.id = syllabus_id
      and s.owner_user_id = auth.uid()
  )
);

create policy user_syllabus_subjects_insert_own
on public.user_syllabus_subjects
for insert to authenticated
with check (
  exists (
    select 1
    from public.user_syllabi s
    where s.id = syllabus_id
      and s.owner_user_id = auth.uid()
  )
);

create policy user_syllabus_subjects_update_own
on public.user_syllabus_subjects
for update to authenticated
using (
  exists (
    select 1
    from public.user_syllabi s
    where s.id = syllabus_id
      and s.owner_user_id = auth.uid()
  )
)
with check (
  exists (
    select 1
    from public.user_syllabi s
    where s.id = syllabus_id
      and s.owner_user_id = auth.uid()
  )
);

create policy user_syllabus_subjects_delete_own
on public.user_syllabus_subjects
for delete to authenticated
using (
  exists (
    select 1
    from public.user_syllabi s
    where s.id = syllabus_id
      and s.owner_user_id = auth.uid()
  )
);

create policy user_syllabus_topics_select_own
on public.user_syllabus_topics
for select to authenticated
using (
  exists (
    select 1
    from public.user_syllabus_subjects subject
    join public.user_syllabi syllabus on syllabus.id = subject.syllabus_id
    where subject.id = subject_id
      and syllabus.owner_user_id = auth.uid()
  )
);

create policy user_syllabus_topics_insert_own
on public.user_syllabus_topics
for insert to authenticated
with check (
  exists (
    select 1
    from public.user_syllabus_subjects subject
    join public.user_syllabi syllabus on syllabus.id = subject.syllabus_id
    where subject.id = subject_id
      and syllabus.owner_user_id = auth.uid()
  )
);

create policy user_syllabus_topics_update_own
on public.user_syllabus_topics
for update to authenticated
using (
  exists (
    select 1
    from public.user_syllabus_subjects subject
    join public.user_syllabi syllabus on syllabus.id = subject.syllabus_id
    where subject.id = subject_id
      and syllabus.owner_user_id = auth.uid()
  )
)
with check (
  exists (
    select 1
    from public.user_syllabus_subjects subject
    join public.user_syllabi syllabus on syllabus.id = subject.syllabus_id
    where subject.id = subject_id
      and syllabus.owner_user_id = auth.uid()
  )
);

create policy user_syllabus_topics_delete_own
on public.user_syllabus_topics
for delete to authenticated
using (
  exists (
    select 1
    from public.user_syllabus_subjects subject
    join public.user_syllabi syllabus on syllabus.id = subject.syllabus_id
    where subject.id = subject_id
      and syllabus.owner_user_id = auth.uid()
  )
);

insert into storage.buckets (id, name, public, file_size_limit, allowed_mime_types)
values
  ('ai-syllabus-sources', 'ai-syllabus-sources', false, 52428800, array['application/pdf']::text[]),
  ('ai-syllabus-snapshots', 'ai-syllabus-snapshots', false, 52428800, array['application/json']::text[])
on conflict (id) do update
set public = false;

create policy ai_syllabus_sources_select_own
on storage.objects
for select to authenticated
using (
  bucket_id = 'ai-syllabus-sources'
  and (storage.foldername(name))[1] = auth.uid()::text
);

create policy ai_syllabus_sources_insert_own
on storage.objects
for insert to authenticated
with check (
  bucket_id = 'ai-syllabus-sources'
  and (storage.foldername(name))[1] = auth.uid()::text
);

create policy ai_syllabus_sources_update_own
on storage.objects
for update to authenticated
using (
  bucket_id = 'ai-syllabus-sources'
  and (storage.foldername(name))[1] = auth.uid()::text
)
with check (
  bucket_id = 'ai-syllabus-sources'
  and (storage.foldername(name))[1] = auth.uid()::text
);

create policy ai_syllabus_sources_delete_own
on storage.objects
for delete to authenticated
using (
  bucket_id = 'ai-syllabus-sources'
  and (storage.foldername(name))[1] = auth.uid()::text
);

create policy ai_syllabus_snapshots_select_own
on storage.objects
for select to authenticated
using (
  bucket_id = 'ai-syllabus-snapshots'
  and (storage.foldername(name))[1] = auth.uid()::text
);

create policy ai_syllabus_snapshots_insert_own
on storage.objects
for insert to authenticated
with check (
  bucket_id = 'ai-syllabus-snapshots'
  and (storage.foldername(name))[1] = auth.uid()::text
);

create policy ai_syllabus_snapshots_update_own
on storage.objects
for update to authenticated
using (
  bucket_id = 'ai-syllabus-snapshots'
  and (storage.foldername(name))[1] = auth.uid()::text
)
with check (
  bucket_id = 'ai-syllabus-snapshots'
  and (storage.foldername(name))[1] = auth.uid()::text
);

create policy ai_syllabus_snapshots_delete_own
on storage.objects
for delete to authenticated
using (
  bucket_id = 'ai-syllabus-snapshots'
  and (storage.foldername(name))[1] = auth.uid()::text
);

create function public.create_or_get_ai_job_and_reserve_quota(
  p_feature public.ai_feature,
  p_idempotency_key text,
  p_request_fingerprint text,
  p_request_payload jsonb default '{}'::jsonb
)
returns table (
  job_id uuid,
  status public.ai_job_status,
  reservation_id uuid,
  quota_period date,
  quota_remaining integer,
  reused boolean
)
language plpgsql
security definer
set search_path = public
as $$
declare
  v_user_id uuid := auth.uid();
  v_job public.ai_jobs;
  v_reservation public.ai_quota_reservations;
  v_usage public.ai_quota_usage;
  v_period date := public.ai_quota_period(p_feature);
  v_limit integer := public.ai_quota_limit(p_feature);
  v_flag_key text := public.ai_feature_flag_key(p_feature);
begin
  if v_user_id is null then
    raise exception using errcode = '42501', message = 'AI_AUTH_REQUIRED';
  end if;

  select * into v_job
  from public.ai_jobs
  where user_id = v_user_id
    and feature = p_feature
    and idempotency_key = p_idempotency_key
  for update;

  if found then
    if v_job.request_fingerprint <> p_request_fingerprint then
      raise exception using errcode = 'P0001', message = 'IDEMPOTENCY_KEY_CONFLICT';
    end if;

    select * into v_reservation
    from public.ai_quota_reservations as reservation
    where reservation.job_id = v_job.id;

    if not found then
      raise exception using errcode = 'P0001', message = 'AI_JOB_RESERVATION_REQUIRED';
    end if;

    select * into v_usage
    from public.ai_quota_usage
    where user_id = v_user_id
      and feature = p_feature
      and period_start = v_period
    for update;

    return query
    select v_job.id,
           v_job.status,
           v_reservation.id,
           v_reservation.period_start,
           greatest(v_limit - v_usage.successful_count - v_usage.reserved_count, 0),
           true;
    return;
  end if;

  if not exists (
    select 1 from public.profiles where user_id = v_user_id and beta_access
  ) or not exists (
    select 1 from public.ai_feature_flags where flag_key = 'AI_BETA_ENABLED' and enabled
  ) or not exists (
    select 1 from public.ai_feature_flags where flag_key = v_flag_key and enabled
  ) then
    raise exception using errcode = '42501', message = 'AI_ACCESS_DENIED';
  end if;

  insert into public.ai_quota_usage (user_id, feature, period_start)
  values (v_user_id, p_feature, v_period)
  on conflict (user_id, feature, period_start) do nothing;

  select * into v_usage
  from public.ai_quota_usage
  where user_id = v_user_id
    and feature = p_feature
    and period_start = v_period
  for update;

  insert into public.ai_jobs (
    user_id,
    feature,
    idempotency_key,
    request_fingerprint,
    request_payload
  )
  values (
    v_user_id,
    p_feature,
    p_idempotency_key,
    p_request_fingerprint,
    coalesce(p_request_payload, '{}'::jsonb)
  )
  on conflict (user_id, feature, idempotency_key) do nothing
  returning * into v_job;

  if not found then
    select * into v_job
    from public.ai_jobs
    where user_id = v_user_id
      and feature = p_feature
      and idempotency_key = p_idempotency_key
    for update;

    if v_job.request_fingerprint <> p_request_fingerprint then
      raise exception using errcode = 'P0001', message = 'IDEMPOTENCY_KEY_CONFLICT';
    end if;

    select * into v_reservation
    from public.ai_quota_reservations as reservation
    where reservation.job_id = v_job.id;

    if not found then
      raise exception using errcode = 'P0001', message = 'AI_JOB_RESERVATION_REQUIRED';
    end if;

    return query
    select v_job.id,
           v_job.status,
           v_reservation.id,
           v_reservation.period_start,
           greatest(v_limit - v_usage.successful_count - v_usage.reserved_count, 0),
           true;
    return;
  end if;

  if v_usage.successful_count + v_usage.reserved_count >= v_limit then
    raise exception using errcode = 'P0001', message = 'AI_QUOTA_EXHAUSTED';
  end if;

  insert into public.ai_quota_reservations (
    job_id,
    user_id,
    feature,
    period_start
  )
  values (
    v_job.id,
    v_user_id,
    p_feature,
    v_period
  )
  returning * into v_reservation;

  update public.ai_quota_usage
  set reserved_count = reserved_count + 1
  where user_id = v_user_id
    and feature = p_feature
    and period_start = v_period;

  return query
  select v_job.id,
         v_job.status,
         v_reservation.id,
         v_period,
         greatest(v_limit - v_usage.successful_count - v_usage.reserved_count - 1, 0),
         false;
end;
$$;

create function public.claim_ai_job(
  p_job_id uuid,
  p_lease_owner text,
  p_lease_seconds integer default 300
)
returns public.ai_jobs
language plpgsql
security definer
set search_path = public
as $$
declare
  v_job public.ai_jobs;
begin
  if p_lease_owner is null or length(btrim(p_lease_owner)) = 0 or p_lease_seconds <= 0 then
    raise exception using errcode = '22023', message = 'INVALID_JOB_LEASE';
  end if;

  select * into v_job
  from public.ai_jobs
  where id = p_job_id
  for update;

  if not found then
    raise exception using errcode = 'P0001', message = 'AI_JOB_NOT_FOUND';
  end if;
  perform public.ai_assert_job_owner(v_job.user_id);

  if v_job.status <> 'RESERVED' then
    raise exception using errcode = 'P0001', message = 'AI_JOB_NOT_RESERVABLE';
  end if;
  if v_job.source_object_path is null or v_job.source_hash is null then
    raise exception using errcode = 'P0001', message = 'SOURCE_NOT_BOUND';
  end if;

  perform set_config('ai.internal_job_transition', '1', true);
  update public.ai_jobs
  set status = 'PROCESSING',
      lease_owner = p_lease_owner,
      lease_expires_at = now() + make_interval(secs => p_lease_seconds),
      processing_started_at = coalesce(processing_started_at, now())
  where id = p_job_id
  returning * into v_job;

  perform set_config('ai.internal_job_transition', '0', true);

  return v_job;
end;
$$;

create function public.release_ai_job_reservation(
  p_job_id uuid,
  p_terminal_status public.ai_job_status default 'FAILED',
  p_error_code text default null,
  p_error_message text default null
)
returns public.ai_jobs
language plpgsql
security definer
set search_path = public
as $$
declare
  v_job public.ai_jobs;
  v_reservation public.ai_quota_reservations;
  v_usage public.ai_quota_usage;
begin
  if p_terminal_status not in ('FAILED', 'EXPIRED', 'CANCELLED') then
    raise exception using errcode = '22023', message = 'INVALID_RELEASE_STATUS';
  end if;

  select * into v_job
  from public.ai_jobs
  where id = p_job_id
  for update;

  if not found then
    raise exception using errcode = 'P0001', message = 'AI_JOB_NOT_FOUND';
  end if;
  perform public.ai_assert_job_owner(v_job.user_id);

  if v_job.status = p_terminal_status then
    return v_job;
  end if;
  if v_job.status in ('SUCCEEDED', 'FAILED', 'EXPIRED', 'CANCELLED') then
    raise exception using errcode = 'P0001', message = 'TERMINAL_JOB_IMMUTABLE';
  end if;

  if p_terminal_status in ('FAILED', 'EXPIRED') and v_job.status <> 'PROCESSING' then
    raise exception using errcode = 'P0001', message = 'INVALID_FAILURE_STATE';
  end if;
  if p_terminal_status in ('FAILED', 'EXPIRED') and v_job.status = 'PROCESSING'
     and (v_job.provider_reconciled_at is null or v_job.provider_result_recoverable is not false) then
    if coalesce(v_job.provider_result_recoverable, false) then
      raise exception using errcode = 'P0001', message = 'PROVIDER_RESULT_RECOVERABLE';
    end if;
    raise exception using errcode = 'P0001', message = 'PROVIDER_RECONCILIATION_REQUIRED';
  end if;
  if p_terminal_status = 'CANCELLED' and v_job.status = 'PROCESSING'
     and (v_job.provider_reconciled_at is null or v_job.provider_result_recoverable is not false) then
    raise exception using errcode = 'P0001', message = 'CANCELLATION_RECONCILIATION_REQUIRED';
  end if;
  if p_terminal_status = 'CANCELLED' and v_job.status = 'RESERVED'
     and (v_job.provider_execution_started_at is not null or v_job.openai_response_id is not null) then
    raise exception using errcode = 'P0001', message = 'CANCELLATION_PROVIDER_STARTED';
  end if;

  select * into v_reservation
  from public.ai_quota_reservations
  where job_id = v_job.id
  for update;
  if not found or v_reservation.status <> 'RESERVED' then
    raise exception using errcode = 'P0001', message = 'AI_RESERVATION_NOT_ACTIVE';
  end if;

  select * into v_usage
  from public.ai_quota_usage
  where user_id = v_reservation.user_id
    and feature = v_reservation.feature
    and period_start = v_reservation.period_start
  for update;
  if not found or v_usage.reserved_count <= 0 then
    raise exception using errcode = 'P0001', message = 'AI_QUOTA_RESERVATION_MISSING';
  end if;

  perform set_config('ai.internal_job_transition', '1', true);
  update public.ai_jobs
  set status = p_terminal_status,
      finished_at = now(),
      lease_owner = null,
      lease_expires_at = null,
      error_code = p_error_code,
      error_message = p_error_message
  where id = v_job.id
  returning * into v_job;

  perform set_config('ai.internal_job_transition', '0', true);

  update public.ai_quota_reservations
  set status = 'RELEASED',
      released_at = now()
  where id = v_reservation.id;

  update public.ai_quota_usage
  set reserved_count = reserved_count - 1
  where user_id = v_usage.user_id
    and feature = v_usage.feature
    and period_start = v_usage.period_start;

  return v_job;
end;
$$;

create function public.finalize_ai_job_success(
  p_job_id uuid,
  p_proposal jsonb,
  p_warnings jsonb default '[]'::jsonb,
  p_openai_response_id text default null,
  p_prompt_version text default null,
  p_schema_version integer default null,
  p_model_version text default null
)
returns public.ai_jobs
language plpgsql
security definer
set search_path = public
as $$
declare
  v_job public.ai_jobs;
  v_reservation public.ai_quota_reservations;
  v_usage public.ai_quota_usage;
begin
  if p_proposal is null or jsonb_typeof(p_proposal) <> 'object' then
    raise exception using errcode = '22023', message = 'INVALID_PROPOSAL';
  end if;
  if p_warnings is null or jsonb_typeof(p_warnings) <> 'array' then
    raise exception using errcode = '22023', message = 'INVALID_WARNINGS';
  end if;

  select * into v_job
  from public.ai_jobs
  where id = p_job_id
  for update;
  if not found then
    raise exception using errcode = 'P0001', message = 'AI_JOB_NOT_FOUND';
  end if;
  perform public.ai_assert_job_owner(v_job.user_id);
  if v_job.status = 'SUCCEEDED' then
    return v_job;
  end if;
  if v_job.status <> 'PROCESSING' then
    raise exception using errcode = 'P0001', message = 'AI_JOB_NOT_PROCESSING';
  end if;

  select * into v_reservation
  from public.ai_quota_reservations
  where job_id = v_job.id
  for update;
  if not found or v_reservation.status <> 'RESERVED' then
    raise exception using errcode = 'P0001', message = 'AI_RESERVATION_NOT_ACTIVE';
  end if;

  select * into v_usage
  from public.ai_quota_usage
  where user_id = v_reservation.user_id
    and feature = v_reservation.feature
    and period_start = v_reservation.period_start
  for update;
  if not found or v_usage.reserved_count <= 0 then
    raise exception using errcode = 'P0001', message = 'AI_QUOTA_RESERVATION_MISSING';
  end if;

  perform set_config('ai.internal_job_transition', '1', true);
  update public.ai_jobs
  set status = 'SUCCEEDED',
      openai_response_id = coalesce(p_openai_response_id, openai_response_id),
      provider_execution_started_at = case
        when p_openai_response_id is not null then coalesce(provider_execution_started_at, now())
        else provider_execution_started_at
      end,
      prompt_version = coalesce(p_prompt_version, prompt_version),
      schema_version = coalesce(p_schema_version, schema_version),
      model_version = coalesce(p_model_version, model_version),
      proposal = p_proposal,
      warnings = p_warnings,
      finished_at = now(),
      lease_owner = null,
      lease_expires_at = null,
      error_code = null,
      error_message = null
  where id = v_job.id
  returning * into v_job;

  perform set_config('ai.internal_job_transition', '0', true);

  update public.ai_quota_reservations
  set status = 'CONSUMED',
      consumed_at = now()
  where id = v_reservation.id;

  update public.ai_quota_usage
  set reserved_count = reserved_count - 1,
      successful_count = successful_count + 1
  where user_id = v_usage.user_id
    and feature = v_usage.feature
    and period_start = v_usage.period_start;

  return v_job;
end;
$$;

create function public.finalize_ai_job_failure(
  p_job_id uuid,
  p_terminal_status public.ai_job_status default 'FAILED',
  p_error_code text default null,
  p_error_message text default null
)
returns public.ai_jobs
language plpgsql
security definer
set search_path = public
as $$
declare
  v_job public.ai_jobs;
begin
  if p_terminal_status not in ('FAILED', 'EXPIRED') then
    raise exception using errcode = '22023', message = 'INVALID_FAILURE_STATUS';
  end if;

  select * into v_job
  from public.release_ai_job_reservation(
    p_job_id,
    p_terminal_status,
    p_error_code,
    p_error_message
  );
  return v_job;
end;
$$;

revoke all on function public.create_or_get_ai_job_and_reserve_quota(public.ai_feature, text, text, jsonb) from public;
revoke all on function public.claim_ai_job(uuid, text, integer) from public;
revoke all on function public.release_ai_job_reservation(uuid, public.ai_job_status, text, text) from public;
revoke all on function public.finalize_ai_job_success(uuid, jsonb, jsonb, text, text, integer, text) from public;
revoke all on function public.finalize_ai_job_failure(uuid, public.ai_job_status, text, text) from public;

grant execute on function public.create_or_get_ai_job_and_reserve_quota(public.ai_feature, text, text, jsonb) to authenticated, service_role;
grant execute on function public.claim_ai_job(uuid, text, integer) to authenticated, service_role;
grant execute on function public.release_ai_job_reservation(uuid, public.ai_job_status, text, text) to authenticated, service_role;
grant execute on function public.finalize_ai_job_success(uuid, jsonb, jsonb, text, text, integer, text) to authenticated, service_role;
grant execute on function public.finalize_ai_job_failure(uuid, public.ai_job_status, text, text) to authenticated, service_role;
