package br.com.estudario.domain

import kotlin.time.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

/**
 * Os dias de atividade (sequência, calendário, Home e foco), num lugar só para o app e o site.
 * Cada lado só converte as suas linhas para estes registros simples.
 */
object DailyActivityBuilder {
    data class Answer(val answeredAt: Long, val correct: Boolean)
    data class Session(val id: Long, val topicId: Long, val completedAt: Long)
    data class Focus(val completedAt: Long, val durationSeconds: Long, val free: Boolean)
    data class QuestionRun(val startedAt: Long, val completedAt: Long, val questionCount: Int)

    fun build(
        answers: List<Answer>,
        reviewedAt: List<Long>,
        sessions: List<Session>,
        focusSessions: List<Focus>,
        executionsCompletedAt: List<Long>,
        questionRuns: List<QuestionRun>,
        zone: TimeZone,
    ): List<DailyActivity> {
        fun day(ms: Long): LocalDate = Instant.fromEpochMilliseconds(ms).toLocalDateTime(zone).date
        val rows = HashMap<LocalDate, DailyActivity>()
        fun merge(date: LocalDate, block: (DailyActivity) -> DailyActivity) { rows[date] = block(rows[date] ?: DailyActivity(date)) }
        answers.forEach { a -> merge(day(a.answeredAt)) { it.copy(questions = it.questions + 1, correct = it.correct + if (a.correct) 1 else 0) } }
        reviewedAt.forEach { at -> merge(day(at)) { it.copy(reviews = it.reviews + 1) } }
        // "Tópico estudado" vale uma vez por tópico, no dia da primeira sessão dele.
        val firstSessionIds = sessions.filter { it.topicId != 0L }.groupBy { it.topicId }.mapTo(hashSetOf()) { (_, r) -> r.minBy { it.completedAt }.id }
        sessions.forEach { s -> merge(day(s.completedAt)) { it.copy(studySessions = it.studySessions + if (s.id in firstSessionIds) 1 else 0) } }
        // Minutos de foco do dia: sessões que se sobrepõem (duas abertas ao mesmo tempo) contam uma vez só.
        fun focusSpans(rows: List<Focus>) = rows.mapNotNull { StudyTime.focus(it.completedAt, it.durationSeconds) }
        val freeByDay = StudyTime.minutesByDay(focusSpans(focusSessions.filter { it.free }), zone)
        focusSessions.groupBy { day(it.completedAt) }.forEach { (date, rowsForDay) ->
            merge(date) { it.copy(focusSessions = it.focusSessions + rowsForDay.size) }
        }
        StudyTime.minutesByDay(focusSpans(focusSessions), zone).forEach { (date, m) ->
            merge(date) { it.copy(focusMinutes = m, freeFocusMinutes = freeByDay[date] ?: 0) }
        }
        executionsCompletedAt.forEach { at -> merge(day(at)) { it.copy(planTasks = it.planTasks + 1) } }
        // O tempo do dia: foco + baterias de questões, contando uma vez só o que se sobrepõe.
        val questionSpans = questionRuns.mapNotNull { StudyTime.questions(it.startedAt, it.completedAt, it.questionCount) }
        val spans = focusSessions.mapNotNull { StudyTime.focus(it.completedAt, it.durationSeconds) } + questionSpans
        StudyTime.minutesByDay(spans, zone).forEach { (date, m) -> merge(date) { it.copy(minutes = m) } }
        StudyTime.minutesByDay(questionSpans, zone).forEach { (date, m) -> merge(date) { it.copy(questionMinutes = m) } }
        return rows.values.toList()
    }
}
