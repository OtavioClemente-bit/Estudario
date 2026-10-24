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
    private const val GUARD_WORK = "streak_guard"
    internal const val SNOOZE_WORK = "reminder_snooze"
    internal const val ACTION_SNOOZE = "br.com.estudario.SNOOZE_REMINDER"
    /** Fim do dia: o aviso de que a sequência ainda não foi garantida. */
    private const val GUARD_HOUR = 21

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
            work.cancelUniqueWork(GUARD_WORK)
            return
        }
        if (preferences.dailyReminderEnabled.first()) scheduleWeekly(context) else work.cancelUniqueWork(WEEKLY_WORK)
        if (preferences.dailyReminderEnabled.first()) reminderTime(context, preferences).let { (h, m) -> scheduleDaily(context, h, m) }
        else work.cancelUniqueWork(DAILY_WORK)
        if (preferences.streakGuardEnabled.first()) scheduleGuard(context) else work.cancelUniqueWork(GUARD_WORK)
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

    /**
     * O minuto do dia em que a pessoa costuma começar a estudar (mediana do primeiro estudo de cada dia
     * nas últimas 3 semanas); nulo com menos de 4 dias de histórico.
     */
    suspend fun usualStudyMinute(context: Context): Int? {
        val app = context.applicationContext as EstudarioApplication
        val dao = app.database.dao()
        val zone = java.time.ZoneId.systemDefault()
        val since = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(21)
        val starts = dao.attemptsOnce().map { it.answeredAt } + dao.reviewHistoryOnce().map { it.reviewedAt } +
            dao.sessionsOnce().map { it.completedAt - it.durationSeconds * 1000L } + dao.focusSessionsOnce().map { it.completedAt - it.durationSeconds * 1000L }
        val firstPerDay = starts.filter { it >= since }
            .map { java.time.Instant.ofEpochMilli(it).atZone(zone) }
            .groupBy { it.toLocalDate() }
            .map { (_, list) -> list.minOf { it.hour * 60 + it.minute } }
        return br.com.estudario.domain.StudyInsights.usualStart(firstPerDay)
    }

    /** O horário do lembrete: o aprendido (um pouco antes do horário de costume) ou o fixo escolhido. */
    suspend fun reminderTime(context: Context, preferences: AppPreferences): Pair<Int, Int> {
        if (preferences.smartReminderEnabled.first()) usualStudyMinute(context)?.let(br.com.estudario.domain.StudyInsights::reminderMinute)?.let { m ->
            return m / 60 to m % 60
        }
        return preferences.reminderHour.first() to preferences.reminderMinute.first()
    }

    /** O aviso da sequência: todo dia às 21h (só chega se a sequência ainda não foi garantida). */
    fun scheduleGuard(context: Context) {
        val now = ZonedDateTime.now()
        var next = now.withHour(GUARD_HOUR).withMinute(0).withSecond(0).withNano(0)
        if (!next.isAfter(now)) next = next.plusDays(1)
        val request = OneTimeWorkRequestBuilder<StreakGuardWorker>()
            .setInitialDelay(Duration.between(now, next).toMillis(), TimeUnit.MILLISECONDS)
            .addTag(GUARD_WORK)
            .build()
        WorkManager.getInstance(context).enqueueUniqueWork(GUARD_WORK, ExistingWorkPolicy.REPLACE, request)
    }

    /** "Lembrar em 1 hora": o mesmo lembrete volta daqui a uma hora. */
    fun snooze(context: Context) {
        val request = OneTimeWorkRequestBuilder<DailyStudyReminderWorker>()
            .setInitialDelay(1, TimeUnit.HOURS)
            .setInputData(workDataOf("snooze" to true))
            .addTag(SNOOZE_WORK)
            .build()
        WorkManager.getInstance(context).enqueueUniqueWork(SNOOZE_WORK, ExistingWorkPolicy.REPLACE, request)
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
        /** Botões "Começar agora" e "Lembrar em 1 hora" (lembretes de estudo). */
        actions: Boolean = false,
    ) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) return
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
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
            .setColor(0xFF3326CE.toInt())
            .apply {
                if (actions) {
                    addAction(0, "Começar agora", pendingIntent)
                    val snooze = PendingIntent.getBroadcast(
                        context, id + 50, Intent(context, ReminderActionReceiver::class.java).setAction(ACTION_SNOOZE).putExtra("id", id),
                        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
                    )
                    addAction(0, "Lembrar em 1 hora", snooze)
                }
            }
            .build()
        NotificationManagerCompat.from(context).notify(id, notification)
    }
}

class DailyStudyReminderWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        val app = applicationContext as EstudarioApplication
        if (!app.preferences.notificationsEnabled.first() || !app.preferences.dailyReminderEnabled.first()) return Result.success()
        // Modo foco ativo: o lembrete pode esperar: interromper agora é o oposto do que a pessoa pediu.
        val snoozed = inputData.getBoolean("snooze", false)
        // Modo foco ativo: o lembrete pode esperar: interromper agora é o oposto do que a pessoa pediu.
        if (app.preferences.focusSession.first().active) {
            if (!snoozed) StudyNotificationCoordinator.reminderTime(applicationContext, app.preferences).let { (h, m) -> StudyNotificationCoordinator.scheduleDaily(applicationContext, h, m) }
            return Result.success()
        }
        val dao = app.database.dao()
        val due = dao.reviewsOnce().count { it.completedAt == null && it.ignoredAt == null && it.dueAt <= System.currentTimeMillis() }
        val queued = dao.queueOnce().count { !it.paused }
        // Dia já garantido e nenhuma revisão vencida: não precisa chamar. (Começou e não fechou o dia:
        // ainda vale lembrar quanto falta.)
        val today = java.time.LocalDate.now().atStartOfDay(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli()
        val studiedToday = dao.attemptsOnce().any { it.answeredAt >= today } || dao.sessionsOnce().any { it.completedAt >= today } ||
            dao.reviewHistoryOnce().any { it.reviewedAt >= today }
        val dayDone = br.com.estudario.ui.widget.FolhaWidget.read(applicationContext)?.todayDone ?: studiedToday
        if (dayDone && due == 0) {
            if (!snoozed) StudyNotificationCoordinator.reminderTime(applicationContext, app.preferences).let { (h, m) -> StudyNotificationCoordinator.scheduleDaily(applicationContext, h, m) }
            return Result.success()
        }
        // o ponto fraco da semana vira a sugestão do lembrete
        val weak = runCatching {
            val topicSubject = dao.topicsOnce().associate { it.id to it.subjectId }
            val subjectName = dao.subjectsOnce().associate { it.id to it.name }
            val questionTopic = dao.questionsOnce().associate { it.question.id to it.question.topicId }
            val answers = dao.attemptsOnce().map { a -> val sid = questionTopic[a.questionId]?.let { topicSubject[it] }; br.com.estudario.domain.StudyInsights.Answer(a.answeredAt, a.correct, sid, sid?.let { subjectName[it] }) }
            br.com.estudario.domain.StudyInsights.weakSpot(answers, System.currentTimeMillis())
        }.getOrNull()
        val widget = br.com.estudario.ui.widget.FolhaWidget.read(applicationContext)
        val streak = widget?.streak ?: 0
        // O que está pendente de verdade: questões que faltam para fechar o dia, a revisão que vence hoje
        // (com o nome do tópico) e a tarefa de hoje no plano. O título usa o mais concreto.
        val goal = widget?.goal?.takeIf { it > 0 } ?: 0
        val doneToday = widget?.todayQuestions ?: 0
        val missing = if (goal > 0 && widget?.todayDone == false && doneToday > 0) (goal - doneToday).coerceAtLeast(0) else 0
        val topicName = runCatching { dao.topicsOnce().associate { it.id to it.title } }.getOrDefault(emptyMap())
        val dueReviews = dao.reviewsOnce().filter { it.completedAt == null && it.ignoredAt == null && it.dueAt <= System.currentTimeMillis() }.sortedBy { it.dueAt }
        val firstReview = dueReviews.firstOrNull()?.let { topicName[it.topicId] }
        val todayTask = runCatching {
            val epoch = java.time.LocalDate.now().toEpochDay()
            app.database.plannerDao().tasksOnce().firstOrNull { it.scheduledEpochDay == epoch && (it.status == br.com.estudario.domain.planner.PlanTaskStatus.PLANEJADA || it.status == br.com.estudario.domain.planner.PlanTaskStatus.EM_ANDAMENTO) }
        }.getOrNull()
        val concrete: Pair<String, String>? = when {
            streak >= 2 && missing in 1..15 -> "Faltam $missing ${if (missing == 1) "questão" else "questões"} para manter seus $streak dias 🔥" to "Você já fez $doneToday hoje. Mais $missing e o dia está garantido."
            firstReview != null -> (if (dueReviews.size > 1) "Sua revisão de $firstReview vence hoje (e mais ${dueReviews.size - 1})" else "Sua revisão de $firstReview vence hoje") to "Uns ${StudyCopy.minutes(dueReviews.size)}. Revisar no dia certo é o que fixa o conteúdo."
            todayTask != null -> "Hoje no plano: ${todayTask.topicNameSnapshot ?: todayTask.subjectNameSnapshot ?: "seu próximo bloco"}" to "${todayTask.plannedMinutes} min de ${when (todayTask.type) { br.com.estudario.domain.planner.PlanTaskType.THEORY -> "teoria"; br.com.estudario.domain.planner.PlanTaskType.QUESTIONS -> "questões"; br.com.estudario.domain.planner.PlanTaskType.REVIEW -> "revisão"; else -> "estudo" }}." + if (streak >= 2) " E a sequência de $streak dias continua 🔥" else ""
            else -> null
        }
        val body = concrete?.second ?: when {
            due > 0 && queued > 0 -> "${StudyCopy.reviews(due)} e ${StudyCopy.items(queued)} na fila. ${StudyCopy.minutes(due)} de revisão já mantêm o ritmo."
            due > 0 -> "${StudyCopy.reviews(due)} para hoje, uns ${StudyCopy.minutes(due)}. Revisar no dia certo é o que fixa o conteúdo."
            queued > 0 -> "${StudyCopy.items(queued)} no seu plano. Continue do próximo bloco."
            weak != null -> "Sugestão de hoje: 10 questões de ${weak.subject}, a matéria que mais te derrubou essa semana (${weak.accuracy}% de acerto)."
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
        val title = when {
            snoozed -> "Passou uma hora: bora agora?"
            concrete != null -> concrete.first
            streak >= 2 -> "Bora manter os $streak dias seguidos?"
            else -> calls[java.time.LocalDate.now().dayOfYear % calls.size]
        }
        StudyNotificationCoordinator.post(applicationContext, "study_reminders", 1001, title, body, actions = true)
        if (!snoozed) StudyNotificationCoordinator.reminderTime(applicationContext, app.preferences).let { (h, m) -> StudyNotificationCoordinator.scheduleDaily(applicationContext, h, m) }
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

/**
 * Fim do dia (21h): se a sequência está em jogo (2 dias ou mais) e hoje ainda não teve estudo, o Folha
 * avisa uma vez, com o tamanho exato do que salva o dia. Já estudou, modo foco ou desligado: silêncio.
 */
class StreakGuardWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        val app = applicationContext as EstudarioApplication
        val prefs = app.preferences
        if (prefs.notificationsEnabled.first() && prefs.streakGuardEnabled.first() && !prefs.focusSession.first().active) {
            val dao = app.database.dao()
            val today = java.time.LocalDate.now().atStartOfDay(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli()
            val studiedToday = dao.attemptsOnce().any { it.answeredAt >= today } || dao.sessionsOnce().any { it.completedAt >= today } ||
                dao.reviewHistoryOnce().any { it.reviewedAt >= today }
            val day = br.com.estudario.ui.widget.FolhaWidget.read(applicationContext)
            val streak = day?.streak ?: 0
            if (!studiedToday && streak >= 2) {
                val goal = day?.goal?.takeIf { it > 0 } ?: 10
                StudyNotificationCoordinator.post(
                    applicationContext, "study_reminders", 1004,
                    "Seus $streak dias seguidos acabam à meia-noite",
                    "Ainda dá tempo! ${if (goal <= 15) "$goal questões" else "Uma tarefa do plano ou uma revisão"} já garante o dia. Eu fico aqui torcendo.",
                    mood = br.com.estudario.ui.assistant.FolhaMood.SURPRISED, actions = true,
                )
            }
        }
        StudyNotificationCoordinator.scheduleGuard(applicationContext)
        return Result.success()
    }
}

/** O botão "Lembrar em 1 hora" das notificações de estudo. */
class ReminderActionReceiver : android.content.BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != StudyNotificationCoordinator.ACTION_SNOOZE) return
        NotificationManagerCompat.from(context).cancel(intent.getIntExtra("id", 1001))
        StudyNotificationCoordinator.snooze(context)
    }
}
