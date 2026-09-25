package br.com.estudario.ui.ai

import android.app.Application
import android.net.Uri
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ApplicationProvider
import br.com.estudario.data.ai.AiFeature
import br.com.estudario.data.ai.AiJob
import br.com.estudario.data.ai.AiJobStatus
import br.com.estudario.data.local.RemoteSyllabusSyncState
import br.com.estudario.data.syllabus.ApplyResult
import br.com.estudario.ui.setup.AiPdfUriPermission
import br.com.estudario.ui.theme.EstudarioTheme
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class AiReviewEntryPointTest {
    @get:Rule val compose = createComposeRule()

    @Test
    fun productionEntryPointPickerCallbackPersistsPermissionBeforeStartingJob() {
        val picker = CapturingPdfPicker()
        val permission = RecordingPermission()
        val jobs = RecordingJobs()
        val viewModel = createViewModel(jobs)
        val selectedUri = Uri.parse("content://provider/edital.pdf")

        compose.setContent {
            EstudarioTheme(false) {
                AiReviewEntryPoint(
                    target = AiReviewTarget(42L, "TRT-3"),
                    onClose = {},
                    reviewViewModel = viewModel,
                    pdfPicker = picker,
                    pdfPermission = permission,
                )
            }
        }
        compose.waitUntil { viewModel.state.value.access.kind == AiReviewAccessKind.READY }
        compose.onNodeWithText("Selecionar PDF do edital").performClick()
        picker.resultCallback!!.invoke(selectedUri)
        compose.waitUntil { jobs.startedUris.size == 1 }

        assertEquals(listOf(selectedUri), permission.persistedUris)
        assertEquals(listOf(selectedUri.toString()), jobs.startedUris)
    }

    @Test
    fun productionEntryPointDoesNotStartWhenUriPermissionFails() {
        val picker = CapturingPdfPicker()
        val permission = RecordingPermission(failure = IllegalStateException("provider denied"))
        val jobs = RecordingJobs()
        val viewModel = createViewModel(jobs)

        compose.setContent {
            EstudarioTheme(false) {
                AiReviewEntryPoint(
                    target = AiReviewTarget(42L, "TRT-3"),
                    onClose = {},
                    reviewViewModel = viewModel,
                    pdfPicker = picker,
                    pdfPermission = permission,
                )
            }
        }
        compose.waitUntil { viewModel.state.value.access.kind == AiReviewAccessKind.READY }
        compose.onNodeWithText("Selecionar PDF do edital").performClick()
        picker.resultCallback!!.invoke(Uri.parse("content://provider/edital.pdf"))
        compose.waitUntil { permission.persistedUris.size == 1 }

        assertTrue(jobs.startedUris.isEmpty())
        compose.onNodeWithText("Não foi possível manter acesso ao PDF selecionado.").assertExists()
    }

    private fun createViewModel(jobs: RecordingJobs): AiReviewViewModel = AiReviewViewModel(
        application = ApplicationProvider.getApplicationContext<Application>(),
        targetId = 42L,
        targetTitle = "TRT-3",
        jobs = jobs,
        applier = AiReviewApplier { _, _, _, _ ->
            ApplyResult(
                localSyllabusId = 7L,
                sourceJobId = "job-entrypoint",
                packageJson = "{}",
                payloadHash = "hash",
                outboxId = 7L,
                remoteSyllabusId = null,
                state = RemoteSyllabusSyncState.PENDING,
                created = true,
                alreadyApplied = false,
            )
        },
        sessions = InMemorySessions(),
        accessGateway = object : AiReviewAccessGateway {
            override suspend fun check() = AiReviewAccessResult(true, true)
        },
    )

    private class CapturingPdfPicker : AiReviewPdfPicker {
        var resultCallback: ((Uri?) -> Unit)? = null
        override fun launch(onResult: (Uri?) -> Unit) { resultCallback = onResult }
    }

    private class RecordingPermission(private val failure: Throwable? = null) : AiPdfUriPermission {
        val persistedUris = mutableListOf<Uri>()
        override fun persist(uri: Uri) {
            persistedUris += uri
            failure?.let { throw it }
        }
    }

    private class InMemorySessions : AiReviewSessionStore {
        private var current: AiReviewPersistedSession? = null
        override suspend fun load(targetId: Long): AiReviewPersistedSession? = current?.takeIf { it.targetId == targetId }
        override suspend fun loadLatest(): AiReviewPersistedSession? = current
        override suspend fun save(session: AiReviewPersistedSession) { current = session }
        override suspend fun clear(targetId: Long) { current = null }
    }

    private class RecordingJobs : AiReviewJobs {
        val startedUris = mutableListOf<String>()
        override suspend fun start(targetId: Long, uri: String, fileName: String?): AiReviewStarted {
            startedUris += uri
            return AiReviewStarted(
                job = AiJob(
                    jobId = "job-entrypoint",
                    feature = AiFeature.SYLLABUS_GENERATION,
                    status = AiJobStatus.PROCESSING,
                    schemaVersion = null,
                    promptVersion = null,
                    modelVersion = null,
                    proposal = null,
                    warnings = emptyList(),
                    errorCode = null,
                    errorMessage = null,
                    createdAt = "2026-09-24T10:00:00Z",
                    updatedAt = "2026-09-24T10:00:00Z",
                    finishedAt = null,
                    providerExecutionStartedAt = null,
                ),
                identity = AiReviewRequestIdentity("request-entrypoint", "job-entrypoint", "idem-entrypoint"),
            )
        }

        override suspend fun recover(requestId: String): AiReviewStarted = error("not expected")
        override suspend fun retryFailed(requestId: String): AiReviewStarted = error("not expected")
        override suspend fun recoverPending(): List<AiReviewStarted> = emptyList()
        override suspend fun identityForJob(jobId: String): AiReviewRequestIdentity? = null
    }
}
