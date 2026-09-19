package br.com.estudario.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.estudario.ui.AppViewModel
import br.com.estudario.ui.components.MetricCard
import br.com.estudario.domain.StreakCalculator
import java.time.LocalDate

@Composable
fun StatisticsScreen(viewModel: AppViewModel, onBack: () -> Unit) {
    val subjects by viewModel.subjects.collectAsState()
    val topics by viewModel.topics.collectAsState()
    val questions by viewModel.questions.collectAsState()
    val attempts by viewModel.attempts.collectAsState()
    val reviews by viewModel.reviews.collectAsState()
    val history by viewModel.reviewHistory.collectAsState()
    val studySessions by viewModel.studySessions.collectAsState()
    val questionSessions by viewModel.questionSessions.collectAsState()
    var periodDays by remember { mutableIntStateOf(30) }
    val since = System.currentTimeMillis() - periodDays * 86_400_000L
    val periodAttempts = attempts.filter { it.answeredAt >= since }
    val total = periodAttempts.size
    val correct = periodAttempts.count { it.correct }
    val activityDays = (attempts.map { StreakCalculator.day(it.answeredAt) } + history.map { StreakCalculator.day(it.reviewedAt) } + studySessions.map { StreakCalculator.day(it.completedAt) }).toSet()
    val streak = StreakCalculator.calculate(activityDays)
    LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically) { IconButton(onClick = onBack) { Icon(Icons.Outlined.ArrowBack, "Voltar") }; Text("Desempenho", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold) }
        }
        item { FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) { listOf(7 to "7 dias", 30 to "30 dias", 90 to "90 dias", 3650 to "Tudo").forEach { (days, label) -> FilterChip(selected = periodDays == days, onClick = { periodDays = days }, label = { Text(label) }) } } }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                MetricCard("Questões", total.toString(), "tentativas", modifier = Modifier.weight(1f))
                MetricCard("Acertos", if (total == 0) "—" else "${correct * 100 / total}%", "$correct corretas", Color(0xFF087F5B), Modifier.weight(1f))
            }
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                MetricCard("Sequência", "${streak.current}", "dias • melhor ${streak.best}", modifier = Modifier.weight(1f))
                MetricCard("Tempo", "${questionSessions.filter { it.completedAt >= since }.sumOf { it.durationSeconds } / 60} min", "em questões", modifier = Modifier.weight(1f))
            }
        }
        item { MetricCard("Revisões", reviews.count { it.completedAt != null && it.completedAt >= since }.toString(), "concluídas no período", modifier = Modifier.fillMaxWidth()) }
        item {
            Text("Atividade — últimos 28 dias", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            FlowRow(horizontalArrangement = Arrangement.spacedBy(5.dp), verticalArrangement = Arrangement.spacedBy(5.dp), maxItemsInEachRow = 7) {
                (27 downTo 0).forEach { offset ->
                    val day = LocalDate.now().minusDays(offset.toLong())
                    val count = attempts.count { StreakCalculator.day(it.answeredAt) == day } + history.count { StreakCalculator.day(it.reviewedAt) == day } + studySessions.count { StreakCalculator.day(it.completedAt) == day }
                    Box(Modifier.size(30.dp).background(when { count == 0 -> MaterialTheme.colorScheme.surfaceVariant; count < 3 -> MaterialTheme.colorScheme.primaryContainer; else -> MaterialTheme.colorScheme.primary }, RoundedCornerShape(6.dp)), contentAlignment = Alignment.Center) { Text(day.dayOfMonth.toString(), style = MaterialTheme.typography.labelSmall, color = if (count >= 3) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant) }
                }
            }
        }
        item { Text("Por matéria", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold) }
        items(subjects, key = { it.id }) { subject ->
            val topicIds = topics.filter { it.subjectId == subject.id }.map { it.id }.toSet()
            val qs = questions.filter { it.question.topicId in topicIds }
            val ids = qs.map { it.question.id }.toSet()
            val subjectAttempts = periodAttempts.filter { it.questionId in ids }
            val answered = subjectAttempts.size
            val hits = subjectAttempts.count { it.correct }
            if (qs.isNotEmpty()) ElevatedCard {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(7.dp)) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) { Text(subject.name, fontWeight = FontWeight.Bold); Text(if (answered == 0) "—" else "${hits * 100 / answered}%") }
                    LinearProgressIndicator({ if (answered == 0) 0f else hits.toFloat() / answered }, Modifier.fillMaxWidth())
                    Text("$answered respostas • ${answered - hits} erros", style = MaterialTheme.typography.bodySmall)
                    qs.sortedBy { item -> if (item.question.answerCount == 0) 101 else item.question.correctCount * 100 / item.question.answerCount }.take(3).forEach { q ->
                        val topic = topics.firstOrNull { it.id == q.question.topicId }
                        val rate = if (q.question.answerCount == 0) 0 else q.question.correctCount * 100 / q.question.answerCount
                        Text("${topic?.title}: $rate%${if (q.question.answerCount > 0 && rate < 70) "  ⚠" else ""}", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}
