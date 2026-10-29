package br.com.estudario

import kotlinx.coroutines.flow.first
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
import br.com.estudario.data.remote.HttpSupabaseAuthClient
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
    /** Catálogo de concursos prontos (busca pública, envio com conta, aprovação por administrador). */
    val contestCatalog: br.com.estudario.data.catalog.ContestCatalogRepository by lazy {
        br.com.estudario.data.catalog.ContestCatalogRepository(supabaseClientConfig, { supabaseAuthRepository.accessToken() })
    }
    lateinit var repository: StudyRepository
        private set
    lateinit var focusSessionRepository: FocusSessionRepository
        private set
    lateinit var preferences: AppPreferences
        private set
    lateinit var planRepository: StudyPlanRepository
        private set
    lateinit var calendarSyncService: br.com.estudario.data.planner.CalendarSyncService
    lateinit var planService: StudyPlanApplicationService
        private set
    lateinit var executionService: StudyExecutionService
        private set
    val supabaseAuthRepository: SupabaseAuthRepository by lazy {
        DefaultSupabaseAuthRepository(
            client = HttpSupabaseAuthClient(supabaseClientConfig),
            sessionStore = DataStoreSupabaseSessionStore(this, applicationScope),
            refreshStore = br.com.estudario.data.remote.DataStoreSupabaseRefreshTokenStore(this),
        )
    }
    val aiSyllabusRepository: DefaultAiSyllabusRepository by lazy {
        DefaultAiSyllabusRepository(
            api = HttpAiApiClient(
                baseUrl = supabaseClientConfig.projectUrl,
                publishableKey = supabaseClientConfig.publishableKey,
                accessTokenProvider = SupabaseAiTokenProvider(supabaseAuthRepository),
                integrityTokens = BuildConfig.PLAY_INTEGRITY_CLOUD_PROJECT_NUMBER.takeIf { it > 0L }?.let {
                    br.com.estudario.data.ai.PlayIntegrityTokenSource(this, it)
                },
            ),
            sourceReader = PdfSourceReader.fromContentResolver(contentResolver),
            requestStore = DataStoreAiJobRequestStore(this),
            accessTokenProvider = SupabaseAiTokenProvider(supabaseAuthRepository),
            sourceSnapshots = FilePdfSourceSnapshotStore(File(filesDir, "ai-syllabus-sources")),
            userIdProvider = { supabaseAuthRepository.currentUserId() },
            scheduleRecovery = { AiJobRecoveryWorker.enqueue(this, ensureFollowUp = true) },
        )
    }
    /** Conteúdo de tópico e plano pela IA do Estudário: mesma função e mesmo login do edital. */
    val aiTextJobClient: br.com.estudario.data.ai.AiTextJobClient by lazy {
        br.com.estudario.data.ai.AiTextJobClient(
            publishableKey = supabaseClientConfig.publishableKey,
            accessTokenProvider = SupabaseAiTokenProvider(supabaseAuthRepository),
            transport = br.com.estudario.data.ai.UrlConnectionAiHttpTransport(supabaseClientConfig.projectUrl),
        )
    }
    /** Read-only access boundary shared with the AI review gate and future quota UI. */
    val aiAccessRepository: AiAccessRepository by lazy {
        DefaultAiAccessRepository(
            config = supabaseClientConfig,
            authRepository = supabaseAuthRepository,
        )
    }
    /** Conteúdo de tópico pela IA do Estudário (fila de jobs de texto no servidor). */
    val aiContentGenerator: br.com.estudario.data.ai.AiContentGenerator by lazy {
        br.com.estudario.data.ai.AiContentGenerator(
            config = supabaseClientConfig,
            authRepository = supabaseAuthRepository,
        )
    }
    /** Simulados: planta, geração em partes, prova e correção. */
    val simulationService: br.com.estudario.data.simulation.SimulationService by lazy {
        br.com.estudario.data.simulation.SimulationService(this, database, repository, aiTextJobClient)
    }
    /** Reporte de erro no material, direto na tabela do Supabase. */
    val contentReportClient: br.com.estudario.data.remote.ContentReportClient by lazy {
        br.com.estudario.data.remote.ContentReportClient(supabaseClientConfig, supabaseAuthRepository)
    }
    /** Assinaturas pelo Google Play: a compra é conferida no servidor antes de valer. */
    val playBilling: br.com.estudario.data.billing.PlayBilling by lazy {
        br.com.estudario.data.billing.PlayBilling(this, supabaseClientConfig, supabaseAuthRepository)
    }
    /** Plano efetivo, catálogo e uso. Somente leitura: quem decide cota é o servidor. */
    val aiPlanRepository: br.com.estudario.ui.plans.AiPlanRepository by lazy {
        br.com.estudario.ui.plans.HttpAiPlanRepository(
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
    /** Sincronização com a conta (celular ⇄ app web). */
    val cloudSync: br.com.estudario.data.sync.CloudSyncService by lazy {
        br.com.estudario.data.sync.CloudSyncService(this, database, supabaseClientConfig, supabaseAuthRepository)
    }
    lateinit var planTransferService: StudyPlanTransferService
        private set
    val incomingFiles = IncomingFileCoordinator()

    override fun onCreate() {
        super.onCreate()
        br.com.estudario.ui.ai.BackgroundAiTasks.init(this)
        br.com.estudario.data.ai.DeviceIdentity.init(this)
        br.com.estudario.data.ai.EditalPdfText.init(this)
        database = AppDatabase.create(this)
        repository = StudyRepository(database)
        focusSessionRepository = FocusSessionRepository(database.dao())
        preferences = AppPreferences(this)
        planRepository = StudyPlanRepository(database)
        calendarSyncService = br.com.estudario.data.planner.CalendarSyncService(this)
        planService = StudyPlanApplicationService(
            database, StudyPlannerEngine(), calendarSyncService,
            excludedTopics = { br.com.estudario.data.planner.LibraryGaps.excluded(this) },
            freeRules = { br.com.estudario.data.planner.LibraryGaps.free(this) },
        )
        executionService = StudyExecutionService(database, planService)
        planTransferService = StudyPlanTransferService(database)
        aiSyllabusRepository
        AiJobRecoveryWorker.enqueue(this)
        RemoteSyllabusSyncWorker.enqueue(this)
        br.com.estudario.data.sync.CloudSyncWorker.schedulePeriodic(this)
        br.com.estudario.data.sync.CloudSyncWorker.syncSoon(this)
        // Simulado que estava sendo gerado quando o app fechou continua de onde parou.
        simulationService.resumeAll()
        StudyNotificationCoordinator.createChannels(this)
        br.com.estudario.notifications.AiGenerationNotifications.createChannels(this)
        CoroutineScope(SupervisorJob() + Dispatchers.Default).launch {
            StudyNotificationCoordinator.refresh(this@EstudarioApplication, preferences)
            // O edital escolhido no catálogo se atualiza sozinho. Espera a sincronização com o site
            // terminar (até 1 minuto), para o app e o site não acrescentarem o mesmo tópico duas vezes.
            if (cloudSync.enabled) kotlinx.coroutines.withTimeoutOrNull(60_000) {
                cloudSync.status.first { it is br.com.estudario.data.sync.CloudSyncStatus.Done || it is br.com.estudario.data.sync.CloudSyncStatus.Failed || it is br.com.estudario.data.sync.CloudSyncStatus.SignedOut }
            }
            runCatching { br.com.estudario.data.catalog.EditalUpdate.runDaily(this@EstudarioApplication) }.getOrNull()?.forEach { e ->
                val parts = listOfNotNull(
                    e.added.takeIf { it > 0 }?.let { if (it == 1) "1 tópico novo" else "$it tópicos novos" },
                    e.renamed.takeIf { it > 0 }?.let { if (it == 1) "1 nome corrigido" else "$it nomes corrigidos" },
                    e.removed.takeIf { it > 0 }?.let { if (it == 1) "1 tópico saiu do edital (guardado, fora do plano)" else "$it tópicos saíram do edital (guardados, fora do plano)" },
                )
                StudyNotificationCoordinator.post(
                    this@EstudarioApplication, "study_reminders", 1011, "Atualizei o edital ${e.competition}",
                    "${parts.joinToString(", ").replaceFirstChar { it.uppercase() }}. Seu progresso, matérias e erros continuam.",
                    mood = br.com.estudario.ui.assistant.FolhaMood.PROUD,
                )
            }
            // As matérias da biblioteca que a equipe corrigiu se atualizam no lugar (uma vez por dia).
            runCatching { br.com.estudario.data.library.LibraryRefresh.runDaily(this@EstudarioApplication) }.getOrNull()?.takeIf { it.isNotEmpty() }?.let { updated ->
                StudyNotificationCoordinator.post(
                    this@EstudarioApplication, "study_reminders", 1010,
                    if (updated.size == 1) "Atualizei a matéria de ${updated[0].title}" else "Atualizei ${updated.size} matérias suas",
                    if (updated.size == 1) "A equipe corrigiu: ${updated[0].parts.joinToString(", ")}. Suas respostas, grifos e revisões continuam."
                    else "A equipe corrigiu ${updated.take(3).joinToString(", ") { it.title }}${if (updated.size > 3) " e outras" else ""}. Suas respostas, grifos e revisões continuam.",
                    mood = br.com.estudario.ui.assistant.FolhaMood.PROUD,
                )
            }
            // Sessão de foco que ficou aberta (app fechado, aparelho reiniciado) é resolvida aqui:
            // ou volta com a notificação, ou é encerrada e o Não Perturbe é devolvido.
            FocusSessionManager.reconcile(this@EstudarioApplication)
            // Edital do catálogo escolhido antes de ter o peso da prova recebe as prioridades agora.
            br.com.estudario.data.catalog.ExamWeightsApplier.backfill(database.dao(), br.com.estudario.data.catalog.ExamCatalogProvider.get(this@EstudarioApplication))
        }
    }
}
