package br.com.estudario.ui.brand

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.estudario.data.sync.SyncGate
import br.com.estudario.ui.assistant.Folha
import br.com.estudario.ui.assistant.FolhaMood
import kotlinx.coroutines.delay
import kotlin.math.PI
import kotlin.math.sin

private val Ink1 = Color(0xFF2E2799)
private val Ink2 = Color(0xFF1B1464)
private val Gold = Color(0xFFFFC94D)
private val Mint = Color(0xFF2FD39A)

/**
 * "Sincronizando": ao abrir o app, enquanto o celular confere a conta, o Folha fica entre o celular e
 * o computador e passa as folhas de um para o outro (as que chegam ele engole e brilha). Só aparece
 * se a conferência demorar (nada de piscar quando é rápida) e fecha com "Tudo em dia".
 */
@Composable
fun SyncBanner() {
    val fresh by SyncGate.fresh.collectAsState()
    var phase by remember { mutableStateOf(0) } // 0 escondido, 1 sincronizando, 2 em dia
    LaunchedEffect(fresh) {
        if (!fresh) {
            delay(450)
            phase = 1
        } else if (phase == 1) {
            delay(500) // deixa a última folha chegar
            phase = 2
            delay(1300)
            phase = 0
        } else phase = 0
    }
    Box(Modifier.fillMaxSize().statusBarsPadding().padding(top = 8.dp), contentAlignment = Alignment.TopCenter) {
        AnimatedVisibility(
            visible = phase != 0,
            enter = slideInVertically(spring(dampingRatio = 0.62f, stiffness = 420f)) { -it * 2 } + fadeIn(tween(200)),
            exit = slideOutVertically(tween(320)) { -it * 2 } + fadeOut(tween(260)),
        ) { SyncCard(done = phase == 2) }
    }
}

@Composable
private fun SyncCard(done: Boolean) {
    val loop = rememberInfiniteTransition(label = "sync")
    val t by loop.animateFloat(0f, 1f, infiniteRepeatable(tween(1800, easing = LinearEasing)), label = "folhas")
    val dots by loop.animateFloat(0f, 3f, infiniteRepeatable(tween(1200, easing = LinearEasing)), label = "pontos")
    val glow by loop.animateFloat(0.35f, 0.9f, infiniteRepeatable(tween(900, easing = FastOutSlowInEasing), RepeatMode.Reverse), label = "brilho")
    // a folha chega ao Folha no meio de cada viagem: o "engolir" acompanha
    val gulp = if (done) 0f else (1f - kotlin.math.abs(((t * 2f) % 1f) - 0.5f) * 4f).coerceIn(0f, 1f)
    Column(
        Modifier.padding(horizontal = 16.dp).widthIn(max = 380.dp).fillMaxWidth()
            .shadow(18.dp, RoundedCornerShape(24.dp), ambientColor = Ink2, spotColor = Ink2)
            .background(Brush.linearGradient(listOf(Ink1, Ink2)), RoundedCornerShape(24.dp))
            .border(1.5.dp, Color.White.copy(alpha = 0.14f), RoundedCornerShape(24.dp))
            .padding(horizontal = 14.dp, vertical = 10.dp)
            .semantics { liveRegion = LiveRegionMode.Polite },
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(Modifier.fillMaxWidth().height(78.dp), contentAlignment = Alignment.Center) {
            Canvas(Modifier.fillMaxSize()) { drawRelay(t, glow, done) }
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                BrandIcon(Glyph.Phone, Modifier.padding(start = 4.dp), size = 38.dp)
                Folha(60.dp, mood = if (done) FolhaMood.PROUD else FolhaMood.TALKING, gulp = gulp, enter = false)
                BrandIcon(Glyph.Laptop, Modifier.padding(end = 4.dp), size = 40.dp)
            }
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (done) BrandIcon(Glyph.Check, size = 20.dp)
            Text(
                if (done) " Tudo em dia" else "Sincronizando seus estudos" + ".".repeat(dots.toInt() + 1).padEnd(3, ' '),
                color = Color.White, fontWeight = FontWeight.Black, fontSize = 15.sp,
            )
        }
        Text(
            if (done) "o que você fez no site já está aqui" else "o Folha está trazendo o que você fez no site",
            color = Color.White.copy(alpha = 0.72f), fontSize = 12.sp,
        )
    }
}

/** O arco tracejado entre os aparelhos e as folhas viajando: uma vai do celular ao computador, outra volta. */
private fun DrawScope.drawRelay(t: Float, glow: Float, done: Boolean) {
    val left = Offset(size.width * 0.13f, size.height * 0.5f)
    val right = Offset(size.width * 0.87f, size.height * 0.5f)
    val mid = Offset(size.width / 2f, size.height * 0.5f)
    val lift = size.height * 0.62f
    fun arc(a: Offset, b: Offset) = Path().apply {
        moveTo(a.x, a.y)
        quadraticTo((a.x + b.x) / 2f, a.y - lift, b.x, b.y)
    }
    val dash = PathEffect.dashPathEffect(floatArrayOf(6.dp.toPx(), 7.dp.toPx()), -t * 26.dp.toPx())
    val line = (if (done) Mint else Gold).copy(alpha = if (done) 0.8f else glow * 0.7f)
    drawPath(arc(left, mid), line, style = Stroke(2.dp.toPx(), cap = StrokeCap.Round, pathEffect = dash))
    drawPath(arc(mid, right), line, style = Stroke(2.dp.toPx(), cap = StrokeCap.Round, pathEffect = dash))
    if (done) return
    // ida: celular → Folha → computador; volta: computador → Folha → celular (meio ciclo de atraso)
    listOf(t to true, (t + 0.5f) % 1f to false).forEach { (p, forward) ->
        val firstLeg = p < 0.5f
        val k = (if (firstLeg) p else p - 0.5f) * 2f
        val (a, b) = when {
            forward && firstLeg -> left to mid
            forward -> mid to right
            firstLeg -> right to mid
            else -> mid to left
        }
        val x = (1 - k) * (1 - k) * a.x + 2 * (1 - k) * k * ((a.x + b.x) / 2f) + k * k * b.x
        val y = (1 - k) * (1 - k) * a.y + 2 * (1 - k) * k * (a.y - lift) + k * k * b.y
        val fade = sin(k * PI).toFloat().coerceIn(0f, 1f)
        drawSheet(Offset(x, y), (k - 0.5f) * (if (forward) 50f else -50f), 0.45f + 0.55f * fade)
    }
}

/** Uma folhinha de caderno: papel claro, pauta azul e margem vermelha. */
private fun DrawScope.drawSheet(at: Offset, angle: Float, alpha: Float) {
    val w = 15.dp.toPx(); val h = 19.dp.toPx()
    rotate(angle, at) {
        val topLeft = Offset(at.x - w / 2f, at.y - h / 2f)
        drawRoundRect(Color(0xFFFFFDF8).copy(alpha = alpha), topLeft, Size(w, h), CornerRadius(2.5.dp.toPx()))
        for (i in 1..3) {
            val y = topLeft.y + h * i / 4.2f
            drawLine(Color(0xFF7FB2FF).copy(alpha = alpha), Offset(topLeft.x + 3.dp.toPx(), y), Offset(topLeft.x + w - 2.dp.toPx(), y), 1.dp.toPx())
        }
        drawLine(Color(0xFFFF7A8A).copy(alpha = alpha), Offset(topLeft.x + 4.dp.toPx(), topLeft.y + 1.dp.toPx()), Offset(topLeft.x + 4.dp.toPx(), topLeft.y + h - 1.dp.toPx()), 1.dp.toPx())
    }
}
