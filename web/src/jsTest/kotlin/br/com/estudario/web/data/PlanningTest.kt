package br.com.estudario.web.data

import br.com.estudario.domain.planner.ReplanReason
import br.com.estudario.domain.planner.StudyMethodConfig
import br.com.estudario.domain.planner.StudyProfile
import br.com.estudario.time.plusDays
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/** O caminho do assistente do site: edital do catálogo -> plano montado pelo motor comum. */
class PlanningTest {
    private val subjects = listOf(
        CatalogSubject("Língua Portuguesa", listOf("Interpretação de texto", "Crase", "Concordância", "Pontuação")),
        CatalogSubject("Direito Constitucional", listOf("Direitos fundamentais", "Organização do Estado", "Poder Judiciário")),
        CatalogSubject("Raciocínio Lógico", listOf("Proposições", "Porcentagem")),
    )
    private val entry = CatalogEntry(id = "teste", shortName = "PMXX", agency = "Polícia Militar", role = "Soldado")

    private fun newPlan(data: Snapshot, competitionId: Long, exam: Boolean) = Planning.NewPlan(
        competitionId = competitionId,
        name = "Plano PMXX",
        objective = "Soldado",
        startDate = Queries.todayDate(),
        examDate = if (exam) Queries.todayDate().plusDays(150) else null,
        weeklyMinutes = listOf(120, 120, 120, 120, 120, 180, 0),
        subjects = data.subjects.filter { it.competitionId == competitionId }.map { Planning.SubjectChoice(it.id, it.name) },
        method = StudyMethodConfig.forProfile(StudyProfile.DO_ZERO),
    )

    @Test
    fun catalogEditalCreatesSubjectsAndTopicsWithoutDuplicates() {
        val (once, id) = Catalog.apply(Snapshot.empty(), entry, subjects)
        val (twice, sameId) = Catalog.apply(once, entry, subjects)
        assertEquals(id, sameId)
        assertEquals(3, twice.subjects.size)
        assertEquals(9, twice.topics.size)
        assertTrue(twice.competitions.single().primary)
    }

    @Test
    fun newPlanIsActiveAndScheduledOnAvailableDaysOnly() {
        val (withEdital, competitionId) = Catalog.apply(Snapshot.empty(), entry, subjects)
        val (planned, planId) = Planning.create(withEdital, newPlan(withEdital, competitionId, exam = true))
        val plan = planned.plans.single { it.id == planId }
        assertTrue(plan.active)
        val tasks = planned.tasks.filter { it.planId == planId }
        assertTrue(tasks.isNotEmpty(), "o motor deve gerar tarefas")
        val sundays = tasks.filter { kotlinx.datetime.LocalDate.fromEpochDays(it.day).dayOfWeek == kotlinx.datetime.DayOfWeek.SUNDAY }
        assertTrue(sundays.isEmpty(), "domingo é folga")
        assertTrue(tasks.all { it.subjectName.isNotBlank() })
        // O resultado volta a ser lido como foto (formato do backup do app).
        val reparsed = Snapshot.parse(planned.encode())
        assertEquals(tasks.size, reparsed.tasks.count { it.planId == planId })
    }

    @Test
    fun completingATaskAndReplanningKeepsTheCompletedTask() {
        val (withEdital, competitionId) = Catalog.apply(Snapshot.empty(), entry, subjects)
        val (planned, planId) = Planning.create(withEdital, newPlan(withEdital, competitionId, exam = false))
        val first = planned.tasks.filter { it.planId == planId }.minBy { it.day }
        val done = Actions.completeTask(planned, first.id, first.minutes)
        val replanned = Planning.replan(done, planId, ReplanReason.TASK_COMPLETED)
        assertEquals("CONCLUIDA", replanned.tasks.single { it.id == first.id }.status)
        assertEquals(1, replanned.executions.count { it.taskId == first.id })
        assertTrue(replanned.plans.single { it.id == planId }.revision >= 2)
    }
}
