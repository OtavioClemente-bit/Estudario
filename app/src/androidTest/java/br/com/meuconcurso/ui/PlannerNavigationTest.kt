package br.com.meuconcurso.ui

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.core.app.ApplicationProvider
import br.com.meuconcurso.MeuConcursoApplication
import org.junit.Rule
import org.junit.Test

class PlannerNavigationTest {
    @get:Rule val compose = createComposeRule()

    @Test fun bottomNavigationContainsPlanAndErrorsLivesInMore() {
        val app = ApplicationProvider.getApplicationContext<MeuConcursoApplication>()
        compose.setContent { MeuConcursoApp(AppViewModel(app)) }
        listOf("Início", "Edital", "Plano", "Treinar", "Mais").forEach { compose.onNodeWithText(it).assertExists() }
        compose.onNodeWithText("Plano").performClick()
        compose.onNodeWithText("Hoje").assertExists()
        compose.onNodeWithText("Mais").performClick()
        compose.onNodeWithText("Caderno de erros").assertExists()
    }
}
