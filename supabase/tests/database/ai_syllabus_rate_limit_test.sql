select no_plan();
begin;

insert into auth.users (id, aud, role, email, created_at, updated_at)
values
  ('00000000-0000-0000-0000-0000000000d1', 'authenticated', 'authenticated', 'rate-a@example.test', now(), now()),
  ('00000000-0000-0000-0000-0000000000d2', 'authenticated', 'authenticated', 'rate-b@example.test', now(), now())
on conflict (id) do nothing;
insert into public.profiles (user_id, beta_access)
values ('00000000-0000-0000-0000-0000000000d1', true), ('00000000-0000-0000-0000-0000000000d2', true)
on conflict (user_id) do update set beta_access = excluded.beta_access;
update public.ai_feature_flags set enabled = true where flag_key in ('AI_BETA_ENABLED', 'SYLLABUS_AI_ENABLED', 'PLAN_AI_ENABLED');
create or replace function public.ai_quota_limit(p_feature public.ai_feature)
returns integer language sql immutable as $$ select 10 $$;
select set_config('request.jwt.claim.role', 'authenticated', true);
select set_config('request.jwt.claim.sub', '00000000-0000-0000-0000-0000000000d1', true);

select is((select max_attempts from public.ai_syllabus_rate_limit_config where id = true), 3, 'server policy defaults to three attempts');
select lives_ok($$select * from public.create_or_get_ai_job_and_reserve_quota('SYLLABUS_GENERATION', 'rate-1', 'fp-1', '{}'::jsonb)$$, 'first syllabus attempt accepted');
select lives_ok($$select * from public.create_or_get_ai_job_and_reserve_quota('SYLLABUS_GENERATION', 'rate-2', 'fp-2', '{}'::jsonb)$$, 'second syllabus attempt accepted');
select lives_ok($$select * from public.create_or_get_ai_job_and_reserve_quota('SYLLABUS_GENERATION', 'rate-3', 'fp-3', '{}'::jsonb)$$, 'third syllabus attempt accepted');
select is((select attempt_count from public.ai_syllabus_rate_counters where user_id = auth.uid() and window_index = floor(extract(epoch from clock_timestamp()) / 600)::bigint), 3, 'one fixed window records three attempts');
select throws_ok($$select * from public.create_or_get_ai_job_and_reserve_quota('SYLLABUS_GENERATION', 'rate-4', 'fp-4', '{}'::jsonb)$$, 'P0001', 'AI_RATE_LIMIT_EXCEEDED', 'fourth attempt rejected');
select is((select attempt_count from public.ai_syllabus_rate_counters where user_id = auth.uid() and window_index = floor(extract(epoch from clock_timestamp()) / 600)::bigint), 3, 'rejection does not increment counter');
select is((select count(*) from public.ai_jobs where user_id = auth.uid() and idempotency_key = 'rate-4'), 0::bigint, 'rejected attempt creates no job');
select is((select reused from public.create_or_get_ai_job_and_reserve_quota('SYLLABUS_GENERATION', 'rate-1', 'fp-1', '{}'::jsonb)), true, 'same key and fingerprint reuse after cap');
select throws_ok($$select * from public.create_or_get_ai_job_and_reserve_quota('SYLLABUS_GENERATION', 'rate-1', 'different', '{}'::jsonb)$$, 'P0001', 'IDEMPOTENCY_KEY_CONFLICT', 'conflict precedes rate check');
select is((select attempt_count from public.ai_syllabus_rate_counters where user_id = auth.uid() and window_index = floor(extract(epoch from clock_timestamp()) / 600)::bigint), 3, 'retry and conflict do not consume attempts');
select lives_ok($$select * from public.create_or_get_ai_job_and_reserve_quota('PLAN_GENERATION', 'plan-1', 'plan-fp', '{}'::jsonb)$$, 'other feature does not use syllabus rate counter');
select set_config('request.jwt.claim.sub', '00000000-0000-0000-0000-0000000000d2', true);
select lives_ok($$select * from public.create_or_get_ai_job_and_reserve_quota('SYLLABUS_GENERATION', 'other-user', 'other-fp', '{}'::jsonb)$$, 'second user has independent allowance');
select set_config('request.jwt.claim.sub', '00000000-0000-0000-0000-0000000000d1', true);
select is((select attempt_count from public.ai_syllabus_rate_counters where user_id = auth.uid() and window_index = floor(extract(epoch from clock_timestamp()) / 600)::bigint), 3, 'other user does not consume first user allowance');
select set_config('ai.internal_job_transition', '1', true);
update public.ai_jobs set status = 'PROCESSING', source_object_path = auth.uid()::text || '/fixture.pdf', source_hash = repeat('a', 64)
where user_id = auth.uid() and idempotency_key = 'rate-1';
select lives_ok($$update public.ai_jobs set status = 'FAILED', error_code = 'AI_PROVIDER_UNAVAILABLE', provider_reconciled_at = now(), provider_result_recoverable = false where user_id = auth.uid() and idempotency_key = 'rate-1'$$, 'provider failure fixture recorded');
select set_config('ai.internal_job_transition', '', true);
select throws_ok($$select * from public.create_or_get_ai_job_and_reserve_quota('SYLLABUS_GENERATION', 'after-failure', 'after-failure-fp', '{}'::jsonb)$$, 'P0001', 'AI_RATE_LIMIT_EXCEEDED', 'provider failure still counts as accepted attempt');
update public.ai_syllabus_rate_limit_config set max_attempts = 4 where id = true;
select lives_ok($$select * from public.create_or_get_ai_job_and_reserve_quota('SYLLABUS_GENERATION', 'rate-4', 'fp-4', '{}'::jsonb)$$, 'server-side policy can raise the allowance');
select throws_ok($$select * from public.create_or_get_ai_job_and_reserve_quota('SYLLABUS_GENERATION', 'rate-5', 'fp-5', '{}'::jsonb)$$, 'P0001', 'AI_RATE_LIMIT_EXCEEDED', 'configured fifth attempt rejected');
update public.ai_syllabus_rate_counters set window_index = window_index - 1 where user_id = auth.uid();
select lives_ok($$select * from public.create_or_get_ai_job_and_reserve_quota('SYLLABUS_GENERATION', 'new-window', 'new-window-fp', '{}'::jsonb)$$, 'a prior fixed-window row does not block current window');
select is((select attempt_count from public.ai_syllabus_rate_counters where user_id = auth.uid() and window_index = floor(extract(epoch from clock_timestamp()) / 600)::bigint), 1, 'new fixed window starts at one');

select * from finish();
rollback;
