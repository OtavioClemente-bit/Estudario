package br.com.estudario.ui.setup

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp

/** Mantém trilho e indicador visíveis enquanto houver conteúdo fora da tela. */
@Composable
internal fun SetupScrollContainer(
    modifier: Modifier = Modifier,
    showScrollIndicator: Boolean,
    content: @Composable ColumnScope.() -> Unit,
) {
    val scroll = rememberScrollState()
    val trackColor = MaterialTheme.colorScheme.outlineVariant
    val thumbColor = MaterialTheme.colorScheme.primary
    Column(modifier) {
        Box(Modifier.weight(1f).fillMaxWidth()) {
            Column(
                Modifier.fillMaxWidth().testTag("setup_scroll_content")
                    .verticalScroll(scroll)
                    .padding(top = 22.dp, bottom = 18.dp, end = if (showScrollIndicator) 18.dp else 0.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                content = content,
            )
            if (showScrollIndicator && scroll.maxValue > 0) {
                Canvas(Modifier.align(Alignment.CenterEnd).padding(vertical = 12.dp).fillMaxHeight().width(6.dp).testTag("setup_scrollbar")) {
                    val viewport = scroll.viewportSize.toFloat()
                    val thumbHeight = (size.height * viewport / (viewport + scroll.maxValue)).coerceIn(32.dp.toPx().coerceAtMost(size.height), size.height)
                    val top = (size.height - thumbHeight) * scroll.value / scroll.maxValue
                    val radius = CornerRadius(size.width / 2)
                    drawRoundRect(trackColor, cornerRadius = radius)
                    drawRoundRect(thumbColor, topLeft = Offset(0f, top), size = Size(size.width, thumbHeight), cornerRadius = radius)
                }
            }
        }
        if (showScrollIndicator && scroll.maxValue > 0) {
            Text(
                if (scroll.canScrollForward) "↓ Role para ver mais matérias e opções" else "Você chegou ao fim da lista",
                Modifier.padding(vertical = 8.dp), style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary,
            )
        }
    }
}
