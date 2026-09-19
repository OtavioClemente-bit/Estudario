package br.com.estudario.domain

import org.junit.Assert.assertEquals
import org.junit.Test

class PriorityResolverTest {
    @Test
    fun explicitMediumDoesNotInheritParent() {
        val state = PriorityState(
            hasAssessedPriority = true,
            assessment = PriorityAssessment(50, PrioritySource.DEFAULT, 0f, null, emptyList()),
            userPriorityOverride = null,
        )
        assertEquals(PriorityLevel.MEDIUM, PriorityResolver.effectivePriority(state, PriorityLevel.VERY_HIGH))
    }

    @Test
    fun missingAssessmentInheritsParentThenFallsBackToMedium() {
        val state = PriorityState(false, PriorityAssessment(50, PrioritySource.DEFAULT, 0f, null, emptyList()), null)
        assertEquals(PriorityLevel.HIGH, PriorityResolver.effectivePriority(state, PriorityLevel.HIGH))
        assertEquals(PriorityLevel.MEDIUM, PriorityResolver.effectivePriority(state, null))
    }

    @Test
    fun manualOverrideWinsOverAutomaticAssessment() {
        val state = PriorityState(
            hasAssessedPriority = true,
            assessment = PriorityAssessment(90, PrioritySource.AI_INFERENCE, .8f, null, emptyList()),
            userPriorityOverride = PriorityLevel.LOW,
        )
        assertEquals(PriorityLevel.LOW, PriorityResolver.effectivePriority(state, PriorityLevel.HIGH))
    }
}
