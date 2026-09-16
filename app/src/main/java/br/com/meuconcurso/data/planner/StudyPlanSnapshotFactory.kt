package br.com.meuconcurso.data.planner

import androidx.room.withTransaction
import br.com.meuconcurso.data.local.AppDatabase
import br.com.meuconcurso.data.local.TopicStatus
import br.com.meuconcurso.domain.planner.*
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

class StudyPlanSnapshotFactory(private val db: AppDatabase) {
    suspend fun create(planId: String, today: LocalDate = LocalDate.now()): StudyPlannerSnapshot = db.withTransaction {
        val planner = db.plannerDao()
        val plan = planner.plan(planId) ?: error("Plano não encontrado.")
        val planSubjects = planner.subjectsFor(planId)
        val subjectIds = planSubjects.mapTo(hashSetOf()) { it.subjectId }
        val topics = db.dao().topicsOnce().filter { it.subjectId in subjectIds }
        val tasks = planner.tasksForOnce(planId).map { it.toDomain() }
        val executions = planner.executionsForOnce(planId).map { it.toDomain() }
        val questions = db.dao().questionsOnce().groupBy { it.question.topicId }
        val attempts = db.dao().attemptsOnce().groupBy { it.questionId }
        val reviews = db.dao().reviewsOnce().filter { it.completedAt == null && it.ignoredAt == null }
        val zone = ZoneId.systemDefault()
        val occupiedTopicTypes = tasks.filter { it.status in setOf(PlanTaskStatus.PLANEJADA, PlanTaskStatus.EM_ANDAMENTO, PlanTaskStatus.CONCLUIDA, PlanTaskStatus.PAUSADA) }
            .mapTo(hashSetOf()) { it.topicId to it.type }
        val demands = topics.filter { it.status == TopicStatus.NAO_ESTUDADO && (it.id to PlanTaskType.THEORY) !in occupiedTopicTypes }.map { topic ->
            val subject = planSubjects.first { it.subjectId == topic.subjectId }
            TaskDemand(
                id = "topic:${topic.id}",
                subjectId = topic.subjectId,
                topicId = topic.id,
                type = PlanTaskType.THEORY,
                minutes = 60,
                priority = subject.priority,
                subjectPosition = subject.position,
                topicPosition = topic.position,
            )
        } + reviews.mapNotNull { review ->
            val topic = topics.firstOrNull { it.id == review.topicId } ?: return@mapNotNull null
            if ((topic.id to PlanTaskType.REVIEW) in occupiedTopicTypes) return@mapNotNull null
            val subject = planSubjects.firstOrNull { it.subjectId == topic.subjectId } ?: return@mapNotNull null
            TaskDemand(
                id = "review:${review.id}",
                subjectId = topic.subjectId,
                topicId = topic.id,
                type = PlanTaskType.REVIEW,
                minutes = 30,
                questions = review.questionTotal.coerceAtLeast(0),
                priority = subject.priority,
                deadline = Instant.ofEpochMilli(review.dueAt).atZone(zone).toLocalDate(),
                subjectPosition = subject.position,
                topicPosition = topic.position,
            )
        }
        val performance = topics.map { topic ->
            val topicQuestions = questions[topic.id].orEmpty()
            val ids = topicQuestions.mapTo(hashSetOf()) { it.question.id }
            val topicAttempts = ids.flatMap { attempts[it].orEmpty() }
            TopicPerformance(
                topicId = topic.id,
                answered = topicAttempts.size,
                correct = topicAttempts.count { it.correct },
                lastStudiedDate = topic.lastStudiedAt?.let { Instant.ofEpochMilli(it).atZone(zone).toLocalDate() },
            )
        }
        StudyPlannerSnapshot(
            planId = plan.id,
            revision = plan.revision,
            today = today,
            availability = planner.availabilityFor(planId).associate { row ->
                val day = DayOfWeek.of(row.dayOfWeek)
                day to DailyCapacity(day, row.availableMinutes, row.unavailable)
            },
            dayOverrides = planner.dayOverridesFor(planId).associate { row ->
                val date = LocalDate.ofEpochDay(row.epochDay)
                date to DayCapacityOverride(date, row.availableMinutes, row.unavailable, row.locked)
            },
            subjects = planSubjects.map { SubjectDemand(it.subjectId, it.subjectNameSnapshot, it.priority, it.minimumMaintenanceMinutes, it.position, it.paused) },
            demands = demands,
            tasks = tasks,
            executions = executions,
            topicPerformance = performance,
            completedDependencyIds = tasks.filter { it.status == PlanTaskStatus.CONCLUIDA }.mapTo(hashSetOf()) { it.id },
            remainingPhaseMinutes = demands.sumOf { it.minutes },
            recurringReservedMinutes = planSubjects.sumOf { it.minimumMaintenanceMinutes },
        )
    }
}
