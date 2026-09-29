package br.com.estudario.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.outlined.Bookmark
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Style
import androidx.compose.material.icons.outlined.TouchApp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import br.com.estudario.ui.theme.EstudarioShapes
import kotlinx.coroutines.launch

/** Um cartão: frente (termo ou pergunta) e verso (explicação, com Markdown). Verso vazio = cartão de leitura. */
data class Flashcard(val front: String, val back: String)

/**
 * Transforma a revisão rápida (Markdown) em cartões:
 * - seções ## / ### viram cartão (título na frente, texto no verso);
 * - linhas de tabela viram cartão (1ª coluna na frente, as demais no verso, com o cabeçalho);
 * - itens de lista "**termo**: explicação" ou "termo: explicação" viram frente e verso.
 * Sem nada disso, cada parágrafo vira um cartão de leitura.
 */
object FlashcardParser {
    private val termLine = Regex("""^\*\*(.+?)\*\*\s*[:\u2014\u2013-]\s*(.+)$""")
    private val plainTerm = Regex("""^([^:]{2,80}):\s+(.+)$""")
    private val listMarker = Regex("""^(\s*)([-*+]|\d+[.)])\s+""")

    fun parse(markdown: String): List<Flashcard> {
        val text = StudyMarkdownNormalizer.normalize(markdown).trim()
        if (text.isBlank()) return emptyList()
        val lines = text.lines()
        val cards = mutableListOf<Flashcard>()

        // Seções com título: o modelo mais explícito de frente e verso.
        val headingIndexes = lines.indices.filter { lines[it].trimStart().startsWith("##") }
        if (headingIndexes.isNotEmpty()) {
            headingIndexes.forEachIndexed { position, start ->
                val end = headingIndexes.getOrNull(position + 1) ?: lines.size
                val front = lines[start].trimStart().trimStart('#').trim()
                val back = lines.subList(start + 1, end).joinToString("\n").trim()
                if (front.isNotBlank()) cards += Flashcard(front, back)
            }
            return cards
        }

        var index = 0
        while (index < lines.size) {
            val line = lines[index].trim()
            when {
                line.isBlank() || line.startsWith("# ") -> index++
                line.startsWith("|") -> {
                    val table = mutableListOf<String>()
                    while (index < lines.size && lines[index].trim().startsWith("|")) { table += lines[index].trim(); index++ }
                    cards += tableCards(table)
                }
                else -> {
                    // Um item de lista (com suas linhas de continuação) ou um parágrafo.
                    val block = StringBuilder(line.replace(listMarker, ""))
                    index++
                    while (index < lines.size) {
                        val next = lines[index]
                        if (next.isBlank() || listMarker.containsMatchIn(next) && !next.startsWith("   ") || next.trim().startsWith("|")) break
                        block.append('\n').append(next.trim()); index++
                    }
                    cards += itemCard(block.toString().trim())
                }
            }
        }
        return cards.filter { it.front.isNotBlank() }
    }

    private fun itemCard(item: String): Flashcard {
        val firstLine = item.lineSequence().first()
        val rest = item.lines().drop(1).joinToString("\n").trim()
        termLine.find(firstLine)?.let { match ->
            return Flashcard(match.groupValues[1].trim(), listOf(match.groupValues[2].trim(), rest).filter(String::isNotBlank).joinToString("\n\n"))
        }
        plainTerm.find(firstLine)?.let { match ->
            return Flashcard(match.groupValues[1].replace("**", "").trim(), listOf(match.groupValues[2].trim(), rest).filter(String::isNotBlank).joinToString("\n\n"))
        }
        return Flashcard(item, "")
    }

    private fun tableCards(rows: List<String>): List<Flashcard> {
        fun cells(row: String) = row.trim().trim('|').split('|').map { it.trim() }
        val body = rows.filterNot { it.replace("|", "").replace(":", "").replace("-", "").isBlank() }
        if (body.size < 2) return emptyList()
        val header = cells(body.first())
        return body.drop(1).mapNotNull { row ->
            val values = cells(row)
            val front = values.firstOrNull()?.takeIf { it.isNotBlank() } ?: return@mapNotNull null
            val back = values.drop(1).mapIndexed { i, value -> "**${header.getOrElse(i + 1) { "" }}**: $value" }.joinToString("\n\n")
            Flashcard(front, back)
        }
    }
}

/**
 * Baralho em tela cheia: toque no cartão para virar, deslize para o próximo. O verso aceita tabela,
 * fórmula e destaque. [saved] guarda o baralho no Caderno de estudo.
 */
@Composable
fun FlashcardDeckDialog(title: String, cards: List<Flashcard>, saved: Boolean, onToggleSave: () -> Unit, onDismiss: () -> Unit) {
    val pager = rememberPagerState { cards.size }
    val flipped = remember { mutableStateMapOf<Int, Boolean>() }
    val scope = rememberCoroutineScope()
    Dialog(onDismissRequest = onDismiss, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            Column(Modifier.fillMaxSize().statusBarsPadding().navigationBarsPadding()) {
                Row(Modifier.fillMaxWidth().padding(start = 16.dp, end = 4.dp, top = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Outlined.Style, null, tint = MaterialTheme.colorScheme.primary)
                    Spacer(Modifier.width(10.dp))
                    Column(Modifier.weight(1f)) {
                        Text("Flashcards", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                        Text(title, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1)
                    }
                    IconButton(onClick = onToggleSave) {
                        Icon(if (saved) Icons.Outlined.Bookmark else Icons.Outlined.BookmarkBorder, if (saved) "Remover dos salvos" else "Salvar baralho", tint = MaterialTheme.colorScheme.primary)
                    }
                    IconButton(onClick = onDismiss) { Icon(Icons.Outlined.Close, "Fechar") }
                }
                if (cards.isEmpty()) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("Este material não tem cartões.") }
                    return@Column
                }
                Column(Modifier.padding(horizontal = 20.dp, vertical = 8.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("Cartão ${pager.currentPage + 1} de ${cards.size}", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
                    LinearProgressIndicator({ (pager.currentPage + 1f) / cards.size }, Modifier.fillMaxWidth())
                }
                HorizontalPager(pager, Modifier.weight(1f).fillMaxWidth(), contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 20.dp), pageSpacing = 12.dp) { page ->
                    val card = cards[page]
                    val isFlipped = flipped[page] == true && card.back.isNotBlank()
                    val rotation by animateFloatAsState(if (isFlipped) 180f else 0f, tween(450), label = "flip")
                    Surface(
                        Modifier
                            .fillMaxSize()
                            .padding(vertical = 12.dp)
                            .graphicsLayer { rotationY = rotation; cameraDistance = 12f * density }
                            .clickable(enabled = card.back.isNotBlank()) { flipped[page] = !(flipped[page] ?: false) },
                        shape = EstudarioShapes.spotlight,
                        color = if (rotation <= 90f) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceContainerHigh,
                        tonalElevation = 3.dp,
                    ) {
                        // O verso é desenhado espelhado de volta para ler normalmente depois do giro.
                        Box(Modifier.fillMaxSize().graphicsLayer { rotationY = if (rotation > 90f) 180f else 0f }.padding(24.dp)) {
                            if (rotation <= 90f) {
                                Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(if (card.back.isNotBlank()) "FRENTE" else "PARA REVISAR", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                                    Spacer(Modifier.height(12.dp))
                                    StudyMarkdown(card.front, Modifier.fillMaxWidth(), textSizeSp = if (card.back.isNotBlank()) 22f else 17f)
                                    if (card.back.isNotBlank()) {
                                        Spacer(Modifier.height(20.dp))
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(Icons.Outlined.TouchApp, null, Modifier.size(16.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
                                            Spacer(Modifier.width(6.dp))
                                            Text("Tente lembrar e toque para virar", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center)
                                        }
                                    }
                                }
                            } else {
                                Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                    Text("VERSO", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                                    Text(card.front.replace("**", ""), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                                    StudyMarkdown(card.back, Modifier.fillMaxWidth())
                                }
                            }
                        }
                    }
                }
                Row(Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 12.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedButton(onClick = { scope.launch { pager.animateScrollToPage((pager.currentPage - 1).coerceAtLeast(0)) } }, enabled = pager.currentPage > 0, modifier = Modifier.weight(1f)) {
                        Icon(Icons.AutoMirrored.Outlined.ArrowBack, null); Spacer(Modifier.width(6.dp)); Text("Anterior")
                    }
                    OutlinedButton(onClick = { scope.launch { pager.animateScrollToPage((pager.currentPage + 1).coerceAtMost(cards.lastIndex)) } }, enabled = pager.currentPage < cards.lastIndex, modifier = Modifier.weight(1f)) {
                        Text("Próximo"); Spacer(Modifier.width(6.dp)); Icon(Icons.AutoMirrored.Outlined.ArrowForward, null)
                    }
                }
            }
        }
    }
}
