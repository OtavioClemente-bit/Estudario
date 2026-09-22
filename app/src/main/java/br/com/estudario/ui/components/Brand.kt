package br.com.estudario.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import br.com.estudario.ui.theme.estudarioColors

@Composable
fun EstudarioGlyph(size: Dp = 28.dp, modifier: Modifier = Modifier) {
    val filled = MaterialTheme.colorScheme.primary
    val pending = estudarioColors().upcoming
    Canvas(modifier.size(size)) {
        val gap = this.size.height * .11f
        val height = (this.size.height - gap * 3) / 4
        listOf(1f, .82f, .64f, .46f).forEachIndexed { index, width ->
            val top = (3 - index) * (height + gap)
            drawRoundRect(if (index == 3) pending.copy(alpha = .45f) else filled, Offset.Zero.copy(y = top), Size(this.size.width * width, height), CornerRadius(height / 2))
        }
    }
}
