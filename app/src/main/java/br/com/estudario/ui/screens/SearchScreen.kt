package br.com.estudario.ui.screens

import br.com.estudario.ui.theme.screenPadding
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.estudario.ui.AppViewModel
import br.com.estudario.ui.components.EmptyState

@Composable
fun SearchScreen(
    viewModel: AppViewModel,
    onBack: () -> Unit,
    onOpenTopic: (Long) -> Unit,
    onOpenTheory: (Long) -> Unit,
    showInlineBack: Boolean = true,
) {
    val subjects by viewModel.subjects.collectAsState()
    val topics by viewModel.topics.collectAsState()
    val summaries by viewModel.summaries.collectAsState()
    val theories by viewModel.theories.collectAsState()
    val theoryMarks by viewModel.theoryMarks.collectAsState()
    val questions by viewModel.questions.collectAsState()
    var query by remember { mutableStateOf("") }
    val normalized = query.trim()
    val subjectMatches = if (normalized.length < 2) emptyList() else subjects.filter { it.name.contains(normalized, true) }
    val topicMatches = if (normalized.length < 2) emptyList() else topics.filter { it.title.contains(normalized, true) || it.description.contains(normalized, true) || it.notes.contains(normalized, true) }
    val summaryMatches = if (normalized.length < 2) emptyList() else summaries.filter { it.title.contains(normalized, true) || it.markdown.contains(normalized, true) || it.ownNotes.contains(normalized, true) }
    val theoryMatches = if (normalized.length < 2) emptyList() else theories.filter { theory -> theory.title.contains(normalized, true) || theory.markdown.contains(normalized, true) || theoryMarks.any { it.theoryId == theory.id && (it.quote.contains(normalized, true) || it.note.contains(normalized, true)) } }
    val questionMatches = if (normalized.length < 2) emptyList() else questions.filter { it.question.statement.contains(normalized, true) || it.question.explanation.contains(normalized, true) || it.question.tagsText.contains(normalized, true) }
    LazyColumn(Modifier.fillMaxSize(), contentPadding = screenPadding(), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        item { Row(verticalAlignment = Alignment.CenterVertically) { if (showInlineBack) IconButton(onClick = onBack) { Icon(Icons.Outlined.ArrowBack, "Voltar") }; Text("Pesquisa global", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold) } }
        item { OutlinedTextField(query, { query = it }, Modifier.fillMaxWidth(), leadingIcon = { Icon(Icons.Outlined.Search, null) }, label = { Text("Teoria, observação, resumo, questão ou tópico") }, singleLine = true) }
        if (normalized.length < 2) item { EmptyState("Digite para pesquisar", "A busca funciona inteiramente offline.") }
        else if (subjectMatches.isEmpty() && topicMatches.isEmpty() && theoryMatches.isEmpty() && summaryMatches.isEmpty() && questionMatches.isEmpty()) item { EmptyState("Nenhum resultado", "Tente outro termo.") }
        subjectMatches.forEach { subject -> item(key = "s${subject.id}") { ResultCard("Matéria", subject.name, "${topics.count { it.subjectId == subject.id }} tópicos") {} } }
        topicMatches.forEach { topic -> item(key = "t${topic.id}") { ResultCard("Tópico", topic.title, topic.description.ifBlank { topic.status.displayName() }) { onOpenTopic(topic.id) } } }
        theoryMatches.forEach { theory -> item(key = "th${theory.id}") { ResultCard("Teoria", theory.title, theory.markdown.replace("#", "").take(120)) { onOpenTheory(theory.id) } } }
        summaryMatches.forEach { summary -> item(key = "m${summary.id}") { ResultCard("Resumo", summary.title, summary.markdown.take(120)) { onOpenTopic(summary.topicId) } } }
        questionMatches.forEach { question -> item(key = "q${question.question.id}") { ResultCard("Questão", question.question.statement.take(100), question.question.tagsText) { onOpenTopic(question.question.topicId) } } }
    }
}

@Composable private fun ResultCard(type: String, title: String, subtitle: String, onClick: () -> Unit) {
    ElevatedCard(onClick = onClick) { Column(Modifier.padding(14.dp)) { Text(type.uppercase(), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary); Text(title, fontWeight = FontWeight.Bold); if (subtitle.isNotBlank()) Text(subtitle, maxLines = 2, style = MaterialTheme.typography.bodySmall, overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis) } }
}
