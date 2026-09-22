package br.com.estudario.domain.setup

import br.com.estudario.domain.PriorityLevel
import br.com.estudario.domain.planner.PlanPriority
import org.junit.Assert.assertEquals
import org.junit.Test

class SubjectPlanningPriorityTest {
    @Test
    fun `difficulty raises but never lowers the official planning priority`() {
        val cases = listOf(
            Triple(PlanPriority.LOW, SubjectDifficulty.EASY, PlanPriority.LOW),
            Triple(PlanPriority.MEDIUM, SubjectDifficulty.EASY, PlanPriority.MEDIUM),
            Triple(PlanPriority.HIGH, SubjectDifficulty.EASY, PlanPriority.HIGH),
            Triple(PlanPriority.CRITICAL, SubjectDifficulty.EASY, PlanPriority.CRITICAL),
            Triple(PlanPriority.LOW, SubjectDifficulty.MEDIUM, PlanPriority.MEDIUM),
            Triple(PlanPriority.MEDIUM, SubjectDifficulty.MEDIUM, PlanPriority.MEDIUM),
            Triple(PlanPriority.HIGH, SubjectDifficulty.MEDIUM, PlanPriority.HIGH),
            Triple(PlanPriority.CRITICAL, SubjectDifficulty.MEDIUM, PlanPriority.CRITICAL),
            Triple(PlanPriority.LOW, SubjectDifficulty.HARD, PlanPriority.HIGH),
            Triple(PlanPriority.MEDIUM, SubjectDifficulty.HARD, PlanPriority.HIGH),
            Triple(PlanPriority.HIGH, SubjectDifficulty.HARD, PlanPriority.HIGH),
            Triple(PlanPriority.CRITICAL, SubjectDifficulty.HARD, PlanPriority.CRITICAL),
        )

        cases.forEach { (official, difficulty, expected) ->
            assertEquals("$official + $difficulty", expected, effectivePriority(official, difficulty))
        }
    }

    @Test
    fun `official priority levels map explicitly without relying on enum order`() {
        assertEquals(PlanPriority.CRITICAL, PriorityLevel.VERY_HIGH.toPlanPriority())
        assertEquals(PlanPriority.HIGH, PriorityLevel.HIGH.toPlanPriority())
        assertEquals(PlanPriority.MEDIUM, PriorityLevel.MEDIUM.toPlanPriority())
        assertEquals(PlanPriority.LOW, PriorityLevel.LOW.toPlanPriority())
        assertEquals(PlanPriority.LOW, PriorityLevel.VERY_LOW.toPlanPriority())
    }
}
