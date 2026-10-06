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
    var generatingFor by remember { mutableStateOf<Long?>(null) }
    generatingFor?.let { id -> GenerateDialog(id) { generatingFor = null } }
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
                        val withContent = topics.count { t -> data.theories.any { it.topicId == t.id } || data.summaries.any { it.topicId == t.id } }
                        Div({ classes("small", "muted"); attr("style", "margin-top:4px") }) { Text("$withContent/${topics.size} com material · ${topics.count(Queries::isStudied)} estudados") }
                        Div({ classes("row"); attr("style", "margin-top:8px") }) {
                            Div({ attr("style", "flex:1;max-width:280px") }) { ProgressBar(if (topics.isEmpty()) 0.0 else withContent.toDouble() / topics.size) }
                            Span({ classes("small", "muted", "strong") }) { Text("$studied/${leaves.size} estudados") }
                        }
                    }
                    Icon(if (expanded) "expand_less" else "expand_more")
                }
                if (expanded) {
                    Div({ classes("subject-body") }) {
                        val roots = matching.filter { it.parentTopicId == null || query.isNotEmpty() }
                        roots.forEach { topic -> androidx.compose.runtime.key(topic.id) {
                            if (query.isEmpty()) TopicTree(topic, topics, 0) { generatingFor = it } else TopicRow(topic, 0, 0) { generatingFor = it }
                        } }
                    }
                }
            }
        } }
    }
}

@Composable
private fun TopicTree(topic: Topic, all: List<Topic>, depth: Int, onGenerate: (Long) -> Unit) {
    val children = all.filter { it.parentTopicId == topic.id }.sortedBy { it.position }
    TopicRow(topic, depth, children.size, onGenerate)
    children.forEach { child -> androidx.compose.runtime.key(child.id) { TopicTree(child, all, depth + 1, onGenerate) } }
}

/** Linha do edital como no app: tópico principal e, recuados, os subtópicos gerados. */
@Composable
private fun TopicRow(topic: Topic, depth: Int, childCount: Int, onGenerate: (Long) -> Unit) {
    val data = Store.data
    val hasMaterial = data.theories.any { it.topicId == topic.id } || data.summaries.any { it.topicId == topic.id }
    val studied = Queries.isStudied(topic)
    Div({
        classes(*listOfNotNull("topic-row", if (depth > 0) "child" else null, if (childCount > 0) "parent" else null).toTypedArray())
        attr("style", "padding-left:${14 + depth * 22}px")
        attr("role", "link"); attr("tabindex", "0")
        onClick { Router.go(Route.Topic(topic.id)) }
    }) {
        Icon(if (studied) "check_circle" else if (depth > 0) "subdirectory_arrow_right" else "radio_button_unchecked", extraClass = if (studied) "ok-ink" else "faint")
        Div({ attr("style", "flex:1;min-width:0") }) {
            Div({ classes("topic-row-title") }) { Text(topic.title) }
            Div({ classes("xs", if (hasMaterial) "primary-ink" else "muted") }) {
                Text(if (childCount > 0) "Dividido em $childCount subtópicos · gere o material em cada um" else if (hasMaterial) "${Queries.topicStatusLabel(topic.status)} · material pronto" else Queries.topicStatusLabel(topic.status))
            }
        }
        if (!hasMaterial && childCount == 0 && !Store.demo) Button({ classes("gen-btn"); onClick { e -> e.stopPropagation(); onGenerate(topic.id) } }) { Folha("happy", 22); Text("Gerar") }
        Icon("chevron_right", extraClass = "faint")
    }
}
