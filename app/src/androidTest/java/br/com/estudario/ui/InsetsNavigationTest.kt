package br.com.estudario.ui

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import br.com.estudario.ui.navigation.EstudarioTopBar
import br.com.estudario.ui.profile.UserProfile
import br.com.estudario.ui.theme.EstudarioTheme
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class InsetsNavigationTest {
    @get:Rule
    val compose = createComposeRule()

    @Test
    fun topBarPlacesBrandBelowProvidedStatusInset() {
        val topInsetPx = with(Density(1f)) { 24.dp.roundToPx() }
        compose.setContent {
            EstudarioTheme(false) {
                EstudarioTopBar(
                    profile = UserProfile(name = "Otávio"),
                    onOpenMenu = {},
                    onSearch = {},
                    onProfile = {},
                    windowInsets = WindowInsets(top = topInsetPx),
                )
            }
        }

        val brand = compose.onNodeWithText("ESTUDÁRIO").fetchSemanticsNode().boundsInRoot
        assertTrue("brand must clear the status bar inset: $brand", brand.top >= topInsetPx)
    }

    @Test
    fun topBarKeepsIdentityActionsAvailable() {
        compose.setContent {
            EstudarioTheme(false) {
                EstudarioTopBar(
                    profile = UserProfile(name = "Otávio"),
                    onOpenMenu = {},
                    onSearch = {},
                    onProfile = {},
                )
            }
        }

        compose.onNodeWithText("ESTUDÁRIO").assertExists()
        compose.onNodeWithContentDescription("Abrir menu").assertExists()
        compose.onNodeWithContentDescription("Pesquisar").assertExists()
    }
}
