-- Aplicada direto no banco (outra sessão); trazida para o repositório em 13/10/2026.
create table if not exists public.ai_user_quota_overrides (
 user_id uuid not null references auth.users(id) on delete cascade,
 feature text not null check (feature in ('SYLLABUS_GENERATION','CONTENT_GENERATION','PLAN_GENERATION','QUESTION_BATCH','SIMULATION_GENERATION')),
 quota_limit integer not null check (quota_limit between 0 and 500),
 updated_at timestamptz not null default now(),
 primary key (user_id,feature)
);
alter table public.ai_user_quota_overrides enable row level security;
revoke all on public.ai_user_quota_overrides from public, anon, authenticated;
grant select on public.ai_user_quota_overrides to authenticated;
grant all on public.ai_user_quota_overrides to service_role;
drop policy if exists "Read own AI quota overrides" on public.ai_user_quota_overrides;
create policy "Read own AI quota overrides"
on public.ai_user_quota_overrides for select to authenticated
using ((select auth.uid()) = user_id);

create or replace function public.ai_quota_limit(p_feature ai_feature)
returns integer language plpgsql stable security definer
set search_path to 'public'
as $$
declare
 v_user_id uuid := auth.uid();
 v_tier text := public.ai_effective_plan_tier(v_user_id);
 v_row public.ai_plan_limits := public.ai_plan_quota_row(v_tier,p_feature::text);
 v_override integer;
begin
 if v_tier='TESTE' then
   select o.quota_limit into v_override
   from public.ai_user_quota_overrides o
   where o.user_id=v_user_id and o.feature=p_feature::text;
 end if;
 return coalesce(v_override,v_row.quota_limit,0);
end;
$$;
