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
