package br.com.estudario.data.transfer

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.com.estudario.data.local.*
import br.com.estudario.data.local.planner.StudyPlanEntity
import br.com.estudario.data.local.planner.StudyPlanRevisionEntity
import kotlinx.coroutines.runBlocking
import org.json.JSONArray
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

    @Test fun roundTripKeepsLearningDataInV7() = runBlocking {
        val dao = database.dao()
        val competition = dao.insertCompetition(CompetitionEntity(name = "C", isPrimary = true))
        val subject = dao.insertSubject(SubjectEntity(competitionId = competition, name = "M"))
        val topic = dao.insertTopic(TopicEntity(subjectId = subject, title = "T"))
        dao.insertSnippet(TopicSnippetEntity(topicId = topic, kind = SnippetKind.RECUPERACAO, text = "Pergunta?", isFavorite = true))
        dao.insertErrorConcept(ErrorConceptEntity(topicId = topic, title = "Conceito", summary = "Correção"))
        dao.insertQuestionSession(QuestionSessionEntity("session", QuestionSessionType.SMART, 1, 61, 60, 10, 8, topicIdsText = topic.toString()))

        val service = BackupService(database)
        val backup = service.export()
        assertEquals(7, JSONObject(backup).getInt("version"))
        service.restore(backup)

        assertEquals("Pergunta?", dao.snippetsOnce().single().text)
        assertEquals(true, dao.snippetsOnce().single().isFavorite)
        assertEquals("Conceito", dao.errorConceptsOnce().single().title)
        assertEquals(60L, dao.questionSessionsOnce().single().durationSeconds)
    }

    @Test fun roundTripKeepsPlannerGraphInV7AndLegacyArraysRemainOptional() = runBlocking {
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
        listOf("studyPlans", "studyPlanRevisions", "focusSessions").forEach(legacy::remove)
        service.restore(legacy.toString())
        assertEquals(0, database.plannerDao().plansOnce().size)
    }

    @Test fun focusHistoryRoundTripsFreeAndPlanLinkedSessions() = runBlocking {
        val dao = database.dao()
        val free = FocusSessionEntity("focus-1", "Direito", 1_000, 61_000, 60, "4,5", FocusSessionOrigin.LIVRE)
        val plan = FocusSessionEntity("focus-2", "Português", 2_000, 62_000, 60, "8", FocusSessionOrigin.PLANO, taskId = "task-1")
        dao.insertFocusSessionIfAbsent(free)
        dao.insertFocusSessionIfAbsent(plan)
        val service = BackupService(database)

        val backup = service.export()
        assertEquals(7, JSONObject(backup).getInt("version"))
        service.restore(backup)

        val restored = dao.focusSessionsOnce().associateBy { it.id }
        assertEquals(setOf(free.id, plan.id), restored.keys)
        assertEquals(free.title, restored.getValue(free.id).title)
        assertEquals(free.startedAt, restored.getValue(free.id).startedAt)
        assertEquals(free.completedAt, restored.getValue(free.id).completedAt)
        assertEquals(free.durationSeconds, restored.getValue(free.id).durationSeconds)
        assertEquals(free.subjectIdsText, restored.getValue(free.id).subjectIdsText)
        assertEquals(free.origin, restored.getValue(free.id).origin)
        assertEquals(plan.title, restored.getValue(plan.id).title)
        assertEquals(plan.startedAt, restored.getValue(plan.id).startedAt)
        assertEquals(plan.completedAt, restored.getValue(plan.id).completedAt)
        assertEquals(plan.durationSeconds, restored.getValue(plan.id).durationSeconds)
        assertEquals(plan.subjectIdsText, restored.getValue(plan.id).subjectIdsText)
        assertEquals(plan.origin, restored.getValue(plan.id).origin)
        assertEquals(plan.taskId, restored.getValue(plan.id).taskId)
    }

    @Test fun legacyV6BackupWithoutFocusArrayClearsFocusHistory() = runBlocking {
        val dao = database.dao()
        dao.insertFocusSessionIfAbsent(
            FocusSessionEntity("old-local", "Matemática", 1_000, 31_000, 30, origin = FocusSessionOrigin.LIVRE),
        )
        val service = BackupService(database)
        val legacy = JSONObject(service.export()).put("version", 6).apply { remove("focusSessions") }

        service.restore(legacy.toString())

        assertEquals(0, dao.focusSessionsOnce().size)
    }

    @Test fun legacyV6StudyRowsMarkedAsFocusMoveIntoDedicatedHistory() = runBlocking {
        val service = BackupService(database)
        val legacy = JSONObject(service.export()).put("version", 6).apply {
            remove("focusSessions")
            put("sessions", JSONArray().put(
                JSONObject()
                    .put("id", 41)
                    .put("topicId", 42)
                    .put("startedAt", 1_000)
                    .put("completedAt", 91_000)
                    .put("competitionId", 3)
                    .put("subjectId", 7)
                    .put("durationSeconds", 90)
                    .put("questionCount", 0)
                    .put("correctCount", 0)
                    .put("wrongCount", 0)
                    .put("notes", "Modo foco")
                    .put("sourcePackageId", JSONObject.NULL),
            ))
        }

        service.restore(legacy.toString())

        assertEquals(0, database.dao().sessionsOnce().size)
        val restored = database.dao().focusSessionsOnce().single()
        assertEquals("legacy-41", restored.id)
        assertEquals("7", restored.subjectIdsText)
        assertEquals(FocusSessionOrigin.MATERIA, restored.origin)
        assertEquals(42L, restored.topicId)
        assertEquals(90L, restored.durationSeconds)
    }
}
