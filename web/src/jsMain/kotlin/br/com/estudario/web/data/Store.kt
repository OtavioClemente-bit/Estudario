package br.com.estudario.web.data

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
    var session by mutableStateOf(Auth.session)
        private set

    /** Demonstração local: nada é enviado para a conta. */
    var demo by mutableStateOf(false)
        private set

    private var baseRevision = 0L
    /** Última edição feita neste navegador; decide o conflito sem perguntar. */
    private var lastLocalChangeAt = 0.0
    private var saveJob: Job? = null

    init {
        window.addEventListener("beforeunload", { event ->
            if (save != SaveState.Saved) {
                event.preventDefault()
                event.asDynamic().returnValue = ""
            }
        })
        window.addEventListener("online", { if (save == SaveState.Offline) scheduleSave(0) })
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

    private const val REFRESH_EVERY_MS = 30_000L
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
            val fresh = Snapshot.parse(Cloud.download(head))
            if (save != SaveState.Saved) return
            data = fresh
            baseRevision = head.revision
        } catch (_: SignedOutException) {
            load = LoadState.SignedOut
        } catch (_: Throwable) {
            // Sem rede ou erro passageiro: a próxima rodada tenta de novo.
        } finally {
            refreshing = false
        }
    }

    fun start() {
        scope.launch { reload() }
    }

    suspend fun reload() {
        Auth.consumeRedirect()
        session = Auth.session
        if (Auth.accessToken() == null) { session = null; load = LoadState.SignedOut; return }
        load = LoadState.Loading
        try {
            val head = Cloud.head()
            baseRevision = head.revision
            if (head.revision == 0L || head.objectPath == null) {
                data = Snapshot.empty()
                load = LoadState.NoData
            } else {
                data = Snapshot.parse(Cloud.download(head))
                load = LoadState.Ready
            }
            save = SaveState.Saved
            conflict = null
        } catch (_: SignedOutException) {
            load = LoadState.SignedOut
        } catch (error: Throwable) {
            load = LoadState.Failed(error.message ?: "Não deu para carregar seus dados.")
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
        lastLocalChangeAt = kotlin.js.Date.now()
        if (load == LoadState.NoData) load = LoadState.Ready
        if (!demo) scheduleSave(1500)
    }

    private fun scheduleSave(delayMillis: Long) {
        save = SaveState.Pending
        saveJob?.cancel()
        saveJob = scope.launch {
            delay(delayMillis)
            pushNow()
        }
    }

    private suspend fun pushNow(force: Boolean = false) {
        save = SaveState.Saving
        try {
            val expected = if (force) Cloud.head().revision else baseRevision
            baseRevision = Cloud.upload(data.encode(), expected)
            save = SaveState.Saved
        } catch (error: CloudConflictException) {
            // Outro aparelho publicou depois da nossa base: vence quem mudou por último, sem
            // perguntar (as fotos anteriores ficam guardadas na conta).
            val remoteAt = error.head.updatedAt?.let { kotlin.js.Date(it).getTime() } ?: 0.0
            if (lastLocalChangeAt >= remoteAt) pushNow(force = true) else reload()
        } catch (_: SignedOutException) {
            load = LoadState.SignedOut
        } catch (_: Throwable) {
            save = SaveState.Offline
        }
    }

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
