select no_plan();
select is(dblink_connect('resolution_setup', format('host=supabase_db_estudario-local port=5432 dbname=%s user=postgres password=postgres options=-csearch_path=', current_database())), 'OK', 'committed race fixture connection opens');
select ok(dblink_exec('resolution_setup', $sql$
  begin;
  delete from public.ai_job_provider_resolution_audit where job_id in (select id from public.ai_jobs where user_id = '34000000-0000-0000-0000-000000000001');
  delete from public.ai_jobs where user_id = '34000000-0000-0000-0000-000000000001';
  delete from auth.users where id = '34000000-0000-0000-0000-000000000001';
  insert into auth.users (id, aud, role, email, created_at, updated_at)
  values ('34000000-0000-0000-0000-000000000001', 'authenticated', 'authenticated', 'resolution-race@example.test', now(), now());
  insert into public.ai_jobs (id, user_id, feature, status, idempotency_key, request_fingerprint,
    source_object_path, source_hash, lease_owner, lease_token, lease_generation, lease_expires_at,
    provider_execution_started_at, provider_start_outcome, provider_quarantined_at)
  values
    ('34000000-0000-0000-0000-000000000011', '34000000-0000-0000-0000-000000000001', 'SYLLABUS_GENERATION', 'PROCESSING', 'resolution-race-worker-first', repeat('a', 64), 'source/file.pdf', repeat('a', 64), 'race-worker', 'race-token', 1, now() + interval '5 minutes', now(), 'IN_FLIGHT', now()),
    ('34000000-0000-0000-0000-000000000012', '34000000-0000-0000-0000-000000000001', 'SYLLABUS_GENERATION', 'PROCESSING', 'resolution-race-admin-first', repeat('b', 64), 'source/file.pdf', repeat('b', 64), 'race-worker', 'race-token', 1, now() + interval '5 minutes', now(), 'IN_FLIGHT', now()),
    ('34000000-0000-0000-0000-000000000013', '34000000-0000-0000-0000-000000000001', 'SYLLABUS_GENERATION', 'PROCESSING', 'resolution-race-conflict', repeat('c', 64), 'source/file.pdf', repeat('c', 64), 'race-worker', 'race-token', 1, now() + interval '5 minutes', now(), 'IN_FLIGHT', now());
  insert into public.ai_quota_usage (user_id, feature, period_start, reserved_count)
  values ('34000000-0000-0000-0000-000000000001', 'SYLLABUS_GENERATION', (now() at time zone 'America/Sao_Paulo')::date, 3);
  insert into public.ai_quota_reservations (job_id, user_id, feature, period_start)
  select id, user_id, feature, (now() at time zone 'America/Sao_Paulo')::date
  from public.ai_jobs where user_id = '34000000-0000-0000-0000-000000000001';
  commit;
$sql$) is not null, 'quarantined response-race fixtures commit');
select is(dblink_disconnect('resolution_setup'), 'OK', 'fixture connection closes');

select is(dblink_connect('resolution_first', format('host=supabase_db_estudario-local port=5432 dbname=%s user=postgres password=postgres options=-csearch_path=', current_database())), 'OK', 'first race session opens');
select is(dblink_connect('resolution_second', format('host=supabase_db_estudario-local port=5432 dbname=%s user=postgres password=postgres options=-csearch_path=', current_database())), 'OK', 'second race session opens');
select ok(dblink_exec('resolution_first', 'set request.jwt.claim.role = ''service_role''; set role service_role') is not null, 'first session authenticates as backend');
select ok(dblink_exec('resolution_second', 'set request.jwt.claim.role = ''service_role''; set role service_role') is not null, 'second session authenticates as backend');
create temporary table resolution_race_pids (first_pid integer, second_pid integer);
insert into resolution_race_pids
select (select pid from dblink('resolution_first', 'select pg_backend_pid()') as t(pid integer)),
       (select pid from dblink('resolution_second', 'select pg_backend_pid()') as t(pid integer));
create function pg_temp.wait_for_resolution_block(p_waiter integer, p_blocker integer)
returns boolean language plpgsql as $$
declare v_deadline timestamptz := clock_timestamp() + interval '5 seconds';
begin
  loop
    if p_blocker = any(pg_blocking_pids(p_waiter)) then return true; end if;
    if clock_timestamp() >= v_deadline then return false; end if;
  end loop;
end;
$$;

-- Worker persists first; ATTACH waits on the same job row and converges to its ID.
select ok(dblink_exec('resolution_first', $sql$begin; do $body$begin
  perform 1 from public.ai_jobs where id = '34000000-0000-0000-0000-000000000011' for update;
  perform public.persist_ai_job_provider_response('34000000-0000-0000-0000-000000000011', 'resp-race-same', 'race-worker', 'race-token', 1);
end$body$;$sql$) is not null, 'worker persistence holds the first row lock');
select is(dblink_send_query('resolution_second', $sql$select public.resolve_ai_job_provider_quarantine('34000000-0000-0000-0000-000000000011', '44000000-0000-0000-0000-000000000011', 'ATTACH_RESPONSE_ID', '{"response_id":"resp-race-same"}'::jsonb, 'operator:race', null) ->> 'status'$sql$), 1, 'same-ID attach is dispatched against the held job row');
select ok((select pg_temp.wait_for_resolution_block(second_pid, first_pid) from resolution_race_pids), 'same-ID attach reaches the row lock before worker commit');
select ok(dblink_exec('resolution_first', 'commit') is not null, 'worker persistence commits first');
select is((select status from dblink_get_result('resolution_second') as t(status text)), 'PROCESSING', 'waiting attach converges successfully after worker persistence');
select is((select count(*) from dblink_get_result('resolution_second') as t(status text)), 0::bigint, 'async result stream is fully drained before the next race');
select is((select openai_response_id from public.ai_jobs where id = '34000000-0000-0000-0000-000000000011'), 'resp-race-same', 'worker and attach retain the identical response ID');

-- ATTACH writes first; an identical worker persistence retry is idempotent.
select ok(dblink_exec('resolution_first', $sql$begin; do $body$begin
  perform 1 from public.ai_jobs where id = '34000000-0000-0000-0000-000000000012' for update;
  perform public.resolve_ai_job_provider_quarantine('34000000-0000-0000-0000-000000000012', '44000000-0000-0000-0000-000000000012', 'ATTACH_RESPONSE_ID', '{"response_id":"resp-race-admin"}'::jsonb, 'operator:race', null);
end$body$;$sql$) is not null, 'administrative attach holds the second row lock');
select is(dblink_send_query('resolution_second', $sql$select (public.persist_ai_job_provider_response('34000000-0000-0000-0000-000000000012', 'resp-race-admin', 'race-worker', 'race-token', 1)).openai_response_id$sql$), 1, 'worker persistence is dispatched behind same-ID attach');
select ok((select pg_temp.wait_for_resolution_block(second_pid, first_pid) from resolution_race_pids), 'worker reaches the row lock before attach commit');
select ok(dblink_exec('resolution_first', 'commit') is not null, 'administrative attach commits first');
select is((select response_id from dblink_get_result('resolution_second') as t(response_id text)), 'resp-race-admin', 'worker persistence converges idempotently after attach');
select is((select count(*) from dblink_get_result('resolution_second') as t(response_id text)), 0::bigint, 'second async result stream is fully drained before the next race');

-- A divergent worker ID must conflict after ATTACH wins, without overwriting it.
select ok(dblink_exec('resolution_first', $sql$begin; do $body$begin
  perform 1 from public.ai_jobs where id = '34000000-0000-0000-0000-000000000013' for update;
  perform public.resolve_ai_job_provider_quarantine('34000000-0000-0000-0000-000000000013', '44000000-0000-0000-0000-000000000013', 'ATTACH_RESPONSE_ID', '{"response_id":"resp-race-admin-wins"}'::jsonb, 'operator:race', null);
end$body$;$sql$) is not null, 'administrative attach holds the conflict row lock');
select is(dblink_send_query('resolution_second', $sql$select public.persist_ai_job_provider_response('34000000-0000-0000-0000-000000000013', 'resp-race-worker-loses', 'race-worker', 'race-token', 1)$sql$), 1, 'divergent worker persistence is dispatched behind attach');
select ok((select pg_temp.wait_for_resolution_block(second_pid, first_pid) from resolution_race_pids), 'divergent worker reaches the row lock before attach commit');
select ok(dblink_exec('resolution_first', 'commit') is not null, 'conflicting administrative attach commits first');
select is((select count(*) from dblink_get_result('resolution_second', false) as t(response_id text)), 0::bigint, 'divergent worker persistence produces no success row');
select ok(dblink_error_message('resolution_second') like '%PROVIDER_RESPONSE_ID_CONFLICT%', 'divergent worker persistence returns stable ID conflict');
select is((select openai_response_id from public.ai_jobs where id = '34000000-0000-0000-0000-000000000013'), 'resp-race-admin-wins', 'divergent worker response cannot overwrite attached ID');

select is(dblink_disconnect('resolution_first'), 'OK', 'first race session disconnects');
select is(dblink_disconnect('resolution_second'), 'OK', 'second race session disconnects');
select is(dblink_connect('resolution_cleanup', format('host=supabase_db_estudario-local port=5432 dbname=%s user=postgres password=postgres options=-csearch_path=', current_database())), 'OK', 'cleanup connection opens');
select ok(dblink_exec('resolution_cleanup', $sql$delete from public.ai_job_provider_resolution_audit where job_id in (select id from public.ai_jobs where user_id = '34000000-0000-0000-0000-000000000001'); delete from public.ai_jobs where user_id = '34000000-0000-0000-0000-000000000001'; delete from auth.users where id = '34000000-0000-0000-0000-000000000001'$sql$) is not null, 'concurrency fixtures are removed');
select is(dblink_disconnect('resolution_cleanup'), 'OK', 'cleanup connection disconnects');
select * from finish();
