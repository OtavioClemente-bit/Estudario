package br.com.estudario.ui.planner

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.com.estudario.data.local.planner.PerceivedDifficulty
import br.com.estudario.data.planner.CompleteTaskInput
import br.com.estudario.domain.ProgressEngine
import br.com.estudario.ui.components.XpTag
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight

@Composable
fun TaskExecutionDialog(task: PlannerTaskUi, measuredMinutes: Int? = null, onDismiss: () -> Unit, onConfirm: (CompleteTaskInput) -> Unit) {
    // Quando a sessão veio do modo foco, os minutos já vêm do cronômetro — nada de chutar.
    var minutes by remember { mutableStateOf((measuredMinutes?.takeIf { it > 0 } ?: task.entity.plannedMinutes).toString()) }
    var questions by remember { mutableStateOf(task.entity.plannedQuestions.toString()) }
    var correct by remember { mutableStateOf("0") }
    var note by remember { mutableStateOf("") }
    var difficulty by remember { mutableStateOf(PerceivedDifficulty.NORMAL) }
    val m = minutes.toIntOrNull(); val q = questions.toIntOrNull(); val c = correct.toIntOrNull()
    val valid = m != null && m >= 0 && q != null && q >= 0 && c != null && c in 0..q
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Concluir atividade", modifier = Modifier.weight(1f))
                // O selo acompanha o que está sendo digitado: a pessoa vê o XP subir enquanto preenche.
                XpTag(ProgressEngine.XpReward(ProgressEngine.earnedPlanTask(task.entity.type, m ?: 0, (c ?: 0).coerceAtMost(q ?: 0))), earned = true)
            }
        },
        text = { Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(minutes, { minutes = it }, label = { Text("Minutos realizados") }, singleLine = true)
            if (measuredMinutes != null && measuredMinutes > 0) Text(
                "Tempo medido no modo foco: $measuredMinutes min.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary,
            )
            OutlinedTextField(questions, { questions = it }, label = { Text("Questões") }, singleLine = true)
            OutlinedTextField(correct, { correct = it }, label = { Text("Acertos") }, singleLine = true, isError = c != null && q != null && c !in 0..q)
            FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) { PerceivedDifficulty.entries.forEach { value -> FilterChip(difficulty == value, { difficulty = value }, { Text(when(value) { PerceivedDifficulty.EASY -> "Fácil"; PerceivedDifficulty.NORMAL -> "Normal"; PerceivedDifficulty.HARD -> "Difícil" }) }) } }
            OutlinedTextField(note, { note = it }, label = { Text("Observação opcional") }, modifier = Modifier.fillMaxWidth())
            if (!valid) Text("Use números não negativos e acertos entre zero e o total.", color = MaterialTheme.colorScheme.error)
            else Text(
                "Você vai receber ${ProgressEngine.earnedPlanTask(task.entity.type, m ?: 0, (c ?: 0).coerceAtMost(q ?: 0))} XP por esta atividade.",
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.tertiary,
            )
        } },
        confirmButton = { TextButton(enabled = valid, onClick = { val now = System.currentTimeMillis(); onConfirm(CompleteTaskInput(now - (m!! * 60_000L), now, m, q!!, c!!, note, difficulty)); onDismiss() }) { Text("Salvar execução") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } },
    )
}
