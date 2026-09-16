package br.com.meuconcurso.ui.planner

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun MonthPlanScreen(state: ActivePlanUiState) {
    val current = state.monthlyPlans.firstOrNull { it.yearMonth == "%04d-%02d".format(state.today.year, state.today.monthValue) }
    val planned = state.monthTasks.sumOf { it.entity.plannedMinutes }; val actual = state.monthTasks.sumOf { it.actualMinutes }
    LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item { PlannerSummary(planned, actual) }
        item { ElevatedCard(Modifier.fillMaxWidth()) { Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(current?.focus ?: "Foco mensal ainda não definido", fontWeight = FontWeight.Bold)
            Text("Meta: ${minutesLabel(current?.targetMinutes ?: planned)} • ${current?.targetQuestions ?: state.monthTasks.sumOf { it.entity.plannedQuestions }} questões • ${current?.targetDiscursives ?: 0} discursivas")
            Text("Aderência: ${if (planned == 0) 0 else (actual * 100 / planned).coerceAtMost(999)}%")
        } } }
        val bySubject = state.monthTasks.groupBy { it.entity.subjectNameSnapshot }
        bySubject.forEach { (subject, rows) -> item { ListItem(headlineContent = { Text(subject) }, supportingContent = { Text("${minutesLabel(rows.sumOf { it.entity.plannedMinutes })} planejadas • ${minutesLabel(rows.sumOf { it.actualMinutes })} realizadas") }) } }
    }
}
