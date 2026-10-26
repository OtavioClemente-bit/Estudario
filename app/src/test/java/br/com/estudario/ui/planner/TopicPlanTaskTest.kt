package br.com.estudario.ui.planner

import br.com.estudario.data.local.planner.PlanTaskEntity
import br.com.estudario.data.local.planner.StudyPlanEntity
import br.com.estudario.domain.planner.PlanPriority
import br.com.estudario.domain.planner.PlanTaskStatus
import br.com.estudario.domain.planner.PlanTaskType
import java.time.LocalDate
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class TopicPlanTaskTest {
    private val today = LocalDate.of(2026, 10, 26)
    private val plan = StudyPlanEntity("plan", 1, "Plano", "Concurso", today.toEpochDay(), active = true)

    private fun task(id: String, type: PlanTaskType, status: PlanTaskStatus = PlanTaskStatus.PLANEJADA) = PlannerTaskUi(
        PlanTaskEntity(
            id = id, planId = "plan", competitionId = 1, subjectId = 1, topicId = 10,
            subjectNameSnapshot = "Informática", topicNameSnapshot = "Redes", scheduledEpochDay = today.toEpochDay(),
            type = type, plannedMinutes = if (type == PlanTaskType.THEORY) 50 else 22,
            priority = PlanPriority.MEDIUM, status = status, createdRevision = 0, updatedRevision = 0,
        ),
        actualMinutes = 0, questionsDone = 0, correctAnswers = 0,
    )

    private fun state(vararg tasks: PlannerTaskUi) = ActivePlanUiState(activePlan = plan, tasks = tasks.toList(), today = today)

    @Test
    fun completingTheorySelectsTheoryEvenWhenQuestionsComeFirst() {
        val current = state(task("questions", PlanTaskType.QUESTIONS), task("theory", PlanTaskType.THEORY))

        assertEquals("theory", matchingPendingTopicTask(current, 10)?.entity?.id)
    }

    @Test
    fun questionTaskCannotBePreferredForTopicCompletion() {
        val current = state(task("questions", PlanTaskType.QUESTIONS), task("theory", PlanTaskType.THEORY))

        assertEquals("theory", matchingPendingTopicTask(current, 10, "questions")?.entity?.id)
    }

    @Test
    fun completedTheoryDoesNotFallBackToPendingQuestions() {
        val current = state(task("theory", PlanTaskType.THEORY, PlanTaskStatus.CONCLUIDA), task("questions", PlanTaskType.QUESTIONS))

        assertNull(matchingPendingTopicTask(current, 10, "theory"))
    }

    @Test
    fun reviewCompletionOnlySelectsAnActualReview() {
        val current = state(task("questions", PlanTaskType.QUESTIONS), task("review", PlanTaskType.REVIEW))

        assertEquals("review", matchingPendingTopicTask(current, 10, type = PlanTaskType.REVIEW)?.entity?.id)
        assertNull(matchingPendingTopicTask(state(task("questions", PlanTaskType.QUESTIONS)), 10, type = PlanTaskType.REVIEW))
    }

    @Test
    fun topicCompletionDoesNotSelectOtherPlansOrInactiveTasks() {
        val otherPlan = task("other", PlanTaskType.THEORY).let { it.copy(entity = it.entity.copy(planId = "other-plan")) }
        val current = state(otherPlan, task("reprogrammed", PlanTaskType.THEORY, PlanTaskStatus.REPROGRAMADA), task("questions", PlanTaskType.QUESTIONS))

        assertNull(matchingPendingTopicTask(current, 10))
    }
}
