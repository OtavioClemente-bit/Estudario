package br.com.estudario.domain

import kotlin.math.roundToInt

/**
 * Peso real da prova (quantas questões cada matéria tem, lido no quadro do edital) virando
 * prioridade da matéria. É uma avaliação com fonte oficial: a escolha da pessoa
 * (userPriorityOverride) continua mandando por cima dela.
 */
object ExamWeights {
    /** Prioridade de uma matéria pela fatia dela na prova, comparada com a fatia média. */
    fun assess(questions: Int, total: Int, subjectCount: Int): PriorityAssessment {
        val share = if (total > 0) questions.toDouble() / total else 0.0
        val average = if (subjectCount > 0) 1.0 / subjectCount else share
        val ratio = if (average > 0) share / average else 1.0
        val score = when {
            ratio >= 1.8 -> 90
            ratio >= 1.25 -> 72
            ratio >= 0.8 -> 50
            ratio >= 0.5 -> 32
            else -> 15
        }
        val percent = (share * 100).roundToInt()
        val description = "$questions de $total questões na prova ($percent%)"
        return PriorityAssessment(
            score = score,
            source = PrioritySource.OFFICIAL_EXAM_STRUCTURE,
            confidence = 0.95f,
            rationale = "Peso no edital: $description.",
            evidence = listOf(PriorityEvidence(PriorityEvidenceType.OFFICIAL_QUESTION_COUNT, description, questions.toDouble())),
        )
    }

    /** As avaliações de todas as matérias de um edital; vazio se faltar o número de alguma. */
    fun assessAll(questionsBySubject: List<Int?>): List<PriorityAssessment> {
        if (questionsBySubject.isEmpty() || questionsBySubject.any { it == null || it <= 0 }) return emptyList()
        val counts = questionsBySubject.map { it!! }
        val total = counts.sum()
        return counts.map { assess(it, total, counts.size) }
    }

    /** Número de questões na prova guardado na evidência da avaliação (null se não houver). */
    fun questionsOf(evidence: List<PriorityEvidence>): Int? =
        evidence.firstOrNull { it.type == PriorityEvidenceType.OFFICIAL_QUESTION_COUNT }?.value?.roundToInt()?.takeIf { it > 0 }

    /** Evidência no formato do JSON guardado (o mesmo do PriorityEvidenceCodec do app). */
    fun evidenceJson(evidence: List<PriorityEvidence>): String = evidence.joinToString(",", "[", "]") { item ->
        val text = item.description.replace("\\", "\\\\").replace("\"", "\\\"")
        "{\"type\":\"${item.type.name}\",\"description\":\"$text\"${item.value?.let { ",\"value\":$it" } ?: ""}}"
    }

    /** Lê o número de questões do JSON de evidência (app e site guardam igual). */
    fun questionsOfJson(json: String?): Int? {
        if (json.isNullOrBlank()) return null
        val match = Regex("\"type\"\\s*:\\s*\"OFFICIAL_QUESTION_COUNT\"[^}]*?\"value\"\\s*:\\s*([0-9.]+)").find(json) ?: return null
        return match.groupValues[1].toDoubleOrNull()?.roundToInt()?.takeIf { it > 0 }
    }

    /** Pontuação usada pela planta do simulado para um nível de prioridade efetivo. */
    fun levelScore(level: PriorityLevel): Int = when (level) {
        PriorityLevel.VERY_HIGH -> 90
        PriorityLevel.HIGH -> 72
        PriorityLevel.MEDIUM -> 50
        PriorityLevel.LOW -> 32
        PriorityLevel.VERY_LOW -> 15
    }
}
