package br.com.estudario.domain.planner

import java.time.DayOfWeek
import java.time.LocalDate

enum class PlanPriority { CRITICAL, HIGH, MEDIUM, LOW }
enum class PlanTaskType { THEORY, QUESTIONS, REVIEW, ACTIVE_RECALL, FLASHCARDS, SIMULATION, DISCURSIVE }
enum class PlanTaskStatus { PLANEJADA, EM_ANDAMENTO, CONCLUIDA, REPROGRAMADA, NAO_REALIZADA, PAUSADA }
enum class PlanOrigin { ENGINE, MANUAL, IMPORTED, REVIEW_SCHEDULE, MASTER_PLAN }
enum class ReplanReason { INITIAL, AVAILABILITY_CHANGED, DAY_MISSED, TASK_PARTIAL, TASK_COMPLETED, TASK_SKIPPED, SUBJECT_CHANGED, SYLLABUS_CHANGED, IMPORTED, PLAN_ACTIVATED }
enum class ForecastUnavailableReason { NO_USABLE_CAPACITY, NO_REMAINING_DEMAND }

data class DailyCapacity(
    val dayOfWeek: DayOfWeek,
    val minutes: Int,
    val unavailable: Boolean = false,
) {
    init { require(minutes >= 0) { "Daily capacity cannot be negative." } }
    val effectiveMinutes: Int get() = if (unavailable) 0 else minutes
}

data class DayCapacityOverride(
    val date: LocalDate,
    val minutes: Int? = null,
    val unavailable: Boolean = false,
    val locked: Boolean = false,
) {
    init { require(minutes == null || minutes >= 0) { "Day override capacity cannot be negative." } }
}

data class TaskExecution(
    val id: String,
    val taskId: String?,
    val subjectId: Long?,
    val topicId: Long?,
    val date: LocalDate,
    val minutes: Int,
    val questions: Int,
    val correct: Int,
) {
    init {
        require(minutes >= 0) { "Execution minutes cannot be negative." }
        require(questions >= 0) { "Execution question count cannot be negative." }
        require(correct in 0..questions) { "Correct answers must be between zero and the question count." }
    }
}

data class PlannerTask(
    val id: String,
    val planId: String,
    val subjectId: Long?,
    val topicId: Long?,
    val date: LocalDate,
    val type: PlanTaskType,
    val plannedMinutes: Int,
    val plannedQuestions: Int,
    val priority: PlanPriority,
    val status: PlanTaskStatus,
    val locked: Boolean,
    val origin: PlanOrigin = PlanOrigin.ENGINE,
    val deadline: LocalDate? = null,
    val subjectPosition: Int = Int.MAX_VALUE,
    val topicPosition: Int = Int.MAX_VALUE,
    val replannedFromTaskId: String? = null,
) {
    init {
        require(plannedMinutes >= 0) { "Planned minutes cannot be negative." }
        require(plannedQuestions >= 0) { "Planned question count cannot be negative." }
    }
}

data class SubjectDemand(
    val subjectId: Long,
    val name: String,
    val priority: PlanPriority,
    val minimumMaintenanceMinutes: Int = 0,
    val position: Int = Int.MAX_VALUE,
    val paused: Boolean = false,
) {
    init { require(minimumMaintenanceMinutes >= 0) { "Maintenance minutes cannot be negative." } }
}

data class TaskDemand(
    val id: String,
    val subjectId: Long,
    val topicId: Long?,
    val type: PlanTaskType,
    val minutes: Int,
    val questions: Int = 0,
    val priority: PlanPriority,
    val deadline: LocalDate? = null,
    val subjectPosition: Int = Int.MAX_VALUE,
    val topicPosition: Int = Int.MAX_VALUE,
    val dependsOnTaskIds: Set<String> = emptySet(),
    val splittable: Boolean = true,
    val replannedFromTaskId: String? = null,
    /**
     * Posição decidida por quem montou a demanda. Quando vem preenchida, ela manda na ordem do
     * calendário, é assim que o plano sem IA garante o rodízio de matérias que calculou.
     * O padrão deixa a ordenação por pontuação valendo.
     */
    val order: Int = Int.MAX_VALUE,
    /** Data a partir da qual a tarefa pode ser marcada (simulado no sábado, por exemplo). */
    val anchorDate: LocalDate? = null,
) {
    init {
        require(minutes >= 0) { "Demand minutes cannot be negative." }
        require(questions >= 0) { "Demand question count cannot be negative." }
    }
}

data class TopicPerformance(
    val topicId: Long,
    val answered: Int,
    val correct: Int,
    val lastStudiedDate: LocalDate? = null,
) {
    init {
        require(answered >= 0) { "Answered count cannot be negative." }
        require(correct in 0..answered) { "Correct count must be between zero and answered." }
    }
}

data class ReviewDemand(
    val id: String,
    val subjectId: Long,
    val topicId: Long,
    val dueDate: LocalDate,
    val minutes: Int,
    val plannedQuestions: Int = 0,
) {
    init {
        require(minutes >= 0) { "Review minutes cannot be negative." }
        require(plannedQuestions >= 0) { "Review question count cannot be negative." }
    }
}

data class PlannerPolicy(
    val version: Int = 1,
    val horizonDays: Int = 28,
    val minimumBlockMinutes: Int = 15,
    /** Tamanho de bloco que o alocador tenta usar em cada pedaço de tarefa. */
    val preferredBlockMinutes: Int = 60,
    val maxFlexibleSharePercent: Int = 40,
    /** Teto de um dia que uma mesma matéria pode ocupar (100 = sem teto). */
    val dailySubjectSharePercent: Int = 100,
    val weaknessThresholdPercent: Int = 65,
    val weaknessMinimumSample: Int = 10,
    val weaknessWeeklyCapMinutes: Int = 90,
) {
    init {
        require(version > 0)
        require(horizonDays > 0)
        require(minimumBlockMinutes > 0)
        require(preferredBlockMinutes >= minimumBlockMinutes)
        require(maxFlexibleSharePercent in 1..100)
        require(dailySubjectSharePercent in 1..100)
        require(weaknessThresholdPercent in 0..100)
        require(weaknessMinimumSample >= 0)
        require(weaknessWeeklyCapMinutes >= 0)
    }
}

data class StudyPlannerSnapshot(
    val planId: String,
    val revision: Long,
    val today: LocalDate,
    val availability: Map<DayOfWeek, DailyCapacity>,
    val dayOverrides: Map<LocalDate, DayCapacityOverride> = emptyMap(),
    val subjects: List<SubjectDemand>,
    val demands: List<TaskDemand>,
    val reviews: List<ReviewDemand> = emptyList(),
    val tasks: List<PlannerTask> = emptyList(),
    val executions: List<TaskExecution> = emptyList(),
    val topicPerformance: List<TopicPerformance> = emptyList(),
    val completedDependencyIds: Set<String> = emptySet(),
    val remainingPhaseMinutes: Int = 0,
    val recurringReservedMinutes: Int = 0,
    val policy: PlannerPolicy = PlannerPolicy(),
    /** Explicações do método que montou as demandas; viram o "por que este plano" na tela. */
    val notes: List<String> = emptyList(),
) {
    init {
        require(revision >= 0)
        require(remainingPhaseMinutes >= 0)
        require(recurringReservedMinutes >= 0)
    }
}

data class TaskTransition(
    val taskId: String,
    val from: PlanTaskStatus,
    val to: PlanTaskStatus,
    val reason: String,
)

data class UnallocatedDemand(val demandId: String, val remainingMinutes: Int, val reason: String)

data class CapacityReport(
    val requiredMinutes: Int,
    val availableMinutes: Int,
    val deficitMinutes: Int,
    val availableByDate: Map<LocalDate, Int>,
    val unallocated: List<UnallocatedDemand>,
)

data class PlanningExplanation(val code: String, val message: String)

data class ForecastResult(
    val estimatedDate: LocalDate?,
    val usableWeeklyMinutes: Int,
    val remainingMinutes: Int,
    val unavailableReason: ForecastUnavailableReason? = null,
)

data class PlanningProposal(
    val id: String,
    val planId: String,
    val baseRevision: Long,
    val preservedTaskIds: Set<String>,
    val transitions: List<TaskTransition>,
    val newTasks: List<PlannerTask>,
    val capacity: CapacityReport,
    val explanations: List<PlanningExplanation>,
    val forecast: ForecastResult,
)

data class PlanMetrics(
    val plannedMinutes: Int,
    val actualMinutes: Int,
    val adherencePercent: Int,
    val questions: Int,
    val correct: Int,
    val accuracyPercent: Int?,
    val syllabusCoveragePercent: Int,
    val overtimeMinutes: Int,
)

data class MasterSubject(val id: Long, val name: String, val priority: PlanPriority)

data class MasterPlanAlert(
    val subjectId: Long,
    val subjectName: String,
    val inactiveDays: Long,
    val lastExecutionDate: LocalDate?,
    val blocking: Boolean = false,
)
