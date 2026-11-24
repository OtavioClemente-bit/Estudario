package br.com.estudario.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import br.com.estudario.ui.brand.Button
import br.com.estudario.ui.brand.FilterChip
import br.com.estudario.ui.brand.Icon
import br.com.estudario.ui.brand.NoteKind
import br.com.estudario.ui.brand.TextButton
import br.com.estudario.ui.theme.Nunito
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

// ---------------------------------------------------------------- lógica (testada em EstudarioDatePickerTest)

private val PtBr = Locale("pt", "BR")

/** As 42 casas (6 semanas, começando na segunda) do mês; null nas casas de outros meses. */
internal fun monthGrid(month: YearMonth): List<LocalDate?> {
    val first = month.atDay(1)
    val lead = first.dayOfWeek.value - DayOfWeek.MONDAY.value
    return List(42) { i ->
        val day = i - lead + 1
        if (day in 1..month.lengthOfMonth()) month.atDay(day) else null
    }
}

/** Atalho "Em N meses" da data da prova, sem passar do limite. */
internal fun quickDate(today: LocalDate, months: Long, maxDate: LocalDate? = null): LocalDate =
    today.plusMonths(months).let { if (maxDate != null && it.isAfter(maxDate)) maxDate else it }

internal fun isSelectable(date: LocalDate, minDate: LocalDate?, maxDate: LocalDate?): Boolean =
    (minDate == null || !date.isBefore(minDate)) && (maxDate == null || !date.isAfter(maxDate))

/** Primeiro e último mês navegáveis (sem limite, dez anos para cada lado). */
internal fun monthRange(today: LocalDate, minDate: LocalDate?, maxDate: LocalDate?): Pair<YearMonth, YearMonth> =
    YearMonth.from(minDate ?: today.minusYears(10)) to YearMonth.from(maxDate ?: today.plusYears(10))

/** Toque num dia ao escolher um período: o primeiro toque começa, o segundo fecha (ou recomeça se for antes). */
internal fun nextRange(start: LocalDate?, end: LocalDate?, tapped: LocalDate): Pair<LocalDate?, LocalDate?> = when {
    start == null || end != null -> tapped to null
    tapped.isBefore(start) -> tapped to null
    else -> start to tapped
}

internal fun monthTitle(month: YearMonth): String =
    month.month.getDisplayName(TextStyle.FULL, PtBr).replaceFirstChar { it.uppercase() } + " de " + month.year

private fun fmt(d: LocalDate) = "%02d/%02d/%d".format(d.dayOfMonth, d.monthValue, d.year)

// ---------------------------------------------------------------- diálogos

/**
 * O calendário do Estudário: uma folha de caderno pautada com margem vermelha, o mês escrito em
 * Nunito Black e o dia escolhido circulado como caneta. [examChips] mostra os atalhos da data da
 * prova ("Em 3 meses", "Em 6 meses" e, se [allowUnknown], "Ainda não sei", que confirma null).
 */
@Composable
fun EstudarioDatePickerDialog(
    initial: LocalDate?,
    onDismiss: () -> Unit,
    onConfirm: (LocalDate?) -> Unit,
    title: String = "Escolha a data",
    minDate: LocalDate? = null,
    maxDate: LocalDate? = null,
    examChips: Boolean = false,
    allowUnknown: Boolean = examChips,
    confirmLabel: String = "Usar esta data",
    today: LocalDate = LocalDate.now(),
) {
    var selected by remember { mutableStateOf(initial?.takeIf { isSelectable(it, minDate, maxDate) }) }
    var jump by remember { mutableStateOf<YearMonth?>(null) }
    PaperDialog(
        title = title,
        onDismiss = onDismiss,
        confirmLabel = selected?.let { "$confirmLabel · ${fmt(it)}" } ?: confirmLabel,
        confirmEnabled = selected != null,
        onConfirm = { onConfirm(selected) },
        dismissLabel = "Cancelar",
    ) {
        if (examChips) ExamChips(today, maxDate, selected, allowUnknown, onPick = { selected = it; jump = YearMonth.from(it) }, onUnknown = { onConfirm(null) })
        NotebookCalendar(
            today = today,
            minDate = minDate,
            maxDate = maxDate,
            start = selected,
            end = null,
            initialMonth = YearMonth.from(selected ?: today),
            jumpTo = jump,
            onDay = { selected = it },
        )
    }
}

/** O mesmo calendário para escolher um período (de/até). [intro] entra acima do mês. */
@Composable
fun EstudarioDateRangePickerDialog(
    onDismiss: () -> Unit,
    onConfirm: (LocalDate, LocalDate) -> Unit,
    title: String = "Escolha o período",
    minDate: LocalDate? = null,
    maxDate: LocalDate? = null,
    confirmLabel: (LocalDate?, LocalDate?) -> String = { a, b -> if (a == null) "Escolha os dias" else "De ${fmt(a)} a ${fmt(b ?: a)}" },
    dismissLabel: String = "Cancelar",
    today: LocalDate = LocalDate.now(),
    intro: @Composable ColumnScope.() -> Unit = {},
) {
    var start by remember { mutableStateOf<LocalDate?>(null) }
    var end by remember { mutableStateOf<LocalDate?>(null) }
    PaperDialog(
        title = title,
        onDismiss = onDismiss,
        confirmLabel = confirmLabel(start, end ?: start),
        confirmEnabled = start != null,
        onConfirm = { start?.let { onConfirm(it, end ?: it) } },
        dismissLabel = dismissLabel,
    ) {
        intro()
        NotebookCalendar(
            today = today,
            minDate = minDate,
            maxDate = maxDate,
            start = start,
            end = end,
            initialMonth = YearMonth.from(today),
            jumpTo = null,
            onDay = { d -> nextRange(start, end, d).let { start = it.first; end = it.second } },
        )
    }
}

@Composable
private fun PaperDialog(
    title: String,
    onDismiss: () -> Unit,
    confirmLabel: String,
    confirmEnabled: Boolean,
    onConfirm: () -> Unit,
    dismissLabel: String,
    content: @Composable ColumnScope.() -> Unit,
) {
    val scheme = MaterialTheme.colorScheme
    val dark = scheme.surface.red < 0.5f
    val paper = if (dark) Color(0xFF1E1D2B) else Color(0xFFFFFDF6)
    val line = if (dark) Color(0x229AA4FF) else Color(0x265B8DEF)
    val margin = if (dark) Color(0x66FF7A85) else Color(0x80E5484D)
    Dialog(onDismissRequest = onDismiss, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        Column(
            Modifier
                .padding(16.dp)
                .widthIn(max = 420.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(paper)
                .drawBehind {
                    val step = 28.dp.toPx()
                    var y = 56.dp.toPx()
                    while (y < size.height) {
                        drawLine(line, Offset(0f, y), Offset(size.width, y), strokeWidth = 1.dp.toPx())
                        y += step
                    }
                    val x = 22.dp.toPx()
                    drawLine(margin, Offset(x, 0f), Offset(x, size.height), strokeWidth = 1.5.dp.toPx())
                }
                .verticalScroll(rememberScrollState())
                .padding(start = 32.dp, end = 16.dp, top = 18.dp, bottom = 14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Text(title, style = MaterialTheme.typography.titleMedium, fontFamily = Nunito, fontWeight = FontWeight.Black, color = scheme.onSurface)
            content()
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End, verticalAlignment = Alignment.CenterVertically) {
                TextButton(onClick = onDismiss) { Text(dismissLabel) }
                Spacer(Modifier.padding(4.dp))
                Button(onClick = onConfirm, enabled = confirmEnabled, kind = NoteKind.Confirm) { Text(confirmLabel, textAlign = TextAlign.Center) }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ExamChips(today: LocalDate, maxDate: LocalDate?, selected: LocalDate?, allowUnknown: Boolean, onPick: (LocalDate) -> Unit, onUnknown: () -> Unit) {
    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
        listOf(3L to "Em 3 meses", 6L to "Em 6 meses").forEach { (m, label) ->
            val d = quickDate(today, m, maxDate)
            FilterChip(selected = selected == d, onClick = { onPick(d) }, label = { Text(label) })
        }
        if (allowUnknown) FilterChip(selected = false, onClick = onUnknown, label = { Text("Ainda não sei") })
    }
}

/** O mês em folha de caderno: setas e deslizar trocam de mês; toque escolhe o dia. */
@Composable
private fun NotebookCalendar(
    today: LocalDate,
    minDate: LocalDate?,
    maxDate: LocalDate?,
    start: LocalDate?,
    end: LocalDate?,
    initialMonth: YearMonth,
    jumpTo: YearMonth?,
    onDay: (LocalDate) -> Unit,
) {
    val (first, last) = remember(today, minDate, maxDate) { monthRange(today, minDate, maxDate) }
    val count = (first.until(last, java.time.temporal.ChronoUnit.MONTHS) + 1).toInt().coerceAtLeast(1)
    fun indexOf(m: YearMonth) = first.until(m, java.time.temporal.ChronoUnit.MONTHS).toInt().coerceIn(0, count - 1)
    val pager = rememberPagerState(initialPage = indexOf(initialMonth)) { count }
    val scope = rememberCoroutineScope()
    LaunchedEffect(jumpTo) { jumpTo?.let { pager.animateScrollToPage(indexOf(it)) } }
    val scheme = MaterialTheme.colorScheme
    val shown = first.plusMonths(pager.currentPage.toLong())

    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(
                monthTitle(shown),
                style = MaterialTheme.typography.headlineSmall,
                fontFamily = Nunito,
                fontWeight = FontWeight.Black,
                color = scheme.primary,
                modifier = Modifier.weight(1f),
            )
            IconButton(onClick = { scope.launch { pager.animateScrollToPage(pager.currentPage - 1) } }, enabled = pager.currentPage > 0) {
                Icon(Icons.AutoMirrored.Outlined.KeyboardArrowLeft, contentDescription = "Mês anterior")
            }
            IconButton(onClick = { scope.launch { pager.animateScrollToPage(pager.currentPage + 1) } }, enabled = pager.currentPage < count - 1) {
                Icon(Icons.AutoMirrored.Outlined.KeyboardArrowRight, contentDescription = "Próximo mês")
            }
        }
        Row(Modifier.fillMaxWidth()) {
            listOf("S", "T", "Q", "Q", "S", "S", "D").forEach {
                Text(it, Modifier.weight(1f), textAlign = TextAlign.Center, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = scheme.onSurfaceVariant, maxLines = 1)
            }
        }
        HorizontalPager(pager, Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top) { page ->
            val grid = remember(page) { monthGrid(first.plusMonths(page.toLong())) }
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                grid.chunked(7).forEach { week ->
                    Row(Modifier.fillMaxWidth()) {
                        week.forEach { day ->
                            Box(Modifier.weight(1f).heightIn(min = 40.dp), contentAlignment = Alignment.Center) {
                                if (day != null) DayCell(day, today, minDate, maxDate, start, end, onDay)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DayCell(day: LocalDate, today: LocalDate, minDate: LocalDate?, maxDate: LocalDate?, start: LocalDate?, end: LocalDate?, onDay: (LocalDate) -> Unit) {
    val scheme = MaterialTheme.colorScheme
    val enabled = isSelectable(day, minDate, maxDate)
    val isEdge = day == start || day == end
    val inRange = start != null && end != null && day.isAfter(start) && day.isBefore(end)
    val isToday = day == today
    val pen = scheme.primary
    // O círculo de caneta se desenha quando o dia é escolhido.
    val draw = remember { Animatable(if (isEdge) 1f else 0f) }
    LaunchedEffect(isEdge) {
        if (isEdge) { draw.snapTo(0f); draw.animateTo(1f, tween(320, easing = FastOutSlowInEasing)) } else draw.snapTo(0f)
    }
    val label = "${day.dayOfMonth} de ${day.month.getDisplayName(TextStyle.FULL, PtBr)}" + (if (isToday) ", hoje" else "")
    Box(
        Modifier
            .fillMaxWidth()
            .heightIn(min = 40.dp)
            .padding(horizontal = 1.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(if (inRange) pen.copy(alpha = 0.12f) else Color.Transparent)
            .clickable(enabled = enabled) { onDay(day) }
            .semantics { contentDescription = label; selected = isEdge }
            .drawBehind {
                val p = draw.value
                if (p > 0f) {
                    val r = minOf(size.width, size.height) / 2f - 2.dp.toPx()
                    val c = Offset(size.width / 2f, size.height / 2f)
                    rotate(-12f, c) {
                        drawArc(
                            pen,
                            startAngle = -80f,
                            sweepAngle = 375f * p,
                            useCenter = false,
                            topLeft = Offset(c.x - r * 1.08f, c.y - r * 0.92f),
                            size = Size(r * 2.16f, r * 1.84f),
                            style = Stroke(width = 2.2.dp.toPx(), cap = StrokeCap.Round),
                        )
                    }
                }
                if (isToday) {
                    val w = 12.dp.toPx()
                    val y = size.height / 2f + 11.dp.toPx()
                    drawLine(scheme.tertiary, Offset(size.width / 2f - w / 2f, y), Offset(size.width / 2f + w / 2f, y), strokeWidth = 2.dp.toPx(), cap = StrokeCap.Round)
                }
            },
        contentAlignment = Alignment.Center,
    ) {
        Text(
            "${day.dayOfMonth}",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = if (isEdge || isToday) FontWeight.Black else FontWeight.Medium,
            color = when { !enabled -> scheme.onSurface.copy(alpha = 0.3f); isEdge -> pen; else -> scheme.onSurface },
            maxLines = 1,
            softWrap = false,
        )
    }
}
