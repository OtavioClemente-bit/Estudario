package br.com.estudario.domain.performance

import br.com.estudario.domain.DailyActivityBuilder
import br.com.estudario.domain.StudyTime
import br.com.estudario.domain.planner.PlanTaskStatus
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlin.time.Instant
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.minus

/** As fórmulas da tela Desempenho que o app e o site usam juntos. */
class SharedPerformanceTest {
    private val zone = TimeZone.of("America/Sao_Paulo")
    private val today = LocalDate(2026, 11, 18)
    private fun day(back: Int) = today.minus(back, DateTimeUnit.DAY)
    private fun at(back: Int, hour: Int = 12): Long = day(back).atStartOfDayIn(zone).toEpochMilliseconds() + hour * 3_600_000L
    private fun inst(ms: Long) = Instant.fromEpochMilliseconds(ms)
    private fun answer(back: Int, correct: Boolean, subject: Long = 1, topic: Long = 10) =
        PerformanceAttempt(inst(at(back)), correct, subject, "Matéria $subject", topic, "Tópico $topic")

    @Test
    fun hoursCountFocusAndQuestionRunsOnceAndActiveDaysCountAnyEvent() {
        val focus = StudyTime.focus(at(0, 11), 3_600)!! // 10h-11h hoje
        val run = StudyTime.questions(at(0, 10) + 30 * 60_000L, at(0, 11), 5)!! // dentro do foco
        val input = StudyPerformanceInput(
            attempts = listOf(answer(0, true)),
            studySessions = listOf(PerformanceSession(inst(at(3)), 600)), // só sessão de tópico, sem foco
            questionSessions = listOf(PerformanceSession(inst(at(0, 11)), 1_200)),
            studySpans = listOf(focus, run),
        )
        val r = StudyPerformanceEvaluator.evaluate(input, StudyPerformancePeriod.DAYS_7, today, zone)
        assertEquals(60, r.current.studyMinutes)
        assertEquals(2, r.current.activeDays)
        assertEquals(7, r.rhythm.days.size)
        assertEquals(2, r.rhythm.activeDays)
        assertEquals(60, r.rhythm.averageMinutes)
        assertEquals(today, r.rhythm.bestDay?.date)
        assertEquals(60, r.rhythm.totalMinutes)
    }

    @Test
    fun rhythmShowsAtMostThirtyDaysAndLabelSaysSo() {
        val r90 = StudyPerformanceEvaluator.evaluate(StudyPerformanceInput(), StudyPerformancePeriod.DAYS_90, today, zone)
        val all = StudyPerformanceEvaluator.evaluate(StudyPerformanceInput(), StudyPerformancePeriod.ALL, today, zone)
        assertEquals(30, r90.rhythm.days.size)
        assertEquals(30, all.rhythm.days.size)
        assertEquals("Últimos 30 dias", all.dailyActivityLabel)
        assertEquals(today, all.rhythm.days.last().date)
    }

    @Test
    fun accuracyDeltaNeedsFiveAnswersInPreviousWindow() {
        val now = List(4) { answer(1, true) }
        val fewBefore = List(4) { answer(10, false) }
        assertNull(StudyPerformanceEvaluator.evaluate(StudyPerformanceInput(attempts = now + fewBefore), StudyPerformancePeriod.DAYS_7, today, zone).accuracyDelta)
        val enoughBefore = List(5) { answer(10, false) }
        assertEquals(100, StudyPerformanceEvaluator.evaluate(StudyPerformanceInput(attempts = now + enoughBefore), StudyPerformancePeriod.DAYS_7, today, zone).accuracyDelta)
    }

    @Test
    fun subjectsFollowEditalFilterAndFiveAnswerSample() {
        val attempts = List(5) { answer(0, it < 2, subject = 1) } + List(4) { answer(0, false, subject = 2, topic = 20) }
        val r = StudyPerformanceEvaluator.evaluate(StudyPerformanceInput(attempts = attempts), StudyPerformancePeriod.DAYS_30, today, zone, subjectIds = setOf(1L, 2L))
        assertTrue(r.subjects.first { it.subjectId == 1L }.hasSufficientSample)
        assertTrue(!r.subjects.first { it.subjectId == 2L }.hasSufficientSample)
        assertEquals(40, r.subjects.first { it.subjectId == 1L }.accuracyPercent)
        val only1 = StudyPerformanceEvaluator.evaluate(StudyPerformanceInput(attempts = attempts), StudyPerformancePeriod.DAYS_30, today, zone, subjectIds = setOf(1L))
        assertEquals(listOf(1L), only1.subjects.map { it.subjectId })
        // KPIs continuam com todas as respostas
        assertEquals(9, only1.current.attempts)
    }

    @Test
    fun pastUnfinishedTasksCountAsMissed() {
        val tasks = listOf(
            PerformancePlannedTask("p", day(2), 30, PlanTaskStatus.PLANEJADA),
            PerformancePlannedTask("p", day(1), 30, PlanTaskStatus.EM_ANDAMENTO),
            PerformancePlannedTask("p", today, 30, PlanTaskStatus.EM_ANDAMENTO),
            PerformancePlannedTask("p", today, 30, PlanTaskStatus.CONCLUIDA),
            PerformancePlannedTask("p", day(1), 30, PlanTaskStatus.NAO_REALIZADA),
            PerformancePlannedTask("p", day(1), 30, PlanTaskStatus.REPROGRAMADA),
        )
        val m = StudyPerformanceEvaluator.evaluate(StudyPerformanceInput(activePlanIds = setOf("p"), tasks = tasks), StudyPerformancePeriod.DAYS_7, today, zone).current
        assertEquals(5, m.plannedTasks)
        assertEquals(3, m.missedTasks)
        assertEquals(1, m.inProgressTasks)
        assertEquals(20, m.taskCompletionPercent)
    }

    @Test
    fun editalCoverageCountsOnlyLeaves() {
        val c = EditalCoverage.of(
            listOf(
                CoverageTopic(1, null, false), // pai
                CoverageTopic(2, 1, true),
                CoverageTopic(3, 1, false),
                CoverageTopic(4, null, false),
            ),
            theoryTopicIds = setOf(3L, 1L),
        )
        assertEquals(3, c.total)
        assertEquals(1, c.studied)
        assertEquals(1, c.withMaterial)
        assertEquals(1, c.remaining)
        assertEquals(33, c.studiedPercent)
    }

    @Test
    fun dailyActivityCountsTopicOnceAndSplitsTime() {
        val days = DailyActivityBuilder.build(
            answers = listOf(DailyActivityBuilder.Answer(at(0), true), DailyActivityBuilder.Answer(at(0), false)),
            reviewedAt = listOf(at(1)),
            sessions = listOf(DailyActivityBuilder.Session(1, 7, at(2)), DailyActivityBuilder.Session(2, 7, at(0))),
            focusSessions = listOf(DailyActivityBuilder.Focus(at(0, 11), 1_800, free = true)),
            executionsCompletedAt = emptyList(),
            questionRuns = emptyList(),
            zone = zone,
        ).associateBy { it.date }
        assertEquals(2, days[today]!!.questions)
        assertEquals(1, days[today]!!.correct)
        assertEquals(0, days[today]!!.studySessions)
        assertEquals(1, days[day(2)]!!.studySessions)
        assertEquals(30, days[today]!!.minutes)
        assertEquals(30, days[today]!!.freeFocusMinutes)
        assertEquals(1, days[day(1)]!!.reviews)
    }
}
