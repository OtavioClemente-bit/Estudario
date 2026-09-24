package br.com.estudario.ui.ai

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.test.core.app.ApplicationProvider
import br.com.estudario.data.remote.AiAccessTokenProvider
import br.com.estudario.data.ai.AiHttpRequest
import br.com.estudario.data.ai.AiHttpResponse
import br.com.estudario.data.ai.AiHttpTransport
import br.com.estudario.data.ai.AiJobRequestStore
import br.com.estudario.data.ai.AiJobStatus
import br.com.estudario.data.ai.AiPollingPolicy
import br.com.estudario.data.ai.DefaultAiSyllabusRepository
import br.com.estudario.data.ai.DataStoreAiJobRequestStore
import br.com.estudario.data.ai.InMemoryPdfSourceSnapshotStore
import br.com.estudario.data.ai.PdfSourceInput
import br.com.estudario.data.ai.PdfSourceProvider
import br.com.estudario.data.ai.PdfSourceReader
import br.com.estudario.data.ai.HttpAiApiClient
import br.com.estudario.data.local.RemoteSyllabusSyncState
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
            val createPath = request.method == "POST" && request.path == "/functions/v1/ai-syllabus/jobs"
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
