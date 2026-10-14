package br.com.estudario.ui.planner

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import br.com.estudario.domain.planner.PlanTaskStatus
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.ChronoUnit

private val Ink = Color(0xFF2E2799)
private val InkDeep = Color(0xFF1B1464)
private val Gold = Color(0xFFF5B83D)
private val Mint = Color(0xFF7EE0B8)
private val Paper = Color(0xFFFFFBEF)
private val Tape = Color(0x99F5E6B8)
private val PaperInk = Color(0xFF26215C)
private val weekdayInitials = listOf("S", "T", "Q", "Q", "S", "S", "D")

/**
 * O plano em uma olhada, com a cara do Estudário: o cartão de tinta índigo com a pauta do caderno,
 * o objetivo inteiro, os dias até a prova num ingresso dourado, o quanto do plano já foi num anel, a
 * semana em bolinhas (feita, marcada, folga, hoje) e a missão de hoje num bilhete colado com fita.
 */
@Composable
fun PlanProgressHeader(state: ActivePlanUiState, modifier: Modifier = Modifier) {
    val plan = state.activePlan
    val load = state.tasks.plannedLoadTasks()
    val total = load.size
    val done = load.count { it.entity.status == PlanTaskStatus.CONCLUIDA }
    val percent = if (total == 0) 0 else (done * 100 / total).coerceIn(0, 100)
    val today = state.todayTasks.plannedLoadTasks()
    val todayDone = today.count { it.entity.status == PlanTaskStatus.CONCLUIDA }
    val daysToExam = plan?.examEpochDay?.let(LocalDate::ofEpochDay)?.let { ChronoUnit.DAYS.between(state.today, it) }?.takeIf { it >= 0 }
    val title = plan?.objective?.takeIf { it.isNotBlank() } ?: plan?.name ?: "Plano de estudos"
    val white = Color.White

    Box(modifier.fillMaxWidth()) {
        Column(
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(28.dp))
                .background(Brush.linearGradient(listOf(Ink, InkDeep)))
                .drawBehind {
                    val step = 26.dp.toPx()
                    var y = step
                    while (y < size.height) { drawLine(white.copy(alpha = 0.06f), Offset(0f, y), Offset(size.width, y), 1.dp.toPx()); y += step }
                    val margin = 14.dp.toPx()
                    drawLine(Color(0xFFE7A9A3).copy(alpha = 0.45f), Offset(margin, 0f), Offset(margin, size.height), 1.5.dp.toPx())
                }
                .padding(start = 28.dp, end = 20.dp, top = 20.dp, bottom = 22.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("SEU PLANO", Modifier.weight(1f), style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Black, color = Gold)
                if (daysToExam != null) Row(
                    Modifier.clip(RoundedCornerShape(10.dp)).background(Gold).padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text("$daysToExam", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black, color = InkDeep)
                    Spacer(Modifier.width(6.dp))
                    Text(if (daysToExam == 1L) "dia para\na prova" else "dias para\na prova", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = InkDeep)
                }
            }
            Text(title, style = if (title.length > 60) MaterialTheme.typography.titleLarge else MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Black, color = white)
            Row(verticalAlignment = Alignment.CenterVertically) {
                PlanRing(percent)
                Spacer(Modifier.width(16.dp))
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text("do plano cumprido", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = white)
                    Text("$done de $total atividades", style = MaterialTheme.typography.bodySmall, color = white.copy(alpha = 0.75f))
                    Text(
                        state.forecastDate?.let { "Edital visto até ${forecastDateLabelPtBr(it)}" } ?: "Calculando a previsão…",
                        style = MaterialTheme.typography.bodySmall,
                        color = Mint,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
            }
            WeekDots(state)
            Spacer(Modifier.height(54.dp)) // o bilhete de hoje fica por cima do fim do cartão
        }
        TodayNote(
            text = when {
                today.isEmpty() && state.nextStudyDay != null -> "Folga hoje. Próximo estudo ${dayLabelPtBr(state.nextStudyDay!!, state.today)}: ${state.nextDayTasks.size} atividade(s), ${minutesLabelPtBr(state.nextDayTasks.sumOf { it.entity.plannedMinutes })}."
                today.isEmpty() -> "Dia livre no plano."
                todayDone == today.size -> "Missão cumprida! Amanhã o plano continua."
                else -> "$todayDone de ${today.size} feitas · ${minutesLabelPtBr(state.todayActualMinutes)} de ${minutesLabelPtBr(state.todayPlannedMinutes)}"
            },
            overdue = state.overdueTasks.size,
            modifier = Modifier.align(Alignment.BottomCenter).padding(horizontal = 18.dp).offset(y = 14.dp),
        )
    }
    Spacer(Modifier.height(18.dp))
}

/** O anel do plano cumprido: trilha clara, arco dourado animado e a porcentagem no meio. */
@Composable
private fun PlanRing(percent: Int) {
    val shown by animateFloatAsState((percent / 100f).coerceIn(0f, 1f), tween(1100), label = "planRing")
    Box(Modifier.size(76.dp), contentAlignment = Alignment.Center) {
        Canvas(Modifier.size(76.dp)) {
            val stroke = 9.dp.toPx()
            val inset = stroke / 2
            val arc = Size(size.width - stroke, size.height - stroke)
            drawArc(Color.White.copy(alpha = 0.16f), 0f, 360f, false, Offset(inset, inset), arc, style = Stroke(stroke))
            drawArc(Gold, -90f, 360f * shown, false, Offset(inset, inset), arc, style = Stroke(stroke, cap = StrokeCap.Round))
        }
        Text("$percent%", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black, color = Color.White)
    }
}

/** A semana em bolinhas: cheia quando o dia foi feito, contornada quando tem estudo, apagada na folga. */
@Composable
private fun WeekDots(state: ActivePlanUiState) {
    val monday = state.today.with(DayOfWeek.MONDAY)
    val load = state.tasks.plannedLoadTasks()
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        (0 until 7).forEach { offset ->
            val day = monday.plusDays(offset.toLong())
            val dayTasks = load.filter { it.entity.scheduledEpochDay == day.toEpochDay() }
            val complete = dayTasks.isNotEmpty() && dayTasks.all { it.entity.status == PlanTaskStatus.CONCLUIDA }
            val isToday = day == state.today
            val missed = day < state.today && dayTasks.isNotEmpty() && !complete
            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(weekdayInitials[offset], style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = if (isToday) Gold else Color.White.copy(alpha = 0.65f))
                val fill = when {
                    complete -> Mint
                    isToday -> Gold.copy(alpha = 0.25f)
                    else -> Color.Transparent
                }
                val ring = when {
                    complete -> Mint
                    isToday -> Gold
                    missed -> Color(0xFFFF8A80)
                    dayTasks.isNotEmpty() -> Color.White.copy(alpha = 0.55f)
                    else -> Color.White.copy(alpha = 0.15f)
                }
                Box(
                    Modifier.size(30.dp).clip(CircleShape).background(fill).border(2.dp, ring, CircleShape),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        if (complete) "✓" else "${day.dayOfMonth}",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Black,
                        color = if (complete) InkDeep else Color.White.copy(alpha = if (dayTasks.isEmpty()) 0.45f else 0.95f),
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
    }
}

/** A missão de hoje num bilhete de papel colado com fita, levemente torto. */
@Composable
private fun TodayNote(text: String, overdue: Int, modifier: Modifier = Modifier) {
    Box(modifier.rotate(-1.2f)) {
        Row(
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(6.dp))
                .background(Paper)
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            br.com.estudario.ui.brand.BrandIcon(br.com.estudario.ui.brand.Glyph.Target, size = 34.dp)
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text("Missão de hoje", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Black, color = PaperInk)
                Text(text, style = MaterialTheme.typography.bodySmall, color = PaperInk.copy(alpha = 0.75f))
            }
            if (overdue > 0) Box(Modifier.clip(RoundedCornerShape(50)).background(Color(0xFFC0262D)).padding(horizontal = 10.dp, vertical = 4.dp)) {
                Text("$overdue atrasada${if (overdue > 1) "s" else ""}", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
        // A fita adesiva no alto do bilhete.
        Box(Modifier.align(Alignment.TopCenter).offset(y = (-8).dp).size(64.dp, 16.dp).rotate(2f).background(Tape))
    }
}
