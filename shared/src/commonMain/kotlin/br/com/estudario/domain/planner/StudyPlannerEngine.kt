package br.com.estudario.domain.planner

import br.com.estudario.text.Sha256
import br.com.estudario.text.toHex
import kotlinx.datetime.LocalDate
import br.com.estudario.time.*

class StudyPlannerEngine(
    private val scoring: PlannerScoringPolicy = PlannerScoringPolicy(),
) {
    fun plan(snapshot: StudyPlannerSnapshot, reason: ReplanReason): PlanningProposal {
        val dates = (0 until snapshot.policy.horizonDays).map { snapshot.today.plusDays(it.toLong()) }
        val rawCapacity = dates.associateWith { date -> capacityFor(snapshot, date) }
        // Grátis: se hoje já tem estudo, hoje fica como está (trocar edital, horas ou matérias não
        // muda a matéria do dia); o replanejamento vale de amanhã em diante.
        val keepToday = snapshot.policy.keepTodayTasks && reason != ReplanReason.INITIAL && snapshot.tasks.any {
            it.date == snapshot.today && it.status !in terminalInactiveStatuses
        }
        val fixedTasks = snapshot.tasks.filter { task ->
            task.status == PlanTaskStatus.CONCLUIDA ||
                task.locked ||
                snapshot.dayOverrides[task.date]?.locked == true ||
                task.subjectId == null ||
                (keepToday && task.date == snapshot.today)
        }
        // Por id: a previsão roda o motor semana a semana com centenas de tarefas, e procurar cada uma
        // na lista (comparando campo a campo) travava o Início do site por segundos.
        val fixedIds = fixedTasks.mapTo(hashSetOf()) { it.id }
        // Tarefa concluída continua ocupando o tempo do dia dela. Sem isso, cada conclusão "liberava"
        // o dia de novo e o replanejamento puxava as matérias de amanhã: o dia nunca terminava.
        val lockedUsage = fixedTasks
            .filter { it.date in rawCapacity.keys && (it.status == PlanTaskStatus.CONCLUIDA || it.status !in terminalInactiveStatuses) }
            .groupBy { it.date }
            .mapValues { (_, tasks) -> tasks.sumOf { it.plannedMinutes } }
        val executionsByTask = snapshot.executions.filter { it.taskId != null }.groupBy { it.taskId }
        // O que já foi estudado numa tarefa em andamento também gastou o dia dela; só o que falta
        // volta para o replanejamento.
        val partialUsage = if (reason == ReplanReason.INITIAL) emptyMap() else snapshot.tasks
            .filter { it.id !in fixedIds && it.status == PlanTaskStatus.EM_ANDAMENTO && it.date in rawCapacity.keys }
            .groupBy { it.date }
            .mapValues { (_, tasks) -> tasks.sumOf { task -> minOf(task.plannedMinutes, executionsByTask[task.id].orEmpty().sumOf { it.minutes }) } }
        val reserve = reviewReserve(snapshot, rawCapacity)
        val allocatableByDate = rawCapacity.mapValues { (date, minutes) ->
            if (snapshot.dayOverrides[date]?.locked == true || (keepToday && date == snapshot.today)) 0
            else (minutes - lockedUsage.getOrElse(date) { 0 } - partialUsage.getOrElse(date) { 0 } - reserve.getOrElse(date) { 0 }).coerceAtLeast(0)
        }
        val freeByDate = allocatableByDate.toMutableMap()
        val replanCandidates = if (reason == ReplanReason.INITIAL) emptyList() else snapshot.tasks.filter { task ->
            task.id !in fixedIds && task.status in setOf(PlanTaskStatus.PLANEJADA, PlanTaskStatus.EM_ANDAMENTO)
        }
        // Concluir, pular ou fazer parte de uma tarefa não traz matéria nova para hoje: o dia fica com
        // o que já estava nele. Sem isso, cada conclusão abria espaço e o plano puxava as de amanhã,
        // e o estudo do dia não acabava nunca.
        if (reason in dayKeepingReasons && snapshot.today in freeByDate) {
            val pendingToday = replanCandidates.filter { it.date == snapshot.today }.sumOf { task ->
                (task.plannedMinutes - executionsByTask[task.id].orEmpty().sumOf { it.minutes }).coerceAtLeast(0)
            }
            freeByDate[snapshot.today] = minOf(freeByDate.getValue(snapshot.today), pendingToday)
        }
        // Revisão espaçada não é mais tarefa do plano (fica na aba Revisões, com o tempo reservado no
        // dia): as que sobraram de planos antigos saem na primeira reorganização, em vez de voltar.
        val derivedDemands = replanCandidates.filter { it.type != PlanTaskType.REVIEW }.mapNotNull { task ->
            val executedMinutes = executionsByTask[task.id].orEmpty().sumOf { it.minutes }
            val executedQuestions = executionsByTask[task.id].orEmpty().sumOf { it.questions }
            val remainingMinutes = (task.plannedMinutes - executedMinutes).coerceAtLeast(0)
            // Feita quase toda (falta menos que um bloco mínimo): não volta como caco de poucos minutos.
            val leftover = executedMinutes > 0 && remainingMinutes < snapshot.policy.minimumBlockMinutes
            // Questões que sobram, até o que o tópico tem salvo (a tarefa antiga podia pedir 20 de 10).
            val leftQuestions = (task.plannedQuestions - executedQuestions).coerceAtLeast(0)
            val cap = task.topicId?.let { snapshot.questionCapByTopic[it] }
            val questions = if (cap != null && task.type in cappedTypes) minOf(leftQuestions, (cap - executedQuestions).coerceAtLeast(0)) else leftQuestions
            val minutes = if (questions < leftQuestions && leftQuestions > 0) {
                (remainingMinutes * questions / leftQuestions).coerceAtLeast(snapshot.policy.minimumBlockMinutes)
            } else remainingMinutes
            remainingMinutes.takeIf { it > 0 && !leftover && !(questions == 0 && leftQuestions > 0) }?.let {
                TaskDemand(
                    id = "replan:${task.id}",
                    subjectId = task.subjectId ?: return@mapNotNull null,
                    topicId = task.topicId,
                    type = task.type,
                    minutes = minutes,
                    questions = questions,
                    priority = task.priority,
                    deadline = task.deadline,
                    subjectPosition = task.subjectPosition,
                    topicPosition = task.topicPosition,
                    replannedFromTaskId = task.id,
                    // O que ficou para trás volta na frente (colocar em dia vem antes de avançar), e o que já
                    // estava marcado mantém a ordem do calendário em que estava. Antes todas iam com a mesma
                    // ordem e o desempate era a pontuação: a teoria de matéria de pouco peso perdia o lugar a
                    // cada replanejamento e andava para a frente (de dezembro a março) sem nunca chegar.
                    order = (daysBetween(snapshot.today, task.date).toInt().coerceIn(-1, 999) - 1_001) * 1_000 + task.sequence.coerceIn(0, 999),
                    // Tarefa de um dia que ainda não chegou nunca é adiantada pelo replanejamento: fica no
                    // dia dela ou depois. Sem isso, concluir hoje puxava as tarefas de amanhã para hoje.
                    anchorDate = task.date.takeIf { it > snapshot.today },
                )
            }
        }
        // Refazer do zero: o que não foi feito sai (vira reprogramado) e nada entra agora; o DAY_MISSED
        // seguinte monta o plano pelo método atual, sem carregar a estrutura antiga.
        val allDemands = if (reason == ReplanReason.REBUILD) emptyList() else snapshot.demands + derivedDemands
        val eligible = allDemands
            .filterNot { demand -> snapshot.subjects.firstOrNull { it.subjectId == demand.subjectId }?.paused == true }
            .filter { it.dependsOnTaskIds.all(snapshot.completedDependencyIds::contains) }
            .sortedWith(demandComparator(snapshot))
        val blocked = allDemands.filterNot { it in eligible }
        val remaining = eligible.associate { it.id to it.minutes }.toMutableMap()
        val remainingQuestions = eligible.associate { it.id to it.questions }.toMutableMap()
        val generated = mutableListOf<PlannerTask>()
        // Blocos da mesma demanda no mesmo dia viram uma tarefa só ("Teoria · 50 min"), em vez de
        // dois cartões iguais seguidos.
        // A chave é tópico + tipo (quando há tópico): duas demandas do mesmo assunto no mesmo dia,
        // como a sobra remarcada e a nova, também viram um cartão só.
        val generatedIndex = mutableMapOf<Pair<String, LocalDate>, Int>()
        fun mergeKey(demand: TaskDemand) = demand.topicId?.let { "topic:$it:${demand.type}" } ?: demand.id
        val scheduledBySubject = mutableMapOf<Long, Int>()
        var sequence = 0

        val usedBySubjectPerDate = mutableMapOf<Pair<LocalDate, Long>, Int>()
        val dailyShare = snapshot.policy.dailySubjectSharePercent
        // Tópicos com teoria em cada dia (os que já ficam e os novos), para o teto de matérias por dia.
        val topicCap = snapshot.policy.maxTheoryTopicsPerDay
        val theoryTopicsByDate = mutableMapOf<LocalDate, MutableSet<Long>>()
        fixedTasks.filter { it.type == PlanTaskType.THEORY && it.topicId != null && (it.status == PlanTaskStatus.CONCLUIDA || it.status !in terminalInactiveStatuses) }
            .forEach { theoryTopicsByDate.getOrPut(it.date) { mutableSetOf() } += it.topicId!! }
        fun topicFits(demand: TaskDemand, date: LocalDate): Boolean {
            if (topicCap == null || demand.type != PlanTaskType.THEORY || demand.topicId == null) return true
            val day = theoryTopicsByDate[date] ?: return true
            return demand.topicId in day || day.size < topicCap
        }
        // Teto por dia e por matéria: evita quatro horas seguidas da mesma coisa. Quando nenhum dia
        // aceita a matéria, a segunda passada roda sem o teto para não desperdiçar capacidade.
        fun dateFor(demand: TaskDemand, respectShare: Boolean): LocalDate? {
            val from = demand.anchorDate
            val window = if (from == null) dates else dates.filterNot { it.isBefore(from) }.ifEmpty { dates }
            return window.firstOrNull { date ->
                if (freeByDate.getValue(date) <= 0) return@firstOrNull false
                if (!topicFits(demand, date)) return@firstOrNull false
                if (!respectShare || dailyShare >= 100) return@firstOrNull true
                val capacity = rawCapacity.getValue(date)
                val cap = (capacity * dailyShare / 100).coerceAtLeast(snapshot.policy.preferredBlockMinutes)
                usedBySubjectPerDate.getOrElse(date to demand.subjectId) { 0 } < cap
            }
        }

        fun allocate(demand: TaskDemand, maximum: Int, respectShare: Boolean = true): Int {
            var left = minOf(maximum, remaining.getValue(demand.id))
            var allocated = 0
            while (left > 0) {
                val date = dateFor(demand, respectShare) ?: break
                val free = freeByDate.getValue(date)
                val minimum = snapshot.policy.minimumBlockMinutes
                var chunk = minOf(left, free, snapshot.policy.preferredBlockMinutes.coerceAtLeast(minimum))
                // Nunca deixa um caco para outro dia ("Java · 2 min"): a sobra menor que um bloco mínimo
                // vai junto, mesmo passando um pouco do dia (até um bloco mínimo); se nem assim cabe, o
                // corte vem antes, para o pedaço seguinte ter ao menos um bloco mínimo.
                val rest = left - chunk
                if (rest in 1 until minimum) {
                    when {
                        left - free <= minimum && left <= snapshot.policy.preferredBlockMinutes + minimum -> chunk = left
                        chunk - (minimum - rest) >= minimum -> chunk -= (minimum - rest)
                    }
                }
                if (chunk < snapshot.policy.minimumBlockMinutes && left >= snapshot.policy.minimumBlockMinutes) {
                    freeByDate[date] = 0
                    continue
                }
                val questionLeft = remainingQuestions.getValue(demand.id)
                val minuteLeftBefore = remaining.getValue(demand.id)
                val chunkQuestions = if (chunk == minuteLeftBefore) questionLeft
                else if (minuteLeftBefore == 0) 0
                else questionLeft * chunk / minuteLeftBefore
                val sameDay = generatedIndex[mergeKey(demand) to date]
                if (sameDay != null) {
                    val previous = generated[sameDay]
                    generated[sameDay] = previous.copy(
                        plannedMinutes = previous.plannedMinutes + chunk,
                        plannedQuestions = previous.plannedQuestions + chunkQuestions,
                    )
                } else {
                    val position = sequence++
                    generatedIndex[mergeKey(demand) to date] = generated.size
                    generated += PlannerTask(
                        id = stableId(snapshot.planId, snapshot.revision.toString(), demand.id, date.toString(), position.toString()),
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
                        sequence = position,
                    )
                }
                freeByDate[date] = (free - chunk).coerceAtLeast(0)
                if (demand.type == PlanTaskType.THEORY) demand.topicId?.let { theoryTopicsByDate.getOrPut(date) { mutableSetOf() } += it }
                usedBySubjectPerDate[date to demand.subjectId] = usedBySubjectPerDate.getOrElse(date to demand.subjectId) { 0 } + chunk
                remaining[demand.id] = minuteLeftBefore - chunk
                remainingQuestions[demand.id] = questionLeft - chunkQuestions
                left -= chunk
                allocated += chunk
                scheduledBySubject[demand.subjectId] = scheduledBySubject.getOrElse(demand.subjectId) { 0 } + chunk
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
                        (flexibleCap - flexibleBySubject.getOrElse(demand.subjectId) { 0 }).coerceAtLeast(0)
                    } else demandRemaining
                    if (capLeft == 0) return@forEach
                    val before = scheduledBySubject.getOrElse(demand.subjectId) { 0 }
                    // A demanda inteira de uma vez (em blocos, dentro do teto do dia): antes ia um
                    // bloco por volta do rodízio, e as questões de um tópico entravam entre as duas
                    // metades da teoria dele.
                    val amount = allocate(demand, minOf(demandRemaining, capLeft), respectShare)
                    if (amount > 0) {
                        val after = scheduledBySubject.getOrElse(demand.subjectId) { 0 }
                        val maintenance = maintenanceBySubject.getOrElse(demand.subjectId) { 0 }
                        flexibleBySubject[demand.subjectId] = (after - maintenance).coerceAtLeast(0)
                        madeProgress = madeProgress || after > before
                    }
                }
            } while (madeProgress && freeByDate.values.any { it > 0 } && remaining.values.any { it > 0 })
        }
        flexiblePass(respectShare = true)
        if (dailyShare < 100) flexiblePass(respectShare = false)

        // Dia com tempo sobrando e nada mais para fazer: o próximo tópico novo (teoria e as questões dele)
        // vem para cá. O ritmo do edital é um piso, não um teto: quem tem tempo livre adianta em vez de
        // ficar parado. Hoje, depois de concluir alguma coisa, não ganha tópico novo (freeByDate já veio
        // limitado ao que estava no dia).
        run {
            val minimum = snapshot.policy.minimumBlockMinutes
            dates.forEach { date ->
                var guard = 0
                while (guard++ < 8) {
                    val free = freeByDate.getValue(date)
                    if (free < minimum) break
                    if (topicCap != null && (theoryTopicsByDate[date]?.size ?: 0) >= topicCap) break
                    val theory = generated.filter { it.type == PlanTaskType.THEORY && it.topicId != null && it.date > date }
                        .minWithOrNull(compareBy({ it.date }, { it.sequence })) ?: break
                    val bundle = generated.filter { it.topicId == theory.topicId && it.date == theory.date && it.type in setOf(PlanTaskType.THEORY, PlanTaskType.QUESTIONS) }
                    val need = bundle.sumOf { it.plannedMinutes }
                    if (need > free) break
                    val from = theory.date
                    bundle.forEach { task -> generated[generated.indexOf(task)] = task.copy(date = date, sequence = sequence++) }
                    freeByDate[date] = (free - need).coerceAtLeast(0)
                    freeByDate[from] = freeByDate.getValue(from) + need
                    theoryTopicsByDate.getOrPut(date) { mutableSetOf() } += theory.topicId!!
                    theoryTopicsByDate[from]?.remove(theory.topicId)
                }
            }
        }

        // A teoria de um tópico vem antes das questões e revisões dele. Ao remarcar o atrasado, a
        // teoria podia cair num dia depois das questões do mesmo tópico; elas vão para o dia da
        // (última parte da) teoria, logo depois dela (juntando com o cartão do mesmo tipo, se houver).
        run {
            val lastTheory = generated.filter { it.type == PlanTaskType.THEORY && it.topicId != null }
                .groupBy { it.topicId!! }.mapValues { (_, l) -> l.maxWith(compareBy({ it.date }, { it.sequence })) }
            val fixed = mutableListOf<PlannerTask>()
            generated.forEach { task ->
                val theory = task.topicId?.let { lastTheory[it] }
                val moved = if (theory != null && task.type != PlanTaskType.THEORY && task.date < theory.date) task.copy(date = theory.date, sequence = theory.sequence) else task
                val twin = fixed.indexOfFirst { it.topicId != null && it.topicId == moved.topicId && it.type == moved.type && it.date == moved.date }
                if (moved !== task && twin >= 0) {
                    val prev = fixed[twin]
                    fixed[twin] = prev.copy(plannedMinutes = prev.plannedMinutes + moved.plannedMinutes, plannedQuestions = prev.plannedQuestions + moved.plannedQuestions)
                } else fixed += moved
            }
            generated.clear(); generated += fixed
        }

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

    /**
     * Tempo de cada dia separado para as revisões espaçadas da aba Revisões: uma por tópico que vence no
     * dia (as atrasadas contam hoje), a [REVIEW_MINUTES] cada, até [REVIEW_SHARE] do dia. O resto do
     * dia é do plano; revisão demais num dia só não engole o estudo novo, ela espera na aba.
     */
    fun reviewReserve(snapshot: StudyPlannerSnapshot, capacity: Map<LocalDate, Int>): Map<LocalDate, Int> {
        val byDate = snapshot.reviews
            .groupBy { it.topicId }
            .map { (_, list) -> list.minOf { it.dueDate } }
            .map { if (it.isBefore(snapshot.today)) snapshot.today else it }
            .groupingBy { it }.eachCount()
        return byDate.mapNotNull { (date, count) ->
            val day = capacity[date] ?: return@mapNotNull null
            date to minOf(count * REVIEW_MINUTES, day * REVIEW_SHARE / 100)
        }.toMap()
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
        }.thenBy { it.deadline ?: MAX_DATE }
            .thenBy { it.subjectPosition }
            .thenBy { it.topicPosition }
            .thenBy { it.id }
    }

    private fun stableId(vararg values: String): String {
        val bytes = Sha256.digest(values.joinToString("|").encodeToByteArray())
        return bytes.copyOf(16).toHex()
    }

    companion object {
        /** Minutos de uma revisão espaçada (o cartão da aba Revisões estima o mesmo por revisão). */
        const val REVIEW_MINUTES = 4
        /** Teto do dia que as revisões podem reservar, em porcento. */
        const val REVIEW_SHARE = 35
        /** Tarefas cujo tamanho vem de questões do tópico (e por isso respeitam questionCapByTopic). */
        val cappedTypes = setOf(PlanTaskType.QUESTIONS, PlanTaskType.ACTIVE_RECALL)
        val dayKeepingReasons = setOf(ReplanReason.TASK_COMPLETED, ReplanReason.TASK_PARTIAL, ReplanReason.TASK_SKIPPED)
        val terminalInactiveStatuses = setOf(
            PlanTaskStatus.CONCLUIDA,
            PlanTaskStatus.REPROGRAMADA,
            PlanTaskStatus.NAO_REALIZADA,
            PlanTaskStatus.PAUSADA,
        )
    }
}
