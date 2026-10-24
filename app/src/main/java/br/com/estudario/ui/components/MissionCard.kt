package br.com.estudario.ui.components

import androidx.compose.ui.draw.drawBehind

import br.com.estudario.ui.planner.taskTitlePtBr
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import br.com.estudario.ui.brand.ElevatedCard
import br.com.estudario.ui.brand.FilledTonalButton
import br.com.estudario.ui.brand.OutlinedButton
import br.com.estudario.ui.brand.Button
import br.com.estudario.ui.brand.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.clickable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import br.com.estudario.domain.planner.PlanTaskStatus
import br.com.estudario.domain.planner.PlanTaskType
import br.com.estudario.domain.ProgressEngine
import br.com.estudario.ui.planner.PlannerTaskUi
import br.com.estudario.ui.planner.completionActionPtBr
import br.com.estudario.ui.planner.displayNamePtBr
import br.com.estudario.ui.planner.minutesLabelPtBr

/** A teoria vigente de cada tópico no plano (para o cartão de questões dizer onde ela está). */
val LocalTheoryByTopic = androidx.compose.runtime.staticCompositionLocalOf<Map<Long, br.com.estudario.data.local.planner.PlanTaskEntity>> { emptyMap() }

/** Abre o tópico de uma tarefa ao tocar no nome dela (dado pelo plano; concluída ou não). */
val LocalOpenTaskTopic = androidx.compose.runtime.staticCompositionLocalOf<((Long) -> Unit)?> { null }

@Composable
fun MissionCard(
    taskUi: PlannerTaskUi,
    modifier: Modifier = Modifier,
    onStart: () -> Unit,
    onReprogram: (() -> Unit)? = null,
    onSkip: (() -> Unit)? = null,
    isOverdue: Boolean = false
) {
    val task = taskUi.entity
    // Atrasada é a tarefa de um dia que já passou e ainda não foi feita, em qualquer tela (Hoje,
    // Semana, Mês, um dia antigo aberto no calendário), não só na lista de atrasadas. Antes, fora
    // dessa lista, o cartão mostrava "Começar" e reprogramar não era possível.
    // "Não realizada" também é pendente: antes caía no botão genérico, que dizia Reprogramar mas abria a tarefa.
    val pending = task.status == PlanTaskStatus.PLANEJADA || task.status == PlanTaskStatus.EM_ANDAMENTO || task.status == PlanTaskStatus.NAO_REALIZADA || task.status == PlanTaskStatus.REPROGRAMADA
    val isOverdue = pending && (isOverdue || task.scheduledEpochDay < java.time.LocalDate.now().toEpochDay())
    // Tarefa de um dia que ainda vem: o botão é "Adiantar" (no Grátis, adiantar pede um anúncio curto,
    // oferecido só quando a pessoa toca aqui).
    val isFuture = pending && !isOverdue && task.scheduledEpochDay > java.time.LocalDate.now().toEpochDay()
    val statusColor = when (task.status) {
        PlanTaskStatus.CONCLUIDA -> Color(0xFF087F5B)
        PlanTaskStatus.EM_ANDAMENTO -> MaterialTheme.colorScheme.primary
        PlanTaskStatus.PLANEJADA -> if (isOverdue) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.outline
        else -> MaterialTheme.colorScheme.outlineVariant
    }

    val typeIcon = when (task.type) {
        PlanTaskType.THEORY -> Icons.Outlined.MenuBook
        PlanTaskType.QUESTIONS -> Icons.Outlined.Checklist
        PlanTaskType.REVIEW -> Icons.Outlined.Autorenew
        PlanTaskType.ACTIVE_RECALL -> Icons.Outlined.Psychology
        PlanTaskType.FLASHCARDS -> Icons.Outlined.Style
        PlanTaskType.SIMULATION -> Icons.Outlined.Assignment
        PlanTaskType.DISCURSIVE -> Icons.Outlined.EditNote
    }

    ElevatedCard(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        )
    ) {
        // A margem do caderno: uma faixa com a cor do estado (dourada planejada, roxa em andamento,
        // verde concluída, vermelha atrasada).
        val accent = when {
            task.status == PlanTaskStatus.CONCLUIDA -> Color(0xFF2FBF8A)
            isOverdue -> MaterialTheme.colorScheme.error
            task.status == PlanTaskStatus.EM_ANDAMENTO -> MaterialTheme.colorScheme.primary
            else -> Color(0xFFF5B83D)
        }
        Column(
            modifier = Modifier
                .drawBehind { drawRect(accent, size = androidx.compose.ui.geometry.Size(5.dp.toPx(), size.height)) }
                .padding(start = 20.dp, end = 16.dp, top = 16.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(RoundedCornerShape(5.dp))
                            .background(statusColor)
                    )
                    Text(
                        text = task.subjectNameSnapshot.uppercase(),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = statusColor,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                
                AssistChip(
                    onClick = { },
                    label = { Text(task.type.displayNamePtBr(), maxLines = 1, overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis) },
                    leadingIcon = { Icon(typeIcon, contentDescription = null, modifier = Modifier.size(16.dp)) },
                    colors = AssistChipDefaults.assistChipColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    border = null
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                // Tocar no nome abre o conteúdo do tópico, mesmo com a tarefa concluída.
                val openTopic = LocalOpenTaskTopic.current
                val topicId = task.topicId
                Text(
                    text = task.taskTitlePtBr(),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = if (openTopic != null && topicId != null) Modifier.clip(androidx.compose.foundation.shape.RoundedCornerShape(6.dp)).clickable(onClickLabel = "Abrir o conteúdo") { openTopic(topicId) } else Modifier,
                )
                
                // Questões de um tópico: onde está a teoria dele (feita, atrasada ou marcada para outro dia).
                if (task.type == PlanTaskType.QUESTIONS && topicId != null && task.status != PlanTaskStatus.CONCLUIDA) {
                    val theoryTask = LocalTheoryByTopic.current[topicId]
                    val today = java.time.LocalDate.now().toEpochDay()
                    fun date(day: Long) = java.time.LocalDate.ofEpochDay(day).format(java.time.format.DateTimeFormatter.ofPattern("d MMM", java.util.Locale("pt", "BR")))
                    val (text, warn) = when {
                        theoryTask == null -> "Teoria deste tópico fora do plano: confira o conteúdo antes" to true
                        theoryTask.status == PlanTaskStatus.CONCLUIDA -> "Teoria feita em ${date(theoryTask.scheduledEpochDay)}" to false
                        theoryTask.status == PlanTaskStatus.NAO_REALIZADA -> "Teoria pulada em ${date(theoryTask.scheduledEpochDay)}: leia o conteúdo antes" to true
                        theoryTask.scheduledEpochDay < today -> "Teoria atrasada desde ${date(theoryTask.scheduledEpochDay)}: faça ela antes" to true
                        theoryTask.scheduledEpochDay > task.scheduledEpochDay -> "Teoria marcada para ${date(theoryTask.scheduledEpochDay)}" to true
                        else -> "Teoria no mesmo dia, antes destas questões" to false
                    }
                    Text(
                        text,
                        modifier = Modifier.clip(androidx.compose.foundation.shape.RoundedCornerShape(50)).background(if (warn) Color(0x2EF2A900) else MaterialTheme.colorScheme.surfaceVariant).then(if (openTopic != null) Modifier.clickable { openTopic(topicId) } else Modifier).padding(horizontal = 10.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (warn) Color(0xFFD98A00) else MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                // FlowRow: "45 min • 20 qts • Prioridade alta" não cabe numa linha em tela estreita
                // ou com fonte maior; os pedaços descem para a linha de baixo em vez de serem cortados.
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                ) {
                    Text(
                        text = minutesLabelPtBr(task.plannedMinutes),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    if (task.plannedQuestions > 0) {
                        Text("•", color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(
                            text = "${task.plannedQuestions} qts",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Text("•", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(
                        text = "Prioridade ${task.priority.displayNamePtBr()}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Botões e XP: lado a lado quando cabem; senão o XP desce para a linha de baixo.
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    if (task.status == PlanTaskStatus.CONCLUIDA) {
                        FilledTonalButton(
                            onClick = { },
                            enabled = false,
                            colors = ButtonDefaults.filledTonalButtonColors(
                                disabledContainerColor = Color(0xFFE6FCF5),
                                disabledContentColor = Color(0xFF087F5B)
                            )
                        ) {
                            Icon(Icons.Outlined.CheckCircle, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(8.dp))
                            Text("Concluída")
                        }
                    } else if (isOverdue && onReprogram != null) {
                        Button(
                            onClick = onReprogram,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.error
                            )
                        ) {
                            Text("Reprogramar")
                        }
                        // Quem quer fazer mesmo atrasado ainda pode: abre a matéria como antes.
                        OutlinedButton(onClick = onStart) {
                            Text(if (task.status == PlanTaskStatus.EM_ANDAMENTO) "Continuar" else "Fazer agora")
                        }
                        if (onSkip != null) {
                            TextButton(onClick = { onSkip.invoke() }) {
                                Text("Pular")
                            }
                        }
                    } else {
                        Button(
                            onClick = onStart,
                        ) {
                            if (isFuture) {
                                Icon(Icons.Outlined.FastForward, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(Modifier.width(6.dp))
                                Text("Adiantar")
                            } else Text(completionActionPtBr(task.status))
                        }
                        if (onSkip != null) {
                            TextButton(onClick = { onSkip.invoke() }) {
                                Text("Pular")
                            }
                        }
                    }
                }
                
                val xpBase = task.plannedMinutes + (task.plannedQuestions * 2)
                XpTag(
                    reward = ProgressEngine.XpReward(base = xpBase),
                    earned = task.status == PlanTaskStatus.CONCLUIDA,
                    modifier = Modifier.align(Alignment.CenterVertically),
                )
            }
        }
    }
}
