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
import org.jetbrains.compose.web.dom.B
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.H2
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H3
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text

/**
 * Modo foco solto, no visual do estúdio: o tempo sobe (nada de contagem regressiva), como no app.
 * Um anel grande enche até a meta (os minutos da tarefa ou o aviso escolhido). Parado, a pessoa
 * escolhe uma tarefa do plano de hoje ou uma sessão livre; o cronômetro segue pelo site (FocusBar).
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
    val active = TopicFocus.active
    val goal = if (active) TopicFocus.plannedMinutes else task?.minutes ?: if (alertOn) alertMinutes else null
    val pct = if (!active || goal == null || goal <= 0) 0 else ((TopicFocus.elapsedMs / 600.0) / goal).toInt().coerceIn(0, 100)
    val todayMinutes = Queries.dailyActivity(data).firstOrNull { it.date == Queries.todayDate() }?.minutes ?: 0

    Div({ classes("gx-panel", "fc") }) {
        Div({ classes("fc-top") }) {
            Div({ classes("gx-top-id") }) {
                Span({ classes("gx-kicker") }) { Text("MODO FOCO") }
                H2 { Text(if (active) (if (TopicFocus.running) "Focado. Bom trabalho!" else "Pausado") else "Escolha e comece") }
                P({ classes("gx-topic") }) { Text(if (active && TopicFocus.title.isNotBlank()) TopicFocus.title else "O tempo sobe e segue com você pelo site. Hoje: ${Queries.minutesLabel(todayMinutes)} de estudo.") }
            }
            Button({ classes("gx-ghost", "small"); attr("type", "button"); onClick { Router.go(Route.FocusHistory) } }) { Icon("history"); Span { Text("Histórico") } }
        }
        // O relógio: anel que enche até a meta, com o tempo grande no meio.
        Div({ classes("fc-dial-wrap") }) {
            Div({ classes(*listOfNotNull("fc-dial", if (TopicFocus.running) "on" else null, if (TopicFocus.timeUp) "up" else null).toTypedArray()); attr("style", "--p:$pct") }) {
                Div({ classes("fc-dial-in") }) {
                    B({ classes("fc-clock") }) { Text(when { !active -> "00:00"; TopicFocus.clockHidden -> "••:••"; else -> TopicFocus.clock() }) }
                    Span { Text(when { !active -> goal?.let { "meta de ${Queries.minutesLabel(it)}" } ?: "sem meta"; goal != null -> "$pct% da meta de ${Queries.minutesLabel(goal)}"; else -> if (TopicFocus.running) "focado" else "pausado" }) }
                }
            }
        }
        if (active) {
            Div({ classes("fc-actions") }) {
                if (TopicFocus.running) GxLaunch("Pausar", icon = "timer") { TopicFocus.pause() }
                else GxLaunch("Continuar", icon = "play_circle") { TopicFocus.resume() }
                Button({ classes("gx-ghost"); attr("type", "button"); onClick { TopicFocus.stop() } }) { Text("Encerrar e salvar") }
                TopicFocus.topicId?.let { id -> Button({ classes("gx-ghost"); attr("type", "button"); onClick { Router.go(Route.Topic(id)) } }) { Text("Abrir o tópico") } }
                Button({ classes("gx-ghost"); attr("type", "button"); onClick { TopicFocus.setClockHidden(!TopicFocus.clockHidden) } }) { Text(if (TopicFocus.clockHidden) "Mostrar o tempo" else "Esconder o tempo") }
            }
            if (TopicFocus.elapsedMs < 60_000) P({ classes("gx-note", "fc-center") }) { Text("Sessões com menos de 1 minuto não entram no histórico.") }
        } else Div({ classes("gx-controls", "solo") }) {
            GxSection("menu_book", "Estudar o quê?", if (pending.isEmpty()) "Sem tarefas no plano hoje: uma sessão livre conta nos minutos e na sequência." else "Uma tarefa do plano de hoje ou uma sessão livre", "#A78BFF") {
                GxTiles(
                    listOf(GxOption<String?>(null, "Sessão livre", "Conta nos minutos do dia e na sequência", "coffee", "#22D3EE")) +
                        pending.map { item -> GxOption<String?>(item.id, item.topicName ?: item.subjectName, "${Queries.taskTypeLabel(item.type)} · ${Queries.minutesLabel(item.minutes)} · ${item.subjectName}", br.com.estudario.domain.catalog.SubjectArt.iconFor(item.subjectName), subjectColor(item.subjectName)) },
                    { it == taskId }, cols = 2, multi = false,
                ) { taskId = it }
            }
            if (task == null) GxSection("notifications", "Aviso de tempo", "O aviso não encerra nada: o tempo continua subindo até você parar.", "#FFC94D") {
                GxSwitchRow("Avisar quando der o tempo", if (alertOn) "Eu aviso depois de ${Queries.minutesLabel(alertMinutes)}" else "Sem aviso: foco livre", alertOn) { alertOn = it }
                if (alertOn) GxCounter(alertMinutes, { alertMinutes = it; Prefs.focusMinutes = it }, min = 5, max = 180, step = 5, unit = "minutos", chips = listOf(25, 50, 90), label = "Avisar depois de")
            }
            Div({ classes("gx-go", "row") }) {
                Div({ classes("gx-go-sum") }) {
                    Span({ classes("gx-kicker") }) { Text("SEU FOCO") }
                    B { Text(task?.let { "${it.topicName ?: it.subjectName} · ${Queries.minutesLabel(it.minutes)}" } ?: if (alertOn) "Sessão livre · aviso em ${Queries.minutesLabel(alertMinutes)}" else "Sessão livre") }
                }
                GxLaunch("Começar o foco", icon = "timer") {
                    TopicFocus.start(task?.topicId, task?.topicName ?: task?.subjectName ?: "Sessão de foco", taskId = task?.id, plannedMinutes = goal, subjectId = task?.subjectId)
                    task?.topicId?.let { Router.go(Route.Topic(it)) }
                }
            }
        }
    }
}
