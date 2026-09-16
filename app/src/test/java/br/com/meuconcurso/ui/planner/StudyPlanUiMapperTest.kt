package br.com.meuconcurso.ui.planner

import br.com.meuconcurso.data.local.planner.PlanTaskEntity
import br.com.meuconcurso.data.local.planner.StudyPlanEntity
import br.com.meuconcurso.data.local.planner.StudyTaskExecutionEntity
import br.com.meuconcurso.domain.planner.*
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
}
