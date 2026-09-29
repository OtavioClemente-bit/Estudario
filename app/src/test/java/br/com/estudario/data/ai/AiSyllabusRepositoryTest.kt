package br.com.estudario.data.ai

import br.com.estudario.data.remote.AiAccessTokenProvider
import java.io.ByteArrayInputStream
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class AiSyllabusRepositoryTest {
    @Test
    fun requestContextIsPersistedBeforeAnyRemoteJobCall() = runTest {
        val api = FakeAiApiClient()
        val store = InMemoryAiJobRequestStore()
        val repository = repository(api, store, userId = "user-a")
        val callbacks = mutableListOf<PersistedAiJobRequest>()

        repository.start(
            uri = "content://edital",
            fileName = "edital.pdf",
            targetSyllabusId = 73L,
            targetTitle = "TRT-3",
        ) { request ->
            callbacks += request
            assertEquals("user-a", request.ownerUserId)
            assertEquals(73L, request.targetSyllabusId)
            assertEquals("TRT-3", request.targetTitle)
            assertEquals("SYLLABUS_GENERATION", request.feature)
            assertEquals(request, store.values[request.requestId])
            if (callbacks.size == 1) assertEquals(0, api.createCalls)
        }

        assertEquals(store.values.values.single().requestId, callbacks.last().requestId)
        assertEquals("job-1", callbacks.first { it.jobId != null }.jobId)
        assertTrue(callbacks.first().createdAtEpochMillis > 0L)
    }

    @Test
    fun recoveryNeverTouchesRequestsOwnedByAnotherAuthenticatedUser() = runTest {
        val api = FakeAiApiClient()
        val store = InMemoryAiJobRequestStore()
        val foreign = PersistedAiJobRequest(
            requestId = "request-user-a",
            idempotencyKey = "idempotency-user-a",
            sourceUri = "content://private-a",
            fileName = "edital.pdf",
            mimeType = "application/pdf",
            sourceHash = "a".repeat(64),
            sourceBytes = 123L,
            jobId = "job-user-a",
            status = AiJobStatus.PROCESSING.name,
            ownerUserId = "user-a",
            targetSyllabusId = 73L,
            targetTitle = "TRT-3",
        )
        store.save(foreign)
        val repository = repository(api, store, userId = "user-b")

        val recovered = repository.recoverPendingJobs()

        assertTrue(recovered.isEmpty())
        assertTrue(api.awaitedJobIds.isEmpty())
        assertEquals(foreign, store.get(foreign.requestId))
    }

    @Test
    fun legacyRequestIsAdoptedOnlyAfterOwnerScopedGetAndDoesNotNeedLocalPdf() = runTest {
        val api = FakeAiApiClient(nextJob = fakeJob(AiJobStatus.SUCCEEDED, "legacy-job"))
        val store = InMemoryAiJobRequestStore()
        val legacy = PersistedAiJobRequest(
            requestId = "legacy-request",
            idempotencyKey = "legacy-idempotency",
            sourceUri = "content://expired-permission",
            fileName = "edital.pdf",
            mimeType = "application/pdf",
            sourceHash = "a".repeat(64),
            sourceBytes = 123L,
            jobId = "legacy-job",
            status = AiJobStatus.PROCESSING.name,
        )
        store.save(legacy)
        val repository = repository(api, store, userId = "user-current")
        val updates = mutableListOf<PersistedAiJobRequest>()

        repository.recoverLegacyPending(73L, "TRT-3", updates::add)

        val adopted = store.get(legacy.requestId)!!
        assertEquals("user-current", adopted.ownerUserId)
        assertEquals(73L, adopted.targetSyllabusId)
        assertEquals("TRT-3", adopted.targetTitle)
        assertEquals("legacy-job", adopted.jobId)
        assertEquals(1, api.getJobCalls)
        assertTrue(api.awaitedJobIds.isEmpty())
        assertEquals(0, api.createCalls)
        assertTrue(updates.any { it.ownerUserId == "user-current" })
    }

    @Test
    fun legacyRequestWithoutKnownJobCannotBeAdopted() = runTest {
        val api = FakeAiApiClient()
        val store = InMemoryAiJobRequestStore()
        val legacy = PersistedAiJobRequest(
            requestId = "legacy-request",
            idempotencyKey = "legacy-idempotency",
            sourceUri = "content://private-old-account",
            fileName = "edital.pdf",
            mimeType = "application/pdf",
            sourceHash = "a".repeat(64),
            sourceBytes = 123L,
        )
        store.save(legacy)
        val repository = repository(api, store, userId = "user-current")

        val error = runCatching { repository.recoverLegacyPending(73L, "TRT-3") }.exceptionOrNull()

        assertTrue(error is AiLegacyRecoveryBlockedException)
        assertEquals(legacy, store.get(legacy.requestId))
        assertEquals(0, api.getJobCalls)
        assertEquals(0, api.createCalls)
    }

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
    fun selectedTitleIsSentForUploadBindAndPreservedOnFailedJobRetry() = runTest {
        val api = StatefulFakeAiApiClient(nextJob = job(AiJobStatus.FAILED))
        val store = InMemoryAiJobRequestStore()
        val repository = repository(api, store, userId = "user-a")
        val title = "TRT-3 - Técnico Judiciário - TI"

        repository.start("content://edital", "edital.pdf", targetSyllabusId = 73L, targetTitle = title)
        val failedRequest = store.values.values.single()
        assertEquals(title, failedRequest.targetTitle)
        api.nextJob = job(AiJobStatus.SUCCEEDED, id = "job-2")
        repository.retryFailed(failedRequest.requestId)

        assertEquals(listOf(title, title, title, title), api.targetTitles)
        assertEquals(title, store.values.getValue(failedRequest.requestId).targetTitle)
    }

    @Test
    fun legacyReservedRequestKeepsItsPreV2FingerprintShape() = runTest {
        val api = FakeAiApiClient(nextJob = job(AiJobStatus.SUCCEEDED))
        val store = InMemoryAiJobRequestStore()
        val snapshots = InMemoryPdfSourceSnapshotStore()
        val legacyPath = "private/legacy-edital.pdf"
        snapshots.replace(legacyPath, PdfSource(legacyPath, "edital.pdf", "application/pdf", ByteArray(123), "a".repeat(64)))
        val repository = repository(api, store, userId = "user-a", snapshots = snapshots)
        val legacyReserved = PersistedAiJobRequest(
            requestId = "old-request",
            idempotencyKey = "old-idempotency-key",
            sourceUri = "content://edital",
            fileName = "edital.pdf",
            mimeType = "application/pdf",
            sourceHash = "a".repeat(64),
            sourceBytes = 123L,
            sourcePath = legacyPath,
            jobId = "old-job",
            uploadPath = "user/old-job.pdf",
            sourceUploaded = true,
            status = AiJobStatus.RESERVED.name,
            ownerUserId = "user-a",
            targetSyllabusId = 73L,
            targetTitle = "TRT-3 - Técnico Judiciário - TI",
        )
        store.save(legacyReserved)

        repository.recover(legacyReserved.requestId)

        assertEquals(listOf(null), api.targetTitles)
        assertEquals(1, api.createCalls)
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
    fun existingProcessingJobRecoversWithoutReopeningOrRevalidatingLocalPdf() = runTest {
        val provider = MutablePdfSourceProvider()
        val snapshots = InMemoryPdfSourceSnapshotStore()
        val api = StatefulFakeAiApiClient()
        val store = InMemoryAiJobRequestStore()
        val repository = repository(api, store, provider, snapshots)

        repository.start("content://edital", "edital.pdf")
        val saved = store.values.values.single()
        assertEquals(1, provider.opens)

        snapshots.replace(saved.sourcePath!!, PdfSource(saved.sourcePath, saved.fileName, "application/pdf", "%PDF-changed".toByteArray(), "f".repeat(64)))
        api.nextJob = job(AiJobStatus.SUCCEEDED, api.uniqueJobIds.single())

        val recovered = repository.recover(saved.requestId)

        assertEquals(AiJobStatus.SUCCEEDED, recovered.status)
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
        userId: String = "user-test",
    ): DefaultAiSyllabusRepository =
        DefaultAiSyllabusRepository(
            api = api,
            sourceReader = PdfSourceReader(provider),
            requestStore = store,
            accessTokenProvider = AiAccessTokenProvider { "supabase-jwt" },
            sourceSnapshots = snapshots,
            userIdProvider = { userId },
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
    var getJobCalls = 0
    private var existingJobId: String? = null
    val idempotencyKeys = mutableListOf<String>()
    val targetTitles = mutableListOf<String?>()
    val awaitedJobIds = mutableListOf<String>()
    val processedJobIds = mutableListOf<String>()

    override suspend fun createOrGetJob(idempotencyKey: String, source: AiSourceMetadata, sourceReady: Boolean, targetTitle: String?): AiCreateJob {
        createCalls += 1
        idempotencyKeys += idempotencyKey
        targetTitles += targetTitle
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

    override suspend fun getJob(jobId: String, timeoutMillis: Long?): AiJob {
        getJobCalls += 1
        return nextJob.copy(jobId = jobId)
    }

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
    val targetTitles = mutableListOf<String?>()
    val uniqueJobIds = linkedSetOf<String>()
    private val jobsByFingerprint = linkedMapOf<Triple<String, String, String?>, String>()
    private val hashesByKey = linkedMapOf<String, String>()
    private var nextJobNumber = 1

    override suspend fun createOrGetJob(idempotencyKey: String, source: AiSourceMetadata, sourceReady: Boolean, targetTitle: String?): AiCreateJob {
        hashesByKey[idempotencyKey]?.let { previousHash ->
            check(previousHash == source.sourceHash) { "idempotency key reused with different source bytes" }
        } ?: run { hashesByKey[idempotencyKey] = source.sourceHash }
        val fingerprint = Triple(idempotencyKey, source.sourceHash, targetTitle)
        val jobId = jobsByFingerprint.getOrPut(fingerprint) { "stateful-job-${nextJobNumber++}" }
        idempotencyKeys += idempotencyKey
        targetTitles += targetTitle
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
