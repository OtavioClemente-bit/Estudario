package br.com.estudario.web.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import br.com.estudario.web.Route
import br.com.estudario.web.Router
import br.com.estudario.web.data.Snapshot
import br.com.estudario.web.data.Store
import kotlinx.browser.window
import org.jetbrains.compose.web.dom.B
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text
import org.w3c.dom.events.KeyboardEvent
import kotlin.random.Random

data class FlashCard(val topicId: Long, val front: String, val back: String)

/**
 * Os mesmos cartões do app: a "Revisão rápida" do tópico guarda um cartão por seção (### frente,
 * verso no corpo) e as perguntas de memorização viram cartões de pergunta e resposta. [saved]: só o
 * que está no Caderno (baralhos marcados e perguntas com estrela), como o praticar do Caderno do app.
 */
fun flashcardsOf(data: Snapshot, topicId: Long?, saved: Boolean = false, deckId: Long? = null): List<FlashCard> {
    val summaries = data.summaries.filter { it.kind == "RAPIDO" && (deckId == null || it.id == deckId) && (topicId == null || it.topicId == topicId) && (!saved || it.favorite) }
    val fromDecks = summaries.flatMap { summary ->
        summary.markdown.split(Regex("^### ", RegexOption.MULTILINE)).drop(1).mapNotNull { section ->
            val front = section.substringBefore('\n').trim()
            val back = section.substringAfter('\n', "").trim()
            if (front.isBlank() || back.isBlank()) null else FlashCard(summary.topicId, front, back)
        }
    }
    val fromRecall = data.snippets.filter { it.kind == "RECUPERACAO" && !it.answer.isNullOrBlank() && (topicId == null || it.topicId == topicId) && (!saved || it.favorite) }
        .map { FlashCard(it.topicId, it.text, it.answer!!) }
    return if (deckId != null) fromDecks else fromDecks + fromRecall
}

@Composable
fun FlashcardsScreen(topicId: Long?, saved: Boolean = false, deckId: Long? = null) {
    val data = Store.data
    val all = remember(topicId, saved, deckId, data) { flashcardsOf(data, topicId, saved, deckId) }
    val deckTitle = deckId?.let { id -> data.summaries.firstOrNull { it.id == id } }?.let { d -> d.title.ifBlank { null } ?: data.topics.firstOrNull { it.id == d.topicId }?.title }
    val topics = data.topics.associateBy { it.id }
    var deck by remember(topicId, saved, deckId) { mutableStateOf(all.shuffled(Random(topicId ?: 7))) }
    var index by remember(topicId, saved, deckId) { mutableStateOf(0) }
    var flipped by remember(topicId, saved, deckId) { mutableStateOf(false) }
    var knew by remember(topicId, saved, deckId) { mutableStateOf(0) }
    var again by remember(topicId, saved, deckId) { mutableStateOf(listOf<FlashCard>()) }

    Div({ classes("row") }) {
        when {
            saved -> Btn("Caderno", { Router.go(Route.Notebook) }, style = "ghost", small = true, icon = "arrow_back")
            topicId != null -> Btn("Tópico", { Router.go(Route.Topic(topicId)) }, style = "ghost", small = true, icon = "arrow_back")
            else -> Btn("Treinar", { Router.go(Route.Train) }, style = "ghost", small = true, icon = "arrow_back")
        }
    }
    PageHead(
        if (saved) "Flashcards do Caderno" else "Flashcards",
        if (saved) (deckTitle?.let { "Baralho: $it" } ?: topicId?.let { topics[it]?.title }?.let { "Os cartões que você guardou de $it" } ?: "Os cartões que você guardou")
        else topicId?.let { topics[it]?.title } ?: "Revisão rápida de todos os tópicos",
    )

    if (all.isEmpty()) {
        if (saved) Card { Empty("style", "Nada guardado aqui ainda", "Na revisão rápida de um tópico, guarde o baralho ou as perguntas de memorização com a estrela: eles aparecem no Caderno e dá para praticar só eles.", action = { Btn("Voltar ao Caderno", { Router.go(Route.Notebook) }, icon = "arrow_back") }) }
        else Card { Empty("style", "Nenhum flashcard ainda", "Estude a matéria de um tópico: a revisão rápida dele vira um baralho aqui.", action = { Btn("Ir para o Edital", { Router.go(Route.Edital) }, icon = "checklist") }) }
        return
    }

    if (index >= deck.size) {
        Card(extra = "pad-lg") {
            Div({ classes("stack"); attr("style", "align-items:center;text-align:center;padding:12px 0") }) {
                Ring(if (deck.isEmpty()) 1.0 else knew.toDouble() / deck.size) { B { Text("$knew/${deck.size}") }; Span { Text("lembrei") } }
                org.jetbrains.compose.web.dom.H2 { Text(if (again.isEmpty()) "Baralho dominado!" else "Fim da rodada") }
                P({ classes("muted") }) { Text(if (again.isEmpty()) "Você lembrou de todos os cartões." else "${again.size} cartão(ões) para ver de novo.") }
                Div({ classes("row", "wrap"); attr("style", "justify-content:center") }) {
                    if (again.isNotEmpty()) Btn("Rever os que errei", { deck = again.shuffled(); again = emptyList(); index = 0; knew = 0; flipped = false }, icon = "replay")
                    Btn("Embaralhar tudo", { deck = all.shuffled(); again = emptyList(); index = 0; knew = 0; flipped = false }, style = "outline", icon = "shuffle")
                }
            }
        }
        return
    }

    HoldCelebrations()
    val card = deck[index]
    fun answer(remembered: Boolean) {
        som(if (remembered) "lembrei" else "nao_lembrei")
        if (remembered) knew++ else again = again + card
        flipped = false
        index++
    }
    val latestAnswer = androidx.compose.runtime.rememberUpdatedState(::answer)
    val latestFlipped = androidx.compose.runtime.rememberUpdatedState(flipped)
    DisposableEffect(topicId) {
        val listener: (org.w3c.dom.events.Event) -> Unit = { event ->
            when ((event as KeyboardEvent).key) {
                " ", "Enter" -> { event.preventDefault(); flipped = !latestFlipped.value }
                "ArrowRight", "1" -> if (latestFlipped.value) latestAnswer.value(true)
                "ArrowLeft", "2" -> if (latestFlipped.value) latestAnswer.value(false)
            }
        }
        window.addEventListener("keydown", listener)
        onDispose { window.removeEventListener("keydown", listener) }
    }

    Div({ classes("row", "between") }) {
        Span({ classes("strong", "muted") }) { Text("Cartão ${index + 1} de ${deck.size}") }
        if (topicId == null) topics[card.topicId]?.let { Chip(it.title, "primary") }
    }
    ProgressBar(index.toDouble() / deck.size)
    Div({ classes(*listOfNotNull("flash", if (flipped) "flipped" else null).toTypedArray()); onClick { flipped = !flipped } }) {
        Div({ classes("flash-inner") }) {
            Div({ classes("flash-face") }) {
                Span({ classes("eyebrow") }) { Text("Pergunta") }
                Div({ classes("q") }) { Inline(card.front) }
                Span({ classes("xs", "faint") }) { Text("Clique ou aperte espaço para virar") }
            }
            Div({ classes("flash-face", "back") }) {
                Span({ classes("eyebrow") }) { Text("Resposta") }
                Div({ classes("a") }) { Markdown(card.back) }
            }
        }
    }
    if (flipped) Div({ classes("grid", "cols-2") }) {
        Btn("Não lembrei", { answer(false) }, style = "outline", icon = "close", block = true)
        Btn("Lembrei", { answer(true) }, icon = "check", block = true)
    } else Btn("Virar cartão", { flipped = true }, style = "tonal", icon = "flip", block = true)
}

