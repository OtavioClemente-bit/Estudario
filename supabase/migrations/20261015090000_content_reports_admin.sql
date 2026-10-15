-- Leitura dos reportes e comentários (content_reports) pelo dono do Estudário, na página
-- "Reportes" do site. Só a conta do administrador passa; para qualquer outra a função recusa.
-- A tabela continua sem SELECT para os usuários: tudo passa por estas duas funções.

create or replace function public.admin_is_owner()
returns boolean
language sql
stable
security definer
set search_path = public
as $$
  select coalesce(auth.uid() = '1f75cd98-6341-4cc2-b606-e8ecd53a6c8a'::uuid, false);
$$;

create or replace function public.admin_content_reports(p_status text default null, p_limit int default 200)
returns table (
  id bigint,
  kind text,
  reason text,
  competition text,
  topic text,
  excerpt text,
  comment text,
  app_version text,
  status text,
  created_at timestamptz,
  reporter_email text
)
language plpgsql
stable
security definer
set search_path = public
as $$
begin
  if not public.admin_is_owner() then
    raise exception 'ADMIN_ONLY' using errcode = '42501';
  end if;
  return query
    select r.id, r.kind, r.reason, r.competition, r.topic, r.excerpt, r.comment, r.app_version, r.status, r.created_at,
           u.email::text
    from public.content_reports r
    left join auth.users u on u.id = r.user_id
    where p_status is null or r.status = p_status
    order by r.created_at desc
    limit greatest(1, least(coalesce(p_limit, 200), 1000));
end;
$$;

create or replace function public.admin_set_report_status(p_id bigint, p_status text)
returns void
language plpgsql
security definer
set search_path = public
as $$
begin
  if not public.admin_is_owner() then
    raise exception 'ADMIN_ONLY' using errcode = '42501';
  end if;
  if p_status not in ('OPEN', 'FIXED', 'DISMISSED') then
    raise exception 'BAD_STATUS' using errcode = '22023';
  end if;
  update public.content_reports set status = p_status where id = p_id;
end;
$$;

revoke all on function public.admin_is_owner() from public, anon;
revoke all on function public.admin_content_reports(text, int) from public, anon;
revoke all on function public.admin_set_report_status(bigint, text) from public, anon;
grant execute on function public.admin_is_owner() to authenticated;
grant execute on function public.admin_content_reports(text, int) to authenticated;
grant execute on function public.admin_set_report_status(bigint, text) to authenticated;
