package br.com.estudario.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.estudario.domain.DailyActivity
import br.com.estudario.domain.DailyGoal
import br.com.estudario.domain.StreakEngine
import java.time.LocalDate

private val CELL = 15.dp
private val GAP = 4.dp
private val MONTH_ROW = 18.dp
private val LABELS_WIDTH = 34.dp
private val MESES = listOf("jan", "fev", "mar", "abr", "mai", "jun", "jul", "ago", "set", "out", "nov", "dez")
private val SEMANA = listOf("seg", "", "qua", "", "sex", "", "dom")

/**
 * Mapa de frequência no estilo do GitHub: uma coluna por semana, uma linha por dia da semana.
 * O nome do mês pode transbordar a largura da coluna (é assim que ele cabe), a grade rola na
 * horizontal e já abre no dia de hoje. Tocar em um dia mostra o detalhe embaixo.
 */
@Composable
fun ActivityHeatmap(
    calendar: List<DailyActivity>,
    goal: DailyGoal,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(horizontal = 16.dp),
) {
    if (calendar.isEmpty()) return
    val weeks = remember(calendar) { calendar.chunked(7) }
    var selected by remember { mutableStateOf<DailyActivity?>(null) }
    val scrollState = rememberScrollState()
    var settled by remember(weeks.size) { mutableStateOf(false) }
    LaunchedEffect(weeks.size, scrollState.maxValue) {
        if (!settled && scrollState.maxValue > 0) { scrollState.scrollTo(scrollState.maxValue); settled = true }
    }

    Column(modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(Modifier.fillMaxWidth()) {
            // Coluna fixa dos dias da semana: não rola junto com a grade.
            Column(
                Modifier.padding(start = 16.dp).width(LABELS_WIDTH),
                verticalArrangement = Arrangement.spacedBy(GAP),
            ) {
                Spacer(Modifier.height(MONTH_ROW))
                SEMANA.forEach { label ->
                    Box(Modifier.height(CELL), contentAlignment = Alignment.CenterStart) {
                        if (label.isNotEmpty()) {
                            Text(
                                label,
                                fontSize = 10.sp,
                                lineHeight = 11.sp,
                                maxLines = 1,
                                softWrap = false,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                }
            }
            Row(
                Modifier.horizontalScroll(scrollState).padding(contentPadding),
                horizontalArrangement = Arrangement.spacedBy(GAP),
            ) {
                weeks.forEachIndexed { index, week ->
                    val month = week.first().date.monthValue
                    val previousMonth = weeks.getOrNull(index - 1)?.first()?.date?.monthValue
                    Column(verticalArrangement = Arrangement.spacedBy(GAP)) {
                        Box(Modifier.height(MONTH_ROW).width(CELL), contentAlignment = Alignment.CenterStart) {
                            if (previousMonth != month) {
                                Text(
                                    MESES[month - 1],
                                    // Deixa o nome do mês passar da largura da coluna em vez de cortar.
                                    modifier = Modifier.wrapContentWidth(Alignment.Start, unbounded = true),
                                    fontSize = 10.sp,
                                    lineHeight = 11.sp,
                                    maxLines = 1,
                                    softWrap = false,
                                    overflow = TextOverflow.Visible,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                            }
                        }
                        week.forEach { day ->
                            HeatCell(day, goal, day.date == selected?.date) { selected = if (selected?.date == day.date) null else day }
                        }
                        // A semana atual costuma estar incompleta: completa a coluna para a grade não subir.
                        repeat(7 - week.size) { Spacer(Modifier.size(CELL)) }
                    }
                }
            }
        }

        Row(
            Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp),
        ) {
            Text("Menos", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            (0..4).forEach { level -> Box(Modifier.size(11.dp).clip(RoundedCornerShape(3.dp)).background(levelColor(level))) }
            Text("Mais", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }

        val current = selected
        Surface(
            Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            shape = RoundedCornerShape(10.dp),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
        ) {
            Text(
                if (current == null) "Toque em um dia para ver o que você fez nele." else describe(current),
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 9.dp),
                style = MaterialTheme.typography.bodySmall,
                color = if (current == null) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface,
                fontWeight = if (current == null) FontWeight.Normal else FontWeight.SemiBold,
            )
        }
    }
}

@Composable
private fun HeatCell(day: DailyActivity, goal: DailyGoal, selected: Boolean, onClick: () -> Unit) {
    val today = day.date == LocalDate.now()
    val shape = RoundedCornerShape(4.dp)
    Box(
        Modifier
            .size(CELL)
            .clip(shape)
            .background(levelColor(StreakEngine.level(day, goal)))
            .then(
                when {
                    selected -> Modifier.border(2.dp, MaterialTheme.colorScheme.onSurface, shape)
                    today -> Modifier.border(2.dp, MaterialTheme.colorScheme.primary, shape)
                    else -> Modifier
                },
            )
            .clickable(onClick = onClick),
    )
}

@Composable
private fun levelColor(level: Int): Color {
    val base = MaterialTheme.colorScheme.secondary
    return when (level) {
        1 -> base.copy(alpha = 0.3f)
        2 -> base.copy(alpha = 0.52f)
        3 -> base.copy(alpha = 0.76f)
        4 -> base
        else -> MaterialTheme.colorScheme.surfaceVariant
    }
}

private fun describe(day: DailyActivity): String {
    val parts = buildList {
        if (day.questions > 0) add("${day.questions} questões (${day.correct} certas)")
        if (day.planTasks > 0) add("${day.planTasks} tarefa(s) do plano")
        if (day.reviews > 0) add("${day.reviews} revisão(ões)")
        if (day.minutes > 0) add("${day.minutes} min")
    }
    val date = "%02d/%02d".format(day.date.dayOfMonth, day.date.monthValue)
    return if (parts.isEmpty()) "$date — sem estudo registrado" else "$date — ${parts.joinToString(" • ")}"
}
