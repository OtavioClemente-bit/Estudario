package br.com.estudario.domain.planner

import java.time.LocalDate
import java.time.temporal.ChronoUnit

object StudyPaceEvaluator {
    const val COMFORTABLE_MARGIN_DAYS = 14L

    sealed interface Pace {
        data object Unknown : Pace
        data class NoExamDate(val forecast: LocalDate) : Pace
        data class Comfortable(val forecast: LocalDate, val daysBeforeExam: Long) : Pace
        data class Tight(val forecast: LocalDate, val daysBeforeExam: Long) : Pace
        data class Behind(val forecast: LocalDate, val daysAfterExam: Long) : Pace
        data object Complete : Pace
    }

    fun evaluate(today: LocalDate, forecast: LocalDate?, examDate: LocalDate?, hasRemainingWork: Boolean): Pace {
        if (!hasRemainingWork) return Pace.Complete
        if (forecast == null) return Pace.Unknown
        val effectiveForecast = if (forecast.isBefore(today)) today else forecast
        if (examDate == null) return Pace.NoExamDate(effectiveForecast)
        val days = ChronoUnit.DAYS.between(effectiveForecast, examDate)
        return when {
            days < 0 -> Pace.Behind(effectiveForecast, -days)
            days >= COMFORTABLE_MARGIN_DAYS -> Pace.Comfortable(effectiveForecast, days)
            else -> Pace.Tight(effectiveForecast, days)
        }
    }
}
