set role service_role;
select set_config('request.jwt.claim.role', 'service_role', false);
do $$
declare
  v_message text;
  v_replay jsonb;
begin
  if (select count(*) from public.user_syllabi where id = '00000000-0000-4000-8000-0000000000f9') <> 0
     or (select count(*) from public.user_syllabus_mutations where owner_user_id = '00000000-0000-0000-0000-0000000000f8' and mutation_id = 'delete-concurrent' and response->>'state' = 'SYNCED') <> 1 then
    raise exception 'concurrent delete did not commit one tree deletion and one ACK';
  end if;
  v_replay := public.delete_private_syllabus_atomic(
    '00000000-0000-0000-0000-0000000000f8'::uuid,
    'delete-concurrent', '00000000-0000-4000-8000-0000000000f9'::uuid, repeat('e', 64)
  );
  if v_replay->>'state' <> 'SYNCED' or v_replay->>'remoteSyllabusId' <> '00000000-0000-4000-8000-0000000000f9' then
    raise exception 'delete replay did not return the committed ACK';
  end if;
  begin
    perform public.delete_private_syllabus_atomic(
      '00000000-0000-0000-0000-0000000000f8'::uuid,
      'delete-concurrent', '00000000-0000-4000-8000-0000000000f9'::uuid, repeat('f', 64)
    );
    raise exception 'hash conflict was ACKed';
  exception when others then
    get stacked diagnostics v_message = message_text;
    if v_message <> 'IDEMPOTENCY_KEY_CONFLICT' then raise exception 'unexpected hash conflict: %', v_message; end if;
  end;
  begin
    perform public.delete_private_syllabus_atomic(
      '00000000-0000-0000-0000-0000000000f8'::uuid,
      'delete-concurrent', '00000000-0000-4000-8000-0000000000fc'::uuid, repeat('e', 64)
    );
    raise exception 'remote identity conflict was ACKed';
  exception when others then
    get stacked diagnostics v_message = message_text;
    if v_message <> 'IDEMPOTENCY_KEY_CONFLICT' then raise exception 'unexpected remote conflict: %', v_message; end if;
  end;
end;
$$;
select 'two-session delete lock/replay/hash/remote conflicts passed' as result;
