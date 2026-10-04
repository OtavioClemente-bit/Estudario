package br.com.estudario.ui.profile

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lock
import br.com.estudario.ui.brand.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import androidx.compose.ui.unit.sp
import br.com.estudario.domain.Badge
import kotlin.math.cos
import kotlin.math.sin

/**
 * O desenho do emblema: um broche de metal esmaltado, com espessura, o desenho da categoria no
 * centro e a fita do nível (I a V) atravessando a base. O metal muda com a faixa (bronze, prata,
 * ouro, ametista, esmeralda). Bloqueado, fica em cinza com o cadeado, para a pessoa ver o que vem.
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
    val tier = badge.tier.coerceIn(1, 5)
    // A fita com o nível só cabe no tamanho grande (comemoração); nas listas ela vira um risco.
    val big = size >= 96.dp
    Box(modifier.size(size), contentAlignment = Alignment.Center) {
        Canvas(Modifier.fillMaxSize()) {
            val w = this.size.width
            val h = this.size.height
            // A placa: um broche de metal com espessura, como os de lapela. Nada de raio de luz.
            val plaqueW = w * 0.80f
            val plaqueH = h * 0.80f
            val left = (w - plaqueW) / 2f
            val top = h * 0.04f
            val corner = androidx.compose.ui.geometry.CornerRadius(plaqueW * 0.30f)
            val lip = h * 0.06f
            drawOval(Color.Black.copy(alpha = 0.12f), Offset(left + plaqueW * 0.1f, top + plaqueH + lip * 0.6f), Size(plaqueW * 0.8f, h * 0.06f))
            drawRoundRect(palette.rim, Offset(left, top + lip), Size(plaqueW, plaqueH), corner)
            drawRoundRect(
                Brush.verticalGradient(listOf(palette.light, palette.dark), top, top + plaqueH),
                Offset(left, top), Size(plaqueW, plaqueH), corner,
            )
            // Esmalte interno, um tom mais claro, com o filete de metal em volta.
            val inset = plaqueW * 0.09f
            drawRoundRect(
                palette.disc.copy(alpha = if (earned) 0.55f else 0.4f),
                Offset(left + inset, top + inset), Size(plaqueW - inset * 2, plaqueH - inset * 2),
                androidx.compose.ui.geometry.CornerRadius(plaqueW * 0.22f),
            )
            drawRoundRect(
                palette.inner.copy(alpha = 0.8f),
                Offset(left + inset, top + inset), Size(plaqueW - inset * 2, plaqueH - inset * 2),
                androidx.compose.ui.geometry.CornerRadius(plaqueW * 0.22f),
                style = Stroke(width = plaqueW * 0.025f),
            )
            if (earned) {
                // Brilho de metal polido no alto, curto e discreto.
                drawRoundRect(
                    Color.White.copy(alpha = 0.28f),
                    Offset(left + plaqueW * 0.16f, top + plaqueH * 0.05f), Size(plaqueW * 0.42f, plaqueH * 0.06f),
                    androidx.compose.ui.geometry.CornerRadius(plaqueH * 0.03f),
                )
            }
            if (earned && big) {
                // A fita do nível atravessando a base, com as pontas recortadas.
                val bandY = top + plaqueH * 0.74f
                val bandH = plaqueH * 0.22f
                val bandL = w * 0.04f
                val bandR = w * 0.96f
                val notch = bandH * 0.45f
                fun band(dy: Float, color: Color) = drawPath(
                    Path().apply {
                        moveTo(bandL, bandY + dy); lineTo(bandR, bandY + dy)
                        lineTo(bandR - notch, bandY + bandH / 2 + dy); lineTo(bandR, bandY + bandH + dy)
                        lineTo(bandL, bandY + bandH + dy); lineTo(bandL + notch, bandY + bandH / 2 + dy); close()
                    },
                    color,
                )
                band(lip * 0.6f, palette.rim)
                band(0f, palette.dark)
            }
        }
        // Bloqueado mostra o mesmo desenho, em cinza: a pessoa vê o que vai ganhar.
        Icon(
            imageVector = categoryIcon(badge.category),
            contentDescription = null,
            modifier = Modifier.size(size * if (big) 0.40f else 0.48f).offset(y = -size * if (earned && big) 0.07f else 0.02f),
            tint = if (earned) Color.Unspecified else MaterialTheme.colorScheme.outline,
        )
        if (!earned) Box(
            Modifier.align(Alignment.BottomEnd).size(size * 0.36f)
                .clip(androidx.compose.foundation.shape.CircleShape)
                .background(MaterialTheme.colorScheme.surfaceContainerHighest),
            contentAlignment = Alignment.Center,
        ) { Icon(Icons.Outlined.Lock, "Bloqueado", Modifier.size(size * 0.24f), tint = MaterialTheme.colorScheme.onSurfaceVariant) }
        if (earned && big) {
            androidx.compose.material3.Text(
                listOf("I", "II", "III", "IV", "V")[tier - 1],
                modifier = Modifier.offset(y = size * 0.31f),
                color = Color.White,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Black,
                fontSize = (size.value * 0.13f).sp,
                maxLines = 1,
            )
        }
    }
}

/** Versão pequena usada em listas e no perfil. */
@Composable
internal fun BadgeMedal(badge: Badge, earned: Boolean, size: Dp = 56.dp) = BadgeArt(badge, earned, size)

/** Usado apenas pelo texto de apoio: a cor da faixa, sem o desenho. */
internal fun tierColor(tier: Int): Color = tierPalette(tier).dark
