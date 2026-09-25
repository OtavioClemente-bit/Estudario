select no_plan();

select is(dblink_connect('rate_setup', format('host=supabase_db_estudario-local port=5432 dbname=%s user=postgres password=postgres options=-csearch_path=', current_database())), 'OK', 'fixture connection opens');
select ok(dblink_exec('rate_setup', $sql$
  insert into auth.users (id, aud, role, email, created_at, updated_at)
  values ('00000000-0000-0000-0000-0000000000d3', 'authenticated', 'authenticated', 'rate-race@example.test', now(), now())
  on conflict (id) do nothing;
  insert into public.profiles (user_id, beta_access)
  values ('00000000-0000-0000-0000-0000000000d3', true)
  on conflict (user_id) do update set beta_access = true;
  update public.ai_feature_flags set enabled = true where flag_key in ('AI_BETA_ENABLED', 'SYLLABUS_AI_ENABLED');
  update public.ai_syllabus_rate_limit_config set max_attempts = 1 where id = true;
$sql$) is not null, 'one-attempt policy and user fixture committed');
select is(dblink_disconnect('rate_setup'), 'OK', 'fixture connection closes');

create temp table rate_race_results (job_id uuid);
select is(dblink_connect('rate_r1', format('host=supabase_db_estudario-local port=5432 dbname=%s user=postgres password=postgres options=-csearch_path=', current_database())), 'OK', 'first racer connects');
select is(dblink_connect('rate_r2', format('host=supabase_db_estudario-local port=5432 dbname=%s user=postgres password=postgres options=-csearch_path=', current_database())), 'OK', 'second racer connects');
select is(dblink_connect('rate_r3', format('host=supabase_db_estudario-local port=5432 dbname=%s user=postgres password=postgres options=-csearch_path=', current_database())), 'OK', 'third racer connects');
select is(dblink_connect('rate_r4', format('host=supabase_db_estudario-local port=5432 dbname=%s user=postgres password=postgres options=-csearch_path=', current_database())), 'OK', 'fourth racer connects');

select is(dblink_send_query('rate_r1', $sql$select set_config('request.jwt.claim.sub', '00000000-0000-0000-0000-0000000000d3', false); select job_id from public.create_or_get_ai_job_and_reserve_quota('SYLLABUS_GENERATION', 'race-1', 'fp-1', '{}'::jsonb)$sql$), 1, 'first request dispatched');
select is(dblink_send_query('rate_r2', $sql$select set_config('request.jwt.claim.sub', '00000000-0000-0000-0000-0000000000d3', false); select job_id from public.create_or_get_ai_job_and_reserve_quota('SYLLABUS_GENERATION', 'race-2', 'fp-2', '{}'::jsonb)$sql$), 1, 'second request dispatched');
select is(dblink_send_query('rate_r3', $sql$select set_config('request.jwt.claim.sub', '00000000-0000-0000-0000-0000000000d3', false); select job_id from public.create_or_get_ai_job_and_reserve_quota('SYLLABUS_GENERATION', 'race-3', 'fp-3', '{}'::jsonb)$sql$), 1, 'third request dispatched');
select is(dblink_send_query('rate_r4', $sql$select set_config('request.jwt.claim.sub', '00000000-0000-0000-0000-0000000000d3', false); select job_id from public.create_or_get_ai_job_and_reserve_quota('SYLLABUS_GENERATION', 'race-4', 'fp-4', '{}'::jsonb)$sql$), 1, 'fourth request dispatched');

-- Consume the claim-setting result first, then the job result. Errors produce zero rows.
select * from dblink_get_result('rate_r1', false) as t(claim text);
insert into rate_race_results select job_id from dblink_get_result('rate_r1', false) as t(job_id uuid);
select * from dblink_get_result('rate_r2', false) as t(claim text);
insert into rate_race_results select job_id from dblink_get_result('rate_r2', false) as t(job_id uuid);
select * from dblink_get_result('rate_r3', false) as t(claim text);
insert into rate_race_results select job_id from dblink_get_result('rate_r3', false) as t(job_id uuid);
select * from dblink_get_result('rate_r4', false) as t(claim text);
insert into rate_race_results select job_id from dblink_get_result('rate_r4', false) as t(job_id uuid);

select is((select count(*) from rate_race_results), 1::bigint, 'one of four concurrent new jobs is accepted');
select is((select count(*) from public.ai_jobs where user_id = '00000000-0000-0000-0000-0000000000d3'), 1::bigint, 'one durable job exists');
select is((select attempt_count from public.ai_syllabus_rate_counters where user_id = '00000000-0000-0000-0000-0000000000d3' and window_index = floor(extract(epoch from clock_timestamp()) / 600)::bigint), 1, 'atomic counter never overshoots cap');

select is(dblink_disconnect('rate_r1'), 'OK', 'first racer closes');
select is(dblink_disconnect('rate_r2'), 'OK', 'second racer closes');
select is(dblink_disconnect('rate_r3'), 'OK', 'third racer closes');
select is(dblink_disconnect('rate_r4'), 'OK', 'fourth racer closes');
select is(dblink_connect('rate_cleanup', format('host=supabase_db_estudario-local port=5432 dbname=%s user=postgres password=postgres options=-csearch_path=', current_database())), 'OK', 'cleanup connects');
select ok(dblink_exec('rate_cleanup', $sql$update public.ai_syllabus_rate_limit_config set max_attempts = 3 where id = true; delete from public.ai_jobs where user_id = '00000000-0000-0000-0000-0000000000d3'; delete from public.ai_syllabus_rate_counters where user_id = '00000000-0000-0000-0000-0000000000d3'; delete from auth.users where id = '00000000-0000-0000-0000-0000000000d3'$sql$) is not null, 'fixture restored');
select is(dblink_disconnect('rate_cleanup'), 'OK', 'cleanup closes');

select * from finish();
