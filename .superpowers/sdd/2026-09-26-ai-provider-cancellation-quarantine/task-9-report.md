# Task 9 — Full regression/security review and acceptance

Status: complete; local implementation acceptance passed. Task 10 remains outside authorization.

## Task 9 follow-up fixes from independent review

- Added migration `20260927010000_harden_provider_success_and_resolution_audit.sql`. A success transition now requires a nonconflicting response ID and a non-quarantined state; it atomically records `ACCEPTED` and clears quarantine. This preserves compatibility with an already-deployed pre-state-machine worker that goes from `NOT_STARTED` directly to the existing finalizer with a provider response ID. pgTAP exercises both lease-bound and cancellation success finalizers.
- The admin resolver’s underlying full-row implementation was renamed and revoked from every runtime role. A service-role-only wrapper returns an allowlisted state result, and a private trigger minimizes persisted audit snapshots on both insert and update. Existing audit snapshots are scrubbed in the migration. pgTAP verifies the stored and returned snapshot excludes proposal/source/request content and that the internal function is not runtime-executable.
- `OpenAiProviderError` now preserves a validated response ID when a 2xx payload has an unsupported/malformed status; the worker persists that ID and retrieves the same response instead of leaving a recoverable ID only in process memory.
- The worker emits structured provider diagnostics for start/retrieve/cancel with only allowlisted status, category, safe code/type, validated request ID, and standardized message. A callback makes the event contract deterministic in tests; the default runtime sink is structured `console.error` and cannot alter job state if logging fails.
- An initially reported active reconciliation-lease gap was investigated and withdrawn by the reviewer: `claim_ai_job_provider_reconciliation` atomically writes matching owner/token/generation/expiry to both queue and job leases; the terminal evidence exception is for the same known response ID and status permitted by the frozen spec. The test now explicitly sets and asserts that paired active lease. No extra lease policy was introduced.
- The reported incomplete mapping discrepancy was stale: current worker code already maps `incomplete` to `PROVIDER_INCOMPLETE`; no change was needed.

## Verification evidence

- Fresh local `npx supabase db reset --local --yes` — PASS; applied all migrations through `20260927010000_harden_provider_success_and_resolution_audit.sql`.
- Full `npx supabase test db --local` — 20 files, 616 tests, PASS.
- Full Deno with type-check and tests: `npx --yes deno test --node-modules-dir=auto --allow-env --allow-read=supabase/functions supabase/functions` — 101 passed, 0 failed. Plain dependency resolution initially could not find local `npm:@types/node`; auto node-modules resolution passed without editing manifests.
- Production entrypoint type-check: `npx --yes deno check supabase/functions/ai-syllabus-cancel/index.ts supabase/functions/ai-syllabus-worker/index.ts` — PASS.
- `git diff --check d1e000679ea6661849ec4b404fa684475ea0dfd8..HEAD` — PASS.
- Local catalog ACL inspection: `resolve_ai_job_provider_quarantine(uuid,uuid,text,jsonb,text,text)`, `claim_ai_job(uuid,text,integer)`, and `get_ai_syllabus_source_metadata(uuid,text)` are `SECURITY DEFINER`, executable by `service_role`, not by `anon` or `authenticated`. `request_ai_job_cancellation(uuid)` remains available to `authenticated` and `service_role`, not `anon`, per the owner-scoped client contract. Related pgTAP privilege suites pass.
- Tracked repository scan for plausible Supabase/OpenAI/JWT secret literals — no matches.
- Current-workspace scan excluding local dependency/runtime artifacts for plausible Supabase/OpenAI/JWT secret literals — no matches.
- `npx --yes deno fmt --check` on the changed Deno files — PASS.
- Failure-injection and concurrency cases are included in pgTAP and Deno suites; full suites pass, including provider start/cancellation race, reconciliation queue lease contention, duplicate resolution replay, quota release, same-ID attach/persist convergence, and divergent-ID conflict.

## Independent review

The full feature diff was independently reviewed at `d1e000679ea6661849ec4b404fa684475ea0dfd8..42667a10eaf86eca07d75683a781910cf6af2ff4`. Initial verdict requested changes and identified success-finalizer state, audit minimization, active reconciliation lease, and response-ID recovery concerns; it also requested structured diagnostics. The success-finalizer, audit, response-ID, and diagnostics items were fixed with regression tests. The reviewer withdrew the reconciliation-lease concern after verifying the paired-lease invariant and updated test; no extra guard was required by the approved terminal-evidence exception. The incomplete mapping report was already corrected in current code. A second pass of the fixes found no new Critical or Important issues. Accepted against the local Task 9 scope only.

## Environment and authorization gates

- All work and database checks are local. No cloud project was linked or queried for this task.
- Live/deployment validation remains a deployment/configuration gate and depends on separately authorized configuration and credentials. No credentials were invented or read.
- Cron remains paused/unchanged. The production reference job `ca9588a7-ee0e-45bb-9fb8-5b602ef1d3d9` was not accessed or modified.
- Task 10 deployment/remote validation is not authorized by this report and was not started.
