package br.com.estudario.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
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
    TrainMode("simulation", "Simulado", "Como na prova: sem correção a cada questão, resultado só no final.", Icons.Outlined.Timer),
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
    var builderOpen by rememberSaveable { mutableStateOf(false) }

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
        // Simulado antes de tudo: o diagnóstico funciona até para quem ainda não tem questões.
        item {
            Surface(onClick = onSimulations, shape = EstudarioShapes.spotlight, color = MaterialTheme.colorScheme.tertiaryContainer, modifier = Modifier.fillMaxWidth()) {
                Row(Modifier.padding(18.dp), verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text("SIMULADOS", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.tertiary)
                        Text("Prova inédita no estilo da sua banca", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onTertiaryContainer)
                        Text("Com cronômetro, gabarito no final e nota por matéria. Comece pelo diagnóstico.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onTertiaryContainer)
                    }
                    Icon(Icons.AutoMirrored.Outlined.ArrowForward, null, tint = MaterialTheme.colorScheme.onTertiaryContainer)
                }
            }
        }
        if (questions.isEmpty()) {
            item { EmptyState("Sem questões ainda", "Na aba Concursos, abra um tópico e gere o material com questões. Elas aparecem aqui para treinar.") }
            return@LazyColumn
        }

        // Diagnóstico: o ponto de partida de qualquer treino com sentido.
        item {
            Surface(shape = EstudarioShapes.spotlight, color = MaterialTheme.colorScheme.primaryContainer, modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    Text("SEU DIAGNÓSTICO", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    // Quatro números alinhados, todos com o mesmo peso: nada de traço solto quando ainda
                    // não há respostas, a frase embaixo explica.
                    Row(Modifier.fillMaxWidth()) {
                        DiagnosisStat(accuracy?.let { "$it%" } ?: "0%", "acerto", Modifier.weight(1f))
                        DiagnosisStat("$answered", "respostas", Modifier.weight(1f))
                        DiagnosisStat("$pendingErrors", "a refazer", Modifier.weight(1f))
                        DiagnosisStat("$neverAnswered", "inéditas", Modifier.weight(1f))
                    }
                    if (accuracy == null) Text("Resolva as primeiras questões para o diagnóstico mostrar seu acerto por matéria.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onPrimaryContainer)
                    if (weakest != null) Text(
                        "Ponto fraco agora: ${weakest.name} (${weakest.accuracy}% de acerto).",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                }
            }
        }

        item { SectionTitle("Recomendado para você", "Comece por aqui: cada treino diz por que foi sugerido.") }
        recommendations.forEachIndexed { index, recommendation ->
            item(key = "rec-$index") {
                RecommendationCard(recommendation, Modifier.then(if (recommendation.config.mode == "daily") target(TourKey.TRAIN_DAILY) else Modifier)) { onStart(recommendation.config) }
            }
        }

        if (perSubject.isNotEmpty()) {
            item { SectionTitle("Onde você está", "Toque numa matéria para treinar só ela.") }
            item {
                Surface(shape = EstudarioShapes.panel, color = MaterialTheme.colorScheme.surfaceContainerLow, modifier = Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(vertical = 8.dp)) {
                        perSubject.sortedBy { it.accuracy ?: 101 }.forEach { score ->
                            Surface(onClick = { onStart(QuizConfig(15, subjectId = score.id, mode = "smart")) }, color = Color.Transparent, modifier = Modifier.fillMaxWidth()) {
                                Column(Modifier.padding(horizontal = 16.dp, vertical = 10.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(score.name, Modifier.weight(1f), style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                        Text(score.accuracy?.let { "$it%" } ?: "sem respostas", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.SemiBold, color = accuracyColor(score.accuracy))
                                    }
                                    Box(Modifier.fillMaxWidth().height(8.dp).clip(CircleShape).background(MaterialTheme.colorScheme.surfaceVariant)) {
                                        Box(Modifier.fillMaxWidth(((score.accuracy ?: 0) / 100f).coerceIn(0.02f, 1f)).fillMaxHeight().clip(CircleShape).background(accuracyColor(score.accuracy)))
                                    }
                                    Text("${score.total} questões · ${score.answered} respostas", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }
                        }
                    }
                }
            }
        }

        // Treino montado à mão: para quem sabe o que quer.
        item {
            Surface(onClick = { builderOpen = !builderOpen }, shape = EstudarioShapes.panel, color = MaterialTheme.colorScheme.surfaceContainerLow, modifier = Modifier.fillMaxWidth()) {
                Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f)) {
                        Text("Montar meu treino", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                        Text("Escolha o modo, a matéria, a banca, o nível e a quantidade.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Icon(if (builderOpen) Icons.Outlined.ExpandLess else Icons.Outlined.ExpandMore, if (builderOpen) "Recolher" else "Abrir")
                }
            }
        }
        if (builderOpen) {
            item {
                Column(target(TourKey.TRAIN_MODES), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Modo", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                    trainModes.forEach { option -> ChoiceCard(option.title, mode == option.key, { mode = option.key }, description = option.description, icon = option.icon) }
                }
            }
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Matéria", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                    Row(Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        FilterChip(subjectId == null, { subjectId = null; topicId = null }, label = { Text("Todas") })
                        availableSubjects.forEach { subject -> FilterChip(subjectId == subject.id, { subjectId = subject.id; topicId = null }, label = { Text(subject.name, maxLines = 1, overflow = TextOverflow.Ellipsis) }) }
                    }
                    if (subjectId != null) {
                        Text("Tópico", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                        Row(Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            FilterChip(topicId == null, { topicId = null }, label = { Text("Todos") })
                            topics.filter { it.subjectId == subjectId }.forEach { topic -> FilterChip(topicId == topic.id, { topicId = topic.id }, label = { Text(topic.title, maxLines = 1, overflow = TextOverflow.Ellipsis) }) }
                        }
                    }
                    if (boards.isNotEmpty()) {
                        Text("Banca", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                        Row(Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            FilterChip(board == null, { board = null }, label = { Text("Todas") })
                            boards.forEach { value -> FilterChip(board == value, { board = value }, label = { Text(value) }) }
                        }
                    }
                }
            }
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Nível", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                    DifficultySelector(difficulty) { difficulty = it }
                }
            }
            item {
                BigValueSlider(count, 5..50, 5, { "$it" }, { count = it }, caption = "questões nesta sessão", quickValues = listOf(10, 20, 30, 50))
            }
            item {
                Button(
                    onClick = {
                        val level = when (difficulty) { QuestionDifficulty.EASY -> "FACIL"; QuestionDifficulty.MEDIUM -> "MEDIA"; QuestionDifficulty.HARD -> "DIFICIL"; QuestionDifficulty.MIXED -> null }
                        onStart(QuizConfig(count, topicId, subjectId, mode, board, level))
                    },
                    modifier = Modifier.fillMaxWidth().heightIn(min = 54.dp).then(target(TourKey.TRAIN_START)),
                ) {
                    Icon(Icons.Outlined.PlayArrow, null); Spacer(Modifier.width(8.dp))
                    Text(when (mode) { "simulation" -> "Iniciar simulado"; "smart" -> "Iniciar treino inteligente"; else -> "Começar treino" })
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
        Text(text, Modifier.padding(horizontal = 8.dp, vertical = 2.dp), style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.SemiBold, color = color)
    }
}
