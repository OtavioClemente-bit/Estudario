package br.com.estudario.domain.performance

import br.com.estudario.domain.planner.PlanTaskStatus
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

enum class StudyPerformancePeriod(val days: Int?, val label: String) {
    DAYS_7(7, "7 dias"),
    DAYS_30(30, "30 dias"),
    DAYS_90(90, "90 dias"),
    ALL(null, "Tudo"),
}

data class PerformanceAttempt(
    val answeredAt: Instant,
    val correct: Boolean,
    val subjectId: Long?,
    val subjectName: String?,
    val topicId: Long?,
    val topicName: String?,
)

data class PerformanceSession(val completedAt: Instant, val durationSeconds: Long)
data class PerformanceReview(val reviewedAt: Instant)
data class PerformancePlannedTask(
    val planId: String,
    val scheduledDate: LocalDate,
    val plannedMinutes: Int,
    val status: PlanTaskStatus,
)
data class PerformanceExecution(val planId: String, val completedAt: Instant, val actualMinutes: Int)

data class StudyPerformanceInput(
    val attempts: List<PerformanceAttempt> = emptyList(),
    val reviews: List<PerformanceReview> = emptyList(),
    val studySessions: List<PerformanceSession> = emptyList(),
    val questionSessions: List<PerformanceSession> = emptyList(),
    val activePlanIds: Set<String> = emptySet(),
    val tasks: List<PerformancePlannedTask> = emptyList(),
    val executions: List<PerformanceExecution> = emptyList(),
)

data class WindowMetrics(
    val activeDays: Int = 0,
    val studySessions: Int = 0,
    val questionSessions: Int = 0,
    val attempts: Int = 0,
    val correctAttempts: Int = 0,
    val reviews: Int = 0,
    val plannedTasks: Int = 0,
    val completedTasks: Int = 0,
    val missedTasks: Int = 0,
    val inProgressTasks: Int = 0,
    val accuracyPercent: Int? = null,
    val taskCompletionPercent: Int? = null,
    val plannedMinutes: Long = 0,
    val studyMinutes: Long = 0,
    val questionMinutes: Long = 0,
    val planExecutionMinutes: Long = 0,
)

data class TopicPerformance(
    val topicId: Long,
    val name: String,
    val attempts: Int,
    val correct: Int,
    val accuracyPercent: Int,
    val hasSufficientSample: Boolean,
)

data class SubjectPerformance(
    val subjectId: Long,
    val name: String,
    val attempts: Int,
    val correct: Int,
    val accuracyPercent: Int,
    val hasSufficientSample: Boolean,
    val topics: List<TopicPerformance>,
)

data class DailyActivityPoint(val date: LocalDate, val eventCount: Int)
data class PerformanceRecommendation(val message: String, val evidence: String)

data class StudyPerformanceResult(
    val period: StudyPerformancePeriod,
    val current: WindowMetrics,
    val previous: WindowMetrics?,
    val startInclusive: Instant?,
    val endExclusive: Instant,
    val previousStartInclusive: Instant?,
    val previousEndExclusive: Instant?,
    val dailyActivity: List<DailyActivityPoint>,
    val dailyActivityLabel: String,
    val subjects: List<SubjectPerformance>,
    val recommendations: List<PerformanceRecommendation>,
    val streakThroughToday: Int,
)

/** Deterministic evaluation over recorded events. It deliberately does not infer a global score or retention. */
object StudyPerformanceEvaluator {
    private data class Window(val start: Instant?, val end: Instant, val startDate: LocalDate?) {
        fun includes(at: Instant): Boolean = (start == null || at >= start) && at < end
        fun includes(date: LocalDate, zone: ZoneId): Boolean =
            (startDate == null || date >= startDate) && date < end.atZone(zone).toLocalDate()
    }

    fun evaluate(
        input: StudyPerformanceInput,
        period: StudyPerformancePeriod,
        today: LocalDate,
        zone: ZoneId,
    ): StudyPerformanceResult {
        val end = today.plusDays(1).atStartOfDay(zone).toInstant()
        val startDate = period.days?.let { today.minusDays(it - 1L) }
        val currentWindow = Window(startDate?.atStartOfDay(zone)?.toInstant(), end, startDate)
        val previousStartDate = startDate?.minusDays(period.days!!.toLong())
        val previousWindow = if (startDate != null) {
            Window(previousStartDate!!.atStartOfDay(zone).toInstant(), startDate.atStartOfDay(zone).toInstant(), previousStartDate)
        } else null

        val current = measure(input, currentWindow, zone)
        val previous = previousWindow?.let { measure(input, it, zone) }
        val subjectMetrics = subjects(input.attempts.filter { currentWindow.includes(it.answeredAt) })
        val recommendations = buildList {
            subjectMetrics.filter { it.hasSufficientSample && it.accuracyPercent < 70 }.forEach {
                add(
                    PerformanceRecommendation(
                        message = "Reforce ${it.name}",
                        evidence = "${it.accuracyPercent}% de acerto em ${it.attempts} respostas • ${period.label}",
                    ),
                )
            }
            if (current.plannedTasks >= 5 && (current.taskCompletionPercent ?: 100) < 70) {
                add(
                    PerformanceRecommendation(
                        message = "Revise a carga do plano",
                        evidence = "${current.completedTasks} de ${current.plannedTasks} tarefas (${current.taskCompletionPercent}%) • ${period.label}",
                    ),
                )
            }
        }

        val activityStart = startDate ?: today.minusDays(29)
        val activityEndDate = today
        val eventCounts = linkedMapOf<LocalDate, Int>()
        fun record(at: Instant) {
            if (currentWindow.includes(at)) {
                val date = at.atZone(zone).toLocalDate()
                if (!date.isBefore(activityStart) && !date.isAfter(activityEndDate)) {
                    eventCounts[date] = (eventCounts[date] ?: 0) + 1
                }
            }
        }
        input.attempts.forEach { record(it.answeredAt) }
        input.reviews.forEach { record(it.reviewedAt) }
        input.studySessions.forEach { record(it.completedAt) }
        input.questionSessions.forEach { record(it.completedAt) }
        input.executions.forEach { record(it.completedAt) }
        val dailyActivity = generateSequence(activityStart) { date -> date.plusDays(1).takeIf { it <= activityEndDate } }
            .map { DailyActivityPoint(it, eventCounts[it] ?: 0) }
            .toList()

        return StudyPerformanceResult(
            period = period,
            current = current,
            previous = previous,
            startInclusive = currentWindow.start,
            endExclusive = end,
            previousStartInclusive = previousWindow?.start,
            previousEndExclusive = previousWindow?.end,
            dailyActivity = dailyActivity,
            dailyActivityLabel = if (period == StudyPerformancePeriod.ALL) "Últimos 30 dias" else "No período selecionado",
            subjects = subjectMetrics,
            recommendations = recommendations,
            streakThroughToday = streak(input, today, zone),
        )
    }

    private fun measure(input: StudyPerformanceInput, window: Window, zone: ZoneId): WindowMetrics {
        val attempts = input.attempts.filter { window.includes(it.answeredAt) }
        val tasks = input.tasks.filter {
            it.planId in input.activePlanIds && window.includes(it.scheduledDate, zone) &&
                it.status != PlanTaskStatus.PAUSADA && it.status != PlanTaskStatus.REPROGRAMADA
        }
        // Execução é um evento histórico já realizado. Arquivar o plano não apaga estudo concluído.
        val executions = input.executions.filter { window.includes(it.completedAt) }
        val studySessions = input.studySessions.filter { window.includes(it.completedAt) }
        val questionSessions = input.questionSessions.filter { window.includes(it.completedAt) }
        val reviews = input.reviews.count { window.includes(it.reviewedAt) }
        val activityDates = buildSet {
            attempts.forEach { add(it.answeredAt.atZone(zone).toLocalDate()) }
            input.reviews.filter { window.includes(it.reviewedAt) }.forEach { add(it.reviewedAt.atZone(zone).toLocalDate()) }
            studySessions.forEach { add(it.completedAt.atZone(zone).toLocalDate()) }
            questionSessions.forEach { add(it.completedAt.atZone(zone).toLocalDate()) }
            executions.forEach { add(it.completedAt.atZone(zone).toLocalDate()) }
        }
        val correct = attempts.count { it.correct }
        val completed = tasks.count { it.status == PlanTaskStatus.CONCLUIDA }
        return WindowMetrics(
            activeDays = activityDates.size,
            studySessions = studySessions.size,
            questionSessions = questionSessions.size,
            attempts = attempts.size,
            correctAttempts = correct,
            reviews = reviews,
            plannedTasks = tasks.size,
            completedTasks = completed,
            missedTasks = tasks.count { it.status == PlanTaskStatus.NAO_REALIZADA },
            inProgressTasks = tasks.count { it.status == PlanTaskStatus.EM_ANDAMENTO },
            accuracyPercent = attempts.size.takeIf { it > 0 }?.let { correct * 100 / it },
            taskCompletionPercent = tasks.size.takeIf { it > 0 }?.let { completed * 100 / it },
            plannedMinutes = tasks.fold(0L) { total, task -> saturatingAdd(total, task.plannedMinutes.coerceAtLeast(0).toLong()) },
            studyMinutes = sessionMinutes(studySessions),
            questionMinutes = sessionMinutes(questionSessions),
            planExecutionMinutes = executions.fold(0L) { total, execution ->
                saturatingAdd(total, execution.actualMinutes.coerceAtLeast(0).toLong())
            },
        )
    }

    private fun sessionMinutes(sessions: List<PerformanceSession>): Long {
        val seconds = sessions.fold(0L) { total, session -> saturatingAdd(total, session.durationSeconds.coerceAtLeast(0)) }
        return seconds / 60
    }

    private fun subjects(attempts: List<PerformanceAttempt>): List<SubjectPerformance> = attempts
        .filter { it.subjectId != null }
        .groupBy { it.subjectId!! }
        .map { (subjectId, events) ->
            val topicMetrics = events.filter { it.topicId != null }.groupBy { it.topicId!! }.map { (topicId, topicEvents) ->
                val correct = topicEvents.count { it.correct }
                TopicPerformance(
                    topicId = topicId,
                    name = topicEvents.firstNotNullOfOrNull { it.topicName } ?: "Tópico",
                    attempts = topicEvents.size,
                    correct = correct,
                    accuracyPercent = correct * 100 / topicEvents.size,
                    hasSufficientSample = topicEvents.size >= 10,
                )
            }.sortedWith(compareBy<TopicPerformance> { it.accuracyPercent }.thenBy { it.name })
            val correct = events.count { it.correct }
            SubjectPerformance(
                subjectId = subjectId,
                name = events.firstNotNullOfOrNull { it.subjectName } ?: "Matéria",
                attempts = events.size,
                correct = correct,
                accuracyPercent = correct * 100 / events.size,
                hasSufficientSample = events.size >= 10,
                topics = topicMetrics,
            )
        }
        .sortedWith(compareBy<SubjectPerformance> { it.accuracyPercent }.thenBy { it.name })

    private fun streak(input: StudyPerformanceInput, today: LocalDate, zone: ZoneId): Int {
        val dates = buildSet {
            input.attempts.forEach { add(it.answeredAt.atZone(zone).toLocalDate()) }
            input.reviews.forEach { add(it.reviewedAt.atZone(zone).toLocalDate()) }
            input.studySessions.forEach { add(it.completedAt.atZone(zone).toLocalDate()) }
            input.questionSessions.forEach { add(it.completedAt.atZone(zone).toLocalDate()) }
            input.executions.forEach { add(it.completedAt.atZone(zone).toLocalDate()) }
        }.filter { it <= today }.toSet()
        var cursor = when {
            today in dates -> today
            today.minusDays(1) in dates -> today.minusDays(1)
            else -> return 0
        }
        var length = 0
        while (cursor in dates) {
            length++
            cursor = cursor.minusDays(1)
        }
        return length
    }

    private fun saturatingAdd(left: Long, right: Long): Long =
        if (Long.MAX_VALUE - left < right) Long.MAX_VALUE else left + right
}
