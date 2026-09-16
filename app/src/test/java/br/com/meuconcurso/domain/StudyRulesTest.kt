package br.com.meuconcurso.domain

import org.junit.Assert.assertEquals
import org.junit.Test

class StudyRulesTest {
    @Test fun reviewScheduleUsesOneSevenThirtyDays() {
        assertEquals(listOf(1L, 7L, 30L), ReviewIntervals.days)
    }

    @Test fun completedQueueItemMovesAfterCurrentMaximum() {
        assertEquals(8, StudyQueueRules.completedPosition(7))
    }

    @Test fun missedStudyDoesNotAdvanceQueue() {
        assertEquals(3, StudyQueueRules.missedPosition(3))
    }
}
