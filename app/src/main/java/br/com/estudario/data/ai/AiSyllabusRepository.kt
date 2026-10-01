package br.com.estudario.data.ai

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import br.com.estudario.data.remote.AiAccessTokenProvider
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.flow.first
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
data class PersistedAiJobRequest(
    val requestId: String,
    val idempotencyKey: String,
    val sourceUri: String,
    val fileName: String,
    val mimeType: String,
    val sourceHash: String,
    val sourceBytes: Long,
    val sourcePath: String? = null,
    val jobId: String? = null,
    val uploadPath: String? = null,
    val sourceUploaded: Boolean = false,
    val status: String? = null,
    val ownerUserId: String? = null,
    val feature: String = "SYLLABUS_GENERATION",
    val createdAtEpochMillis: Long = System.currentTimeMillis(),
    val targetSyllabusId: Long? = null,
    val targetTitle: String? = null,
    val updatedAtEpochMillis: Long = System.currentTimeMillis(),
    val preferences: AiSyllabusPreferences? = null,
    /** A definite quota refusal before we received a job ID, not an unknown network outcome. */
    val creationRejectionCode: String? = null,
)

interface AiJobRequestStore {
    suspend fun save(value: PersistedAiJobRequest)

    suspend fun get(requestId: String): PersistedAiJobRequest?

    suspend fun list(): List<PersistedAiJobRequest>
}

interface AiJobRecoveryRepository {
    suspend fun recoverPendingJobs(): List<AiJob>
}

private val Context.aiJobRequestDataStore by preferencesDataStore(name = "ai_job_requests")

class DataStoreAiJobRequestStore(
    private val dataStore: DataStore<Preferences>,
) : AiJobRequestStore {
    constructor(context: Context) : this(context.aiJobRequestDataStore)

    private val requestsKey = stringPreferencesKey("requests")
    private val json = Json { ignoreUnknownKeys = true; encodeDefaults = true }

    override suspend fun save(value: PersistedAiJobRequest) {
        dataStore.edit { preferences ->
            val values = decode(preferences[requestsKey]).associateBy { it.requestId }.toMutableMap()
            values[value.requestId] = value
            preferences[requestsKey] = json.encodeToString(values.values.toList())
        }
    }

    override suspend fun get(requestId: String): PersistedAiJobRequest? = list().firstOrNull { it.requestId == requestId }

    override suspend fun list(): List<PersistedAiJobRequest> = decode(dataStore.data.first()[requestsKey])

    private fun decode(raw: String?): List<PersistedAiJobRequest> = raw?.let {
        runCatching { json.decodeFromString<List<PersistedAiJobRequest>>(it) }.getOrDefault(emptyList())
    } ?: emptyList()
}

class DefaultAiSyllabusRepository(
    private val api: AiApiClient,
    private val sourceReader: PdfSourceReader,
    private val requestStore: AiJobRequestStore,
    private val accessTokenProvider: AiAccessTokenProvider,
    private val pollingPolicy: AiPollingPolicy = AiPollingPolicy(),
    private val sourceSnapshots: PdfSourceSnapshotStore,
    private val userIdProvider: () -> String? = { null },
    private val scheduleRecovery: () -> Unit = {},
) : AiJobRecoveryRepository {
    /** Local IO only: no authentication, job, upload or quota reservation. */
    suspend fun prepare(
        uri: String,
        fileName: String? = null,
        snapshotPath: String? = null,
        expectedHash: String? = null,
        attemptId: String? = null,
    ): PreparedSyllabusSource = withContext(Dispatchers.IO) {
        val source = if (snapshotPath == null) sourceReader.read(uri, fileName)
            else sourceSnapshots.read(snapshotPath, fileName ?: "edital.pdf")
        if (expectedHash != null && source.sha256 != expectedHash) throw PdfSourceChangedException(expectedHash, source.sha256)
        val path = snapshotPath ?: sourceSnapshots.save(source)
        val pages = runCatching { EditalPdfText.pages(source.bytes) }.getOrDefault(emptyList())
        PreparedSyllabusSource(uri, source.fileName, path, source.sha256, source.bytes.size.toLong(), pages, attemptId ?: UUID.randomUUID().toString())
    }

    /** Called by the explicit confirmation action, with the exact bytes checked locally. */
    suspend fun startPrepared(
        prepared: PreparedSyllabusSource,
        targetSyllabusId: Long,
        targetTitle: String,
        preferences: AiSyllabusPreferences,
        onRequestPersisted: suspend (PersistedAiJobRequest) -> Unit = {},
    ): AiJob {
        require(preferences.isComplete && prepared.preflight(preferences).canGenerate)
        val source = withContext(Dispatchers.IO) { sourceSnapshots.read(prepared.snapshotPath, prepared.fileName) }
        if (source.sha256 != prepared.sha256 || source.bytes.size.toLong() != prepared.sizeBytes) throw PdfSourceChangedException(prepared.sha256, source.sha256)
        return startSource(source.copy(uri = prepared.uri), prepared.snapshotPath, targetSyllabusId, targetTitle, preferences, onRequestPersisted, prepared.attemptId)
    }

    suspend fun start(
        uri: String,
        fileName: String? = null,
        targetSyllabusId: Long? = null,
        targetTitle: String? = null,
        preferences: AiSyllabusPreferences? = null,
        onRequestPersisted: suspend (PersistedAiJobRequest) -> Unit = {},
    ): AiJob {
        requireAuthenticatedUser()
        val source = withContext(Dispatchers.IO) { sourceReader.read(uri, fileName) }
        val sourcePath = withContext(Dispatchers.IO) { sourceSnapshots.save(source) }
        return startSource(source, sourcePath, targetSyllabusId, targetTitle, preferences, onRequestPersisted)
    }

    private suspend fun startSource(
        source: PdfSource,
        sourcePath: String,
        targetSyllabusId: Long?,
        targetTitle: String?,
        preferences: AiSyllabusPreferences?,
        onRequestPersisted: suspend (PersistedAiJobRequest) -> Unit,
        confirmedAttemptId: String? = null,
    ): AiJob {
        val ownerUserId = requireAuthenticatedUser()
        val request = PersistedAiJobRequest(
            requestId = confirmedAttemptId ?: UUID.randomUUID().toString(),
            idempotencyKey = confirmedAttemptId ?: UUID.randomUUID().toString(),
            sourceUri = source.uri,
            fileName = source.fileName,
            mimeType = source.mimeType,
            sourceHash = source.sha256,
            sourceBytes = source.bytes.size.toLong(),
            sourcePath = sourcePath,
            ownerUserId = ownerUserId,
            targetSyllabusId = targetSyllabusId,
            targetTitle = targetTitle,
            preferences = preferences,
        )
        return requestLock(request.requestId).withLock {
            if (requestStore.get(request.requestId) != null) return@withLock recoverLocked(request.requestId, onRequestPersisted)
            requestStore.save(request)
            scheduleRecovery()
            onRequestPersisted(request)
            continueRequest(request, source, onRequestPersisted)
        }
    }

    suspend fun recover(
        requestId: String,
        onRequestPersisted: suspend (PersistedAiJobRequest) -> Unit = {},
    ): AiJob = requestLock(requestId).withLock { recoverLocked(requestId, onRequestPersisted) }

    private suspend fun recoverLocked(requestId: String, onRequestPersisted: suspend (PersistedAiJobRequest) -> Unit): AiJob {
        requireAuthenticated()
        val stored = requestStore.get(requestId) ?: throw IllegalArgumentException("AI request not found.")
        requireOwnedByCurrentUser(stored)
        if (stored.jobId == null && stored.creationRejectionCode != null) {
            onRequestPersisted(stored)
            throw AiApiException(stored.creationRejectionCode, 429)
        }
        stored.jobId?.let { jobId ->
            val visible = api.getJob(jobId)
            require(visible.jobId == jobId && visible.feature == AiFeature.SYLLABUS_GENERATION)
            val refreshed = stored.copy(status = visible.status.name, updatedAtEpochMillis = System.currentTimeMillis())
            requestStore.save(refreshed)
            onRequestPersisted(refreshed)
            if (visible.status.name in TERMINAL_STATUSES) return visible
            if (visible.status == AiJobStatus.PROCESSING) return awaitExisting(refreshed, jobId, onRequestPersisted)
        }
        val (request, source) = durableSource(stored)
        return continueRequest(request, source, onRequestPersisted)
    }

    suspend fun retryFailed(
        requestId: String,
        onRequestPersisted: suspend (PersistedAiJobRequest) -> Unit = {},
    ): AiJob = requestLock(requestId).withLock { retryFailedLocked(requestId, onRequestPersisted) }

    private suspend fun retryFailedLocked(requestId: String, onRequestPersisted: suspend (PersistedAiJobRequest) -> Unit): AiJob {
        requireAuthenticated()
        val previous = requestStore.get(requestId) ?: throw IllegalArgumentException("AI request not found.")
        requireOwnedByCurrentUser(previous)
        val previousStatus = previous.status?.let { runCatching { AiJobStatus.valueOf(it) }.getOrNull() }
        require(previousStatus == AiJobStatus.FAILED || previousStatus == AiJobStatus.EXPIRED || previousStatus == AiJobStatus.CANCELLED) {
            "Only a terminal failed AI job can be retried."
        }
        val (durablePrevious, source) = durableSource(previous)
        val retry = previous.copy(
            idempotencyKey = UUID.randomUUID().toString(),
            jobId = null,
            uploadPath = null,
            sourceUploaded = false,
            status = null,
            creationRejectionCode = null,
            sourceHash = source.sha256,
            sourceBytes = source.bytes.size.toLong(),
            sourcePath = durablePrevious.sourcePath,
            updatedAtEpochMillis = System.currentTimeMillis(),
        )
        requestStore.save(retry)
        scheduleRecovery()
        onRequestPersisted(retry)
        return continueRequest(retry, source, onRequestPersisted)
    }

    /** Resumes the latest persisted attempt, retrying only when that attempt is terminal. */
    suspend fun resumeOrRetry(
        requestId: String,
        onRequestPersisted: suspend (PersistedAiJobRequest) -> Unit = {},
    ): AiJob = requestLock(requestId).withLock {
        requireAuthenticated()
        val request = requestStore.get(requestId) ?: throw IllegalArgumentException("AI request not found.")
        requireOwnedByCurrentUser(request)
        // Only an explicit user retry may recheck a previously refused creation. Keep its key.
        if (request.jobId == null && request.creationRejectionCode != null) {
            requestStore.save(request.copy(creationRejectionCode = null))
        }
        val status = request.status?.let { runCatching { AiJobStatus.valueOf(it) }.getOrNull() }
        if (status in RETRYABLE_TERMINAL_STATUSES) {
            retryFailedLocked(requestId, onRequestPersisted)
        } else {
            recoverLocked(requestId, onRequestPersisted)
        }
    }

    /** Adopts one legacy local request only after the current JWT can read its known job ID. */
    suspend fun recoverLegacyPending(
        targetSyllabusId: Long,
        targetTitle: String,
        onRequestPersisted: suspend (PersistedAiJobRequest) -> Unit = {},
    ): AiJob? {
        require(targetSyllabusId > 0L)
        val ownerUserId = requireAuthenticatedUser()
        val legacy = requestStore.list().filter {
            it.ownerUserId == null && it.feature == "SYLLABUS_GENERATION" && it.targetSyllabusId == null
        }
        if (legacy.isEmpty()) return null
        if (legacy.size != 1) throw AiLegacyRecoveryBlockedException()
        val request = legacy.single()
        val jobId = request.jobId ?: throw AiLegacyRecoveryBlockedException()

        // The public owner-scoped GET is the proof that this legacy ID belongs to this JWT.
        val visibleJob = api.getJob(jobId)
        if (visibleJob.jobId != jobId || visibleJob.feature != AiFeature.SYLLABUS_GENERATION) {
            throw AiLegacyRecoveryBlockedException()
        }

        val adopted = request.copy(
            ownerUserId = ownerUserId,
            targetSyllabusId = targetSyllabusId,
            targetTitle = targetTitle,
            status = visibleJob.status.name,
            updatedAtEpochMillis = System.currentTimeMillis(),
        )
        requestStore.save(adopted)
        onRequestPersisted(adopted)
        return if (visibleJob.status == AiJobStatus.RESERVED) recover(request.requestId, onRequestPersisted) else visibleJob
    }

    override suspend fun recoverPendingJobs(): List<AiJob> = buildList {
        val ownerUserId = requireAuthenticatedUser()
        var firstFailure: Throwable? = null
        requestStore.list()
            .filter { it.ownerUserId == ownerUserId }
            .filter { it.jobId != null || it.creationRejectionCode == null }
            .filter { it.status !in setOf(AiJobStatus.SUCCEEDED.name, AiJobStatus.FAILED.name, AiJobStatus.EXPIRED.name, AiJobStatus.CANCELLED.name) }
            .forEach { request ->
                try {
                    add(recover(request.requestId))
                } catch (error: Throwable) {
                    if (error is CancellationException) throw error
                    if (firstFailure == null) firstFailure = error
                }
            }
        firstFailure?.let { throw it }
    }

    private suspend fun durableSource(request: PersistedAiJobRequest): Pair<PersistedAiJobRequest, PdfSource> = withContext(Dispatchers.IO) {
        val path = request.sourcePath
        val (durableRequest, source) = if (path == null) {
            val read = sourceReader.read(request.sourceUri, request.fileName)
            val snapshotPath = sourceSnapshots.save(read)
            val migrated = request.copy(sourcePath = snapshotPath, updatedAtEpochMillis = System.currentTimeMillis())
            requestStore.save(migrated)
            migrated to read
        } else {
            request to sourceSnapshots.read(path, request.fileName)
        }
        if (source.mimeType != request.mimeType || source.bytes.size.toLong() != request.sourceBytes || source.sha256 != request.sourceHash) {
            throw PdfSourceChangedException(request.sourceHash, source.sha256)
        }
        durableRequest to source
    }

    private suspend fun continueRequest(
        request: PersistedAiJobRequest,
        source: PdfSource,
        onRequestPersisted: suspend (PersistedAiJobRequest) -> Unit = {},
    ): AiJob {
        var current = request
        // Ler o texto do PDF leva de um a alguns segundos: nunca na thread da tela.
        val sourceMetadata = kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.Default) { source.toMetadata() }
        val sourceReady = current.sourceUploaded && current.uploadPath != null
        val created = try {
            api.createOrGetJob(
                current.idempotencyKey,
                sourceMetadata.copy(objectPath = current.uploadPath.takeIf { sourceReady }),
                sourceReady = sourceReady,
                preferences = current.preferences,
            )
        } catch (error: AiApiException) {
            if (current.jobId == null && error.code in AI_QUOTA_REJECTION_CODES) {
                val rejected = current.copy(creationRejectionCode = error.code, updatedAtEpochMillis = System.currentTimeMillis())
                requestStore.save(rejected)
                onRequestPersisted(rejected)
            }
            throw error
        }
        current = current.copy(
            jobId = created.jobId,
            uploadPath = created.uploadTarget.path,
            sourceUploaded = sourceReady || created.sourceBound,
            status = created.status.name,
            creationRejectionCode = null,
            updatedAtEpochMillis = System.currentTimeMillis(),
        )
        requestStore.save(current)
        onRequestPersisted(current)

        if (created.status.name in TERMINAL_STATUSES || created.status == AiJobStatus.PROCESSING) {
            return awaitExisting(current, created.jobId, onRequestPersisted)
        }
        if (!created.sourceBound) {
            api.uploadSource(created.uploadTarget, source)
            current = current.copy(sourceUploaded = true, updatedAtEpochMillis = System.currentTimeMillis())
            requestStore.save(current)
            onRequestPersisted(current)
            val bound = api.createOrGetJob(
                current.idempotencyKey,
                sourceMetadata.copy(objectPath = created.uploadTarget.path),
                sourceReady = true,
                preferences = current.preferences,
            )
            check(bound.sourceBound) { "AI source binding was not confirmed." }
            check(bound.jobId == created.jobId) { "AI source binding changed job identity." }
            current = current.copy(
                jobId = bound.jobId,
                uploadPath = bound.uploadTarget.path,
                sourceUploaded = true,
                status = bound.status.name,
                updatedAtEpochMillis = System.currentTimeMillis(),
            )
            requestStore.save(current)
            onRequestPersisted(current)
        }
        if (current.status in TERMINAL_STATUSES || current.status == AiJobStatus.PROCESSING.name) {
            return awaitExisting(current, current.jobId ?: error("AI job id is missing."), onRequestPersisted)
        }
        return processAndAwait(current, onRequestPersisted)
    }

    private suspend fun processAndAwait(
        request: PersistedAiJobRequest,
        onRequestPersisted: suspend (PersistedAiJobRequest) -> Unit,
    ): AiJob {
        val jobId = request.jobId ?: error("AI job id must be persisted before processing.")
        val acceptedStatus = api.processJob(jobId)
        val processing = request.copy(status = acceptedStatus.name, updatedAtEpochMillis = System.currentTimeMillis())
        requestStore.save(processing)
        onRequestPersisted(processing)
        return awaitExisting(processing, jobId, onRequestPersisted)
    }

    private suspend fun awaitExisting(
        request: PersistedAiJobRequest,
        jobId: String,
        onRequestPersisted: suspend (PersistedAiJobRequest) -> Unit = {},
    ): AiJob {
        val job = api.awaitJob(jobId, pollingPolicy)
        val updated = request.copy(jobId = job.jobId, status = job.status.name, updatedAtEpochMillis = System.currentTimeMillis())
        requestStore.save(updated)
        onRequestPersisted(updated)
        return job
    }

    private fun requireAuthenticated() {
        if (accessTokenProvider.accessToken().isNullOrBlank()) throw AiAuthenticationRequiredException()
    }

    private fun requireAuthenticatedUser(): String {
        requireAuthenticated()
        return userIdProvider()?.takeIf(String::isNotBlank) ?: throw AiAuthenticationRequiredException()
    }

    private fun requireOwnedByCurrentUser(request: PersistedAiJobRequest) {
        val ownerUserId = requireAuthenticatedUser()
        if (request.ownerUserId != ownerUserId) throw AiJobOwnershipException()
    }

    private fun PdfSource.toMetadata(objectPath: String? = null): AiSourceMetadata = AiSourceMetadata(
        fileName = fileName,
        mimeType = mimeType,
        sourceHash = sha256,
        sourceBytes = bytes.size.toLong(),
        objectPath = objectPath,
        sourceText = EditalPdfText.of(bytes, sha256),
    )

    private fun requestLock(requestId: String): Mutex = REQUEST_LOCKS.computeIfAbsent(requestId) { Mutex() }

    private companion object {
        // Shared by foreground and WorkManager repository instances. Remote process also claims atomically.
        val REQUEST_LOCKS = ConcurrentHashMap<String, Mutex>()
        val RETRYABLE_TERMINAL_STATUSES = setOf(
            AiJobStatus.FAILED,
            AiJobStatus.EXPIRED,
            AiJobStatus.CANCELLED,
        )
        val TERMINAL_STATUSES = setOf(
            AiJobStatus.SUCCEEDED.name,
            AiJobStatus.FAILED.name,
            AiJobStatus.EXPIRED.name,
            AiJobStatus.CANCELLED.name,
        )
    }
}

class AiJobOwnershipException : SecurityException("This AI request does not belong to the authenticated user.")

class AiLegacyRecoveryBlockedException : IllegalStateException(
    "A prior AI request cannot be safely matched to this account and syllabus.",
)
