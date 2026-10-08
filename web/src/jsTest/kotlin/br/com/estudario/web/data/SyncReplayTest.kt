package br.com.estudario.web.data

import br.com.estudario.domain.planner.StudyMethodConfig
import br.com.estudario.domain.planner.StudyProfile
import br.com.estudario.time.plusDays
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * O que o site faz quando o celular publicou no meio-tempo: baixa a versão da conta e refaz por
 * cima dela só as edições feitas aqui. Concluir no site uma tarefa que o celular já concluiu não
 * pode mudar nada (nem registrar o estudo de novo), e o que o celular fez continua lá.
 */
class SyncReplayTest {
    private fun planned(): Pair<Snapshot, String> {
        val subjects = listOf(CatalogSubject("Língua Portuguesa", listOf("Crase", "Concordância")), CatalogSubject("Direito Constitucional", listOf("Direitos fundamentais")))
        val (withEdital, competitionId) = Catalog.apply(Snapshot.empty(), CatalogEntry(id = "t", shortName = "PMXX", agency = "Polícia Militar", role = "Soldado"), subjects)
        return Planning.create(
            withEdital,
            Planning.NewPlan(
                competitionId = competitionId, name = "Plano", objective = "Soldado", startDate = Queries.todayDate(),
                examDate = Queries.todayDate().plusDays(90), weeklyMinutes = List(7) { 120 },
                subjects = withEdital.subjects.map { Planning.SubjectChoice(it.id, it.name) },
                method = StudyMethodConfig.forProfile(StudyProfile.DO_ZERO),
            ),
        )
    }

    @Test
    fun concluirDeNovoOQueOCelularJaConcluiuNaoMudaNada() {
        val (base, planId) = planned()
        val task = base.tasks.first { it.planId == planId && it.status == "PLANEJADA" }
        val siteEdit: (Snapshot) -> Snapshot = { Actions.completeTask(it, task.id, task.minutes) }

        // O celular concluiu a mesma tarefa e publicou antes.
        val remote = Actions.completeTask(base, task.id, task.minutes)
        val replayed = siteEdit(remote)

        assertEquals(remote.encode(), replayed.encode())
        assertEquals(1, replayed.executions.count { it.taskId == task.id })
    }

    @Test
    fun edicaoDoSiteEntraSemApagarADoCelular() {
        val (base, planId) = planned()
        val tasks = base.tasks.filter { it.planId == planId && it.status == "PLANEJADA" }.take(2)
        val phoneDone = tasks[0]
        val siteDone = tasks[1]

        val remote = Actions.completeTask(base, phoneDone.id, phoneDone.minutes)
        val replayed = Actions.completeTask(remote, siteDone.id, siteDone.minutes)

        assertEquals("CONCLUIDA", replayed.tasks.first { it.id == phoneDone.id }.status)
        assertEquals("CONCLUIDA", replayed.tasks.first { it.id == siteDone.id }.status)
        assertEquals(2, replayed.executions.size)
    }
}
