package br.com.estudario.ui.planner

import br.com.estudario.data.local.planner.*
import java.time.LocalDate
import br.com.estudario.domain.planner.MasterPlanAlert
import br.com.estudario.data.transfer.planner.ContextWeakTopic
import br.com.estudario.domain.planner.PlanTaskStatus

/** Loading bloqueante de uma ação do plano (criar, gerar, recalcular). */
data class PlanBusyState(val title: String, val message: String? = null)

enum class PlanSection(val label: String) { TODAY("Hoje"), WEEK("Semana"), MONTH("Mês"), YEAR("Visão geral") }

data class PlannerTaskUi(
    val entity: PlanTaskEntity,
    val actualMinutes: Int,
    val questionsDone: Int,
    val correctAnswers: Int,
)

/** Tarefas antigas continuam no histórico, mas não representam carga vigente após replanejamento. */
fun PlanTaskStatus.countsAsPlannedLoad(): Boolean = this in plannedLoadStatuses

fun Iterable<PlannerTaskUi>.plannedLoadTasks(): List<PlannerTaskUi> =
    filter { it.entity.status.countsAsPlannedLoad() }

fun Iterable<PlannerTaskUi>.plannedLoadMinutes(): Int =
    plannedLoadTasks().sumOf { it.entity.plannedMinutes.coerceAtLeast(0) }

fun Iterable<PlannerTaskUi>.plannedLoadCompletionPercent(): Int {
    val current = plannedLoadTasks()
    return if (current.isEmpty()) 0 else current.count { it.entity.status == PlanTaskStatus.CONCLUIDA } * 100 / current.size
}

private val plannedLoadStatuses = setOf(
    PlanTaskStatus.PLANEJADA,
    PlanTaskStatus.EM_ANDAMENTO,
    PlanTaskStatus.CONCLUIDA,
    PlanTaskStatus.NAO_REALIZADA,
)

data class ActivePlanUiState(
    val loading: Boolean = false,
    val selectedSection: PlanSection = PlanSection.TODAY,
    val activePlan: StudyPlanEntity? = null,
    val allPlans: List<StudyPlanEntity> = emptyList(),
    val tasks: List<PlannerTaskUi> = emptyList(),
    val availability: List<StudyAvailabilityEntity> = emptyList(),
    val planSubjects: List<PlanSubjectEntity> = emptyList(),
    val dayOverrides: List<StudyDayOverrideEntity> = emptyList(),
    val annualPhases: List<AnnualPhaseEntity> = emptyList(),
    val monthlyPlans: List<MonthlyPlanEntity> = emptyList(),
    val weeklyPlans: List<WeeklyPlanEntity> = emptyList(),
    val today: LocalDate = LocalDate.now(),
    val todayPlannedMinutes: Int = 0,
    val todayActualMinutes: Int = 0,
    val deficitMinutes: Int = 0,
    val forecastDate: LocalDate? = null,
    val masterAlerts: List<MasterPlanAlert> = emptyList(),
    val weakTopics: List<ContextWeakTopic> = emptyList(),
    /** Explicações do método que gerou as tarefas atuais. */
    val methodNotes: List<String> = emptyList(),
    val message: String? = null,
) {
    val todayTasks get() = tasks.filter { it.entity.scheduledEpochDay == today.toEpochDay() }
    val overdueTasks get() = tasks.filter { it.entity.scheduledEpochDay < today.toEpochDay() && it.entity.status in listOf(br.com.estudario.domain.planner.PlanTaskStatus.PLANEJADA, br.com.estudario.domain.planner.PlanTaskStatus.EM_ANDAMENTO) }
    val todayCompletionPercent: Int get() = todayTasks.plannedLoadCompletionPercent()
    val weekStart: LocalDate get() = today.minusDays((today.dayOfWeek.value - 1).toLong())
    val weekTasks get() = tasks.filter { LocalDate.ofEpochDay(it.entity.scheduledEpochDay) in weekStart..weekStart.plusDays(6) }
    val monthTasks get() = tasks.filter { LocalDate.ofEpochDay(it.entity.scheduledEpochDay).run { year == today.year && month == today.month } }
    val weekPlannedMinutes: Int get() = weekTasks.plannedLoadMinutes()
    val monthPlannedMinutes: Int get() = monthTasks.plannedLoadMinutes()
    val totalPlannedMinutes: Int get() = tasks.plannedLoadMinutes()
}

sealed interface PlanTransferUiState {
    data object Idle : PlanTransferUiState
    data object Loading : PlanTransferUiState
    data class Preview(val raw: String, val value: br.com.estudario.data.transfer.planner.StudyPlanImportPreview) : PlanTransferUiState
    data class Success(val message: String) : PlanTransferUiState
    data class Error(val message: String) : PlanTransferUiState
}

object StudyPlanUiMapper {
    fun map(
        plan: StudyPlanEntity?,
        tasks: List<PlanTaskEntity>,
        executions: List<StudyTaskExecutionEntity>,
        today: LocalDate,
        allPlans: List<StudyPlanEntity> = listOfNotNull(plan),
        availability: List<StudyAvailabilityEntity> = emptyList(),
        planSubjects: List<PlanSubjectEntity> = emptyList(),
        annual: List<AnnualPhaseEntity> = emptyList(),
        monthly: List<MonthlyPlanEntity> = emptyList(),
        weekly: List<WeeklyPlanEntity> = emptyList(),
        dayOverrides: List<StudyDayOverrideEntity> = emptyList(),
        deficitMinutes: Int = 0,
        section: PlanSection = PlanSection.TODAY,
    ): ActivePlanUiState {
        val executionByTask = executions.filter { it.taskId != null }.groupBy { it.taskId }
        val rows = tasks.map { task ->
            val done = executionByTask[task.id].orEmpty()
            PlannerTaskUi(task, done.sumOf { it.actualMinutes }, done.sumOf { it.questionsDone }, done.sumOf { it.correctAnswers })
        }
        val todayRows = rows.filter { it.entity.scheduledEpochDay == today.toEpochDay() }
        return ActivePlanUiState(
            selectedSection = section,
            activePlan = plan,
            allPlans = allPlans,
            tasks = rows,
            availability = availability,
            planSubjects = planSubjects,
            dayOverrides = dayOverrides,
            annualPhases = annual,
            monthlyPlans = monthly,
            weeklyPlans = weekly,
            today = today,
            todayPlannedMinutes = todayRows.plannedLoadMinutes(),
            todayActualMinutes = todayRows.sumOf { it.actualMinutes },
            deficitMinutes = deficitMinutes,
        )
    }
}
