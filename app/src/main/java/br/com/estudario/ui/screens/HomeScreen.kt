package br.com.estudario.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
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
import br.com.estudario.ui.screens.home.HomeHeader
import br.com.estudario.ui.screens.home.HomeMetrics
import br.com.estudario.ui.screens.home.HomeNoContestState
import br.com.estudario.ui.screens.home.calculateHomeMetrics
import br.com.estudario.ui.screens.home.HomeSectionLink
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
import kotlinx.coroutines.withContext
import java.time.LocalDate

/**
 * O painel de evolução do concurso — não o planejamento, e não a agenda.
 *
 * A Home responde, em segundos: qual concurso estou estudando, o que faço agora, há quanto tempo
 * mantenho constância, quanto do edital já cobri, como está meu desempenho e quando devo terminar
 * o conteúdo. A resposta de "como vou chegar lá" é a aba Plano; a de "o que estou fazendo agora" é
 * a tela de estudo. Misturar os três é o que transformava esta tela numa cópia do cronograma.
 *
 * Composição, de cima para baixo:
 *
 * 1. [HomeHeader]              — saudação discreta e o concurso ativo.
 * 2. [JourneySnapshot]          — onde a pessoa está no edital e na própria evolução.
 * 3. [CurrentStudySection]      — AGORA: a única coisa com peso máximo na tela.
 * 4. [NextUpStrip]              — a continuidade, sem esconder atividades.
 * 5. [SyllabusCoverage]         — o edital completo, matéria por matéria.
 * 6. [PaceForecast]             — quando o edital fecha, e o que isso significa perto da prova.
 * 7. [PerformanceAndStanding]   — acerto recente e constância, dois números escolhidos.
 * 8. [LevelRow]                 — nível e XP, discretos, fechando a evolução.
 * 9. [HomeAttention]            — revisões, erros e ponto frágil, no rodapé.
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
    onFocus: () -> Unit = {},
    onStatistics: () -> Unit = {},
    onErrors: () -> Unit = {},
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
    val tourStep by viewModel.tourStep.collectAsState()

    val competition = competitions.firstOrNull { it.isPrimary } ?: competitions.firstOrNull()

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

    LazyColumn(
        Modifier.fillMaxSize(),
        // O espaço da barra inferior já chega aqui pelo padding do Scaffold; não duplicar com um
        // valor fixo evita que a Home fique com um rodapé diferente em cada modo de navegação.
        contentPadding = PaddingValues(top = EstudarioSpacing.small),
    ) {
        if (competition == null) {
            item {
                Box(Modifier.padding(horizontal = EstudarioSpacing.screenGutter)) {
                    HomeNoContestState(onAddContest = onSyllabus)
                }
            }
            return@LazyColumn
        }

        val focusTask = pickFocusTask(planState)
        val currentStudy = currentStudyState(planState, focusTask)
        val activePlan = planState.activePlan

        item {
            Box(Modifier.padding(horizontal = EstudarioSpacing.screenGutter)) {
                HomeHeader(
                    firstName = profile.firstName,
                    contest = ActiveContestUi(competition.name, activePlan?.objective),
                    onOpenContest = onSyllabus,
                )
            }
        }

        item { Spacer(Modifier.height(EstudarioSpacing.comfortable)) }

        item {
            Box(Modifier.padding(horizontal = EstudarioSpacing.screenGutter)) {
                JourneySnapshot(
                    coverage = coverageUi(metrics),
                    standing = standingUi(streak, progress),
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
                            is CurrentStudyUiState.Ready -> startTask(viewModel, focusTask, onStudyTask, onFocus)
                            is CurrentStudyUiState.NoPlan -> onPlan()
                            else -> Unit
                        }
                    },
                    modifier = Modifier.tourTarget(TourKey.HOME_MISSION, tourStep?.key) { viewModel.reportTourTargetBounds(TourKey.HOME_MISSION, it) },
                    practiceAvailable = questions.isNotEmpty(),
                    onPractice = { onQuiz(10, "daily") },
                )
            }
        }

        val nextUp = nextUpToday(planState, focusTask)
        if (nextUp.items.isNotEmpty()) {
            item { Spacer(Modifier.height(EstudarioSpacing.medium)) }
            item {
                Box(Modifier.padding(horizontal = EstudarioSpacing.screenGutter)) {
                    NextUpStrip(nextUp, onOpenPlan = onPlan)
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

/** A tarefa de agora: a primeira já em andamento, senão a primeira planejada. */
private fun pickFocusTask(planState: ActivePlanUiState): PlannerTaskUi? =
    planState.tasks.firstOrNull {
        it.entity.status in setOf(PlanTaskStatus.PLANEJADA, PlanTaskStatus.EM_ANDAMENTO) &&
            it.entity.scheduledEpochDay <= planState.today.toEpochDay()
    } ?: planState.tasks.firstOrNull {
        it.entity.status in setOf(PlanTaskStatus.PLANEJADA, PlanTaskStatus.EM_ANDAMENTO)
    }

private fun currentStudyState(planState: ActivePlanUiState, focusTask: PlannerTaskUi?): CurrentStudyUiState {
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

/** As próximas de hoje, sem a de agora — a aba Plano continua sendo o lugar do cronograma completo. */
private fun nextUpToday(planState: ActivePlanUiState, focusTask: PlannerTaskUi?): NextUpUi {
    val pendentes = planState.todayTasks.filter {
        it.entity.status in setOf(PlanTaskStatus.PLANEJADA, PlanTaskStatus.EM_ANDAMENTO) &&
            it.entity.id != focusTask?.entity?.id
    }
    return NextUpUi(items = pendentes.map { it.toStudyTaskUi() }, remainingToday = pendentes.size)
}

private fun PlannerTaskUi.toStudyTaskUi(): StudyTaskUi = StudyTaskUi(
    id = entity.id,
    topicId = entity.topicId,
    subjectName = entity.subjectNameSnapshot,
    topicName = entity.topicNameSnapshot ?: "Sessão de estudos",
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
        // Leva para o tópico, onde a teoria e as questões estão de fato — o modo foco é uma etapa
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
        today = planState.today,
        forecast = planState.forecastDate,
        examDate = plan.examEpochDay?.let(LocalDate::ofEpochDay),
        hasRemainingWork = pendente,
    )
    return when (pace) {
        is StudyPaceEvaluator.Pace.Comfortable -> PaceUi.Comfortable(pace.forecast, pace.daysBeforeExam)
        is StudyPaceEvaluator.Pace.Tight -> PaceUi.Tight(pace.forecast, pace.daysBeforeExam)
        is StudyPaceEvaluator.Pace.Behind -> PaceUi.Behind(pace.forecast, pace.daysAfterExam)
        is StudyPaceEvaluator.Pace.NoExamDate -> PaceUi.NoExamDate(pace.forecast)
        StudyPaceEvaluator.Pace.Complete -> PaceUi.Complete
        StudyPaceEvaluator.Pace.Unknown -> PaceUi.Unknown
    }
}

/**
 * Desempenho recente: as últimas [RECENT_WINDOW] respostas contra as [RECENT_WINDOW] anteriores.
 * Janela fixa em número de questões, não em dias — assim quem responde pouco não vê a taxa oscilar
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
