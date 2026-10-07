package br.com.estudario.web.ui

import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.dom.B
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text

/** Enunciado separado em texto-base, comando e fonte (a fonte vai para o fim, fora do texto). */
data class StatementParts(val label: String?, val title: String?, val base: String, val command: String?, val source: String?)

private val sourceParen = Regex("""\s*\(((?:[^()]|\([^()]*\))*?(?:fonte|https?://|adapta|dispon[ií]vel)(?:[^()]|\([^()]*\))*)\)""", RegexOption.IGNORE_CASE)
private val header = Regex("""^\s*\**\s*(TEXTO[- ]BASE(?:\s*[IVX\d]+)?|TEXTO\s+[IVX\d]+)\s*\**\s*[—–:-]?\s*""", RegexOption.IGNORE_CASE)

fun splitStatement(statement: String): StatementParts {
    var text = statement.replace("\r\n", "\n").trim()
    // Fonte entre parênteses (com link ou "adaptação de fonte real"): sai do texto e vai para o fim.
    var source: String? = null
    sourceParen.find(text)?.let { m ->
        source = m.groupValues[1].trim()
        text = text.removeRange(m.range).replace(Regex("[ \t]{2,}"), " ").trim()
    }
    val labelMatch = header.find(text)
    var label: String? = null
    var title: String? = null
    if (labelMatch != null) {
        label = labelMatch.groupValues[1].trim().uppercase()
        text = text.substring(labelMatch.range.last + 1).trimStart()
        // Título curto antes do primeiro ponto/linha ("Prioridades ambientais.").
        val cut = Regex("""^([^.\n]{3,80})(?:\.\s+|\n)""").find(text)
        if (cut != null && !cut.groupValues[1].contains(",")) { title = cut.groupValues[1].trim(); text = text.substring(cut.range.last + 1).trimStart() }
    }
    // Comando: última linha/parágrafo que é pergunta ou ordem ("1. Que relação...?").
    val paragraphs = text.split(Regex("\n\\s*\n|\n(?=\\s*\\d+[.)]\\s)")).map { it.trim() }.filter { it.isNotEmpty() }
    var command: String? = null
    var base = text
    if (paragraphs.size > 1) {
        val last = paragraphs.last().replace(Regex("""^\d+[.)]\s+"""), "")
        if (last.endsWith("?") || last.endsWith(":") || last.length < 260) {
            command = last
            base = paragraphs.dropLast(1).joinToString("\n\n")
        }
    }
    if (label == null && source == null && command == null) return StatementParts(null, null, statement, null, null)
    return StatementParts(label, title, base, command, source)
}

@Composable
fun QuestionStatement(statement: String) {
    val parts = splitStatement(statement)
    Div({ classes("statement") }) {
        if (parts.label != null) {
            Div({ classes("base-text") }) {
                Div({ classes("base-head") }) {
                    Span({ classes("base-label") }) { Text(parts.label) }
                    parts.title?.let { B { Text(it) } }
                }
                Markdown(parts.base)
            }
        } else Markdown(parts.base)
        parts.command?.let { P({ classes("command") }) { Inline(it) } }
    }
}

/** A fonte do texto-base, discreta, no fim da questão. */
@Composable
fun StatementSource(statement: String) {
    val source = splitStatement(statement).source ?: return
    Div({ classes("statement-source") }) {
        Icon("link")
        Span { Text("Fonte: "); Inline(source.replace(Regex("""adapta[çc][ãa]o did[áa]tica de fonte real:?\s*""", RegexOption.IGNORE_CASE), "")) }
    }
}
