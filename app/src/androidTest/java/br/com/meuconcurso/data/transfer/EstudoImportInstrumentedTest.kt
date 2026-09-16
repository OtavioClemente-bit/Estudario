package br.com.meuconcurso.data.transfer

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.com.meuconcurso.data.local.AppDatabase
import br.com.meuconcurso.data.StudyRepository
import br.com.meuconcurso.data.local.ReviewHistoryEntity
import br.com.meuconcurso.data.local.StudyQueueEntity
import br.com.meuconcurso.data.local.StudySessionEntity
import br.com.meuconcurso.data.local.UserNoteEntity
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
