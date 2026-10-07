package br.com.estudario.data.sync

import android.content.Context
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import br.com.estudario.EstudarioApplication
import java.util.concurrent.TimeUnit

/**
 * Sincroniza em segundo plano: ao abrir e ao sair do app, e a cada poucas horas. Conflito não é
 * resolvido aqui; fica marcado para a pessoa escolher na tela de sincronização.
 */
class CloudSyncWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        val app = applicationContext as EstudarioApplication
        return when (app.cloudSync.syncIfEnabled()) {
            is CloudSyncStatus.Failed -> if (runAttemptCount < 3) Result.retry() else Result.success()
            else -> Result.success()
        }
    }

    companion object {
        private const val NOW = "cloud-sync-now"
        private const val PERIODIC = "cloud-sync-periodic"
        private val network = Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()

        fun syncSoon(context: Context, delaySeconds: Long = 0) {
            WorkManager.getInstance(context).enqueueUniqueWork(
                NOW,
                ExistingWorkPolicy.REPLACE,
                OneTimeWorkRequestBuilder<CloudSyncWorker>()
                    .setConstraints(network)
                    .setInitialDelay(delaySeconds, TimeUnit.SECONDS)
                    .build(),
            )
        }

        fun schedulePeriodic(context: Context) {
            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                PERIODIC,
                ExistingPeriodicWorkPolicy.KEEP,
                PeriodicWorkRequestBuilder<CloudSyncWorker>(6, TimeUnit.HOURS).setConstraints(network).build(),
            )
        }
    }
}
