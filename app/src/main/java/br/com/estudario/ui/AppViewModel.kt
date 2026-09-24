package br.com.estudario.ui

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import br.com.estudario.EstudarioApplication
import br.com.estudario.data.local.*
import br.com.estudario.data.transfer.*
import br.com.estudario.data.preferences.FocusSessionPrefs
import br.com.estudario.data.local.FocusSessionOrigin
import br.com.estudario.focus.FocusSessionManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import br.com.estudario.notifications.StudyNotificationCoordinator
import androidx.compose.ui.geometry.Rect
import br.com.estudario.ui.tour.TourId
import br.com.estudario.ui.tour.TourKey
import br.com.estudario.ui.tour.TourStep
import br.com.estudario.ui.tour.tourSteps
import br.com.estudario.data.local.planner.StudyTaskExecutionEntity
import br.com.estudario.data.local.planner.PlanTaskEntity
import br.com.estudario.data.local.planner.StudyPlanEntity
import br.com.estudario.domain.performance.StudyPerformanceEvaluator
import br.com.estudario.domain.performance.StudyPerformanceInput
import br.com.estudario.domain.performance.StudyPerformancePeriod
import br.com.estudario.ui.screens.performance.StudyPerformanceInputMapper
import br.com.estudario.ui.screens.performance.StudyPerformanceUiState
import br.com.estudario.ui.ai.AiReviewTarget
import br.com.estudario.ui.ai.DataStoreAiReviewSessionStore
import br.com.estudario.domain.DailyActivity
import br.com.estudario.domain.DailyGoal
import br.com.estudario.domain.StreakEngine
import br.com.estudario.domain.StreakSummary
import br.com.estudario.domain.Badge
import br.com.estudario.domain.ProgressEngine
import br.com.estudario.domain.PriorityLevel
import br.com.estudario.data.account.GoogleDriveBackupService
import br.com.estudario.ui.profile.GoogleAction
import br.com.estudario.ui.profile.StreakCelebration
import br.com.estudario.ui.profile.UserProfile
import br.com.estudario.domain.setup.InitialSetupStatus
import br.com.estudario.domain.setup.InitialSetupSnapshot
import java.io.File
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

sealed interface TransferState {
    data object Idle : TransferState
    data object Loading : TransferState
    data class Preview(val value: EstudoPreview, val raw: String, val targetCompetitionId: Long? = null) : TransferState
    data class Success(val message: String) : TransferState
    data class Error(val message: String) : TransferState
}

class AppViewModel(application: Application) : AndroidViewModel(application) {
    private val app = application as EstudarioApplication
    private val repository = app.repository
    private val estudoService = EstudoPackageService(app.database)
    private val backupService = BackupService(app.database)
    private val drive = GoogleDriveBackupService()

    private val _aiReviewTarget = MutableStateFlow<AiReviewTarget?>(null)
    val aiReviewTarget: StateFlow<AiReviewTarget?> = _aiReviewTarget.asStateFlow()

    init {
        viewModelScope.launch {
            DataStoreAiReviewSessionStore(application).loadLatest()?.let { saved ->
                _aiReviewTarget.value = AiReviewTarget(saved.targetId, saved.targetTitle)
            }
        }
    }

    fun openAiReview(targetId: Long, targetTitle: String) {
        if (targetId > 0L && targetTitle.isNotBlank()) _aiReviewTarget.value = AiReviewTarget(targetId, targetTitle)
    }

    fun closeAiReview() { _aiReviewTarget.value = null }

    val competitions = repository.competitions.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
    val subjects = repository.subjects.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
    val topics = repository.topics.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
    val sources = repository.sources.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
    val importPackages = repository.importPackages.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
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
    val focusSessions = app.focusSessionRepository.sessions.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
    val questionSessions = repository.questionSessions.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
    val darkTheme = app.preferences.darkTheme.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), false)
    val editalPrompt = app.preferences.editalPrompt.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), br.com.estudario.data.preferences.PromptTemplates.EDITAL)
    val contentPrompt = app.preferences.contentPrompt.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), br.com.estudario.data.preferences.PromptTemplates.CONTEUDO)
    val studyPlanPrompt = app.preferences.studyPlanPrompt.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), br.com.estudario.data.preferences.PromptTemplates.PLANO)
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
    val hasCompletedOnboarding: StateFlow<Boolean?> = app.preferences.hasCompletedOnboarding.stateIn(viewModelScope, SharingStarted.Eagerly, null)
    val initialSetup: StateFlow<InitialSetupSnapshot?> = app.preferences.initialSetup.stateIn(viewModelScope, SharingStarted.Eagerly, null)
    private val studyPlans = app.planRepository.plans.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
    val hasExistingWorkspace: StateFlow<Boolean?> = combine(competitions, studyPlans) { contests, plans -> contests.isNotEmpty() || plans.isNotEmpty() }
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)
    /** Execuções de tarefas do plano, de todos os planos: entram na sequência junto com questões e revisões. */
    val planExecutions: StateFlow<List<StudyTaskExecutionEntity>> = app.database.plannerDao().executions().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    private val allPlanTasks: StateFlow<List<PlanTaskEntity>> = app.database.plannerDao().tasks()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
    private val performancePeriod = MutableStateFlow(StudyPerformancePeriod.DAYS_30)

    private data class PerformanceCoreSources(
        val attempts: List<QuestionAttemptEntity>,
        val reviews: List<ReviewHistoryEntity>,
        val studySessions: List<StudySessionEntity>,
        val questionSessions: List<QuestionSessionEntity>,
        val focusSessions: List<FocusSessionEntity> = emptyList(),
    )

    private data class PerformanceCatalog(
        val questions: List<QuestionWithOptions>,
        val subjects: List<SubjectEntity>,
        val topics: List<TopicEntity>,
    )
    private data class PerformancePlanSources(
        val plans: List<StudyPlanEntity>,
        val tasks: List<PlanTaskEntity>,
        val executions: List<StudyTaskExecutionEntity>,
    )

    private val performanceInput: StateFlow<StudyPerformanceInput> = combine(
        combine(
            combine(attempts, reviewHistory, studySessions, questionSessions) { answers, history, sessions, questionRuns ->
                PerformanceCoreSources(answers, history, sessions, questionRuns)
            },
            focusSessions,
        ) { core, focus -> core.copy(focusSessions = focus) },
        combine(questions, subjects, topics) { questionList, subjectList, topicList ->
            PerformanceCatalog(questionList, subjectList, topicList)
        },
        combine(studyPlans, allPlanTasks, planExecutions) { plans, tasks, executions -> PerformancePlanSources(plans, tasks, executions) },
    ) { core, catalog, plan ->
        StudyPerformanceInputMapper.map(
            attempts = core.attempts,
            questions = catalog.questions,
            subjects = catalog.subjects,
            topics = catalog.topics,
            reviewHistory = core.reviews,
            studySessions = core.studySessions,
            focusSessions = core.focusSessions,
            questionSessions = core.questionSessions,
            plans = plan.plans,
            tasks = plan.tasks,
            executions = plan.executions,
        )
    }.flowOn(Dispatchers.Default)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), StudyPerformanceInput())

    val studyPerformance: StateFlow<StudyPerformanceUiState> = combine(performanceInput, performancePeriod) { input, period ->
        val zone = ZoneId.systemDefault()
        StudyPerformanceUiState(
            selectedPeriod = period,
            result = StudyPerformanceEvaluator.evaluate(input, period, LocalDate.now(zone), zone),
        )
    }.flowOn(Dispatchers.Default)
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            StudyPerformanceUiState(
                selectedPeriod = StudyPerformancePeriod.DAYS_30,
                result = StudyPerformanceEvaluator.evaluate(StudyPerformanceInput(), StudyPerformancePeriod.DAYS_30, LocalDate.now(), ZoneId.systemDefault()),
            ),
        )

    fun selectStudyPerformancePeriod(period: StudyPerformancePeriod) {
        performancePeriod.value = period
    }

    val profile: StateFlow<UserProfile> = combine(
        app.preferences.userName,
        app.preferences.userEmail,
        app.preferences.userPhotoPath,
        app.preferences.dailyGoalQuestions,
    ) { name, email, photo, goal -> UserProfile(name, email, photo, DailyGoal(goal)) }
        .stateIn(viewModelScope, SharingStarted.Eagerly, UserProfile())

    /** Histórico cruzado uma vez só: sequência, mapa de frequência, XP e emblemas saem todos daqui. */
    private data class ActivityBundle(
        val days: List<DailyActivity>,
        val goal: DailyGoal,
        val executions: List<StudyTaskExecutionEntity>,
        val totalQuestions: Int,
        val correctQuestions: Int,
        val reviewsCompleted: Int,
    )

    private data class ActivitySources(
        val answers: List<QuestionAttemptEntity>,
        val history: List<ReviewHistoryEntity>,
        val sessions: List<StudySessionEntity>,
        val executions: List<StudyTaskExecutionEntity>,
        val goal: DailyGoal,
    )

    private val activity: StateFlow<ActivityBundle?> = combine(
        combine(attempts, reviewHistory, studySessions, planExecutions, app.preferences.dailyGoalQuestions) { answers, history, sessions, executions, goal ->
            ActivitySources(answers, history, sessions, executions, DailyGoal(goal))
        },
        focusSessions,
    ) { sources, focus ->
        ActivityBundle(
            days = dailyActivity(sources.answers, sources.history, sources.sessions, focus, sources.executions),
            goal = sources.goal,
            executions = sources.executions,
            totalQuestions = sources.answers.size,
            correctQuestions = sources.answers.count { it.correct },
            reviewsCompleted = sources.history.size,
        )
    }.flowOn(Dispatchers.Default).stateIn(viewModelScope, SharingStarted.Eagerly, null)

    val streak: StateFlow<StreakSummary?> = activity
        .map { bundle -> bundle?.let { StreakEngine.summarize(it.days, it.goal) } }
        .flowOn(Dispatchers.Default)
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    private val planTaskTypes = app.database.plannerDao().taskTypes()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    /** (total de tópicos, tópicos já estudados) do concurso principal, base do emblema de edital. */
    private val syllabus: StateFlow<Pair<Int, Int>> = combine(competitions, subjects, topics) { contests, allSubjects, allTopics ->
        val primary = contests.firstOrNull { it.isPrimary } ?: contests.firstOrNull()
        val ids = allSubjects.filter { it.competitionId == primary?.id }.mapTo(hashSetOf()) { it.id }
        val scoped = allTopics.filter { it.subjectId in ids }
        scoped.size to scoped.count { it.status != TopicStatus.NAO_ESTUDADO }
    }.flowOn(Dispatchers.Default).stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0 to 0)

    /** XP, nível e emblemas. Tudo recalculado do histórico, nada guardado, backup devolve o nível. */
    val progress: StateFlow<ProgressEngine.ProgressSummary?> =
        combine(activity, streak, planTaskTypes, syllabus) { bundle, streakSummary, types, counts ->
            if (bundle == null || streakSummary == null) return@combine null
            val typeById = types.associate { it.id to it.type }
            val zone = ZoneId.systemDefault()
            val planWork = bundle.executions.mapNotNull { execution ->
                val type = execution.taskId?.let(typeById::get) ?: return@mapNotNull null
                ProgressEngine.PlanWork(
                    date = Instant.ofEpochMilli(execution.completedAt).atZone(zone).toLocalDate(),
                    type = type,
                    minutes = execution.actualMinutes,
                    correct = execution.correctAnswers,
                )
            }
            ProgressEngine.evaluate(
                ProgressEngine.ProgressInput(
                    today = LocalDate.now(),
                    days = bundle.days,
                    goal = bundle.goal,
                    planWork = planWork,
                    bestStreak = streakSummary.best,
                    currentStreak = streakSummary.current,
                    totalQuestions = bundle.totalQuestions,
                    correctQuestions = bundle.correctQuestions,
                    reviewsCompleted = bundle.reviewsCompleted,
                    topicsStudied = counts.second,
                    topicsTotal = counts.first,
                ),
            )
        }.flowOn(Dispatchers.Default).stateIn(viewModelScope, SharingStarted.Eagerly, null)

    private val _badgeUnlock = MutableStateFlow<List<Badge>>(emptyList())
    /** Emblemas conquistados agora, esperando a tela de comemoração. */
    val badgeUnlock: StateFlow<List<Badge>> = _badgeUnlock.asStateFlow()
    fun consumeBadgeUnlock() { _badgeUnlock.value = emptyList() }

    private val _celebration = MutableStateFlow<StreakCelebration?>(null)
    /** Tela de comemoração: abre uma única vez por dia, na primeira atividade que fecha a meta. */
    val celebration: StateFlow<StreakCelebration?> = _celebration.asStateFlow()

    init {
        viewModelScope.launch {
            var celebratedDay = app.preferences.lastCelebratedDay.first()
            streak.filterNotNull().collect { summary ->
                val today = LocalDate.now().toEpochDay()
                if (!summary.todayDone || celebratedDay == today) return@collect
                celebratedDay = today
                app.preferences.setLastCelebratedDay(today)
                _celebration.value = StreakCelebration(summary, celebrationReason(summary), progress.value?.xpToday ?: 0)
            }
        }
    }

    init {
        viewModelScope.launch {
            var conhecidos = app.preferences.earnedBadges.first()
            progress.filterNotNull().collect { summary ->
                val atuais = summary.earnedBadges.mapTo(hashSetOf()) { it.badge.id }
                val novos = atuais - conhecidos
                if (novos.isEmpty()) return@collect
                // Primeira leitura com histórico antigo: registra em silêncio em vez de despejar
                // dezenas de comemorações de uma vez.
                val backfill = conhecidos.isEmpty() && novos.size > 3
                conhecidos = conhecidos + novos
                app.preferences.markBadgesEarned(novos)
                if (!backfill) _badgeUnlock.value = summary.earnedBadges.filter { it.badge.id in novos }.map { it.badge }
            }
        }
    }

    private fun celebrationReason(summary: StreakSummary) = when {
        summary.todayPlanTasks > 0 -> "Tarefa do plano concluída"
        summary.todayReviews > 0 -> "Revisão em dia"
        else -> "${summary.todayQuestions} questões respondidas"
    }

    val driveLastBackupAt: StateFlow<Long> = app.preferences.driveLastBackupAt.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0L)

    /**
     * Um caminho só para entrar, subir e restaurar: o token já vem com permissão de perfil e da
     * pasta privada do app no Drive, então a ação pedida acontece na sequência do login.
     */
    fun handleGoogleToken(action: GoogleAction, token: String) = viewModelScope.launch {
        _transfer.value = TransferState.Loading
        _transfer.value = try {
            val user = drive.userInfo(token)
            app.preferences.setUserEmail(user.email)
            if (app.preferences.userName.first().isBlank() && user.name.isNotBlank()) app.preferences.setUserName(user.name)
            if (app.preferences.userPhotoPath.first() == null) user.pictureUrl?.let { saveGooglePhoto(it) }
            when (action) {
                GoogleAction.SIGN_IN -> TransferState.Success("Conectado como ${user.email}. Os backups ficam numa pasta privada do app no seu Drive, ela não aparece no Meu Drive e só este app enxerga.")
                GoogleAction.BACKUP -> {
                    val content = withContext(Dispatchers.Default) { backupService.export() }
                    val file = drive.upload(token, "estudario-${LocalDate.now()}.json", content)
                    app.preferences.setDriveLastBackupAt(file.modifiedAt)
                    TransferState.Success("Backup enviado para o Google Drive: ${file.name}.")
                }
                GoogleAction.RESTORE -> {
                    val latest = drive.listBackups(token).firstOrNull() ?: error("Nenhum backup encontrado na sua conta do Google.")
                    val content = drive.download(token, latest.id)
                    withContext(Dispatchers.Default) { backupService.restore(content) }
                    TransferState.Success("Backup ${latest.name} restaurado.")
                }
            }
        } catch (e: Exception) { TransferState.Error(e.message ?: "Não foi possível falar com o Google.") }
    }

    fun reportGoogleError(message: String) {
        _transfer.value = if (message == "Login cancelado.") TransferState.Idle else TransferState.Error(message)
    }

    /** Desconecta só neste aparelho: os dados locais e o backup no Drive continuam onde estão. */
    fun signOutGoogle() = launchCatching {
        app.preferences.setUserEmail("")
        _transfer.value = TransferState.Success("Conta desconectada deste aparelho. Para retirar o acesso do app, use myaccount.google.com › Segurança.")
    }

    private suspend fun saveGooglePhoto(url: String) {
        val target = File(app.filesDir, "profile_${System.currentTimeMillis()}.jpg")
        if (drive.downloadPhoto(url, target)) {
            withContext(Dispatchers.IO) { deleteOldPhotos(target.name) }
            app.preferences.setUserPhotoPath(target.absolutePath)
        }
    }

    fun consumeCelebration() { _celebration.value = null }
    fun setUserName(value: String) = launchCatching { app.preferences.setUserName(value) }
    fun setDailyGoal(value: Int) = launchCatching { app.preferences.setDailyGoalQuestions(value) }
    fun clearProfilePhoto() = launchCatching { app.preferences.setUserPhotoPath(null); withContext(Dispatchers.IO) { deleteOldPhotos(null) } }

    /** Copia a foto escolhida para dentro do app, reduzida, o URI da galeria não sobrevive ao reinício. */
    fun setProfilePhoto(uri: Uri) = launchCatching {
        val path = withContext(Dispatchers.IO) {
            val target = File(app.filesDir, "profile_${System.currentTimeMillis()}.jpg")
            val bitmap = app.contentResolver.openInputStream(uri)?.use { input ->
                BitmapFactory.decodeStream(input, null, BitmapFactory.Options().apply { inSampleSize = 2 })
            } ?: return@withContext null
            val side = minOf(bitmap.width, bitmap.height)
            val square = Bitmap.createBitmap(bitmap, (bitmap.width - side) / 2, (bitmap.height - side) / 2, side, side)
            val scaled = Bitmap.createScaledBitmap(square, 512, 512, true)
            target.outputStream().use { scaled.compress(Bitmap.CompressFormat.JPEG, 90, it) }
            deleteOldPhotos(target.name)
            target.absolutePath
        }
        if (path == null) _transfer.value = TransferState.Error("Não foi possível ler essa imagem. Escolha outra foto.")
        else app.preferences.setUserPhotoPath(path)
    }

    private fun deleteOldPhotos(keep: String?) {
        app.filesDir.listFiles { file -> file.name.startsWith("profile_") && file.name != keep }?.forEach { it.delete() }
    }

    private fun dailyActivity(
        answers: List<QuestionAttemptEntity>,
        history: List<ReviewHistoryEntity>,
        sessions: List<StudySessionEntity>,
        focusSessions: List<FocusSessionEntity>,
        executions: List<StudyTaskExecutionEntity>,
    ): List<DailyActivity> {
        val zone = ZoneId.systemDefault()
        fun day(epochMillis: Long): LocalDate = Instant.ofEpochMilli(epochMillis).atZone(zone).toLocalDate()
        val rows = HashMap<LocalDate, DailyActivity>()
        fun merge(date: LocalDate, block: (DailyActivity) -> DailyActivity) {
            rows[date] = block(rows[date] ?: DailyActivity(date))
        }
        answers.forEach { attempt -> merge(day(attempt.answeredAt)) { it.copy(questions = it.questions + 1, correct = it.correct + if (attempt.correct) 1 else 0) } }
        history.forEach { review -> merge(day(review.reviewedAt)) { it.copy(reviews = it.reviews + 1) } }
        sessions.forEach { session -> merge(day(session.completedAt)) { it.copy(studySessions = it.studySessions + 1, minutes = it.minutes + (session.durationSeconds / 60).toInt()) } }
        focusSessions.groupBy { day(it.completedAt) }.forEach { (date, rowsForDay) ->
            val totalSeconds = rowsForDay.sumOf { it.durationSeconds.coerceAtLeast(0L) }
            val freeSeconds = rowsForDay.asSequence().filter { it.origin == FocusSessionOrigin.LIVRE }
                .sumOf { it.durationSeconds.coerceAtLeast(0L) }
            val totalMinutes = (totalSeconds / 60L).coerceAtMost(Int.MAX_VALUE.toLong()).toInt()
            val freeMinutes = (freeSeconds / 60L).coerceAtMost(Int.MAX_VALUE.toLong()).toInt()
            merge(date) {
                it.copy(
                    focusSessions = it.focusSessions + rowsForDay.size,
                    focusMinutes = it.focusMinutes + totalMinutes,
                    freeFocusMinutes = it.freeFocusMinutes + freeMinutes,
                    minutes = it.minutes + totalMinutes,
                )
            }
        }
        executions.forEach { execution -> merge(day(execution.completedAt)) { it.copy(planTasks = it.planTasks + 1, minutes = it.minutes + execution.actualMinutes) } }
        return rows.values.toList()
    }

    val seenTours: StateFlow<Set<String>?> = app.preferences.seenTours.stateIn(viewModelScope, SharingStarted.Eagerly, null)

    private val _activeTour = MutableStateFlow<TourId?>(null)
    val activeTour: StateFlow<TourId?> = _activeTour.asStateFlow()
    private val _tourStepIndex = MutableStateFlow(0)
    val tourStepIndex: StateFlow<Int> = _tourStepIndex.asStateFlow()
    val tourStep: StateFlow<TourStep?> = combine(_activeTour, _tourStepIndex) { tour, index -> tour?.let { tourSteps(it).getOrNull(index) } }
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)
    private val _tourTargetBounds = MutableStateFlow<Rect?>(null)
    val tourTargetBounds: StateFlow<Rect?> = _tourTargetBounds.asStateFlow()
    private var pendingContentTour = false
    /** Guias encerrados nesta sessão, antes mesmo de a preferência terminar de gravar. */
    private val closedTours = mutableSetOf<TourId>()

    fun startTour(id: TourId = TourId.EDITAL) { _tourTargetBounds.value = null; _tourStepIndex.value = 0; _activeTour.value = id }
    /** Abre o guia só se ainda não foi visto e nenhum outro guia estiver aberto. */
    fun maybeStartTour(id: TourId) {
        val seen = seenTours.value ?: return
        if (_activeTour.value == null && id.name !in seen && id !in closedTours) startTour(id)
    }
    fun stopTour() {
        val tour = _activeTour.value ?: return
        closedTours += tour
        _activeTour.value = null
        _tourTargetBounds.value = null
        launchCatching { app.preferences.markTourSeen(tour.name) }
    }
    fun advanceTour() {
        val tour = _activeTour.value ?: return
        val next = _tourStepIndex.value + 1
        if (next >= tourSteps(tour).size) stopTour() else { _tourTargetBounds.value = null; _tourStepIndex.value = next }
    }
    fun previousTourStep() {
        if (_activeTour.value == null || _tourStepIndex.value == 0) return
        _tourTargetBounds.value = null
        _tourStepIndex.value -= 1
    }
    fun reportTourTargetBounds(key: TourKey, bounds: Rect) { if (tourStep.value?.key == key) _tourTargetBounds.value = bounds }

    private val _transfer = MutableStateFlow<TransferState>(TransferState.Idle)
    val transfer: StateFlow<TransferState> = _transfer.asStateFlow()
    private val _notificationDestination = MutableStateFlow<String?>(null)
    val notificationDestination: StateFlow<String?> = _notificationDestination.asStateFlow()

    fun addCompetition(name: String) = launchCatching { if (name.isNotBlank()) repository.addCompetition(name) }
    fun reportIncomingFileError(message: String) { _transfer.value = TransferState.Error(message) }
    /** Liga o loading assim que a pessoa escolhe um arquivo, antes da leitura começar. */
    fun beginIncomingFile() { _transfer.value = TransferState.Loading }
    fun setPrimary(id: Long) = launchCatching { repository.setPrimary(id) }
    fun setCompetitionPriorityOverride(id: Long, override: PriorityLevel?) = launchCatching { repository.setCompetitionPriorityOverride(id, override) }
    fun deleteCompetition(value: CompetitionEntity) = launchCatching { repository.deleteCompetition(value) }
    fun addSubject(competitionId: Long, name: String) = launchCatching { if (name.isNotBlank()) repository.addSubject(competitionId, name) }
    fun setSubjectPriorityOverride(id: Long, override: PriorityLevel?) = launchCatching { repository.setSubjectPriorityOverride(id, override) }
    fun deleteSubject(value: SubjectEntity) = launchCatching { repository.deleteSubject(value) }
    fun addTopic(subjectId: Long, title: String) = launchCatching { if (title.isNotBlank()) repository.addTopic(subjectId, title) }
    fun updateTopic(value: TopicEntity) = launchCatching { repository.updateTopic(value) }
    fun setTopicPriorityOverride(id: Long, override: PriorityLevel?) = launchCatching { repository.setTopicPriorityOverride(id, override) }
    fun deleteTopic(value: TopicEntity) = launchCatching { repository.deleteTopic(value) }
    fun markStudied(value: TopicEntity) = launchCatching { repository.markStudied(value, reviewIntervals.value) }
    fun unmarkStudied(value: TopicEntity) = launchCatching { repository.unmarkStudied(value) }
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
    suspend fun completeStudyNow(topicId: Long, startedAt: Long, notes: String = "") = repository.completeStudy(topicId, startedAt, notes)
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

    // ---------------------------------------------------------------- modo foco
    /** Sessão de foco em andamento, lida do disco: sobrevive a fechar o app e a reiniciar o aparelho. */
    val focusSession: StateFlow<FocusSessionPrefs> = app.preferences.focusSession.stateIn(viewModelScope, SharingStarted.Eagerly, FocusSessionPrefs())
    val focusDoNotDisturb = app.preferences.focusDoNotDisturb.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), true)
    val focusKeepScreenOn = app.preferences.focusKeepScreenOn.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), true)
    /** Tempo medido na última sessão encerrada, usado para preencher a conclusão da tarefa do plano. */
    val lastFocusMinutes = app.preferences.lastFocusMinutes.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0)
    val lastFocusTaskId = app.preferences.lastFocusTaskId.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), "")

    fun startFocus(
        title: String,
        topicId: Long? = null,
        taskId: String? = null,
        subjectIds: Set<Long> = emptySet(),
        origin: FocusSessionOrigin = when {
            taskId != null -> FocusSessionOrigin.PLANO
            topicId != null -> FocusSessionOrigin.MATERIA
            else -> FocusSessionOrigin.LIVRE
        },
    ) = launchCatching {
        FocusSessionManager.start(app, title.ifBlank { "Sessão de estudo" }, subjectIds, origin, topicId, taskId)
    }
    /** Devolve os minutos medidos para quem encerrou, a tela usa isso para registrar a tarefa. */
    suspend fun stopFocus(): Int = FocusSessionManager.stop(app)
    fun clearLastFocus() = launchCatching { app.preferences.clearLastFocus() }
    fun setFocusDoNotDisturb(value: Boolean) = launchCatching { app.preferences.setFocusDoNotDisturb(value) }
    fun setFocusKeepScreenOn(value: Boolean) = launchCatching { app.preferences.setFocusKeepScreenOn(value) }
    fun setEditalSubjectExpanded(subjectId: Long, expanded: Boolean) = launchCatching { app.preferences.setEditalSubjectExpanded(subjectId, expanded) }
    fun completeOnboarding() = launchCatching { app.preferences.setOnboardingCompleted(true) }
    fun reopenInitialSetup() = launchCatching {
        app.preferences.updateInitialSetup { current ->
            current.copy(status = InitialSetupStatus.IN_PROGRESS)
        }
    }

    /**
     * Garante IDs externos estáveis para concurso, matérias e tópicos criados à mão. O plano gerado
     * por IA referencia esses IDs; sem eles a importação do .plano não encontra as matérias.
     * Os valores seguem [br.com.estudario.data.prompt.PromptIds], os mesmos usados nos prompts.
     */
    fun ensureExternalIds(competitionId: Long) = launchCatching {
        val dao = app.database.dao()
        dao.competitionsOnce().firstOrNull { it.id == competitionId && it.externalId == null }?.let { dao.updateCompetition(it.copy(externalId = br.com.estudario.data.prompt.PromptIds.competition(it))) }
        dao.subjectsFor(competitionId).forEach { subject ->
            if (subject.externalId == null) dao.updateSubject(subject.copy(externalId = br.com.estudario.data.prompt.PromptIds.subject(subject)))
            dao.topicsFor(subject.id).filter { it.externalId == null }.forEach { topic -> dao.updateTopic(topic.copy(externalId = br.com.estudario.data.prompt.PromptIds.topic(topic))) }
        }
    }

    /**
     * Texto vindo de "Abrir com", "Compartilhar" ou da área de transferência: descobre o tipo e
     * encaminha. Limpeza e detecção fazem parse de JSON, em respostas grandes de IA isso segurava
     * a thread principal, então roda fora dela com o loading já na tela.
     */
    fun openIncomingText(raw: String, targetCompetitionId: Long? = null) = viewModelScope.launch {
        _transfer.value = TransferState.Loading
        val text = withContext(Dispatchers.Default) { IncomingText.clean(raw) }
        when (withContext(Dispatchers.Default) { IncomingFileFormat.detect(text) }) {
            IncomingFileFormat.ESTUDO -> previewEstudo(text, targetCompetitionId)
            IncomingFileFormat.PLANO -> { app.incomingFiles.publishPlan(text); _transfer.value = TransferState.Idle }
            IncomingFileFormat.BACKUP -> _transfer.value = TransferState.Error("Este é um backup completo. Para substituir os dados deste aparelho, use Mais › Restaurar backup.")
            null -> _transfer.value = TransferState.Error(
                if (text.trimStart().startsWith("{")) "O conteúdo não parece um arquivo do Estudário ou o JSON está incompleto (a resposta da IA pode ter sido cortada). Peça para a IA continuar ou gerar de novo e tente outra vez."
                else "Não encontrei um arquivo .estudo ou .plano nesse conteúdo. Copie a resposta inteira da IA (o JSON) ou baixe o arquivo gerado e abra com o Estudário.",
            )
        }
    }
    fun updateTheoryProgress(value: TheoryDocumentEntity, block: Int) = launchCatching { repository.updateTheoryProgress(value, block) }
    fun saveTheoryMark(value: TheoryMarkEntity) = launchCatching { repository.saveTheoryMark(value) }
    fun deleteTheoryMark(value: TheoryMarkEntity) = launchCatching { repository.deleteTheoryMark(value) }
    fun saveEditalPrompt(value: String) = launchCatching { if (value.isNotBlank()) app.preferences.setEditalPrompt(value) }
    fun saveContentPrompt(value: String) = launchCatching { if (value.isNotBlank()) app.preferences.setContentPrompt(value) }
    fun saveStudyPlanPrompt(value: String) = launchCatching { if (value.isNotBlank()) app.preferences.setStudyPlanPrompt(value) }
    fun setNotificationsEnabled(value: Boolean) = launchCatching { app.preferences.setNotificationsEnabled(value); StudyNotificationCoordinator.refresh(app, app.preferences) }
    fun setDailyReminderEnabled(value: Boolean) = launchCatching { app.preferences.setDailyReminderEnabled(value); StudyNotificationCoordinator.refresh(app, app.preferences) }
    fun setPendingAlertsEnabled(value: Boolean) = launchCatching { app.preferences.setPendingAlertsEnabled(value); StudyNotificationCoordinator.refresh(app, app.preferences) }
    fun setReminderTime(hour: Int, minute: Int) = launchCatching { app.preferences.setReminderTime(hour, minute); StudyNotificationCoordinator.refresh(app, app.preferences) }
    fun sendTestNotification() { StudyNotificationCoordinator.showTest(app) }
    fun openFromNotification(destination: String) { _notificationDestination.value = destination }
    fun consumeNotificationDestination() { _notificationDestination.value = null }

    suspend fun answer(question: QuestionWithOptions, selectedKey: String, sessionId: String? = null): Boolean = repository.answer(question, selectedKey, sessionId)
    suspend fun smartQuestions(count: Int): List<QuestionWithOptions> = repository.smartQuestions(count.coerceIn(1, 100))
    suspend fun saveQuestionSession(value: QuestionSessionEntity) = repository.saveQuestionSession(value)
    fun toggleQuestionFavorite(value: QuestionEntity) = launchCatching { repository.toggleQuestionFavorite(value) }

    fun inspectEstudo(text: String) = viewModelScope.launch { previewEstudo(text) }

    private suspend fun previewEstudo(text: String, targetCompetitionId: Long? = null) {
        _transfer.value = TransferState.Loading
        _transfer.value = try {
            withContext(Dispatchers.Default) {
                val clean = IncomingText.clean(text)
                TransferState.Preview(estudoService.preview(clean), clean, targetCompetitionId)
            }
        } catch (e: Exception) { TransferState.Error(e.message ?: "Não foi possível analisar o arquivo.") }
    }

    fun confirmImport(
        raw: String,
        mode: ImportMode = ImportMode.SKIP,
        markAsStudied: Boolean = false,
        targetCompetitionId: Long? = null,
    ) = viewModelScope.launch {
        _transfer.value = TransferState.Loading
        _transfer.value = try {
            val result = withContext(Dispatchers.Default) { estudoService.import(raw, mode, targetCompetitionId) }
            if (markAsStudied) result.importedTopicIds.forEach { topicId -> repository.completeStudy(topicId, System.currentTimeMillis(), "Importado e marcado como estudado") }
            if (result.subjectsCreated + result.topicsCreated > 0) pendingContentTour = true
            TransferState.Success(
                "Edital atualizado: ${result.subjectsCreated} matéria(s) nova(s), ${result.topicsCreated} tópico(s) novo(s) e ${result.topicsUpdated} tópico(s) sincronizado(s). " +
                    "Conteúdo: ${result.theories} teoria(s), ${result.summaries} resumo(s), ${result.snippets} item(ns) de memorização, ${result.questions} questão(ões) e ${result.errorConcepts} conceito(s) de erro. " +
                    "${result.updatedContent} item(ns) atualizado(s) preservando o histórico; ${result.skipped} duplicado(s) ignorado(s)." +
                        (if (result.sources > 0) " ${result.sources} fonte(s) registrada(s) em Mais › Histórico e fontes." else "") +
                        (if (result.downgradedQuestions > 0) " ${result.downgradedQuestions} questão(ões) sem origem comprovada entraram como autorais." else ""),
            )
        } catch (e: Exception) { TransferState.Error(e.message ?: "Falha ao importar.") }
    }

    suspend fun createBackup(): String = withContext(Dispatchers.Default) { backupService.export() }
    fun restoreBackup(text: String) = viewModelScope.launch {
        _transfer.value = TransferState.Loading
        _transfer.value = try { withContext(Dispatchers.Default) { backupService.restore(text) }; TransferState.Success("Backup restaurado com sucesso.") } catch (e: Exception) { TransferState.Error(e.message ?: "Falha ao restaurar backup.") }
    }
    fun clearTransfer() {
        val finishedImport = _transfer.value is TransferState.Success
        _transfer.value = TransferState.Idle
        // Primeiro edital importado: ensina a gerar o conteúdo das matérias.
        if (finishedImport && pendingContentTour) { pendingContentTour = false; maybeStartTour(TourId.CONTENT) }
    }

    private fun launchCatching(block: suspend () -> Unit) = viewModelScope.launch {
        try { block() } catch (e: Exception) { _transfer.value = TransferState.Error(e.message ?: "Ocorreu um erro.") }
    }
}
