package br.com.estudario.ui.planner

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import br.com.estudario.ui.brand.ElevatedCard
import br.com.estudario.ui.theme.estudarioColors
import java.time.LocalDate

/**
 * "Como fica sua semana": o plano resumido do jeito que a pessoa pensa nele. Quanto estuda em cada
 * dia (barras) e para onde vai o tempo (matérias). Baseado nas próximas duas semanas do plano, que
 * é o que ela vai viver de fato.
 */
@Composable
internal fun PlanWeekShape(state: ActivePlanUiState, modifier: Modifier = Modifier) {
    val start = state.today.toEpochDay()
    val window = state.tasks.plannedLoadTasks().filter { it.entity.scheduledEpochDay in start..(start + 13) }
    if (window.isEmpty()) return
    // Média por dia da semana nas próximas duas semanas (segunda = 0).
    val perWeekday = IntArray(7)
    window.forEach { perWeekday[LocalDate.ofEpochDay(it.entity.scheduledEpochDay).dayOfWeek.value - 1] += it.entity.plannedMinutes }
    val avg = perWeekday.map { it / 2 }
    val weekTotal = avg.sum()
    val subjects = window.groupBy { it.entity.subjectNameSnapshot.ifBlank { "Geral" } }
        .mapValues { (_, tasks) -> tasks.sumOf { it.entity.plannedMinutes } }
        .entries.sortedByDescending { it.value }
    val top = subjects.take(5)
    val rest = subjects.drop(5).sumOf { it.value }
    val total = subjects.sumOf { it.value }.coerceAtLeast(1)
    val palette = estudarioColors().subjectPalette
    val bar = MaterialTheme.colorScheme.primary
    val track = MaterialTheme.colorScheme.surfaceVariant
    val labels = listOf("S", "T", "Q", "Q", "S", "S", "D")

    ElevatedCard(modifier.fillMaxWidth(), colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow)) {
        Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Text("Como fica sua semana", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black)
            Text(
                "Cerca de ${minutesLabelPtBr(weekTotal)} por semana, divididos assim:",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            // Barras por dia: altura proporcional ao maior dia; folga fica só com a base.
            val maxDay = (avg.maxOrNull() ?: 0).coerceAtLeast(1)
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                avg.forEachIndexed { i, minutes ->
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                        Text(
                            if (minutes == 0) "folga" else minutesLabelPtBr(minutes),
                            style = MaterialTheme.typography.labelSmall,
                            color = if (minutes == 0) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface,
                            maxLines = 1, overflow = TextOverflow.Clip, textAlign = TextAlign.Center,
                        )
                        Spacer(Modifier.height(4.dp))
                        Canvas(Modifier.width(26.dp).height(96.dp)) {
                            val r = CornerRadius(8.dp.toPx())
                            drawRoundRect(track, cornerRadius = r)
                            if (minutes > 0) {
                                val h = size.height * (minutes.toFloat() / maxDay).coerceIn(0.08f, 1f)
                                val top = size.height - h
                                drawRoundRect(lerp(bar, Color.Black, 0.25f), Offset(0f, top + 3.dp.toPx()), Size(size.width, h - 3.dp.toPx()), r)
                                drawRoundRect(bar, Offset(0f, top), Size(size.width, h - 3.dp.toPx()), r)
                            }
                        }
                        Spacer(Modifier.height(4.dp))
                        Text(labels[i], style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                    }
                }
            }
            Text("Para onde vai o seu tempo", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
            // Uma faixa só, dividida pelas matérias, e a legenda com a porcentagem de cada uma.
            Canvas(Modifier.fillMaxWidth().height(16.dp)) {
                var x = 0f
                val r = CornerRadius(size.height / 2)
                drawRoundRect(track, cornerRadius = r)
                (top.map { it.value } + listOfNotNull(rest.takeIf { it > 0 })).forEachIndexed { i, v ->
                    val w = size.width * v / total
                    drawRect(if (i < top.size) palette[i % palette.size] else track, Offset(x, 0f), Size(w, size.height))
                    x += w
                }
            }
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                top.forEachIndexed { i, (name, minutes) ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(Modifier.size(12.dp).clip(CircleShape).background(palette[i % palette.size]))
                        Spacer(Modifier.width(10.dp))
                        Text(name, Modifier.weight(1f), style = MaterialTheme.typography.bodyMedium, maxLines = 1, overflow = TextOverflow.Ellipsis)
                        Text("${minutes * 100 / total}%", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                    }
                }
                if (rest > 0) Text("Outras matérias: ${rest * 100 / total}%", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}
