package br.com.estudario.web.data

import br.com.estudario.domain.planner.StudyMethodConfig
import br.com.estudario.domain.planner.StudyProfile
import br.com.estudario.time.plusDays
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Dois concursos num plano só: apagar o concurso que criou o plano não pode levar junto o plano
 * nem as matérias do outro concurso. O plano fica com o que sobrou e passa a ser do outro concurso.
 */
class DeleteCompetitionTest {
    @Test
    fun apagarOConcursoQueCriouOPlanoMantemOPlanoComOOutroConcurso() {
        val (first, firstId) = Catalog.apply(Snapshot.empty(), CatalogEntry(id = "a", shortName = "PMXX", agency = "Polícia Militar", role = "Soldado"), listOf(CatalogSubject("Português", listOf("Crase", "Concordância"))))
        val (both, secondId) = Catalog.apply(first, CatalogEntry(id = "b", shortName = "TRXX", agency = "Tribunal", role = "Técnico"), listOf(CatalogSubject("Direito Constitucional", listOf("Direitos fundamentais", "Organização do Estado"))))
        val (planned, planId) = Planning.create(
            both,
            Planning.NewPlan(
                competitionId = firstId, name = "Plano", objective = "Os dois", startDate = Queries.todayDate(),
                examDate = Queries.todayDate().plusDays(90), weeklyMinutes = List(7) { 120 },
                subjects = both.subjects.map { Planning.SubjectChoice(it.id, it.name) },
                method = StudyMethodConfig.forProfile(StudyProfile.DO_ZERO),
            ),
        )

        val after = Actions.deleteCompetition(planned, firstId)

        val plan = after.plans.single { it.id == planId }
        assertEquals(secondId, plan.competitionId)
        val subjectIds = after.subjects.map { it.id }.toSet()
        assertTrue(after.planSubjects.filter { it.planId == planId }.all { it.subjectId in subjectIds })
        assertTrue(after.tasks.filter { it.planId == planId && it.subjectId != null }.all { it.subjectId in subjectIds })
        assertTrue(after.tasks.any { it.planId == planId && it.status == "PLANEJADA" }, "o plano continua com tarefas do concurso que ficou")
    }

    @Test
    fun planoQueFicaSemMateriaSaiJunto() {
        val (first, firstId) = Catalog.apply(Snapshot.empty(), CatalogEntry(id = "a", shortName = "PMXX", agency = "Polícia Militar", role = "Soldado"), listOf(CatalogSubject("Português", listOf("Crase"))))
        val (planned, planId) = Planning.create(
            first,
            Planning.NewPlan(
                competitionId = firstId, name = "Plano", objective = "Um", startDate = Queries.todayDate(),
                examDate = Queries.todayDate().plusDays(90), weeklyMinutes = List(7) { 120 },
                subjects = first.subjects.map { Planning.SubjectChoice(it.id, it.name) },
                method = StudyMethodConfig.forProfile(StudyProfile.DO_ZERO),
            ),
        )
        val after = Actions.deleteCompetition(planned, firstId)
        assertTrue(after.plans.none { it.id == planId })
        assertTrue(after.tasks.none { it.planId == planId })
    }
}
