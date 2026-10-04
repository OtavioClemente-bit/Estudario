package br.com.estudario.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Flag
import androidx.compose.material.icons.outlined.Info
import br.com.estudario.ui.brand.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import br.com.estudario.ui.brand.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
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
    var showDetails by remember { mutableStateOf(false) }
    // O título chega como "Prioridade • <tópico>": o tópico vira subtítulo menor.
    val topic = title.substringAfter("•", "").trim()
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = { Icon(Icons.Outlined.Flag, contentDescription = null) },
        title = { Text("Prioridade") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                if (topic.isNotBlank()) Text(topic, maxLines = 3, overflow = TextOverflow.Ellipsis, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Medium)
                Text(
                    "${PriorityPresentation.label(state.effectivePriority)} · ${PriorityPresentation.sourceLabel(state.assessment.source, state.userOverride != null).replaceFirstChar { it.lowercase() }}",
                    style = MaterialTheme.typography.bodySmall,
                )
                Spacer(Modifier.height(4.dp))
                PriorityLevel.entries.forEach { level ->
                    val selected = level == state.effectivePriority
                    Surface(
                        onClick = { onSetOverride(level) },
                        shape = RoundedCornerShape(16.dp),
                        color = if (selected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceContainerHigh,
                        border = if (selected) BorderStroke(1.dp, MaterialTheme.colorScheme.primary) else null,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Row(Modifier.padding(horizontal = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(selected = selected, onClick = { onSetOverride(level) })
                            Text(
                                PriorityPresentation.label(level),
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
                                color = if (selected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface,
                            )
                        }
                    }
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    TextButton(onClick = { showDetails = true }) {
                        Icon(Icons.Outlined.Info, contentDescription = null)
                        Spacer(Modifier.width(6.dp))
                        Text("Por que esta prioridade?")
                    }
                    if (state.userOverride != null) TextButton(onClick = onClearOverride) { Text("Voltar à sugestão") }
                }
                Text("É uma estimativa para o planejamento; não garante cobrança na prova.", style = MaterialTheme.typography.bodySmall)
            }
        },
        confirmButton = { TextButton(onClick = onDismiss) { Text("Fechar") } },
    )
    if (showDetails) {
        AlertDialog(
            onDismissRequest = { showDetails = false },
            icon = { Icon(Icons.Outlined.Info, contentDescription = null) },
            title = { Text("Por que esta prioridade") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(state.assessment.rationale ?: "Sem justificativa declarada.")
                    Text(PriorityPresentation.confidenceLabel(state.assessment.confidence))
                    Text(PriorityPresentation.evidenceLabel(state.assessment.evidence))
                }
            },
            confirmButton = { TextButton(onClick = { showDetails = false }) { Text("Fechar") } },
        )
    }
}
