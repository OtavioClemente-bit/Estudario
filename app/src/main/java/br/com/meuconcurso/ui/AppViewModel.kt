package br.com.meuconcurso.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import br.com.meuconcurso.MeuConcursoApplication
import br.com.meuconcurso.data.local.*
import br.com.meuconcurso.data.transfer.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import br.com.meuconcurso.notifications.StudyNotificationCoordinator

sealed interface TransferState {
    data object Idle : TransferState
    data object Loading : TransferState
    data class Preview(val value: EstudoPreview, val raw: String) : TransferState
    data class Success(val message: String) : TransferState
    data class Error(val message: String) : TransferState
}

class AppViewModel(application: Application) : AndroidViewModel(application) {
    private val app = application as MeuConcursoApplication
    private val repository = app.repository
    private val estudoService = EstudoPackageService(app.database)
    private val backupService = BackupService(app.database)

    val competitions = repository.competitions.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
    val subjects = repository.subjects.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
    val topics = repository.topics.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
    val summaries = repository.summaries.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
    val snippets = repository.snippets.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
    val theories = repository.theories.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
    val theoryMarks = repository.theoryMarks.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
    val questions = repository.questions.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
    val attempts = repository.attempts.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
    val errors = repository.errors.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
    val errorConcepts = repository.errorConcepts.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
    val errorConceptEntries = repository.errorConceptEntries.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
    val reviews = repository.reviews.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
    val reviewHistory = repository.reviewHistory.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
    val reviewSessions = repository.reviewSessions.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
    val queue = repository.queue.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
    val queueEvents = repository.queueEvents.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
    val studySessions = repository.studySessions.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
    val questionSessions = repository.questionSessions.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
    val darkTheme = app.preferences.darkTheme.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), false)
    val editalPrompt = app.preferences.editalPrompt.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), br.com.meuconcurso.data.preferences.PromptTemplates.EDITAL)
    val contentPrompt = app.preferences.contentPrompt.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), br.com.meuconcurso.data.preferences.PromptTemplates.CONTEUDO)
    val notificationsEnabled = app.preferences.notificationsEnabled.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), false)
    val dailyReminderEnabled = app.preferences.dailyReminderEnabled.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), true)
    val pendingAlertsEnabled = app.preferences.pendingAlertsEnabled.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), true)
    val reminderHour = app.preferences.reminderHour.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 19)
    val reminderMinute = app.preferences.reminderMinute.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0)
    val themeMode = app.preferences.themeMode.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), "SYSTEM")
    val questionTimer = app.preferences.questionTimer.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), true)
    val defaultQuestionCount = app.preferences.defaultQuestionCount.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 10)
    val showExplanation = app.preferences.showExplanation.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), true)
    val reviewIntervals = app.preferences.reviewIntervals.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), listOf(1L, 7L, 30L))
    val expandedEditalSubjects = app.preferences.expandedEditalSubjects.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptySet())

    private val _transfer = MutableStateFlow<TransferState>(TransferState.Idle)
    val transfer: StateFlow<TransferState> = _transfer.asStateFlow()
    private val _notificationDestination = MutableStateFlow<String?>(null)
    val notificationDestination: StateFlow<String?> = _notificationDestination.asStateFlow()

    fun addCompetition(name: String) = launchCatching { if (name.isNotBlank()) repository.addCompetition(name) }
    fun reportIncomingFileError(message: String) { _transfer.value = TransferState.Error(message) }
    fun setPrimary(id: Long) = launchCatching { repository.setPrimary(id) }
    fun deleteCompetition(value: CompetitionEntity) = launchCatching { repository.deleteCompetition(value) }
    fun addSubject(competitionId: Long, name: String) = launchCatching { if (name.isNotBlank()) repository.addSubject(competitionId, name) }
    fun deleteSubject(value: SubjectEntity) = launchCatching { repository.deleteSubject(value) }
    fun addTopic(subjectId: Long, title: String) = launchCatching { if (title.isNotBlank()) repository.addTopic(subjectId, title) }
    fun updateTopic(value: TopicEntity) = launchCatching { repository.updateTopic(value) }
    fun deleteTopic(value: TopicEntity) = launchCatching { repository.deleteTopic(value) }
    fun markStudied(value: TopicEntity) = launchCatching { repository.markStudied(value, reviewIntervals.value) }
    fun addSummary(topicId: Long, title: String, markdown: String) = launchCatching { if (title.isNotBlank() && markdown.isNotBlank()) repository.addSummary(topicId, title, markdown) }
    fun updateSummary(value: SummaryEntity) = launchCatching { repository.updateSummary(value) }
    fun deleteSummary(value: SummaryEntity) = launchCatching { repository.deleteSummary(value) }
    fun saveSnippet(value: TopicSnippetEntity) = launchCatching { repository.saveSnippet(value) }
    fun deleteSnippet(value: TopicSnippetEntity) = launchCatching { repository.deleteSnippet(value) }
    fun completeReview(
        value: ReviewScheduleEntity,
        difficulty: ReviewDifficulty = ReviewDifficulty.NORMAL,
        correct: Int = 0,
        total: Int = 0,
        recalled: Int = 0,
        forgotten: Int = 0,
        startedAt: Long = System.currentTimeMillis(),
    ) = launchCatching { repository.completeReview(value, difficulty, correct, total, recalled, forgotten, startedAt) }
    fun ignoreReview(value: ReviewScheduleEntity) = launchCatching { repository.ignoreReview(value) }
    fun enqueue(topicId: Long) = launchCatching { if (queue.value.none { it.item.topicId == topicId }) repository.enqueue(topicId) }
    fun updateQueue(value: StudyQueueEntity) = launchCatching { repository.updateQueue(value) }
    fun removeQueue(value: StudyQueueEntity) = launchCatching { repository.removeQueue(value) }
    fun completeQueue(value: StudyQueueEntity) = launchCatching { repository.completeQueue(value) }
    fun completeStudy(topicId: Long, startedAt: Long, notes: String = "") = launchCatching { repository.completeStudy(topicId, startedAt, notes) }
    fun postponeQueue(value: StudyQueueEntity, reason: String) = launchCatching { repository.postponeQueue(value, reason) }
    fun deleteError(id: Long) = launchCatching { repository.deleteError(id) }
    fun updateError(value: ErrorNotebookEntryEntity) = launchCatching { repository.updateError(value) }
    fun saveErrorConcept(value: ErrorConceptEntity) = launchCatching { repository.saveErrorConcept(value) }
    fun deleteErrorConcept(value: ErrorConceptEntity) = launchCatching { repository.deleteErrorConcept(value) }
    fun linkErrorConcept(conceptId: Long, errorEntryId: Long) = launchCatching { repository.linkErrorConcept(conceptId, errorEntryId) }
    fun loadDemo() = launchCatching { repository.loadDemoData() }
    fun setDarkTheme(value: Boolean) = launchCatching { app.preferences.setDarkTheme(value) }
    fun setThemeMode(value: String) = launchCatching { app.preferences.setThemeMode(value) }
    fun setQuestionTimer(value: Boolean) = launchCatching { app.preferences.setQuestionTimer(value) }
    fun setDefaultQuestionCount(value: Int) = launchCatching { app.preferences.setDefaultQuestionCount(value.coerceIn(5, 100)) }
    fun setShowExplanation(value: Boolean) = launchCatching { app.preferences.setShowExplanation(value) }
    fun setReviewIntervals(value: List<Long>) = launchCatching { app.preferences.setReviewIntervals(value.filter { it > 0 }.distinct().sorted()) }
    fun setEditalSubjectExpanded(subjectId: Long, expanded: Boolean) = launchCatching { app.preferences.setEditalSubjectExpanded(subjectId, expanded) }
    fun updateTheoryProgress(value: TheoryDocumentEntity, block: Int) = launchCatching { repository.updateTheoryProgress(value, block) }
    fun saveTheoryMark(value: TheoryMarkEntity) = launchCatching { repository.saveTheoryMark(value) }
    fun deleteTheoryMark(value: TheoryMarkEntity) = launchCatching { repository.deleteTheoryMark(value) }
    fun saveEditalPrompt(value: String) = launchCatching { if (value.isNotBlank()) app.preferences.setEditalPrompt(value) }
    fun saveContentPrompt(value: String) = launchCatching { if (value.isNotBlank()) app.preferences.setContentPrompt(value) }
    fun setNotificationsEnabled(value: Boolean) = launchCatching { app.preferences.setNotificationsEnabled(value); StudyNotificationCoordinator.refresh(app, app.preferences) }
    fun setDailyReminderEnabled(value: Boolean) = launchCatching { app.preferences.setDailyReminderEnabled(value); StudyNotificationCoordinator.refresh(app, app.preferences) }
    fun setPendingAlertsEnabled(value: Boolean) = launchCatching { app.preferences.setPendingAlertsEnabled(value); StudyNotificationCoordinator.refresh(app, app.preferences) }
    fun setReminderTime(hour: Int, minute: Int) = launchCatching { app.preferences.setReminderTime(hour, minute); StudyNotificationCoordinator.refresh(app, app.preferences) }
    fun sendTestNotification() { StudyNotificationCoordinator.showTest(app) }
    fun openFromNotification(destination: String) { _notificationDestination.value = destination }
    fun consumeNotificationDestination() { _notificationDestination.value = null }

    suspend fun answer(question: QuestionWithOptions, selectedKey: String, sessionId: String? = null): Boolean = repository.answer(question, selectedKey, sessionId)
    suspend fun smartQuestions(count: Int): List<QuestionWithOptions> = repository.smartQuestions(count.coerceIn(1, 100))
    fun saveQuestionSession(value: QuestionSessionEntity) = launchCatching { repository.saveQuestionSession(value) }
    fun toggleQuestionFavorite(value: QuestionEntity) = launchCatching { repository.toggleQuestionFavorite(value) }

    fun inspectEstudo(text: String) = viewModelScope.launch {
        _transfer.value = TransferState.Loading
        _transfer.value = try { TransferState.Preview(estudoService.preview(text), text) } catch (e: Exception) { TransferState.Error(e.message ?: "Não foi possível analisar o arquivo.") }
    }

    fun confirmImport(raw: String, mode: ImportMode = ImportMode.SKIP, markAsStudied: Boolean = false) = viewModelScope.launch {
        _transfer.value = TransferState.Loading
        _transfer.value = try {
            val result = estudoService.import(raw, mode)
            if (markAsStudied) result.importedTopicIds.forEach { topicId -> repository.completeStudy(topicId, System.currentTimeMillis(), "Importado e marcado como estudado") }
            TransferState.Success(
                "Edital atualizado: ${result.subjectsCreated} matéria(s) nova(s), ${result.topicsCreated} tópico(s) novo(s) e ${result.topicsUpdated} tópico(s) sincronizado(s). " +
                    "Conteúdo: ${result.theories} teoria(s), ${result.summaries} resumo(s), ${result.snippets} item(ns) de memorização, ${result.questions} questão(ões) e ${result.errorConcepts} conceito(s) de erro. " +
                    "${result.updatedContent} item(ns) atualizado(s) preservando o histórico; ${result.skipped} duplicado(s) ignorado(s).",
            )
        } catch (e: Exception) { TransferState.Error(e.message ?: "Falha ao importar.") }
    }

    suspend fun createBackup(): String = backupService.export()
    fun restoreBackup(text: String) = viewModelScope.launch {
        _transfer.value = TransferState.Loading
        _transfer.value = try { backupService.restore(text); TransferState.Success("Backup restaurado com sucesso.") } catch (e: Exception) { TransferState.Error(e.message ?: "Falha ao restaurar backup.") }
    }
    fun clearTransfer() { _transfer.value = TransferState.Idle }

    private fun launchCatching(block: suspend () -> Unit) = viewModelScope.launch {
        try { block() } catch (e: Exception) { _transfer.value = TransferState.Error(e.message ?: "Ocorreu um erro.") }
    }
}
