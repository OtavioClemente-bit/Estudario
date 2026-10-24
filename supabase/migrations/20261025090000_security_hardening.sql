-- Endurecimento de segurança (auditoria de 24/10/2026).
--
-- 1. Pedido de IA só nasce no servidor. create_or_get_ai_job_and_reserve_quota era chamada com o
--    login da pessoa, então um app alterado podia chamá-la direto com o conteúdo que quisesse
--    (ex.: "compensated": true, que devolve a cota depois da IA = IA sem limite). Agora só a
--    função create_ai_job_for_user, com a chave interna, cria pedido; ela passa a conta adiante e
--    a lógica de cota, rajada e idempotência continua a mesma.
-- 2. dblink sai: ninguém usa e ele ficava no esquema público, liberado até para visitante.
-- 3. Prova oficial (exam_simulation) só com login.
-- 4. Arquivos: teto de tamanho e de envios por dia nos PDFs de edital e na cópia na nuvem.
-- 5. Permissões de escrita que só o RLS segurava saem das tabelas de cota, pedidos e perfis.
-- 6. Google Play: reembolso derruba o plano e não volta; vencimentos rodam todo dia.
-- 7. Cancelar depois que a IA começou não devolve a cota (o custo já existiu).

-- ------------------------------------------------------------------ 1. criação de pedido
create or replace function public.create_ai_job_for_user(
  p_user_id uuid,
  p_feature public.ai_feature,
  p_idempotency_key text,
  p_request_fingerprint text,
  p_request_payload jsonb default '{}'::jsonb
)
returns table(job_id uuid, status public.ai_job_status, reservation_id uuid, quota_period date, quota_remaining integer, reused boolean)
language plpgsql
security definer
set search_path = public
as $$
begin
  if coalesce(auth.role(), '') not in ('service_role', 'supabase_admin') then
    raise exception using errcode = '42501', message = 'AI_WORKER_FORBIDDEN';
  end if;
  if p_user_id is null then
    raise exception using errcode = '42501', message = 'AI_AUTH_REQUIRED';
  end if;
  -- A função de sempre lê a conta de auth.uid(); aqui ela é a conta que o servidor conferiu.
  perform set_config('request.jwt.claim.sub', p_user_id::text, true);
  return query select * from public.create_or_get_ai_job_and_reserve_quota(
    p_feature, p_idempotency_key, p_request_fingerprint, p_request_payload
  );
end;
$$;

revoke all on function public.create_ai_job_for_user(uuid, public.ai_feature, text, text, jsonb) from public, anon, authenticated;
grant execute on function public.create_ai_job_for_user(uuid, public.ai_feature, text, text, jsonb) to service_role;

revoke all on function public.create_or_get_ai_job_and_reserve_quota(public.ai_feature, text, text, jsonb) from public, anon, authenticated;
grant execute on function public.create_or_get_ai_job_and_reserve_quota(public.ai_feature, text, text, jsonb) to service_role;

-- ------------------------------------------------------------------ 2. dblink
drop extension if exists dblink;

-- ------------------------------------------------------------------ 3. prova oficial
drop policy if exists exam_simulation_read_published on public.exam_simulation;
create policy exam_simulation_read_published on public.exam_simulation
  for select to authenticated using (status = 'PUBLISHED');
revoke all on table public.exam_simulation from anon;

-- ------------------------------------------------------------------ 4. arquivos
update storage.buckets set file_size_limit = 31457280 where id = 'ai-syllabus-sources';   -- 30 MB
update storage.buckets set file_size_limit = 31457280 where id = 'ai-syllabus-snapshots'; -- 30 MB
update storage.buckets set file_size_limit = 26214400 where id = 'user-sync';             -- 25 MB

drop policy if exists ai_syllabus_sources_insert_own on storage.objects;
create policy ai_syllabus_sources_insert_own on storage.objects
  for insert to authenticated
  with check (
    bucket_id = 'ai-syllabus-sources'
    and (storage.foldername(name))[1] = auth.uid()::text
    and (select count(*) from storage.objects o
         where o.bucket_id = 'ai-syllabus-sources'
           and (storage.foldername(o.name))[1] = auth.uid()::text
           and o.created_at > now() - interval '1 day') < 10
  );

drop policy if exists ai_syllabus_snapshots_insert_own on storage.objects;
create policy ai_syllabus_snapshots_insert_own on storage.objects
  for insert to authenticated
  with check (
    bucket_id = 'ai-syllabus-snapshots'
    and (storage.foldername(name))[1] = auth.uid()::text
    and (select count(*) from storage.objects o
         where o.bucket_id = 'ai-syllabus-snapshots'
           and (storage.foldername(o.name))[1] = auth.uid()::text
           and o.created_at > now() - interval '1 day') < 20
  );

-- ------------------------------------------------------------------ 5. permissões das tabelas
-- O RLS já barrava (só há políticas de leitura), mas a permissão de escrita ficava aberta: um erro
-- futuro numa política abriria tudo. Fica só a leitura da própria conta.
revoke insert, update, delete, truncate, references, trigger on table
  public.ai_jobs, public.ai_quota_usage, public.ai_quota_reservations, public.ai_feature_flags,
  public.profiles, public.ai_ad_reward_interest, public.ai_plan_limits
  from anon, authenticated;
revoke all on table
  public.ai_jobs, public.ai_quota_usage, public.ai_quota_reservations, public.ai_feature_flags,
  public.profiles, public.ai_ad_reward_interest,
  public.user_syllabi, public.user_syllabus_mutations, public.user_syllabus_subjects, public.user_syllabus_topics
  from anon;
revoke truncate, trigger, references on all tables in schema public from anon, authenticated;

-- ------------------------------------------------------------------ 6. Google Play
create or replace function public.apply_play_subscription(
  p_user_id uuid,
  p_purchase_token text,
  p_product_id text,
  p_base_plan_id text,
  p_plan_tier text,
  p_state text,
  p_expires_at timestamptz,
  p_auto_renewing boolean
)
returns void
language plpgsql
security definer
set search_path = public
as $$
declare
  v_owner uuid;
  v_best record;
begin
  -- Um token pertence a uma conta só: não deixa outra conta "restaurar" a compra alheia.
  select user_id into v_owner from public.play_subscriptions where purchase_token = p_purchase_token;
  if v_owner is not null and v_owner <> p_user_id then
    raise exception 'purchase_belongs_to_another_account' using errcode = '42501';
  end if;

  -- Compra reembolsada (VOIDED, vindo do aviso do Google) não volta a valer, mesmo que o Google
  -- ainda mostre a assinatura ativa até o fim do período.
  insert into public.play_subscriptions (purchase_token, user_id, product_id, base_plan_id, plan_tier, state, expires_at, auto_renewing)
  values (p_purchase_token, p_user_id, p_product_id, p_base_plan_id, p_plan_tier, p_state, p_expires_at, p_auto_renewing)
  on conflict (purchase_token) do update
    set product_id = excluded.product_id, base_plan_id = excluded.base_plan_id, plan_tier = excluded.plan_tier,
        state = case when public.play_subscriptions.state = 'VOIDED' then 'VOIDED' else excluded.state end,
        expires_at = case when public.play_subscriptions.state = 'VOIDED' then public.play_subscriptions.expires_at else excluded.expires_at end,
        auto_renewing = excluded.auto_renewing, updated_at = now();

  perform public.refresh_profile_plan(p_user_id);
end;
$$;

-- O perfil recebe a melhor assinatura ativa da conta (Pro vence Essencial; depois a que vence mais tarde).
create or replace function public.refresh_profile_plan(p_user_id uuid)
returns void
language plpgsql
security definer
set search_path = public
as $$
declare
  v_best record;
begin
  select plan_tier, expires_at into v_best
  from public.play_subscriptions
  where user_id = p_user_id and state in ('ACTIVE', 'IN_GRACE_PERIOD') and expires_at > now()
  order by case plan_tier when 'PRO' then 2 else 1 end desc, expires_at desc
  limit 1;

  if v_best.plan_tier is not null then
    update public.profiles set plan_tier = v_best.plan_tier, plan_renews_at = v_best.expires_at
    where user_id = p_user_id and plan_tier <> 'TESTE';
  else
    update public.profiles set plan_tier = 'FREE', plan_renews_at = null
    where user_id = p_user_id and plan_tier in ('ESSENCIAL', 'PRO');
  end if;
end;
$$;

-- Reembolso avisado pelo Google: a assinatura vira VOIDED e o plano cai na hora.
create or replace function public.void_play_subscription(p_purchase_token text)
returns void
language plpgsql
security definer
set search_path = public
as $$
declare
  v_user uuid;
begin
  update public.play_subscriptions
  set state = 'VOIDED', expires_at = least(coalesce(expires_at, now()), now()), auto_renewing = false, updated_at = now()
  where purchase_token = p_purchase_token
  returning user_id into v_user;
  if v_user is not null then
    perform public.refresh_profile_plan(v_user);
  end if;
end;
$$;

revoke all on function public.apply_play_subscription(uuid, text, text, text, text, text, timestamptz, boolean) from public, anon, authenticated;
grant execute on function public.apply_play_subscription(uuid, text, text, text, text, text, timestamptz, boolean) to service_role;
revoke all on function public.refresh_profile_plan(uuid) from public, anon, authenticated;
grant execute on function public.refresh_profile_plan(uuid) to service_role;
revoke all on function public.void_play_subscription(text) from public, anon, authenticated;
grant execute on function public.void_play_subscription(text) to service_role;

select cron.schedule('expire-play-subscriptions', '23 9 * * *', 'select public.expire_play_subscriptions()');

-- ------------------------------------------------------------------ 7. cancelamento
create or replace function public.release_ai_job_reservation(
  p_job_id uuid,
  p_terminal_status public.ai_job_status default 'FAILED',
  p_error_code text default null,
  p_error_message text default null
)
returns public.ai_jobs
language plpgsql
security definer
set search_path to ''
as $function$
declare
  v_job public.ai_jobs;
  v_reservation public.ai_quota_reservations;
  v_usage public.ai_quota_usage;
  v_charged boolean;
begin
  if p_terminal_status not in ('FAILED', 'EXPIRED', 'CANCELLED') then
    raise exception using errcode = '22023', message = 'INVALID_RELEASE_STATUS';
  end if;
  select * into v_job from public.ai_jobs where id = p_job_id for update;
  if not found then raise exception using errcode = 'P0001', message = 'AI_JOB_NOT_FOUND'; end if;
  perform public.ai_assert_job_owner(v_job.user_id);
  if v_job.status = p_terminal_status then return v_job; end if;
  if v_job.status in ('SUCCEEDED', 'FAILED', 'EXPIRED', 'CANCELLED') then
    raise exception using errcode = 'P0001', message = 'TERMINAL_JOB_IMMUTABLE';
  end if;
  if v_job.provider_quarantined_at is not null then
    raise exception using errcode = 'P0001', message = 'CANCELLATION_RECONCILIATION_REQUIRED';
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
     and (v_job.provider_start_outcome <> 'NOT_STARTED' or v_job.openai_response_id is not null) then
    raise exception using errcode = 'P0001', message = 'CANCELLATION_PROVIDER_STARTED';
  end if;
  -- Cancelado pela pessoa depois que a IA começou: o custo já existiu, a cota conta como usada.
  -- Sem isso, começar e cancelar em seguida, sem parar, gastava IA sem gastar cota.
  v_charged := p_terminal_status = 'CANCELLED' and v_job.status = 'PROCESSING'
    and (v_job.openai_response_id is not null or v_job.provider_start_outcome <> 'NOT_STARTED');
  select * into v_reservation from public.ai_quota_reservations where job_id = v_job.id for update;
  if not found or v_reservation.status <> 'RESERVED' then
    raise exception using errcode = 'P0001', message = 'AI_RESERVATION_NOT_ACTIVE';
  end if;
  select * into v_usage from public.ai_quota_usage
    where user_id = v_reservation.user_id and feature = v_reservation.feature
      and period_start = v_reservation.period_start for update;
  if not found or v_usage.reserved_count <= 0 then
    raise exception using errcode = 'P0001', message = 'AI_QUOTA_RESERVATION_MISSING';
  end if;
  perform set_config('ai.internal_job_transition', '1', true);
  update public.ai_jobs set status = p_terminal_status, finished_at = now(), lease_owner = null,
    lease_expires_at = null, error_code = p_error_code, error_message = p_error_message
  where id = v_job.id returning * into v_job;
  perform set_config('ai.internal_job_transition', '0', true);
  if v_charged then
    update public.ai_quota_reservations set status = 'CONSUMED', consumed_at = now()
      where id = v_reservation.id;
    update public.ai_quota_usage set reserved_count = reserved_count - 1, successful_count = successful_count + 1
      where user_id = v_usage.user_id and feature = v_usage.feature and period_start = v_usage.period_start;
  else
    update public.ai_quota_reservations set status = 'RELEASED', released_at = now()
      where id = v_reservation.id;
    update public.ai_quota_usage set reserved_count = reserved_count - 1
      where user_id = v_usage.user_id and feature = v_usage.feature and period_start = v_usage.period_start;
  end if;
  return v_job;
end;
$function$;

revoke all on function public.release_ai_job_reservation(uuid, public.ai_job_status, text, text) from public, anon;
grant execute on function public.release_ai_job_reservation(uuid, public.ai_job_status, text, text) to authenticated, service_role;

-- ------------------------------------------------------------------ 8. teto diário da plataforma
-- Tokens de IA (entrada + saída) gastos nas últimas 24 horas, somando todas as contas. O worker
-- compara com o segredo AI_DAILY_TOKEN_BUDGET (sem ele, não há teto) antes de chamar a IA.
create or replace function public.ai_tokens_last_day()
returns bigint
language sql
stable
security definer
set search_path = public
as $$
  select coalesce(sum(coalesce(input_tokens, 0) + coalesce(output_tokens, 0)), 0)::bigint
  from public.ai_job_costs
  where created_at > now() - interval '1 day'
$$;
revoke all on function public.ai_tokens_last_day() from public, anon, authenticated;
grant execute on function public.ai_tokens_last_day() to service_role;
