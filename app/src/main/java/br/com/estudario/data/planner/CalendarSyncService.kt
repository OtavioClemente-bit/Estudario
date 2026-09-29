package br.com.estudario.data.planner

import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.provider.CalendarContract
import br.com.estudario.data.local.planner.PlanTaskEntity
import br.com.estudario.domain.planner.PlanTaskStatus
import java.time.LocalDate
import java.time.ZoneId
import java.util.TimeZone
import br.com.estudario.ui.planner.displayNamePtBr

/**
 * Plano de estudo na agenda do celular, num calendário próprio ("Plano de Estudos - Estudário").
 *
 * Só escreve quando a pessoa ligou a sincronização na tela Agenda. Cada dia começa no horário
 * escolhido e as atividades vêm em sequência, sem se sobrepor. Apagar remove o calendário inteiro
 * (e com ele todos os eventos), sem tocar em nenhum outro calendário da pessoa.
 */
class CalendarSyncService(private val context: Context) {

    data class Settings(
        val enabled: Boolean = false,
        val startHour: Int = 18,
        val startMinute: Int = 0,
        /** Minutos de antecedência do lembrete; 0 = sem lembrete. */
        val reminderMinutes: Int = 10,
        /** Quantos dias à frente entram na agenda; 0 = o plano inteiro. */
        val daysAhead: Int = 30,
        val lastSyncAt: Long = 0,
        val lastSyncCount: Int = 0,
    )

    private val prefs = context.getSharedPreferences("calendar_sync", Context.MODE_PRIVATE)

    fun settings(): Settings = Settings(
        enabled = prefs.getBoolean("enabled", false),
        startHour = prefs.getInt("hour", 18),
        startMinute = prefs.getInt("minute", 0),
        reminderMinutes = prefs.getInt("reminder", 10),
        daysAhead = prefs.getInt("days", 30),
        lastSyncAt = prefs.getLong("last_at", 0),
        lastSyncCount = prefs.getInt("last_count", 0),
    )

    fun saveSettings(value: Settings) {
        prefs.edit()
            .putBoolean("enabled", value.enabled)
            .putInt("hour", value.startHour)
            .putInt("minute", value.startMinute)
            .putInt("reminder", value.reminderMinutes)
            .putInt("days", value.daysAhead)
            .apply()
    }

    fun hasPermissions(): Boolean =
        context.checkSelfPermission(android.Manifest.permission.WRITE_CALENDAR) == android.content.pm.PackageManager.PERMISSION_GRANTED &&
            context.checkSelfPermission(android.Manifest.permission.READ_CALENDAR) == android.content.pm.PackageManager.PERMISSION_GRANTED

    private fun asSyncAdapter(uri: Uri): Uri = uri.buildUpon()
        .appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, "true")
        .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_NAME, ACCOUNT_NAME)
        .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_TYPE, ACCOUNT_TYPE)
        .build()

    private fun findCalendar(): Long? {
        val selection = "${CalendarContract.Calendars.ACCOUNT_NAME} = ? AND ${CalendarContract.Calendars.ACCOUNT_TYPE} = ? AND ${CalendarContract.Calendars.NAME} = ?"
        context.contentResolver.query(
            CalendarContract.Calendars.CONTENT_URI, arrayOf(CalendarContract.Calendars._ID), selection,
            arrayOf(ACCOUNT_NAME, ACCOUNT_TYPE, CALENDAR_NAME), null,
        )?.use { cursor -> if (cursor.moveToFirst()) return cursor.getLong(0) }
        return null
    }

    private fun getOrCreateCalendar(): Long? {
        if (!hasPermissions()) return null
        findCalendar()?.let { return it }
        val values = ContentValues().apply {
            put(CalendarContract.Calendars.ACCOUNT_NAME, ACCOUNT_NAME)
            put(CalendarContract.Calendars.ACCOUNT_TYPE, ACCOUNT_TYPE)
            put(CalendarContract.Calendars.NAME, CALENDAR_NAME)
            put(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME, "Plano de Estudos - Estudário")
            put(CalendarContract.Calendars.CALENDAR_COLOR, 0xFF4F46E5.toInt())
            put(CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL, CalendarContract.Calendars.CAL_ACCESS_OWNER)
            put(CalendarContract.Calendars.OWNER_ACCOUNT, ACCOUNT_NAME)
            put(CalendarContract.Calendars.VISIBLE, 1)
            put(CalendarContract.Calendars.SYNC_EVENTS, 1)
            put(CalendarContract.Calendars.CALENDAR_TIME_ZONE, TimeZone.getDefault().id)
        }
        return context.contentResolver.insert(asSyncAdapter(CalendarContract.Calendars.CONTENT_URI), values)?.lastPathSegment?.toLongOrNull()
    }

    /** Quantos eventos do Estudário estão na agenda agora. */
    fun eventCount(): Int {
        if (!hasPermissions()) return 0
        val calendarId = findCalendar() ?: return 0
        return context.contentResolver.query(
            CalendarContract.Events.CONTENT_URI, arrayOf(CalendarContract.Events._ID),
            "${CalendarContract.Events.CALENDAR_ID} = ? AND ${CalendarContract.Events.DELETED} = 0", arrayOf(calendarId.toString()), null,
        )?.use { it.count } ?: 0
    }

    /**
     * Escreve (ou atualiza) as atividades do plano. Ignora a chamada se a sincronização estiver
     * desligada: o replanejamento chama isto sempre, mas só quem escolheu vê a agenda mudar.
     * Devolve quantos eventos ficaram na agenda.
     */
    suspend fun syncTasks(tasks: List<PlanTaskEntity>, force: Boolean = false): Int {
        val settings = settings()
        if ((!settings.enabled && !force) || !hasPermissions()) return 0
        val calendarId = getOrCreateCalendar() ?: return 0

        val existingEvents = mutableMapOf<String, Long>()
        context.contentResolver.query(
            CalendarContract.Events.CONTENT_URI,
            arrayOf(CalendarContract.Events._ID, CalendarContract.Events.CUSTOM_APP_URI),
            "${CalendarContract.Events.CALENDAR_ID} = ? AND ${CalendarContract.Events.CUSTOM_APP_URI} IS NOT NULL",
            arrayOf(calendarId.toString()), null,
        )?.use { cursor ->
            while (cursor.moveToNext()) existingEvents[cursor.getString(1).removePrefix("estudario://task/")] = cursor.getLong(0)
        }

        val today = LocalDate.now().toEpochDay()
        val lastDay = if (settings.daysAhead > 0) today + settings.daysAhead else Long.MAX_VALUE
        val toSync = tasks
            .filter { it.status in setOf(PlanTaskStatus.PLANEJADA, PlanTaskStatus.EM_ANDAMENTO, PlanTaskStatus.CONCLUIDA) }
            .filter { it.scheduledEpochDay in today..lastDay }
            .sortedBy { it.scheduledEpochDay }
        val synced = mutableSetOf<String>()
        val zone = ZoneId.systemDefault()

        // Dentro do dia, uma atividade depois da outra a partir do horário escolhido.
        toSync.groupBy { it.scheduledEpochDay }.forEach { (day, dayTasks) ->
            var start = LocalDate.ofEpochDay(day).atTime(settings.startHour, settings.startMinute).atZone(zone).toInstant().toEpochMilli()
            dayTasks.forEach { task ->
                synced += task.id
                val end = start + task.plannedMinutes.coerceAtLeast(5) * 60_000L
                val done = task.status == PlanTaskStatus.CONCLUIDA
                val values = ContentValues().apply {
                    put(CalendarContract.Events.CALENDAR_ID, calendarId)
                    put(CalendarContract.Events.TITLE, (if (done) "✓ " else "") + (task.topicNameSnapshot ?: task.subjectNameSnapshot ?: "Sessão de estudos"))
                    put(
                        CalendarContract.Events.DESCRIPTION,
                        listOfNotNull(task.subjectNameSnapshot, "${task.type.displayNamePtBr()} · ${task.plannedMinutes} min", "Abra o Estudário para começar.").joinToString("\n"),
                    )
                    put(CalendarContract.Events.DTSTART, start)
                    put(CalendarContract.Events.DTEND, end)
                    put(CalendarContract.Events.EVENT_TIMEZONE, zone.id)
                    put(CalendarContract.Events.CUSTOM_APP_URI, "estudario://task/${task.id}")
                    put(CalendarContract.Events.HAS_ALARM, if (settings.reminderMinutes > 0 && !done) 1 else 0)
                }
                val eventId = existingEvents[task.id]?.also { id ->
                    context.contentResolver.update(ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, id), values, null, null)
                } ?: context.contentResolver.insert(CalendarContract.Events.CONTENT_URI, values)?.lastPathSegment?.toLongOrNull()
                if (eventId != null) setReminder(eventId, if (done) 0 else settings.reminderMinutes)
                start = end
            }
        }

        existingEvents.filterKeys { it !in synced }.values.forEach { id ->
            context.contentResolver.delete(ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, id), null, null)
        }
        prefs.edit().putLong("last_at", System.currentTimeMillis()).putInt("last_count", synced.size).apply()
        return synced.size
    }

    private fun setReminder(eventId: Long, minutes: Int) {
        context.contentResolver.delete(CalendarContract.Reminders.CONTENT_URI, "${CalendarContract.Reminders.EVENT_ID} = ?", arrayOf(eventId.toString()))
        if (minutes <= 0) return
        context.contentResolver.insert(CalendarContract.Reminders.CONTENT_URI, ContentValues().apply {
            put(CalendarContract.Reminders.EVENT_ID, eventId)
            put(CalendarContract.Reminders.MINUTES, minutes)
            put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT)
        })
    }

    /** Tira o plano da agenda: apaga só o calendário do Estudário e desliga a sincronização. */
    fun removeAll(): Boolean {
        saveSettings(settings().copy(enabled = false))
        prefs.edit().putLong("last_at", 0).putInt("last_count", 0).apply()
        if (!hasPermissions()) return false
        val calendarId = findCalendar() ?: return true
        return context.contentResolver.delete(asSyncAdapter(ContentUris.withAppendedId(CalendarContract.Calendars.CONTENT_URI, calendarId)), null, null) > 0
    }

    private companion object {
        const val CALENDAR_NAME = "Estudario"
        const val ACCOUNT_NAME = "Estudario"
        const val ACCOUNT_TYPE = CalendarContract.ACCOUNT_TYPE_LOCAL
    }
}
