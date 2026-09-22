# Plano multi-edital Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Permitir que um único plano de estudos reúna matérias e tópicos de vários editais selecionados pela pessoa.

**Architecture:** Um plano mantém o seu edital âncora para compatibilidade, mas passa a ter relações explícitas em `plan_competitions`. O assistente seleciona os editais e entrega as matérias unificadas ao serviço; o snapshot e a aplicação de propostas derivam a origem real da tarefa pela matéria.

**Tech Stack:** Kotlin, Jetpack Compose, Room 2.7, coroutines, JUnit 4 e Compose UI Test.

**Spec:** `docs/superpowers/specs/2026-09-22-plano-multi-edital-design.md`

## Global Constraints

- Banco Room sobe de versão 12 para 13 com migração que preserva cada plano atual.
- Todo plano contém pelo menos um edital e cada matéria do plano pertence a um edital ligado a ele.
- O edital âncora continua preenchido para preservar chaves estrangeiras e formatos já usados.
- Uma tarefa e sua execução usam o edital real da matéria, nunca um edital escolhido por conveniência.
- O assistente não deixa incluir edital vazio nem concluir sem pelo menos um edital marcado.
- O fluxo de um edital deve continuar visualmente equivalente ao atual.

## Review Focus

- Migração com plano antigo ativo: deve criar exatamente uma relação e preservar o plano ativo.
- Dois planos que compartilham um edital: ativar um combinado deve retirar a ativação do outro.
- Matérias com o mesmo nome em editais diferentes: pesos, tarefas e origem devem continuar distintos.
- Edital vazio: aparece na seleção, mas não pode ser marcado nem liberar o avanço.
- Exportar ou importar um `.plano` antigo: precisa resultar em uma relação de edital âncora, sem perder tarefas.

---

### Task 1: Persistir as relações entre plano e edital

**Files:**
- Modify: `app/src/main/java/br/com/estudario/data/local/planner/PlannerEntities.kt`
- Modify: `app/src/main/java/br/com/estudario/data/local/planner/PlannerDao.kt`
- Modify: `app/src/main/java/br/com/estudario/data/local/AppDatabase.kt`
- Modify: `app/src/androidTest/java/br/com/estudario/data/local/AppDatabaseMigrationTest.kt`

**Interfaces:**
- Produces: `PlanCompetitionEntity(planId: String, competitionId: Long, position: Int)`.
- Produces: `PlannerDao.competitionsFor(planId): List<PlanCompetitionEntity>`, `upsertPlanCompetitions`, `combinedPlansUsingCompetition`, `deactivatePlansSharingCompetitions` and `unmarkMasterPlansSharingCompetitions`.
- Consumes: `StudyPlanEntity.competitionId` as the anchor used to backfill existing plans.

- [ ] **Step 1: Write the failing migration test**

Add `migrateTwelveToThirteenBackfillsPlanCompetition` in `AppDatabaseMigrationTest`.

```kotlin
helper.createDatabase(name, 12).apply {
    execSQL("INSERT INTO competitions (id, name, isPrimary, createdAt) VALUES (1, 'TRF', 1, 1)")
    execSQL("INSERT INTO study_plans (id, competitionId, name, objective, startEpochDay, active, masterPlan, archived, revision, createdAt, updatedAt, profile, blockMinutes, weeklyQuestionsTarget, questionsPerTopic, simulationsPerMonth, discursivesPerMonth, interleaveSubjects) VALUES ('p1', 1, 'Plano', 'Aprovação', 1, 1, 0, 0, 0, 1, 1, 'DO_ZERO', 50, 100, 15, 2, 0, 1)")
    close()
}
helper.runMigrationsAndValidate(name, 13, true, AppDatabase.MIGRATION_12_13).apply {
    query("SELECT competitionId, position FROM plan_competitions WHERE planId = 'p1'").use { cursor ->
        assertTrue(cursor.moveToFirst())
        assertEquals(1L, cursor.getLong(0))
        assertEquals(0, cursor.getInt(1))
    }
}
```

- [ ] **Step 2: Run the migration test to verify it fails**

Run:

```powershell
$env:JAVA_HOME = 'C:/Users/otavi/.jdks/jbr-17.0.14'
.\gradlew.bat :app:connectedDebugAndroidTest '-Pandroid.testInstrumentationRunnerArguments.class=br.com.estudario.data.local.AppDatabaseMigrationTest'
```

Expected: compilation failure because `MIGRATION_12_13` and `plan_competitions` do not exist.

- [ ] **Step 3: Add the entity, DAO contract and migration**

```kotlin
@Entity(
    tableName = "plan_competitions",
    primaryKeys = ["planId", "competitionId"],
    foreignKeys = [
        ForeignKey(entity = StudyPlanEntity::class, parentColumns = ["id"], childColumns = ["planId"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = CompetitionEntity::class, parentColumns = ["id"], childColumns = ["competitionId"], onDelete = ForeignKey.CASCADE),
    ],
    indices = [Index("planId"), Index("competitionId")],
)
data class PlanCompetitionEntity(val planId: String, val competitionId: Long, val position: Int)
```

Include it in `@Database.entities`, increment database version to 13, create the table and indexes in `MIGRATION_12_13`, then backfill with:

```sql
INSERT INTO plan_competitions (planId, competitionId, position)
SELECT id, competitionId, 0 FROM study_plans
```

Register `MIGRATION_12_13` in `AppDatabase.create`. Add DAO methods with the exact relation list and overlap queries; overlap queries must exclude the plan being activated or marked master.

- [ ] **Step 4: Run the migration test to verify it passes**

Run the command from Step 2. Expected: `AppDatabaseMigrationTest` passes on the emulator.

- [ ] **Step 5: Commit the schema slice**

```powershell
git add app/src/main/java/br/com/estudario/data/local/planner/PlannerEntities.kt app/src/main/java/br/com/estudario/data/local/planner/PlannerDao.kt app/src/main/java/br/com/estudario/data/local/AppDatabase.kt app/src/androidTest/java/br/com/estudario/data/local/AppDatabaseMigrationTest.kt
git commit -m "feat: persist plan edital selections"
```

### Task 2: Criar, duplicar e ativar planos com vários editais

**Files:**
- Modify: `app/src/main/java/br/com/estudario/data/planner/StudyPlanApplicationService.kt`
- Modify: `app/src/main/java/br/com/estudario/data/planner/StudyPlanRepository.kt`
- Modify: `app/src/androidTest/java/br/com/estudario/data/planner/StudyPlanApplicationServiceTest.kt`

**Interfaces:**
- Consumes: `CreatePlanInput.competitionIds: List<Long>` and `PlanCompetitionEntity` from Task 1.
- Produces: every created or duplicated plan has a relation row for every selected edital.
- Produces: `activate(planId)` and `markMaster(planId)` resolve conflicts over any shared edital.

- [ ] **Step 1: Write failing service tests**

Extend the fixture with a second competition and subject, then add these tests:

```kotlin
@Test fun createsPlanWithBothSelectedEditais() = runBlocking {
    val planId = service.createPlan(input("Combinado").copy(
        competitionIds = listOf(competitionId, secondCompetitionId),
        subjects = listOf(firstSubjectInput, secondSubjectInput),
    ))
    assertEquals(listOf(competitionId, secondCompetitionId), db.plannerDao().competitionsFor(planId).map { it.competitionId })
}

@Test fun activatingCombinedPlanDeactivatesPlanThatSharesAnyEdital() = runBlocking {
    val first = service.createPlan(input("Primeiro"))
    val combined = service.createPlan(input("Combinado").copy(competitionIds = listOf(competitionId, secondCompetitionId), subjects = listOf(firstSubjectInput, secondSubjectInput)))
    service.activate(first)
    service.activate(combined)
    assertFalse(db.plannerDao().plan(first)!!.active)
    assertTrue(db.plannerDao().plan(combined)!!.active)
}
```

Also test rejection of `competitionIds = emptyList()`, repeated IDs and a subject belonging to an unselected edital.

- [ ] **Step 2: Run the service tests to verify they fail**

Run:

```powershell
.\gradlew.bat :app:connectedDebugAndroidTest '-Pandroid.testInstrumentationRunnerArguments.class=br.com.estudario.data.planner.StudyPlanApplicationServiceTest'
```

Expected: test compile failure because `competitionIds` and relation persistence are absent.

- [ ] **Step 3: Implement service invariants and shared activation**

Change the input contract to make the selection explicit while keeping a convenience anchor:

```kotlin
data class CreatePlanInput(
    val competitionId: Long,
    val competitionIds: List<Long> = listOf(competitionId),
    // existing fields remain unchanged
)
```

Inside `createPlan`, normalize and validate the selected IDs before inserting:

```kotlin
val selectedCompetitionIds = input.competitionIds.distinct()
require(selectedCompetitionIds.isNotEmpty()) { "Selecione pelo menos um edital." }
require(input.competitionId in selectedCompetitionIds) { "O edital principal precisa fazer parte do plano." }
val subjectCompetitions = db.dao().subjectsOnce().associate { it.id to it.competitionId }
require(input.subjects.all { subjectCompetitions[it.subjectId] in selectedCompetitionIds }) {
    "Há matéria fora dos editais selecionados."
}
planner.upsertPlanCompetitions(selectedCompetitionIds.mapIndexed { position, id -> PlanCompetitionEntity(planId, id, position) })
```

Copy relations in `duplicate`. Replace anchor-only activation/master updates with DAO overlap updates based on `plan_competitions`, then set the requested plan true. Keep calls transactional.

- [ ] **Step 4: Run the service tests to verify they pass**

Run the command from Step 2. Expected: creation, duplication, validation, active and master conflict tests pass.

- [ ] **Step 5: Commit the service slice**

```powershell
git add app/src/main/java/br/com/estudario/data/planner/StudyPlanApplicationService.kt app/src/main/java/br/com/estudario/data/planner/StudyPlanRepository.kt app/src/androidTest/java/br/com/estudario/data/planner/StudyPlanApplicationServiceTest.kt
git commit -m "feat: create multi-edital study plans"
```

### Task 3: Planejar e executar tarefas pela origem real da matéria

**Files:**
- Modify: `app/src/main/java/br/com/estudario/data/planner/StudyPlanSnapshotFactory.kt`
- Modify: `app/src/main/java/br/com/estudario/data/planner/StudyPlanApplicationService.kt`
- Modify: `app/src/androidTest/java/br/com/estudario/data/planner/StudyPlanApplicationServiceTest.kt`

**Interfaces:**
- Consumes: `PlannerDao.competitionsFor(planId)` from Task 1.
- Produces: snapshot com matérias e tópicos dos editais selecionados e `PlanTaskEntity.competitionId` igual ao edital da matéria.

- [ ] **Step 1: Write the failing task-origin test**

```kotlin
@Test fun replanCombinedPlanKeepsTheCompetitionOfEachSubject() = runBlocking {
    val planId = service.createPlan(input("Combinado").copy(
        competitionIds = listOf(competitionId, secondCompetitionId),
        subjects = listOf(firstSubjectInput, secondSubjectInput),
    ))
    service.replan(planId, ReplanReason.INITIAL, LocalDate.of(2026, 9, 15))
    val taskCompetitionBySubject = db.plannerDao().tasksForOnce(planId).associate { it.subjectId to it.competitionId }
    assertEquals(competitionId, taskCompetitionBySubject[firstSubjectId])
    assertEquals(secondCompetitionId, taskCompetitionBySubject[secondSubjectId])
}
```

- [ ] **Step 2: Run the task-origin test to verify it fails**

Run the Task 2 test command. Expected: second subject task incorrectly has the anchor competition or does not exist.

- [ ] **Step 3: Include all selected edital data and derive task origins**

In `StudyPlanSnapshotFactory.create`, load all selected IDs from `planner.competitionsFor(planId)` and filter `dao.subjectsOnce()` and `dao.topicsOnce()` by that set. In `applyProposal`, calculate a `subjectCompetitionIds` map from `db.dao().subjectsOnce()` and pass it to the entity mapper:

```kotlin
val subjectCompetitionIds = db.dao().subjectsOnce().associate { it.id to it.competitionId }
val entities = proposal.newTasks.map { task ->
    task.toEntity(
        competitionId = task.subjectId?.let(subjectCompetitionIds::get) ?: plan.competitionId,
        revision = nextRevision,
        subjectName = task.subjectId?.let { subjects[it]?.name } ?: "Matéria não vinculada",
        topicName = task.topicId?.let { topics[it]?.title },
    )
}
```

- [ ] **Step 4: Run the task-origin test to verify it passes**

Run the Task 2 command. Expected: both task origins match their source edital.

- [ ] **Step 5: Commit the planning slice**

```powershell
git add app/src/main/java/br/com/estudario/data/planner/StudyPlanSnapshotFactory.kt app/src/main/java/br/com/estudario/data/planner/StudyPlanApplicationService.kt app/src/androidTest/java/br/com/estudario/data/planner/StudyPlanApplicationServiceTest.kt
git commit -m "fix: preserve edital origin in combined tasks"
```

### Task 4: Selecionar vários editais no assistente de criação

**Files:**
- Modify: `app/src/main/java/br/com/estudario/ui/planner/PlanWizardScreen.kt`
- Modify: `app/src/androidTest/java/br/com/estudario/ui/planner/PlanHierarchyScreensTest.kt`

**Interfaces:**
- Consumes: `competitions`, `subjects`, `topics` and `CreatePlanInput.competitionIds`.
- Produces: `CreatePlanInput` com edital âncora e a seleção ordenada de editais utilizáveis.

- [ ] **Step 1: Write failing compact-screen UI tests**

Add a test that creates two editais com matérias e um edital vazio, opens `PlanWizardScreen`, advances to “Quais editais entram neste plano”, and asserts:

```kotlin
compose.onNodeWithText("Quais editais entram neste plano?").assertIsDisplayed()
compose.onNodeWithText("Edital A").performClick()
compose.onNodeWithText("Edital B").performClick()
compose.onNodeWithText("2 editais selecionados").assertIsDisplayed()
compose.onNodeWithText("Edital vazio").assertIsNotEnabled()
```

Capture the `CreatePlanInput` callback and assert that it has the two IDs and subjects from both editais. Add a second assertion that removing both valid selections disables Continuar.

- [ ] **Step 2: Run the UI test to verify it fails**

Run:

```powershell
.\gradlew.bat :app:connectedDebugAndroidTest '-Pandroid.testInstrumentationRunnerArguments.class=br.com.estudario.ui.planner.PlanHierarchyScreensTest'
```

Expected: “Quais editais entram neste plano?” and multi-selection controls do not exist.

- [ ] **Step 3: Implement selection, labels and unified subjects**

Replace `competitionId` state with ordered `selectedCompetitionIds`, initialized with the primary edital when it has subjects, otherwise the first edital with subjects. Derive the anchor as the first selection and the usable IDs as:

```kotlin
val subjectsByCompetition = subjects.groupBy { it.competitionId }
val selectableCompetitionIds = competitions.filter { subjectsByCompetition[it.id].orEmpty().isNotEmpty() }.map { it.id }
val selectedSubjects = subjects.filter { it.competitionId in selectedCompetitionIds }.sortedWith(compareBy<SubjectEntity> { selectedCompetitionIds.indexOf(it.competitionId) }.thenBy { it.position })
```

Use selectable cards or filter chips with a check mark, subject/topic counts and clear disabled copy for an empty edital. Pass `competitionIds = selectedCompetitionIds` on creation. When `selectedCompetitionIds.size > 1`, prefix weight rows with the edital name.

- [ ] **Step 4: Run the UI test to verify it passes**

Run the command from Step 2. Expected: multi-selection, disabled empty edital, unified input and disabled continuation all pass on the emulator.

- [ ] **Step 5: Commit the wizard slice**

```powershell
git add app/src/main/java/br/com/estudario/ui/planner/PlanWizardScreen.kt app/src/androidTest/java/br/com/estudario/ui/planner/PlanHierarchyScreensTest.kt
git commit -m "feat: select multiple editais in plan wizard"
```

### Task 5: Exibir o escopo combinado e impedir exclusão inconsistente de edital

**Files:**
- Modify: `app/src/main/java/br/com/estudario/ui/planner/PlanScreen.kt`
- Modify: `app/src/main/java/br/com/estudario/data/StudyRepository.kt`
- Modify: `app/src/main/java/br/com/estudario/ui/screens/EditalScreen.kt`
- Modify: `app/src/androidTest/java/br/com/estudario/ui/planner/PlanHierarchyScreensTest.kt`
- Create: `app/src/androidTest/java/br/com/estudario/data/StudyRepositoryPlanScopeTest.kt`

**Interfaces:**
- Consumes: `PlannerDao.competitionsFor(planId)` and `combinedPlansUsingCompetition(competitionId)`.
- Produces: `PlanScope(names: List<String>)` no estado de tela e `PlanScopeSummary(scope: PlanScope)` para mostrar “N editais incluídos”.
- Produces: erro legível ao tentar apagar edital usado por plano combinado.

- [ ] **Step 1: Write failing presentation and deletion tests**

```kotlin
compose.onNodeWithText("2 editais incluídos").assertIsDisplayed()

val error = runCatching { repository.deleteCompetition(secondCompetition) }.exceptionOrNull()
assertEquals("Este edital faz parte de um plano combinado. Ajuste ou arquive o plano antes de excluí-lo.", error?.message)
```

Add the same message to the Edital screen error path so the UI does not silently dismiss the action.

- [ ] **Step 2: Run the relevant tests to verify they fail**

Run:

```powershell
.\gradlew.bat :app:connectedDebugAndroidTest '-Pandroid.testInstrumentationRunnerArguments.class=br.com.estudario.data.StudyRepositoryPlanScopeTest'
```

Expected: missing scope presentation helper or deletion succeeds.

- [ ] **Step 3: Implement scope display and deletion guard**

Expose `data class PlanScope(val names: List<String>)` from `StudyPlanViewModel`, built from the plan relations and competition names. Add `PlanScopeSummary(scope)` below the plan title only when `scope.names.size > 1`:

```kotlin
@Composable
private fun PlanScopeSummary(scope: PlanScope) {
    Text("${scope.names.size} editais incluídos", style = MaterialTheme.typography.labelLarge)
    Text(scope.names.joinToString(" • "), style = MaterialTheme.typography.bodySmall)
}
```

Add `PlannerDao.combinedPlansUsingCompetition`, which returns only plans having more than one row in `plan_competitions`, and make `StudyRepository.deleteCompetition` reject when that query is nonempty. Existing one-edital plans keep their current deletion behavior.

- [ ] **Step 4: Run the relevant tests to verify they pass**

Run the command from Step 2 and the Task 4 UI command. Expected: plan scope label appears and deletion reports the explicit reason.

- [ ] **Step 5: Commit the presentation slice**

```powershell
git add app/src/main/java/br/com/estudario/ui/planner/PlanScreen.kt app/src/main/java/br/com/estudario/data/StudyRepository.kt app/src/main/java/br/com/estudario/ui/screens/EditalScreen.kt app/src/androidTest/java/br/com/estudario/ui/planner/PlanHierarchyScreensTest.kt app/src/androidTest/java/br/com/estudario/data/StudyRepositoryPlanScopeTest.kt
git commit -m "feat: show and protect combined plan scope"
```

### Task 6: Preservar os editais em exportação e importação de plano

**Files:**
- Modify: `app/src/main/java/br/com/estudario/data/transfer/planner/StudyPlanDtos.kt`
- Modify: `app/src/main/java/br/com/estudario/data/transfer/planner/StudyPlanCodec.kt`
- Modify: `app/src/main/java/br/com/estudario/data/transfer/planner/StudyPlanImportResolver.kt`
- Modify: `app/src/main/java/br/com/estudario/data/transfer/planner/StudyPlanTransferService.kt`
- Modify: `app/src/main/java/br/com/estudario/data/transfer/planner/PlanContextExporter.kt`
- Modify: `app/src/test/java/br/com/estudario/data/transfer/planner/StudyPlanCodecTest.kt`
- Modify: `app/src/androidTest/java/br/com/estudario/data/transfer/planner/StudyPlanTransferServiceTest.kt`

**Interfaces:**
- Produces: format version 2 containing `editais: List<PlanCompetitionDto>` while version 1 reads as one anchor edital.
- Produces: imported plan relation rows in the same order as the file.

- [ ] **Step 1: Write failing codec and transfer tests**

```kotlin
@Test fun versionTwoRoundTripsSelectedEditais() {
    val plan = codec.decode(validPlanV2WithTwoCompetitions())
    assertEquals(listOf("competition-1", "competition-2"), plan.competitions.map { it.externalId })
    assertEquals(plan, codec.decode(codec.encode(plan)))
}

@Test fun importingVersionOneCreatesAnchorOnlyRelation() = runBlocking {
    val imported = service.import(planJson(version = 1), PlanImportMode.CREATE)
    assertEquals(1, db.plannerDao().competitionsFor(imported.planId).size)
}
```

- [ ] **Step 2: Run the codec and transfer tests to verify they fail**

Run:

```powershell
.\gradlew.bat :app:testDebugUnitTest --tests 'br.com.estudario.data.transfer.planner.StudyPlanCodecTest'
```

Expected: version 2 is rejected and `competitions` is absent from the DTO.

- [ ] **Step 3: Add format version 2 with backward reader**

Add `competitions` to the in-memory plan DTO; keep `competition` as `competitions.first()` for legacy consumers. Decode both version 1 and 2, mapping version 1 to `listOf(competition)`. Encode version 2 for every export. Resolve every external competition ID and reject an import with an unresolved edital before inserting a plan. Persist resolved IDs using `upsertPlanCompetitions` on create and replacement paths.

- [ ] **Step 4: Run codec and transfer tests to verify they pass**

Run the command from Step 2, then:

```powershell
.\gradlew.bat :app:connectedDebugAndroidTest '-Pandroid.testInstrumentationRunnerArguments.class=br.com.estudario.data.transfer.planner.StudyPlanTransferServiceTest'
```

Expected: version 1 remains accepted; version 2 preserves both editais and import creates their relation rows.

- [ ] **Step 5: Commit transfer compatibility**

```powershell
git add app/src/main/java/br/com/estudario/data/transfer/planner app/src/test/java/br/com/estudario/data/transfer/planner/StudyPlanCodecTest.kt app/src/androidTest/java/br/com/estudario/data/transfer/planner/StudyPlanTransferServiceTest.kt
git commit -m "feat: transfer multi-edital study plans"
```

### Task 7: Final verification and manual acceptance

**Files:**
- Modify only if a failure requires a focused correction in a file above.

**Interfaces:**
- Consumes: every behavior from Tasks 1–6.
- Produces: a debug APK that can create, re-open, duplicate, export and import a two-edital plan.

- [ ] **Step 1: Run the complete unit suite**

```powershell
$env:JAVA_HOME = 'C:/Users/otavi/.jdks/jbr-17.0.14'
.\gradlew.bat :app:testDebugUnitTest --console=plain
```

Expected: all unit tests pass.

- [ ] **Step 2: Build the app and Android test APKs**

```powershell
.\gradlew.bat :app:assembleDebug :app:assembleDebugAndroidTest --console=plain
```

Expected: both APK artifacts build successfully.

- [ ] **Step 3: Run focused tests only on the emulator**

```powershell
$adbPath = 'C:/Users/otavi/AppData/Local/Android/Sdk/platform-tools/adb.exe'
& $adbPath -s emulator-5554 install -r 'app/build/outputs/apk/debug/app-debug.apk'
& $adbPath -s emulator-5554 install -r 'app/build/outputs/apk/androidTest/debug/app-debug-androidTest.apk'
& $adbPath -s emulator-5554 shell am instrument -w -e class 'br.com.estudario.data.local.AppDatabaseMigrationTest,br.com.estudario.data.planner.StudyPlanApplicationServiceTest,br.com.estudario.data.transfer.planner.StudyPlanTransferServiceTest,br.com.estudario.ui.planner.PlanHierarchyScreensTest' br.com.estudario.test/androidx.test.runner.AndroidJUnitRunner
```

Expected: all focused instrumentation tests pass; do not run tests on a physical device.

- [ ] **Step 4: Manual acceptance flow**

On the emulator, create “Edital A” and “Edital B”, each with at least one matter and topic. Open Criar plano sem IA, confirm both appear as selected controls, select both, complete the wizard, and confirm tasks from both editais appear in one plan. Then duplicate the plan and verify its scope still shows two editais.

- [ ] **Step 5: Record the verified state**

Run:

```powershell
git status --short
git log -7 --oneline
```

Expected: the commits from Tasks 1–6 are present and there are no unreviewed changes in their files.
