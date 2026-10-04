package br.com.estudario.ui.brand

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SliderColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.setProgress
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

/**
 * O controle de arrastar do Estudário: trilha grossa, preenchimento na cor da marca e um botão
 * com espessura que cresce enquanto a pessoa segura. Toque em qualquer ponto da trilha também
 * leva o valor até ali. Mesma assinatura do Slider do Material.
 */
@Composable
fun Slider(
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    steps: Int = 0,
    onValueChangeFinished: (() -> Unit)? = null,
    @Suppress("UNUSED_PARAMETER") colors: SliderColors? = null,
    @Suppress("UNUSED_PARAMETER") interactionSource: MutableInteractionSource? = null,
) {
    val scheme = MaterialTheme.colorScheme
    val fill = scheme.primary
    val track = scheme.surfaceVariant
    val change by rememberUpdatedState(onValueChange)
    val finished by rememberUpdatedState(onValueChangeFinished)
    var held by remember { mutableStateOf(false) }
    val grow by animateFloatAsState(if (held) 1.18f else 1f, label = "slider-grow")
    val span = (valueRange.endInclusive - valueRange.start).takeIf { it > 0f } ?: 1f

    fun snap(fraction: Float): Float {
        val f = fraction.coerceIn(0f, 1f)
        val snapped = if (steps > 0) (f * (steps + 1)).roundToInt() / (steps + 1f) else f
        return valueRange.start + snapped * span
    }

    val fraction = ((value - valueRange.start) / span).coerceIn(0f, 1f)
    Canvas(
        modifier
            .fillMaxWidth()
            .height(44.dp)
            .alpha(if (enabled) 1f else 0.45f)
            .semantics {
                progressBarRangeInfo = ProgressBarRangeInfo(value, valueRange, steps)
                if (enabled) setProgress { target -> change(target.coerceIn(valueRange.start, valueRange.endInclusive)); true }
            }
            .then(
                if (!enabled) Modifier else Modifier
                    .pointerInput(valueRange, steps) {
                        val knob = 14.dp.toPx()
                        detectTapGestures(onPress = { pos ->
                            held = true
                            change(snap((pos.x - knob) / (size.width - 2 * knob)))
                            tryAwaitRelease()
                            held = false
                            finished?.invoke()
                        })
                    }
                    .pointerInput(valueRange, steps) {
                        val knob = 14.dp.toPx()
                        detectHorizontalDragGestures(
                            onDragStart = { held = true },
                            onDragEnd = { held = false; finished?.invoke() },
                            onDragCancel = { held = false },
                        ) { pointer, _ ->
                            pointer.consume()
                            change(snap((pointer.position.x - knob) / (size.width - 2 * knob)))
                        }
                    },
            ),
    ) {
        val knob = 14.dp.toPx()
        val y = size.height / 2
        val left = knob
        val width = size.width - 2 * knob
        val h = 12.dp.toPx()
        val r = CornerRadius(h / 2)
        drawRoundRect(track, Offset(left - h / 2, y - h / 2), Size(width + h, h), r)
        val x = left + width * fraction
        drawRoundRect(fill, Offset(left - h / 2, y - h / 2), Size(x - left + h, h), r)
        // Marcas dos passos, quando poucas: ajudam a "sentir" os encaixes.
        if (steps in 1..12) for (i in 1..steps) {
            val sx = left + width * i / (steps + 1f)
            drawCircle(if (sx <= x) Color.White.copy(alpha = 0.55f) else scheme.outline.copy(alpha = 0.6f), 2.dp.toPx(), Offset(sx, y))
        }
        // O botão: espessura embaixo, face clara e o aro na cor da marca.
        val kr = knob * grow
        drawCircle(lerp(fill, Color.Black, 0.3f), kr, Offset(x, y + 3.dp.toPx()))
        drawCircle(Color.White, kr, Offset(x, y))
        drawCircle(fill, kr, Offset(x, y), style = Stroke(4.dp.toPx()))
    }
}
