-- Sincronização: teto de arquivos por conta no bucket user-sync.
--
-- Cada envio grava um arquivo novo e os clientes apagam os antigos depois, mas um cliente
-- alterado poderia só enviar e nunca apagar, enchendo o storage (custo) sem limite. Com o teto,
-- a conta precisa apagar fotos antigas antes de gravar outra; os clientes oficiais ficam bem
-- abaixo disso (guardam 5).

drop policy if exists "user_sync_insert_own" on storage.objects;

create policy "user_sync_insert_own" on storage.objects
  for insert to authenticated
  with check (
    bucket_id = 'user-sync'
    and (storage.foldername(name))[1] = auth.uid()::text
    and (
      select count(*) from storage.objects o
      where o.bucket_id = 'user-sync' and (storage.foldername(o.name))[1] = auth.uid()::text
    ) < 25
  );
