package br.com.meuconcurso.domain

import br.com.meuconcurso.data.local.ReviewScheduleEntity
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate

class StudyAlgorithmsTest {
    @Test fun smartScorePrioritizesRecurringErrorsAndOverdueReviews() {
        val regular = SmartQuestionCandidate(1, 1, 60, 0, false, false, 5, 1)
        val priority = SmartQuestionCandidate(2, 1, 60, 2, true, true, 5, 1)
        assertTrue(SmartQuestionSelector.score(priority) > SmartQuestionSelector.score(regular))
    }

    @Test fun reviewStatusIsDerivedWithoutMutatingDates() {
        val now = 1_000_000_000L
        assertEquals(ComputedReviewStatus.ATRASADA, ReviewPolicy.status(ReviewScheduleEntity(topicId = 1, stage = 1, dueAt = now - 2 * 86_400_000L), now))
        assertEquals(ComputedReviewStatus.DISPONIVEL, ReviewPolicy.status(ReviewScheduleEntity(topicId = 1, stage = 1, dueAt = now), now))
    }

    @Test fun streakAllowsTodayOrYesterdayAsCurrentRun() {
        val today = LocalDate.of(2026, 9, 11)
        val result = StreakCalculator.calculate(setOf(today.minusDays(1), today.minusDays(2), today.minusDays(4)), today)
        assertEquals(2, result.current)
        assertEquals(2, result.best)
    }
}
