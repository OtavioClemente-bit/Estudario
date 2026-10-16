package br.com.estudario.ui.components

import androidx.compose.animation.core.withInfiniteAnimationFrameNanos
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import br.com.estudario.ui.assistant.Folha
import br.com.estudario.ui.assistant.FolhaMood
import br.com.estudario.ui.brand.BrandIcon
import br.com.estudario.ui.brand.Glyph
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

/*
 * O foguete do Estudário (o mesmo do site, rocket.js): desenhado no traço da turma, com borda de
 * adesivo e rostinho. RocketLaunch faz a decolagem do plano: o foguete sai do botão, sobe em curva
 * com fumaça, explode em anéis, confete e adesivos da turma, e o Folha comemora no meio da tela.
 */

private val INK = Color(0xFF26234A)

private fun path(vararg pts: Float): Path = Path().apply {
    moveTo(pts[0], pts[1]); var i = 2
    while (i < pts.size) { lineTo(pts[i], pts[i + 1]); i += 2 }
    close()
}

private val BODY = Path().apply { moveTo(50f, 7f); cubicTo(66f, 21f, 71f, 41f, 68f, 64f); lineTo(32f, 64f); cubicTo(29f, 41f, 34f, 21f, 50f, 7f); close() }
private val NOSE = Path().apply { moveTo(50f, 7f); cubicTo(57f, 13f, 61.5f, 19f, 63.5f, 25f); lineTo(36.5f, 25f); cubicTo(38.5f, 19f, 43f, 13f, 50f, 7f); close() }
private val FIN_L = path(33f, 49f, 19f, 66f, 21f, 77f, 34f, 67f)
private val FIN_R = path(67f, 49f, 81f, 66f, 79f, 77f, 66f, 67f)
private val NOZZLE = path(39f, 63f, 61f, 63f, 58f, 72f, 42f, 72f)
private val FLAME = Path().apply { moveTo(41f, 70f); quadraticTo(50f, 99f, 59f, 70f); close() }
private val FLAME_IN = Path().apply { moveTo(45.5f, 71f); quadraticTo(50f, 88f, 54.5f, 71f); close() }

/** Desenha o foguete ocupando um quadrado de [side] px com o canto em (0,0); [flame] de 0 a 1.5. */
fun DrawScope.drawRocket(side: Float, flame: Float = 1f) {
    val k = side / 100f
    withTransform({ scale(k, k, Offset.Zero) }) {
        val ink = Stroke(4.5f, cap = StrokeCap.Round, join = StrokeJoin.Round)
        if (flame > 0f) withTransform({ scale(1f, flame, Offset(50f, 70f)) }) {
            drawPath(FLAME, Color(0xFFFFB421)); drawPath(FLAME, INK, style = Stroke(3.5f, join = StrokeJoin.Round)); drawPath(FLAME_IN, Color(0xFFFFE07A))
        }
        val white = Stroke(13f, cap = StrokeCap.Round, join = StrokeJoin.Round)
        listOf(BODY, FIN_L, FIN_R, NOZZLE).forEach { drawPath(it, Color.White); drawPath(it, Color.White, style = white) }
        drawPath(FIN_L, Color(0xFF5B4BF5)); drawPath(FIN_L, INK, style = ink)
        drawPath(FIN_R, Color(0xFF5B4BF5)); drawPath(FIN_R, INK, style = ink)
        drawPath(NOZZLE, Color(0xFF6B6886)); drawPath(NOZZLE, INK, style = ink)
        drawPath(BODY, Color(0xFFFFFDF8)); drawPath(BODY, INK, style = ink)
        drawPath(NOSE, Color(0xFFFF5C4D)); drawPath(NOSE, INK, style = ink)
        drawCircle(Color(0xFF8FD3FF), 8.5f, Offset(50f, 37f)); drawCircle(INK, 8.5f, Offset(50f, 37f), style = ink)
        drawCircle(INK, 2.6f, Offset(44.5f, 50f)); drawCircle(INK, 2.6f, Offset(55.5f, 50f))
        drawArc(INK, 20f, 140f, false, Offset(46.5f, 51f), androidx.compose.ui.geometry.Size(7f, 5f), style = Stroke(2.8f, cap = StrokeCap.Round))
        drawCircle(Color(0xFFF4A6C0).copy(alpha = .8f), 2.2f, Offset(41f, 54f)); drawCircle(Color(0xFFF4A6C0).copy(alpha = .8f), 2.2f, Offset(59f, 54f))
    }
}

/** O foguete pequeno, para dentro de botão. */
@Composable
fun RocketIcon(modifier: Modifier = Modifier, size: androidx.compose.ui.unit.Dp = 28.dp) {
    Canvas(modifier.size(size)) { drawRocket(this.size.minDimension, flame = 0.8f) }
}

@Composable
private fun rememberSeconds(): Float {
    val t by produceState(0f) {
        val start = withInfiniteAnimationFrameNanos { it }
        while (true) withInfiniteAnimationFrameNanos { now -> value = (now - start) / 1_000_000_000f }
    }
    return t
}

private const val IGNITE = 0.38f
private const val FLIGHT = 1.15f
private const val BOOM = IGNITE + FLIGHT
private const val END = BOOM + 3.2f

/**
 * A decolagem por cima de tudo. [from] é o centro do foguete do botão, em pixels da tela.
 * Um toque pula a cena. Ao terminar, chama [onDone].
 */
@Composable
fun RocketLaunch(from: Offset, onDone: () -> Unit, line: String = "Decolamos! Seu plano está no ar.", sub: String = "Primeira parada: o estudo de hoje.") {
    val done by rememberUpdatedState(onDone)
    val t = rememberSeconds()
    LaunchedEffect(t >= END) { if (t >= END) done() }
    val confetti = remember {
        val r = java.util.Random(7)
        List(64) { Triple(r.nextFloat() * 2f * PI.toFloat(), 0.25f + r.nextFloat() * 0.6f, r.nextInt(8)) }
    }
    val colors = listOf(Color(0xFFFFD23F), Color(0xFFFFB421), Color(0xFF5B4BF5), Color(0xFF9D93FF), Color(0xFF22C59A), Color(0xFFFF5C4D), Color(0xFFF4A6C0), Color(0xFF8FD3FF))
    BoxWithConstraints(
        Modifier.fillMaxSize().clickable(remember { MutableInteractionSource() }, null) { done() },
    ) {
        val wPx = constraints.maxWidth.toFloat(); val hPx = constraints.maxHeight.toFloat()
        val end = Offset(wPx / 2, hPx * 0.40f)
        val ctrl = Offset(from.x + (end.x - from.x) * 0.15f, minOf(from.y, end.y) - hPx * 0.38f)
        fun at(p: Float): Offset { val u = 1 - p; return Offset(u * u * from.x + 2 * u * p * ctrl.x + p * p * end.x, u * u * from.y + 2 * u * p * ctrl.y + p * p * end.y) }
        fun angle(p: Float): Float { val u = 1 - p; val dx = 2 * u * (ctrl.x - from.x) + 2 * p * (end.x - ctrl.x); val dy = 2 * u * (ctrl.y - from.y) + 2 * p * (end.y - ctrl.y); return Math.toDegrees(atan2(dy, dx).toDouble()).toFloat() + 90f }
        val dimA = (t / 0.5f).coerceIn(0f, 1f) * (1f - ((t - (END - 0.4f)) / 0.4f).coerceIn(0f, 1f))
        Canvas(Modifier.fillMaxSize()) {
            drawRect(Color(0xFF110F26).copy(alpha = 0.6f * dimA))
            val side = 56.dp.toPx()
            // Fumaça: uma baforada a cada 30 ms do voo, no ponto em que o foguete passou.
            if (t > IGNITE) {
                var tk = IGNITE
                while (tk < minOf(t, BOOM)) {
                    val p = ((tk - IGNITE) / FLIGHT).let { it * it }
                    val age = t - tk
                    if (age < 0.9f) {
                        val pos = at(p); val back = Math.toRadians((angle(maxOf(p, .02f)) + 90f).toDouble())
                        val r = (8.dp.toPx() + p * 10.dp.toPx()) * (0.5f + age * 1.8f)
                        drawCircle(Color(0xFFE7E3FF).copy(alpha = (1f - age / 0.9f) * 0.9f), r, Offset(pos.x + cos(back).toFloat() * side * .45f, pos.y + sin(back).toFloat() * side * .45f + age * 18.dp.toPx()))
                    }
                    tk += 0.03f
                }
            }
            val jitter = 2.dp.toPx()
            when {
                t < IGNITE -> withTransform({ translate(from.x - side / 2 + sin(t * 90f) * jitter, from.y - side / 2); rotate(sin(t * 70f) * 4f, Offset(side / 2, side / 2)) }) { drawRocket(side, 0.8f + t * 1.5f) }
                t < BOOM -> {
                    val p = ((t - IGNITE) / FLIGHT).let { it * it }
                    val pos = at(p); val sc = 1f + p * 1.6f
                    withTransform({ translate(pos.x - side / 2, pos.y - side / 2); rotate(angle(maxOf(p, .02f)), Offset(side / 2, side / 2)); scale(sc, sc, Offset(side / 2, side / 2)) }) { drawRocket(side, 1.6f) }
                }
                else -> {
                    val age = t - BOOM
                    // Clarão e três anéis.
                    if (age < 0.55f) drawCircle(Color(0xFFFFE07A).copy(alpha = 1f - age / 0.55f), 60.dp.toPx() * (0.2f + age * 7f), end)
                    listOf(Color(0xFFFFD23F) to 0f, Color(0xFF5B4BF5) to 0.09f, Color(0xFFFF5C4D) to 0.17f).forEach { (c, d) ->
                        val a = age - d
                        if (a in 0f..0.9f) drawCircle(c.copy(alpha = 1f - a / 0.9f), 50.dp.toPx() * (0.3f + a * 5.5f), end, style = Stroke((10f - a * 9f).dp.toPx()))
                    }
                    // Confete com gravidade.
                    val maxD = maxOf(wPx, hPx) * 0.55f
                    confetti.forEachIndexed { i, (ang, dist, ci) ->
                        val a = age.coerceAtMost(2f)
                        val out = 1f - (1f - (a / 0.6f).coerceIn(0f, 1f)).let { it * it * it }
                        val x = end.x + cos(ang) * dist * maxD * out
                        val y = end.y + sin(ang) * dist * maxD * out + (a * a) * 160.dp.toPx() * 0.5f
                        val alpha = (1f - ((a - 1.2f) / 0.8f).coerceIn(0f, 1f))
                        withTransform({ rotate(a * 400f * (if (i % 2 == 0) 1 else -1), Offset(x, y)) }) {
                            drawRect(colors[ci].copy(alpha = alpha), Offset(x - 4.dp.toPx(), y - 6.dp.toPx()), androidx.compose.ui.geometry.Size(8.dp.toPx(), if (i % 3 == 0) 8.dp.toPx() else 13.dp.toPx()))
                        }
                    }
                }
            }
        }
        // Adesivos da turma voando da explosão.
        if (t > BOOM) {
            val age = t - BOOM
            val names = listOf(Glyph.Star, Glyph.Trophy, Glyph.Bolt, Glyph.Heart, Glyph.Check, Glyph.Fire, Glyph.Medal, Glyph.Gem, Glyph.Books, Glyph.Bulb)
            val density = androidx.compose.ui.platform.LocalDensity.current
            names.forEachIndexed { i, g ->
                val ang = i / names.size.toFloat() * 2f * PI.toFloat()
                val p = (age / 0.7f).coerceIn(0f, 1f).let { 1f - (1f - it) * (1f - it) }
                val dist = minOf(wPx, hPx) * 0.36f * p
                val x = end.x + cos(ang) * dist; val y = end.y + sin(ang) * dist + age * 30f
                val alpha = 1f - ((age - 1.1f) / 0.6f).coerceIn(0f, 1f)
                with(density) {
                    BrandIcon(g, Modifier.offset((x - 26.dp.toPx()).toDp(), (y - 26.dp.toPx()).toDp()).graphicsLayer { this.alpha = alpha; scaleX = 0.4f + p * 0.7f; scaleY = 0.4f + p * 0.7f; rotationZ = (i * 37 % 60 - 30).toFloat() }, size = 52.dp)
                }
            }
        }
        // O Folha comemorando, com a fala.
        if (t > BOOM + 0.26f) {
            val a = t - BOOM - 0.26f
            val pop = (a / 0.6f).coerceIn(0f, 1f).let { x -> val y = x - 1f; 1f + 2.4f * y * y * y + 1.4f * y * y }
            val fade = 1f - ((t - (END - 0.4f)) / 0.4f).coerceIn(0f, 1f)
            Column(
                Modifier.align(Alignment.Center).padding(bottom = 40.dp).graphicsLayer { scaleX = pop; scaleY = pop; alpha = fade },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Folha(190.dp, mood = FolhaMood.HAPPY)
                Surface(shape = RoundedCornerShape(22.dp), color = Color(0xFFFFFDF8), border = androidx.compose.foundation.BorderStroke(2.5.dp, INK), shadowElevation = 6.dp, modifier = Modifier.widthIn(max = 340.dp)) {
                    Column(Modifier.padding(horizontal = 20.dp, vertical = 14.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        val shown = ((a / 0.028f).toInt()).coerceIn(0, line.length)
                        Text(line.take(shown), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black, color = INK, textAlign = TextAlign.Center)
                        if (shown >= line.length) Text(sub, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = Color(0xFF4E4B6B), textAlign = TextAlign.Center)
                    }
                }
            }
        }
    }
}
