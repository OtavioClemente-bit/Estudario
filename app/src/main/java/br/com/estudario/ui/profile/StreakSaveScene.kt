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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * O Folha salva a sequência (o marca-página): a fênix dorme quase apagada, com "z" subindo; o Folha
 * chega na ponta dos pés, pede silêncio ("shhh…"), solta uma faísca e acende a chama ele mesmo. Aí a
 * fênix acorda em labaredas e ele sussurra "sequência salva!". Uma vez por dia salvo.
 */
@Composable
fun StreakSaveScene(days: Int, left: Int, dayLabel: String, week: List<Pair<String, Int>> = emptyList(), onClose: () -> Unit) {
    var step by remember { mutableIntStateOf(0) }
    val folhaX = remember { Animatable(260f) }
    val spark = remember { Animatable(0f) }
    val flame = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        delay(900); step = 1
        folhaX.animateTo(70f, tween(1400, easing = FastOutSlowInEasing))
        step = 2; delay(900)                 // "shhh…" para a gente
        step = 3; spark.animateTo(1f, tween(800, easing = FastOutSlowInEasing))
        step = 4; launch { flame.animateTo(1f, tween(700, easing = FastOutSlowInEasing)) }
        delay(1100); step = 5
    }
    val loop = rememberInfiniteTransition(label = "cena")
    val sway by loop.animateFloat(-3f, 3f, infiniteRepeatable(tween(650), RepeatMode.Reverse), label = "balanco")
    val zz by loop.animateFloat(0f, 1f, infiniteRepeatable(tween(1800)), label = "zz")
    val hop by loop.animateFloat(0f, 1f, infiniteRepeatable(tween(260), RepeatMode.Reverse), label = "passinho")
    Dialog(onDismissRequest = { if (step >= 5) onClose() }, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        Box(Modifier.fillMaxSize().background(Brush.radialGradient(listOf(Color(0xFF241A6E), Color(0xFF07051A)))), contentAlignment = Alignment.Center) {
            Column(Modifier.fillMaxWidth().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Box(Modifier.fillMaxWidth().height(300.dp)) {
                    // a fênix: labaredas em leque, apagada e dormindo até o Folha acender
                    val lit = flame.value
                    Box(Modifier.align(Alignment.TopCenter).size(190.dp), contentAlignment = Alignment.Center) {
                        Canvas(Modifier.size(190.dp).rotate(if (lit > 0.5f) sway else 0f)) {
                            val w = size.width; val h = size.height; val cx = w / 2; val cy = h * .55f
                            val open = 0.15f + 0.85f * lit
                            if (lit > 0f) drawCircle(Brush.radialGradient(listOf(Color(0x66FF8A2A), Color.Transparent), Offset(cx, cy), w * .55f), w * .55f, Offset(cx, cy))
                            for (side in listOf(-1f, 1f)) for (i in 0 until 7) {
                                val f = i / 6f
                                val ang = Math.toRadians((-90 + side * (12 + f * 70) * open).toDouble())
                                val len = h * (.30f + .16f * (1 - f)) * (0.55f + 0.45f * open)
                                val ex = cx + (Math.cos(ang) * len).toFloat(); val ey = cy + (Math.sin(ang) * len).toFloat()
                                val p = Path().apply { moveTo(cx, cy); quadraticBezierTo(cx + side * len * .5f, cy - len * .1f, ex, ey) }
                                val color = if (lit > 0f) Color(0xFFFF9A1F).copy(alpha = .35f + .6f * lit) else Color(0xFF8E86C8).copy(alpha = .35f)
                                drawPath(p, color, style = androidx.compose.ui.graphics.drawscope.Stroke(width = (5f - f * 2.5f) * density, cap = androidx.compose.ui.graphics.StrokeCap.Round))
                                if (lit > 0f) drawPath(p, Color(0xFFFFF0B8).copy(alpha = .7f * lit), style = androidx.compose.ui.graphics.drawscope.Stroke(width = 1.6f * density, cap = androidx.compose.ui.graphics.StrokeCap.Round))
                            }
                            // o corpo/peito
                            drawCircle(if (lit > 0f) Color(0xFFFFB13B).copy(alpha = .5f + .5f * lit) else Color(0xFF6E66A8), w * .09f, Offset(cx, cy))
                        }
                        Text("$days", Modifier.offset(y = 18.dp).alpha(0.35f + 0.65f * lit).scale(1f + 0.25f * lit), color = Color(0xFFFFE6A0), fontWeight = FontWeight.Black, fontSize = 34.sp)
                        // dormindo: "z" subindo
                        if (lit < 0.5f) for (j in 0 until 3) {
                            val u = (zz + j / 3f) % 1f
                            Text("z", Modifier.offset(x = (40 + u * 26).dp, y = (-20 - u * 50).dp).alpha((1 - u) * (1 - lit * 2)), color = Color(0xFFCFC8FF), fontWeight = FontWeight.Black, fontSize = (12 + u * 10).sp)
                        }
                    }
                    // os braseiros da semana: feitos com chama baixinha, o dia que faltou soltando fumaça,
                    // e quando o Folha acende a fênix esse braseiro também pega fogo (roxo e dourado)
                    if (week.isNotEmpty()) androidx.compose.foundation.layout.Row(
                        Modifier.align(Alignment.BottomStart).padding(start = 4.dp, bottom = 14.dp),
                        horizontalArrangement = Arrangement.spacedBy(2.dp),
                    ) {
                        week.forEach { (letter, st) ->
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Canvas(Modifier.size(24.dp, 30.dp)) {
                                    val w = size.width; val h = size.height
                                    val on = when (st) { 1 -> 0.55f + 0.45f * lit; 2 -> if (step >= 4) lit else 0f; else -> 0f }
                                    if (on > 0f) {
                                        val core = if (st == 2) Color(0xFFFFD25A) else Color(0xFFFFF0B8)
                                        val body = if (st == 2) Color(0xFF8B7BFF) else Color(0xFFFF9A1F)
                                        if (st == 2) drawCircle(Brush.radialGradient(listOf(Color(0x998B7BFF), Color.Transparent), Offset(w / 2, h * .45f), w * .7f), w * .7f, Offset(w / 2, h * .45f))
                                        val fh = h * .62f * on
                                        val flameP = Path().apply { moveTo(w / 2, h * .7f - fh); quadraticBezierTo(w * .9f, h * .55f, w / 2, h * .72f); quadraticBezierTo(w * .1f, h * .55f, w / 2, h * .7f - fh); close() }
                                        drawPath(flameP, body)
                                        drawCircle(core, w * .1f, Offset(w / 2, h * .62f))
                                    } else if (st == 2 || st == 0) {
                                        for (j in 0 until 3) { val u = (zz + j / 3f) % 1f; drawCircle(Color(0xFFB4AFD2).copy(alpha = .35f * (1 - u)), (2f + u * 3f) * density / 2, Offset(w / 2 + kotlin.math.sin(j + zz * 6) * 2f, h * .62f - u * h * .5f)) }
                                    }
                                    // a tigela
                                    val bowl = Path().apply { moveTo(w * .12f, h * .72f); quadraticBezierTo(w / 2, h * 1.02f, w * .88f, h * .72f); close() }
                                    drawPath(bowl, if (st == 2 && lit > 0f) Color(0xFF5A4AB0) else Color(0xFF3A3466))
                                }
                                Text(letter, color = if (st == 2 && lit > 0f) Color(0xFFFFD25A) else Color.White.copy(alpha = if (st == 1) .85f else .45f), fontSize = 10.sp, fontWeight = FontWeight.Black)
                            }
                        }
                    }
                    // a faísca saindo do dedo do Folha até o peito da fênix
                    if (step == 3 || (step == 4 && lit < 0.3f)) {
                        val u = spark.value
                        Box(Modifier.align(Alignment.TopCenter).offset(x = (folhaX.value * (1 - u) - 10).dp, y = (220 - 116 * u - 40 * kotlin.math.sin(Math.PI * u).toFloat()).dp).size(14.dp)
                            .background(Brush.radialGradient(listOf(Color.White, Color(0xFFFFD25A), Color.Transparent)), RoundedCornerShape(50)))
                    }
                    // o Folha, na ponta dos pés
                    Box(Modifier.align(Alignment.BottomCenter).offset(x = folhaX.value.dp, y = (if (step == 1) -6f * hop else 0f).dp)) {
                        br.com.estudario.ui.assistant.Folha(
                            104.dp,
                            mood = when {
                                step >= 4 -> br.com.estudario.ui.assistant.FolhaMood.HAPPY
                                step == 3 -> br.com.estudario.ui.assistant.FolhaMood.TALKING
                                else -> br.com.estudario.ui.assistant.FolhaMood.IDLE
                            },
                        )
                        // o balão: "shhh…" chegando, "sequência salva!" no fim
                        val bubble = when { step in 1..2 -> "shhh…"; step >= 5 -> "sequência salva!"; else -> null }
                        if (bubble != null) Text(
                            bubble,
                            Modifier.align(Alignment.TopCenter).offset(y = (-30).dp).background(Color.White, RoundedCornerShape(12.dp)).padding(horizontal = 10.dp, vertical = 4.dp),
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
