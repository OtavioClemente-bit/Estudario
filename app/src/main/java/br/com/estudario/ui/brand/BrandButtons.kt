package br.com.estudario.ui.brand

import androidx.compose.animation.core.animateDpAsState
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * Os botões do Estudário. Têm espessura: uma borda mais escura embaixo, e afundam quando tocados.
 * Mesma assinatura dos botões do Material, então trocar o import basta para o app inteiro mudar.
 */
private val Lip = 4.dp
val BrandButtonShape = RoundedCornerShape(16.dp)

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
    val face = if (enabled) colors.containerColor else colors.disabledContainerColor
    ChunkyButton(
        onClick, modifier, enabled, shape,
        face = face,
        lip = if (enabled) lerp(face, Color.Black, 0.28f) else MaterialTheme.colorScheme.surfaceVariant,
        contentColor = if (enabled) colors.contentColor else colors.disabledContentColor,
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
        lip = lerp(face, MaterialTheme.colorScheme.onSurface, 0.22f),
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
        lip = border?.let { outline } ?: outline,
        contentColor = if (enabled) colors.contentColor else colors.disabledContentColor,
        border = border ?: BorderStroke(2.dp, outline),
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
    border: BorderStroke?,
    contentPadding: PaddingValues,
    interactionSource: MutableInteractionSource?,
    content: @Composable RowScope.() -> Unit,
) {
    val source = interactionSource ?: remember { MutableInteractionSource() }
    val pressed by source.collectIsPressedAsState()
    val sink by animateDpAsState(if (pressed && enabled) Lip else 0.dp, tween(70), label = "button-press")
    Box(
        modifier
            .defaultMinSize(minWidth = 64.dp, minHeight = 48.dp + Lip)
            .clip(shape)
            .clickable(interactionSource = source, indication = null, enabled = enabled, role = Role.Button, onClick = onClick),
        propagateMinConstraints = true,
    ) {
        Box(Modifier.matchParentSize().padding(top = Lip).background(lip, shape))
        Row(
            Modifier
                .padding(bottom = Lip)
                .offset(y = sink)
                .background(face, shape)
                .then(if (border != null) Modifier.border(border, shape) else Modifier)
                .padding(contentPadding),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            CompositionLocalProvider(LocalContentColor provides contentColor) {
                ProvideTextStyle(MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.ExtraBold)) { content() }
            }
        }
    }
}
