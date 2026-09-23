package br.com.estudario.ui.screens.performance

import br.com.estudario.data.local.QuestionAttemptEntity
import br.com.estudario.data.local.QuestionEntity
import br.com.estudario.data.local.QuestionSessionEntity
import br.com.estudario.data.local.QuestionSessionType
import br.com.estudario.data.local.QuestionWithOptions
import br.com.estudario.data.local.ReviewHistoryEntity
import br.com.estudario.data.local.StudySessionEntity
import br.com.estudario.data.local.FocusSessionEntity
import br.com.estudario.data.local.FocusSessionOrigin
import br.com.estudario.data.local.SubjectEntity
import br.com.estudario.data.local.TopicEntity
import br.com.estudario.data.local.planner.PlanTaskEntity
import br.com.estudario.data.local.planner.StudyPlanEntity
import br.com.estudario.data.local.planner.StudyTaskExecutionEntity
import br.com.estudario.domain.planner.PlanPriority
import br.com.estudario.domain.planner.PlanTaskStatus
import br.com.estudario.domain.planner.PlanTaskType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.Instant
import java.time.LocalDate

class StudyPerformanceInputMapperTest {
    @Test fun `maps real question associations and only active non archived plans`() {
        val now = 1_789_000_000_000L
        val subject = SubjectEntity(id = 7, competitionId = 1, name = "Direito")
        val topic = TopicEntity(id = 8, subjectId = 7, title = "Atos administrativos")
        val question = QuestionEntity(id = 9, topicId = 8, statement = "Pergunta", explanation = "Resposta")
        val active = StudyPlanEntity("active", 1, "Atual", "", 0, active = true)
        val inactive = StudyPlanEntity("inactive", 1, "Inativo", "", 0, active = false)
        val archived = StudyPlanEntity("archived", 1, "Arquivado", "", 0, active = true, archived = true)

        val input = StudyPerformanceInputMapper.map(
            attempts = listOf(QuestionAttemptEntity(id = 1, questionId = 9, selectedKey = "A", correct = false, answeredAt = now)),
            questions = listOf(QuestionWithOptions(question, emptyList())),
            subjects = listOf(subject),
            topics = listOf(topic),
            reviewHistory = listOf(ReviewHistoryEntity(topicId = 8, reviewedAt = now)),
            studySessions = listOf(StudySessionEntity(topicId = 8, startedAt = now - 60_000, completedAt = now, durationSeconds = 60)),
            focusSessions = listOf(FocusSessionEntity("focus-1", "Direito", now - 120_000, now, 120, "7", FocusSessionOrigin.PLANO, topicId = 8)),
            questionSessions = listOf(QuestionSessionEntity("q", QuestionSessionType.TOPIC, now - 60_000, now, 30, 1, 0)),
            plans = listOf(active, inactive, archived),
            tasks = listOf(task("active"), task("inactive"), task("archived")),
            executions = listOf(execution("active", now), execution("inactive", now), execution("archived", now)),
        )

        assertEquals(1, input.attempts.size)
        assertEquals(7L, input.attempts.single().subjectId)
        assertEquals("Direito", input.attempts.single().subjectName)
        assertEquals(8L, input.attempts.single().topicId)
        assertEquals("Atos administrativos", input.attempts.single().topicName)
        assertEquals(setOf("active"), input.activePlanIds)
        assertTrue(input.tasks.any { it.planId == "archived" }) // evaluator applies activePlanIds consistently
        assertEquals(Instant.ofEpochMilli(now), input.reviews.single().reviewedAt)
        assertEquals(setOf(60L, 120L), input.studySessions.map { it.durationSeconds }.toSet())
    }

    @Test fun `an unanswered question with no loaded association does not invent a subject`() {
        val input = StudyPerformanceInputMapper.map(
            attempts = listOf(QuestionAttemptEntity(questionId = 999, selectedKey = "A", correct = true)),
            questions = emptyList(), subjects = emptyList(), topics = emptyList(), reviewHistory = emptyList(),
            studySessions = emptyList(), focusSessions = emptyList(), questionSessions = emptyList(), plans = emptyList(), tasks = emptyList(), executions = emptyList(),
        )

        assertEquals(null, input.attempts.single().subjectId)
        assertEquals(null, input.attempts.single().topicId)
    }

    private fun task(planId: String) = PlanTaskEntity(
        id = "task-$planId",
        planId = planId,
        competitionId = 1,
        subjectNameSnapshot = "Direito",
        scheduledEpochDay = LocalDate.of(2026, 9, 22).toEpochDay(),
        type = PlanTaskType.THEORY,
        plannedMinutes = 30,
        priority = PlanPriority.MEDIUM,
        status = PlanTaskStatus.PLANEJADA,
        createdRevision = 0,
        updatedRevision = 0,
    )

    private fun execution(planId: String, now: Long) = StudyTaskExecutionEntity(
        id = "exec-$planId", planId = planId, competitionId = 1, startedAt = now - 60_000,
        completedAt = now, actualMinutes = 20,
    )
}
