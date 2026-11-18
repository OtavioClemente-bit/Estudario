package br.com.estudario.web.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import br.com.estudario.web.data.Actions
import br.com.estudario.web.data.Keys
import br.com.estudario.web.data.Store
import br.com.estudario.web.data.jsonOf
import br.com.estudario.domain.FocusRules
import br.com.estudario.domain.FocusRunning
import br.com.estudario.web.data.Prefs
import kotlinx.browser.document
import kotlinx.browser.window
import kotlin.js.Date

/**
 * O modo foco do site, um só para o site inteiro (como o cronômetro no topo do app): o tempo
 * cresce, nunca regride. Pode nascer de um tópico, de uma tarefa do plano ou solto. Se houver
 * tempo planejado, ao alcançá-lo avisa uma vez, e a sessão continua contando.
 */
object TopicFocus {
    /** Identidade da sessão, a mesma no app (FocusRunning.sessionId) e na linha do histórico. */
    var sessionId by mutableStateOf("")
    /** Matérias da sessão (o app permite várias; o site guarda todas as que vierem de lá). */
    var subjectList by mutableStateOf<List<Long>>(emptyList())
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
    /** Contagem escondida: a mesma do app (preferência de foco da conta, FocusSync.kt). */
    val clockHidden: Boolean get() = Prefs.focusPrefs().clockHidden
    fun setClockHidden(value: Boolean) {
        runCatching { window.localStorage.setItem("estudario.focusbar.hidden", if (value) "1" else "0") }
        Prefs.setFocusPrefs(Prefs.focusPrefs().copy(clockHidden = value))
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
        if (active) { pausedTotal += gap.coerceAtMost(elapsedMs); rev = Date.now().toLong(); persist(); publish() }
        idleGap = null
    }

    // ---------------------------------------------------------------- sobreviver ao F5
    // O foco fica guardado neste navegador a cada segundo. Recarregar ou fechar a aba sem querer
    // não perde a sessão: ela volta pausada no ponto em que estava (o tempo com a aba fechada não
    // conta) e é só continuar ou encerrar.
    private const val SAVE_KEY = "estudario.focus.active"
    /**
     * Focos já encerrados neste navegador (pela hora em que começaram, que é a identidade do foco e é a
     * mesma em todas as janelas). Nenhuma janela volta a guardar, restaurar ou salvar um foco desta lista:
     * é o que impede o foco duplicado quando há várias janelas abertas.
     */
    private const val DONE_KEY = "estudario.focus.done"
    /** Momento da última ação da pessoa (começar, pausar, continuar, descontar): entre janelas, a mais nova vale. */
    private var rev = 0L

    private fun doneList(): List<Long> = runCatching {
        (window.localStorage.getItem(DONE_KEY) ?: "").split(',').mapNotNull { it.trim().toLongOrNull() }
    }.getOrDefault(emptyList())

    private fun isDone(started: Long): Boolean = started > 0 && (started in doneList() || Store.data.focusSessions.any { it.startedAt == started || (sessionId.isNotBlank() && it.id == sessionId) })

    private fun markDone(started: Long) {
        runCatching { window.localStorage.setItem(DONE_KEY, (doneList().filter { it != started } + started).takeLast(60).joinToString(",")) }
    }

    private fun readSaved(): dynamic = runCatching { window.localStorage.getItem(SAVE_KEY)?.let { JSON.parse<Any>(it).asDynamic() } }.getOrNull()

    /** Some o foco desta janela sem salvar nada (foi encerrado em outra). */
    private fun dropLocal() {
        ticker?.let { window.clearInterval(it) }
        ticker = null
        document.title = "Estudário"
        topicId = null; taskId = null; subjectId = null; title = ""; startedAt = 0L; pausedAt = null; pausedTotal = 0L; plannedMinutes = null; timeUpSeen = false; rev = 0L
        sessionId = ""; subjectList = emptyList()
    }

    /** Copia o foco guardado (de outra janela) para esta. */
    private fun adopt(o: dynamic, keepRunning: Boolean) {
        val started = (o.startedAt as? Number)?.toLong() ?: return
        sessionId = (o.sessionId as? String)?.takeIf { it.isNotBlank() } ?: "web-$started"
        subjectList = (o.subjectIds as? String).orEmpty().split(',').mapNotNull { it.trim().toLongOrNull() }
        topicId = (o.topicId as? Number)?.toLong()
        taskId = o.taskId as? String
        subjectId = (o.subjectId as? Number)?.toLong()
        title = (o.title as? String).orEmpty()
        plannedMinutes = (o.plannedMinutes as? Number)?.toInt()
        timeUpSeen = o.timeUpSeen == true
        startedAt = started
        pausedTotal = (o.pausedTotal as? Number)?.toLong() ?: 0L
        val lastSeen = (o.lastSeen as? Number)?.toLong() ?: started
        pausedAt = (o.pausedAt as? Number)?.toLong() ?: if (keepRunning) null else lastSeen
        rev = (o.rev as? Number)?.toLong() ?: 0L
        now = Date.now().toLong()
        if (ticker == null) startTicker()
    }

    private fun persist() {
        runCatching {
            if (!active) { window.localStorage.removeItem(SAVE_KEY); return }
            // Encerrado em outra janela: esta não pode ressuscitar o foco.
            if (isDone(startedAt)) { dropLocal(); window.localStorage.removeItem(SAVE_KEY); return }
            // Outra janela mexeu depois (pausou, continuou, começou outro): vale o que ela guardou.
            val saved = readSaved()
            val savedRev = (saved?.rev as? Number)?.toLong() ?: 0L
            val savedStart = (saved?.startedAt as? Number)?.toLong() ?: 0L
            if (saved != null && savedRev > rev && !isDone(savedStart)) { adopt(saved, keepRunning = true); return }
            val o: dynamic = js("({})")
            o.sessionId = sessionId; o.subjectIds = subjectList.joinToString(",")
            o.topicId = topicId?.toDouble(); o.taskId = taskId; o.subjectId = subjectId?.toDouble(); o.title = title
            o.startedAt = startedAt.toDouble(); o.pausedAt = pausedAt?.toDouble(); o.pausedTotal = pausedTotal.toDouble()
            o.plannedMinutes = plannedMinutes; o.timeUpSeen = timeUpSeen; o.lastSeen = Date.now(); o.rev = rev.toDouble()
            window.localStorage.setItem(SAVE_KEY, JSON.stringify(o))
        }
    }

    private fun startTicker() {
        ticker?.let { window.clearInterval(it) }
        ticker = window.setInterval({
            now = Date.now().toLong()
            // Esquecido aberto além do teto (o mesmo do app): encerra e salva sozinho.
            if (running && elapsedMinutes >= FocusRules.MAX_SESSION_MINUTES) {
                stop()
                Toast.show("O foco ficou aberto por ${FocusRules.MAX_SESSION_MINUTES / 60} horas e foi encerrado sozinho.")
                return@setInterval
            }
            document.title = if (clockHidden) "Foco · Estudário" else "${clock()} · Foco · Estudário"
            persist()
        }, 1000)
    }

    // ---------------------------------------------------------------- o mesmo foco no app
    // A sessão vai na foto da conta ("focusRunning" em userPrefs, FocusSync.kt) a cada mexida. Quando
    // a foto muda (o celular começou, pausou, continuou ou encerrou), esta janela fica igual.

    /** A sessão desta janela no formato da conta. */
    private fun running(): FocusRunning? = if (!active) null else FocusRunning(
        sessionId = sessionId.ifBlank { "web-$startedAt" }, startedAt = startedAt, title = title.ifBlank { FocusRules.DEFAULT_TITLE },
        subjectIds = subjectList.ifEmpty { listOfNotNull(subjectId) }, origin = FocusRules.origin(taskId, topicId),
        topicId = topicId, taskId = taskId, plannedMinutes = plannedMinutes,
        pausedAt = pausedAt ?: 0L, pausedMillis = pausedTotal, updatedAt = rev,
    )

    private fun publish(r: FocusRunning? = running()) {
        if (r == null || Store.demo) return
        Prefs.setSyncedFields(mapOf(FocusRules.KEY_RUNNING to kotlinx.serialization.json.JsonObject(Prefs.fieldsJson(r.fields()))))
    }

    private fun remoteRunning(): FocusRunning? =
        (Prefs.synced(Store.data)[FocusRules.KEY_RUNNING] as? kotlinx.serialization.json.JsonObject)?.let { FocusRunning.from(Prefs.getter(it)) }

    private var lastData: Any? = null
    private var lastEndedHere: String? = null

    private fun watchAccount() {
        if (Store.data === lastData) return
        lastData = Store.data
        val remote = remoteRunning() ?: return
        val saved = Store.data.focusSessions.mapTo(hashSetOf()) { it.id }
        when (val d = FocusRunning.reconcile(running(), remote, Date.now().toLong(), { it in saved }, lastEndedHere)) {
            FocusRunning.Remote.Keep -> Unit
            is FocusRunning.Remote.Adopt -> {
                val r = d.running
                val isNew = r.sessionId != sessionId
                val o: dynamic = js("({})")
                o.sessionId = r.sessionId; o.subjectIds = r.subjectIds.joinToString(",")
                o.topicId = r.topicId?.toDouble(); o.taskId = r.taskId; o.subjectId = r.subjectIds.firstOrNull()?.toDouble()
                o.title = r.title; o.plannedMinutes = r.plannedMinutes; o.timeUpSeen = !isNew && timeUpSeen
                o.startedAt = r.startedAt.toDouble(); o.pausedAt = r.pausedAt.takeIf { it > 0L }?.toDouble(); o.pausedTotal = r.pausedMillis.toDouble()
                o.rev = r.updatedAt.toDouble(); o.lastSeen = Date.now()
                adopt(o, keepRunning = true)
                if (isNew) Toast.show("Foco começado no outro aparelho: o tempo segue aqui também.")
            }
            FocusRunning.Remote.EndedElsewhere -> {
                markDone(startedAt)
                dropLocal()
                runCatching { window.localStorage.removeItem(SAVE_KEY) }
                Toast.show("Este foco foi encerrado no outro aparelho.")
            }
        }
    }

    init {
        window.setInterval({ runCatching { watchAccount() } }, 3000)
        runCatching {
            val o: dynamic = readSaved() ?: return@runCatching
            val started = (o.startedAt as? Number)?.toLong() ?: return@runCatching
            // Já foi encerrado (nesta ou em outra janela): não volta.
            if (started in doneList()) { window.localStorage.removeItem(SAVE_KEY); return@runCatching }
            val lastSeen = (o.lastSeen as? Number)?.toLong() ?: started
            // Outra janela está com ele rodando agora (guardou há poucos segundos): esta entra rodando junto.
            // Senão (recarregou ou fechou a aba), volta pausado no último segundo visto.
            val otherWindowAlive = o.pausedAt == null && Date.now().toLong() - lastSeen < 4_000
            adopt(o, keepRunning = otherWindowAlive)
            if (!otherWindowAlive) window.setTimeout({ Toast.show("Seu foco foi guardado e está pausado. Continue quando quiser.") }, 1500)
        }
        // Janelas abertas ao mesmo tempo ficam iguais: pausar, continuar ou encerrar numa vale para todas.
        window.addEventListener("storage", { event ->
            val e = event.asDynamic()
            if (e.key != SAVE_KEY && e.key != DONE_KEY) return@addEventListener
            val saved = readSaved()
            val savedStart = (saved?.startedAt as? Number)?.toLong() ?: 0L
            when {
                active && isDone(startedAt) -> { dropLocal(); Toast.show("Este foco foi encerrado em outra janela.") }
                saved != null && savedStart > 0 && !isDone(savedStart) && ((saved.rev as? Number)?.toLong() ?: 0L) >= rev && (savedStart != startedAt || ((saved.rev as? Number)?.toLong() ?: 0L) > rev) -> adopt(saved, keepRunning = true)
                saved == null && active && !isDone(startedAt) -> Unit
            }
        })
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
        subjectList = listOfNotNull(this.subjectId)
        sessionId = Actions.newSessionId()
        title = name
        this.plannedMinutes = plannedMinutes
        startedAt = Date.now().toLong()
        now = startedAt
        pausedAt = null
        pausedTotal = 0L
        timeUpSeen = false
        rev = startedAt
        startTicker()
        persist()
        publish()
    }

    /** Compatível com a chamada antiga do tópico. */
    fun start(id: Long, name: String) = start(id, name, null, null, null)

    fun pause() { if (running) { pausedAt = Date.now().toLong(); rev = Date.now().toLong(); persist(); publish() } }
    fun resume() { pausedAt?.let { pausedTotal += Date.now().toLong() - it; pausedAt = null; rev = Date.now().toLong(); persist(); publish() } }

    /** O último encerrar não salvou porque esse foco já estava salvo (outra janela ou atualização). */
    private var lastStopWasDuplicate = false

    /** Encerra e salva no histórico (menos de 1 minuto não conta). Devolve os minutos. */
    fun stop(): Int {
        lastStopWasDuplicate = false
        if (!active) return 0
        // Já salvo antes (em outra janela, ou a página voltou com ele): não salva de novo.
        if (isDone(startedAt)) {
            lastStopWasDuplicate = true
            dropLocal()
            runCatching { window.localStorage.removeItem(SAVE_KEY) }
            Toast.show("Este foco já estava salvo no histórico. Nada foi duplicado.")
            return 0
        }
        markDone(startedAt)
        val end = Date.now().toLong()
        val seconds = elapsedMs / 1000
        // O outro aparelho fica sabendo que acabou (e não salva de novo).
        val ended = running()?.end(end)
        val savedId = ended?.sessionId ?: Actions.newSessionId()
        val savedSubjects = subjectList.ifEmpty { listOfNotNull(subjectId) }
        lastEndedHere = savedId
        sessionId = ""; subjectList = emptyList()
        val id = topicId
        val savedTask = taskId
        val savedTitle = title
        val savedStart = startedAt
        val savedSubject = subjectId
        ticker?.let { window.clearInterval(it) }
        ticker = null
        document.title = "Estudário"
        topicId = null; taskId = null; startedAt = 0L; pausedAt = null; pausedTotal = 0L; plannedMinutes = null; timeUpSeen = false; rev = 0L
        persist()
        publish(ended)
        if (!FocusRules.counts(seconds)) { Toast.show("Foco encerrado (menos de 1 minuto não conta)"); return 0 }
        som("foco_fim")
        Store.update {
            // Última trava: o histórico não aceita duas sessões que começaram no mesmo instante.
            if (it.focusSessions.any { s -> s.startedAt == savedStart || s.id == savedId }) return@update it
            it.append(
                Keys.FOCUS_SESSIONS,
                jsonOf(
                    "id" to savedId, "title" to savedTitle.ifBlank { FocusRules.DEFAULT_TITLE }, "startedAt" to savedStart, "completedAt" to end,
                    "durationSeconds" to seconds, "subjectIds" to savedSubjects.sorted().joinToString(","),
                    // Os mesmos valores do app (FocusSessionOrigin: LIVRE, MATERIA, PLANO); outro valor quebrava a restauração lá.
                    "origin" to FocusRules.origin(savedTask, id),
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
        if (lastStopWasDuplicate) return
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
