package br.com.estudario.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import androidx.activity.compose.BackHandler
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
import br.com.estudario.ui.navigation.EstudarioDrawerContent
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

private data class Destination(val route: String, val label: String, val selected: androidx.compose.ui.graphics.vector.ImageVector, val unselected: androidx.compose.ui.graphics.vector.ImageVector)

@Composable
fun EstudarioApp(viewModel: AppViewModel) {
    val themeMode by viewModel.themeMode.collectAsState()
    val dark = when (themeMode) { "DARK" -> true; "LIGHT" -> false; else -> isSystemInDarkTheme() }
    EstudarioTheme(dark) {
        val onboardingConcluido by viewModel.hasCompletedOnboarding.collectAsState()
        val seenTours by viewModel.seenTours.collectAsState()
        val initialSetup by viewModel.initialSetup.collectAsState()
        val hasExistingWorkspace by viewModel.hasExistingWorkspace.collectAsState()
        val aiReviewTarget by viewModel.aiReviewTarget.collectAsState()
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
                onApplied = {
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
    var showHelpGuidePicker by remember { mutableStateOf(false) }
    var tutorialToShow by remember { mutableStateOf<TutorialVideo?>(null) }
    BackHandler(enabled = tourStep != null && tutorialToShow == null) { viewModel.stopTour() }
    val entry by navController.currentBackStackEntryAsState()
    val currentRoute = entry?.destination?.route
    val focusSession by viewModel.focusSession.collectAsState()
    var showFocusOverlay by rememberSaveable { mutableStateOf(false) }
    var focusClockNow by remember { mutableLongStateOf(System.currentTimeMillis()) }
    LaunchedEffect(focusSession.active, focusSession.startedAt) {
        while (focusSession.active) {
            focusClockNow = System.currentTimeMillis()
            delay(1_000)
        }
    }
    LaunchedEffect(seenTours != null) { if (seenTours?.contains(TourId.EDITAL.name) == false) viewModel.maybeStartTour(TourId.EDITAL) }
    // Nenhum guia abre por cima de uma importação em andamento: a pessoa está no meio de um
    // diálogo, e o guia aparecendo ali é o que fazia a tela parecer travada depois de importar.
    val appTransfer by viewModel.transfer.collectAsState()
    val planTransfer by planViewModel.transfer.collectAsState()
    val planBusy by planViewModel.busy.collectAsState()
    val importandoAlgo = appTransfer != TransferState.Idle || planTransfer != PlanTransferUiState.Idle || planBusy != null
    LaunchedEffect(currentRoute, activeTour, seenTours, importandoAlgo) {
        val tour = tourForRoute(currentRoute) ?: return@LaunchedEffect
        if (importandoAlgo) return@LaunchedEffect
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
        Destination("home", "Início", Icons.Rounded.Home, Icons.Outlined.Home),
        Destination("syllabus", "Edital", Icons.Rounded.Checklist, Icons.Outlined.Checklist),
        Destination("focus", "Foco", Icons.Rounded.Timer, Icons.Outlined.Timer),
        Destination("plan", "Plano", Icons.Rounded.CalendarMonth, Icons.Outlined.CalendarMonth),
        Destination("train", "Treinar", Icons.Rounded.School, Icons.Outlined.School),
    )
    val showBottom = currentRoute in destinations.map { it.route }
    val drawerGestureDisabled = currentRoute in setOf(
        "topic/{id}",
        "topic/{id}/task/{taskId}",
        "theory/{id}",
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
    fun abrirAbaDoMenu(route: String) {
        drawerScope.launch { drawerState.close() }
        navController.navigate(route) { popUpTo("home") { saveState = true }; launchSingleTop = true; restoreState = true }
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
    Box(Modifier.fillMaxSize()) {
        ModalNavigationDrawer(
            drawerState = drawerState,
            // O botão Estudário permanece visível em todas as rotas; só o gesto de borda cede aos
            // leitores, ao quiz, à revisão ativa e ao cronômetro de foco.
            gesturesEnabled = !drawerGestureDisabled,
            drawerContent = {
                EstudarioDrawerContent(
                    profile = profile,
                    level = progress?.level,
                    totalXp = progress?.totalXp,
                    currentRoute = currentRoute,
                    sections = estudarioDrawerSections(
                        onSyllabus = { abrirAbaDoMenu("syllabus") },
                        onPlan = { abrirAbaDoMenu("plan") },
                        onTrain = { abrirAbaDoMenu("train") },
                        onReviews = { abrirDoMenu("reviews") },
                        onErrors = { abrirDoMenu("errors") },
                        onFocus = { drawerScope.launch { drawerState.close() }; showFocusOverlay = true },
                        onFocusHistory = { abrirDoMenu("focus-history") },
                        onStatistics = { abrirDoMenu("statistics") },
                        onBadges = { abrirDoMenu("badges") },
                        onSources = { abrirDoMenu("sources") },
                        onSettings = { abrirDoMenu("more") },
                        onNotifications = { abrirDoMenu("notifications") },
                        onSyncCalendar = ::sincronizarAgenda,
                        onHelp = { drawerScope.launch { drawerState.close() }; showTourPicker = true },
                    ),
                    appVersion = "Estudário ${BuildConfig.VERSION_NAME}",
                    onOpenProfile = { abrirDoMenu("profile") },
                )
            },
        ) {
        Scaffold(
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            topBar = {
                if (currentRoute != null) EstudarioTopBar(
                    profile = profile,
                    onOpenMenu = { drawerScope.launch { drawerState.open() } },
                    onSearch = { navController.navigate("search") },
                    onProfile = { navController.navigate("profile") },
                    onBack = if (showBottom || currentRoute in setOf("focus", "review-session/{id}", "quiz/{count}/{topic}/{subject}/{mode}/{board}/{difficulty}", "plan-quiz/{taskId}/{count}/{topic}/{subject}")) null else ({
                        if (!navController.popBackStack()) navController.navigate("home")
                    }),
                    title = when {
                        currentRoute == "profile" -> "Perfil"
                        currentRoute == "badges" -> "Emblemas"
                        currentRoute == "sources" -> "Histórico e fontes"
                        currentRoute == "focus-history" -> "Histórico de foco"
                        currentRoute == "theory/{id}" -> "Leitura"
                        else -> null
                    },
                    profileModifier = if (showBottom) Modifier.tourTarget(TourKey.HOME_PROFILE, tourStep?.key) { viewModel.reportTourTargetBounds(TourKey.HOME_PROFILE, it) } else Modifier,
                    menuModifier = Modifier.tourTarget(TourKey.NAV_MENU, tourStep?.key) { viewModel.reportTourTargetBounds(TourKey.NAV_MENU, it) },
                )
            },
            bottomBar = {
                if (showBottom) NavigationBar(windowInsets = WindowInsets.navigationBars, modifier = Modifier.testTag("main-bottom-navigation")) {
                    destinations.forEach { destination ->
                        val selected = if (destination.route == "focus") showFocusOverlay else currentRoute == destination.route
                        val tourKey = tourKeyForRoute(destination.route)
                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                if (destination.route == "focus") showFocusOverlay = true
                                else navegarAba(destination.route)
                            },
                            icon = {
                                if (destination.route == "focus") {
                                    val elapsedMinutes = focusSession.elapsedMinutes(focusClockNow)
                                    FocusNavigationIcon(
                                        active = focusSession.active,
                                        elapsedLabel = if (elapsedMinutes < 1) "menos de 1 min" else "$elapsedMinutes min",
                                        onClick = { showFocusOverlay = true },
                                    )
                                } else {
                                    Icon(if (selected) destination.selected else destination.unselected, null)
                                }
                            },
                            label = { Text(destination.label, maxLines = 1, softWrap = false, overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis) },
                            modifier = if (tourKey != null) Modifier.tourTarget(tourKey, tourStep?.key) { viewModel.reportTourTargetBounds(tourKey, it) } else Modifier,
                        )
                    }
                }
            },
        ) { padding ->
            NavHost(
                navController,
                startDestination = "home",
                modifier = Modifier
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
                    )
                }
                composable("syllabus") { EditalScreen(viewModel, onTopic = { navController.navigate("topic/$it") }, onHelp = { showHelpGuidePicker = true }) }
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
                        onStartQuestions = { task, count ->
                            navController.navigate("plan-quiz/${Uri.encode(task.id)}/$count/${task.topicId ?: 0}/${task.subjectId ?: 0}")
                        },
                    )
                }
                composable("train") { TrainScreen(viewModel, onStart = { config -> navController.navigate("quiz/${config.count}/${config.topicId ?: 0}/${config.subjectId ?: 0}/${config.mode}/${Uri.encode(config.board ?: "_")}/${config.difficulty ?: "_"}") }, onHelp = { viewModel.startTour(TourId.TRAIN) }) }
                composable("errors") { ErrorsScreen(viewModel, onTrainErrors = { navController.navigate("quiz/20/0/0/errors/_/_") }, onOpenTopic = { navController.navigate("topic/$it") }) }
                composable("more") { MoreScreen(viewModel, onOpenSetup = viewModel::reopenInitialSetup) }
                composable("topic/{id}") { backStack ->
                    val id = backStack.arguments?.getString("id")?.toLongOrNull() ?: 0
                    TopicDetailScreen(viewModel, id, planViewModel = planViewModel, onBack = { navController.popBackStack() }, onQuiz = { navController.navigate("quiz/15/$id/0/random/_/_") }, onTheory = { navController.navigate("theory/$it") }, onFocus = { showFocusOverlay = true })
                }
                composable("topic/{id}/task/{taskId}") { backStack ->
                    val id = backStack.arguments?.getString("id")?.toLongOrNull() ?: 0
                    val taskId = backStack.arguments?.getString("taskId")
                    // A rota mantém a origem da tarefa para sincronizar a conclusão do tópico com o plano.
                    TopicDetailScreen(viewModel, id, taskId = taskId, planViewModel = planViewModel, onBack = { navController.popBackStack() }, onQuiz = { navController.navigate("quiz/15/$id/0/random/_/_") }, onTheory = { navController.navigate("theory/$it") }, onFocus = { showFocusOverlay = true })
                }
                composable("theory/{id}") { backStack -> TheoryReaderScreen(viewModel, backStack.arguments?.getString("id")?.toLongOrNull() ?: 0, showInternalTopBar = false) { navController.popBackStack() } }
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
                composable("review-session/{id}") { backStack -> ReviewSessionScreen(viewModel, backStack.arguments?.getString("id")?.toLongOrNull() ?: 0) { navController.popBackStack() } }
                composable("queue") { QueueScreen(viewModel, { navController.popBackStack() }, { navController.navigate("topic/$it") }, showInlineBack = false) }
                composable("statistics") { StatisticsScreen(viewModel, { navController.popBackStack() }, showInlineBack = false) }
                composable("question-bank") { QuestionBankScreen(viewModel, { navController.popBackStack() }, { config -> navController.navigate("quiz/${config.count}/${config.topicId ?: 0}/${config.subjectId ?: 0}/${config.mode}/${Uri.encode(config.board ?: "_")}/${config.difficulty ?: "_"}") }, showInlineBack = false) }
                composable("notifications") { NotificationSettingsScreen(viewModel, onBack = { navController.popBackStack() }, showInlineBack = false) }
                composable("sources") { SourcesScreen(viewModel, { navController.popBackStack() }, { navController.navigate("topic/$it") }, showInternalTopBar = false) }
                composable("focus-history") { FocusHistoryScreen(viewModel) }
                composable("search") { SearchScreen(viewModel, { navController.popBackStack() }, { navController.navigate("topic/$it") }, { navController.navigate("theory/$it") }, showInlineBack = false) }
                composable("profile") { ProfileScreen(viewModel, onBack = { navController.popBackStack() }, onBadges = { navController.navigate("badges") }, showInternalTopBar = false) }
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
        TransferDialog(viewModel)
        // Comemoração da sequência: só na primeira atividade que fecha a meta do dia, e nunca por
        // cima de um guia em andamento.
        val celebration by viewModel.celebration.collectAsState()
        celebration?.let { current -> if (tourStep == null) StreakCelebrationScreen(current, viewModel::consumeCelebration) }
        // Emblema novo entra depois da comemoração do dia, para as duas não brigarem pela tela.
        val badgeUnlock by viewModel.badgeUnlock.collectAsState()
        if (celebration == null && tourStep == null) BadgeCelebrationScreen(badgeUnlock, viewModel::consumeBadgeUnlock)
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
                TutorialVideo.entries.forEach { video ->
                    ListItem(
                        headlineContent = { Text("Vídeo: ${video.title}") },
                        supportingContent = { Text(video.description) },
                        leadingContent = { Icon(Icons.Outlined.PlayCircleOutline, null) },
                        modifier = Modifier.clickable { onWatchVideo(video) },
                    )
                }
                TourId.entries.forEach { tour ->
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
        TransferState.Loading -> LoadingDialog("Processando o arquivo", "Arquivos grandes da IA podem levar alguns segundos.")
        is TransferState.Preview -> AlertDialog(
            onDismissRequest = viewModel::clearTransfer,
            title = { Text("Confirmar importação") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(current.value.competition, style = MaterialTheme.typography.titleMedium)
                    Text("Formato v${current.value.version} • ${current.value.subjects.size} matéria(s) • ${current.value.topicCount} tópico(s) • ${current.value.subtopicCount} subtópico(s)")
                    current.value.subjects.take(6).forEach { subject ->
                        Text("• ${subject.name}: ${subject.topicCount} tópico(s), ${subject.subtopicCount} subtópico(s), ${subject.theoryCount} teoria(s), ${subject.questionCount} questão(ões)", style = MaterialTheme.typography.bodySmall)
                        subject.topics.filter { it.theoryCount + it.summaryCount + it.questionCount + it.snippetCount > 0 }.take(8).forEach { topic ->
                            Text("${"  ".repeat(topic.depth + 1)}✓ ${topic.title}: +${topic.theoryCount} teoria, +${topic.summaryCount} resumo, +${topic.questionCount} questões", style = MaterialTheme.typography.labelSmall)
                        }
                    }
                    if (current.value.subjects.size > 6) Text("… e mais ${current.value.subjects.size - 6} matéria(s)", style = MaterialTheme.typography.bodySmall)
                    HorizontalDivider()
                    Text("${current.value.theoryCount} teoria(s) • ${current.value.summaryCount} resumo(s) • ${current.value.questionCount} questão(ões)")
                    Text("${current.value.snippetCount} item(ns) de memorização • ${current.value.errorConceptCount} conceito(s) de erro")
                    if (current.value.sourceCount > 0) Text("${current.value.sourceCount} fonte(s) declarada(s), ficam salvas para você conferir depois.", color = MaterialTheme.colorScheme.secondary)
                    if (current.value.downgradedQuestions > 0) {
                        // A IA marcou como prova real sem apontar origem. O app não exibe procedência
                        // que ninguém consegue conferir, então trata como autoral e avisa.
                        Text(
                            "${current.value.downgradedQuestions} questão(ões) vieram marcadas como de prova real sem apontar a origem. Elas entram como autorais, sem banca nem ano.",
                            color = MaterialTheme.colorScheme.error,
                        )
                    }
                    if (current.value.packageAlreadyImported || current.value.duplicateCount > 0) {
                        Text("Este pacote já foi importado. Você pode atualizar o conteúdo preservando o histórico ou criar uma cópia independente.", color = MaterialTheme.colorScheme.primary)
                    }
                }
            },
            confirmButton = {
                if (current.value.packageAlreadyImported || current.value.duplicateCount > 0) Row {
                    TextButton(onClick = { viewModel.confirmImport(current.raw, ImportMode.UPDATE, targetCompetitionId = current.targetCompetitionId) }) { Text("Atualizar") }
                    TextButton(onClick = { viewModel.confirmImport(current.raw, ImportMode.COPY, targetCompetitionId = current.targetCompetitionId) }) { Text("Criar cópia") }
                } else Row {
                    TextButton(onClick = { viewModel.confirmImport(current.raw, targetCompetitionId = current.targetCompetitionId) }) { Text("Importar") }
                    TextButton(onClick = { viewModel.confirmImport(current.raw, markAsStudied = true, targetCompetitionId = current.targetCompetitionId) }) { Text("Importar e marcar estudado") }
                }
            },
            dismissButton = { TextButton(onClick = viewModel::clearTransfer) { Text("Cancelar") } },
        )
        is TransferState.Success -> AlertDialog(onDismissRequest = viewModel::clearTransfer, title = { Text("Concluído") }, text = { Text(current.message) }, confirmButton = { TextButton(onClick = viewModel::clearTransfer) { Text("OK") } })
        is TransferState.Error -> AlertDialog(onDismissRequest = viewModel::clearTransfer, title = { Text("Não foi possível concluir") }, text = { Text(current.message) }, confirmButton = { TextButton(onClick = viewModel::clearTransfer) { Text("Entendi") } })
    }
}
