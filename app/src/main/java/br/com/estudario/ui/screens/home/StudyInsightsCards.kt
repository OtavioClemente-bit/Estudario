package br.com.estudario.ui.screens.home

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.GpsFixed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.estudario.data.local.QuestionAttemptEntity
import br.com.estudario.data.local.SubjectEntity
import br.com.estudario.data.local.TopicEntity
import br.com.estudario.domain.StreakSummary
import br.com.estudario.domain.StudyInsights
import br.com.estudario.ui.brand.Button

/** As questões respondidas, com a matéria de cada uma (ponto fraco e resumo da semana). */
internal fun studyAnswers(
    attempts: List<QuestionAttemptEntity>, questionTopic: Map<Long, Long>, topics: List<TopicEntity>, subjects: List<SubjectEntity>,
): List<StudyInsights.Answer> {
    val topicSubject = topics.associate { it.id to it.subjectId }
    val subjectName = subjects.associate { it.id to it.name }
    return attempts.map { a ->
        val sid = questionTopic[a.questionId]?.let { topicSubject[it] }
        StudyInsights.Answer(a.answeredAt, a.correct, sid, sid?.let { subjectName[it] })
    }
}

/**
 * Na tela inicial, depois do "Agora": o resumo da semana (domingo e segunda, até fechar) e a missão do
 * Folha para o ponto fraco, com o botão que já abre 10 questões da matéria.
 */
@Composable
fun StudyInsightsSection(
    attempts: List<QuestionAttemptEntity>, questionTopic: Map<Long, Long>, topics: List<TopicEntity>, subjects: List<SubjectEntity>,
    streak: StreakSummary?, onTrainSubject: (Long) -> Unit,
) {
    val answers = remember(attempts.size, topics.size) { studyAnswers(attempts, questionTopic, topics, subjects) }
    val context = LocalContext.current
    val done = remember(answers, MissionStore.version.intValue) { StudyInsights.missionResult(MissionStore.get(context), answers, System.currentTimeMillis()) }
    val train = { id: Long, name: String, fallback: Int -> MissionStore.start(context, id, name, StudyInsights.subjectAccuracy(answers, id, System.currentTimeMillis()) ?: fallback); onTrainSubject(id) }
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        if (streak != null) WeekReport(answers, streak)
        if (done != null) MissionDone(done, onAgain = { train(done.mission.subjectId, done.mission.subject, done.after) }, onClose = { MissionStore.clear(context) })
        val spot = remember(answers) { val now = System.currentTimeMillis(); StudyInsights.weakSpot(answers, now) ?: StudyInsights.weakSpot(answers, now, 14) }
        // a missão acabou de ser cumprida nesta matéria: o card de cima já fala dela
        if (spot != null && done?.mission?.subjectId != spot.subjectId) WeakSpot(spot) { train(spot.subjectId, spot.subject, spot.accuracy) }
    }
}

@Composable
private fun InsightCard(accent: Color, content: @Composable ColumnScope.() -> Unit) {
    Row(Modifier.fillMaxWidth().clip(RoundedCornerShape(20.dp)).background(MaterialTheme.colorScheme.surfaceContainerLow)) {
        Box(Modifier.width(4.dp).fillMaxHeight().background(accent))
        Column(Modifier.padding(16.dp).weight(1f), verticalArrangement = Arrangement.spacedBy(10.dp), content = content)
    }
}

/**
 * O anel do acerto na matéria, com a marca verde da meta de 70%. Fica no lugar do Folha na missão: ele já
 * está no card do "Agora", logo acima.
 */
@Composable
private fun AccuracyRing(accuracy: Int, shown: Float = accuracy.toFloat(), from: Color = Color(0xFFF0506B), to: Color = Color(0xFFFFB421)) {
    val track = from.copy(alpha = 0.16f)
    Box(Modifier.size(64.dp), contentAlignment = Alignment.Center) {
        androidx.compose.foundation.Canvas(Modifier.fillMaxSize()) {
            val w = 6.dp.toPx(); val inset = w / 2 + 2.dp.toPx()
            val arcSize = androidx.compose.ui.geometry.Size(size.width - inset * 2, size.height - inset * 2); val tl = androidx.compose.ui.geometry.Offset(inset, inset)
            drawArc(track, 0f, 360f, false, tl, arcSize, style = androidx.compose.ui.graphics.drawscope.Stroke(w))
            drawArc(Brush.sweepGradient(listOf(from, to, from)), -90f, 360f * shown / 100f, false, tl, arcSize,
                style = androidx.compose.ui.graphics.drawscope.Stroke(w, cap = androidx.compose.ui.graphics.StrokeCap.Round))
            // a meta: 70% da volta
            val a = Math.toRadians((-90.0 + 252.0)); val r = arcSize.width / 2; val cx = size.width / 2; val cy = size.height / 2
            val dx = kotlin.math.cos(a).toFloat(); val dy = kotlin.math.sin(a).toFloat()
            drawLine(Color(0xFF1FA65C), androidx.compose.ui.geometry.Offset(cx + dx * (r - w), cy + dy * (r - w)), androidx.compose.ui.geometry.Offset(cx + dx * (r + w), cy + dy * (r + w)), 3.dp.toPx(), androidx.compose.ui.graphics.StrokeCap.Round)
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("$accuracy%", fontWeight = FontWeight.Black, fontSize = 16.sp, lineHeight = 16.sp)
            Text("acerto", fontSize = 10.sp, lineHeight = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun WeakSpot(spot: StudyInsights.WeakSpot, onTrain: () -> Unit) {
    val scheme = MaterialTheme.colorScheme
    InsightCard(Color(0xFFF0506B)) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            AccuracyRing(spot.accuracy)
            Column(Modifier.weight(1f)) {
                Text("MISSÃO DO FOLHA", style = MaterialTheme.typography.labelSmall, color = scheme.onSurfaceVariant, letterSpacing = 1.sp)
                Text("Reforçar ${spot.subject}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black)
                Text(
                    "Você errou ${spot.wrong} de ${spot.total} questões dessa matéria nas últimas semanas (${spot.accuracy}% de acerto). Treinar o ponto fraco é o que mais sobe a nota.",
                    style = MaterialTheme.typography.bodySmall, color = scheme.onSurfaceVariant,
                )
            }
        }
        Button(onClick = onTrain, modifier = Modifier.fillMaxWidth()) {
            Icon(Icons.Outlined.GpsFixed, null, Modifier.size(18.dp)); Spacer(Modifier.width(6.dp)); Text("Treinar 10 questões de ${spot.subject}", fontWeight = FontWeight.Bold)
        }
    }
}

/**
 * A missão do ponto fraco, guardada ao tocar em "Treinar": a matéria, o acerto daquele momento e a hora.
 * Quando a pessoa responde as questões dela, o Início mostra o antes e o depois e o Folha comemora.
 */
object MissionStore {
    val version = mutableIntStateOf(0)
    fun get(context: Context): StudyInsights.Mission? = StudyInsights.Mission.decode(weekPrefs(context).getString("missao", null))
    fun start(context: Context, subjectId: Long, subject: String, before: Int) {
        weekPrefs(context).edit().putString("missao", StudyInsights.Mission(subjectId, subject, before, System.currentTimeMillis()).encode()).apply(); version.intValue++
    }
    fun clear(context: Context) { weekPrefs(context).edit().remove("missao").apply(); version.intValue++ }
}

/**
 * Missão cumprida: o anel sai do acerto de antes e corre até o de agora, com a diferença em pontos e,
 * se a matéria ainda pede reforço, o botão para mais uma rodada.
 */
@Composable
private fun MissionDone(r: StudyInsights.MissionResult, onAgain: () -> Unit, onClose: () -> Unit) {
    val scheme = MaterialTheme.colorScheme
    val anim = remember(r.mission.startedAt) { androidx.compose.animation.core.Animatable(r.mission.before.toFloat()) }
    val ctxSom = LocalContext.current
    LaunchedEffect(r.mission.startedAt, r.after) {
        // a primeira vez que o card aparece para esta missão, toca o som de missão cumprida
        val p = weekPrefs(ctxSom)
        if (p.getLong("missao_som", 0L) != r.mission.startedAt) { p.edit().putLong("missao_som", r.mission.startedAt).apply(); br.com.estudario.ui.brand.EstudarioSom.play(ctxSom, "missao") }
        kotlinx.coroutines.delay(450); anim.animateTo(r.after.toFloat(), androidx.compose.animation.core.tween(1600, easing = androidx.compose.animation.core.FastOutSlowInEasing)) }
    val green = Color(0xFF1FA65C)
    InsightCard(green) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            AccuracyRing(r.after, anim.value, green, Color(0xFF7EE0B8))
            Column(Modifier.weight(1f)) {
                Text("MISSÃO CUMPRIDA", style = MaterialTheme.typography.labelSmall, color = scheme.onSurfaceVariant, letterSpacing = 1.sp)
                Text(r.mission.subject, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black)
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("${r.mission.before}% → ${r.after}%", fontWeight = FontWeight.Black)
                    if (r.delta != 0) {
                        val c = if (r.delta > 0) green else Color(0xFFF0506B)
                        Text(if (r.delta > 0) "+${r.delta} pts" else "${r.delta} pts", Modifier.clip(RoundedCornerShape(50)).background(c.copy(alpha = 0.16f)).padding(horizontal = 8.dp, vertical = 2.dp), color = c, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
                Text("${r.answered} questões respondidas nessa matéria desde a missão.", style = MaterialTheme.typography.bodySmall, color = scheme.onSurfaceVariant)
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            if (r.after < 70) Button(onClick = onAgain, modifier = Modifier.weight(1f)) {
                Icon(Icons.Outlined.GpsFixed, null, Modifier.size(18.dp)); Spacer(Modifier.width(6.dp)); Text("Mais 10 questões", fontWeight = FontWeight.Bold)
            }
            OutlinedButton(onClick = onClose, modifier = Modifier.weight(1f)) { Text("Fechar") }
        }
    }
}

private fun weekPrefs(context: Context) = context.getSharedPreferences("study_insights", Context.MODE_PRIVATE)

/** Tempo curto para as caixinhas: "45 min", "2h", "13h43". */
private fun hm(min: Int): String = when { min < 60 -> "$min min"; min % 60 == 0 -> "${min / 60}h"; else -> "${min / 60}h${(min % 60).toString().padStart(2, '0')}" }

@Composable
private fun WeekReport(answers: List<StudyInsights.Answer>, streak: StreakSummary) {
    val context = LocalContext.current
    val today = br.com.estudario.time.today()
    // domingo ou segunda
    if (today.dayOfWeek.ordinal !in setOf(6, 0)) return
    val start = java.time.LocalDate.now().atStartOfDay(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli()
    val week = remember(answers, streak.calendar.size) { StudyInsights.week(answers, streak.calendar, today, start) }
    val key = week.from.toString()
    var seen by remember { mutableStateOf(weekPrefs(context).getString("vista", null) == key) }
    if (seen || (week.days == 0 && week.prevDays == 0)) return
    val scheme = MaterialTheme.colorScheme
    InsightCard(Color(0xFF7EE0B8)) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Box(Modifier.size(52.dp).clip(RoundedCornerShape(16.dp)).background(Color(0x297EE0B8)), contentAlignment = Alignment.Center) {
                Icon(Icons.Outlined.DateRange, null, Modifier.size(28.dp), tint = Color(0xFF3FBF8F))
            }
            Column(Modifier.weight(1f)) {
                Text("SUA SEMANA", style = MaterialTheme.typography.labelSmall, color = scheme.onSurfaceVariant, letterSpacing = 1.sp)
                Text(StudyInsights.weekLine(week), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black)
            }
            IconButton(onClick = { weekPrefs(context).edit().putString("vista", key).apply(); seen = true }) { Icon(Icons.Outlined.Close, "Fechar o resumo") }
        }
        fun delta(d: Int, fmt: (Int) -> String = { "$it" }) = when { d > 0 -> "+" + fmt(d); d < 0 -> "−" + fmt(-d); else -> null }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            WeekStat("${week.days}", if (week.days == 1) "dia" else "dias", delta(week.days - week.prevDays), Modifier.weight(1f))
            WeekStat(hm(week.minutes), "estudo", delta(week.minutes - week.prevMinutes, ::hm), Modifier.weight(1f))
            WeekStat("${week.questions}", "questões", delta(week.questions - week.prevQuestions), Modifier.weight(1f))
            WeekStat(week.accuracy?.let { "$it%" } ?: "–", "acerto", week.accuracyDelta?.let { d -> delta(d) { "$it pts" } }, Modifier.weight(1f))
        }
        week.best?.let { (name, acc) -> Text("Mais forte: $name ($acc%)", style = MaterialTheme.typography.labelMedium, color = Color(0xFF1FA65C), fontWeight = FontWeight.Bold) }
        week.worst?.let { (name, acc) -> Text("Pede atenção: $name ($acc%)", style = MaterialTheme.typography.labelMedium, color = Color(0xFFD93A2B), fontWeight = FontWeight.Bold) }
    }
}

@Composable
private fun WeekStat(value: String, label: String, delta: String?, modifier: Modifier) {
    Column(modifier.clip(RoundedCornerShape(14.dp)).background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)).padding(horizontal = 8.dp, vertical = 8.dp)) {
        Text(value, fontWeight = FontWeight.Black, fontSize = 17.sp, maxLines = 1)
        Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1)
        if (delta != null) Text(delta, fontSize = 10.sp, fontWeight = FontWeight.Black, color = if (delta.startsWith("+")) Color(0xFF1FA65C) else Color(0xFFD93A2B), maxLines = 1)
    }
}
