package br.com.estudario.web.ui

import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.dom.Blockquote
import org.jetbrains.compose.web.dom.Code
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Em
import org.jetbrains.compose.web.dom.H2
import org.jetbrains.compose.web.dom.H3
import org.jetbrains.compose.web.dom.H4
import org.jetbrains.compose.web.dom.Hr
import org.jetbrains.compose.web.dom.Li
import org.jetbrains.compose.web.dom.Ol
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.B
import org.jetbrains.compose.web.dom.Table
import org.jetbrains.compose.web.dom.Tbody
import org.jetbrains.compose.web.dom.Td
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.dom.Th
import org.jetbrains.compose.web.dom.Thead
import org.jetbrains.compose.web.dom.Tr
import org.jetbrains.compose.web.dom.Ul

/**
 * Markdown das teorias e resumos (o mesmo subconjunto que o app desenha): títulos, parágrafos,
 * listas, citações, tabelas, negrito, itálico e código. Tudo vira elemento HTML, nunca innerHTML,
 * então texto vindo do material não consegue injetar nada na página.
 */
@Composable
fun Markdown(source: String) {
    Div({ classes("reader") }) {
        parseBlocks(source).forEach { block -> RenderBlock(block) }
    }
}

private sealed interface Block {
    data class Heading(val level: Int, val text: String) : Block
    data class Paragraph(val text: String) : Block
    data class ListBlock(val ordered: Boolean, val items: List<String>) : Block
    data class Quote(val text: String) : Block
    data class TableBlock(val header: List<String>, val rows: List<List<String>>) : Block
    data object Rule : Block
}

private fun parseBlocks(source: String): List<Block> {
    val lines = source.replace("\r\n", "\n").split('\n')
    val blocks = mutableListOf<Block>()
    var i = 0
    val paragraph = StringBuilder()
    fun flush() { if (paragraph.isNotBlank()) blocks += Block.Paragraph(paragraph.toString().trim()); paragraph.clear() }
    while (i < lines.size) {
        val line = lines[i]
        val trimmed = line.trim()
        when {
            trimmed.isEmpty() -> { flush(); i++ }
            trimmed.startsWith("#") -> {
                flush()
                val level = trimmed.takeWhile { it == '#' }.length.coerceIn(1, 4)
                blocks += Block.Heading(level, trimmed.drop(level).trim())
                i++
            }
            trimmed == "---" || trimmed == "***" -> { flush(); blocks += Block.Rule; i++ }
            trimmed.startsWith(">") -> {
                flush()
                val quote = StringBuilder()
                while (i < lines.size && lines[i].trim().startsWith(">")) { quote.append(lines[i].trim().removePrefix(">").trim()).append(' '); i++ }
                blocks += Block.Quote(quote.toString().trim())
            }
            trimmed.startsWith("|") && i + 1 < lines.size && lines[i + 1].trim().matches(Regex("^\\|?\\s*:?-{2,}.*")) -> {
                flush()
                fun cells(row: String) = row.trim().trim('|').split('|').map { it.trim() }
                val header = cells(trimmed)
                i += 2
                val rows = mutableListOf<List<String>>()
                while (i < lines.size && lines[i].trim().startsWith("|")) { rows += cells(lines[i]); i++ }
                blocks += Block.TableBlock(header, rows)
            }
            Regex("^([-*+]|\\d+[.)])\\s+").containsMatchIn(trimmed) -> {
                flush()
                val ordered = trimmed.first().isDigit()
                val items = mutableListOf<String>()
                while (i < lines.size) {
                    val current = lines[i].trim()
                    val match = Regex("^([-*+]|\\d+[.)])\\s+(.*)").find(current)
                    when {
                        match != null -> items += match.groupValues[2]
                        current.isNotEmpty() && items.isNotEmpty() && lines[i].startsWith(" ") -> items[items.lastIndex] = items.last() + " " + current
                        else -> break
                    }
                    i++
                }
                blocks += Block.ListBlock(ordered, items)
            }
            else -> { paragraph.append(trimmed).append(' '); i++ }
        }
    }
    flush()
    return blocks
}

@Composable
private fun RenderBlock(block: Block) {
    when (block) {
        is Block.Heading -> when (block.level) {
            1, 2 -> H2 { Inline(block.text) }
            3 -> H3 { Inline(block.text) }
            else -> H4 { Inline(block.text) }
        }
        is Block.Paragraph -> P { Inline(block.text) }
        is Block.Quote -> Blockquote { Inline(block.text) }
        is Block.ListBlock -> if (block.ordered) Ol { block.items.forEach { Li { Inline(it) } } } else Ul { block.items.forEach { Li { Inline(it) } } }
        is Block.TableBlock -> Table {
            Thead { Tr { block.header.forEach { Th { Inline(it) } } } }
            Tbody { block.rows.forEach { row -> Tr { row.forEach { Td { Inline(it) } } } } }
        }
        Block.Rule -> Hr()
    }
}

/** **negrito**, *itálico* / _itálico_ e `código`. */
@Composable
fun Inline(text: String) {
    val pattern = Regex("(\\*\\*[^*]+\\*\\*|__[^_]+__|`[^`]+`|\\*[^*\\s][^*]*\\*|_[^_\\s][^_]*_)")
    var last = 0
    pattern.findAll(text).forEach { match ->
        if (match.range.first > last) Text(text.substring(last, match.range.first))
        val token = match.value
        when {
            token.startsWith("**") || token.startsWith("__") -> B { Text(token.substring(2, token.length - 2)) }
            token.startsWith("`") -> Code { Text(token.substring(1, token.length - 1)) }
            else -> Em { Text(token.substring(1, token.length - 1)) }
        }
        last = match.range.last + 1
    }
    if (last < text.length) Text(text.substring(last))
}
