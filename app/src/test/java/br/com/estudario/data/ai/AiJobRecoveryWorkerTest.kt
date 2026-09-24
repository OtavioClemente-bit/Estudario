package br.com.estudario.data.ai

import android.app.Application
import androidx.work.Data
import androidx.work.NetworkType
import androidx.work.DefaultWorkerFactory
import androidx.work.ForegroundUpdater
import androidx.work.ProgressUpdater
import androidx.work.WorkerParameters
import androidx.work.impl.utils.taskexecutor.SerialExecutor
import androidx.work.impl.utils.taskexecutor.TaskExecutor
import androidx.work.impl.utils.futures.SettableFuture
import android.content.Context
import java.util.UUID
import java.security.MessageDigest
import kotlin.coroutines.EmptyCoroutineContext
import java.util.concurrent.Executor
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class AiJobRecoveryWorkerTest {
    @Test
    fun recoveryUsesConnectedNetworkAndStopsAfterBoundedAttempts() {
        assertEquals(NetworkType.CONNECTED, AiJobRecoveryWorker.recoveryConstraints().requiredNetworkType)
        assertTrue(AiJobRecoveryWorker.shouldRetry(0))
        assertTrue(AiJobRecoveryWorker.shouldRetry(AiJobRecoveryWorker.MAX_ATTEMPTS - 2))
        assertFalse(AiJobRecoveryWorker.shouldRetry(AiJobRecoveryWorker.MAX_ATTEMPTS - 1))
        assertFalse(AiJobRecoveryWorker.shouldRetry(AiJobRecoveryWorker.MAX_ATTEMPTS))
    }

    @Test
    fun doWorkRunsRecoveryAgainstInjectedApiAndStoreBackedRepository() = runBlocking {
        var storeReads = 0
        val api = object : AiApiClient {
            override suspend fun createOrGetJob(idempotencyKey: String, source: AiSourceMetadata, sourceReady: Boolean) = error("not reached")
            override suspend fun uploadSource(target: AiUploadTarget, source: PdfSource) = error("not reached")
            override suspend fun processJob(jobId: String) = error("not reached")
            override suspend fun getJob(jobId: String, timeoutMillis: Long?) = error("not reached")
            override suspend fun awaitJob(jobId: String, policy: AiPollingPolicy, sleeper: suspend (Long) -> Unit, clockMillis: () -> Long) = error("not reached")
        }
        val store = object : AiJobRequestStore {
            override suspend fun save(value: PersistedAiJobRequest) = error("not reached")
            override suspend fun get(requestId: String) = error("not reached")
            override suspend fun list(): List<PersistedAiJobRequest> {
                storeReads += 1
                return emptyList()
            }
        }
        val repository = DefaultAiSyllabusRepository(
            api = api,
            sourceReader = PdfSourceReader(provider = PdfSourceProvider { error("not reached") }),
            requestStore = store,
            accessTokenProvider = br.com.estudario.data.remote.AiAccessTokenProvider { "supabase-jwt" },
            sourceSnapshots = object : PdfSourceSnapshotStore {
                override fun save(source: PdfSource): String = "private/${source.sha256}.pdf"
                override fun read(path: String, fileName: String): PdfSource = error("not reached")
            },
        )
        val worker = AiJobRecoveryWorker(Application(), workerParameters(), repository)

        assertEquals(androidx.work.ListenableWorker.Result.success(), worker.doWork())
        assertEquals(1, storeReads)
    }

    @Test
    fun doWorkRetriesARealPendingRequestAndRecoversTheSameJobExactlyOnce() = runBlocking {
        val sourceBytes = "%PDF-pending".toByteArray()
        val source = PdfSource(
            uri = "content://edital",
            fileName = "edital.pdf",
            mimeType = "application/pdf",
            bytes = sourceBytes,
            sha256 = MessageDigest.getInstance("SHA-256").digest(sourceBytes).toHexForTest(),
        )
        val snapshots = WorkerSnapshotStore()
        val sourcePath = snapshots.save(source)
        val pending = PersistedAiJobRequest(
            requestId = "request-pending",
            idempotencyKey = "idem-pending",
            sourceUri = source.uri,
            fileName = source.fileName,
            mimeType = source.mimeType,
            sourceHash = source.sha256,
            sourceBytes = source.bytes.size.toLong(),
            sourcePath = sourcePath,
            status = AiJobStatus.RESERVED.name,
        )
        val store = MutableRequestStore(pending)
        val api = StatefulRecoveryApi()
        val repository = DefaultAiSyllabusRepository(
            api = api,
            sourceReader = PdfSourceReader(PdfSourceProvider { error("recovery must use the private snapshot") }),
            requestStore = store,
            accessTokenProvider = br.com.estudario.data.remote.AiAccessTokenProvider { "supabase-jwt" },
            sourceSnapshots = snapshots,
        )

        val first = AiJobRecoveryWorker(Application(), workerParameters(), repository).doWork()
        assertEquals(androidx.work.ListenableWorker.Result.retry(), first)
        assertEquals(AiJobStatus.PROCESSING.name, store.value.status)
        assertEquals("job-stable", store.value.jobId)

        val second = AiJobRecoveryWorker(Application(), workerParameters(), repository).doWork()

        assertEquals(androidx.work.ListenableWorker.Result.success(), second)
        assertEquals(1, api.processCalls)
        assertEquals(1, api.awaitCalls)
        assertEquals(1, api.idempotencyKeys.distinct().size)
        assertEquals(listOf("job-stable"), api.processedJobIds)
        assertEquals(listOf("job-stable"), api.awaitedJobIds)
    }

    private fun workerParameters(): WorkerParameters = WorkerParameters(
        UUID.randomUUID(),
        Data.EMPTY,
        emptyList(),
        WorkerParameters.RuntimeExtras(),
        0,
        0,
        Executor { it.run() },
        EmptyCoroutineContext,
        object : TaskExecutor {
            private val executor = Executor { it.run() }
            private val serial = object : SerialExecutor {
                override fun execute(command: Runnable) = executor.execute(command)
                override fun hasPendingTasks(): Boolean = false
            }
            override fun getMainThreadExecutor(): Executor = executor
            override fun getSerialTaskExecutor(): SerialExecutor = serial
        },
        DefaultWorkerFactory,
        ProgressUpdater { _: Context, _, _ -> completedFuture() },
        ForegroundUpdater { _: Context, _, _ -> completedFuture() },
    )

    private fun completedFuture() = SettableFuture.create<Void>().also { it.set(null) }
}

private class MutableRequestStore(initial: PersistedAiJobRequest) : AiJobRequestStore {
    var value = initial

    override suspend fun save(value: PersistedAiJobRequest) { this.value = value }
    override suspend fun get(requestId: String): PersistedAiJobRequest? = value.takeIf { it.requestId == requestId }
    override suspend fun list(): List<PersistedAiJobRequest> = listOf(value)
}

private class WorkerSnapshotStore : PdfSourceSnapshotStore {
    private val values = linkedMapOf<String, PdfSource>()

    override fun save(source: PdfSource): String {
        val path = "private/${source.sha256}.pdf"
        values[path] = source.copy(bytes = source.bytes.copyOf())
        return path
    }

    override fun read(path: String, fileName: String): PdfSource = values[path]?.copy(fileName = fileName, bytes = values.getValue(path).bytes.copyOf())
        ?: error("private snapshot missing")
}

private class StatefulRecoveryApi : AiApiClient {
    val idempotencyKeys = mutableListOf<String>()
    val processedJobIds = mutableListOf<String>()
    val awaitedJobIds = mutableListOf<String>()
    var processCalls = 0
    var awaitCalls = 0

    override suspend fun createOrGetJob(idempotencyKey: String, source: AiSourceMetadata, sourceReady: Boolean): AiCreateJob {
        idempotencyKeys += idempotencyKey
        return AiCreateJob("job-stable", AiJobStatus.RESERVED, AiUploadTarget("user/job-stable.pdf", null), sourceReady)
    }

    override suspend fun uploadSource(target: AiUploadTarget, source: PdfSource) = Unit

    override suspend fun processJob(jobId: String): AiJobStatus {
        processCalls += 1
        processedJobIds += jobId
        throw AiProcessTimeoutException(jobId)
    }

    override suspend fun getJob(jobId: String, timeoutMillis: Long?): AiJob = recoveredJob(jobId)

    override suspend fun awaitJob(
        jobId: String,
        policy: AiPollingPolicy,
        sleeper: suspend (Long) -> Unit,
        clockMillis: () -> Long,
    ): AiJob {
        awaitCalls += 1
        awaitedJobIds += jobId
        return recoveredJob(jobId)
    }

    private fun recoveredJob(jobId: String) = AiJob(
        jobId = jobId,
        feature = AiFeature.SYLLABUS_GENERATION,
        status = AiJobStatus.SUCCEEDED,
        schemaVersion = CURRENT_AI_SCHEMA_VERSION,
        promptVersion = "syllabus-v1",
        modelVersion = "gpt-6-luna",
        proposal = AiSyllabusProposal(
            schemaVersion = CURRENT_AI_SCHEMA_VERSION,
            promptVersion = "syllabus-v1",
            modelVersion = "gpt-6-luna",
            documentTitle = "Edital",
            subjects = listOf(AiSubjectProposal("Direito", 0, AiPriority.NORMAL, listOf(AiTopicProposal("Constituição", 0, emptyList(), listOf(1))), listOf(1))),
            warnings = emptyList(),
            ambiguities = emptyList(),
        ),
        warnings = emptyList(),
        errorCode = null,
        errorMessage = null,
        createdAt = "2026-09-24T10:00:00Z",
        updatedAt = "2026-09-24T10:00:01Z",
        finishedAt = "2026-09-24T10:00:01Z",
        providerExecutionStartedAt = "2026-09-24T10:00:00Z",
    )
}

private fun ByteArray.toHexForTest(): String = joinToString("") { "%02x".format(it) }
