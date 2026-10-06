package br.com.estudario.domain.planner

import br.com.estudario.time.daysBetween
import br.com.estudario.time.epochMillisToDate
import br.com.estudario.time.isAfter
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone

/**
 * Monta o retrato que o motor de planejamento recebe, a partir de dados simples. O app Android
 * (Room) e o app web (foto JSON) convertem seus dados para estas entradas e chamam o mesmo código,
 * então os dois planejam igual. As tarefas são desenhadas pelo [StudyPlanBlueprint].
 */
object PlannerSnapshotBuilder {
    data class Plan(
        val id: String,
        val revision: Long,
        val startEpochDay: Long,
        val examEpochDay: Long?,
        val config: StudyMethodConfig,
    )

    data class PlanSubject(
        val subjectId: Long,
        val name: String,
        val priority: PlanPriority,
        val paused: Boolean,
        val minimumMaintenanceMinutes: Int,
        val weightOverride: Int?,
        val position: Int,
        val personalDifficulty: PersonalDifficulty,
        val initialKnowledge: InitialKnowledge,
    ) {
        val dimensions: StudyDimensions
            get() = StudyDimensions(examPriority = priority.toExamPriority(), personalDifficulty = personalDifficulty, initialKnowledge = initialKnowledge)
    }

    data class Topic(
        val id: Long,
        val subjectId: Long,
        val parentTopicId: Long?,
        val title: String,
        val position: Int,
        val studied: Boolean,
        val lastStudiedAt: Long?,
    )

    data class Attempt(val questionId: Long, val correct: Boolean)

    /** Revisão ainda pendente (nem feita nem ignorada). */
    data class PendingReview(val id: Long, val topicId: Long, val dueAt: Long, val questionTotal: Int)

    data class Availability(val isoDayOfWeek: Int, val minutes: Int, val unavailable: Boolean)

    data class DayOverride(val epochDay: Long, val minutes: Int?, val unavailable: Boolean, val locked: Boolean)

    data class Input(
        val plan: Plan,
        val planSubjects: List<PlanSubject>,
        /** Todos os tópicos; os das matérias fora do plano são ignorados. */
        val topics: List<Topic>,
        /** Ids das questões de cada tópico. */
        val questionsByTopic: Map<Long, Set<Long>>,
        val attempts: List<Attempt>,
        val pendingReviews: List<PendingReview>,
        val tasks: List<PlannerTask>,
        val executions: List<TaskExecution>,
        val availability: List<Availability>,
        val dayOverrides: List<DayOverride>,
        val today: LocalDate,
        val zone: TimeZone,
    )

    fun build(input: Input): StudyPlannerSnapshot = with(input) {
        val config = plan.config
        val subjectIds = planSubjects.mapTo(hashSetOf()) { it.subjectId }
        val topics = topics.filter { it.subjectId in subjectIds }
        val attemptsByQuestion = attempts.groupBy { it.questionId }

        val occupied = tasks
            .filter { it.status in setOf(PlanTaskStatus.PLANEJADA, PlanTaskStatus.EM_ANDAMENTO, PlanTaskStatus.CONCLUIDA, PlanTaskStatus.PAUSADA) }
            .mapTo(hashSetOf()) { it.topicId to it.type }

        val performance = topics.map { topic ->
            val topicAttempts = questionsByTopic[topic.id].orEmpty().flatMap { attemptsByQuestion[it].orEmpty() }
            TopicPerformance(
                topicId = topic.id,
                answered = topicAttempts.size,
                correct = topicAttempts.count { it.correct },
                lastStudiedDate = topic.lastStudiedAt?.let { epochMillisToDate(it, zone) },
            )
        }
        val performanceById = performance.associateBy { it.topicId }

        // Camada de necessidade: os três eixos e a evidência de cada matéria/tópico viram o
        // NeedScore que decide frequência, volume de questões e ordem do reforço.
        val planStart = LocalDate.fromEpochDays(plan.startEpochDay)
        val examDate = plan.examEpochDay?.let { LocalDate.fromEpochDays(it) }
        val daysUntilExam = examDate?.let { daysBetween(today, it).toInt() }?.takeIf { it >= 0 }
        val currentPhase = StudyMethod.phaseAt(StudyMethod.phases(planStart, examDate, config.profile), today)
        val weights = PlannerWeights.forPhase(currentPhase.kind)

        val missedBySubject = tasks
            .filter { it.status == PlanTaskStatus.NAO_REALIZADA && it.subjectId != null }
            .groupingBy { it.subjectId!! }
            .eachCount()
        val subjectByTopic = topics.associate { it.id to it.subjectId }
        val overdueReviewsBySubject = pendingReviews
            .filter { !epochMillisToDate(it.dueAt, zone).isAfter(today) }
            .mapNotNull { subjectByTopic[it.topicId] }
            .groupingBy { it }
            .eachCount()

        val topicEvidence = topics.associate { topic ->
            val row = performanceById[topic.id]
            topic.id to StudyEvidence(
                answered = row?.answered ?: 0,
                correct = row?.correct ?: 0,
                coveredTopics = if (topic.studied) 1 else 0,
                totalTopics = 1,
                daysSinceContact = row?.lastStudiedDate?.let { daysBetween(it, today).toInt().coerceAtLeast(0) },
            )
        }
        val evidenceBySubject = planSubjects.associate { subject ->
            val rows = topics.filter { it.subjectId == subject.subjectId }.map { topicEvidence.getValue(it.id) }
            subject.subjectId to StudyEvidence(
                answered = rows.sumOf { it.answered },
                correct = rows.sumOf { it.correct },
                coveredTopics = rows.sumOf { it.coveredTopics },
                totalTopics = rows.size,
                overdueReviews = overdueReviewsBySubject[subject.subjectId] ?: 0,
                missedTasks = missedBySubject[subject.subjectId] ?: 0,
                daysSinceContact = rows.mapNotNull { it.daysSinceContact }.minOrNull(),
            )
        }
        val subjectDimensions = planSubjects.associate { it.subjectId to it.dimensions }
        val subjectNeeds = planSubjects.associate { subject ->
            subject.subjectId to StudyNeedCalculator.evaluate(
                subjectId = subject.subjectId,
                dimensions = subjectDimensions.getValue(subject.subjectId),
                evidence = evidenceBySubject.getValue(subject.subjectId),
                weights = weights,
                daysUntilExam = daysUntilExam,
            )
        }
        val topicNeeds = topics.associate { topic ->
            // O tópico herda os eixos da matéria e traz a própria evidência.
            topic.id to StudyNeedCalculator.evaluate(
                subjectId = topic.subjectId,
                topicId = topic.id,
                dimensions = subjectDimensions[topic.subjectId] ?: StudyDimensions.DEFAULT,
                evidence = topicEvidence.getValue(topic.id),
                weights = weights,
                daysUntilExam = daysUntilExam,
            )
        }

        val blueprintSubjects = planSubjects.map { subject ->
            BlueprintSubject(
                subjectId = subject.subjectId,
                name = subject.name,
                priority = subject.priority,
                // O peso do rodízio vem só da prova; a dificuldade age pelo NeedScore.
                weight = subject.weightOverride?.coerceIn(1, 5) ?: subject.priority.toExamPriority().rotationWeight(),
                position = subject.position,
                paused = subject.paused,
                dimensions = subjectDimensions.getValue(subject.subjectId),
                need = subjectNeeds[subject.subjectId],
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
                studied = topic.studied,
                lastStudied = row?.lastStudiedDate,
                answered = row?.answered ?: 0,
                accuracyPercent = row?.takeIf { it.answered > 0 }?.let { it.correct * 100 / it.answered },
                evidence = topicEvidence.getValue(topic.id),
                need = topicNeeds[topic.id],
            )
        }
        val reviewDemands = pendingReviews.mapNotNull { review ->
            val topic = topics.firstOrNull { it.id == review.topicId } ?: return@mapNotNull null
            ReviewDemand(
                id = review.id.toString(),
                subjectId = topic.subjectId,
                topicId = topic.id,
                dueDate = epochMillisToDate(review.dueAt, zone),
                minutes = 30,
                plannedQuestions = review.questionTotal.coerceAtLeast(0),
            )
        }

        val weeklyCapacity = availability.sumOf { if (it.unavailable) 0 else it.minutes }
        val heaviestDay = availability.filterNot { it.unavailable }.maxByOrNull { it.minutes }?.let { DayOfWeek(it.isoDayOfWeek) } ?: DayOfWeek.SATURDAY
        val activeSubjectCount = blueprintSubjects.count { !it.paused }
        val policy = PlannerPolicy(
            horizonDays = 28,
            minimumBlockMinutes = (config.blockMinutes / 2).coerceAtLeast(10),
            preferredBlockMinutes = config.blockMinutes,
            // O rodízio já é decidido no blueprint; o que segura o dia é o teto diário escolhido.
            maxFlexibleSharePercent = 100,
            dailySubjectSharePercent = if (activeSubjectCount > 1) config.dailySubjectSharePercent else 100,
        )

        val blueprint = StudyPlanBlueprint.build(
            BlueprintInput(
                today = today,
                planStart = planStart,
                examDate = examDate,
                config = config,
                subjects = blueprintSubjects,
                topics = blueprintTopics,
                pendingReviews = reviewDemands,
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
            availability = availability.associate { row ->
                val day = DayOfWeek(row.isoDayOfWeek)
                day to DailyCapacity(day, row.minutes, row.unavailable)
            },
            dayOverrides = dayOverrides.associate { row ->
                val date = LocalDate.fromEpochDays(row.epochDay)
                date to DayCapacityOverride(date, row.minutes, row.unavailable, row.locked)
            },
            subjects = planSubjects.map { SubjectDemand(it.subjectId, it.name, it.priority, it.minimumMaintenanceMinutes, it.position, it.paused) },
            demands = blueprint.demands,
            reviews = reviewDemands,
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
