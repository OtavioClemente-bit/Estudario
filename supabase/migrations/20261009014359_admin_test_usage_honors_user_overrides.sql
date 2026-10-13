-- Aplicada direto no banco (outra sessão); trazida para o repositório em 13/10/2026.
create or replace function public.admin_test_plan_usage(p_email text)
returns table(feature text, limite integer, usado integer, reservado integer, ciclo date)
language sql stable security definer
set search_path to 'public'
as $$
 select l.feature, coalesce(o.quota_limit,l.quota_limit),
        coalesce(u.successful_count,0),coalesce(u.reserved_count,0),p.test_cycle_start
 from auth.users a
 join public.profiles p on p.user_id=a.id and p.plan_tier='TESTE'
 join public.ai_plan_limits l on l.plan_tier='TESTE' and l.period_kind='LIFETIME'
 left join public.ai_user_quota_overrides o on o.user_id=a.id and o.feature=l.feature
 left join public.ai_quota_usage u on u.user_id=a.id and u.feature::text=l.feature and u.period_start=p.test_cycle_start
 where lower(a.email)=lower(trim(p_email))
 order by l.feature;
$$;
