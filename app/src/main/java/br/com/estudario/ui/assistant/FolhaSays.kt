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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

/**
 * O Folha conversando: o texto aparece no balão como se ele estivesse falando (com pausas nas
 * vírgulas e nos pontos), e a boca dele acompanha. Terminada a fala, ele fica ouvindo.
 */
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
                .background(bubbleColor, RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp, bottomEnd = 18.dp, bottomStart = 4.dp))
                .padding(horizontal = 14.dp, vertical = 10.dp),
        ) {
            // O texto inteiro, invisível, reserva a altura final: o balão não fica "pulando".
            Text(text, style = MaterialTheme.typography.bodyMedium, color = Color.Transparent)
            Text(text.take(shown), style = MaterialTheme.typography.bodyMedium, color = textColor)
        }
    }
}
