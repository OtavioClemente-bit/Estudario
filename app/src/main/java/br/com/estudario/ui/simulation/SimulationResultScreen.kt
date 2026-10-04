package br.com.estudario.ui.simulation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.RemoveCircleOutline
import br.com.estudario.ui.brand.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilterChip
import br.com.estudario.ui.brand.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import br.com.estudario.ui.brand.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.estudario.EstudarioApplication
import br.com.estudario.data.local.QuestionWithOptions
import br.com.estudario.data.remote.ReportKind
import br.com.estudario.data.simulation.SimulationService
import br.com.estudario.data.simulation.SubjectScore
import br.com.estudario.domain.simulation.SimulationMode
import br.com.estudario.ui.AppViewModel
import br.com.estudario.ui.components.MarkdownText
import br.com.estudario.ui.components.ReportErrorButton
import br.com.estudario.ui.theme.EstudarioShapes
import br.com.estudario.ui.theme.estudarioColors
import br.com.estudario.ui.theme.screenPadding
import kotlinx.coroutines.launch

private enum class CorrectionFilter(val label: String) { ALL("Todas"), WRONG("Erradas"), BLANK("Em branco"), RIGHT("Certas") }

@Composable
fun SimulationResultScreen(
    viewModel: AppViewModel,
    simulationId: Long,
    onBack: () -> Unit,
    onTrainSubject: (Long) -> Unit,
    onOpenSimulations: () -> Unit,
) {
    val app = LocalContext.current.applicationContext as EstudarioApplication
    val service = app.simulationService
    val simulation by service.simulation(simulationId).collectAsState(initial = null)
    val questions by service.questions(simulationId).collectAsState(initial = emptyList())
    val all by service.simulations.collectAsState(initial = emptyList())
    val profiles by service.examProfiles.collectAsState(initial = emptyList())
    val subjects by viewModel.subjects.collectAsState()
    val topics by viewModel.topics.collectAsState()
    val scope = rememberCoroutineScope()
    var scores by remember { mutableStateOf<List<SubjectScore>>(emptyList()) }
    var filter by remember { mutableStateOf(CorrectionFilter.WRONG) }
    var rematchMessage by remember { mutableStateOf<String?>(null) }
    LaunchedEffect(simulationId, simulation?.status) { scores = service.subjectScores(simulationId) }
    val current = simulation ?: return
    val answers = remember(current.answersJson) { SimulationService.decodeAnswers(current.answersJson) }
    val target = profiles.firstOrNull { it.competitionId == current.competitionId }?.targetPercent ?: 70
    val score = current.scorePercent ?: 0
    val history = all.filter { it.competitionId == current.competitionId && it.status == "FINISHED" }.sortedBy { it.finishedAt }
    val previous = history.takeWhile { it.id != current.id }.lastOrNull()
    val correctKey = { row: QuestionWithOptions -> row.options.firstOrNull { it.isCorrect }?.key }
    val personal = questions.filter { "ponto-fraco" in it.question.tagsText }
    val personalRight = personal.count { answers[it.question.id] == correctKey(it) }
    val wrong = questions.count { it.question.id in answers && answers[it.question.id] != correctKey(it) }
    val blank = questions.count { it.question.id !in answers }
    val shown = questions.withIndex().filter { (_, row) ->
        when (filter) {
            CorrectionFilter.ALL -> true
            CorrectionFilter.WRONG -> row.question.id in answers && answers[row.question.id] != correctKey(row)
            CorrectionFilter.BLANK -> row.question.id !in answers
            CorrectionFilter.RIGHT -> answers[row.question.id] == correctKey(row)
        }
    }

    LazyColumn(Modifier.fillMaxSize(), contentPadding = screenPadding(), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) { Icon(Icons.Outlined.ArrowBack, "Voltar") }
                Text(current.title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            }
        }
        item {
            val reached = score >= target
            Surface(shape = EstudarioShapes.spotlight, color = if (reached) estudarioColors().completed.copy(alpha = 0.18f) else MaterialTheme.colorScheme.primaryContainer, modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(22.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("SUA NOTA", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text("$score%", style = MaterialTheme.typography.displayMedium, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.width(12.dp))
                        Text("${current.correctCount} de ${current.questionCount} certas", Modifier.padding(bottom = 10.dp))
                    }
                    Text(
                        when {
                            reached -> "Acima da sua meta de $target%. Se a prova fosse hoje, você estaria no páreo."
                            else -> {
                                val missing = ((target * current.questionCount + 99) / 100) - current.correctCount
                                "Faltaram $missing questão(ões) para a sua meta de $target%. Olhe as matérias abaixo: é ali que estão esses pontos."
                            }
                        },
                        style = MaterialTheme.typography.bodyMedium,
                    )
                    previous?.scorePercent?.let { before ->
                        val delta = score - before
                        Text(
                            if (delta >= 0) "▲ $delta ponto(s) em relação ao simulado anterior." else "▼ ${-delta} ponto(s) em relação ao simulado anterior.",
                            style = MaterialTheme.typography.labelLarge,
                            color = if (delta >= 0) estudarioColors().completed else MaterialTheme.colorScheme.error,
                        )
                    }
                    val minutes = current.elapsedSeconds / 60
                    Text(
                        "Tempo: $minutes de ${current.timeLimitMinutes} min${if (minutes > current.timeLimitMinutes) " (passou do tempo da prova)" else ""} · $wrong erradas · $blank em branco",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
        if (personal.isNotEmpty()) item {
            ElevatedCard(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("Feitas para você", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text(
                        "${personal.size} questão(ões) foram montadas em cima das confusões que já apareceram no seu caderno de erros. Você acertou $personalRight." +
                            if (personalRight == personal.size) " Essas confusões ficaram para trás." else " Vale revisar esses conceitos antes do próximo.",
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
        }
        if (scores.isNotEmpty()) item {
            ElevatedCard(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Por matéria", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text("Da mais fraca para a mais forte. Toque em treinar para atacar o ponto fraco agora.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    scores.forEach { subject ->
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(subject.subjectName, Modifier.weight(1f), style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
                                Text("${subject.correct}/${subject.total} · ${subject.percent}%", style = MaterialTheme.typography.labelLarge)
                                subjects.firstOrNull { it.name == subject.subjectName && it.competitionId == current.competitionId }?.let { entity ->
                                    if (subject.percent < target) TextButton(onClick = { onTrainSubject(entity.id) }) { Text("Treinar") }
                                }
                            }
                            LinearProgressIndicator(
                                { subject.percent / 100f },
                                Modifier.fillMaxWidth(),
                                color = if (subject.percent >= target) estudarioColors().completed else if (subject.percent < target - 20) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                                strokeCap = StrokeCap.Round,
                            )
                        }
                    }
                }
            }
        }
        if (history.size >= 2) item { EvolutionChart(history.mapNotNull { it.scorePercent }, target) }
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                if (wrong + blank > 0 && current.mode != SimulationMode.REMATCH.name) Button(
                    onClick = {
                        scope.launch {
                            runCatching { service.create(current.competitionId, SimulationMode.REMATCH, 30) }
                                .onSuccess { onOpenSimulations() }
                                .onFailure { rematchMessage = it.message }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                ) { Text("Revanche: refazer os erros com outro cenário") }
                rematchMessage?.let { Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall) }
                OutlinedButton(onClick = onOpenSimulations, modifier = Modifier.fillMaxWidth()) { Text("Voltar aos simulados") }
                Text("Os erros já foram para o Caderno de erros e voltam para revisão nos próximos dias.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Correção comentada", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    CorrectionFilter.entries.forEach { value -> FilterChip(filter == value, { filter = value }, { Text(value.label) }) }
                }
            }
        }
        if (shown.isEmpty()) item { Text("Nada aqui.", color = MaterialTheme.colorScheme.onSurfaceVariant) }
        itemsIndexed(shown, key = { _, entry -> entry.value.question.id }) { _, entry ->
            CorrectionCard(entry.index + 1, entry.value, answers[entry.value.question.id], topics.firstOrNull { it.id == entry.value.question.topicId }?.title)
        }
    }
}

@Composable
private fun CorrectionCard(number: Int, row: QuestionWithOptions, selected: String?, topic: String?) {
    var open by remember { mutableStateOf(false) }
    val correct = row.options.firstOrNull { it.isCorrect }?.key
    val status = when (selected) {
        null -> Triple(Icons.Outlined.RemoveCircleOutline, "Em branco", MaterialTheme.colorScheme.outline)
        correct -> Triple(Icons.Outlined.CheckCircle, "Certa", estudarioColors().completed)
        else -> Triple(Icons.Outlined.Cancel, "Errada", MaterialTheme.colorScheme.error)
    }
    ElevatedCard(onClick = { open = !open }, modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(status.first, status.second, tint = status.third)
                Spacer(Modifier.width(8.dp))
                Text("Questão $number", fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                Text("Sua: ${selected ?: "em branco"} · Gabarito: ${correct ?: "?"}", style = MaterialTheme.typography.labelLarge)
            }
            topic?.let { Text(it, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary, maxLines = 2) }
            if (!open) {
                Text(br.com.estudario.ui.components.plainFormulaText(row.question.statement), maxLines = 3, style = MaterialTheme.typography.bodySmall, overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis)
                return@Column
            }
            MarkdownText(row.question.statement)
            row.options.sortedBy { it.position }.forEach { option ->
                val color = when {
                    option.isCorrect -> estudarioColors().completed
                    option.key == selected -> MaterialTheme.colorScheme.error
                    else -> MaterialTheme.colorScheme.onSurfaceVariant
                }
                br.com.estudario.ui.components.StudyInlineText("${option.key}) ${option.text}", color = color, fontWeight = if (option.isCorrect || option.key == selected) FontWeight.SemiBold else FontWeight.Normal, style = MaterialTheme.typography.bodyMedium)
            }
            Surface(color = MaterialTheme.colorScheme.surfaceVariant, shape = MaterialTheme.shapes.medium) {
                Column(Modifier.fillMaxWidth().padding(12.dp)) { MarkdownText(row.question.explanation) }
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Algo errado nesta questão?", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.weight(1f))
                ReportErrorButton(ReportKind.QUESTION, excerpt = {
                    buildString {
                        appendLine(row.question.statement)
                        row.options.sortedBy { it.position }.forEach { appendLine("${it.key}) ${it.text}${if (it.isCorrect) "  ← gabarito" else ""}") }
                    }
                }, topic = topic)
            }
        }
    }
}
