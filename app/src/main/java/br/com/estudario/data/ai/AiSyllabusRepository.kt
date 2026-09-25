package br.com.estudario.data.ai

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import br.com.estudario.data.remote.AiAccessTokenProvider
import java.util.UUID
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
    val updatedAtEpochMillis: Long = System.currentTimeMillis(),
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
) : AiJobRecoveryRepository {
    suspend fun start(uri: String, fileName: String? = null): AiJob {
        requireAuthenticated()
        val source = sourceReader.read(uri, fileName)
        val sourcePath = sourceSnapshots.save(source)
        val request = PersistedAiJobRequest(
            requestId = UUID.randomUUID().toString(),
            idempotencyKey = UUID.randomUUID().toString(),
            sourceUri = source.uri,
            fileName = source.fileName,
            mimeType = source.mimeType,
            sourceHash = source.sha256,
            sourceBytes = source.bytes.size.toLong(),
            sourcePath = sourcePath,
        )
        requestStore.save(request)
        return continueRequest(request, source)
    }

    suspend fun recover(requestId: String): AiJob {
        requireAuthenticated()
        val stored = requestStore.get(requestId) ?: throw IllegalArgumentException("AI request not found.")
        val (request, source) = durableSource(stored)
        if (request.jobId != null && request.status != AiJobStatus.RESERVED.name) return awaitExisting(request, request.jobId)
        return continueRequest(request, source)
    }

    suspend fun retryFailed(requestId: String): AiJob {
        requireAuthenticated()
        val previous = requestStore.get(requestId) ?: throw IllegalArgumentException("AI request not found.")
        val previousStatus = previous.status?.let { runCatching { AiJobStatus.valueOf(it) }.getOrNull() }
        require(previousStatus == AiJobStatus.FAILED || previousStatus == AiJobStatus.EXPIRED || previousStatus == AiJobStatus.CANCELLED) {
            "Only a terminal failed AI job can be retried."
        }
        val (durablePrevious, source) = durableSource(previous)
        val retry = previous.copy(
            idempotencyKey = UUID.randomUUID().toString(),
            jobId = null,
            uploadPath = null,
            status = null,
            sourceHash = source.sha256,
            sourceBytes = source.bytes.size.toLong(),
            sourcePath = durablePrevious.sourcePath,
            updatedAtEpochMillis = System.currentTimeMillis(),
        )
        requestStore.save(retry)
        return continueRequest(retry, source)
    }

    /** Resumes the latest persisted attempt, retrying only when that attempt is terminal. */
    suspend fun resumeOrRetry(requestId: String): AiJob {
        requireAuthenticated()
        val request = requestStore.get(requestId) ?: throw IllegalArgumentException("AI request not found.")
        val status = request.status?.let { runCatching { AiJobStatus.valueOf(it) }.getOrNull() }
        return if (status in RETRYABLE_TERMINAL_STATUSES) retryFailed(requestId) else recover(requestId)
    }

    override suspend fun recoverPendingJobs(): List<AiJob> = buildList {
        requireAuthenticated()
        requestStore.list()
            .filter { it.status !in setOf(AiJobStatus.SUCCEEDED.name, AiJobStatus.FAILED.name, AiJobStatus.EXPIRED.name, AiJobStatus.CANCELLED.name) }
            .forEach { request ->
                val (durableRequest, source) = durableSource(request)
                if (durableRequest.jobId != null && durableRequest.status != AiJobStatus.RESERVED.name) {
                    add(awaitExisting(durableRequest, durableRequest.jobId))
                } else {
                    add(continueRequest(durableRequest, source))
                }
            }
    }

    private suspend fun durableSource(request: PersistedAiJobRequest): Pair<PersistedAiJobRequest, PdfSource> {
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
        return durableRequest to source
    }

    private suspend fun continueRequest(request: PersistedAiJobRequest, source: PdfSource): AiJob {
        var current = request
        val sourceMetadata = source.toMetadata()
        val sourceReady = current.sourceUploaded && current.uploadPath != null
        val created = api.createOrGetJob(
            current.idempotencyKey,
            sourceMetadata.copy(objectPath = current.uploadPath.takeIf { sourceReady }),
            sourceReady = sourceReady,
        )
        current = current.copy(
            jobId = created.jobId,
            uploadPath = created.uploadTarget.path,
            sourceUploaded = sourceReady || created.sourceBound,
            status = created.status.name,
            updatedAtEpochMillis = System.currentTimeMillis(),
        )
        requestStore.save(current)

        if (!created.sourceBound) {
            api.uploadSource(created.uploadTarget, source)
            current = current.copy(sourceUploaded = true, updatedAtEpochMillis = System.currentTimeMillis())
            requestStore.save(current)
            val bound = api.createOrGetJob(
                current.idempotencyKey,
                sourceMetadata.copy(objectPath = created.uploadTarget.path),
                sourceReady = true,
            )
            current = current.copy(
                jobId = bound.jobId,
                uploadPath = bound.uploadTarget.path,
                sourceUploaded = true,
                status = bound.status.name,
                updatedAtEpochMillis = System.currentTimeMillis(),
            )
            requestStore.save(current)
        }
        if (current.status in TERMINAL_STATUSES) {
            return awaitExisting(current, current.jobId ?: error("AI job id is missing."))
        }
        return processAndAwait(current)
    }

    private suspend fun processAndAwait(request: PersistedAiJobRequest): AiJob {
        val jobId = request.jobId ?: error("AI job id must be persisted before processing.")
        try {
            val acceptedStatus = api.processJob(jobId)
            val processing = request.copy(status = acceptedStatus.name, updatedAtEpochMillis = System.currentTimeMillis())
            requestStore.save(processing)
            return awaitExisting(processing, jobId)
        } catch (error: AiProcessTimeoutException) {
            requestStore.save(request.copy(jobId = jobId, status = AiJobStatus.PROCESSING.name, updatedAtEpochMillis = System.currentTimeMillis()))
            throw error
        }
    }

    private suspend fun awaitExisting(request: PersistedAiJobRequest, jobId: String): AiJob {
        return try {
            val job = api.awaitJob(jobId, pollingPolicy)
            requestStore.save(request.copy(jobId = job.jobId, status = job.status.name, updatedAtEpochMillis = System.currentTimeMillis()))
            job
        } catch (error: AiProcessTimeoutException) {
            requestStore.save(request.copy(jobId = jobId, status = AiJobStatus.PROCESSING.name, updatedAtEpochMillis = System.currentTimeMillis()))
            throw error
        }
    }

    private fun requireAuthenticated() {
        if (accessTokenProvider.accessToken().isNullOrBlank()) throw AiAuthenticationRequiredException()
    }

    private fun PdfSource.toMetadata(objectPath: String? = null): AiSourceMetadata = AiSourceMetadata(
        fileName = fileName,
        mimeType = mimeType,
        sourceHash = sha256,
        sourceBytes = bytes.size.toLong(),
        objectPath = objectPath,
    )

    private companion object {
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
