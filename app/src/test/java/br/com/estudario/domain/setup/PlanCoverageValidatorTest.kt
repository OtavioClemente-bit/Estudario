package br.com.estudario.domain.setup

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate

class PlanCoverageValidatorTest {
    private val monday = LocalDate.of(2026, 9, 21)
    private val subjects = listOf(
        PlanCoverageSubject("materia-1", "Direito Constitucional", listOf(
            PlanCoverageTopic("topico-1", "Direitos fundamentais"),
            PlanCoverageTopic("topico-2", "Organização do Estado"),
        )),
        PlanCoverageSubject("materia-2", "Informática", emptyList()),
    )

    @Test
    fun completeTopicAndTopiclessSubjectCoverageIsAccepted() {
        val result = PlanCoverageValidator.validate(
            subjects,
            listOf(
                PlanCoverageTask("materia-1", "topico-1", monday, 30),
                PlanCoverageTask("materia-1", "topico-2", monday.plusDays(1), 30),
                PlanCoverageTask("materia-2", null, monday.plusDays(1), 30),
            ),
            dayMinutes = listOf(30, 60, 0, 0, 0, 0, 0),
            importedDayMinutes = listOf(30, 60, 0, 0, 0, 0, 0),
        )

        assertTrue(result.isComplete)
    }

    @Test
    fun omittedTopicsAreReportedWithStableIdsAndNames() {
        val result = PlanCoverageValidator.validate(
            subjects,
            listOf(PlanCoverageTask("materia-1", "topico-1", monday, 30)),
            dayMinutes = listOf(120, 0, 0, 0, 0, 0, 0),
        )

        assertFalse(result.isComplete)
        assertEquals(
            listOf(MissingTopic("materia-1", "topico-2", "Direito Constitucional", "Organização do Estado")),
            result.missingTopics,
        )
        assertEquals(listOf("Informática"), result.missingSubjects)
    }

    @Test
    fun unknownTopicIdsDoNotCountAsCoverageAndMultipleMissingTopicsRemainOrdered() {
        val result = PlanCoverageValidator.validate(
            subjects,
            listOf(PlanCoverageTask("materia-1", "topico-desconhecido", monday, 30)),
            dayMinutes = listOf(120, 0, 0, 0, 0, 0, 0),
        )

        assertEquals(listOf("topico-1", "topico-2"), result.missingTopics.map { it.topicId })
        assertEquals(listOf("Informática"), result.missingSubjects)
    }

    @Test
    fun tasksMustMatchBothSubjectAndTopicToCoverATopic() {
        val result = PlanCoverageValidator.validate(
            subjects,
            listOf(PlanCoverageTask("materia-errada", "topico-1", monday, 30)),
            dayMinutes = listOf(120, 0, 0, 0, 0, 0, 0),
        )

        assertEquals(listOf("topico-1", "topico-2"), result.missingTopics.map { it.topicId })
    }

    @Test
    fun overCapacityAndAvailabilityMismatchAreReportedByDateAndWeekday() {
        val result = PlanCoverageValidator.validate(
            subjects,
            listOf(
                PlanCoverageTask("materia-1", "topico-1", monday, 45),
                PlanCoverageTask("materia-1", "topico-2", monday, 30),
                PlanCoverageTask("materia-2", null, monday, 45),
            ),
            dayMinutes = listOf(60, 0, 0, 0, 0, 0, 0),
            importedDayMinutes = listOf(120, 0, 0, 0, 0, 0, 0),
        )

        assertEquals(listOf(monday), result.overCapacityDates)
        assertEquals(listOf(1), result.availabilityMismatchDays)
    }
}
