package br.com.estudario.ui.ai

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import br.com.estudario.ui.theme.EstudarioShapes

/**
 * Aviso no topo do app para as gerações que foram para segundo plano: "gerando…" enquanto roda e
 * "pronto, revisar e salvar" quando termina. [onOpen] recebe o texto e a matéria de destino.
 */
@Composable
fun BackgroundAiBanner(onOpen: (String, Long?) -> Unit, modifier: Modifier = Modifier) {
    val tasks by BackgroundAiTasks.tasks.collectAsState()
    val visible = tasks.filterNot { it.inForeground }
    AnimatedVisibility(visible.isNotEmpty(), modifier) {
        Column(Modifier.padding(horizontal = 16.dp, vertical = 6.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            visible.forEach { task -> TaskRow(task, onOpen) }
        }
    }
}

@Composable
private fun TaskRow(task: BackgroundAiTasks.Task, onOpen: (String, Long?) -> Unit) {
    val status = task.status
    val failed = status is BackgroundAiTasks.Status.Failed
    Surface(
        shape = EstudarioShapes.row,
        color = if (failed) MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.primaryContainer,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(Modifier.padding(start = 14.dp, end = 4.dp, top = 8.dp, bottom = 8.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            when (status) {
                BackgroundAiTasks.Status.Running -> CircularProgressIndicator(Modifier.size(20.dp), strokeWidth = 2.dp)
                is BackgroundAiTasks.Status.Ready -> Icon(Icons.Outlined.AutoAwesome, null, tint = MaterialTheme.colorScheme.primary)
                is BackgroundAiTasks.Status.Failed -> Icon(Icons.Outlined.ErrorOutline, null, tint = MaterialTheme.colorScheme.error)
            }
            Column(Modifier.weight(1f)) {
                Text(
                    when (status) {
                        BackgroundAiTasks.Status.Running -> "Preparando ${task.kind.lowercase()}…"
                        is BackgroundAiTasks.Status.Ready -> "${task.kind} pronto"
                        is BackgroundAiTasks.Status.Failed -> "Não deu para gerar"
                    },
                    style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.SemiBold,
                )
                Text(
                    if (status is BackgroundAiTasks.Status.Failed) status.message else task.title,
                    style = MaterialTheme.typography.bodySmall, maxLines = 2, overflow = TextOverflow.Ellipsis,
                )
            }
            if (status is BackgroundAiTasks.Status.Ready) {
                FilledTonalButton(onClick = { BackgroundAiTasks.dismiss(task.id); task.open?.invoke() ?: onOpen(status.result, status.competitionId) }) { Text("Revisar") }
            }
            if (status is BackgroundAiTasks.Status.Failed) {
                IconButton(onClick = { BackgroundAiTasks.dismiss(task.id) }) { Icon(Icons.Outlined.Close, "Dispensar aviso", Modifier.size(18.dp)) }
            }
        }
    }
}
