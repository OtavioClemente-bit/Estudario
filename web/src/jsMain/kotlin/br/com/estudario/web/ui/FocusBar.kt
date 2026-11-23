package br.com.estudario.web.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import br.com.estudario.web.Route
import br.com.estudario.web.Router
import org.jetbrains.compose.web.dom.B
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H2
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text


/**
 * O cronômetro do foco no topo do site (como a faixa do app): o tempo subindo, o nome do que está
 * sendo estudado, pausar e encerrar. Dá para esconder; aí sobra só o ícone com o tempo.
 */
@Composable
fun FocusBar() {
    if (!TopicFocus.active) return
    val hidden = TopicFocus.clockHidden
    fun setHidden(value: Boolean) = TopicFocus.setClockHidden(value)

    if (hidden) {
        // Escondido: sem contagem na tela (o tempo distrai); só o selo de que o foco segue ativo.
        Button({
            classes(*listOfNotNull("focus-mini", if (!TopicFocus.running) "paused" else null).toTypedArray())
            attr("title", "Mostrar o cronômetro"); attr("aria-label", "Foco ativo. Mostrar o cronômetro")
            onClick { setHidden(false) }
        }) {
            Span({ classes("focus-dot") })
            Text(if (TopicFocus.running) "Em foco" else "Pausado")
        }
    } else {
        Div({ classes(*listOfNotNull("focus-bar", if (!TopicFocus.running) "paused" else null).toTypedArray()) }) {
            // O Folha estuda junto: escrevendo concentrado; com o foco pausado, cochila.
            Span({ classes("focus-folha") }) { Folha(if (TopicFocus.running) "focus" else "sleepy", 30, live = true) }
            Span({ classes("focus-clock") }) { Text(TopicFocus.clock()) }
            Span({ classes("focus-title", "clamp-2") }) {
                Text(TopicFocus.title.ifBlank { "Sessão de foco" })
                TopicFocus.plannedMinutes?.let { Span({ classes("xs", "muted") }) { Text(" · meta ${br.com.estudario.web.data.Queries.minutesLabel(it)}") } }
            }
            if (TopicFocus.running) IconButton("pause", "Pausar") { TopicFocus.pause() }
            else IconButton("play_arrow", "Continuar") { TopicFocus.resume() }
            IconButton("stop", "Encerrar") { FocusModals.confirmStop = true }
            IconButton("visibility_off", "Esconder") { setHidden(true) }
        }
    }

}

/**
 * As janelas do foco ficam na raiz da página, não dentro da barra do topo: ali o fundo desfocado da
 * barra prendia a janela no alto e ela aparecia cortada, grudada no navegador.
 */
object FocusModals {
    var confirmStop by androidx.compose.runtime.mutableStateOf(false)
}

@Composable
fun FocusModalsHost() {
    androidx.compose.runtime.DisposableEffect(Unit) {
        val listener: (org.w3c.dom.events.Event) -> Unit = { TopicFocus.activity() }
        val events = listOf("mousemove", "keydown", "pointerdown", "scroll", "touchstart", "wheel")
        events.forEach { kotlinx.browser.window.addEventListener(it, listener, js("({ passive: true })")) }
        onDispose { events.forEach { kotlinx.browser.window.removeEventListener(it, listener) } }
    }
    TopicFocus.idleGap?.let { gap ->
        if (!TopicFocus.active) TopicFocus.idleGap = null
        else Modal(onDismiss = { TopicFocus.idleGap = null }) {
            FolhaSays("thinking", 72) { H2 { Text("Você estava estudando?") } }
            val minutes = (gap / 60_000L).toInt()
            P({ classes("muted") }) { Text("O foco ficou ligado e a página ficou parada por ${br.com.estudario.web.data.Queries.minutesLabel(minutes)}. Se você estava lendo um livro ou fazendo exercício no papel, pode contar tudo.") }
            Div({ classes("stack", "tight") }) {
                Btn("Sim, contar esse tempo", { TopicFocus.idleGap = null }, style = "primary", icon = "check", block = true)
                Btn("Não, descontar ${br.com.estudario.web.data.Queries.minutesLabel(minutes)}", { TopicFocus.discountIdle(); Toast.show("Tempo parado descontado do foco") }, style = "outline", icon = "remove", block = true)
            }
        }
    }
    // Cronômetro esquecido (a mesma regra do app): esta pergunta vem antes das outras janelas do foco.
    if (TopicFocus.needsCheck) { StillStudyingModal(); return }
    if (FocusModals.confirmStop && TopicFocus.active) StopFocusModal(onDismiss = { FocusModals.confirmStop = false })
    if (TopicFocus.timeUp) TimeUpModal()
}

/** "Você ainda está estudando?": sim segue contando; já parei pergunta quanto tempo e salva só isso. */
@Composable
private fun StillStudyingModal() {
    val label = br.com.estudario.web.data.Queries::minutesLabel
    val elapsed = TopicFocus.elapsedMinutes.coerceAtLeast(1)
    val planned = TopicFocus.plannedMinutes
    var asking by remember(TopicFocus.sessionId) { mutableStateOf(false) }
    var minutes by remember(TopicFocus.sessionId) { mutableStateOf(br.com.estudario.domain.FocusRules.suggestedMinutes(planned).coerceAtMost(elapsed)) }
    Modal(onDismiss = {}) {
        if (!asking) {
            FolhaSays("thinking", 72) { H2 { Text("Você ainda está estudando?") } }
            P({ classes("muted") }) { Text("O cronômetro do foco está ligado há ${label(elapsed)}. Se você já tinha parado, a gente salva só o tempo que você estudou de verdade.") }
            Div({ classes("stack", "tight") }) {
                Btn("Sim, continuar", { TopicFocus.confirmStillStudying() }, style = "primary", icon = "check", block = true)
                Btn("Já parei", { asking = true }, style = "outline", icon = "stop", block = true)
            }
        } else {
            H2 { Text("Quanto tempo você estudou?") }
            P({ classes("muted") }) { Text("Ajuste o tempo e o foco é salvo com ele.") }
            Div({ classes("row"); style { property("justify-content", "center"); property("align-items", "center"); property("gap", "12px") } }) {
                Btn("−5", { minutes = (minutes - 5).coerceAtLeast(1) }, style = "outline")
                B { Text(label(minutes)) }
                Btn("+5", { minutes = (minutes + 5).coerceAtMost(elapsed) }, style = "outline")
            }
            Div({ classes("stack", "tight") }) {
                if (planned != null && planned in 1..elapsed && planned != minutes) Btn("Usar a meta (${label(planned)})", { minutes = planned }, style = "ghost", block = true)
                if (minutes != elapsed) Btn("Estudei o tempo todo (${label(elapsed)})", { minutes = elapsed }, style = "ghost", block = true)
                Btn("Salvar ${label(minutes)}", { TopicFocus.stop(studiedMinutes = minutes) }, style = "primary", icon = "check", block = true)
                Btn("Voltar", { asking = false }, style = "ghost", block = true)
            }
        }
    }
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
            if (TopicFocus.canComplete && topicId != null && !studied) Btn("Encerrar e concluir o tópico", { onDismiss(); TopicFocus.stopAndComplete() }, style = "primary", icon = "task_alt", block = true)
            else if (TopicFocus.canComplete && TopicFocus.taskId != null) Btn("Encerrar e concluir a tarefa", { onDismiss(); TopicFocus.stopAndComplete() }, style = "primary", icon = "task_alt", block = true)
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
            if (TopicFocus.canComplete) Btn(if (topicId != null) "Encerrar e concluir o tópico" else "Encerrar e concluir a tarefa", { TopicFocus.timeUpSeen = true; TopicFocus.stopAndComplete(); if (topicId != null) Router.go(Route.Topic(topicId)) }, style = "primary", icon = "task_alt", block = true)
            Btn("Ok, vou seguir", { TopicFocus.timeUpSeen = true }, style = if (topicId != null) "outline" else "primary", block = true)
            Btn("Só encerrar", { TopicFocus.timeUpSeen = true; TopicFocus.stop() }, style = "ghost", block = true)
        }
    }
}
