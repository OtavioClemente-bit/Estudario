package br.com.estudario.data.planner

import androidx.room.withTransaction
import br.com.estudario.data.local.AppDatabase
import br.com.estudario.data.local.TopicStatus
import br.com.estudario.domain.planner.*
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

/**
 * Lê o estado do app e entrega ao motor o retrato do plano. As tarefas em si são desenhadas pelo
 * [StudyPlanBlueprint], que aplica o método escolhido no assistente — é o que faz o plano sem IA
 * ter cara de cronograma pensado e não de lista de tópicos.
 */
class StudyPlanSnapshotFactory(private val db: AppDatabase) {
    suspend fun create(planId: String, today: LocalDate = LocalDate.now()): StudyPlannerSnapshot = db.withTransaction {
        val planner = db.plannerDao()
        val plan = planner.plan(planId) ?: error("Plano não encontrado.")
        val config = plan.methodConfig()
        val planSubjects = planner.subjectsFor(planId)
        val subjectIds = planSubjects.mapTo(hashSetOf()) { it.subjectId }
        val topics = db.dao().topicsOnce().filter { it.subjectId in subjectIds }
        val tasks = planner.tasksForOnce(planId).map { it.toDomain() }
        val executions = planner.executionsForOnce(planId).map { it.toDomain() }
        val questions = db.dao().questionsOnce().groupBy { it.question.topicId }
        val attempts = db.dao().attemptsOnce().groupBy { it.questionId }
        val reviews = db.dao().reviewsOnce().filter { it.completedAt == null && it.ignoredAt == null }
        val availabilityRows = planner.availabilityFor(planId)
        val zone = ZoneId.systemDefault()

        val occupied = tasks
            .filter { it.status in setOf(PlanTaskStatus.PLANEJADA, PlanTaskStatus.EM_ANDAMENTO, PlanTaskStatus.CONCLUIDA, PlanTaskStatus.PAUSADA) }
            .mapTo(hashSetOf()) { it.topicId to it.type }

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
        val performanceById = performance.associateBy { it.topicId }

        val blueprintSubjects = planSubjects.map { subject ->
            BlueprintSubject(
                subjectId = subject.subjectId,
                name = subject.subjectNameSnapshot,
                priority = subject.priority,
                weight = StudyMethod.weightOf(subject.priority, subject.weightOverride),
                position = subject.position,
                paused = subject.paused,
            )
        }
        val blueprintTopics = topics.map { topic ->
            val row = performanceById[topic.id]
            BlueprintTopic(
                topicId = topic.id,
                subjectId = topic.subjectId,
                title = topic.title,
                position = topic.position,
                depth = if (topic.parentTopicId == null) 0 else 1,
                studied = topic.status != TopicStatus.NAO_ESTUDADO,
                lastStudied = row?.lastStudiedDate,
                answered = row?.answered ?: 0,
                accuracyPercent = row?.takeIf { it.answered > 0 }?.let { it.correct * 100 / it.answered },
            )
        }
        val pendingReviews = reviews.mapNotNull { review ->
            val topic = topics.firstOrNull { it.id == review.topicId } ?: return@mapNotNull null
            ReviewDemand(
                id = review.id.toString(),
                subjectId = topic.subjectId,
                topicId = topic.id,
                dueDate = Instant.ofEpochMilli(review.dueAt).atZone(zone).toLocalDate(),
                minutes = 30,
                plannedQuestions = review.questionTotal.coerceAtLeast(0),
            )
        }

        val weeklyCapacity = availabilityRows.sumOf { if (it.unavailable) 0 else it.availableMinutes }
        val heaviestDay = availabilityRows
            .filterNot { it.unavailable }
            .maxByOrNull { it.availableMinutes }
            ?.let { DayOfWeek.of(it.dayOfWeek) }
            ?: DayOfWeek.SATURDAY
        val activeSubjectCount = blueprintSubjects.count { !it.paused }
        val policy = PlannerPolicy(
            horizonDays = 28,
            minimumBlockMinutes = (config.blockMinutes / 2).coerceAtLeast(10),
            preferredBlockMinutes = config.blockMinutes,
            // O rodízio de matérias já é decidido no blueprint, então o teto antigo por matéria sai
            // do caminho; o que segura o dia é o teto diário abaixo.
            maxFlexibleSharePercent = 100,
            dailySubjectSharePercent = if (config.interleaveSubjects && activeSubjectCount > 1) 50 else 100,
        )

        val blueprint = StudyPlanBlueprint.build(
            BlueprintInput(
                today = today,
                planStart = LocalDate.ofEpochDay(plan.startEpochDay),
                examDate = plan.examEpochDay?.let(LocalDate::ofEpochDay),
                config = config,
                subjects = blueprintSubjects,
                topics = blueprintTopics,
                pendingReviews = pendingReviews,
                weeklyCapacityMinutes = weeklyCapacity,
                horizonDays = policy.horizonDays,
                occupied = occupied,
                heaviestDay = heaviestDay,
            ),
        )

        val remainingTopics = blueprintTopics.count { !it.studied }
        StudyPlannerSnapshot(
            planId = plan.id,
            revision = plan.revision,
            today = today,
            availability = availabilityRows.associate { row ->
                val day = DayOfWeek.of(row.dayOfWeek)
                day to DailyCapacity(day, row.availableMinutes, row.unavailable)
            },
            dayOverrides = planner.dayOverridesFor(planId).associate { row ->
                val date = LocalDate.ofEpochDay(row.epochDay)
                date to DayCapacityOverride(date, row.availableMinutes, row.unavailable, row.locked)
            },
            subjects = planSubjects.map { SubjectDemand(it.subjectId, it.subjectNameSnapshot, it.priority, it.minimumMaintenanceMinutes, it.position, it.paused) },
            demands = blueprint.demands,
            reviews = pendingReviews,
            tasks = tasks,
            executions = executions,
            topicPerformance = performance,
            completedDependencyIds = tasks.filter { it.status == PlanTaskStatus.CONCLUIDA }.mapTo(hashSetOf()) { it.id },
            // Previsão de conclusão do edital: teoria + consolidação de tudo que falta ver.
            remainingPhaseMinutes = remainingTopics * (config.blockMinutes + config.questionsPerTopic * config.minutesPerQuestion),
            recurringReservedMinutes = planSubjects.sumOf { it.minimumMaintenanceMinutes },
            policy = policy,
            notes = blueprint.notes,
        )
    }
}
