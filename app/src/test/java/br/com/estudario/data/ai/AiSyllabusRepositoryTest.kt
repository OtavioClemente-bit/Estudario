package br.com.estudario.data.ai

import br.com.estudario.data.remote.AiAccessTokenProvider
import java.io.ByteArrayInputStream
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Test

class AiSyllabusRepositoryTest {
    @Test
    fun processTimeoutKeepsJobMetadataForLaterRecovery() = runTest {
        val api = FakeAiApiClient()
        api.awaitFailure = AiProcessTimeoutException("job-1")
        val store = InMemoryAiJobRequestStore()
        val repository = repository(api, store)

        val failure = runCatching { repository.start("content://edital", "edital.pdf") }
        assertTrue(failure.exceptionOrNull() is AiProcessTimeoutException)

        val saved = store.values.values.single()
        assertEquals("job-1", saved.jobId)
        assertEquals(AiJobStatus.PROCESSING.name, saved.status)

        api.awaitFailure = null
        api.nextJob = job(AiJobStatus.SUCCEEDED)
        val recovered = repository.recover(saved.requestId)

        assertEquals(AiJobStatus.SUCCEEDED, recovered.status)
        assertEquals("job-1", api.awaitedJobIds.last())
        assertEquals(2, api.createCalls)
    }

    @Test
    fun successfulJobIsRecoveredByPersistedJobIdWithoutCreatingAnotherJob() = runTest {
        val api = FakeAiApiClient(nextJob = job(AiJobStatus.SUCCEEDED))
        val store = InMemoryAiJobRequestStore()
        val repository = repository(api, store)

        val result = repository.start("content://edital", "edital.pdf")

        assertEquals(AiJobStatus.SUCCEEDED, result.status)
        assertEquals("job-1", api.awaitedJobIds.single())
        assertEquals(2, api.createCalls)

        api.nextJob = job(AiJobStatus.SUCCEEDED)
        repository.recover(store.values.values.single().requestId)

        assertEquals(2, api.createCalls)
    }

    @Test
    fun retryingFailedJobUsesANewIdempotencyKey() = runTest {
        val api = FakeAiApiClient(nextJob = job(AiJobStatus.FAILED))
        val store = InMemoryAiJobRequestStore()
        val repository = repository(api, store)

        val failed = repository.start("content://edital", "edital.pdf")
        val firstKey = store.values.values.single().idempotencyKey

        api.nextJob = job(AiJobStatus.SUCCEEDED, id = "job-2")
        val retried = repository.retryFailed(store.values.values.single().requestId)

        assertEquals(AiJobStatus.SUCCEEDED, retried.status)
        assertNotEquals(firstKey, api.idempotencyKeys.last())
        assertEquals(4, api.createCalls)
        assertEquals(AiJobStatus.FAILED, failed.status)
    }

    @Test
    fun absentAuthFailsBeforeReadingOrCallingApi() = runTest {
        val api = FakeAiApiClient()
        val provider = CountingPdfSourceProvider()
        val repository = DefaultAiSyllabusRepository(
            api = api,
            sourceReader = PdfSourceReader(provider),
            requestStore = InMemoryAiJobRequestStore(),
            accessTokenProvider = AiAccessTokenProvider { null },
        )

        val failure = runCatching { repository.start("content://edital", "edital.pdf") }
        assertTrue(failure.exceptionOrNull() is AiAuthenticationRequiredException)

        assertEquals(0, provider.opens)
        assertEquals(0, api.createCalls)
    }

    @Test
    fun recoveryAfterUploadTimeoutResumesUploadAndProcessingWithSameJob() = runTest {
        val api = FakeAiApiClient()
        api.uploadFailure = true
        val store = InMemoryAiJobRequestStore()
        val repository = repository(api, store)

        val failure = runCatching { repository.start("content://edital", "edital.pdf") }
        assertTrue(failure.isFailure)
        val saved = store.values.values.single()
        assertEquals("job-1", saved.jobId)

        api.uploadFailure = false
        api.nextJob = job(AiJobStatus.SUCCEEDED)
        val recovered = repository.recover(saved.requestId)

        assertEquals(AiJobStatus.SUCCEEDED, recovered.status)
        assertEquals(2, api.uploadCalls)
        assertEquals(listOf("job-1"), api.processedJobIds)
    }

    private fun repository(api: FakeAiApiClient, store: InMemoryAiJobRequestStore): DefaultAiSyllabusRepository =
        DefaultAiSyllabusRepository(
            api = api,
            sourceReader = PdfSourceReader(CountingPdfSourceProvider()),
            requestStore = store,
            accessTokenProvider = AiAccessTokenProvider { "supabase-jwt" },
        )

    private fun job(status: AiJobStatus, id: String = "job-1"): AiJob = AiJob(
        jobId = id,
        feature = AiFeature.SYLLABUS_GENERATION,
        status = status,
        schemaVersion = null,
        promptVersion = null,
        modelVersion = null,
        proposal = null,
        warnings = emptyList(),
        errorCode = null,
        errorMessage = null,
        createdAt = "2026-09-24T10:00:00Z",
        updatedAt = "2026-09-24T10:00:01Z",
        finishedAt = null,
        providerExecutionStartedAt = null,
    )
}

private class CountingPdfSourceProvider : PdfSourceProvider {
    var opens = 0
    override fun open(uri: String): PdfSourceInput {
        opens += 1
        return PdfSourceInput("application/pdf", "edital.pdf", ByteArrayInputStream("%PDF-test".toByteArray()))
    }
}

private class InMemoryAiJobRequestStore : AiJobRequestStore {
    val values = linkedMapOf<String, PersistedAiJobRequest>()

    override suspend fun save(value: PersistedAiJobRequest) {
        values[value.requestId] = value
    }

    override suspend fun get(requestId: String): PersistedAiJobRequest? = values[requestId]

    override suspend fun list(): List<PersistedAiJobRequest> = values.values.toList()
}

private class FakeAiApiClient(
    var nextJob: AiJob = fakeJob(AiJobStatus.PROCESSING),
) : AiApiClient {
    var awaitFailure: Throwable? = null
    var uploadFailure = false
    var createCalls = 0
    var uploadCalls = 0
    private var existingJobId: String? = null
    val idempotencyKeys = mutableListOf<String>()
    val awaitedJobIds = mutableListOf<String>()
    val processedJobIds = mutableListOf<String>()

    override suspend fun createOrGetJob(idempotencyKey: String, source: AiSourceMetadata, sourceReady: Boolean): AiCreateJob {
        createCalls += 1
        idempotencyKeys += idempotencyKey
        val jobId = existingJobId ?: "job-${createCalls}".also { existingJobId = it }
        return AiCreateJob(
            jobId = jobId,
            status = AiJobStatus.RESERVED,
            uploadTarget = AiUploadTarget("user/$jobId.pdf", null),
            sourceBound = sourceReady,
        )
    }

    override suspend fun uploadSource(target: AiUploadTarget, source: PdfSource) {
        uploadCalls += 1
        if (uploadFailure) throw AiApiException("NETWORK_UNAVAILABLE", 503)
    }

    override suspend fun processJob(jobId: String): AiJobStatus {
        processedJobIds += jobId
        return AiJobStatus.PROCESSING
    }

    override suspend fun getJob(jobId: String): AiJob = nextJob.copy(jobId = jobId)

    override suspend fun awaitJob(
        jobId: String,
        policy: AiPollingPolicy,
        sleeper: suspend (Long) -> Unit,
        clockMillis: () -> Long,
    ): AiJob {
        awaitedJobIds += jobId
        awaitFailure?.let { throw it }
        return nextJob.copy(jobId = jobId)
    }
}

private fun fakeJob(status: AiJobStatus, id: String = "job-1"): AiJob = AiJob(
    jobId = id,
    feature = AiFeature.SYLLABUS_GENERATION,
    status = status,
    schemaVersion = null,
    promptVersion = null,
    modelVersion = null,
    proposal = null,
    warnings = emptyList(),
    errorCode = null,
    errorMessage = null,
    createdAt = "2026-09-24T10:00:00Z",
    updatedAt = "2026-09-24T10:00:01Z",
    finishedAt = null,
    providerExecutionStartedAt = null,
)
