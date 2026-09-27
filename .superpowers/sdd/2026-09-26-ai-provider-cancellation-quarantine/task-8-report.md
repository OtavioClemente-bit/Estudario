# Task 8 — Administrative provider-quarantine resolution

Status: implementation and local verification complete; commit pending.

## Files

- `supabase/migrations/20260927004546_implement_provider_quarantine_resolution.sql`
- `supabase/tests/database/ai_provider_quarantine_resolution_test.sql`
- `supabase/tests/database/ai_provider_quarantine_resolution_race_test.sql`

## Behavior delivered

- Added service-role-only `resolve_ai_job_provider_quarantine(...)` with a fixed empty search path and explicit role check.
- Added strict decision payload validation, bounded allowlisted operator/evidence references, mandatory evidence for destructive decisions, SHA-256 request fingerprints, and globally serialized idempotent resolution IDs. Replays return the immutable original result snapshot; divergent replays conflict.
- `CONFIRM_NOT_CREATED` only resolves quarantined jobs without a response ID and without live generation/reconciliation leases. It selects CANCELLED when cancellation was requested, otherwise FAILED, and releases the reservation/counter once in the same transaction.
- `ATTACH_RESPONSE_ID` accepts a recovered ID only for quarantine, or converges an already-accepted identical ID race. It rejects a different ID, preserves an already accepted normal-path deadline, clears quarantine, and resets the deadline only when recovering from quarantine. Pending cancellation is durably enqueued.
- `CONFIRM_TERMINAL_NO_RESULT` requires the exact accepted response ID and a confirmed terminal status; it releases the reservation exactly once. The same-ID provider-terminal evidence is the only live-lease exception.
- Audit records include technical actor, optional opaque operator/evidence references, fingerprint, terminal/provider metadata, and an immutable result snapshot; no provider response body or credential is stored.
- Added explicit two-session serialization tests: worker persistence wins then same-ID attach converges; attach wins then same-ID worker persistence converges; divergent worker ID is rejected without overwriting the attached ID.

## Verification

- `npx supabase db reset --local --yes` — PASS.
- Focused resolution pgTAP — 42/42 PASS.
- Focused concurrency pgTAP — 32/32 PASS.
- Full pgTAP: `npx supabase test db --local` — 20 files, 608 tests, PASS.
- Full Deno: `npx --yes deno test --no-check --allow-env --allow-read=supabase/functions supabase/functions` — 98 passed, 0 failed.
- `git diff --check` — PASS.

## Scope and deployment gates

Local only. No cloud database, deployment, secrets, Cron, OpenAI, Android/public contract, or real job was accessed or changed. The reference production job remains untouched. This report does not authorize Task 9 or Task 10.
