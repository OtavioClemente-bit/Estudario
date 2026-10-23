package br.com.estudario.ui.profile

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
 * O marca-página do Folha: quando um dia sem estudo foi salvo, na volta a pessoa vê o Folha chegar
 * correndo, colocar o marca-página no dia que faltou e a chama da fênix reacender. Uma vez por dia salvo.
 */
@Composable
fun StreakSaveScene(days: Int, left: Int, dayLabel: String, onClose: () -> Unit) {
    var step by remember { mutableIntStateOf(0) }
    val folhaX = remember { Animatable(420f) }
    val markY = remember { Animatable(-260f) }
    val flame = remember { Animatable(0.65f) }
    LaunchedEffect(Unit) {
        delay(150); step = 1
        launch { folhaX.animateTo(0f, spring(dampingRatio = 0.55f, stiffness = Spring.StiffnessLow)) }
        delay(1000); step = 2
        markY.animateTo(0f, spring(dampingRatio = 0.45f, stiffness = Spring.StiffnessMediumLow))
        step = 3
        flame.animateTo(1.15f, tween(600, easing = FastOutSlowInEasing))
        delay(250); step = 4
    }
    val wobble = rememberInfiniteTransition(label = "chama")
    val sway by wobble.animateFloat(-4f, 4f, infiniteRepeatable(tween(700), RepeatMode.Reverse), label = "balanco")
    Dialog(onDismissRequest = { if (step >= 4) onClose() }, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        Box(
            Modifier.fillMaxSize().background(Brush.radialGradient(listOf(Color(0xFF281A78), Color(0xFF08061A)))),
            contentAlignment = Alignment.Center,
        ) {
            Column(Modifier.fillMaxWidth().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(18.dp)) {
                Box(Modifier.fillMaxWidth().height(220.dp)) {
                    // a chama da sequência: apagando no começo, reacende quando o marca-página cai
                    Box(Modifier.align(Alignment.CenterStart).offset(x = 8.dp).size(110.dp), contentAlignment = Alignment.Center) {
                        val lit = step >= 3
                        Canvas(Modifier.size(90.dp).scale(flame.value).rotate(sway).alpha(if (lit) 1f else 0.55f)) {
                            val w = size.width; val h = size.height
                            val outer = Path().apply {
                                moveTo(w * .52f, 0f); cubicTo(w * .6f, h * .2f, w * .95f, h * .38f, w * .9f, h * .66f)
                                cubicTo(w * .86f, h * .94f, w * .14f, h * .98f, w * .12f, h * .64f)
                                cubicTo(w * .1f, h * .44f, w * .3f, h * .36f, w * .38f, h * .26f); close()
                            }
                            drawPath(outer, Brush.verticalGradient(if (lit) listOf(Color(0xFFFFD84A), Color(0xFFFF9A1F), Color(0xFFFF4D2E)) else listOf(Color(0xFF9A9AB0), Color(0xFF5C5C70))))
                            val inner = Path().apply {
                                moveTo(w * .5f, h * .45f); cubicTo(w * .6f, h * .6f, w * .72f, h * .7f, w * .66f, h * .82f)
                                cubicTo(w * .6f, h * .95f, w * .38f, h * .95f, w * .34f, h * .8f); close()
                            }
                            drawPath(inner, Color(if (lit) 0xFFFFF4B8 else 0xFFB5B5C5))
                            if (lit && step >= 3) repeat(8) { i ->
                                val a = Math.toRadians(i * 45.0)
                                drawCircle(Color(0xFFFFD25A), 3.dp.toPx(), Offset(w / 2 + (Math.cos(a) * w * .62).toFloat(), h / 2 + (Math.sin(a) * h * .62).toFloat()), alpha = 0.7f)
                            }
                        }
                        Text("$days", Modifier.align(Alignment.BottomCenter), color = Color(0xFFFFD25A), fontWeight = FontWeight.Black, fontSize = 22.sp)
                    }
                    // o dia que faltou, onde o marca-página cai
                    Box(
                        Modifier.align(Alignment.Center).size(100.dp, 112.dp)
                            .background(if (step >= 3) Color(0x406E5CFF) else Color(0x14FFFFFF), RoundedCornerShape(18.dp))
                            .border(2.dp, if (step >= 3) Color(0xFFFFB421) else Color(0x4DFFFFFF), RoundedCornerShape(18.dp)),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(dayLabel, Modifier.align(Alignment.TopCenter).offset(y = (-24).dp), color = Color.White.copy(alpha = .85f), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        if (step >= 2) Canvas(Modifier.size(48.dp, 62.dp).offset(y = markY.value.dp)) {
                            val w = size.width; val h = size.height
                            val ribbon = Path().apply { moveTo(0f, 0f); lineTo(w, 0f); lineTo(w, h); lineTo(w / 2, h * .76f); lineTo(0f, h); close() }
                            drawPath(ribbon, Brush.linearGradient(listOf(Color(0xFF8B7BFF), Color(0xFF4A36E0))))
                            drawCircle(Brush.verticalGradient(listOf(Color(0xFFFFE38A), Color(0xFFFFB421))), w * .22f, Offset(w / 2, h * .36f))
                        }
                    }
                    // o Folha chegando correndo
                    Box(Modifier.align(Alignment.CenterEnd).offset(x = folhaX.value.dp)) {
                        br.com.estudario.ui.assistant.Folha(110.dp, mood = if (step >= 3) br.com.estudario.ui.assistant.FolhaMood.HAPPY else br.com.estudario.ui.assistant.FolhaMood.TALKING)
                    }
                }
                Column(Modifier.alpha(if (step >= 4) 1f else 0f), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("O FOLHA GUARDOU SEU LUGAR", color = Color(0xFFFFD25A), fontWeight = FontWeight.Black, fontSize = 12.sp, letterSpacing = 1.sp)
                    Text("Sua sequência de $days ${if (days == 1) "dia" else "dias"} continua!", color = Color.White, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Black, textAlign = TextAlign.Center)
                    Text(
                        "Num dia sem estudo, o Folha colocou um marca-página e a chama da fênix não apagou. " +
                            if (left > 0) "Ainda sobra $left ${if (left == 1) "marca-página" else "marca-páginas"}." else "Estude 7 dias seguidos para ganhar outro.",
                        color = Color.White.copy(alpha = .85f), textAlign = TextAlign.Center, style = MaterialTheme.typography.bodyMedium,
                    )
                    Button(onClick = onClose, modifier = Modifier.padding(top = 6.dp)) { Text("Bora continuar", fontWeight = FontWeight.Bold) }
                }
            }
        }
    }
}
