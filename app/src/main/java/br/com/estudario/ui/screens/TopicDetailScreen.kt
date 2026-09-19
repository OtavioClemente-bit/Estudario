package br.com.estudario.ui.screens

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import br.com.estudario.data.local.SummaryEntity
import br.com.estudario.data.local.SummaryKind
import br.com.estudario.data.local.SnippetKind
import br.com.estudario.data.local.ErrorStatus
import br.com.estudario.ui.prompt.ContentPromptBuilderDialog
import br.com.estudario.domain.MasteryCalculator
import br.com.estudario.domain.MasteryInput
import br.com.estudario.domain.ReviewPolicy
import br.com.estudario.domain.ComputedReviewStatus
import br.com.estudario.ui.AppViewModel
import br.com.estudario.ui.components.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun TopicDetailScreen(viewModel: AppViewModel, topicId: Long, onBack: () -> Unit, onQuiz: () -> Unit, onTheory: (Long) -> Unit, onFocus: () -> Unit) {
    val topics by viewModel.topics.collectAsState()
    val subjects by viewModel.subjects.collectAsState()
    val summaries by viewModel.summaries.collectAsState()
    val snippets by viewModel.snippets.collectAsState()
    val theories by viewModel.theories.collectAsState()
    val theoryMarks by viewModel.theoryMarks.collectAsState()
    val questions by viewModel.questions.collectAsState()
    val reviews by viewModel.reviews.collectAsState()
    val attempts by viewModel.attempts.collectAsState()
    val errors by viewModel.errors.collectAsState()
    val errorConcepts by viewModel.errorConcepts.collectAsState()
    val studySessions by viewModel.studySessions.collectAsState()
    val reviewHistory by viewModel.reviewHistory.collectAsState()
    val queue by viewModel.queue.collectAsState()
    val topic = topics.firstOrNull { it.id == topicId }
    if (topic == null) { EmptyState("Tópico não encontrado", "Ele pode ter sido excluído.", "Voltar", onBack); return }
    val subject = subjects.firstOrNull { it.id == topic.subjectId }
    val parent = topics.firstOrNull { it.id == topic.parentTopicId }
    val topicQuestions = questions.filter { it.question.topicId == topicId }
    val answered = topicQuestions.sumOf { it.question.answerCount }
    val correct = topicQuestions.sumOf { it.question.correctCount }
    val completedReviews = reviews.count { it.topicId == topicId && it.completedAt != null }
    val questionIds = topicQuestions.map { it.question.id }.toSet()
    val recentAttempts = attempts.filter { it.questionId in questionIds }.sortedByDescending { it.answeredAt }.take(20)
    val topicErrors = errors.filter { it.entry.questionId in questionIds }
    val now = System.currentTimeMillis()
    val lastContact = listOfNotNull(topic.lastStudiedAt, topic.lastReviewedAt, recentAttempts.maxOfOrNull { it.answeredAt }).maxOrNull() ?: now
    val mastery = MasteryCalculator.percent(MasteryInput(
        topic.status, answered, correct, recentAttempts.count { !it.correct }, completedReviews,
        recentAttempts.size, recentAttempts.count { it.correct }, topicErrors.count { it.entry.status == ErrorStatus.RECORRENTE },
        reviews.count { it.topicId == topicId && ReviewPolicy.status(it, now) == ComputedReviewStatus.ATRASADA }, ((now - lastContact) / 86_400_000L).toInt(),
    ))
    var editor by remember { mutableStateOf<SummaryEntity?>(null) }
    var creating by remember { mutableStateOf(false) }
    var expandedSummary by remember { mutableStateOf<SummaryEntity?>(null) }
    val studyStartedAt = remember(topicId) { System.currentTimeMillis() }
    var finishStudy by remember { mutableStateOf(false) }
    var desmarcar by remember { mutableStateOf(false) }
    val sources by viewModel.sources.collectAsState()
    var completedNow by remember { mutableStateOf(false) }
    var studyNotes by remember { mutableStateOf("") }
    var selectedTab by remember { mutableIntStateOf(0) }
    val queueItem = queue.firstOrNull { it.item.topicId == topicId }
    val sessionAttempts = attempts.filter { it.questionId in questionIds && it.answeredAt >= studyStartedAt }
    var showContentPrompt by remember { mutableStateOf(false) }
    val competitions by viewModel.competitions.collectAsState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val importLauncher = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        uri?.let {
            viewModel.beginIncomingFile()
            scope.launch {
                val text = readContentFile(context, it)
                if (text.isNullOrBlank()) viewModel.reportIncomingFileError("Não foi possível ler o arquivo escolhido.") else viewModel.openIncomingText(text)
            }
        }
    }

    if (showContentPrompt && subject != null) {
        ContentPromptBuilderDialog(viewModel, subject.id, setOf(topic.id), onDismiss = { showContentPrompt = false }, onPickFile = { importLauncher.launch(arrayOf("*/*")) })
    }
    if (creating) SummaryEditorDialog(null, onDismiss = { creating = false }) { title, markdown -> viewModel.addSummary(topicId, title, markdown) }
    editor?.let { value -> SummaryEditorDialog(value, onDismiss = { editor = null }) { title, markdown -> viewModel.updateSummary(value.copy(title = title, markdown = markdown)) } }
    expandedSummary?.let { summary ->
        AlertDialog(
            onDismissRequest = { expandedSummary = null },
            title = { Text(summary.title) },
            text = { Box(Modifier.heightIn(max = 520.dp)) { LazyColumn { item { MarkdownText(summary.markdown) } } } },
            confirmButton = { TextButton(onClick = { expandedSummary = null }) { Text("Fechar") } },
        )
    }
    if (finishStudy) AlertDialog(
        onDismissRequest = { finishStudy = false },
        title = { Text("Estudo concluído hoje") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Matéria: ${subject?.name ?: "—"}")
                Text("Tópico: ${topic.title}")
                Text("Questões: ${sessionAttempts.size} • Acertos: ${sessionAttempts.count { it.correct }} • Erros: ${sessionAttempts.count { !it.correct }}")
                Text("Tempo: ${((System.currentTimeMillis() - studyStartedAt) / 60_000).coerceAtLeast(1)} min")
                OutlinedTextField(studyNotes, { studyNotes = it }, label = { Text("Observações (opcional)") }, minLines = 2)
                Text("Confirmar conclusão?", fontWeight = FontWeight.Bold)
                br.com.estudario.ui.components.XpLine(
                    br.com.estudario.domain.ProgressEngine.previewTopicStudied(),
                    prefix = "Ao concluir você ganha",
                )
                Text("As revisões D+1, D+7 e D+30 também entram na agenda — e continuam depois disso, com intervalo maior. Cada uma vale mais XP.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        },
        confirmButton = { TextButton(onClick = { viewModel.completeStudy(topicId, studyStartedAt, studyNotes); finishStudy = false; completedNow = true }) { Text("Confirmar") } },
        dismissButton = { TextButton(onClick = { finishStudy = false }) { Text("Cancelar") } },
    )

    if (desmarcar) ConfirmDialog(
        "Desmarcar como estudado?",
        "O tópico volta para \"não estudado\" e as revisões ainda não feitas dele são canceladas. Sessões de estudo e revisões já concluídas continuam no seu histórico.",
        "Desmarcar",
        onDismiss = { desmarcar = false },
    ) { viewModel.unmarkStudied(topic); desmarcar = false }

    val topicSources = sources.filter { it.topicId == topicId }
    val uriHandler = androidx.compose.ui.platform.LocalUriHandler.current

    LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        item {
            Row {
                IconButton(onClick = onBack) { Icon(Icons.Outlined.ArrowBack, "Voltar") }
                Column(Modifier.weight(1f)) {
                    Text(topic.title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                    Text(listOfNotNull(subject?.name, parent?.title).joinToString(" › "), color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(topic.contentOriginType.displayName(), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
                }
                IconButton(onClick = { showContentPrompt = true }) { Icon(Icons.Outlined.AutoAwesome, "Gerar conteúdo com IA", tint = MaterialTheme.colorScheme.primary) }
                IconButton(onClick = { importLauncher.launch(arrayOf("*/*")) }) { Icon(Icons.Outlined.FileOpen, "Importar arquivo .estudo") }
            }
        }
        item {
            ElevatedCard(colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
                Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Domínio ${MasteryCalculator.label(mastery)}", fontWeight = FontWeight.Bold)
                        Text("$mastery%", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    }
                    LinearProgressIndicator({ mastery / 100f }, Modifier.fillMaxWidth())
                    val coverage = listOf(topic.status != br.com.estudario.data.local.TopicStatus.NAO_ESTUDADO, theories.any { it.topicId == topicId && it.lastReadBlock >= 0 }, answered > 0, completedReviews > 0).count { it } * 25
                    Text("Cobertura $coverage% • Questões: ${if (answered == 0) "amostra insuficiente" else "${correct * 100 / answered}% ($answered)"} • Revisões: $completedReviews", style = MaterialTheme.typography.bodySmall)
                    // Estudar de verdade este tópico: cronômetro rodando e Não Perturbe ligado.
                    FilledTonalButton(
                        onClick = { viewModel.startFocus(topic.title, topicId = topicId); onFocus() },
                        modifier = Modifier.fillMaxWidth(),
                    ) { Text("Estudar com modo foco") }
                    if (topic.status != br.com.estudario.data.local.TopicStatus.NAO_ESTUDADO) {
                        // Dá para voltar atrás: desmarcar cancela as revisões pendentes e devolve o
                        // tópico para "não estudado", sem apagar sessões nem revisões já feitas.
                        TextButton(onClick = { desmarcar = true }, contentPadding = PaddingValues(0.dp)) { Text("Desmarcar como estudado") }
                    }
                }
            }
        }
        if (!topic.scopeCovers.isNullOrBlank() || !topic.scopeExcludes.isNullOrBlank()) {
            item {
                // O recorte que a IA declarou antes de escrever. Fica visível para a pessoa bater
                // com o edital dela — é a defesa contra estudar o que não vai cair.
                ElevatedCard(Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Recorte deste item do edital", fontWeight = FontWeight.Bold)
                        topic.scopeCovers?.takeIf { it.isNotBlank() }?.let { cobre ->
                            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                Text("COBRE", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.secondary)
                                Text(cobre, style = MaterialTheme.typography.bodySmall)
                            }
                        }
                        topic.scopeExcludes?.takeIf { it.isNotBlank() }?.let { fora ->
                            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                Text("FICA DE FORA", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.error)
                                Text(fora, style = MaterialTheme.typography.bodySmall)
                            }
                        }
                        Text(
                            "Confira com o edital na mão. Se não bater, gere o conteúdo de novo pelo botão ✨.",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }
        }
        if (topicSources.isNotEmpty()) {
            item {
                // De onde a IA tirou o conteúdo deste tópico. Fica aqui, ao lado do material, para a
                // conferência ser possível no momento em que a dúvida aparece.
                var abertas by remember { mutableStateOf(false) }
                ElevatedCard(Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(Modifier.fillMaxWidth().clickable { abertas = !abertas }, verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                            Column(Modifier.weight(1f)) {
                                Text("Fontes deste conteúdo", fontWeight = FontWeight.Bold)
                                Text(
                                    "${topicSources.count { it.kind == br.com.estudario.data.local.SourceKind.OFICIAL }} oficial(is) • ${topicSources.count { it.kind == br.com.estudario.data.local.SourceKind.COMPLEMENTAR }} complementar(es)",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                            }
                            Icon(if (abertas) Icons.Outlined.ExpandLess else Icons.Outlined.ExpandMore, if (abertas) "Recolher" else "Ver fontes")
                        }
                        if (abertas) topicSources.forEach { fonte ->
                            SourceCard(
                                fonte = fonte,
                                topicTitle = null,
                                onOpenTopic = {},
                                onOpenUrl = { url -> runCatching { uriHandler.openUri(url) } },
                                showTopic = false,
                            )
                        }
                    }
                }
            }
        }
        item {
            val unreadTheory = theories.filter { it.topicId == topicId }.sortedByDescending { it.updatedAt }.firstOrNull { it.lastReadBlock < 0 }
            Button(
                onClick = { if (unreadTheory != null) onTheory(unreadTheory.id) else if (topicQuestions.isNotEmpty()) onQuiz() else finishStudy = true },
                modifier = Modifier.fillMaxWidth(),
            ) { Icon(Icons.Outlined.PlayArrow, null); Spacer(Modifier.width(6.dp)); Text("Continuar de onde parei") }
        }
        if (completedNow) item {
            ElevatedCard(colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)) {
                Text("ESTUDO CONCLUÍDO ✓\nO próximo item da fila não será iniciado automaticamente.", Modifier.padding(16.dp), fontWeight = FontWeight.Bold)
            }
        }
        item {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { finishStudy = true }, Modifier.weight(1f)) { Icon(Icons.Outlined.Check, null); Spacer(Modifier.width(4.dp)); Text("Estudo concluído hoje") }
                OutlinedButton(onClick = { if (queueItem == null) viewModel.enqueue(topic.id) }, enabled = queueItem == null, modifier = Modifier.weight(1f)) { Text(if (queueItem == null) "Adicionar à fila" else "Na fila") }
            }
        }
        item {
            ScrollableTabRow(selectedTabIndex = selectedTab, edgePadding = 0.dp) {
                listOf("TEORIA", "REVISÃO", "BIZUS", "QUESTÕES", "ERROS", "HISTÓRICO").forEachIndexed { index, label ->
                    Tab(selected = selectedTab == index, onClick = { selectedTab = index }, text = { Text(label) })
                }
            }
        }
        if (selectedTab == 0) {
        item {
            Text("Teoria completa", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        }
        val topicTheories = theories.filter { it.topicId == topicId }
        if (topicTheories.isEmpty()) item { EmptyState("Teoria ainda não importada", "Toque em ✨ no topo para montar o pedido para a IA e depois importe o .estudo gerado.", "Gerar com IA") { showContentPrompt = true } }
        topicTheories.forEach { theory ->
            item(key = "theory-${theory.id}") {
                ElevatedCard(onClick = { onTheory(theory.id) }) {
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(Modifier.fillMaxWidth(), verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                            Icon(Icons.Outlined.MenuBook, null, tint = MaterialTheme.colorScheme.primary)
                            Spacer(Modifier.width(10.dp))
                            Text(theory.title, Modifier.weight(1f), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        }
                        val blockCount = theory.markdown.split(Regex("\\n\\s*\\n")).count { it.isNotBlank() }.coerceAtLeast(1)
                        val readPercent = if (theory.lastReadBlock < 0) 0 else ((theory.lastReadBlock + 1) * 100 / blockCount).coerceIn(0, 100)
                        LinearProgressIndicator({ readPercent / 100f }, Modifier.fillMaxWidth())
                        Text("$readPercent% lido • ${theoryMarks.count { it.theoryId == theory.id }} marcação(ões)", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Button(onClick = { onTheory(theory.id) }, Modifier.fillMaxWidth()) { Text(if (theory.lastReadBlock > 0) "Continuar leitura" else "Começar leitura") }
                    }
                }
            }
        }
        }
        if (selectedTab == 1) {
        item {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Resumo completo e rápido", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                TextButton(onClick = { creating = true }) { Icon(Icons.Outlined.Add, null); Text("Novo") }
            }
        }
        val topicSummaries = summaries.filter { it.topicId == topicId }
        if (topicSummaries.isEmpty()) item { EmptyState("Sem resumos", "Adicione Markdown ou importe um pacote .estudo.") }
        topicSummaries.forEach { summary ->
            item(key = "summary-${summary.id}") {
                ElevatedCard(onClick = { expandedSummary = summary }) {
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(Modifier.fillMaxWidth()) {
                            Column(Modifier.weight(1f)) { Text(summary.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold); Text(if (summary.kind == SummaryKind.RAPIDO) "REVISÃO RÁPIDA" else "RESUMO COMPLETO", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary) }
                            IconButton(onClick = { editor = summary }) { Icon(Icons.Outlined.Edit, "Editar") }
                            IconButton(onClick = { viewModel.deleteSummary(summary) }) { Icon(Icons.Outlined.Delete, "Excluir") }
                        }
                        Text(summary.markdown.replace("#", "").replace("**", "").take(160), maxLines = 3, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }
        }
        val topicSnippets = snippets.filter { it.topicId == topicId }
        if (selectedTab == 1 || selectedTab == 2) {
        val visibleSnippets = topicSnippets.filter { if (selectedTab == 1) it.kind == SnippetKind.RECUPERACAO else it.kind != SnippetKind.RECUPERACAO }
        if (visibleSnippets.isNotEmpty()) item { Text(if (selectedTab == 1) "Memorização ativa" else "Bizus e pegadinhas", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold) }
        visibleSnippets.groupBy { it.kind }.forEach { (kind, values) ->
            item(key = "snippet-title-$kind") { Text(when (kind) { SnippetKind.BIZU -> "Bizus"; SnippetKind.PEGADINHA -> "Pegadinhas"; SnippetKind.RECUPERACAO -> "Perguntas para lembrar" }, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold) }
            values.forEach { snippet ->
                item(key = "snippet-${snippet.id}") {
                    ElevatedCard(colors = CardDefaults.elevatedCardColors(containerColor = when (kind) { SnippetKind.PEGADINHA -> MaterialTheme.colorScheme.errorContainer; SnippetKind.BIZU -> MaterialTheme.colorScheme.tertiaryContainer; else -> MaterialTheme.colorScheme.secondaryContainer })) {
                        Row(Modifier.padding(14.dp), verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                            Text(snippet.text, Modifier.weight(1f))
                            IconButton(onClick = { viewModel.saveSnippet(snippet.copy(isFavorite = !snippet.isFavorite)) }) { Icon(if (snippet.isFavorite) Icons.Outlined.Star else Icons.Outlined.StarBorder, "Favoritar") }
                        }
                    }
                }
            }
        }
        }
        if (selectedTab == 3) {
        item {
            Text("Questões", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            ElevatedCard {
                Row(Modifier.padding(16.dp), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Column(Modifier.weight(1f)) {
                        Text("${topicQuestions.size}", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                        Text("${topicQuestions.count { it.question.answerCount == 0 }} nunca respondidas • ${topicQuestions.count { it.question.isFavorite }} favoritas", style = MaterialTheme.typography.bodySmall)
                    }
                    Button(onClick = onQuiz, enabled = topicQuestions.isNotEmpty()) { Text("Treinar tópico") }
                }
            }
        }
        }
        if (selectedTab == 4) {
        item { Text("Erros por conceito", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold) }
        val topicConcepts = errorConcepts.filter { it.topicId == topicId }.sortedWith(compareByDescending<br.com.estudario.data.local.ErrorConceptEntity> { it.errorCount }.thenByDescending { it.lastErrorAt ?: 0 })
        if (topicConcepts.isEmpty()) item { EmptyState("Nenhum conceito recorrente", "Os conceitos serão organizados quando houver respostas erradas.") }
        topicConcepts.forEach { concept ->
            item(key = "concept-${concept.id}") {
                ListItem(
                    headlineContent = { Text(concept.title, fontWeight = FontWeight.SemiBold) },
                    supportingContent = { Text("${concept.errorCount} erro(s) • ${if (concept.mastered) "corrigido" else "prioridade ${concept.priority.name.lowercase()}"}${concept.summary.takeIf { it.isNotBlank() }?.let { "\n$it" }.orEmpty()}") },
                    leadingContent = { Icon(Icons.Outlined.ErrorOutline, null, tint = MaterialTheme.colorScheme.error) },
                )
            }
        }
        }
        if (selectedTab == 5) {
        item { Text("Histórico", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold) }
        val history = buildList<Pair<Long, String>> {
            studySessions.filter { it.topicId == topicId }.forEach { add(it.completedAt to "Estudo concluído") }
            reviewHistory.filter { it.topicId == topicId }.forEach { add(it.reviewedAt to "Revisão realizada") }
            recentAttempts.forEach { add(it.answeredAt to if (it.correct) "Questão correta" else "Questão errada") }
        }.sortedByDescending { it.first }.take(30)
        if (history.isEmpty()) item { EmptyState("Sem histórico", "As atividades concluídas aparecerão aqui.") }
        history.forEachIndexed { index, event ->
            item(key = "history-$index-${event.first}") {
                val date = java.time.Instant.ofEpochMilli(event.first).atZone(java.time.ZoneId.systemDefault()).toLocalDate()
                ListItem(headlineContent = { Text(event.second) }, supportingContent = { Text(date.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"))) }, leadingContent = { Icon(Icons.Outlined.History, null) })
            }
        }
        }
    }
}

private suspend fun readContentFile(context: Context, uri: Uri): String? = withContext(Dispatchers.IO) {
    runCatching { context.contentResolver.openInputStream(uri)?.use { it.readBytes().toString(Charsets.UTF_8) } }.getOrNull()
}

@Composable
private fun SummaryEditorDialog(summary: SummaryEntity?, onDismiss: () -> Unit, onSave: (String, String) -> Unit) {
    var title by remember { mutableStateOf(summary?.title.orEmpty()) }
    var markdown by remember { mutableStateOf(summary?.markdown.orEmpty()) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (summary == null) "Novo resumo" else "Editar resumo") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(title, { title = it }, label = { Text("Título") }, singleLine = true)
                OutlinedTextField(markdown, { markdown = it }, label = { Text("Markdown") }, minLines = 8, maxLines = 14)
            }
        },
        confirmButton = { TextButton(enabled = title.isNotBlank() && markdown.isNotBlank(), onClick = { onSave(title, markdown); onDismiss() }) { Text("Salvar") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } },
    )
}
