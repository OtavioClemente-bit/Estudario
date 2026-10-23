package br.com.estudario.domain

import br.com.estudario.time.plusDays
import kotlinx.datetime.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class StreakFreezeTest {
    private val start = LocalDate(2026, 10, 1)
    private val goal = DailyGoal(questions = 5)
    private fun studied(vararg offsets: Int) = offsets.map { DailyActivity(start.plusDays(it.toLong()), questions = 10) }

    @Test
    fun sevenDaysEarnAFreezeThatSavesAMissedDay() {
        // 7 dias seguidos (0..6), falta no 7, volta no 8 (hoje).
        val summary = StreakEngine.summarize(studied(0, 1, 2, 3, 4, 5, 6, 8), goal, today = start.plusDays(8))
        assertEquals(8, summary.current)
        assertTrue(start.plusDays(7) in summary.protectedDays)
        assertEquals(0, summary.freezes)
    }

    @Test
    fun withoutAFreezeTheStreakBreaks() {
        val summary = StreakEngine.summarize(studied(0, 1, 2, 4), goal, today = start.plusDays(4))
        assertEquals(1, summary.current)
        assertTrue(summary.protectedDays.isEmpty())
    }

    @Test
    fun todayOpenNeverSpendsAFreeze() {
        val summary = StreakEngine.summarize(studied(0, 1, 2, 3, 4, 5, 6), goal, today = start.plusDays(7))
        assertEquals(7, summary.current)
        assertEquals(1, summary.freezes)
    }
}
