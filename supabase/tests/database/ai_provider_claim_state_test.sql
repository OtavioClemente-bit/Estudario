begin;
select no_plan();

insert into auth.users (id, aud, role, email, created_at, updated_at)
values ('31000000-0000-0000-0000-000000000001', 'authenticated', 'authenticated', 'provider-claim@example.test', now(), now());
insert into public.profiles (user_id, beta_access)
values ('31000000-0000-0000-0000-000000000001', true);

insert into public.ai_jobs (
  id, user_id, feature, status, idempotency_key, request_fingerprint,
  source_object_path, source_hash, cancellation_requested_at, provider_start_outcome, provider_quarantined_at, created_at
)
values
  ('31000000-0000-0000-0000-000000000011', '31000000-0000-0000-0000-000000000001', 'SYLLABUS_GENERATION', 'RESERVED', 'claim-valid', 'claim-valid', '31000000-0000-0000-0000-000000000001/a.pdf', repeat('a', 64), null, 'NOT_STARTED', null, now() - interval '4 minutes'),
  ('31000000-0000-0000-0000-000000000012', '31000000-0000-0000-0000-000000000001', 'SYLLABUS_GENERATION', 'RESERVED', 'claim-cancelled', 'claim-cancelled', '31000000-0000-0000-0000-000000000001/b.pdf', repeat('b', 64), now(), 'NOT_STARTED', null, now() - interval '3 minutes'),
  ('31000000-0000-0000-0000-000000000013', '31000000-0000-0000-0000-000000000001', 'SYLLABUS_GENERATION', 'RESERVED', 'claim-quarantined', 'claim-quarantined', '31000000-0000-0000-0000-000000000001/c.pdf', repeat('c', 64), null, 'IN_FLIGHT', now(), now() - interval '2 minutes'),
  ('31000000-0000-0000-0000-000000000014', '31000000-0000-0000-0000-000000000001', 'SYLLABUS_GENERATION', 'FAILED', 'claim-terminal', 'claim-terminal', '31000000-0000-0000-0000-000000000001/d.pdf', repeat('d', 64), null, 'NOT_STARTED', null, now() - interval '1 minute');

select set_config('request.jwt.claim.role', 'authenticated', true);
select set_config('request.jwt.claim.sub', '31000000-0000-0000-0000-000000000001', true);
select is((select id from public.claim_ai_job('31000000-0000-0000-0000-000000000011', 'edge', 60)), '31000000-0000-0000-0000-000000000011'::uuid, 'legacy active claim admits only the valid owner job');
update public.ai_jobs set lease_token = 'edge-active-token', lease_expires_at = now() + interval '5 minutes'
where id = '31000000-0000-0000-0000-000000000011';
select throws_ok($$select * from public.claim_ai_job('31000000-0000-0000-0000-000000000012', 'edge', 60)$$, 'P0001', 'AI_JOB_NOT_RESERVABLE', 'legacy claim rejects cancellation-pending job');
select throws_ok($$select * from public.claim_ai_job('31000000-0000-0000-0000-000000000013', 'edge', 60)$$, 'P0001', 'AI_JOB_NOT_RESERVABLE', 'legacy claim rejects quarantined job');
select throws_ok($$select * from public.claim_ai_job('31000000-0000-0000-0000-000000000014', 'edge', 60)$$, 'P0001', 'AI_JOB_NOT_RESERVABLE', 'legacy claim rejects terminal job');

insert into public.ai_jobs (
  id, user_id, feature, status, idempotency_key, request_fingerprint,
  source_object_path, source_hash, cancellation_requested_at, provider_start_outcome, provider_quarantined_at, created_at
)
values
  ('31000000-0000-0000-0000-000000000021', '31000000-0000-0000-0000-000000000001', 'SYLLABUS_GENERATION', 'PROCESSING', 'worker-cancelled', 'worker-cancelled', '31000000-0000-0000-0000-000000000001/e.pdf', repeat('e', 64), now(), 'NOT_STARTED', null, now() - interval '4 minutes'),
  ('31000000-0000-0000-0000-000000000022', '31000000-0000-0000-0000-000000000001', 'SYLLABUS_GENERATION', 'PROCESSING', 'worker-quarantine', 'worker-quarantine', '31000000-0000-0000-0000-000000000001/f.pdf', repeat('f', 64), null, 'TRANSPORT_AMBIGUOUS', now(), now() - interval '3 minutes'),
  ('31000000-0000-0000-0000-000000000023', '31000000-0000-0000-0000-000000000001', 'SYLLABUS_GENERATION', 'FAILED', 'worker-terminal', 'worker-terminal', '31000000-0000-0000-0000-000000000001/g.pdf', repeat('a', 64), null, 'NOT_STARTED', null, now() - interval '2 minutes'),
  ('31000000-0000-0000-0000-000000000024', '31000000-0000-0000-0000-000000000001', 'SYLLABUS_GENERATION', 'PROCESSING', 'worker-valid', 'worker-valid', '31000000-0000-0000-0000-000000000001/h.pdf', repeat('b', 64), null, 'NOT_STARTED', null, now() - interval '1 minute');
select set_config('request.jwt.claim.role', 'service_role', true);
select is((select id from public.claim_ai_syllabus_worker_job('worker', 'claim-token', 60, 900)), '31000000-0000-0000-0000-000000000024'::uuid, 'worker claim skips cancelled and quarantined rows and selects eligible processing job');
select ok((select lease_token = 'claim-token' and lease_expires_at > now() and processing_deadline_at > now() from public.ai_jobs where id = '31000000-0000-0000-0000-000000000024'), 'eligible claim initializes its lease and bounded processing deadline');
select ok((select bool_and(lease_token is null and processing_deadline_at is null) from public.ai_jobs where id in ('31000000-0000-0000-0000-000000000021', '31000000-0000-0000-0000-000000000022', '31000000-0000-0000-0000-000000000023')), 'excluded claims do not initialize lease or deadline');
select is((select id from public.claim_ai_syllabus_worker_job('worker-2', 'claim-token-2', 60, 900)), null::uuid, 'worker claim excludes cancelled, quarantined, and terminal rows when no eligible work remains');

select * from finish();
rollback;
