package br.com.estudario.web.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import br.com.estudario.domain.PriorityLevel
import br.com.estudario.web.Route
import br.com.estudario.web.Router
import br.com.estudario.web.data.Queries
import br.com.estudario.web.data.Store
import br.com.estudario.web.data.Subject
import br.com.estudario.web.data.Topic
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.attributes.placeholder
import org.jetbrains.compose.web.dom.B
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H2
import org.jetbrains.compose.web.dom.H3
import org.jetbrains.compose.web.dom.Input
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text

/**
 * Meus concursos, como o EditalScreen do app: o concurso em destaque (matérias, tópicos, com
 * material, estudados), a busca, o filtro "Com material", a legenda de atenção e cada matéria com a
 * faixa de cor do quanto ela pede (peso na prova + a dificuldade que você disse ter).
 */
@Composable
fun EditalScreen() {
    val data = Store.data
    val competitions = data.competitions
    var competitionId by remember { mutableStateOf(Queries.primaryCompetition(data)?.id) }
    var filter by remember { mutableStateOf("") }
    var onlyWithContent by remember { mutableStateOf(false) }
    var open by remember { mutableStateOf(setOf<Long>()) }
    var generatingFor by remember { mutableStateOf<Long?>(null) }
    generatingFor?.let { id -> GenerateDialog(id) { generatingFor = null } }
    val competition = competitions.firstOrNull { it.id == competitionId }
    val subjects = Queries.subjectsOf(data, competitionId)
    val subjectIds = subjects.mapTo(hashSetOf()) { it.id }
    val allTopics = data.topics.filter { it.subjectId in subjectIds }
    val contentIds = buildSet {
        data.theories.forEach { add(it.topicId) }
        data.summaries.forEach { add(it.topicId) }
        data.snippets.forEach { add(it.topicId) }
        data.questions.forEach { add(it.topicId) }
    }
    val withContent = allTopics.count { it.id in contentIds }
    val studied = allTopics.count(Queries::isStudied)
    val compPriority = competitionPriority(data, competitionId)

    PageHead("Concursos", "Cada tópico vira material de estudo") {
        Btn("Novo concurso", { Store.startEmpty(); Router.go(Route.Setup) }, style = "tonal", small = true, icon = "add")
    }

    if (competitions.size > 1) Div({ classes("filter-chips") }) {
        competitions.forEach { c ->
            key(c.id) {
                Button({ classes(*listOfNotNull("fchip", if (c.id == competitionId) "on" else null).toTypedArray()); onClick { competitionId = c.id } }) {
                    if (c.primary) Icon("star")
                    Text(c.name)
                }
            }
        }
    }

    if (competition == null || subjects.isEmpty()) {
        Div({ classes("edital-hero") }) {
            H2 { Text("Comece pelo seu concurso") }
            P { Text("Escolha um edital do catálogo ou envie o PDF: o Estudário organiza as matérias e os tópicos. Depois, cada tópico vira teoria, flashcards e questões.") }
            Btn("Criar meu concurso", { Router.go(Route.Setup) }, style = "primary", icon = "add")
        }
        return
    }

    // Visão do concurso: quanto do edital já virou material e quanto já foi estudado.
    Div({ classes("edital-hero", "rise") }) {
        Div({ classes("row", "between"); attr("style", "align-items:flex-start;gap:16px") }) {
            H2({ classes("clamp-3") }) { Text(competition.name) }
            Folha(if (withContent == allTopics.size && allTopics.isNotEmpty()) "happy" else "idle", 72, "margin:-10px -6px -16px 0")
        }
        Div({ classes("eh-stats") }) {
            EditalStat("${subjects.size}", "matérias")
            EditalStat("${allTopics.size}", "tópicos")
            EditalStat("$withContent", "com material")
            EditalStat("$studied", "estudados")
        }
        Div({ classes("eh-bars") }) {
            Div({ classes("eh-bar") }) {
                DualFill(percent(withContent, allTopics.size), percent(studied, allTopics.size))
            }
            Div({ classes("row", "wrap", "xs"); attr("style", "gap:14px") }) {
                Span({ classes("legend") }) { Span({ classes("sw", "s") }); Text("estudado ${percent(studied, allTopics.size)}%") }
                Span({ classes("legend") }) { Span({ classes("sw", "m") }); Text("com material ${percent(withContent, allTopics.size)}%") }
            }
        }
        P({ classes("small") }) {
            Text(if (withContent < allTopics.size) "Abra um tópico e clique em Gerar: o Estudário escreve a teoria, os flashcards e as questões dele." else "Todo o edital tem material. Agora é estudar e treinar.")
        }
    }

    Div({ classes("row", "wrap"); attr("style", "gap:10px") }) {
        Div({ classes("search", "grow"); attr("style", "min-width:240px") }) {
            Icon("search", plain = true)
            Input(InputType.Search) {
                classes("input")
                placeholder("Buscar matéria ou tópico")
                value(filter)
                onInput { filter = it.value }
            }
        }
        Button({ classes(*listOfNotNull("fchip", if (onlyWithContent) "on" else null).toTypedArray()); onClick { onlyWithContent = !onlyWithContent } }) {
            Icon(if (onlyWithContent) "check" else "library_books", plain = onlyWithContent)
            Text("Com material")
        }
    }
    Div({ classes("attn-legend") }) {
        Attention.entries.forEach { a -> Span { Span({ classes("adot"); attr("style", "background:${a.color}") }); Text(a.short.replaceFirstChar(Char::uppercase)) } }
        Span({ classes("muted") }) { Text("· peso na prova + sua dificuldade") }
    }

    val query = filter.trim().lowercase()
    val visible = subjects.map { subject -> subject to data.topics.filter { it.subjectId == subject.id }.sortedBy { it.position } }
        .filter { (subject, topics) ->
            val matchesSearch = query.isEmpty() || subject.name.lowercase().contains(query) || topics.any { it.title.lowercase().contains(query) }
            val matchesContent = !onlyWithContent || topics.any { it.id in contentIds }
            matchesSearch && matchesContent
        }
    Div({ classes("stack") }) {
        visible.forEach { (subject, topics) ->
            key(subject.id) {
                SubjectCard(
                    subject, topics, contentIds,
                    expanded = subject.id in open || query.isNotEmpty(),
                    query = query, onlyWithContent = onlyWithContent,
                    parentPriority = compPriority,
                    onToggle = { open = if (subject.id in open) open - subject.id else open + subject.id },
                    onGenerate = { generatingFor = it },
                )
            }
        }
    }
    if (visible.isEmpty()) Card { Empty("search", "Nenhuma matéria encontrada", "Altere a pesquisa ou desligue o filtro de material.") }
}

@Composable
private fun EditalStat(value: String, label: String) {
    Div({ classes("eh-stat") }) { B { Text(value) }; Span { Text(label) } }
}

/** Uma matéria do edital: faixa de atenção, progresso de material e estudo, e os tópicos. */
@Composable
private fun SubjectCard(
    subject: Subject,
    topics: List<Topic>,
    contentIds: Set<Long>,
    expanded: Boolean,
    query: String,
    onlyWithContent: Boolean,
    parentPriority: PriorityLevel?,
    onToggle: () -> Unit,
    onGenerate: (Long) -> Unit,
) {
    val data = Store.data
    val difficulty = subjectDifficulty(data, subject.id)
    val priority = subjectPriority(subject, parentPriority)
    val attention = Attention.of(priority, difficulty)
    val withContent = topics.count { it.id in contentIds }
    val studied = topics.count(Queries::isStudied)
    val latest = androidx.compose.runtime.rememberUpdatedState(onToggle)
    fun hasContentBelow(id: Long): Boolean = id in contentIds || topics.filter { it.parentTopicId == id }.any { hasContentBelow(it.id) }
    Div({ classes(*listOfNotNull("subject", if (expanded) "open" else null).toTypedArray()); attr("style", "--attn:${attention.color}") }) {
        Button({ classes("subject-head"); attr("aria-expanded", expanded.toString()); onClick { latest.value() } }) {
            Div({ classes("grow"); attr("style", "min-width:0") }) {
                H3 { Text(subject.name) }
                Div({ classes("row", "wrap"); attr("style", "gap:8px;margin-top:6px") }) {
                    Span({ classes("attn-pill"); attr("title", "Prioridade ${priorityLabel(priority).lowercase()}${difficulty?.takeIf { it != "NORMAL" }?.let { " · dificuldade ${difficultyLabel(it)}" } ?: ""}") }) {
                        Span({ classes("adot") }); Text(attention.label)
                    }
                    Span({ classes("xs", "muted") }) { Text("$withContent/${topics.size} com material · $studied estudados") }
                }
                Div({ classes("subject-bar-track") }) {
                    DualFill(percent(withContent, topics.size), percent(studied, topics.size))
                }
            }
            Span({ classes("subject-pct") }) { B { Text("${percent(studied, topics.size)}%") }; Span({ classes("xs", "muted") }) { Text("estudado") } }
            Icon("expand_more", extraClass = "chev", plain = true)
        }
        if (expanded) Div({ classes("subject-body") }) {
            val visible: (Topic) -> Boolean = { t -> (!onlyWithContent || hasContentBelow(t.id)) && (query.isEmpty() || t.title.lowercase().contains(query) || subject.name.lowercase().contains(query)) }
            if (query.isEmpty()) {
                topics.filter { it.parentTopicId == null && visible(it) }.forEach { topic ->
                    key(topic.id) { TopicTree(topic, topics, 0, priority, difficulty, contentIds, visible, onGenerate) }
                }
            } else topics.filter(visible).forEach { topic ->
                key(topic.id) { TopicRow(topic, 0, 0, topicPriority(topic, priority), difficulty, topic.id in contentIds, onGenerate) }
            }
        }
    }
}

private fun difficultyLabel(code: String) = when (code) {
    "VERY_EASY" -> "muito fácil"; "EASY" -> "fácil"; "HARD" -> "difícil"; "VERY_HARD" -> "muito difícil"; else -> "normal"
}

@Composable
private fun TopicTree(topic: Topic, all: List<Topic>, depth: Int, parent: PriorityLevel, difficulty: String?, contentIds: Set<Long>, visible: (Topic) -> Boolean, onGenerate: (Long) -> Unit) {
    val children = all.filter { it.parentTopicId == topic.id }.sortedBy { it.position }
    val priority = topicPriority(topic, parent)
    TopicRow(topic, depth, children.size, priority, difficulty, topic.id in contentIds, onGenerate)
    children.filter(visible).forEach { child -> key(child.id) { TopicTree(child, all, depth + 1, priority, difficulty, contentIds, visible, onGenerate) } }
}

/** Linha do edital como no app: o estado, o pontinho de atenção, o título e o botão Gerar. */
@Composable
private fun TopicRow(topic: Topic, depth: Int, childCount: Int, priority: PriorityLevel, difficulty: String?, hasMaterial: Boolean, onGenerate: (Long) -> Unit) {
    val studied = Queries.isStudied(topic)
    val attention = Attention.of(priority, difficulty)
    Div({
        classes(*listOfNotNull("topic-row", if (depth > 0) "child" else null, if (childCount > 0) "parent" else null).toTypedArray())
        attr("style", "padding-left:${14 + depth * 22}px")
        attr("role", "link"); attr("tabindex", "0")
        onClick { Router.go(Route.Topic(topic.id)) }
    }) {
        Span({ classes("topic-state") }) {
            Icon(if (studied) "check_circle" else if (depth > 0) "subdirectory_arrow_right" else "radio_button_unchecked", extraClass = if (studied) null else "faint", plain = !studied)
            Span({ classes("adot", "corner"); attr("style", "background:${attention.color}"); attr("title", attention.label) })
        }
        Div({ attr("style", "flex:1;min-width:0") }) {
            Div({ classes("topic-row-title") }) { Text(topic.title) }
            Div({ classes("xs", if (hasMaterial) "primary-ink" else "muted") }) {
                Text(if (childCount > 0) "Dividido em $childCount subtópicos · gere o material em cada um" else if (hasMaterial) "${Queries.topicStatusLabel(topic.status)} · material pronto" else Queries.topicStatusLabel(topic.status))
            }
        }
        if (!hasMaterial && childCount == 0 && !Store.demo) Button({ classes("gen-btn"); onClick { e -> e.stopPropagation(); onGenerate(topic.id) } }) { Folha("happy", 22); Text("Gerar") }
        Icon("chevron_right", extraClass = "faint", plain = true)
    }
}

/** Duas faixas na mesma barra: a maior embaixo, para a menor nunca sumir atrás dela. */
@Composable
private fun DualFill(material: Int, studied: Int) {
    val fills = listOf("m" to material, "s" to studied).sortedByDescending { it.second }
    fills.forEach { (cls, value) -> Span({ classes(cls); attr("style", "width:$value%") }) }
}
