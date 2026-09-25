package br.com.estudario.ui.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.estudario.data.StudyRepository
import br.com.estudario.data.local.CompetitionEntity
import br.com.estudario.data.local.RemoteSyllabusSyncEntity
import br.com.estudario.data.local.RemoteSyllabusSyncState
import br.com.estudario.data.remote.PrivateSyllabus
import br.com.estudario.data.remote.PrivateSyllabusLibraryRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

data class MySyllabusRow(
    val local: CompetitionEntity? = null,
    val remote: PrivateSyllabus? = null,
    val syncState: RemoteSyllabusSyncState? = null,
)

data class MySyllabiState(
    val rows: List<MySyllabusRow> = emptyList(),
    val loading: Boolean = true,
    val remoteUnavailable: Boolean = false,
    val actionError: String? = null,
)

interface MySyllabiLibrary {
    suspend fun listRemote(): List<PrivateSyllabus>
    suspend fun download(remoteSyllabusId: String): Long
    suspend fun removeFromDevice(localSyllabusId: Long)
    suspend fun deleteFromAccount(remoteSyllabusId: String)
}

class DefaultMySyllabiLibrary(
    private val studyRepository: StudyRepository,
    private val privateSyllabusRepository: PrivateSyllabusLibraryRepository,
) : MySyllabiLibrary {
    override suspend fun listRemote(): List<PrivateSyllabus> = privateSyllabusRepository.listRemote()

    override suspend fun download(remoteSyllabusId: String): Long = privateSyllabusRepository.download(remoteSyllabusId)

    override suspend fun removeFromDevice(localSyllabusId: Long) {
        studyRepository.removeCompetition(localSyllabusId)
    }

    override suspend fun deleteFromAccount(remoteSyllabusId: String) {
        privateSyllabusRepository.deleteRemote(remoteSyllabusId)
    }
}

class MySyllabiViewModel(
    localSyllabi: Flow<List<CompetitionEntity>>,
    syncRows: Flow<List<RemoteSyllabusSyncEntity>>,
    private val library: MySyllabiLibrary,
) : ViewModel() {
    private val mutableState = MutableStateFlow(MySyllabiState())
    val state: StateFlow<MySyllabiState> = mutableState

    private var locals: List<CompetitionEntity> = emptyList()
    private var syncMutations: List<RemoteSyllabusSyncEntity> = emptyList()
    private var remotes: List<PrivateSyllabus> = emptyList()
    private var remoteRevision: Long = 0

    init {
        viewModelScope.launch {
            localSyllabi.collect {
                locals = it
                publishRows()
            }
        }
        viewModelScope.launch {
            syncRows.collect {
                syncMutations = it
                publishRows()
            }
        }
        viewModelScope.launch { refresh() }
    }

    suspend fun refresh() {
        val requestRevision = ++remoteRevision
        mutableState.value = mutableState.value.copy(loading = true, actionError = null)
        try {
            val response = library.listRemote()
            if (requestRevision == remoteRevision) {
                remotes = response
                mutableState.value = mutableState.value.copy(remoteUnavailable = false, loading = false)
            }
        } catch (error: CancellationException) {
            throw error
        } catch (_: Throwable) {
            if (requestRevision == remoteRevision) {
                mutableState.value = mutableState.value.copy(remoteUnavailable = true, loading = false)
            }
        }
        publishRows()
    }

    fun download(remoteSyllabusId: String) {
        viewModelScope.launch {
            runAction { library.download(remoteSyllabusId) }
        }
    }

    fun removeFromDevice(localSyllabusId: Long) {
        viewModelScope.launch {
            runAction { library.removeFromDevice(localSyllabusId) }
        }
    }

    fun deleteFromAccount(remoteSyllabusId: String) {
        viewModelScope.launch {
            runAction {
                library.deleteFromAccount(remoteSyllabusId)
                remoteRevision++
                remotes = remotes.filterNot { it.remoteSyllabusId == remoteSyllabusId }
                publishRows()
            }
        }
    }

    private suspend fun runAction(action: suspend () -> Unit) {
        mutableState.value = mutableState.value.copy(actionError = null)
        try {
            action()
        } catch (error: CancellationException) {
            throw error
        } catch (_: Throwable) {
            mutableState.value = mutableState.value.copy(actionError = "Não foi possível concluir. Verifique a conexão e tente novamente.")
        }
        publishRows()
    }

    private fun publishRows() {
        val unmatchedRemotes = remotes.toMutableList()
        val rows = locals.map { local ->
            val remote = unmatchedRemotes.firstOrNull { matches(local, it) }
            if (remote != null) unmatchedRemotes.remove(remote)
            val syncState = if (remote != null) RemoteSyllabusSyncState.SYNCED else {
                syncMutations.asSequence()
                    .filter { it.localSyllabusId == local.id && it.lastError != "SUPERSEDED" }
                    .maxByOrNull { it.updatedAt }
                    ?.state
            }
            MySyllabusRow(local = local, remote = remote, syncState = syncState)
        } + unmatchedRemotes.map { MySyllabusRow(remote = it) }
        mutableState.value = mutableState.value.copy(rows = rows)
    }

    private fun matches(local: CompetitionEntity, remote: PrivateSyllabus): Boolean {
        if (local.remoteSyllabusId != null && local.remoteSyllabusId == remote.remoteSyllabusId) return true
        val metadata = remote.metadata
        val externalId = local.externalId ?: return false
        val remoteExternalId = metadata["localSyllabusExternalId"]?.toString()?.trim('"')
        val stableIdentity = metadata["stableIdentity"]?.toString()?.trim('"')
        return externalId == remoteExternalId || externalId == stableIdentity || "competition:$externalId" == stableIdentity
    }
}
