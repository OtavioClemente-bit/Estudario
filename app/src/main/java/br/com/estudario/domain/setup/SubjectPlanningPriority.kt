package br.com.estudario.domain.setup

import br.com.estudario.domain.PriorityLevel
import br.com.estudario.domain.planner.PlanPriority

fun SubjectDifficulty.toPlanningPriority(): PlanPriority = when (this) {
    SubjectDifficulty.EASY -> PlanPriority.LOW
    SubjectDifficulty.MEDIUM -> PlanPriority.MEDIUM
    SubjectDifficulty.HARD -> PlanPriority.HIGH
}

fun PlanPriority.planningRank(): Int = when (this) {
    PlanPriority.LOW -> 1
    PlanPriority.MEDIUM -> 2
    PlanPriority.HIGH -> 3
    PlanPriority.CRITICAL -> 4
}

fun effectivePriority(official: PlanPriority, difficulty: SubjectDifficulty): PlanPriority =
    if (official.planningRank() >= difficulty.toPlanningPriority().planningRank()) official else difficulty.toPlanningPriority()

fun PriorityLevel.toPlanPriority(): PlanPriority = when (this) {
    PriorityLevel.VERY_HIGH -> PlanPriority.CRITICAL
    PriorityLevel.HIGH -> PlanPriority.HIGH
    PriorityLevel.MEDIUM -> PlanPriority.MEDIUM
    PriorityLevel.LOW, PriorityLevel.VERY_LOW -> PlanPriority.LOW
}
