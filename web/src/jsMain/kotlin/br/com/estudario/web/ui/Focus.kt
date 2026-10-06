package br.com.estudario.web.ui

import org.jetbrains.compose.web.svg.Defs
import org.jetbrains.compose.web.svg.LinearGradient
import org.jetbrains.compose.web.svg.Stop
import org.jetbrains.compose.web.svg.Svg
import org.jetbrains.compose.web.svg.Circle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import br.com.estudario.web.data.Actions
import br.com.estudario.web.data.Keys
import br.com.estudario.web.data.Prefs
import br.com.estudario.web.data.Queries
import br.com.estudario.web.data.Store
import br.com.estudario.web.data.jsonOf
import kotlinx.browser.document
import kotlinx.browser.window
import org.jetbrains.compose.web.dom.B
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H3
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text
import kotlin.js.Date

/**
 * Modo foco: cronômetro de estudo como no app. A sessão concluída entra no histórico (focusSessions),
 * conta nos minutos do dia e na sequência; ligada a uma tarefa do plano, abre a conclusão dela.
 */
@Composable
fun FocusScreen() {
    val data = Store.data
    val plan = Queries.activePlan(data)
    val pending = plan?.let { Queries.tasksOn(data, it.id, Queries.todayEpoch()) }.orEmpty().filter { it.status == "PLANEJADA" || it.status == "EM_ANDAMENTO" }
    var minutes by remember { mutableStateOf(Prefs.focusMinutes) }
    var taskId by remember { mutableStateOf(pending.firstOrNull()?.id) }
    var startedAt by remember { mutableStateOf<Long?>(null) }
    var pausedTotal by remember { mutableStateOf(0L) }
    var pausedAt by remember { mutableStateOf<Long?>(null) }
    var now by remember { mutableStateOf(Date.now().toLong()) }
    var finishedTask by remember { mutableStateOf<br.com.estudario.web.data.PlanTask?>(null) }

    DisposableEffect(startedAt) {
        val handle = if (startedAt != null) window.setInterval({ now = Date.now().toLong() }, 500) else null
        onDispose { handle?.let { window.clearInterval(it) } }
    }
    // Aviso ao fechar a aba com a sessão rodando.
    DisposableEffect(startedAt) {
        val listener: (org.w3c.dom.events.Event) -> Unit = { event -> if (startedAt != null) { event.preventDefault(); event.asDynamic().returnValue = "" } }
        window.addEventListener("beforeunload", listener)
        onDispose { window.removeEventListener("beforeunload", listener) }
    }

    val totalMillis = minutes * 60_000L
    val elapsed = startedAt?.let { ((pausedAt ?: now) - it - pausedTotal).coerceAtLeast(0) } ?: 0L
    val remaining = (totalMillis - elapsed).coerceAtLeast(0)
    val running = startedAt != null && pausedAt == null
    val task = pending.firstOrNull { it.id == taskId }

    // Título da aba com o tempo, para ver de outra janela.
    DisposableEffect(remaining, running) {
        document.title = if (startedAt != null) "${clock(remaining)} · Foco · Estudário" else "Estudário"
        onDispose { document.title = "Estudário" }
    }

    fun finish() {
        val start = startedAt ?: return
        val end = Date.now().toLong()
        val seconds = (elapsed / 1000).coerceAtLeast(0)
        if (seconds >= 60) {
            val topic = task?.topicId
            Store.update {
                it.append(
                    Keys.FOCUS_SESSIONS,
                    jsonOf(
                        "id" to Actions.newSessionId(), "title" to (task?.topicName ?: task?.subjectName ?: "Sessão de foco"), "startedAt" to start, "completedAt" to end,
                        "durationSeconds" to seconds, "subjectIds" to (task?.subjectId?.toString() ?: ""), "origin" to if (task != null) "PLANO" else "LIVRE",
                        "topicId" to topic, "taskId" to task?.id,
                    ),
                )
            }
            Toast.show("Sessão salva: ${Queries.minutesLabel((seconds / 60).toInt())} de foco")
            if (task != null) finishedTask = task.copy(minutes = task.minutes)
        }
        startedAt = null; pausedAt = null; pausedTotal = 0
    }
    if (remaining == 0L && startedAt != null && running) finish()

    PageHead("Modo foco", "Um bloco de estudo sem distração. Deixe esta aba aberta.")
    Card(extra = "pad-lg") {
        Div({ classes("focus-wrap") }) {
            Div({ classes("timer") }) {
                val radius = 140.0
                val circumference = 2 * kotlin.math.PI * radius
                Svg(viewBox = "0 0 320 320") {
                    Defs {
                        LinearGradient(id = "focusGrad", attrs = { attr("x1", "0"); attr("y1", "0"); attr("x2", "1"); attr("y2", "1") }) {
                            Stop({ attr("offset", "0%"); attr("stop-color", "#4F46E5") })
                            Stop({ attr("offset", "100%"); attr("stop-color", "#8C7FFF") })
                        }
                    }
                    Circle(160, 160, radius, { classes("track"); attr("fill", "none"); attr("stroke-width", "16") })
                    Circle(160, 160, radius, {
                        classes("value"); attr("fill", "none"); attr("stroke-width", "16"); attr("stroke-linecap", "round")
                        attr("stroke-dasharray", "$circumference"); attr("stroke-dashoffset", "${circumference * (elapsed.toDouble() / totalMillis).coerceIn(0.0, 1.0)}")
                    })
                }
                Div({ classes("center") }) {
                    Span({ classes("clock") }) { Text(clock(if (startedAt == null) totalMillis else remaining)) }
                    Span({ classes("small", "muted", "strong") }) { Text(when { startedAt == null -> "pronto para começar"; running -> "focado"; else -> "pausado" }) }
                }
            }
            if (startedAt == null) {
                Div({ classes("row", "wrap"); attr("style", "justify-content:center") }) {
                    Span({ classes("strong") }) { Text("Duração") }
                    Stepper(minutes, { minutes = it; Prefs.focusMinutes = it }, step = 5, min = 5, max = 180, format = Queries::minutesLabel, label = "Duração do foco")
                }
                Btn("Começar", { startedAt = Date.now().toLong(); now = Date.now().toLong() }, style = "primary", icon = "play_arrow", size = "lg")
            } else {
                Div({ classes("row", "wrap"); attr("style", "justify-content:center") }) {
                    if (running) Btn("Pausar", { pausedAt = Date.now().toLong() }, style = "tonal", icon = "pause")
                    else Btn("Continuar", { pausedTotal += Date.now().toLong() - (pausedAt ?: Date.now().toLong()); pausedAt = null }, style = "primary", icon = "play_arrow")
                    Btn("Encerrar e salvar", { finish() }, style = "outline", icon = "stop")
                }
                if (elapsed < 60_000) P({ classes("xs", "faint") }) { Text("Sessões com menos de 1 minuto não entram no histórico.") }
            }
        }
    }
    if (startedAt == null) Card {
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
                            P({ classes("small", "muted") }) { Text("${Queries.taskTypeLabel(item.type)} · ${item.subjectName} · ${Queries.minutesLabel(item.minutes)}") }
                        }
                    }
                }
            }
        }
    }
    val history = data.focusSessions.sortedByDescending { it.completedAt }.take(6)
    if (history.isNotEmpty() && startedAt == null) Card {
        CardHead("Últimas sessões")
        Div({ classes("stack", "tight") }) {
            history.forEach { session ->
                Div({ classes("row", "between") }) {
                    Span({ classes("small", "clamp-2") }) { Text(session.title.ifBlank { "Sessão de foco" }) }
                    B({ classes("small", "nowrap") }) { Text("${Queries.minutesLabel((session.durationSeconds / 60).toInt())} · ${Queries.shortDate(Queries.dateOf(session.completedAt))}") }
                }
            }
        }
    }
    finishedTask?.let { done -> CompleteTaskDialog(done) { finishedTask = null } }
}

private fun clock(millis: Long): String {
    val total = (millis + 999) / 1000
    val h = total / 3600
    val m = (total % 3600) / 60
    val s = total % 60
    fun two(v: Long) = v.toString().padStart(2, '0')
    return if (h > 0) "$h:${two(m)}:${two(s)}" else "${two(m)}:${two(s)}"
}

