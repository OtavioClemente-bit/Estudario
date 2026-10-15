package br.com.estudario.ui.profile

import androidx.compose.animation.core.withInfiniteAnimationFrameNanos
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import br.com.estudario.domain.ProgressEngine
import br.com.estudario.ui.AppViewModel
import br.com.estudario.ui.assistant.Folha
import br.com.estudario.ui.assistant.FolhaMood
import br.com.estudario.ui.brand.Button
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.exp
import kotlin.math.pow
import kotlin.math.sin

private val InkTop = Color(0xFF3B2FC9)
private val InkMid = Color(0xFF1D1670)
private val InkBottom = Color(0xFF110F26)
private val Gold = Color(0xFFFFB421)
private val Pale = Color(0xFFFFE58A)

/** Metade de cima do número antigo (o corte é na diagonal). */
private val TopHalf: Shape = GenericShape { size, _ -> moveTo(0f, 0f); lineTo(size.width, 0f); lineTo(size.width, size.height * 0.38f); lineTo(0f, size.height * 0.66f); close() }
private val BottomHalf: Shape = GenericShape { size, _ -> moveTo(0f, size.height * 0.66f); lineTo(size.width, size.height * 0.38f); lineTo(size.width, size.height); lineTo(0f, size.height); close() }

private fun seg(t: Float, from: Float, to: Float) = ((t - from) / (to - from)).coerceIn(0f, 1f)
private fun easeIn(x: Float) = x * x * x
private fun easeOut(x: Float) = 1f - (1f - x).pow(3)
/** Mola: passa do ponto e volta. */
private fun springOut(x: Float) = if (x >= 1f) 1f else 1f - exp(-6f * x) * cos(x * 12f)

/**
 * A subida de nível, para impressionar (a mesma cena do site): o nível antigo aparece no anel, um corte
 * atravessa a tela com um clarão e o número se parte em dois, as metades caem girando e a tela treme;
 * o nível novo cai do alto com impacto, onda de choque, raios girando e estouro de partículas; a barra
 * de XP enche, o título aparece (com selo quando é título novo) e o Folha salta de baixo com os parabéns.
 */
@Composable
fun LevelUpScreen(event: AppViewModel.LevelUp, onClose: () -> Unit) {
    Dialog(onDismissRequest = onClose, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        val time by produceState(0f) {
            val start = withInfiniteAnimationFrameNanos { it }
            while (true) withInfiniteAnimationFrameNanos { value = (it - start) / 1_000_000_000f }
        }
        val t = time
        val old = (event.level - 1).coerceAtLeast(1)
        // Tremor no impacto do corte.
        val shake = seg(t, 0.62f, 1.12f).let { p -> if (p in 0.001f..0.999f) sin(p * 40f) * (1f - p) * 12f else 0f }
        Box(
            Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(InkTop, InkMid, InkBottom)))
                .graphicsLayer { translationX = shake; translationY = shake * 0.6f },
        ) {
            // Clarão do corte.
            val flash = seg(t, 0.6f, 1.1f).let { if (it <= 0f) 0f else if (it < 0.15f) it / 0.15f * 0.85f else (1f - it) * 0.85f / 0.85f }
            Column(
                Modifier.fillMaxSize().systemBarsPadding().padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Box(Modifier.fillMaxWidth().height(300.dp), contentAlignment = Alignment.Center) {
                    // Raios, onda de choque e partículas (depois do impacto).
                    Canvas(Modifier.fillMaxSize()) {
                        val c = Offset(size.width / 2, size.height / 2)
                        val rays = seg(t, 1.6f, 2.2f)
                        if (rays > 0f) rotate((t - 1.6f) * 26f, c) {
                            repeat(16) { i ->
                                val a = i * (2 * PI / 16).toFloat()
                                val r = size.minDimension * 0.75f
                                val path = androidx.compose.ui.graphics.Path().apply {
                                    moveTo(c.x, c.y)
                                    lineTo(c.x + cos(a - 0.07f) * r, c.y + sin(a - 0.07f) * r)
                                    lineTo(c.x + cos(a + 0.07f) * r, c.y + sin(a + 0.07f) * r)
                                    close()
                                }
                                drawPath(path, Brush.radialGradient(listOf(Pale.copy(alpha = 0.28f * rays), Color.Transparent), c, r))
                            }
                        }
                        val shock = seg(t, 1.6f, 2.4f)
                        if (shock in 0.001f..0.999f) drawCircle(Pale.copy(alpha = 0.9f * (1f - shock)), 95.dp.toPx() * (0.8f + 1.8f * shock), c, style = Stroke((6f * (1f - shock) + 1f).dp.toPx()))
                        val burst = seg(t, 1.6f, 2.5f)
                        if (burst in 0.001f..0.999f) repeat(16) { i ->
                            val a = i * (2 * PI / 16).toFloat()
                            val d = (95 + (i % 4) * 22).dp.toPx() * 2.2f * easeOut(burst)
                            drawRoundRect(
                                listOf(Gold, Color(0xFF7EE0B8), Color(0xFFABA2FF), Color(0xFFFF8FA3))[i % 4].copy(alpha = 1f - burst),
                                topLeft = Offset(c.x + cos(a) * d - 6, c.y + sin(a) * d - 6),
                                size = androidx.compose.ui.geometry.Size(12f * (1.2f - burst), 12f * (1.2f - burst)),
                            )
                        }
                        // O corte: um risco de luz atravessando.
                        val slash = seg(t, 0.45f, 0.83f)
                        if (slash in 0.001f..0.999f) rotate(-16f, c) {
                            val x0 = size.width * (-0.6f + 1.2f * slash)
                            drawLine(Brush.horizontalGradient(listOf(Color.Transparent, Color.White, Pale, Color.Transparent), x0 - size.width * 0.5f, x0 + size.width * 0.5f), Offset(x0 - size.width * 0.5f, c.y), Offset(x0 + size.width * 0.5f, c.y), 6.dp.toPx())
                        }
                    }
                    // O nível antigo, partido ao meio.
                    if (t < 1.6f) {
                        val p = easeIn(seg(t, 0.62f, 1.52f))
                        val appear = easeOut(seg(t, 0f, 0.4f))
                        OldHalf(old, TopHalf, Modifier.graphicsLayer { scaleX = appear; scaleY = appear; translationX = -150.dp.toPx() * p - 10 * seg(t, 0.62f, 0.8f); translationY = 420.dp.toPx() * p - 14 * seg(t, 0.62f, 0.8f); rotationZ = -70f * p; alpha = 1f - p })
                        OldHalf(old, BottomHalf, Modifier.graphicsLayer { scaleX = appear; scaleY = appear; translationX = 160.dp.toPx() * p + 10 * seg(t, 0.62f, 0.8f); translationY = 440.dp.toPx() * p + 10 * seg(t, 0.62f, 0.8f); rotationZ = 80f * p; alpha = 1f - p })
                    }
                    // O novo caindo do alto, com impacto.
                    if (t >= 1.05f) {
                        val drop = easeIn(seg(t, 1.05f, 1.6f))
                        val settle = springOut(seg(t, 1.6f, 2.1f))
                        val s = if (t < 1.6f) 2.4f - 1.22f * drop else 1.18f - 0.18f * settle
                        Box(
                            Modifier.size(190.dp).graphicsLayer {
                                translationY = if (t < 1.6f) (-420).dp.toPx() * (1f - drop) else 0f
                                scaleX = s; scaleY = if (t < 1.6f) s else s * (0.82f + 0.18f * settle).coerceAtMost(1.05f)
                            },
                            contentAlignment = Alignment.Center,
                        ) {
                            Canvas(Modifier.fillMaxSize()) {
                                rotate((t * 70f) % 360f) {
                                    drawCircle(Brush.sweepGradient(listOf(Gold, Color(0xFF7EE0B8), Color(0xFFABA2FF), Color(0xFFFF8FA3), Gold)), size.minDimension / 2)
                                }
                                drawCircle(Color(0xFF2E2799), size.minDimension / 2 * 0.86f)
                            }
                            Text("${event.level}", fontSize = 104.sp, fontWeight = FontWeight.Black, color = Gold)
                        }
                    }
                }
                val textIn = easeOut(seg(t, 1.7f, 2.2f))
                Column(Modifier.graphicsLayer { alpha = textIn; translationY = (1f - textIn) * 24f }, horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("SUBIU DE NÍVEL", color = Gold, fontWeight = FontWeight.Black, fontSize = 13.sp)
                    Text("Nível ${event.level} · ${event.title}", color = Color.White, fontWeight = FontWeight.Black, fontSize = 28.sp, textAlign = TextAlign.Center)
                    if (event.newTitle) Text(
                        "Novo título!",
                        Modifier.padding(top = 8.dp).clip(RoundedCornerShape(50)).background(Gold).padding(horizontal = 14.dp, vertical = 6.dp),
                        color = InkMid, fontWeight = FontWeight.Black,
                    )
                    Spacer(Modifier.height(12.dp))
                    // A barra de XP enchendo.
                    val fill = easeOut(seg(t, 2.1f, 3.3f))
                    Box(Modifier.fillMaxWidth(0.8f).height(26.dp).clip(RoundedCornerShape(50)).background(Color.White.copy(alpha = 0.12f)), contentAlignment = Alignment.Center) {
                        Box(Modifier.align(Alignment.CenterStart).fillMaxHeight().fillMaxWidth(0.08f + 0.92f * fill).clip(RoundedCornerShape(50)).background(Brush.horizontalGradient(listOf(Color(0xFF5B4BF5), Gold))))
                        Text("${event.totalXp} XP", color = Color.White, fontWeight = FontWeight.Black, fontSize = 13.sp)
                    }
                }
            }
            if (flash > 0f) Box(Modifier.fillMaxSize().background(Color.White.copy(alpha = flash.coerceIn(0f, 1f))))
            // O Folha salta de baixo com os parabéns.
            val folhaIn = springOut(seg(t, 2.3f, 3.1f))
            val bubble = easeOut(seg(t, 2.8f, 3.2f))
            Row(
                Modifier.align(Alignment.BottomStart).systemBarsPadding().padding(start = 12.dp, bottom = 84.dp)
                    .graphicsLayer { translationY = (1f - folhaIn) * 300.dp.toPx() },
                verticalAlignment = Alignment.Bottom,
            ) {
                val hop = if (t > 3.1f) (sin((t - 3.1f) * 12f).coerceAtLeast(0f) * 14f * exp(-(t - 3.1f))) else 0f
                Folha(110.dp, mood = if (t < 2.8f) FolhaMood.HAPPY else if (t < 5f) FolhaMood.TALKING else FolhaMood.WAVE, modifier = Modifier.graphicsLayer { translationY = -hop.dp.toPx() })
                Text(
                    "Parabéns! Você chegou ao nível ${event.level}. Esse esforço tá aparecendo!",
                    Modifier.padding(bottom = 60.dp).widthIn(max = 220.dp)
                        .graphicsLayer { alpha = bubble; scaleX = 0.6f + 0.4f * bubble; scaleY = 0.6f + 0.4f * bubble; transformOrigin = androidx.compose.ui.graphics.TransformOrigin(0f, 1f) }
                        .clip(RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp, bottomEnd = 18.dp, bottomStart = 4.dp)).background(Color.White).padding(horizontal = 14.dp, vertical = 10.dp),
                    color = InkMid, fontWeight = FontWeight.Bold, fontSize = 14.sp,
                )
            }
            Button(
                onClick = onClose,
                modifier = Modifier.align(Alignment.BottomCenter).systemBarsPadding().padding(24.dp).fillMaxWidth().heightIn(min = 52.dp)
                    .graphicsLayer { alpha = easeOut(seg(t, 3.4f, 3.9f)) },
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Gold, contentColor = InkMid),
            ) { Text("Continuar", fontWeight = FontWeight.Black) }
        }
    }
}

@Composable
private fun OldHalf(level: Int, shape: Shape, modifier: Modifier) {
    Box(modifier.size(190.dp).clip(shape), contentAlignment = Alignment.Center) {
        Box(Modifier.fillMaxSize().clip(CircleShape).background(Brush.sweepGradient(listOf(Color(0xFFABA2FF), Color(0xFF5B4BF5), Color(0xFFABA2FF)))))
        Box(Modifier.fillMaxSize(0.86f).clip(CircleShape).background(Color(0xFF2E2799)))
        Text("$level", fontSize = 104.sp, fontWeight = FontWeight.Black, color = Color.White)
    }
}

@Suppress("unused")
private fun titleOf(level: Int) = ProgressEngine.levelTitle(level)
