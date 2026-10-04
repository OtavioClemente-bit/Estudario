package br.com.estudario.ui.brand

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class BrandTab(val route: String, val label: String, val glyph: Glyph)

/**
 * A barra de baixo. Aba atual em cor e dentro de uma moldura; as outras em cinza, esperando.
 * O ícone dá um pulinho quando a aba é escolhida.
 */
@Composable
fun BrandBottomBar(
    tabs: List<BrandTab>,
    currentRoute: String?,
    onSelect: (String) -> Unit,
    modifier: Modifier = Modifier,
    itemModifier: (BrandTab) -> Modifier = { Modifier },
) {
    val line = MaterialTheme.colorScheme.outlineVariant
    Column(modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surface)) {
        Box(Modifier.fillMaxWidth().height(2.dp).background(line))
        Row(
            Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.navigationBars)
                .padding(horizontal = 6.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            tabs.forEach { tab ->
                val selected = tab.route == currentRoute
                val bounce by animateFloatAsState(
                    if (selected) 1.12f else 1f,
                    spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessMedium),
                    label = "tab-bounce",
                )
                val shape = RoundedCornerShape(14.dp)
                Column(
                    itemModifier(tab)
                        .weight(1f)
                        .padding(horizontal = 2.dp)
                        .clip(shape)
                        .then(
                            if (selected) Modifier
                                .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.55f))
                                .border(2.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.55f), shape)
                            else Modifier,
                        )
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            role = Role.Tab,
                        ) { onSelect(tab.route) }
                        .semantics { this.selected = selected }
                        .padding(vertical = 6.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    BrandIcon(tab.glyph, Modifier.scale(bounce), size = 30.dp, muted = !selected)
                    Text(
                        tab.label,
                        fontSize = 11.sp,
                        fontWeight = if (selected) FontWeight.ExtraBold else FontWeight.SemiBold,
                        color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1, softWrap = false, overflow = TextOverflow.Ellipsis,
                    )
                }
            }
        }
    }
}
