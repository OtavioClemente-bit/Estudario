package br.com.estudario.ui.screens

import br.com.estudario.ui.theme.screenPadding
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.estudario.ui.AppViewModel
import br.com.estudario.ui.components.EmptyState
import br.com.estudario.data.local.TopicStatus
import br.com.estudario.ui.screens.home.firstEligibleQueueTopic

@Composable
fun QueueScreen(viewModel: AppViewModel, onBack: () -> Unit, onOpenTopic: (Long) -> Unit, showInlineBack: Boolean = true) {
    val queue by viewModel.queue.collectAsState()
    val events by viewModel.queueEvents.collectAsState()
    val nextQueueItemId = firstEligibleQueueTopic(queue)?.item?.id
    var postpone by remember { mutableStateOf<br.com.estudario.data.local.StudyQueueEntity?>(null) }
    var reason by remember { mutableStateOf("") }
    postpone?.let { item ->
        AlertDialog(
            onDismissRequest = { postpone = null },
            title = { Text("Não consegui estudar") },
            text = { Column(verticalArrangement = Arrangement.spacedBy(8.dp)) { Text("O item continuará na mesma posição. Este registro ajuda a entender as pendências sem punir sua sequência."); OutlinedTextField(reason, { reason = it }, label = { Text("Motivo (opcional)") }) } },
            confirmButton = { TextButton(onClick = { viewModel.postponeQueue(item, reason); postpone = null; reason = "" }) { Text("Registrar") } },
            dismissButton = { TextButton(onClick = { postpone = null }) { Text("Cancelar") } },
        )
    }
    LazyColumn(Modifier.fillMaxSize(), contentPadding = screenPadding(), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (showInlineBack) IconButton(onClick = onBack) { Icon(Icons.Outlined.ArrowBack, "Voltar") }
                Column { Text("Fila de estudos", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold); Text("A fila não avança com a mudança do dia") }
            }
        }
        if (queue.isEmpty()) item { EmptyState("Fila vazia", "Abra um tópico do edital e escolha “Adicionar à fila”.") }
        itemsIndexed(queue, key = { _, item -> item.item.id }) { index, row ->
            ElevatedCard(onClick = { onOpenTopic(row.topic.id) }) {
                Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Surface(shape = MaterialTheme.shapes.small, color = MaterialTheme.colorScheme.primaryContainer) { Text("${index + 1}", Modifier.padding(horizontal = 10.dp, vertical = 6.dp), fontWeight = FontWeight.Bold) }
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f)) {
                        Text(row.topic.title, fontWeight = FontWeight.Bold)
                        val completed = row.topic.status in setOf(TopicStatus.ESTUDADO, TopicStatus.REVISANDO, TopicStatus.DOMINADO)
                        val label = queueRowLabel(
                            paused = row.item.paused,
                            completed = completed,
                            isNext = row.item.id == nextQueueItemId,
                        )
                        Text(label + if (row.item.postponements > 0) " • ${row.item.postponements} adiamento(s)" else "", style = MaterialTheme.typography.bodySmall)
                    }
                    IconButton(onClick = { postpone = row.item }) { Icon(Icons.Outlined.EventBusy, "Não consegui estudar") }
                    IconButton(enabled = index > 0, onClick = {
                        val previous = queue[index - 1].item
                        viewModel.updateQueue(previous.copy(position = row.item.position))
                        viewModel.updateQueue(row.item.copy(position = previous.position))
                    }) { Icon(Icons.Outlined.KeyboardArrowUp, "Mover para cima") }
                    IconButton(enabled = index < queue.lastIndex, onClick = {
                        val next = queue[index + 1].item
                        viewModel.updateQueue(next.copy(position = row.item.position))
                        viewModel.updateQueue(row.item.copy(position = next.position))
                    }) { Icon(Icons.Outlined.KeyboardArrowDown, "Mover para baixo") }
                    IconButton(onClick = { viewModel.updateQueue(row.item.copy(paused = !row.item.paused)) }) { Icon(if (row.item.paused) Icons.Outlined.PlayArrow else Icons.Outlined.Pause, if (row.item.paused) "Retomar" else "Pausar") }
                    IconButton(onClick = { viewModel.removeQueue(row.item) }) { Icon(Icons.Outlined.Delete, "Remover") }
                }
            }
        }
        if (events.isNotEmpty()) item { Text("Histórico recente", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold) }
        itemsIndexed(events.take(10), key = { _, event -> "event-${event.id}" }) { _, event ->
            ListItem(headlineContent = { Text(queue.firstOrNull { it.topic.id == event.topicId }?.topic?.title ?: "Tópico") }, supportingContent = { Text(event.type.name.lowercase().replaceFirstChar { it.uppercase() } + event.reason.takeIf { it.isNotBlank() }?.let { " • $it" }.orEmpty()) }, leadingContent = { Icon(Icons.Outlined.History, null) })
        }
    }
}
