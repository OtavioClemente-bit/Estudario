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
 * [StudyPlanBlueprint], que aplica o método escolhido no assistente, é o que faz o plano sem IA
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

        // --- Camada de necessidade -------------------------------------------------------------
        // Os três eixos e a evidência de cada matéria/tópico entram aqui; daqui sai o NeedScore que
        // decide frequência, volume de questões e ordem do reforço. Tudo determinístico.
        val planStart = LocalDate.ofEpochDay(plan.startEpochDay)
        val examDate = plan.examEpochDay?.let(LocalDate::ofEpochDay)
        val daysUntilExam = examDate
            ?.let { java.time.temporal.ChronoUnit.DAYS.between(today, it).toInt() }
            ?.takeIf { it >= 0 }
        val currentPhase = StudyMethod.phaseAt(
            StudyMethod.phases(planStart, examDate, config.profile),
            today,
        )
        val weights = PlannerWeights.forPhase(currentPhase.kind)

        val missedBySubject = tasks
            .filter { it.status == PlanTaskStatus.NAO_REALIZADA && it.subjectId != null }
            .groupingBy { it.subjectId!! }
            .eachCount()
        val overdueReviewsBySubject = pendingReviewsBySubject(reviews, topics, today, zone)

        fun evidenceOf(topic: br.com.estudario.data.local.TopicEntity): StudyEvidence {
            val row = performanceById[topic.id]
            val covered = topic.status != TopicStatus.NAO_ESTUDADO
            return StudyEvidence(
                answered = row?.answered ?: 0,
                correct = row?.correct ?: 0,
                coveredTopics = if (covered) 1 else 0,
                totalTopics = 1,
                daysSinceContact = row?.lastStudiedDate
                    ?.let { java.time.temporal.ChronoUnit.DAYS.between(it, today).toInt().coerceAtLeast(0) },
            )
        }

        val topicEvidence = topics.associate { it.id to evidenceOf(it) }
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

        val subjectDimensions = planSubjects.associate { it.subjectId to it.dimensions() }
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
            // O tópico herda os eixos da matéria e traz a própria evidência, é assim que ninguém
            // precisa classificar 120 tópicos no primeiro setup e mesmo assim o plano diferencia
            // tópico forte de tópico fraco dentro da mesma matéria.
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
                name = subject.subjectNameSnapshot,
                priority = subject.priority,
                // O peso do rodízio vem só da prova; a dificuldade age pelo NeedScore.
                weight = subject.weightOverride?.coerceIn(1, 5)
                    ?: subject.priority.toExamPriority().rotationWeight(),
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
                studied = topic.status != TopicStatus.NAO_ESTUDADO,
                lastStudied = row?.lastStudiedDate,
                answered = row?.answered ?: 0,
                accuracyPercent = row?.takeIf { it.answered > 0 }?.let { it.correct * 100 / it.answered },
                evidence = topicEvidence.getValue(topic.id),
                need = topicNeeds[topic.id],
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
            // do caminho; o que segura o dia é o teto diário abaixo, escolhido no assistente.
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

    /**
     * Revisões já vencidas, por matéria. Alimenta o fator de revisão do NeedScore: revisão perdida
     * é conteúdo perdido, e a matéria que a acumula precisa voltar antes.
     */
    private fun pendingReviewsBySubject(
        reviews: List<br.com.estudario.data.local.ReviewScheduleEntity>,
        topics: List<br.com.estudario.data.local.TopicEntity>,
        today: LocalDate,
        zone: ZoneId,
    ): Map<Long, Int> {
        val subjectByTopic = topics.associate { it.id to it.subjectId }
        return reviews
            .filter { review ->
                val due = Instant.ofEpochMilli(review.dueAt).atZone(zone).toLocalDate()
                !due.isAfter(today)
            }
            .mapNotNull { subjectByTopic[it.topicId] }
            .groupingBy { it }
            .eachCount()
    }
}
