package br.com.estudario.web.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
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
fun Markdown(source: String, compact: Boolean = false) {
    Div({ classes(*listOfNotNull("reader", if (compact) "compact" else null).toTypedArray()) }) {
        parseBlocks(MathText.normalize(source)).forEach { block -> RenderBlock(block) }
    }
}

private sealed interface Block {
    data class Heading(val level: Int, val text: String) : Block
    data class Paragraph(val text: String) : Block
    data class ListBlock(val ordered: Boolean, val items: List<String>) : Block
    data class Quote(val text: String) : Block
    data class TableBlock(val header: List<String>, val rows: List<List<String>>) : Block
    data object Rule : Block
    data class Math(val tex: String) : Block
    data class CodeBlock(val code: String) : Block
    data class Chart(val source: String) : Block
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
            // Bloco entre ```: gráfico (```grafico) vira desenho; o resto é código, mostrado como está.
            trimmed.startsWith("```") -> {
                flush()
                val lang = trimmed.removePrefix("```").trim().lowercase()
                val body = StringBuilder()
                i++
                while (i < lines.size && !lines[i].trim().startsWith("```")) { body.append(lines[i]).append('\n'); i++ }
                i++
                blocks += if (lang in WebChart.fences) Block.Chart(body.toString()) else Block.CodeBlock(body.toString().trimEnd('\n'))
            }
            trimmed == "\$\$" -> {
                flush()
                val tex = StringBuilder()
                i++
                while (i < lines.size && lines[i].trim() != "\$\$") { tex.append(lines[i]).append('\n'); i++ }
                i++
                blocks += Block.Math(tex.toString().trim())
            }
            MathText.lone.matches(trimmed) -> { flush(); blocks += Block.Math(MathText.lone.find(trimmed)!!.groupValues[1].trim()); i++ }
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
        is Block.TableBlock -> Div({ classes("table-wrap") }) { Table {
            Thead { Tr { block.header.forEach { Th { Inline(it) } } } }
            Tbody { block.rows.forEach { row -> Tr { row.forEach { Td { Inline(it) } } } } }
        } }
        Block.Rule -> Hr()
        is Block.Math -> MathTex(block.tex, display = true)
        is Block.CodeBlock -> org.jetbrains.compose.web.dom.Pre({ classes("code-block") }) { Code { Text(block.code) } }
        is Block.Chart -> ChartBlock(block.source)
    }
}

/**
 * **negrito**, *itálico* / _itálico_, `código` e links. O link vira um texto curto clicável: o
 * endereço inteiro nunca aparece no meio do estudo (com a URL crua o texto ficava ilegível).
 */
@Composable
fun Inline(text: String) {
    val pattern = Regex("(\\\$\\\$[^\\n]+?\\\$\\\$|!?\\[[^\\]]*\\]\\([^)\\s]+\\)|<?https?://[^\\s<>)]+>?|\\*\\*[^*]+\\*\\*|__[^_]+__|`[^`]+`|\\*[^*\\s][^*]*\\*|(?<![\\w/])_[^_\\s][^_]*_(?!\\w))")
    var last = 0
    pattern.findAll(text).forEach { match ->
        if (match.range.first > last) Text(text.substring(last, match.range.first))
        val token = match.value
        when {
            token.startsWith("\$\$") -> MathTex(token.substring(2, token.length - 2).trim(), display = false)
            token.startsWith("![") -> {
                val url = token.substringAfter("](").dropLast(1)
                if (url.startsWith("https://")) org.jetbrains.compose.web.dom.Img(src = url, alt = token.substring(2).substringBefore("]"), attrs = { classes("md-img") })
            }
            token.startsWith("[") -> {
                val label = token.substring(1).substringBefore("](")
                val url = token.substringAfter("](").dropLast(1)
                LinkOut(url, label.ifBlank { hostOf(url) })
            }
            token.startsWith("http") || token.startsWith("<http") -> {
                val raw = token.trim('<', '>')
                val url = raw.trimEnd('.', ',', ';', ':')
                LinkOut(url, hostOf(url))
                if (raw.length > url.length) Text(raw.substring(url.length))
            }
            token.startsWith("**") || token.startsWith("__") -> B { Text(token.substring(2, token.length - 2)) }
            token.startsWith("`") -> Code { Text(token.substring(1, token.length - 1)) }
            else -> Em { Text(token.substring(1, token.length - 1)) }
        }
        last = match.range.last + 1
    }
    if (last < text.length) Text(text.substring(last))
}

private fun hostOf(url: String): String = url.substringAfter("://").substringBefore('/').removePrefix("www.").ifBlank { "fonte" }

@Composable
private fun LinkOut(url: String, label: String) {
    if (!url.startsWith("http://") && !url.startsWith("https://")) { Text(label); return }
    org.jetbrains.compose.web.dom.A(href = url, { classes("md-link"); attr("target", "_blank"); attr("rel", "noopener noreferrer"); attr("title", url) }) { Text(label) }
}

/**
 * Fórmulas (StudyMarkdownNormalizer do app): `\( \)` e `\[ \]` viram `$$`, e valor em reais
 * escrito como fórmula volta a ser texto. `$` sozinho fica como está, porque também é cifrão.
 */
object MathText {
    private const val D = "$"
    private const val BS = "\\"
    private val displayBrackets = Regex("${BS}${BS}${BS}[([${BS}s${BS}S]+?)${BS}${BS}${BS}]")
    private val inlineParens = Regex("${BS}${BS}${BS}(([${BS}s${BS}S]+?)${BS}${BS}${BS})")
    private val moneyFormula = Regex("${BS}$D${BS}$D${BS}s*(?:${BS}${BS}text${BS}{)?R${BS}${BS}${BS}$D${BS}}?${BS}s*(?:${BS}${BS}[,;! ])?${BS}s*([${BS}d.,]+)${BS}s*${BS}$D${BS}$D")

    /** Fórmula sozinha numa linha: vira fórmula de bloco, centralizada, como num livro. */
    val lone = Regex("^${BS}$D${BS}$D([^$D]+)${BS}$D${BS}$D$")

    fun normalize(markdown: String): String = markdown
        .replace("\r\n", "\n")
        .replace(displayBrackets) { "\n$D$D\n${it.groupValues[1].trim()}\n$D$D\n" }
        .replace(inlineParens) { "$D$D${it.groupValues[1].trim()}$D$D" }
        .replace(moneyFormula) { "R$D ${it.groupValues[1]}" }

    private val symbols = listOf(
        "cdot" to "·", "times" to "×", "div" to "÷", "leq" to "≤", "le" to "≤", "geq" to "≥", "ge" to "≥",
        "neq" to "≠", "ne" to "≠", "approx" to "≈", "infty" to "∞", "pm" to "±", "Delta" to "Δ", "pi" to "π",
        "to" to "→", "Rightarrow" to "⇒", "%" to "%", "," to " ", ";" to " ", "lfloor" to "⌊", "rfloor" to "⌋",
    ).map { (name, symbol) -> BS + name to symbol }.sortedByDescending { it.first.length }

    // Em MathML o KaTeX desenha "ç" como "c" com uma cedilha solta embaixo e "í" como "ı" com o acento
    // em cima ("endereço físico" vira "endere c ¸ o f ı ˊ sico"). Depois de desenhar, cada letra com
    // acento solto volta a ser a letra acentuada de verdade.
    private val combining = mapOf(
        "´" to "́", "ˊ" to "́", "`" to "̀", "ˋ" to "̀", "^" to "̂", "ˆ" to "̂",
        "~" to "̃", "˜" to "̃", "¨" to "̈", "¸" to "̧",
    )

    fun fixAccents(root: dynamic) {
        val nodes = root.querySelectorAll("mover, munder")
        val count = nodes.length as Int
        for (i in count - 1 downTo 0) {
            val el = nodes[i]
            if ((el.children.length as Int) != 2) continue
            val baseEl = el.children[0]
            val base = (baseEl.textContent as String?) ?: continue
            val mark = combining[((el.children[1].textContent as String?) ?: "").trim()] ?: continue
            if (base.length != 1) continue
            val letter = when (base) { "ı" -> "i"; "ȷ" -> "j"; else -> base }
            val composed = (letter + mark).asDynamic().normalize("NFC") as String
            val replacement = kotlinx.browser.document.createElementNS("http://www.w3.org/1998/Math/MathML", baseEl.tagName as String)
            val variant = baseEl.getAttribute("mathvariant") as String?
            if (variant != null) replacement.setAttribute("mathvariant", variant)
            replacement.textContent = composed
            el.replaceWith(replacement)
        }
    }

    /** A fórmula como texto simples (se o desenhista de fórmulas não carregar): \frac{a}{b} vira (a)/(b). */
    fun plain(tex: String): String {
        var out = tex.replace(Regex("${BS}${BS}frac${BS}{([^{}]*)${BS}}${BS}{([^{}]*)${BS}}"), "($1)/($2)")
            .replace(Regex("${BS}${BS}sqrt${BS}{([^{}]*)${BS}}"), "√($1)")
            .replace(Regex("${BS}${BS}(?:text|mathrm|mathbf|operatorname)${BS}{([^{}]*)${BS}}"), "$1")
        symbols.forEach { (latex, symbol) -> out = out.replace(latex, symbol) }
        return out.replace(Regex("${BS}${BS}left|${BS}${BS}right"), "").replace(Regex("${BS}${BS}[a-zA-Z]+"), "")
            .replace("{", "").replace("}", "").replace(Regex("${BS}s+"), " ").trim()
    }
}

/**
 * Uma fórmula, desenhada pelo KaTeX (katex.min.js) em MathML, que o navegador mostra com as próprias
 * fontes. O KaTeX recebe só o texto da fórmula e não executa nada dela; sem ele, fica o texto simples.
 */
@Composable
fun MathTex(tex: String, display: Boolean) {
    key(tex, display) {
        org.jetbrains.compose.web.dom.Span({
            classes(if (display) "math-block" else "math-inline")
            ref { element ->
                val katex = kotlinx.browser.window.asDynamic().katex
                val ok = katex != null && runCatching {
                    katex.render(tex, element, kotlin.js.json("displayMode" to display, "output" to "mathml", "throwOnError" to false, "strict" to "ignore", "trust" to false))
                    MathText.fixAccents(element)
                }.isSuccess
                if (!ok) element.textContent = MathText.plain(tex)
                onDispose { }
            }
        })
    }
}
