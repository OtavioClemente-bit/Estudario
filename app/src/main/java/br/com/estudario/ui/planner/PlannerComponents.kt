package br.com.estudario.ui.planner

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.foundation.clickable
import androidx.compose.foundation.background
import androidx.compose.material.icons.outlined.ExpandLess
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.material.icons.outlined.CheckCircle
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

internal fun minutesLabel(value: Int) = minutesLabelPtBr(value)
internal fun PlanTaskType.label() = displayNamePtBr()
internal fun PlanTaskStatus.label() = displayNamePtBr()

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

@Composable
fun PlannerSummary(plannedMinutes: Int, actualMinutes: Int, deficitMinutes: Int = 0, completionPercent: Int) {
    ElevatedCard(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(Icons.Outlined.CheckCircle, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Text("Progresso de Hoje", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(Modifier.weight(1f))
                Text("$completionPercent%", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            }
            
            LinearProgressIndicator(
                progress = { completionPercent / 100f },
                modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                AssistChip(onClick = {}, label = { Text("${minutesLabel(plannedMinutes)} planejadas") })
                AssistChip(onClick = {}, label = { Text("${minutesLabel(actualMinutes)} realizadas") })
            }
            if (deficitMinutes > 0) Text("Capacidade insuficiente: faltam ${minutesLabel(deficitMinutes)}.", color = MaterialTheme.colorScheme.error)
        }
    }
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
