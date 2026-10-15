package br.com.estudario.ui.profile

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import br.com.estudario.domain.StreakDay
import br.com.estudario.time.isoDayOfWeek
import br.com.estudario.ui.assistant.Folha
import br.com.estudario.ui.assistant.FolhaMood
import br.com.estudario.ui.brand.BrandIcon
import br.com.estudario.ui.brand.Button
import br.com.estudario.ui.brand.Glyph
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private val InkTop = Color(0xFF3B2FC9)
private val InkMid = Color(0xFF1D1670)
private val InkBottom = Color(0xFF110F26)
private val Ember = Color(0xFFFFB421)
private val FireGlow = Color(0xFFFF8A1F)

/**
 * A sequência do dia (a mesma cena do site): a chama acende. Uma faísca estala na base e o fogo pega
 * de baixo para cima (estica, passa do ponto, assenta) e fica vivo, com halo de calor e brasas. O
 * número rola como contador (ontem → hoje), os dias feitos da semana aparecem acesos e hoje pega fogo
 * junto com a virada. O Folha aparece no canto com um balão curto. Uma vez por dia, ao fechar a meta.
 */
@Composable
fun StreakCelebrationScreen(celebration: StreakCelebration, onClose: () -> Unit) {
    val summary = celebration.summary
    val spark = remember { Animatable(0f) }
    val igniteX = remember { Animatable(0f) }
    val igniteY = remember { Animatable(0f) }
    val glow = remember { Animatable(0f) }
    val roll = remember { Animatable(0f) }
    val todayLit = remember { Animatable(0f) }
    val textIn = remember { Animatable(0f) }
    val folhaIn = remember { Animatable(0f) }
    val bubble = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        launch { delay(200); spark.animateTo(1f, tween(450)) }
        // O fogo pega de baixo para cima: estica, passa do ponto e assenta.
        launch {
            delay(450)
            igniteY.animateTo(1f, keyframes { durationMillis = 950; 0f at 0; 0.9f at 210; 1.22f at 430; 0.88f at 590; 1.06f at 740; 1f at 950 })
        }
        launch {
            delay(450)
            igniteX.animateTo(1f, keyframes { durationMillis = 950; 0.08f at 0; 0.35f at 210; 0.85f at 430; 1.12f at 590; 0.96f at 740; 1f at 950 })
        }
        launch { delay(600); glow.animateTo(1f, tween(900)) }
        delay(1450)
        launch { roll.animateTo(1f, spring(0.45f, Spring.StiffnessMediumLow)) }
        launch { delay(100); todayLit.animateTo(1f, spring(0.4f, Spring.StiffnessMedium)) }
        launch { delay(250); textIn.animateTo(1f, tween(500)) }
        delay(550)
        folhaIn.animateTo(1f, spring(0.5f, Spring.StiffnessLow))
        bubble.animateTo(1f, spring(0.5f, Spring.StiffnessMedium))
    }

    // Depois de acesa: o corpo tremula num ritmo e o halo pulsa em outro.
    val life = rememberInfiniteTransition(label = "life")
    val flick by life.animateFloat(0f, 1f, infiniteRepeatable(tween(1700, easing = LinearEasing)), label = "flick")
    val haze by life.animateFloat(0.5f, 0.78f, infiniteRepeatable(tween(1050), RepeatMode.Reverse), label = "haze")
    val emberT by life.animateFloat(0f, 1f, infiniteRepeatable(tween(2600, easing = LinearEasing)), label = "ember")
    val wave = (kotlin.math.sin(flick * 2 * Math.PI) * 0.6 + kotlin.math.sin(flick * 4 * Math.PI + 1.3) * 0.4).toFloat()

    Dialog(onDismissRequest = onClose, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        BoxWithConstraints(Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(InkTop, InkMid, InkBottom)))) {
            val compact = maxHeight < 640.dp
            val flameSize = if (compact) 140.dp else 170.dp
            Column(
                Modifier.fillMaxSize().systemBarsPadding().padding(horizontal = 24.dp).padding(bottom = 90.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Box(Modifier.fillMaxWidth().height(if (compact) 200.dp else 236.dp), contentAlignment = Alignment.BottomCenter) {
                    // Brilho, faísca e brasas.
                    Canvas(Modifier.fillMaxSize()) {
                        val base = Offset(size.width / 2, size.height - 14.dp.toPx())
                        val r = size.minDimension * 0.75f
                        drawCircle(Brush.radialGradient(listOf(FireGlow.copy(alpha = 0.45f * glow.value), Ember.copy(alpha = 0.12f * glow.value), Color.Transparent), Offset(base.x, base.y - r * 0.3f), r), r, Offset(base.x, base.y - r * 0.3f))
                        if (spark.value in 0.01f..0.99f) {
                            val sr = 7.dp.toPx() * (1f + 4f * spark.value)
                            drawCircle(Brush.radialGradient(listOf(Color.White.copy(alpha = 1f - spark.value), Color(0xFFFFD84A).copy(alpha = 1f - spark.value), Color.Transparent), base, sr), sr, base)
                        }
                        if (glow.value > 0.5f) repeat(12) { i ->
                            val p = (emberT * (0.8f + (i % 4) * 0.15f) + i / 12f) % 1f
                            val x = base.x + ((i * 23) % 70 - 35).dp.toPx() * (1f + p * 0.5f)
                            val y = base.y - 60.dp.toPx() - p * 170.dp.toPx()
                            drawCircle(Color(0xFFFFC24A).copy(alpha = (if (p < 0.15f) p / 0.15f else 1f - p) * 0.95f), (3 + i % 3).dp.toPx() * 0.5f, Offset(x, y))
                        }
                    }
                    val fireLayer = Modifier.graphicsLayer {
                        transformOrigin = TransformOrigin(0.5f, 0.94f)
                        val lit = igniteY.value >= 1f && igniteX.value >= 1f
                        scaleX = igniteX.value * (if (lit) 1f + 0.03f * wave else 1f)
                        scaleY = igniteY.value * (if (lit) 1f - 0.04f * wave else 1f)
                        rotationZ = if (lit) 2f * wave else 0f
                    }
                    // Halo de calor: a mesma chama, desfocada, atrás.
                    BrandIcon(Glyph.Fire, Modifier.padding(bottom = 8.dp).then(fireLayer).graphicsLayer { alpha = haze; scaleX = 1.12f; scaleY = 1.12f }.blur(14.dp), size = flameSize)
                    BrandIcon(Glyph.Fire, Modifier.padding(bottom = 8.dp).then(fireLayer), size = flameSize)
                }
                // O contador: ontem embaixo, hoje entra rolando.
                val previous = (summary.current - 1).coerceAtLeast(0)
                Box(Modifier.height(74.dp).clip(RoundedCornerShape(4.dp)), contentAlignment = Alignment.TopCenter) {
                    Column(Modifier.graphicsLayer { translationY = -roll.value * 74.dp.toPx() }) {
                        listOf(previous, summary.current).forEach { n ->
                            Box(Modifier.height(74.dp), contentAlignment = Alignment.Center) {
                                Text("$n", fontSize = 64.sp, fontWeight = FontWeight.Black, color = Ember)
                            }
                        }
                    }
                }
                Column(Modifier.graphicsLayer { alpha = textIn.value; translationY = (1f - textIn.value) * 24f }, horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(if (summary.current == 1) "DIA DE SEQUÊNCIA" else "DIAS DE SEQUÊNCIA", color = Color.White.copy(alpha = 0.7f), fontWeight = FontWeight.Black, fontSize = 12.sp, letterSpacing = 1.5.sp)
                    Spacer(Modifier.height(4.dp))
                    Text(
                        when {
                            summary.current <= 1 -> "A chama acendeu"
                            summary.current >= summary.best -> "Recorde! Ninguém te para."
                            else -> "Mais um dia aceso"
                        },
                        color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Black, textAlign = TextAlign.Center,
                    )
                    Text(
                        when {
                            summary.current <= 1 -> "Volte amanhã para manter o fogo aceso."
                            summary.current >= summary.best -> "Sua maior sequência até agora. Amanhã tem mais."
                            else -> "Faltam ${summary.best - summary.current} para o seu recorde de ${summary.best} dias."
                        },
                        color = Color.White.copy(alpha = 0.8f), style = MaterialTheme.typography.bodyMedium, textAlign = TextAlign.Center,
                    )
                }
                Spacer(Modifier.height(16.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    summary.week.forEachIndexed { i, day -> WeekDay(day, today = i == summary.week.lastIndex, todayLit = todayLit.value) }
                }
                if (celebration.xpToday > 0) {
                    Spacer(Modifier.height(12.dp))
                    Text(
                        "+${celebration.xpToday} XP hoje",
                        Modifier.graphicsLayer { alpha = textIn.value }.clip(RoundedCornerShape(50)).background(Ember.copy(alpha = 0.18f)).padding(horizontal = 14.dp, vertical = 6.dp),
                        color = Color(0xFFFFD27A), fontWeight = FontWeight.Black,
                    )
                }
            }
            Button(
                onClick = onClose,
                modifier = Modifier.align(Alignment.BottomStart).systemBarsPadding().padding(start = 24.dp, bottom = 24.dp).fillMaxWidth(0.55f).heightIn(min = 52.dp)
                    .graphicsLayer { alpha = textIn.value },
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Ember, contentColor = InkMid),
            ) { Text("Bora continuar", fontWeight = FontWeight.Black) }
            // O Folha no canto, com um balão curto.
            Column(
                Modifier.align(Alignment.BottomEnd).systemBarsPadding().padding(end = 12.dp)
                    .graphicsLayer { translationY = (1f - folhaIn.value) * size.height * 1.2f },
                horizontalAlignment = Alignment.End,
            ) {
                Text(
                    if (summary.current <= 1) "Acendeu! Amanhã a gente mantém." else "${summary.current} dias! Tá pegando fogo!",
                    Modifier.widthIn(max = 180.dp)
                        .graphicsLayer { alpha = bubble.value; scaleX = 0.6f + 0.4f * bubble.value; scaleY = 0.6f + 0.4f * bubble.value; transformOrigin = TransformOrigin(1f, 1f) }
                        .clip(RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp, bottomStart = 18.dp, bottomEnd = 4.dp)).background(Color.White)
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    color = InkMid, fontWeight = FontWeight.Bold, fontSize = 14.sp,
                )
                Folha(96.dp, mood = FolhaMood.WAVE)
            }
        }
    }
}

@Composable
private fun WeekDay(day: StreakDay, today: Boolean, todayLit: Float) {
    val label = if (today) "hoje" else listOf("S", "T", "Q", "Q", "S", "S", "D").getOrElse(day.date.isoDayOfWeek - 1) { "" }
    val size = if (today) 44.dp else 36.dp
    val lit = day.done && (!today || todayLit > 0f)
    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(5.dp)) {
        Box(
            Modifier.size(size).clip(CircleShape)
                .background(if (lit) Ember.copy(alpha = if (today) 0.22f else 0.16f) else Color.White.copy(alpha = 0.06f))
                .border(2.dp, if (lit) Ember.copy(alpha = if (today) 1f else 0.6f) else Color.White.copy(alpha = 0.16f), CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            if (lit) BrandIcon(Glyph.Fire, Modifier.graphicsLayer { val s = if (today) todayLit else 1f; scaleX = s; scaleY = s }, size = size * 0.72f)
        }
        Text(label, color = if (today) Ember else Color.White.copy(alpha = 0.55f), fontSize = 11.sp, fontWeight = FontWeight.Black)
    }
}
