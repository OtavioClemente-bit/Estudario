package br.com.estudario.ui.screens

import br.com.estudario.ui.theme.screenPadding
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material.icons.outlined.Close
import androidx.compose.ui.draw.alpha
import androidx.compose.material.icons.outlined.Undo
import androidx.compose.material.icons.outlined.StrikethroughS
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.ui.draw.clip
import br.com.estudario.ui.components.offerUndo
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.*
import br.com.estudario.ui.brand.LinearProgressIndicator
import br.com.estudario.ui.brand.Surface
import br.com.estudario.ui.brand.ElevatedCard
import br.com.estudario.ui.brand.FilledTonalButton
import br.com.estudario.ui.brand.OutlinedButton
import br.com.estudario.ui.brand.Button
import br.com.estudario.ui.brand.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.estudario.data.local.QuestionWithOptions
import br.com.estudario.data.local.QuestionSessionEntity
import br.com.estudario.data.planner.CompleteTaskInput
import br.com.estudario.domain.SessionTypeMapper
import br.com.estudario.domain.ProgressEngine
import br.com.estudario.domain.planner.PlanTaskType
import br.com.estudario.ui.AppViewModel
import br.com.estudario.ui.components.EmptyState
import br.com.estudario.ui.components.MarkdownText
import br.com.estudario.ui.components.QuestionProvenance
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import java.util.UUID

@Composable
fun QuizScreen(
    viewModel: AppViewModel,
    config: QuizConfig,
    onBack: () -> Unit,
    onOpenTopic: (Long) -> Unit,
    onPlanTaskComplete: suspend (String, CompleteTaskInput) -> Unit = { _, _ -> },
) {
    val allQuestions by viewModel.questions.collectAsState()
    val topics by viewModel.topics.collectAsState()
    val errors by viewModel.errors.collectAsState()
    val timerEnabled by viewModel.questionTimer.collectAsState()
    val showExplanation by viewModel.showExplanation.collectAsState()
    val scope = rememberCoroutineScope()
    // Tudo abaixo é rememberSaveable de propósito: abrir o resumo de um tópico empilha outra tela
    // por cima do quiz e antes disso a sessão inteira se perdia, voltava na questão 1, sem as
    // respostas já dadas. Agora a sessão sobrevive a sair, voltar e até o app ser recriado.
    val sessionId = rememberSaveable { UUID.randomUUID().toString() }
    val startedAt = rememberSaveable { System.currentTimeMillis() }
    var elapsedSeconds by remember { mutableLongStateOf(0L) }
    var finished by rememberSaveable { mutableStateOf(false) }
    var finishing by rememberSaveable { mutableStateOf(false) }
    var finishError by rememberSaveable { mutableStateOf<String?>(null) }
    var planTaskXp by rememberSaveable { mutableIntStateOf(-1) }
    var questionIdsText by rememberSaveable { mutableStateOf("") }
    val questionIds = remember(questionIdsText) { questionIdsText.split(",").mapNotNull(String::toLongOrNull) }
    val eligible = allQuestions.filter { item ->
        val topic = topics.firstOrNull { it.id == item.question.topicId }
        !item.question.isHidden &&
        (config.topicId == null || item.question.topicId == config.topicId) &&
            (config.subjectId == null || topic?.subjectId == config.subjectId) &&
            (config.board == null || item.question.board == config.board) &&
            (config.difficulty == null || item.question.difficulty?.name == config.difficulty) &&
            when (config.mode) {
                "new" -> item.question.answerCount == 0
                "errors", "most_errors" -> item.question.errorCount > 0 || errors.any { it.entry.questionId == item.question.id }
                "never_correct" -> item.question.correctCount == 0
                "favorites" -> item.question.isFavorite
                else -> true
            }
    }
    LaunchedEffect(config, eligible.map { it.question.id }) {
        if (questionIdsText.isNotBlank()) return@LaunchedEffect
        val sorteadas = when (config.mode) {
            "smart", "daily" -> viewModel.smartQuestions(config.count).map { it.question.id }
            "most_errors" -> eligible.sortedByDescending { it.question.errorCount }.take(config.count).map { it.question.id }
            // Treinar erros respeita a escada de reencontro: primeiro o que já venceu (3, 10, 30
            // dias), depois o que ainda não venceu, e por último o que já está dominado.
            "errors" -> {
                val agenda = errors.associate { it.entry.questionId to (it.entry.nextRetryAt ?: Long.MAX_VALUE) }
                eligible.sortedWith(compareBy({ agenda[it.question.id] ?: Long.MAX_VALUE }, { -it.question.errorCount }))
                    .take(config.count).map { it.question.id }
            }
            else -> eligible.shuffled().take(config.count).map { it.question.id }
        }
        if (sorteadas.isNotEmpty()) questionIdsText = sorteadas.joinToString(",")
    }
    LaunchedEffect(timerEnabled, finished) {
        while (timerEnabled && !finished) {
            elapsedSeconds = (System.currentTimeMillis() - startedAt) / 1_000
            delay(1_000)
        }
    }
    // Questão ocultada no meio da bateria sai da sessão; a seguinte assume o mesmo índice.
    val sessionQuestions = questionIds.mapNotNull { id -> allQuestions.firstOrNull { it.question.id == id } }.filterNot { it.question.isHidden }
    val snackbar = remember { SnackbarHostState() }
    var index by rememberSaveable { mutableIntStateOf(0) }
    val selections = rememberSaveable(saver = selectionsSaver) { mutableStateMapOf<Long, String>() }
    val results = rememberSaveable(saver = resultsSaver) { mutableStateMapOf<Long, Boolean>() }
    var revisao by remember { mutableStateOf<QuestionWithOptions?>(null) }
    val simulation = config.mode == "simulation"
    // Alternativas em ordem nova a cada sessão (OptionShuffle): a pessoa não decora a letra.
    // Simulado fica na ordem original, como na prova.
    val shuffleSeed = rememberSaveable { System.nanoTime() }
    suspend fun finishSession() {
        val now = System.currentTimeMillis()
        val correctCount = results.values.count { it }
        val actualMinutes = ((now - startedAt) / 60_000L).toInt().coerceAtLeast(0)
        viewModel.saveQuestionSession(QuestionSessionEntity(
            id = sessionId,
            type = SessionTypeMapper.fromMode(config.mode, config.topicId, config.subjectId),
            startedAt = startedAt,
            completedAt = now,
            durationSeconds = (now - startedAt) / 1_000,
            questionCount = sessionQuestions.size,
            correctCount = correctCount,
            subjectIdsText = config.subjectId?.toString().orEmpty(),
            topicIdsText = config.topicId?.toString().orEmpty(),
        ))
        config.planTaskId?.let { taskId ->
            onPlanTaskComplete(
                taskId,
                CompleteTaskInput(
                    startedAt = startedAt,
                    completedAt = now,
                    actualMinutes = actualMinutes,
                    questions = sessionQuestions.size,
                    correct = correctCount,
                ),
            )
            planTaskXp = ProgressEngine.earnedPlanTask(PlanTaskType.QUESTIONS, actualMinutes, correctCount)
        }
        finished = true
    }
    fun requestFinish(beforeSave: suspend () -> Unit = {}) {
        if (finished || finishing) return
        finishing = true
        finishError = null
        scope.launch {
            runCatching {
                beforeSave()
                finishSession()
            }.onFailure {
                finishError = "Não foi possível salvar a sessão e concluir a bateria. Tente novamente."
            }
            finishing = false
        }
    }

    if (sessionQuestions.isEmpty()) {
        Column(Modifier.fillMaxSize().padding(screenPadding())) {
            IconButton(onClick = onBack) { Icon(Icons.Outlined.ArrowBack, "Voltar") }
            EmptyState("Nenhuma questão encontrada", "Ajuste os filtros ou importe mais conteúdo.", "Voltar", onBack)
        }
        return
    }
    if (finished) {
        QuizResult(sessionQuestions, results, planTaskXp.takeIf { it >= 0 }, config.planTaskId != null, onBack)
        return
    }

    // no meio da bateria as comemorações esperam; aparecem no resultado
    br.com.estudario.ui.HoldCelebrations()
    val current = sessionQuestions[index.coerceAtMost(sessionQuestions.lastIndex)]
    val selected = selections[current.question.id]
    // A questão nova entra deslizando da direita.
    val enter = remember(current.question.id) { androidx.compose.animation.core.Animatable(0f) }
    LaunchedEffect(current.question.id) { enter.animateTo(1f, androidx.compose.animation.core.tween(380, easing = androidx.compose.animation.core.FastOutSlowInEasing)) }
    val enterLayer = Modifier.graphicsLayer { translationX = (1f - enter.value) * 44.dp.toPx(); alpha = enter.value }
    val confirmed = results.containsKey(current.question.id)
    val correct = results[current.question.id]
    // Alternativas riscadas, como na prova de papel: "id da questão:letra". Só ajuda a pensar,
    // não muda a resposta nem é salvo.
    val eliminated = remember { mutableStateMapOf<String, Boolean>() }
    fun isEliminated(key: String) = eliminated["${current.question.id}:$key"] == true
    fun toggleEliminated(key: String) {
        val id = "${current.question.id}:$key"
        val now = eliminated[id] != true
        eliminated[id] = now
        if (now && selections[current.question.id] == key) selections.remove(current.question.id)
    }
    Box(Modifier.fillMaxSize()) {
    LazyColumn(Modifier.fillMaxSize(), contentPadding = screenPadding(), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        finishError?.let { message -> item { Text(message, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodyMedium) } }
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) { Icon(Icons.Outlined.ArrowBack, "Sair") }
                Column(Modifier.weight(1f)) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(if (simulation) "Simulado" else "Questão ${index + 1} de ${sessionQuestions.size}", fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f).padding(end = 8.dp))
                        if (timerEnabled) Text("%02d:%02d".format(elapsedSeconds / 60, elapsedSeconds % 60), style = MaterialTheme.typography.labelLarge)
                    }
                    LinearProgressIndicator({ (index + 1f) / sessionQuestions.size }, Modifier.fillMaxWidth())
                }
                IconButton(onClick = { viewModel.toggleQuestionFavorite(current.question) }) {
                    Icon(if (current.question.isFavorite) Icons.Outlined.Favorite else Icons.Outlined.FavoriteBorder, if (current.question.isFavorite) "Remover dos favoritos" else "Favoritar")
                }
                br.com.estudario.ui.components.ReportErrorButton(
                    br.com.estudario.data.remote.ReportKind.QUESTION,
                    excerpt = {
                        buildString {
                            appendLine(current.question.statement)
                            current.options.sortedBy { it.position }.forEach { appendLine("${it.key}) ${it.text}${if (it.isCorrect) "  ← gabarito" else ""}") }
                        }
                    },
                    topic = topics.firstOrNull { it.id == current.question.topicId }?.title,
                )
            }
        }
        item(key = "enunciado-${current.question.id}") {
            Column(enterLayer) {
                // Questão inédita já diz a banca no selo de origem; o chip só repete.
                if (current.question.questionSourceType != br.com.estudario.data.local.QuestionSourceType.AUTHORIAL) current.question.board?.let { AssistChip(onClick = {}, label = { Text(listOfNotNull(it, current.question.year?.toString()).joinToString(" • ")) }) }
                Spacer(Modifier.height(8.dp))
                // O enunciado é o protagonista: letra maior que as alternativas.
                br.com.estudario.ui.components.StudyMarkdown(current.question.statement, Modifier.fillMaxWidth(), textSizeSp = 19f)
                Spacer(Modifier.height(8.dp))
                QuestionProvenance(current.question)
                if (!confirmed && current.options.size > 2 && index == 0) Text(
                    "Em dúvida? Arraste para o lado as alternativas que você descarta, para riscá-las.",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
        val certoErrado = current.options.size == 2 &&
            current.options.mapTo(hashSetOf()) { it.key.uppercase() } == setOf("C", "E")
        if (certoErrado) {
            item {
                // Questão de banca tipo Cespe: dois botões largos, sem lista de alternativas.
                Row(Modifier.fillMaxWidth().height(IntrinsicSize.Min), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    current.options.sortedBy { it.key }.forEach { option ->
                        val reveal = confirmed && !simulation
                        val container = when {
                            reveal && option.isCorrect -> MaterialTheme.colorScheme.secondaryContainer
                            reveal && option.key == selected && !option.isCorrect -> MaterialTheme.colorScheme.errorContainer
                            option.key == selected -> MaterialTheme.colorScheme.primaryContainer
                            else -> MaterialTheme.colorScheme.surface
                        }
                        Surface(
                            // Altura mínima em vez de fixa: com fonte maior o rótulo não é cortado.
                            modifier = Modifier.weight(1f).fillMaxHeight().heightIn(min = 72.dp).selectable(
                                selected = option.key == selected,
                                enabled = !confirmed || simulation,
                            ) { selections[current.question.id] = option.key },
                            shape = RoundedCornerShape(14.dp),
                            color = container,
                            border = BorderStroke(if (option.key == selected) 2.dp else 1.dp, if (option.key == selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant),
                        ) {
                            Column(Modifier.fillMaxSize().padding(vertical = 10.dp, horizontal = 8.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                                Icon(
                                    if (option.key.equals("C", true)) Icons.Outlined.CheckCircle else Icons.Outlined.Cancel,
                                    null,
                                    tint = if (option.key.equals("C", true)) Color(0xFF087F5B) else MaterialTheme.colorScheme.error,
                                )
                                Spacer(Modifier.height(4.dp))
                                Text(
                                    option.text.ifBlank { if (option.key.equals("C", true)) "Certo" else "Errado" },
                                    fontWeight = FontWeight.Bold,
                                )
                            }
                        }
                    }
                }
            }
        } else shuffledOptions(current.options, shuffleSeed + current.question.id * 7919, fixed = simulation).forEach { (option, shownLetter) ->
            item(key = "opt-${current.question.id}-${option.id}") {
              Box(enterLayer) {
                val reveal = confirmed && !simulation
                val crossed = isEliminated(option.key) && !reveal
                val container = when {
                    reveal && option.isCorrect -> MaterialTheme.colorScheme.secondaryContainer
                    reveal && option.key == selected && !option.isCorrect -> MaterialTheme.colorScheme.errorContainer
                    option.key == selected -> MaterialTheme.colorScheme.primaryContainer
                    else -> MaterialTheme.colorScheme.surface
                }
                SwipeToEliminate(crossed = crossed, enabled = !confirmed || simulation, onToggle = { toggleEliminated(option.key) }) {
                    val tileState = when {
                        reveal && option.isCorrect -> AnswerState.RIGHT
                        reveal && option.key == selected -> AnswerState.WRONG
                        option.key == selected -> AnswerState.CHOSEN
                        else -> AnswerState.IDLE
                    }
                    AnswerTile(
                        letter = shownLetter,
                        state = tileState,
                        crossed = crossed,
                        // Riscada: tocar desfaz o risco em vez de marcar, para não marcar sem querer.
                        modifier = Modifier.selectable(selected = option.key == selected, enabled = !confirmed || simulation) {
                            if (crossed) toggleEliminated(option.key) else selections[current.question.id] = option.key
                        },
                    ) {
                            if (option.text.contains("$$")) {
                                // Alternativa com fórmula: o leitor de Markdown desenha o LaTeX; tocar no texto marca a alternativa.
                                br.com.estudario.ui.components.StudyInlineText(
                                    option.text,
                                    Modifier.weight(1f),
                                    onTap = { if (!confirmed || simulation) { if (crossed) toggleEliminated(option.key) else selections[current.question.id] = option.key } },
                                )
                            } else Text(
                                option.text,
                                Modifier.weight(1f),
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = if (tileState == AnswerState.IDLE) FontWeight.Normal else FontWeight.SemiBold,
                                textDecoration = if (crossed) androidx.compose.ui.text.style.TextDecoration.LineThrough else null,
                            )
                            if (crossed) Icon(Icons.Outlined.Undo, "Desfazer risco", Modifier.size(18.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
              }
        }
        if (!simulation && confirmed) {
            item {
                // Mesmo tom dos blocos de resposta: suave, sem o vermelho de alerta do sistema.
                val darkUi = MaterialTheme.colorScheme.background.red < 0.5f
                val tone = when {
                    correct == true -> if (darkUi) Color(0xFF3DD68C) else Color(0xFF1FA65C)
                    else -> if (darkUi) Color(0xFFFF6B5E) else Color(0xFFD93A2B)
                }
                ElevatedCard(colors = CardDefaults.elevatedCardColors(containerColor = androidx.compose.ui.graphics.lerp(MaterialTheme.colorScheme.surface, tone, if (darkUi) 0.20f else 0.12f))) {
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        // O Folha reage, só depois da resposta: comemora o acerto, consola no erro, solta
                        // o ar quando a pessoa vira depois de errar (ufa) e pega fogo numa sequência longa.
                        val history = sessionQuestions.take(index.coerceAtMost(sessionQuestions.lastIndex) + 1).mapNotNull { results[it.question.id] }
                        val inARow = history.asReversed().takeWhile { it }.size
                        val relief = correct == true && inARow == 1 && history.size >= 2 && !history[history.size - 2]
                        val fire = correct == true && inARow >= 5
                        // o som da resposta (sons do Estudário): acerto, erro ou o arpejo da sequência
                        val ctxSom = androidx.compose.ui.platform.LocalContext.current
                        androidx.compose.runtime.LaunchedEffect(current.question.id) { br.com.estudario.ui.brand.EstudarioSom.play(ctxSom, if (fire || inARow in setOf(3, 10, 15, 20)) "combo" else if (correct == true) "acerto" else "erro") }
                        // errou três ou mais seguidas: em vez da frase de erro, ele acolhe
                        val tough = correct != true && history.size >= 3 && history.takeLast(3).none { it }
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            androidx.compose.runtime.key(current.question.id) {
                                br.com.estudario.ui.assistant.Folha(
                                    64.dp,
                                    mood = when {
                                        fire -> br.com.estudario.ui.assistant.FolhaMood.ECSTATIC
                                        relief -> br.com.estudario.ui.assistant.FolhaMood.RELIEVED
                                        correct == true -> br.com.estudario.ui.assistant.FolhaMood.HAPPY
                                        tough -> br.com.estudario.ui.assistant.FolhaMood.TALKING
                                        else -> br.com.estudario.ui.assistant.FolhaMood.SAD
                                    },
                                )
                            }
                            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(when { fire -> "Em chamas!"; relief -> "Ufa, virou!"; correct == true -> "Mandou bem!"; tough -> "Tô aqui com você"; else -> "Não foi dessa vez" }, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black, color = tone)
                                }
                                Text(
                                    when {
                                        fire -> br.com.estudario.ui.assistant.FolhaLines.forFire(current.question.id)
                                        relief -> br.com.estudario.ui.assistant.FolhaLines.forRelief(current.question.id)
                                        tough -> br.com.estudario.domain.FolhaMemory.toughLines[(current.question.id % br.com.estudario.domain.FolhaMemory.toughLines.size).toInt()]
                                        else -> br.com.estudario.ui.assistant.FolhaLines.forAnswer(correct == true, current.question.id)
                                    },
                                    style = MaterialTheme.typography.bodyMedium,
                                )
                            }
                        }
                        // A dificuldade só aparece agora, depois de respondida: saber que a questão é
                        // "DIFICIL" antes muda o jeito de responder, e na prova ninguém te avisa.
                        current.question.difficulty?.let { nivel ->
                            AssistChip(
                                onClick = {},
                                label = {
                                    Text(
                                        when (nivel) {
                                            br.com.estudario.data.local.Difficulty.FACIL -> "Nível fácil"
                                            br.com.estudario.data.local.Difficulty.MEDIA -> "Nível médio"
                                            br.com.estudario.data.local.Difficulty.DIFICIL -> "Nível difícil"
                                        },
                                    )
                                },
                            )
                        }
                        if (showExplanation) MarkdownText(shuffledExplanation(current.question.explanation, current.options, shuffleSeed + current.question.id * 7919, fixed = simulation))
                        else Text("Explicação oculta pela sua configuração.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                        if (correct == false) {
                            FlowRow(verticalArrangement = Arrangement.spacedBy(4.dp), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                FilledTonalButton(onClick = { revisao = current }) { Text("Revisar este assunto") }
                                TextButton(onClick = { onOpenTopic(current.question.topicId) }) { Text("Abrir tópico") }
                            }
                        }
                    }
                }
            }
        }
        item {
            if (simulation) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(onClick = { if (index > 0) index-- }, enabled = index > 0, modifier = Modifier.weight(1f)) { Text("Anterior") }
                    if (index < sessionQuestions.lastIndex) Button(onClick = { index++ }, enabled = selected != null, modifier = Modifier.weight(1f)) { Text("Próxima") }
                    else Button(onClick = {
                        requestFinish {
                            sessionQuestions.forEach { question -> selections[question.question.id]?.let { key -> results[question.question.id] = viewModel.answer(question, key, sessionId) } }
                        }
                    }, enabled = selections.size == sessionQuestions.size && !finishing, modifier = Modifier.weight(1f)) { Text(if (finishing) "Salvando…" else "Finalizar") }
                }
            } else if (!confirmed) {
                Button(onClick = { scope.launch { results[current.question.id] = viewModel.answer(current, selected!!, sessionId) } }, enabled = selected != null, modifier = Modifier.fillMaxWidth()) { Text("Confirmar resposta") }
            } else {
                Button(
                    onClick = { if (index < sessionQuestions.lastIndex) index++ else requestFinish() },
                    enabled = !finishing,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        when {
                            finishing -> "Salvando…"
                            index < sessionQuestions.lastIndex -> "Próxima questão"
                            config.planTaskId != null -> "Concluir bateria do plano"
                            else -> "Ver resultado"
                        },
                    )
                }
            }
        }
    }
    SnackbarHost(snackbar, Modifier.align(Alignment.BottomCenter).padding(16.dp))
    }

    revisao?.let { alvo ->
        QuestionReviewSheet(
            viewModel = viewModel,
            question = alvo.question,
            onOpenTopic = onOpenTopic,
            onDismiss = { revisao = null },
        )
    }
}

/** Serializa as respostas escolhidas para elas voltarem intactas ao reabrir a sessão. */
private val selectionsSaver = Saver<SnapshotStateMap<Long, String>, String>(
    save = { map -> map.entries.joinToString(";") { "${it.key}=${it.value}" } },
    restore = { texto ->
        mutableStateMapOf<Long, String>().apply {
            texto.split(";").filter { it.isNotBlank() }.forEach { par ->
                val id = par.substringBefore('=').toLongOrNull() ?: return@forEach
                put(id, par.substringAfter('='))
            }
        }
    },
)

private val resultsSaver = Saver<SnapshotStateMap<Long, Boolean>, String>(
    save = { map -> map.entries.joinToString(";") { "${it.key}=${if (it.value) 1 else 0}" } },
    restore = { texto ->
        mutableStateMapOf<Long, Boolean>().apply {
            texto.split(";").filter { it.isNotBlank() }.forEach { par ->
                val id = par.substringBefore('=').toLongOrNull() ?: return@forEach
                put(id, par.substringAfter('=') == "1")
            }
        }
    },
)

@Composable
private fun QuizResult(
    questions: List<QuestionWithOptions>,
    results: SnapshotStateMap<Long, Boolean>,
    planTaskXp: Int?,
    isPlanTask: Boolean,
    onBack: () -> Unit,
) {
    val correct = results.values.count { it }
    val percent = if (questions.isEmpty()) 0 else correct * 100 / questions.size
    Column(Modifier.fillMaxSize().verticalScroll(androidx.compose.foundation.rememberScrollState()).padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        // O Folha fecha a bateria: comemora se foi bem, motiva se foi difícil.
        // fim da sessão: a assinatura do Estudário
        val ctxSom = androidx.compose.ui.platform.LocalContext.current
        androidx.compose.runtime.LaunchedEffect(Unit) { br.com.estudario.ui.brand.EstudarioSom.play(ctxSom, "resultado") }
        br.com.estudario.ui.assistant.Folha(
            170.dp,
            mood = if (percent >= 60) br.com.estudario.ui.assistant.FolhaMood.HAPPY else br.com.estudario.ui.assistant.FolhaMood.TALKING,
        )
        Spacer(Modifier.height(8.dp))
        Text("Sessão concluída", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Text("$percent%", style = MaterialTheme.typography.displayMedium, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
        Text("$correct acertos • ${questions.size - correct} erros • ${questions.size} questões")
        Spacer(Modifier.height(12.dp))
        br.com.estudario.ui.assistant.FolhaSays(
            br.com.estudario.ui.assistant.FolhaLines.forResult(percent, questions.sumOf { it.question.id }),
            avatar = 0.dp,
            bubbleColor = MaterialTheme.colorScheme.primaryContainer,
            textColor = MaterialTheme.colorScheme.onPrimaryContainer,
        )
        Spacer(Modifier.height(10.dp))
        // Bateria do plano: as questões pagam pela tarefa (abaixo), não como avulsas.
        if (planTaskXp == null) br.com.estudario.ui.components.XpTag(
            br.com.estudario.domain.ProgressEngine.XpReward(questions.size + correct),
            earned = true,
        )
        if (planTaskXp != null) {
            Spacer(Modifier.height(8.dp))
            Text("Bateria do plano concluída", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
            br.com.estudario.ui.components.XpTag(ProgressEngine.XpReward(planTaskXp), earned = true)
        }
        Spacer(Modifier.height(24.dp))
        Button(onClick = onBack) {
            Text(if (isPlanTask) "Voltar ao plano" else "Voltar ao treino")
        }
    }
}

/**
 * Arrastar uma alternativa para qualquer lado risca ou desfaz o risco. O fundo mostra o que vai
 * acontecer; a alternativa volta para o lugar (não sai da tela), só muda de estado.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SwipeToEliminate(crossed: Boolean, enabled: Boolean, onToggle: () -> Unit, content: @Composable () -> Unit) {
    val toggle by rememberUpdatedState(onToggle)
    val state = rememberSwipeToDismissBoxState(
        confirmValueChange = { value ->
            if (value != SwipeToDismissBoxValue.Settled) toggle()
            false
        },
        positionalThreshold = { total -> total * 0.3f },
    )
    SwipeToDismissBox(
        state = state,
        enableDismissFromStartToEnd = enabled,
        enableDismissFromEndToStart = enabled,
        backgroundContent = {
            val toLeft = state.dismissDirection == SwipeToDismissBoxValue.EndToStart
            Box(
                Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(12.dp)).padding(horizontal = 20.dp),
                contentAlignment = if (toLeft) Alignment.CenterEnd else Alignment.CenterStart,
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(if (crossed) Icons.Outlined.Undo else Icons.Outlined.StrikethroughS, null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(Modifier.width(6.dp))
                    Text(if (crossed) "Desfazer" else "Riscar", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        },
        content = { content() },
    )
}

private enum class AnswerState { IDLE, CHOSEN, RIGHT, WRONG }

/**
 * Alternativa como bloco de resposta: letra num selo, borda grossa e espessura embaixo. Escolhida
 * fica na cor da marca; corrigida, verde com o certo ou vermelha com o xis, relevo junto.
 */
@Composable
private fun AnswerTile(
    letter: String,
    state: AnswerState,
    crossed: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable androidx.compose.foundation.layout.RowScope.() -> Unit,
) {
    val scheme = MaterialTheme.colorScheme
    val dark = scheme.background.red < 0.5f
    val green = if (dark) Color(0xFF3DD68C) else Color(0xFF1FA65C)
    val red = if (dark) Color(0xFFFF6B5E) else Color(0xFFD93A2B)
    val accent = when (state) {
        AnswerState.RIGHT -> green
        AnswerState.WRONG -> red
        AnswerState.CHOSEN -> scheme.primary
        AnswerState.IDLE -> scheme.outlineVariant
    }
    val face = when (state) {
        AnswerState.IDLE -> scheme.surfaceContainerLowest
        else -> androidx.compose.ui.graphics.lerp(scheme.surface, accent, if (dark) 0.22f else 0.13f)
    }
    val lip = if (state == AnswerState.IDLE) scheme.outlineVariant else androidx.compose.ui.graphics.lerp(accent, Color.Black, 0.25f)
    val shape = RoundedCornerShape(16.dp)
    // Resposta com movimento: a certa dá um pulinho, a errada treme.
    val pop = remember { androidx.compose.animation.core.Animatable(1f) }
    val shake = remember { androidx.compose.animation.core.Animatable(0f) }
    LaunchedEffect(state) {
        when (state) {
            AnswerState.RIGHT -> { pop.animateTo(1.04f, androidx.compose.animation.core.tween(140)); pop.animateTo(1f, androidx.compose.animation.core.spring(0.35f, androidx.compose.animation.core.Spring.StiffnessMedium)) }
            AnswerState.WRONG -> listOf(-7f, 6f, -4f, 2f, 0f).forEach { shake.animateTo(it, androidx.compose.animation.core.tween(70)) }
            else -> Unit
        }
    }
    Box(modifier.fillMaxWidth().graphicsLayer { scaleX = pop.value; scaleY = pop.value; translationX = shake.value.dp.toPx() }.clip(shape), propagateMinConstraints = true) {
        Box(Modifier.matchParentSize().padding(top = 4.dp).background(lip, shape))
        Row(
            Modifier
                .padding(bottom = 4.dp)
                .background(face, shape)
                .border(2.dp, accent, shape)
                .padding(horizontal = 12.dp, vertical = 12.dp)
                .alpha(if (crossed) 0.4f else 1f),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // O selo da letra; na correção vira o certo ou o xis.
            Box(
                Modifier.size(34.dp).clip(RoundedCornerShape(10.dp))
                    .background(if (state == AnswerState.IDLE) scheme.surfaceVariant else accent),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    when (state) { AnswerState.RIGHT -> "✓"; AnswerState.WRONG -> "✕"; else -> letter.uppercase() },
                    fontWeight = FontWeight.Black,
                    style = MaterialTheme.typography.titleMedium,
                    color = if (state == AnswerState.IDLE) scheme.onSurfaceVariant else Color.White,
                )
            }
            Spacer(Modifier.width(12.dp))
            content()
        }
    }
}

/** As alternativas na ordem de exibição, com a letra que aparece na tela (a original fica na resposta). */
private fun shuffledOptions(options: List<br.com.estudario.data.local.QuestionOptionEntity>, seed: Long, fixed: Boolean): List<Pair<br.com.estudario.data.local.QuestionOptionEntity, String>> {
    val original = options.sortedBy { it.position }
    val order = if (fixed) original.indices.toList() else br.com.estudario.domain.OptionShuffle.order(original.map { it.text }, seed)
    return order.map { i -> original[i] to br.com.estudario.domain.OptionShuffle.displayLetter(order, i) }
}

/** A explicação com as letras da ordem mostrada. */
private fun shuffledExplanation(explanation: String, options: List<br.com.estudario.data.local.QuestionOptionEntity>, seed: Long, fixed: Boolean): String {
    if (fixed) return explanation
    val original = options.sortedBy { it.position }
    return br.com.estudario.domain.OptionShuffle.relabel(explanation, original.map { it.key }, br.com.estudario.domain.OptionShuffle.order(original.map { it.text }, seed))
}
