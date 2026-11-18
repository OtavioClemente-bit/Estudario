package br.com.estudario.web.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import br.com.estudario.domain.StudyNotebook
import br.com.estudario.web.Route
import br.com.estudario.web.Router
import br.com.estudario.web.data.Actions
import br.com.estudario.web.data.Store
import kotlinx.browser.document
import kotlinx.browser.window
import org.jetbrains.compose.web.dom.B
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H3
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.dom.TextArea

/*
 * Ferramentas do grifo: a barra que aparece ao selecionar um trecho da teoria (cores com nome, anotar,
 * flashcard, revisão, copiar, fixar), a janelinha para montar o flashcard e, no Caderno, a seção
 * "Seus grifos" (cada grifo com a etiqueta da cor, filtro por cor e o trecho no marca-texto da cor).
 */

/** A barra flutuante da seleção. [current] é a cor do grifo que a seleção já cobre (ganha o check). */
@Composable
fun MarkSelectionBar(
    x: Double,
    y: Double,
    current: Int?,
    onColor: (Int) -> Unit,
    onNote: () -> Unit,
    onCard: () -> Unit,
    onReview: () -> Unit,
    onCopy: () -> Unit,
    onPin: () -> Unit,
    onClose: () -> Unit,
) {
    var hover by remember { mutableStateOf<Int?>(null) }
    // Esc fecha a barra (e desfaz a seleção); Tab percorre os botões normalmente.
    androidx.compose.runtime.DisposableEffect(Unit) {
        val key: (org.w3c.dom.events.Event) -> Unit = { e -> if ((e as org.w3c.dom.events.KeyboardEvent).key == "Escape") onClose() }
        document.addEventListener("keydown", key)
        onDispose { document.removeEventListener("keydown", key) }
    }
    val w = window.innerWidth.toDouble()
    val left = x.coerceIn(220.0, (w - 220.0).coerceAtLeast(220.0))
    val shown = MarkColors.all.firstOrNull { it.id == (hover ?: current) }
    Div({
        classes(*listOfNotNull("mk-bar", if (y < 110) "below" else null).toTypedArray())
        attr("role", "toolbar"); attr("aria-label", "Grifar o trecho selecionado")
        attr("style", "left:${left}px;top:${if (y < 110) y + 30 else y - 10}px")
        // Clicar na barra não pode desfazer a seleção antes de salvar.
        addEventListener("mousedown") { it.preventDefault() }
        addEventListener("click") { it.stopPropagation() }
    }) {
        Div({ classes("mk-row") }) {
            Div({ classes("mk-dots") }) {
                MarkColors.all.forEach { tone ->
                    val on = tone.id == current
                    Button({
                        classes(*listOfNotNull("mk-dot", if (on) "on" else null).toTypedArray())
                        attr("style", "--mk:${tone.hex}")
                        attr("aria-label", "Grifar: ${tone.label}. ${tone.hint}")
                        attr("aria-pressed", on.toString())
                        addEventListener("mouseenter") { hover = tone.id }
                        addEventListener("mouseover") { hover = tone.id }
                        addEventListener("mouseleave") { hover = null }
                        addEventListener("focus") { hover = tone.id }
                        addEventListener("blur") { hover = null }
                        onClick { onColor(tone.id) }
                    }) {
                        Span({ classes("mk-dot-c") }) { if (on) Icon("check", plain = true) }
                        Span({ classes("mk-dot-l") }) { Text(tone.label) }
                    }
                }
            }
            Span({ classes("mk-sep") })
            MkAction("edit_note", "Anotar", onNote)
            MkAction("style", "Flashcard", onCard)
            MkAction("event_repeat", "Revisão", onReview)
            MkAction("content_copy", "Copiar", onCopy)
            MkAction("push_pin", "Fixar", onPin)
        }
        // A legenda da cor: aparece ao passar o mouse (ou com o foco do teclado) e na cor já escolhida.
        Div({ classes(*listOfNotNull("mk-cap", if (shown != null) "on" else null).toTypedArray()); attr("aria-live", "polite") }) {
            if (shown != null) {
                Span({ classes("mk-cap-dot"); attr("style", "background:${shown.hex}") })
                B { Text(shown.label) }
                Span { Text(shown.hint) }
            }
        }
    }
}

@Composable
private fun MkAction(icon: String, label: String, onClick: () -> Unit) {
    Button({ classes("mk-act"); attr("title", label); attr("aria-label", label); onClick { onClick() } }) {
        Icon(icon); Span { Text(label) }
    }
}

/** Montar um flashcard com o trecho: frente e verso editáveis, já com duas sugestões de montagem. */
@Composable
fun QuoteFlashcardModal(quote: String, place: String, onDismiss: () -> Unit, onSave: (front: String, back: String) -> Unit) {
    val plain = remember(quote) { quote.replace(Regex("""[*_`#>|]+"""), "").replace(Regex("""\s+"""), " ").trim() }
    var front by remember { mutableStateOf(plain) }
    var back by remember { mutableStateOf("") }
    var flipped by remember { mutableStateOf(false) }
    Modal(onDismiss = onDismiss, extraClass = "mk-card-modal") {
        Div({ classes("row", "between") }) {
            Div {
                H3({ attr("style", "margin:0") }) { Text("Novo flashcard") }
                Span({ classes("xs", "muted") }) { Text(place) }
            }
            IconButton("close", "Fechar") { onDismiss() }
        }
        Div({ classes("mk-presets"); attr("role", "radiogroup") }) {
            Button({ classes(*listOfNotNull("mk-preset", if (!flipped) "on" else null).toTypedArray()); attr("role", "radio"); attr("aria-checked", (!flipped).toString()); onClick { flipped = false; front = plain; back = "" } }) {
                Icon("format_quote"); Text("Trecho na frente")
            }
            Button({ classes(*listOfNotNull("mk-preset", if (flipped) "on" else null).toTypedArray()); attr("role", "radio"); attr("aria-checked", flipped.toString()); onClick { flipped = true; front = ""; back = plain } }) {
                Icon("help"); Text("Pergunta na frente, trecho no verso")
            }
        }
        Div({ classes("mk-face") }) {
            Span({ classes("mk-face-l") }) { Text("FRENTE") }
            TextArea(front, { classes("input"); attr("rows", "3"); attr("maxlength", "2000"); attr("aria-label", "Frente do cartão"); attr("placeholder", "Uma pergunta: o que diz o art. sobre…?"); onInput { front = it.value } })
        }
        Div({ classes("mk-face", "back") }) {
            Span({ classes("mk-face-l") }) { Text("VERSO") }
            TextArea(back, { classes("input"); attr("rows", "3"); attr("maxlength", "4000"); attr("aria-label", "Verso do cartão"); attr("placeholder", "A resposta ou um complemento (opcional)"); onInput { back = it.value } })
        }
        P({ classes("xs", "muted") }) { Text("O cartão fica nos Flashcards deste tópico e em Salvos, no Caderno de estudo.") }
        Div({ classes("row"); attr("style", "gap:8px;justify-content:flex-end") }) {
            Btn("Cancelar", onDismiss, style = "ghost")
            Btn("Salvar flashcard", { onSave(front, back) }, style = "primary", icon = "check", enabled = front.isNotBlank())
        }
    }
}

/** A cor escolhida no filtro de cada página do Caderno (null = todas). */
private object MarksNav { val color = mutableStateMapOf<Long, Int>() }

/**
 * "Seus grifos" na página do tópico no Caderno: um lugar só para os grifos (antes apareciam aqui e de
 * novo em "Salvos deste assunto"). Na ordem do texto, com a etiqueta da cor e o trecho no marca-texto
 * daquela cor; dá para filtrar por cor e trocar a cor ali mesmo.
 */
@Composable
fun TopicMarksSection(topicId: Long) {
    val data = Store.data
    val theoryOrder = remember(data.theories) { data.theories.withIndex().associate { it.value.id to it.index } }
    val marks = remember(data) {
        savedByTopic(data)[topicId]?.marks.orEmpty().sortedWith(compareBy({ theoryOrder[it.theoryId] ?: 0 }, { it.blockIndex }, { it.createdAt }))
    }
    val filter = MarksNav.color[topicId]?.takeIf { c -> marks.any { it.color == c } }
    val shown = if (filter == null) marks else marks.filter { it.color == filter }
    Div({ classes("mk-sec", "rise"); attr("id", "seus-grifos") }) {
        Div({ classes("mk-sec-head") }) {
            Span({ classes("mk-sec-ic") }) { Icon("format_ink_highlighter", filled = true) }
            Div({ classes("grow") }) {
                H3 { Text("Seus grifos") }
                Span({ classes("nbs-sub") }) { Text(if (marks.isEmpty()) "Nenhum trecho grifado ainda" else "${marks.size} ${if (marks.size == 1) "trecho" else "trechos"}, na ordem do texto") }
            }
        }
        if (marks.isEmpty()) {
            Div({ classes("mk-empty") }) {
                Div({ classes("mk-empty-dots") }) { MarkColors.all.forEach { Span({ attr("style", "background:${it.hex}") }) } }
                P { Text("Na teoria, selecione um trecho e escolha a cor: cada uma tem um sentido (cai muito, entendi, revisar, lei e artigo, pegadinha). Os grifos ficam aqui, separados por cor.") }
                if (topicId >= 0) Btn("Grifar na teoria", { Router.go(Route.Topic(topicId)) }, style = "tonal", small = true, icon = "menu_book")
            }
            return@Div
        }
        Div({ classes("mk-filter"); attr("role", "group"); attr("aria-label", "Filtrar grifos por cor") }) {
            Button({ classes(*listOfNotNull("mk-chip", if (filter == null) "on" else null).toTypedArray()); attr("aria-pressed", (filter == null).toString()); onClick { MarksNav.color.remove(topicId) } }) {
                Text("Todas"); B { Text("${marks.size}") }
            }
            MarkColors.all.forEach { tone ->
                val n = marks.count { it.color == tone.id }
                if (n > 0) Button({
                    classes(*listOfNotNull("mk-chip", if (filter == tone.id) "on" else null).toTypedArray())
                    attr("style", "--mk:${tone.hex}"); attr("title", tone.hint); attr("aria-pressed", (filter == tone.id).toString())
                    onClick { if (filter == tone.id) MarksNav.color.remove(topicId) else MarksNav.color[topicId] = tone.id }
                }) { Span({ classes("mk-chip-dot") }); Text(tone.label); B { Text("$n") } }
            }
        }
        Div({ classes("mk-list") }) {
            shown.forEach { m -> key(m.id) { MarkItem(m) } }
        }
    }
}

@Composable
private fun MarkItem(m: br.com.estudario.web.data.TheoryMark) {
    val tone = MarkColors.of(m.color)
    val md = remember(m.quote) { StudyNotebook.quoteMarkdown(m.quote) }
    val long = md.length > 360 || md.count { it == '\n' } > 6
    var open by remember(m.id) { mutableStateOf(false) }
    var recolor by remember(m.id) { mutableStateOf(false) }
    val topicId = Store.data.theories.firstOrNull { it.id == m.theoryId }?.topicId
    Div({ classes("mk-item"); attr("style", "--mk:${tone.hex}") }) {
        Div({ classes("mk-item-top") }) {
            Button({ classes("mk-tag"); attr("title", "Trocar a cor"); attr("aria-expanded", recolor.toString()); onClick { recolor = !recolor } }) {
                Span({ classes("mk-chip-dot") }); Text(tone.label)
            }
            Span({ classes("grow") })
            if (topicId != null) Button({ classes("mk-mini"); attr("title", "Abrir na teoria"); attr("aria-label", "Abrir na teoria"); onClick { TheoryJump.go(topicId, m.theoryId, m.blockIndex, m.quote) } }) { Icon("menu_book") }
            Button({ classes("mk-mini"); attr("title", "Apagar grifo"); attr("aria-label", "Apagar grifo"); onClick { Store.update { Actions.deleteTheoryMark(it, m.id) }; Toast.show("Grifo apagado") } }) { Icon("delete") }
        }
        if (recolor) Div({ classes("mk-recolor") }) {
            MarkColors.all.forEach { t ->
                Button({
                    classes(*listOfNotNull("mk-chip", if (t.id == m.color) "on" else null).toTypedArray()); attr("style", "--mk:${t.hex}")
                    onClick { if (t.id != m.color) { Store.update { Actions.setTheoryMarkColor(it, m.id, t.id) }; Toast.show("Grifo agora em ${t.label}") }; recolor = false }
                }) { Span({ classes("mk-chip-dot") }); Text(t.label) }
            }
        }
        Div({ classes(*listOfNotNull("mk-quote", if (long && !open) "clamp" else null).toTypedArray()) }) {
            Div({ classes("mk-ink") }) { Markdown(md, compact = true) }
        }
        if (long) Button({ classes("cb-link"); onClick { open = !open } }) { Text(if (open) "recolher" else "ver o trecho inteiro") }
        if (m.note.isNotBlank()) Div({ classes("mk-note") }) { Icon("edit_note"); Span { Text(NoteTags.split(m.note).second) } }
    }
}
