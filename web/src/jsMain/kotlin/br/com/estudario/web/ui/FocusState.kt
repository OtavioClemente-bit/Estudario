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

    /** Contagem escondida (o tempo distrai): some da barra, do título da aba, do tópico e do Modo foco. Fica neste navegador. */
    var clockHidden by mutableStateOf(runCatching { window.localStorage.getItem("estudario.focusbar.hidden") == "1" }.getOrDefault(false))
        private set
    fun setClockHidden(value: Boolean) {
        clockHidden = value
        runCatching { window.localStorage.setItem("estudario.focusbar.hidden", if (value) "1" else "0") }
        if (active) document.title = if (value) "Foco · Estudário" else "${clock()} · Foco · Estudário"
    }
    var timeUpSeen by mutableStateOf(false)
    private var ticker: Int? = null

    // ---------------------------------------------------------------- tempo parado
    // Esqueceu o foco ligado: sem mexer na página por 20 minutos, na volta ele pergunta se a pessoa
    // estava estudando e, se não, desconta o tempo parado. O tempo continua honesto.
    private const val IDLE_MS = 20 * 60_000L
    private var lastActive = Date.now().toLong()
    /** Tempo parado esperando resposta (ms), ou null. */
    var idleGap by mutableStateOf<Long?>(null)

    /** Chamado a cada movimento, tecla, toque ou rolagem. */
    fun activity() {
        val t = Date.now().toLong()
        if (running && idleGap == null && t - lastActive >= IDLE_MS) idleGap = t - lastActive
        lastActive = t
    }

    /** Desconta o tempo parado como pausa. */
    fun discountIdle() {
        val gap = idleGap ?: return
        if (active) { pausedTotal += gap.coerceAtMost(elapsedMs); persist() }
        idleGap = null
    }

    // ---------------------------------------------------------------- sobreviver ao F5
    // O foco fica guardado neste navegador a cada segundo. Recarregar ou fechar a aba sem querer
    // não perde a sessão: ela volta pausada no ponto em que estava (o tempo com a aba fechada não
    // conta) e é só continuar ou encerrar.
    private const val SAVE_KEY = "estudario.focus.active"

    private fun persist() {
        runCatching {
            if (!active) { window.localStorage.removeItem(SAVE_KEY); return }
            val o: dynamic = js("({})")
            o.topicId = topicId?.toDouble(); o.taskId = taskId; o.subjectId = subjectId?.toDouble(); o.title = title
            o.startedAt = startedAt.toDouble(); o.pausedAt = pausedAt?.toDouble(); o.pausedTotal = pausedTotal.toDouble()
            o.plannedMinutes = plannedMinutes; o.timeUpSeen = timeUpSeen; o.lastSeen = Date.now()
            window.localStorage.setItem(SAVE_KEY, JSON.stringify(o))
        }
    }

    private fun startTicker() {
        ticker?.let { window.clearInterval(it) }
        ticker = window.setInterval({
            now = Date.now().toLong()
            document.title = if (clockHidden) "Foco · Estudário" else "${clock()} · Foco · Estudário"
            persist()
        }, 1000)
    }

    init {
        runCatching {
            val raw = window.localStorage.getItem(SAVE_KEY) ?: return@runCatching
            val o: dynamic = JSON.parse<Any>(raw)
            val started = (o.startedAt as? Number)?.toLong() ?: return@runCatching
            val lastSeen = (o.lastSeen as? Number)?.toLong() ?: started
            topicId = (o.topicId as? Number)?.toLong()
            taskId = o.taskId as? String
            subjectId = (o.subjectId as? Number)?.toLong()
            title = (o.title as? String).orEmpty()
            plannedMinutes = (o.plannedMinutes as? Number)?.toInt()
            timeUpSeen = o.timeUpSeen == true
            startedAt = started
            pausedTotal = (o.pausedTotal as? Number)?.toLong() ?: 0L
            // Já estava pausado: fica como estava. Rodando: pausa no último segundo visto.
            pausedAt = (o.pausedAt as? Number)?.toLong() ?: lastSeen
            now = Date.now().toLong()
            startTicker()
            window.setTimeout({ Toast.show("Seu foco foi guardado e está pausado. Continue quando quiser.") }, 1500)
        }
        // Com foco ativo, o navegador pergunta antes de recarregar ou fechar a aba.
        window.addEventListener("beforeunload", { event ->
            if (active) {
                persist()
                event.preventDefault()
                event.asDynamic().returnValue = ""
            }
        })
    }

    val active: Boolean get() = startedAt > 0L
    val running: Boolean get() = active && pausedAt == null
    val elapsedMs: Long get() = if (!active) 0L else ((pausedAt ?: now) - startedAt - pausedTotal).coerceAtLeast(0L)
    val elapsedMinutes: Int get() = (elapsedMs / 60_000L).toInt()
    val timeUp: Boolean get() = active && !timeUpSeen && plannedMinutes?.let { elapsedMs >= it * 60_000L } == true
    val canComplete: Boolean get() = if (taskId == null) topicId != null else Store.data.tasks.any { it.id == taskId && it.type != "QUESTIONS" }

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
        startTicker()
        persist()
    }

    /** Compatível com a chamada antiga do tópico. */
    fun start(id: Long, name: String) = start(id, name, null, null, null)

    fun pause() { if (running) { pausedAt = Date.now().toLong(); persist() } }
    fun resume() { pausedAt?.let { pausedTotal += Date.now().toLong() - it; pausedAt = null; persist() } }

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
        persist()
        if (seconds < 60) { Toast.show("Foco encerrado (menos de 1 minuto não conta)"); return 0 }
        Store.update {
            it.append(
                Keys.FOCUS_SESSIONS,
                jsonOf(
                    "id" to Actions.newSessionId(), "title" to savedTitle.ifBlank { "Sessão de foco" }, "startedAt" to savedStart, "completedAt" to end,
                    "durationSeconds" to seconds, "subjectIds" to (savedSubject?.toString() ?: ""),
                    // Os mesmos valores do app (FocusSessionOrigin: LIVRE, MATERIA, PLANO); outro valor quebrava a restauração lá.
                    "origin" to when { savedTask != null -> "PLANO"; id != null -> "MATERIA"; else -> "LIVRE" },
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
        val complete = canComplete
        val minutes = stop()
        if (!complete) {
            Toast.show("Foco salvo. Finalize a bateria de questões para concluí-la no plano.")
            return
        }
        Store.update { snapshot ->
            Actions.completeFocusWithPlan(snapshot, id, task, start, minutes)
        }
        Toast.show("Conclusão registrada.")
    }

    fun clock(): String {
        val s = elapsedMs / 1000
        val h = s / 3600
        val m = (s % 3600) / 60
        val sec = s % 60
        return if (h > 0) "${h}:${m.toString().padStart(2, '0')}:${sec.toString().padStart(2, '0')}" else "${m.toString().padStart(2, '0')}:${sec.toString().padStart(2, '0')}"
    }
}
