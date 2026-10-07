-- Limite de rajada para TODA geração com IA, por conta: no máximo N jobs novos a cada 10 min.
--
-- O limite curto já existia só para o edital (ai_syllabus_rate_counters). Conteúdo, questões,
-- plano e simulado tinham apenas a cota do mês/vida, então uma conta paga (ou uma sessão roubada)
-- podia disparar dezenas de pedidos em segundos e inflar a conta da API. Aqui todo job novo
-- passa por um contador por janela de 10 minutos antes de chegar à função anterior; pedido
-- repetido (mesma chave de idempotência) não conta, como antes.

create table public.ai_job_burst_config (
  id boolean primary key default true check (id),
  max_jobs_per_window integer not null default 12 check (max_jobs_per_window > 0),
  window_seconds integer not null default 600 check (window_seconds between 60 and 3600)
);
insert into public.ai_job_burst_config (id) values (true);
alter table public.ai_job_burst_config enable row level security;
revoke all on public.ai_job_burst_config from public, anon, authenticated;

create table public.ai_job_burst_counters (
  user_id uuid not null references auth.users (id) on delete cascade,
  window_index bigint not null,
  job_count integer not null check (job_count > 0),
  primary key (user_id, window_index)
);
alter table public.ai_job_burst_counters enable row level security;
revoke all on public.ai_job_burst_counters from public, anon, authenticated;

alter function public.create_or_get_ai_job_and_reserve_quota(public.ai_feature, text, text, jsonb)
  rename to create_or_get_ai_job_and_reserve_quota_syllabus_limited;
revoke all on function public.create_or_get_ai_job_and_reserve_quota_syllabus_limited(public.ai_feature, text, text, jsonb)
  from public, anon, authenticated, service_role;

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
  v_config public.ai_job_burst_config;
  v_window_index bigint;
  v_count integer;
  v_retry_seconds integer;
begin
  if v_user_id is null then
    raise exception using errcode = '42501', message = 'AI_AUTH_REQUIRED';
  end if;

  -- Pedido repetido (mesma chave) devolve o job existente sem gastar a janela.
  if exists (
    select 1 from public.ai_jobs
    where user_id = v_user_id and feature = p_feature and idempotency_key = p_idempotency_key
  ) then
    return query select * from public.create_or_get_ai_job_and_reserve_quota_syllabus_limited(
      p_feature, p_idempotency_key, p_request_fingerprint, p_request_payload
    );
    return;
  end if;

  select * into v_config from public.ai_job_burst_config where id = true;
  if not found then
    raise exception using errcode = 'P0001', message = 'AI_RATE_LIMIT_UNAVAILABLE';
  end if;

  v_window_index := floor(extract(epoch from clock_timestamp()) / v_config.window_seconds)::bigint;
  insert into public.ai_job_burst_counters (user_id, window_index, job_count)
  values (v_user_id, v_window_index, 1)
  on conflict (user_id, window_index) do update
    set job_count = public.ai_job_burst_counters.job_count + 1
    where public.ai_job_burst_counters.job_count < v_config.max_jobs_per_window
  returning job_count into v_count;

  if v_count is null then
    v_retry_seconds := greatest(0, ceil((v_window_index + 1) * v_config.window_seconds - extract(epoch from clock_timestamp()))::integer);
    raise exception using errcode = 'P0001', message = 'AI_RATE_LIMIT_EXCEEDED',
      detail = format('retry_after_seconds=%s', v_retry_seconds);
  end if;

  return query select * from public.create_or_get_ai_job_and_reserve_quota_syllabus_limited(
    p_feature, p_idempotency_key, p_request_fingerprint, p_request_payload
  );
end;
$$;

revoke all on function public.create_or_get_ai_job_and_reserve_quota(public.ai_feature, text, text, jsonb) from public, anon;
grant execute on function public.create_or_get_ai_job_and_reserve_quota(public.ai_feature, text, text, jsonb) to authenticated, service_role;

-- Limpeza: janelas com mais de um dia não servem para nada.
create function public.ai_job_burst_counters_cleanup()
returns void language sql security definer set search_path = public as $$
  delete from public.ai_job_burst_counters
  where window_index < floor(extract(epoch from clock_timestamp()) / 60)::bigint - 1440;
$$;
revoke all on function public.ai_job_burst_counters_cleanup() from public, anon, authenticated;
