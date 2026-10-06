-- Sincronização entre o app Android e o app web (app.estudario.com.br).
--
-- Cada conta guarda "fotos" completas dos seus dados de estudo (o mesmo formato do backup do app)
-- no bucket privado user-sync, em <user_id>/<arquivo>. A tabela sync_heads aponta qual foto é a
-- atual e tem um número de revisão: um aparelho só publica uma foto nova se ainda estiver na
-- revisão que leu (controle otimista). Se outro aparelho publicou antes, sync_commit recusa com
-- 'sync_conflict' e o app pergunta qual versão manter, sem apagar nenhuma das duas.

insert into storage.buckets (id, name, public, file_size_limit, allowed_mime_types)
values ('user-sync', 'user-sync', false, 52428800, array['application/json', 'application/gzip'])
on conflict (id) do nothing;

create policy "user_sync_select_own" on storage.objects
  for select to authenticated
  using (bucket_id = 'user-sync' and (storage.foldername(name))[1] = auth.uid()::text);

create policy "user_sync_insert_own" on storage.objects
  for insert to authenticated
  with check (bucket_id = 'user-sync' and (storage.foldername(name))[1] = auth.uid()::text);

create policy "user_sync_delete_own" on storage.objects
  for delete to authenticated
  using (bucket_id = 'user-sync' and (storage.foldername(name))[1] = auth.uid()::text);

create table public.sync_heads (
  user_id uuid primary key references auth.users (id) on delete cascade,
  revision bigint not null default 0 check (revision >= 0),
  object_path text,
  sha256 text,
  size_bytes bigint,
  device_id text,
  device_label text,
  updated_at timestamptz not null default now()
);

alter table public.sync_heads enable row level security;

create policy "sync_heads_select_own" on public.sync_heads
  for select to authenticated
  using (user_id = auth.uid());

revoke all on public.sync_heads from anon, authenticated;
grant select on public.sync_heads to authenticated;

-- Publica a foto já enviada ao bucket como a versão atual da conta.
create function public.sync_commit(
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
begin
  if v_user is null then
    raise exception 'not_authenticated' using errcode = '28000';
  end if;
  if p_object_path is null or split_part(p_object_path, '/', 1) <> v_user::text then
    raise exception 'invalid_object_path' using errcode = '22023';
  end if;
  if not exists (
    select 1 from storage.objects
    where bucket_id = 'user-sync' and name = p_object_path and owner_id = v_user::text
  ) then
    raise exception 'object_not_found' using errcode = '22023';
  end if;
  if p_sha256 is null or p_sha256 !~ '^[0-9a-f]{64}$' then
    raise exception 'invalid_sha256' using errcode = '22023';
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
