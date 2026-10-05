package br.com.estudario.ui.focus

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import br.com.estudario.ui.assistant.Folha
import br.com.estudario.ui.assistant.FolhaMood
import br.com.estudario.ui.brand.Button
import br.com.estudario.ui.brand.Surface
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 * Aviso de que o tempo planejado para o que está sendo estudado acabou: o Folha feliz gira e pula,
 * o celular vibra de leve, e a pessoa escolhe seguir estudando ou encerrar a sessão. Só avisa, não
 * para nada: o relógio do foco continua contando.
 */
@Composable
fun FocusTimeUpDialog(plannedMinutes: Int, title: String, onContinue: () -> Unit, onFinish: () -> Unit) {
    val haptics = LocalHapticFeedback.current
    LaunchedEffect(Unit) { haptics.performHapticFeedback(HapticFeedbackType.LongPress) }
    Dialog(onDismissRequest = onContinue) {
        Surface(shape = RoundedCornerShape(30.dp), color = MaterialTheme.colorScheme.surface, tonalElevation = 6.dp, shadowElevation = 18.dp, modifier = Modifier.widthIn(max = 400.dp)) {
            Column(
                Modifier.padding(horizontal = 24.dp, vertical = 26.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                CelebratingFolha()
                Text("Deu o tempo planejado!", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                Text(
                    "Você já estudou os ${minutesText(plannedMinutes)} que o plano reservou para $title. Pode seguir, se estiver rendendo, ou encerrar e registrar.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                )
                Button(onClick = onContinue, modifier = Modifier.fillMaxWidth().padding(top = 6.dp)) { Text("Ok, vou seguir") }
                TextButton(onClick = onFinish, modifier = Modifier.fillMaxWidth()) { Text("Encerrar a sessão") }
            }
        }
    }
}

private fun minutesText(minutes: Int): String = when {
    minutes < 60 -> "$minutes minutos"
    minutes % 60 == 0 -> if (minutes == 60) "60 minutos" else "${minutes / 60} horas"
    else -> "${minutes / 60}h${"%02d".format(minutes % 60)}"
}

/** O Folha feliz dando uma volta inteira com pulinho, de tempos em tempos, com um anel de brilho girando. */
@Composable
private fun CelebratingFolha() {
    val entrance = remember { Animatable(0f) }
    LaunchedEffect(Unit) { entrance.animateTo(1f, tween(700, easing = FastOutSlowInEasing)) }
    val loop = rememberInfiniteTransition(label = "focus-done")
    // Uma volta com pulinho a cada 2,4 s; entre uma e outra ele só balança de leve.
    val spin by loop.animateFloat(
        0f, 360f,
        infiniteRepeatable(keyframes { durationMillis = 2_400; 0f at 0; 0f at 1_300; 360f at 2_100 using FastOutSlowInEasing; 360f at 2_400 }),
        label = "spin",
    )
    val hop by loop.animateFloat(
        0f, 0f,
        infiniteRepeatable(keyframes { durationMillis = 2_400; 0f at 1_300; -18f at 1_700; 0f at 2_100 }),
        label = "hop",
    )
    val ring by loop.animateFloat(0f, 360f, infiniteRepeatable(tween(6_000, easing = LinearEasing), RepeatMode.Restart), label = "ring")
    val colors = listOf(Color(0xFF8B6CFF), Color(0xFF22C59A), Color(0xFFFFC94D), Color(0xFFFF4FD8))
    Box(Modifier.size(170.dp), contentAlignment = Alignment.Center) {
        Canvas(Modifier.fillMaxSize()) {
            val c = center
            val r = size.minDimension / 2
            drawCircle(Brush.radialGradient(listOf(Color(0xFF8B6CFF).copy(alpha = 0.35f), Color.Transparent), c, r), r, c)
            // Anel de pontinhos coloridos girando em volta.
            repeat(16) { i ->
                val a = (ring + i * 22.5f) * PI.toFloat() / 180f
                val rr = r * 0.86f
                drawCircle(colors[i % colors.size].copy(alpha = if (i % 2 == 0) 0.95f else 0.5f), (if (i % 2 == 0) 4.5f else 3f).dp.toPx(), Offset(c.x + cos(a) * rr, c.y + sin(a) * rr))
            }
        }
        Folha(
            118.dp,
            mood = FolhaMood.HAPPY,
            modifier = Modifier.graphicsLayer {
                val e = entrance.value
                scaleX = 0.5f + 0.5f * e
                scaleY = 0.5f + 0.5f * e
                alpha = e
                rotationZ = spin
                translationY = hop * density
            },
        )
    }
}
