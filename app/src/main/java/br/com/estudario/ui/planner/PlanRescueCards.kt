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
import br.com.estudario.ui.brand.TextButton
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
 * uma semana o plano encaixa sozinho). Com [onStart], também lista cada atrasada com "Fazer agora",
 * para quem quer recuperar na hora em vez de reorganizar.
 */
@Composable
fun OverdueRescueCard(state: ActivePlanUiState, onFit: () -> Unit, onPush: () -> Unit, modifier: Modifier = Modifier, onStart: ((PlannerTaskUi) -> Unit)? = null) {
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
            if (onStart != null) {
                var all by remember { mutableStateOf(false) }
                val ordered = late.sortedWith(compareBy({ it.entity.scheduledEpochDay }, { it.entity.type.ordinal }))
                Surface(shape = RoundedCornerShape(14.dp), color = MaterialTheme.colorScheme.surfaceContainerHigh) {
                    Column(Modifier.padding(horizontal = 12.dp, vertical = 6.dp)) {
                        (if (all) ordered else ordered.take(3)).forEachIndexed { i, task ->
                            if (i > 0) androidx.compose.material3.HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                            OverdueRow(task, today, onStart)
                        }
                        if (ordered.size > 3) TextButton(onClick = { all = !all }, modifier = Modifier.fillMaxWidth()) {
                            Text(if (all) "Mostrar menos" else "Ver todas as ${ordered.size}")
                        }
                    }
                }
                Text("Ou reorganize o plano:", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
            }
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

/** Uma atrasada: o que é, de quando, quanto tempo, e o botão para fazer agora. */
@Composable
private fun OverdueRow(task: PlannerTaskUi, today: Long, onStart: (PlannerTaskUi) -> Unit) {
    val e = task.entity
    val ago = (today - e.scheduledEpochDay).toInt()
    val left = (e.plannedMinutes - task.actualMinutes).coerceAtLeast(0)
    val what = when (e.type) {
        br.com.estudario.domain.planner.PlanTaskType.QUESTIONS -> if (e.plannedQuestions > 0) "${(e.plannedQuestions - task.questionsDone).coerceAtLeast(1)} questões" else "Questões"
        else -> e.type.label()
    }
    Row(Modifier.fillMaxWidth().padding(vertical = 6.dp), verticalAlignment = androidx.compose.ui.Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        Column(Modifier.weight(1f)) {
            Text(e.topicNameSnapshot ?: e.subjectNameSnapshot, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold, maxLines = 2, overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis)
            Text(
                "$what · ${formatMinutes(left)} · " + (if (ago == 1) "de ontem" else "de $ago dias atrás") + (if (e.status == br.com.estudario.domain.planner.PlanTaskStatus.EM_ANDAMENTO) " · começada" else ""),
                style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1, overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
            )
        }
        br.com.estudario.ui.brand.FilledTonalButton(onClick = { onStart(task) }) {
            Text(if (e.status == br.com.estudario.domain.planner.PlanTaskStatus.EM_ANDAMENTO) "Continuar" else "Fazer agora")
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
