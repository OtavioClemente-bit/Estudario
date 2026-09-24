package br.com.estudario.data.local

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
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
    fun canonicalEstudoSnapshotRoundTripsWithExistingOutbox() = runBlocking {
        val localId = dao.insertCompetition(CompetitionEntity(name = "Concurso"))
        val snapshot = "{\"version\":2,\"documentTitle\":\"Concurso\"}"
        val rowId = dao.enqueueRemoteSyllabusSync(
            sync(localId, payloadHash = "hash-canonical", nextAttemptAt = 100L).copy(payloadJson = snapshot),
        )

        assertEquals(snapshot, dao.remoteSyllabusSyncById(rowId)!!.payloadJson)
    }

    @Test
    fun supersededRowsKeepSnapshotsAndRejectOldWorkerCas() = runBlocking {
        val localId = dao.insertCompetition(CompetitionEntity(name = "Concurso"))
        val pendingId = dao.enqueueRemoteSyllabusSync(
            sync(localId, payloadHash = "old-pending", nextAttemptAt = 100L).copy(
                payloadJson = "{\"payload\":\"pending\"}",
            ),
        )
        assertEquals(
            1,
            dao.markRemoteSyncAttempt(
                pendingId,
                expectedAttemptToken = "",
                attemptToken = "old-pending-token",
                nextAttemptAt = 200L,
                updatedAt = 150L,
            ),
        )
        val failedId = dao.enqueueRemoteSyllabusSync(
            sync(localId, payloadHash = "old-failed", nextAttemptAt = 100L).copy(
                payloadJson = "{\"payload\":\"failed\"}",
                state = RemoteSyllabusSyncState.FAILED,
                attemptToken = "old-failed-token",
                lastError = RemoteSyllabusSyncError.NETWORK,
            ),
        )

        assertEquals(
            2,
            dao.supersedeRemoteSyllabusSync(
                localSyllabusId = localId,
                remoteSyllabusId = "remote-$localId",
                supersessionToken = "replacement-token",
                updatedAt = 300L,
            ),
        )

        val replacementId = dao.enqueueRemoteSyllabusSync(
            sync(localId, payloadHash = "replacement", nextAttemptAt = 300L).copy(
                payloadJson = "{\"payload\":\"replacement\"}",
            ),
        )
        assertEquals(listOf(replacementId), dao.pendingRemoteSyllabusSync(now = 300L).map { it.id })
        assertTrue(dao.failedRemoteSyllabusSync(now = 300L).isEmpty())
        assertEquals(0, dao.requeueRemoteSync(failedId, "old-failed-token", "late-retry", 400L, 400L))
        assertEquals(0, dao.markRemoteSyncSynced(pendingId, "old-pending-token", 500L))
        assertEquals(0, dao.markRemoteSyncFailed(pendingId, "old-pending-token", "late completion", 600L, 600L))

        val supersededPending = dao.remoteSyllabusSyncById(pendingId)!!
        val supersededFailed = dao.remoteSyllabusSyncById(failedId)!!
        assertEquals(RemoteSyllabusSyncState.FAILED, supersededPending.state)
        assertEquals(RemoteSyllabusSyncError.SUPERSEDED, supersededPending.lastError)
        assertEquals("{\"payload\":\"pending\"}", supersededPending.payloadJson)
        assertEquals("replacement-token", supersededPending.attemptToken)
        assertEquals(RemoteSyllabusSyncState.FAILED, supersededFailed.state)
        assertEquals(RemoteSyllabusSyncError.SUPERSEDED, supersededFailed.lastError)
        assertEquals("{\"payload\":\"failed\"}", supersededFailed.payloadJson)
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

        assertEquals(1, dao.markRemoteSyncAttempt(rowId, expectedAttemptToken = "", attemptToken = "attempt-1", nextAttemptAt = 250L, updatedAt = 200L))
        val attempted = dao.pendingRemoteSyllabusSync(now = 200L)
        assertTrue(attempted.isEmpty())
        val afterAttempt = dao.remoteSyllabusSyncById(rowId)!!
        assertEquals(1, afterAttempt.attemptCount)
        assertEquals(250L, afterAttempt.nextAttemptAt)

        assertEquals(1, dao.markRemoteSyncFailed(rowId, attemptToken = "attempt-1", error = "network", nextAttemptAt = 500L, updatedAt = 300L))
        val failed = dao.remoteSyllabusSyncById(rowId)!!
        assertEquals(RemoteSyllabusSyncState.FAILED, failed.state)
        assertEquals(RemoteSyllabusSyncError.NETWORK, failed.lastError)

        assertEquals(0, dao.markRemoteSyncSynced(rowId, attemptToken = "attempt-1", updatedAt = 400L))
        assertEquals(0, dao.markRemoteSyncFailed(rowId, attemptToken = "attempt-1", error = "late worker", nextAttemptAt = 600L, updatedAt = 500L))
        assertEquals(RemoteSyllabusSyncState.FAILED, dao.remoteSyllabusSyncById(rowId)!!.state)
    }

    @Test
    fun failedRowsRequeueOnlyAfterBackoffAndReturnToPending() = runBlocking {
        val localId = dao.insertCompetition(CompetitionEntity(name = "Concurso"))
        val rowId = dao.enqueueRemoteSyllabusSync(sync(localId, payloadHash = "hash-1", nextAttemptAt = 100L))

        dao.markRemoteSyncAttempt(rowId, expectedAttemptToken = "", attemptToken = "attempt-1", nextAttemptAt = 250L, updatedAt = 200L)
        dao.markRemoteSyncFailed(
            rowId,
            attemptToken = "attempt-1",
            error = "HTTP 503 Authorization: test-secret",
            nextAttemptAt = 500L,
            updatedAt = 300L,
        )
        val persistedFailure = dao.remoteSyllabusSyncById(rowId)!!
        assertEquals(RemoteSyllabusSyncError.AUTH, persistedFailure.lastError)
        assertFalse(persistedFailure.lastError.orEmpty().contains("test-secret"))

        assertTrue(dao.pendingRemoteSyllabusSync(now = 499L).isEmpty())
        assertTrue(dao.failedRemoteSyllabusSync(now = 499L).isEmpty())
        assertEquals(0, dao.requeueRemoteSync(rowId, expectedAttemptToken = "attempt-1", attemptToken = "attempt-2", now = 499L, updatedAt = 499L))
        assertEquals(1, dao.failedRemoteSyllabusSync(now = 500L).size)
        assertEquals(1, dao.requeueRemoteSync(rowId, expectedAttemptToken = "attempt-1", attemptToken = "attempt-2", now = 500L, updatedAt = 500L))

        val requeued = dao.pendingRemoteSyllabusSync(now = 500L).single()
        assertEquals(RemoteSyllabusSyncState.PENDING, requeued.state)
        assertEquals(500L, requeued.nextAttemptAt)
        assertNull(requeued.lastError)
    }

    @Test
    fun staleWorkersCannotOverwriteSyncedRows() = runBlocking {
        val localId = dao.insertCompetition(CompetitionEntity(name = "Concurso"))
        val rowId = dao.enqueueRemoteSyllabusSync(sync(localId, payloadHash = "hash-1", nextAttemptAt = 100L))

        assertEquals(1, dao.markRemoteSyncAttempt(rowId, expectedAttemptToken = "", attemptToken = "attempt-1", nextAttemptAt = 150L, updatedAt = 150L))
        assertEquals(1, dao.markRemoteSyncSynced(rowId, attemptToken = "attempt-1", updatedAt = 200L))
        assertEquals(0, dao.markRemoteSyncSynced(rowId, attemptToken = "attempt-1", updatedAt = 300L))
        assertEquals(0, dao.markRemoteSyncFailed(rowId, attemptToken = "attempt-1", error = "late network", nextAttemptAt = 400L, updatedAt = 300L))
        assertEquals(0, dao.markRemoteSyncAttempt(rowId, expectedAttemptToken = "attempt-1", attemptToken = "attempt-2", nextAttemptAt = 500L, updatedAt = 400L))
        val synced = dao.remoteSyllabusSyncById(rowId)!!
        assertEquals(RemoteSyllabusSyncState.SYNCED, synced.state)
        assertNull(synced.lastError)
    }

    @Test
    fun sameMutationCannotBeEnqueuedAgainAfterFailure() = runBlocking {
        val localId = dao.insertCompetition(CompetitionEntity(name = "Concurso"))
        val mutation = sync(localId, payloadHash = "hash-1", nextAttemptAt = 100L)
        val rowId = dao.enqueueRemoteSyllabusSync(mutation)
        dao.markRemoteSyncAttempt(rowId, expectedAttemptToken = "", attemptToken = "attempt-1", nextAttemptAt = 200L, updatedAt = 150L)
        dao.markRemoteSyncFailed(rowId, attemptToken = "attempt-1", error = "network", nextAttemptAt = 500L, updatedAt = 200L)

        assertEquals(-1L, dao.enqueueRemoteSyllabusSync(mutation.copy(id = 0, state = RemoteSyllabusSyncState.PENDING)))
        assertEquals(1, dao.failedRemoteSyllabusSync(now = 500L).size)
        assertTrue(dao.pendingRemoteSyllabusSync(now = 500L).isEmpty())
    }

    @Test
    fun staleWorkerCannotCompleteAfterFailedRowIsRequeued() = runBlocking {
        val localId = dao.insertCompetition(CompetitionEntity(name = "Concurso"))
        val rowId = dao.enqueueRemoteSyllabusSync(sync(localId, payloadHash = "hash-1", nextAttemptAt = 100L))

        assertEquals(1, dao.markRemoteSyncAttempt(rowId, expectedAttemptToken = "", attemptToken = "attempt-1", nextAttemptAt = 150L, updatedAt = 150L))
        assertEquals(1, dao.markRemoteSyncFailed(rowId, attemptToken = "attempt-1", error = "network", nextAttemptAt = 500L, updatedAt = 200L))
        assertEquals(1, dao.requeueRemoteSync(rowId, expectedAttemptToken = "attempt-1", attemptToken = "attempt-2", now = 500L, updatedAt = 500L))

        assertEquals(0, dao.markRemoteSyncSynced(rowId, attemptToken = "attempt-1", updatedAt = 600L))
        assertEquals(0, dao.markRemoteSyncFailed(rowId, attemptToken = "attempt-1", error = "late network", nextAttemptAt = 700L, updatedAt = 600L))
        assertEquals(RemoteSyllabusSyncState.PENDING, dao.remoteSyllabusSyncById(rowId)!!.state)

        assertEquals(1, dao.markRemoteSyncAttempt(rowId, expectedAttemptToken = "attempt-2", attemptToken = "attempt-3", nextAttemptAt = 650L, updatedAt = 650L))
        assertEquals(1, dao.markRemoteSyncSynced(rowId, attemptToken = "attempt-3", updatedAt = 700L))
    }

    @Test
    fun deletingCompetitionCascadesItsOutboxRows() = runBlocking {
        val competition = CompetitionEntity(name = "Concurso")
        val localId = dao.insertCompetition(competition)
        val rowId = dao.enqueueRemoteSyllabusSync(sync(localId, payloadHash = "hash-1", nextAttemptAt = 100L))

        dao.deleteCompetition(competition.copy(id = localId))

        assertNull(dao.remoteSyllabusSyncById(rowId))
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
