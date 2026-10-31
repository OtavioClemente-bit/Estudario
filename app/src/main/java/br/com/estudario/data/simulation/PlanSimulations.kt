package br.com.estudario.data.simulation

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import br.com.estudario.EstudarioApplication
import br.com.estudario.data.local.planner.PlanTaskEntity
import br.com.estudario.domain.planner.PlanTaskStatus
import br.com.estudario.domain.planner.ReplanReason
import java.time.LocalDate

/**
 * O simulado do plano: a tarefa SIMULATION abre um simulado montado só com o banco e as questões que a
 * pessoa já fez (SimulationService.createForPlan, sem cota), em todos os planos, inclusive o Grátis.
 * A tarefa guarda o simulado dela (um só por tarefa) e é concluída quando ele é entregue. Não dá para
 * adiantar: o simulado é do dia marcado.
 */
object PlanSimulations {
    private const val PREFS = "simulado_do_plano"

    /** Pedido de navegação para a tela do simulado (a raiz do app observa e navega). */
    var openExam by mutableStateOf<Long?>(null)
    var openList by mutableStateOf(false)

    private fun prefs(context: Context) = context.applicationContext.getSharedPreferences(PREFS, Context.MODE_PRIVATE)

    fun simulationFor(context: Context, taskId: String): Long? =
        prefs(context).getLong("tarefa:$taskId", -1L).takeIf { it > 0 }

    fun taskFor(context: Context, simulationId: Long): String? =
        prefs(context).getString("simulado:$simulationId", null)

    /** Abre (ou monta) o simulado da tarefa. Devolve a mensagem para a pessoa, quando houver. */
    suspend fun open(app: EstudarioApplication, task: PlanTaskEntity): String? {
        val day = LocalDate.ofEpochDay(task.scheduledEpochDay)
        if (day.isAfter(LocalDate.now())) {
            return "Este simulado é de ${day.dayOfMonth}/${day.monthValue}. Ele não pode ser adiantado: faz parte do ritmo do plano."
        }
        val service = app.simulationService
        simulationFor(app, task.id)?.let { id ->
            val simulation = app.database.simulationDao().simulation(id)
            if (simulation != null) {
                if (simulation.status == "READY" || simulation.status == "PARTIAL" || simulation.status == "IN_PROGRESS") openExam = id else openList = true
                return null
            }
        }
        // O primeiro do plano é o diagnóstico; depois, só do que já foi estudado.
        val diagnostic = app.database.simulationDao().simulationsOnce().none { it.competitionId == task.competitionId && it.status == "FINISHED" }
        val id = runCatching { service.createForPlan(task.competitionId, task.plannedQuestions.coerceAtLeast(10), diagnostic) }
            .getOrElse { return it.message ?: "Não deu para montar o simulado agora. Tente de novo em instantes." }
        prefs(app).edit().putLong("tarefa:${task.id}", id).putString("simulado:$id", task.id).apply()
        openList = true
        return "Montando o seu simulado do plano: avisamos quando estiver pronto."
    }

    /** Simulado entregue: a tarefa do plano que o pediu fica concluída. */
    suspend fun finished(app: EstudarioApplication, simulationId: Long, minutes: Int, questions: Int, correct: Int) {
        val taskId = taskFor(app, simulationId) ?: return
        val task = app.planRepository.task(taskId) ?: return
        if (task.status == PlanTaskStatus.CONCLUIDA) return
        val now = System.currentTimeMillis()
        runCatching {
            app.executionService.complete(
                taskId,
                br.com.estudario.data.planner.CompleteTaskInput(now - minutes * 60_000L, now, minutes.coerceAtLeast(task.plannedMinutes), questions, correct.coerceIn(0, questions)),
            )
            app.planService.replan(task.planId, ReplanReason.TASK_COMPLETED)
        }
    }
}
