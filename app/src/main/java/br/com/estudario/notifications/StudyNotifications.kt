package br.com.estudario.notifications

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.work.*
import br.com.estudario.MainActivity
import br.com.estudario.EstudarioApplication
import br.com.estudario.R
import br.com.estudario.data.preferences.AppPreferences
import br.com.estudario.focus.FocusMode
import kotlinx.coroutines.flow.first
import java.time.Duration
import java.time.ZonedDateTime
import java.util.concurrent.TimeUnit

object StudyNotificationCoordinator {
    private const val CHANNEL_STUDY = "study_reminders"
    private const val CHANNEL_PENDING = "pending_study"
    private const val DAILY_WORK = "daily_study_reminder"
    private const val PENDING_WORK = "pending_study_check"

    fun createChannels(context: Context) {
        val manager = context.getSystemService(NotificationManager::class.java)
        manager.createNotificationChannels(listOf(
            NotificationChannel(CHANNEL_STUDY, "Lembretes de estudo", NotificationManager.IMPORTANCE_DEFAULT).apply {
                description = "Lembrete diário no horário escolhido"
                enableVibration(true)
            },
            NotificationChannel(CHANNEL_PENDING, "Pendências e revisões", NotificationManager.IMPORTANCE_HIGH).apply {
                description = "Revisões vencidas e itens pendentes da fila de estudos"
                enableVibration(true)
            },
            // Silencioso de propósito: é o controle da sessão, não um aviso. Passa pelo Não
            // Perturbe porque é justamente o botão que desliga o Não Perturbe.
            NotificationChannel(FocusMode.CHANNEL, "Modo foco", NotificationManager.IMPORTANCE_LOW).apply {
                description = "Sessão de estudo em andamento, com o botão de encerrar"
                setShowBadge(false)
                enableVibration(false)
                setSound(null, null)
                setBypassDnd(true)
            },
        ))
    }

    suspend fun refresh(context: Context, preferences: AppPreferences) {
        val work = WorkManager.getInstance(context)
        if (!preferences.notificationsEnabled.first()) {
            work.cancelUniqueWork(DAILY_WORK)
            work.cancelUniqueWork(PENDING_WORK)
            return
        }
        if (preferences.dailyReminderEnabled.first()) scheduleDaily(context, preferences.reminderHour.first(), preferences.reminderMinute.first())
        else work.cancelUniqueWork(DAILY_WORK)
        if (preferences.pendingAlertsEnabled.first()) schedulePending(context)
        else work.cancelUniqueWork(PENDING_WORK)
    }

    fun scheduleDaily(context: Context, hour: Int, minute: Int) {
        val now = ZonedDateTime.now()
        var next = now.withHour(hour).withMinute(minute).withSecond(0).withNano(0)
        if (!next.isAfter(now)) next = next.plusDays(1)
        val request = OneTimeWorkRequestBuilder<DailyStudyReminderWorker>()
            .setInitialDelay(Duration.between(now, next).toMillis(), TimeUnit.MILLISECONDS)
            .addTag(DAILY_WORK)
            .build()
        WorkManager.getInstance(context).enqueueUniqueWork(DAILY_WORK, ExistingWorkPolicy.REPLACE, request)
    }

    private fun schedulePending(context: Context) {
        val request = PeriodicWorkRequestBuilder<PendingStudyWorker>(12, TimeUnit.HOURS)
            .setInitialDelay(2, TimeUnit.HOURS)
            .addTag(PENDING_WORK)
            .build()
        WorkManager.getInstance(context).enqueueUniquePeriodicWork(PENDING_WORK, ExistingPeriodicWorkPolicy.UPDATE, request)
    }

    fun showTest(context: Context) = post(
        context, CHANNEL_STUDY, 9001,
        "Hora de avançar no seu concurso",
        "Seu lembrete está funcionando. Abra o app e continue de onde parou.",
    )

    // Lint não reconhece a checagem de permissão logo abaixo como guarda válida para o notify().
    @SuppressLint("MissingPermission")
    internal fun post(context: Context, channel: String, id: Int, title: String, body: String) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) return
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra("notification_destination", if (channel == CHANNEL_PENDING) "reviews" else "home")
        }
        val pendingIntent = PendingIntent.getActivity(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        val notification = NotificationCompat.Builder(context, channel)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(body)
            .setStyle(NotificationCompat.BigTextStyle().bigText(body))
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .setPriority(if (channel == CHANNEL_PENDING) NotificationCompat.PRIORITY_HIGH else NotificationCompat.PRIORITY_DEFAULT)
            .build()
        NotificationManagerCompat.from(context).notify(id, notification)
    }
}

class DailyStudyReminderWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        val app = applicationContext as EstudarioApplication
        if (!app.preferences.notificationsEnabled.first() || !app.preferences.dailyReminderEnabled.first()) return Result.success()
        // Modo foco ativo: o lembrete pode esperar: interromper agora é o oposto do que a pessoa pediu.
        if (app.preferences.focusSession.first().active) {
            StudyNotificationCoordinator.scheduleDaily(applicationContext, app.preferences.reminderHour.first(), app.preferences.reminderMinute.first())
            return Result.success()
        }
        val due = app.database.dao().reviewsOnce().count { it.completedAt == null && it.ignoredAt == null && it.dueAt <= System.currentTimeMillis() }
        val queued = app.database.dao().queueOnce().count { !it.paused }
        val body = when {
            due > 0 && queued > 0 -> "Você tem $due revisão(ões) pendente(s) e $queued item(ns) na fila. Um bloco curto hoje mantém o ritmo."
            due > 0 -> "Você tem $due revisão(ões) pendente(s). Faça uma agora para fortalecer a memória."
            queued > 0 -> "Há $queued item(ns) na sua fila de estudos. Continue do próximo bloco."
            else -> "Abra o Estudário e avance um tópico hoje. Consistência vale mais que uma sessão perfeita."
        }
        StudyNotificationCoordinator.post(applicationContext, "study_reminders", 1001, "Seu estudo de hoje está esperando", body)
        StudyNotificationCoordinator.scheduleDaily(applicationContext, app.preferences.reminderHour.first(), app.preferences.reminderMinute.first())
        return Result.success()
    }
}

class PendingStudyWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        val app = applicationContext as EstudarioApplication
        if (!app.preferences.notificationsEnabled.first() || !app.preferences.pendingAlertsEnabled.first()) return Result.success()
        if (app.preferences.focusSession.first().active) return Result.success()
        val due = app.database.dao().reviewsOnce().count { it.completedAt == null && it.ignoredAt == null && it.dueAt <= System.currentTimeMillis() }
        val queued = app.database.dao().queueOnce().count { !it.paused }
        val lastSession = app.database.dao().sessionsOnce().maxOfOrNull { it.completedAt } ?: 0L
        val queueIsWaiting = queued > 0 && System.currentTimeMillis() - lastSession >= TimeUnit.HOURS.toMillis(24)
        val body = when {
            due > 0 && queueIsWaiting -> "$due revisão(ões) venceram e $queued item(ns) continuam na fila. Comece pela revisão mais antiga."
            due > 0 -> "$due revisão(ões) já estão disponíveis. Resolva as mais antigas primeiro."
            queueIsWaiting -> "$queued item(ns) aguardam na fila e não há sessão concluída nas últimas 24 horas. Que tal um bloco curto?"
            else -> null
        }
        if (body != null) StudyNotificationCoordinator.post(applicationContext, "pending_study", 1002, "Pendências do seu plano de estudo", body)
        return Result.success()
    }
}
