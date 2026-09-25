package br.com.estudario.ui.setup

import androidx.lifecycle.ViewModelStore
import androidx.test.core.app.ApplicationProvider
import br.com.estudario.EstudarioApplication
import br.com.estudario.domain.setup.InitialSetupSnapshot
import br.com.estudario.domain.setup.InitialSetupStep
import br.com.estudario.domain.setup.SyllabusMethod
import br.com.estudario.domain.planner.PersonalDifficulty
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class SetupSyllabusPersistenceTest {
    @Test
    fun reviewEditsPersistAndAdvancementUsesCurrentSubjects() = runBlocking {
        val app = ApplicationProvider.getApplicationContext<EstudarioApplication>()
        val previous = app.preferences.initialSetup.first()
        val dao = app.database.dao()
        val competitionId = app.repository.addCompetition("Teste de revisão ${System.nanoTime()}")
        val store = ViewModelStore()
        val viewModel = withContext(Dispatchers.Main) { InitialSetupViewModel(app).also { store.put("setup", it) } }
        try {
            app.preferences.setInitialSetup(InitialSetupSnapshot(competitionId = competitionId, step = InitialSetupStep.SYLLABUS_REVIEW, syllabusMethod = SyllabusMethod.MANUAL))
            viewModel.advance(InitialSetupStep.SYLLABUS_REVIEW, InitialSetupStep.PROFILE).join()
            assertEquals(InitialSetupStep.SYLLABUS_REVIEW, app.preferences.initialSetup.first().step)
            viewModel.addReviewSubject("Português").join()
            viewModel.addReviewSubject("Matemática").join()
            val subjects = dao.subjectsFor(competitionId)
            val portuguese = subjects.first { it.name == "Português" }
            val math = subjects.first { it.name == "Matemática" }
            viewModel.addReviewTopic(portuguese.id, "Interpretação").join()
            viewModel.addReviewTopic(portuguese.id, " interpretação ").join()
            assertEquals(1, dao.topicsFor(portuguese.id).size)
            val topic = dao.topicsFor(portuguese.id).single()
            app.repository.addTopic(portuguese.id, "Textos literários", topic.id)
            viewModel.removeReviewTopic(topic).join()
            assertTrue(dao.topicsFor(portuguese.id).isEmpty())
            assertTrue(app.preferences.initialSetup.first().manualTopics["Português"].orEmpty().isEmpty())
            viewModel.addReviewTopic(portuguese.id, "Ortografia").join()
            assertEquals(listOf("Ortografia"), dao.topicsFor(portuguese.id).map { it.title })
            viewModel.advance(InitialSetupStep.SYLLABUS_REVIEW, InitialSetupStep.SUBJECT_PRIORITY).join()
            assertEquals(InitialSetupStep.SUBJECT_PRIORITY, app.preferences.initialSetup.first().step)

            app.preferences.updateInitialSetup { it.copy(step = InitialSetupStep.SUBJECT_DIFFICULTY) }
            viewModel.advance(InitialSetupStep.SUBJECT_DIFFICULTY, InitialSetupStep.AVAILABILITY).join()
            assertEquals(InitialSetupStep.AVAILABILITY, app.preferences.initialSetup.first().step)
            viewModel.setSubjectDifficulty(portuguese.id.toString(), PersonalDifficulty.HARD).join()
            viewModel.setSubjectDifficulty(math.id.toString(), PersonalDifficulty.EASY).join()
            assertEquals(
                mapOf(portuguese.id.toString() to PersonalDifficulty.HARD, math.id.toString() to PersonalDifficulty.EASY),
                app.preferences.initialSetup.first().subjectDifficulties,
            )

            app.preferences.updateInitialSetup { it.copy(step = InitialSetupStep.SYLLABUS_REVIEW) }
            viewModel.removeReviewSubject(math).join()
            assertEquals(listOf("Português"), dao.subjectsFor(competitionId).map { it.name })
            assertEquals(listOf("Português"), app.preferences.initialSetup.first().manualSubjects)
            assertFalse(math.id.toString() in app.preferences.initialSetup.first().subjectDifficulties)
            viewModel.confirmManualSyllabus().join()
            assertEquals(listOf("Português"), dao.subjectsFor(competitionId).map { it.name })
            assertEquals(listOf("Ortografia"), dao.topicsFor(portuguese.id).map { it.title })
        } finally {
            withContext(Dispatchers.Main) { store.clear() }
            dao.competitionsOnce().firstOrNull { it.id == competitionId }?.let { app.repository.deleteCompetition(it) }
            app.preferences.setInitialSetup(previous)
        }
    }

    @Test
    fun importingSyllabusIntoNamedCompetitionDoesNotCreateAnotherCompetition() = runBlocking {
        val app = ApplicationProvider.getApplicationContext<EstudarioApplication>()
        val previous = app.preferences.initialSetup.first()
        val dao = app.database.dao()
        val existingCompetitionIds = dao.competitionsOnce().map { it.id }.toSet()
        val chosenName = "Nome escolhido ${System.nanoTime()}"
        val competitionId = app.repository.addCompetition(chosenName)
        val store = ViewModelStore()
        val viewModel = withContext(Dispatchers.Main) { InitialSetupViewModel(app).also { store.put("setup", it) } }
        val importedName = "Nome vindo do arquivo ${System.nanoTime()}"
        val raw = """
            {
              "version":2,
              "packageId":"setup-target-${System.nanoTime()}",
              "concurso":{"nome":"$importedName","principal":true},
              "materias":[{"id":"materia","nome":"Matéria","topicos":[{
                "id":"topico","titulo":"Tópico","resumos":[],"questoes":[]
              }]}]
            }
        """.trimIndent()

        try {
            app.preferences.setInitialSetup(
                InitialSetupSnapshot(
                    status = br.com.estudario.domain.setup.InitialSetupStatus.IN_PROGRESS,
                    step = InitialSetupStep.SYLLABUS_METHOD,
                    competitionId = competitionId,
                    competitionName = chosenName,
                    syllabusMethod = SyllabusMethod.IMPORT_ESTUDO,
                ),
            )

            viewModel.confirmEstudoImport(raw).join()

            val createdCompetitions = dao.competitionsOnce().filter { it.id !in existingCompetitionIds }
            assertEquals(1, createdCompetitions.size)
            assertEquals(competitionId, createdCompetitions.single().id)
            assertEquals(chosenName, createdCompetitions.single().name)
            assertEquals(1, dao.subjectsFor(competitionId).size)
            assertEquals(competitionId, app.preferences.initialSetup.first().competitionId)
        } finally {
            withContext(Dispatchers.Main) { store.clear() }
            dao.competitionsOnce().filter { it.id !in existingCompetitionIds }.forEach { app.repository.deleteCompetition(it) }
            app.preferences.setInitialSetup(previous)
        }
    }

    @Test
    fun appliedAiSyllabusMovesSetupToReviewStepForTheSameCompetition() = runBlocking {
        val app = ApplicationProvider.getApplicationContext<EstudarioApplication>()
        val previous = app.preferences.initialSetup.first()
        val competitionId = app.repository.addCompetition("IA aplicada ${System.nanoTime()}")
        val store = ViewModelStore()
        val viewModel = withContext(Dispatchers.Main) { InitialSetupViewModel(app).also { store.put("setup", it) } }
        try {
            app.preferences.setInitialSetup(
                InitialSetupSnapshot(
                    status = br.com.estudario.domain.setup.InitialSetupStatus.IN_PROGRESS,
                    step = InitialSetupStep.SYLLABUS_METHOD,
                    competitionId = competitionId,
                    competitionName = "IA aplicada",
                    syllabusMethod = SyllabusMethod.DIRECT_AI,
                ),
            )

            viewModel.onAiSyllabusApplied().join()

            val snapshot = app.preferences.initialSetup.first()
            assertEquals(InitialSetupStep.SYLLABUS_REVIEW, snapshot.step)
            assertEquals(SyllabusMethod.DIRECT_AI, snapshot.syllabusMethod)
        } finally {
            withContext(Dispatchers.Main) { store.clear() }
            app.repository.deleteCompetition(app.database.dao().competitionsOnce().first { it.id == competitionId })
            app.preferences.setInitialSetup(previous)
        }
    }
}
