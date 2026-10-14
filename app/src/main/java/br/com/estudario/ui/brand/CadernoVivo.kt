package br.com.estudario.ui.brand

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.unit.dp

/*
 * A identidade "caderno vivo", a mesma do site (styles.css, bloco CADERNO VIVO): o papel pontilhado
 * ao fundo e a passada de marca-texto amarelo atrás dos títulos e da marca.
 */

private val Highlighter = Color(0xFFFFD23F)

/** Se o tema atual é o escuro (pelo fundo, então vale também a escolha feita nos ajustes do app). */
@Composable
fun isCadernoNight(): Boolean = MaterialTheme.colorScheme.background.luminance() < 0.5f

/** Passada de marca-texto atrás da metade de baixo do texto, como nos títulos do site. */
@Composable
fun Modifier.marker(): Modifier {
    val color = Highlighter.copy(alpha = if (isCadernoNight()) 0.30f else 0.55f)
    return this.drawBehind {
        drawRoundRect(
            color,
            topLeft = Offset(-3.dp.toPx(), size.height * 0.60f),
            size = Size(size.width + 6.dp.toPx(), size.height * 0.32f),
            cornerRadius = CornerRadius(2.dp.toPx()),
        )
    }
}

/** O papel de caderno pontilhado (pontos de 1,3 dp a cada 22 dp), atrás de tudo. */
@Composable
fun Modifier.dottedPaper(): Modifier {
    val night = isCadernoNight()
    val dot = if (night) Color.White.copy(alpha = 0.07f) else Color(0xFF26234A).copy(alpha = 0.11f)
    val paper = MaterialTheme.colorScheme.background
    return this.drawBehind {
        drawRect(paper)
        val step = 22.dp.toPx()
        val radius = 1.3.dp.toPx()
        var y = step / 2
        while (y < size.height) {
            var x = step / 2
            while (x < size.width) {
                drawCircle(dot, radius, Offset(x, y))
                x += step
            }
            y += step
        }
    }
}
