package br.com.estudario.ui.planner

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import br.com.estudario.data.local.planner.PlanTaskEntity
import br.com.estudario.domain.planner.*
import br.com.estudario.ui.theme.EstudarioTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import java.time.LocalDate

class TodayPlanScreenTest {
    @get:Rule val compose = createComposeRule()

    @Test fun todayShowsRequiredTaskDataAndActions() {
        val today = LocalDate.of(2026, 9, 15)
        val task = PlanTaskEntity("task", "plan", 1, subjectId = 10, topicId = 20, subjectNameSnapshot = "Segurança", topicNameSnapshot = "Criptografia", scheduledEpochDay = today.toEpochDay(), type = PlanTaskType.QUESTIONS, plannedMinutes = 60, plannedQuestions = 20, priority = PlanPriority.HIGH, createdRevision = 0, updatedRevision = 0)
        var focado = ""
        val state = StudyPlanUiMapper.map(null, listOf(task), emptyList(), today).copy(activePlan = br.com.estudario.data.local.planner.StudyPlanEntity("plan", 1, "Plano", "Objetivo", today.toEpochDay()))
        compose.setContent {
            EstudarioTheme(false) {
                CalendarScreen(
                    state = state,
                    onOpenTopic = {},
                    onStart = {},
                    onFocus = { focado = it.entity.id },
                    onComplete = {},
                    onReprogram = {},
                    onSkip = {},
                    onToggleLock = { _, _ -> },
                    onToggleDayLock = { _, _ -> },
                    onGenerate = {},
                    onSyncCalendar = {},
                )
            }
        }
        compose.onNodeWithText("SEGURANÇA").assertIsDisplayed()
        compose.onNodeWithText("Criptografia").assertIsDisplayed()
        compose.onNodeWithText("60 min").assertIsDisplayed()
        compose.onNodeWithText("20 qts").assertIsDisplayed()
        compose.onNodeWithText("Começar").performClick()
        assertEquals("task", focado)
    }
}
