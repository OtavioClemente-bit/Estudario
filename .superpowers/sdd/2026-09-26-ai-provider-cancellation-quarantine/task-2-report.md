# Task 2 — migration state model, safe backfill, constraints, and ACL

## Result

Implemented and pushed one additive local migration for the provider delivery state, quarantine marker, safe legacy classification, reconciliation queue schema, private resolution audit schema, state checks, and backend RPC ACLs. No cloud database, Edge deployment, secret, scheduler, Android code, or real job was accessed or changed. The reference UUID `ca9588a7-ee0e-45bb-9fb8-5b602ef1d3d9` appears only as a rolled-back local pgTAP fixture.

- Worktree: `C:\Users\otavi\.codex\worktrees\ia-api\vc-x20`, branch `codex/ia-api`.
- Base: `ed4ccb95757407721329d281292533b2b41d6f0f`.
- Task 2 commit: `42c719ded7a662558c7869aa0b6a9771677cf51d` (`feat(db): add AI provider quarantine state and safe backfill`).
- Push: `git push origin HEAD:refs/heads/codex/ia-api` succeeded, `ed4ccb9..42c719d`; the branch was exactly one commit ahead before push.

## Changed files

- `supabase/migrations/20260926202442_ai_provider_quarantine_state.sql` — generated with `npx supabase migration new ai_provider_quarantine_state`; adds `provider_start_outcome text NOT NULL DEFAULT 'NOT_STARTED'`, nullable quarantine timestamp, reconciliation queue lease/deadline schema, private audit schema, an owner-only idempotent backfill routine, validated provider state checks, indexes, compatible claim/mark/persist RPC bodies, and final explicit ACL statements.
- `supabase/tests/database/ai_provider_quarantine_backfill_test.sql` — exercises the actual classifier on rolled-back legacy fixtures, including the reference UUID and idempotent replay.
- `supabase/tests/database/ai_provider_quarantine_test.sql` — checks default inserts and the deployed four-argument create/reserve shape, state combinations and validation, lease-bound mark/persist, accepted-ID retrieve reclaim, terminal claim exclusion, and no false recoverability without an ID.
- `supabase/tests/database/ai_provider_quarantine_grants_test.sql` — verifies active and revoked overloads, source metadata service access, owner RPC grants, and private queue/audit tables.
- `supabase/tests/database/ai_foundation_test.sql`, `ai_syllabus_cancellation_test.sql`, `ai_syllabus_worker_round1_test.sql` — align existing fixtures with the new mark/persist/ACCEPTED sequence. The foundation concurrency fixture also stops taking quota and job locks in reverse order, which produced a reproducible local deadlock during the full suite.

No function TypeScript, Android, config, Cron, or cloud-job changes were made. The extra backfill test file and updates to three existing pgTAP files are scope additions needed for independent transactional fixture replay and a green full database suite.

## State and compatibility details

- The NOT NULL default is installed with the column, before any later statement can create a row. New create/reserve calls that omit the field therefore get `NOT_STARTED`; response ID and quarantine remain NULL.
- Backfill precedence is response ID → `ACCEPTED`; otherwise no start timestamp → `NOT_STARTED`; otherwise nonterminal → `LEGACY_AMBIGUOUS` with quarantine; otherwise terminal → `LEGACY_AMBIGUOUS` without active quarantine. Legacy `provider_result_recoverable` is cleared when no ID exists. It preserves status, reservation state, quota counts, consumption/release timestamps, reconciliation timestamp, response ID, and `updated_at`.
- The reference fixture remains `PROCESSING`, reservation `RESERVED`, `LEGACY_AMBIGUOUS`, quarantined, with NULL response ID, recoverability, and reconciliation; reserved quota remains 3 for the four-fixture test setup. The fixture is rolled back.
- Constraints are added `NOT VALID` and validated after backfill. They enforce the outcome domain, `NOT_STARTED` and `ACCEPTED` tuples, quarantine of nonterminal uncertain states, terminal-only `NOT_SENT`, no terminal active quarantine, and no `provider_result_recoverable=true` without a response ID.
- The active `claim_ai_job(uuid,text,integer)` signature and valid-job behavior remain available to `service_role` only. It now excludes cancelled/quarantined or non-`NOT_STARTED` reservations. The worker claim excludes cancellation and quarantine but retains `ACCEPTED` jobs with known IDs so an expired lease can be reclaimed for retrieve rather than another provider start.
- `mark_ai_job_provider_execution_started` uses the existing row-locking lease assertion and writes `IN_FLIGHT` plus quarantine atomically after checking cancellation and canonical state. `persist_ai_job_provider_response` preserves the existing argument/return shape, attaches the same ID as `ACCEPTED`, and clears quarantine atomically; divergent IDs conflict.
- The existing `get_ai_syllabus_source_metadata(uuid,text)` stays `service_role` only; unused legacy worker-claim and reconciliation overloads remain revoked. The deliberate authenticated create/cancel grants are retained. New queue and audit tables have RLS enabled and no direct runtime table grants; the backfill helper has no runtime execute grant.

## TDD and local verification

All Supabase commands that support it used `--local`; migration filename generation has no local flag. No `--linked`, project ref, remote URL, deployment, or secret command was used. Local PostgreSQL reported 17.6 in the frozen inventory; the migration uses no PG17-specific syntax.

1. Wrote the initial pgTAP tests before the migration. `npx supabase test db --local supabase/tests/database/ai_provider_quarantine_test.sql supabase/tests/database/ai_provider_quarantine_grants_test.sql` exited 1: `function public.ai_backfill_provider_start_outcome() does not exist` (the grant test had eight assertions pass before stopping at the same missing function). This is the captured initial RED.
2. Generated the migration using `npx supabase migration new ai_provider_quarantine_state`, then applied it with `npx supabase migration up --local` (exit 0). Early focused runs exposed a pgTAP fixture transaction/DDL conflict. Splitting backfill replay into its own test file resolved the harness issue; the three focused files first passed 40/40.
3. The full suite exposed older fixtures that wrote IDs without the new outcome/mark sequence; those test fixtures were updated. A foundation two-session test repeatedly deadlocked because one session held the quota row while the other held the job row. Moving its sleep after the real create call removed the reversed lock acquisition without changing production quota code.
4. During review, a new accepted-ID reclaim test failed RED (`have: NULL`, expected the accepted job UUID). The worker claim was narrowed to the safe `NOT_STARTED`/`ACCEPTED` set and then passed.
5. A backfill timestamp preservation test failed RED (`have: 0`, `want: 4` matching fixtures). The transition guard now retains `updated_at` only for the narrowly authorized internal backfill, then passed.
6. A recoverability test failed RED: `record_ai_job_provider_reconciliation(..., true)` on a quarantined, ID-less job raised no exception and changed evidence. The validated ID/recoverability check now rejects that write with SQLSTATE `23514` and leaves the row unchanged.
7. Final `npx supabase db reset --local --yes` exited 0 and applied the migration from scratch. Final focused `npx supabase test db --local` on the three new files passed **51/51**. Final full `npx supabase test db --local` passed **375/375 across 13 files**. Rate-limit notices in the concurrency test were expected; no test failed.
8. Focused staged diff review found seven intended files. `git diff --check` and `git diff --cached --check` exited 0. The untracked `deno.lock`, `node_modules/`, `package-lock.json`, `package.json`, `supabase/.branches/`, and `supabase/.temp/` artifacts were not staged.

## Risks and follow-on scope

- The reconciliation queue and resolution audit are schema scaffolding in Task 2; queue executor, recovery persistence, atomic `NOT_SENT`, and administrative resolution RPCs remain in the later approved tasks. Cron remains paused. Quarantined ambiguous jobs hold their reservations until those follow-on routes are implemented and verified.
- The old Edge worker may still attempt a reconciliation write inconsistent with the new ID rule after an uncertain start. The database rejects it and retains quarantine/reservation, preventing a false recoverability claim or automatic new generation. The later worker task must handle that rejection through the approved outcome state machine.
- Only local behavior and catalog ACLs were verified. No cloud migration or deployed Edge version was inspected or changed.

## Fix round 1 — Task 2 review findings

- Reworked the foundation quota concurrency fixture to lock the existing quota usage row explicitly before launching two competing create/reserve calls. The test waits until both database sessions report a lock wait, releases the held quota lock, and retains the one-job, one-reservation, and one-quota-unit assertions. Both requests acquire locks in the create/reserve RPC's quota-before-job order, avoiding the prior reverse-order deadlock. Synchronization is based on observed lock state, with bounded condition polling rather than a sleep used as the sole synchronization.
- Added a valid terminal `FAILED + NOT_SENT + provider_quarantined_at NULL + openai_response_id NULL` tuple assertion.
- Added `anon` and `authenticated` execution-denial assertions for the current lease-bound `claim_ai_syllabus_worker_job(text,text,integer,integer)` and `persist_ai_job_provider_response(uuid,text,text,text,bigint)` signatures. Existing service-role-only ACLs remain unchanged.
- Focused local pgTAP run passed: **181/181 across 3 files** (`ai_foundation_test.sql`, `ai_provider_quarantine_test.sql`, and `ai_provider_quarantine_grants_test.sql`). The initial run exposed incomplete consumption of the asynchronous dblink result; the fixture was corrected and rerun successfully.
- Full `npx supabase test db --local` passed: **397/397 across 13 files**. Expected rate-limit notices appeared in the concurrency test.
- `git diff --check` passed. Changes are confined to the three test files and this report appendix. Preexisting untracked local environment artifacts were not staged. No Task 3 work, cloud access, or secret access occurred.
