package br.com.estudario.ui.planner

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.*
import br.com.estudario.ui.theme.EstudarioTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import java.time.LocalDate

class PlanHierarchyScreensTest {
    @get:Rule val compose = createComposeRule()
    @Test fun calendarExposesSelectedDayAndMonthToggle() {
        val state = ActivePlanUiState(today = LocalDate.of(2026, 9, 15))
        compose.setContent {
            EstudarioTheme(false) {
                CalendarScreen(
                    state = state,
                    onOpenTopic = {},
                    onStart = {},
                    onFocus = {},
                    onComplete = {},
                    onReprogram = {},
                    onSkip = {},
                    onToggleLock = { _, _ -> },
                    onToggleDayLock = { _, _ -> },
                    onGenerate = {},
                )
            }
        }
        compose.onNodeWithText("Hoje").assertExists()
        compose.onNodeWithText("Ver mês").assertExists()
    }

    @Test fun emptyDayKeepsPortugueseActionsWithoutAgendaActionOnPlan() {
        var generateClicks = 0
        compose.setContent {
            EstudarioTheme(false) {
                CalendarScreen(
                    state = ActivePlanUiState(today = LocalDate.of(2026, 9, 15)),
                    onOpenTopic = {},
                    onStart = {},
                    onFocus = {},
                    onComplete = {},
                    onReprogram = {},
                    onSkip = {},
                    onToggleLock = { _, _ -> },
                    onToggleDayLock = { _, _ -> },
                    onGenerate = { generateClicks++ },
                )
            }
        }

        compose.onNodeWithText("Gerar planejamento").performClick()
        assertEquals(1, generateClicks)
        compose.onNodeWithText("Sincronizar com a agenda").assertDoesNotExist()
    }
}
