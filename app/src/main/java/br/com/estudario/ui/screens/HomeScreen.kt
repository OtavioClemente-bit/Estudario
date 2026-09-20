package br.com.estudario.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.rounded.Bolt
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.HelpOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.estudario.data.local.*
import br.com.estudario.domain.*
import br.com.estudario.domain.planner.PlanTaskStatus
import br.com.estudario.ui.AppViewModel
import br.com.estudario.ui.components.*
import br.com.estudario.ui.planner.ActivePlanUiState
import br.com.estudario.ui.planner.StudyPlanViewModel
import br.com.estudario.ui.planner.reward
import br.com.estudario.ui.profile.BadgeArt
import br.com.estudario.ui.profile.UserProfile
import br.com.estudario.ui.tour.TourKey
import br.com.estudario.ui.tour.tourTarget
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

private val Azul = Color(0xFF071B45)
private val Indigo = Color(0xFF4F46E5)

@Composable
fun HomeScreen(
    viewModel: AppViewModel,
    planViewModel: StudyPlanViewModel,
    onQuiz: (Int, String) -> Unit,
    onTopic: (Long) -> Unit,
    onReviews: () -> Unit,
    onProfile: () -> Unit = {},
    onSearch: () -> Unit = {},
    onPlan: () -> Unit = {},
    onFocus: () -> Unit = {},
    onBadges: () -> Unit = {},
    onErrors: () -> Unit = {},
    onHelp: () -> Unit = {},
) {
    val competitions by viewModel.competitions.collectAsState()
    val subjects by viewModel.subjects.collectAsState()
    val topics by viewModel.topics.collectAsState()
    val questions by viewModel.questions.collectAsState()
    val attempts by viewModel.attempts.collectAsState()
    val errors by viewModel.errors.collectAsState()
    val reviews by viewModel.reviews.collectAsState()
    val history by viewModel.reviewHistory.collectAsState()
    val studySessions by viewModel.studySessions.collectAsState()
    val profile by viewModel.profile.collectAsState()
    val streak by viewModel.streak.collectAsState()
    val progress by viewModel.progress.collectAsState()
    val planState by planViewModel.state.collectAsState()
    val tourStep by viewModel.tourStep.collectAsState()

    val competition = competitions.firstOrNull { it.isPrimary } ?: competitions.firstOrNull()
    val focusTask = planState.tasks.firstOrNull {
        it.entity.status in setOf(PlanTaskStatus.PLANEJADA, PlanTaskStatus.EM_ANDAMENTO) &&
            it.entity.scheduledEpochDay <= planState.today.toEpochDay()
    } ?: planState.tasks.firstOrNull {
        it.entity.status in setOf(PlanTaskStatus.PLANEJADA, PlanTaskStatus.EM_ANDAMENTO)
    }

    val metrics by produceState<HomeMetrics?>(
        null, competition?.id, subjects, topics, questions, attempts, errors, reviews, history, studySessions,
    ) {
        val current = competition
        if (current == null) {
            value = HomeMetrics()
            return@produceState
        }
        value = withContext(Dispatchers.Default) {
            computeHomeMetrics(current.id, subjects, topics, questions, attempts, errors, reviews, history, studySessions)
        }
    }

    LazyColumn(Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(20.dp)) {
        item {
            Hero(
                profile = profile,
                subtitle = competition?.name ?: "Comece criando seu concurso",
                streakDays = streak?.current ?: 0,
                level = progress?.level ?: 1,
                levelTitle = progress?.levelTitle.orEmpty(),
                xpToday = progress?.xpToday ?: 0,
                forecastDate = planState.forecastDate,
                coverage = metrics?.coverage,
                onProfile = onProfile,
                onSearch = onSearch,
                profileModifier = Modifier.tourTarget(TourKey.HOME_PROFILE, tourStep?.key) { viewModel.reportTourTargetBounds(TourKey.HOME_PROFILE, it) },
            )
        }

        if (competition == null) {
            item {
                Box(Modifier.padding(horizontal = 20.dp)) {
                    EmptyState("Seu espaço de estudo está vazio", "Toque em Edital para importar o conteúdo do seu concurso, ou veja o passo a passo em Mais › Como usar o app.")
                }
            }
        } else {
            item {
                Box(Modifier.padding(horizontal = 20.dp)) {
                    StudyFocusCard(
                        task = focusTask,
                        onStart = {
                            focusTask?.let { task ->
                                viewModel.startFocus(
                                    title = listOfNotNull(task.entity.subjectNameSnapshot, task.entity.topicNameSnapshot).joinToString(" › "),
                                    topicId = task.entity.topicId,
                                    taskId = task.entity.id,
                                )
                            }
                            onFocus()
                        },
                        onOpenPlan = onPlan,
                    )
                }
            }

            item {
                Box(Modifier.padding(horizontal = 20.dp)) {
                    PlanoDeHojeHeader(planState, streak)
                }
            }

            val todayTasks = planState.todayTasks
            if (todayTasks.isNotEmpty()) {
                items(todayTasks, key = { it.entity.id }) { task ->
                    Box(Modifier.padding(horizontal = 20.dp)) {
                        MissionCard(
                            taskUi = task,
                            onStart = {
                                viewModel.startFocus(
                                    title = listOfNotNull(task.entity.subjectNameSnapshot, task.entity.topicNameSnapshot).joinToString(" › "),
                                    topicId = task.entity.topicId,
                                    taskId = task.entity.id
                                )
                                onFocus()
                            },
                            isOverdue = false
                        )
                    }
                }
            } else {
                item {
                    Box(Modifier.padding(horizontal = 20.dp)) {
                        DesafioDoDiaCard(questions.isNotEmpty(), onQuiz)
                    }
                }
            }

            item {
                Text(
                    text = "Seu progresso",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Black,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
                )
            }

            item {
                Box(Modifier.padding(horizontal = 20.dp)) {
                    metrics?.let { CoverageCard(it.coverage, it.mastery) } ?: SkeletonCard(lines = 3)
                }
            }

            item {
                Box(Modifier.padding(horizontal = 20.dp)) {
                    metrics?.let {
                        PendingCard(
                            reviewsPending = it.pendingReviews,
                            errorsPending = it.errorsDueToday,
                            onClick = onReviews,
                        )
                    } ?: SkeletonCard(lines = 3)
                }
            }

            item {
                Box(Modifier.padding(horizontal = 20.dp)) {
                    RitmoDaSemana(streak)
                }
            }

            item {
                Box(Modifier.padding(horizontal = 20.dp)) {
                    if (metrics?.weakTopicId != null) {
                        WeakTopicCard(
                            topicName = metrics!!.weakTopicTitle,
                            masteryPercent = metrics!!.weakTopicMastery,
                            onClick = { onTopic(metrics!!.weakTopicId!!) },
                        )
                    } else {
                        progress?.nextBadges?.firstOrNull()?.let { proximo ->
                            ProximaConquistaCard(proximo, onBadges)
                        }
                    }
                }
            }
        }
        item { Spacer(Modifier.height(16.dp)) }
    }
}

// ---------------------------------------------------------------- Hero

@Composable
private fun Hero(
    profile: UserProfile,
    subtitle: String,
    streakDays: Int,
    level: Int,
    levelTitle: String,
    xpToday: Int,
    forecastDate: LocalDate?,
    coverage: Int?,
    onProfile: () -> Unit,
    onSearch: () -> Unit,
    profileModifier: Modifier = Modifier,
) {
    Box(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
            .background(Brush.linearGradient(listOf(Indigo, Color(0xFF3A32B8), Azul)))
            .padding(start = 20.dp, end = 20.dp, top = 24.dp, bottom = 28.dp),
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Box(profileModifier.clip(CircleShape).clickable(onClick = onProfile)) {
                    ProfileAvatar(profile.photoPath, profile.initials, 52.dp)
                }
                Column(Modifier.weight(1f)) {
                    Text("${greeting()},", color = Color.White.copy(alpha = 0.72f), style = MaterialTheme.typography.bodyMedium)
                    Text(profile.firstName, color = Color.White, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, maxLines = 1)
                    Text(subtitle, color = Color.White.copy(alpha = 0.6f), style = MaterialTheme.typography.bodySmall, maxLines = 1)
                }
                IconButton(onClick = onSearch) { Icon(Icons.Outlined.Search, "Pesquisar", tint = Color.White) }
            }
            
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                HeroPill(Modifier.weight(1f), "$streakDays", if (streakDays == 1) "dia seguido" else "dias seguidos", onProfile) {
                    AppMark(20.dp)
                }
                if (forecastDate != null) {
                    val formatter = DateTimeFormatter.ofPattern("MMM/yy", Locale("pt", "BR"))
                    HeroPill(Modifier.weight(1f), forecastDate.format(formatter).replaceFirstChar { it.uppercase() }, "previsão", onProfile) {
                        Box(
                            Modifier.size(20.dp).clip(CircleShape).background(Color.White.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center,
                        ) { Text("~", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Black) }
                    }
                } else {
                    HeroPill(Modifier.weight(1f), "+$xpToday", "XP hoje", onProfile) {
                        Icon(Icons.Rounded.Bolt, null, Modifier.size(18.dp), tint = Color(0xFFFFC94D))
                    }
                }
                if (coverage != null) {
                    HeroPill(Modifier.weight(1f), "$coverage%", "cobertura", onProfile) {
                        Box(
                            Modifier.size(20.dp).clip(CircleShape).background(Color.White.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center,
                        ) { Text("%", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Black) }
                    }
                }
            }
        }
    }
}

@Composable
private fun HeroPill(modifier: Modifier, value: String, label: String, onClick: () -> Unit, icon: @Composable () -> Unit) {
    Row(
        modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White.copy(alpha = 0.12f))
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        icon()
        Column {
            Text(value, color = Color.White, fontWeight = FontWeight.Black, style = MaterialTheme.typography.titleMedium, maxLines = 1)
            Text(label, color = Color.White.copy(alpha = 0.65f), fontSize = 10.sp, maxLines = 1)
        }
    }
}

// ---------------------------------------------------------------- plano de hoje

@Composable
private fun PlanoDeHojeHeader(plan: ActivePlanUiState, streak: StreakSummary?) {
    val batida = streak?.todayDone == true
    val progresso = streak?.todayProgress ?: 0f

    Row(
        Modifier.fillMaxWidth().padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text("Plano de Hoje", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black)
            Text("${plan.todayPlannedMinutes} min • ${plan.todayTasks.size} missões", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Box(Modifier.size(46.dp)) {
            AnelDaMeta(progresso, batida, 46.dp)
        }
    }
}

@Composable
private fun DesafioDoDiaCard(temQuestoes: Boolean, onQuiz: (Int, String) -> Unit) {
    ElevatedCard(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Box(
                    Modifier.size(46.dp).clip(CircleShape).background(MaterialTheme.colorScheme.tertiary.copy(alpha = 0.16f)),
                    contentAlignment = Alignment.Center,
                ) { Icon(Icons.Rounded.Bolt, null, Modifier.size(24.dp), tint = MaterialTheme.colorScheme.tertiary) }
                Column(Modifier.weight(1f)) {
                    Text("Desafio do Dia", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text("Não há missões no plano. Complete um desafio para manter a sequência.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            Button(onClick = { onQuiz(10, "daily") }, Modifier.fillMaxWidth(), enabled = temQuestoes) {
                Text("Iniciar 10 questões")
            }
        }
    }
}

@Composable
private fun AnelDaMeta(progresso: Float, batida: Boolean, size: Dp = 82.dp) {
    val trilha = MaterialTheme.colorScheme.surfaceVariant
    val cor = if (batida) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary
    Box(Modifier.size(size), contentAlignment = Alignment.Center) {
        Canvas(Modifier.fillMaxSize()) {
            val espessura = this.size.minDimension * 0.12f
            val inset = espessura / 2f
            drawArc(
                color = trilha,
                startAngle = 270f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = Offset(inset, inset),
                size = Size(this.size.width - espessura, this.size.height - espessura),
                style = Stroke(width = espessura, cap = StrokeCap.Round),
            )
            drawArc(
                color = cor,
                startAngle = 270f,
                sweepAngle = 360f * (if (batida) 1f else progresso).coerceIn(0f, 1f),
                useCenter = false,
                topLeft = Offset(inset, inset),
                size = Size(this.size.width - espessura, this.size.height - espessura),
                style = Stroke(width = espessura, cap = StrokeCap.Round),
            )
        }
        if (batida) {
            Icon(Icons.Rounded.Check, null, Modifier.size(size * 0.45f), tint = cor)
        } else {
            Text("${(progresso * 100).toInt()}%", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Black)
        }
    }
}

// ---------------------------------------------------------------- ritmo da semana compact

@Composable
private fun RitmoDaSemana(streak: StreakSummary?) {
    if (streak == null) { SkeletonCard(lines = 2); return }
    val porData = remember(streak) { streak.calendar.associateBy { it.date } }
    val teto = remember(porData) { porData.values.maxOfOrNull { it.minutes }?.coerceAtLeast(30) ?: 30 }
    val letras = listOf("S", "T", "Q", "Q", "S", "S", "D")

    ElevatedCard(Modifier.fillMaxWidth().height(140.dp)) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Ritmo da semana", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(Modifier.weight(1f))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Bottom) {
                streak.week.forEach { dia ->
                    val minutos = porData[dia.date]?.minutes ?: 0
                    val altura = (40f * minutos / teto).coerceIn(if (minutos > 0) 6f else 4f, 40f)
                    val cor = when {
                        dia.done -> MaterialTheme.colorScheme.secondary
                        dia.partial -> MaterialTheme.colorScheme.secondary.copy(alpha = 0.4f)
                        dia.future -> MaterialTheme.colorScheme.surfaceVariant
                        else -> MaterialTheme.colorScheme.error.copy(alpha = 0.25f)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Box(
                            Modifier
                                .width(10.dp)
                                .height(altura.dp)
                                .clip(RoundedCornerShape(50))
                                .background(cor),
                        )
                        Text(
                            letras.getOrElse(dia.date.dayOfWeek.value - 1) { "" },
                            fontSize = 10.sp,
                            fontWeight = if (dia.date == LocalDate.now()) FontWeight.Black else FontWeight.Normal,
                            color = if (dia.date == LocalDate.now()) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }
        }
    }
}

// ---------------------------------------------------------------- cards auxiliares

@Composable
private fun ProximaConquistaCard(proximo: BadgeProgress, onBadges: () -> Unit) {
    ElevatedCard(Modifier.fillMaxWidth().height(140.dp).clickable(onClick = onBadges)) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Próxima conquista", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(Modifier.weight(1f))
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                BadgeArt(proximo.badge, earned = false, size = 32.dp)
                Column(Modifier.weight(1f)) {
                    Text(proximo.badge.name, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, maxLines = 1)
                    LinearProgressIndicator({ proximo.percent }, Modifier.fillMaxWidth().padding(vertical = 4.dp).height(4.dp).clip(RoundedCornerShape(50)))
                    Text(proximo.hint ?: "Faltam ${proximo.remaining}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1)
                }
            }
        }
    }
}

@Composable
private fun ContinuarCard(titulo: String, onClick: () -> Unit) {
    ElevatedCard(
        Modifier.fillMaxWidth().clickable(onClick = onClick),
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
    ) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(14.dp)) {
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text("CONTINUAR ESTUDO", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.primary)
                Text(titulo, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, maxLines = 1)
            }
            Box(
                Modifier.size(40.dp).clip(CircleShape).background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center,
            ) { Icon(Icons.AutoMirrored.Outlined.ArrowForward, null, Modifier.size(20.dp), tint = MaterialTheme.colorScheme.onPrimary) }
        }
    }
}

// ---------------------------------------------------------------- dados

private fun greeting(now: LocalTime = LocalTime.now()): String = when (now.hour) {
    in 0..4 -> "Boa madrugada"
    in 5..11 -> "Bom dia"
    in 12..17 -> "Boa tarde"
    else -> "Boa noite"
}

private data class HomeMetrics(
    val coverage: Int = 0,
    val startedTopics: Int = 0,
    val totalTopics: Int = 0,
    val mastery: Int = 0,
    val pendingReviews: Int = 0,
    val pendingErrors: Int = 0,
    val errorsDueToday: Int = 0,
    val answeredCount: Int = 0,
    val streakCurrent: Int = 0,
    val streakBest: Int = 0,
    val weakTopicId: Long? = null,
    val weakTopicTitle: String = "",
    val weakTopicMastery: Int = 0,
)

private fun computeHomeMetrics(
    competitionId: Long,
    subjects: List<SubjectEntity>,
    topics: List<TopicEntity>,
    questions: List<QuestionWithOptions>,
    attempts: List<QuestionAttemptEntity>,
    errors: List<ErrorWithQuestion>,
    reviews: List<ReviewScheduleEntity>,
    history: List<ReviewHistoryEntity>,
    studySessions: List<StudySessionEntity>,
): HomeMetrics {
    val now = System.currentTimeMillis()
    val subjectIds = subjects.filter { it.competitionId == competitionId }.mapTo(hashSetOf()) { it.id }
    val competitionTopics = topics.filter { it.subjectId in subjectIds }
    val questionsByTopic = questions.groupBy { it.question.topicId }
    val attemptsByQuestion = attempts.groupBy { it.questionId }
    val errorsByQuestion = errors.groupBy { it.entry.questionId }
    val reviewsByTopic = reviews.groupBy { it.topicId }

    var masterySum = 0
    var weakTopic: TopicEntity? = null
    var weakMastery = Int.MAX_VALUE
    competitionTopics.forEach { topic ->
        val topicQuestions = questionsByTopic[topic.id].orEmpty()
        val topicAttempts = topicQuestions.flatMap { attemptsByQuestion[it.question.id].orEmpty() }
        val recent = topicAttempts.sortedByDescending { it.answeredAt }.take(20)
        val topicErrors = topicQuestions.flatMap { errorsByQuestion[it.question.id].orEmpty() }
        val topicReviews = reviewsByTopic[topic.id].orEmpty()
        val percent = MasteryCalculator.percent(
            MasteryInput(
                topic.status,
                topicQuestions.sumOf { it.question.answerCount },
                topicQuestions.sumOf { it.question.correctCount },
                recent.count { !it.correct },
                topicReviews.count { it.completedAt != null },
                recent.size,
                recent.count { it.correct },
                topicErrors.count { it.entry.status == ErrorStatus.RECORRENTE },
                topicReviews.count { ReviewPolicy.status(it, now) == ComputedReviewStatus.ATRASADA },
            ),
        )
        masterySum += percent
        if (topic.status != TopicStatus.NAO_ESTUDADO && percent < weakMastery) {
            weakMastery = percent
            weakTopic = topic
        }
    }

    val started = competitionTopics.count { it.status != TopicStatus.NAO_ESTUDADO }
    val activityDays = (
        attempts.map { StreakCalculator.day(it.answeredAt) } +
            history.map { StreakCalculator.day(it.reviewedAt) } +
            studySessions.map { StreakCalculator.day(it.completedAt) }
        ).toSet()
    val streak = StreakCalculator.calculate(activityDays)
    return HomeMetrics(
        coverage = if (competitionTopics.isEmpty()) 0 else started * 100 / competitionTopics.size,
        startedTopics = started,
        totalTopics = competitionTopics.size,
        mastery = if (competitionTopics.isEmpty()) 0 else masterySum / competitionTopics.size,
        pendingReviews = reviews.count { ReviewPolicy.status(it, now) in setOf(ComputedReviewStatus.DISPONIVEL, ComputedReviewStatus.ATRASADA) },
        pendingErrors = errors.count { it.entry.pending },
        errorsDueToday = errors.count { item -> item.entry.nextRetryAt?.let { it <= now } == true },
        answeredCount = questions.sumOf { it.question.answerCount },
        streakCurrent = streak.current,
        streakBest = streak.best,
        weakTopicId = weakTopic?.id,
        weakTopicTitle = weakTopic?.title.orEmpty(),
        weakTopicMastery = if (weakTopic == null) 0 else weakMastery,
    )
}
