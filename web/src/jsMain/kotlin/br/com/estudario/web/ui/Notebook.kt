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
import br.com.estudario.web.data.Store
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.attributes.selected
import org.jetbrains.compose.web.dom.B
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Option
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Select
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.dom.TextArea
import kotlin.js.Date

/** Tudo o que a pessoa guardou de um tópico, junto (como no app). */
private class NbBag(
    val marks: List<br.com.estudario.web.data.TheoryMark>,
    val notes: List<br.com.estudario.web.data.UserNote>,
    val tips: List<br.com.estudario.web.data.Snippet>,
    val cards: List<br.com.estudario.web.data.Snippet>,
    val decks: List<br.com.estudario.web.data.Summary>,
    val questions: List<br.com.estudario.web.data.Question>,
) {
    val total get() = marks.size + notes.size + tips.size + cards.size + decks.size + questions.size
}

private fun nbSummary(bags: List<NbBag>): String = listOf(
    bags.sumOf { it.marks.size } to ("grifo" to "grifos"),
    bags.sumOf { it.notes.size } to ("anotação" to "anotações"),
    bags.sumOf { it.tips.size } to ("dica" to "dicas"),
    bags.sumOf { it.cards.size + it.decks.size } to ("flashcard" to "flashcards"),
    bags.sumOf { it.questions.size } to ("questão" to "questões"),
).filter { it.first > 0 }.joinToString(" · ") { (n, w) -> "$n ${if (n == 1) w.first else w.second}" }

/** O tópico aberto no Caderno (sobrevive a ir e voltar da teoria). -1 = itens sem tópico. */
private object NotebookNav {
    var topic by androidx.compose.runtime.mutableStateOf<Long?>(null)
    /** Filtro de cor dos grifos (null = todas). */
    var color by androidx.compose.runtime.mutableStateOf<Int?>(null)
}

/** Os dias, contados do grifo, em que ele volta como cartão (como uma revisão espaçada). */
private val MARK_REVIEW_DAYS = setOf(1, 3, 7, 15, 30, 60, 90, 180)

/** Grifos para revisar hoje: os marcados como "revisar" e os que fazem 1, 3, 7, 15, 30... dias. */
fun marksDueToday(marks: List<br.com.estudario.web.data.TheoryMark>): List<br.com.estudario.web.data.TheoryMark> {
    val now = kotlin.js.Date.now()
    return marks.filter { m -> m.color == 2 || ((now - m.createdAt) / 86_400_000.0).toInt() in MARK_REVIEW_DAYS }
}

/**
 * Caderno de estudo, organizado como a pessoa estuda: escolhe a matéria, depois o tópico (Java,
 * dentro de Linguagens de Programação), e ali está tudo o que guardou dele, junto: grifos,
 * anotações, dicas, flashcards e questões. No topo, a revisão completa passa por todos os grifos,
 * todos os flashcards etc., um por um.
 */
@Composable
fun NotebookScreen() {
    val data = Store.data
    // Só o concurso em foco (aba no topo quando há mais de um).
    val scope = CompetitionFilter.topicIds(data)
    val topics = data.topics.filter { it.id in scope }.associateBy { it.id }
    val subjects = data.subjects.associateBy { it.id }
    val theories = data.theories.filter { it.topicId in scope }.associateBy { it.id }
    val allMarksRaw = data.theoryMarks.filter { it.theoryId in theories }
    val marks = allMarksRaw.filter { NotebookNav.color == null || it.color == NotebookNav.color }
    var markCards by remember { mutableStateOf<List<br.com.estudario.web.data.TheoryMark>?>(null) }
    markCards?.let { list -> MarkCardsReview(list, { m -> theories[m.theoryId]?.topicId?.let { t -> topics[t]?.title } ?: "Teoria" }) { markCards = null } }
    val favQuestions = data.questions.filter { it.favorite && !it.hidden && it.topicId in scope }
    val recallCards = data.snippets.filter { it.kind == "RECUPERACAO" && it.favorite && it.topicId in scope }
    val favDecks = data.summaries.filter { it.kind == "RAPIDO" && it.favorite && it.topicId in scope }
    val tips = data.snippets.filter { it.favorite && it.kind != "RECUPERACAO" && it.topicId in scope }
    val notes = data.notes.filter { it.topicId == null || it.topicId in scope }
    fun markTopic(m: br.com.estudario.web.data.TheoryMark) = theories[m.theoryId]?.topicId

    val ids = (marks.map { markTopic(it) } + notes.map { it.topicId } + tips.map { it.topicId } + recallCards.map { it.topicId } + favDecks.map { it.topicId } + favQuestions.map { it.topicId }).map { it ?: -1L }.toSet()
    fun order(id: Long): Long { val t = topics[id]; val s = t?.let { subjects[it.subjectId] }; return (s?.position ?: Int.MAX_VALUE).toLong() * 100_000L + (t?.position ?: 0) }
    val bags: List<Pair<Long, NbBag>> = ids.sortedBy(::order).map { id ->
        fun same(t: Long?) = (t ?: -1L) == id
        id to NbBag(
            marks.filter { same(markTopic(it)) }.sortedBy { it.blockIndex },
            notes.filter { same(it.topicId) }.sortedByDescending { it.createdAt },
            tips.filter { same(it.topicId) },
            recallCards.filter { same(it.topicId) },
            favDecks.filter { same(it.topicId) },
            favQuestions.filter { same(it.topicId) },
        )
    }
    fun topicName(id: Long?) = id?.takeIf { it >= 0 }?.let { topics[it]?.title } ?: "Sem tópico"
    fun subjectOf(id: Long) = topics[id]?.let { subjects[it.subjectId] }

    var reviewing by remember { mutableStateOf<Pair<String, List<NbCard>>?>(null) }
    reviewing?.let { (title, cards) -> NotebookReview(title, cards) { reviewing = null } }
    fun markCard(m: br.com.estudario.web.data.TheoryMark, kicker: String = "GRIFO") = markTopic(m).let { t ->
        NbCard(kicker, topicName(t), m.quote, m.note.ifBlank { null }, t) { if (t != null) TheoryJump.go(t, m.theoryId, m.blockIndex, m.quote) }
    }
    fun noteCard(n: br.com.estudario.web.data.UserNote) = NbCard("ANOTAÇÃO", topicName(n.topicId), null, n.text, n.topicId)
    fun tipCard(t: br.com.estudario.web.data.Snippet) = NbCard(if (t.kind == "PEGADINHA") "PEGADINHA" else "DICA", topicName(t.topicId), null, listOfNotNull(t.text, t.answer?.takeIf { it.isNotBlank() }?.let { "Resposta: $it" }).joinToString("\n\n"), t.topicId)
    fun bagCards(b: NbBag) = b.marks.map { markCard(it) } + b.notes.map(::noteCard) + b.tips.map(::tipCard)

    // Notas: ler, editar, criar (a folha de caderno).
    var viewing by remember { mutableStateOf<Any?>(null) }
    var editing by remember { mutableStateOf<br.com.estudario.web.data.UserNote?>(null) }
    var creatingFor by remember { mutableStateOf<Long?>(null) }
    var creating by remember { mutableStateOf(false) }
    var editingMark by remember { mutableStateOf<br.com.estudario.web.data.TheoryMark?>(null) }
    val leafTopics = topics.values.filter { t -> data.topics.none { it.parentTopicId == t.id } }.map { it.id to it.title }
    when (val v = viewing) {
        is br.com.estudario.web.data.UserNote -> NoteView(v.text, v.createdAt, topicName(v.topicId), null,
            onEdit = { editing = v; viewing = null },
            onDelete = { Store.update { Actions.deleteNote(it, v.id) }; viewing = null; Toast.show("Anotação excluída") },
            onClose = { viewing = null })
        is br.com.estudario.web.data.TheoryMark -> NoteView(v.note, v.createdAt, topicName(markTopic(v)), v.quote,
            onEdit = { editingMark = v; viewing = null },
            onDelete = { Store.update { Actions.setTheoryMarkNote(it, v.id, "") }; viewing = null; Toast.show("Nota excluída (o grifo ficou)") },
            onClose = { viewing = null },
            onOpen = markTopic(v)?.let { t -> { viewing = null; TheoryJump.go(t, v.theoryId, v.blockIndex, v.quote) } })
    }
    if (creating || editing != null) {
        val n = editing
        NotePaperEditor(
            initialText = n?.text.orEmpty(), isNew = n == null, createdAt = n?.createdAt,
            onDismiss = { creating = false; editing = null },
            onSave = { text, topicId ->
                Store.update { Actions.saveNote(it, n?.id ?: 0L, topicId, text) }; Toast.show("Anotação salva no Caderno")
                creating = false; editing = null
            },
            onDelete = n?.let { note -> { Store.update { Actions.deleteNote(it, note.id) }; editing = null; Toast.show("Anotação apagada") } },
            topics = leafTopics, initialTopicId = n?.topicId ?: creatingFor,
        )
    }
    editingMark?.let { m ->
        NotePaperEditor(
            initialText = m.note, isNew = m.note.isBlank(), createdAt = m.createdAt,
            onDismiss = { editingMark = null },
            onSave = { text, _ -> Store.update { Actions.setTheoryMarkNote(it, m.id, text) }; editingMark = null; Toast.show("Nota salva") },
            onDelete = if (m.note.isNotBlank()) ({ Store.update { Actions.setTheoryMarkNote(it, m.id, "") }; editingMark = null; Toast.show("Nota apagada (o grifo ficou)") }) else null,
            quote = m.quote, fixedPlace = topicName(markTopic(m)),
            deleteLabel = "Apagar a nota (o grifo fica)",
        )
    }

    // ============================== O TÓPICO ABERTO ==============================
    val opened = NotebookNav.topic
    val bag = opened?.let { id -> bags.firstOrNull { it.first == id }?.second }
    if (opened != null && bag != null) {
        val subjectName = subjectOf(opened)?.name ?: "Geral"
        val color = subjectColor(subjectName)
        Div({ attr("style", "display:flex") }) { Btn("Todas as matérias", { NotebookNav.topic = null }, style = "ghost", small = true, icon = "chevron_left") }
        Div({ classes("nbt-hero"); attr("style", "--c:$color") }) {
            Div({ classes("row"); attr("style", "gap:14px;align-items:center") }) {
                Span({ classes("hs-art", "big"); attr("style", "--c:$color") }) { Icon(br.com.estudario.domain.catalog.SubjectArt.iconFor(subjectName)) }
                Div({ classes("grow"); attr("style", "min-width:0") }) {
                    Div({ classes("nbt-subject") }) { Text(subjectName.uppercase()) }
                    org.jetbrains.compose.web.dom.H2({ classes("nbt-title") }) { Text(topicName(opened)) }
                    Div({ classes("small", "muted") }) { Text(nbSummary(listOf(bag))) }
                }
            }
            Div({ classes("row", "wrap"); attr("style", "gap:8px;margin-top:14px") }) {
                val cards = bagCards(bag)
                if (cards.isNotEmpty()) Btn("Revisar este tópico, um por um", { reviewing = topicName(opened) to cards }, style = "primary", icon = "play_arrow")
                if (bag.cards.isNotEmpty() || bag.decks.isNotEmpty()) Btn("Praticar flashcards", { Router.go(Route.Flashcards(opened.takeIf { it >= 0 })) }, style = "outline", icon = "style")
                if (bag.questions.isNotEmpty() && opened >= 0) Btn("Treinar questões salvas", { Router.go(Route.Quiz("m-favorites~s--1~t-$opened~b-all~d-all~n-200")) }, style = "outline", icon = "quiz")
                if (opened >= 0) Btn("Abrir conteúdo", { Router.go(Route.Topic(opened)) }, style = "ghost", icon = "menu_book")
                Btn("Anotar", { creatingFor = opened.takeIf { it >= 0 }; creating = true }, style = "ghost", icon = "edit_note")
            }
        }
        if (allMarksRaw.any { m -> (theories[m.theoryId]?.topicId ?: -1L) == opened }) ColorFilterRow(allMarksRaw.filter { m -> (theories[m.theoryId]?.topicId ?: -1L) == opened })
        if (bag.marks.isNotEmpty()) NbSection("format_ink_highlighter", "Grifos", bag.marks.size) {
            bag.marks.forEach { mark ->
                key("m${mark.id}") {
                    val topicId = markTopic(mark)
                    Div({ classes("saved-item", "mark-item"); attr("style", "border-left:5px solid ${MarkColors.of(mark.color).hex}") }) {
                        Div({
                            classes("mark-body", "grow")
                            attr("role", "link"); attr("tabindex", "0")
                            onClick { if (mark.note.isNotBlank()) viewing = mark else if (topicId != null) TheoryJump.go(topicId, mark.theoryId, mark.blockIndex, mark.quote) }
                        }) {
                            Markdown(mark.quote, compact = true)
                            if (mark.note.isNotBlank()) P({ classes("small", "muted", "mark-note") }) { Icon("edit_note"); Text(NoteTags.split(mark.note).second) }
                        }
                        Div({ classes("stack", "tight"); attr("style", "align-items:flex-end") }) {
                            if (topicId != null) IconButton("menu_book", "Abrir na teoria") { TheoryJump.go(topicId, mark.theoryId, mark.blockIndex, mark.quote) }
                            if (mark.note.isBlank()) IconButton("edit_note", "Anotar") { editingMark = mark }
                            IconButton("delete", "Apagar grifo") { Store.update { Actions.deleteTheoryMark(it, mark.id) } }
                        }
                    }
                }
            }
        }
        if (bag.notes.isNotEmpty()) NbSection("edit_note", "Anotações", bag.notes.size) {
            bag.notes.forEach { n -> key("n${n.id}") { NotePaperCard(n.text, n.createdAt, onClick = { viewing = n }) } }
        }
        if (bag.tips.isNotEmpty()) NbSection("lightbulb", "Dicas e pegadinhas", bag.tips.size) {
            bag.tips.forEach { t -> key("t${t.id}") { SavedCard(t.id, null, t.text, t.answer) } }
        }
        if (bag.cards.isNotEmpty() || bag.decks.isNotEmpty()) NbSection("style", "Flashcards", bag.cards.size + bag.decks.size) {
            bag.decks.forEach { d ->
                key("d${d.id}") {
                    Div({ classes("saved-item") }) {
                        Div({ classes("grow") }) { B { Text("Baralho · ${d.title.ifBlank { "Flashcards" }}") }; Div({ classes("small", "muted") }) { Text("${deckCards(d.markdown).size} cartões") } }
                        Btn("Praticar", { Router.go(Route.Flashcards(d.topicId)) }, style = "tonal", small = true, icon = "style")
                        IconButton("bookmark", "Tirar do Caderno") { Store.update { Actions.toggleSummaryFavorite(it, d.id) } }
                    }
                }
            }
            bag.cards.forEach { c -> key("c${c.id}") { SavedCard(c.id, null, c.text, c.answer) } }
        }
        if (bag.questions.isNotEmpty()) NbSection("quiz", "Questões salvas", bag.questions.size) {
            bag.questions.forEach { q ->
                key("q${q.id}") {
                    Div({ classes("saved-item") }) {
                        P({ classes("clamp-3", "grow") }) { Inline(splitStatement(q.statement).let { it.command ?: it.base }.take(300)) }
                        IconButton("star", "Tirar dos salvos") { Store.update { Actions.toggleFavorite(it, q.id) } }
                    }
                }
            }
        }
        return
    }
    if (opened != null && bag == null) NotebookNav.topic = null

    // ============================== AS MATÉRIAS ==============================
    PageHead("Caderno de estudo", "Escolha a matéria e o tópico: tudo o que você guardou dele está junto")
    CompetitionTabs()
    val total = bags.sumOf { it.second.total }
    val subjectsWithItems = bags.mapNotNull { subjectOf(it.first)?.id }.distinct().size
    Div({ classes("nb-hero") }) {
        Folha("happy", 64)
        Div({ classes("grow") }) {
            B { Text(if (total == 0) "Seu caderno está em branco" else "$total ${if (total == 1) "coisa guardada" else "coisas guardadas"}") }
            Div({ classes("small") }) { Text(if (total == 0) "Grife trechos, salve questões e escreva anotações: tudo vem para cá, arrumado por matéria." else "Em $subjectsWithItems ${if (subjectsWithItems == 1) "matéria" else "matérias"}. Abra uma matéria e o tópico, ou revise tudo um por um.") }
        }
        Btn("Nova anotação", { creatingFor = null; creating = true }, style = "outline", icon = "edit_note")
    }
    if (total > 0) {
        // Revisão completa: passar por todos os grifos, todas as anotações, todos os flashcards...
        val allMarks = bags.flatMap { it.second.marks }
        val allNotes = bags.flatMap { it.second.notes }
        val allTips = bags.flatMap { it.second.tips }
        val markNotes = allMarks.filter { it.note.isNotBlank() }
        Div({ classes("nb-review-all") }) {
            Div { B { Text("Revisão completa") }; Div({ classes("small", "muted") }) { Text("Passe por tudo o que guardou, de todas as matérias, na ordem do edital.") } }
            Div({ classes("nb-chips") }) {
                val everything = bags.flatMap { bagCards(it.second) }
                if (everything.isNotEmpty()) NbChip("play_arrow", "Tudo, um por um", everything.size, primary = true) { reviewing = "Revisão completa" to everything }
                if (allMarks.isNotEmpty()) NbChip("format_ink_highlighter", "Grifos", allMarks.size) { reviewing = "Todos os grifos" to allMarks.map { markCard(it) } }
                val due = marksDueToday(allMarksRaw)
                if (due.isNotEmpty()) NbChip("style", "Grifos de hoje, em cartão", due.size) { markCards = due }
                if (allNotes.size + markNotes.size > 0) NbChip("edit_note", "Anotações", allNotes.size + markNotes.size) { reviewing = "Todas as anotações" to (allNotes.map(::noteCard) + markNotes.map { markCard(it, "NOTA NUM TRECHO") }) }
                if (allTips.isNotEmpty()) NbChip("lightbulb", "Dicas", allTips.size) { reviewing = "Todas as dicas" to allTips.map(::tipCard) }
                val cardCount = recallCards.size + favDecks.size
                if (cardCount > 0) NbChip("style", "Flashcards", cardCount) { Router.go(Route.Flashcards(null)) }
                if (favQuestions.isNotEmpty()) NbChip("quiz", "Questões", favQuestions.size) { Router.go(Route.Quiz("m-favorites~n-200")) }
            }
        }
    }
    var query by remember { mutableStateOf("") }
    if (allMarksRaw.isNotEmpty()) ColorFilterRow(allMarksRaw)
    if (total > 0) Div({ classes("catalog-search"); attr("style", "margin:14px 0") }) {
        Icon("search", plain = true)
        org.jetbrains.compose.web.dom.Input(InputType.Search) { classes("input"); attr("placeholder", "Buscar matéria, tópico ou o que você guardou"); value(query); onInput { query = it.value } }
    }
    val needle = query.trim().lowercase()
    fun has(vararg t: String?) = needle.isEmpty() || t.any { it != null && it.lowercase().contains(needle) }
    val visible = bags.filter { (id, b) ->
        needle.isEmpty() || has(topicName(id), subjectOf(id)?.name) || b.marks.any { has(it.quote, it.note) } || b.notes.any { has(it.text) } ||
            b.tips.any { has(it.text, it.answer) } || b.cards.any { has(it.text, it.answer) } || b.questions.any { has(it.statement) }
    }
    if (total == 0) { Empty("edit_note", "Seu caderno está em branco", "Na leitura da teoria, grife trechos e escreva notas. Salve questões, dicas e flashcards nos tópicos. Tudo aparece aqui, por matéria."); return }
    if (visible.isEmpty()) { Empty("search", "Nada encontrado", "Nenhuma matéria, tópico ou item guardado tem esse texto."); return }
    val opens = remember { androidx.compose.runtime.mutableStateMapOf<Long, Boolean>() }
    Div({ classes("eyebrow"); attr("style", "margin:6px 0 8px") }) { Text("SUAS MATÉRIAS") }
    Div({ classes("nb-subjects") }) {
        visible.groupBy { subjectOf(it.first) }.forEach { (subject, entries) ->
            val sid = subject?.id ?: -1L
            key(sid) {
                val name = subject?.name ?: "Geral"
                val color = subjectColor(name)
                val open = needle.isNotEmpty() || opens[sid] == true
                Div({ classes(*listOfNotNull("nb-subj", if (open) "open" else null).toTypedArray()); attr("style", "--c:$color") }) {
                    Div({ classes("nb-subj-head"); attr("role", "button"); attr("tabindex", "0"); attr("aria-expanded", open.toString()); onClick { opens[sid] = !open } }) {
                        Span({ classes("hs-art"); attr("style", "--c:$color") }) { Icon(br.com.estudario.domain.catalog.SubjectArt.iconFor(name)) }
                        Div({ classes("grow"); attr("style", "min-width:0") }) {
                            B({ classes("clamp-2") }) { Text(name) }
                            Div({ classes("xs", "muted") }) { Text(nbSummary(entries.map { it.second })) }
                            Div({ classes("nb-subj-count") }) { Text("${entries.size} ${if (entries.size == 1) "tópico" else "tópicos"}") }
                        }
                        Icon(if (open) "expand_less" else "expand_more", extraClass = "faint")
                    }
                    if (open) Div({ classes("nb-topics") }) {
                        entries.forEach { (id, b) ->
                            key(id) {
                                Button({ classes("nb-topic-row"); onClick { NotebookNav.topic = id; kotlinx.browser.window.scrollTo(0.0, 0.0) } }) {
                                    Span({ classes("grow"); attr("style", "min-width:0;text-align:left") }) {
                                        B({ classes("clamp-2") }) { Text(topicName(id)) }
                                        Span({ classes("xs", "muted") }) { Text(nbSummary(listOf(b))) }
                                    }
                                    Span({ classes("count-pill") }) { Text("${b.total}") }
                                    Icon("chevron_right", plain = true)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun NbSection(icon: String, label: String, count: Int, content: @Composable () -> Unit) {
    Div({ classes("nbt-section") }) {
        Div({ classes("nbt-section-head") }) { Icon(icon); B { Text(label) }; Span({ classes("count-pill") }) { Text("$count") } }
        Div({ classes("stack") }) { content() }
    }
}

@Composable
private fun NbChip(icon: String, label: String, count: Int, primary: Boolean = false, onClick: () -> Unit) {
    Button({ classes(*listOfNotNull("nb-chip", if (primary) "primary" else null).toTypedArray()); onClick { onClick() } }) {
        Icon(icon); Text(label); Span({ classes("n") }) { Text("$count") }
    }
}

@Composable
private fun SavedCard(id: Long, topic: String?, text: String, answer: String?) {
    var open by remember { mutableStateOf(false) }
    Div({ classes("saved-item", "tip-item") }) {
        Div({ classes("grow"); attr("style", "min-width:0") }) {
            if (topic != null) Chip(topic, icon = "lightbulb")
            Markdown(text, compact = true)
            if (!answer.isNullOrBlank()) {
                if (open) Div({ classes("recall-answer") }) { Markdown(answer, compact = true) }
                else Btn("Ver resposta", { open = true }, style = "ghost", small = true, icon = "visibility")
            }
        }
        IconButton("star", "Tirar dos salvos") { Store.update { Actions.toggleSnippetFavorite(it, id) } }
    }
}

/**
 * Anotações: as livres e as notas feitas nos trechos da teoria, juntas, mais novas primeiro, por
 * tópico. Cada uma é um pedaço de folha pautada; tocar abre a folha para editar.
 */
@Composable
private fun NotesTab(
    notes: List<br.com.estudario.web.data.UserNote>,
    markNotes: List<br.com.estudario.web.data.TheoryMark>,
    topicOfMark: (br.com.estudario.web.data.TheoryMark) -> Long?,
    topics: List<Pair<Long, String>>,
    onReview: (String, List<Any>) -> Unit,
) {
    var editing by remember { mutableStateOf<br.com.estudario.web.data.UserNote?>(null) }
    var creating by remember { mutableStateOf(false) }
    var editingMark by remember { mutableStateOf<br.com.estudario.web.data.TheoryMark?>(null) }
    var viewing by remember { mutableStateOf<Any?>(null) }
    when (val v = viewing) {
        is br.com.estudario.web.data.UserNote -> NoteView(v.text, v.createdAt, v.topicId?.let { tid -> topics.firstOrNull { it.first == tid }?.second } ?: "Geral", null,
            onEdit = { editing = v; viewing = null },
            onDelete = { Store.update { Actions.deleteNote(it, v.id) }; viewing = null; Toast.show("Anotação excluída") },
            onClose = { viewing = null })
        is br.com.estudario.web.data.TheoryMark -> NoteView(v.note, v.createdAt, topicOfMark(v)?.let { tid -> topics.firstOrNull { it.first == tid }?.second } ?: "Teoria", v.quote,
            onEdit = { editingMark = v; viewing = null },
            onDelete = { Store.update { Actions.setTheoryMarkNote(it, v.id, "") }; viewing = null; Toast.show("Nota excluída (o grifo ficou)") },
            onClose = { viewing = null },
            onOpen = topicOfMark(v)?.let { t -> { viewing = null; TheoryJump.go(t, v.theoryId, v.blockIndex, v.quote) } })
    }
    val names = topics.toMap()
    if (creating || editing != null) {
        val n = editing
        NotePaperEditor(
            initialText = n?.text.orEmpty(), isNew = n == null, createdAt = n?.createdAt,
            onDismiss = { creating = false; editing = null },
            onSave = { text, topicId ->
                Store.update { Actions.saveNote(it, n?.id ?: 0L, topicId, text) }
                creating = false; editing = null
                Toast.show("Anotação salva no Caderno")
            },
            onDelete = n?.let { note -> { Store.update { Actions.deleteNote(it, note.id) }; editing = null; Toast.show("Anotação apagada") } },
            topics = topics, initialTopicId = n?.topicId,
        )
    }
    editingMark?.let { m ->
        NotePaperEditor(
            initialText = m.note, isNew = false, createdAt = m.createdAt,
            onDismiss = { editingMark = null },
            onSave = { text, _ -> Store.update { Actions.setTheoryMarkNote(it, m.id, text) }; editingMark = null; Toast.show("Nota salva") },
            onDelete = { Store.update { Actions.setTheoryMarkNote(it, m.id, "") }; editingMark = null; Toast.show("Nota apagada (o grifo ficou)") },
            quote = m.quote, fixedPlace = topicOfMark(m)?.let { names[it] } ?: "Teoria",
            deleteLabel = "Apagar a nota (o grifo fica)",
        )
    }
    Button({ classes("note-new"); onClick { creating = true } }) {
        Span({ classes("note-new-ic") }) { Icon("edit_note") }
        Span({ classes("grow") }) { B { Text("Nova anotação") }; Span({ classes("small", "muted") }) { Text("Macete, resumo, dúvida… com suas palavras. Na teoria, selecione um trecho e use Grifar com nota.") } }
        Icon("add", plain = true)
    }
    if (notes.isEmpty() && markNotes.isEmpty()) { Empty("edit_note", "Nenhuma anotação ainda", "Suas anotações e as notas dos trechos grifados aparecem aqui."); return }
    val rows: List<Pair<Long?, Any>> = (notes.map { it.topicId to (it as Any) } + markNotes.map { topicOfMark(it) to (it as Any) })
        .sortedByDescending { (_, v) -> (v as? br.com.estudario.web.data.UserNote)?.createdAt ?: (v as br.com.estudario.web.data.TheoryMark).createdAt }
    BySubject(rows.groupBy({ it.first }, { it.second }), review = { list, name -> onReview("$name · Anotações", list) }, reviewAll = { list -> onReview("Caderno · Anotações", list) }, reviewLabel = "Revisar as anotações uma por uma") { row ->
        when (row) {
            is br.com.estudario.web.data.UserNote -> NotePaperCard(row.text, row.createdAt, onClick = { viewing = row })
            is br.com.estudario.web.data.TheoryMark -> NotePaperCard(row.note, row.createdAt, onClick = { viewing = row }, quote = row.quote) {
                topicOfMark(row)?.let { t -> Button({ classes("note-open"); onClick { TheoryJump.go(t, row.theoryId, row.blockIndex, row.quote) } }) { Icon("menu_book"); Text("Abrir na teoria") } }
            }
        }
    }
}

/**
 * Agrupa por tópico, com o nome da matéria, para o Caderno não virar uma lista solta. Com mais de um
 * grupo, todos começam recolhidos (só o cabeçalho com a quantidade): a página não vira um rolo
 * gigante e a pessoa abre o tópico que quer, ou todos de uma vez.
 */
@Composable
fun <T> ByTopic(groups: Map<*, List<T>>, item: @Composable (T) -> Unit) {
    val data = Store.data
    val topics = data.topics.associateBy { it.id }
    val subjects = data.subjects.associateBy { it.id }
    val many = groups.size > 1
    val opened = remember { androidx.compose.runtime.mutableStateMapOf<Long, Boolean>() }
    fun isOpen(id: Long) = opened[id] ?: !many
    val ids = groups.keys.map { (it as Long?) ?: -1L }
    Div({ classes("stack") }) {
        if (many) Div({ classes("row", "between") }) {
            Span({ classes("small", "muted") }) { Text("${groups.size} tópicos · ${groups.values.sumOf { it.size }} itens") }
            val allOpen = ids.all { isOpen(it) }
            Btn(if (allOpen) "Recolher todos" else "Abrir todos", { ids.forEach { opened[it] = !allOpen } }, style = "ghost", small = true, icon = if (allOpen) "unfold_less" else "unfold_more")
        }
        groups.entries.map { (it.key as Long?) to it.value }.sortedBy { e -> e.first?.let { topics[it]?.title } ?: "~" }.forEach { (topicId, list) ->
            key(topicId ?: -1L) {
                val topic = topicId?.let { topics[it] }
                val id = topicId ?: -1L
                val open = isOpen(id)
                Div({ classes(*listOfNotNull("topic-group", if (open) null else "collapsed").toTypedArray()) }) {
                    Div({
                        classes("topic-group-head", "clickable")
                        attr("role", "button"); attr("tabindex", "0"); attr("aria-expanded", open.toString())
                        onClick { opened[id] = !open }
                    }) {
                        Icon(if (open) "expand_less" else "expand_more", extraClass = "faint")
                        Div({ classes("grow"); attr("style", "min-width:0") }) {
                            Div({ classes("crumb") }) { Text(topic?.let { subjects[it.subjectId]?.name }?.uppercase() ?: "GERAL") }
                            B { Text(topic?.title ?: "Sem tópico") }
                        }
                        Span({ classes("count-pill") }) { Text("${list.size}") }
                        if (topic != null) Span({ onClick { it.stopPropagation() } }) { IconButton("arrow_forward", "Abrir tópico") { Router.go(Route.Topic(topic.id)) } }
                    }
                    if (open) Div({ classes("topic-group-body") }) { list.forEach { item(it) } }
                }
            }
        }
    }
}

/** Um cartão do modo revisão do Caderno. */
data class NbCard(val kicker: String, val place: String, val quote: String?, val body: String?, val topicId: Long?, val open: (() -> Unit)? = null)

/**
 * O Caderno por matéria: cada matéria é um cabeçalho fechado ao entrar (a inicial, o nome e quantos
 * itens e tópicos); aberta, mostra os tópicos na ordem do edital. Com muitas matérias vira um índice
 * limpo. [review] revisa os itens da matéria um por um; [reviewAll] tudo da aba.
 */
@Composable
fun <T> BySubject(
    groups: Map<*, List<T>>,
    review: ((List<T>, String) -> Unit)? = null,
    reviewAll: ((List<T>) -> Unit)? = null,
    reviewLabel: String = "Revisar um por um",
    item: @Composable (T) -> Unit,
) {
    val data = Store.data
    val topics = data.topics.associateBy { it.id }
    val subjects = data.subjects.associateBy { it.id }
    val opened = remember { androidx.compose.runtime.mutableStateMapOf<Long, Boolean>() }
    val bySubject = groups.entries.map { (it.key as Long?) to it.value }
        .groupBy { (topicId, _) -> topicId?.let { topics[it]?.subjectId } }
        .entries.sortedWith(compareBy({ it.key == null }, { it.key?.let { k -> subjects[k]?.position } ?: Int.MAX_VALUE }))
    fun ordered(entries: List<Pair<Long?, List<T>>>) = entries.sortedWith(compareBy({ it.first == null }, { it.first?.let { t -> topics[t]?.position } ?: 0 }))
    Div({ classes("stack") }) {
        if (reviewAll != null) Btn(reviewLabel, { reviewAll(bySubject.flatMap { s -> ordered(s.value).flatMap { it.second } }) }, style = "primary", icon = "play_arrow")
        if (bySubject.size > 1) Div({ classes("row", "between") }) {
            Span({ classes("small", "muted") }) { Text("${bySubject.size} matérias · ${groups.values.sumOf { it.size }} itens") }
            val allOpen = bySubject.all { opened[it.key ?: -1L] == true }
            Btn(if (allOpen) "Fechar todas" else "Abrir todas", { bySubject.forEach { opened[it.key ?: -1L] = !allOpen } }, style = "ghost", small = true, icon = if (allOpen) "unfold_less" else "unfold_more")
        }
        bySubject.forEach { (subjectId, entries) ->
            val id = subjectId ?: -1L
            key(id) {
                val name = subjectId?.let { subjects[it]?.name } ?: "Geral"
                val open = opened[id] == true
                val count = entries.sumOf { it.second.size }
                Div({ classes(*listOfNotNull("nb-subject", if (open) "open" else null).toTypedArray()) }) {
                    Div({
                        classes("nb-subject-head")
                        attr("role", "button"); attr("tabindex", "0"); attr("aria-expanded", open.toString())
                        onClick { opened[id] = !open }
                    }) {
                        // O desenho da matéria (turma do Folha), igual ao do edital e do início.
                        Span({ classes("hs-art"); attr("style", "--c:${subjectColor(name)}") }) { Icon(br.com.estudario.domain.catalog.SubjectArt.iconFor(name)) }
                        Div({ classes("grow"); attr("style", "min-width:0") }) {
                            B({ classes("clamp-2") }) { Text(name) }
                            Div({ classes("xs", "muted") }) { Text("$count ${if (count == 1) "item" else "itens"} · ${entries.size} ${if (entries.size == 1) "tópico" else "tópicos"}") }
                        }
                        if (review != null) Span({ onClick { it.stopPropagation() } }) { IconButton("play_arrow", "Revisar esta matéria") { review(ordered(entries).flatMap { it.second }, name) } }
                        Icon(if (open) "expand_less" else "expand_more", extraClass = "faint")
                    }
                    if (open) Div({ classes("nb-subject-body") }) {
                        ordered(entries).forEach { (topicId, list) ->
                            key(topicId ?: -1L) {
                                Div({ classes("nb-topic") }) {
                                    Span({ classes("nb-dot") })
                                    val topic = topicId?.let { topics[it] }
                                    if (topic != null) Button({ classes("nb-topic-name"); onClick { Router.go(Route.Topic(topic.id)) } }) { Text(topic.title) }
                                    else B({ classes("nb-topic-name") }) { Text("Sem tópico") }
                                    Span({ classes("xs", "muted") }) { Text("${list.size}") }
                                }
                                Div({ classes("nb-items") }) { list.forEach { item(it) } }
                            }
                        }
                    }
                }
            }
        }
    }
}

/** Revisar o Caderno um item por vez, como um fichário, na ordem do edital. Setas do teclado também. */
@Composable
private fun NotebookReview(title: String, cards: List<NbCard>, onClose: () -> Unit) {
    var index by remember(cards) { mutableStateOf(0) }
    val card = cards.getOrNull(index) ?: return
    androidx.compose.runtime.DisposableEffect(cards) {
        val keys: (org.w3c.dom.events.Event) -> Unit = { e ->
            when ((e as org.w3c.dom.events.KeyboardEvent).key) {
                "ArrowRight" -> if (index < cards.lastIndex) index++
                "ArrowLeft" -> if (index > 0) index--
                "Escape" -> onClose()
            }
        }
        kotlinx.browser.window.addEventListener("keydown", keys)
        onDispose { kotlinx.browser.window.removeEventListener("keydown", keys) }
    }
    Modal(onDismiss = onClose, extraClass = "nb-review") {
        Div({ classes("row", "between") }) {
            Div { B { Text(title) }; Div({ classes("xs", "muted") }) { Text("${index + 1} de ${cards.size} · use as setas do teclado") } }
            IconButton("close", "Fechar revisão") { onClose() }
        }
        Div({ classes("nb-review-bar") }) { Div({ attr("style", "width:${(index + 1) * 100 / cards.size}%") }) }
        Div({ classes("nb-review-card") }) {
            Div({ classes("row"); attr("style", "gap:8px;flex-wrap:wrap") }) {
                Span({ classes("nb-kicker") }) { Text(card.kicker) }
                Span({ classes("small", "primary-ink", "strong") }) { Text(card.place) }
            }
            if (!card.quote.isNullOrBlank()) Div({ classes("nb-review-quote") }) { Markdown(card.quote, compact = true) }
            if (!card.body.isNullOrBlank()) {
                if (card.quote != null) Div({ classes("xs", "muted", "strong") }) { Text("SUA NOTA") }
                val (tag, body) = NoteTags.split(card.body)
                Div({ classes("nb-review-body") }) {
                    if (tag != null) Span({ classes("note-pill", NoteTags.cls(tag)) }) { Text(tag.uppercase()) }
                    Markdown(body, compact = true)
                }
            }
            card.open?.let { open -> Btn("Abrir na teoria", { onClose(); open() }, style = "ghost", small = true, icon = "menu_book") }
        }
        Div({ classes("row", "between") }) {
            Btn("Anterior", { index-- }, style = "outline", icon = "chevron_left", enabled = index > 0)
            Btn(if (index < cards.lastIndex) "Próximo" else "Concluir", { if (index < cards.lastIndex) index++ else onClose() }, style = "primary", icon = if (index < cards.lastIndex) "chevron_right" else "check")
        }
    }
}

/** Filtrar os grifos por cor: todas, cai muito, entendi, revisar (com a contagem de cada uma). */
@Composable
private fun ColorFilterRow(marks: List<br.com.estudario.web.data.TheoryMark>) {
    Div({ classes("nb-colors") }) {
        Span({ classes("xs", "muted", "strong") }) { Text("GRIFOS POR COR") }
        Button({ classes(*listOfNotNull("nb-color", if (NotebookNav.color == null) "on" else null).toTypedArray()); onClick { NotebookNav.color = null } }) { Text("Todas"); Span({ classes("n") }) { Text("${marks.size}") } }
        MarkColors.all.forEach { tone ->
            val n = marks.count { it.color == tone.id }
            Button({ classes(*listOfNotNull("nb-color", if (NotebookNav.color == tone.id) "on" else null).toTypedArray()); onClick { NotebookNav.color = if (NotebookNav.color == tone.id) null else tone.id } }) {
                Span({ classes("tone-dot"); attr("style", "background:${tone.hex}") }); Text(tone.label); Span({ classes("n") }) { Text("$n") }
            }
        }
    }
}

/**
 * Os grifos voltando como cartão, igual aos flashcards: aparece o tópico, o trecho fica escondido até
 * a pessoa tentar lembrar; "Lembrei" pinta de verde (entendi) e "Não lembrei" de rosa (revisar), e o
 * rosa volta todo dia até virar verde.
 */
@Composable
private fun MarkCardsReview(cards: List<br.com.estudario.web.data.TheoryMark>, topicOf: (br.com.estudario.web.data.TheoryMark) -> String, onClose: () -> Unit) {
    var index by remember(cards) { mutableStateOf(0) }
    var shown by remember(cards) { mutableStateOf(false) }
    var remembered by remember(cards) { mutableStateOf(0) }
    Modal(onDismiss = onClose, extraClass = "nb-review") {
        val card = cards.getOrNull(index)
        if (card == null) {
            Div({ classes("stack"); attr("style", "align-items:center;text-align:center;padding:12px 0") }) {
                Folha("happy", 90)
                org.jetbrains.compose.web.dom.H2 { Text("Grifos de hoje revisados!") }
                P({ classes("muted") }) { Text("Você lembrou de $remembered de ${cards.size}. Os que ficaram em rosa voltam amanhã.") }
                Btn("Fechar", onClose, style = "primary", icon = "check")
            }
            return@Modal
        }
        Div({ classes("row", "between") }) {
            Div { B { Text("Grifos de hoje") }; Div({ classes("xs", "muted") }) { Text("${index + 1} de ${cards.size}") } }
            IconButton("close", "Fechar") { onClose() }
        }
        Div({ classes("nb-review-bar") }) { Div({ attr("style", "width:${(index + 1) * 100 / cards.size}%") }) }
        Div({ classes("nb-review-card", "mark-card") }) {
            Div({ classes("row"); attr("style", "gap:8px;flex-wrap:wrap") }) {
                Span({ classes("nb-kicker") }) { Text("GRIFO") }
                Span({ classes("small", "primary-ink", "strong") }) { Text(topicOf(card)) }
            }
            if (!shown) {
                P({ classes("muted") }) { Text("Você grifou um trecho deste tópico. Lembra o que ele dizia? Pense um pouco antes de ver.") }
                Div({ classes("mark-card-blur") }) { Markdown(card.quote, compact = true) }
                Btn("Mostrar o trecho", { shown = true }, style = "primary", icon = "visibility")
            } else {
                Div({ classes("nb-review-quote"); attr("style", "border-left-color:${MarkColors.of(card.color).hex}") }) { Markdown(card.quote, compact = true) }
                if (card.note.isNotBlank()) Div({ classes("small", "muted") }) { Text("Sua nota: " + NoteTags.split(card.note).second) }
                Div({ classes("row", "wrap"); attr("style", "gap:8px") }) {
                    Btn("Não lembrei", {
                        if (!Store.demo) Store.update { br.com.estudario.web.data.Actions.setTheoryMarkColor(it, card.id, 2) }
                        shown = false; index++
                    }, style = "outline", icon = "replay")
                    Btn("Lembrei", {
                        if (!Store.demo && card.color == 2) Store.update { br.com.estudario.web.data.Actions.setTheoryMarkColor(it, card.id, 1) }
                        remembered++; shown = false; index++
                    }, style = "primary", icon = "check")
                }
            }
        }
    }
}
