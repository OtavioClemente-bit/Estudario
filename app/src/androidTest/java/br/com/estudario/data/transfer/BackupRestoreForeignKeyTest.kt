package br.com.estudario.data.transfer

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.com.estudario.data.local.AppDatabase
import br.com.estudario.data.local.CompetitionEntity
import br.com.estudario.data.local.SubjectEntity
import br.com.estudario.data.local.TopicEntity
import kotlinx.coroutines.runBlocking
import org.json.JSONObject
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BackupRestoreForeignKeyTest {
    private lateinit var database: AppDatabase

    @Before fun setup() {
        database = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext<Context>(), AppDatabase::class.java).build()
    }

    @After fun close() { database.close() }

    /** Export ordena por posição: subtópico com posição menor que o pai sai antes dele no arquivo. */
    @Test fun restoresSubtopicListedBeforeItsParent() = runBlocking {
        val dao = database.dao()
        val competitionId = dao.insertCompetition(CompetitionEntity(name = "Concurso"))
        val subjectId = dao.insertSubject(SubjectEntity(competitionId = competitionId, name = "Matéria"))
        val parentId = dao.insertTopic(TopicEntity(subjectId = subjectId, title = "Pai", position = 5))
        dao.insertTopic(TopicEntity(subjectId = subjectId, parentTopicId = parentId, title = "Filho", position = 0))

        val exported = BackupService(database).export()
        BackupService(database).restore(exported)

        val topics = dao.topicsOnce()
        assertEquals(2, topics.size)
        assertEquals(parentId, topics.single { it.title == "Filho" }.parentTopicId)
        // externalId nulo não pode voltar como o texto "null": colide no índice único e um tópico apaga o outro.
        assertTrue(topics.all { it.externalId == null })
    }

    @Test fun dropsOrphanRowsInsteadOfFailingWholeRestore() = runBlocking {
        val dao = database.dao()
        val competitionId = dao.insertCompetition(CompetitionEntity(name = "Concurso"))
        val subjectId = dao.insertSubject(SubjectEntity(competitionId = competitionId, name = "Matéria"))
        dao.insertTopic(TopicEntity(subjectId = subjectId, title = "Tópico"))

        val backup = JSONObject(BackupService(database).export())
        backup.getJSONArray("summaries").put(
            JSONObject().put("id", 999).put("topicId", 424242).put("title", "Órfão").put("markdown", "")
                .put("favorite", false).put("ownNotes", "").put("createdAt", 0).put("updatedAt", 0),
        )
        BackupService(database).restore(backup.toString())

        assertEquals(1, dao.topicsOnce().size)
        assertEquals(0, dao.summariesOnce().size)
    }
}
