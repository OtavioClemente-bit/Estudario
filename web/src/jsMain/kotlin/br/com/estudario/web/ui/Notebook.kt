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
    val topics = data.topics.associateBy { it.id }
    val theories = data.theories.associateBy { it.id }
    val marks = data.theoryMarks.sortedByDescending { it.createdAt }
    val favQuestions = data.questions.filter { it.favorite && !it.hidden }
    val recallCards = data.snippets.filter { it.kind == "RECUPERACAO" && it.favorite }
    val favDecks = data.summaries.filter { it.kind == "RAPIDO" && it.favorite }
    val tips = data.snippets.filter { it.favorite && it.kind != "RECUPERACAO" }
    val notes = data.notes.sortedByDescending { it.createdAt }
    val counts = mapOf("grifos" to marks.size, "questoes" to favQuestions.size, "flashcards" to recallCards.size + favDecks.size, "dicas" to tips.size, "notas" to notes.size)

    PageHead("Caderno de estudo", "Tudo o que você grifou, salvou e favoritou enquanto estudava")
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
            Div({ classes("saved-item") }) {
                P({ attr("style", "border-left:4px solid #f2b33d;padding-left:12px") }) { Text(mark.quote) }
                if (mark.note.isNotBlank()) P({ classes("small", "muted") }) { Text(mark.note) }
                IconButton("delete", "Apagar grifo") { Store.update { Actions.deleteTheoryMark(it, mark.id) } }
            }
        }
        "questoes" -> if (favQuestions.isEmpty()) Empty("bookmark", "Nenhuma questão salva", "Toque na estrela de uma questão para guardá-la e treinar depois.") else {
            Btn("Treinar as ${favQuestions.size} questões salvas", { Router.go(Route.Quiz("m-favorites~n-200")) }, style = "primary", icon = "play_arrow")
            ByTopic(favQuestions.groupBy { it.topicId }) { q ->
                Div({ classes("saved-item") }) {
                    P({ classes("clamp-3") }) { Text(splitStatement(q.statement).let { it.command ?: it.base }.take(300)) }
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
        else -> NotesTab(notes.map { Triple(it.id, it.topicId, it.text) }, topics.values.filter { t -> data.topics.none { it.parentTopicId == t.id } }.map { it.id to it.title })
    }
}

@Composable
private fun SavedCard(id: Long, topic: String?, text: String, answer: String?) {
    var open by remember { mutableStateOf(false) }
    Card {
        Div({ classes("row", "between") }) {
            if (topic != null) Chip(topic, icon = "lightbulb") else Span {}
            IconButton("star", "Tirar dos salvos") { Store.update { Actions.toggleSnippetFavorite(it, id) } }
        }
        P({ attr("style", "margin-top:8px") }) { Text(text) }
        if (!answer.isNullOrBlank()) {
            if (open) P({ classes("small"); attr("style", "background:var(--soft);padding:10px 12px;border-radius:12px") }) { Text(answer) }
            else Btn("Ver resposta", { open = true }, style = "ghost", small = true, icon = "visibility")
        }
    }
}

@Composable
private fun NotesTab(notes: List<Triple<Long, Long?, String>>, topics: List<Pair<Long, String>>) {
    var editing by remember { mutableStateOf<Long?>(null) }
    var text by remember { mutableStateOf("") }
    var topicId by remember { mutableStateOf<Long?>(null) }
    val names = topics.toMap()
    Card {
        CardHead(if (editing == null) "Nova anotação" else "Editar anotação")
        Select({
            classes("input")
            onChange { topicId = it.value?.toLongOrNull() }
        }) {
            Option("", { if (topicId == null) selected() }) { Text("Geral (sem tópico)") }
            topics.forEach { (id, title) -> Option("$id", { if (topicId == id) selected() }) { Text(title) } }
        }
        TextArea(text, {
            classes("input"); attr("rows", "4"); attr("placeholder", "Escreva o que quer lembrar…")
            attr("style", "margin-top:8px;width:100%")
            onInput { text = it.value }
        })
        Div({ classes("row"); attr("style", "margin-top:8px") }) {
            Btn("Salvar", {
                val id = editing ?: 0L
                val value = text.trim()
                Store.update { Actions.saveNote(it, id, topicId, value) }
                editing = null; text = ""; topicId = null
                Toast.show("Anotação salva")
            }, style = "primary", icon = "save", enabled = text.isNotBlank())
            if (editing != null) Btn("Cancelar", { editing = null; text = ""; topicId = null }, style = "ghost")
        }
    }
    if (notes.isEmpty()) Empty("edit_note", "Nenhuma anotação", "Suas anotações livres aparecem aqui.")
    notes.forEach { (id, topic, body) ->
        key(id) {
            Card {
                Div({ classes("row", "between") }) {
                    Chip(topic?.let { names[it] } ?: "Geral", icon = "edit_note")
                    Div({ classes("row") }) {
                        IconButton("edit", "Editar") { editing = id; text = body; topicId = topic }
                        IconButton("delete", "Apagar") { Store.update { Actions.deleteNote(it, id) } }
                    }
                }
                P({ attr("style", "white-space:pre-wrap;margin-top:8px") }) { Text(body) }
            }
        }
    }
}

/** Agrupa por tópico, com o nome da matéria, para o Caderno não virar uma lista solta. */
@Composable
fun <T> ByTopic(groups: Map<*, List<T>>, item: @Composable (T) -> Unit) {
    val data = Store.data
    val topics = data.topics.associateBy { it.id }
    val subjects = data.subjects.associateBy { it.id }
    Div({ classes("stack") }) {
        groups.entries.map { (it.key as Long?) to it.value }.sortedBy { e -> e.first?.let { topics[it]?.title } ?: "~" }.forEach { (topicId, list) ->
            key(topicId ?: -1L) {
                val topic = topicId?.let { topics[it] }
                Div({ classes("topic-group") }) {
                    Div({ classes("topic-group-head") }) {
                        Div({ classes("grow"); attr("style", "min-width:0") }) {
                            Div({ classes("crumb") }) { Text(topic?.let { subjects[it.subjectId]?.name }?.uppercase() ?: "GERAL") }
                            B { Text(topic?.title ?: "Sem tópico") }
                        }
                        Span({ classes("count-pill") }) { Text("${list.size}") }
                        if (topic != null) IconButton("arrow_forward", "Abrir tópico") { Router.go(Route.Topic(topic.id)) }
                    }
                    Div({ classes("topic-group-body") }) { list.forEach { item(it) } }
                }
            }
        }
    }
}
