# Plano de Estudos Adaptativo Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Entregar o fluxo completo de criação, geração, execução, replanejamento, exportação e mesclagem de planos adaptativos sem alterar histórico acadêmico nem quebrar `.estudo`, edital, questões ou backups existentes.

**Architecture:** Um domínio Kotlin puro produz `PlanningProposal` determinística a partir de `StudyPlannerSnapshot`. Uma camada de aplicação monta snapshots e aplica propostas no Room v5 com revisão otimista; execução real fica append-only e separada. UI Compose e DTOs `.plano` dependem dessas interfaces, mas o motor não depende de Android, Room, JSON ou Compose.

**Tech Stack:** Kotlin 2.2.20, Android/Compose Material 3, Room 2.7.2, coroutines/Flow, `org.json`, JUnit 4, Room MigrationTestHelper e Compose UI Test.

**Spec:** `docs/superpowers/specs/2026-09-15-plano-estudos-adaptativo-design.md`

## Global Constraints

- Preservar integralmente o banco Room v4 e todos os fluxos atuais.
- Usar minutos líquidos inteiros para capacidade e planejamento.
- Não adicionar API de IA, rede, pagamentos, nuvem ou gamificação.
- `StudyPlannerEngine` não pode importar classes Android, Room, Compose ou JSON.
- Replanejamento nunca altera ou exclui `StudyTaskExecutionEntity`.
- Tarefas concluídas e itens `locked` são imutáveis para o motor.
- `PlanningProposal.baseRevision` deve coincidir com `StudyPlanEntity.revision` na aplicação.
- IDs numéricos do Room não são portáveis; `.plano` resolve edital por `externalId`.
- Histórico local sempre prevalece sobre planejamento importado.
- A barra principal final deve ser Início, Edital, Plano, Treinar e Mais.
- O projeto continua compilando para Java 17.
- Não inicializar nem fazer commit no repositório Git vazio localizado acima do workspace. Só executar etapas de commit caso `git rev-parse --show-toplevel` retorne exatamente o diretório `vc-x20`.

---

### Task 1: Estabelecer a linha de base com Java 17

**Files:**
- Inspect: `README.md`
- Inspect: `build.gradle.kts`
- Inspect: `app/build.gradle.kts`
- Inspect: `gradle/wrapper/gradle-wrapper.properties`
- Create after successful verification: `docs/superpowers/baselines/2026-09-15-before-adaptive-plan.md`

**Interfaces:**
- Consumes: Gradle 8.14 e alvo JVM 17 já configurados.
- Produces: linha de base reproduzível com resultados de `test`, `lint` e `assembleDebug` antes de código funcional.

- [ ] **Step 1: Localizar ou instalar um JDK 17**

Run:

```powershell
Get-ChildItem 'C:\Program Files','C:\Users\otavi\.jdks' -Filter java.exe -Recurse -ErrorAction SilentlyContinue |
  Where-Object { $_.FullName -match '17|jbr' } |
  Select-Object -First 10 -ExpandProperty FullName
```

If no JDK 17 is present, run:

```powershell
winget install --id EclipseAdoptium.Temurin.17.JDK --exact --silent --accept-package-agreements --accept-source-agreements
```

Expected: a `java.exe` from JDK 17 is available.

- [ ] **Step 2: Select Java 17 only for the current shell and verify it**

Run after resolving the installation directory:

```powershell
$jdk17 = Get-ChildItem 'C:\Program Files\Eclipse Adoptium' -Directory -Filter 'jdk-17*' |
  Sort-Object Name -Descending |
  Select-Object -First 1 -ExpandProperty FullName
$env:JAVA_HOME = $jdk17
$env:Path = "$jdk17\bin;$env:Path"
java -version
```

Expected: version starts with `17`.

- [ ] **Step 3: Run the existing unit suite**

Run:

```powershell
.\gradlew.bat test --stacktrace
```

Expected: PASS. If an existing test fails, record its exact task and failure without changing application behavior during this step.

- [ ] **Step 4: Run existing static analysis and build**

Run:

```powershell
.\gradlew.bat lintDebug assembleDebug --stacktrace
```

Expected: both tasks complete. Record pre-existing warnings separately.

- [ ] **Step 5: Record the baseline**

Create `docs/superpowers/baselines/2026-09-15-before-adaptive-plan.md` with the exact Java version, Gradle commands, pass/fail counts, lint issues, APK result and the previously observed Java 25 incompatibility.

- [ ] **Step 6: Create a repository checkpoint if an isolated Git repository exists**

Run:

```powershell
$root = git rev-parse --show-toplevel 2>$null
if ($root -eq (Get-Location).Path) {
  git add docs/superpowers/baselines/2026-09-15-before-adaptive-plan.md
  git commit -m "docs: record adaptive planner baseline"
} else {
  Write-Output 'Checkpoint skipped: workspace is not an isolated Git repository.'
}
```

Expected in the current environment: checkpoint skipped.

---

### Task 2: Criar os tipos puros e cálculos fundamentais do planejador

**Files:**
- Create: `app/src/main/java/br/com/estudario/domain/planner/PlannerModels.kt`
- Create: `app/src/main/java/br/com/estudario/domain/planner/StudyPlanProgressCalculator.kt`
- Create: `app/src/main/java/br/com/estudario/domain/planner/StudyPlanForecastCalculator.kt`
- Create: `app/src/test/java/br/com/estudario/domain/planner/PlannerCalculatorsTest.kt`

**Interfaces:**
- Consumes: `java.time.LocalDate`, `java.time.DayOfWeek`; no Android types.
- Produces: `StudyPlannerSnapshot`, `PlanningProposal`, `PlannerTask`, `TaskDemand`, `DailyCapacity`, `PlanMetrics`, `ForecastResult`, enums and stable IDs used by every later task.

- [ ] **Step 1: Write failing tests for weekly capacity, honest progress and forecast**

Add tests with these assertions:

```kotlin
@Test fun `weekly capacity sums only available liquid minutes`() {
    val availability = DayOfWeek.entries.associateWith { day ->
        DailyCapacity(day, if (day == DayOfWeek.SUNDAY) 0 else 120, unavailable = day == DayOfWeek.SUNDAY)
    }
    assertEquals(720, availability.values.sumOf { it.effectiveMinutes })
}

@Test fun `progress keeps schedule adherence separate from syllabus coverage`() {
    val metrics = StudyPlanProgressCalculator.calculate(
        plannedMinutes = 240,
        completedPlannedMinutes = 120,
        actualMinutes = 180,
        questions = 20,
        correct = 15,
        syllabusCoveragePercent = 30,
    )
    assertEquals(50, metrics.adherencePercent)
    assertEquals(75, metrics.accuracyPercent)
    assertEquals(30, metrics.syllabusCoveragePercent)
    assertEquals(60, metrics.overtimeMinutes)
}

@Test fun `forecast uses usable capacity after recurring reservations`() {
    val result = StudyPlanForecastCalculator.forecast(
        start = LocalDate.of(2026, 9, 15),
        remainingMinutes = 1_920,
        weeklyCapacityMinutes = 1_200,
        recurringReservedMinutes = 240,
    )
    assertEquals(LocalDate.of(2026, 9, 29), result.estimatedDate)
    assertEquals(960, result.usableWeeklyMinutes)
}

@Test fun `forecast is unavailable when usable capacity is zero`() {
    val result = StudyPlanForecastCalculator.forecast(
        LocalDate.of(2026, 9, 15), 600, 300, 300,
    )
    assertNull(result.estimatedDate)
    assertEquals(ForecastUnavailableReason.NO_USABLE_CAPACITY, result.unavailableReason)
}
```

- [ ] **Step 2: Run the calculator tests and verify RED**

Run:

```powershell
.\gradlew.bat testDebugUnitTest --tests "br.com.estudario.domain.planner.PlannerCalculatorsTest"
```

Expected: FAIL because the planner types and calculators do not exist.

- [ ] **Step 3: Define the domain contract**

Implement these exact public types in `PlannerModels.kt`:

```kotlin
enum class PlanPriority { CRITICAL, HIGH, MEDIUM, LOW }
enum class PlanTaskType { THEORY, QUESTIONS, REVIEW, ACTIVE_RECALL, FLASHCARDS, SIMULATION, DISCURSIVE }
enum class PlanTaskStatus { PLANEJADA, EM_ANDAMENTO, CONCLUIDA, REPROGRAMADA, NAO_REALIZADA, PAUSADA }
enum class PlanOrigin { ENGINE, MANUAL, IMPORTED, REVIEW_SCHEDULE, MASTER_PLAN }
enum class ReplanReason { INITIAL, AVAILABILITY_CHANGED, DAY_MISSED, TASK_PARTIAL, TASK_COMPLETED, TASK_SKIPPED, SUBJECT_CHANGED, SYLLABUS_CHANGED, IMPORTED, PLAN_ACTIVATED }

data class DailyCapacity(val dayOfWeek: DayOfWeek, val minutes: Int, val unavailable: Boolean = false) {
    val effectiveMinutes: Int get() = if (unavailable) 0 else minutes.coerceAtLeast(0)
}

data class TaskExecution(
    val id: String,
    val taskId: String?,
    val subjectId: Long?,
    val topicId: Long?,
    val date: LocalDate,
    val minutes: Int,
    val questions: Int,
    val correct: Int,
)

data class PlannerTask(
    val id: String,
    val planId: String,
    val subjectId: Long?,
    val topicId: Long?,
    val date: LocalDate,
    val type: PlanTaskType,
    val plannedMinutes: Int,
    val plannedQuestions: Int,
    val priority: PlanPriority,
    val status: PlanTaskStatus,
    val locked: Boolean,
    val replannedFromTaskId: String? = null,
)

data class PlanningProposal(
    val id: String,
    val planId: String,
    val baseRevision: Long,
    val preservedTaskIds: Set<String>,
    val transitions: List<TaskTransition>,
    val newTasks: List<PlannerTask>,
    val capacity: CapacityReport,
    val explanations: List<PlanningExplanation>,
    val forecast: ForecastResult,
)
```

Add focused value types for subject demand, topic performance, review demand, day override, annual phase, monthly goal, weekly goal, task transition, capacity report and explanations. Constructors must reject negative minutes, questions or correct counts above question counts.

- [ ] **Step 4: Implement calculators minimally**

`StudyPlanProgressCalculator.calculate` must compute adherence from completed planned minutes, accuracy only when questions are nonzero, actual minutes independently, and overtime separately. `StudyPlanForecastCalculator.forecast` must use ceiling division by usable weekly capacity and return an explicit unavailable reason when no date can be justified.

- [ ] **Step 5: Run tests and verify GREEN**

Run:

```powershell
.\gradlew.bat testDebugUnitTest --tests "br.com.estudario.domain.planner.PlannerCalculatorsTest"
```

Expected: PASS.

- [ ] **Step 6: Run all existing unit tests**

Run:

```powershell
.\gradlew.bat testDebugUnitTest
```

Expected: PASS.

---

### Task 3: Implementar geração determinística, capacidade e limites por matéria

**Files:**
- Create: `app/src/main/java/br/com/estudario/domain/planner/StudyPlannerEngine.kt`
- Create: `app/src/main/java/br/com/estudario/domain/planner/PlannerScoringPolicy.kt`
- Create: `app/src/test/java/br/com/estudario/domain/planner/StudyPlannerEngineAllocationTest.kt`

**Interfaces:**
- Consumes: tipos da Task 2.
- Produces: `StudyPlannerEngine.plan(snapshot, reason): PlanningProposal` e `PlannerScoringPolicy` configurável.

- [ ] **Step 1: Write failing tests for determinism, critical maintenance and insufficient capacity**

Cover these behaviors with fixed dates and IDs:

```kotlin
@Test fun `same snapshot always creates identical proposal content`() {
    val first = engine.plan(snapshot22Hours(), ReplanReason.INITIAL)
    val second = engine.plan(snapshot22Hours(), ReplanReason.INITIAL)
    assertEquals(first.copy(id = "proposal"), second.copy(id = "proposal"))
}

@Test fun `critical subject receives maintenance without monopolizing flexible time`() {
    val proposal = engine.plan(snapshotWithCriticalAndMaintenanceSubjects(), ReplanReason.INITIAL)
    val bySubject = proposal.newTasks.groupBy { it.subjectId }.mapValues { row -> row.value.sumOf { it.plannedMinutes } }
    assertTrue(bySubject.getValue(SECURITY_ID) >= 120)
    assertTrue(bySubject.getValue(SECURITY_ID) <= 480)
    assertTrue(bySubject.getValue(PORTUGUESE_ID) > 0)
}

@Test fun `capacity deficit is explicit and no day exceeds availability`() {
    val proposal = engine.plan(snapshotWithDemand(minutes = 1_500, weeklyCapacity = 1_080), ReplanReason.INITIAL)
    assertEquals(420, proposal.capacity.deficitMinutes)
    proposal.newTasks.groupBy { it.date }.forEach { (date, tasks) ->
        assertTrue(tasks.sumOf { it.plannedMinutes } <= proposal.capacity.availableByDate.getValue(date))
    }
}
```

- [ ] **Step 2: Run allocation tests and verify RED**

Run:

```powershell
.\gradlew.bat testDebugUnitTest --tests "br.com.estudario.domain.planner.StudyPlannerEngineAllocationTest"
```

Expected: FAIL because the engine is absent.

- [ ] **Step 3: Implement deterministic scoring**

Create `PlannerScoringPolicy` with versioned integer weights for strategic priority, overdue review, exam proximity, weakness, time without study, released dependency and recent saturation. Require a minimum question sample before weakness contributes and cap weakness bonus per week.

Sort equal scores by deadline, subject position, topic position and stable ID. Generate proposal IDs from a stable SHA-256 digest of `planId`, `revision`, reason and normalized snapshot content rather than random UUIDs.

- [ ] **Step 4: Implement two-pass allocation**

First reserve eligible minimum maintenance. Then allocate flexible demand by score, respecting:

- effective daily minutes;
- locked-day capacity;
- existing locked tasks;
- dependency completion;
- splittable task types;
- default four-week horizon;
- configured per-subject flexible cap, default 40%.

Return unallocated demand in `CapacityReport` and add explanations for deficit or cap exceptions.

- [ ] **Step 5: Run allocation tests and verify GREEN**

Run:

```powershell
.\gradlew.bat testDebugUnitTest --tests "br.com.estudario.domain.planner.StudyPlannerEngineAllocationTest"
```

Expected: PASS.

- [ ] **Step 6: Run domain regression tests**

Run:

```powershell
.\gradlew.bat testDebugUnitTest --tests "br.com.estudario.domain.*" --tests "br.com.estudario.domain.planner.*"
```

Expected: PASS.

---

### Task 4: Implementar replanejamento, parcial, bloqueios e alertas Mestre

**Files:**
- Modify: `app/src/main/java/br/com/estudario/domain/planner/StudyPlannerEngine.kt`
- Create: `app/src/main/java/br/com/estudario/domain/planner/MasterPlanMonitor.kt`
- Create: `app/src/test/java/br/com/estudario/domain/planner/StudyPlannerEngineReplanTest.kt`
- Create: `app/src/test/java/br/com/estudario/domain/planner/MasterPlanMonitorTest.kt`

**Interfaces:**
- Consumes: engine e modelos das Tasks 2–3.
- Produces: replanejamento somente do futuro, `MasterPlanAlert` e saldo sucessor auditável.

- [ ] **Step 1: Write failing tests for the mandatory replanning scenarios**

Add separate tests for:

- changing every available day from 240 to 120 minutes;
- increasing availability without moving completed tasks;
- missing a day without stacking its full remainder on the next day;
- completing early;
- locked day;
- locked task;
- partial execution of 25/60 producing exactly 35 pending minutes;
- completed tasks remaining byte-for-byte equal in preserved IDs;
- successor task pointing to `replannedFromTaskId`.

Use this core assertion for partial work:

```kotlin
val proposal = engine.plan(snapshotWithPartialExecution(planned = 60, actual = 25), ReplanReason.TASK_PARTIAL)
assertEquals(35, proposal.newTasks.single { it.replannedFromTaskId == "task-1" }.plannedMinutes)
assertEquals(PlanTaskStatus.REPROGRAMADA, proposal.transitions.single { it.taskId == "task-1" }.to)
assertEquals(25, snapshot.executions.single().minutes)
```

- [ ] **Step 2: Run replan tests and verify RED**

Run:

```powershell
.\gradlew.bat testDebugUnitTest --tests "br.com.estudario.domain.planner.StudyPlannerEngineReplanTest"
```

Expected: FAIL on missing replan behavior.

- [ ] **Step 3: Implement eligibility and remainder rules**

Freeze CONCLUIDA, locked tasks, locked days and every execution. For EM_ANDAMENTO tasks, compute `max(0, plannedMinutes - executedMinutes)` once. Mark the original REPROGRAMADA and create only the positive successor demand. Do not mutate the input snapshot.

Redistribute missed work across available future days using the same ranking and capacity limits from Task 3.

- [ ] **Step 4: Run replan tests and verify GREEN**

Run:

```powershell
.\gradlew.bat testDebugUnitTest --tests "br.com.estudario.domain.planner.StudyPlannerEngineReplanTest"
```

Expected: PASS.

- [ ] **Step 5: Write and run a failing Master Plan alert test**

```kotlin
@Test fun `critical master subject warns after configured inactivity`() {
    val alerts = MasterPlanMonitor.evaluate(
        today = LocalDate.of(2026, 9, 15),
        inactivityThresholdDays = 7,
        subjects = listOf(MasterSubject(10, "Segurança da Informação", PlanPriority.CRITICAL)),
        lastExecutionBySubject = mapOf(10L to LocalDate.of(2026, 9, 6)),
    )
    assertEquals(9, alerts.single().inactiveDays)
    assertFalse(alerts.single().blocking)
}
```

Run the test and confirm it fails before implementing `MasterPlanMonitor.evaluate`.

- [ ] **Step 6: Implement and verify Master Plan alerts**

Return non-blocking alerts only for essential subjects at or beyond the configured threshold. Include subject ID, captured name, inactivity days and last execution date.

Run:

```powershell
.\gradlew.bat testDebugUnitTest --tests "br.com.estudario.domain.planner.*"
```

Expected: PASS.

---

### Task 5: Adicionar schema Room v5 e migração aditiva

**Files:**
- Create: `app/src/main/java/br/com/estudario/data/local/planner/PlannerEntities.kt`
- Create: `app/src/main/java/br/com/estudario/data/local/planner/PlannerRelations.kt`
- Create: `app/src/main/java/br/com/estudario/data/local/planner/PlannerDao.kt`
- Modify: `app/src/main/java/br/com/estudario/data/local/Converters.kt`
- Modify: `app/src/main/java/br/com/estudario/data/local/AppDatabase.kt`
- Modify: `app/src/androidTest/java/br/com/estudario/data/local/AppDatabaseMigrationTest.kt`
- Generate: `app/schemas/br.com.estudario.data.local.AppDatabase/5.json`

**Interfaces:**
- Consumes: domain enums from Task 2 and Room v4 schema.
- Produces: Room v5, `plannerDao()`, all additive tables, indices and triggers.

- [ ] **Step 1: Extend the migration test first**

Create a v4 database containing one competition, subject, topic, theory, question, attempt, review and study session. Migrate with `MIGRATION_4_5`. Assert all original row counts and representative values are unchanged, planner tables are empty, and schema validation passes.

Add persistence assertions that raw SQL cannot leave two active plans or two Master plans for one competition and cannot keep an archived plan active.

- [ ] **Step 2: Run the migration test and verify RED**

Run with an emulator/device:

```powershell
.\gradlew.bat connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=br.com.estudario.data.local.AppDatabaseMigrationTest
```

Expected: FAIL because v5 and `MIGRATION_4_5` do not exist. If no device exists, run `assembleDebugAndroidTest` to verify compilation and mark runtime execution pending until Task 13.

- [ ] **Step 3: Define focused Room entities**

Create entities matching the approved spec:

- `study_plans`;
- `study_plan_revisions`;
- `study_availability`;
- `study_day_overrides`;
- `plan_subjects`;
- `annual_phases` and phase-subject/topic joins;
- `monthly_plans` and month-subject/topic joins;
- `weekly_plans`;
- `plan_tasks`;
- `plan_task_dependencies`;
- `study_task_executions`.

Use String UUID primary keys for plan-owned records. Use nullable subject/topic FKs with `SET_NULL` and plan ownership FKs with `CASCADE`. The planner UI never exposes permanent plan deletion: normal lifecycle uses archive/restore. A separately confirmed competition deletion may continue using the existing destructive cascade, so it must not become blocked by planner history.

- [ ] **Step 4: Add converters and DAO operations**

Add converters for every new enum. `PlannerDao` must expose flows for plans, current hierarchy, tasks by date/range, executions by range, and one-shot snapshot reads. Add atomic update methods:

```kotlin
@Query("UPDATE study_plans SET revision = revision + 1, updatedAt = :now WHERE id = :planId AND revision = :baseRevision")
suspend fun claimRevision(planId: String, baseRevision: Long, now: Long): Int

@Query("UPDATE study_plans SET active = CASE WHEN id = :planId THEN 1 ELSE 0 END WHERE competitionId = :competitionId AND archived = 0")
suspend fun activateOnly(competitionId: Long, planId: String)

@Query("UPDATE study_plans SET masterPlan = CASE WHEN id = :planId THEN 1 ELSE 0 END WHERE competitionId = :competitionId AND archived = 0")
suspend fun markOnlyMaster(competitionId: Long, planId: String)
```

- [ ] **Step 5: Implement `MIGRATION_4_5` and triggers**

Create every table and index explicitly. Add triggers that normalize active/Master exclusivity, clear active/Master on archive, reject archived activation and reject cross-plan task dependencies. Add `MIGRATION_4_5` to `Room.databaseBuilder` and bump `@Database(version = 5)`.

- [ ] **Step 6: Run schema generation and migration verification**

Run:

```powershell
.\gradlew.bat kaptDebugKotlin assembleDebugAndroidTest
```

Expected: `5.json` is generated and Android tests compile. Run the connected migration test when a device is available and require PASS.

- [ ] **Step 7: Run unit regression tests**

Run:

```powershell
.\gradlew.bat testDebugUnitTest
```

Expected: PASS.

---

### Task 6: Implementar snapshot, revisão otimista e execução append-only

**Files:**
- Create: `app/src/main/java/br/com/estudario/data/planner/StudyPlanMappers.kt`
- Create: `app/src/main/java/br/com/estudario/data/planner/StudyPlanSnapshotFactory.kt`
- Create: `app/src/main/java/br/com/estudario/data/planner/StudyPlanRepository.kt`
- Create: `app/src/main/java/br/com/estudario/data/planner/StudyPlanApplicationService.kt`
- Create: `app/src/main/java/br/com/estudario/data/planner/StudyExecutionService.kt`
- Create: `app/src/androidTest/java/br/com/estudario/data/planner/StudyPlanApplicationServiceTest.kt`

**Interfaces:**
- Consumes: `PlannerDao`, `AppDao`, domain engine.
- Produces: transactional create/activate/master/archive/duplicate/replan/execute APIs and `StalePlanningProposalException`.

- [ ] **Step 1: Write failing integration tests**

Cover:

- activating B deactivates A;
- marking B Master clears A;
- duplicate has new plan-owned IDs and zero executions;
- closed execution survives every replan;
- applying a proposal increments revision exactly once;
- applying stale `baseRevision` throws and changes no task;
- partial execution creates a 35-minute successor from a 60-minute task with 25 minutes logged;
- archiving preserves all rows and removes active/Master state.

- [ ] **Step 2: Compile/run integration tests and verify RED**

Run:

```powershell
.\gradlew.bat assembleDebugAndroidTest
```

Expected: FAIL until service APIs exist.

- [ ] **Step 3: Implement snapshot mapping**

`StudyPlanSnapshotFactory.create(planId, today)` must fetch all required data in one Room transaction and map entities to immutable domain values. Aggregate question performance by topic from existing questions/attempts, pending reviews from `review_schedule`, and real study from existing sessions plus planner executions without double counting.

- [ ] **Step 4: Implement optimistic proposal application**

Within `withTransaction`:

1. load current plan;
2. reject archived or wrong-plan proposals;
3. call `claimRevision` with `proposal.baseRevision`;
4. throw `StalePlanningProposalException` if result is zero;
5. revalidate locked/completed/started tasks;
6. apply transitions and insert new tasks;
7. insert one revision audit row;
8. commit.

Any validation failure must roll back the revision claim and all task writes.

- [ ] **Step 5: Implement execution service**

Expose:

```kotlin
suspend fun start(taskId: String, startedAt: Long)
suspend fun complete(taskId: String, input: CompleteTaskInput): String
suspend fun skip(taskId: String, reason: String)
suspend fun reprogram(taskId: String, targetDate: LocalDate?): PlanningProposal
```

`complete` inserts a closed execution and changes task state in one transaction, then requests replan from a new snapshot. Validate nonnegative minutes/questions and `correct <= questions`.

- [ ] **Step 6: Verify integration tests**

Run connected tests when possible and always run:

```powershell
.\gradlew.bat assembleDebugAndroidTest testDebugUnitTest
```

Expected: PASS/compile success, with connected tests recorded separately if no device is available.

---

### Task 7: Implementar codec `.plano` v1 e validação pura

**Files:**
- Create: `app/src/main/java/br/com/estudario/data/transfer/planner/StudyPlanDtos.kt`
- Create: `app/src/main/java/br/com/estudario/data/transfer/planner/StudyPlanCodec.kt`
- Create: `app/src/main/java/br/com/estudario/data/transfer/planner/StudyPlanValidation.kt`
- Create: `app/src/test/java/br/com/estudario/data/transfer/planner/StudyPlanCodecTest.kt`
- Create: `docs/PLANO_FORMAT.md`
- Create: `examples/trt-ti-trilha-mestra.plano`

**Interfaces:**
- Consumes: `org.json`; no Room entities.
- Produces: `StudyPlanFileV1`, `StudyPlanCodec.decode`, `encode`, `StudyPlanValidationException` with path-aware errors.

- [ ] **Step 1: Write failing parser tests**

Add one valid round-trip and separate rejection tests for:

- malformed JSON;
- wrong `format`;
- missing required field;
- negative minutes;
- unknown enum;
- duplicate UUID;
- missing dependency;
- cyclic dependency;
- future version 2.

Assert the future version message exactly:

```kotlin
assertEquals("Versão .plano não suportada: 2. Este aplicativo aceita a versão 1.", error.message)
```

- [ ] **Step 2: Run codec tests and verify RED**

Run:

```powershell
.\gradlew.bat testDebugUnitTest --tests "br.com.estudario.data.transfer.planner.StudyPlanCodecTest"
```

Expected: FAIL because codec types are missing.

- [ ] **Step 3: Implement DTOs and path-aware validation**

DTOs must model the exact v1 root, configuration, priority, annual, monthly, weekly, task, dependency and metadata fields. Decode dates with `LocalDate.parse`, UUIDs with `UUID.fromString`, and report paths such as `tarefas[3].tempoPlanejado`.

Validate all references and detect dependency cycles with depth-first traversal using VISITING/VISITED states.

- [ ] **Step 4: Implement canonical encoding**

Encode in stable key and list order so export→import→export is deterministic. Never emit Room numeric IDs as authoritative references. Emit competition/subject/topic stable external IDs and captured display names.

- [ ] **Step 5: Add documentation and a non-hardcoded example**

Document every field, enum, validation rule, mode and stable-ID rule in `docs/PLANO_FORMAT.md`. The example may express Otávio’s TRT/TI strategy and 22-hour distribution, but no application source or default may depend on it.

- [ ] **Step 6: Verify tests and examples**

Add the example to the test resources path lookup used by existing `.estudo` example tests and require successful decode.

Run:

```powershell
.\gradlew.bat testDebugUnitTest --tests "br.com.estudario.data.transfer.planner.*"
```

Expected: PASS.

---

### Task 8: Implementar CREATE, MERGE, REPLACE_FUTURE e contexto compacto

**Files:**
- Create: `app/src/main/java/br/com/estudario/data/transfer/planner/StudyPlanImportResolver.kt`
- Create: `app/src/main/java/br/com/estudario/data/transfer/planner/StudyPlanTransferService.kt`
- Create: `app/src/main/java/br/com/estudario/data/transfer/planner/PlanContextExporter.kt`
- Create: `app/src/androidTest/java/br/com/estudario/data/transfer/planner/StudyPlanTransferServiceTest.kt`
- Create: `app/src/test/java/br/com/estudario/data/transfer/planner/PlanContextExporterTest.kt`

**Interfaces:**
- Consumes: codec Task 7, DAO/services Task 6.
- Produces: `preview`, `import(mode)`, `exportPlan`, `exportContextJson`, `exportContextText`.

- [ ] **Step 1: Write failing import-policy tests**

Create tests for:

- CREATE generates new plan-owned IDs and no execution;
- MERGE preserves completed, partially executed and locked local tasks;
- MERGE imports only the remaining portion of partial work;
- REPLACE_FUTURE retires only eligible future tasks;
- both policies preserve academic history and past tasks;
- unresolved external IDs appear in preview and are not silently discarded;
- imported active/Master flags are pending user confirmation.

- [ ] **Step 2: Run/compile tests and verify RED**

Run:

```powershell
.\gradlew.bat assembleDebugAndroidTest
```

Expected: FAIL until transfer service exists.

- [ ] **Step 3: Implement reference resolution and preview**

Resolve competition, subjects and topics by `externalId`. Use names only to present candidates; do not auto-bind by name when no stable ID matches. Return counts for create/update/preserve/retire, unresolved references, conflicts, active/Master impact and protected history.

- [ ] **Step 4: Implement import modes transactionally**

CREATE inserts a new plan graph without executions. MERGE matches plan-owned UUIDs, protects local completed/partial/locked records and applies future data conservatively. REPLACE_FUTURE retires only future eligible tasks and inserts the imported future graph. Every mode increments revision and writes an audit row exactly once.

- [ ] **Step 5: Write failing compact-context tests**

Assert exported context contains plan identity, phase, current month/week, capacity, metrics, priorities, weak topics with sample size, missed tasks, relevant future tasks, deficits and alerts. Assert it does not contain theory Markdown or question statements supplied in the fixture.

- [ ] **Step 6: Implement context export and verify**

Produce canonical compact JSON and a human-readable text variant from the same DTO. Limit future task detail to the configured horizon and aggregate older history.

Run unit and connected transfer tests; expected PASS.

---

### Task 9: Atualizar backup e rotear formatos sem regressão

**Files:**
- Create: `app/src/main/java/br/com/estudario/data/transfer/IncomingFileFormat.kt`
- Create: `app/src/main/java/br/com/estudario/data/transfer/IncomingFileCoordinator.kt`
- Modify: `app/src/main/java/br/com/estudario/data/transfer/BackupService.kt`
- Modify: `app/src/main/java/br/com/estudario/MainActivity.kt`
- Modify: `app/src/main/java/br/com/estudario/ui/AppViewModel.kt`
- Modify: `app/src/androidTest/java/br/com/estudario/data/transfer/BackupV3InstrumentedTest.kt`
- Create: `app/src/test/java/br/com/estudario/data/transfer/IncomingFileFormatTest.kt`

**Interfaces:**
- Consumes: existing `.estudo` and backup services; planner transfer service.
- Produces: format-safe dispatch and backup v5 backward compatible with v1–v4.

- [ ] **Step 1: Write failing routing tests**

Assert `IncomingFileFormat.detect(text)` distinguishes ESTUDO, PLANO and BACKUP from root `format`, preserves legacy `.estudo` detection and rejects unknown JSON.

- [ ] **Step 2: Run routing tests and verify RED**

Run the exact new test class; expected FAIL.

- [ ] **Step 3: Implement the detector and MainActivity dispatch**

Read incoming text once and detect format. Existing `.estudo` input continues to invoke its current preview. `.plano` input is published through `IncomingFileCoordinator`; `EstudarioApp` navigates to Plano and `StudyPlanViewModel` consumes it for preview. `AppViewModel` may transport the event but must not parse or apply planner data. Do not make `EstudoPackageService` understand `.plano`.

- [ ] **Step 4: Extend backup to v5**

Export every planner table. Restore v5 in FK-safe order. Accept v1–v4 with missing planner arrays treated as empty. Continue rejecting unknown future backup versions.

- [ ] **Step 5: Extend backup regression tests**

Add a v5 round trip with one complete plan graph and execution. Preserve existing backup tests and add a v4 fixture restore asserting an empty planner.

- [ ] **Step 6: Run transfer regressions**

Run:

```powershell
.\gradlew.bat testDebugUnitTest --tests "br.com.estudario.data.transfer.*"
.\gradlew.bat assembleDebugAndroidTest
```

Expected: PASS/compile success.

---

### Task 10: Criar `StudyPlanViewModel`, estado único e assistente inicial

**Files:**
- Modify: `app/src/main/java/br/com/estudario/EstudarioApplication.kt`
- Create: `app/src/main/java/br/com/estudario/ui/planner/StudyPlanUiModels.kt`
- Create: `app/src/main/java/br/com/estudario/ui/planner/StudyPlanViewModel.kt`
- Create: `app/src/main/java/br/com/estudario/ui/planner/StudyPlanViewModelFactory.kt`
- Create: `app/src/main/java/br/com/estudario/ui/planner/PlanWizardScreen.kt`
- Create: `app/src/test/java/br/com/estudario/ui/planner/StudyPlanUiMapperTest.kt`

**Interfaces:**
- Consumes: repository/application/transfer services.
- Produces: `StateFlow<ActivePlanUiState>`, `StateFlow<PlanTransferUiState>` and intents used by every planner screen.

- [ ] **Step 1: Write failing UI mapping tests**

Test empty plan, active plan, archived-only plans, deficit banner, planned/actual summary and default selected section TODAY. Keep these tests pure by mapping domain values to UI models without Compose.

- [ ] **Step 2: Verify RED**

Run the UI mapper test class; expected FAIL because models are missing.

- [ ] **Step 3: Register planner dependencies**

Construct `StudyPlanRepository`, `StudyPlanApplicationService`, `StudyExecutionService` and `StudyPlanTransferService` in `EstudarioApplication`, reusing the existing database. Do not add planner logic to `AppViewModel`.

- [ ] **Step 4: Implement a single active-plan state**

Combine active plan, hierarchy, tasks, executions, metrics, forecast and Master alerts into `ActivePlanUiState`. Expose intents for create/edit, availability, generate, start, complete, skip, reprogram, lock, plan activation, Master selection, duplicate, archive, restore, import and export.

- [ ] **Step 5: Implement the seven-step wizard**

Persist nothing until final confirmation. Validate objective, contest, dates, at least one available day, nonnegative capacity, subject priorities and question/discursive goals. The review step must show weekly liquid minutes and known deficit before creation.

- [ ] **Step 6: Verify mapper tests and compile Compose**

Run:

```powershell
.\gradlew.bat testDebugUnitTest --tests "br.com.estudario.ui.planner.*"
.\gradlew.bat assembleDebug
```

Expected: PASS.

---

### Task 11: Implementar a experiência Hoje e registro de execução

**Files:**
- Create: `app/src/main/java/br/com/estudario/ui/planner/PlanScreen.kt`
- Create: `app/src/main/java/br/com/estudario/ui/planner/TodayPlanScreen.kt`
- Create: `app/src/main/java/br/com/estudario/ui/planner/TaskExecutionDialog.kt`
- Create: `app/src/main/java/br/com/estudario/ui/planner/PlannerComponents.kt`
- Create: `app/src/androidTest/java/br/com/estudario/ui/planner/TodayPlanScreenTest.kt`

**Interfaces:**
- Consumes: `ActivePlanUiState` and ViewModel intents from Task 10.
- Produces: default Today experience and callback `onOpenTopic(Long)`.

- [ ] **Step 1: Write Compose tests first**

Cover:

- no active plan offers create/import/select;
- entering Plan selects Hoje;
- cards show time, subject, topic, type and questions;
- deficit is visible;
- locked state is explicit;
- Iniciar, Concluir, Reprogramar and Pular call the expected callbacks;
- tapping a resolved topic invokes `onOpenTopic` with its existing ID;
- completion dialog rejects negative numbers and correct answers above questions.

- [ ] **Step 2: Compile tests and verify RED**

Run `assembleDebugAndroidTest`; expected compile failure until composables exist.

- [ ] **Step 3: Implement `PlanScreen` and shared components**

Use Material 3 and existing `ScreenTitle`, `MetricCard`, `EmptyState`, cards, chips and spacing. Add secondary tabs Hoje, Semana, Mês and Ano with Hoje selected initially. Encode status with label and icon in addition to color.

- [ ] **Step 4: Implement Today actions**

Show planned/actual summary, capacity warning and ordered tasks. Completion dialog collects actual minutes, questions, correct, perceived difficulty and optional note. Reprogram offers explicit date or automatic redistribution. Skip requires a reason and visibly marks NAO_REALIZADA.

- [ ] **Step 5: Run Compose tests and build**

Run connected tests if available and always run `assembleDebug assembleDebugAndroidTest`. Expected: success.

---

### Task 12: Implementar Semana, Mês, Ano e gerenciamento de planos

**Files:**
- Create: `app/src/main/java/br/com/estudario/ui/planner/WeekPlanScreen.kt`
- Create: `app/src/main/java/br/com/estudario/ui/planner/MonthPlanScreen.kt`
- Create: `app/src/main/java/br/com/estudario/ui/planner/YearPlanScreen.kt`
- Create: `app/src/main/java/br/com/estudario/ui/planner/PlanManagementScreen.kt`
- Modify: `app/src/main/java/br/com/estudario/ui/planner/PlanScreen.kt`
- Create: `app/src/androidTest/java/br/com/estudario/ui/planner/PlanHierarchyScreensTest.kt`

**Interfaces:**
- Consumes: same `ActivePlanUiState`; no independent metric calculation.
- Produces: hierarchy views, lock controls and plan lifecycle UI.

- [ ] **Step 1: Write Compose tests first**

Test day grouping and totals in Semana, monthly focuses/metas, annual phase criteria/progress/forecast, task/day locking, plan activate/Master/archive/restore/duplicate confirmations and archived plans excluded from active hierarchy.

- [ ] **Step 2: Compile and verify RED**

Run `assembleDebugAndroidTest`; expected failure until screens exist.

- [ ] **Step 3: Implement Semana**

Render each day with capacity, planned/actual, questions, accuracy, completion and blocks. Provide lock/unlock controls. If manual load exceeds capacity, show the exact excess instead of clipping it.

- [ ] **Step 4: Implement Mês and Ano**

Mês shows focus, maintenance, principal topics, targets, capacity, realized values and adherence. Ano shows versioned phases, periods, objectives, main subjects, targets, completion criteria, progress and forecast assumptions.

- [ ] **Step 5: Implement plan management**

List active, Master, inactive and archived plans distinctly. Confirm active/Master replacement impact. Duplicate strategy only, archive without deletion, restore without automatic activation, export `.plano` and expose both context formats.

- [ ] **Step 6: Run UI verification**

Run connected Compose tests when available plus `assembleDebug`. Expected: success.

---

### Task 13: Integrar navegação, Mais e Storage Access Framework

**Files:**
- Modify: `app/src/main/java/br/com/estudario/ui/EstudarioApp.kt`
- Modify: `app/src/main/java/br/com/estudario/ui/screens/MoreScreen.kt`
- Modify: `app/src/main/AndroidManifest.xml`
- Create: `app/src/androidTest/java/br/com/estudario/ui/PlannerNavigationTest.kt`

**Interfaces:**
- Consumes: screens Tasks 10–12 and transfer APIs Tasks 7–9.
- Produces: final navigation and file import/export entry points.

- [ ] **Step 1: Write navigation test first**

Assert the bottom bar has exactly Início, Edital, Plano, Treinar and Mais; clicking Plano opens Hoje; Erros is reachable from Mais; topic navigation from Hoje reaches the existing topic route.

- [ ] **Step 2: Verify RED**

Compile/run the navigation test; expected failure because the old bar still contains Erros.

- [ ] **Step 3: Integrate the planner route**

Instantiate `StudyPlanViewModel` with its factory at the planner navigation graph boundary. Add routes for Plan, wizard and management. Pass `onOpenTopic` into the existing `topic/{id}` route.

- [ ] **Step 4: Move Erros to Mais and add planner file actions**

Remove only the Erros bottom destination. Add an Erros item in More. Add import `.plano`, export `.plano`, export context JSON and export context text using `OpenDocument`/`CreateDocument`; preserve all existing `.estudo` and backup actions.

- [ ] **Step 5: Update intent filters safely**

Allow `.plano` through the same local file-open mechanism without claiming unrelated formats. Runtime routing must still validate the JSON `format` field.

- [ ] **Step 6: Verify navigation and build**

Run connected navigation tests if available and `assembleDebug`. Expected: success.

---

### Task 14: Executar regressões, análise estática e validação do fluxo completo

**Files:**
- Modify as needed for verified defects only: files introduced in Tasks 2–13
- Modify: `README.md`
- Modify: `docs/superpowers/baselines/2026-09-15-before-adaptive-plan.md`
- Create: `docs/superpowers/baselines/2026-09-15-after-adaptive-plan.md`

**Interfaces:**
- Consumes: complete feature.
- Produces: evidence-backed delivery report and updated project documentation.

- [ ] **Step 1: Run every unit test from a clean build**

Run:

```powershell
.\gradlew.bat clean testDebugUnitTest --stacktrace
```

Expected: PASS, including existing `.estudo`, algorithms, mastery and all planner tests.

- [ ] **Step 2: Run all Android tests**

With a connected emulator/device:

```powershell
.\gradlew.bat connectedDebugAndroidTest --stacktrace
```

Expected: PASS for migrations, imports, backup, services and Compose flows. If no device exists, do not claim these tests passed; report them as not executed and preserve the successful `assembleDebugAndroidTest` evidence.

- [ ] **Step 3: Run lint and compile both debug artifacts**

Run:

```powershell
.\gradlew.bat lintDebug assembleDebug assembleDebugAndroidTest --stacktrace
```

Expected: success with no new relevant warnings.

- [ ] **Step 4: Validate the complete user flow**

Use an emulator/device and verify in order:

1. create a plan;
2. define simple and advanced availability;
3. generate the week;
4. open Hoje;
5. start and complete study;
6. confirm progress updates;
7. reduce availability;
8. confirm only future eligible tasks move;
9. export compact context;
10. export `.plano`;
11. import it with MERGE;
12. confirm executions, completed tasks and `.estudo` content remain unchanged.

Capture exact observed totals and revision transitions in the after-baseline document.

- [ ] **Step 5: Update README and final evidence**

Document the Plano tab, Room v5, `.plano`, Java 17 prerequisite, import policies, architecture folders and test commands. Record files created/modified, migration, algorithm summary, test results, warnings and remaining limitations in `2026-09-15-after-adaptive-plan.md`.

- [ ] **Step 6: Perform final diff and safety review**

Run:

```powershell
rg -n "fallbackToDestructiveMigration|allowMainThreadQueries|NotImplementedError" app docs
git status --short 2>$null
```

Expected: no destructive Room fallback, no main-thread database access, no unfinished planner markers, and no changes outside the workspace.

- [ ] **Step 7: Create a final checkpoint if an isolated repository is available**

Run the repository-root guard from Task 1. If it matches the workspace, commit with:

```powershell
git add app docs README.md examples
git commit -m "feat: add adaptive study planning"
```

Otherwise leave the verified working tree uncommitted and report that Git isolation is the reason.
