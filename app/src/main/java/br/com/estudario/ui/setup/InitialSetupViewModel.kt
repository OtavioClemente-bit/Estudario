package br.com.estudario.ui.setup

import kotlinx.datetime.toKotlinLocalDate
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import br.com.estudario.EstudarioApplication
import br.com.estudario.data.local.CompetitionEntity
import br.com.estudario.data.local.TopicEntity
import androidx.room.withTransaction
import br.com.estudario.data.local.SubjectEntity
import br.com.estudario.data.local.planner.AvailabilityMode
import br.com.estudario.data.local.planner.StudyAvailabilityEntity
import br.com.estudario.data.planner.CreatePlanInput
import br.com.estudario.data.planner.PlanSubjectInput
import br.com.estudario.data.transfer.EstudoPackageService
import br.com.estudario.data.transfer.EstudoPreview
import br.com.estudario.data.transfer.ImportMode
import br.com.estudario.data.transfer.planner.PlanImportMode
import br.com.estudario.data.transfer.planner.StudyPlanCodec
import br.com.estudario.data.transfer.planner.StudyPlanImportPreview
import br.com.estudario.data.prompt.PromptIds
import br.com.estudario.data.prompt.PlanSubjectInfo
import br.com.estudario.domain.planner.InitialKnowledge
import br.com.estudario.domain.planner.PersonalDifficulty
import br.com.estudario.domain.planner.PlanPriority
import br.com.estudario.domain.planner.ReplanReason
import br.com.estudario.domain.planner.StudyMethodConfig
import br.com.estudario.domain.planner.rotationWeight
import br.com.estudario.domain.planner.toExamPriority
import br.com.estudario.domain.setup.InitialSetupSnapshot
import br.com.estudario.domain.setup.InitialSetupStatus
import br.com.estudario.domain.setup.InitialSetupStep
import br.com.estudario.domain.setup.InitialSetupTransitions
import br.com.estudario.domain.setup.InitialSetupWorkspace
import br.com.estudario.domain.setup.MissingTopic
import br.com.estudario.domain.setup.PlanCoverageResult
import br.com.estudario.domain.setup.PlanCoverageSubject
import br.com.estudario.domain.setup.PlanCoverageTask
import br.com.estudario.domain.setup.PlanCoverageTopic
import br.com.estudario.domain.setup.PlanCoverageValidator
import br.com.estudario.domain.setup.PlanCreationMethod
import br.com.estudario.domain.setup.SyllabusMethod
import br.com.estudario.domain.setup.SubjectVariety
import br.com.estudario.ui.ai.AiReviewTarget
import br.com.estudario.domain.planner.ExamPriority
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate

sealed interface SetupOperation {
    data object Idle : SetupOperation
    data object Loading : SetupOperation
    data class Preview(val raw: String, val value: EstudoPreview) : SetupOperation
    data class PlanPreview(val raw: String, val value: StudyPlanImportPreview) : SetupOperation
    data class PlanCoverageError(val raw: String, val result: PlanCoverageResult) : SetupOperation
    data class Success(val message: String) : SetupOperation
    data class Error(val message: String) : SetupOperation
}

data class InitialSetupUiState(
    val snapshot: InitialSetupSnapshot = InitialSetupSnapshot(),
    val competition: CompetitionEntity? = null,
    val competitions: List<CompetitionEntity> = emptyList(),
    val subjects: List<SubjectEntity> = emptyList(),
    val topicEntities: List<TopicEntity> = emptyList(),
    val topicCount: Int = 0,
    val topicTitlesBySubject: Map<Long, List<String>> = emptyMap(),
    val promptSubjects: List<PlanSubjectInfo> = emptyList(),
    val officialPrioritiesBySubjectId: Map<Long, PlanPriority> = emptyMap(),
    val planningPrioritiesBySubjectId: Map<Long, PlanPriority> = emptyMap(),
    val planningPrioritiesByExternalId: Map<String, PlanPriority> = emptyMap(),
)

/** Orquestra a primeira configuração e mantém a lógica de dados fora das telas Compose. */
class InitialSetupViewModel(application: Application) : AndroidViewModel(application) {
    private val app = application as EstudarioApplication
    private val repository = app.repository
    private val dao = app.database.dao()
    private val estudoService = EstudoPackageService(app.database)

    private val syllabusState = combine(
        app.preferences.initialSetup,
        repository.competitions,
        repository.subjects,
        repository.topics,
    ) { snapshot, competitions, subjects, topics ->
        val competition = snapshot.competitionId?.let { id -> competitions.firstOrNull { it.id == id } }
            ?: competitions.firstOrNull { it.isPrimary }
            ?: competitions.firstOrNull { it.name.equals(snapshot.competitionName, ignoreCase = true) }
        val scopedSubjects = competition?.let { current -> subjects.filter { it.competitionId == current.id } }.orEmpty()
        val scopedSubjectIds = scopedSubjects.mapTo(hashSetOf()) { it.id }
        InitialSetupUiState(
            snapshot = snapshot,
            competition = competition,
            competitions = competitions,
            subjects = scopedSubjects,
            topicEntities = topics.filter { it.subjectId in scopedSubjectIds },
            topicCount = topics.count { it.subjectId in scopedSubjectIds },
            topicTitlesBySubject = topics.filter { it.subjectId in scopedSubjectIds }.groupBy { it.subjectId }.mapValues { (_, values) -> values.map { it.title } },
        )
    }

    val state: StateFlow<InitialSetupUiState> = combine(syllabusState, dao.attempts(), dao.questions()) { base, attempts, questions ->
        val planData = InitialSetupPlanMapper.map(base.competition, base.subjects, base.topicEntities, questions, attempts, base.snapshot)
        base.copy(
            promptSubjects = planData.promptSubjects,
            officialPrioritiesBySubjectId = planData.officialPrioritiesBySubjectId,
            planningPrioritiesBySubjectId = planData.planningPrioritiesBySubjectId,
            planningPrioritiesByExternalId = planData.planningPrioritiesByExternalId,
        )
    }.stateIn(viewModelScope, SharingStarted.Eagerly, InitialSetupUiState())

    private val _operation = MutableStateFlow<SetupOperation>(SetupOperation.Idle)
    val operation: StateFlow<SetupOperation> = _operation.asStateFlow()

    /**
     * O PDF do edital anexado no começo. Mora aqui, e não na tela, porque a tela do assistente sai
     * de composição quando a IA do Estudário abre; guardado na tela, o anexo se perdia e a pessoa
     * tinha que escolher o mesmo PDF de novo.
     */
    private val _editalAttachment = MutableStateFlow<br.com.estudario.ui.prompt.PromptAttachment?>(null)
    val editalAttachment: StateFlow<br.com.estudario.ui.prompt.PromptAttachment?> = _editalAttachment.asStateFlow()
    fun setEditalAttachment(value: br.com.estudario.ui.prompt.PromptAttachment?) {
        _editalAttachment.value = value
        checkEditalAttachment(value)
    }

    // --- Conferência do PDF anexado ---------------------------------------------------------------

    private val _attachmentCheck = MutableStateFlow<EditalAttachmentCheck>(EditalAttachmentCheck.None)
    /** Resultado da leitura do PDF no próprio aparelho: avisa logo se não parece o edital certo. */
    val attachmentCheck: StateFlow<EditalAttachmentCheck> = _attachmentCheck.asStateFlow()
    private var attachmentCheckJob: kotlinx.coroutines.Job? = null
    /** Texto das páginas do último PDF lido: mudar nome ou cargo refaz só a conferência, sem reler o arquivo. */
    private var attachmentPages: Pair<android.net.Uri, List<String>>? = null
    private var draftName: String = ""
    private var draftRole: String = ""

    private fun checkEditalAttachment(value: br.com.estudario.ui.prompt.PromptAttachment?) {
        attachmentCheckJob?.cancel()
        if (value == null) {
            attachmentPages = null
            _attachmentCheck.value = EditalAttachmentCheck.None
            return
        }
        if (attachmentPages?.first != value.uri) _attachmentCheck.value = EditalAttachmentCheck.Checking
        attachmentCheckJob = viewModelScope.launch {
            val snapshot = app.preferences.initialSetup.first()
            _attachmentCheck.value = try {
                val pages = attachmentPages?.takeIf { it.first == value.uri }?.second ?: withContext(Dispatchers.IO) {
                    br.com.estudario.data.ai.EditalPdfText.init(app)
                    val bytes = app.contentResolver.openInputStream(value.uri)?.use { it.readBytes() }
                        ?: error("Não consegui abrir o PDF.")
                    br.com.estudario.data.ai.EditalPdfText.pages(bytes)
                }.also { attachmentPages = value.uri to it }
                val result = withContext(Dispatchers.Default) {
                    br.com.estudario.data.ai.SyllabusPreflight.inspect(
                        pages,
                        br.com.estudario.data.ai.AiSyllabusPreferences(
                            competitionName = draftName.ifBlank { snapshot.competitionName },
                            role = draftRole.ifBlank { snapshot.role },
                        ),
                    )
                }
                EditalAttachmentCheck.Done(result)
            } catch (error: kotlinx.coroutines.CancellationException) {
                throw error
            } catch (error: Throwable) {
                EditalAttachmentCheck.Unreadable
            }
        }
    }

    /** O nome e o cargo que a pessoa está digitando: o aviso de "outro concurso" depende deles. */
    fun updateDraftIdentity(name: String, role: String) {
        if (name.trim() == draftName && role.trim() == draftRole) return
        draftName = name.trim()
        draftRole = role.trim()
        if (_attachmentCheck.value is EditalAttachmentCheck.Done) checkEditalAttachment(_editalAttachment.value)
    }

    // --- Catálogo de editais --------------------------------------------------------------------

    private val catalog = br.com.estudario.data.catalog.ExamCatalogProvider.get(application)
    private val _catalogState = MutableStateFlow(ExamCatalogUiState())
    val catalogState: StateFlow<ExamCatalogUiState> = _catalogState.asStateFlow()

    /** Mostra na hora a última lista guardada e atualiza pela rede em seguida. */
    fun loadCatalog(force: Boolean = false) = viewModelScope.launch {
        if (!catalog.isConfigured) return@launch
        if (_catalogState.value.entries.isEmpty()) {
            catalog.cached()?.let { cached -> _catalogState.value = _catalogState.value.copy(entries = cached) }
        }
        _catalogState.value = _catalogState.value.copy(loading = true, error = null)
        _catalogState.value = try {
            _catalogState.value.copy(entries = catalog.entries(force = force || _catalogState.value.entries.isNotEmpty()), loading = false)
        } catch (error: kotlinx.coroutines.CancellationException) {
            throw error
        } catch (error: Exception) {
            _catalogState.value.copy(loading = false, error = error.message)
        }
    }

    /**
     * Monta o edital escolhido no catálogo: cria (ou reaproveita) o concurso com o nome e o cargo do
     * edital e grava matérias e tópicos na ordem oficial, sem IA. Itens que já existem não repetem.
     */
    fun chooseCatalogExam(entry: br.com.estudario.data.catalog.ExamCatalogEntry) = viewModelScope.launch {
        _operation.value = SetupOperation.Loading
        try {
            val subjects = catalog.subjects(entry.id)
            val current = app.preferences.initialSetup.first()
            val competitions = dao.competitionsOnce()
            val draft = current.competitionId?.let { id -> competitions.firstOrNull { it.id == id } }
            // Concurso com o mesmo nome e matérias de outra origem não é misturado com o edital do
            // catálogo: o novo ganha o cargo no nome. Escolher de novo o mesmo edital só completa.
            val sameExamAgain = current.catalogExamId == entry.id && draft != null
            val byName = competitions.firstOrNull { it.name.equals(entry.shortName, ignoreCase = true) }
            val fullName = "${entry.shortName} · ${entry.role}".take(120)
            val competitionId = when {
                sameExamAgain -> draft!!.id
                draft != null && dao.subjectsFor(draft.id).isEmpty() ->
                    draft.id.also { dao.updateCompetition(draft.copy(name = byName?.let { fullName } ?: entry.shortName)) }
                byName != null && dao.subjectsFor(byName.id).isEmpty() -> byName.id
                byName != null -> competitions.firstOrNull { it.name.equals(fullName, ignoreCase = true) }?.id
                    ?: repository.addCompetition(fullName)
                else -> repository.addCompetition(entry.shortName)
            }
            val competitionName = dao.competitionsOnce().first { it.id == competitionId }.name
            app.database.withTransaction {
                val existingSubjects = dao.subjectsFor(competitionId).associateBy { it.name.lowercase() }.toMutableMap()
                subjects.forEach { subject ->
                    val subjectId = existingSubjects[subject.name.lowercase()]?.id
                        ?: repository.addSubject(competitionId, subject.name).also { id ->
                            existingSubjects[subject.name.lowercase()] = SubjectEntity(id = id, competitionId = competitionId, name = subject.name)
                        }
                    val existingTopics = dao.topicsFor(subjectId).mapTo(hashSetOf()) { it.title.lowercase() }
                    subject.topics.forEach { title -> if (existingTopics.add(title.lowercase())) repository.addTopic(subjectId, title) }
                }
            }
            repository.setPrimary(competitionId)
            _editalAttachment.value = null
            _attachmentCheck.value = EditalAttachmentCheck.None
            app.preferences.updateInitialSetup {
                it.copy(
                    status = InitialSetupStatus.IN_PROGRESS,
                    step = InitialSetupStep.EXAM_DATE,
                    competitionId = competitionId,
                    competitionName = competitionName,
                    role = entry.role,
                    syllabusMethod = SyllabusMethod.CATALOG,
                    catalogExamId = entry.id,
                )
            }
            _operation.value = SetupOperation.Success("Edital carregado: ${subjects.size} matérias e ${subjects.sumOf { it.topics.size }} tópicos.")
        } catch (error: kotlinx.coroutines.CancellationException) {
            throw error
        } catch (error: Exception) {
            _operation.value = SetupOperation.Error(error.message ?: "Não foi possível carregar este edital.")
        }
    }

    fun selectedAiTarget(): AiReviewTarget? = state.value.competition?.let { competition ->
        val snapshot = state.value.snapshot
        AiReviewTarget(competition.id, competition.name, preferences = br.com.estudario.data.ai.AiSyllabusPreferences(
            competitionName = competition.name,
            role = snapshot.role.takeIf { snapshot.competitionId == competition.id }.orEmpty(),
        ))
    }

    /** Called only after the AI proposal was applied locally and its outbox row was created. */
    fun onAiSyllabusApplied() = viewModelScope.launch {
        app.preferences.updateInitialSetup {
            it.copy(
                status = InitialSetupStatus.IN_PROGRESS,
                step = InitialSetupStep.SYLLABUS_REVIEW,
                syllabusMethod = SyllabusMethod.DIRECT_AI,
            )
        }
    }

    fun begin(reopen: Boolean = false) = viewModelScope.launch {
        app.preferences.updateInitialSetup { current ->
            val step = when {
                current.status == InitialSetupStatus.COMPLETED && reopen -> InitialSetupStep.COMPETITION
                current.step == InitialSetupStep.READY -> InitialSetupStep.PLAN_REVIEW
                else -> current.step
            }
            current.copy(status = InitialSetupStatus.IN_PROGRESS, step = step)
        }
    }

    fun bypassForExistingWorkspace() = viewModelScope.launch {
        app.preferences.updateInitialSetup { it.copy(status = InitialSetupStatus.COMPLETED, step = InitialSetupStep.READY) }
    }

    fun defer() = viewModelScope.launch {
        app.preferences.updateInitialSetup { it.copy(status = InitialSetupStatus.DEFERRED) }
    }

    fun saveCompetition(name: String, role: String) = viewModelScope.launch {
        val cleanName = name.trim()
        if (cleanName.isBlank()) return@launch
        val current = app.preferences.initialSetup.first()
        val existing = dao.competitionsOnce().firstOrNull { it.name.equals(cleanName, ignoreCase = true) }
        val draftCompetition = current.competitionId?.let { id -> dao.competitionsOnce().firstOrNull { it.id == id } }
        val id = draftCompetition?.id ?: existing?.id ?: repository.addCompetition(cleanName)
        if (draftCompetition != null && !draftCompetition.name.equals(cleanName, ignoreCase = true)) {
            dao.updateCompetition(draftCompetition.copy(name = cleanName))
        }
        if (existing == null && current.competitionId == null) dao.competitionsOnce().firstOrNull { it.id == id }?.let { repository.setPrimary(it.id) }
        app.preferences.setInitialSetup(
            current.copy(
                status = InitialSetupStatus.IN_PROGRESS,
                step = InitialSetupStep.EXAM_DATE,
                competitionId = id,
                competitionName = cleanName,
                role = role.trim(),
                // Digitado à mão: o edital passa a vir do PDF, de arquivo ou da montagem manual.
                catalogExamId = null,
                syllabusMethod = current.syllabusMethod.takeUnless { it == SyllabusMethod.CATALOG },
            ),
        )
    }

    fun selectCompetition(competition: CompetitionEntity) = viewModelScope.launch {
        if (state.value.snapshot.competitionId != competition.id) _editalAttachment.value = null
        app.preferences.updateInitialSetup {
            it.copy(
                status = InitialSetupStatus.IN_PROGRESS,
                step = InitialSetupStep.EXAM_DATE,
                competitionId = competition.id,
                competitionName = competition.name,
                role = it.role.takeIf { _ -> it.competitionId == competition.id }.orEmpty(),
                catalogExamId = it.catalogExamId.takeIf { _ -> it.competitionId == competition.id },
            )
        }
        repository.setPrimary(competition.id)
    }

    fun saveExamDate(value: String?) = viewModelScope.launch {
        val clean = value?.trim()?.takeIf(String::isNotBlank)
        val date = clean?.let { runCatching { LocalDate.parse(it) }.getOrNull() }
        if (clean != null && (date == null || date.isBefore(LocalDate.now()))) {
            _operation.value = SetupOperation.Error("A data da prova precisa ser hoje ou uma data futura.")
            return@launch
        }
        // Edital do catálogo já está gravado: vai direto para a conferência das matérias.
        val competitionId = state.value.snapshot.competitionId
        val catalogReady = state.value.snapshot.syllabusMethod == SyllabusMethod.CATALOG &&
            competitionId != null && dao.subjectsFor(competitionId).isNotEmpty()
        app.preferences.updateInitialSetup {
            it.copy(examDate = clean, step = if (catalogReady) InitialSetupStep.SYLLABUS_REVIEW else InitialSetupStep.SYLLABUS_METHOD)
        }
    }
    fun chooseSyllabusMethod(value: SyllabusMethod) = update { it.copy(syllabusMethod = value) }
    fun saveManualSubjects(value: List<String>) = update { it.copy(manualSubjects = value) }
    fun saveManualTopics(value: Map<String, List<String>>) = update { it.copy(manualTopics = value) }
    fun renameManualSubject(oldName: String, newName: String) = update { current ->
        val old = oldName.trim()
        val new = newName.trim()
        if (old.isBlank() || new.isBlank() || old.equals(new, ignoreCase = true)) current
        else {
            val subjects = current.manualSubjects.map { if (it.equals(old, ignoreCase = true)) new else it }
            val topics = current.manualTopics.toMutableMap().apply {
                val oldEntry = entries.firstOrNull { it.key.equals(old, ignoreCase = true) }
                val oldTopics = oldEntry?.value
                oldEntry?.key?.let(::remove)
                if (oldTopics != null) put(new, oldTopics)
            }
            current.copy(manualSubjects = subjects, manualTopics = topics)
        }
    }
    fun deleteManualSubject(subjectName: String) = update { current ->
        val subject = subjectName.trim()
        current.copy(
            manualSubjects = current.manualSubjects.filterNot { it.equals(subject, ignoreCase = true) },
            manualTopics = current.manualTopics.filterKeys { !it.equals(subject, ignoreCase = true) },
        )
    }
    fun addManualTopic(subjectName: String, topic: String) = update { current ->
        val subject = subjectName.trim()
        val title = topic.trim()
        if (subject.isBlank() || title.isBlank()) current
        else current.copy(manualTopics = current.manualTopics.toMutableMap().apply {
            val existing = entries.firstOrNull { it.key.equals(subject, ignoreCase = true) }
            val key = existing?.key ?: subject
            val topics = existing?.value.orEmpty()
            if (topics.none { it.equals(title, ignoreCase = true) }) put(key, topics + title)
        })
    }
    fun renameManualTopic(subjectName: String, index: Int, newTitle: String) = update { current ->
        val subject = subjectName.trim()
        val title = newTitle.trim()
        val entry = current.manualTopics.entries.firstOrNull { it.key.equals(subject, ignoreCase = true) }
        if (entry == null || title.isBlank() || index !in entry.value.indices) current
        else current.copy(manualTopics = current.manualTopics.toMutableMap().apply {
            val topics = entry.value.toMutableList()
            if (topics.withIndex().any { it.index != index && it.value.equals(title, ignoreCase = true) }) return@update current
            topics[index] = title
            put(entry.key, topics)
        })
    }
    fun deleteManualTopic(subjectName: String, index: Int) = update { current ->
        val entry = current.manualTopics.entries.firstOrNull { it.key.equals(subjectName.trim(), ignoreCase = true) }
            ?: return@update current
        if (index !in entry.value.indices) current
        else current.copy(manualTopics = current.manualTopics.toMutableMap().apply {
            put(entry.key, entry.value.toMutableList().apply { removeAt(index) })
        })
    }
    fun chooseProfile(value: br.com.estudario.domain.planner.StudyProfile) = update { it.copy(studyProfile = value) }
    // --- Os três eixos ------------------------------------------------------------------------
    // Cada um tem seu próprio setter e seu próprio mapa. Não existe um caminho no código em que
    // responder sobre dificuldade altere a prioridade da prova, ou vice-versa.

    /** Eixo 1: a pessoa discorda do peso que o edital deu à matéria e corrige à mão. */
    fun setSubjectPriority(subjectId: String, value: ExamPriority) = update { current ->
        if (subjectId.isBlank()) current else current.copy(subjectPriorities = current.subjectPriorities + (subjectId to value))
    }

    /** Volta a matéria para a prioridade que veio do edital, removendo o ajuste manual. */
    fun clearSubjectPriority(subjectId: String) = update { current ->
        current.copy(subjectPriorities = current.subjectPriorities - subjectId)
    }

    /** Eixo 2: quanto a matéria custa para esta pessoa. */
    fun setSubjectDifficulty(subjectId: String, value: PersonalDifficulty) = update { current ->
        if (subjectId.isBlank()) current else current.copy(subjectDifficulties = current.subjectDifficulties + (subjectId to value))
    }

    /** Eixo 3: quanto ela já sabia ao começar. */
    fun setSubjectKnowledge(subjectId: String, value: InitialKnowledge) = update { current ->
        if (subjectId.isBlank()) current else current.copy(subjectKnowledge = current.subjectKnowledge + (subjectId to value))
    }

    fun chooseVariety(value: SubjectVariety) = update { it.copy(variety = value) }

    fun reconcileSubjectDifficulties(subjectIds: Set<String>) = update { current ->
        current.copy(
            subjectDifficulties = current.subjectDifficultiesFor(subjectIds),
            subjectKnowledge = current.subjectKnowledge.filterKeys { it in subjectIds },
            subjectPriorities = current.subjectPriorities.filterKeys { it in subjectIds },
        )
    }
    fun chooseSessionMinutes(value: Int) = update { it.copy(sessionMinutes = value.coerceIn(15, 180)) }
    fun choosePlanMethod(value: PlanCreationMethod) = update { it.copy(planMethod = value) }
    fun savePlanPreference(value: String) = update { it.copy(planPreference = value) }

    fun setAvailability(dayIndex: Int, minutes: Int) = update { current ->
        val values = current.availabilityMinutes.toMutableList().apply { this[dayIndex.coerceIn(0, 6)] = minutes.coerceIn(0, 1_440) }
        current.copy(availabilityMinutes = values)
    }

    /**
     * Volta a um passo já respondido, sem perder nada do que foi coletado.
     *
     * É o que permite ao resumo dizer "sua agenda está apertada" e oferecer o caminho de volta à
     * disponibilidade: a pessoa ajusta a carga e o resumo se refaz com o número novo.
     */
    fun jumpTo(step: InitialSetupStep) = viewModelScope.launch {
        app.preferences.updateInitialSetup { it.copy(status = InitialSetupStatus.IN_PROGRESS, step = step) }
    }

    fun advance(from: InitialSetupStep, to: InitialSetupStep) = viewModelScope.launch {
        val current = app.preferences.initialSetup.first()
        val subjectIds = current.competitionId?.let { id -> dao.subjectsFor(id).mapTo(hashSetOf()) { it.id.toString() } }.orEmpty()
        app.preferences.updateInitialSetup {
            if (it.step == from && InitialSetupTransitions.canAdvance(it, to, subjectIds)) {
                it.copy(status = InitialSetupStatus.IN_PROGRESS, step = to)
            } else it
        }
    }

    fun addReviewSubject(name: String) = editSyllabus { competitionId ->
        val clean = name.trim()
        require(clean.isNotBlank()) { "Digite o nome da matéria." }
        require(dao.subjectsFor(competitionId).none { it.name.equals(clean, true) }) { "Esta matéria já está no edital." }
        repository.addSubject(competitionId, clean)
    }

    fun addReviewTopic(subjectId: Long, title: String) = editSyllabus { competitionId ->
        require(dao.subjectsFor(competitionId).any { it.id == subjectId }) { "Esta matéria não está mais no edital." }
        val clean = title.trim()
        require(clean.isNotBlank()) { "Digite o nome do tópico." }
        require(dao.topicsFor(subjectId).none { it.title.equals(clean, true) }) { "Este tópico já está na matéria." }
        repository.addTopic(subjectId, clean)
    }

    fun removeReviewSubject(subject: SubjectEntity) = editSyllabus { competitionId ->
        dao.subjectsFor(competitionId).firstOrNull { it.id == subject.id }?.let { repository.deleteSubject(it) }
    }

    fun removeReviewTopic(topic: TopicEntity) = editSyllabus { competitionId ->
        if (dao.subjectsFor(competitionId).any { it.id == topic.subjectId }) {
            dao.topic(topic.id)?.let { repository.deleteTopic(it) }
        }
    }

    private fun editSyllabus(edit: suspend (Long) -> Unit) = viewModelScope.launch {
        try {
            val competitionId = ensureCompetition(app.preferences.initialSetup.first())
            edit(competitionId)
            val subjects = dao.subjectsFor(competitionId)
            val topics = subjects.associate { subject -> subject.name to dao.topicsFor(subject.id).map { it.title } }
            app.preferences.updateInitialSetup { current ->
                val ids = subjects.mapTo(hashSetOf()) { it.id.toString() }
                current.copy(
                    subjectDifficulties = current.subjectDifficultiesFor(ids),
                    subjectKnowledge = current.subjectKnowledge.filterKeys { it in ids },
                    subjectPriorities = current.subjectPriorities.filterKeys { it in ids },
                    // Mantém a edição ao voltar à entrada manual, sem recriar itens removidos.
                    manualSubjects = if (current.syllabusMethod == SyllabusMethod.MANUAL) subjects.map { it.name } else current.manualSubjects,
                    manualTopics = if (current.syllabusMethod == SyllabusMethod.MANUAL) topics else current.manualTopics,
                )
            }
        } catch (error: Exception) {
            _operation.value = SetupOperation.Error(error.message ?: "Não foi possível atualizar o edital.")
        }
    }

    fun goBack() = viewModelScope.launch {
        val snapshot = state.value.snapshot
        val previous = if (snapshot.step == InitialSetupStep.SYLLABUS_REVIEW && snapshot.syllabusMethod == SyllabusMethod.CATALOG) {
            InitialSetupStep.EXAM_DATE
        } else {
            InitialSetupTransitions.previous(snapshot.step) ?: return@launch
        }
        app.preferences.updateInitialSetup { it.copy(step = previous) }
    }

    /** Analisa somente conteúdo real e devolve preview; não existe barra de progresso fictícia. */
    fun inspectEstudo(raw: String) = viewModelScope.launch {
        if (raw.isBlank()) {
            _operation.value = SetupOperation.Error("Cole ou escolha um arquivo .estudo para continuar.")
            return@launch
        }
        _operation.value = SetupOperation.Loading
        _operation.value = try {
            val clean = withContext(Dispatchers.Default) { br.com.estudario.data.transfer.IncomingText.clean(raw) }
            val preview = withContext(Dispatchers.Default) { estudoService.preview(clean) }
            SetupOperation.Preview(clean, preview)
        } catch (error: Exception) {
            SetupOperation.Error(error.message ?: "Não foi possível analisar este arquivo .estudo.")
        }
    }

    fun adoptEstudoPreview(raw: String, preview: EstudoPreview) {
        _operation.value = SetupOperation.Preview(raw, preview)
    }

    fun inspectPlan(raw: String) = viewModelScope.launch {
        if (raw.isBlank()) {
            _operation.value = SetupOperation.Error("Escolha um arquivo .plano válido para continuar.")
            return@launch
        }
        _operation.value = SetupOperation.Loading
        _operation.value = try {
            val clean = withContext(Dispatchers.Default) { br.com.estudario.data.transfer.IncomingText.clean(raw) }
            SetupOperation.PlanPreview(clean, withContext(Dispatchers.Default) { app.planTransferService.preview(clean) })
        } catch (error: Exception) {
            SetupOperation.Error(error.message ?: "Não foi possível analisar este arquivo .plano.")
        }
    }

    fun confirmPlanImport(raw: String) = viewModelScope.launch {
        _operation.value = SetupOperation.Loading
        try {
            val current = app.preferences.initialSetup.first()
            val competitionId = current.competitionId ?: state.value.competition?.id
                ?: error("Selecione o concurso antes de importar o plano.")
            val file = withContext(Dispatchers.Default) {
                StudyPlanCodec().decode(br.com.estudario.data.transfer.IncomingText.clean(raw))
            }
            val sourceSubjects = dao.subjectsFor(competitionId).map { subject ->
                PlanCoverageSubject(
                    id = PromptIds.subject(subject),
                    name = subject.name,
                    topics = dao.topicsFor(subject.id).map { topic -> PlanCoverageTopic(PromptIds.topic(topic), topic.title) },
                )
            }
            val expectedCompetition = dao.competitionsOnce().firstOrNull { it.id == competitionId }
                ?: error("O concurso selecionado não foi encontrado.")
            require(file.competition.externalId == PromptIds.competition(expectedCompetition)) {
                "Este .plano foi criado para outro concurso. Gere ou escolha um plano para ${expectedCompetition.name}."
            }
            val importedDayMinutes = List(7) { index ->
                file.configuration.days.firstOrNull { it.day == index + 1 }
                    ?.let { if (it.unavailable) 0 else it.minutes }
                    ?: 0
            }
            val coverage = withContext(Dispatchers.Default) {
                PlanCoverageValidator.validate(
                    sourceSubjects = sourceSubjects,
                    importedTasks = file.tasks.map { task ->
                        PlanCoverageTask(task.subjectExternalId, task.topicExternalId, task.date.toKotlinLocalDate(), task.minutes)
                    },
                    dayMinutes = current.availabilityMinutes,
                    importedDayMinutes = importedDayMinutes,
                )
            }
            if (!coverage.isComplete) {
                _operation.value = SetupOperation.PlanCoverageError(raw, coverage)
                return@launch
            }
            ensureExternalIds()
            val result = withContext(Dispatchers.Default) {
                app.planTransferService.`import`(br.com.estudario.data.transfer.IncomingText.clean(raw), PlanImportMode.CREATE, confirmActive = true, confirmMaster = true)
            }
            app.planService.activate(result.planId)
            app.planService.markMaster(result.planId)
            app.preferences.updateInitialSetup { it.copy(planMethod = PlanCreationMethod.EXTERNAL_AI, lastValidPlanId = result.planId, step = InitialSetupStep.PLAN_REVIEW) }
            _operation.value = SetupOperation.Success("Plano importado e pronto para sua conferência.")
        } catch (error: Exception) {
            _operation.value = SetupOperation.Error(error.message ?: "Falha ao importar o plano.")
        }
    }

    fun confirmEstudoImport(raw: String) = viewModelScope.launch {
        _operation.value = SetupOperation.Loading
        try {
            val current = app.preferences.initialSetup.first()
            val targetCompetitionId = current.competitionId ?: state.value.competition?.id
            val clean = withContext(Dispatchers.Default) { br.com.estudario.data.transfer.IncomingText.clean(raw) }
            val preview = withContext(Dispatchers.Default) { estudoService.preview(clean) }
            withContext(Dispatchers.Default) { estudoService.import(clean, ImportMode.SKIP, targetCompetitionId) }
            val importedCompetition = targetCompetitionId?.let { id -> dao.competitionsOnce().firstOrNull { it.id == id } }
                ?: dao.competitionsOnce().firstOrNull { it.name.equals(preview.competition, true) }
            importedCompetition?.let { repository.setPrimary(it.id) }
            app.preferences.updateInitialSetup {
                it.copy(
                    competitionId = importedCompetition?.id ?: it.competitionId,
                    competitionName = importedCompetition?.name ?: it.competitionName,
                    syllabusMethod = it.syllabusMethod ?: SyllabusMethod.IMPORT_ESTUDO,
                    step = InitialSetupStep.SYLLABUS_REVIEW,
                )
            }
            _operation.value = SetupOperation.Success("Edital importado. Confira o resumo antes de montar seu plano.")
        } catch (error: Exception) {
            _operation.value = SetupOperation.Error(error.message ?: "Falha ao importar o edital.")
        }
    }

    fun confirmManualSyllabus() = viewModelScope.launch {
        val current = app.preferences.initialSetup.first()
        val competitionId = ensureCompetition(current)
        val existing = dao.subjectsFor(competitionId).map { it.name.lowercase() }.toMutableSet()
        current.manualSubjects.map(String::trim).filter(String::isNotBlank).distinct().forEach { name ->
            if (existing.add(name.lowercase())) repository.addSubject(competitionId, name)
        }
        val subjects = dao.subjectsFor(competitionId)
        current.manualTopics.forEach { (subjectName, topics) ->
            val subject = subjects.firstOrNull { it.name.equals(subjectName, true) } ?: return@forEach
            val existingTopics = dao.topicsFor(subject.id).map { it.title.lowercase() }.toMutableSet()
            topics.map(String::trim).filter(String::isNotBlank).distinct().forEach { title ->
                if (existingTopics.add(title.lowercase())) repository.addTopic(subject.id, title)
            }
        }
        app.preferences.updateInitialSetup { it.copy(competitionId = competitionId, step = InitialSetupStep.SYLLABUS_REVIEW) }
    }

    fun createAutomaticPlan() = viewModelScope.launch {
        _operation.value = SetupOperation.Loading
        try {
            val current = app.preferences.initialSetup.first()
            val competitionId = ensureCompetition(current)
            val subjects = dao.subjectsFor(competitionId)
            require(subjects.isNotEmpty()) { "Adicione ao menos uma matéria antes de criar o plano." }
            val competition = dao.competitionsOnce().firstOrNull { it.id == competitionId }
            val planData = InitialSetupPlanMapper.map(
                competition = competition,
                subjects = subjects,
                topics = subjects.flatMap { dao.topicsFor(it.id) },
                questions = dao.questionsOnce(),
                attempts = dao.attemptsOnce(),
                snapshot = current,
            )
            val exam = current.examDate?.let { LocalDate.parse(it) }
            require(exam == null || !exam.isBefore(LocalDate.now())) {
                "A data da prova não pode ser anterior à data de início do plano."
            }
            // O método gravado no plano carrega o que o assistente coletou sobre ritmo: tamanho de
            // bloco e preferência de variedade. Replanejar depois segue as mesmas regras.
            val method = StudyMethodConfig.forProfile(current.studyProfile).copy(
                blockMinutes = current.sessionMinutes,
                interleaveSubjects = current.variety.interleave,
                dailySubjectSharePercent = current.variety.dailySubjectSharePercent,
            )
            val baseObjective = current.role.ifBlank { "Preparação para ${current.competitionName.ifBlank { "a prova" }}" }
            val objective = if (current.planPreference.isBlank()) baseObjective else "$baseObjective • Prioridade declarada: ${current.planPreference}"
            val existingPlan = current.lastValidPlanId?.let { id -> app.database.plannerDao().plan(id) }
            val planId = existingPlan?.id ?: app.planService.createPlan(
                CreatePlanInput(
                    competitionId = competitionId,
                    name = "Plano de ${current.competitionName.ifBlank { "estudos" }}",
                    objective = objective,
                    startDate = LocalDate.now(),
                    examDate = exam,
                    availability = current.availabilityMinutes.mapIndexed { index, minutes ->
                        StudyAvailabilityEntity("pending", index + 1, minutes, unavailable = minutes <= 0, mode = AvailabilityMode.SIMPLE)
                    },
                    subjects = subjects.mapIndexed { index, subject ->
                        // Os três eixos chegam separados ao plano: a prova define a prioridade, a
                        // pessoa define a dificuldade e o conhecimento prévio. Fundir qualquer par
                        // deles aqui apagaria a informação que o Smart Planner usa para decidir.
                        PlanSubjectInput(
                            subjectId = subject.id,
                            name = subject.name,
                            priority = planData.officialPrioritiesBySubjectId[subject.id] ?: PlanPriority.MEDIUM,
                            minimumMaintenanceMinutes = 0,
                            paused = false,
                            position = index,
                            weight = (planData.officialPrioritiesBySubjectId[subject.id] ?: PlanPriority.MEDIUM)
                                .toExamPriority().rotationWeight(),
                            personalDifficulty = planData.difficultiesBySubjectId[subject.id]
                                ?: PersonalDifficulty.NORMAL,
                            initialKnowledge = planData.knowledgeBySubjectId[subject.id]
                                ?: InitialKnowledge.NONE,
                        )
                    },
                    active = true,
                    masterPlan = true,
                    method = method,
                ),
            )
            // Grava o id antes da etapa pesada: se o processo morrer ou o motor falhar, a próxima
            // tentativa reaproveita este plano em vez de criar uma segunda proposta silenciosa.
            app.preferences.updateInitialSetup { it.copy(lastValidPlanId = planId) }
            val existingTasks = app.database.plannerDao().tasksForOnce(planId)
            if (existingPlan == null || existingTasks.isEmpty()) {
                app.planService.activate(planId)
                app.planService.markMaster(planId)
                app.planService.replan(planId, ReplanReason.INITIAL)
            }
            app.preferences.updateInitialSetup { it.copy(planMethod = PlanCreationMethod.AUTOMATIC, lastValidPlanId = planId, step = InitialSetupStep.PLAN_REVIEW) }
            _operation.value = SetupOperation.Success("Plano criado com base no seu edital e na sua disponibilidade.")
        } catch (error: Exception) {
            _operation.value = SetupOperation.Error(error.message ?: "Não foi possível criar o plano agora.")
        }
    }

    private suspend fun ensureExternalIds() {
        val competition = dao.competitionsOnce().firstOrNull { it.id == app.preferences.initialSetup.first().competitionId }
            ?: return
        if (competition.externalId == null) dao.updateCompetition(competition.copy(externalId = PromptIds.competition(competition)))
        dao.subjectsFor(competition.id).forEach { subject ->
            if (subject.externalId == null) dao.updateSubject(subject.copy(externalId = PromptIds.subject(subject)))
            dao.topicsFor(subject.id).filter { it.externalId == null }.forEach { topic -> dao.updateTopic(topic.copy(externalId = PromptIds.topic(topic))) }
        }
    }

    fun complete() = viewModelScope.launch {
        val current = app.preferences.initialSetup.first()
        require(current.lastValidPlanId != null) { "Crie um plano antes de concluir." }
        app.preferences.setInitialSetup(current.copy(status = InitialSetupStatus.IN_PROGRESS, step = InitialSetupStep.READY))
    }

    fun finish() = viewModelScope.launch {
        app.preferences.updateInitialSetup { it.copy(status = InitialSetupStatus.COMPLETED, step = InitialSetupStep.READY) }
    }

    /** Sai do "novo plano" sem mexer no resto: a configuração volta ao status e à etapa de antes. */
    fun cancelPlanMode(previousStatus: String?, previousStep: String?) = viewModelScope.launch {
        val status = previousStatus?.let { runCatching { InitialSetupStatus.valueOf(it) }.getOrNull() } ?: InitialSetupStatus.COMPLETED
        val step = previousStep?.let { runCatching { InitialSetupStep.valueOf(it) }.getOrNull() } ?: InitialSetupStep.READY
        app.preferences.updateInitialSetup { it.copy(status = status, step = step) }
    }

    fun reportError(message: String) { _operation.value = SetupOperation.Error(message) }

    fun clearOperation() { _operation.value = SetupOperation.Idle }

    private fun update(transform: (InitialSetupSnapshot) -> InitialSetupSnapshot) = viewModelScope.launch {
        app.preferences.updateInitialSetup(transform)
    }

    private suspend fun ensureCompetition(current: InitialSetupSnapshot): Long {
        current.competitionId?.let { id -> if (dao.competitionsOnce().any { it.id == id }) return id }
        val name = current.competitionName.trim().ifBlank { "Meu concurso" }
        val existing = dao.competitionsOnce().firstOrNull { it.name.equals(name, true) }
        val id = existing?.id ?: repository.addCompetition(name)
        repository.setPrimary(id)
        app.preferences.updateInitialSetup { it.copy(competitionId = id, competitionName = name) }
        return id
    }

    companion object {
        fun hasExistingWorkspace(competitions: List<CompetitionEntity>, plans: List<br.com.estudario.data.local.planner.StudyPlanEntity>): Boolean =
            InitialSetupWorkspace.hasExistingData(competitions.size, plans.size)
    }
}

/** Preferências do modo "novo plano" (assistente aberto pela aba Plano). */
const val PLAN_MODE_PREFS = "estudario_ui"
const val PLAN_MODE_PREVIOUS_STATUS = "plan_mode_previous_status"
const val PLAN_MODE_PREVIOUS_STEP = "plan_mode_previous_step"

/** Lista do catálogo de editais na tela do concurso. */
data class ExamCatalogUiState(
    val entries: List<br.com.estudario.data.catalog.ExamCatalogEntry> = emptyList(),
    val loading: Boolean = false,
    val error: String? = null,
)

/** Conferência local do PDF anexado, feita assim que a pessoa escolhe o arquivo. */
sealed interface EditalAttachmentCheck {
    data object None : EditalAttachmentCheck
    data object Checking : EditalAttachmentCheck
    /** PDF protegido ou só de imagem: não dá para conferir no aparelho, mas pode seguir. */
    data object Unreadable : EditalAttachmentCheck
    data class Done(val result: br.com.estudario.data.ai.SyllabusPreflightResult) : EditalAttachmentCheck
}
