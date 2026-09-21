# Initial Setup Experience Implementation Plan
> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans when executing this plan.

**Goal:** Implement a persisted, resumable first-use setup assistant for Estudário that guides a new student from contest creation and syllabus acquisition through profile setup, real plan generation, review, and a usable first Home activity.

**Architecture:** Keep the existing visual onboarding and account choice intact, then gate the main navigation behind a dedicated `InitialSetupViewModel` and a compact Compose flow. Persist a versioned setup snapshot in DataStore, use the existing Room repository and `StudyPlanApplicationService` for real workspace creation, and reuse the existing `.estudo` validation/import, edital prompt, ChatGPT tutorial, and video components. Existing workspaces bypass the assistant; interrupted new-user sessions resume at their persisted step; deferred sessions remain accessible from Home and configuration.

**Tech Stack:** Kotlin, Jetpack Compose, Navigation Compose, DataStore Preferences, Room, existing `EstudoPackageService`, `StudyPlanApplicationService`, `StudyPlannerEngine`, Android Activity Result APIs, JUnit, existing Compose/instrumented test setup.

**Spec:** `C:\Users\otavi\.codex\attachments\fd221716-cb31-4012-bdcf-eda68eb13030\Texto colado.txt`

---

## Scope and product constraints

- The setup is a state machine, not a second copy of the full app editor.
- Every meaningful transition and draft value is persisted; rotation, process death, app close, and interrupted import return to the same logical step.
- No fake AI progress or invented syllabus data. AI routes use the existing structured edital prompt and `.estudo` import flow; internal processing is shown only while a real operation is active.
- The first implementation must fully support automatic plan generation, manual syllabus entry, valid `.estudo` import, external ChatGPT guidance, optional exam date, availability, profile, final review, completion, existing-user bypass, defer, and reopen.
- Optional banca/administrative metadata remains outside the initial path.
- Existing features and prior visual/navigation fixes remain intact.

## Task 1: Model and persist the setup state

**Files:**
- Create `app/src/main/java/br/com/estudario/domain/setup/InitialSetup.kt`
- Modify `app/src/main/java/br/com/estudario/data/preferences/AppPreferences.kt`
- Create `app/src/test/java/br/com/estudario/domain/setup/InitialSetupTest.kt`

1. Add enums for setup status, step, syllabus method, and plan method with stable serialized names.
2. Add a versioned `InitialSetupSnapshot` containing the current step, contest draft, optional role/date, syllabus method and manual subjects, profile, per-day availability, session duration, plan method, AI preference note, and last valid plan id.
3. Implement pure transition validation and a DataStore-safe codec with forward-compatible defaults.
4. Add a single persisted snapshot flow plus atomic update/reset helpers to `AppPreferences`; do not scatter uncoordinated setup keys across the store.
5. Write tests for round-trip persistence encoding, default state, legal transitions, invalid transitions, and resuming an interrupted step.

## Task 2: Add orchestration and existing-workspace detection

**Files:**
- Create `app/src/main/java/br/com/estudario/ui/setup/InitialSetupViewModel.kt`
- Modify `app/src/main/java/br/com/estudario/ui/AppViewModel.kt` if shared import/state helpers are needed
- Create/modify domain tests near `app/src/test/java/br/com/estudario/domain/`

1. Expose setup state as a `StateFlow` and provide small intent methods for each transition/draft update.
2. Reuse the existing repository, `EstudoPackageService`, and plan service; do not duplicate import or planner logic.
3. Implement real `.estudo` inspection and confirmation, including error states and retry/change/manual alternatives.
4. Implement manual subject commit with duplicate-safe names and preserve the draft until confirmation.
5. Implement deterministic plan creation from the persisted profile/availability using the existing planner service and `StudyMethodConfig.forProfile`; retain the last valid plan on later errors.
6. Track real operation states (`idle`, `loading`, `preview`, `success`, `error`) without timers or fabricated completion percentages.
7. Add a pure existing-workspace predicate covering restored backup data, contests, subjects, and plans.

## Task 3: Gate navigation, resume, defer, and reopen

**Files:**
- Modify `app/src/main/java/br/com/estudario/ui/EstudarioApp.kt`
- Modify `app/src/main/java/br/com/estudario/MainActivity.kt` only if startup hydration needs to include setup state
- Modify `app/src/main/java/br/com/estudario/ui/more/MoreScreen.kt` or its current path
- Add/update `HomeScreen` and navigation call sites for a deferred-setup CTA

1. Keep visual onboarding/account choice as-is and route new users into the setup assistant after account choice.
2. Auto-complete/bypass setup only when the persisted setup is untouched and a real workspace already exists.
3. Never bypass an in-progress setup merely because its draft contest now exists.
4. Resume the persisted step after recreation/process death; `Configurar depois` goes to MainNavigation with a clear Home CTA.
5. Add a configuration entry to reopen the assistant without duplicating the lateral menu’s existing preferences.
6. Ensure completed/existing users go directly to the current Home and do not see the wizard.

## Task 4: Build the premium, compact setup shell and contest/date steps

**Files:**
- Create `app/src/main/java/br/com/estudario/ui/setup/InitialSetupScreen.kt`
- Create `app/src/main/java/br/com/estudario/ui/setup/InitialSetupComponents.kt` if composition warrants it
- Modify theme/component files only where required for existing tokens

1. Build a responsive, scroll-safe shell with title, discreet progress, back/continue controls, clear hierarchy, dark-mode support, IME-safe fields, and long-name handling.
2. Add welcome/context, contest create/select, cargo as optional context, and optional exam-date steps.
3. Make the primary action explicit and keep deferral discreet but available.
4. Use animated transitions that do not expose internal navigation mechanics.
5. Add Compose tests for compact widths, dark theme, back navigation, and persisted draft rendering.

## Task 5: Implement syllabus acquisition and review

**Files:**
- Modify `app/src/main/java/br/com/estudario/ui/setup/InitialSetupScreen.kt`
- Reuse/modify `app/src/main/java/br/com/estudario/ui/components/PromptBuilderDialogs.kt` only for integration gaps
- Reuse `app/src/main/java/br/com/estudario/ui/components/TutorialVideoDialog.kt`
- Reuse `app/src/main/java/br/com/estudario/data/transfer/EstudoPackageService.kt` and existing file utilities

1. Present four truthful methods: direct AI prompt/import, `.estudo` import, manual entry, and ChatGPT tutorial/import.
2. For direct AI, reuse the structured edital prompt and accept only a validated `.estudo`; no unsupported in-app model is simulated.
3. For ChatGPT, provide copy/open convenience, real tutorial/video access, and the same validated import route.
4. Add a real processing/error surface with retry, choose another file, manual route, and return actions.
5. Add review with real subject/topic counts and expandable subject summaries; keep full-tree editing outside this initial flow.
6. Test preview/import success, malformed files, no-data manual validation, and retry/change behavior.

## Task 6: Implement profile, availability, and plan method

**Files:**
- Modify `app/src/main/java/br/com/estudario/ui/setup/InitialSetupScreen.kt`
- Reuse `app/src/main/java/br/com/estudario/domain/planner/StudyMethod.kt`
- Reuse `app/src/main/java/br/com/estudario/domain/planner/StudyPlanApplicationService.kt`

1. Add study profile choices, day-by-day availability, session duration, current situation, and priority signals without making irrelevant administrative fields mandatory.
2. Add automatic plan as the fully supported primary route, backed by the existing deterministic engine.
3. Add an external-AI plan route only through structured context/prompt and validated `.plano` import or a clearly explained return to automatic planning; preserve the last valid proposal.
4. Keep agenda synchronization out of this flow; it remains in the lateral menu.
5. Test profile mapping, default availability, optional exam date, plan-method selection, and plan input validity.

## Task 7: Final review, completion, and Home handoff

**Files:**
- Modify `app/src/main/java/br/com/estudario/ui/setup/InitialSetupScreen.kt`
- Modify `app/src/main/java/br/com/estudario/ui/home/HomeScreen.kt` and call sites
- Add/update `app/src/test` and Compose tests for finalization

1. Show a concise, real summary of contest, syllabus counts, profile, availability, and plan method.
2. Show the first real planned activity when available; never use placeholder content that pretends a plan exists.
3. On confirmation, atomically mark setup complete after the workspace is valid and route to the existing Home.
4. Add an elegant “Tudo pronto” state and verify Home is non-empty for a valid subject/plan.
5. Add a deferred-state Home CTA and a reopen path from configuration.

## Task 8: Verification, polish, and regression protection

**Files:**
- Add/update unit tests for setup state, codec, existing-user detection, deterministic planning, import validation, completion, and prior-plan preservation
- Add/update Compose/instrumented tests under `app/src/androidTest`

1. Run focused unit tests first, then the full debug unit suite and compile debug/instrumented Kotlin.
2. Exercise first open, local/Google account paths, contest without exam date, manual/import/AI/tutorial paths, real processing/error states, syllabus review, profile/availability, automatic plan, external-AI fallback, interruption/resume, existing-user bypass, defer/reopen, dark mode, and small-screen behavior.
3. Run `git diff --check` and inspect the final diff for accidental changes to the previously completed Home/plan/edital work.
4. Only claim runtime instrumented coverage if the connected device installs and executes tests; report device restrictions separately if installation is blocked.

## Self-review checklist

- Is every setup decision recoverable after process death?
- Does the assistant use actual repository/planner/import services instead of parallel fake implementations?
- Can a new user reach a real first activity without an exam date?
- Can an existing/restored user bypass the assistant without losing data?
- Are AI limitations explicit and free of fake progress or invented syllabus content?
- Are the screen, drawer, settings, notifications, plan, and edital fixes from the previous work preserved?
