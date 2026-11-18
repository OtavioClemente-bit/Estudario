package br.com.estudario.ui.components

import androidx.compose.ui.draw.clip
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.Report
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

/** Cores suaves de dica (verde) e pegadinha (coral), em claro e escuro: fundo claro, tinta e destaque. */
@Composable
private fun tipColors(trap: Boolean): Triple<Color, Color, Color> {
    val dark = MaterialTheme.colorScheme.background.red < 0.5f
    val accent = if (trap) Color(0xFFE5484D) else Color(0xFF1FA97F)
    val face = androidx.compose.ui.graphics.lerp(MaterialTheme.colorScheme.surface, accent, if (dark) 0.08f else 0.04f)
    val ink = when {
        trap && dark -> Color(0xFFFFB4A8)
        trap -> Color(0xFFB3261E)
        dark -> Color(0xFF7EE0B8)
        else -> Color(0xFF0E7A57)
    }
    return Triple(face, ink, accent)
}

/** O ícone de dica ou de pegadinha numa bolinha clara, à esquerda de cada cartão. */
@Composable
private fun TipBadge(trap: Boolean, size: androidx.compose.ui.unit.Dp = 38.dp) {
    val (_, ink, accent) = tipColors(trap)
    Box(Modifier.size(size).clip(androidx.compose.foundation.shape.RoundedCornerShape(12.dp)).background(accent.copy(alpha = 0.14f)), contentAlignment = Alignment.Center) {
        Icon(if (trap) Icons.Outlined.Report else Icons.Outlined.Lightbulb, null, Modifier.size(size * 0.58f), tint = ink)
    }
}

/** "**Título:** resto" vira título curto e texto; sem negrito no começo, é só texto. */
fun tipParts(text: String): Pair<String?, String> {
    val match = Regex("""^\s*\*\*([^*]{2,70})\*\*\s*[:.]?\s*""").find(text) ?: return null to text
    val rest = text.substring(match.range.last + 1).trim()
    return if (rest.isBlank()) null to text else match.groupValues[1].trim().trimEnd(':', '.').trim() to rest
}

/** Cabeçalho leve de um grupo de dicas ou de pegadinhas: rótulo e contador, sem fundo pesado. */
@Composable
fun TipGroupHeader(trap: Boolean, count: Int, modifier: Modifier = Modifier) {
    val (_, ink, _) = tipColors(trap)
    Row(modifier.fillMaxWidth().padding(top = 8.dp, bottom = 2.dp), verticalAlignment = Alignment.CenterVertically) {
        TipBadge(trap, 30.dp)
        Spacer(Modifier.width(10.dp))
        Text(if (trap) "Pegadinhas da banca" else "Dicas que decidem questão", Modifier.weight(1f), style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Black, color = ink)
        Text("$count", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

/** Uma dica: cartão curto e claro, ícone à esquerda, título em negrito quando houver e a estrela do Caderno. */
@Composable
fun TipRow(text: String, trap: Boolean, favorite: Boolean, onToggleFavorite: () -> Unit, modifier: Modifier = Modifier) {
    val (face, ink, accent) = tipColors(trap)
    val (title, body) = tipParts(text)
    val shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)
    Row(
        modifier.fillMaxWidth().clip(shape).background(face).border(1.5.dp, accent.copy(alpha = 0.32f), shape).padding(start = 14.dp, top = 14.dp, bottom = 14.dp),
        verticalAlignment = Alignment.Top,
    ) {
        TipBadge(trap)
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(if (trap) "PEGADINHA" else "DICA", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Black, color = ink)
            if (title != null) StudyInlineText(title, style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
            StudyInlineText(body, style = MaterialTheme.typography.bodyMedium)
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

enum class CalloutKind { TIP, TRAP }

private val TRAP_WORDS = listOf("pegadinha", "atenção", "atencao", "cuidado", "não confunda", "nao confunda", "erro comum", "armadilha")
private val TIP_WORDS = listOf("dica", "bizu", "macete", "lembre", "memorize", "resumindo", "na prova")

/** Citação do material que é, na verdade, uma dica ou um alerta. Só olha o começo do texto. */
fun calloutKindOf(block: String): CalloutKind? {
    val text = block.trimStart()
    // "**Pegadinha:** ..." e "**Resumo para a prova:** ..." no fim dos capítulos também viram cartão.
    if (!text.startsWith(">")) return boldCalloutKindOf(text)
    val head = text.removePrefix(">").trimStart('*', '_', ' ', '#').take(40).lowercase()
    return when {
        TRAP_WORDS.any { head.startsWith(it) || head.contains("$it:") } -> CalloutKind.TRAP
        TIP_WORDS.any { head.startsWith(it) || head.contains("$it:") } -> CalloutKind.TIP
        else -> null
    }
}

/** Parágrafo que começa com um rótulo curto em negrito de dica ou alerta (o mesmo do site). */
private fun boldCalloutKindOf(text: String): CalloutKind? {
    if (text.lines().any { it.trimStart().startsWith("|") }) return null
    val match = Regex("""^\*\*([^*]{2,40})\*\*""").find(text) ?: return null
    val label = match.groupValues[1].trim().lowercase()
    if (!label.endsWith(":") && !text.substring(match.range.last + 1).trimStart().startsWith(":")) return null
    return when {
        TRAP_WORDS.any { label.startsWith(it) } -> CalloutKind.TRAP
        TIP_WORDS.any { label.startsWith(it) } || label.startsWith("resumo") -> CalloutKind.TIP
        else -> null
    }
}

/** Tira o ">" de cada linha: o cartão já faz o papel da citação. */
fun calloutBody(block: String): String = block.lines().joinToString("\n") { it.trimStart().removePrefix(">").removePrefix(" ") }

/** Moldura de dica ou pegadinha no meio do texto de estudo: cartão claro com o ícone ao lado. */
@Composable
fun CalloutFrame(kind: CalloutKind, modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    val trap = kind == CalloutKind.TRAP
    val (face, _, accent) = tipColors(trap)
    val shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)
    Row(modifier.fillMaxWidth().clip(shape).background(face).border(1.5.dp, accent.copy(alpha = 0.32f), shape).padding(start = 12.dp, top = 12.dp, bottom = 4.dp), verticalAlignment = Alignment.Top) {
        TipBadge(trap, 34.dp)
        Column(Modifier.weight(1f)) { content() }
    }
}
