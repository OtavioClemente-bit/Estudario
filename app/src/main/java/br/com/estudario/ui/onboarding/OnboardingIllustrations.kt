package br.com.estudario.ui.onboarding

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.dp
import br.com.estudario.ui.theme.estudarioColors

@Composable
fun EmptyNoExamArt(modifier: Modifier = Modifier) {
    val color = estudarioColors().current
    Canvas(modifier.fillMaxWidth().height(116.dp)) {
        val width = size.width * .7f
        val left = (size.width - width) / 2
        val barHeight = size.height / 7
        repeat(4) { index ->
            val top = size.height * .12f + index * barHeight * 1.35f
            drawRoundRect(color.copy(alpha = if (index < 3) .85f else .25f), Offset(left, top), Size(width * (1 - index * .12f), barHeight), CornerRadius(barHeight / 2))
        }
    }
}
