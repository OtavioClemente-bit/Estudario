package br.com.meuconcurso.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.meuconcurso.data.local.SummaryEntity
import br.com.meuconcurso.data.local.SummaryKind
import br.com.meuconcurso.data.local.SnippetKind
import br.com.meuconcurso.data.local.ErrorStatus
import br.com.meuconcurso.domain.MasteryCalculator
import br.com.meuconcurso.domain.MasteryInput
import br.com.meuconcurso.domain.ReviewPolicy
import br.com.meuconcurso.domain.ComputedReviewStatus
import br.com.meuconcurso.ui.AppViewModel
import br.com.meuconcurso.ui.components.*

@Composable
fun TopicDetailScreen(viewModel: AppViewModel, topicId: Long, onBack: () -> Unit, onQuiz: () -> Unit, onTheory: (Long) -> Unit) {
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
    var completedNow by remember { mutableStateOf(false) }
    var studyNotes by remember { mutableStateOf("") }
    var selectedTab by remember { mutableIntStateOf(0) }
    val queueItem = queue.firstOrNull { it.item.topicId == topicId }
    val sessionAttempts = attempts.filter { it.questionId in questionIds && it.answeredAt >= studyStartedAt }

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
            }
        },
        confirmButton = { TextButton(onClick = { viewModel.completeStudy(topicId, studyStartedAt, studyNotes); finishStudy = false; completedNow = true }) { Text("Confirmar") } },
        dismissButton = { TextButton(onClick = { finishStudy = false }) { Text("Cancelar") } },
    )

    LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        item {
            Row {
                IconButton(onClick = onBack) { Icon(Icons.Outlined.ArrowBack, "Voltar") }
                Column(Modifier.weight(1f)) {
                    Text(topic.title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                    Text(listOfNotNull(subject?.name, parent?.title).joinToString(" › "), color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(topic.contentOriginType.displayName(), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
                }
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
                    val coverage = listOf(topic.status != br.com.meuconcurso.data.local.TopicStatus.NAO_ESTUDADO, theories.any { it.topicId == topicId && it.lastReadBlock >= 0 }, answered > 0, completedReviews > 0).count { it } * 25
                    Text("Cobertura $coverage% • Questões: ${if (answered == 0) "amostra insuficiente" else "${correct * 100 / answered}% ($answered)"} • Revisões: $completedReviews", style = MaterialTheme.typography.bodySmall)
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
        if (topicTheories.isEmpty()) item { EmptyState("Livro ainda não importado", "Gere um pacote .estudo com teoria completa, resumo e questões.") }
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
        val topicConcepts = errorConcepts.filter { it.topicId == topicId }.sortedWith(compareByDescending<br.com.meuconcurso.data.local.ErrorConceptEntity> { it.errorCount }.thenByDescending { it.lastErrorAt ?: 0 })
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
