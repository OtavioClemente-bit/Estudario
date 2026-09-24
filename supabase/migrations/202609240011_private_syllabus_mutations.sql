create table public.user_syllabus_mutations (
  owner_user_id uuid not null references auth.users (id) on delete cascade,
  mutation_id text not null,
  remote_syllabus_id uuid not null,
  operation text not null,
  payload_hash text not null,
  response jsonb,
  created_at timestamptz not null default now(),
  updated_at timestamptz not null default now(),
  primary key (owner_user_id, mutation_id),
  constraint user_syllabus_mutations_id_check check (length(btrim(mutation_id)) > 0 and length(mutation_id) <= 255),
  constraint user_syllabus_mutations_operation_check check (operation in ('UPSERT', 'DELETE')),
  constraint user_syllabus_mutations_payload_hash_check check (payload_hash ~ '^[0-9a-f]{64}$'),
  constraint user_syllabus_mutations_response_check check (response is null or jsonb_typeof(response) = 'object')
);

create index user_syllabus_mutations_syllabus_idx
  on public.user_syllabus_mutations (owner_user_id, remote_syllabus_id, updated_at desc);

create trigger user_syllabus_mutations_set_updated_at
before update on public.user_syllabus_mutations
for each row execute function public.set_updated_at();

alter table public.user_syllabus_mutations enable row level security;

create policy user_syllabus_mutations_select_own
on public.user_syllabus_mutations
for select to authenticated
using (owner_user_id = auth.uid());

create policy user_syllabus_mutations_insert_own
on public.user_syllabus_mutations
for insert to authenticated
with check (owner_user_id = auth.uid());

create policy user_syllabus_mutations_update_own
on public.user_syllabus_mutations
for update to authenticated
using (owner_user_id = auth.uid())
with check (owner_user_id = auth.uid());

create policy user_syllabus_mutations_delete_own
on public.user_syllabus_mutations
for delete to authenticated
using (owner_user_id = auth.uid());
