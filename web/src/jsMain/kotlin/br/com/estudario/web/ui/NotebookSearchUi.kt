package br.com.estudario.web.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import br.com.estudario.domain.NotebookSearch
import br.com.estudario.web.Route
import br.com.estudario.web.Router
import br.com.estudario.web.data.Queries
import br.com.estudario.web.data.Snapshot
import br.com.estudario.web.data.Store
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.dom.B
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H3
import org.jetbrains.compose.web.dom.Input
import org.jetbrains.compose.web.dom.Label
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text

/*
 * A busca geral do Caderno de estudo, no topo da entrada (igual ao app, NotebookSearchPanel.kt): a
 * pessoa escreve uma matéria, um tópico ou um pedaço do que guardou e acha. A regra (sem acento, com
 * pedaço de palavra, singular acha plural) é NotebookSearch, no módulo comum. Vazia, a tela de sempre.
 */

/** Para onde leva cada resultado. */
private sealed class SearchRef {
    data class Topic(val id: Long, val studied: Boolean, val hasMaterial: Boolean) : SearchRef()
    data class Saved(val page: Long, val topicId: Long? = null, val theoryId: Long? = null, val block: Int = 0, val quote: String = "") : SearchRef()
}

/** Texto sem as marcas de markdown mais comuns, para título e trecho. */
private fun plain(md: String) = md.replace(Regex("[*_`#>|]+"), " ").replace(Regex("\\s+"), " ").trim()

/** Tudo o que dá para achar: os tópicos (de todas as matérias da pessoa) e tudo o que ela guardou. */
private fun searchItems(data: Snapshot): List<NotebookSearch.Item<SearchRef>> {
    val subjectName = data.subjects.associate { it.id to it.name }
    val withTheory = data.theories.mapTo(hashSetOf()) { it.topicId }
    val topicById = data.topics.associateBy { it.id }
    val theoryTopic = data.theories.associate { it.id to it.topicId }
    val out = ArrayList<NotebookSearch.Item<SearchRef>>()
    Queries.leafTopics(data).forEach { t ->
        out += NotebookSearch.Item(NotebookSearch.Kind.TOPIC, t.title, subjectName[t.subjectId], ref = SearchRef.Topic(t.id, Queries.isStudied(t), t.id in withTheory))
    }
    savedByTopic(data).forEach { (page, s) ->
        val t = topicById[page]
        val where = listOfNotNull(t?.title ?: "Geral", t?.let { subjectName[it.subjectId] }).joinToString(" · ")
        s.marks.forEach { m ->
            val quote = plain(m.quote)
            out += NotebookSearch.Item(NotebookSearch.Kind.MARK, quote.take(90), where, "$quote ${m.note}", SearchRef.Saved(page, theoryTopic[m.theoryId], m.theoryId, m.blockIndex, m.quote))
        }
        s.notes.forEach { n -> out += NotebookSearch.Item(NotebookSearch.Kind.NOTE, plain(n.text).take(90), where, n.text, SearchRef.Saved(page)) }
        s.tips.forEach { x -> out += NotebookSearch.Item(NotebookSearch.Kind.TIP, plain(x.text).take(90), where, "${x.text} ${x.answer.orEmpty()}", SearchRef.Saved(page)) }
        s.traps.forEach { x -> out += NotebookSearch.Item(NotebookSearch.Kind.TRAP, plain(x.text).take(90), where, "${x.text} ${x.answer.orEmpty()}", SearchRef.Saved(page)) }
        s.cards.forEach { x -> out += NotebookSearch.Item(NotebookSearch.Kind.CARD, plain(x.text).take(90), where, "${x.text} ${x.answer.orEmpty()}", SearchRef.Saved(page)) }
        s.decks.forEach { d -> out += NotebookSearch.Item(NotebookSearch.Kind.DECK, d.title.ifBlank { "Flashcards" }, where, d.markdown, SearchRef.Saved(page)) }
        s.questions.forEach { q -> val text = plain(q.statement); out += NotebookSearch.Item(NotebookSearch.Kind.QUESTION, text.take(90), where, text, SearchRef.Saved(page)) }
    }
    return out
}

private fun kindLabel(k: NotebookSearch.Kind) = when (k) {
    NotebookSearch.Kind.TOPIC -> "Tópico"; NotebookSearch.Kind.MARK -> "Grifo"; NotebookSearch.Kind.NOTE -> "Anotação"
    NotebookSearch.Kind.TIP -> "Dica"; NotebookSearch.Kind.TRAP -> "Pegadinha"; NotebookSearch.Kind.CARD -> "Flashcard"
    NotebookSearch.Kind.DECK -> "Baralho"; NotebookSearch.Kind.QUESTION -> "Questão"
}

private fun kindIcon(k: NotebookSearch.Kind) = when (k) {
    NotebookSearch.Kind.TOPIC -> "menu_book"; NotebookSearch.Kind.MARK -> "format_ink_highlighter"; NotebookSearch.Kind.NOTE -> "edit_note"
    NotebookSearch.Kind.TIP -> "lightbulb"; NotebookSearch.Kind.TRAP -> "pest_control_rodent"
    NotebookSearch.Kind.CARD, NotebookSearch.Kind.DECK -> "style"; NotebookSearch.Kind.QUESTION -> "quiz"
}

/** O texto da busca (sobrevive a abrir um resultado e voltar). */
object NotebookQuery { var text by mutableStateOf("") }

/** Quantos de cada grupo aparecem antes do "Ver mais". */
private const val FIRST = 8

/** O campo de busca do topo do Caderno. */
@Composable
fun NotebookSearchBox(query: String, onQuery: (String) -> Unit) {
    Label(attrs = { classes("nbq") }) {
        Icon("search")
        Input(InputType.Search) {
            classes("nbq-input"); attr("placeholder", "Buscar no caderno: matéria, tópico, grifo ou anotação")
            attr("aria-label", "Buscar no caderno"); attr("autocomplete", "off")
            value(query); onInput { onQuery(it.value) }
        }
        if (query.isNotEmpty()) Button({ classes("nb-ic", "nbq-x"); attr("type", "button"); attr("aria-label", "Limpar a busca"); onClick { onQuery("") } }) { Icon("close") }
    }
}

/**
 * Os resultados, em três grupos: matérias e tópicos (clicar abre a página do caderno, ou o tópico para
 * estudar ou gerar o material), grifos e anotações, e questões/flashcards/dicas salvos (abrem a página
 * do tópico onde estão). Sem nada, uma mensagem e o caminho para o edital.
 */
@Composable
fun NotebookSearchResults(query: String) {
    val data = Store.data
    val items = remember(data) { searchItems(data) }
    val hits = remember(items, query) { NotebookSearch.search(query, items) }
    val groups = remember(hits) { NotebookSearch.grouped(hits) }
    var showAll by remember(query) { mutableStateOf(setOf<NotebookSearch.Group>()) }
    Div({ classes("nbq-results") }) {
        Span({ classes("nbq-count") }) { Text(if (hits.isEmpty()) "Nenhum resultado" else "${hits.size} ${if (hits.size == 1) "resultado" else "resultados"} para “${query.trim()}”") }
        if (hits.isEmpty()) {
            Div({ classes("nbq-empty") }) {
                Div({ classes("nbq-empty-top") }) { Icon("search_off"); H3 { Text("Não achei “${query.trim()}” no seu caderno") } }
                P { Text("Confira a grafia ou tente uma palavra só (por exemplo, \"crase\" ou \"licitação\"). Se a matéria ainda não está no seu edital, procure no edital ou crie uma matéria sua.") }
                Btn("Ir para o edital", { Router.go(Route.Edital) }, style = "outline", icon = "menu_book", kind = "study")
            }
            return@Div
        }
        val topicsOnly = groups.size == 1 && groups[0].first == NotebookSearch.Group.TOPICS
        val noMaterial = groups.firstOrNull { it.first == NotebookSearch.Group.TOPICS }?.second.orEmpty().count { (it.item.ref as SearchRef.Topic).let { r -> !r.hasMaterial && !r.studied } }
        if (topicsOnly && noMaterial > 0) Div({ classes("nbq-hint") }) {
            Icon("auto_awesome")
            P { Text("Nada guardado sobre isso ainda. ${if (noMaterial == 1) "O tópico abaixo ainda não tem material" else "Alguns tópicos abaixo ainda não têm material"}: clique em \"Gerar material\" para estudar.") }
        }
        groups.forEach { (group, list) ->
            key(group.name) {
                Div({ classes("nbq-group") }) {
                    Div({ classes("nbq-group-head") }) { B { Text(group.label) }; Span { Text("${list.size}") } }
                    val all = group in showAll
                    list.take(if (all) list.size else FIRST).forEachIndexed { i, hit -> key("${group.name}-$i-${hit.item.title}") { HitRow(hit) } }
                    if (!all && list.size > FIRST) Btn("Ver mais ${list.size - FIRST}", { showAll = showAll + group }, style = "outline", block = true)
                }
            }
        }
    }
}

@Composable
private fun HitRow(hit: NotebookSearch.Hit<SearchRef>) {
    val item = hit.item
    val ref = item.ref
    val open: () -> Unit = when (ref) {
        is SearchRef.Topic -> ({ Router.go(if (ref.studied) Route.NotebookPage(ref.id) else Route.Topic(ref.id)) })
        is SearchRef.Saved -> ({ Router.go(Route.NotebookPage(ref.page)) })
    }
    Div({ classes("nbq-hit") }) {
        Button({ classes("nbq-hit-main"); attr("type", "button"); onClick { open() } }) {
            Icon(kindIcon(item.kind))
            Span({ classes("grow", "nbq-hit-txt") }) {
                Span({ classes("nbq-hit-sub") }) { Text(if (item.kind == NotebookSearch.Kind.TOPIC) item.context ?: "Matéria" else "${kindLabel(item.kind)} · ${item.context.orEmpty()}") }
                B { Text(item.title) }
                hit.excerpt?.takeIf { it != item.title }?.let { Span({ classes("nbq-hit-ex") }) { Text(it) } }
            }
            Icon("chevron_right", plain = true)
        }
        when (ref) {
            is SearchRef.Topic -> Div({ classes("nbq-hit-actions") }) {
                when {
                    ref.studied -> Span({ classes("nbq-pill", "ok") }) { Text("No seu caderno") }
                    ref.hasMaterial -> Span({ classes("nbq-pill", "mid") }) { Text("Tem material, ainda não estudado") }
                    else -> Span({ classes("nbq-pill", "low") }) { Text("Sem material ainda") }
                }
                if (!ref.studied) Btn(if (ref.hasMaterial) "Estudar" else "Gerar material", { Router.go(Route.Topic(ref.id)) }, style = "primary", small = true, icon = if (ref.hasMaterial) "menu_book" else "auto_awesome", kind = "study")
            }
            is SearchRef.Saved -> if (ref.theoryId != null && ref.topicId != null) Div({ classes("nbq-hit-actions") }) {
                Btn("Abrir na teoria", { TheoryJump.go(ref.topicId, ref.theoryId, ref.block, ref.quote) }, style = "tonal", small = true, icon = "menu_book")
            }
        }
    }
}
