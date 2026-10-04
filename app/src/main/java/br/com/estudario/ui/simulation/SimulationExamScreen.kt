package br.com.estudario.ui.simulation

import br.com.estudario.ui.components.AlertDialog
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Flag
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.OutlinedFlag
import br.com.estudario.ui.brand.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import br.com.estudario.ui.brand.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import br.com.estudario.ui.brand.OutlinedButton
import br.com.estudario.ui.brand.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.estudario.EstudarioApplication
import br.com.estudario.data.simulation.SimulationService
import br.com.estudario.ui.components.MarkdownText
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * A prova. Sem gabarito até entregar, como na vida real: cronômetro regressivo, marcar para revisar,
 * mapa das questões e entrega com resumo do que ficou em branco. Tudo é salvo enquanto a pessoa
 * responde; sair pausa o tempo e a prova continua de onde parou.
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun SimulationExamScreen(simulationId: Long, onExit: () -> Unit, onFinished: (Long) -> Unit) {
    val service = (LocalContext.current.applicationContext as EstudarioApplication).simulationService
    val simulation by service.simulation(simulationId).collectAsState(initial = null)
    val questions by service.questions(simulationId).collectAsState(initial = emptyList())
    val scope = rememberCoroutineScope()
    val answers = remember { mutableStateMapOf<Long, String>() }
    val flagged = remember { mutableStateMapOf<Long, Boolean>() }
    var loaded by remember { mutableStateOf(false) }
    var index by remember { mutableIntStateOf(0) }
    var elapsed by remember { mutableLongStateOf(0L) }
    var grid by remember { mutableStateOf(false) }
    var confirmSubmit by remember { mutableStateOf(false) }
    var confirmExit by remember { mutableStateOf(false) }
    var timeUp by remember { mutableStateOf(false) }
    var overtime by remember { mutableStateOf(false) }
    var submitting by remember { mutableStateOf(false) }

    val current = simulation
    LaunchedEffect(current?.id) {
        if (current != null && !loaded) {
            answers.putAll(SimulationService.decodeAnswers(current.answersJson))
            SimulationService.decodeFlagged(current.flaggedJson).forEach { flagged[it] = true }
            elapsed = current.elapsedSeconds
            loaded = true
            if (current.status == "FINISHED") onFinished(simulationId)
        }
    }
    fun save() = scope.launch { service.saveProgress(simulationId, answers.toMap(), flagged.filterValues { it }.keys, elapsed) }
    fun submit() {
        if (submitting) return
        submitting = true
        scope.launch {
            service.submit(simulationId, answers.toMap(), elapsed)
            onFinished(simulationId)
        }
    }
    val limitSeconds = (current?.timeLimitMinutes ?: 0) * 60L
    LaunchedEffect(loaded) {
        if (!loaded) return@LaunchedEffect
        var tick = 0
        while (true) {
            delay(1_000)
            if (submitting) break
            elapsed++
            if (++tick % 15 == 0) save()
            if (limitSeconds in 1..elapsed && !timeUp && !overtime) {
                timeUp = true
                save()
            }
        }
    }
    BackHandler { confirmExit = true }

    if (current == null || questions.isEmpty() || !loaded) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { LinearProgressIndicator() }
        return
    }
    val safeIndex = index.coerceIn(0, questions.lastIndex)
    val question = questions[safeIndex]
    val remaining = (limitSeconds - elapsed).coerceAtLeast(0)
    val lowTime = limitSeconds > 0 && remaining < 10 * 60

    Column(Modifier.fillMaxSize().navigationBarsPadding()) {
        Surface(tonalElevation = 3.dp) {
            Column(Modifier.fillMaxWidth().padding(start = 4.dp, end = 8.dp, top = 8.dp, bottom = 8.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { confirmExit = true }) { Icon(Icons.Outlined.Close, "Sair e continuar depois") }
                    Column(Modifier.weight(1f)) {
                        Text("Questão ${safeIndex + 1} de ${questions.size}", fontWeight = FontWeight.Bold)
                        Text("${answers.size} respondidas · ${flagged.count { it.value }} para revisar", style = MaterialTheme.typography.labelSmall)
                    }
                    Surface(shape = RoundedCornerShape(50), color = if (lowTime) MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.secondaryContainer) {
                        Text(
                            "%d:%02d:%02d".format(remaining / 3600, remaining % 3600 / 60, remaining % 60),
                            Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                    IconButton(onClick = { flagged[question.question.id] = !(flagged[question.question.id] ?: false); save() }) {
                        Icon(if (flagged[question.question.id] == true) Icons.Outlined.Flag else Icons.Outlined.OutlinedFlag, "Marcar para revisar", tint = if (flagged[question.question.id] == true) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    IconButton(onClick = { grid = true }) { Icon(Icons.Outlined.GridView, "Mapa das questões") }
                }
                LinearProgressIndicator({ answers.size / questions.size.toFloat() }, Modifier.fillMaxWidth().padding(horizontal = 12.dp))
            }
        }
        Column(Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            MarkdownText(question.question.statement)
            val trueFalse = question.options.map { it.key }.toSet() == setOf("C", "E")
            question.options.sortedBy { it.position }.forEach { option ->
                val selected = answers[question.question.id] == option.key
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(if (selected) 2.dp else 1.dp, if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant),
                    color = if (selected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,
                    modifier = Modifier.fillMaxWidth().selectable(selected = selected) {
                        // Tocar de novo na marcada desmarca: deixar em branco é uma escolha de prova.
                        if (selected) answers.remove(question.question.id) else answers[question.question.id] = option.key
                        save()
                    },
                ) {
                    Row(Modifier.padding(14.dp), verticalAlignment = Alignment.Top) {
                        Box(
                            Modifier.size(28.dp).clip(CircleShape).background(if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant),
                            contentAlignment = Alignment.Center,
                        ) { Text(option.key, fontWeight = FontWeight.Bold, color = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant) }
                        Spacer(Modifier.width(12.dp))
                        if (trueFalse) Text(if (option.key == "C") "Certo" else "Errado", Modifier.weight(1f), fontWeight = FontWeight.SemiBold)
                        else MarkdownText(option.text, Modifier.weight(1f))
                    }
                }
            }
        }
        Row(Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedButton(onClick = { index = (safeIndex - 1).coerceAtLeast(0) }, enabled = safeIndex > 0, modifier = Modifier.weight(1f)) { Text("Anterior") }
            if (safeIndex < questions.lastIndex) Button(onClick = { index = safeIndex + 1 }, modifier = Modifier.weight(1f)) { Text("Próxima") }
            else Button(onClick = { confirmSubmit = true }, modifier = Modifier.weight(1f)) { Text("Entregar") }
        }
    }

    if (grid) ModalBottomSheet(onDismissRequest = { grid = false }) {
        Column(Modifier.padding(horizontal = 20.dp).padding(bottom = 32.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("Mapa da prova", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Text("Azul: respondida · Contorno laranja: marcada para revisar · Cinza: em branco", style = MaterialTheme.typography.bodySmall)
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                questions.forEachIndexed { position, row ->
                    val answered = row.question.id in answers
                    val isFlagged = flagged[row.question.id] == true
                    Box(
                        Modifier.size(44.dp).clip(RoundedCornerShape(10.dp))
                            .background(if (answered) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant)
                            .border(if (isFlagged) 3.dp else if (position == safeIndex) 2.dp else 0.dp, if (isFlagged) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.onSurface, RoundedCornerShape(10.dp))
                            .clickable { index = position; grid = false },
                        contentAlignment = Alignment.Center,
                    ) { Text("${position + 1}", color = if (answered) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant, fontWeight = FontWeight.Bold) }
                }
            }
            Button(onClick = { grid = false; confirmSubmit = true }, modifier = Modifier.fillMaxWidth()) { Text("Entregar a prova") }
        }
    }
    if (confirmSubmit) {
        val blank = questions.size - answers.size
        val toReview = flagged.count { it.value }
        AlertDialog(
            onDismissRequest = { confirmSubmit = false },
            title = { Text("Entregar a prova?") },
            text = {
                Text(buildString {
                    append("Você respondeu ${answers.size} de ${questions.size}.")
                    if (blank > 0) append(" $blank ficaram em branco e contam como erro.")
                    if (toReview > 0) append(" $toReview estão marcadas para revisar.")
                    append(" Depois de entregar, o gabarito aparece e não dá para mudar as respostas.")
                })
            },
            confirmButton = { TextButton(onClick = { confirmSubmit = false; submit() }, enabled = !submitting) { Text("Entregar") } },
            dismissButton = { TextButton(onClick = { confirmSubmit = false }) { Text("Voltar à prova") } },
        )
    }
    if (confirmExit) AlertDialog(
        onDismissRequest = { confirmExit = false },
        title = { Text("Sair da prova?") },
        text = { Text("Suas respostas ficam salvas e o cronômetro pausa. Você continua de onde parou pela tela de simulados.") },
        confirmButton = { TextButton(onClick = { confirmExit = false; save(); onExit() }) { Text("Sair e continuar depois") } },
        dismissButton = { TextButton(onClick = { confirmExit = false }) { Text("Ficar") } },
    )
    if (timeUp && !submitting) AlertDialog(
        onDismissRequest = {},
        title = { Text("Tempo esgotado") },
        text = { Text("Na prova de verdade, a folha seria recolhida agora. Você pode entregar como está ou continuar sem limite (o tempo extra aparece no resultado).") },
        confirmButton = { TextButton(onClick = { submit() }) { Text("Entregar agora") } },
        dismissButton = { TextButton(onClick = { timeUp = false; overtime = true }) { Text("Continuar") } },
    )
}
