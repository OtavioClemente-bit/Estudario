package br.com.meuconcurso.ui.planner

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.com.meuconcurso.ui.components.EmptyState

@Composable
fun TodayPlanScreen(state: ActivePlanUiState, onOpenTopic: (Long) -> Unit, onStart: (String) -> Unit, onComplete: (PlannerTaskUi) -> Unit, onReprogram: (PlannerTaskUi) -> Unit, onSkip: (PlannerTaskUi) -> Unit, onToggleLock: (String, Boolean) -> Unit, onGenerate: () -> Unit) {
    LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item { PlannerSummary(state.todayPlannedMinutes, state.todayActualMinutes, state.deficitMinutes) }
        items(state.masterAlerts, key = { it.subjectId }) { alert -> AssistChip(onClick = {}, label = { Text("${alert.subjectName} não recebe estudo há ${alert.inactiveDays} dias e é crítica no Plano Mestre.") }) }
        if (state.todayTasks.isEmpty()) item { EmptyState("Dia livre", "Não há atividades planejadas para hoje.", "Gerar planejamento", onGenerate) }
        items(state.todayTasks, key = { it.entity.id }) { PlannerTaskCard(it, onOpenTopic, onStart, onComplete, onReprogram, onSkip, onToggleLock) }
    }
}
