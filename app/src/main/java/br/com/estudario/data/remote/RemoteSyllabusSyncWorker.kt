package br.com.estudario.data.remote

import android.content.Context
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import br.com.estudario.EstudarioApplication
import br.com.estudario.data.ai.AiAuthenticationRequiredException
import br.com.estudario.data.local.AppDatabase
import br.com.estudario.data.local.RemoteSyllabusSyncEntity
import br.com.estudario.data.local.RemoteSyllabusSyncOperation
import br.com.estudario.data.local.RemoteSyllabusSyncState as LocalRemoteSyllabusSyncState
import java.util.UUID
import java.util.concurrent.TimeUnit

interface RemoteSyllabusSyncGateway {
    suspend fun pending(now: Long): List<RemoteSyllabusSyncEntity>
    suspend fun failed(now: Long): List<RemoteSyllabusSyncEntity>
    suspend fun requeue(row: RemoteSyllabusSyncEntity, attemptToken: String, now: Long, updatedAt: Long): Boolean
    suspend fun claim(row: RemoteSyllabusSyncEntity, attemptToken: String, nextAttemptAt: Long, updatedAt: Long): Boolean
    suspend fun expectedRemoteSyllabusId(row: RemoteSyllabusSyncEntity): String
    suspend fun sync(row: RemoteSyllabusSyncEntity): RemoteSyllabusSyncAcknowledgement
    suspend fun markSynced(row: RemoteSyllabusSyncEntity, remoteSyllabusId: String, attemptToken: String, updatedAt: Long): Boolean
    suspend fun markFailed(row: RemoteSyllabusSyncEntity, attemptToken: String, error: String, nextAttemptAt: Long, updatedAt: Long): Boolean
}

data class RemoteSyllabusSyncRunResult(val attempted: Int, val synced: Int, val failed: Int) {
    val shouldRetry: Boolean get() = failed > 0
}

class RemoteSyllabusSyncRunner(
    private val gateway: RemoteSyllabusSyncGateway,
    private val now: () -> Long = { System.currentTimeMillis() },
    private val tokenFactory: () -> String = { UUID.randomUUID().toString() },
    private val backoffMillis: (Int) -> Long = { attempt -> 30_000L * (1L shl (attempt.coerceIn(1, 6) - 1)) },
) {
    suspend fun run(): RemoteSyllabusSyncRunResult {
        val currentTime = now()
        val failedRows = gateway.failed(currentTime)
        val pendingRows = gateway.pending(currentTime)
        var attempted = 0
        var synced = 0
        var failed = 0
        for (original in failedRows + pendingRows) {
            val token = tokenFactory()
            val claimed = if (original.state == LocalRemoteSyllabusSyncState.FAILED) {
                gateway.requeue(original, token, currentTime, currentTime)
            } else {
                gateway.claim(original, token, currentTime + backoffMillis(original.attemptCount + 1), currentTime)
            }
            if (!claimed) continue
            attempted++
            val current = original.copy(state = LocalRemoteSyllabusSyncState.PENDING, attemptToken = token, attemptCount = original.attemptCount + 1)
            try {
                val acknowledgement = gateway.sync(current)
                check(acknowledgement.state == RemoteSyllabusSyncState.SYNCED) { "Remote server did not acknowledge synchronization." }
                check(acknowledgement.payloadHash == current.payloadHash) { "Remote server acknowledged a different payload hash." }
                val expectedRemoteSyllabusId = gateway.expectedRemoteSyllabusId(current)
                check(acknowledgement.remoteSyllabusId != null && acknowledgement.remoteSyllabusId == expectedRemoteSyllabusId) {
                    "Remote server acknowledged a different syllabus identity."
                }
                if (gateway.markSynced(current, acknowledgement.remoteSyllabusId, token, now())) {
                    synced++
                } else {
                    // The remote mutation is already acknowledged, but the local CAS lost a race.
                    // Keep the outbox retryable instead of reporting a false success.
                    gateway.markFailed(
                        current,
                        token,
                        "CAS_CONFLICT",
                        now() + backoffMillis(current.attemptCount),
                        now(),
                    )
                    failed++
                }
            } catch (error: Throwable) {
                gateway.markFailed(current, token, safeError(error), now() + backoffMillis(current.attemptCount), now())
                failed++
            }
        }
        return RemoteSyllabusSyncRunResult(attempted, synced, failed)
    }

    private fun safeError(error: Throwable): String = when (error) {
        is PrivateSyllabusApiException -> error.code
        is AiAuthenticationRequiredException -> "AUTH_REQUIRED"
        else -> "SYNC_FAILED"
    }
}

private class RoomRemoteSyllabusSyncGateway(
    private val database: AppDatabase,
    private val repository: PrivateSyllabusRepository,
) : RemoteSyllabusSyncGateway {
    private val dao = database.dao()

    override suspend fun pending(now: Long): List<RemoteSyllabusSyncEntity> = dao.pendingRemoteSyllabusSync(now)

    override suspend fun failed(now: Long): List<RemoteSyllabusSyncEntity> = dao.failedRemoteSyllabusSync(now)

    override suspend fun requeue(row: RemoteSyllabusSyncEntity, attemptToken: String, now: Long, updatedAt: Long): Boolean =
        dao.requeueRemoteSync(row.id, row.attemptToken, attemptToken, now, updatedAt) == 1

    override suspend fun claim(row: RemoteSyllabusSyncEntity, attemptToken: String, nextAttemptAt: Long, updatedAt: Long): Boolean =
        dao.markRemoteSyncAttempt(row.id, row.attemptToken, attemptToken, nextAttemptAt, updatedAt) == 1

    override suspend fun expectedRemoteSyllabusId(row: RemoteSyllabusSyncEntity): String = repository.expectedRemoteSyllabusId(row)

    override suspend fun sync(row: RemoteSyllabusSyncEntity): RemoteSyllabusSyncAcknowledgement = repository.syncOutbox(row)

    override suspend fun markSynced(row: RemoteSyllabusSyncEntity, remoteSyllabusId: String, attemptToken: String, updatedAt: Long): Boolean =
        dao.markRemoteSyncSyncedAndAssociate(
            id = row.id,
            localSyllabusId = row.localSyllabusId,
            remoteSyllabusId = remoteSyllabusId,
            attemptToken = attemptToken,
            updatedAt = updatedAt,
            associateCompetition = row.operation == RemoteSyllabusSyncOperation.UPSERT,
        )

    override suspend fun markFailed(row: RemoteSyllabusSyncEntity, attemptToken: String, error: String, nextAttemptAt: Long, updatedAt: Long): Boolean =
        dao.markRemoteSyncFailed(row.id, attemptToken, error, nextAttemptAt, updatedAt) == 1
}

class RemoteSyllabusSyncWorker(
    context: Context,
    parameters: WorkerParameters,
) : CoroutineWorker(context, parameters) {
    override suspend fun doWork(): Result {
        val runner = run {
            val application = applicationContext as EstudarioApplication
            if (!application.supabaseClientConfig.isConfigured) return Result.success()
            RemoteSyllabusSyncRunner(
                RoomRemoteSyllabusSyncGateway(application.database, application.privateSyllabusRepository),
            )
        }
        val outcome = runner.run()
        return if (outcome.shouldRetry && runAttemptCount < MAX_ATTEMPTS) Result.retry() else Result.success()
    }

    companion object {
        const val UNIQUE_WORK_NAME = "private-syllabus-sync"
        const val MAX_ATTEMPTS = 5
        const val BACKOFF_SECONDS = 30L

        fun constraints(): Constraints = Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()

        fun enqueue(context: Context) {
            val request = OneTimeWorkRequestBuilder<RemoteSyllabusSyncWorker>()
                .setConstraints(constraints())
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, BACKOFF_SECONDS, TimeUnit.SECONDS)
                .build()
            WorkManager.getInstance(context.applicationContext).enqueueUniqueWork(UNIQUE_WORK_NAME, ExistingWorkPolicy.KEEP, request)
        }
    }
}
