package br.com.estudario.ui.profile

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
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
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import br.com.estudario.time.isoDayOfWeek
import br.com.estudario.domain.StreakDay
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
 * A cena da sequência (a mesma do site): uma faísca acende, a chama da turma do Folha cresce aos
 * trancos com brasas subindo; o Folha, que estava ali, leva um susto e sai correndo; a chama se
 * assenta e o número de dias sobe contando; a semana acende dia a dia e hoje ganha o carimbo da
 * chama; por fim o Folha volta pelo canto, dando tchau. Aparece uma vez por dia, ao fechar a meta.
 */
@Composable
fun StreakCelebrationScreen(celebration: StreakCelebration, onClose: () -> Unit) {
    val summary = celebration.summary
    val density = LocalDensity.current
    val fire = remember { Animatable(0f) }
    val glow = remember { Animatable(0f) }
    val spark = remember { Animatable(0f) }
    val runX = remember { Animatable(0f) }
    val runY = remember { Animatable(0f) }
    val runTilt = remember { Animatable(0f) }
    val corner = remember { Animatable(0f) }
    val textIn = remember { Animatable(0f) }
    var runnerMood by remember { mutableStateOf(FolhaMood.IDLE) }
    var cornerMood by remember { mutableStateOf(FolhaMood.WAVE) }
    var shown by remember { mutableIntStateOf(0) }
    var weekLit by remember { mutableIntStateOf(0) }
    var stamped by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        launch { spark.animateTo(1f, tween(550)) }
        launch { delay(400); glow.animateTo(1f, tween(2100, easing = FastOutSlowInEasing)) }
        // A chama pega aos trancos: cresce, recua, cresce mais.
        launch {
            delay(350)
            listOf(0.2f to 160, 0.16f to 200, 0.42f to 280, 0.36f to 200, 0.8f to 320, 0.72f to 200, 1.14f to 340, 1f to 300).forEach { (v, ms) ->
                fire.animateTo(v, tween(ms, easing = FastOutSlowInEasing))
            }
        }
        // O Folha: olha, se assusta (pula para trás) e sai correndo.
        launch {
            delay(900); runnerMood = FolhaMood.TALKING
            launch { runY.animateTo(-26f, tween(180)); runY.animateTo(0f, spring(Spring.DampingRatioMediumBouncy)) }
            runTilt.animateTo(10f, tween(160)); runTilt.animateTo(0f, tween(200))
            delay(250)
            runTilt.animateTo(-14f, tween(140))
            launch { repeat(5) { runY.animateTo(-8f, tween(70)); runY.animateTo(0f, tween(70)) } }
            runX.animateTo(700f, tween(720, easing = LinearEasing))
        }
        delay(2050)
        launch { textIn.animateTo(1f, tween(500)) }
        val steps = summary.current.coerceIn(1, 30)
        for (i in 1..steps) { shown = summary.current * i / steps; delay((520L / steps).coerceIn(18, 120)) }
        shown = summary.current
        for (i in 1..summary.week.size) { weekLit = i; delay(110) }
        delay(150); stamped = true
        delay(250)
        corner.animateTo(1f, spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessLow))
        delay(1600); cornerMood = FolhaMood.HAPPY
    }

    val flicker = rememberInfiniteTransition(label = "flicker")
    val flick by flicker.animateFloat(0f, 1f, infiniteRepeatable(tween(650, easing = LinearEasing), RepeatMode.Reverse), label = "flick")
    val emberT by flicker.animateFloat(0f, 1f, infiniteRepeatable(tween(1600, easing = LinearEasing)), label = "ember")

    Dialog(onDismissRequest = onClose, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        BoxWithConstraints(Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(InkTop, InkMid, InkBottom)))) {
            val compact = maxHeight < 640.dp
            Column(
                Modifier.fillMaxSize().systemBarsPadding().padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                // O palco: a chama no meio, o Folha ao lado.
                Box(Modifier.fillMaxWidth().height(if (compact) 200.dp else 250.dp), contentAlignment = Alignment.Center) {
                    // Brilho e faísca.
                    Canvas(Modifier.fillMaxSize()) {
                        val c = Offset(size.width / 2, size.height * 0.55f)
                        val r = size.minDimension * 0.62f * (0.2f + 0.8f * glow.value) * (1f + 0.06f * flick)
                        drawCircle(Brush.radialGradient(listOf(FireGlow.copy(alpha = 0.55f * glow.value), Ember.copy(alpha = 0.15f * glow.value), Color.Transparent), c, r), r, c)
                        if (spark.value in 0.01f..0.99f) {
                            val sr = 12.dp.toPx() * (1f + 3f * spark.value)
                            drawCircle(Brush.radialGradient(listOf(Color.White.copy(alpha = 1f - spark.value), Color(0xFFFFD84A).copy(alpha = 1f - spark.value), Color.Transparent), Offset(c.x, size.height * 0.72f), sr), sr, Offset(c.x, size.height * 0.72f))
                        }
                        // Brasas subindo.
                        if (fire.value > 0.3f) repeat(12) { i ->
                            val p = (emberT + i / 12f) % 1f
                            val x = c.x + ((i * 29) % 90 - 45).dp.toPx() * (1f + 0.4f * p)
                            val y = size.height * 0.78f - p * size.height * 0.75f
                            drawCircle(Ember.copy(alpha = (1f - p) * 0.9f), (2.5f + i % 3).dp.toPx() * (1f - p * 0.6f), Offset(x, y))
                        }
                    }
                    BrandIcon(
                        Glyph.Fire,
                        Modifier.graphicsLayer {
                            transformOrigin = TransformOrigin(0.5f, 0.85f)
                            val f = fire.value
                            scaleX = f * (1f + 0.03f * flick); scaleY = f * (1f + 0.05f * (1f - flick))
                        },
                        size = if (compact) 130.dp else 160.dp,
                    )
                    // O Folha que se assusta e corre.
                    Folha(
                        if (compact) 78.dp else 92.dp,
                        mood = runnerMood,
                        modifier = Modifier.align(Alignment.BottomCenter).offset(x = if (compact) 100.dp else 120.dp)
                            .graphicsLayer {
                                translationX = with(density) { runX.value.dp.toPx() }
                                translationY = with(density) { runY.value.dp.toPx() }
                                rotationZ = runTilt.value
                                transformOrigin = TransformOrigin(0.5f, 1f)
                            },
                    )
                    if (shown > 0) Text(
                        "$shown",
                        Modifier.align(Alignment.BottomCenter),
                        fontSize = 60.sp, fontWeight = FontWeight.Black, color = Ember,
                    )
                }
                Column(Modifier.graphicsLayer { alpha = textIn.value; translationY = (1f - textIn.value) * 30f }, horizontalAlignment = Alignment.CenterHorizontally) {
                    Spacer(Modifier.height(10.dp))
                    Text("META DO DIA CUMPRIDA", color = Ember, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Black)
                    Text(
                        if (summary.current <= 1) "Sequência acesa!" else "${summary.current} dias seguidos!",
                        color = Color.White, fontSize = 30.sp, fontWeight = FontWeight.Black, textAlign = TextAlign.Center,
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        when {
                            summary.current <= 1 -> "Hoje começa a sua sequência. Volte amanhã para mantê-la acesa."
                            summary.current >= summary.best -> "É o seu recorde! Cada dia conta ponto na prova."
                            else -> "Faltam ${summary.best - summary.current} para bater o seu recorde de ${summary.best} dias."
                        },
                        color = Color.White.copy(alpha = 0.8f), style = MaterialTheme.typography.bodyMedium, textAlign = TextAlign.Center,
                    )
                    Spacer(Modifier.height(18.dp))
                    // A semana acende dia a dia; hoje (o último) ganha o carimbo da chama.
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                        summary.week.forEachIndexed { i, day -> WeekFlame(day, lit = i < weekLit, today = i == summary.week.lastIndex, stamped = stamped) }
                    }
                    if (celebration.xpToday > 0) {
                        Spacer(Modifier.height(14.dp))
                        Text(
                            "+${celebration.xpToday} XP hoje",
                            Modifier.clip(RoundedCornerShape(50)).background(Ember.copy(alpha = 0.18f)).padding(horizontal = 14.dp, vertical = 6.dp),
                            color = Color(0xFFFFD27A), fontWeight = FontWeight.Black,
                        )
                    }
                }
            }
            Button(
                onClick = onClose,
                modifier = Modifier.align(Alignment.BottomCenter).systemBarsPadding().padding(start = 24.dp, end = 140.dp, bottom = 24.dp).fillMaxWidth().heightIn(min = 52.dp)
                    .graphicsLayer { alpha = textIn.value },
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Ember, contentColor = InkMid),
            ) { Text("Bora continuar", fontWeight = FontWeight.Black) }
            // O Folha volta pelo canto, dando tchau.
            Folha(
                110.dp,
                mood = cornerMood,
                modifier = Modifier.align(Alignment.BottomEnd).systemBarsPadding().padding(end = 14.dp, bottom = 8.dp)
                    .graphicsLayer {
                        val k = corner.value
                        translationX = (1f - k) * size.width * 1.6f
                        translationY = (1f - k) * size.height * 0.4f
                        rotationZ = (1f - k) * 20f - 4f * k
                        alpha = if (k > 0.01f) 1f else 0f
                    },
            )
        }
    }
}

@Composable
private fun WeekFlame(day: StreakDay, lit: Boolean, today: Boolean, stamped: Boolean) {
    val pop = remember { Animatable(0f) }
    LaunchedEffect(lit) { if (lit) pop.animateTo(1f, spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessMedium)) }
    val stamp = remember { Animatable(0f) }
    LaunchedEffect(stamped) { if (stamped && today) stamp.animateTo(1f, spring(Spring.DampingRatioLowBouncy, Spring.StiffnessMediumLow)) }
    val label = if (today) "hoje" else listOf("S", "T", "Q", "Q", "S", "S", "D").getOrElse(day.date.isoDayOfWeek - 1) { "" }
    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(6.dp)) {
        val size = if (today) 42.dp else 34.dp
        Box(
            Modifier.size(size).graphicsLayer {
                val s = if (today && stamped) (2.4f - 1.4f * stamp.value) else if (day.done) (0.6f + 0.4f * pop.value) else 1f
                scaleX = s; scaleY = s
                rotationZ = if (today && stamped) (1f - stamp.value) * -25f else 0f
            }
                .clip(CircleShape)
                .background(if (day.done && (lit || today)) Ember else Color.White.copy(alpha = 0.08f))
                .border(2.dp, if (day.done) Ember else Color.White.copy(alpha = 0.18f), CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            if (day.done && (if (today) stamped else lit)) BrandIcon(Glyph.Fire, size = size * 0.78f)
        }
        Text(label, color = if (today) Ember else Color.White.copy(alpha = 0.6f), fontSize = 11.sp, fontWeight = FontWeight.Black)
    }
}
