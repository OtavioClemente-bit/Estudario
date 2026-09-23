package br.com.estudario.ui.profile

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import br.com.estudario.domain.Badge
import kotlin.math.cos
import kotlin.math.sin

/**
 * O desenho do emblema.
 *
 * Nada de ícone dentro de um círculo: é um escudo hexagonal com metal degradê, aro biselado,
 * raios de brilho atrás, disco interno, reflexo em cima e uma pedrinha por faixa na base. A
 * silhueta é sempre a mesma, o que muda com a faixa é o metal (bronze, prata, ouro, ametista,
 * esmeralda) e o número de pedras, para dar de longe a noção de progressão.
 *
 * O emblema bloqueado mantém a silhueta em cinza com um cadeado, para a pessoa ver o que está
 * perdendo em vez de um espaço vazio.
 */
internal data class BadgePalette(
    val light: Color,
    val dark: Color,
    val rim: Color,
    val inner: Color,
    val disc: Color,
    val glyph: Color,
    val glow: Color,
)

internal fun tierPalette(tier: Int): BadgePalette = when (tier) {
    1 -> BadgePalette(Color(0xFFF0B27A), Color(0xFF9C5221), Color(0xFF6E3814), Color(0xFFFFD9B3), Color(0xFF7A3F19), Color(0xFFFFE9D6), Color(0xFFE8925A))
    2 -> BadgePalette(Color(0xFFF2F5F9), Color(0xFF7C8794), Color(0xFF545D68), Color(0xFFFFFFFF), Color(0xFF616B78), Color(0xFFF7FAFF), Color(0xFFCFD8E3))
    3 -> BadgePalette(Color(0xFFFFE9A3), Color(0xFFCE8E00), Color(0xFF8A5E00), Color(0xFFFFF6D6), Color(0xFFA36F00), Color(0xFFFFF8E1), Color(0xFFFFC94D))
    4 -> BadgePalette(Color(0xFFCBB8FF), Color(0xFF5B3FD6), Color(0xFF3A248F), Color(0xFFE6DCFF), Color(0xFF442BA8), Color(0xFFF1EBFF), Color(0xFF9B7BFF))
    else -> BadgePalette(Color(0xFF9DF2D6), Color(0xFF12876A), Color(0xFF0A5B47), Color(0xFFD6FFF1), Color(0xFF0E6B54), Color(0xFFE8FFF7), Color(0xFF3FD9AC))
}

@Composable
private fun lockedPalette(): BadgePalette {
    val scheme = MaterialTheme.colorScheme
    return BadgePalette(
        light = scheme.surfaceVariant,
        dark = scheme.surfaceVariant.copy(alpha = 0.72f),
        rim = scheme.outlineVariant,
        inner = scheme.outlineVariant.copy(alpha = 0.6f),
        disc = scheme.surfaceVariant.copy(alpha = 0.55f),
        glyph = scheme.outline,
        glow = Color.Transparent,
    )
}

/** Hexágono de topo plano: sobra largura em cima e embaixo para o aro e as pedras. */
private fun hexagon(center: Offset, radius: Float): Path {
    val path = Path()
    repeat(6) { index ->
        val angle = Math.toRadians(60.0 * index)
        val x = center.x + radius * cos(angle).toFloat()
        val y = center.y + radius * sin(angle).toFloat()
        if (index == 0) path.moveTo(x, y) else path.lineTo(x, y)
    }
    path.close()
    return path
}

private fun DrawScope.rays(center: Offset, radius: Float, color: Color) {
    repeat(12) { index ->
        rotate(degrees = index * 30f, pivot = center) {
            val path = Path().apply {
                moveTo(center.x - radius * 0.055f, center.y - radius * 0.86f)
                lineTo(center.x + radius * 0.055f, center.y - radius * 0.86f)
                lineTo(center.x, center.y - radius * 1.02f)
                close()
            }
            drawPath(path, color)
        }
    }
}

@Composable
fun BadgeArt(badge: Badge, earned: Boolean, size: Dp, modifier: Modifier = Modifier) {
    val palette = if (earned) tierPalette(badge.tier) else lockedPalette()
    Box(modifier.size(size), contentAlignment = Alignment.Center) {
        Canvas(Modifier.fillMaxSize()) {
            val side = this.size.minDimension
            val center = Offset(this.size.width / 2f, this.size.height / 2f)
            val radius = side / 2f

            if (earned) {
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(palette.glow.copy(alpha = 0.45f), Color.Transparent),
                        center = center,
                        radius = radius,
                    ),
                    radius = radius,
                    center = center,
                )
                rays(center, radius, palette.glow.copy(alpha = 0.55f))
            }

            // Aro externo e corpo do escudo.
            drawPath(hexagon(center, radius * 0.88f), palette.rim)
            drawPath(
                hexagon(center, radius * 0.79f),
                Brush.linearGradient(
                    colors = listOf(palette.light, palette.dark),
                    start = Offset(center.x - radius, center.y - radius),
                    end = Offset(center.x + radius, center.y + radius),
                ),
            )
            // Bisel interno: a linha fina que dá o aspecto de metal cunhado.
            drawPath(hexagon(center, radius * 0.64f), palette.inner.copy(alpha = 0.75f), style = Stroke(width = radius * 0.05f))
            drawCircle(palette.disc, radius = radius * 0.5f, center = center)

            if (earned) {
                // Reflexo no canto superior esquerdo.
                drawArc(
                    color = Color.White.copy(alpha = 0.32f),
                    startAngle = 185f,
                    sweepAngle = 80f,
                    useCenter = false,
                    topLeft = Offset(center.x - radius * 0.66f, center.y - radius * 0.66f),
                    size = Size(radius * 1.32f, radius * 1.32f),
                    style = Stroke(width = radius * 0.1f),
                )
                // Pedras da faixa, na base do escudo.
                val pips = badge.tier.coerceIn(1, 5)
                val gap = radius * 0.17f
                val baseY = center.y + radius * 0.615f
                val startX = center.x - gap * (pips - 1) / 2f
                repeat(pips) { index ->
                    val pip = Path().apply {
                        val cx = startX + gap * index
                        val size = radius * 0.062f
                        moveTo(cx, baseY - size)
                        lineTo(cx + size, baseY)
                        lineTo(cx, baseY + size)
                        lineTo(cx - size, baseY)
                        close()
                    }
                    drawPath(pip, palette.inner)
                }
            }
        }
        Icon(
            imageVector = if (earned) categoryIcon(badge.category) else Icons.Outlined.Lock,
            contentDescription = null,
            modifier = Modifier.size(size * 0.33f),
            tint = palette.glyph,
        )
    }
}

/** Versão pequena usada em listas e no perfil. */
@Composable
internal fun BadgeMedal(badge: Badge, earned: Boolean, size: Dp = 56.dp) = BadgeArt(badge, earned, size)

/** Usado apenas pelo texto de apoio: a cor da faixa, sem o desenho. */
internal fun tierColor(tier: Int): Color = tierPalette(tier).dark
