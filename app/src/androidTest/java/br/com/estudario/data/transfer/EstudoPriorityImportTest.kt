package br.com.estudario.data.transfer

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.com.estudario.data.local.AppDatabase
import br.com.estudario.data.local.CompetitionEntity
import br.com.estudario.data.local.SubjectEntity
import br.com.estudario.data.local.TopicEntity
import br.com.estudario.domain.PriorityLevel
import br.com.estudario.domain.PrioritySource
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class EstudoPriorityImportTest {
    private lateinit var database: AppDatabase

    @Before fun setup() {
        database = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext<Context>(), AppDatabase::class.java).build()
    }

    @After fun close() { database.close() }

    @Test fun automaticAssessmentUpdatesButManualOverrideSurvives() = runBlocking {
        val dao = database.dao()
        val competitionId = dao.insertCompetition(CompetitionEntity(name = "Concurso", externalId = "c"))
        val subjectId = dao.insertSubject(SubjectEntity(competitionId = competitionId, name = "Matéria", externalId = "m"))
        val topicId = dao.insertTopic(TopicEntity(subjectId = subjectId, title = "Tópico", externalId = "t", assessedPriorityScore = 70, assessedPrioritySource = PrioritySource.HISTORICAL_EVIDENCE, hasAssessedPriority = true, userPriorityOverride = PriorityLevel.LOW))
        val service = EstudoPackageService(database)

        service.import(packageJson)
        val imported = dao.topic(topicId) ?: error("topic missing")
        assertEquals(95, imported.assessedPriorityScore)
        assertEquals(PrioritySource.OFFICIAL_EXAM_STRUCTURE, imported.assessedPrioritySource)
        assertEquals(PriorityLevel.LOW, imported.userPriorityOverride)

        service.import(packageJsonWithoutAssessment, ImportMode.UPDATE)
        val preserved = dao.topic(topicId) ?: error("topic missing")
        assertEquals(95, preserved.assessedPriorityScore)
        assertEquals(PriorityLevel.LOW, preserved.userPriorityOverride)
    }

    @Test fun newTopicWithoutAssessmentDoesNotClaimAutomaticPriority() = runBlocking {
        val service = EstudoPackageService(database)
        service.import(packageJsonWithoutAssessment)
        val topic = database.dao().topicsOnce().single()
        assertFalse(topic.hasAssessedPriority)
        assertEquals(50, topic.assessedPriorityScore)
        assertTrue(topic.userPriorityOverride == null)
    }

    private val packageJsonWithoutAssessment = """
        {"version":2,"packageId":"priority-import","concurso":{"id":"c","nome":"Concurso"},
        "materias":[{"id":"m","nome":"Matéria","topicos":[{"id":"t","titulo":"Tópico","ordem":0,"teorias":[],"resumos":[],"questoes":[],"subtopicos":[]}]}]}
    """.trimIndent()

    private val packageJson = packageJsonWithoutAssessment.replace(
        "\"id\":\"t\",\"titulo\":\"Tópico\"",
        "\"id\":\"t\",\"titulo\":\"Tópico\",\"priorityAssessment\":{\"score\":95,\"source\":\"OFFICIAL_EXAM_STRUCTURE\",\"confidence\":0.9,\"rationale\":\"Peso oficial\",\"evidence\":[]}",
    )
}
