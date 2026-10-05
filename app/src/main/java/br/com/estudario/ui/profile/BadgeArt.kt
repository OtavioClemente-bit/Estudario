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
import br.com.estudario.domain.Badge
import br.com.estudario.domain.BadgeCategory
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.drawscope.translate
import kotlin.math.PI
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

/** Silhueta de cada categoria: dá para reconhecer o emblema só pelo contorno. */
private enum class BadgeShape { SHIELD, BURST, PENTAGON, HEXAGON, CIRCLE, OCTAGON, DIAMOND, SCALLOP, SQUARE, TAG, HEX_FLAT }

private fun shapeOf(category: BadgeCategory): BadgeShape = when (category) {
    BadgeCategory.PLANO -> BadgeShape.SHIELD
    BadgeCategory.SEQUENCIA -> BadgeShape.BURST
    BadgeCategory.METAS -> BadgeShape.PENTAGON
    BadgeCategory.QUESTOES -> BadgeShape.HEXAGON
    BadgeCategory.PRECISAO -> BadgeShape.CIRCLE
    BadgeCategory.TOPICOS -> BadgeShape.OCTAGON
    BadgeCategory.EDITAL -> BadgeShape.DIAMOND
    BadgeCategory.REVISOES -> BadgeShape.SCALLOP
    BadgeCategory.SIMULADOS -> BadgeShape.SQUARE
    BadgeCategory.DISCURSIVAS -> BadgeShape.TAG
    BadgeCategory.MARATONA -> BadgeShape.HEX_FLAT
}

/** Esmalte de cada categoria (claro, escuro): a cor que a pessoa associa àquele tipo de conquista. */
private fun enamelOf(category: BadgeCategory): Pair<Color, Color> = when (category) {
    BadgeCategory.PLANO -> Color(0xFF8C86FF) to Color(0xFF3B33C9)
    BadgeCategory.SEQUENCIA -> Color(0xFFFFB067) to Color(0xFFD9480F)
    BadgeCategory.METAS -> Color(0xFF6EE7A0) to Color(0xFF15803D)
    BadgeCategory.QUESTOES -> Color(0xFF7FB2FF) to Color(0xFF1D4ED8)
    BadgeCategory.PRECISAO -> Color(0xFFFF8A8A) to Color(0xFFB91C1C)
    BadgeCategory.TOPICOS -> Color(0xFF5EEAD4) to Color(0xFF0F766E)
    BadgeCategory.EDITAL -> Color(0xFFFFD27A) to Color(0xFFB45309)
    BadgeCategory.REVISOES -> Color(0xFFFF9CCB) to Color(0xFFBE185D)
    BadgeCategory.SIMULADOS -> Color(0xFF9FB0C8) to Color(0xFF334155)
    BadgeCategory.DISCURSIVAS -> Color(0xFFC4A6FF) to Color(0xFF6D28D9)
    BadgeCategory.MARATONA -> Color(0xFF67E8F9) to Color(0xFF0E7490)
}

private fun polygon(c: Offset, r: Float, sides: Int, rotationDeg: Float): Path = Path().apply {
    repeat(sides) { i ->
        val a = Math.toRadians((rotationDeg + i * 360.0 / sides)).toFloat()
        val p = Offset(c.x + r * cos(a), c.y + r * sin(a))
        if (i == 0) moveTo(p.x, p.y) else lineTo(p.x, p.y)
    }
    close()
}

private fun waved(c: Offset, r: Float, bumps: Int, depth: Float): Path = Path().apply {
    val steps = bumps * 12
    for (i in 0..steps) {
        val a = (i.toFloat() / steps) * 2f * PI.toFloat() - PI.toFloat() / 2
        val rr = r * (1f - depth + depth * cos(a * bumps).let { (it + 1f) / 2f })
        val p = Offset(c.x + rr * cos(a), c.y + rr * sin(a))
        if (i == 0) moveTo(p.x, p.y) else lineTo(p.x, p.y)
    }
    close()
}

private fun shapePath(shape: BadgeShape, c: Offset, r: Float): Path = when (shape) {
    BadgeShape.CIRCLE -> Path().apply { addOval(androidx.compose.ui.geometry.Rect(c, r)) }
    BadgeShape.HEXAGON -> polygon(c, r * 1.04f, 6, -90f)
    BadgeShape.HEX_FLAT -> polygon(c, r * 1.04f, 6, 0f)
    BadgeShape.OCTAGON -> polygon(c, r * 1.02f, 8, 22.5f)
    BadgeShape.PENTAGON -> polygon(c, r * 1.08f, 5, -90f)
    BadgeShape.DIAMOND -> polygon(c, r * 1.2f, 4, -90f)
    BadgeShape.BURST -> waved(c, r * 1.06f, 12, 0.16f)
    BadgeShape.SCALLOP -> waved(c, r * 1.04f, 8, 0.1f)
    BadgeShape.SQUARE -> Path().apply {
        addRoundRect(androidx.compose.ui.geometry.RoundRect(androidx.compose.ui.geometry.Rect(c, r * 0.9f), androidx.compose.ui.geometry.CornerRadius(r * 0.32f)))
    }
    BadgeShape.SHIELD -> Path().apply {
        val top = c.y - r * 0.95f
        moveTo(c.x - r * 0.9f, top + r * 0.12f)
        quadraticTo(c.x, top - r * 0.12f, c.x + r * 0.9f, top + r * 0.12f)
        lineTo(c.x + r * 0.9f, c.y + r * 0.05f)
        quadraticTo(c.x + r * 0.85f, c.y + r * 0.7f, c.x, c.y + r * 1.08f)
        quadraticTo(c.x - r * 0.85f, c.y + r * 0.7f, c.x - r * 0.9f, c.y + r * 0.05f)
        close()
    }
    BadgeShape.TAG -> Path().apply {
        moveTo(c.x - r * 0.85f, c.y - r * 0.9f)
        lineTo(c.x + r * 0.85f, c.y - r * 0.9f)
        lineTo(c.x + r * 0.85f, c.y + r * 0.35f)
        lineTo(c.x, c.y + r * 1.1f)
        lineTo(c.x - r * 0.85f, c.y + r * 0.35f)
        close()
    }
}

/** Estrela de cinco pontas, usada para marcar o nível embaixo do emblema. */
private fun star(c: Offset, r: Float): Path = Path().apply {
    repeat(10) { i ->
        val a = (-90f + i * 36f) * PI.toFloat() / 180f
        val rr = if (i % 2 == 0) r else r * 0.45f
        val p = Offset(c.x + rr * cos(a), c.y + rr * sin(a))
        if (i == 0) moveTo(p.x, p.y) else lineTo(p.x, p.y)
    }
    close()
}

/**
 * O emblema: a forma e o esmalte dizem a categoria; o metal da moldura e as estrelas dizem o nível.
 * Do ouro para cima ele ganha fitas e louros; a ametista ganha asas; a esmeralda, coroa e raios.
 * Bloqueado, fica em cinza com o cadeado, sem enfeites, para a pessoa ver o que vem.
 */
@Composable
fun BadgeArt(badge: Badge, earned: Boolean, size: Dp, modifier: Modifier = Modifier) {
    val tier = badge.tier.coerceIn(1, 5)
    val metal = if (earned) tierPalette(tier) else lockedPalette()
    val (enamelLight, enamelDark) = if (earned) enamelOf(badge.category) else MaterialTheme.colorScheme.let { it.surfaceVariant to it.outlineVariant }
    val shape = shapeOf(badge.category)
    val coin = if (earned) Color(0xFFFFFBF2) else MaterialTheme.colorScheme.surfaceContainerHighest
    Box(modifier.size(size), contentAlignment = Alignment.Center) {
        Canvas(Modifier.fillMaxSize()) {
            val s = this.size.minDimension
            val c = Offset(this.size.width / 2, s * 0.47f)
            val r = s * 0.31f

            if (earned && tier >= 5) {
                // Esmeralda: raios de luz atrás de tudo.
                repeat(16) { i ->
                    rotate(i * 22.5f, c) {
                        drawPath(
                            Path().apply { moveTo(c.x - r * 0.07f, c.y - r * 1.15f); lineTo(c.x + r * 0.07f, c.y - r * 1.15f); lineTo(c.x, c.y - r * 1.55f); close() },
                            metal.glow.copy(alpha = if (i % 2 == 0) 0.85f else 0.45f),
                        )
                    }
                }
            }
            if (earned && tier >= 3) {
                // Fitas penduradas atrás, na cor da categoria.
                listOf(-1f, 1f).forEach { side ->
                    val x0 = c.x + side * r * 0.32f
                    drawPath(
                        Path().apply {
                            moveTo(x0 - r * 0.2f, c.y + r * 0.4f); lineTo(x0 + r * 0.2f, c.y + r * 0.4f)
                            lineTo(x0 + side * r * 0.12f + r * 0.2f, c.y + r * 1.42f)
                            lineTo(x0 + side * r * 0.12f, c.y + r * 1.25f)
                            lineTo(x0 + side * r * 0.12f - r * 0.2f, c.y + r * 1.42f); close()
                        },
                        Brush.verticalGradient(listOf(enamelDark, enamelLight), c.y + r * 0.4f, c.y + r * 1.42f),
                    )
                }
            }
            if (earned && tier >= 4) {
                // Ametista e esmeralda: asas de metal dos dois lados.
                listOf(-1f, 1f).forEach { side ->
                    repeat(4) { k ->
                        val len = r * (0.95f - k * 0.17f)
                        val y = c.y - r * 0.35f + k * r * 0.2f
                        val x = c.x + side * r * 0.75f
                        drawPath(
                            Path().apply {
                                moveTo(x, y)
                                quadraticTo(x + side * len * 0.6f, y - r * 0.22f, x + side * len, y - r * 0.05f)
                                quadraticTo(x + side * len * 0.55f, y + r * 0.1f, x, y + r * 0.16f)
                                close()
                            },
                            Brush.horizontalGradient(if (side < 0) listOf(metal.light, metal.dark) else listOf(metal.dark, metal.light), x - len, x + len),
                        )
                    }
                }
            }
            if (earned && tier >= 3) {
                // Louros subindo pelos lados da moldura.
                listOf(-1f, 1f).forEach { side ->
                    repeat(5) { k ->
                        val a = (100f + k * 22f) * PI.toFloat() / 180f
                        val at = Offset(c.x + side * cos(a - PI.toFloat()) * r * -1.18f, c.y + sin(a) * r * 1.0f - r * 0.05f)
                        rotate(side * (k * 22f - 30f), at) {
                            drawOval(metal.dark, Offset(at.x - r * 0.08f, at.y - r * 0.17f), Size(r * 0.16f, r * 0.34f))
                            drawOval(metal.light, Offset(at.x - r * 0.05f, at.y - r * 0.14f), Size(r * 0.08f, r * 0.26f))
                        }
                    }
                }
            }

            val outer = shapePath(shape, c, r)
            // Sombra, espessura (o "lado" do metal) e a moldura com brilho que gira em volta.
            translate(0f, s * 0.035f) { drawPath(outer, Color.Black.copy(alpha = 0.22f)) }
            translate(0f, s * 0.02f) { drawPath(outer, metal.rim) }
            drawPath(outer, Brush.sweepGradient(listOf(metal.light, metal.dark, metal.light, metal.dark, metal.light), c))
            drawPath(outer, metal.inner.copy(alpha = 0.6f), style = Stroke(s * 0.012f))

            // Esmalte da categoria, com o filete de metal e um reflexo no alto.
            val innerR = r * 0.8f
            val inner = shapePath(shape, c, innerR)
            drawPath(inner, Brush.radialGradient(listOf(enamelLight, enamelDark), c - Offset(0f, innerR * 0.35f), innerR * 1.5f))
            drawPath(inner, metal.dark.copy(alpha = 0.7f), style = Stroke(s * 0.016f))
            if (earned) clipPath(inner) {
                drawOval(Color.White.copy(alpha = 0.22f), Offset(c.x - innerR * 1.1f, c.y - innerR * 1.5f), Size(innerR * 2.2f, innerR * 1.3f))
            }

            // Moeda clara no centro, onde fica o desenho da conquista.
            drawCircle(Color.Black.copy(alpha = 0.18f), innerR * 0.6f, c + Offset(0f, s * 0.012f))
            drawCircle(coin, innerR * 0.6f, c)
            drawCircle(metal.dark.copy(alpha = 0.5f), innerR * 0.6f, c, style = Stroke(s * 0.01f))

            if (earned && tier >= 5) {
                // Coroa no topo.
                val cy = c.y - r * 1.12f
                val crown = Path().apply {
                    moveTo(c.x - r * 0.42f, cy + r * 0.18f)
                    lineTo(c.x - r * 0.46f, cy - r * 0.2f); lineTo(c.x - r * 0.2f, cy)
                    lineTo(c.x, cy - r * 0.3f); lineTo(c.x + r * 0.2f, cy)
                    lineTo(c.x + r * 0.46f, cy - r * 0.2f); lineTo(c.x + r * 0.42f, cy + r * 0.18f); close()
                }
                drawPath(crown, Brush.verticalGradient(listOf(Color(0xFFFFF1A8), Color(0xFFE0A100)), cy - r * 0.3f, cy + r * 0.18f))
                drawCircle(Color(0xFFFF4F8B), r * 0.06f, Offset(c.x, cy - r * 0.02f))
            }

            if (earned) {
                // Estrelas do nível numa faixa embaixo.
                val starR = s * 0.045f
                val gap = starR * 2.25f
                val y = c.y + r * 1.22f
                val x0 = c.x - gap * (tier - 1) / 2f
                repeat(tier) { i ->
                    val p = Offset(x0 + gap * i, y)
                    drawPath(star(p + Offset(0f, s * 0.008f), starR), Color.Black.copy(alpha = 0.25f))
                    drawPath(star(p, starR), Brush.verticalGradient(listOf(metal.light, metal.dark), p.y - starR, p.y + starR))
                }
            }
        }
        Icon(
            imageVector = categoryIcon(badge.category),
            contentDescription = null,
            modifier = Modifier.size(size * 0.27f).offset(y = -size * 0.03f),
            tint = if (earned) Color.Unspecified else MaterialTheme.colorScheme.outline,
        )
        if (!earned) Box(
            Modifier.align(Alignment.BottomEnd).size(size * 0.34f)
                .clip(androidx.compose.foundation.shape.CircleShape)
                .background(MaterialTheme.colorScheme.surfaceContainerHighest),
            contentAlignment = Alignment.Center,
        ) { Icon(Icons.Outlined.Lock, "Bloqueado", Modifier.size(size * 0.22f), tint = MaterialTheme.colorScheme.onSurfaceVariant) }
    }
}

/** Versão pequena usada em listas e no perfil. */
@Composable
internal fun BadgeMedal(badge: Badge, earned: Boolean, size: Dp = 56.dp) = BadgeArt(badge, earned, size)

/** Usado apenas pelo texto de apoio: a cor da faixa, sem o desenho. */
internal fun tierColor(tier: Int): Color = tierPalette(tier).dark
