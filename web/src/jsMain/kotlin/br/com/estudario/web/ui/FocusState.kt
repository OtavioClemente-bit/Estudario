package br.com.estudario.web.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import br.com.estudario.web.data.Actions
import br.com.estudario.web.data.Keys
import br.com.estudario.web.data.Store
import br.com.estudario.web.data.jsonOf
import kotlinx.browser.document
import kotlinx.browser.window
import kotlin.js.Date

/**
 * O modo foco do site, um só para o site inteiro (como o cronômetro no topo do app): o tempo
 * cresce, nunca regride. Pode nascer de um tópico, de uma tarefa do plano ou solto. Se houver
 * tempo planejado, ao alcançá-lo avisa uma vez, e a sessão continua contando.
 */
object TopicFocus {
    var topicId by mutableStateOf<Long?>(null)
    var taskId by mutableStateOf<String?>(null)
    var subjectId by mutableStateOf<Long?>(null)
    var title by mutableStateOf("")
    var startedAt by mutableStateOf(0L)
    var pausedAt by mutableStateOf<Long?>(null)
    var pausedTotal by mutableStateOf(0L)
    /** Minutos previstos (tarefa do plano ou aviso escolhido); null = sem aviso. */
    var plannedMinutes by mutableStateOf<Int?>(null)
    var now by mutableStateOf(0L)
    var timeUpSeen by mutableStateOf(false)
    private var ticker: Int? = null

    val active: Boolean get() = startedAt > 0L
    val running: Boolean get() = active && pausedAt == null
    val elapsedMs: Long get() = if (!active) 0L else ((pausedAt ?: now) - startedAt - pausedTotal).coerceAtLeast(0L)
    val elapsedMinutes: Int get() = (elapsedMs / 60_000L).toInt()
    val timeUp: Boolean get() = active && !timeUpSeen && plannedMinutes?.let { elapsedMs >= it * 60_000L } == true

    fun start(id: Long?, name: String, taskId: String? = null, plannedMinutes: Int? = null, subjectId: Long? = null) {
        if (active) stop()
        topicId = id
        this.taskId = taskId
        this.subjectId = subjectId ?: id?.let { tid -> Store.data.topics.firstOrNull { it.id == tid }?.subjectId }
        title = name
        this.plannedMinutes = plannedMinutes
        startedAt = Date.now().toLong()
        now = startedAt
        pausedAt = null
        pausedTotal = 0L
        timeUpSeen = false
        ticker?.let { window.clearInterval(it) }
        ticker = window.setInterval({ now = Date.now().toLong(); document.title = "${clock()} · Foco · Estudário" }, 1000)
    }

    /** Compatível com a chamada antiga do tópico. */
    fun start(id: Long, name: String) = start(id, name, null, null, null)

    fun pause() { if (running) pausedAt = Date.now().toLong() }
    fun resume() { pausedAt?.let { pausedTotal += Date.now().toLong() - it; pausedAt = null } }

    /** Encerra e salva no histórico (menos de 1 minuto não conta). Devolve os minutos. */
    fun stop(): Int {
        if (!active) return 0
        val end = Date.now().toLong()
        val seconds = elapsedMs / 1000
        val id = topicId
        val savedTask = taskId
        val savedTitle = title
        val savedStart = startedAt
        val savedSubject = subjectId
        ticker?.let { window.clearInterval(it) }
        ticker = null
        document.title = "Estudário"
        topicId = null; taskId = null; startedAt = 0L; pausedAt = null; pausedTotal = 0L; plannedMinutes = null; timeUpSeen = false
        if (seconds < 60) { Toast.show("Foco encerrado (menos de 1 minuto não conta)"); return 0 }
        Store.update {
            it.append(
                Keys.FOCUS_SESSIONS,
                jsonOf(
                    "id" to Actions.newSessionId(), "title" to savedTitle.ifBlank { "Sessão de foco" }, "startedAt" to savedStart, "completedAt" to end,
                    "durationSeconds" to seconds, "subjectIds" to (savedSubject?.toString() ?: ""),
                    "origin" to when { savedTask != null -> "PLANO"; id != null -> "TOPICO"; else -> "LIVRE" },
                    "topicId" to id, "taskId" to savedTask,
                ),
            )
        }
        Toast.show("Foco salvo: ${br.com.estudario.web.data.Queries.minutesLabel((seconds / 60).toInt())}")
        return (seconds / 60).toInt()
    }

    /** Encerra, salva, e conclui o tópico (e a tarefa do plano, se houver). */
    fun stopAndComplete() {
        val id = topicId
        val task = taskId
        val start = startedAt
        val minutes = stop()
        Store.update { snapshot ->
            var next = snapshot
            if (task != null) next = Actions.completeTask(next, task, minutes.coerceAtLeast(1))
            if (id != null) next = Actions.completeStudy(next, id, start)
            next
        }
        Toast.show("Tópico concluído. As revisões já estão na agenda.")
    }

    fun clock(): String {
        val s = elapsedMs / 1000
        val h = s / 3600
        val m = (s % 3600) / 60
        val sec = s % 60
        return if (h > 0) "${h}:${m.toString().padStart(2, '0')}:${sec.toString().padStart(2, '0')}" else "${m.toString().padStart(2, '0')}:${sec.toString().padStart(2, '0')}"
    }
}
