package br.com.estudario.ui.screens.performance

import br.com.estudario.data.local.QuestionAttemptEntity
import br.com.estudario.data.local.QuestionSessionEntity
import br.com.estudario.data.local.QuestionWithOptions
import br.com.estudario.data.local.ReviewHistoryEntity
import br.com.estudario.data.local.StudySessionEntity
import br.com.estudario.data.local.FocusSessionEntity
import br.com.estudario.data.local.SubjectEntity
import br.com.estudario.data.local.TopicEntity
import br.com.estudario.data.local.planner.PlanTaskEntity
import br.com.estudario.data.local.planner.StudyPlanEntity
import br.com.estudario.data.local.planner.StudyTaskExecutionEntity
import br.com.estudario.domain.performance.PerformanceAttempt
import br.com.estudario.domain.performance.PerformanceExecution
import br.com.estudario.domain.performance.PerformancePlannedTask
import br.com.estudario.domain.performance.PerformanceReview
import br.com.estudario.domain.performance.PerformanceSession
import br.com.estudario.domain.performance.StudyPerformanceInput
import java.time.Instant
import java.time.LocalDate

object StudyPerformanceInputMapper {
    fun map(
        attempts: List<QuestionAttemptEntity>,
        questions: List<QuestionWithOptions>,
        subjects: List<SubjectEntity>,
        topics: List<TopicEntity>,
        reviewHistory: List<ReviewHistoryEntity>,
        studySessions: List<StudySessionEntity>,
        focusSessions: List<FocusSessionEntity> = emptyList(),
        questionSessions: List<QuestionSessionEntity>,
        plans: List<StudyPlanEntity>,
        tasks: List<PlanTaskEntity>,
        executions: List<StudyTaskExecutionEntity>,
    ): StudyPerformanceInput {
        val questionById = questions.associateBy { it.question.id }
        val topicById = topics.associateBy { it.id }
        val subjectById = subjects.associateBy { it.id }
        val attemptsByQuestion = attempts.map { attempt ->
            val topic = questionById[attempt.questionId]?.question?.topicId?.let(topicById::get)
            PerformanceAttempt(
                answeredAt = Instant.ofEpochMilli(attempt.answeredAt),
                correct = attempt.correct,
                subjectId = topic?.subjectId,
                subjectName = topic?.subjectId?.let(subjectById::get)?.name,
                topicId = topic?.id,
                topicName = topic?.title,
            )
        }
        val activePlanIds = plans.asSequence().filter { it.active && !it.archived }.mapTo(linkedSetOf()) { it.id }
        return StudyPerformanceInput(
            attempts = attemptsByQuestion,
            reviews = reviewHistory.map { PerformanceReview(Instant.ofEpochMilli(it.reviewedAt)) },
            studySessions = studySessions.map { PerformanceSession(Instant.ofEpochMilli(it.completedAt), it.durationSeconds) } +
                focusSessions.map { PerformanceSession(Instant.ofEpochMilli(it.completedAt), it.durationSeconds) },
            questionSessions = questionSessions.map { PerformanceSession(Instant.ofEpochMilli(it.completedAt), it.durationSeconds) },
            activePlanIds = activePlanIds,
            tasks = tasks.map {
                PerformancePlannedTask(
                    planId = it.planId,
                    scheduledDate = LocalDate.ofEpochDay(it.scheduledEpochDay),
                    plannedMinutes = it.plannedMinutes,
                    status = it.status,
                )
            },
            executions = executions.map {
                PerformanceExecution(it.planId, Instant.ofEpochMilli(it.completedAt), it.actualMinutes)
            },
        )
    }
}
