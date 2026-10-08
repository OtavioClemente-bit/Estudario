package br.com.estudario.web.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import br.com.estudario.web.Route
import br.com.estudario.web.Router
import kotlinx.browser.localStorage
import org.jetbrains.compose.web.dom.B
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H2
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text

private const val HIDDEN_KEY = "estudario.focusbar.hidden"

/**
 * O cronômetro do foco no topo do site (como a faixa do app): o tempo subindo, o nome do que está
 * sendo estudado, pausar e encerrar. Dá para esconder; aí sobra só o ícone com o tempo.
 */
@Composable
fun FocusBar() {
    if (!TopicFocus.active) return
    var hidden by remember { mutableStateOf(localStorage.getItem(HIDDEN_KEY) == "1") }
    fun setHidden(value: Boolean) { hidden = value; localStorage.setItem(HIDDEN_KEY, if (value) "1" else "0") }
    var confirmStop by remember { mutableStateOf(false) }

    if (hidden) {
        Button({ classes("focus-mini"); attr("title", "Mostrar o foco"); onClick { setHidden(false) } }) { Icon("timer", filled = true); Text(TopicFocus.clock()) }
    } else {
        Div({ classes(*listOfNotNull("focus-bar", if (!TopicFocus.running) "paused" else null).toTypedArray()) }) {
            Icon("timer", filled = true)
            Span({ classes("focus-clock") }) { Text(TopicFocus.clock()) }
            Span({ classes("focus-title", "clamp-2") }) {
                Text(TopicFocus.title.ifBlank { "Sessão de foco" })
                TopicFocus.plannedMinutes?.let { Span({ classes("xs", "muted") }) { Text(" · meta ${br.com.estudario.web.data.Queries.minutesLabel(it)}") } }
            }
            if (TopicFocus.running) IconButton("pause", "Pausar") { TopicFocus.pause() }
            else IconButton("play_arrow", "Continuar") { TopicFocus.resume() }
            IconButton("stop", "Encerrar") { confirmStop = true }
            IconButton("visibility_off", "Esconder") { setHidden(true) }
        }
    }

    if (confirmStop) StopFocusModal(onDismiss = { confirmStop = false })
    if (TopicFocus.timeUp) TimeUpModal()
}

/** Encerrar: só salvar o tempo, ou salvar e concluir o tópico (e a tarefa do plano). */
@Composable
private fun StopFocusModal(onDismiss: () -> Unit) {
    val topicId = TopicFocus.topicId
    val studied = topicId?.let { id -> br.com.estudario.web.data.Store.data.topics.firstOrNull { it.id == id } }?.let(br.com.estudario.web.data.Queries::isStudied) == true
    Modal(onDismiss = onDismiss) {
        H2 { Text("Encerrar o foco?") }
        P({ classes("muted") }) { Text("Você estudou ${TopicFocus.clock()}" + (TopicFocus.title.takeIf { it.isNotBlank() }?.let { " em $it" } ?: "") + ". O tempo vai para o seu histórico.") }
        Div({ classes("stack", "tight") }) {
            if (topicId != null && !studied) Btn("Encerrar e concluir o tópico", { onDismiss(); TopicFocus.stopAndComplete() }, style = "primary", icon = "task_alt", block = true)
            else if (TopicFocus.taskId != null) Btn("Encerrar e concluir a tarefa", { onDismiss(); TopicFocus.stopAndComplete() }, style = "primary", icon = "task_alt", block = true)
            Btn("Só encerrar", { onDismiss(); TopicFocus.stop() }, style = if (topicId != null && !studied) "outline" else "primary", icon = "stop", block = true)
            Btn("Continuar estudando", onDismiss, style = "ghost", block = true)
        }
    }
}

/** Deu o tempo planejado: a sessão continua contando; a pessoa escolhe. */
@Composable
private fun TimeUpModal() {
    val planned = TopicFocus.plannedMinutes ?: return
    val topicId = TopicFocus.topicId
    Modal(onDismiss = { TopicFocus.timeUpSeen = true }) {
        FolhaSays("happy", 80) { H2 { Text("Deu o tempo planejado!") } }
        P({ classes("muted") }) {
            Text("Você já estudou os ${br.com.estudario.web.data.Queries.minutesLabel(planned)}" + (TopicFocus.title.takeIf { it.isNotBlank() }?.let { " de $it" } ?: "") + ". Pode seguir, se estiver rendendo, ou encerrar e registrar.")
        }
        Div({ classes("stack", "tight") }) {
            if (topicId != null || TopicFocus.taskId != null) Btn(if (topicId != null) "Encerrar e concluir o tópico" else "Encerrar e concluir a tarefa", { TopicFocus.timeUpSeen = true; TopicFocus.stopAndComplete(); if (topicId != null) Router.go(Route.Topic(topicId)) }, style = "primary", icon = "task_alt", block = true)
            Btn("Ok, vou seguir", { TopicFocus.timeUpSeen = true }, style = if (topicId != null) "outline" else "primary", block = true)
            Btn("Só encerrar", { TopicFocus.timeUpSeen = true; TopicFocus.stop() }, style = "ghost", block = true)
        }
    }
}
