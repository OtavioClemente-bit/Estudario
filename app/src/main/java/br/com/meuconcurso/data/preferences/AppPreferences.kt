package br.com.meuconcurso.data.preferences

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("settings")

class AppPreferences(private val context: Context) {
    private val darkKey = booleanPreferencesKey("dark_theme")
    private val editalPromptKey = stringPreferencesKey("edital_prompt")
    private val contentPromptKey = stringPreferencesKey("content_prompt")
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
    val darkTheme: Flow<Boolean> = context.dataStore.data.map { it[darkKey] ?: false }
    val editalPrompt: Flow<String> = context.dataStore.data.map { it[editalPromptKey] ?: PromptTemplates.EDITAL }
    val contentPrompt: Flow<String> = context.dataStore.data.map { it[contentPromptKey] ?: PromptTemplates.CONTEUDO }
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
    suspend fun setDarkTheme(enabled: Boolean) { context.dataStore.edit { it[darkKey] = enabled } }
    suspend fun setEditalPrompt(value: String) { context.dataStore.edit { it[editalPromptKey] = value } }
    suspend fun setContentPrompt(value: String) { context.dataStore.edit { it[contentPromptKey] = value } }
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
}
