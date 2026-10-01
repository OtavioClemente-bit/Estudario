package br.com.estudario.ui.ai

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import br.com.estudario.EstudarioApplication
import br.com.estudario.data.ai.AiSyllabusPreferences
import br.com.estudario.data.ai.PreparedSyllabusSource
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import br.com.estudario.data.ai.AiJob
import br.com.estudario.data.ai.AiApiException
import br.com.estudario.data.ai.AI_QUOTA_REJECTION_CODES
import br.com.estudario.data.ai.DEVICE_QUOTA_MESSAGE
import br.com.estudario.data.ai.AiAuthenticationRequiredException
import br.com.estudario.data.ai.AiJobRequestStore
import br.com.estudario.data.ai.AiProcessTimeoutException
import br.com.estudario.data.ai.DataStoreAiJobRequestStore
import br.com.estudario.data.ai.DefaultAiSyllabusRepository
import br.com.estudario.data.ai.PersistedAiJobRequest
import br.com.estudario.data.ai.AiJobStatus
import br.com.estudario.data.local.RemoteSyllabusSyncState
import br.com.estudario.data.syllabus.ApplyResult
import br.com.estudario.data.syllabus.ExistingSyllabusContentException
import br.com.estudario.data.syllabus.SyllabusApplicationService
import br.com.estudario.domain.ai.AiSyllabusDraft
import br.com.estudario.domain.ai.AiSyllabusDraftSubject
import br.com.estudario.domain.ai.AiSyllabusDraftTopic
import br.com.estudario.data.ai.AiPriority
import br.com.estudario.data.ai.AiSubjectProposal
import br.com.estudario.data.ai.AiTopicProposal
import br.com.estudario.data.ai.AiWarning
import br.com.estudario.data.ai.AiWarningCode
import br.com.estudario.data.ai.AiWarningSeverity
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

data class AiReviewStarted(val job: AiJob, val identity: AiReviewRequestIdentity)

/**
 * A non-terminal failure after the durable request was created. The request identity is retained
 * so retry/restart can continue the same server-side idempotent request instead of creating one.
 */
class AiReviewStartException(
    val pendingIdentity: AiReviewPendingRequestIdentity?,
    cause: Throwable,
) : IllegalStateException("AI review start did not complete.", cause) {
    val identity: AiReviewRequestIdentity? get() = pendingIdentity?.asStartedIdentity()

    constructor(identity: AiReviewRequestIdentity, cause: Throwable) : this(
        AiReviewPendingRequestIdentity(identity.requestId, identity.idempotencyKey, identity.jobId, identity.ownerUserId),
        cause,
    )
}

interface AiReviewJobs {
    suspend fun prepare(target: AiReviewTarget): PreparedSyllabusSource = error("Local PDF preparation is unavailable.")
    suspend fun startPrepared(targetId: Long, targetTitle: String, source: PreparedSyllabusSource, preferences: AiSyllabusPreferences, onRequestPersisted: suspend (PersistedAiJobRequest) -> Unit): AiReviewStarted =
        start(targetId, targetTitle, source.uri, source.fileName, preferences, onRequestPersisted)
    suspend fun start(targetId: Long, uri: String, fileName: String?): AiReviewStarted
    suspend fun start(
        targetId: Long,
        targetTitle: String,
        uri: String,
        fileName: String?,
        onRequestPersisted: suspend (PersistedAiJobRequest) -> Unit,
    ): AiReviewStarted = start(targetId, uri, fileName)
    suspend fun start(
        targetId: Long,
        targetTitle: String,
        uri: String,
        fileName: String?,
        preferences: AiSyllabusPreferences?,
        onRequestPersisted: suspend (PersistedAiJobRequest) -> Unit,
    ): AiReviewStarted = start(targetId, targetTitle, uri, fileName, onRequestPersisted)
    suspend fun recover(requestId: String): AiReviewStarted
    suspend fun recover(
        requestId: String,
        onRequestPersisted: suspend (PersistedAiJobRequest) -> Unit = {},
    ): AiReviewStarted = recover(requestId)
    suspend fun recoverPersistedForTarget(
        targetId: Long,
        onRequestPersisted: suspend (PersistedAiJobRequest) -> Unit = {},
    ): AiReviewStarted? = null
    suspend fun recoverPersistedForTarget(targetId: Long, preferredRequestId: String?, onRequestPersisted: suspend (PersistedAiJobRequest) -> Unit): AiReviewStarted? =
        recoverPersistedForTarget(targetId, onRequestPersisted)
    suspend fun retryFailed(requestId: String): AiReviewStarted
    suspend fun resumeOrRetry(requestId: String): AiReviewStarted = recover(requestId)
    suspend fun resumeOrRetry(
        requestId: String,
        onRequestPersisted: suspend (PersistedAiJobRequest) -> Unit = {},
    ): AiReviewStarted = resumeOrRetry(requestId)
    suspend fun recoverPending(): List<AiReviewStarted>
    suspend fun identityForJob(jobId: String): AiReviewRequestIdentity?
}

class DefaultAiReviewJobs(
    private val repository: DefaultAiSyllabusRepository,
    private val requestStore: AiJobRequestStore,
    private val userIdProvider: () -> String?,
) : AiReviewJobs {
    override suspend fun prepare(target: AiReviewTarget) = repository.prepare(
        requireNotNull(target.sourceUri), target.sourceName, target.snapshotPath, target.sourceHash, target.entryId,
    )

    override suspend fun startPrepared(targetId: Long, targetTitle: String, source: PreparedSyllabusSource, preferences: AiSyllabusPreferences, onRequestPersisted: suspend (PersistedAiJobRequest) -> Unit): AiReviewStarted = try {
        started(repository.startPrepared(source, targetId, targetTitle, preferences, onRequestPersisted))
    } catch (error: Throwable) {
        if (error is CancellationException || error is AiProcessTimeoutException) throw error
        throw AiReviewStartException(requestStore.get(source.attemptId)?.toPendingIdentity(), error)
    }
    override suspend fun start(targetId: Long, uri: String, fileName: String?): AiReviewStarted =
        start(targetId, "", uri, fileName) {}

    override suspend fun start(
        targetId: Long,
        targetTitle: String,
        uri: String,
        fileName: String?,
        onRequestPersisted: suspend (PersistedAiJobRequest) -> Unit,
    ): AiReviewStarted = start(targetId, targetTitle, uri, fileName, null, onRequestPersisted)

    override suspend fun start(
        targetId: Long,
        targetTitle: String,
        uri: String,
        fileName: String?,
        preferences: AiSyllabusPreferences?,
        onRequestPersisted: suspend (PersistedAiJobRequest) -> Unit,
    ): AiReviewStarted {
        require(targetId > 0L) { "targetId must be positive" }
        val knownRequestIds = requestStore.list().mapTo(hashSetOf()) { it.requestId }
        return try {
            val job = repository.start(uri, fileName, targetId, targetTitle, preferences, onRequestPersisted)
            started(job)
        } catch (timeout: AiProcessTimeoutException) {
            throw timeout
        } catch (error: Throwable) {
            if (error is CancellationException) throw error
            val persisted = requestStore.list()
                .asSequence()
                .filterNot { it.requestId in knownRequestIds }
                .filter { it.ownerUserId == userIdProvider() }
                .filter { it.sourceUri == uri && (fileName == null || it.fileName == fileName) }
                .maxByOrNull { it.updatedAtEpochMillis }
            throw AiReviewStartException(persisted?.toPendingIdentity(), error)
        }
    }

    override suspend fun recover(requestId: String): AiReviewStarted = recover(requestId) {}

    override suspend fun recover(
        requestId: String,
        onRequestPersisted: suspend (PersistedAiJobRequest) -> Unit,
    ): AiReviewStarted = started(repository.recover(requestId, onRequestPersisted))

    override suspend fun recoverPersistedForTarget(
        targetId: Long,
        onRequestPersisted: suspend (PersistedAiJobRequest) -> Unit,
    ): AiReviewStarted? = recoverPersistedForTarget(targetId, null, onRequestPersisted)

    override suspend fun recoverPersistedForTarget(targetId: Long, preferredRequestId: String?, onRequestPersisted: suspend (PersistedAiJobRequest) -> Unit): AiReviewStarted? {
        val owner = userIdProvider()?.takeIf(String::isNotBlank) ?: return null
        val matching = requestStore.list().filter { it.ownerUserId == owner && it.targetSyllabusId == targetId }
        matching.firstOrNull { it.requestId == preferredRequestId }?.let {
            return started(repository.recover(it.requestId, onRequestPersisted))
        }
        val active = matching.filter { it.creationRejectionCode == null && it.status !in setOf("SUCCEEDED", "FAILED", "CANCELLED", "EXPIRED") }
        if (active.size > 1) throw AiRecoveryAmbiguousException()
        val request = active.singleOrNull() ?: matching.maxByOrNull { it.createdAtEpochMillis } ?: return null
        return started(repository.recover(request.requestId, onRequestPersisted))
    }

    override suspend fun retryFailed(requestId: String): AiReviewStarted = started(repository.retryFailed(requestId))

    override suspend fun resumeOrRetry(requestId: String): AiReviewStarted = resumeOrRetry(requestId) {}

    override suspend fun resumeOrRetry(
        requestId: String,
        onRequestPersisted: suspend (PersistedAiJobRequest) -> Unit,
    ): AiReviewStarted = started(repository.resumeOrRetry(requestId, onRequestPersisted))

    override suspend fun recoverPending(): List<AiReviewStarted> = repository.recoverPendingJobs().map { started(it) }

    override suspend fun identityForJob(jobId: String): AiReviewRequestIdentity? = requestStore.list()
        .firstOrNull { it.jobId == jobId && it.ownerUserId == userIdProvider() }
        ?.toIdentity()

    private suspend fun started(job: AiJob): AiReviewStarted {
        val request = requestStore.list().firstOrNull { it.jobId == job.jobId && it.ownerUserId == userIdProvider() }
            ?: error("AI request metadata was not persisted for ${job.jobId}.")
        return AiReviewStarted(job, request.toIdentity())
    }
}

fun interface AiReviewApplier {
    suspend fun apply(targetId: Long, draft: AiSyllabusDraft, jobId: String, replaceExisting: Boolean): ApplyResult

    suspend fun findApplied(targetId: Long, sourceJobId: String): ApplyResult? = null
}

interface AiReviewSessionStore {
    suspend fun load(targetId: Long): AiReviewPersistedSession? = null
    suspend fun loadLatest(): AiReviewPersistedSession? = null
    suspend fun save(session: AiReviewPersistedSession)
    suspend fun clear(targetId: Long) = Unit
    suspend fun load(targetId: Long, ownerUserId: String): AiReviewPersistedSession? =
        load(targetId)?.takeIf { it.ownerUserId == ownerUserId }
    suspend fun loadLatest(ownerUserId: String): AiReviewPersistedSession? =
        loadLatest()?.takeIf { it.ownerUserId == ownerUserId && !it.applied }
    suspend fun clear(targetId: Long, ownerUserId: String) = clear(targetId)
}

@Serializable
data class AiReviewPersistedSession(
    val targetId: Long,
    val targetTitle: String,
    val requestId: String,
    val jobId: String = "",
    val idempotencyKey: String,
    val ownerUserId: String? = null,
    val draftJson: String? = null,
    val applied: Boolean = false,
    val outboxId: Long? = null,
)

private val Context.aiReviewSessionDataStore: DataStore<Preferences> by preferencesDataStore(name = "ai_review_sessions")
private val Context.aiReviewTargetDataStore: DataStore<Preferences> by preferencesDataStore(name = "ai_review_target")

class DataStoreAiReviewSessionStore(private val dataStore: DataStore<Preferences>) : AiReviewSessionStore {
    constructor(context: Context) : this(context.aiReviewSessionDataStore)

    private val key = stringPreferencesKey("sessions")
    private val json = Json { ignoreUnknownKeys = true; encodeDefaults = true }

    override suspend fun load(targetId: Long): AiReviewPersistedSession? = null

    override suspend fun loadLatest(): AiReviewPersistedSession? = null

    override suspend fun clear(targetId: Long) = Unit

    override suspend fun load(targetId: Long, ownerUserId: String): AiReviewPersistedSession? = dataStore.data.first()[key]
        ?.let { runCatching { json.decodeFromString<List<AiReviewPersistedSession>>(it) }.getOrDefault(emptyList()) }
        ?.firstOrNull { it.targetId == targetId && it.ownerUserId == ownerUserId }

    override suspend fun loadLatest(ownerUserId: String): AiReviewPersistedSession? = dataStore.data.first()[key]
        ?.let { runCatching { json.decodeFromString<List<AiReviewPersistedSession>>(it) }.getOrDefault(emptyList()) }
        ?.lastOrNull { it.ownerUserId == ownerUserId && !it.applied }

    override suspend fun save(session: AiReviewPersistedSession) {
        dataStore.edit { preferences ->
            val sessions = preferences[key]?.let { runCatching { json.decodeFromString<List<AiReviewPersistedSession>>(it) }.getOrDefault(emptyList()) }.orEmpty()
            preferences[key] = json.encodeToString((sessions.filterNot {
                it.targetId == session.targetId && it.ownerUserId == session.ownerUserId
            } + session))
        }
    }

    override suspend fun clear(targetId: Long, ownerUserId: String) {
        dataStore.edit { preferences ->
            val sessions = preferences[key]?.let { runCatching { json.decodeFromString<List<AiReviewPersistedSession>>(it) }.getOrDefault(emptyList()) }.orEmpty()
            preferences[key] = json.encodeToString(sessions.filterNot {
                it.targetId == targetId && it.ownerUserId == ownerUserId
            })
        }
    }
}

interface AiReviewTargetStore {
    suspend fun load(): AiReviewTarget? = null
    suspend fun loadUnownedLegacy(): AiReviewTarget? = null
    suspend fun save(target: AiReviewTarget) = Unit
    suspend fun saveAfterOwnershipProof(target: AiReviewTarget, ownerUserId: String) = save(target, ownerUserId)
    suspend fun clear() = Unit
    suspend fun load(ownerUserId: String): AiReviewTarget? = load()
    suspend fun save(target: AiReviewTarget, ownerUserId: String) = save(target)
    suspend fun clear(ownerUserId: String) = clear()
}

class DataStoreAiReviewTargetStore(private val dataStore: DataStore<Preferences>) : AiReviewTargetStore {
    constructor(context: Context) : this(context.aiReviewTargetDataStore)

    private val key = stringPreferencesKey("target")
    private val json = Json { ignoreUnknownKeys = true; encodeDefaults = true }

    @Serializable
    private data class StoredTarget(
        val ownerUserId: String? = null,
        val id: Long,
        val title: String,
        val sourceUri: String? = null,
        val sourceName: String? = null,
        val preferences: AiSyllabusPreferences? = null,
        val awaitingConfirmation: Boolean = false,
        val snapshotPath: String? = null,
        val sourceHash: String? = null,
        val entryId: String = "legacy",
    )

    override suspend fun load(): AiReviewTarget? = null

    override suspend fun save(target: AiReviewTarget) = Unit

    override suspend fun clear() = Unit

    override suspend fun loadUnownedLegacy(): AiReviewTarget? {
        val stored = dataStore.data.first()[key]?.let { raw ->
            runCatching { json.decodeFromString<List<StoredTarget>>(raw) }.getOrElse {
                runCatching { listOf(json.decodeFromString<StoredTarget>(raw)) }.getOrDefault(emptyList())
            }
        }.orEmpty().filter { it.ownerUserId == null }
        return stored.singleOrNull()?.let { it.target() }
    }

    override suspend fun load(ownerUserId: String): AiReviewTarget? = dataStore.data.first()[key]
        ?.let { raw ->
            runCatching { json.decodeFromString<List<StoredTarget>>(raw) }.getOrElse {
                runCatching { listOf(json.decodeFromString<StoredTarget>(raw)) }.getOrDefault(emptyList())
            }
        }
        ?.firstOrNull { it.ownerUserId == ownerUserId }
        ?.let { it.target() }

    override suspend fun save(target: AiReviewTarget, ownerUserId: String) {
        dataStore.edit { preferences ->
            val stored = preferences[key]?.let { raw ->
                runCatching { json.decodeFromString<List<StoredTarget>>(raw) }.getOrElse {
                    runCatching { listOf(json.decodeFromString<StoredTarget>(raw)) }.getOrDefault(emptyList())
                }
            }.orEmpty()
            val updated = storedTarget(target, ownerUserId)
            preferences[key] = json.encodeToString(stored.filterNot { it.ownerUserId == ownerUserId } + updated)
        }
    }

    override suspend fun saveAfterOwnershipProof(target: AiReviewTarget, ownerUserId: String) {
        dataStore.edit { preferences ->
            val stored = preferences[key]?.let { raw ->
                runCatching { json.decodeFromString<List<StoredTarget>>(raw) }.getOrElse {
                    runCatching { listOf(json.decodeFromString<StoredTarget>(raw)) }.getOrDefault(emptyList())
                }
            }.orEmpty()
            val updated = storedTarget(target, ownerUserId)
            preferences[key] = json.encodeToString(stored.filterNot {
                it.ownerUserId == ownerUserId || (it.ownerUserId == null && it.id == target.id)
            } + updated)
        }
    }

    override suspend fun clear(ownerUserId: String) {
        dataStore.edit { preferences ->
            val stored = preferences[key]?.let { raw ->
                runCatching { json.decodeFromString<List<StoredTarget>>(raw) }.getOrElse {
                    runCatching { listOf(json.decodeFromString<StoredTarget>(raw)) }.getOrDefault(emptyList())
                }
            }.orEmpty()
            val remaining = stored.filterNot { it.ownerUserId == ownerUserId }
            if (remaining.isEmpty()) preferences.remove(key) else preferences[key] = json.encodeToString(remaining)
        }
    }

    private fun StoredTarget.target() = AiReviewTarget(id, title, sourceUri, sourceName, preferences, awaitingConfirmation, snapshotPath, sourceHash, entryId)
    private fun storedTarget(target: AiReviewTarget, owner: String) = StoredTarget(owner, target.id, target.title, target.sourceUri, target.sourceName, target.preferences, target.awaitingConfirmation, target.snapshotPath, target.sourceHash, target.entryId)
}

class AiReviewViewModel(
    application: Application,
    private val targetId: Long,
    private val targetTitle: String,
    private val jobs: AiReviewJobs,
    private val applier: AiReviewApplier,
    private val sessions: AiReviewSessionStore,
    private val targetStore: AiReviewTargetStore? = null,
    private val accessGateway: AiReviewAccessGateway,
    private val userIdProvider: () -> String? = { null },
    private val userIdFlow: Flow<String?> = flowOf(userIdProvider()),
    private val loginLauncher: AiReviewLoginLauncher = AiReviewLoginLauncher {},
    private val syncState: suspend (Long) -> RemoteSyllabusSyncState? = { null },
    private val syncPollDelayMillis: Long = 1_000L,
    private val jobRecoveryRetryDelayMillis: Long = 2_000L,
    private val initialTarget: AiReviewTarget? = null,
) : AndroidViewModel(application) {
    private val _state = MutableStateFlow(AiReviewUiState.gate(targetId, targetTitle, AiReviewAccessState.LOADING))
    val state: StateFlow<AiReviewUiState> = _state.asStateFlow()
    private var identity: AiReviewRequestIdentity? = null
    private var pendingIdentity: AiReviewPendingRequestIdentity? = null
    private var pendingSource: AiReviewSource? = null
    private val _preferences = MutableStateFlow<AiSyllabusPreferences?>(initialTarget?.preferences ?: AiSyllabusPreferences(competitionName = targetTitle, role = ""))
    private var preparedSource: PreparedSyllabusSource? = null
    private var preparingTarget: AiReviewTarget? = initialTarget?.takeIf { it.awaitingConfirmation && it.sourceUri != null }
    private var recoveredSourceTarget: AiReviewTarget? = null
    private var recoveryJob: Job? = null
    private var flowEpoch = 0L
    /** Respostas do formulário obrigatório; sem elas a geração não começa. */
    val preferences: StateFlow<AiSyllabusPreferences?> = _preferences.asStateFlow()
    private val startMutex = Mutex()
    private val sessionMutex = Mutex()
    private var hasAppliedLocally = false
    private var appliedReconciliationRequired = false

    init {
        require(jobRecoveryRetryDelayMillis > 0L)
    }

    init {
        recoveryJob = viewModelScope.launch {
            if (preparingTarget != null) prepareSource(requireNotNull(preparingTarget))
            refreshAccessAndRestore()
        }
        viewModelScope.launch {
            var first = true
            var previousUserId = userIdProvider()
            userIdFlow.distinctUntilChanged().collect { currentUserId ->
                if (first) {
                    first = false
                    previousUserId = currentUserId
                    return@collect
                }
                if (previousUserId != currentUserId) {
                    val wasAnonymousPreparation = previousUserId == null && identity == null && pendingIdentity == null && preparedSource != null
                    previousUserId = currentUserId
                    if (wasAnonymousPreparation) {
                        onLoginReturned()
                        return@collect
                    }
                    flowEpoch++
                    recoveryJob?.cancel()
                    identity = null
                    pendingIdentity = null
                    pendingSource = null
                    preparedSource = null
                    preparingTarget = null
                    recoveredSourceTarget = null
                    _preferences.value = AiSyllabusPreferences(competitionName = targetTitle, role = "")
                    hasAppliedLocally = false
                    _state.value = AiReviewUiState.gate(targetId, targetTitle, AiReviewAccessState.UNAUTHENTICATED)
                    refreshAccessAndRestore()
                }
            }
        }
    }

    fun requestLogin() = loginLauncher.launch(::onLoginReturned)

    fun onLoginReturned() {
        recoveryJob?.cancel()
        recoveryJob = viewModelScope.launch { refreshAccessAndRestore() }
    }

    fun updatePreferences(value: AiSyllabusPreferences) {
        if (_state.value.content is AiReviewContent.Submitting || _state.value.content is AiReviewContent.Processing) return
        _preferences.value = value
        if (_state.value.content is AiReviewContent.Confirmation) preparedSource?.let { _state.value = _state.value.copy(content = AiReviewContent.Confirmation(it, it.preflight(value))) }
        viewModelScope.launch { persistPreparation() }
    }

    fun start(uri: String, fileName: String?) {
        if (_state.value.content is AiReviewContent.Processing || _state.value.content is AiReviewContent.Submitting) return
        flowEpoch++
        recoveryJob?.cancel()
        viewModelScope.launch {
            startMutex.withLock {
                if (_state.value.content is AiReviewContent.Processing || _state.value.content is AiReviewContent.Submitting) return@withLock
                val ownerUserId = userIdProvider()
                if (ownerUserId != null) {
                    sessionMutex.withLock {
                        sessions.clear(targetId, ownerUserId)
                        hasAppliedLocally = false
                        appliedReconciliationRequired = false
                    }
                    targetStore?.clear(ownerUserId)
                }
                identity = null
                pendingIdentity = null
                pendingSource = AiReviewSource(uri, fileName)
                prepareSource(AiReviewTarget(targetId, targetTitle, uri, fileName, _preferences.value))
            }
            refreshAccessAndRestore()
        }
    }

    fun provideSource(uri: String, fileName: String?) {
        if (initialTarget != null || preparedSource?.uri == uri) return
        start(uri, fileName)
    }

    private suspend fun prepareSource(target: AiReviewTarget) {
        val epoch = flowEpoch
        preparingTarget = target
        preparedSource = null
        _state.value = _state.value.copy(content = AiReviewContent.Preparing)
        try {
            val startedAt = System.currentTimeMillis()
            val prepared = jobs.prepare(target)
            // A leitura costuma levar menos de um segundo: segura a tela até as três etapas
            // aparecerem, senão ela pisca e some antes de dar para ler.
            val minimum = PREPARATION_STAGE_MILLIS * PreparationStages.size + 250L
            kotlinx.coroutines.delay((minimum - (System.currentTimeMillis() - startedAt)).coerceAtLeast(0L))
            if (epoch != flowEpoch) return
            preparedSource = prepared
            pendingSource = AiReviewSource(prepared.uri, prepared.fileName)
            persistPreparation()
            if (epoch != flowEpoch) return
            _state.value = _state.value.copy(content = AiReviewContent.Confirmation(prepared, prepared.preflight(requireNotNull(_preferences.value))))
        } catch (error: Throwable) {
            if (error is CancellationException) throw error
            fail("Não foi possível ler este PDF. Escolha outro arquivo para continuar.")
        }
    }

    private suspend fun persistPreparation() {
        val source = preparedSource ?: return
        val owner = userIdProvider()?.takeIf(String::isNotBlank) ?: return
        targetStore?.save(AiReviewTarget(targetId, targetTitle, source.uri, source.fileName, _preferences.value, true, source.snapshotPath, source.sha256, source.attemptId), owner)
    }

    fun confirmGeneration() {
        val confirmation = _state.value.content as? AiReviewContent.Confirmation ?: return
        val preferences = _preferences.value?.takeIf { it.isComplete } ?: return
        if (!confirmation.preflight.canGenerate || _state.value.access.kind != AiReviewAccessKind.READY) return
        // Synchronous transition consumes repeated taps before the coroutine starts.
        _state.value = _state.value.copy(content = AiReviewContent.Submitting)
        val epoch = flowEpoch
        val owner = userIdProvider()
        viewModelScope.launch { if (epoch == flowEpoch && owner == userIdProvider()) startConfirmed(confirmation.source, preferences) }
    }

    fun editInformation() {
        flowEpoch++
        if (preparedSource == null) {
            val previous = recoveredSourceTarget ?: return
            recoveryJob?.cancel()
            viewModelScope.launch {
                identity = null
                pendingIdentity = null
                prepareSource(previous.copy(awaitingConfirmation = true, entryId = java.util.UUID.randomUUID().toString()))
            }
            return
        }
        preparedSource?.let { source ->
            val fresh = source.copy(attemptId = java.util.UUID.randomUUID().toString())
            preparedSource = fresh
            identity = null
            pendingIdentity = null
            _state.value = _state.value.copy(content = AiReviewContent.Confirmation(fresh, fresh.preflight(requireNotNull(_preferences.value))))
            preparingTarget = AiReviewTarget(targetId, targetTitle, source.uri, source.fileName, _preferences.value)
            viewModelScope.launch { persistPreparation() }
        }
    }

    fun changeDraft(draft: AiSyllabusDraft) {
        val current = _state.value.content as? AiReviewContent.Review ?: return
        _state.value = _state.value.copy(content = current.copy(draft = draft, validationError = null))
        viewModelScope.launch { save(identity ?: return@launch, draft) }
    }

    fun apply() = apply(replaceExisting = false)

    fun confirmReplacement() = apply(replaceExisting = true)

    fun cancelReplacement() {
        val current = _state.value.content as? AiReviewContent.Review ?: return
        _state.value = _state.value.copy(content = current.copy(confirmReplacement = false))
    }

    fun retry() {
        val failure = _state.value.content as? AiReviewContent.Failure ?: return
        if (!failure.canRetry) return
        if (appliedReconciliationRequired) {
            viewModelScope.launch { refreshAccessAndRestore() }
            return
        }
        val requestId = identity?.requestId ?: pendingIdentity?.requestId ?: return
        val epoch = flowEpoch
        _state.value = _state.value.copy(content = AiReviewContent.Submitting)
        viewModelScope.launch {
            runCatching {
                jobs.resumeOrRetry(requestId, requestContextWriter(flowEpoch))
            }
                .onSuccess { started -> if (epoch == flowEpoch) { identity = started.identity; render(started.job, started.identity) } }
                .onFailure { error ->
                    if (error is CancellationException) throw error
                    if (epoch != flowEpoch) return@onFailure
                    fail(error)
                }
        }
    }

    fun useFallback() {
        viewModelScope.launch {
            val ownerUserId = userIdProvider()
            sessionMutex.withLock {
                if (ownerUserId != null) sessions.clear(targetId, ownerUserId)
                hasAppliedLocally = false
            }
            ownerUserId?.let { targetStore?.clear(it) }
            identity = null
            pendingIdentity = null
            _state.value = AiReviewUiState.gate(targetId, targetTitle, _state.value.access)
        }
    }

    private fun apply(replaceExisting: Boolean) {
        val current = _state.value.content as? AiReviewContent.Review ?: return
        val requestIdentity = identity ?: return
        if (requestIdentity.ownerUserId == null || requestIdentity.ownerUserId != userIdProvider()) {
            fail("Entre novamente na conta que iniciou esta geração para aplicar o resultado.")
            return
        }
        viewModelScope.launch {
            try {
                val result = applier.apply(targetId, current.draft, requestIdentity.jobId, replaceExisting)
                saveApplied(requestIdentity, current.draft, result.outboxId)
                _state.value = _state.value.copy(content = AiReviewContent.Applied(result.state))
                watchSync(result.outboxId)
            } catch (_: ExistingSyllabusContentException) {
                save(requestIdentity, current.draft)
                _state.value = _state.value.copy(content = current.copy(confirmReplacement = true))
            } catch (error: Throwable) {
                save(requestIdentity, current.draft)
                _state.value = _state.value.copy(content = current.copy(validationError = safeMessage(error)))
            }
        }
    }

    private suspend fun refreshAccessAndRestore() {
        val epoch = flowEpoch
        val access = runCatching { accessGateway.check().toUiState() }
            .getOrElse { if (it is CancellationException) throw it; AiReviewAccessState.denied("ACCESS_UNAVAILABLE") }
        if (epoch != flowEpoch) return
        _state.value = _state.value.copy(access = access)
        if (preparingTarget != null || _state.value.content is AiReviewContent.Confirmation) {
            persistPreparation()
            return
        }
        if (initialTarget?.awaitingConfirmation == true && initialTarget.sourceUri == null) return
        if (access.kind == AiReviewAccessKind.UNAUTHENTICATED || access.kind == AiReviewAccessKind.LOADING) return
        val ownerUserId = userIdProvider()?.takeIf(String::isNotBlank) ?: return
        val saved = sessions.load(targetId, ownerUserId)
        if (epoch != flowEpoch) return
        if (saved?.applied == true) {
            appliedReconciliationRequired = false
            hasAppliedLocally = true
            val outboxId = saved.outboxId
            val restoredSyncState = outboxId?.let { runCatching { syncState(it) }.getOrNull() }
                ?: RemoteSyllabusSyncState.PENDING
            if (epoch != flowEpoch) return
            _state.value = _state.value.copy(content = AiReviewContent.Applied(restoredSyncState))
            if (outboxId != null && restoredSyncState == RemoteSyllabusSyncState.PENDING) watchSync(outboxId)
            return
        }
        if (saved != null) {
            val savedJobId = saved.jobId.takeIf { it.isNotBlank() }
            if (savedJobId != null) {
                appliedReconciliationRequired = true
                val appliedResult = try {
                    applier.findApplied(targetId, savedJobId)
                } catch (error: Throwable) {
                    if (error is CancellationException) throw error
                    fail(error)
                    return
                }
                if (epoch != flowEpoch) return
                if (appliedResult != null) {
                    if (appliedResult.localSyllabusId != targetId ||
                        appliedResult.sourceJobId != savedJobId ||
                        appliedResult.outboxId <= 0L
                    ) {
                        fail("Não foi possível validar a aplicação local deste edital.")
                        return
                    }
                    sessionMutex.withLock {
                        sessions.save(saved.copy(applied = true, outboxId = appliedResult.outboxId))
                        hasAppliedLocally = true
                    }
                    appliedReconciliationRequired = false
                    val restoredSyncState = runCatching { syncState(appliedResult.outboxId) }.getOrNull()
                        ?: appliedResult.state
                    if (epoch != flowEpoch) return
                    _state.value = _state.value.copy(content = AiReviewContent.Applied(restoredSyncState))
                    if (restoredSyncState == RemoteSyllabusSyncState.PENDING) watchSync(appliedResult.outboxId)
                    return
                }
                appliedReconciliationRequired = false
            }
            pendingIdentity = AiReviewPendingRequestIdentity(
                saved.requestId,
                saved.idempotencyKey,
                saved.jobId.takeIf { it.isNotBlank() },
                saved.ownerUserId,
            )
            identity = pendingIdentity?.asStartedIdentity()
            if (identity != null) {
                _state.value = AiReviewUiState.processing(targetId, targetTitle, saved.jobId, saved.idempotencyKey, access)
            }
            runCatching { jobs.recover(saved.requestId, requestContextWriter(flowEpoch)) }
                .onSuccess { started -> if (epoch == flowEpoch) { identity = started.identity; render(started.job, started.identity, saved.draftJson) } }
                .onFailure { if (it is CancellationException) throw it; if (epoch == flowEpoch) fail(it) }
        } else {
            runCatching { jobs.recoverPersistedForTarget(targetId, initialTarget?.entryId, requestContextWriter(flowEpoch)) }
                .onSuccess { recovered ->
                    if (epoch != flowEpoch) return@onSuccess
                    if (recovered == null) {
                        return@onSuccess
                    } else {
                        identity = recovered.identity
                        render(recovered.job, recovered.identity)
                    }
                }
                .onFailure { if (it is CancellationException) throw it; if (epoch == flowEpoch) fail(it) }
        }
    }

    private suspend fun startConfirmed(source: PreparedSyllabusSource, preferences: AiSyllabusPreferences) {
        val epoch = flowEpoch
        if (_state.value.access.kind != AiReviewAccessKind.READY) return
        startMutex.withLock {
            if (identity != null || pendingIdentity != null ||
                _state.value.content is AiReviewContent.Processing ||
                _state.value.content is AiReviewContent.Review ||
                _state.value.content is AiReviewContent.Applied
            ) return
            try {
                val ownerUserId = userIdProvider()?.takeIf(String::isNotBlank)
                    ?: throw AiAuthenticationRequiredException()
                sessions.clear(targetId, ownerUserId)
                if (epoch != flowEpoch || ownerUserId != userIdProvider()) return
                preparingTarget = null
                val started = jobs.startPrepared(targetId, targetTitle, source, preferences, requestContextWriter(flowEpoch))
                if (epoch != flowEpoch) return
                identity = started.identity
                pendingIdentity = null
                pendingSource = null
                save(started.identity, null)
                render(started.job, started.identity)
            } catch (timeout: AiProcessTimeoutException) {
                if (epoch != flowEpoch) return
                val recoveredIdentity = jobs.identityForJob(timeout.jobId)
                if (epoch != flowEpoch) return
                if (recoveredIdentity != null) {
                    identity = recoveredIdentity
                    pendingIdentity = AiReviewPendingRequestIdentity(
                        recoveredIdentity.requestId,
                        recoveredIdentity.idempotencyKey,
                        recoveredIdentity.jobId,
                        recoveredIdentity.ownerUserId,
                    )
                    pendingSource = null
                    save(recoveredIdentity, null)
                    _state.value = AiReviewUiState.processing(targetId, targetTitle, recoveredIdentity.jobId, recoveredIdentity.idempotencyKey, _state.value.access)
                    recoverUntilTerminal(recoveredIdentity.requestId)
                } else {
                    fail("A análise continua no servidor, mas não foi possível recuperar seus dados locais.")
                }
            } catch (startFailure: AiReviewStartException) {
                if (epoch != flowEpoch) return
                val recoveredIdentity = startFailure.identity
                val persistedIdentity = startFailure.pendingIdentity
                if (persistedIdentity != null) {
                    pendingIdentity = persistedIdentity
                }
                val cause = startFailure.cause ?: startFailure
                if (recoveredIdentity == null && cause is AiApiException && cause.code in AI_QUOTA_REJECTION_CODES) {
                    persistedIdentity?.let { savePending(it) }
                    fail(cause)
                    return
                }
                if (recoveredIdentity != null) {
                    identity = recoveredIdentity
                    pendingSource = null
                    save(recoveredIdentity, null)
                    _state.value = AiReviewUiState.processing(
                        targetId,
                        targetTitle,
                        recoveredIdentity.jobId,
                        recoveredIdentity.idempotencyKey,
                        _state.value.access,
                    )
                    recoverUntilTerminal(recoveredIdentity.requestId)
                } else if (persistedIdentity != null) {
                    pendingSource = null
                    savePending(persistedIdentity)
                    val knownIdentity = persistedIdentity.asStartedIdentity()
                    if (knownIdentity != null && knownIdentity.ownerUserId == userIdProvider()) {
                        identity = knownIdentity
                        _state.value = AiReviewUiState.processing(
                            targetId,
                            targetTitle,
                            knownIdentity.jobId,
                            knownIdentity.idempotencyKey,
                            _state.value.access,
                        )
                        recoverUntilTerminal(knownIdentity.requestId)
                    } else {
                        fail("Não foi possível confirmar o início da análise. Tente novamente para retomar a mesma solicitação.")
                    }
                } else {
                    fail(cause)
                }
            } catch (error: Throwable) {
                if (error is CancellationException) throw error
                if (epoch != flowEpoch) return
                fail(error)
            }
        }
    }

    private fun render(
        job: AiJob,
        requestIdentity: AiReviewRequestIdentity,
        persistedDraftJson: String? = null,
    ) {
        if (requestIdentity.ownerUserId == null || requestIdentity.ownerUserId != userIdProvider()) return
        val content = when {
            job.status == AiJobStatus.SUCCEEDED && job.proposal != null -> AiReviewContent.Review(
                persistedDraftJson?.let { AiReviewDraftCodec.decode(it) } ?: AiSyllabusDraft.fromProposal(targetId, targetTitle, job.proposal),
            )
            job.status == AiJobStatus.FAILED -> AiReviewContent.Failure(
                "A geração não foi concluída. Você pode tentar novamente.",
                terminalStatus = job.status,
            )
            job.status == AiJobStatus.CANCELLED -> AiReviewContent.Failure(
                "A geração foi cancelada. Você pode tentar novamente.",
                terminalStatus = job.status,
            )
            job.status == AiJobStatus.EXPIRED -> AiReviewContent.Failure(
                "A geração expirou. Você pode tentar novamente.",
                terminalStatus = job.status,
            )
            job.status == AiJobStatus.SUCCEEDED -> AiReviewContent.Failure(
                "A geração terminou, mas não foi possível recuperar o resultado.",
                canRetry = false,
                terminalStatus = job.status,
            )
            job.status == AiJobStatus.RESERVED -> AiReviewContent.Submitting
            job.status == AiJobStatus.PROCESSING ->
                AiReviewContent.Processing(job.jobId, requestIdentity.idempotencyKey)
            else -> error("Unhandled AI job status: ${job.status}")
        }
        _state.value = _state.value.copy(content = content, access = AiReviewAccessState.READY)
        if (content is AiReviewContent.Failure) {
            viewModelScope.launch {
                sessionMutex.withLock {
                    val current = sessions.load(targetId, requestIdentity.ownerUserId)
                    if (current?.requestId == requestIdentity.requestId && identity?.jobId == requestIdentity.jobId) sessions.clear(targetId, requestIdentity.ownerUserId)
                }
            }
        } else {
            viewModelScope.launch { save(requestIdentity, (content as? AiReviewContent.Review)?.draft) }
        }
    }

    /** Continues observing the same persisted job after a bounded poll times out or the network blips. */
    private suspend fun recoverUntilTerminal(requestId: String) {
        val epoch = flowEpoch
        var retryDelay = jobRecoveryRetryDelayMillis
        while (identity?.requestId == requestId && (_state.value.content is AiReviewContent.Processing || _state.value.content is AiReviewContent.Submitting)) {
            try {
                val started = jobs.recover(requestId, requestContextWriter(flowEpoch))
                if (epoch != flowEpoch) return
                identity = started.identity
                render(started.job, started.identity)
                if (started.job.status in TERMINAL_STATUSES) return
            } catch (error: AiProcessTimeoutException) {
                // The existing job is still non-terminal; keep polling the same request.
            } catch (error: AiApiException) {
                if (!error.isTransientForJobRecovery()) {
                    fail(error)
                    return
                }
            } catch (error: AiAuthenticationRequiredException) {
                fail(error)
                return
            } catch (error: Throwable) {
                if (error is CancellationException) throw error
                fail(error)
                return
            }
            delay(retryDelay)
            retryDelay = (retryDelay * 2).coerceAtMost(MAX_JOB_RECOVERY_RETRY_DELAY_MILLIS)
        }
    }

    private suspend fun save(requestIdentity: AiReviewRequestIdentity, draft: AiSyllabusDraft?) {
        if (identity?.requestId != requestIdentity.requestId || identity?.jobId != requestIdentity.jobId ||
            _state.value.content is AiReviewContent.Preparing || _state.value.content is AiReviewContent.Confirmation) return
        pendingIdentity = AiReviewPendingRequestIdentity(
            requestIdentity.requestId,
            requestIdentity.idempotencyKey,
            requestIdentity.jobId,
            requestIdentity.ownerUserId,
        )
        savePending(
            AiReviewPendingRequestIdentity(
                requestIdentity.requestId,
                requestIdentity.idempotencyKey,
                requestIdentity.jobId,
                requestIdentity.ownerUserId,
            ),
            draft?.let(AiReviewDraftCodec::encode),
        )
    }

    private suspend fun savePending(requestIdentity: AiReviewPendingRequestIdentity, draftJson: String? = null) {
        sessionMutex.withLock {
            if (hasAppliedLocally) return@withLock
            val ownerUserId = requestIdentity.ownerUserId ?: return@withLock
            if (ownerUserId != userIdProvider()) return@withLock
            sessions.save(
                AiReviewPersistedSession(
                    targetId = targetId,
                    targetTitle = targetTitle,
                    requestId = requestIdentity.requestId,
                    jobId = requestIdentity.jobId.orEmpty(),
                    idempotencyKey = requestIdentity.idempotencyKey,
                    ownerUserId = ownerUserId,
                    draftJson = draftJson,
                ),
            )
        }
    }

    private fun requestContextWriter(epoch: Long): suspend (PersistedAiJobRequest) -> Unit = { request ->
        if (epoch == flowEpoch) saveRequestContext(request, epoch)
    }

    private suspend fun saveRequestContext(request: PersistedAiJobRequest, epoch: Long) {
        val ownerUserId = request.ownerUserId ?: return
        if (ownerUserId != userIdProvider() || request.targetSyllabusId != targetId) return
        pendingIdentity = request.toPendingIdentity()
        recoveredSourceTarget = AiReviewTarget(targetId, targetTitle, request.sourceUri, request.fileName, request.preferences, false, request.sourcePath, request.sourceHash, request.requestId)
        if (initialTarget?.preferences == null && preparedSource == null) request.preferences?.let { _preferences.value = it }
        targetStore?.save(AiReviewTarget(targetId, targetTitle, request.sourceUri, request.fileName, request.preferences, false, request.sourcePath, request.sourceHash, request.requestId), ownerUserId)
        if (epoch != flowEpoch || ownerUserId != userIdProvider()) return
        request.jobId?.let { jobId ->
            identity = request.toIdentity()
            _state.value = _state.value.copy(
                content = if (request.status == AiJobStatus.PROCESSING.name) AiReviewContent.Processing(jobId, request.idempotencyKey) else AiReviewContent.Submitting,
            )
        }
        val previous = sessions.load(targetId, ownerUserId)?.takeIf { it.requestId == request.requestId && it.jobId == request.jobId }
        if (epoch != flowEpoch || ownerUserId != userIdProvider()) return
        sessions.save(
            AiReviewPersistedSession(
                targetId = targetId,
                targetTitle = request.targetTitle ?: targetTitle,
                requestId = request.requestId,
                jobId = request.jobId.orEmpty(),
                idempotencyKey = request.idempotencyKey,
                ownerUserId = ownerUserId,
                draftJson = previous?.draftJson,
            ),
        )
    }

    private suspend fun saveApplied(requestIdentity: AiReviewRequestIdentity, draft: AiSyllabusDraft, outboxId: Long) {
        sessionMutex.withLock {
            val ownerUserId = requestIdentity.ownerUserId ?: return@withLock
            if (ownerUserId != userIdProvider()) return@withLock
            sessions.save(
                AiReviewPersistedSession(
                    targetId = targetId,
                    targetTitle = targetTitle,
                    requestId = requestIdentity.requestId,
                    jobId = requestIdentity.jobId,
                    idempotencyKey = requestIdentity.idempotencyKey,
                    ownerUserId = ownerUserId,
                    draftJson = AiReviewDraftCodec.encode(draft),
                    applied = true,
                    outboxId = outboxId,
                ),
            )
            hasAppliedLocally = true
            targetStore?.clear(ownerUserId)
        }
    }

    private fun watchSync(outboxId: Long) {
        viewModelScope.launch {
            while (true) {
                val state = syncState(outboxId) ?: break
                if (state != RemoteSyllabusSyncState.PENDING) {
                    _state.value = _state.value.copy(content = AiReviewContent.Applied(state))
                    break
                }
                delay(syncPollDelayMillis)
            }
        }
    }

    private fun fail(message: String) {
        _state.value = _state.value.copy(content = AiReviewContent.Failure(message, canRetry = identity != null || pendingIdentity != null || appliedReconciliationRequired))
    }

    private fun fail(error: Throwable) {
        val cause = (error as? AiReviewStartException)?.cause ?: error
        val quotaRejected = cause is AiApiException && cause.code in AI_QUOTA_REJECTION_CODES
        _state.value = _state.value.copy(content = AiReviewContent.Failure(
            safeMessage(cause), canRetry = !quotaRejected && (identity != null || pendingIdentity != null || appliedReconciliationRequired),
        ))
    }

    private fun safeMessage(error: Throwable): String = when (error) {
        is AiApiException -> if (error.code == "DEVICE_QUOTA_EXHAUSTED") {
            DEVICE_QUOTA_MESSAGE
        } else if (error.code == "QUOTA_EXHAUSTED") {
            "A cota de geração desta conta foi atingida. Confira seu plano e uso no Perfil."
        } else if (error.code == "INTEGRITY_REQUIRED" || error.code == "INTEGRITY_FAILED") {
            "Não foi possível confirmar que este é o app original da Google Play. Instale ou atualize o Estudário pela Play Store e tente novamente."
        } else if (error.code == "INTEGRITY_UNAVAILABLE") {
            "A verificação de segurança do Google está indisponível agora. Tente novamente em alguns minutos."
        } else if (error.code == "AI_RATE_LIMIT_EXCEEDED") {
            error.retryAfterSeconds?.takeIf { it > 0 }?.let { seconds ->
                "Muitas tentativas em pouco tempo. Tente novamente em ${seconds.coerceAtLeast(1)} segundos."
            } ?: "Muitas tentativas em pouco tempo. Tente novamente em alguns minutos."
        } else if (error.status >= 500) {
            "Não foi possível vincular o PDF agora. Tente novamente."
        } else error.message?.takeIf { it.isNotBlank() } ?: "Não foi possível processar este edital."
        else -> error.message?.takeIf { it.isNotBlank() } ?: "Não foi possível processar este edital."
    }

    private companion object {
        val TERMINAL_STATUSES = setOf(AiJobStatus.SUCCEEDED, AiJobStatus.FAILED, AiJobStatus.CANCELLED, AiJobStatus.EXPIRED)
        const val MAX_JOB_RECOVERY_RETRY_DELAY_MILLIS = 30_000L
    }
}

private fun AiApiException.isTransientForJobRecovery(): Boolean =
    code !in AI_QUOTA_REJECTION_CODES && (status == 0 || status == 408 || status == 425 || status == 429 || status >= 500 ||
        code == "NETWORK_UNAVAILABLE" || code == "HTTP_TIMEOUT")

class AiRecoveryAmbiguousException : IllegalStateException(
    "More than one unfinished AI request exists for this syllabus; automatic recovery was stopped.",
)

class AiReviewViewModelFactory(
    private val application: Application,
    private val targetId: Long,
    private val targetTitle: String,
    private val loginLauncher: AiReviewLoginLauncher = AiReviewLoginLauncher {},
    private val initialTarget: AiReviewTarget? = null,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val app = application as EstudarioApplication
        val requestStore = DataStoreAiJobRequestStore(app)
        val service = SyllabusApplicationService(app.database)
        return AiReviewViewModel(
            application = application,
            targetId = targetId,
            targetTitle = targetTitle,
            jobs = DefaultAiReviewJobs(app.aiSyllabusRepository, requestStore) { app.supabaseAuthRepository.currentUserId() },
            applier = object : AiReviewApplier {
                override suspend fun apply(targetId: Long, draft: AiSyllabusDraft, jobId: String, replaceExisting: Boolean) =
                    service.applyReviewedSyllabus(targetId, draft, jobId, replaceExisting)

                override suspend fun findApplied(targetId: Long, sourceJobId: String) =
                    service.findAppliedSyllabus(targetId, sourceJobId)
            },
            sessions = DataStoreAiReviewSessionStore(app),
            targetStore = DataStoreAiReviewTargetStore(app),
            accessGateway = DefaultAiReviewAccessGateway(app.aiAccessRepository),
            userIdProvider = { app.supabaseAuthRepository.currentUserId() },
            userIdFlow = app.supabaseAuthRepository.observeSession().let { states ->
                states.map { state ->
                    (state as? br.com.estudario.data.remote.SupabaseSessionState.Ready)
                        ?.session?.userId?.takeIf(String::isNotBlank)
                }
            },
            loginLauncher = loginLauncher,
            initialTarget = initialTarget,
            syncState = { id -> app.database.dao().remoteSyllabusSyncById(id)?.state },
        ) as T
    }
}

private fun PersistedAiJobRequest.toIdentity() = AiReviewRequestIdentity(
    requestId,
    jobId ?: error("jobId is missing"),
    idempotencyKey,
    ownerUserId,
)
private fun PersistedAiJobRequest.toPendingIdentity() = AiReviewPendingRequestIdentity(requestId, idempotencyKey, jobId, ownerUserId)

private object AiReviewDraftCodec {
    @Serializable private data class DraftPayload(val targetId: Long, val targetTitle: String, val titleOverride: String?, val sourceVersion: String, val sourcePromptVersion: String, val sourceModelVersion: String, val sourceSchemaVersion: Int, val sourceHash: String?, val documentTitle: String, val subjects: List<SubjectPayload>, val warnings: List<WarningPayload>, val ambiguities: List<String>)
    @Serializable private data class SubjectPayload(val name: String, val position: Int, val priority: String, val externalId: String, val sourcePages: List<Int>, val topics: List<TopicPayload>)
    @Serializable private data class TopicPayload(val name: String, val position: Int, val externalId: String, val sourcePages: List<Int>, val children: List<TopicPayload>)
    @Serializable private data class WarningPayload(val code: String, val severity: String, val message: String, val sourcePages: List<Int>, val ambiguity: String?)
    private val json = Json { encodeDefaults = true; ignoreUnknownKeys = true }

    fun encode(draft: AiSyllabusDraft): String = json.encodeToString(DraftPayload(
        draft.targetSyllabusId, draft.targetTitle, draft.titleOverride, draft.sourceVersion, draft.sourcePromptVersion, draft.sourceModelVersion, draft.sourceSchemaVersion, draft.sourceHash, draft.proposal.documentTitle,
        draft.subjects.map { subject -> SubjectPayload(subject.name, subject.position, subject.suggestedPriority.name, subject.externalId, subject.sourcePages, subject.topics.map { topic(it) }) },
        draft.warnings.map { WarningPayload(it.code.name, it.severity.name, it.message, it.sourcePages, it.ambiguity) }, draft.ambiguities,
    ))

    fun decode(raw: String): AiSyllabusDraft = json.decodeFromString<DraftPayload>(raw).let { payload ->
        val proposal = br.com.estudario.data.ai.AiSyllabusProposal(
            schemaVersion = payload.sourceSchemaVersion,
            promptVersion = payload.sourcePromptVersion,
            modelVersion = payload.sourceModelVersion,
            documentTitle = payload.documentTitle,
            subjects = payload.subjects.map { subject -> AiSubjectProposal(subject.name, subject.position, AiPriority.valueOf(subject.priority), subject.topics.map { proposalTopic(it) }, subject.sourcePages) },
            warnings = payload.warnings.map { AiWarning(AiWarningCode.valueOf(it.code), AiWarningSeverity.valueOf(it.severity), it.message, it.sourcePages, it.ambiguity) },
            ambiguities = payload.ambiguities,
        )
        AiSyllabusDraft(payload.targetId, proposal, payload.targetTitle, titleOverride = payload.titleOverride, subjects = payload.subjects.map { subject(it) }, warnings = proposal.warnings, ambiguities = payload.ambiguities, sourceVersion = payload.sourceVersion, sourcePromptVersion = payload.sourcePromptVersion, sourceModelVersion = payload.sourceModelVersion, sourceSchemaVersion = payload.sourceSchemaVersion, sourceHash = payload.sourceHash)
    }

    private fun topic(value: AiSyllabusDraftTopic): TopicPayload = TopicPayload(value.name, value.position, value.externalId, value.sourcePages, value.children.map { child: AiSyllabusDraftTopic -> topic(child) })
    private fun subject(value: SubjectPayload): AiSyllabusDraftSubject = AiSyllabusDraftSubject(value.name, value.position, AiPriority.valueOf(value.priority), value.topics.map { child: TopicPayload -> draftTopic(child) }, value.externalId, value.sourcePages)
    private fun draftTopic(value: TopicPayload): AiSyllabusDraftTopic = AiSyllabusDraftTopic(value.name, value.position, value.externalId, value.children.map { child: TopicPayload -> draftTopic(child) }, value.sourcePages)
    private fun proposalTopic(value: TopicPayload): AiTopicProposal = AiTopicProposal(value.name, value.position, value.children.map { child: TopicPayload -> proposalTopic(child) }, value.sourcePages)
}
