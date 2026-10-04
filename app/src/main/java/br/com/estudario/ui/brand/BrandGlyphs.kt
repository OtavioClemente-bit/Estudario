package br.com.estudario.ui.brand

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.cos
import kotlin.math.sin

/**
 * Os ícones do Estudário. Cada um é desenhado aqui, peça por peça, no mesmo estilo: volume de
 * massinha, luz vinda de cima, uma borda mais escura embaixo que dá a espessura e um brilho no alto.
 * É o mesmo relevo dos botões ([Button]), para que a tela inteira pareça feita da mesma matéria.
 *
 * As cores são fixas e não mudam com o tema: um ícone é um objeto, e um objeto não troca de cor
 * quando a luz do quarto apaga. Sem cor (desligado, bloqueado, aba fora de foco) ele fica em cinza.
 */
enum class Glyph {
    House, Books, OpenBook, Calendar, Timer, Clipboard, Notebook, Bookmark, Cap, Spark, Check, Flag,
    Lock, Star, Trophy, Medal, Bulb, Warning, Fire, Bolt, Heart, Bell, Chart, Cycle, Target, Folder,
    Gear, Help, Person, Shield, Cards, Dice, Play, Cloud, Doc, Pencil, Sun, Moon, Phone, Gem, Brain,
}

object BrandPalette {
    val Indigo = Color(0xFF5B4BF5)
    val Green = Color(0xFF1FB574)
    val Amber = Color(0xFFFFB421)
    val Coral = Color(0xFFFF5C4D)
    val Sky = Color(0xFF2EA8F5)
    val Pink = Color(0xFFF0559A)
    val Violet = Color(0xFF8E5CF7)
    val Brown = Color(0xFFB9772E)
    val Paper = Color(0xFFFFFDF8)
    val Ink = Color(0xFF2B2A4A)
    val Steel = Color(0xFF8D8BA8)
}

@Composable
fun BrandIcon(glyph: Glyph, modifier: Modifier = Modifier, size: Dp = 28.dp, muted: Boolean = false) {
    Canvas(modifier.size(size)) { drawGlyph(glyph, muted) }
}

/** Desenha o ícone ocupando a área atual, centralizado. */
fun DrawScope.drawGlyph(glyph: Glyph, muted: Boolean = false) {
    val side = size.minDimension
    val k = side / 100f
    translate((size.width - side) / 2f, (size.height - side) / 2f) {
        scale(k, k, pivot = Offset.Zero) {
            val g = GlyphPainter(this, muted)
            g.shadow()
            g.paint(glyph)
        }
    }
}

private class GlyphPainter(val d: DrawScope, val muted: Boolean) {
    private val B = BrandPalette

    fun c(color: Color): Color {
        if (!muted) return color
        val l = color.luminance()
        val v = 0.55f + l * 0.42f
        return Color(v, v, v * 1.02f, color.alpha)
    }

    private fun dark(color: Color, f: Float = 0.28f) = lerp(c(color), Color(0xFF1A1440), f)
    private fun light(color: Color, f: Float = 0.24f) = lerp(c(color), Color.White, f)

    fun shadow() {
        d.drawOval(Color.Black.copy(alpha = 0.10f), Offset(18f, 88f), Size(64f, 9f))
    }

    /** Peça de massinha: espessura embaixo, face com luz de cima e brilho no alto à esquerda. */
    fun clay(path: Path, color: Color, depth: Float = 6f, round: Float = 0f, gloss: Boolean = true) {
        val side = dark(color)
        val stroke = if (round > 0f) Stroke(round, cap = StrokeCap.Round, join = StrokeJoin.Round) else null
        for (step in listOf(depth, depth * 0.5f)) {
            d.translate(0f, step) {
                drawPath(path, side)
                if (stroke != null) drawPath(path, side, style = stroke)
            }
        }
        val b = path.getBounds()
        val brush = Brush.verticalGradient(listOf(light(color), c(color)), b.top, b.bottom)
        d.drawPath(path, brush)
        if (stroke != null) d.drawPath(path, brush, style = stroke)
        if (gloss) {
            d.clipPath(path) {
                drawOval(
                    Color.White.copy(alpha = 0.30f),
                    Offset(b.left + b.width * 0.10f, b.top + b.height * 0.06f),
                    Size(b.width * 0.42f, (b.height * 0.20f).coerceAtMost(14f)),
                )
            }
        }
    }

    /** Detalhe plano por cima de uma peça (linhas, ponteiros, símbolos). */
    fun line(color: Color, width: Float, a: Offset, b: Offset, last: Offset? = null) {
        val p = Path().apply { moveTo(a.x, a.y); lineTo(b.x, b.y); last?.let { lineTo(it.x, it.y) } }
        d.drawPath(p, c(color), style = Stroke(width, cap = StrokeCap.Round, join = StrokeJoin.Round))
    }

    fun dot(color: Color, x: Float, y: Float, r: Float) = d.drawCircle(c(color), r, Offset(x, y))
    fun flat(path: Path, color: Color) = d.drawPath(path, c(color))

    fun paint(glyph: Glyph) = when (glyph) {
        Glyph.House -> {
            clay(rr(22f, 44f, 56f, 40f, 8f), B.Sky)
            clay(poly(14f, 50f, 50f, 16f, 86f, 50f), B.Coral, depth = 5f, round = 10f)
            clay(rr(42f, 60f, 16f, 24f, 5f), B.Amber, depth = 3f)
            dot(B.Ink, 54f, 73f, 1.8f)
        }
        Glyph.Books -> {
            clay(rr(12f, 26f, 22f, 60f, 6f), B.Indigo)
            clay(rr(38f, 14f, 22f, 72f, 6f), B.Green)
            d.rotate(9f, Offset(76f, 86f)) { clay(rr(64f, 30f, 22f, 56f, 6f), B.Amber) }
            for ((x, top) in listOf(12f to 26f, 38f to 14f)) {
                flat(rr(x + 4f, top + 10f, 14f, 5f, 2.5f), B.Paper.copy(alpha = 0.85f))
                flat(rr(x + 4f, top + 19f, 14f, 3f, 1.5f), B.Paper.copy(alpha = 0.6f))
            }
        }
        Glyph.OpenBook -> {
            clay(rr(6f, 30f, 88f, 54f, 10f), B.Indigo)
            clay(poly(12f, 24f, 48f, 32f, 48f, 78f, 12f, 72f), B.Paper, depth = 3f, round = 6f, gloss = false)
            clay(poly(88f, 24f, 52f, 32f, 52f, 78f, 88f, 72f), B.Paper, depth = 3f, round = 6f, gloss = false)
            for (i in 0..2) {
                val y = 42f + i * 9f
                line(B.Steel, 3f, Offset(20f, y - 2f), Offset(41f, y + 2f))
                line(B.Steel, 3f, Offset(80f, y - 2f), Offset(59f, y + 2f))
            }
        }
        Glyph.Calendar -> {
            clay(rr(12f, 22f, 76f, 64f, 12f), B.Paper)
            val head = Path().apply { addRoundRect(RoundRect(Rect(12f, 22f, 88f, 44f), topLeft = CornerRadius(12f), topRight = CornerRadius(12f))) }
            flat(head, B.Coral)
            clay(rr(28f, 12f, 9f, 18f, 4.5f), B.Ink, depth = 2f, gloss = false)
            clay(rr(63f, 12f, 9f, 18f, 4.5f), B.Ink, depth = 2f, gloss = false)
            for (row in 0..1) for (col in 0..2) {
                val x = 24f + col * 19f
                val y = 52f + row * 15f
                if (row == 1 && col == 2) clay(rr(x, y, 13f, 10f, 3f), B.Green, depth = 2f, gloss = false)
                else flat(rr(x, y, 13f, 10f, 3f), B.Steel.copy(alpha = 0.35f))
            }
        }
        Glyph.Timer -> {
            clay(rr(42f, 8f, 16f, 11f, 4f), B.Coral, depth = 3f)
            clay(rr(68f, 22f, 12f, 9f, 4f), B.Coral, depth = 3f)
            clay(circle(50f, 56f, 32f), B.Sky)
            flat(circle(50f, 56f, 24f), B.Paper)
            line(B.Ink, 5f, Offset(50f, 56f), Offset(50f, 40f))
            line(B.Coral, 5f, Offset(50f, 56f), Offset(62f, 62f))
            dot(B.Ink, 50f, 56f, 4f)
        }
        Glyph.Clipboard -> {
            clay(rr(16f, 16f, 68f, 72f, 12f), B.Violet)
            flat(rr(25f, 26f, 50f, 54f, 6f), B.Paper)
            clay(rr(36f, 10f, 28f, 14f, 6f), B.Steel, depth = 3f)
            for (i in 0..2) {
                val y = 38f + i * 14f
                line(B.Green, 4.5f, Offset(31f, y), Offset(35f, y + 4f), Offset(41f, y - 3f))
                line(B.Steel, 4f, Offset(48f, y + 1f), Offset(67f, y + 1f))
            }
        }
        Glyph.Notebook -> {
            clay(rr(20f, 10f, 60f, 76f, 10f), B.Green)
            flat(rr(20f, 10f, 13f, 76f, 6f), lerp(B.Green, B.Ink, 0.25f))
            flat(rr(42f, 26f, 28f, 14f, 4f), B.Paper)
            line(B.Steel, 2.5f, Offset(47f, 33f), Offset(64f, 33f))
            clay(poly(58f, 78f, 72f, 78f, 72f, 96f, 65f, 90f, 58f, 96f), B.Coral, depth = 2f, gloss = false)
        }
        Glyph.Bookmark -> clay(poly(28f, 12f, 72f, 12f, 72f, 86f, 50f, 70f, 28f, 86f), B.Coral, round = 8f)
        Glyph.Cap -> {
            clay(rr(28f, 42f, 44f, 28f, 10f), lerp(B.Indigo, B.Ink, 0.3f))
            clay(poly(50f, 16f, 92f, 36f, 50f, 54f, 8f, 36f), B.Indigo, depth = 5f, round = 6f)
            line(B.Amber, 4f, Offset(50f, 35f), Offset(80f, 44f), Offset(80f, 62f))
            dot(B.Amber, 80f, 65f, 5f)
        }
        Glyph.Spark -> {
            clay(star4(46f, 52f, 36f, 11f), B.Violet, depth = 5f, round = 4f)
            clay(star4(78f, 20f, 13f, 4f), B.Amber, depth = 3f, round = 2f)
            clay(star4(20f, 20f, 8f, 3f), B.Sky, depth = 2f, round = 2f, gloss = false)
        }
        Glyph.Check -> {
            clay(circle(50f, 50f, 38f), B.Green)
            line(B.Paper, 10f, Offset(32f, 51f), Offset(45f, 63f), Offset(68f, 38f))
        }
        Glyph.Flag -> {
            clay(rr(18f, 10f, 9f, 80f, 4.5f), B.Steel, depth = 3f)
            val flag = Path().apply {
                moveTo(27f, 14f); cubicTo(45f, 6f, 60f, 24f, 82f, 16f)
                lineTo(74f, 34f); lineTo(84f, 50f)
                cubicTo(62f, 58f, 46f, 40f, 27f, 50f); close()
            }
            clay(flag, B.Coral, depth = 5f, round = 5f)
        }
        Glyph.Lock -> {
            d.drawPath(arch(50f, 44f, 20f), dark(B.Steel), style = Stroke(10f, cap = StrokeCap.Round))
            d.drawPath(arch(50f, 41f, 20f), c(B.Steel), style = Stroke(9f, cap = StrokeCap.Round))
            clay(rr(16f, 40f, 68f, 46f, 12f), B.Amber)
            dot(B.Ink, 50f, 58f, 6f)
            line(B.Ink, 5f, Offset(50f, 60f), Offset(50f, 71f))
        }
        Glyph.Star -> clay(star5(50f, 52f, 40f, 18f), B.Amber, round = 8f)
        Glyph.Trophy -> {
            d.drawPath(ring(24f, 34f, 13f), dark(B.Amber), style = Stroke(7f))
            d.drawPath(ring(76f, 34f, 13f), dark(B.Amber), style = Stroke(7f))
            clay(rr(30f, 76f, 40f, 12f, 4f), B.Brown, depth = 3f)
            clay(rr(44f, 58f, 12f, 20f, 3f), dark(B.Amber, 0.1f), depth = 2f, gloss = false)
            val cup = Path().apply {
                moveTo(24f, 14f); lineTo(76f, 14f); lineTo(74f, 36f)
                cubicTo(72f, 54f, 62f, 62f, 50f, 62f); cubicTo(38f, 62f, 28f, 54f, 26f, 36f); close()
            }
            clay(cup, B.Amber, round = 4f)
            clay(star5(50f, 36f, 11f, 5f), B.Paper, depth = 2f, gloss = false)
        }
        Glyph.Medal -> {
            clay(poly(30f, 8f, 46f, 8f, 56f, 44f, 42f, 46f), B.Sky, depth = 2f, round = 3f, gloss = false)
            clay(poly(70f, 8f, 54f, 8f, 44f, 44f, 58f, 46f), B.Coral, depth = 2f, round = 3f, gloss = false)
            clay(circle(50f, 62f, 25f), B.Amber)
            clay(star5(50f, 62f, 12f, 5.5f), B.Paper, depth = 2f, gloss = false)
        }
        Glyph.Bulb -> {
            for (a in listOf(-60f, -25f, 25f, 60f)) {
                val r = Math.toRadians((a - 90).toDouble())
                line(B.Amber, 4f, Offset(50f + 38f * cos(r).toFloat(), 40f + 38f * sin(r).toFloat()), Offset(50f + 46f * cos(r).toFloat(), 40f + 46f * sin(r).toFloat()))
            }
            clay(rr(38f, 60f, 24f, 24f, 7f), B.Steel, depth = 3f)
            clay(circle(50f, 42f, 25f), B.Amber)
            line(B.Paper, 4f, Offset(43f, 50f), Offset(50f, 42f), Offset(57f, 50f))
        }
        Glyph.Warning -> {
            clay(poly(50f, 14f, 88f, 80f, 12f, 80f), B.Amber, round = 12f)
            line(B.Ink, 8f, Offset(50f, 38f), Offset(50f, 58f))
            dot(B.Ink, 50f, 70f, 4.5f)
        }
        Glyph.Fire -> {
            clay(flame(50f, 8f, 36f, 80f), B.Coral, round = 2f)
            clay(flame(50f, 38f, 20f, 50f), B.Amber, depth = 2f, gloss = false)
        }
        Glyph.Bolt -> clay(poly(58f, 8f, 24f, 54f, 46f, 54f, 40f, 90f, 76f, 42f, 54f, 42f), B.Amber, round = 6f)
        Glyph.Heart -> {
            val heart = Path().apply {
                moveTo(50f, 84f)
                cubicTo(14f, 60f, 6f, 40f, 16f, 26f)
                cubicTo(26f, 12f, 44f, 16f, 50f, 30f)
                cubicTo(56f, 16f, 74f, 12f, 84f, 26f)
                cubicTo(94f, 40f, 86f, 60f, 50f, 84f); close()
            }
            clay(heart, B.Pink)
        }
        Glyph.Bell -> {
            dot(B.Brown, 50f, 82f, 8f)
            val bell = Path().apply {
                moveTo(50f, 12f)
                cubicTo(30f, 12f, 24f, 30f, 24f, 48f)
                cubicTo(24f, 60f, 14f, 66f, 14f, 72f); lineTo(86f, 72f)
                cubicTo(86f, 66f, 76f, 60f, 76f, 48f)
                cubicTo(76f, 30f, 70f, 12f, 50f, 12f); close()
            }
            clay(bell, B.Amber, round = 4f)
            clay(circle(50f, 11f, 5f), B.Brown, depth = 2f, gloss = false)
        }
        Glyph.Chart -> {
            clay(rr(12f, 52f, 20f, 34f, 6f), B.Sky)
            clay(rr(40f, 34f, 20f, 52f, 6f), B.Indigo)
            clay(rr(68f, 14f, 20f, 72f, 6f), B.Green)
        }
        Glyph.Cycle -> {
            clay(circle(50f, 50f, 38f), B.Sky)
            d.drawArc(c(B.Paper), 200f, 230f, false, Offset(30f, 30f), Size(40f, 40f), style = Stroke(8f, cap = StrokeCap.Round))
            flat(poly(60f, 22f, 76f, 30f, 62f, 40f), B.Paper)
        }
        Glyph.Target -> {
            clay(circle(46f, 54f, 34f), B.Coral)
            flat(circle(46f, 54f, 24f), B.Paper)
            flat(circle(46f, 54f, 14f), B.Coral)
            flat(circle(46f, 54f, 5f), B.Paper)
            line(B.Ink, 5f, Offset(48f, 52f), Offset(82f, 18f))
            clay(poly(78f, 10f, 92f, 8f, 90f, 22f), B.Green, depth = 2f, gloss = false)
        }
        Glyph.Folder -> {
            clay(poly(10f, 20f, 38f, 20f, 46f, 28f, 90f, 28f, 90f, 80f, 10f, 80f), dark(B.Amber, 0.12f), depth = 2f, round = 8f, gloss = false)
            flat(rr(18f, 32f, 62f, 20f, 4f), B.Paper)
            clay(poly(8f, 42f, 92f, 42f, 88f, 84f, 12f, 84f), B.Amber, round = 8f)
        }
        Glyph.Gear -> {
            val gear = Path()
            for (i in 0 until 8) {
                val a = i * 45.0
                val t = Path().apply { addRoundRect(RoundRect(Rect(43f, 10f, 57f, 30f), CornerRadius(4f))) }
                val m = androidx.compose.ui.graphics.Matrix().apply { translate(50f, 50f); rotateZ(a.toFloat()); translate(-50f, -50f) }
                t.transform(m)
                gear.addPath(t)
            }
            gear.addOval(Rect(20f, 20f, 80f, 80f))
            clay(gear, B.Steel)
            clay(circle(50f, 50f, 13f), B.Paper, depth = -3f, gloss = false)
        }
        Glyph.Help -> {
            clay(bubble(), B.Sky)
            d.drawArc(c(B.Paper), 190f, 250f, false, Offset(40f, 22f), Size(20f, 20f), style = Stroke(7f, cap = StrokeCap.Round))
            line(B.Paper, 7f, Offset(51f, 42f), Offset(50f, 49f))
            dot(B.Paper, 50f, 59f, 4.5f)
        }
        Glyph.Person -> {
            clay(rr(18f, 54f, 64f, 34f, 18f), B.Indigo)
            clay(circle(50f, 32f, 18f), B.Amber)
        }
        Glyph.Shield -> {
            val sh = Path().apply {
                moveTo(50f, 10f); lineTo(84f, 22f)
                cubicTo(84f, 56f, 70f, 76f, 50f, 88f)
                cubicTo(30f, 76f, 16f, 56f, 16f, 22f); close()
            }
            clay(sh, B.Green, round = 6f)
            line(B.Paper, 8f, Offset(36f, 48f), Offset(46f, 58f), Offset(64f, 38f))
        }
        Glyph.Cards -> {
            d.rotate(-12f, Offset(50f, 50f)) { clay(rr(18f, 18f, 46f, 62f, 8f), B.Sky) }
            d.rotate(8f, Offset(50f, 50f)) {
                clay(rr(36f, 20f, 46f, 62f, 8f), B.Paper)
                flat(rr(43f, 30f, 32f, 12f, 4f), B.Indigo)
                line(B.Steel, 3.5f, Offset(44f, 54f), Offset(74f, 54f))
                line(B.Steel, 3.5f, Offset(44f, 64f), Offset(66f, 64f))
            }
        }
        Glyph.Dice -> {
            d.rotate(-8f, Offset(50f, 50f)) {
                clay(rr(16f, 16f, 66f, 66f, 16f), B.Coral)
                for ((x, y) in listOf(33f to 33f, 65f to 33f, 49f to 49f, 33f to 65f, 65f to 65f)) dot(B.Paper, x, y, 5.5f)
            }
        }
        Glyph.Play -> {
            clay(circle(50f, 50f, 38f), B.Coral)
            clay(poly(42f, 34f, 68f, 50f, 42f, 66f), B.Paper, depth = 2f, round = 6f, gloss = false)
        }
        Glyph.Cloud -> {
            val cl = Path().apply {
                addOval(Rect(14f, 42f, 50f, 78f)); addOval(Rect(30f, 22f, 74f, 66f)); addOval(Rect(52f, 40f, 88f, 76f))
                addRoundRect(RoundRect(Rect(30f, 52f, 72f, 78f), CornerRadius(4f)))
            }
            clay(cl, B.Sky)
        }
        Glyph.Doc -> {
            clay(poly(20f, 10f, 62f, 10f, 80f, 28f, 80f, 88f, 20f, 88f), B.Paper, round = 6f)
            flat(poly(62f, 10f, 80f, 28f, 62f, 28f), B.Steel.copy(alpha = 0.5f))
            flat(rr(28f, 38f, 30f, 8f, 4f), B.Sky)
            for (i in 0..2) line(B.Steel, 3.5f, Offset(30f, 56f + i * 10f), Offset(70f - i * 8f, 56f + i * 10f))
        }
        Glyph.Pencil -> d.rotate(45f, Offset(50f, 50f)) {
            clay(rr(38f, 12f, 24f, 14f, 5f), B.Pink, depth = 3f)
            clay(rr(38f, 24f, 24f, 46f, 3f), B.Amber)
            clay(poly(38f, 70f, 62f, 70f, 50f, 92f), Color(0xFFF1D2A8), depth = 2f, round = 2f, gloss = false)
            flat(poly(46f, 85f, 54f, 85f, 50f, 92f), B.Ink)
        }
        Glyph.Sun -> {
            for (i in 0 until 8) {
                val r = Math.toRadians(i * 45.0)
                line(B.Amber, 6f, Offset(50f + 32f * cos(r).toFloat(), 50f + 32f * sin(r).toFloat()), Offset(50f + 42f * cos(r).toFloat(), 50f + 42f * sin(r).toFloat()))
            }
            clay(circle(50f, 50f, 22f), B.Amber)
        }
        Glyph.Moon -> {
            val moon = Path().apply {
                moveTo(60f, 12f)
                cubicTo(30f, 14f, 14f, 40f, 22f, 62f)
                cubicTo(30f, 84f, 62f, 94f, 84f, 72f)
                cubicTo(56f, 74f, 40f, 44f, 60f, 12f); close()
            }
            clay(moon, B.Violet)
        }
        Glyph.Phone -> {
            clay(rr(26f, 8f, 48f, 82f, 12f), B.Ink)
            flat(rr(31f, 18f, 38f, 58f, 5f), B.Sky)
            dot(B.Steel, 50f, 82f, 3.5f)
        }
        Glyph.Gem -> {
            clay(poly(28f, 18f, 72f, 18f, 90f, 38f, 50f, 86f, 10f, 38f), B.Sky, round = 4f)
            line(B.Paper.copy(alpha = 0.7f), 3f, Offset(12f, 38f), Offset(88f, 38f))
            line(B.Paper.copy(alpha = 0.5f), 3f, Offset(38f, 18f), Offset(32f, 38f), Offset(50f, 84f))
        }
        Glyph.Brain -> {
            val br = Path().apply {
                addOval(Rect(12f, 28f, 50f, 72f)); addOval(Rect(50f, 28f, 88f, 72f))
                addOval(Rect(24f, 14f, 56f, 46f)); addOval(Rect(44f, 14f, 76f, 46f))
                addOval(Rect(26f, 50f, 74f, 84f))
            }
            clay(br, B.Pink)
            line(lerp(B.Pink, B.Ink, 0.35f), 3.5f, Offset(50f, 22f), Offset(50f, 80f))
            line(lerp(B.Pink, B.Ink, 0.35f), 3f, Offset(26f, 48f), Offset(38f, 52f), Offset(38f, 62f))
            line(lerp(B.Pink, B.Ink, 0.35f), 3f, Offset(74f, 48f), Offset(62f, 52f), Offset(62f, 62f))
        }
    }

    // ------------------------------------------------------------ formas

    private fun rr(x: Float, y: Float, w: Float, h: Float, r: Float) =
        Path().apply { addRoundRect(RoundRect(Rect(x, y, x + w, y + h), CornerRadius(r))) }

    private fun circle(cx: Float, cy: Float, r: Float) = Path().apply { addOval(Rect(cx - r, cy - r, cx + r, cy + r)) }

    private fun ring(cx: Float, cy: Float, r: Float) = circle(cx, cy, r)

    private fun poly(vararg v: Float) = Path().apply {
        moveTo(v[0], v[1])
        var i = 2
        while (i < v.size) { lineTo(v[i], v[i + 1]); i += 2 }
        close()
    }

    private fun arch(cx: Float, bottom: Float, r: Float) = Path().apply {
        moveTo(cx - r, bottom)
        lineTo(cx - r, bottom - r * 0.6f)
        arcTo(Rect(cx - r, bottom - r * 1.6f - r * 0.4f, cx + r, bottom - r * 0.6f + r * 0.4f), 180f, 180f, false)
        lineTo(cx + r, bottom)
    }

    private fun star5(cx: Float, cy: Float, outer: Float, inner: Float) = Path().apply {
        for (i in 0 until 10) {
            val r = if (i % 2 == 0) outer else inner
            val a = Math.toRadians(-90.0 + i * 36.0)
            val x = cx + r * cos(a).toFloat()
            val y = cy + r * sin(a).toFloat()
            if (i == 0) moveTo(x, y) else lineTo(x, y)
        }
        close()
    }

    private fun star4(cx: Float, cy: Float, r: Float, w: Float) = Path().apply {
        moveTo(cx, cy - r)
        quadraticTo(cx + w * 0.4f, cy - w * 0.4f, cx + r, cy)
        quadraticTo(cx + w * 0.4f, cy + w * 0.4f, cx, cy + r)
        quadraticTo(cx - w * 0.4f, cy + w * 0.4f, cx - r, cy)
        quadraticTo(cx - w * 0.4f, cy - w * 0.4f, cx, cy - r)
        close()
    }

    private fun flame(cx: Float, top: Float, halfW: Float, h: Float) = Path().apply {
        val bottom = top + h
        moveTo(cx, top)
        cubicTo(cx + halfW * 0.4f, top + h * 0.25f, cx + halfW, top + h * 0.38f, cx + halfW, top + h * 0.66f)
        cubicTo(cx + halfW, bottom - h * 0.1f, cx + halfW * 0.5f, bottom, cx, bottom)
        cubicTo(cx - halfW * 0.5f, bottom, cx - halfW, bottom - h * 0.1f, cx - halfW, top + h * 0.66f)
        cubicTo(cx - halfW, top + h * 0.45f, cx - halfW * 0.5f, top + h * 0.4f, cx - halfW * 0.3f, top + h * 0.25f)
        cubicTo(cx - halfW * 0.1f, top + h * 0.36f, cx, top + h * 0.2f, cx, top)
        close()
    }

    private fun bubble() = Path().apply {
        addRoundRect(RoundRect(Rect(10f, 12f, 90f, 72f), CornerRadius(18f)))
        moveTo(28f, 66f); lineTo(24f, 88f); lineTo(46f, 70f); close()
    }
}
