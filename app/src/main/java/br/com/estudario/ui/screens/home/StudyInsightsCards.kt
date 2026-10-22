package br.com.estudario.ui.screens.home

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
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
import br.com.estudario.ui.assistant.Folha
import br.com.estudario.ui.assistant.FolhaMood
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
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        if (streak != null) WeekReport(answers, streak)
        val spot = remember(answers) { val now = System.currentTimeMillis(); StudyInsights.weakSpot(answers, now) ?: StudyInsights.weakSpot(answers, now, 14) }
        if (spot != null) WeakSpot(spot, onTrainSubject)
    }
}

@Composable
private fun InsightCard(accent: Color, content: @Composable ColumnScope.() -> Unit) {
    Row(Modifier.fillMaxWidth().clip(RoundedCornerShape(20.dp)).background(MaterialTheme.colorScheme.surfaceContainerLow)) {
        Box(Modifier.width(4.dp).fillMaxHeight().background(accent))
        Column(Modifier.padding(16.dp).weight(1f), verticalArrangement = Arrangement.spacedBy(10.dp), content = content)
    }
}

@Composable
private fun WeakSpot(spot: StudyInsights.WeakSpot, onTrainSubject: (Long) -> Unit) {
    val scheme = MaterialTheme.colorScheme
    InsightCard(Color(0xFFF0506B)) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Folha(56.dp, mood = FolhaMood.POINT, enter = false)
            Column(Modifier.weight(1f)) {
                Text("MISSÃO DO FOLHA", style = MaterialTheme.typography.labelSmall, color = scheme.onSurfaceVariant, letterSpacing = 1.sp)
                Text("Reforçar ${spot.subject}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black)
                Text(
                    "Você errou ${spot.wrong} de ${spot.total} questões dessa matéria nas últimas semanas (${spot.accuracy}% de acerto). Treinar o ponto fraco é o que mais sobe a nota.",
                    style = MaterialTheme.typography.bodySmall, color = scheme.onSurfaceVariant,
                )
            }
        }
        // o acerto e a meta de 70%
        BoxWithConstraints(Modifier.fillMaxWidth().height(10.dp)) {
            Box(Modifier.fillMaxSize().clip(RoundedCornerShape(50)).background(Color(0x24F0506B)))
            Box(Modifier.fillMaxHeight().fillMaxWidth(spot.accuracy / 100f).clip(RoundedCornerShape(50)).background(Brush.horizontalGradient(listOf(Color(0xFFF0506B), Color(0xFFFFB421)))))
            Box(Modifier.offset(x = maxWidth * 0.7f).width(3.dp).fillMaxHeight().background(Color(0xFF1FA65C)))
        }
        Button(onClick = { onTrainSubject(spot.subjectId) }, modifier = Modifier.fillMaxWidth()) {
            Icon(Icons.Outlined.GpsFixed, null, Modifier.size(18.dp)); Spacer(Modifier.width(6.dp)); Text("Treinar 10 questões de ${spot.subject}", fontWeight = FontWeight.Bold)
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
            Folha(56.dp, mood = if (week.days >= 4) FolhaMood.PROUD else FolhaMood.WAVE, enter = false)
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
