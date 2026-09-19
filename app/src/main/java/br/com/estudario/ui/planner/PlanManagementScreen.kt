package br.com.estudario.ui.planner

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
                        "Também some o registro de $execucoes atividade(s) que você concluiu por ele — e o XP dessas atividades sai da sua conta.",
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
            Row(verticalAlignment = Alignment.CenterVertically) {
                TextButton(onClick = onDismiss) { Text("Cancelar") }
                if (!plan.archived) TextButton(onClick = onArchive) { Text("Arquivar") }
            }
        },
    )
}

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

    var excluir by remember { mutableStateOf<StudyPlanEntity?>(null) }
    var execucoes by remember { mutableStateOf<Int?>(null) }
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
    LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item { Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) { Text("Gerenciar planos", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold); TextButton(onClick = onBack) { Text("Voltar") } } }
        if (state.activePlan != null) item { ElevatedCard(Modifier.fillMaxWidth()) { Column(Modifier.padding(16.dp)) { Text("Contexto compacto", fontWeight = FontWeight.Bold); Text("Somente planejamento, métricas e alertas — sem teorias ou enunciados.", style = MaterialTheme.typography.bodySmall); Row { TextButton(onClick = onContextJson) { Text("Exportar JSON") }; TextButton(onClick = onContextText) { Text("Exportar texto") } } } } }
        if (state.activePlan != null) item { ElevatedCard(Modifier.fillMaxWidth()) { Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) { Text("Disponibilidade e matérias", fontWeight = FontWeight.Bold); TextButton(onClick = onEditAvailability) { Text("Editar horas") } }
            state.planSubjects.forEach { subject -> Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) { Column(Modifier.weight(1f)) { Text(subject.subjectNameSnapshot); Text(subject.priority.name, style = MaterialTheme.typography.labelSmall) }; TextButton(onClick = { val values = PlanPriority.entries; onUpdateSubject(subject.subjectId, values[(subject.priority.ordinal + 1) % values.size], subject.paused) }) { Text("Prioridade") }; Switch(subject.paused, { onUpdateSubject(subject.subjectId, subject.priority, it) }) } }
        } } }
        items(state.allPlans, key = { it.id }) { plan ->
            ElevatedCard(Modifier.fillMaxWidth()) { Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(7.dp)) {
                Text(plan.name, fontWeight = FontWeight.Bold)
                Text(listOfNotNull(if (plan.active) "Ativo" else null, if (plan.masterPlan) "Plano Mestre" else null, if (plan.archived) "Arquivado" else null).ifEmpty { listOf("Inativo") }.joinToString(" • "))
                Text("Revisão ${plan.revision} • ${plan.objective}", style = MaterialTheme.typography.bodySmall)
                FlowRow(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    if (!plan.archived && !plan.active) TextButton(onClick = { confirm = "O plano ativo atual será desativado, sem apagar o histórico." to { onActivate(plan.id) } }) { Text("Ativar") }
                    if (!plan.archived && !plan.masterPlan) TextButton(onClick = { confirm = "O Plano Mestre anterior será desmarcado, preservando todos os dados." to { onMaster(plan.id) } }) { Text("Tornar Mestre") }
                    TextButton(onClick = { onDuplicate(plan.id, "${plan.name} — cópia") }) { Text("Duplicar") }
                    TextButton(onClick = { onExport(plan) }) { Text("Exportar") }
                    if (!plan.archived) TextButton(onClick = { confirm = "O plano ficará consultável, mas deixará de alimentar Hoje/Semana/Mês/Ano." to { onArchive(plan.id) } }) { Text("Arquivar") }
                    else TextButton(onClick = { onRestore(plan.id) }) { Text("Restaurar") }
                    TextButton(onClick = { excluir = plan }, colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)) { Text("Excluir") }
                }
            } }
        }
    }
}
