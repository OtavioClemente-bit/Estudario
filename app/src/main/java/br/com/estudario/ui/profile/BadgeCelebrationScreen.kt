package br.com.estudario.ui.profile

import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.estudario.domain.Badge
import br.com.estudario.ui.brand.Button

private fun faixaLabel(tier: Int) = when (tier) {
    1 -> "Bronze"; 2 -> "Prata"; 3 -> "Ouro"; 4 -> "Safira"; 5 -> "Rubi"; else -> "Esmeralda"
}

/**
 * Emblema novo (a mesma cena do site, "a forja"): o Folha, de martelo na mão, cunha o símbolo da
 * categoria num disco em brasa na bigorna em três marteladas; o metal esfria no vapor e a medalha sobe
 * girando até o centro, ganha a fita, a moldura do nível e a faixa, e o Folha comemora erguendo o martelo.
 * Quando cai mais de um emblema de uma vez, cada um ganha a sua cena, em sequência.
 */
@Composable
fun BadgeCelebrationScreen(badges: List<Badge>, onClose: () -> Unit) {
    if (badges.isEmpty()) return
    var index by remember(badges) { mutableIntStateOf(0) }
    val badge = badges.getOrNull(index) ?: return
    SceneCelebration(
        scene = listOf("tipo" to "emblema", "tier" to badge.tier.toString(), "categoria" to badge.id.substringBefore("-"), "faixa" to faixaLabel(badge.tier)),
        reveal = 4.35f,
        onDismiss = onClose,
        sceneKey = index,
        text = {
            CelEyebrow("NOVO EMBLEMA · ${faixaLabel(badge.tier).uppercase()}" + if (badges.size > 1) "  ${index + 1}/${badges.size}" else "")
            CelTitle(badge.name)
            CelBody(badge.requirement)
            CelChip(badge.category.label)
        },
        actions = {
            Button(
                onClick = { if (index < badges.lastIndex) index++ else onClose() },
                modifier = Modifier.widthIn(min = 200.dp).heightIn(min = 52.dp), shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = CelGold, contentColor = CelMid),
            ) { Text(if (index < badges.lastIndex) "Próximo emblema" else "Continuar", fontWeight = FontWeight.Black) }
        },
    )
}
