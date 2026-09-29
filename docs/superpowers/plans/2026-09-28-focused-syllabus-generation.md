# Focused syllabus generation Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Generate only the syllabus sections applicable to the selected destination title, and fail safely with a clear Android message when the PDF cannot be matched.

**Architecture:** Reuse the existing Android `targetTitle`, transmit and fingerprint it at job creation, and recover it from persisted `request_payload` in the worker. Introduce syllabus proposal schema v2 with an explicit match status; accept content only for `MATCHED`, and finalize `NOT_FOUND`/`AMBIGUOUS` as terminal failures without a proposal. Preserve readers for v1 proposals/jobs and keep existing endpoint authentication, RLS, storage, quotas, Cron, and retry behavior.

**Tech Stack:** Kotlin, Android/JUnit, TypeScript, Deno, Supabase Edge Functions and existing Postgres RPCs.

**Spec:** `docs/superpowers/specs/2026-09-28-focused-syllabus-generation-design.md`

## Global Constraints

- Target title maximum: 200 Unicode code points; normalize to NFC and reject blank/control-character input.
- PDF and target are untrusted data; prompt rules remain system-owned and the target is passed in a separate task-context field.
- Only `MATCHED` with at least one applicable subject can succeed; all other match states produce terminal failure with no proposal.
- Do not create a new schema migration; `request_payload` already persists the job request and claim RPC returns the `ai_jobs` record.
- Do not change provider choice, quota rules, Cron, global processing limits, worker retries, RLS, endpoint auth, or storage visibility.
- Do not automatically retry a job or initiate a paid real generation; do not commit unless the user asks.
- Preserve successful v1 job/proposal decoding and local recovery data.

## Review Focus

- Generic destination titles such as “Meu edital” must not yield a guessed match; test `AMBIGUOUS` and the friendly terminal message.
- Other TRT-3 roles and specialties must not leak into Técnico Judiciário — TI; cover with a controlled fixture and explicit section assertions.
- Reusing an idempotency key with a different target must conflict; cover normalized target in the fingerprint.
- A PDF that contains instructions to ignore the target must not change selection; test prompt isolation and provider contract.
- A legacy v1 succeeded job must remain readable and recoverable after introducing v2; add compatibility tests in both TypeScript and Kotlin.

---

### Task 1: Carry the selected title through authenticated job creation

**Files:**
- Modify: `app/src/main/java/br/com/estudario/data/ai/AiApiClient.kt`
- Modify: `app/src/main/java/br/com/estudario/data/ai/AiSyllabusRepository.kt`
- Modify: `supabase/functions/ai-syllabus-jobs/index.ts`
- Test: `app/src/test/java/br/com/estudario/data/ai/AiApiClientTest.kt`
- Test: `app/src/test/java/br/com/estudario/data/ai/AiSyllabusRepositoryTest.kt`
- Test: `supabase/functions/ai-syllabus-jobs/index_test.ts`

**Interfaces:**
- `AiApiClient.createOrGetJob(idempotencyKey, source, sourceReady, targetTitle)` sends the existing selected title.
- Creation body carries `target: { title }`; server validates, NFC-normalizes, trims, limits to 200 code points, rejects controls/blank, stores normalized target in `request_payload`, and includes it in the request fingerprint.
- Missing target is allowed only for legacy-compatible requests and is stored as an explicit unknown marker that the worker must fail as ambiguous.

- [ ] **Step 1: Write failing Android and Edge Function tests**
  - Android asserts the JSON body includes the exact target title and repository resume/retry reuses the saved title.
  - Edge Function asserts normalization, size/control/blank rejection, persistence, and that changing target conflicts under the same idempotency key.
- [ ] **Step 2: Run focused tests and confirm the new assertions fail for the missing target behavior.**
- [ ] **Step 3: Implement the parameter threading and server target validation/fingerprinting.**
- [ ] **Step 4: Run `./gradlew.bat testDebugUnitTest --tests br.com.estudario.data.ai.AiApiClientTest --tests br.com.estudario.data.ai.AiSyllabusRepositoryTest` and `npx.cmd --no-install deno test --no-check --quiet supabase/functions/ai-syllabus-jobs/index_test.ts`.**

### Task 2: Version and validate the matched-target proposal contract

**Files:**
- Modify: `supabase/functions/_shared/contracts.ts`
- Modify: `supabase/functions/_shared/schema.ts`
- Create: `supabase/functions/_shared/schemas/ai-syllabus-proposal-v2.ts`
- Modify: `supabase/functions/_shared/proposal-validator.ts`
- Modify: `app/src/main/java/br/com/estudario/data/ai/AiModels.kt`
- Modify: `app/src/main/java/br/com/estudario/domain/ai/AiSyllabusProposalValidator.kt`
- Test: shared proposal/schema compatibility tests and `app/src/test/java/br/com/estudario/domain/ai/AiSyllabusProposalValidatorTest.kt`

**Interfaces:**
- New v2 proposal fields: `schemaVersion: 2` and `targetMatch: "MATCHED" | "NOT_FOUND" | "AMBIGUOUS"`.
- `MATCHED` requires at least one subject. `NOT_FOUND` and `AMBIGUOUS` require zero subjects. Version 1 decoding remains supported and has no targetMatch.
- Add `CURRENT_AI_SYLLABUS_SCHEMA_VERSION = 2` separately from `CURRENT_AI_SCHEMA_VERSION = 1`; only syllabus proposals/jobs may use 1 or 2. Other AI feature contracts and the remote `.estudo` schema remain at version 1.

- [ ] **Step 1: Add failing tests for v2 matched, not-found, ambiguous, invalid combinations, and v1 compatibility.**
- [ ] **Step 2: Run the shared Deno and Kotlin contract tests to observe the expected failures.**
- [ ] **Step 3: Add schema selection and validation that enforce targetMatch/content consistency while retaining v1 parsers.**
- [ ] **Step 4: Run focused Deno schema/proposal tests and `./gradlew.bat testDebugUnitTest --tests br.com.estudario.domain.ai.AiSyllabusProposalValidatorTest`.**

### Task 3: Focus extraction and fail closed in the worker

**Files:**
- Create: `supabase/functions/_shared/prompts/syllabus-v2.ts`
- Modify: `supabase/functions/_shared/prompts/syllabus-v1.ts` only if needed to retain old-job compatibility
- Modify: `supabase/functions/ai-syllabus-worker/index.ts`
- Test: `supabase/functions/ai-syllabus-worker/worker_test.ts`
- Test: adversarial prompt and worker contract tests

**Interfaces:**
- Worker job includes a validated target title parsed from the claimed record's `request_payload`; no transient request state is required.
- `syllabusUserPrompt(targetTitle)` provides target in a distinct delimited task-context section and labels all PDF content as untrusted.
- `MATCHED` flows through normal validator/success finalizer. `NOT_FOUND` maps to `TARGET_NOT_FOUND`; `AMBIGUOUS` or missing/unknown legacy target maps to `TARGET_AMBIGUOUS`. These paths finalize failure with a friendly safe message, no proposal, and no sensitive content in logs.

- [ ] **Step 1: Write failing worker tests for target recovery from request_payload, target-specific prompt, successful matched TI filtering, no-match/ambiguous terminal state, and legacy unknown target.**
- [ ] **Step 2: Run focused worker tests and confirm they fail on the current whole-PDF extraction behavior.**
- [ ] **Step 3: Implement `syllabus-v2`, validated job target parsing, target-focused prompt, and terminal handling before proposal persistence.**
- [ ] **Step 4: Run focused worker and shared provider tests; run `npx.cmd --no-install deno check --no-lock` on changed Edge Function entrypoints.**

### Task 4: Show match failures clearly and preserve Android recovery compatibility

**Files:**
- Modify: `app/src/main/java/br/com/estudario/ui/ai/AiReviewViewModel.kt`
- Modify: `app/src/main/java/br/com/estudario/data/ai/AiModels.kt`
- Test: relevant `AiReviewViewModelTest.kt`, `AiApiClientTest.kt`, and repository recovery tests

**Interfaces:**
- `TARGET_NOT_FOUND` renders “Não encontrei no PDF o conteúdo do edital para o cargo/área selecionado. Confira o nome do edital e tente novamente.”
- `TARGET_AMBIGUOUS` renders “Não consegui confirmar o cargo/área neste PDF. Use um nome de edital que indique o cargo e a especialidade e tente novamente.”
- Terminal failures clear the active session as existing FAILED behavior does; recovery stays on the same job and persisted title.
- Old v1 succeeded responses still decode and open review; no auto-apply or generation retry is introduced.

- [ ] **Step 1: Add failing ViewModel tests for both error mappings, terminal session cleanup, same-target recovery, and legacy v1 success rendering.**
- [ ] **Step 2: Run focused ViewModel tests and confirm expected failures.**
- [ ] **Step 3: Implement friendly error mapping and compatibility behavior.**
- [ ] **Step 4: Run relevant Android unit tests and `./gradlew.bat assembleDebug`.**

### Task 5: Cross-boundary verification and review

**Files:** all files listed above; no further behavior changes unless verification finds a defect.

- [ ] **Step 1: Run complete relevant Deno suite for jobs, contracts, proposal validator, and worker.**
- [ ] **Step 2: Run complete Android unit test suite and debug build.**
- [ ] **Step 3: Review diff for target injection, unsafe content logging, auth/RLS/storage changes, schema compatibility, and accidental provider/quota/Cron/retry changes.**
- [ ] **Step 4: Run `git diff --check`; leave changes uncommitted.**
- [ ] **Step 5: Report any live provider test as pending until the user manually starts one; never claim end-to-end completion from mocked tests.**
