package br.com.estudario.ui.planner

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import kotlinx.coroutines.launch
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import br.com.estudario.ui.brand.TextButton
import br.com.estudario.ui.brand.Surface
import br.com.estudario.ui.brand.ElevatedCard
import br.com.estudario.ui.brand.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.estudario.ui.components.EmptyState
import br.com.estudario.ui.components.MissionCard
import br.com.estudario.ui.theme.estudarioLayout
import br.com.estudario.domain.planner.PlanTaskStatus
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
    onEditAvailability: () -> Unit = {},
    onShiftRoadmapEnd: (Long) -> Unit = {},
    /** Arrastar no mês (planos pagos): o estudo vai para o dia escolhido e fica fixado lá. */
    onMoveTask: (PlannerTaskUi, LocalDate) -> Unit = { _, _ -> },
    onSwapTasks: (PlannerTaskUi, PlannerTaskUi) -> Unit = { _, _ -> },
    onDayOff: (LocalDate, Boolean) -> Unit = { _, _ -> },
) {
    val theoryByTopic = remember(state.tasks) {
        state.tasks.map { it.entity }.filter { it.type == br.com.estudario.domain.planner.PlanTaskType.THEORY && it.topicId != null && it.status != PlanTaskStatus.REPROGRAMADA }
            // Teoria concluída em qualquer tarefa vale (uma "não realizada" antiga não pode acusar "pulada" o que já foi feito).
            .groupBy { it.topicId!! }.mapValues { (_, l) -> l.filter { it.status == PlanTaskStatus.CONCLUIDA }.maxByOrNull { it.scheduledEpochDay } ?: l.maxBy { it.scheduledEpochDay } }
    }
    // Primeira teoria concluída de cada tópico: uma teoria marcada depois dela é retomada ("por que esta tarefa").
    val firstTheoryDone = remember(state.tasks) {
        state.tasks.map { it.entity }.filter { it.type == br.com.estudario.domain.planner.PlanTaskType.THEORY && it.topicId != null && it.status == PlanTaskStatus.CONCLUIDA }
            .groupBy { it.topicId!! }.mapValues { (_, l) -> l.minOf { it.scheduledEpochDay } }
    }
    androidx.compose.runtime.CompositionLocalProvider(br.com.estudario.ui.components.LocalOpenTaskTopic provides onOpenTopic, br.com.estudario.ui.components.LocalTheoryByTopic provides theoryByTopic, br.com.estudario.ui.components.LocalFirstTheoryDone provides firstTheoryDone) {
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
        PlanSection.WEEK -> WeekPlanView(state = state, onFocus = onFocus, onReprogram = onReprogram, onSkip = onSkip, onGenerate = onGenerate, header = header)
        PlanSection.MONTH -> MonthPlanView(state = state, onFocus = onFocus, onReprogram = onReprogram, onSkip = onSkip, onGenerate = onGenerate, header = header, onMoveTask = onMoveTask, onSwapTasks = onSwapTasks, onDayOff = onDayOff)
        PlanSection.YEAR -> OverviewPlanView(state = state, header = header, onEditAvailability = onEditAvailability, onShiftRoadmapEnd = onShiftRoadmapEnd)
    }
    }
}

/** No mesmo dia, a teoria de um tópico aparece antes das questões e revisões dele (o resto fica na ordem do plano). */
internal fun List<PlannerTaskUi>.theoryFirst(): List<PlannerTaskUi> {
    val theory = br.com.estudario.domain.planner.PlanTaskType.THEORY
    val out = mutableListOf<PlannerTaskUi>()
    forEach { t ->
        if (t in out) return@forEach
        val topic = t.entity.topicId
        if (t.entity.type != theory && topic != null) firstOrNull { it.entity.type == theory && it.entity.topicId == topic && it !in out }?.let { out += it }
        out += t
    }
    return out
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
    // O aviso de atrasadas do Início abre o plano com elas já abertas.
    var showOverdue by remember { mutableStateOf(PlanOverdueRequest.consume()) }
    
    // Só as tarefas vigentes do dia. Ao refazer o plano, as antigas ficam no histórico como
    // reprogramadas; listá-las junto dobrava o dia (3h viravam 6h), repetia a teoria e punha
    // questões antigas na frente da teoria nova.
    val tasksForSelectedDate = state.tasks.plannedLoadTasks().filter { it.entity.scheduledEpochDay == selectedDate.toEpochDay() }.theoryFirst()
    val plannedMinutes = tasksForSelectedDate.plannedLoadMinutes()
    val actualMinutes = tasksForSelectedDate.sumOf { it.actualMinutes }
    
    val capacity = state.availability.firstOrNull { it.dayOfWeek == selectedDate.dayOfWeek.value }?.let { if (it.unavailable) 0 else it.availableMinutes } ?: 0
    val isDayLocked = state.dayOverrides.firstOrNull { it.epochDay == selectedDate.toEpochDay() }?.locked == true

    val loadByDate = state.tasks.plannedLoadTasks().groupBy { LocalDate.ofEpochDay(it.entity.scheduledEpochDay) }
    val taskDates = loadByDate.keys
    val minutesByDate = loadByDate.mapValues { (_, tasks) -> tasks.plannedLoadMinutes() }
    val doneDates = loadByDate.filterValues { tasks -> tasks.all { it.entity.status == PlanTaskStatus.CONCLUIDA } }.keys

    // Dia do plano concluído: de vez em quando (AppReview.shouldInvite), o Folha pergunta se a pessoa está curtindo.
    val reviewContext = androidx.compose.ui.platform.LocalContext.current
    val dayDone = state.todayTasks.isNotEmpty() && state.todayTasks.none { it.entity.status == PlanTaskStatus.PLANEJADA || it.entity.status == PlanTaskStatus.EM_ANDAMENTO } &&
        state.todayTasks.any { it.entity.status == PlanTaskStatus.CONCLUIDA }
    var reviewInvite by remember { mutableStateOf(false) }
    androidx.compose.runtime.LaunchedEffect(dayDone) {
        if (dayDone && br.com.estudario.ui.AppReview.shouldInvite(reviewContext)) {
            kotlinx.coroutines.delay(1_200)
            br.com.estudario.ui.AppReview.markInvited(reviewContext)
            reviewInvite = true
        }
    }
    if (reviewInvite) br.com.estudario.ui.components.ReviewInvite { reviewInvite = false }

    PlanList(header) {
        // Calendário Interativo
        item {
            ElevatedCard(Modifier.fillMaxWidth()) {
                InteractiveCalendar(
                    selectedDate = selectedDate,
                    onDateSelected = { selectedDate = it },
                    taskDates = taskDates,
                    modifier = Modifier.padding(16.dp),
                    minutesByDate = minutesByDate,
                    doneDates = doneDates,
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

        // Summary do dia selecionado (hoje já aparece na "Missão de hoje" do topo)
        if (selectedDate != state.today) item {
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
        // Atraso é aviso, não parede vermelha: um cartão recolhido com a contagem; quem quiser abre.
        if (selectedDate == state.today && state.overdueTasks.isNotEmpty()) {
            item {
                Surface(
                    onClick = { showOverdue = !showOverdue },
                    shape = br.com.estudario.ui.theme.EstudarioShapes.panel,
                    color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.35f),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Column(Modifier.weight(1f)) {
                            Text("${state.overdueTasks.size} atividade(s) atrasada(s)", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                            Text("Faça agora ou toque em Reprogramar em cada uma: o plano redistribui sozinho.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Text(if (showOverdue) "Ocultar" else "Ver", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
                    }
                }
            }
            if (showOverdue) items(state.overdueTasks, key = { "overdue_${it.entity.id}" }) { taskUi ->
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
                } else if (selectedDate == state.today && state.nextStudyDay != null) {
                    EmptyState(
                        title = "Hoje é dia de descanso",
                        body = "Descanso também é parte do plano. Seu próximo estudo é ${dayLabelPtBr(state.nextStudyDay!!, state.today)}. Se quiser adiantar, é só começar uma das atividades abaixo.",
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
            if (selectedDate == state.today && !isDayLocked && state.nextStudyDay != null) {
                item {
                    Text(
                        "Próximo estudo · ${dayLabelPtBr(state.nextStudyDay!!, state.today).replaceFirstChar { it.uppercase() }}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                    )
                }
                items(state.nextDayTasks, key = { "next_${it.entity.id}" }) { taskUi ->
                    MissionCard(taskUi = taskUi, onStart = { onFocus(taskUi) }, onReprogram = { onReprogram(taskUi) }, onSkip = { onSkip(taskUi) }, isOverdue = false)
                }
            }
        } else {
            item {
                Text(
                    text = "Atividades do dia · ${tasksForSelectedDate.count { it.entity.status == PlanTaskStatus.CONCLUIDA }} de ${tasksForSelectedDate.size} feitas",
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
            // Hoje feito: o próximo dia aparece logo abaixo, com "Adiantar" em cada matéria.
            val todayDone = selectedDate == state.today && tasksForSelectedDate.none { it.entity.status == PlanTaskStatus.PLANEJADA || it.entity.status == PlanTaskStatus.EM_ANDAMENTO }
            if (todayDone && state.nextStudyDay != null && state.nextDayTasks.isNotEmpty()) {
                item {
                    androidx.compose.foundation.layout.Column(Modifier.padding(top = 12.dp), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text("Dia concluído! Quer adiantar?", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Text(
                            "Estas são as matérias de ${dayLabelPtBr(state.nextStudyDay!!, state.today)}. Adiantando agora, o plano segue daqui.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
                items(state.nextDayTasks.theoryFirst(), key = { "advance_${it.entity.id}" }) { taskUi ->
                    MissionCard(taskUi = taskUi, onStart = { onFocus(taskUi) }, onReprogram = { onReprogram(taskUi) }, onSkip = { onSkip(taskUi) }, isOverdue = false)
                }
            }
        }

    }
}

// ---------------------------------------------------------------- semana

@Composable
private fun WeekPlanView(
    state: ActivePlanUiState,
    onFocus: (PlannerTaskUi) -> Unit,
    onReprogram: (PlannerTaskUi) -> Unit,
    onSkip: (PlannerTaskUi) -> Unit,
    onGenerate: () -> Unit,
    header: LazyListScope.() -> Unit,
) {
    val weekStart = state.weekStart
    val weekEnd = weekStart.plusDays(6)
    val weekTasks = state.weekTasks
    val plannedMinutes = weekTasks.plannedLoadMinutes()
    val actualMinutes = weekTasks.sumOf { it.actualMinutes }
    val completionPercent = weekTasks.plannedLoadCompletionPercent()
    val byDay = remember(weekTasks) { weekTasks.groupBy { LocalDate.ofEpochDay(it.entity.scheduledEpochDay) } }
    val dayNames = listOf("Segunda-feira", "Terça-feira", "Quarta-feira", "Quinta-feira", "Sexta-feira", "Sábado", "Domingo")
    // Cada dia da semana numa página: a faixa do topo mostra os 7 dias e deslizar troca de dia.
    val pager = rememberPagerState(initialPage = weekDayIndex(weekStart, state.today) ?: 0) { 7 }
    val scope = rememberCoroutineScope()

    PlanList(header) {
        item {
            Text(
                "Semana de ${weekStart.dayOfMonth} a ${weekEnd.dayOfMonth} de ${weekEnd.month.getDisplayName(TextStyle.FULL, Locale("pt", "BR")).replaceFirstChar { it.uppercase() }}",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
            )
        }
        item { PlannerSummary(plannedMinutes = plannedMinutes, actualMinutes = actualMinutes, completionPercent = completionPercent, plannedLabel = "planejadas na semana", periodTitle = "Progresso da semana") }
        if (weekTasks.isEmpty() && state.upcomingWeekTasks.isNotEmpty()) {
            // A semana do calendário acabou, mas o plano continua: mostra os próximos 7 dias.
            item { Text("Esta semana já fechou. Veja o que vem nos próximos 7 dias:", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant) }
            state.upcomingWeekTasks.groupBy { it.entity.scheduledEpochDay }.toSortedMap().forEach { (day, tasks) ->
                val date = LocalDate.ofEpochDay(day)
                item(key = "up_head_$day") {
                    Text(
                        dayLabelPtBr(date, state.today).replaceFirstChar { it.uppercase() } + " · ${tasks.size} atividade(s) · " + br.com.estudario.ui.planner.minutesLabelPtBr(tasks.sumOf { it.entity.plannedMinutes }),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                    )
                }
                items(tasks, key = { "up_${it.entity.id}" }) { taskUi ->
                    MissionCard(taskUi = taskUi, onStart = { onFocus(taskUi) }, onReprogram = { onReprogram(taskUi) }, onSkip = { onSkip(taskUi) })
                }
            }
        } else if (weekTasks.isEmpty()) {
            item { EmptyState("Semana livre", "Não há tarefas planejadas para esta semana.", "Gerar planejamento", onGenerate) }
        } else {
            item(key = "week-strip") {
                WeekDayStrip(
                    weekStart = weekStart,
                    today = state.today,
                    selected = pager.targetPage,
                    marks = (0..6).map { dayMark(byDay[weekStart.plusDays(it.toLong())].orEmpty().map { t -> t.entity.status }) },
                    onSelect = { i -> scope.launch { pager.animateScrollToPage(i) } },
                )
            }
            item(key = "week-pager") {
                HorizontalPager(
                    state = pager,
                    modifier = Modifier.fillMaxWidth(),
                    pageSpacing = 16.dp,
                    verticalAlignment = Alignment.Top,
                ) { page ->
                    val date = weekStart.plusDays(page.toLong())
                    val tasks = byDay[date].orEmpty().theoryFirst()
                    Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        Text(
                            text = dayNames[page] + " • ${date.dayOfMonth}/${date.monthValue}" + if (date == state.today) " (hoje)" else "",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = if (date == state.today) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                        )
                        if (tasks.isEmpty()) {
                            EmptyState(
                                title = if (date < state.today) "Nenhuma atividade" else "Dia livre",
                                body = if (date < state.today) "Não houve tarefas registradas para este dia." else "Não há atividades planejadas para este dia. Deslize para ver os outros dias.",
                                actionLabel = null,
                            )
                        } else {
                            Text(
                                "${tasks.count { it.entity.status == PlanTaskStatus.CONCLUIDA }} de ${tasks.size} feitas · " + minutesLabelPtBr(tasks.plannedLoadMinutes()),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                            tasks.forEach { taskUi ->
                                key(taskUi.entity.id) {
                                    MissionCard(taskUi = taskUi, onStart = { onFocus(taskUi) }, onReprogram = { onReprogram(taskUi) }, onSkip = { onSkip(taskUi) })
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

/** Marca de cada dia na faixa da semana: nada, atividades pendentes ou tudo feito. */
internal enum class WeekDayMark { NONE, PENDING, DONE }

internal fun dayMark(statuses: List<PlanTaskStatus>): WeekDayMark {
    val load = statuses.filter { it.countsAsPlannedLoad() }
    return when {
        load.isEmpty() -> WeekDayMark.NONE
        load.all { it == PlanTaskStatus.CONCLUIDA } -> WeekDayMark.DONE
        else -> WeekDayMark.PENDING
    }
}

/** Posição (0 = segunda) de [date] na semana que começa em [weekStart], ou null se estiver fora dela. */
internal fun weekDayIndex(weekStart: LocalDate, date: LocalDate): Int? {
    val d = date.toEpochDay() - weekStart.toEpochDay()
    return if (d in 0L..6L) d.toInt() else null
}

/** Faixa S T Q Q S S D no topo da semana: hoje destacado, ponto nos dias com atividade, check nos feitos. */
@Composable
private fun WeekDayStrip(weekStart: LocalDate, today: LocalDate, selected: Int, marks: List<WeekDayMark>, onSelect: (Int) -> Unit) {
    val letters = listOf("S", "T", "Q", "Q", "S", "S", "D")
    val full = listOf("segunda", "terça", "quarta", "quinta", "sexta", "sábado", "domingo")
    val scheme = MaterialTheme.colorScheme
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        (0..6).forEach { i ->
            val date = weekStart.plusDays(i.toLong())
            val isSel = i == selected
            val isToday = date == today
            val bg by animateColorAsState(if (isSel) scheme.primary else if (isToday) scheme.primaryContainer else scheme.surfaceContainerLow, label = "dayBg")
            val fg by animateColorAsState(if (isSel) scheme.onPrimary else if (isToday) scheme.onPrimaryContainer else scheme.onSurface, label = "dayFg")
            val markText = when (marks[i]) { WeekDayMark.DONE -> ", tudo feito"; WeekDayMark.PENDING -> ", com atividades"; WeekDayMark.NONE -> "" }
            Surface(
                onClick = { onSelect(i) },
                shape = RoundedCornerShape(14.dp),
                color = bg,
                modifier = Modifier.weight(1f).heightIn(min = 64.dp)
                    .semantics(mergeDescendants = true) {
                        contentDescription = "${full[i]}, ${date.dayOfMonth}" + (if (isToday) ", hoje" else "") + markText
                        this.selected = isSel
                    },
            ) {
                Column(Modifier.padding(vertical = 8.dp, horizontal = 2.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(letters[i], style = MaterialTheme.typography.labelMedium, color = fg.copy(alpha = 0.8f), maxLines = 1)
                    Text("${date.dayOfMonth}", style = MaterialTheme.typography.titleMedium, fontWeight = if (isToday || isSel) FontWeight.Black else FontWeight.Bold, color = fg, maxLines = 1, softWrap = false)
                    Box(Modifier.height(12.dp), contentAlignment = Alignment.Center) {
                        when (marks[i]) {
                            WeekDayMark.DONE -> Icon(Icons.Outlined.CheckCircle, contentDescription = null, tint = if (isSel) fg else scheme.tertiary, modifier = Modifier.size(12.dp))
                            WeekDayMark.PENDING -> Box(Modifier.size(6.dp).clip(CircleShape).background(if (isSel) fg else scheme.primary))
                            WeekDayMark.NONE -> Unit
                        }
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
    onReprogram: (PlannerTaskUi) -> Unit,
    onSkip: (PlannerTaskUi) -> Unit,
    onGenerate: () -> Unit,
    header: LazyListScope.() -> Unit,
    onMoveTask: (PlannerTaskUi, LocalDate) -> Unit,
    onSwapTasks: (PlannerTaskUi, PlannerTaskUi) -> Unit,
    onDayOff: (LocalDate, Boolean) -> Unit,
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
        // O mês inteiro, legível e arrastável (pagos): o calendário e a lista do dia escolhido.
        item(key = "month-board") { PlanMonthBoard(state, onFocus, onMoveTask, onSwapTasks, onDayOff) }
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
                MissionCard(taskUi = taskUi, onStart = { onFocus(taskUi) }, onReprogram = { onReprogram(taskUi) }, onSkip = { onSkip(taskUi) })
            }
        }
    }
}

// ---------------------------------------------------------------- visão geral

@Composable
private fun OverviewPlanView(state: ActivePlanUiState, header: LazyListScope.() -> Unit, onEditAvailability: () -> Unit, onShiftRoadmapEnd: (Long) -> Unit) {
    val currentTasks = state.tasks.plannedLoadTasks()
    val totalTasks = currentTasks.size
    val completedTasks = currentTasks.count { it.entity.status == br.com.estudario.domain.planner.PlanTaskStatus.CONCLUIDA }
    val completionPercent = if (totalTasks == 0) 0 else (completedTasks * 100 / totalTasks)
    val plannedMinutes = state.totalPlannedMinutes
    val actualMinutes = state.tasks.sumOf { it.actualMinutes }

    PlanList(header) {
        item { Text("Visão geral do plano", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold) }
        // O caminho inteiro até a prova vem primeiro: é o que dá confiança no plano.
        // Primeiro o plano do jeito que a pessoa vive (a semana), depois o caminho inteiro até a prova.
        state.weeklyBalance?.let { balance -> item(key = "weekly-balance") { WeeklyBalanceCard(balance, state.roadmap) } }
        item(key = "week-shape") { PlanWeekShape(state) }
        state.roadmap?.let { roadmap -> editalRoadmap(roadmap, onEditAvailability, onShiftRoadmapEnd, state.activePlan?.id.orEmpty()) }
        item { Text("Andamento", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 8.dp)) }
        item {
            Text(
                "$completedTasks de $totalTasks missões concluídas desde o início do plano.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        item { PlannerSummary(plannedMinutes = plannedMinutes, actualMinutes = actualMinutes, completionPercent = completionPercent, plannedLabel = "planejadas no plano", periodTitle = "Progresso do plano") }
        if (state.roadmap == null) item {
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

/** Pedido do Início para abrir o plano já nas atrasadas (vale uma vez). */
object PlanOverdueRequest {
    @Volatile private var pending = false
    fun request() { pending = true }
    fun consume(): Boolean = pending.also { pending = false }
}
