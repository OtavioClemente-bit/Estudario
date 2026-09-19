package br.com.estudario.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.*
import android.net.Uri
import br.com.estudario.ui.screens.*
import br.com.estudario.ui.focus.FocusScreen
import br.com.estudario.ui.theme.EstudarioTheme
import br.com.estudario.data.transfer.ImportMode
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.estudario.EstudarioApplication
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
import br.com.estudario.ui.tour.TourOverlay
import br.com.estudario.ui.tour.TutorialVideo
import br.com.estudario.ui.tour.TutorialVideoDialog
import br.com.estudario.ui.tour.tourForRoute
import br.com.estudario.ui.tour.tourKeyForRoute
import br.com.estudario.ui.tour.tourSteps
import br.com.estudario.ui.tour.tourTarget
import kotlinx.coroutines.delay

private data class Destination(val route: String, val label: String, val selected: androidx.compose.ui.graphics.vector.ImageVector, val unselected: androidx.compose.ui.graphics.vector.ImageVector)

@Composable
fun EstudarioApp(viewModel: AppViewModel) {
    val themeMode by viewModel.themeMode.collectAsState()
    val dark = when (themeMode) { "DARK" -> true; "LIGHT" -> false; else -> isSystemInDarkTheme() }
    EstudarioTheme(dark) {
        // null: a splash do sistema ainda cobre a tela enquanto a preferência carrega.
        // O tour guiado (primeira abertura ou replay via Mais > Como usar o app) roda dentro
        // da própria navegação principal, destacando os botões reais - ver MainNavigation.
        if (viewModel.hasCompletedOnboarding.collectAsState().value != null && viewModel.seenTours.collectAsState().value != null) {
            MainNavigation(viewModel)
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
    // o mesmo .plano não navega duas vezes — era assim que a aba Plano voltava sozinha.
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
    // Guias: o de primeiros passos abre sozinho na primeira abertura; os de Plano, Treinar e Mais
    // abrem na primeira visita a cada aba; o de conteúdo abre depois do primeiro edital importado.
    // Todos podem ser repetidos em Mais › Como usar o app.
    val seenTours by viewModel.seenTours.collectAsState()
    val activeTour by viewModel.activeTour.collectAsState()
    val tourStep by viewModel.tourStep.collectAsState()
    val tourStepIndex by viewModel.tourStepIndex.collectAsState()
    val tourBounds by viewModel.tourTargetBounds.collectAsState()
    var showTourPicker by remember { mutableStateOf(false) }
    var tutorialToShow by remember { mutableStateOf<TutorialVideo?>(null) }
    BackHandler(enabled = tourStep != null && tutorialToShow == null) { viewModel.stopTour() }
    val entry by navController.currentBackStackEntryAsState()
    val currentRoute = entry?.destination?.route
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
    val destinations = listOf(
        Destination("home", "Início", Icons.Rounded.Home, Icons.Outlined.Home),
        Destination("syllabus", "Edital", Icons.Rounded.Checklist, Icons.Outlined.Checklist),
        Destination("plan", "Plano", Icons.Rounded.CalendarMonth, Icons.Outlined.CalendarMonth),
        Destination("train", "Treinar", Icons.Rounded.School, Icons.Outlined.School),
        Destination("more", "Mais", Icons.Rounded.MoreHoriz, Icons.Outlined.MoreHoriz),
    )
    val showBottom = currentRoute in destinations.map { it.route }
    Box(Modifier.fillMaxSize()) {
        Scaffold(
            bottomBar = {
                if (showBottom) NavigationBar {
                    destinations.forEach { destination ->
                        val selected = currentRoute == destination.route
                        val tourKey = tourKeyForRoute(destination.route)
                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                navController.navigate(destination.route) { popUpTo("home") { saveState = true }; launchSingleTop = true; restoreState = true }
                            },
                            icon = { Icon(if (selected) destination.selected else destination.unselected, null) },
                            label = { Text(destination.label) },
                            modifier = if (tourKey != null) Modifier.tourTarget(tourKey, tourStep?.key) { viewModel.reportTourTargetBounds(tourKey, it) } else Modifier,
                        )
                    }
                }
            },
        ) { padding ->
            NavHost(navController, startDestination = "home", modifier = Modifier.padding(padding)) {
                composable("home") {
                    HomeScreen(
                        viewModel = viewModel,
                        planViewModel = planViewModel,
                        onQuiz = { count, mode -> navController.navigate("quiz/$count/0/0/$mode/_/_") },
                        onTopic = { navController.navigate("topic/$it") },
                        onReviews = { navController.navigate("reviews") },
                        onProfile = { navController.navigate("profile") },
                        onSearch = { navController.navigate("search") },
                        onPlan = { navController.navigate("plan") { popUpTo("home") { saveState = true }; launchSingleTop = true; restoreState = true } },
                        onBadges = { navController.navigate("badges") },
                        onErrors = { navController.navigate("errors") },
                        onHelp = { viewModel.startTour(TourId.PROFILE) },
                    )
                }
                composable("syllabus") { EditalScreen(viewModel, onTopic = { navController.navigate("topic/$it") }, onHelp = { tutorialToShow = TutorialVideo.EDITAL }) }
                composable("plan") { PlanScreen(planViewModel, viewModel, onOpenTopic = { navController.navigate("topic/$it") }, onOpenErrors = { navController.navigate("errors") }, onFocus = { navController.navigate("focus") }, onHelp = { viewModel.startTour(TourId.PLAN) }) }
                composable("train") { TrainScreen(viewModel, onStart = { config -> navController.navigate("quiz/${config.count}/${config.topicId ?: 0}/${config.subjectId ?: 0}/${config.mode}/${Uri.encode(config.board ?: "_")}/${config.difficulty ?: "_"}") }, onHelp = { viewModel.startTour(TourId.TRAIN) }) }
                composable("errors") { ErrorsScreen(viewModel, onTrainErrors = { navController.navigate("quiz/20/0/0/errors/_/_") }, onOpenTopic = { navController.navigate("topic/$it") }) }
                composable("more") { MoreScreen(viewModel, onReviews = { navController.navigate("reviews") }, onQueue = { navController.navigate("queue") }, onStatistics = { navController.navigate("statistics") }, onQuestionBank = { navController.navigate("question-bank") }, onNotifications = { navController.navigate("notifications") }, onErrors = { navController.navigate("errors") }, onPlan = { navController.navigate("plan") }, onGuide = { showTourPicker = true }, onProfile = { navController.navigate("profile") }, onSources = { navController.navigate("sources") }, onFocus = { navController.navigate("focus") }, onHelp = { viewModel.startTour(TourId.MORE) }) }
                composable("topic/{id}") { backStack ->
                    val id = backStack.arguments?.getString("id")?.toLongOrNull() ?: 0
                    TopicDetailScreen(viewModel, id, onBack = { navController.popBackStack() }, onQuiz = { navController.navigate("quiz/15/$id/0/random/_/_") }, onTheory = { navController.navigate("theory/$it") }, onFocus = { navController.navigate("focus") })
                }
                composable("theory/{id}") { backStack -> TheoryReaderScreen(viewModel, backStack.arguments?.getString("id")?.toLongOrNull() ?: 0) { navController.popBackStack() } }
                composable("quiz/{count}/{topic}/{subject}/{mode}/{board}/{difficulty}") { backStack ->
                    val args = backStack.arguments
                    val topic = args?.getString("topic")?.toLongOrNull()?.takeIf { it != 0L }
                    val subject = args?.getString("subject")?.toLongOrNull()?.takeIf { it != 0L }
                    QuizScreen(viewModel, QuizConfig(args?.getString("count")?.toIntOrNull() ?: 10, topic, subject, args?.getString("mode") ?: "random", args?.getString("board")?.takeIf { it != "_" }, args?.getString("difficulty")?.takeIf { it != "_" }), onBack = { navController.popBackStack() }, onOpenTopic = { navController.navigate("topic/$it") })
                }
                composable("reviews") { ReviewsScreen(viewModel, { navController.popBackStack() }, { navController.navigate("review-session/$it") }) { navController.navigate("topic/$it") } }
                composable("review-session/{id}") { backStack -> ReviewSessionScreen(viewModel, backStack.arguments?.getString("id")?.toLongOrNull() ?: 0) { navController.popBackStack() } }
                composable("queue") { QueueScreen(viewModel, { navController.popBackStack() }) { navController.navigate("topic/$it") } }
                composable("statistics") { StatisticsScreen(viewModel) { navController.popBackStack() } }
                composable("question-bank") { QuestionBankScreen(viewModel, { navController.popBackStack() }) { config -> navController.navigate("quiz/${config.count}/${config.topicId ?: 0}/${config.subjectId ?: 0}/${config.mode}/${Uri.encode(config.board ?: "_")}/${config.difficulty ?: "_"}") } }
                composable("notifications") { NotificationSettingsScreen(viewModel) { navController.popBackStack() } }
                composable("focus") {
                    FocusScreen(
                        viewModel,
                        onBack = { if (!navController.popBackStack()) navController.navigate("home") },
                        // Sessão presa a uma tarefa: volta para o plano, que abre a conclusão com o tempo medido.
                        onFinishedTask = { _, _ -> navController.navigate("plan") { popUpTo("focus") { inclusive = true } } },
                    )
                }
                composable("sources") { SourcesScreen(viewModel, { navController.popBackStack() }) { navController.navigate("topic/$it") } }
                composable("search") { SearchScreen(viewModel, { navController.popBackStack() }, { navController.navigate("topic/$it") }, { navController.navigate("theory/$it") }) }
                composable("profile") { ProfileScreen(viewModel, onBack = { navController.popBackStack() }, onBadges = { navController.navigate("badges") }) }
                composable("badges") { BadgesScreen(viewModel) { navController.popBackStack() } }
            }
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
                    if (current.value.sourceCount > 0) Text("${current.value.sourceCount} fonte(s) declarada(s) — ficam salvas para você conferir depois.", color = MaterialTheme.colorScheme.secondary)
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
                    TextButton(onClick = { viewModel.confirmImport(current.raw, ImportMode.UPDATE) }) { Text("Atualizar") }
                    TextButton(onClick = { viewModel.confirmImport(current.raw, ImportMode.COPY) }) { Text("Criar cópia") }
                } else Row {
                    TextButton(onClick = { viewModel.confirmImport(current.raw) }) { Text("Importar") }
                    TextButton(onClick = { viewModel.confirmImport(current.raw, markAsStudied = true) }) { Text("Importar e marcar estudado") }
                }
            },
            dismissButton = { TextButton(onClick = viewModel::clearTransfer) { Text("Cancelar") } },
        )
        is TransferState.Success -> AlertDialog(onDismissRequest = viewModel::clearTransfer, title = { Text("Concluído") }, text = { Text(current.message) }, confirmButton = { TextButton(onClick = viewModel::clearTransfer) { Text("OK") } })
        is TransferState.Error -> AlertDialog(onDismissRequest = viewModel::clearTransfer, title = { Text("Não foi possível concluir") }, text = { Text(current.message) }, confirmButton = { TextButton(onClick = viewModel::clearTransfer) { Text("Entendi") } })
    }
}
