package br.com.estudario.web.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import br.com.estudario.data.local.ReviewDifficulty
import br.com.estudario.web.Route
import br.com.estudario.web.Router
import br.com.estudario.web.data.Actions
import br.com.estudario.web.data.Queries
import br.com.estudario.web.data.Question
import br.com.estudario.web.data.Review
import br.com.estudario.web.data.Store
import org.jetbrains.compose.web.dom.B
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H2
import org.jetbrains.compose.web.dom.H3
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text
import kotlin.js.Date

/*
 * Revisões espaçadas com a cara do Estudário, como a revisão ativa do app: a lista com o resumo do
 * dia e a escadinha das etapas (D+1, D+7, D+30...), e a sessão em quatro passos numa tela própria:
 * lembrar (cartões que viram), revisão rápida, testar (até 3 questões, as erradas primeiro) e
 * avaliar (com a data da próxima revisão em cada opção).
 */

private const val DAY = 86_400_000L
private val stageLabels = listOf("D+1", "D+7", "D+30", "D+60", "D+120")

/** Revisar tudo em sequência: ao terminar uma, a próxima do dia abre sozinha. */
private object ReviewRun { var active = false }

@Composable
fun ReviewsScreen() {
    val data = Store.data
    val topics = data.topics.associateBy { it.id }
    val subjects = data.subjects.associateBy { it.id }
    val scope = CompetitionFilter.topicIds(data)
    val now = Date.now().toLong()
    val due = Queries.dueReviews(data).filter { it.topicId in scope }.sortedBy { it.dueAt }
    val late = due.count { it.dueAt < Queries.startOfToday() }
    val weekEnd = Queries.endOfToday() + 6 * DAY
    val upcoming = data.reviews.filter { it.completedAt == null && it.ignoredAt == null && it.dueAt >= Queries.endOfToday() && it.topicId in scope }.sortedBy { it.dueAt }
    val doneToday = data.reviews.count { (it.completedAt ?: 0) >= Queries.startOfToday() && it.topicId in scope }

    PageHead("Revisões espaçadas", "Revisar na hora certa é o que faz o conteúdo ficar")
    CompetitionTabs()

    Div({ classes("rv-hero", "rise") }) {
        Div({ classes("rv-hero-main") }) {
            Span({ classes("eyebrow") }) { Text("REVISÃO ESPAÇADA") }
            H2 { Text(if (due.isEmpty()) "Tudo em dia por hoje" else "${due.size} ${if (due.size == 1) "revisão" else "revisões"} para hoje") }
            P { Text(if (due.isEmpty()) "Quando um tópico estudado chegar no dia de revisar, ele aparece aqui." else "Cada uma leva poucos minutos: lembrar, rever o resumo e testar com questões.") }
            if (due.isNotEmpty()) Button({ classes("rv-cta"); onClick { ReviewRun.active = true; Router.go(Route.ReviewSession(due.first().id)) } }) {
                Icon("play_arrow", filled = true); Text(if (due.size > 1) "Revisar tudo em sequência" else "Começar a revisão")
            }
        }
        Div({ classes("rv-hero-stats") }) {
            RvStat("${due.size}", "para hoje", "event_available", "#FFB421")
            RvStat("$late", "atrasadas", "schedule", "#FF8A80")
            RvStat("${upcoming.count { it.dueAt < weekEnd }}", "nos próximos 7 dias", "date_range", "#ABA2FF")
            RvStat("$doneToday", "feitas hoje", "task_alt", "#7EE0B8")
        }
    }

    if (due.isNotEmpty()) {
        H3({ classes("rv-section") }) { Text("Para fazer agora") }
        Div({ classes("rv-grid") }) {
            due.forEach { review ->
                key(review.id) {
                    val topic = topics[review.topicId]
                    val daysLate = ((now - review.dueAt) / DAY).toInt()
                    Div({ classes(*listOfNotNull("rv-card", if (daysLate > 0) "late" else null).toTypedArray()) }) {
                        Div({ classes("rv-card-top") }) {
                            Span({ classes("rv-subject"); attr("style", "--c:${subjectColor(subjects[topic?.subjectId]?.name ?: "")}") }) { Text((subjects[topic?.subjectId]?.name ?: "").uppercase()) }
                            if (daysLate > 0) Span({ classes("rv-late") }) { Text("atrasada $daysLate ${if (daysLate == 1) "dia" else "dias"}") }
                        }
                        B({ classes("rv-title") }) { Text(topic?.title ?: "Tópico") }
                        StageLadder(review.stage)
                        Div({ classes("rv-card-foot") }) {
                            Btn("Pular", { Store.update { Actions.ignoreReview(it, review.id) }; Toast.show("Revisão pulada") }, style = "ghost", small = true)
                            Btn("Revisar", { ReviewRun.active = false; Router.go(Route.ReviewSession(review.id)) }, style = "primary", small = true, icon = "play_arrow")
                        }
                    }
                }
            }
        }
    } else Card { Empty("task_alt", "Revisões em dia", "Nenhuma revisão pendente. Estude um tópico e marque como estudado: as revisões entram sozinhas em D+1, D+7 e D+30.") }

    if (upcoming.isNotEmpty()) {
        H3({ classes("rv-section") }) { Text("Próximas") }
        Card {
            Div({ classes("rv-timeline") }) {
                upcoming.take(30).groupBy { Queries.dateOf(it.dueAt) }.entries.take(10).forEach { (date, list) ->
                    Div({ classes("rv-day") }) {
                        Div({ classes("rv-day-date") }) {
                            B { Text("${date.day}") }
                            Span { Text(Queries.monthShort(date)) }
                        }
                        Div({ classes("rv-day-items") }) {
                            list.forEach { review ->
                                Div({ classes("rv-day-item") }) {
                                    Span({ classes("rv-stage-pill") }) { Text(stageLabels.getOrElse(review.stage - 1) { "R${review.stage}" }) }
                                    Span { Text(topics[review.topicId]?.title ?: "Tópico") }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun RvStat(value: String, label: String, icon: String, color: String) {
    Div({ classes("rv-stat"); attr("style", "--c:$color") }) {
        Icon(icon, filled = true)
        Div { B { Text(value) }; Span { Text(label) } }
    }
}

/** A escadinha das etapas: as feitas, a de agora e as que vêm. */
@Composable
private fun StageLadder(stage: Int) {
    Div({ classes("rv-ladder") }) {
        (1..4).forEach { s ->
            val state = when { s < stage -> "done"; s == stage -> "now"; else -> null }
            Div({ classes(*listOfNotNull("rv-step", state).toTypedArray()) }) {
                Span({ classes("rv-step-dot") }) { if (s < stage) Icon("check") }
                Span({ classes("rv-step-l") }) { Text(stageLabels.getOrElse(s - 1) { "R$s" }) }
            }
        }
        if (stage > 4) Span({ classes("rv-step-more") }) { Text("+ etapa $stage") }
    }
}

// ---------------------------------------------------------------- sessão

@Composable
fun ReviewSessionScreen(reviewId: Long) {
    val data = Store.data
    val review = data.reviews.firstOrNull { it.id == reviewId }
    // Acabou de concluir: a tela de fechamento, com as questões para fixar.
    ReviewDone.state?.takeIf { it.reviewId == reviewId }?.let { ReviewDoneScreen(it); return }
    if (review == null || review.completedAt != null) {
        Card { Empty("task_alt", "Revisão concluída", "Ela já foi feita ou não existe mais.", action = { Btn("Voltar às revisões", { Router.go(Route.Reviews) }) }) }
        return
    }
    HoldCelebrations()
    val topic = data.topics.firstOrNull { it.id == review.topicId }
    val subject = data.subjects.firstOrNull { it.id == topic?.subjectId }
    // Revisão espaçada vale no dia dela: antes disso, ainda é cedo (revisar antes enfraquece o efeito).
    if (review.dueAt >= Queries.endOfToday()) {
        Card {
            Empty("event", "Ainda não é dia desta revisão", "${topic?.title ?: "Este tópico"} volta em ${Queries.shortDate(Queries.dateOf(review.dueAt))}. Revisar antes da hora enfraquece o efeito da revisão espaçada; até lá, treine questões se quiser.", action = {
                Div({ classes("row", "wrap"); attr("style", "justify-content:center;gap:8px") }) {
                    Btn("Voltar às revisões", { Router.go(Route.Reviews) }, style = "outline")
                    if (data.questions.any { it.topicId == review.topicId && !it.hidden }) Btn("Treinar questões do tópico", { Router.go(Route.Quiz("topico-${review.topicId}-5")) }, style = "primary", icon = "quiz")
                }
            })
        }
        return
    }
    val recall = remember(reviewId) { data.snippets.filter { it.topicId == review.topicId && it.kind == "RECUPERACAO" && !it.answer.isNullOrBlank() }.shuffled().take(8) }
    val summary = data.summaries.firstOrNull { it.topicId == review.topicId && it.kind == "RAPIDO" } ?: data.summaries.firstOrNull { it.topicId == review.topicId }
    // Revisar é reencontrar a falha: primeiro as erradas, depois as nunca respondidas, depois o resto.
    val questions = remember(reviewId) {
        val ofTopic = data.questions.filter { it.topicId == review.topicId && !it.hidden && it.simulationId == null }
        val wrong = data.errors.filter { it.status != "CORRIGIDO" }.mapTo(hashSetOf()) { it.questionId }
        (ofTopic.filter { it.id in wrong }.shuffled() + ofTopic.filter { it.id !in wrong && it.answerCount == 0 }.shuffled() + ofTopic.filter { it.id !in wrong && it.answerCount > 0 }.shuffled()).take(3)
    }
    val steps = listOfNotNull("recall".takeIf { recall.isNotEmpty() }, "summary", "test".takeIf { questions.isNotEmpty() }, "rate")
    val startedAt = remember(reviewId) { Date.now().toLong() }
    val sessionId = remember(reviewId) { Actions.newSessionId() }
    var step by remember(reviewId) { mutableStateOf(0) }
    var recallIndex by remember(reviewId) { mutableStateOf(0) }
    var flipped by remember(reviewId) { mutableStateOf(false) }
    var recalled by remember(reviewId) { mutableStateOf(0) }
    var forgotten by remember(reviewId) { mutableStateOf(0) }
    var questionIndex by remember(reviewId) { mutableStateOf(0) }
    var selected by remember(reviewId) { mutableStateOf<String?>(null) }
    var answeredRight by remember(reviewId) { mutableStateOf<Boolean?>(null) }
    var correct by remember(reviewId) { mutableStateOf(0) }
    val current = steps[step.coerceAtMost(steps.lastIndex)]
    fun advance() { step = (step + 1).coerceAtMost(steps.lastIndex) }

    Div({ classes("rv-session") }) {
        Div({ classes("rv-top") }) {
            Btn("Sair", { Router.go(Route.Reviews) }, style = "ghost", small = true, icon = "close")
            Div({ classes("rv-top-title") }) {
                Span({ classes("xs", "muted") }) { Text("${subject?.name ?: ""} · ${stageLabels.getOrElse(review.stage - 1) { "etapa ${review.stage}" }}") }
                B { Text(topic?.title ?: "Revisão") }
            }
        }
        // Os passos da revisão, como um caminho.
        Div({ classes("rv-steps") }) {
            steps.forEachIndexed { i, s ->
                val (icon, label) = when (s) { "recall" -> "psychology" to "Lembrar"; "summary" -> "menu_book" to "Revisar"; "test" -> "quiz" to "Testar"; else -> "star" to "Avaliar" }
                Div({ classes(*listOfNotNull("rv-s", if (i < step) "done" else null, if (i == step) "now" else null).toTypedArray()) }) {
                    Span({ classes("rv-s-ic") }) { Icon(if (i < step) "check" else icon) }
                    Span({ classes("rv-s-l") }) { Text(label) }
                }
                if (i < steps.lastIndex) Span({ classes(*listOfNotNull("rv-s-line", if (i < step) "done" else null).toTypedArray()) })
            }
        }

        when (current) {
            "recall" -> {
                val card = recall[recallIndex.coerceAtMost(recall.lastIndex)]
                P({ classes("rv-hint") }) { Text("Tente lembrar antes de virar o cartão. Recuperar de memória é o que fixa.") }
                Div({ classes(*listOfNotNull("rv-flip", if (flipped) "on" else null).toTypedArray()); onClick { flipped = true } }) {
                    Div({ classes("rv-flip-in") }) {
                        Div({ classes("rv-face", "front") }) {
                            Span({ classes("rv-face-tag") }) { Text("PERGUNTA ${recallIndex + 1} DE ${recall.size}") }
                            Div({ classes("rv-face-text") }) { Inline(card.text) }
                            Span({ classes("rv-face-tap") }) { Icon("touch_app"); Text("Clique para ver a resposta") }
                        }
                        Div({ classes("rv-face", "back") }) {
                            Span({ classes("rv-face-tag") }) { Text("RESPOSTA") }
                            Div({ classes("rv-face-text") }) { Markdown(card.answer.orEmpty()) }
                        }
                    }
                }
                if (flipped) P({ classes("rv-hint"); attr("style", "margin-bottom:-4px") }) { B { Text("Você lembrou antes de virar o cartão?") } }
                if (flipped) Div({ classes("rv-choice") }) {
                    Button({ classes("rv-btn", "no"); onClick { som("nao_lembrei"); forgotten++; flipped = false; if (recallIndex < recall.lastIndex) recallIndex++ else advance() } }) { Icon("close"); Text("Não lembrei") }
                    Button({ classes("rv-btn", "yes"); onClick { som("lembrei"); recalled++; flipped = false; if (recallIndex < recall.lastIndex) recallIndex++ else advance() } }) { Icon("check"); Text("Lembrei") }
                } else Div({ classes("rv-choice") }) { Btn("Virar o cartão", { flipped = true }, style = "primary", icon = "flip") }
            }
            "summary" -> {
                Div({ classes("rv-paper") }) {
                    Span({ classes("rv-face-tag") }) { Text("REVISÃO RÁPIDA") }
                    if (summary != null) Markdown(summary.markdown)
                    else P({ classes("muted") }) { Text("Este tópico ainda não tem revisão rápida. Relembre os pontos principais e siga para as questões.") }
                }
                Div({ classes("rv-choice") }) { Btn(if (steps.contains("test")) "Ir para as questões" else "Avaliar a revisão", { advance() }, style = "primary", icon = "arrow_forward") }
            }
            "test" -> {
                val question = questions[questionIndex.coerceAtMost(questions.lastIndex)]
                ReviewQuestion(question, questionIndex, questions.size, selected, answeredRight, onSelect = { if (answeredRight == null) selected = it }) {
                    if (answeredRight == null) {
                        val key = selected ?: return@ReviewQuestion
                        var ok = false
                        Store.update { snapshot -> Actions.answer(snapshot, question.id, key, sessionId).also { ok = it.second }.first }
                        answeredRight = ok
                        if (ok) correct++
                    } else {
                        selected = null; answeredRight = null
                        if (questionIndex < questions.lastIndex) questionIndex++ else advance()
                    }
                }
            }
            else -> RateStep(review, recalled, forgotten, correct, questions.size, startedAt)
        }
    }
}

@Composable
private fun ReviewQuestion(question: Question, index: Int, total: Int, selected: String?, answeredRight: Boolean?, onSelect: (String) -> Unit, onPrimary: () -> Unit) {
    val original = question.options.sortedBy { it.position }
    val order = remember(question.id) { br.com.estudario.domain.OptionShuffle.order(original.map { it.text }, Date.now().toLong() + question.id) }
    // Alternativas riscadas, como no treino normal.
    val crossed = remember(question.id) { androidx.compose.runtime.mutableStateMapOf<String, Boolean>() }
    Div({ classes("rv-paper") }) {
        Span({ classes("rv-face-tag") }) { Text("QUESTÃO ${index + 1} DE $total") }
        QuestionStatement(question.statement)
        Div({ classes("stack", "tight"); attr("style", "margin-top:12px") }) {
            order.forEachIndexed { pos, i ->
                val option = original[i]
                val state = when {
                    answeredRight != null && option.correct -> "right"
                    answeredRight == false && option.key == selected -> "wrong"
                    option.key == selected -> "chosen"
                    else -> null
                }
                val isCrossed = crossed[option.key] == true && answeredRight == null
                Div({ classes("option-row") }) {
                    Button({
                        classes(*listOfNotNull("option", state, if (isCrossed) "crossed" else null).toTypedArray())
                        if (answeredRight != null) attr("disabled", "")
                        onClick { if (isCrossed) crossed[option.key] = false else onSelect(option.key) }
                        addEventListener("contextmenu") { e -> if (answeredRight == null) { e.preventDefault(); crossed[option.key] = !isCrossed } }
                    }) {
                        Span({ classes("key") }) { Text("ABCDEF"[pos].toString()) }
                        Span { Inline(option.text) }
                    }
                    if (answeredRight == null) Button({
                        classes("strike-btn", *listOfNotNull(if (isCrossed) "on" else null).toTypedArray())
                        attr("title", if (isCrossed) "Desfazer risco" else "Riscar alternativa")
                        onClick { crossed[option.key] = !isCrossed }
                    }) { Icon("strikethrough_s") }
                }
            }
        }
        if (answeredRight != null) Div({ classes("rv-feedback", if (answeredRight) "ok" else "bad") }) {
            B { Text(if (answeredRight) "Isso! Você lembrou." else "Ainda não. Ela vai para o caderno de erros.") }
            if (question.explanation.isNotBlank()) Markdown(br.com.estudario.domain.OptionShuffle.relabel(question.explanation, original.map { it.key }, order))
        }
    }
    Div({ classes("rv-choice") }) {
        Btn(if (answeredRight == null) "Confirmar" else if (index < total - 1) "Próxima questão" else "Avaliar a revisão", onPrimary, style = "primary", icon = if (answeredRight == null) "check" else "arrow_forward", enabled = selected != null)
    }
}

/** Quando a próxima revisão cairia com cada escolha (simula sem gravar). */
private fun nextDueWith(reviewId: Long, difficulty: ReviewDifficulty, correct: Int, total: Int): Long? {
    val review = Store.data.reviews.firstOrNull { it.id == reviewId } ?: return null
    val after = Actions.completeReview(Store.data, reviewId, difficulty, correct, total)
    return after.reviews.filter { it.topicId == review.topicId && it.completedAt == null && it.ignoredAt == null }.minOfOrNull { it.dueAt }
}

@Composable
private fun RateStep(review: Review, recalled: Int, forgotten: Int, correct: Int, total: Int, startedAt: Long) {
    // A sugestão vem do desempenho: errou mais da metade ou esqueceu mais do que lembrou, difícil.
    val recallRate = if (recalled + forgotten == 0) null else recalled.toDouble() / (recalled + forgotten)
    val questionRate = if (total == 0) null else correct.toDouble() / total
    val score = listOfNotNull(recallRate, questionRate).takeIf { it.isNotEmpty() }?.average()
    val suggested = when { score == null -> ReviewDifficulty.NORMAL; score >= 0.85 -> ReviewDifficulty.FACIL; score >= 0.5 -> ReviewDifficulty.NORMAL; else -> ReviewDifficulty.DIFICIL }
    var pick by remember(review.id) { mutableStateOf(suggested) }

    Div({ classes("rv-paper", "rv-result") }) {
        Span({ classes("rv-face-tag") }) { Text("SEU RESULTADO") }
        Div({ classes("rv-result-row") }) {
            if (recalled + forgotten > 0) RvRing(recalled, recalled + forgotten, "lembrou")
            if (total > 0) RvRing(correct, total, "acertou")
            Folha(if ((score ?: 0.6) >= 0.5) "happy" else "thinking", 84)
        }
        H3 { Text("Como foi lembrar este tópico?") }
        P({ classes("small", "muted") }) { Text("A escolha define quando ele volta. Sugerimos pelo seu desempenho, mas a decisão é sua.") }
        Div({ classes("rv-rates") }) {
            listOf(
                Triple(ReviewDifficulty.FACIL, "Fácil", "Recuperei quase tudo com segurança"),
                Triple(ReviewDifficulty.NORMAL, "Normal", "Lembrei depois de pensar um pouco"),
                Triple(ReviewDifficulty.DIFICIL, "Difícil", "Ainda confundo pontos importantes"),
            ).forEach { (difficulty, label, hint) ->
                val due = nextDueWith(review.id, difficulty, correct, total)
                val days = due?.let { ((it - Queries.startOfToday()) / DAY).toInt().coerceAtLeast(1) }
                Button({ classes(*listOfNotNull("rv-rate", difficulty.name.lowercase(), if (pick == difficulty) "on" else null).toTypedArray()); onClick { pick = difficulty } }) {
                    if (difficulty == suggested) Span({ classes("rv-suggest") }) { Text("SUGERIDO") }
                    B { Text(label) }
                    Span({ classes("xs", "muted") }) { Text(hint) }
                    Span({ classes("rv-next") }) { Icon("event"); Text(if (days == null) "em breve" else if (days == 1) "volta amanhã" else "volta em $days dias") }
                }
            }
        }
        Div({ classes("rv-choice") }) {
            Btn("Concluir revisão", {
                val days = nextDueWith(review.id, pick, correct, total)?.let { ((it - Queries.startOfToday()) / DAY).toInt().coerceAtLeast(1) }
                Store.update { Actions.completeReview(it, review.id, pick, correct, total, startedAt, recalled, forgotten) }
                ReviewDone.state = ReviewDone.State(review.id, review.topicId, days)
            }, style = "primary", icon = "check", size = "lg")
        }
    }
}

@Composable
private fun RvRing(value: Int, total: Int, label: String) {
    val pct = if (total == 0) 0 else value * 100 / total
    Div({ classes("rv-ring"); attr("style", "--p:$pct") }) {
        B { Text("$value/$total") }
        Span { Text(label) }
    }
}

/** O fechamento da revisão que acabou de ser concluída (mostrado na própria rota da sessão). */
private object ReviewDone {
    data class State(val reviewId: Long, val topicId: Long, val nextInDays: Int?)
    var state by mutableStateOf<State?>(null)
}

/**
 * Revisão concluída: quando o tópico volta e, para fixar de verdade, algumas questões dele agora
 * (as erradas e as inéditas primeiro). Sem questões no tópico, oferece gerar.
 */
@Composable
private fun ReviewDoneScreen(done: ReviewDone.State) {
    val data = Store.data
    val topic = data.topics.firstOrNull { it.id == done.topicId }
    val available = data.questions.count { it.topicId == done.topicId && !it.hidden && it.simulationId == null }
    val nextDue = Queries.dueReviews(data).filter { it.id != done.reviewId }.minByOrNull { it.dueAt }
    fun leave(go: () -> Unit) { ReviewDone.state = null; go() }
    Div({ classes("rv-paper", "rv-done") }) {
        Folha("happy", 110)
        Span({ classes("rv-face-tag") }) { Text("REVISÃO CONCLUÍDA") }
        Span({ classes("rv-xp") }) { Icon("Bolt"); Text("+${br.com.estudario.domain.ProgressEngine.previewReview().max} XP") }
        H2 { Text(topic?.title ?: "Tópico revisado") }
        P({ classes("muted") }) { Text(done.nextInDays?.let { if (it <= 1) "Ele volta amanhã para mais uma revisão." else "Ele volta em $it dias. Até lá, a memória trabalha por você." } ?: "A próxima revisão já está na agenda.") }
        Div({ classes("rv-fix") }) {
            Span({ classes("rv-fix-ic") }) { Icon("quiz", filled = true) }
            Div({ classes("grow") }) {
                B { Text("Fixe agora com questões") }
                Span({ classes("small", "muted") }) { Text(if (available > 0) "Responder logo depois de revisar é o que mais segura o conteúdo." else "Este tópico ainda não tem questões. Gere um lote para treinar.") }
            }
        }
        Div({ classes("rv-choice") }) {
            if (available > 0) Btn("Fazer ${minOf(5, available)} questões", { leave { Router.go(Route.Quiz("topico-${done.topicId}-5")) } }, style = "primary", icon = "play_arrow", size = "lg")
            else Btn("Gerar questões", { leave { Router.go(Route.Topic(done.topicId)) } }, style = "primary", icon = "auto_awesome", size = "lg")
            if (ReviewRun.active && nextDue != null) Btn("Próxima revisão", { leave { Router.go(Route.ReviewSession(nextDue.id)) } }, style = "outline", icon = "skip_next")
            else Btn("Voltar às revisões", { leave { ReviewRun.active = false; Router.go(Route.Reviews) } }, style = "ghost")
        }
    }
}
