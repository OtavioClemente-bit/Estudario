# AI Provider Cancellation and Quarantine Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox syntax for tracking.

**Goal:** Make provider start, cancellation, recovery, quota release, and administrative resolution crash-safe and auditable without replaying an uncertain OpenAI Responses creation.

**Architecture:** PostgreSQL remains the durable state machine and quota authority. Lease-bound worker RPCs linearize cancellation against provider start; explicit outcome/quarantine fields govern generation and a separately leased reconciliation queue. The existing backend worker drains reconciliation first, while a service-role-only administrative RPC provides audited, idempotent resolution.

**Tech Stack:** Supabase Postgres migrations and pgTAP; Deno Edge Functions; TypeScript worker/provider tests; existing Supabase Edge deployment and paused Cron configuration.

**Spec:** `docs/superpowers/specs/2026-09-26-ai-provider-cancellation-quarantine-design.md` (approved at `181e29f3625b1936c1e1dc4d76da19a968864f0d`).

## Global Constraints

- Keep `gpt-6-luna`, `AI_DEFAULT_MODEL`, and server-only `OPENAI_API_KEY` behavior.
- Keep Android and public API contracts unchanged unless an existing internal response requires no client-visible change.
- Keep backend/worker/admin RPCs inaccessible to `anon` and `authenticated`; only deliberate owner-scoped client RPCs remain authenticated.
- Do not replay `provider.start()` after any uncertain start outcome; `NOT_SENT` is terminalized atomically.
- Do not release quota from timeout, lease expiry, or ambiguous provider state.
- Keep the real job `ca9588a7-ee0e-45bb-9fb8-5b602ef1d3d9` unmodified manually; its internal migration backfill is explicitly allowed and must preserve `PROCESSING` + `RESERVED`.
- Keep Cron paused throughout this plan. No task enables or changes its schedule.
- Preserve `ai-syllabus-worker`'s `verify_jwt = false` plus required custom `AI_WORKER_AUTH_TOKEN` Bearer validation; this is not a public endpoint. Preserve `ai-syllabus-jobs` and `ai-syllabus-cancel` verify_jwt settings independently.
- Never record provider body, prompt, PDF, base64, PII, JWT, API key, Authorization header, or arbitrary error text.
- Do not deploy code or apply migrations to cloud until a separate explicit deployment authorization.

## Review Focus

- Crash after provider 2xx but before ID persistence: recovery persists only the same ID; lease expiry never causes another start.
- Crash at `NOT_SENT`: one transaction leaves either the pre-existing quarantined state or a terminal job with one quota release.
- Concurrent cancellation/start and admin resolution/worker response: row-lock order and lease rules determine one safe result.
- Reconciliation retries and duplicate worker invocations: exclusive reconciliation lease, bounded backoff, and no `provider.start()`.
- Legacy rows and overload ACLs: backfill satisfies constraints before they are validated; every retained claim signature enforces the same exclusions.

---

## Read-only baseline inventory (performed before implementation)

Inventory was taken from local migrations and `supabase/functions` on branch `codex/ia-api` at approved spec commit `181e29f`. Cloud ACLs are not inferred from local migrations; a later authorized deployment validation must query the target project independently.

### Existing columns and current consumers

| Field | Existing definition / SQL use | TypeScript use found | Planned treatment |
|---|---|---|---|
| `provider_execution_started_at` | Added in `202609230001_ai_foundation.sql`; used by `202609230002_ai_security_and_rpcs.sql` guards/release/finalization; set by cancellation migration `202609240010_ai_syllabus_cancellation.sql`; set/persisted in `202609240008_ai_syllabus_worker_lease_cleanup.sql`. | `_shared/job-finalizer.ts` selects/maps it; `ai-syllabus-worker/index.ts` maps it. | Preserve as timestamp evidence only; remove decision-making based solely on it from release/cancel/finalize paths. |
| `provider_result_recoverable` | Foundation column; read by foundation trigger/release; written by `202609240007_ai_syllabus_worker.sql`, lease worker reconciliation/failure RPCs, cancellation reconciliation/finalize functions. | `_shared/job-finalizer.ts` selects/maps and sends reconciliation value; worker currently mostly writes it through RPCs. | Preserve column; legacy ambiguous rows become `NULL`; new value true only with confirmed recoverable result. |
| `openai_response_id` | Foundation column; read/written in security/release/finalization, worker lease migration, and cancellation migration. | `_shared/job-finalizer.ts` selects/maps/sends ID; worker maps and passes ID to persist/finalize. | Preserve as provider object identity; accepted state requires it; never overwrite a different ID. |
| `processing_deadline_at` | Added by `202609240008_ai_syllabus_worker_lease_cleanup.sql`; initialized during normal claim and used for worker expiration. | `ai-syllabus-worker/index.ts` maps and checks it. | Preserve for normal processing; reset to NULL on attach/recovery; reconciliation gets a separate deadline. |
| `cancellation_requested_at` | Added and used in `202609240010_ai_syllabus_cancellation.sql`; cancellation request and finish paths inspect/update it. | Worker/finalizer expose existing request/cancel operations; cancellation Edge Function delegates to shared finalizer. | Preserve client endpoint/contract; claims and start linearization enforce it; backend queue handles known IDs. |

### New-row semantics after migration

Every new `ai_jobs` row—whether inserted through the currently deployed Edge bundle during rollout or through the new bundle afterward—must be born as `provider_start_outcome = 'NOT_STARTED'`, `provider_quarantined_at = NULL`, and `openai_response_id = NULL`. Use a database `NOT NULL DEFAULT 'NOT_STARTED'` for the outcome (or prove an equivalent atomic DB mechanism); the nullable quarantine/ID fields remain null until a valid transition. Install the default before backfilling existing rows, then override legacy rows according to the evidence table. Constraints must hold immediately for new inserts.

### Existing RPCs, overloads, callers, and ACL baseline

| RPC/signature found | Current caller / ACL from local migrations | Planned treatment |
|---|---|---|
| `claim_ai_syllabus_worker_job(text, integer)` | Defined in `202609240007_ai_syllabus_worker.sql`; superseded and revoked from `PUBLIC`, `anon`, `authenticated`, `service_role` by `20260925154028_revoke_legacy_ai_rpc_overloads.sql`. No current TypeScript caller found. | Keep revoked. Reconfirm no caller in inventory task; do not restore grants. |
| `claim_ai_syllabus_worker_job(text, text, integer, integer)` | Lease-bound definition in `202609240008_ai_syllabus_worker_lease_cleanup.sql`; `service_role` only in `20260925152639_ai_security_definer_grant_hardening.sql`. | Update filters for cancellation/quarantine/terminal; preserve service-only ACL. |
| `mark_ai_job_provider_execution_started(uuid,text,text,bigint)` | Definition updated in `202609240010_ai_syllabus_cancellation.sql`; worker store calls it; `service_role` only. | Make the row-locked linearization point and persist `IN_FLIGHT` + quarantine. |
| `persist_ai_job_provider_response(uuid,text,text,text,bigint)` | Lease-bound definition in `202609240008_ai_syllabus_worker_lease_cleanup.sql`; worker store calls it; `service_role` only. | Persist `ACCEPTED` + ID atomically; preserve same-ID idempotency/conflict semantics. |
| `record_ai_job_provider_reconciliation(uuid,text,text,bigint,boolean)` | Lease-bound worker RPC; worker calls it; `service_role` only. | Update to record confirmed evidence consistently with canonical outcome; no inferred recoverability. |
| `record_ai_job_provider_reconciliation(uuid,boolean)` | Legacy overload revoked from all runtime roles in `20260925154028_revoke_legacy_ai_rpc_overloads.sql`. | Keep revoked; add regression ACL assertion. |
| `assert_ai_job_lease(uuid,text,text,bigint)` | Worker calls; `service_role` only. | Preserve and ensure shared locking/validity conventions where required. |
| `finalize_ai_job_success_with_lease(uuid,jsonb,jsonb,text,text,integer,text,text,text,bigint)`; `finalize_ai_job_failure_with_lease(uuid,ai_job_status,text,text,boolean,text,text,bigint)` | Worker store calls both; `service_role` only. | Preserve signatures/ACL; success requires matching accepted response ID; failure cannot release unresolved quarantine and uses canonical outcome, not timestamp. |
| `release_ai_job_reservation(uuid,ai_job_status,text,text)`; `finalize_ai_job_success(uuid,jsonb,jsonb,text,text,integer,text)`; `finalize_ai_job_failure(uuid,ai_job_status,text,text)` | Legacy functions from foundation; hardening revokes service/anon/auth from selected overloads; release retains authenticated per migration, called by owner-scoped flows. | Preserve owner-scoped public contract, but ensure internal worker/admin paths cannot bypass canonical state/quota invariants. Do not broaden access. |
| `request_ai_job_cancellation(uuid)` | Called by shared job finalizer/`ai-syllabus-cancel`; `authenticated` and `service_role`, not `anon`. | Preserve API and owner check; use same job row lock as provider-start marker. |
| `cancel_ai_job_without_provider(uuid)` | Called by shared finalizer; authenticated and service-role grants; currently checks timestamp/ID. | Update to canonical outcome/quarantine semantics and ensure no conflicting active lease. |
| `record_ai_job_cancellation_reconciliation(uuid,text,text,boolean,text)`; `finalize_ai_job_success_after_cancellation(uuid,jsonb,jsonb,text,text,integer,text)` | Called by shared cancellation finalizer; service-only per hardening migration. | Preserve signatures/ACL; update canonical outcome and same-ID conflict checks; terminal success consumes once only for the accepted response. |
| `increment_ai_job_retry(uuid)`; `record_ai_job_usage(uuid,bigint,bigint,bigint)` | Legacy non-lease overloads revoked from every runtime role by `20260925154028_revoke_legacy_ai_rpc_overloads.sql`; no current TypeScript caller found. | Keep revoked and add exact ACL tests. |
| `increment_ai_job_retry(uuid,text,text,bigint)`; `record_ai_job_usage(uuid,text,text,bigint,bigint,bigint,bigint)` | Lease-bound worker RPCs; TypeScript worker store calls; `service_role` only. | Preserve service-only and lease checks; usage/retry remains distinct from provider-start attempts. |
| `prepare_ai_job_source_cleanup(uuid,text,text,bigint)`; `complete_ai_job_source_cleanup(uuid,text,text,bigint)`; `fail_ai_job_source_cleanup(uuid,text,text,text,bigint)`; `claim_ai_job_source_cleanup(text,text,integer)` | Worker store; `service_role` only. | Preserve signatures/ACLs and keep source cleanup queue separate from provider reconciliation queue. |
| `claim_ai_job(uuid,text,integer)`; `create_or_get_ai_job_and_reserve_quota_unlimited(ai_feature,text,text,jsonb)` | Legacy RPC overloads revoked from all runtime roles by `20260925154028_revoke_legacy_ai_rpc_overloads.sql`; no current caller found in Edge Function search. | Keep revoked; no reintroduction or client grant. |

Exact field-reference file inventory from exhaustive `rg -l` over migrations and Edge TypeScript:

- `provider_execution_started_at`: `202609230001_ai_foundation.sql`, `202609230002_ai_security_and_rpcs.sql`, `202609240008_ai_syllabus_worker_lease_cleanup.sql`, `202609240010_ai_syllabus_cancellation.sql`, `_shared/job-finalizer.ts`, `ai-syllabus-worker/index.ts`.
- `provider_result_recoverable`: `202609230001_ai_foundation.sql`, `202609230002_ai_security_and_rpcs.sql`, `202609240007_ai_syllabus_worker.sql`, `202609240008_ai_syllabus_worker_lease_cleanup.sql`, `202609240010_ai_syllabus_cancellation.sql`, `_shared/job-finalizer.ts`.
- `openai_response_id`: `202609230001_ai_foundation.sql`, `202609230002_ai_security_and_rpcs.sql`, `202609240008_ai_syllabus_worker_lease_cleanup.sql`, `202609240010_ai_syllabus_cancellation.sql`, `_shared/job-finalizer.ts`, `ai-syllabus-worker/index.ts`.
- `processing_deadline_at`: `202609240008_ai_syllabus_worker_lease_cleanup.sql`, `ai-syllabus-worker/index.ts`.
- `cancellation_requested_at`: `202609240010_ai_syllabus_cancellation.sql`; current TypeScript callers invoke cancellation RPCs but do not directly select/map this column.

Relevant TypeScript consumers are `supabase/functions/ai-syllabus-worker/index.ts`, `supabase/functions/_shared/job-finalizer.ts`, `supabase/functions/ai-syllabus-cancel/index.ts`, and tests in `worker*_test.ts`, `openai-provider*_test.ts`, and cancellation/job endpoint tests. No Android consumer change is planned.

## Planned files

| File | Planned responsibility |
|---|---|
| `supabase/migrations/<generated>_ai_provider_quarantine_state.sql` | Additive outcome/quarantine and reconciliation/audit schema, safe backfill, validated constraints and ACL groundwork. Generate filename with `npx supabase migration new`; do not hand-invent timestamp. |
| `supabase/migrations/<generated>_ai_provider_quarantine_rpcs.sql` | Replace existing RPC bodies and add recovery, NOT_SENT, reconciliation, and admin-resolution RPCs; explicit ACLs. Generate a second filename through the CLI only if the task split confirms separate migrations are safer. |
| `supabase/functions/_shared/openai-provider.ts` | Provider error classes/categories, safe diagnostics fields, correlation metadata, response parsing. |
| `supabase/functions/_shared/openai-provider_test.ts` (and round1 test only if existing test organization requires it) | Deterministic HTTP and metadata assertions. |
| `supabase/functions/ai-syllabus-worker/index.ts` | Reconciliation-first invocation and normal generation state machine; Data API RPC adapter methods. |
| `supabase/functions/ai-syllabus-worker/worker_test.ts`, `worker_round1_test.ts`, `worker_store_round1_test.ts` | Ordering, crash, no-second-start, queue executor and RPC payload tests. |
| `supabase/functions/_shared/job-finalizer.ts` | Cancellation behavior and existing client-facing job response mapping; no public contract expansion unless unavoidable and approved. |
| `supabase/functions/ai-syllabus-cancel/index.ts` + existing tests | Route existing cancellation request to pending/reconciliation semantics without depending on repeated Android calls. |
| `supabase/functions/ai-syllabus-jobs/index.ts` + existing tests | Only if its job state serialization must map new internal fields; keep private fields out of public DTO. |
| `supabase/tests/database/ai_provider_quarantine_test.sql` | Backfill/invariants/transitions/concurrency/quota assertions. |
| `supabase/tests/database/ai_provider_quarantine_grants_test.sql` | Exact overload ACL assertions for retained and revoked RPCs. |
| `.superpowers/specs/2026-09-26-ai-provider-cancellation-quarantine-design.md` | Approved immutable design input; change only if implementation finds a real incompatibility and user approves. |

No Android files, secrets, production job rows, cron configuration, or deployed cloud configuration are planned for implementation commits.

## Database contract proposal

Exact Postgres types/argument order for new RPCs are finalized from the inventory in Task 1 and recorded in that task's review report before the migration starts. Names and behavioral contracts below are fixed by the approved spec; the plan does not authorize changing existing public signatures.

| RPC | Proposed signature shape | Caller | Lock / preconditions | Mutation/result / quota / lease |
|---|---|---|---|---|
| `mark_ai_job_provider_execution_started` (alter existing) | Existing `(p_job_id uuid,p_lease_owner text,p_lease_token text,p_lease_generation bigint)` | `service_role` | Lock `ai_jobs` row; `PROCESSING`, live matching lease, no cancellation, no response ID, `NOT_STARTED`, no active quarantine. | Set `IN_FLIGHT`, quarantine timestamp, logical-start timestamp. No quota change; lease retained. Stable cancellation/lease/state errors. |
| `persist_ai_job_provider_response` (alter existing) | Existing `(p_job_id uuid,p_response_id text,p_lease_owner text,p_lease_token text,p_lease_generation bigint)` | `service_role` | Lock row; matching live lease; processing; compatible `IN_FLIGHT`/quarantine; ID null or equal. | Set ID + `ACCEPTED`, clear creation quarantine, **preserve the current `processing_deadline_at` unchanged**. No quota change; lease retained. Same ID is idempotent; different ID conflicts. |
| `record_ai_job_provider_reconciliation(uuid,text,text,bigint,boolean)` (alter existing) | Preserve current signature | `service_role` | Lock row; valid live generation lease; outcome is provider-confirmed recoverability, not inference from a failed request. | Update reconciliation evidence and canonical outcome consistently; no quota change. Keep legacy `(uuid,boolean)` overload revoked. |
| `assert_ai_job_lease(uuid,text,text,bigint)` (preserve/alter body only if needed) | Preserve current signature | `service_role` | Lock/read job; match owner/token/generation and unexpired lease; terminal/state guards follow canonical status. | Return current job or stable lease-loss error; no state/quota change. |
| `recover_ai_job_provider_response` (new) | `(p_job_id uuid,p_response_id text)` | `service_role` | Lock row; `PROCESSING`, compatible `IN_FLIGHT`/`RESPONSE_AMBIGUOUS` quarantine; ID null/equal. No active lease required. | Persist same ID, `ACCEPTED`, clear quarantine, set `processing_deadline_at = NULL` so the next bounded claim establishes a fresh window. If cancellation exists, enqueue reconciliation atomically. No quota/lease mutation. Terminal/different-ID errors. |
| `record_ai_job_provider_outcome` (new) | `(p_job_id uuid,p_outcome text,p_lease_owner text,p_lease_token text,p_lease_generation bigint)`; allow only `PROVIDER_REJECTED`, `TRANSPORT_AMBIGUOUS`, `RESPONSE_AMBIGUOUS` | `service_role` | Lock row and validate live lease/state; allow `IN_FLIGHT` to listed outcome or identical replay. | Persist outcome + keep quarantine; no quota release; lease remains held until worker relinquishes/expiry. Diagnostic details are sanitized logs, not arbitrary DB payload. |
| `finalize_ai_job_provider_not_sent` (new) | `(p_job_id uuid,p_error_code text,p_lease_owner text,p_lease_token text,p_lease_generation bigint)` | `service_role` | Single row/quota lock transaction; `PROCESSING`, valid lease, `IN_FLIGHT` + quarantine, explicit proof classified pre-fetch. | Set `NOT_SENT`, negative reconciliation evidence, status `CANCELLED` iff cancellation requested else `FAILED`, release exactly once, clear lease/quarantine, terminal result. Retry returns stable original terminal result; conflicting state rejects. |
| `claim_ai_syllabus_worker_job` (alter retained overload) | Existing lease-bound `(p_lease_owner text,p_lease_token text,p_lease_seconds integer,p_processing_seconds integer)` | `service_role` | `FOR UPDATE SKIP LOCKED`; only eligible nonterminal generation jobs with no cancellation/quarantine and valid source/state. | New lease token/generation and bounded processing deadline. No quota change. Legacy `(text,integer)` remains revoked. |
| `claim_ai_job_provider_reconciliation` (new) | `(p_lease_owner text,p_lease_token text,p_lease_seconds integer default 300)` returns `public.ai_jobs` | `service_role` | Atomic `SKIP LOCKED`; only `PROCESSING`, cancellation pending, known response ID, retry due, no active reconciliation lease. | Exclusive reconciliation owner/token/generation/expiry; no generation lease or quota mutation. Empty claim returns canonical no-row. |
| `defer_ai_job_provider_reconciliation` (new) | `(p_job_id uuid,p_error_code text,p_lease_owner text,p_lease_token text,p_lease_generation bigint)` | `service_role` | Lock job; validate reconciliation lease and response ID state. | Bounded server-side capped backoff, increment retry, clear queue lease; keep PROCESSING + RESERVED. Idempotent for same lease result; never calls start. |
| `finalize_ai_job_provider_reconciliation_success` (new) | `(p_job_id uuid,p_proposal jsonb,p_warnings jsonb,p_response_id text,p_prompt_version text,p_schema_version integer,p_model_version text,p_lease_owner text,p_lease_token text,p_lease_generation bigint)` | `service_role` | Lock job/reservation/usage; validate reconciliation lease, cancellation request and same known response ID. | Finalize valid completed result, consume quota once, clear reconciliation lease; idempotent success. |
| `finalize_ai_job_provider_reconciliation_terminal` (new) | `(p_job_id uuid,p_provider_status text,p_error_code text,p_lease_owner text,p_lease_token text,p_lease_generation bigint)` | `service_role` | Lock job/reservation/usage; validate reconciliation lease and known ID; provider status allowlist. | Exact mapping: failed→FAILED, cancelled→CANCELLED, expired→EXPIRED, incomplete→FAILED/PROVIDER_INCOMPLETE; release once and clear lease. |
| `release_ai_job_reservation(uuid,ai_job_status,text,text)` (alter existing body only) | Preserve current signature and deliberate authenticated owner-scoped ACL | Authenticated owner-scoped only on Data API as current ACL; no `anon` or direct service-role grant | Lock job/reservation/usage; canonical outcome checks; reject ambiguous/quarantined release. | Only allowed terminal releases by authorized flow; quota counters move once. No new client privilege. |
| `finalize_ai_job_failure_with_lease(uuid,ai_job_status,text,text,boolean,text,text,bigint)` (alter existing body only) | Preserve current signature | `service_role` | Validate active lease and canonical state; generic failure cannot resolve quarantine; only NOT_SENT atomically releases via dedicated RPC. | Preserve existing role; no quota release for uncertain outcome; stable state error. |
| `finalize_ai_job_success_after_cancellation(uuid,jsonb,jsonb,text,text,integer,text)` (alter existing body only) | Preserve current signature | `service_role` | Lock job/reservation/usage; cancellation path and accepted same response ID; reject ID mismatch/quarantine. | Consume reservation once for valid completed proposal; clear leases, finalize SUCCEEDED. |
| `record_ai_job_cancellation_reconciliation(uuid,text,text,boolean,text)` (alter existing body only) | Preserve current signature | `service_role` | Lock job; require cancellation/known or provider-confirmed terminal context; same-ID conflict protection. | Store allowlisted reconciliation result; ambiguous remains pending/reserved; no release until terminal finalizer. |
| `resolve_ai_job_provider_quarantine` (new) | `(p_job_id uuid,p_resolution_id uuid,p_decision text,p_payload jsonb,p_operator_reference text,p_evidence_reference text)` | `service_role` only | Lock job and resolution key; validate fingerprint/replay; destructive decisions require no live generation or reconciliation lease, except confirmed terminal evidence for same known response ID. | Audit identity/fingerprint/evidence; `CONFIRM_NOT_CREATED` → CANCELLED if requested else FAILED; terminal mapping exact; `ATTACH_RESPONSE_ID` same-ID only semantics and reset deadline. Enqueue reconciliation when cancellation pending. Idempotent identical replay; divergent replay conflict. |
| `request_ai_job_cancellation(uuid)` (alter existing body only) | Preserve existing signature | `authenticated` owner-scoped + `service_role` | Lock same job row as mark-start; verify owner for authenticated. | Set request once; result reflects current state. No quota mutation; active provider/quarantine routes remain reconcilable. |
| `cancel_ai_job_without_provider(uuid)` (alter existing body only) | Preserve existing signature | Existing authenticated owner-scoped + `service_role` ACL | Lock job; use canonical outcome/ID/quarantine, not timestamp alone; avoid active worker conflict. | Terminalize/release only when no start or canonical `NOT_SENT` proves no request; otherwise return pending/reconciliation. |

All new backend RPCs revoke `PUBLIC`, `anon`, and `authenticated`, then grant only `service_role`. Existing ACLs outside explicitly affected functions remain unchanged. Legacy claim `(text,integer)` and reconciliation `(uuid,boolean)` overloads stay revoked from every runtime role. Task 1 verifies exact signatures using catalog queries and pgTAP.

## Ordered implementation tasks and checkpoints

Every task has its own failing test first, focused test run, full relevant suite, diff review, and separate commit. Dependent tasks do not run in parallel. Checkpoints are blocking: no later task starts with a failing test or unreviewed diff.

### Task 1 — Complete baseline inventory and contract lock

**Files:** no product edits; create/update only implementation-plan task ledger/report if needed.

- [ ] Enumerate all five named columns across every migration and function, including trigger functions, not only direct RPC bodies.
- [ ] Enumerate exact overloads and ACLs from local catalog after `npx supabase db reset` only if later authorized for implementation; for this planning stage, use migration definitions and grants. During implementation Task 1, query the reset local DB catalogs, never cloud.
- [ ] Search all TypeScript callers and document exact arguments/response shapes, including finalizer and cancel endpoint.
- [ ] Confirm legacy claim overload has no current TypeScript consumer and is revoked for `service_role`; confirm legacy reconciliation overload same.
- [ ] Record each Edge Function's current `verify_jwt` config/deployed value. `ai-syllabus-worker` is intentionally `verify_jwt = false` in `supabase/config.toml`; its handler requires exact custom `Authorization: Bearer <AI_WORKER_AUTH_TOKEN>`. Preserve that pair, never substitute a client JWT, and record current independent values for `ai-syllabus-jobs` and `ai-syllabus-cancel`.
- [ ] Produce a frozen inventory table containing every reference, exact signature, caller, grants, and action (modify/preserve/revoke).
- [ ] Review inventory with user at Checkpoint 0; only a real incompatibility can reopen architecture.

**Verification:** `rg` searches saved in task report; local catalog privilege assertions planned for migration tests. No code, migration, DB, or cloud mutation in this task.

### Task 2 — Migration state model, safe backfill, constraints, and ACL tests

**Files:** generated migration; `ai_provider_quarantine_test.sql`; `ai_provider_quarantine_grants_test.sql`.

- [ ] Write pgTAP tests first for all outcome/ID/quarantine/status combinations, including rejection of `NOT_STARTED` with ID/quarantine, ambiguous nonterminal without quarantine, `ACCEPTED` without ID/with quarantine, `PROCESSING + NOT_SENT + quarantine NULL`, and terminal claim exclusion.
- [ ] Write backfill fixtures asserting NOT_STARTED, ACCEPTED, LEGACY_AMBIGUOUS, and legacy `provider_result_recoverable` normalization, with unchanged status, reservation, consumed/released timestamps and reconciliation evidence.
- [ ] Add columns/enum-or-checked-text and reconciliation queue/lease fields/table plus private audit table, initially without strict constraints that reject existing rows.
- [ ] Install `provider_start_outcome` as `NOT NULL DEFAULT 'NOT_STARTED'` before any post-migration insert can occur; keep that default active throughout the old-Edge/new-Edge interval. New rows must also have `provider_quarantined_at = NULL` and `openai_response_id = NULL`.
- [ ] Backfill idempotently in precedence order: response ID => ACCEPTED; otherwise no start timestamp => NOT_STARTED; otherwise nonterminal => LEGACY_AMBIGUOUS + quarantine timestamp + recoverability NULL; terminal started/no ID => LEGACY_AMBIGUOUS without active quarantine + recoverability NULL.
- [ ] Add pgTAP: ordinary new-job insert/create receives NOT_STARTED and NULL quarantine/response ID; constraints hold immediately; no new row has NULL outcome. Then claim it into PROCESSING with a valid lease and prove mark-start accepts it. Exercise the existing create/reserve RPC request shape used by the currently deployed Edge bundle (which does not send the new field) to prove the DB default makes the migration/deploy interval safe.
- [ ] Explicitly assert the real reference job ends PROCESSING, reservation RESERVED, LEGACY_AMBIGUOUS, quarantined, ID NULL, recoverability NULL, no fabricated reconciliation, and no quota mutation.
- [ ] Add CHECK constraints as `NOT VALID` where supported, then validate only after the backfill passes; add queue/audit indexes after row semantics are stable.
- [ ] Create/replace all state-transition and queue RPC bodies only after columns/tables exist; keep new function execute grants closed during definition.
- [ ] As the final migration statements, revoke default/public grants on every affected/new function and table, grant backend RPC execution only to service_role, preserve deliberate authenticated RPCs, and assert retained/revoked overload ACLs.
- [ ] If this cannot be delivered as one additive migration safely, split state/backfill and RPC replacement into two generated migrations while preserving the same order and proving the intermediate schema works with currently deployed Edge bundles. Stop if backward compatibility cannot be proven.
- [ ] Run `npx supabase db reset` and `npx supabase test db`; review migration diff.

**Checkpoint 1:** schema/backfill/constraints/grants green. No cloud apply.

### Task 3 — Provider diagnostics and correlation metadata

**Files:** `_shared/openai-provider.ts`, `_shared/openai-provider_test.ts`.

- [ ] Add tests for distinct `NOT_SENT` pre-fetch, HTTP `PROVIDER_REJECTED`, transport ambiguity, malformed/ID-less success, valid `ACCEPTED`, and safe generic public error.
- [ ] Assert status, allowlisted `error.type`/`error.code`, validated request ID, and sanitized internal message; raw body/headers never enter logs/errors.
- [ ] Add correlation metadata to `POST /responses`: `estudario_job_id` and `feature=SYLLABUS_GENERATION`; prompt/schema version only if already available and non-sensitive. Assert exact allowlist and secret/PII/PDF sentinel absence.
- [ ] Confirm no unsupported metadata search API is invented; document metadata as external dashboard/operations correlation if programmatic search is unavailable.
- [ ] Preserve model resolution (`AI_DEFAULT_MODEL`, `gpt-6-luna`) and server-only `OPENAI_API_KEY`.
- [ ] Run focused provider tests and full Deno tests/type-check.

### Task 4 — Atomic NOT_SENT, linearized start, response persistence recovery

**Files:** migration follow-up if separation is safer; `ai-syllabus-worker/index.ts`; worker store tests; pgTAP tests.

- [ ] Add failing deterministic SQL race tests for cancellation-first and mark-start-first using explicit two-session barriers/locks (not sleeps).
- [ ] Add failing Deno tests where a local pre-fetch failure follows mark-start and must call exactly one atomic NOT_SENT operation, never generic failure-finalize then retry.
- [ ] Update mark-start under the shared job-row lock; test status/lease/cancellation/ID/outcome preconditions and stable errors.
- [ ] Persist a successful response ID as `ACCEPTED`; make same ID idempotent and different ID conflict.
- [ ] Add recovery persistence RPC and tests: initial persist failure after valid 2xx, same-ID recovery succeeds without active lease, duplicate same ID succeeds, different ID conflicts, terminal job rejects, cancellation pending enqueues reconciliation, no quota mutation.
- [ ] Ensure normal `persist_ai_job_provider_response` preserves the original `processing_deadline_at` unchanged. Only `recover_ai_job_provider_response` after quarantine and administrative `ATTACH_RESPONSE_ID` set it to NULL; the next bounded claim/reconciliation establishes a fresh window. Neither path enables normal generation claim while cancellation is pending.
- [ ] Add paired regression tests proving normal persistence keeps the old deadline while recovery/administrative attach after quarantine clears it.
- [ ] Update old timestamp-only gates in cancellation, pre-provider definitive handling, finalization/release and reconciliation to use canonical outcome tuple. Keep `provider_execution_started_at` audit-only.
- [ ] Run focused SQL and worker tests, then full Deno and pgTAP.

### Task 5 — Normal generation claim and worker outcome state machine

**Files:** migration; worker `index.ts`; `worker_test.ts`, `worker_round1_test.ts`, `worker_store_round1_test.ts`; pgTAP.

- [ ] Assert every retained claim overload excludes cancellation, quarantine, and terminal rows; assert legacy overload stays revoked from all runtime roles.
- [ ] Make normal claim return no job for quarantined/cancel-pending jobs and initialize lease/deadline only for eligible generation.
- [ ] Implement exact sequence: claim → validate canonical state/source → mark IN_FLIGHT (linearization) → one provider.start → persist ID → retrieve/process → usage/finalize.
- [ ] Inject failures at each boundary: before mark, mark denied, pre-fetch NOT_SENT, transport ambiguous, provider rejection, 2xx malformed/no ID, valid ID with persist failure, lease loss, retrieve failure, and finalization failure.
- [ ] Prove for every path after IN_FLIGHT without persisted ID there is no second `provider.start()`; valid response ID only retries same-ID persistence/retrieval.
- [ ] Run worker-specific Deno + pgTAP; review no behavior changes to provider model, quota policy or source validation.

**Checkpoint 2:** provider + atomic start/NOT_SENT/recovery + normal worker are green.

### Task 6 — Reconciliation queue, lease, executor, and bounded retry

**Files:** migration; worker `index.ts`; new reconciliation-focused test file or existing worker tests; pgTAP.

- [ ] Write SQL concurrency test with two claimers proving exactly one reconciliation lease wins.
- [ ] Add service-only claim/complete/fail RPCs; queue only known-ID cancellation-pending jobs; lease token/generation/expiry; separate reconciliation deadline; bounded retry/backoff.
- [ ] Make each worker invocation claim reconciliation first; execute retrieve/cancel/reconcile only; if empty, proceed to ordinary generation claim.
- [ ] Record ambiguous transport as pending, preserve PROCESSING and RESERVED; lease expiry/retry never releases quota.
- [ ] Finalize confirmed provider terminal state once with exact status mapping; duplicate terminal processing cannot consume/release twice.
- [ ] Prove worker using existing Cron invocation drains this queue without a new scheduler; Cron remains paused and unchanged.
- [ ] Run focused reconciliation tests, full worker Deno tests, pgTAP, ACL tests.

### Task 7 — Cancellation endpoint integration

**Files:** `_shared/job-finalizer.ts`, `ai-syllabus-cancel/index.ts`, cancellation tests, migration only if RPC update required.

- [ ] Test pre-provider cancel, `NOT_SENT`, quarantine without ID, accepted ID, provider active/completed/terminal, and ambiguous retrieve/cancel.
- [ ] Update `cancel_ai_job_without_provider` to rely on `NOT_STARTED` or already-terminalized `NOT_SENT`, not `provider_execution_started_at` alone; reject while incompatible generation lease is active.
- [ ] Keep known-ID cancellation on retrieve/cancel/reconcile path; pending/ambiguous retains quota and can be picked up by worker reconciliation queue even if Android stops calling.
- [ ] Preserve existing authenticated ownership/error mapping and avoid public contract changes; job remains pending on 202 until backend reconciliation confirms terminal outcome.
- [ ] Run cancellation Edge tests, worker reconciliation tests and related pgTAP.

**Checkpoint 3:** cancellation and backend recovery paths are deterministic, service-only where required, and green.

### Task 8 — Administrative resolution and audit

**Files:** migration; new pgTAP admin-resolution tests; worker/provider integration test fixtures only as needed.

- [ ] Test global `resolution_id` replay with identical fingerprint; divergent payload/decision conflicts without mutation.
- [ ] Validate actor/evidence allowlists and lengths (operator reference <=128 chars; evidence reference <=256 chars); audit technical identity, optional trusted operator reference, decision, fingerprint, evidence reference, server timestamp; no provider body/secrets.
- [ ] Test destructive resolution blocked while a processing lease is live, no job/audit decision/quota mutation; allowed after expiry.
- [ ] Test strict exception for provider-confirmed terminal evidence only on same known response ID; no `CONFIRM_NOT_CREATED` exception.
- [ ] Test `ATTACH_RESPONSE_ID` racing worker response: identical IDs converge, divergent IDs conflict without overwrite.
- [ ] Implement exact mappings: CONFIRM_NOT_CREATED => CANCELLED if cancellation requested else FAILED; terminal provider failed=>FAILED, cancelled=>CANCELLED, expired=>EXPIRED, incomplete=>FAILED/PROVIDER_INCOMPLETE. Release reservation once.
- [ ] Test ATTACH resets deadline and atomically enqueues reconciliation when cancellation is pending.
- [ ] Run admin-resolution pgTAP, full database suite and Deno suite.

### Task 9 — Full regression/security review and acceptance

**Files:** all implementation files from Tasks 2–8; implementation report.

- [ ] Run full Deno with type-check; full `npx supabase db reset`; full `npx supabase test db`; `git diff --check`; repository secret scan.
- [ ] Verify ACLs in local catalog and pgTAP for every exact overload; verify no new client grant, service secret in tests/logs, or public DTO leakage.
- [ ] Review failure injection/race tests and confirm no durable forbidden state or duplicate quota effect.
- [ ] Independently review full diff against approved spec; resolve every regression before deploy planning.
- [ ] Document baseline/environment gates separately; no cloud credentials are to be invented.

**Checkpoint 4:** implementation acceptance before any deployment authorization.

### Task 10 — Separately authorized deployment and remote validation

This task is a deployment runbook only after explicit user authorization; no cloud deployment is authorized by this plan.

1. Confirm current cloud migration head, deployed function versions, configured secrets, each function's current `verify_jwt` value, and Cron paused state using read-only checks.
2. Back up/record quota reservation and internal state for the reference job without mutating it; verify expected pre-deploy state.
3. Apply the additive migration first. Validate catalog/ACLs, constraints, backfill counts and specifically the reference job: `PROCESSING`, reservation `RESERVED`, `LEGACY_AMBIGUOUS`, quarantined, response ID NULL, recoverability NULL, no new reconciliation evidence.
4. Deploy the complete `ai-syllabus-worker` bundle, including shared provider code; then deploy complete `ai-syllabus-cancel` and `ai-syllabus-jobs` bundles if changed. Preserve the worker's existing `verify_jwt = false` and its required custom `AI_WORKER_AUTH_TOKEN` Bearer check; the deploy command/config must preserve this setting and must not replace it with a Supabase client JWT or remove handler authentication. Preserve each of `ai-syllabus-jobs` and `ai-syllabus-cancel` at its own current `verify_jwt` value; do not copy the worker setting to them.
5. If any Edge deploy fails, stop; do not activate Cron. Query the deployed function configuration and explicitly verify `ai-syllabus-worker.verify_jwt = false`; verify the handler still rejects absent/incorrect `AI_WORKER_AUTH_TOKEN`. Separately verify `ai-syllabus-jobs` and `ai-syllabus-cancel` retain their pre-deploy `verify_jwt` values. Run remote smoke tests against non-production/test jobs only, covering RPC grants, start/cancel linearization, provider stubs where possible, recovery, and reconciliation lease.
6. Confirm remote logs are redacted, RPCs service-role-only, Android cannot execute internal RPCs, quota behavior is exact, and Cron remains paused.
7. Only under a separate explicit decision, resolve the real job using verified provider evidence and the approved admin RPC. Never manually edit its row or invent a response ID.
8. Cron reactivation is a separate approval gate after every criterion below passes; it is not part of this plan's execution.

**No automatic deployment rollback:** prefer forward corrective migration and redeployment. Never drop new state or erase audit evidence as an initial rollback.

## Commit and rollout order

Implementation commits are separate and follow task dependencies. Each task writes its failing test first, verifies the failure, implements, reruns green, reviews its diff, then commits code and its tests together (no intentional red-test commit):

1. `feat(db): add provider quarantine state and safe backfill`.
2. `feat(ai): classify provider outcomes and add correlation metadata`.
3. `feat(db): linearize provider start and atomically finish not-sent jobs`.
4. `feat(worker): add durable provider response recovery`.
5. `feat(worker): drain leased provider reconciliation before generation`.
6. `fix(ai): route cancellation through canonical provider state`.
7. `feat(db): add audited provider quarantine resolution`.
8. `test(ai): complete provider quarantine acceptance gates`.

Deployment order after explicit authorization: migration(s) → full `ai-syllabus-worker` bundle → full `ai-syllabus-cancel` bundle → full `ai-syllabus-jobs` bundle only if required → remote ACL/state smoke checks → separate real-job resolution decision → separate Cron reactivation decision. The migration must be compatible with all currently deployed Edge versions during the interval before bundle replacement: keep existing RPC signatures, preserve response shapes, provide safe defaults for internal fields, and ensure old call paths cannot start quarantined/cancelled jobs or release ambiguous quota. Prove this using contract tests against the pre-change TypeScript adapters. If compatibility fails, stop and request a revised rollout approval; do not improvise a deploy order. If new Edge code cannot run before cron is reactivated, the old deployed bundle remains in place only during the bounded deployment interval and the cron stays paused.

## Rollback

- **Migration succeeds, Edge deploy fails:** leave Cron paused and old functions deployed. The migration must be backward-compatible with old functions: nullable/defaulted internal fields and RPC semantics should not make existing callers fail before new bundles arrive. If that cannot be guaranteed, deployment must stop before migration or use a separately approved expand/contract sequence. Prefer a forward-fix migration; do not drop columns/queue/audit or reverse backfill.
- **Partial Edge deploy:** keep Cron paused; deploy the complete missing bundle versions. Do not route generation to a mixed worker state. If old functions cannot safely operate against expanded schema, use tested compatibility guards, not destructive rollback.
- **New worker errors:** pause/keep Cron paused; old worker bundle can be redeployed only if migration guarantees old-code compatibility. Quarantined jobs remain reserved and are not force-released. Correct forward and rerun tests.
- **Unexpected backfill row:** migration should fail closed before constraints/commit, or classify as explicit unknown/quarantined without status/quota mutation only if that behavior is covered by the approved spec. Stop and report; do not manually alter rows or relax constraints.
- **Quota/audit discrepancy:** stop worker scheduling, preserve audit, reconcile via approved service-role resolution only after evidence; no direct SQL repair or manual quota mutation.
- **Cron:** never reactivate as a rollback or diagnostic action; its state is separately controlled.

## Acceptance gates before implementation authorization

- User approves this detailed plan and explicitly authorizes an execution mode (subagent-driven or native).
- Task 1 inventory resolves every current signature/caller/ACL, especially `cancel_ai_job_without_provider` timestamp use and all release/finalization paths.
- Every migration/test command is local-only until deployment authorization; no secrets/config values are fabricated.
- All plan tasks pass their focused and full gates; no unresolved feature regression.
- Reconciliation has one concrete executor (existing worker), exclusive leases, bounded retry, and no start path.
- Reference-job post-backfill state is verified without terminal/quota/provider mutation.
- Remote validation, actual job resolution, and Cron reactivation each require their own explicit approval; this plan grants none of them.
