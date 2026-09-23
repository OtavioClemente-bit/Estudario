package br.com.estudario.domain.performance

import org.junit.Assert.*
import org.junit.Test
import br.com.estudario.domain.planner.PlanTaskStatus
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

class StudyPerformanceEvaluatorTest {
    private val zone = ZoneId.of("America/Sao_Paulo")
    private val today = LocalDate.of(2026, 9, 22)

    @Test fun `finite window uses local calendar boundaries and previous window`() {
        val start = today.minusDays(6).atStartOfDay(zone).toInstant()
        val end = today.plusDays(1).atStartOfDay(zone).toInstant()
        val input = StudyPerformanceInput(
            attempts = listOf(
                attempt(start.minusNanos(1)),
                attempt(start),
                attempt(end.minusNanos(1)),
                attempt(end),
            ),
            activePlanIds = emptySet(),
        )

        val result = StudyPerformanceEvaluator.evaluate(input, StudyPerformancePeriod.DAYS_7, today, zone)

        assertEquals(2, result.current.attempts)
        assertEquals(start, result.startInclusive)
        assertEquals(end, result.endExclusive)
        assertEquals(start.minusSeconds(7 * 24 * 60 * 60L), result.previousStartInclusive)
        assertEquals(start, result.previousEndExclusive)
    }

    @Test fun `all period has no previous comparison and does not include tomorrow`() {
        val end = today.plusDays(1).atStartOfDay(zone).toInstant()
        val result = StudyPerformanceEvaluator.evaluate(
            StudyPerformanceInput(attempts = listOf(attempt(end), attempt(end.minusNanos(1))), activePlanIds = emptySet()),
            StudyPerformancePeriod.ALL,
            today,
            zone,
        )

        assertNull(result.startInclusive)
        assertNull(result.previous)
        assertEquals(1, result.current.attempts)
    }

    @Test fun `finite windows preserve local day boundaries across daylight saving`() {
        val zoneWithDst = ZoneId.of("America/New_York")
        val dstToday = LocalDate.of(2024, 3, 10)
        val result = StudyPerformanceEvaluator.evaluate(
            StudyPerformanceInput(activePlanIds = emptySet()),
            StudyPerformancePeriod.DAYS_7,
            dstToday,
            zoneWithDst,
        )

        assertEquals(dstToday.minusDays(6).atStartOfDay(zoneWithDst).toInstant(), result.startInclusive)
        assertEquals(dstToday.plusDays(1).atStartOfDay(zoneWithDst).toInstant(), result.endExclusive)
        assertEquals(167 * 60 * 60L, java.time.Duration.between(result.startInclusive, result.endExclusive).seconds)
    }

    @Test fun `sample thresholds and percentages are explicit`() {
        val start = today.minusDays(29).atStartOfDay(zone).toInstant()
        val subjectEvents = (1..10).map { index ->
            PerformanceAttempt(start.plusSeconds(index.toLong()), correct = index >= 8, subjectId = 10, subjectName = "Matemática", topicId = 20, topicName = "Álgebra")
        } + (1..9).map { index ->
            PerformanceAttempt(start.plusSeconds(100 + index.toLong()), correct = false, subjectId = 11, subjectName = "Direito", topicId = 21, topicName = "Princípios")
        }
        val result = StudyPerformanceEvaluator.evaluate(
            StudyPerformanceInput(attempts = subjectEvents, activePlanIds = emptySet()),
            StudyPerformancePeriod.DAYS_30,
            today,
            zone,
        )

        assertEquals(19, result.current.attempts)
        assertEquals(10, result.subjects.first { it.subjectId == 10L }.attempts)
        assertTrue(result.subjects.first { it.subjectId == 10L }.hasSufficientSample)
        assertFalse(result.subjects.first { it.subjectId == 11L }.hasSufficientSample)
        assertEquals(30, result.subjects.first { it.subjectId == 10L }.accuracyPercent)
        assertNull(result.current.taskCompletionPercent)
    }

    @Test fun `recommendations require evidence and include its counts`() {
        val start = today.minusDays(6).atStartOfDay(zone).toInstant()
        val attempts = (1..10).map { index ->
            PerformanceAttempt(start.plusSeconds(index.toLong()), correct = index == 1, subjectId = 10, subjectName = "Matemática", topicId = 20, topicName = "Álgebra")
        }
        val tasks = (1..5).map { index ->
            PerformancePlannedTask("active", today.minusDays((index - 1).toLong()), 50, PlanTaskStatus.PLANEJADA)
        }
        val result = StudyPerformanceEvaluator.evaluate(
            StudyPerformanceInput(attempts = attempts, activePlanIds = setOf("active"), tasks = tasks),
            StudyPerformancePeriod.DAYS_7,
            today,
            zone,
        )

        assertEquals(2, result.recommendations.size)
        assertTrue(result.recommendations.any { it.evidence.contains("10 respostas") && it.evidence.contains("10%") })
        assertTrue(result.recommendations.any { it.evidence.contains("0 de 5") && it.evidence.contains("0%") })
    }

    @Test fun `paused and superseded tasks do not enter adherence while missed and partial do`() {
        val active = listOf(
            task(PlanTaskStatus.PLANEJADA),
            task(PlanTaskStatus.CONCLUIDA),
            task(PlanTaskStatus.NAO_REALIZADA),
            task(PlanTaskStatus.EM_ANDAMENTO),
            task(PlanTaskStatus.PAUSADA),
            task(PlanTaskStatus.REPROGRAMADA),
            task(PlanTaskStatus.PLANEJADA, planId = "archived"),
        )
        val result = StudyPerformanceEvaluator.evaluate(
            StudyPerformanceInput(activePlanIds = setOf("active"), tasks = active),
            StudyPerformancePeriod.DAYS_7,
            today,
            zone,
        )

        assertEquals(4, result.current.plannedTasks)
        assertEquals(1, result.current.completedTasks)
        assertEquals(1, result.current.missedTasks)
        assertEquals(1, result.current.inProgressTasks)
        assertEquals(25, result.current.taskCompletionPercent)
    }

    @Test fun `time stays in independent source subtotals and negative values are ignored`() {
        val now = today.atTime(12, 0).atZone(zone).toInstant()
        val result = StudyPerformanceEvaluator.evaluate(
            StudyPerformanceInput(
                studySessions = listOf(PerformanceSession(now, 3_660), PerformanceSession(now, -60)),
                questionSessions = listOf(PerformanceSession(now, 1_200)),
                executions = listOf(PerformanceExecution("active", now, 17)),
                activePlanIds = setOf("active"),
            ),
            StudyPerformancePeriod.DAYS_7,
            today,
            zone,
        )

        assertEquals(61, result.current.studyMinutes)
        assertEquals(20, result.current.questionMinutes)
        assertEquals(17, result.current.planExecutionMinutes)
    }

    @Test fun `streak is through today and is independent from selected window`() {
        val older = today.minusDays(15).atTime(9, 0).atZone(zone).toInstant()
        val todayEvent = today.atTime(9, 0).atZone(zone).toInstant()
        val result = StudyPerformanceEvaluator.evaluate(
            StudyPerformanceInput(attempts = listOf(attempt(older), attempt(todayEvent)), activePlanIds = emptySet()),
            StudyPerformancePeriod.DAYS_7,
            today,
            zone,
        )

        assertEquals(1, result.current.attempts)
        assertEquals(1, result.streakThroughToday)
    }

    @Test fun `archiving a plan excludes future load but preserves its completed activity`() {
        val completedAt = today.atTime(10, 0).atZone(zone).toInstant()
        val result = StudyPerformanceEvaluator.evaluate(
            StudyPerformanceInput(
                activePlanIds = emptySet(),
                tasks = listOf(task(PlanTaskStatus.PLANEJADA, planId = "archived")),
                executions = listOf(PerformanceExecution("archived", completedAt, 35)),
            ),
            StudyPerformancePeriod.DAYS_7,
            today,
            zone,
        )

        assertEquals(0, result.current.plannedTasks)
        assertEquals(35, result.current.planExecutionMinutes)
        assertEquals(1, result.current.activeDays)
        assertEquals(1, result.streakThroughToday)
    }

    private fun attempt(at: Instant) = PerformanceAttempt(at, correct = true, subjectId = null, subjectName = null, topicId = null, topicName = null)
    private fun task(state: PlanTaskStatus, planId: String = "active") =
        PerformancePlannedTask(planId, today, 30, state)
}
