package br.com.estudario.domain.planner

import kotlin.math.abs
import kotlin.math.sqrt

/**
 * O motor de necessidade, o cérebro do Smart Planner.
 *
 * Ele responde uma pergunta só: *quanta atenção esta matéria (ou tópico) precisa receber agora?*
 * A resposta é um número em 0..1 que depois decide fila, frequência, número de sessões, volume de
 * questões e proximidade das revisões.
 *
 * Como o número é formado, na ordem:
 *
 * 1. Cada fator vira um sinal normalizado em 0..1 (prioridade, dificuldade, déficit de domínio,
 *    cobertura restante, revisão vencida, atraso, tempo sem contato).
 * 2. Os sinais entram numa **média ponderada**, não numa multiplicação, que geraria extremos: uma
 *    matéria com um fator zerado sumiria do plano e uma com todos altos dominaria tudo.
 * 3. O resultado passa por uma modulação suave de prioridade, limitada por
 *    [PlannerWeights.PRIORITY_AMPLITUDE], para que duas matérias com a mesma necessidade bruta e
 *    prioridades diferentes não empatem.
 * 4. O valor final é preso entre [PlannerWeights.MINIMUM_ACTIVE_NEED] e 1.0: matéria de peso baixo
 *    recebe pouco, nunca nada, o plano não apaga conteúdo cobrado em silêncio.
 *
 * O ponto central é o item que diferencia este motor de uma distribuição ingênua: **a evidência
 * vence a declaração, na medida da confiança estatística**. Quem marcou "Segurança é muito
 * difícil" e depois acerta 95% em 200 questões vê o peso extra desaparecer sozinho; quem marcou
 * "fácil" e erra metade vê o peso aparecer. Nada de IA, [confidenceOf] e [wilsonLowerBound].
 */
object StudyNeedCalculator {

    /**
     * Confiança da amostra em 0..1. Em [PlannerWeights.CONFIDENCE_HALF_SAMPLE] respostas vale 0,5;
     * cresce rápido no começo e satura devagar, que é como a certeza sobre alguém realmente se
     * comporta. Uma resposta única praticamente não move nada.
     */
    fun confidenceOf(answered: Int): Double {
        val n = answered.coerceAtLeast(0).toDouble()
        return n / (n + PlannerWeights.CONFIDENCE_HALF_SAMPLE)
    }

    /**
     * Limite inferior do intervalo de Wilson (95%) para a taxa de acerto.
     *
     * É o que impede o motor de tratar "1 de 1" como domínio: 1/1 devolve ~0,21, 19/20 devolve
     * ~0,76 e 190/200 devolve ~0,91. Acerto alto com amostra pequena é tratado como o que é,
     * ainda não sabemos.
     */
    fun wilsonLowerBound(correct: Int, answered: Int): Double =
        wilsonBounds(correct, answered).first

    /**
     * Limite superior do mesmo intervalo.
     *
     * Existe por simetria de honestidade: assim como acerto alto com amostra pequena não prova
     * domínio, acerto baixo com amostra pequena não prova fraqueza. O motor só chama um conteúdo
     * de fraco quando o intervalo inteiro está abaixo do limiar, ver [QuestionAllocator.maturityOf].
     */
    fun wilsonUpperBound(correct: Int, answered: Int): Double =
        wilsonBounds(correct, answered).second

    /** Intervalo de confiança de 95% (Wilson) para a taxa de acerto. */
    fun wilsonBounds(correct: Int, answered: Int): Pair<Double, Double> {
        if (answered <= 0) return 0.0 to 1.0
        val n = answered.toDouble()
        val p = correct.toDouble() / n
        val z = 1.96
        val z2 = z * z
        val denominator = 1.0 + z2 / n
        val center = p + z2 / (2 * n)
        val margin = z * sqrt((p * (1 - p) + z2 / (4 * n)) / n)
        val lower = ((center - margin) / denominator).coerceIn(0.0, 1.0)
        val upper = ((center + margin) / denominator).coerceIn(0.0, 1.0)
        return lower to upper
    }

    /**
     * Taxa de acerto crua, a que a pessoa vê nas estatísticas.
     *
     * O planejamento usa a estimativa conservadora ([observedAccuracy]); o texto mostrado usa esta.
     * Dizer "seu acerto foi 60%" para quem acertou 75% seria o app mentindo para justificar a
     * própria margem de segurança.
     */
    fun rawAccuracy(evidence: StudyEvidence): Double? {
        if (evidence.answered <= 0) return null
        if (evidence.recentAnswered >= PlannerWeights.MINIMUM_MEANINGFUL_SAMPLE) {
            return evidence.recentCorrect.toDouble() / evidence.recentAnswered
        }
        return evidence.correct.toDouble() / evidence.answered
    }

    /**
     * Acerto observado, combinando histórico e janela recente. A janela recente pesa mais porque o
     * plano precisa reagir à tendência, mas só entra quando tem tamanho suficiente para significar
     * alguma coisa.
     */
    fun observedAccuracy(evidence: StudyEvidence): Double {
        val lifetime = wilsonLowerBound(evidence.correct, evidence.answered)
        if (evidence.recentAnswered < PlannerWeights.MINIMUM_MEANINGFUL_SAMPLE) return lifetime
        val recent = wilsonLowerBound(evidence.recentCorrect, evidence.recentAnswered)
        return RECENT_SHARE * recent + (1 - RECENT_SHARE) * lifetime
    }

    /**
     * Calcula a necessidade de uma matéria ou tópico.
     *
     * @param weights perfil de pesos, normalmente [PlannerWeights.forPhase] da fase atual.
     * @param daysUntilExam dias até a prova, ou `null` quando não há data. Sem data o plano
     *   continua funcionando: a urgência apenas não entra na conta.
     * @param maintenanceOnly conteúdo coberto e consolidado, que entra só para não enferrujar.
     */
    fun evaluate(
        subjectId: Long,
        topicId: Long? = null,
        dimensions: StudyDimensions = StudyDimensions.DEFAULT,
        evidence: StudyEvidence = StudyEvidence.EMPTY,
        weights: PlannerWeights = PlannerWeights.DEFAULT,
        daysUntilExam: Int? = null,
        maintenanceOnly: Boolean = false,
    ): StudyNeed {
        val priority = dimensions.examPriority.normalized
        val declaredDifficulty = dimensions.personalDifficulty.normalized
        val priorKnowledge = dimensions.initialKnowledge.normalized

        val confidence = confidenceOf(evidence.answered)
        val accuracy = observedAccuracy(evidence)

        // Dificuldade: parte da declaração e migra para a evidência conforme a amostra cresce.
        val measuredDifficulty = 1.0 - accuracy
        val effectiveDifficulty = blend(declaredDifficulty, measuredDifficulty, confidence)

        // Domínio: mesma lógica. O que a pessoa disse saber vale até os dados dizerem outra coisa.
        val effectiveMastery = blend(priorKnowledge, accuracy, confidence)
        val masteryDeficit = (1.0 - effectiveMastery).coerceIn(0.0, 1.0)

        // Cobertura é outra coisa: "já passei por isso" não é "eu retenho isso".
        val coverage = if (evidence.totalTopics > 0) {
            evidence.coveredTopics.toDouble() / evidence.totalTopics.toDouble()
        } else 0.0
        val remainingCoverage = (1.0 - coverage).coerceIn(0.0, 1.0)

        val revisionDue = saturate(evidence.overdueReviews, PlannerWeights.REVISION_SATURATION_COUNT)
        val overdueFactor = saturate(evidence.missedTasks, PlannerWeights.OVERDUE_SATURATION_TASKS)
        val staleness = evidence.daysSinceContact
            ?.let { saturate(it, PlannerWeights.STALENESS_SATURATION_DAYS) }
            ?: 0.0

        // Déficit de domínio só vale para o que dá para medir. Sem cobertura e sem questões, o que
        // falta é *ver* o conteúdo, isso já está em remainingCoverage, e contar as duas coisas
        // faria a fase de reta final tratar tópico nunca aberto como se fosse ponto fraco.
        val masteryRelevance = maxOf(
            coverage,
            (evidence.answered.toDouble() / PlannerWeights.MINIMUM_MEANINGFUL_SAMPLE).coerceAtMost(1.0),
        )

        // E quem já chega com base gasta menos tempo na primeira passada pelo conteúdo, é para isso
        // que serve perguntar conhecimento prévio separado de dificuldade. Conforme as questões
        // chegam, o desconto passa a vir do desempenho real e não mais da declaração.
        val coverageDiscount = 1.0 - KNOWLEDGE_COVERAGE_DISCOUNT * effectiveMastery

        val contributions = linkedMapOf(
            Signal.EXAM_PRIORITY to weights.examPriority * priority,
            Signal.DIFFICULTY to weights.difficulty * effectiveDifficulty,
            Signal.MASTERY_DEFICIT to weights.masteryDeficit * masteryDeficit * masteryRelevance,
            Signal.REMAINING_COVERAGE to weights.remainingCoverage * remainingCoverage * coverageDiscount,
            Signal.REVISION_DUE to weights.revisionDue * revisionDue,
            Signal.OVERDUE to weights.overdue * overdueFactor,
            Signal.STALENESS to weights.staleness * staleness,
        )
        val weightedMean = contributions.values.sum() / weights.total

        // Urgência não muda o que é necessário; ela aumenta o quanto a prioridade separa as
        // matérias. Perto da prova, o que vale mais ponto abre vantagem mais rápido.
        val urgency = daysUntilExam?.let { days ->
            if (days >= URGENCY_HORIZON_DAYS) 0.0
            else (URGENCY_HORIZON_DAYS - days.coerceAtLeast(0)).toDouble() / URGENCY_HORIZON_DAYS
        } ?: 0.0
        val amplitude = PlannerWeights.PRIORITY_AMPLITUDE * (1.0 + URGENCY_AMPLITUDE_GAIN * urgency)
        val priorityMultiplier = 1.0 + amplitude * (priority - 0.5) * 2.0

        val ceiling = if (maintenanceOnly) MAINTENANCE_CEILING else 1.0
        val score = (weightedMean * priorityMultiplier)
            .coerceIn(PlannerWeights.MINIMUM_ACTIVE_NEED, ceiling)

        val reasons = buildReasons(
            contributions = contributions,
            totalContribution = contributions.values.sum(),
            dimensions = dimensions,
            evidence = evidence,
            confidence = confidence,
            declaredDifficulty = declaredDifficulty,
            effectiveDifficulty = effectiveDifficulty,
            coverage = coverage,
            urgency = urgency,
            maintenanceOnly = maintenanceOnly,
        )

        return StudyNeed(
            subjectId = subjectId,
            topicId = topicId,
            score = score,
            effectiveDifficulty = effectiveDifficulty,
            effectiveMastery = effectiveMastery,
            masteryDeficit = masteryDeficit,
            remainingCoverage = remainingCoverage,
            performanceConfidence = confidence,
            reasons = reasons,
        )
    }

    /**
     * Ordem determinística da fila de necessidade. Score decide; empate é desfeito por prioridade,
     * depois por id, nunca por sorteio, para que a mesma entrada produza sempre o mesmo plano.
     */
    fun queueComparator(): Comparator<StudyNeed> =
        compareByDescending<StudyNeed> { round(it.score) }
            .thenByDescending { round(it.masteryDeficit) }
            .thenBy { it.subjectId }
            .thenBy { it.topicId ?: Long.MIN_VALUE }

    private fun round(value: Double): Double = Math.round(value * 10_000.0) / 10_000.0

    private fun blend(declared: Double, measured: Double, confidence: Double): Double =
        (declared * (1.0 - confidence) + measured * confidence).coerceIn(0.0, 1.0)

    private fun saturate(value: Int, saturation: Int): Double =
        if (saturation <= 0) 0.0 else (value.coerceAtLeast(0).toDouble() / saturation).coerceAtMost(1.0)

    private fun buildReasons(
        contributions: Map<Signal, Double>,
        totalContribution: Double,
        dimensions: StudyDimensions,
        evidence: StudyEvidence,
        confidence: Double,
        declaredDifficulty: Double,
        effectiveDifficulty: Double,
        coverage: Double,
        urgency: Double,
        maintenanceOnly: Boolean,
    ): List<PlannerReason> {
        val reasons = mutableListOf<PlannerReason>()
        fun share(signal: Signal): Double =
            if (totalContribution <= 0.0) 0.0 else contributions.getValue(signal) / totalContribution

        // Prioridade da prova.
        val priorityShare = share(Signal.EXAM_PRIORITY)
        when {
            dimensions.examPriority >= ExamPriority.HIGH ->
                reasons += PlannerReason(PlannerReasonCode.HIGH_EXAM_PRIORITY, priorityShare, dimensions.examPriority.normalized)
            dimensions.examPriority <= ExamPriority.LOW ->
                reasons += PlannerReason(PlannerReasonCode.LOW_EXAM_PRIORITY, priorityShare, dimensions.examPriority.normalized)
        }

        // Dificuldade declarada e o que a evidência fez com ela.
        if (effectiveDifficulty >= HIGH_DIFFICULTY) {
            reasons += PlannerReason(PlannerReasonCode.HIGH_PERSONAL_DIFFICULTY, share(Signal.DIFFICULTY), effectiveDifficulty)
        }
        if (confidence >= EVIDENCE_OVERRIDE_CONFIDENCE && abs(effectiveDifficulty - declaredDifficulty) >= EVIDENCE_OVERRIDE_DELTA) {
            val code = if (effectiveDifficulty < declaredDifficulty) {
                PlannerReasonCode.DIFFICULTY_RELAXED_BY_EVIDENCE
            } else {
                PlannerReasonCode.DIFFICULTY_RAISED_BY_EVIDENCE
            }
            reasons += PlannerReason(code, share(Signal.DIFFICULTY), effectiveDifficulty - declaredDifficulty)
        }

        // Desempenho, o motivo só é afirmado quando a amostra sustenta a afirmação, e o número
        // mostrado é o acerto real da pessoa, não a estimativa conservadora usada no cálculo.
        if (evidence.answered >= PlannerWeights.MINIMUM_MEANINGFUL_SAMPLE) {
            val observed = rawAccuracy(evidence)
            when (QuestionAllocator.confidentBand(evidence)) {
                ContentMaturity.WEAK ->
                    reasons += PlannerReason(PlannerReasonCode.LOW_RECENT_ACCURACY, share(Signal.MASTERY_DEFICIT), observed)
                ContentMaturity.STRONG ->
                    reasons += PlannerReason(PlannerReasonCode.HIGH_RECENT_ACCURACY, share(Signal.MASTERY_DEFICIT), observed)
                else -> Unit
            }
        } else if (evidence.hasSample) {
            reasons += PlannerReason(PlannerReasonCode.INSUFFICIENT_SAMPLE, 0.0, evidence.answered.toDouble())
        }

        // Cobertura, "já passei por isso", separado de domínio.
        val coverageShare = share(Signal.REMAINING_COVERAGE)
        when {
            coverage <= 0.0 -> reasons += PlannerReason(PlannerReasonCode.CONTENT_NOT_STARTED, coverageShare, 0.0)
            coverage >= FULL_COVERAGE -> reasons += PlannerReason(PlannerReasonCode.CONTENT_COVERED, coverageShare, coverage)
            else -> reasons += PlannerReason(PlannerReasonCode.CONTENT_REMAINING, coverageShare, 1.0 - coverage)
        }

        if (dimensions.initialKnowledge >= InitialKnowledge.SOLID && confidence < EVIDENCE_OVERRIDE_CONFIDENCE) {
            reasons += PlannerReason(PlannerReasonCode.STRONG_PRIOR_KNOWLEDGE, 0.0, dimensions.initialKnowledge.normalized)
        }
        if (evidence.overdueReviews > 0) {
            val code = if (evidence.overdueReviews >= PlannerWeights.REVISION_SATURATION_COUNT) {
                PlannerReasonCode.REVISION_OVERDUE
            } else {
                PlannerReasonCode.REVISION_DUE
            }
            reasons += PlannerReason(code, share(Signal.REVISION_DUE), evidence.overdueReviews.toDouble())
        }
        if (evidence.missedTasks > 0) {
            reasons += PlannerReason(PlannerReasonCode.TASKS_MISSED, share(Signal.OVERDUE), evidence.missedTasks.toDouble())
        }
        evidence.daysSinceContact?.takeIf { it >= PlannerWeights.STALENESS_SATURATION_DAYS / 2 }?.let { days ->
            reasons += PlannerReason(PlannerReasonCode.LONG_TIME_WITHOUT_CONTACT, share(Signal.STALENESS), days.toDouble())
        }
        if (urgency > 0.0) {
            reasons += PlannerReason(PlannerReasonCode.EXAM_APPROACHING, 0.0, urgency)
        }
        if (maintenanceOnly) {
            reasons += PlannerReason(PlannerReasonCode.MAINTENANCE_ONLY, 0.0)
        }
        return reasons
    }

    private enum class Signal {
        EXAM_PRIORITY, DIFFICULTY, MASTERY_DEFICIT, REMAINING_COVERAGE, REVISION_DUE, OVERDUE, STALENESS
    }

    /** Quanto a janela recente pesa contra o histórico quando ela tem tamanho suficiente. */
    private const val RECENT_SHARE = 0.60

    /** Dias até a prova a partir dos quais a urgência começa a valer. */
    private const val URGENCY_HORIZON_DAYS = 90

    /** Quanto a urgência amplia a separação por prioridade, no máximo. */
    private const val URGENCY_AMPLITUDE_GAIN = 0.50

    /** Teto de necessidade de conteúdo que só precisa de manutenção. */
    private const val MAINTENANCE_CEILING = 0.35

    /**
     * Quanto o domínio já existente barateia a primeira passada pelo conteúdo. Nunca 100%: ter base
     * não é ter visto, e o edital continua precisando ser coberto.
     */
    private const val KNOWLEDGE_COVERAGE_DISCOUNT = 0.60

    private const val HIGH_DIFFICULTY = 0.65
    private const val FULL_COVERAGE = 0.999
    private const val EVIDENCE_OVERRIDE_CONFIDENCE = 0.50
    private const val EVIDENCE_OVERRIDE_DELTA = 0.15
}
