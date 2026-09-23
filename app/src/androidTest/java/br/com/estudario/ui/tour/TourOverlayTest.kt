package br.com.estudario.ui.tour

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import br.com.estudario.ui.theme.EstudarioTheme
import org.junit.Rule
import org.junit.Test

class TourOverlayTest {
    @get:Rule val compose = createComposeRule()

    @Test
    fun keepsTheGuideCardVisibleWhileWaitingForATrainTargetAfterScrolling() {
        val step = tourSteps(TourId.TRAIN).first { it.key == TourKey.TRAIN_START }

        compose.setContent {
            EstudarioTheme(darkTheme = false) {
                TourOverlay(
                    tour = TourId.TRAIN,
                    step = step,
                    stepIndex = 3,
                    stepCount = tourSteps(TourId.TRAIN).size,
                    bounds = null,
                    onPrevious = {},
                    onNext = {},
                    onSkip = {},
                    onWatchVideo = {},
                )
            }
        }

        compose.onNodeWithText("Monte a sessão").assertIsDisplayed()
    }
}
