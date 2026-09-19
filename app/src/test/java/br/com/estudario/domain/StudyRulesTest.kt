package br.com.estudario.domain

import br.com.estudario.data.local.ReviewDifficulty
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class StudyRulesTest {
    @Test fun reviewScheduleUsesOneSevenThirtyDays() {
        assertEquals(listOf(1L, 7L, 30L), ReviewIntervals.days)
    }

    @Test fun reviewNeverEndsAfterTheLastFixedStage() {
        assertEquals(60L, ReviewIntervals.nextIntervalDays(30L))
        assertEquals(120L, ReviewIntervals.nextIntervalDays(60L))
        assertEquals(240L, ReviewIntervals.nextIntervalDays(120L))
    }

    @Test fun perpetualIntervalStopsGrowingAtTheCeiling() {
        assertEquals(ReviewIntervals.MAX_DAYS, ReviewIntervals.nextIntervalDays(240L))
        assertEquals(ReviewIntervals.MAX_DAYS, ReviewIntervals.nextIntervalDays(900L))
    }

    @Test fun intensiveCycleDoesNotSkipStraightToTheCeiling() {
        // No ciclo intensivo o último salto é de 16 dias; ainda assim a volta seguinte é 60, não 240.
        assertEquals(60L, ReviewIntervals.nextIntervalDays(16L))
    }

    @Test fun hardReviewComesBackSoonerAndEasyLater() {
        val normal = ReviewIntervals.adjustedIntervalDays(30L, ReviewDifficulty.NORMAL)
        val dificil = ReviewIntervals.adjustedIntervalDays(30L, ReviewDifficulty.DIFICIL)
        val facil = ReviewIntervals.adjustedIntervalDays(30L, ReviewDifficulty.FACIL)
        assertEquals(60L, normal)
        assertTrue(dificil < normal)
        assertTrue(facil > normal)
        assertTrue(dificil >= 1L)
    }

    @Test fun missingMostQuestionsPullsTheReviewCloser() {
        val bom = ReviewIntervals.adjustedIntervalDays(30L, ReviewDifficulty.NORMAL, correct = 3, total = 3)
        val ruim = ReviewIntervals.adjustedIntervalDays(30L, ReviewDifficulty.NORMAL, correct = 1, total = 3)
        assertTrue(ruim < bom)
    }

    @Test fun wrongQuestionComesBackInThreeDays() {
        assertEquals(0, ErrorRetryLadder.nextStreak(2, correct = false))
        assertEquals(3L, ErrorRetryLadder.intervalDays(0))
    }

    @Test fun eachCorrectAnswerClimbsOneRungOfTheLadder() {
        val primeiro = ErrorRetryLadder.nextStreak(0, correct = true)
        val segundo = ErrorRetryLadder.nextStreak(primeiro, correct = true)
        val terceiro = ErrorRetryLadder.nextStreak(segundo, correct = true)
        assertEquals(10L, ErrorRetryLadder.intervalDays(primeiro))
        assertEquals(30L, ErrorRetryLadder.intervalDays(segundo))
        assertNull(ErrorRetryLadder.intervalDays(terceiro))
    }

    @Test fun masteredQuestionLeavesTheLadderUntilItIsMissedAgain() {
        val dominada = 3
        assertNull(ErrorRetryLadder.nextRetryAt(dominada, now = 0L))
        val depoisDeErrar = ErrorRetryLadder.nextStreak(dominada, correct = false)
        assertEquals(3L * 86_400_000L, ErrorRetryLadder.nextRetryAt(depoisDeErrar, now = 0L))
    }

    @Test fun completedQueueItemMovesAfterCurrentMaximum() {
        assertEquals(8, StudyQueueRules.completedPosition(7))
    }

    @Test fun missedStudyDoesNotAdvanceQueue() {
        assertEquals(3, StudyQueueRules.missedPosition(3))
    }
}
