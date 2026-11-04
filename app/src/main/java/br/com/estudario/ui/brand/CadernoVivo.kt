package br.com.estudario.ui.brand

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.graphicsLayer
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

/**
 * O papel ao fundo de tudo: milimetrado no claro (linhas finas a cada 8 dp, fortes a cada 40 dp, tinta
 * azul de caneta) e papel de planta azul de engenheiro no escuro, com a luz da luminária no canto.
 * (O nome ficou da época do pontilhado; quem chama não precisa mudar.)
 */
@Composable
fun Modifier.dottedPaper(): Modifier {
    val night = isCadernoNight()
    androidx.compose.ui.platform.LocalContext.current.let { PaperStyle.load(it) }
    val pattern = PaperStyle.pattern
    val ink = if (night) Color(0xFF8CBEFF) else Color(0xFF2F5BD3)
    val fine = ink.copy(alpha = if (night) 0.09f else 0.07f)
    val strong = ink.copy(alpha = if (night) 0.19f else 0.16f)
    val glowBase = when (PaperStyle.light) {
        "azul" -> Color(0xFF5C86FF); "por-do-sol" -> Color(0xFFFF7FA8); "menta" -> Color(0xFF4CD08A); "sem" -> Color.Transparent
        else -> if (night) Color(0xFF5C86FF) else Color(0xFFFFD23F)
    }
    val glow = glowBase.copy(alpha = if (glowBase == Color.Transparent) 0f else if (night) 0.22f else 0.20f)
    val margin = Color(0xFFE0484B).copy(alpha = if (night) 0.35f else 0.30f)
    val paper = MaterialTheme.colorScheme.background
    // O padrão vira uma pecinha desenhada uma vez e repetida pela GPU (antes eram ~1000 círculos a cada
    // quadro, e qualquer animação de qualquer tela fazia redesenhar o fundo inteiro: o app travava).
    return this.drawWithCache {
        val step = (if (pattern == "pautado") 28.dp else if (pattern == "pontos") 20.dp else 40.dp).toPx().toInt().coerceAtLeast(5)
        val small = step / 5f
        val line = 1.dp.toPx().coerceAtLeast(1f)
        val tile = androidx.compose.ui.graphics.ImageBitmap(step, step)
        androidx.compose.ui.graphics.drawscope.CanvasDrawScope().draw(this, layoutDirection, androidx.compose.ui.graphics.Canvas(tile), Size(step.toFloat(), step.toFloat())) {
            drawRect(paper)
            when (pattern) {
                "quadriculado" -> {
                    drawRect(strong, Offset(0f, 0f), Size(line, step.toFloat()))
                    drawRect(strong, Offset(0f, 0f), Size(step.toFloat(), line))
                }
                "pontos" -> drawCircle(strong.copy(alpha = strong.alpha * 1.6f), 1.3.dp.toPx(), Offset(step / 2f, step / 2f))
                "pautado" -> drawRect(strong, Offset(0f, step - line), Size(step.toFloat(), line))
                "liso" -> Unit
                else -> {
                    for (i in 1 until 5) {
                        drawRect(fine, Offset(i * small, 0f), Size(line, step.toFloat()))
                        drawRect(fine, Offset(0f, i * small), Size(step.toFloat(), line))
                    }
                    drawRect(strong, Offset(0f, 0f), Size(line, step.toFloat()))
                    drawRect(strong, Offset(0f, 0f), Size(step.toFloat(), line))
                }
            }
        }
        val brush = androidx.compose.ui.graphics.ShaderBrush(androidx.compose.ui.graphics.ImageShader(tile, androidx.compose.ui.graphics.TileMode.Repeated, androidx.compose.ui.graphics.TileMode.Repeated))
        val light = androidx.compose.ui.graphics.Brush.radialGradient(listOf(glow, Color.Transparent), center = Offset(size.width, 0f), radius = size.maxDimension * 0.7f)
        onDrawBehind {
            drawRect(brush)
            if (pattern == "pautado") drawRect(margin, Offset(28.dp.toPx(), 0f), Size(1.5.dp.toPx(), size.height))
            drawRect(light)
        }
    }.graphicsLayer()
}
