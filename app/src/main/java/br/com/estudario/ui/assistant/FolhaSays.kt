package br.com.estudario.ui.assistant

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

/**
 * O Folha conversando: o texto aparece no balão como se ele estivesse falando (com pausas nas
 * vírgulas e nos pontos), e a boca dele acompanha. Terminada a fala, ele fica ouvindo.
 */
/**
 * O balão de fala com cara de quadrinho, igual ao do site: cantos irregulares feitos à mão, contorno
 * de caneta, rabicho curvo apontando para o Folha e a sombra dura dos adesivos (antes era um retângulo).
 */
internal fun Modifier.speechBubble(fill: Color, tail: Boolean = true): Modifier = this.drawBehind {
    val ink = Color(0xFF26234A).copy(alpha = 0.85f)
    val stroke = 2.5.dp.toPx()
    val lip = 3.dp.toPx()
    val w = size.width
    val h = size.height
    fun bubble(dy: Float) = androidx.compose.ui.graphics.Path().apply {
        val r1 = 24.dp.toPx(); val r2 = 20.dp.toPx(); val r3 = 22.dp.toPx(); val r4 = 12.dp.toPx()
        moveTo(r1, dy)
        lineTo(w - r2, dy)
        quadraticTo(w, dy, w, dy + r2)
        lineTo(w, h - r3 + dy)
        quadraticTo(w, h + dy, w - r3, h + dy)
        lineTo(r4 + 10.dp.toPx(), h + dy)
        if (tail) {
            // o rabicho sai do canto de baixo e curva até o Folha
            quadraticTo(r4, h + dy, 2.dp.toPx(), h + dy + 2.dp.toPx())
            quadraticTo(-10.dp.toPx(), h + dy + 4.dp.toPx(), -14.dp.toPx(), h + dy - 2.dp.toPx())
            quadraticTo(-4.dp.toPx(), h + dy - 6.dp.toPx(), 0f, h + dy - r4)
        } else {
            quadraticTo(0f, h + dy, 0f, h + dy - r4)
        }
        lineTo(0f, dy + r1)
        quadraticTo(0f, dy, r1, dy)
        close()
    }
    drawPath(bubble(lip), ink.copy(alpha = 0.55f))
    val shape = bubble(0f)
    drawPath(shape, fill)
    drawPath(shape, ink, style = androidx.compose.ui.graphics.drawscope.Stroke(stroke))
    // o brilho de papel no alto
    drawRoundRect(Color.White.copy(alpha = 0.28f), topLeft = androidx.compose.ui.geometry.Offset(16.dp.toPx(), 6.dp.toPx()),
        size = androidx.compose.ui.geometry.Size(30.dp.toPx(), 5.dp.toPx()), cornerRadius = androidx.compose.ui.geometry.CornerRadius(4.dp.toPx()))
}

@Composable
fun FolhaSays(
    text: String,
    modifier: Modifier = Modifier,
    avatar: Dp = 52.dp,
    bubbleColor: Color = MaterialTheme.colorScheme.surfaceContainerHighest,
    textColor: Color = MaterialTheme.colorScheme.onSurface,
    onFolhaClick: (() -> Unit)? = null,
) {
    var shown by remember(text) { mutableIntStateOf(0) }
    LaunchedEffect(text) {
        while (shown < text.length) {
            val ch = text[shown]
            shown++
            delay(
                when (ch) {
                    '.', '!', '?', ':' -> 220L
                    ',' -> 110L
                    ' ' -> 18L
                    else -> 14L
                },
            )
        }
    }
    val talking = shown < text.length
    Row(modifier, verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        if (avatar > 0.dp) Folha(avatar, mood = if (talking) FolhaMood.TALKING else FolhaMood.IDLE, onClick = onFolhaClick)
        Box(
            Modifier
                .weight(1f)
                .padding(start = if (avatar > 0.dp) 8.dp else 0.dp, bottom = 3.dp)
                .speechBubble(bubbleColor, tail = avatar > 0.dp)
                .padding(horizontal = 16.dp, vertical = 11.dp),
        ) {
            // O texto inteiro, invisível, reserva a altura final: o balão não fica "pulando".
            Text(text, style = MaterialTheme.typography.bodyMedium, color = Color.Transparent)
            Text(text.take(shown), style = MaterialTheme.typography.bodyMedium, color = textColor)
        }
    }
}
