package br.com.estudario.web.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import br.com.estudario.web.Route
import br.com.estudario.web.Router
import br.com.estudario.web.data.Actions
import br.com.estudario.web.data.Question
import br.com.estudario.web.data.Queries
import br.com.estudario.web.data.Snapshot
import br.com.estudario.web.data.Store
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H2
import org.jetbrains.compose.web.dom.H3
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text
import kotlin.js.Date
import kotlin.random.Random

@Composable
fun QuestionsScreen() {
    val data = Store.data
    val competition = Queries.primaryCompetition(data)
    val subjects = Queries.subjectsOf(data, competition?.id)
    val topicsById = data.topics.associateBy { it.id }
    val questions = Queries.visibleQuestions(data)
    val bySubject = questions.groupBy { topicsById[it.topicId]?.subjectId }
    val answered = questions.count { it.answerCount > 0 }
    val correct = questions.sumOf { it.correctCount }
    val total = questions.sumOf { it.answerCount }
    var count by remember { mutableStateOf(10) }

    PageHead("Questões", "${questions.size} questões no seu banco") {
        Btn("Treino rápido", { Router.go(Route.Quiz("rapido-$count")) }, icon = "bolt", enabled = questions.isNotEmpty())
    }

    Div({ classes("grid", "cols-3") }) {
        Card(extra = "flat") { Stat("$answered", "questões já respondidas") }
        Card(extra = "flat") { Stat(if (total == 0) "–" else "${percent(correct, total)}%", "de acerto no geral") }
        Card(extra = "flat") { Stat("${Queries.pendingErrors(data).size}", "no caderno de erros") }
    }

    Card {
        CardHead("Tamanho do treino") {
            Div({ classes("segmented") }) {
                listOf(5, 10, 20, 30).forEach { n -> Button({ classes(*listOfNotNull(if (count == n) "on" else null).toTypedArray()); onClick { count = n } }) { Text("$n") } }
            }
        }
        P({ classes("muted", "small") }) { Text("O treino rápido mistura questões que você ainda não fez com as que errou. Também dá para treinar por matéria abaixo ou por tópico no Edital.") }
    }

    if (questions.isEmpty()) {
        Card { Empty("quiz", "Banco vazio", "Gere ou importe questões no app: elas aparecem aqui para resolver no computador.") }
        return
    }

    Card {
        CardHead("Por matéria")
        Div({ classes("stack") }) {
            subjects.forEach { subject ->
                val list = bySubject[subject.id].orEmpty()
                if (list.isEmpty()) return@forEach
                val subjectTotal = list.sumOf { it.answerCount }
                val subjectCorrect = list.sumOf { it.correctCount }
                Div({ classes("task") }) {
                    Div({ classes("task-body") }) {
                        Div({ classes("task-title") }) { Text(subject.name) }
                        Div({ classes("task-meta") }) {
                            Text("${list.size} questões · ${list.count { it.answerCount == 0 }} inéditas")
                            if (subjectTotal > 0) Text(" · ${percent(subjectCorrect, subjectTotal)}% de acerto")
                        }
                    }
                    Btn("Treinar", { Router.go(Route.Quiz("materia-${subject.id}-$count")) }, style = "tonal", small = true)
                }
            }
        }
    }
}

/** Escolhe as questões do treino conforme o endereço (#/questoes/treino/...). */
private fun pickQuestions(data: Snapshot, scope: String): List<Question> {
    val pool = Queries.visibleQuestions(data)
    val topicsById = data.topics.associateBy { it.id }
    val parts = scope.split('-')
    val seed = Date.now().toLong()
    val random = Random(seed)
    fun smart(list: List<Question>, n: Int): List<Question> {
        // Inéditas e erradas primeiro, depois as demais; dentro de cada grupo, ordem aleatória.
        val errorIds = data.errors.filter { it.status != "CORRIGIDO" }.mapTo(hashSetOf()) { it.questionId }
        val (priority, rest) = list.partition { it.answerCount == 0 || it.id in errorIds }
        return (priority.shuffled(random) + rest.shuffled(random)).take(n)
    }
    return when (parts.firstOrNull()) {
        "topico" -> pool.filter { it.topicId == parts.getOrNull(1)?.toLongOrNull() }.let { smart(it, it.size) }
        "materia" -> {
            val subjectId = parts.getOrNull(1)?.toLongOrNull()
            smart(pool.filter { topicsById[it.topicId]?.subjectId == subjectId }, parts.getOrNull(2)?.toIntOrNull() ?: 10)
        }
        "erros" -> {
            val ids = data.errors.filter { it.status != "CORRIGIDO" }.sortedBy { it.nextRetryAt ?: 0 }.map { it.questionId }
            ids.mapNotNull { id -> pool.firstOrNull { it.id == id } }.take(parts.getOrNull(1)?.toIntOrNull() ?: 20)
        }
        else -> smart(pool, parts.getOrNull(1)?.toIntOrNull() ?: 10)
    }
}

@Composable
fun QuizScreen(scope: String) {
    val initial = remember(scope) { pickQuestions(Store.data, scope) }
    val sessionId = remember(scope) { Actions.newSessionId() }
    val startedAt = remember(scope) { Date.now().toLong() }
    var index by remember(scope) { mutableStateOf(0) }
    var selected by remember(scope) { mutableStateOf<String?>(null) }
    var results by remember(scope) { mutableStateOf(listOf<Boolean>()) }
    var saved by remember(scope) { mutableStateOf(false) }

    if (initial.isEmpty()) {
        Card { Empty("quiz", "Nada para treinar aqui", "Não há questões neste recorte.", action = { Btn("Voltar", { Router.go(Route.Questions) }) }) }
        return
    }

    if (index >= initial.size) {
        val right = results.count { it }
        if (!saved) {
            saved = true
            val topicIds = initial.mapTo(hashSetOf()) { it.topicId }
            val subjectIds = Store.data.topics.filter { it.id in topicIds }.mapTo(hashSetOf()) { it.subjectId }
            val type = when { scope.startsWith("topico") -> "TOPIC"; scope.startsWith("materia") -> "SUBJECT"; scope.startsWith("erros") -> "ERROR_REVIEW"; else -> "QUICK" }
            Store.update { Actions.saveQuestionSession(it, type, startedAt, results.size, right, subjectIds, topicIds, sessionId) }
        }
        Card {
            Div({ classes("stack"); attr("style", "align-items:center;text-align:center;padding:24px 0") }) {
                Icon(if (right * 2 >= results.size) "celebration" else "fitness_center", extraClass = "big")
                H2 { Text("$right de ${results.size} certas (${percent(right, results.size)}%)") }
                P({ classes("muted") }) { Text(if (results.size - right > 0) "As que você errou foram para o caderno de erros e voltam em 3 dias." else "Gabaritou! Nenhuma foi para o caderno de erros.") }
                Div({ classes("row") }) {
                    Btn("Treinar de novo", { Router.go(Route.Questions) }, style = "outline")
                    Btn("Voltar ao início", { Router.go(Route.Home) })
                }
            }
        }
        return
    }

    val question = Store.data.questions.firstOrNull { it.id == initial[index].id } ?: initial[index]
    val topic = Store.data.topics.firstOrNull { it.id == question.topicId }
    val answered = selected != null
    val correctKey = question.options.firstOrNull { it.correct }?.key

    Div({ classes("row", "between", "wrap") }) {
        Btn("Sair", { Router.go(Route.Questions) }, style = "ghost", small = true, icon = "close")
        Span({ classes("strong", "muted") }) { Text("Questão ${index + 1} de ${initial.size}") }
        Span({ classes("small", "muted") }) { Text("${results.count { it }} certas") }
    }
    ProgressBar(index.toDouble() / initial.size)
    Div({ classes("grid", "main-side") }) {
        Card {
            Div({ classes("stack") }) {
                Div({ classes("row", "wrap") }) {
                    topic?.let { Chip(it.title) }
                    listOfNotNull(question.board, question.agency, question.year?.toString()).takeIf { it.isNotEmpty() }?.let { Chip(it.joinToString(" · ")) }
                }
                P({ classes("statement") }) { Text(question.statement) }
                Div({ classes("stack") }) {
                    question.options.sortedBy { it.position }.forEach { option -> androidx.compose.runtime.key(question.id, option.key) {
                        val state = when {
                            !answered -> null
                            option.key == correctKey -> "right"
                            option.key == selected -> "wrong"
                            else -> null
                        }
                        Button({
                            classes(*listOfNotNull("option", state).toTypedArray())
                            if (answered) attr("disabled", "")
                            onClick {
                                if (selected == null) {
                                    selected = option.key
                                    var ok = false
                                    Store.update { snapshot -> Actions.answer(snapshot, question.id, option.key, sessionId).also { ok = it.second }.first }
                                    results = results + ok
                                }
                            }
                        }) {
                            Span({ classes("key") }) { Text(option.key) }
                            Span { Inline(option.text) }
                        }
                    } }
                }
            }
        }
        Div({ classes("stack") }) {
            if (answered) {
                val right = selected == correctKey
                Div({ classes("banner", if (right) "info" else "error") }) {
                    Icon(if (right) "check_circle" else "cancel", filled = true)
                    Text(if (right) "Resposta certa!" else "Resposta certa: $correctKey")
                }
                if (question.explanation.isNotBlank()) Card { H3 { Text("Explicação") }; Div({ attr("style", "margin-top:8px") }) { Markdown(question.explanation) } }
                Btn(if (index + 1 < initial.size) "Próxima questão" else "Ver resultado", { selected = null; index++ }, block = true, icon = "arrow_forward")
            } else {
                Card(extra = "soft") { P({ classes("muted", "small") }) { Text("Escolha uma alternativa. A correção e a explicação aparecem logo em seguida.") } }
            }
        }
    }
}
