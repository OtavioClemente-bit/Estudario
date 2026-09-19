package br.com.estudario.ui.planner

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.time.LocalDate

@Composable
fun YearPlanScreen(state: ActivePlanUiState) {
    LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item { Text(state.forecastDate?.let { "Com a disponibilidade atual, a demanda vigente termina aproximadamente em $it." } ?: "A previsão depende de capacidade líquida e demanda futura registradas.", style = MaterialTheme.typography.bodyMedium) }
        if (state.annualPhases.isEmpty()) item { Text("As fases anuais serão versionadas quando forem definidas ou importadas.") }
        items(state.annualPhases, key = { it.id }) { phase ->
            val phaseTasks = state.tasks.filter { it.entity.scheduledEpochDay in phase.startEpochDay..phase.endEpochDay }
            val actual = phaseTasks.sumOf { it.actualMinutes }
            ElevatedCard(Modifier.fillMaxWidth()) { Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(phase.name, fontWeight = FontWeight.Bold)
                Text("${LocalDate.ofEpochDay(phase.startEpochDay)} a ${LocalDate.ofEpochDay(phase.endEpochDay)}")
                Text(phase.objective)
                Text("Meta ${minutesLabel(phase.targetMinutes)} • ${phase.targetQuestions} questões • ${phase.targetDiscursives} discursivas")
                Text("Realizado ${minutesLabel(actual)} • Critério: ${phase.completionCriteria}", style = MaterialTheme.typography.bodySmall)
                Text("Versão ${phase.validFromRevision}${phase.validUntilRevision?.let { "–$it" } ?: " (vigente)"}", style = MaterialTheme.typography.labelSmall)
            } }
        }
    }
}
