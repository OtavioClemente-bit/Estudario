package br.com.estudario.domain

object PriorityResolver {
    fun normalizeScore(score: Int): Int = score.coerceIn(0, 100)

    fun normalizeConfidence(confidence: Float): Float =
        if (confidence.isFinite()) confidence.coerceIn(0f, 1f) else 0f

    fun scoreToLevel(score: Int): PriorityLevel = when (normalizeScore(score)) {
        in 81..100 -> PriorityLevel.VERY_HIGH
        in 61..80 -> PriorityLevel.HIGH
        in 41..60 -> PriorityLevel.MEDIUM
        in 21..40 -> PriorityLevel.LOW
        else -> PriorityLevel.VERY_LOW
    }

    fun effectivePriority(state: PriorityState, parent: PriorityLevel?): PriorityLevel =
        state.userPriorityOverride
            ?: state.assessment.level.takeIf { state.hasAssessedPriority }
            ?: parent
            ?: PriorityLevel.MEDIUM
}
