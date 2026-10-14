package br.com.estudario.ui.brand

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * O botão do menu no jeito do Estudário, no lugar dos três tracinhos do Android: um adesivo de
 * caderno como os ícones da turma do Folha (recorte branco, traço de caneta, aba de profundidade
 * embaixo) com três abas coloridas de tamanhos diferentes, como as divisórias de um fichário.
 * As cores são fixas, como as da turma, e não mudam com o tema.
 */
@Composable
fun BrandMenuMark(modifier: Modifier = Modifier, size: Dp = 30.dp) {
    Canvas(modifier.size(size)) {
        val s = this.size.minDimension
        val pen = s * 0.06f
        val lip = s * 0.08f
        val left = s * 0.08f
        val top = s * 0.08f
        val w = s * 0.84f
        val h = s * 0.76f
        val radius = CornerRadius(s * 0.22f)
        // Aba de profundidade (o "degrau" embaixo, como nos adesivos da turma).
        drawRoundRect(Ink, Offset(left, top + lip), Size(w, h), radius)
        // O recorte branco com o traço de caneta.
        drawRoundRect(Paper, Offset(left, top), Size(w, h), radius)
        drawRoundRect(Ink, Offset(left, top), Size(w, h), radius, style = Stroke(pen))
        // As três abas: tamanhos diferentes, nas cores da marca, com o mesmo traço.
        val barH = s * 0.15f
        val barX = left + s * 0.15f
        val gap = (h - 3 * barH) / 4f
        listOf(Indigo to 0.54f, Green to 0.42f, Amber to 0.30f).forEachIndexed { i, (color, width) ->
            val y = top + gap + i * (barH + gap)
            val barW = s * width
            val corner = CornerRadius(barH / 2f)
            drawRoundRect(color, Offset(barX, y), Size(barW, barH), corner)
            drawRoundRect(Ink, Offset(barX, y), Size(barW, barH), corner, style = Stroke(pen * 0.7f))
        }
    }
}

private val Ink = Color(0xFF1E1B4B)
private val Paper = Color(0xFFFFFFFF)
private val Indigo = Color(0xFF6366F1)
private val Green = Color(0xFF22C59A)
private val Amber = Color(0xFFF2B33D)
