package br.com.estudario.ui.planner

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.estudario.ui.brand.Button
import br.com.estudario.ui.brand.OutlinedButton
import java.time.LocalDate

private const val PREFS = "plano_atrasadas"

/**
 * Estudo atrasado: o plano não varre mais calado o que ficou para trás. Ele mostra quanto foi e a
 * pessoa escolhe: encaixar nos próximos dias (volta na frente e o resto se ajusta), empurrar o
 * plano inteiro (nada se perde, tudo anda junto) ou deixar para depois (some até amanhã; passada
 * uma semana o plano encaixa sozinho).
 */
@Composable
fun OverdueRescueCard(state: ActivePlanUiState, onFit: () -> Unit, onPush: () -> Unit, modifier: Modifier = Modifier) {
    val late = state.overdueTasks
    if (late.isEmpty()) return
    val context = LocalContext.current
    val prefs = remember { context.getSharedPreferences(PREFS, android.content.Context.MODE_PRIVATE) }
    val today = state.today.toEpochDay()
    var hidden by remember(today) { mutableStateOf(prefs.getLong("adiado", -1) == today) }
    if (hidden) return
    val oldest = late.minOf { it.entity.scheduledEpochDay }
    val days = (today - oldest).toInt()
    val minutes = late.sumOf { (it.entity.plannedMinutes - it.actualMinutes).coerceAtLeast(0) }
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(2.dp, Color(0xFFFF8FA3)),
        modifier = modifier.fillMaxWidth().padding(vertical = 4.dp),
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(
                if (late.size == 1) "1 estudo atrasado" else "${late.size} estudos atrasados",
                style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black,
            )
            Text(
                "${formatMinutes(minutes)} que ficaram de " + (if (days == 1) "ontem" else "$days dias atrás") +
                    ". O que fazemos com eles?",
                style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Button(onClick = onFit, modifier = Modifier.fillMaxWidth()) { Text("Encaixar nos próximos dias") }
            Text(
                "Voltam na frente e o resto do plano se ajusta ao seu ritmo, sem passar das suas horas.",
                style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(onClick = onPush, modifier = Modifier.weight(1f)) {
                    Text(if (days == 1) "Empurrar tudo 1 dia" else "Empurrar tudo $days dias")
                }
                OutlinedButton(onClick = { prefs.edit().putLong("adiado", today).apply(); hidden = true }, modifier = Modifier.weight(1f)) {
                    Text("Deixar para depois")
                }
            }
        }
    }
}

/**
 * "Hoje não vou conseguir estudar": o dia sai do plano e o que estava nele vai para os próximos
 * dias. Pede confirmação; com o dia já marcado, mostra o aviso e o desfazer.
 */
@Composable
fun DayOffAction(state: ActivePlanUiState, onDayOff: (LocalDate, Boolean) -> Unit, modifier: Modifier = Modifier) {
    var asking by remember { mutableStateOf(false) }
    if (state.todayOff) {
        Surface(shape = RoundedCornerShape(16.dp), color = MaterialTheme.colorScheme.surfaceContainerHigh, modifier = modifier.fillMaxWidth()) {
            Row(Modifier.padding(start = 16.dp, end = 6.dp, top = 6.dp, bottom = 6.dp), verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                Text("Hoje ficou sem estudo. O plano já passou tudo para a frente.", style = MaterialTheme.typography.bodySmall, modifier = Modifier.weight(1f))
                TextButton(onClick = { onDayOff(state.today, false) }) { Text("Desfazer") }
            }
        }
        return
    }
    val pending = state.todayTasks.filter { it.entity.status in setOf(br.com.estudario.domain.planner.PlanTaskStatus.PLANEJADA, br.com.estudario.domain.planner.PlanTaskStatus.EM_ANDAMENTO) }
    if (pending.isEmpty()) return
    TextButton(onClick = { asking = true }, modifier = modifier) { Text("Hoje não vou conseguir estudar") }
    if (asking) AlertDialog(
        onDismissRequest = { asking = false },
        title = { Text("Tudo bem, acontece") },
        text = {
            Text(
                "Os ${pending.size} estudos de hoje (${formatMinutes(pending.sumOf { it.entity.plannedMinutes })}) vão para os próximos dias, " +
                    "sem passar das horas que você tem. Se der para estudar depois, é só desfazer.",
            )
        },
        confirmButton = { TextButton(onClick = { asking = false; onDayOff(state.today, true) }) { Text("Passar para a frente") } },
        dismissButton = { TextButton(onClick = { asking = false }) { Text("Vou estudar") } },
    )
}

private fun formatMinutes(minutes: Int): String = when {
    minutes < 60 -> "$minutes min"
    minutes % 60 == 0 -> "${minutes / 60}h"
    else -> "${minutes / 60}h${(minutes % 60).toString().padStart(2, '0')}"
}
