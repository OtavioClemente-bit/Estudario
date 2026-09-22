package br.com.estudario.data

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.com.estudario.data.local.AppDatabase
import br.com.estudario.data.local.CompetitionEntity
import br.com.estudario.data.local.StudyQueueEntity
import br.com.estudario.data.local.SubjectEntity
import br.com.estudario.data.local.TopicEntity
import br.com.estudario.data.local.TopicStatus
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class StudyRepositoryCompletionTest {
    private lateinit var db: AppDatabase
    private lateinit var repository: StudyRepository
    private var topicId = 0L

    @Before fun setup() = runBlocking {
        db = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(), AppDatabase::class.java).allowMainThreadQueries().build()
        val competition = db.dao().insertCompetition(CompetitionEntity(name = "TRF"))
        val subject = db.dao().insertSubject(SubjectEntity(competitionId = competition, name = "Direito"))
        topicId = db.dao().insertTopic(TopicEntity(subjectId = subject, title = "Constitucional"))
        db.dao().insertQueue(StudyQueueEntity(topicId = topicId, position = 0))
        repository = StudyRepository(db)
    }

    @After fun close() = db.close()

    @Test fun completingTopicIsIdempotentAndRemovesItFromQueue() = runBlocking {
        repository.completeStudy(topicId, 1)
        repository.completeStudy(topicId, 2)

        assertEquals(TopicStatus.ESTUDADO, db.dao().topic(topicId)?.status)
        assertEquals(0, db.dao().queueOnce().size)
        assertEquals(1, db.dao().studySessionCount(topicId))

        repository.unmarkStudied(db.dao().topic(topicId)!!)
        repository.completeStudy(topicId, 3)
        assertEquals(1, db.dao().studySessionCount(topicId))
    }
}
