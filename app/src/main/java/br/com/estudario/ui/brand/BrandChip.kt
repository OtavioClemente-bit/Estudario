package br.com.estudario.ui.brand

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.SelectableChipColors
import androidx.compose.material3.SelectableChipElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

private val ChipShape = RoundedCornerShape(50)

/**
 * Filtro em pílula, no lugar do chip do Material. Escolhido, fica na cor da marca com espessura
 * embaixo; os outros, contorno firme. Mesma assinatura do FilterChip para trocar o app inteiro.
 */
@Composable
fun FilterChip(
    selected: Boolean,
    onClick: () -> Unit,
    label: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    @Suppress("UNUSED_PARAMETER") shape: Shape = ChipShape,
    @Suppress("UNUSED_PARAMETER") colors: SelectableChipColors? = null,
    @Suppress("UNUSED_PARAMETER") elevation: SelectableChipElevation? = null,
    @Suppress("UNUSED_PARAMETER") border: BorderStroke? = null,
    interactionSource: MutableInteractionSource? = null,
) {
    val scheme = MaterialTheme.colorScheme
    val face by animateColorAsState(if (selected) scheme.primary else scheme.surfaceContainerLowest, label = "chip-face")
    val content by animateColorAsState(if (selected) scheme.onPrimary else scheme.onSurface, label = "chip-content")
    val edge = if (selected) lerp(scheme.primary, Color.Black, 0.25f) else scheme.outlineVariant
    val source = interactionSource ?: remember { MutableInteractionSource() }
    Box(
        modifier
            .alpha(if (enabled) 1f else 0.45f)
            .clip(ChipShape)
            .clickable(source, indication = null, enabled = enabled, role = Role.Checkbox, onClick = onClick),
    ) {
        Box(Modifier.matchParentSize().padding(top = 3.dp).background(edge, ChipShape))
        Row(
            Modifier
                .padding(bottom = 3.dp)
                .heightIn(min = 36.dp)
                .background(face, ChipShape)
                .border(2.dp, edge, ChipShape)
                .padding(horizontal = 14.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            CompositionLocalProvider(LocalContentColor provides content) {
                ProvideTextStyle(MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.ExtraBold)) {
                    leadingIcon?.invoke()
                    label()
                    trailingIcon?.invoke()
                }
            }
        }
    }
}
