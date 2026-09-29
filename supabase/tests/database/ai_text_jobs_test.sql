select no_plan();

-- Conteúdo e plano renovam por dia; o edital continua com cota única.
select is(public.ai_quota_period('PLAN_GENERATION'), (now() at time zone 'America/Sao_Paulo')::date, 'plan quota renews daily');
select is(public.ai_quota_period('CONTENT_GENERATION'), (now() at time zone 'America/Sao_Paulo')::date, 'content quota renews daily');
select is(public.ai_quota_period('SYLLABUS_GENERATION'), date '1970-01-01', 'syllabus quota does not renew');

select ok(
  not has_function_privilege('authenticated', 'public.claim_ai_text_worker_job(text, text, integer, integer)', 'EXECUTE'),
  'authenticated cannot claim text worker jobs'
);
select ok(
  has_function_privilege('service_role', 'public.claim_ai_text_worker_job(text, text, integer, integer)', 'EXECUTE'),
  'service_role can claim text worker jobs'
);
select ok(
  not has_function_privilege('authenticated', 'public.claim_ai_text_job_provider_reconciliation(text, text, integer, integer)', 'EXECUTE'),
  'authenticated cannot claim text reconciliation'
);

begin;

do $$
begin
  insert into auth.users (id, aud, role, email, created_at, updated_at)
  values ('00000000-0000-0000-0000-0000000000c1', 'authenticated', 'authenticated', 'ai-text-jobs@example.test', now(), now())
  on conflict (id) do nothing;
  insert into public.profiles (user_id, beta_access)
  values ('00000000-0000-0000-0000-0000000000c1', true)
  on conflict (user_id) do update set beta_access = true;
end;
$$;

set local role postgres;
-- Um job de conteúdo (sem PDF) e um de edital sem PDF, ambos reservados.
insert into public.ai_jobs (id, user_id, feature, status, idempotency_key, request_fingerprint, request_payload)
values
  ('00000000-0000-0000-0000-0000000000c2', '00000000-0000-0000-0000-0000000000c1', 'CONTENT_GENERATION', 'RESERVED',
   'text-content-key', 'text-content-fingerprint', '{"feature":"CONTENT_GENERATION","input":{}}'::jsonb),
  ('00000000-0000-0000-0000-0000000000c3', '00000000-0000-0000-0000-0000000000c1', 'SYLLABUS_GENERATION', 'RESERVED',
   'text-syllabus-key', 'text-syllabus-fingerprint', '{}'::jsonb);
insert into public.ai_quota_usage (user_id, feature, period_start, reserved_count)
values
  ('00000000-0000-0000-0000-0000000000c1', 'CONTENT_GENERATION', (now() at time zone 'America/Sao_Paulo')::date, 1),
  ('00000000-0000-0000-0000-0000000000c1', 'SYLLABUS_GENERATION', date '1970-01-01', 1);
insert into public.ai_quota_reservations (job_id, user_id, feature, period_start)
values
  ('00000000-0000-0000-0000-0000000000c2', '00000000-0000-0000-0000-0000000000c1', 'CONTENT_GENERATION', (now() at time zone 'America/Sao_Paulo')::date),
  ('00000000-0000-0000-0000-0000000000c3', '00000000-0000-0000-0000-0000000000c1', 'SYLLABUS_GENERATION', date '1970-01-01');

set local role service_role;
select set_config('request.jwt.claim.role', 'service_role', true);

select is(
  (select status::text from public.claim_ai_job('00000000-0000-0000-0000-0000000000c2'::uuid, 'edge-text', 60)),
  'PROCESSING', 'content job starts processing without a PDF'
);
select throws_ok(
  $$select * from public.claim_ai_job('00000000-0000-0000-0000-0000000000c3'::uuid, 'edge-syllabus', 60)$$,
  'P0001', 'SOURCE_NOT_BOUND', 'syllabus job still requires a bound PDF'
);

-- Cada worker só enxerga a própria fila.
select ok(
  (select id from public.claim_ai_syllabus_worker_job('syllabus-worker', 'token-s', 60, 900)) is null,
  'syllabus worker does not claim the content job'
);
select is(
  (select id from public.claim_ai_text_worker_job('text-worker', 'token-t', 60, 900)),
  '00000000-0000-0000-0000-0000000000c2'::uuid, 'text worker claims the content job'
);

-- Um cancelamento pendente de job de texto só aparece para a reconciliação de texto.
set local role postgres;
update public.ai_jobs
set cancellation_requested_at = now(), openai_response_id = 'resp_text', provider_start_outcome = 'ACCEPTED',
    provider_execution_started_at = now(), lease_token = null, lease_expires_at = null
where id = '00000000-0000-0000-0000-0000000000c2';
insert into public.ai_job_provider_reconciliation_queue (job_id) values ('00000000-0000-0000-0000-0000000000c2')
on conflict (job_id) do nothing;

set local role service_role;
select ok(
  (select id from public.claim_ai_job_provider_reconciliation('syllabus-worker', 'token-r1', 60, 900)) is null,
  'syllabus reconciliation ignores text jobs'
);
select is(
  (select id from public.claim_ai_text_job_provider_reconciliation('text-worker', 'token-r2', 60, 900)),
  '00000000-0000-0000-0000-0000000000c2'::uuid, 'text reconciliation claims the text job'
);

select * from finish();
rollback;
