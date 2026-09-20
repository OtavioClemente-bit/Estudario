package br.com.estudario.ui.planner

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import br.com.estudario.data.local.planner.StudyPlanEntity
import br.com.estudario.ui.theme.EstudarioTheme
import org.junit.Rule
import org.junit.Test
import java.time.LocalDate

class PlanProgressHeaderTest {
    @get:Rule
    val compose = createComposeRule()

    @Test
    fun progressHeaderExplainsThePlanInPortuguese() {
        compose.setContent {
            EstudarioTheme(false) {
                PlanProgressHeader(sampleActivePlanState())
            }
        }

        compose.onNodeWithText("Progresso do plano").assertIsDisplayed()
        compose.onNodeWithText("Hoje").assertIsDisplayed()
        compose.onNodeWithText("Previsão de conclusão").assertIsDisplayed()
        compose.onNodeWithText(forecastDateLabelPtBr(LocalDate.now().plusDays(45))).assertIsDisplayed()
    }

    private fun sampleActivePlanState() = ActivePlanUiState(
        activePlan = StudyPlanEntity(
            id = "plan",
            competitionId = 1L,
            name = "Plano concurso",
            objective = "Aprovação",
            startEpochDay = LocalDate.now().toEpochDay(),
            examEpochDay = LocalDate.now().plusMonths(3).toEpochDay(),
            active = true,
        ),
        today = LocalDate.now(),
        forecastDate = LocalDate.now().plusDays(45),
    )
}
