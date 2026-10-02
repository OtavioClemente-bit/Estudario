package br.com.estudario.data.transfer

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.com.estudario.data.ai.AiContentEstudo
import br.com.estudario.data.local.AppDatabase
import br.com.estudario.data.local.CompetitionEntity
import kotlinx.coroutines.runBlocking
import org.json.JSONArray
import org.json.JSONObject
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Material gerado para um tópico entra no tópico certo, sem criar cópias, mesmo quando dois
 * editais lidos pela IA usam os mesmos ids curtos (s1, t1...).
 */
@RunWith(AndroidJUnit4::class)
class ContentImportDuplicationTest {
    private lateinit var database: AppDatabase

    @Before fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
    }

    @After fun close() { database.close() }

    private fun syllabus(name: String, p: String = name.lowercase()) = """
        {"version":2,"packageId":"syllabus-$name","concurso":{"id":"$p-c1","nome":"$name"},
         "materias":[{"id":"$p-s1","nome":"Matemática","topicos":[
           {"id":"$p-t1","titulo":"Funções: afim, quadrática e exponencial","subtopicos":[
             {"id":"$p-t1-1","titulo":"Função afim","contentOriginType":"DIDACTIC_SUBDIVISION"},
             {"id":"$p-t1-2","titulo":"Função quadrática","contentOriginType":"DIDACTIC_SUBDIVISION"}
           ]},
           {"id":"$p-t2","titulo":"Porcentagem"}
         ]}]}
    """.trimIndent()

    private val proposal = JSONObject()
        .put("theoryTitle", "Função afim")
        .put("chapters", JSONArray().put(JSONObject().put("title", "1. Fundamentos").put("markdown", "Texto.")))
        .put("summary", "Resumo.")
        .put("quickReview", "")
        .put("tips", JSONArray()).put("traps", JSONArray()).put("activeRecall", JSONArray())
        .put("errorConcepts", JSONArray()).put("questions", JSONArray()).put("sources", JSONArray())

    @Test fun materialEntraNoTopicoSemDuplicar() = runBlocking {
        val dao = database.dao()
        val service = EstudoPackageService(database)
        val a = dao.insertCompetition(CompetitionEntity(name = "PMMG"))
        val b = dao.insertCompetition(CompetitionEntity(name = "PCMG"))
        service.importInTransaction(syllabus("PMMG"), ImportMode.SKIP, a)
        service.importInTransaction(syllabus("PCMG"), ImportMode.SKIP, b)
        val before = dao.topicsOnce().size

        val competition = dao.competitionsOnce().first { it.id == a }
        val subject = dao.subjectsFor(a).single()
        val topics = dao.topicsFor(subject.id)
        val target = topics.first { it.title == "Função afim" }
        val estudo = AiContentEstudo.build(competition, subject, topics, target, proposal)
        service.importInTransaction(estudo, ImportMode.SKIP, a)

        assertEquals("nenhum tópico novo", before, dao.topicsOnce().size)
        assertEquals(1, dao.theoriesOnce().count { it.topicId == target.id })
    }

    @Test fun topicosCriadosAMaoNaoDuplicam() = runBlocking {
        val dao = database.dao()
        val service = EstudoPackageService(database)
        val competitionId = dao.insertCompetition(CompetitionEntity(name = "Polícia Federal"))
        val subjectId = dao.insertSubject(br.com.estudario.data.local.SubjectEntity(competitionId = competitionId, name = "Física"))
        val parent = dao.insertTopic(br.com.estudario.data.local.TopicEntity(subjectId = subjectId, title = "Cinemática "))
        dao.insertTopic(br.com.estudario.data.local.TopicEntity(subjectId = subjectId, parentTopicId = parent, title = "Movimento uniforme"))
        val before = dao.topicsOnce().size

        val competition = dao.competitionsOnce().first { it.id == competitionId }
        val subject = dao.subjectsFor(competitionId).single()
        val topics = dao.topicsFor(subjectId)
        val target = topics.first { it.title == "Movimento uniforme" }
        service.importInTransaction(AiContentEstudo.build(competition, subject, topics, target, proposal), ImportMode.SKIP, competitionId)

        assertEquals("nenhum tópico novo", before, dao.topicsOnce().size)
        assertEquals(1, dao.subjectsFor(competitionId).size)
        assertEquals(1, dao.theoriesOnce().count { it.topicId == target.id })
    }
}
