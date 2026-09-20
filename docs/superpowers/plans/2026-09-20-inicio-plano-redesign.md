# Redesign da tela inicial e do plano Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Transformar Início e Plano em uma experiência mobile-first, profissional, legível e totalmente em português, preservando o motor, o banco e os dados atuais.

**Architecture:** Manter `StudyPlanViewModel` e `ActivePlanUiState` como fonte de dados. Separar textos de apresentação e componentes visuais reutilizáveis, deixando `HomeScreen` e `PlanScreen` responsáveis apenas por composição e navegação. Usar uma coluna vertical como padrão e manter o calendário e as missões como componentes independentes, testáveis por Compose.

**Tech Stack:** Kotlin, Jetpack Compose, Material 3, Compose UI tests, JUnit 4, Gradle Android.

**Spec:** `docs/superpowers/specs/2026-09-20-inicio-plano-redesign.md`

## Global Constraints

- Não alterar o motor de planejamento, o banco ou o formato de importação/exportação.
- Reutilizar `ActivePlanUiState`, `PlannerTaskUi`, `StudyPlanViewModel` e os cálculos existentes.
- Manter o `applicationId` `br.com.estudario`.
- Nenhuma permissão de calendário, widget ou integração externa pode ser necessária para abrir o app.
- Todo texto visível de Início, Plano, calendário, missões e widget deve estar em português.
- Em larguras pequenas, conteúdo essencial não pode depender de duas colunas estreitas.

## Review Focus

- Tela com largura de 360 dp: missão principal, matéria, tópico e botão continuam legíveis.
- Plano sem tarefas ou sem plano ativo: estado vazio explica exatamente o próximo passo.
- Tarefa atrasada, em andamento, concluída ou pausada: status e ação não podem aparecer em inglês nem ficar ambíguos.
- Permissão de calendário negada: o Plano continua abrindo e exibindo as missões normalmente.
- App iniciado sem widget, sem calendário e sem dados: nenhum componente opcional pode causar crash.

### Task 1: Camada de apresentação em português

**Files:**
- Create: `app/src/main/java/br/com/estudario/ui/planner/PlannerPresentation.kt`
- Test: `app/src/test/java/br/com/estudario/ui/planner/PlannerPresentationTest.kt`
- Modify: `app/src/main/java/br/com/estudario/ui/components/MissionCard.kt`
- Modify: `app/src/main/java/br/com/estudario/ui/planner/CalendarScreen.kt`
- Modify: `app/src/main/java/br/com/estudario/ui/widget/EstudarioWidget.kt`

**Interfaces:**
- Produces `PlanTaskType.displayNamePtBr(): String`, `PlanTaskStatus.displayNamePtBr(): String`, `PlanPriority.displayNamePtBr(): String`, `minutesLabelPtBr(Int): String` and `completionActionPtBr(PlanTaskStatus): String`.
- Consumers use these functions instead of `enum.name.lowercase()` or labels técnicos.

- [ ] **Step 1: Write the failing tests**

```kotlin
class PlannerPresentationTest {
    @Test fun taskTypesUsePortugueseLabels() {
        assertEquals("Recordação ativa", PlanTaskType.ACTIVE_RECALL.displayNamePtBr())
        assertEquals("Questões", PlanTaskType.QUESTIONS.displayNamePtBr())
    }

    @Test fun statusesAndActionsUsePortugueseLabels() {
        assertEquals("Em andamento", PlanTaskStatus.EM_ANDAMENTO.displayNamePtBr())
        assertEquals("Continuar", completionActionPtBr(PlanTaskStatus.EM_ANDAMENTO))
        assertEquals("Começar", completionActionPtBr(PlanTaskStatus.PLANEJADA))
    }

    @Test fun minutesAreReadableForShortAndLongSessions() {
        assertEquals("45 min", minutesLabelPtBr(45))
        assertEquals("1h 20min", minutesLabelPtBr(80))
    }
}
```

- [ ] **Step 2: Run the focused test and verify it fails**

Run: `./gradlew.bat :app:testDebugUnitTest --tests br.com.estudario.ui.planner.PlannerPresentationTest`

Expected: FAIL because the presentation functions do not exist yet.

- [ ] **Step 3: Implement the presentation layer**

Create explicit `when` mappings for every `PlanTaskType`, `PlanTaskStatus` and `PlanPriority`. Do not use `name`, `lowercase()` or English fallback text in UI-facing functions. Format minutes as `X min` below one hour and `Hh Mmin` at one hour or more.

- [ ] **Step 4: Replace technical labels in existing surfaces**

Use the new functions in `MissionCard`, `CalendarScreen` and `EstudarioWidget`. Keep persisted enum values unchanged. Ensure the widget says “PRÓXIMA MISSÃO”, “Tudo concluído”, “Começar estudo” and uses Portuguese duration/status text.

- [ ] **Step 5: Run the focused test and verify it passes**

Run: `./gradlew.bat :app:testDebugUnitTest --tests br.com.estudario.ui.planner.PlannerPresentationTest`

Expected: PASS with all presentation cases green.

- [ ] **Step 6: Commit the isolated task**

Run: `git add app/src/main/java/br/com/estudario/ui/planner/PlannerPresentation.kt app/src/test/java/br/com/estudario/ui/planner/PlannerPresentationTest.kt app/src/main/java/br/com/estudario/ui/components/MissionCard.kt app/src/main/java/br/com/estudario/ui/planner/CalendarScreen.kt app/src/main/java/br/com/estudario/ui/widget/EstudarioWidget.kt && git commit -m "feat: localize planner presentation to Portuguese"`

### Task 2: Missão principal e tela Início responsiva

**Files:**
- Create: `app/src/main/java/br/com/estudario/ui/components/StudyFocusCard.kt`
- Modify: `app/src/main/java/br/com/estudario/ui/screens/HomeScreen.kt`
- Modify: `app/src/main/java/br/com/estudario/ui/components/MissionCard.kt`
- Test: `app/src/androidTest/java/br/com/estudario/ui/components/StudyFocusCardTest.kt`

**Interfaces:**
- `StudyFocusCard(task: PlannerTaskUi?, onStart: () -> Unit, onOpenPlan: () -> Unit, modifier: Modifier = Modifier)` renders the primary “Estudar agora” action.
- `MissionCard` keeps its existing callback contract but gains a larger readable layout and Portuguese presentation labels.

- [ ] **Step 1: Write the failing Compose tests**

```kotlin
@Test fun focusCardShowsThePrimaryActionAndTaskDetails() {
    compose.setContent {
        EstudarioTheme(false) {
            StudyFocusCard(sampleTaskUi(), onStart = {}, onOpenPlan = {})
        }
    }
    compose.onNodeWithText("Estudar agora").assertIsDisplayed()
    compose.onNodeWithText("Direito Constitucional").assertIsDisplayed()
    compose.onNodeWithText("Começar").assertIsDisplayed()
}

@Test fun focusCardExplainsWhenThereIsNoMission() {
    compose.setContent {
        EstudarioTheme(false) {
            StudyFocusCard(null, onStart = {}, onOpenPlan = {})
        }
    }
    compose.onNodeWithText("Seu próximo estudo aparece aqui").assertIsDisplayed()
    compose.onNodeWithText("Abrir plano").assertIsDisplayed()
}
```

- [ ] **Step 2: Run the focused instrumented test and verify it fails**

Run: `./gradlew.bat :app:connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=br.com.estudario.ui.components.StudyFocusCardTest`

Expected: FAIL because `StudyFocusCard` and its new copy do not exist.

- [ ] **Step 3: Implement `StudyFocusCard`**

Use a full-width `ElevatedCard` with a prominent title, subject and topic hierarchy, duration/status row, and one primary filled button. Add a secondary text button only for “Abrir plano”. Keep all hit targets at least 48 dp and do not put the primary card beside another card.

- [ ] **Step 4: Recompose `HomeScreen` as a vertical reading flow**

Keep the existing data collection and callbacks, but replace the two-column metric rows with full-width sections in this order: header, focus card, “Plano de hoje”, today missions, “Seu progresso”, forecast, weekly rhythm, and secondary review/conquest card. Remove duplicate “Continuar” behavior that currently starts focus and navigates to a topic at the same time; the primary action must execute one clearly defined path.

- [ ] **Step 5: Improve `MissionCard` hierarchy**

Increase internal padding, make the subject a small uppercase eyebrow, keep the topic as the largest text, move duration/status into a readable metadata row, and keep “Começar”/“Continuar” visible without opening a menu. Render `Reprogramar` and `Pular` only for overdue tasks.

- [ ] **Step 6: Run focused tests and verify they pass**

Run: `./gradlew.bat :app:connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=br.com.estudario.ui.components.StudyFocusCardTest`

Expected: PASS on the supported Android device/emulator.

- [ ] **Step 7: Commit the isolated task**

Run: `git add app/src/main/java/br/com/estudario/ui/components/StudyFocusCard.kt app/src/main/java/br/com/estudario/ui/screens/HomeScreen.kt app/src/main/java/br/com/estudario/ui/components/MissionCard.kt app/src/androidTest/java/br/com/estudario/ui/components/StudyFocusCardTest.kt && git commit -m "feat: redesign home focus flow"`

### Task 3: Resumo e navegação do Plano

**Files:**
- Create: `app/src/main/java/br/com/estudario/ui/planner/PlanProgressHeader.kt`
- Modify: `app/src/main/java/br/com/estudario/ui/planner/PlanScreen.kt`
- Modify: `app/src/main/java/br/com/estudario/ui/planner/CalendarScreen.kt`
- Modify: `app/src/main/java/br/com/estudario/ui/planner/PlannerComponents.kt`
- Test: `app/src/androidTest/java/br/com/estudario/ui/planner/PlanProgressHeaderTest.kt`

**Interfaces:**
- `PlanProgressHeader(state: ActivePlanUiState, modifier: Modifier = Modifier)` shows totals, minutes, coverage and forecast from the existing state.
- `PlanSection` labels remain `Hoje`, `Semana`, `Mês` and gain `Visão geral` only if the current state supports it without adding a new persistence model.

- [ ] **Step 1: Write the failing Compose tests**

```kotlin
@Test fun progressHeaderExplainsThePlanInPortuguese() {
    compose.setContent {
        EstudarioTheme(false) {
            PlanProgressHeader(sampleActivePlanState())
        }
    }
    compose.onNodeWithText("Progresso do plano").assertIsDisplayed()
    compose.onNodeWithText("Hoje").assertIsDisplayed()
    compose.onNodeWithText("Previsão de conclusão").assertIsDisplayed()
}
```

- [ ] **Step 2: Run the focused test and verify it fails**

Run: `./gradlew.bat :app:connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=br.com.estudario.ui.planner.PlanProgressHeaderTest`

Expected: FAIL because the new progress header does not exist.

- [ ] **Step 3: Implement `PlanProgressHeader`**

Use one prominent full-width card. Show completed/total missions, planned/actual minutes, coverage when available, and the forecast date in Portuguese. If a value is unavailable, show an explanatory label instead of zero or an English fallback.

- [ ] **Step 4: Rebuild the Plan top-level flow**

Keep the existing actions for help, import, create and management, but place them after the title and progress header. Make the active section control visually prominent and label it in Portuguese. The initial section remains `Hoje`.

- [ ] **Step 5: Make calendar and mission grouping consistent**

Use the weekly calendar as the default, show the selected date clearly, place the day summary before mission cards, and keep “Ver mês” as an explicit secondary action. Group missions by day in weekly/monthly views and avoid showing raw date/enum values.

- [ ] **Step 6: Run focused tests and verify they pass**

Run: `./gradlew.bat :app:connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=br.com.estudario.ui.planner.PlanProgressHeaderTest`

Expected: PASS.

- [ ] **Step 7: Commit the isolated task**

Run: `git add app/src/main/java/br/com/estudario/ui/planner/PlanProgressHeader.kt app/src/main/java/br/com/estudario/ui/planner/PlanScreen.kt app/src/main/java/br/com/estudario/ui/planner/CalendarScreen.kt app/src/main/java/br/com/estudario/ui/planner/PlannerComponents.kt app/src/androidTest/java/br/com/estudario/ui/planner/PlanProgressHeaderTest.kt && git commit -m "feat: redesign planner overview"`

### Task 4: Estados, idioma e integração visual final

**Files:**
- Modify: `app/src/main/java/br/com/estudario/ui/screens/HomeScreen.kt`
- Modify: `app/src/main/java/br/com/estudario/ui/planner/PlanScreen.kt`
- Modify: `app/src/main/java/br/com/estudario/ui/planner/CalendarScreen.kt`
- Modify: `app/src/main/java/br/com/estudario/ui/components/MissionCard.kt`
- Modify: `app/src/main/java/br/com/estudario/ui/widget/EstudarioWidget.kt`
- Modify: `app/src/main/java/br/com/estudario/ui/EstudarioApp.kt`
- Test: `app/src/androidTest/java/br/com/estudario/ui/PlannerNavigationTest.kt`
- Test: `app/src/androidTest/java/br/com/estudario/ui/planner/PlanHierarchyScreensTest.kt`
- Test: `app/src/androidTest/java/br/com/estudario/ui/planner/TodayPlanScreenTest.kt`

- [ ] **Step 1: Write failing coverage for empty and optional states**

Add tests that render an empty `ActivePlanUiState`, deny calendar synchronization through the callback without throwing, and verify the UI still shows “Gerar planejamento”, “Abrir plano” or the relevant Portuguese next action. Add an app navigation assertion that the labels `Início`, `Edital`, `Plano`, `Treinar` and `Mais` remain visible.

- [ ] **Step 2: Run the regression tests and verify the new assertions fail or expose missing copy**

Run: `./gradlew.bat :app:connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=br.com.estudario.ui.PlannerNavigationTest` and the two planner test classes.

Expected: any missing Portuguese labels or broken callbacks are reported before final cleanup.

- [ ] **Step 3: Fix all visible English and technical fallbacks**

Search the scoped UI files for `name`, `lowercase`, `planned`, `status`, `subject`, `topic`, `Start`, `Continue`, `Today`, `Week`, `Month`, `Year`, `Mission`, `Progress` and replace user-facing occurrences with the presentation layer or Portuguese copy. Do not rename database fields or enum constants.

- [ ] **Step 4: Verify accessibility and narrow layouts**

Give every icon-only action a Portuguese `contentDescription`, keep primary actions at least 48 dp, ensure long subject/topic names wrap or ellipsize intentionally, and remove horizontal layouts that make the main action smaller than the body text on a 360 dp device.

- [ ] **Step 5: Run the full verification suite**

Run:

```text
./gradlew.bat :app:assembleDebug
./gradlew.bat :app:testDebugUnitTest
./gradlew.bat :app:compileDebugAndroidTestKotlin
./gradlew.bat :app:connectedDebugAndroidTest
```

Expected: APK build succeeds, unit tests pass, Android tests compile, and connected tests pass. If a device blocks installation, report the exact device error and retain the successful build/unit/compile evidence.

- [ ] **Step 6: Commit the final integration task**

Run: `git add app/src/main/java app/src/androidTest/java app/src/test/java && git commit -m "feat: polish home and planner experience"`

## Self-review checklist

- Every requirement from the spec is covered by at least one task.
- All new public/internal component names and signatures are defined above.
- No task changes the planning engine, database schema or persisted enum values.
- English labels are handled through a dedicated presentation layer rather than scattered conditionals.
- Narrow-screen behavior, empty states, optional permissions and app startup are explicitly tested.
