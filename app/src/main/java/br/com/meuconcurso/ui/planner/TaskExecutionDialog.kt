package br.com.meuconcurso.ui.planner

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.com.meuconcurso.data.local.planner.PerceivedDifficulty
import br.com.meuconcurso.data.planner.CompleteTaskInput

@Composable
fun TaskExecutionDialog(task: PlannerTaskUi, onDismiss: () -> Unit, onConfirm: (CompleteTaskInput) -> Unit) {
    var minutes by remember { mutableStateOf(task.entity.plannedMinutes.toString()) }
    var questions by remember { mutableStateOf(task.entity.plannedQuestions.toString()) }
    var correct by remember { mutableStateOf("0") }
    var note by remember { mutableStateOf("") }
    var difficulty by remember { mutableStateOf(PerceivedDifficulty.NORMAL) }
    val m = minutes.toIntOrNull(); val q = questions.toIntOrNull(); val c = correct.toIntOrNull()
    val valid = m != null && m >= 0 && q != null && q >= 0 && c != null && c in 0..q
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Concluir atividade") },
        text = { Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(minutes, { minutes = it }, label = { Text("Minutos realizados") }, singleLine = true)
            OutlinedTextField(questions, { questions = it }, label = { Text("Questões") }, singleLine = true)
            OutlinedTextField(correct, { correct = it }, label = { Text("Acertos") }, singleLine = true, isError = c != null && q != null && c !in 0..q)
            FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) { PerceivedDifficulty.entries.forEach { value -> FilterChip(difficulty == value, { difficulty = value }, { Text(when(value) { PerceivedDifficulty.EASY -> "Fácil"; PerceivedDifficulty.NORMAL -> "Normal"; PerceivedDifficulty.HARD -> "Difícil" }) }) } }
            OutlinedTextField(note, { note = it }, label = { Text("Observação opcional") }, modifier = Modifier.fillMaxWidth())
            if (!valid) Text("Use números não negativos e acertos entre zero e o total.", color = MaterialTheme.colorScheme.error)
        } },
        confirmButton = { TextButton(enabled = valid, onClick = { val now = System.currentTimeMillis(); onConfirm(CompleteTaskInput(now - (m!! * 60_000L), now, m, q!!, c!!, note, difficulty)); onDismiss() }) { Text("Salvar execução") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } },
    )
}
