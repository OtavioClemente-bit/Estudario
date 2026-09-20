package br.com.estudario.data.planner

import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.CalendarContract
import android.util.Log
import br.com.estudario.data.local.planner.PlanTaskEntity
import br.com.estudario.domain.planner.PlanTaskStatus
import java.time.LocalDate
import java.time.ZoneId
import java.util.TimeZone

class CalendarSyncService(private val context: Context) {

    private val CALENDAR_NAME = "Estudario"
    private val ACCOUNT_NAME = "Estudario"
    private val ACCOUNT_TYPE = CalendarContract.ACCOUNT_TYPE_LOCAL

    fun hasPermissions(): Boolean {
        return context.checkSelfPermission(android.Manifest.permission.WRITE_CALENDAR) == android.content.pm.PackageManager.PERMISSION_GRANTED &&
               context.checkSelfPermission(android.Manifest.permission.READ_CALENDAR) == android.content.pm.PackageManager.PERMISSION_GRANTED
    }

    private fun getOrCreateCalendar(): Long? {
        if (!hasPermissions()) return null

        val projection = arrayOf(CalendarContract.Calendars._ID)
        val selection = "${CalendarContract.Calendars.ACCOUNT_NAME} = ? AND ${CalendarContract.Calendars.ACCOUNT_TYPE} = ? AND ${CalendarContract.Calendars.NAME} = ?"
        val selectionArgs = arrayOf(ACCOUNT_NAME, ACCOUNT_TYPE, CALENDAR_NAME)

        context.contentResolver.query(
            CalendarContract.Calendars.CONTENT_URI,
            projection, selection, selectionArgs, null
        )?.use { cursor ->
            if (cursor.moveToFirst()) {
                return cursor.getLong(0)
            }
        }

        // Criar calendário
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

        val uri = CalendarContract.Calendars.CONTENT_URI.buildUpon()
            .appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, "true")
            .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_NAME, ACCOUNT_NAME)
            .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_TYPE, ACCOUNT_TYPE)
            .build()

        val resultUri = context.contentResolver.insert(uri, values)
        return resultUri?.lastPathSegment?.toLongOrNull()
    }

    suspend fun syncTasks(tasks: List<PlanTaskEntity>) {
        if (!hasPermissions()) return
        val calendarId = getOrCreateCalendar() ?: return

        // Pega todos os eventos já sincronizados no calendário (identificados pela coluna CUSTOM_APP_URI)
        val existingEvents = mutableMapOf<String, Long>()
        val projection = arrayOf(CalendarContract.Events._ID, CalendarContract.Events.CUSTOM_APP_URI)
        val selection = "${CalendarContract.Events.CALENDAR_ID} = ? AND ${CalendarContract.Events.CUSTOM_APP_URI} IS NOT NULL"
        val selectionArgs = arrayOf(calendarId.toString())

        context.contentResolver.query(
            CalendarContract.Events.CONTENT_URI,
            projection, selection, selectionArgs, null
        )?.use { cursor ->
            val idIdx = cursor.getColumnIndexOrThrow(CalendarContract.Events._ID)
            val uriIdx = cursor.getColumnIndexOrThrow(CalendarContract.Events.CUSTOM_APP_URI)
            while (cursor.moveToNext()) {
                val eventId = cursor.getLong(idIdx)
                val taskId = cursor.getString(uriIdx).removePrefix("estudario://task/")
                existingEvents[taskId] = eventId
            }
        }

        // Determinar o que inserir/atualizar
        val tasksToSync = tasks.filter { it.status in listOf(PlanTaskStatus.PLANEJADA, PlanTaskStatus.EM_ANDAMENTO, PlanTaskStatus.CONCLUIDA) }
        val syncedTaskIds = mutableSetOf<String>()

        tasksToSync.forEach { task ->
            syncedTaskIds.add(task.id)
            val startTime = LocalDate.ofEpochDay(task.scheduledEpochDay)
                .atTime(18, 0) // Define um horário padrão no dia (ex: 18:00)
                .atZone(ZoneId.systemDefault())
                .toInstant().toEpochMilli()
            
            val endTime = startTime + (task.plannedMinutes * 60 * 1000L)

            val title = "${task.subjectNameSnapshot} - ${task.topicNameSnapshot ?: "Sessão"}"
            val description = "Atividade de ${task.plannedMinutes} min. Status: ${task.status.labelPtBr()}"

            val values = ContentValues().apply {
                put(CalendarContract.Events.CALENDAR_ID, calendarId)
                put(CalendarContract.Events.TITLE, title)
                put(CalendarContract.Events.DESCRIPTION, description)
                put(CalendarContract.Events.DTSTART, startTime)
                put(CalendarContract.Events.DTEND, endTime)
                put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().id)
                put(CalendarContract.Events.CUSTOM_APP_URI, "estudario://task/${task.id}")
                put(CalendarContract.Events.HAS_ALARM, 0)
            }

            val existingEventId = existingEvents[task.id]
            if (existingEventId != null) {
                // Update
                val updateUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, existingEventId)
                context.contentResolver.update(updateUri, values, null, null)
            } else {
                // Insert
                context.contentResolver.insert(CalendarContract.Events.CONTENT_URI, values)
            }
        }

        // Deletar eventos de tarefas que foram arquivadas/removidas do plano ativo
        val tasksToDelete = existingEvents.filterKeys { !syncedTaskIds.contains(it) }
        tasksToDelete.values.forEach { eventId ->
            val deleteUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, eventId)
            context.contentResolver.delete(deleteUri, null, null)
        }
    }

    private fun PlanTaskStatus.labelPtBr(): String = when (this) {
        PlanTaskStatus.PLANEJADA -> "Planejada"
        PlanTaskStatus.EM_ANDAMENTO -> "Em andamento"
        PlanTaskStatus.CONCLUIDA -> "Concluída"
        PlanTaskStatus.REPROGRAMADA -> "Reprogramada"
        PlanTaskStatus.NAO_REALIZADA -> "Não realizada"
        PlanTaskStatus.PAUSADA -> "Pausada"
    }
}
