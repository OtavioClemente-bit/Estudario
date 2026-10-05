package br.com.estudario.ui.profile

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.withInfiniteAnimationFrameNanos
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import br.com.estudario.ui.AppViewModel
import kotlinx.coroutines.delay
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

private val Violet = Color(0xFF5B3FD6)
private val Emerald = Color(0xFF0E9F7A)
private val Confetti = listOf(Color(0xFF8B6CFF), Color(0xFF22E3C4), Color(0xFFFF4FD8), Color(0xFFFFC94D), Color(0xFF7FB2FF))

/**
 * "Subiu de nível!": o selo hexagonal entra crescendo, com raios girando e confete caindo. Quando
 * o nível traz título novo, ele aparece em destaque. Só abre por estudo de verdade (o ViewModel
 * não dispara ao abrir o app nem ao restaurar backup).
 */
@Composable
fun LevelUpScreen(event: AppViewModel.LevelUp, onClose: () -> Unit) {
    Dialog(onDismissRequest = onClose, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        val time by produceState(0f) {
            val start = withInfiniteAnimationFrameNanos { it }
            while (true) withInfiniteAnimationFrameNanos { value = (it - start) / 1_000_000_000f }
        }
        val pop = remember { Animatable(0f) }
        LaunchedEffect(Unit) { pop.animateTo(1f, spring(dampingRatio = 0.45f, stiffness = Spring.StiffnessLow)) }
        Box(Modifier.fillMaxSize().background(Color(0xF00B0A1A)), contentAlignment = Alignment.Center) {
            Canvas(Modifier.fillMaxSize()) { drawConfetti(time) }
            Column(
                Modifier.widthIn(max = 420.dp).padding(28.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                Text("SUBIU DE NÍVEL!", color = Color(0xFFCBB8FF), fontWeight = FontWeight.Black, fontSize = 15.sp, letterSpacing = 3.sp)
                Box(Modifier.size(260.dp).graphicsLayer { scaleX = pop.value; scaleY = pop.value }, contentAlignment = Alignment.Center) {
                    Canvas(Modifier.fillMaxSize()) { drawLevelSeal(time) }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("NÍVEL", color = Color.White.copy(alpha = 0.85f), fontWeight = FontWeight.Bold, fontSize = 14.sp, letterSpacing = 2.sp)
                        Text("${event.level}", color = Color.White, fontWeight = FontWeight.Black, fontSize = 72.sp)
                    }
                }
                if (event.newTitle) {
                    Text("Título novo", color = Color(0xFF6EE7C8), fontWeight = FontWeight.Bold, fontSize = 13.sp, letterSpacing = 1.sp)
                }
                Text(event.title, color = Color.White, fontWeight = FontWeight.Black, fontSize = 30.sp, textAlign = TextAlign.Center)
                Text(
                    "${"%,d".format(event.totalXp).replace(',', '.')} XP conquistados estudando. Cada nível é feito de dias de estudo de verdade.",
                    color = Color.White.copy(alpha = 0.75f),
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                )
                Spacer(Modifier.height(10.dp))
                Box(
                    Modifier.fillMaxWidth().clip(RoundedCornerShape(50))
                        .background(Brush.horizontalGradient(listOf(Violet, Emerald)))
                        .clickable(onClick = onClose)
                        .padding(vertical = 15.dp),
                    contentAlignment = Alignment.Center,
                ) { Text("Continuar", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp) }
            }
        }
    }
}

private fun hexPath(c: Offset, r: Float) = Path().apply {
    repeat(6) { i ->
        val a = (-90f + i * 60f) * PI.toFloat() / 180f
        val p = Offset(c.x + r * cos(a), c.y + r * sin(a))
        if (i == 0) moveTo(p.x, p.y) else lineTo(p.x, p.y)
    }
    close()
}

private fun DrawScope.drawLevelSeal(time: Float) {
    val c = center
    val r = size.minDimension / 2
    // Raios girando atrás do selo.
    rotate(time * 18f, c) {
        repeat(18) { i ->
            val a = i * (2f * PI.toFloat() / 18)
            val beam = Path().apply {
                moveTo(c.x, c.y)
                lineTo(c.x + cos(a - 0.06f) * r, c.y + sin(a - 0.06f) * r)
                lineTo(c.x + cos(a + 0.06f) * r, c.y + sin(a + 0.06f) * r)
                close()
            }
            drawPath(beam, Brush.radialGradient(listOf(Color(0xFFFFE9A3).copy(alpha = if (i % 2 == 0) 0.4f else 0.18f), Color.Transparent), c, r))
        }
    }
    drawCircle(Brush.radialGradient(listOf(Color(0xFF8B6CFF).copy(alpha = 0.6f), Color.Transparent), c, r * 0.8f), r * 0.8f, c, blendMode = BlendMode.Plus)
    val seal = r * 0.56f
    drawPath(hexPath(c + Offset(0f, 6.dp.toPx()), seal), Color.Black.copy(alpha = 0.35f))
    drawPath(hexPath(c, seal), Brush.linearGradient(listOf(Violet, Emerald), c - Offset(seal, seal), c + Offset(seal, seal)))
    drawPath(hexPath(c, seal), Color(0xFFE6DCFF), style = Stroke(4.dp.toPx()))
    drawPath(hexPath(c, seal * 0.86f), Color.White.copy(alpha = 0.25f), style = Stroke(1.5.dp.toPx()))
    // Brilho que passa pelo selo de tempos em tempos, recortado dentro do hexágono.
    val sweep = ((time * 0.6f) % 1.6f) - 0.3f
    clipPath(hexPath(c, seal)) { drawLine(
        Color.White.copy(alpha = 0.35f),
        Offset(c.x - seal + seal * 2 * sweep, c.y - seal),
        Offset(c.x - seal * 1.4f + seal * 2 * sweep, c.y + seal),
        10.dp.toPx(),
    ) }
}

/** Confete caindo e balançando, em loop, nas cores da marca. */
private fun DrawScope.drawConfetti(time: Float) {
    repeat(70) { i ->
        val speed = 0.16f + (i % 7) * 0.03f
        val y = (((time * speed) + i * 0.137f) % 1.15f) - 0.08f
        val x = ((i * 0.618f) % 1f) + sin(time * 1.6f + i) * 0.025f
        val at = Offset(x * size.width, y * size.height)
        rotate(time * 160f + i * 37f, at) {
            drawRect(
                Confetti[i % Confetti.size],
                topLeft = at - Offset(5f, 9f),
                size = androidx.compose.ui.geometry.Size(if (i % 3 == 0) 10f else 14f, if (i % 3 == 0) 10f else 22f),
            )
        }
    }
}

/**
 * "+N XP" que sobe e some no topo da tela, toda vez que a pessoa ganha XP fora da comemoração.
 */
@Composable
fun XpGainToast(gain: AppViewModel.XpGain?) {
    var visible by remember { mutableStateOf<AppViewModel.XpGain?>(null) }
    LaunchedEffect(gain?.id) {
        if (gain == null) return@LaunchedEffect
        visible = gain
        delay(1_800)
        visible = null
    }
    Box(Modifier.fillMaxSize().statusBarsPadding().padding(top = 64.dp), contentAlignment = Alignment.TopCenter) {
        AnimatedVisibility(
            visible = visible != null,
            enter = slideInVertically(tween(380, easing = FastOutSlowInEasing)) { it } + fadeIn(tween(250)),
            exit = slideOutVertically(tween(500)) { -it } + fadeOut(tween(500)),
        ) {
            val amount = visible?.amount ?: gain?.amount ?: 0
            Row(
                Modifier.clip(RoundedCornerShape(50))
                    .background(Brush.horizontalGradient(listOf(Violet, Emerald)))
                    .padding(horizontal = 18.dp, vertical = 9.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text("+$amount", color = Color.White, fontWeight = FontWeight.Black, fontSize = 18.sp)
                Spacer(Modifier.size(6.dp))
                Text("XP", color = Color(0xFFE6DCFF), fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
        }
    }
}
