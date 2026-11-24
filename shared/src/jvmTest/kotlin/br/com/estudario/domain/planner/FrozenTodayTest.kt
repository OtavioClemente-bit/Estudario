package br.com.estudario.domain.planner

import br.com.estudario.time.*
import kotlinx.datetime.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Hoje não muda sozinho: toda reorganização automática (abrir o app, virar o dia, depois de sincronizar, a
 * acomodação do plano longo, o método novo) mantém o que falta de hoje exatamente como está (mesmas tarefas,
 * minutos e ordem). Caso real de 24/11: depois da semente, um reinício trocou "4 atividades · 1h42" por "6 · 2h".
 */
class FrozenTodayTest {
    private val today = LocalDate(2026, 11, 24)

    private data class Day(val id: String, val minutes: Int, val questions: Int, val sequence: Int)

    private fun todayOf(tasks: List<PlannerTask>) = tasks.filter { it.date == today && it.status in setOf(PlanTaskStatus.PLANEJADA, PlanTaskStatus.EM_ANDAMENTO) }
        .sortedWith(compareBy({ it.sequence }, { it.id })).map { Day(it.id, it.plannedMinutes, it.plannedQuestions, it.sequence) }

    private fun apply(tasks: List<PlannerTask>, proposal: PlanningProposal): List<PlannerTask> {
        val moved = proposal.transitions.associateBy { it.taskId }
        return tasks.map { t -> moved[t.id]?.let { t.copy(status = it.to) } ?: t } + proposal.newTasks
    }

    private fun automatic(input: PlannerSnapshotBuilder.Input, reason: ReplanReason): PlanningProposal {
        val snap = PlannerSnapshotBuilder.build(input).let { it.copy(policy = it.policy.copy(freezeToday = true)) }
        return PlanStability.stabilize(StudyPlannerEngine().plan(snap, reason), snap.tasks)
    }

    @Test fun reorganizacoesAutomaticasNaoMexemEmHoje() {
        for (id in listOf("bb-2022--agente-comercial", "pmmg-cfsd-qppm-2025")) for (minutes in listOf(60, 120, 240)) for (exam in listOf(null, 120)) for (free in listOf(false, true)) {
            val base = CatalogEditalFixtures.input(CatalogEditalFixtures.load(id), today, minutes, exam, free = free)
            var tasks = StudyPlannerEngine().plan(PlannerSnapshotBuilder.build(base), ReplanReason.INITIAL).newTasks
            val expected = todayOf(tasks)
            assertTrue(expected.isNotEmpty(), "$id: hoje vazio")
            var revision = 1L
            // Como a semente e a sincronização: chegam duas tarefas de dias passados já feitas (outro aparelho).
            val first = base.topics.first()
            tasks = tasks + listOf(
                PlannerTask("sync:1", "plan-cat", first.subjectId, first.id, today.minusDays(1), PlanTaskType.THEORY, 50, 0, PlanPriority.HIGH, PlanTaskStatus.CONCLUIDA, false),
                PlannerTask("sync:2", "plan-cat", first.subjectId, first.id, today.minusDays(2), PlanTaskType.QUESTIONS, 20, 10, PlanPriority.HIGH, PlanTaskStatus.CONCLUIDA, false),
            )
            // Abrir o app várias vezes, virar o método, acomodar: sempre automático.
            for (reason in listOf(ReplanReason.DAY_MISSED, ReplanReason.DAY_MISSED, ReplanReason.REBUILD, ReplanReason.DAY_MISSED, ReplanReason.DAY_MISSED)) {
                val proposal = automatic(base.copy(plan = base.plan.copy(revision = revision), tasks = tasks), reason)
                if (!PlanStability.isNoop(proposal)) { tasks = apply(tasks, proposal); revision++ }
                assertEquals(expected, todayOf(tasks), "$id ${minutes}min exam=$exam free=$free: hoje mudou depois de $reason")
            }
            // E o edital continua todo no plano (o resto se reorganiza de amanhã em diante).
            val theory = tasks.filter { it.type == PlanTaskType.THEORY && it.status in setOf(PlanTaskStatus.PLANEJADA, PlanTaskStatus.EM_ANDAMENTO, PlanTaskStatus.CONCLUIDA) }.mapNotNull { it.topicId }.toSet()
            assertEquals(base.topics.size, theory.size, "$id ${minutes}min exam=$exam free=$free: tópico saiu do plano")
        }
    }

    @Test fun acaoDaPessoaAindaPodeMudarHoje() {
        // Mudar as horas (explícito) não é congelado: o dia de hoje segue as horas novas.
        val base = CatalogEditalFixtures.input(CatalogEditalFixtures.load("pmmg-cfsd-qppm-2025"), today, 240, null)
        val tasks = StudyPlannerEngine().plan(PlannerSnapshotBuilder.build(base), ReplanReason.INITIAL).newTasks
        val fewer = base.copy(plan = base.plan.copy(revision = 1), tasks = tasks, availability = base.availability.map { it.copy(minutes = 60) })
        val snap = PlannerSnapshotBuilder.build(fewer)
        val after = apply(tasks, PlanStability.stabilize(StudyPlannerEngine().plan(snap, ReplanReason.AVAILABILITY_CHANGED), snap.tasks))
        assertTrue(todayOf(after).sumOf { it.minutes } <= 60 + 30, "hoje continuou com ${todayOf(after).sumOf { it.minutes }} min depois de baixar para 1h")
    }
}
