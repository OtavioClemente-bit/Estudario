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
        // Com um plano ativo e tarefas pendentes, a Home segue o plano, na ordem dele. A fila manual
        // de tópicos só manda quando não há plano; antes ela passava na frente e o "próximo a
        // estudar" ignorava o plano (inclusive o importado da IA).
        val planDrives = planState.activePlan != null && focusTask != null
        val currentQueueItem = if (planDrives) null else firstEligibleQueueTopic(queue)
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

        item {
            Box(Modifier.padding(horizontal = EstudarioSpacing.screenGutter)) {
                CurrentStudySection(
                    state = currentStudy,
                    onPrimaryAction = {
                        when (currentStudy) {
                            is CurrentStudyUiState.Ready -> {
                                if (queueTask != null) queueTask.topicId?.let(onTopic)
                                else startTask(viewModel, focusTask, onStudyTask, onFocus)
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

        // Missão do dia: o checklist do plano de hoje, logo abaixo do que fazer agora.
        val todayLoad = planState.todayTasks.filter { it.entity.status.countsAsPlannedLoad() }
        if (activePlan != null && todayLoad.isNotEmpty()) {
            item { Spacer(Modifier.height(EstudarioSpacing.medium)) }
            item {
                Box(Modifier.padding(horizontal = EstudarioSpacing.screenGutter)) {
                    DailyMissionCard(
                        DailyMissionUi(
                            items = todayLoad.map {
                                MissionItemUi(
                                    title = it.entity.taskTitlePtBr(),
                                    subtitle = listOfNotNull(it.entity.type.displayNamePtBr(), minutesLabelPtBr(it.entity.plannedMinutes), it.entity.subjectNameSnapshot.takeIf { _ -> it.entity.topicNameSnapshot != null }).joinToString(" · "),
                                    done = it.entity.status == PlanTaskStatus.CONCLUIDA,
                                    current = it.entity.id == focusTask?.entity?.id && it.entity.status != PlanTaskStatus.CONCLUIDA,
                                )
                            },
                            plannedMinutes = planState.todayPlannedMinutes,
                            doneMinutes = planState.todayActualMinutes,
                        ),
                        onOpenPlan = onPlan,
                    )
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

        item {
            Box(Modifier.padding(horizontal = EstudarioSpacing.screenGutter)) {
                SyllabusCoverage(
                    coverage = coverageUi(metrics),
                    onOpenSyllabus = onSyllabus,
                )
            }
        }

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
                )
            }
        }

        item { Spacer(Modifier.height(EstudarioSpacing.large)) }

        item {
            Box(Modifier.padding(horizontal = EstudarioSpacing.screenGutter)) {
                LevelRow(standing = standingUi(streak, progress), onOpenProfile = onProfile)
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
                    onOpenPlan = onPlan,
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
 * primeira de hoje na ordem do plano. Antes valia a pendente mais antiga do plano inteiro, e uma
 * tarefa de ontem esquecida tomava o lugar da de hoje: o Início mandava estudar uma matéria e o
 * Plano mostrava outra. Atrasadas ficam no aviso "Pedindo atenção"; sem nada pendente hoje, vale a
 * próxima da ordem.
 */
internal fun pickFocusTask(planState: ActivePlanUiState): PlannerTaskUi? {
    // Ordem do plano: data e, no mesmo dia, a posição em que a tarefa veio. Algo já começado vem
    // antes de abrir uma nova.
    val ordered = planState.tasks.withIndex()
        .filter { it.value.entity.status in setOf(PlanTaskStatus.PLANEJADA, PlanTaskStatus.EM_ANDAMENTO) }
        .sortedWith(compareBy({ it.value.entity.scheduledEpochDay }, { it.index }))
        .map { it.value }
    val today = planState.today.toEpochDay()
    val ofToday = ordered.filter { it.entity.scheduledEpochDay == today }
    return ofToday.firstOrNull { it.entity.status == PlanTaskStatus.EM_ANDAMENTO }
        ?: ofToday.firstOrNull()
        ?: ordered.firstOrNull { it.entity.status == PlanTaskStatus.EM_ANDAMENTO }
        ?: ordered.firstOrNull()
}

private fun currentStudyState(planState: ActivePlanUiState, focusTask: PlannerTaskUi?): CurrentStudyUiState {
    if (planState.loading && planState.activePlan == null) return CurrentStudyUiState.Loading
    if (planState.activePlan == null) return CurrentStudyUiState.NoPlan

    val todayTasks = planState.todayTasks
    // Nada marcado para hoje, mas o plano segue: mostra a próxima tarefa da ordem.
    if (todayTasks.isEmpty()) {
        return focusTask?.let { CurrentStudyUiState.Ready(task = it.toStudyTaskUi(), progressFraction = null) }
            ?: CurrentStudyUiState.NoTaskToday
    }

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
