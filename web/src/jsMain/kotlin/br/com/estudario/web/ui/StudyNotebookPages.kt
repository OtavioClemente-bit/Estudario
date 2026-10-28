package br.com.estudario.web.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import br.com.estudario.domain.StudyNotebook
import br.com.estudario.web.Route
import br.com.estudario.web.Router
import br.com.estudario.web.data.Actions
import br.com.estudario.web.data.Queries
import br.com.estudario.web.data.Snapshot
import br.com.estudario.web.data.Store
import kotlinx.browser.localStorage
import org.jetbrains.compose.web.dom.B
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H2
import org.jetbrains.compose.web.dom.H3
import org.jetbrains.compose.web.dom.H4
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text
import kotlin.js.Date

/*
 * O Caderno de estudo que se escreve sozinho (a regra em StudyNotebook, módulo comum): a entrada é um
 * fichário com as matérias e a "Folheada do dia"; cada tópico estudado é uma página de caderno com o
 * essencial em frases curtas, os grifos em marca-texto, as notas em post-it, as pegadinhas como
 * carimbo e os cartões em fichinhas. O que a pessoa guardou continua em "Grifos, notas e salvos".
 */

private const val LAST_PAGE_KEY = "estudario.caderno.ultima"
private const val FLIP_DONE_KEY = "estudario.caderno.folheada"

/** Os tópicos estudados do concurso escolhido, com o que cada um tem, no formato do StudyNotebook. */
private fun notebookTopics(data: Snapshot): List<StudyNotebook.Topic> {
    val scope = CompetitionFilter.topicIds(data)
    val theoriesByTopic = data.theories.groupBy { it.topicId }
    val marksByTheory = data.theoryMarks.groupBy { it.theoryId }
    val questionTopic = data.questions.associate { it.id to it.topicId }
    val wrongByTopic = data.errors.filter { it.status != "CORRIGIDO" }.groupBy { questionTopic[it.questionId] }
    val snippets = data.snippets.groupBy { it.topicId }
    val summaries = data.summaries.groupBy { it.topicId }
    val notes = data.notes.filter { it.topicId != null }.groupBy { it.topicId }
    return Queries.leafTopics(data).filter { it.id in scope && Queries.isStudied(it) }.map { t ->
        val mine = snippets[t.id].orEmpty()
        val sums = summaries[t.id].orEmpty()
        // os cartões da revisão rápida ("### frente" + verso) também contam como cartões para lembrar
        val quickCards = sums.filter { it.kind == "RAPIDO" }.flatMap { s ->
            Regex("""###\s*(.+?)\n+([\s\S]+?)(?=\n###|$)""").findAll(s.markdown).map { it.groupValues[1].trim() to it.groupValues[2].trim() }.toList()
        }
        StudyNotebook.Topic(
            id = t.id, subjectId = t.subjectId, title = t.title, status = t.status,
            lastSeenAt = listOfNotNull(t.lastReviewedAt, t.lastStudiedAt).maxOrNull(),
            summary = (sums.firstOrNull { it.kind == "COMPLETO" } ?: sums.firstOrNull { it.kind != "RAPIDO" })?.markdown,
            tips = mine.filter { it.kind == "BIZU" }.map { it.text },
            traps = mine.filter { it.kind == "PEGADINHA" }.map { it.text },
            recall = mine.filter { it.kind == "RECUPERACAO" && !it.answer.isNullOrBlank() }.map { it.text to it.answer!! } + quickCards,
            marks = theoriesByTopic[t.id].orEmpty().flatMap { th -> val b = studyBlocks(th.markdown); marksByTheory[th.id].orEmpty().map { it.copy(quote = StudyNotebook.restoreQuote(it.quote, b, it.blockIndex)) } }.filter { it.quote.isNotBlank() }.distinctBy { it.quote }
                .map { StudyNotebook.Mark(it.quote, it.note, it.createdAt) },
            wrongQuestions = wrongByTopic[t.id].orEmpty().map { it.questionId },
            notes = notes[t.id].orEmpty().map { it.text },
        )
    }
}

private fun daySeed(): Long = Queries.todayEpoch()

private fun readLocal(k: String) = runCatching { localStorage.getItem(k) }.getOrNull()
private fun writeLocal(k: String, v: String) { runCatching { localStorage.setItem(k, v) } }

private fun firmClass(f: StudyNotebook.Firmness) = when (f) { StudyNotebook.Firmness.FIRM -> "ok"; StudyNotebook.Firmness.MID -> "mid"; StudyNotebook.Firmness.COLD -> "low" }

private fun greeting(): String {
    val h = Date().getHours()
    return when { h < 12 -> "Bom dia"; h < 18 -> "Boa tarde"; else -> "Boa noite" }
}

// ------------------------------------------------------------------ a entrada (o fichário)

@Composable
fun NotebookHome() {
    val data = Store.data
    val now = Date.now().toLong()
    val topics = remember(data) { notebookTopics(data) }
    val pages = remember(data) { StudyNotebook.folheada(topics, now, daySeed()) }
    val flippedToday = readLocal(FLIP_DONE_KEY) == daySeed().toString()
    val subjects = data.subjects.associateBy { it.id }
    val bySubject = topics.groupBy { it.subjectId }.entries.sortedByDescending { (_, list) -> list.size }
    var open by remember { mutableStateOf<Long?>(null) }
    val last = readLocal(LAST_PAGE_KEY)?.toLongOrNull()?.let { id -> topics.firstOrNull { it.id == id } }
    val name = br.com.estudario.web.ui.FolhaFriend.firstName()

    NotebookTabs(saved = false)
    CompetitionTabs()
    Div({ classes("cb-binder", "rise") }) {
        Div({ classes("cb-rings") }) { repeat(5) { org.jetbrains.compose.web.dom.I() } }
        Div({ classes("cb-hello") }) {
            Folha(if (topics.isEmpty()) "thinking" else "happy", 64)
            Div {
                H2 { Text("${greeting()}${name?.let { ", $it" } ?: ""}!") }
                P { Text(if (topics.isEmpty()) "Seu caderno se escreve sozinho: cada tópico que você estudar vira página aqui." else "Você já estudou ${topics.size} ${if (topics.size == 1) "tópico" else "tópicos"}. Bora não deixar esfriar?") }
            }
        }
        if (pages.isNotEmpty()) Div({ classes("cb-today") }) {
            Span({ classes("cb-eyebrow") }) { Text(if (flippedToday) "FOLHEADA DE HOJE · FEITA" else "FOLHEADA DO DIA · ~${StudyNotebook.minutes(pages)} MIN") }
            H3 { Text(if (flippedToday) "Feita! Quer folhear de novo?" else "${pages.size} ${if (pages.size == 1) "página separada" else "páginas separadas"} para você") }
            P { Text("Do que você viu e está começando a esquecer.") }
            Button({ classes("cb-cta"); onClick { Router.go(Route.NotebookFlip) } }) { Icon("auto_stories", filled = true); Text(if (flippedToday) "Folhear de novo" else "Folhear agora") }
        } else Div({ classes("cb-today", "empty") }) {
            Span({ classes("cb-eyebrow") }) { Text("FOLHEADA DO DIA") }
            H3 { Text("Estude o primeiro tópico") }
            P { Text("Leia a teoria e marque como estudado: o resumo, as dicas, as pegadinhas e os cartões viram páginas do seu caderno.") }
            Button({ classes("cb-cta"); onClick { Router.go(Route.Edital) } }) { Icon("menu_book", filled = true); Text("Ir para o edital") }
        }
        if (last != null) Div({ classes("cb-resume"); onClick { Router.go(Route.NotebookPage(last.id)) } }) {
            Span({ classes("cb-rib") })
            Div { B { Text("Continuar de onde parou") }; Span { Text(last.title) } }
        }
        if (bySubject.isNotEmpty()) Div({ classes("cb-sect") }) { B { Text("Suas matérias") }; Span { Text("toque para abrir") } }
        Div({ classes("cb-shelf") }) {
            bySubject.forEach { (subjectId, list) ->
                key(subjectId) {
                    val subjectName = subjects[subjectId]?.name ?: "Matéria"
                    val firm = list.map { StudyNotebook.firmness(it, now) }
                    val cold = firm.count { it == StudyNotebook.Firmness.COLD }
                    Div({ classes("cb-tab"); attr("style", "--c:${subjectColor(subjectName)}"); onClick { open = if (open == subjectId) null else subjectId } }) {
                        Span({ classes("hs-art", "subject-art", "cb-art"); attr("style", "--c:${subjectColor(subjectName)}") }) { Icon(br.com.estudario.domain.catalog.SubjectArt.iconFor(subjectName)) }
                        Div({ classes("cb-meta") }) {
                            B { Text(subjectName) }
                            Span { Text("${list.size} ${if (list.size == 1) "tópico visto" else "tópicos vistos"} · ${firm.count { it == StudyNotebook.Firmness.FIRM }} firmes") }
                            Div({ classes("cb-firm") }) { firm.sortedBy { it.ordinal }.forEach { f -> org.jetbrains.compose.web.dom.I({ classes(firmClass(f)) }) } }
                        }
                        if (cold > 0) Span({ classes("cb-cold") }) { Text("$cold esfriando") }
                        Icon(if (open == subjectId) "expand_less" else "expand_more")
                    }
                    if (open == subjectId) Div({ classes("cb-topics") }) {
                        list.sortedBy { it.lastSeenAt ?: 0 }.forEach { t ->
                            val f = StudyNotebook.firmness(t, now)
                            Div({ classes("cb-topic"); onClick { Router.go(Route.NotebookPage(t.id)) } }) {
                                org.jetbrains.compose.web.dom.I({ classes("cb-dot", firmClass(f)) })
                                Span({ classes("grow") }) { Text(t.title) }
                                t.lastSeenAt?.let { Span({ classes("xs", "muted") }) { Text(sinceLabel(it, now)) } }
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun sinceLabel(at: Long, now: Long): String {
    val d = ((now - at) / 86_400_000L).toInt()
    return when { d <= 0 -> "hoje"; d == 1 -> "ontem"; else -> "há $d dias" }
}

private fun pageKind(p: StudyNotebook.Page): Pair<String, String> = when (p) {
    is StudyNotebook.Page.Key -> "k" to "Ponto-chave"
    is StudyNotebook.Page.MarkPage -> "m" to "Seu grifo"
    is StudyNotebook.Page.Trap -> "p" to "Pegadinha"
    is StudyNotebook.Page.Recall -> "c" to "Lembrar"
    is StudyNotebook.Page.Question -> "q" to "Questão"
}

// ------------------------------------------------------------------ a Folheada do dia

@Composable
fun NotebookFlipScreen() {
    val data = Store.data
    val now = remember { Date.now().toLong() }
    val topics = remember { notebookTopics(data) }
    val pages = remember { StudyNotebook.folheada(topics, now, daySeed()) }
    val byId = topics.associateBy { it.id }
    val subjects = data.subjects.associateBy { it.id }
    var index by remember { mutableStateOf(0) }
    val sessionId = remember { Actions.newSessionId() }
    val started = remember { Date.now() }
    // As questões da folheada contam o tempo no Desempenho, como uma bateria (tipo QUICK, o mesmo do app).
    val answers = remember { androidx.compose.runtime.mutableStateListOf<Pair<Long, Boolean>>() }
    var answersSaved by remember { mutableStateOf(false) }
    fun saveAnswers() {
        if (answersSaved || answers.isEmpty()) return
        answersSaved = true
        val topicIds = answers.mapTo(hashSetOf()) { it.first }
        val subjectIds = Store.data.topics.filter { it.id in topicIds }.mapTo(hashSetOf()) { it.subjectId }
        Store.update { Actions.saveQuestionSession(it, "QUICK", started.toLong(), answers.size, answers.count { a -> a.second }, subjectIds, topicIds, sessionId) }
    }
    androidx.compose.runtime.DisposableEffect(Unit) { onDispose { saveAnswers() } }
    if (pages.isEmpty()) { Card { Empty("auto_stories", "Nada para folhear ainda", "Estude um tópico e ele vira página do seu caderno.", action = { Btn("Voltar ao caderno", { Router.go(Route.Notebook) }) }) }; return }
    val done = index >= pages.size
    Div({ classes("cb-flip") }) {
        Div({ classes("cb-flip-top") }) {
            Btn("Fechar", { Router.go(Route.Notebook) }, style = "ghost", small = true, icon = "close")
            B { Text("Folheada do dia") }
            Span({ classes("cb-hand") }) { Text(if (done) "pronto" else "${index + 1} de ${pages.size}") }
        }
        Div({ classes("cb-dots") }) { pages.indices.forEach { i -> org.jetbrains.compose.web.dom.I({ classes(*listOfNotNull(if (i <= index) "on" else null).toTypedArray()) }) } }
        if (done) {
            DisposableOnce { writeLocal(FLIP_DONE_KEY, daySeed().toString()); saveAnswers() }
            Div({ classes("cb-done", "rise") }) {
                Folha("proud", 110)
                H2 { Text("Folheada feita!") }
                val minutes = ((Date.now() - started) / 60_000).toInt().coerceAtLeast(1)
                val names = pages.map { it.topicId }.distinct().mapNotNull { byId[it]?.title }.take(3)
                P { Text("${pages.size} páginas em $minutes ${if (minutes == 1) "minuto" else "minutos"}. ${joinNames(names)} voltaram para a memória.") }
                Div({ classes("row", "wrap"); attr("style", "gap:8px;justify-content:center") }) {
                    Btn("Voltar ao caderno", { Router.go(Route.Notebook) }, style = "primary", icon = "auto_stories")
                }
            }
            return@Div
        }
        val page = pages[index]
        val topic = byId[page.topicId]
        key(index) {
            Div({ classes("cb-page", "turn-in") }) {
                val (cls, label) = pageKind(page)
                Span({ classes("cb-tag", cls) }) { Text(label.uppercase()) }
                P({ classes("cb-sub") }) { Text(listOfNotNull(topic?.let { subjects[it.subjectId]?.name }, topic?.title).joinToString(" · ")) }
                when (page) {
                    is StudyNotebook.Page.Key -> {
                        H3({ classes("cb-title") }) { Text(if (page.lines.size == 1) "Dica de prova" else "O que não pode esquecer") }
                        Div({ classes("cb-body") }) { page.lines.forEach { P { Inline(it) } } }
                    }
                    is StudyNotebook.Page.MarkPage -> {
                        H3({ classes("cb-title") }) { Text("O que você destacou") }
                        MarkQuote(page.mark)
                    }
                    is StudyNotebook.Page.Trap -> {
                        H3({ classes("cb-title") }) { Text("A banca tenta te pegar aqui") }
                        Div({ classes("cb-body") }) { P { Inline(page.text) } }
                        Span({ classes("cb-stamp") }) { Text("PEGADINHA") }
                    }
                    is StudyNotebook.Page.Recall -> FlipRecall(page)
                    is StudyNotebook.Page.Question -> FlipQuestion(page.questionId, sessionId) { ok -> answers += page.topicId to ok }
                }
                Div({ classes("cb-page-foot") }) {
                    Button({ classes("cb-link"); onClick { Router.go(Route.NotebookPage(page.topicId)) } }) { Icon("description"); Text("Abrir a página do tópico") }
                    Button({ classes("cb-link"); onClick { Reports.report(ReportKindWeb.OTHER, StudyNotebook.pageText(page), topic?.title) } }) { Icon("flag"); Text("Tem erro aqui") }
                }
            }
        }
        Div({ classes("cb-flip-nav") }) {
            if (index > 0) Btn("Voltar", { index-- }, style = "ghost", icon = "arrow_back")
            Btn(if (index == pages.lastIndex) "Terminar" else "Virar página", { index++ }, style = "primary", icon = "arrow_forward")
        }
    }
}


@Composable
private fun FlipRecall(page: StudyNotebook.Page.Recall) {
    var flipped by remember(page) { mutableStateOf(false) }
    H3({ classes("cb-title") }) { Text("Tente lembrar antes de virar") }
    Div({ classes("cb-index", *listOfNotNull(if (flipped) "on" else null).toTypedArray()); attr("role", "button"); attr("tabindex", "0"); onClick { flipped = !flipped } }) {
        Div({ classes("cb-index-in") }) {
            Div({ classes("cb-face", "front") }) { B { Inline(page.question) }; Span { Text("toque na ficha para ver a resposta") } }
            Div({ classes("cb-face", "back") }) { Div { Markdown(page.answer, compact = true) } }
        }
    }
}

@Composable
private fun FlipQuestion(questionId: Long, sessionId: String, onAnswered: (Boolean) -> Unit) {
    val question = Store.data.questions.firstOrNull { it.id == questionId }
    if (question == null) { P { Text("Esta questão não está mais no banco.") }; return }
    var picked by remember(questionId) { mutableStateOf<String?>(null) }
    var right by remember(questionId) { mutableStateOf<Boolean?>(null) }
    H3({ classes("cb-title") }) { Text("Uma que você errou: e agora?") }
    Div({ classes("cb-body") }) { QuestionStatement(question.statement) }
    AnswerOptions(question.id, question.options, picked, right, onPick = { picked = it })
    if (right == null) ConfirmAnswer(picked != null) {
        val key = picked ?: return@ConfirmAnswer
        var ok = false
        Store.update { s -> Actions.answer(s, question.id, key, sessionId).also { ok = it.second }.first }
        right = ok
        som(if (ok) "acerto" else "erro")
        onAnswered(ok)
    }
    if (right != null) {
        Div({ classes("cb-postit", if (right == true) "ok" else "bad") }) { Text(if (right == true) "Agora foi! Ela sai do caderno de erros quando você acertar de novo." else "Ainda não. Leia a explicação e ela volta depois.") }
        if (question.explanation.isNotBlank()) Div({ classes("cb-body"); attr("style", "margin-top:8px") }) { Markdown(question.explanation, compact = true) }
    }
}

/** Roda [block] uma vez quando aparece. */
@Composable
private fun DisposableOnce(block: () -> Unit) { androidx.compose.runtime.LaunchedEffect(Unit) { block() } }

// ------------------------------------------------------------------ a página do tópico

@Composable
fun NotebookPageScreen(topicId: Long) {
    val data = Store.data
    val now = Date.now().toLong()
    val topic = remember(data, topicId) { notebookTopics(data).firstOrNull { it.id == topicId } }
    val raw = data.topics.firstOrNull { it.id == topicId }
    androidx.compose.runtime.LaunchedEffect(topicId) { writeLocal(LAST_PAGE_KEY, topicId.toString()) }
    if (topic == null || raw == null) {
        Card { Empty("description", "Esta página ainda está em branco", "Estude o tópico (leia a teoria e marque como estudado) e ele vira página do seu caderno.", action = {
            Div({ classes("row", "wrap"); attr("style", "gap:8px;justify-content:center") }) {
                Btn("Voltar ao caderno", { Router.go(Route.Notebook) }, style = "outline")
                if (raw != null) Btn("Abrir o tópico", { Router.go(Route.Topic(topicId)) }, style = "primary", icon = "menu_book")
            }
        }) }
        return
    }
    val subject = data.subjects.firstOrNull { it.id == topic.subjectId }
    val lines = StudyNotebook.summaryLines(topic.summary, max = 14)
    val quotes = topic.marks.map { it.quote.trim() }.filter { it.length >= 8 }
    val thin = lines.size < 3 && topic.tips.size < 3 && topic.recall.size < 4
    Div({ classes("cb-top") }) {
        Btn("Caderno", { Router.go(Route.Notebook) }, style = "ghost", small = true, icon = "arrow_back")
    }
    Div({ classes("cb-tpage", "rise"); attr("style", "--c:${subjectColor(subject?.name ?: "")}") }) {
        Span({ classes("cb-crumb") }) { Text((subject?.name ?: "").uppercase()) }
        H2 { Text(topic.title) }
        Div({ classes("cb-chips") }) {
            topic.lastSeenAt?.let { Span { Text("visto ${sinceLabel(it, now)}") } }
            if (topic.marks.isNotEmpty()) Span { Text("${topic.marks.size} ${if (topic.marks.size == 1) "grifo" else "grifos"}") }
            if (topic.notes.isNotEmpty()) Span { Text("${topic.notes.size} ${if (topic.notes.size == 1) "nota" else "notas"}") }
            if (topic.wrongQuestions.isNotEmpty()) Span({ classes("w") }) { Text("${topic.wrongQuestions.size} ${if (topic.wrongQuestions.size == 1) "erro" else "erros"} para refazer") }
        }
        if (thin) Div({ classes("cb-thin") }) {
            Icon("info", filled = true)
            Span { Text("Este tópico veio com pouco material. Gerar de novo traz a matéria completa.") }
            Btn("Abrir o tópico", { Router.go(Route.Topic(topic.id)) }, style = "outline", small = true)
        }
        if (lines.isNotEmpty()) {
            H4 { Text("O ESSENCIAL") }
            Div({ classes("cb-body") }) {
                lines.forEach { line ->
                    // o trecho que a pessoa grifou aparece em marca-texto dentro da frase
                    val hit = quotes.firstOrNull { q -> line.contains(q.take(60), ignoreCase = true) }
                    P { if (hit != null) Span({ classes("cb-hl") }) { Inline(line) } else Inline(line) }
                }
            }
        }
        if (quotes.isNotEmpty()) {
            H4 { Text("SEUS GRIFOS") }
            topic.marks.take(8).forEachIndexed { i, m -> key(i) { MarkQuote(m) } }
        }
        topic.notes.take(3).forEach { note -> Div({ classes("cb-postit") }) { Text(note) } }
        if (topic.tips.isNotEmpty()) {
            H4 { Text("DICAS DE PROVA") }
            Div({ classes("cb-body") }) { org.jetbrains.compose.web.dom.Ul { topic.tips.take(6).forEach { org.jetbrains.compose.web.dom.Li { Inline(it) } } } }
        }
        topic.traps.take(3).forEach { trap ->
            Div({ classes("cb-trap") }) { Span({ classes("cb-stamp") }) { Text("PEGADINHA") }; P { Inline(trap) } }
        }
        if (topic.recall.isNotEmpty()) {
            H4 { Text("PARA LEMBRAR · TOQUE PARA VIRAR") }
            Div({ classes("cb-cards") }) {
                topic.recall.take(10).forEachIndexed { i, (q, a) -> key(i) { MiniCard(q, a) } }
            }
        }
        Div({ classes("cb-foot") }) {
            Btn("Ler a teoria", { Router.go(Route.Topic(topic.id)) }, style = "outline", icon = "menu_book")
            if (data.questions.any { it.topicId == topic.id && !it.hidden }) Btn("Treinar 5 questões", { Router.go(Route.Quiz("topico-${topic.id}-5")) }, style = "primary", icon = "quiz")
        }
        Button({ classes("cb-link"); attr("style", "margin-top:10px"); onClick { Reports.report(ReportKindWeb.SUMMARY, lines.take(3).joinToString(" "), topic.title) } }) { Icon("flag"); Text("Tem erro nesta página") }
    }
}

@Composable
private fun MiniCard(front: String, back: String) {
    var on by remember { mutableStateOf(false) }
    Div({ classes("cb-mini", *listOfNotNull(if (on) "on" else null).toTypedArray()); onClick { on = !on } }) {
        Div({ classes("cb-mini-in") }) {
            Div({ classes("cb-mini-f") }) { Span { Inline(front) } }
            Div({ classes("cb-mini-b") }) { Span { Inline(back.take(260)) } }
        }
    }
}

/**
 * Um grifo: o trecho como citação (barra amarela, fundo leve), em markdown de verdade (tabela vira
 * tabela, lista vira lista) e com a nota da pessoa junto, para ficar claro de que trecho ela é.
 * Grifo longo vem recolhido, com "ver o trecho inteiro".
 */
@Composable
private fun MarkQuote(mark: StudyNotebook.Mark) {
    val md = remember(mark.quote) { StudyNotebook.quoteMarkdown(mark.quote) }
    val long = md.length > 360 || md.count { it == '\n' } > 6
    var open by remember(mark.quote) { mutableStateOf(false) }
    Div({ classes(*listOfNotNull("cb-quote", if (long && !open) "clamp" else null).toTypedArray()) }) {
        Div({ classes("cb-quote-in") }) { Markdown(md, compact = true) }
        if (long) Button({ classes("cb-link", "cb-quote-more"); onClick { open = !open } }) { Text(if (open) "recolher" else "ver o trecho inteiro") }
        if (mark.note.isNotBlank()) Div({ classes("cb-quote-note") }) { Span { Text("✎ sua nota") }; Text(mark.note) }
    }
}

/**
 * As abas do Caderno: o caderno que se escreve sozinho e o que a pessoa guardou (grifos, notas,
 * questões e cartões salvos). Antes o segundo ficava num link no fim da página.
 */
@Composable
internal fun NotebookTabs(saved: Boolean) {
    val data = Store.data
    val guardados = data.theoryMarks.size + data.notes.size
    Div({ classes("cb-tabs"); attr("role", "tablist") }) {
        Button({ classes(*listOfNotNull("cb-tabbtn", if (!saved) "on" else null).toTypedArray()); attr("role", "tab"); attr("aria-selected", (!saved).toString()); onClick { Router.go(Route.Notebook) } }) {
            Icon("auto_stories", filled = true); Span { Text("Caderno") }
        }
        Button({ classes(*listOfNotNull("cb-tabbtn", if (saved) "on" else null).toTypedArray()); attr("role", "tab"); attr("aria-selected", saved.toString()); onClick { Router.go(Route.NotebookSaved) } }) {
            Icon("bookmarks", filled = true); Span { Text("Grifos, notas e salvos") }
            if (guardados > 0) B({ classes("cb-tabcount") }) { Text("$guardados") }
        }
    }
}
