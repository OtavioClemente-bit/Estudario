-- Only creation of a new syllabus job consumes this short-window allowance.
-- The existing quota RPC remains responsible for access, monthly quota, and reservation.
create table public.ai_syllabus_rate_limit_config (
  id boolean primary key default true check (id),
  max_attempts integer not null default 3 check (max_attempts > 0)
);
insert into public.ai_syllabus_rate_limit_config (id) values (true);

create table public.ai_syllabus_rate_counters (
  user_id uuid not null references auth.users (id) on delete cascade,
  window_index bigint not null,
  attempt_count integer not null check (attempt_count > 0),
  primary key (user_id, window_index)
);

alter table public.ai_syllabus_rate_limit_config enable row level security;
alter table public.ai_syllabus_rate_counters enable row level security;
revoke all on public.ai_syllabus_rate_limit_config from anon, authenticated;
revoke all on public.ai_syllabus_rate_counters from anon, authenticated;

alter function public.create_or_get_ai_job_and_reserve_quota(public.ai_feature, text, text, jsonb)
  rename to create_or_get_ai_job_and_reserve_quota_unlimited;
revoke all on function public.create_or_get_ai_job_and_reserve_quota_unlimited(public.ai_feature, text, text, jsonb) from public, authenticated, service_role;

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
  v_existing public.ai_jobs;
  v_limit integer;
  v_now timestamptz;
  v_window_index bigint;
  v_attempts integer;
  v_retry_seconds integer;
begin
  if v_user_id is null then
    raise exception using errcode = '42501', message = 'AI_AUTH_REQUIRED';
  end if;

  if p_feature <> 'SYLLABUS_GENERATION' then
    return query select * from public.create_or_get_ai_job_and_reserve_quota_unlimited(
      p_feature, p_idempotency_key, p_request_fingerprint, p_request_payload
    );
    return;
  end if;

  -- Serialize all creation attempts for this identity before checking idempotency.
  -- The profile row also exists for every account eligible to reserve a job.
  perform 1 from public.profiles where user_id = v_user_id for update;

  select * into v_existing from public.ai_jobs
  where user_id = v_user_id and feature = p_feature and idempotency_key = p_idempotency_key;
  if found then
    return query select * from public.create_or_get_ai_job_and_reserve_quota_unlimited(
      p_feature, p_idempotency_key, p_request_fingerprint, p_request_payload
    );
    return;
  end if;

  select max_attempts into v_limit from public.ai_syllabus_rate_limit_config where id = true;
  if v_limit is null then
    raise exception using errcode = 'P0001', message = 'AI_RATE_LIMIT_UNAVAILABLE';
  end if;

  v_now := clock_timestamp();
  v_window_index := floor(extract(epoch from v_now) / 600)::bigint;
  insert into public.ai_syllabus_rate_counters (user_id, window_index, attempt_count)
  values (v_user_id, v_window_index, 1)
  on conflict (user_id, window_index) do update
    set attempt_count = public.ai_syllabus_rate_counters.attempt_count + 1
    where public.ai_syllabus_rate_counters.attempt_count < v_limit
  returning attempt_count into v_attempts;

  if v_attempts is null then
    v_retry_seconds := greatest(0, ceil((v_window_index + 1) * 600 - extract(epoch from clock_timestamp()))::integer);
    raise exception using errcode = 'P0001', message = 'AI_RATE_LIMIT_EXCEEDED',
      detail = format('retry_after_seconds=%s', v_retry_seconds);
  end if;

  return query select * from public.create_or_get_ai_job_and_reserve_quota_unlimited(
    p_feature, p_idempotency_key, p_request_fingerprint, p_request_payload
  );
end;
$$;

revoke all on function public.create_or_get_ai_job_and_reserve_quota(public.ai_feature, text, text, jsonb) from public;
grant execute on function public.create_or_get_ai_job_and_reserve_quota(public.ai_feature, text, text, jsonb) to authenticated, service_role;
