-- Simuladão do edital: todos os alunos do mesmo edital fazem a mesma prova (exam_simulation), e o
-- resultado entra num ranking. Vale a primeira entrega de cada pessoa (refazer não melhora a posição).
-- Ninguém lê a nota dos outros: as funções devolvem só a posição, o total de participantes e a média.

create table public.exam_simulation_results (
  exam_simulation_id text not null references public.exam_simulation (id) on delete cascade,
  user_id uuid not null references auth.users (id) on delete cascade,
  correct integer not null check (correct >= 0),
  total integer not null check (total > 0 and correct <= total),
  score_percent integer generated always as ((correct * 100) / total) stored,
  elapsed_seconds integer not null default 0 check (elapsed_seconds >= 0),
  finished_at timestamptz not null default now(),
  primary key (exam_simulation_id, user_id)
);

create index exam_simulation_results_score_idx on public.exam_simulation_results (exam_simulation_id, score_percent);

alter table public.exam_simulation_results enable row level security;
revoke all on table public.exam_simulation_results from public, anon, authenticated;

-- A posição da pessoa numa prova: quantos fizeram, quantos ficaram abaixo dela e a média geral.
create or replace function public.exam_simulation_ranking(p_simulation_id text)
returns jsonb
language plpgsql
security definer
set search_path = public
stable
as $$
declare
  v_user uuid := auth.uid();
  v_mine integer;
  v_total integer;
  v_below integer;
  v_average integer;
begin
  if v_user is null then
    raise exception using errcode = '42501', message = 'AUTH_REQUIRED';
  end if;
  select score_percent into v_mine from public.exam_simulation_results where exam_simulation_id = p_simulation_id and user_id = v_user;
  select count(*), coalesce(round(avg(score_percent)), 0) into v_total, v_average
  from public.exam_simulation_results where exam_simulation_id = p_simulation_id;
  if v_mine is null then
    return jsonb_build_object('participants', v_total, 'average', v_average);
  end if;
  select count(*) into v_below from public.exam_simulation_results
  where exam_simulation_id = p_simulation_id and score_percent < v_mine;
  return jsonb_build_object(
    'participants', v_total,
    'average', v_average,
    'score', v_mine,
    -- À frente de quantos por cento dos outros (sozinho na prova: 100).
    'aheadPercent', case when v_total <= 1 then 100 else (v_below * 100) / (v_total - 1) end
  );
end;
$$;

-- Entrega: grava só a primeira tentativa da pessoa e devolve a posição.
create or replace function public.submit_exam_simulation_result(p_simulation_id text, p_correct integer, p_total integer, p_elapsed_seconds integer default 0)
returns jsonb
language plpgsql
security definer
set search_path = public
as $$
declare
  v_user uuid := auth.uid();
begin
  if v_user is null then
    raise exception using errcode = '42501', message = 'AUTH_REQUIRED';
  end if;
  if not exists (select 1 from public.exam_simulation where id = p_simulation_id and status = 'PUBLISHED') then
    raise exception using errcode = 'P0001', message = 'SIMULATION_NOT_FOUND';
  end if;
  if p_total is null or p_total <= 0 or p_correct is null or p_correct < 0 or p_correct > p_total then
    raise exception using errcode = 'P0001', message = 'INVALID_RESULT';
  end if;
  insert into public.exam_simulation_results (exam_simulation_id, user_id, correct, total, elapsed_seconds)
  values (p_simulation_id, v_user, p_correct, p_total, greatest(coalesce(p_elapsed_seconds, 0), 0))
  on conflict (exam_simulation_id, user_id) do nothing;
  return public.exam_simulation_ranking(p_simulation_id);
end;
$$;

revoke all on function public.exam_simulation_ranking(text) from public, anon;
revoke all on function public.submit_exam_simulation_result(text, integer, integer, integer) from public, anon;
grant execute on function public.exam_simulation_ranking(text) to authenticated;
grant execute on function public.submit_exam_simulation_result(text, integer, integer, integer) to authenticated;
