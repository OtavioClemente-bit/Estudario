package br.com.estudario.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.Bolt
import androidx.compose.material.icons.outlined.ExpandLess
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FiberNew
import androidx.compose.material.icons.outlined.HelpOutline
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.Replay
import androidx.compose.material.icons.outlined.Shuffle
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material.icons.outlined.TrendingDown
import androidx.compose.material.icons.outlined.TrendingUp
import androidx.compose.material.icons.outlined.WarningAmber
import androidx.compose.material3.*
import br.com.estudario.ui.brand.FilterChip
import br.com.estudario.ui.brand.Surface
import br.com.estudario.ui.brand.Button
import br.com.estudario.ui.brand.Icon
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import br.com.estudario.data.prompt.QuestionDifficulty
import br.com.estudario.ui.AppViewModel
import br.com.estudario.ui.studio.gxClick
import br.com.estudario.ui.components.EmptyState
import br.com.estudario.ui.components.ScreenTitle
import br.com.estudario.ui.prompt.BigValueSlider
import br.com.estudario.ui.prompt.ChoiceCard
import br.com.estudario.ui.prompt.DifficultySelector
import br.com.estudario.ui.theme.EstudarioShapes
import br.com.estudario.ui.theme.estudarioColors
import br.com.estudario.ui.theme.screenPadding
import br.com.estudario.ui.tour.TourKey
import br.com.estudario.ui.tour.tourTarget

data class QuizConfig(
    val count: Int,
    val topicId: Long? = null,
    val subjectId: Long? = null,
    val mode: String = "random",
    val board: String? = null,
    val difficulty: String? = null,
    val planTaskId: String? = null,
)

/** Um modo de treino e para que ele serve, em uma frase. */
private data class TrainMode(val key: String, val title: String, val description: String, val icon: ImageVector)

private val trainModes = listOf(
    TrainMode("smart", "Treino inteligente", "Mistura seus erros, os tópicos mais fracos e questões novas. O melhor para o dia a dia.", Icons.Outlined.AutoAwesome),
    TrainMode("errors", "Refazer erros", "Só as questões que você errou, para fechar as lacunas.", Icons.Outlined.Replay),
    TrainMode("never_correct", "Nunca acertadas", "As que você ainda não acertou nenhuma vez.", Icons.Outlined.WarningAmber),
    TrainMode("most_errors", "Mais erradas", "As que você mais errou. Onde está o ponto fraco de verdade.", Icons.Outlined.TrendingDown),
    TrainMode("new", "Questões novas", "Só as que você nunca respondeu. Bom para avançar no conteúdo.", Icons.Outlined.FiberNew),
    TrainMode("favorites", "Favoritas", "As que você marcou com o coração para rever.", Icons.Outlined.Favorite),
    TrainMode("random", "Aleatórias", "Sorteio livre, sem critério.", Icons.Outlined.Shuffle),
)

/** Treino pronto que a tela sugere a partir do desempenho, com o motivo escrito. */
private data class Recommendation(val title: String, val reason: String, val icon: ImageVector, val accent: Color, val config: QuizConfig)

/**
 * Treinar: primeiro o diagnóstico (onde a pessoa está), depois o que fazer agora (treinos
 * recomendados com o motivo), o acerto por matéria e, por último, o treino montado à mão.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TrainScreen(viewModel: AppViewModel, onStart: (QuizConfig) -> Unit, onHelp: () -> Unit = {}, onSimulations: () -> Unit = {}) {
    val competitions by viewModel.competitions.collectAsState()
    val subjects by viewModel.subjects.collectAsState()
    val topics by viewModel.topics.collectAsState()
    val questions by viewModel.questions.collectAsState()
    val defaultCount by viewModel.defaultQuestionCount.collectAsState()
    val primaryId = competitions.firstOrNull { it.isPrimary }?.id ?: competitions.firstOrNull()?.id
    val availableSubjects = subjects.filter { it.competitionId == primaryId }
    val colors = estudarioColors()

    var subjectId by rememberSaveable { mutableStateOf<Long?>(null) }
    var topicId by rememberSaveable { mutableStateOf<Long?>(null) }
    var count by rememberSaveable(defaultCount) { mutableIntStateOf(defaultCount.coerceIn(5, 50)) }
    var mode by rememberSaveable { mutableStateOf("smart") }
    var board by rememberSaveable { mutableStateOf<String?>(null) }
    var difficulty by rememberSaveable { mutableStateOf(QuestionDifficulty.MIXED) }
    var builderOpen by rememberSaveable { mutableStateOf(true) }

    val subjectByTopic = remember(topics) { topics.associate { it.id to it.subjectId } }
    val answered = questions.sumOf { it.question.answerCount }
    val correct = questions.sumOf { it.question.correctCount }
    val accuracy = if (answered == 0) null else correct * 100 / answered
    val pendingErrors = questions.count { it.question.errorCount > 0 && it.question.correctCount < it.question.errorCount }
    val neverAnswered = questions.count { it.question.answerCount == 0 }
    val perSubject = remember(questions, availableSubjects, subjectByTopic) {
        availableSubjects.mapNotNull { subject ->
            val own = questions.filter { subjectByTopic[it.question.topicId] == subject.id }
            if (own.isEmpty()) return@mapNotNull null
            val a = own.sumOf { it.question.answerCount }
            val c = own.sumOf { it.question.correctCount }
            SubjectScore(subject.id, subject.name, own.size, a, if (a == 0) null else c * 100 / a)
        }
    }
    val weakest = perSubject.filter { it.answered >= 5 && it.accuracy != null }.minByOrNull { it.accuracy!! }
    val recommendations = buildList {
        if (pendingErrors > 0) add(Recommendation("Refazer seus erros", "$pendingErrors questões em que você mais erra do que acerta.", Icons.Outlined.Replay, MaterialTheme.colorScheme.error, QuizConfig(pendingErrors.coerceIn(5, 20), mode = "errors")))
        if (weakest != null && (weakest.accuracy ?: 100) < 70) add(Recommendation("Reforçar ${weakest.name}", "Sua matéria mais fraca: ${weakest.accuracy}% de acerto em ${weakest.answered} respostas.", Icons.Outlined.TrendingDown, colors.attention, QuizConfig(15, subjectId = weakest.id, mode = "smart")))
        add(Recommendation("Desafio do dia", "10 questões entre erros recorrentes, revisões atrasadas e tópicos de menor domínio.", Icons.Outlined.Bolt, MaterialTheme.colorScheme.primary, QuizConfig(10, mode = "daily")))
        if (neverAnswered > 0 && size < 3) add(Recommendation("Avançar com questões novas", "$neverAnswered questões que você ainda não viu.", Icons.Outlined.Explore, colors.completed, QuizConfig(10, mode = "new")))
    }.take(3)
    val boards = questions.mapNotNull { it.question.board?.takeIf(String::isNotBlank) }.distinct().sorted()

    val tourStep by viewModel.tourStep.collectAsState()
    val listState = rememberLazyListState()
    LaunchedEffect(tourStep?.key) {
        when (tourStep?.key) {
            TourKey.TRAIN_DAILY -> listState.animateScrollToItem(0)
            TourKey.TRAIN_MODES, TourKey.TRAIN_START -> { builderOpen = true; listState.animateScrollToItem((listState.layoutInfo.totalItemsCount - 1).coerceAtLeast(0)) }
            else -> Unit
        }
    }
    fun target(key: TourKey) = Modifier.tourTarget(key, tourStep?.key) { viewModel.reportTourTargetBounds(key, it) }

    LazyColumn(Modifier.fillMaxSize(), state = listState, contentPadding = screenPadding(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        item { ScreenTitle("Treinar", "Questões que mudam o seu estudo", stackActionsWhenNarrow = false) { IconButton(onClick = onHelp) { Icon(Icons.Outlined.HelpOutline, "Como treinar") } } }
        if (questions.isEmpty()) {
            item { EmptyState("Sem questões ainda", "Na aba Concursos, abra um tópico e gere o material com questões. Elas aparecem aqui para treinar.") }
            return@LazyColumn
        }

        // Diagnóstico enxuto (como no site): o acerto geral num medidor, até três treinos com o motivo
        // (o primeiro em destaque) e as matérias da mais fraca para a mais forte, quatro à vista.
        item {
            val recs = buildList {
                if (pendingErrors > 0) add(DiagRec(br.com.estudario.ui.brand.Glyph.Cycle, br.com.estudario.ui.studio.Gx.red, "Refazer seus erros", "$pendingErrors questões em que você mais erra do que acerta", QuizConfig(pendingErrors.coerceIn(5, 20), mode = "errors")))
                if (weakest != null && (weakest.accuracy ?: 100) < 70) add(DiagRec(br.com.estudario.ui.brand.Glyph.Target, br.com.estudario.ui.studio.Gx.gold, "Reforçar ${weakest.name}", "Sua matéria mais fraca: ${weakest.accuracy}% de acerto", QuizConfig(15, subjectId = weakest.id, mode = "smart")))
                add(DiagRec(br.com.estudario.ui.brand.Glyph.Bolt, br.com.estudario.ui.studio.Gx.violet, "Desafio do dia", "10 questões entre erros, revisões atrasadas e pontos fracos", QuizConfig(10, mode = "daily")))
                if (neverAnswered > 0) add(DiagRec(br.com.estudario.ui.brand.Glyph.Star, br.com.estudario.ui.studio.Gx.mint, "Questões novas", "$neverAnswered que você ainda não viu", QuizConfig(10, mode = "new")))
            }.take(3)
            DiagnosisPanel(accuracy, answered, perSubject, recs, { rec -> if (rec.config.mode == "daily") target(TourKey.TRAIN_DAILY) else Modifier }, onStart)
        }

        // Montar o treino: o mesmo estúdio de gerar material (como no site), num painel de console.
        item {
            br.com.estudario.ui.studio.GxPanel {
                Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    Column(Modifier.padding(horizontal = 4.dp, vertical = 4.dp)) {
                        br.com.estudario.ui.studio.GxKicker("Estúdio de treino")
                        Text("Monte o seu treino", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Black, color = br.com.estudario.ui.studio.Gx.strong)
                    }
                    Box(target(TourKey.TRAIN_MODES)) {
                        br.com.estudario.ui.studio.GxSection(br.com.estudario.ui.brand.Glyph.Target, "Modo", "Como as questões vão ser escolhidas", br.com.estudario.ui.studio.Gx.violet) {
                            br.com.estudario.ui.studio.GxTiles(trainModes.map { br.com.estudario.ui.studio.GxOption(it.key, it.title, it.description, modeGlyph(it.key), modeColor(it.key)) }, { it == mode }, columns = 2, multi = false) { mode = it }
                        }
                    }
                    br.com.estudario.ui.studio.GxSection(br.com.estudario.ui.brand.Glyph.Books, "Matéria", null, br.com.estudario.ui.studio.Gx.cyan) {
                        br.com.estudario.ui.studio.GxPicker(listOf<Pair<Long?, String>>(null to "Todas") + availableSubjects.map { it.id to it.name }, subjectId, searchHint = "Buscar matéria") { subjectId = it; topicId = null }
                        if (subjectId != null) {
                            br.com.estudario.ui.studio.GxLabel("Tópico")
                            br.com.estudario.ui.studio.GxPicker(listOf<Pair<Long?, String>>(null to "Todos") + topics.filter { it.subjectId == subjectId }.map { it.id to it.title }, topicId, limit = 6, searchHint = "Buscar tópico") { topicId = it }
                        }
                    }
                    br.com.estudario.ui.studio.GxSection(br.com.estudario.ui.brand.Glyph.Bolt, "Dificuldade", "Do básico às pegadinhas", br.com.estudario.ui.studio.Gx.gold) {
                        DifficultySelector(difficulty) { difficulty = it }
                    }
                    if (boards.isNotEmpty()) br.com.estudario.ui.studio.GxSection(br.com.estudario.ui.brand.Glyph.Colunas, "Banca", null, br.com.estudario.ui.studio.Gx.blue) {
                        br.com.estudario.ui.studio.GxPicker(listOf<Pair<String?, String>>(null to "Todas") + boards.map { it to it }, board, limit = 6, searchHint = "Buscar banca") { board = it }
                    }
                    br.com.estudario.ui.studio.GxSection(br.com.estudario.ui.brand.Glyph.Clipboard, "Quantidade", "Questões nesta sessão", br.com.estudario.ui.studio.Gx.mint) {
                        br.com.estudario.ui.studio.GxCounter(count, { count = it }, 5..50, step = 5, unit = "questões", quick = listOf(10, 20, 30, 50))
                    }
                    val summary = listOfNotNull(trainModes.firstOrNull { it.key == mode }?.title, availableSubjects.firstOrNull { it.id == subjectId }?.name, board, difficulty.label.takeIf { difficulty != QuestionDifficulty.MIXED }, "$count questões").joinToString(" · ")
                    Column(Modifier.padding(horizontal = 4.dp)) {
                        br.com.estudario.ui.studio.GxKicker("Seu treino")
                        Text(summary, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = br.com.estudario.ui.studio.Gx.strong, maxLines = 2, overflow = TextOverflow.Ellipsis)
                    }
                    Box(target(TourKey.TRAIN_START)) {
                        br.com.estudario.ui.studio.GxLaunch(
                            when (mode) { "smart" -> "Iniciar treino inteligente"; else -> "Começar treino" },
                            glyph = br.com.estudario.ui.brand.Glyph.Play,
                        ) {
                            val level = when (difficulty) { QuestionDifficulty.EASY -> "FACIL"; QuestionDifficulty.MEDIUM -> "MEDIA"; QuestionDifficulty.HARD -> "DIFICIL"; QuestionDifficulty.MIXED -> null }
                            onStart(QuizConfig(count, topicId, subjectId, mode, board, level))
                        }
                    }
                }
            }
        }
    }
}

/** O desenho da turma de cada modo de treino. */
private fun modeGlyph(key: String): br.com.estudario.ui.brand.Glyph = when (key) {
    "smart" -> br.com.estudario.ui.brand.Glyph.Brain
    "errors" -> br.com.estudario.ui.brand.Glyph.Cycle
    "never_correct" -> br.com.estudario.ui.brand.Glyph.Warning
    "most_errors" -> br.com.estudario.ui.brand.Glyph.Target
    "new" -> br.com.estudario.ui.brand.Glyph.Star
    "favorites" -> br.com.estudario.ui.brand.Glyph.Heart
    else -> br.com.estudario.ui.brand.Glyph.Dice
}

private fun modeColor(key: String): Color = when (key) {
    "smart" -> br.com.estudario.ui.studio.Gx.violet
    "errors" -> br.com.estudario.ui.studio.Gx.red
    "never_correct" -> br.com.estudario.ui.studio.Gx.orange
    "most_errors" -> br.com.estudario.ui.studio.Gx.pink
    "new" -> br.com.estudario.ui.studio.Gx.mint
    "favorites" -> br.com.estudario.ui.studio.Gx.gold
    else -> br.com.estudario.ui.studio.Gx.cyan
}

/** Um treino sugerido no diagnóstico. */
private data class DiagRec(val glyph: br.com.estudario.ui.brand.Glyph, val color: Color, val title: String, val reason: String, val config: QuizConfig)

private fun accuracyTone(accuracy: Int?): Color = when {
    accuracy == null -> br.com.estudario.ui.studio.Gx.mute
    accuracy < 50 -> br.com.estudario.ui.studio.Gx.red
    accuracy < 70 -> br.com.estudario.ui.studio.Gx.gold
    else -> br.com.estudario.ui.studio.Gx.mint
}

@Composable
private fun DiagnosisPanel(
    accuracy: Int?,
    answered: Int,
    perSubject: List<SubjectScore>,
    recs: List<DiagRec>,
    recModifier: (DiagRec) -> Modifier,
    onStart: (QuizConfig) -> Unit,
) {
    var allSubjects by rememberSaveable { mutableStateOf(false) }
    val tone = accuracyTone(accuracy)
    val fill by androidx.compose.animation.core.animateFloatAsState((accuracy ?: 0) / 100f, androidx.compose.animation.core.tween(900), label = "diag")
    br.com.estudario.ui.studio.GxPanel {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.size(96.dp), contentAlignment = Alignment.Center) {
                    androidx.compose.foundation.Canvas(Modifier.matchParentSize()) {
                        val stroke = 11.dp.toPx()
                        val inset = stroke / 2
                        val arc = androidx.compose.ui.geometry.Size(size.width - stroke, size.height - stroke)
                        val tl = androidx.compose.ui.geometry.Offset(inset, inset)
                        drawArc(br.com.estudario.ui.studio.Gx.tint(0.08f), -90f, 360f, false, tl, arc, style = androidx.compose.ui.graphics.drawscope.Stroke(stroke))
                        drawArc(tone, -90f, 360f * fill, false, tl, arc, style = androidx.compose.ui.graphics.drawscope.Stroke(stroke, cap = androidx.compose.ui.graphics.StrokeCap.Round))
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(accuracy?.let { "$it%" } ?: "—", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black, color = br.com.estudario.ui.studio.Gx.strong)
                        Text("de acerto", style = MaterialTheme.typography.labelSmall, color = br.com.estudario.ui.studio.Gx.mute)
                    }
                }
                Spacer(Modifier.width(14.dp))
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    br.com.estudario.ui.studio.GxKicker("Seu diagnóstico")
                    Text(
                        when { accuracy == null -> "Resolva as primeiras questões"; accuracy >= 70 -> "Você está no ritmo da aprovação"; accuracy >= 50 -> "Bom caminho, dá para subir"; else -> "Hora de reforçar a base" },
                        style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black, color = br.com.estudario.ui.studio.Gx.strong,
                    )
                    Text(
                        if (answered == 0) "O diagnóstico mostra seu acerto por matéria assim que você responder." else "$answered respostas em ${perSubject.count { it.answered > 0 }} matérias",
                        style = MaterialTheme.typography.bodySmall, color = br.com.estudario.ui.studio.Gx.mute,
                    )
                }
            }
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                recs.forEachIndexed { i, rec ->
                    val top = i == 0
                    val shape = RoundedCornerShape(18.dp)
                    Row(
                        recModifier(rec).fillMaxWidth().then(Modifier.gxClick { onStart(rec.config) }).clip(shape)
                            .background(if (top) androidx.compose.ui.graphics.Brush.linearGradient(listOf(rec.color.copy(alpha = 0.24f), br.com.estudario.ui.studio.Gx.tint(0.03f))) else androidx.compose.ui.graphics.SolidColor(br.com.estudario.ui.studio.Gx.tint(0.04f)))
                            .border(if (top) 1.5.dp else 1.dp, if (top) rec.color else br.com.estudario.ui.studio.Gx.tint(0.08f), shape)
                            .padding(horizontal = 12.dp, vertical = if (top) 14.dp else 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Box(Modifier.size(if (top) 48.dp else 40.dp).clip(RoundedCornerShape(13.dp)).background(rec.color.copy(alpha = 0.18f)), contentAlignment = Alignment.Center) {
                            br.com.estudario.ui.brand.BrandIcon(rec.glyph, size = if (top) 38.dp else 32.dp)
                        }
                        Spacer(Modifier.width(12.dp))
                        Column(Modifier.weight(1f)) {
                            if (top) Text(
                                "RECOMENDADO AGORA", Modifier.padding(bottom = 3.dp).clip(RoundedCornerShape(6.dp)).background(rec.color).padding(horizontal = 7.dp, vertical = 1.dp),
                                style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Black, color = Color(0xFF1A1440),
                            )
                            Text(rec.title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Black, color = br.com.estudario.ui.studio.Gx.strong, maxLines = 2, overflow = TextOverflow.Ellipsis)
                            Text(rec.reason, style = MaterialTheme.typography.bodySmall, color = br.com.estudario.ui.studio.Gx.mute, maxLines = 2, overflow = TextOverflow.Ellipsis)
                        }
                        Spacer(Modifier.width(10.dp))
                        Box(
                            Modifier.size(if (top) 46.dp else 36.dp).clip(CircleShape).background(if (top) rec.color else br.com.estudario.ui.studio.Gx.tint(0.08f)),
                            contentAlignment = Alignment.Center,
                        ) { Icon(Icons.Outlined.PlayArrow, "Começar", Modifier.size(if (top) 26.dp else 20.dp), tint = if (top) Color(0xFF1A1440) else Color.White) }
                    }
                }
            }
            if (perSubject.isNotEmpty()) {
                val ranked = perSubject.sortedBy { it.accuracy ?: 101 }
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    br.com.estudario.ui.studio.GxLabel("Por matéria · da mais fraca para a mais forte")
                    (if (allSubjects) ranked else ranked.take(4)).forEach { score ->
                        val c = accuracyTone(score.accuracy)
                        Row(
                            Modifier.fillMaxWidth().gxClick { onStart(QuizConfig(15, subjectId = score.id, mode = "smart")) }.clip(RoundedCornerShape(12.dp))
                                .background(br.com.estudario.ui.studio.Gx.tint(0.03f)).padding(horizontal = 10.dp, vertical = 9.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(score.name, Modifier.weight(1f), style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold, color = br.com.estudario.ui.studio.Gx.strong, maxLines = 1, overflow = TextOverflow.Ellipsis)
                            Spacer(Modifier.width(8.dp))
                            Box(Modifier.width(80.dp).height(6.dp).clip(CircleShape).background(br.com.estudario.ui.studio.Gx.tint(0.08f))) {
                                Box(Modifier.fillMaxWidth(((score.accuracy ?: 0) / 100f).coerceIn(0.03f, 1f)).fillMaxHeight().clip(CircleShape).background(c))
                            }
                            Spacer(Modifier.width(8.dp))
                            Text(score.accuracy?.let { "$it%" } ?: "—", Modifier.width(40.dp), style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Black, color = br.com.estudario.ui.studio.Gx.readable(c), textAlign = androidx.compose.ui.text.style.TextAlign.End)
                        }
                    }
                    if (ranked.size > 4) TextButton(onClick = { allSubjects = !allSubjects }) {
                        Text(if (allSubjects) "Mostrar menos" else "Ver todas as ${ranked.size} matérias", color = br.com.estudario.ui.studio.Gx.accentText, fontWeight = FontWeight.Black)
                        Icon(if (allSubjects) Icons.Outlined.ExpandLess else Icons.Outlined.ExpandMore, null, tint = br.com.estudario.ui.studio.Gx.accentText)
                    }
                }
            }
        }
    }
}

private data class SubjectScore(val id: Long, val name: String, val total: Int, val answered: Int, val accuracy: Int?)

@Composable
private fun accuracyColor(accuracy: Int?): Color {
    val colors = estudarioColors()
    return when {
        accuracy == null -> MaterialTheme.colorScheme.outline
        accuracy >= 75 -> colors.completed
        accuracy >= 55 -> colors.attention
        else -> MaterialTheme.colorScheme.error
    }
}

@Composable
private fun SectionTitle(title: String, subtitle: String) {
    Column(Modifier.padding(top = 4.dp)) {
        Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Text(subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun DiagnosisLine(value: String, label: String) {
    Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimaryContainer)
        Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onPrimaryContainer)
    }
}

@Composable
private fun DiagnosisStat(value: String, label: String, modifier: Modifier) {
    Column(modifier, horizontalAlignment = Alignment.Start) {
        Text(value, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimaryContainer)
        Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = .8f), maxLines = 1, overflow = TextOverflow.Ellipsis)
    }
}

/**
 * Cartão de treino sugerido: ícone em bloco na cor do tipo de treino, o porquê da sugestão e a
 * duração estimada. O cartão inteiro é o botão; a seta só indica que abre.
 */
@Composable
private fun RecommendationCard(recommendation: Recommendation, modifier: Modifier = Modifier, onStart: () -> Unit) {
    Surface(
        onClick = onStart,
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surfaceContainerLow,
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = .6f)),
        modifier = modifier.fillMaxWidth(),
    ) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(14.dp)) {
            Box(Modifier.size(48.dp), contentAlignment = Alignment.Center) { Icon(recommendation.icon, null, Modifier.size(43.dp), tint = recommendation.accent) }
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                Text(recommendation.title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, maxLines = 2, overflow = TextOverflow.Ellipsis)
                Text(recommendation.reason, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 4.dp)) {
                    MetaPill("${recommendation.config.count} questões", recommendation.accent)
                    Spacer(Modifier.width(6.dp))
                    MetaPill("~${(recommendation.config.count * 1.5).toInt()} min", MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            Icon(Icons.AutoMirrored.Outlined.ArrowForward, "Começar", tint = MaterialTheme.colorScheme.outline)
        }
    }
}

@Composable
private fun MetaPill(text: String, color: Color) {
    Surface(shape = RoundedCornerShape(50), color = color.copy(alpha = .1f)) {
        Text(text, Modifier.padding(horizontal = 8.dp, vertical = 2.dp), style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.SemiBold, color = br.com.estudario.ui.studio.Gx.readable(color))
    }
}
