package br.com.estudario.web.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import br.com.estudario.web.Route
import br.com.estudario.web.Router
import br.com.estudario.web.data.Prefs
import br.com.estudario.web.data.Queries
import br.com.estudario.web.data.Store
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H3
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text

/**
 * Modo foco solto: o tempo sobe (nada de contagem regressiva), como no app. A pessoa escolhe uma
 * tarefa do plano de hoje (a meta vem dos minutos da tarefa) ou uma sessão livre com um aviso
 * opcional. O cronômetro mora no topo do site (FocusBar) e segue por todas as telas.
 */
@Composable
fun FocusScreen() {
    val data = Store.data
    val plan = Queries.activePlan(data)
    val pending = plan?.let { Queries.tasksOn(data, it.id, Queries.todayEpoch()) }.orEmpty().filter { it.status == "PLANEJADA" || it.status == "EM_ANDAMENTO" }
    var taskId by remember { mutableStateOf(pending.firstOrNull()?.id) }
    var alertMinutes by remember { mutableStateOf(Prefs.focusMinutes) }
    var alertOn by remember { mutableStateOf(true) }
    val task = pending.firstOrNull { it.id == taskId }

    PageHead("Modo foco", "Um bloco de estudo sem distração. O tempo conta aqui em cima e segue com você pelo site.")
    Card(extra = "pad-lg") {
        Div({ classes("focus-wrap") }) {
            Div({ classes("timer", "count-up") }) {
                Div({ classes("center") }) {
                    Span({ classes("clock") }) { Text(if (TopicFocus.active) TopicFocus.clock() else "00:00") }
                    Span({ classes("small", "muted", "strong") }) {
                        Text(when { !TopicFocus.active -> "pronto para começar"; TopicFocus.running -> "focado"; else -> "pausado" })
                    }
                    if (TopicFocus.active && TopicFocus.title.isNotBlank()) Span({ classes("xs", "muted", "clamp-2") }) { Text(TopicFocus.title) }
                }
            }
            if (!TopicFocus.active) {
                Div({ classes("row", "wrap"); attr("style", "justify-content:center;gap:12px") }) {
                    Switch(alertOn, "Avisar quando der o tempo") { alertOn = it }
                    Span({ classes("strong") }) { Text("Avisar depois de") }
                    Stepper(alertMinutes, { alertMinutes = it; Prefs.focusMinutes = it }, step = 5, min = 5, max = 180, format = Queries::minutesLabel, label = "Avisar depois de")
                }
                P({ classes("xs", "muted"); attr("style", "text-align:center") }) { Text("O aviso não encerra nada: o tempo continua subindo até você parar.") }
                Btn("Começar", {
                    val planned = if (task != null) task.minutes else if (alertOn) alertMinutes else null
                    TopicFocus.start(task?.topicId, task?.topicName ?: task?.subjectName ?: "Sessão de foco", taskId = task?.id, plannedMinutes = planned, subjectId = task?.subjectId)
                    task?.topicId?.let { Router.go(Route.Topic(it)) }
                }, style = "primary", icon = "play_arrow", size = "lg")
            } else {
                Div({ classes("row", "wrap"); attr("style", "justify-content:center") }) {
                    if (TopicFocus.running) Btn("Pausar", { TopicFocus.pause() }, style = "tonal", icon = "pause")
                    else Btn("Continuar", { TopicFocus.resume() }, style = "primary", icon = "play_arrow")
                    Btn("Encerrar e salvar", { TopicFocus.stop() }, style = "outline", icon = "stop")
                    TopicFocus.topicId?.let { id -> Btn("Abrir o tópico", { Router.go(Route.Topic(id)) }, style = "ghost", icon = "menu_book") }
                }
                if (TopicFocus.elapsedMs < 60_000) P({ classes("xs", "faint") }) { Text("Sessões com menos de 1 minuto não entram no histórico.") }
            }
        }
    }
    if (!TopicFocus.active) Card {
        CardHead("Estudar o quê?")
        Div({ classes("stack", "tight") }) {
            Button({ classes(*listOfNotNull("option", if (taskId == null) "chosen" else null).toTypedArray()); onClick { taskId = null } }) {
                Span({ classes("key") }) { Icon("self_improvement") }
                Div { H3 { Text("Sessão livre") }; P({ classes("small", "muted") }) { Text("Conta nos minutos do dia e na sequência.") } }
            }
            pending.forEach { item ->
                androidx.compose.runtime.key(item.id) {
                    Button({ classes(*listOfNotNull("option", if (taskId == item.id) "chosen" else null).toTypedArray()); onClick { taskId = item.id } }) {
                        Span({ classes("key") }) { Icon(taskIcon(item.type)) }
                        Div {
                            H3 { Text(item.topicName ?: item.subjectName) }
                            P({ classes("small", "muted") }) { Text("${Queries.taskTypeLabel(item.type)} · ${item.subjectName} · meta de ${Queries.minutesLabel(item.minutes)}") }
                        }
                    }
                }
            }
        }
    }
    val history = data.focusSessions.sortedByDescending { it.completedAt }.take(6)
    if (history.isNotEmpty() && !TopicFocus.active) Card {
        CardHead("Últimas sessões") { Btn("Ver tudo", { Router.go(Route.FocusHistory) }, style = "ghost", small = true, icon = "history") }
        Div({ classes("stack", "tight") }) {
            history.forEach { session ->
                Div({ classes("row", "between") }) {
                    Span({ classes("small", "clamp-2") }) { Text(session.title.ifBlank { "Sessão de foco" }) }
                    Span({ classes("small", "muted", "nowrap") }) { Text("${Queries.shortDate(Queries.dateOf(session.completedAt))} · ${Queries.minutesLabel((session.durationSeconds / 60).toInt())}") }
                }
            }
        }
    }
}
