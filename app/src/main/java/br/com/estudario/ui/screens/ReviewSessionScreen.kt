package br.com.estudario.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Event
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import br.com.estudario.data.local.QuestionWithOptions
import br.com.estudario.data.local.ReviewDifficulty
import br.com.estudario.data.local.ReviewScheduleEntity
import br.com.estudario.data.local.SnippetKind
import br.com.estudario.data.local.SummaryKind
import br.com.estudario.domain.OptionShuffle
import br.com.estudario.domain.ReviewIntervals
import br.com.estudario.ui.AppViewModel
import br.com.estudario.ui.brand.Button
import br.com.estudario.ui.brand.Icon
import br.com.estudario.ui.brand.marker
import br.com.estudario.ui.components.EmptyState
import br.com.estudario.ui.components.MarkdownText
import br.com.estudario.ui.components.QuestionProvenance
import br.com.estudario.ui.theme.screenPadding
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId
import java.util.UUID

private val RvGold = Color(0xFFF5B83D)
private val RvMint = Color(0xFF2FBF8A)
private val RvRed = Color(0xFFE5484D)
private val stageLabels = listOf("D+1", "D+7", "D+30", "D+60", "D+120")

private enum class RvStep(val label: String) { RECALL("Lembrar"), SUMMARY("Revisar"), TEST("Testar"), RATE("Avaliar") }

/**
 * Revisão ativa, com a mesma cara do site: o caminho dos passos no alto, cartões que viram para
 * lembrar, o resumo num papel, até 3 questões (as erradas primeiro, alternativas embaralhadas) e a
 * avaliação com a data em que o tópico volta em cada opção, já com a sugestão pelo desempenho.
 */
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
    val recall = remember(review.topicId, snippets.size) { snippets.filter { it.topicId == review.topicId && it.kind == SnippetKind.RECUPERACAO && !it.answer.isNullOrBlank() }.shuffled().take(8) }
    val quick = summaries.firstOrNull { it.topicId == review.topicId && it.kind == SummaryKind.RAPIDO } ?: summaries.firstOrNull { it.topicId == review.topicId }
    // Revisar é reencontrar a falha: primeiro o que já errou, depois o que nunca respondeu, depois o resto.
    val questions = remember(review.topicId) {
        val doTopico = allQuestions.filter { it.question.topicId == review.topicId && !it.question.isHidden }
        val erradas = doTopico.filter { it.question.errorCount > 0 }.sortedWith(compareByDescending<QuestionWithOptions> { it.question.errorCount }.thenBy { it.question.lastAnsweredAt ?: 0L })
        val nunca = doTopico.filter { it.question.errorCount == 0 && it.question.answerCount == 0 }.shuffled()
        val resto = doTopico.filter { it.question.errorCount == 0 && it.question.answerCount > 0 }.shuffled()
        (erradas + nunca + resto).take(3)
    }
    val steps = listOfNotNull(RvStep.RECALL.takeIf { recall.isNotEmpty() }, RvStep.SUMMARY, RvStep.TEST.takeIf { questions.isNotEmpty() }, RvStep.RATE)
    val scope = rememberCoroutineScope()
    val startedAt = remember { System.currentTimeMillis() }
    val sessionId = remember { "review-${UUID.randomUUID()}" }
    val shuffleSeed = remember { System.nanoTime() }
    var stepIndex by remember { mutableIntStateOf(0) }
    var recallIndex by remember { mutableIntStateOf(0) }
    var recalled by remember { mutableIntStateOf(0) }
    var forgotten by remember { mutableIntStateOf(0) }
    var questionIndex by remember { mutableIntStateOf(0) }
    var selected by remember { mutableStateOf<String?>(null) }
    var answered by remember { mutableStateOf<Boolean?>(null) }
    var correct by remember { mutableIntStateOf(0) }
    val step = steps[stepIndex.coerceAtMost(steps.lastIndex)]
    fun advance() { stepIndex = (stepIndex + 1).coerceAtMost(steps.lastIndex) }

    LazyColumn(Modifier.fillMaxSize(), contentPadding = screenPadding(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) { Icon(Icons.Outlined.ArrowBack, "Sair") }
                Column(Modifier.weight(1f)) {
                    Text("Revisão · ${stageLabels.getOrElse(review.stage - 1) { "etapa ${review.stage}" }}", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Black, color = Color(0xFFC98A0B))
                    Text(topic?.title ?: "Tópico", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.onSurface)
                }
            }
        }
        item { StepPath(steps, stepIndex) }
        when (step) {
            RvStep.RECALL -> {
                val prompt = recall[recallIndex.coerceAtMost(recall.lastIndex)]
                item {
                    Text("Tente lembrar antes de ver a resposta. Recuperar de memória é o que fixa.", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                }
                item {
                    Text("PERGUNTA ${recallIndex + 1} DE ${recall.size}", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Black, color = Color(0xFFC98A0B))
                    br.com.estudario.ui.components.RecallCard(prompt.text, prompt.answer, revealKey = prompt.id)
                }
                item {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        ChoiceButton("Não lembrei", Icons.Outlined.Close, RvRed, filled = false, Modifier.weight(1f)) { forgotten++; if (recallIndex < recall.lastIndex) recallIndex++ else advance() }
                        ChoiceButton("Lembrei", Icons.Outlined.Check, RvMint, filled = true, Modifier.weight(1f)) { recalled++; if (recallIndex < recall.lastIndex) recallIndex++ else advance() }
                    }
                }
            }
            RvStep.SUMMARY -> {
                item {
                    PaperCard("REVISÃO RÁPIDA") {
                        if (quick == null) Text("Este tópico ainda não tem revisão rápida. Relembre os pontos principais e siga.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                        else MarkdownText(quick.markdown)
                    }
                }
                item { Button(onClick = { advance() }, Modifier.fillMaxWidth()) { Text(if (RvStep.TEST in steps) "Ir para as questões" else "Avaliar a revisão") } }
            }
            RvStep.TEST -> {
                val question = questions[questionIndex.coerceAtMost(questions.lastIndex)]
                val original = question.options.sortedBy { it.position }
                val order = OptionShuffle.order(original.map { it.text }, shuffleSeed + question.question.id * 7919)
                item {
                    PaperCard("QUESTÃO ${questionIndex + 1} DE ${questions.size}") {
                        MarkdownText(question.question.statement)
                        QuestionProvenance(question.question)
                    }
                }
                order.forEachIndexed { pos, i ->
                    val option = original[i]
                    item(key = "rvopt-${option.id}") {
                        val container = when {
                            answered != null && option.isCorrect -> RvMint.copy(alpha = 0.18f)
                            answered == false && option.key == selected -> RvRed.copy(alpha = 0.15f)
                            option.key == selected -> MaterialTheme.colorScheme.primaryContainer
                            else -> MaterialTheme.colorScheme.surfaceContainerLow
                        }
                        val border = when {
                            answered != null && option.isCorrect -> RvMint
                            answered == false && option.key == selected -> RvRed
                            option.key == selected -> MaterialTheme.colorScheme.primary
                            else -> MaterialTheme.colorScheme.outlineVariant
                        }
                        Row(
                            Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(container).border(2.dp, border, RoundedCornerShape(16.dp))
                                .clickable(enabled = answered == null) { selected = option.key }.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Box(Modifier.size(30.dp).clip(CircleShape).background(border.copy(alpha = 0.18f)), contentAlignment = Alignment.Center) {
                                Text("ABCDEF"[pos].toString(), fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.onSurface)
                            }
                            Spacer(Modifier.width(12.dp))
                            br.com.estudario.ui.components.StudyInlineText(option.text, Modifier.weight(1f), onTap = { if (answered == null) selected = option.key })
                        }
                    }
                }
                if (answered != null) item {
                    Column(
                        Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).background((if (answered == true) RvMint else RvRed).copy(alpha = 0.12f)).padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                    ) {
                        Text(if (answered == true) "Isso! Você lembrou." else "Ainda não. Ela vai para o caderno de erros.", fontWeight = FontWeight.Black, color = if (answered == true) Color(0xFF1E9A6C) else RvRed)
                        MarkdownText(OptionShuffle.relabel(question.question.explanation, original.map { it.key }, order))
                    }
                }
                item {
                    if (answered == null) Button(enabled = selected != null, onClick = { scope.launch { answered = viewModel.answer(question, selected!!, sessionId); if (answered == true) correct++ } }, modifier = Modifier.fillMaxWidth()) { Text("Confirmar") }
                    else Button(onClick = { if (questionIndex < questions.lastIndex) { questionIndex++; selected = null; answered = null } else advance() }, Modifier.fillMaxWidth()) {
                        Text(if (questionIndex < questions.lastIndex) "Próxima questão" else "Avaliar a revisão")
                    }
                }
            }
            RvStep.RATE -> item {
                RateCard(review, reviews, recalled, forgotten, correct, questions.size) { difficulty ->
                    viewModel.completeReview(review, difficulty, correct, questions.size, recalled, forgotten, startedAt)
                    onBack()
                }
            }
        }
    }
}

/** O caminho da revisão: os passos feitos ficam verdes, o de agora em destaque. */
@Composable
private fun StepPath(steps: List<RvStep>, current: Int) {
    Row(
        Modifier.fillMaxWidth().clip(RoundedCornerShape(18.dp)).background(MaterialTheme.colorScheme.surfaceContainerLow)
            .border(1.5.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f), RoundedCornerShape(18.dp)).padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        steps.forEachIndexed { i, step ->
            val done = i < current
            val now = i == current
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    Modifier.size(30.dp).clip(CircleShape).background(
                        when { done -> RvMint; now -> MaterialTheme.colorScheme.primary; else -> MaterialTheme.colorScheme.surfaceVariant },
                    ),
                    contentAlignment = Alignment.Center,
                ) {
                    if (done) Icon(Icons.Outlined.Check, null, Modifier.size(16.dp), tint = Color.White)
                    else Text("${i + 1}", fontWeight = FontWeight.Black, color = if (now) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Text(step.label, style = MaterialTheme.typography.labelSmall, fontWeight = if (now) FontWeight.Black else FontWeight.SemiBold, color = if (now) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant)
            }
            if (i < steps.lastIndex) Box(Modifier.weight(1f).padding(horizontal = 6.dp).padding(bottom = 14.dp).height(2.dp).background(if (done) RvMint else MaterialTheme.colorScheme.outlineVariant))
        }
    }
}

@Composable
private fun PaperCard(tag: String, content: @Composable ColumnScope.() -> Unit) {
    Column(
        Modifier.fillMaxWidth().clip(RoundedCornerShape(22.dp)).background(MaterialTheme.colorScheme.surfaceContainerLow)
            .border(1.5.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.7f), RoundedCornerShape(22.dp))
            .drawBehind { drawRect(RvGold, size = Size(5.dp.toPx(), size.height)) }
            .padding(start = 22.dp, end = 18.dp, top = 16.dp, bottom = 18.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(tag, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Black, color = Color(0xFFC98A0B))
        content()
    }
}

@Composable
private fun ChoiceButton(label: String, icon: androidx.compose.ui.graphics.vector.ImageVector, color: Color, filled: Boolean, modifier: Modifier, onClick: () -> Unit) {
    Row(
        modifier.clip(RoundedCornerShape(16.dp)).background(if (filled) color else MaterialTheme.colorScheme.surfaceContainerLow)
            .border(2.dp, color.copy(alpha = if (filled) 1f else 0.5f), RoundedCornerShape(16.dp)).clickable(onClick = onClick).padding(vertical = 14.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(icon, null, Modifier.size(20.dp), tint = if (filled) Color.White else color)
        Spacer(Modifier.width(8.dp))
        Text(label, fontWeight = FontWeight.Black, color = if (filled) Color.White else color)
    }
}

/** Quando o tópico volta com cada escolha: a mesma regra de StudyRepository.completeReview. */
private fun nextDueDays(review: ReviewScheduleEntity, all: List<ReviewScheduleEntity>, difficulty: ReviewDifficulty, correct: Int, total: Int): Long {
    val now = System.currentTimeMillis()
    val doTopico = all.filter { it.topicId == review.topicId }
    val next = doTopico.filter { it.stage > review.stage && it.completedAt == null && it.ignoredAt == null }.minByOrNull { it.stage }
    val zone = ZoneId.systemDefault()
    val today = LocalDate.now(zone)
    if (next != null) {
        val remaining = (next.dueAt - now).coerceAtLeast(86_400_000L)
        val due = when (difficulty) {
            ReviewDifficulty.DIFICIL -> now + 86_400_000L
            ReviewDifficulty.NORMAL -> next.dueAt
            ReviewDifficulty.FACIL -> now + (remaining * 1.35).toLong()
        }
        return java.time.temporal.ChronoUnit.DAYS.between(today, java.time.Instant.ofEpochMilli(due).atZone(zone).toLocalDate()).coerceAtLeast(1)
    }
    val ordenadas = doTopico.sortedBy { it.stage }
    val anterior = if (ordenadas.size >= 2) (ordenadas[ordenadas.lastIndex].dueAt - ordenadas[ordenadas.lastIndex - 1].dueAt) / 86_400_000L else ReviewIntervals.days.last()
    return ReviewIntervals.adjustedIntervalDays(anterior, difficulty, correct, total).coerceAtLeast(1)
}

@Composable
private fun RateCard(review: ReviewScheduleEntity, all: List<ReviewScheduleEntity>, recalled: Int, forgotten: Int, correct: Int, total: Int, onDone: (ReviewDifficulty) -> Unit) {
    val recallRate = if (recalled + forgotten == 0) null else recalled.toDouble() / (recalled + forgotten)
    val questionRate = if (total == 0) null else correct.toDouble() / total
    val score = listOfNotNull(recallRate, questionRate).takeIf { it.isNotEmpty() }?.average()
    val suggested = when { score == null -> ReviewDifficulty.NORMAL; score >= 0.85 -> ReviewDifficulty.FACIL; score >= 0.5 -> ReviewDifficulty.NORMAL; else -> ReviewDifficulty.DIFICIL }
    var pick by remember { mutableStateOf(suggested) }
    PaperCard("SEU RESULTADO") {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.CenterVertically) {
            if (recalled + forgotten > 0) ResultRing(recalled, recalled + forgotten, "lembrou")
            if (total > 0) ResultRing(correct, total, "acertou")
            br.com.estudario.ui.assistant.Folha(76.dp, mood = if ((score ?: 0.6) >= 0.5) br.com.estudario.ui.assistant.FolhaMood.HAPPY else br.com.estudario.ui.assistant.FolhaMood.THINKING)
        }
        Text("Como foi lembrar este tópico?", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black, modifier = Modifier.padding(top = 6.dp).marker())
        Text("A escolha define quando ele volta. Sugerimos pelo seu desempenho, mas a decisão é sua.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        listOf(
            Triple(ReviewDifficulty.FACIL, "Fácil", "Recuperei quase tudo com segurança"),
            Triple(ReviewDifficulty.NORMAL, "Normal", "Lembrei depois de pensar um pouco"),
            Triple(ReviewDifficulty.DIFICIL, "Difícil", "Ainda confundo pontos importantes"),
        ).forEach { (difficulty, label, hint) ->
            val color = when (difficulty) { ReviewDifficulty.FACIL -> RvMint; ReviewDifficulty.NORMAL -> MaterialTheme.colorScheme.primary; ReviewDifficulty.DIFICIL -> RvRed }
            val on = pick == difficulty
            val days = nextDueDays(review, all, difficulty, correct, total)
            Row(
                Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(if (on) color.copy(alpha = 0.12f) else MaterialTheme.colorScheme.surfaceContainerHigh.copy(alpha = 0.5f))
                    .border(2.dp, if (on) color else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f), RoundedCornerShape(16.dp))
                    .clickable { pick = difficulty }.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(label, fontWeight = FontWeight.Black, color = color)
                        if (difficulty == suggested) Text(
                            "SUGERIDO",
                            Modifier.padding(start = 8.dp).clip(RoundedCornerShape(50)).background(RvGold).padding(horizontal = 8.dp, vertical = 2.dp),
                            style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Black, color = Color(0xFF1D1670),
                        )
                    }
                    Text(hint, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Outlined.Event, null, Modifier.size(16.dp), tint = color)
                    Spacer(Modifier.width(4.dp))
                    Text(if (days <= 1L) "amanhã" else "em $days dias", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                }
            }
        }
        Button(onClick = { onDone(pick) }, Modifier.fillMaxWidth().padding(top = 4.dp)) { Text("Concluir revisão") }
    }
}

@Composable
private fun ResultRing(value: Int, total: Int, label: String) {
    val fraction by animateFloatAsState(if (total == 0) 0f else value.toFloat() / total, tween(900), label = "rvRing")
    val primary = MaterialTheme.colorScheme.primary
    val track = MaterialTheme.colorScheme.surfaceVariant
    Box(Modifier.size(92.dp), contentAlignment = Alignment.Center) {
        Canvas(Modifier.size(92.dp)) {
            val stroke = 9.dp.toPx()
            val inset = stroke / 2
            val arc = Size(size.width - stroke, size.height - stroke)
            drawArc(track, 0f, 360f, false, Offset(inset, inset), arc, style = Stroke(stroke))
            drawArc(primary, -90f, 360f * fraction, false, Offset(inset, inset), arc, style = Stroke(stroke, cap = StrokeCap.Round))
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("$value/$total", fontWeight = FontWeight.Black, style = MaterialTheme.typography.titleMedium)
            Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
