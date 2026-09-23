package br.com.estudario.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
            containerColor = if (isOverdue && task.status == PlanTaskStatus.PLANEJADA) MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
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
                Text(
                    text = task.topicNameSnapshot ?: "Sessão de estudos",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                
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
                    } else if (isOverdue && task.status == PlanTaskStatus.PLANEJADA) {
                        Button(
                            onClick = { onReprogram?.invoke() },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.error
                            )
                        ) {
                            Text("Reprogramar")
                        }
                        if (onSkip != null) {
                            OutlinedButton(onClick = { onSkip.invoke() }) {
                                Text("Pular")
                            }
                        }
                    } else {
                        Button(
                            onClick = onStart,
                        ) {
                            Text(completionActionPtBr(task.status))
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
