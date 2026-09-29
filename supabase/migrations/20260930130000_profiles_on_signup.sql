-- Toda conta nova ganha o perfil na hora do cadastro.
--
-- Antes, o perfil (onde ficam o plano e o acesso à IA) só existia se fosse criado à mão, então
-- quem entrava pela primeira vez (por e-mail ou pelo Google) recebia "IA indisponível". Agora o
-- perfil nasce junto com a conta, no plano Grátis e com a IA liberada; os limites de uso do plano
-- Grátis e do aparelho continuam valendo.

create function public.create_profile_for_new_user()
returns trigger
language plpgsql
security definer
set search_path = public
as $$
begin
  insert into public.profiles (user_id, beta_access, beta_access_granted_at)
  values (new.id, true, now())
  on conflict (user_id) do nothing;
  return new;
end;
$$;

revoke all on function public.create_profile_for_new_user() from public, anon, authenticated;

create trigger on_auth_user_created_profile
  after insert on auth.users
  for each row execute function public.create_profile_for_new_user();

-- Contas que já existem sem perfil (criadas antes deste trigger).
insert into public.profiles (user_id, beta_access, beta_access_granted_at)
select users.id, true, now()
from auth.users as users
where not exists (select 1 from public.profiles as profile where profile.user_id = users.id);
