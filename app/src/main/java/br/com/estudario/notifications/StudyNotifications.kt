package br.com.estudario.notifications

import android.Manifest
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
    private const val WEEKLY_WORK = "weekly_study_summary"

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
            work.cancelUniqueWork(WEEKLY_WORK)
            return
        }
        if (preferences.dailyReminderEnabled.first()) scheduleWeekly(context) else work.cancelUniqueWork(WEEKLY_WORK)
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

    /** Resumo da semana: domingo às 19h. */
    fun scheduleWeekly(context: Context) {
        val now = ZonedDateTime.now()
        var next = now.with(java.time.temporal.TemporalAdjusters.nextOrSame(java.time.DayOfWeek.SUNDAY)).withHour(19).withMinute(0).withSecond(0).withNano(0)
        if (!next.isAfter(now)) next = next.plusWeeks(1)
        val request = OneTimeWorkRequestBuilder<WeeklySummaryWorker>()
            .setInitialDelay(Duration.between(now, next).toMillis(), TimeUnit.MILLISECONDS)
            .addTag(WEEKLY_WORK)
            .build()
        WorkManager.getInstance(context).enqueueUniqueWork(WEEKLY_WORK, ExistingWorkPolicy.KEEP, request)
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

    internal fun post(
        context: Context, channel: String, id: Int, title: String, body: String,
        mood: br.com.estudario.ui.assistant.FolhaMood = br.com.estudario.ui.assistant.FolhaMood.HAPPY,
    ) {
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
            // O Folha chamando, ao lado do texto.
            .setLargeIcon(runCatching { br.com.estudario.ui.assistant.folhaBitmap(256, mood) }.getOrNull())
            .setSubText("Folha, seu assistente")
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
        val dao = app.database.dao()
        val due = dao.reviewsOnce().count { it.completedAt == null && it.ignoredAt == null && it.dueAt <= System.currentTimeMillis() }
        val queued = dao.queueOnce().count { !it.paused }
        // Já estudou hoje e não tem revisão vencida: não precisa chamar.
        val today = java.time.LocalDate.now().atStartOfDay(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli()
        val studiedToday = dao.attemptsOnce().any { it.answeredAt >= today } || dao.sessionsOnce().any { it.completedAt >= today } ||
            dao.reviewHistoryOnce().any { it.reviewedAt >= today }
        if (studiedToday && due == 0) {
            StudyNotificationCoordinator.scheduleDaily(applicationContext, app.preferences.reminderHour.first(), app.preferences.reminderMinute.first())
            return Result.success()
        }
        val body = when {
            due > 0 && queued > 0 -> "${StudyCopy.reviews(due)} e ${StudyCopy.items(queued)} na fila. ${StudyCopy.minutes(due)} de revisão já mantêm o ritmo."
            due > 0 -> "${StudyCopy.reviews(due)} para hoje, uns ${StudyCopy.minutes(due)}. Revisar no dia certo é o que fixa o conteúdo."
            queued > 0 -> "${StudyCopy.items(queued)} no seu plano. Continue do próximo bloco."
            else -> "Abra o Estudário e avance um tópico hoje. Constância vale mais que uma sessão perfeita."
        }
        // O Folha chamando para estudar, com uma fala diferente a cada dia.
        val calls = listOf(
            "Ei, bora estudar?",
            "Seu estudo de hoje está esperando",
            "Um bloquinho hoje e a posse fica mais perto",
            "Tô aqui com o seu plano pronto!",
            "Bora manter a sequência?",
        )
        val title = calls[java.time.LocalDate.now().dayOfYear % calls.size]
        StudyNotificationCoordinator.post(applicationContext, "study_reminders", 1001, title, body)
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
            due > 0 && queueIsWaiting -> "${StudyCopy.reviews(due)} esperando e ${StudyCopy.items(queued)} na fila. Comece pela revisão mais antiga."
            due > 0 -> "${StudyCopy.reviews(due)} já ${if (due == 1) "está disponível" else "estão disponíveis"}. Comece pela mais antiga."
            queueIsWaiting -> "${StudyCopy.items(queued)} no plano e nenhum estudo nas últimas 24 horas. Que tal um bloco curto?"
            else -> null
        }
        if (body != null) StudyNotificationCoordinator.post(applicationContext, "pending_study", 1002, "Pendências do seu plano de estudo", body)
        return Result.success()
    }
}

/** Textos das notificações com o plural certo ("2 revisões", não "revisão(ões)"). */
internal object StudyCopy {
    fun reviews(n: Int) = if (n == 1) "1 revisão" else "$n revisões"
    fun items(n: Int) = if (n == 1) "1 item" else "$n itens"
    /** Uma revisão leva uns 3 minutos. */
    fun minutes(reviews: Int) = "${(reviews * 3).coerceAtLeast(3)} minutos"
    fun hours(seconds: Long): String {
        val m = (seconds / 60).toInt()
        return when {
            m < 60 -> "$m min"
            m % 60 == 0 -> "${m / 60}h"
            else -> "${m / 60}h${(m % 60).toString().padStart(2, '0')}"
        }
    }
}

/** Resumo da semana no domingo à noite: dias de estudo, tempo, questões e o acerto comparado com a semana anterior. */
class WeeklySummaryWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        val app = applicationContext as EstudarioApplication
        if (!app.preferences.notificationsEnabled.first() || !app.preferences.dailyReminderEnabled.first()) return Result.success()
        val dao = app.database.dao()
        val zone = java.time.ZoneId.systemDefault()
        val start = java.time.LocalDate.now().minusDays(6).atStartOfDay(zone).toInstant().toEpochMilli()
        val previous = start - TimeUnit.DAYS.toMillis(7)
        fun day(ms: Long) = java.time.Instant.ofEpochMilli(ms).atZone(zone).toLocalDate()
        val attempts = dao.attemptsOnce()
        val week = attempts.filter { it.answeredAt >= start }
        val before = attempts.filter { it.answeredAt in previous until start }
        val sessions = dao.sessionsOnce().filter { it.completedAt >= start }
        val focus = dao.focusSessionsOnce().filter { it.completedAt >= start }
        val days = (week.map { day(it.answeredAt) } + sessions.map { day(it.completedAt) } + focus.map { day(it.completedAt) } +
            dao.reviewHistoryOnce().filter { it.reviewedAt >= start }.map { day(it.reviewedAt) }).toSet().size
        val seconds = maxOf(sessions.sumOf { it.durationSeconds }, focus.sumOf { it.durationSeconds })
        if (days == 0) {
            StudyNotificationCoordinator.post(applicationContext, "study_reminders", 1003, "Semana nova, página nova",
                "Essa semana passou sem estudo, e tudo bem. Amanhã é segunda: um tópico só já recomeça o ritmo.")
        } else {
            val accuracy = if (week.isEmpty()) null else week.count { it.correct } * 100 / week.size
            val beforeAccuracy = if (before.size < 10) null else before.count { it.correct } * 100 / before.size
            val parts = buildList {
                add(if (days == 1) "1 dia de estudo" else "$days dias de estudo")
                if (seconds >= 60) add(StudyCopy.hours(seconds) + " de foco")
                if (week.isNotEmpty()) add((if (week.size == 1) "1 questão" else "${week.size} questões") + " com $accuracy% de acerto")
            }
            val trend = when {
                accuracy == null || beforeAccuracy == null -> ""
                accuracy - beforeAccuracy >= 3 -> " Seu acerto subiu ${accuracy - beforeAccuracy} pontos em relação à semana passada."
                beforeAccuracy - accuracy >= 3 -> " O acerto caiu ${beforeAccuracy - accuracy} pontos: vale revisar os erros antes de avançar."
                else -> " Acerto firme em relação à semana passada."
            }
            val title = if (days >= 5) "Que semana! Você estudou em $days dias" else "Sua semana no Estudário"
            StudyNotificationCoordinator.post(applicationContext, "study_reminders", 1003, title, parts.joinToString(", ") + "." + trend)
        }
        // Agenda o próximo domingo depois de mostrar este.
        val next = OneTimeWorkRequestBuilder<WeeklySummaryWorker>().setInitialDelay(7, TimeUnit.DAYS).addTag("weekly_study_summary").build()
        WorkManager.getInstance(applicationContext).enqueueUniqueWork("weekly_study_summary", ExistingWorkPolicy.APPEND_OR_REPLACE, next)
        return Result.success()
    }
}
