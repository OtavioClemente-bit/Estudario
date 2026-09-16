package br.com.meuconcurso.domain.planner

import java.time.LocalDate

object StudyPlanForecastCalculator {
    fun forecast(
        start: LocalDate,
        remainingMinutes: Int,
        weeklyCapacityMinutes: Int,
        recurringReservedMinutes: Int,
    ): ForecastResult {
        require(remainingMinutes >= 0)
        require(weeklyCapacityMinutes >= 0)
        require(recurringReservedMinutes >= 0)

        val usable = (weeklyCapacityMinutes - recurringReservedMinutes).coerceAtLeast(0)
        if (remainingMinutes == 0) {
            return ForecastResult(start, usable, 0, ForecastUnavailableReason.NO_REMAINING_DEMAND)
        }
        if (usable == 0) {
            return ForecastResult(null, 0, remainingMinutes, ForecastUnavailableReason.NO_USABLE_CAPACITY)
        }
        val weeks = (remainingMinutes + usable - 1) / usable
        return ForecastResult(start.plusWeeks(weeks.toLong()), usable, remainingMinutes)
    }
}
