package br.com.estudario.focus

import br.com.estudario.data.local.FocusSessionEntity

/** Mantém o cronômetro recuperável até que a sessão esteja salva no histórico. */
internal class FocusSessionStopCoordinator(
    private val save: suspend (FocusSessionEntity) -> Unit,
    private val clearActive: suspend () -> Unit,
) {
    suspend fun finish(session: FocusSessionEntity) {
        save(session)
        clearActive()
    }
}
