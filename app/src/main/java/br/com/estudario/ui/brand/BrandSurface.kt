package br.com.estudario.ui.brand

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import br.com.estudario.ui.theme.EstudarioShapes

/**
 * O `Surface` do app. Os blocos grandes (formatos [EstudarioShapes.spotlight] e
 * [EstudarioShapes.panel]) ganham a mesma espessura dos botões e cartões; pílulas, diálogos e
 * fundos continuam exatamente como o Material desenha.
 */
@Composable
fun Surface(
    modifier: Modifier = Modifier,
    shape: Shape = RectangleShape,
    color: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = contentColorFor(color),
    tonalElevation: Dp = 0.dp,
    shadowElevation: Dp = 0.dp,
    border: BorderStroke? = null,
    content: @Composable () -> Unit,
) = androidx.compose.material3.Surface(
    modifier.chunkyFor(shape, color), shape, color, contentColor, tonalElevation, shadowElevation, border ?: edgeFor(shape, color), content,
)

@Composable
fun Surface(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = RectangleShape,
    color: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = contentColorFor(color),
    tonalElevation: Dp = 0.dp,
    shadowElevation: Dp = 0.dp,
    border: BorderStroke? = null,
    interactionSource: MutableInteractionSource? = null,
    content: @Composable () -> Unit,
) = androidx.compose.material3.Surface(
    onClick, modifier.chunkyFor(shape, color), enabled, shape, color, contentColor, tonalElevation, shadowElevation,
    border ?: edgeFor(shape, color), interactionSource, content,
)

private fun isBlock(shape: Shape) = shape === EstudarioShapes.spotlight || shape === EstudarioShapes.panel

@Composable
private fun lipColor(color: Color): Color {
    val face = if (color.alpha < 1f) color.compositeOver(MaterialTheme.colorScheme.surface) else color
    val plain = face == MaterialTheme.colorScheme.surface || face == MaterialTheme.colorScheme.surfaceContainerLow ||
        face == MaterialTheme.colorScheme.surfaceContainer || face == MaterialTheme.colorScheme.surfaceContainerLowest
    return if (plain) MaterialTheme.colorScheme.outlineVariant else lerp(face, Color.Black, 0.32f)
}

@Composable
private fun edgeFor(shape: Shape, color: Color): BorderStroke? {
    if (!isBlock(shape) || color.alpha == 0f) return null
    return BorderStroke(2.dp, lipColor(color).copy(alpha = 0.55f))
}

/** Desenha a espessura 4dp abaixo do bloco, fora dos limites dele. */
@Composable
private fun Modifier.chunkyFor(shape: Shape, color: Color): Modifier {
    if (!isBlock(shape) || color.alpha == 0f) return this
    val lip = lipColor(color)
    return this.drawBehind {
        val outline = shape.createOutline(size, layoutDirection, this)
        translate(0f, 4.dp.toPx()) { drawOutline(outline, lip) }
    }
}

