select no_plan();
begin;

select ok(has_function_privilege('service_role', 'public.resolve_ai_job_provider_quarantine(uuid, uuid, text, jsonb, text, text)', 'EXECUTE'), 'service_role can invoke administrative resolution');
select ok(not has_function_privilege('anon', 'public.resolve_ai_job_provider_quarantine(uuid, uuid, text, jsonb, text, text)', 'EXECUTE') and not has_function_privilege('authenticated', 'public.resolve_ai_job_provider_quarantine(uuid, uuid, text, jsonb, text, text)', 'EXECUTE') and not exists (select 1 from pg_proc p cross join lateral aclexplode(coalesce(p.proacl, acldefault('f', p.proowner))) a where p.oid = 'public.resolve_ai_job_provider_quarantine(uuid, uuid, text, jsonb, text, text)'::regprocedure and a.grantee = 0 and a.privilege_type = 'EXECUTE'), 'administrative resolution is closed to all client/public roles');
select ok(not has_table_privilege('anon', 'public.ai_job_provider_resolution_audit', 'SELECT') and not has_table_privilege('authenticated', 'public.ai_job_provider_resolution_audit', 'SELECT') and not has_table_privilege('service_role', 'public.ai_job_provider_resolution_audit', 'SELECT'), 'resolution audit has no direct runtime table read');

insert into auth.users (id, aud, role, email, created_at, updated_at)
select ('33000000-0000-0000-0000-' || lpad(n::text, 12, '0'))::uuid, 'authenticated', 'authenticated', 'resolution-' || n || '@example.test', now(), now()
from generate_series(1, 11) n
on conflict (id) do nothing;
insert into public.profiles (user_id, beta_access)
select ('33000000-0000-0000-0000-' || lpad(n::text, 12, '0'))::uuid, true from generate_series(1, 11) n
on conflict (user_id) do update set beta_access = true;
update public.ai_feature_flags set enabled = true;

create temporary table resolution_jobs (label text primary key, user_id uuid not null, job_id uuid not null);
create temporary table resolution_results (label text primary key, result jsonb not null);
grant all on resolution_jobs to authenticated, service_role;
grant all on resolution_results to service_role;

set local role authenticated;
select set_config('request.jwt.claim.role', 'authenticated', false);
select set_config('request.jwt.claim.sub', '33000000-0000-0000-0000-000000000001', false);
insert into resolution_jobs select 'not-created-cancel', '33000000-0000-0000-0000-000000000001', job_id
from public.create_or_get_ai_job_and_reserve_quota('SYLLABUS_GENERATION', 'resolution-1', 'resolution-1', '{}'::jsonb);
select set_config('request.jwt.claim.sub', '33000000-0000-0000-0000-000000000002', false);
insert into resolution_jobs select 'not-created-fail', '33000000-0000-0000-0000-000000000002', job_id
from public.create_or_get_ai_job_and_reserve_quota('SYLLABUS_GENERATION', 'resolution-2', 'resolution-2', '{}'::jsonb);
select set_config('request.jwt.claim.sub', '33000000-0000-0000-0000-000000000003', false);
insert into resolution_jobs select 'attach', '33000000-0000-0000-0000-000000000003', job_id
from public.create_or_get_ai_job_and_reserve_quota('SYLLABUS_GENERATION', 'resolution-3', 'resolution-3', '{}'::jsonb);
select set_config('request.jwt.claim.sub', '33000000-0000-0000-0000-000000000004', false);
insert into resolution_jobs select 'attach-cancel', '33000000-0000-0000-0000-000000000004', job_id
from public.create_or_get_ai_job_and_reserve_quota('SYLLABUS_GENERATION', 'resolution-4', 'resolution-4', '{}'::jsonb);
select set_config('request.jwt.claim.sub', '33000000-0000-0000-0000-000000000005', false);
insert into resolution_jobs select 'terminal-failed', '33000000-0000-0000-0000-000000000005', job_id
from public.create_or_get_ai_job_and_reserve_quota('SYLLABUS_GENERATION', 'resolution-5', 'resolution-5', '{}'::jsonb);
select set_config('request.jwt.claim.sub', '33000000-0000-0000-0000-000000000006', false);
insert into resolution_jobs select 'terminal-cancelled-active-lease', '33000000-0000-0000-0000-000000000006', job_id
from public.create_or_get_ai_job_and_reserve_quota('SYLLABUS_GENERATION', 'resolution-6', 'resolution-6', '{}'::jsonb);
select set_config('request.jwt.claim.sub', '33000000-0000-0000-0000-000000000007', false);
insert into resolution_jobs select 'not-created-live-lease', '33000000-0000-0000-0000-000000000007', job_id
from public.create_or_get_ai_job_and_reserve_quota('SYLLABUS_GENERATION', 'resolution-7', 'resolution-7', '{}'::jsonb);
select set_config('request.jwt.claim.sub', '33000000-0000-0000-0000-000000000008', false);
insert into resolution_jobs select 'attach-same-worker-id', '33000000-0000-0000-0000-000000000008', job_id
from public.create_or_get_ai_job_and_reserve_quota('SYLLABUS_GENERATION', 'resolution-8', 'resolution-8', '{}'::jsonb);
select set_config('request.jwt.claim.sub', '33000000-0000-0000-0000-000000000009', false);
insert into resolution_jobs select 'terminal-expired', '33000000-0000-0000-0000-000000000009', job_id
from public.create_or_get_ai_job_and_reserve_quota('SYLLABUS_GENERATION', 'resolution-9', 'resolution-9', '{}'::jsonb);
select set_config('request.jwt.claim.sub', '33000000-0000-0000-0000-000000000010', false);
insert into resolution_jobs select 'terminal-incomplete', '33000000-0000-0000-0000-000000000010', job_id
from public.create_or_get_ai_job_and_reserve_quota('SYLLABUS_GENERATION', 'resolution-10', 'resolution-10', '{}'::jsonb);
select set_config('request.jwt.claim.sub', '33000000-0000-0000-0000-000000000011', false);
insert into resolution_jobs select 'attach-conflict', '33000000-0000-0000-0000-000000000011', job_id
from public.create_or_get_ai_job_and_reserve_quota('SYLLABUS_GENERATION', 'resolution-11', 'resolution-11', '{}'::jsonb);
reset role;

set role postgres;
update public.ai_jobs j set source_object_path = j.user_id::text || '/resolution.pdf', source_hash = repeat('a', 64), source_bytes = 10, source_pages = 1, source_file_count = 1
from resolution_jobs r where j.id = r.job_id;
set request.jwt.claim.role = 'service_role';
set role service_role;
select public.claim_ai_job(job_id, 'resolution-worker', 300) from resolution_jobs order by label;
reset role;

update public.ai_jobs j set provider_execution_started_at = now(), provider_start_outcome = 'TRANSPORT_AMBIGUOUS', provider_quarantined_at = now(), processing_deadline_at = now() - interval '2 hours', cancellation_requested_at = case when r.label in ('not-created-cancel', 'attach-cancel') then now() else null end,
  lease_owner = case when r.label = 'not-created-live-lease' then 'live-generation' when r.label in ('terminal-cancelled-active-lease', 'attach-same-worker-id') then 'worker-race' else null end,
  lease_token = case when r.label = 'not-created-live-lease' then 'live-generation-token' when r.label in ('terminal-cancelled-active-lease', 'attach-same-worker-id') then 'worker-race-token' else null end,
  lease_expires_at = case when r.label in ('terminal-cancelled-active-lease', 'not-created-live-lease', 'attach-same-worker-id') then now() + interval '1 hour' else now() - interval '1 second' end
from resolution_jobs r where j.id = r.job_id and r.label in ('not-created-cancel', 'not-created-fail', 'attach', 'attach-cancel', 'not-created-live-lease');
update public.ai_jobs j set provider_execution_started_at = now(), provider_start_outcome = 'ACCEPTED', provider_quarantined_at = null, openai_response_id = 'resp-admin-' || r.label,
  cancellation_requested_at = case when r.label = 'terminal-cancelled-active-lease' then now() else null end,
  provider_result_recoverable = true,
  processing_deadline_at = case when r.label = 'attach-same-worker-id' then now() + interval '10 minutes' else processing_deadline_at end,
  lease_owner = case when r.label in ('terminal-cancelled-active-lease', 'attach-same-worker-id') then j.lease_owner else null end,
  lease_token = case when r.label in ('terminal-cancelled-active-lease', 'attach-same-worker-id') then j.lease_token else null end,
  lease_expires_at = case when r.label in ('terminal-cancelled-active-lease', 'attach-same-worker-id') then j.lease_expires_at else now() - interval '1 second' end
from resolution_jobs r where j.id = r.job_id and r.label in ('terminal-failed', 'terminal-cancelled-active-lease', 'terminal-expired', 'terminal-incomplete', 'attach-same-worker-id', 'attach-conflict');
insert into public.ai_job_provider_reconciliation_queue (job_id, lease_owner, lease_token, lease_generation, lease_expires_at)
select job_id, 'reconcile-worker', 'reconcile-token', 1, now() + interval '5 minutes'
from resolution_jobs where label = 'terminal-cancelled-active-lease';

set request.jwt.claim.role = 'service_role';
set role service_role;

insert into resolution_results values ('not-created-cancel', public.resolve_ai_job_provider_quarantine(
  (select job_id from resolution_jobs where label = 'not-created-cancel'),
  '44000000-0000-0000-0000-000000000001', 'CONFIRM_NOT_CREATED', '{}'::jsonb, 'operator:one', 'evidence:case-1'));
select is((select result ->> 'status' from resolution_results where label = 'not-created-cancel'), 'CANCELLED', 'CONFIRM_NOT_CREATED maps cancellation-requested job to CANCELLED');
select is((select provider_start_outcome from public.ai_jobs where id = (select job_id from resolution_jobs where label = 'not-created-cancel')), 'TRANSPORT_AMBIGUOUS', 'administrative absence confirmation does not mislabel an uncertain send as NOT_SENT');
select is((select status from public.ai_quota_reservations where job_id = (select job_id from resolution_jobs where label = 'not-created-cancel')), 'RELEASED', 'CONFIRM_NOT_CREATED releases the reservation atomically');
select is((select reserved_count from public.ai_quota_usage where user_id = '33000000-0000-0000-0000-000000000001' and feature = 'SYLLABUS_GENERATION'), 0, 'CONFIRM_NOT_CREATED decrements reserved quota once');
set role postgres;
select ok((select technical_actor = 'service_role' and evidence_reference = 'evidence:case-1' and operator_reference = 'operator:one' and result_status = 'CANCELLED' and created_at <= now() and result_snapshot ->> 'status' = 'CANCELLED' from public.ai_job_provider_resolution_audit where resolution_id = '44000000-0000-0000-0000-000000000001'), 'audit captures backend identity, references, original result, and server timestamp');
set role service_role;
select is(public.resolve_ai_job_provider_quarantine(
  (select job_id from resolution_jobs where label = 'not-created-cancel'),
  '44000000-0000-0000-0000-000000000001', 'CONFIRM_NOT_CREATED', '{}'::jsonb, 'operator:one', 'evidence:case-1'),
  (select result from resolution_results where label = 'not-created-cancel'), 'identical resolution replay returns the original result snapshot');
select throws_ok($$select public.resolve_ai_job_provider_quarantine((select job_id from resolution_jobs where label = 'not-created-cancel'), '44000000-0000-0000-0000-000000000001', 'CONFIRM_NOT_CREATED', '{}'::jsonb, 'operator:one', 'evidence:changed')$$, 'P0001', 'AI_RESOLUTION_ID_CONFLICT', 'same global resolution ID with a divergent fingerprint conflicts');
select throws_ok($$select public.resolve_ai_job_provider_quarantine((select job_id from resolution_jobs where label = 'not-created-fail'), '44000000-0000-0000-0000-000000000001', 'CONFIRM_NOT_CREATED', '{}'::jsonb, 'operator:one', 'evidence:case-1')$$, 'P0001', 'AI_RESOLUTION_ID_CONFLICT', 'resolution IDs are globally unique across jobs');
set role postgres;
select is((select count(*) from public.ai_job_provider_resolution_audit where resolution_id = '44000000-0000-0000-0000-000000000001'), 1::bigint, 'idempotent replay never duplicates the audit row');
set role service_role;

select throws_ok($$select public.resolve_ai_job_provider_quarantine((select job_id from resolution_jobs where label = 'not-created-fail'), '44000000-0000-0000-0000-000000000002', 'CONFIRM_NOT_CREATED', '{}'::jsonb, repeat('x', 129), 'evidence:case-2')$$, 'P0001', 'INVALID_OPERATOR_REFERENCE', 'operator reference length is bounded');
select throws_ok($$select public.resolve_ai_job_provider_quarantine((select job_id from resolution_jobs where label = 'not-created-fail'), '44000000-0000-0000-0000-000000000002', 'CONFIRM_NOT_CREATED', '{}'::jsonb, 'operator:two', repeat('e', 257))$$, 'P0001', 'INVALID_EVIDENCE_REFERENCE', 'evidence reference length is bounded');
select throws_ok($$select public.resolve_ai_job_provider_quarantine((select job_id from resolution_jobs where label = 'not-created-fail'), '44000000-0000-0000-0000-000000000002', 'CONFIRM_NOT_CREATED', '{}'::jsonb, 'operator:two', 'https://example.test/secret')$$, 'P0001', 'INVALID_EVIDENCE_REFERENCE', 'evidence reference does not accept arbitrary URLs');
select throws_ok($$select public.resolve_ai_job_provider_quarantine((select job_id from resolution_jobs where label = 'not-created-fail'), '44000000-0000-0000-0000-000000000002', 'CONFIRM_NOT_CREATED', '{}'::jsonb, 'operator:two', null)$$, 'P0001', 'AI_RESOLUTION_EVIDENCE_REQUIRED', 'CONFIRM_NOT_CREATED requires an auditable evidence reference');
select throws_ok($$select public.resolve_ai_job_provider_quarantine((select job_id from resolution_jobs where label = 'not-created-fail'), '44000000-0000-0000-0000-000000000002', 'CONFIRM_NOT_CREATED', '{"secret":"unexpected"}'::jsonb, 'operator:two', 'evidence:case-2')$$, '22023', 'INVALID_AI_PROVIDER_RESOLUTION_PAYLOAD', 'decision payload rejects fields outside the normalized contract');
select is((public.resolve_ai_job_provider_quarantine((select job_id from resolution_jobs where label = 'not-created-fail'), '44000000-0000-0000-0000-000000000002', 'CONFIRM_NOT_CREATED', '{}'::jsonb, 'operator:two', 'evidence:case-2') ->> 'status'), 'FAILED', 'CONFIRM_NOT_CREATED without cancellation maps to FAILED');
select is((select status from public.ai_quota_reservations where job_id = (select job_id from resolution_jobs where label = 'not-created-fail')), 'RELEASED', 'non-cancelled absence confirmation releases its reservation');

select is((public.resolve_ai_job_provider_quarantine((select job_id from resolution_jobs where label = 'attach'), '44000000-0000-0000-0000-000000000003', 'ATTACH_RESPONSE_ID', '{"response_id":"resp-recovered-3"}'::jsonb, 'operator:three', 'evidence:recovery-3') ->> 'status'), 'PROCESSING', 'ATTACH_RESPONSE_ID preserves processing state');
select ok((select provider_start_outcome = 'ACCEPTED' and openai_response_id = 'resp-recovered-3' and provider_quarantined_at is null and processing_deadline_at is null from public.ai_jobs where id = (select job_id from resolution_jobs where label = 'attach')), 'attach persists ID, clears quarantine, and resets stale deadline');
select is((select status from public.ai_quota_reservations where job_id = (select job_id from resolution_jobs where label = 'attach')), 'RESERVED', 'attach does not release or consume quota');
select is((select reserved_count from public.ai_quota_usage where user_id = '33000000-0000-0000-0000-000000000003' and feature = 'SYLLABUS_GENERATION'), 1, 'attach preserves the quota reservation counter');

select is((public.resolve_ai_job_provider_quarantine((select job_id from resolution_jobs where label = 'attach-cancel'), '44000000-0000-0000-0000-000000000004', 'ATTACH_RESPONSE_ID', '{"response_id":"resp-recovered-4"}'::jsonb, 'operator:four', 'evidence:recovery-4') ->> 'status'), 'PROCESSING', 'attach with cancellation request stays pending for backend reconciliation');
set role postgres;
select is((select count(*) from public.ai_job_provider_reconciliation_queue where job_id = (select job_id from resolution_jobs where label = 'attach-cancel')), 1::bigint, 'attach atomically creates durable reconciliation continuation');
set role service_role;
select is((select status from public.ai_quota_reservations where job_id = (select job_id from resolution_jobs where label = 'attach-cancel')), 'RESERVED', 'pending attach reconciliation retains quota');

select is((public.resolve_ai_job_provider_quarantine((select job_id from resolution_jobs where label = 'attach-same-worker-id'), '44000000-0000-0000-0000-000000000008', 'ATTACH_RESPONSE_ID', '{"response_id":"resp-admin-attach-same-worker-id"}'::jsonb, 'operator:eight', null) ->> 'status'), 'PROCESSING', 'ATTACH converges when worker already persisted the same response ID');
select ok((select processing_deadline_at > now() and provider_start_outcome = 'ACCEPTED' and openai_response_id = 'resp-admin-attach-same-worker-id' from public.ai_jobs where id = (select job_id from resolution_jobs where label = 'attach-same-worker-id')), 'same-ID worker race preserves the worker-owned deadline');
select throws_ok($$select public.resolve_ai_job_provider_quarantine((select job_id from resolution_jobs where label = 'attach-same-worker-id'), '44000000-0000-0000-0000-000000000012', 'CONFIRM_TERMINAL_NO_RESULT', '{"response_id":"resp-different","provider_status":"cancelled"}'::jsonb, 'operator:eight', 'evidence:provider-12')$$, 'P0001', 'PROVIDER_RESPONSE_ID_CONFLICT', 'terminal exception cannot resolve a different response ID');
select throws_ok($$select public.resolve_ai_job_provider_quarantine((select job_id from resolution_jobs where label = 'attach-conflict'), '44000000-0000-0000-0000-000000000011', 'ATTACH_RESPONSE_ID', '{"response_id":"resp-first"}'::jsonb, 'operator:eleven', null)$$, 'P0001', 'PROVIDER_RESPONSE_ID_CONFLICT', 'divergent attached response ID cannot overwrite provider evidence');
select ok((select openai_response_id = 'resp-admin-attach-conflict' and provider_quarantined_at is null from public.ai_jobs where id = (select job_id from resolution_jobs where label = 'attach-conflict')), 'conflicting attach leaves the worker-accepted response ID unchanged');

select is((public.resolve_ai_job_provider_quarantine((select job_id from resolution_jobs where label = 'terminal-failed'), '44000000-0000-0000-0000-000000000005', 'CONFIRM_TERMINAL_NO_RESULT', jsonb_build_object('response_id', 'resp-admin-terminal-failed', 'provider_status', 'failed'), 'operator:five', 'evidence:provider-5') ->> 'status'), 'FAILED', 'provider failed maps to FAILED');
select is((select error_code from public.ai_jobs where id = (select job_id from resolution_jobs where label = 'terminal-failed')), 'PROVIDER_RESULT_UNAVAILABLE', 'provider failed retains stable internal error code');
select is((select status from public.ai_quota_reservations where job_id = (select job_id from resolution_jobs where label = 'terminal-failed')), 'RELEASED', 'provider terminal result releases quota once');

select is((public.resolve_ai_job_provider_quarantine((select job_id from resolution_jobs where label = 'terminal-cancelled-active-lease'), '44000000-0000-0000-0000-000000000006', 'CONFIRM_TERMINAL_NO_RESULT', jsonb_build_object('response_id', 'resp-admin-terminal-cancelled-active-lease', 'provider_status', 'cancelled'), 'operator:six', 'evidence:provider-6') ->> 'status'), 'CANCELLED', 'same-ID provider terminal evidence is the only destructive active-lease exception');
select is((select lease_token from public.ai_jobs where id = (select job_id from resolution_jobs where label = 'terminal-cancelled-active-lease')), null, 'terminal provider resolution revokes the generation lease');
select is((select status from public.ai_quota_reservations where job_id = (select job_id from resolution_jobs where label = 'terminal-cancelled-active-lease')), 'RELEASED', 'same-ID terminal exception releases quota atomically');

select is((public.resolve_ai_job_provider_quarantine((select job_id from resolution_jobs where label = 'terminal-expired'), '44000000-0000-0000-0000-000000000009', 'CONFIRM_TERMINAL_NO_RESULT', jsonb_build_object('response_id', 'resp-admin-terminal-expired', 'provider_status', 'expired'), 'operator:nine', 'evidence:provider-9') ->> 'status'), 'EXPIRED', 'provider expired maps exactly to EXPIRED');
select is((public.resolve_ai_job_provider_quarantine((select job_id from resolution_jobs where label = 'terminal-incomplete'), '44000000-0000-0000-0000-000000000010', 'CONFIRM_TERMINAL_NO_RESULT', jsonb_build_object('response_id', 'resp-admin-terminal-incomplete', 'provider_status', 'incomplete'), 'operator:ten', 'evidence:provider-10') ->> 'status'), 'FAILED', 'provider incomplete maps to FAILED');
select is((select error_code from public.ai_jobs where id = (select job_id from resolution_jobs where label = 'terminal-incomplete')), 'PROVIDER_INCOMPLETE', 'incomplete provider result receives its stable error code');

select throws_ok($$select public.resolve_ai_job_provider_quarantine((select job_id from resolution_jobs where label = 'not-created-live-lease'), '44000000-0000-0000-0000-000000000007', 'CONFIRM_NOT_CREATED', '{}'::jsonb, 'operator:seven', 'evidence:case-7')$$, 'P0001', 'AI_PROVIDER_RESOLUTION_PENDING', 'CONFIRM_NOT_CREATED cannot release quota under a live generation lease');
select ok((select status = 'PROCESSING' and provider_quarantined_at is not null and lease_expires_at > now() from public.ai_jobs where id = (select job_id from resolution_jobs where label = 'not-created-live-lease')), 'live-lease rejection leaves job unchanged');
select is((select status from public.ai_quota_reservations where job_id = (select job_id from resolution_jobs where label = 'not-created-live-lease')), 'RESERVED', 'live-lease rejection leaves reservation unchanged');
set role postgres;
select is((select count(*) from public.ai_job_provider_resolution_audit where job_id = (select job_id from resolution_jobs where label = 'not-created-live-lease')), 0::bigint, 'live-lease rejection writes no resolution audit');

select is((select count(*) from public.ai_job_provider_resolution_audit), 9::bigint, 'only committed unique decisions are audited');
reset role;

select * from finish();
rollback;
