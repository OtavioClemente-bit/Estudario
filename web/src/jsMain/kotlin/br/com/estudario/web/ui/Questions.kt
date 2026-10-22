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

/** Os modos do Treinar no app, com o mesmo texto de cada um. */
private val modes = listOf(
    "smart" to "Treino inteligente",
    "wrong" to "Refazer erros",
    "neverright" to "Nunca acertadas",
    "mostwrong" to "Mais erradas",
    "new" to "Questões novas",
    "favorites" to "Favoritas",
    "random" to "Aleatórias",
)
private val modeInfo = mapOf(
    "smart" to ("Brain" to "Mistura seus erros, os tópicos mais fracos e questões novas. O melhor para o dia a dia."),
    "wrong" to ("Cycle" to "Só as questões que você errou, para fechar as lacunas."),
    "neverright" to ("Warning" to "As que você ainda não acertou nenhuma vez."),
    "mostwrong" to ("Target" to "As que você mais errou. Onde está o ponto fraco de verdade."),
    "new" to ("Star" to "Só as que você nunca respondeu. Bom para avançar no conteúdo."),
    "favorites" to ("Heart" to "As que você marcou para rever."),
    "random" to ("Dice" to "Sorteio livre, sem critério."),
)

private val difficulties = listOf("all" to "Todas", "FACIL" to "Fácil", "MEDIA" to "Média", "DIFICIL" to "Difícil")

@Composable
fun TrainScreen() {
    val data = Store.data
    val competition = CompetitionFilter.current(data)
    val subjects = Queries.subjectsOf(data, competition?.id)
    val questions = Queries.visibleQuestions(data)
    val topicsById = data.topics.associateBy { it.id }
    var mode by remember { mutableStateOf("smart") }
    var subject by remember { mutableStateOf(-1L) }
    var topic by remember { mutableStateOf(-1L) }
    var board by remember { mutableStateOf("all") }
    var difficulty by remember { mutableStateOf("all") }
    var count by remember { mutableStateOf(10) }
    val boards = questions.mapNotNull { it.board?.trim()?.takeIf(String::isNotBlank) }.distinct().sorted()
    val due = Queries.dueReviews(data)
    val errors = Queries.pendingErrors(data)
    val scope = "m-$mode~s-$subject~t-$topic~b-${js("encodeURIComponent")(board)}~d-$difficulty~n-$count"
    val available = pickQuestions(data, scope, preview = true).size

    PageHead("Treinar questões", "Simulados, treinos recomendados e o seu treino do seu jeito")
    CompetitionTabs()

    // Simulados: a porta de entrada fica no topo, como no app.
    Button({ classes("train-sim"); onClick { Router.go(Route.Simulations) } }) {
        Span({ classes("train-sim-ic") }) { Icon("Timer") }
        Span({ classes("grow") }) {
            Span({ classes("train-tag") }) { Text("SIMULADOS") }
            B { Text("Prova inédita no estilo da sua banca") }
            Span({ classes("small") }) { Text("Com cronômetro, gabarito no final e nota por matéria. Comece pelo diagnóstico.") }
        }
        Icon("chevron_right", plain = true)
    }

    if (questions.isNotEmpty()) {
        // Diagnóstico: o acerto geral e o de cada matéria; tocar numa matéria abre um treino dela.
        val bySubjectAll = questions.groupBy { topicsById[it.topicId]?.subjectId }
        val answered = questions.sumOf { it.answerCount }
        val right = questions.sumOf { it.correctCount }
        val scores = subjects.mapNotNull { sb ->
            val list = bySubjectAll[sb.id].orEmpty()
            if (list.isEmpty()) null else Triple(sb, list.sumOf { it.answerCount }, list.sumOf { it.correctCount })
        }
        Card {
            Div({ classes("train-diag") }) {
                Div {
                    Span({ classes("train-tag") }) { Text("SEU DIAGNÓSTICO") }
                    B({ classes("train-diag-num") }) { Text(if (answered == 0) "—" else "${percent(right, answered)}%") }
                    Span({ classes("small", "muted") }) { Text(if (answered == 0) "Resolva as primeiras questões para o diagnóstico mostrar seu acerto por matéria." else "de acerto em $answered respostas") }
                }
            }
            // Recomendados: o que treinar agora, com o motivo escrito.
            val weakest = scores.filter { it.second >= 5 }.minByOrNull { it.third.toDouble() / it.second }
            val neverAnswered = questions.count { it.answerCount == 0 }
            Div({ classes("train-recs") }) {
                if (errors.isNotEmpty()) TrainTile("Cycle", "Refazer seus erros", "${errors.size} questões em que você mais erra do que acerta.", true) { Router.go(Route.Quiz("m-wrong~s--1~t--1~b-all~d-all~n-${errors.size.coerceIn(5, 20)}")) }
                if (weakest != null && percent(weakest.third, weakest.second) < 70) TrainTile("Target", "Reforçar ${weakest.first.name}", "Sua matéria mais fraca: ${percent(weakest.third, weakest.second)}% de acerto em ${weakest.second} respostas.", true) { Router.go(Route.Quiz("m-smart~s-${weakest.first.id}~t--1~b-all~d-all~n-15")) }
                TrainTile("Bolt", "Desafio do dia", "10 questões entre erros recorrentes, revisões atrasadas e tópicos de menor domínio.", false) { Router.go(Route.Quiz("m-smart~s--1~t--1~b-all~d-all~n-10")) }
                if (neverAnswered > 0) TrainTile("Star", "Avançar com questões novas", "$neverAnswered questões que você ainda não viu.", false) { Router.go(Route.Quiz("m-new~s--1~t--1~b-all~d-all~n-10")) }
            }
            if (scores.isNotEmpty()) Div({ classes("train-subjects") }) {
                scores.forEach { (sb, total, ok) ->
                    androidx.compose.runtime.key(sb.id) {
                        Button({ classes("train-subj"); onClick { Router.go(Route.Quiz("m-smart~s-${sb.id}~t--1~b-all~d-all~n-15")) } }) {
                            Span({ classes("grow") }) {
                                B({ classes("clamp-2") }) { Text(sb.name) }
                                Span({ classes("xs", "muted") }) { Text("${bySubjectAll[sb.id].orEmpty().size} questões · $total respostas") }
                            }
                            Span({ classes("train-subj-pct") }) { Text(if (total == 0) "—" else "${percent(ok, total)}%") }
                            Icon("play_arrow", plain = true)
                        }
                    }
                }
            }
        }
    }

    Card(extra = "pad-lg") {
        if (questions.isEmpty()) {
            Empty("quiz", "Seu banco de questões está vazio", "Abra um tópico no Edital e gere o material com IA: as questões aparecem aqui para treinar.", action = {
                Btn("Ir para o Edital", { Router.go(Route.Edital) }, icon = "checklist")
            })
            return@Card
        }
        Div({ classes("stack", "loose") }) {
            H2({ attr("style", "margin:0") }) { Text("Montar meu treino") }
            FilterBlock("Modo") {
                Div({ classes("train-modes") }) {
                    modes.forEach { (key, label) ->
                        val (icon, desc) = modeInfo[key] ?: ("quiz" to "")
                        androidx.compose.runtime.key(key) {
                            Button({ classes(*listOfNotNull("train-mode", if (mode == key) "on" else null).toTypedArray()); onClick { mode = key } }) {
                                Span({ classes("train-mode-ic") }) { Icon(icon) }
                                Span({ classes("grow") }) { B { Text(label) }; Span({ classes("xs", "muted") }) { Text(desc) } }
                            }
                        }
                    }
                }
            }
            FilterBlock("Matéria") {
                FilterChips(listOf(-1L to "Todas") + subjects.filter { s -> questions.any { topicsById[it.topicId]?.subjectId == s.id } }.map { it.id to it.name }, subject) { subject = it; topic = -1L }
            }
            if (subject >= 0) {
                val topicsWithQuestions = data.topics.filter { t -> t.subjectId == subject && questions.any { it.topicId == t.id } }
                if (topicsWithQuestions.isNotEmpty()) FilterBlock("Tópico") {
                    FilterChips(listOf(-1L to "Todos") + topicsWithQuestions.map { it.id to it.title }, topic) { topic = it }
                }
            }
            if (boards.isNotEmpty()) FilterBlock("Banca") { FilterChips(listOf("all" to "Todas") + boards.map { it to it }, board) { board = it } }
            FilterBlock("Dificuldade") { FilterChips(difficulties, difficulty) { difficulty = it } }
            Div({ classes("row", "between", "wrap") }) {
                Div {
                    H3 { Text("Quantidade") }
                    P({ classes("small", "muted") }) { Text(if (available == 0) "Nenhuma questão com esses filtros" else "$available disponíve${if (available == 1) "l" else "is"} com esses filtros") }
                }
                BrandCounter(count, { count = it }, step = 5, min = 5, max = 60, unit = "questões", chips = listOf(10, 20, 30), label = "Quantidade de questões")
            }
            Btn(modes.first { it.first == mode }.second.let { "Iniciar ${it.lowercase()}" }, { Router.go(Route.Quiz(scope)) }, block = true, style = "primary", icon = "play_arrow", enabled = available > 0)
        }
    }

    TrainPreferences()
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
        // topico-ID ou topico-ID-N (N = quantas a tarefa do plano pede; sem N, todas do tópico).
        "topico" -> return pool.filter { it.topicId == legacy.getOrNull(1)?.toLongOrNull() }.let { smart(it, legacy.getOrNull(2)?.toIntOrNull()?.coerceAtLeast(1) ?: it.size) }
        "materia" -> return smart(pool.filter { topicsById[it.topicId]?.subjectId == legacy.getOrNull(1)?.toLongOrNull() }, legacy.getOrNull(2)?.toIntOrNull() ?: 10)
        "erros" -> return data.errors.filter { it.status != "CORRIGIDO" }.sortedBy { it.nextRetryAt ?: 0 }.mapNotNull { e -> pool.firstOrNull { it.id == e.questionId } }.take(legacy.getOrNull(1)?.toIntOrNull() ?: 20)
        "rapido" -> return smart(pool, legacy.getOrNull(1)?.toIntOrNull() ?: 10)
    }
    val params = scope.split('~').associate { part -> part.substringBefore('-') to part.substringAfter('-', "") }
    val mode = params["m"] ?: "smart"
    val subject = params["s"]?.toLongOrNull() ?: -1L
    val topicFilter = params["t"]?.toLongOrNull() ?: -1L
    val board = params["b"]?.let { js("decodeURIComponent")(it) as String } ?: "all"
    val difficulty = params["d"] ?: "all"
    val count = params["n"]?.toIntOrNull()?.coerceIn(1, 100) ?: 10
    val errorCounts = data.errors.associate { it.questionId to it.errorCount }
    val filtered = pool.filter { q ->
        (subject < 0 || topicsById[q.topicId]?.subjectId == subject) &&
            (topicFilter < 0 || q.topicId == topicFilter) &&
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

/** Explicação após responder fica aqui, onde a pessoa treina; a meta diária de questões fica nos Ajustes. */
@Composable
fun TrainPreferences() {
    var explain by remember { mutableStateOf(Prefs.explanationRightAway) }
    Card {
        CardHead("Suas preferências de treino")
        Div({ classes("setting") }) {
            Icon("visibility")
            Div({ classes("grow") }) { B { Text("Explicação logo após responder") }; Div({ classes("small", "muted") }) { Text("Desligue para ver a correção só no fim.") } }
            Switch(explain, "Explicação logo após responder") { explain = it; Prefs.explanationRightAway = it }
        }
    }
}

@Composable
fun QuizScreen(scope: String) {
    val initial = remember(scope) { pickQuestions(Store.data, scope) }
    val sessionId = remember(scope) { Actions.newSessionId() }
    val startedAt = remember(scope) { Date.now().toLong() }
    var index by remember(scope) { mutableStateOf(0) }
    var selected by remember(scope) { mutableStateOf<String?>(null) }
    // Como no app: clicar só marca a alternativa; a resposta vale ao confirmar.
    var marked by remember(scope) { mutableStateOf<String?>(null) }
    var revealed by remember(scope) { mutableStateOf(false) }
    var results by remember(scope) { mutableStateOf(listOf<Boolean>()) }
    // Alternativas riscadas, como na prova de papel ("id da questão:letra"). Só ajuda a pensar.
    val crossedOut = remember(scope) { androidx.compose.runtime.mutableStateMapOf<String, Boolean>() }
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
            // Treino aberto por uma tarefa do plano: concluí-la junto, com o tempo e os acertos (como no app).
            val planTask = PlanQuiz.taskId?.takeIf { PlanQuiz.scope == scope }
            PlanQuiz.taskId = null
            Store.update { snapshot ->
                var next = Actions.saveQuestionSession(snapshot, type, startedAt, results.size, right, subjectIds, topicIds, sessionId)
                if (planTask != null && results.isNotEmpty()) {
                    val task = next.tasks.firstOrNull { it.id == planTask }
                    if (task != null) {
                        val minutes = ((Date.now().toLong() - startedAt) / 60_000L).toInt().coerceAtLeast(1)
                        next = Actions.completeTask(next, task.id, minutes, results.size, right, fromQuiz = true)
                        next = runCatching { br.com.estudario.web.data.Planning.replan(next, task.planId, br.com.estudario.domain.planner.ReplanReason.TASK_COMPLETED) }.getOrDefault(next)
                    }
                }
                next
            }
            if (planTask != null) Toast.show("Tarefa do plano concluída!")
        }
        val xp = ProgressEngine.previewQuestions(results.size).base + right
        Card(extra = "pad-lg") {
            Div({ classes("stack"); attr("style", "align-items:center;text-align:center;padding:12px 0") }) {
                Ring(if (results.isEmpty()) 0.0 else right.toDouble() / results.size) {
                    B { Text("${percent(right, results.size)}%") }
                    Span { Text("de acerto") }
                }
                H2 { Text(if (right * 10 >= results.size * 7) "Mandou bem!" else if (right * 2 >= results.size) "Bom treino!" else "Cada erro é um degrau") }
                // O Folha comenta o treino, como no app: fala no balão e depois comemora (ou pensa junto).
                val good = right * 10 >= results.size * 7
                val fine = right * 2 >= results.size
                FolhaTalking(sessionLine(scope, right, results.size, sessionId), 104, extra = "quiz-end-talk", restMood = if (good) "happy" else if (fine) "wink" else "thinking")
                P({ classes("muted") }) { Text("$right de ${results.size} certas. ${if (results.size - right > 0) "As erradas foram para o caderno de erros e voltam em 3 dias." else "Nenhuma foi para o caderno de erros."}") }
                Xp(xp)
                Div({ classes("row", "wrap"); attr("style", "justify-content:center;margin-top:8px") }) {
                    // Treino de um tópico volta para o conteúdo dele (o foco pode estar rodando lá, e é lá que se conclui).
                    val topicBack = scope.removePrefix("topico-").substringBefore("-").toLongOrNull()?.takeIf { scope.startsWith("topico-") }
                    if (topicBack != null) {
                        Btn("Treinar de novo", { Router.go(Route.Quiz("topico-$topicBack-${results.size.coerceAtLeast(5)}")) }, style = "outline", icon = "refresh")
                        Btn("Voltar ao conteúdo", { Router.go(Route.Topic(topicBack)) }, style = "primary", icon = "menu_book")
                    } else {
                        Btn("Treinar de novo", { Router.go(Route.Train) }, style = "outline", icon = "refresh")
                        Btn("Voltar ao início", { Router.go(Route.Home) }, icon = "home")
                    }
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
    val original = question.options.sortedBy { it.position }
    // Ordem nova a cada vez que a questão aparece (OptionShuffle): a letra mostrada segue a posição;
    // a resposta continua gravada pela alternativa original.
    val order = remember(question.id, index, sessionId) { br.com.estudario.domain.OptionShuffle.order(original.map { it.text }, Date.now().toLong() + question.id * 7919) }
    val options = order.map { original[it] }
    fun shownLetter(key: String?): String = original.indexOfFirst { it.key == key }.takeIf { it >= 0 }?.let { br.com.estudario.domain.OptionShuffle.displayLetter(order, it) } ?: (key ?: "?")
    val explanation = remember(question.id, order) { br.com.estudario.domain.OptionShuffle.relabel(question.explanation, original.map { it.key }, order) }

    fun choose(key: String) {
        if (selected != null) return
        selected = key
        var ok = false
        Store.update { snapshot -> Actions.answer(snapshot, question.id, key, sessionId).also { ok = it.second }.first }
        results = results + ok
    }
    fun mark(key: String) { if (selected == null) marked = if (marked == key) null else key }
    fun confirm() { marked?.let { choose(it) } }
    fun next() { selected = null; marked = null; revealed = false; index++ }

    // Teclado: A–E escolhem, Enter avança.
    val latestChoose = androidx.compose.runtime.rememberUpdatedState(::mark)
    val latestConfirm = androidx.compose.runtime.rememberUpdatedState(::confirm)
    val latestNext = androidx.compose.runtime.rememberUpdatedState(::next)
    // Teclado pela letra mostrada (A é a primeira da tela), não pela original.
    val latestState = androidx.compose.runtime.rememberUpdatedState(Triple(answered, options.mapIndexed { i, o -> "ABCDEF"[i].toString() to o.key }.toMap(), showResult))
    DisposableEffect(scope) {
        val listener: (org.w3c.dom.events.Event) -> Unit = { event ->
            val typing = (event.target as? org.w3c.dom.Element)?.closest("input, textarea, select, [contenteditable]") != null
            val raw = (event as KeyboardEvent).key.uppercase()
            // 1 a 5 também escolhem (1 = A), para quem prefere a fileira de números.
            val key = raw.toIntOrNull()?.takeIf { it in 1..6 }?.let { "ABCDEF"[it - 1].toString() } ?: raw
            val (isAnswered, keys, shown) = latestState.value
            if (!typing && !event.ctrlKey && !event.metaKey && !event.altKey) when {
                !isAnswered && key in keys -> latestChoose.value(keys.getValue(key))
                !isAnswered && key == "ENTER" -> latestConfirm.value()
                isAnswered && (key == "ENTER" || key == "ARROWRIGHT") && shown -> latestNext.value()
            }
        }
        window.addEventListener("keydown", listener)
        onDispose { window.removeEventListener("keydown", listener) }
    }

    Div({ classes("row", "between") }) {
        Btn("Sair", { scope.removePrefix("topico-").substringBefore("-").toLongOrNull()?.takeIf { scope.startsWith("topico-") }?.let { Router.go(Route.Topic(it)) } ?: Router.go(Route.Train) }, style = "ghost", small = true, icon = "close")
        Span({ classes("strong", "muted") }) { Text("${index + 1} de ${initial.size}") }
        Div({ classes("row") }) {
            // Os contadores saltam quando mudam (a chave nova recria o elemento e a animação roda).
            androidx.compose.runtime.key("ok-" + results.count { it }) { Span({ classes("chip", "green", "q-bump") }) { Icon("check"); Text("${results.count { it }}") } }
            androidx.compose.runtime.key("no-" + results.count { !it }) { Span({ classes("chip", "red", "q-bump") }) { Icon("close"); Text("${results.count { !it }}") } }
        }
    }
    ProgressBar(index.toDouble() / initial.size)
    // A questão nova entra deslizando da direita (o cartão é recriado a cada questão).
    androidx.compose.runtime.key(index) {
    Card(extra = "pad-lg q-card") {
        Div({ classes("stack") }) {
            Div({ classes("row", "between") }) {
                Div({ classes("row", "wrap") }) {
                    topic?.let { Chip(it.title, "primary") }
                    listOfNotNull(question.board, question.agency, question.year?.toString()).filter { !it.contains("http") && it.length <= 60 }.takeIf { it.isNotEmpty() }?.let { Chip(it.joinToString(" · ")) }
                }
                Div({ classes("row"); attr("style", "gap:2px") }) {
                    IconButton("forum", "Comentar esta questão") { Reports.comment(ReportKindWeb.QUESTION, question.statement, topic?.title) }
                    IconButton("flag", "Reportar erro nesta questão") { Reports.report(ReportKindWeb.QUESTION, question.statement, topic?.title) }
                    IconButton(if (question.favorite) "star" else "star_outline", if (question.favorite) "Tirar dos favoritos" else "Favoritar") {
                        Store.update { Actions.toggleFavorite(it, question.id) }
                    }
                }
            }
            QuestionStatement(question.statement)
            Div({ classes("stack", "tight") }) {
                options.forEach { option ->
                    androidx.compose.runtime.key(question.id, option.key) {
                        val state = when {
                            !answered -> null
                            showResult && option.key == correctKey -> "right"
                            showResult && option.key == selected -> "wrong"
                            option.key == selected -> "chosen"
                            else -> null
                        } ?: if (!answered && option.key == marked) "marked" else null
                        val crossKey = "${question.id}:${option.key}"
                        val crossed = crossedOut[crossKey] == true && !answered
                        fun toggleCross() { crossedOut[crossKey] = crossedOut[crossKey] != true }
                        Div({ classes("option-row") }) {
                            Button({
                                classes(*listOfNotNull("option", state, if (crossed) "crossed" else null).toTypedArray())
                                if (answered) attr("disabled", "")
                                // Riscada: clicar desfaz o risco em vez de marcar, para não marcar sem querer.
                                onClick { if (crossed) toggleCross() else mark(option.key) }
                                // Botão direito também risca, como no app (deslizar para o lado).
                                addEventListener("contextmenu") { event -> if (!answered) { event.preventDefault(); toggleCross() } }
                            }) {
                                Span({ classes("key") }) { Text(shownLetter(option.key)) }
                                Span { Inline(option.text) }
                            }
                            if (!answered) Button({
                                classes("strike-btn", *listOfNotNull(if (crossed) "on" else null).toTypedArray())
                                attr("title", if (crossed) "Desfazer risco" else "Riscar alternativa")
                                attr("aria-label", if (crossed) "Desfazer risco da alternativa ${option.key}" else "Riscar alternativa ${option.key}")
                                onClick { toggleCross() }
                            }) { Icon("strikethrough_s") }
                        }
                    }
                }
            }
        }
    }
    }
    if (answered) {
        if (showResult) {
            val right = selected == correctKey
            AnswerReaction(right, shownLetter(correctKey), question.id, results)
            if (question.explanation.isNotBlank()) Card {
                CardHead("Explicação")
                Markdown(explanation)
            }
            StatementSource(question.statement)
            Btn(if (index + 1 < initial.size) "Próxima questão" else "Ver resultado", { next() }, block = true, icon = "arrow_forward")
        } else {
            Div({ classes("row", "wrap") }) {
                Btn("Ver correção", { revealed = true }, style = "tonal", icon = "visibility")
                Btn(if (index + 1 < initial.size) "Próxima" else "Ver resultado", { next() }, icon = "arrow_forward")
            }
        }
    } else {
        Btn("Confirmar resposta", { confirm() }, block = true, icon = "check", enabled = marked != null)
        P({ classes("small", "faint"); attr("style", "text-align:center") }) { Text("Dica: A a E (ou 1 a 5) marcam a alternativa, Enter confirma e → passa para a próxima.") }
    }
}


/** O que o Folha fala ao acertar e errar (FolhaLines do app); a semente é a questão, para não trocar a cada tela. */
private val RIGHT_LINES = listOf(
    "Isso! Mandou muito bem.",
    "Acertou! Esse conteúdo já é seu.",
    "Na mosca! Banca nenhuma te pega nessa.",
    "Boa! É assim que a vaga vai chegando.",
    "Perfeito. Pode marcar esse ponto no placar.",
    "Acertou e com estilo. Próxima!",
)
private val WRONG_LINES = listOf(
    "Errar aqui é melhor que errar na prova. Lê a explicação comigo?",
    "Tudo bem! Essa volta pro seu caderno de erros e na próxima é sua.",
    "Quase! Esse é exatamente o tipo de pegadinha que a banca adora.",
    "Respira. Cada erro agora é um ponto a mais no dia da prova.",
    "Faz parte. Entendendo o porquê, você não erra mais essa.",
    "Opa, essa enganou. Bora entender e virar o jogo?",
)

/** Acertou logo depois de errar: o Folha solta o ar ("ufa"). */
private val RELIEF_LINES = listOf(
    "Ufa! Errou a anterior, mas essa você virou. É assim que se aprende.",
    "Ufa! Recuperou na hora. Essa sacudida faz bem.",
    "Ufa, voltou pro trilho! Bora manter.",
    "Viu? Um erro não define a bateria. Mandou bem agora.",
)

/** Muitos acertos seguidos: o Folha pega fogo junto. */
private val FIRE_LINES = listOf(
    "Tá pegando fogo! Ninguém te segura hoje.",
    "Sequência absurda! A banca que se cuide.",
    "Em chamas! Continua nesse ritmo que a vaga é sua.",
    "Isso é constância de quem passa. Próxima!",
)

/** Emblemas da sequência de acertos na bateria: aparecem ao chegar em cada marca. */
private val STREAK_BADGES = listOf(3 to "Aquecendo", 5 to "Em chamas", 10 to "Imparável", 15 to "Lenda da bateria", 20 to "Gabaritando")

/**
 * A reação do Folha à resposta, como no app: comemora o acerto, consola no erro, mostra a
 * sequência de acertos seguidos e, ao bater uma marca (3, 5, 10, 15, 20), o emblema.
 */
@Composable
private fun AnswerReaction(right: Boolean, correctKey: String?, questionId: Long, results: List<Boolean>) {
    val streak = results.asReversed().takeWhile { it }.size
    val badge = if (right) STREAK_BADGES.lastOrNull { it.first == streak } else null
    // Só depois de responder, ele reage ao contexto: virou depois de errar (ufa), sequência longa (em chamas).
    val relief = right && streak == 1 && results.size >= 2 && !results[results.size - 2]
    val fire = right && streak >= 5
    // errou três ou mais seguidas: em vez da frase de erro, ele acolhe
    val tough = !right && results.size >= 3 && results.takeLast(3).none { it }
    val lines = when { fire -> FIRE_LINES; relief -> RELIEF_LINES; right -> RIGHT_LINES; tough -> br.com.estudario.domain.FolhaMemory.toughLines; else -> WRONG_LINES }
    val line = lines[((questionId % lines.size + lines.size) % lines.size).toInt()]
    val mood = when { fire -> "ecstatic"; badge != null -> "proud"; relief -> "relieved"; right -> "happy"; tough -> "talking"; else -> "sad" }
    Div({ classes("answer-reaction", if (right) "ok" else "bad", "pop-in") }) {
        Div({ classes(if (right) "folha-cheer" else "folha-shake") }) { androidx.compose.runtime.key(questionId) { Folha(mood, 72) } }
        Div({ classes("grow") }) {
            B({ classes("reaction-title") }) { Text(when { fire -> "Em chamas!"; relief -> "Ufa, virou!"; right -> "Mandou bem!"; tough -> "Tô aqui com você"; else -> "Não foi dessa vez" }) }
            P({ classes("small") }) { Text(line) }
            if (!right) P({ classes("xs", "muted") }) { Text("Resposta certa: ${correctKey ?: "?"}. A questão foi para o caderno de erros.") }
            if (right && streak >= 2) Span({ classes("streak-chip") }) { Icon("local_fire_department", filled = true); Text("$streak seguidas") }
        }
        if (badge != null) Div({ classes("badge-pop") }) {
            Icon("military_tech", filled = true)
            Span({ classes("xs") }) { Text("Emblema") }
            B { Text(badge.second) }
        }
    }
}

/** O que o Folha diz ao fim do treino: muda com o resultado, com o tipo de treino e de vez em quando. */
private fun sessionLine(scope: String, right: Int, total: Int, seed: String): String {
    val rate = if (total == 0) 0.0 else right.toDouble() / total
    val errors = scope.startsWith("erros") || scope.contains("m-wrong")
    val options = when {
        total > 0 && right == total && errors -> listOf("Caderno de erros vencido! Errou antes, acertou agora: isso é aprendizado de verdade.", "Todas certas nas que você tinha errado. Esse conteúdo agora é seu.")
        total > 0 && right == total -> listOf("Gabaritou! $right de $total. Assim a aprovação chega mais perto.", "Perfeito! Nenhum erro. Tá voando nesse conteúdo.", "100%! Bora manter esse ritmo amanhã também.")
        rate >= 0.7 -> listOf("Mandou muito bem! $right de $total certas. Revise as erradas e fecha com chave de ouro.", "Esse é o ritmo de quem passa. As que escaparam voltam pra você no caderno de erros.", "Ótimo treino! Cada acerto é um ponto a mais na prova.")
        rate >= 0.5 -> listOf("Bom treino! Mais da metade certa. Vamos firmar as que escaparam?", "Tá no caminho. As erradas vão para o caderno de erros e voltam em 3 dias.", "Metade já é sua. Mais uma rodada e esse tópico fica firme.")
        else -> listOf("Calma, errar no treino é o que faz acertar na prova. Vamos revisar juntos?", "Esse tópico ainda está pegando. Que tal reler a teoria e tentar de novo?", "Todo mundo começa assim. As erradas voltam pra você treinar de novo.")
    }
    return options[(seed.hashCode() and Int.MAX_VALUE) % options.size]
}
