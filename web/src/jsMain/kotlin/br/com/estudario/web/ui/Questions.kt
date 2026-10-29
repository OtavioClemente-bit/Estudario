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
import org.jetbrains.compose.web.dom.I
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
    "neverright" to "Nunca acertadas",
    "mostwrong" to "Mais erradas",
    "new" to "Questões novas",
    "favorites" to "Favoritas",
    "random" to "Aleatórias",
)
private val modeInfo = mapOf(
    "smart" to ("Brain" to "Mistura seus erros, os tópicos mais fracos e questões novas. O melhor para o dia a dia."),
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
    var help by remember { mutableStateOf(false) }
    if (help) TrainHelp { help = false }

    PageHead("Treinar questões", "Treinos recomendados e o seu treino do seu jeito. Os simulados ganharam aba própria.")
    CompetitionTabs()

    if (questions.isNotEmpty()) {
        // Diagnóstico enxuto: o acerto geral num medidor, até três treinos com o motivo e as matérias
        // da mais fraca para a mais forte (quatro à vista; o resto em "Ver todas").
        val bySubjectAll = questions.groupBy { topicsById[it.topicId]?.subjectId }
        val answered = questions.sumOf { it.answerCount }
        val right = questions.sumOf { it.correctCount }
        val scores = subjects.mapNotNull { sb ->
            val list = bySubjectAll[sb.id].orEmpty()
            if (list.isEmpty()) null else Triple(sb, list.sumOf { it.answerCount }, list.sumOf { it.correctCount })
        }
        val weakest = scores.filter { it.second >= 5 }.minByOrNull { it.third.toDouble() / it.second }
        val neverAnswered = questions.count { it.answerCount == 0 }
        val overall = if (answered == 0) null else percent(right, answered)
        var allSubjects by remember { mutableStateOf(false) }
        Div({ classes("gx-panel", "dg") }) {
            Div({ classes("dg-top") }) {
                Div({ classes("dg-gauge"); attr("style", "--v:${overall ?: 0};--c:${accuracyTone(overall)}") }) {
                    Div({ classes("dg-gauge-in") }) { B { Text(overall?.let { "$it%" } ?: "—") }; Span { Text("de acerto") } }
                }
                Div({ classes("dg-head") }) {
                    Span({ classes("gx-kicker") }) { Text("SEU DIAGNÓSTICO") }
                    B { Text(when { overall == null -> "Resolva as primeiras questões"; overall >= 70 -> "Você está no ritmo da aprovação"; overall >= 50 -> "Bom caminho, dá para subir"; else -> "Hora de reforçar a base" }) }
                    Span { Text(if (answered == 0) "O diagnóstico mostra seu acerto por matéria assim que você responder." else "$answered respostas em ${scores.count { it.second > 0 }} matérias") }
                }
            }
            // Treinos para agora: o primeiro é o recomendado, em destaque.
            Div({ classes("dg-recs") }) {
                val recs = buildList {
                    if (weakest != null && percent(weakest.third, weakest.second) < 70) add(Triple(Triple("Target", "#FFC94D", "Reforçar ${weakest.first.name}"), "Sua matéria mais fraca: ${percent(weakest.third, weakest.second)}% de acerto", "m-smart~s-${weakest.first.id}~t--1~b-all~d-all~n-15"))
                    add(Triple(Triple("Bolt", "#A78BFF", "Desafio do dia"), "10 questões entre erros, revisões atrasadas e pontos fracos", "m-smart~s--1~t--1~b-all~d-all~n-10"))
                    if (neverAnswered > 0) add(Triple(Triple("Star", "#2FD39A", "Questões novas"), "$neverAnswered que você ainda não viu", "m-new~s--1~t--1~b-all~d-all~n-10"))
                }.take(3)
                recs.forEachIndexed { i, (look, why, scope) ->
                    val (icon, color, title) = look
                    Button({ classes(*listOfNotNull("dg-rec", if (i == 0) "top" else null).toTypedArray()); attr("type", "button"); attr("style", "--c:$color"); onClick { Router.go(Route.Quiz(scope)) } }) {
                        Span({ classes("dg-rec-ic") }) { Icon(icon) }
                        Span({ classes("dg-rec-txt") }) {
                            if (i == 0) Span({ classes("dg-rec-tag") }) { Text("RECOMENDADO AGORA") }
                            B { Text(title) }
                            Span { Text(why) }
                        }
                        Span({ classes("dg-rec-go") }) { Icon("play_arrow", plain = true) }
                    }
                }
            }
            if (scores.isNotEmpty()) {
                val ranked = scores.sortedBy { if (it.second == 0) 101 else percent(it.third, it.second) }
                Div({ classes("dg-subjects") }) {
                    Span({ classes("gx-label") }) { Text("Por matéria · da mais fraca para a mais forte") }
                    (if (allSubjects) ranked else ranked.take(4)).forEach { (sb, total, ok) ->
                        androidx.compose.runtime.key(sb.id) {
                            val pct = if (total == 0) null else percent(ok, total)
                            Button({ classes("dg-subj"); attr("type", "button"); attr("style", "--c:${accuracyTone(pct)}"); onClick { Router.go(Route.Quiz("m-smart~s-${sb.id}~t--1~b-all~d-all~n-15")) } }) {
                                Span({ classes("dg-subj-name") }) { Text(sb.name) }
                                Span({ classes("dg-subj-bar") }) { Span({ attr("style", "width:${(pct ?: 0).coerceAtLeast(3)}%") }) }
                                B { Text(pct?.let { "$it%" } ?: "—") }
                            }
                        }
                    }
                    if (ranked.size > 4) Button({ classes("gx-more"); attr("type", "button"); onClick { allSubjects = !allSubjects } }) {
                        Text(if (allSubjects) "Mostrar menos" else "Ver todas as ${ranked.size} matérias")
                        Icon(if (allSubjects) "expand_less" else "expand_more", plain = true)
                    }
                }
            }
        }
    }

    if (questions.isEmpty()) {
        Card(extra = "pad-lg") {
            Empty("quiz", "Seu banco de questões está vazio", "Abra um tópico no Edital e gere o material com IA: as questões aparecem aqui para treinar.", action = {
                Btn("Ir para o Edital", { Router.go(Route.Edital) }, icon = "checklist")
            })
        }
        TrainPreferences()
        return
    }
    // Montar o treino: o mesmo estúdio de gerar material (gx-*), com o resumo e o botão no fim.
    Div({ classes("gx-panel", "gx-train") }) {
        Div({ classes("gx-top", "flat") }) {
            Div({ classes("gx-top-id") }) {
                Span({ classes("gx-kicker") }) { Text("ESTÚDIO DE TREINO") }
                H2 { Text("Monte o seu treino") }
            }
            Button({ classes("vr-help", "inline"); attr("type", "button"); attr("aria-label", "Como funciona o treino"); attr("title", "Como funciona o treino"); onClick { help = true } }) { Text("?") }
        }
        Div({ classes("gx-controls", "solo") }) {
            GxSection("gps_fixed", "Modo", "Como as questões vão ser escolhidas", "#A78BFF") {
                GxTiles(modes.map { (key, label) -> val (icon, desc) = modeInfo[key] ?: ("quiz" to ""); GxOption(key, label, desc, icon, MODE_COLORS[key] ?: "#A78BFF") }, { it == mode }, cols = 3, multi = false) { mode = it }
            }
            GxSection("library_books", "Matéria", null, "#22D3EE") {
                GxPicker(listOf(-1L to "Todas") + subjects.filter { s -> questions.any { topicsById[it.topicId]?.subjectId == s.id } }.map { it.id to it.name }, subject, searchHint = "Buscar matéria") { subject = it; topic = -1L }
                if (subject >= 0) {
                    val topicsWithQuestions = data.topics.filter { t -> t.subjectId == subject && questions.any { it.topicId == t.id } }
                    if (topicsWithQuestions.isNotEmpty()) {
                        Span({ classes("gx-label") }) { Text("Tópico") }
                        GxPicker(listOf(-1L to "Todos") + topicsWithQuestions.map { it.id to it.title }, topic, limit = 6, searchHint = "Buscar tópico") { topic = it }
                    }
                }
            }
            GxSection("speed", "Dificuldade", "Igual ao estúdio de material: do básico às pegadinhas", "#FFC94D") {
                GxSeg(listOf(
                    GxOption("all", "Todas", "Um pouco de cada", color = "#A78BFF"),
                    GxOption("FACIL", "Fácil", "Fixar o básico", color = "#2FD39A"),
                    GxOption("MEDIA", "Média", "Nível da maioria das provas", color = "#FFC94D"),
                    GxOption("DIFICIL", "Difícil", "Pegadinhas e detalhes", color = "#FF5A7A"),
                ), difficulty, { difficulty = it }) { option ->
                    Span({ classes("gx-meter", when (option.key) { "FACIL" -> "d-easy"; "MEDIA" -> "d-medium"; "DIFICIL" -> "d-hard"; else -> "d-mixed" }) }) { repeat(3) { I {} } }
                }
            }
            if (boards.isNotEmpty()) GxSection("Colunas", "Banca", null, "#6F9BFF") { GxPicker(listOf("all" to "Todas") + boards.map { it to it }, board, limit = 6, searchHint = "Buscar banca") { board = it } }
            GxSection("quiz", "Quantidade", if (available == 0) "Nenhuma questão com esses filtros" else "$available disponíve${if (available == 1) "l" else "is"} com esses filtros", "#2FD39A") {
                GxCounter(count, { count = it }, min = 5, max = 60, step = 5, unit = "questões", chips = listOf(10, 20, 30), label = "Quantidade de questões")
            }
            Div({ classes("gx-go", "row") }) {
                Div({ classes("gx-go-sum") }) {
                    Span({ classes("gx-kicker") }) { Text("SEU TREINO") }
                    B { Text(listOfNotNull(modes.first { it.first == mode }.second, subjects.firstOrNull { it.id == subject }?.name, board.takeIf { it != "all" }, "$count questões").joinToString(" · ")) }
                }
                GxLaunch(modes.first { it.first == mode }.second.let { "Iniciar ${it.lowercase()}" }, icon = "play_circle", enabled = available > 0) { Router.go(Route.Quiz(scope)) }
            }
        }
    }

    TrainPreferences()
}

/** O "?" do Treinar: os modos, os filtros e o que dá para fazer durante a bateria. */
@Composable
private fun TrainHelp(onClose: () -> Unit) {
    Modal(onDismiss = onClose, extraClass = "gx-sheetmodal th-help") {
        Div({ classes("gx-top") }) {
            Div({ classes("gx-top-id") }) {
                Span({ classes("gx-kicker") }) { Text("COMO FUNCIONA") }
                H2 { Text("Treinar do seu jeito") }
                P({ classes("gx-topic") }) { Text("Escolha o modo, filtre se quiser e comece. O que você erra vai para o caderno de erros sozinho.") }
            }
            Button({ classes("gx-x"); attr("type", "button"); attr("aria-label", "Fechar"); onClick { onClose() } }) { Icon("close", plain = true) }
        }
        Div({ classes("gx-controls", "solo") }) {
            GxSection("insights", "Seu diagnóstico", "O medidor mostra seu acerto geral. Embaixo, até 3 treinos prontos (o primeiro é o recomendado agora) e as matérias da mais fraca para a mais forte: toque numa para treinar só ela.", "#22D3EE") {}
            GxSection("gps_fixed", "Os modos", "Como as questões são escolhidas:", "#A78BFF") {
                Div({ classes("th-help-list") }) {
                    modes.forEach { (key, label) -> Span({ attr("style", "--c:${MODE_COLORS[key] ?: "#A78BFF"}") }) { B { Text("$label: ") }; Text(modeInfo[key]?.second.orEmpty()) } }
                }
            }
            GxSection("tune", "Filtros", "Matéria e tópico, dificuldade (fácil, média, difícil ou todas) e banca. A quantidade vai de 5 a 60 questões; embaixo aparece quantas existem com esses filtros.", "#FFC94D") {}
            GxSection("quiz", "Respondendo", null, "#2FD39A") {
                Div({ classes("th-help-list") }) {
                    Span { B { Text("Marque e confirme: ") }; Text("toque na alternativa e em \"Confirmar resposta\". No teclado: A a E (ou 1 a 5) e Enter; Enter ou → passa para a próxima.") }
                    Span { B { Text("Riscar alternativa: ") }; Text("o botão de riscar ao lado dela, ou o botão direito do mouse. Riscada, clicar desfaz o risco (não marca sem querer).") }
                    Span { B { Text("★ Favoritar: ") }; Text("guarda a questão para o modo Favoritas.") }
                    Span { B { Text("Reportar ou comentar: ") }; Text("achou erro na questão? Os botões ficam em cima dela.") }
                }
            }
            GxSection("description", "Explicação na hora ou no fim", "Em \"Suas preferências de treino\", logo abaixo, você escolhe ver a explicação logo após responder ou só a correção no fim.", "#6F9BFF") {}
            GxSection("autorenew", "Errou? Não se perde", "As erradas vão para o caderno de erros e voltam em 3 dias. Acertando de novo, sobem a escada da Virada (3, 10 e 30 dias).", "#FF5A7A") {}
            Div({ classes("gx-go", "row") }) {
                Span {}
                GxLaunch("Entendi, bora treinar", icon = "check_circle") { onClose() }
            }
        }
    }
}

/** Cor do acerto: vermelho abaixo de 50%, amarelo até 69%, verde dali para cima. */
private fun accuracyTone(pct: Int?): String = when { pct == null -> "#7F7BB5"; pct >= 70 -> "#2FD39A"; pct >= 50 -> "#FFC94D"; else -> "#FF5A7A" }

/** A cor de cada modo de treino no estúdio. */
private val MODE_COLORS = mapOf("smart" to "#A78BFF", "wrong" to "#FF5A7A", "neverright" to "#FF8A3D", "mostwrong" to "#F472B6", "new" to "#2FD39A", "favorites" to "#FFC94D", "random" to "#22D3EE")

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
/**
 * A bateria da tarefa do plano (Missão do Folha) sem repetir acerto: não respondidas do tópico, os
 * erros para refazer e as não respondidas dos tópicos já começados da mesma matéria (PlanQuestionPool,
 * a mesma regra do app). Vazia: não sobrou nada novo nem errado.
 */
fun planQuestionIds(data: Snapshot, topicId: Long?, subjectId: Long?, count: Int): List<Long> {
    val topicsById = data.topics.associateBy { it.id }
    val errorIds = data.errors.filter { it.status != "CORRIGIDO" }.mapTo(hashSetOf()) { it.questionId }
    val candidates = Queries.visibleQuestions(data).filter { it.simulationId == null }.map {
        br.com.estudario.domain.planner.PlanQuestionCandidate(it.id, it.topicId, topicsById[it.topicId]?.subjectId, it.answerCount > 0, it.id in errorIds)
    }
    val studied = data.topics.filter(Queries::isStudied).mapTo(hashSetOf()) { it.id }
    val subject = subjectId ?: topicId?.let { topicsById[it]?.subjectId }
    return br.com.estudario.domain.planner.PlanQuestionPool.pick(candidates, topicId, subject, studied, count)
}

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
        // plano-TÓPICO-MATÉRIA-N (0 = sem): a bateria da tarefa do plano, sem repetir acerto.
        "plano" -> {
            val ids = planQuestionIds(data, legacy.getOrNull(1)?.toLongOrNull()?.takeIf { it > 0 }, legacy.getOrNull(2)?.toLongOrNull()?.takeIf { it > 0 }, legacy.getOrNull(3)?.toIntOrNull()?.coerceAtLeast(1) ?: 10)
            val byId = pool.associateBy { it.id }
            return ids.mapNotNull(byId::get)
        }
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
    var attempt by remember(scope) { mutableStateOf(0) }
    // Repetir o mesmo recorte não muda a URL. Cada tentativa precisa de uma composição própria
    // para reiniciar respostas, sorteio, cronômetro e id da sessão, sem duplicar a tarefa do plano.
    androidx.compose.runtime.key(scope, attempt) {
        QuizAttempt(scope, onRepeat = { attempt++ })
    }
}

@Composable
private fun QuizAttempt(scope: String, onRepeat: () -> Unit) {
    val initial = remember(scope) { pickQuestions(Store.data, scope) }
    val sessionId = remember(scope) { Actions.newSessionId() }
    val startedAt = remember(scope) { Date.now().toLong() }
    // O cronômetro da bateria, como no app: corre enquanto há questão e o tempo vai para o Desempenho.
    var elapsed by remember(scope) { mutableStateOf(0L) }
    androidx.compose.runtime.LaunchedEffect(scope) {
        while (true) { elapsed = (Date.now().toLong() - startedAt) / 1000; kotlinx.coroutines.delay(1000) }
    }
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
        // fim da bateria: a assinatura do Estudário
        androidx.compose.runtime.LaunchedEffect(Unit) { runCatching { kotlinx.browser.window.asDynamic().estudarioSom?.play("resultado") } }
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
                        Btn("Treinar de novo", onRepeat, style = "outline", icon = "refresh")
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

    HoldCelebrations()
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

    QuestionHud(index, initial.size, results, startedAt, onExit = { scope.removePrefix("topico-").substringBefore("-").toLongOrNull()?.takeIf { scope.startsWith("topico-") }?.let { Router.go(Route.Topic(it)) } ?: Router.go(Route.Train) })
    // A questão nova entra deslizando da direita (o cartão é recriado a cada questão).
    androidx.compose.runtime.key(index) {
    Card(extra = "pad-lg q-card") {
        Div({ classes("stack") }) {
            Div({ classes("row", "between", "q-head") }) {
                Div({ classes("row", "wrap", "q-head-tags") }) {
                    topic?.let { Chip(it.title, "primary") }
                    listOfNotNull(question.board, question.agency, question.year?.toString()).filter { !it.contains("http") && it.length <= 60 }.takeIf { it.isNotEmpty() }?.let { Chip(it.joinToString(" · ")) }
                }
                Div({ classes("row", "q-head-tools"); attr("style", "gap:2px") }) {
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


/** Emblemas da sequência de acertos na bateria: aparecem ao chegar em cada marca. */
private val STREAK_BADGES = listOf(3 to "Aquecendo", 5 to "Em chamas", 10 to "Imparável", 15 to "Lenda da bateria", 20 to "Gabaritando")

/**
 * A reação do Folha à resposta, como no app: comemora o acerto, consola no erro, mostra a
 * sequência de acertos seguidos e, ao bater uma marca (3, 5, 10, 15, 20), o emblema.
 */
@Composable
internal fun AnswerReaction(right: Boolean, correctKey: String?, questionId: Long, results: List<Boolean>) {
    val streak = results.asReversed().takeWhile { it }.size
    val badge = if (right) STREAK_BADGES.lastOrNull { it.first == streak } else null
    // Só depois de responder, ele reage ao contexto: virou depois de errar (ufa), sequência longa (em chamas).
    val relief = right && streak == 1 && results.size >= 2 && !results[results.size - 2]
    val fire = right && streak >= 5
    // errou três ou mais seguidas: em vez da frase de erro, ele acolhe
    val tough = !right && results.size >= 3 && results.takeLast(3).none { it }
    val F = br.com.estudario.domain.FolhaFalas
    val tl = br.com.estudario.domain.FolhaMemory.toughLines
    val line = when { fire -> F.fire(questionId); relief -> F.relief(questionId); right -> F.answer(true, questionId); tough -> tl[((questionId % tl.size + tl.size) % tl.size).toInt()]; else -> F.answer(false, questionId) }
    // o som da resposta (sons do Estudário): acerto, erro ou o arpejo da sequência
    androidx.compose.runtime.LaunchedEffect(questionId) { runCatching { kotlinx.browser.window.asDynamic().estudarioSom?.play(if (fire || badge != null) "combo" else if (right) "acerto" else "erro") } }
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
            // Marca da bateria (3, 5, 10... seguidas), só na hora: não é emblema das Conquistas nem fica salva.
            Icon("local_fire_department", filled = true)
            Span({ classes("xs") }) { Text("${streak} seguidas") }
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
