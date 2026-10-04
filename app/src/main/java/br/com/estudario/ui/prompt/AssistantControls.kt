package br.com.estudario.ui.prompt

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.RadioButtonUnchecked
import androidx.compose.material3.FilterChip
import br.com.estudario.ui.brand.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import br.com.estudario.ui.brand.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import br.com.estudario.EstudarioApplication
import br.com.estudario.data.prompt.GenerationLimits
import br.com.estudario.data.prompt.QuestionDifficulty
import br.com.estudario.ui.plans.AiPlanLoadResult
import br.com.estudario.ui.plans.planDisplayName
import br.com.estudario.ui.theme.EstudarioShapes
import br.com.estudario.ui.theme.estudarioColors
import kotlin.math.roundToInt

/** Cartão de escolha: título, descrição opcional e marcação clara do selecionado. */
@Composable
fun ChoiceCard(
    title: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    description: String? = null,
    icon: ImageVector? = null,
    accent: Color = MaterialTheme.colorScheme.primary,
    multi: Boolean = false,
) {
    val border by animateColorAsState(if (selected) accent else MaterialTheme.colorScheme.outlineVariant, label = "choice-border")
    val container by animateColorAsState(
        if (selected) accent.copy(alpha = 0.10f) else MaterialTheme.colorScheme.surface,
        label = "choice-container",
    )
    Surface(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = EstudarioShapes.row,
        color = container,
        border = BorderStroke(if (selected) 2.dp else 1.dp, border),
    ) {
        Row(Modifier.padding(horizontal = 14.dp, vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) {
            if (icon != null) {
                Box(Modifier.size(36.dp), contentAlignment = Alignment.Center) { Icon(icon, null, Modifier.size(32.dp), tint = accent) }
                Spacer(Modifier.width(12.dp))
            }
            Column(Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                if (description != null) Text(description, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Spacer(Modifier.width(8.dp))
            Icon(
                if (selected) Icons.Outlined.CheckCircle else Icons.Outlined.RadioButtonUnchecked,
                if (selected) "Selecionado" else null,
                tint = if (selected) accent else MaterialTheme.colorScheme.outline,
                modifier = Modifier.size(if (multi) 22.dp else 20.dp),
            )
        }
    }
}

/** Lista de cartões com uma única escolha. */
@Composable
fun <T> ChoiceCards(
    options: List<T>,
    selected: T,
    title: (T) -> String,
    onSelect: (T) -> Unit,
    description: (T) -> String? = { null },
    icon: (T) -> ImageVector? = { null },
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        options.forEach { option ->
            ChoiceCard(title(option), option == selected, { onSelect(option) }, description = description(option), icon = icon(option))
        }
    }
}

/** Lista de cartões com várias escolhas. */
@Composable
fun <T> MultiChoiceCards(
    options: List<T>,
    selected: Set<T>,
    title: (T) -> String,
    onChange: (Set<T>) -> Unit,
    description: (T) -> String? = { null },
    icon: (T) -> ImageVector? = { null },
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        options.forEach { option ->
            val isSelected = option in selected
            ChoiceCard(
                title(option), isSelected, { onChange(if (isSelected) selected - option else selected + option) },
                description = description(option), icon = icon(option), multi = true,
            )
        }
    }
}

/** Valor grande no centro, régua para arrastar e atalhos com os valores mais usados. */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BigValueSlider(
    value: Int,
    range: IntRange,
    step: Int,
    format: (Int) -> String,
    onChange: (Int) -> Unit,
    caption: String? = null,
    quickValues: List<Int> = emptyList(),
) {
    val current = value.coerceIn(range)
    Surface(shape = EstudarioShapes.panel, color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f), contentColor = MaterialTheme.colorScheme.onSurface) {
        Column(Modifier.fillMaxWidth().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(format(current), style = MaterialTheme.typography.displaySmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            if (caption != null) Text(caption, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center)
            Slider(
                value = current.toFloat(),
                onValueChange = { raw -> onChange(((raw / step).roundToInt() * step).coerceIn(range)) },
                valueRange = range.first.toFloat()..range.last.toFloat(),
                steps = ((range.last - range.first) / step - 1).coerceAtLeast(0),
                colors = SliderDefaults.colors(
                    inactiveTickColor = Color.Transparent,
                    activeTickColor = Color.Transparent,
                    inactiveTrackColor = MaterialTheme.colorScheme.outlineVariant,
                ),
                modifier = Modifier.fillMaxWidth(),
            )
            Row(Modifier.fillMaxWidth()) {
                Text(format(range.first), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(Modifier.weight(1f))
                Text(format(range.last), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            val quick = quickValues.filter { it in range }
            if (quick.isNotEmpty()) FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally), modifier = Modifier.fillMaxWidth()) {
                quick.forEach { option -> FilterChip(selected = option == current, onClick = { onChange(option) }, label = { Text(format(option)) }) }
            }
        }
    }
}

/**
 * Limite de questões por geração do plano da conta (Grátis 10, Essencial 20, Pro 30). Enquanto
 * carrega, ou sem conta, vale o limite do Grátis: o servidor é quem aplica o limite de verdade.
 */
data class QuestionBatchLimit(val max: Int, val planName: String)

@Composable
fun rememberQuestionBatchLimit(): QuestionBatchLimit {
    val context = LocalContext.current
    var limit by remember { mutableStateOf(QuestionBatchLimit(GenerationLimits.FREE_MAX_QUESTIONS, "Grátis")) }
    LaunchedEffect(Unit) {
        val app = context.applicationContext as? EstudarioApplication ?: return@LaunchedEffect
        val summary = (app.aiPlanRepository.load() as? AiPlanLoadResult.Available)?.summary ?: return@LaunchedEffect
        val perRequest = summary.plans.firstOrNull { it.planTier == summary.planTier }
            ?.limits?.firstOrNull { it.feature == "QUESTION_BATCH" }?.maxPerRequest
        if (perRequest != null && perRequest >= GenerationLimits.MIN_QUESTIONS) {
            limit = QuestionBatchLimit(perRequest, planDisplayName(summary.planTier))
        }
    }
    return limit
}

@Composable
fun QuestionCountSelector(value: Int, limit: QuestionBatchLimit, perTopic: Boolean, onChange: (Int) -> Unit) {
    val current = value.coerceIn(GenerationLimits.MIN_QUESTIONS, limit.max)
    LaunchedEffect(value, limit.max) { if (value != current) onChange(current) }
    BigValueSlider(
        value = current,
        range = GenerationLimits.MIN_QUESTIONS..limit.max,
        step = 1,
        format = { "$it" },
        onChange = onChange,
        caption = (if (perTopic) "questões por tópico" else "questões") + " · plano ${limit.planName}: até ${limit.max}",
        quickValues = listOf(5, 10, 15, 20, 30),
    )
}

/** Dificuldade em cartões lado a lado, com a cor de cada nível. */
@Composable
fun DifficultySelector(selected: QuestionDifficulty, onSelect: (QuestionDifficulty) -> Unit) {
    val colors = estudarioColors()
    fun accent(level: QuestionDifficulty): Color = when (level) {
        QuestionDifficulty.EASY -> colors.completed
        QuestionDifficulty.MEDIUM -> colors.attention
        QuestionDifficulty.HARD -> Color(0xFFC0392B)
        QuestionDifficulty.MIXED -> colors.current
    }
    fun hint(level: QuestionDifficulty): String = when (level) {
        QuestionDifficulty.EASY -> "Fixar o básico"
        QuestionDifficulty.MEDIUM -> "Nível da maioria das provas"
        QuestionDifficulty.HARD -> "Pegadinhas e detalhes"
        QuestionDifficulty.MIXED -> "Um pouco de cada"
    }
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        listOf(QuestionDifficulty.EASY, QuestionDifficulty.MEDIUM, QuestionDifficulty.HARD, QuestionDifficulty.MIXED).chunked(2).forEach { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                row.forEach { level ->
                    val isSelected = level == selected
                    val tone = accent(level)
                    Surface(
                        onClick = { onSelect(level) },
                        modifier = Modifier.weight(1f),
                        shape = EstudarioShapes.row,
                        color = if (isSelected) tone.copy(alpha = 0.12f) else MaterialTheme.colorScheme.surface,
                        border = BorderStroke(if (isSelected) 2.dp else 1.dp, if (isSelected) tone else MaterialTheme.colorScheme.outlineVariant),
                    ) {
                        Column(Modifier.padding(12.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(Modifier.size(10.dp).clip(CircleShape).background(tone))
                                Spacer(Modifier.width(8.dp))
                                Text(level.label, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                            }
                            Text(hint(level), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }
        }
    }
}

private val weekLabels = listOf("Seg", "Ter", "Qua", "Qui", "Sex", "Sáb", "Dom")

/**
 * Semana em barras: arraste cada barra para cima ou para baixo para definir o tempo do dia (em
 * passos de 15 minutos, até [maxMinutes]); um toque define direto na altura tocada.
 */
@Composable
fun WeekHoursPicker(minutes: List<Int>, onChange: (day: Int, minutes: Int) -> Unit, maxMinutes: Int = 480) {
    val colors = estudarioColors()
    Surface(shape = EstudarioShapes.panel, color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f), contentColor = MaterialTheme.colorScheme.onSurface) {
        Column(Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 14.dp)) {
            Row(Modifier.fillMaxWidth().height(200.dp), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                minutes.forEachIndexed { day, value ->
                    DayBar(
                        label = weekLabels[day],
                        value = value,
                        maxMinutes = maxMinutes,
                        color = if (day >= 5) colors.subjectPalette[6] else MaterialTheme.colorScheme.primary,
                        onChange = { onChange(day, it) },
                        modifier = Modifier.weight(1f).fillMaxHeight(),
                    )
                }
            }
            Spacer(Modifier.height(8.dp))
            Text(
                "Total: ${durationText(minutes.sum())} por semana",
                Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
private fun DayBar(label: String, value: Int, maxMinutes: Int, color: Color, onChange: (Int) -> Unit, modifier: Modifier) {
    val latestValue by rememberUpdatedState(value)
    val latestOnChange by rememberUpdatedState(onChange)
    Column(modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            if (value == 0) "folga" else durationText(value),
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.SemiBold,
            color = if (value == 0) MaterialTheme.colorScheme.onSurfaceVariant else color,
            maxLines = 1,
        )
        Spacer(Modifier.height(4.dp))
        BoxWithConstraints(
            Modifier
                .weight(1f)
                .fillMaxWidth()
                .clip(EstudarioShapes.compact)
                .background(MaterialTheme.colorScheme.surface)
                .border(1.dp, MaterialTheme.colorScheme.outlineVariant, EstudarioShapes.compact)
                .semantics { contentDescription = "$label: ${if (value == 0) "folga" else durationText(value)}. Arraste para ajustar." },
            contentAlignment = Alignment.BottomCenter,
        ) {
            val heightPx = constraints.maxHeight.toFloat().coerceAtLeast(1f)
            fun minutesAt(y: Float): Int {
                val fraction = (1f - y / heightPx).coerceIn(0f, 1f)
                return ((fraction * maxMinutes) / 15f).roundToInt() * 15
            }
            val fill by animateDpAsState(maxHeight * (value.toFloat() / maxMinutes).coerceIn(0f, 1f), label = "day-bar")
            Box(
                Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .pointerInput(maxMinutes) { detectTapGestures { offset -> latestOnChange(minutesAt(offset.y)) } }
                    .pointerInput(maxMinutes) {
                        detectDragGestures { change, _ ->
                            change.consume()
                            val next = minutesAt(change.position.y)
                            if (next != latestValue) latestOnChange(next)
                        }
                    },
            )
            Box(Modifier.fillMaxWidth().height(fill).clip(EstudarioShapes.compact).background(color))
        }
        Spacer(Modifier.height(6.dp))
        Text(label, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold)
    }
}

fun durationText(minutes: Int): String = when {
    minutes < 60 -> "$minutes min"
    minutes % 60 == 0 -> "${minutes / 60}h"
    else -> "${minutes / 60}h${"%02d".format(minutes % 60)}"
}
