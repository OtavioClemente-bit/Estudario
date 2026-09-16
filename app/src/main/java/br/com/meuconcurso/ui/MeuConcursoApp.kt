package br.com.meuconcurso.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.*
import android.net.Uri
import br.com.meuconcurso.ui.screens.*
import br.com.meuconcurso.ui.theme.MeuConcursoTheme
import br.com.meuconcurso.data.transfer.ImportMode
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.meuconcurso.MeuConcursoApplication
import br.com.meuconcurso.ui.planner.PlanScreen
import br.com.meuconcurso.ui.planner.StudyPlanViewModel
import br.com.meuconcurso.ui.planner.StudyPlanViewModelFactory

private data class Destination(val route: String, val label: String, val selected: androidx.compose.ui.graphics.vector.ImageVector, val unselected: androidx.compose.ui.graphics.vector.ImageVector)

@Composable
fun MeuConcursoApp(viewModel: AppViewModel) {
    val themeMode by viewModel.themeMode.collectAsState()
    val dark = when (themeMode) { "DARK" -> true; "LIGHT" -> false; else -> isSystemInDarkTheme() }
    MeuConcursoTheme(dark) {
        val navController = rememberNavController()
        val application = LocalContext.current.applicationContext as MeuConcursoApplication
        val planViewModel: StudyPlanViewModel = viewModel(factory = StudyPlanViewModelFactory(application))
        val incomingPlan by application.incomingFiles.pendingPlan.collectAsState()
        LaunchedEffect(incomingPlan?.token) {
            incomingPlan?.let { incoming ->
                navController.navigate("plan") { launchSingleTop = true }
                planViewModel.inspectPlan(incoming.raw)
                application.incomingFiles.consumePlan(incoming.token)
            }
        }
        val notificationDestination by viewModel.notificationDestination.collectAsState()
        LaunchedEffect(notificationDestination) {
            notificationDestination?.let { destination -> navController.navigate(destination) { launchSingleTop = true }; viewModel.consumeNotificationDestination() }
        }
        val entry by navController.currentBackStackEntryAsState()
        val currentRoute = entry?.destination?.route
        val destinations = listOf(
            Destination("home", "Início", Icons.Rounded.Home, Icons.Outlined.Home),
            Destination("syllabus", "Edital", Icons.Rounded.Checklist, Icons.Outlined.Checklist),
            Destination("plan", "Plano", Icons.Rounded.CalendarMonth, Icons.Outlined.CalendarMonth),
            Destination("train", "Treinar", Icons.Rounded.School, Icons.Outlined.School),
            Destination("more", "Mais", Icons.Rounded.MoreHoriz, Icons.Outlined.MoreHoriz),
        )
        val showBottom = currentRoute in destinations.map { it.route }
        Scaffold(
            bottomBar = {
                if (showBottom) NavigationBar {
                    destinations.forEach { destination ->
                        val selected = currentRoute == destination.route
                        NavigationBarItem(
                            selected = selected,
                            onClick = { navController.navigate(destination.route) { popUpTo("home") { saveState = true }; launchSingleTop = true; restoreState = true } },
                            icon = { Icon(if (selected) destination.selected else destination.unselected, null) },
                            label = { Text(destination.label) },
                        )
                    }
                }
            },
        ) { padding ->
            NavHost(navController, startDestination = "home", modifier = Modifier.padding(padding)) {
                composable("home") { HomeScreen(viewModel, onQuiz = { count, mode -> navController.navigate("quiz/$count/0/0/$mode/_/_") }, onTopic = { navController.navigate("topic/$it") }, onReviews = { navController.navigate("reviews") }) }
                composable("syllabus") { EditalScreen(viewModel) { navController.navigate("topic/$it") } }
                composable("plan") { PlanScreen(planViewModel, onOpenTopic = { navController.navigate("topic/$it") }, onOpenErrors = { navController.navigate("errors") }) }
                composable("train") { TrainScreen(viewModel) { config -> navController.navigate("quiz/${config.count}/${config.topicId ?: 0}/${config.subjectId ?: 0}/${config.mode}/${Uri.encode(config.board ?: "_")}/${config.difficulty ?: "_"}") } }
                composable("errors") { ErrorsScreen(viewModel, onTrainErrors = { navController.navigate("quiz/20/0/0/errors/_/_") }, onOpenTopic = { navController.navigate("topic/$it") }) }
                composable("more") { MoreScreen(viewModel, onSearch = { navController.navigate("search") }, onReviews = { navController.navigate("reviews") }, onQueue = { navController.navigate("queue") }, onStatistics = { navController.navigate("statistics") }, onQuestionBank = { navController.navigate("question-bank") }, onImportGuide = { navController.navigate("import-guide") }, onNotifications = { navController.navigate("notifications") }, onErrors = { navController.navigate("errors") }, onPlan = { navController.navigate("plan") }) }
                composable("topic/{id}") { backStack ->
                    val id = backStack.arguments?.getString("id")?.toLongOrNull() ?: 0
                    TopicDetailScreen(viewModel, id, onBack = { navController.popBackStack() }, onQuiz = { navController.navigate("quiz/15/$id/0/random/_/_") }, onTheory = { navController.navigate("theory/$it") })
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
                composable("search") { SearchScreen(viewModel, { navController.popBackStack() }, { navController.navigate("topic/$it") }, { navController.navigate("theory/$it") }) }
                composable("import-guide") { ImportGuideScreen(viewModel) { navController.popBackStack() } }
            }
        }
        TransferDialog(viewModel)
    }
}

@Composable
private fun TransferDialog(viewModel: AppViewModel) {
    val state by viewModel.transfer.collectAsState()
    when (val current = state) {
        TransferState.Idle -> Unit
        TransferState.Loading -> AlertDialog(onDismissRequest = {}, title = { Text("Processando arquivo") }, text = { LinearProgressIndicator() }, confirmButton = {})
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
