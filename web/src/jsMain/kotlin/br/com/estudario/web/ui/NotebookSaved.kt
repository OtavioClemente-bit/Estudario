package br.com.estudario.web.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import br.com.estudario.web.Route
import br.com.estudario.web.Router
import br.com.estudario.web.data.Actions
import br.com.estudario.web.data.Snapshot
import br.com.estudario.web.data.Store
import org.jetbrains.compose.web.dom.B
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H3
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text

/*
 * "Salvos deste assunto": dentro da página de cada tópico do Caderno, o que a pessoa guardou dele
 * (grifos, questões, dicas, pegadinhas, flashcards e notas), por tipo. Substitui a antiga aba
 * separada "Grifos, notas e salvos". O que não tem tópico fica no "Geral" (página -1).
 */

/** Id da página "Geral": o que foi guardado sem tópico (ou de tópico que não existe mais). */
const val NOTEBOOK_GENERAL = -1L

/** Tudo o que foi guardado, de um tópico. */
class SavedOfTopic(
    val marks: List<br.com.estudario.web.data.TheoryMark>,
    val questions: List<br.com.estudario.web.data.Question>,
    val tips: List<br.com.estudario.web.data.Snippet>,
    val traps: List<br.com.estudario.web.data.Snippet>,
    val cards: List<br.com.estudario.web.data.Snippet>,
    val decks: List<br.com.estudario.web.data.Summary>,
    val notes: List<br.com.estudario.web.data.UserNote>,
) {
    val total get() = marks.size + questions.size + tips.size + traps.size + cards.size + decks.size + notes.size
}

/** O que foi guardado, agrupado pelo tópico (NOTEBOOK_GENERAL para sem tópico). Nada fica de fora. */
fun savedByTopic(data: Snapshot): Map<Long, SavedOfTopic> {
    val known = data.topics.mapTo(hashSetOf()) { it.id }
    fun key(t: Long?) = t?.takeIf { it in known } ?: NOTEBOOK_GENERAL
    val theoryTopic = data.theories.associate { it.id to it.topicId }
    val marks = data.theoryMarks.groupBy { key(theoryTopic[it.theoryId]) }
    val questions = data.questions.filter { it.favorite }.groupBy { key(it.topicId) }
    val snips = data.snippets.filter { it.favorite }
    val cards = snips.filter { it.kind == "RECUPERACAO" }.groupBy { key(it.topicId) }
    val traps = snips.filter { it.kind == "PEGADINHA" }.groupBy { key(it.topicId) }
    val tips = snips.filter { it.kind != "RECUPERACAO" && it.kind != "PEGADINHA" }.groupBy { key(it.topicId) }
    val decks = data.summaries.filter { it.kind == "RAPIDO" && it.favorite }.groupBy { key(it.topicId) }
    val notes = data.notes.groupBy { key(it.topicId) }
    val ids = marks.keys + questions.keys + cards.keys + traps.keys + tips.keys + decks.keys + notes.keys
    return ids.associateWith { id ->
        SavedOfTopic(
            marks[id].orEmpty().sortedBy { it.blockIndex }, questions[id].orEmpty(), tips[id].orEmpty(), traps[id].orEmpty(),
            cards[id].orEmpty(), decks[id].orEmpty(), notes[id].orEmpty().sortedByDescending { it.createdAt },
        )
    }
}

private enum class SavedKind(val icon: String, val label: String) {
    MARKS("format_ink_highlighter", "Grifos"), QUESTIONS("quiz", "Questões"), TIPS("lightbulb", "Dicas"),
    TRAPS("pest_control_rodent", "Pegadinhas"), CARDS("style", "Flashcards"), NOTES("edit_note", "Notas"),
}

private fun SavedOfTopic.count(k: SavedKind) = when (k) {
    SavedKind.MARKS -> marks.size; SavedKind.QUESTIONS -> questions.size; SavedKind.TIPS -> tips.size
    SavedKind.TRAPS -> traps.size; SavedKind.CARDS -> cards.size + decks.size; SavedKind.NOTES -> notes.size + marks.count { it.note.isNotBlank() }
}

/** O tipo aberto em cada tópico (sobrevive a ir à teoria e voltar). */
private object SavedNav { val open = androidx.compose.runtime.mutableStateMapOf<Long, String>() }

/** O bloco "Salvos deste assunto" de um tópico: abas com contador e a lista do tipo escolhido. */
@Composable
fun TopicSavedPanel(topicId: Long) {
    val data = Store.data
    val saved = remember(data) { savedByTopic(data) }[topicId]
    val topicTitle = data.topics.firstOrNull { it.id == topicId }?.title ?: "Geral"
    Div({ classes("nbs") ; attr("id", "salvos") }) {
        Div({ classes("nbs-head") }) {
            Icon("bookmarks", filled = true)
            Div({ classes("grow") }) {
                H3 { Text("Salvos deste assunto") }
                Span({ classes("nbs-sub") }) { Text(if (saved == null || saved.total == 0) "Nada guardado ainda" else "${saved.total} ${if (saved.total == 1) "item guardado" else "itens guardados"}") }
            }
        }
        if (saved == null || saved.total == 0) {
            Div({ classes("nbs-empty") }) {
                Icon("tips_and_updates")
                P { Text("Na teoria, selecione um trecho e toque em Grifar. Nas questões, dicas, pegadinhas e flashcards, toque na estrela para guardar. Tudo deste tópico aparece aqui.") }
            }
            return@Div
        }
        val kinds = SavedKind.entries.filter { saved.count(it) > 0 }
        val chosen = SavedNav.open[topicId]?.let { n -> kinds.firstOrNull { it.name == n } } ?: kinds.first()
        Div({ classes("nbs-tabs"); attr("role", "tablist") }) {
            kinds.forEach { k ->
                Button({ classes(*listOfNotNull("nbs-tab", if (k == chosen) "on" else null).toTypedArray()); attr("role", "tab"); attr("aria-selected", (k == chosen).toString()); onClick { SavedNav.open[topicId] = k.name } }) {
                    Icon(k.icon); Span { Text(k.label) }; B({ classes("nbs-n") }) { Text("${saved.count(k)}") }
                }
            }
        }
        Div({ classes("nbs-list") }) {
            when (chosen) {
                SavedKind.MARKS -> saved.marks.forEach { m -> key("m${m.id}") { MarkPaper(m) } }
                SavedKind.QUESTIONS -> saved.questions.forEach { q -> key("q${q.id}") { QuestionPaper(q, topicId) } }
                SavedKind.TIPS -> saved.tips.forEach { t -> key("t${t.id}") { TipPaper(t, trap = false) } }
                SavedKind.TRAPS -> saved.traps.forEach { t -> key("p${t.id}") { TipPaper(t, trap = true) } }
                SavedKind.CARDS -> {
                    saved.decks.forEach { d -> key("d${d.id}") { DeckPaper(d) } }
                    saved.cards.forEachIndexed { i, c -> key("c${c.id}") { CardPaper(c, i, saved.cards.size) } }
                }
                SavedKind.NOTES -> NotesPapers(saved, topicId, topicTitle)
            }
        }
        if (chosen == SavedKind.QUESTIONS && topicId >= 0) Div({ classes("nbs-more") }) {
            Btn(if (saved.questions.size == 1) "Treinar a questão salva" else "Treinar as ${saved.questions.size} salvas", { Router.go(Route.Quiz("m-favorites~s--1~t-$topicId~b-all~d-all~n-200")) }, style = "primary", icon = "play_arrow")
        }
        if (chosen == SavedKind.CARDS && saved.cards.size + saved.decks.size > 1) Div({ classes("nbs-more") }) {
            Btn("Praticar todos, um por um", { Router.go(Route.Flashcards(topicId.takeIf { it >= 0 }, saved = true)) }, style = "primary", icon = "style")
        }
    }
}

@Composable
private fun PaperShell(tag: String, icon: String, accent: String? = null, actions: @Composable () -> Unit, body: @Composable () -> Unit) {
    Div({ classes("nbs-card"); accent?.let { attr("style", "--acc:$it") } }) {
        Div({ classes("nbs-card-top") }) { Span({ classes("pcard-tag") }) { Icon(icon, filled = true); Text(tag) } }
        Div({ classes("nbs-card-body") }) { body() }
        Div({ classes("nbs-card-actions") }) { actions() }
    }
}

@Composable
private fun MarkPaper(m: br.com.estudario.web.data.TheoryMark) {
    val topicId = Store.data.theories.firstOrNull { it.id == m.theoryId }?.topicId
    PaperShell("Grifo", "format_ink_highlighter", MarkColors.of(m.color).hex, actions = {
        if (topicId != null) Btn("Abrir na teoria", { TheoryJump.go(topicId, m.theoryId, m.blockIndex, m.quote) }, style = "tonal", small = true, icon = "menu_book")
        IconButton("delete", "Apagar grifo") { Store.update { Actions.deleteTheoryMark(it, m.id) }; Toast.show("Grifo apagado") }
    }) {
        Div({ classes("nbs-quote") }) { Markdown(m.quote, compact = true) }
        if (m.note.isNotBlank()) Div({ classes("nbs-note") }) { Icon("edit_note"); Text(NoteTags.split(m.note).second) }
    }
}

@Composable
private fun QuestionPaper(q: br.com.estudario.web.data.Question, topicId: Long) {
    PaperShell("Questão", "quiz", actions = {
        if (topicId >= 0) Btn("Resolver", { Router.go(Route.Quiz("m-favorites~s--1~t-$topicId~b-all~d-all~n-200")) }, style = "tonal", small = true, icon = "play_arrow")
        IconButton("star", "Tirar dos salvos") { Store.update { Actions.toggleFavorite(it, q.id) }; Toast.show("Questão tirada dos salvos") }
    }) {
        P({ classes("nbs-text", "clamp-5") }) { Inline(splitStatement(q.statement).let { it.command ?: it.base }.take(500)) }
    }
}

@Composable
private fun TipPaper(t: br.com.estudario.web.data.Snippet, trap: Boolean) {
    var open by remember(t.id) { mutableStateOf(false) }
    PaperShell(if (trap) "Pegadinha" else "Dica", if (trap) "pest_control_rodent" else "lightbulb", if (trap) "#EE6A4B" else "#1FA97F", actions = {
        if (t.topicId != null) Btn("Abrir no tópico", { Router.go(Route.Topic(t.topicId)) }, style = "tonal", small = true, icon = "menu_book")
        IconButton("star", "Tirar dos salvos") { Store.update { Actions.toggleSnippetFavorite(it, t.id) }; Toast.show("Tirado dos salvos") }
    }) {
        Div({ classes("nbs-text") }) { Markdown(t.text, compact = true) }
        if (!t.answer.isNullOrBlank()) {
            if (open) Div({ classes("nbs-answer") }) { Markdown(t.answer, compact = true) }
            else Btn("Ver resposta", { open = true }, style = "ghost", small = true, icon = "visibility")
        }
    }
}

@Composable
private fun CardPaper(c: br.com.estudario.web.data.Snippet, index: Int, total: Int) {
    var flipped by remember(c.id) { mutableStateOf(false) }
    Div({ classes("nbs-flash") }) {
        PaperFlashCard(c.text, c.answer.orEmpty(), flipped, index, total, "Toque para virar", compact = true, tagIcon = "style") { flipped = !flipped }
        Div({ classes("nbs-card-actions") }) {
            IconButton("star", "Tirar dos salvos") { Store.update { Actions.toggleSnippetFavorite(it, c.id) }; Toast.show("Cartão tirado dos salvos") }
        }
    }
}

@Composable
private fun DeckPaper(d: br.com.estudario.web.data.Summary) {
    PaperShell("Baralho", "style", actions = {
        Btn("Praticar", { Router.go(Route.Flashcards(d.topicId, saved = true, deckId = d.id)) }, style = "tonal", small = true, icon = "play_arrow")
        IconButton("bookmark", "Tirar do Caderno") { Store.update { Actions.toggleSummaryFavorite(it, d.id) }; Toast.show("Baralho tirado do Caderno") }
    }) {
        B({ classes("nbs-text") }) { Text(d.title.ifBlank { "Flashcards" }) }
        Div({ classes("small", "muted") }) { Text("${deckCards(d.markdown).size} cartões") }
    }
}

@Composable
private fun NotesPapers(saved: SavedOfTopic, topicId: Long, place: String) {
    var viewing by remember { mutableStateOf<br.com.estudario.web.data.UserNote?>(null) }
    var editing by remember { mutableStateOf<br.com.estudario.web.data.UserNote?>(null) }
    viewing?.let { v ->
        NoteView(v.text, v.createdAt, place, null,
            onEdit = { editing = v; viewing = null },
            onDelete = { Store.update { Actions.deleteNote(it, v.id) }; viewing = null; Toast.show("Anotação excluída") },
            onClose = { viewing = null })
    }
    editing?.let { n ->
        NotePaperEditor(
            initialText = n.text, isNew = false, createdAt = n.createdAt,
            onDismiss = { editing = null },
            onSave = { text, _ -> Store.update { Actions.saveNote(it, n.id, n.topicId, text) }; editing = null; Toast.show("Anotação salva") },
            onDelete = { Store.update { Actions.deleteNote(it, n.id) }; editing = null; Toast.show("Anotação apagada") },
            fixedPlace = place,
        )
    }
    saved.notes.forEach { n -> key("n${n.id}") { NotePaperCard(n.text, n.createdAt, onClick = { viewing = n }) } }
    saved.marks.filter { it.note.isNotBlank() }.forEach { m ->
        key("mn${m.id}") {
            NotePaperCard(NoteTags.split(m.note).second, m.createdAt, quote = m.quote, onClick = {
                Store.data.theories.firstOrNull { it.id == m.theoryId }?.topicId?.let { t -> TheoryJump.go(t, m.theoryId, m.blockIndex, m.quote) }
            })
        }
    }
}

/** Na entrada do Caderno: os assuntos com algo guardado, para chegar a cada "Salvos deste assunto". */
@Composable
fun SavedTopicsShelf() {
    val data = Store.data
    val scope = CompetitionFilter.topicIds(data)
    val all = remember(data) { savedByTopic(data) }
    val entries = all.entries.filter { (id, s) -> s.total > 0 && (id < 0 || id in scope) }
    if (entries.isEmpty()) return
    val topics = data.topics.associateBy { it.id }
    val subjects = data.subjects.associateBy { it.id }
    val sorted = entries.sortedWith(compareBy({ if (it.key < 0) 1 else 0 }, { topics[it.key]?.let { t -> subjects[t.subjectId]?.position } ?: 0 }, { topics[it.key]?.position ?: 0 }))
    Div({ classes("cb-sect") }) { B { Text("Seus salvos, por assunto") }; Span { val n = sorted.sumOf { it.value.total }; Text("$n ${if (n == 1) "item" else "itens"}") } }
    Div({ classes("nbs-shelf") }) {
        sorted.forEach { (id, s) ->
            key(id) {
                val t = topics[id]
                val subject = t?.let { subjects[it.subjectId]?.name }
                Button({ classes("nbs-row"); attr("style", "--c:${subjectColor(subject ?: "Geral")}"); onClick { Router.go(Route.NotebookPage(id)) } }) {
                    Span({ classes("hs-art"); attr("style", "--c:${subjectColor(subject ?: "Geral")}") }) { Icon(if (t == null) "folder_open" else br.com.estudario.domain.catalog.SubjectArt.iconFor(subject ?: "")) }
                    Span({ classes("grow", "nbs-row-txt") }) {
                        if (subject != null) Span({ classes("nbs-row-sub") }) { Text(subject.uppercase()) }
                        B { Text(t?.title ?: "Geral") }
                        Span({ classes("nbs-row-kinds") }) {
                            SavedKind.entries.filter { s.count(it) > 0 }.forEach { k -> Span { Icon(k.icon); Text("${s.count(k)}") } }
                        }
                    }
                    Icon("chevron_right", plain = true)
                }
            }
        }
    }
}
