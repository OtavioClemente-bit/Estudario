-- Two independent sessions serialize each ordering on the same job row.
-- The first holds the lock through its RPC; the second is dispatched before commit.
select no_plan();
select is(dblink_connect('race_setup', format('host=supabase_db_estudario-local port=5432 dbname=%s user=postgres password=postgres options=-csearch_path=', current_database())), 'OK', 'setup connection opens');
select ok(dblink_exec('race_setup', $sql$
  begin;
  delete from public.ai_jobs where user_id = '20000000-0000-0000-0000-000000000101';
  delete from auth.users where id = '20000000-0000-0000-0000-000000000101';
  insert into auth.users (id, aud, role, email, created_at, updated_at)
  values ('20000000-0000-0000-0000-000000000101', 'authenticated', 'authenticated', 'provider-race@example.test', now(), now());
  insert into public.profiles (user_id, beta_access) values ('20000000-0000-0000-0000-000000000101', true);
  insert into public.ai_jobs (id, user_id, feature, status, idempotency_key, request_fingerprint,
    source_object_path, source_hash, lease_owner, lease_token, lease_generation, lease_expires_at)
  select ('20000000-0000-0000-0000-' || lpad(n::text, 12, '0'))::uuid,
    '20000000-0000-0000-0000-000000000101'::uuid, 'SYLLABUS_GENERATION',
    'PROCESSING', 'race-' || n, 'race-' || n, 'source/file.pdf', repeat('a', 64),
    'worker', 'token', 1, now() + interval '5 minutes'
  from generate_series(102, 103) n;
  insert into public.ai_quota_usage (user_id, feature, period_start, reserved_count)
  values ('20000000-0000-0000-0000-000000000101', 'SYLLABUS_GENERATION', (now() at time zone 'America/Sao_Paulo')::date, 2);
  insert into public.ai_quota_reservations (job_id, user_id, feature, period_start)
  select id, user_id, feature, (now() at time zone 'America/Sao_Paulo')::date
  from public.ai_jobs where user_id = '20000000-0000-0000-0000-000000000101';
  commit;
$sql$) is not null, 'race fixtures committed');
select is(dblink_disconnect('race_setup'), 'OK', 'setup connection closes');
select is(dblink_connect('race_first', format('host=supabase_db_estudario-local port=5432 dbname=%s user=postgres password=postgres options=-csearch_path=', current_database())), 'OK', 'first session opens');
select is(dblink_connect('race_second', format('host=supabase_db_estudario-local port=5432 dbname=%s user=postgres password=postgres options=-csearch_path=', current_database())), 'OK', 'second session opens');
select ok(dblink_exec('race_first', 'set request.jwt.claim.role = ''service_role''') is not null, 'first session has service role');
select ok(dblink_exec('race_second', 'set request.jwt.claim.role = ''service_role''') is not null, 'second session has service role');
create temp table race_pids (first_pid integer, second_pid integer);
insert into race_pids
select (select pid from dblink('race_first', 'select pg_backend_pid()') as t(pid integer)),
       (select pid from dblink('race_second', 'select pg_backend_pid()') as t(pid integer));
create function pg_temp.wait_for_row_block(p_waiter integer, p_blocker integer)
returns boolean language plpgsql as $$
declare v_deadline timestamptz := clock_timestamp() + interval '5 seconds';
begin
  loop
    if p_blocker = any(pg_blocking_pids(p_waiter)) then return true; end if;
    if clock_timestamp() >= v_deadline then return false; end if;
  end loop;
end;
$$;

-- Cancellation wins: mark-start must observe the committed request.
select ok(dblink_exec('race_first', $sql$begin; do $body$ begin perform 1 from public.ai_jobs where id = '20000000-0000-0000-0000-000000000102' for update; perform public.request_ai_job_cancellation('20000000-0000-0000-0000-000000000102'); end $body$;$sql$) is not null, 'cancellation holds the row lock');
select is(dblink_send_query('race_second', $sql$select provider_start_outcome from public.mark_ai_job_provider_execution_started('20000000-0000-0000-0000-000000000102', 'worker', 'token', 1)$sql$), 1, 'mark-start dispatched against held lock');
select ok((select pg_temp.wait_for_row_block(second_pid, first_pid) from race_pids), 'mark-start reaches the locked job row before cancellation commits');
select ok(dblink_exec('race_first', 'commit') is not null, 'cancellation commits before mark-start');
select is((select count(*) from dblink_get_result('race_second', false) as t(outcome text)), 0::bigint, 'mark-start returns no success row');
select ok(dblink_error_message('race_second') like '%AI_JOB_CANCELLATION_PENDING%', 'mark-start reports stable cancellation error');
select ok((select cancellation_requested_at is not null and provider_start_outcome = 'NOT_STARTED' and provider_quarantined_at is null from public.ai_jobs where id = '20000000-0000-0000-0000-000000000102'), 'cancellation-first never marks provider start');
select is(dblink_disconnect('race_second'), 'OK', 'failed mark session closes');
select is(dblink_connect('race_second', format('host=supabase_db_estudario-local port=5432 dbname=%s user=postgres password=postgres options=-csearch_path=', current_database())), 'OK', 'second ordering session opens');
select ok(dblink_exec('race_second', 'set request.jwt.claim.role = ''service_role''') is not null, 'second ordering has service role');
update race_pids set second_pid = (select pid from dblink('race_second', 'select pg_backend_pid()') as t(pid integer));

-- Mark-start wins: the later cancellation records pending work and cannot undo quarantine.
select ok(dblink_exec('race_first', $sql$begin; do $body$ begin perform 1 from public.ai_jobs where id = '20000000-0000-0000-0000-000000000103' for update; perform public.mark_ai_job_provider_execution_started('20000000-0000-0000-0000-000000000103', 'worker', 'token', 1); end $body$;$sql$) is not null, 'mark-start holds the row lock');
select is(dblink_send_query('race_second', $sql$select cancellation_requested_at is not null from public.request_ai_job_cancellation('20000000-0000-0000-0000-000000000103')$sql$), 1, 'cancellation dispatched against held lock');
select ok((select pg_temp.wait_for_row_block(second_pid, first_pid) from race_pids), 'cancellation reaches the locked job row before mark-start commits');
select ok(dblink_exec('race_first', 'commit') is not null, 'mark-start commits before cancellation');
select is((select requested from dblink_get_result('race_second') as t(requested boolean)), true, 'later cancellation records request');
select ok((select provider_start_outcome = 'IN_FLIGHT' and provider_quarantined_at is not null and openai_response_id is null and cancellation_requested_at is not null from public.ai_jobs where id = '20000000-0000-0000-0000-000000000103'), 'mark-first remains quarantined with cancellation pending');
select is(dblink_disconnect('race_first'), 'OK', 'first session closes');
select is(dblink_disconnect('race_second'), 'OK', 'second session closes');
select is(dblink_connect('race_cleanup', format('host=supabase_db_estudario-local port=5432 dbname=%s user=postgres password=postgres options=-csearch_path=', current_database())), 'OK', 'cleanup connection opens');
select ok(dblink_exec('race_cleanup', $sql$delete from public.ai_jobs where user_id = '20000000-0000-0000-0000-000000000101'; delete from auth.users where id = '20000000-0000-0000-0000-000000000101'$sql$) is not null, 'race fixtures removed');
select is(dblink_disconnect('race_cleanup'), 'OK', 'cleanup connection closes');
select * from finish();
