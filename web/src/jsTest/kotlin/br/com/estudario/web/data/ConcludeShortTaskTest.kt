package br.com.estudario.web.data

import br.com.estudario.domain.planner.ReplanReason
import br.com.estudario.domain.planner.StudyMethodConfig
import br.com.estudario.domain.planner.StudyProfile
import br.com.estudario.time.plusDays
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Caso real de 23/11: plano criado no dia, teoria de Literatura no Início. "Concluir" com o tempo do foco
 * (menos que o planejado) deixava a tarefa em andamento, a reorganização punha o resto numa tarefa nova
 * no mesmo dia e cada toque pagava o XP base de novo: a tarefa nunca saía do dia.
 */
class ConcludeShortTaskTest {
    private fun planned(): Pair<Snapshot, String> {
        val subjects = listOf(CatalogSubject("Língua Portuguesa", listOf("Literatura", "Interpretação de texto", "Crase")), CatalogSubject("Direito", (1..8).map { "Tópico $it" }))
        val (data, competitionId) = Catalog.apply(Snapshot.empty(), CatalogEntry(id = "x", shortName = "TRT", agency = "Tribunal", role = "Analista"), subjects)
        return Planning.create(
            data,
            Planning.NewPlan(
                competitionId = competitionId, name = "Plano", objective = "TRT", startDate = Queries.todayDate(),
                examDate = Queries.todayDate().plusDays(120), weeklyMinutes = List(7) { 120 },
                subjects = data.subjects.map { Planning.SubjectChoice(it.id, it.name) },
                method = StudyMethodConfig.forProfile(StudyProfile.DO_ZERO),
            ),
        )
    }

    private fun Snapshot.liveTheory(planId: String, topicId: Long?) = tasks.firstOrNull {
        it.planId == planId && it.day == Queries.todayEpoch() && it.topicId == topicId && it.type == "THEORY" && it.status in setOf("PLANEJADA", "EM_ANDAMENTO")
    }

    /** O mesmo que o botão do Início faz (Home.kt). */
    private fun tap(data: Snapshot, planId: String, taskId: String, minutes: Int, finish: Boolean): Snapshot {
        val task = data.tasks.first { it.id == taskId }
        val worked = Actions.completeTask(data, taskId, minutes, finish = finish)
        val closed = worked.tasks.first { it.id == taskId }.status == "CONCLUIDA"
        val done = if (closed && task.topicId != null) Actions.completeStudy(worked, task.topicId) else worked
        return Planning.replan(done, planId, if (closed) ReplanReason.TASK_COMPLETED else ReplanReason.TASK_PARTIAL)
    }

    @Test
    fun concluirComMenosTempoFechaATarefaESaiDoDia() {
        var (data, planId) = planned()
        val first = data.tasks.first { it.planId == planId && it.day == Queries.todayEpoch() && it.type == "THEORY" && it.topicId != null }
        data = tap(data, planId, first.id, 5, finish = true)
        assertEquals("CONCLUIDA", data.tasks.first { it.id == first.id }.status)
        assertEquals(null, data.liveTheory(planId, first.topicId), "a teoria voltou para hoje depois de concluída")
        assertTrue(data.topics.first { it.id == first.topicId }.status == "ESTUDADO")
        // Tocar de novo na mesma tarefa não registra nada (nem XP).
        val again = Actions.completeTask(data, first.id, 5, finish = true)
        assertTrue(again === data)
    }

    @Test
    fun salvarProgressoVariasVezesPagaOXpBaseUmaVezSo() {
        var (data, planId) = planned()
        val first = data.tasks.first { it.planId == planId && it.day == Queries.todayEpoch() && it.type == "THEORY" && it.topicId != null }
        repeat(4) {
            val live = data.liveTheory(planId, first.topicId) ?: return@repeat
            data = tap(data, planId, live.id, 5, finish = false)
        }
        assertEquals(4, data.executions.size)
        val work = br.com.estudario.domain.ProgressInputs.planWork(Progress.sources(data), Queries.zone)
        assertEquals(1, work.size, "cada parte remarcada virou uma tarefa a mais no XP")
        assertEquals(20, work.single().minutes)
    }
}
