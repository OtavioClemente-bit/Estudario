package br.com.estudario.domain

import kotlin.math.roundToInt

/**
 * Peso real da prova (lido no quadro do edital) virando prioridade da matéria. É uma avaliação com
 * fonte oficial: a escolha da pessoa (userPriorityOverride) continua mandando por cima dela.
 */
object ExamWeights {
    /**
     * Fatia de uma matéria na prova. [questions]: questões dela; [weight]: peso de cada questão
     * (ex.: 2,5 nos conhecimentos específicos); [blockNote]: quando o edital só dá o total de um bloco
     * ("Conhecimentos específicos: 30 questões para 5 matérias"), as questões são a divisão do bloco e
     * a nota explica isso para a pessoa, sem apresentar como número exato.
     */
    data class Share(val questions: Int, val weight: Double = 1.0, val blockNote: String? = null) {
        val points: Double get() = questions * weight
    }

    /** As avaliações de todas as matérias de um edital; vazio se faltar a fatia de alguma. */
    fun assessAll(shares: List<Share?>): List<PriorityAssessment> {
        if (shares.isEmpty() || shares.any { it == null || it.questions <= 0 || it.weight <= 0 }) return emptyList()
        val list = shares.map { it!! }
        val totalPoints = list.sumOf { it.points }
        val totalQuestions = list.sumOf { it.questions }
        val average = totalPoints / list.size
        return list.map { share ->
            val ratio = share.points / average
            val score = when {
                ratio >= 1.8 -> 90
                ratio >= 1.25 -> 72
                ratio >= 0.8 -> 50
                ratio >= 0.5 -> 32
                else -> 15
            }
            val percent = (share.points * 100 / totalPoints).roundToInt()
            val weightText = if (share.weight != 1.0) ", peso ${format(share.weight)}" else ""
            val description = share.blockNote?.let { "$it$weightText ($percent% da nota)" }
                ?: "${share.questions} de $totalQuestions questões na prova$weightText ($percent% da nota)"
            PriorityAssessment(
                score = score,
                source = PrioritySource.OFFICIAL_EXAM_STRUCTURE,
                confidence = if (share.blockNote != null) 0.7f else 0.95f,
                rationale = "Peso no edital: $description.",
                evidence = listOf(
                    PriorityEvidence(
                        if (share.blockNote != null) PriorityEvidenceType.OFFICIAL_DISTRIBUTION else PriorityEvidenceType.OFFICIAL_QUESTION_COUNT,
                        description,
                        share.questions.toDouble(),
                    ),
                ),
            )
        }
    }

    private fun format(value: Double): String =
        if (value == value.roundToInt().toDouble()) value.roundToInt().toString() else value.toString().replace('.', ',')

    /** Evidência no formato do JSON guardado (o mesmo do PriorityEvidenceCodec do app). */
    fun evidenceJson(evidence: List<PriorityEvidence>): String = evidence.joinToString(",", "[", "]") { item ->
        val text = item.description.replace("\\", "\\\\").replace("\"", "\\\"")
        "{\"type\":\"${item.type.name}\",\"description\":\"$text\"${item.value?.let { ",\"value\":$it" } ?: ""}}"
    }

    private val EVIDENCE = Regex("\"type\"\\s*:\\s*\"(OFFICIAL_QUESTION_COUNT|OFFICIAL_DISTRIBUTION)\"\\s*,\\s*\"description\"\\s*:\\s*\"((?:[^\"\\\\]|\\\\.)*)\"[^}]*?\"value\"\\s*:\\s*([0-9.]+)")

    /** Número de questões da matéria na prova, guardado na evidência (app e site guardam igual). */
    fun questionsOfJson(json: String?): Int? =
        json?.let { EVIDENCE.find(it) }?.groupValues?.get(3)?.toDoubleOrNull()?.roundToInt()?.takeIf { it > 0 }

    /** Frase para mostrar ao lado da matéria ("12 de 70 questões na prova, peso 2 (24% da nota)"). */
    fun descriptionOfJson(json: String?): String? =
        json?.let { EVIDENCE.find(it) }?.groupValues?.get(2)?.replace("\\\"", "\"")?.replace("\\\\", "\\")?.takeIf { it.isNotBlank() }

    /** Pontuação usada pela planta do simulado para um nível de prioridade efetivo. */
    fun levelScore(level: PriorityLevel): Int = when (level) {
        PriorityLevel.VERY_HIGH -> 90
        PriorityLevel.HIGH -> 72
        PriorityLevel.MEDIUM -> 50
        PriorityLevel.LOW -> 32
        PriorityLevel.VERY_LOW -> 15
    }
}
