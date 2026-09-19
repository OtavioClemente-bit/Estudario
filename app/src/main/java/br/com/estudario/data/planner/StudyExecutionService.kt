package br.com.estudario.data.planner

import androidx.room.withTransaction
import br.com.estudario.data.local.AppDatabase
import br.com.estudario.data.local.planner.PerceivedDifficulty
import br.com.estudario.data.local.planner.StudyPlanRevisionEntity
import br.com.estudario.data.local.planner.StudyTaskExecutionEntity
import br.com.estudario.domain.planner.PlanTaskStatus
import java.time.LocalDate
import java.util.UUID
import br.com.estudario.domain.planner.*

data class CompleteTaskInput(
    val startedAt: Long,
    val completedAt: Long = System.currentTimeMillis(),
    val actualMinutes: Int,
    val questions: Int = 0,
    val correct: Int = 0,
    val notes: String = "",
    val perceivedDifficulty: PerceivedDifficulty = PerceivedDifficulty.NORMAL,
)

class StudyExecutionService(
    private val db: AppDatabase,
    private val planService: StudyPlanApplicationService,
) {
    private val planner = db.plannerDao()

    suspend fun start(taskId: String, startedAt: Long = System.currentTimeMillis()) = db.withTransaction {
        val task = planner.task(taskId) ?: error("Tarefa não encontrada.")
        require(task.status == PlanTaskStatus.PLANEJADA) { "Somente tarefas planejadas podem ser iniciadas." }
        val plan = planner.plan(task.planId) ?: error("Plano não encontrado.")
        require(planner.claimRevision(plan.id, plan.revision, startedAt) == 1)
        planner.updateTask(task.withStatus(PlanTaskStatus.EM_ANDAMENTO, plan.revision + 1))
        planner.insertRevision(StudyPlanRevisionEntity(plan.id, plan.revision + 1, plan.revision, "TASK_STARTED", summary = task.id))
    }

    suspend fun complete(taskId: String, input: CompleteTaskInput): String = db.withTransaction {
        require(input.actualMinutes >= 0) { "O tempo realizado não pode ser negativo." }
        require(input.questions >= 0) { "A quantidade de questões não pode ser negativa." }
        require(input.correct in 0..input.questions) { "Os acertos devem estar entre zero e o total de questões." }
        require(input.completedAt >= input.startedAt) { "O término deve ocorrer depois do início." }
        val task = planner.task(taskId) ?: error("Tarefa não encontrada.")
        require(task.status !in setOf(PlanTaskStatus.CONCLUIDA, PlanTaskStatus.NAO_REALIZADA, PlanTaskStatus.PAUSADA)) { "A tarefa não aceita nova conclusão." }
        val plan = planner.plan(task.planId) ?: error("Plano não encontrado.")
        require(planner.claimRevision(plan.id, plan.revision, input.completedAt) == 1)
        val executionId = UUID.randomUUID().toString()
        planner.insertExecution(
            StudyTaskExecutionEntity(
                id = executionId,
                planId = task.planId,
                taskId = task.id,
                competitionId = task.competitionId,
                subjectId = task.subjectId,
                topicId = task.topicId,
                startedAt = input.startedAt,
                completedAt = input.completedAt,
                actualMinutes = input.actualMinutes,
                questionsDone = input.questions,
                correctAnswers = input.correct,
                notes = input.notes.trim(),
                perceivedDifficulty = input.perceivedDifficulty,
            ),
        )
        val nextStatus = if (input.actualMinutes < task.plannedMinutes) PlanTaskStatus.EM_ANDAMENTO else PlanTaskStatus.CONCLUIDA
        planner.updateTask(task.withStatus(nextStatus, plan.revision + 1))
        planner.insertRevision(StudyPlanRevisionEntity(plan.id, plan.revision + 1, plan.revision, if (nextStatus == PlanTaskStatus.CONCLUIDA) "TASK_COMPLETED" else "TASK_PARTIAL", summary = task.id))
        executionId
    }

    suspend fun skip(taskId: String, reason: String) = db.withTransaction {
        val task = planner.task(taskId) ?: error("Tarefa não encontrada.")
        require(!task.locked) { "Tarefa bloqueada não pode ser pulada." }
        val plan = planner.plan(task.planId) ?: error("Plano não encontrado.")
        require(planner.claimRevision(plan.id, plan.revision, System.currentTimeMillis()) == 1)
        planner.updateTask(task.copy(notes = reason.trim()).withStatus(PlanTaskStatus.NAO_REALIZADA, plan.revision + 1))
        planner.insertRevision(StudyPlanRevisionEntity(plan.id, plan.revision + 1, plan.revision, "TASK_SKIPPED", summary = task.id))
    }

    suspend fun reprogram(taskId: String, targetDate: LocalDate?): PlanningProposal {
        val task = planner.task(taskId) ?: error("Tarefa não encontrada.")
        require(!task.locked) { "Tarefa bloqueada não pode ser reprogramada." }
        if (targetDate != null) {
            return db.withTransaction {
                val plan = planner.plan(task.planId) ?: error("Plano não encontrado.")
                require(planner.claimRevision(plan.id, plan.revision, System.currentTimeMillis()) == 1)
                planner.updateTask(task.copy(scheduledEpochDay = targetDate.toEpochDay(), updatedRevision = plan.revision + 1, updatedAt = System.currentTimeMillis()))
                planner.insertRevision(StudyPlanRevisionEntity(plan.id, plan.revision + 1, plan.revision, "TASK_REPROGRAMMED", summary = task.id))
                PlanningProposal(
                    id = "manual:${task.id}:${plan.revision + 1}", planId = plan.id, baseRevision = plan.revision + 1,
                    preservedTaskIds = setOf(task.id), transitions = emptyList(), newTasks = emptyList(),
                    capacity = CapacityReport(0, 0, 0, emptyMap(), emptyList()), explanations = listOf(PlanningExplanation("MANUAL_DATE", "Data escolhida pelo usuário preservada.")),
                    forecast = ForecastResult(null, 0, 0, ForecastUnavailableReason.NO_REMAINING_DEMAND),
                )
            }
        }
        return planService.replan(task.planId, ReplanReason.TASK_PARTIAL)
    }
}
