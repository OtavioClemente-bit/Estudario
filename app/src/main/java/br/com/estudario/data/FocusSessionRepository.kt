package br.com.estudario.data

import br.com.estudario.data.local.AppDao
import br.com.estudario.data.local.FocusSessionEntity
import kotlinx.coroutines.flow.Flow

/** Persistência do histórico do modo foco, com identidade estável para encerrar uma sessão uma vez. */
class FocusSessionRepository(private val dao: AppDao) {
    val sessions: Flow<List<FocusSessionEntity>> = dao.focusSessions()

    /** Retorna false quando esta sessão já foi gravada. */
    suspend fun recordOnce(session: FocusSessionEntity): Boolean =
        dao.insertFocusSessionIfAbsent(session) != -1L
}
