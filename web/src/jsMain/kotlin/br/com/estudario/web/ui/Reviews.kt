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
import br.com.estudario.web.data.ReviewPreferences
import br.com.estudario.web.data.reviewPracticeQuestions
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
/** "2ª revisão · 7 dias": a etapa e o intervalo de verdade (antes eram rótulos fixos D+1, D+7, D+30 que não batiam). */
private fun stageName(review: Review): String = "${review.stage}ª revisão" + (review.intervalDays?.let { " · ${it} ${if (it == 1L) "dia" else "dias"}" } ?: "")

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

    PageHead("Revisões espaçadas", "Revisar na hora certa é o que faz o conteúdo ficar", actions = { IconButton("settings", "Configurar revisão") { ReviewPracticePanels.openSettings() } })
    CompetitionTabs()

    // ---- o topo: quantas para hoje e o botão de começar, no visual do estúdio
    Div({ classes("gx-panel", "rvx-hero") }) {
        Div({ classes("rvx-hero-top") }) {
            Div({ classes("rvx-folha") }) { Folha(if (due.isEmpty()) "proud" else if (late > 0) "talking" else "happy", 88) }
            Div({ classes("rvx-hero-txt") }) {
                Span({ classes("gx-kicker") }) { Text("REVISÃO ESPAÇADA") }
                H2 { Text(if (due.isEmpty()) "Tudo em dia por hoje" else "${due.size} ${if (due.size == 1) "revisão" else "revisões"} para hoje") }
                P { Text(if (due.isEmpty()) "Quando um tópico estudado chegar no dia de revisar, ele aparece aqui." else "Cada uma leva poucos minutos: lembrar, rever o resumo e testar com questões.") }
            }
        }
        if (due.isNotEmpty()) Div({ classes("rvx-cta") }) {
            GxLaunch(if (due.size > 1) "Revisar tudo em sequência" else "Começar a revisão", icon = "play_circle") { ReviewRun.active = true; Router.go(Route.ReviewSession(due.first().id)) }
        }
        Div({ classes("rvx-stats") }) {
            RvxStat("${due.size}", "para hoje", "event_available", "#FFC94D")
            RvxStat("$late", "atrasadas", "schedule", "#FF5A7A")
            RvxStat("${upcoming.count { it.dueAt < weekEnd }}", "nos próximos 7 dias", "date_range", "#A78BFF")
            RvxStat("$doneToday", "feitas hoje", "task_alt", "#2FD39A")
        }
    }

    // ---- para fazer agora: uma lista curta, as atrasadas primeiro
    if (due.isNotEmpty()) {
        var allDue by remember { mutableStateOf(false) }
        val ordered = due.sortedWith(compareBy({ it.dueAt >= Queries.startOfToday() }, { it.dueAt }))
        Div({ classes("gx-panel", "rvx-panel") }) {
            Div({ classes("rvx-head") }) {
                Span({ classes("rvx-head-ic"); attr("style", "--c:#FFC94D") }) { Icon("event_available", filled = true) }
                Div({ classes("grow") }) { B { Text("Para fazer agora") }; Span { Text(if (late > 0) "$late ${if (late == 1) "atrasada vem" else "atrasadas vêm"} primeiro" else "Na ordem do dia") } }
                Span({ classes("rvx-count") }) { Text("${due.size}") }
            }
            Div({ classes("rvx-list") }) {
                (if (allDue) ordered else ordered.take(5)).forEach { review ->
                    key(review.id) {
                        val topic = topics[review.topicId]
                        val subjectName = subjects[topic?.subjectId]?.name ?: ""
                        val daysLate = ((now - review.dueAt) / DAY).toInt()
                        Div({ classes(*listOfNotNull("rvx-row", "now", if (daysLate > 0) "late" else null).toTypedArray()); attr("style", "--c:${subjectColor(subjectName)}") }) {
                            Button({ classes("rvx-row-main"); attr("type", "button"); onClick { ReviewRun.active = false; Router.go(Route.ReviewSession(review.id)) } }) {
                                Span({ classes("rvx-stage") }) { B { Text("${review.stage}ª") }; Span { Text("etapa") } }
                                Span({ classes("rvx-row-txt") }) {
                                    Span({ classes("rvx-subject") }) { Text(subjectName.ifBlank { "Matéria" }) }
                                    B { Text(topic?.title ?: "Tópico") }
                                    Span({ classes("rvx-meta") }) {
                                        Text(
                                            when {
                                                daysLate > 0 -> "atrasada $daysLate ${if (daysLate == 1) "dia" else "dias"} · ${stageName(review)}"
                                                review.perceivedDifficulty == "DIFICIL" -> "repetindo · ${stageName(review)}"
                                                else -> stageName(review)
                                            },
                                        )
                                    }
                                }
                                Span({ classes("rvx-go") }) { Icon("play_arrow", plain = true) }
                            }
                            Button({ classes("rvx-skip"); attr("type", "button"); attr("title", "Pular esta revisão"); attr("aria-label", "Pular esta revisão"); onClick { Store.update { Actions.ignoreReview(it, review.id) }; Toast.show("Revisão pulada") } }) { Text("Pular") }
                        }
                    }
                }
            }
            if (ordered.size > 5) Button({ classes("gx-more"); attr("type", "button"); onClick { allDue = !allDue } }) {
                Text(if (allDue) "Mostrar menos" else "Ver todas (${ordered.size})")
                Icon(if (allDue) "expand_less" else "expand_more", plain = true)
            }
        }
    } else Div({ classes("gx-panel", "rvx-panel", "rvx-empty") }) {
        Span({ classes("rvx-head-ic"); attr("style", "--c:#2FD39A") }) { Icon("task_alt", filled = true) }
        Div { B { Text("Revisões em dia") }; P { Text("Nenhuma revisão pendente. Estude um tópico e marque como estudado: as revisões entram sozinhas em D+1, D+7 e D+30.") } }
    }

    // ---- próximas: linha do tempo por quando, cada grupo fechado em poucas linhas
    if (upcoming.isNotEmpty()) {
        var subjectFilter by remember { mutableStateOf(-1L) }
        val subjectOptions = listOf(-1L to "Todas") + upcoming.mapNotNull { topics[it.topicId]?.subjectId }.distinct()
            .mapNotNull { id -> subjects[id]?.let { id to it.name } }.sortedBy { it.second }
        val filtered = if (subjectFilter < 0) upcoming else upcoming.filter { topics[it.topicId]?.subjectId == subjectFilter }
        val tomorrowEnd = Queries.endOfToday() + DAY
        val monthEnd = Queries.endOfToday() + 30 * DAY
        val groups = listOf(
            Triple("Amanhã", "#FFC94D", filtered.filter { it.dueAt < tomorrowEnd }),
            Triple("Nesta semana", "#A78BFF", filtered.filter { it.dueAt in tomorrowEnd until weekEnd }),
            Triple("Neste mês", "#22D3EE", filtered.filter { it.dueAt in weekEnd until monthEnd }),
            Triple("Mais adiante", "#2FD39A", filtered.filter { it.dueAt >= monthEnd }),
        ).filter { it.third.isNotEmpty() }
        Div({ classes("gx-panel", "rvx-panel") }) {
            Div({ classes("rvx-head") }) {
                Span({ classes("rvx-head-ic"); attr("style", "--c:#A78BFF") }) { Icon("date_range", filled = true) }
                Div({ classes("grow") }) { B { Text("Próximas") }; Span { Text("O que volta para revisar, por quando") } }
                Span({ classes("rvx-count") }) { Text("${upcoming.size}") }
            }
            if (subjectOptions.size > 2) Div({ classes("rvx-filter") }) {
                Span({ classes("gx-label") }) { Text("Matéria") }
                GxPicker(subjectOptions, subjectFilter, limit = 6, searchHint = "Buscar matéria") { subjectFilter = it }
            }
            if (groups.isEmpty()) P({ classes("gx-note") }) { Text("Nada dessa matéria nas próximas revisões.") }
            Div({ classes("rvx-groups") }) {
                groups.forEach { (label, color, list) ->
                    key(label) {
                        var open by remember(subjectFilter) { mutableStateOf(false) }
                        Div({ classes("rvx-group"); attr("style", "--g:$color") }) {
                            Div({ classes("rvx-group-head") }) {
                                Span({ classes("rvx-dot") })
                                B { Text(label) }
                                Span({ classes("rvx-group-n") }) { Text("${list.size} ${if (list.size == 1) "revisão" else "revisões"}") }
                            }
                            Div({ classes("rvx-list", "soon") }) {
                                (if (open) list else list.take(3)).forEach { review ->
                                    key(review.id) {
                                        val topic = topics[review.topicId]
                                        val date = Queries.dateOf(review.dueAt)
                                        val subjectName = subjects[topic?.subjectId]?.name ?: ""
                                        Div({ classes("rvx-row", "soon"); attr("style", "--c:${subjectColor(subjectName)}") }) {
                                            Span({ classes("rvx-date") }) { B { Text("${date.day}") }; Span { Text(Queries.monthShort(date)) } }
                                            Span({ classes("rvx-row-txt") }) {
                                                B { Text(topic?.title ?: "Tópico") }
                                                Span({ classes("rvx-meta") }) { Text(listOf(subjectName, "${review.stage}ª revisão").filter { it.isNotBlank() }.joinToString(" · ")) }
                                            }
                                        }
                                    }
                                }
                            }
                            if (list.size > 3) Button({ classes("gx-more"); attr("type", "button"); onClick { open = !open } }) {
                                Text(if (open) "Mostrar menos" else "Ver todas (${list.size})")
                                Icon(if (open) "expand_less" else "expand_more", plain = true)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun RvxStat(value: String, label: String, icon: String, color: String) {
    Div({ classes("rvx-stat"); attr("style", "--c:$color") }) {
        Span({ classes("rvx-stat-ic") }) { Icon(icon, filled = true) }
        Div { B { Text(value) }; Span { Text(label) } }
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
private fun StageLadder(stage: Int, repeating: Boolean = false, note: Boolean = true) {
    // Uma janela de cinco etapas em volta da atual: na 7ª revisão, mostra da 5ª à 9ª (antes parava na 4ª).
    val first = (stage - 2).coerceAtLeast(1)
    Div({ classes("rv-ladder") }) {
        (first until first + 5).forEach { s ->
            val state = when { s < stage -> "done"; s == stage -> if (repeating) "now repeat" else "now"; else -> null }
            Div({ classes(*listOfNotNull("rv-step", *state?.split(" ")?.toTypedArray().orEmpty()).toTypedArray()) }) {
                Span({ classes("rv-step-dot") }) { if (s < stage) Icon("check") else if (s == stage && repeating) Icon("replay") }
                Span({ classes("rv-step-l") }) { Text("${s}ª") }
            }
        }
    }
    if (repeating && note) P({ classes("rv-repeat-note") }) { Icon("replay"); Text("Repetindo esta etapa: volta todo dia até você marcar Normal ou Fácil.") }
}

/** Modelos prontos de revisão: rápida, equilibrada e completa. */
private val RV_PRESETS = listOf(
    Triple("bolt", "Rápida", br.com.estudario.domain.ReviewPracticeOptions(questionCount = 3, flashcardCount = 4, showSummary = false, offerHighlights = false)),
    Triple("balance", "Equilibrada", br.com.estudario.domain.ReviewPracticeOptions(questionCount = 5, flashcardCount = 8)),
    Triple("workspace_premium", "Completa", br.com.estudario.domain.ReviewPracticeOptions(questionCount = 10, flashcardCount = 15)),
)

/**
 * "Monte sua revisão", à vista na abertura (antes ficava escondido na engrenagem): modelos prontos,
 * quantos cartões e questões, o que consultar e a ordem. Muda esta sessão na hora e fica salvo.
 */
@Composable
private fun ReviewTuner(config: br.com.estudario.domain.ReviewPracticeOptions, cards: Int, questions: Int, hasSummary: Boolean, hasHighlights: Boolean, onChange: (br.com.estudario.domain.ReviewPracticeOptions) -> Unit) {
    fun same(a: br.com.estudario.domain.ReviewPracticeOptions, b: br.com.estudario.domain.ReviewPracticeOptions) =
        // compara pelo que o tópico tem: com 6 cartões, "Completa" (15) e 6 são a mesma escolha
        minOf(a.questionCount, questions) == minOf(b.questionCount, questions) && minOf(a.flashcardCount, cards) == minOf(b.flashcardCount, cards) &&
            a.showSummary == b.showSummary && a.offerHighlights == b.offerHighlights
    Div({ classes("rv-tuner") }) {
        Div({ classes("rv-tuner-head") }) {
            B { Text("Monte sua revisão") }
            Span({ classes("xs", "muted") }) { Text("Vale agora e fica salvo para as próximas") }
        }
        Div({ classes("rv-presets") }) {
            RV_PRESETS.forEach { (icon, name, preset) ->
                val on = same(config, preset)
                Button({
                    classes(*listOfNotNull("rv-preset", if (on) "on" else null).toTypedArray())
                    attr("aria-pressed", on.toString())
                    onClick { onChange(preset.copy(order = config.order, shuffleFlashcards = config.shuffleFlashcards, showHighlightNotes = config.showHighlightNotes)) }
                }) {
                    Icon(icon, filled = true)
                    B { Text(name) }
                    Span { Text("${minOf(preset.flashcardCount, cards)} ${if (minOf(preset.flashcardCount, cards) == 1) "cartão" else "cartões"} · ${minOf(preset.questionCount, questions)} ${if (minOf(preset.questionCount, questions) == 1) "questão" else "questões"}") }
                }
            }
        }
        RvCount("psychology", "Cartões para lembrar", config.flashcardCount, cards, 30) { onChange(config.copy(flashcardCount = it)) }
        RvCount("quiz", "Questões", config.questionCount, questions, 50) { onChange(config.copy(questionCount = it)) }
        Div({ classes("rv-toggles") }) {
            RvToggle("menu_book", "Revisão rápida", config.showSummary, enabled = hasSummary) { onChange(config.copy(showSummary = it)) }
            RvToggle("bookmarks", "Oferecer meus grifos", config.offerHighlights, enabled = hasHighlights) { onChange(config.copy(offerHighlights = it)) }
            RvToggle("sticky_note_2", "Notas dos grifos", config.showHighlightNotes, enabled = hasHighlights) { onChange(config.copy(showHighlightNotes = it)) }
            RvToggle("shuffle", "Embaralhar cartões", config.shuffleFlashcards) { onChange(config.copy(shuffleFlashcards = it)) }
        }
        Div({ classes("rv-order") }) {
            Span({ classes("xs", "muted") }) { Text("Questões primeiro:") }
            listOf(
                br.com.estudario.domain.ReviewQuestionOrder.ERRORS_FIRST to "as que errei",
                br.com.estudario.domain.ReviewQuestionOrder.UNSEEN_FIRST to "as novas",
                br.com.estudario.domain.ReviewQuestionOrder.MIXED to "misturado",
            ).forEach { (order, label) ->
                Button({ classes(*listOfNotNull("fchip", if (config.order == order) "on" else null).toTypedArray()); onClick { onChange(config.copy(order = order)) } }) { Text(label) }
            }
        }
    }
}

/** Um contador da revisão (− número +), com quantos o tópico tem. */
@Composable
private fun RvCount(icon: String, label: String, value: Int, available: Int, max: Int, onChange: (Int) -> Unit) {
    val shown = value.coerceAtMost(available)
    Div({ classes("rv-count") }) {
        Span({ classes("rv-plan-ic") }) { Icon(icon, filled = true) }
        Div({ classes("grow") }) {
            B { Text(label) }
            Span({ classes("xs", "muted") }) { Text(if (available == 0) "Este tópico ainda não tem" else if (value > available) "Vai usar as $available que o tópico tem" else "O tópico tem $available") }
        }
        Button({ classes("rv-step-btn"); attr("aria-label", "Menos"); if (value <= 0) attr("disabled", ""); onClick { onChange((value - 1).coerceAtLeast(0)) } }) { Icon("remove") }
        B({ classes("rv-count-n") }) { Text("$shown") }
        Button({ classes("rv-step-btn"); attr("aria-label", "Mais"); if (value >= max || value >= available) attr("disabled", ""); onClick { onChange((value + 1).coerceAtMost(max)) } }) { Icon("add") }
    }
}

@Composable
private fun RvToggle(icon: String, label: String, on: Boolean, enabled: Boolean = true, onChange: (Boolean) -> Unit) {
    Button({
        classes(*listOfNotNull("rv-toggle", if (on && enabled) "on" else null).toTypedArray())
        attr("role", "switch"); attr("aria-checked", (on && enabled).toString())
        if (!enabled) { attr("disabled", ""); attr("title", "Este tópico ainda não tem") }
        onClick { onChange(!on) }
    }) {
        Icon(icon, filled = true)
        Span({ classes("rv-toggle-l") }) { Text(label) }
        Span({ classes("rv-switch") })
    }
}

/** Um item da abertura da revisão: o ícone, o que é e para que serve. */
@Composable
private fun RvPlanItem(icon: String, title: String, hint: String) {
    Div({ classes("rv-plan-item") }) {
        Span({ classes("rv-plan-ic") }) { Icon(icon, filled = true) }
        Div { B { Text(title) }; Span({ classes("xs", "muted") }) { Text(hint) } }
    }
}

// ---------------------------------------------------------------- sessão

@Composable
fun ReviewSessionScreen(reviewId: Long) {
    val data = Store.data
    val review = data.reviews.firstOrNull { it.id == reviewId }
    // Acabou de concluir: a tela de fechamento, com as questões para fixar.
    ReviewDone.state?.takeIf { it.reviewId == reviewId }?.let { ReviewDoneScreen(it); return }
    if (review == null || review.completedAt != null || review.ignoredAt != null) {
        Card { Empty("task_alt", "Revisão concluída", "Ela já foi feita ou não existe mais.", action = { Btn("Voltar às revisões", { Router.go(Route.Reviews) }) }) }
        return
    }
    if (data.reviews.any { it.topicId == review.topicId && it.stage < review.stage && it.completedAt == null && it.ignoredAt == null }) {
        Card { Empty("event", "Há uma etapa anterior pendente", "Conclua a etapa anterior com Normal ou Fácil para avançar nesta revisão.", action = { Btn("Voltar às revisões", { Router.go(Route.Reviews) }) }) }
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
    var config by remember(reviewId) { mutableStateOf(ReviewPreferences.value) }
    val material = remember(reviewId) { data }
    val practiceSeed = remember(reviewId) { Date.now().toLong() }
    val topicTheories = material.theories.filter { it.topicId == review.topicId }
    val theoryOrder = topicTheories.mapIndexed { index, theory -> theory.id to index }.toMap()
    val highlights = remember(reviewId, config) {
        material.theoryMarks.filter { it.theoryId in theoryOrder && it.quote.isNotBlank() }
            .sortedWith(compareBy({ theoryOrder[it.theoryId] }, { it.blockIndex }, { it.createdAt }))
            .map { NbCard("GRIFO", topicTheories.firstOrNull { theory -> theory.id == it.theoryId }?.title.orEmpty(), it.quote, it.note.takeIf { _ -> config.showHighlightNotes }, review.topicId) }
    }
    val recall = remember(reviewId, config) {
        val cards = material.snippets.filter { it.topicId == review.topicId && it.kind == "RECUPERACAO" && !it.answer.isNullOrBlank() }.sortedWith(compareBy({ it.position }, { it.id }))
        (if (config.shuffleFlashcards) cards.shuffled(kotlin.random.Random(practiceSeed)) else cards).take(config.flashcardCount)
    }
    val summary = material.summaries.firstOrNull { it.topicId == review.topicId && it.kind == "RAPIDO" } ?: material.summaries.firstOrNull { it.topicId == review.topicId }
    // Revisar é reencontrar a falha: primeiro as erradas, depois as nunca respondidas, depois o resto.
    val questions = remember(reviewId, config) { reviewPracticeQuestions(material, review.topicId, config, practiceSeed) }
    val consultation = config.showSummary || (config.offerHighlights && highlights.isNotEmpty())
    // A abertura ("start") diz o que vem e quanto tempo leva; sem nada para fazer, vai direto ao ajuste.
    val activities = listOfNotNull("recall".takeIf { recall.isNotEmpty() }, "summary".takeIf { consultation }, "test".takeIf { questions.isNotEmpty() })
    val steps = (if (activities.isEmpty()) emptyList() else listOf("start")) + activities + "rate"
    // Difícil na última vez: a mesma etapa volta todo dia até a pessoa marcar Normal ou Fácil.
    val repeating = review.perceivedDifficulty == "DIFICIL"
    val lastSeen = (data.reviews.filter { it.topicId == review.topicId }.mapNotNull { it.completedAt } + listOfNotNull(topic?.lastReviewedAt)).maxOrNull()
    val minutes = (recall.size * 0.7 + (if (config.showSummary && summary != null) 2.0 else 0.0) + questions.size * 1.5 + 0.5).toInt().coerceAtLeast(1)
    fun openSettings() { ReviewPracticePanels.openSettings { if (steps.size == 1) config = ReviewPreferences.value } }
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
    // Respostas desta revisão, na ordem: o Folha reage como no treino (ufa, sequência, acolhe).
    var results by remember(reviewId) { mutableStateOf(listOf<Boolean>()) }
    val current = steps[step.coerceAtMost(steps.lastIndex)]
    // Quanto da revisão já foi: cada cartão e cada questão contam um passinho.
    val totalUnits = recall.size + (if (consultation) 1 else 0) + questions.size + 1
    val doneUnits = when (current) {
        "start" -> 0
        "recall" -> recallIndex
        "summary" -> recall.size
        "test" -> recall.size + (if (consultation) 1 else 0) + questionIndex + (if (answeredRight != null) 1 else 0)
        else -> totalUnits - 1
    }
    fun advance() { step = (step + 1).coerceAtMost(steps.lastIndex) }

    Div({ classes("rv-session") }) {
        Div({ classes("rv-top") }) {
            Btn("Sair", { Router.go(Route.Reviews) }, style = "ghost", small = true, icon = "close")
            Div({ classes("rv-top-title") }) {
                Span({ classes("xs", "muted") }) { Text("${subject?.name ?: ""} · ${stageName(review)}") }
                B { Text(topic?.title ?: "Revisão") }
                Span({ classes("xs", "muted") }) { Text("${recall.size} ${if (recall.size == 1) "flashcard" else "flashcards"} · ${questions.size} ${if (questions.size == 1) "questão" else "questões"} · ~$minutes min") }
            }
            // Os grifos da matéria, a qualquer momento e só para ler (não dá para mudar daqui).
            if (highlights.isNotEmpty()) Button({
                classes("rv-marks-btn"); attr("title", "Ler meus grifos (só leitura)")
                onClick { ReviewPracticePanels.highlights = "Grifos · ${topic?.title.orEmpty()}" to highlights }
            }) { Icon("bookmarks", filled = true); B { Text("${highlights.size}") } }
        }
        Div({ classes("rv-progress"); attr("role", "progressbar"); attr("aria-valuenow", "$doneUnits"); attr("aria-valuemax", "$totalUnits") }) {
            Span({ attr("style", "width:${(doneUnits * 100 / totalUnits.coerceAtLeast(1))}%") })
        }
        // Os passos da revisão, como um caminho.
        if (current != "start") Div({ classes("rv-steps") }) {
            steps.filter { it != "start" }.forEachIndexed { i0, s ->
                val i = i0 + (if (steps.first() == "start") 1 else 0)
                val (icon, label) = when (s) { "recall" -> "psychology" to "Lembrar"; "summary" -> "menu_book" to "Consultar"; "test" -> "quiz" to "Testar"; else -> "star" to "Avaliar" }
                Div({ classes(*listOfNotNull("rv-s", if (i < step) "done" else null, if (i == step) "now" else null).toTypedArray()) }) {
                    Span({ classes("rv-s-ic") }) { Icon(if (i < step) "check" else icon) }
                    Span({ classes("rv-s-l") }) { Text(label) }
                }
                if (i < steps.lastIndex) Span({ classes(*listOfNotNull("rv-s-line", if (i < step) "done" else null).toTypedArray()) })
            }
        }

        when (current) {
            // A abertura: o Folha chama para relembrar, diz o que vem e quanto leva.
            "start" -> Div({ classes("rv-start", "rise") }) {
                val since = lastSeen?.let { ((Date.now().toLong() - it) / DAY).toInt() }
                FolhaSays(if (repeating) "talking" else "happy", 96) {
                    B { Text(if (repeating) "De novo hoje, e tudo bem!" else "Bora relembrar ${topic?.title ?: "este tópico"}?") }
                    P { Text(when {
                        repeating -> "Na última você marcou Difícil, então ele voltou na mesma etapa. Ele volta todo dia até ficar Normal ou Fácil: é assim que fixa."
                        since == null -> "Recuperar de memória agora é o que faz isso ficar para a prova."
                        since <= 0 -> "Você viu isso hoje mesmo. Uma passada rápida e está garantido."
                        since == 1 -> "Faz 1 dia que você viu isso. É o momento certo de puxar da memória."
                        else -> "Faz $since dias que você viu isso. É o momento certo de puxar da memória, antes de esquecer."
                    }) }
                }
                Div({ classes("rv-start-plan") }) {
                    if (recall.isNotEmpty()) RvPlanItem("psychology", "${recall.size} ${if (recall.size == 1) "cartão" else "cartões"} para lembrar", "Tente responder antes de virar")
                    if (consultation) RvPlanItem("menu_book", if (config.showSummary) "Revisão rápida" else "Seus grifos", "Para conferir o que você lembrou")
                    if (questions.isNotEmpty()) RvPlanItem("quiz", "${questions.size} ${if (questions.size == 1) "questão" else "questões"}", "As que você errou vêm primeiro")
                    RvPlanItem("star", "Como foi", "Difícil volta amanhã; Normal e Fácil avançam a etapa")
                }
                val cardsAvailable = material.snippets.count { it.topicId == review.topicId && it.kind == "RECUPERACAO" && !it.answer.isNullOrBlank() }
                val questionsAvailable = data.questions.count { it.topicId == review.topicId && !it.hidden && it.simulationId == null }
                val hasHighlights = material.theoryMarks.any { it.theoryId in theoryOrder && it.quote.isNotBlank() }
                ReviewTuner(config, cardsAvailable, questionsAvailable, summary != null, hasHighlights) { next ->
                    config = next
                    ReviewPreferences.save(next)
                }
                StageLadder(review.stage, repeating)
                Div({ classes("rv-choice") }) {
                    Btn("Começar · ~$minutes min", { advance() }, style = "primary", icon = "play_arrow", size = "lg")
                }
            }
            "recall" -> {
                val card = recall[recallIndex.coerceAtMost(recall.lastIndex)]
                P({ classes("rv-hint") }) { Text("Tente lembrar antes de virar o cartão. Recuperar de memória é o que fixa.") }
                Div({
                    classes(*listOfNotNull("rv-flip", if (flipped) "on" else null).toTypedArray())
                    attr("role", "button"); attr("tabindex", "0"); attr("aria-label", if (flipped) "Voltar à pergunta" else "Ver a resposta")
                    onClick { flipped = !flipped }
                    onKeyDown { if (it.key == "Enter" || it.key == " ") { it.preventDefault(); flipped = !flipped } }
                }) {
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
                if (flipped) Div({ classes("rv-choice") }) { Btn("Voltar à pergunta", { flipped = false }, style = "ghost", icon = "flip") }
            }
            "summary" -> {
                if (config.offerHighlights && highlights.isNotEmpty()) Div({ classes("rv-paper", "rv-highlights") }) {
                    Span({ classes("rv-face-tag") }) { Text("SEUS GRIFOS") }
                    H3 { Text("Você já tentou lembrar. Quer conferir o que destacou?") }
                    P({ classes("muted") }) { Text("Leia seus ${highlights.size} trechos, um por vez, e volte para continuar a revisão.") }
                    Btn("Ler meus grifos", { ReviewPracticePanels.highlights = "Grifos · ${topic?.title.orEmpty()}" to highlights }, style = "outline", icon = "bookmarks")
                }
                if (config.showSummary) Div({ classes("rv-paper") }) {
                    Span({ classes("rv-face-tag") }) { Text("REVISÃO RÁPIDA") }
                    if (summary != null) Markdown(summary.markdown)
                    else P({ classes("muted") }) { Text("Este tópico ainda não tem revisão rápida. Relembre os pontos principais e siga para as questões.") }
                }
                Div({ classes("rv-choice") }) { Btn(if (steps.contains("test")) "Ir para as questões" else "Avaliar a revisão", { advance() }, style = "primary", icon = "arrow_forward") }
            }
            "test" -> {
                val question = questions[questionIndex.coerceAtMost(questions.lastIndex)]
                ReviewQuestion(question, questionIndex, questions.size, selected, answeredRight, results, onSelect = { if (answeredRight == null) selected = it }) {
                    if (answeredRight == null) {
                        val key = selected ?: return@ReviewQuestion
                        var ok = false
                        Store.update { snapshot -> Actions.answer(snapshot, question.id, key, sessionId).also { ok = it.second }.first }
                        answeredRight = ok
                        if (ok) correct++
                        results = results + ok
                    } else {
                        selected = null; answeredRight = null
                        if (questionIndex < questions.lastIndex) questionIndex++ else advance()
                    }
                }
            }
            else -> if (steps.size == 1) Card { Empty("settings", "Monte sua revisão", "Não há atividade disponível com suas escolhas. Ative flashcards, resumo ou questões na engrenagem.", action = { Btn("Ajustar revisão", { openSettings() }, style = "primary", icon = "settings") }) }
                else RateStep(review, recalled, forgotten, correct, questions.size, startedAt)
        }
    }
}

@Composable
private fun ReviewQuestion(question: Question, index: Int, total: Int, selected: String?, answeredRight: Boolean?, results: List<Boolean>, onSelect: (String) -> Unit, onPrimary: () -> Unit) {
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
        if (answeredRight != null) {
            // O Folha reage como no treino; a letra certa é a da ordem embaralhada desta tela.
            val rightLetter = order.indexOfFirst { original[it].correct }.takeIf { it >= 0 }?.let { "ABCDEF"[it].toString() }
            AnswerReaction(answeredRight, rightLetter, question.id, results)
            if (question.explanation.isNotBlank()) Div({ classes("rv-feedback", if (answeredRight) "ok" else "bad") }) {
                B { Text("Por quê") }
                Markdown(br.com.estudario.domain.OptionShuffle.relabel(question.explanation, original.map { it.key }, order))
            }
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
        P({ classes("small", "muted") }) { Text("Difícil repete amanhã sem avançar a etapa. Normal e Fácil avançam. A sugestão vem do seu desempenho; a decisão é sua.") }
        Div({ classes("rv-rates") }) {
            listOf(
                Triple(ReviewDifficulty.FACIL, "Fácil", "Recuperei quase tudo com segurança"),
                Triple(ReviewDifficulty.NORMAL, "Normal", "Lembrei depois de pensar um pouco"),
                Triple(ReviewDifficulty.DIFICIL, "Difícil", "Repito amanhã, na mesma etapa"),
            ).forEach { (difficulty, label, hint) ->
                val due = nextDueWith(review.id, difficulty, correct, total)
                val days = due?.let { br.com.estudario.time.daysBetween(Queries.todayDate(), Queries.dateOf(it)).toInt().coerceAtLeast(1) }
                Button({ classes(*listOfNotNull("rv-rate", difficulty.name.lowercase(), if (pick == difficulty) "on" else null).toTypedArray()); onClick { pick = difficulty } }) {
                    if (difficulty == suggested) Span({ classes("rv-suggest") }) { Text("SUGERIDO") }
                    B { Text(label) }
                    Span({ classes("xs", "muted") }) { Text(hint) }
                    Span({ classes("rv-next") }) { Icon("event"); Text(if (days == null) "em breve" else if (days == 1) "volta amanhã" else "volta em $days dias") }
                }
            }
        }
        Div({ classes("rv-choice") }) {
            Btn(if (pick == ReviewDifficulty.DIFICIL) "Salvar e repetir amanhã" else "Concluir etapa", {
                val days = nextDueWith(review.id, pick, correct, total)?.let { br.com.estudario.time.daysBetween(Queries.todayDate(), Queries.dateOf(it)).toInt().coerceAtLeast(1) }
                Store.update { Actions.completeReviewWithPlan(it, review.id, pick, correct, total, startedAt, recalled, forgotten) }
                ReviewDone.state = ReviewDone.State(review.id, review.topicId, days, review.stage.takeIf { pick == ReviewDifficulty.DIFICIL })
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
    data class State(val reviewId: Long, val topicId: Long, val nextInDays: Int?, val repeatedStage: Int? = null)
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
        Span({ classes("rv-face-tag") }) { Text(if (done.repeatedStage != null) "SESSÃO REGISTRADA · ETAPA MANTIDA" else "ETAPA CONCLUÍDA") }
        Span({ classes("rv-xp") }) { Icon("Bolt"); Text("+${br.com.estudario.domain.ProgressEngine.previewReview().max} XP") }
        H2 { Text(topic?.title ?: "Tópico revisado") }
        P({ classes("muted") }) { Text(if (done.repeatedStage != null) "A ${done.repeatedStage}ª revisão volta amanhã. Você avança quando marcar Normal ou Fácil."
            else done.nextInDays?.let { if (it <= 1) "Ele volta amanhã para mais uma revisão." else "Ele volta em $it dias. Até lá, a memória trabalha por você." } ?: "A próxima revisão já está na agenda.") }
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
