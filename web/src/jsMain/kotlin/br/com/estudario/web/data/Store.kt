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
    private var saveJob: Job? = null

    init {
        window.addEventListener("beforeunload", { event ->
            if (save != SaveState.Saved) {
                event.preventDefault()
                event.asDynamic().returnValue = ""
            }
        })
        window.addEventListener("online", { if (save == SaveState.Offline) scheduleSave(0) })
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
            conflict = error.head
            save = SaveState.Pending
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
