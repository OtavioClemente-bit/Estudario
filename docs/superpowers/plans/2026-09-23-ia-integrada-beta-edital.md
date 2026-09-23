# IA integrada do Estudário — edital e biblioteca privada Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or **superpowers:executing-plans** to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Implement the closed-beta AI syllabus flow end to end: account-gated access, atomic quota/job reservation, asynchronous PDF processing, structured editable proposal, atomic local `.estudo` application, private remote library, lossless restoration, and automatic local-to-remote synchronization.

**Architecture:** Supabase Postgres/RLS owns account identity, beta access, quota, durable jobs, private syllabus trees, and idempotent RPCs. Supabase Edge Functions expose short HTTP commands and a durable worker/poller starts and reconciles OpenAI Responses background work; the Android app only receives a proposal, lets the user review it, applies it through the official `.estudo` pipeline, and queues remote persistence through a Room outbox. Google Drive OAuth remains a separate token and feature.

**Tech Stack:** Kotlin, Jetpack Compose, Room 2.7.2, WorkManager 2.11.2, DataStore, existing `.estudo`/`EstudoPackageService`, Supabase Postgres/RLS/Storage/Edge Functions, Deno TypeScript, OpenAI Responses API with server-configured `gpt-6-luna`, Structured Outputs, and OpenAI background mode when supported by the configured model/project.

**Spec:** `docs/superpowers/specs/2026-09-23-ia-integrada-beta-edital-design.md`

## Global Constraints

- Preserve the current local-first behavior: Room remains the source of truth for the installed edital and the app must continue working offline.
- Keep Google Drive OAuth separate from the Supabase account session. Drive tokens must never be used as API authorization for the AI backend.
- Keep the OpenAI API key, Supabase service-role key, and any provider credentials server-side only.
- Every AI application must carry an explicit `targetSyllabusId`/target competition ID. Never infer the target from an imported file name or external ID.
- The model may return only an `AiSyllabusProposal`; it must never write directly to Room or remote tables.
- Apply reviewed content through the existing official `.estudo` import/application pipeline so import, replacement, rollback, and ID generation retain one source of truth.
- Create the durable job and reserve quota through the atomic `create_or_get_ai_job_and_reserve_quota()` Postgres/RPC operation.
- Treat `SUCCEEDED`, `FAILED`, `EXPIRED`, and `CANCELLED` as terminal states. Allow `RESERVED -> CANCELLED` only when no provider execution has started and release its reservation atomically; allow `PROCESSING -> CANCELLED` only after provider reconciliation confirms no completed or recoverable execution. Never release quota for a completed or recoverable provider result.
- `POST /process` must return quickly with `202 Accepted`; processing belongs to the durable worker/poller and may use OpenAI background mode.
- Bind every job to the exact Storage object and computed `sourceHash` that will be processed before any OpenAI call begins.
- Store stable `external_id`, `package_version`, and `schema_version` for remote subjects and topics so restoration returns the same identity-bearing tree.
- Commit the local `.estudo` mutation and the `PENDING` remote outbox row in one Room transaction. Show “saved in your account” only after a server acknowledgment marks the outbox row `SYNCED`.
- Treat PDF contents as untrusted source data, not instructions. Enforce server-side byte, page, file, processing, output, subject, topic, and depth limits.
- Keep quota, feature flags, and beta access server-authoritative. Use `America/Sao_Paulo` for user-facing dates and reset messaging.
- The first milestone does not add billing, public sharing, premium plans, or AI generation of study plans/content.

## Review Focus

- Prompt-injection and malformed-PDF handling: Tasks 7 and 16 must prove the PDF is treated as data and that limits fail safely.
- Cancellation after `openai_response_id` creation: Task 8 must reconcile provider state before any quota release.
- Concurrency, double taps, idempotency, and account isolation: Tasks 1, 6, 7, and 16 must cover these explicitly.
- Lossless local → private remote library → restoration: Task 12 must compare the complete identity-bearing tree, not only names and counts.
- Local success followed by network failure: Tasks 11, 12, and 16 must prove the outbox retries and does not report false synchronization.

---

## File Map

### Backend and database

- `supabase/config.toml`
- `supabase/migrations/202609230001_ai_foundation.sql`
- `supabase/migrations/202609230002_ai_security_and_rpcs.sql`
- `supabase/tests/database/ai_foundation_test.sql`
- `supabase/functions/_shared/contracts.ts`
- `supabase/functions/_shared/auth.ts`
- `supabase/functions/_shared/access-policy.ts`
- `supabase/functions/_shared/db.ts`
- `supabase/functions/_shared/errors.ts`
- `supabase/functions/_shared/limits.ts`
- `supabase/functions/_shared/openai-provider.ts`
- `supabase/functions/_shared/proposal-validator.ts`
- `supabase/functions/_shared/storage-source.ts`
- `supabase/functions/_shared/job-finalizer.ts`
- `supabase/functions/_shared/schema.ts`
- `supabase/functions/ai-access/index.ts`
- `supabase/functions/ai-syllabus-jobs/index.ts`
- `supabase/functions/ai-syllabus-worker/index.ts`
- `supabase/functions/ai-syllabus-cancel/index.ts`
- `supabase/functions/user-syllabi/index.ts`
- `supabase/tests/functions/*.test.ts`

### Android application

- `app/build.gradle.kts`, `gradle.properties`, `.gitignore`
- `app/src/main/java/.../data/local/Entities.kt`
- `app/src/main/java/.../data/local/AppDatabase.kt`
- `app/src/main/java/.../data/local/AppDao.kt`
- `app/src/main/java/.../data/ai/`
- `app/src/main/java/.../data/remote/`
- `app/src/main/java/.../data/sync/`
- `app/src/main/java/.../domain/ai/`
- `app/src/main/java/.../data/syllabus/SyllabusApplicationService.kt`
- existing `EstudoPackageService`, `InitialSetupViewModel`, `AppViewModel`, and app initialization/navigation files
- `app/src/main/java/.../ui/ai/`
- `app/src/main/java/.../ui/library/`
- `app/src/test/java/.../`
- `app/src/androidTest/java/.../`

## Implementation Tasks

### Task 1: Add the Supabase local scaffold and foundational schema

**Files:**

- Create `supabase/config.toml` entries, local environment examples, `supabase/migrations/202609230001_ai_foundation.sql`, `supabase/migrations/202609230002_ai_security_and_rpcs.sql`, and `supabase/tests/database/ai_foundation_test.sql`.

**Steps:**

- [ ] Write database tests first for table existence, enum/check constraints, indexes, ownership policies, and the allowed job state graph. Run them against the local Supabase database and confirm they fail because the schema does not exist.
- [ ] Create tables for profiles/beta access, feature flags, `ai_jobs`, quota reservations, usage, `user_syllabi`, `user_syllabus_subjects`, and `user_syllabus_topics`. Include job fields for feature, status, user, idempotency key, request fingerprint, Storage object path, source hash/bytes/pages, provider response ID, prompt/schema/model versions, proposal, warnings, lease, and timestamps.
- [ ] Encode and test the state graph explicitly: `RESERVED -> PROCESSING -> SUCCEEDED`, `RESERVED -> CANCELLED` only before provider execution starts, and `PROCESSING -> FAILED`, `PROCESSING -> EXPIRED`, or reconciled `PROCESSING -> CANCELLED`; terminal states have no outgoing transitions.
- [ ] Add `external_id`, `package_version`, and `schema_version` to remote subjects and topics, plus positions and parent relationships needed for exact restoration.
- [ ] Add private Storage buckets/policies and RLS policies so a user can access only their own source objects and private syllabi.
- [ ] Add the exact transactional RPCs: `create_or_get_ai_job_and_reserve_quota()`, `claim_ai_job()`, `release_ai_job_reservation()`, `finalize_ai_job_success()`, and `finalize_ai_job_failure()`. The first RPC must be idempotent for the same key/fingerprint and must never leave a reservation without a job.
- [ ] Add database assertions for quota concurrency, duplicate idempotency keys, terminal-state immutability, and cancellation release rules. Include a specific assertion that `RESERVED -> CANCELLED` releases quota atomically only when no provider/OpenAI response was initiated. Run `supabase db reset` and `supabase test db` until green.
- [ ] Commit as `feat: add AI beta database foundation`.

**Configuration gate:** Install/configure the Supabase CLI for local development only. No external credential is needed for this task.

### Task 2: Add Room remote association and sync-outbox migration

**Files:**

- Modify `app/src/main/java/.../data/local/Entities.kt`, `AppDatabase.kt`, `AppDao.kt`, and the study repository that owns competition persistence.
- Create Room migration tests and outbox DAO tests under `app/src/test/java/.../data/local/`.

**Steps:**

- [ ] Write a migration test from the current Room version 14 that asserts existing competitions and hierarchy data survive and that the new columns/tables are present. Run it before implementation and capture the expected failure.
- [ ] Add nullable `remoteSyllabusId` to `CompetitionEntity` without changing existing local IDs.
- [ ] Add `RemoteSyllabusSyncEntity` with operation, local syllabus ID, remote syllabus ID, source job ID, payload hash, state (`PENDING`, `SYNCED`, `FAILED`), attempt count, next attempt time, last error, and timestamps. Add a uniqueness rule for one pending mutation per logical syllabus/payload.
- [ ] Increment the Room schema version and provide the explicit migration. Keep `exportSchema = true` and update the exported schema fixture if this repository tracks it.
- [ ] Add DAO operations: `enqueueRemoteSyllabusSync`, `pendingRemoteSyllabusSync`, `markRemoteSyncAttempt`, `markRemoteSyncSynced`, `markRemoteSyncFailed`, and `competitionByRemoteSyllabusId`.
- [ ] Make DAO tests pass for migration, idempotent enqueue, retry ordering, and remote-ID lookup. Commit as `feat: add private syllabus association and sync outbox`.

**Dependency:** Task 1 defines the remote identifiers and payload concepts; contract tests may use deterministic local fixtures until Task 12 is available.

### Task 3: Define shared job, proposal, and private-library contracts

**Files:**

- Create `supabase/functions/_shared/contracts.ts`, `schema.ts`, and versioned fixtures.
- Create Android `data/ai/AiModels.kt`, `data/remote/RemoteSyllabusModels.kt`, and serialization tests.

**Steps:**

- [ ] Write contract tests first for a valid proposal, warnings, ambiguous source text, nested topics, stable external IDs, and malformed/unknown fields. Verify they fail before the schema exists.
- [ ] Define the versioned proposal shape with `schemaVersion`, `promptVersion`, `modelVersion`, syllabus name, subjects, positions, topics/children, source pages, and typed warnings (`code`, `severity`, `message`, `sourcePages`, `ambiguity`). Do not include definitive local Room IDs.
- [ ] Define job status, quota, access, private-library, and sync acknowledgment DTOs with the same field names and nullability on Deno and Kotlin sides.
- [ ] Configure strict JSON/schema validation so an invalid provider payload is a failed job, never a partially applied syllabus.
- [ ] Make Deno and Kotlin serialization tests pass, including backward rejection of unsupported schema versions. Commit as `feat: define AI proposal and private syllabus contracts`.

**Dependency:** Task 1 database columns and Task 2 local outbox fields must be reflected in these contracts.

### Task 4: Add the separate Supabase account-session boundary

**Files:**

- Modify `app/build.gradle.kts`, `gradle.properties`, and app configuration.
- Create `data/remote/SupabaseAuthRepository.kt`, `data/remote/SupabaseSessionStore.kt`, and auth tests.
- Modify app initialization/preferences only where needed to expose the new session state.

**Steps:**

- [ ] Write tests proving that the Supabase JWT is the only token exposed to AI API calls, Google Drive tokens remain in the Drive path, sign-out clears the Supabase session, and a missing session produces an unauthenticated state.
- [ ] Add the minimal client-safe Supabase configuration and repository interface: `signInWithGoogle`, `sendEmailOtp`, `verifyEmailOtp`, `signOut`, `observeSession`, and `accessToken`.
- [ ] Keep the Supabase URL and publishable/anon key client-safe; reject accidental service-role/OpenAI key configuration at build-time or startup tests.
- [ ] Make auth tests pass and commit as `feat: add separate Supabase account session`.

**Configuration gate:** The user must provide the Supabase project URL, publishable/anon key, Google provider configuration, OTP sender configuration, and the Android redirect/deep-link values before device testing. Do not place any service-role or OpenAI secret in the app.

### Task 5: Implement server access policy and quota-read endpoint

**Files:**

- Create `supabase/functions/_shared/auth.ts`, `supabase/functions/_shared/access-policy.ts`, `supabase/functions/ai-access/index.ts`, and function tests.

**Steps:**

- [ ] Write failing tests for beta-enabled and beta-disabled accounts, feature flags, quota exhausted/available, account A versus account B, and reset timestamps rendered in `America/Sao_Paulo`.
- [ ] Implement `ClosedBetaAiPolicy.canUse(userId, feature)` using server-side profile, flag, quota, and account checks. Never trust an account or quota value sent by Android.
- [ ] Implement `GET /functions/v1/ai-access` with a compact access/quota response and safe errors. Keep the endpoint read-only and independent of Google Drive authorization.
- [ ] Run the Deno function tests and commit as `feat: add server AI access policy`.

**Dependency:** Task 1 schema and Task 4 session are required before an authenticated end-to-end request can be exercised.

### Task 6: Create atomic jobs, reserve quota, and bind the actual source

**Files:**

- Create `supabase/functions/_shared/storage-source.ts`, `job-finalizer.ts`, `supabase/functions/ai-syllabus-jobs/index.ts`, and job endpoint tests.
- Extend the database tests from Task 1 where necessary.

**Steps:**

- [ ] Write failing tests for create-job idempotency, conflicting reuse of an idempotency key, account isolation, missing/foreign Storage objects, unsupported MIME, oversized bytes/pages/files, and provider-call prevention when source binding fails.
- [ ] Implement `POST /ai-syllabus/jobs`: authenticate the Supabase JWT, validate the requested feature and limits, generate the user-scoped Storage object path, and call `create_or_get_ai_job_and_reserve_quota()` so job creation and reservation are one transaction.
- [ ] Implement source validation that checks Storage ownership and metadata, downloads/streams only within limits, computes the server SHA-256, counts pages/files, and persists the exact object path plus `sourceHash`, byte/page counts, and source metadata before processing.
- [ ] Implement `POST /ai-syllabus/jobs/{jobId}/process` as a short command that verifies the job is reserved and the source is bound, transitions it to `PROCESSING`, schedules/queues durable work, and returns `202 Accepted` with `jobId` and status. It must not perform the long PDF/OpenAI operation inline.
- [ ] Make job endpoint tests pass, including the invariant that no OpenAI provider method is called before path/hash binding is committed. Commit as `feat: add atomic syllabus jobs and source binding`.

**Dependency:** Tasks 1, 3, and 5. The Android upload client is intentionally deferred to Task 9.

### Task 7: Add the OpenAI provider, strict proposal validator, and durable worker

**Files:**

- Implement `supabase/functions/_shared/openai-provider.ts`, `proposal-validator.ts`, versioned prompt/schema resources, and `supabase/functions/ai-syllabus-worker/index.ts`.
- Add provider, worker, schema, and adversarial-PDF tests.

**Steps:**

- [ ] Write failing tests for valid structured output, schema mismatch, empty output, provider timeout, provider error, lease recovery, and a PDF fixture containing instructions that attempt to override the system prompt.
- [ ] Define the internal prompt rule that the PDF is source data, not instructions; the model must not execute or follow embedded instructions, disclose prompts, invent missing syllabus content, or use tools. Require warnings with source pages for ambiguity.
- [ ] Implement a narrow provider interface for start, retrieve, and cancel. Use the configured `gpt-6-luna`, Structured Outputs, and OpenAI background mode when enabled; persist `openai_response_id` immediately after provider creation.
- [ ] Implement the worker/poller with database lease claims, bounded retries, provider retrieval, strict validation, terminal finalization, usage capture, and cleanup. `EdgeRuntime.waitUntil` may accelerate dispatch but is not the durability mechanism.
- [ ] Ensure the worker never writes the local Android database and never publishes a partially validated proposal. Make tests pass and commit as `feat: process syllabus jobs asynchronously with OpenAI`.

**Configuration gate:** The user must add `OPENAI_API_KEY` as a Supabase secret and set the server-side `AI_DEFAULT_MODEL=gpt-6-luna` (or the approved configured model). The key must not be present in Android, logs, fixtures, or client configuration.

**Dependency:** Tasks 1, 3, 5, and 6. The worker consumes only persisted, source-bound jobs and versioned proposal contracts.

### Task 8: Implement safe cancellation and provider reconciliation

**Files:**

- Create `supabase/functions/ai-syllabus-cancel/index.ts` and extend provider/finalizer modules and tests.

**Steps:**

- [ ] Write failing tests for these exact cases:
  - `RESERVED` cancellation becomes `CANCELLED` and releases quota atomically only when no provider execution or OpenAI response was initiated.
  - A `RESERVED` job with evidence that provider execution started is not cancelled through the pre-provider path and does not release quota without provider reconciliation.
  - `PROCESSING` without a provider response ID becomes `CANCELLED` and releases quota.
  - `PROCESSING` with `openai_response_id` queries/cancels/reconciles the provider before deciding quota.
  - A completed or recoverable provider response wins over a cancellation request and consumes quota.
  - A confirmed provider cancellation with no result becomes `CANCELLED` and releases quota.
  - An unknown provider state remains recoverable/`PROCESSING` and never releases quota.
- [ ] Implement `POST /ai-syllabus/jobs/{jobId}/cancel` as an idempotent command. It must not mark a job cancelled merely because the client requested it; quota release requires backend confirmation that no completed or recoverable OpenAI execution exists.
- [ ] Make late provider completion, repeated cancellation, lease races, and already-terminal jobs deterministic. Preserve `openai_response_id` and reconciliation evidence in audit fields.
- [ ] Run cancellation tests and commit as `feat: reconcile safe AI job cancellation`.

**Dependency:** Task 7 provider lifecycle and Task 1 finalization RPCs.

### Task 9: Add Android API client, upload/hash flow, and job recovery

**Files:**

- Create `app/src/main/java/.../data/ai/AiApiClient.kt`, `AiSyllabusRepository.kt`, `AiJobRecoveryWorker.kt`, `PdfSourceReader.kt`, and their tests.
- Modify application initialization only to register the worker and repositories.

**Steps:**

- [ ] Write failing tests for source SHA-256, byte/MIME preflight, `202 Accepted`, polling/backoff, process timeout recovery, `SUCCEEDED` retrieval by `jobId`, failed-job retry with a new idempotency key, and absent-auth behavior.
- [ ] Implement the client with the Supabase JWT, `Idempotency-Key`, signed Storage upload, source metadata, and job polling. Never expose an OpenAI URL or key to Android.
- [ ] Persist enough local request metadata to recover a job after process death or network timeout without creating a new job on every retry.
- [ ] Register a WorkManager recovery worker with network constraints and bounded backoff. Make tests pass and commit as `feat: add Android AI job client and recovery`.

**Dependency:** Tasks 2, 4, and 6. The worker can be tested against fakes before a real Supabase environment is configured.

### Task 10: Validate the proposal, bind the target, and map through `.estudo`

**Files:**

- Create `app/src/main/java/.../domain/ai/AiSyllabusProposalValidator.kt`, `AiSyllabusToEstudoMapper.kt`, `AiSyllabusDraft.kt`, and tests.
- Inspect and reuse the existing `EstudoPackageService`/`StudyPlanCodec` boundary; do not create a second import format.

**Steps:**

- [ ] Write failing tests for invalid names, duplicate external IDs, duplicate siblings, cycles/depth overflow, warning retention, source/model ID mismatch, target-title override, stable external IDs, and the regression where one selected edital plus an imported file previously created two editais.
- [ ] Implement `validateDraft`, `bindToTarget`, and `toOfficialPackage`. The selected target ID is authoritative; imported file names and model-proposed names cannot create a second competition.
- [ ] Preserve hierarchy, order, topic parents, subject/topic external IDs, source-page warnings, and the package/schema versions needed by restoration.
- [ ] Make mapper tests pass against the existing `.estudo` representation and commit as `feat: map reviewed AI syllabus proposals to estudo`.

**Dependency:** Tasks 2, 3, and 9. This task remains pure until Task 11 performs the database transaction.

### Task 11: Apply the reviewed syllabus atomically and enqueue the outbox

**Files:**

- Create `app/src/main/java/.../data/syllabus/SyllabusApplicationService.kt` and tests.
- Modify `EstudoPackageService` and the relevant `AppDao`/repository transaction boundary.

**Steps:**

- [ ] Write failing tests for successful application, Room rollback on a mid-application error, explicit replacement of existing content, duplicate apply of the same source job, and the rule that a local success with remote failure remains `PENDING`.
- [ ] Implement `applyReviewedSyllabus(targetSyllabusId, draft, sourceJobId): ApplyResult`. Validate the target and draft, serialize the confirmed package through the official `.estudo` path, and make the operation idempotent for the source job.
- [ ] In one `database.withTransaction`, apply the local package, preserve the target local identity, update any known remote syllabus association, compute the remote payload hash, and insert the `PENDING` outbox mutation. Never mark it `SYNCED` optimistically.
- [ ] Make transaction and idempotency tests pass and commit as `feat: apply AI syllabus atomically with sync outbox`.

**Dependency:** Tasks 2, 3, and 10. Remote server acknowledgment is intentionally handled by Task 12.

### Task 12: Implement the private library API, outbox worker, and lossless round-trip

**Files:**

- Implement `supabase/functions/user-syllabi/index.ts` and backend library tests.
- Create Android `PrivateSyllabusRepository.kt`, `RemoteSyllabusMapper.kt`, and `RemoteSyllabusSyncWorker.kt`.
- Add the round-trip tests under Android and backend integration test directories.

**Steps:**

- [ ] Write failing RLS tests for list/read/upsert/delete by owner, cross-account denial, invalid parent/external-ID relationships, duplicate mutations, and idempotent payload hashes.
- [ ] Implement private-library upsert/read/list/delete with server validation of the complete tree. Persist and return `external_id`, `package_version`, `schema_version`, positions, parent relationships, priorities, metadata, and remote syllabus identity.
- [ ] Implement the WorkManager sync worker with network constraints, exponential backoff, attempt tracking, mutation idempotency, and explicit `SYNCED` acknowledgment. Keep failed rows available for retry and surface `FAILED` without deleting local content.
- [ ] Add the required lossless test: construct a local official syllabus tree, sync it to the private library, fetch it back, restore it locally, and compare the full structure—syllabus identity, names, ordering, priorities, all subject/topic external IDs, parent links, metadata, package/schema versions, and payload hash. The restored tree must not be merely equivalent by name and must not receive replacement IDs.
- [ ] Add local-only/offline, remote-only download, remote deletion, reinstall/download, and local deletion-with-remote-retention tests. Make tests pass and commit as `feat: sync and restore private syllabi losslessly`.

**Configuration gate:** Deploy the migrations, RLS, Storage policies, library functions, and worker scheduling to a Supabase project. Prepare a disposable test account and a beta-enabled account before device/integration testing.

### Task 13: Add Android job recovery, proposal draft, and review UI

**Files:**

- Create `app/src/main/java/.../ui/ai/` review/gate/progress state and components.
- Modify `InitialSetupViewModel`, setup screens, `EstudarioApp`, and `AppViewModel` only to thread the new state and target ID.
- Add Compose/UI tests under `app/src/androidTest/java/.../ui/ai/`.

**Steps:**

- [ ] Write failing UI tests for unauthenticated gate, login return to the same target, processing progress, app restart recovery, warnings, subject/topic counts, add/remove/edit, explicit replacement confirmation, and provider failure fallback.
- [ ] Implement the closed-beta gate and job state screen. Resume by the persisted `jobId` and idempotency key; ordinary network timeouts must not create a new job.
- [ ] Render the structured proposal as an editable draft with source-page warnings and clear validation errors. Keep the selected target syllabus visible throughout review.
- [ ] Wire “Usar este edital” to `SyllabusApplicationService`; after local success show synchronization as pending until the outbox server acknowledgment arrives.
- [ ] Run unit and Compose tests and commit as `feat: add AI syllabus review flow`.

**Configuration gate:** Device testing requires the Supabase auth values from Task 4 and a beta-enabled test account from Task 12.

### Task 14: Add “Meus editais” library and deduplication UI

**Files:**

- Create `app/src/main/java/.../ui/library/MySyllabiScreen.kt` and its ViewModel.
- Modify drawer/navigation and existing edital screens.
- Add UI tests.

**Steps:**

- [ ] Write failing tests for one combined local/remote row, remote-only download, local-only offline state, deletion of local data while retaining the private remote copy, and deletion from the account.
- [ ] Implement list merging by stable local/remote syllabus identity, not by display name. A selected target plus imported file must render as one edital.
- [ ] Add restore/download actions that use the lossless mapper from Task 12 and show pending/failed synchronization honestly.
- [ ] Make tests pass and commit as `feat: add private syllabus library UI`.

**Dependency:** Tasks 2, 11, and 12.

### Task 15: Add quota display, flags, telemetry, and safe fallback behavior

**Files:**

- Create `app/src/main/java/.../ui/ai/AiAccessViewModel.kt` and tests.
- Modify profile/editais/setup surfaces and server finalizer telemetry.

**Steps:**

- [ ] Write failing tests for quota remaining/exhausted, disabled feature, non-beta account, reset date formatting, provider failure, and offline fallback.
- [ ] Implement a read-only quota/availability display with no billing, premium, or buy flows. Keep server decisions authoritative.
- [ ] Add structured telemetry for feature, user pseudonymous ID, model/prompt/schema versions, request/job ID, token usage, duration, and terminal status. Never log source documents, provider secrets, raw auth tokens, or full prompts.
- [ ] Make fallback behavior explicit: local editing/import remains available when AI access is unavailable. Commit as `feat: add beta quota UI and safe fallbacks`.

**Dependency:** Tasks 5, 7, 8, 13, and 14.

### Task 16: Run security, adversarial, integration, and acceptance verification

**Files:**

- Add backend security/acceptance tests, Android acceptance tests, and a short operational runbook under `docs/`.

**Steps:**

- [ ] Add acceptance coverage for account A/B isolation, provider failure, invalid structured output, double tap/idempotency, Android timeout after provider creation, Room failure/reapply, local deletion/remote retention, reinstall/download, duplicate target/import regression, filename mismatch, source mismatch warning, unreadable/oversized/page-limit PDF, non-beta access, and quota exhaustion.
- [ ] Add explicit cancellation acceptance tests before and after `openai_response_id` creation: before the ID, cancellation releases quota when no execution exists; after the ID, the backend reconciles provider state first, preserves a completed/recoverable result, and releases quota only when no result is or can be recovered.
- [ ] Add explicit restoration acceptance tests for complete structural restoration: same private syllabus identity, same subject/topic external IDs, exact order, parent links, priorities, metadata, versions, and no name-based deduplication or regenerated identity.
- [ ] Verify JWT/RLS/Storage isolation, rate/size/page/payload limits, prompt-injection resistance, safe errors, and absence of secrets with `git diff --check` and repository scans.
- [ ] Run the complete commands:
  - `supabase db reset`
  - `supabase test db`
  - `deno test supabase/tests/functions`
  - `./gradlew testDebugUnitTest`
  - `./gradlew connectedDebugAndroidTest`
  - `./gradlew :app:assembleDebug`
  - `rg -n -i 'OPENAI_API_KEY|service_role|sk-[A-Za-z0-9]' app supabase --glob '!**/build/**' --glob '!**/.gradle/**'`
- [ ] If an instrumented device blocks installation with `INSTALL_FAILED_USER_RESTRICTED`, record it as an environment limitation, keep unit/backend evidence separate, and rerun on an approved emulator/device before release.
- [ ] Commit as `test: verify closed beta AI syllabus flow` only after all required checks are green or the remaining environment blocker is explicitly documented.

## Credential and deployment checkpoints

1. **After Task 4:** provide Supabase project URL, publishable/anon key, Google provider settings, OTP sender settings, Android redirect/deep-link configuration, and a test account.
2. **Before Task 7 integration:** configure `OPENAI_API_KEY` as a Supabase secret and `AI_DEFAULT_MODEL=gpt-6-luna`; confirm the selected project/model supports the intended background and structured-output behavior.
3. **Before Task 12/16 staging:** deploy migrations, RLS, private Storage policies, Edge Functions, worker scheduling/queue, feature flags, quota values, and beta-access rows.
4. **Before device acceptance:** use a disposable beta account, a non-beta account, and a second account to verify isolation; use a known PDF fixture and an adversarial fixture.

## Plan self-review checklist

- [ ] Confirm every task is small enough to execute independently and has explicit dependencies.
- [ ] Confirm migrations and database tests precede backend contracts, backend behavior precedes Android UI, and every milestone has tests before implementation.
- [ ] Confirm the three requested additions are explicit: safe quota release on cancellation, lossless local/private/restoration round-trip, and pre-provider binding to actual Storage object plus `sourceHash`.
- [ ] Confirm acceptance criteria cover cancellation after `openai_response_id` and full structural restoration.
- [ ] Confirm the plan contains no product implementation changes, unfinished sections, or unresolved architecture decisions outside the frozen specification.
