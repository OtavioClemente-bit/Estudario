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

    PageHead("Caderno de estudo", "Tudo o que você grifou, salvou e favoritou enquanto estudava")
    CompetitionTabs()
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
    when (tab) {
        "grifos" -> if (marks.isEmpty()) Empty("format_ink_highlighter", "Nenhum grifo ainda", "Na teoria de um tópico, selecione um trecho e grife para guardar aqui.")
        else ByTopic(marks.groupBy { theories[it.theoryId]?.topicId }) { mark ->
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
            ByTopic(favQuestions.groupBy { it.topicId }) { q ->
                Div({ classes("saved-item") }) {
                    P({ classes("clamp-3", "grow") }) { Inline(splitStatement(q.statement).let { it.command ?: it.base }.take(300)) }
                    IconButton("star", "Tirar dos salvos") { Store.update { Actions.toggleFavorite(it, q.id) } }
                }
            }
        }
        "flashcards" -> if (recallCards.isEmpty() && favDecks.isEmpty()) Empty("style", "Nenhum flashcard salvo", "Salve um baralho ou cartões no tópico para revisar aqui.")
        else ByTopic((favDecks.map { it.topicId to it as Any } + recallCards.map { it.topicId to it as Any }).groupBy({ it.first }, { it.second })) { item ->
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
        else ByTopic(tips.groupBy { it.topicId }) { tip -> SavedCard(tip.id, null, tip.text, tip.answer) }
        else -> NotesTab(notes, markNotes, { theories[it.theoryId]?.topicId }, topics.values.filter { t -> data.topics.none { it.parentTopicId == t.id } }.map { it.id to it.title })
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
    ByTopic(rows.groupBy({ it.first }, { it.second })) { row ->
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
