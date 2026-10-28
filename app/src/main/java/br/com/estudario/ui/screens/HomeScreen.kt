package br.com.estudario.ui.screens

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import br.com.estudario.ui.planner.taskTitlePtBr
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.IconButton
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import br.com.estudario.ui.brand.Button
import br.com.estudario.ui.brand.ElevatedCard
import br.com.estudario.ui.brand.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import br.com.estudario.data.local.*
import br.com.estudario.domain.*
import br.com.estudario.domain.planner.PlanTaskStatus
import br.com.estudario.domain.planner.StudyPaceEvaluator
import br.com.estudario.ui.AppViewModel
import br.com.estudario.ui.planner.ActivePlanUiState
import br.com.estudario.ui.planner.PlannerTaskUi
import br.com.estudario.ui.planner.StudyPlanViewModel
import br.com.estudario.ui.planner.completionActionPtBr
import br.com.estudario.ui.planner.displayNamePtBr
import br.com.estudario.ui.planner.minutesLabelPtBr
import br.com.estudario.ui.screens.home.ActiveContestUi
import br.com.estudario.ui.screens.home.CurrentStudySection
import br.com.estudario.ui.screens.home.CurrentStudyUiState
import br.com.estudario.ui.screens.home.HomeAttention
import br.com.estudario.ui.screens.home.HomeDivider
import br.com.estudario.ui.screens.home.HomeHero
import br.com.estudario.ui.screens.home.DailyMissionCard
import br.com.estudario.ui.screens.home.DailyMissionUi
import br.com.estudario.ui.screens.home.MissionItemUi
import br.com.estudario.ui.planner.countsAsPlannedLoad
import br.com.estudario.ui.screens.home.HomeMetrics
import br.com.estudario.ui.screens.home.HomeNoContestState
import br.com.estudario.ui.screens.home.calculateHomeMetrics
import br.com.estudario.ui.screens.home.HomeSectionLink
import br.com.estudario.ui.screens.home.firstEligibleQueueTopic
import br.com.estudario.ui.screens.home.queueTopicUi
import br.com.estudario.ui.screens.home.currentHomeTask
import br.com.estudario.ui.screens.home.JourneySnapshot
import br.com.estudario.ui.screens.home.LevelRow
import br.com.estudario.ui.screens.home.NextUpStrip
import br.com.estudario.ui.screens.home.NextUpUi
import br.com.estudario.ui.screens.home.PaceForecast
import br.com.estudario.ui.screens.home.PaceUi
import br.com.estudario.ui.screens.home.PerformanceAndStanding
import br.com.estudario.ui.screens.home.PerformanceUi
import br.com.estudario.ui.screens.home.StandingUi
import br.com.estudario.ui.screens.home.StudyTaskUi
import br.com.estudario.ui.screens.home.SubjectCoverageUi
import br.com.estudario.ui.screens.home.SyllabusCoverage
import br.com.estudario.ui.screens.home.SyllabusCoverageUi
import br.com.estudario.ui.theme.EstudarioSpacing
import br.com.estudario.ui.tour.TourKey
import br.com.estudario.ui.tour.tourTarget
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toKotlinLocalDate

/**
 * O painel de evolução do concurso, não o planejamento, e não a agenda.
 *
 * A Home responde, em segundos: qual concurso estou estudando, o que faço agora, há quanto tempo
 * mantenho constância, quanto do edital já cobri, como está meu desempenho e quando devo terminar
 * o conteúdo. A resposta de "como vou chegar lá" é a aba Plano; a de "o que estou fazendo agora" é
 * a tela de estudo. Misturar os três é o que transformava esta tela numa cópia do cronograma.
 *
 * Composição, de cima para baixo:
 *
 * 1. [HomeHeader], saudação discreta e o concurso ativo.
 * 2. [JourneySnapshot], onde a pessoa está no edital e na própria evolução.
 * 3. [CurrentStudySection], AGORA: a única coisa com peso máximo na tela.
 * 4. [NextUpStrip], a continuidade, sem esconder atividades.
 * 5. [SyllabusCoverage], o edital completo, matéria por matéria.
 * 6. [PaceForecast], quando o edital fecha, e o que isso significa perto da prova.
 * 7. [PerformanceAndStanding], acerto recente e constância, dois números escolhidos.
 * 8. [LevelRow], nível e XP, discretos, fechando a evolução.
 * 9. [HomeAttention], revisões, erros e ponto frágil, no rodapé.
 */
@Composable
fun HomeScreen(
    viewModel: AppViewModel,
    planViewModel: StudyPlanViewModel,
    onQuiz: (Int, String) -> Unit,
    onTopic: (Long) -> Unit,
    onStudyTask: (Long, String) -> Unit = { id, _ -> onTopic(id) },
    onReviews: () -> Unit,
    onProfile: () -> Unit = {},
    onSyllabus: () -> Unit = {},
    onPlan: () -> Unit = {},
    onOpenQueue: () -> Unit = {},
    onFocus: () -> Unit = {},
    onStatistics: () -> Unit = {},
    onErrors: () -> Unit = {},
    showSetupCta: Boolean = false,
    onSetup: () -> Unit = {},
    /** Tarefa de questões do plano: abre a bateria direto, na quantidade pedida pelo plano. */
    onPlanQuestions: (br.com.estudario.data.local.planner.PlanTaskEntity, Int) -> Unit = { task, _ -> task.topicId?.let(onTopic) },
    /** Missão do ponto fraco: abre 10 questões da matéria. */
    onTrainSubject: (Long) -> Unit = {},
) {
    val competitions by viewModel.competitions.collectAsState()
    val subjects by viewModel.subjects.collectAsState()
    val topics by viewModel.topics.collectAsState()
    val questions by viewModel.questions.collectAsState()
    val attempts by viewModel.attempts.collectAsState()
    val errors by viewModel.errors.collectAsState()
    val reviews by viewModel.reviews.collectAsState()
    val profile by viewModel.profile.collectAsState()
    val streak by viewModel.streak.collectAsState()
    // O Folha salvou a sequência num dia sem estudo: a cena aparece uma vez por dia salvo.
    val savePrefs = androidx.compose.ui.platform.LocalContext.current.getSharedPreferences("estudario_ui", android.content.Context.MODE_PRIVATE)
    val savedRecently = streak?.protectedDays.orEmpty().filter { java.time.LocalDate.now().toEpochDay() - it.toEpochDays() <= 7 }.map { it.toString() }
    var savedSeen by remember { mutableStateOf(savePrefs.getStringSet("streak_saved_seen", emptySet()).orEmpty()) }
    val savedPending = savedRecently.filter { it !in savedSeen }
    // Só depois de conferir a conta: o dia "sem estudo" pode ter sido estudado no site e ainda não ter
    // chegado aqui (antes a cena aparecia ao abrir e gastava o marca-página de um dia estudado).
    val cloudSync = (androidx.compose.ui.platform.LocalContext.current.applicationContext as br.com.estudario.EstudarioApplication).cloudSync
    val syncStatus by cloudSync.status.collectAsState()
    val syncSettled = !cloudSync.enabled || syncStatus is br.com.estudario.data.sync.CloudSyncStatus.Done ||
        syncStatus is br.com.estudario.data.sync.CloudSyncStatus.Failed || syncStatus is br.com.estudario.data.sync.CloudSyncStatus.SignedOut
    if (savedPending.isNotEmpty() && streak != null && syncSettled) {
        val d = java.time.LocalDate.parse(savedPending.max())
        // os últimos 7 dias para os braseiros: 1 feito, 2 salvo pelo Folha, 0 sem estudo
        val s = streak!!
        val byDate = s.calendar.associateBy { it.date }
        val week = (6 downTo 0).map { back ->
            val day = java.time.LocalDate.now().minusDays(back.toLong())
            val kd = kotlinx.datetime.LocalDate.fromEpochDays(day.toEpochDay().toInt())
            val flag = when { kd in s.protectedDays -> 2; byDate[kd]?.let { br.com.estudario.domain.StreakEngine.isDone(it, s.goal) } == true -> 1; else -> 0 }
            day.dayOfWeek.getDisplayName(java.time.format.TextStyle.NARROW, java.util.Locale("pt", "BR")).uppercase() to flag
        }
        br.com.estudario.ui.profile.StreakSaveScene(
            streak!!.current, streak!!.freezes,
            d.dayOfWeek.getDisplayName(java.time.format.TextStyle.SHORT, java.util.Locale("pt", "BR")).replace(".", "").replaceFirstChar { it.uppercase() } + ", " + d.dayOfMonth + "/" + d.monthValue,
            week,
        ) {
            savedSeen = savedSeen + savedPending
            savePrefs.edit().putStringSet("streak_saved_seen", savedSeen).apply()
        }
    }
    // Os avisos do Folha (revisão do dia, sequência em risco): sem a permissão do Android nada chega
    // com o app fechado. O convite aparece no Início depois do primeiro estudo, e no máximo de novo
    // uma vez, uma semana depois de um "agora não".
    val inviteContext = androidx.compose.ui.platform.LocalContext.current
    var inviteStudy by remember {
        val granted = androidx.core.content.ContextCompat.checkSelfPermission(inviteContext, android.Manifest.permission.POST_NOTIFICATIONS) == android.content.pm.PackageManager.PERMISSION_GRANTED
        val asks = savePrefs.getInt("study_notifications_asks", 0)
        val last = savePrefs.getLong("study_notifications_asked_at", 0L)
        mutableStateOf(android.os.Build.VERSION.SDK_INT >= 33 && !granted && asks < 2 && System.currentTimeMillis() - last > java.util.concurrent.TimeUnit.DAYS.toMillis(7))
    }
    val studiedOnce = streak?.calendar?.any { br.com.estudario.domain.StreakEngine.isDone(it, streak!!.goal) } == true
    if (inviteStudy && studiedOnce && savedPending.isEmpty()) br.com.estudario.ui.components.NotificationInvite(forStudy = true, onDone = {
        savePrefs.edit().putInt("study_notifications_asks", savePrefs.getInt("study_notifications_asks", 0) + 1).putLong("study_notifications_asked_at", System.currentTimeMillis()).apply()
        inviteStudy = false
    })
    // Bateria do plano sem nada novo nem errado: avisa e oferece pular, em vez de repetir acertos.
    var allDoneTask by remember { mutableStateOf<br.com.estudario.data.local.planner.PlanTaskEntity?>(null) }
    allDoneTask?.let { task -> br.com.estudario.ui.planner.AllQuestionsDoneDialog(task, onSkip = { planViewModel.skip(task.id, "Sem questões novas nem erros para refazer"); allDoneTask = null }, onDismiss = { allDoneTask = null }) }
    val progress by viewModel.progress.collectAsState()
    val planState by planViewModel.state.collectAsState()
    val queue by viewModel.queue.collectAsState()
    val tourStep by viewModel.tourStep.collectAsState()
    val theories by viewModel.theories.collectAsState()

    // "Gerar o material deste tópico" na missão do dia abre o mesmo gerador do tópico.
    var generateFor by remember { mutableStateOf<TopicEntity?>(null) }
    val context = androidx.compose.ui.platform.LocalContext.current
    val scope = androidx.compose.runtime.rememberCoroutineScope()
    val importLauncher = androidx.activity.compose.rememberLauncherForActivityResult(androidx.activity.result.contract.ActivityResultContracts.OpenDocument()) { uri ->
        uri?.let {
            viewModel.beginIncomingFile()
            scope.launch {
                val text = withContext(Dispatchers.IO) { runCatching { context.contentResolver.openInputStream(it)?.use { s -> s.readBytes().toString(Charsets.UTF_8) } }.getOrNull() }
                if (text.isNullOrBlank()) viewModel.reportIncomingFileError("Não foi possível ler o arquivo escolhido.") else viewModel.openIncomingText(text, null)
            }
        }
    }
    generateFor?.let { topic ->
        br.com.estudario.ui.prompt.ContentPromptBuilderDialog(viewModel, topic.subjectId, setOf(topic.id), onDismiss = { generateFor = null }, onPickFile = { importLauncher.launch(arrayOf("*/*")) })
    }

    val competition =competitions.firstOrNull { it.isPrimary } ?: competitions.firstOrNull()

    val metrics by produceState<HomeMetrics?>(
        null, competition?.id, subjects, topics, questions, attempts, errors, reviews,
    ) {
        val current = competition
        if (current == null) {
            value = HomeMetrics()
            return@produceState
        }
        value = withContext(Dispatchers.Default) {
            calculateHomeMetrics(current.id, subjects, topics, questions, attempts, errors, reviews)
        }
    }

    val homePrefs = androidx.compose.ui.platform.LocalContext.current.getSharedPreferences("estudario_ui", android.content.Context.MODE_PRIVATE)
    var setupCtaDismissed by remember { mutableStateOf(homePrefs.getBoolean("setup_cta_dismissed", false)) }
    LazyColumn(
        Modifier.fillMaxSize(),
        // O espaço da barra inferior já chega aqui pelo padding do Scaffold; não duplicar com um
        // valor fixo evita que a Home fique com um rodapé diferente em cada modo de navegação.
        contentPadding = PaddingValues(top = EstudarioSpacing.small),
    ) {
        if (showSetupCta && !setupCtaDismissed) {
            item {
                Box(Modifier.padding(horizontal = EstudarioSpacing.screenGutter)) {
                    // Lembrete discreto, numa linha: o que importa na Home é o estudo, não a configuração.
                    Row(
                        Modifier.fillMaxWidth().clip(RoundedCornerShape(50)).background(MaterialTheme.colorScheme.surfaceContainerHigh)
                            .clickable(onClick = onSetup).padding(start = 14.dp, end = 4.dp, top = 4.dp, bottom = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text("Termine de configurar seu plano", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.weight(1f))
                        Text("Retomar", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
                        // Quem não quer retomar agora pode fechar; a configuração continua em Ajustes.
                        IconButton(onClick = { setupCtaDismissed = true; homePrefs.edit().putBoolean("setup_cta_dismissed", true).apply() }, modifier = Modifier.size(32.dp)) {
                            Icon(Icons.Outlined.Close, "Fechar aviso", Modifier.size(16.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }
            item { Spacer(Modifier.height(EstudarioSpacing.small)) }
        }
        if (competition == null) {
            item {
                Box(Modifier.padding(horizontal = EstudarioSpacing.screenGutter)) {
                    HomeNoContestState(onAddContest = onSyllabus)
                }
            }
            return@LazyColumn
        }

        val focusTask = pickFocusTask(planState)
        val planCurrentStudy = currentStudyState(planState, focusTask)
        // O plano ativo também manda nos dias de folga e depois de concluir o dia. A fila manual
        // só vira missão quando não há plano; não pode substituir o descanso previsto por ele.
        val currentQueueItem = pickHomeQueueTopic(planState, queue)
        val queueTask = currentQueueItem?.let { row ->
            val subjectName = subjects.firstOrNull { it.id == row.topic.subjectId }?.name ?: "Matéria removida"
            queueTopicUi(row, subjectName)
        }
        val selectedCurrentTask = currentHomeTask(
            queueTask = queueTask,
            planTask = (planCurrentStudy as? CurrentStudyUiState.Ready)?.task,
        )
        val currentStudy = when {
            queueTask != null -> CurrentStudyUiState.Ready(task = queueTask, progressFraction = null)
            selectedCurrentTask != null && planCurrentStudy is CurrentStudyUiState.Ready ->
                planCurrentStudy.copy(task = selectedCurrentTask)
            else -> planCurrentStudy
        }
        val activePlan = planState.activePlan

        item {
            val daysToExam = activePlan?.examEpochDay
                ?.let { java.time.temporal.ChronoUnit.DAYS.between(planState.today, LocalDate.ofEpochDay(it)) }
                ?.takeIf { it >= 0 }
            Box(Modifier.padding(horizontal = EstudarioSpacing.screenGutter)) {
                HomeHero(
                    firstName = profile.firstName,
                    contest = ActiveContestUi(competition.name, activePlan?.objective),
                    daysToExam = daysToExam,
                    coverage = coverageUi(metrics),
                    standing = standingUi(streak, progress),
                    onOpenContest = onSyllabus,
                )
            }
        }

        item { Spacer(Modifier.height(EstudarioSpacing.medium)) }

        // Primeiros passos: escolher o concurso (feito), ver o plano e responder 5 questões com o Folha.
        if (activePlan != null && attempts.size < 5) item(key = "first-steps") {
            Box(Modifier.padding(horizontal = EstudarioSpacing.screenGutter).padding(bottom = EstudarioSpacing.medium)) {
                FirstStepsCard(answered = attempts.size, onPlan = onPlan, onQuiz = { onQuiz(5, "random") })
            }
        }

        // Com tarefas do plano hoje, a missão do dia vem primeiro e já diz por onde começar (o cartão do
        // "agora" sairia repetindo o mesmo passo). A fila manual continua no cartão de sempre.
        val todayLoadTop = planState.todayTasks.filter { it.entity.status.countsAsPlannedLoad() }
        val missionFirst = activePlan != null && todayLoadTop.isNotEmpty() && queueTask == null
        val startCurrent: () -> Unit = {
            if (focusTask?.entity?.type == br.com.estudario.domain.planner.PlanTaskType.QUESTIONS) {
                val task = focusTask.entity
                scope.launch {
                    val count = runCatching { planViewModel.startQuestionTask(task.id) }.getOrDefault(0)
                    if (count > 0) onPlanQuestions(task, count)
                    else if (count < 0) allDoneTask = task
                    else startTask(viewModel, focusTask, onStudyTask, onFocus)
                }
            } else startTask(viewModel, focusTask, onStudyTask, onFocus)
        }
        if (missionFirst) item(key = "daily-mission") {
            val missingTopic = focusTask?.entity?.topicId
                ?.takeIf { id -> theories.none { it.topicId == id } && questions.none { it.question.topicId == id } }
                ?.let { id -> topics.firstOrNull { it.id == id } }
            Box(Modifier.padding(horizontal = EstudarioSpacing.screenGutter)) {
                DailyMissionCard(
                    missionUi(planState, todayLoadTop, focusTask),
                    onOpenPlan = onPlan,
                    modifier = Modifier.tourTarget(TourKey.HOME_MISSION, tourStep?.key) { viewModel.reportTourTargetBounds(TourKey.HOME_MISSION, it) },
                    onStartCurrent = startCurrent,
                    onGenerate = missingTopic?.let { topic -> { generateFor = topic } },
                )
            }
        }

        if (!missionFirst) item {
            Box(Modifier.padding(horizontal = EstudarioSpacing.screenGutter)) {
                // a memória do Folha: saudade, prova chegando, o que você estudou por último, seu nome
                val memory = remember(streak, attempts.size, profile.name, activePlan?.examEpochDay) {
                    val s = streak
                    val last = attempts.maxByOrNull { it.answeredAt }?.let { a ->
                        val topicId = questions.firstOrNull { it.question.id == a.questionId }?.question?.topicId
                        val subject = topics.firstOrNull { it.id == topicId }?.let { t -> subjects.firstOrNull { it.id == t.subjectId }?.name }
                        val start = LocalDate.now().atStartOfDay(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli()
                        subject?.let { it to if (a.answeredAt >= start) 0 else kotlin.math.ceil((start - a.answeredAt) / 86_400_000.0).toInt() }
                    }
                    br.com.estudario.domain.FolhaMemory.home(
                        br.com.estudario.domain.FolhaMemory.Context(
                            name = profile.firstName, bond = br.com.estudario.domain.FolhaBond.level(s?.activeDays ?: 0).index,
                            daysAway = s?.let { br.com.estudario.domain.FolhaBond.daysAway(it.calendar, br.com.estudario.time.today()) },
                            todayDone = s?.todayDone == true, streak = s?.current ?: 0,
                            daysToExam = activePlan?.examEpochDay?.let { (it - LocalDate.now().toEpochDay()).toInt() }?.takeIf { it >= 0 },
                            lastSubject = last?.first, lastSubjectDaysAgo = last?.second,
                            hour = java.time.LocalTime.now().hour, seed = System.currentTimeMillis() / 60_000,
                        ),
                    )
                }
                // missão do ponto fraco cumprida: ele comemora o antes e o depois aqui no balão
                val context = androidx.compose.ui.platform.LocalContext.current
                val missionLine = remember(attempts.size, br.com.estudario.ui.screens.home.MissionStore.version.intValue) {
                    val qt = questions.associate { it.question.id to it.question.topicId }
                    br.com.estudario.domain.StudyInsights.missionResult(
                        br.com.estudario.ui.screens.home.MissionStore.get(context), br.com.estudario.ui.screens.home.studyAnswers(attempts, qt, topics, subjects), System.currentTimeMillis(),
                    )?.let { r -> br.com.estudario.domain.FolhaMemory.Line(br.com.estudario.domain.FolhaFalas.mission(r.mission.subject, r.mission.before, r.after, r.mission.startedAt / 1000), if (r.delta >= 0) "proud" else "happy") }
                }
                CurrentStudySection(
                    memory = missionLine ?: memory,
                    state = currentStudy,
                    onPrimaryAction = {
                        when (currentStudy) {
                            is CurrentStudyUiState.Ready -> {
                                if (queueTask != null) queueTask.topicId?.let(onTopic)
                                else if (focusTask?.entity?.type == br.com.estudario.domain.planner.PlanTaskType.QUESTIONS) {
                                    // Questões do plano vão direto para as questões (como na tela do Plano); sem
                                    // questões salvas, cai no tópico para gerar.
                                    val task = focusTask.entity
                                    scope.launch {
                                        val count = runCatching { planViewModel.startQuestionTask(task.id) }.getOrDefault(0)
                                        if (count > 0) onPlanQuestions(task, count)
                                        else if (count < 0) allDoneTask = task
                                        else startTask(viewModel, focusTask, onStudyTask, onFocus)
                                    }
                                } else startTask(viewModel, focusTask, onStudyTask, onFocus)
                            }
                            is CurrentStudyUiState.NoPlan -> onPlan()
                            else -> Unit
                        }
                    },
                    modifier = Modifier.tourTarget(TourKey.HOME_MISSION, tourStep?.key) { viewModel.reportTourTargetBounds(TourKey.HOME_MISSION, it) },
                    practiceAvailable = questions.isNotEmpty(),
                    onPractice = { onQuiz(10, "daily") },
                    onGenerate = (currentStudy as? CurrentStudyUiState.Ready)?.task?.topicId
                        ?.takeIf { id -> theories.none { it.topicId == id } && questions.none { it.question.topicId == id } }
                        ?.let { id -> topics.firstOrNull { it.id == id } }
                        ?.let { topic -> { generateFor = topic } },
                    generateModifier = Modifier.tourTarget(TourKey.HOME_GENERATE, tourStep?.key) { viewModel.reportTourTargetBounds(TourKey.HOME_GENERATE, it) },
                )
            }
        }

        // o resumo da semana (domingo e segunda) e a missão do ponto fraco
        item {
            val questionTopic = remember(questions.size) { questions.associate { it.question.id to it.question.topicId } }
            Box(Modifier.padding(horizontal = EstudarioSpacing.screenGutter).padding(top = EstudarioSpacing.medium)) {
                br.com.estudario.ui.screens.home.StudyInsightsSection(attempts, questionTopic, topics, subjects, streak, onTrainSubject)
            }
        }
        // Missão do dia: o checklist do plano de hoje, logo abaixo do que fazer agora.
        val todayLoad = planState.todayTasks.filter { it.entity.status.countsAsPlannedLoad() }
        if (activePlan != null && todayLoad.isNotEmpty() && !missionFirst) {
            item { Spacer(Modifier.height(EstudarioSpacing.medium)) }
            item {
                Box(Modifier.padding(horizontal = EstudarioSpacing.screenGutter)) {
                    DailyMissionCard(missionUi(planState, todayLoad, focusTask), onOpenPlan = onPlan)
                }
            }
        }

        val nextUp = currentQueueItem?.let { selected ->
            val remaining = queue.asSequence()
                .filter { it.item.id != selected.item.id }
                .filter { firstEligibleQueueTopic(listOf(it)) != null }
                .sortedBy { it.item.position }
                .map { row ->
                    val subjectName = subjects.firstOrNull { it.id == row.topic.subjectId }?.name ?: "Matéria removida"
                    queueTopicUi(row, subjectName)
                }
                .toList()
            NextUpUi(items = remaining, remainingToday = remaining.size, fromQueue = true)
        } ?: nextUpToday(planState, focusTask)
        // Com a missão do dia na tela, o "Depois" repetiria as mesmas tarefas: só aparece para a fila.
        val missionShown = activePlan != null && todayLoad.isNotEmpty()
        if (nextUp.items.isNotEmpty() && (!missionShown || nextUp.fromQueue)) {
            item { Spacer(Modifier.height(EstudarioSpacing.medium)) }
            item {
                Box(Modifier.padding(horizontal = EstudarioSpacing.screenGutter)) {
                    NextUpStrip(nextUp, onOpenPlan = onPlan, onOpenQueue = onOpenQueue)
                }
            }
        }

        item { Spacer(Modifier.height(EstudarioSpacing.section)) }

        // "Seu edital" saiu do Início: "Meus concursos" já mostra o andamento do edital.

        item {
            Box(Modifier.padding(horizontal = EstudarioSpacing.screenGutter)) {
                HomeDivider(Modifier.padding(vertical = EstudarioSpacing.large))
            }
        }

        item {
            Box(Modifier.padding(horizontal = EstudarioSpacing.screenGutter)) {
                PaceForecast(pace = paceUi(planState), onOpenPlan = onPlan)
            }
        }

        item { Spacer(Modifier.height(EstudarioSpacing.large)) }

        item {
            Box(Modifier.padding(horizontal = EstudarioSpacing.screenGutter)) {
                PerformanceAndStanding(
                    performance = performanceUi(attempts),
                    standing = standingUi(streak, progress),
                    onOpenStatistics = onStatistics,
                    onOpenProfile = onProfile,
                    // Nível e constância moram no perfil; aqui fica só o desempenho.
                    showStanding = false,
                )
            }
        }


        item { Spacer(Modifier.height(EstudarioSpacing.large)) }

        item {
            val atual = metrics
            Box(Modifier.padding(horizontal = EstudarioSpacing.screenGutter)) {
                HomeAttention(
                    pendingReviews = atual?.pendingReviews ?: 0,
                    pendingErrors = atual?.errorsDueToday ?: 0,
                    weakTopicName = atual?.weakTopicTitle?.takeIf { atual.weakTopicId != null && it.isNotBlank() },
                    weakTopicMastery = atual?.weakTopicMastery ?: 0,
                    onOpenReviews = onReviews,
                    onOpenErrors = onErrors,
                    onOpenWeakTopic = { atual?.weakTopicId?.let(onTopic) },
                    overduePlanTasks = if (activePlan != null) planState.overdueTasks.size else 0,
                    onOpenPlan = { br.com.estudario.ui.planner.PlanOverdueRequest.request(); onPlan() },
                )
            }
        }

        item {
            Box(Modifier.padding(horizontal = EstudarioSpacing.screenGutter, vertical = EstudarioSpacing.medium)) {
                HomeSectionLink("Ver desempenho completo", onStatistics)
            }
        }
    }
}

// ---------------------------------------------------------------- mapeamento de estado
// Tradução do estado real para os modelos da pasta `home`. Nenhuma regra nova nasce aqui: previsão
// e ritmo vêm do domínio, cobertura vem de computeHomeMetrics, XP e sequência vêm do ProgressEngine
// e do StreakEngine. Esta camada só escolhe o que a Home mostra.

/**
 * A tarefa de agora, a mesma que a aba Plano mostra para hoje: a de hoje já em andamento, senão a
 * primeira de hoje na ordem do plano. Atrasadas ficam no aviso "Pedindo atenção" e tarefas futuras
 * ficam na continuidade. Sem pendências hoje, a Home respeita a folga ou o dia já concluído.
 */
internal fun pickFocusTask(planState: ActivePlanUiState): PlannerTaskUi? {
    val planId = planState.activePlan?.id ?: return null
    // A agenda já incorpora a disponibilidade semanal e as exceções de cada data. Não presumir
    // folga por dia da semana: uma exceção pode ter criado uma tarefa para este domingo.
    val ofToday = planState.todayTasks.filter {
        it.entity.planId == planId && it.entity.status in setOf(PlanTaskStatus.PLANEJADA, PlanTaskStatus.EM_ANDAMENTO)
    }
    return ofToday.firstOrNull { it.entity.status == PlanTaskStatus.EM_ANDAMENTO }
        ?: ofToday.firstOrNull()
}

internal fun pickHomeQueueTopic(planState: ActivePlanUiState, queue: List<QueueWithTopic>): QueueWithTopic? {
    val planDrives = planState.activePlan != null
    return if (planDrives) null else firstEligibleQueueTopic(queue)
}

internal fun currentStudyState(planState: ActivePlanUiState, focusTask: PlannerTaskUi?): CurrentStudyUiState {
    if (planState.loading && planState.activePlan == null) return CurrentStudyUiState.Loading
    if (planState.activePlan == null) return CurrentStudyUiState.NoPlan

    val todayTasks = planState.todayTasks
    if (todayTasks.isEmpty()) return CurrentStudyUiState.NoTaskToday

    val doneToday = todayTasks.count { it.entity.status == PlanTaskStatus.CONCLUIDA }
    if (focusTask == null || doneToday == todayTasks.size) {
        return CurrentStudyUiState.DayComplete(missionsToday = doneToday, minutesToday = planState.todayActualMinutes)
    }

    val progressFraction = if (focusTask.entity.status == PlanTaskStatus.EM_ANDAMENTO && focusTask.entity.plannedMinutes > 0) {
        (focusTask.actualMinutes.toFloat() / focusTask.entity.plannedMinutes).coerceIn(0f, 1f)
    } else null

    return CurrentStudyUiState.Ready(task = focusTask.toStudyTaskUi(), progressFraction = progressFraction)
}

/**
 * A fila do plano depois da de agora, na ordem do plano (data e posição), incluindo os próximos
 * dias: só as de hoje deixava a lista vazia assim que o dia acabava. A aba Plano continua sendo o
 * lugar do cronograma completo.
 */
private fun nextUpToday(planState: ActivePlanUiState, focusTask: PlannerTaskUi?): NextUpUi {
    val pending = setOf(PlanTaskStatus.PLANEJADA, PlanTaskStatus.EM_ANDAMENTO)
    val proximas = planState.tasks.withIndex()
        .filter { (_, row) -> row.entity.status in pending && row.entity.id != focusTask?.entity?.id }
        .sortedWith(compareBy({ it.value.entity.scheduledEpochDay }, { it.index }))
        .map { it.value }
        .take(6)
    val hoje = planState.todayTasks.count { it.entity.status in pending && it.entity.id != focusTask?.entity?.id }
    return NextUpUi(items = proximas.map { it.toStudyTaskUi() }, remainingToday = hoje)
}

private fun PlannerTaskUi.toStudyTaskUi(): StudyTaskUi = StudyTaskUi(
    id = entity.id,
    topicId = entity.topicId,
    subjectName = entity.subjectNameSnapshot,
    topicName = entity.taskTitlePtBr(),
    activityLabel = entity.type.displayNamePtBr(),
    durationLabel = minutesLabelPtBr(entity.plannedMinutes),
    ctaLabel = completionActionPtBr(entity.status),
    scheduledForToday = true,
)

/** As tarefas de hoje no formato da missão do dia (o passo atual é o mesmo do "agora"). */
private fun missionUi(planState: br.com.estudario.ui.planner.ActivePlanUiState, load: List<PlannerTaskUi>, focusTask: PlannerTaskUi?) = DailyMissionUi(
    items = load.map {
        MissionItemUi(
            title = it.entity.taskTitlePtBr(),
            subtitle = listOfNotNull(it.entity.type.displayNamePtBr(), minutesLabelPtBr(it.entity.plannedMinutes), it.entity.subjectNameSnapshot.takeIf { _ -> it.entity.topicNameSnapshot != null }).joinToString(" · "),
            done = it.entity.status == PlanTaskStatus.CONCLUIDA,
            current = it.entity.id == focusTask?.entity?.id && it.entity.status != PlanTaskStatus.CONCLUIDA,
            kind = it.entity.type,
            typeLabel = it.entity.type.displayNamePtBr(),
            minutes = it.entity.plannedMinutes,
            subject = it.entity.subjectNameSnapshot.takeIf { _ -> it.entity.topicNameSnapshot != null },
            inProgress = it.entity.status == PlanTaskStatus.EM_ANDAMENTO,
        )
    },
    plannedMinutes = planState.todayPlannedMinutes,
    doneMinutes = planState.todayActualMinutes,
)

private fun startTask(
    viewModel: AppViewModel,
    task: PlannerTaskUi?,
    onStudyTask: (Long, String) -> Unit,
    onFocus: () -> Unit,
) {
    val current = task ?: return
    val topicId = current.entity.topicId
    if (topicId != null) {
        // Leva para o tópico, onde a teoria e as questões estão de fato, o modo foco é uma etapa
        // de dentro do tópico, não o destino direto.
        onStudyTask(topicId, current.entity.id)
    } else {
        viewModel.startFocus(
            title = listOfNotNull(current.entity.subjectNameSnapshot, current.entity.topicNameSnapshot).joinToString(" › "),
            topicId = null,
            taskId = current.entity.id,
        )
        onFocus()
    }
}

private fun coverageUi(metrics: HomeMetrics?): SyllabusCoverageUi {
    val current = metrics ?: return SyllabusCoverageUi(0, 0, 0, emptyList(), null)
    return SyllabusCoverageUi(
        percent = current.coverage,
        studiedTopics = current.startedTopics,
        totalTopics = current.totalTopics,
        subjects = current.subjects,
        // Domínio médio só é honesto com base de questões: sem isso, o número mediria a própria
        // ausência de dados. Abaixo do mínimo, a Home simplesmente não fala em domínio.
        masteryPercent = current.mastery.takeIf { current.answeredCount >= MIN_ANSWERS_FOR_MASTERY },
    )
}

private fun paceUi(planState: ActivePlanUiState): PaceUi {
    val plan = planState.activePlan ?: return PaceUi.Unknown
    val pendente = planState.tasks.any { it.entity.status in setOf(PlanTaskStatus.PLANEJADA, PlanTaskStatus.EM_ANDAMENTO) }
        // Tópico do edital ainda não estudado também é trabalho que falta, mesmo sem tarefa marcada.
        || planState.roadmap?.let { it.studiedTopics < it.totalTopics } == true
    val pace = StudyPaceEvaluator.evaluate(
        today = planState.today.toKotlinLocalDate(),
        forecast = planState.forecastDate?.toKotlinLocalDate(),
        examDate = plan.examEpochDay?.let { kotlinx.datetime.LocalDate.fromEpochDays(it) },
        hasRemainingWork = pendente,
    )
    return when (pace) {
        is StudyPaceEvaluator.Pace.Comfortable -> PaceUi.Comfortable(pace.forecast.toJavaLocalDate(), pace.daysBeforeExam)
        is StudyPaceEvaluator.Pace.Tight -> PaceUi.Tight(pace.forecast.toJavaLocalDate(), pace.daysBeforeExam)
        is StudyPaceEvaluator.Pace.Behind -> PaceUi.Behind(pace.forecast.toJavaLocalDate(), pace.daysAfterExam)
        is StudyPaceEvaluator.Pace.NoExamDate -> PaceUi.NoExamDate(pace.forecast.toJavaLocalDate())
        StudyPaceEvaluator.Pace.Complete -> PaceUi.Complete
        StudyPaceEvaluator.Pace.Unknown -> PaceUi.Unknown
    }
}

/**
 * Desempenho recente: as últimas [RECENT_WINDOW] respostas contra as [RECENT_WINDOW] anteriores.
 * Janela fixa em número de questões, não em dias, assim quem responde pouco não vê a taxa oscilar
 * por causa de uma semana parada.
 */
private const val RECENT_WINDOW = 50
private const val MIN_ANSWERS_FOR_PERFORMANCE = 10
private const val MIN_ANSWERS_FOR_MASTERY = 20

private fun performanceUi(attempts: List<QuestionAttemptEntity>): PerformanceUi? {
    if (attempts.size < MIN_ANSWERS_FOR_PERFORMANCE) return null
    val ordenadas = attempts.sortedByDescending { it.answeredAt }
    val recentes = ordenadas.take(RECENT_WINDOW)
    val anteriores = ordenadas.drop(RECENT_WINDOW).take(RECENT_WINDOW)
    fun acerto(lista: List<QuestionAttemptEntity>) = lista.count { it.correct } * 100 / lista.size
    return PerformanceUi(
        recentAccuracy = acerto(recentes),
        previousAccuracy = if (anteriores.size >= MIN_ANSWERS_FOR_PERFORMANCE) acerto(anteriores) else null,
        answeredRecently = recentes.size,
    )
}

private fun standingUi(streak: StreakSummary?, progress: ProgressEngine.ProgressSummary?): StandingUi = StandingUi(
    streakDays = streak?.current ?: 0,
    bestStreakDays = streak?.best ?: 0,
    level = progress?.level ?: 1,
    levelTitle = progress?.levelTitle ?: "",
    totalXp = progress?.totalXp ?: 0,
    levelFraction = progress?.levelProgress ?: 0f,
    xpIntoLevel = progress?.xpIntoLevel ?: 0,
    xpForNextLevel = progress?.xpForNextLevel ?: 100,
)

/**
 * Os primeiros 3 minutos: o concurso já escolhido, ver o plano montado e responder 5 questões com o
 * Folha comemorando no fim. Some sozinho depois das 5 questões (ou quando a pessoa fecha).
 */
@Composable
private fun FirstStepsCard(answered: Int, onPlan: () -> Unit, onQuiz: () -> Unit) {
    val prefs = androidx.compose.ui.platform.LocalContext.current.getSharedPreferences("estudario_ui", android.content.Context.MODE_PRIVATE)
    var hidden by remember { mutableStateOf(prefs.getBoolean("first_steps_hidden", false)) }
    var planSeen by remember { mutableStateOf(prefs.getBoolean("first_steps_plan", false)) }
    if (hidden) return
    val scheme = MaterialTheme.colorScheme
    br.com.estudario.ui.brand.Surface(shape = RoundedCornerShape(22.dp), color = scheme.primaryContainer.copy(alpha = .55f), modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                br.com.estudario.ui.assistant.Folha(52.dp, mood = br.com.estudario.ui.assistant.FolhaMood.TALKING)
                Column(Modifier.weight(1f).padding(start = 10.dp)) {
                    Text("Seus primeiros 3 minutos", style = MaterialTheme.typography.titleMedium, fontWeight = androidx.compose.ui.text.font.FontWeight.Black)
                    val left = listOf(true, planSeen, answered >= 5).count { !it }
                    Text("Eu te mostro o caminho. Faltam $left ${if (left == 1) "passo" else "passos"}.", style = MaterialTheme.typography.bodySmall, color = scheme.onSurfaceVariant)
                }
                androidx.compose.material3.TextButton(onClick = { hidden = true; prefs.edit().putBoolean("first_steps_hidden", true).apply() }) { Text("Fechar") }
            }
            FirstStep(1, "Escolher o concurso", true, null) {}
            FirstStep(2, "Ver o seu plano montado", planSeen, "Ver plano") { planSeen = true; prefs.edit().putBoolean("first_steps_plan", true).apply(); onPlan() }
            FirstStep(3, if (answered > 0) "Responder 5 questões ($answered de 5)" else "Responder 5 questões", answered >= 5, "Começar") { onQuiz() }
        }
    }
}

@Composable
private fun FirstStep(n: Int, label: String, done: Boolean, action: String?, onClick: () -> Unit) {
    val scheme = MaterialTheme.colorScheme
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        Box(Modifier.size(28.dp).background(if (done) androidx.compose.ui.graphics.Color(0xFF22A06B) else scheme.surface, androidx.compose.foundation.shape.CircleShape), contentAlignment = Alignment.Center) {
            Text(if (done) "✓" else "$n", fontWeight = androidx.compose.ui.text.font.FontWeight.Black, color = if (done) androidx.compose.ui.graphics.Color.White else scheme.primary)
        }
        Text(label, Modifier.weight(1f), style = MaterialTheme.typography.bodyMedium, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold, color = if (done) scheme.onSurfaceVariant else scheme.onSurface)
        if (!done && action != null) androidx.compose.material3.FilledTonalButton(onClick = onClick) { Text(action) }
    }
}
