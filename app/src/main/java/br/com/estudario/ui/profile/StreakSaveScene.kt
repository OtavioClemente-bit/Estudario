package br.com.estudario.ui.profile

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val BRAZIER_W = 30
private const val BRAZIER_GAP = 4

/**
 * O Folha salva a sequência (o marca-página), na mesma montagem da comemoração da sequência: a fênix em
 * cima dormindo quase apagada, os braseiros da semana no meio (o último antes de HOJE é o dia que faltou,
 * soltando fumaça) e o Folha embaixo. Ele pede silêncio ("shhh…"), joga uma bola de fogo nesse
 * braseiro, que acende roxo e dourado, e o fogo sobe até a fênix, que acorda em labaredas.
 */
@Composable
fun StreakSaveScene(days: Int, left: Int, dayLabel: String, week: List<Pair<String, Int>> = emptyList(), onClose: () -> Unit) {
    val days7 = week.ifEmpty { listOf("T" to 1, "Q" to 1, "Q" to 1, "S" to 1, "S" to 1, "D" to 2, "S" to 0) }
    val savedI = days7.indexOfLast { it.second == 2 }.takeIf { it >= 0 } ?: (days7.size - 2).coerceAtLeast(0)
    var step by remember { mutableIntStateOf(0) }
    val rise = remember { Animatable(80f) }
    val ball = remember { Animatable(0f) }
    val jet = remember { Animatable(0f) }
    val wake = remember { Animatable(0f) }
    val ctx = androidx.compose.ui.platform.LocalContext.current
    LaunchedEffect(Unit) {
        // o mesmo som da comemoração da sequência
        br.com.estudario.ui.brand.EstudarioSom.play(ctx, "cena_sequencia")
        delay(500)
        rise.animateTo(0f, tween(600, easing = FastOutSlowInEasing))
        step = 1; delay(1500)                         // "shhh…"
        step = 2; ball.animateTo(1f, tween(650, easing = FastOutSlowInEasing))   // joga a chama
        step = 3; delay(250)                          // o braseiro pega fogo roxo e dourado
        jet.animateTo(1f, tween(450, easing = FastOutSlowInEasing))
        step = 4; launch { wake.animateTo(1f, tween(800, easing = FastOutSlowInEasing)) }
        delay(1000); step = 5
    }
    val loop = rememberInfiniteTransition(label = "cena")
    val flicker by loop.animateFloat(0.92f, 1.08f, infiniteRepeatable(tween(420), RepeatMode.Reverse), label = "chama")
    val zz by loop.animateFloat(0f, 1f, infiniteRepeatable(tween(1800)), label = "zz")
    val woke = wake.value
    Dialog(onDismissRequest = { if (step >= 5) onClose() }, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        Box(Modifier.fillMaxSize().background(Brush.radialGradient(listOf(if (woke > 0f) Color(0xFF3A1E5E) else Color(0xFF241A6E), Color(0xFF07051A)))), contentAlignment = Alignment.Center) {
            Column(Modifier.fillMaxWidth().padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Box(Modifier.fillMaxWidth().height(340.dp), contentAlignment = Alignment.TopCenter) {
                    // ---- a fênix (em cima, no meio)
                    Box(Modifier.size(180.dp), contentAlignment = Alignment.Center) {
                        Canvas(Modifier.size(180.dp)) {
                            val w = size.width; val h = size.height; val cx = w / 2; val cy = h * .58f
                            val open = 0.12f + 0.88f * woke
                            if (woke > 0f) drawCircle(Brush.radialGradient(listOf(Color(0x77FF8A2A), Color.Transparent), Offset(cx, cy), w * .6f), w * .6f, Offset(cx, cy))
                            for (side in listOf(-1f, 1f)) for (i in 0 until 8) {
                                val f = i / 7f
                                val ang = Math.toRadians((-90 + side * (10 + f * 72) * open).toDouble())
                                val len = h * (.30f + .17f * (1 - f)) * (0.5f + 0.5f * open) * (if (woke > 0f) flicker else 1f)
                                val ex = cx + (Math.cos(ang) * len).toFloat(); val ey = cy + (Math.sin(ang) * len).toFloat()
                                val p = Path().apply { moveTo(cx, cy); quadraticBezierTo(cx + side * len * .45f, cy - len * .12f, ex, ey) }
                                drawPath(p, if (woke > 0f) Color(0xFFFF8A1F).copy(alpha = .35f + .6f * woke) else Color(0xFF8E86C8).copy(alpha = .3f), style = Stroke((5f - f * 2.6f) * density, cap = StrokeCap.Round))
                                if (woke > 0f) drawPath(p, Color(0xFFFFF0B8).copy(alpha = .75f * woke), style = Stroke(1.5f * density, cap = StrokeCap.Round))
                            }
                            drawCircle(if (woke > 0f) Color(0xFFFFB13B) else Color(0xFF5E5698), w * .085f, Offset(cx, cy))
                        }
                        Text("$days", Modifier.offset(y = 22.dp).alpha(0.3f + 0.7f * woke).scale(1f + 0.25f * woke), color = Color(0xFFFFE6A0), fontWeight = FontWeight.Black, fontSize = 32.sp)
                        if (woke < 0.5f) for (j in 0 until 3) {
                            val u = (zz + j / 3f) % 1f
                            Text("z", Modifier.offset(x = (34 + u * 24).dp, y = (-18 - u * 46).dp).alpha((1 - u) * (1 - woke * 2)), color = Color(0xFFCFC8FF), fontWeight = FontWeight.Black, fontSize = (11 + u * 9).sp)
                        }
                    }
                    // ---- os braseiros da semana (no meio, em fila)
                    Row(Modifier.offset(y = 186.dp), horizontalArrangement = Arrangement.spacedBy(BRAZIER_GAP.dp)) {
                        days7.forEachIndexed { i, (letter, st) ->
                            val today = i == days7.lastIndex
                            Column(Modifier.width(BRAZIER_W.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                Canvas(Modifier.size(BRAZIER_W.dp, 34.dp)) {
                                    val w = size.width; val h = size.height
                                    val isSaved = i == savedI
                                    val on = when { isSaved -> if (step >= 3) 1f else 0f; st == 1 -> 0.5f + 0.5f * woke; else -> 0f }
                                    if (on > 0f) {
                                        drawCircle(Brush.radialGradient(listOf(if (isSaved) Color(0xAA3D9BFF) else Color(0x66FF7A1F), Color.Transparent), Offset(w / 2, h * .45f), w * .8f), w * .8f, Offset(w / 2, h * .45f))
                                        val fh = h * .6f * on * flicker
                                        val flame = Path().apply { moveTo(w / 2, h * .66f - fh); quadraticBezierTo(w * .92f, h * .52f, w / 2, h * .7f); quadraticBezierTo(w * .08f, h * .52f, w / 2, h * .66f - fh); close() }
                                        drawPath(flame, Brush.verticalGradient(if (isSaved) listOf(Color(0xFF9FE6FF), Color(0xFF2F8BFF), Color(0xFF1238FF)) else listOf(Color(0xFFFFD54A), Color(0xFFFF7A1F), Color(0xFFFF2E14))))
                                        // o miolo da chama, mais claro
                                        val core = Path().apply { moveTo(w / 2, h * .66f - fh * .55f); quadraticBezierTo(w * .68f, h * .6f, w / 2, h * .69f); quadraticBezierTo(w * .32f, h * .6f, w / 2, h * .66f - fh * .55f); close() }
                                        drawPath(core, if (isSaved) Color(0xFFF2FCFF) else Color(0xFFFFF6D8))
                                    } else if (isSaved) {
                                        for (j in 0 until 3) { val u = (zz + j / 3f) % 1f; drawCircle(Color(0xFFB4AFD2).copy(alpha = .4f * (1 - u)), (2f + u * 3f) * density / 2, Offset(w / 2 + kotlin.math.sin(j + zz * 6) * 2f, h * .6f - u * h * .5f)) }
                                    }
                                    // o braseiro: tripé, tigela de metal, brasas e borda com brilho
                                    drawLine(Color(0xFF1E1A3C), Offset(w * .3f, h * .8f), Offset(w * .2f, h), 1.6f * density, StrokeCap.Round)
                                    drawLine(Color(0xFF1E1A3C), Offset(w * .7f, h * .8f), Offset(w * .8f, h), 1.6f * density, StrokeCap.Round)
                                    val bowl = Path().apply { moveTo(w * .08f, h * .7f); cubicTo(w * .12f, h * .92f, w * .88f, h * .92f, w * .92f, h * .7f); close() }
                                    drawPath(bowl, Brush.horizontalGradient(listOf(Color(0xFF2A2550), Color(0xFF5E5792), Color(0xFF231F45))))
                                    drawOval(if (on > 0f) (if (isSaved) Color(0xFF7CC8FF) else Color(0xFFFFA040)).copy(alpha = .5f + .4f * on) else Color(0xFF15112E), Offset(w * .1f, h * .66f), androidx.compose.ui.geometry.Size(w * .8f, h * .09f))
                                    drawOval(Color(0xFF8C86C0), Offset(w * .08f, h * .655f), androidx.compose.ui.geometry.Size(w * .84f, h * .1f), style = Stroke(1.1f * density))
                                }
                                Text(if (today) "HOJE" else letter, color = when { i == savedI && step >= 3 -> Color(0xFF9FE6FF); st == 1 -> Color(0xFFFFE1AA); else -> Color.White.copy(alpha = .45f) }, fontSize = if (today) 8.sp else 10.sp, fontWeight = FontWeight.Black)
                            }
                        }
                    }
                    // a bola de fogo (do Folha até o braseiro) e o jato (do braseiro até a fênix)
                    val targetX = (savedI - (days7.size - 1) / 2f) * (BRAZIER_W + BRAZIER_GAP)
                    if (step == 2) {
                        val u = ball.value
                        val x = targetX * u; val y = 280f - (280f - 196f) * u - 60f * kotlin.math.sin(Math.PI * u).toFloat()
                        Box(Modifier.offset(x = x.dp, y = y.dp).size(18.dp).background(Brush.radialGradient(listOf(Color.White, Color(0xFF9FE6FF), Color(0xFF2F8BFF), Color.Transparent)), RoundedCornerShape(50)))
                    }
                    if (step == 3 || (step == 4 && woke < 0.3f)) {
                        val u = jet.value
                        val x = targetX * (1 - u); val y = 190f - 90f * u - 30f * kotlin.math.sin(Math.PI * u).toFloat()
                        Box(Modifier.offset(x = x.dp, y = y.dp).size(16.dp).background(Brush.radialGradient(listOf(Color.White, Color(0xFFFFB13B), Color.Transparent)), RoundedCornerShape(50)))
                    }
                    // ---- o Folha (embaixo, no meio)
                    Box(Modifier.align(Alignment.BottomCenter).offset(y = rise.value.dp)) {
                        br.com.estudario.ui.assistant.Folha(
                            100.dp,
                            mood = when {
                                step >= 4 -> br.com.estudario.ui.assistant.FolhaMood.HAPPY
                                step == 2 || step == 3 -> br.com.estudario.ui.assistant.FolhaMood.TALKING
                                else -> br.com.estudario.ui.assistant.FolhaMood.IDLE
                            },
                        )
                        val bubble = when { step == 1 -> "shhh…"; step >= 5 -> "sequência salva!"; else -> null }
                        if (bubble != null) Text(
                            bubble,
                            Modifier.align(Alignment.TopCenter).offset(y = (-26).dp).background(Color.White, RoundedCornerShape(12.dp)).padding(horizontal = 10.dp, vertical = 4.dp),
                            color = Color(0xFF1D1670), fontWeight = FontWeight.Black, fontSize = 14.sp,
                        )
                    }
                }
                Column(Modifier.alpha(if (step >= 5) 1f else 0f), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("SEQUÊNCIA SALVA" + if (dayLabel.isNotBlank()) " · $dayLabel" else "", color = Color(0xFFFFD25A), fontWeight = FontWeight.Black, fontSize = 12.sp, letterSpacing = 1.sp)
                    Text("Shhh… o Folha reacendeu a sua fênix", color = Color.White, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Black, textAlign = TextAlign.Center)
                    Text(
                        "Num dia sem estudo ela quase apagou, e o Folha usou um marca-página para acender de novo: são $days ${if (days == 1) "dia" else "dias"} de sequência. " +
                            if (left > 0) "Ainda sobra $left ${if (left == 1) "marca-página" else "marca-páginas"}." else "Estude 7 dias seguidos para ele guardar outro.",
                        color = Color.White.copy(alpha = .85f), textAlign = TextAlign.Center, style = MaterialTheme.typography.bodyMedium,
                    )
                    Button(onClick = onClose, modifier = Modifier.padding(top = 6.dp)) { Text("Bora continuar", fontWeight = FontWeight.Bold) }
                }
            }
        }
    }
}
