package br.com.estudario.ui.planner

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import br.com.estudario.ui.brand.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import br.com.estudario.domain.planner.PlanTaskStatus
import br.com.estudario.ui.theme.EstudarioShapes
import java.time.LocalDate
import java.time.temporal.ChronoUnit

/**
 * O plano em uma olhada: para onde ele vai (prova e previsão), quanto já foi, e a missão de hoje.
 * É a primeira coisa da aba; a pessoa precisa entender o próprio plano sem abrir nada.
 */
@Composable
fun PlanProgressHeader(state: ActivePlanUiState, modifier: Modifier = Modifier) {
    val plan = state.activePlan
    val currentTasks = state.tasks.plannedLoadTasks()
    val totalTasks = currentTasks.size
    val completedTasks = currentTasks.count { it.entity.status == PlanTaskStatus.CONCLUIDA }
    val completionPercent = if (totalTasks == 0) 0 else (completedTasks * 100 / totalTasks).coerceIn(0, 100)
    val today = state.todayTasks.plannedLoadTasks()
    val todayDone = today.count { it.entity.status == PlanTaskStatus.CONCLUIDA }
    val examDate = plan?.examEpochDay?.let(LocalDate::ofEpochDay)
    val daysToExam = examDate?.let { ChronoUnit.DAYS.between(state.today, it) }?.takeIf { it >= 0 }
    val onHero = MaterialTheme.colorScheme.onPrimaryContainer

    Surface(shape = EstudarioShapes.spotlight, color = MaterialTheme.colorScheme.primaryContainer, modifier = modifier.fillMaxWidth()) {
        Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text("SEU PLANO", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    Text(plan?.objective?.takeIf { it.isNotBlank() } ?: plan?.name ?: "Plano de estudos", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = onHero, maxLines = 2, overflow = TextOverflow.Ellipsis)
                }
                if (daysToExam != null) Column(horizontalAlignment = Alignment.End) {
                    Text("$daysToExam", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = onHero)
                    Text(if (daysToExam == 1L) "dia para a prova" else "dias para a prova", style = MaterialTheme.typography.labelSmall, color = onHero)
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Row {
                    Text("$completionPercent% do plano cumprido", Modifier.weight(1f), style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.SemiBold, color = onHero)
                    Text("$completedTasks/$totalTasks", style = MaterialTheme.typography.labelLarge, color = onHero)
                }
                Bar(completionPercent / 100f, MaterialTheme.colorScheme.primary)
                Text(
                    state.forecastDate?.let { "No ritmo atual você fecha o edital em ${forecastDateLabelPtBr(it)}." } ?: "Calculando a previsão de conclusão…",
                    style = MaterialTheme.typography.bodySmall, color = onHero,
                )
            }

            // Missão de hoje: o que o plano pede agora, em números simples.
            Surface(shape = EstudarioShapes.row, color = MaterialTheme.colorScheme.surface.copy(alpha = 0.55f)) {
                Row(Modifier.fillMaxWidth().padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text("Missão de hoje", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                        Text(
                            when {
                                today.isEmpty() -> "Dia livre no plano."
                                todayDone == today.size -> "Concluída. Amanhã o plano continua."
                                else -> "$todayDone de ${today.size} atividades · ${minutesLabelPtBr(state.todayActualMinutes)} de ${minutesLabelPtBr(state.todayPlannedMinutes)}"
                            },
                            style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                    if (state.overdueTasks.isNotEmpty()) Surface(shape = CircleShape, color = MaterialTheme.colorScheme.errorContainer) {
                        Text("${state.overdueTasks.size} atrasada(s)", Modifier.padding(horizontal = 10.dp, vertical = 4.dp), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onErrorContainer)
                    }
                }
            }
        }
    }
}

@Composable
private fun Bar(fraction: Float, color: Color) {
    Box(Modifier.fillMaxWidth().height(8.dp).clip(CircleShape).background(MaterialTheme.colorScheme.surface.copy(alpha = 0.6f))) {
        Box(Modifier.fillMaxWidth(fraction.coerceIn(0.015f, 1f)).fillMaxHeight().clip(CircleShape).background(color))
    }
}
