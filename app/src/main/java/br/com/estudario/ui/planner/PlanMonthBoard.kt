package br.com.estudario.ui.planner

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import br.com.estudario.ui.brand.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.translate
import br.com.estudario.ui.brand.cutPanel
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.estudario.domain.planner.PlanTaskStatus
import br.com.estudario.domain.planner.PlanTaskType
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale
import kotlin.math.roundToInt

/** A cor de cada tipo de estudo no mês (a mesma legenda embaixo do calendário). */
internal fun taskTypeColor(type: PlanTaskType): Color = when (type) {
    PlanTaskType.THEORY -> Color(0xFF2F5BD3)
    PlanTaskType.QUESTIONS -> Color(0xFF22B07D)
    PlanTaskType.REVIEW, PlanTaskType.ACTIVE_RECALL, PlanTaskType.FLASHCARDS -> Color(0xFF3B9BE8)
    PlanTaskType.SIMULATION, PlanTaskType.DISCURSIVE -> Color(0xFFF2A93B)
}

private val LateColor = Color(0xFFF0506B)
private val DoneColor = Color(0xFF9AA0A6)
private val pending = setOf(PlanTaskStatus.PLANEJADA, PlanTaskStatus.EM_ANDAMENTO)

/**
 * O mês inteiro do plano, legível: cada dia mostra os estudos dele (cor pelo tipo, atrasado em
 * vermelho, feito apagado) e um toque abre a lista do dia embaixo. Nos planos pagos, segurar um
 * estudo da lista e soltar num dia do calendário muda ele de dia (fica fixado lá e o resto do
 * plano se encaixa em volta). No Grátis, segurar mostra a oferta.
 */
@Composable
/** O nome curto da matéria, como no site ("Direito - Administrativo" vira "Administrativo"). */
private fun monthSubject(name: String) = name.substringAfter(" \u2013 ").substringAfter(" - ").trim()

/** Minutos curtos para caber no dia: 45m, 1h, 1h30. */
private fun shortMin(minutes: Int): String = when {
    minutes < 60 -> "${minutes}m"
    minutes % 60 == 0 -> "${minutes / 60}h"
    else -> "${minutes / 60}h${(minutes % 60).toString().padStart(2, '0')}"
}

/** Peça de papel recortado (setas do mês). */
@Composable
private fun MonthCut(modifier: Modifier, face: Color, onClick: () -> Unit, content: @Composable () -> Unit) {
    val ink = br.com.estudario.ui.brand.cutInk()
    val shape = RoundedCornerShape(11.dp, 7.dp, 12.dp, 8.dp)
    Box(
        modifier.clickable(onClick = onClick).padding(end = 3.dp, bottom = 3.dp)
            .drawBehind { val o = shape.createOutline(size, layoutDirection, this); translate(3.dp.toPx(), 3.dp.toPx()) { drawOutline(o, ink) } }
            .clip(shape).background(face).border(2.dp, ink, shape),
        contentAlignment = Alignment.Center,
    ) { content() }
}

/** O painel do dia: papel com borda de tinta e sombra dura. */
@Composable
private fun Modifier.br_cutPanel(): Modifier {
    val shape = RoundedCornerShape(18.dp, 11.dp, 20.dp, 13.dp)
    return this.cutPanel(shape).clip(shape).background(MaterialTheme.colorScheme.surface)
}

@Composable
internal fun PlanMonthBoard(
    state: ActivePlanUiState,
    onFocus: (PlannerTaskUi) -> Unit,
    onMove: (PlannerTaskUi, LocalDate) -> Unit,
    onSwap: (PlannerTaskUi, PlannerTaskUi) -> Unit = { _, _ -> },
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    val density = LocalDensity.current
    val canEdit = remember { br.com.estudario.data.planner.LibraryGaps.knownPaid(context) }
    var upgrade by remember { mutableStateOf(false) }
    if (upgrade) br.com.estudario.ui.plans.UpgradeDialog(br.com.estudario.ui.plans.UpgradeReason.PLAN_EDIT, onDismiss = { upgrade = false })

    var month by remember { mutableStateOf(YearMonth.from(state.today)) }
    var selected by remember { mutableStateOf<LocalDate?>(state.today) }
    val today = state.today.toEpochDay()
    val byDay = remember(state.tasks) {
        state.tasks.filter { it.entity.status.countsAsPlannedLoad() || (it.entity.status in pending) }.groupBy { it.entity.scheduledEpochDay }
    }
    val offDays = remember(state.dayOverrides) { state.dayOverrides.filter { it.unavailable }.mapTo(hashSetOf()) { it.epochDay } }
    // O limite de cada dia: as horas daquele dia da semana, ou o que foi marcado só para ele.
    fun limitOf(epoch: Long): Int {
        state.dayOverrides.firstOrNull { it.epochDay == epoch }?.let { o -> if (o.unavailable) return 0; o.availableMinutes?.let { return it } }
        val dow = LocalDate.ofEpochDay(epoch).dayOfWeek.value
        return state.availability.firstOrNull { it.dayOfWeek == dow }?.let { if (it.unavailable) 0 else it.availableMinutes } ?: 0
    }
    // passou do limite do dia: a pessoa escolhe trocar com um estudo de lá, colocar mesmo assim ou cancelar
    var overflow by remember { mutableStateOf<Pair<PlannerTaskUi, Long>?>(null) }
    overflow?.let { (task, to) ->
        val there = byDay[to].orEmpty().filter { it.entity.status in pending }
        val limit = limitOf(to)
        val load = byDay[to].orEmpty().sumOf { it.entity.plannedMinutes }
        androidx.compose.material3.AlertDialog(
            onDismissRequest = { overflow = null },
            title = { Text(if (limit == 0) "Esse dia é de folga" else "Esse dia passa do seu limite") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        if (limit == 0) "${dayLabelPtBr(LocalDate.ofEpochDay(to), state.today)} é folga no seu plano."
                        else "${dayLabelPtBr(LocalDate.ofEpochDay(to), state.today)} já tem ${minutesLabel(load)} de ${minutesLabel(limit)}. Com este estudo passa do limite.",
                    )
                    if (there.isNotEmpty()) {
                        Text("Trocar com um estudo de lá:", style = MaterialTheme.typography.labelMedium)
                        there.forEach { other ->
                            Surface(
                                shape = RoundedCornerShape(12.dp), color = MaterialTheme.colorScheme.surfaceContainerHigh,
                                modifier = Modifier.fillMaxWidth().clickable { overflow = null; onSwap(task, other) },
                            ) {
                                Column(Modifier.padding(10.dp)) {
                                    Text(other.entity.topicNameSnapshot ?: other.entity.subjectNameSnapshot, fontWeight = FontWeight.Bold, maxLines = 2, overflow = TextOverflow.Ellipsis)
                                    Text("${other.entity.subjectNameSnapshot} · ${minutesLabel(other.entity.plannedMinutes)}", style = MaterialTheme.typography.labelSmall)
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = { TextButton(onClick = { overflow = null; onMove(task, LocalDate.ofEpochDay(to)); selected = LocalDate.ofEpochDay(to) }) { Text("Colocar mesmo assim") } },
            dismissButton = { TextButton(onClick = { overflow = null }) { Text("Cancelar") } },
        )
    }

    // arrastar: onde está cada dia na tela, o estudo que está sendo levado e o dedo
    val cells = remember { mutableStateMapOf<Long, Rect>() }
    var dragging by remember { mutableStateOf<PlannerTaskUi?>(null) }
    var finger by remember { mutableStateOf(Offset.Zero) }
    var boardOrigin by remember { mutableStateOf(Offset.Zero) }
    val target = dragging?.let { cells.entries.firstOrNull { (_, r) -> r.contains(finger) }?.key }

    Box(Modifier.fillMaxWidth().onGloballyPositioned { boardOrigin = it.positionInRoot() }) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            // cabeçalho do mês: setas recortadas, o mês e "Hoje" quando estiver em outro mês (igual ao site)
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                MonthCut(Modifier.size(42.dp), face = MaterialTheme.colorScheme.surface, onClick = { month = month.minusMonths(1) }) { Text("‹", fontSize = 22.sp, fontWeight = FontWeight.Black) }
                Text(
                    month.month.getDisplayName(TextStyle.FULL, Locale("pt", "BR")).replaceFirstChar { it.uppercase() } + " de ${month.year}",
                    style = MaterialTheme.typography.titleLarge, modifier = Modifier.weight(1f),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                )
                MonthCut(Modifier.size(42.dp), face = MaterialTheme.colorScheme.surface, onClick = { month = month.plusMonths(1) }) { Text("›", fontSize = 22.sp, fontWeight = FontWeight.Black) }
            }
            if (month != YearMonth.from(state.today)) TextButton(onClick = { month = YearMonth.from(state.today); selected = state.today }, modifier = Modifier.align(Alignment.CenterHorizontally)) { Text("Voltar para hoje") }
            // legenda: as mesmas etiquetas do site
            androidx.compose.foundation.layout.FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                listOf("Teoria" to taskTypeColor(PlanTaskType.THEORY), "Questões" to taskTypeColor(PlanTaskType.QUESTIONS), "Revisão" to taskTypeColor(PlanTaskType.REVIEW), "Simulado" to taskTypeColor(PlanTaskType.SIMULATION), "Atrasado" to LateColor).forEach { (label, c) ->
                    Text(label, Modifier.clip(RoundedCornerShape(8.dp, 5.dp, 9.dp, 6.dp)).background(c.copy(alpha = 0.16f)).border(1.5.dp, c, RoundedCornerShape(8.dp, 5.dp, 9.dp, 6.dp)).padding(horizontal = 8.dp, vertical = 3.dp),
                        style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.onSurface)
                }
            }
            // a semana começa na segunda, como no site
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                listOf("Seg", "Ter", "Qua", "Qui", "Sex", "Sáb", "Dom").forEach {
                    Text(it.uppercase(), Modifier.weight(1f), style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                }
            }
            val first = month.atDay(1)
            val lead = first.dayOfWeek.value - 1
            val total = lead + month.lengthOfMonth()
            val weeks = (total + 6) / 7
            val ink = br.com.estudario.ui.brand.cutInk()
            for (w in 0 until weeks) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                    for (d in 0 until 7) {
                        val index = w * 7 + d - lead
                        if (index < 0 || index >= month.lengthOfMonth()) { Spacer(Modifier.weight(1f)); continue }
                        val date = month.atDay(index + 1)
                        val epoch = date.toEpochDay()
                        val tasks = byDay[epoch].orEmpty().sortedWith(compareBy({ it.entity.status == PlanTaskStatus.CONCLUIDA }, { it.entity.type != PlanTaskType.THEORY }))
                        val isTarget = target == epoch
                        val isSelected = selected == date
                        val past = epoch < today
                        val limit = limitOf(epoch)
                        val used = tasks.sumOf { it.entity.plannedMinutes }
                        val doneCount = tasks.count { it.entity.status == PlanTaskStatus.CONCLUIDA }
                        val cellShape = RoundedCornerShape(9.dp, 5.dp, 10.dp, 6.dp)
                        Column(
                            Modifier.weight(1f).aspectRatio(0.56f)
                                .padding(end = 2.dp, bottom = 2.dp)
                                .drawBehind {
                                    if (!isSelected) {
                                        val o = cellShape.createOutline(size, layoutDirection, this)
                                        translate(2.dp.toPx(), 2.dp.toPx()) { drawOutline(o, ink.copy(alpha = if (past) 0.35f else 1f)) }
                                    }
                                }
                                .clip(cellShape)
                                .background(
                                    when {
                                        isTarget -> Color(0xFF2F5BD3).copy(alpha = 0.25f)
                                        limit == 0 -> MaterialTheme.colorScheme.surfaceVariant
                                        else -> MaterialTheme.colorScheme.surface
                                    },
                                )
                                .border(if (isSelected || isTarget) 2.5.dp else 1.5.dp, if (isSelected || isTarget) Color(0xFF2F5BD3) else ink.copy(alpha = if (past) 0.35f else 1f), cellShape)
                                .onGloballyPositioned { cells[epoch] = it.boundsInRoot() }
                                .clickable { selected = date },
                            verticalArrangement = Arrangement.spacedBy(2.dp),
                        ) {
                            // a cabeça do dia: o número (hoje em amarelo) e o tempo ocupado
                            Row(
                                Modifier.fillMaxWidth().background(if (epoch == today) Color(0xFFFFD23F) else Color.Transparent).padding(horizontal = 3.dp, vertical = 1.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Text(
                                    "${date.dayOfMonth}", style = MaterialTheme.typography.labelMedium, fontFamily = br.com.estudario.ui.theme.MonoNum,
                                    fontWeight = FontWeight.Bold,
                                    color = when { epoch == today -> Color(0xFF2A2100); past -> MaterialTheme.colorScheme.onSurfaceVariant; else -> MaterialTheme.colorScheme.onSurface },
                                )
                            }
                            Column(Modifier.padding(horizontal = 3.dp), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                if (limit == 0) Text("folga", fontSize = 8.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                else if (used > 0) {
                                    Text("${shortMin(used)}/${shortMin(limit)}", fontSize = 8.sp, lineHeight = 9.sp, fontFamily = br.com.estudario.ui.theme.MonoNum, color = if (used > limit) LateColor else MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1)
                                    Box(Modifier.fillMaxWidth().height(3.dp).clip(RoundedCornerShape(2.dp)).background(MaterialTheme.colorScheme.surfaceVariant)) {
                                        Box(Modifier.fillMaxWidth((used.toFloat() / limit).coerceIn(0f, 1f)).height(3.dp).background(if (used > limit) LateColor else Color(0xFF3BB273)))
                                    }
                                }
                                if (past && tasks.isNotEmpty()) {
                                    // dia que passou: só o resumo, como no site
                                    Text(if (doneCount == tasks.size) "✓ feito" else "$doneCount/${tasks.size}", fontSize = 8.sp, lineHeight = 9.sp, fontWeight = FontWeight.Black, color = if (doneCount == tasks.size) Color(0xFF3BB273) else LateColor)
                                } else {
                                    tasks.take(3).forEach { t ->
                                        val done = t.entity.status == PlanTaskStatus.CONCLUIDA
                                        val color = if (done) DoneColor else taskTypeColor(t.entity.type)
                                        Text(
                                            monthSubject(t.entity.subjectNameSnapshot),
                                            Modifier.fillMaxWidth().clip(RoundedCornerShape(3.dp)).background(color.copy(alpha = if (done) 0.2f else 0.9f)).padding(horizontal = 2.dp),
                                            fontSize = 8.sp, lineHeight = 9.sp, maxLines = 1, overflow = TextOverflow.Clip, fontWeight = FontWeight.Bold,
                                            color = if (done) MaterialTheme.colorScheme.onSurfaceVariant else Color.White,
                                        )
                                    }
                                    if (tasks.size > 3) Text("+${tasks.size - 3}", fontSize = 8.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }
                        }
                    }
                }
            }
            // o dia escolhido: painel de papel com o limite do dia e os estudos (igual ao painel do site)
            selected?.let { day ->
                val list = byDay[day.toEpochDay()].orEmpty().sortedWith(compareBy({ it.entity.status == PlanTaskStatus.CONCLUIDA }, { it.entity.type != PlanTaskType.THEORY }))
                val dayLimit = limitOf(day.toEpochDay())
                val load = list.sumOf { it.entity.plannedMinutes }
                Column(
                    Modifier.fillMaxWidth().padding(top = 6.dp).br_cutPanel().padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Text(dayLabelPtBr(day, state.today).replaceFirstChar { it.uppercase() }, style = MaterialTheme.typography.titleLarge)
                    if (dayLimit == 0) Text("Folga no seu plano.", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    else {
                        Row(Modifier.fillMaxWidth()) {
                            Text("${minutesLabel(load)} de ${minutesLabel(dayLimit)}", Modifier.weight(1f), style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Black)
                            Text(if (load > dayLimit) "passou do limite" else "${minutesLabel(dayLimit - load)} livres", style = MaterialTheme.typography.labelLarge, color = if (load > dayLimit) LateColor else MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Box(Modifier.fillMaxWidth().height(10.dp).clip(RoundedCornerShape(6.dp, 4.dp, 6.dp, 4.dp)).background(MaterialTheme.colorScheme.surfaceVariant).border(1.5.dp, ink, RoundedCornerShape(6.dp, 4.dp, 6.dp, 4.dp))) {
                            Box(Modifier.fillMaxWidth((load.toFloat() / dayLimit.coerceAtLeast(1)).coerceIn(0f, 1f)).height(10.dp).background(if (load > dayLimit) LateColor else Color(0xFF3BB273)))
                        }
                    }
                    Text(
                        if (canEdit) "Segure um estudo e solte num dia do calendário para mudar. Ele fica fixado lá." else "Toque para começar. Mudar de dia é dos planos Essencial e Pro.",
                        style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    if (list.isEmpty()) Text(if (day.toEpochDay() < today) "Nada planejado neste dia." else "Nada planejado ainda.", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    list.forEach { t ->
                        var rowOrigin by remember(t.entity.id) { mutableStateOf(Offset.Zero) }
                        val movable = t.entity.status in pending
                        TaskRow(
                            t, today,
                            Modifier
                                .onGloballyPositioned { rowOrigin = it.positionInRoot() }
                                .then(if (!movable) Modifier else Modifier.pointerInput(t.entity.id, canEdit) {
                                    detectDragGesturesAfterLongPress(
                                        onDragStart = { at ->
                                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                            if (!canEdit) { upgrade = true; return@detectDragGesturesAfterLongPress }
                                            dragging = t; finger = rowOrigin + at
                                        },
                                        onDrag = { change, amount -> if (dragging != null) { change.consume(); finger += amount } },
                                        onDragEnd = {
                                            val to = target
                                            val moving = dragging
                                            dragging = null
                                            if (moving != null && to != null && to != moving.entity.scheduledEpochDay) {
                                                val date = LocalDate.ofEpochDay(to)
                                                if (to < today) return@detectDragGesturesAfterLongPress
                                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                                val dayLoad = byDay[to].orEmpty().sumOf { it.entity.plannedMinutes }
                                                if (dayLoad + moving.entity.plannedMinutes > limitOf(to)) overflow = moving to to
                                                else { onMove(moving, date); selected = date }
                                            }
                                        },
                                        onDragCancel = { dragging = null },
                                    )
                                })
                                .clickable { onFocus(t) },
                        )
                    }
                }
            }
        }
        // o estudo seguindo o dedo
        dragging?.let { t ->
            val local = finger - boardOrigin
            Surface(
                shape = RoundedCornerShape(12.dp), color = taskTypeColor(t.entity.type), shadowElevation = 8.dp,
                modifier = Modifier.offset { IntOffset((local.x - with(density) { 70.dp.toPx() }).roundToInt(), (local.y - with(density) { 46.dp.toPx() }).roundToInt()) }.widthIn(max = 160.dp),
            ) {
                Text(t.entity.topicNameSnapshot ?: t.entity.subjectNameSnapshot, Modifier.padding(horizontal = 10.dp, vertical = 6.dp), color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold, maxLines = 2, overflow = TextOverflow.Ellipsis)
            }
        }
    }
}

@Composable
private fun TaskRow(t: PlannerTaskUi, today: Long, modifier: Modifier) {
    val late = t.entity.scheduledEpochDay < today && t.entity.status in pending
    val done = t.entity.status == PlanTaskStatus.CONCLUIDA
    val color = when { late -> LateColor; done -> DoneColor; else -> taskTypeColor(t.entity.type) }
    val ink = br.com.estudario.ui.brand.cutInk()
    val shape = RoundedCornerShape(12.dp, 7.dp, 13.dp, 8.dp)
    Row(
        modifier.fillMaxWidth().padding(end = 3.dp, bottom = 3.dp)
            .drawBehind { val o = shape.createOutline(size, layoutDirection, this); translate(3.dp.toPx(), 3.dp.toPx()) { drawOutline(o, ink) } }
            .clip(shape).background(MaterialTheme.colorScheme.surface).border(2.dp, ink, shape).padding(12.dp),
        verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Box(Modifier.width(4.dp).height(36.dp).clip(RoundedCornerShape(2.dp)).background(color))
        Column(Modifier.weight(1f)) {
            val kind = when (t.entity.type) {
                PlanTaskType.THEORY -> "Teoria"; PlanTaskType.QUESTIONS -> "Questões"; PlanTaskType.SIMULATION -> "Simulado"
                PlanTaskType.DISCURSIVE -> "Discursiva"; else -> "Revisão"
            }
            Text(
                "$kind · ${minutesLabel(t.entity.plannedMinutes)}" + when { late -> " · atrasado"; done -> " · feito"; t.entity.locked -> " · fixado por você"; else -> "" },
                style = MaterialTheme.typography.labelSmall, color = if (late) LateColor else MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(t.entity.topicNameSnapshot ?: t.entity.subjectNameSnapshot, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, maxLines = 2, overflow = TextOverflow.Ellipsis)
            Text(t.entity.subjectNameSnapshot, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
        if (t.entity.status in pending) Text("⋮⋮", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 16.sp)
    }
}
