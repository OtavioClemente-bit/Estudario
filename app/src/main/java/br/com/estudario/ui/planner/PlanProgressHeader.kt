package br.com.estudario.ui.planner

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.EventAvailable
import androidx.compose.material.icons.outlined.Flag
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.estudario.domain.planner.PlanTaskStatus
import java.time.format.DateTimeFormatter
import java.util.Locale

private val portugueseDateFormatter = DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy", Locale("pt", "BR"))

@Composable
fun PlanProgressHeader(state: ActivePlanUiState, modifier: Modifier = Modifier) {
    val totalTasks = state.tasks.size
    val completedTasks = state.tasks.count { it.entity.status == PlanTaskStatus.CONCLUIDA }
    val completionPercent = if (totalTasks == 0) 0 else (completedTasks * 100 / totalTasks).coerceIn(0, 100)
    val plannedMinutes = state.tasks.sumOf { it.entity.plannedMinutes }
    val actualMinutes = state.tasks.sumOf { it.actualMinutes }

    ElevatedCard(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("Progresso do plano", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Text(
                        state.activePlan?.name ?: "Seu planejamento de estudos",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        maxLines = 2,
                    )
                }
                Surface(
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    shape = MaterialTheme.shapes.small,
                ) {
                    Text(
                        state.selectedSection.label,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Medium,
                    )
                }
            }

            LinearProgressIndicator(
                progress = { completionPercent / 100f },
                modifier = Modifier.fillMaxWidth().height(8.dp),
            )
            Text(
                "$completedTasks de $totalTasks missões concluídas · $completionPercent% do plano",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
            )

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                PlanMetric(
                    modifier = Modifier.weight(1f),
                    icon = { Icon(Icons.Outlined.Flag, contentDescription = null) },
                    value = "$completedTasks/$totalTasks",
                    label = "Missões",
                )
                PlanMetric(
                    modifier = Modifier.weight(1f),
                    icon = { Icon(Icons.Outlined.Timer, contentDescription = null) },
                    value = minutesLabelPtBr(plannedMinutes),
                    label = "Tempo planejado",
                )
                PlanMetric(
                    modifier = Modifier.weight(1f),
                    icon = { Icon(Icons.Outlined.Timer, contentDescription = null) },
                    value = minutesLabelPtBr(actualMinutes),
                    label = "Tempo realizado",
                )
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.16f))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Icon(Icons.Outlined.EventAvailable, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text("Previsão de conclusão", fontWeight = FontWeight.Bold)
                    Text(
                        state.forecastDate?.format(portugueseDateFormatter) ?: "Ainda calculando com sua disponibilidade",
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
        }
    }
}

@Composable
private fun PlanMetric(
    modifier: Modifier,
    icon: @Composable () -> Unit,
    value: String,
    label: String,
) {
    Column(modifier, verticalArrangement = Arrangement.spacedBy(4.dp)) {
        icon()
        Text(value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Text(label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}
