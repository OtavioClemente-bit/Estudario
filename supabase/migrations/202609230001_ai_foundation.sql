create extension if not exists pgcrypto;

create type public.ai_feature as enum (
  'SYLLABUS_GENERATION',
  'PLAN_GENERATION',
  'CONTENT_GENERATION'
);

create type public.ai_job_status as enum (
  'RESERVED',
  'PROCESSING',
  'SUCCEEDED',
  'FAILED',
  'EXPIRED',
  'CANCELLED'
);

create type public.ai_quota_reservation_status as enum (
  'RESERVED',
  'CONSUMED',
  'RELEASED'
);

create table public.profiles (
  user_id uuid primary key references auth.users (id) on delete cascade,
  beta_access boolean not null default false,
  beta_access_granted_at timestamptz,
  created_at timestamptz not null default now(),
  updated_at timestamptz not null default now()
);

create index profiles_beta_access_idx
  on public.profiles (beta_access)
  where beta_access;

create table public.ai_feature_flags (
  flag_key text primary key,
  enabled boolean not null default false,
  created_at timestamptz not null default now(),
  updated_at timestamptz not null default now(),
  constraint ai_feature_flags_key_check check (
    flag_key in (
      'AI_BETA_ENABLED',
      'SYLLABUS_AI_ENABLED',
      'PLAN_AI_ENABLED',
      'CONTENT_AI_ENABLED'
    )
  )
);

insert into public.ai_feature_flags (flag_key, enabled)
values
  ('AI_BETA_ENABLED', false),
  ('SYLLABUS_AI_ENABLED', false),
  ('PLAN_AI_ENABLED', false),
  ('CONTENT_AI_ENABLED', false);

create table public.ai_quota_usage (
  user_id uuid not null references auth.users (id) on delete cascade,
  feature public.ai_feature not null,
  period_start date not null,
  successful_count integer not null default 0,
  reserved_count integer not null default 0,
  created_at timestamptz not null default now(),
  updated_at timestamptz not null default now(),
  primary key (user_id, feature, period_start),
  constraint ai_quota_usage_successful_count_check check (successful_count >= 0),
  constraint ai_quota_usage_reserved_count_check check (reserved_count >= 0)
);

create table public.ai_jobs (
  id uuid primary key default gen_random_uuid(),
  user_id uuid not null references auth.users (id) on delete cascade,
  feature public.ai_feature not null,
  status public.ai_job_status not null default 'RESERVED',
  idempotency_key text not null,
  request_fingerprint text not null,
  request_payload jsonb not null default '{}'::jsonb,
  source_object_path text,
  source_mime_type text,
  source_hash text,
  source_bytes bigint,
  source_pages integer,
  source_file_count integer,
  source_metadata jsonb not null default '{}'::jsonb,
  openai_response_id text,
  provider_execution_started_at timestamptz,
  provider_reconciled_at timestamptz,
  provider_result_recoverable boolean,
  prompt_version text,
  schema_version integer,
  model_version text,
  proposal jsonb,
  warnings jsonb not null default '[]'::jsonb,
  lease_owner text,
  lease_expires_at timestamptz,
  processing_started_at timestamptz,
  finished_at timestamptz,
  error_code text,
  error_message text,
  created_at timestamptz not null default now(),
  updated_at timestamptz not null default now(),
  constraint ai_jobs_idempotency_key_check check (length(btrim(idempotency_key)) > 0 and length(idempotency_key) <= 255),
  constraint ai_jobs_request_fingerprint_check check (length(btrim(request_fingerprint)) > 0 and length(request_fingerprint) <= 255),
  constraint ai_jobs_request_payload_object_check check (jsonb_typeof(request_payload) = 'object'),
  constraint ai_jobs_source_hash_check check (source_hash is null or source_hash ~ '^[0-9a-f]{64}$'),
  constraint ai_jobs_source_bytes_check check (source_bytes is null or source_bytes >= 0),
  constraint ai_jobs_source_pages_check check (source_pages is null or source_pages >= 0),
  constraint ai_jobs_source_file_count_check check (source_file_count is null or source_file_count >= 0),
  constraint ai_jobs_warnings_array_check check (jsonb_typeof(warnings) = 'array'),
  constraint ai_jobs_schema_version_check check (schema_version is null or schema_version > 0),
  constraint ai_jobs_succeeded_proposal_check check (status <> 'SUCCEEDED' or proposal is not null)
);

create unique index ai_jobs_user_feature_idempotency_key_idx
  on public.ai_jobs (user_id, feature, idempotency_key);

create index ai_jobs_status_lease_idx
  on public.ai_jobs (status, lease_expires_at, created_at);

create index ai_jobs_user_status_idx
  on public.ai_jobs (user_id, status, created_at desc);

create table public.ai_quota_reservations (
  id uuid primary key default gen_random_uuid(),
  job_id uuid not null unique references public.ai_jobs (id) on delete cascade,
  user_id uuid not null references auth.users (id) on delete cascade,
  feature public.ai_feature not null,
  period_start date not null,
  status public.ai_quota_reservation_status not null default 'RESERVED',
  created_at timestamptz not null default now(),
  released_at timestamptz,
  consumed_at timestamptz,
  updated_at timestamptz not null default now(),
  constraint ai_quota_reservations_terminal_timestamp_check check (
    (status = 'RESERVED' and released_at is null and consumed_at is null)
    or (status = 'RELEASED' and released_at is not null and consumed_at is null)
    or (status = 'CONSUMED' and consumed_at is not null and released_at is null)
  )
);

create index ai_quota_reservations_user_status_idx
  on public.ai_quota_reservations (user_id, status, created_at desc);

create table public.user_syllabi (
  id uuid primary key default gen_random_uuid(),
  owner_user_id uuid not null references auth.users (id) on delete cascade,
  title text not null,
  position integer not null default 0,
  visibility text not null default 'PRIVATE',
  source text not null default 'AI_GENERATED',
  source_job_id uuid unique references public.ai_jobs (id) on delete set null,
  source_hash text,
  schema_version integer not null,
  status text not null default 'ACTIVE',
  metadata jsonb not null default '{}'::jsonb,
  created_at timestamptz not null default now(),
  updated_at timestamptz not null default now(),
  constraint user_syllabi_title_check check (length(btrim(title)) > 0),
  constraint user_syllabi_position_check check (position >= 0),
  constraint user_syllabi_visibility_check check (visibility in ('PRIVATE', 'COMMUNITY_REVIEWED', 'VERIFIED', 'SUPERSEDED', 'ARCHIVED')),
  constraint user_syllabi_source_check check (source in ('AI_GENERATED', 'IMPORTED', 'MANUAL')),
  constraint user_syllabi_source_hash_check check (source_hash is null or source_hash ~ '^[0-9a-f]{64}$'),
  constraint user_syllabi_schema_version_check check (schema_version > 0),
  constraint user_syllabi_status_check check (status in ('ACTIVE', 'ARCHIVED', 'DELETED')),
  constraint user_syllabi_metadata_object_check check (jsonb_typeof(metadata) = 'object')
);

create index user_syllabi_owner_position_idx
  on public.user_syllabi (owner_user_id, position, created_at);

create table public.user_syllabus_subjects (
  id uuid primary key default gen_random_uuid(),
  syllabus_id uuid not null references public.user_syllabi (id) on delete cascade,
  external_id text not null,
  name text not null,
  position integer not null,
  suggested_priority text not null default 'NORMAL',
  package_version text not null,
  schema_version integer not null,
  metadata jsonb not null default '{}'::jsonb,
  created_at timestamptz not null default now(),
  updated_at timestamptz not null default now(),
  constraint user_syllabus_subjects_external_id_check check (length(btrim(external_id)) > 0),
  constraint user_syllabus_subjects_name_check check (length(btrim(name)) > 0),
  constraint user_syllabus_subjects_position_check check (position >= 0),
  constraint user_syllabus_subjects_priority_check check (suggested_priority in ('LOW', 'NORMAL', 'HIGH')),
  constraint user_syllabus_subjects_package_version_check check (length(btrim(package_version)) > 0),
  constraint user_syllabus_subjects_schema_version_check check (schema_version > 0),
  constraint user_syllabus_subjects_metadata_object_check check (jsonb_typeof(metadata) = 'object'),
  constraint user_syllabus_subjects_external_id_key unique (syllabus_id, external_id),
  constraint user_syllabus_subjects_position_key unique (syllabus_id, position),
  constraint user_syllabus_subjects_id_syllabus_key unique (id, syllabus_id)
);

create index user_syllabus_subjects_syllabus_external_id_idx
  on public.user_syllabus_subjects (syllabus_id, external_id);

create index user_syllabus_subjects_syllabus_position_idx
  on public.user_syllabus_subjects (syllabus_id, position);

create table public.user_syllabus_topics (
  id uuid primary key default gen_random_uuid(),
  subject_id uuid not null references public.user_syllabus_subjects (id) on delete cascade,
  external_id text not null,
  parent_topic_id uuid,
  name text not null,
  position integer not null,
  package_version text not null,
  schema_version integer not null,
  metadata jsonb not null default '{}'::jsonb,
  created_at timestamptz not null default now(),
  updated_at timestamptz not null default now(),
  constraint user_syllabus_topics_external_id_check check (length(btrim(external_id)) > 0),
  constraint user_syllabus_topics_name_check check (length(btrim(name)) > 0),
  constraint user_syllabus_topics_position_check check (position >= 0),
  constraint user_syllabus_topics_package_version_check check (length(btrim(package_version)) > 0),
  constraint user_syllabus_topics_schema_version_check check (schema_version > 0),
  constraint user_syllabus_topics_metadata_object_check check (jsonb_typeof(metadata) = 'object'),
  constraint user_syllabus_topics_external_id_key unique (subject_id, external_id),
  constraint user_syllabus_topics_id_subject_key unique (id, subject_id),
  constraint user_syllabus_topics_parent_same_subject_fk
    foreign key (parent_topic_id, subject_id)
    references public.user_syllabus_topics (id, subject_id)
    on delete cascade
);

create index user_syllabus_topics_subject_external_id_idx
  on public.user_syllabus_topics (subject_id, external_id);

create index user_syllabus_topics_parent_position_idx
  on public.user_syllabus_topics (subject_id, parent_topic_id, position);

create unique index user_syllabus_topics_sibling_position_idx
  on public.user_syllabus_topics (
    subject_id,
    coalesce(parent_topic_id, '00000000-0000-0000-0000-000000000000'::uuid),
    position
  );

alter table public.profiles enable row level security;
alter table public.ai_feature_flags enable row level security;
alter table public.ai_quota_usage enable row level security;
alter table public.ai_jobs enable row level security;
alter table public.ai_quota_reservations enable row level security;
alter table public.user_syllabi enable row level security;
alter table public.user_syllabus_subjects enable row level security;
alter table public.user_syllabus_topics enable row level security;
