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
 * "Fixar na tela" (só no site): um trecho, uma tabela ou um parágrafo da teoria vira uma janelinha que
 * fica por cima da página enquanto a pessoa lê, para consultar sem voltar lá em cima (uma tabela de
 * siglas, uma definição). Dá para arrastar pelo título, mudar o tamanho pelo canto, minimizar e fechar.
 * Até 3 ao mesmo tempo. Ficam guardadas neste navegador, por teoria, com a posição e o tamanho.
 */
object TheoryPins {
    data class Pin(
        val id: String,
        val title: String,
        val markdown: String,
        val collapsed: Boolean = false,
        val x: Int = -1,
        val y: Int = -1,
        val w: Int = 380,
        val h: Int = 260,
    )

    const val MAX = 3
    private var loadedFor: Long? = null
    val pins = mutableStateListOf<Pin>()

    /** A teoria aberta agora (as janelas só aparecem com ela na tela). */
    var openTheory by mutableStateOf<Long?>(null)

    private fun key(id: Long) = "estudario.pins.$id"

    fun load(id: Long) {
        if (loadedFor == id) return
        loadedFor = id
        pins.clear()
        runCatching {
            val raw = kotlinx.browser.window.localStorage.getItem(key(id)) ?: return@runCatching
            val arr = JSON.parse<Array<dynamic>>(raw)
            arr.forEach { o ->
                pins += Pin(
                    o.id as String, o.title as String, o.markdown as String, (o.collapsed as? Boolean) ?: false,
                    (o.x as? Number)?.toInt() ?: -1, (o.y as? Number)?.toInt() ?: -1, (o.w as? Number)?.toInt() ?: 380, (o.h as? Number)?.toInt() ?: 260,
                )
            }
        }
    }

    private fun save() {
        val id = loadedFor ?: return
        runCatching {
            val arr = pins.map { p -> kotlin.js.json("id" to p.id, "title" to p.title, "markdown" to p.markdown, "collapsed" to p.collapsed, "x" to p.x, "y" to p.y, "w" to p.w, "h" to p.h) }.toTypedArray()
            kotlinx.browser.window.localStorage.setItem(key(id), JSON.stringify(arr))
        }
    }

    /** Fixa um conteúdo. Devolve false quando já há 3 fixados. */
    fun add(title: String, markdown: String): Boolean {
        if (markdown.isBlank()) return false
        if (pins.any { it.markdown == markdown }) return true
        if (pins.size >= MAX) return false
        // Cada janela nova aparece um pouco deslocada da anterior, no canto direito.
        val w = kotlinx.browser.window.innerWidth
        val n = pins.size
        pins += Pin("p" + kotlin.js.Date.now().toLong().toString(36), title.take(60), markdown.take(8000), x = (w - 400 - n * 28).coerceAtLeast(12), y = 120 + n * 40)
        save()
        return true
    }

    fun remove(id: String) { pins.removeAll { it.id == id }; save() }
    fun toggle(id: String) = change(id) { it.copy(collapsed = !it.collapsed) }
    fun change(id: String, persist: Boolean = true, f: (Pin) -> Pin) {
        val i = pins.indexOfFirst { it.id == id }
        if (i >= 0) { pins[i] = f(pins[i]); if (persist) save() }
    }
    fun persist() = save()

    /** Um nome curto para o fixado: o título da seção, "Tabela: ..." ou as primeiras palavras. */
    fun titleFor(markdown: String): String {
        val lines = markdown.lines().map { it.trim() }.filter { it.isNotEmpty() }
        val heading = lines.firstOrNull { it.startsWith("#") }?.trimStart('#')?.let(::plainText)
        if (heading != null) return heading
        val table = lines.firstOrNull { it.startsWith("|") }
        if (table != null) return "Tabela: " + table.trim('|').split('|').map(::plainText).filter { it.isNotEmpty() }.take(3).joinToString(" · ")
        val words = plainText(lines.joinToString(" ")).replace(Regex("""[>#]"""), "").split(Regex("""\s+""")).take(7).joinToString(" ")
        return words + "…"
    }

    /** Título é texto: sem negrito/itálico pela metade ("Outorga** (por...") e com a fórmula como símbolos. */
    private fun plainText(markdown: String): String = markdown
        .replace(Regex("""\$\$(.+?)\$\$""")) { MathText.plain(it.groupValues[1]) }
        .replace(Regex("""[*_`]"""), "").trim()
}

/** A teoria aberta registra suas janelas fixadas (some ao sair da teoria). */
@Composable
fun TheoryPinDock(theoryId: Long) {
    TheoryPins.load(theoryId)
    androidx.compose.runtime.DisposableEffect(theoryId) {
        TheoryPins.openTheory = theoryId
        onDispose { if (TheoryPins.openTheory == theoryId) TheoryPins.openTheory = null }
    }
}

/**
 * As janelas fixadas, na raiz da página (por cima de tudo). Arrastar pelo título move; o canto de baixo
 * muda o tamanho; o tamanho e a posição ficam guardados.
 */
@Composable
fun TheoryPinWindows() {
    if (TheoryPins.openTheory == null || TheoryPins.pins.isEmpty()) return
    var front by androidx.compose.runtime.remember { mutableStateOf<String?>(null) }
    TheoryPins.pins.forEach { pin ->
        key(pin.id) {
            val vw = kotlinx.browser.window.innerWidth
            val vh = kotlinx.browser.window.innerHeight
            val x = (if (pin.x < 0) vw - pin.w - 24 else pin.x).coerceIn(0, (vw - 120).coerceAtLeast(0))
            val y = (if (pin.y < 0) 120 else pin.y).coerceIn(0, (vh - 48).coerceAtLeast(0))
            Div({
                classes(*listOfNotNull("pin-window", if (pin.collapsed) "collapsed" else null, if (front == pin.id) "front" else null).toTypedArray())
                attr("style", "left:${x}px;top:${y}px;width:${pin.w}px;" + if (pin.collapsed) "" else "height:${pin.h}px")
                attr("role", "dialog"); attr("aria-label", pin.title)
                addEventListener("pointerdown") { front = pin.id }
                // Mudou o tamanho pelo canto: guarda.
                addEventListener("pointerup") { e ->
                    val el = e.nativeEvent.currentTarget.asDynamic()
                    val w = (el.offsetWidth as Number).toInt(); val h = (el.offsetHeight as Number).toInt()
                    if (!pin.collapsed && (w != pin.w || h != pin.h)) TheoryPins.change(pin.id) { it.copy(w = w, h = h) }
                }
            }) {
                Div({
                    classes("pin-head")
                    attr("title", "Arraste para mover")
                    addEventListener("pointerdown") { ev ->
                        val e = ev.nativeEvent.asDynamic()
                        if ((e.target as? org.w3c.dom.Element)?.closest("button") != null) return@addEventListener
                        e.preventDefault()
                        val startX = (e.clientX as Number).toInt(); val startY = (e.clientY as Number).toInt()
                        val ox = x; val oy = y
                        lateinit var move: (org.w3c.dom.events.Event) -> Unit
                        lateinit var up: (org.w3c.dom.events.Event) -> Unit
                        move = { m ->
                            val me = m.asDynamic()
                            val nx = ox + ((me.clientX as Number).toInt() - startX)
                            val ny = oy + ((me.clientY as Number).toInt() - startY)
                            TheoryPins.change(pin.id, persist = false) { it.copy(x = nx, y = ny) }
                        }
                        up = { _ ->
                            kotlinx.browser.window.removeEventListener("pointermove", move)
                            kotlinx.browser.window.removeEventListener("pointerup", up)
                            TheoryPins.persist()
                        }
                        kotlinx.browser.window.addEventListener("pointermove", move)
                        kotlinx.browser.window.addEventListener("pointerup", up)
                    }
                }) {
                    Icon("drag_indicator")
                    B({ classes("clamp-1", "grow") }) { Text(pin.title) }
                    Button({ classes("pin-btn"); attr("title", if (pin.collapsed) "Abrir" else "Minimizar"); onClick { TheoryPins.toggle(pin.id) } }) { Icon(if (pin.collapsed) "open_in_full" else "minimize") }
                    Button({ classes("pin-btn"); attr("title", "Fechar"); onClick { TheoryPins.remove(pin.id) } }) { Icon("close") }
                }
                if (!pin.collapsed) Div({ classes("pin-body") }) { Markdown(pin.markdown, compact = true) }
                // Alças para mudar o tamanho: o canto de baixo, a borda da direita e a de baixo.
                if (!pin.collapsed) listOf("se", "e", "s").forEach { edge ->
                    Span({
                        classes("pin-resize", edge)
                        addEventListener("pointerdown") { ev ->
                            val e = ev.nativeEvent.asDynamic()
                            e.preventDefault(); e.stopPropagation()
                            val sx = (e.clientX as Number).toInt(); val sy = (e.clientY as Number).toInt()
                            val ow = pin.w; val oh = pin.h
                            lateinit var move: (org.w3c.dom.events.Event) -> Unit
                            lateinit var up: (org.w3c.dom.events.Event) -> Unit
                            move = { m ->
                                val me = m.asDynamic()
                                val dw = if (edge != "s") (me.clientX as Number).toInt() - sx else 0
                                val dh = if (edge != "e") (me.clientY as Number).toInt() - sy else 0
                                TheoryPins.change(pin.id, persist = false) { it.copy(w = (ow + dw).coerceIn(240, (vw * 0.92).toInt()), h = (oh + dh).coerceIn(120, (vh * 0.86).toInt())) }
                            }
                            up = { _ ->
                                kotlinx.browser.window.removeEventListener("pointermove", move)
                                kotlinx.browser.window.removeEventListener("pointerup", up)
                                TheoryPins.persist()
                            }
                            kotlinx.browser.window.addEventListener("pointermove", move)
                            kotlinx.browser.window.addEventListener("pointerup", up)
                        }
                    })
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
