package br.com.estudario.ui.library

import br.com.estudario.data.local.CompetitionEntity
import br.com.estudario.data.local.RemoteSyllabusSyncEntity
import br.com.estudario.data.local.RemoteSyllabusSyncState
import br.com.estudario.data.remote.PrivateSyllabus
import br.com.estudario.data.remote.PrivateSyllabusSource
import br.com.estudario.data.remote.PrivateSyllabusStatus
import br.com.estudario.data.remote.PrivateSyllabusVisibility
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.delay
import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.CompletableDeferred
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class MySyllabiViewModelTest {
    @Test
    fun equalDeviceLocalIdsAndDisplayNamesDoNotMergeDifferentStableIdentities() = runBlocking {
        val local = CompetitionEntity(id = 44, name = "Edital repetido", externalId = "local-identity-a")
        val remote = remote(
            id = "remote-identity-b",
            title = "Edital repetido",
            metadata = buildJsonObject {
                put("localSyllabusId", JsonPrimitive("44"))
                put("localSyllabusExternalId", JsonPrimitive("remote-identity-b"))
                put("stableIdentity", JsonPrimitive("competition:remote-identity-b"))
            },
        )
        val viewModel = viewModel(locals = listOf(local), remotes = listOf(remote))

        viewModel.refresh()
        await { viewModel.state.value.rows.size == 2 }

        assertEquals(2, viewModel.state.value.rows.size)
        assertEquals(1, viewModel.state.value.rows.count { it.local != null && it.remote == null })
        assertEquals(1, viewModel.state.value.rows.count { it.local == null && it.remote != null })
    }

    @Test
    fun differentDeviceLocalIdsMergeWhenStableExternalIdentityMatches() = runBlocking {
        val local = CompetitionEntity(id = 51, name = "Edital local", externalId = "stable-shared")
        val remote = remote(
            id = "remote-shared",
            title = "Outro nome de exibição",
            metadata = buildJsonObject {
                put("localSyllabusId", JsonPrimitive("902"))
                put("localSyllabusExternalId", JsonPrimitive("stable-shared"))
                put("stableIdentity", JsonPrimitive("competition:stable-shared"))
            },
        )
        val viewModel = viewModel(locals = listOf(local), remotes = listOf(remote))

        viewModel.refresh()
        await { viewModel.state.value.rows.singleOrNull()?.let { it.local != null && it.remote != null } == true }

        val row = viewModel.state.value.rows.single()
        assertEquals(51L, row.local?.id)
        assertEquals("remote-shared", row.remote?.remoteSyllabusId)
    }

    @Test
    fun matchingStableRemoteIdentityAppearsAsOneCombinedRowRegardlessOfDifferentTitles() = runBlocking {
        val local = CompetitionEntity(id = 7, name = "Edital importado", externalId = "stable-local", remoteSyllabusId = "remote-1")
        val remote = remote("remote-1", "Título remoto diferente")
        val viewModel = viewModel(locals = listOf(local), remotes = listOf(remote))

        viewModel.refresh()
        await { viewModel.state.value.rows.singleOrNull()?.let { it.local != null && it.remote != null } == true }

        val rows = viewModel.state.value.rows
        assertEquals(1, rows.size)
        assertEquals(7L, rows.single().local?.id)
        assertEquals("remote-1", rows.single().remote?.remoteSyllabusId)
    }

    @Test
    fun remoteOnlyDownloadCallsLosslessRestoreAndAssociatesTheSameRemoteIdentity() = runBlocking {
        val remote = remote("remote-restore", "TRT-3")
        val library = FakeLibrary(listOf(remote))
        val viewModel = viewModel(library = library)

        viewModel.refresh()
        viewModel.download("remote-restore")
        await { library.downloaded.isNotEmpty() }

        assertEquals(listOf("remote-restore"), library.downloaded)
        assertEquals("remote-restore", viewModel.state.value.rows.single().remote?.remoteSyllabusId)
    }

    @Test
    fun localOnlySyllabusRemainsVisibleWithPendingOfflineState() = runBlocking {
        val local = CompetitionEntity(id = 12, name = "Só neste aparelho", externalId = "offline-local")
        val sync = RemoteSyllabusSyncEntity(
            id = 3,
            operation = br.com.estudario.data.local.RemoteSyllabusSyncOperation.UPSERT,
            localSyllabusId = 12,
            payloadHash = "a".repeat(64),
            state = RemoteSyllabusSyncState.PENDING,
        )
        val viewModel = viewModel(locals = listOf(local), syncRows = listOf(sync), listFailure = IllegalStateException("offline"))

        viewModel.refresh()
        await { viewModel.state.value.remoteUnavailable }

        await { viewModel.state.value.rows.size == 1 }
        val row = viewModel.state.value.rows.single()
        assertEquals("Só neste aparelho", row.local?.name)
        assertNull(row.remote)
        assertEquals(RemoteSyllabusSyncState.PENDING, row.syncState)
        assertTrue(viewModel.state.value.remoteUnavailable)
    }

    @Test
    fun deletingLocalDataKeepsPrivateRemoteCopy() = runBlocking {
        val remote = remote("remote-retained", "Edital")
        val local = CompetitionEntity(id = 5, name = "Edital", remoteSyllabusId = remote.remoteSyllabusId)
        val library = FakeLibrary(listOf(remote))
        val viewModel = viewModel(locals = listOf(local), library = library)
        viewModel.refresh()

        viewModel.removeFromDevice(local.id)
        await { library.removedLocally.isNotEmpty() }

        assertEquals(listOf(5L), library.removedLocally)
        await { viewModel.state.value.rows.singleOrNull()?.local == null }
        assertEquals("remote-retained", viewModel.state.value.rows.single().remote?.remoteSyllabusId)
        assertNull(viewModel.state.value.rows.single().local)
    }

    @Test
    fun deletingFromAccountRemovesRemoteCopyWithoutDeletingLocalData() = runBlocking {
        val remote = remote("remote-delete", "Edital")
        val local = CompetitionEntity(id = 8, name = "Edital", remoteSyllabusId = remote.remoteSyllabusId)
        val staleListGate = CompletableDeferred<Unit>()
        val library = FakeLibrary(listOf(remote), firstListGate = staleListGate)
        val viewModel = viewModel(locals = listOf(local), library = library)
        await { viewModel.state.value.rows.singleOrNull()?.local?.id == local.id }

        viewModel.deleteFromAccount("remote-delete")
        await { library.deletedRemotely.isNotEmpty() }

        assertEquals(listOf("remote-delete"), library.deletedRemotely)
        assertEquals(8L, viewModel.state.value.rows.single().local?.id)
        staleListGate.complete(Unit)
        delay(100)
        assertNull(viewModel.state.value.rows.single().remote)
    }

    private fun viewModel(
        locals: List<CompetitionEntity> = emptyList(),
        remotes: List<PrivateSyllabus> = emptyList(),
        syncRows: List<RemoteSyllabusSyncEntity> = emptyList(),
        library: FakeLibrary = FakeLibrary(remotes),
        listFailure: Throwable? = null,
    ): MySyllabiViewModel {
        val localFlow = MutableStateFlow(locals)
        val testLibrary = library.apply {
            failure = listFailure
            onLocalRemoval = { id -> localFlow.value = localFlow.value.filterNot { it.id == id } }
            onDownload = { remoteId ->
                val source = remote.single { it.remoteSyllabusId == remoteId }
                localFlow.value = localFlow.value + CompetitionEntity(
                    id = remoteId.hashCode().toLong(),
                    name = source.title,
                    remoteSyllabusId = remoteId,
                )
            }
        }
        return MySyllabiViewModel(
            localSyllabi = localFlow,
            syncRows = MutableStateFlow(syncRows),
            library = testLibrary,
        )
    }

    private suspend fun await(condition: () -> Boolean) {
        withTimeout(3_000) {
            while (!condition()) delay(10)
        }
    }

    private fun remote(id: String, title: String, metadata: JsonObject = buildJsonObject { }) = PrivateSyllabus(
        remoteSyllabusId = id,
        title = title,
        position = 0,
        visibility = PrivateSyllabusVisibility.PRIVATE,
        source = PrivateSyllabusSource.IMPORTED,
        sourceJobId = null,
        sourceHash = null,
        schemaVersion = 1,
        status = PrivateSyllabusStatus.ACTIVE,
        metadata = metadata,
        subjects = listOf(
            br.com.estudario.data.remote.PrivateSyllabusSubject(
                remoteSubjectId = "subject-$id",
                externalId = "subject-external-$id",
                name = "Matéria",
                position = 0,
                suggestedPriority = br.com.estudario.data.ai.AiPriority.NORMAL,
                packageVersion = "estudo-v2",
                schemaVersion = 1,
                metadata = buildJsonObject { },
                topics = emptyList(),
            ),
        ),
    )

    private class FakeLibrary(
        initialRemote: List<PrivateSyllabus>,
        private val firstListGate: CompletableDeferred<Unit>? = null,
    ) : MySyllabiLibrary {
        var remote = initialRemote
        var failure: Throwable? = null
        private var listCalls = 0
        val downloaded = mutableListOf<String>()
        val removedLocally = mutableListOf<Long>()
        val deletedRemotely = mutableListOf<String>()
        var onLocalRemoval: (Long) -> Unit = {}
        var onDownload: (String) -> Unit = {}

        override suspend fun listRemote(): List<PrivateSyllabus> {
            listCalls++
            val snapshot = remote
            if (listCalls == 1) firstListGate?.await()
            failure?.let { throw it }
            return snapshot
        }

        override suspend fun download(remoteSyllabusId: String): Long {
            downloaded += remoteSyllabusId
            onDownload(remoteSyllabusId)
            return remoteSyllabusId.hashCode().toLong()
        }

        override suspend fun removeFromDevice(localSyllabusId: Long) {
            removedLocally += localSyllabusId
            onLocalRemoval(localSyllabusId)
        }

        override suspend fun deleteFromAccount(remoteSyllabusId: String) {
            deletedRemotely += remoteSyllabusId
            remote = remote.filterNot { it.remoteSyllabusId == remoteSyllabusId }
        }
    }
}
