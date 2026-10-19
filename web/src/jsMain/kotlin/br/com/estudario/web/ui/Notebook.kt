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

private val notebookTabs = listOf(
    "grifos" to "Grifos",
    "questoes" to "Questões",
    "flashcards" to "Flashcards",
    "dicas" to "Dicas",
    "notas" to "Anotações",
)

/** Caderno de estudo do app: tudo o que a pessoa grifou, salvou ou favoritou enquanto estudava. */
@Composable
fun NotebookScreen() {
    val data = Store.data
    var tab by remember { mutableStateOf("grifos") }
    // Só o concurso em foco (aba no topo quando há mais de um).
    val scope = CompetitionFilter.topicIds(data)
    val topics = data.topics.filter { it.id in scope }.associateBy { it.id }
    val theories = data.theories.filter { it.topicId in scope }.associateBy { it.id }
    val marks = data.theoryMarks.filter { it.theoryId in theories }.sortedByDescending { it.createdAt }
    val favQuestions = data.questions.filter { it.favorite && !it.hidden && it.topicId in scope }
    val recallCards = data.snippets.filter { it.kind == "RECUPERACAO" && it.favorite && it.topicId in scope }
    val favDecks = data.summaries.filter { it.kind == "RAPIDO" && it.favorite && it.topicId in scope }
    val tips = data.snippets.filter { it.favorite && it.kind != "RECUPERACAO" && it.topicId in scope }
    val notes = data.notes.filter { it.topicId == null || it.topicId in scope }.sortedByDescending { it.createdAt }
    val markNotes = marks.filter { it.note.isNotBlank() }
    val counts = mapOf("grifos" to marks.size, "questoes" to favQuestions.size, "flashcards" to recallCards.size + favDecks.size, "dicas" to tips.size, "notas" to notes.size + markNotes.size)

    PageHead("Caderno de estudo", "Tudo o que você grifou, salvou e anotou, arrumado por matéria")
    CompetitionTabs()
    var reviewing by remember { mutableStateOf<Pair<String, List<NbCard>>?>(null) }
    reviewing?.let { (title, cards) -> NotebookReview(title, cards) { reviewing = null } }
    val total = marks.size + favQuestions.size + recallCards.size + favDecks.size + tips.size + notes.size + markNotes.size
    val subjectsWithItems = (marks.mapNotNull { theories[it.theoryId]?.topicId } + favQuestions.map { it.topicId } + tips.map { it.topicId } + notes.mapNotNull { it.topicId }).mapNotNull { topics[it]?.subjectId }.distinct().size
    Div({ classes("nb-hero") }) {
        Folha("happy", 64)
        Div({ classes("grow") }) {
            B { Text(if (total == 0) "Seu caderno está em branco" else "$total ${if (total == 1) "coisa guardada" else "coisas guardadas"}") }
            Div({ classes("small") }) { Text(if (total == 0) "Grife trechos, salve questões e escreva anotações: tudo vem para cá, arrumado por matéria." else "Em $subjectsWithItems ${if (subjectsWithItems == 1) "matéria" else "matérias"}. Abra uma matéria ou revise tudo um por um antes da prova.") }
        }
    }
    val tabIcons = mapOf("grifos" to "format_ink_highlighter", "questoes" to "quiz", "flashcards" to "style", "dicas" to "lightbulb", "notas" to "edit_note")
    Div({ classes("topic-tabs", "nb-tabs"); attr("role", "tablist") }) {
        notebookTabs.forEach { (id, label) ->
            Button({ classes(*listOfNotNull("ttab", if (tab == id) "on" else null).toTypedArray()); attr("role", "tab"); onClick { tab = id } }) {
                Icon(tabIcons[id] ?: "circle")
                Text(label)
                Span({ classes("count") }) { Text("${counts[id] ?: 0}") }
            }
        }
    }
    fun topicName(id: Long?) = id?.let { topics[it]?.title } ?: "Geral"
    fun markCard(m: br.com.estudario.web.data.TheoryMark, kicker: String) = theories[m.theoryId]?.topicId.let { t ->
        NbCard(kicker, topicName(t), m.quote, m.note.ifBlank { null }, t) { if (t != null) TheoryJump.go(t, m.theoryId, m.blockIndex, m.quote) }
    }
    fun reviewButton(label: String, cards: List<NbCard>) {
        if (cards.isNotEmpty()) reviewing = label to cards
    }
    when (tab) {
        "grifos" -> if (marks.isEmpty()) Empty("format_ink_highlighter", "Nenhum grifo ainda", "Na teoria de um tópico, selecione um trecho e grife para guardar aqui.")
        else BySubject(marks.groupBy { theories[it.theoryId]?.topicId }, review = { list, name -> reviewButton("$name · Grifos", list.map { markCard(it, "GRIFO") }) }, reviewAll = { list -> reviewButton("Caderno · Grifos", list.map { markCard(it, "GRIFO") }) }, reviewLabel = "Revisar os grifos um por um") { mark ->
            val topicId = theories[mark.theoryId]?.topicId
            Div({ classes("saved-item", "mark-item") }) {
                // Clicar leva para o trecho na teoria, já destacado (como no app).
                Div({
                    classes("mark-body", "grow")
                    if (topicId != null) {
                        attr("role", "link"); attr("tabindex", "0"); attr("title", "Abrir na teoria")
                        onClick { TheoryJump.go(topicId, mark.theoryId, mark.blockIndex, mark.quote) }
                    }
                }) {
                    Markdown(mark.quote, compact = true)
                    if (mark.note.isNotBlank()) P({ classes("small", "muted", "mark-note") }) { Icon("edit_note"); Text(mark.note) }
                    if (topicId != null) Span({ classes("mark-open") }) { Text("Abrir na teoria") }
                }
                IconButton("delete", "Apagar grifo") { Store.update { Actions.deleteTheoryMark(it, mark.id) } }
            }
        }
        "questoes" -> if (favQuestions.isEmpty()) Empty("bookmark", "Nenhuma questão salva", "Toque na estrela de uma questão para guardá-la e treinar depois.") else {
            Btn("Treinar as ${favQuestions.size} questões salvas", { Router.go(Route.Quiz("m-favorites~n-200")) }, style = "primary", icon = "play_arrow")
            BySubject(favQuestions.groupBy { it.topicId }) { q ->
                Div({ classes("saved-item") }) {
                    P({ classes("clamp-3", "grow") }) { Inline(splitStatement(q.statement).let { it.command ?: it.base }.take(300)) }
                    IconButton("star", "Tirar dos salvos") { Store.update { Actions.toggleFavorite(it, q.id) } }
                }
            }
        }
        "flashcards" -> if (recallCards.isEmpty() && favDecks.isEmpty()) Empty("style", "Nenhum flashcard salvo", "Salve um baralho ou cartões no tópico para revisar aqui.")
        else BySubject((favDecks.map { it.topicId to it as Any } + recallCards.map { it.topicId to it as Any }).groupBy({ it.first }, { it.second })) { item ->
            when (item) {
                is br.com.estudario.web.data.Summary -> Div({ classes("saved-item") }) {
                    Div({ classes("grow") }) { B { Text("Baralho · ${item.title.ifBlank { "Flashcards" }}") }; Div({ classes("small", "muted") }) { Text("${deckCards(item.markdown).size} cartões") } }
                    Btn("Revisar", { Router.go(Route.Flashcards(item.topicId)) }, style = "tonal", small = true, icon = "style")
                    IconButton("bookmark", "Tirar do Caderno") { Store.update { Actions.toggleSummaryFavorite(it, item.id) } }
                }
                is br.com.estudario.web.data.Snippet -> SavedCard(item.id, null, item.text, item.answer)
            }
        }
        "dicas" -> if (tips.isEmpty()) Empty("lightbulb", "Nenhuma dica salva", "Favorite dicas e pegadinhas no tópico para juntar tudo aqui.")
        else {
            fun tipCard(t: br.com.estudario.web.data.Snippet) = NbCard(if (t.kind == "PEGADINHA") "PEGADINHA" else "DICA", topicName(t.topicId), null, listOfNotNull(t.text, t.answer?.takeIf { it.isNotBlank() }?.let { "Resposta: $it" }).joinToString("\n\n"), t.topicId)
            BySubject(tips.groupBy { it.topicId }, review = { list, name -> reviewButton("$name · Dicas", list.map(::tipCard)) }, reviewAll = { list -> reviewButton("Caderno · Dicas", list.map(::tipCard)) }, reviewLabel = "Revisar as dicas uma por uma") { tip -> SavedCard(tip.id, null, tip.text, tip.answer) }
        }
        else -> NotesTab(notes, markNotes, { theories[it.theoryId]?.topicId }, topics.values.filter { t -> data.topics.none { it.parentTopicId == t.id } }.map { it.id to it.title }) { label, list ->
            reviewButton(label, list.map { row -> when (row) { is br.com.estudario.web.data.UserNote -> NbCard("ANOTAÇÃO", topicName(row.topicId), null, row.text, row.topicId); else -> markCard(row as br.com.estudario.web.data.TheoryMark, "NOTA NUM TRECHO") } })
        }
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
            is br.com.estudario.web.data.UserNote -> NotePaperCard(row.text, row.createdAt, onClick = { editing = row })
            is br.com.estudario.web.data.TheoryMark -> NotePaperCard(row.note, row.createdAt, onClick = { editingMark = row }, quote = row.quote) {
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
                        Span({ classes("nb-initial") }) { Text(name.trim().take(1).uppercase()) }
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
