package br.com.estudario.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.rounded.Bolt
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material.icons.outlined.EventRepeat
import androidx.compose.material.icons.outlined.Quiz
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.HelpOutline
import androidx.compose.material.icons.outlined.TrendingUp
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
    val queue by viewModel.queue.collectAsState()
    val history by viewModel.reviewHistory.collectAsState()
    val studySessions by viewModel.studySessions.collectAsState()
    val profile by viewModel.profile.collectAsState()
    val streak by viewModel.streak.collectAsState()
    val progress by viewModel.progress.collectAsState()
    val planState by planViewModel.state.collectAsState()
    val tourStep by viewModel.tourStep.collectAsState()

    val competition = competitions.firstOrNull { it.isPrimary } ?: competitions.firstOrNull()
    val next = queue.firstOrNull { !it.item.paused }

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

    LazyColumn(Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        item {
            Hero(
                profile = profile,
                subtitle = competition?.name ?: "Comece criando seu concurso",
                streakDays = streak?.current ?: 0,
                level = progress?.level ?: 1,
                levelTitle = progress?.levelTitle.orEmpty(),
                xpToday = progress?.xpToday ?: 0,
                onProfile = onProfile,
                onSearch = onSearch,
                onHelp = onHelp,
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
                Box(Modifier.padding(horizontal = 20.dp).tourTarget(TourKey.HOME_MISSION, tourStep?.key) { viewModel.reportTourTargetBounds(TourKey.HOME_MISSION, it) }) {
                    MissaoDeHoje(streak, planState, onQuiz = { onQuiz(10, "daily") }, onPlan = onPlan, temQuestoes = questions.isNotEmpty())
                }
            }

            next?.let { queueItem ->
                item {
                    Box(Modifier.padding(horizontal = 20.dp)) {
                        ContinuarCard(queueItem.topic.title) { onTopic(queueItem.topic.id) }
                    }
                }
            }

            item {
                Box(Modifier.padding(horizontal = 20.dp)) {
                    RitmoDaSemana(streak)
                }
            }

            item {
                val summary = metrics
                Box(Modifier.padding(horizontal = 20.dp)) {
                    if (summary == null) SkeletonCard(lines = 3) else Panorama(summary, onReviews, onErrors)
                }
            }

            metrics?.weakTopicId?.let { topicId ->
                item {
                    Box(Modifier.padding(horizontal = 20.dp)) {
                        FocoRecomendado(metrics!!.weakTopicTitle, metrics!!.weakTopicMastery) { onTopic(topicId) }
                    }
                }
            }

            progress?.nextBadges?.firstOrNull()?.let { proximo ->
                item {
                    Box(Modifier.padding(horizontal = 20.dp)) {
                        ProximaConquista(proximo, onBadges)
                    }
                }
            }
        }
        item { Spacer(Modifier.height(8.dp)) }
    }
}

// ---------------------------------------------------------------- topo

@Composable
private fun Hero(
    profile: UserProfile,
    subtitle: String,
    streakDays: Int,
    level: Int,
    levelTitle: String,
    xpToday: Int,
    onProfile: () -> Unit,
    onSearch: () -> Unit,
    onHelp: () -> Unit,
    profileModifier: Modifier = Modifier,
) {
    Box(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp))
            .background(Brush.linearGradient(listOf(Indigo, Color(0xFF3A32B8), Azul)))
            // O Scaffold já desconta a barra de status; aqui é só o respiro interno do cabeçalho.
            .padding(start = 20.dp, end = 20.dp, top = 18.dp, bottom = 22.dp),
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Box(profileModifier.clip(CircleShape).clickable(onClick = onProfile)) {
                    ProfileAvatar(profile.photoPath, profile.initials, 48.dp)
                }
                Column(Modifier.weight(1f)) {
                    Text("${greeting()},", color = Color.White.copy(alpha = 0.72f), style = MaterialTheme.typography.bodyMedium)
                    Text(profile.firstName, color = Color.White, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, maxLines = 1)
                    Text(subtitle, color = Color.White.copy(alpha = 0.6f), style = MaterialTheme.typography.bodySmall, maxLines = 1)
                }
                IconButton(onClick = onHelp) { Icon(Icons.Outlined.HelpOutline, "Como usar o app", tint = Color.White) }
                IconButton(onClick = onSearch) { Icon(Icons.Outlined.Search, "Pesquisar em todo o conteúdo", tint = Color.White) }
            }
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                HeroPill(Modifier.weight(1f), "$streakDays", if (streakDays == 1) "dia seguido" else "dias seguidos", onProfile) {
                    AppMark(22.dp)
                }
                HeroPill(Modifier.weight(1f), "$level", levelTitle.ifBlank { "nível" }, onProfile) {
                    Box(
                        Modifier.size(22.dp).clip(CircleShape).background(Color.White.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center,
                    ) { Text("N", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Black) }
                }
                HeroPill(Modifier.weight(1f), "+$xpToday", "XP hoje", onProfile) {
                    Icon(Icons.Rounded.Bolt, null, Modifier.size(20.dp), tint = Color(0xFFFFC94D))
                }
            }
        }
    }
}

@Composable
private fun HeroPill(modifier: Modifier, value: String, label: String, onClick: () -> Unit, icon: @Composable () -> Unit) {
    Row(
        modifier
            .clip(RoundedCornerShape(14.dp))
            .background(Color.White.copy(alpha = 0.12f))
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 9.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(7.dp),
    ) {
        icon()
        Column {
            Text(value, color = Color.White, fontWeight = FontWeight.Black, style = MaterialTheme.typography.titleMedium, maxLines = 1)
            Text(label, color = Color.White.copy(alpha = 0.65f), fontSize = 10.sp, maxLines = 1)
        }
    }
}

// ---------------------------------------------------------------- missão

@Composable
private fun MissaoDeHoje(
    streak: StreakSummary?,
    plan: ActivePlanUiState,
    onQuiz: () -> Unit,
    onPlan: () -> Unit,
    temQuestoes: Boolean,
) {
    val tarefas = plan.todayTasks
    val concluidas = tarefas.filter { it.entity.status == PlanTaskStatus.CONCLUIDA }
    val pendentes = tarefas - concluidas.toSet()
    val xpNaMesa = pendentes.sumOf { it.reward().base }
    val batida = streak?.todayDone == true
    val progresso = streak?.todayProgress ?: 0f

    ElevatedCard(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(18.dp)) {
                AnelDaMeta(progresso, batida)
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("Missão de hoje", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text(
                        when {
                            batida -> "Meta batida. A sequência de hoje está garantida."
                            tarefas.isNotEmpty() -> "${pendentes.size} atividade(s) do plano esperando você."
                            streak != null -> "Faltam ${(streak.goal.questions - streak.todayQuestions).coerceAtLeast(0)} questões — ou 1 tarefa do plano."
                            else -> "Comece por qualquer atividade do dia."
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    if (xpNaMesa > 0) {
                        Spacer(Modifier.height(2.dp))
                        XpTag(ProgressEngine.XpReward(xpNaMesa), showBonus = false)
                    }
                }
            }
            if (tarefas.isNotEmpty()) {
                Button(onClick = onPlan, Modifier.fillMaxWidth()) {
                    Text(if (pendentes.isEmpty()) "Ver o plano de hoje" else "Abrir minhas atividades")
                    Spacer(Modifier.width(8.dp))
                    Icon(Icons.AutoMirrored.Outlined.ArrowForward, null, Modifier.size(18.dp))
                }
            } else {
                Button(onClick = onQuiz, Modifier.fillMaxWidth(), enabled = temQuestoes) {
                    Icon(Icons.Rounded.Bolt, null, Modifier.size(18.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("Desafio do dia — 10 questões")
                }
            }
        }
    }
}

/** Anel da meta do dia: cheio e com o certo verde quando fechada. */
@Composable
private fun AnelDaMeta(progresso: Float, batida: Boolean, size: Dp = 82.dp) {
    val trilha = MaterialTheme.colorScheme.surfaceVariant
    val cor = if (batida) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary
    Box(Modifier.size(size), contentAlignment = Alignment.Center) {
        Canvas(Modifier.fillMaxSize()) {
            val espessura = this.size.minDimension * 0.11f
            val inset = espessura / 2f
            drawArc(
                color = trilha,
                startAngle = 135f,
                sweepAngle = 270f,
                useCenter = false,
                topLeft = Offset(inset, inset),
                size = Size(this.size.width - espessura, this.size.height - espessura),
                style = Stroke(width = espessura, cap = StrokeCap.Round),
            )
            drawArc(
                color = cor,
                startAngle = 135f,
                sweepAngle = 270f * (if (batida) 1f else progresso).coerceIn(0f, 1f),
                useCenter = false,
                topLeft = Offset(inset, inset),
                size = Size(this.size.width - espessura, this.size.height - espessura),
                style = Stroke(width = espessura, cap = StrokeCap.Round),
            )
        }
        if (batida) {
            Icon(Icons.Rounded.Check, null, Modifier.size(size * 0.42f), tint = cor)
        } else {
            Text("${(progresso * 100).toInt()}%", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black)
        }
    }
}

// ---------------------------------------------------------------- semana

@Composable
private fun RitmoDaSemana(streak: StreakSummary?) {
    if (streak == null) { SkeletonCard(lines = 2); return }
    val porData = remember(streak) { streak.calendar.associateBy { it.date } }
    val teto = remember(porData) { porData.values.maxOfOrNull { it.minutes }?.coerceAtLeast(30) ?: 30 }
    val letras = listOf("S", "T", "Q", "Q", "S", "S", "D")

    ElevatedCard(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text("Ritmo da semana", fontWeight = FontWeight.Bold)
                    Text(
                        "${streak.week.count { it.done }} de 7 dias no alvo",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                Text("melhor: ${streak.best}", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.Bottom) {
                streak.week.forEach { dia ->
                    val minutos = porData[dia.date]?.minutes ?: 0
                    val altura = (52f * minutos / teto).coerceIn(if (minutos > 0) 8f else 4f, 52f)
                    val cor = when {
                        dia.done -> MaterialTheme.colorScheme.secondary
                        dia.partial -> MaterialTheme.colorScheme.secondary.copy(alpha = 0.4f)
                        dia.future -> MaterialTheme.colorScheme.surfaceVariant
                        else -> MaterialTheme.colorScheme.error.copy(alpha = 0.25f)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Box(
                            Modifier
                                .width(16.dp)
                                .height(altura.dp)
                                .clip(RoundedCornerShape(50))
                                .background(cor),
                        )
                        Text(
                            letras.getOrElse(dia.date.dayOfWeek.value - 1) { "" },
                            fontSize = 11.sp,
                            fontWeight = if (dia.date == LocalDate.now()) FontWeight.Black else FontWeight.Normal,
                            color = if (dia.date == LocalDate.now()) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }
        }
    }
}

// ---------------------------------------------------------------- panorama

@Composable
private fun Panorama(summary: HomeMetrics, onReviews: () -> Unit, onErrors: () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        ElevatedCard(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("Onde você está", fontWeight = FontWeight.Bold)
                Barra("Cobertura do edital", summary.coverage, "${summary.startedTopics} de ${summary.totalTopics} tópicos", MaterialTheme.colorScheme.primary)
                Barra("Domínio estimado", summary.mastery, "teoria, questões, erros e revisões", MaterialTheme.colorScheme.tertiary)
            }
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Tile(Icons.Outlined.EventRepeat, summary.pendingReviews.toString(), "revisões hoje", MaterialTheme.colorScheme.primary, Modifier.weight(1f), onReviews)
            // Quando há erro vencido na escada de reencontro, o número do dia é esse — é o que a
            // pessoa precisa fazer hoje, não o total acumulado.
            val erroEmAberto = summary.errorsDueToday > 0
            Tile(
                Icons.Outlined.ErrorOutline,
                (if (erroEmAberto) summary.errorsDueToday else summary.pendingErrors).toString(),
                if (erroEmAberto) "erros voltam hoje" else "erros pendentes",
                MaterialTheme.colorScheme.error,
                Modifier.weight(1f),
                onErrors,
            )
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Tile(Icons.Outlined.Quiz, summary.answeredCount.toString(), "respostas salvas", MaterialTheme.colorScheme.secondary, Modifier.weight(1f))
            Tile(Icons.Outlined.TrendingUp, "${summary.streakBest}", "melhor sequência", MaterialTheme.colorScheme.tertiary, Modifier.weight(1f))
        }
    }
}

@Composable
private fun Barra(titulo: String, percent: Int, apoio: String, cor: Color) {
    Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(titulo, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
            Text("$percent%", fontWeight = FontWeight.Black, color = cor)
        }
        LinearProgressIndicator({ percent / 100f }, Modifier.fillMaxWidth().height(7.dp).clip(RoundedCornerShape(50)), color = cor)
        Text(apoio, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun Tile(icon: ImageVector, valor: String, label: String, cor: Color, modifier: Modifier = Modifier, onClick: (() -> Unit)? = null) {
    ElevatedCard(modifier.then(if (onClick == null) Modifier else Modifier.clickable(onClick = onClick))) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Box(
                Modifier.size(32.dp).clip(RoundedCornerShape(10.dp)).background(cor.copy(alpha = 0.14f)),
                contentAlignment = Alignment.Center,
            ) { Icon(icon, null, Modifier.size(18.dp), tint = cor) }
            Text(valor, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Black)
            Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

// ---------------------------------------------------------------- cartões de ação

@Composable
private fun ContinuarCard(titulo: String, onClick: () -> Unit) {
    ElevatedCard(
        Modifier.fillMaxWidth().clickable(onClick = onClick),
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
    ) {
        Row(Modifier.padding(18.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(14.dp)) {
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                Text("CONTINUAR DE ONDE PAROU", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.primary)
                Text(titulo, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, maxLines = 2)
            }
            Box(
                Modifier.size(42.dp).clip(CircleShape).background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center,
            ) { Icon(Icons.AutoMirrored.Outlined.ArrowForward, null, Modifier.size(20.dp), tint = MaterialTheme.colorScheme.onPrimary) }
        }
    }
}

@Composable
private fun FocoRecomendado(titulo: String, dominio: Int, onClick: () -> Unit) {
    ElevatedCard(Modifier.fillMaxWidth().clickable(onClick = onClick)) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(14.dp)) {
            Box(
                Modifier.size(40.dp).clip(RoundedCornerShape(12.dp)).background(MaterialTheme.colorScheme.error.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center,
            ) { Icon(Icons.Outlined.TrendingUp, null, Modifier.size(20.dp), tint = MaterialTheme.colorScheme.error) }
            Column(Modifier.weight(1f)) {
                Text("FOCO RECOMENDADO", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.error)
                Text(titulo, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, maxLines = 2)
                Text("Domínio em $dominio% — retome a teoria ou faça questões.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@Composable
private fun ProximaConquista(proximo: BadgeProgress, onBadges: () -> Unit) {
    ElevatedCard(Modifier.fillMaxWidth().clickable(onClick = onBadges)) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(14.dp)) {
            BadgeArt(proximo.badge, earned = false, size = 46.dp)
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("PRÓXIMO EMBLEMA", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.primary)
                Text(proximo.badge.name, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                LinearProgressIndicator({ proximo.percent }, Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(50)))
                Text(
                    proximo.hint ?: "faltam ${proximo.remaining} ${proximo.badge.unit}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
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

/** Resumo do Início já calculado: a tela só desenha, não faz conta. */
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

/**
 * Cálculo puro do resumo do Início. Indexa questões, tentativas, erros e revisões uma única vez em
 * vez de varrer as listas inteiras por tópico — com um edital grande, a versão anterior era o que
 * segurava a thread principal.
 */
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
