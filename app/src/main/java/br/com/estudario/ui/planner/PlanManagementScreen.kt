package br.com.estudario.ui.planner

import br.com.estudario.ui.components.AlertDialog
import br.com.estudario.ui.theme.screenPadding
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import br.com.estudario.ui.brand.IconButton
import br.com.estudario.ui.brand.TextButton
import br.com.estudario.ui.brand.Surface
import br.com.estudario.ui.brand.FilledTonalButton
import br.com.estudario.ui.brand.OutlinedButton
import br.com.estudario.ui.brand.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.estudario.data.local.planner.StudyPlanEntity
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import br.com.estudario.ui.components.ConfirmDialog
import br.com.estudario.domain.planner.PlanPriority
import br.com.estudario.ui.components.ActionSheet
import br.com.estudario.ui.components.SheetAction
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.icons.automirrored.outlined.ArrowBack

/**
 * Excluir plano é a única ação daqui que não tem volta, então a conversa é direta: o que some, o
 * que fica, e o caminho reversível (arquivar) oferecido no mesmo lugar.
 */
@Composable
private fun ExcluirPlanoDialog(
    plan: StudyPlanEntity,
    execucoes: Int?,
    onDismiss: () -> Unit,
    onArchive: () -> Unit,
    onConfirm: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Excluir \"${plan.name}\"?") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("Some para sempre: o cronograma, as tarefas e as fases deste plano.")
                if (execucoes != null && execucoes > 0) {
                    Text(
                        "Também some o registro de $execucoes atividade(s) que você concluiu por ele. O XP que você ganhou com elas continua na sua conta.",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                Text("Continua intacto: seu edital, tópicos estudados, questões respondidas, revisões e caderno de erros.")
                if (!plan.archived) {
                    HorizontalDivider()
                    Text(
                        "Se você só quer tirar o plano do caminho, arquive: ele sai de Hoje/Semana/Mês/Ano e o histórico fica guardado.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        },
        confirmButton = {
            TextButton(kind = br.com.estudario.ui.brand.NoteKind.Danger, onClick = onConfirm, colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)) {
                Text("Excluir definitivamente")
            }
        },
        dismissButton = {
            FlowRow(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                TextButton(onClick = onDismiss) { Text("Cancelar") }
                if (!plan.archived) TextButton(onClick = onArchive) { Text("Arquivar") }
            }
        },
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PlanManagementScreen(
    state: ActivePlanUiState,
    onBack: () -> Unit,
    onActivate: (String) -> Unit,
    onMaster: (String) -> Unit,
    onDuplicate: (String, String) -> Unit,
    onArchive: (String) -> Unit,
    onRestore: (String) -> Unit,
    onDelete: (String) -> Unit,
    executionCount: suspend (String) -> Int,
    onExport: (StudyPlanEntity) -> Unit,
    onContextJson: () -> Unit,
    onContextText: () -> Unit,
    onEditAvailability: () -> Unit,
    onUpdateSubject: (Long, PlanPriority, Boolean) -> Unit,
    onDaysOff: (java.time.LocalDate, java.time.LocalDate, Boolean) -> Unit = { _, _, _ -> },
    onUpdateMethod: (java.time.LocalDate?, br.com.estudario.domain.planner.StudyProfile) -> Unit = { _, _ -> },
    competitionChoices: suspend () -> List<Triple<Long, String, Boolean>> = { emptyList() },
    onSetCompetitions: (Set<Long>) -> Unit = {},
) {
    var competitionsDialog by remember { mutableStateOf(false) }
    if (competitionsDialog) CompetitionsDialog(competitionChoices, onDismiss = { competitionsDialog = false }, onSave = { competitionsDialog = false; onSetCompetitions(it) })
    var methodDialog by remember { mutableStateOf(false) }
    if (methodDialog) state.activePlan?.let { MethodDialog(it, onDismiss = { methodDialog = false }, onSave = { d, p -> methodDialog = false; onUpdateMethod(d, p) }) }
    var confirm by remember { mutableStateOf<Pair<String, () -> Unit>?>(null) }
    var daysOffDialog by remember { mutableStateOf(false) }
    if (daysOffDialog) DaysOffDialog(state, onDismiss = { daysOffDialog = false }, onDaysOff = onDaysOff)
    confirm?.let { (message, action) -> ConfirmDialog("Confirmar alteração", message, onDismiss = { confirm = null }, onConfirm = action) }
    androidx.activity.compose.BackHandler(onBack = onBack)

    var excluir by remember { mutableStateOf<StudyPlanEntity?>(null) }
    val entitlements = br.com.estudario.ui.plans.rememberEntitlements()
    var planUpgrade by remember { mutableStateOf(false) }
    if (planUpgrade) br.com.estudario.ui.plans.UpgradeDialog(br.com.estudario.ui.plans.UpgradeReason.PLANS, onDismiss = { planUpgrade = false })
    var execucoes by remember { mutableStateOf<Int?>(null) }
    var planMenu by remember { mutableStateOf<StudyPlanEntity?>(null) }
    var subjectPriority by remember { mutableStateOf<Long?>(null) }
    LaunchedEffect(excluir?.id) {
        execucoes = excluir?.let { plano -> runCatching { executionCount(plano.id) }.getOrNull() }
    }
    excluir?.let { plano ->
        ExcluirPlanoDialog(
            plan = plano,
            execucoes = execucoes,
            onDismiss = { excluir = null },
            onArchive = { excluir = null; onArchive(plano.id) },
            onConfirm = { excluir = null; onDelete(plano.id) },
        )
    }
    planMenu?.let { plan ->
        ActionSheet(
            title = plan.name,
            subtitle = planStatus(plan),
            actions = buildList {
                if (!plan.archived && !plan.active) add(SheetAction(Icons.Outlined.PlayCircle, "Ativar este plano", "O ativo atual é desativado, sem apagar o histórico") { confirm = "O plano ativo atual será desativado, sem apagar o histórico." to { onActivate(plan.id) } })
                if (plan.active) add(SheetAction(Icons.Outlined.Layers, "Concursos do plano", "Estudar para mais de um concurso no mesmo plano") { competitionsDialog = true })
                if (!plan.archived && !plan.masterPlan) add(SheetAction(Icons.Outlined.Star, "Tornar Plano Mestre", "O plano de referência para o ano todo") { confirm = "O Plano Mestre anterior será desmarcado, preservando todos os dados." to { onMaster(plan.id) } })
                add(SheetAction(Icons.Outlined.ContentCopy, "Duplicar", "Uma cópia para testar outra estratégia") { if (entitlements?.canCreatePlan(state.allPlans.map { it.createdAt }) == false) planUpgrade = true else onDuplicate(plan.id, "${plan.name}, cópia") })
                if (!plan.archived) add(SheetAction(Icons.Outlined.Archive, "Arquivar", "Sai de Hoje/Semana/Mês/Ano; o histórico fica") { confirm = "O plano ficará consultável, mas deixará de alimentar Hoje/Semana/Mês/Ano." to { onArchive(plan.id) } })
                else add(SheetAction(Icons.Outlined.Unarchive, "Restaurar") { onRestore(plan.id) })
                add(SheetAction(Icons.Outlined.DeleteOutline, "Excluir plano", "Apaga o cronograma e as tarefas dele", destructive = true) { if (entitlements?.canDeletePlan == false) planUpgrade = true else excluir = plan })
            },
            onDismiss = { planMenu = null },
        )
    }
    subjectPriority?.let { id ->
        val subject = state.planSubjects.firstOrNull { it.subjectId == id }
        if (subject != null) PriorityPickerDialog(
            subjectName = subject.subjectNameSnapshot,
            current = subject.priority,
            onDismiss = { subjectPriority = null },
            onPick = { level -> if (level != subject.priority) onUpdateSubject(subject.subjectId, level, subject.paused) },
        )
    }

    LazyColumn(Modifier.fillMaxSize(), contentPadding = screenPadding(), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Outlined.ArrowBack, "Voltar") }
                Column(Modifier.weight(1f)) {
                    Text("Meus planos", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                    Text("Ajuste o plano ativo e organize os outros", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
        state.activePlan?.let { active ->
            item {
                Surface(shape = RoundedCornerShape(24.dp), color = MaterialTheme.colorScheme.primaryContainer, modifier = Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text("PLANO ATIVO", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.primary)
                        Text(active.name, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimaryContainer)
                        Text(active.objective, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onPrimaryContainer, maxLines = 3)
                        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            FilledTonalButton(onClick = onEditAvailability, shape = RoundedCornerShape(12.dp)) { Icon(Icons.Outlined.Schedule, null, Modifier.size(18.dp)); Spacer(Modifier.width(6.dp)); Text("Horas por dia") }
                            FilledTonalButton(onClick = { daysOffDialog = true }, shape = RoundedCornerShape(12.dp)) { Icon(Icons.Outlined.CalendarMonth, null, Modifier.size(18.dp)); Spacer(Modifier.width(6.dp)); Text("Dias sem estudo") }
                            FilledTonalButton(onClick = { methodDialog = true }, shape = RoundedCornerShape(12.dp)) { Icon(Icons.Outlined.Event, null, Modifier.size(18.dp)); Spacer(Modifier.width(6.dp)); Text("Prova e momento") }
                            OutlinedButton(onClick = { planMenu = active }, shape = RoundedCornerShape(12.dp)) { Icon(Icons.Outlined.MoreHoriz, null, Modifier.size(18.dp)); Spacer(Modifier.width(6.dp)); Text("Mais") }
                        }
                    }
                }
            }
            if (state.planSubjects.isNotEmpty()) {
                item { SectionLabel("Matérias no plano", "Toque para mudar a prioridade. Pausar tira a matéria do cronograma até você voltar.") }
                item {
                    Surface(shape = RoundedCornerShape(20.dp), color = MaterialTheme.colorScheme.surfaceContainerLow, modifier = Modifier.fillMaxWidth()) {
                        Column(Modifier.padding(vertical = 6.dp)) {
                            state.planSubjects.forEachIndexed { index, subject ->
                                if (index > 0) HorizontalDivider(Modifier.padding(horizontal = 16.dp), color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = .5f))
                                Row(Modifier.fillMaxWidth().padding(start = 16.dp, end = 12.dp, top = 8.dp, bottom = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                                    Column(Modifier.weight(1f)) {
                                        Text(subject.subjectNameSnapshot, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium, color = if (subject.paused) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface)
                                        Text(if (subject.paused) "Pausada, fora do cronograma" else "No cronograma", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                    PriorityPill(subject.priority, enabled = !subject.paused) { subjectPriority = subject.subjectId }
                                    Spacer(Modifier.width(8.dp))
                                    Switch(!subject.paused, { onUpdateSubject(subject.subjectId, subject.priority, !it) })
                                }
                            }
                        }
                    }
                }
            }
        }
        val others = state.allPlans.filterNot { it.id == state.activePlan?.id }
        if (others.isNotEmpty()) {
            item { SectionLabel("Outros planos", null) }
            items(others, key = { it.id }) { plan ->
                Surface(
                    onClick = { planMenu = plan },
                    shape = RoundedCornerShape(18.dp),
                    color = MaterialTheme.colorScheme.surfaceContainerLow,
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = .6f)),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Surface(shape = RoundedCornerShape(12.dp), color = Color.Transparent, modifier = Modifier.size(42.dp)) {
                            Box(contentAlignment = Alignment.Center) { Icon(if (plan.archived) Icons.Outlined.Archive else Icons.Outlined.CalendarMonth, null, Modifier.size(38.dp), tint = MaterialTheme.colorScheme.secondary) }
                        }
                        Spacer(Modifier.width(14.dp))
                        Column(Modifier.weight(1f)) {
                            Text(plan.name, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold, maxLines = 2)
                            Text(planStatus(plan), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Icon(Icons.Outlined.MoreVert, "Ações do plano", tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }
    }
}

@Composable
private fun SectionLabel(title: String, subtitle: String?) {
    Column(Modifier.padding(top = 6.dp)) {
        Text(title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
        subtitle?.let { Text(it, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant) }
    }
}

private fun planStatus(plan: StudyPlanEntity): String =
    listOfNotNull(if (plan.active) "Ativo" else null, if (plan.masterPlan) "Plano Mestre" else null, if (plan.archived) "Arquivado" else null)
        .ifEmpty { listOf("Inativo") }.joinToString(" · ") + " · revisão ${plan.revision}"

private fun priorityLabel(priority: PlanPriority): String = when (priority.name) {
    "CRITICAL" -> "Crítica"
    "HIGH" -> "Alta"
    "MEDIUM" -> "Média"
    "LOW" -> "Baixa"
    else -> priority.name.lowercase().replaceFirstChar(Char::uppercase)
}

/**
 * Férias, viagem, plantão, semana de provas da faculdade: a pessoa marca um período e o plano empurra o
 * conteúdo para depois, sem ela precisar tirar dia por dia. Os períodos já marcados aparecem para devolver.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DaysOffDialog(state: ActivePlanUiState, onDismiss: () -> Unit, onDaysOff: (java.time.LocalDate, java.time.LocalDate, Boolean) -> Unit) {
    val zone = java.time.ZoneOffset.UTC
    val today = state.today
    val range = rememberDateRangePickerState(
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long) = java.time.Instant.ofEpochMilli(utcTimeMillis).atZone(zone).toLocalDate() >= today
        },
    )
    // Períodos futuros já sem estudo (dias seguidos viram um intervalo só).
    val offDays = state.dayOverrides.filter { it.unavailable && it.epochDay >= today.toEpochDay() }.map { it.epochDay }.sorted()
    val periods = buildList {
        var start: Long? = null; var prev = 0L
        for (d in offDays) {
            if (start == null) start = d else if (d != prev + 1) { add(start to prev); start = d }
            prev = d
        }
        if (start != null) add(start to prev)
    }
    fun fmt(day: Long) = java.time.LocalDate.ofEpochDay(day).let { "%02d/%02d".format(it.dayOfMonth, it.monthValue) }
    val from = range.selectedStartDateMillis?.let { java.time.Instant.ofEpochMilli(it).atZone(zone).toLocalDate() }
    val to = range.selectedEndDateMillis?.let { java.time.Instant.ofEpochMilli(it).atZone(zone).toLocalDate() } ?: from
    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = { if (from != null && to != null) { onDaysOff(from, to, true); onDismiss() } }, enabled = from != null) {
                Text(if (from == null) "Escolha os dias" else "Sem estudo de ${fmt(from.toEpochDay())} a ${fmt(to!!.toEpochDay())}")
            }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Fechar") } },
    ) {
        Column {
            Text(
                "Férias, viagem ou plantão? Marque o período: nada fica marcado nesses dias e o plano empurra o conteúdo para depois.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(start = 24.dp, end = 24.dp, top = 16.dp),
            )
            if (periods.isNotEmpty()) Column(Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                Text("Já sem estudo", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 8.dp))
                periods.take(4).forEach { (a, b) ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(if (a == b) fmt(a) else "${fmt(a)} a ${fmt(b)}", Modifier.weight(1f).padding(start = 8.dp), style = MaterialTheme.typography.bodyMedium)
                        TextButton(onClick = { onDaysOff(java.time.LocalDate.ofEpochDay(a), java.time.LocalDate.ofEpochDay(b), false); onDismiss() }) { Text("Devolver") }
                    }
                }
            }
            DateRangePicker(range, Modifier.weight(1f, fill = false).heightIn(max = 460.dp), title = null, headline = null, showModeToggle = false)
        }
    }
}

/** Data da prova e momento da pessoa: mudou o edital ou a fase, o plano recalcula sem perder o feito. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MethodDialog(plan: StudyPlanEntity, onDismiss: () -> Unit, onSave: (java.time.LocalDate?, br.com.estudario.domain.planner.StudyProfile) -> Unit) {
    var exam by remember { mutableStateOf(plan.examEpochDay?.let { java.time.LocalDate.ofEpochDay(it) }) }
    var profile by remember { mutableStateOf(plan.methodConfig().profile) }
    var picking by remember { mutableStateOf(false) }
    if (picking) {
        val picker = rememberDatePickerState(
            initialSelectedDateMillis = exam?.atStartOfDay(java.time.ZoneOffset.UTC)?.toInstant()?.toEpochMilli(),
            selectableDates = object : SelectableDates {
                override fun isSelectableDate(utcTimeMillis: Long) = java.time.Instant.ofEpochMilli(utcTimeMillis).atZone(java.time.ZoneOffset.UTC).toLocalDate() >= java.time.LocalDate.now()
            },
        )
        DatePickerDialog(
            onDismissRequest = { picking = false },
            confirmButton = { TextButton(onClick = { picker.selectedDateMillis?.let { exam = java.time.Instant.ofEpochMilli(it).atZone(java.time.ZoneOffset.UTC).toLocalDate() }; picking = false }) { Text("Usar esta data") } },
            dismissButton = { TextButton(onClick = { picking = false }) { Text("Cancelar") } },
        ) { DatePicker(picker) }
    }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Prova e momento") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("Data da prova", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    OutlinedButton(onClick = { picking = true }, shape = RoundedCornerShape(12.dp)) {
                        Text(exam?.let { "%02d/%02d/%d".format(it.dayOfMonth, it.monthValue, it.year) } ?: "Ainda não sei")
                    }
                    if (exam != null) TextButton(onClick = { exam = null }) { Text("Ainda não sei") }
                }
                Text("Em que momento você está", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                br.com.estudario.domain.planner.StudyProfile.entries.forEach { option ->
                    Surface(
                        onClick = { profile = option },
                        shape = RoundedCornerShape(14.dp),
                        color = if (profile == option) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceContainerLow,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Column(Modifier.padding(12.dp)) {
                            Text(option.label, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold)
                            Text(option.summary, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }
        },
        confirmButton = { TextButton(onClick = { onSave(exam, profile) }) { Text("Salvar e recalcular") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } },
    )
}

/** Um plano para vários concursos: marque os que entram; as matérias deles passam a dividir o tempo. */
@Composable
private fun CompetitionsDialog(load: suspend () -> List<Triple<Long, String, Boolean>>, onDismiss: () -> Unit, onSave: (Set<Long>) -> Unit) {
    var options by remember { mutableStateOf<List<Triple<Long, String, Boolean>>?>(null) }
    var chosen by remember { mutableStateOf(setOf<Long>()) }
    LaunchedEffect(Unit) { load().also { list -> options = list; chosen = list.filter { it.third }.mapTo(hashSetOf()) { it.first } } }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Concursos do plano") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("Marque os concursos que entram neste plano. As matérias deles dividem o seu tempo; o que você já fez fica no histórico.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                val list = options
                if (list == null) Text("Carregando…")
                else if (list.size < 2) Text("Você tem só um concurso. Adicione outro em Meus concursos para estudar os dois juntos.")
                else list.forEach { (id, name, _) ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(checked = id in chosen, onCheckedChange = { on -> chosen = if (on) chosen + id else chosen - id })
                        Text(name, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        },
        confirmButton = { TextButton(onClick = { onSave(chosen) }, enabled = (options?.size ?: 0) >= 2 && chosen.isNotEmpty()) { Text("Salvar e redividir") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } },
    )
}
