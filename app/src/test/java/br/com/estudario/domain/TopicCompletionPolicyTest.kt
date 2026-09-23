package br.com.estudario.domain

import org.junit.Assert.assertEquals
import org.junit.Test

class TopicCompletionPolicyTest {
    @Test
    fun warnsWhenTopicIsNotInActivePlan() {
        assertEquals(
            TopicCompletionWarning.OUTSIDE_ACTIVE_PLAN,
            TopicCompletionPolicy.warning(plannedMinutes = null, actualMinutes = 30),
        )
    }

    @Test
    fun warnsWhenStudyTimeIsShorterButDoesNotBlockTheChoice() {
        assertEquals(
            TopicCompletionWarning.UNDER_PLANNED_TIME,
            TopicCompletionPolicy.warning(plannedMinutes = 60, actualMinutes = 9),
        )
    }

    @Test
    fun doesNotWarnWhenPlannedTimeWasMet() {
        assertEquals(
            TopicCompletionWarning.NONE,
            TopicCompletionPolicy.warning(plannedMinutes = 60, actualMinutes = 60),
        )
    }
}
