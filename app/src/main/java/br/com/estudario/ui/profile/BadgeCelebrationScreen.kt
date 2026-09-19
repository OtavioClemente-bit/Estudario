package br.com.estudario.ui.profile

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import br.com.estudario.domain.Badge
import br.com.estudario.ui.components.AppMarkBackground
import kotlin.math.cos
import kotlin.math.sin
import kotlinx.coroutines.delay

private val Verde = Color(0xFF20B486)

private fun faixaLabel(tier: Int) = when (tier) {
    1 -> "Bronze"; 2 -> "Prata"; 3 -> "Ouro"; 4 -> "Ametista"; else -> "Esmeralda"
}

/**
 * Comemoração do emblema, no mesmo formato da sequência: tela cheia, raios girando atrás e o
 * escudo entrando com mola. Quando cai mais de um emblema de uma vez, eles aparecem em sequência —
 * cada conquista ganha sua própria tela em vez de virar uma lista.
 */
@Composable
fun BadgeCelebrationScreen(badges: List<Badge>, onClose: () -> Unit) {
    if (badges.isEmpty()) return
    var index by remember(badges) { mutableIntStateOf(0) }
    val badge = badges.getOrNull(index) ?: return

    Dialog(onDismissRequest = onClose, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        Box(
            Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(listOf(AppMarkBackground, Color(0xFF0E2E63), AppMarkBackground))),
        ) {
            // key: cada emblema reinicia a animação do zero.
            key(index) { Conteudo(badge, badges.size, index) }

            Button(
                onClick = { if (index < badges.lastIndex) index++ else onClose() },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .systemBarsPadding()
                    .padding(28.dp)
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Verde, contentColor = Color.White),
            ) {
                Text(
                    if (index < badges.lastIndex) "Próximo emblema" else "Continuar",
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}

@Composable
private fun Conteudo(badge: Badge, total: Int, index: Int) {
    val palette = tierPalette(badge.tier)
    var stage by remember { mutableIntStateOf(0) }
    LaunchedEffect(Unit) { stage = 1; delay(420); stage = 2 }

    val escala by animateFloatAsState(if (stage >= 1) 1f else 0.4f, spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessLow), label = "escala")
    val opacidade by animateFloatAsState(if (stage >= 1) 1f else 0f, tween(320), label = "opacidade")
    val texto by animateFloatAsState(if (stage >= 2) 1f else 0f, tween(420), label = "texto")

    val giro = rememberInfiniteTransition(label = "giro")
    val angulo by giro.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(26_000, easing = LinearEasing), RepeatMode.Restart),
        label = "angulo",
    )

    Column(
        Modifier.fillMaxSize().systemBarsPadding().padding(start = 28.dp, end = 28.dp, top = 28.dp, bottom = 108.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            "EMBLEMA CONQUISTADO",
            color = palette.glow,
            fontWeight = FontWeight.Black,
            letterSpacing = 2.sp,
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.graphicsLayer { alpha = opacidade },
        )
        if (total > 1) {
            Text(
                "${index + 1} de $total",
                color = Color.White.copy(alpha = 0.55f),
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.graphicsLayer { alpha = opacidade },
            )
        }

        Spacer(Modifier.height(28.dp))

        Box(contentAlignment = Alignment.Center) {
            Canvas(
                Modifier
                    .size(300.dp)
                    .graphicsLayer { rotationZ = angulo; alpha = opacidade * 0.5f },
            ) {
                val centro = Offset(size.width / 2f, size.height / 2f)
                val raio = size.minDimension / 2f
                repeat(16) { i ->
                    val passo = Math.toRadians(22.5 * i)
                    val largura = Math.toRadians(5.0)
                    val path = Path().apply {
                        moveTo(centro.x, centro.y)
                        lineTo(
                            centro.x + raio * cos(passo - largura).toFloat(),
                            centro.y + raio * sin(passo - largura).toFloat(),
                        )
                        lineTo(
                            centro.x + raio * cos(passo + largura).toFloat(),
                            centro.y + raio * sin(passo + largura).toFloat(),
                        )
                        close()
                    }
                    drawPath(path, palette.glow.copy(alpha = if (i % 2 == 0) 0.3f else 0.14f))
                }
            }
            BadgeArt(
                badge = badge,
                earned = true,
                size = 190.dp,
                modifier = Modifier.graphicsLayer { scaleX = escala; scaleY = escala; alpha = opacidade },
            )
        }

        Spacer(Modifier.height(28.dp))

        Text(
            badge.name,
            color = Color.White,
            fontWeight = FontWeight.Black,
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.graphicsLayer { alpha = texto },
        )
        Spacer(Modifier.height(8.dp))
        Text(
            badge.requirement,
            color = Color.White.copy(alpha = 0.78f),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.graphicsLayer { alpha = texto },
        )
        Spacer(Modifier.height(18.dp))
        Row(
            Modifier
                .graphicsLayer { alpha = texto }
                .clip(RoundedCornerShape(50))
                .background(palette.glow.copy(alpha = 0.16f))
                .border(1.dp, palette.glow.copy(alpha = 0.45f), RoundedCornerShape(50))
                .padding(horizontal = 14.dp, vertical = 7.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(faixaLabel(badge.tier), color = palette.glow, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelLarge)
            Text("• ${badge.category.label}", color = Color.White.copy(alpha = 0.6f), style = MaterialTheme.typography.labelMedium)
        }
    }
}
