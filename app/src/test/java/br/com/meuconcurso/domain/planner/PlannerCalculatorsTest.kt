package br.com.meuconcurso.domain.planner

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import java.time.DayOfWeek
import java.time.LocalDate

class PlannerCalculatorsTest {
    @Test
    fun `weekly capacity sums only available liquid minutes`() {
        val availability = DayOfWeek.entries.associateWith { day ->
            DailyCapacity(
                dayOfWeek = day,
                minutes = if (day == DayOfWeek.SUNDAY) 0 else 120,
                unavailable = day == DayOfWeek.SUNDAY,
            )
        }

        assertEquals(720, availability.values.sumOf { it.effectiveMinutes })
    }

    @Test
    fun `progress keeps schedule adherence separate from syllabus coverage`() {
        val metrics = StudyPlanProgressCalculator.calculate(
            plannedMinutes = 240,
            completedPlannedMinutes = 120,
            actualMinutes = 180,
            questions = 20,
            correct = 15,
            syllabusCoveragePercent = 30,
        )

        assertEquals(50, metrics.adherencePercent)
        assertEquals(75, metrics.accuracyPercent)
        assertEquals(30, metrics.syllabusCoveragePercent)
        assertEquals(60, metrics.overtimeMinutes)
    }

    @Test
    fun `forecast uses usable capacity after recurring reservations`() {
        val result = StudyPlanForecastCalculator.forecast(
            start = LocalDate.of(2026, 9, 15),
            remainingMinutes = 1_920,
            weeklyCapacityMinutes = 1_200,
            recurringReservedMinutes = 240,
        )

        assertEquals(LocalDate.of(2026, 9, 29), result.estimatedDate)
        assertEquals(960, result.usableWeeklyMinutes)
        assertNull(result.unavailableReason)
    }

    @Test
    fun `forecast is unavailable when usable capacity is zero`() {
        val result = StudyPlanForecastCalculator.forecast(
            start = LocalDate.of(2026, 9, 15),
            remainingMinutes = 600,
            weeklyCapacityMinutes = 300,
            recurringReservedMinutes = 300,
        )

        assertNull(result.estimatedDate)
        assertEquals(ForecastUnavailableReason.NO_USABLE_CAPACITY, result.unavailableReason)
    }
}
