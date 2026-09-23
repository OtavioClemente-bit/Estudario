package br.com.estudario.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Insights
import androidx.compose.material.icons.outlined.School
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import br.com.estudario.domain.performance.DailyActivityPoint
import br.com.estudario.domain.performance.StudyPerformancePeriod
import br.com.estudario.domain.performance.StudyPerformanceResult
import br.com.estudario.ui.AppViewModel
import br.com.estudario.ui.screens.performance.StudyPerformanceUiState
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun StatisticsScreen(viewModel: AppViewModel, onBack: () -> Unit, showInlineBack: Boolean = true) {
    val state by viewModel.studyPerformance.collectAsState()
    val result = state.result

    LazyColumn(
        modifier = Modifier.fillMaxSize().testTag("performance-list"),
        contentPadding = PaddingValues(horizontal = 18.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (showInlineBack) IconButton(onClick = onBack) { Icon(Icons.Outlined.ArrowBack, contentDescription = "Voltar") }
                Column(Modifier.weight(1f)) {
                    Text("Desempenho", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                    Text("Uma leitura do que você realmente fez", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
        item { PeriodSelector(state, viewModel::selectStudyPerformancePeriod) }
        item { LearningSummary(result) }
        item { ConsistencySummary(result) }
        item { DailyActivity(result) }
        item { QuestionEvidence(result) }
        item { PlanEvidence(result) }
        item { TimeEvidence(result) }
        item { ReviewEvidence(result) }
        item { SubjectEvidence(result) }
        if (result.recommendations.isNotEmpty()) {
            item { SectionHeading("Próximos ajustes", "Sugestões acionadas pelos dados deste período") }
            items(result.recommendations) { recommendation ->
                ElevatedCard(colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)) {
                    Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(recommendation.message, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                        Text(recommendation.evidence, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSecondaryContainer)
                    }
                }
            }
        }
        if (result.current.attempts == 0 && result.current.studySessions == 0 && result.current.questionSessions == 0 && result.current.reviews == 0 && result.current.plannedTasks == 0) {
            item {
                ElevatedCard {
                    Column(Modifier.fillMaxWidth().padding(18.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Ainda não há atividade registrada neste período", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                        Text("Responder questões registra acertos e erros. Estudar pelo foco registra sessões; concluir uma revisão ou tarefa também aparece aqui.", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }
        item { Spacer(Modifier.height(8.dp)) }
    }
}

@Composable
private fun PeriodSelector(state: StudyPerformanceUiState, onPeriodChange: (StudyPerformancePeriod) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Período", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Row(Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            StudyPerformancePeriod.entries.forEach { period ->
                val tag = if (period == StudyPerformancePeriod.ALL) "all" else period.days.toString()
                FilterChip(
                    selected = state.selectedPeriod == period,
                    onClick = { onPeriodChange(period) },
                    label = { Text(period.label, maxLines = 1, overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis) },
                    modifier = Modifier.testTag("performance-period-$tag"),
                )
            }
        }
    }
}

@Composable
private fun LearningSummary(result: StudyPerformanceResult) {
    val metrics = result.current
    ElevatedCard(colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
        Column(Modifier.fillMaxWidth().padding(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text("Seu resumo · ${result.period.label}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                SummaryMetric("Respostas", metrics.attempts.toString(), Modifier.weight(1f))
                SummaryMetric("Acerto", metrics.accuracyPercent?.let { "$it%" } ?: ",", Modifier.weight(1f))
                SummaryMetric("Dias ativos", metrics.activeDays.toString(), Modifier.weight(1f))
            }
            Text(
                result.previous?.let { previous ->
                    val old = previous.accuracyPercent
                    val current = metrics.accuracyPercent
                    if (old != null && current != null) "Período anterior: $old% · variação de ${formatPointChange(current - old)}"
                    else "Período anterior: sem respostas suficientes para comparar"
                } ?: "Sem comparação anterior no resumo geral",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
            )
            Text("A sequência atual é ${result.streakThroughToday} ${if (result.streakThroughToday == 1) "dia" else "dias"} até hoje; ela não muda com o filtro.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onPrimaryContainer)
        }
    }
}

@Composable
private fun SummaryMetric(label: String, value: String, modifier: Modifier = Modifier) {
    Column(modifier, verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Text(value, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, maxLines = 1, overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis)
        Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onPrimaryContainer, maxLines = 2, overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis)
    }
}

@Composable
private fun ConsistencySummary(result: StudyPerformanceResult) {
    SectionHeading("Consistência", "Dias com atividade registrada no período")
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        MetricTile("Sessões de estudo", result.current.studySessions.toString(), modifier = Modifier.weight(1f))
        MetricTile("Sessões de questões", result.current.questionSessions.toString(), modifier = Modifier.weight(1f))
    }
}

@Composable
private fun DailyActivity(result: StudyPerformanceResult) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        SectionHeading("Atividade diária", result.dailyActivityLabel)
        ElevatedCard {
            Column(Modifier.fillMaxWidth().padding(vertical = 12.dp, horizontal = 8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(5.dp), contentPadding = PaddingValues(horizontal = 4.dp)) {
                    items(result.dailyActivity, key = { it.date.toEpochDay() }) { point -> ActivityDay(point) }
                }
                Text("Cada marca indica quantos eventos foram registrados naquele dia. Deslize para consultar as datas.", Modifier.padding(horizontal = 6.dp), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@Composable
private fun ActivityDay(point: DailyActivityPoint) {
    val dateLabel = point.date.format(DateTimeFormatter.ofPattern("dd/MM", Locale("pt", "BR")))
    Column(
        Modifier.width(28.dp).semantics { contentDescription = "$dateLabel: ${point.eventCount} ${if (point.eventCount == 1) "atividade" else "atividades"}" },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Box(
            Modifier.size(18.dp).background(
                color = when {
                    point.eventCount == 0 -> MaterialTheme.colorScheme.surfaceVariant
                    point.eventCount < 3 -> MaterialTheme.colorScheme.primaryContainer
                    else -> MaterialTheme.colorScheme.primary
                },
                shape = RoundedCornerShape(5.dp),
            ),
        )
        Text(point.date.dayOfMonth.toString(), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun QuestionEvidence(result: StudyPerformanceResult) {
    val current = result.current
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        SectionHeading("Questões", "Acurácia baseada em tentativas respondidas")
        ElevatedCard {
            Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("${current.correctAttempts} de ${current.attempts} respostas corretas", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                if (current.accuracyPercent != null) {
                    LinearProgressIndicator(progress = { current.accuracyPercent / 100f }, modifier = Modifier.fillMaxWidth())
                    Text("${current.accuracyPercent}% de acerto", style = MaterialTheme.typography.bodyMedium)
                } else {
                    Text("Sem respostas para calcular um percentual neste período.", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}

@Composable
private fun PlanEvidence(result: StudyPerformanceResult) {
    val current = result.current
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        SectionHeading("Plano", "Tarefas agendadas em planos ativos e não arquivados")
        EvidenceCard(Icons.Outlined.School) {
            Text(
                if (current.plannedTasks == 0) "Nenhuma tarefa elegível no período"
                else "${current.completedTasks} de ${current.plannedTasks} tarefas concluídas",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
            )
            Text(current.taskCompletionPercent?.let { "$it% de conclusão" } ?: "Sem tarefas no denominador; a aderência não é 100% por padrão.", color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text("Não realizadas: ${current.missedTasks} · em andamento: ${current.inProgressTasks}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text("Previsto: ${formatMinutes(current.plannedMinutes)} · Execuções registradas: ${formatMinutes(current.planExecutionMinutes)}", color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text("Tarefas pausadas e substituídas por replanejamento ficam fora; tarefas não realizadas contam como não concluídas.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun TimeEvidence(result: StudyPerformanceResult) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        SectionHeading("Tempo registrado", "Minutos em fontes separadas para não somar sessões sobrepostas")
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            MetricTile("Estudo", formatMinutes(result.current.studyMinutes), Icons.Outlined.Schedule, Modifier.weight(1f))
            MetricTile("Questões", formatMinutes(result.current.questionMinutes), Icons.Outlined.Insights, Modifier.weight(1f))
        }
        Text("O tempo de execução do plano aparece separado na seção Plano.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun ReviewEvidence(result: StudyPerformanceResult) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        SectionHeading("Revisões concluídas", "Quantidade registrada no histórico; não mede qualidade da lembrança")
        MetricTile("Revisões no período", result.current.reviews.toString(), modifier = Modifier.fillMaxWidth())
    }
}

@Composable
private fun SubjectEvidence(result: StudyPerformanceResult) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        SectionHeading("Por matéria e tópico", "Cada resultado mostra seu tamanho de amostra")
        if (result.subjects.isEmpty()) {
            ElevatedCard { Text("As respostas ainda não estão vinculadas a matérias neste período.", Modifier.padding(16.dp), color = MaterialTheme.colorScheme.onSurfaceVariant) }
        } else {
            result.subjects.forEach { subject ->
                ElevatedCard {
                    Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Text(subject.name, Modifier.weight(1f), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                            Text("${subject.accuracyPercent}%", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        }
                        LinearProgressIndicator(progress = { subject.accuracyPercent / 100f }, modifier = Modifier.fillMaxWidth())
                        Text(sampleLabel(subject.hasSufficientSample, subject.attempts), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        subject.topics.forEach { topic ->
                            Column(Modifier.fillMaxWidth().padding(start = 10.dp), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                                Text(topic.name, style = MaterialTheme.typography.bodyMedium)
                                Text("${topic.accuracyPercent}% · ${sampleLabel(topic.hasSufficientSample, topic.attempts)}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                }
            }
        }
        Text("Diagnósticos por matéria e tópico só são acionados com pelo menos 10 respostas.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun SectionHeading(title: String, subtitle: String) {
    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
        Text(subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun MetricTile(label: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector? = null, modifier: Modifier = Modifier) {
    ElevatedCard(modifier) {
        Column(Modifier.fillMaxWidth().padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            if (icon != null) Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Text(value, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Text(label, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun EvidenceCard(icon: androidx.compose.ui.graphics.vector.ImageVector, content: @Composable ColumnScope.() -> Unit) {
    ElevatedCard {
        Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            content(this)
        }
    }
}

private fun sampleLabel(sufficient: Boolean, count: Int): String =
    if (sufficient) "$count respostas" else "Poucos dados · $count ${if (count == 1) "resposta" else "respostas"}"

private fun formatMinutes(minutes: Long): String {
    val hours = minutes / 60
    val remaining = minutes % 60
    return when {
        hours == 0L -> "$remaining min"
        remaining == 0L -> "$hours h"
        else -> "$hours h $remaining min"
    }
}

private fun formatPointChange(points: Int): String = when {
    points > 0 -> "+$points p.p."
    points < 0 -> "$points p.p."
    else -> "0 p.p."
}
