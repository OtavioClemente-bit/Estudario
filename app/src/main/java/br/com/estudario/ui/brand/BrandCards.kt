package br.com.estudario.ui.brand

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp

/**
 * Os cartões do Estudário: borda firme e uma espessura embaixo, como os botões. Os que dá para
 * tocar afundam. Mesma assinatura dos cartões do Material, então o app inteiro muda junto.
 */
private val CardLip = 4.dp
val BrandCardShape = RoundedCornerShape(20.dp)

@Composable
fun Card(
    modifier: Modifier = Modifier,
    shape: Shape = BrandCardShape,
    colors: CardColors = CardDefaults.cardColors(),
    @Suppress("UNUSED_PARAMETER") elevation: CardElevation = CardDefaults.cardElevation(),
    border: BorderStroke? = null,
    content: @Composable ColumnScope.() -> Unit,
) = ChunkyCard(null, modifier, true, shape, colors, border, null, content)

@Composable
fun Card(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = BrandCardShape,
    colors: CardColors = CardDefaults.cardColors(),
    @Suppress("UNUSED_PARAMETER") elevation: CardElevation = CardDefaults.cardElevation(),
    border: BorderStroke? = null,
    interactionSource: MutableInteractionSource? = null,
    content: @Composable ColumnScope.() -> Unit,
) = ChunkyCard(onClick, modifier, enabled, shape, colors, border, interactionSource, content)

@Composable
fun ElevatedCard(
    modifier: Modifier = Modifier,
    shape: Shape = BrandCardShape,
    colors: CardColors = CardDefaults.elevatedCardColors(),
    @Suppress("UNUSED_PARAMETER") elevation: CardElevation = CardDefaults.elevatedCardElevation(),
    content: @Composable ColumnScope.() -> Unit,
) = ChunkyCard(null, modifier, true, shape, colors, null, null, content)

@Composable
fun ElevatedCard(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = BrandCardShape,
    colors: CardColors = CardDefaults.elevatedCardColors(),
    @Suppress("UNUSED_PARAMETER") elevation: CardElevation = CardDefaults.elevatedCardElevation(),
    interactionSource: MutableInteractionSource? = null,
    content: @Composable ColumnScope.() -> Unit,
) = ChunkyCard(onClick, modifier, enabled, shape, colors, null, interactionSource, content)

@Composable
fun OutlinedCard(
    modifier: Modifier = Modifier,
    shape: Shape = BrandCardShape,
    colors: CardColors = CardDefaults.outlinedCardColors(),
    @Suppress("UNUSED_PARAMETER") elevation: CardElevation = CardDefaults.outlinedCardElevation(),
    border: BorderStroke? = null,
    content: @Composable ColumnScope.() -> Unit,
) = ChunkyCard(null, modifier, true, shape, colors, border, null, content)

@Composable
fun OutlinedCard(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = BrandCardShape,
    colors: CardColors = CardDefaults.outlinedCardColors(),
    @Suppress("UNUSED_PARAMETER") elevation: CardElevation = CardDefaults.outlinedCardElevation(),
    border: BorderStroke? = null,
    interactionSource: MutableInteractionSource? = null,
    content: @Composable ColumnScope.() -> Unit,
) = ChunkyCard(onClick, modifier, enabled, shape, colors, border, interactionSource, content)

@Composable
private fun ChunkyCard(
    onClick: (() -> Unit)?,
    modifier: Modifier,
    enabled: Boolean,
    shape: Shape,
    colors: CardColors,
    border: BorderStroke?,
    interactionSource: MutableInteractionSource?,
    content: @Composable ColumnScope.() -> Unit,
) {
    val surface = MaterialTheme.colorScheme.surface
    val outline = MaterialTheme.colorScheme.outlineVariant
    // Cores translúcidas são resolvidas sobre o fundo, para a espessura não ficar transparente.
    val face = colors.containerColor.let { if (it.alpha < 1f) it.compositeOver(surface) else it }
    val tinted = face != MaterialTheme.colorScheme.surfaceContainerLow && face != MaterialTheme.colorScheme.surface &&
        face != MaterialTheme.colorScheme.surfaceContainerLowest && face != MaterialTheme.colorScheme.surfaceContainer
    val lip = if (tinted) lerp(face, Color.Black, 0.30f) else outline
    val edge = border ?: BorderStroke(2.dp, if (tinted) lerp(face, Color.Black, 0.12f) else outline)

    val source = interactionSource ?: remember { MutableInteractionSource() }
    val pressed by source.collectIsPressedAsState()
    val sink by animateDpAsState(if (onClick != null && pressed && enabled) CardLip else 0.dp, tween(70), label = "card-press")

    Box(
        modifier
            .clip(shape)
            .then(
                if (onClick != null) Modifier.clickable(source, indication = null, enabled = enabled, role = Role.Button, onClick = onClick)
                else Modifier,
            ),
        propagateMinConstraints = true,
    ) {
        Box(Modifier.matchParentSize().padding(top = CardLip).background(lip, shape))
        Column(
            Modifier
                .padding(bottom = CardLip)
                .offset(y = sink)
                .background(face, shape)
                .border(edge, shape),
        ) {
            CompositionLocalProvider(LocalContentColor provides colors.contentColor) { content() }
        }
    }
}
