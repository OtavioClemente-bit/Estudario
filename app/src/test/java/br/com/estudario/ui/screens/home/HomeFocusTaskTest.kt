package br.com.estudario.ui.screens.home

import br.com.estudario.data.local.planner.PlanTaskEntity
import br.com.estudario.data.local.planner.StudyAvailabilityEntity
import br.com.estudario.data.local.planner.StudyDayOverrideEntity
import br.com.estudario.data.local.planner.StudyPlanEntity
import br.com.estudario.data.local.QueueWithTopic
import br.com.estudario.data.local.StudyQueueEntity
import br.com.estudario.data.local.TopicEntity
import br.com.estudario.domain.planner.PlanPriority
import br.com.estudario.domain.planner.PlanTaskStatus
import br.com.estudario.domain.planner.PlanTaskType
import br.com.estudario.ui.planner.ActivePlanUiState
import br.com.estudario.ui.planner.PlannerTaskUi
import br.com.estudario.ui.screens.pickFocusTask
import br.com.estudario.ui.screens.currentStudyState
import br.com.estudario.ui.screens.pickHomeQueueTopic
import java.time.LocalDate
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class HomeFocusTaskTest {
    private val sunday = LocalDate.of(2026, 10, 25)
    private val plan = StudyPlanEntity("active", 1, "Plano", "Concurso", sunday.toEpochDay(), active = true)

    @Test
    fun sundayOffDoesNotPromoteMondaysTaskToNow() {
        val state = state(tasks = listOf(task("monday", sunday.plusDays(1))))

        assertNull(pickFocusTask(state))
    }

    @Test
    fun weekdayWithoutScheduledStudyDoesNotPromoteFutureTask() {
        val wednesday = sunday.plusDays(3)
        val state = state(tasks = listOf(task("thursday", wednesday.plusDays(1)))).copy(today = wednesday)

        assertNull(pickFocusTask(state))
    }

    @Test
    fun overdueInProgressStaysOutOfNowOnDayOff() {
        val state = state(tasks = listOf(
            task("overdue", sunday.minusDays(1), PlanTaskStatus.EM_ANDAMENTO),
            task("monday", sunday.plusDays(1)),
        ))

        assertNull(pickFocusTask(state))
    }

    @Test
    fun completedTodayDoesNotPromoteFutureOrOverdueTask() {
        val state = state(tasks = listOf(
            task("done", sunday, PlanTaskStatus.CONCLUIDA),
            task("overdue", sunday.minusDays(1)),
            task("monday", sunday.plusDays(1), PlanTaskStatus.EM_ANDAMENTO),
        ))

        assertNull(pickFocusTask(state))
    }

    @Test
    fun todayInProgressHasPriorityOverOtherTasksToday() {
        val state = state(tasks = listOf(
            task("first", sunday),
            task("continuing", sunday, PlanTaskStatus.EM_ANDAMENTO),
            task("overdue", sunday.minusDays(1), PlanTaskStatus.EM_ANDAMENTO),
        ))

        assertEquals("continuing", pickFocusTask(state)?.entity?.id)
    }

    @Test
    fun todayUsesPlanOrderAndIgnoresReprogrammedTasks() {
        val state = state(tasks = listOf(
            task("old", sunday, PlanTaskStatus.REPROGRAMADA),
            task("first", sunday),
            task("second", sunday),
        ))

        assertEquals("first", pickFocusTask(state)?.entity?.id)
    }

    @Test
    fun specialAvailabilityCanHaveTodaysMissionOnUsuallyFreeSunday() {
        val state = state(tasks = listOf(task("special-sunday", sunday))).copy(
            dayOverrides = listOf(StudyDayOverrideEntity(plan.id, sunday.toEpochDay(), availableMinutes = 60)),
        )

        assertEquals("special-sunday", pickFocusTask(state)?.entity?.id)
    }

    @Test
    fun noActivePlanDoesNotShowStalePlanTask() {
        val state = state(tasks = listOf(task("stale", sunday))).copy(activePlan = null)

        assertNull(pickFocusTask(state))
    }

    @Test
    fun tasksFromAnotherPlanDoNotBecomeCurrentMission() {
        val other = task("other", sunday).let { it.copy(entity = it.entity.copy(planId = "inactive")) }

        assertNull(pickFocusTask(state(tasks = listOf(other))))
    }

    @Test
    fun dayOffShowsNoTaskTodayInsteadOfReadyForFuture() {
        val state = state(tasks = listOf(task("monday", sunday.plusDays(1))))

        assertEquals(CurrentStudyUiState.NoTaskToday, currentStudyState(state, pickFocusTask(state)))
    }

    @Test
    fun completedTodayShowsCelebrationWithFutureTasksRemaining() {
        val state = state(tasks = listOf(
            task("done", sunday, PlanTaskStatus.CONCLUIDA),
            task("monday", sunday.plusDays(1)),
        )).copy(todayActualMinutes = 30)

        assertEquals(CurrentStudyUiState.DayComplete(1, 30), currentStudyState(state, pickFocusTask(state)))
    }

    @Test
    fun activePlanWithoutTodaysTasksDoesNotFallBackToManualQueue() {
        assertNull(pickHomeQueueTopic(state(tasks = emptyList()), listOf(queueItem)))
    }

    @Test
    fun manualQueueRemainsAvailableWithoutActivePlan() {
        val state = state(tasks = emptyList()).copy(activePlan = null)

        assertEquals(queueItem, pickHomeQueueTopic(state, listOf(queueItem)))
    }

    private val queueItem = QueueWithTopic(
        item = StudyQueueEntity(id = 1, topicId = 1, position = 0),
        topic = TopicEntity(id = 1, subjectId = 1, title = "Fila manual"),
    )

    private fun state(tasks: List<PlannerTaskUi>) = ActivePlanUiState(
        activePlan = plan,
        tasks = tasks,
        today = sunday,
        availability = listOf(StudyAvailabilityEntity(plan.id, 7, 0, unavailable = true)),
    )

    private fun task(id: String, date: LocalDate, status: PlanTaskStatus = PlanTaskStatus.PLANEJADA) = PlannerTaskUi(
        entity = PlanTaskEntity(
            id = id,
            planId = plan.id,
            competitionId = 1,
            subjectNameSnapshot = "Português",
            topicNameSnapshot = "Concordância",
            scheduledEpochDay = date.toEpochDay(),
            type = PlanTaskType.THEORY,
            plannedMinutes = 30,
            priority = PlanPriority.MEDIUM,
            status = status,
            createdRevision = 0,
            updatedRevision = 0,
        ),
        actualMinutes = 0,
        questionsDone = 0,
        correctAnswers = 0,
    )
}
