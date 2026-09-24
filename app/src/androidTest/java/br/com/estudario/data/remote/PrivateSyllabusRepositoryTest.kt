package br.com.estudario.data.remote

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import br.com.estudario.data.local.AppDatabase
import br.com.estudario.data.local.CompetitionEntity
import br.com.estudario.data.local.RemoteSyllabusSyncEntity
import br.com.estudario.data.local.RemoteSyllabusSyncOperation
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PrivateSyllabusRepositoryTest {
    @Test
    fun remoteOnlyDownloadAndReinstallDownloadRestoreTheSameRemoteIdentity() = runDatabase { database ->
        val remote = remote()
        val api = FakeApi(remote)
        val repository = PrivateSyllabusRepository(database, api)

        val firstId = repository.download(remote.remoteSyllabusId)
        val first = database.dao().competitionsOnce().single()
        assertEquals(firstId, first.id)
        assertEquals(remote.remoteSyllabusId, first.remoteSyllabusId)
        assertEquals("subject-stable", database.dao().subjectsFor(first.id).single().externalId)
        assertEquals("topic-stable", database.dao().topicsFor(database.dao().subjectsFor(first.id).single().id).single().externalId)

        database.dao().deleteCompetition(first)
        assertTrue(api.remoteStillExists)

        val restoredId = repository.download(remote.remoteSyllabusId)
        assertNotEquals(firstId, restoredId)
        assertEquals(remote.remoteSyllabusId, database.dao().competitionsOnce().single().remoteSyllabusId)
    }

    @Test
    fun localDeletionRetainsRemoteAndRemoteDeletionIsExplicit() = runDatabase { database ->
        val remote = remote()
        val api = FakeApi(remote)
        val repository = PrivateSyllabusRepository(database, api)
        val localId = database.dao().insertCompetition(CompetitionEntity(name = "Local", remoteSyllabusId = remote.remoteSyllabusId))
        val deleteRow = RemoteSyllabusSyncEntity(
            operation = RemoteSyllabusSyncOperation.DELETE,
            localSyllabusId = localId,
            remoteSyllabusId = remote.remoteSyllabusId,
            jobId = "delete-job",
            payloadHash = "a".repeat(64),
        )

        database.dao().deleteCompetition(database.dao().competitionsOnce().single())
        assertTrue(api.remoteStillExists)
        assertEquals(0, api.deleteCalls)

        repository.syncOutbox(deleteRow)
        assertEquals(1, api.deleteCalls)
        assertTrue(!api.remoteStillExists)
    }

    private fun remote() = PrivateSyllabus(
        remoteSyllabusId = "remote-fixed",
        title = "Edital remoto",
        position = 0,
        visibility = PrivateSyllabusVisibility.PRIVATE,
        source = PrivateSyllabusSource.IMPORTED,
        sourceJobId = null,
        sourceHash = null,
        schemaVersion = 1,
        status = PrivateSyllabusStatus.ACTIVE,
        metadata = buildJsonObject {
            put("localSyllabusExternalId", JsonPrimitive("remote-fixed"))
            put("payloadHash", JsonPrimitive("a".repeat(64)))
        },
        subjects = listOf(
            PrivateSyllabusSubject(
                remoteSubjectId = "remote-subject",
                externalId = "subject-stable",
                name = "Matéria remota",
                position = 0,
                suggestedPriority = br.com.estudario.data.ai.AiPriority.HIGH,
                packageVersion = "estudo-v2",
                schemaVersion = 1,
                metadata = buildJsonObject { put("marker", JsonPrimitive("subject")) },
                topics = listOf(
                    PrivateSyllabusTopic("remote-topic", "topic-stable", null, "Tópico remoto", 0, "estudo-v2", 1, buildJsonObject { put("marker", JsonPrimitive("topic")) }, emptyList()),
                ),
            ),
        ),
    )

    private fun runDatabase(block: suspend (AppDatabase) -> Unit) = runBlocking {
        val database = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().targetContext,
            AppDatabase::class.java,
        ).allowMainThreadQueries().build()
        try {
            block(database)
        } finally {
            database.close()
        }
    }

    private class FakeApi(initial: PrivateSyllabus) : PrivateSyllabusRemoteApi {
        private var remote: PrivateSyllabus? = initial
        var deleteCalls = 0
        val remoteStillExists: Boolean get() = remote != null

        override suspend fun list(): List<PrivateSyllabus> = listOfNotNull(remote)
        override suspend fun get(remoteSyllabusId: String): PrivateSyllabus? = remote?.takeIf { it.remoteSyllabusId == remoteSyllabusId }
        override suspend fun upsert(syllabus: PrivateSyllabus, mutationId: String, payloadHash: String) = ack(syllabus.remoteSyllabusId, payloadHash)
        override suspend fun delete(remoteSyllabusId: String, mutationId: String, payloadHash: String): RemoteSyllabusSyncAcknowledgement {
            deleteCalls++
            remote = null
            return ack(remoteSyllabusId, payloadHash)
        }

        private fun ack(remoteSyllabusId: String, payloadHash: String) = RemoteSyllabusSyncAcknowledgement(
            remoteSyllabusId = remoteSyllabusId,
            jobId = null,
            payloadHash = payloadHash,
            state = RemoteSyllabusSyncState.SYNCED,
            attemptCount = 1,
            nextAttemptAt = null,
            safeError = null,
            createdAt = "2026-09-24T00:00:00Z",
            updatedAt = "2026-09-24T00:00:00Z",
            attemptToken = null,
        )
    }
}
