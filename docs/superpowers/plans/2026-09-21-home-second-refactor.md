# Segunda refatoração da Home do Estudário Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Reorganizar a Home do Estudário como uma página pessoal do estudante, corrigindo edge-to-edge/insets, exibindo toda a cobertura do edital e melhorando a hierarquia visual com dados reais.

**Architecture:** Manter `AppViewModel`, `StudyPlanViewModel`, `ActivePlanUiState` e o domínio como fontes de verdade. Extrair o cálculo de métricas da Home para uma unidade pura testável, manter modelos simples de apresentação na pasta `home` e recompor a tela em bandas integradas: contexto, jornada, Agora, Depois, cobertura, ritmo e evolução. Tornar o shell de navegação responsável por consumir os insets uma única vez.

**Tech Stack:** Kotlin, Jetpack Compose, Material 3, Compose UI tests, JUnit 4, Android Activity edge-to-edge e Gradle Android.

**Spec:** `docs/superpowers/specs/2026-09-21-home-second-refactor-design.md`

## Global Constraints

- O motor de planejamento, as entidades Room, o formato de importação/exportação e a lógica de geração do plano permanecem inalterados.
- A política de insets será explícita e única; nenhum componente filho aplicará novamente `statusBarsPadding` ou `navigationBarsPadding` se já estiver dentro do espaço consumido pelo shell.
- Cobertura por matéria será construída para todas as `SubjectEntity` com tópicos, sem `take`, `subList` ou limite silencioso.
- A navegação inferior continuará com `Início`, `Edital`, `Plano` e `Treinar`; Perfil terá presença própria no avatar e no drawer.
- Nomes longos quebrarão de forma legível sem esmagar o percentual nem cortar o tópico principal de modo arbitrário.
- Não serão adicionados gradientes aleatórios, glassmorphism, neon ou ilustrações decorativas à Home.
- A Home removerá o bottom padding expansivo usado como compensação; o espaço da NavigationBar virá do `innerPadding` do `Scaffold`.

## Review Focus

- Status bar com edge-to-edge, notch e diferentes modos de navegação: a top bar deve começar abaixo da área segura e o fim da lista deve ficar acima da NavigationBar. Teste principal: `InsetsNavigationTest` e inspeção visual em dispositivo/emulador.
- Cinco ou mais matérias e nomes reais longos: todas devem existir na árvore sem “e mais …”, com percentual separado do nome. Testes: `HomeMetricsCalculatorTest` e `HomeComponentsPresentationTest`.
- 0%, progresso intermediário e 100% de cobertura: a trilha, o percentual e cada linha devem manter leitura coerente. Teste: `HomeComponentsPresentationTest`.
- Sem questões, sem sequência e sem data de prova: a Home deve usar empty states explicativos, sem traços ou números falsamente relevantes. Teste: `HomeComponentsPresentationTest`.
- XP parcial e fonte ampliada: o nível deve explicar o progresso para o próximo nível sem quebrar a linha. Teste: `HomeComponentsPresentationTest` e preview de fonte ampliada.

## Mapa de arquivos

- `app/src/main/java/br/com/estudario/MainActivity.kt`: habilitar edge-to-edge explicitamente antes de montar a UI.
- `app/src/main/java/br/com/estudario/ui/EstudarioApp.kt`: definir a política única de `Scaffold`, aplicar o `innerPadding` ao `NavHost` e configurar a `NavigationBar`.
- `app/src/main/java/br/com/estudario/ui/navigation/EstudarioDrawer.kt`: fazer a top bar e o drawer consumirem seus insets e preservar menu/perfil/busca.
- `app/src/main/java/br/com/estudario/ui/screens/home/HomeMetricsCalculator.kt`: novo cálculo puro das métricas e da lista completa de cobertura por matéria.
- `app/src/main/java/br/com/estudario/ui/screens/home/HomeUiModels.kt`: ampliar modelos de apresentação com posição da matéria e XP dentro/próximo do nível.
- `app/src/main/java/br/com/estudario/ui/screens/HomeScreen.kt`: remover o cálculo privado, mudar a ordem da composição e eliminar padding compensatório.
- `app/src/main/java/br/com/estudario/ui/screens/home/JourneySnapshot.kt`: novo componente integrado de “onde estou”.
- `app/src/main/java/br/com/estudario/ui/screens/home/HomeHeader.kt`: ajustar hierarquia do contexto do concurso e capitalização.
- `app/src/main/java/br/com/estudario/ui/screens/home/CurrentStudySection.kt`: reduzir o peso do cartão, manter o tópico em até três linhas e tornar o CTA compacto.
- `app/src/main/java/br/com/estudario/ui/screens/home/NextUpStrip.kt`: separar matéria, tópico e duração em linhas legíveis.
- `app/src/main/java/br/com/estudario/ui/screens/home/SyllabusCoverage.kt`: renderizar todas as matérias, preservar nomes longos e substituir o rótulo “e mais …”.
- `app/src/main/java/br/com/estudario/ui/screens/home/HomeInsights.kt`: integrar desempenho, sequência e nível com empty states e XP no nível.
- `app/src/main/java/br/com/estudario/ui/screens/home/HomePreviews.kt`: usar nomes reais longos, cinco+ matérias e estados do briefing.
- `app/src/androidTest/java/br/com/estudario/ui/InsetsNavigationTest.kt`: novo teste de regressão do shell e destinos inferiores.
- `app/src/test/java/br/com/estudario/ui/screens/home/HomeMetricsCalculatorTest.kt`: novo teste unitário da origem e completude da cobertura.
- `app/src/androidTest/java/br/com/estudario/ui/screens/home/HomeComponentsPresentationTest.kt`: novo teste de apresentação dos componentes e empty states.
- `app/src/androidTest/java/br/com/estudario/ui/PlannerNavigationTest.kt`: atualizar expectativas da navegação para a arquitetura atual sem aba duplicada “Mais”.

---

### Task 1: Corrigir a política de edge-to-edge e insets do shell

**Files:**
- Modify: `app/src/main/java/br/com/estudario/MainActivity.kt`
- Modify: `app/src/main/java/br/com/estudario/ui/EstudarioApp.kt`
- Modify: `app/src/main/java/br/com/estudario/ui/navigation/EstudarioDrawer.kt`
- Create: `app/src/androidTest/java/br/com/estudario/ui/InsetsNavigationTest.kt`
- Modify: `app/src/androidTest/java/br/com/estudario/ui/PlannerNavigationTest.kt`

**Interfaces:**
- Consumes: `EstudarioTopBar`, `NavigationBar`, `ModalNavigationDrawer` e o `NavHost` atuais.
- Produces: shell em que `Scaffold` não adiciona insets implícitos, a top bar consome `WindowInsets.statusBars`, o bottom bar consome `WindowInsets.navigationBars` e o conteúdo recebe somente o `innerPadding` real.

- [ ] **Step 1: Write the failing shell regression test**

Adicionar `InsetsNavigationTest` com o padrão já usado em `PlannerNavigationTest`:

```kotlin
@Test
fun mainDestinationsKeepIdentityActionsAndBottomNavigation() {
    val app = ApplicationProvider.getApplicationContext<EstudarioApplication>()
    compose.setContent { EstudarioApp(AppViewModel(app)) }

    compose.onNodeWithContentDescription("Abrir menu").assertExists()
    compose.onNodeWithContentDescription("Pesquisar").assertExists()
    compose.onNodeWithText("Início").assertExists()
    compose.onNodeWithText("Edital").assertExists()
    compose.onNodeWithText("Plano").assertExists()
    compose.onNodeWithText("Treinar").assertExists()
}
```

Run: `./gradlew.bat :app:connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=br.com.estudario.ui.InsetsNavigationTest`

Expected: the test exposes the current shell contract; if the app cannot start because the existing dirty worktree is incomplete, record that baseline failure before changing production code.

- [ ] **Step 2: Enable edge-to-edge explicitly**

In `MainActivity.onCreate`, call `enableEdgeToEdge()` after `super.onCreate(savedInstanceState)` and before `setContent { ... }`. Keep splash and intent handling unchanged.

- [ ] **Step 3: Make `Scaffold` and bars own disjoint inset regions**

In `EstudarioApp.kt`, change the main `Scaffold` to this policy:

```kotlin
Scaffold(
    contentWindowInsets = WindowInsets(0, 0, 0, 0),
    topBar = {
        if (showBottom) EstudarioTopBar(/* existing callbacks */)
    },
    bottomBar = {
        if (showBottom) NavigationBar(windowInsets = WindowInsets.navigationBars) {
            // existing NavigationBarItem loop
        }
    },
) { innerPadding ->
    NavHost(
        navController,
        startDestination = "home",
        modifier = Modifier
            .padding(innerPadding)
            .consumeWindowInsets(innerPadding),
    ) {
        // existing destinations
    }
}
```

Import `androidx.compose.foundation.layout.WindowInsets`, `consumeWindowInsets`, `navigationBars`, and preserve the existing `ModalNavigationDrawer`/route logic.

- [ ] **Step 4: Make custom top bar and drawer consume safe areas**

In `EstudarioDrawer.kt`:

```kotlin
Row(
    Modifier
        .fillMaxWidth()
        .windowInsetsPadding(WindowInsets.statusBars)
        .background(MaterialTheme.colorScheme.background)
        .padding(horizontal = EstudarioSpacing.small, vertical = EstudarioSpacing.tight),
    // existing content
)
```

Apply `.windowInsetsPadding(WindowInsets.safeDrawing)` to the drawer’s outer column. Do not add a numeric top padding and do not add a second status-bar modifier to `HomeScreen` or individual sections.

- [ ] **Step 5: Run the shell test and compile the affected sources**

Run:

```text
./gradlew.bat :app:connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=br.com.estudario.ui.InsetsNavigationTest
./gradlew.bat :app:compileDebugKotlin
```

Expected: the shell test passes on a connected device/emulator; if no device exists, the Kotlin compile must still pass and the exact connected-test availability error must be recorded.

- [ ] **Step 6: Commit the isolated shell change**

```text
git add app/src/main/java/br/com/estudario/MainActivity.kt app/src/main/java/br/com/estudario/ui/EstudarioApp.kt app/src/main/java/br/com/estudario/ui/navigation/EstudarioDrawer.kt app/src/androidTest/java/br/com/estudario/ui/InsetsNavigationTest.kt app/src/androidTest/java/br/com/estudario/ui/PlannerNavigationTest.kt
git commit -m "fix: consume system insets in app shell"
```

### Task 2: Extrair métricas puras e garantir a lista completa do edital

**Files:**
- Create: `app/src/main/java/br/com/estudario/ui/screens/home/HomeMetricsCalculator.kt`
- Modify: `app/src/main/java/br/com/estudario/ui/screens/home/HomeUiModels.kt`
- Modify: `app/src/main/java/br/com/estudario/ui/screens/HomeScreen.kt`
- Create: `app/src/test/java/br/com/estudario/ui/screens/home/HomeMetricsCalculatorTest.kt`

**Interfaces:**
- Consumes: `CompetitionEntity`, `SubjectEntity`, `TopicEntity`, question/attempt/error/review lists and existing `MasteryCalculator`/`ReviewPolicy`.
- Produces: `internal data class HomeMetrics` and `internal fun calculateHomeMetrics(...)` returning complete `subjects`, overall coverage, answer count and attention metrics. `HomeScreen` calls this function from `produceState`.

- [ ] **Step 1: Write the failing metric tests**

Create `HomeMetricsCalculatorTest` with real entities and two focused cases:

```kotlin
@Test
fun calculatorReturnsEverySubjectInEditalOrder() {
    val competition = CompetitionEntity(id = 1L, name = "TRT 3ª Região", isPrimary = true)
    val subjects = listOf(
        SubjectEntity(id = 10L, competitionId = competition.id, name = "LÍNGUA PORTUGUESA (NÍVEL MÉDIO/SUPERIOR)", position = 0),
        SubjectEntity(id = 11L, competitionId = competition.id, name = "ANALISTA JUDICIÁRIO – ÁREA APOIO ESPECIALIZADO – TECNOLOGIA DA INFORMAÇÃO", position = 1),
        SubjectEntity(id = 12L, competitionId = competition.id, name = "Direito Administrativo", position = 2),
        SubjectEntity(id = 13L, competitionId = competition.id, name = "Banco de Dados", position = 3),
        SubjectEntity(id = 14L, competitionId = competition.id, name = "Redes de Computadores", position = 4),
    )
    val topics = subjects.mapIndexed { index, subject ->
        TopicEntity(id = 100L + index, subjectId = subject.id, title = "Tópico ${index + 1}")
    }

    val result = calculateHomeMetrics(competition.id, subjects, topics, emptyList(), emptyList(), emptyList(), emptyList())

    assertEquals(subjects.map { it.name }, result.subjects.map { it.name })
    assertEquals(5, result.subjects.size)
}

@Test
fun calculatorCountsZeroAndFullyStudiedTopicsWithoutHidingSubjects() {
    val subjects = listOf(
        SubjectEntity(id = 20L, competitionId = 2L, name = "Português", position = 0),
        SubjectEntity(id = 21L, competitionId = 2L, name = "Direito", position = 1),
    )
    val topics = listOf(
        TopicEntity(id = 200L, subjectId = 20L, title = "Gramática", status = TopicStatus.ESTUDADO),
        TopicEntity(id = 201L, subjectId = 20L, title = "Texto"),
        TopicEntity(id = 202L, subjectId = 21L, title = "Princípios", status = TopicStatus.DOMINADO),
    )

    val result = calculateHomeMetrics(2L, subjects, topics, emptyList(), emptyList(), emptyList(), emptyList())

    assertEquals(2, result.subjects.size)
    assertEquals(66, result.coverage)
    assertEquals(listOf(50, 100), result.subjects.map { it.percent })
}
```

Run: `./gradlew.bat :app:testDebugUnitTest --tests br.com.estudario.ui.screens.home.HomeMetricsCalculatorTest`

Expected: FAIL because the calculator is not extracted and the public testable result does not exist.

- [ ] **Step 2: Extract the current calculation without changing domain rules**

Move the existing `HomeMetrics` data class and `computeHomeMetrics` body from `HomeScreen.kt` into `HomeMetricsCalculator.kt`, rename the function to `calculateHomeMetrics`, make both `internal`, and retain the existing mastery/review/error formulas. Add `position: Int` to `SubjectCoverageUi` with a default value for source compatibility, and map `SubjectEntity.position` into it. Sort subjects by `position`, then by `name` as a deterministic tie-breaker. Do not add any `take`, `subList` or database query limit.

- [ ] **Step 3: Make HomeScreen consume the extracted result**

Replace the `computeHomeMetrics(...)` call in `HomeScreen.produceState` with `calculateHomeMetrics(...)`; remove the duplicate private `HomeMetrics` and calculator implementation from `HomeScreen.kt`. Keep the existing `coverageUi` conversion and callbacks unchanged until the visual task.

- [ ] **Step 4: Run the metric tests and the existing domain suite**

Run:

```text
./gradlew.bat :app:testDebugUnitTest --tests br.com.estudario.ui.screens.home.HomeMetricsCalculatorTest
./gradlew.bat :app:testDebugUnitTest
```

Expected: both the focused tests and the complete unit-test task pass.

- [ ] **Step 5: Commit the data-boundary change**

```text
git add app/src/main/java/br/com/estudario/ui/screens/home/HomeMetricsCalculator.kt app/src/main/java/br/com/estudario/ui/screens/home/HomeUiModels.kt app/src/main/java/br/com/estudario/ui/screens/HomeScreen.kt app/src/test/java/br/com/estudario/ui/screens/home/HomeMetricsCalculatorTest.kt
git commit -m "fix: preserve complete home syllabus coverage"
```

### Task 3: Recompose the Home components around the student journey

**Files:**
- Create: `app/src/main/java/br/com/estudario/ui/screens/home/JourneySnapshot.kt`
- Modify: `app/src/main/java/br/com/estudario/ui/screens/home/HomeHeader.kt`
- Modify: `app/src/main/java/br/com/estudario/ui/screens/home/CurrentStudySection.kt`
- Modify: `app/src/main/java/br/com/estudario/ui/screens/home/NextUpStrip.kt`
- Modify: `app/src/main/java/br/com/estudario/ui/screens/home/SyllabusCoverage.kt`
- Modify: `app/src/main/java/br/com/estudario/ui/screens/home/HomeInsights.kt`
- Modify: `app/src/main/java/br/com/estudario/ui/screens/home/HomeAttention.kt`
- Create: `app/src/androidTest/java/br/com/estudario/ui/screens/home/HomeComponentsPresentationTest.kt`

**Interfaces:**
- Consumes: `SyllabusCoverageUi`, `StandingUi`, `CurrentStudyUiState`, `NextUpUi`, `PaceUi` and `PerformanceUi`.
- Produces: `JourneySnapshot(coverage: SyllabusCoverageUi, standing: StandingUi, modifier: Modifier = Modifier)` and updated existing composables with the same callback contracts.

- [ ] **Step 1: Write failing Compose presentation tests**

Create a test class using `createComposeRule()` and `EstudarioTheme(false)`:

```kotlin
@Test
fun coverageShowsAllLongSubjectNames() {
    val subjects = listOf(
        SubjectCoverageUi("LÍNGUA PORTUGUESA (NÍVEL MÉDIO/SUPERIOR)", 8, 10, 0),
        SubjectCoverageUi("ANALISTA JUDICIÁRIO – ÁREA APOIO ESPECIALIZADO – TECNOLOGIA DA INFORMAÇÃO", 7, 10, 1),
        SubjectCoverageUi("Direito Administrativo", 6, 10, 2),
        SubjectCoverageUi("Banco de Dados", 5, 10, 3),
        SubjectCoverageUi("Redes de Computadores", 4, 10, 4),
    )
    compose.setContent {
        EstudarioTheme(false) {
            SyllabusCoverage(SyllabusCoverageUi(60, 30, 50, subjects, null), onOpenSyllabus = {})
        }
    }
    subjects.forEach { compose.onNodeWithText(it.name).assertExists() }
    compose.onNodeWithText("e mais").assertDoesNotExist()
}

@Test
fun emptyPerformanceAndStreakExplainWhatToDoNext() {
    compose.setContent {
        EstudarioTheme(false) {
            PerformanceAndStanding(
                performance = null,
                standing = StandingUi(0, 0, 1, "Iniciante", 0, 0, 0, 100),
                onOpenStatistics = {},
                onOpenProfile = {},
            )
        }
    }
    compose.onNodeWithText("Ainda sem histórico de questões.").assertIsDisplayed()
    compose.onNodeWithText("Comece hoje.").assertIsDisplayed()
    compose.onNodeWithText("—").assertDoesNotExist()
}

@Test
fun levelExplainsXpInsideCurrentLevel() {
    compose.setContent {
        EstudarioTheme(false) {
            LevelRow(StandingUi(12, 21, 8, "Estudante", 420, 0.35f, 70, 200), onOpenProfile = {})
        }
    }
    compose.onNodeWithText("Nível 8").assertIsDisplayed()
    compose.onNodeWithText("70 / 200 XP").assertIsDisplayed()
}
```

Run: `./gradlew.bat :app:connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=br.com.estudario.ui.screens.home.HomeComponentsPresentationTest`

Expected: FAIL because the coverage truncation, empty copy, and level XP presentation have not been changed.

- [ ] **Step 2: Implement the integrated journey snapshot**

Create `JourneySnapshot` as a full-width composition containing the overall percentage/count, the existing segmented coverage visual as a supporting rail, and compact streak/level information. Use the existing brand colors and `EstudarioGlyph`/subject palette where appropriate. Do not use a donut, gauge, gradient or three independent cards.

- [ ] **Step 3: Rebuild the current-action component**

Keep `CurrentStudyUiState` and callbacks unchanged. In `CurrentStudySection`, keep the subject as a small context label, render `topicName` with `maxLines = 3` and `overflow = TextOverflow.Ellipsis`, put activity and duration in metadata, and use a compact 44–48 dp CTA aligned with the metadata row. Preserve loading, no-plan, no-task and completed-day states with the same navigational callbacks.

- [ ] **Step 4: Rebuild “Depois” without horizontal truncation**

In `NextUpStrip`, render each item as a `Column`/two-row block: subject in `labelMedium`, topic in `bodyMedium` with up to two lines, and duration in a separate trailing or metadata row. Keep the existing maximum of two visible activities and the link to Plano for remaining activities, but never concatenate subject and topic into a `maxLines = 1` string.

- [ ] **Step 5: Render every coverage subject with long-name-safe rows**

Remove `coverage.subjects.take(4)` and the “e mais …” branch. Change each subject row to keep the name and percentage in a top row, with the thin progress indicator below it. Allow two lines for the name and keep the percentage in a fixed-width trailing element. Preserve the segmented overview above the rows and keep the whole coverage section clickable to Edital.

- [ ] **Step 6: Replace report-like insight states**

Use sentence case section labels. For no performance data, render `Desempenho`, `Ainda sem histórico de questões.` and a compact `Resolver questões →` action. For zero streak, render `Sequência`, `Comece hoje.` and no prominent `0`. When data exists, show the current value and supporting context. Update `StandingUi`/`LevelRow` to display `xpIntoLevel / xpForNextLevel XP` and keep the progress rail visually subordinate.

- [ ] **Step 7: Run the focused presentation test and full unit tests**

Run:

```text
./gradlew.bat :app:connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=br.com.estudario.ui.screens.home.HomeComponentsPresentationTest
./gradlew.bat :app:testDebugUnitTest
```

Expected: the focused Compose assertions and all unit tests pass.

- [ ] **Step 8: Commit the component refactor**

```text
git add app/src/main/java/br/com/estudario/ui/screens/home app/src/androidTest/java/br/com/estudario/ui/screens/home/HomeComponentsPresentationTest.kt
git commit -m "feat: redesign home journey components"
```

### Task 4: Integrar a nova ordem, previews reais e navegação final

**Files:**
- Modify: `app/src/main/java/br/com/estudario/ui/screens/HomeScreen.kt`
- Modify: `app/src/main/java/br/com/estudario/ui/screens/home/HomePreviews.kt`
- Modify: `app/src/androidTest/java/br/com/estudario/ui/PlannerNavigationTest.kt`

**Interfaces:**
- Consumes: the complete `HomeMetrics`, updated component contracts and the existing app navigation callbacks.
- Produces: the final Home order and preview matrix without altering plan logic or adding duplicate navigation destinations.

- [ ] **Step 1: Add a failing order/preview contract**

Extend `HomeComponentsPresentationTest` with a compact composition using the same sections in the screen order and assert the key texts exist: `TRT 3ª Região`, `68%`, `Agora`, `Depois`, `Cobertura do edital`, `Ritmo atual`, `Desempenho` and `Nível`. The test must also assert that the long current topic exists with its full first two lines and that the “e mais” label does not exist.

Run the focused test and confirm it fails until the integration order and copy are updated.

- [ ] **Step 2: Recompose `HomeScreen` in the specified reading order**

Use this sequence inside the `LazyColumn`, with only screen gutters and meaningful section spacing:

```text
HomeHeader
JourneySnapshot
CurrentStudySection
NextUpStrip (when there is a next item)
SyllabusCoverage
PaceForecast
PerformanceAndStanding
HomeAttention (only when actionable)
LevelRow
```

Call `JourneySnapshot(coverageUi(metrics), standingUi(streak, progress))` before `CurrentStudySection`. Keep `HomeHeader` and the real plan/competition state. Replace `PaddingValues(bottom = EstudarioSpacing.expansive)` with a normal section-sized bottom value because the `Scaffold` now supplies the bottom bar inset. Do not alter `pickFocusTask`, `currentStudyState`, `nextUpToday`, `startTask` or `paceUi` semantics beyond what is needed to feed the new components.

- [ ] **Step 3: Make previews exercise real data extremes**

Update `HomePreviews.kt` so the main preview uses:

```kotlin
SubjectCoverageUi("LÍNGUA PORTUGUESA (NÍVEL MÉDIO/SUPERIOR)", 41, 50, 0)
SubjectCoverageUi("ANALISTA JUDICIÁRIO – ÁREA APOIO ESPECIALIZADO – TECNOLOGIA DA INFORMAÇÃO", 32, 45, 1)
SubjectCoverageUi("Banco de Dados", 21, 30, 2)
SubjectCoverageUi("Redes de Computadores", 16, 25, 3)
SubjectCoverageUi("Segurança da Informação", 12, 22, 4)
SubjectCoverageUi("Direito Administrativo", 4, 18, 5)
```

Keep previews for normal, no plan, day complete, almost done, behind, dark mode, large font, no exam date and no contest. Use `StandingUi` values that exercise `0 / 100 XP` and partial `70 / 200 XP`.

- [ ] **Step 4: Align navigation regression expectations**

Update `PlannerNavigationTest` to assert exactly the four bottom destinations (`Início`, `Edital`, `Plano`, `Treinar`), open the drawer through `Abrir menu`, and assert drawer destinations such as `Desempenho`, `Caderno de erros` and `Ajustes`. Remove the stale expectation that `Mais` is a bottom tab; `MoreScreen` remains a drawer destination.

- [ ] **Step 5: Run the final focused integration checks**

Run:

```text
./gradlew.bat :app:connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=br.com.estudario.ui.PlannerNavigationTest
./gradlew.bat :app:connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=br.com.estudario.ui.screens.home.HomeComponentsPresentationTest
./gradlew.bat :app:compileDebugAndroidTestKotlin
```

Expected: navigation and presentation tests pass; instrumented sources compile even if no device is available.

- [ ] **Step 6: Commit the integrated Home composition**

```text
git add app/src/main/java/br/com/estudario/ui/screens/HomeScreen.kt app/src/main/java/br/com/estudario/ui/screens/home/HomePreviews.kt app/src/androidTest/java/br/com/estudario/ui/PlannerNavigationTest.kt
git commit -m "feat: compose student-first home flow"
```

### Task 5: Full verification and visual review

**Files:**
- No new production files; review the complete diff and generated APK.
- Verify: all files changed by Tasks 1–4 plus pre-existing worktree changes are kept separate and not accidentally reverted.

**Interfaces:**
- Consumes: the completed Home, shell and test changes.
- Produces: fresh evidence for build, unit tests, instrumented compilation and connected tests when a device exists.

- [ ] **Step 1: Inspect the final diff and search for the original defects**

Run:

```text
git status --short
git diff --stat
rg -n "subjects\.take\(|e mais .*matéria|maxLines = 1|PaddingValues\(.*expansive|AGORA|DEPOIS|COBERTURA DO EDITAL|DESEMPENHO|CONSTÂNCIA" app/src/main/java/br/com/estudario/ui/screens app/src/main/java/br/com/estudario/ui/navigation
```

Confirm that no coverage truncation remains, essential NextUp content is not forced into one line, and section labels use the new hierarchy intentionally.

- [ ] **Step 2: Build and run the complete unit suite**

Run:

```text
./gradlew.bat :app:assembleDebug
./gradlew.bat :app:testDebugUnitTest
```

Expected: both commands exit with code 0 and the unit suite reports no failures.

- [ ] **Step 3: Compile and run instrumented tests**

Run:

```text
./gradlew.bat :app:compileDebugAndroidTestKotlin
./gradlew.bat :app:connectedDebugAndroidTest
```

Expected: instrumented Kotlin compilation succeeds. If `connectedDebugAndroidTest` cannot run because no device/emulator is attached, record the exact Gradle/ADB message rather than treating it as a UI failure.

- [ ] **Step 4: Perform visual QA at the target states**

Open the Home previews and, if a device/emulator is available, inspect a viewport near 360 dp with system bars visible. Verify: no text under the clock, no duplicated insets, complete subject list, long names wrapping, Now/coverage/streak visible in the first fold, and the final list item scrollable above the bottom bar. Also inspect dark mode and font scale 1.6 previews.

- [ ] **Step 5: Re-check scope and report evidence**

Confirm that no plan engine, Room entity, import/export format or persisted navigation contract was changed. Report the exact files altered, the status-bar cause/fix, the subject-omission cause/fix, hierarchy changes, states tested, and build/test results. Do not claim visual/device success without fresh output or visual inspection evidence.
