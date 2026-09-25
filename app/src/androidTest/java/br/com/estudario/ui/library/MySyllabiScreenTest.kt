package br.com.estudario.ui.library

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import br.com.estudario.data.local.CompetitionEntity
import br.com.estudario.data.local.RemoteSyllabusSyncEntity
import br.com.estudario.data.local.RemoteSyllabusSyncOperation
import br.com.estudario.data.local.RemoteSyllabusSyncState
import br.com.estudario.data.remote.PrivateSyllabus
import br.com.estudario.data.remote.PrivateSyllabusSource
import br.com.estudario.data.remote.PrivateSyllabusStatus
import br.com.estudario.data.remote.PrivateSyllabusVisibility
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.serialization.json.buildJsonObject
import org.junit.Rule
import org.junit.Test

class MySyllabiScreenTest {
    @get:Rule val compose = createComposeRule()

    @Test
    fun pendingLocalCopyIsNeverPresentedAsSavedInAccount() {
        val viewModel = MySyllabiViewModel(
            localSyllabi = MutableStateFlow(listOf(CompetitionEntity(id = 2, name = "TRT-3"))),
            syncRows = MutableStateFlow(
                listOf(
                    RemoteSyllabusSyncEntity(
                        id = 9,
                        operation = RemoteSyllabusSyncOperation.UPSERT,
                        localSyllabusId = 2,
                        payloadHash = "f".repeat(64),
                        state = RemoteSyllabusSyncState.PENDING,
                    ),
                ),
            ),
            library = ScreenFakeLibrary(),
        )
        compose.setContent { MySyllabiScreen(viewModel) }

        compose.onNodeWithText("PENDING · Salvo neste dispositivo; sincronização pendente").assertIsDisplayed()
        compose.onNodeWithText("Salvo na sua conta").assertDoesNotExist()
    }

    @Test
    fun deletingFromAccountRequiresExplicitConfirmationAndExplainsLocalRetention() {
        val remote = PrivateSyllabus(
            remoteSyllabusId = "remote-2",
            title = "TRT-3 remoto",
            position = 0,
            visibility = PrivateSyllabusVisibility.PRIVATE,
            source = PrivateSyllabusSource.IMPORTED,
            sourceJobId = null,
            sourceHash = null,
            schemaVersion = 1,
            status = PrivateSyllabusStatus.ACTIVE,
            metadata = buildJsonObject { },
            subjects = listOf(
                br.com.estudario.data.remote.PrivateSyllabusSubject(
                    remoteSubjectId = "subject-2",
                    externalId = "subject-ext-2",
                    name = "Português",
                    position = 0,
                    suggestedPriority = br.com.estudario.data.ai.AiPriority.NORMAL,
                    packageVersion = "estudo-v2",
                    schemaVersion = 1,
                    metadata = buildJsonObject { },
                    topics = emptyList(),
                ),
            ),
        )
        val viewModel = MySyllabiViewModel(
            localSyllabi = MutableStateFlow(emptyList()),
            syncRows = MutableStateFlow(emptyList()),
            library = ScreenFakeLibrary(listOf(remote)),
        )
        compose.setContent { MySyllabiScreen(viewModel) }

        compose.onNodeWithText("Excluir da minha conta").performClick()
        compose.onNodeWithText("A cópia privada será excluída da conta. Qualquer cópia local deste edital será mantida.").assertIsDisplayed()
    }

    private class ScreenFakeLibrary(private val remote: List<PrivateSyllabus> = emptyList()) : MySyllabiLibrary {
        override suspend fun listRemote(): List<PrivateSyllabus> = remote
        override suspend fun download(remoteSyllabusId: String): Long = 1L
        override suspend fun removeFromDevice(localSyllabusId: Long) = Unit
        override suspend fun deleteFromAccount(remoteSyllabusId: String) = Unit
    }
}
