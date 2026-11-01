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
import androidx.compose.material3.TextButton
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
    PlanTaskType.THEORY -> Color(0xFF7C6CF2)
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
internal fun PlanMonthBoard(
    state: ActivePlanUiState,
    onFocus: (PlannerTaskUi) -> Unit,
    onMove: (PlannerTaskUi, LocalDate) -> Unit,
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

    // arrastar: onde está cada dia na tela, o estudo que está sendo levado e o dedo
    val cells = remember { mutableStateMapOf<Long, Rect>() }
    var dragging by remember { mutableStateOf<PlannerTaskUi?>(null) }
    var finger by remember { mutableStateOf(Offset.Zero) }
    var boardOrigin by remember { mutableStateOf(Offset.Zero) }
    val target = dragging?.let { cells.entries.firstOrNull { (_, r) -> r.contains(finger) }?.key }

    Box(Modifier.fillMaxWidth().onGloballyPositioned { boardOrigin = it.positionInRoot() }) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            // cabeçalho do mês com as setas
            Row(verticalAlignment = Alignment.CenterVertically) {
                TextButton(onClick = { month = month.minusMonths(1) }) { Text("‹", fontSize = 22.sp) }
                Text(
                    month.month.getDisplayName(TextStyle.FULL, Locale("pt", "BR")).replaceFirstChar { it.uppercase() } + " de ${month.year}",
                    style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                )
                TextButton(onClick = { month = month.plusMonths(1) }) { Text("›", fontSize = 22.sp) }
            }
            Row(Modifier.fillMaxWidth()) {
                listOf("D", "S", "T", "Q", "Q", "S", "S").forEach {
                    Text(it, Modifier.weight(1f), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                }
            }
            val first = month.atDay(1)
            val lead = first.dayOfWeek.value % 7
            val total = lead + month.lengthOfMonth()
            val weeks = (total + 6) / 7
            for (w in 0 until weeks) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    for (d in 0 until 7) {
                        val index = w * 7 + d - lead
                        if (index < 0 || index >= month.lengthOfMonth()) { Spacer(Modifier.weight(1f)); continue }
                        val date = month.atDay(index + 1)
                        val epoch = date.toEpochDay()
                        val tasks = byDay[epoch].orEmpty()
                        val isTarget = target == epoch
                        val isSelected = selected == date
                        Column(
                            Modifier.weight(1f).aspectRatio(0.62f).clip(RoundedCornerShape(10.dp))
                                .background(
                                    when {
                                        isTarget -> MaterialTheme.colorScheme.primary.copy(alpha = 0.25f)
                                        epoch in offDays -> MaterialTheme.colorScheme.surfaceContainerHighest.copy(alpha = 0.5f)
                                        else -> MaterialTheme.colorScheme.surfaceContainerHigh
                                    },
                                )
                                .border(
                                    if (isSelected || isTarget || epoch == today) 2.dp else 0.dp,
                                    when { isTarget -> MaterialTheme.colorScheme.primary; epoch == today -> Color(0xFFFFC22E); isSelected -> MaterialTheme.colorScheme.primary; else -> Color.Transparent },
                                    RoundedCornerShape(10.dp),
                                )
                                .onGloballyPositioned { cells[epoch] = it.boundsInRoot() }
                                .clickable { selected = date }
                                .padding(3.dp),
                            verticalArrangement = Arrangement.spacedBy(2.dp),
                        ) {
                            Text(
                                "${date.dayOfMonth}", style = MaterialTheme.typography.labelSmall,
                                fontWeight = if (epoch == today) FontWeight.Black else FontWeight.Medium,
                                color = if (epoch < today) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface,
                            )
                            if (epoch in offDays) Text("folga", fontSize = 8.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            tasks.sortedBy { it.entity.sequence }.take(3).forEach { t ->
                                val late = epoch < today && t.entity.status in pending
                                val done = t.entity.status == PlanTaskStatus.CONCLUIDA
                                val color = when { late -> LateColor; done -> DoneColor; else -> taskTypeColor(t.entity.type) }
                                Text(
                                    t.entity.topicNameSnapshot ?: t.entity.subjectNameSnapshot,
                                    Modifier.fillMaxWidth().clip(RoundedCornerShape(4.dp)).background(color.copy(alpha = if (done) 0.25f else 0.85f)).padding(horizontal = 2.dp),
                                    fontSize = 8.sp, lineHeight = 9.sp, maxLines = 1, overflow = TextOverflow.Clip,
                                    color = if (done) MaterialTheme.colorScheme.onSurfaceVariant else Color.White,
                                )
                            }
                            if (tasks.size > 3) Text("+${tasks.size - 3}", fontSize = 8.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }
            // legenda
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
                listOf("Teoria" to taskTypeColor(PlanTaskType.THEORY), "Questões" to taskTypeColor(PlanTaskType.QUESTIONS), "Revisão" to taskTypeColor(PlanTaskType.REVIEW), "Atrasado" to LateColor).forEach { (label, c) ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(Modifier.size(8.dp).clip(CircleShape).background(c))
                        Spacer(Modifier.width(4.dp))
                        Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
            // o dia escolhido
            selected?.let { day ->
                val list = byDay[day.toEpochDay()].orEmpty().sortedBy { it.entity.sequence }
                Text(dayLabelPtBr(day, state.today).replaceFirstChar { it.uppercase() }, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                Text(
                    if (canEdit) "Segure um estudo e solte num dia do calendário para mudar." else "Toque para começar. Segure para mudar de dia.",
                    style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                if (list.isEmpty()) Text(if (day.toEpochDay() in offDays) "Dia sem estudo." else "Nada planejado neste dia.", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
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
                                            onMove(moving, date)
                                            selected = date
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
    Row(
        modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp)).background(MaterialTheme.colorScheme.surfaceContainerHigh).padding(12.dp),
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
