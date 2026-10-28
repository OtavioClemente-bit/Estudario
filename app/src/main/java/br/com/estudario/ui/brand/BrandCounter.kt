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
    // O contador do estúdio (o mesmo do site): número grande, − e + redondos, régua e atalhos, num
    // painel escuro de console, em qualquer tela que use o contador.
    br.com.estudario.ui.studio.GxPanel {
        Box(Modifier.padding(3.dp)) {
            br.com.estudario.ui.studio.GxCounter(value, onChange, range, step = step, unit = unit, quick = quickValues, mood = mood)
        }
    }
}

/** Botão − ou + com degrau: afunda ao tocar, como os botões do app. */
@Suppress("unused")
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
