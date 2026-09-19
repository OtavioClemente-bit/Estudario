package br.com.estudario.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import br.com.estudario.domain.SessionTypeMapper
import br.com.estudario.ui.AppViewModel
import br.com.estudario.ui.components.EmptyState
import br.com.estudario.ui.components.MarkdownText
import br.com.estudario.ui.components.QuestionProvenance
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import java.util.UUID

@Composable
fun QuizScreen(viewModel: AppViewModel, config: QuizConfig, onBack: () -> Unit, onOpenTopic: (Long) -> Unit) {
    val allQuestions by viewModel.questions.collectAsState()
    val topics by viewModel.topics.collectAsState()
    val errors by viewModel.errors.collectAsState()
    val timerEnabled by viewModel.questionTimer.collectAsState()
    val showExplanation by viewModel.showExplanation.collectAsState()
    val scope = rememberCoroutineScope()
    // Tudo abaixo é rememberSaveable de propósito: abrir o resumo de um tópico empilha outra tela
    // por cima do quiz e antes disso a sessão inteira se perdia — voltava na questão 1, sem as
    // respostas já dadas. Agora a sessão sobrevive a sair, voltar e até o app ser recriado.
    val sessionId = rememberSaveable { UUID.randomUUID().toString() }
    val startedAt = rememberSaveable { System.currentTimeMillis() }
    var elapsedSeconds by remember { mutableLongStateOf(0L) }
    var finished by rememberSaveable { mutableStateOf(false) }
    var questionIdsText by rememberSaveable { mutableStateOf("") }
    val questionIds = remember(questionIdsText) { questionIdsText.split(",").mapNotNull(String::toLongOrNull) }
    val eligible = allQuestions.filter { item ->
        val topic = topics.firstOrNull { it.id == item.question.topicId }
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
    val sessionQuestions = questionIds.mapNotNull { id -> allQuestions.firstOrNull { it.question.id == id } }
    var index by rememberSaveable { mutableIntStateOf(0) }
    val selections = rememberSaveable(saver = selectionsSaver) { mutableStateMapOf<Long, String>() }
    val results = rememberSaveable(saver = resultsSaver) { mutableStateMapOf<Long, Boolean>() }
    var revisao by remember { mutableStateOf<QuestionWithOptions?>(null) }
    val simulation = config.mode == "simulation"
    val finishSession = {
        val now = System.currentTimeMillis()
        viewModel.saveQuestionSession(QuestionSessionEntity(
            id = sessionId,
            type = SessionTypeMapper.fromMode(config.mode, config.topicId, config.subjectId),
            startedAt = startedAt,
            completedAt = now,
            durationSeconds = (now - startedAt) / 1_000,
            questionCount = sessionQuestions.size,
            correctCount = results.values.count { it },
            subjectIdsText = config.subjectId?.toString().orEmpty(),
            topicIdsText = config.topicId?.toString().orEmpty(),
        ))
        finished = true
    }

    if (sessionQuestions.isEmpty()) {
        Column(Modifier.fillMaxSize().padding(20.dp)) {
            IconButton(onClick = onBack) { Icon(Icons.Outlined.ArrowBack, "Voltar") }
            EmptyState("Nenhuma questão encontrada", "Ajuste os filtros ou importe mais conteúdo.", "Voltar", onBack)
        }
        return
    }
    if (finished) {
        QuizResult(sessionQuestions, results, onBack)
        return
    }

    val current = sessionQuestions[index.coerceAtMost(sessionQuestions.lastIndex)]
    val selected = selections[current.question.id]
    val confirmed = results.containsKey(current.question.id)
    val correct = results[current.question.id]
    LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) { Icon(Icons.Outlined.ArrowBack, "Sair") }
                Column(Modifier.weight(1f)) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(if (simulation) "Simulado" else "Questão ${index + 1} de ${sessionQuestions.size}", fontWeight = FontWeight.Bold)
                        if (timerEnabled) Text("%02d:%02d".format(elapsedSeconds / 60, elapsedSeconds % 60), style = MaterialTheme.typography.labelLarge)
                    }
                    LinearProgressIndicator({ (index + 1f) / sessionQuestions.size }, Modifier.fillMaxWidth())
                }
                IconButton(onClick = { viewModel.toggleQuestionFavorite(current.question) }) {
                    Icon(if (current.question.isFavorite) Icons.Outlined.Favorite else Icons.Outlined.FavoriteBorder, if (current.question.isFavorite) "Remover dos favoritos" else "Favoritar")
                }
            }
        }
        item {
            current.question.board?.let { AssistChip(onClick = {}, label = { Text(listOfNotNull(it, current.question.year?.toString()).joinToString(" • ")) }) }
            Spacer(Modifier.height(8.dp))
            MarkdownText(current.question.statement)
            Spacer(Modifier.height(8.dp))
            QuestionProvenance(current.question)
        }
        val certoErrado = current.options.size == 2 &&
            current.options.mapTo(hashSetOf()) { it.key.uppercase() } == setOf("C", "E")
        if (certoErrado) {
            item {
                // Questão de banca tipo Cespe: dois botões largos, sem lista de alternativas.
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    current.options.sortedBy { it.key }.forEach { option ->
                        val reveal = confirmed && !simulation
                        val container = when {
                            reveal && option.isCorrect -> MaterialTheme.colorScheme.secondaryContainer
                            reveal && option.key == selected && !option.isCorrect -> MaterialTheme.colorScheme.errorContainer
                            option.key == selected -> MaterialTheme.colorScheme.primaryContainer
                            else -> MaterialTheme.colorScheme.surface
                        }
                        Surface(
                            modifier = Modifier.weight(1f).height(72.dp).selectable(
                                selected = option.key == selected,
                                enabled = !confirmed || simulation,
                            ) { selections[current.question.id] = option.key },
                            shape = RoundedCornerShape(14.dp),
                            color = container,
                            border = BorderStroke(if (option.key == selected) 2.dp else 1.dp, if (option.key == selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant),
                        ) {
                            Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
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
        } else current.options.sortedBy { it.position }.forEach { option ->
            item(key = option.id) {
                val reveal = confirmed && !simulation
                val container = when {
                    reveal && option.isCorrect -> MaterialTheme.colorScheme.secondaryContainer
                    reveal && option.key == selected && !option.isCorrect -> MaterialTheme.colorScheme.errorContainer
                    option.key == selected -> MaterialTheme.colorScheme.primaryContainer
                    else -> MaterialTheme.colorScheme.surface
                }
                Surface(
                    modifier = Modifier.fillMaxWidth().selectable(selected = option.key == selected, enabled = !confirmed || simulation) { selections[current.question.id] = option.key },
                    shape = RoundedCornerShape(12.dp),
                    color = container,
                    border = BorderStroke(1.dp, if (option.key == selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant),
                ) {
                    Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(selected = option.key == selected, onClick = null)
                        Spacer(Modifier.width(8.dp))
                        Text("${option.key}) ${option.text}", Modifier.weight(1f))
                    }
                }
            }
        }
        if (!simulation && confirmed) {
            item {
                ElevatedCard(colors = CardDefaults.elevatedCardColors(containerColor = if (correct == true) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.errorContainer)) {
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(if (correct == true) Icons.Outlined.CheckCircle else Icons.Outlined.Cancel, null, tint = if (correct == true) Color(0xFF087F5B) else MaterialTheme.colorScheme.error)
                            Spacer(Modifier.width(8.dp)); Text(if (correct == true) "Correto" else "Incorreto", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
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
                        if (showExplanation) MarkdownText(current.question.explanation)
                        else Text("Explicação oculta pela sua configuração.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                        if (correct == false) {
                            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
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
                        scope.launch {
                            sessionQuestions.forEach { question -> selections[question.question.id]?.let { key -> results[question.question.id] = viewModel.answer(question, key, sessionId) } }
                            finishSession()
                        }
                    }, enabled = selections.size == sessionQuestions.size, modifier = Modifier.weight(1f)) { Text("Finalizar") }
                }
            } else if (!confirmed) {
                Button(onClick = { scope.launch { results[current.question.id] = viewModel.answer(current, selected!!, sessionId) } }, enabled = selected != null, modifier = Modifier.fillMaxWidth()) { Text("Confirmar resposta") }
            } else {
                Button(onClick = { if (index < sessionQuestions.lastIndex) index++ else finishSession() }, Modifier.fillMaxWidth()) { Text(if (index < sessionQuestions.lastIndex) "Próxima questão" else "Ver resultado") }
            }
        }
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
private fun QuizResult(questions: List<QuestionWithOptions>, results: SnapshotStateMap<Long, Boolean>, onBack: () -> Unit) {
    val correct = results.values.count { it }
    val percent = if (questions.isEmpty()) 0 else correct * 100 / questions.size
    Column(Modifier.fillMaxSize().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Icon(if (percent >= 70) Icons.Outlined.CheckCircle else Icons.Outlined.Cancel, null, Modifier.size(72.dp), tint = if (percent >= 70) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.error)
        Spacer(Modifier.height(16.dp))
        Text("Sessão concluída", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Text("$percent%", style = MaterialTheme.typography.displayMedium, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
        Text("$correct acertos • ${questions.size - correct} erros • ${questions.size} questões")
        Spacer(Modifier.height(10.dp))
        br.com.estudario.ui.components.XpTag(
            br.com.estudario.domain.ProgressEngine.XpReward(questions.size + correct),
            earned = true,
        )
        Spacer(Modifier.height(24.dp))
        Button(onClick = onBack) { Text("Voltar ao treino") }
    }
}
