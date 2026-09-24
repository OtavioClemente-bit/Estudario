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
import br.com.estudario.data.ai.AiJobRecoveryRepository
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
import java.util.UUID
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

data class AiReviewStarted(val job: AiJob, val identity: AiReviewRequestIdentity)

interface AiReviewJobs {
    suspend fun start(uri: String, fileName: String?): AiReviewStarted
    suspend fun recover(requestId: String): AiReviewStarted
    suspend fun recoverPending(): List<AiReviewStarted>
    suspend fun identityForJob(jobId: String): AiReviewRequestIdentity?
}

class DefaultAiReviewJobs(
    private val repository: DefaultAiSyllabusRepository,
    private val requestStore: AiJobRequestStore,
) : AiReviewJobs {
    override suspend fun start(uri: String, fileName: String?): AiReviewStarted = try {
        val job = repository.start(uri, fileName)
        started(job)
    } catch (timeout: AiProcessTimeoutException) {
        throw timeout
    }

    override suspend fun recover(requestId: String): AiReviewStarted = started(repository.recover(requestId))

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
    val jobId: String,
    val idempotencyKey: String,
    val draftJson: String? = null,
)

private val Context.aiReviewSessionDataStore: DataStore<Preferences> by preferencesDataStore(name = "ai_review_sessions")

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

class AiReviewViewModel(
    application: Application,
    private val targetId: Long,
    private val targetTitle: String,
    private val jobs: AiReviewJobs,
    private val applier: AiReviewApplier,
    private val sessions: AiReviewSessionStore,
    private val canUseAi: () -> Boolean,
    private val syncState: suspend (Long) -> RemoteSyllabusSyncState? = { null },
) : AndroidViewModel(application) {
    private val _state = MutableStateFlow(AiReviewUiState.gate(targetId, targetTitle))
    val state: StateFlow<AiReviewUiState> = _state.asStateFlow()
    private var identity: AiReviewRequestIdentity? = null

    init {
        viewModelScope.launch { restore() }
    }

    fun onLoginReturned() {
        viewModelScope.launch { restore() }
    }

    fun start(uri: String, fileName: String?) {
        if (!canUseAi()) return
        viewModelScope.launch {
            try {
                val started = jobs.start(uri, fileName)
                identity = started.identity
                save(started.identity, null)
                render(started.job, started.identity)
            } catch (timeout: AiProcessTimeoutException) {
                val recoveredIdentity = jobs.identityForJob(timeout.jobId)
                if (recoveredIdentity != null) {
                    identity = recoveredIdentity
                    save(recoveredIdentity, null)
                    _state.value = AiReviewUiState.processing(targetId, targetTitle, recoveredIdentity.jobId, recoveredIdentity.idempotencyKey)
                } else {
                    fail("A análise continua no servidor, mas não foi possível recuperar seus dados locais.")
                }
            } catch (error: Throwable) {
                fail(safeMessage(error))
            }
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
        val current = identity ?: return
        viewModelScope.launch {
            runCatching { jobs.recover(current.requestId) }
                .onSuccess { started -> identity = started.identity; render(started.job, started.identity) }
                .onFailure { fail(safeMessage(it)) }
        }
    }

    fun useFallback() {
        viewModelScope.launch {
            sessions.clear(targetId)
            _state.value = AiReviewUiState.gate(targetId, targetTitle)
        }
    }

    private fun apply(replaceExisting: Boolean) {
        val current = _state.value.content as? AiReviewContent.Review ?: return
        val source = identity?.jobId ?: return
        viewModelScope.launch {
            try {
                val result = applier.apply(targetId, current.draft, source, replaceExisting)
                _state.value = _state.value.copy(content = AiReviewContent.Applied(result.state))
                watchSync(result.outboxId)
            } catch (_: ExistingSyllabusContentException) {
                _state.value = _state.value.copy(content = current.copy(confirmReplacement = true))
            } catch (error: Throwable) {
                _state.value = _state.value.copy(content = current.copy(validationError = safeMessage(error)))
            }
        }
    }

    private suspend fun restore() {
        if (!canUseAi()) return
        val saved = sessions.load(targetId) ?: return
        identity = AiReviewRequestIdentity(saved.requestId, saved.jobId, saved.idempotencyKey)
        _state.value = AiReviewUiState.processing(targetId, targetTitle, saved.jobId, saved.idempotencyKey)
        runCatching { jobs.recover(saved.requestId) }
            .onSuccess { started -> identity = started.identity; render(started.job, started.identity, saved.draftJson) }
            .onFailure { fail(safeMessage(it)) }
    }

    private fun render(job: AiJob, requestIdentity: AiReviewRequestIdentity, persistedDraftJson: String? = null) {
        val content = when {
            job.status == AiJobStatus.SUCCEEDED && job.proposal != null -> AiReviewContent.Review(
                persistedDraftJson?.let { AiReviewDraftCodec.decode(it) } ?: AiSyllabusDraft.fromProposal(targetId, targetTitle, job.proposal),
            )
            job.status in setOf(AiJobStatus.FAILED, AiJobStatus.EXPIRED, AiJobStatus.CANCELLED) -> AiReviewContent.Failure(job.errorMessage ?: "Não foi possível processar este edital.")
            else -> AiReviewContent.Processing(job.jobId, requestIdentity.idempotencyKey)
        }
        _state.value = _state.value.copy(content = content)
        viewModelScope.launch { save(requestIdentity, (content as? AiReviewContent.Review)?.draft) }
    }

    private suspend fun save(requestIdentity: AiReviewRequestIdentity, draft: AiSyllabusDraft?) {
        sessions.save(
            AiReviewPersistedSession(
                targetId = targetId,
                targetTitle = targetTitle,
                requestId = requestIdentity.requestId,
                jobId = requestIdentity.jobId,
                idempotencyKey = requestIdentity.idempotencyKey,
                draftJson = draft?.let(AiReviewDraftCodec::encode),
            ),
        )
    }

    private fun watchSync(outboxId: Long) {
        viewModelScope.launch {
            while (true) {
                val state = syncState(outboxId) ?: break
                if (state != RemoteSyllabusSyncState.PENDING) {
                    _state.value = _state.value.copy(content = AiReviewContent.Applied(state))
                    break
                }
                delay(1_000)
            }
        }
    }

    private fun fail(message: String) { _state.value = _state.value.copy(content = AiReviewContent.Failure(message)) }

    private fun safeMessage(error: Throwable): String = error.message?.takeIf { it.isNotBlank() } ?: "Não foi possível processar este edital."
}

class AiReviewViewModelFactory(
    private val application: Application,
    private val targetId: Long,
    private val targetTitle: String,
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
            applier = AiReviewApplier { id, draft, jobId, replace -> service.applyReviewedSyllabus(id, draft, jobId, replace) },
            sessions = DataStoreAiReviewSessionStore(app),
            canUseAi = { app.supabaseClientConfig.isConfigured && app.supabaseAuthRepository.accessToken() != null },
            syncState = { id -> app.database.dao().remoteSyllabusSyncById(id)?.state },
        ) as T
    }
}

private fun PersistedAiJobRequest.toIdentity() = AiReviewRequestIdentity(requestId, jobId ?: error("jobId is missing"), idempotencyKey)

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
