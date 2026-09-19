# Arquivos do Estudario Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Criar uma pipeline única para importar `.plano`, `.estudo` e backup por seletores internos, `ACTION_VIEW` e `ACTION_SEND`, com detecção segura, resolução contextual de vínculos, transações, conflitos explícitos e navegação após sucesso.

**Architecture:** Um coordenador recebe uma `Uri`, lê metadados e conteúdo pelo `ContentResolver`, classifica o arquivo e delega a um handler pequeno por formato. O plano é primeiro decodificado para um rascunho estrutural com referências opcionais, depois resolvido contra o Room e finalmente convertido em `StudyPlanFileV1` para reutilizar validação e persistência existentes.

**Tech Stack:** Kotlin 2.2.20, Android SDK 36, Jetpack Compose, Navigation Compose, ViewModel, StateFlow, coroutines, Room 2.7.2, `ContentResolver`, JUnit 4 e AndroidX Test.

**Spec:** `docs/superpowers/specs/2026-09-16-arquivos-estudario-design.md`

## Global Constraints

- Kotlin 2.2.20, Java 17, `minSdk` 26, `targetSdk` 36 e banco Room versão 5.
- Não adicionar `MANAGE_EXTERNAL_STORAGE` nem resolver caminho físico de `content://`.
- Não derivar, fabricar ou gerar `externalId` durante a importação.
- IDs emitidos por mecanismos oficiais do Estudario são válidos.
- Comparar nomes por igualdade após trim, caixa, diacríticos e espaços; nunca usar similaridade aproximada.
- Limitar matéria ao concurso resolvido e tópico à matéria resolvida.
- Não sobrescrever, mesclar, copiar ou restaurar silenciosamente.
- Backup detectado pela pipeline sempre exige confirmação de restauração.
- Preservar `.estudo` v1/v2, `.plano` v1 e backup v1–v5.
- Preservar todas as alterações não relacionadas já presentes no workspace.

---

## Estrutura de arquivos planejada

- `data/transfer/EstudarioFileSource.kt`: metadados de URI e leitura limitada por `ContentResolver`.
- `data/transfer/EstudarioFileDetector.kt`: classificação por nome, MIME e assinatura JSON.
- `data/transfer/EstudarioFileModels.kt`: formatos, erros, decisões e estados públicos da pipeline.
- `data/transfer/EstudarioFileCoordinator.kt`: máquina de estados e delegação aos formatos.
- `data/transfer/EstudoFileHandler.kt`: ponte entre coordenador e `EstudoPackageService`.
- `data/transfer/BackupFileHandler.kt`: encaminhamento obrigatório à confirmação de restauração.
- `data/transfer/planner/StudyPlanImportDraft.kt`: representação estrutural com referências opcionais.
- `data/transfer/planner/StudyPlanImportDraftCodec.kt`: parse estrutural do `.plano`.
- `data/transfer/planner/ImportLinkResolver.kt`: resolução contextual e resultados tipados.
- `data/transfer/planner/StudyPlanFileHandler.kt`: orquestra draft, resolução, prévia e importação.
- `ui/screens/FileImportDialogs.kt`: escolhas de vínculo, duplicidade, restauração, progresso e erros.
- Arquivos existentes serão modificados somente para integrar essas unidades.

---

### Task 1: Fonte de URI e detector definitivo de formato

**Files:**
- Create: `app/src/main/java/br/com/estudario/data/transfer/EstudarioFileSource.kt`
- Create: `app/src/main/java/br/com/estudario/data/transfer/EstudarioFileModels.kt`
- Create: `app/src/main/java/br/com/estudario/data/transfer/EstudarioFileDetector.kt`
- Modify: `app/src/main/java/br/com/estudario/data/transfer/IncomingFileFormat.kt`
- Test: `app/src/test/java/br/com/estudario/data/transfer/EstudarioFileDetectorTest.kt`

**Interfaces:**
- Produces: `IncomingFilePayload`, `EstudarioFileFormat`, `FileDetectionResult` e `EstudarioFileDetector.detect(payload)`.
- Consumes: assinaturas reais já reconhecidas por `IncomingFileFormat` e `EstudoPackageParser`.

- [ ] **Step 1: Escrever testes falhando para classificação e divergência**

```kotlin
class EstudarioFileDetectorTest {
    private val detector = EstudarioFileDetector()

    @Test fun planoRequiresMatchingSignature() {
        val payload = IncomingFilePayload("Plano.plano", "application/octet-stream", """{"format":"estudario-plano","version":1}""")
        assertEquals(FileDetectionResult.Match(EstudarioFileFormat.PLANO), detector.detect(payload))
    }

    @Test fun extensionAndContentDivergenceIsRejected() {
        val payload = IncomingFilePayload("Plano.plano", "application/json", """{"format":"estudario-backup","version":5}""")
        assertTrue(detector.detect(payload) is FileDetectionResult.ExtensionMismatch)
    }

    @Test fun genericMimeAcceptsValidEstudoSignature() {
        val payload = IncomingFilePayload("conteudo.estudo", "text/plain", """{"version":2,"packageId":"p","competition":"C","subject":"S","topic":"T"}""")
        assertEquals(FileDetectionResult.Match(EstudarioFileFormat.ESTUDO), detector.detect(payload))
    }

    @Test fun invalidJsonHasFriendlyFailure() {
        assertTrue(detector.detect(IncomingFilePayload("x.plano", "application/json", "{")) is FileDetectionResult.InvalidJson)
    }
}
```

- [ ] **Step 2: Executar o teste e confirmar falha por tipos ausentes**

Run: `./gradlew.bat :app:testDebugUnitTest --tests "br.com.estudario.data.transfer.EstudarioFileDetectorTest"`

Expected: FAIL por `Unresolved reference` nos novos modelos.

- [ ] **Step 3: Implementar modelos, leitura limitada e detector**

```kotlin
data class IncomingFilePayload(val displayName: String?, val mimeType: String?, val text: String)
enum class EstudarioFileFormat { ESTUDO, PLANO, BACKUP }
sealed interface FileDetectionResult {
    data class Match(val format: EstudarioFileFormat) : FileDetectionResult
    data class ExtensionMismatch(val extension: String, val detected: EstudarioFileFormat) : FileDetectionResult
    data object InvalidJson : FileDetectionResult
    data object Unknown : FileDetectionResult
}

class EstudarioFileDetector {
    fun detect(payload: IncomingFilePayload): FileDetectionResult
}
```

`readIncomingFile` deve consultar `OpenableColumns.DISPLAY_NAME`, usar `ContentResolver.getType`, ler UTF-8 por stream e rejeitar conteúdo acima de 32 MiB com `FileReadException("Este arquivo é grande demais para ser importado.")`.

- [ ] **Step 4: Executar testes do detector e regressão atual**

Run: `./gradlew.bat :app:testDebugUnitTest --tests "br.com.estudario.data.transfer.*File*Test"`

Expected: PASS.

- [ ] **Step 5: Commitar a unidade**

```powershell
git add app/src/main/java/br/com/estudario/data/transfer app/src/test/java/br/com/estudario/data/transfer
git commit -m "feat: add safe Estudario file detection"
```

---

### Task 2: Parse estrutural de plano antes da resolução

**Files:**
- Create: `app/src/main/java/br/com/estudario/data/transfer/planner/StudyPlanImportDraft.kt`
- Create: `app/src/main/java/br/com/estudario/data/transfer/planner/StudyPlanImportDraftCodec.kt`
- Modify: `app/src/main/java/br/com/estudario/data/transfer/planner/StudyPlanCodec.kt`
- Test: `app/src/test/java/br/com/estudario/data/transfer/planner/StudyPlanImportDraftCodecTest.kt`

**Interfaces:**
- Produces: `StudyPlanImportDraftCodec.decode(text): StudyPlanImportDraft` e `StudyPlanImportDraft.toResolvedFile(bindings): StudyPlanFileV1`.
- Consumes: DTOs e regras de tipos do `StudyPlanCodec` atual.

- [ ] **Step 1: Escrever testes falhando para referências opcionais e validação estrutural**

```kotlin
@Test fun acceptsBlankCompetitionExternalIdForLaterResolution() {
    val draft = codec.decode(validPlan.replace("\"externalId\":\"competition-1\"", "\"externalId\":\"\""))
    assertNull(draft.competition.externalId)
    assertEquals("Concurso", draft.competition.name)
}

@Test fun rejectsUnsupportedVersionBeforeResolution() {
    val error = assertFailsWith<StudyPlanValidationException> { codec.decode(validPlan.replace("\"version\":1", "\"version\":2")) }
    assertEquals("Este plano foi criado em uma versão de formato ainda não suportada.", error.message)
}

@Test fun acceptsNullTaskReferencesWithNames() {
    val draft = codec.decode(planWithNullTaskIds)
    assertEquals("TI", draft.tasks.single().subjectName)
}
```

- [ ] **Step 2: Executar o teste e confirmar que o codec atual rejeita ID vazio**

Run: `./gradlew.bat :app:testDebugUnitTest --tests "*.StudyPlanImportDraftCodecTest"`

Expected: FAIL com a mensagem atual de `concurso.externalId`.

- [ ] **Step 3: Implementar draft e codec reutilizando funções de parse puras**

```kotlin
data class ImportReferenceDraft(val externalId: String?, val name: String?)

data class StudyPlanImportDraft(
    val planId: String,
    val competition: ImportReferenceDraft,
    val name: String,
    val objective: String,
    val active: Boolean,
    val masterPlan: Boolean,
    val startDate: LocalDate,
    val examDate: LocalDate?,
    val baseRevision: Long?,
    val configuration: PlanConfigurationDto,
    val subjects: List<PlanSubjectDraft>,
    val annualPhases: List<AnnualPhaseDto>,
    val monthlyPlans: List<MonthlyPlanDto>,
    val weeklyPlans: List<WeeklyPlanDto>,
    val tasks: List<PlanTaskDraft>,
    val metadata: Map<String, String>,
)
```

Extrair do codec existente o parse de datas, enums, configuração, fases e tarefas. `StudyPlanCodec.decode` continuará sendo a entrada estrita para exportações e testes antigos, convertendo um draft apenas quando todas as referências já estiverem presentes.

- [ ] **Step 4: Executar os testes novos e os testes existentes do codec**

Run: `./gradlew.bat :app:testDebugUnitTest --tests "br.com.estudario.data.transfer.planner.*CodecTest"`

Expected: PASS, incluindo round-trip existente.

- [ ] **Step 5: Commitar a unidade**

```powershell
git add app/src/main/java/br/com/estudario/data/transfer/planner app/src/test/java/br/com/estudario/data/transfer/planner
git commit -m "refactor: split plan structure parsing from link validation"
```

---

### Task 3: Resolvedor contextual de concurso, matéria e tópico

**Files:**
- Create: `app/src/main/java/br/com/estudario/data/transfer/planner/ImportLinkResolver.kt`
- Modify: `app/src/main/java/br/com/estudario/data/transfer/planner/StudyPlanImportResolver.kt`
- Modify: `app/src/main/java/br/com/estudario/data/local/AppDao.kt`
- Test: `app/src/androidTest/java/br/com/estudario/data/transfer/planner/ImportLinkResolverTest.kt`

**Interfaces:**
- Produces: `ImportLinkResolver.resolve(draft, overrides): LinkResolutionReport`.
- Produces: `LinkResolutionReport.resolvedFileOrNull(): StudyPlanFileV1?`.
- Consumes: `StudyPlanImportDraft` e entidades Room reais.

- [ ] **Step 1: Escrever testes instrumentados falhando para todos os estados relevantes**

```kotlin
@Test fun missingCompetitionIdResolvesUniqueNormalizedName() = runBlocking {
    dao.insertCompetition(CompetitionEntity(name = "TRT DA 3ª REGIÃO", externalId = "trt-3"))
    val report = resolver.resolve(draft(competitionId = null, competitionName = "  trt da 3a regiao "))
    assertEquals(LinkResolutionKind.MISSING_RESOLVED_BY_NAME, report.competition.kind)
    assertEquals("trt-3", report.competition.resolvedExternalId)
}

@Test fun suppliedUnknownIdIsRecordedAsDivergence() = runBlocking {
    dao.insertCompetition(CompetitionEntity(name = "TRT 3", externalId = "trt-3"))
    val report = resolver.resolve(draft(competitionId = "wrong", competitionName = "TRT 3"))
    assertEquals(LinkResolutionKind.DIVERGENT_RESOLVED_BY_NAME, report.competition.kind)
}

@Test fun identicalTopicNameNeverCrossesSubjectBoundary() = runBlocking {
    val report = resolver.resolve(draftForDuplicatedTopicNames())
    assertEquals(expectedTopicInResolvedSubject, report.tasks.single().topic.localId)
}

@Test fun entityWithoutOfficialIdCannotSatisfyRequiredReference() = runBlocking {
    dao.insertCompetition(CompetitionEntity(name = "TRT 3", externalId = null))
    val report = resolver.resolve(draft(competitionId = null, competitionName = "TRT 3"))
    assertEquals(LinkResolutionKind.MATCHED_WITHOUT_OFFICIAL_EXTERNAL_ID, report.competition.kind)
}
```

- [ ] **Step 2: Executar testes instrumentados e confirmar falha**

Run: `./gradlew.bat :app:connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=br.com.estudario.data.transfer.planner.ImportLinkResolverTest`

Expected: FAIL por `ImportLinkResolver` ausente.

- [ ] **Step 3: Implementar normalização e resultados tipados**

```kotlin
enum class LinkResolutionKind {
    PRESENT_AND_MATCHED,
    MISSING_RESOLVED_BY_NAME,
    DIVERGENT_RESOLVED_BY_NAME,
    AMBIGUOUS,
    NOT_FOUND,
    MATCHED_WITHOUT_OFFICIAL_EXTERNAL_ID,
    CONTEXT_MISMATCH,
}

data class LinkResolution<T>(
    val kind: LinkResolutionKind,
    val suppliedExternalId: String?,
    val candidates: List<T>,
    val selected: T? = null,
)

internal fun normalizedImportName(value: String): String =
    Normalizer.normalize(value.trim().lowercase(Locale.ROOT), Normalizer.Form.NFD)
        .replace("\\p{M}+".toRegex(), "")
        .replace("\\s+".toRegex(), " ")
```

Carregar concursos, matérias e tópicos uma vez por resolução. Validar contexto mesmo quando o ID informado existe. `overrides` só aceitará IDs locais apresentados anteriormente como candidatos.

- [ ] **Step 4: Executar testes instrumentados do resolvedor**

Run: `./gradlew.bat :app:connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=br.com.estudario.data.transfer.planner.ImportLinkResolverTest`

Expected: PASS.

- [ ] **Step 5: Commitar a unidade**

```powershell
git add app/src/main/java/br/com/estudario/data/local/AppDao.kt app/src/main/java/br/com/estudario/data/transfer/planner app/src/androidTest/java/br/com/estudario/data/transfer/planner
git commit -m "feat: resolve imported plan links in context"
```

---

### Task 4: Integrar resolução, duplicidade e persistência de planos

**Files:**
- Create: `app/src/main/java/br/com/estudario/data/transfer/planner/StudyPlanFileHandler.kt`
- Modify: `app/src/main/java/br/com/estudario/data/transfer/planner/StudyPlanTransferService.kt`
- Modify: `app/src/main/java/br/com/estudario/ui/planner/StudyPlanViewModel.kt`
- Test: `app/src/androidTest/java/br/com/estudario/data/transfer/planner/StudyPlanFileHandlerTest.kt`
- Test: `app/src/androidTest/java/br/com/estudario/data/transfer/planner/StudyPlanTransferServiceTest.kt`

**Interfaces:**
- Produces: `StudyPlanFileHandler.inspect(payload): PlanFileInspection`.
- Produces: `StudyPlanFileHandler.import(inspection, decision): ImportedDestination.Plan(planId)`.
- Consumes: detector, draft codec, resolvedor e serviço transacional.

- [ ] **Step 1: Escrever testes falhando para importação automática, duplicidade e rollback**

```kotlin
@Test fun newFullyResolvedPlanCanImportWithoutPreviewConfirmation() = runBlocking {
    val inspection = handler.inspect(payload(validPlan))
    assertTrue(inspection is PlanFileInspection.ReadyToCreate)
    val destination = handler.import(inspection, PlanImportDecision.Create)
    assertEquals(planId, (destination as ImportedDestination.Plan).planId)
}

@Test fun duplicatePlanRequiresExplicitDecision() = runBlocking {
    handler.import(handler.inspect(payload(validPlan)), PlanImportDecision.Create)
    assertTrue(handler.inspect(payload(validPlan)) is PlanFileInspection.Duplicate)
}

@Test fun unresolvedRequiredPriorityDoesNotWriteAnything() = runBlocking {
    assertTrue(handler.inspect(payload(planWithMissingSubject)) is PlanFileInspection.NeedsLinks)
    assertTrue(db.plannerDao().plansOnce().isEmpty())
}
```

- [ ] **Step 2: Executar teste e confirmar falha**

Run: `./gradlew.bat :app:connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=br.com.estudario.data.transfer.planner.StudyPlanFileHandlerTest`

Expected: FAIL por handler ausente.

- [ ] **Step 3: Implementar inspeção imutável e revalidação transacional**

```kotlin
sealed interface PlanFileInspection {
    data class ReadyToCreate(val resolved: StudyPlanFileV1) : PlanFileInspection
    data class NeedsLinks(val draft: StudyPlanImportDraft, val report: LinkResolutionReport) : PlanFileInspection
    data class Duplicate(val resolved: StudyPlanFileV1, val localRevision: Long) : PlanFileInspection
}

sealed interface PlanImportDecision {
    data object Create : PlanImportDecision
    data object OpenExisting : PlanImportDecision
    data object Merge : PlanImportDecision
    data object ReplaceFuture : PlanImportDecision
    data object Copy : PlanImportDecision
}
```

Antes de persistir, resolver novamente as escolhas por IDs oficiais. Reutilizar `StudyPlanTransferService.import` para criação, mesclagem e substituição futura. Adicionar `StudyPlanTransferService.importAsCopy(file): StudyPlanImportResult`, que cria um novo `planId` por `UUID.randomUUID()` dentro do mecanismo oficial e remapeia, no mesmo serviço, todos os UUIDs internos e dependências antes de chamar a persistência transacional.

- [ ] **Step 4: Executar testes de handler e regressão do serviço**

Run: `./gradlew.bat :app:connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.package=br.com.estudario.data.transfer.planner`

Expected: PASS.

- [ ] **Step 5: Commitar a unidade**

```powershell
git add app/src/main/java/br/com/estudario/data/transfer/planner app/src/main/java/br/com/estudario/ui/planner app/src/androidTest/java/br/com/estudario/data/transfer/planner
git commit -m "feat: import resolved plan files safely"
```

---

### Task 5: Handlers de estudo e backup

**Files:**
- Create: `app/src/main/java/br/com/estudario/data/transfer/EstudoFileHandler.kt`
- Create: `app/src/main/java/br/com/estudario/data/transfer/BackupFileHandler.kt`
- Modify: `app/src/main/java/br/com/estudario/data/transfer/EstudoPackageService.kt`
- Modify: `app/src/main/java/br/com/estudario/data/transfer/BackupService.kt`
- Test: `app/src/androidTest/java/br/com/estudario/data/transfer/FormatFileHandlersTest.kt`

**Interfaces:**
- Produces: `EstudoFileHandler.inspect/import` e `BackupFileHandler.inspect/restore`.
- Consumes: serviços transacionais existentes e `IncomingFilePayload`.

- [ ] **Step 1: Escrever testes falhando para estudo novo, pacote repetido e backup não automático**

```kotlin
@Test fun newEstudoIsReadyForSafeImport() = runBlocking {
    assertTrue(estudo.inspect(payload(estudoJson)) is EstudoInspection.Ready)
}

@Test fun repeatedPackageRequiresDecision() = runBlocking {
    estudo.import(estudo.inspect(payload(estudoJson)), EstudoImportDecision.Create)
    assertTrue(estudo.inspect(payload(estudoJson)) is EstudoInspection.Duplicate)
}

@Test fun backupInspectionNeverRestoresData() = runBlocking {
    val before = dao.competitionsOnce()
    assertTrue(backup.inspect(payload(backupJson)) is BackupInspection.NeedsConfirmation)
    assertEquals(before, dao.competitionsOnce())
}
```

- [ ] **Step 2: Executar teste e confirmar falha por handlers ausentes**

Run: `./gradlew.bat :app:connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=br.com.estudario.data.transfer.FormatFileHandlersTest`

Expected: FAIL.

- [ ] **Step 3: Implementar adaptadores finos sobre serviços atuais**

```kotlin
sealed interface EstudoInspection {
    data class Ready(val preview: EstudoPreview, val raw: String) : EstudoInspection
    data class Duplicate(val preview: EstudoPreview, val raw: String) : EstudoInspection
}

data class BackupInspection(val version: Int, val exportedAt: Long?) {
    val needsExplicitConfirmation: Boolean = true
}
```

Não mover persistência para os handlers. `EstudoPackageService.import` e `BackupService.restore` continuam sendo as fronteiras transacionais.

- [ ] **Step 4: Executar testes dos handlers e importadores existentes**

Run: `./gradlew.bat :app:connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.package=br.com.estudario.data.transfer`

Expected: PASS.

- [ ] **Step 5: Commitar a unidade**

```powershell
git add app/src/main/java/br/com/estudario/data/transfer app/src/androidTest/java/br/com/estudario/data/transfer
git commit -m "feat: adapt study and backup imports to file pipeline"
```

---

### Task 6: Coordenador único e máquina de estados

**Files:**
- Create: `app/src/main/java/br/com/estudario/data/transfer/EstudarioFileCoordinator.kt`
- Modify: `app/src/main/java/br/com/estudario/data/transfer/IncomingFileCoordinator.kt`
- Modify: `app/src/main/java/br/com/estudario/EstudarioApplication.kt`
- Test: `app/src/test/java/br/com/estudario/data/transfer/EstudarioFileCoordinatorTest.kt`

**Interfaces:**
- Produces: `state: StateFlow<FileImportState>`.
- Produces: `open(source)`, `chooseLink`, `chooseDuplicateAction`, `confirmBackup`, `dismiss`.
- Consumes: leitor, detector e três handlers.

- [ ] **Step 1: Escrever testes com fakes para transições completas**

```kotlin
@Test fun resolvedPlanMovesFromReadingToSuccess() = runTest {
    coordinator.open(fakeSource("Plano.plano"))
    assertEquals(FileImportState.Success(ImportedDestination.Plan("plan-id"), "Plano importado com sucesso."), coordinator.state.value)
}

@Test fun backupStopsAtDestructiveConfirmation() = runTest {
    coordinator.open(fakeSource("backup.json"))
    assertTrue(coordinator.state.value is FileImportState.NeedsDestructiveConfirmation)
    assertEquals(0, backupHandler.restoreCalls)
}

@Test fun ambiguousCompetitionStopsAtLinkChoice() = runTest {
    coordinator.open(fakeSource("Plano.plano"))
    assertTrue(coordinator.state.value is FileImportState.NeedsLinkChoice)
}
```

- [ ] **Step 2: Executar teste e confirmar falha**

Run: `./gradlew.bat :app:testDebugUnitTest --tests "*.EstudarioFileCoordinatorTest"`

Expected: FAIL por coordenador ausente.

- [ ] **Step 3: Implementar estados e comandos idempotentes**

```kotlin
sealed interface FileImportState {
    data object Idle : FileImportState
    data class Reading(val displayName: String) : FileImportState
    data class NeedsLinkChoice(val request: LinkChoiceRequest) : FileImportState
    data class NeedsDuplicateDecision(val request: DuplicateDecisionRequest) : FileImportState
    data class NeedsDestructiveConfirmation(val backup: BackupInspection) : FileImportState
    data class Importing(val displayName: String) : FileImportState
    data class Success(val destination: ImportedDestination, val message: String) : FileImportState
    data class Error(val message: String) : FileImportState
}
```

Proteger cada operação por token da importação para que respostas de uma URI anterior não sobrescrevam a atual.

- [ ] **Step 4: Executar os testes da máquina de estados**

Run: `./gradlew.bat :app:testDebugUnitTest --tests "*.EstudarioFileCoordinatorTest"`

Expected: PASS.

- [ ] **Step 5: Commitar a unidade**

```powershell
git add app/src/main/java/br/com/estudario/data/transfer app/src/main/java/br/com/estudario/EstudarioApplication.kt app/src/test/java/br/com/estudario/data/transfer
git commit -m "feat: coordinate all Estudario file imports"
```

---

### Task 7: Interface de conflitos, progresso e resultados

**Files:**
- Create: `app/src/main/java/br/com/estudario/ui/screens/FileImportDialogs.kt`
- Modify: `app/src/main/java/br/com/estudario/ui/EstudarioApp.kt`
- Modify: `app/src/main/java/br/com/estudario/ui/AppViewModel.kt`
- Modify: `app/src/main/java/br/com/estudario/ui/planner/StudyPlanViewModel.kt`
- Test: `app/src/androidTest/java/br/com/estudario/ui/FileImportDialogsTest.kt`

**Interfaces:**
- Consumes: `FileImportState` e comandos do coordenador.
- Produces: navegação para `plan`, `syllabus` ou `topic/{id}` e mensagens de sucesso.

- [ ] **Step 1: Escrever testes Compose falhando para escolhas e duplicidade**

```kotlin
@Test fun ambiguousCompetitionShowsCandidateNames() {
    compose.setContent { FileImportDialogs(state = ambiguousState, actions = fakeActions) }
    compose.onNodeWithText("Encontramos mais de um concurso compatível").assertIsDisplayed()
    compose.onNodeWithText("TRT 3 - TI").assertIsDisplayed()
}

@Test fun duplicatePlanOffersOpenWithoutSilentWrite() {
    compose.setContent { FileImportDialogs(state = duplicatePlanState, actions = fakeActions) }
    compose.onNodeWithText("Abrir existente").assertIsDisplayed()
    compose.onNodeWithText("Mesclar").assertIsDisplayed()
}

@Test fun backupRequiresRestoreButton() {
    compose.setContent { FileImportDialogs(state = backupState, actions = fakeActions) }
    compose.onNodeWithText("Restaurar backup?").assertIsDisplayed()
}
```

- [ ] **Step 2: Executar teste e confirmar falha**

Run: `./gradlew.bat :app:connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=br.com.estudario.ui.FileImportDialogsTest`

Expected: FAIL por composable ausente.

- [ ] **Step 3: Implementar diálogos e navegação consumível uma vez**

Renderizar progresso não cancelável durante persistência; listas de candidatos por nome e contexto; ações explícitas para abrir, mesclar, substituir futuro, copiar e restaurar. Ao receber `Success`, navegar uma vez e chamar `coordinator.consumeSuccess(token)`.

```kotlin
LaunchedEffect(importState) {
    val success = importState as? FileImportState.Success ?: return@LaunchedEffect
    when (val destination = success.destination) {
        is ImportedDestination.Plan -> navController.navigate("plan") { launchSingleTop = true }
        is ImportedDestination.Topic -> navController.navigate("topic/${destination.topicId}") { launchSingleTop = true }
        ImportedDestination.Syllabus -> navController.navigate("syllabus") { launchSingleTop = true }
        ImportedDestination.HomeAfterRestore -> navController.navigate("home") { popUpTo(0) }
    }
}
```

- [ ] **Step 4: Executar testes Compose**

Run: `./gradlew.bat :app:connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=br.com.estudario.ui.FileImportDialogsTest`

Expected: PASS.

- [ ] **Step 5: Commitar a unidade**

```powershell
git add app/src/main/java/br/com/estudario/ui app/src/androidTest/java/br/com/estudario/ui
git commit -m "feat: add file import conflict and result UI"
```

---

### Task 8: Integrar `ACTION_VIEW`, `ACTION_SEND` e Manifest

**Files:**
- Modify: `app/src/main/AndroidManifest.xml`
- Modify: `app/src/main/java/br/com/estudario/MainActivity.kt`
- Test: `app/src/androidTest/java/br/com/estudario/ExternalFileIntentTest.kt`

**Interfaces:**
- Consumes: `EstudarioFileCoordinator.open(UriFileSource)`.
- Produces: entrada Android única para URI de visualização ou compartilhamento.

- [ ] **Step 1: Escrever testes instrumentados falhando para extração de URI**

```kotlin
@Test fun actionViewContentUriReachesCoordinator() {
    launchActivity<MainActivity>(Intent(Intent.ACTION_VIEW, provider.planUri).addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION))
    assertEquals(provider.planUri, fakeCoordinator.lastOpenedUri)
}

@Test fun actionSendSingleStreamReachesCoordinator() {
    val intent = Intent(Intent.ACTION_SEND).setType("application/octet-stream")
        .putExtra(Intent.EXTRA_STREAM, provider.estudoUri)
        .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    launchActivity<MainActivity>(intent)
    assertEquals(provider.estudoUri, fakeCoordinator.lastOpenedUri)
}
```

- [ ] **Step 2: Executar teste e confirmar que `ACTION_SEND` falha**

Run: `./gradlew.bat :app:connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=br.com.estudario.ExternalFileIntentTest`

Expected: `ACTION_VIEW` parcial e `ACTION_SEND` não encaminhado.

- [ ] **Step 3: Simplificar Activity e configurar filtros conservadores**

```kotlin
private fun incomingUri(intent: Intent?): Uri? = when (intent?.action) {
    Intent.ACTION_VIEW -> intent.data
    Intent.ACTION_SEND -> IntentCompat.getParcelableExtra(intent, Intent.EXTRA_STREAM, Uri::class.java)
    else -> null
}

private fun handleIncomingFile(intent: Intent?) {
    incomingUri(intent)?.let { application.fileCoordinator.open(UriFileSource(it, intent?.type)) }
}
```

No Manifest, manter `ACTION_VIEW` e adicionar `ACTION_SEND` em filtros separados. Usar tipos vendor quando fornecidos e combinações de extensão/caminho disponíveis, sem declarar o app como manipulador universal irrestrito de todo `text/plain`. A validação runtime continua obrigatória para qualquer Intent recebido.

- [ ] **Step 4: Executar teste de Intents e inspecionar Manifest mesclado**

Run: `./gradlew.bat :app:processDebugMainManifest :app:connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=br.com.estudario.ExternalFileIntentTest`

Expected: PASS e nenhum `MANAGE_EXTERNAL_STORAGE` no Manifest mesclado.

- [ ] **Step 5: Commitar a unidade**

```powershell
git add app/src/main/AndroidManifest.xml app/src/main/java/br/com/estudario/MainActivity.kt app/src/androidTest/java/br/com/estudario/ExternalFileIntentTest.kt
git commit -m "feat: open Estudario files from Android intents"
```

---

### Task 9: Migrar seletores internos para a pipeline comum

**Files:**
- Modify: `app/src/main/java/br/com/estudario/ui/screens/MoreScreen.kt`
- Modify: `app/src/main/java/br/com/estudario/ui/planner/PlanScreen.kt`
- Modify: `app/src/main/java/br/com/estudario/ui/AppViewModel.kt`
- Modify: `app/src/main/java/br/com/estudario/ui/planner/StudyPlanViewModel.kt`
- Test: `app/src/androidTest/java/br/com/estudario/ui/InternalFileImportTest.kt`

**Interfaces:**
- Consumes: `fileCoordinator.open(UriFileSource(uri, expectedFormat))`.
- Removes: leituras locais `readText` e `readPlanText` duplicadas.

- [ ] **Step 1: Escrever testes falhando que verificam o coordenador nos botões atuais**

```kotlin
@Test fun estudoPickerUsesSharedCoordinator() {
    openMoreAndChooseEstudo(provider.estudoUri)
    assertEquals(EstudarioFileFormat.ESTUDO, fakeCoordinator.lastExpectedFormat)
}

@Test fun planPickerUsesSharedCoordinator() {
    openPlanAndChooseFile(provider.planUri)
    assertEquals(EstudarioFileFormat.PLANO, fakeCoordinator.lastExpectedFormat)
}
```

- [ ] **Step 2: Executar teste e confirmar que as telas ainda chamam ViewModels antigos**

Run: `./gradlew.bat :app:connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=br.com.estudario.ui.InternalFileImportTest`

Expected: FAIL nas expectativas do coordenador.

- [ ] **Step 3: Encaminhar URIs e remover pipelines duplicadas**

```kotlin
val importLauncher = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
    uri?.let { fileCoordinator.open(UriFileSource(it, expectedFormat = EstudarioFileFormat.ESTUDO)) }
}
```

Aplicar o mesmo padrão a plano e backup. Preservar criação/exportação de documentos, pois são fluxos de saída e não pertencem ao coordenador de importação.

- [ ] **Step 4: Executar testes de UI e regressão dos fluxos manuais**

Run: `./gradlew.bat :app:connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=br.com.estudario.ui.InternalFileImportTest`

Expected: PASS.

- [ ] **Step 5: Commitar a unidade**

```powershell
git add app/src/main/java/br/com/estudario/ui app/src/androidTest/java/br/com/estudario/ui/InternalFileImportTest.kt
git commit -m "refactor: use shared pipeline for manual imports"
```

---

### Task 10: Mensagens, documentação e verificação integral

**Files:**
- Modify: `docs/ESTUDO_FORMAT.md`
- Modify: `docs/PLANO_FORMAT.md`
- Modify: `README.md`
- Modify: `app/src/main/res/values/strings.xml` se as mensagens forem extraídas para recursos.
- Test: todos os testes da aplicação.

**Interfaces:**
- Consumes: comportamento final das Tasks 1–9.
- Produces: documentação de uso e evidência final de aceitação.

- [ ] **Step 1: Atualizar documentação com abertura externa e regras de vínculo**

Documentar os seguintes fluxos com nomes reais:

```text
Downloads → tocar em arquivo .plano → Estudario → importação → Plano
Compartilhar arquivo .estudo → Estudario → importação → tópico ou Edital
Abrir backup → Estudario → confirmação explícita → restauração
```

Incluir que registros sem `externalId` oficial não satisfazem vínculos obrigatórios de plano e que nenhuma identidade é derivada de nomes.

- [ ] **Step 2: Executar testes unitários completos**

Run: `./gradlew.bat :app:testDebugUnitTest`

Expected: BUILD SUCCESSFUL.

- [ ] **Step 3: Executar testes instrumentados completos**

Run: `./gradlew.bat :app:connectedDebugAndroidTest`

Expected: BUILD SUCCESSFUL quando houver dispositivo ou emulador conectado. Se não houver, registrar exatamente a ausência de dispositivo e executar os testes instrumentados assim que um alvo estiver disponível.

- [ ] **Step 4: Executar lint e compilação**

Run: `./gradlew.bat :app:lintDebug :app:assembleDebug`

Expected: BUILD SUCCESSFUL e APK em `app/build/outputs/apk/debug/app-debug.apk`.

- [ ] **Step 5: Revisar Manifest e conteúdo do artefato**

Run: `rg -n "ACTION_VIEW|ACTION_SEND|MANAGE_EXTERNAL_STORAGE|mimeType|scheme" app/build/intermediates/merged_manifests/debug/processDebugMainManifest/AndroidManifest.xml app/src/main/AndroidManifest.xml`

Expected: `ACTION_VIEW` e `ACTION_SEND` presentes, sem `MANAGE_EXTERNAL_STORAGE`.

- [ ] **Step 6: Executar aceitação manual em Android real**

1. Instalar `app-debug.apk`.
2. Abrir `Plano_Mestre_TRT3_TI_Otavio.plano` em Downloads.
3. Confirmar detecção do concurso por nome e uso do `externalId` oficial.
4. Confirmar importação, navegação para Plano e mensagem de sucesso.
5. Abrir o mesmo arquivo novamente e confirmar decisão de duplicidade.
6. Repetir com `.estudo` por Downloads e `ACTION_SEND` de um app compatível.
7. Abrir backup e confirmar que nenhuma restauração ocorre antes do botão explícito.

- [ ] **Step 7: Commitar documentação e ajustes finais**

```powershell
git add docs README.md app/src/main/res/values/strings.xml
git commit -m "docs: document Android file import workflow"
```

---

## Gate de conclusão

Antes de declarar a implementação concluída, comparar o resultado com cada critério de aceitação da especificação, inspecionar `git diff --check`, confirmar que os testes manuais não criam duplicatas e registrar qualquer teste instrumentado impedido por ausência de dispositivo. Nenhuma afirmação de sucesso deve ser feita com base apenas na compilação.
