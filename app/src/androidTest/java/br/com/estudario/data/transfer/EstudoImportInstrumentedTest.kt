package br.com.estudario.data.transfer

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.com.estudario.data.local.AppDatabase
import br.com.estudario.data.local.CompetitionEntity
import br.com.estudario.data.StudyRepository
import br.com.estudario.data.local.ReviewHistoryEntity
import br.com.estudario.data.local.StudyQueueEntity
import br.com.estudario.data.local.StudySessionEntity
import br.com.estudario.data.local.UserNoteEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class EstudoImportInstrumentedTest {
    private lateinit var database: AppDatabase

    @Before fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
    }

    @After fun close() { database.close() }

    @Test fun importsHierarchyAndContentWithoutDuplicating() = runBlocking {
        val service = EstudoPackageService(database)
        val first = service.import(packageJson)
        assertEquals(1, first.subjectsCreated)
        assertEquals(2, first.topicsCreated)
        assertEquals(1, first.theories)
        assertEquals(1, first.summaries)
        assertEquals(1, first.questions)
        assertEquals(2, database.dao().topicsOnce().size)
        assertEquals(1, database.dao().questionsOnce().size)
        assertEquals(1, database.dao().theoriesOnce().size)

        val preview = service.preview(packageJson)
        assertEquals(3, preview.duplicateCount)
        val second = service.import(packageJson)
        assertEquals(3, second.skipped)
        assertTrue(second.topicsUpdated >= 2)
        assertEquals(1, database.dao().questionsOnce().size)
    }

    @Test fun selectedCompetitionReceivesImportedContentWhenFileHasAnotherName() = runBlocking {
        val dao = database.dao()
        val chosenName = "Nome escolhido"
        val chosenId = dao.insertCompetition(CompetitionEntity(name = chosenName))
        val service = EstudoPackageService(database)

        service.import(packageJson.replace("Concurso Instrumentado", "Nome vindo do arquivo"), targetCompetitionId = chosenId)

        assertEquals(1, dao.competitionsOnce().size)
        assertEquals(chosenName, dao.competitionsOnce().single().name)
        assertEquals(listOf(chosenId), dao.subjectsOnce().map { it.competitionId }.distinct())
    }

    @Test fun selectedExistingCompetitionUpdatesNameMatchedEntitiesWithImportedIdentityAndParents() = runBlocking {
        val dao = database.dao()
        val targetId = dao.insertCompetition(CompetitionEntity(name = "Edital selecionado", externalId = "target-existing"))
        val oldSubjectId = dao.insertSubject(br.com.estudario.data.local.SubjectEntity(competitionId = targetId, name = "Matéria", position = 99, externalId = "old-subject"))
        val oldRootId = dao.insertTopic(br.com.estudario.data.local.TopicEntity(subjectId = oldSubjectId, title = "Tópico", position = 99, externalId = "old-root"))
        dao.insertTopic(br.com.estudario.data.local.TopicEntity(subjectId = oldSubjectId, parentTopicId = oldRootId, title = "Subtópico", position = 99, externalId = "old-child"))

        val imported = packageJson
            .replace("\"id\":\"m\",\"nome\":\"Matéria\"", "\"id\":\"package-subject-id\",\"externalId\":\"import-subject\",\"nome\":\"Matéria\",\"ordem\":4")
            .replace("\"id\":\"t\",\"titulo\":\"Tópico\"", "\"id\":\"package-root-id\",\"externalId\":\"import-root\",\"titulo\":\"Tópico\",\"ordem\":2")
            .replace("\"id\":\"st\",\"titulo\":\"Subtópico\"", "\"id\":\"package-child-id\",\"externalId\":\"import-child\",\"parentExternalId\":\"import-root\",\"titulo\":\"Subtópico\",\"ordem\":3")

        EstudoPackageService(database).import(imported, targetCompetitionId = targetId)

        assertEquals(1, dao.competitionsOnce().size)
        assertEquals("Edital selecionado", dao.competitionsOnce().single().name)
        assertEquals(targetId, dao.competitionsOnce().single().id)
        val subject = dao.subjectsOnce().single()
        assertEquals(targetId, subject.competitionId)
        assertEquals("import-subject", subject.externalId)
        assertEquals(4, subject.position)
        val topics = dao.topicsOnce().sortedBy { it.position }
        assertEquals(listOf("import-root", "import-child"), topics.map { it.externalId })
        assertEquals(listOf(2, 3), topics.map { it.position })
        assertEquals(null, topics[0].parentTopicId)
        assertEquals(topics[0].id, topics[1].parentTopicId)
    }

    @Test fun updateModeChangesContentAndPreservesStudyHistory() = runBlocking {
        val service = EstudoPackageService(database)
        service.import(packageJson)
        val dao = database.dao()
        val question = dao.questionsOnce().single().question
        val theory = dao.theoriesOnce().single()
        dao.updateQuestion(question.copy(answerCount = 7, correctCount = 5, isFavorite = true))
        dao.updateTheory(theory.copy(lastReadBlock = 4))

        val changed = packageJson.replace("Pergunta?", "Pergunta atualizada?").replace("Texto da teoria.", "Teoria atualizada.")
        val result = service.import(changed, ImportMode.UPDATE)

        assertTrue(result.updatedContent >= 3)
        assertEquals("Pergunta atualizada?", dao.questionsOnce().single().question.statement)
        assertEquals(7, dao.questionsOnce().single().question.answerCount)
        assertTrue(dao.questionsOnce().single().question.isFavorite)
        assertEquals(4, dao.theoriesOnce().single().lastReadBlock)
    }

    @Test fun anotherDailyPackageTargetsSameStableLeafWithoutDuplicatingTree() = runBlocking {
        val service = EstudoPackageService(database)
        service.import(packageJson)
        val daily = packageJson.replace("instrumented-v2", "daily-content-v2").replace("Texto da teoria.", "Conteúdo diário atualizado.")
        service.import(daily, ImportMode.UPDATE)
        assertEquals(2, database.dao().topicsOnce().size)
        assertEquals("Subtópico", database.dao().topicsOnce().single { it.externalId == "st" }.title)
        assertTrue(database.dao().theoriesOnce().single().markdown.contains("Conteúdo diário atualizado."))
    }

    @Test fun deletingCompetitionRemovesOrphansAndKeepsAppFlowsUsable() = runBlocking {
        val service = EstudoPackageService(database)
        service.import(packageJson)
        val dao = database.dao()
        val competition = dao.competitionsOnce().single()
        val topicId = dao.topicsOnce().last().id
        dao.insertQueue(StudyQueueEntity(topicId = topicId, position = 0))
        dao.insertReviewHistory(ReviewHistoryEntity(topicId = topicId))
        dao.insertStudySession(StudySessionEntity(topicId = topicId, startedAt = System.currentTimeMillis()))
        dao.restoreNotes(listOf(UserNoteEntity(topicId = topicId, text = "nota")))

        StudyRepository(database).deleteCompetition(competition)

        assertTrue(dao.competitionsOnce().isEmpty())
        assertTrue(dao.queue().first().isEmpty())
        assertTrue(dao.queueOnce().isEmpty())
        assertTrue(dao.reviewHistoryOnce().isEmpty())
        assertTrue(dao.sessionsOnce().isEmpty())
        assertTrue(dao.notesOnce().isEmpty())
    }

    private val packageJson = """
        {
          "version":2,"packageId":"instrumented-v2",
          "concurso":{"nome":"Concurso Instrumentado","principal":true},
          "materias":[{"id":"m","nome":"Matéria","topicos":[{
            "id":"t","titulo":"Tópico","resumos":[],"questoes":[],
            "subtopicos":[{
              "id":"st","titulo":"Subtópico",
              "teorias":[{"id":"th1","titulo":"Livro","capitulos":[{"id":"c1","titulo":"Capítulo","markdown":"Texto da teoria."}]}],
              "resumos":[{"id":"r1","titulo":"Resumo","markdown":"# Conteúdo"}],
              "questoes":[{"id":"q1","enunciado":"Pergunta?","explicacao":"Explicação.",
                "alternativas":[{"chave":"A","texto":"Certa","correta":true},{"chave":"B","texto":"Errada","correta":false}]
              }]
            }]
          }]}]
        }
    """.trimIndent()
}
