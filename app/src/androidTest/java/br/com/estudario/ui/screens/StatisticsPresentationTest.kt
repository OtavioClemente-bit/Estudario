package br.com.estudario.ui.screens

import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.test.hasText
import androidx.test.core.app.ApplicationProvider
import br.com.estudario.EstudarioApplication
import br.com.estudario.ui.AppViewModel
import br.com.estudario.ui.EstudarioApp
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class StatisticsPresentationTest {
    @get:Rule val compose = createComposeRule()

    @Test fun periodControlsAndEvidenceSectionsAreAvailableOnCompactScreen() {
        val app = ApplicationProvider.getApplicationContext<EstudarioApplication>()
        compose.setContent { EstudarioApp(AppViewModel(app)) }
        compose.waitUntil(5_000) {
            compose.onAllNodesWithContentDescription("Abrir menu").fetchSemanticsNodes().isNotEmpty()
        }

        compose.onNodeWithContentDescription("Abrir menu").performClick()
        compose.onNodeWithText("Desempenho").performClick()

        compose.onNodeWithTag("performance-period-30").assertIsSelected()
        compose.onNodeWithTag("performance-period-7").performClick().assertIsSelected()
        compose.onNodeWithTag("performance-period-90").performClick().assertIsSelected()
        compose.onNodeWithTag("performance-period-all").performClick().assertIsSelected()
        compose.onNodeWithTag("performance-list").performScrollToNode(hasText("Acurácia baseada em tentativas respondidas"))
        compose.onNodeWithText("Acurácia baseada em tentativas respondidas").assertExists()
        compose.onNodeWithTag("performance-list").performScrollToNode(hasText("Tarefas agendadas em planos ativos e não arquivados"))
        compose.onNodeWithText("Tarefas agendadas em planos ativos e não arquivados").assertExists()
        compose.onNodeWithTag("performance-list").performScrollToNode(hasText("Minutos em fontes separadas para não somar sessões sobrepostas"))
        compose.onNodeWithText("Minutos em fontes separadas para não somar sessões sobrepostas").assertExists()
        compose.onNodeWithText("Retenção").assertDoesNotExist()
        assertEquals(1, compose.onAllNodesWithContentDescription("Abrir menu").fetchSemanticsNodes().size)
        compose.onNodeWithTag("main-bottom-navigation").assertDoesNotExist()
    }
}
