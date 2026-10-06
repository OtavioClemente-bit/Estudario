package br.com.estudario.web.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import br.com.estudario.web.Route
import br.com.estudario.web.Router
import br.com.estudario.web.data.Actions
import br.com.estudario.web.data.Queries
import br.com.estudario.web.data.Store
import br.com.estudario.web.data.Topic
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.attributes.placeholder
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H3
import org.jetbrains.compose.web.dom.Input
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text

@Composable
fun EditalScreen() {
    val data = Store.data
    val competitions = data.competitions
    var competitionId by remember { mutableStateOf(Queries.primaryCompetition(data)?.id) }
    var filter by remember { mutableStateOf("") }
    var open by remember { mutableStateOf(setOf<Long>()) }
    val subjects = Queries.subjectsOf(data, competitionId)
    val allLeaves = Queries.leafTopics(data, subjects.mapTo(hashSetOf()) { it.id })
    val studiedAll = allLeaves.count(Queries::isStudied)

    PageHead("Edital", competitions.firstOrNull { it.id == competitionId }?.name) {
        if (competitions.size > 1) {
            org.jetbrains.compose.web.dom.Select({
                classes("input")
                onChange { event -> competitionId = event.value?.toLongOrNull() }
            }) {
                competitions.forEach { c ->
                    org.jetbrains.compose.web.dom.Option(c.id.toString(), { if (c.id == competitionId) attr("selected", "") }) { Text(c.name) }
                }
            }
        }
    }

    Div({ classes("grid", "cols-3") }) {
        Card(extra = "flat") { Stat("${percent(studiedAll, allLeaves.size)}%", "do edital estudado") }
        Card(extra = "flat") { Stat("$studiedAll de ${allLeaves.size}", "tópicos estudados") }
        Card(extra = "flat") { Stat("${subjects.size}", if (subjects.size == 1) "matéria" else "matérias") }
    }

    Input(InputType.Search) {
        classes("input")
        placeholder("Procurar tópico…")
        value(filter)
        onInput { filter = it.value }
    }

    if (subjects.isEmpty()) {
        Card { Empty("menu_book", "Edital vazio", "Cadastre o edital no app: as matérias e tópicos aparecem aqui.") }
        return
    }

    val query = filter.trim().lowercase()
    Div({ classes("stack") }) {
        subjects.forEach { subject -> androidx.compose.runtime.key(subject.id) {
            val topics = data.topics.filter { it.subjectId == subject.id }.sortedBy { it.position }
            val leaves = Queries.leafTopics(data, setOf(subject.id))
            val studied = leaves.count(Queries::isStudied)
            val matching = if (query.isEmpty()) topics else topics.filter { it.title.lowercase().contains(query) }
            if (query.isNotEmpty() && matching.isEmpty()) return@forEach
            val expanded = subject.id in open || query.isNotEmpty()
            Div({ classes("subject") }) {
                Button({
                    classes("subject-head")
                    attr("aria-expanded", expanded.toString())
                    onClick { open = if (subject.id in open) open - subject.id else open + subject.id }
                }) {
                    Div({ attr("style", "flex:1;min-width:0") }) {
                        H3 { Text(subject.name) }
                        Div({ classes("row"); attr("style", "margin-top:8px") }) {
                            Div({ attr("style", "flex:1;max-width:280px") }) { ProgressBar(if (leaves.isEmpty()) 0.0 else studied.toDouble() / leaves.size) }
                            Span({ classes("small", "muted", "strong") }) { Text("$studied/${leaves.size}") }
                        }
                    }
                    Icon(if (expanded) "expand_less" else "expand_more")
                }
                if (expanded) {
                    Div({ classes("subject-body") }) {
                        val roots = matching.filter { it.parentTopicId == null || query.isNotEmpty() }
                        roots.forEach { topic -> androidx.compose.runtime.key(topic.id) {
                            TopicRow(topic, child = false)
                            if (query.isEmpty()) topics.filter { it.parentTopicId == topic.id }.forEach { androidx.compose.runtime.key(it.id) { TopicRow(it, child = true) } }
                        } }
                    }
                }
            }
        } }
    }
}

@Composable
private fun TopicRow(topic: Topic, child: Boolean) {
    val data = Store.data
    val hasMaterial = data.theories.any { it.topicId == topic.id } || data.summaries.any { it.topicId == topic.id }
    val questions = data.questions.count { it.topicId == topic.id && !it.hidden && it.simulationId == null }
    Div({
        classes(*listOfNotNull("topic-row", if (child) "child" else null).toTypedArray())
        attr("role", "link")
        attr("tabindex", "0")
        onClick { Router.go(Route.Topic(topic.id)) }
    }) {
        Span({ classes("dot", topic.status) ; attr("title", Queries.topicStatusLabel(topic.status)) })
        Span({ attr("style", "flex:1;min-width:0") }) { Text(topic.title) }
        if (hasMaterial) Icon("article", extraClass = "muted")
        if (questions > 0) Chip("$questions questões")
    }
}

@Composable
fun TopicScreen(topicId: Long) {
    val data = Store.data
    val topic = data.topics.firstOrNull { it.id == topicId }
    if (topic == null) {
        Card { Empty("search_off", "Tópico não encontrado", "Ele pode ter sido apagado no app.", action = { Btn("Voltar ao edital", { Router.go(Route.Edital) }) }) }
        return
    }
    val subject = data.subjects.firstOrNull { it.id == topic.subjectId }
    val theories = data.theories.filter { it.topicId == topicId }
    val summaries = data.summaries.filter { it.topicId == topicId }
    val snippets = data.snippets.filter { it.topicId == topicId }.sortedBy { it.position }
    val questions = data.questions.filter { it.topicId == topicId && !it.hidden && it.simulationId == null }
    val tabs = buildList {
        theories.forEach { add("t${it.id}" to it.title.ifBlank { "Teoria" }) }
        summaries.forEach { add("s${it.id}" to it.title.ifBlank { "Resumo" }) }
        if (snippets.isNotEmpty()) add("dicas" to "Dicas e pegadinhas")
    }
    var generating by remember(topicId) { mutableStateOf(false) }
    var tab by remember(topicId) { mutableStateOf(tabs.firstOrNull()?.first) }
    val studied = Queries.isStudied(topic)

    Div({ classes("row") }) { Btn("Edital", { Router.go(Route.Edital) }, style = "ghost", small = true, icon = "arrow_back") }
    PageHead(topic.title, subject?.name) {
        Chip(Queries.topicStatusLabel(topic.status), when (topic.status) { "DOMINADO" -> "green"; "EM_ESTUDO" -> "amber"; "NAO_ESTUDADO" -> null; else -> "primary" })
        if (questions.isNotEmpty()) Btn("Resolver ${questions.size} questões", { Router.go(Route.Quiz("topico-$topicId")) }, style = "tonal", icon = "quiz")
        if (!Store.demo && tabs.isNotEmpty()) Btn("Gerar de novo", { generating = true }, style = "outline", icon = "auto_awesome")
        if (!studied) Btn("Marcar como estudado", { Store.update { Actions.completeStudy(it, topicId) } }, icon = "check")
        else Btn("Desmarcar estudado", { Store.update { Actions.unmarkStudied(it, topicId) } }, style = "outline")
    }
    if (topic.description.isNotBlank()) P({ classes("muted") }) { Text(topic.description) }
    if (generating) GenerateDialog(topicId) { generating = false }
    if (tabs.isEmpty()) {
        Card { Empty("article", "Sem material ainda", "Gere teoria, resumo, flashcards e questões deste tópico com a IA do Estudário.", action = { if (!Store.demo) Btn("Gerar material com IA", { generating = true }, icon = "auto_awesome") }) }
        return
    }
    if (tabs.size > 1) {
        Div({ classes("segmented"); attr("style", "flex-wrap:wrap;align-self:flex-start") }) {
            tabs.forEach { (key, label) -> Button({ classes(*listOfNotNull(if (tab == key) "on" else null).toTypedArray()); onClick { tab = key } }) { Text(label) } }
        }
    }
    Card {
        when {
            tab == "dicas" -> Div({ classes("stack") }) {
                snippets.forEach { snippet ->
                    Div({ classes("card", "soft") }) {
                        Chip(when (snippet.kind) { "BIZU" -> "Bizu"; "PEGADINHA" -> "Pegadinha"; else -> "Recuperação" }, if (snippet.kind == "PEGADINHA") "red" else "primary")
                        P({ attr("style", "margin-top:8px") }) { Inline(snippet.text) }
                        snippet.answer?.let { P({ classes("muted", "small"); attr("style", "margin-top:6px") }) { Inline(it) } }
                    }
                }
            }
            tab?.startsWith("t") == true -> theories.firstOrNull { "t${it.id}" == tab }?.let { Markdown(it.markdown) }
            tab?.startsWith("s") == true -> summaries.firstOrNull { "s${it.id}" == tab }?.let { Markdown(it.markdown) }
        }
    }
}


@Composable
private fun GenerateDialog(topicId: Long, onClose: () -> Unit) {
    val scope = androidx.compose.runtime.rememberCoroutineScope()
    var depth by remember { mutableStateOf("BOOK") }
    var count by remember { mutableStateOf(10) }
    var progress by remember { mutableStateOf<br.com.estudario.web.data.AiContent.Progress?>(null) }
    val running = progress != null && progress !is br.com.estudario.web.data.AiContent.Progress.Failed && progress !is br.com.estudario.web.data.AiContent.Progress.Done
    Modal(onDismiss = { if (!running) onClose() }) {
        org.jetbrains.compose.web.dom.H2 { Text("Gerar material com IA") }
        when (val current = progress) {
            null, is br.com.estudario.web.data.AiContent.Progress.Failed -> {
                P({ classes("muted") }) { Text("Teoria, resumo, flashcards, dicas, perguntas de memorização e questões, no estilo do seu concurso. Leva alguns minutos.") }
                Div({ classes("field") }) {
                    org.jetbrains.compose.web.dom.Label { Text("Profundidade da teoria") }
                    Div({ classes("segmented") }) {
                        listOf("ESSENTIAL" to "Essencial", "DEEP" to "Aprofundada", "BOOK" to "Livro completo").forEach { (key, label) ->
                            Button({ classes(*listOfNotNull(if (depth == key) "on" else null).toTypedArray()); onClick { depth = key } }) { Text(label) }
                        }
                    }
                }
                Div({ classes("field") }) {
                    org.jetbrains.compose.web.dom.Label { Text("Questões") }
                    Div({ classes("segmented") }) {
                        listOf(5, 10, 15, 20).forEach { n -> Button({ classes(*listOfNotNull(if (count == n) "on" else null).toTypedArray()); onClick { count = n } }) { Text("$n") } }
                    }
                }
                if (current is br.com.estudario.web.data.AiContent.Progress.Failed) Div({ classes("banner", "error") }) { Text(current.message) }
                Div({ classes("row"); attr("style", "justify-content:flex-end") }) {
                    Btn("Cancelar", onClose, style = "ghost")
                    Btn("Gerar", {
                        progress = br.com.estudario.web.data.AiContent.Progress.Checking
                        scope.launch {
                            val result = br.com.estudario.web.data.AiContent.generate(Store.data, topicId, br.com.estudario.web.data.AiContent.Options(depth = depth, questionCount = count)) { progress = it }
                            if (result is br.com.estudario.web.data.AiContent.Progress.Done) {
                                Store.update { br.com.estudario.web.data.AiContent.apply(it, topicId, result.proposal) }
                            }
                            progress = result
                        }
                    }, icon = "auto_awesome")
                }
            }
            is br.com.estudario.web.data.AiContent.Progress.Done -> {
                Div({ classes("banner", "info") }) { Icon("check_circle", filled = true); Text("Material pronto e salvo neste tópico.") }
                Div({ classes("row"); attr("style", "justify-content:flex-end") }) { Btn("Ver material", onClose) }
            }
            else -> Div({ classes("stack"); attr("style", "align-items:center;padding:16px 0") }) {
                Spinner()
                P({ classes("muted") }) {
                    Text(
                        when (current) {
                            br.com.estudario.web.data.AiContent.Progress.Checking -> "Verificando a segurança…"
                            br.com.estudario.web.data.AiContent.Progress.Sending -> "Enviando o pedido…"
                            else -> "A IA está escrevendo o material. Pode levar alguns minutos; deixe esta página aberta."
                        },
                    )
                }
            }
        }
    }
}
