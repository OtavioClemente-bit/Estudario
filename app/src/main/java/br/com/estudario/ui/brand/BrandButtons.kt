package br.com.estudario.ui.brand

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * Os botões do Estudário, "caderno de projeto": papel recortado colado na folha. Borda de tinta,
 * cantos levemente desiguais, sombra dura deslocada para baixo e para a direita, verniz no topo e, no
 * principal, uma faixa de luz que passa de vez em quando. Ao tocar, o recorte afunda na sombra.
 * Mesma assinatura dos botões do Material, então trocar o import basta para o app inteiro mudar.
 */
private val Lip = 4.dp
private val LipX = 3.dp
val BrandButtonShape = RoundedCornerShape(topStart = 14.dp, topEnd = 7.dp, bottomEnd = 16.dp, bottomStart = 9.dp)

/** A cor da tinta do recorte (borda e sombra dura). */
@Composable
fun cutInk(): Color = if (MaterialTheme.colorScheme.surface.luminance() < 0.5f) Color(0xFF05091A) else Color(0xFF1C1E25)

@Composable
fun Button(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = BrandButtonShape,
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    @Suppress("UNUSED_PARAMETER") elevation: ButtonElevation? = null,
    border: BorderStroke? = null,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    interactionSource: MutableInteractionSource? = null,
    content: @Composable RowScope.() -> Unit,
) {
    // Como no site: o primário é sempre a tinta azul de caneta com texto branco, nos dois temas, e o
    // desligado fica papel apagado.
    val scheme = MaterialTheme.colorScheme
    val dark = scheme.surface.luminance() < 0.5f
    val brand = colors.containerColor == scheme.primary
    val face = when {
        !enabled -> if (dark) Color(0xFF1D2F54) else Color(0xFFE9E3D2)
        brand -> Color(0xFF2F5BD3)
        else -> colors.containerColor
    }
    ChunkyButton(
        onClick, modifier, enabled, shape,
        face = face,
        lip = cutInk().copy(alpha = if (enabled) 1f else 0.35f),
        shine = brand && enabled,
        contentColor = when {
            !enabled -> if (dark) Color(0xFF8DA0C6) else Color(0xFF6A6F7D)
            brand -> Color.White
            else -> colors.contentColor
        },
        border = border, contentPadding = contentPadding, interactionSource = interactionSource, content = content,
    )
}

@Composable
fun FilledTonalButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = BrandButtonShape,
    colors: ButtonColors = ButtonDefaults.filledTonalButtonColors(),
    @Suppress("UNUSED_PARAMETER") elevation: ButtonElevation? = null,
    border: BorderStroke? = null,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    interactionSource: MutableInteractionSource? = null,
    content: @Composable RowScope.() -> Unit,
) {
    val face = if (enabled) colors.containerColor else colors.disabledContainerColor
    ChunkyButton(
        onClick, modifier, enabled, shape,
        face = face,
        lip = cutInk(),
        contentColor = if (enabled) colors.contentColor else colors.disabledContentColor,
        border = border, contentPadding = contentPadding, interactionSource = interactionSource, content = content,
    )
}

@Composable
fun OutlinedButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = BrandButtonShape,
    colors: ButtonColors = ButtonDefaults.outlinedButtonColors(),
    @Suppress("UNUSED_PARAMETER") elevation: ButtonElevation? = null,
    border: BorderStroke? = null,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    interactionSource: MutableInteractionSource? = null,
    content: @Composable RowScope.() -> Unit,
) {
    val outline = MaterialTheme.colorScheme.outlineVariant
    val face = colors.containerColor.takeIf { it.alpha > 0f } ?: MaterialTheme.colorScheme.surfaceContainerLowest
    ChunkyButton(
        onClick, modifier, enabled, shape,
        face = face,
        lip = cutInk(),
        contentColor = if (enabled) colors.contentColor else colors.disabledContentColor,
        border = border,
        contentPadding = contentPadding, interactionSource = interactionSource, content = content,
    )
}

@Composable
private fun ChunkyButton(
    onClick: () -> Unit,
    modifier: Modifier,
    enabled: Boolean,
    shape: Shape,
    face: Color,
    lip: Color,
    contentColor: Color,
    shine: Boolean = false,
    border: BorderStroke?,
    contentPadding: PaddingValues,
    interactionSource: MutableInteractionSource?,
    content: @Composable RowScope.() -> Unit,
) {
    val source = interactionSource ?: remember { MutableInteractionSource() }
    val pressed by source.collectIsPressedAsState()
    val sink by animateDpAsState(if (pressed && enabled) Lip else 0.dp, tween(70), label = "button-press")
    val sinkX by animateDpAsState(if (pressed && enabled) LipX else 0.dp, tween(70), label = "button-press-x")
    val edge = border ?: BorderStroke(2.5.dp, lip)
    // A faixa de luz que passa no principal (a mesma do site), a cada ~3,5 s.
    val sweep = if (shine) {
        val t = androidx.compose.animation.core.rememberInfiniteTransition(label = "shine")
        t.animateFloat(-0.6f, 1.6f, androidx.compose.animation.core.infiniteRepeatable(androidx.compose.animation.core.keyframes { durationMillis = 3400; -0.6f at 2000; 1.6f at 3400 }), label = "shine-x").value
    } else null
    Box(
        modifier
            .defaultMinSize(minWidth = 64.dp, minHeight = 48.dp + Lip)
            .clickable(interactionSource = source, indication = null, enabled = enabled, role = Role.Button, onClick = onClick),
        propagateMinConstraints = true,
    ) {
        Box(Modifier.matchParentSize().padding(top = Lip, start = LipX).background(lip, shape))
        Row(
            Modifier
                .padding(bottom = Lip, end = LipX)
                .offset(x = sinkX, y = sink)
                .clip(shape)
                .background(face, shape)
                .drawWithContent {
                    drawContent()
                    // verniz: a luz no topo, como papel plastificado
                    drawRoundRect(
                        androidx.compose.ui.graphics.Brush.verticalGradient(listOf(Color.White.copy(alpha = 0.38f), Color.Transparent), startY = 0f, endY = size.height * 0.42f),
                        topLeft = androidx.compose.ui.geometry.Offset(8.dp.toPx(), 3.dp.toPx()),
                        size = androidx.compose.ui.geometry.Size(size.width - 16.dp.toPx(), size.height * 0.36f),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(8.dp.toPx()),
                    )
                    if (sweep != null) {
                        val w = size.width * 0.36f
                        val x = size.width * sweep
                        drawRect(androidx.compose.ui.graphics.Brush.linearGradient(listOf(Color.Transparent, Color.White.copy(alpha = 0.45f), Color.Transparent),
                            start = androidx.compose.ui.geometry.Offset(x, 0f), end = androidx.compose.ui.geometry.Offset(x + w, size.height)))
                    }
                }
                .border(edge, shape)
                .padding(contentPadding),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            CompositionLocalProvider(LocalContentColor provides contentColor) {
                ProvideTextStyle(MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Black)) { content() }
            }
        }
    }
}
