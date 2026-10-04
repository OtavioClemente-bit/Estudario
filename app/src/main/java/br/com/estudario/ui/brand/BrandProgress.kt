package br.com.estudario.ui.brand

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * A barra de progresso do Estudário: grossa, redonda, trilha neutra (vazio é vazio) e o
 * preenchimento na cor da marca com um filete de brilho em cima, como os botões. Sem o pontinho
 * no fim da trilha do Material, que fazia "0 de 10" parecer barra cheia.
 */
@Composable
fun LinearProgressIndicator(
    progress: () -> Float,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
    trackColor: Color = Color.Unspecified,
    @Suppress("UNUSED_PARAMETER") strokeCap: StrokeCap = StrokeCap.Round,
    @Suppress("UNUSED_PARAMETER") gapSize: Dp = 0.dp,
    @Suppress("UNUSED_PARAMETER") drawStopIndicator: DrawScope.() -> Unit = {},
) {
    val track = if (trackColor == Color.Unspecified || trackColor == ProgressIndicatorDefaults.linearTrackColor) {
        MaterialTheme.colorScheme.surfaceVariant
    } else trackColor
    val value = progress().coerceIn(0f, 1f)
    Canvas(
        modifier
            .fillMaxWidth()
            .height(10.dp)
            .semantics { progressBarRangeInfo = ProgressBarRangeInfo(value, 0f..1f) },
    ) {
        val r = CornerRadius(size.height / 2, size.height / 2)
        drawRoundRect(track, cornerRadius = r)
        if (value <= 0f) return@Canvas
        val w = (size.width * value).coerceAtLeast(size.height)
        drawRoundRect(color, size = Size(w, size.height), cornerRadius = r)
        // Filete de luz no alto do preenchimento.
        val inset = size.height * 0.28f
        if (w > size.height * 1.5f) drawRoundRect(
            Color.White.copy(alpha = 0.30f),
            topLeft = Offset(inset * 1.4f, size.height * 0.18f),
            size = Size(w - inset * 2.8f, size.height * 0.22f),
            cornerRadius = CornerRadius(size.height * 0.11f),
        )
    }
}

/** Sem progresso conhecido, continua a animação do Material, com a cor da marca. */
@Composable
fun LinearProgressIndicator(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
    trackColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    strokeCap: StrokeCap = StrokeCap.Round,
) = androidx.compose.material3.LinearProgressIndicator(modifier.height(8.dp), color, trackColor, strokeCap)
