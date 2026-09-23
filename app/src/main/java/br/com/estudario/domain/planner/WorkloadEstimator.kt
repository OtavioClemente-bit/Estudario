package br.com.estudario.domain.planner

/**
 * Quanto trabalho ainda existe pela frente, em minutos.
 *
 * O plano precisa de um número honesto aqui, porque é ele que sustenta previsão, alerta e
 * estratégia. Um tópico não custa só o tempo da teoria: custa teoria + consolidação em questões +
 * as revisões que ele vai gerar. Contar só a teoria é como o app prometer terminar o edital num
 * prazo que nunca vai cumprir.
 */
data class TopicWorkload(
    val subjectId: Long,
    val topicId: Long,
    val theoryMinutes: Int,
    val questionMinutes: Int,
    val revisionMinutes: Int,
) {
    val totalMinutes: Int get() = theoryMinutes + questionMinutes + revisionMinutes
}

data class WorkloadEstimate(
    val items: List<TopicWorkload>,
    /** Fator de calibração aplicado; 1,0 quando ainda não há histórico suficiente. */
    val calibrationFactor: Double,
    val calibrated: Boolean,
) {
    val totalMinutes: Int get() = items.sumOf { it.totalMinutes }
    val theoryMinutes: Int get() = items.sumOf { it.theoryMinutes }
    val questionMinutes: Int get() = items.sumOf { it.questionMinutes }
    val revisionMinutes: Int get() = items.sumOf { it.revisionMinutes }
}

object WorkloadEstimator {

    /** Quantas revisões um tópico novo costuma gerar até a prova, para efeito de estimativa. */
    private const val EXPECTED_REVISIONS_PER_TOPIC = 3

    /** Histórico mínimo antes de o app confiar no próprio tempo medido. */
    const val MINIMUM_CALIBRATION_SAMPLE = 8

    /** Limites da calibração: o app corrige a estimativa, não a reinventa. */
    private const val MIN_CALIBRATION = 0.70
    private const val MAX_CALIBRATION = 1.60

    /**
     * Fator de calibração a partir do que a pessoa realmente leva.
     *
     * Se as sessões de 50 minutos costumam levar 70, a projeção passa a usar 1,4×, mas só depois
     * de [MINIMUM_CALIBRATION_SAMPLE] execuções. Com pouca amostra o fator é 1,0: é melhor uma
     * estimativa neutra que uma projeção puxada por duas sessões atípicas.
     */
    fun calibrationFactor(plannedMinutes: Int, actualMinutes: Int, sampleSize: Int): Double {
        if (sampleSize < MINIMUM_CALIBRATION_SAMPLE || plannedMinutes <= 0 || actualMinutes <= 0) return 1.0
        return (actualMinutes.toDouble() / plannedMinutes.toDouble()).coerceIn(MIN_CALIBRATION, MAX_CALIBRATION)
    }

    /**
     * Estima o trabalho restante.
     *
     * @param pendingTopics tópicos ainda não cobertos, já filtrados pelas matérias ativas.
     * @param needs necessidade por tópico, usada para dimensionar as questões de cada um.
     * @param evidenceByTopic evidência por tópico, para o alocador de questões.
     */
    fun estimate(
        pendingTopics: List<BlueprintTopic>,
        config: StudyMethodConfig,
        needs: Map<Long, StudyNeed>,
        evidenceByTopic: Map<Long, StudyEvidence> = emptyMap(),
        calibrationFactor: Double = 1.0,
        includeRevisions: Boolean = true,
    ): WorkloadEstimate {
        val factor = calibrationFactor.coerceIn(MIN_CALIBRATION, MAX_CALIBRATION)
        val items = pendingTopics.map { topic ->
            val need = needs[topic.topicId]
            val evidence = evidenceByTopic[topic.topicId] ?: StudyEvidence.EMPTY
            val theory = Math.round(config.blockMinutes * factor).toInt().coerceAtLeast(1)
            val questions = if (need == null) {
                config.questionsPerTopic * config.minutesPerQuestion
            } else {
                QuestionAllocator.allocate(config.questionsPerTopic, need, evidence, topic.studied).questions *
                    config.minutesPerQuestion
            }
            val revisionUnit = (config.blockMinutes / 2).coerceAtLeast(10)
            val revisions = if (includeRevisions) revisionUnit * EXPECTED_REVISIONS_PER_TOPIC else 0
            TopicWorkload(
                subjectId = topic.subjectId,
                topicId = topic.topicId,
                theoryMinutes = theory,
                questionMinutes = Math.round(questions * factor).toInt(),
                revisionMinutes = Math.round(revisions * factor).toInt(),
            )
        }
        return WorkloadEstimate(
            items = items,
            calibrationFactor = factor,
            calibrated = factor != 1.0,
        )
    }

    /** Capacidade semanal em minutos a partir da disponibilidade declarada. */
    fun weeklyCapacityMinutes(availability: Collection<DailyCapacity>): Int =
        availability.sumOf { it.effectiveMinutes }
}
