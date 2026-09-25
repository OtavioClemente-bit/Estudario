package br.com.estudario.ui.components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import br.com.estudario.data.local.planner.PlanTaskEntity
import br.com.estudario.domain.planner.PlanPriority
import br.com.estudario.domain.planner.PlanTaskType
import br.com.estudario.ui.planner.PlannerTaskUi
import br.com.estudario.ui.theme.EstudarioTheme
import org.junit.Rule
import org.junit.Test

class StudyFocusCardTest {
    @get:Rule
    val compose = createComposeRule()

    @Test
    fun focusCardShowsThePrimaryActionAndTaskDetails() {
        compose.setContent {
            EstudarioTheme(false) {
                StudyFocusCard(sampleTaskUi(), onStart = {}, onOpenPlan = {})
            }
        }
        compose.onNodeWithText("ESTUDAR AGORA").assertIsDisplayed()
        compose.onNodeWithText("Direito Constitucional").assertIsDisplayed()
        compose.onNodeWithText("Começar").assertIsDisplayed()
    }

    @Test
    fun focusCardExplainsWhenThereIsNoMission() {
        compose.setContent {
            EstudarioTheme(false) {
                StudyFocusCard(null, onStart = {}, onOpenPlan = {})
            }
        }
        compose.onNodeWithText("Seu próximo estudo aparece aqui").assertIsDisplayed()
        compose.onNodeWithText("Abrir plano").assertIsDisplayed()
    }

    private fun sampleTaskUi() = PlannerTaskUi(
        entity = PlanTaskEntity(
            id = "task",
            planId = "plan",
            competitionId = 1L,
            subjectId = 10L,
            topicId = 20L,
            subjectNameSnapshot = "Direito Constitucional",
            topicNameSnapshot = "Princípios fundamentais",
            scheduledEpochDay = 20_000L,
            type = PlanTaskType.THEORY,
            plannedMinutes = 50,
            priority = PlanPriority.HIGH,
            createdRevision = 0,
            updatedRevision = 0,
        ),
        actualMinutes = 0,
        questionsDone = 0,
        correctAnswers = 0,
    )
}
