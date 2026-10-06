package br.com.estudario.data.sync

/**
 * O que a sincronização deve fazer, decidido só com números, sem rede nem banco.
 *
 * A conta guarda uma "foto" atual com um número de revisão. O aparelho lembra a revisão e o hash
 * da última foto que enviou ou recebeu (a base). Comparando a base com a foto local e com a
 * revisão da nuvem sabemos quem mudou desde a última vez.
 */
object SyncPlanner {
    data class Input(
        /** Revisão da última foto enviada/recebida por este aparelho; null se nunca sincronizou. */
        val baseRevision: Long?,
        /** Hash da última foto enviada/recebida por este aparelho. */
        val baseSha: String?,
        /** Hash da foto atual deste aparelho. */
        val localSha: String,
        /** Este aparelho ainda não tem nenhum dado de estudo (instalação nova). */
        val localEmpty: Boolean,
        /** Revisão atual da nuvem; 0 quando a conta nunca recebeu uma foto. */
        val remoteRevision: Long,
        /** Hash da foto atual da nuvem; null quando não existe. */
        val remoteSha: String?,
    )

    sealed interface Action {
        data object Nothing : Action
        data object Upload : Action
        data object Download : Action
        /** Os dois lados mudaram: a pessoa escolhe qual versão manter. */
        data object Conflict : Action
        /** Os dois lados já são iguais; só falta lembrar a revisão da nuvem como base. */
        data object AdoptRemote : Action
    }

    fun decide(input: Input): Action = with(input) {
        val remoteEmpty = remoteRevision == 0L || remoteSha == null
        if (remoteEmpty) return if (localEmpty) Action.Nothing else Action.Upload
        if (remoteSha == localSha) return if (baseRevision == remoteRevision && baseSha == localSha) Action.Nothing else Action.AdoptRemote
        if (baseRevision == null) {
            // Primeira vez neste aparelho e a conta já tem dados.
            return if (localEmpty) Action.Download else Action.Conflict
        }
        val localChanged = localSha != baseSha
        val remoteChanged = remoteRevision != baseRevision
        when {
            localChanged && remoteChanged -> Action.Conflict
            localChanged -> Action.Upload
            remoteChanged -> Action.Download
            else -> Action.Nothing
        }
    }
}
