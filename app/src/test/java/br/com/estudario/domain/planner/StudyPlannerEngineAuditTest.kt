package br.com.estudario.domain.planner

import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import br.com.estudario.time.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Test

/**
 * Auditoria do motor do plano: simula semanas de estudo de verdade (abrir o app, concluir tarefa por
 * tarefa, replanejar a cada conclusão, faltar alguns dias) e confere as regras que a pessoa sente:
 * nada de caco de poucos minutos, nenhum dia além da disponibilidade, concluir nunca enche o dia de
 * novo, e no fim todo o conteúdo prometido foi coberto.
 */
class StudyPlannerEngineAuditTest {
    private val engine = StudyPlannerEngine()
    private val start = LocalDate(2026, 10, 12) // segunda
    private val policy = PlannerPolicy(horizonDays = 28, minimumBlockMinutes = 25, preferredBlockMinutes = 50, maxFlexibleSharePercent = 100, dailySubjectSharePercent = 100)

    // Disponibilidade irregular, como a de quem trabalha: 98 min na semana, 3 h no sábado, domingo livre.
    private val capacity = mapOf(
        DayOfWeek.MONDAY to 98, DayOfWeek.TUESDAY to 98, DayOfWeek.WEDNESDAY to 98, DayOfWeek.THURSDAY to 98,
        DayOfWeek.FRIDAY to 98, DayOfWeek.SATURDAY to 180, DayOfWeek.SUNDAY to 0,
    )

    private data class Work(val subjectId: Long, val topicId: Long, val type: PlanTaskType, val minutes: Int, val questions: Int)

    // 6 matérias × 8 tópicos: teoria de 50 min + questões de 22 min (11 questões) por tópico.
    private val work = (1L..6L).flatMap { subject ->
        (1L..8L).flatMap { topic ->
            val topicId = subject * 100 + topic
            listOf(Work(subject, topicId, PlanTaskType.THEORY, 50, 0), Work(subject, topicId, PlanTaskType.QUESTIONS, 22, 11))
        }
    }

    @Test
    fun `semanas de estudo seguem as regras e cobrem todo o conteudo`() = simulate(missEvery = 0)

    @Test
    fun `faltando um dia por semana o plano se reorganiza sem cacos e ainda cobre tudo`() = simulate(missEvery = 7)

    private fun simulate(missEvery: Int) {
        val tasks = mutableListOf<PlannerTask>()
        val executions = mutableListOf<TaskExecution>()
        var revision = 0L
        val total = work.sumOf { it.minutes }

        fun demands(): List<TaskDemand> = work.mapNotNull { w ->
            val covered = tasks.filter { it.topicId == w.topicId && it.type == w.type && it.status in setOf(PlanTaskStatus.PLANEJADA, PlanTaskStatus.EM_ANDAMENTO, PlanTaskStatus.CONCLUIDA) }
                .sumOf { it.plannedMinutes }
            val left = w.minutes - covered
            if (left <= 0) null
            else TaskDemand(
                id = "d:${w.topicId}:${w.type}", subjectId = w.subjectId, topicId = w.topicId, type = w.type,
                minutes = left, questions = if (w.questions == 0) 0 else w.questions * left / w.minutes,
                priority = PlanPriority.HIGH, subjectPosition = w.subjectId.toInt(), topicPosition = (w.topicId % 100).toInt(),
            )
        }

        fun replan(today: LocalDate, reason: ReplanReason): PlanningProposal {
            val snapshot = StudyPlannerSnapshot(
                planId = "plan", revision = revision, today = today,
                availability = capacity.mapValues { (day, minutes) -> DailyCapacity(day, minutes) },
                subjects = (1L..6L).map { SubjectDemand(it, "Matéria $it", PlanPriority.HIGH, position = it.toInt()) },
                demands = demands(), tasks = tasks.toList(), executions = executions.toList(),
                remainingPhaseMinutes = (total - executions.sumOf { it.minutes }).coerceAtLeast(0),
                policy = policy,
            )
            val proposal = engine.plan(snapshot, reason)
            proposal.transitions.forEach { t -> tasks.replaceAll { if (it.id == t.taskId) it.copy(status = t.to) else it } }
            tasks += proposal.newTasks
            revision++
            proposal.newTasks.forEach { task ->
                val demandTotal = work.first { it.topicId == task.topicId && it.type == task.type }.minutes
                if (task.plannedMinutes < policy.minimumBlockMinutes && demandTotal >= policy.minimumBlockMinutes) {
                    fail("Caco: ${task.type} do tópico ${task.topicId} com ${task.plannedMinutes} min em ${task.date} ($reason)")
                }
            }
            return proposal
        }

        replan(start, ReplanReason.INITIAL)
        var day = start
        repeat(90) { index ->
            val missed = missEvery > 0 && index % missEvery == 3
            if (index > 0) replan(day, ReplanReason.DAY_MISSED)
            val cap = capacity.getValue(day.dayOfWeek)
            val todays = tasks.filter { it.date == day && it.status == PlanTaskStatus.PLANEJADA }
            assertTrue(
                "Dia $day com ${todays.sumOf { it.plannedMinutes }} min para $cap min disponíveis",
                todays.sumOf { it.plannedMinutes } <= cap + policy.minimumBlockMinutes,
            )
            if (!missed) {
                // Sempre a tarefa que está na tela agora (o replanejamento troca as do dia por novas).
                var guard = 0
                while (guard++ < 30) {
                    val task = tasks.firstOrNull { it.date == day && it.status == PlanTaskStatus.PLANEJADA } ?: break
                    val before = tasks.filter { it.date == day && it.status == PlanTaskStatus.PLANEJADA }.map { it.id }.toSet()
                    tasks.replaceAll { if (it.id == task.id) it.copy(status = PlanTaskStatus.CONCLUIDA) else it }
                    executions += TaskExecution("e-${task.id}", task.id, task.subjectId!!, task.topicId, day, task.plannedMinutes, task.plannedQuestions, 0)
                    val proposal = replan(day, ReplanReason.TASK_COMPLETED)
                    val added = proposal.newTasks.filter { it.date == day && it.replannedFromTaskId !in before }
                    assertTrue("Concluir em $day trouxe tarefa nova para o dia: $added", added.isEmpty())
                }
                assertTrue("Dia $day não terminou: o plano seguiu trazendo tarefas", guard < 30)
            }
            day = day.plusDays(1)
        }

        // Cobertura: cada tópico estudado por inteiro, sem sobrar nem passar do prometido.
        val done = executions.groupBy { it.topicId to tasks.first { t -> t.id == it.taskId }.type }.mapValues { (_, list) -> list.sumOf { it.minutes } }
        work.forEach { w ->
            val history = tasks.filter { it.topicId == w.topicId && it.type == w.type }.joinToString { "${it.date}:${it.plannedMinutes}:${it.status}:${it.id.take(6)}<-${it.replannedFromTaskId?.take(6)}" }
            assertEquals("Cobertura de ${w.type} do tópico ${w.topicId} | $history", w.minutes, done[w.topicId to w.type] ?: 0)
        }
    }
}
