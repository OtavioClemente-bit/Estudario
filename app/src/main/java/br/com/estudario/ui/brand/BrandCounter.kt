package br.com.estudario.ui.brand

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * O contador do Estudário (meta do dia, questões por sessão): no lugar do controle deslizante do
 * Android, o número grande num adesivo com o personagem da turma, botões − e + no estilo do app
 * (borda de caneta e degrau embaixo), atalhos em fichas e uma frase que muda com o ritmo escolhido.
 */
@Composable
fun BrandCounter(
    value: Int,
    range: IntRange,
    step: Int,
    unit: String,
    glyph: Glyph,
    onChange: (Int) -> Unit,
    quickValues: List<Int> = emptyList(),
    /** Frase para o valor atual ("ritmo leve", "ritmo firme"...). */
    mood: (Int) -> String? = { null },
) {
    val current = value.coerceIn(range)
    val haptic = LocalHapticFeedback.current
    fun set(next: Int) {
        val clamped = next.coerceIn(range)
        if (clamped != current) { haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove); onChange(clamped) }
    }
    val scheme = MaterialTheme.colorScheme
    Column(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(scheme.surfaceContainerLow)
            .border(1.5.dp, scheme.outlineVariant, RoundedCornerShape(24.dp))
            .padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            StepButton("−", enabled = current > range.first) { set(current - step) }
            Column(Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    BrandIcon(glyph, size = 40.dp)
                    Spacer(Modifier.width(8.dp))
                    AnimatedContent(
                        targetState = current,
                        transitionSpec = {
                            val up = targetState > initialState
                            (slideInVertically { if (up) it else -it } + fadeIn()) togetherWith (slideOutVertically { if (up) -it else it } + fadeOut())
                        },
                        label = "counter",
                    ) { shown ->
                        Text("$shown", style = MaterialTheme.typography.displaySmall, fontWeight = FontWeight.Black, color = scheme.primary)
                    }
                }
                Text(unit, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = scheme.onSurface)
                mood(current)?.let { Text(it, style = MaterialTheme.typography.bodySmall, color = scheme.onSurfaceVariant) }
            }
            StepButton("+", enabled = current < range.last) { set(current + step) }
        }
        if (quickValues.isNotEmpty()) Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            quickValues.forEach { quick ->
                val selected = quick == current
                val bg by animateColorAsState(if (selected) scheme.primary else scheme.surface, label = "chip")
                val ink by animateColorAsState(if (selected) scheme.onPrimary else scheme.onSurface, label = "chipInk")
                val lip = if (selected) scheme.primary.copy(alpha = 0.55f) else scheme.outlineVariant
                Box(
                    Modifier
                        .weight(1f)
                        .height(44.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(lip)
                        .padding(bottom = 3.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(bg)
                        .border(1.5.dp, if (selected) scheme.primary else scheme.outlineVariant, RoundedCornerShape(14.dp))
                        .clickable { set(quick) },
                    contentAlignment = Alignment.Center,
                ) { Text("$quick", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Black, color = ink) }
            }
        }
    }
}

/** Botão − ou + com degrau: afunda ao tocar, como os botões do app. */
@Composable
private fun StepButton(label: String, enabled: Boolean, onClick: () -> Unit) {
    val scheme = MaterialTheme.colorScheme
    val source = remember { MutableInteractionSource() }
    val pressed by source.collectIsPressedAsState()
    val sink by animateFloatAsState(if (pressed) 1f else 0f, spring(stiffness = Spring.StiffnessHigh), label = "sink")
    val face = if (enabled) scheme.primaryContainer else scheme.surfaceVariant
    val ink = if (enabled) scheme.onPrimaryContainer else scheme.onSurfaceVariant.copy(alpha = 0.5f)
    Box(Modifier.size(52.dp, 56.dp)) {
        Box(Modifier.offset(y = 4.dp).size(52.dp).clip(RoundedCornerShape(16.dp)).background(if (enabled) scheme.primary.copy(alpha = 0.45f) else scheme.outlineVariant))
        Box(
            Modifier
                .graphicsLayer { translationY = sink * 4.dp.toPx() }
                .size(52.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(face)
                .border(1.5.dp, if (enabled) scheme.primary.copy(alpha = 0.6f) else scheme.outlineVariant, RoundedCornerShape(16.dp))
                .clickable(interactionSource = source, indication = null, enabled = enabled, onClick = onClick),
            contentAlignment = Alignment.Center,
        ) { Text(label, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Black, color = ink) }
    }
}
