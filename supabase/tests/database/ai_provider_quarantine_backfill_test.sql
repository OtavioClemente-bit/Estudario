begin;
select no_plan();

insert into auth.users (id, aud, role, email, created_at, updated_at)
values ('10000000-0000-0000-0000-000000000001', 'authenticated', 'authenticated', 'provider-migration@example.test', now(), now());
insert into public.profiles (user_id, beta_access)
values ('10000000-0000-0000-0000-000000000001', true);
update public.ai_feature_flags set enabled = true;

-- Recreate pre-migration rows inside this rolled-back pgTAP transaction.
-- The production migration validates these same checks after classification.
create temporary table provider_checks as
select conname, pg_get_constraintdef(oid) as definition
from pg_constraint
where conrelid = 'public.ai_jobs'::regclass
  and conname in ('ai_jobs_provider_start_outcome_check', 'ai_jobs_provider_state_check', 'ai_jobs_provider_recoverable_id_check');
do $$
declare v_check record;
begin
  for v_check in select conname from provider_checks loop
    execute format('alter table public.ai_jobs drop constraint %I', v_check.conname);
  end loop;
end;
$$;

-- These rows have the old schema's evidence and exercise the migration's
-- idempotent classifier without relying on any cloud job.
insert into public.ai_jobs (
  id, user_id, feature, status, idempotency_key, request_fingerprint,
  source_object_path, source_hash, openai_response_id,
  provider_execution_started_at, provider_result_recoverable, updated_at
)
values
  ('10000000-0000-0000-0000-000000000002', '10000000-0000-0000-0000-000000000001', 'PLAN_GENERATION', 'RESERVED', 'legacy-unstarted', 'legacy-unstarted', null, null, null, null, true, '2025-01-01T00:00:00Z'),
  ('10000000-0000-0000-0000-000000000003', '10000000-0000-0000-0000-000000000001', 'PLAN_GENERATION', 'PROCESSING', 'legacy-accepted', 'legacy-accepted', 'legacy/accepted.pdf', repeat('a', 64), 'resp-legacy', now() - interval '2 hours', true, '2025-01-01T00:00:00Z'),
  ('ca9588a7-ee0e-45bb-9fb8-5b602ef1d3d9', '10000000-0000-0000-0000-000000000001', 'PLAN_GENERATION', 'PROCESSING', 'reference-local-only', 'reference-local-only', 'legacy/reference.pdf', repeat('b', 64), null, now() - interval '2 hours', true, '2025-01-01T00:00:00Z'),
  ('10000000-0000-0000-0000-000000000004', '10000000-0000-0000-0000-000000000001', 'PLAN_GENERATION', 'FAILED', 'legacy-terminal', 'legacy-terminal', 'legacy/terminal.pdf', repeat('c', 64), null, now() - interval '2 hours', true, '2025-01-01T00:00:00Z');

insert into public.ai_quota_usage (user_id, feature, period_start, reserved_count)
values ('10000000-0000-0000-0000-000000000001', 'PLAN_GENERATION', current_date, 3);
insert into public.ai_quota_reservations (job_id, user_id, feature, period_start, status, released_at)
values
  ('10000000-0000-0000-0000-000000000002', '10000000-0000-0000-0000-000000000001', 'PLAN_GENERATION', current_date, 'RESERVED', null),
  ('10000000-0000-0000-0000-000000000003', '10000000-0000-0000-0000-000000000001', 'PLAN_GENERATION', current_date, 'RESERVED', null),
  ('ca9588a7-ee0e-45bb-9fb8-5b602ef1d3d9', '10000000-0000-0000-0000-000000000001', 'PLAN_GENERATION', current_date, 'RESERVED', null),
  ('10000000-0000-0000-0000-000000000004', '10000000-0000-0000-0000-000000000001', 'PLAN_GENERATION', current_date, 'RELEASED', now() - interval '1 hour');

create temporary table provider_before as
select j.id, j.status, j.openai_response_id, j.provider_reconciled_at, j.updated_at,
       r.status as reservation_status, r.consumed_at, r.released_at
from public.ai_jobs j join public.ai_quota_reservations r on r.job_id = j.id
where j.user_id = '10000000-0000-0000-0000-000000000001';

select public.ai_backfill_provider_start_outcome();
select is((select provider_start_outcome from public.ai_jobs where id = '10000000-0000-0000-0000-000000000002'), 'NOT_STARTED', 'unstarted legacy row remains NOT_STARTED');
select is((select provider_start_outcome from public.ai_jobs where id = '10000000-0000-0000-0000-000000000003'), 'ACCEPTED', 'persisted response ID takes precedence');
select is((select provider_start_outcome from public.ai_jobs where id = 'ca9588a7-ee0e-45bb-9fb8-5b602ef1d3d9'), 'LEGACY_AMBIGUOUS', 'reference fixture is ambiguous');
select ok((select provider_quarantined_at is not null from public.ai_jobs where id = 'ca9588a7-ee0e-45bb-9fb8-5b602ef1d3d9'), 'reference fixture is quarantined');
select is((select provider_start_outcome from public.ai_jobs where id = '10000000-0000-0000-0000-000000000004'), 'LEGACY_AMBIGUOUS', 'terminal started row retains historical ambiguity');
select ok((select provider_quarantined_at is null from public.ai_jobs where id = '10000000-0000-0000-0000-000000000004'), 'terminal legacy row has no active quarantine');
select is((select count(*) from public.ai_jobs where user_id = '10000000-0000-0000-0000-000000000001' and provider_result_recoverable is not null), 1::bigint, 'only accepted ID retains recoverability evidence');
select is((select status::text from public.ai_jobs where id = 'ca9588a7-ee0e-45bb-9fb8-5b602ef1d3d9'), 'PROCESSING', 'reference status is unchanged');
select is((select status::text from public.ai_quota_reservations where job_id = 'ca9588a7-ee0e-45bb-9fb8-5b602ef1d3d9'), 'RESERVED', 'reference reservation remains RESERVED');
select ok((select openai_response_id is null and provider_result_recoverable is null and provider_reconciled_at is null from public.ai_jobs where id = 'ca9588a7-ee0e-45bb-9fb8-5b602ef1d3d9'), 'reference has no invented ID or reconciliation');
select is((select reserved_count from public.ai_quota_usage where user_id = '10000000-0000-0000-0000-000000000001' and feature = 'PLAN_GENERATION'), 3, 'backfill does not mutate quota usage');
select is((select count(*) from provider_before b join public.ai_jobs j using (id) join public.ai_quota_reservations r on r.job_id = j.id where b.status = j.status and b.openai_response_id is not distinct from j.openai_response_id and b.provider_reconciled_at is not distinct from j.provider_reconciled_at and b.updated_at = j.updated_at and b.reservation_status = r.status and b.consumed_at is not distinct from r.consumed_at and b.released_at is not distinct from r.released_at), 4::bigint, 'backfill preserves status, timestamps, reservation, and reconciliation evidence');

create temporary table first_backfill as select id, provider_start_outcome, provider_quarantined_at from public.ai_jobs where user_id = '10000000-0000-0000-0000-000000000001';
select public.ai_backfill_provider_start_outcome();
select is((select count(*) from first_backfill b join public.ai_jobs j using (id) where b.provider_start_outcome = j.provider_start_outcome and b.provider_quarantined_at is not distinct from j.provider_quarantined_at), 4::bigint, 'backfill replay is idempotent');

select * from finish();
rollback;
