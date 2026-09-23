package br.com.estudario.data

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.com.estudario.data.local.AppDatabase
import br.com.estudario.data.local.RemoteSyllabusSyncState
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RemoteSyllabusAssociationTest {
    private lateinit var database: AppDatabase

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext<Context>(),
            AppDatabase::class.java,
        ).allowMainThreadQueries().build()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun associationAndPendingMutationAreCommittedTogether() = runBlocking {
        val localId = database.dao().insertCompetition(br.com.estudario.data.local.CompetitionEntity(name = "Concurso"))
        val repository = StudyRepository(database)

        repository.associateCompetitionWithRemoteSyllabus(
            competitionId = localId,
            remoteSyllabusId = "remote-41",
            jobId = "job-41",
            payloadHash = "payload-41",
            now = 100L,
        )

        assertEquals("remote-41", database.dao().competitionsOnce().single().remoteSyllabusId)
        val outbox = database.dao().pendingRemoteSyllabusSync(100L).single()
        assertEquals(localId, outbox.localSyllabusId)
        assertEquals("job-41", outbox.jobId)
        assertEquals("payload-41", outbox.payloadHash)
        assertEquals(RemoteSyllabusSyncState.PENDING, outbox.state)
        assertNotNull(outbox.id)
    }
}
