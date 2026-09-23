package br.com.estudario.ui

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.unit.dp
import androidx.test.core.app.ApplicationProvider
import br.com.estudario.EstudarioApplication
import br.com.estudario.data.local.FocusSessionOrigin
import br.com.estudario.domain.setup.InitialSetupStatus
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class PlannerNavigationTest {
    @get:Rule val compose = createComposeRule()
    private lateinit var app: EstudarioApplication

    @Before fun skipFirstRunForNavigationTests() = runBlocking {
        app = ApplicationProvider.getApplicationContext()
        app.preferences.setOnboardingCompleted(true)
        app.preferences.updateInitialSetup { it.copy(status = InitialSetupStatus.DEFERRED) }
        app.preferences.clearFocusSession(minutes = 0, taskId = null)
    }

    private fun openHome() {
        compose.setContent { EstudarioApp(AppViewModel(app)) }
        compose.waitUntil(5_000) {
            compose.onAllNodesWithText("Início").fetchSemanticsNodes().isNotEmpty()
        }
    }

    @Test fun bottomNavigationContainsPlanAndErrorsLivesInMore() {
        openHome()
        listOf("Início", "Edital", "Plano", "Treinar").forEach { label ->
            assertTrue(compose.onAllNodesWithText(label).fetchSemanticsNodes().isNotEmpty())
        }
        compose.onNodeWithText("Mais").assertDoesNotExist()
        compose.onNodeWithContentDescription("Abrir perfil").assertExists()
        compose.onNodeWithText("Plano").performClick()
        compose.onNodeWithText("Como você quer montar seu plano?").assertExists()
        compose.onNodeWithContentDescription("Abrir menu").performClick()
        compose.onNodeWithText("Caderno de erros").assertExists()
    }

    @Test fun focusOpensOverCurrentTabAndDismissesBackToIt() {
        openHome()

        compose.onNodeWithText("Plano").performClick()
        compose.onNodeWithText("Como você quer montar seu plano?").assertIsDisplayed()
        compose.onAllNodesWithText("Foco").onFirst().performClick()

        compose.onNodeWithTag("focus-overlay").assertIsDisplayed()
        val overlayHeight = compose.onNodeWithTag("focus-overlay").fetchSemanticsNode().boundsInRoot.height
        assertTrue("Focus window should leave more of the app visible behind it", overlayHeight <= with(compose.density) { 640.dp.toPx() })
        compose.onNodeWithTag("main-bottom-navigation").assertIsDisplayed()
        compose.onNodeWithContentDescription("Fechar janela").performClick()

        compose.onNodeWithTag("focus-overlay").assertDoesNotExist()
        compose.onNodeWithText("Como você quer montar seu plano?").assertIsDisplayed()
    }

    @Test fun closingFocusWindowKeepsAnActiveTimerRunning() {
        runBlocking {
            app.preferences.startFocusSession(
                startedAt = System.currentTimeMillis(),
                title = "Sessão de teste",
                subjectIds = emptySet(),
                origin = FocusSessionOrigin.LIVRE,
                topicId = null,
                taskId = null,
                previousFilter = -1,
            )
            try {
                openHome()
                compose.onAllNodesWithText("Foco").onFirst().performClick()
                compose.onNodeWithText("Você está estudando").assertIsDisplayed()

                compose.onNodeWithContentDescription("Fechar janela").performClick()
                compose.onNodeWithTag("focus-overlay").assertDoesNotExist()

                compose.onAllNodesWithText("Foco").onFirst().performClick()
                compose.onNodeWithText("Você está estudando").assertIsDisplayed()
                compose.onNodeWithText("Sessão de teste").assertIsDisplayed()
            } finally {
                app.preferences.clearFocusSession(minutes = 0, taskId = null)
            }
        }
    }

    @Test fun secondaryScreensKeepOneGlobalMenuAndDoNotShowBottomNavigation() {
        openHome()

        compose.onNodeWithContentDescription("Abrir menu").performClick()
        compose.onNodeWithText("Histórico e fontes").performClick()
        compose.onNodeWithTag("main-bottom-navigation").assertDoesNotExist()
        compose.onAllNodesWithContentDescription("Abrir menu").assertCountEquals(1)

        compose.onNodeWithContentDescription("Abrir menu").performClick()
        compose.onNodeWithText("Desempenho").performClick()
        compose.onNodeWithTag("main-bottom-navigation").assertDoesNotExist()
        compose.onAllNodesWithContentDescription("Abrir menu").assertCountEquals(1)

        compose.onNodeWithContentDescription("Abrir menu").performClick()
        compose.onNodeWithText("Notificações").performClick()
        compose.onNodeWithTag("main-bottom-navigation").assertDoesNotExist()
        compose.onAllNodesWithContentDescription("Abrir menu").assertCountEquals(1)

        compose.onNodeWithContentDescription("Abrir menu").performClick()
        compose.onNodeWithText("Ajustes").performClick()
        compose.onNodeWithText("Preferências de estudo").assertExists()
        compose.onNodeWithTag("main-bottom-navigation").assertDoesNotExist()
        compose.onAllNodesWithContentDescription("Abrir menu").assertCountEquals(1)
    }

    @Test fun focusHistoryOpensAsItsOwnDrawerDestination() {
        openHome()

        compose.onNodeWithContentDescription("Abrir menu").performClick()
        compose.onNodeWithText("Histórico do foco").performClick()

        compose.onNodeWithText("Seu histórico de foco").assertIsDisplayed()
        compose.onNodeWithText("Tempo total de foco").assertIsDisplayed()
        compose.onNodeWithTag("main-bottom-navigation").assertDoesNotExist()
    }
}
