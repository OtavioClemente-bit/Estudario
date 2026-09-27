# SDD ledger — plan: docs/superpowers/plans/2026-09-26-ai-provider-cancellation-quarantine.md

## Authorization and guardrails

- Authorized scope: Tasks 1–9 only. Task 10 remains out of scope.
- Branch: `codex/ia-api`; base before Task 1: `d1e000679ea6661849ec4b404fa684475ea0dfd8`.
- No cloud access/mutation, migration deployment, Edge deployment, secret/config changes, Cron changes, manual edits to the reference job, OpenAI calls, Android/public API/model changes.
- Preserve `gpt-6-luna`, `AI_DEFAULT_MODEL`, server-only `OPENAI_API_KEY`, worker `verify_jwt = false` and its custom `AI_WORKER_AUTH_TOKEN` check.
- Keep existing untracked environment artifacts out of commits: `deno.lock`, `node_modules/`, `package-lock.json`, `package.json`, `supabase/.branches/`, `supabase/.temp/`.

## Preflight conflict scan

| Scope | Shared files/interfaces or consistency risk | Ruling |
|---|---|---|
| Task 1 self-check | Inventory statement that legacy claim/reconciliation overloads are revoked vs local migration/catalog truth | Verify via local reset catalog and migrations before contract lock; stop on mismatch. |
| Task 2 self-check | Backfill, NOT NULL default, checks, and currently deployed Edge request compatibility | Default must precede backfill; fixture uses current create/reserve request body; only local DB. |
| Task 3 self-check | Error classifications vs later worker transitions | Define provider result taxonomy without changing persistence; Task 4/5 consume it. |
| Task 4 self-check | Normal persistence vs recovery deadline semantics | Preserve deadline in normal persist; reset only recovery/admin attach. |
| Task 5 self-check | Claims and provider start/cancel race | Use shared row-lock semantics and canonical state, do not duplicate start path. |
| Task 6 self-check | Reconciliation executor vs normal worker claims | Reconciliation claim first; never call provider start on that path. |
| Task 7 self-check | Cancellation endpoint vs worker/admin state transitions | Keep client ownership/API stable; rely on canonical outcomes and queue for known IDs. |
| Task 8 self-check | Admin resolution vs worker/cancel response races | One audited transaction, idempotency fingerprint, strict active-lease policy. |
| Task 9 self-check | Full diff/security scan vs local-only authorization | Run complete local suites and review; do not execute Task 10 or use cloud credentials. |
| Tasks 2 ↔ 4–8 | Schema foundation and later RPC replacements share database state/functions | Task 2 establishes columns/default/backfill/constraints/ACL groundwork; follow-up tasks own their specified RPC behavior. Split migration if compatibility requires it, not architecture. |
| Tasks 3 ↔ 4/5/6 | Provider error categories feed persistence, worker, and reconciliation | Task 3 locks safe provider result shape; downstream tasks consume it. |
| Tasks 4 ↔ 5 | Both touch worker start/store and migrations | Task 4 first owns linearization/recovery; Task 5 builds normal claim/outcome flow atop it. No parallel execution. |
| Tasks 4 ↔ 6/7/8 | Recovery, cancel, and attach can enqueue reconciliation | Task 4 creates persistence contract; Task 6 queue/executor; Tasks 7/8 integrate cancel/admin. |
| Tasks 5 ↔ 6 | Both may touch worker index/tests and claim semantics | Task 5 completes normal path first; Task 6 adds a distinct reconciliation-first branch. |
| Tasks 6 ↔ 7/8 | Queue completion and cancellation/admin terminalization overlap | Task 6 provides durable backend continuation; later tasks wire callers/resolution. |
| Tasks 7 ↔ 8 | Cancellation and administrative resolution share job state and quota finalization | Task 7 preserves public cancellation; Task 8 adds independent service-only resolution. |

No task-level parallelism is planned because these shared interfaces are dependency ordered.

## Rulings

- Ruling: Task 2 will follow its explicit checklist to create/replace SQL state-transition and queue RPC bodies after the schema objects exist, while Tasks 4–8 add their task-specific recovery/worker/cancel/admin behavior and regressions — this preserves the plan's literal dependency order — if this interpretation is wrong, migration work may need to be split/rebased before downstream tasks.
- Ruling: the local Supabase catalog ran PostgreSQL 17.6 while `supabase/config.toml` declares major version 15 — this is an environment/version discrepancy, not a reason to alter the approved architecture, and implementation should avoid PG17-only syntax — if that assumption is wrong, local tests may not model the intended PG15 deployment target.

## Task status

- [x] Task 1 — baseline inventory and contract lock (Checkpoint 0 approved; plan correction pushed as `ed4ccb9`)
- [x] Task 2 — migration state model, backfill, constraints, ACL tests (commits `42c719d` + `7a185b4`; review clean)
- [x] Task 3 — provider diagnostics and metadata (independent review clean; 15 focused / 77 full Deno tests green)
- [x] Task 4 — atomic NOT_SENT/start/recovery (commit `6d5dc67`)
- [x] Task 5 — normal claim and worker state machine (commit `6a662fc`)
- [x] Task 6 — reconciliation queue and executor (commit `a5db270`)
- [x] Task 7 — cancellation endpoint integration (commit `24320eb`)
- [x] Task 8 — administrative resolution and audit (local verification complete; commit pending)
- [ ] Task 9 — full regression/security review and acceptance
- [ ] Task 10 — explicitly excluded

## Per-task evidence

Task reports, review-package paths, independent review verdicts, commits, test commands/results, and push results will be appended here after each task.

- **Task 1 complete (Checkpoint 0 approved):** [task-1-report.md](task-1-report.md). Local reset and catalog/caller inventory are complete. User approved and the pushed plan correction `ed4ccb9` records `claim_ai_job(uuid,text,integer)` as active, service_role-only, consumed by `/process`, and not to be removed or opened to anon/authenticated. `get_ai_syllabus_source_metadata(uuid,text)` remains service_role-only via `SupabaseStorageSourceStore.getObject`. No remote operation occurred. Task 2 is authorized locally.
- **Task 2 implementation awaiting review:** commit `42c719d` was created and pushed to `origin/codex/ia-api` before the controller's requested pre-commit review could happen; this process deviation is documented. No next task will start until the independent review of base `ed4ccb9`..`42c719d` completes and any findings are resolved. Implementer reports pgTAP 51/51 focused and 375/375 full after fresh local reset; claims remain subject to review.
- **Task 2 complete:** implementation commit `42c719d` plus review-fix commit `7a185b4`, both pushed to `origin/codex/ia-api`. Initial review found two test coverage gaps; fix re-review marked both ADDRESSED with no important new breakage. Controller independently reran `npx supabase test db --local`: **13 files, 397 tests, PASS**; `git diff --check` and cached check passed. Existing untracked env artifacts remain untouched/unstaged. Process deviation: first commit/push preceded controller diff review; subsequent review and corrective commit completed, no history rewrite.
- **Task 3 complete:** [task-3-report.md](task-3-report.md). Provider delivery outcomes, bounded/sanitized diagnostics, and exact correlation metadata implemented. Independent final review found no findings. Focused provider tests **15/15**, complete Deno runtime suite **77/77**, production TypeScript `deno check`, formatter and diff check pass. Full test type-check attempt is blocked by missing environment package `npm:@types/node`; no package manifests/dependencies were changed. Commit `4686585` pushed to `origin/codex/ia-api`; no remote service or secrets used.
- **Task 4 preflight ruling:** the plan asks Task 4 to test administrative `ATTACH_RESPONSE_ID` deadline reset, but repository inventory confirms no `resolve_ai_job_provider_quarantine`/attach RPC exists before Task 8. To keep Task 4 tests green without inventing a parallel admin path, Task 4 will test normal persistence vs. its new recovery RPC; Task 8, which creates the administrative attach behavior, will add the paired attach-deadline regression test. This is test placement only; schema/behavior remain frozen. Cost if wrong: administrative deadline coverage lands later, before the final acceptance checkpoint.
- **Task 4 in progress:** baseline `4686585f5932c9c396803c05d1a20336be4bb17e`; no parallel implementation task dispatched.
- **Task 4 review findings / rulings:** review confirmed three existing cross-task cancellation gaps in files/RPCs outside Task 4's write scope: (a) quarantine without response ID must return pending via the endpoint; (b) accepted ID with cancellation pending after normal persistence needs durable reconciliation; (c) endpoint must not use audit timestamp as delivery evidence. Plan Tasks 6–7 explicitly own the durable queue and endpoint integration. Ruling: do not implement those future tasks early; carry all three as open acceptance items into Tasks 6–7 and require tests there before Task 9. Cost if wrong: cancellation may remain pending or fail to enqueue until those tasks land; Task 9 cannot pass while any remain. One Task 4 minor review finding requires strengthening the quota rollback test to fail after partial writes; fix/re-review before Task 4 completion.
- **Task 4 fix round 1/5:** address only the rollback-test coverage finding. The three cancellation findings remain assigned to Tasks 6–7 under the ruling above; no production fix is being pulled forward.
- **Task 4 fix round 1 complete:** strengthened the rollback test to induce a failure after job/reservation mutations and prove transaction rollback. Scoped independent re-review marked the finding addressed with no new Task 4 issue. Final evidence: focused pgTAP 35/35, full pgTAP 15 files/467 tests, Deno runtime 80/80, production `deno check`, and staged diff check pass. Three cross-task cancellation/reconciliation gaps remain open for Tasks 6–7 and block Task 9 until tested. Exactly nine intended Task 4 files are staged; local environment artifacts remain unstaged.
- **Task 4 complete:** commit `6d5dc67802e989c3326c3fc225b96efc4c31487c` (`feat(db): linearize provider start and recovery`); tests recorded above, final review clean within Task 4 scope. No push or remote operation performed. Task 5 base: `6d5dc67802e989c3326c3fc225b96efc4c31487c`.
- **Task 5 in progress:** normal generation claim and worker outcome state machine; following `task-5-brief.md` with Task 4 commit as base.
- **Task 5 complete, commit pending:** provider outcome persistence and worker no-replay behavior implemented; existing claim predicates were already correct and are now covered by explicit SQL regression tests. RED test proved classification was missing. Local reset, full Deno runtime **82/82**, worker Deno **28/28**, full pgTAP **17 files/489 tests**, worker production `deno check`, and diff check passed. See [task-5-report.md](task-5-report.md). No remote operation; untracked environment artifacts excluded.
- **Task 5 complete:** commit `6a662fc7ec20f7375da7bba739769367717fc642` (`feat(worker): persist provider delivery outcomes`). Task5 report contains files/tests/results and scope review. No push or remote operation.
- **Task 6 in progress:** reconcile queue and executor; base `6a662fc7ec20f7375da7bba739769367717fc642`.
- **Task 6 complete, commit pending:** durable service-only queue leases and worker-first reconciliation executor implemented. Added deterministic two-session claim race, no-known-ID exclusion, backoff/quota invariants, terminal mapping and lease/retry checks. RED tests confirmed missing queue drain and response-ID mismatch acceptance; both now pass. Final evidence: focused pgTAP **36/36**, worker Deno **35/35**, full pgTAP **18 files/525 tests**, full Deno **89/89**, worker `deno check`, and diff check pass. See [task-6-report.md](task-6-report.md). Cron and all remote state untouched; local artifacts excluded.
- **Task 6 committed:** `a5db2705f40189c46cccc16016dd7f517e747cf1` (`feat(worker): drain leased provider reconciliation before generation`). No push or remote operation.
- **Task 7 in progress:** cancellation endpoint integration; base `a5db2705f40189c46cccc16016dd7f517e747cf1`.
- **Task 7 complete:** commit `24320eb` (`fix(ai): persist cancellation reconciliation requests`). Endpoint now always asks the atomic owner-scoped SQL transition to decide whether local cancellation is safe; timestamp-only evidence no longer controls routing. Quarantine without a response ID returns HTTP 202 without writing false provider/cancellation reconciliation or releasing quota. `request_ai_job_cancellation` atomically inserts known-ID cancellation into the existing worker queue, preserving the authenticated/service-role-only grant and empty search path. RED tests reproduced the timestamp branch, false reconciliation, and missing queue insertion. Final evidence: cancellation/worker Deno full suite **98/98**, focused pgTAP **3 files/98 tests**, scoped `deno check`, test-file formatter, and `git diff --check` pass. See [task-7-report.md](task-7-report.md). No cloud, Cron, secrets, deployment, or real-job state touched.
Task 7: complete (commits a5db270..24320eb, tests: npx --yes deno test --no-check --allow-env --allow-read=supabase/functions supabase/functions → ok | 98 passed | 0 failed (2s))

- **Task 8 complete, commit pending:** Admin-only quarantine resolution and audit implemented. Two-session race coverage proves identical response IDs converge in either lock order while a divergent ID conflicts without overwrite. Local reset passed; focused pgTAP 42/42, race pgTAP 32/32, full pgTAP 20 files/608 tests, full Deno 98/98, and `git diff --check` passed. See [task-8-report.md](task-8-report.md). No remote environment, secrets, Cron, or reference job touched. Task 9 remains unstarted; Task 10 remains explicitly excluded.
