package br.com.estudario.ui.planner

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.foundation.clickable
import androidx.compose.foundation.background
import androidx.compose.material.icons.outlined.ExpandLess
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.LockOpen
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.rounded.Bolt
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.sp
import br.com.estudario.domain.ProgressEngine
import br.com.estudario.domain.planner.PlanTaskStatus
import br.com.estudario.domain.planner.PlanTaskType
import br.com.estudario.ui.components.XpTag

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

/**
 * A recompensa desta tarefa: o que ela ainda vale, ou o que já rendeu depois de concluída.
 * Sai das mesmas regras que pagam o XP, então o anunciado é o que cai.
 */
internal fun PlannerTaskUi.reward(): ProgressEngine.XpReward =
    if (entity.status == PlanTaskStatus.CONCLUIDA) {
        ProgressEngine.XpReward(ProgressEngine.earnedPlanTask(entity.type, actualMinutes, correctAnswers))
    } else {
        ProgressEngine.previewPlanTask(entity.type, entity.plannedMinutes, entity.plannedQuestions)
    }

/**
 * O quadro de missão do dia: quanto XP as atividades de hoje valem, quanto já foi conquistado e
 * quanto ainda está na mesa. É o que transforma a lista de tarefas em algo que dá vontade de fechar.
 */
@Composable
fun RecompensaDoDia(tasks: List<PlannerTaskUi>) {
    if (tasks.isEmpty()) return
    val concluidas = tasks.filter { it.entity.status == PlanTaskStatus.CONCLUIDA }
    val pendentes = tasks - concluidas.toSet()
    val ganho = concluidas.sumOf { it.reward().base }
    val disponivel = pendentes.sumOf { it.reward().base }
    val teto = pendentes.sumOf { it.reward().max }
    val total = (ganho + disponivel).coerceAtLeast(1)
    val cor = MaterialTheme.colorScheme.tertiary

    ElevatedCard(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(
                    Modifier.size(30.dp).clip(RoundedCornerShape(50)).background(cor.copy(alpha = 0.16f)),
                    contentAlignment = Alignment.Center,
                ) { Icon(Icons.Rounded.Bolt, null, Modifier.size(18.dp), tint = cor) }
                Column(Modifier.weight(1f)) {
                    Text("Missão de hoje", fontWeight = FontWeight.Bold)
                    Text(
                        "${concluidas.size} de ${tasks.size} atividade(s) concluída(s)",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("+$disponivel", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black, color = cor)
                    Text("XP na mesa", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            LinearProgressIndicator({ ganho.toFloat() / total }, Modifier.fillMaxWidth().clip(RoundedCornerShape(50)))
            Text(
                if (disponivel == 0) "Tudo fechado: $ganho XP conquistados hoje."
                else "$ganho XP conquistados" + if (teto > disponivel) " • até +$teto se acertar tudo." else ".",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
fun PlannerSummary(planned: Int, actual: Int, deficit: Int = 0) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        AssistChip(onClick = {}, label = { Text("${minutesLabel(planned)} planejadas") })
        AssistChip(onClick = {}, label = { Text("${minutesLabel(actual)} realizadas") })
    }
    if (deficit > 0) Text("Capacidade insuficiente: faltam ${minutesLabel(deficit)}.", color = MaterialTheme.colorScheme.error)
}

/**
 * O plano sem IA não é caixa-preta: aqui ficam, em uma frase cada, as regras que produziram as
 * tarefas desta rodada — fase atual, divisão do tempo, revisões atrasadas, rodízio das matérias.
 */
@Composable
fun PorQueEstePlano(notes: List<String>) {
    var aberto by remember { mutableStateOf(false) }
    ElevatedCard(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                Modifier.fillMaxWidth().clickable { aberto = !aberto },
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
            ) {
                Column(Modifier.weight(1f)) {
                    Text("Por que este plano", fontWeight = FontWeight.Bold)
                    if (!aberto) Text(notes.first(), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1)
                }
                Icon(if (aberto) Icons.Outlined.ExpandLess else Icons.Outlined.ExpandMore, if (aberto) "Recolher" else "Ver regras")
            }
            if (aberto) notes.forEach { note ->
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("•", color = MaterialTheme.colorScheme.primary)
                    Text(note, style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}

@Composable
fun PlannerTaskCard(row: PlannerTaskUi, onOpenTopic: (Long) -> Unit, onStart: (String) -> Unit, onFocus: (PlannerTaskUi) -> Unit, onComplete: (PlannerTaskUi) -> Unit, onReprogram: (PlannerTaskUi) -> Unit, onSkip: (PlannerTaskUi) -> Unit, onToggleLock: (String, Boolean) -> Unit) {
    val task = row.entity
    ElevatedCard(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text(task.subjectNameSnapshot, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                XpTag(row.reward(), earned = task.status == PlanTaskStatus.CONCLUIDA)
                IconButton(onClick = { onToggleLock(task.id, !task.locked) }, enabled = task.status != PlanTaskStatus.CONCLUIDA) { Icon(if (task.locked) Icons.Outlined.Lock else Icons.Outlined.LockOpen, if (task.locked) "Desbloquear" else "Bloquear") }
            }
            task.topicNameSnapshot?.let { topic -> TextButton(onClick = { task.topicId?.let(onOpenTopic) }, contentPadding = PaddingValues(0.dp)) { Text(topic) } }
            Text("${minutesLabel(task.plannedMinutes)} • ${task.type.label()}${if (task.plannedQuestions > 0) " • ${task.plannedQuestions} questões" else ""}")
            Text("${task.status.label()}${if (row.actualMinutes > 0) " • ${minutesLabel(row.actualMinutes)} realizadas" else ""}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            if (!task.locked && task.status !in setOf(PlanTaskStatus.CONCLUIDA, PlanTaskStatus.REPROGRAMADA)) FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                if (task.status == PlanTaskStatus.PLANEJADA) TextButton(onClick = { onStart(task.id) }) { Text("Iniciar") }
                // Estudar agora com o tempo medido: a conclusão depois já vem com os minutos reais.
                FilledTonalButton(onClick = { onFocus(row) }) { Text("Modo foco") }
                TextButton(onClick = { onComplete(row) }) { Text("Concluir") }
                TextButton(onClick = { onReprogram(row) }) { Text("Reprogramar") }
                TextButton(onClick = { onSkip(row) }) { Text("Pular") }
            }
        }
    }
}
