package br.com.estudario.ui.planner

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.estudario.ui.components.EmptyState
import br.com.estudario.ui.components.MissionCard
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun CalendarScreen(
    state: ActivePlanUiState,
    onOpenTopic: (Long) -> Unit,
    onStart: (String) -> Unit,
    onFocus: (PlannerTaskUi) -> Unit,
    onComplete: (PlannerTaskUi) -> Unit,
    onReprogram: (PlannerTaskUi) -> Unit,
    onSkip: (PlannerTaskUi) -> Unit,
    onToggleLock: (String, Boolean) -> Unit,
    onToggleDayLock: (LocalDate, Boolean) -> Unit,
    onGenerate: () -> Unit,
    onSyncCalendar: () -> Unit
) {
    var selectedDate by remember { mutableStateOf(state.today) }
    
    val tasksForSelectedDate = state.tasks.filter { it.entity.scheduledEpochDay == selectedDate.toEpochDay() }
    val plannedMinutes = tasksForSelectedDate.sumOf { it.entity.plannedMinutes }
    val actualMinutes = tasksForSelectedDate.sumOf { it.actualMinutes }
    
    val capacity = state.availability.firstOrNull { it.dayOfWeek == selectedDate.dayOfWeek.value }?.let { if (it.unavailable) 0 else it.availableMinutes } ?: 0
    val isDayLocked = state.dayOverrides.firstOrNull { it.epochDay == selectedDate.toEpochDay() }?.locked == true

    val taskDates = state.tasks.map { LocalDate.ofEpochDay(it.entity.scheduledEpochDay) }.toSet()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Calendário Interativo
        item {
            ElevatedCard(Modifier.fillMaxWidth()) {
                InteractiveCalendar(
                    selectedDate = selectedDate,
                    onDateSelected = { selectedDate = it },
                    taskDates = taskDates,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }

        // Header do dia e ações
        item {
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = if (selectedDate == state.today) "Hoje" else "${selectedDate.dayOfMonth} de ${selectedDate.month.getDisplayName(TextStyle.FULL, Locale("pt", "BR"))}",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                
                TextButton(
                    onClick = { onToggleDayLock(selectedDate, !isDayLocked) },
                    colors = ButtonDefaults.textButtonColors(contentColor = if (isDayLocked) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary)
                ) {
                    Icon(if (isDayLocked) Icons.Outlined.Lock else Icons.Outlined.LockOpen, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text(if (isDayLocked) "Dia trancado" else "Trancar folga")
                }
            }
        }

        // Summary do dia selecionado
        item {
            val completionPercent = if (tasksForSelectedDate.isEmpty()) 0 else (tasksForSelectedDate.count { it.entity.status == br.com.estudario.domain.planner.PlanTaskStatus.CONCLUIDA } * 100) / tasksForSelectedDate.size
            PlannerSummary(
                plannedMinutes = plannedMinutes,
                actualMinutes = actualMinutes,
                deficitMinutes = if (plannedMinutes > capacity) plannedMinutes - capacity else 0,
                completionPercent = completionPercent
            )
        }

        // Atrasadas (exibir apenas se o dia selecionado for Hoje)
        if (selectedDate == state.today && state.overdueTasks.isNotEmpty()) {
            item {
                Text(
                    text = "Atrasadas",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
            items(state.overdueTasks, key = { "overdue_${it.entity.id}" }) { taskUi ->
                MissionCard(
                    taskUi = taskUi,
                    onStart = { onFocus(taskUi) },
                    onReprogram = { onReprogram(taskUi) },
                    onSkip = { onSkip(taskUi) },
                    isOverdue = true
                )
            }
        }

        // Missões do dia selecionado
        if (tasksForSelectedDate.isEmpty()) {
            item {
                if (isDayLocked) {
                    EmptyState(
                        title = "Folga programada",
                        body = "Você trancou este dia para não receber atividades do motor de plano.",
                        actionLabel = "Destrancar dia",
                        onAction = { onToggleDayLock(selectedDate, false) }
                    )
                } else if (selectedDate < state.today) {
                    EmptyState(
                        title = "Nenhuma atividade",
                        body = "Não houve tarefas registradas para este dia.",
                        actionLabel = null,
                    )
                } else {
                    EmptyState(
                        title = "Dia livre",
                        body = "Não há atividades planejadas para este dia.",
                        actionLabel = "Gerar planejamento",
                        onAction = onGenerate
                    )
                }
            }
        } else {
            item {
                Text(
                    text = "Atividades Planejadas",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
            items(tasksForSelectedDate, key = { it.entity.id }) { taskUi ->
                MissionCard(
                    taskUi = taskUi,
                    onStart = { onFocus(taskUi) },
                    onReprogram = { onReprogram(taskUi) },
                    onSkip = { onSkip(taskUi) },
                    isOverdue = false
                )
            }
        }

        // Integração e Previsão (só mostra no "Hoje")
        if (selectedDate == state.today) {
            item {
                ElevatedCard(
                    Modifier.fillMaxWidth().padding(top = 16.dp),
                    colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(Icons.Outlined.EventAvailable, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        Text("Previsão de conclusão", fontWeight = FontWeight.Bold)
                        }
                        Text(
                            text = state.forecastDate?.let {
                                "Com sua disponibilidade de tempo e o tamanho do edital atual, a demanda terminará aproximadamente em ${forecastDateLabelPtBr(it)}."
                            } ?: "A previsão depende da análise do edital com a disponibilidade cadastrada.",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        
                        Divider(color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.2f))
                        
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(Icons.Outlined.Sync, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            Text("Sincronizar com a Agenda", fontWeight = FontWeight.Bold)
                        }
                        Text(
                            text = "Exporte e mantenha suas missões diárias sincronizadas com o Google Calendar / Agenda nativa do seu celular.",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Button(
                            onClick = onSyncCalendar,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Ativar Sincronização")
                        }
                    }
                }
            }
        }
    }
}
