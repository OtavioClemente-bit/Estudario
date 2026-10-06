package br.com.estudario.data.planner

import androidx.room.withTransaction
import br.com.estudario.data.local.AppDatabase
import br.com.estudario.data.local.TopicStatus
import br.com.estudario.domain.planner.PlannerSnapshotBuilder
import br.com.estudario.domain.planner.StudyPlannerSnapshot
import br.com.estudario.time.today
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone

/**
 * Lê o estado do app e entrega ao motor o retrato do plano. A montagem em si é a mesma do app web
 * ([PlannerSnapshotBuilder], no módulo comum); aqui só os dados do Room viram as entradas dela.
 */
class StudyPlanSnapshotFactory(private val db: AppDatabase) {
    suspend fun create(planId: String, today: LocalDate = today()): StudyPlannerSnapshot = db.withTransaction {
        val planner = db.plannerDao()
        val plan = planner.plan(planId) ?: error("Plano não encontrado.")
        PlannerSnapshotBuilder.build(
            PlannerSnapshotBuilder.Input(
                plan = PlannerSnapshotBuilder.Plan(plan.id, plan.revision, plan.startEpochDay, plan.examEpochDay, plan.methodConfig()),
                planSubjects = planner.subjectsFor(planId).map {
                    PlannerSnapshotBuilder.PlanSubject(
                        subjectId = it.subjectId,
                        name = it.subjectNameSnapshot,
                        priority = it.priority,
                        paused = it.paused,
                        minimumMaintenanceMinutes = it.minimumMaintenanceMinutes,
                        weightOverride = it.weightOverride,
                        position = it.position,
                        personalDifficulty = it.personalDifficulty,
                        initialKnowledge = it.initialKnowledge,
                    )
                },
                topics = db.dao().topicsOnce().map {
                    PlannerSnapshotBuilder.Topic(it.id, it.subjectId, it.parentTopicId, it.title, it.position, it.status != TopicStatus.NAO_ESTUDADO, it.lastStudiedAt)
                },
                questionsByTopic = db.dao().questionsOnce().groupBy({ it.question.topicId }, { it.question.id }).mapValues { it.value.toSet() },
                attempts = db.dao().attemptsOnce().map { PlannerSnapshotBuilder.Attempt(it.questionId, it.correct) },
                pendingReviews = db.dao().reviewsOnce().filter { it.completedAt == null && it.ignoredAt == null }
                    .map { PlannerSnapshotBuilder.PendingReview(it.id, it.topicId, it.dueAt, it.questionTotal) },
                tasks = planner.tasksForOnce(planId).map { it.toDomain() },
                executions = planner.executionsForOnce(planId).map { it.toDomain() },
                availability = planner.availabilityFor(planId).map { PlannerSnapshotBuilder.Availability(it.dayOfWeek, it.availableMinutes, it.unavailable) },
                dayOverrides = planner.dayOverridesFor(planId).map { PlannerSnapshotBuilder.DayOverride(it.epochDay, it.availableMinutes, it.unavailable, it.locked) },
                today = today,
                zone = TimeZone.currentSystemDefault(),
            ),
        )
    }
}
