package br.com.estudario.data.transfer

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.com.estudario.data.local.AppDatabase
import br.com.estudario.data.local.CompetitionEntity
import br.com.estudario.data.local.ExamProfileEntity
import br.com.estudario.data.local.QuestionEntity
import br.com.estudario.data.local.SimulationEntity
import br.com.estudario.data.local.SubjectEntity
import br.com.estudario.data.local.TopicEntity
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/** Backup v8: simulados, perfis de prova e questões ocultas/de simulado sobrevivem à restauração. */
@RunWith(AndroidJUnit4::class)
class BackupV8SimulationTest {
    private lateinit var database: AppDatabase

    @Before fun setup() {
        database = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext<Context>(), AppDatabase::class.java).build()
    }

    @After fun close() { database.close() }

    @Test fun keepsSimulationsExamProfilesAndQuestionFlags() = runBlocking {
        val dao = database.dao()
        val simulations = database.simulationDao()
        val competitionId = dao.insertCompetition(CompetitionEntity(name = "Concurso"))
        val subjectId = dao.insertSubject(SubjectEntity(competitionId = competitionId, name = "Matéria"))
        val topicId = dao.insertTopic(TopicEntity(subjectId = subjectId, title = "Tópico"))
        simulations.saveExamProfile(ExamProfileEntity(competitionId = competitionId, board = "Cebraspe", style = "TRUE_FALSE", targetPercent = 80, updatedAt = 1L))
        val simulationId = simulations.insertSimulation(
            SimulationEntity(competitionId = competitionId, mode = "FULL", title = "Simulado 1", plannedQuestions = 10, timeLimitMinutes = 60, status = "FINISHED", answersJson = "{\"1\":\"A\"}", createdAt = 2L, scorePercent = 70),
        )
        dao.insertQuestion(QuestionEntity(topicId = topicId, statement = "Oculta", explanation = "", isHidden = true))
        dao.insertQuestion(QuestionEntity(topicId = topicId, statement = "Do simulado", explanation = "", simulationId = simulationId))

        val before = BackupService(database).export()
        BackupService(database).restore(before)

        assertEquals("Cebraspe", simulations.examProfile(competitionId)?.board)
        val restored = simulations.simulation(simulationId)!!
        assertEquals("{\"1\":\"A\"}", restored.answersJson)
        assertEquals(70, restored.scorePercent)
        val questions = dao.questionsOnce().map { it.question }
        assertTrue(questions.single { it.statement == "Oculta" }.isHidden)
        assertEquals(simulationId, questions.single { it.statement == "Do simulado" }.simulationId)
    }
}
