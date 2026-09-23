package br.com.estudario.domain.planner

import java.time.DayOfWeek
import java.time.LocalDate

data class BlueprintSubject(
    val subjectId: Long,
    val name: String,
    /** Importância na prova. Nunca elevada pela dificuldade pessoal, são eixos distintos. */
    val priority: PlanPriority,
    /** Peso de rodízio derivado da prioridade da prova. */
    val weight: Int,
    val position: Int,
    val paused: Boolean = false,
    /** Os três eixos da matéria. Ausente, o motor usa os padrões neutros. */
    val dimensions: StudyDimensions = StudyDimensions.DEFAULT,
    /** Necessidade calculada. Quando vem preenchida, ela modula frequência e volume. */
    val need: StudyNeed? = null,
)

data class BlueprintTopic(
    val topicId: Long,
    val subjectId: Long,
    val title: String,
    val position: Int,
    val depth: Int = 0,
    val studied: Boolean = false,
    val lastStudied: LocalDate? = null,
    val answered: Int = 0,
    val accuracyPercent: Int? = null,
    /** Evidência do tópico, para o alocador de questões classificar a maturidade. */
    val evidence: StudyEvidence = StudyEvidence.EMPTY,
    /** Necessidade do tópico; na falta dela, herda a da matéria. */
    val need: StudyNeed? = null,
)

data class BlueprintInput(
    val today: LocalDate,
    val planStart: LocalDate,
    val examDate: LocalDate?,
    val config: StudyMethodConfig,
    val subjects: List<BlueprintSubject>,
    val topics: List<BlueprintTopic>,
    val pendingReviews: List<ReviewDemand> = emptyList(),
    val weeklyCapacityMinutes: Int,
    val horizonDays: Int = 28,
    /** Pares (tópico, tipo) que já têm tarefa viva, não geramos a mesma coisa duas vezes. */
    val occupied: Set<Pair<Long?, PlanTaskType>> = emptySet(),
    /** Dia da semana com mais tempo livre: é onde o simulado cai. */
    val heaviestDay: DayOfWeek = DayOfWeek.SATURDAY,
)

data class BlueprintResult(
    val phases: List<StudyPhase>,
    val currentPhase: StudyPhase,
    val demands: List<TaskDemand>,
    /** Frases curtas explicando as escolhas, o app mostra isso como "por que este plano". */
    val notes: List<String>,
    val plannedMinutes: Int,
    val horizonCapacityMinutes: Int,
    /** Versão do algoritmo que produziu estas demandas. Viaja com o plano. */
    val algorithmVersion: Int = PlannerWeights.ALGORITHM_VERSION,
)

/**
 * Transforma edital + disponibilidade + método em uma lista de tarefas, sem IA e sem sorteio.
 *
 * A ordem em que as tarefas saem daqui é a ordem em que elas caem no calendário, por isso as
 * matérias são intercaladas aqui dentro, com rodízio proporcional ao peso de cada uma. As regras,
 * na ordem em que mandam:
 *
 * 1. Revisão atrasada ou do dia vem antes de tudo: revisão perdida é conteúdo perdido.
 * 2. Cada tópico novo entra como teoria e, logo em seguida, questões daquele tópico.
 * 3. O tempo é repartido entre teoria, questões e revisão conforme a fase da preparação.
 * 4. Cada matéria recebe tempo proporcional ao seu peso, em rodízio, nunca quatro horas seguidas
 *    da mesma matéria.
 * 5. Tópico já estudado com acerto baixo volta como reforço, do pior para o melhor.
 * 6. Simulado e discursiva caem no dia mais livre da semana, na frequência escolhida.
 * 7. Se a meta semanal de questões não for atingida pelas tarefas acima, entra uma bateria para
 *    fechar a conta.
 */
object StudyPlanBlueprint {

    private const val THEORY_BASE_MINUTES = 50
    private const val REINFORCEMENT_ACCURACY = 70

    /**
     * Piso do fator de necessidade sobre o peso do edital. Com 0,55, uma matéria de necessidade
     * mínima conserva 55% do peso que o edital lhe deu e uma de necessidade máxima chega a 145%,
     * a necessidade modula o rodízio, nunca o substitui.
     */
    private const val NEED_WEIGHT_FLOOR = 0.55
    private const val MAX_ROTATION_WEIGHT = 8

    /** Quantas matérias explicam a própria presença no resumo do plano. */
    private const val EXPLAINED_SUBJECTS = 3

    fun build(input: BlueprintInput): BlueprintResult {
        val config = input.config
        val block = config.blockMinutes
        val phases = StudyMethod.phases(input.planStart, input.examDate, config.profile)
        val phase = StudyMethod.phaseAt(phases, input.today)
        val mix = phase.mix

        val horizonCapacity = (input.weeklyCapacityMinutes * input.horizonDays / 7).coerceAtLeast(block)
        val theoryBudget = horizonCapacity * mix.theoryPercent / 100
        val questionsBudget = horizonCapacity * mix.questionsPercent / 100
        val reviewBudget = horizonCapacity * mix.reviewPercent / 100

        val notes = mutableListOf<String>()
        val demands = mutableListOf<TaskDemand>()
        var order = 0
        fun next() = order++

        val activeSubjects = input.subjects.filterNot { it.paused }.sortedBy { it.position }
        val subjectById = activeSubjects.associateBy { it.subjectId }
        val topicsBySubject = input.topics
            .filter { it.subjectId in subjectById }
            .groupBy { it.subjectId }

        // Peso efetivo do rodízio: o edital define a base, a necessidade modula dentro de limites.
        // Sem NeedScore calculado, o comportamento é exatamente o anterior, o peso do edital puro.
        fun effectiveWeight(subject: BlueprintSubject): Int {
            val need = subject.need ?: return subject.weight.coerceAtLeast(1)
            val factor = NEED_WEIGHT_FLOOR + (1.0 - NEED_WEIGHT_FLOOR) * 2.0 * need.score
            return Math.round(subject.weight * factor).toInt().coerceIn(1, MAX_ROTATION_WEIGHT)
        }

        notes += "Fase atual: ${phase.kind.label}, ${mix.describe()}."
        notes += "Teoria usa blocos de $block minutos; tarefas de questões usam cerca de ${config.minutesPerQuestion} min por questão."

        // 1. Revisões pendentes: têm data e vêm primeiro, da mais atrasada para a mais nova.
        val reviewMinutes = (block / 2).coerceAtLeast(20)
        var reviewUsed = 0
        var overdueCount = 0
        var reviewCount = 0
        for (review in input.pendingReviews.sortedWith(compareBy({ it.dueDate }, { it.topicId }))) {
            val subject = subjectById[review.subjectId] ?: continue
            if ((review.topicId to PlanTaskType.REVIEW) in input.occupied) continue
            val atrasada = !review.dueDate.isAfter(input.today)
            // Revisão atrasada ou do dia entra mesmo que estoure a fatia de revisão do período.
            if (!atrasada && reviewUsed >= reviewBudget) continue
            if (atrasada) overdueCount++
            reviewCount++
            reviewUsed += reviewMinutes
            demands += TaskDemand(
                id = "review:${review.id}",
                order = next(),
                subjectId = review.subjectId,
                topicId = review.topicId,
                type = PlanTaskType.REVIEW,
                minutes = reviewMinutes,
                questions = review.plannedQuestions,
                priority = subject.priority,
                deadline = review.dueDate,
                subjectPosition = subject.position,
            )
        }
        if (reviewCount > 0) {
            notes += if (overdueCount > 0) "$reviewCount revisão(ões) no período, $overdueCount para ontem, elas vêm antes de qualquer conteúdo novo."
            else "$reviewCount revisão(ões) espaçada(s) caem no período."
        }

        // 2+3+4. Teoria e questões por tópico, em rodízio proporcional ao peso da matéria.
        val weights = activeSubjects.associate { it.subjectId to effectiveWeight(it) }
        val totalWeight = weights.values.sum().coerceAtLeast(1)
        val theoryPerTopic = StudyMethod.toBlocks(THEORY_BASE_MINUTES, block)
        val questionsPerTopicMinutes = config.questionsPerTopic * config.minutesPerQuestion
        val streams = mutableListOf<Pair<Int, MutableList<List<TaskDemand>>>>()
        var theoryPlanned = 0
        var questionsPlanned = 0

        activeSubjects.forEach { subject ->
            val subjectWeight = weights.getValue(subject.subjectId)
            val subjectTheoryBudget = theoryBudget * subjectWeight / totalWeight
            var used = 0
            val units = mutableListOf<List<TaskDemand>>()
            val pending = topicsBySubject[subject.subjectId].orEmpty()
                .filterNot { it.studied }
                .sortedWith(compareBy({ it.position }, { it.topicId }))
            for (topic in pending) {
                if (mix.theoryPercent == 0 || used >= subjectTheoryBudget) break
                val unit = mutableListOf<TaskDemand>()
                if ((topic.topicId to PlanTaskType.THEORY) !in input.occupied) {
                    unit += TaskDemand(
                        id = "theory:${topic.topicId}",
                        subjectId = subject.subjectId,
                        topicId = topic.topicId,
                        type = PlanTaskType.THEORY,
                        minutes = theoryPerTopic,
                        priority = subject.priority,
                        subjectPosition = subject.position,
                        topicPosition = topic.position,
                    )
                    used += theoryPerTopic
                    theoryPlanned += theoryPerTopic
                }
                // Consolidação: questões do tópico logo depois da teoria, no mesmo rodízio.
                // A quantidade não é fixa, sai do alocador, que cruza maturidade e necessidade.
                if (config.questionsPerTopic > 0 && (topic.topicId to PlanTaskType.QUESTIONS) !in input.occupied) {
                    val topicNeed = topic.need ?: subject.need
                    val quantidade = if (topicNeed == null) config.questionsPerTopic else {
                        QuestionAllocator.allocate(
                            baseQuestions = config.questionsPerTopic,
                            need = topicNeed,
                            evidence = topic.evidence,
                            covered = topic.studied,
                        ).questions
                    }
                    val minutos = quantidade * config.minutesPerQuestion
                    unit += TaskDemand(
                        id = "topic-questions:${topic.topicId}",
                        subjectId = subject.subjectId,
                        topicId = topic.topicId,
                        type = PlanTaskType.QUESTIONS,
                        minutes = minutos,
                        questions = quantidade,
                        priority = subject.priority,
                        subjectPosition = subject.position,
                        topicPosition = topic.position,
                    )
                    questionsPlanned += minutos
                }
                if (unit.isNotEmpty()) units += unit
            }
            if (units.isNotEmpty()) streams += subjectWeight to units
        }

        val interleaved = weightedMerge(streams)
        interleaved.forEach { unit -> unit.forEach { demand -> demands += demand.copy(order = next()) } }
        if (interleaved.isNotEmpty()) {
            val materias = streams.size
            notes += "$materias matéria(s) em rodízio no período, com tempo proporcional ao peso que você deu."
            notes += "Cada tópico novo entra como teoria e, na sequência, questões do próprio tópico."
        } else if (mix.theoryPercent == 0) {
            notes += "Fase sem teoria nova: o tempo vai para questões, revisão e simulados."
        }

        // 5. Reforço: tópico já estudado com acerto baixo volta, do pior para o melhor.
        var reinforcementUsed = 0
        val reinforcementBudget = (questionsBudget - questionsPlanned).coerceAtLeast(0)
        // A ordem do reforço é a da necessidade, não a do acerto puro: um tópico com 60% numa
        // matéria decisiva precisa voltar antes de um com 55% numa matéria de peso baixo. Sem
        // NeedScore calculado, a ordenação cai no critério anterior (do pior acerto para o melhor).
        val weakTopics = input.topics
            .filter { it.subjectId in subjectById && it.studied }
            .filter { it.answered >= 10 && (it.accuracyPercent ?: 100) < REINFORCEMENT_ACCURACY }
            .filter { (it.topicId to PlanTaskType.ACTIVE_RECALL) !in input.occupied }
            .sortedWith(
                compareByDescending<BlueprintTopic> { topic ->
                    val need = topic.need ?: subjectById[topic.subjectId]?.need
                    need?.score ?: 0.0
                }.thenBy { it.accuracyPercent ?: 100 }.thenBy { it.topicId },
            )
        var reinforcedCount = 0
        for (topic in weakTopics) {
            if (reinforcementUsed >= reinforcementBudget) break
            reinforcedCount++
            val subject = subjectById.getValue(topic.subjectId)
            demands += TaskDemand(
                id = "reinforce:${topic.topicId}",
                order = next(),
                subjectId = topic.subjectId,
                topicId = topic.topicId,
                type = PlanTaskType.ACTIVE_RECALL,
                minutes = questionsPerTopicMinutes,
                questions = config.questionsPerTopic,
                priority = subject.priority,
                subjectPosition = subject.position,
                topicPosition = topic.position,
            )
            reinforcementUsed += questionsPerTopicMinutes
            questionsPlanned += questionsPerTopicMinutes
        }
        if (reinforcedCount > 0) {
            notes += "$reinforcedCount tópico(s) com acerto abaixo de $REINFORCEMENT_ACCURACY% voltam como reforço, do pior para o melhor."
        }

        // 6. Simulado e discursiva no dia mais livre da semana.
        val heaviestDays = (0 until input.horizonDays)
            .map { input.today.plusDays(it.toLong()) }
            .filter { it.dayOfWeek == input.heaviestDay }
        val anchorSubject = activeSubjects.firstOrNull()
        val heaviestSubject = activeSubjects.maxByOrNull { it.weight }
        val simulationInterval = if (config.simulationsPerMonth <= 0 || anchorSubject == null) 0 else (30 / config.simulationsPerMonth).coerceAtLeast(7)
        if (simulationInterval > 0 && anchorSubject != null) {
            val simulationMinutes = StudyMethod.toBlocks(150, block)
            var last: LocalDate? = null
            var count = 0
            heaviestDays.forEach { date ->
                if (last != null && date.toEpochDay() - last!!.toEpochDay() < simulationInterval) return@forEach
                last = date
                count++
                demands += TaskDemand(
                    id = "simulation:${date}",
                    order = next(),
                    subjectId = anchorSubject.subjectId,
                    topicId = null,
                    type = PlanTaskType.SIMULATION,
                    minutes = simulationMinutes,
                    questions = simulationMinutes / config.minutesPerQuestion,
                    priority = PlanPriority.HIGH,
                    deadline = date,
                    anchorDate = date,
                    subjectPosition = -1,
                )
            }
            if (count > 0) notes += "$count simulado(s) no período, sempre no seu dia mais livre (${diaLabel(input.heaviestDay)})."
        }
        if (config.discursivesPerMonth > 0 && heaviestSubject != null) {
            val interval = (30 / config.discursivesPerMonth).coerceAtLeast(7)
            var last: LocalDate? = null
            var count = 0
            heaviestDays.forEach { date ->
                if (last != null && date.toEpochDay() - last!!.toEpochDay() < interval) return@forEach
                last = date
                count++
                demands += TaskDemand(
                    id = "discursive:${date}",
                    order = next(),
                    subjectId = heaviestSubject.subjectId,
                    topicId = null,
                    type = PlanTaskType.DISCURSIVE,
                    minutes = StudyMethod.toBlocks(90, block),
                    priority = PlanPriority.HIGH,
                    deadline = date,
                    anchorDate = date,
                    subjectPosition = -1,
                )
            }
            if (count > 0) notes += "$count discursiva(s) programada(s), escrever à mão e cronometrado."
        }

        // 7. Bateria semanal para fechar a meta de questões.
        val weeks = (input.horizonDays / 7).coerceAtLeast(1)
        val plannedQuestions = demands.sumOf { it.questions }
        val targetQuestions = config.weeklyQuestionsTarget * weeks
        if (config.weeklyQuestionsTarget > 0 && plannedQuestions < targetQuestions && heaviestSubject != null) {
            val missing = targetQuestions - plannedQuestions
            val perWeek = (missing / weeks).coerceAtLeast(1)
            val batteryMinutes = perWeek * config.minutesPerQuestion
            (0 until weeks).forEach { week ->
                val date = input.today.plusDays((week * 7L) + 3)
                demands += TaskDemand(
                    id = "battery:$week",
                    order = next(),
                    subjectId = heaviestSubject.subjectId,
                    topicId = null,
                    type = PlanTaskType.QUESTIONS,
                    minutes = batteryMinutes,
                    questions = perWeek,
                    priority = PlanPriority.MEDIUM,
                    anchorDate = date,
                    subjectPosition = -1,
                )
            }
            notes += "Bateria semanal de $perWeek questões para fechar a meta de ${config.weeklyQuestionsTarget} por semana."
        }

        if (config.includeFlashcards && mix.reviewPercent > 0) {
            notes += "Marcar um tópico como estudado agenda sozinho as revisões D+1, D+7 e D+30, e depois delas o tópico continua voltando, com intervalo maior a cada rodada."
        }

        val planned = demands.sumOf { it.minutes }
        if (planned > horizonCapacity) {
            notes += "A demanda do período passa da sua disponibilidade: o que não couber é remarcado automaticamente."
        }

        // "Por que este plano": as matérias que mais receberam espaço explicam a própria presença,
        // com os motivos que o motor realmente usou. É o equivalente ao que uma IA diria, só que
        // derivado do cálculo, e não gerado.
        activeSubjects
            .mapNotNull { subject -> subject.need?.let { subject to it } }
            .sortedWith(compareByDescending<Pair<BlueprintSubject, StudyNeed>> { it.second.score }.thenBy { it.first.subjectId })
            .take(EXPLAINED_SUBJECTS)
            .forEach { (subject, need) ->
                notes += PlanningExplanationBuilder.explain(subject.name, need)
            }

        return BlueprintResult(
            phases = phases,
            currentPhase = phase,
            demands = demands,
            notes = notes,
            plannedMinutes = planned,
            horizonCapacityMinutes = horizonCapacity,
        )
    }

    /**
     * Rodízio proporcional: uma matéria peso 5 aparece cerca de cinco vezes mais que uma peso 1,
     * mas todas aparecem desde a primeira semana.
     */
    private fun <T> weightedMerge(streams: List<Pair<Int, MutableList<T>>>): List<T> {
        if (streams.isEmpty()) return emptyList()
        val queues = streams.map { it.second }
        val weights = streams.map { it.first.coerceAtLeast(1) }
        val total = weights.sum()
        val credits = IntArray(queues.size)
        val out = mutableListOf<T>()
        while (queues.any { it.isNotEmpty() }) {
            queues.indices.forEach { index -> if (queues[index].isNotEmpty()) credits[index] += weights[index] }
            val pick = queues.indices.filter { queues[it].isNotEmpty() }.maxByOrNull { credits[it] } ?: break
            credits[pick] -= total
            out += queues[pick].removeAt(0)
        }
        return out
    }

    private fun diaLabel(day: DayOfWeek) = when (day) {
        DayOfWeek.MONDAY -> "segunda"; DayOfWeek.TUESDAY -> "terça"; DayOfWeek.WEDNESDAY -> "quarta"
        DayOfWeek.THURSDAY -> "quinta"; DayOfWeek.FRIDAY -> "sexta"; DayOfWeek.SATURDAY -> "sábado"
        DayOfWeek.SUNDAY -> "domingo"
    }
}
