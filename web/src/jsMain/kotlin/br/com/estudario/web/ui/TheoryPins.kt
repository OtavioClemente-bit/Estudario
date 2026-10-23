package br.com.estudario.web.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import org.jetbrains.compose.web.dom.B
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text

/**
 * "Fixar na tela" (só no site): um trecho, uma tabela ou um parágrafo da teoria fica preso no alto da
 * leitura enquanto a pessoa desce o texto, para consultar sem voltar lá em cima (uma tabela de siglas,
 * uma definição). Até 3 ao mesmo tempo; cada um minimiza ou fecha. Fica guardado neste navegador, por teoria.
 */
object TheoryPins {
    data class Pin(val id: String, val title: String, val markdown: String, val collapsed: Boolean = false)

    const val MAX = 3
    private var loadedFor: Long? = null
    val pins = mutableStateListOf<Pin>()
    var theoryId by mutableStateOf<Long?>(null)
        private set

    private fun key(id: Long) = "estudario.pins.$id"

    fun load(id: Long) {
        if (loadedFor == id) return
        loadedFor = id; theoryId = id
        pins.clear()
        runCatching {
            val raw = kotlinx.browser.window.localStorage.getItem(key(id)) ?: return@runCatching
            val arr = JSON.parse<Array<dynamic>>(raw)
            arr.forEach { o -> pins += Pin(o.id as String, o.title as String, o.markdown as String, (o.collapsed as? Boolean) ?: false) }
        }
    }

    private fun save() {
        val id = theoryId ?: return
        runCatching {
            val arr = pins.map { p -> kotlin.js.json("id" to p.id, "title" to p.title, "markdown" to p.markdown, "collapsed" to p.collapsed) }.toTypedArray()
            kotlinx.browser.window.localStorage.setItem(key(id), JSON.stringify(arr))
        }
    }

    /** Fixa um conteúdo. Devolve false quando já há 3 fixados. */
    fun add(title: String, markdown: String): Boolean {
        if (markdown.isBlank()) return false
        if (pins.any { it.markdown == markdown }) return true
        if (pins.size >= MAX) return false
        pins += Pin("p" + kotlin.js.Date.now().toLong().toString(36), title.take(60), markdown.take(8000))
        save()
        return true
    }

    fun remove(id: String) { pins.removeAll { it.id == id }; save() }
    fun toggle(id: String) {
        val i = pins.indexOfFirst { it.id == id }
        if (i >= 0) { pins[i] = pins[i].copy(collapsed = !pins[i].collapsed); save() }
    }

    /** Um nome curto para o fixado: o título da seção, "Tabela: ..." ou as primeiras palavras. */
    fun titleFor(markdown: String): String {
        val lines = markdown.lines().map { it.trim() }.filter { it.isNotEmpty() }
        val heading = lines.firstOrNull { it.startsWith("#") }?.trimStart('#')?.trim()
        if (heading != null) return heading
        val table = lines.firstOrNull { it.startsWith("|") }
        if (table != null) return "Tabela: " + table.trim('|').split('|').map { it.trim().trim('*') }.filter { it.isNotEmpty() }.take(3).joinToString(" · ")
        val words = lines.joinToString(" ").replace(Regex("""[*_`>#]"""), "").split(Regex("""\s+""")).take(7).joinToString(" ")
        return words + "…"
    }
}

/** Os fixados no alto da leitura: lado a lado, presos enquanto a página desce. */
@Composable
fun TheoryPinDock(theoryId: Long) {
    TheoryPins.load(theoryId)
    if (TheoryPins.pins.isEmpty()) return
    Div({ classes("pin-dock"); attr("style", "--pins:${TheoryPins.pins.size}") }) {
        TheoryPins.pins.forEach { pin ->
            key(pin.id) {
                Div({ classes(*listOfNotNull("pin-card", if (pin.collapsed) "collapsed" else null).toTypedArray()) }) {
                    Div({ classes("pin-head") }) {
                        Icon("push_pin")
                        B({ classes("clamp-1", "grow") }) { Text(pin.title) }
                        Button({ classes("pin-btn"); attr("title", if (pin.collapsed) "Abrir" else "Minimizar"); onClick { TheoryPins.toggle(pin.id) } }) { Icon(if (pin.collapsed) "expand_more" else "expand_less") }
                        Button({ classes("pin-btn"); attr("title", "Soltar"); onClick { TheoryPins.remove(pin.id) } }) { Icon("close") }
                    }
                    if (!pin.collapsed) Div({ classes("pin-body") }) { Markdown(pin.markdown, compact = true) }
                }
            }
        }
    }
}

/** O que o botão direito achou: a seleção (se houver), o parágrafo debaixo do mouse e o grifo (se houver). */
data class TheoryContext(
    val x: Double,
    val y: Double,
    val selection: String?,
    val selectionBlock: Int?,
    val block: Int?,
    val markId: Long?,
)

/**
 * O menu do botão direito na teoria: grifar nas três cores, grifar com nota, fixar na tela, copiar,
 * pesquisar e o tamanho da letra. Com o mouse num grifo, também mudar a cor, ver a nota e tirar.
 */
@Composable
fun TheoryContextMenu(
    ctx: TheoryContext,
    blockText: String?,
    markHasNote: Boolean,
    onColor: (Int) -> Unit,
    onNote: () -> Unit,
    onPinSelection: () -> Unit,
    onPinBlock: () -> Unit,
    onMarkColor: (Int) -> Unit,
    onMarkNote: () -> Unit,
    onMarkRemove: () -> Unit,
    onClose: () -> Unit,
) {
    val w = kotlinx.browser.window.innerWidth.toDouble()
    val h = kotlinx.browser.window.innerHeight.toDouble()
    val left = ctx.x.coerceAtMost(w - 260.0).coerceAtLeast(8.0)
    val top = ctx.y.coerceAtMost(h - 380.0).coerceAtLeast(8.0)
    Div({ classes("ctx-scrim"); onClick { onClose() }; addEventListener("contextmenu") { it.preventDefault(); onClose() } })
    Div({ classes("ctx-menu"); attr("style", "left:${left}px;top:${top}px"); attr("role", "menu"); addEventListener("mousedown") { it.preventDefault() } }) {
        val sel = ctx.selection?.takeIf { it.isNotBlank() }
        if (sel != null) {
            Div({ classes("ctx-label") }) { Text("GRIFAR O TRECHO") }
            Div({ classes("ctx-colors") }) {
                MarkColors.all.forEach { tone ->
                    Button({ classes("ctx-color"); onClick { onColor(tone.id); onClose() } }) { Span({ classes("tone-dot"); attr("style", "background:${tone.hex}") }); Text(tone.label) }
                }
            }
            CtxItem("edit_note", "Grifar com nota") { onNote(); onClose() }
            CtxItem("push_pin", "Fixar o trecho na tela") { onPinSelection(); onClose() }
            CtxItem("content_copy", "Copiar") { runCatching { kotlinx.browser.window.navigator.asDynamic().clipboard.writeText(sel) }; Toast.show("Trecho copiado"); onClose() }
            CtxItem("search", "Pesquisar no Google") { kotlinx.browser.window.open("https://www.google.com/search?q=" + js("encodeURIComponent")(sel.take(200)), "_blank"); onClose() }
        }
        if (ctx.markId != null) {
            if (sel != null) Div({ classes("ctx-sep") })
            Div({ classes("ctx-label") }) { Text("ESTE GRIFO") }
            Div({ classes("ctx-colors") }) {
                MarkColors.all.forEach { tone ->
                    Button({ classes("ctx-color"); onClick { onMarkColor(tone.id); onClose() } }) { Span({ classes("tone-dot"); attr("style", "background:${tone.hex}") }); Text(tone.label) }
                }
            }
            CtxItem("edit_note", if (markHasNote) "Ver a nota" else "Anotar") { onMarkNote(); onClose() }
            CtxItem("ink_eraser", "Tirar o grifo") { onMarkRemove(); onClose() }
        }
        if (ctx.block != null && blockText != null) {
            Div({ classes("ctx-sep") })
            val isTable = blockText.lines().count { it.trimStart().startsWith("|") } >= 2
            CtxItem("push_pin", if (isTable) "Fixar a tabela na tela" else "Fixar o parágrafo na tela") { onPinBlock(); onClose() }
            CtxItem("content_copy", if (isTable) "Copiar a tabela" else "Copiar o parágrafo") {
                runCatching { kotlinx.browser.window.navigator.asDynamic().clipboard.writeText(blockText) }
                Toast.show("Copiado"); onClose()
            }
        }
        Div({ classes("ctx-sep") })
        Div({ classes("ctx-row") }) {
            Span({ classes("xs", "muted") }) { Text("Letra") }
            Button({ classes("ctx-mini"); onClick { ReaderPrefs.setSize(ReaderPrefs.size - 1) } }) { Text("A−") }
            Button({ classes("ctx-mini", "big"); onClick { ReaderPrefs.setSize(ReaderPrefs.size + 1) } }) { Text("A+") }
        }
    }
}

@Composable
private fun CtxItem(icon: String, label: String, onClick: () -> Unit) {
    Button({ classes("ctx-item"); attr("role", "menuitem"); onClick { onClick() } }) { Icon(icon); Text(label) }
}
