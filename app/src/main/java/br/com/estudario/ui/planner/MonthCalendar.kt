package br.com.estudario.ui.planner

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.estudario.domain.planner.PlanTaskStatus
import br.com.estudario.ui.brand.ElevatedCard
import br.com.estudario.ui.components.subjectAccentColor
import br.com.estudario.ui.theme.estudarioColors
import java.time.LocalDate

private val weekdayInitials = listOf("S", "T", "Q", "Q", "S", "S", "D")

/**
 * O mês em grade, como um calendário de verdade: cada dia já mostra as matérias (na cor de cada
 * uma, riscadas quando feitas) e o tempo planejado, sem precisar tocar. Tocar escolhe o dia e as
 * tarefas dele aparecem logo abaixo.
 */
@Composable
fun MonthCalendar(
    month: LocalDate,
    today: LocalDate,
    tasks: List<PlannerTaskUi>,
    selected: LocalDate?,
    onSelect: (LocalDate) -> Unit,
) {
    val first = month.withDayOfMonth(1)
    val leading = first.dayOfWeek.value - 1 // segunda = 0
    val days = first.lengthOfMonth()
    val byDay = remember(tasks) { tasks.groupBy { LocalDate.ofEpochDay(it.entity.scheduledEpochDay) } }
    val palette = estudarioColors().subjectPalette
    val cells = ((leading + days + 6) / 7) * 7
    ElevatedCard(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Row(Modifier.fillMaxWidth()) {
                weekdayInitials.forEach { initial ->
                    Text(
                        initial,
                        Modifier.weight(1f),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Bold,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    )
                }
            }
            (0 until cells / 7).forEach { week ->
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    (0 until 7).forEach { column ->
                        val index = week * 7 + column - leading
                        if (index < 0 || index >= days) {
                            Spacer(Modifier.weight(1f))
                        } else {
                            val date = first.plusDays(index.toLong())
                            DayCell(
                                date = date,
                                tasks = byDay[date].orEmpty(),
                                isToday = date == today,
                                isSelected = date == selected,
                                isPast = date < today,
                                palette = palette,
                                modifier = Modifier.weight(1f),
                                onClick = { onSelect(date) },
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DayCell(
    date: LocalDate,
    tasks: List<PlannerTaskUi>,
    isToday: Boolean,
    isSelected: Boolean,
    isPast: Boolean,
    palette: List<androidx.compose.ui.graphics.Color>,
    modifier: Modifier,
    onClick: () -> Unit,
) {
    val active = tasks.filter { it.entity.status != PlanTaskStatus.REPROGRAMADA }
    val minutes = active.sumOf { it.entity.plannedMinutes }
    val subjects = active.groupBy { it.entity.subjectNameSnapshot.ifBlank { "Revisão" } }
        .entries.sortedByDescending { entry -> entry.value.sumOf { it.entity.plannedMinutes } }
    val allDone = active.isNotEmpty() && active.all { it.entity.status == PlanTaskStatus.CONCLUIDA }
    val scheme = MaterialTheme.colorScheme
    val shape = RoundedCornerShape(10.dp)
    val background = when {
        isSelected -> scheme.primaryContainer
        allDone -> scheme.secondaryContainer.copy(alpha = 0.55f)
        else -> scheme.surfaceVariant.copy(alpha = if (isPast) 0.25f else 0.45f)
    }
    Column(
        modifier
            .heightIn(min = 64.dp)
            .aspectRatio(0.62f, matchHeightConstraintsFirst = false)
            .clip(shape)
            .background(background)
            .then(if (isToday) Modifier.border(2.dp, scheme.primary, shape) else Modifier)
            .clickable(onClick = onClick)
            .semantics {
                contentDescription = if (active.isEmpty()) "Dia ${date.dayOfMonth}, livre"
                else "Dia ${date.dayOfMonth}: ${subjects.joinToString { it.key }}, ${minutesLabel(minutes)}"
            }
            .padding(horizontal = 3.dp, vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        Text(
            "${date.dayOfMonth}",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = if (isToday) FontWeight.Black else FontWeight.Bold,
            color = if (isToday) scheme.primary else if (isPast) scheme.onSurfaceVariant else scheme.onSurface,
        )
        subjects.take(2).forEach { (name, list) ->
            val color = subjectAccentColor(name, palette)
            val done = list.all { it.entity.status == PlanTaskStatus.CONCLUIDA }
            Box(
                Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(4.dp))
                    .background(color.copy(alpha = if (done) 0.18f else 0.30f))
                    .padding(horizontal = 2.dp),
            ) {
                Text(
                    shortSubject(name),
                    fontSize = 9.sp,
                    lineHeight = 11.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Clip,
                    color = scheme.onSurface,
                    textDecoration = if (done) TextDecoration.LineThrough else null,
                )
            }
        }
        if (subjects.size > 2) {
            Text("+${subjects.size - 2}", fontSize = 9.sp, lineHeight = 11.sp, color = scheme.onSurfaceVariant, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.weight(1f))
        if (minutes > 0) {
            Box(Modifier.fillMaxWidth().height(3.dp).clip(RoundedCornerShape(2.dp)).background(if (allDone) scheme.secondary else scheme.primary.copy(alpha = 0.7f)))
        }
    }
}

/** "Direito Constitucional" vira "D. Constit."; nomes curtos ficam como estão. */
internal fun shortSubject(name: String): String {
    val words = name.trim().split(Regex("\\s+")).filter { it.length > 2 || it.firstOrNull()?.isUpperCase() == true }
    if (name.length <= 9 || words.size <= 1) return name.trim()
    return words.first().take(1) + ". " + words.drop(1).first().take(7)
}
