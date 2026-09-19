package br.com.estudario.domain

enum class PriorityLevel { VERY_HIGH, HIGH, MEDIUM, LOW, VERY_LOW }

enum class PrioritySource {
    OFFICIAL_EXAM_STRUCTURE,
    HISTORICAL_EVIDENCE,
    AI_INFERENCE,
    DEFAULT,
    USER,
}

enum class PriorityEvidenceType {
    OFFICIAL_QUESTION_COUNT,
    OFFICIAL_WEIGHT,
    OFFICIAL_SCORE,
    ELIMINATION_CRITERION,
    OFFICIAL_DISTRIBUTION,
    BOARD_HISTORY,
    ROLE_OR_AREA_HISTORY,
    TOPIC_RECURRENCE,
    STRUCTURAL_RELEVANCE,
    ABSENCE_OF_EVIDENCE,
}

data class PriorityEvidence(
    val type: PriorityEvidenceType,
    val description: String,
    val value: Double? = null,
)

data class PriorityAssessment(
    val score: Int,
    val source: PrioritySource,
    val confidence: Float,
    val rationale: String?,
    val evidence: List<PriorityEvidence>,
) {
    val level: PriorityLevel get() = PriorityResolver.scoreToLevel(score)
}

data class PriorityState(
    val hasAssessedPriority: Boolean,
    val assessment: PriorityAssessment,
    val userPriorityOverride: PriorityLevel?,
)
