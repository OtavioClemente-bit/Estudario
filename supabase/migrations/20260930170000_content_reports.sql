-- Reporte de erro no material gerado (teoria, resumo, flashcard, questão). A pessoa só grava;
-- quem lê é a equipe pelo painel/serviço. Nada aqui é exposto a outros usuários.

create table public.content_reports (
  id bigint generated always as identity primary key,
  user_id uuid not null default auth.uid() references auth.users (id) on delete cascade,
  kind text not null check (kind in ('THEORY', 'SUMMARY', 'FLASHCARD', 'QUESTION', 'TIP', 'OTHER')),
  reason text not null check (reason in ('WRONG_FACT', 'OUTDATED_LAW', 'WRONG_ANSWER', 'GENERIC', 'OFF_TOPIC', 'OTHER')),
  competition text check (char_length(competition) <= 200),
  topic text check (char_length(topic) <= 500),
  excerpt text not null check (char_length(excerpt) between 1 and 4000),
  comment text check (char_length(comment) <= 1000),
  app_version text check (char_length(app_version) <= 40),
  status text not null default 'OPEN' check (status in ('OPEN', 'FIXED', 'DISMISSED')),
  created_at timestamptz not null default now()
);

create index content_reports_open_idx on public.content_reports (created_at desc) where status = 'OPEN';

alter table public.content_reports enable row level security;

revoke all on public.content_reports from anon, authenticated;
grant insert (kind, reason, competition, topic, excerpt, comment, app_version) on public.content_reports to authenticated;

create policy content_reports_insert_own on public.content_reports
  for insert to authenticated
  with check (user_id = auth.uid());

-- Freio contra abuso: no máximo 30 reportes por pessoa por dia.
create function public.content_reports_rate_limit()
returns trigger
language plpgsql
security definer
set search_path = public
as $$
begin
  if (
    select count(*) from public.content_reports
    where user_id = new.user_id and created_at > now() - interval '1 day'
  ) >= 30 then
    raise exception 'CONTENT_REPORT_RATE_LIMITED' using errcode = 'P0001';
  end if;
  return new;
end;
$$;

revoke all on function public.content_reports_rate_limit() from public, anon, authenticated;

create trigger content_reports_rate_limit
  before insert on public.content_reports
  for each row execute function public.content_reports_rate_limit();
