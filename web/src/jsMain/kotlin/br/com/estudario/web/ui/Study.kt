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
import br.com.estudario.web.data.Snapshot
import br.com.estudario.web.data.Store
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H2
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text
import kotlin.js.Date

// As revisões espaçadas ficam em Reviews.kt.

// ------------------------------------------------------------------ caderno de erros

/**
 * Os erros que o caderno mostra: do edital escolhido e com a questão ainda no banco. O aviso do Início
 * conta estes; antes contava todos (outros editais, questões já apagadas) e o caderno abria "limpo".
 */
fun notebookErrors(data: Snapshot): List<br.com.estudario.web.data.ErrorEntry> {
    val questions = data.questions.associateBy { it.id }
    val scope = CompetitionFilter.topicIds(data)
    return Queries.pendingErrors(data).filter { questions[it.questionId]?.topicId in scope }
}

@Composable
fun ErrorsScreen() {
    val data = Store.data
    val questions = data.questions.associateBy { it.id }
    val topics = data.topics.associateBy { it.id }
    val pending = notebookErrors(data).sortedByDescending { it.lastErrorAt }
    val concepts = data.errorConcepts.filter { !it.mastered && it.errorCount > 0 }.sortedByDescending { it.errorCount }.take(8)

    PageHead("Caderno de erros", "As questões que você errou voltam em 3, 10 e 30 dias até você dominar") {
        Btn("Refazer erradas", { Router.go(Route.Quiz("erros-20")) }, icon = "replay", enabled = pending.isNotEmpty())
    }
    CompetitionTabs()
    if (pending.isEmpty()) Card { Empty("task_alt", "Caderno limpo", "Nenhuma questão errada esperando revisão.") }
    else ByTopic(pending.take(120).filter { questions[it.questionId] != null }.groupBy { questions[it.questionId]!!.topicId }) { entry ->
        val question = questions[entry.questionId]!!
        Div({ classes("saved-item") }) {
            Div({ classes("grow"); attr("style", "min-width:0") }) {
                P({ classes("clamp-3") }) { Text(splitStatement(question.statement).let { it.command ?: it.base }.take(300)) }
                P({ classes("small", "muted"); attr("style", "margin-top:4px") }) {
                    Text("Você marcou ${entry.selectedAnswer ?: "?"} · certa: ${entry.correctAnswer ?: "?"} · errou ${entry.errorCount}×")
                    entry.nextRetryAt?.let { Text(" · volta em ${Queries.shortDate(Queries.dateOf(it))}") }
                }
            }
            Chip(when (entry.status) { "RECORRENTE" -> "Recorrente"; "REVISANDO" -> "Revisando"; else -> "Nova" }, if (entry.status == "RECORRENTE") "red" else "amber")
        }
    }
    if (concepts.isNotEmpty()) Card {
        CardHead("Conceitos que mais travam")
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

