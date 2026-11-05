package br.com.estudario.ui.brand

import androidx.compose.foundation.layout.padding
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

/**
 * O "risco do Folha", a assinatura dos títulos (a mesma do site): um risco de caneta feito à mão,
 * levemente ondulado, com um segundo traço fino por baixo e a folhinha verde na ponta. Fica embaixo
 * do texto, sem cobrir as letras. (O nome marker ficou da época da faixa de marca-texto.)
 */
@Composable
fun Modifier.marker(): Modifier {
    val night = isCadernoNight()
    val ink = if (night) Color(0xFF8FB0FF) else Color(0xFF2F5BD3)
    val leaf = if (night) Color(0xFF4CD08A) else Color(0xFF3BB273)
    val edge = if (night) Color(0xFF05091A) else Color(0xFF1C1E25)
    return this.padding(end = 15.dp, bottom = 8.dp).drawBehind {
        val leafW = 15.dp.toPx(); val leafH = 11.dp.toPx()
        val w = size.width - leafW * 0.75f
        val y = size.height - 4.dp.toPx()
        val main = androidx.compose.ui.graphics.Path().apply {
            moveTo(1.dp.toPx(), y)
            cubicTo(w * 0.16f, y - 3.dp.toPx(), w * 0.31f, y + 2.dp.toPx(), w * 0.47f, y - 1.dp.toPx())
            cubicTo(w * 0.64f, y - 3.5f.dp.toPx(), w * 0.8f, y - 1.dp.toPx(), w, y - 0.5f.dp.toPx())
        }
        drawPath(main, ink, style = androidx.compose.ui.graphics.drawscope.Stroke(2.6f.dp.toPx(), cap = androidx.compose.ui.graphics.StrokeCap.Round))
        val thin = androidx.compose.ui.graphics.Path().apply {
            moveTo(w * 0.06f, y + 3.dp.toPx())
            cubicTo(w * 0.3f, y + 1.5f.dp.toPx(), w * 0.55f, y + 4.dp.toPx(), w * 0.78f, y + 2.dp.toPx())
        }
        drawPath(thin, ink.copy(alpha = 0.5f), style = androidx.compose.ui.graphics.drawscope.Stroke(1.3f.dp.toPx(), cap = androidx.compose.ui.graphics.StrokeCap.Round))
        // a folhinha na ponta do risco
        val lx = size.width - leafW; val ly = y - leafH * 0.85f
        fun px(x: Float) = lx + x / 24f * leafW
        fun py(v: Float) = ly + v / 18f * leafH
        val shape = androidx.compose.ui.graphics.Path().apply {
            moveTo(px(2f), py(15f))
            cubicTo(px(4f), py(5f), px(13f), py(1f), px(22f), py(2f))
            cubicTo(px(21f), py(10f), px(14f), py(16f), px(2f), py(15f))
            close()
        }
        drawPath(shape, leaf)
        drawPath(shape, edge, style = androidx.compose.ui.graphics.drawscope.Stroke(1.3f.dp.toPx(), join = androidx.compose.ui.graphics.StrokeJoin.Round))
        val vein = androidx.compose.ui.graphics.Path().apply { moveTo(px(3.5f), py(14f)); cubicTo(px(9f), py(10f), px(14f), py(7f), px(19f), py(4f)) }
        drawPath(vein, edge, style = androidx.compose.ui.graphics.drawscope.Stroke(1.dp.toPx(), cap = androidx.compose.ui.graphics.StrokeCap.Round))
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
