package br.com.estudario.data.remote

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import br.com.estudario.data.local.AppDatabase
import br.com.estudario.data.local.CompetitionEntity
import br.com.estudario.data.local.RemoteSyllabusSyncEntity
import br.com.estudario.data.local.RemoteSyllabusSyncOperation
import br.com.estudario.data.local.RemoteSyllabusSyncState as LocalRemoteSyllabusSyncState
import br.com.estudario.data.transfer.EstudoPackageService
import br.com.estudario.data.transfer.ImportMode
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.withLock
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import java.security.MessageDigest

@RunWith(AndroidJUnit4::class)
class PrivateSyllabusRepositoryTest {
    @Test
    fun officialTreeOutboxAtomicRpcFetchAndReinstallPreserveEveryIdentityField() = runDatabase { database ->
        val packageJson = officialPackage()
        val payloadHash = sha256(packageJson)
        val originalId = database.dao().insertCompetition(
            CompetitionEntity(id = 41L, name = "Edital oficial", externalId = "competition-official"),
        )
        assertEquals(41L, originalId)
        val importResult = EstudoPackageService(database).import(packageJson, ImportMode.SKIP, targetCompetitionId = 41L)
        assertEquals(41L, importResult.competitionId)
        val originalTree = tree(database, 41L)
        val outboxId = database.dao().enqueueRemoteSyllabusSync(
            RemoteSyllabusSyncEntity(
                operation = RemoteSyllabusSyncOperation.UPSERT,
                localSyllabusId = 41L,
                payloadHash = payloadHash,
                payloadJson = packageJson,
                jobId = "mutation-round-trip",
            ),
        )
        val outbox = database.dao().remoteSyllabusSyncById(outboxId)!!
        assertNotNull(outbox)

        val backend = TransactionalPrivateSyllabusBackend()
        val repository = PrivateSyllabusRepository(database, backend)
        val concurrentAcknowledgements = coroutineScope {
            listOf(
                async { repository.syncOutbox(outbox) },
                async { repository.syncOutbox(outbox) },
            ).awaitAll()
        }
        assertEquals(2, concurrentAcknowledgements.size)
        assertEquals(concurrentAcknowledgements[0], concurrentAcknowledgements[1])
        assertEquals("SYNCED", concurrentAcknowledgements[0].state.name)
        assertEquals(concurrentAcknowledgements[0].remoteSyllabusId, backend.remote!!.remoteSyllabusId)
        assertEquals(1, backend.committedMutations)

        val hashConflict = runCatching {
            backend.upsert(backend.remote!!, "mutation-round-trip", "b".repeat(64))
        }.exceptionOrNull()
        assertTrue(hashConflict is PrivateSyllabusApiException)
        assertEquals("IDEMPOTENCY_KEY_CONFLICT", (hashConflict as PrivateSyllabusApiException).code)

        val gateway = RoomGateway(database, repository)
        val runner = RemoteSyllabusSyncRunner(gateway, now = { 100L }, tokenFactory = { "attempt-round-trip" })
        val runResult = runner.run()
        assertEquals(1, runResult.synced)
        val syncedCompetition = database.dao().competitionById(41L)!!
        val syncedOutbox = database.dao().remoteSyllabusSyncById(outboxId)!!
        assertEquals(backend.remote!!.remoteSyllabusId, syncedCompetition.remoteSyllabusId)
        assertEquals(backend.remote!!.remoteSyllabusId, syncedOutbox.remoteSyllabusId)
        assertEquals(LocalRemoteSyllabusSyncState.SYNCED, syncedOutbox.state)
        assertEquals("", syncedOutbox.lastError ?: "")

        val fetched = repository.getRemote(backend.remote!!.remoteSyllabusId)!!
        val fetchedPackage = RemoteSyllabusMapper.toOfficialPackage(fetched)
        assertEquals(payloadHash, sha256(fetchedPackage))
        assertEquals(backend.remote, RemoteSyllabusMapper.fromLocal(syncedCompetition, packageJson, payloadHash))

        database.dao().deleteCompetition(syncedCompetition)
        assertTrue(backend.remote != null)
        val restoredId = repository.download(backend.remote!!.remoteSyllabusId)
        val restored = database.dao().competitionById(restoredId)!!
        assertEquals("competition-official", restored.externalId)
        assertEquals(backend.remote!!.remoteSyllabusId, restored.remoteSyllabusId)
        assertEquals(originalTree, tree(database, restoredId))
        assertEquals(payloadHash, sha256(RemoteSyllabusMapper.toOfficialPackage(backend.remote!!)))
    }

    @Test
    fun localDeletionRetainsRemoteAndRemoteDeletionIsExplicit() = runDatabase { database ->
        val packageJson = officialPackage()
        val remote = RemoteSyllabusMapper.fromLocal(
            CompetitionEntity(id = 41L, name = "Edital oficial", externalId = "competition-official"),
            packageJson,
            sha256(packageJson),
        )
        val backend = TransactionalPrivateSyllabusBackend(remote)
        val repository = PrivateSyllabusRepository(database, backend)
        val localId = database.dao().insertCompetition(CompetitionEntity(name = "Local", remoteSyllabusId = remote.remoteSyllabusId))
        val deleteRow = RemoteSyllabusSyncEntity(
            operation = RemoteSyllabusSyncOperation.DELETE,
            localSyllabusId = localId,
            remoteSyllabusId = remote.remoteSyllabusId,
            jobId = "delete-job",
            payloadHash = "a".repeat(64),
        )

        database.dao().deleteCompetition(database.dao().competitionById(localId)!!)
        assertTrue(backend.remote != null)
        repository.syncOutbox(deleteRow)
        assertTrue(backend.remote == null)
    }

    private suspend fun tree(database: AppDatabase, competitionId: Long): TreeSnapshot {
        val subjects = database.dao().subjectsFor(competitionId).sortedBy { it.position }.map { subject ->
            val topics = database.dao().topicsFor(subject.id).sortedBy { it.position }
            val externalById = topics.associate { it.id to it.externalId }
            SubjectSnapshot(subject.externalId, subject.name, subject.position, topics.map { topic ->
                TopicSnapshot(topic.externalId, topic.title, topic.position, topic.priority.name, externalById[topic.parentTopicId])
            })
        }
        return TreeSnapshot(database.dao().competitionById(competitionId)!!.externalId, subjects)
    }

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

    private fun officialPackage() = """
        {"version":2,"packageId":"pkg-round-trip","packageVersion":"estudo-v9","schemaVersion":7,
        "metadata":{"marker":"root","stableIdentity":"competition-official"},
        "concurso":{"id":"competition-official","nome":"Edital oficial","principal":false},
        "materias":[{"id":"subject-internal","externalId":"subject-official","nome":"Direito","ordem":1,"prioridade":"ALTA","metadata":{"marker":"subject"},
        "topicos":[{"id":"topic-internal","externalId":"topic-official","titulo":"Constitucional","ordem":2,"prioridade":"BAIXA","metadata":{"marker":"topic"},
        "subtopicos":[{"id":"child-internal","externalId":"child-official","parentExternalId":"topic-official","titulo":"Direitos","ordem":0,"prioridade":"NORMAL","metadata":{"marker":"child"}}]}]}]}
    """.trimIndent().replace("\n", "")

    private fun sha256(value: String): String = MessageDigest.getInstance("SHA-256")
        .digest(value.toByteArray())
        .joinToString("") { "%02x".format(it) }

    private data class TreeSnapshot(val externalId: String?, val subjects: List<SubjectSnapshot>)
    private data class SubjectSnapshot(val externalId: String?, val name: String, val position: Int, val topics: List<TopicSnapshot>)
    private data class TopicSnapshot(val externalId: String?, val title: String, val position: Int, val priority: String, val parentExternalId: String?)

    private class TransactionalPrivateSyllabusBackend(initial: PrivateSyllabus? = null) : PrivateSyllabusRemoteApi {
        var remote: PrivateSyllabus? = initial
            private set
        var committedMutations = 0
            private set
        private val ledger = mutableMapOf<String, Pair<String, RemoteSyllabusSyncAcknowledgement>>()
        private val lock = kotlinx.coroutines.sync.Mutex()

        override suspend fun list(): List<PrivateSyllabus> = listOfNotNull(remote)
        override suspend fun get(remoteSyllabusId: String): PrivateSyllabus? = remote?.takeIf { it.remoteSyllabusId == remoteSyllabusId }

        override suspend fun upsert(syllabus: PrivateSyllabus, mutationId: String, payloadHash: String): RemoteSyllabusSyncAcknowledgement = lock.withLock {
            val previous = ledger[mutationId]
            if (previous != null) {
                if (previous.first != payloadHash) throw PrivateSyllabusApiException("IDEMPOTENCY_KEY_CONFLICT", 409)
                return@withLock previous.second
            }
            val acknowledgement = ack(syllabus.remoteSyllabusId, payloadHash)
            remote = syllabus
            ledger[mutationId] = payloadHash to acknowledgement
            committedMutations++
            acknowledgement
        }

        override suspend fun delete(remoteSyllabusId: String, mutationId: String, payloadHash: String): RemoteSyllabusSyncAcknowledgement = lock.withLock {
            remote = null
            ack(remoteSyllabusId, payloadHash)
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

    private class RoomGateway(private val database: AppDatabase, private val repository: PrivateSyllabusRepository) : RemoteSyllabusSyncGateway {
        private val dao = database.dao()
        override suspend fun pending(now: Long) = dao.pendingRemoteSyllabusSync(now)
        override suspend fun failed(now: Long) = dao.failedRemoteSyllabusSync(now)
        override suspend fun requeue(row: RemoteSyllabusSyncEntity, attemptToken: String, now: Long, updatedAt: Long) = dao.requeueRemoteSync(row.id, row.attemptToken, attemptToken, now, updatedAt) == 1
        override suspend fun claim(row: RemoteSyllabusSyncEntity, attemptToken: String, nextAttemptAt: Long, updatedAt: Long) = dao.markRemoteSyncAttempt(row.id, row.attemptToken, attemptToken, nextAttemptAt, updatedAt) == 1
        override suspend fun expectedRemoteSyllabusId(row: RemoteSyllabusSyncEntity) = repository.expectedRemoteSyllabusId(row)
        override suspend fun sync(row: RemoteSyllabusSyncEntity) = repository.syncOutbox(row)
        override suspend fun markSynced(row: RemoteSyllabusSyncEntity, remoteSyllabusId: String, attemptToken: String, updatedAt: Long) = dao.markRemoteSyncSyncedAndAssociate(row.id, row.localSyllabusId, remoteSyllabusId, attemptToken, updatedAt, row.operation == RemoteSyllabusSyncOperation.UPSERT)
        override suspend fun markFailed(row: RemoteSyllabusSyncEntity, attemptToken: String, error: String, nextAttemptAt: Long, updatedAt: Long) = dao.markRemoteSyncFailed(row.id, attemptToken, error, nextAttemptAt, updatedAt) == 1
    }
}
