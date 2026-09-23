package br.com.estudario.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertHeightIsAtLeast
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.test.core.app.ApplicationProvider
import androidx.compose.ui.unit.dp
import br.com.estudario.EstudarioApplication
import br.com.estudario.ui.AppViewModel
import br.com.estudario.ui.focus.FocusScreen
import br.com.estudario.ui.focus.FocusSubjectOption
import br.com.estudario.ui.focus.FocusSubjectPicker
import br.com.estudario.ui.navigation.FocusNavigationIcon
import br.com.estudario.ui.theme.EstudarioTheme
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class FocusNavigationTest {
    @get:Rule
    val compose = createComposeRule()

    @Test
    fun focusShowsRegisteredSubjectsAndStartsWithMultipleSelections() {
        compose.setContent {
            EstudarioTheme(darkTheme = false) {
                FocusSubjectPicker(
                    subjects = listOf(FocusSubjectOption(1, "Português"), FocusSubjectOption(2, "Direito")),
                    selectedIds = emptySet(),
                    onToggle = {},
                )
            }
        }

        compose.onNodeWithText("Português").assertIsDisplayed()
        compose.onNodeWithText("Direito").assertIsDisplayed()
    }

    @Test
    fun centralFocusIconExposesActiveState() {
        compose.setContent {
            EstudarioTheme(darkTheme = false) {
                FocusNavigationIcon(active = true, elapsedLabel = "12 min") { }
            }
        }

        compose.onNodeWithContentDescription("Foco ativo, 12 min").assertIsDisplayed()
        compose.onNodeWithTag("focus-active-outline").assertIsDisplayed()
    }

    @Test
    fun centralFocusButtonHasLargerTapTarget() {
        compose.setContent {
            EstudarioTheme(darkTheme = false) {
                FocusNavigationIcon(active = false, elapsedLabel = "") { }
            }
        }

        compose.onNodeWithTag("focus-navigation-button").assertHeightIsAtLeast(56.dp)
    }

    @Test
    fun focusOffersActionToCloseItsWindow() {
        val app = ApplicationProvider.getApplicationContext<EstudarioApplication>()
        var wentHome = false
        compose.setContent {
            EstudarioTheme(darkTheme = false) {
                FocusScreen(AppViewModel(app), onBack = {}, onHome = { wentHome = true })
            }
        }

        compose.onNodeWithText("Fechar janela", substring = true).performClick()
        compose.runOnIdle { assertTrue(wentHome) }
    }
}
