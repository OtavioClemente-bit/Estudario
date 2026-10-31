package br.com.estudario.domain.planner

import br.com.estudario.math.javaRound
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import br.com.estudario.time.*

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
    /** Teto de questões numa tarefa deste tópico (as salvas, ou o que a geração entrega); null = sem teto. */
    val questionCap: Int? = null,
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
    /** Último dia em que cada tópico teve questões feitas no plano (treino, reforço, diagnóstico). */
    val lastPracticeByTopic: Map<Long, LocalDate> = emptyMap(),
    /** Último dia da revisão de matéria (questões misturadas da matéria) feita, por matéria. */
    val lastSubjectMixBySubject: Map<Long, LocalDate> = emptyMap(),
    /** Último dia em que a pessoa estudou um tópico novo de cada matéria (o ritmo conta a partir dele). */
    val lastTheoryBySubject: Map<Long, LocalDate> = emptyMap(),
    /** Quantos tópicos novos da matéria foram estudados nesse último dia. */
    val theoryCountOnLastDay: Map<Long, Int> = emptyMap(),
    /** Matérias que já têm uma revisão de matéria marcada e ainda não feita. */
    val liveSubjectMixes: Set<Long> = emptySet(),
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
    /** Tópicos novos por dia que mantêm o ritmo de fechar o edital no prazo (null = sem teoria no período). */
    val theoryTopicsPerDay: Int? = null,
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
    /** Teoria de retomada, para quem já viu o conteúdo e precisa relembrar. */
    private const val SHORT_THEORY_MINUTES = 25
    /** Diagnóstico de quem diz dominar: questões antes de qualquer teoria. */
    const val DIAGNOSTIC_QUESTIONS = 8
    /** Acerto no diagnóstico (ou em qualquer bateria do tópico) que dispensa a teoria. */
    const val DIAGNOSTIC_PASS = 0.75
    /** Parte da janela de conteúdo usada para ver o edital; o resto é folga para atraso e segunda passada. */
    private const val CONTENT_WINDOW_SHARE = 0.85
    private const val SUBJECT_MIX_INTERVAL_DAYS = 14
    private const val SUBJECT_MIX_QUESTIONS = 15
    private const val MIN_SEEN_FOR_SUBJECT_MIX = 3

    /** A trilha de um tópico, decidida pelo que a pessoa declarou e, acima disso, pelo que as questões mostraram. */
    private enum class Track { FULL, SHORT, DIAGNOSE, COVERED }

    private fun trackOf(topic: BlueprintTopic, subject: BlueprintSubject): Track {
        if (topic.studied) return Track.COVERED
        val ev = topic.evidence
        val accuracy = if (ev.answered > 0) ev.correct.toDouble() / ev.answered else 0.0
        // As questões provaram que sabe: não precisa de teoria, seja qual for a declaração.
        if (ev.answered >= DIAGNOSTIC_QUESTIONS && accuracy >= DIAGNOSTIC_PASS) return Track.COVERED
        return when (subject.dimensions.initialKnowledge) {
            InitialKnowledge.NONE, InitialKnowledge.CONTACT -> Track.FULL
            InitialKnowledge.BASIC -> Track.SHORT
            // Disse que domina: diagnóstico primeiro; se já fez e errou, a teoria volta (curta se foi perto).
            InitialKnowledge.SOLID, InitialKnowledge.STRONG -> when {
                ev.answered < DIAGNOSTIC_QUESTIONS -> Track.DIAGNOSE
                accuracy >= 0.5 -> Track.SHORT
                else -> Track.FULL
            }
        }
    }

    /** Quanto a dificuldade declarada estica o tempo de teoria e o volume de questões. */
    private fun difficultyFactor(difficulty: PersonalDifficulty): Double = when (difficulty) {
        PersonalDifficulty.VERY_EASY -> 0.7
        PersonalDifficulty.EASY -> 0.85
        PersonalDifficulty.NORMAL -> 1.0
        PersonalDifficulty.HARD -> 1.3
        PersonalDifficulty.VERY_HARD -> 1.5
    }

    /** O que a resposta de "o quanto você já sabe" faz no plano, para o assistente mostrar ao lado da escolha. */
    fun knowledgeEffect(knowledge: InitialKnowledge, difficulty: PersonalDifficulty = PersonalDifficulty.NORMAL): String {
        val pace = when (difficulty) {
            PersonalDifficulty.VERY_EASY, PersonalDifficulty.EASY -> " Por ser fácil para você, o tempo de cada tópico encolhe."
            PersonalDifficulty.HARD, PersonalDifficulty.VERY_HARD -> " Por ser difícil para você, cada tópico ganha mais tempo e mais questões."
            PersonalDifficulty.NORMAL -> ""
        }
        return when (knowledge) {
            InitialKnowledge.NONE, InitialKnowledge.CONTACT -> "No plano: teoria completa e questões em cada tópico."
            InitialKnowledge.BASIC -> "No plano: teoria curta de retomada e questões em cada tópico."
            InitialKnowledge.SOLID, InitialKnowledge.STRONG -> "No plano: sem teoria. Cada tópico começa com $DIAGNOSTIC_QUESTIONS questões; se errar muito, a teoria volta."
        } + pace
    }

    private fun roundTo5(minutes: Double): Int = (javaRound(minutes / 5.0).toInt() * 5).coerceAtLeast(5)
    private const val REINFORCEMENT_ACCURACY = 70

    /** Simulado fora do plano por enquanto; a configuração continua guardada para quando voltar. */
    const val SIMULATIONS_IN_PLAN = false

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
            return javaRound(subject.weight * factor).toInt().coerceIn(1, MAX_ROTATION_WEIGHT)
        }

        notes += "Fase atual: ${phase.kind.label}, ${mix.describe()}."
        notes += "Teoria usa blocos de $block minutos; tarefas de questões usam cerca de ${config.minutesPerQuestion} min por questão."

        // 1. Revisões espaçadas (D+1, D+7, D+30 e as seguintes) vivem na aba Revisões, com o ritmo que
        // a pessoa dá a cada uma (Difícil volta amanhã). O plano não cria um cartão por revisão, o que
        // lotaria o dia; ele reserva o tempo delas em cada dia (ver StudyPlannerEngine.reviewReserve)
        // e o cartão "revisões para hoje" mostra quantas são.
        val horizonEnd = input.today.plusDays(input.horizonDays.toLong())
        val studiedTopicIds = input.topics.filter { it.studied }.mapTo(hashSetOf()) { it.topicId }
        val dueReviews = input.pendingReviews.filter { it.topicId in studiedTopicIds && it.dueDate.isBefore(horizonEnd) }
        val overdueCount = dueReviews.filter { !it.dueDate.isAfter(input.today) }.distinctBy { it.topicId }.size
        if (dueReviews.isNotEmpty()) {
            notes += if (overdueCount > 0) "$overdueCount revisão(ões) para hoje na aba Revisões: o tempo delas já está separado no seu dia."
            else "As revisões espaçadas do período já têm o tempo separado nos dias em que vencem."
        }


        // 2+3+4. A trilha de cada tópico, no ritmo que fecha o edital antes da reta final.
        //
        // Cada tópico segue a trilha do que a pessoa sabe dele: quem nunca viu tem teoria inteira e
        // questões; quem já viu tem uma teoria curta de retomada; quem diz dominar faz primeiro um
        // diagnóstico de questões, e só volta para a teoria se errar. A dificuldade da matéria estica
        // ou encolhe o tempo. O ritmo não é "o mais rápido possível": cada matéria anda para terminar
        // numa data (as de mais peso antes), e as tarefas de conteúdo novo ficam espalhadas pelas
        // semanas, para o que sobra de tempo virar treino e revisão em vez de engolir o edital em
        // dois meses e deixar o resto vazio.
        val weights = activeSubjects.associate { it.subjectId to effectiveWeight(it) }
        val maxWeight = weights.values.maxOrNull()?.coerceAtLeast(1) ?: 1
        val contentEnd = phases.firstOrNull { it.kind == PlanPhaseKind.RETA_FINAL }?.start?.minusDays(1) ?: phases.last().end
        val contentDays = if (phase.kind == PlanPhaseKind.RETA_FINAL) 0
            else (daysBetween(input.today, contentEnd).toInt() + 1).coerceAtLeast(1)
        var theoryPlanned = 0
        var contentNeeded = 0.0
        var questionsPlanned = 0
        var diagnosticCount = 0
        var skippedByEvidence = 0
        val anchoredUnits = mutableListOf<Triple<LocalDate, Int, List<TaskDemand>>>()
        val horizonEndContent = minOf(input.today.plusDays(input.horizonDays.toLong()), contentEnd.plusDays(1))
        val newUnits = mutableListOf<Pair<Long, List<TaskDemand>>>()

        activeSubjects.forEach { subject ->
            val factor = difficultyFactor(subject.dimensions.personalDifficulty)
            val tracks = topicsBySubject[subject.subjectId].orEmpty()
                .sortedWith(compareBy({ it.position }, { it.topicId }))
                .map { it to trackOf(it, subject) }
            skippedByEvidence += tracks.count { (t, track) -> track == Track.COVERED && !t.studied }
            val pending = tracks.filter { it.second != Track.COVERED }
            if (pending.isEmpty() || contentDays == 0) return@forEach
            fun minutesOf(track: Track) = when (track) {
                Track.FULL -> roundTo5(THEORY_BASE_MINUTES * factor)
                Track.SHORT -> roundTo5(SHORT_THEORY_MINUTES * factor).coerceAtLeast(20)
                Track.DIAGNOSE -> DIAGNOSTIC_QUESTIONS * config.minutesPerQuestion
                Track.COVERED -> 0
            }
            // A matéria de mais peso termina antes (75% da janela); a de menos, no fim dela.
            val weight = weights.getValue(subject.subjectId)
            val finishShare = 1.0 - 0.25 * (weight - 1).toDouble() / (maxWeight - 1).coerceAtLeast(1)
            val window = (contentDays * CONTENT_WINDOW_SHARE * finishShare).coerceAtLeast(7.0)
            // Ritmo da matéria: um tópico a cada `interval` dias, contados do último tópico que a pessoa
            // estudou nela (não de hoje). Assim o ritmo não depende de quantas vezes o plano se refaz:
            // contar de hoje deixava o primeiro tópico sempre "para hoje", e quem conclui tarefas o dia
            // todo engolia o edital em semanas.
            val interval = window * 0.92 / pending.size
            // Conteúdo (teoria + questões do tópico) que esta matéria precisa ver no período para fechar no prazo.
            contentNeeded += pending.sumOf { minutesOf(it.second) * (1.0 + config.questionsPerTopic * config.minutesPerQuestion.toDouble() / THEORY_BASE_MINUTES) } * (input.horizonDays / window).coerceAtMost(1.0)
            val last = input.lastTheoryBySubject[subject.subjectId]
            var used = 0
            var k = 0
            val units = mutableListOf<List<TaskDemand>>()
            for ((topic, track) in pending) {
                val minutes = minutesOf(track)
                // O m-ésimo tópico desde o começo do último dia de estudo cai no dia floor(m × intervalo);
                // os já feitos naquele dia contam, para a matéria que anda mais de um por dia não travar em um.
                val done = if (last == null) 0 else input.theoryCountOnLastDay[subject.subjectId] ?: 1
                val offset = kotlin.math.floor((done + k) * interval).toLong()
                val anchor = (last ?: input.today).plusDays(offset).let { if (it.isBefore(input.today)) input.today else it }
                if (!anchor.isBefore(horizonEndContent)) break
                k++
                val unit = mutableListOf<TaskDemand>()
                if (track == Track.DIAGNOSE) {
                    if ((topic.topicId to PlanTaskType.QUESTIONS) !in input.occupied) {
                        val quantidade = topic.questionCap?.let { minOf(DIAGNOSTIC_QUESTIONS, it) } ?: DIAGNOSTIC_QUESTIONS
                        unit += TaskDemand(
                            id = "diagnostic:${topic.topicId}", subjectId = subject.subjectId, topicId = topic.topicId,
                            type = PlanTaskType.QUESTIONS, minutes = quantidade * config.minutesPerQuestion, questions = quantidade,
                            priority = subject.priority, subjectPosition = subject.position, topicPosition = topic.position,
                        )
                        diagnosticCount++
                        questionsPlanned += quantidade * config.minutesPerQuestion
                    }
                } else {
                    if ((topic.topicId to PlanTaskType.THEORY) !in input.occupied) {
                        unit += TaskDemand(
                            id = "theory:${topic.topicId}", subjectId = subject.subjectId, topicId = topic.topicId,
                            type = PlanTaskType.THEORY, minutes = minutes,
                            priority = subject.priority, subjectPosition = subject.position, topicPosition = topic.position,
                        )
                        theoryPlanned += minutes
                    }
                    // Consolidação: questões do tópico logo depois da teoria. A quantidade sai do
                    // alocador (maturidade × necessidade) e a dificuldade da matéria estica o volume.
                    if (config.questionsPerTopic > 0 && (topic.topicId to PlanTaskType.QUESTIONS) !in input.occupied) {
                        val topicNeed = topic.need ?: subject.need
                        val planned = if (topicNeed == null) config.questionsPerTopic else {
                            QuestionAllocator.allocate(config.questionsPerTopic, topicNeed, topic.evidence, covered = false).questions
                        }
                        val scaled = javaRound(planned * factor.coerceAtLeast(0.8)).toInt().coerceAtLeast(1)
                        val quantidade = topic.questionCap?.let { minOf(scaled, it) } ?: scaled
                        unit += TaskDemand(
                            id = "topic-questions:${topic.topicId}", subjectId = subject.subjectId, topicId = topic.topicId,
                            type = PlanTaskType.QUESTIONS, minutes = quantidade * config.minutesPerQuestion, questions = quantidade,
                            priority = subject.priority, subjectPosition = subject.position, topicPosition = topic.position,
                        )
                        questionsPlanned += quantidade * config.minutesPerQuestion
                    }
                }
                used += minutes
                if (unit.isNotEmpty()) anchoredUnits += Triple(anchor, weight, unit.map { it.copy(anchorDate = anchor.takeIf { a -> a.isAfter(input.today) }) })
            }
        }

        // Conteúdo novo na ordem do dia em que cada tópico pode entrar; no mesmo dia, a matéria de mais
        // peso primeiro. A âncora segura cada tópico no dia dele: o motor não adianta conteúdo novo.
        val ordered = anchoredUnits.sortedWith(compareBy<Triple<LocalDate, Int, List<TaskDemand>>> { it.first }.thenByDescending { it.second })
        ordered.forEach { (_, _, unit) -> unit.forEach { demand -> demands += demand.copy(order = next()) } }
        val subjectsAdvancing = ordered.map { it.third.first().subjectId }.distinct().size
        if (ordered.isNotEmpty()) {
            notes += "$subjectsAdvancing matéria(s) avançando no período, cada uma no ritmo de fechar antes da reta final (as de mais peso primeiro)."
            notes += "Cada tópico segue o que você sabe dele: teoria e questões, teoria curta de retomada ou só um diagnóstico de questões."
        } else if (phase.kind == PlanPhaseKind.RETA_FINAL) {
            notes += "Reta final: sem teoria nova, o tempo vai para questões de todas as matérias e revisão."
        }
        if (diagnosticCount > 0) {
            notes += "$diagnosticCount tópico(s) que você disse dominar começam com um diagnóstico de $DIAGNOSTIC_QUESTIONS questões: acertando ${(DIAGNOSTIC_PASS * 100).toInt()}% ou mais, a teoria sai do plano; errando, ela volta."
        }
        if (skippedByEvidence > 0) {
            notes += "$skippedByEvidence tópico(s) ficaram sem teoria porque suas questões já mostraram que você sabe."
        }

        // 5. Treino: o que já foi visto volta em questões, espalhado por todas as matérias, na ordem da
        // necessidade (peso na prova, acerto, dificuldade e tempo sem ver). Ponto fraco volta em 3
        // dias, o que está consolidando em uma semana, o dominado em duas. Nunca vira bateria de uma
        // matéria só.
        val horizonEnd2 = input.today.plusDays(input.horizonDays.toLong())
        val practiceStreams = mutableListOf<Pair<Int, MutableList<List<TaskDemand>>>>()
        var practiceCount = 0
        activeSubjects.forEach { subject ->
            val factor = difficultyFactor(subject.dimensions.personalDifficulty)
            val pool = topicsBySubject[subject.subjectId].orEmpty()
                .filter { trackOf(it, subject) == Track.COVERED }
                .filter { (it.topicId to PlanTaskType.QUESTIONS) !in input.occupied && (it.topicId to PlanTaskType.ACTIVE_RECALL) !in input.occupied }
                .mapNotNull { topic ->
                    val need = topic.need ?: subject.need
                    // Sem a evidência detalhada, o acerto do tópico decide (10+ respondidas abaixo de 65% é ponto fraco).
                    val maturity = if (need != null && topic.evidence.hasSample) QuestionAllocator.maturityOf(need, topic.evidence, covered = true)
                        else if (topic.answered >= PlannerWeights.MINIMUM_MEANINGFUL_SAMPLE && (topic.accuracyPercent ?: 100) < (PlannerWeights.WEAK_ACCURACY * 100).toInt()) ContentMaturity.WEAK
                        else ContentMaturity.CONSOLIDATING
                    val gap = when (maturity) { ContentMaturity.WEAK -> 3; ContentMaturity.STRONG -> 14; else -> 7 }
                    val last = input.lastPracticeByTopic[topic.topicId] ?: topic.lastStudied
                    val dueOn = last?.plusDays(gap.toLong()) ?: input.today
                    if (!dueOn.isBefore(horizonEnd2)) return@mapNotNull null
                    Triple(topic, need, maturity) to (if (dueOn.isAfter(input.today)) dueOn else null)
                }
                .sortedWith(compareByDescending<Pair<Triple<BlueprintTopic, StudyNeed?, ContentMaturity>, LocalDate?>> { it.first.second?.score ?: 0.0 }
                    .thenBy { it.first.first.accuracyPercent ?: 100 }.thenBy { it.first.first.topicId })
            val units = pool.map { (info, anchor) ->
                val (topic, need, maturity) = info
                val planned = if (need == null) config.questionsPerTopic else QuestionAllocator.allocate(config.questionsPerTopic, need, topic.evidence, covered = true).questions
                val scaled = javaRound(planned * factor.coerceAtLeast(0.8)).toInt().coerceAtLeast(1)
                val quantidade = topic.questionCap?.let { minOf(scaled, it) } ?: scaled
                practiceCount++
                listOf(
                    TaskDemand(
                        id = "practice:${topic.topicId}",
                        subjectId = subject.subjectId, topicId = topic.topicId,
                        type = PlanTaskType.QUESTIONS,
                        minutes = quantidade * config.minutesPerQuestion, questions = quantidade,
                        priority = subject.priority, subjectPosition = subject.position, topicPosition = topic.position,
                        anchorDate = anchor,
                    ),
                )
            }.toMutableList()
            if (units.isNotEmpty()) practiceStreams += weights.getValue(subject.subjectId) to units
        }
        // O treino cede ao conteúdo novo quando o prazo aperta: ele só ocupa o que sobra depois do
        // conteúdo que precisa ser visto no período e das revisões. Com folga, sobra muito; com o
        // edital apertado, quase nada, e o tempo vai para ver o que falta.
        val reviewEstimate = dueReviews.distinctBy { it.topicId }.size * StudyPlannerEngine.REVIEW_MINUTES
        var practiceRoom = (horizonCapacity * 0.95 - contentNeeded - reviewEstimate).toInt()
        weightedMerge(practiceStreams).forEach { unit ->
            if (practiceRoom <= 0) return@forEach
            practiceRoom -= unit.sumOf { it.minutes }
            unit.forEach { demands += it.copy(order = next()) }
        }
        if (practiceCount > 0) notes += "$practiceCount tópico(s) já vistos voltam em questões no período: ponto fraco em 3 dias, o resto em 1 ou 2 semanas."

        // 6. Revisão de matéria: a cada duas semanas, cada matéria com pelo menos três tópicos vistos
        // ganha uma bateria misturada de tudo que já foi visto nela. É o que segura o que ficou para
        // trás sem lotar o dia: uma por matéria, em dias diferentes.
        var mixCount = 0
        activeSubjects.forEachIndexed { index, subject ->
            if (subject.subjectId in input.liveSubjectMixes) return@forEachIndexed
            val seen = topicsBySubject[subject.subjectId].orEmpty().count { trackOf(it, subject) == Track.COVERED }
            if (seen < MIN_SEEN_FOR_SUBJECT_MIX) return@forEachIndexed
            val last = input.lastSubjectMixBySubject[subject.subjectId]
            val due = last?.plusDays(SUBJECT_MIX_INTERVAL_DAYS.toLong()) ?: input.today.plusDays((index % 7).toLong() + 3)
            if (!due.isBefore(horizonEnd2)) return@forEachIndexed
            val quantidade = SUBJECT_MIX_QUESTIONS
            mixCount++
            demands += TaskDemand(
                id = "subject-mix:${subject.subjectId}",
                order = next(),
                subjectId = subject.subjectId,
                topicId = null,
                type = PlanTaskType.QUESTIONS,
                minutes = quantidade * config.minutesPerQuestion,
                questions = quantidade,
                priority = subject.priority,
                anchorDate = due.takeIf { it.isAfter(input.today) },
                subjectPosition = subject.position,
            )
        }
        if (mixCount > 0) notes += "$mixCount revisão(ões) de matéria no período: questões misturadas de tudo que você já viu nela, uma matéria por dia."

        // 7. Discursiva no dia mais livre da semana (simulado fica fora do plano por enquanto).
        val heaviestDays = (0 until input.horizonDays)
            .map { input.today.plusDays(it.toLong()) }
            .filter { it.dayOfWeek == input.heaviestDay }
        val heaviestSubject = activeSubjects.maxByOrNull { it.weight }
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
