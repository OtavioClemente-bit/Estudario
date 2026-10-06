package br.com.estudario.domain.planner

import kotlinx.datetime.LocalDate
import br.com.estudario.time.*

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
                val inactiveDays = lastExecution?.let { daysBetween(it, today).coerceAtLeast(0) }
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
