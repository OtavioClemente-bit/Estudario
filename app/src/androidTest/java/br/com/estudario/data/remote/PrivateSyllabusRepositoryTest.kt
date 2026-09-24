package br.com.estudario.data.remote

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import br.com.estudario.data.ai.AiPriority
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
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import org.json.JSONArray
import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNotSame
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import java.security.MessageDigest
import java.nio.charset.StandardCharsets
import java.util.UUID

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
        val expectedRemote = expectedRemoteFromPackage(packageJson, 41L, payloadHash)
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
        assertNotSame(backend.remote, fetched)
        assertRemoteTreeEquals(expectedRemote, fetched)
        assertEquals(payloadHash, fetched.metadata["payloadHash"]?.toString()?.trim('"'))
        val fetchedPackage = RemoteSyllabusMapper.toOfficialPackage(fetched)
        assertEquals(payloadHash, sha256(fetchedPackage))
        assertOfficialPackageFields(
            packageJson,
            RemoteSyllabusMapper.toOfficialPackage(fetched.copy(metadata = withoutCanonicalPayload(fetched.metadata))),
            payloadHash,
        )

        database.dao().deleteCompetition(syncedCompetition)
        assertTrue(backend.remote != null)
        val restoredId = repository.download(backend.remote!!.remoteSyllabusId)
        val restored = database.dao().competitionById(restoredId)!!
        assertEquals("competition-official", restored.externalId)
        assertEquals(backend.remote!!.remoteSyllabusId, restored.remoteSyllabusId)
        assertEquals(originalTree, tree(database, restoredId))
        assertEquals(payloadHash, sha256(RemoteSyllabusMapper.toOfficialPackage(backend.remote!!)))
        assertOfficialPackageFields(
            packageJson,
            RemoteSyllabusMapper.toOfficialPackage(backend.remote!!.copy(metadata = withoutCanonicalPayload(backend.remote!!.metadata))),
            payloadHash,
        )
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
        "materias":[
          {"id":"subject-internal","externalId":"subject-official","nome":"Direito","ordem":1,"prioridade":"ALTA","metadata":{"marker":"subject"},
            "topicos":[
              {"id":"topic-sibling","externalId":"topic-sibling","titulo":"Administrativo","ordem":0,"prioridade":"ALTA","metadata":{"marker":"sibling"},"subtopicos":[]},
              {"id":"topic-internal","externalId":"topic-official","titulo":"Constitucional","ordem":2,"prioridade":"BAIXA","metadata":{"marker":"topic","officialPriority":"original-metadata"},
                "subtopicos":[
                  {"id":"child-internal","externalId":"child-official","parentExternalId":"topic-official","titulo":"Direitos","ordem":0,"prioridade":"NORMAL","metadata":{"marker":"child","officialPriority":"original-child"}},
                  {"id":"child-second","externalId":"child-second","parentExternalId":"topic-official","titulo":"Garantias","ordem":3,"prioridade":"ALTA","metadata":{"marker":"child-second"}}
                ]}
            ]},
          {"id":"subject-second","externalId":"subject-second","nome":"Português","ordem":4,"prioridade":"BAIXA","metadata":{"marker":"subject-second"},
            "topicos":[{"id":"topic-second","externalId":"topic-second","titulo":"Gramática","ordem":1,"prioridade":"ALTA","metadata":{"marker":"topic-second"},
              "subtopicos":[{"id":"child-third","externalId":"child-third","parentExternalId":"topic-second","titulo":"Sintaxe","ordem":2,"prioridade":"BAIXA","metadata":{"marker":"child-third"}}]}]}
        ]}
    """.trimIndent().replace("\n", "")

    private fun sha256(value: String): String = MessageDigest.getInstance("SHA-256")
        .digest(value.toByteArray())
        .joinToString("") { "%02x".format(it) }

    private fun withoutCanonicalPayload(metadata: JsonObject): JsonObject =
        JsonObject(metadata.filterKeys { it != "canonicalPayload" })

    /**
     * Independent package oracle. It deliberately removes canonicalPayload
     * before re-export, so a fake cannot pass by echoing the original object.
     * Internal Room/remote ids may change, but every official identity and
     * lossless field must remain equal.
     */
    private fun assertOfficialPackageFields(originalJson: String, restoredJson: String, payloadHash: String) {
        val original = JSONObject(originalJson)
        val restored = JSONObject(restoredJson)
        assertEquals(original.getInt("version"), restored.getInt("version"))
        assertEquals(original.getString("packageId"), restored.getString("packageId"))
        assertEquals(original.getString("packageVersion"), restored.getString("packageVersion"))
        assertEquals(original.getInt("schemaVersion"), restored.getInt("schemaVersion"))
        assertEquals(payloadHash, sha256(originalJson))
        assertEquals(payloadHash, restored.getJSONObject("metadata").getString("payloadHash"))
        assertMetadataContains(original.optJSONObject("metadata"), restored.optJSONObject("metadata"))

        val originalCompetition = original.getJSONObject("concurso")
        val restoredCompetition = restored.getJSONObject("concurso")
        assertEquals(originalCompetition.getString("id"), restoredCompetition.getString("id"))
        assertEquals(originalCompetition.getString("nome"), restoredCompetition.getString("nome"))
        assertEquals(originalCompetition.getBoolean("principal"), restoredCompetition.getBoolean("principal"))

        val originalSubjects = jsonObjects(original.getJSONArray("materias"))
        val restoredSubjects = jsonObjects(restored.getJSONArray("materias"))
        assertEquals(originalSubjects.size, restoredSubjects.size)
        originalSubjects.zip(restoredSubjects).forEach { (expected, actual) ->
            assertEquals(expected.optString("externalId", expected.getString("id")), actual.getString("externalId"))
            assertEquals(expected.getString("nome"), actual.getString("nome"))
            assertEquals(expected.getInt("ordem"), actual.getInt("ordem"))
            assertEquals(expected.getString("prioridade"), actual.getString("prioridade"))
            assertMetadataEquals(expected.optJSONObject("metadata"), actual.optJSONObject("metadata"))
            assertOfficialTopics(expected.getJSONArray("topicos"), actual.getJSONArray("topicos"))
        }
    }

    private fun assertOfficialTopics(original: JSONArray, restored: JSONArray) {
        val expectedTopics = jsonObjects(original)
        val actualTopics = jsonObjects(restored)
        assertEquals(expectedTopics.size, actualTopics.size)
        expectedTopics.zip(actualTopics).forEach { (expected, actual) ->
            assertEquals(expected.optString("externalId", expected.getString("id")), actual.getString("externalId"))
            assertEquals(expected.getString("titulo"), actual.getString("titulo"))
            assertEquals(expected.getInt("ordem"), actual.getInt("ordem"))
            assertEquals(expected.getString("prioridade"), actual.getString("prioridade"))
            assertEquals(optionalString(expected, "parentExternalId"), optionalString(actual, "parentExternalId"))
            assertMetadataEquals(expected.optJSONObject("metadata"), actual.optJSONObject("metadata"))
            assertOfficialTopics(expected.optJSONArray("subtopicos") ?: JSONArray(), actual.optJSONArray("subtopicos") ?: JSONArray())
        }
    }

    private fun assertMetadataContains(expected: JSONObject?, actual: JSONObject?) {
        if (expected == null) return
        assertNotNull(actual)
        expected.keys().forEach { key -> assertEquals(expected.get(key).toString(), actual!!.get(key).toString()) }
    }

    private fun assertMetadataEquals(expected: JSONObject?, actual: JSONObject?) {
        if (expected == null) {
            assertTrue(actual == null || actual.length() == 0)
        } else {
            assertNotNull(actual)
            assertEquals(expected.toString(), actual!!.toString())
        }
    }

    private fun optionalString(value: JSONObject, key: String): String =
        if (!value.has(key) || value.isNull(key)) "" else value.getString(key)

    /** Independent oracle: this reads the official package instead of invoking RemoteSyllabusMapper. */
    private fun expectedRemoteFromPackage(packageJson: String, localSyllabusId: Long, payloadHash: String): PrivateSyllabus {
        val root = JSONObject(packageJson)
        val competition = root.getJSONObject("concurso")
        val rootInputMetadata = root.optJSONObject("metadata") ?: JSONObject()
        val stableIdentity = rootInputMetadata.optString("stableIdentity", competition.getString("id"))
        val remoteSyllabusId = stableId("syllabus", stableIdentity)
        val rootMetadata = JSONObject(rootInputMetadata.toString())
            .put("stableIdentity", stableIdentity)
            .put("payloadHash", payloadHash)
            .put("remoteSyllabusId", remoteSyllabusId)
            .put("localSyllabusId", localSyllabusId)
            .put("localSyllabusExternalId", competition.getString("id"))
            .put("packageId", root.getString("packageId"))
        val packageVersion = root.optString("packageVersion", "estudo-v${root.optInt("version", 2)}")
        val schemaVersion = root.optInt("schemaVersion", 1)
        val subjects = jsonObjects(root.getJSONArray("materias")).map { subject ->
            val remoteSubjectId = stableId("subject:$remoteSyllabusId", subject.optString("externalId", subject.getString("id")))
            PrivateSyllabusSubject(
                remoteSubjectId = remoteSubjectId,
                externalId = subject.optString("externalId", subject.getString("id")),
                name = subject.getString("nome"),
                position = subject.optInt("ordem", 0),
                suggestedPriority = aiPriority(subject.optString("prioridade", "NORMAL")),
                packageVersion = packageVersion,
                schemaVersion = schemaVersion,
                metadata = jsonObject(subject.optJSONObject("metadata") ?: JSONObject()),
                topics = expectedTopics(subject.getJSONArray("topicos"), remoteSubjectId, packageVersion, schemaVersion),
            )
        }
        return PrivateSyllabus(
            remoteSyllabusId = remoteSyllabusId,
            title = competition.getString("nome"),
            position = 0,
            visibility = PrivateSyllabusVisibility.PRIVATE,
            source = PrivateSyllabusSource.AI_GENERATED,
            sourceJobId = null,
            sourceHash = null,
            schemaVersion = schemaVersion,
            status = PrivateSyllabusStatus.ACTIVE,
            metadata = jsonObject(rootMetadata),
            subjects = subjects,
        )
    }

    private fun expectedTopics(array: JSONArray, subjectId: String, packageVersion: String, schemaVersion: Int): List<PrivateSyllabusTopic> =
        jsonObjects(array).map { topic -> expectedTopic(topic, subjectId, null, packageVersion, schemaVersion) }

    private fun expectedTopic(
        topic: JSONObject,
        subjectId: String,
        parentRemoteTopicId: String?,
        packageVersion: String,
        schemaVersion: Int,
    ): PrivateSyllabusTopic {
        val externalId = topic.optString("externalId", topic.getString("id"))
        val remoteTopicId = stableId("topic:$subjectId", externalId)
        val metadata = JSONObject(topic.optJSONObject("metadata")?.toString() ?: "{}")
            .put("__estudario_official_priority", topic.optString("prioridade", "NORMAL").uppercase())
        return PrivateSyllabusTopic(
            remoteTopicId = remoteTopicId,
            externalId = externalId,
            parentRemoteTopicId = parentRemoteTopicId,
            name = topic.getString("titulo"),
            position = topic.optInt("ordem", 0),
            packageVersion = packageVersion,
            schemaVersion = schemaVersion,
            metadata = jsonObject(metadata),
            children = jsonObjects(topic.optJSONArray("subtopicos") ?: JSONArray()).map {
                expectedTopic(it, subjectId, remoteTopicId, packageVersion, schemaVersion)
            },
        )
    }

    private fun jsonObjects(array: JSONArray): List<JSONObject> =
        (0 until array.length()).map { array.getJSONObject(it) }.sortedBy { it.optInt("ordem", 0) }

    private fun aiPriority(value: String): AiPriority = when (value.uppercase()) {
        "BAIXA", "LOW" -> AiPriority.LOW
        "ALTA", "HIGH" -> AiPriority.HIGH
        else -> AiPriority.NORMAL
    }

    private fun stableId(namespace: String, value: String): String = UUID.nameUUIDFromBytes(
        "$namespace:$value".toByteArray(StandardCharsets.UTF_8),
    ).toString()

    private fun jsonObject(value: JSONObject): JsonObject = Json.parseToJsonElement(value.toString()).jsonObject

    private fun assertRemoteTreeEquals(expected: PrivateSyllabus, actual: PrivateSyllabus) {
        assertEquals(expected.remoteSyllabusId, actual.remoteSyllabusId)
        assertEquals(expected.title, actual.title)
        assertEquals(expected.position, actual.position)
        assertEquals(expected.visibility, actual.visibility)
        assertEquals(expected.source, actual.source)
        assertEquals(expected.sourceJobId, actual.sourceJobId)
        assertEquals(expected.sourceHash, actual.sourceHash)
        assertEquals(expected.schemaVersion, actual.schemaVersion)
        assertEquals(expected.status, actual.status)
        assertEquals(expected.metadata, JsonObject(actual.metadata.filterKeys { it != "canonicalPayload" }))
        assertEquals(expected.subjects.size, actual.subjects.size)
        expected.subjects.forEachIndexed { index, expectedSubject ->
            val actualSubject = actual.subjects[index]
            assertEquals(expectedSubject.remoteSubjectId, actualSubject.remoteSubjectId)
            assertEquals(expectedSubject.externalId, actualSubject.externalId)
            assertEquals(expectedSubject.name, actualSubject.name)
            assertEquals(expectedSubject.position, actualSubject.position)
            assertEquals(expectedSubject.suggestedPriority, actualSubject.suggestedPriority)
            assertEquals(expectedSubject.packageVersion, actualSubject.packageVersion)
            assertEquals(expectedSubject.schemaVersion, actualSubject.schemaVersion)
            assertEquals(expectedSubject.metadata, actualSubject.metadata)
            assertEquals(expectedSubject.topics.size, actualSubject.topics.size)
            assertRemoteTopicsEquals(expectedSubject.topics, actualSubject.topics)
        }
    }

    private fun assertRemoteTopicsEquals(expected: List<PrivateSyllabusTopic>, actual: List<PrivateSyllabusTopic>) {
        expected.forEachIndexed { index, expectedTopic ->
            val actualTopic = actual[index]
            assertEquals(expectedTopic.remoteTopicId, actualTopic.remoteTopicId)
            assertEquals(expectedTopic.externalId, actualTopic.externalId)
            assertEquals(expectedTopic.parentRemoteTopicId, actualTopic.parentRemoteTopicId)
            assertEquals(expectedTopic.name, actualTopic.name)
            assertEquals(expectedTopic.position, actualTopic.position)
            assertEquals(expectedTopic.packageVersion, actualTopic.packageVersion)
            assertEquals(expectedTopic.schemaVersion, actualTopic.schemaVersion)
            assertEquals(expectedTopic.metadata, actualTopic.metadata)
            assertEquals(expectedTopic.children.size, actualTopic.children.size)
            assertRemoteTopicsEquals(expectedTopic.children, actualTopic.children)
        }
    }

    private data class TreeSnapshot(val externalId: String?, val subjects: List<SubjectSnapshot>)
    private data class SubjectSnapshot(val externalId: String?, val name: String, val position: Int, val topics: List<TopicSnapshot>)
    private data class TopicSnapshot(val externalId: String?, val title: String, val position: Int, val priority: String, val parentExternalId: String?)

    private class TransactionalPrivateSyllabusBackend(initial: PrivateSyllabus? = null) : PrivateSyllabusRemoteApi {
        private var persistedRoot: PrivateSyllabus? = null
        private val persistedSubjects = mutableMapOf<String, PrivateSyllabusSubject>()
        private val persistedTopics = mutableMapOf<String, PrivateSyllabusTopic>()
        private val topicSubjectIds = mutableMapOf<String, String>()
        var committedMutations = 0
            private set
        private val ledger = mutableMapOf<String, Mutation>()
        private val lock = kotlinx.coroutines.sync.Mutex()

        init { initial?.let(::persist) }

        val remote: PrivateSyllabus?
            get() = persistedRoot?.let { readTree(it.remoteSyllabusId) }

        override suspend fun list(): List<PrivateSyllabus> = listOfNotNull(remote)
        override suspend fun get(remoteSyllabusId: String): PrivateSyllabus? = readTree(remoteSyllabusId)

        override suspend fun upsert(syllabus: PrivateSyllabus, mutationId: String, payloadHash: String): RemoteSyllabusSyncAcknowledgement = lock.withLock {
            val previous = ledger[mutationId]
            if (previous != null) {
                if (previous.payloadHash != payloadHash || previous.remoteSyllabusId != syllabus.remoteSyllabusId) throw PrivateSyllabusApiException("IDEMPOTENCY_KEY_CONFLICT", 409)
                return@withLock previous.acknowledgement
            }
            val acknowledgement = ack(syllabus.remoteSyllabusId, payloadHash)
            persist(syllabus)
            ledger[mutationId] = Mutation(payloadHash, syllabus.remoteSyllabusId, acknowledgement)
            committedMutations++
            acknowledgement
        }

        override suspend fun delete(remoteSyllabusId: String, mutationId: String, payloadHash: String): RemoteSyllabusSyncAcknowledgement = lock.withLock {
            val previous = ledger[mutationId]
            if (previous != null) {
                if (previous.payloadHash != payloadHash || previous.remoteSyllabusId != remoteSyllabusId) throw PrivateSyllabusApiException("IDEMPOTENCY_KEY_CONFLICT", 409)
                return@withLock previous.acknowledgement
            }
            if (readTree(remoteSyllabusId) == null) throw PrivateSyllabusNotFoundException(remoteSyllabusId)
            persistedRoot = null
            persistedSubjects.clear()
            persistedTopics.clear()
            topicSubjectIds.clear()
            ack(remoteSyllabusId, payloadHash).also { ledger[mutationId] = Mutation(payloadHash, remoteSyllabusId, it) }
        }

        private fun persist(syllabus: PrivateSyllabus) {
            persistedRoot = syllabus.copy(subjects = emptyList())
            persistedSubjects.clear()
            persistedTopics.clear()
            topicSubjectIds.clear()
            syllabus.subjects.forEach { subject ->
                persistedSubjects[subject.remoteSubjectId] = subject.copy(topics = emptyList())
                fun store(topic: PrivateSyllabusTopic) {
                    persistedTopics[topic.remoteTopicId] = topic.copy(children = emptyList())
                    topicSubjectIds[topic.remoteTopicId] = subject.remoteSubjectId
                    topic.children.forEach(::store)
                }
                subject.topics.forEach(::store)
            }
        }

        private fun readTree(remoteSyllabusId: String): PrivateSyllabus? {
            val root = persistedRoot?.takeIf { it.remoteSyllabusId == remoteSyllabusId } ?: return null
            return root.copy(subjects = persistedSubjects.values
                .sortedBy { it.position }
                .map { subject -> subject.copy(topics = readTopics(subject.remoteSubjectId)) })
        }

        private fun readTopics(subjectId: String): List<PrivateSyllabusTopic> {
            val rows = persistedTopics.values.filter { topicSubjectIds[it.remoteTopicId] == subjectId }
            fun children(parentId: String?): List<PrivateSyllabusTopic> = rows
                .filter { it.parentRemoteTopicId == parentId }
                .sortedBy { it.position }
                .map { topic -> topic.copy(children = children(topic.remoteTopicId)) }
            return rows.filter { it.parentRemoteTopicId == null }
                .sortedBy { it.position }
                .map { topic -> topic.copy(children = children(topic.remoteTopicId)) }
        }

        private data class Mutation(val payloadHash: String, val remoteSyllabusId: String, val acknowledgement: RemoteSyllabusSyncAcknowledgement)

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
