package br.com.estudario.ui.setup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.estudario.domain.setup.formatAvailabilityMinutes
import br.com.estudario.domain.setup.snapAvailabilityMinutes

@Composable
internal fun AvailabilityDaySlider(
    shortDayName: String,
    fullDayName: String,
    minutes: Int,
    modifier: Modifier = Modifier,
    onMinutesChange: (Int) -> Unit,
) {
    Column(modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(shortDayName, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
            Text(formatAvailabilityMinutes(minutes), style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.primary)
        }
        Slider(
            value = minutes.coerceIn(0, 1_440).toFloat(),
            onValueChange = { onMinutesChange(snapAvailabilityMinutes(it)) },
            valueRange = 0f..1_440f,
            steps = 95,
            modifier = Modifier
                .fillMaxWidth()
                .semantics {
                    contentDescription = "Tempo disponível na $fullDayName"
                    stateDescription = formatAvailabilityMinutes(minutes)
                },
        )
    }
}
