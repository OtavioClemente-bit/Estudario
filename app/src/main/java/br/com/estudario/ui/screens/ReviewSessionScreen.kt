package br.com.estudario.ui.screens

import br.com.estudario.ui.theme.estudarioLayout
import br.com.estudario.ui.theme.screenPadding
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.estudario.data.local.QuestionWithOptions
import br.com.estudario.data.local.ReviewDifficulty
import br.com.estudario.data.local.SnippetKind
import br.com.estudario.data.local.SummaryKind
import br.com.estudario.ui.AppViewModel
import br.com.estudario.ui.components.EmptyState
import br.com.estudario.ui.components.MarkdownText
import br.com.estudario.ui.components.QuestionProvenance
import kotlinx.coroutines.launch
import java.util.UUID

@Composable
fun ReviewSessionScreen(viewModel: AppViewModel, reviewId: Long, onBack: () -> Unit) {
    val reviews by viewModel.reviews.collectAsState()
    val topics by viewModel.topics.collectAsState()
    val snippets by viewModel.snippets.collectAsState()
    val summaries by viewModel.summaries.collectAsState()
    val allQuestions by viewModel.questions.collectAsState()
    val review = reviews.firstOrNull { it.id == reviewId }
    if (review == null) { EmptyState("Revisão não encontrada", "Ela pode já ter sido removida.", "Voltar", onBack); return }
    val topic = topics.firstOrNull { it.id == review.topicId }
    val recall = snippets.filter { it.topicId == review.topicId && it.kind == SnippetKind.RECUPERACAO }
    val quick = summaries.firstOrNull { it.topicId == review.topicId && it.kind == SummaryKind.RAPIDO }
        ?: summaries.firstOrNull { it.topicId == review.topicId }
    // A revisão não sorteia às cegas: testa primeiro o que a pessoa já errou nesse tópico, depois o
    // que ela nunca respondeu, e só então o resto. Revisar é reencontrar a falha, não passear.
    val questions = remember(allQuestions, review.topicId) {
        val doTopico = allQuestions.filter { it.question.topicId == review.topicId }
        val erradas = doTopico.filter { it.question.errorCount > 0 }
            .sortedWith(compareByDescending<QuestionWithOptions> { it.question.errorCount }.thenBy { it.question.lastAnsweredAt ?: 0L })
        val nunca = doTopico.filter { it.question.errorCount == 0 && it.question.answerCount == 0 }.shuffled()
        val resto = doTopico.filter { it.question.errorCount == 0 && it.question.answerCount > 0 }.shuffled()
        (erradas + nunca + resto).take(3)
    }
    val scope = rememberCoroutineScope()
    val startedAt = remember { System.currentTimeMillis() }
    val sessionId = remember { "review-${UUID.randomUUID()}" }
    var step by remember { mutableIntStateOf(if (recall.isEmpty()) 1 else 0) }
    var recallIndex by remember { mutableIntStateOf(0) }
    var recalled by remember { mutableIntStateOf(0) }
    var forgotten by remember { mutableIntStateOf(0) }
    var questionIndex by remember { mutableIntStateOf(0) }
    var selected by remember { mutableStateOf<String?>(null) }
    var answered by remember { mutableStateOf<Boolean?>(null) }
    var correct by remember { mutableIntStateOf(0) }

    LazyColumn(Modifier.fillMaxSize(), contentPadding = screenPadding(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) { Icon(Icons.Outlined.ArrowBack, "Sair") }
                Column(Modifier.weight(1f)) {
                    Text("Revisão ativa", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                    Text(topic?.title ?: "Tópico")
                }
                Text("${step + 1}/4", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
            }
            LinearProgressIndicator({ (step + 1) / 4f }, Modifier.fillMaxWidth())
        }
        when (step) {
            0 -> {
                val prompt = recall[recallIndex.coerceAtMost(recall.lastIndex)]
                item {
                    Text("1. Tente lembrar sem consultar", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    ElevatedCard(colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
                        Text(prompt.text, Modifier.padding(20.dp), style = MaterialTheme.typography.titleMedium)
                    }
                    Text("Responda mentalmente e registre como foi. O objetivo é recuperar a informação, não apenas reconhecê-la.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                item {
                    // Lado a lado quando cabe; em tela estreita ou fonte grande, um botão por linha.
                    FlowRow(Modifier.fillMaxWidth(), maxItemsInEachRow = if (estudarioLayout().prefersStacking) 1 else Int.MAX_VALUE, verticalArrangement = Arrangement.spacedBy(8.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        OutlinedButton(onClick = { forgotten++; if (recallIndex < recall.lastIndex) recallIndex++ else step = 1 }, Modifier.weight(1f)) { Text("Não lembrei") }
                        Button(onClick = { recalled++; if (recallIndex < recall.lastIndex) recallIndex++ else step = 1 }, Modifier.weight(1f)) { Text("Lembrei") }
                    }
                }
            }
            1 -> {
                item { Text("2. Confira a revisão rápida", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold) }
                item {
                    ElevatedCard { Column(Modifier.padding(18.dp)) { if (quick == null) Text("Este tópico ainda não possui revisão rápida. Você pode continuar normalmente.") else MarkdownText(quick.markdown) } }
                }
                item { Button(onClick = { step = if (questions.isEmpty()) 3 else 2 }, Modifier.fillMaxWidth()) { Text("Ir para as questões") } }
            }
            2 -> {
                val question = questions[questionIndex.coerceAtMost(questions.lastIndex)]
                item { Text("3. Teste o entendimento", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold); Text("Questão ${questionIndex + 1} de ${questions.size}") }
                item { Column { MarkdownText(question.question.statement); Spacer(Modifier.height(8.dp)); QuestionProvenance(question.question) } }
                question.options.sortedBy { it.position }.forEach { option ->
                    item(key = option.id) {
                        val color = when { answered != null && option.isCorrect -> MaterialTheme.colorScheme.secondaryContainer; answered == false && option.key == selected -> MaterialTheme.colorScheme.errorContainer; option.key == selected -> MaterialTheme.colorScheme.primaryContainer; else -> MaterialTheme.colorScheme.surface }
                        Surface(onClick = { if (answered == null) selected = option.key }, color = color, shape = RoundedCornerShape(12.dp), border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant), modifier = Modifier.fillMaxWidth()) {
                            Text("${option.key}) ${option.text}", Modifier.padding(14.dp))
                        }
                    }
                }
                if (answered != null) item { ElevatedCard { Column(Modifier.padding(16.dp)) { Text(if (answered == true) "Correto" else "Incorreto", fontWeight = FontWeight.Bold); MarkdownText(question.question.explanation) } } }
                item {
                    if (answered == null) Button(enabled = selected != null, onClick = { scope.launch { answered = viewModel.answer(question, selected!!, sessionId); if (answered == true) correct++ } }, modifier = Modifier.fillMaxWidth()) { Text("Confirmar") }
                    else Button(onClick = { if (questionIndex < questions.lastIndex) { questionIndex++; selected = null; answered = null } else step = 3 }, Modifier.fillMaxWidth()) { Text(if (questionIndex < questions.lastIndex) "Próxima" else "Avaliar revisão") }
                }
            }
            else -> {
                item {
                    Text("4. Como foi esta revisão?", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Text("Lembrei: $recalled • Esqueci: $forgotten • Questões: $correct/${questions.size}", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        DifficultyButton("Fácil", "Recuperei quase tudo com segurança", ReviewDifficulty.FACIL, review, viewModel, correct, questions.size, recalled, forgotten, startedAt, onBack)
                        DifficultyButton("Normal", "Lembrei depois de pensar ou consultar", ReviewDifficulty.NORMAL, review, viewModel, correct, questions.size, recalled, forgotten, startedAt, onBack)
                        DifficultyButton("Difícil", "Ainda confundo pontos importantes", ReviewDifficulty.DIFICIL, review, viewModel, correct, questions.size, recalled, forgotten, startedAt, onBack)
                    }
                }
            }
        }
    }
}

@Composable
private fun DifficultyButton(label: String, subtitle: String, difficulty: ReviewDifficulty, review: br.com.estudario.data.local.ReviewScheduleEntity, viewModel: AppViewModel, correct: Int, total: Int, recalled: Int, forgotten: Int, startedAt: Long, onDone: () -> Unit) {
    OutlinedButton(onClick = { viewModel.completeReview(review, difficulty, correct, total, recalled, forgotten, startedAt); onDone() }, Modifier.fillMaxWidth()) {
        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.Start) { Text(label, fontWeight = FontWeight.Bold); Text(subtitle, style = MaterialTheme.typography.bodySmall) }
    }
}
