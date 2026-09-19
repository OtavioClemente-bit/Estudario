package br.com.estudario.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.com.estudario.domain.PriorityLevel

@Composable
fun PriorityEditorDialog(
    title: String,
    state: PriorityEditorState,
    onDismiss: () -> Unit,
    onSetOverride: (PriorityLevel) -> Unit,
    onClearOverride: () -> Unit,
) {
    var showDetails = androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(false) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("Prioridade efetiva: ${PriorityPresentation.label(state.effectivePriority)}")
                Text(PriorityPresentation.sourceLabel(state.assessment.source, state.userOverride != null))
                Text("A prioridade é uma estimativa de planejamento; não garante cobrança na prova.")
                HorizontalDivider()
                PriorityLevel.entries.forEach { level ->
                    val selected = state.userOverride == level
                    if (selected) {
                        Button(onClick = { onSetOverride(level) }, modifier = Modifier.fillMaxWidth()) { Text(PriorityPresentation.label(level)) }
                    } else {
                        OutlinedButton(onClick = { onSetOverride(level) }, modifier = Modifier.fillMaxWidth()) { Text(PriorityPresentation.label(level)) }
                    }
                }
                if (state.userOverride != null) {
                    TextButton(onClick = onClearOverride, modifier = Modifier.fillMaxWidth()) { Text("Usar sugestão automática") }
                }
                AssistChip(
                    onClick = { showDetails.value = true },
                    label = { Text("Por que esta prioridade?") },
                    leadingIcon = { Icon(Icons.Outlined.Info, contentDescription = null) },
                )
            }
        },
        confirmButton = { TextButton(onClick = onDismiss) { Text("Fechar") } },
    )
    if (showDetails.value) {
        AlertDialog(
            onDismissRequest = { showDetails.value = false },
            title = { Text("Justificativa") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(state.assessment.rationale ?: "Sem justificativa declarada.")
                    Text(PriorityPresentation.confidenceLabel(state.assessment.confidence))
                    Spacer(Modifier.height(2.dp))
                    Text(PriorityPresentation.evidenceLabel(state.assessment.evidence))
                }
            },
            confirmButton = { TextButton(onClick = { showDetails.value = false }) { Text("Fechar") } },
        )
    }
}
