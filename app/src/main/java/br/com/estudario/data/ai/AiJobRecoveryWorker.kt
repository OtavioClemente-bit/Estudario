package br.com.estudario.data.ai

import android.content.Context
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import java.util.concurrent.TimeUnit
import br.com.estudario.EstudarioApplication
import kotlinx.coroutines.CancellationException

class AiJobRecoveryWorker(
    context: Context,
    parameters: WorkerParameters,
    private val recoveryOverride: AiJobRecoveryRepository? = null,
) : CoroutineWorker(context, parameters) {
    override suspend fun doWork(): Result {
        val recovery = recoveryOverride ?: (applicationContext as EstudarioApplication).aiSyllabusRepository
        return execute(recovery, runAttemptCount)
    }

    private suspend fun execute(recovery: AiJobRecoveryRepository, attempt: Int): Result {
        if (attempt >= MAX_ATTEMPTS) return Result.failure()
        return try {
            recovery.recoverPendingJobs()
            Result.success()
        } catch (_: AiAuthenticationRequiredException) {
            Result.success()
        } catch (error: CancellationException) {
            throw error
        } catch (_: Throwable) {
            if (shouldRetry(attempt)) Result.retry() else Result.failure()
        }
    }

    companion object {
        const val UNIQUE_WORK_NAME = "ai-syllabus-job-recovery"
        const val MAX_ATTEMPTS = 5
        const val BACKOFF_SECONDS = 30L

        fun shouldRetry(runAttemptCount: Int): Boolean = runAttemptCount < MAX_ATTEMPTS - 1

        fun enqueue(context: Context) {
            val request = OneTimeWorkRequestBuilder<AiJobRecoveryWorker>()
                .setConstraints(recoveryConstraints())
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, BACKOFF_SECONDS, TimeUnit.SECONDS)
                .build()
            WorkManager.getInstance(context.applicationContext).enqueueUniqueWork(
                UNIQUE_WORK_NAME,
                ExistingWorkPolicy.KEEP,
                request,
            )
        }

        fun recoveryConstraints(): Constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
    }
}
