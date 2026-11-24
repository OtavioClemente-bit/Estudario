package br.com.estudario.domain.planner

import br.com.estudario.time.*
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * O caminho da semente de teste (RealSeed: TRT-3, 143 tópicos, plano e histórico) e do reinício do app
 * (EstudarioApplication: reorganização diária automática). Caso real de 24/11 no celular: depois da semente, o
 * primeiro reinício mudou a missão de hoje de "4 atividades · 1h42" para "6 atividades · 2h". Agora a semente já
 * termina no plano que a abertura produziria, e nenhum reinício mexe em hoje.
 */
class SeedRestartTodayTest {
    private val today = LocalDate(2026, 11, 24)
    private val engine = StudyPlannerEngine()

    private fun input(tasks: List<PlannerTask>, revision: Long): PlannerSnapshotBuilder.Input {
        val sizes = listOf(30, 20, 22, 16, 14, 14, 15, 12)
        val knowledge = listOf(InitialKnowledge.BASIC, InitialKnowledge.CONTACT, InitialKnowledge.CONTACT, InitialKnowledge.CONTACT, InitialKnowledge.NONE, InitialKnowledge.NONE, InitialKnowledge.SOLID, InitialKnowledge.SOLID)
        val subjects = sizes.indices.map { i ->
            PlannerSnapshotBuilder.PlanSubject(i + 1L, "Matéria ${i + 1}", if (i < 2) PlanPriority.CRITICAL else PlanPriority.HIGH, false, 0, null, i,
                if (i == 1 || i == 5) PersonalDifficulty.HARD else PersonalDifficulty.NORMAL, knowledge[i])
        }
        val topics = sizes.flatMapIndexed { i, n -> (0 until n).map { p -> PlannerSnapshotBuilder.Topic(1000L * (i + 1) + p, i + 1L, null, "T$i.$p", p, p < 3, if (p < 3) today.minusDays(10L + p).toEpochDays() * 86_400_000L else null) } }
        return PlannerSnapshotBuilder.Input(
            plan = PlannerSnapshotBuilder.Plan("seed-plan", revision, today.minusDays(20).toEpochDay(), today.plusDays(150).toEpochDay(), StudyMethodConfig.forProfile(StudyProfile.DO_ZERO)),
            planSubjects = subjects, topics = topics, questionsByTopic = emptyMap(), attempts = emptyList(), pendingReviews = emptyList(),
            tasks = tasks, executions = emptyList(),
            availability = (1..7).map { PlannerSnapshotBuilder.Availability(it, if (it == 7) 0 else 120, it == 7) },
            dayOverrides = emptyList(), today = today, zone = TimeZone.UTC,
        )
    }

    /** StudyPlanApplicationService.replan(automatic = true), sem o banco. */
    private fun automaticReplan(tasks: List<PlannerTask>, revision: Long): Pair<List<PlannerTask>, Boolean> {
        val snap = PlannerSnapshotBuilder.build(input(tasks, revision)).let { it.copy(policy = it.policy.copy(freezeToday = true)) }
        val proposal = PlanStability.stabilize(engine.plan(snap, ReplanReason.DAY_MISSED), snap.tasks)
        if (PlanStability.isNoop(proposal)) return tasks to false
        val moved = proposal.transitions.associateBy { it.taskId }
        return (tasks.map { t -> moved[t.id]?.let { t.copy(status = it.to) } ?: t } + proposal.newTasks) to true
    }

    private fun todayOf(tasks: List<PlannerTask>) = tasks.filter { it.date == today && it.status == PlanTaskStatus.PLANEJADA }
        .sortedWith(compareBy({ it.sequence }, { it.id })).map { Triple(it.id, it.plannedMinutes, it.sequence) }

    @Test
    fun `semente e reinicios nao mudam a missao de hoje`() {
        // RealSeed: plano INITIAL e, depois, duas tarefas de dias anteriores já feitas.
        var tasks = engine.plan(PlannerSnapshotBuilder.build(input(emptyList(), 0)), ReplanReason.INITIAL).newTasks +
            listOf(1, 2).map { back -> PlannerTask("seed:task:$back", "seed-plan", 1L, 1000L + back, today.minusDays(back.toLong()), if (back == 1) PlanTaskType.QUESTIONS else PlanTaskType.THEORY, 50, if (back == 1) 10 else 0, PlanPriority.HIGH, PlanTaskStatus.CONCLUIDA, false) }
        val mission = todayOf(tasks)
        assertTrue("a missão de hoje veio vazia", mission.isNotEmpty())
        var revision = 1L
        // A semente termina com a mesma reorganização da abertura, até não mudar mais (RealSeed).
        for (round in 0 until 3) { val (next, wrote) = automaticReplan(tasks, revision); tasks = next; if (!wrote) break; revision++ }
        assertEquals("a semente mudou a missão de hoje", mission, todayOf(tasks))
        // Reinícios: nada a gravar, e hoje igual.
        repeat(3) {
            val (next, wrote) = automaticReplan(tasks, revision)
            assertTrue("o reinício ${it + 1} reorganizou o plano", !wrote)
            tasks = next
            assertEquals(mission, todayOf(tasks))
        }
    }
}
