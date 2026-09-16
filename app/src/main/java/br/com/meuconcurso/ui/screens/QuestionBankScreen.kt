package br.com.meuconcurso.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.meuconcurso.ui.AppViewModel
import br.com.meuconcurso.data.local.QuestionSourceType
import br.com.meuconcurso.data.local.Difficulty
import br.com.meuconcurso.ui.components.EmptyState

@Composable
fun QuestionBankScreen(viewModel: AppViewModel, onBack: () -> Unit, onStart: (QuizConfig) -> Unit) {
    val questions by viewModel.questions.collectAsState()
    val topics by viewModel.topics.collectAsState()
    val subjects by viewModel.subjects.collectAsState()
    var query by remember { mutableStateOf("") }
    var subjectId by remember { mutableStateOf<Long?>(null) }
    var favorites by remember { mutableStateOf(false) }
    var status by remember { mutableStateOf("all") }
    var sourceType by remember { mutableStateOf<QuestionSourceType?>(null) }
    var board by remember { mutableStateOf<String?>(null) }
    var agency by remember { mutableStateOf<String?>(null) }
    var year by remember { mutableStateOf<Int?>(null) }
    var topicId by remember { mutableStateOf<Long?>(null) }
    var difficulty by remember { mutableStateOf<Difficulty?>(null) }
    val filtered = questions.asSequence().filter { row ->
        val topic = topics.firstOrNull { it.id == row.question.topicId }
        (subjectId == null || topic?.subjectId == subjectId) &&
            (topicId == null || row.question.topicId == topicId) &&
            (sourceType == null || row.question.questionSourceType == sourceType) &&
            (board == null || row.question.board == board) &&
            (agency == null || row.question.agency == agency) &&
            (year == null || row.question.year == year) &&
            (difficulty == null || row.question.difficulty == difficulty) &&
            (!favorites || row.question.isFavorite) &&
            (query.isBlank() || row.question.statement.contains(query, true) || row.question.tagsText.contains(query, true)) &&
            when (status) { "new" -> row.question.answerCount == 0; "errors" -> row.question.errorCount > 0; "correct" -> row.question.correctCount > 0; else -> true }
    }.take(250).toList()

    LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        item { Row(verticalAlignment = Alignment.CenterVertically) { IconButton(onClick = onBack) { Icon(Icons.Outlined.ArrowBack, "Voltar") }; Column { Text("Banco de questões", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold); Text("${questions.size} questões locais") } } }
        item { OutlinedTextField(query, { query = it }, Modifier.fillMaxWidth(), leadingIcon = { Icon(Icons.Outlined.Search, null) }, label = { Text("Buscar enunciado ou tag") }, singleLine = true) }
        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                item { FilterChip(subjectId == null, { subjectId = null }, { Text("Todas as matérias") }) }
                items(subjects, key = { it.id }) { subject -> FilterChip(subjectId == subject.id, { subjectId = subject.id }, { Text(subject.name) }) }
            }
        }
        item { FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) { listOf("all" to "Todas", "new" to "Novas", "errors" to "Com erros", "correct" to "Já acertadas").forEach { (key, label) -> FilterChip(status == key, { status = key }, { Text(label) }) }; FilterChip(favorites, { favorites = !favorites }, { Text("Favoritas") }, leadingIcon = { Icon(Icons.Outlined.Favorite, null) }) } }
        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                item { FilterChip(sourceType == null, { sourceType = null }, { Text("Todas as origens") }) }
                item { FilterChip(sourceType == QuestionSourceType.REAL, { sourceType = QuestionSourceType.REAL }, { Text("Reais") }) }
                item { FilterChip(sourceType == QuestionSourceType.REAL_ADAPTED, { sourceType = QuestionSourceType.REAL_ADAPTED }, { Text("Reais adaptadas") }) }
                item { FilterChip(sourceType == QuestionSourceType.AUTHORIAL, { sourceType = QuestionSourceType.AUTHORIAL }, { Text("Autorais") }) }
            }
        }
        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                item { FilterChip(board == null, { board = null }, { Text("Todas as bancas") }) }
                items(questions.mapNotNull { it.question.board }.distinct().sorted()) { value -> FilterChip(board == value, { board = value }, { Text(value) }) }
            }
        }
        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                item { FilterChip(agency == null, { agency = null }, { Text("Todos os órgãos") }) }
                items(questions.mapNotNull { it.question.agency }.distinct().sorted()) { value -> FilterChip(agency == value, { agency = value }, { Text(value) }) }
                item { FilterChip(year == null, { year = null }, { Text("Todos os anos") }) }
                items(questions.mapNotNull { it.question.year }.distinct().sortedDescending()) { value -> FilterChip(year == value, { year = value }, { Text(value.toString()) }) }
                item { FilterChip(difficulty == null, { difficulty = null }, { Text("Todas as dificuldades") }) }
                items(Difficulty.entries) { value -> FilterChip(difficulty == value, { difficulty = value }, { Text(value.name.lowercase().replaceFirstChar(Char::uppercase)) }) }
            }
        }
        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                item { FilterChip(topicId == null, { topicId = null }, { Text("Todos os tópicos") }) }
                items(topics.filter { subjectId == null || it.subjectId == subjectId }, key = { it.id }) { value -> FilterChip(topicId == value.id, { topicId = value.id }, { Text(value.title) }) }
            }
        }
        item { Button(onClick = { onStart(QuizConfig(filtered.size.coerceAtMost(30), subjectId = subjectId, mode = when { favorites -> "favorites"; status == "new" -> "new"; status == "errors" -> "errors"; else -> "random" })) }, enabled = filtered.isNotEmpty(), modifier = Modifier.fillMaxWidth()) { Text("Treinar esta seleção") } }
        if (filtered.isEmpty()) item { EmptyState("Nenhuma questão", "Ajuste os filtros ou importe um pacote com questões.") }
        items(filtered, key = { it.question.id }) { row ->
            val topic = topics.firstOrNull { it.id == row.question.topicId }
            ElevatedCard {
                Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) { Text(topic?.title ?: "Tópico", Modifier.weight(1f), style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary); IconButton(onClick = { viewModel.toggleQuestionFavorite(row.question) }) { Icon(if (row.question.isFavorite) Icons.Outlined.Favorite else Icons.Outlined.FavoriteBorder, "Favoritar") } }
                    Text(row.question.statement, maxLines = 4)
                    Text(listOfNotNull(row.question.board, row.question.agency, row.question.year?.toString(), row.question.difficulty?.name).joinToString(" • ") + " • ${row.question.answerCount} resposta(s)", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(when (row.question.questionSourceType) { QuestionSourceType.REAL -> "Questão real"; QuestionSourceType.REAL_ADAPTED -> "Questão real adaptada/parafraseada"; QuestionSourceType.AUTHORIAL -> "Questão autoral" } + (row.question.sourceId?.let { " • Origem: $it" } ?: ""), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
                }
            }
        }
        if (filtered.size == 250) item { Text("Exibindo os primeiros 250 resultados. Refine a busca para localizar outros itens.", style = MaterialTheme.typography.bodySmall) }
    }
}
