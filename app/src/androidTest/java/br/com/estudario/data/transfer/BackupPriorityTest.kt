package br.com.estudario.data.transfer

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.com.estudario.data.local.AppDatabase
import br.com.estudario.data.local.CompetitionEntity
import br.com.estudario.data.local.SubjectEntity
import br.com.estudario.data.local.TopicEntity
import br.com.estudario.domain.PriorityEvidence
import br.com.estudario.domain.PriorityEvidenceType
import br.com.estudario.domain.PriorityLevel
import br.com.estudario.domain.PrioritySource
import kotlinx.coroutines.runBlocking
import org.json.JSONObject
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BackupPriorityTest {
    private lateinit var database: AppDatabase

    @Before fun setup() {
        database = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext<Context>(), AppDatabase::class.java).build()
    }

    @After fun close() { database.close() }

    @Test fun newBackupRoundTripsAutomaticAssessmentAndOverride() = runBlocking {
        val dao = database.dao()
        val competitionId = dao.insertCompetition(CompetitionEntity(name = "Concurso", externalId = "c", assessedPriorityScore = 88, assessedPrioritySource = PrioritySource.AI_INFERENCE, assessedPriorityConfidence = .8f, assessedPriorityRationale = "Análise", assessedPriorityEvidenceJson = "[{\"type\":\"OFFICIAL_WEIGHT\",\"description\":\"Peso 2\",\"value\":2}]", hasAssessedPriority = true, userPriorityOverride = PriorityLevel.LOW))
        val subjectId = dao.insertSubject(SubjectEntity(competitionId = competitionId, name = "Matéria", externalId = "m", assessedPriorityScore = 70, assessedPrioritySource = PrioritySource.HISTORICAL_EVIDENCE, hasAssessedPriority = true))
        dao.insertTopic(TopicEntity(subjectId = subjectId, title = "Tópico", externalId = "t", assessedPriorityScore = 50, hasAssessedPriority = true))

        val exported = BackupService(database).export()
        assertEquals(7, JSONObject(exported).getInt("version"))
        database.dao().clearCompetitions()
        database.dao().clearSubjects()
        database.dao().clearTopics()
        BackupService(database).restore(exported)

        val restored = database.dao().competitionsOnce().single()
        assertEquals(88, restored.assessedPriorityScore)
        assertEquals(PrioritySource.AI_INFERENCE, restored.assessedPrioritySource)
        assertEquals(PriorityLevel.LOW, restored.userPriorityOverride)
        assertTrue(restored.hasAssessedPriority)
    }

    @Test fun oldBackupGetsNeutralAssessmentDefaults() = runBlocking {
        val dao = database.dao()
        val competitionId = dao.insertCompetition(CompetitionEntity(name = "Concurso", externalId = "c"))
        val subjectId = dao.insertSubject(SubjectEntity(competitionId = competitionId, name = "Matéria", externalId = "m"))
        dao.insertTopic(TopicEntity(subjectId = subjectId, title = "Tópico", externalId = "t"))
        val old = JSONObject(BackupService(database).export()).put("version", 5)
        old.getJSONArray("competitions").getJSONObject(0).remove("assessedPriorityScore")
        old.getJSONArray("competitions").getJSONObject(0).remove("assessedPrioritySource")
        old.getJSONArray("competitions").getJSONObject(0).remove("assessedPriorityConfidence")
        old.getJSONArray("competitions").getJSONObject(0).remove("assessedPriorityRationale")
        old.getJSONArray("competitions").getJSONObject(0).remove("assessedPriorityEvidenceJson")
        old.getJSONArray("competitions").getJSONObject(0).remove("hasAssessedPriority")
        old.getJSONArray("competitions").getJSONObject(0).remove("userPriorityOverride")
        database.dao().clearCompetitions(); database.dao().clearSubjects(); database.dao().clearTopics()

        BackupService(database).restore(old.toString())
        val restored = database.dao().competitionsOnce().single()
        assertEquals(50, restored.assessedPriorityScore)
        assertEquals(PrioritySource.DEFAULT, restored.assessedPrioritySource)
        assertFalse(restored.hasAssessedPriority)
        assertTrue(restored.userPriorityOverride == null)
    }
}
