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
            sourceSnapshots = InMemoryPdfSourceSnapshotStore(),
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

    @Test
    fun recoveryUsesPrivateSnapshotAndRejectsChangedBytesBeforeReusingKey() = runTest {
        val provider = MutablePdfSourceProvider()
        val snapshots = InMemoryPdfSourceSnapshotStore()
        val api = StatefulFakeAiApiClient()
        val store = InMemoryAiJobRequestStore()
        val repository = repository(api, store, provider, snapshots)

        repository.start("content://edital", "edital.pdf")
        val saved = store.values.values.single()
        assertEquals(1, provider.opens)

        snapshots.replace(saved.sourcePath!!, PdfSource(saved.sourcePath, saved.fileName, "application/pdf", "%PDF-changed".toByteArray(), "f".repeat(64)))
        assertTrue(runCatching { repository.recover(saved.requestId) }.exceptionOrNull() is PdfSourceChangedException)
        assertEquals(1, provider.opens)
        assertEquals(1, api.uniqueJobIds.size)
        assertEquals(1, api.idempotencyKeys.distinct().size)
    }

    @Test
    fun statefulFakeReusesJobForSameKeyFingerprintAndCreatesNewJobForFailedRetry() = runTest {
        val api = StatefulFakeAiApiClient(nextJob = job(AiJobStatus.FAILED))
        val store = InMemoryAiJobRequestStore()
        val snapshots = InMemoryPdfSourceSnapshotStore()
        val repository = repository(api, store, CountingPdfSourceProvider(), snapshots)

        val failed = repository.start("content://edital", "edital.pdf")
        val failedRequest = store.values.values.single()
        val firstJob = failedRequest.jobId
        repository.recover(failedRequest.requestId)
        assertEquals(firstJob, store.values.values.single().jobId)

        api.nextJob = job(AiJobStatus.SUCCEEDED)
        repository.retryFailed(failedRequest.requestId)
        assertEquals(2, api.uniqueJobIds.size)
        assertEquals(2, api.idempotencyKeys.distinct().size)
        assertEquals(AiJobStatus.FAILED, failed.status)
    }

    @Test
    fun retryAfterTerminalRetryPartiallyPersistsRecoversTheNewReservedJob() = runTest {
        val api = StatefulFakeAiApiClient(nextJob = job(AiJobStatus.CANCELLED))
        val store = InMemoryAiJobRequestStore()
        val repository = repository(api, store, snapshots = InMemoryPdfSourceSnapshotStore())
        repository.start("content://edital", "edital.pdf")
        val oldRequest = store.values.values.single()
        val oldKey = oldRequest.idempotencyKey

        api.nextJob = job(AiJobStatus.SUCCEEDED)
        api.processFailure = AiApiException("TEMPORARY", 503)
        assertTrue(runCatching { repository.resumeOrRetry(oldRequest.requestId) }.isFailure)
        val partiallyRetried = store.values.getValue(oldRequest.requestId)
        assertEquals(AiJobStatus.RESERVED.name, partiallyRetried.status)
        assertNotEquals(oldKey, partiallyRetried.idempotencyKey)
        val newJobId = partiallyRetried.jobId
        val retryKey = partiallyRetried.idempotencyKey

        api.processFailure = null
        val recovered = repository.resumeOrRetry(oldRequest.requestId)

        assertEquals(AiJobStatus.SUCCEEDED, recovered.status)
        assertEquals(oldRequest.requestId, store.values.values.single().requestId)
        assertEquals(newJobId, store.values.getValue(oldRequest.requestId).jobId)
        assertEquals(retryKey, store.values.getValue(oldRequest.requestId).idempotencyKey)
        assertEquals(2, api.uniqueJobIds.size)
        assertEquals(2, api.idempotencyKeys.distinct().size)
    }

    @Test
    fun resumeOrRetryCreatesNewAttemptOnlyForCurrentRetryableTerminalStatus() = runTest {
        listOf(AiJobStatus.FAILED, AiJobStatus.EXPIRED, AiJobStatus.CANCELLED).forEach { terminal ->
            val api = StatefulFakeAiApiClient(nextJob = job(terminal))
            val store = InMemoryAiJobRequestStore()
            val repository = repository(api, store)
            repository.start("content://edital", "edital.pdf")
            val requestId = store.values.values.single().requestId
            api.nextJob = job(AiJobStatus.SUCCEEDED)

            repository.resumeOrRetry(requestId)

            assertEquals("$terminal should create one fresh job", 2, api.uniqueJobIds.size)
            assertEquals("$terminal should use one fresh idempotency key", 2, api.idempotencyKeys.distinct().size)
            assertEquals(requestId, store.values.getValue(requestId).requestId)
        }
    }

    @Test
    fun resumeOrRetryRecoversReservedProcessingAndSucceededJobsWithoutNewAttempt() = runTest {
        listOf<AiJobStatus?>(AiJobStatus.RESERVED, AiJobStatus.PROCESSING, AiJobStatus.SUCCEEDED, null).forEach { status ->
            val api = StatefulFakeAiApiClient(nextJob = job(status ?: AiJobStatus.PROCESSING))
            val store = InMemoryAiJobRequestStore()
            val repository = repository(api, store)
            repository.start("content://edital", "edital.pdf")
            val saved = store.values.values.single()
            if (status == AiJobStatus.RESERVED || status == null) {
                store.save(saved.copy(status = status?.name))
            }
            api.nextJob = job(AiJobStatus.SUCCEEDED)

            repository.resumeOrRetry(saved.requestId)

            assertEquals("$status must reuse the existing job", 1, api.uniqueJobIds.size)
            assertEquals("$status must reuse the existing idempotency key", 1, api.idempotencyKeys.distinct().size)
        }
    }

    private fun repository(
        api: AiApiClient,
        store: InMemoryAiJobRequestStore,
        provider: PdfSourceProvider = CountingPdfSourceProvider(),
        snapshots: PdfSourceSnapshotStore = InMemoryPdfSourceSnapshotStore(),
    ): DefaultAiSyllabusRepository =
        DefaultAiSyllabusRepository(
            api = api,
            sourceReader = PdfSourceReader(provider),
            requestStore = store,
            accessTokenProvider = AiAccessTokenProvider { "supabase-jwt" },
            sourceSnapshots = snapshots,
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

private class MutablePdfSourceProvider : PdfSourceProvider {
    var opens = 0
    var bytes = "%PDF-original".toByteArray()
    override fun open(uri: String): PdfSourceInput {
        opens += 1
        return PdfSourceInput("application/pdf", "edital.pdf", ByteArrayInputStream(bytes))
    }
}

private class InMemoryPdfSourceSnapshotStore : PdfSourceSnapshotStore {
    private val values = linkedMapOf<String, PdfSource>()

    override fun save(source: PdfSource): String {
        val path = "private/${source.sha256}.pdf"
        values[path] = source.copy(bytes = source.bytes.copyOf())
        return path
    }

    override fun read(path: String, fileName: String): PdfSource = values[path]?.copy(fileName = fileName) ?: error("snapshot missing")

    fun replace(path: String, source: PdfSource) {
        values[path] = source
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

    override suspend fun getJob(jobId: String, timeoutMillis: Long?): AiJob = nextJob.copy(jobId = jobId)

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

private class StatefulFakeAiApiClient(
    var nextJob: AiJob = fakeJob(AiJobStatus.PROCESSING),
) : AiApiClient {
    var processFailure: Throwable? = null
    val idempotencyKeys = mutableListOf<String>()
    val uniqueJobIds = linkedSetOf<String>()
    private val jobsByFingerprint = linkedMapOf<Pair<String, String>, String>()
    private val hashesByKey = linkedMapOf<String, String>()
    private var nextJobNumber = 1

    override suspend fun createOrGetJob(idempotencyKey: String, source: AiSourceMetadata, sourceReady: Boolean): AiCreateJob {
        hashesByKey[idempotencyKey]?.let { previousHash ->
            check(previousHash == source.sourceHash) { "idempotency key reused with different source bytes" }
        } ?: run { hashesByKey[idempotencyKey] = source.sourceHash }
        val fingerprint = idempotencyKey to source.sourceHash
        val jobId = jobsByFingerprint.getOrPut(fingerprint) { "stateful-job-${nextJobNumber++}" }
        idempotencyKeys += idempotencyKey
        uniqueJobIds += jobId
        return AiCreateJob(jobId, AiJobStatus.RESERVED, AiUploadTarget("user/$jobId.pdf", null), sourceReady)
    }

    override suspend fun uploadSource(target: AiUploadTarget, source: PdfSource) = Unit
    override suspend fun processJob(jobId: String): AiJobStatus {
        processFailure?.let { throw it }
        return AiJobStatus.PROCESSING
    }
    override suspend fun getJob(jobId: String, timeoutMillis: Long?): AiJob = nextJob.copy(jobId = jobId)
    override suspend fun awaitJob(jobId: String, policy: AiPollingPolicy, sleeper: suspend (Long) -> Unit, clockMillis: () -> Long): AiJob = nextJob.copy(jobId = jobId)
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
