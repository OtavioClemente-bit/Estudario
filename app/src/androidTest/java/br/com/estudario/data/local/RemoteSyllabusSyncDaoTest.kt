package br.com.estudario.data.local

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RemoteSyllabusSyncDaoTest {
    private lateinit var database: AppDatabase
    private lateinit var dao: AppDao

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext<Context>(),
            AppDatabase::class.java,
        ).allowMainThreadQueries().build()
        dao = database.dao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun enqueueIsIdempotentForTheSamePendingSyllabusPayload() = runBlocking {
        val localId = dao.insertCompetition(CompetitionEntity(name = "Concurso"))
        val mutation = sync(localId, payloadHash = "hash-1", nextAttemptAt = 100L)

        dao.enqueueRemoteSyllabusSync(mutation)
        dao.enqueueRemoteSyllabusSync(mutation.copy(id = 0, createdAt = 200L, updatedAt = 200L))

        val pending = dao.pendingRemoteSyllabusSync(now = 100L)
        assertEquals(1, pending.size)
        assertEquals(mutation.payloadHash, pending.single().payloadHash)
        assertEquals(mutation.createdAt, pending.single().createdAt)
    }

    @Test
    fun pendingRowsAreReturnedInRetryOrderAndFutureRowsWait() = runBlocking {
        val localId = dao.insertCompetition(CompetitionEntity(name = "Concurso"))
        dao.enqueueRemoteSyllabusSync(sync(localId, payloadHash = "late", nextAttemptAt = 300L, createdAt = 30L))
        dao.enqueueRemoteSyllabusSync(sync(localId, payloadHash = "first", nextAttemptAt = 100L, createdAt = 10L))
        dao.enqueueRemoteSyllabusSync(sync(localId, payloadHash = "second", nextAttemptAt = 100L, createdAt = 20L))

        val pending = dao.pendingRemoteSyllabusSync(now = 100L)
        assertEquals(listOf("first", "second"), pending.map { it.payloadHash })
    }

    @Test
    fun attemptAndTerminalMarksPersistRetryMetadataAndState() = runBlocking {
        val localId = dao.insertCompetition(CompetitionEntity(name = "Concurso"))
        val id = dao.enqueueRemoteSyllabusSync(sync(localId, payloadHash = "hash-1", nextAttemptAt = 100L))
        val rowId = if (id == -1L) dao.pendingRemoteSyllabusSync(100L).single().id else id

        dao.markRemoteSyncAttempt(rowId, nextAttemptAt = 250L, updatedAt = 200L)
        val attempted = dao.pendingRemoteSyllabusSync(now = 200L)
        assertTrue(attempted.isEmpty())
        val afterAttempt = dao.remoteSyllabusSyncById(rowId)!!
        assertEquals(1, afterAttempt.attemptCount)
        assertEquals(250L, afterAttempt.nextAttemptAt)

        dao.markRemoteSyncFailed(rowId, error = "network", nextAttemptAt = 500L, updatedAt = 300L)
        assertEquals(RemoteSyllabusSyncState.FAILED, dao.remoteSyllabusSyncById(rowId)!!.state)
        assertEquals("network", dao.remoteSyllabusSyncById(rowId)!!.lastError)

        dao.markRemoteSyncSynced(rowId, updatedAt = 400L)
        assertEquals(RemoteSyllabusSyncState.SYNCED, dao.remoteSyllabusSyncById(rowId)!!.state)
        assertNull(dao.remoteSyllabusSyncById(rowId)!!.lastError)
    }

    @Test
    fun competitionCanBeFoundByItsRemoteSyllabusId() = runBlocking {
        val localId = dao.insertCompetition(CompetitionEntity(name = "Concurso", remoteSyllabusId = "remote-41"))

        val found = dao.competitionByRemoteSyllabusId("remote-41")

        assertEquals(localId, found?.id)
        assertEquals("remote-41", found?.remoteSyllabusId)
    }

    private fun sync(
        localId: Long,
        payloadHash: String,
        nextAttemptAt: Long,
        createdAt: Long = nextAttemptAt,
    ) = RemoteSyllabusSyncEntity(
        operation = RemoteSyllabusSyncOperation.UPSERT,
        localSyllabusId = localId,
        remoteSyllabusId = "remote-$localId",
        jobId = "job-$localId",
        payloadHash = payloadHash,
        nextAttemptAt = nextAttemptAt,
        createdAt = createdAt,
        updatedAt = createdAt,
    )
}
