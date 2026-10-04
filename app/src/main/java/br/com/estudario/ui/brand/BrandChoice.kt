package br.com.estudario.ui.brand

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp

private val ChoiceLip = 4.dp
private val ChoiceShape = RoundedCornerShape(18.dp)

/**
 * Uma opção para escolher (tipo de treino, meta, ritmo, ajuste). É um bloco de verdade: borda
 * grossa, espessura embaixo, afunda ao toque. Escolhido, ganha a cor da marca por inteiro, borda,
 * fundo e espessura, para não haver dúvida do que está marcado.
 */
@Composable
fun BrandChoice(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    accent: Color = MaterialTheme.colorScheme.primary,
    content: @Composable RowScope.() -> Unit,
) {
    val scheme = MaterialTheme.colorScheme
    val face by animateColorAsState(if (selected) lerp(scheme.surface, accent, 0.14f) else scheme.surfaceContainerLowest, label = "choice-face")
    val edge by animateColorAsState(if (selected) accent else scheme.outlineVariant, label = "choice-edge")
    val lip by animateColorAsState(if (selected) lerp(accent, Color.Black, 0.25f) else scheme.outlineVariant, label = "choice-lip")
    val source = remember { MutableInteractionSource() }
    val pressed by source.collectIsPressedAsState()
    val sink by animateDpAsState(if (pressed) ChoiceLip else 0.dp, tween(70), label = "choice-press")

    Box(
        modifier
            .fillMaxWidth()
            .clip(ChoiceShape)
            .clickable(source, indication = null, role = Role.RadioButton, onClick = onClick),
        propagateMinConstraints = true,
    ) {
        Box(Modifier.matchParentSize().padding(top = ChoiceLip).background(lip, ChoiceShape))
        Row(
            Modifier
                .padding(bottom = ChoiceLip)
                .offset(y = sink)
                .background(face, ChoiceShape)
                .border(2.dp, edge, ChoiceShape)
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            content = content,
        )
    }
}

/** Marcador redondo da escolha: vazio, ou cheio com o certo desenhado. */
@Composable
fun BrandChoiceMark(selected: Boolean, accent: Color = MaterialTheme.colorScheme.primary, modifier: Modifier = Modifier) {
    val outline = MaterialTheme.colorScheme.outlineVariant
    Box(
        modifier
            .size(26.dp)
            .clip(CircleShape)
            .then(if (selected) Modifier.background(accent) else Modifier.border(2.5.dp, outline, CircleShape))
            .drawBehind {
                if (!selected) return@drawBehind
                val w = size.width
                val path = androidx.compose.ui.graphics.Path().apply {
                    moveTo(w * 0.28f, w * 0.52f); lineTo(w * 0.44f, w * 0.68f); lineTo(w * 0.73f, w * 0.36f)
                }
                drawPath(path, Color.White, style = Stroke(w * 0.12f, cap = StrokeCap.Round, join = androidx.compose.ui.graphics.StrokeJoin.Round))
            },
    )
}

