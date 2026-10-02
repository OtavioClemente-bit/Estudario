package br.com.estudario.ui.planner

import br.com.estudario.ui.components.AlertDialog
import br.com.estudario.ui.theme.screenPadding
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
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
                        "Também some o registro de $execucoes atividade(s) que você concluiu por ele, e o XP dessas atividades sai da sua conta.",
                        color = MaterialTheme.colorScheme.error,
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
            TextButton(onClick = onConfirm, colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)) {
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
) {
    var confirm by remember { mutableStateOf<Pair<String, () -> Unit>?>(null) }
    confirm?.let { (message, action) -> ConfirmDialog("Confirmar alteração", message, onDismiss = { confirm = null }, onConfirm = action) }
    androidx.activity.compose.BackHandler(onBack = onBack)

    var excluir by remember { mutableStateOf<StudyPlanEntity?>(null) }
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
                if (!plan.archived && !plan.masterPlan) add(SheetAction(Icons.Outlined.Star, "Tornar Plano Mestre", "O plano de referência para o ano todo") { confirm = "O Plano Mestre anterior será desmarcado, preservando todos os dados." to { onMaster(plan.id) } })
                add(SheetAction(Icons.Outlined.ContentCopy, "Duplicar", "Uma cópia para testar outra estratégia") { onDuplicate(plan.id, "${plan.name}, cópia") })
                if (!plan.archived) add(SheetAction(Icons.Outlined.Archive, "Arquivar", "Sai de Hoje/Semana/Mês/Ano; o histórico fica") { confirm = "O plano ficará consultável, mas deixará de alimentar Hoje/Semana/Mês/Ano." to { onArchive(plan.id) } })
                else add(SheetAction(Icons.Outlined.Unarchive, "Restaurar") { onRestore(plan.id) })
                add(SheetAction(Icons.Outlined.DeleteOutline, "Excluir plano", "Apaga o cronograma e as tarefas dele", destructive = true) { excluir = plan })
            },
            onDismiss = { planMenu = null },
        )
    }
    subjectPriority?.let { id ->
        val subject = state.planSubjects.firstOrNull { it.subjectId == id }
        if (subject != null) ActionSheet(
            title = subject.subjectNameSnapshot,
            subtitle = "Prioridade no plano: ${priorityLabel(subject.priority)}",
            actions = PlanPriority.entries.map { level ->
                SheetAction(Icons.Outlined.Flag, priorityLabel(level), if (level == subject.priority) "Atual" else null) { onUpdateSubject(subject.subjectId, level, subject.paused) }
            },
            onDismiss = { subjectPriority = null },
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
                                        Text(if (subject.paused) "Pausada" else "Prioridade ${priorityLabel(subject.priority).lowercase()}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                    AssistChip(onClick = { subjectPriority = subject.subjectId }, label = { Text(priorityLabel(subject.priority)) }, leadingIcon = { Icon(Icons.Outlined.Flag, null, Modifier.size(16.dp)) }, enabled = !subject.paused)
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
                        Surface(shape = RoundedCornerShape(12.dp), color = MaterialTheme.colorScheme.secondaryContainer, modifier = Modifier.size(42.dp)) {
                            Box(contentAlignment = Alignment.Center) { Icon(if (plan.archived) Icons.Outlined.Archive else Icons.Outlined.CalendarMonth, null, tint = MaterialTheme.colorScheme.secondary) }
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
