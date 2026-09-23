package br.com.estudario.ui

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import br.com.estudario.ui.navigation.EstudarioTopBar
import br.com.estudario.ui.withSecondaryRouteBottomInset
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
        var menuOpened = false
        compose.setContent {
            EstudarioTheme(false) {
                EstudarioTopBar(
                    profile = UserProfile(name = "Otávio"),
                    onOpenMenu = { menuOpened = true },
                    onSearch = {},
                    onProfile = {},
                )
            }
        }

        compose.onNodeWithText("ESTUDÁRIO").assertExists()
        compose.onNodeWithContentDescription("Abrir menu").assertExists()
        compose.onNodeWithContentDescription("Pesquisar").assertExists()
        compose.onNodeWithContentDescription("Abrir menu").performClick()
        assertTrue(menuOpened)
    }

    @Test
    fun secondaryRouteReservesBottomNavigationInsetWithoutAddingTopInset() {
        val bottomInsetPx = with(Density(1f)) { 24.dp.roundToPx() }
        compose.setContent {
            EstudarioTheme(false) {
                Box(Modifier.height(200.dp).testTag("viewport")) {
                    Box(
                        Modifier
                            .fillMaxSize()
                            .withSecondaryRouteBottomInset(
                                showBottomNavigation = false,
                                bottomInsets = WindowInsets(bottom = bottomInsetPx),
                            ),
                    ) {
                        androidx.compose.material3.Text("Último conteúdo", Modifier.align(Alignment.BottomCenter).testTag("safe-content"))
                    }
                }
            }
        }

        val viewportBottom = compose.onNodeWithTag("viewport").fetchSemanticsNode().boundsInRoot.bottom
        val contentBottom = compose.onNodeWithTag("safe-content").fetchSemanticsNode().boundsInRoot.bottom
        assertTrue("secondary route content must stop above the system navigation area", contentBottom <= viewportBottom - bottomInsetPx)
    }
}
