package br.com.estudario.ui.planner

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
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
import br.com.estudario.ui.theme.estudarioLayout
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
    /** Itens que abrem a lista e rolam junto com ela (resumo do plano, abas fixas). */
    header: LazyListScope.() -> Unit = {},
) {
    // Os chips "Acompanhe seu plano" (Hoje/Semana/Mês/Visão geral) escolhem qual destas visões
    // aparece, antes eles só destacavam o próprio chip e a tela sempre mostrava o dia selecionado.
    when (state.selectedSection) {
        PlanSection.TODAY -> DayPlanView(
            state = state,
            onFocus = onFocus,
            onReprogram = onReprogram,
            onSkip = onSkip,
            onToggleDayLock = onToggleDayLock,
            onGenerate = onGenerate,
            header = header,
        )
        PlanSection.WEEK -> WeekPlanView(state = state, onFocus = onFocus, onGenerate = onGenerate, header = header)
        PlanSection.MONTH -> MonthPlanView(state = state, onFocus = onFocus, onGenerate = onGenerate, header = header)
        PlanSection.YEAR -> OverviewPlanView(state = state, header = header)
    }
}

@Composable
private fun DayPlanView(
    state: ActivePlanUiState,
    onFocus: (PlannerTaskUi) -> Unit,
    onReprogram: (PlannerTaskUi) -> Unit,
    onSkip: (PlannerTaskUi) -> Unit,
    onToggleDayLock: (LocalDate, Boolean) -> Unit,
    onGenerate: () -> Unit,
    header: LazyListScope.() -> Unit,
) {
    var selectedDate by remember { mutableStateOf(state.today) }
    
    val tasksForSelectedDate = state.tasks.filter { it.entity.scheduledEpochDay == selectedDate.toEpochDay() }
    val plannedMinutes = tasksForSelectedDate.plannedLoadMinutes()
    val actualMinutes = tasksForSelectedDate.sumOf { it.actualMinutes }
    
    val capacity = state.availability.firstOrNull { it.dayOfWeek == selectedDate.dayOfWeek.value }?.let { if (it.unavailable) 0 else it.availableMinutes } ?: 0
    val isDayLocked = state.dayOverrides.firstOrNull { it.epochDay == selectedDate.toEpochDay() }?.locked == true

    val taskDates = state.tasks.map { LocalDate.ofEpochDay(it.entity.scheduledEpochDay) }.toSet()

    PlanList(header) {
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
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = if (selectedDate == state.today) "Hoje" else "${selectedDate.dayOfMonth} de ${selectedDate.month.getDisplayName(TextStyle.FULL, Locale("pt", "BR"))}",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f),
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
            val completionPercent = tasksForSelectedDate.plannedLoadCompletionPercent()
            PlannerSummary(
                plannedMinutes = plannedMinutes,
                actualMinutes = actualMinutes,
                deficitMinutes = if (plannedMinutes > capacity) plannedMinutes - capacity else 0,
                completionPercent = completionPercent,
                plannedLabel = "planejadas no dia",
                periodTitle = "Progresso do dia",
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

    }
}

// ---------------------------------------------------------------- semana

@Composable
private fun WeekPlanView(
    state: ActivePlanUiState,
    onFocus: (PlannerTaskUi) -> Unit,
    onGenerate: () -> Unit,
    header: LazyListScope.() -> Unit,
) {
    val weekStart = state.weekStart
    val weekEnd = weekStart.plusDays(6)
    val weekTasks = state.weekTasks
    val plannedMinutes = weekTasks.plannedLoadMinutes()
    val actualMinutes = weekTasks.sumOf { it.actualMinutes }
    val completionPercent = weekTasks.plannedLoadCompletionPercent()
    val byDay = weekTasks.groupBy { LocalDate.ofEpochDay(it.entity.scheduledEpochDay) }
    val dayNames = listOf("Segunda-feira", "Terça-feira", "Quarta-feira", "Quinta-feira", "Sexta-feira", "Sábado", "Domingo")

    PlanList(header) {
        item {
            Text(
                "Semana de ${weekStart.dayOfMonth} a ${weekEnd.dayOfMonth} de ${weekEnd.month.getDisplayName(TextStyle.FULL, Locale("pt", "BR")).replaceFirstChar { it.uppercase() }}",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
            )
        }
        item { PlannerSummary(plannedMinutes = plannedMinutes, actualMinutes = actualMinutes, completionPercent = completionPercent, plannedLabel = "planejadas na semana", periodTitle = "Progresso da semana") }
        if (weekTasks.isEmpty()) {
            item { EmptyState("Semana livre", "Não há tarefas planejadas para esta semana.", "Gerar planejamento", onGenerate) }
        } else {
            (0..6).forEach { offset ->
                val date = weekStart.plusDays(offset.toLong())
                val tasks = byDay[date].orEmpty()
                if (tasks.isNotEmpty()) {
                    item {
                        Text(
                            text = dayNames[offset] + " • ${date.dayOfMonth}/${date.monthValue}" + if (date == state.today) " (hoje)" else "",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = if (date == state.today) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(top = 4.dp),
                        )
                    }
                    items(tasks, key = { "week_${it.entity.id}" }) { taskUi ->
                        MissionCard(taskUi = taskUi, onStart = { onFocus(taskUi) })
                    }
                }
            }
        }
    }
}

// ---------------------------------------------------------------- mês

@Composable
private fun MonthPlanView(
    state: ActivePlanUiState,
    onFocus: (PlannerTaskUi) -> Unit,
    onGenerate: () -> Unit,
    header: LazyListScope.() -> Unit,
) {
    val monthTasks = state.monthTasks
    val plannedMinutes = monthTasks.plannedLoadMinutes()
    val actualMinutes = monthTasks.sumOf { it.actualMinutes }
    val completionPercent = monthTasks.plannedLoadCompletionPercent()
    val monthLabel = state.today.month.getDisplayName(TextStyle.FULL, Locale("pt", "BR")).replaceFirstChar { it.uppercase() }
    val upcoming = monthTasks
        .filter {
            it.entity.scheduledEpochDay >= state.today.toEpochDay() &&
                it.entity.status in setOf(
                    br.com.estudario.domain.planner.PlanTaskStatus.PLANEJADA,
                    br.com.estudario.domain.planner.PlanTaskStatus.EM_ANDAMENTO,
                )
        }
        .sortedBy { it.entity.scheduledEpochDay }
    val bySubject = monthTasks
        .filter { it.entity.status.countsAsPlannedLoad() }
        .groupBy { it.entity.subjectNameSnapshot }
        .map { (name, tasks) -> name to tasks.sumOf { it.entity.plannedMinutes } }
        .sortedByDescending { it.second }

    PlanList(header) {
        item { Text("$monthLabel de ${state.today.year}", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold) }
        item { PlannerSummary(plannedMinutes = plannedMinutes, actualMinutes = actualMinutes, completionPercent = completionPercent, plannedLabel = "planejadas no mês", periodTitle = "Progresso do mês") }
        if (bySubject.isNotEmpty()) {
            item { Text("Distribuição por matéria", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold) }
            item {
                ElevatedCard(Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        bySubject.forEach { (name, minutes) ->
                            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(name, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(1f))
                                Text(minutesLabel(minutes), style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, maxLines = 1, modifier = Modifier.padding(start = 8.dp), overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis)
                            }
                        }
                    }
                }
            }
        }
        if (upcoming.isEmpty()) {
            item { EmptyState("Nada planejado à frente", "Não há tarefas futuras neste mês.", "Gerar planejamento", onGenerate) }
        } else {
            item { Text("Próximas missões do mês", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold) }
            items(upcoming, key = { "month_${it.entity.id}" }) { taskUi ->
                MissionCard(taskUi = taskUi, onStart = { onFocus(taskUi) })
            }
        }
    }
}

// ---------------------------------------------------------------- visão geral

@Composable
private fun OverviewPlanView(state: ActivePlanUiState, header: LazyListScope.() -> Unit) {
    val currentTasks = state.tasks.plannedLoadTasks()
    val totalTasks = currentTasks.size
    val completedTasks = currentTasks.count { it.entity.status == br.com.estudario.domain.planner.PlanTaskStatus.CONCLUIDA }
    val completionPercent = if (totalTasks == 0) 0 else (completedTasks * 100 / totalTasks)
    val plannedMinutes = state.totalPlannedMinutes
    val actualMinutes = state.tasks.sumOf { it.actualMinutes }

    PlanList(header) {
        item { Text("Visão geral do plano", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold) }
        item {
            Text(
                "$completedTasks de $totalTasks missões concluídas desde o início do plano.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        item { PlannerSummary(plannedMinutes = plannedMinutes, actualMinutes = actualMinutes, completionPercent = completionPercent, plannedLabel = "planejadas no plano", periodTitle = "Progresso do plano") }
        item {
            ElevatedCard(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(Icons.Outlined.EventAvailable, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        Text("Previsão de conclusão", fontWeight = FontWeight.Bold)
                    }
                    Text(
                        state.forecastDate?.let { "Com o seu ritmo atual, a demanda termina aproximadamente em ${forecastDateLabelPtBr(it)}." }
                            ?: "A previsão depende da análise do edital com a disponibilidade cadastrada.",
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
        }
        if (state.masterAlerts.isNotEmpty()) {
            item { Text("Alertas do plano mestre", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.error) }
            items(state.masterAlerts, key = { "alert_${it.subjectName}" }) { alert ->
                ElevatedCard(
                    Modifier.fillMaxWidth(),
                    colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                ) {
                    Text(
                        "${alert.subjectName} sem estudo há ${alert.inactiveDays} dias.",
                        modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colorScheme.onErrorContainer,
                    )
                }
            }
        }
    }
}

/**
 * A lista de todas as visões do plano. O [header] entra primeiro e rola junto (antes o resumo e as
 * abas ficavam presos acima da lista e, em celular com fonte normal, quase não sobrava tela para as
 * tarefas). Margem lateral proporcional à largura do aparelho.
 */
@Composable
private fun PlanList(header: LazyListScope.() -> Unit, content: LazyListScope.() -> Unit) {
    val gutter = estudarioLayout().screenGutter
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = gutter, end = gutter, top = 8.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        header()
        content()
    }
}
