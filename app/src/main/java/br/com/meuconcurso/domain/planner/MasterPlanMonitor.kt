package br.com.meuconcurso.domain.planner

import java.time.LocalDate
import java.time.temporal.ChronoUnit

object MasterPlanMonitor {
    fun evaluate(
        today: LocalDate,
        inactivityThresholdDays: Int,
        subjects: List<MasterSubject>,
        lastExecutionBySubject: Map<Long, LocalDate>,
    ): List<MasterPlanAlert> {
        require(inactivityThresholdDays >= 0)
        return subjects.asSequence()
            .filter { it.priority == PlanPriority.CRITICAL || it.priority == PlanPriority.HIGH }
            .mapNotNull { subject ->
                val lastExecution = lastExecutionBySubject[subject.id]
                val inactiveDays = lastExecution?.let { ChronoUnit.DAYS.between(it, today).coerceAtLeast(0) }
                    ?: inactivityThresholdDays.toLong()
                inactiveDays.takeIf { it >= inactivityThresholdDays }?.let {
                    MasterPlanAlert(
                        subjectId = subject.id,
                        subjectName = subject.name,
                        inactiveDays = it,
                        lastExecutionDate = lastExecution,
                    )
                }
            }
            .sortedWith(compareByDescending<MasterPlanAlert> { it.inactiveDays }.thenBy { it.subjectName })
            .toList()
    }
}
