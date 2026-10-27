package br.com.estudario.web.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import br.com.estudario.domain.ErrorRetryLadder
import br.com.estudario.domain.ErrorVirada
import br.com.estudario.web.Route
import br.com.estudario.web.Router
import br.com.estudario.web.data.Actions
import br.com.estudario.web.data.ErrorEntry
import br.com.estudario.web.data.Question
import br.com.estudario.web.data.Queries
import br.com.estudario.web.data.Snapshot
import br.com.estudario.web.data.Store
import org.jetbrains.compose.web.dom.B
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H2
import org.jetbrains.compose.web.dom.H3
import org.jetbrains.compose.web.dom.I
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text
import kotlin.js.Date

/*
 * "A Virada": o caderno de erros com propósito (a regra em ErrorVirada, módulo comum; o app faz
 * igual). O placar dos erros que viraram, a missão do dia, a escadinha de cada erro até o "VIROU!",
 * o porquê dos erros e o mural dos que viraram. As cores vêm das variáveis do tema (claro e escuro).
 */

/** O que a tela lembra entre a Virada e a missão: os tópicos abertos e o tópico escolhido para virar. */
private object ViradaUi {
    var expanded by mutableStateOf(setOf<Long>())
    var topicId by mutableStateOf<Long?>(null)
    fun toggle(id: Long) { expanded = if (id in expanded) expanded - id else expanded + id }
}

private data class Ficha(val entry: ErrorEntry, val question: Question, val virada: ErrorVirada.Entry)

private fun fichas(data: Snapshot): List<Ficha> {
    val scope = CompetitionFilter.topicIds(data)
    val questions = data.questions.filter { it.topicId in scope }.associateBy { it.id }
    return data.errors.mapNotNull { e ->
        val q = questions[e.questionId] ?: return@mapNotNull null
        Ficha(e, q, ErrorVirada.Entry(e.id, e.questionId, q.topicId, e.errorCount, e.retryStreak, e.nextRetryAt, e.lastReviewedAt, e.lastErrorAt, e.comment))
    }
}

private fun optionLabel(q: Question, key: String?): String {
    if (key == null) return "?"
    val text = q.options.firstOrNull { it.key == key }?.text?.trim().orEmpty()
    return if (text.isEmpty()) key else "$key · ${text.take(48)}${if (text.length > 48) "…" else ""}"
}

private fun daysLabel(at: Long?, now: Long): String {
    if (at == null) return "virou"
    val d = ((at - now + 86_399_999L) / 86_400_000L).toInt()
    return when { d <= 0 -> "vence hoje"; d == 1 -> "volta amanhã"; else -> "volta em $d dias" }
}

@Composable
private fun Ladder(step: Int) {
    val labels = listOf("errou") + ErrorRetryLadder.days.map { "$it dias" } + "virou"
    Div({ classes("vr-ladder") }) {
        labels.forEachIndexed { i, label ->
            // i = 0 é o erro; 1..3 os degraus de acerto; o último é o troféu
            val state = when {
                i == 0 -> "err"
                i <= step -> "done"
                i == step + 1 && i < labels.lastIndex -> "now"
                i == labels.lastIndex && step >= ErrorRetryLadder.days.size -> "won"
                else -> null
            }
            Div({ classes(*listOfNotNull("vr-step", state).toTypedArray()) }) {
                I { Text(when { i == 0 -> "✕"; i == labels.lastIndex -> "★"; i <= step -> "✓"; else -> "$i" }) }
                Span { Text(label) }
            }
        }
    }
}

// ------------------------------------------------------------------ a entrada

@Composable
fun ViradaScreen() {
    val data = Store.data
    val now = Date.now().toLong()
    val all = remember(data) { fichas(data) }
    val entries = all.map { it.virada }
    val won = all.filter { ErrorVirada.stage(it.virada) == ErrorVirada.Stage.TURNED }
    val turning = all.count { ErrorVirada.stage(it.virada) == ErrorVirada.Stage.TURNING }
    val table = all.size - won.size - turning
    val due = ErrorVirada.due(entries, now)
    val mission = due.take(5)
    val week = ErrorVirada.stepsThisWeek(entries, now)
    val reasons = ErrorVirada.reasonStats(entries)
    val topics = data.topics.associateBy { it.id }
    val subjects = data.subjects.associateBy { it.id }
    val concepts = data.errorConcepts.filter { !it.mastered && it.errorCount > 0 && it.topicId in CompetitionFilter.topicIds(data) }.sortedByDescending { it.errorCount }.take(3)
    val pct = { n: Int -> if (all.isEmpty()) 0 else n * 100 / all.size }

    CompetitionTabs()
    Div({ classes("vr-wrap") }) {
        Div({ classes("vr-score", "rise") }) {
            Div({ classes("vr-score-row") }) {
                Folha(if (all.isEmpty()) "happy" else if (won.size * 2 >= all.size) "proud" else "talking", 72)
                Div {
                    Span({ classes("vr-eyebrow") }) { Text("PLACAR DA VIRADA") }
                    H2 { Text(if (all.isEmpty()) "Nenhum erro na mesa" else "Você já virou ${won.size} de ${all.size} ${if (all.size == 1) "erro" else "erros"}") }
                    P { Text(if (all.isEmpty()) "Quando errar uma questão, ela vem para cá. Aqui erro vira acerto." else "Erro virado é questão a mais na prova.") }
                }
            }
            if (all.isNotEmpty()) {
                Div({ classes("vr-meter") }) {
                    I({ classes("won"); attr("style", "width:${pct(won.size)}%") })
                    I({ classes("going"); attr("style", "width:${pct(turning)}%") })
                }
                Div({ classes("vr-legend") }) {
                    Span({ attr("style", "--c:#2FBF8A") }) { Text("${won.size} viraram") }
                    Span({ attr("style", "--c:#FFB421") }) { Text("$turning virando") }
                    Span({ attr("style", "--c:rgba(255,255,255,.4)") }) { Text("$table na mesa") }
                }
                if (week > 0) Span({ classes("vr-gain") }) { Text("▲ $week ${if (week == 1) "degrau subido" else "degraus subidos"} esta semana") }
            }
        }

        if (mission.isNotEmpty()) Div({ classes("vr-card", "vr-mission") }) {
            H3 { Text("Missão de hoje · virar ${mission.size} ${if (mission.size == 1) "erro" else "erros"}") }
            P { Text("Os da mesa e os que vencem hoje, ~${(mission.size * 1.2).toInt().coerceAtLeast(1)} min. Acertou, ele sobe um degrau.") }
            Div({ classes("vr-peek") }) {
                ErrorVirada.missionByTopic(mission).forEach { (t, n) -> Span { B { Text("$n") }; Text(" · " + (t?.let { topics[it]?.title?.take(24) } ?: "Erro")) } }
            }
            Button({ classes("vr-cta"); onClick { ViradaUi.topicId = null; Router.go(Route.ErrorMission) } }) { Icon("replay", filled = true); Text("Começar a virada") }
        } else if (all.isNotEmpty()) Div({ classes("vr-card", "vr-mission") }) {
            H3 { Text("Nada vencendo hoje") }
            P { Text("Os erros voltam no dia certo (3, 10 e 30 dias). Quer treinar os que estão na mesa mesmo assim?") }
            Btn("Refazer erradas", { Router.go(Route.Quiz("erros-20")) }, style = "outline", icon = "replay")
        }

        Div({ classes("vr-sect") }) { B { Text("Por que você erra") }; if (reasons.isNotEmpty()) Span { Text("dos erros com motivo") } }
        Div({ classes("vr-card", "vr-why") }) {
            if (reasons.isEmpty()) P({ classes("muted") }) { Text("Na missão, quando errar de novo, um toque diz o motivo (não sabia, confundi, li rápido, pegadinha). Aqui aparece o seu padrão e a dica do Folha para ele.") }
            else {
                val total = reasons.sumOf { it.second }
                ErrorVirada.Reason.entries.forEach { r ->
                    val n = reasons.firstOrNull { it.first == r }?.second ?: 0
                    Div({ classes("vr-why-row") }) {
                        Span { Text(r.label) }
                        Div({ classes("vr-bar") }) { I({ attr("style", "width:${n * 100 / total}%;--c:${reasonColor(r)}") }) }
                        B { Text("${n * 100 / total}%") }
                    }
                }
                val top = reasons.first().first
                Div({ classes("vr-insight") }) { Text("Seu erro mais comum é "); B { Text(top.label.lowercase()) }; Text(". Dica do Folha: ${top.tip}") }
            }
        }

        if (concepts.isNotEmpty()) {
            Div({ classes("vr-sect") }) { B { Text("Os conceitos que mais te derrubam") } }
            concepts.forEach { c ->
                key(c.id) {
                    val subject = topics[c.topicId]?.let { subjects[it.subjectId]?.name }
                    Div({ classes("vr-concept") }) {
                        Span({ classes("vr-n") }) { Text("${c.errorCount}") }
                        Div({ classes("grow") }) { B { Text(c.title) }; Span { Text(listOfNotNull(subject, topics[c.topicId]?.title).joinToString(" · ")) } }
                        Btn("Treinar 3", { Router.go(Route.Quiz("topico-${c.topicId}-3")) }, style = "primary", small = true)
                    }
                }
            }
        }

        // Um cartão por tópico (com as fichas de cada questão dentro): com muitos erros, uma ficha
        // por questão virava uma rolagem sem fim. A escada continua por questão.
        val groups = ErrorVirada.byTopic(entries, now)
        val fichaById = all.associateBy { it.entry.id }
        val openGroups = groups.filter { !it.done }
        if (openGroups.isNotEmpty()) {
            Div({ classes("vr-sect") }) { B { Text("Seus erros por tópico") }; Span { Text("${due.size} para hoje · ${openGroups.size} ${if (openGroups.size == 1) "tópico" else "tópicos"}") } }
            openGroups.forEach { g ->
                key(g.topicId ?: -1L) {
                    val topic = g.topicId?.let { topics[it] }
                    val subjectName = topic?.let { subjects[it.subjectId]?.name } ?: ""
                    val expanded = (g.topicId ?: -1L) in ViradaUi.expanded
                    Div({ classes("vr-topic"); attr("style", "--c:${subjectColor(subjectName)}") }) {
                        Button({ classes("vr-topic-head"); attr("aria-expanded", "$expanded"); onClick { ViradaUi.toggle(g.topicId ?: -1L) } }) {
                            Div({ classes("vr-topic-ring"); attr("style", "--p:${(g.progress * 100).toInt()}") }) { B { Text("${g.entries.size}") } }
                            Div({ classes("grow") }) {
                                Span({ classes("vr-topic-sub") }) { Text(subjectName.uppercase()) }
                                B { Text(topic?.title ?: "Sem tópico") }
                                Div({ classes("vr-topic-meta") }) {
                                    if (g.due > 0) Span({ classes("due") }) { Text("${g.due} para hoje") }
                                    if (g.turned > 0) Span({ classes("won") }) { Text("${g.turned} ${if (g.turned == 1) "virou" else "viraram"}") }
                                    if (g.turning > 0) Span { Text("${g.turning} virando") }
                                    g.topReason?.let { Span({ classes("why") }) { Text(it.label) } }
                                }
                            }
                            Icon(if (expanded) "expand_less" else "expand_more", extraClass = "faint")
                        }
                        Div({ classes("vr-topic-bar") }) { I({ attr("style", "width:${(g.progress * 100).toInt()}%") }) }
                        if (g.due > 0 || expanded) Div({ classes("vr-topic-actions") }) {
                            if (g.due > 0) Btn("Virar ${g.due} ${if (g.due == 1) "erro" else "erros"} deste tópico", { ViradaUi.topicId = g.topicId; Router.go(Route.ErrorMission) }, style = "primary", small = true, icon = "replay")
                            g.topicId?.let { id -> Btn("Ler a teoria", { Router.go(Route.Topic(id)) }, style = "ghost", small = true, icon = "menu_book") }
                        }
                        if (expanded) Div({ classes("vr-topic-list") }) {
                            g.entries.mapNotNull { fichaById[it.id] }.forEach { f ->
                                key(f.entry.id) {
                                    val note = ErrorVirada.noteOf(f.entry.comment)
                                    val reason = ErrorVirada.reasonOf(f.entry.comment)
                                    val turned = ErrorVirada.stage(f.virada) == ErrorVirada.Stage.TURNED
                                    Div({ classes(*listOfNotNull("vr-ficha", if (turned) "turned" else null).toTypedArray()) }) {
                                        Div({ classes("vr-ficha-top") }) {
                                            Span { Text(if (turned) "VIROU ★" else if (ErrorVirada.stage(f.virada) == ErrorVirada.Stage.TABLE) "NA MESA · DÁ PARA VIRAR JÁ" else daysLabel(f.virada.nextRetryAt, now).uppercase()) }
                                            Span { Text("errou ${f.entry.errorCount}×") }
                                        }
                                        Div({ classes("vr-q") }) { Text(splitStatement(f.question.statement).let { it.command ?: it.base }.take(220)) }
                                        Div({ classes("vr-ans") }) {
                                            f.entry.selectedAnswer?.let { Span({ classes("mine") }) { Text("você: ${optionLabel(f.question, it)}") } }
                                            (f.entry.correctAnswer ?: f.question.options.firstOrNull { it.correct }?.key)?.let { Span({ classes("right") }) { Text("certa: ${optionLabel(f.question, it)}") } }
                                            reason?.let { Span({ classes("why") }) { Text(it.label) } }
                                        }
                                        Ladder(ErrorVirada.step(f.virada))
                                        if (note.isNotBlank()) Div({ classes("vr-note") }) { Text(note) }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // O mural: um troféu por tópico virado por inteiro (antes, um por questão).
        val wonTopics = groups.filter { it.done }
        if (wonTopics.isNotEmpty()) {
            Div({ classes("vr-sect") }) { B { Text("Mural dos tópicos que viraram") }; Span { Text("${wonTopics.size} ${if (wonTopics.size == 1) "troféu" else "troféus"}") } }
            Div({ classes("vr-wall") }) {
                wonTopics.sortedByDescending { g -> g.entries.maxOf { it.lastReviewedAt ?: 0 } }.take(12).forEachIndexed { i, g ->
                    key(g.topicId ?: -1L) {
                        Div({ classes("vr-trophy"); attr("style", "--r:${listOf(-2, 1.5, -1, 2)[i % 4]}deg") }) {
                            Span({ classes("vr-stamp") }) { Text("VIROU!") }
                            B { Text(g.topicId?.let { topics[it]?.title } ?: "Tópico") }
                            Span { Text("${g.entries.size} ${if (g.entries.size == 1) "erro virado" else "erros virados"} · em ${g.entries.maxOf { it.lastReviewedAt ?: 0 }.takeIf { it > 0 }?.let { Queries.shortDate(Queries.dateOf(it)) } ?: ""}") }
                        }
                    }
                }
            }
        }
        if (all.isEmpty()) Card { Empty("task_alt", "Caderno limpo", "Nenhum erro por aqui. Quando errar, a questão volta em 3, 10 e 30 dias até virar.") }
    }
}

private fun reasonColor(r: ErrorVirada.Reason) = when (r) {
    ErrorVirada.Reason.NAO_SABIA -> "#5B8DEF"; ErrorVirada.Reason.CONFUNDI -> "#8E7CFF"; ErrorVirada.Reason.LI_RAPIDO -> "#FFB421"; ErrorVirada.Reason.PEGADINHA -> "#FF6B6B"
}

// ------------------------------------------------------------------ a missão

@Composable
fun ViradaMissionScreen() {
    val now = remember { Date.now().toLong() }
    // a lista é fixada ao abrir: responder muda o erro, mas a missão continua a mesma
    // do tópico escolhido no cartão (todos os que vencem dele) ou a missão do dia, que junta os do mesmo tópico
    val missionIds = remember {
        val all = fichas(Store.data).map { it.virada }
        val topic = ViradaUi.topicId
        (if (topic != null) ErrorVirada.due(all.filter { it.topicId == topic }, now).take(10) else ErrorVirada.mission(all, now)).map { it.id }
    }
    var index by remember { mutableStateOf(0) }
    var climbed by remember { mutableStateOf(0) }
    val sessionId = remember { Actions.newSessionId() }
    if (missionIds.isEmpty()) { Card { Empty("task_alt", "Nada vencendo hoje", "Os erros voltam no dia certo. Volte amanhã!", action = { Btn("Voltar", { Router.go(Route.Errors) }) }) }; return }
    Div({ classes("vr-wrap", "vr-mission-screen") }) {
        Div({ classes("vr-mtop") }) {
            Btn("Sair", { Router.go(Route.Errors) }, style = "ghost", small = true, icon = "close")
            B { Text(if (index < missionIds.size) "Missão · ${index + 1} de ${missionIds.size}" else "Missão feita") }
            Span({ classes("cb-hand") }) { Text("~${(missionIds.size * 1.2).toInt().coerceAtLeast(1)} min") }
        }
        Div({ classes("cb-dots") }) { missionIds.indices.forEach { i -> I({ classes(*listOfNotNull(if (i <= index) "on" else null).toTypedArray()) }) } }
        if (index >= missionIds.size) {
            Div({ classes("cb-done", "rise") }) {
                Folha(if (climbed > 0) "proud" else "talking", 110)
                H2 { Text(if (climbed > 0) "Virada feita!" else "Missão feita") }
                P { Text(if (climbed > 0) "Você subiu $climbed ${if (climbed == 1) "degrau" else "degraus"} hoje. Erro virado é questão a mais na prova." else "Desta vez não virou, e tudo bem: eles voltam amanhã, e agora você sabe o porquê.") }
                Btn("Voltar à Virada", { Router.go(Route.Errors) }, style = "primary", icon = "replay")
            }
            return@Div
        }
        key(missionIds[index]) {
            MissionItem(missionIds[index], sessionId, onClimb = { climbed++ }) { index++ }
        }
    }
}

@Composable
private fun MissionItem(errorId: Long, sessionId: String, onClimb: () -> Unit, onNext: () -> Unit) {
    val data = Store.data
    val start = remember { data.errors.firstOrNull { it.id == errorId } }
    val question = start?.let { s -> data.questions.firstOrNull { it.id == s.questionId } }
    if (start == null || question == null) { onNext(); return }
    val topic = data.topics.firstOrNull { it.id == question.topicId }
    val subject = topic?.let { t -> data.subjects.firstOrNull { it.id == t.subjectId } }
    var picked by remember { mutableStateOf<String?>(null) }
    var right by remember { mutableStateOf<Boolean?>(null) }
    var reason by remember { mutableStateOf(ErrorVirada.reasonOf(start.comment)) }
    val after = data.errors.firstOrNull { it.id == errorId }
    val step = if (right == null) start.retryStreak else after?.retryStreak ?: start.retryStreak
    Div({ classes("vr-card") }) {
        Span({ classes("vr-meta") }) { Text(listOfNotNull(subject?.name, topic?.title, "errou ${start.errorCount}×").joinToString(" · ").uppercase()) }
        Ladder(step.coerceIn(0, ErrorRetryLadder.days.size))
        Div({ classes("vr-statement") }) { QuestionStatement(question.statement) }
        // Clicar só escolhe; vale ao confirmar. A marcação antiga aparece só depois de responder.
        AnswerOptions(question.id, question.options, picked, right, onPick = { picked = it }, previousKey = start.selectedAnswer)
        if (right == null) ConfirmAnswer(picked != null) {
            val key = picked ?: return@ConfirmAnswer
            var ok = false
            Store.update { s -> Actions.answer(s, question.id, key, sessionId).also { ok = it.second }.first }
            right = ok
            if (ok) onClimb()
        }
    }
    if (right == true) Div({ classes("vr-virou") }) {
        val turned = (after?.retryStreak ?: 0) >= ErrorRetryLadder.days.size
        Span({ classes("vr-big") }) { Text(if (turned) "VIROU! ★" else "VIROU UM DEGRAU!") }
        P { Text(if (turned) "Esse erro virou troféu: foi para o mural." else "Agora ela volta em ${ErrorRetryLadder.intervalDays(after?.retryStreak ?: 1) ?: 30} dias. ${if ((after?.retryStreak ?: 0) == ErrorRetryLadder.days.size - 1) "Mais um acerto e vira troféu." else "Continue subindo."}") }
    }
    if (right == false) Div({ classes("vr-reason") }) {
        B { Text("Ainda não. Por que você errou de novo?") }
        Div({ classes("vr-chips") }) {
            ErrorVirada.Reason.entries.forEach { r ->
                Button({
                    classes(*listOfNotNull("vr-chip", if (reason == r) "on" else null).toTypedArray())
                    onClick {
                        reason = r
                        val current = Store.data.errors.firstOrNull { it.id == errorId }?.comment ?: start.comment
                        Store.update { Actions.setErrorComment(it, errorId, ErrorVirada.withReason(current, r)) }
                    }
                }) { Text(r.label) }
            }
        }
        Span({ classes("xs", "muted") }) { Text(reason?.let { "Dica do Folha: ${it.tip}" } ?: "Um toque já ajuda: o caderno aprende o seu padrão. A questão volta em 3 dias.") }
    }
    if (right != null && question.explanation.isNotBlank()) Div({ classes("vr-card", "vr-explain") }) { B { Text("Por quê") }; Markdown(question.explanation, compact = true) }
    if (right != null) Div({ classes("cb-flip-nav") }) { Btn("Próximo erro", onNext, style = "primary", icon = "arrow_forward") }
}
