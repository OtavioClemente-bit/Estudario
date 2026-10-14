package br.com.estudario.ui.setup

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import br.com.estudario.domain.setup.formatAvailabilityMinutes
import kotlin.math.roundToInt

/**
 * Rotina da semana e tamanho do bloco, na mesma língua dos medidores de matéria ([LevelMeter]):
 * barras arredondadas que enchem com a cor do eixo, o valor escrito numa pílula ao lado e nada que
 * role para o lado ou corte em celular estreito. O site usa os mesmos desenhos (SetupWizard.kt).
 */
internal val RoutineIndigo = Color(0xFF5B4BF5)
internal val RoutineWeekend = Color(0xFF9563FF)
internal val RoutineBlock = Color(0xFFFF7A45)

private val dayShort = listOf("Seg", "Ter", "Qua", "Qui", "Sex", "Sáb", "Dom")
private val dayLong = listOf("segunda", "terça", "quarta", "quinta", "sexta", "sábado", "domingo")

/** Pílula com o valor atual: o mesmo destaque do nível escolhido nos medidores. */
@Composable
internal fun ValuePill(text: String, color: Color, modifier: Modifier = Modifier) {
    Text(
        text,
        style = MaterialTheme.typography.labelLarge,
        fontWeight = FontWeight.Bold,
        color = color,
        textAlign = TextAlign.Center,
        maxLines = 1,
        modifier = modifier
            .background(color.copy(alpha = 0.14f), RoundedCornerShape(50))
            .padding(horizontal = 12.dp, vertical = 4.dp),
    )
}

/** Botão redondo de − / +, pequeno e com toque generoso. */
@Composable
private fun StepButton(symbol: String, description: String, color: Color, enabled: Boolean, onClick: () -> Unit) {
    Box(
        Modifier
            .size(36.dp)
            .clip(CircleShape)
            .background(if (enabled) color.copy(alpha = 0.14f) else MaterialTheme.colorScheme.surfaceVariant)
            .clickable(enabled = enabled, role = Role.Button, onClick = onClick)
            .semantics { contentDescription = description },
        contentAlignment = Alignment.Center,
    ) {
        Text(symbol, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = if (enabled) color else MaterialTheme.colorScheme.outline)
    }
}

/** Atalhos em células de largura igual (nunca cortam): "1h · 2h · 3h · 4h de seg a sex". */
@Composable
private fun PresetCells(options: List<Pair<String, Boolean>>, color: Color, onPick: (Int) -> Unit) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        options.forEachIndexed { i, (label, on) ->
            val shape = RoundedCornerShape(10.dp)
            val bg by animateColorAsState(if (on) color else MaterialTheme.colorScheme.surfaceVariant, label = "atalho")
            Box(
                Modifier
                    .weight(1f)
                    .height(38.dp)
                    .clip(shape)
                    .background(bg)
                    .clickable(role = Role.RadioButton) { onPick(i) }
                    .semantics { this.selected = on },
                contentAlignment = Alignment.Center,
            ) {
                Text(label, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold, color = if (on) Color.White else MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

/**
 * Horas de cada dia: uma linha por dia com a barra que enche conforme o tempo (toque na barra para
 * marcar direto ali), a pílula com o valor e − / + de 15 em 15 minutos. Zero é folga.
 */
@Composable
internal fun RoutineWeek(minutes: List<Int>, onChange: (day: Int, minutes: Int) -> Unit, maxMinutes: Int = 720) {
    val visualMax = 480f // a barra enche em 8 h; acima disso ela fica cheia e a pílula diz o valor
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        val weekday = (0..4).map { minutes.getOrElse(it) { 0 } }
        val presets = listOf(60, 120, 180, 240)
        Text("Atalho: o mesmo tempo de segunda a sexta", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        PresetCells(presets.map { m -> "${m / 60}h" to weekday.all { it == m } }, RoutineIndigo) { i -> (0..4).forEach { day -> onChange(day, presets[i]) } }
        minutes.forEachIndexed { day, value ->
            val color = if (day >= 5) RoutineWeekend else RoutineIndigo
            val latest by rememberUpdatedState(value)
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(dayShort[day], style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, modifier = Modifier.width(36.dp))
                val fill by animateFloatAsState((value / visualMax).coerceIn(0f, 1f), label = "dia")
                Box(
                    Modifier
                        .weight(1f)
                        .height(14.dp)
                        .clip(RoundedCornerShape(50))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .pointerInput(day) {
                            detectTapGestures { offset ->
                                val raw = offset.x / size.width * visualMax
                                onChange(day, ((raw / 15f).roundToInt() * 15).coerceIn(0, maxMinutes))
                            }
                        }
                        .semantics { contentDescription = "Tempo de ${dayLong[day]}: ${if (latest == 0) "folga" else formatAvailabilityMinutes(latest)}" },
                ) {
                    Box(Modifier.fillMaxHeight().fillMaxWidth(fill).clip(RoundedCornerShape(50)).background(color))
                }
                ValuePill(if (value == 0) "Folga" else formatAvailabilityMinutes(value), if (value == 0) MaterialTheme.colorScheme.outline else color, Modifier.widthIn(min = 72.dp))
                StepButton("−", "Menos 15 minutos na ${dayLong[day]}", color, value > 0) { onChange(day, (value - 15).coerceAtLeast(0)) }
                StepButton("+", "Mais 15 minutos na ${dayLong[day]}", color, value < maxMinutes) { onChange(day, (value + 15).coerceAtMost(maxMinutes)) }
            }
        }
        Row(Modifier.fillMaxWidth().padding(top = 4.dp), verticalAlignment = Alignment.CenterVertically) {
            Text("Total na semana", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.weight(1f))
            ValuePill(formatAvailabilityMinutes(minutes.sum()), RoutineIndigo)
        }
    }
}

/**
 * Tamanho de cada bloco de estudo: cinco tamanhos comuns em barrinhas crescentes (como os medidores)
 * e − / + de 5 em 5 minutos para quem quer outro valor.
 */
@Composable
internal fun BlockSizePicker(value: Int, onChange: (Int) -> Unit, range: IntRange = 15..120) {
    val presets = listOf(25, 45, 50, 60, 90)
    Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Cada tarefa do plano dura", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.weight(1f))
            StepButton("−", "Menos 5 minutos no bloco", RoutineBlock, value > range.first) { onChange((value - 5).coerceAtLeast(range.first)) }
            ValuePill("$value min", RoutineBlock, Modifier.widthIn(min = 76.dp))
            StepButton("+", "Mais 5 minutos no bloco", RoutineBlock, value < range.last) { onChange((value + 5).coerceAtMost(range.last)) }
        }
        Row(Modifier.fillMaxWidth().height(44.dp), horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.Bottom) {
            presets.forEachIndexed { i, minutes ->
                val on = minutes == value
                val reached = minutes <= value
                val shape = RoundedCornerShape(8.dp)
                val bg by animateColorAsState(if (reached) RoutineBlock else MaterialTheme.colorScheme.surfaceVariant, label = "bloco")
                Box(
                    Modifier
                        .weight(1f)
                        .height((22 + i * 5).dp)
                        .clip(shape)
                        .background(bg)
                        .then(if (on) Modifier.border(2.dp, RoutineBlock.copy(alpha = 0.55f), shape) else Modifier)
                        .clickable(role = Role.RadioButton) { onChange(minutes) }
                        .semantics { contentDescription = "Bloco de $minutes minutos"; this.selected = on },
                    contentAlignment = Alignment.Center,
                ) {
                    Text("$minutes", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = if (reached) Color.White else MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
        Text(
            "Num dia de 2h com blocos de $value min, cabem ${(120 / value.coerceAtLeast(1)).coerceAtLeast(1)} tarefa(s). É o tamanho-base, não o total do dia.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}
