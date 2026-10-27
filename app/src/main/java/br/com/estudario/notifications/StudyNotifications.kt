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
import br.com.estudario.domain.StudyNudges
import br.com.estudario.focus.FocusMode
import kotlinx.coroutines.flow.first
import java.time.Duration
import java.time.ZonedDateTime
import java.util.concurrent.TimeUnit

/**
 * Os avisos do Folha com o app fechado, no ritmo dos apps de hábito (as falas em StudyNudges):
 *  - de manhã (9h): a revisão que vence hoje, com o nome do tópico;
 *  - no horário de costume: o lembrete do dia (sumido há dias, marco da prova, quase na meta, revisão, plano…);
 *  - 21h: a sequência em risco, se o dia ainda não foi salvo;
 *  - 22h30: a última chance, se continua sem estudo;
 *  - domingo 19h: o resumo da semana.
 * No máximo [DAILY_CAP] avisos de estudo por dia, nenhum durante o modo foco e nenhum depois de o
 * dia estar garantido (a não ser revisão vencida).
 */
object StudyNotificationCoordinator {
    private const val CHANNEL_STUDY = "study_reminders"
    private const val CHANNEL_PENDING = "pending_study"
    private const val DAILY_WORK = "daily_study_reminder"
    private const val PENDING_WORK = "pending_study_check"
    private const val MORNING_WORK = "morning_review_reminder"
    private const val WEEKLY_WORK = "weekly_study_summary"
    private const val GUARD_WORK = "streak_guard"
    private const val LAST_CALL_WORK = "streak_last_call"
    internal const val SNOOZE_WORK = "reminder_snooze"
    internal const val ACTION_SNOOZE = "br.com.estudario.SNOOZE_REMINDER"
    private const val MORNING_HOUR = 9
    /** Fim do dia: o aviso de que a sequência ainda não foi garantida. */
    private const val GUARD_HOUR = 21
    private const val DAILY_CAP = 3

    fun createChannels(context: Context) {
        val manager = context.getSystemService(NotificationManager::class.java)
        manager.createNotificationChannels(listOf(
            NotificationChannel(CHANNEL_STUDY, "Lembretes de estudo", NotificationManager.IMPORTANCE_HIGH).apply {
                description = "Lembrete do dia, sequência em risco e resumo da semana"
                enableVibration(true)
            },
            NotificationChannel(CHANNEL_PENDING, "Pendências e revisões", NotificationManager.IMPORTANCE_HIGH).apply {
                description = "A revisão que vence hoje, de manhã"
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
        // o aviso antigo de pendências (a cada 12 horas, genérico) deu lugar ao da manhã
        work.cancelUniqueWork(PENDING_WORK)
        if (!preferences.notificationsEnabled.first()) {
            listOf(DAILY_WORK, MORNING_WORK, WEEKLY_WORK, GUARD_WORK, LAST_CALL_WORK).forEach(work::cancelUniqueWork)
            return
        }
        if (preferences.dailyReminderEnabled.first()) scheduleWeekly(context) else work.cancelUniqueWork(WEEKLY_WORK)
        if (preferences.dailyReminderEnabled.first()) reminderTime(context, preferences).let { (h, m) -> scheduleDaily(context, h, m) }
        else work.cancelUniqueWork(DAILY_WORK)
        if (preferences.streakGuardEnabled.first()) { scheduleGuard(context); scheduleLastCall(context) }
        else { work.cancelUniqueWork(GUARD_WORK); work.cancelUniqueWork(LAST_CALL_WORK) }
        if (preferences.pendingAlertsEnabled.first()) scheduleMorning(context) else work.cancelUniqueWork(MORNING_WORK)
    }

    private inline fun <reified W : ListenableWorker> scheduleAt(context: Context, name: String, hour: Int, minute: Int) {
        val now = ZonedDateTime.now()
        var next = now.withHour(hour).withMinute(minute).withSecond(0).withNano(0)
        if (!next.isAfter(now)) next = next.plusDays(1)
        val request = OneTimeWorkRequestBuilder<W>()
            .setInitialDelay(Duration.between(now, next).toMillis(), TimeUnit.MILLISECONDS)
            .addTag(name)
            .build()
        WorkManager.getInstance(context).enqueueUniqueWork(name, ExistingWorkPolicy.REPLACE, request)
    }

    fun scheduleDaily(context: Context, hour: Int, minute: Int) = scheduleAt<DailyStudyReminderWorker>(context, DAILY_WORK, hour, minute)
    fun scheduleMorning(context: Context) = scheduleAt<PendingStudyWorker>(context, MORNING_WORK, MORNING_HOUR, 0)
    fun scheduleGuard(context: Context) = scheduleAt<StreakGuardWorker>(context, GUARD_WORK, GUARD_HOUR, 0)
    fun scheduleLastCall(context: Context) = scheduleAt<StreakLastCallWorker>(context, LAST_CALL_WORK, 22, 30)

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

    fun showTest(context: Context) = post(
        context, CHANNEL_STUDY, 9001,
        "Oi! Sou eu, o Folha 👋",
        "Seus avisos estão funcionando. Eu te chamo na hora das revisões e se a sequência estiver em risco.",
    )

    // ------------------------------------------------------------------ o dia de hoje

    /** O que o Folha sabe do dia para escolher a fala (tudo do banco local e do resumo do widget). */
    internal suspend fun facts(context: Context): StudyNudges.Facts {
        val app = context.applicationContext as EstudarioApplication
        val dao = app.database.dao()
        val now = System.currentTimeMillis()
        val widget = br.com.estudario.ui.widget.FolhaWidget.read(context)
        val topicName = runCatching { dao.topicsOnce().associate { it.id to it.title } }.getOrDefault(emptyMap())
        val dueReviews = dao.reviewsOnce().filter { it.completedAt == null && it.ignoredAt == null && it.dueAt <= endOfToday() }.sortedBy { it.dueAt }
        val goal = widget?.goal?.takeIf { it > 0 } ?: 0
        val doneToday = widget?.todayQuestions ?: 0
        val epoch = java.time.LocalDate.now().toEpochDay()
        val task = runCatching {
            app.database.plannerDao().tasksOnce().firstOrNull { it.scheduledEpochDay == epoch && (it.status == br.com.estudario.domain.planner.PlanTaskStatus.PLANEJADA || it.status == br.com.estudario.domain.planner.PlanTaskStatus.EM_ANDAMENTO) }
        }.getOrNull()
        val weak = runCatching {
            val topicSubject = dao.topicsOnce().associate { it.id to it.subjectId }
            val subjectName = dao.subjectsOnce().associate { it.id to it.name }
            val questionTopic = dao.questionsOnce().associate { it.question.id to it.question.topicId }
            val answers = dao.attemptsOnce().map { a -> val sid = questionTopic[a.questionId]?.let { topicSubject[it] }; br.com.estudario.domain.StudyInsights.Answer(a.answeredAt, a.correct, sid, sid?.let { subjectName[it] }) }
            br.com.estudario.domain.StudyInsights.weakSpot(answers, now)
        }.getOrNull()
        val plan = runCatching { app.database.plannerDao().plansOnce().firstOrNull { it.active && !it.archived } }.getOrNull()
        val competition = runCatching { dao.competitionsOnce().let { list -> list.firstOrNull { it.id == plan?.competitionId } ?: list.firstOrNull { it.isPrimary } } }.getOrNull()
        return StudyNudges.Facts(
            name = runCatching { app.preferences.userName.first() }.getOrNull(),
            streak = widget?.streak ?: 0,
            daysAway = widget?.daysAway ?: 0,
            dueReviews = dueReviews.size,
            firstReview = dueReviews.firstOrNull()?.let { topicName[it.topicId] },
            missing = if (goal > 0 && widget?.todayDone == false) (goal - doneToday).coerceAtLeast(0) else 0,
            doneToday = doneToday,
            todayTask = task?.let { it.topicNameSnapshot ?: it.subjectNameSnapshot },
            taskMinutes = task?.plannedMinutes ?: 0,
            weakSubject = weak?.subject,
            weakAccuracy = weak?.accuracy ?: 0,
            daysToExam = plan?.examEpochDay?.let { (it - epoch).toInt() }?.takeIf { it >= 0 },
            competition = competition?.name?.take(40),
        )
    }

    internal suspend fun studiedToday(context: Context): Boolean {
        val app = context.applicationContext as EstudarioApplication
        val dao = app.database.dao()
        val today = java.time.LocalDate.now().atStartOfDay(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli()
        val any = dao.attemptsOnce().any { it.answeredAt >= today } || dao.sessionsOnce().any { it.completedAt >= today } || dao.reviewHistoryOnce().any { it.reviewedAt >= today }
        return br.com.estudario.ui.widget.FolhaWidget.read(context)?.todayDone ?: any
    }

    private fun endOfToday(): Long = java.time.LocalDate.now().plusDays(1).atStartOfDay(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli() - 1

    /** Teto de avisos de estudo por dia: conta e diz se ainda cabe mais um hoje. */
    internal fun takeSlot(context: Context): Boolean {
        val p = context.getSharedPreferences("study_nudges", Context.MODE_PRIVATE)
        val day = java.time.LocalDate.now().toEpochDay()
        val used = if (p.getLong("day", -1) == day) p.getInt("count", 0) else 0
        if (used >= DAILY_CAP) return false
        p.edit().putLong("day", day).putInt("count", used + 1).apply()
        return true
    }

    /** Mostra a fala escolhida (variação do dia, sem repetir a última do mesmo tipo). */
    internal fun postNudge(context: Context, channel: String, id: Int, kind: StudyNudges.Kind, f: StudyNudges.Facts, actions: Boolean = true) {
        val p = context.getSharedPreferences("study_nudges", Context.MODE_PRIVATE)
        val day = java.time.LocalDate.now().toEpochDay().toInt()
        var seed = day + kind.ordinal * 7
        if (p.getInt("last_${kind.name}", -1) == seed) seed++
        p.edit().putInt("last_${kind.name}", seed).apply()
        val n = StudyNudges.pick(kind, f, seed)
        val mood = runCatching { br.com.estudario.ui.assistant.FolhaMood.valueOf(n.mood) }.getOrDefault(br.com.estudario.ui.assistant.FolhaMood.HAPPY)
        post(context, channel, id, n.title, n.body, mood = mood, actions = actions)
    }

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
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setColor(0xFF3326CE.toInt())
            .apply {
                if (actions) {
                    addAction(0, if (channel == CHANNEL_PENDING) "Revisar agora" else "Começar agora", pendingIntent)
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

/** O lembrete do dia, no horário de costume (ou no escolhido). */
class DailyStudyReminderWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        val app = applicationContext as EstudarioApplication
        val snoozed = inputData.getBoolean("snooze", false)
        suspend fun again() { if (!snoozed) StudyNotificationCoordinator.reminderTime(applicationContext, app.preferences).let { (h, m) -> StudyNotificationCoordinator.scheduleDaily(applicationContext, h, m) } }
        if (!app.preferences.notificationsEnabled.first() || !app.preferences.dailyReminderEnabled.first()) return Result.success()
        // Modo foco ativo: o lembrete pode esperar: interromper agora é o oposto do que a pessoa pediu.
        if (app.preferences.focusSession.first().active) { again(); return Result.success() }
        val f = StudyNotificationCoordinator.facts(applicationContext)
        // Dia já garantido e nenhuma revisão vencida: não precisa chamar.
        if (StudyNotificationCoordinator.studiedToday(applicationContext) && f.dueReviews == 0) { again(); return Result.success() }
        if (snoozed || StudyNotificationCoordinator.takeSlot(applicationContext)) {
            StudyNotificationCoordinator.postNudge(applicationContext, "study_reminders", 1001, StudyNudges.dailyKind(f), f)
        }
        again()
        return Result.success()
    }
}

/** De manhã (9h): a revisão que vence hoje, com o nome do tópico. (Nome antigo mantido: é o trabalho já agendado.) */
class PendingStudyWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        val app = applicationContext as EstudarioApplication
        if (app.preferences.notificationsEnabled.first() && app.preferences.pendingAlertsEnabled.first() && !app.preferences.focusSession.first().active) {
            val f = StudyNotificationCoordinator.facts(applicationContext)
            if (f.dueReviews > 0 && StudyNotificationCoordinator.takeSlot(applicationContext)) {
                StudyNotificationCoordinator.postNudge(applicationContext, "pending_study", 1002, StudyNudges.Kind.REVIEW_MORNING, f)
            }
        }
        StudyNotificationCoordinator.scheduleMorning(applicationContext)
        return Result.success()
    }
}

/**
 * Fim do dia (21h): se há sequência (1 dia ou mais) e hoje ainda não teve estudo, o Folha avisa que
 * ela acaba à meia-noite. Já estudou, modo foco ou desligado: silêncio.
 */
class StreakGuardWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        val app = applicationContext as EstudarioApplication
        val prefs = app.preferences
        if (prefs.notificationsEnabled.first() && prefs.streakGuardEnabled.first() && !prefs.focusSession.first().active) {
            val f = StudyNotificationCoordinator.facts(applicationContext)
            if (!StudyNotificationCoordinator.studiedToday(applicationContext) && f.streak >= 1 && StudyNotificationCoordinator.takeSlot(applicationContext)) {
                StudyNotificationCoordinator.postNudge(applicationContext, "study_reminders", 1004, StudyNudges.Kind.STREAK_RISK, f)
            }
        }
        StudyNotificationCoordinator.scheduleGuard(applicationContext)
        return Result.success()
    }
}

/** 22h30: a última chance, se a sequência continua em jogo e o dia não foi salvo (fora do teto do dia). */
class StreakLastCallWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        val app = applicationContext as EstudarioApplication
        val prefs = app.preferences
        if (prefs.notificationsEnabled.first() && prefs.streakGuardEnabled.first() && !prefs.focusSession.first().active) {
            val f = StudyNotificationCoordinator.facts(applicationContext)
            if (!StudyNotificationCoordinator.studiedToday(applicationContext) && f.streak >= 2) {
                StudyNotificationCoordinator.postNudge(applicationContext, "study_reminders", 1004, StudyNudges.Kind.LAST_CALL, f)
            }
        }
        StudyNotificationCoordinator.scheduleLastCall(applicationContext)
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

/** O botão "Lembrar em 1 hora" das notificações de estudo. */
class ReminderActionReceiver : android.content.BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != StudyNotificationCoordinator.ACTION_SNOOZE) return
        NotificationManagerCompat.from(context).cancel(intent.getIntExtra("id", 1001))
        StudyNotificationCoordinator.snooze(context)
    }
}
