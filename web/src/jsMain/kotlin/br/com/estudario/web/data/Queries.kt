package br.com.estudario.web.data

import br.com.estudario.domain.DailyActivity
import br.com.estudario.domain.DailyGoal
import br.com.estudario.domain.StreakEngine
import br.com.estudario.domain.StreakSummary
import br.com.estudario.time.epochMillisToDate
import br.com.estudario.time.startOfDay
import br.com.estudario.time.toEpochDay
import br.com.estudario.time.today
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone

/** Contas que várias telas usam, feitas sobre a foto carregada (mesmas regras do app). */
object Queries {
    val zone: TimeZone get() = TimeZone.currentSystemDefault()
    fun todayDate(): LocalDate = today(zone)
    fun todayEpoch(): Long = todayDate().toEpochDay()
    fun dateOf(epochMillis: Long): LocalDate = epochMillisToDate(epochMillis, zone)
    fun endOfToday(): Long = todayDate().let { LocalDate.fromEpochDays(it.toEpochDay() + 1) }.startOfDay(zone).toEpochMilliseconds()

    fun primaryCompetition(data: Snapshot): Competition? =
        data.competitions.firstOrNull { it.primary } ?: data.competitions.firstOrNull()

    fun activePlan(data: Snapshot): StudyPlan? =
        data.plans.firstOrNull { it.active && !it.archived } ?: data.plans.firstOrNull { !it.archived }

    fun tasksOn(data: Snapshot, planId: String, epochDay: Long): List<PlanTask> =
        data.tasks.filter { it.planId == planId && it.day == epochDay }.sortedWith(compareBy({ it.status == "CONCLUIDA" }, { typeOrder(it.type) }))

    private fun typeOrder(type: String) = listOf("REVIEW", "THEORY", "QUESTIONS", "ACTIVE_RECALL", "FLASHCARDS", "DISCURSIVE", "SIMULATION").indexOf(type).let { if (it < 0) 99 else it }

    /** Tópicos que contam no edital: os que não viraram grupo de subtópicos. */
    fun leafTopics(data: Snapshot, subjectIds: Set<Long>? = null): List<Topic> {
        val topics = if (subjectIds == null) data.topics else data.topics.filter { it.subjectId in subjectIds }
        val parents = topics.mapNotNullTo(hashSetOf()) { it.parentTopicId }
        return topics.filter { it.id !in parents }
    }

    fun isStudied(topic: Topic) = topic.status != "NAO_ESTUDADO"

    fun subjectsOf(data: Snapshot, competitionId: Long?): List<Subject> =
        data.subjects.filter { competitionId == null || it.competitionId == competitionId }.sortedBy { it.position }

    fun dueReviews(data: Snapshot): List<Review> {
        val end = endOfToday()
        return data.reviews.filter { it.completedAt == null && it.ignoredAt == null && it.dueAt < end }.sortedBy { it.dueAt }
    }

    fun pendingErrors(data: Snapshot): List<ErrorEntry> = data.errors.filter { it.status != "CORRIGIDO" }

    /** Banco de questões: sem as ocultas e sem as de simulado ainda não entregue (como no app). */
    fun visibleQuestions(data: Snapshot): List<Question> {
        val open = data.simulations.filter { it.status != "FINISHED" }.mapTo(hashSetOf()) { it.id }
        return data.questions.filter { !it.hidden && (it.simulationId == null || it.simulationId !in open) }
    }

    /** Mesmo cálculo de AppViewModel.dailyActivity no Android. */
    fun dailyActivity(data: Snapshot): List<DailyActivity> {
        val rows = HashMap<LocalDate, DailyActivity>()
        fun merge(date: LocalDate, block: (DailyActivity) -> DailyActivity) { rows[date] = block(rows[date] ?: DailyActivity(date)) }
        data.attempts.forEach { attempt -> merge(dateOf(attempt.answeredAt)) { it.copy(questions = it.questions + 1, correct = it.correct + if (attempt.correct) 1 else 0) } }
        data.reviewHistory.forEach { review -> merge(dateOf(review.reviewedAt)) { it.copy(reviews = it.reviews + 1) } }
        val firstSessionIds = data.sessions.filter { it.topicId != 0L }.groupBy { it.topicId }.mapTo(hashSetOf()) { (_, rows) -> rows.minBy { it.completedAt }.id }
        data.sessions.forEach { session ->
            merge(dateOf(session.completedAt)) {
                it.copy(studySessions = it.studySessions + if (session.id in firstSessionIds) 1 else 0, minutes = it.minutes + (session.durationSeconds / 60).toInt())
            }
        }
        data.focusSessions.groupBy { dateOf(it.completedAt) }.forEach { (date, rowsForDay) ->
            val totalMinutes = (rowsForDay.sumOf { it.durationSeconds.coerceAtLeast(0L) } / 60L).toInt()
            val freeMinutes = (rowsForDay.filter { it.origin == "LIVRE" }.sumOf { it.durationSeconds.coerceAtLeast(0L) } / 60L).toInt()
            merge(date) {
                it.copy(focusSessions = it.focusSessions + rowsForDay.size, focusMinutes = it.focusMinutes + totalMinutes, freeFocusMinutes = it.freeFocusMinutes + freeMinutes, minutes = it.minutes + totalMinutes)
            }
        }
        data.executions.forEach { execution -> merge(dateOf(execution.completedAt)) { it.copy(planTasks = it.planTasks + 1, minutes = it.minutes + execution.minutes) } }
        return rows.values.toList()
    }

    fun streak(data: Snapshot): StreakSummary = StreakEngine.summarize(dailyActivity(data), DailyGoal.DEFAULT, todayDate())

    fun minutesLabel(minutes: Int): String = when {
        minutes < 60 -> "$minutes min"
        minutes % 60 == 0 -> "${minutes / 60} h"
        else -> "${minutes / 60} h ${minutes % 60} min"
    }

    fun taskTypeLabel(type: String) = when (type) {
        "THEORY" -> "Teoria"
        "QUESTIONS" -> "Questões"
        "REVIEW" -> "Revisão"
        "ACTIVE_RECALL" -> "Recordação ativa"
        "FLASHCARDS" -> "Flashcards"
        "SIMULATION" -> "Simulado"
        "DISCURSIVE" -> "Discursiva"
        else -> type
    }

    fun taskStatusLabel(status: String) = when (status) {
        "PLANEJADA" -> "Planejada"
        "EM_ANDAMENTO" -> "Em andamento"
        "CONCLUIDA" -> "Concluída"
        "REPROGRAMADA" -> "Reprogramada"
        "NAO_REALIZADA" -> "Não realizada"
        "PAUSADA" -> "Pausada"
        else -> status
    }

    fun topicStatusLabel(status: String) = when (status) {
        "NAO_ESTUDADO" -> "Não estudado"
        "EM_ESTUDO" -> "Em estudo"
        "ESTUDADO" -> "Estudado"
        "REVISANDO" -> "Revisando"
        "DOMINADO" -> "Dominado"
        else -> status
    }

    val weekdayShort = listOf("seg", "ter", "qua", "qui", "sex", "sáb", "dom")
    val weekdayLong = listOf("segunda-feira", "terça-feira", "quarta-feira", "quinta-feira", "sexta-feira", "sábado", "domingo")
    val monthShort = listOf("jan", "fev", "mar", "abr", "mai", "jun", "jul", "ago", "set", "out", "nov", "dez")
    val monthLong = listOf("janeiro", "fevereiro", "março", "abril", "maio", "junho", "julho", "agosto", "setembro", "outubro", "novembro", "dezembro")

    fun dayLabel(date: LocalDate): String = "${date.day} de ${monthLong[date.month.ordinal]}"
    fun shortDate(date: LocalDate): String = "${date.day} ${monthShort[date.month.ordinal]}"
}
