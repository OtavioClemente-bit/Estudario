package br.com.estudario.web.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import br.com.estudario.data.local.ReviewDifficulty
import br.com.estudario.web.Route
import br.com.estudario.web.Router
import br.com.estudario.web.data.Actions
import br.com.estudario.web.data.Queries
import br.com.estudario.web.data.Review
import br.com.estudario.web.data.Store
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H2
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text
import kotlin.js.Date

// ------------------------------------------------------------------ revisões

@Composable
fun ReviewsScreen() {
    val data = Store.data
    val topics = data.topics.associateBy { it.id }
    val subjects = data.subjects.associateBy { it.id }
    val due = Queries.dueReviews(data)
    val upcoming = data.reviews.filter { it.completedAt == null && it.ignoredAt == null && it.dueAt >= Queries.endOfToday() }.sortedBy { it.dueAt }.take(12)
    var reviewing by remember { mutableStateOf<Review?>(null) }

    PageHead("Revisões", "Revisões espaçadas: D+1, D+7, D+30 e depois intervalos que crescem com você")
    Card {
        CardHead(if (due.isEmpty()) "Nada para hoje" else "Para fazer agora (${due.size})")
        if (due.isEmpty()) {
            Empty("task_alt", "Revisões em dia", "Quando um tópico estudado chegar no dia de revisar, ele aparece aqui.")
        } else {
            Div({ classes("stack") }) {
                due.forEach { review -> androidx.compose.runtime.key(review.id) {
                    val topic = topics[review.topicId]
                    val late = ((Date.now().toLong() - review.dueAt) / 86_400_000L).toInt()
                    Div({ classes("task") }) {
                        Div({ classes("task-mark"); attr("style", "background:var(--amber-soft);color:var(--on-amber-soft)") }) { Text("R${review.stage}") }
                        Div({ classes("task-body") }) {
                            Div({ classes("task-title") }) { Text(topic?.title ?: "Tópico") }
                            Div({ classes("task-meta") }) {
                                Text(subjects[topic?.subjectId]?.name ?: "")
                                if (late > 0) Text(" · atrasada $late ${if (late == 1) "dia" else "dias"}")
                            }
                        }
                        Btn("Ignorar", { Store.update { Actions.ignoreReview(it, review.id) } }, style = "ghost", small = true)
                        Btn("Revisar", { reviewing = review }, style = "tonal", small = true)
                    }
                } }
            }
        }
    }
    if (upcoming.isNotEmpty()) {
        Card {
            CardHead("Próximas")
            Div({ classes("stack", "tight") }) {
                upcoming.forEach { review ->
                    Div({ classes("row", "between") }) {
                        Span { Text(topics[review.topicId]?.title ?: "Tópico") }
                        Span({ classes("muted", "small") }) { Text("R${review.stage} · ${Queries.shortDate(Queries.dateOf(review.dueAt))}") }
                    }
                }
            }
        }
    }
    reviewing?.let { review -> ReviewDialog(review) { reviewing = null } }
}

@Composable
private fun ReviewDialog(review: Review, onClose: () -> Unit) {
    val data = Store.data
    val topic = data.topics.firstOrNull { it.id == review.topicId }
    val startedAt = remember { Date.now().toLong() }
    val summary = data.summaries.firstOrNull { it.topicId == review.topicId && it.kind == "RAPIDO" } ?: data.summaries.firstOrNull { it.topicId == review.topicId }
    Modal(onDismiss = onClose) {
        H2 { Text("Revisar: ${topic?.title ?: "tópico"}") }
        if (summary != null) {
            Div({ attr("style", "max-height:45vh;overflow:auto;border:1px solid var(--line);border-radius:12px;padding:4px 14px") }) { Markdown(summary.markdown) }
        } else {
            P({ classes("muted") }) { Text("Tente lembrar os pontos principais do tópico. Depois diga como foi:") }
        }
        P({ classes("strong") }) { Text("Como foi lembrar?") }
        Div({ classes("grid", "cols-3") }) {
            Btn("Difícil", { Store.update { Actions.completeReview(it, review.id, ReviewDifficulty.DIFICIL, startedAt = startedAt) }; onClose() }, style = "outline")
            Btn("Normal", { Store.update { Actions.completeReview(it, review.id, ReviewDifficulty.NORMAL, startedAt = startedAt) }; onClose() }, style = "tonal")
            Btn("Fácil", { Store.update { Actions.completeReview(it, review.id, ReviewDifficulty.FACIL, startedAt = startedAt) }; onClose() })
        }
        P({ classes("small", "muted") }) { Text("Difícil traz a próxima revisão para amanhã; fácil afasta. É a mesma regra do app.") }
    }
}

// ------------------------------------------------------------------ caderno de erros

@Composable
fun ErrorsScreen() {
    val data = Store.data
    val questions = data.questions.associateBy { it.id }
    val topics = data.topics.associateBy { it.id }
    val pending = Queries.pendingErrors(data).sortedByDescending { it.lastErrorAt }
    val concepts = data.errorConcepts.filter { !it.mastered && it.errorCount > 0 }.sortedByDescending { it.errorCount }.take(8)

    PageHead("Caderno de erros", "As questões que você errou voltam em 3, 10 e 30 dias até você dominar") {
        Btn("Refazer erradas", { Router.go(Route.Quiz("erros-20")) }, icon = "replay", enabled = pending.isNotEmpty())
    }
    Div({ classes("grid", "main-side") }) {
        Card {
            CardHead("Questões (${pending.size})")
            if (pending.isEmpty()) Empty("task_alt", "Caderno limpo", "Nenhuma questão errada esperando revisão.")
            Div({ classes("stack") }) {
                pending.take(60).forEach { entry ->
                    val question = questions[entry.questionId] ?: return@forEach
                    Div({ classes("card", "flat") }) {
                        Div({ classes("row", "between", "wrap") }) {
                            Chip(topics[question.topicId]?.title ?: "Questão")
                            Chip(
                                when (entry.status) { "RECORRENTE" -> "Recorrente"; "REVISANDO" -> "Revisando"; else -> "Nova" },
                                if (entry.status == "RECORRENTE") "red" else "amber",
                            )
                        }
                        P({ attr("style", "margin:10px 0 6px;display:-webkit-box;-webkit-line-clamp:3;-webkit-box-orient:vertical;overflow:hidden") }) { Text(question.statement) }
                        P({ classes("small", "muted") }) {
                            Text("Você marcou ${entry.selectedAnswer ?: "?"} · certa: ${entry.correctAnswer ?: "?"} · errou ${entry.errorCount}×")
                            entry.nextRetryAt?.let { Text(" · volta em ${Queries.shortDate(Queries.dateOf(it))}") }
                        }
                    }
                }
            }
        }
        Card {
            CardHead("Conceitos que mais travam")
            if (concepts.isEmpty()) P({ classes("muted") }) { Text("Quando você errar questões, os conceitos aparecem aqui.") }
            Div({ classes("stack", "tight") }) {
                concepts.forEach { concept ->
                    Div({ classes("row", "between") }) {
                        Span { Text(concept.title) }
                        Chip("${concept.errorCount} erros", if (concept.errorCount >= 3) "red" else "amber")
                    }
                }
            }
        }
    }
}

// ------------------------------------------------------------------ simulados

@Composable
fun SimulationsScreen() {
    val data = Store.data
    val simulations = data.simulations.sortedByDescending { it.createdAt }
    val finished = simulations.filter { it.status == "FINISHED" }
    PageHead("Simulados", "Resultados dos simulados feitos no app")
    if (finished.isNotEmpty()) {
        Div({ classes("grid", "cols-3") }) {
            Card(extra = "flat") { Stat("${finished.size}", "simulados feitos") }
            Card(extra = "flat") { Stat("${finished.mapNotNull { it.scorePercent }.maxOrNull() ?: 0}%", "melhor nota") }
            Card(extra = "flat") { Stat("${finished.mapNotNull { it.scorePercent }.takeIf { it.isNotEmpty() }?.average()?.toInt() ?: 0}%", "média") }
        }
    }
    Card {
        if (simulations.isEmpty()) Empty("timer", "Nenhum simulado ainda", "Gere um simulado no app: ele monta a prova no estilo da sua banca. Os resultados aparecem aqui.")
        Div({ classes("stack") }) {
            simulations.forEach { simulation ->
                Div({ classes("task") }) {
                    Div({ classes("task-mark"); attr("style", "background:var(--primary-soft);color:var(--on-primary-soft)") }) { Icon("timer") }
                    Div({ classes("task-body") }) {
                        Div({ classes("task-title") }) { Text(simulation.title) }
                        Div({ classes("task-meta") }) {
                            Text("${Queries.shortDate(Queries.dateOf(simulation.createdAt))} · ${simulation.questionCount.takeIf { it > 0 } ?: simulation.plannedQuestions} questões · ${simulation.timeLimitMinutes} min")
                        }
                    }
                    when (simulation.status) {
                        "FINISHED" -> Chip("${simulation.scorePercent ?: percent(simulation.correctCount, simulation.questionCount)}%", if ((simulation.scorePercent ?: 0) >= 70) "green" else "amber")
                        "IN_PROGRESS" -> Chip("Em andamento no app", "amber")
                        "GENERATING", "PARTIAL" -> Chip("Sendo gerado", "primary")
                        "FAILED" -> Chip("Falhou", "red")
                        else -> Chip("Pronto no app")
                    }
                }
            }
        }
    }
}
