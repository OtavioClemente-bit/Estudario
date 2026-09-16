package br.com.meuconcurso.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.material.icons.outlined.Bolt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.meuconcurso.data.local.TopicStatus
import br.com.meuconcurso.data.local.ErrorStatus
import br.com.meuconcurso.domain.*
import br.com.meuconcurso.ui.AppViewModel
import br.com.meuconcurso.ui.components.*
import java.time.LocalDate
import java.time.ZoneId

@Composable
fun HomeScreen(viewModel: AppViewModel, onQuiz: (Int, String) -> Unit, onTopic: (Long) -> Unit, onReviews: () -> Unit) {
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
    val competition = competitions.firstOrNull { it.isPrimary } ?: competitions.firstOrNull()
    val subjectIds = subjects.filter { it.competitionId == competition?.id }.map { it.id }.toSet()
    val competitionTopics = topics.filter { it.subjectId in subjectIds }
    val coverage = if (competitionTopics.isEmpty()) 0 else competitionTopics.count { it.status != TopicStatus.NAO_ESTUDADO } * 100 / competitionTopics.size
    val now = System.currentTimeMillis()
    val topicMastery = competitionTopics.associate { topic ->
        val qs = questions.filter { it.question.topicId == topic.id }
        val ids = qs.map { it.question.id }.toSet()
        val recent = attempts.filter { it.questionId in ids }.sortedByDescending { it.answeredAt }.take(20)
        val topicErrors = errors.filter { it.entry.questionId in ids }
        topic.id to MasteryCalculator.percent(MasteryInput(
            topic.status, qs.sumOf { it.question.answerCount }, qs.sumOf { it.question.correctCount }, recent.count { !it.correct },
            reviews.count { it.topicId == topic.id && it.completedAt != null }, recent.size, recent.count { it.correct },
            topicErrors.count { it.entry.status == ErrorStatus.RECORRENTE }, reviews.count { it.topicId == topic.id && ReviewPolicy.status(it, now) == ComputedReviewStatus.ATRASADA },
        ))
    }
    val mastery = topicMastery.values.takeIf { it.isNotEmpty() }?.average()?.toInt() ?: 0
    val weekAgo = System.currentTimeMillis() - 7 * 86_400_000L
    val weekAttempts = attempts.filter { it.answeredAt >= weekAgo }
    val accuracy = if (weekAttempts.isEmpty()) 0 else weekAttempts.count { it.correct } * 100 / weekAttempts.size
    val todayEnd = LocalDate.now().plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
    val pendingReviews = reviews.count { ReviewPolicy.status(it, now) in setOf(ComputedReviewStatus.DISPONIVEL, ComputedReviewStatus.ATRASADA) }
    val next = queue.firstOrNull { !it.item.paused }
    val activityDays = (attempts.map { StreakCalculator.day(it.answeredAt) } + history.map { StreakCalculator.day(it.reviewedAt) } + studySessions.map { StreakCalculator.day(it.completedAt) }).toSet()
    val streak = StreakCalculator.calculate(activityDays)
    val weakTopic = competitionTopics.filter { it.status != TopicStatus.NAO_ESTUDADO }.minByOrNull { topicMastery[it.id] ?: 0 }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            ScreenTitle("Meu Concurso", competition?.name ?: "Comece criando seu concurso")
        }
        if (competition == null) {
            item { EmptyState("Seu espaço de estudo está vazio", "Carregue os dados demonstrativos em Mais ou crie um concurso na área Edital.") }
        } else {
            item {
                ElevatedCard(colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
                    Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Cobertura do edital", fontWeight = FontWeight.SemiBold)
                            Text("$coverage%", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        }
                        LinearProgressIndicator(progress = { coverage / 100f }, Modifier.fillMaxWidth())
                        Text("${competitionTopics.count { it.status != TopicStatus.NAO_ESTUDADO }} de ${competitionTopics.size} tópicos iniciados", style = MaterialTheme.typography.bodySmall)
                        HorizontalDivider()
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) { Text("Domínio estimado", fontWeight = FontWeight.SemiBold); Text("$mastery%", fontWeight = FontWeight.Bold) }
                        LinearProgressIndicator(progress = { mastery / 100f }, Modifier.fillMaxWidth(), color = MaterialTheme.colorScheme.tertiary)
                        Text("Baseado em teoria, amostra de questões, desempenho recente, erros e revisões.", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
            next?.let { queueItem ->
                item {
                    ElevatedCard(onClick = { onTopic(queueItem.topic.id) }) {
                        Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text("PRÓXIMO ESTUDO", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
                            Text(queueItem.topic.title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                            Text("O item permanece aqui até você concluir o bloco.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Button(onClick = { onTopic(queueItem.topic.id) }) { Text("Abrir"); Spacer(Modifier.width(8.dp)); Icon(Icons.Outlined.ArrowForward, null) }
                        }
                    }
                }
            }
            item {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    MetricCard("Revisões", pendingReviews.toString(), "pendentes hoje", modifier = Modifier.weight(1f))
                    MetricCard("Sequência", "${streak.current} dia${if (streak.current == 1) "" else "s"}", "melhor: ${streak.best}", Color(0xFF087F5B), Modifier.weight(1f))
                }
            }
            item {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    MetricCard("Caderno de erros", errors.count { it.entry.pending }.toString(), "itens pendentes", MaterialTheme.colorScheme.error, Modifier.weight(1f))
                    MetricCard("Realizadas", questions.sumOf { it.question.answerCount }.toString(), "respostas salvas", modifier = Modifier.weight(1f))
                }
            }
            if (pendingReviews > 0) item { OutlinedButton(onClick = onReviews, Modifier.fillMaxWidth()) { Text("Ver revisões de hoje") } }
            weakTopic?.let { topic -> item { ElevatedCard(onClick = { onTopic(topic.id) }) { Column(Modifier.padding(16.dp)) { Text("FOCO RECOMENDADO", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary); Text(topic.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold); Text("Domínio estimado em ${topicMastery[topic.id]}%. Retome a teoria ou faça questões.") } } } }
            item {
                Text("Desafio diário", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                Button(onClick = { onQuiz(10, "daily") }, Modifier.fillMaxWidth(), enabled = questions.isNotEmpty()) { Icon(Icons.Outlined.Bolt, null); Spacer(Modifier.width(6.dp)); Text("Começar 10 questões inteligentes") }
            }
        }
    }
}
