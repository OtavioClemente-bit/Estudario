-- Registro curto dos avisos do Mercado Pago (para diagnosticar aviso recusado ou perdido).
-- Guarda só tipo, id do recurso e resultado; nada de dados de cartão ou pessoais.
create table if not exists public.mp_webhook_log (
  id bigint generated always as identity primary key,
  received_at timestamptz not null default now(),
  type text,
  resource_id text,
  signature_ok boolean,
  outcome text
);
alter table public.mp_webhook_log enable row level security;
revoke all on public.mp_webhook_log from public, anon, authenticated;
