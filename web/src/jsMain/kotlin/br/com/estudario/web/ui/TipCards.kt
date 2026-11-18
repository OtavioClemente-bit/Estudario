package br.com.estudario.web.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import br.com.estudario.web.data.Snippet
import org.jetbrains.compose.web.attributes.ButtonType
import org.jetbrains.compose.web.attributes.type
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text

// ------------------------------------------------------------------ leitura do texto (o mesmo do app, TipDeck.kt)

/**
 * Uma dica ou pegadinha reorganizada para ler fácil, sem mudar o texto: título curto (do negrito do
 * começo, do rótulo antes dos dois pontos ou da primeira frase curta), parágrafos de uma frase e, quando
 * o texto já traz "Enunciado: '...'. Erro: ...", o par afirmação da banca e correção.
 */
data class TipShape(val title: String?, val paras: List<String>, val claim: String? = null, val fix: String? = null)

private val claimRe = Regex("""^\s*(?:\*\*)?(?:Enunciado|Afirma[çc][ãa]o|Assertiva|Item|Frase)(?:\*\*)?\s*:\s*(?:\*\*)?\s*['"“‘]([\s\S]+?)['"”’]\s*[.;]?\s*(?:\*\*)?\s*(?:Erro|Errado|Falso)(?:\*\*)?\s*:\s*(?:\*\*)?\s*([\s\S]+)$""", RegexOption.IGNORE_CASE)
private val errRe = Regex("""^([\s\S]{12,}?[.!?])\s+(?:\*\*)?(?:Erro|Errado)(?:\*\*)?\s*:\s*(?:\*\*)?\s*([\s\S]{8,})$""")
private val boldTitleRe = Regex("""^\s*\*\*([^*]{2,70})\*\*\s*[:.]?\s*""")
private val colonTitleRe = Regex("""^([^:.;!?*\n]{6,56}):\s+(\S[\s\S]*)$""")
private val sentenceEndRe = Regex("""[.!?…]["”’)]?\s+(?=[A-ZÁÉÍÓÚÂÊÔÃÕÀÇ“"'(])""")
private val abbreviations = setOf("art", "arts", "inc", "n", "nº", "p", "pág", "pg", "al", "dr", "dra", "sr", "sra", "ex", "obs", "cf", "etc", "fls", "min", "prof", "profa", "s.a", "ltda", "lc", "ec", "cp", "cpp", "cc", "cf/88")

private fun upperFirst(s: String) = s.replaceFirstChar { if (it.isLowerCase()) it.uppercaseChar() else it }

/** Frases de um parágrafo; lista e tabela ficam inteiras. "art." e outras abreviações não cortam. */
fun tipSentences(text: String): List<String> {
    val t = text.trim()
    if (t.isEmpty()) return emptyList()
    if (t.lines().any { val l = it.trimStart(); l.startsWith("- ") || l.startsWith("* ") || l.startsWith("|") || l.startsWith("#") || Regex("""^\d+[.)]\s""").containsMatchIn(l) }) return listOf(t)
    val out = mutableListOf<String>()
    var start = 0
    for (m in sentenceEndRe.findAll(t)) {
        val piece = t.substring(start, m.range.first + 1)
        val last = piece.trimEnd().takeLastWhile { !it.isWhitespace() }.trimEnd('.', '!', '?', '…').lowercase()
        if (last in abbreviations) continue
        out += t.substring(start, m.range.last + 1).trim()
        start = m.range.last + 1
    }
    out += t.substring(start).trim()
    return out.filter { it.isNotBlank() }
}

fun tipShape(text: String, answer: String? = null): TipShape {
    val raw = text.trim()
    val extra = answer?.trim()?.takeIf { it.isNotBlank() }?.let { listOf(it) }.orEmpty()
    claimRe.find(raw)?.let { m -> return TipShape(null, extra, m.groupValues[1].trim(), upperFirst(m.groupValues[2].trim())) }
    var title: String? = null
    var rest = raw
    boldTitleRe.find(raw)?.let { m ->
        val after = raw.substring(m.range.last + 1).trim()
        if (after.isNotBlank()) { title = m.groupValues[1].trim().trimEnd(':', '.').trim(); rest = after }
    }
    if (title == null) colonTitleRe.find(raw)?.let { m -> title = m.groupValues[1].trim(); rest = upperFirst(m.groupValues[2].trim()) }
    errRe.find(rest)?.let { m -> return TipShape(title, extra, m.groupValues[1].trim(), upperFirst(m.groupValues[2].trim())) }
    var paras = tipSentences(rest)
    if (title == null && paras.size >= 2 && paras[0].length <= 90) { title = paras[0].trimEnd('.'); paras = paras.drop(1) }
    return TipShape(title, paras + extra)
}

/** Um cartão de dica, pegadinha, atenção ou resumo dentro da teoria: o rótulo vira cabeçalho. */
data class CalloutInfo(val kind: String, val label: String, val body: String)

fun calloutKindOfLabel(label: String): String? {
    val l = label.trim().lowercase()
    return when {
        listOf("pegadinha", "cuidado", "não confunda", "nao confunda", "erro comum", "armadilha").any { l.startsWith(it) } -> "trap"
        l.startsWith("atenção") || l.startsWith("atencao") -> "warn"
        l.startsWith("resumo") || l.startsWith("resumindo") || l.startsWith("na prova") -> "sum"
        listOf("dica", "bizu", "macete", "lembre", "memorize").any { l.startsWith(it) } -> "tip"
        else -> null
    }
}

private val calloutLabelRe = Regex("""^\*\*([^*]{2,40}?)\s*:?\s*\*\*\s*:?\s*""")

fun calloutInfo(block: String): CalloutInfo? {
    val start = block.trimStart()
    val quoted = start.startsWith(">")
    val text = if (quoted) start.lines().joinToString("\n") { it.trimStart().removePrefix(">").removePrefix(" ") }.trim() else start
    if (text.lines().any { it.trimStart().startsWith("|") } && !quoted) return null
    val m = calloutLabelRe.find(text)
    if (m != null) {
        val labelRaw = text.substring(0, m.range.last + 1)
        val hasColon = labelRaw.contains(":")
        val kind = calloutKindOfLabel(m.groupValues[1])
        if (kind != null && (hasColon || quoted)) {
            val body = text.substring(m.range.last + 1).trim()
            if (body.isNotBlank()) return CalloutInfo(kind, m.groupValues[1].trim().trimEnd(':').trim(), body)
        }
    }
    if (!quoted) return null
    val head = text.trimStart('*', '_', ' ', '#').take(40)
    val kind = calloutKindOfLabel(head) ?: return null
    return CalloutInfo(kind, kindName(kind), text)
}

/**
 * Como cada bloco da teoria aparece perto das caixas: "### Pegadinha" logo antes de "**Pegadinha:** ..."
 * some (o cartão já diz); "### Resumo para a prova" seguido de lista vira o topo de um cartão de resumo
 * e a lista, o corpo. Resultado por bloco: null (normal), "hide", "top:<tipo>" ou "bottom:<tipo>".
 */
fun calloutRoles(blocks: List<String>, infos: List<CalloutInfo?>): List<String?> {
    val roles = MutableList<String?>(blocks.size) { null }
    val heading = Regex("""^#{3,4}\s+(.+)$""")
    blocks.forEachIndexed { i, b ->
        val title = heading.find(b.trim())?.groupValues?.get(1)?.trim()?.trim('*')?.trim() ?: return@forEachIndexed
        if (b.trim().lines().size > 1) return@forEachIndexed
        val kind = calloutKindOfLabel(title) ?: return@forEachIndexed
        val next = blocks.getOrNull(i + 1) ?: return@forEachIndexed
        when {
            infos.getOrNull(i + 1)?.kind == kind -> roles[i] = "hide"
            infos.getOrNull(i + 1) == null && !next.trimStart().startsWith("#") && !next.trimStart().startsWith("|") -> { roles[i] = "top:$kind"; roles[i + 1] = "bottom:$kind" }
        }
    }
    return roles
}

/** Topo de um cartão feito de um título da teoria ("### Resumo para a prova"). */
@Composable
fun TheoryCalloutTop(kind: String, block: String) {
    Div({ classes("tcall-head") }) {
        TipArt(kind, if (kind == "warn" || kind == "sum") 54 else 40)
        Span({ classes("tcall-label") }) { Text(block.trim().trimStart('#').trim().trim('*').trim()) }
    }
}

fun kindName(kind: String) = when (kind) { "trap" -> "Pegadinha"; "warn" -> "Atenção"; "sum" -> "Resumo"; else -> "Dica" }

// ------------------------------------------------------------------ desenhos

/** O desenho de cada tipo: ratoeira da turma (pegadinha), lâmpada com carinha (dica) e o Folha apontando. */
@Composable
fun TipArt(kind: String, size: Int) {
    Span({ classes("tart", "k-$kind"); attr("style", "--tart:${size}px"); attr("aria-hidden", "true") }) {
        when (kind) {
            "trap" -> Icon("pest_control_rodent")
            "tip" -> Icon("lightbulb")
            else -> Folha("point", size, live = false)
        }
    }
}

// ------------------------------------------------------------------ cartões

@Composable
private fun TipParas(shape: TipShape, big: Boolean) {
    shape.title?.let { Div({ classes("tnote-title") }) { Inline(it) } }
    if (shape.claim != null) {
        Div({ classes("tnote-vs") }) {
            Div({ classes("tnote-row", "bad") }) { Span({ classes("tnote-mark") }) { Text("✘ Pegadinha") }; Div({ classes("tnote-p") }) { Inline("“${shape.claim}”") } }
            shape.fix?.let { fix -> Div({ classes("tnote-row", "good") }) { Span({ classes("tnote-mark") }) { Text("✔ Certo") }; Div({ classes("tnote-p") }) { Inline(fix) } } }
        }
    }
    shape.paras.forEachIndexed { i, p ->
        Div({ classes(*listOfNotNull("tnote-p", if (i == 0 && shape.title == null && shape.claim == null) "lead" else null).toTypedArray()) }) { Inline(p) }
    }
}

/** Um cartão grande, uma ideia só: desenho, rótulo, título em destaque e frases curtas. */
@Composable
fun TipNote(tip: Snippet, counter: String?, action: @Composable () -> Unit = {}) {
    val trap = tip.kind == "PEGADINHA"
    val shape = remember(tip.text, tip.answer) { tipShape(tip.text, tip.answer) }
    Div({ classes("tnote", if (trap) "k-trap" else "k-tip") }) {
        Div({ classes("tnote-top") }) {
            TipArt(if (trap) "trap" else "tip", 46)
            Span({ classes("tnote-kind") }) { Text(if (trap) "Pegadinha" else "Dica") }
            counter?.let { Span({ classes("tnote-count") }) { Text(it) } }
            action()
        }
        Div({ classes("tnote-body") }) { TipParas(shape, true) }
    }
}

/**
 * Pegadinha que traz a frase da banca: vira desafio. Frente "Verdadeiro ou pegadinha?" com a frase;
 * a pessoa escolhe e o cartão vira mostrando o erro. A frase da pegadinha é sempre falsa.
 */
@Composable
private fun TrapChallenge(tip: Snippet, shape: TipShape, counter: String?, answered: Int, onAnswer: (Int) -> Unit, action: @Composable () -> Unit) {
    val flipped = answered != 0
    Div({ classes(*listOfNotNull("tflip", if (flipped) "flipped" else null).toTypedArray()) }) {
        Div({ classes("tflip-inner") }) {
            Div({ classes("tnote", "k-trap", "tflip-face", "front") }) {
                Div({ classes("tnote-top") }) {
                    TipArt("trap", 46)
                    Span({ classes("tnote-kind") }) { Text("Verdadeiro ou pegadinha?") }
                    counter?.let { Span({ classes("tnote-count") }) { Text(it) } }
                }
                Div({ classes("tnote-body") }) {
                    Div({ classes("tnote-claim") }) { Inline("“${shape.claim}”") }
                    Div({ classes("tnote-ask") }) {
                        Button({ type(ButtonType.Button); classes("tnote-btn", "ok"); onClick { onAnswer(1) } }) { Text("✔ Verdadeiro") }
                        Button({ type(ButtonType.Button); classes("tnote-btn", "no"); onClick { onAnswer(2) } }) { Text("✘ É pegadinha") }
                    }
                }
            }
            Div({ classes("tnote", "k-trap", "tflip-face", "back") }) {
                Div({ classes("tnote-top") }) {
                    TipArt("trap", 46)
                    Span({ classes("tnote-kind") }) { Text(if (answered == 2) "Isso! Era pegadinha" else "Caiu! Era pegadinha") }
                    counter?.let { Span({ classes("tnote-count") }) { Text(it) } }
                    action()
                }
                Div({ classes("tnote-body") }) {
                    TipParas(shape, true)
                    Button({ type(ButtonType.Button); classes("tnote-again"); onClick { onAnswer(0) } }) { Icon("replay"); Text("Ver a frase de novo") }
                }
            }
        }
    }
}

@Composable
private fun TipCardAny(tip: Snippet, counter: String?, answers: MutableMap<Long, Int>, action: @Composable () -> Unit) {
    val shape = remember(tip.text, tip.answer) { tipShape(tip.text, tip.answer) }
    if (tip.kind == "PEGADINHA" && shape.claim != null) TrapChallenge(tip, shape, counter, answers[tip.id] ?: 0, { answers[tip.id] = it }, action)
    else TipNote(tip, counter, action)
}

/**
 * Um cartão por vez, grande, com "3 de 8", setas, teclado (setas) e deslizar no celular. Embaixo,
 * bolinhas de cada cartão (coral para pegadinha, verde para dica).
 */
@Composable
fun TipDeck(tips: List<Snippet>, action: @Composable (Snippet) -> Unit = {}) {
    if (tips.isEmpty()) return
    var index by remember(tips.map { it.id }) { mutableStateOf(0) }
    val answers = remember { mutableStateMapOf<Long, Int>() }
    val i = index.coerceIn(0, tips.lastIndex)
    val tip = tips[i]
    fun go(d: Int) { index = (i + d + tips.size) % tips.size }
    val goRef = androidx.compose.runtime.rememberUpdatedState<(Int) -> Unit> { d -> go(d) }
    var touchX by remember { mutableStateOf<Double?>(null) }
    Div({
        classes("tdeck")
        attr("aria-label", "Dicas e pegadinhas, uma por vez. Use as setas do teclado para passar.")
        ref { el ->
            // Setas do teclado pela janela (sem foco no baralho: o foco rolava a página e perdia o clique).
            val keys: (org.w3c.dom.events.Event) -> Unit = { e ->
                val k = e.asDynamic().key as? String
                val tag = (kotlinx.browser.document.activeElement?.tagName ?: "").uppercase()
                if (tag != "INPUT" && tag != "TEXTAREA" && kotlinx.browser.document.activeElement?.getAttribute("contenteditable") == null) {
                    if (k == "ArrowRight") { e.preventDefault(); goRef.value(1) } else if (k == "ArrowLeft") { e.preventDefault(); goRef.value(-1) }
                }
            }
            kotlinx.browser.window.addEventListener("keydown", keys)
            val start: (org.w3c.dom.events.Event) -> Unit = { e -> touchX = (e.asDynamic().touches[0]?.clientX as? Double) }
            val end: (org.w3c.dom.events.Event) -> Unit = { e ->
                val x0 = touchX; val x1 = e.asDynamic().changedTouches[0]?.clientX as? Double
                if (x0 != null && x1 != null && kotlin.math.abs(x1 - x0) > 60) goRef.value(if (x1 < x0) 1 else -1)
                touchX = null
            }
            el.addEventListener("touchstart", start)
            el.addEventListener("touchend", end)
            onDispose { el.removeEventListener("touchstart", start); el.removeEventListener("touchend", end); kotlinx.browser.window.removeEventListener("keydown", keys) }
        }
    }) {
        key(tip.id) { Div({ classes("tdeck-card") }) { TipCardAny(tip, "${i + 1} de ${tips.size}", answers) { action(tip) } } }
        if (tips.size > 1) Div({ classes("tdeck-nav") }) {
            Button({ type(ButtonType.Button); classes("tdeck-arrow"); attr("aria-label", "Anterior"); onClick { go(-1) } }) { Icon("chevron_left") }
            Div({ classes("tdeck-dots") }) {
                tips.forEachIndexed { j, t ->
                    Button({
                        type(ButtonType.Button)
                        classes(*listOfNotNull("tdeck-dot", if (t.kind == "PEGADINHA") "trap" else "tip", if (j == i) "on" else null).toTypedArray())
                        attr("aria-label", "Cartão ${j + 1}")
                        onClick { index = j }
                    })
                }
            }
            Button({ type(ButtonType.Button); classes("tdeck-arrow"); attr("aria-label", "Próxima"); onClick { go(1) } }) { Icon("chevron_right") }
        }
    }
}

/** Todas de uma vez: uma coluna só, cartões bem separados. */
@Composable
fun TipList(tips: List<Snippet>, action: @Composable (Snippet) -> Unit = {}) {
    val answers = remember { mutableStateMapOf<Long, Int>() }
    Div({ classes("tlist") }) {
        tips.forEachIndexed { i, tip -> key(tip.id) { TipCardAny(tip, "${i + 1} de ${tips.size}", answers) { action(tip) } } }
    }
}

/** Dica, pegadinha, atenção ou resumo no meio da teoria: cabeçalho com o desenho e frases curtas. */
@Composable
fun TheoryCallout(info: CalloutInfo) {
    Div({ classes("tcall-head") }) {
        TipArt(info.kind, if (info.kind == "warn" || info.kind == "sum") 54 else 40)
        Span({ classes("tcall-label") }) { Text(info.label) }
    }
    Div({ classes("tcall-body") }) {
        val errSplit = errRe.find(info.body)
        if (errSplit != null) {
            Div({ classes("tnote-vs") }) {
                Div({ classes("tnote-row", "bad") }) { Span({ classes("tnote-mark") }) { Text("✘ Pegadinha") }; Div({ classes("tcall-p") }) { Markdown(errSplit.groupValues[1].trim()) } }
                Div({ classes("tnote-row", "good") }) { Span({ classes("tnote-mark") }) { Text("✔ Certo") }; Div({ classes("tcall-p") }) { Markdown(upperFirst(errSplit.groupValues[2].trim())) } }
            }
        } else tipSentences(info.body).forEach { p -> Div({ classes("tcall-p") }) { Markdown(upperFirst(p)) } }
    }
}
