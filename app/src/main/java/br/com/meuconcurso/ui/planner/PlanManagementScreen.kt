package br.com.meuconcurso.ui.planner

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.meuconcurso.data.local.planner.StudyPlanEntity
import br.com.meuconcurso.ui.components.ConfirmDialog
import br.com.meuconcurso.domain.planner.PlanPriority

@Composable
fun PlanManagementScreen(state: ActivePlanUiState, onBack: () -> Unit, onActivate: (String) -> Unit, onMaster: (String) -> Unit, onDuplicate: (String, String) -> Unit, onArchive: (String) -> Unit, onRestore: (String) -> Unit, onExport: (StudyPlanEntity) -> Unit, onContextJson: () -> Unit, onContextText: () -> Unit, onEditAvailability: () -> Unit, onUpdateSubject: (Long, PlanPriority, Boolean) -> Unit) {
    var confirm by remember { mutableStateOf<Pair<String, () -> Unit>?>(null) }
    confirm?.let { (message, action) -> ConfirmDialog("Confirmar alteração", message, onDismiss = { confirm = null }, onConfirm = action) }
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
                }
            } }
        }
    }
}
