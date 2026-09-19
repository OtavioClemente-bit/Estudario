package br.com.estudario.data.preferences

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("settings")

class AppPreferences(private val context: Context) {
    private val darkKey = booleanPreferencesKey("dark_theme")
    private val editalPromptKey = stringPreferencesKey("edital_prompt")
    private val contentPromptKey = stringPreferencesKey("content_prompt")
    private val studyPlanPromptKey = stringPreferencesKey("study_plan_prompt")
    private val notificationsKey = booleanPreferencesKey("notifications_enabled")
    private val dailyReminderKey = booleanPreferencesKey("daily_reminder_enabled")
    private val pendingAlertsKey = booleanPreferencesKey("pending_alerts_enabled")
    private val reminderHourKey = intPreferencesKey("reminder_hour")
    private val reminderMinuteKey = intPreferencesKey("reminder_minute")
    private val themeModeKey = stringPreferencesKey("theme_mode")
    private val questionTimerKey = booleanPreferencesKey("question_timer")
    private val defaultQuestionCountKey = intPreferencesKey("default_question_count")
    private val showExplanationKey = booleanPreferencesKey("show_explanation")
    private val reviewIntervalsKey = stringPreferencesKey("review_intervals")
    private val expandedEditalSubjectsKey = stringPreferencesKey("expanded_edital_subjects")
    private val onboardingCompletedKey = booleanPreferencesKey("onboarding_completed")
    private val seenToursKey = stringPreferencesKey("seen_tours")
    private val userNameKey = stringPreferencesKey("user_name")
    private val userEmailKey = stringPreferencesKey("user_email")
    private val userPhotoKey = stringPreferencesKey("user_photo_path")
    private val dailyGoalKey = intPreferencesKey("daily_goal_questions")
    private val lastCelebratedDayKey = longPreferencesKey("last_celebrated_day")
    private val driveBackupAtKey = longPreferencesKey("drive_last_backup_at")
    private val earnedBadgesKey = stringPreferencesKey("earned_badges")
    // ---- Modo foco: uma sessão de estudo cronometrada, com o Não Perturbe do sistema ligado.
    private val focusStartedAtKey = longPreferencesKey("focus_started_at")
    private val focusTitleKey = stringPreferencesKey("focus_title")
    private val focusTopicKey = longPreferencesKey("focus_topic_id")
    private val focusTaskKey = stringPreferencesKey("focus_task_id")
    private val focusPreviousFilterKey = intPreferencesKey("focus_previous_filter")
    private val focusDndKey = booleanPreferencesKey("focus_do_not_disturb")
    private val focusKeepScreenOnKey = booleanPreferencesKey("focus_keep_screen_on")
    private val lastFocusMinutesKey = intPreferencesKey("focus_last_minutes")
    private val lastFocusTaskKey = stringPreferencesKey("focus_last_task_id")
    val darkTheme: Flow<Boolean> = context.dataStore.data.map { it[darkKey] ?: false }
    val editalPrompt: Flow<String> = context.dataStore.data.map { it[editalPromptKey] ?: PromptTemplates.EDITAL }
    val contentPrompt: Flow<String> = context.dataStore.data.map { it[contentPromptKey] ?: PromptTemplates.CONTEUDO }
    val studyPlanPrompt: Flow<String> = context.dataStore.data.map { it[studyPlanPromptKey] ?: PromptTemplates.PLANO }
    val notificationsEnabled: Flow<Boolean> = context.dataStore.data.map { it[notificationsKey] ?: false }
    val dailyReminderEnabled: Flow<Boolean> = context.dataStore.data.map { it[dailyReminderKey] ?: true }
    val pendingAlertsEnabled: Flow<Boolean> = context.dataStore.data.map { it[pendingAlertsKey] ?: true }
    val reminderHour: Flow<Int> = context.dataStore.data.map { it[reminderHourKey] ?: 19 }
    val reminderMinute: Flow<Int> = context.dataStore.data.map { it[reminderMinuteKey] ?: 0 }
    val themeMode: Flow<String> = context.dataStore.data.map { it[themeModeKey] ?: if (it[darkKey] == true) "DARK" else "SYSTEM" }
    val questionTimer: Flow<Boolean> = context.dataStore.data.map { it[questionTimerKey] ?: true }
    val defaultQuestionCount: Flow<Int> = context.dataStore.data.map { it[defaultQuestionCountKey] ?: 10 }
    val showExplanation: Flow<Boolean> = context.dataStore.data.map { it[showExplanationKey] ?: true }
    val reviewIntervals: Flow<List<Long>> = context.dataStore.data.map { prefs -> prefs[reviewIntervalsKey]?.split(",")?.mapNotNull(String::toLongOrNull)?.takeIf { it.isNotEmpty() } ?: listOf(1, 7, 30) }
    val expandedEditalSubjects: Flow<Set<Long>> = context.dataStore.data.map { prefs ->
        prefs[expandedEditalSubjectsKey]?.split(",")?.mapNotNull(String::toLongOrNull)?.toSet().orEmpty()
    }
    val hasCompletedOnboarding: Flow<Boolean> = context.dataStore.data.map { it[onboardingCompletedKey] ?: false }
    /** Guias (tours) já vistos, por nome. Quem já concluiu o onboarding antigo conta como tendo visto "EDITAL". */
    val seenTours: Flow<Set<String>> = context.dataStore.data.map { prefs ->
        val seen = prefs[seenToursKey]?.split(",")?.filter { it.isNotBlank() }?.toSet().orEmpty()
        if (prefs[onboardingCompletedKey] == true) seen + "EDITAL" else seen
    }
    /** Perfil local. Continua valendo quando a conta Google for ligada: o login só preenche estes campos. */
    val userName: Flow<String> = context.dataStore.data.map { it[userNameKey].orEmpty() }
    val userEmail: Flow<String> = context.dataStore.data.map { it[userEmailKey].orEmpty() }
    val userPhotoPath: Flow<String?> = context.dataStore.data.map { it[userPhotoKey]?.takeIf(String::isNotBlank) }
    val dailyGoalQuestions: Flow<Int> = context.dataStore.data.map { it[dailyGoalKey] ?: 20 }
    /** Dia (epochDay) em que a tela de sequência já foi mostrada — evita comemorar duas vezes. */
    val lastCelebratedDay: Flow<Long> = context.dataStore.data.map { it[lastCelebratedDayKey] ?: 0L }

    /**
     * Sessão de foco em andamento (0 = nenhuma). Fica em disco de propósito: se o app for fechado
     * ou o aparelho reiniciar, ainda dá para encerrar a sessão e devolver o Não Perturbe ao normal.
     */
    val focusSession: Flow<FocusSessionPrefs> = context.dataStore.data.map { prefs ->
        FocusSessionPrefs(
            startedAt = prefs[focusStartedAtKey] ?: 0L,
            title = prefs[focusTitleKey].orEmpty(),
            topicId = prefs[focusTopicKey]?.takeIf { it > 0 },
            taskId = prefs[focusTaskKey]?.takeIf(String::isNotBlank),
            previousFilter = prefs[focusPreviousFilterKey] ?: FocusSessionPrefs.FILTER_UNKNOWN,
        )
    }
    /** Ligar o Não Perturbe do sistema durante a sessão (respeitando as exceções da pessoa). */
    val focusDoNotDisturb: Flow<Boolean> = context.dataStore.data.map { it[focusDndKey] ?: true }
    /** Manter a tela acesa enquanto a sessão está aberta. */
    val focusKeepScreenOn: Flow<Boolean> = context.dataStore.data.map { it[focusKeepScreenOnKey] ?: true }
    /** Minutos da última sessão encerrada, para preencher a conclusão da tarefa do plano. */
    val lastFocusMinutes: Flow<Int> = context.dataStore.data.map { it[lastFocusMinutesKey] ?: 0 }
    val lastFocusTaskId: Flow<String> = context.dataStore.data.map { it[lastFocusTaskKey].orEmpty() }

    suspend fun startFocusSession(startedAt: Long, title: String, topicId: Long?, taskId: String?, previousFilter: Int) {
        context.dataStore.edit { prefs ->
            prefs[focusStartedAtKey] = startedAt
            prefs[focusTitleKey] = title
            prefs[focusTopicKey] = topicId ?: 0L
            prefs[focusTaskKey] = taskId.orEmpty()
            prefs[focusPreviousFilterKey] = previousFilter
        }
    }

    /** Encerra a sessão guardando o tempo medido — nada aqui depende do app estar aberto. */
    suspend fun clearFocusSession(minutes: Int, taskId: String?) {
        context.dataStore.edit { prefs ->
            prefs.remove(focusStartedAtKey)
            prefs.remove(focusTitleKey)
            prefs.remove(focusTopicKey)
            prefs.remove(focusTaskKey)
            prefs.remove(focusPreviousFilterKey)
            prefs[lastFocusMinutesKey] = minutes
            prefs[lastFocusTaskKey] = taskId.orEmpty()
        }
    }

    suspend fun clearLastFocus() { context.dataStore.edit { it[lastFocusMinutesKey] = 0; it[lastFocusTaskKey] = "" } }
    suspend fun setFocusDoNotDisturb(value: Boolean) { context.dataStore.edit { it[focusDndKey] = value } }
    suspend fun setFocusKeepScreenOn(value: Boolean) { context.dataStore.edit { it[focusKeepScreenOnKey] = value } }

    /** Quando o último backup foi enviado ao Google Drive (0 = nenhum). */
    val driveLastBackupAt: Flow<Long> = context.dataStore.data.map { it[driveBackupAtKey] ?: 0L }
    suspend fun setDriveLastBackupAt(value: Long) { context.dataStore.edit { it[driveBackupAtKey] = value } }

    /** Emblemas já anunciados — evita comemorar o mesmo emblema duas vezes. */
    val earnedBadges: Flow<Set<String>> = context.dataStore.data.map { prefs ->
        prefs[earnedBadgesKey]?.split(",")?.filter { it.isNotBlank() }?.toSet().orEmpty()
    }
    suspend fun markBadgesEarned(ids: Collection<String>) {
        if (ids.isEmpty()) return
        context.dataStore.edit { prefs ->
            val current = prefs[earnedBadgesKey]?.split(",")?.filter { it.isNotBlank() }?.toMutableSet() ?: mutableSetOf()
            current += ids
            prefs[earnedBadgesKey] = current.sorted().joinToString(",")
        }
    }

    suspend fun setUserName(value: String) { context.dataStore.edit { it[userNameKey] = value.trim() } }
    suspend fun setUserEmail(value: String) { context.dataStore.edit { it[userEmailKey] = value.trim() } }
    suspend fun setUserPhotoPath(value: String?) {
        context.dataStore.edit { prefs -> if (value.isNullOrBlank()) prefs.remove(userPhotoKey) else prefs[userPhotoKey] = value }
    }
    suspend fun setDailyGoalQuestions(value: Int) { context.dataStore.edit { it[dailyGoalKey] = value.coerceIn(5, 100) } }
    suspend fun setLastCelebratedDay(epochDay: Long) { context.dataStore.edit { it[lastCelebratedDayKey] = epochDay } }

    suspend fun setDarkTheme(enabled: Boolean) { context.dataStore.edit { it[darkKey] = enabled } }
    suspend fun setEditalPrompt(value: String) { context.dataStore.edit { it[editalPromptKey] = value } }
    suspend fun setContentPrompt(value: String) { context.dataStore.edit { it[contentPromptKey] = value } }
    suspend fun setStudyPlanPrompt(value: String) { context.dataStore.edit { it[studyPlanPromptKey] = value } }
    suspend fun setNotificationsEnabled(value: Boolean) { context.dataStore.edit { it[notificationsKey] = value } }
    suspend fun setDailyReminderEnabled(value: Boolean) { context.dataStore.edit { it[dailyReminderKey] = value } }
    suspend fun setPendingAlertsEnabled(value: Boolean) { context.dataStore.edit { it[pendingAlertsKey] = value } }
    suspend fun setReminderTime(hour: Int, minute: Int) { context.dataStore.edit { it[reminderHourKey] = hour; it[reminderMinuteKey] = minute } }
    suspend fun setThemeMode(value: String) { context.dataStore.edit { it[themeModeKey] = value; it[darkKey] = value == "DARK" } }
    suspend fun setQuestionTimer(value: Boolean) { context.dataStore.edit { it[questionTimerKey] = value } }
    suspend fun setDefaultQuestionCount(value: Int) { context.dataStore.edit { it[defaultQuestionCountKey] = value } }
    suspend fun setShowExplanation(value: Boolean) { context.dataStore.edit { it[showExplanationKey] = value } }
    suspend fun setReviewIntervals(value: List<Long>) { context.dataStore.edit { it[reviewIntervalsKey] = value.joinToString(",") } }
    suspend fun setEditalSubjectExpanded(subjectId: Long, expanded: Boolean) {
        context.dataStore.edit { prefs ->
            val current = prefs[expandedEditalSubjectsKey]?.split(",")?.mapNotNull(String::toLongOrNull)?.toMutableSet() ?: mutableSetOf()
            if (expanded) current += subjectId else current -= subjectId
            prefs[expandedEditalSubjectsKey] = current.sorted().joinToString(",")
        }
    }
    suspend fun setOnboardingCompleted(value: Boolean) { context.dataStore.edit { it[onboardingCompletedKey] = value } }
    suspend fun markTourSeen(name: String) {
        context.dataStore.edit { prefs ->
            val current = prefs[seenToursKey]?.split(",")?.filter { it.isNotBlank() }?.toMutableSet() ?: mutableSetOf()
            current += name
            prefs[seenToursKey] = current.sorted().joinToString(",")
            if (name == "EDITAL") prefs[onboardingCompletedKey] = true
        }
    }
}

/** Retrato da sessão de foco guardada em disco. */
data class FocusSessionPrefs(
    val startedAt: Long = 0L,
    val title: String = "",
    val topicId: Long? = null,
    val taskId: String? = null,
    val previousFilter: Int = FILTER_UNKNOWN,
) {
    val active: Boolean get() = startedAt > 0L
    fun elapsedMinutes(now: Long = System.currentTimeMillis()): Int =
        if (!active) 0 else (((now - startedAt) / 60_000L).coerceAtLeast(0L)).toInt()

    companion object {
        /** Não sabemos qual era o filtro antes (permissão negada ou sessão antiga): não mexer nele. */
        const val FILTER_UNKNOWN = -1
    }
}
