package br.com.estudario.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.TrendingDown
import androidx.compose.material.icons.automirrored.outlined.TrendingUp
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.EventAvailable
import androidx.compose.material.icons.outlined.LocalFireDepartment
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.Quiz
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.ThumbDown
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.FilterChip
import br.com.estudario.ui.brand.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import br.com.estudario.data.local.TopicStatus
import br.com.estudario.domain.performance.DailyActivityPoint
import br.com.estudario.domain.performance.StudyPerformancePeriod
import br.com.estudario.domain.performance.StudyPerformanceResult
import br.com.estudario.domain.performance.TopicPerformance
import br.com.estudario.ui.AppViewModel
import br.com.estudario.ui.screens.performance.StudyPerformanceUiState
import java.time.format.TextStyle
import java.util.Locale

// Escala de acerto usada em toda a tela: vermelho abaixo de 50%, âmbar até 70%, verde a partir daí.
private val Weak = Color(0xFFE5484D)
private val Medium = Color(0xFFF2A900)
private val Strong = Color(0xFF2FA36B)
private fun accuracyTone(percent: Int?): Color = when {
    percent == null -> Color(0xFF9AA0A6)
    percent < 50 -> Weak
    percent < 70 -> Medium
    else -> Strong
}

/**
 * Desempenho: a pessoa entende como está sem precisar ler. Um anel com o acerto e a tendência,
 * o ritmo dia a dia em barras, o edital em rosca, as matérias em ranking colorido e os pontos
 * fortes e fracos lado a lado. Os textos só confirmam o que o desenho já mostra.
 */
@Composable
fun StatisticsScreen(viewModel: AppViewModel, onBack: () -> Unit, showInlineBack: Boolean = true) {
    val state by viewModel.studyPerformance.collectAsState()
    val competitions by viewModel.competitions.collectAsState()
    val subjects by viewModel.subjects.collectAsState()
    val topics by viewModel.topics.collectAsState()
    val theories by viewModel.theories.collectAsState()
    val result = state.result
    val primary = competitions.firstOrNull { it.isPrimary } ?: competitions.firstOrNull()
    val primarySubjects = subjects.filter { it.competitionId == primary?.id }.map { it.id }.toSet()
    val editalTopics = topics.filter { it.subjectId in primarySubjects }.let { all ->
        val parents = all.mapNotNull { it.parentTopicId }.toSet()
        all.filter { it.id !in parents }
    }
    val withTheory = theories.map { it.topicId }.toSet()
    val studied = editalTopics.count { it.status != TopicStatus.NAO_ESTUDADO }
    val withMaterial = editalTopics.count { it.status == TopicStatus.NAO_ESTUDADO && it.id in withTheory }
    val empty = result.current.attempts == 0 && result.current.studySessions == 0 && result.current.questionSessions == 0 &&
        result.current.reviews == 0 && result.current.plannedTasks == 0

    LazyColumn(
        modifier = Modifier.fillMaxSize().testTag("performance-list"),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (showInlineBack) IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Voltar") }
                Column(Modifier.weight(1f)) {
                    Text("Desempenho", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                    Text("Como você está, num relance", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
        item { PeriodSelector(state, viewModel::selectStudyPerformancePeriod) }
        item { HeroCard(result) }
        item { KpiRow(result) }
        item { RhythmCard(result.dailyActivity, result.dailyActivityLabel) }
        if (editalTopics.isNotEmpty()) item { EditalCard(primary?.name.orEmpty(), editalTopics.size, studied, withMaterial) }
        if (result.subjects.isNotEmpty()) item { SubjectsCard(result) }
        val allTopics = result.subjects.flatMap { it.topics }.filter { it.hasSufficientSample }
        if (allTopics.size >= 2) item { StrengthsCard(allTopics) }
        if (result.current.plannedTasks > 0) item { PlanCard(result) }
        if (result.recommendations.isNotEmpty()) item {
            Panel {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Outlined.Lightbulb, null, tint = MaterialTheme.colorScheme.primary)
                    Spacer(Modifier.width(8.dp))
                    Text("Próximos ajustes", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                }
                result.recommendations.forEach { recommendation ->
                    Surface(shape = RoundedCornerShape(14.dp), color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = .6f), modifier = Modifier.fillMaxWidth()) {
                        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(recommendation.message, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                            Text(recommendation.evidence, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }
        }
        if (empty) item {
            Panel {
                Text("Ainda não há atividade neste período", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                Text("Responda questões, estude com o modo foco ou conclua uma revisão: os gráficos se enchem sozinhos.", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        item { Spacer(Modifier.height(8.dp)) }
    }
}

@Composable
private fun PeriodSelector(state: StudyPerformanceUiState, onPeriodChange: (StudyPerformancePeriod) -> Unit) {
    Row(Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        StudyPerformancePeriod.entries.forEach { period ->
            val tag = if (period == StudyPerformancePeriod.ALL) "all" else period.days.toString()
            FilterChip(
                selected = state.selectedPeriod == period,
                onClick = { onPeriodChange(period) },
                label = { Text(period.label, maxLines = 1) },
                modifier = Modifier.testTag("performance-period-$tag"),
            )
        }
    }
}

@Composable
private fun Panel(modifier: Modifier = Modifier, content: @Composable ColumnScope.() -> Unit) {
    Surface(shape = RoundedCornerShape(24.dp), color = MaterialTheme.colorScheme.surfaceContainerLow, modifier = modifier.fillMaxWidth()) {
        Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp), content = content)
    }
}

@Composable
private fun PanelTitle(title: String, subtitle: String? = null) {
    Column {
        Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        subtitle?.let { Text(it, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant) }
    }
}

// ---------------------------------------------------------------------------------- anel de acerto

@Composable
private fun HeroCard(result: StudyPerformanceResult) {
    val accuracy = result.current.accuracyPercent
    val previous = result.previous?.accuracyPercent
    val delta = if (accuracy != null && previous != null) accuracy - previous else null
    val tone = accuracyTone(accuracy)
    val progress = remember { Animatable(0f) }
    LaunchedEffect(accuracy) { progress.snapTo(0f); progress.animateTo((accuracy ?: 0) / 100f, tween(900, easing = FastOutSlowInEasing)) }
    Surface(shape = RoundedCornerShape(28.dp), color = MaterialTheme.colorScheme.primaryContainer, modifier = Modifier.fillMaxWidth()) {
        Row(Modifier.padding(20.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(132.dp), contentAlignment = Alignment.Center) {
                val track = MaterialTheme.colorScheme.surface.copy(alpha = .7f)
                Canvas(Modifier.fillMaxSize()) {
                    val stroke = 16.dp.toPx()
                    val inset = stroke / 2
                    val arc = Size(size.width - stroke, size.height - stroke)
                    drawArc(track, -90f, 360f, false, Offset(inset, inset), arc, style = Stroke(stroke, cap = StrokeCap.Round))
                    if (progress.value > 0f) drawArc(tone, -90f, 360f * progress.value, false, Offset(inset, inset), arc, style = Stroke(stroke, cap = StrokeCap.Round))
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(accuracy?.let { "$it%" } ?: "--", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.onPrimaryContainer)
                    Text("acerto", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = .75f))
                }
            }
            Spacer(Modifier.width(18.dp))
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    when {
                        accuracy == null -> "Responda questões para medir seu acerto"
                        accuracy >= 70 -> "Nível de aprovação"
                        accuracy >= 50 -> "No caminho certo"
                        else -> "Hora de reforçar a base"
                    },
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                )
                if (delta != null) TrendChip(delta)
                Text("${result.current.correctAttempts} certas de ${result.current.attempts}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = .8f))
            }
        }
    }
}

@Composable
private fun TrendChip(delta: Int) {
    val color = when { delta > 0 -> Strong; delta < 0 -> Weak; else -> Color(0xFF9AA0A6) }
    Surface(shape = RoundedCornerShape(50), color = color.copy(alpha = .16f)) {
        Row(Modifier.padding(horizontal = 10.dp, vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(if (delta >= 0) Icons.AutoMirrored.Outlined.TrendingUp else Icons.AutoMirrored.Outlined.TrendingDown, null, Modifier.size(16.dp), tint = color)
            Spacer(Modifier.width(4.dp))
            Text(
                when { delta > 0 -> "+$delta pontos"; delta < 0 -> "$delta pontos"; else -> "estável" } + " vs. antes",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = color,
            )
        }
    }
}

// ---------------------------------------------------------------------------------- números-chave

@Composable
private fun KpiRow(result: StudyPerformanceResult) {
    val minutes = result.current.studyMinutes + result.current.questionMinutes + result.current.planExecutionMinutes
    val days = result.period.days
    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        Kpi(Icons.Outlined.Schedule, formatHours(minutes), "estudadas", Color(0xFF5B5BD6), Modifier.weight(1f))
        Kpi(Icons.Outlined.Quiz, "${result.current.attempts}", "questões", Color(0xFF0E9AA7), Modifier.weight(1f))
        Kpi(Icons.Outlined.CalendarMonth, if (days != null) "${result.current.activeDays}/$days" else "${result.current.activeDays}", "dias ativos", Color(0xFF8E4EC6), Modifier.weight(1f))
        Kpi(Icons.Outlined.LocalFireDepartment, "${result.streakThroughToday}", "sequência", Color(0xFFE8590C), Modifier.weight(1f))
    }
}

@Composable
private fun Kpi(icon: ImageVector, value: String, label: String, color: Color, modifier: Modifier) {
    Surface(shape = RoundedCornerShape(18.dp), color = color.copy(alpha = .1f), modifier = modifier) {
        Column(Modifier.padding(vertical = 12.dp, horizontal = 8.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Icon(icon, null, Modifier.size(20.dp), tint = color)
            Text(value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black, maxLines = 1)
            Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
    }
}

// ---------------------------------------------------------------------------------- ritmo diário

/** Barras = minutos de estudo por dia; pontos sobre a barra = acerto do dia, na cor da escala. */
@Composable
private fun RhythmCard(points: List<DailyActivityPoint>, label: String) {
    val days = points.takeLast(if (points.size > 31) 31 else points.size)
    val maxMinutes = (days.maxOfOrNull { it.minutes } ?: 0).coerceAtLeast(30)
    val active = days.count { it.eventCount > 0 }
    Panel {
        PanelTitle("Seu ritmo", "$label · $active de ${days.size} dias com estudo")
        val barColor = MaterialTheme.colorScheme.primary
        val idle = MaterialTheme.colorScheme.surfaceVariant
        val grid = MaterialTheme.colorScheme.outlineVariant.copy(alpha = .5f)
        Canvas(Modifier.fillMaxWidth().height(150.dp)) {
            val slot = size.width / days.size.coerceAtLeast(1)
            val barWidth = (slot * .62f).coerceAtMost(28.dp.toPx())
            val chartHeight = size.height - 10.dp.toPx()
            for (fraction in listOf(0.5f, 1f)) {
                val y = chartHeight * (1 - fraction)
                drawLine(grid, Offset(0f, y), Offset(size.width, y), strokeWidth = 1f)
            }
            days.forEachIndexed { index, day ->
                val x = slot * index + (slot - barWidth) / 2
                val height = if (day.minutes > 0) (day.minutes.toFloat() / maxMinutes * chartHeight).coerceAtLeast(6.dp.toPx())
                else if (day.eventCount > 0) 6.dp.toPx() else 3.dp.toPx()
                drawRoundRect(
                    if (day.eventCount > 0) barColor else idle,
                    Offset(x, chartHeight - height),
                    Size(barWidth, height),
                    CornerRadius(barWidth / 2, barWidth / 2),
                )
                day.accuracyPercent?.let { percent ->
                    val cy = chartHeight - height - 7.dp.toPx()
                    drawCircle(Color.White, 5.dp.toPx(), Offset(x + barWidth / 2, cy))
                    drawCircle(accuracyTone(percent), 3.5.dp.toPx(), Offset(x + barWidth / 2, cy))
                }
            }
        }
        // Rótulos: dia da semana quando cabe; em períodos longos, só o começo e o fim.
        if (days.size <= 14) Row(Modifier.fillMaxWidth()) {
            days.forEach { day ->
                Text(
                    day.date.dayOfWeek.getDisplayName(TextStyle.NARROW, Locale("pt", "BR")).uppercase(),
                    Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        } else Row(Modifier.fillMaxWidth()) {
            Text(shortDate(days.first()), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.weight(1f))
            Text("hoje", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Row(horizontalArrangement = Arrangement.spacedBy(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Legend(barColor, "minutos de estudo")
            Legend(Strong, "acerto do dia", round = true)
        }
    }
}

@Composable
private fun Legend(color: Color, label: String, round: Boolean = false) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(Modifier.size(10.dp).background(color, if (round) CircleShape else RoundedCornerShape(3.dp)))
        Spacer(Modifier.width(6.dp))
        Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

// ---------------------------------------------------------------------------------- edital

/** Rosca do edital: estudado, com material à espera e o que ainda falta gerar. */
@Composable
private fun EditalCard(name: String, total: Int, studied: Int, withMaterial: Int) {
    val remaining = (total - studied - withMaterial).coerceAtLeast(0)
    val studiedColor = Strong
    val materialColor = MaterialTheme.colorScheme.primary
    val remainingColor = MaterialTheme.colorScheme.surfaceVariant
    val progress = remember { Animatable(0f) }
    LaunchedEffect(total, studied, withMaterial) { progress.snapTo(0f); progress.animateTo(1f, tween(900, easing = FastOutSlowInEasing)) }
    Panel {
        PanelTitle("Seu edital", name)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(120.dp), contentAlignment = Alignment.Center) {
                Canvas(Modifier.fillMaxSize()) {
                    val stroke = 18.dp.toPx()
                    val inset = stroke / 2
                    val arc = Size(size.width - stroke, size.height - stroke)
                    var start = -90f
                    listOf(studied to studiedColor, withMaterial to materialColor, remaining to remainingColor).forEach { (count, color) ->
                        if (count == 0) return@forEach
                        val sweep = 360f * count / total * progress.value
                        drawArc(color, start, (sweep - 2f).coerceAtLeast(0.5f), false, Offset(inset, inset), arc, style = Stroke(stroke, cap = StrokeCap.Butt))
                        start += sweep
                    }
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("${studied * 100 / total.coerceAtLeast(1)}%", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black)
                    Text("estudado", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            Spacer(Modifier.width(18.dp))
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                DonutLegend(studiedColor, "$studied", "estudados")
                DonutLegend(materialColor, "$withMaterial", "com material, a estudar")
                DonutLegend(remainingColor, "$remaining", "sem material ainda")
            }
        }
    }
}

@Composable
private fun DonutLegend(color: Color, value: String, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(Modifier.size(12.dp).background(color, RoundedCornerShape(4.dp)))
        Spacer(Modifier.width(8.dp))
        Text(value, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
        Spacer(Modifier.width(6.dp))
        Text(label, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

// ---------------------------------------------------------------------------------- matérias

@Composable
private fun SubjectsCard(result: StudyPerformanceResult) {
    val ordered = result.subjects.sortedByDescending { it.accuracyPercent }
    Panel {
        PanelTitle("Acerto por matéria", "Da mais forte para a mais fraca")
        ordered.forEach { subject ->
            val tone = accuracyTone(if (subject.hasSufficientSample) subject.accuracyPercent else null)
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(subject.name, Modifier.weight(1f), style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    Text("${subject.accuracyPercent}%", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Black, color = tone)
                }
                Box(Modifier.fillMaxWidth().height(10.dp).background(MaterialTheme.colorScheme.surfaceVariant, CircleShape)) {
                    Box(Modifier.fillMaxWidth((subject.accuracyPercent / 100f).coerceIn(0.03f, 1f)).height(10.dp).background(tone, CircleShape))
                }
                Text(
                    if (subject.hasSufficientSample) "${subject.correct} de ${subject.attempts} certas" else "Poucos dados · ${subject.attempts} resposta(s)",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Legend(Strong, "70% ou mais", round = true)
            Legend(Medium, "50 a 69%", round = true)
            Legend(Weak, "abaixo de 50%", round = true)
        }
    }
}

// ---------------------------------------------------------------------------------- fortes x fracos

@Composable
private fun StrengthsCard(topics: List<TopicPerformance>) {
    val sorted = topics.sortedByDescending { it.accuracyPercent }
    val strong = sorted.take(3)
    val weak = sorted.takeLast(3).reversed().filterNot { it in strong }
    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        TopicColumn("Pontos fortes", Icons.Outlined.ThumbUp, Strong, strong, Modifier.weight(1f))
        TopicColumn("Para reforçar", Icons.Outlined.ThumbDown, Weak, weak, Modifier.weight(1f))
    }
}

@Composable
private fun TopicColumn(title: String, icon: ImageVector, color: Color, topics: List<TopicPerformance>, modifier: Modifier) {
    Surface(shape = RoundedCornerShape(22.dp), color = color.copy(alpha = .08f), modifier = modifier) {
        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, null, Modifier.size(18.dp), tint = color)
                Spacer(Modifier.width(6.dp))
                Text(title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = color)
            }
            if (topics.isEmpty()) Text("Sem dados suficientes", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            topics.forEach { topic ->
                Column {
                    Text(topic.name, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Medium, maxLines = 2, overflow = TextOverflow.Ellipsis)
                    Text("${topic.accuracyPercent}%", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black, color = color)
                }
            }
        }
    }
}

// ---------------------------------------------------------------------------------- plano

@Composable
private fun PlanCard(result: StudyPerformanceResult) {
    val done = result.current.completedTasks
    val total = result.current.plannedTasks
    val missed = result.current.missedTasks
    val percent = result.current.taskCompletionPercent ?: 0
    Panel {
        PanelTitle("Plano de estudos", "Tarefas do período")
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Outlined.EventAvailable, null, Modifier.size(28.dp), tint = accuracyTone(percent))
            Spacer(Modifier.width(10.dp))
            Text("$percent%", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Black)
            Spacer(Modifier.width(8.dp))
            Text("cumprido", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        // Barra segmentada: feito, não feito e o que ainda vem.
        val pending = (total - done - missed).coerceAtLeast(0)
        Row(Modifier.fillMaxWidth().height(12.dp)) {
            if (done > 0) Box(Modifier.weight(done.toFloat()).height(12.dp).background(Strong, RoundedCornerShape(topStart = 6.dp, bottomStart = 6.dp)))
            if (missed > 0) Box(Modifier.weight(missed.toFloat()).height(12.dp).background(Weak))
            if (pending > 0) Box(Modifier.weight(pending.toFloat()).height(12.dp).background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(topEnd = 6.dp, bottomEnd = 6.dp)))
        }
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Legend(Strong, "$done feitas")
            Legend(Weak, "$missed perdidas")
            Legend(MaterialTheme.colorScheme.surfaceVariant, "$pending a fazer")
        }
    }
}

// ---------------------------------------------------------------------------------- formatação

private fun formatHours(minutes: Long): String = when {
    minutes < 60 -> "${minutes}min"
    minutes % 60 == 0L -> "${minutes / 60}h"
    else -> "${minutes / 60}h${(minutes % 60).toString().padStart(2, '0')}"
}

private fun shortDate(point: DailyActivityPoint): String = "%02d/%02d".format(point.date.dayOfMonth, point.date.monthValue)

