package br.com.estudario.ui.focus

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.estudario.domain.FocusInsights
import br.com.estudario.ui.AppViewModel
import br.com.estudario.ui.assistant.Folha
import br.com.estudario.ui.assistant.FolhaMood
import br.com.estudario.ui.brand.FilterChip
import br.com.estudario.ui.brand.Icon
import br.com.estudario.ui.brand.Surface
import br.com.estudario.ui.theme.estudarioColors
import br.com.estudario.ui.theme.screenPadding
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import kotlin.math.ceil
import kotlin.math.roundToInt

/*
 * Histórico do foco que explica o estudo (a conta em FocusInsights, módulo comum; o site faz igual):
 * os números do período com a comparação ao anterior, as frases do que aconteceu, o tempo por dia
 * empilhado por matéria, a divisão por matéria, o horário e o dia da semana em que a pessoa mais rende,
 * os tópicos mais estudados e as sessões. Cores das matérias pela paleta do tema, na ordem da legenda.
 */

private val PERIODS = listOf(7 to "7 dias", 30 to "30 dias", 90 to "90 dias")
private val WEEK = listOf("Seg", "Ter", "Qua", "Qui", "Sex", "Sáb", "Dom")

private fun fmt(min: Double): String = formatLongMinutes(min.roundToInt().toLong())

@Composable
fun FocusHistoryScreen(viewModel: AppViewModel) {
    val history by viewModel.focusSessions.collectAsState()
    val subjects by viewModel.subjects.collectAsState()
    val topics by viewModel.topics.collectAsState()
    val activeSession by viewModel.focusSession.collectAsState()
    var period by rememberSaveable { mutableIntStateOf(7) }
    val zone = ZoneId.systemDefault()
    val scheme = MaterialTheme.colorScheme
    val colors = estudarioColors()
    val subjectName = remember(subjects) { subjects.associate { it.id to it.name } }
    val topicById = remember(topics) { topics.associateBy { it.id } }

    val sessions = remember(history, subjectName, topicById) {
        history.map { s ->
            val at = Instant.ofEpochMilli(s.startedAt).atZone(zone)
            val topic = s.topicId?.let(topicById::get)
            val subject = topic?.subjectId?.let(subjectName::get) ?: s.subjectIdsText.split(',').firstNotNullOfOrNull { it.trim().toLongOrNull()?.let(subjectName::get) } ?: "Foco livre"
            FocusInsights.Session(s.id, at.toLocalDate().toEpochDay(), at.hour, at.dayOfWeek.value - 1, s.durationSeconds / 60.0, subject, topic?.title)
        }
    }
    val today = LocalDate.now(zone).toEpochDay()
    val r = remember(sessions, period, today) { FocusInsights.report(sessions, today, period) }
    val palette = colors.subjectPalette
    fun colorOf(name: String): Color = if (name == FocusInsights.OTHERS) scheme.outline else palette[r.legend.indexOf(name).coerceAtLeast(0) % palette.size]
    val inPeriod = history.filter { Instant.ofEpochMilli(it.startedAt).atZone(zone).toLocalDate().toEpochDay() >= today - period + 1 }.sortedByDescending { it.startedAt }

    LazyColumn(Modifier.fillMaxSize(), contentPadding = screenPadding(vertical = 16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("Histórico do foco", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                Text("O que você anda estudando, quando e se está melhorando.", style = MaterialTheme.typography.bodyMedium, color = scheme.onSurfaceVariant)
            }
        }
        if (activeSession.active) item {
            Surface(shape = RoundedCornerShape(16.dp), color = scheme.secondaryContainer, modifier = Modifier.fillMaxWidth()) {
                Row(Modifier.padding(14.dp), horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Outlined.Timer, null, tint = scheme.primary)
                    Text("Sessão em andamento: entra no histórico quando for encerrada.", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
        if (history.isEmpty()) {
            item { Panel { Text("Ainda sem sessões", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold); Text("Ligue o Modo foco antes de estudar e encerre ao terminar: aqui aparecem os gráficos do que você estudou.", color = scheme.onSurfaceVariant) } }
            return@LazyColumn
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) { PERIODS.forEach { (d, label) -> FilterChip(period == d, { period = d }, label = { Text(label) }) } }
        }
        item {
            val delta = if (r.previousTotal > 0) ((r.total - r.previousTotal) / r.previousTotal * 100).roundToInt() else null
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Kpi("Tempo de foco", fmt(r.total), delta?.let { if (it >= 0) "▲ $it%" else "▼ ${-it}%" }, delta?.let { if (it >= 0) colors.completed else scheme.error }, Modifier.weight(1f))
                    Kpi("Dias com foco", "${r.activeDays} de $period", null, null, Modifier.weight(1f))
                }
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Kpi("Sessões", "${r.sessions}", if (r.sessions > 0) "média ${fmt(r.averageSession)}" else null, null, Modifier.weight(1f))
                    Kpi("Melhor dia", r.bestDay?.let { fmt(it.total) } ?: "—", r.bestDay?.let { LocalDate.ofEpochDay(it.epochDay).let { d -> "${d.dayOfMonth}/${d.monthValue}" } }, null, Modifier.weight(1f))
                }
            }
        }
        if (r.total <= 0) {
            item { Panel { Text("Sem foco neste período", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold); Text("Troque o período ou ligue o Modo foco hoje.", color = scheme.onSurfaceVariant) } }
            return@LazyColumn
        }
        if (r.insights.isNotEmpty()) item {
            Surface(shape = RoundedCornerShape(16.dp), color = scheme.surfaceContainerLow, modifier = Modifier.fillMaxWidth()) {
                Row(Modifier.height(IntrinsicSize.Min)) {
                    Box(Modifier.width(4.dp).fillMaxHeight().background(Color(0xFFFFB421)))
                    Row(Modifier.padding(14.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Folha(48.dp, mood = FolhaMood.TALKING, enter = false)
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text("O que o seu foco mostra", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                            r.insights.forEach { Text("• $it", style = MaterialTheme.typography.bodySmall, color = scheme.onSurfaceVariant) }
                        }
                    }
                }
            }
        }
        // tempo por dia, empilhado por matéria
        item {
            Panel {
                Text("Tempo por dia", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                val max = r.days.maxOf { it.total }.coerceAtLeast(1.0)
                val step = niceStep(max)
                val top = ceil(max / step) * step
                val grid = scheme.outlineVariant
                val base = scheme.outline
                Row(Modifier.fillMaxWidth().height(190.dp).padding(top = 8.dp)) {
                    Column(Modifier.fillMaxHeight().padding(bottom = 18.dp, end = 6.dp), verticalArrangement = Arrangement.SpaceBetween, horizontalAlignment = Alignment.End) {
                        (3 downTo 0).forEach { k -> Text(fmt(top * k / 3), fontSize = 10.sp, color = scheme.onSurfaceVariant) }
                    }
                    Column(Modifier.weight(1f).fillMaxHeight()) {
                        Canvas(Modifier.fillMaxWidth().weight(1f)) {
                            for (k in 1..3) {
                                val y = size.height * (1 - k / 3f)
                                drawLine(grid, Offset(0f, y), Offset(size.width, y), 1.dp.toPx(), pathEffect = PathEffect.dashPathEffect(floatArrayOf(6f, 6f)))
                            }
                            drawLine(base, Offset(0f, size.height), Offset(size.width, size.height), 1.dp.toPx())
                            val n = r.days.size
                            val gap = if (n > 31) 1.5.dp.toPx() else 5.dp.toPx()
                            val w = (size.width - gap * (n - 1)) / n
                            r.days.forEachIndexed { i, d ->
                                var y = size.height
                                val x = i * (w + gap)
                                val parts = r.legend.mapNotNull { name -> d.bySubject[name]?.takeIf { it > 0 }?.let { name to it } }
                                parts.forEachIndexed { pi, (name, v) ->
                                    val h = (v / top * size.height).toFloat()
                                    y -= h
                                    val radius = if (pi == parts.lastIndex) CornerRadius(4.dp.toPx().coerceAtMost(w / 2)) else CornerRadius.Zero
                                    drawRoundRect(colorOf(name), Offset(x, y), Size(w, h), radius)
                                }
                            }
                        }
                        Row(Modifier.fillMaxWidth().height(18.dp)) {
                            r.days.forEachIndexed { i, d ->
                                val date = LocalDate.ofEpochDay(d.epochDay)
                                val show = r.days.size <= 7 || i % (if (r.days.size <= 31) 5 else 14) == 0 || i == r.days.lastIndex
                                Box(Modifier.weight(1f), contentAlignment = Alignment.BottomCenter) {
                                    if (show) Text(if (r.days.size <= 7) WEEK[date.dayOfWeek.value - 1] else "${date.dayOfMonth}/${date.monthValue}", fontSize = 9.sp, color = scheme.onSurfaceVariant, maxLines = 1, softWrap = false)
                                }
                            }
                        }
                    }
                }
                FlowLegend(r.legend.map { it to colorOf(it) })
            }
        }
        // onde foi o foco (rosca)
        item {
            Panel {
                Text("Onde foi o seu foco", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                val grouped = r.subjects.groupBy { if (it.subject in r.legend) it.subject else FocusInsights.OTHERS }.map { (k, l) -> k to l.sumOf { it.share } }.sortedBy { r.legend.indexOf(it.first) }
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.padding(top = 8.dp)) {
                    Box(Modifier.size(120.dp), contentAlignment = Alignment.Center) {
                        Canvas(Modifier.fillMaxSize()) {
                            var start = -90f
                            val stroke = 22.dp.toPx()
                            grouped.forEach { (k, share) ->
                                val sweep = (share * 360).toFloat()
                                drawArc(colorOf(k), start, (sweep - if (grouped.size > 1) 1.5f else 0f).coerceAtLeast(0.5f), false, Offset(stroke / 2, stroke / 2), Size(size.width - stroke, size.height - stroke), style = Stroke(stroke))
                                start += sweep
                            }
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(fmt(r.total), fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleSmall)
                            Text("no período", fontSize = 10.sp, color = scheme.onSurfaceVariant)
                        }
                    }
                    Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        r.subjects.take(6).forEach { s ->
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Box(Modifier.size(10.dp).clip(RoundedCornerShape(3.dp)).background(colorOf(if (s.subject in r.legend) s.subject else FocusInsights.OTHERS)))
                                Text(s.subject, Modifier.weight(1f), style = MaterialTheme.typography.bodySmall, maxLines = 2, overflow = TextOverflow.Ellipsis)
                                Text("${(s.share * 100).roundToInt()}%", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
        // quando você rende
        item {
            Panel {
                Text("Quando você rende", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text("Minutos de foco por hora do dia", style = MaterialTheme.typography.labelSmall, color = scheme.onSurfaceVariant)
                val hMax = r.byHour.max().coerceAtLeast(1.0)
                val primary = scheme.primary
                val base = scheme.outline
                Canvas(Modifier.fillMaxWidth().height(80.dp).padding(top = 6.dp)) {
                    val w = size.width / 24
                    r.byHour.forEachIndexed { h, v ->
                        if (v > 0) {
                            val bh = (v / hMax * size.height).toFloat().coerceAtLeast(3.dp.toPx())
                            drawRoundRect(primary.copy(alpha = (0.35 + 0.65 * v / hMax).toFloat()), Offset(h * w + 1, size.height - bh), Size(w - 2, bh), CornerRadius(2.dp.toPx()))
                        }
                    }
                    drawLine(base, Offset(0f, size.height), Offset(size.width, size.height), 1.dp.toPx())
                }
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) { listOf("0h", "6h", "12h", "18h", "23h").forEach { Text(it, fontSize = 10.sp, color = scheme.onSurfaceVariant) } }
                Text("Por dia da semana", Modifier.padding(top = 10.dp), style = MaterialTheme.typography.labelSmall, color = scheme.onSurfaceVariant)
                val wMax = r.byWeekday.max().coerceAtLeast(1.0)
                Row(Modifier.fillMaxWidth().height(90.dp).padding(top = 6.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    WEEK.forEachIndexed { w, label ->
                        Column(Modifier.weight(1f).fillMaxHeight(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Box(Modifier.weight(1f).fillMaxWidth().clip(RoundedCornerShape(6.dp)).background(scheme.surfaceVariant), contentAlignment = Alignment.BottomCenter) {
                                val f = (r.byWeekday[w] / wMax).toFloat()
                                if (f > 0f) Box(Modifier.fillMaxWidth().fillMaxHeight(f).clip(RoundedCornerShape(6.dp)).background(Color(0xFFFFB421)))
                            }
                            Text(label, fontSize = 10.sp, color = scheme.onSurfaceVariant)
                        }
                    }
                }
            }
        }
        if (r.topTopics.isNotEmpty()) item {
            Panel {
                Text("Tópicos em que você mais focou", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                val tMax = r.topTopics.first().second.coerceAtLeast(1.0)
                r.topTopics.forEachIndexed { i, (title, minutes) ->
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Box(Modifier.size(26.dp).clip(RoundedCornerShape(8.dp)).background(scheme.surfaceVariant), contentAlignment = Alignment.Center) { Text("${i + 1}", fontWeight = FontWeight.Bold, fontSize = 12.sp) }
                        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Row { Text(title, Modifier.weight(1f), style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold, maxLines = 1, overflow = TextOverflow.Ellipsis); Text(fmt(minutes), style = MaterialTheme.typography.labelSmall, color = scheme.onSurfaceVariant) }
                            Box(Modifier.fillMaxWidth().height(6.dp).clip(CircleShape).background(scheme.surfaceVariant)) {
                                Box(Modifier.fillMaxWidth((minutes / tMax).toFloat()).fillMaxHeight().clip(CircleShape).background(scheme.primary))
                            }
                        }
                    }
                }
            }
        }
        item { Text("Sessões do período", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold) }
        if (inPeriod.isEmpty()) item { Text("Nenhuma sessão neste período.", color = scheme.onSurfaceVariant) }
        items(inPeriod.take(40), key = { it.id }) { session ->
            var confirmDelete by remember { mutableStateOf(false) }
            if (confirmDelete) AlertDialog(
                onDismissRequest = { confirmDelete = false },
                title = { Text("Excluir sessão?") },
                text = { Text("Ela sai do histórico do foco aqui e no site.") },
                confirmButton = { TextButton(onClick = { confirmDelete = false; viewModel.deleteFocusSession(session.id) }) { Text("Excluir") } },
                dismissButton = { TextButton(onClick = { confirmDelete = false }) { Text("Cancelar") } },
            )
            val at = Instant.ofEpochMilli(session.startedAt).atZone(zone)
            val topic = session.topicId?.let(topicById::get)
            val subject = sessions.firstOrNull { it.id == session.id }?.subject ?: "Foco livre"
            Surface(shape = RoundedCornerShape(14.dp), color = scheme.surfaceContainerLow, modifier = Modifier.fillMaxWidth()) {
                Row(Modifier.height(IntrinsicSize.Min), verticalAlignment = Alignment.CenterVertically) {
                    Box(Modifier.width(4.dp).fillMaxHeight().background(colorOf(if (subject in r.legend) subject else FocusInsights.OTHERS)))
                    Column(Modifier.weight(1f).padding(horizontal = 12.dp, vertical = 10.dp)) {
                        Text(session.title.ifBlank { topic?.title ?: "Foco livre" }, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold, maxLines = 2, overflow = TextOverflow.Ellipsis)
                        Text("${at.dayOfMonth}/${at.monthValue} · ${at.hour}h${at.minute.toString().padStart(2, '0')} · $subject", style = MaterialTheme.typography.labelSmall, color = scheme.onSurfaceVariant, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    }
                    Text(formatLongMinutes(session.durationSeconds / 60), style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                    IconButton(onClick = { confirmDelete = true }) { Icon(Icons.Outlined.Delete, "Excluir sessão", tint = scheme.onSurfaceVariant) }
                }
            }
        }
    }
}

@Composable
private fun Panel(content: @Composable ColumnScope.() -> Unit) {
    Surface(shape = RoundedCornerShape(18.dp), color = MaterialTheme.colorScheme.surfaceContainerLow, modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp), content = content)
    }
}

@Composable
private fun Kpi(label: String, value: String, sub: String?, subColor: Color?, modifier: Modifier) {
    Surface(shape = RoundedCornerShape(16.dp), color = MaterialTheme.colorScheme.surfaceContainerLow, modifier = modifier) {
        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(value, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, maxLines = 1)
            sub?.let { Text(it, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = subColor ?: MaterialTheme.colorScheme.onSurfaceVariant) }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun FlowLegend(items: List<Pair<String, Color>>) {
    FlowRow(horizontalArrangement = Arrangement.spacedBy(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
        items.forEach { (name, c) ->
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Box(Modifier.size(10.dp).clip(RoundedCornerShape(3.dp)).background(c))
                Text(name, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

/** Passo "redondo" do eixo (15, 30, 60, 120 min…). */
private fun niceStep(max: Double): Double {
    val raw = max / 3
    return listOf(5.0, 10.0, 15.0, 20.0, 30.0, 60.0, 90.0, 120.0, 180.0, 240.0, 360.0, 480.0).firstOrNull { it >= raw } ?: (ceil(raw / 60) * 60)
}
