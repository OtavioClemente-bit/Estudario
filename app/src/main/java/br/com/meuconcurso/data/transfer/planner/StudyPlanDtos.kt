package br.com.meuconcurso.data.transfer.planner

import br.com.meuconcurso.data.local.planner.AvailabilityMode
import br.com.meuconcurso.domain.planner.PlanOrigin
import br.com.meuconcurso.domain.planner.PlanPriority
import br.com.meuconcurso.domain.planner.PlanTaskStatus
import br.com.meuconcurso.domain.planner.PlanTaskType
import java.time.LocalDate

data class PlanCompetitionDto(val externalId: String, val name: String)
data class PlanDayDto(val day: Int, val minutes: Int, val unavailable: Boolean)
data class PlanConfigurationDto(
    val mode: AvailabilityMode,
    val days: List<PlanDayDto>,
    val weeklyQuestions: Int,
    val monthlyDiscursives: Int,
)
data class PlanSubjectDto(
    val externalId: String,
    val name: String,
    val priority: PlanPriority,
    val maintenanceMinutes: Int,
    val paused: Boolean,
    val position: Int,
)
data class AnnualPhaseDto(
    val id: String,
    val name: String,
    val objective: String,
    val completionCriteria: String,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val targetMinutes: Int,
    val targetQuestions: Int,
    val targetDiscursives: Int,
    val targetPercent: Int,
)
data class MonthlyPlanDto(
    val id: String,
    val yearMonth: String,
    val focus: String,
    val targetMinutes: Int,
    val targetQuestions: Int,
    val targetDiscursives: Int,
    val targetPercent: Int,
)
data class WeeklyPlanDto(
    val id: String,
    val weekStart: LocalDate,
    val objective: String,
    val targetMinutes: Int,
    val targetQuestions: Int,
    val targetDiscursives: Int,
)
data class PlanTaskDto(
    val id: String,
    val subjectExternalId: String?,
    val topicExternalId: String?,
    val subjectName: String?,
    val topicName: String?,
    val date: LocalDate,
    val type: PlanTaskType,
    val minutes: Int,
    val questions: Int,
    val priority: PlanPriority,
    val status: PlanTaskStatus,
    val origin: PlanOrigin,
    val locked: Boolean,
    val notes: String,
    val dependencies: List<String>,
)
data class StudyPlanFileV1(
    val planId: String,
    val competition: PlanCompetitionDto,
    val name: String,
    val objective: String,
    val active: Boolean,
    val masterPlan: Boolean,
    val startDate: LocalDate,
    val examDate: LocalDate?,
    val baseRevision: Long?,
    val configuration: PlanConfigurationDto,
    val subjects: List<PlanSubjectDto>,
    val annualPhases: List<AnnualPhaseDto>,
    val monthlyPlans: List<MonthlyPlanDto>,
    val weeklyPlans: List<WeeklyPlanDto>,
    val tasks: List<PlanTaskDto>,
    val metadata: Map<String, String>,
)
