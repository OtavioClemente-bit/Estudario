package br.com.estudario.ui.planner

import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material3.*
import br.com.estudario.ui.brand.Icon
import br.com.estudario.ui.brand.isCadernoNight
import br.com.estudario.ui.brand.marker
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

private val CalGold = Color(0xFFF5B83D)
private val CalMint = Color(0xFF2FBF8A)
private val weekInitials = listOf("S", "T", "Q", "Q", "S", "S", "D")

/**
 * O calendário do plano com a cara do Estudário: cada dia é um quadradinho de papel; hoje tem a
 * borda dourada, o dia feito fica verde com visto, o escolhido fica roxo com a "sombra de botão" e a
 * barrinha embaixo mostra quanto estudo o dia tem. Semana e mês num seletor, setas para os meses.
 */
@Composable
fun InteractiveCalendar(
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    taskDates: Set<LocalDate>,
    modifier: Modifier = Modifier,
    minutesByDate: Map<LocalDate, Int> = emptyMap(),
    doneDates: Set<LocalDate> = emptySet(),
) {
    var isExpanded by remember { mutableStateOf(false) }
    var currentMonth by remember { mutableStateOf(YearMonth.from(selectedDate)) }
    val today = LocalDate.now()
    val heaviest = (minutesByDate.values.maxOrNull() ?: 0).coerceAtLeast(60)
    val cell: @Composable RowScope.(LocalDate?) -> Unit = { date ->
        Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
            if (date != null) CalendarDay(
                date = date,
                isSelected = date == selectedDate,
                isToday = date == today,
                isDone = date in doneDates,
                hasTasks = date in taskDates,
                load = ((minutesByDate[date] ?: 0).toFloat() / heaviest).coerceIn(0f, 1f),
                dimmed = isExpanded && YearMonth.from(date) != currentMonth,
                onClick = { onDateSelected(date); currentMonth = YearMonth.from(date) },
            )
        }
    }

    Column(modifier = modifier.fillMaxWidth().animateContentSize(), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "${currentMonth.month.getDisplayName(TextStyle.FULL, Locale("pt", "BR")).replaceFirstChar { it.uppercase() }} ${currentMonth.year}",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Black,
                modifier = Modifier.padding(start = 3.dp).marker(),
            )
            Spacer(Modifier.weight(1f))
            MonthArrow(Icons.AutoMirrored.Outlined.KeyboardArrowLeft, "Mês anterior") { currentMonth = currentMonth.minusMonths(1); isExpanded = true }
            Spacer(Modifier.width(6.dp))
            MonthArrow(Icons.AutoMirrored.Outlined.KeyboardArrowRight, "Mês seguinte") { currentMonth = currentMonth.plusMonths(1); isExpanded = true }
        }
        // Semana ou mês, num seletor (no lugar do "Ver mês" escondido embaixo).
        // Duas abas de papel recortado; a escolhida fica na tinta azul e "afundada".
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            listOf(false to "Semana", true to "Mês").forEach { (expanded, label) ->
                val on = isExpanded == expanded
                CutTile(
                    Modifier.weight(1f).height(44.dp),
                    face = if (on) CalInk else MaterialTheme.colorScheme.surface,
                    pressed = on,
                    onClick = { isExpanded = expanded; if (!expanded) currentMonth = YearMonth.from(selectedDate) },
                ) {
                    Text(label, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Black, color = if (on) Color.White else MaterialTheme.colorScheme.onSurface)
                }
            }
        }
        Row(Modifier.fillMaxWidth()) {
            weekInitials.forEach { initial ->
                Text(initial, Modifier.weight(1f), style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center)
            }
        }
        if (isExpanded) {
            // O mês inteiro, de segunda a domingo; os dias de fora do mês aparecem apagados.
            val first = currentMonth.atDay(1)
            val gridStart = first.minusDays((first.dayOfWeek.value - 1).toLong())
            val last = currentMonth.atEndOfMonth()
            val gridEnd = last.plusDays((7 - last.dayOfWeek.value).toLong())
            var weekStart = gridStart
            while (!weekStart.isAfter(gridEnd)) {
                val start = weekStart
                Row(Modifier.fillMaxWidth()) { for (i in 0..6) cell(start.plusDays(i.toLong())) }
                weekStart = weekStart.plusWeeks(1)
            }
        } else {
            val weekStart = selectedDate.minusDays(selectedDate.dayOfWeek.value - 1L)
            Row(Modifier.fillMaxWidth()) { for (i in 0..6) cell(weekStart.plusDays(i.toLong())) }
        }
        // Legenda curta: o que cada cor quer dizer.
        Row(Modifier.fillMaxWidth().padding(top = 2.dp), horizontalArrangement = Arrangement.spacedBy(14.dp, Alignment.CenterHorizontally)) {
            Legend(Color(0xFFFFD23F), "Hoje")
            Legend(Color(0xFF3BB273), "Feito")
            Legend(CalInk, "Com estudo", bar = true)
        }
    }
}

@Composable
private fun MonthArrow(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, onClick: () -> Unit) {
    CutTile(Modifier.size(42.dp), face = MaterialTheme.colorScheme.surface, onClick = onClick) {
        Icon(icon, contentDescription = label, tint = MaterialTheme.colorScheme.onSurface)
    }
}

/** A tinta azul de caneta do Estudário (a mesma do botão principal e do site). */
private val CalInk = Color(0xFF2F5BD3)

/**
 * Um quadradinho de papel recortado: cantos desiguais, borda de tinta e sombra dura deslocada.
 * [pressed] mostra a peça afundada (sem sombra), como a aba escolhida.
 */
@Composable
private fun CutTile(modifier: Modifier, face: Color, pressed: Boolean = false, onClick: () -> Unit, content: @Composable () -> Unit) {
    val ink = br.com.estudario.ui.brand.cutInk()
    val shape = RoundedCornerShape(topStart = 11.dp, topEnd = 7.dp, bottomEnd = 12.dp, bottomStart = 8.dp)
    val source = remember { androidx.compose.foundation.interaction.MutableInteractionSource() }
    val down by source.collectIsPressedAsState()
    val sunk = pressed || down
    Box(
        modifier
            .clickable(source, indication = null, onClick = onClick)
            .padding(end = 3.dp, bottom = 3.dp)
            .drawBehind {
                if (!sunk) {
                    val o = shape.createOutline(size, layoutDirection, this)
                    translate(3.dp.toPx(), 3.dp.toPx()) { drawOutline(o, ink) }
                }
            }
            .offset(x = if (sunk) 2.dp else 0.dp, y = if (sunk) 2.dp else 0.dp)
            .background(face, shape)
            .border(2.dp, ink, shape),
        contentAlignment = Alignment.Center,
    ) { content() }
}

@Composable
private fun Legend(color: Color, label: String, ring: Boolean = false, bar: Boolean = false) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            Modifier.size(if (bar) 14.dp else 10.dp, if (bar) 4.dp else 10.dp).clip(if (bar) RoundedCornerShape(50) else CircleShape)
                .then(if (ring) Modifier.border(2.dp, color, CircleShape) else Modifier.background(color)),
        )
        Spacer(Modifier.width(6.dp))
        Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun CalendarDay(
    date: LocalDate,
    isSelected: Boolean,
    isToday: Boolean,
    isDone: Boolean,
    hasTasks: Boolean,
    load: Float,
    dimmed: Boolean,
    onClick: () -> Unit,
) {
    val primary = CalInk
    val lip = br.com.estudario.ui.brand.cutInk()
    val tile = when {
        isSelected -> CalInk
        isDone -> Color(0xFF3BB273)
        isToday -> Color(0xFFFFD23F)
        else -> MaterialTheme.colorScheme.surface
    }
    val textColor = when {
        isSelected -> Color.White
        isDone -> Color(0xFF08240F)
        isToday -> Color(0xFF2A2100)
        else -> MaterialTheme.colorScheme.onSurface
    }
    val cut = RoundedCornerShape(topStart = 11.dp, topEnd = 7.dp, bottomEnd = 12.dp, bottomStart = 8.dp)
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(vertical = 3.dp).clip(RoundedCornerShape(12.dp)).clickable(onClick = onClick).padding(2.dp),
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(40.dp)
                .drawBehind {
                    // Papel recortado: sombra dura de tinta deslocada (some no dia escolhido, que "afunda").
                    if (!isSelected) {
                        val o = cut.createOutline(size, layoutDirection, this)
                        translate(3.dp.toPx(), 3.dp.toPx()) { drawOutline(o, lip.copy(alpha = if (dimmed) 0.3f else 1f)) }
                    }
                }
                .offset(x = if (isSelected) 2.dp else 0.dp, y = if (isSelected) 2.dp else 0.dp)
                .background(tile.copy(alpha = if (dimmed) 0.5f else 1f), cut)
                .border(2.dp, lip.copy(alpha = if (dimmed) 0.3f else 1f), cut),
        ) {
            Text(
                text = if (isDone && !isSelected) "✓" else date.dayOfMonth.toString(),
                color = textColor.copy(alpha = if (dimmed) 0.35f else 1f),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = if (isSelected || isToday || isDone) FontWeight.Black else FontWeight.SemiBold,
            )
        }
        // Quanto estudo o dia tem: a barrinha cresce com os minutos planejados.
        Box(Modifier.padding(top = 6.dp).width(24.dp).height(4.dp).clip(RoundedCornerShape(50)).background(if (hasTasks) MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f) else Color.Transparent)) {
            if (hasTasks) Box(Modifier.fillMaxWidth(load.coerceAtLeast(0.25f)).height(4.dp).clip(RoundedCornerShape(50)).background(if (isDone) CalMint else primary))
        }
    }
}
