package br.com.estudario.data.transfer

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.com.estudario.data.local.*
import br.com.estudario.data.local.planner.StudyPlanEntity
import br.com.estudario.data.local.planner.StudyPlanRevisionEntity
import kotlinx.coroutines.runBlocking
import org.json.JSONObject
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BackupV3InstrumentedTest {
    private lateinit var database: AppDatabase

    @Before fun setup() {
        database = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext<Context>(), AppDatabase::class.java).build()
    }

    @After fun close() = database.close()

    @Test fun roundTripKeepsV2LearningData() = runBlocking {
        val dao = database.dao()
        val competition = dao.insertCompetition(CompetitionEntity(name = "C", isPrimary = true))
        val subject = dao.insertSubject(SubjectEntity(competitionId = competition, name = "M"))
        val topic = dao.insertTopic(TopicEntity(subjectId = subject, title = "T"))
        dao.insertSnippet(TopicSnippetEntity(topicId = topic, kind = SnippetKind.RECUPERACAO, text = "Pergunta?", isFavorite = true))
        dao.insertErrorConcept(ErrorConceptEntity(topicId = topic, title = "Conceito", summary = "Correção"))
        dao.insertQuestionSession(QuestionSessionEntity("session", QuestionSessionType.SMART, 1, 61, 60, 10, 8, topicIdsText = topic.toString()))

        val service = BackupService(database)
        val backup = service.export()
        assertEquals(5, JSONObject(backup).getInt("version"))
        service.restore(backup)

        assertEquals("Pergunta?", dao.snippetsOnce().single().text)
        assertEquals(true, dao.snippetsOnce().single().isFavorite)
        assertEquals("Conceito", dao.errorConceptsOnce().single().title)
        assertEquals(60L, dao.questionSessionsOnce().single().durationSeconds)
    }

    @Test fun roundTripKeepsPlannerGraphInV5AndLegacyArraysRemainOptional() = runBlocking {
        val competition = database.dao().insertCompetition(CompetitionEntity(name = "C", isPrimary = true))
        database.plannerDao().insertPlan(StudyPlanEntity("plan", competition, "Plano", "Aprovação", 1, active = true, masterPlan = true, revision = 2))
        database.plannerDao().insertRevision(StudyPlanRevisionEntity("plan", 2, 1, "TEST"))
        val service = BackupService(database)

        val backup = service.export()
        service.restore(backup)

        val restored = database.plannerDao().plan("plan")!!
        assertEquals(2, restored.revision)
        assertEquals(true, restored.active)
        assertEquals(true, restored.masterPlan)
        val legacy = JSONObject(backup).put("version", 4)
        listOf("studyPlans", "studyPlanRevisions").forEach(legacy::remove)
        service.restore(legacy.toString())
        assertEquals(0, database.plannerDao().plansOnce().size)
    }
}
