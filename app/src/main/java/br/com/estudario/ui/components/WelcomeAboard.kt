package br.com.estudario.ui.components

import androidx.compose.animation.core.withInfiniteAnimationFrameNanos
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import br.com.estudario.ui.assistant.Folha
import br.com.estudario.ui.assistant.FolhaMood
import kotlin.math.PI
import kotlin.math.sin

/*
 * Boas-vindas de quem acabou de assinar, em três atos, no traço do caderno:
 *  1. o carimbo: uma página de caderno entra e o carimbo "ASSINANTE <PLANO>" bate nela, tremendo a tela;
 *  2. a decolagem: o foguete do plano sai do carimbo, explode em confete e o Folha agradece;
 *  3. a turma: vários Folhas sobem da borda de baixo, pulando, e o grande chama para estudar já.
 * Um toque pula para o fim; "Bora estudar" fecha e leva ao estudo de hoje.
 */

private val INK = Color(0xFF26234A)
private val PAPER = Color(0xFFFFFDF8)
private val STAMP_ESSENCIAL = Color(0xFF2F5BD3)
private val STAMP_PRO = Color(0xFFE0452F)

@Composable
private fun seconds(): Float {
    val t by produceState(0f) {
        val start = withInfiniteAnimationFrameNanos { it }
        while (true) withInfiniteAnimationFrameNanos { now -> value = (now - start) / 1_000_000_000f }
    }
    return t
}

/** Mola com um passo além do alvo (0 a 1 vira 0 a ~1.1 e volta a 1). */
private fun pop(x: Float): Float { val c = x.coerceIn(0f, 1f); val y = c - 1f; return 1f + 2.6f * y * y * y + 1.6f * y * y }

private const val STAMP_IN = 0.35f
private const val STAMP_HIT = 0.95f
private const val ROCKET_AT = 1.9f

@Composable
fun WelcomeAboard(planName: String, pro: Boolean, onStudy: () -> Unit, onClose: () -> Unit) {
    var act by remember { mutableStateOf(1) }
    Dialog(onDismissRequest = onClose, properties = DialogProperties(usePlatformDefaultWidth = false, decorFitsSystemWindows = false)) {
        Box(Modifier.fillMaxSize()) {
            when (act) {
                1 -> StampAct(planName, pro) { act = 2 }
                2 -> {
                    StampAct(planName, pro, still = true) {}
                    BoxWithConstraints(Modifier.fillMaxSize()) {
                        val w = constraints.maxWidth.toFloat(); val h = constraints.maxHeight.toFloat()
                        RocketLaunch(
                            from = Offset(w / 2, h * 0.52f),
                            onDone = { act = 3 },
                            line = "Valeu demais por acreditar!",
                            sub = "Agora você é $planName. A turma toda já sabe.",
                        )
                    }
                }
                else -> CrewAct(planName, pro, onStudy, onClose)
            }
        }
    }
}

@Composable
private fun StampAct(planName: String, pro: Boolean, still: Boolean = false, onDone: () -> Unit) {
    val t = if (still) 99f else seconds()
    if (!still) androidx.compose.runtime.LaunchedEffect(t >= ROCKET_AT) { if (t >= ROCKET_AT) onDone() }
    val ink = if (pro) STAMP_PRO else STAMP_ESSENCIAL
    val density = LocalDensity.current
    // Tranco do carimbo: a tela inteira treme um instante.
    val shake = if (t in STAMP_HIT..(STAMP_HIT + 0.25f)) sin((t - STAMP_HIT) * 90f) * (1f - (t - STAMP_HIT) / 0.25f) * 10f else 0f
    Box(
        Modifier.fillMaxSize().background(Color(0xFF110F26).copy(alpha = 0.7f * (t / 0.3f).coerceIn(0f, 1f)))
            .clickable(remember { MutableInteractionSource() }, null) { onDone() },
        contentAlignment = Alignment.Center,
    ) {
        if (pro) ProRays()
        // A página do caderno, entrando girada.
        val inP = pop(t / 0.5f)
        Box(
            Modifier.offset(x = with(density) { shake.toDp() }).size(300.dp, 380.dp)
                .graphicsLayer { scaleX = inP; scaleY = inP; rotationZ = -4f + (1f - inP) * -14f }
                .background(PAPER, RoundedCornerShape(6.dp)).border(if (pro) 4.dp else 2.5.dp, if (pro) Color(0xFFF5B83D) else INK, RoundedCornerShape(6.dp)),
        ) {
            Canvas(Modifier.fillMaxSize()) {
                // pauta, margem dupla e furos do fichário
                var y = 52.dp.toPx()
                while (y < size.height - 10.dp.toPx()) { drawLine(Color(0xFFB9C8F0), Offset(0f, y), Offset(size.width, y), 1.dp.toPx()); y += 26.dp.toPx() }
                drawLine(Color(0xFFF2A3A3), Offset(46.dp.toPx(), 0f), Offset(46.dp.toPx(), size.height), 1.5.dp.toPx())
                drawLine(Color(0xFFF2A3A3), Offset(50.dp.toPx(), 0f), Offset(50.dp.toPx(), size.height), 1.5.dp.toPx())
                listOf(.18f, .5f, .82f).forEach { drawCircle(Color(0xFF110F26).copy(alpha = .25f), 7.dp.toPx(), Offset(22.dp.toPx(), size.height * it)) }
                // respingos do carimbo, depois da batida
                if (t > STAMP_HIT) {
                    val a = ((t - STAMP_HIT) / 0.2f).coerceIn(0f, 1f)
                    val c = Offset(size.width / 2, size.height * 0.55f)
                    val r = java.util.Random(3)
                    repeat(14) {
                        val ang = r.nextFloat() * 2f * PI.toFloat(); val d = (90 + r.nextInt(50)).dp.toPx() * a
                        drawCircle(ink.copy(alpha = .55f), (2 + r.nextInt(4)).dp.toPx(), Offset(c.x + kotlin.math.cos(ang) * d, c.y + sin(ang) * d * 0.7f))
                    }
                }
            }
            Column(Modifier.align(Alignment.TopCenter).padding(top = 18.dp, start = 56.dp, end = 16.dp)) {
                Text("Diário de estudos", fontWeight = FontWeight.Black, color = INK, fontSize = 15.sp)
                Text("Hoje: virei assinante.", color = Color(0xFF4E4B6B), fontSize = 14.sp, modifier = Modifier.padding(top = 10.dp))
            }
            // O carimbo: cai de cima, grande e torto, e bate.
            if (t > STAMP_IN) {
                val fall = ((t - STAMP_IN) / (STAMP_HIT - STAMP_IN)).coerceIn(0f, 1f).let { it * it }
                val sc = 2.6f - 1.6f * fall
                val alpha = 0.35f + 0.65f * fall
                Box(
                    Modifier.align(Alignment.Center).padding(top = 60.dp)
                        .graphicsLayer { scaleX = sc; scaleY = sc; rotationZ = -12f; this.alpha = alpha }
                        .border(BorderStroke(4.dp, ink), RoundedCornerShape(10.dp)).padding(3.dp)
                        .border(BorderStroke(1.5.dp, ink), RoundedCornerShape(7.dp)).padding(horizontal = 16.dp, vertical = 8.dp),
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        if (pro) Text("👑", fontSize = 26.sp)
                        Text("★ ASSINANTE ★", color = ink, fontWeight = FontWeight.Black, fontSize = 13.sp, letterSpacing = 2.sp)
                        Text(planName.uppercase(), color = ink, fontWeight = FontWeight.Black, fontSize = 34.sp, letterSpacing = 3.sp)
                        Text("ESTUDÁRIO · ${java.time.LocalDate.now().let { "%02d/%02d/%d".format(it.dayOfMonth, it.monthValue, it.year) }}", color = ink, fontWeight = FontWeight.Bold, fontSize = 11.sp, letterSpacing = 1.sp)
                    }
                }
            }
        }
    }
}


/** Os raios dourados do Pro: um sol girando atrás de tudo, com o brilho pulsando. */
@Composable
private fun ProRays() {
    val t = seconds()
    Canvas(Modifier.fillMaxSize()) {
        val c = Offset(size.width / 2, size.height * 0.42f)
        val r = size.maxDimension * 0.75f
        val fade = (t / 0.6f).coerceIn(0f, 1f)
        drawCircle(androidx.compose.ui.graphics.Brush.radialGradient(listOf(Color(0x66FFD27A), Color(0x00FFB421)), center = c, radius = r * 0.6f), r * 0.6f * (1f + 0.05f * sin(t * 2.4f)), c, alpha = fade)
        rotate(t * 10f, c) {
            for (i in 0 until 16) {
                val a = i * 2f * PI.toFloat() / 16f
                val p = androidx.compose.ui.graphics.Path().apply {
                    moveTo(c.x, c.y)
                    lineTo(c.x + kotlin.math.cos(a - 0.07f) * r, c.y + sin(a - 0.07f) * r)
                    lineTo(c.x + kotlin.math.cos(a + 0.07f) * r, c.y + sin(a + 0.07f) * r); close()
                }
                drawPath(p, androidx.compose.ui.graphics.Brush.radialGradient(listOf(Color(0xFFFFE07A).copy(alpha = (0.22f + 0.1f * sin(t * 2f + i)) * fade), Color(0x00FFE07A)), center = c, radius = r))
            }
        }
    }
}

@Composable
private fun CrewAct(planName: String, pro: Boolean, onStudy: () -> Unit, onClose: () -> Unit) {
    val t = seconds()
    val accent = if (pro) STAMP_PRO else STAMP_ESSENCIAL
    // O presente de assinante já vestido no Folha (as mesmas peças que entram no armário).
    val style = remember(pro) {
        br.com.estudario.ui.assistant.FolhaStyle(
            pieces = if (pro) mapOf("back" to "asas-fenix", "aura" to "supernova") else mapOf("aura" to "avioes"),
            noScene = true,
        )
    }
    val gift = if (pro) "Asas de fênix + Supernova" else "Aviõezinhos de papel"
    Box(Modifier.fillMaxSize().background(Color(0xFF110F26).copy(alpha = 0.78f)).clickable(remember { MutableInteractionSource() }, null) { onClose() }) {
        if (pro) ProRays()
        // Confete que continua caindo (dourado no Pro).
        Canvas(Modifier.fillMaxSize()) {
            val r = java.util.Random(11)
            val cols = if (pro) listOf(Color(0xFFFFD23F), Color(0xFFF5B83D), Color(0xFFFFF1BF), STAMP_PRO, Color(0xFFFF9F3A))
            else listOf(Color(0xFFFFD23F), accent, Color(0xFF22C59A), Color(0xFFFF5C4D), Color(0xFF8FD3FF))
            repeat(if (pro) 56 else 40) {
                val x0 = r.nextFloat() * size.width; val sp = 60f + r.nextFloat() * 90f; val ph = r.nextFloat() * size.height
                val y = (ph + t * sp.dp.toPx()) % (size.height + 40f) - 20f
                val x = x0 + sin(t * 2f + it) * 14.dp.toPx()
                rotate(t * 200f * (if (it % 2 == 0) 1 else -1), Offset(x, y)) { drawRect(cols[it % cols.size], Offset(x, y), Size(7.dp.toPx(), 11.dp.toPx())) }
            }
        }
        val big = pop(t / 0.6f)
        Column(
            Modifier.align(Alignment.Center).padding(horizontal = 20.dp).padding(bottom = 90.dp).graphicsLayer { scaleX = big; scaleY = big },
            horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Folha(230.dp, mood = if (t < 2.4f) FolhaMood.TALKING else FolhaMood.ECSTATIC, style = style, enter = false)
            // A etiqueta do presente, que entra girando depois do Folha.
            val tag = pop((t - 0.7f) / 0.45f)
            Row(
                Modifier.graphicsLayer { scaleX = tag; scaleY = tag; rotationZ = -3f; alpha = if (t < 0.7f) 0f else 1f }
                    .background(if (pro) Color(0xFFFFD23F) else Color(0xFFEAF0FF), RoundedCornerShape(12.dp))
                    .border(2.dp, INK, RoundedCornerShape(12.dp)).padding(horizontal = 14.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Text("🎁", fontSize = 16.sp)
                Text("Presente de assinante: $gift", color = INK, fontWeight = FontWeight.Black, fontSize = 13.sp)
            }
            Surface(shape = RoundedCornerShape(22.dp), color = PAPER, border = BorderStroke(2.5.dp, INK), shadowElevation = 6.dp, modifier = Modifier.widthIn(max = 340.dp)) {
                Column(Modifier.padding(horizontal = 20.dp, vertical = 14.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    val line = if (pro) "Obrigado de verdade. Agora você é Pro: estuda como se a prova fosse amanhã!" else "Obrigado de verdade. Agora é estudar como se a prova fosse amanhã!"
                    val shown = (t / 0.03f).toInt().coerceIn(0, line.length)
                    Text(line.take(shown), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black, color = INK, textAlign = TextAlign.Center)
                    if (shown >= line.length) Text("Tudo do $planName já está liberado, no app e no site. O presente está no armário do Folha.", style = MaterialTheme.typography.bodyMedium, color = Color(0xFF4E4B6B), textAlign = TextAlign.Center, modifier = Modifier.padding(top = 4.dp))
                }
            }
        }
        // O botão, pulsando.
        val pulse = 1f + (if (t > 1.8f) sin(t * 6f) * 0.04f else 0f)
        val showBtn = pop((t - 1.6f) / 0.4f)
        Box(
            Modifier.align(Alignment.BottomCenter).navigationBarsPadding().padding(bottom = 28.dp)
                .graphicsLayer { scaleX = showBtn * pulse; scaleY = showBtn * pulse; alpha = if (t < 1.6f) 0f else 1f }
                .background(Color(0xFFFFD23F), RoundedCornerShape(16.dp)).border(2.5.dp, INK, RoundedCornerShape(16.dp))
                .clickable { onStudy() }.padding(horizontal = 34.dp, vertical = 14.dp),
        ) { Text("Bora estudar agora  →", color = INK, fontWeight = FontWeight.Black, fontSize = 18.sp) }
    }
}
