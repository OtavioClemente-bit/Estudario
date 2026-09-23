package br.com.estudario.domain.planner

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class QuestionTaskDurationCorrectionTest {
    private fun corrected(
        type: PlanTaskType = PlanTaskType.QUESTIONS,
        status: PlanTaskStatus = PlanTaskStatus.PLANEJADA,
        origin: PlanOrigin = PlanOrigin.ENGINE,
        locked: Boolean = false,
        plannedMinutes: Int = 50,
        plannedQuestions: Int = 15,
        minutesPerQuestion: Int = 2,
    ) = QuestionTaskDurationCorrection.correctedMinutes(
        type = type,
        status = status,
        origin = origin,
        locked = locked,
        plannedMinutes = plannedMinutes,
        plannedQuestions = plannedQuestions,
        minutesPerQuestion = minutesPerQuestion,
    )

    @Test
    fun reducesOnlyAnUnstartedUnlockedEngineQuestionTask() {
        assertEquals(30, corrected())
    }

    @Test
    fun leavesStartedLockedManualAndAlreadyAccurateTasksUntouched() {
        assertNull(corrected(status = PlanTaskStatus.EM_ANDAMENTO))
        assertNull(corrected(origin = PlanOrigin.MANUAL))
        assertNull(corrected(locked = true))
        assertNull(corrected(plannedMinutes = 30))
        assertNull(corrected(type = PlanTaskType.THEORY))
    }
}
