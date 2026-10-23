package br.com.estudario.web.data

import kotlinx.coroutines.await
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.browser.window
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

sealed interface LoadState {
    data object Loading : LoadState
    data object SignedOut : LoadState
    /** Conta sem dados ainda: a pessoa precisa ligar a sincronização no app (ou começar aqui). */
    data object NoData : LoadState
    data object Ready : LoadState
    data class Failed(val message: String) : LoadState
}

enum class SaveState { Saved, Pending, Saving, Offline }

/**
 * Estado do app web: a foto carregada, a revisão em que ela se baseia e o envio das mudanças.
 * Cada edição troca a foto inteira (imutável) e agenda o envio; se o celular publicou algo nesse
 * meio-tempo, a pessoa escolhe qual versão manter, como no app.
 */
object Store {
    private val scope: CoroutineScope = MainScope()

    var load by mutableStateOf<LoadState>(LoadState.Loading)
        private set
    var data by mutableStateOf(Snapshot.empty())
        private set
    var save by mutableStateOf(SaveState.Saved)
        private set
    var conflict by mutableStateOf<CloudHead?>(null)
        private set
    /** Por que o último envio falhou (aparece no aviso de "sem conexão" e em Ajustes). */
    var lastError by mutableStateOf<String?>(null)
        private set
    var session by mutableStateOf(Auth.session)
        private set

    /** Demonstração local: nada é enviado para a conta. */
    var demo by mutableStateOf(false)
        private set

    private var baseRevision = 0L
    private var saveJob: Job? = null

    /**
     * As edições feitas aqui desde o último envio, na ordem. Cada uma é uma função pura sobre a foto
     * ("conclua a tarefa X e reorganize o plano"). Se o celular publicou no meio-tempo, a foto da
     * conta é baixada e só essas edições são refeitas sobre ela: nada do celular se perde, e o que
     * já tinha sido feito lá (uma tarefa concluída) vira uma edição sem efeito aqui.
     */
    private val pending = mutableListOf<(Snapshot) -> Snapshot>()

    init {
        window.addEventListener("beforeunload", { event ->
            if (save != SaveState.Saved) {
                event.preventDefault()
                event.asDynamic().returnValue = ""
            }
        })
        window.addEventListener("online", { if (save == SaveState.Offline) scheduleSave(0) })
        // Sem conexão não é estado final: tenta de novo sozinho a cada 30 s (antes ficava travado
        // até recarregar a página, e o que foi feito aqui não chegava ao celular).
        window.setInterval({ if (!demo && save == SaveState.Offline && pending.isNotEmpty()) scheduleSave(0) }, 30_000)
        // O que foi feito no celular aparece aqui sem recarregar: ao voltar para a aba e a cada
        // meio minuto com ela visível, confere se a conta tem revisão nova e troca a foto.
        window.addEventListener("focus", { scope.launch { refreshIfRemoteChanged() } })
        kotlinx.browser.document.addEventListener("visibilitychange", {
            if (kotlinx.browser.document.asDynamic().visibilityState == "visible") scope.launch { refreshIfRemoteChanged() }
        })
        scope.launch {
            while (true) {
                delay(REFRESH_EVERY_MS)
                if (kotlinx.browser.document.asDynamic().visibilityState == "visible") refreshIfRemoteChanged()
            }
        }
    }

    private const val REFRESH_EVERY_MS = 15_000L

    // ---------------------------------------------------------------- cópia sem internet
    // A última foto da conta fica guardada neste navegador (IndexedDB, offline.js). Sem rede, o site abre
    // com ela; o que for feito sem rede fica marcado como "a enviar" e sobe quando a conexão volta.
    /** O site abriu com a cópia deste aparelho (sem internet). */
    var offlineCopy by mutableStateOf(false)
        private set

    private suspend fun cacheGet(): dynamic {
        val userId = Auth.session?.userId ?: return null
        val api = window.asDynamic().estudarioCache ?: return null
        return runCatching { (api.get(userId) as kotlin.js.Promise<dynamic>).await() }.getOrNull()
    }

    private fun cacheSet(text: String, revision: Long, dirty: Boolean) {
        if (demo) return
        val userId = Auth.session?.userId ?: return
        val api = window.asDynamic().estudarioCache ?: return
        runCatching { api.set(kotlin.js.json("userId" to userId, "text" to text, "revision" to revision.toDouble(), "dirty" to dirty, "savedAt" to kotlin.js.Date.now())) }
    }
    private var refreshing = false

    /**
     * Troca a foto pela da conta quando outro aparelho publicou depois da nossa base. Só quando não
     * há nada pendente aqui: edição em andamento tem prioridade e o conflito, se houver, aparece
     * no envio, como antes.
     */
    suspend fun refreshIfRemoteChanged() {
        if (demo || refreshing || load != LoadState.Ready || save != SaveState.Saved || conflict != null) return
        refreshing = true
        try {
            val head = Cloud.head()
            if (head.revision == baseRevision || head.objectPath == null) return
            if (save != SaveState.Saved) return // alguém editou enquanto a cabeça era lida
            val text = Cloud.download(head)
            val fresh = Snapshot.parse(text)
            if (save != SaveState.Saved) return
            data = fresh
            baseRevision = head.revision
            offlineCopy = false
            cacheSet(text, head.revision, dirty = false)
        } catch (_: SignedOutException) {
            load = LoadState.SignedOut
        } catch (_: Throwable) {
            // Sem rede ou erro passageiro: a próxima rodada tenta de novo.
        } finally {
            refreshing = false
        }
    }

    fun start() {
        scope.launch {
            reload()
            // Quem escolheu um edital do catálogo antes de ele ter os pesos da prova recebe agora.
            if (load == LoadState.Ready) runCatching { Catalog.backfillWeights(data) }.getOrNull()?.let { filled -> update { filled(it) } }
        }
    }

    suspend fun reload() {
        Auth.consumeRedirect()
        session = Auth.session
        if (Auth.accessToken() == null) { session = null; load = LoadState.SignedOut; return }
        load = LoadState.Loading
        val cached = cacheGet()
        try {
            val head = Cloud.head()
            baseRevision = head.revision
            conflict = null
            pending.clear()
            save = SaveState.Saved
            offlineCopy = false
            val dirty = cached != null && cached.dirty == true
            if (dirty) {
                // Ficou coisa feita sem internet: sobe agora. Se a conta mudou em outro aparelho nesse
                // meio-tempo, pergunta qual versão manter (a outra fica no histórico da conta).
                data = Snapshot.parse(cached.text as String)
                load = LoadState.Ready
                val cachedRevision = (cached.revision as Number).toLong()
                if (cachedRevision == head.revision) {
                    baseRevision = Cloud.upload(cached.text as String, head.revision)
                    cacheSet(cached.text as String, baseRevision, dirty = false)
                } else {
                    baseRevision = cachedRevision
                    conflict = head
                }
            } else if (head.revision == 0L || head.objectPath == null) {
                data = Snapshot.empty()
                load = LoadState.NoData
            } else {
                val text = Cloud.download(head)
                data = Snapshot.parse(text)
                load = LoadState.Ready
                cacheSet(text, head.revision, dirty = false)
            }
        } catch (_: SignedOutException) {
            load = LoadState.SignedOut
        } catch (error: Throwable) {
            // Sem internet: abre com a cópia deste aparelho, se houver.
            if (cached != null && cached.text != null) {
                data = Snapshot.parse(cached.text as String)
                baseRevision = (cached.revision as Number).toLong()
                load = LoadState.Ready
                offlineCopy = true
                save = if (cached.dirty == true) SaveState.Offline else SaveState.Saved
                lastError = "Sem internet: usando a cópia guardada neste aparelho."
            } else load = LoadState.Failed(error.message ?: "Não deu para carregar seus dados.")
        }
    }

    fun startDemo() {
        demo = true
        load = LoadState.Loading
        scope.launch {
            load = try { data = Demo.build(); LoadState.Ready } catch (error: Throwable) { LoadState.Failed(error.message ?: "Não deu para abrir a demonstração.") }
        }
    }

    fun leaveDemo() {
        demo = false
        data = Snapshot.empty()
        load = LoadState.SignedOut
    }

    fun onSignedIn() {
        session = Auth.session
        start()
    }

    fun signOut() {
        if (demo) { leaveDemo(); return }
        runCatching { window.asDynamic().estudarioCache?.clear() }
        scope.launch {
            Auth.signOut()
            session = null
            data = Snapshot.empty()
            load = LoadState.SignedOut
        }
    }

    /** Começar do zero no navegador, quando a conta ainda não tem dados. */
    fun startEmpty() {
        load = LoadState.Ready
    }

    /** Aplica uma mudança e agenda o envio. */
    fun update(change: (Snapshot) -> Snapshot) {
        data = change(data)
        if (load == LoadState.NoData) load = LoadState.Ready
        if (!demo) {
            pending += change
            scheduleSave(1500)
        }
    }

    private fun scheduleSave(delayMillis: Long) {
        save = SaveState.Pending
        saveJob?.cancel()
        saveJob = scope.launch {
            delay(delayMillis)
            pushNow()
        }
    }

    private suspend fun pushNow(force: Boolean = false, attempt: Int = 0) {
        save = SaveState.Saving
        val sent = pending.size
        try {
            val expected = if (force) Cloud.head().revision else baseRevision
            val text = data.encode()
            baseRevision = Cloud.upload(text, expected)
            cacheSet(text, baseRevision, dirty = false)
            offlineCopy = false
            // Só saem da fila as edições que foram nesta foto; as feitas durante o envio vão na próxima.
            repeat(sent.coerceAtMost(pending.size)) { pending.removeAt(0) }
            save = if (pending.isEmpty()) SaveState.Saved else SaveState.Pending
            lastError = null
            if (pending.isNotEmpty()) scheduleSave(800)
        } catch (error: CloudConflictException) {
            // Outro aparelho publicou depois da nossa base: baixa a versão da conta e refaz só as
            // edições feitas aqui por cima dela. Nunca sobrescreve o que veio do celular.
            if (attempt >= 3) { lastError = "A conta mudou várias vezes seguidas durante o envio."; save = SaveState.Offline; return }
            try {
                var merged = Snapshot.parse(Cloud.download(error.head))
                pending.toList().forEach { change -> merged = runCatching { change(merged) }.getOrDefault(merged) }
                data = merged
                baseRevision = error.head.revision
                pushNow(attempt = attempt + 1)
            } catch (_: SignedOutException) {
                load = LoadState.SignedOut
            } catch (other: Throwable) {
                lastError = describe(other)
                save = SaveState.Offline
            }
        } catch (_: SignedOutException) {
            load = LoadState.SignedOut
        } catch (error: Throwable) {
            lastError = describe(error)
            save = SaveState.Offline
            // Guarda o que foi feito sem internet, para não perder se a aba for fechada.
            runCatching { cacheSet(data.encode(), baseRevision, dirty = true) }
        }
    }

    private fun describe(error: Throwable): String = when (error) {
        is HttpException -> if (error.status == 0) "Sem internet." else "O servidor recusou (${error.status}): ${error.body.take(160)}"
        else -> error.message ?: error.toString()
    }

    /** Tenta enviar de novo agora (o aviso de "sem conexão" chama isto). */
    fun retryNow() { if (!demo && save == SaveState.Offline) scheduleSave(0) }

    /** Conflito: manter o que foi feito aqui (substitui a versão da conta). */
    fun keepMine() {
        conflict = null
        scope.launch { pushNow(force = true) }
    }

    /** Conflito: descartar o que foi feito aqui e carregar a versão da conta. */
    fun takeTheirs() {
        conflict = null
        scope.launch { reload() }
    }
}
