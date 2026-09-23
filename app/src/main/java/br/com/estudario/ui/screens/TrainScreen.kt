package br.com.estudario.ui.screens

import br.com.estudario.ui.theme.screenPadding
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Bolt
import androidx.compose.material.icons.outlined.HelpOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.estudario.ui.AppViewModel
import br.com.estudario.ui.components.EmptyState
import br.com.estudario.ui.components.ScreenTitle
import br.com.estudario.ui.tour.TourKey
import br.com.estudario.ui.tour.tourTarget
import androidx.compose.foundation.lazy.rememberLazyListState

data class QuizConfig(
    val count: Int,
    val topicId: Long? = null,
    val subjectId: Long? = null,
    val mode: String = "random",
    val board: String? = null,
    val difficulty: String? = null,
    val planTaskId: String? = null,
)

@Composable
fun TrainScreen(viewModel: AppViewModel, onStart: (QuizConfig) -> Unit, onHelp: () -> Unit = {}) {
    val competitions by viewModel.competitions.collectAsState()
    val subjects by viewModel.subjects.collectAsState()
    val topics by viewModel.topics.collectAsState()
    val questions by viewModel.questions.collectAsState()
    val defaultCount by viewModel.defaultQuestionCount.collectAsState()
    val primaryId = competitions.firstOrNull { it.isPrimary }?.id ?: competitions.firstOrNull()?.id
    var subjectId by remember { mutableStateOf<Long?>(null) }
    var topicId by remember { mutableStateOf<Long?>(null) }
    var count by remember(defaultCount) { mutableIntStateOf(defaultCount) }
    var mode by remember { mutableStateOf("smart") }
    var board by remember { mutableStateOf<String?>(null) }
    var difficulty by remember { mutableStateOf<String?>(null) }
    val availableSubjects = subjects.filter { it.competitionId == primaryId }
    val availableTopics = topics.filter { subjectId == null || it.subjectId == subjectId }
    val modes = listOf("smart" to "Treino inteligente", "random" to "Aleatórias", "new" to "Novas", "errors" to "Apenas erradas", "most_errors" to "Mais erradas", "never_correct" to "Nunca acertadas", "favorites" to "Favoritas", "simulation" to "Simulado")
    val boards = questions.mapNotNull { it.question.board }.distinct().sorted()
    val tourStep by viewModel.tourStep.collectAsState()
    val listState = rememberLazyListState()
    LaunchedEffect(tourStep?.key) {
        when (tourStep?.key) {
            TourKey.TRAIN_DAILY -> listState.animateScrollToItem(0)
            TourKey.TRAIN_MODES -> listState.animateScrollToItem(2)
            TourKey.TRAIN_START -> listState.animateScrollToItem((listState.layoutInfo.totalItemsCount - 1).coerceAtLeast(0))
            else -> Unit
        }
    }
    fun target(key: TourKey) = Modifier.tourTarget(key, tourStep?.key) { viewModel.reportTourTargetBounds(key, it) }

    LazyColumn(Modifier.fillMaxSize(), state = listState, contentPadding = screenPadding(), verticalArrangement = Arrangement.spacedBy(18.dp)) {
        item { ScreenTitle("Treinar", "Escolha o foco da sua sessão", stackActionsWhenNarrow = false) { IconButton(onClick = onHelp) { Icon(Icons.Outlined.HelpOutline, "Como treinar") } } }
        if (questions.isEmpty()) {
            item { EmptyState("Sem questões ainda", "No Edital, toque em ✨ numa matéria ou tópico para pedir questões à IA e importe o .estudo gerado.") }
        } else {
            item {
                ElevatedCard(modifier = target(TourKey.TRAIN_DAILY), colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
                    Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Desafio do dia", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                        Text("10 questões escolhidas entre erros recorrentes, revisões atrasadas e tópicos com menor domínio.")
                        Button(onClick = { onStart(QuizConfig(10, mode = "daily")) }, Modifier.fillMaxWidth()) { Text("Começar desafio") }
                    }
                }
            }
            item {
                Text("Atalhos rápidos", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(10.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(listOf(5, 10, 15)) { amount -> AssistChip(onClick = { onStart(QuizConfig(amount)) }, label = { Text("$amount questões") }, leadingIcon = { Icon(Icons.Outlined.Bolt, null) }) }
                }
            }
            item {
                Text("Modo", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                FlowRow(target(TourKey.TRAIN_MODES), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    modes.forEach { (key, label) -> FilterChip(selected = mode == key, onClick = { mode = key }, label = { Text(label) }) }
                }
            }
            item {
                Text("Matéria", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    item { FilterChip(selected = subjectId == null, onClick = { subjectId = null; topicId = null }, label = { Text("Todas") }) }
                    items(availableSubjects, key = { it.id }) { subject -> FilterChip(selected = subjectId == subject.id, onClick = { subjectId = subject.id; topicId = null }, label = { Text(subject.name) }) }
                }
            }
            if (subjectId != null) item {
                Text("Tópico", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    item { FilterChip(selected = topicId == null, onClick = { topicId = null }, label = { Text("Todos") }) }
                    items(availableTopics, key = { it.id }) { topic -> FilterChip(selected = topicId == topic.id, onClick = { topicId = topic.id }, label = { Text(topic.title) }) }
                }
            }
            if (boards.isNotEmpty()) item {
                Text("Banca", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    item { FilterChip(selected = board == null, onClick = { board = null }, label = { Text("Todas") }) }
                    items(boards) { value -> FilterChip(selected = board == value, onClick = { board = value }, label = { Text(value) }) }
                }
            }
            item {
                Text("Dificuldade", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf(null to "Todas", "FACIL" to "Fácil", "MEDIA" to "Média", "DIFICIL" to "Difícil").forEach { (key, label) ->
                        FilterChip(selected = difficulty == key, onClick = { difficulty = key }, label = { Text(label) })
                    }
                }
            }
            item {
                Text("Quantidade: $count", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Slider(value = count.toFloat().coerceIn(5f, 50f), onValueChange = { count = it.toInt() }, valueRange = 5f..50f, steps = 8)
            }
            item {
                Button(onClick = { onStart(QuizConfig(count, topicId, subjectId, mode, board, difficulty)) }, Modifier.fillMaxWidth().heightIn(min = 52.dp).then(target(TourKey.TRAIN_START))) {
                    Text(when (mode) { "simulation" -> "Iniciar simulado"; "smart" -> "Iniciar treino inteligente"; else -> "Começar treino" })
                }
            }
        }
    }
}
