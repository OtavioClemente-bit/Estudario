package br.com.estudario.ui.ai

import android.app.Application
import androidx.room.Room
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.test.core.app.ApplicationProvider
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
import br.com.estudario.domain.ai.AiSyllabusDraft
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
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
            firstViewModel.start("content://fixture/applied.pdf", "applied.pdf")
            await { firstViewModel.state.value.content is AiReviewContent.Review }

            val review = firstViewModel.state.value.content as AiReviewContent.Review
            val unappliedSession = sessions.load(42L)
            assertNotNull(unappliedSession)
            assertEquals("job-applied", unappliedSession?.jobId)
            assertEquals(false, unappliedSession?.applied)

            // This is the crash window: the Room transaction committed but DataStore still says unapplied.
            val outbox = service.applyReviewedSyllabus(42L, review.draft, "job-applied")
            assertEquals(RemoteSyllabusSyncState.PENDING, outbox.state)
            assertEquals(RemoteSyllabusSyncState.PENDING, database.dao().remoteSyllabusSyncById(outbox.outboxId)?.state)
            assertEquals(false, sessions.load(42L)?.applied)
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
            assertEquals(true, sessions.load(42L)?.applied)
            assertEquals(outbox.outboxId, sessions.load(42L)?.outboxId)
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
            firstViewModel.start("content://fixture/applied.pdf", "applied.pdf")
            await { firstViewModel.state.value.content is AiReviewContent.Review }
            firstViewModel.apply()
            await { firstViewModel.state.value.content == AiReviewContent.Applied(RemoteSyllabusSyncState.PENDING) }
            assertNotNull(outboxId)
            assertTrue(sessions.load(42L)?.applied == true)
            assertEquals(outboxId, sessions.load(42L)?.outboxId)
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
        val firstJobs = DefaultAiReviewJobs(repository(firstTransport, requestStore, snapshots), requestStore)
        val firstViewModel = createViewModel(context, firstJobs, sessions, targetStore)

        try {
            await { firstViewModel.state.value.access.kind == AiReviewAccessKind.READY }
            firstViewModel.provideSource("content://fixture/edital.pdf", "edital.pdf")
            await { firstViewModel.state.value.content is AiReviewContent.Processing }

            val persisted = requestStore.list().single()
            val savedSession = sessions.load(42L)
            assertNotNull(persisted.jobId)
            assertNotNull(savedSession)
            assertEquals(persisted.requestId, savedSession?.requestId)
            assertEquals(persisted.idempotencyKey, savedSession?.idempotencyKey)
            assertEquals(1, firstTransport.createdJobIds.distinct().size)

            val secondTransport = ScriptedTransport(phase = Phase.RECOVER)
            val secondJobs = DefaultAiReviewJobs(repository(secondTransport, requestStore, snapshots), requestStore)
            val recreatedViewModel = createViewModel(context, secondJobs, sessions, targetStore)
            await { recreatedViewModel.state.value.content is AiReviewContent.Review }

            val allCreateHeaders = (firstTransport.createRequests + secondTransport.createRequests)
                .map { it.headers["Idempotency-Key"] }
            assertEquals(3, allCreateHeaders.size)
            assertEquals(setOf(persisted.idempotencyKey), allCreateHeaders.toSet())
            assertEquals(setOf("job-1"), (firstTransport.createdJobIds + secondTransport.createdJobIds).toSet())
            assertEquals(1, requestStore.list().size)
            assertEquals(AiJobStatus.SUCCEEDED.name, requestStore.list().single().status)
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
    ) = AiReviewViewModel(
        application = context,
        targetId = 42L,
        targetTitle = "TRT-3",
        jobs = jobs,
        applier = AiReviewApplier { _, _, _, _ -> error("not used") },
        sessions = sessions,
        targetStore = targetStore,
        accessGateway = object : AiReviewAccessGateway {
            override suspend fun check() = AiReviewAccessResult(true, true)
        },
        syncState = { RemoteSyllabusSyncState.SYNCED },
        syncPollDelayMillis = 1L,
    )

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
    )

    private class PersistedAppliedTestJobs : AiReviewJobs {
        var recoverCalls = 0

        override suspend fun start(targetId: Long, uri: String, fileName: String?): AiReviewStarted = started()

        override suspend fun recover(requestId: String): AiReviewStarted {
            recoverCalls += 1
            return started(requestId)
        }

        override suspend fun retryFailed(requestId: String): AiReviewStarted = error("terminal retry not expected")

        override suspend fun recoverPending(): List<AiReviewStarted> = emptyList()
        override suspend fun identityForJob(jobId: String): AiReviewRequestIdentity? =
            AiReviewRequestIdentity("request-applied", jobId, "idem-applied")

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
            AiReviewRequestIdentity(requestId, "job-applied", "idem-applied"),
        )
    }

    private fun await(condition: () -> Boolean) {
        repeat(400) {
            if (condition()) return
            Thread.sleep(10)
        }
        error("Condition was not reached")
    }

    private enum class Phase { FAIL_AFTER_UPLOAD, RECOVER }

    private class ScriptedTransport(private val phase: Phase) : AiHttpTransport {
        val createRequests = mutableListOf<AiHttpRequest>()
        val createdJobIds = mutableListOf<String>()
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
                    body = """{"jobId":"job-1","status":"${if (phase == Phase.RECOVER) "PROCESSING" else "RESERVED"}","uploadPath":"user/job-1.pdf","sourceBound":${phase == Phase.RECOVER}}""",
                )
            }
            if (request.method == "POST" && request.path.startsWith("/storage/v1/object/")) {
                return AiHttpResponse(201)
            }
            if (request.method == "POST" && request.path.endsWith("/process")) {
                return AiHttpResponse(202, """{"status":"PROCESSING"}""")
            }
            if (request.method == "GET" && request.path.endsWith("/job-1")) {
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
