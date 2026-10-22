package br.com.estudario.ui.profile

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.unit.Dp
import br.com.estudario.domain.Badge
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/** Metal de cada nível de emblema (claro, meio, escuro): o mesmo do site (folha-live.js, TIERS). */
private val MEDAL_TIERS = mapOf(
    1 to listOf(Color(0xFFF2C49A), Color(0xFFC27E45), Color(0xFF7D4520)),
    2 to listOf(Color(0xFFFFFFFF), Color(0xFFC7CDD9), Color(0xFF7D8699)),
    3 to listOf(Color(0xFFFFF1BF), Color(0xFFF5B83D), Color(0xFFB07510)),
    4 to listOf(Color(0xFFCFE3FF), Color(0xFF5E9BFF), Color(0xFF1F4FB8)),
    5 to listOf(Color(0xFFFFD0D8), Color(0xFFF0506B), Color(0xFFA0172F)),
    6 to listOf(Color(0xFFD4FFEE), Color(0xFF3FD69F), Color(0xFF0E7C55)),
)
private const val PIF = PI.toFloat()

/** O símbolo da categoria (caixa de -10 a 10), em traço grosso e partes cheias: o mesmo do site (badgeGlyph). */
private fun glyphPaths(cat: String): Pair<Path, Path> {
    val p = Path(); val f = Path()
    fun arc(path: Path, cx: Float, cy: Float, r: Float, from: Float = 0f, sweep: Float = 2 * PIF) =
        path.arcTo(Rect(cx - r, cy - r, cx + r, cy + r), from * 180f / PIF, sweep * 180f / PIF, true)
    when (cat) {
        "seq" -> f.apply { moveTo(0f, 9.5f); cubicTo(-6.5f, 9.5f, -7.5f, 3f, -4.2f, -1.2f); cubicTo(-3.6f, 1f, -2.4f, 2.2f, -1.2f, 2.2f); cubicTo(-2.4f, -3f, 0f, -7.6f, 3.4f, -9.6f); cubicTo(2.4f, -5.4f, 7.6f, -2.4f, 6.6f, 3.2f); cubicTo(6.2f, 7f, 3.8f, 9.5f, 0f, 9.5f); close() }
        "meta" -> { p.moveTo(-5.5f, 9.5f); p.lineTo(-5.5f, -9f); f.apply { moveTo(-5.5f, -8.6f); cubicTo(-1f, -10.6f, 2.4f, -6f, 7.6f, -8.2f); lineTo(7.6f, 0.4f); cubicTo(2.4f, 2.6f, -1f, -2f, -5.5f, 0f); close() } }
        "plano" -> p.apply { moveTo(-8f, -5f); lineTo(8f, -5f); lineTo(8f, 8.5f); lineTo(-8f, 8.5f); close(); moveTo(-8f, -1f); lineTo(8f, -1f); moveTo(-4f, -8.5f); lineTo(-4f, -3f); moveTo(4f, -8.5f); lineTo(4f, -3f); moveTo(-3.6f, 3.6f); lineTo(-1f, 6f); lineTo(4f, 1.2f) }
        "quest" -> { p.apply { for (y in listOf(-6f, 0f, 6f)) { moveTo(-1.6f, y); lineTo(8f, y) }; moveTo(-8.4f, -6f); lineTo(-6.6f, -4.2f); lineTo(-3.6f, -8.2f); moveTo(-8.4f, 0f); lineTo(-6.6f, 1.8f); lineTo(-3.6f, -2.2f) }; arc(f, -6f, 6f, 1.6f) }
        "acerto" -> { arc(p, -1f, 1f, 8f); arc(p, -1f, 1f, 4.6f); arc(f, -1f, 1f, 1.8f); p.apply { moveTo(-1f, 1f); lineTo(8.4f, -8.4f); moveTo(8.4f, -8.4f); lineTo(5f, -8.6f); moveTo(8.4f, -8.4f); lineTo(8.6f, -5f) } }
        "topico" -> p.apply { moveTo(0f, -5f); cubicTo(-3f, -7.4f, -7f, -7.4f, -9.4f, -6.2f); lineTo(-9.4f, 7.4f); cubicTo(-7f, 6.2f, -3f, 6.2f, 0f, 8.4f); cubicTo(3f, 6.2f, 7f, 6.2f, 9.4f, 7.4f); lineTo(9.4f, -6.2f); cubicTo(7f, -7.4f, 3f, -7.4f, 0f, -5f); lineTo(0f, 8.4f) }
        "edital" -> p.apply { moveTo(-6.6f, -9f); lineTo(3f, -9f); lineTo(7f, -5f); lineTo(7f, 9f); lineTo(-6.6f, 9f); close(); moveTo(3f, -9f); lineTo(3f, -5f); lineTo(7f, -5f); for (y in listOf(-2f, 2f, 6f)) { moveTo(-3.6f, y); lineTo(4f, y) } }
        "revisao" -> { arc(p, 0f, 0f, 7.6f, -2.6f, 2.8f); p.apply { moveTo(7.4f, 2.4f); lineTo(7.6f, -2.4f); moveTo(7.6f, 1.4f); lineTo(4f, -0.2f) }; arc(p, 0f, 0f, 7.6f, 0.54f, 2.8f); p.apply { moveTo(-7.4f, -2.4f); lineTo(-7.6f, 2.4f); moveTo(-7.6f, -1.4f); lineTo(-4f, 0.2f) } }
        "simulado" -> { p.apply { moveTo(-7f, -6.5f); lineTo(7f, -6.5f); lineTo(7f, 9.4f); lineTo(-7f, 9.4f); close(); moveTo(-3.6f, 2.4f); lineTo(-0.8f, 5.2f); lineTo(4f, -0.6f) }; f.apply { moveTo(-3.6f, -9.6f); lineTo(3.6f, -9.6f); lineTo(3.6f, -4.4f); lineTo(-3.6f, -4.4f); close() } }
        "discursiva" -> { f.apply { moveTo(7.8f, -9.4f); cubicTo(1f, -7.6f, -5.6f, -1f, -6.6f, 6.6f); lineTo(-4.6f, 4.8f); cubicTo(1.4f, 3.6f, 6.6f, -2.6f, 7.8f, -9.4f); close() }; p.apply { moveTo(-6.6f, 6.6f); lineTo(-8.6f, 9.4f); moveTo(-8.6f, 9.6f); lineTo(8f, 9.6f) } }
        else -> { arc(p, 0f, 1.4f, 7.8f); p.apply { moveTo(0f, 1.4f); lineTo(0f, -3.4f); moveTo(0f, 1.4f); lineTo(3.6f, 3.4f); moveTo(6.6f, -5f); lineTo(8.2f, -6.6f) }; f.apply { moveTo(-2.4f, -9.6f); lineTo(2.4f, -9.6f); lineTo(2.4f, -7.2f); lineTo(-2.4f, -7.2f); close() } }
    }
    return p to f
}

private fun DrawScope.glyph(cat: String, color: Color) {
    val (p, f) = glyphPaths(cat)
    drawPath(p, color, style = Stroke(2.2f, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(f, color); drawPath(f, color, style = Stroke(1f, cap = StrokeCap.Round, join = StrokeJoin.Round))
}

private fun star5(cx: Float, cy: Float, r: Float) = Path().apply {
    for (i in 0 until 10) { val a = -PIF / 2 + i * PIF / 5; val rr = if (i % 2 == 1) r * 0.45f else r; val x = cx + cos(a) * rr; val y = cy + sin(a) * rr; if (i == 0) moveTo(x, y) else lineTo(x, y) }
    close()
}

/**
 * A medalha de emblema (o mesmo desenho do site): símbolo cunhado no metal, guilhochê, estrelinhas do
 * nível e a moldura que cresce com ele (Ouro louros, Safira gema, Rubi roseta, Esmeralda asas). Centro em
 * [c], raio [r].
 */
private fun DrawScope.medal(c: Offset, r: Float, tier: Int, cat: String) {
    val col = MEDAL_TIERS[tier] ?: MEDAL_TIERS.getValue(3)
    fun metal(x0: Float, y0: Float, x1: Float, y1: Float, a: Color, b: Color, d: Color) = Brush.linearGradient(listOf(a, b, d), Offset(x0, y0), Offset(x1, y1))
    translate(c.x, c.y) {
        if (tier >= 5) for ((ro, ri, rot, base) in listOf(Quad4(1.62f, 1.28f, 0f, col[2]), Quad4(1.48f, 1.18f, PIF / 16, col[1]))) {
            val p = Path(); for (i in 0 until 32) { val a = rot + i * PIF / 16; val rr = r * if (i % 2 == 1) ri else ro; if (i == 0) p.moveTo(cos(a) * rr, sin(a) * rr) else p.lineTo(cos(a) * rr, sin(a) * rr) }; p.close()
            drawPath(p, metal(-r, -r, r, r, base, col[1], col[2])); drawPath(p, Color(0x40000000), style = Stroke(r * 0.02f))
        }
        if (tier >= 6) for (side in listOf(-1f, 1f)) for (i in 4 downTo 0) {
            translate(side * r * 0.85f, -r * 0.15f) { scale(side, 1f, pivot = Offset.Zero) { rotate(-20f - i * 14f, pivot = Offset.Zero) {
                val len = r * (1.05f + i * 0.16f)
                val f = Path().apply { moveTo(0f, -r * 0.12f); cubicTo(len * 0.4f, -r * 0.3f, len * 0.85f, -r * 0.24f, len, -r * 0.03f); cubicTo(len * 0.85f, r * 0.1f, len * 0.4f, r * 0.16f, 0f, r * 0.12f); close() }
                drawPath(f, metal(0f, 0f, len, 0f, col[2], col[1], col[0])); drawPath(f, Color(0x4D000000), style = Stroke(r * 0.02f))
            } } }
        }
        if (tier >= 3) for (side in listOf(-1f, 1f)) {
            for (i in 0 until 9) {
                val a = PIF / 2 + side * (0.4f + i * 0.205f); val x = cos(a) * r * 1.13f; val y = sin(a) * r * 1.13f
                for (off in listOf(-1f, 1f)) translate(x, y) { rotate((a + side * PIF / 2 + off * 0.55f + side * 0.25f) * 180f / PIF, pivot = Offset.Zero) {
                    drawOval(metal(0f, -r * 0.07f, r * 0.3f, r * 0.07f, col[0], col[1], col[2]), Offset(-r * 0.01f, -r * 0.07f), Size(r * 0.34f, r * 0.14f))
                } }
            }
        }
        // a moeda: serrilha, chanfro e face
        val rim = Path(); for (i in 0..96) { val a = i * PIF / 48; val rr = if (i % 2 == 1) r else r * 0.965f; if (i == 0) rim.moveTo(cos(a) * rr, sin(a) * rr) else rim.lineTo(cos(a) * rr, sin(a) * rr) }
        drawCircle(Color(0x40000000), r * 1.02f, Offset(0f, r * 0.06f))
        drawPath(rim, metal(-r, -r, r, r, col[0], col[1], col[2]))
        drawCircle(metal(r, r, -r, -r, col[0], col[1], col[2]), r * 0.87f, Offset.Zero)
        drawCircle(Brush.radialGradient(0f to col[0], 0.65f to col[1], 1f to col[2], center = Offset(-r * 0.3f, -r * 0.35f), radius = r * 0.85f), r * 0.8f, Offset.Zero)
        clipPath(Path().apply { addOval(Rect(-r * 0.8f, -r * 0.8f, r * 0.8f, r * 0.8f)) }) {
            for (i in 1..7) drawCircle(Color(0x21FFFFFF), r * 0.8f * i / 7.5f, Offset.Zero, style = Stroke(r * 0.008f))
            for (i in 0 until 36) { val a = i * PIF / 18; drawLine(Color(0x0F000000), Offset.Zero, Offset(cos(a) * r * 0.8f, sin(a) * r * 0.8f), r * 0.01f) }
        }
        drawCircle(Color(0x38000000), r * 0.71f, Offset.Zero, style = Stroke(r * 0.025f)); drawCircle(Color(0x59FFFFFF), r * 0.69f, Offset.Zero, style = Stroke(r * 0.012f))
        for (i in 0 until tier) { val a = PIF / 2 + (i - (tier - 1) / 2f) * 0.27f; val x = cos(a) * r * 0.58f; val y = sin(a) * r * 0.58f; drawPath(star5(x + r * 0.01f, y + r * 0.015f, r * 0.075f), col[2]); drawPath(star5(x, y, r * 0.07f), col[0]) }
        // o símbolo cunhado (o verso, "verso", é liso)
        val s = r / 13.5f
        if (cat != "verso") translate(0f, -r * 0.06f) { scale(s, s, pivot = Offset.Zero) {
            translate(0.55f, 0.65f) { glyph(cat, Color(0x61000000)) }
            translate(-0.4f, -0.45f) { glyph(cat, Color(0xBFFFFFFF)) }
            glyph(cat, col[2])
        } }
        drawArc(Color(0xBFFFFFFF), 194f, 66f, false, Offset(-r * 0.92f, -r * 0.92f), Size(r * 1.84f, r * 1.84f), style = Stroke(r * 0.05f, cap = StrokeCap.Round))
        if (tier >= 4) {
            val gy = -r * 1.02f; val gs = r * 0.2f
            val gem = Path().apply { moveTo(0f, gy - gs); lineTo(gs * 0.9f, gy - gs * 0.2f); lineTo(0f, gy + gs * 0.9f); lineTo(-gs * 0.9f, gy - gs * 0.2f); close() }
            val gemC = when (tier) { 4 -> Color(0xFF5E9BFF); 5 -> Color(0xFFF0506B); else -> Color(0xFF3FD69F) }
            drawPath(gem, metal(-gs, gy - gs, gs, gy + gs, Color.White, gemC, Color(0xFF1A1040)))
            drawLine(Color(0xB3FFFFFF), Offset(-gs * 0.9f, gy - gs * 0.2f), Offset(gs * 0.9f, gy - gs * 0.2f), r * 0.015f)
        }
    }
}

private data class Quad4(val a: Float, val b: Float, val c: Float, val d: Color)

/** A medalha de um emblema, desenhada; trancada sai em cinza. */
@Composable
fun MedalArt(badge: Badge, earned: Boolean, size: Dp, modifier: Modifier = Modifier) {
    val tier = badge.tier.coerceIn(1, 6)
    val cat = badge.id.substringBefore("-")
    Box(modifier.size(size), contentAlignment = Alignment.Center) {
        Canvas(Modifier.fillMaxSize()) {
            val s = this.size.minDimension
            val r = s / 2f * when { tier >= 5 -> 0.5f; tier >= 3 -> 0.6f; else -> 0.76f }
            if (earned) medal(Offset(this.size.width / 2, this.size.height / 2), r, tier, cat)
            else drawIntoCanvas { canvas ->
                // trancada: a mesma medalha em cinza, mais apagada
                val paint = Paint().apply { colorFilter = ColorFilter.colorMatrix(ColorMatrix().apply { setToSaturation(0f) }); alpha = 0.55f }
                canvas.saveLayer(Rect(0f, 0f, this.size.width, this.size.height), paint)
                medal(Offset(this.size.width / 2, this.size.height / 2), r, minOf(tier, 2), cat)
                canvas.restore()
            }
        }
    }
}
