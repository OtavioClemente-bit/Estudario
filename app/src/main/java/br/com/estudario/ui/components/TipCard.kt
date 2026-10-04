package br.com.estudario.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.estudario.ui.brand.BrandIcon
import br.com.estudario.ui.brand.Card
import br.com.estudario.ui.brand.Glyph
import br.com.estudario.ui.brand.Icon

/**
 * Dica ou pegadinha de banca. A dica acende a lâmpada; a pegadinha é a ratoeira com o queijo de
 * isca, porque é exatamente isso que ela é. As duas têm cor própria, fixa, para serem reconhecidas
 * de relance em qualquer tela.
 */
@Composable
fun TipCard(text: String, trap: Boolean, favorite: Boolean, onToggleFavorite: () -> Unit, modifier: Modifier = Modifier) {
    val dark = MaterialTheme.colorScheme.background.red < 0.5f
    val (face, ink, label) = when {
        trap && dark -> Triple(Color(0xFF3A1F1D), Color(0xFFFFB4A8), "PEGADINHA DA BANCA")
        trap -> Triple(Color(0xFFFFE9E5), Color(0xFFB3261E), "PEGADINHA DA BANCA")
        dark -> Triple(Color(0xFF3A2E12), Color(0xFFFFCF6B), "DICA QUE DECIDE QUESTÃO")
        else -> Triple(Color(0xFFFFF4D6), Color(0xFF8A5300), "DICA QUE DECIDE QUESTÃO")
    }
    Card(modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = face)) {
        Row(Modifier.padding(start = 12.dp, top = 12.dp, bottom = 12.dp), verticalAlignment = Alignment.Top) {
            BrandIcon(if (trap) Glyph.Trap else Glyph.Bulb, size = 48.dp)
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(label, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Black, color = ink)
                StudyInlineText(text, style = MaterialTheme.typography.bodyMedium)
            }
            IconButton(onClick = onToggleFavorite) {
                Icon(
                    if (favorite) Icons.Outlined.Star else Icons.Outlined.StarBorder,
                    if (favorite) "Tirar do Caderno" else "Guardar no Caderno",
                    tint = if (favorite) Color.Unspecified else MaterialTheme.colorScheme.outline,
                )
            }
        }
    }
}

enum class CalloutKind { TIP, TRAP }

private val TRAP_WORDS = listOf("pegadinha", "atenção", "atencao", "cuidado", "não confunda", "nao confunda", "erro comum", "armadilha")
private val TIP_WORDS = listOf("dica", "bizu", "macete", "lembre", "memorize", "resumindo", "na prova")

/** Citação do material que é, na verdade, uma dica ou um alerta. Só olha o começo do texto. */
fun calloutKindOf(block: String): CalloutKind? {
    val text = block.trimStart()
    if (!text.startsWith(">")) return null
    val head = text.removePrefix(">").trimStart('*', '_', ' ', '#').take(40).lowercase()
    return when {
        TRAP_WORDS.any { head.startsWith(it) || head.contains("$it:") } -> CalloutKind.TRAP
        TIP_WORDS.any { head.startsWith(it) || head.contains("$it:") } -> CalloutKind.TIP
        else -> null
    }
}

/** Tira o ">" de cada linha: o cartão já faz o papel da citação. */
fun calloutBody(block: String): String = block.lines().joinToString("\n") { it.trimStart().removePrefix(">").removePrefix(" ") }

/** Moldura de dica ou pegadinha no meio do texto de estudo, com o desenho grande ao lado. */
@Composable
fun CalloutFrame(kind: CalloutKind, modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    val trap = kind == CalloutKind.TRAP
    val dark = MaterialTheme.colorScheme.background.red < 0.5f
    val face = when {
        trap && dark -> Color(0xFF3A1F1D)
        trap -> Color(0xFFFFE9E5)
        dark -> Color(0xFF3A2E12)
        else -> Color(0xFFFFF4D6)
    }
    Card(modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = face)) {
        Row(Modifier.padding(start = 12.dp, top = 12.dp, bottom = 4.dp), verticalAlignment = Alignment.Top) {
            BrandIcon(if (trap) Glyph.Trap else Glyph.Bulb, size = 40.dp)
            Column(Modifier.weight(1f)) { content() }
        }
    }
}
