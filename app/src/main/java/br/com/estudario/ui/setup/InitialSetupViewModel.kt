package br.com.estudario.ui.setup

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import br.com.estudario.EstudarioApplication
import br.com.estudario.data.local.CompetitionEntity
import br.com.estudario.data.local.SubjectEntity
import br.com.estudario.data.local.planner.AvailabilityMode
import br.com.estudario.data.local.planner.StudyAvailabilityEntity
import br.com.estudario.data.planner.CreatePlanInput
import br.com.estudario.data.planner.PlanSubjectInput
import br.com.estudario.data.transfer.EstudoPackageService
import br.com.estudario.data.transfer.EstudoPreview
import br.com.estudario.data.transfer.ImportMode
import br.com.estudario.data.transfer.planner.PlanImportMode
import br.com.estudario.data.transfer.planner.StudyPlanImportPreview
import br.com.estudario.data.prompt.PromptIds
import br.com.estudario.domain.planner.PlanPriority
import br.com.estudario.domain.planner.ReplanReason
import br.com.estudario.domain.planner.StudyMethodConfig
import br.com.estudario.domain.setup.InitialSetupSnapshot
import br.com.estudario.domain.setup.InitialSetupStatus
import br.com.estudario.domain.setup.InitialSetupStep
import br.com.estudario.domain.setup.InitialSetupTransitions
import br.com.estudario.domain.setup.InitialSetupWorkspace
import br.com.estudario.domain.setup.PlanCreationMethod
import br.com.estudario.domain.setup.SyllabusMethod
import br.com.estudario.domain.setup.SubjectDifficulty
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
    data class Success(val message: String) : SetupOperation
    data class Error(val message: String) : SetupOperation
}

data class InitialSetupUiState(
    val snapshot: InitialSetupSnapshot = InitialSetupSnapshot(),
    val competition: CompetitionEntity? = null,
    val competitions: List<CompetitionEntity> = emptyList(),
    val subjects: List<SubjectEntity> = emptyList(),
    val topicCount: Int = 0,
    val topicTitlesBySubject: Map<Long, List<String>> = emptyMap(),
)

/** Orquestra a primeira configuração e mantém a lógica de dados fora das telas Compose. */
class InitialSetupViewModel(application: Application) : AndroidViewModel(application) {
    private val app = application as EstudarioApplication
    private val repository = app.repository
    private val dao = app.database.dao()
    private val estudoService = EstudoPackageService(app.database)

    val state: StateFlow<InitialSetupUiState> = combine(
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
            topicCount = topics.count { it.subjectId in scopedSubjectIds },
            topicTitlesBySubject = topics.filter { it.subjectId in scopedSubjectIds }.groupBy { it.subjectId }.mapValues { (_, values) -> values.map { it.title } },
        )
    }.stateIn(viewModelScope, SharingStarted.Eagerly, InitialSetupUiState())

    private val _operation = MutableStateFlow<SetupOperation>(SetupOperation.Idle)
    val operation: StateFlow<SetupOperation> = _operation.asStateFlow()

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
            ),
        )
    }

    fun selectCompetition(competition: CompetitionEntity) = viewModelScope.launch {
        app.preferences.updateInitialSetup {
            it.copy(
                status = InitialSetupStatus.IN_PROGRESS,
                step = InitialSetupStep.EXAM_DATE,
                competitionId = competition.id,
                competitionName = competition.name,
            )
        }
        repository.setPrimary(competition.id)
    }

    fun saveExamDate(value: String?) = update { it.copy(examDate = value?.trim()?.takeIf(String::isNotBlank), step = InitialSetupStep.SYLLABUS_METHOD) }
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
    fun setSubjectDifficulty(subjectId: String, value: SubjectDifficulty) = update { current ->
        if (subjectId.isBlank()) current else current.copy(subjectDifficulties = current.subjectDifficulties + (subjectId to value))
    }
    fun reconcileSubjectDifficulties(subjectIds: Set<String>) = update { current ->
        val reconciled = current.subjectDifficultiesFor(subjectIds)
        if (reconciled == current.subjectDifficulties) current else current.copy(subjectDifficulties = reconciled)
    }
    fun chooseSessionMinutes(value: Int) = update { it.copy(sessionMinutes = value.coerceIn(15, 180)) }
    fun choosePlanMethod(value: PlanCreationMethod) = update { it.copy(planMethod = value) }
    fun savePlanPreference(value: String) = update { it.copy(planPreference = value) }

    fun setAvailability(dayIndex: Int, minutes: Int) = update { current ->
        val values = current.availabilityMinutes.toMutableList().apply { this[dayIndex.coerceIn(0, 6)] = minutes.coerceIn(0, 1_440) }
        current.copy(availabilityMinutes = values)
    }

    fun advance(from: InitialSetupStep, to: InitialSetupStep) {
        if (!InitialSetupTransitions.canAdvance(from, to)) return
        update { it.copy(status = InitialSetupStatus.IN_PROGRESS, step = to) }
    }

    fun goBack() = viewModelScope.launch {
        val previous = InitialSetupTransitions.previous(state.value.snapshot.step) ?: return@launch
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
            ensureExternalIds()
            val result = withContext(Dispatchers.Default) {
                app.planTransferService.`import`(raw, PlanImportMode.CREATE, confirmActive = true, confirmMaster = true)
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
            val clean = withContext(Dispatchers.Default) { br.com.estudario.data.transfer.IncomingText.clean(raw) }
            val preview = withContext(Dispatchers.Default) { estudoService.preview(clean) }
            withContext(Dispatchers.Default) { estudoService.import(clean, ImportMode.SKIP) }
            val importedCompetition = dao.competitionsOnce().firstOrNull { it.name.equals(preview.competition, true) }
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
            val exam = current.examDate?.let { LocalDate.parse(it) }
            val method = StudyMethodConfig.forProfile(current.studyProfile).copy(blockMinutes = current.sessionMinutes)
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
                        PlanSubjectInput(subject.id, subject.name, PlanPriority.MEDIUM, 0, false, index, weight = 3)
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
            app.preferences.updateInitialSetup { it.copy(lastValidPlanId = planId, step = InitialSetupStep.PLAN_REVIEW) }
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
