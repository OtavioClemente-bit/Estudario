package br.com.estudario.ui.focus

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.estudario.domain.FocusRules
import br.com.estudario.ui.brand.OutlinedButton
import br.com.estudario.ui.brand.TextButton
import br.com.estudario.ui.components.AlertDialog

/**
 * Cronômetro esquecido (regra de FocusRunning.needsCheck, a mesma do site): pergunta, sem bronca, se
 * a pessoa ainda está estudando. "Sim" segue contando; "Já parei" pergunta quanto tempo ela estudou
 * (sugere a meta) e salva só isso.
 */
@Composable
fun FocusStillStudyingDialog(
    elapsedMinutes: Int,
    plannedMinutes: Int?,
    onStillStudying: () -> Unit,
    onSave: (minutes: Int) -> Unit,
) {
    var asking by remember { mutableStateOf(false) }
    val maxMinutes = elapsedMinutes.coerceAtLeast(1)
    var minutes by remember { mutableIntStateOf(FocusRules.suggestedMinutes(plannedMinutes).coerceAtMost(maxMinutes)) }
    if (!asking) {
        AlertDialog(
            onDismissRequest = {},
            title = { Text("Você ainda está estudando?") },
            text = { Text("O cronômetro do foco está ligado há ${label(elapsedMinutes)}. Se você já tinha parado, a gente salva só o tempo que você estudou de verdade.") },
            confirmButton = { TextButton(onClick = onStillStudying) { Text("Sim, continuar") } },
            dismissButton = { TextButton(onClick = { asking = true }) { Text("Já parei") } },
        )
    } else {
        AlertDialog(
            onDismissRequest = {},
            title = { Text("Quanto tempo você estudou?") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Ajuste o tempo e o foco é salvo com ele.")
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally), verticalAlignment = Alignment.CenterVertically) {
                        OutlinedButton(onClick = { minutes = (minutes - 5).coerceAtLeast(1) }, enabled = minutes > 1) { Text("−5") }
                        Text(label(minutes), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                        OutlinedButton(onClick = { minutes = (minutes + 5).coerceAtMost(maxMinutes) }, enabled = minutes < maxMinutes) { Text("+5") }
                    }
                    plannedMinutes?.takeIf { it in 1..maxMinutes && it != minutes }?.let { p ->
                        TextButton(onClick = { minutes = p }) { Text("Usar a meta (${label(p)})") }
                    }
                    if (minutes != maxMinutes) TextButton(onClick = { minutes = maxMinutes }) { Text("Estudei o tempo todo (${label(maxMinutes)})") }
                }
            },
            confirmButton = { TextButton(onClick = { onSave(minutes) }) { Text("Salvar") } },
            dismissButton = { TextButton(onClick = { asking = false }) { Text("Voltar") } },
        )
    }
}

private fun label(minutes: Int): String = when {
    minutes < 60 -> "$minutes min"
    minutes % 60 == 0 -> "${minutes / 60} h"
    else -> "${minutes / 60} h ${minutes % 60} min"
}
