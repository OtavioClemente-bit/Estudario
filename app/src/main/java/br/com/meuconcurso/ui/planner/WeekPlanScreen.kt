package br.com.meuconcurso.ui.planner

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun WeekPlanScreen(state: ActivePlanUiState, onToggleDayLock: (LocalDate, Boolean) -> Unit) {
    LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        items(7) { offset ->
            val date = state.weekStart.plusDays(offset.toLong())
            val rows = state.weekTasks.filter { it.entity.scheduledEpochDay == date.toEpochDay() }
            val capacity = state.availability.firstOrNull { it.dayOfWeek == date.dayOfWeek.value }?.let { if (it.unavailable) 0 else it.availableMinutes } ?: 0
            val planned = rows.sumOf { it.entity.plannedMinutes }; val actual = rows.sumOf { it.actualMinutes }
            val locked = state.dayOverrides.firstOrNull { it.epochDay == date.toEpochDay() }?.locked == true
            ElevatedCard(Modifier.fillMaxWidth()) { Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) { Text(date.format(DateTimeFormatter.ofPattern("EEEE, dd/MM")), fontWeight = FontWeight.Bold); TextButton(onClick = { onToggleDayLock(date, !locked) }) { Text(if (locked) "Desbloquear dia" else "Bloquear dia") } }
                Text("Capacidade ${minutesLabel(capacity)} • planejado ${minutesLabel(planned)} • realizado ${minutesLabel(actual)}")
                if (planned > capacity) Text("Excesso: ${minutesLabel(planned - capacity)}", color = MaterialTheme.colorScheme.error)
                rows.forEach { Text("• ${it.entity.subjectNameSnapshot} — ${minutesLabel(it.entity.plannedMinutes)} — ${it.entity.status.label()}", style = MaterialTheme.typography.bodySmall) }
            } }
        }
    }
}
