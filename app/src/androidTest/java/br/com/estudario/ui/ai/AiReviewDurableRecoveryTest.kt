package br.com.estudario.ui.ai

import android.app.Application
import androidx.room.Room
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.test.core.app.ApplicationProvider
import androidx.lifecycle.viewModelScope
import br.com.estudario.data.remote.AiAccessTokenProvider
import br.com.estudario.data.ai.AiFeature
import br.com.estudario.data.ai.AiHttpRequest
import br.com.estudario.data.ai.AiHttpResponse
import br.com.estudario.data.ai.AiHttpTransport
import br.com.estudario.data.ai.AiJob
import br.com.estudario.data.ai.AiJobRequestStore
import br.com.estudario.data.ai.AiJobStatus
import br.com.estudario.data.ai.AiPriority
import br.com.estudario.data.ai.AiPollingPolicy
import br.com.estudario.data.ai.AiSubjectProposal
import br.com.estudario.data.ai.AiSyllabusProposal
import br.com.estudario.data.ai.AiTopicProposal
import br.com.estudario.data.ai.DefaultAiSyllabusRepository
import br.com.estudario.data.ai.DataStoreAiJobRequestStore
import br.com.estudario.data.ai.InMemoryPdfSourceSnapshotStore
import br.com.estudario.data.ai.PdfSourceInput
import br.com.estudario.data.ai.PdfSourceProvider
import br.com.estudario.data.ai.PdfSourceReader
import br.com.estudario.data.ai.HttpAiApiClient
import br.com.estudario.data.local.AppDatabase
import br.com.estudario.data.local.CompetitionEntity
import br.com.estudario.data.local.RemoteSyllabusSyncState
import br.com.estudario.data.syllabus.SyllabusApplicationService
import br.com.estudario.data.syllabus.ApplyResult
import br.com.estudario.domain.ai.AiSyllabusDraft
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.ByteArrayInputStream
import java.io.File
import java.io.IOException

class AiReviewDurableRecoveryTest {
    @Test
    fun recreatedViewModelResumesPersistedJobAfterStartCoroutineIsCancelled() = runBlocking {
        val context = ApplicationProvider.getApplicationContext<Application>()
        val file = File(context.filesDir, "ai-review-cancelled-start-${System.nanoTime()}.preferences_pb")
        val dataStoreScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
        val dataStore: DataStore<Preferences> = PreferenceDataStoreFactory.create(
            scope = dataStoreScope,
            produceFile = { file },
        )
        val requestStore = DataStoreAiJobRequestStore(dataStore)
        val sessions = DataStoreAiReviewSessionStore(dataStore)
        val targetStore = DataStoreAiReviewTargetStore(dataStore)
        val snapshots = InMemoryPdfSourceSnapshotStore()
        val firstTransport = ScriptedTransport(phase = Phase.BLOCK_GET)
        val firstJobs = DefaultAiReviewJobs(repository(firstTransport, requestStore, snapshots), requestStore) { TEST_USER }
        val firstViewModel = createViewModel(context, firstJobs, sessions, targetStore)

        try {
            await { firstViewModel.state.value.access.kind == AiReviewAccessKind.READY }
            generate(firstViewModel, "content://fixture/edital.pdf", "edital.pdf")
            awaitSuspend {
                sessions.load(42L, TEST_USER)?.jobId == "job-1" &&
                    requestStore.list().singleOrNull()?.status == AiJobStatus.PROCESSING.name
            }

            firstViewModel.viewModelScope.cancel()

            val secondTransport = ScriptedTransport(phase = Phase.RECOVER)
            val secondJobs = DefaultAiReviewJobs(repository(secondTransport, requestStore, snapshots), requestStore) { TEST_USER }
            var autoApplyCalls = 0
            val recreatedViewModel = createViewModel(
                context,
                secondJobs,
                sessions,
                targetStore,
                applier = AiReviewApplier { targetId, _, jobId, _ ->
                    autoApplyCalls += 1
                    appliedResult(targetId, jobId)
                },
            )
            await { recreatedViewModel.state.value.content is AiReviewContent.Review }

            val persisted = requestStore.list().single()
            assertEquals("job-1", persisted.jobId)
            assertEquals(AiJobStatus.SUCCEEDED.name, persisted.status)
            assertEquals(TEST_USER, persisted.ownerUserId)
            assertEquals(1, firstTransport.createdJobIds.distinct().size)
            assertTrue(secondTransport.createRequests.isEmpty())
            assertEquals("job-1", sessions.load(42L, TEST_USER)?.jobId)
            assertEquals(false, sessions.load(42L, TEST_USER)?.applied)
            assertEquals(0, autoApplyCalls)
        } finally {
            firstTransport.releaseProcess.complete(Unit)
            dataStoreScope.cancel()
            file.delete()
        }
    }

    @Test
    fun requestSavedBeforeSessionCallbackIsRecoveredWithoutCreatingAnotherJob() = runBlocking {
        val context = ApplicationProvider.getApplicationContext<Application>()
        val file = File(context.filesDir, "ai-review-request-only-${System.nanoTime()}.preferences_pb")
        val dataStoreScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
        val dataStore: DataStore<Preferences> = PreferenceDataStoreFactory.create(
            scope = dataStoreScope,
            produceFile = { file },
        )
        val requestStore = DataStoreAiJobRequestStore(dataStore)
        val sessions = DataStoreAiReviewSessionStore(dataStore)
        val targetStore = DataStoreAiReviewTargetStore(dataStore)
        val snapshots = InMemoryPdfSourceSnapshotStore()
        val transport = ScriptedTransport(phase = Phase.RECOVER)
        try {
            requestStore.save(
                br.com.estudario.data.ai.PersistedAiJobRequest(
                    requestId = "persisted-before-session",
                    idempotencyKey = "same-idempotency-key",
                    sourceUri = "content://expired-permission",
                    fileName = "edital.pdf",
                    mimeType = "application/pdf",
                    sourceHash = "a".repeat(64),
                    sourceBytes = 123L,
                    jobId = "job-1",
                    status = AiJobStatus.PROCESSING.name,
                    ownerUserId = TEST_USER,
                    targetSyllabusId = 42L,
                    targetTitle = "TRT-3",
                ),
            )
            targetStore.save(AiReviewTarget(42L, "TRT-3", "content://expired-permission", "edital.pdf"), TEST_USER)
            val jobs = DefaultAiReviewJobs(repository(transport, requestStore, snapshots), requestStore) { TEST_USER }
            var autoApplyCalls = 0
            val viewModel = createViewModel(
                context,
                jobs,
                sessions,
                targetStore,
                applier = AiReviewApplier { targetId, _, jobId, _ ->
                    autoApplyCalls += 1
                    appliedResult(targetId, jobId)
                },
            )

            await { viewModel.state.value.content is AiReviewContent.Review }

            assertTrue(transport.createRequests.isEmpty())
            assertEquals("job-1", requestStore.list().single().jobId)
            assertEquals("same-idempotency-key", sessions.load(42L, TEST_USER)?.idempotencyKey)
            assertEquals(false, sessions.load(42L, TEST_USER)?.applied)
            assertEquals(0, autoApplyCalls)
        } finally {
            dataStoreScope.cancel()
            file.delete()
        }
    }

    @Test
    fun reviewSessionAndTargetAreInvisibleToAnotherAccount() = runBlocking {
        val context = ApplicationProvider.getApplicationContext<Application>()
        val file = File(context.filesDir, "ai-review-owner-scope-${System.nanoTime()}.preferences_pb")
        val dataStoreScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
        val dataStore: DataStore<Preferences> = PreferenceDataStoreFactory.create(
            scope = dataStoreScope,
            produceFile = { file },
        )
        val sessions = DataStoreAiReviewSessionStore(dataStore)
        val targets = DataStoreAiReviewTargetStore(dataStore)
        try {
            sessions.save(AiReviewPersistedSession(42L, "TRT-3", "request-a", "job-a", "idem-a", ownerUserId = "user-a"))
            targets.save(AiReviewTarget(42L, "TRT-3", "content://private-a", "a.pdf"), "user-a")

            assertEquals("job-a", sessions.load(42L, "user-a")?.jobId)
            assertEquals(null, sessions.load(42L, "user-b"))
            assertEquals(null, sessions.loadLatest("user-b"))
            assertEquals(null, targets.load("user-b"))
            assertEquals("content://private-a", targets.load("user-a")?.sourceUri)
        } finally {
            dataStoreScope.cancel()
            file.delete()
        }
    }

    @Test
    fun legacyTargetIsNotAssignedToAnAccountBeforeJobOwnershipIsVerified() = runBlocking {
        val context = ApplicationProvider.getApplicationContext<Application>()
        val file = File(context.filesDir, "ai-review-legacy-target-${System.nanoTime()}.preferences_pb")
        val dataStoreScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
        val dataStore: DataStore<Preferences> = PreferenceDataStoreFactory.create(
            scope = dataStoreScope,
            produceFile = { file },
        )
        val targets = DataStoreAiReviewTargetStore(dataStore)
        try {
            dataStore.edit { preferences ->
                preferences[stringPreferencesKey("target")] =
                    """{"id":42,"title":"TRT-3","sourceUri":"content://legacy","sourceName":"edital.pdf"}"""
            }

            assertEquals(null, targets.load("user-a"))
            assertEquals("content://legacy", targets.loadUnownedLegacy()?.sourceUri)
        } finally {
            dataStoreScope.cancel()
            file.delete()
        }
    }

    @Test
    fun restoreReconcilesRoomApplyWhenDataStoreMarkerWasNotWrittenBeforeCrash() = runBlocking {
        val context = ApplicationProvider.getApplicationContext<Application>()
        val file = File(context.filesDir, "ai-review-crash-window-${System.nanoTime()}.preferences_pb")
        val dataStoreScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
        val dataStore: DataStore<Preferences> = PreferenceDataStoreFactory.create(
            scope = dataStoreScope,
            produceFile = { file },
        )
        val sessions = DataStoreAiReviewSessionStore(dataStore)
        val database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        try {
            database.dao().insertCompetition(CompetitionEntity(id = 42L, name = "TRT-3"))
            val service = SyllabusApplicationService(database)
            val firstViewModel = createAppliedViewModel(
                context = context,
                jobs = PersistedAppliedTestJobs(),
                sessions = sessions,
                applier = AiReviewApplier { _, _, _, _ -> error("simulated process died before marker write") },
                database = database,
            )
            await { firstViewModel.state.value.access.kind == AiReviewAccessKind.READY }
            generate(firstViewModel, "content://fixture/applied.pdf", "applied.pdf")
            await { firstViewModel.state.value.content is AiReviewContent.Review }

            val review = firstViewModel.state.value.content as AiReviewContent.Review
            val unappliedSession = sessions.load(42L, TEST_USER)
            assertNotNull(unappliedSession)
            assertEquals("job-applied", unappliedSession?.jobId)
            assertEquals(false, unappliedSession?.applied)

            // This is the crash window: the Room transaction committed but DataStore still says unapplied.
            val outbox = service.applyReviewedSyllabus(42L, review.draft, "job-applied")
            assertEquals(RemoteSyllabusSyncState.PENDING, outbox.state)
            assertEquals(RemoteSyllabusSyncState.PENDING, database.dao().remoteSyllabusSyncById(outbox.outboxId)?.state)
            assertEquals(false, sessions.load(42L, TEST_USER)?.applied)
            assertEquals(null, service.findAppliedSyllabus(43L, "job-applied"))
            assertEquals(null, service.findAppliedSyllabus(42L, "another-job"))

            val restoredJobs = PersistedAppliedTestJobs()
            var reapplyCalls = 0
            val restoredViewModel = createAppliedViewModel(
                context = context,
                jobs = restoredJobs,
                sessions = DataStoreAiReviewSessionStore(dataStore),
                applier = object : AiReviewApplier {
                    override suspend fun apply(
                        targetId: Long,
                        draft: AiSyllabusDraft,
                        jobId: String,
                        replaceExisting: Boolean,
                    ): br.com.estudario.data.syllabus.ApplyResult {
                        reapplyCalls += 1
                        error("restore must not reapply")
                    }

                    override suspend fun findApplied(targetId: Long, sourceJobId: String) =
                        service.findAppliedSyllabus(targetId, sourceJobId)
                },
                database = database,
            )
            await {
                restoredViewModel.state.value.content is AiReviewContent.Review ||
                    restoredViewModel.state.value.content is AiReviewContent.Applied
            }

            assertEquals(AiReviewContent.Applied(RemoteSyllabusSyncState.PENDING), restoredViewModel.state.value.content)
            assertEquals(true, sessions.load(42L, TEST_USER)?.applied)
            assertEquals(outbox.outboxId, sessions.load(42L, TEST_USER)?.outboxId)
            assertEquals(0, restoredJobs.recoverCalls)
            assertEquals(0, reapplyCalls)
            assertEquals(RemoteSyllabusSyncState.PENDING, database.dao().remoteSyllabusSyncById(outbox.outboxId)?.state)
            assertEquals(1, database.dao().subjectsFor(42L).size)
        } finally {
            database.close()
            dataStoreScope.cancel()
            file.delete()
        }
    }

    @Test
    fun appliedReviewSurvivesRestartWithoutReopeningAndOutboxRemainsPending() = runBlocking {
        val context = ApplicationProvider.getApplicationContext<Application>()
        val file = File(context.filesDir, "ai-review-applied-${System.nanoTime()}.preferences_pb")
        val dataStoreScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
        val dataStore: DataStore<Preferences> = PreferenceDataStoreFactory.create(
            scope = dataStoreScope,
            produceFile = { file },
        )
        val sessions = DataStoreAiReviewSessionStore(dataStore)
        val database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        try {
            database.dao().insertCompetition(CompetitionEntity(id = 42L, name = "TRT-3"))
            val service = SyllabusApplicationService(database)
            var outboxId: Long? = null
            val firstJobs = PersistedAppliedTestJobs()
            val firstViewModel = createAppliedViewModel(
                context = context,
                jobs = firstJobs,
                sessions = sessions,
                applier = AiReviewApplier { targetId, draft, jobId, replace ->
                    service.applyReviewedSyllabus(targetId, draft, jobId, replace).also { outboxId = it.outboxId }
                },
                database = database,
            )

            await { firstViewModel.state.value.access.kind == AiReviewAccessKind.READY }
            generate(firstViewModel, "content://fixture/applied.pdf", "applied.pdf")
            await { firstViewModel.state.value.content is AiReviewContent.Review }
            firstViewModel.apply()
            await { firstViewModel.state.value.content == AiReviewContent.Applied(RemoteSyllabusSyncState.PENDING) }
            assertNotNull(outboxId)
            assertTrue(sessions.load(42L, TEST_USER)?.applied == true)
            assertEquals(outboxId, sessions.load(42L, TEST_USER)?.outboxId)
            assertEquals(RemoteSyllabusSyncState.PENDING, database.dao().remoteSyllabusSyncById(outboxId!!)?.state)

            val restoredJobs = PersistedAppliedTestJobs()
            val restoredViewModel = createAppliedViewModel(
                context = context,
                jobs = restoredJobs,
                sessions = DataStoreAiReviewSessionStore(dataStore),
                applier = AiReviewApplier { targetId, draft, jobId, replace ->
                    service.applyReviewedSyllabus(targetId, draft, jobId, replace)
                },
                database = database,
            )
            await {
                restoredViewModel.state.value.content is AiReviewContent.Review ||
                    restoredViewModel.state.value.content is AiReviewContent.Applied
            }

            assertEquals(AiReviewContent.Applied(RemoteSyllabusSyncState.PENDING), restoredViewModel.state.value.content)
            assertEquals(0, restoredJobs.recoverCalls)
            restoredViewModel.apply()
            assertEquals(RemoteSyllabusSyncState.PENDING, database.dao().remoteSyllabusSyncById(outboxId!!)?.state)
            assertEquals(1, database.dao().subjectsFor(42L).size)
        } finally {
            database.close()
            dataStoreScope.cancel()
            file.delete()
        }
    }

    @Test
    fun datastoreRestartRecoversPostUploadFailureWithoutCreatingNewJobOrKey() = runBlocking {
        val context = ApplicationProvider.getApplicationContext<Application>()
        val file = File(context.filesDir, "ai-review-recovery-${System.nanoTime()}.preferences_pb")
        val dataStoreScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
        val dataStore: DataStore<Preferences> = PreferenceDataStoreFactory.create(
            scope = dataStoreScope,
            produceFile = { file },
        )
        val requestStore = DataStoreAiJobRequestStore(dataStore)
        val sessions = DataStoreAiReviewSessionStore(dataStore)
        val targetStore = DataStoreAiReviewTargetStore(dataStore)
        val snapshots = InMemoryPdfSourceSnapshotStore()
        val firstTransport = ScriptedTransport(phase = Phase.FAIL_AFTER_UPLOAD)
        val firstJobs = DefaultAiReviewJobs(repository(firstTransport, requestStore, snapshots), requestStore) { TEST_USER }
        val firstViewModel = createViewModel(context, firstJobs, sessions, targetStore)

        try {
            await { firstViewModel.state.value.access.kind == AiReviewAccessKind.READY }
            generate(firstViewModel, "content://fixture/edital.pdf", "edital.pdf")
            await { firstViewModel.state.value.content is AiReviewContent.Processing }

            val persisted = requestStore.list().single()
            val savedSession = sessions.load(42L, TEST_USER)
            assertNotNull(persisted.jobId)
            assertNotNull(savedSession)
            assertEquals(persisted.requestId, savedSession?.requestId)
            assertEquals(persisted.idempotencyKey, savedSession?.idempotencyKey)
            assertEquals(1, firstTransport.createdJobIds.distinct().size)

            val secondTransport = ScriptedTransport(phase = Phase.RECOVER)
            val secondJobs = DefaultAiReviewJobs(repository(secondTransport, requestStore, snapshots), requestStore) { TEST_USER }
            val recreatedViewModel = createViewModel(context, secondJobs, sessions, targetStore)
            await { recreatedViewModel.state.value.content is AiReviewContent.Review }

            val allCreateHeaders = (firstTransport.createRequests + secondTransport.createRequests)
                .map { it.headers["Idempotency-Key"] }
            assertEquals(2, allCreateHeaders.size)
            assertEquals(setOf(persisted.idempotencyKey), allCreateHeaders.toSet())
            assertEquals(setOf("job-1"), (firstTransport.createdJobIds + secondTransport.createdJobIds).toSet())
            assertEquals(1, requestStore.list().size)
            assertEquals(AiJobStatus.SUCCEEDED.name, requestStore.list().single().status)
            assertEquals(false, sessions.load(42L, TEST_USER)?.applied)
        } finally {
            dataStoreScope.cancel()
            file.delete()
        }
    }

    private fun createViewModel(
        context: Application,
        jobs: AiReviewJobs,
        sessions: AiReviewSessionStore,
        targetStore: AiReviewTargetStore,
        applier: AiReviewApplier = AiReviewApplier { targetId, _, jobId, _ -> appliedResult(targetId, jobId) },
    ) = AiReviewViewModel(
        application = context,
        targetId = 42L,
        targetTitle = "TRT-3",
        jobs = jobs,
        applier = applier,
        sessions = sessions,
        targetStore = targetStore,
        accessGateway = object : AiReviewAccessGateway {
            override suspend fun check() = AiReviewAccessResult(true, true)
        },
        syncState = { RemoteSyllabusSyncState.SYNCED },
        syncPollDelayMillis = 1L,
        userIdProvider = { TEST_USER },
    )

    private fun generate(viewModel: AiReviewViewModel, uri: String, name: String) {
        viewModel.updatePreferences(br.com.estudario.data.ai.AiSyllabusPreferences("TRT-3", "Analista"))
        viewModel.start(uri, name)
        await { viewModel.state.value.content is AiReviewContent.Confirmation }
        viewModel.confirmGeneration()
    }

    private fun repository(
        transport: AiHttpTransport,
        requestStore: AiJobRequestStore,
        snapshots: InMemoryPdfSourceSnapshotStore,
    ) = DefaultAiSyllabusRepository(
        api = HttpAiApiClient(
            baseUrl = "https://project.supabase.co",
            publishableKey = "sb_publishable_test",
            accessTokenProvider = AiAccessTokenProvider { "supabase-jwt" },
            transport = transport,
            httpTimeoutMillis = 5_000L,
        ),
        sourceReader = PdfSourceReader(PdfSourceProvider {
            PdfSourceInput("application/pdf", "edital.pdf", ByteArrayInputStream(byteArrayOf(37, 80, 68, 70, 45, 49)))
        }),
        requestStore = requestStore,
        accessTokenProvider = AiAccessTokenProvider { "supabase-jwt" },
        pollingPolicy = AiPollingPolicy(timeoutMillis = 5_000L, initialDelayMillis = 1L, maxDelayMillis = 1L),
        sourceSnapshots = snapshots,
        userIdProvider = { TEST_USER },
    )

    private fun createAppliedViewModel(
        context: Application,
        jobs: PersistedAppliedTestJobs,
        sessions: AiReviewSessionStore,
        applier: AiReviewApplier,
        database: AppDatabase,
    ) = AiReviewViewModel(
        application = context,
        targetId = 42L,
        targetTitle = "TRT-3",
        jobs = jobs,
        applier = applier,
        sessions = sessions,
        accessGateway = object : AiReviewAccessGateway {
            override suspend fun check() = AiReviewAccessResult(true, true)
        },
        syncState = { id -> database.dao().remoteSyllabusSyncById(id)?.state },
        syncPollDelayMillis = 60_000L,
        userIdProvider = { TEST_USER },
    )

    private class PersistedAppliedTestJobs : AiReviewJobs {
        override suspend fun prepare(target: AiReviewTarget) = preparedFixture(target)
        var recoverCalls = 0

        override suspend fun start(targetId: Long, uri: String, fileName: String?): AiReviewStarted = started()

        override suspend fun recover(requestId: String): AiReviewStarted {
            recoverCalls += 1
            return started(requestId)
        }

        override suspend fun retryFailed(requestId: String): AiReviewStarted = error("terminal retry not expected")

        override suspend fun recoverPending(): List<AiReviewStarted> = emptyList()
        override suspend fun identityForJob(jobId: String): AiReviewRequestIdentity? =
            AiReviewRequestIdentity("request-applied", jobId, "idem-applied", TEST_USER)

        private fun started(requestId: String = "request-applied") = AiReviewStarted(
            AiJob(
                jobId = "job-applied",
                feature = AiFeature.SYLLABUS_GENERATION,
                status = AiJobStatus.SUCCEEDED,
                schemaVersion = 1,
                promptVersion = "syllabus-v1",
                modelVersion = "fixture-model",
                proposal = AiSyllabusProposal(
                    1,
                    "syllabus-v1",
                    "fixture-model",
                    "Edital",
                    listOf(AiSubjectProposal("Direito", 0, AiPriority.NORMAL, listOf(AiTopicProposal("Constituição", 0, emptyList(), listOf(1))), listOf(1))),
                    emptyList(),
                    emptyList(),
                ),
                warnings = emptyList(),
                errorCode = null,
                errorMessage = null,
                createdAt = "2026-09-24T10:00:00Z",
                updatedAt = "2026-09-24T10:00:01Z",
                finishedAt = "2026-09-24T10:00:01Z",
                providerExecutionStartedAt = null,
            ),
            AiReviewRequestIdentity(requestId, "job-applied", "idem-applied", TEST_USER),
        )
    }

    private fun await(condition: () -> Boolean) {
        repeat(400) {
            if (condition()) return
            Thread.sleep(10)
        }
        error("Condition was not reached")
    }

    private suspend fun awaitSuspend(condition: suspend () -> Boolean) {
        repeat(400) {
            if (condition()) return
            delay(10)
        }
        error("Condition was not reached")
    }

    private fun appliedResult(targetId: Long, jobId: String) = ApplyResult(
        localSyllabusId = targetId,
        sourceJobId = jobId,
        packageJson = "{}",
        payloadHash = "a".repeat(64),
        outboxId = 1L,
        remoteSyllabusId = null,
        state = RemoteSyllabusSyncState.PENDING,
        created = true,
        alreadyApplied = false,
    )

    private enum class Phase { FAIL_AFTER_UPLOAD, RECOVER, BLOCK_GET }

    private companion object { const val TEST_USER = "recovery-test-user" }

    private class ScriptedTransport(private val phase: Phase) : AiHttpTransport {
        val createRequests = mutableListOf<AiHttpRequest>()
        val createdJobIds = mutableListOf<String>()
        val releaseProcess = CompletableDeferred<Unit>()
        private var createCount = 0

        override suspend fun execute(request: AiHttpRequest): AiHttpResponse {
            val createPath = request.method == "POST" && request.path == "/functions/v1/ai-syllabus-jobs"
            if (createPath) {
                createRequests += request
                createdJobIds += "job-1"
                if (phase == Phase.FAIL_AFTER_UPLOAD && createCount++ == 1) {
                    throw IOException("generic transport failure after upload")
                }
                return AiHttpResponse(
                    status = 201,
                    body = """{"jobId":"job-1","status":"${if (phase == Phase.RECOVER) "PROCESSING" else "RESERVED"}","uploadPath":"user/job-1.pdf","sourceBound":${phase == Phase.RECOVER || org.json.JSONObject(request.body?.toString(Charsets.UTF_8) ?: "{}").getJSONObject("source").optBoolean("ready")}}""",
                )
            }
            if (request.method == "POST" && request.path.startsWith("/storage/v1/object/")) {
                return AiHttpResponse(201)
            }
            if (request.method == "POST" && request.path.endsWith("/process")) {
                return AiHttpResponse(202, """{"status":"PROCESSING"}""")
            }
            if (request.method == "GET" && request.path.endsWith("/job-1")) {
                if (phase == Phase.BLOCK_GET) releaseProcess.await()
                return AiHttpResponse(200, succeededJobJson())
            }
            error("Unexpected fake transport request: ${request.method} ${request.path}")
        }

        private fun succeededJobJson(): String = """
            {
              "jobId":"job-1","feature":"SYLLABUS_GENERATION","status":"SUCCEEDED",
              "schemaVersion":1,"promptVersion":"syllabus-v1","modelVersion":"fixture-model",
              "proposal":{"schemaVersion":1,"promptVersion":"syllabus-v1","modelVersion":"fixture-model","documentTitle":"Edital","subjects":[{"name":"Direito","position":0,"suggestedPriority":"NORMAL","topics":[{"name":"Constituição","position":0,"children":[],"sourcePages":[1]}],"sourcePages":[1]}],"warnings":[],"ambiguities":[]},
              "warnings":[],"errorCode":null,"errorMessage":null,
              "createdAt":"2026-09-24T10:00:00Z","updatedAt":"2026-09-24T10:00:01Z",
              "finishedAt":"2026-09-24T10:00:01Z","providerExecutionStartedAt":null
            }
        """.trimIndent()
    }
}
