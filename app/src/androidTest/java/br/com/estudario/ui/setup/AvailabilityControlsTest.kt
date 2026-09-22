package br.com.estudario.ui.setup

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performSemanticsAction
import androidx.compose.ui.test.SemanticsMatcher
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class AvailabilityControlsTest {
    @get:Rule val compose = createComposeRule()

    @Test
    fun accessibleDailySliderChangesOnlyItsDayAndSnapsToFifteenMinutes() {
        val monday = mutableIntStateOf(0)
        val tuesday = mutableIntStateOf(60)
        compose.setContent {
            Column {
                AvailabilityDaySlider("Seg", "segunda-feira", monday.intValue) { monday.intValue = it }
                AvailabilityDaySlider("Ter", "terça-feira", tuesday.intValue) { tuesday.intValue = it }
            }
        }

        compose.onNodeWithContentDescription("Tempo disponível na segunda-feira")
            .assert(SemanticsMatcher.expectValue(SemanticsProperties.StateDescription, "Folga"))
            .assert(SemanticsMatcher.expectValue(SemanticsProperties.ProgressBarRangeInfo, ProgressBarRangeInfo(0f, 0f..1_440f, 95)))
            .performSemanticsAction(SemanticsActions.SetProgress) { setProgress -> setProgress(137f) }

        compose.runOnIdle {
            assertEquals(135, monday.intValue)
            assertEquals(60, tuesday.intValue)
        }
    }
}
