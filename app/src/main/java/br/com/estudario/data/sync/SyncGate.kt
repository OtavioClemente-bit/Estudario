package br.com.estudario.data.sync

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Os dados deste aparelho já conferiram a conta desde que o app voltou para a tela?
 *
 * Ao abrir o app, a sincronização roda em segundo plano e leva alguns segundos. Nesse intervalo o
 * celular ainda não sabe o que foi feito no site: uma tarefa concluída lá aparecia aqui como
 * "atrasada". Enquanto [fresh] é falso, as telas não mostram avisos de atraso; volta a valer quando a
 * sincronização termina (bem ou mal) ou depois de [TIMEOUT_MS], para nunca esconder para sempre.
 */
object SyncGate {
    private const val TIMEOUT_MS = 12_000L
    private val _fresh = MutableStateFlow(true)
    val fresh: StateFlow<Boolean> = _fresh.asStateFlow()
    @Volatile private var heldAt = 0L

    /** O app voltou para a tela com conta e sincronização ligadas: espera a conta antes de acusar atraso. */
    fun hold() { heldAt = System.currentTimeMillis(); _fresh.value = false }

    fun release() { _fresh.value = true }

    /** Tempo esgotado (sem rede, por exemplo): mostra o que tem. */
    fun releaseIfStale() { if (!_fresh.value && System.currentTimeMillis() - heldAt >= TIMEOUT_MS) _fresh.value = true }

    const val timeoutMs = TIMEOUT_MS
}
