package br.com.meuconcurso.ui.planner

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.LockOpen
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.meuconcurso.domain.planner.PlanTaskStatus
import br.com.meuconcurso.domain.planner.PlanTaskType

internal fun minutesLabel(value: Int) = "${value / 60}h${(value % 60).toString().padStart(2, '0')}"
internal fun PlanTaskType.label() = when (this) {
    PlanTaskType.THEORY -> "Teoria"; PlanTaskType.QUESTIONS -> "Questões"; PlanTaskType.REVIEW -> "Revisão"
    PlanTaskType.ACTIVE_RECALL -> "Recordação ativa"; PlanTaskType.FLASHCARDS -> "Flashcards"
    PlanTaskType.SIMULATION -> "Simulado"; PlanTaskType.DISCURSIVE -> "Discursiva"
}
internal fun PlanTaskStatus.label() = when (this) {
    PlanTaskStatus.PLANEJADA -> "Planejada"; PlanTaskStatus.EM_ANDAMENTO -> "Em andamento"; PlanTaskStatus.CONCLUIDA -> "Concluída"
    PlanTaskStatus.REPROGRAMADA -> "Reprogramada"; PlanTaskStatus.NAO_REALIZADA -> "Não realizada"; PlanTaskStatus.PAUSADA -> "Pausada"
}

@Composable
fun PlannerSummary(planned: Int, actual: Int, deficit: Int = 0) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        AssistChip(onClick = {}, label = { Text("${minutesLabel(planned)} planejadas") })
        AssistChip(onClick = {}, label = { Text("${minutesLabel(actual)} realizadas") })
    }
    if (deficit > 0) Text("Capacidade insuficiente: faltam ${minutesLabel(deficit)}.", color = MaterialTheme.colorScheme.error)
}

@Composable
fun PlannerTaskCard(row: PlannerTaskUi, onOpenTopic: (Long) -> Unit, onStart: (String) -> Unit, onComplete: (PlannerTaskUi) -> Unit, onReprogram: (PlannerTaskUi) -> Unit, onSkip: (PlannerTaskUi) -> Unit, onToggleLock: (String, Boolean) -> Unit) {
    val task = row.entity
    ElevatedCard(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(task.subjectNameSnapshot, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                IconButton(onClick = { onToggleLock(task.id, !task.locked) }, enabled = task.status != PlanTaskStatus.CONCLUIDA) { Icon(if (task.locked) Icons.Outlined.Lock else Icons.Outlined.LockOpen, if (task.locked) "Desbloquear" else "Bloquear") }
            }
            task.topicNameSnapshot?.let { topic -> TextButton(onClick = { task.topicId?.let(onOpenTopic) }, contentPadding = PaddingValues(0.dp)) { Text(topic) } }
            Text("${minutesLabel(task.plannedMinutes)} • ${task.type.label()}${if (task.plannedQuestions > 0) " • ${task.plannedQuestions} questões" else ""}")
            Text("${task.status.label()}${if (row.actualMinutes > 0) " • ${minutesLabel(row.actualMinutes)} realizadas" else ""}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            if (!task.locked && task.status !in setOf(PlanTaskStatus.CONCLUIDA, PlanTaskStatus.REPROGRAMADA)) FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                if (task.status == PlanTaskStatus.PLANEJADA) TextButton(onClick = { onStart(task.id) }) { Text("Iniciar") }
                TextButton(onClick = { onComplete(row) }) { Text("Concluir") }
                TextButton(onClick = { onReprogram(row) }) { Text("Reprogramar") }
                TextButton(onClick = { onSkip(row) }) { Text("Pular") }
            }
        }
    }
}
