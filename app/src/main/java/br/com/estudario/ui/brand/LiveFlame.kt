package br.com.estudario.ui.brand

import androidx.compose.animation.core.withInfiniteAnimationFrameNanos
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.translate
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sin

private val Ink = Color(0xFF26234A)

/**
 * A chama viva do Estudário (a mesma de web/src/jsMain/resources/flame.js, mesma conta): fogo
 * desenhado quadro a quadro, com línguas que tremulam, três camadas de cor, o contorno de caneta da
 * turma do Folha e o rosto do mascote (pisca, olha em volta, sorri). Nasce como uma brasinha girando
 * depois de [delay] segundos e cresce por [grow] segundos enquanto o giro desacelera.
 */
@Composable
fun LiveFlame(modifier: Modifier = Modifier, delay: Float = 0.3f, grow: Float = 2.2f) {
    val time by produceState(0f) {
        val start = withInfiniteAnimationFrameNanos { it }
        while (true) withInfiniteAnimationFrameNanos { value = (it - start) / 1_000_000_000f }
    }
    Canvas(modifier) {
        val t = time
        val k = ((t - delay) / grow).coerceIn(0f, 1f)
        val g = 1f - (1f - k).pow(3)
        val scaleNow = if (t < delay) 0f else 0.1f + 0.9f * g + sin(k * PI).toFloat() * 0.06f
        if (scaleNow <= 0f) return@Canvas
        val spin = (1f - g) * (1f - g) * 720f
        val w = size.width
        val h = size.height
        val cx = w / 2
        // brilho
        val r = min(w, h) * 0.5f * (0.35f + 0.65f * g)
        val cy = h * 0.62f
        drawCircle(
            Brush.radialGradient(
                0f to Color(0xFFFF8A1F).copy(alpha = 0.5f * g), 0.45f to Color(0xFFFFB421).copy(alpha = 0.16f * g), 1f to Color.Transparent,
                center = Offset(cx, cy), radius = r,
            ),
            r, Offset(cx, cy),
        )
        // brasas subindo
        if (g > 0.25f) repeat(18) { i ->
            val p = ((t * (0.5f + (i % 5) * 0.12f) + i * 0.37f) % 1f)
            val x = cx + ((i * 37) % 60 - 30) * (1f + p * 0.5f) * (w / 220f) + sin(t * 3 + i * 0.37f) * 6f
            val y = h * 0.86f - p * h * 0.8f
            drawCircle(Color(0xFFFFB421).copy(alpha = (1f - p) * 0.9f * g), (2 + i % 4) * (w / 220f) * (1f - p * 0.6f), Offset(x, y))
        }
        val unit = min(w / 128f, h / 150f) * scaleNow
        translate(cx, h * 0.9f - 55f * unit) {
            rotate(spin, pivot = Offset.Zero) {
                scale(unit, unit, pivot = Offset.Zero) {
                    translate(-50f, -108f + 55f) { drawFlame(t) }
                }
            }
        }
    }
}

/** Contorno de uma língua de fogo: gota alongada cujo topo balança com o tempo. */
private fun tongue(cx: Float, base: Float, w: Float, h: Float, t: Float, phase: Float, wild: Float): List<Offset> {
    val n = 28
    return List(n) { i ->
        val a = i.toFloat() / n * 2f * PI.toFloat()
        val up = max(0f, cos(a))
        val side = sin(a)
        var x = side * w * (1f - 0.62f * up * up)
        var y = if (cos(a) > 0) -cos(a) * h else -cos(a) * w * 0.82f
        val wob = sin(t * 7.3f + phase + up * 2.6f) * 0.5f + sin(t * 11.1f + phase * 1.7f + i) * 0.22f
        x += wob * w * 0.42f * up * wild
        y -= (sin(t * 9f + phase + i * 0.6f) * 0.5f + 0.5f) * h * 0.12f * up * up * wild
        Offset(cx + x, base + y - w * 0.82f)
    }
}

private fun smoothPath(pts: List<Offset>): Path {
    val path = Path()
    val n = pts.size
    fun mid(p: Offset, q: Offset) = Offset((p.x + q.x) / 2, (p.y + q.y) / 2)
    val m0 = mid(pts[n - 1], pts[0])
    path.moveTo(m0.x, m0.y)
    for (i in 0 until n) {
        val p = pts[i]
        val m = mid(p, pts[(i + 1) % n])
        path.quadraticTo(p.x, p.y, m.x, m.y)
    }
    path.close()
    return path
}

private fun DrawScope.drawFlame(t: Float) {
    val sway = sin(t * 2.2f) * 2.5f
    val side = { dx: Float, ph: Float -> smoothPath(tongue(50 + dx + sway * 0.6f, 100f, 10f, 30 + sin(t * 5 + ph) * 8, t, ph, 1.2f)) }
    val extras = listOf(side(-24f, 0.7f), side(25f, 2.4f))
    val outer = smoothPath(tongue(50 + sway, 108f, 34f, 74f, t, 0f, 1f))
    (extras + outer).forEach { drawPath(it, Color.White, style = Stroke(13f, join = StrokeJoin.Round)) }
    extras.forEach { drawPath(it, Color(0xFFFF8A2B)); drawPath(it, Ink, style = Stroke(4.5f, join = StrokeJoin.Round)) }
    drawPath(outer, Color(0xFFFF6A1F)); drawPath(outer, Ink, style = Stroke(5f, join = StrokeJoin.Round))
    drawPath(smoothPath(tongue(50 + sway * 0.8f, 104f, 25f, 56f, t, 1.3f, 0.85f)), Color(0xFFFFB421))
    drawPath(smoothPath(tongue(50 + sway * 0.6f, 102f, 15f, 34f, t, 2.1f, 0.6f)), Color(0xFFFFE58A))
    // o rosto: acompanha o balanço, pisca e olha em volta
    val look = sin(t * 0.9f) * (if (t % 5f > 3.6f) 1f else 0.4f)
    val fx = 50 + sway * 0.8f + look * 3
    val fy = 82f
    val blink = if (t % 3.4f > 3.25f) 0.15f else 1f
    drawOval(Color(0xFFFF8FA3).copy(alpha = 0.75f), Offset(fx - 13 - 4.6f, fy + 7 - 2.6f), Size(9.2f, 5.2f))
    drawOval(Color(0xFFFF8FA3).copy(alpha = 0.75f), Offset(fx + 13 - 4.6f, fy + 7 - 2.6f), Size(9.2f, 5.2f))
    listOf(fx - 8, fx + 8).forEach { ex ->
        drawOval(Ink, Offset(ex - 3.9f, fy - 5.2f * blink), Size(7.8f, 10.4f * blink))
        if (blink > 0.5f) drawCircle(Color.White, 1.6f, Offset(ex - 1.2f + look, fy - 2f))
    }
    val mouth = Path().apply { moveTo(fx - 6, fy + 8); quadraticTo(fx, fy + 17, fx + 6, fy + 8); close() }
    drawPath(mouth, Ink)
    drawOval(Color(0xFFFF8FA3), Offset(fx - 2.6f, fy + 12.5f - 1.3f), Size(5.2f, 2.6f))
}
