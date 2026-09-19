package br.com.estudario.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.EditNote
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.estudario.ui.AppViewModel
import br.com.estudario.ui.components.EmptyState
import br.com.estudario.ui.components.ScreenTitle
import java.text.DateFormat
import java.util.Date
import br.com.estudario.data.local.ErrorStatus

@Composable
fun ErrorsScreen(viewModel: AppViewModel, onTrainErrors: () -> Unit, onOpenTopic: (Long) -> Unit) {
    val errors by viewModel.errors.collectAsState()
    val topics by viewModel.topics.collectAsState()
    val subjects by viewModel.subjects.collectAsState()
    val concepts by viewModel.errorConcepts.collectAsState()
    var status by remember { mutableStateOf<ErrorStatus?>(null) }
    val agora = remember { System.currentTimeMillis() }
    val paraHoje = errors.count { it.entry.nextRetryAt != null && it.entry.nextRetryAt!! <= agora }
    var editing by remember { mutableStateOf<br.com.estudario.data.local.ErrorNotebookEntryEntity?>(null) }
    val filtered = errors.filter { status == null || it.entry.status == status }
    editing?.let { entry -> ErrorNoteDialog(entry.comment, entry.concept, onDismiss = { editing = null }) { comment, concept -> viewModel.updateError(entry.copy(comment = comment, concept = concept)) } }

    LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item { ScreenTitle("Caderno de erros", "O histórico nunca é apagado ao acertar novamente") }
        item {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = onTrainErrors, enabled = errors.isNotEmpty(), modifier = Modifier.weight(1f)) {
                    Text(if (paraHoje > 0) "Revisar $paraHoje de hoje" else "Treinar meus erros")
                }
            }
            if (paraHoje > 0) Text(
                "A questão errada volta sozinha: 3 dias depois do erro, 10 se você acertar, 30 no acerto seguinte.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                FilterChip(selected = status == null, onClick = { status = null }, label = { Text("Todos") })
                ErrorStatus.entries.forEach { value -> FilterChip(selected = status == value, onClick = { status = value }, label = { Text(when (value) { ErrorStatus.NOVO -> "Novos"; ErrorStatus.REVISANDO -> "Revisando"; ErrorStatus.CORRIGIDO -> "Corrigidos"; ErrorStatus.RECORRENTE -> "Recorrentes" }) }) }
            }
        }
        if (filtered.isEmpty()) item { EmptyState("Nada pendente", if (errors.isEmpty()) "Questões erradas aparecerão automaticamente aqui." else "Você revisou todos os erros. Muito bem!") }
        items(filtered, key = { it.entry.id }) { item ->
            val topic = topics.firstOrNull { it.id == item.question.topicId }
            val subject = subjects.firstOrNull { it.id == topic?.subjectId }
            ElevatedCard(onClick = { onOpenTopic(item.question.topicId) }) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row {
                        Icon(Icons.Outlined.ErrorOutline, null, tint = if (item.entry.pending) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.secondary)
                        Spacer(Modifier.width(8.dp))
                        Column(Modifier.weight(1f)) {
                            Text(topic?.title ?: "Tópico", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            Text(subject?.name.orEmpty(), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        IconButton(onClick = { viewModel.deleteError(item.entry.id) }) { Icon(Icons.Outlined.Delete, "Remover do caderno") }
                        IconButton(onClick = { editing = item.entry }) { Icon(Icons.Outlined.EditNote, "Editar comentário") }
                    }
                    Text(item.question.statement.take(180), maxLines = 3)
                    Text("Erros: ${item.entry.errorCount}  •  Acertos ao refazer: ${item.entry.retryCorrectCount}", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.error)
                    if (item.entry.selectedAnswer != null || item.entry.correctAnswer != null) Text("Marquei ${item.entry.selectedAnswer ?: "—"} • Correta ${item.entry.correctAnswer ?: "—"}", style = MaterialTheme.typography.bodySmall)
                    if (item.entry.comment.isNotBlank()) Text("Minha observação: ${item.entry.comment}")
                    Text("Último erro: ${DateFormat.getDateInstance(DateFormat.SHORT).format(Date(item.entry.lastErrorAt))}", style = MaterialTheme.typography.bodySmall)
                    item.entry.nextRetryAt?.let { volta ->
                        val venceu = volta <= agora
                        Text(
                            if (venceu) "Volta hoje" else "Volta em ${DateFormat.getDateInstance(DateFormat.SHORT).format(Date(volta))}",
                            style = MaterialTheme.typography.labelMedium,
                            color = if (venceu) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                        )
                    }
                    AssistChip(onClick = {}, label = { Text(item.entry.status.name.lowercase().replaceFirstChar { it.uppercase() }) })
                }
            }
        }
        if (concepts.isNotEmpty()) item { Text("Conceitos para corrigir", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold) }
        items(concepts, key = { "concept-${it.id}" }) { concept ->
            ElevatedCard {
                Row(Modifier.padding(16.dp)) {
                    Column(Modifier.weight(1f)) { Text(concept.title, fontWeight = FontWeight.Bold); if (concept.summary.isNotBlank()) Text(concept.summary, style = MaterialTheme.typography.bodySmall) }
                    IconButton(onClick = { viewModel.saveErrorConcept(concept.copy(isFavorite = !concept.isFavorite)) }) { Icon(if (concept.isFavorite) Icons.Outlined.Star else Icons.Outlined.StarBorder, "Favoritar") }
                }
            }
        }
    }
}

@Composable
private fun ErrorNoteDialog(initialComment: String, initialConcept: String, onDismiss: () -> Unit, onSave: (String, String) -> Unit) {
    var comment by remember { mutableStateOf(initialComment) }
    var concept by remember { mutableStateOf(initialConcept) }
    AlertDialog(onDismissRequest = onDismiss, title = { Text("Anotações do erro") }, text = {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            OutlinedTextField(concept, { concept = it }, label = { Text("Conceito associado") })
            OutlinedTextField(comment, { comment = it }, label = { Text("Comentário próprio") }, minLines = 4)
        }
    }, confirmButton = { TextButton(onClick = { onSave(comment, concept); onDismiss() }) { Text("Salvar") } }, dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } })
}
