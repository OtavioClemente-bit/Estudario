package br.com.estudario.domain.planner

import java.time.LocalDate
import java.time.temporal.ChronoUnit

data class PlannerScoringPolicy(
    val criticalWeight: Int = 400,
    val highWeight: Int = 300,
    val mediumWeight: Int = 200,
    val lowWeight: Int = 100,
    val overdueReviewWeight: Int = 250,
    val weakTopicWeight: Int = 120,
    val inactivityDayWeight: Int = 2,
) {
    fun score(
        demand: TaskDemand,
        today: LocalDate,
        performance: TopicPerformance?,
        policy: PlannerPolicy,
    ): Int {
        val strategic = when (demand.priority) {
            PlanPriority.CRITICAL -> criticalWeight
            PlanPriority.HIGH -> highWeight
            PlanPriority.MEDIUM -> mediumWeight
            PlanPriority.LOW -> lowWeight
        }
        val deadlineBonus = demand.deadline?.let { deadline ->
            val days = ChronoUnit.DAYS.between(today, deadline).coerceAtLeast(0)
            (90 - days).coerceAtLeast(0).toInt()
        } ?: 0
        val weakBonus = performance?.takeIf {
            it.answered >= policy.weaknessMinimumSample &&
                it.correct * 100 / it.answered.coerceAtLeast(1) < policy.weaknessThresholdPercent
        }?.let { weakTopicWeight } ?: 0
        val inactivityBonus = performance?.lastStudiedDate?.let {
            ChronoUnit.DAYS.between(it, today).coerceAtLeast(0).toInt() * inactivityDayWeight
        } ?: 0
        return strategic + deadlineBonus + weakBonus + inactivityBonus
    }
}
