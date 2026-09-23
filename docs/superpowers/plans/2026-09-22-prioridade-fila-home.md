# Home Queue Priority Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Make the next available study queue topic the Home’s current study item, and fall back to the plan only when no queue topic is available.

**Architecture:** Add one pure queue-selection rule over the already ordered `QueueWithTopic` list. Map its result into the existing Home card and “Depois” strip; when the queue has no unpaused, unfinished topic, preserve the existing planner selection. Use the same eligibility rule in QueueScreen so “Próximo estudo” matches Home.

**Tech Stack:** Kotlin, Jetpack Compose Material 3, Room 2.7.2, Navigation Compose 2.9.3, JUnit 4.13.2, AndroidX Compose UI tests.

**Spec:** `docs/superpowers/specs/2026-09-22-modo-foco-e-fila-design.md`

## Global Constraints

- The Home’s current study is the first non-paused, non-completed queue topic ordered by queue position.
- Plan tasks become the current study only when the queue has no available topic.
- A topic stays in the queue until the explicit topic completion removes it.
- Paused or already completed topics are not presented as the next queue study.
- Ending focus does not change queue order or remove a queue topic.
- Preserve pre-existing workspace edits. Before each commit, inspect the diff and stage only the hunks belonging to that task; do not stage an already-dirty file wholesale.

## Review Focus

- A paused row at position zero must not hide an unpaused row at position one; pin in `HomeQueueSelectorTest.skipsPausedFirstRow`.
- A stale row whose topic is already `ESTUDADO`, `REVISANDO`, or `DOMINADO` must be skipped; pin in `HomeQueueSelectorTest.skipsCompletedTopicAndChoosesNext`.
- An all-paused queue must allow the plan fallback; pin in `HomeQueueSelectorTest.returnsNullWhenEveryQueueItemIsUnavailable`.
- A queue item must still win when no study plan exists; pin in `HomeQueuePresentationTest.queueTopicCanBeCurrentWithoutAPlan`.
- After completion removes the current topic, the next available queue row must become current; pin in `StudyRepositoryCompletionTest.completingCurrentQueueTopicRevealsNextTopic`.

---

## File Map

- `HomeQueueSelector.kt` owns the pure eligibility/order rule for Home.
- `HomeScreen.kt` maps the selected queue topic to the current card, adjusts the primary action, and uses the remaining queue for “Depois”.
- `HomeUiModels.kt` and `NextUpStrip.kt` keep the existing composables simple while allowing a queue-specific “open queue” action.
- `EstudarioApp.kt` connects the Home queue strip to the existing Queue route.
- `QueueScreen.kt` uses the same eligible-item rule when it labels the next row.
- Unit and Compose tests cover paused rows, completed topics, the plan fallback, and the displayed current item.

## Task 1: Define and test the queue eligibility rule

**Files:**
- Create: `app/src/main/java/br/com/estudario/ui/screens/home/HomeQueueSelector.kt`
- Create: `app/src/test/java/br/com/estudario/ui/screens/home/HomeQueueSelectorTest.kt`
- Modify: `app/src/main/java/br/com/estudario/data/local/Entities.kt` only if a queue eligibility adapter is needed; otherwise do not change it.

**Interfaces:**
- Consumes: `List<QueueWithTopic>` where rows are already ordered by `paused, position` from `AppDao.queue()`.
- Produces: `internal fun firstEligibleQueueTopic(items: List<QueueWithTopic>): QueueWithTopic?`.

Use this fixture in selector and label tests:

```kotlin
private fun queueRow(
    id: Long,
    position: Int,
    paused: Boolean = false,
    status: TopicStatus,
) = QueueWithTopic(
    item = StudyQueueEntity(id = id, topicId = id, position = position, paused = paused),
    topic = TopicEntity(id = id, subjectId = 1, title = "Tópico $id", status = status),
)
```

- [ ] **Step 1: Add failing tests for paused and completed rows**

```kotlin
@Test
fun skipsPausedFirstRow() {
    val paused = queueRow(id = 1, position = 0, paused = true, status = TopicStatus.NAO_ESTUDADO)
    val ready = queueRow(id = 2, position = 1, paused = false, status = TopicStatus.EM_ESTUDO)
    assertEquals(2L, firstEligibleQueueTopic(listOf(paused, ready))?.item?.topicId)
}

@Test
fun skipsCompletedTopicAndChoosesNext() {
    val complete = queueRow(id = 1, position = 0, status = TopicStatus.ESTUDADO)
    val ready = queueRow(id = 2, position = 1, status = TopicStatus.NAO_ESTUDADO)
    assertEquals(2L, firstEligibleQueueTopic(listOf(complete, ready))?.item?.topicId)
}
```

Add cases for all-paused and all-completed queues returning `null`.

- [ ] **Step 2: Run the selector tests to confirm the function is missing**

Run: `.\gradlew.bat :app:testDebugUnitTest --tests br.com.estudario.ui.screens.home.HomeQueueSelectorTest`

Expected: compilation fails because `firstEligibleQueueTopic` does not exist.

- [ ] **Step 3: Implement queue selection as a pure function**

```kotlin
internal fun firstEligibleQueueTopic(items: List<QueueWithTopic>): QueueWithTopic? =
    items.asSequence()
        .filter { !it.item.paused }
        .filter { it.topic.status !in setOf(TopicStatus.ESTUDADO, TopicStatus.REVISANDO, TopicStatus.DOMINADO) }
        .minByOrNull { it.item.position }
```

Keep `EM_ESTUDO` eligible. An all-paused or all-completed list returns `null` so the caller can use the existing plan fallback.

- [ ] **Step 4: Run selector tests and commit**

Run: `.\gradlew.bat :app:testDebugUnitTest --tests br.com.estudario.ui.screens.home.HomeQueueSelectorTest`

Expected: paused and completed rows are skipped, the minimum-position active row wins, and unavailable queues return `null`.

```powershell
git add app/src/main/java/br/com/estudario/ui/screens/home/HomeQueueSelector.kt app/src/test/java/br/com/estudario/ui/screens/home/HomeQueueSelectorTest.kt
git commit -m "feat: select next available queue topic"
```

## Task 2: Use queue priority in the Home card and continuation strip

**Files:**
- Modify: `app/src/main/java/br/com/estudario/ui/screens/HomeScreen.kt`
- Modify: `app/src/main/java/br/com/estudario/ui/screens/home/HomeUiModels.kt`
- Modify: `app/src/main/java/br/com/estudario/ui/screens/home/NextUpStrip.kt`
- Modify: `app/src/main/java/br/com/estudario/ui/screens/home/HomePreviews.kt`
- Modify: `app/src/main/java/br/com/estudario/ui/EstudarioApp.kt`
- Create: `app/src/test/java/br/com/estudario/ui/screens/home/HomeQueuePresentationTest.kt`

**Interfaces:**
- Consumes: `firstEligibleQueueTopic` from Task 1, `AppViewModel.queue`, and the existing planner `ActivePlanUiState`.
- Produces: Home state where a queue row can be the `CurrentStudyUiState.Ready` source even without a plan; queue “Depois” entries open the Queue route.

Add pure helpers `queueTopicUi(row: QueueWithTopic, subjectName: String): StudyTaskUi` and
`currentHomeTask(queueTask: StudyTaskUi?, planTask: StudyTaskUi?): StudyTaskUi?`. The queue task
wins when available; otherwise the planner task remains the fallback. Keep the existing planner
loading, no-plan, and day-complete states when neither task is available.

Map queue items with ID `queue:<queueItem.id>`, the topic and subject names, activity label
`Fila de estudos`, empty duration, CTA `Abrir tópico`, and `scheduledForToday = false`.

- [ ] **Step 1: Add tests showing a queue topic wins over a plan task**

```kotlin
@Test
fun queueTopicIsShownAsCurrentBeforePlanTask() {
    val queue = StudyTaskUi("queue:1", 1L, "Direito", "Constitucional", "Tópico da fila", "", "Estudar", false)
    val plan = StudyTaskUi("plan:1", 2L, "Português", "Concordância", "Plano", "30 min", "Continuar", true)
    assertEquals(queue, currentHomeTask(queue, plan))
}
```

```kotlin
@Test
fun planTaskIsFallbackWhenNoQueueTopicIsAvailable() {
    val plan = StudyTaskUi("plan:1", 2L, "Português", "Concordância", "Plano", "30 min", "Continuar", true)
    assertEquals(plan, currentHomeTask(queueTask = null, planTask = plan))
}

@Test
fun queueTopicCanBeCurrentWithoutAPlan() {
    val queue = StudyTaskUi("queue:1", 1L, "Direito", "Constitucional", "Tópico da fila", "", "Estudar", false)
    assertEquals(queue, currentHomeTask(queueTask = queue, planTask = null))
}

@Test
fun queueTopicMapsSubjectAndTopicForTheHomeCard() {
    val mapped = queueTopicUi(queueRow(1, 0, status = TopicStatus.EM_ESTUDO), "Direito")
    assertEquals("Direito", mapped.subjectName)
    assertEquals("Tópico 1", mapped.topicName)
    assertEquals(1L, mapped.topicId)
}
```

- [ ] **Step 2: Run the Home presentation test and confirm queue state is not mapped yet**

Run: `.\gradlew.bat :app:testDebugUnitTest --tests br.com.estudario.ui.screens.home.HomeQueuePresentationTest`

Expected: compilation fails because the queue presentation helpers do not exist yet.

- [ ] **Step 3: Map the queue topic into the existing Home card**

Collect `viewModel.queue` in `HomeScreen`, select with `firstEligibleQueueTopic`, and map it with `queueTopicUi`. Compute the existing plan `CurrentStudyUiState`, extract its task only when it is `Ready`, and pass that task with the queue task through `currentHomeTask`. If the queue task wins, present it as `Ready` with no plan progress; if no queue task wins, preserve the existing planner state, including loading, no-plan, and day-complete states. On the primary action, open a selected queue topic with `onTopic(topicId)`; when the plan remains current, keep its existing `startTask` behavior.

- [ ] **Step 4: Build “Depois” from the remaining available queue rows**

When an eligible current queue item exists, show only the other eligible queue topics in “Depois”; do not expose the next plan block there. Add an `onOpenQueue` callback to `HomeScreen`/`NextUpStrip` and connect it to `navController.navigate("queue")`. When there is no eligible queue row, preserve the current plan-based `nextUpToday` and its Plan link.

- [ ] **Step 5: Run Home selector and presentation checks**

Run: `.\gradlew.bat :app:testDebugUnitTest --tests br.com.estudario.ui.screens.home.HomeQueueSelectorTest`

Run: `.\gradlew.bat :app:testDebugUnitTest --tests br.com.estudario.ui.screens.home.HomeQueuePresentationTest`

Expected: active queue topic is the current Home card, remaining queue topics appear in order, and Plan remains the fallback when the queue has no available rows.

- [ ] **Step 6: Commit Home integration**

```powershell
git add app/src/main/java/br/com/estudario/ui/screens/HomeScreen.kt app/src/main/java/br/com/estudario/ui/screens/home/HomeUiModels.kt app/src/main/java/br/com/estudario/ui/screens/home/NextUpStrip.kt app/src/main/java/br/com/estudario/ui/screens/home/HomePreviews.kt app/src/main/java/br/com/estudario/ui/EstudarioApp.kt app/src/test/java/br/com/estudario/ui/screens/home/HomeQueuePresentationTest.kt
git commit -m "feat: prioritize study queue on Home"
```

## Task 3: Label the actual next queue row

**Files:**
- Modify: `app/src/main/java/br/com/estudario/ui/screens/QueueScreen.kt`
- Create: `app/src/main/java/br/com/estudario/ui/screens/QueuePresentation.kt`
- Create: `app/src/test/java/br/com/estudario/ui/screens/QueuePresentationTest.kt`
- Modify: `app/src/androidTest/java/br/com/estudario/data/StudyRepositoryCompletionTest.kt`

**Interfaces:**
- Consumes: `firstEligibleQueueTopic` from Task 1 and the ordered queue flow.
- Produces: `queueRowLabel(paused, completed, isNext)` returning `Pausado`, `Concluído`, `Próximo estudo`, or `Na fila` consistently with Home.

- [ ] **Step 1: Add tests for row labels and queue advancement**

```kotlin
@Test
fun firstUnpausedRowIsNextEvenWhenItIsNotTheFirstRow() {
    assertEquals("Pausado", queueRowLabel(paused = true, completed = false, isNext = false))
    assertEquals("Próximo estudo", queueRowLabel(paused = false, completed = false, isNext = true))
}

@Test
fun pausedRowsKeepPausedLabelWhenAnotherRowIsNext() {
    assertEquals("Pausado", queueRowLabel(paused = true, completed = false, isNext = false))
}

@Test
fun completedStaleRowsCannotBeLabeledAsNext() {
    assertEquals("Concluído", queueRowLabel(paused = false, completed = true, isNext = false))
}
```

Implement the pure presentation helper as `internal fun queueRowLabel(paused: Boolean, completed: Boolean, isNext: Boolean): String`, with paused and completed states taking precedence over the next label.

In `StudyRepositoryCompletionTest`, enqueue a second topic, complete the first, and assert the first ID is removed while the second remains at the front.

- [ ] **Step 2: Run the queue presentation and repository tests to establish the failures**

Run: `.\gradlew.bat :app:testDebugUnitTest --tests br.com.estudario.ui.screens.QueuePresentationTest`

Run: `.\gradlew.bat :app:connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=br.com.estudario.data.StudyRepositoryCompletionTest`

Expected: queue label helper is missing; repository advancement test fails until it covers two queue entries.

- [ ] **Step 3: Share eligibility with QueueScreen**

Compute the `item.id` of `firstEligibleQueueTopic(queue)`. Label paused entries “Pausado” and stale completed entries “Concluído”; label the eligible item “Próximo estudo” even if it is not row zero; label other eligible entries “Na fila”. If all rows are paused or completed, do not label any row as next.

- [ ] **Step 4: Verify completion advances the displayed item and commit**

Run: `.\gradlew.bat :app:testDebugUnitTest --tests br.com.estudario.ui.screens.QueuePresentationTest`

Run: `.\gradlew.bat :app:connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=br.com.estudario.data.StudyRepositoryCompletionTest`

Expected: QueueScreen and Home select the same item; completing that topic removes it and exposes the next queue item.

```powershell
git add app/src/main/java/br/com/estudario/ui/screens/QueueScreen.kt app/src/main/java/br/com/estudario/ui/screens/QueuePresentation.kt app/src/test/java/br/com/estudario/ui/screens/QueuePresentationTest.kt app/src/androidTest/java/br/com/estudario/data/StudyRepositoryCompletionTest.kt
git commit -m "fix: show the correct next study queue item"
```

## Spec Coverage

- Queue selection and plan fallback: Tasks 1–2.
- Home card and “Depois” reflect queue order: Task 2.
- Queue row label matches the next available item: Task 3.
- Completion removes the current topic and reveals the next one: Task 3.
