package br.com.estudario.web.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import br.com.estudario.data.local.TopicStatus
import br.com.estudario.domain.MasteryCalculator
import br.com.estudario.domain.MasteryInput
import br.com.estudario.web.Route
import br.com.estudario.web.Router
import br.com.estudario.web.data.Actions
import br.com.estudario.web.data.Keys
import br.com.estudario.web.data.Queries
import br.com.estudario.web.data.Store
import br.com.estudario.web.data.Summary
import br.com.estudario.web.data.Theory
import br.com.estudario.web.data.jsonOf
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.delay
import org.jetbrains.compose.web.dom.B
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H2
import org.jetbrains.compose.web.dom.H3
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.dom.TextArea
import kotlin.js.Date

// ------------------------------------------------------------------ blocos do texto (studyBlocks do app)

/** Divide a teoria em blocos sem quebrar tabela, código ou fórmula; é a mesma unidade que o app usa para "% lido". */
fun studyBlocks(markdown: String): List<String> {
    val blocks = mutableListOf<String>()
    val current = StringBuilder()
    var fence: String? = null // ``` ou ~~~ aberto
    var inMath = false
    var inTable = false
    fun flush() {
        val block = current.toString().trim('\n')
        if (block.isNotBlank()) blocks += block
        current.clear()
    }
    // As fontes ficam só na aba Fontes, não no meio da teoria (como no app).
    MathText.normalize(br.com.estudario.text.SourcesSection.strip(markdown)).lines().forEach { line ->
        val trimmed = line.trim()
        val mark = trimmed.take(3).takeIf { it == "```" || it == "~~~" }
        // Bloco numa linha só (~~~grafico {...} ~~~) abre e fecha na mesma linha.
        if (mark != null && (fence == null || fence == mark)) { if (fence == null && !(trimmed.length > 6 && trimmed.endsWith(mark))) fence = mark else fence = null }
        else if (fence == null && trimmed == "$$") inMath = !inMath
        inTable = trimmed.startsWith("|")
        if (trimmed.isEmpty() && fence == null && !inMath) flush() else current.append(line).append('\n')
    }
    flush()
    fun heading(block: String) = block.trim().takeIf { it.startsWith("#") && '\n' !in it }?.trimStart('#')?.trim()?.trim('*')?.trim()?.lowercase()
    return blocks.filterIndexed { i, block -> i == 0 || heading(block)?.let { it != heading(blocks[i - 1]) } ?: true }
}

/**
 * Fontes citadas no meio do texto, como "([kernel.org](https://...))" ou "(https://...)": saem do
 * parágrafo e vão para a lista do fim da teoria. Só os parênteses que têm apenas links saem; link
 * que faz parte da frase continua onde está.
 */
object InlineSources {
    private val link = Regex("""\[([^\]]+)\]\((https?://[^)\s]+)\)|<?(https?://[^\s<>()]+)>?""")
    private val citation = Regex("""(?<!\])\s*\(\s*(?:(?:\[[^\]]+\]\(https?://[^)\s]+\)|<?https?://[^\s<>()]+>?)\s*[;,]?\s*)+\)""")

    fun extract(block: String): Pair<String, List<Pair<String, String>>> {
        val found = mutableListOf<Pair<String, String>>()
        val cleaned = citation.replace(block) { m ->
            link.findAll(m.value).forEach { l ->
                val url = (l.groupValues[2].ifBlank { l.groupValues[3] }).trimEnd('.', ',', ';')
                val label = l.groupValues[1].ifBlank { url.substringAfter("://").substringBefore('/').removePrefix("www.") }
                found += label to url
            }
            ""
        }
        return cleaned to found
    }
}

private fun chaptersOf(blocks: List<String>): List<Pair<Int, String>> =
    blocks.withIndex().filter { (i, b) -> b.trimStart().startsWith("## ") || (b.trimStart().startsWith("# ") && i > 0) }
        .map { it.index to it.value.trimStart().trimStart('#').trim().lineSequence().first().trim('*').trim() }

private fun readPercent(theory: Theory): Int {
    val total = studyBlocks(theory.markdown).size.coerceAtLeast(1)
    return if (theory.lastReadBlock < 0) 0 else ((theory.lastReadBlock + 1) * 100 / total).coerceIn(0, 100)
}

private val trapWords = listOf("pegadinha", "atenção", "atencao", "cuidado", "não confunda", "nao confunda", "erro comum", "armadilha")
private val tipWords = listOf("dica", "bizu", "macete", "lembre", "memorize", "resumindo", "na prova")

/** calloutKindOf do app: citação que na verdade é dica (false) ou alerta (true). */
private fun calloutTrap(block: String): Boolean? {
    val text = block.trimStart()
    if (!text.startsWith(">")) return null
    val head = text.removePrefix(">").trimStart('*', '_', ' ', '#').take(40).lowercase()
    return when {
        trapWords.any { head.startsWith(it) || head.contains("$it:") } -> true
        tipWords.any { head.startsWith(it) || head.contains("$it:") } -> false
        else -> null
    }
}

// ------------------------------------------------------------------ baralho (FlashcardParser do app)

data class DeckCard(val front: String, val back: String)

fun deckCards(markdown: String): List<DeckCard> {
    val lines = markdown.replace("\r\n", "\n").trim().lines()
    val headings = lines.indices.filter { lines[it].trimStart().startsWith("##") }
    if (headings.isNotEmpty()) return headings.mapIndexedNotNull { pos, start ->
        val end = headings.getOrNull(pos + 1) ?: lines.size
        val front = lines[start].trimStart().trimStart('#').trim()
        front.takeIf { it.isNotBlank() }?.let { DeckCard(it, lines.subList(start + 1, end).joinToString("\n").trim()) }
    }
    val term = Regex("""^\*\*(.+?)\*\*\s*[:—–-]\s*(.+)$""")
    val plain = Regex("""^([^:]{2,80}):\s+(.+)$""")
    return lines.mapNotNull { raw ->
        val line = raw.trim().replace(Regex("""^([-*+]|\d+[.)])\s+"""), "")
        if (line.isBlank() || line.startsWith("#")) return@mapNotNull null
        term.find(line)?.let { DeckCard(it.groupValues[1].trim(), it.groupValues[2].trim()) }
            ?: plain.find(line)?.let { DeckCard(it.groupValues[1].trim(), it.groupValues[2].trim()) }
    }
}

// ------------------------------------------------------------------ foco do tópico (fica rodando enquanto a pessoa lê)


// ------------------------------------------------------------------ tela do tópico

private val topicTabs = listOf("Teoria", "Revisão", "Dicas", "Questões", "Erros", "Histórico")

@Composable
fun TopicScreen(topicId: Long) {
    val data = Store.data
    val topic = data.topics.firstOrNull { it.id == topicId }
    if (topic == null) {
        Card { Empty("search_off", "Tópico não encontrado", "Ele pode ter sido apagado no app.", action = { Btn("Voltar ao edital", { Router.go(Route.Edital) }) }) }
        return
    }
    val subject = data.subjects.firstOrNull { it.id == topic.subjectId }
    val parent = topic.parentTopicId?.let { id -> data.topics.firstOrNull { it.id == id } }
    val children = data.topics.filter { it.parentTopicId == topicId }.sortedBy { it.position }
    val theories = data.theories.filter { it.topicId == topicId }
    val summaries = data.summaries.filter { it.topicId == topicId }
    val snippets = data.snippets.filter { it.topicId == topicId }.sortedBy { it.position }
    val recall = snippets.filter { it.kind == "RECUPERACAO" && it.externalId?.startsWith("flashcard:") != true }
    val tips = snippets.filter { it.kind != "RECUPERACAO" }
    val questions = data.questions.filter { it.topicId == topicId && !it.hidden && it.simulationId == null }
    val questionIds = questions.mapTo(hashSetOf()) { it.id }
    val attempts = data.attempts.filter { it.questionId in questionIds }
    val now = Date.now().toLong()
    val recent = attempts.filter { now - it.answeredAt < 30L * 86_400_000 }
    val concepts = data.errorConcepts.filter { it.topicId == topicId }.sortedWith(compareByDescending<br.com.estudario.web.data.ErrorConcept> { it.errorCount }.thenByDescending { it.lastErrorAt ?: 0 })
    val reviews = data.reviews.filter { it.topicId == topicId }
    val studied = Queries.isStudied(topic)
    val status = runCatching { TopicStatus.valueOf(topic.status) }.getOrDefault(TopicStatus.NAO_ESTUDADO)
    val lastContact = listOfNotNull(topic.lastStudiedAt, topic.lastReviewedAt, attempts.maxOfOrNull { it.answeredAt }).maxOrNull() ?: now
    val mastery = MasteryCalculator.percent(
        MasteryInput(
            status, attempts.size, attempts.count { it.correct }, recent.count { !it.correct }, reviews.count { it.completedAt != null },
            recent.size, recent.count { it.correct }, 0, reviews.count { it.completedAt == null && it.ignoredAt == null && it.dueAt < now }, ((now - lastContact) / 86_400_000L).toInt(),
        ),
    )
    val accuracy = if (attempts.isEmpty()) null else attempts.count { it.correct } * 100 / attempts.size
    val firstTheory = theories.firstOrNull()
    val reading = firstTheory?.let { readPercent(it) }
    val plan = Queries.activePlan(data)
    val inPlanToday = plan?.let { p -> Queries.tasksOn(data, p.id, Queries.todayEpoch()).any { it.topicId == topicId } } ?: false

    var tab by remember(topicId) { mutableStateOf(0) }
    var generating by remember(topicId) { mutableStateOf(false) }
    var generatingQuestions by remember(topicId) { mutableStateOf(false) }
    var menuOpen by remember(topicId) { mutableStateOf(false) }
    var finishAsk by remember(topicId) { mutableStateOf(false) }
    var deletingMaterial by remember(topicId) { mutableStateOf(false) }
    val hasMaterial = theories.isNotEmpty() || summaries.isNotEmpty() || snippets.isNotEmpty()
    val focusHere = TopicFocus.topicId == topicId

    DisposableEffect(focusHere) {
        val handle = if (focusHere) window.setInterval({ TopicFocus.now = Date.now().toLong() }, 1000) else null
        onDispose { handle?.let { window.clearInterval(it) } }
    }

    Div({ classes("row") }) { Btn("Meus concursos", { Router.go(Route.Edital) }, style = "ghost", small = true, icon = "arrow_back") }

    // ---------------------------------------------------------------- cabeçalho
    Div({ classes("topic-head") }) {
        Div({ classes("grow"); attr("style", "min-width:0") }) {
            Div({ classes("crumb") }) { Text(listOfNotNull(subject?.name, parent?.title).joinToString("  ›  ").uppercase()) }
            H2({ classes("topic-title") }) { Text(topic.title) }
            Div({ classes("row", "wrap"); attr("style", "gap:8px;margin-top:8px") }) {
                Span({ classes("tchip", if (studied) "ok" else "muted") }) { Icon(if (studied) "check_circle" else "radio_button_unchecked"); Text(if (studied) "Estudado" else "Não estudado") }
                Span({ classes("tchip", "primary") }) { Icon("flag"); Text("Prioridade ${priorityLabel(topic.priority)}") }
                if (inPlanToday) Span({ classes("tchip", "tertiary") }) { Icon("event"); Text("No plano de hoje") }
            }
        }
        Div({ attr("style", "position:relative") }) {
            IconButton("more_vert", "Mais ações") { menuOpen = !menuOpen }
            if (menuOpen) Div({ classes("menu-pop") }) {
                if (children.isEmpty() && !Store.demo) MenuItem("auto_awesome", "Gerar material com o Estudário") { menuOpen = false; generating = true }
                if (studied) MenuItem("undo", "Desmarcar como estudado") { menuOpen = false; Store.update { Actions.unmarkStudied(it, topicId) } }
                if (hasMaterial) MenuItem("flag", "Reportar erro no material") { menuOpen = false; Reports.report(if (theories.isNotEmpty()) ReportKindWeb.THEORY else ReportKindWeb.OTHER, theories.firstOrNull()?.let { it.title + ": " + it.markdown.take(600) } ?: topic.title, topic.title) }
                if (hasMaterial) MenuItem("forum", "Comentar o material") { menuOpen = false; Reports.comment(if (theories.isNotEmpty()) ReportKindWeb.THEORY else ReportKindWeb.OTHER, topic.title, topic.title) }
                if (hasMaterial && !Store.demo) MenuItem("delete", "Apagar material") { menuOpen = false; deletingMaterial = true }
                MenuItem("close", "Fechar") { menuOpen = false }
            }
        }
    }

    if (deletingMaterial) {
        val answeredIds = attempts.mapTo(hashSetOf()) { it.questionId }
        DeleteMaterialModal(
            topicTitle = topic.title, theories = theories.size, summaries = summaries.size, snippets = snippets.size,
            unanswered = questions.count { it.id !in answeredIds }, answered = questions.count { it.id in answeredIds },
            onDismiss = { deletingMaterial = false },
        ) { withQuestions ->
            deletingMaterial = false
            Store.update { Actions.deleteTopicMaterial(it, topicId, withQuestions) }
            tab = 0
            Toast.show("Material apagado. O tópico e seu histórico continuam aqui.")
        }
    }

    // ---------------------------------------------------------------- progresso e ação principal
    Div({ classes("topic-stats") }) {
        TopicStat("$mastery%", "Domínio · ${MasteryCalculator.label(mastery).lowercase()}")
        TopicStat(reading?.let { "$it%" } ?: "-", "Leitura")
        TopicStat(accuracy?.let { "$it%" } ?: "-", if (attempts.isEmpty()) "Acerto" else "Acerto · ${attempts.size}")
    }
    val resume = theories.firstOrNull { it.lastReadBlock >= 0 && it.lastReadBlock < studyBlocks(it.markdown).lastIndex } ?: theories.firstOrNull { it.lastReadBlock < 0 }
    val (label, icon, action) = when {
        children.isNotEmpty() -> Triple("Ver os ${children.size} subtópicos", "account_tree") { tab = 0 }
        resume != null && resume.lastReadBlock >= 0 -> Triple("Continuar leitura · ${readPercent(resume)}%", "menu_book") { tab = 0; scrollToBlock(resume.id, resume.lastReadBlock) }
        resume != null -> Triple("Começar a teoria", "menu_book") { tab = 0; scrollToBlock(resume.id, 0) }
        questions.isNotEmpty() -> Triple("Treinar ${questions.size} ${if (questions.size == 1) "questão" else "questões"}", "quiz") { Router.go(Route.Quiz("topico-$topicId")) }
        else -> Triple("Gerar material com o Estudário", "auto_awesome") { if (!Store.demo) generating = true }
    }
    Btn(label, action, style = "primary", icon = icon, block = true)
    Div({ classes("grid", "cols-2"); attr("style", "gap:10px") }) {
        if (focusHere) Btn(if (TopicFocus.clockHidden) "Encerrar foco" else "Encerrar foco · ${TopicFocus.clock()}", { TopicFocus.stop() }, style = "primary", icon = "timer", block = true)
        else Btn("Modo foco", {
            if (TopicFocus.topicId != null) TopicFocus.stop()
            TopicFocus.start(topicId, topic.title)
            Toast.show("Foco ligado. O tempo conta enquanto você estuda aqui.")
        }, style = "tonal", icon = "timer", block = true)
        // Tópico já estudado com tarefa pendente hoje é revisão: dá para concluir, como no app.
        val reviewTask = if (!studied || plan == null) null else Queries.tasksOn(data, plan.id, Queries.todayEpoch())
            .firstOrNull { it.topicId == topicId && it.status in setOf("PLANEJADA", "EM_ANDAMENTO") }
        if (reviewTask != null) Btn("Concluir revisão", {
            if (focusHere) TopicFocus.stop()
            Store.update { snapshot ->
                val withTask = Actions.completeTask(snapshot, reviewTask.id, reviewTask.minutes)
                withTask.reviews
                    .filter { it.topicId == topicId && it.completedAt == null && it.ignoredAt == null }
                    .minByOrNull { it.dueAt }
                    ?.let { Actions.completeReview(withTask, it.id, br.com.estudario.data.local.ReviewDifficulty.NORMAL) }
                    ?: withTask
            }
            Toast.show("Revisão concluída.")
        }, style = "outline", icon = "check", block = true)
        else if (studied) Btn("Concluído", {}, style = "outline", icon = "check", block = true, enabled = false)
        else Btn("Concluir estudo", { finishAsk = true }, style = "outline", icon = "check", block = true)
    }
    if (finishAsk) Modal(onDismiss = { finishAsk = false }) {
        H2 { Text("Estudo concluído hoje") }
        P({ classes("muted") }) { Text("O tópico vira estudado e as revisões D+1, D+7 e D+30 entram na agenda.") }
        Div({ classes("row"); attr("style", "justify-content:flex-end") }) {
            Btn("Cancelar", { finishAsk = false }, style = "ghost")
            Btn("Confirmar", {
                if (focusHere) TopicFocus.stop()
                Store.update { Actions.completeStudyWithPlan(it, topicId) }
                finishAsk = false
                Toast.show("Estudo registrado. Revisões D+1, D+7 e D+30 na agenda.")
            }, style = "primary", icon = "check")
        }
    }
    if (generating) GenerateDialog(topicId) { generating = false }
    if (generatingQuestions) GenerateDialog(topicId, questionsOnly = true) { generatingQuestions = false }

    // ---------------------------------------------------------------- abas
    val counts = listOf(theories.size, summaries.size + recall.size, tips.size, questions.size, concepts.size, 0)
    Div({ classes("topic-tabs"); attr("role", "tablist") }) {
        topicTabs.forEachIndexed { index, name ->
            Button({ classes(*listOfNotNull("ttab", if (tab == index) "on" else null).toTypedArray()); attr("role", "tab"); onClick { tab = index } }) {
                Text(name)
                if (counts[index] > 0) Span({ classes("count") }) { Text("${counts[index]}") }
            }
        }
    }

    when (tab) {
        0 -> {
            if (children.isNotEmpty()) Card {
                CardHead("Dividido em ${children.size} subtópicos")
                P({ classes("small", "muted"); attr("style", "margin:-6px 0 10px") }) { Text("No edital este item junta várias matérias. O material é gerado em cada subtópico, para sair completo.") }
                children.forEach { child ->
                    key(child.id) {
                        Button({ classes("pick-row"); onClick { Router.go(Route.Topic(child.id)) } }) {
                            Icon(if (Queries.isStudied(child)) "check_circle" else "subdirectory_arrow_right", extraClass = "primary-ink")
                            Span({ classes("grow") }) { Text(child.title) }
                            Icon("chevron_right", extraClass = "faint")
                        }
                    }
                }
            } else if (theories.isEmpty()) EmptyGenerate("menu_book", "Ainda não tem teoria aqui", "Escolha o que quer receber (teoria, resumo, flashcards, questões) e o Estudário gera para este tópico.") { generating = true }
            theories.forEach { theory -> key(theory.id) { TheoryReader(theory) } }
            TopicSources(topicId)
            TopicNotes(topicId)
        }
        1 -> ReviewTab(summaries, recall, onGenerate = { generating = true })
        2 -> TipsTab(tips, onGenerate = { generating = true })
        3 -> {
            Div({ classes("topic-panel") }) {
                Div({ classes("topic-stats", "flat") }) {
                    TopicStat("${questions.size}", "questões")
                    TopicStat("${questions.count { it.answerCount == 0 && attempts.none { a -> a.questionId == it.id } }}", "nunca vistas")
                    TopicStat(accuracy?.let { "$it%" } ?: "-", "de acerto")
                }
                Btn("Treinar este tópico", { Router.go(Route.Quiz("topico-$topicId")) }, style = "primary", icon = "play_arrow", block = true, enabled = questions.isNotEmpty())
            }
            if (subject != null && !Store.demo) Card {
                val subjectTopicIds = data.topics.filter { it.subjectId == subject.id }.mapTo(hashSetOf()) { it.id }
                val subjectCount = data.questions.count { it.topicId in subjectTopicIds }
                CardHead(if (questions.isEmpty()) "Gere as primeiras questões" else "Quer mais questões?")
                P({ classes("small", "muted"); attr("style", "margin:-6px 0 12px") }) {
                    Text(if (subjectCount == 0) "Questões inéditas no estilo da banca, com explicação de cada alternativa." else "O Estudário compara com as $subjectCount questões que você já tem nesta matéria para não repetir.")
                }
                Button({ classes("btn", "outline", "block"); onClick { generatingQuestions = true } }) { Folha("talking", 26); Text("Gerar questões") }
            }
        }
        4 -> if (concepts.isEmpty()) Card { Empty("task_alt", "Nenhum erro por aqui", "Quando você errar uma questão deste tópico, o conceito por trás do erro aparece aqui, para atacar o padrão e não só a questão.") }
        else {
            SectionHeader("Onde você erra", "Conceitos por trás dos seus erros, do mais frequente ao menos.")
            concepts.forEach { c ->
                key(c.id) {
                    Div({ classes("concept-row") }) {
                        Span({ classes("times", if (c.mastered) "ok" else "bad") }) { Text("${c.errorCount}×") }
                        Div({ classes("grow") }) {
                            B { Text(c.title) }
                            if (c.summary.isNotBlank()) P({ classes("small", "muted") }) { Text(c.summary) }
                            Span({ classes("xs", if (c.mastered) "ok-ink" else "primary-ink") }) { Text(if (c.mastered) "Corrigido" else "Prioridade ${c.priority.lowercase()}") }
                        }
                    }
                }
            }
        }
        else -> {
            val history = buildList {
                data.sessions.filter { it.topicId == topicId }.forEach { add(Triple(it.completedAt, "Estudo concluído", "school")) }
                data.reviewHistory.filter { it.topicId == topicId }.forEach { add(Triple(it.reviewedAt, "Revisão feita", "replay")) }
                attempts.forEach { add(Triple(it.answeredAt, if (it.correct) "Acertou uma questão" else "Errou uma questão", if (it.correct) "check_circle" else "cancel")) }
                data.focusSessions.filter { it.topicId == topicId }.forEach { add(Triple(it.completedAt, "Foco de ${Queries.minutesLabel((it.durationSeconds / 60).toInt())}", "timer")) }
            }.sortedByDescending { it.first }.take(30)
            if (history.isEmpty()) Card { Empty("history", "Sem histórico ainda", "Estudos, revisões e questões deste tópico aparecem aqui, em ordem.") }
            else Card {
                Div({ classes("stack") }) {
                    history.forEachIndexed { i, (time, text, ic) ->
                        key("$i-$time") {
                            Div({ classes("row") }) {
                                Span({ classes("hist-dot") }) { Icon(ic) }
                                Div { B({ classes("small") }) { Text(text) }; Div({ classes("xs", "muted") }) { Text(dateTime(time)) } }
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun priorityLabel(p: String) = when (p) { "CRITICAL", "MUITO_ALTA" -> "muito alta"; "HIGH", "ALTA" -> "alta"; "LOW", "BAIXA" -> "baixa"; else -> "normal" }

private fun dateTime(millis: Long): String {
    val d = Date(millis.toDouble())
    fun two(n: Int) = n.toString().padStart(2, '0')
    return "${two(d.getDate())}/${two(d.getMonth() + 1)}/${d.getFullYear()} · ${two(d.getHours())}:${two(d.getMinutes())}"
}

/**
 * O sumário da leitura, na margem do caderno: o anel de quanto já foi lido, os capítulos numerados
 * numa trilha que se preenche, o capítulo da tela com marca-texto e os já lidos com visto.
 */
@Composable
private fun TheoryToc(theoryId: Long, chapters: List<Pair<Int, String>>, blockCount: Int, shown: Int, active: Int, percent: Int, marks: Int, onPick: () -> Unit) {
    Div({ classes("toc-head") }) {
        Div({ classes("toc-ring"); attr("style", "--p:$percent") }) { B { Text("$percent%") } }
        Div {
            Span({ classes("toc-eyebrow") }) { Text("NESTE TEXTO") }
            Div({ classes("toc-meta") }) { Text(listOfNotNull("${chapters.size} capítulos", if (marks > 0) "$marks grifo${if (marks == 1) "" else "s"}" else null).joinToString(" · ")) }
        }
    }
    // A trilha: preenchida até o capítulo da tela.
    val fill = if (chapters.size <= 1) 0 else active * 100 / (chapters.size - 1)
    Div({ classes("toc-list"); attr("style", "--fill:$fill%") }) {
        chapters.forEachIndexed { pos, (blockIndex, title) ->
            val done = shown >= (chapters.getOrNull(pos + 1)?.first ?: blockCount) - 1
            val state = when { pos == active -> "on"; done -> "done"; else -> null }
            Button({
                classes(*listOfNotNull("toc-item", state).toTypedArray())
                if (pos == active) attr("aria-current", "location")
                onClick { scrollToBlock(theoryId, blockIndex); onPick() }
            }) {
                Span({ classes("toc-dot") }) { if (done && pos != active) Text("✓") else Text("${pos + 1}") }
                Span({ classes("toc-title") }) { Text(title) }
            }
        }
    }
    Button({ classes("toc-top"); onClick { window.scrollTo(js("({top:0,behavior:'smooth'})")); onPick() } }) { Icon("arrow_upward"); Text("Voltar ao topo") }
}

private fun scrollToBlock(theoryId: Long, block: Int) {
    window.setTimeout({ document.getElementById("tb-$theoryId-$block")?.asDynamic()?.scrollIntoView(js("({behavior:'smooth',block:'start'})")) }, 60)
}

@Composable
private fun TopicStat(value: String, label: String) {
    Div({ classes("tstat") }) { B { Text(value) }; Span { Text(label) } }
}

@Composable
private fun MenuItem(icon: String, label: String, onClick: () -> Unit) {
    val latest = rememberUpdatedState(onClick)
    Button({ classes("menu-item"); onClick { latest.value() } }) { Icon(icon); Text(label) }
}

@Composable
private fun SectionHeader(title: String, subtitle: String? = null, action: (@Composable () -> Unit)? = null) {
    Div({ classes("row", "between"); attr("style", "margin-top:6px") }) {
        Div { H3 { Text(title) }; subtitle?.let { P({ classes("small", "muted") }) { Text(it) } } }
        action?.invoke()
    }
}

@Composable
private fun EmptyGenerate(icon: String, title: String, text: String, onGenerate: () -> Unit) {
    Card { Empty(icon, title, text, action = { if (!Store.demo) Button({ classes("btn", "primary"); onClick { onGenerate() } }) { Folha("happy", 26); Text("Gerar com o Estudário") } }) }
}

// ------------------------------------------------------------------ teoria: o texto já na tela, com capítulos e % lido

@Composable
private fun TheoryReader(theory: Theory) {
    val blocks = remember(theory.markdown) { studyBlocks(theory.markdown) }
    val chapters = remember(blocks) { chaptersOf(blocks) }
    var readUpTo by remember(theory.id) { mutableStateOf(theory.lastReadBlock) }
    val shown = maxOf(readUpTo, theory.lastReadBlock)
    val percent = if (shown < 0) 0 else ((shown + 1) * 100 / blocks.size.coerceAtLeast(1)).coerceIn(0, 100)
    val theoryMarks = Store.data.theoryMarks.filter { it.theoryId == theory.id }
    val marks = theoryMarks.size
    // Blocos com grifo ganham a marca amarela na margem, como o grifo no app.
    val markedBlocks = remember(blocks, theoryMarks) { theoryMarks.mapTo(hashSetOf()) { TheoryJump.blockOf(blocks, it.quote, it.blockIndex) } }
    var flashBlock by remember(theory.id) { mutableStateOf(-1) }
    LaunchedEffect(theory.id, blocks.size) {
        val target = TheoryJump.pending?.takeIf { it.theoryId == theory.id } ?: return@LaunchedEffect
        TheoryJump.pending = null
        val index = TheoryJump.blockOf(blocks, target.quote, target.blockIndex)
        delay(250)
        scrollToBlock(theory.id, index)
        flashBlock = index
        delay(2600)
        flashBlock = -1
    }

    // Grifos pintados de amarelo no próprio texto (highlight.js), como no app.
    LaunchedEffect(theory.id, theoryMarks, blocks.size) {
        delay(200)
        val list = theoryMarks.map { m ->
            val o: dynamic = js("({})")
            o.block = TheoryJump.blockOf(blocks, m.quote, m.blockIndex)
            o.quote = m.quote
            o.id = m.id.toDouble()
            o
        }.toTypedArray()
        // Em dynamic, "?.invoke()" vira chamada ao método invoke (inexistente) e derruba a tela: chamar direto.
        val highlight = window.asDynamic().estudarioHighlight
        if (highlight != null && highlight != undefined) highlight(theory.id, list)
    }
    // Selecionar um trecho mostra a barrinha "Grifar" logo acima dele.
    var picked by remember(theory.id) { mutableStateOf<TextPick?>(null) }
    // Clique num trecho já grifado: a barrinha oferece tirar o grifo ali mesmo.
    var clickedMark by remember(theory.id) { mutableStateOf<Triple<Long, Double, Double>?>(null) }
    var noteFor by remember(theory.id) { mutableStateOf<TextPick?>(null) }
    DisposableEffect(theory.id) {
        val check: (org.w3c.dom.events.Event) -> Unit = {
            window.setTimeout({
                val selection = window.asDynamic().estudarioSelection
                val s = if (selection != null && selection != undefined) selection() else null
                picked = if (s == null || s == undefined || (s.theoryId as Number).toLong() != theory.id) null
                else TextPick(
                    (s.block as Number).toInt(), s.quote as String, (s.x as Number).toDouble(), (s.y as Number).toDouble(),
                    (s.parts as? Array<dynamic>)?.map { ((it.block as Number).toInt()) to (it.quote as String) } ?: emptyList(),
                )
            }, 0)
        }
        val hide: (org.w3c.dom.events.Event) -> Unit = { picked = null; clickedMark = null }
        val click: (org.w3c.dom.events.Event) -> Unit = { e ->
            val m = e as org.w3c.dom.events.MouseEvent
            val markAt = window.asDynamic().estudarioMarkAt
            val hasSelection = (window.asDynamic().getSelection()?.toString() as? String).orEmpty().isNotBlank()
            val id = if (markAt == null || markAt == undefined || hasSelection) null else markAt(theory.id, m.clientX, m.clientY)
            clickedMark = if (id == null || id == undefined) null else Triple((id as Number).toLong(), m.clientX.toDouble(), m.clientY.toDouble())
        }
        document.addEventListener("click", click)
        document.addEventListener("mouseup", check)
        document.addEventListener("keyup", check)
        document.addEventListener("touchend", check)
        window.addEventListener("scroll", hide)
        onDispose {
            document.removeEventListener("mouseup", check)
            document.removeEventListener("keyup", check)
            document.removeEventListener("touchend", check)
            document.removeEventListener("click", click)
            window.removeEventListener("scroll", hide)
        }
    }
    fun saveMark(p: TextPick, note: String) {
        if (Store.demo) Toast.show("Entre com sua conta para grifar e guardar no Caderno.")
        else {
            // Seleção de vários parágrafos: um grifo em cada um (a nota vai no primeiro).
            val parts = p.parts.ifEmpty { listOf(p.block to p.quote) }
            Store.update { snapshot ->
                parts.foldIndexed(snapshot) { i, acc, (block, quote) -> Actions.addTheoryMark(acc, theory.id, block, quote, if (i == 0) note else "") }
            }
            Toast.show(if (parts.size > 1) "${parts.size} trechos grifados e salvos no Caderno de estudo" else "Grifo salvo no Caderno de estudo")
        }
        window.asDynamic().getSelection()?.removeAllRanges()
        picked = null
    }

    // Conta como lido o bloco que já passou pela tela (o leitor do app grava o mesmo número).
    DisposableEffect(theory.id, blocks.size) {
        val listener: (org.w3c.dom.events.Event) -> Unit = {
            val limit = window.innerHeight * 0.75
            var last = -1
            for (i in blocks.indices) {
                val el = document.getElementById("tb-${theory.id}-$i") ?: continue
                if (el.getBoundingClientRect().top < limit) last = i else break
            }
            if (last > readUpTo) readUpTo = last
        }
        window.addEventListener("scroll", listener)
        val first = window.setTimeout({ listener(org.w3c.dom.events.Event("scroll")) }, 400)
        onDispose { window.clearTimeout(first); window.removeEventListener("scroll", listener) }
    }
    // O capítulo na tela agora (o sumário lateral acompanha a rolagem, para cima e para baixo).
    var activeChapter by remember(theory.id) { mutableStateOf(0) }
    var readFraction by remember(theory.id) { mutableStateOf(0.0) }
    var chapterDone by remember(theory.id) { mutableStateOf(0) }
    var readerOpen by remember(theory.id) { mutableStateOf(false) }
    LaunchedEffect(chapterDone) { if (chapterDone > 0) { delay(1900); chapterDone = 0 } }
    var tocOpen by remember(theory.id) { mutableStateOf(false) }
    DisposableEffect(theory.id, chapters) {
        val listener: (org.w3c.dom.events.Event) -> Unit = {
            val line = window.innerHeight * 0.35
            var at = 0
            chapters.forEachIndexed { pos, (blockIndex, _) ->
                val el = document.getElementById("tb-${theory.id}-$blockIndex")
                if (el != null && el.getBoundingClientRect().top < line) at = pos
            }
            if (at > activeChapter && chapters.size > 1) chapterDone = activeChapter + 1
            activeChapter = at
            // Linha de progresso: quanto do texto já passou pela tela.
            document.querySelector(".theory-text")?.getBoundingClientRect()?.let { r ->
                val total = (r.height - window.innerHeight * 0.6).coerceAtLeast(1.0)
                readFraction = ((window.innerHeight * 0.4 - r.top) / total).coerceIn(0.0, 1.0)
            }
        }
        window.addEventListener("scroll", listener)
        val first = window.setTimeout({ listener(org.w3c.dom.events.Event("scroll")) }, 450)
        onDispose { window.clearTimeout(first); window.removeEventListener("scroll", listener) }
    }
    LaunchedEffect(readUpTo) {
        if (readUpTo > theory.lastReadBlock && !Store.demo) {
            delay(1500)
            Store.update { Actions.setLastReadBlock(it, theory.id, readUpTo) }
        }
    }

    Div({ classes("theory-card") }) {
        Div({ classes("row") }) {
            Icon("menu_book", extraClass = "theory-ic")
            Div({ classes("grow") }) {
                B({ classes("theory-name") }) { Text(theory.title.ifBlank { "Teoria" }) }
                Div({ classes("small", "muted") }) { Text(listOfNotNull("${chapters.size.coerceAtLeast(1)} capítulo(s)", "$percent% lido", if (marks > 0) "$marks grifo(s)" else null).joinToString(" · ")) }
            }
            // Ajustes de leitura: tamanho da letra e fonte (ficam neste navegador).
            Div({ attr("style", "position:relative") }) {
                org.jetbrains.compose.web.dom.Button({ classes("reader-aa"); attr("aria-label", "Ajustar a leitura"); onClick { readerOpen = !readerOpen } }) { Text("Aa") }
                if (readerOpen) ReaderSettingsPop { readerOpen = false }
            }
        }
        Div({ attr("style", "margin:12px 0 4px") }) { ProgressBar(percent / 100.0) }
        if (chapters.size > 1) Div({ classes("chapters") }) {
            chapters.forEachIndexed { pos, (blockIndex, title) ->
                val done = shown >= (chapters.getOrNull(pos + 1)?.first ?: blocks.size) - 1
                val current = !done && shown >= blockIndex - 1 && shown >= 0
                Button({ classes(*listOfNotNull("chapter", if (current) "current" else null).toTypedArray()); onClick { scrollToBlock(theory.id, blockIndex) } }) {
                    Icon(when { done -> "check_circle"; current -> "play_circle"; else -> "radio_button_unchecked" }, extraClass = when { done -> "ok-ink"; current -> "primary-ink"; else -> "faint" })
                    Span { Text(title) }
                }
            }
        }
    }
    // Fontes citadas no meio dos parágrafos saem do texto e vão, numeradas, para o fim da teoria.
    // Citações da busca no meio do texto saem; as fontes ficam só em "Sobre este conteúdo".
    val extracted = remember(blocks) { blocks.map { InlineSources.extract(it) } }
    Div({ classes(*listOfNotNull("theory-layout", if (chapters.size > 1) "has-toc" else null).toTypedArray()) }) {
    // A linha fina de progresso no topo e o aviso de capítulo concluído.
    Div({ classes("read-progress") }) { Span({ attr("style", "transform:scaleX($readFraction)") }) }
    if (chapterDone > 0) Div({ classes("chapter-done") }) { Icon("check_circle", filled = true); Text("Capítulo $chapterDone concluído") }
    Div({
        classes(*listOfNotNull("theory-text", if (ReaderPrefs.serif) "reader-book" else null).toTypedArray())
        attr("style", "--reader-size:${ReaderPrefs.sizes[ReaderPrefs.size]}px")
    }) {
        extracted.map { it.first }.forEachIndexed { i, block ->
            val trap = calloutTrap(block)
            Div({
                id("tb-${theory.id}-$i")
                classes(*listOfNotNull("tblock", trap?.let { if (it) "callout-trap" else "callout-tip" }, if (i in markedBlocks) "marked" else null, if (i == flashBlock) "flash-mark" else null).toTypedArray())
            }) {
                if (trap != null) {
                    Div({ classes("callout-head") }) { Icon(if (trap) "warning" else "lightbulb", filled = true); Text(if (trap) "Atenção" else "Dica") }
                    Markdown(block.lines().joinToString("\n") { it.trimStart().removePrefix(">").removePrefix(" ") })
                } else Markdown(block)
            }
        }
        Div({ classes("theory-end") }) {
            Folha(if (percent >= 100) "happy" else "point", 64)
            Span({ classes("small", "muted") }) { Text(if (percent >= 100) "Você leu tudo! Hora de revisar e treinar questões." else "Role até o fim para completar a leitura. O progresso vai para o celular.") }
        }
    }
    if (chapters.size > 1) {
        org.jetbrains.compose.web.dom.Aside({ classes("theory-toc"); attr("aria-label", "Sumário da leitura") }) {
            TheoryToc(theory.id, chapters, blocks.size, shown, activeChapter, percent, marks) { }
        }
    }
    }
    // Tela menor: o sumário vem num botão flutuante.
    if (chapters.size > 1) {
        Button({ classes("toc-fab"); attr("aria-label", "Capítulos"); onClick { tocOpen = !tocOpen } }) {
            Icon(if (tocOpen) "close" else "toc"); Span { Text("${activeChapter + 1}/${chapters.size}") }
        }
        if (tocOpen) {
            Div({ classes("toc-scrim"); onClick { tocOpen = false } })
            Div({ classes("toc-sheet"); attr("role", "dialog"); attr("aria-label", "Capítulos") }) {
                TheoryToc(theory.id, chapters, blocks.size, shown, activeChapter, percent, marks) { tocOpen = false }
            }
        }
    }
    clickedMark?.let { (markId, x, y) ->
        val mark = theoryMarks.firstOrNull { it.id == markId }
        if (mark != null && picked == null) Div({
            classes("grifo-bar")
            attr("style", "left:${x}px;top:${(y - 54).coerceAtLeast(8.0)}px")
            addEventListener("mousedown") { it.preventDefault() }
            addEventListener("click") { it.stopPropagation() }
        }) {
            Button({
                classes("grifo-btn", "danger")
                onClick {
                    if (Store.demo) Toast.show("Entre com sua conta para mexer nos grifos.")
                    else { Store.update { Actions.deleteTheoryMark(it, markId) }; Toast.show("Grifo tirado") }
                    clickedMark = null
                }
            }) { Icon("ink_eraser"); Text("Tirar grifo") }
            if (mark.note.isNotBlank()) Span({ classes("grifo-note") }) { Text(mark.note.take(80)) }
        }
    }
    picked?.let { p ->
        Div({
            classes("grifo-bar")
            attr("style", "left:${p.x}px;top:${(p.y - 54).coerceAtLeast(8.0)}px")
            // Clicar na barra não pode desfazer a seleção antes de salvar.
            addEventListener("mousedown") { it.preventDefault() }
        }) {
            Button({ classes("grifo-btn"); onClick { saveMark(p, "") } }) { Icon("format_ink_highlighter"); Text("Grifar") }
            Button({ classes("grifo-btn"); onClick { noteFor = p; picked = null } }) { Icon("edit_note"); Text("Grifar com nota") }
        }
    }
    noteFor?.let { p ->
        var note by remember(p) { mutableStateOf("") }
        Modal(onDismiss = { noteFor = null }) {
            H2 { Text("Grifar com nota") }
            Div({ classes("grifo-quote") }) { Text("“${p.quote.take(400)}${if (p.quote.length > 400) "…" else ""}”") }
            TextArea(note, { classes("input"); attr("rows", "3"); attr("maxlength", "1000"); attr("placeholder", "Por que esse trecho importa? (opcional)"); attr("style", "width:100%"); onInput { note = it.value } })
            Div({ classes("row", "end") }) {
                Btn("Cancelar", { noteFor = null }, style = "ghost")
                Btn("Salvar grifo", { saveMark(p, note); noteFor = null }, style = "primary", icon = "format_ink_highlighter")
            }
        }
    }
}

/** Trecho selecionado na teoria: bloco, texto e onde a barrinha "Grifar" aparece (coordenadas da tela). */
private data class TextPick(val block: Int, val quote: String, val x: Double, val y: Double, val parts: List<Pair<Int, String>> = emptyList())

@Composable
private fun TopicSources(topicId: Long) {
    val sources = Store.data.contentSources.filter { it.topicId == topicId }
    if (sources.isEmpty()) return
    var open by remember(topicId) { mutableStateOf(false) }
    Card {
        Button({ classes("pick-row", "flat"); onClick { open = !open } }) {
            Span({ classes("grow") }) { B { Text("Sobre este conteúdo") }; Span({ classes("hint") }) { Text("De onde veio o material") } }
            Icon(if (open) "expand_less" else "expand_more")
        }
        if (open) Div({ classes("stack"); attr("style", "margin-top:8px") }) {
            Div({ classes("eyebrow"); attr("style", "color:var(--primary)") }) { Text("FONTES") }
            sources.forEach { src ->
                key(src.id) {
                    Div({ classes("source-row") }) {
                        Chip(if (src.kind == "OFICIAL") "FONTE OFICIAL" else "FONTE COMPLEMENTAR", tone = if (src.kind == "OFICIAL") "green" else null)
                        B({ classes("small") }) { Text(src.title) }
                        listOf(src.publisher, src.reference).filter { it.isNotBlank() }.joinToString(" · ").takeIf { it.isNotBlank() }?.let { Div({ classes("xs", "muted") }) { Text(it) } }
                        src.url?.let { url -> org.jetbrains.compose.web.dom.A(href = url, { attr("target", "_blank"); attr("rel", "noopener"); classes("md-link", "xs") }) { Text(url.substringAfter("://").substringBefore('/')) } }
                    }
                }
            }
        }
    }
}

@Composable
private fun TopicNotes(topicId: Long) {
    val notes = Store.data.notes.filter { it.topicId == topicId }.sortedByDescending { it.createdAt }
    var editing by remember(topicId) { mutableStateOf<Long?>(null) }
    var text by remember(topicId) { mutableStateOf("") }
    SectionHeader("Minhas anotações", "Também ficam no Caderno de estudo") {
        if (editing == null) Btn("Nova", { editing = 0L; text = "" }, style = "ghost", small = true, icon = "add")
    }
    if (editing != null) Card {
        TextArea(text, { classes("input"); attr("rows", "4"); attr("maxlength", "4000"); attr("placeholder", "Escreva com suas palavras o que precisa lembrar…"); attr("style", "width:100%"); onInput { text = it.value } })
        Div({ classes("row"); attr("style", "margin-top:8px;justify-content:flex-end") }) {
            if (editing != 0L) Btn("Apagar", { val id = editing!!; Store.update { Actions.deleteNote(it, id) }; editing = null }, style = "ghost", small = true, icon = "delete")
            Btn("Cancelar", { editing = null }, style = "ghost", small = true)
            Btn("Salvar", { val id = editing!!; val value = text; Store.update { Actions.saveNote(it, id, topicId, value) }; editing = null; Toast.show("Anotação salva") }, style = "primary", small = true, enabled = text.isNotBlank())
        }
    }
    if (notes.isEmpty() && editing == null) P({ classes("small", "muted") }) { Text("Escreva com suas palavras o que precisa lembrar deste tópico.") }
    notes.forEach { note ->
        key(note.id) {
            Button({ classes("note-row"); onClick { editing = note.id; text = note.text } }) {
                Icon("edit_note")
                Span({ classes("grow") }) { Text(note.text) }
            }
        }
    }
}

// ------------------------------------------------------------------ revisão: baralhos, resumos e memorização

@Composable
private fun ReviewTab(summaries: List<Summary>, recall: List<br.com.estudario.web.data.Snippet>, onGenerate: () -> Unit) {
    val decks = summaries.filter { it.kind == "RAPIDO" }
    val full = summaries.filter { it.kind != "RAPIDO" }
    var practicing by remember { mutableStateOf<Summary?>(null) }
    var expanded by remember { mutableStateOf<Long?>(null) }
    if (summaries.isEmpty() && recall.isEmpty()) EmptyGenerate("style", "Nada para revisar ainda", "Gere resumo, flashcards e perguntas de memorização para revisar este tópico em minutos.", onGenerate)
    decks.forEach { deck ->
        key("d${deck.id}") {
            val size = remember(deck.markdown) { deckCards(deck.markdown).size }
            Div({ classes("deck-card"); onClick { practicing = deck } }) {
                Div({ classes("grow") }) {
                    Div({ classes("eyebrow"); attr("style", "color:rgba(255,255,255,.8)") }) { Text("FLASHCARDS") }
                    B({ attr("style", "font-size:18px") }) { Text("$size cartões para praticar") }
                    P({ classes("small"); attr("style", "opacity:.85") }) { Text(if (decks.size > 1 && !deck.title.equals("Revisão rápida", true)) deck.title else "Tente lembrar antes de virar. Salve só os que interessam.") }
                }
                Button({
                    classes("icon-btn", "on-primary"); attr("title", if (deck.favorite) "Tirar baralho do Caderno" else "Salvar baralho no Caderno")
                    onClick { e -> e.stopPropagation(); Store.update { Actions.toggleSummaryFavorite(it, deck.id) }; Toast.show(if (deck.favorite) "Baralho tirado do Caderno" else "Baralho salvo no Caderno") }
                }) { if (deck.favorite) Icon("bookmark", filled = true, plain = true) else Icon("bookmark_border", plain = true) }
                Icon("play_circle", filled = true, extraClass = "deck-play")
            }
        }
    }
    full.forEach { summary ->
        key("s${summary.id}") {
            val open = expanded == summary.id
            Div({ classes("summary-card") }) {
                Button({ classes("pick-row", "flat"); onClick { expanded = if (open) null else summary.id } }) {
                    Icon("article", extraClass = "primary-ink")
                    Span({ classes("grow") }) { Span({ classes("eyebrow"); attr("style", "color:var(--primary)") }) { Text("RESUMO") }; B { Text(summary.title.ifBlank { "Resumo completo" }) } }
                    Icon(if (open) "expand_less" else "expand_more")
                }
                if (open) Div({ attr("style", "padding:4px 14px 14px") }) { Markdown(summary.markdown) }
                else P({ classes("small", "muted", "clamp-3"); attr("style", "padding:0 14px 14px") }) { Text(summary.markdown.replace(Regex("[#*_>`|]"), "").replace(Regex("\\s+"), " ").take(240)) }
            }
        }
    }
    if (recall.isNotEmpty()) {
        // Mesmo formato das dicas e pegadinhas: o personagem uma vez só, no cabeçalho do grupo.
        Div({ classes("tip-head", "recall") }) {
            Span({ classes("tip-ic") }) { Icon("psychology", filled = true) }
            Div {
                B { Text("MEMORIZAÇÃO ATIVA") }
                Div({ classes("small", "muted") }) { Text("${recall.size} ${if (recall.size == 1) "pergunta" else "perguntas"} para responder de cabeça e depois conferir. ★ guarda no Caderno.") }
            }
        }
        recall.forEach { s -> key("r${s.id}") { RecallCard(s.id, s.text, s.answer, s.favorite) } }
    }
    practicing?.let { deck -> DeckDialog(deck) { practicing = null } }
}

@Composable
private fun RecallCard(id: Long, question: String, answer: String?, favorite: Boolean) {
    var shown by remember(id) { mutableStateOf(false) }
    Div({ classes("recall-card") }) {
        Div({ classes("row", "between"); attr("style", "align-items:flex-start") }) {
            Div({ classes("grow") }) { Span({ classes("eyebrow"); attr("style", "color:var(--primary)") }) { Text("PERGUNTA") }; P({ attr("style", "margin-top:4px;font-weight:600") }) { Inline(question) } }
            StarButton(favorite) { Store.update { Actions.toggleSnippetFavorite(it, id) } }
        }
        if (!answer.isNullOrBlank()) {
            if (shown) Div({ classes("recall-answer") }) { Markdown(answer) }
            else Btn("Mostrar resposta", { shown = true }, style = "tonal", small = true, icon = "visibility")
        }
    }
}

@Composable
private fun StarButton(on: Boolean, onToggle: () -> Unit) {
    val latest = rememberUpdatedState(onToggle)
    Button({ classes("fav-star", *listOfNotNull(if (on) "on" else null).toTypedArray()); attr("title", if (on) "Tirar do Caderno" else "Guardar no Caderno"); onClick { latest.value(); Toast.show(if (on) "Tirado do Caderno" else "Guardado no Caderno") } }) {
        // Guardada: a estrela desenhada, cheia. Fora do Caderno: só o contorno, para a diferença saltar aos olhos.
        if (on) Icon("star") else Icon("star", plain = true)
    }
}

@Composable
private fun DeckDialog(deck: Summary, onClose: () -> Unit) {
    val cards = remember(deck.markdown) { deckCards(deck.markdown) }
    var index by remember { mutableStateOf(0) }
    var flipped by remember { mutableStateOf(false) }
    Modal(onDismiss = onClose) {
        Div({ classes("row", "between") }) {
            Span({ classes("small", "muted", "strong") }) { Text(if (cards.isEmpty()) "Baralho" else "${index + 1} de ${cards.size}") }
            IconButton("close", "Fechar", onClose)
        }
        if (cards.isEmpty()) { P { Text("Este baralho não tem cartões.") }; return@Modal }
        ProgressBar((index + 1).toDouble() / cards.size)
        val card = cards[index]
        val saved = Store.data.snippets.any { it.externalId == Actions.savedCardId(deck.id, card.front, card.back) }
        Div({ classes("flash", *listOfNotNull(if (flipped) "flipped" else null).toTypedArray()); onClick { flipped = !flipped } }) {
            if (!flipped) { Span({ classes("eyebrow") }) { Text("FRENTE") }; H3 { Inline(card.front) }; Span({ classes("xs", "muted") }) { Text("Toque para virar") } }
            else { Span({ classes("eyebrow") }) { Text("VERSO") }; if (card.back.isBlank()) P { Text(card.front) } else Markdown(card.back) }
        }
        Div({ classes("row", "between") }) {
            Btn("Anterior", { if (index > 0) { index--; flipped = false } }, style = "ghost", icon = "arrow_back", enabled = index > 0)
            Btn(if (saved) "Salvo no Caderno" else "Salvar cartão", { Store.update { Actions.toggleSavedFlashcard(it, deck.id, deck.topicId, card.front, card.back) } }, style = if (saved) "tonal" else "outline", icon = if (saved) "bookmark" else "bookmark_border")
            if (index < cards.lastIndex) Btn("Próximo", { index++; flipped = false }, style = "primary", icon = "arrow_forward")
            else Btn("Terminar", onClose, style = "primary", icon = "check")
        }
    }
}

// ------------------------------------------------------------------ dicas e pegadinhas

@Composable
private fun TipsTab(tips: List<br.com.estudario.web.data.Snippet>, onGenerate: () -> Unit) {
    if (tips.isEmpty()) { EmptyGenerate("lightbulb", "Sem dicas ainda", "Dicas e pegadinhas de banca vêm junto com o material gerado para este tópico.", onGenerate); return }
    SectionHeader("Dicas e pegadinhas", "O que costuma decidir a questão. ★ guarda no Caderno.")
    listOf(false, true).map { t -> t to tips.filter { (it.kind == "PEGADINHA") == t } }.filter { it.second.isNotEmpty() }.forEach { (trap, group) ->
        key("g$trap") {
            Div({ classes("tip-head", if (trap) "trap" else "tip") }) {
                Span({ classes("tip-ic") }) { Icon(if (trap) "pest_control_rodent" else "lightbulb", filled = true) }
                Div {
                    B { Text(if (trap) "PEGADINHAS DA BANCA" else "DICAS QUE DECIDEM QUESTÃO") }
                    Div({ classes("small", "muted") }) { Text(if (trap) "${group.size} ${if (group.size == 1) "armadilha" else "armadilhas"} que a banca costuma armar" else "${group.size} ${if (group.size == 1) "dica" else "dicas"} para acertar mais rápido") }
                }
            }
            group.forEach { tip ->
                key(tip.id) {
                    Div({ classes("tip-row", if (trap) "trap" else "tip") }) {
                        Div({ classes("grow") }) { Inline(tip.text); tip.answer?.takeIf { it.isNotBlank() }?.let { P({ classes("small", "muted"); attr("style", "margin-top:6px") }) { Inline(it) } } }
                        StarButton(tip.favorite) { Store.update { Actions.toggleSnippetFavorite(it, tip.id) } }
                    }
                }
            }
        }
    }
}

/**
 * Pulo do Caderno para a teoria: o grifo clicado abre o tópico já na aba Teoria, rolado até o
 * trecho e com ele piscando em amarelo. O trecho é achado pelo texto (o bloco guardado é o reserva).
 */
object TheoryJump {
    data class Target(val topicId: Long, val theoryId: Long, val blockIndex: Int, val quote: String)
    var pending: Target? = null

    fun go(topicId: Long, theoryId: Long, blockIndex: Int, quote: String) {
        pending = Target(topicId, theoryId, blockIndex, quote)
        Router.go(Route.Topic(topicId))
    }

    /** Texto sem marcação nem espaços repetidos, para comparar grifo com bloco. */
    fun plain(text: String): String = text.replace(Regex("""[*_`>#$\\{}|]"""), " ").replace(Regex("""\s+"""), " ").trim().lowercase()

    fun blockOf(blocks: List<String>, quote: String, fallback: Int): Int {
        val needle = plain(quote).take(60)
        if (needle.length >= 8) blocks.indexOfFirst { plain(it).contains(needle) }.takeIf { it >= 0 }?.let { return it }
        return fallback.coerceIn(0, (blocks.size - 1).coerceAtLeast(0))
    }
}

/** Ajustes de leitura da teoria, como num leitor de livros: tamanho da letra e fonte. */
object ReaderPrefs {
    val sizes = listOf(16, 17.5, 19, 21)
    private fun read(k: String) = runCatching { kotlinx.browser.localStorage.getItem(k) }.getOrNull()
    private fun write(k: String, v: String) { runCatching { kotlinx.browser.localStorage.setItem(k, v) } }
    var size by mutableStateOf(read("estudario.reader.size")?.toIntOrNull()?.coerceIn(0, 3) ?: 1)
        private set
    var serif by mutableStateOf(read("estudario.reader.font") != "simples")
        private set
    fun setSize(v: Int) { size = v.coerceIn(0, 3); write("estudario.reader.size", size.toString()) }
    fun setSerif(v: Boolean) { serif = v; write("estudario.reader.font", if (v) "livro" else "simples") }
}

@Composable
private fun ReaderSettingsPop(onClose: () -> Unit) {
    Div({ classes("reader-pop") }) {
        Div({ classes("reader-pop-row") }) {
            Span({ classes("xs", "muted", "strong") }) { Text("TAMANHO") }
            Div({ classes("reader-sizes") }) {
                ReaderPrefs.sizes.forEachIndexed { i, px ->
                    org.jetbrains.compose.web.dom.Button({
                        classes(*listOfNotNull("reader-size", if (ReaderPrefs.size == i) "on" else null).toTypedArray())
                        attr("style", "font-size:${11 + i * 3}px"); attr("aria-label", "Letra $px")
                        onClick { ReaderPrefs.setSize(i) }
                    }) { Text("A") }
                }
            }
        }
        Div({ classes("reader-pop-row") }) {
            Span({ classes("xs", "muted", "strong") }) { Text("FONTE") }
            Div({ classes("segmented") }) {
                org.jetbrains.compose.web.dom.Button({ classes(*listOfNotNull("reader-font-book", if (ReaderPrefs.serif) "on" else null).toTypedArray()); onClick { ReaderPrefs.setSerif(true) } }) { Text("Livro") }
                org.jetbrains.compose.web.dom.Button({ classes(*listOfNotNull(if (!ReaderPrefs.serif) "on" else null).toTypedArray()); onClick { ReaderPrefs.setSerif(false) } }) { Text("Simples") }
            }
        }
        Btn("Pronto", onClose, style = "ghost", small = true)
    }
}
