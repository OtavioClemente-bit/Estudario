package br.com.estudario.ui

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.core.app.ApplicationProvider
import br.com.estudario.EstudarioApplication
import org.junit.Rule
import org.junit.Test

class PlannerNavigationTest {
    @get:Rule val compose = createComposeRule()

    @Test fun bottomNavigationContainsPlanAndErrorsLivesInMore() {
        val app = ApplicationProvider.getApplicationContext<EstudarioApplication>()
        compose.setContent { EstudarioApp(AppViewModel(app)) }
        compose.waitUntil(5_000) {
            compose.onAllNodesWithText("Início").fetchSemanticsNodes().isNotEmpty()
        }
        listOf("Início", "Edital", "Plano", "Treinar").forEach { compose.onNodeWithText(it).assertExists() }
        compose.onNodeWithText("Mais").assertDoesNotExist()
        compose.onNodeWithContentDescription("Abrir perfil").assertExists()
        compose.onNodeWithText("Plano").performClick()
        compose.onNodeWithText("Hoje").assertExists()
        compose.onNodeWithContentDescription("Abrir menu").performClick()
        compose.onNodeWithText("Caderno de erros").assertExists()
    }
}
