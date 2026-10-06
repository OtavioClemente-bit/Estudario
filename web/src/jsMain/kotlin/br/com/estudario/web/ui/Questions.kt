package br.com.estudario.web.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import br.com.estudario.domain.ProgressEngine
import br.com.estudario.web.Route
import br.com.estudario.web.Router
import br.com.estudario.web.data.Actions
import br.com.estudario.web.data.Prefs
import br.com.estudario.web.data.Question
import br.com.estudario.web.data.Queries
import br.com.estudario.web.data.Snapshot
import br.com.estudario.web.data.Store
import kotlinx.browser.window
import org.jetbrains.compose.web.dom.B
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H2
import org.jetbrains.compose.web.dom.H3
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text
import org.w3c.dom.events.KeyboardEvent
import kotlin.js.Date
import kotlin.random.Random

/** Os modos do Treinar no app. */
private val modes = listOf(
    "smart" to "Treino inteligente",
    "random" to "Aleatórias",
    "new" to "Novas",
    "wrong" to "Apenas erradas",
    "mostwrong" to "Mais erradas",
    "neverright" to "Nunca acertadas",
    "favorites" to "Favoritas",
)

private val difficulties = listOf("all" to "Todas", "FACIL" to "Fácil", "MEDIA" to "Média", "DIFICIL" to "Difícil")

@Composable
fun TrainScreen() {
    val data = Store.data
    val competition = Queries.primaryCompetition(data)
    val subjects = Queries.subjectsOf(data, competition?.id)
    val questions = Queries.visibleQuestions(data)
    val topicsById = data.topics.associateBy { it.id }
    var mode by remember { mutableStateOf("smart") }
    var subject by remember { mutableStateOf(-1L) }
    var board by remember { mutableStateOf("all") }
    var difficulty by remember { mutableStateOf("all") }
    var count by remember { mutableStateOf(10) }
    val boards = questions.mapNotNull { it.board?.trim()?.takeIf(String::isNotBlank) }.distinct().sorted()
    val due = Queries.dueReviews(data)
    val errors = Queries.pendingErrors(data)
    val scope = "m-$mode~s-$subject~b-${js("encodeURIComponent")(board)}~d-$difficulty~n-$count"
    val available = pickQuestions(data, scope, preview = true).size

    PageHead("Treinar", "Escolha o foco e transforme cada sessão em progresso")

    Div({ classes("grid", "cols-4") }) {
        TrainTile("replay", "Revisões", if (due.isEmpty()) "Em dia" else "${due.size} para hoje", due.isNotEmpty()) { Router.go(Route.Reviews) }
        TrainTile("style", "Flashcards", "Revisão rápida", false) { Router.go(Route.Flashcards(null)) }
        TrainTile("error_med", "Caderno de erros", if (errors.isEmpty()) "Tudo certo" else "${errors.size} para rever", errors.isNotEmpty()) { Router.go(Route.Errors) }
        TrainTile("timer", "Simulados", "Provas completas", false) { Router.go(Route.Simulations) }
    }

    Card(extra = "pad-lg") {
        if (questions.isEmpty()) {
            Empty("quiz", "Seu banco de questões está vazio", "Abra um tópico no Edital e gere o material com IA: as questões aparecem aqui para treinar.", action = {
                Btn("Ir para o Edital", { Router.go(Route.Edital) }, icon = "checklist")
            })
            return@Card
        }
        Div({ classes("stack", "loose") }) {
            FilterBlock("Modo") { FilterChips(modes, mode) { mode = it } }
            FilterBlock("Matéria") {
                FilterChips(listOf(-1L to "Todas") + subjects.filter { s -> questions.any { topicsById[it.topicId]?.subjectId == s.id } }.map { it.id to it.name }, subject) { subject = it }
            }
            if (boards.isNotEmpty()) FilterBlock("Banca") { FilterChips(listOf("all" to "Todas") + boards.map { it to it }, board) { board = it } }
            FilterBlock("Dificuldade") { FilterChips(difficulties, difficulty) { difficulty = it } }
            Div({ classes("row", "between", "wrap") }) {
                Div {
                    H3 { Text("Quantidade") }
                    P({ classes("small", "muted") }) { Text(if (available == 0) "Nenhuma questão com esses filtros" else "$available disponíve${if (available == 1) "l" else "is"} com esses filtros") }
                }
                Stepper(count, { count = it }, step = 5, min = 5, max = 60, label = "Quantidade de questões")
            }
            Btn(modes.first { it.first == mode }.second.let { "Iniciar ${it.lowercase()}" }, { Router.go(Route.Quiz(scope)) }, block = true, style = "primary", icon = "play_arrow", enabled = available > 0)
        }
    }

    // Visão por matéria: quanto já foi feito e o acerto.
    val bySubject = questions.groupBy { topicsById[it.topicId]?.subjectId }
    Card {
        CardHead("Seu banco por matéria")
        Div({ classes("stack") }) {
            subjects.forEach { s ->
                val list = bySubject[s.id].orEmpty()
                if (list.isEmpty()) return@forEach
                val total = list.sumOf { it.answerCount }
                val right = list.sumOf { it.correctCount }
                androidx.compose.runtime.key(s.id) {
                    Div({ classes("stack", "tight") }) {
                        Div({ classes("row", "between") }) {
                            B({ classes("clamp-2") }) { Text(s.name) }
                            Span({ classes("small", "muted", "nowrap") }) { Text("${list.size} questões${if (total > 0) " · ${percent(right, total)}%" else ""}") }
                        }
                        ProgressBar(if (total == 0) 0.0 else right.toDouble() / total, if (total > 0 && right * 100 / total >= 70) "green" else null)
                    }
                }
            }
        }
    }
}

@Composable
private fun FilterBlock(title: String, content: @Composable () -> Unit) {
    Div({ classes("stack", "tight") }) {
        H3 { Text(title) }
        content()
    }
}

@Composable
private fun TrainTile(icon: String, title: String, subtitle: String, highlight: Boolean, onClick: () -> Unit) {
    val latest = androidx.compose.runtime.rememberUpdatedState(onClick)
    Button({ classes("card", "clickable"); attr("style", "text-align:left;font:inherit;color:inherit"); onClick { latest.value() } }) {
        Div({ classes("stack", "tight") }) {
            Span({ classes("task-mark"); attr("style", if (highlight) "background:var(--amber-soft);color:var(--on-amber-soft)" else "background:var(--primary-soft);color:var(--on-primary-soft)") }) { Icon(icon) }
            B { Text(title) }
            Span({ classes("small", "muted") }) { Text(subtitle) }
        }
    }
}

/** Escolhe as questões conforme o endereço (#/treinar/questoes/...). */
fun pickQuestions(data: Snapshot, scope: String, preview: Boolean = false): List<Question> {
    val pool = Queries.visibleQuestions(data)
    val topicsById = data.topics.associateBy { it.id }
    val errorIds = data.errors.filter { it.status != "CORRIGIDO" }.mapTo(hashSetOf()) { it.questionId }
    val random = Random(if (preview) 1 else Date.now().toLong())
    fun smart(list: List<Question>, n: Int): List<Question> {
        // Inéditas e erradas primeiro; dentro de cada grupo, ordem aleatória (como o treino inteligente do app).
        val (priority, rest) = list.partition { it.answerCount == 0 || it.id in errorIds }
        return (priority.shuffled(random) + rest.shuffled(random)).take(n)
    }
    // Endereços antigos: topico-ID, materia-ID-N, erros-N, rapido-N.
    val legacy = scope.split('-')
    when (legacy.firstOrNull()) {
        "topico" -> return pool.filter { it.topicId == legacy.getOrNull(1)?.toLongOrNull() }.let { smart(it, it.size) }
        "materia" -> return smart(pool.filter { topicsById[it.topicId]?.subjectId == legacy.getOrNull(1)?.toLongOrNull() }, legacy.getOrNull(2)?.toIntOrNull() ?: 10)
        "erros" -> return data.errors.filter { it.status != "CORRIGIDO" }.sortedBy { it.nextRetryAt ?: 0 }.mapNotNull { e -> pool.firstOrNull { it.id == e.questionId } }.take(legacy.getOrNull(1)?.toIntOrNull() ?: 20)
        "rapido" -> return smart(pool, legacy.getOrNull(1)?.toIntOrNull() ?: 10)
    }
    val params = scope.split('~').associate { part -> part.substringBefore('-') to part.substringAfter('-', "") }
    val mode = params["m"] ?: "smart"
    val subject = params["s"]?.toLongOrNull() ?: -1L
    val board = params["b"]?.let { js("decodeURIComponent")(it) as String } ?: "all"
    val difficulty = params["d"] ?: "all"
    val count = params["n"]?.toIntOrNull()?.coerceIn(1, 100) ?: 10
    val errorCounts = data.errors.associate { it.questionId to it.errorCount }
    val filtered = pool.filter { q ->
        (subject < 0 || topicsById[q.topicId]?.subjectId == subject) &&
            (board == "all" || q.board?.trim() == board) &&
            (difficulty == "all" || q.difficulty == difficulty)
    }
    val chosen = when (mode) {
        "random" -> filtered.shuffled(random)
        "new" -> filtered.filter { it.answerCount == 0 }.shuffled(random)
        "wrong" -> filtered.filter { it.id in errorIds }.shuffled(random)
        "mostwrong" -> filtered.filter { it.errorCount > 0 }.sortedByDescending { errorCounts[it.id] ?: it.errorCount }
        "neverright" -> filtered.filter { it.answerCount > 0 && it.correctCount == 0 }.shuffled(random)
        "favorites" -> filtered.filter { it.favorite }.shuffled(random)
        else -> return smart(filtered, count)
    }
    return chosen.take(count)
}

@Composable
fun QuizScreen(scope: String) {
    val initial = remember(scope) { pickQuestions(Store.data, scope) }
    val sessionId = remember(scope) { Actions.newSessionId() }
    val startedAt = remember(scope) { Date.now().toLong() }
    var index by remember(scope) { mutableStateOf(0) }
    var selected by remember(scope) { mutableStateOf<String?>(null) }
    var revealed by remember(scope) { mutableStateOf(false) }
    var results by remember(scope) { mutableStateOf(listOf<Boolean>()) }
    var saved by remember(scope) { mutableStateOf(false) }
    val explainNow = remember { Prefs.explanationRightAway }

    if (initial.isEmpty()) {
        Card { Empty("quiz", "Nada para treinar aqui", "Não há questões neste recorte. Tente outros filtros.", action = { Btn("Voltar ao Treinar", { Router.go(Route.Train) }) }) }
        return
    }

    if (index >= initial.size) {
        val right = results.count { it }
        if (!saved) {
            saved = true
            val topicIds = initial.mapTo(hashSetOf()) { it.topicId }
            val subjectIds = Store.data.topics.filter { it.id in topicIds }.mapTo(hashSetOf()) { it.subjectId }
            val type = when { scope.startsWith("topico") -> "TOPIC"; scope.contains("s-") && !scope.contains("s--1") -> "SUBJECT"; scope.startsWith("erros") || scope.contains("m-wrong") -> "ERROR_REVIEW"; scope.contains("m-smart") -> "SMART"; else -> "QUICK" }
            Store.update { Actions.saveQuestionSession(it, type, startedAt, results.size, right, subjectIds, topicIds, sessionId) }
        }
        val xp = ProgressEngine.previewQuestions(results.size).base + right
        Card(extra = "pad-lg") {
            Div({ classes("stack"); attr("style", "align-items:center;text-align:center;padding:12px 0") }) {
                Ring(if (results.isEmpty()) 0.0 else right.toDouble() / results.size) {
                    B { Text("${percent(right, results.size)}%") }
                    Span { Text("de acerto") }
                }
                H2 { Text(if (right * 10 >= results.size * 7) "Mandou bem!" else if (right * 2 >= results.size) "Bom treino!" else "Cada erro é um degrau") }
                P({ classes("muted") }) { Text("$right de ${results.size} certas. ${if (results.size - right > 0) "As erradas foram para o caderno de erros e voltam em 3 dias." else "Nenhuma foi para o caderno de erros."}") }
                Xp(xp)
                Div({ classes("row", "wrap"); attr("style", "justify-content:center;margin-top:8px") }) {
                    Btn("Treinar de novo", { Router.go(Route.Train) }, style = "outline", icon = "refresh")
                    Btn("Voltar ao início", { Router.go(Route.Home) }, icon = "home")
                }
            }
        }
        return
    }

    val question = Store.data.questions.firstOrNull { it.id == initial[index].id } ?: initial[index]
    val topic = Store.data.topics.firstOrNull { it.id == question.topicId }
    val answered = selected != null
    val showResult = answered && (explainNow || revealed)
    val correctKey = question.options.firstOrNull { it.correct }?.key
    val options = question.options.sortedBy { it.position }

    fun choose(key: String) {
        if (selected != null) return
        selected = key
        var ok = false
        Store.update { snapshot -> Actions.answer(snapshot, question.id, key, sessionId).also { ok = it.second }.first }
        results = results + ok
    }
    fun next() { selected = null; revealed = false; index++ }

    // Teclado: A–E escolhem, Enter avança.
    val latestChoose = androidx.compose.runtime.rememberUpdatedState(::choose)
    val latestNext = androidx.compose.runtime.rememberUpdatedState(::next)
    val latestState = androidx.compose.runtime.rememberUpdatedState(Triple(answered, options.map { it.key }, showResult))
    DisposableEffect(scope) {
        val listener: (org.w3c.dom.events.Event) -> Unit = { event ->
            val key = (event as KeyboardEvent).key.uppercase()
            val (isAnswered, keys, shown) = latestState.value
            when {
                !isAnswered && key in keys -> latestChoose.value(key)
                isAnswered && (key == "ENTER" || key == "ARROWRIGHT") && shown -> latestNext.value()
            }
        }
        window.addEventListener("keydown", listener)
        onDispose { window.removeEventListener("keydown", listener) }
    }

    Div({ classes("row", "between") }) {
        Btn("Sair", { Router.go(Route.Train) }, style = "ghost", small = true, icon = "close")
        Span({ classes("strong", "muted") }) { Text("${index + 1} de ${initial.size}") }
        Div({ classes("row") }) {
            Span({ classes("chip", "green") }) { Icon("check"); Text("${results.count { it }}") }
            Span({ classes("chip", "red") }) { Icon("close"); Text("${results.count { !it }}") }
        }
    }
    ProgressBar(index.toDouble() / initial.size)
    Card(extra = "pad-lg") {
        Div({ classes("stack") }) {
            Div({ classes("row", "between") }) {
                Div({ classes("row", "wrap") }) {
                    topic?.let { Chip(it.title, "primary") }
                    listOfNotNull(question.board, question.agency, question.year?.toString()).takeIf { it.isNotEmpty() }?.let { Chip(it.joinToString(" · ")) }
                }
                IconButton(if (question.favorite) "star" else "star_outline", if (question.favorite) "Tirar dos favoritos" else "Favoritar") {
                    Store.update { Actions.toggleFavorite(it, question.id) }
                }
            }
            P({ classes("statement") }) { Text(question.statement) }
            Div({ classes("stack", "tight") }) {
                options.forEach { option ->
                    androidx.compose.runtime.key(question.id, option.key) {
                        val state = when {
                            !answered -> null
                            showResult && option.key == correctKey -> "right"
                            showResult && option.key == selected -> "wrong"
                            option.key == selected -> "chosen"
                            else -> null
                        }
                        Button({
                            classes(*listOfNotNull("option", state).toTypedArray())
                            if (answered) attr("disabled", "")
                            onClick { choose(option.key) }
                        }) {
                            Span({ classes("key") }) { Text(option.key) }
                            Span { Inline(option.text) }
                        }
                    }
                }
            }
        }
    }
    if (answered) {
        if (showResult) {
            val right = selected == correctKey
            Div({ classes("banner", if (right) "ok" else "error") }) {
                Icon(if (right) "check_circle" else "cancel", filled = true)
                Text(if (right) "Resposta certa!" else "Resposta certa: $correctKey. A questão foi para o caderno de erros.")
            }
            if (question.explanation.isNotBlank()) Card {
                CardHead("Explicação")
                Markdown(question.explanation)
            }
            Btn(if (index + 1 < initial.size) "Próxima questão" else "Ver resultado", { next() }, block = true, icon = "arrow_forward")
        } else {
            Div({ classes("row", "wrap") }) {
                Btn("Ver correção", { revealed = true }, style = "tonal", icon = "visibility")
                Btn(if (index + 1 < initial.size) "Próxima" else "Ver resultado", { next() }, icon = "arrow_forward")
            }
        }
    } else {
        P({ classes("small", "faint"); attr("style", "text-align:center") }) { Text("Dica: use as teclas A, B, C, D, E para responder e Enter para avançar.") }
    }
}
