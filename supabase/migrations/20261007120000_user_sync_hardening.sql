-- Sincronização app ⇄ app web: fecha duas brechas pequenas do sync_commit.
--
-- 1. O caminho da foto precisa ter o formato que os clientes usam (<uid>/<horário>-<12 hex>.json):
--    impede apontar a cabeça da conta para um objeto com nome arbitrário dentro da pasta.
-- 2. O tamanho declarado precisa bater com o objeto gravado e ficar dentro do limite do bucket.
-- O restante (dono do objeto, revisão esperada, hash) continua como antes.

create or replace function public.sync_commit(
  p_expected_revision bigint,
  p_object_path text,
  p_sha256 text,
  p_size_bytes bigint,
  p_device_id text,
  p_device_label text
)
returns public.sync_heads
language plpgsql
security definer
set search_path = public
as $$
declare
  v_user uuid := auth.uid();
  v_head public.sync_heads;
  v_stored_size bigint;
begin
  if v_user is null then
    raise exception 'not_authenticated' using errcode = '28000';
  end if;
  if p_object_path is null
     or p_object_path !~ ('^' || v_user::text || '/[0-9]{1,16}-[0-9a-f]{12}\.json$') then
    raise exception 'invalid_object_path' using errcode = '22023';
  end if;
  if p_sha256 is null or p_sha256 !~ '^[0-9a-f]{64}$' then
    raise exception 'invalid_sha256' using errcode = '22023';
  end if;
  if p_size_bytes is null or p_size_bytes < 1 or p_size_bytes > 52428800 then
    raise exception 'invalid_size' using errcode = '22023';
  end if;

  select (metadata ->> 'size')::bigint into v_stored_size
  from storage.objects
  where bucket_id = 'user-sync' and name = p_object_path and owner_id = v_user::text;
  if not found then
    raise exception 'object_not_found' using errcode = '22023';
  end if;
  if v_stored_size is not null and v_stored_size <> p_size_bytes then
    raise exception 'size_mismatch' using errcode = '22023';
  end if;

  insert into public.sync_heads (user_id) values (v_user) on conflict (user_id) do nothing;
  select * into v_head from public.sync_heads where user_id = v_user for update;

  if v_head.revision <> p_expected_revision then
    raise exception 'sync_conflict' using errcode = '40001',
      detail = json_build_object('revision', v_head.revision, 'device_label', v_head.device_label, 'updated_at', v_head.updated_at)::text;
  end if;

  update public.sync_heads
  set revision = v_head.revision + 1,
      object_path = p_object_path,
      sha256 = p_sha256,
      size_bytes = p_size_bytes,
      device_id = left(p_device_id, 100),
      device_label = left(p_device_label, 100),
      updated_at = now()
  where user_id = v_user
  returning * into v_head;

  return v_head;
end;
$$;

revoke all on function public.sync_commit(bigint, text, text, bigint, text, text) from public, anon;
grant execute on function public.sync_commit(bigint, text, text, bigint, text, text) to authenticated;
