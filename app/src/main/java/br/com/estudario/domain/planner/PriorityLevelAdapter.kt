package br.com.estudario.domain.planner

import br.com.estudario.domain.PriorityLevel

/** Adapts the explainable five-level study priority to the planner's four buckets. */
object PriorityLevelAdapter {
    fun toPlanPriority(level: PriorityLevel): PlanPriority = when (level) {
        PriorityLevel.VERY_HIGH -> PlanPriority.CRITICAL
        PriorityLevel.HIGH -> PlanPriority.HIGH
        PriorityLevel.MEDIUM -> PlanPriority.MEDIUM
        PriorityLevel.LOW, PriorityLevel.VERY_LOW -> PlanPriority.LOW
    }
}
