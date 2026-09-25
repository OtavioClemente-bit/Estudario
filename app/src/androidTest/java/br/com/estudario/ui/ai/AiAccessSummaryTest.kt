package br.com.estudario.ui.ai

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import br.com.estudario.data.ai.AiFeature
import br.com.estudario.ui.theme.EstudarioTheme
import org.junit.Rule
import org.junit.Test

class AiAccessSummaryTest {
    @get:Rule val compose = createComposeRule()

    @Test fun exhaustedQuotaAndOfflineFallbackRemainVisible() {
        compose.setContent {
            EstudarioTheme(false) {
                AiAccessSummary(AiAccessUiState(mapOf(
                    AiFeature.SYLLABUS_GENERATION to AiFeatureDisplay("Cota utilizada", "Geração do Beta utilizada", false),
                    AiFeature.PLAN_GENERATION to AiFeatureDisplay("Acesso online indisponível", "", false),
                )))
            }
        }
        compose.onNodeWithText("Geração do Beta utilizada").assertIsDisplayed()
        compose.onNodeWithText("Importar .estudo ou montar manualmente").assertIsDisplayed()
    }
}
