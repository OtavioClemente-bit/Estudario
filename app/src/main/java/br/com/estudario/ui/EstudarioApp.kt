package br.com.estudario.ui

import br.com.estudario.ui.brand.dottedPaper
import br.com.estudario.ui.components.AlertDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import br.com.estudario.ui.brand.Surface
import br.com.estudario.ui.brand.Icon
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.compose.*
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import br.com.estudario.ui.screens.*
import br.com.estudario.ui.focus.FocusScreen
import br.com.estudario.ui.focus.FocusHistoryScreen
import br.com.estudario.ui.theme.EstudarioTheme
import br.com.estudario.ui.theme.estudarioLayout
import br.com.estudario.data.transfer.ImportMode
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.platform.testTag
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.estudario.EstudarioApplication
import br.com.estudario.BuildConfig
import br.com.estudario.ui.planner.PlanScreen
import br.com.estudario.ui.planner.PlanTransferUiState
import br.com.estudario.ui.planner.StudyPlanViewModel
import br.com.estudario.ui.planner.StudyPlanViewModelFactory
import br.com.estudario.ui.planner.countsAsPlannedLoad
import androidx.activity.compose.BackHandler
import br.com.estudario.ui.focus.FocusSessionSheet
import br.com.estudario.ui.focus.FocusTimerActions
import br.com.estudario.ui.focus.FocusTimerBar
import br.com.estudario.ui.components.LoadingDialog
import br.com.estudario.ui.profile.BadgeCelebrationScreen
import br.com.estudario.ui.profile.BadgesScreen
import br.com.estudario.ui.profile.ProfileScreen
import br.com.estudario.ui.profile.StreakCelebrationScreen
import br.com.estudario.ui.tour.TourId
import br.com.estudario.ui.tour.TourKey
import br.com.estudario.ui.tour.TourOverlay
import br.com.estudario.ui.tour.TutorialVideo
import br.com.estudario.ui.tour.TutorialVideoDialog
import br.com.estudario.ui.tour.HelpGuide
import br.com.estudario.ui.tour.helpGuideOptions
import br.com.estudario.ui.tour.tourForRoute
import br.com.estudario.ui.tour.tourKeyForRoute
import br.com.estudario.ui.tour.tourSteps
import br.com.estudario.ui.tour.tourTarget
import br.com.estudario.ui.tour.WELCOME_TOURS
import br.com.estudario.ui.navigation.EstudarioDrawerContent
import br.com.estudario.ui.library.MySyllabiScreen
import br.com.estudario.ui.library.MySyllabiViewModel
import br.com.estudario.ui.library.MySyllabiViewModelFactory
import br.com.estudario.ui.navigation.EstudarioTopBar
import br.com.estudario.ui.navigation.estudarioDrawerSections
import br.com.estudario.ui.navigation.FocusNavigationIcon
import br.com.estudario.ui.onboarding.OnboardingFlow
import br.com.estudario.ui.setup.InitialSetupFlow
import br.com.estudario.ui.setup.InitialSetupViewModel
import br.com.estudario.domain.setup.InitialSetupStatus
import br.com.estudario.ui.ai.AiReviewEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun EstudarioApp(viewModel: AppViewModel) {
    val themeMode by viewModel.themeMode.collectAsState()
    val dark = when (themeMode) { "DARK" -> true; "LIGHT" -> false; else -> isSystemInDarkTheme() }
    EstudarioTheme(dark) {
        // O visual do Folha (armário, sequência, hora do dia) vale para todos os Folhas do app.
        br.com.estudario.ui.profile.FolhaLookSync(viewModel)
        val onboardingConcluido by viewModel.hasCompletedOnboarding.collectAsState()
        val seenTours by viewModel.seenTours.collectAsState()
        val initialSetup by viewModel.initialSetup.collectAsState()
        val hasExistingWorkspace by viewModel.hasExistingWorkspace.collectAsState()
        val aiReviewTarget by viewModel.aiReviewTarget.collectAsState()
        var catalogOffer by remember { mutableStateOf<br.com.estudario.ui.ai.AiReviewTarget?>(null) }
        val catalogCompetitions by viewModel.competitions.collectAsState()
        catalogOffer?.let { offer ->
            if (aiReviewTarget == null) catalogCompetitions.firstOrNull { it.id == offer.id }?.let { competition ->
                br.com.estudario.ui.catalog.CatalogSubmitDialog(
                    competition = competition,
                    viewModel = viewModel,
                    onDismiss = { catalogOffer = null },
                    initialRole = offer.preferences?.role.orEmpty(),
                    initialBoard = offer.preferences?.board.orEmpty(),
                    initialYear = offer.preferences?.year.orEmpty(),
                    offeredAfterGeneration = true,
                )
            }
        }
        val setupViewModel: InitialSetupViewModel = viewModel()
        LaunchedEffect(initialSetup?.status, hasExistingWorkspace) {
            if (initialSetup?.status == InitialSetupStatus.NOT_STARTED && hasExistingWorkspace == true) {
                setupViewModel.bypassForExistingWorkspace()
            }
        }
        when {
            aiReviewTarget != null -> AiReviewEntryPoint(
                target = aiReviewTarget!!,
                onClose = viewModel::closeAiReview,
                onReopen = aiReviewTarget!!.let { t -> { viewModel.openAiReview(t.id, t.title) } },
                onLocalApplied = {
                    // Edital montado e aprovado: oferece deixar pronto no catálogo para os próximos.
                    catalogOffer = aiReviewTarget
                    setupViewModel.onAiSyllabusApplied().invokeOnCompletion { viewModel.closeAiReview() }
                },
            )
            // null: a splash do sistema ainda cobre a tela enquanto a preferência carrega.
            onboardingConcluido == null || seenTours == null || initialSetup == null || hasExistingWorkspace == null -> Unit
            // Primeira instalação: apresentação e escolha de conta antes de qualquer tela do app.
            onboardingConcluido == false -> OnboardingFlow(viewModel) { viewModel.completeOnboarding() }
            initialSetup?.status == InitialSetupStatus.IN_PROGRESS ||
                (initialSetup?.status == InitialSetupStatus.NOT_STARTED && hasExistingWorkspace == false) ->
                InitialSetupFlow(setupViewModel, viewModel) { }
            // O tour guiado (replay em Ajustes > Como usar o app) roda dentro da própria navegação
            // principal, destacando os botões reais - ver MainNavigation.
            else -> MainNavigation(viewModel)
        }
    }
}

@Composable
private fun MainNavigation(viewModel: AppViewModel) {
    val navController = rememberNavController()
    val application = LocalContext.current.applicationContext as EstudarioApplication
    val planViewModel: StudyPlanViewModel = viewModel(factory = StudyPlanViewModelFactory(application))
    val incomingPlan by application.incomingFiles.pendingPlan.collectAsState()
    // Guarda o arquivo já tratado: mesmo que este efeito reinicie (recomposição, troca de tema),
    // o mesmo .plano não navega duas vezes, era assim que a aba Plano voltava sozinha.
    var planoJaTratado by rememberSaveable { mutableStateOf<Long?>(null) }
    LaunchedEffect(incomingPlan?.token) {
        val incoming = incomingPlan ?: return@LaunchedEffect
        if (planoJaTratado == incoming.token) return@LaunchedEffect
        planoJaTratado = incoming.token
        // Mesma navegação da barra de baixo: abrir o plano importado não empilha uma segunda cópia
        // da aba, que era o que fazia o "Início" precisar de dois toques.
        navController.navigate("plan") { popUpTo("home") { saveState = true }; launchSingleTop = true; restoreState = true }
        planViewModel.inspectPlan(incoming.raw)
        application.incomingFiles.consumePlan(incoming.token)
    }
    val notificationDestination by viewModel.notificationDestination.collectAsState()
    LaunchedEffect(notificationDestination) {
        notificationDestination?.let { destination -> navController.navigate(destination) { launchSingleTop = true }; viewModel.consumeNotificationDestination() }
    }
    // Guias: o de primeiros passos abre sozinho na primeira abertura; os de Plano e Treinar abrem
    // na primeira visita a cada aba; o de conteúdo abre depois do primeiro edital importado.
    // Eles podem ser repetidos pelo seletor de ajuda.
    val seenTours by viewModel.seenTours.collectAsState()
    val activeTour by viewModel.activeTour.collectAsState()
    val tourStep by viewModel.tourStep.collectAsState()
    val tourStepIndex by viewModel.tourStepIndex.collectAsState()
    val tourBounds by viewModel.tourTargetBounds.collectAsState()
    var showTourPicker by remember { mutableStateOf(false) }
    var showPlansFromMenu by remember { mutableStateOf(false) }
    var showHelpGuidePicker by remember { mutableStateOf(false) }
    var tutorialToShow by remember { mutableStateOf<TutorialVideo?>(null) }
    BackHandler(enabled = tourStep != null && tutorialToShow == null) { viewModel.stopTour() }
    val entry by navController.currentBackStackEntryAsState()
    val currentRoute = entry?.destination?.route
    val focusSession by viewModel.focusSession.collectAsState()
    var showFocusOverlay by rememberSaveable { mutableStateOf(false) }
    var focusBarCollapsed by rememberSaveable { mutableStateOf(false) }
    var focusSheetOpen by remember { mutableStateOf(false) }
    val backDispatcher = androidx.activity.compose.LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    val focusDnd by viewModel.focusDoNotDisturb.collectAsState()
    val focusKeepOn by viewModel.focusKeepScreenOn.collectAsState()
    val focusScope = rememberCoroutineScope()
    // Esqueceu o foco ligado: o app ficou 20 minutos ou mais fora da tela com o foco contando. Na volta
    // ele pergunta se a pessoa estava estudando (lendo no papel conta) e, se não, desconta o tempo parado.
    val focusLifecycle = androidx.lifecycle.compose.LocalLifecycleOwner.current
    val focusContext = androidx.compose.ui.platform.LocalContext.current
    var focusLeftAt by remember { mutableStateOf(0L) }
    var focusIdleGap by remember { mutableStateOf<Long?>(null) }
    val focusNowState = androidx.compose.runtime.rememberUpdatedState(focusSession)
    androidx.compose.runtime.DisposableEffect(focusLifecycle) {
        val observer = androidx.lifecycle.LifecycleEventObserver { _, event ->
            when (event) {
                androidx.lifecycle.Lifecycle.Event.ON_STOP -> focusLeftAt = System.currentTimeMillis()
                androidx.lifecycle.Lifecycle.Event.ON_START -> {
                    val s = focusNowState.value
                    val gap = if (focusLeftAt > 0L) System.currentTimeMillis() - focusLeftAt else 0L
                    if (s.active && !s.paused && gap >= 20 * 60_000L) focusIdleGap = gap.coerceAtMost(s.elapsedMillis())
                    focusLeftAt = 0L
                }
                else -> Unit
            }
        }
        focusLifecycle.lifecycle.addObserver(observer)
        onDispose { focusLifecycle.lifecycle.removeObserver(observer) }
    }
    focusIdleGap?.let { gap ->
        val minutes = (gap / 60_000L).toInt()
        val label = if (minutes >= 60) "${minutes / 60} h ${minutes % 60} min" else "$minutes min"
        br.com.estudario.ui.components.AlertDialog(
            onDismissRequest = { focusIdleGap = null },
            title = { androidx.compose.material3.Text("Você estava estudando?") },
            text = { androidx.compose.material3.Text("O foco ficou ligado e o app ficou fechado por $label. Se você estava lendo no livro ou fazendo exercício no papel, pode contar tudo.") },
            confirmButton = { androidx.compose.material3.TextButton(onClick = { focusIdleGap = null }) { androidx.compose.material3.Text("Sim, contar") } },
            dismissButton = { androidx.compose.material3.TextButton(onClick = { focusIdleGap = null; focusScope.launch { br.com.estudario.focus.FocusSessionManager.discount(focusContext, gap) } }) { androidx.compose.material3.Text("Não, descontar $label") } },
        )
    }
    val focusHidden by viewModel.focusClockHidden.collectAsState()
    val focusActions = FocusTimerActions(
        onOpen = { focusSheetOpen = true },
        onCollapse = { focusBarCollapsed = true },
        onExpand = { focusBarCollapsed = false },
        onTogglePause = viewModel::toggleFocusPause,
        onToggleHidden = viewModel::toggleFocusClockHidden,
    )
    var focusClockNow by remember { mutableLongStateOf(System.currentTimeMillis()) }
    LaunchedEffect(focusSession.active, focusSession.startedAt) {
        while (focusSession.active) {
            focusClockNow = System.currentTimeMillis()
            delay(1_000)
        }
    }
    // Aviso de tempo planejado: uma vez por sessão (guardado, para não repetir ao reabrir o app),
    // quando o tempo estudado alcança o que o plano reservou. A sessão continua contando.
    val focusPlanned by viewModel.focusPlannedMinutes.collectAsState()
    val focusPlanState by planViewModel.state.collectAsState()
    val focusPlanTask = focusPlanState.tasks.firstOrNull { it.entity.id == focusSession.taskId }
    val canCompleteFocusTopic = focusSession.taskId == null || focusPlanTask?.entity?.type in setOf(
        br.com.estudario.domain.planner.PlanTaskType.THEORY,
        br.com.estudario.domain.planner.PlanTaskType.REVIEW,
    )
    val uiPrefs = androidx.compose.ui.platform.LocalContext.current.getSharedPreferences("estudario_ui", android.content.Context.MODE_PRIVATE)
    var focusTimeUpSeen by remember(focusSession.sessionId) { mutableStateOf(uiPrefs.getString("focus_timeup_session", "") == focusSession.sessionId) }
    val planned = focusPlanned
    if (focusSession.active && !focusSession.paused && !focusTimeUpSeen && planned != null && focusSession.elapsedMinutes(focusClockNow) >= planned) {
        fun markSeen() { focusTimeUpSeen = true; uiPrefs.edit().putString("focus_timeup_session", focusSession.sessionId).apply() }
        br.com.estudario.ui.focus.FocusTimeUpDialog(
            plannedMinutes = planned,
            title = focusSession.title,
            onContinue = { markSeen() },
            onFinish = { markSeen(); focusSheetOpen = true },
            onFinishAndComplete = focusSession.topicId?.takeIf { canCompleteFocusTopic }?.let { topicId ->
                {
                    markSeen()
                    val startedAt = focusSession.startedAt
                    val taskId = focusSession.taskId
                    focusScope.launch {
                        val plannedTask = planViewModel.matchingPendingTask(
                            topicId, taskId, focusPlanTask?.entity?.type ?: br.com.estudario.domain.planner.PlanTaskType.THEORY,
                        )
                        val minutes = viewModel.stopFocus()
                        if (taskId != null && plannedTask == null) return@launch
                        if (plannedTask != null) runCatching {
                            planViewModel.completeFromTopic(
                                plannedTask.entity.id,
                                br.com.estudario.data.planner.CompleteTaskInput(startedAt = startedAt, completedAt = System.currentTimeMillis(), actualMinutes = minutes),
                            )
                        }
                        if (plannedTask?.entity?.type == br.com.estudario.domain.planner.PlanTaskType.REVIEW) {
                            viewModel.reviews.value.filter { it.topicId == topicId && it.completedAt == null && it.ignoredAt == null }
                                .minByOrNull { it.dueAt }?.let { viewModel.completeReview(it, startedAt = startedAt) }
                        } else runCatching { viewModel.completeStudyNow(topicId, startedAt) }
                        plannedTask?.let { runCatching { planViewModel.replanAfterTopicCompletion(it.entity.planId) } }
                    }
                }
            },
        )
    }
    // Nenhum guia abre por cima de uma importação em andamento: a pessoa está no meio de um
    // diálogo, e o guia aparecendo ali é o que fazia a tela parecer travada depois de importar.
    val appTransfer by viewModel.transfer.collectAsState()
    val planTransfer by planViewModel.transfer.collectAsState()
    val planBusy by planViewModel.busy.collectAsState()
    val importandoAlgo = appTransfer != TransferState.Idle || planTransfer != PlanTransferUiState.Idle || planBusy != null
    val initialSetupState by viewModel.initialSetup.collectAsState()
    LaunchedEffect(currentRoute, activeTour, seenTours, importandoAlgo, initialSetupState?.status) {
        val tour = tourForRoute(currentRoute, setupSkipped = initialSetupState?.status == InitialSetupStatus.DEFERRED) ?: return@LaunchedEffect
        if (importandoAlgo) return@LaunchedEffect
        // Um só giro de boas-vindas por pessoa, venha ela da configuração completa ou pulada.
        if (tour in WELCOME_TOURS && WELCOME_TOURS.any { seenTours?.contains(it.name) == true }) return@LaunchedEffect
        // O guia de perfil só entra depois do de primeiros passos: dois guias emendados na primeira
        // abertura cansam, e ele só faz sentido quando já existe XP para mostrar.
        if (tour == TourId.PROFILE && seenTours?.contains(TourId.EDITAL.name) != true) return@LaunchedEffect
        if (activeTour == null && seenTours?.contains(tour.name) == false) { delay(450); viewModel.maybeStartTour(tour) }
    }
    // Se a pessoa sair da aba do guia por conta própria, o guia termina ali. Insistir em trazê-la
    // de volta é o que parecia defeito: tocava em Início e a aba do guia voltava sozinha.
    LaunchedEffect(currentRoute, tourStep) {
        val step = tourStep ?: return@LaunchedEffect
        if (currentRoute == null || currentRoute == step.route) return@LaunchedEffect
        // Espera a navegação do próprio guia assentar antes de concluir que foi a pessoa que saiu.
        delay(350)
        if (navController.currentDestination?.route != step.route) viewModel.stopTour()
    }
    // Cada passo diz em qual aba acontece: o guia leva a pessoa até lá, sem ela precisar tocar.
    LaunchedEffect(tourStep) {
        val route = tourStep?.route ?: return@LaunchedEffect
        if (navController.currentDestination?.route != route) navController.navigate(route) { popUpTo("home") { saveState = true }; launchSingleTop = true; restoreState = true }
    }
    // Só entra na navegação de baixo o que é destino diário. O resto, acompanhamento, ajustes,
    // ferramentas, vive no menu lateral, que é onde dá para nomear as coisas sem inventar uma aba
    // chamada "Mais" para guardar o que não coube.
    val destinations = listOf(
        br.com.estudario.ui.brand.BrandTab("home", "Início", br.com.estudario.ui.brand.Glyph.House),
        br.com.estudario.ui.brand.BrandTab("syllabus", "Concursos", br.com.estudario.ui.brand.Glyph.Books),
        br.com.estudario.ui.brand.BrandTab("plan", "Plano", br.com.estudario.ui.brand.Glyph.Calendar),
        br.com.estudario.ui.brand.BrandTab("train", "Treinar", br.com.estudario.ui.brand.Glyph.Target),
        br.com.estudario.ui.brand.BrandTab("simulations", "Simulados", br.com.estudario.ui.brand.Glyph.Timer),
        br.com.estudario.ui.brand.BrandTab("notebook", "Caderno", br.com.estudario.ui.brand.Glyph.Notebook),
    )
    val showBottom = currentRoute in destinations.map { it.route }
    val drawerGestureDisabled = currentRoute in setOf(
        "topic/{id}",
        "topic/{id}/task/{taskId}",
        "theory/{id}?block={block}",
        "quiz/{count}/{topic}/{subject}/{mode}/{board}/{difficulty}",
        "review-session/{id}",
        "focus",
        "plan-quiz/{taskId}/{count}/{topic}/{subject}",
    )
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val drawerScope = rememberCoroutineScope()
    val profile by viewModel.profile.collectAsState()
    val progress by viewModel.progress.collectAsState()
    val initialSetup by viewModel.initialSetup.collectAsState()
    val calendarPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions(),
    ) { permissions ->
        if (permissions.entries.all { it.value }) planViewModel.generate()
    }
    fun abrirDoMenu(route: String) {
        drawerScope.launch { drawerState.close() }
        navController.navigate(route) { launchSingleTop = true }
    }
    // Pelo menu, a aba sempre abre no começo dela. Restaurar a pilha salva levava de volta à mesma
    // tela interna (ex.: um tópico) e parecia que o toque não tinha feito nada.
    fun abrirAbaDoMenu(route: String) {
        drawerScope.launch { drawerState.close() }
        if (route == "home") { if (!navController.popBackStack("home", inclusive = false)) navController.navigate("home") { launchSingleTop = true }; return }
        navController.navigate(route) { popUpTo("home"); launchSingleTop = true }
    }
    fun voltarParaInicio() {
        if (!navController.popBackStack("home", inclusive = false)) {
            navController.navigate("home") { launchSingleTop = true }
        }
    }
    fun navegarAba(route: String) {
        if (route == "home") {
            voltarParaInicio()
        } else {
            navController.navigate(route) { popUpTo("home") { saveState = true }; launchSingleTop = true; restoreState = true }
        }
    }
    fun sincronizarAgenda() {
        drawerScope.launch {
            drawerState.close()
            calendarPermissionLauncher.launch(
                arrayOf(
                    android.Manifest.permission.READ_CALENDAR,
                    android.Manifest.permission.WRITE_CALENDAR,
                ),
            )
        }
    }
    // Voltar do celular fecha o menu antes de sair da tela.
    BackHandler(enabled = drawerState.isOpen) { drawerScope.launch { drawerState.close() } }
    Box(Modifier.fillMaxSize()) {
        ModalNavigationDrawer(
            drawerState = drawerState,
            // O botão Estudário permanece visível em todas as rotas; só o gesto de borda cede aos
            // leitores, ao quiz, à revisão ativa e ao cronômetro de foco.
            // Aberto, o menu sempre fecha por toque fora ou arrastando, mesmo nessas telas.
            gesturesEnabled = !drawerGestureDisabled || drawerState.isOpen,
            drawerContent = {
                EstudarioDrawerContent(
                    profile = profile,
                    level = progress?.level,
                    totalXp = progress?.totalXp,
                    currentRoute = currentRoute,
                    sections = estudarioDrawerSections(
                        onMySyllabi = { abrirDoMenu("my-syllabi") },
                        onNotebook = { abrirAbaDoMenu("notebook") },
                        onHome = { abrirAbaDoMenu("home") },
                        onSyllabus = { abrirAbaDoMenu("syllabus") },
                        onPlan = { abrirAbaDoMenu("plan") },
                        onTrain = { abrirAbaDoMenu("train") },
                        onSimulations = { abrirAbaDoMenu("simulations") },
                        onReviews = { abrirDoMenu("reviews") },
                        onErrors = { abrirDoMenu("errors") },
                        onFocus = { drawerScope.launch { drawerState.close() }; showFocusOverlay = true },
                        onFocusHistory = { abrirDoMenu("focus-history") },
                        onStatistics = { abrirDoMenu("statistics") },
                        onBadges = { abrirDoMenu("badges") },
                        onSources = { abrirDoMenu("sources") },
                        onWardrobe = { abrirDoMenu("wardrobe") },
                        onSettings = { abrirDoMenu("more") },
                        onNotifications = { abrirDoMenu("notifications") },
                        onSyncCalendar = { abrirDoMenu("agenda") },
                        onHelp = { drawerScope.launch { drawerState.close() }; showTourPicker = true },
                        onPlans = { drawerScope.launch { drawerState.close() }; showPlansFromMenu = true },
                    ),
                    appVersion = "Estudário ${BuildConfig.VERSION_NAME}",
                    onOpenProfile = { abrirDoMenu("profile") },
                )
            },
        ) {
        Scaffold(
            // O papel de caderno pontilhado atrás de todas as telas, como no site.
            modifier = Modifier.dottedPaper(),
            containerColor = androidx.compose.ui.graphics.Color.Transparent,
            // Fundo transparente não diz a cor do texto: sem isto, todo texto sem cor própria (os
            // títulos) saía preto, ilegível no tema escuro.
            contentColor = MaterialTheme.colorScheme.onBackground,
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            topBar = { Column {
                if (currentRoute != null) EstudarioTopBar(
                    profile = profile,
                    onOpenMenu = { drawerScope.launch { drawerState.open() } },
                    onSearch = { navController.navigate("search") },
                    onProfile = { navController.navigate("profile") },
                    // A seta passa pelo mesmo caminho do voltar do celular: a tela pode perguntar antes
                    // de sair (ex.: parar o cronômetro do tópico, sair da prova do simulado).
                    onBack = if (showBottom || currentRoute in setOf("focus", "review-session/{id}", "quiz/{count}/{topic}/{subject}/{mode}/{board}/{difficulty}", "plan-quiz/{taskId}/{count}/{topic}/{subject}")) null else ({
                        backDispatcher?.onBackPressed() ?: run { if (!navController.popBackStack()) navController.navigate("home") }
                    }),
                    title = when {
                        currentRoute == "profile" -> "Perfil"
                        currentRoute == "my-syllabi" -> "Meus concursos"
                        currentRoute == "badges" -> "Emblemas"
                        currentRoute == "wardrobe" -> "Armário do Folha"
                        currentRoute == "sources" -> "Histórico e fontes"
                        currentRoute == "focus-history" -> "Histórico de foco"
                        currentRoute == "theory/{id}?block={block}" -> "Leitura"
                        currentRoute == "notebook" || currentRoute == "notebook/flip" || currentRoute == "notebook/page/{id}" -> "Caderno de estudo"
                        currentRoute == "notebook/saved" -> "Grifos, notas e salvos"
                        currentRoute == "agenda" -> "Agenda"
                        else -> null
                    },
                    profileModifier = if (showBottom) Modifier.tourTarget(TourKey.HOME_PROFILE, tourStep?.key) { viewModel.reportTourTargetBounds(TourKey.HOME_PROFILE, it) } else Modifier,
                    menuModifier = Modifier.tourTarget(TourKey.NAV_MENU, tourStep?.key) { viewModel.reportTourTargetBounds(TourKey.NAV_MENU, it) },
                    trailing = { if (focusBarCollapsed) br.com.estudario.ui.focus.FocusTimerPill(focusSession, focusClockNow, focusHidden, focusActions) },
                    compactBrand = focusBarCollapsed && focusSession.active,
                )
                FocusTimerBar(focusSession, focusClockNow, focusBarCollapsed, focusHidden, focusActions)
                // Gerações da IA que seguem em segundo plano, visíveis em qualquer tela.
                // Uma vez só: mostra que o botão Estudário abre um menu com o resto do app.
                val hintPrefs = androidx.compose.ui.platform.LocalContext.current.getSharedPreferences("estudario_ui", android.content.Context.MODE_PRIVATE)
                var drawerHintSeen by remember { mutableStateOf(hintPrefs.getBoolean("drawer_hint_seen", false)) }
                LaunchedEffect(drawerState.isOpen) { if (drawerState.isOpen && !drawerHintSeen) { drawerHintSeen = true; hintPrefs.edit().putBoolean("drawer_hint_seen", true).apply() } }
                // O tour de boas-vindas já apresenta o menu: depois dele, esta dica não aparece.
                if (!drawerHintSeen && currentRoute == "home" && tourStep == null && WELCOME_TOURS.none { seenTours?.contains(it.name) == true }) {
                    Surface(
                        onClick = { drawerScope.launch { drawerState.open() } },
                        shape = br.com.estudario.ui.theme.EstudarioShapes.row,
                        color = MaterialTheme.colorScheme.inverseSurface,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                    ) {
                        Row(Modifier.padding(start = 14.dp, end = 4.dp, top = 6.dp, bottom = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                            br.com.estudario.ui.brand.BrandMenuMark(size = 22.dp)
                            Spacer(Modifier.width(10.dp))
                            Text("Toque no Estudário, no alto, para abrir o menu: Caderno, Revisões, Desempenho, Agenda e Ajustes.", Modifier.weight(1f), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.inverseOnSurface)
                            IconButton(onClick = { drawerHintSeen = true; hintPrefs.edit().putBoolean("drawer_hint_seen", true).apply() }) {
                                Icon(Icons.Outlined.Close, "Entendi", Modifier.size(18.dp), tint = MaterialTheme.colorScheme.inverseOnSurface)
                            }
                        }
                    }
                }
                br.com.estudario.ui.ai.BackgroundAiBanner(onOpen = { text, competitionId -> viewModel.openIncomingText(text, competitionId) })
            } },
            bottomBar = {
                if (showBottom) br.com.estudario.ui.brand.BrandBottomBar(
                    tabs = destinations,
                    currentRoute = currentRoute,
                    onSelect = { navegarAba(it) },
                    modifier = Modifier.testTag("main-bottom-navigation"),
                    itemModifier = { destination ->
                        val tourKey = tourKeyForRoute(destination.route)
                        if (tourKey != null) Modifier.tourTarget(tourKey, tourStep?.key) { viewModel.reportTourTargetBounds(tourKey, it) } else Modifier
                    },
                )
            },
        ) { padding ->
            NavHost(
                navController,
                startDestination = "home",
                // Camada própria: as animações das telas não fazem redesenhar o fundo, a barra e o topo.
                modifier = Modifier
                    .then(Modifier.graphicsLayer {})
                    .padding(padding)
                    .consumeWindowInsets(padding)
                    .withSecondaryRouteBottomInset(showBottom)
                    // Em tablet/paisagem o conteúdo não estica de ponta a ponta: fica centralizado com
                    // largura de leitura. Em celular (menos de 720dp) isto não muda nada.
                    .padding(horizontal = estudarioLayout().centeredContentInset()),
            ) {
                composable("home") {
                    HomeScreen(
                        viewModel = viewModel,
                        planViewModel = planViewModel,
                        onQuiz = { count, mode -> navController.navigate("quiz/$count/0/0/$mode/_/_") },
                        onTopic = { navController.navigate("topic/$it") },
                        onTrainSubject = { subjectId -> navController.navigate("quiz/10/0/$subjectId/random/_/_") },
                        onStudyTask = { topicId, taskId -> navController.navigate("topic/$topicId/task/$taskId") },
                        onReviews = { navController.navigate("reviews") },
                        onProfile = { navController.navigate("profile") },
                        onSyllabus = { navController.navigate("syllabus") { popUpTo("home") { saveState = true }; launchSingleTop = true; restoreState = true } },
                        onPlan = { navController.navigate("plan") { popUpTo("home") { saveState = true }; launchSingleTop = true; restoreState = true } },
                        onOpenQueue = { navController.navigate("queue") },
                        onFocus = { showFocusOverlay = true },
                        onStatistics = { navController.navigate("statistics") },
                        onErrors = { navController.navigate("errors") },
                        showSetupCta = initialSetup?.status == InitialSetupStatus.DEFERRED,
                        onSetup = viewModel::reopenInitialSetup,
                        onPlanQuestions = { task, count -> navController.navigate("plan-quiz/${Uri.encode(task.id)}/$count/${task.topicId ?: 0}/${task.subjectId ?: 0}") },
                    )
                }
                composable("syllabus") { EditalScreen(viewModel, onTopic = { navController.navigate("topic/$it") }, onHelp = { showHelpGuidePicker = true }, onSavedEditais = { navController.navigate("my-syllabi") }) }
                composable("my-syllabi") {
                    val application = LocalContext.current.applicationContext as EstudarioApplication
                    val libraryViewModel: MySyllabiViewModel = viewModel(factory = MySyllabiViewModelFactory(application))
                    MySyllabiScreen(libraryViewModel)
                }
                composable("focus") {
                    // Migra aberturas antigas da rota para a janela sobreposta sem deixar a aba no histórico.
                    LaunchedEffect(Unit) {
                        showFocusOverlay = true
                        if (!navController.popBackStack()) {
                            navController.navigate("home") { popUpTo("focus") { inclusive = true }; launchSingleTop = true }
                        }
                    }
                }
                composable("plan") {
                    PlanScreen(
                        planViewModel,
                        viewModel,
                        onOpenTopic = { navController.navigate("topic/$it") },
                        onOpenTopicTask = { topicId, taskId -> navController.navigate("topic/$topicId/task/$taskId") },
                        onOpenErrors = { navController.navigate("errors") },
                        onFocus = { showFocusOverlay = true },
                        onHelp = { viewModel.startTour(TourId.PLAN) },
                        onReviews = { navController.navigate("reviews") },
                        onStartQuestions = { task, count ->
                            navController.navigate("plan-quiz/${Uri.encode(task.id)}/$count/${task.topicId ?: 0}/${task.subjectId ?: 0}")
                        },
                    )
                }
                composable("train") { TrainScreen(viewModel, onSimulations = { navController.navigate("simulations") }, onStart = { config -> navController.navigate("quiz/${config.count}/${config.topicId ?: 0}/${config.subjectId ?: 0}/${config.mode}/${Uri.encode(config.board ?: "_")}/${config.difficulty ?: "_"}") }, onHelp = { viewModel.startTour(TourId.TRAIN) }) }
                composable("simulations") {
                    br.com.estudario.ui.simulation.SimulationsScreen(
                        viewModel,
                        onOpenExam = { navController.navigate("simulation/$it/exam") },
                        onOpenResult = { navController.navigate("simulation/$it/result") },
                    )
                }
                composable("simulation/{id}/exam") { backStack ->
                    val id = backStack.arguments?.getString("id")?.toLongOrNull() ?: 0L
                    br.com.estudario.ui.simulation.SimulationExamScreen(
                        id,
                        onExit = { navController.popBackStack() },
                        onFinished = { finished -> navController.navigate("simulation/$finished/result") { popUpTo("simulations") } },
                    )
                }
                composable("simulation/{id}/result") { backStack ->
                    val id = backStack.arguments?.getString("id")?.toLongOrNull() ?: 0L
                    br.com.estudario.ui.simulation.SimulationResultScreen(
                        viewModel,
                        id,
                        onBack = { navController.popBackStack() },
                        onTrainSubject = { subjectId -> navController.navigate("quiz/15/0/$subjectId/random/_/_") },
                        onOpenSimulations = { navController.navigate("simulations") { popUpTo("simulations") { inclusive = true } } },
                    )
                }
                composable("errors") {
                    ViradaScreen(
                        viewModel,
                        onMission = { topic -> navController.navigate("errors/mission?topic=${topic ?: -1}") },
                        onOpenTopic = { navController.navigate("topic/$it") },
                        onTrainErrors = { navController.navigate("quiz/20/0/0/errors/_/_") },
                        onTrainTopic = { navController.navigate("quiz/3/$it/0/random/_/_") },
                    )
                }
                composable("errors/mission?topic={topic}", arguments = listOf(androidx.navigation.navArgument("topic") { type = androidx.navigation.NavType.LongType; defaultValue = -1L })) { back ->
                    ViradaMissionScreen(viewModel, topicId = back.arguments?.getLong("topic")?.takeIf { it >= 0 }, onBack = { navController.popBackStack() })
                }
                composable("more") { MoreScreen(viewModel, onOpenSetup = viewModel::reopenInitialSetup, onNotifications = { navController.navigate("notifications") }, onAgenda = { navController.navigate("agenda") }) }
                composable("agenda") {
                    val agendaPlan by planViewModel.state.collectAsState()
                    br.com.estudario.ui.planner.AgendaSyncScreen(agendaPlan.activePlan?.name, loadTasks = { planViewModel.state.value.tasks.map { it.entity } })
                }
                composable("topic/{id}") { backStack ->
                    val id = backStack.arguments?.getString("id")?.toLongOrNull() ?: 0
                    TopicDetailScreen(viewModel, id, planViewModel = planViewModel, onBack = { navController.popBackStack() }, onQuiz = { navController.navigate("quiz/15/$id/0/random/_/_") }, onTheory = { navController.navigate("theory/$it") }, onFocus = {}, onOpenTopic = { navController.navigate("topic/$it") }, onTheoryAt = { theoryId, block -> navController.navigate("theory/$theoryId?block=$block") }, onQuizCount = { n -> navController.navigate("quiz/$n/$id/0/random/_/_") })
                }
                composable("topic/{id}/task/{taskId}") { backStack ->
                    val id = backStack.arguments?.getString("id")?.toLongOrNull() ?: 0
                    val taskId = backStack.arguments?.getString("taskId")
                    // A rota mantém a origem da tarefa para sincronizar a conclusão do tópico com o plano.
                    TopicDetailScreen(viewModel, id, taskId = taskId, planViewModel = planViewModel, onBack = { navController.popBackStack() }, onQuiz = { navController.navigate("quiz/15/$id/0/random/_/_") }, onTheory = { navController.navigate("theory/$it") }, onFocus = {}, onOpenTopic = { navController.navigate("topic/$it") }, onTheoryAt = { theoryId, block -> navController.navigate("theory/$theoryId?block=$block") }, onQuizCount = { n -> navController.navigate("quiz/$n/$id/0/random/_/_") })
                }
                composable("theory/{id}?block={block}", arguments = listOf(androidx.navigation.navArgument("block") { type = androidx.navigation.NavType.IntType; defaultValue = -1 })) { backStack ->
                    TheoryReaderScreen(
                        viewModel,
                        backStack.arguments?.getString("id")?.toLongOrNull() ?: 0,
                        showInternalTopBar = false,
                        initialBlock = backStack.arguments?.getInt("block")?.takeIf { it >= 0 },
                    ) { navController.popBackStack() }
                }
                // O Caderno que se escreve sozinho: a entrada (fichário), a Folheada do dia e a página
                // de cada tópico estudado. O que a pessoa guardou continua em "notebook/saved".
                composable("notebook") {
                    androidx.compose.foundation.layout.Column(Modifier.fillMaxSize()) {
                        NotebookTabs(saved = false, onNotebook = {}, onSaved = { navController.navigate("notebook/saved") { launchSingleTop = true } }, viewModel = viewModel)
                        androidx.compose.foundation.layout.Box(Modifier.weight(1f)) {
                            NotebookHomeScreen(
                                viewModel,
                                onFlip = { navController.navigate("notebook/flip") },
                                onPage = { navController.navigate("notebook/page/$it") },
                                onSaved = { navController.navigate("notebook/saved") },
                                onEdital = { navController.navigate("syllabus") },
                            )
                        }
                    }
                }
                composable("notebook/flip") {
                    NotebookFlipScreen(viewModel, onClose = { navController.popBackStack() }, onPage = { navController.navigate("notebook/page/$it") })
                }
                composable("notebook/page/{id}") { backStack ->
                    NotebookPageScreen(
                        viewModel, backStack.arguments?.getString("id")?.toLongOrNull() ?: 0,
                        onBack = { navController.popBackStack() },
                        onTheory = { navController.navigate("topic/$it") },
                        onTrain = { navController.navigate("quiz/5/$it/0/random/_/_") },
                    )
                }
                composable("notebook/saved") {
                    androidx.compose.foundation.layout.Column(Modifier.fillMaxSize()) {
                        NotebookTabs(saved = true, onNotebook = { if (!navController.popBackStack("notebook", false)) navController.navigate("notebook") }, onSaved = {}, viewModel = viewModel)
                        androidx.compose.foundation.layout.Box(Modifier.weight(1f)) {
                            StudyNotebookScreen(
                                viewModel,
                                onOpenTheory = { theoryId, block -> navController.navigate("theory/$theoryId?block=$block") },
                                onOpenTopic = { navController.navigate("topic/$it") },
                                onTrainFavorites = { navController.navigate("quiz/20/0/0/favorites/_/_") },
                                onTrainTopicFavorites = { topicId -> navController.navigate("quiz/20/$topicId/0/favorites/_/_") },
                            )
                        }
                    }
                }
                composable("quiz/{count}/{topic}/{subject}/{mode}/{board}/{difficulty}") { backStack ->
                    val args = backStack.arguments
                    val topic = args?.getString("topic")?.toLongOrNull()?.takeIf { it != 0L }
                    val subject = args?.getString("subject")?.toLongOrNull()?.takeIf { it != 0L }
                    QuizScreen(viewModel, QuizConfig(args?.getString("count")?.toIntOrNull() ?: 10, topic, subject, args?.getString("mode") ?: "random", args?.getString("board")?.takeIf { it != "_" }, args?.getString("difficulty")?.takeIf { it != "_" }), onBack = { navController.popBackStack() }, onOpenTopic = { navController.navigate("topic/$it") })
                }
                composable("plan-quiz/{taskId}/{count}/{topic}/{subject}") { backStack ->
                    val args = backStack.arguments
                    val taskId = args?.getString("taskId").orEmpty()
                    val topic = args?.getString("topic")?.toLongOrNull()?.takeIf { it != 0L }
                    val subject = args?.getString("subject")?.toLongOrNull()?.takeIf { it != 0L }
                    QuizScreen(
                        viewModel = viewModel,
                        config = QuizConfig(
                            count = args?.getString("count")?.toIntOrNull() ?: 10,
                            topicId = topic,
                            subjectId = subject,
                            planTaskId = taskId,
                        ),
                        onBack = { navController.popBackStack() },
                        onOpenTopic = { navController.navigate("topic/$it") },
                        onPlanTaskComplete = planViewModel::completeFromQuestionQuiz,
                    )
                }
                composable("reviews") { ReviewsScreen(viewModel, { navController.popBackStack() }, { navController.navigate("review-session/$it") }, { navController.navigate("topic/$it") }, showInlineBack = false) }
                composable("review-session/{id}") { backStack ->
                    ReviewSessionScreen(
                        viewModel, backStack.arguments?.getString("id")?.toLongOrNull() ?: 0,
                        onQuizTopic = { topicId -> navController.popBackStack(); navController.navigate("quiz/5/$topicId/0/random/_/_") },
                        onOpenTopic = { topicId -> navController.popBackStack(); navController.navigate("topic/$topicId") },
                        onNextReview = { id -> navController.popBackStack(); navController.navigate("review-session/$id") },
                        // Revisar pela agenda também fecha a tarefa "Revisão" do plano de hoje (ou atrasada)
                        // daquele tópico; antes ela ficava pendente e o plano pedia a revisão de novo.
                        onReviewCompleted = { topicId, startedAt ->
                            focusScope.launch {
                                val task = planViewModel.matchingPendingTask(topicId, null, br.com.estudario.domain.planner.PlanTaskType.REVIEW)
                                    ?.takeIf { it.entity.scheduledEpochDay <= java.time.LocalDate.now().toEpochDay() } ?: return@launch
                                runCatching {
                                    planViewModel.completeFromTopic(task.entity.id, br.com.estudario.data.planner.CompleteTaskInput(startedAt = startedAt, completedAt = System.currentTimeMillis(), actualMinutes = ((System.currentTimeMillis() - startedAt) / 60_000L).toInt().coerceAtLeast(1)))
                                    planViewModel.replanAfterTopicCompletion(task.entity.planId)
                                }
                            }
                        },
                    ) { navController.popBackStack() }
                }
                composable("queue") { QueueScreen(viewModel, { navController.popBackStack() }, { navController.navigate("topic/$it") }, showInlineBack = false) }
                composable("statistics") { StatisticsScreen(viewModel, { navController.popBackStack() }, showInlineBack = false) }
                composable("question-bank") { QuestionBankScreen(viewModel, { navController.popBackStack() }, { config -> navController.navigate("quiz/${config.count}/${config.topicId ?: 0}/${config.subjectId ?: 0}/${config.mode}/${Uri.encode(config.board ?: "_")}/${config.difficulty ?: "_"}") }, showInlineBack = false) }
                composable("notifications") { NotificationSettingsScreen(viewModel, onBack = { navController.popBackStack() }, showInlineBack = false) }
                composable("sources") { SourcesScreen(viewModel, { navController.popBackStack() }, { navController.navigate("topic/$it") }, showInternalTopBar = false) }
                composable("focus-history") { FocusHistoryScreen(viewModel) }
                composable("search") { SearchScreen(viewModel, { navController.popBackStack() }, { navController.navigate("topic/$it") }, { navController.navigate("theory/$it") }, showInlineBack = false) }
                composable("profile") {
                    // A missão do dia do perfil é a mesma da tela inicial: as tarefas do plano de hoje.
                    val profilePlan by planViewModel.state.collectAsState()
                    val todayLoad = profilePlan.todayTasks.filter { it.entity.status.countsAsPlannedLoad() }
                    val mission = if (profilePlan.activePlan != null && todayLoad.isNotEmpty())
                        todayLoad.count { it.entity.status == br.com.estudario.domain.planner.PlanTaskStatus.CONCLUIDA } to todayLoad.size else null
                    ProfileScreen(
                        viewModel, onBack = { navController.popBackStack() }, onBadges = { navController.navigate("badges") }, onWardrobe = { navController.navigate("wardrobe") },
                        showInternalTopBar = false, mission = mission, onOpenPlan = { navegarAba("plan") },
                    )
                }
                composable("wardrobe") { br.com.estudario.ui.profile.WardrobeScreen(viewModel, { navController.popBackStack() }, showInternalTopBar = false) }
                composable("badges") { BadgesScreen(viewModel, { navController.popBackStack() }, showInternalTopBar = false) }
            }
        }
        }
        if (showFocusOverlay) {
            FocusOverlay(
                viewModel = viewModel,
                onDismiss = { showFocusOverlay = false },
                onOpenPlan = { showFocusOverlay = false; navegarAba("plan") },
            )
        }
        if (focusSheetOpen && focusSession.active) FocusSessionSheet(
            session = focusSession,
            now = focusClockNow,
            hidden = focusHidden,
            doNotDisturb = focusDnd,
            keepScreenOn = focusKeepOn,
            onDoNotDisturb = viewModel::setFocusDoNotDisturb,
            onKeepScreenOn = viewModel::setFocusKeepScreenOn,
            onTogglePause = viewModel::toggleFocusPause,
            onToggleHidden = viewModel::toggleFocusClockHidden,
            onStop = { focusSheetOpen = false; focusScope.launch { viewModel.stopFocus() } },
            onOpenTopic = focusSession.topicId?.let { id -> { focusSheetOpen = false; navController.navigate("topic/$id") { launchSingleTop = true } } },
            onHistory = { focusSheetOpen = false; navController.navigate("focus-history") },
            onDismiss = { focusSheetOpen = false },
        )
        TransferDialog(viewModel)
        // Comemoração da sequência: só na primeira atividade que fecha a meta do dia, e nunca por
        // cima de um guia em andamento.
        // No meio de uma rodada de estudo (questões, revisão, simulado) ou com o foco contando, as
        // comemorações esperam: aparecem quando a rodada acaba, sem interromper o estudo.
        val studying = StudyFlow.busy || (focusSession.active && !focusSession.paused)
        val celebration by viewModel.celebration.collectAsState()
        val reviewContext = androidx.compose.ui.platform.LocalContext.current
        celebration?.let { current ->
            if (tourStep == null && !studying) StreakCelebrationScreen(current) {
                viewModel.consumeCelebration()
                // Fechou a comemoração de uma boa sequência: o momento certo para a avaliação na Play.
                AppReview.maybeAsk(reviewContext, current.summary.current)
            }
        }
        // Emblema novo entra depois da comemoração do dia, para as duas não brigarem pela tela.
        val badgeUnlock by viewModel.badgeUnlock.collectAsState()
        if (celebration == null && tourStep == null && !studying) BadgeCelebrationScreen(badgeUnlock, viewModel::consumeBadgeUnlock)
        // Subida de nível: depois da sequência e dos emblemas, uma tela de cada vez.
        val levelUp by viewModel.levelUp.collectAsState()
        levelUp?.let { event -> if (celebration == null && badgeUnlock.isEmpty() && tourStep == null && !studying) br.com.estudario.ui.profile.LevelUpScreen(event, viewModel::consumeLevelUp) }
        // Peça nova no armário: o baú abre depois das outras comemorações.
        br.com.estudario.ui.profile.FolhaGiftHost(viewModel, enabled = celebration == null && badgeUnlock.isEmpty() && levelUp == null && tourStep == null && !studying)
        val xpGain by viewModel.xpGain.collectAsState()
        br.com.estudario.ui.profile.XpGainToast(xpGain)
        if (showPlansFromMenu) br.com.estudario.ui.plans.PlansDialog(onDismiss = { showPlansFromMenu = false }, onSignIn = { showPlansFromMenu = false; navController.navigate("profile") })
        if (showTourPicker) TourPickerDialog(
            seen = seenTours.orEmpty(),
            onDismiss = { showTourPicker = false },
            onStart = { tour -> showTourPicker = false; viewModel.startTour(tour) },
            onWatchVideo = { video -> showTourPicker = false; tutorialToShow = video },
        )
        if (showHelpGuidePicker) HelpGuidePickerDialog(
            onDismiss = { showHelpGuidePicker = false },
            onStart = { guide -> showHelpGuidePicker = false; viewModel.startTour(guide.tour) },
        )
        TourOverlay(
            tour = activeTour,
            step = tourStep,
            stepIndex = tourStepIndex,
            stepCount = activeTour?.let { tourSteps(it).size } ?: 0,
            bounds = tourBounds,
            onPrevious = viewModel::previousTourStep,
            onNext = viewModel::advanceTour,
            onSkip = viewModel::stopTour,
            onWatchVideo = { tutorialToShow = it },
        )
        tutorialToShow?.let { video -> TutorialVideoDialog(video) { tutorialToShow = null } }
    }
}

@Composable
private fun FocusOverlay(
    viewModel: AppViewModel,
    onDismiss: () -> Unit,
    onOpenPlan: () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
            usePlatformDefaultWidth = false,
        ),
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .heightIn(max = 680.dp)
                .testTag("focus-overlay"),
            shape = RoundedCornerShape(28.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp,
            shadowElevation = 18.dp,
        ) {
            Column(Modifier.fillMaxWidth().heightIn(max = 680.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(start = 22.dp, end = 12.dp, top = 10.dp, bottom = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text("Modo foco", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                        Text(
                            "Estude sem sair da tela atual.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                    IconButton(onClick = onDismiss, modifier = Modifier.semantics { contentDescription = "Fechar janela" }) {
                        Icon(Icons.Outlined.Close, contentDescription = null)
                    }
                }
                HorizontalDivider()
                FocusScreen(
                    viewModel = viewModel,
                    onBack = onDismiss,
                    onOpenPlan = onOpenPlan,
                    onHome = onDismiss,
                    embedded = true,
                )
            }
        }
    }
}

@Composable
fun Modifier.withSecondaryRouteBottomInset(
    showBottomNavigation: Boolean,
    bottomInsets: WindowInsets = WindowInsets.navigationBars,
): Modifier = if (showBottomNavigation) this else windowInsetsPadding(bottomInsets)

@Composable
private fun HelpGuidePickerDialog(onDismiss: () -> Unit, onStart: (HelpGuide) -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Escolha um guia") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("Selecione o que você quer aprender agora.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                helpGuideOptions().forEach { guide ->
                    ListItem(
                        headlineContent = { Text(guide.title) },
                        supportingContent = { Text(guide.subtitle) },
                        leadingContent = { Icon(Icons.Outlined.HelpOutline, null) },
                        modifier = Modifier.clickable { onStart(guide) },
                    )
                }
            }
        },
        confirmButton = { TextButton(onClick = onDismiss) { Text("Fechar") } },
    )
}

@Composable
private fun TourPickerDialog(seen: Set<String>, onDismiss: () -> Unit, onStart: (TourId) -> Unit, onWatchVideo: (TutorialVideo) -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Como usar o app") },
        text = {
            Column(Modifier.heightIn(max = 420.dp).verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("Escolha um guia para ver passo a passo.", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                // Guias de edital e de material saíram: o fluxo novo de criar concurso e gerar no
                // tópico já se explica sozinho, e os vídeos mostravam telas antigas.
                TourId.entries.filterNot { it == TourId.EDITAL || it == TourId.CONTENT || it == TourId.WELCOME_START }.forEach { tour ->
                    ListItem(
                        headlineContent = { Text(tour.title) },
                        supportingContent = { Text(tour.subtitle) },
                        trailingContent = { if (tour.name in seen) Icon(Icons.Outlined.CheckCircle, "Já visto", tint = MaterialTheme.colorScheme.secondary) },
                        modifier = Modifier.clickable { onStart(tour) },
                    )
                }
            }
        },
        confirmButton = { TextButton(onClick = onDismiss) { Text("Fechar") } },
    )
}

@Composable
private fun TransferDialog(viewModel: AppViewModel) {
    val state by viewModel.transfer.collectAsState()
    when (val current = state) {
        TransferState.Idle -> Unit
        TransferState.Loading -> LoadingDialog("Processando o arquivo", "Arquivos grandes podem levar alguns segundos.")
        is TransferState.Working -> br.com.estudario.ui.components.EstudarioProcessDialog(current.title, current.message)
        is TransferState.Done -> br.com.estudario.ui.components.TransferDoneDialog(current.title, current.message, current.facts, current.cloud, viewModel::clearTransfer)
        is TransferState.Preview -> ImportReviewScreen(
            state = current,
            onConfirm = { mode, studied ->
                if (mode != null) viewModel.confirmImport(current.raw, mode, targetCompetitionId = current.targetCompetitionId)
                else viewModel.confirmImport(current.raw, markAsStudied = studied, targetCompetitionId = current.targetCompetitionId)
            },
            onCancel = viewModel::clearTransfer,
        )
        is TransferState.Success -> AlertDialog(onDismissRequest = viewModel::clearTransfer, title = { Text("Concluído") }, text = { Text(current.message) }, confirmButton = { TextButton(onClick = viewModel::clearTransfer) { Text("OK") } })
        is TransferState.Error -> AlertDialog(onDismissRequest = viewModel::clearTransfer, title = { Text("Não foi possível concluir") }, text = { Text(current.message) }, confirmButton = { TextButton(onClick = viewModel::clearTransfer) { Text("Entendi") } })
    }
}
