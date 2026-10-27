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
import androidx.compose.material.icons.outlined.FormatStrikethrough
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.TouchApp
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Event
import androidx.compose.material.icons.outlined.WorkspacePremium
import androidx.compose.material.icons.outlined.StickyNote2
import androidx.compose.material.icons.outlined.Shuffle
import androidx.compose.material.icons.outlined.Remove
import androidx.compose.material.icons.outlined.Bookmarks
import androidx.compose.material.icons.outlined.Bolt
import androidx.compose.material.icons.outlined.Balance
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.Psychology
import androidx.compose.material.icons.outlined.Quiz
import androidx.compose.material.icons.outlined.Replay
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
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
import androidx.compose.ui.unit.sp
import br.com.estudario.data.local.QuestionWithOptions
import br.com.estudario.data.local.ReviewDifficulty
import br.com.estudario.data.local.ReviewScheduleEntity
import br.com.estudario.domain.ReviewPracticeOptions
import br.com.estudario.domain.ReviewQuestionOrder
import br.com.estudario.data.local.SnippetKind
import br.com.estudario.data.local.SummaryKind
import br.com.estudario.domain.OptionShuffle
import br.com.estudario.domain.ReviewStageProgression
import br.com.estudario.domain.toStageState
import br.com.estudario.domain.reviewPracticeQuestions
import br.com.estudario.ui.brand.BrandIcon
import br.com.estudario.ui.brand.Glyph
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
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
/** "2ª revisão · 7 dias": a etapa e o intervalo de verdade (antes eram rótulos fixos D+1, D+7, D+30 que não batiam). */
private fun stageName(review: ReviewScheduleEntity): String = "${review.stage}ª revisão" + (review.intervalDays?.let { " · $it ${if (it == 1L) "dia" else "dias"}" } ?: "")

private enum class RvStep(val label: String) { START("Começar"), RECALL("Lembrar"), SUMMARY("Consultar"), TEST("Testar"), RATE("Avaliar") }
private data class ReviewOutcome(val topicId: Long, val days: Long, val repeatedStage: Int? = null)

/**
 * Revisão ativa, com a mesma cara do site: o caminho dos passos no alto, cartões que viram para
 * lembrar, o resumo num papel, até 3 questões (as erradas primeiro, alternativas embaralhadas) e a
 * avaliação com a data em que o tópico volta em cada opção, já com a sugestão pelo desempenho.
 */
@Composable
fun ReviewSessionScreen(
    viewModel: AppViewModel,
    reviewId: Long,
    onQuizTopic: (Long) -> Unit = {},
    onOpenTopic: (Long) -> Unit = {},
    /** Revisão concluída (tópico, início): a rota fecha a tarefa de revisão do plano do dia. */
    onReviewCompleted: (Long, Long) -> Unit = { _, _ -> },
    onBack: () -> Unit,
) {
    val reviews by viewModel.reviews.collectAsState()
    val topics by viewModel.topics.collectAsState()
    val allQuestions by viewModel.questions.collectAsState()
    val preferences by viewModel.reviewPractice.collectAsState()
    val review = reviews.firstOrNull { it.id == reviewId }
    // Acabou de concluir: o fechamento, com as questões para fixar.
    var done by remember { mutableStateOf<ReviewOutcome?>(null) }
    done?.let { (topicId, days, repeatedStage) ->
        val available = allQuestions.count { it.question.topicId == topicId && !it.question.isHidden }
        ReviewDone(topics.firstOrNull { it.id == topicId }?.title ?: "Tópico revisado", days, available, repeatedStage, onQuiz = { onQuizTopic(topicId) }, onGenerate = { onOpenTopic(topicId) }, onBack = onBack)
        return
    }
    if (review == null) { EmptyState("Revisão não encontrada", "Ela pode já ter sido removida.", "Voltar", onBack); return }
    if (review.completedAt != null || review.ignoredAt != null) { EmptyState("Revisão já encerrada", "Volte à agenda para ver a próxima etapa.", "Voltar", onBack); return }
    if (reviews.any { it.topicId == review.topicId && it.stage < review.stage && it.completedAt == null && it.ignoredAt == null }) {
        EmptyState("Há uma etapa anterior pendente", "Conclua a etapa anterior com Normal ou Fácil para avançar nesta revisão.", "Voltar às revisões", onBack)
        return
    }
    br.com.estudario.ui.HoldCelebrations()
    val topic = topics.firstOrNull { it.id == review.topicId }
    // Revisão espaçada vale no dia dela: antes, ainda é cedo (revisar antes enfraquece o efeito).
    val zoneHere = ZoneId.systemDefault()
    val dueDay = java.time.Instant.ofEpochMilli(review.dueAt).atZone(zoneHere).toLocalDate()
    if (dueDay.isAfter(LocalDate.now(zoneHere))) {
        EmptyState(
            "Ainda não é dia desta revisão",
            "${topic?.title ?: "Este tópico"} volta em ${dueDay.dayOfMonth}/${dueDay.monthValue.toString().padStart(2, '0')}. Revisar antes da hora enfraquece o efeito; até lá, treine questões se quiser.",
            "Treinar questões do tópico",
        ) { onQuizTopic(review.topicId) }
        return
    }
    val loadedOptions = preferences
    if (loadedOptions == null) { Text("Preparando sua revisão…", Modifier.padding(24.dp)); return }
    var loadAttempt by remember(reviewId) { mutableIntStateOf(0) }
    val materialResult by produceState<Result<br.com.estudario.domain.ReviewPracticeMaterial>?>(null, reviewId, loadAttempt) {
        value = try { Result.success(viewModel.loadReviewPracticeMaterial(review.topicId)) }
        catch (cancelled: kotlinx.coroutines.CancellationException) { throw cancelled }
        catch (error: Exception) { Result.failure(error) }
    }
    if (materialResult?.isFailure == true) { EmptyState("Não consegui abrir o material", "Seu progresso continua salvo. Tente carregar a revisão novamente.", "Tentar novamente") { loadAttempt++ }; return }
    val material = materialResult?.getOrNull()
    if (material == null) { Text("Preparando seu material…", Modifier.padding(24.dp)); return }
    var config by remember(reviewId) { mutableStateOf(loadedOptions) }
    val practiceSeed = remember(reviewId) { System.nanoTime() }
    var settingsOpen by remember(reviewId) { mutableStateOf(false) }
    var highlightsOpen by remember(reviewId) { mutableStateOf(false) }
    val topicTheories = material.theories
    val theoryOrder = topicTheories.mapIndexed { index, theory -> theory.id to index }.toMap()
    val highlights = remember(reviewId, material, config) {
        material.marks.filter { it.theoryId in theoryOrder && it.quote.isNotBlank() }
            .sortedWith(compareBy({ theoryOrder[it.theoryId] }, { it.blockIndex }, { it.createdAt }))
            .map { ReviewCard("GRIFO", topicTheories.firstOrNull { theory -> theory.id == it.theoryId }?.title.orEmpty(), it.quote, it.note.takeIf { _ -> config.showHighlightNotes }) }
    }
    if (highlightsOpen && highlights.isNotEmpty()) NotebookReview("Grifos · ${topic?.title.orEmpty()}", highlights, allowNavigation = false) { highlightsOpen = false }
    val recall = remember(reviewId, material, config) {
        val cards = material.snippets.filter { it.kind == SnippetKind.RECUPERACAO && !it.answer.isNullOrBlank() }.sortedWith(compareBy({ it.position }, { it.id }))
        (if (config.shuffleFlashcards) cards.shuffled(kotlin.random.Random(practiceSeed)) else cards).take(config.flashcardCount)
    }
    val quick = material.summaries.firstOrNull { it.kind == SummaryKind.RAPIDO } ?: material.summaries.firstOrNull()
    // Revisar é reencontrar a falha: primeiro o que já errou, depois o que nunca respondeu, depois o resto.
    val questions = remember(reviewId, material, config) { reviewPracticeQuestions(material.questions, review.topicId, config, practiceSeed) }
    val consultation = config.showSummary || (config.offerHighlights && highlights.isNotEmpty())
    // A abertura (START) diz o que vem e quanto leva; sem atividade, vai direto ao ajuste.
    val activities = listOfNotNull(RvStep.RECALL.takeIf { recall.isNotEmpty() }, RvStep.SUMMARY.takeIf { consultation }, RvStep.TEST.takeIf { questions.isNotEmpty() })
    val steps = (if (activities.isEmpty()) emptyList() else listOf(RvStep.START)) + activities + RvStep.RATE
    // Difícil na última vez: a mesma etapa volta todo dia até a pessoa marcar Normal ou Fácil.
    val repeating = review.perceivedDifficulty == ReviewDifficulty.DIFICIL
    val lastSeen = (reviews.filter { it.topicId == review.topicId }.mapNotNull { it.completedAt } + listOfNotNull(topic?.lastReviewedAt)).maxOrNull()
    val minutes = (recall.size * 0.7 + (if (config.showSummary && quick != null) 2.0 else 0.0) + questions.size * 1.5 + 0.5).toInt().coerceAtLeast(1)
    if (settingsOpen) ReviewPracticeSettings(loadedOptions, onClose = { settingsOpen = false }, onSave = { viewModel.setReviewPractice(it); if (steps.size == 1) config = it; settingsOpen = false })
    val scope = rememberCoroutineScope()
    val startedAt = remember { System.currentTimeMillis() }
    val sessionId = remember { "review-${UUID.randomUUID()}" }
    val shuffleSeed = remember { System.nanoTime() }
    var stepIndex by remember { mutableIntStateOf(0) }
    var recallIndex by remember { mutableIntStateOf(0) }
    var flipped by remember { mutableStateOf(false) }
    var recalled by remember { mutableIntStateOf(0) }
    var forgotten by remember { mutableIntStateOf(0) }
    var questionIndex by remember { mutableIntStateOf(0) }
    var selected by remember { mutableStateOf<String?>(null) }
    var answered by remember { mutableStateOf<Boolean?>(null) }
    var correct by remember { mutableIntStateOf(0) }
    // Alternativas riscadas, como no treino normal (tocar no risco de novo desfaz).
    var crossed by remember { mutableStateOf(setOf<Long>()) }
    // Respostas desta revisão, na ordem: o Folha reage como no treino (ufa, sequência, acolhe).
    var results by remember { mutableStateOf(listOf<Boolean>()) }
    val step = steps[stepIndex.coerceAtMost(steps.lastIndex)]
    fun advance() { stepIndex = (stepIndex + 1).coerceAtMost(steps.lastIndex) }
    // Quanto da revisão já foi: cada cartão e cada questão contam um passinho.
    val totalUnits = recall.size + (if (consultation) 1 else 0) + questions.size + 1
    val doneUnits = when (step) {
        RvStep.START -> 0
        RvStep.RECALL -> recallIndex
        RvStep.SUMMARY -> recall.size
        RvStep.TEST -> recall.size + (if (consultation) 1 else 0) + questionIndex + (if (answered != null) 1 else 0)
        RvStep.RATE -> totalUnits - 1
    }
    val progress by androidx.compose.animation.core.animateFloatAsState(doneUnits / totalUnits.coerceAtLeast(1).toFloat(), label = "rv-progress")

    LazyColumn(Modifier.fillMaxSize(), contentPadding = screenPadding(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) { Icon(Icons.Outlined.ArrowBack, "Sair") }
                Column(Modifier.weight(1f)) {
                    Text(stageName(review), style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Black, color = Color(0xFFC98A0B))
                    Text(topic?.title ?: "Tópico", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.onSurface)
                    Text("${recall.size} ${if (recall.size == 1) "flashcard" else "flashcards"} · ${questions.size} ${if (questions.size == 1) "questão" else "questões"} · ~$minutes min", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                // Os grifos da matéria, a qualquer momento e só para ler (não dá para mudar daqui).
                if (highlights.isNotEmpty()) IconButton(onClick = { highlightsOpen = true }, modifier = Modifier.semantics { contentDescription = "Ler meus grifos (só leitura)" }) {
                    androidx.compose.material3.BadgedBox(badge = { androidx.compose.material3.Badge(containerColor = RvGold) { Text("${highlights.size}", color = Color(0xFF3B2300)) } }) { BrandIcon(Glyph.Bookmark, size = 24.dp) }
                }
            }
        }
        item {
            androidx.compose.material3.LinearProgressIndicator(
                progress = { progress }, modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(50)),
                color = RvGold, trackColor = MaterialTheme.colorScheme.surfaceVariant, drawStopIndicator = {},
            )
        }
        if (step != RvStep.START) item { StepPath(steps.filter { it != RvStep.START }, stepIndex - (if (steps.first() == RvStep.START) 1 else 0)) }
        when (step) {
            RvStep.START -> item {
                ReviewStart(
                    topicTitle = topic?.title ?: "este tópico", repeating = repeating, lastSeen = lastSeen,
                    recall = recall.size, consultation = consultation, summary = config.showSummary, questions = questions.size,
                    stage = review.stage, minutes = minutes, onStart = { advance() },
                    tuner = {
                        ReviewTuner(
                            config,
                            cards = material.snippets.count { it.kind == SnippetKind.RECUPERACAO && !it.answer.isNullOrBlank() },
                            questions = material.questions.count { it.question.topicId == review.topicId && !it.question.isHidden && it.question.simulationId == null },
                            hasSummary = quick != null, hasHighlights = material.marks.any { it.theoryId in theoryOrder && it.quote.isNotBlank() },
                        ) { next -> config = next; viewModel.setReviewPractice(next) }
                    },
                )
            }
            RvStep.RECALL -> {
                val prompt = recall[recallIndex.coerceAtMost(recall.lastIndex)]
                item {
                    Text("Tente lembrar antes de virar o cartão. Recuperar de memória é o que fixa.", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                }
                item(key = "rv-flip-${prompt.id}") {
                    // O mesmo cartão do site: pergunta na frente, resposta no verso, vira em 3D.
                    ReviewFlipCard(prompt.text, prompt.answer.orEmpty(), recallIndex + 1, recall.size, flipped) { flipped = !flipped }
                }
                item {
                    val somCtx = androidx.compose.ui.platform.LocalContext.current
                    Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        if (!flipped) Button(onClick = { flipped = true }, modifier = Modifier.fillMaxWidth().height(52.dp)) { Text("Virar o cartão", fontWeight = FontWeight.Bold) }
                        else {
                            Text("Você lembrou antes de virar o cartão?", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                fun next(lembrou: Boolean) {
                                    br.com.estudario.ui.brand.EstudarioSom.play(somCtx, if (lembrou) "lembrei" else "nao_lembrei")
                                    if (lembrou) recalled++ else forgotten++
                                    flipped = false
                                    if (recallIndex < recall.lastIndex) recallIndex++ else advance()
                                }
                                androidx.compose.material3.OutlinedButton(
                                    onClick = { next(false) }, modifier = Modifier.weight(1f).height(54.dp), shape = RoundedCornerShape(16.dp),
                                    border = androidx.compose.foundation.BorderStroke(2.dp, RvRed.copy(alpha = 0.55f)),
                                    colors = androidx.compose.material3.ButtonDefaults.outlinedButtonColors(containerColor = MaterialTheme.colorScheme.surface, contentColor = RvRed),
                                ) { Icon(Icons.Outlined.Close, null, Modifier.size(18.dp)); Spacer(Modifier.width(6.dp)); Text("Não lembrei", fontWeight = FontWeight.Black) }
                                androidx.compose.material3.Button(
                                    onClick = { next(true) }, modifier = Modifier.weight(1f).height(54.dp), shape = RoundedCornerShape(16.dp),
                                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = Color(0xFF2FBF8A), contentColor = Color.White),
                                ) { Icon(Icons.Outlined.Check, null, Modifier.size(18.dp)); Spacer(Modifier.width(6.dp)); Text("Lembrei", fontWeight = FontWeight.Black) }
                            }
                            androidx.compose.material3.TextButton(onClick = { flipped = false }) { Text("Voltar à pergunta") }
                        }
                    }
                }
            }
            RvStep.SUMMARY -> {
                if (config.offerHighlights && highlights.isNotEmpty()) item {
                    PaperCard("SEUS GRIFOS") {
                        Text("Você já tentou lembrar. Quer conferir o que destacou?", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Text("Leia seus ${highlights.size} trechos, um por vez, e volte para continuar a revisão.", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        br.com.estudario.ui.brand.OutlinedButton(onClick = { highlightsOpen = true }, modifier = Modifier.fillMaxWidth()) { BrandIcon(Glyph.Bookmark, size = 24.dp); Spacer(Modifier.width(8.dp)); Text("Ler meus grifos") }
                    }
                }
                if (config.showSummary) item {
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
                // A questão inteira num papel só, como no site: enunciado, alternativas, a reação do Folha e o porquê.
                item(key = "rv-q-${question.question.id}") {
                    PaperCard("QUESTÃO ${questionIndex + 1} DE ${questions.size}") {
                        MarkdownText(question.question.statement)
                        QuestionProvenance(question.question)
                        Spacer(Modifier.height(4.dp))
                        order.forEachIndexed { pos, i ->
                            val option = original[i]
                            val isCrossed = option.id in crossed && answered == null
                            val tint = when {
                                answered != null && option.isCorrect -> RvMint
                                answered == false && option.key == selected -> RvRed
                                option.key == selected -> MaterialTheme.colorScheme.primary
                                else -> null
                            }
                            // Fundo sólido (a cor do estado por cima da superfície): antes era transparente e o
                            // "Riscar" do gesto aparecia por trás do texto.
                            val base = MaterialTheme.colorScheme.surface
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Row(
                                    Modifier.weight(1f).clip(RoundedCornerShape(16.dp)).background(base)
                                        .background(tint?.copy(alpha = if (answered != null || option.key != selected) 0.16f else 0.12f) ?: Color.Transparent)
                                        .border(2.dp, tint ?: MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(16.dp))
                                        .graphicsLayer { alpha = if (isCrossed) 0.45f else 1f }
                                        .clickable(enabled = answered == null) { if (isCrossed) crossed = crossed - option.id else selected = option.key }
                                        .padding(horizontal = 12.dp, vertical = 12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    Box(
                                        Modifier.size(30.dp).clip(CircleShape).background(tint ?: MaterialTheme.colorScheme.surfaceVariant),
                                        contentAlignment = Alignment.Center,
                                    ) {
                                        Text(
                                            when { answered != null && option.isCorrect -> "✓"; answered == false && option.key == selected -> "✕"; else -> "ABCDEF"[pos].toString() },
                                            fontWeight = FontWeight.Black, color = if (tint != null) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                                        )
                                    }
                                    Spacer(Modifier.width(12.dp))
                                    br.com.estudario.ui.components.StudyInlineText(
                                        option.text, Modifier.weight(1f),
                                        onTap = { if (answered == null) { if (isCrossed) crossed = crossed - option.id else selected = option.key } },
                                        style = if (isCrossed) MaterialTheme.typography.bodyLarge.copy(textDecoration = androidx.compose.ui.text.style.TextDecoration.LineThrough) else MaterialTheme.typography.bodyLarge,
                                    )
                                }
                                // Riscar: um botão ao lado, como no site (o gesto de arrastar ficava por trás do texto).
                                if (answered == null) IconButton(
                                    onClick = { crossed = if (option.id in crossed) crossed - option.id else crossed + option.id; if (selected == option.key) selected = null },
                                    modifier = Modifier.size(40.dp).semantics { contentDescription = if (isCrossed) "Desfazer risco" else "Riscar alternativa" },
                                ) {
                                    Icon(Icons.Outlined.FormatStrikethrough, null, tint = if (isCrossed) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f))
                                }
                            }
                        }
                        if (answered != null) {
                            // O Folha reage como no treino: comemora, consola, solta o "ufa" ao virar e acolhe na sequência de erros.
                            val right = answered == true
                            val inARow = results.asReversed().takeWhile { it }.size
                            val relief = right && inARow == 1 && results.size >= 2 && !results[results.size - 2]
                            val tough = !right && results.size >= 3 && results.takeLast(3).none { it }
                            Spacer(Modifier.height(4.dp))
                            Column(
                                Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(MaterialTheme.colorScheme.surface)
                                    .background((if (right) RvMint else RvRed).copy(alpha = 0.12f)).padding(14.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                    androidx.compose.runtime.key(question.question.id) {
                                        br.com.estudario.ui.assistant.Folha(
                                            52.dp,
                                            mood = when {
                                                relief -> br.com.estudario.ui.assistant.FolhaMood.RELIEVED
                                                right -> br.com.estudario.ui.assistant.FolhaMood.HAPPY
                                                tough -> br.com.estudario.ui.assistant.FolhaMood.TALKING
                                                else -> br.com.estudario.ui.assistant.FolhaMood.SAD
                                            },
                                        )
                                    }
                                    Column(Modifier.weight(1f)) {
                                        Text(when { relief -> "Ufa, virou!"; right -> "Isso! Você lembrou."; tough -> "Tô aqui com você"; else -> "Ainda não." }, fontWeight = FontWeight.Black, color = if (right) Color(0xFF1E9A6C) else RvRed)
                                        Text(
                                            when {
                                                relief -> br.com.estudario.ui.assistant.FolhaLines.forRelief(question.question.id)
                                                tough -> br.com.estudario.domain.FolhaMemory.toughLines[(question.question.id % br.com.estudario.domain.FolhaMemory.toughLines.size).toInt()]
                                                else -> br.com.estudario.ui.assistant.FolhaLines.forAnswer(right, question.question.id)
                                            } + if (right) "" else " Ela foi para o caderno de erros.",
                                            style = MaterialTheme.typography.bodyMedium,
                                        )
                                    }
                                }
                                if (question.question.explanation.isNotBlank()) {
                                    Text("Por quê", fontWeight = FontWeight.Black, color = if (right) Color(0xFF1E9A6C) else RvRed)
                                    MarkdownText(OptionShuffle.relabel(question.question.explanation, original.map { it.key }, order))
                                }
                            }
                        }
                    }
                }
                item {
                    if (answered == null) Button(enabled = selected != null, onClick = { scope.launch { val ok = viewModel.answer(question, selected!!, sessionId); answered = ok; if (ok == true) correct++; results = results + (ok == true) } }, modifier = Modifier.fillMaxWidth().height(52.dp)) { Text("Confirmar", fontWeight = FontWeight.Bold) }
                    else Button(onClick = { crossed = emptySet(); if (questionIndex < questions.lastIndex) { questionIndex++; selected = null; answered = null } else advance() }, Modifier.fillMaxWidth().height(52.dp)) {
                        Text(if (questionIndex < questions.lastIndex) "Próxima questão" else "Avaliar a revisão", fontWeight = FontWeight.Bold)
                    }
                }
            }
            RvStep.RATE -> item {
                if (steps.size == 1) {
                    EmptyState("Monte sua revisão", "Não há atividade disponível com suas escolhas. Ative flashcards, resumo ou questões na engrenagem.", "Ajustar revisão") { settingsOpen = true }
                } else RateCard(review, reviews, recalled, forgotten, correct, questions.size) { difficulty ->
                    val days = nextDueDays(review, reviews, difficulty, correct, questions.size)
                    viewModel.completeReview(review, difficulty, correct, questions.size, recalled, forgotten, startedAt)
                    onReviewCompleted(review.topicId, startedAt)
                    done = ReviewOutcome(review.topicId, days, review.stage.takeIf { difficulty == ReviewDifficulty.DIFICIL })
                }
            }
        }
    }
}

/** Modelos prontos de revisão: rápida, equilibrada e completa (os mesmos do site). */
private val rvPresets = listOf(
    Triple(Icons.Outlined.Bolt, "Rápida", ReviewPracticeOptions(questionCount = 3, flashcardCount = 4, showSummary = false, offerHighlights = false)),
    Triple(Icons.Outlined.Balance, "Equilibrada", ReviewPracticeOptions(questionCount = 5, flashcardCount = 8)),
    Triple(Icons.Outlined.WorkspacePremium, "Completa", ReviewPracticeOptions(questionCount = 10, flashcardCount = 15)),
)

/**
 * "Monte sua revisão", à vista na abertura (antes só na engrenagem): modelos prontos, quantos cartões
 * e questões, o que consultar e a ordem. Muda esta sessão na hora e fica salvo para as próximas.
 */
@Composable
private fun ReviewTuner(config: ReviewPracticeOptions, cards: Int, questions: Int, hasSummary: Boolean, hasHighlights: Boolean, onChange: (ReviewPracticeOptions) -> Unit) {
    fun same(a: ReviewPracticeOptions, b: ReviewPracticeOptions) =
        // compara pelo que o tópico tem: com 6 cartões, "Completa" (15) e 6 são a mesma escolha
        minOf(a.questionCount, questions) == minOf(b.questionCount, questions) && minOf(a.flashcardCount, cards) == minOf(b.flashcardCount, cards) &&
            a.showSummary == b.showSummary && a.offerHighlights == b.offerHighlights
    Column(
        Modifier.fillMaxWidth().clip(RoundedCornerShape(20.dp)).background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f))
            .border(1.5.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(20.dp)).padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Column {
            Text("Monte sua revisão", fontWeight = FontWeight.Black)
            Text("Vale agora e fica salvo para as próximas", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            rvPresets.forEach { (icon, name, preset) ->
                val on = same(config, preset)
                Column(
                    Modifier.weight(1f).clip(RoundedCornerShape(16.dp))
                        .background(if (on) RvGold.copy(alpha = 0.16f) else MaterialTheme.colorScheme.surfaceContainerLow)
                        .border(2.dp, if (on) RvGold else MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(16.dp))
                        .clickable { onChange(preset.copy(order = config.order, shuffleFlashcards = config.shuffleFlashcards, showHighlightNotes = config.showHighlightNotes)) }
                        .semantics { contentDescription = "Revisão $name" }
                        .padding(10.dp),
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                ) {
                    Icon(icon, null, tint = Color(0xFFC98A0B))
                    Text(name, fontWeight = FontWeight.Black, style = MaterialTheme.typography.bodyMedium)
                    Text("${minOf(preset.flashcardCount, cards)} ${if (minOf(preset.flashcardCount, cards) == 1) "cartão" else "cartões"} · ${minOf(preset.questionCount, questions)} ${if (minOf(preset.questionCount, questions) == 1) "questão" else "questões"}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
        RvCount(Icons.Outlined.Psychology, "Cartões para lembrar", config.flashcardCount, cards, 30) { onChange(config.copy(flashcardCount = it)) }
        RvCount(Icons.Outlined.Quiz, "Questões", config.questionCount, questions, 50) { onChange(config.copy(questionCount = it)) }
        RvToggle(Icons.Outlined.MenuBook, "Revisão rápida", config.showSummary, hasSummary) { onChange(config.copy(showSummary = it)) }
        RvToggle(Icons.Outlined.Bookmarks, "Oferecer meus grifos", config.offerHighlights, hasHighlights) { onChange(config.copy(offerHighlights = it)) }
        RvToggle(Icons.Outlined.StickyNote2, "Notas dos grifos", config.showHighlightNotes, hasHighlights) { onChange(config.copy(showHighlightNotes = it)) }
        RvToggle(Icons.Outlined.Shuffle, "Embaralhar cartões", config.shuffleFlashcards, true) { onChange(config.copy(shuffleFlashcards = it)) }
        Text("Questões primeiro", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            listOf(ReviewQuestionOrder.ERRORS_FIRST to "as que errei", ReviewQuestionOrder.UNSEEN_FIRST to "as novas", ReviewQuestionOrder.MIXED to "misturado").forEach { (order, label) ->
                androidx.compose.material3.FilterChip(selected = config.order == order, onClick = { onChange(config.copy(order = order)) }, label = { Text(label) })
            }
        }
    }
}

/** Um contador da revisão (− número +), com quantos o tópico tem. */
@Composable
private fun RvCount(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, value: Int, available: Int, max: Int, onChange: (Int) -> Unit) {
    Row(
        Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(MaterialTheme.colorScheme.surfaceContainerLow).padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Box(Modifier.size(36.dp).clip(RoundedCornerShape(12.dp)).background(RvGold.copy(alpha = 0.22f)), contentAlignment = Alignment.Center) { Icon(icon, null, tint = Color(0xFFC98A0B)) }
        Column(Modifier.weight(1f)) {
            Text(label, fontWeight = FontWeight.Bold)
            Text(if (available == 0) "Este tópico ainda não tem" else if (value > available) "Vai usar as $available que o tópico tem" else "O tópico tem $available", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        IconButton(onClick = { onChange((value - 1).coerceAtLeast(0)) }, enabled = value > 0, modifier = Modifier.semantics { contentDescription = "Menos" }) { Icon(Icons.Outlined.Remove, null) }
        Text("${value.coerceAtMost(available)}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black)
        IconButton(onClick = { onChange((value + 1).coerceAtMost(max)) }, enabled = value < max && value < available, modifier = Modifier.semantics { contentDescription = "Mais" }) { Icon(Icons.Outlined.Add, null) }
    }
}

@Composable
private fun RvToggle(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, on: Boolean, enabled: Boolean, onChange: (Boolean) -> Unit) {
    Row(
        Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp)).background(MaterialTheme.colorScheme.surfaceContainerLow)
            .clickable(enabled = enabled) { onChange(!on) }.padding(horizontal = 12.dp, vertical = 4.dp)
            .graphicsLayer { alpha = if (enabled) 1f else 0.5f },
        verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Icon(icon, null, tint = if (on && enabled) Color(0xFFC98A0B) else MaterialTheme.colorScheme.onSurfaceVariant)
        Text(label, Modifier.weight(1f), fontWeight = FontWeight.SemiBold)
        androidx.compose.material3.Switch(checked = on && enabled, onCheckedChange = { onChange(it) }, enabled = enabled)
    }
}

/** A abertura da revisão: o Folha chama para relembrar, diz o que vem, quanto leva e onde está na escadinha. */
@Composable
private fun ReviewStart(topicTitle: String, repeating: Boolean, lastSeen: Long?, recall: Int, consultation: Boolean, summary: Boolean, questions: Int, stage: Int, minutes: Int, onStart: () -> Unit, tuner: @Composable () -> Unit = {}) {
    val since = lastSeen?.let { ((System.currentTimeMillis() - it) / 86_400_000L).toInt() }
    PaperCard(if (repeating) "REPETINDO A ETAPA" else "HORA DE RELEMBRAR") {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            br.com.estudario.ui.assistant.Folha(80.dp, mood = if (repeating) br.com.estudario.ui.assistant.FolhaMood.TALKING else br.com.estudario.ui.assistant.FolhaMood.HAPPY)
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(if (repeating) "De novo hoje, e tudo bem!" else "Bora relembrar $topicTitle?", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black)
                Text(
                    when {
                        repeating -> "Na última você marcou Difícil, então ela voltou na mesma etapa. Volta todo dia até ficar Normal ou Fácil: é assim que fixa."
                        since == null -> "Recuperar de memória agora é o que faz isso ficar para a prova."
                        since <= 0 -> "Você viu isso hoje mesmo. Uma passada rápida e está garantido."
                        since == 1 -> "Faz 1 dia que você viu isso. É o momento certo de puxar da memória."
                        else -> "Faz $since dias que você viu isso. É o momento certo de puxar da memória, antes de esquecer."
                    },
                    style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
        if (recall > 0) PlanItem(Icons.Outlined.Psychology, "$recall ${if (recall == 1) "cartão" else "cartões"} para lembrar", "Tente responder antes de virar")
        if (consultation) PlanItem(Icons.Outlined.MenuBook, if (summary) "Revisão rápida" else "Seus grifos", "Para conferir o que você lembrou")
        if (questions > 0) PlanItem(Icons.Outlined.Quiz, "$questions ${if (questions == 1) "questão" else "questões"}", "As que você errou vêm primeiro")
        PlanItem(Icons.Outlined.StarOutline, "Como foi", "Difícil volta amanhã; Normal e Fácil avançam a etapa")
        tuner()
        StageLadder(stage, repeating)
        Button(onClick = onStart, modifier = Modifier.fillMaxWidth().height(52.dp)) { Text("Começar · ~$minutes min", fontWeight = FontWeight.Black) }
    }
}

@Composable
private fun PlanItem(icon: androidx.compose.ui.graphics.vector.ImageVector, title: String, hint: String) {
    Row(
        Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f)).padding(12.dp),
        verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Box(Modifier.size(38.dp).clip(RoundedCornerShape(12.dp)).background(RvGold.copy(alpha = 0.22f)), contentAlignment = Alignment.Center) { Icon(icon, null, tint = Color(0xFFC98A0B)) }
        Column { Text(title, fontWeight = FontWeight.Bold); Text(hint, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant) }
    }
}

/** Cinco etapas em volta da atual (na 7ª, da 5ª à 9ª); a etapa repetida por Difícil fica laranja. */
@Composable
private fun StageLadder(stage: Int, repeating: Boolean) {
    val first = (stage - 2).coerceAtLeast(1)
    val orange = Color(0xFFFF8A3D)
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top) {
        (first until first + 5).forEach { s ->
            Column(Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(4.dp)) {
                val color = when { s < stage -> RvMint; s == stage -> if (repeating) orange else RvGold; else -> MaterialTheme.colorScheme.surfaceVariant }
                Box(Modifier.size(24.dp).clip(CircleShape).background(color), contentAlignment = Alignment.Center) {
                    if (s < stage) Icon(Icons.Outlined.Check, null, Modifier.size(14.dp), tint = Color.White)
                    else if (s == stage && repeating) Icon(Icons.Outlined.Replay, null, Modifier.size(14.dp), tint = Color.White)
                }
                Text("${s}ª", style = MaterialTheme.typography.labelSmall, fontWeight = if (s == stage) FontWeight.Black else FontWeight.SemiBold, color = if (s == stage) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
    if (repeating) Text("Repetindo esta etapa: volta todo dia até você marcar Normal ou Fácil.", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = Color(0xFFB5560F), textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
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
    val zone = kotlinx.datetime.TimeZone.currentSystemDefault()
    val change = ReviewStageProgression.record(all.filter { it.topicId == review.topicId }.map { it.toStageState() }, review.id, difficulty, now, zone, correct, total) ?: return 1
    val due = (change.stages + listOfNotNull(change.newStage)).filter { it.pending }.minByOrNull { it.stage }?.dueAt ?: return 1
    return br.com.estudario.time.daysBetween(br.com.estudario.time.epochMillisToDate(now, zone), br.com.estudario.time.epochMillisToDate(due, zone)).coerceAtLeast(1)
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
        Text("Difícil repete amanhã sem avançar a etapa. Normal e Fácil avançam. A sugestão vem do seu desempenho; a decisão é sua.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        listOf(
            Triple(ReviewDifficulty.FACIL, "Fácil", "Recuperei quase tudo com segurança"),
            Triple(ReviewDifficulty.NORMAL, "Normal", "Lembrei depois de pensar um pouco"),
            Triple(ReviewDifficulty.DIFICIL, "Difícil", "Repito amanhã, na mesma etapa"),
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
        Button(onClick = { onDone(pick) }, Modifier.fillMaxWidth().padding(top = 4.dp)) { Text(if (pick == ReviewDifficulty.DIFICIL) "Salvar e repetir amanhã" else "Concluir etapa") }
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

/** Revisão concluída: quando o tópico volta e algumas questões dele agora, para fixar de verdade. */
@Composable
private fun ReviewDone(title: String, days: Long, available: Int, repeatedStage: Int?, onQuiz: () -> Unit, onGenerate: () -> Unit, onBack: () -> Unit) {
    Column(
        Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
    ) {
        br.com.estudario.ui.assistant.Folha(120.dp, mood = br.com.estudario.ui.assistant.FolhaMood.HAPPY)
        Text(if (repeatedStage != null) "SESSÃO REGISTRADA · ETAPA MANTIDA" else "ETAPA CONCLUÍDA", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Black, color = Color(0xFFC98A0B))
        // O XP da revisão, à vista: é o que dá peso a ela.
        br.com.estudario.ui.components.XpTag(br.com.estudario.domain.ProgressEngine.previewReview(), earned = true)
        Text(title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Black, textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.onSurface)
        Text(
            if (repeatedStage != null) "A ${repeatedStage}ª revisão volta amanhã. Você avança quando marcar Normal ou Fácil."
            else if (days <= 1L) "Ele volta amanhã para mais uma revisão." else "Ele volta em $days dias. Até lá, a memória trabalha por você.",
            style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center,
        )
        Row(
            Modifier.fillMaxWidth().clip(RoundedCornerShape(18.dp)).background(MaterialTheme.colorScheme.primaryContainer).padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(Modifier.weight(1f)) {
                Text("Fixe agora com questões", fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.onPrimaryContainer)
                Text(
                    if (available > 0) "Responder logo depois de revisar é o que mais segura o conteúdo." else "Este tópico ainda não tem questões. Gere um lote para treinar.",
                    style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f),
                )
            }
        }
        if (available > 0) Button(onClick = onQuiz, Modifier.fillMaxWidth()) { Text("Fazer ${minOf(5, available)} questões") }
        else Button(onClick = onGenerate, Modifier.fillMaxWidth()) { Text("Gerar questões") }
        TextButton(onClick = onBack) { Text("Voltar às revisões") }
    }
}

/**
 * O cartão de lembrar da revisão, igual ao do site: a pergunta na frente (papel claro), a resposta
 * no verso (índigo), virando em 3D. Tocar vira e desvira.
 */
@Composable
private fun ReviewFlipCard(question: String, answer: String, number: Int, total: Int, flipped: Boolean, onToggle: () -> Unit) {
    val angle by androidx.compose.animation.core.animateFloatAsState(if (flipped) 180f else 0f, androidx.compose.animation.core.spring(dampingRatio = 0.72f, stiffness = 260f), label = "rv-flip")
    val density = androidx.compose.ui.platform.LocalDensity.current.density
    val front = angle <= 90f
    val shape = RoundedCornerShape(24.dp)
    Box(
        Modifier.fillMaxWidth().heightIn(min = 260.dp)
            .graphicsLayer { rotationY = angle; cameraDistance = 14f * density }
            .clip(shape)
            .background(if (front) MaterialTheme.colorScheme.surface else Color(0xFF231C86))
            .then(if (front) Modifier.border(2.dp, MaterialTheme.colorScheme.outlineVariant, shape) else Modifier)
            .clickable(onClick = onToggle)
            .semantics { contentDescription = if (flipped) "Voltar à pergunta" else "Ver a resposta" },
        contentAlignment = Alignment.Center,
    ) {
        Column(
            Modifier.fillMaxWidth().padding(horizontal = 22.dp, vertical = 26.dp).graphicsLayer { rotationY = if (front) 0f else 180f },
            horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            if (front) {
                Text("PERGUNTA $number DE $total", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Black, letterSpacing = 1.4.sp, color = Color(0xFFC98A0B))
                br.com.estudario.ui.components.StudyInlineText(question, style = MaterialTheme.typography.titleLarge.copy(textAlign = TextAlign.Center, lineHeight = 28.sp), fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.onSurface)
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Icon(Icons.Outlined.TouchApp, null, Modifier.size(16.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("Toque para ver a resposta", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            } else {
                Text("RESPOSTA", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Black, letterSpacing = 1.4.sp, color = Color(0xFFFFB421))
                // o verso é índigo nos dois temas: o texto da resposta vai em branco
                val scheme = MaterialTheme.colorScheme
                MaterialTheme(colorScheme = scheme.copy(onSurface = Color.White, onSurfaceVariant = Color(0xFFD9D5FF), onBackground = Color.White, primary = Color(0xFFFFD27A))) {
                    androidx.compose.runtime.CompositionLocalProvider(androidx.compose.material3.LocalContentColor provides Color.White) {
                        if (answer.isBlank()) Text("Este material não trouxe a resposta. Confira na teoria do tópico.", color = Color.White)
                        else MarkdownText(answer)
                    }
                }
            }
        }
    }
}
