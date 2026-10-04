package br.com.estudario.ui.screens

import br.com.estudario.ui.components.AlertDialog
import br.com.estudario.ui.theme.screenPadding
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.EditNote
import androidx.compose.material.icons.outlined.ExpandLess
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.OpenInNew
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.*
import br.com.estudario.ui.brand.Button
import br.com.estudario.ui.brand.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import br.com.estudario.data.local.ErrorStatus
import br.com.estudario.ui.AppViewModel
import br.com.estudario.ui.components.ConfirmDialog
import br.com.estudario.ui.components.EmptyState
import br.com.estudario.ui.components.MarkdownText
import br.com.estudario.ui.components.ScreenTitle
import br.com.estudario.ui.theme.EstudarioShapes
import br.com.estudario.ui.theme.estudarioColors
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Caderno de erros: onde a pessoa vê o que errou, por que errou e quando refazer.
 *
 * De cima para baixo: o resumo (o que refazer hoje e quanto já foi corrigido), onde mais se erra
 * (por matéria, tocável para filtrar), os filtros e os erros em cartões com a sua resposta ao lado
 * da correta e a resolução a um toque. O histórico nunca é apagado ao acertar de novo.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ErrorsScreen(viewModel: AppViewModel, onTrainErrors: () -> Unit, onOpenTopic: (Long) -> Unit) {
    val errors by viewModel.errors.collectAsState()
    val topics by viewModel.topics.collectAsState()
    val subjects by viewModel.subjects.collectAsState()
    val concepts by viewModel.errorConcepts.collectAsState()
    var status by remember { mutableStateOf<ErrorStatus?>(null) }
    var subjectFilter by remember { mutableStateOf<Long?>(null) }
    var editing by remember { mutableStateOf<br.com.estudario.data.local.ErrorNotebookEntryEntity?>(null) }
    var deleting by remember { mutableStateOf<br.com.estudario.data.local.ErrorNotebookEntryEntity?>(null) }
    val agora = remember { System.currentTimeMillis() }
    val colors = estudarioColors()

    val topicById = remember(topics) { topics.associateBy { it.id } }
    val subjectById = remember(subjects) { subjects.associateBy { it.id } }
    fun subjectOf(topicId: Long) = topicById[topicId]?.subjectId
    val paraHoje = errors.count { it.entry.pending && it.entry.nextRetryAt != null && it.entry.nextRetryAt!! <= agora }
    val recorrentes = errors.count { it.entry.status == ErrorStatus.RECORRENTE }
    val corrigidos = errors.count { it.entry.status == ErrorStatus.CORRIGIDO }
    val bySubject = remember(errors, topicById) {
        errors.groupBy { subjectOf(it.question.topicId) }.mapNotNull { (id, list) -> id?.let { it to list.size } }.sortedByDescending { it.second }
    }
    val filtered = errors
        .filter { status == null || it.entry.status == status }
        .filter { subjectFilter == null || subjectOf(it.question.topicId) == subjectFilter }
        .sortedWith(compareByDescending<br.com.estudario.data.local.ErrorWithQuestion> { it.entry.nextRetryAt?.let { v -> v <= agora } == true }.thenByDescending { it.entry.lastErrorAt })

    editing?.let { entry -> ErrorNoteDialog(entry.comment, entry.concept, onDismiss = { editing = null }) { comment, concept -> viewModel.updateError(entry.copy(comment = comment, concept = concept)) } }
    deleting?.let { entry ->
        ConfirmDialog(
            title = "Tirar do caderno?",
            message = "Este erro sai do caderno e deixa de voltar para refazer. As suas respostas continuam no histórico de desempenho.",
            confirmLabel = "Tirar do caderno",
            onDismiss = { deleting = null },
        ) { viewModel.deleteError(entry.id); deleting = null }
    }

    LazyColumn(Modifier.fillMaxSize(), contentPadding = screenPadding(), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        item { ScreenTitle("Caderno de erros", "Cada erro vira um acerto na prova") }

        if (errors.isEmpty()) {
            item { EmptyState("Nenhum erro ainda", "Quando você errar uma questão, ela entra aqui sozinha com a sua resposta, a correta e o dia certo de refazer.") }
            return@LazyColumn
        }

        // Resumo: o que fazer agora.
        item {
            Surface(shape = EstudarioShapes.spotlight, color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.55f), modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    Row(verticalAlignment = Alignment.Bottom) {
                        Column(Modifier.weight(1f)) {
                            Text("$paraHoje", style = MaterialTheme.typography.displaySmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.error)
                            Text(if (paraHoje == 1) "erro para refazer hoje" else "erros para refazer hoje", style = MaterialTheme.typography.bodyMedium)
                        }
                        Column(horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            SummaryLine("${errors.size}", "no caderno")
                            SummaryLine("$recorrentes", "recorrentes")
                            SummaryLine("$corrigidos", "corrigidos")
                        }
                    }
                    val fraction = if (errors.isEmpty()) 0f else corrigidos.toFloat() / errors.size
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Box(Modifier.fillMaxWidth().height(8.dp).clip(CircleShape).background(MaterialTheme.colorScheme.surface.copy(alpha = 0.6f))) {
                            Box(Modifier.fillMaxWidth(fraction.coerceIn(0.015f, 1f)).fillMaxHeight().clip(CircleShape).background(colors.completed))
                        }
                        Text("${(fraction * 100).toInt()}% dos erros já corrigidos (acertou ao refazer em sequência)", style = MaterialTheme.typography.labelSmall)
                    }
                    Button(onClick = onTrainErrors, modifier = Modifier.fillMaxWidth().heightIn(min = 50.dp)) {
                        Icon(Icons.Outlined.PlayArrow, null); Spacer(Modifier.width(8.dp))
                        Text(if (paraHoje > 0) "Refazer os $paraHoje de hoje" else "Treinar meus erros")
                    }
                    Text("A questão volta sozinha: 3 dias depois do erro, 10 dias se você acertar, 30 no acerto seguinte.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }

        // Onde mais se erra: tocar filtra a lista.
        if (bySubject.size > 1) item {
            Surface(shape = EstudarioShapes.panel, color = MaterialTheme.colorScheme.surfaceContainerLow, modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(vertical = 10.dp)) {
                    Text("Onde você mais erra", Modifier.padding(horizontal = 16.dp, vertical = 4.dp), style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                    val max = bySubject.first().second
                    bySubject.take(6).forEach { (id, count) ->
                        val selected = subjectFilter == id
                        Surface(onClick = { subjectFilter = if (selected) null else id }, color = if (selected) MaterialTheme.colorScheme.primaryContainer else Color.Transparent) {
                            Column(Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Row {
                                    Text(subjectById[id]?.name ?: "Matéria", Modifier.weight(1f), style = MaterialTheme.typography.bodyMedium, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                    Text("$count", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.error)
                                }
                                Box(Modifier.fillMaxWidth().height(6.dp).clip(CircleShape).background(MaterialTheme.colorScheme.surfaceVariant)) {
                                    Box(Modifier.fillMaxWidth(count.toFloat() / max).fillMaxHeight().clip(CircleShape).background(MaterialTheme.colorScheme.error.copy(alpha = 0.75f)))
                                }
                            }
                        }
                    }
                }
            }
        }

        item {
            Row(Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(status == null, { status = null }, label = { Text("Todos (${errors.size})") })
                ErrorStatus.entries.forEach { value ->
                    val count = errors.count { it.entry.status == value }
                    if (count > 0) FilterChip(status == value, { status = value }, label = { Text("${statusLabel(value)} ($count)") })
                }
            }
            if (subjectFilter != null) AssistChip(
                onClick = { subjectFilter = null },
                label = { Text("Matéria: ${subjectById[subjectFilter]?.name ?: ""} ✕", maxLines = 1, overflow = TextOverflow.Ellipsis) },
                modifier = Modifier.padding(top = 6.dp),
            )
        }

        if (filtered.isEmpty()) item { EmptyState("Nada neste filtro", "Troque o filtro para ver os outros erros.") }
        items(filtered, key = { it.entry.id }) { item ->
            val topic = topicById[item.question.topicId]
            ErrorCard(
                item = item,
                path = listOfNotNull(topic?.subjectId?.let(subjectById::get)?.name, topic?.title).joinToString(" › "),
                now = agora,
                onOpenTopic = { onOpenTopic(item.question.topicId) },
                onEdit = { editing = item.entry },
                onDelete = { deleting = item.entry },
            )
        }

        if (concepts.isNotEmpty()) {
            item {
                Column(Modifier.padding(top = 8.dp)) {
                    Text("Conceitos que mais derrubam você", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Text("As ideias por trás dos erros. Marque com a estrela as que quer rever antes da prova.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            items(concepts, key = { "concept-${it.id}" }) { concept ->
                Surface(shape = EstudarioShapes.row, color = MaterialTheme.colorScheme.surfaceContainerLow, modifier = Modifier.fillMaxWidth()) {
                    Row(Modifier.padding(start = 16.dp, top = 12.dp, bottom = 12.dp, end = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                        Column(Modifier.weight(1f)) {
                            Text(concept.title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                            if (concept.summary.isNotBlank()) Text(concept.summary, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        IconButton(onClick = { viewModel.saveErrorConcept(concept.copy(isFavorite = !concept.isFavorite)) }) {
                            Icon(if (concept.isFavorite) Icons.Outlined.Star else Icons.Outlined.StarBorder, "Favoritar", tint = if (concept.isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SummaryLine(value: String, label: String) {
    Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Text(label, style = MaterialTheme.typography.labelSmall)
    }
}

private fun statusLabel(value: ErrorStatus) = when (value) {
    ErrorStatus.NOVO -> "Novos"
    ErrorStatus.REVISANDO -> "Revisando"
    ErrorStatus.CORRIGIDO -> "Corrigidos"
    ErrorStatus.RECORRENTE -> "Recorrentes"
}

@Composable
private fun statusColor(value: ErrorStatus): Color = when (value) {
    ErrorStatus.NOVO -> MaterialTheme.colorScheme.error
    ErrorStatus.REVISANDO -> MaterialTheme.colorScheme.primary
    ErrorStatus.CORRIGIDO -> estudarioColors().completed
    ErrorStatus.RECORRENTE -> estudarioColors().attention
}

private val shortDate = SimpleDateFormat("dd/MM", Locale("pt", "BR"))

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ErrorCard(
    item: br.com.estudario.data.local.ErrorWithQuestion,
    path: String,
    now: Long,
    onOpenTopic: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
) {
    val entry = item.entry
    var expanded by remember { mutableStateOf(false) }
    var menu by remember { mutableStateOf(false) }
    val accent = statusColor(entry.status)
    val statusName = when (entry.status) { ErrorStatus.NOVO -> "Novo"; ErrorStatus.REVISANDO -> "Revisando"; ErrorStatus.CORRIGIDO -> "Corrigido"; ErrorStatus.RECORRENTE -> "Recorrente" }
    Surface(shape = EstudarioShapes.panel, color = MaterialTheme.colorScheme.surfaceContainerLow, modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(shape = CircleShape, color = accent.copy(alpha = 0.15f)) {
                    Text(statusName, Modifier.padding(horizontal = 10.dp, vertical = 3.dp), style = MaterialTheme.typography.labelMedium, color = accent, fontWeight = FontWeight.Bold)
                }
                Spacer(Modifier.width(8.dp))
                val retry = entry.nextRetryAt
                Text(
                    when {
                        retry == null -> "Errou em ${shortDate.format(Date(entry.lastErrorAt))}"
                        retry <= now -> "Refazer hoje"
                        else -> "Volta em ${shortDate.format(Date(retry))}"
                    },
                    Modifier.weight(1f),
                    style = MaterialTheme.typography.labelMedium,
                    color = if (retry != null && retry <= now) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = if (retry != null && retry <= now) FontWeight.Bold else FontWeight.Normal,
                )
                Box {
                    IconButton(onClick = { menu = true }, modifier = Modifier.size(36.dp)) { Icon(Icons.Outlined.MoreVert, "Opções") }
                    DropdownMenu(expanded = menu, onDismissRequest = { menu = false }) {
                        DropdownMenuItem(text = { Text("Abrir o tópico") }, leadingIcon = { Icon(Icons.Outlined.OpenInNew, null) }, onClick = { menu = false; onOpenTopic() })
                        DropdownMenuItem(text = { Text("Anotar por que errei") }, leadingIcon = { Icon(Icons.Outlined.EditNote, null) }, onClick = { menu = false; onEdit() })
                        DropdownMenuItem(text = { Text("Tirar do caderno", color = MaterialTheme.colorScheme.error) }, leadingIcon = { Icon(Icons.Outlined.Delete, null, tint = MaterialTheme.colorScheme.error) }, onClick = { menu = false; onDelete() })
                    }
                }
            }
            if (path.isNotBlank()) Text(path, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(br.com.estudario.ui.components.plainFormulaText(item.question.statement), style = MaterialTheme.typography.bodyMedium, maxLines = if (expanded) Int.MAX_VALUE else 4, overflow = TextOverflow.Ellipsis)

            // Sua resposta ao lado da correta: o erro inteiro em uma olhada.
            if (entry.selectedAnswer != null || entry.correctAnswer != null) FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                entry.selectedAnswer?.let { AnswerPill("Você marcou", it, MaterialTheme.colorScheme.error) }
                entry.correctAnswer?.let { AnswerPill("Correta", it, estudarioColors().completed) }
            }
            Text(
                "Errou ${entry.errorCount}× · acertou ao refazer ${entry.retryCorrectCount}×" + if (entry.concept.isNotBlank()) " · ${entry.concept}" else "",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            if (entry.comment.isNotBlank()) Surface(shape = EstudarioShapes.compact, color = MaterialTheme.colorScheme.secondaryContainer) {
                Row(Modifier.fillMaxWidth().padding(10.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(Icons.Outlined.EditNote, null, Modifier.size(18.dp))
                    Text(entry.comment, style = MaterialTheme.typography.bodySmall)
                }
            }
            AnimatedVisibility(expanded && item.question.explanation.isNotBlank()) {
                Surface(shape = EstudarioShapes.compact, color = MaterialTheme.colorScheme.surface) {
                    Column(Modifier.fillMaxWidth().padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text("RESOLUÇÃO", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        MarkdownText(item.question.explanation)
                    }
                }
            }
            if (item.question.explanation.isNotBlank()) TextButton(onClick = { expanded = !expanded }, contentPadding = PaddingValues(horizontal = 0.dp)) {
                Icon(if (expanded) Icons.Outlined.ExpandLess else Icons.Outlined.ExpandMore, null, Modifier.size(18.dp)); Spacer(Modifier.width(4.dp))
                Text(if (expanded) "Esconder resolução" else "Ver resolução")
            }
        }
    }
}

@Composable
private fun AnswerPill(label: String, value: String, color: Color) {
    Surface(shape = CircleShape, color = color.copy(alpha = 0.14f)) {
        Row(Modifier.padding(horizontal = 12.dp, vertical = 5.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(label, style = MaterialTheme.typography.labelMedium, color = color)
            Text(value, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold, color = color)
        }
    }
}

@Composable
private fun ErrorNoteDialog(initialComment: String, initialConcept: String, onDismiss: () -> Unit, onSave: (String, String) -> Unit) {
    var comment by remember { mutableStateOf(initialComment) }
    var concept by remember { mutableStateOf(initialConcept) }
    AlertDialog(onDismissRequest = onDismiss, title = { Text("Por que você errou?") }, text = {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text("Escrever o motivo com suas palavras é o que faz o erro não se repetir.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            OutlinedTextField(concept, { concept = it }, label = { Text("Conceito (ex.: crase antes de masculino)") }, singleLine = true)
            OutlinedTextField(comment, { comment = it }, label = { Text("O que eu confundi") }, minLines = 3)
        }
    }, confirmButton = { TextButton(onClick = { onSave(comment, concept); onDismiss() }) { Text("Salvar") } }, dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } })
}
