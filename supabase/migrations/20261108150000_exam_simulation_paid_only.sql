-- Prova oficial e Simuladões são do plano pago: antes qualquer conta logada lia a tabela pela API
-- (o bloqueio era só na tela). Agora o banco só entrega a quem tem plano pago vigente ou é da equipe.
-- O ranking também passa a exigir plano pago e um resultado coerente com a prova.

create or replace function public.exam_simulation_allowed()
returns boolean
language sql
stable
security definer
set search_path = public
as $$
  select auth.uid() is not null
    and (public.ai_effective_plan_tier(auth.uid()) in ('ESSENCIAL', 'PRO') or public.is_app_admin());
$$;
revoke all on function public.exam_simulation_allowed() from public, anon;
grant execute on function public.exam_simulation_allowed() to authenticated;

drop policy if exists exam_simulation_read_published on public.exam_simulation;
create policy exam_simulation_read_published on public.exam_simulation
  for select to authenticated
  using (status = 'PUBLISHED' and public.exam_simulation_allowed());

create or replace function public.submit_exam_simulation_result(p_simulation_id text, p_correct integer, p_total integer, p_elapsed_seconds integer default 0)
returns jsonb
language plpgsql
security definer
set search_path = public
as $$
declare
  v_user uuid := auth.uid();
  v_items integer;
begin
  if v_user is null then
    raise exception using errcode = '42501', message = 'AUTH_REQUIRED';
  end if;
  if not public.exam_simulation_allowed() then
    raise exception using errcode = '42501', message = 'PAID_ONLY';
  end if;
  select item_count into v_items from public.exam_simulation where id = p_simulation_id and status = 'PUBLISHED';
  if not found then
    raise exception using errcode = 'P0001', message = 'SIMULATION_NOT_FOUND';
  end if;
  if p_total is null or p_total <= 0 or p_correct is null or p_correct < 0 or p_correct > p_total
     or (v_items is not null and v_items > 0 and p_total > v_items) then
    raise exception using errcode = 'P0001', message = 'INVALID_RESULT';
  end if;
  -- Ninguém responde uma prova inteira em menos de 3 segundos por questão: resultado forjado não entra.
  if coalesce(p_elapsed_seconds, 0) < p_total * 3 then
    raise exception using errcode = 'P0001', message = 'INVALID_RESULT';
  end if;
  insert into public.exam_simulation_results (exam_simulation_id, user_id, correct, total, elapsed_seconds)
  values (p_simulation_id, v_user, p_correct, p_total, p_elapsed_seconds)
  on conflict (exam_simulation_id, user_id) do nothing;
  return public.exam_simulation_ranking(p_simulation_id);
end;
$$;
revoke all on function public.submit_exam_simulation_result(text, integer, integer, integer) from public, anon;
grant execute on function public.submit_exam_simulation_result(text, integer, integer, integer) to authenticated;
