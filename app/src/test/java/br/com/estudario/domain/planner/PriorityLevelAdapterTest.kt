package br.com.estudario.domain.planner

import br.com.estudario.domain.PriorityLevel
import org.junit.Assert.assertEquals
import org.junit.Test

class PriorityLevelAdapterTest {
    @Test fun mapsStudyPriorityToPlannerPriorityWithoutPretendingFiveLevelsAreFour() {
        assertEquals(PlanPriority.CRITICAL, PriorityLevelAdapter.toPlanPriority(PriorityLevel.VERY_HIGH))
        assertEquals(PlanPriority.HIGH, PriorityLevelAdapter.toPlanPriority(PriorityLevel.HIGH))
        assertEquals(PlanPriority.MEDIUM, PriorityLevelAdapter.toPlanPriority(PriorityLevel.MEDIUM))
        assertEquals(PlanPriority.LOW, PriorityLevelAdapter.toPlanPriority(PriorityLevel.LOW))
        assertEquals(PlanPriority.LOW, PriorityLevelAdapter.toPlanPriority(PriorityLevel.VERY_LOW))
    }
}
