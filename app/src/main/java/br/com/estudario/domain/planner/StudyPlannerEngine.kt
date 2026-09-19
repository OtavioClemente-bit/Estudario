package br.com.estudario.domain.planner

import java.security.MessageDigest
import java.time.LocalDate

class StudyPlannerEngine(
    private val scoring: PlannerScoringPolicy = PlannerScoringPolicy(),
) {
    fun plan(snapshot: StudyPlannerSnapshot, reason: ReplanReason): PlanningProposal {
        val dates = (0 until snapshot.policy.horizonDays).map { snapshot.today.plusDays(it.toLong()) }
        val rawCapacity = dates.associateWith { date -> capacityFor(snapshot, date) }
        val fixedTasks = snapshot.tasks.filter { task ->
            task.status == PlanTaskStatus.CONCLUIDA ||
                task.locked ||
                snapshot.dayOverrides[task.date]?.locked == true ||
                task.subjectId == null
        }
        val lockedUsage = fixedTasks
            .filter { it.date in rawCapacity.keys && it.status !in terminalInactiveStatuses }
            .groupBy { it.date }
            .mapValues { (_, tasks) -> tasks.sumOf { it.plannedMinutes } }
        val allocatableByDate = rawCapacity.mapValues { (date, minutes) ->
            if (snapshot.dayOverrides[date]?.locked == true) 0
            else (minutes - lockedUsage.getOrDefault(date, 0)).coerceAtLeast(0)
        }
        val freeByDate = allocatableByDate.toMutableMap()
        val executionsByTask = snapshot.executions.filter { it.taskId != null }.groupBy { it.taskId }
        val replanCandidates = if (reason == ReplanReason.INITIAL) emptyList() else snapshot.tasks.filter { task ->
            task !in fixedTasks && task.status in setOf(PlanTaskStatus.PLANEJADA, PlanTaskStatus.EM_ANDAMENTO)
        }
        val derivedDemands = replanCandidates.mapNotNull { task ->
            val executedMinutes = executionsByTask[task.id].orEmpty().sumOf { it.minutes }
            val executedQuestions = executionsByTask[task.id].orEmpty().sumOf { it.questions }
            val remainingMinutes = (task.plannedMinutes - executedMinutes).coerceAtLeast(0)
            remainingMinutes.takeIf { it > 0 }?.let {
                TaskDemand(
                    id = "replan:${task.id}",
                    subjectId = task.subjectId ?: return@mapNotNull null,
                    topicId = task.topicId,
                    type = task.type,
                    minutes = remainingMinutes,
                    questions = (task.plannedQuestions - executedQuestions).coerceAtLeast(0),
                    priority = task.priority,
                    deadline = task.deadline,
                    subjectPosition = task.subjectPosition,
                    topicPosition = task.topicPosition,
                    replannedFromTaskId = task.id,
                    // O que ficou para trás volta na frente: colocar em dia vem antes de avançar.
                    order = -1,
                )
            }
        }
        val allDemands = snapshot.demands + derivedDemands
        val eligible = allDemands
            .filterNot { demand -> snapshot.subjects.firstOrNull { it.subjectId == demand.subjectId }?.paused == true }
            .filter { it.dependsOnTaskIds.all(snapshot.completedDependencyIds::contains) }
            .sortedWith(demandComparator(snapshot))
        val blocked = allDemands.filterNot { it in eligible }
        val remaining = eligible.associate { it.id to it.minutes }.toMutableMap()
        val remainingQuestions = eligible.associate { it.id to it.questions }.toMutableMap()
        val generated = mutableListOf<PlannerTask>()
        val scheduledBySubject = mutableMapOf<Long, Int>()
        var sequence = 0

        val usedBySubjectPerDate = mutableMapOf<Pair<LocalDate, Long>, Int>()
        val dailyShare = snapshot.policy.dailySubjectSharePercent
        // Teto por dia e por matéria: evita quatro horas seguidas da mesma coisa. Quando nenhum dia
        // aceita a matéria, a segunda passada roda sem o teto para não desperdiçar capacidade.
        fun dateFor(demand: TaskDemand, respectShare: Boolean): LocalDate? {
            val from = demand.anchorDate
            val window = if (from == null) dates else dates.filterNot { it.isBefore(from) }.ifEmpty { dates }
            return window.firstOrNull { date ->
                if (freeByDate.getValue(date) <= 0) return@firstOrNull false
                if (!respectShare || dailyShare >= 100) return@firstOrNull true
                val capacity = rawCapacity.getValue(date)
                val cap = (capacity * dailyShare / 100).coerceAtLeast(snapshot.policy.preferredBlockMinutes)
                usedBySubjectPerDate.getOrDefault(date to demand.subjectId, 0) < cap
            }
        }

        fun allocate(demand: TaskDemand, maximum: Int, respectShare: Boolean = true): Int {
            var left = minOf(maximum, remaining.getValue(demand.id))
            var allocated = 0
            while (left > 0) {
                val date = dateFor(demand, respectShare) ?: break
                val free = freeByDate.getValue(date)
                val chunk = minOf(left, free, snapshot.policy.preferredBlockMinutes.coerceAtLeast(snapshot.policy.minimumBlockMinutes))
                if (chunk < snapshot.policy.minimumBlockMinutes && left >= snapshot.policy.minimumBlockMinutes) {
                    freeByDate[date] = 0
                    continue
                }
                val questionLeft = remainingQuestions.getValue(demand.id)
                val minuteLeftBefore = remaining.getValue(demand.id)
                val chunkQuestions = if (chunk == minuteLeftBefore) questionLeft
                else if (minuteLeftBefore == 0) 0
                else questionLeft * chunk / minuteLeftBefore
                generated += PlannerTask(
                    id = stableId(snapshot.planId, snapshot.revision.toString(), demand.id, date.toString(), sequence++.toString()),
                    planId = snapshot.planId,
                    subjectId = demand.subjectId,
                    topicId = demand.topicId,
                    date = date,
                    type = demand.type,
                    plannedMinutes = chunk,
                    plannedQuestions = chunkQuestions,
                    priority = demand.priority,
                    status = PlanTaskStatus.PLANEJADA,
                    locked = false,
                    deadline = demand.deadline,
                    subjectPosition = demand.subjectPosition,
                    topicPosition = demand.topicPosition,
                    replannedFromTaskId = demand.replannedFromTaskId,
                )
                freeByDate[date] = free - chunk
                usedBySubjectPerDate[date to demand.subjectId] = usedBySubjectPerDate.getOrDefault(date to demand.subjectId, 0) + chunk
                remaining[demand.id] = minuteLeftBefore - chunk
                remainingQuestions[demand.id] = questionLeft - chunkQuestions
                left -= chunk
                allocated += chunk
                scheduledBySubject[demand.subjectId] = scheduledBySubject.getOrDefault(demand.subjectId, 0) + chunk
            }
            return allocated
        }

        snapshot.subjects
            .filterNot { it.paused }
            .sortedWith(compareBy<SubjectDemand>({ it.position }, { it.subjectId }))
            .forEach { subject ->
                var maintenanceLeft = subject.minimumMaintenanceMinutes
                eligible.filter { it.subjectId == subject.subjectId }.forEach { demand ->
                    if (maintenanceLeft > 0) maintenanceLeft -= allocate(demand, maintenanceLeft)
                }
            }

        val totalAllocatable = allocatableByDate.values.sum()
        val maintenanceAllocated = generated.sumOf { it.plannedMinutes }
        val flexiblePool = (totalAllocatable - maintenanceAllocated).coerceAtLeast(0)
        val eligibleSubjects = eligible.filter { remaining.getValue(it.id) > 0 }.map { it.subjectId }.distinct()
        val capEnabled = eligibleSubjects.size > 1
        val flexibleCap = flexiblePool * snapshot.policy.maxFlexibleSharePercent / 100
        val maintenanceBySubject = generated.groupBy { it.subjectId }
            .mapValues { (_, tasks) -> tasks.sumOf { it.plannedMinutes } }
        val flexibleBySubject = mutableMapOf<Long, Int>()

        fun flexiblePass(respectShare: Boolean) {
            var madeProgress: Boolean
            do {
                madeProgress = false
                eligible.forEach { demand ->
                    val demandRemaining = remaining.getValue(demand.id)
                    if (demandRemaining == 0) return@forEach
                    val capLeft = if (capEnabled) {
                        (flexibleCap - flexibleBySubject.getOrDefault(demand.subjectId, 0)).coerceAtLeast(0)
                    } else demandRemaining
                    if (capLeft == 0) return@forEach
                    val before = scheduledBySubject.getOrDefault(demand.subjectId, 0)
                    val amount = allocate(demand, minOf(demandRemaining, capLeft, snapshot.policy.preferredBlockMinutes), respectShare)
                    if (amount > 0) {
                        val after = scheduledBySubject.getOrDefault(demand.subjectId, 0)
                        val maintenance = maintenanceBySubject.getOrDefault(demand.subjectId, 0)
                        flexibleBySubject[demand.subjectId] = (after - maintenance).coerceAtLeast(0)
                        madeProgress = madeProgress || after > before
                    }
                }
            } while (madeProgress && freeByDate.values.any { it > 0 } && remaining.values.any { it > 0 })
        }
        flexiblePass(respectShare = true)
        if (dailyShare < 100) flexiblePass(respectShare = false)

        val dependencyUnallocated = blocked.map {
            UnallocatedDemand(it.id, it.minutes, "Dependência ainda não concluída.")
        }
        val capacityUnallocated = eligible.mapNotNull { demand ->
            remaining.getValue(demand.id).takeIf { it > 0 }?.let { minutes ->
                val reasonText = if (capEnabled && freeByDate.values.sum() > 0) {
                    "Limite semanal da matéria atingido."
                } else "Capacidade disponível insuficiente."
                UnallocatedDemand(demand.id, minutes, reasonText)
            }
        }
        val unallocated = dependencyUnallocated + capacityUnallocated
        val required = allDemands.sumOf { it.minutes }
        val scheduled = generated.sumOf { it.plannedMinutes }
        val weeklyCapacity = snapshot.availability.values.sumOf { it.effectiveMinutes }
        val forecast = StudyPlanForecastCalculator.forecast(
            start = snapshot.today,
            remainingMinutes = snapshot.remainingPhaseMinutes,
            weeklyCapacityMinutes = weeklyCapacity,
            recurringReservedMinutes = snapshot.recurringReservedMinutes,
        )
        val explanations = buildList {
            snapshot.notes.forEach { add(PlanningExplanation("METHOD", it)) }
            if (required > scheduled) add(PlanningExplanation("CAPACITY_DEFICIT", "${required - scheduled} minuto(s) de demanda não couberam no horizonte."))
            if (capEnabled && capacityUnallocated.any { it.reason.startsWith("Limite") }) {
                add(PlanningExplanation("SUBJECT_CAP", "O limite flexível por matéria preservou a diversidade semanal."))
            }
        }
        val normalized = buildString {
            append(snapshot.planId).append('|').append(snapshot.revision).append('|').append(reason.name)
            generated.forEach { append('|').append(it.id).append(':').append(it.plannedMinutes) }
            unallocated.forEach { append('|').append(it.demandId).append(':').append(it.remainingMinutes) }
        }
        return PlanningProposal(
            id = stableId(normalized),
            planId = snapshot.planId,
            baseRevision = snapshot.revision,
            preservedTaskIds = fixedTasks.mapTo(linkedSetOf()) { it.id },
            transitions = replanCandidates.map { task ->
                TaskTransition(
                    taskId = task.id,
                    from = task.status,
                    to = if (task.date < snapshot.today && task.status == PlanTaskStatus.PLANEJADA) {
                        PlanTaskStatus.NAO_REALIZADA
                    } else PlanTaskStatus.REPROGRAMADA,
                    reason = reason.name,
                )
            },
            newTasks = generated,
            capacity = CapacityReport(
                requiredMinutes = required,
                availableMinutes = totalAllocatable,
                deficitMinutes = (required - scheduled).coerceAtLeast(0),
                availableByDate = allocatableByDate,
                unallocated = unallocated,
            ),
            explanations = explanations,
            forecast = forecast,
        )
    }

    private fun capacityFor(snapshot: StudyPlannerSnapshot, date: LocalDate): Int {
        val override = snapshot.dayOverrides[date]
        if (override?.unavailable == true) return 0
        if (override?.minutes != null) return override.minutes
        return snapshot.availability[date.dayOfWeek]?.effectiveMinutes ?: 0
    }

    private fun demandComparator(snapshot: StudyPlannerSnapshot): Comparator<TaskDemand> {
        val performance = snapshot.topicPerformance.associateBy { it.topicId }
        // A ordem escolhida por quem montou a demanda vence a pontuação: o plano sem IA já decidiu
        // o rodízio das matérias e a sequência teoria → questões, e não quer que ela seja refeita.
        return compareBy<TaskDemand> { it.order }.thenByDescending {
            scoring.score(it, snapshot.today, it.topicId?.let(performance::get), snapshot.policy)
        }.thenBy { it.deadline ?: LocalDate.MAX }
            .thenBy { it.subjectPosition }
            .thenBy { it.topicPosition }
            .thenBy { it.id }
    }

    private fun stableId(vararg values: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(values.joinToString("|").toByteArray())
        return bytes.take(16).joinToString("") { "%02x".format(it) }
    }

    private companion object {
        val terminalInactiveStatuses = setOf(
            PlanTaskStatus.CONCLUIDA,
            PlanTaskStatus.REPROGRAMADA,
            PlanTaskStatus.NAO_REALIZADA,
            PlanTaskStatus.PAUSADA,
        )
    }
}
