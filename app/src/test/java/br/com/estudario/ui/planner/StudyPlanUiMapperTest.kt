package br.com.estudario.ui.planner

import br.com.estudario.data.local.planner.PlanTaskEntity
import br.com.estudario.data.local.planner.StudyPlanEntity
import br.com.estudario.data.local.planner.StudyTaskExecutionEntity
import br.com.estudario.domain.planner.*
import org.junit.Assert.*
import org.junit.Test
import java.time.LocalDate

class StudyPlanUiMapperTest {
    @Test fun `empty planner starts in today and asks for a plan`() {
        val state = StudyPlanUiMapper.map(null, emptyList(), emptyList(), LocalDate.of(2026, 9, 15))
        assertEquals(PlanSection.TODAY, state.selectedSection)
        assertNull(state.activePlan)
        assertTrue(state.todayTasks.isEmpty())
    }

    @Test fun `summary keeps planned and actual independent and exposes deficit`() {
        val day = LocalDate.of(2026, 9, 15)
        val plan = StudyPlanEntity("p", 1, "Reta final", "Aprovação", day.toEpochDay(), active = true)
        val task = PlanTaskEntity("t", "p", 1, subjectNameSnapshot = "Segurança", scheduledEpochDay = day.toEpochDay(), type = PlanTaskType.THEORY, plannedMinutes = 60, plannedQuestions = 10, priority = PlanPriority.HIGH, createdRevision = 0, updatedRevision = 0)
        val execution = StudyTaskExecutionEntity("e", "p", "t", 1, startedAt = 0, completedAt = 1, actualMinutes = 25, questionsDone = 5, correctAnswers = 4)
        val state = StudyPlanUiMapper.map(plan, listOf(task), listOf(execution), day, deficitMinutes = 35)
        assertEquals(60, state.todayPlannedMinutes)
        assertEquals(25, state.todayActualMinutes)
        assertEquals(35, state.deficitMinutes)
    }

    @Test fun `reprogrammed and paused history does not inflate day week or plan totals`() {
        val today = LocalDate.of(2026, 9, 22)
        val plan = StudyPlanEntity("p", 1, "Reta final", "Aprovação", today.toEpochDay(), active = true)
        fun task(id: String, date: LocalDate, minutes: Int, status: PlanTaskStatus) = PlanTaskEntity(
            id = id,
            planId = "p",
            competitionId = 1,
            subjectNameSnapshot = "Direito",
            scheduledEpochDay = date.toEpochDay(),
            type = PlanTaskType.THEORY,
            plannedMinutes = minutes,
            priority = PlanPriority.HIGH,
            status = status,
            createdRevision = 0,
            updatedRevision = 0,
        )
        val tasks = listOf(
            task("today", today, 80, PlanTaskStatus.PLANEJADA),
            task("old-reprogrammed", today, 280, PlanTaskStatus.REPROGRAMADA),
            task("paused", today, 45, PlanTaskStatus.PAUSADA),
            task("missed", today, 30, PlanTaskStatus.NAO_REALIZADA),
            task("completed", today, 20, PlanTaskStatus.CONCLUIDA),
            task("tomorrow", today.plusDays(1), 60, PlanTaskStatus.EM_ANDAMENTO),
            task("paused-next-week", today.plusDays(7), 500, PlanTaskStatus.PAUSADA),
        )

        val state = StudyPlanUiMapper.map(plan, tasks, emptyList(), today)

        assertEquals(130, state.todayPlannedMinutes)
        assertEquals(190, state.weekPlannedMinutes)
        assertEquals(190, state.totalPlannedMinutes)
    }
}
