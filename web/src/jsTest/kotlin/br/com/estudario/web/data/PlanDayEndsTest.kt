package br.com.estudario.web.data

import br.com.estudario.domain.planner.ReplanReason
import br.com.estudario.domain.planner.StudyMethodConfig
import br.com.estudario.domain.planner.StudyProfile
import br.com.estudario.time.plusDays
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Concluir as tarefas de hoje no site (pelo Início ou pela matéria) tem que esvaziar o dia: nada de
 * matéria nova aparecendo a cada conclusão, nem tarefa repetida.
 */
class PlanDayEndsTest {
    private fun planned(): Pair<Snapshot, String> {
        val subjects = (1..4).map { s -> CatalogSubject("Matéria $s", (1..12).map { "Tópico $s.$it" }) }
        val (data, competitionId) = Catalog.apply(Snapshot.empty(), CatalogEntry(id = "x", shortName = "TRT", agency = "Tribunal", role = "Analista"), subjects)
        return Planning.create(
            data,
            Planning.NewPlan(
                competitionId = competitionId, name = "Plano", objective = "TRT", startDate = Queries.todayDate(),
                examDate = Queries.todayDate().plusDays(120), weeklyMinutes = List(7) { 180 },
                subjects = data.subjects.map { Planning.SubjectChoice(it.id, it.name) },
                method = StudyMethodConfig.forProfile(StudyProfile.DO_ZERO),
            ),
        )
    }

    private fun Snapshot.activeToday(planId: String) =
        tasks.filter { it.planId == planId && it.day == Queries.todayEpoch() && it.status in setOf("PLANEJADA", "EM_ANDAMENTO") }

    private fun Snapshot.liveToday(planId: String) =
        tasks.filter { it.planId == planId && it.day == Queries.todayEpoch() && it.status !in setOf("REPROGRAMADA", "CANCELADA") }

    private fun assertNoDuplicates(data: Snapshot, planId: String) {
        val live = data.activeToday(planId)
        val keys = live.map { listOf(it.day, it.subjectId, it.topicId, it.type) }
        assertEquals(keys.size, keys.toSet().size, "tarefa repetida: ${keys.groupBy { it }.filter { it.value.size > 1 }.keys}")
    }

    @Test
    fun concluirPeloInicioEsvaziaODia() {
        var (data, planId) = planned()
        val initial = data.liveToday(planId).size
        assertTrue(initial > 0)
        var guard = 0
        while (data.activeToday(planId).isNotEmpty() && guard++ < 30) {
            val task = data.activeToday(planId).first()
            // Igual ao botão Concluir do plano (CompleteTaskDialog): teoria concluída marca o tópico.
            data = Actions.completeTask(data, task.id, task.minutes, task.questions, task.questions / 2)
            if (task.type == "THEORY" && task.topicId != null) data = Actions.completeStudy(data, task.topicId)
            data = Planning.replan(data, planId, ReplanReason.TASK_COMPLETED)
            assertNoDuplicates(data, planId)
            assertTrue(data.liveToday(planId).size <= initial, "o dia cresceu: ${data.liveToday(planId).size} > $initial")
        }
        assertEquals(0, data.activeToday(planId).size, "o dia não acabou depois de $guard conclusões")
    }

    @Test
    fun comecarEDepoisConcluirPelaMateriaEsvaziaODia() {
        var (data, planId) = planned()
        val initial = data.liveToday(planId).size
        var guard = 0
        while (data.activeToday(planId).isNotEmpty() && guard++ < 30) {
            val task = data.activeToday(planId).first()
            // Começar: parte do tempo (fica em andamento) e replaneja como parcial.
            data = Actions.completeTask(data, task.id, (task.minutes / 2).coerceAtLeast(1))
            data = Planning.replan(data, planId, ReplanReason.TASK_PARTIAL)
            assertNoDuplicates(data, planId)
            // Depois conclui pelo tópico, como no botão Concluir estudo da matéria.
            val topicId = task.topicId ?: continue
            data = Actions.completeStudyWithPlan(data, topicId)
            assertNoDuplicates(data, planId)
            assertTrue(data.liveToday(planId).size <= initial, "o dia cresceu: ${data.liveToday(planId).size} > $initial")
        }
        assertEquals(0, data.activeToday(planId).size, "o dia não acabou depois de $guard conclusões")
    }
}
