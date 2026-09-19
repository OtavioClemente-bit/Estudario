package br.com.estudario.ui.planner

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.*
import br.com.estudario.ui.theme.EstudarioTheme
import org.junit.Rule
import org.junit.Test
import java.time.LocalDate

class PlanHierarchyScreensTest {
    @get:Rule val compose = createComposeRule()
    @Test fun weekExposesEveryDayAndLockControl() {
        val state = ActivePlanUiState(today = LocalDate.of(2026, 9, 15))
        compose.setContent { EstudarioTheme(false) { WeekPlanScreen(state) { _, _ -> } } }
        compose.onAllNodesWithText("Bloquear dia", substring = false).assertCountEquals(7)
    }
}
