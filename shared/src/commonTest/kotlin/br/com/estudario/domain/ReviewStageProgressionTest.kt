package br.com.estudario.domain

import br.com.estudario.data.local.ReviewDifficulty
import br.com.estudario.time.plusDays
import br.com.estudario.time.startOfDay
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class ReviewStageProgressionTest {
    private val zone = TimeZone.of("America/Sao_Paulo")
    private val start = LocalDate(2026, 10, 26)
    private fun at(day: Long) = start.plusDays(day).startOfDay(zone).toEpochMilliseconds()
    private fun schedule() = listOf(ReviewStageState(1, 1, at(0)), ReviewStageState(2, 2, at(6)), ReviewStageState(3, 3, at(29)))

    @Test fun repeatedDifficultDaysKeepTheSameStageAndOnlyNormalAdvances() {
        var stages = schedule()
        for (day in 0L..3L) {
            stages = assertNotNull(ReviewStageProgression.record(stages, 1, ReviewDifficulty.DIFICIL, at(day), zone)).stages
            assertNull(stages.first().completedAt)
            assertEquals(at(day + 1), stages.first().dueAt)
            assertEquals(at(day + 7), stages[1].dueAt)
        }
        val passed = assertNotNull(ReviewStageProgression.record(stages, 1, ReviewDifficulty.NORMAL, at(4), zone))
        assertEquals(at(4), passed.stages.first().completedAt)
        assertNull(passed.stages[1].completedAt)
        assertEquals(at(10), passed.stages[1].dueAt)
        assertEquals(at(33), passed.stages[2].dueAt)
    }

    @Test fun difficultLastStageDoesNotInflateItsNextInterval() {
        var stages = listOf(
            ReviewStageState(1, 1, at(-29), completedAt = at(-29)),
            ReviewStageState(2, 2, at(-23), completedAt = at(-23)),
            ReviewStageState(3, 3, at(0)),
        )
        for (day in 0L..9L) {
            val repeated = assertNotNull(ReviewStageProgression.record(stages, 3, ReviewDifficulty.DIFICIL, at(day), zone))
            assertNull(repeated.newStage)
            stages = repeated.stages
        }
        val passed = assertNotNull(ReviewStageProgression.record(stages, 3, ReviewDifficulty.NORMAL, at(10), zone))
        assertEquals(4, passed.newStage?.stage)
        assertEquals(60L, passed.newStage?.intervalDays)
        assertEquals(at(70), passed.newStage?.dueAt)
    }

    @Test fun easyAdvancesOnlyOneStageAndPreservesOrder() {
        val passed = assertNotNull(ReviewStageProgression.record(schedule(), 1, ReviewDifficulty.FACIL, at(0), zone))
        assertNotNull(passed.stages.first().completedAt)
        assertNull(passed.stages[1].completedAt)
        assertEquals(at(8), passed.stages[1].dueAt)
        assertEquals(at(31), passed.stages[2].dueAt)
    }

    @Test fun overdueNormalPreservesSpacingInsteadOfLeavingFutureStagesOverdue() {
        val passed = assertNotNull(ReviewStageProgression.record(schedule(), 1, ReviewDifficulty.NORMAL, at(40), zone))
        assertEquals(at(46), passed.stages[1].dueAt)
        assertEquals(at(69), passed.stages[2].dueAt)
    }

    @Test fun cannotRepeatTomorrowTodayOrSkipAPendingStage() {
        val repeated = assertNotNull(ReviewStageProgression.record(schedule(), 1, ReviewDifficulty.DIFICIL, at(0), zone))
        assertNull(ReviewStageProgression.record(repeated.stages, 1, ReviewDifficulty.NORMAL, at(0), zone))
        assertNull(ReviewStageProgression.record(repeated.stages, 2, ReviewDifficulty.NORMAL, at(50), zone))
    }
}
