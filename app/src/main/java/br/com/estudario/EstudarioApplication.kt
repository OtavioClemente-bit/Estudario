package br.com.estudario

import android.app.Application
import java.io.File
import br.com.estudario.data.StudyRepository
import br.com.estudario.data.FocusSessionRepository
import br.com.estudario.data.local.AppDatabase
import br.com.estudario.data.preferences.AppPreferences
import br.com.estudario.data.planner.StudyExecutionService
import br.com.estudario.data.planner.StudyPlanApplicationService
import br.com.estudario.data.planner.StudyPlanRepository
import br.com.estudario.data.remote.DataStoreSupabaseSessionStore
import br.com.estudario.data.remote.DefaultSupabaseAuthRepository
import br.com.estudario.data.remote.SupabaseAuthRepository
import br.com.estudario.data.remote.SupabaseClientConfig
import br.com.estudario.data.remote.UnavailableSupabaseAuthClient
import br.com.estudario.data.ai.AiJobRecoveryWorker
import br.com.estudario.data.ai.DataStoreAiJobRequestStore
import br.com.estudario.data.ai.DefaultAiSyllabusRepository
import br.com.estudario.data.ai.HttpAiApiClient
import br.com.estudario.data.ai.FilePdfSourceSnapshotStore
import br.com.estudario.data.ai.PdfSourceReader
import br.com.estudario.data.remote.SupabaseAiTokenProvider
import br.com.estudario.data.remote.HttpPrivateSyllabusRemoteApi
import br.com.estudario.data.remote.PrivateSyllabusRepository
import br.com.estudario.data.remote.RemoteSyllabusSyncWorker
import br.com.estudario.ui.ai.AiAccessRepository
import br.com.estudario.ui.ai.DefaultAiAccessRepository
import br.com.estudario.data.transfer.IncomingFileCoordinator
import br.com.estudario.data.transfer.planner.StudyPlanTransferService
import br.com.estudario.domain.planner.StudyPlannerEngine
import br.com.estudario.focus.FocusSessionManager
import br.com.estudario.notifications.StudyNotificationCoordinator
import kotlinx.coroutines.*

class EstudarioApplication : Application() {
    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    lateinit var database: AppDatabase
        private set
    val supabaseClientConfig: SupabaseClientConfig by lazy { SupabaseClientConfig.fromBuildConfig() }
    lateinit var repository: StudyRepository
        private set
    lateinit var focusSessionRepository: FocusSessionRepository
        private set
    lateinit var preferences: AppPreferences
        private set
    lateinit var planRepository: StudyPlanRepository
        private set
    lateinit var planService: StudyPlanApplicationService
        private set
    lateinit var executionService: StudyExecutionService
        private set
    val supabaseAuthRepository: SupabaseAuthRepository by lazy {
        DefaultSupabaseAuthRepository(
            client = UnavailableSupabaseAuthClient(supabaseClientConfig),
            sessionStore = DataStoreSupabaseSessionStore(this, applicationScope),
        )
    }
    val aiSyllabusRepository: DefaultAiSyllabusRepository by lazy {
        DefaultAiSyllabusRepository(
            api = HttpAiApiClient(
                baseUrl = supabaseClientConfig.projectUrl,
                publishableKey = supabaseClientConfig.publishableKey,
                accessTokenProvider = SupabaseAiTokenProvider(supabaseAuthRepository),
            ),
            sourceReader = PdfSourceReader.fromContentResolver(contentResolver),
            requestStore = DataStoreAiJobRequestStore(this),
            accessTokenProvider = SupabaseAiTokenProvider(supabaseAuthRepository),
            sourceSnapshots = FilePdfSourceSnapshotStore(File(filesDir, "ai-syllabus-sources")),
        )
    }
    /** Read-only access boundary shared with the AI review gate and future quota UI. */
    val aiAccessRepository: AiAccessRepository by lazy {
        DefaultAiAccessRepository(
            config = supabaseClientConfig,
            authRepository = supabaseAuthRepository,
        )
    }
    val privateSyllabusRepository: PrivateSyllabusRepository by lazy {
        PrivateSyllabusRepository(
            database = database,
            api = HttpPrivateSyllabusRemoteApi(
                baseUrl = supabaseClientConfig.projectUrl,
                publishableKey = supabaseClientConfig.publishableKey,
                accessTokenProvider = SupabaseAiTokenProvider(supabaseAuthRepository),
            ),
        )
    }
    lateinit var planTransferService: StudyPlanTransferService
        private set
    val incomingFiles = IncomingFileCoordinator()

    override fun onCreate() {
        super.onCreate()
        database = AppDatabase.create(this)
        repository = StudyRepository(database)
        focusSessionRepository = FocusSessionRepository(database.dao())
        preferences = AppPreferences(this)
        planRepository = StudyPlanRepository(database)
        val calendarSyncService = br.com.estudario.data.planner.CalendarSyncService(this)
        planService = StudyPlanApplicationService(database, StudyPlannerEngine(), calendarSyncService)
        executionService = StudyExecutionService(database, planService)
        planTransferService = StudyPlanTransferService(database)
        aiSyllabusRepository
        AiJobRecoveryWorker.enqueue(this)
        RemoteSyllabusSyncWorker.enqueue(this)
        StudyNotificationCoordinator.createChannels(this)
        CoroutineScope(SupervisorJob() + Dispatchers.Default).launch {
            StudyNotificationCoordinator.refresh(this@EstudarioApplication, preferences)
            // Sessão de foco que ficou aberta (app fechado, aparelho reiniciado) é resolvida aqui:
            // ou volta com a notificação, ou é encerrada e o Não Perturbe é devolvido.
            FocusSessionManager.reconcile(this@EstudarioApplication)
        }
    }
}
