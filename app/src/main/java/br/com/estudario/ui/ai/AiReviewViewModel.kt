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
import br.com.estudario.data.ai.AiJob
import br.com.estudario.data.ai.AiApiException
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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
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
        AiReviewPendingRequestIdentity(identity.requestId, identity.idempotencyKey, identity.jobId),
        cause,
    )
}

interface AiReviewJobs {
    suspend fun start(targetId: Long, uri: String, fileName: String?): AiReviewStarted
    suspend fun recover(requestId: String): AiReviewStarted
    suspend fun retryFailed(requestId: String): AiReviewStarted
    suspend fun recoverPending(): List<AiReviewStarted>
    suspend fun identityForJob(jobId: String): AiReviewRequestIdentity?
}

class DefaultAiReviewJobs(
    private val repository: DefaultAiSyllabusRepository,
    private val requestStore: AiJobRequestStore,
) : AiReviewJobs {
    override suspend fun start(targetId: Long, uri: String, fileName: String?): AiReviewStarted {
        require(targetId > 0L) { "targetId must be positive" }
        val knownRequestIds = requestStore.list().mapTo(hashSetOf()) { it.requestId }
        return try {
            val job = repository.start(uri, fileName)
            started(job)
        } catch (timeout: AiProcessTimeoutException) {
            throw timeout
        } catch (error: Throwable) {
            val persisted = requestStore.list()
                .asSequence()
                .filterNot { it.requestId in knownRequestIds }
                .filter { it.sourceUri == uri && (fileName == null || it.fileName == fileName) }
                .maxByOrNull { it.updatedAtEpochMillis }
            throw AiReviewStartException(persisted?.toPendingIdentity(), error)
        }
    }

    override suspend fun recover(requestId: String): AiReviewStarted = started(repository.recover(requestId))

    override suspend fun retryFailed(requestId: String): AiReviewStarted = started(repository.retryFailed(requestId))

    override suspend fun recoverPending(): List<AiReviewStarted> = repository.recoverPendingJobs().map { started(it) }

    override suspend fun identityForJob(jobId: String): AiReviewRequestIdentity? = requestStore.list()
        .firstOrNull { it.jobId == jobId }
        ?.toIdentity()

    private suspend fun started(job: AiJob): AiReviewStarted {
        val request = requestStore.list().firstOrNull { it.jobId == job.jobId }
            ?: error("AI request metadata was not persisted for ${job.jobId}.")
        return AiReviewStarted(job, request.toIdentity())
    }
}

fun interface AiReviewApplier {
    suspend fun apply(targetId: Long, draft: AiSyllabusDraft, jobId: String, replaceExisting: Boolean): ApplyResult

    suspend fun findApplied(targetId: Long, sourceJobId: String): ApplyResult? = null
}

interface AiReviewSessionStore {
    suspend fun load(targetId: Long): AiReviewPersistedSession?
    suspend fun loadLatest(): AiReviewPersistedSession?
    suspend fun save(session: AiReviewPersistedSession)
    suspend fun clear(targetId: Long)
}

@Serializable
data class AiReviewPersistedSession(
    val targetId: Long,
    val targetTitle: String,
    val requestId: String,
    val jobId: String = "",
    val idempotencyKey: String,
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

    override suspend fun load(targetId: Long): AiReviewPersistedSession? = dataStore.data.first()[key]
        ?.let { runCatching { json.decodeFromString<List<AiReviewPersistedSession>>(it) }.getOrDefault(emptyList()) }
        ?.firstOrNull { it.targetId == targetId }

    override suspend fun loadLatest(): AiReviewPersistedSession? = dataStore.data.first()[key]
        ?.let { runCatching { json.decodeFromString<List<AiReviewPersistedSession>>(it) }.getOrDefault(emptyList()) }
        ?.lastOrNull()

    override suspend fun save(session: AiReviewPersistedSession) {
        dataStore.edit { preferences ->
            val sessions = preferences[key]?.let { runCatching { json.decodeFromString<List<AiReviewPersistedSession>>(it) }.getOrDefault(emptyList()) }.orEmpty()
            preferences[key] = json.encodeToString((sessions.filterNot { it.targetId == session.targetId } + session))
        }
    }

    override suspend fun clear(targetId: Long) {
        dataStore.edit { preferences ->
            val sessions = preferences[key]?.let { runCatching { json.decodeFromString<List<AiReviewPersistedSession>>(it) }.getOrDefault(emptyList()) }.orEmpty()
            preferences[key] = json.encodeToString(sessions.filterNot { it.targetId == targetId })
        }
    }
}

interface AiReviewTargetStore {
    suspend fun load(): AiReviewTarget?
    suspend fun save(target: AiReviewTarget)
    suspend fun clear()
}

class DataStoreAiReviewTargetStore(private val dataStore: DataStore<Preferences>) : AiReviewTargetStore {
    constructor(context: Context) : this(context.aiReviewTargetDataStore)

    private val key = stringPreferencesKey("target")
    private val json = Json { ignoreUnknownKeys = true; encodeDefaults = true }

    @Serializable
    private data class StoredTarget(
        val id: Long,
        val title: String,
        val sourceUri: String? = null,
        val sourceName: String? = null,
    )

    override suspend fun load(): AiReviewTarget? = dataStore.data.first()[key]
        ?.let { runCatching { json.decodeFromString<StoredTarget>(it) }.getOrNull() }
        ?.let { AiReviewTarget(it.id, it.title, it.sourceUri, it.sourceName) }

    override suspend fun save(target: AiReviewTarget) {
        dataStore.edit { preferences ->
            preferences[key] = json.encodeToString(StoredTarget(target.id, target.title, target.sourceUri, target.sourceName))
        }
    }

    override suspend fun clear() {
        dataStore.edit { preferences -> preferences.remove(key) }
    }
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
    private val loginLauncher: AiReviewLoginLauncher = AiReviewLoginLauncher {},
    private val syncState: suspend (Long) -> RemoteSyllabusSyncState? = { null },
    private val syncPollDelayMillis: Long = 1_000L,
) : AndroidViewModel(application) {
    private val _state = MutableStateFlow(AiReviewUiState.gate(targetId, targetTitle, AiReviewAccessState.LOADING))
    val state: StateFlow<AiReviewUiState> = _state.asStateFlow()
    private var identity: AiReviewRequestIdentity? = null
    private var pendingIdentity: AiReviewPendingRequestIdentity? = null
    private var pendingSource: AiReviewSource? = null
    private val startMutex = Mutex()
    private val sessionMutex = Mutex()
    private var hasAppliedLocally = false
    private var appliedReconciliationRequired = false

    init {
        viewModelScope.launch { refreshAccessAndRestore() }
    }

    fun requestLogin() = loginLauncher.launch(::onLoginReturned)

    fun onLoginReturned() {
        viewModelScope.launch { refreshAccessAndRestore() }
    }

    fun start(uri: String, fileName: String?) {
        provideSource(uri, fileName)
    }

    fun provideSource(uri: String, fileName: String?) {
        pendingSource = AiReviewSource(uri, fileName)
        viewModelScope.launch { startPendingIfReady() }
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
        if (appliedReconciliationRequired) {
            viewModelScope.launch { refreshAccessAndRestore() }
            return
        }
        val requestId = identity?.requestId ?: pendingIdentity?.requestId ?: return
        val terminalStatus = (_state.value.content as? AiReviewContent.Failure)?.terminalStatus
        viewModelScope.launch {
            runCatching {
                if (terminalStatus in RETRYABLE_TERMINAL_STATUSES) jobs.retryFailed(requestId)
                else jobs.recover(requestId)
            }
                .onSuccess { started -> identity = started.identity; render(started.job, started.identity) }
                .onFailure { error ->
                    _state.value = _state.value.copy(
                        content = AiReviewContent.Failure(
                            message = safeMessage(error),
                            terminalStatus = terminalStatus,
                        ),
                    )
                }
        }
    }

    fun useFallback() {
        viewModelScope.launch {
            sessionMutex.withLock {
                sessions.clear(targetId)
                hasAppliedLocally = false
            }
            identity = null
            pendingIdentity = null
            _state.value = AiReviewUiState.gate(targetId, targetTitle, _state.value.access)
        }
    }

    private fun apply(replaceExisting: Boolean) {
        val current = _state.value.content as? AiReviewContent.Review ?: return
        val requestIdentity = identity ?: return
        viewModelScope.launch {
            try {
                val result = applier.apply(targetId, current.draft, requestIdentity.jobId, replaceExisting)
                saveApplied(requestIdentity, current.draft, result.outboxId)
                _state.value = _state.value.copy(content = AiReviewContent.Applied(result.state))
                watchSync(result.outboxId)
            } catch (_: ExistingSyllabusContentException) {
                _state.value = _state.value.copy(content = current.copy(confirmReplacement = true))
            } catch (error: Throwable) {
                _state.value = _state.value.copy(content = current.copy(validationError = safeMessage(error)))
            }
        }
    }

    private suspend fun refreshAccessAndRestore() {
        val access = runCatching { accessGateway.check().toUiState() }
            .getOrElse { AiReviewAccessState.denied("ACCESS_UNAVAILABLE") }
        _state.value = _state.value.copy(access = access)
        if (access.kind != AiReviewAccessKind.READY) return
        val saved = sessions.load(targetId)
        if (saved?.applied == true) {
            appliedReconciliationRequired = false
            hasAppliedLocally = true
            val outboxId = saved.outboxId
            val restoredSyncState = outboxId?.let { runCatching { syncState(it) }.getOrNull() }
                ?: RemoteSyllabusSyncState.PENDING
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
                    fail(safeMessage(error))
                    return
                }
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
                    _state.value = _state.value.copy(content = AiReviewContent.Applied(restoredSyncState))
                    if (restoredSyncState == RemoteSyllabusSyncState.PENDING) watchSync(appliedResult.outboxId)
                    return
                }
                appliedReconciliationRequired = false
            }
            pendingIdentity = AiReviewPendingRequestIdentity(saved.requestId, saved.idempotencyKey, saved.jobId.takeIf { it.isNotBlank() })
            identity = pendingIdentity?.asStartedIdentity()
            if (identity != null) {
                _state.value = AiReviewUiState.processing(targetId, targetTitle, saved.jobId, saved.idempotencyKey, access)
            }
            runCatching { jobs.recover(saved.requestId) }
                .onSuccess { started -> identity = started.identity; render(started.job, started.identity, saved.draftJson) }
                .onFailure { fail(safeMessage(it)) }
        } else {
            startPendingIfReady()
        }
    }

    private suspend fun startPendingIfReady() {
        val source = pendingSource ?: return
        if (_state.value.access.kind != AiReviewAccessKind.READY) return
        startMutex.withLock {
            if (identity != null || pendingIdentity != null || _state.value.content is AiReviewContent.Processing || _state.value.content is AiReviewContent.Review) return
            try {
                targetStore?.save(AiReviewTarget(targetId, targetTitle, source.uri, source.fileName))
                val started = jobs.start(targetId, source.uri, source.fileName)
                identity = started.identity
                pendingIdentity = null
                pendingSource = null
                save(started.identity, null)
                render(started.job, started.identity)
            } catch (timeout: AiProcessTimeoutException) {
                val recoveredIdentity = jobs.identityForJob(timeout.jobId)
                if (recoveredIdentity != null) {
                    identity = recoveredIdentity
                    pendingIdentity = AiReviewPendingRequestIdentity(recoveredIdentity.requestId, recoveredIdentity.idempotencyKey, recoveredIdentity.jobId)
                    pendingSource = null
                    save(recoveredIdentity, null)
                    _state.value = AiReviewUiState.processing(targetId, targetTitle, recoveredIdentity.jobId, recoveredIdentity.idempotencyKey, _state.value.access)
                } else {
                    fail("A análise continua no servidor, mas não foi possível recuperar seus dados locais.")
                }
            } catch (startFailure: AiReviewStartException) {
                val recoveredIdentity = startFailure.identity
                val persistedIdentity = startFailure.pendingIdentity
                if (persistedIdentity != null) {
                    pendingIdentity = persistedIdentity
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
                } else if (persistedIdentity != null) {
                    pendingSource = null
                    savePending(persistedIdentity)
                    fail("A análise foi iniciada e será retomada com a mesma solicitação. Tente novamente.")
                } else {
                    fail("A análise foi iniciada e será retomada com a mesma solicitação. Tente novamente.")
                }
            } catch (error: Throwable) {
                fail(safeMessage(error))
            }
        }
    }

    private fun render(job: AiJob, requestIdentity: AiReviewRequestIdentity, persistedDraftJson: String? = null) {
        val content = when {
            job.status == AiJobStatus.SUCCEEDED && job.proposal != null -> AiReviewContent.Review(
                persistedDraftJson?.let { AiReviewDraftCodec.decode(it) } ?: AiSyllabusDraft.fromProposal(targetId, targetTitle, job.proposal),
            )
            job.status in RETRYABLE_TERMINAL_STATUSES -> AiReviewContent.Failure(
                job.errorMessage ?: "Não foi possível processar este edital.",
                terminalStatus = job.status,
            )
            else -> AiReviewContent.Processing(job.jobId, requestIdentity.idempotencyKey)
        }
        _state.value = _state.value.copy(content = content, access = AiReviewAccessState.READY)
        viewModelScope.launch { save(requestIdentity, (content as? AiReviewContent.Review)?.draft) }
    }

    private suspend fun save(requestIdentity: AiReviewRequestIdentity, draft: AiSyllabusDraft?) {
        pendingIdentity = AiReviewPendingRequestIdentity(requestIdentity.requestId, requestIdentity.idempotencyKey, requestIdentity.jobId)
        savePending(
            AiReviewPendingRequestIdentity(requestIdentity.requestId, requestIdentity.idempotencyKey, requestIdentity.jobId),
            draft?.let(AiReviewDraftCodec::encode),
        )
    }

    private suspend fun savePending(requestIdentity: AiReviewPendingRequestIdentity, draftJson: String? = null) {
        sessionMutex.withLock {
            if (hasAppliedLocally) return@withLock
            sessions.save(
                AiReviewPersistedSession(
                    targetId = targetId,
                    targetTitle = targetTitle,
                    requestId = requestIdentity.requestId,
                    jobId = requestIdentity.jobId.orEmpty(),
                    idempotencyKey = requestIdentity.idempotencyKey,
                    draftJson = draftJson,
                ),
            )
        }
    }

    private suspend fun saveApplied(requestIdentity: AiReviewRequestIdentity, draft: AiSyllabusDraft, outboxId: Long) {
        sessionMutex.withLock {
            sessions.save(
                AiReviewPersistedSession(
                    targetId = targetId,
                    targetTitle = targetTitle,
                    requestId = requestIdentity.requestId,
                    jobId = requestIdentity.jobId,
                    idempotencyKey = requestIdentity.idempotencyKey,
                    draftJson = AiReviewDraftCodec.encode(draft),
                    applied = true,
                    outboxId = outboxId,
                ),
            )
            hasAppliedLocally = true
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

    private fun fail(message: String) { _state.value = _state.value.copy(content = AiReviewContent.Failure(message)) }

    private fun safeMessage(error: Throwable): String = when (error) {
        is AiApiException -> if (error.code == "AI_RATE_LIMIT_EXCEEDED") {
            error.retryAfterSeconds?.takeIf { it > 0 }?.let { seconds ->
                "Muitas tentativas em pouco tempo. Tente novamente em ${seconds.coerceAtLeast(1)} segundos."
            } ?: "Muitas tentativas em pouco tempo. Tente novamente em alguns minutos."
        } else error.message?.takeIf { it.isNotBlank() } ?: "Não foi possível processar este edital."
        else -> error.message?.takeIf { it.isNotBlank() } ?: "Não foi possível processar este edital."
    }

    private companion object {
        val RETRYABLE_TERMINAL_STATUSES = setOf(AiJobStatus.FAILED, AiJobStatus.EXPIRED, AiJobStatus.CANCELLED)
    }
}

class AiReviewViewModelFactory(
    private val application: Application,
    private val targetId: Long,
    private val targetTitle: String,
    private val loginLauncher: AiReviewLoginLauncher = AiReviewLoginLauncher {},
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val app = application as EstudarioApplication
        val requestStore = DataStoreAiJobRequestStore(app)
        val service = SyllabusApplicationService(app.database)
        return AiReviewViewModel(
            application = application,
            targetId = targetId,
            targetTitle = targetTitle,
            jobs = DefaultAiReviewJobs(app.aiSyllabusRepository, requestStore),
            applier = object : AiReviewApplier {
                override suspend fun apply(targetId: Long, draft: AiSyllabusDraft, jobId: String, replaceExisting: Boolean) =
                    service.applyReviewedSyllabus(targetId, draft, jobId, replaceExisting)

                override suspend fun findApplied(targetId: Long, sourceJobId: String) =
                    service.findAppliedSyllabus(targetId, sourceJobId)
            },
            sessions = DataStoreAiReviewSessionStore(app),
            targetStore = DataStoreAiReviewTargetStore(app),
            accessGateway = DefaultAiReviewAccessGateway(app.aiAccessRepository),
            loginLauncher = loginLauncher,
            syncState = { id -> app.database.dao().remoteSyllabusSyncById(id)?.state },
        ) as T
    }
}

private fun PersistedAiJobRequest.toIdentity() = AiReviewRequestIdentity(requestId, jobId ?: error("jobId is missing"), idempotencyKey)
private fun PersistedAiJobRequest.toPendingIdentity() = AiReviewPendingRequestIdentity(requestId, idempotencyKey, jobId)

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
