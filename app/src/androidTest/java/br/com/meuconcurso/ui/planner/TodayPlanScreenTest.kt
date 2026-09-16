package br.com.meuconcurso.ui.planner

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import br.com.meuconcurso.data.local.planner.PlanTaskEntity
import br.com.meuconcurso.domain.planner.*
import br.com.meuconcurso.ui.theme.MeuConcursoTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import java.time.LocalDate

class TodayPlanScreenTest {
    @get:Rule val compose = createComposeRule()

    @Test fun todayShowsRequiredTaskDataAndActions() {
        val today = LocalDate.of(2026, 9, 15)
        val task = PlanTaskEntity("task", "plan", 1, subjectId = 10, topicId = 20, subjectNameSnapshot = "Segurança", topicNameSnapshot = "Criptografia", scheduledEpochDay = today.toEpochDay(), type = PlanTaskType.QUESTIONS, plannedMinutes = 60, plannedQuestions = 20, priority = PlanPriority.HIGH, createdRevision = 0, updatedRevision = 0)
        var started = ""
        val state = StudyPlanUiMapper.map(null, listOf(task), emptyList(), today).copy(activePlan = br.com.meuconcurso.data.local.planner.StudyPlanEntity("plan", 1, "Plano", "Objetivo", today.toEpochDay()))
        compose.setContent { MeuConcursoTheme(false) { TodayPlanScreen(state, {}, { started = it }, {}, {}, {}, { _, _ -> }, {}) } }
        compose.onNodeWithText("Segurança").assertIsDisplayed()
        compose.onNodeWithText("Criptografia").assertIsDisplayed()
        compose.onNodeWithText("1h00 • Questões • 20 questões").assertIsDisplayed()
        compose.onNodeWithText("Iniciar").performClick()
        assertEquals("task", started)
    }
}
