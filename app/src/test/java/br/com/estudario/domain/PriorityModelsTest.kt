package br.com.estudario.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class PriorityModelsTest {
    @Test
    fun scoreToLevelUsesClosedRanges() {
        assertEquals(PriorityLevel.VERY_LOW, PriorityResolver.scoreToLevel(0))
        assertEquals(PriorityLevel.VERY_LOW, PriorityResolver.scoreToLevel(20))
        assertEquals(PriorityLevel.LOW, PriorityResolver.scoreToLevel(21))
        assertEquals(PriorityLevel.LOW, PriorityResolver.scoreToLevel(40))
        assertEquals(PriorityLevel.MEDIUM, PriorityResolver.scoreToLevel(41))
        assertEquals(PriorityLevel.MEDIUM, PriorityResolver.scoreToLevel(60))
        assertEquals(PriorityLevel.HIGH, PriorityResolver.scoreToLevel(61))
        assertEquals(PriorityLevel.HIGH, PriorityResolver.scoreToLevel(80))
        assertEquals(PriorityLevel.VERY_HIGH, PriorityResolver.scoreToLevel(81))
        assertEquals(PriorityLevel.VERY_HIGH, PriorityResolver.scoreToLevel(100))
    }

    @Test
    fun normalizeExternalValuesAndEvidence() {
        assertEquals(0, PriorityResolver.normalizeScore(-5))
        assertEquals(100, PriorityResolver.normalizeScore(120))
        assertEquals(0f, PriorityResolver.normalizeConfidence(-1f))
        assertEquals(1f, PriorityResolver.normalizeConfidence(2f))
        assertEquals(PriorityLevel.MEDIUM, PriorityAssessment(50, PrioritySource.DEFAULT, 0f, null, emptyList()).level)
        assertTrue(PriorityEvidenceType.entries.isNotEmpty())
    }
}
