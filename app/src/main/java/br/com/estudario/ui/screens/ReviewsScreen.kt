package br.com.estudario.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Replay
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import br.com.estudario.domain.ComputedReviewStatus
import br.com.estudario.domain.ReviewPolicy
import br.com.estudario.ui.AppViewModel
import br.com.estudario.ui.brand.Icon
import br.com.estudario.ui.brand.BrandIcon
import br.com.estudario.ui.brand.Glyph
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import br.com.estudario.ui.brand.marker
import br.com.estudario.ui.components.EmptyState
import br.com.estudario.ui.theme.screenPadding
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.TextStyle
import java.util.Locale

private val Gold = Color(0xFFF5B83D)
private val Mint = Color(0xFF2FBF8A)
private val Red = Color(0xFFE5484D)
private val InkA = Color(0xFF2E2799)
private val InkB = Color(0xFF1B1464)
/** Cinco etapas por vez, em volta da atual (antes, rótulos fixos D+1…D+60 que não batiam com os intervalos reais). */
private const val LADDER = 5

/**
 * As revisões espaçadas com a cara do Estudário, como no site: o cartão de tinta com o que há para
 * hoje (e o botão para começar), as revisões do dia com a escadinha das etapas e as próximas numa
 * linha do tempo por dia.
 */
@Composable
fun ReviewsScreen(
    viewModel: AppViewModel,
    onBack: () -> Unit,
    onStartReview: (Long) -> Unit,
    onOpenTopic: (Long) -> Unit,
    showInlineBack: Boolean = true,
) {
    val reviews by viewModel.reviews.collectAsState()
    val topics by viewModel.topics.collectAsState()
    val subjects by viewModel.subjects.collectAsState()
    val options by viewModel.reviewPractice.collectAsState()
    var settingsOpen by remember { mutableStateOf(false) }
    if (settingsOpen) options?.let { initial -> ReviewPracticeSettings(initial, onClose = { settingsOpen = false }, onSave = { viewModel.setReviewPractice(it); settingsOpen = false }) }
    val pending = reviews.filter { it.completedAt == null && it.ignoredAt == null }
    val due = pending.filter { ReviewPolicy.status(it) in setOf(ComputedReviewStatus.DISPONIVEL, ComputedReviewStatus.ATRASADA) }.sortedBy { it.dueAt }
    val late = due.count { ReviewPolicy.status(it) == ComputedReviewStatus.ATRASADA }
    val upcoming = (pending - due.toSet()).sortedBy { it.dueAt }
    val zone = ZoneId.systemDefault()
    val today = LocalDate.now(zone)
    fun dateOf(millis: Long) = Instant.ofEpochMilli(millis).atZone(zone).toLocalDate()
    val doneToday = reviews.count { it.completedAt != null && dateOf(it.completedAt!!) == today }

    LazyColumn(Modifier.fillMaxSize(), contentPadding = screenPadding(), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (showInlineBack) IconButton(onClick = onBack) { Icon(Icons.Outlined.ArrowBack, "Voltar") }
                Column(Modifier.weight(1f)) {
                    Text("Revisões", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Black, modifier = Modifier.padding(start = 3.dp).marker())
                    Text("Revisar na hora certa é o que faz o conteúdo ficar", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                IconButton(onClick = { settingsOpen = true }, enabled = options != null, modifier = Modifier.semantics { contentDescription = "Configurar revisão" }) { BrandIcon(Glyph.Gear) }
            }
        }
        item {
            Column(
                Modifier.fillMaxWidth().clip(RoundedCornerShape(26.dp)).background(Brush.linearGradient(listOf(InkA, InkB)))
                    .drawBehind {
                        val step = 26.dp.toPx(); var y = step
                        while (y < size.height) { drawLine(Color.White.copy(alpha = 0.05f), Offset(0f, y), Offset(size.width, y), 1.dp.toPx()); y += step }
                    }
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text("REVISÃO ESPAÇADA", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Black, color = Gold)
                        Text(
                            if (due.isEmpty()) "Tudo em dia por hoje" else "${due.size} ${if (due.size == 1) "revisão" else "revisões"} para hoje",
                            style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Black, color = Color.White,
                        )
                        Text(
                            when { due.isEmpty() -> "Mandou bem. Amanhã tem mais."; late > 0 -> "Comece pelas atrasadas: são as que mais escapam."; else -> "Poucos minutos cada. É o que segura a matéria até a prova." },
                            style = MaterialTheme.typography.bodySmall, color = Color.White.copy(alpha = 0.78f),
                        )
                    }
                    br.com.estudario.ui.assistant.Folha(72.dp, mood = when { due.isEmpty() -> br.com.estudario.ui.assistant.FolhaMood.PROUD; late > 0 -> br.com.estudario.ui.assistant.FolhaMood.TALKING; else -> br.com.estudario.ui.assistant.FolhaMood.HAPPY })
                }
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    HeroStat("${due.size}", "hoje", Gold, Modifier.weight(1f))
                    HeroStat("$late", "atrasadas", Color(0xFFFF8A80), Modifier.weight(1f))
                    HeroStat("$doneToday", "feitas", Color(0xFF7EE0B8), Modifier.weight(1f))
                }
                if (due.isNotEmpty()) Row(
                    Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(Gold).clickable { onStartReview(due.first().id) }.padding(vertical = 14.dp),
                    horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(Icons.Outlined.PlayArrow, null, Modifier.size(22.dp), tint = InkB)
                    Spacer(Modifier.width(6.dp))
                    Text(if (due.size > 1) "Começar pela primeira" else "Começar a revisão", fontWeight = FontWeight.Black, color = InkB)
                }
            }
        }
        if (due.isEmpty() && upcoming.isEmpty()) item { EmptyState("Revisões em dia", "Marque um tópico como estudado: as revisões entram sozinhas, cada uma no dia certo.") }
        if (due.isNotEmpty()) item { Text("Para fazer agora", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.onSurface) }
        items(due, key = { it.id }) { review ->
            val topic = topics.firstOrNull { it.id == review.topicId }
            val subject = subjects.firstOrNull { it.id == topic?.subjectId }
            val daysLate = java.time.temporal.ChronoUnit.DAYS.between(dateOf(review.dueAt), today)
            Column(
                Modifier.fillMaxWidth().clip(RoundedCornerShape(20.dp)).background(MaterialTheme.colorScheme.surfaceContainerLow)
                    .border(1.5.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.7f), RoundedCornerShape(20.dp))
                    .drawBehind { drawRect(if (daysLate > 0) Red else Gold, size = Size(5.dp.toPx(), size.height)) }
                    .clickable { onStartReview(review.id) }
                    .padding(start = 20.dp, end = 14.dp, top = 14.dp, bottom = 12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text((subject?.name ?: "").uppercase(), Modifier.weight(1f), style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.primary, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    // Difícil na última: a mesma etapa volta todo dia até ficar Normal ou Fácil.
                    if (daysLate <= 0 && review.perceivedDifficulty == br.com.estudario.data.local.ReviewDifficulty.DIFICIL) Text(
                        "repetindo",
                        Modifier.clip(RoundedCornerShape(50)).background(Gold.copy(alpha = 0.22f)).padding(horizontal = 8.dp, vertical = 2.dp),
                        style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Black, color = Color(0xFF9A6700),
                    )
                    if (daysLate > 0) Text(
                        "atrasada $daysLate ${if (daysLate == 1L) "dia" else "dias"}",
                        Modifier.clip(RoundedCornerShape(50)).background(Red.copy(alpha = 0.12f)).padding(horizontal = 8.dp, vertical = 2.dp),
                        style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Black, color = Red,
                    )
                }
                Text(topic?.title ?: "Tópico", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.onSurface)
                StageLadder(review.stage, review.perceivedDifficulty == br.com.estudario.data.local.ReviewDifficulty.DIFICIL)
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End, verticalAlignment = Alignment.CenterVertically) {
                    TextButton(onClick = { onOpenTopic(review.topicId) }) { Text("Ver tópico") }
                    TextButton(onClick = { viewModel.ignoreReview(review) }) { Text("Pular") }
                    br.com.estudario.ui.brand.Button(onClick = { onStartReview(review.id) }) { Text("Revisar") }
                }
            }
        }
        if (upcoming.isNotEmpty()) {
            item { Text("Próximas", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.onSurface, modifier = Modifier.padding(top = 6.dp)) }
            items(upcoming.take(30).groupBy { dateOf(it.dueAt) }.entries.take(10).toList(), key = { it.key.toEpochDay() }) { (date, list) ->
                Row(verticalAlignment = Alignment.Top) {
                    Column(
                        Modifier.width(52.dp).clip(RoundedCornerShape(14.dp)).background(MaterialTheme.colorScheme.surfaceContainerHigh).padding(vertical = 6.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text("${date.dayOfMonth}", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black)
                        Text(date.month.getDisplayName(TextStyle.SHORT, Locale("pt", "BR")).uppercase().trimEnd('.'), style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.primary)
                    }
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f).padding(top = 4.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        list.forEach { review ->
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    "${review.stage}ª",
                                    Modifier.clip(RoundedCornerShape(50)).background(MaterialTheme.colorScheme.primaryContainer).padding(horizontal = 8.dp, vertical = 2.dp),
                                    style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.onPrimaryContainer,
                                )
                                Spacer(Modifier.width(8.dp))
                                Text(topics.firstOrNull { it.id == review.topicId }?.title ?: "Tópico", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface, maxLines = 2, overflow = TextOverflow.Ellipsis)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun HeroStat(value: String, label: String, color: Color, modifier: Modifier) {
    Column(
        modifier.clip(RoundedCornerShape(14.dp)).background(Color.White.copy(alpha = 0.07f)).border(1.dp, Color.White.copy(alpha = 0.12f), RoundedCornerShape(14.dp)).padding(10.dp),
    ) {
        Text(value, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black, color = color)
        Text(label, style = MaterialTheme.typography.labelSmall, color = Color.White.copy(alpha = 0.75f))
    }
}

/** A escadinha das etapas: feitas em verde, a de agora em dourado. */
@Composable
private fun StageLadder(stage: Int, repeating: Boolean = false) {
    val first = (stage - 2).coerceAtLeast(1)
    val nowColor = if (repeating) Color(0xFFFF8A3D) else Gold
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top) {
        (0 until LADDER).forEach { i ->
            val s = first + i
            val done = s < stage
            val now = s == stage
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    Modifier.size(22.dp).clip(CircleShape).background(when { done -> Mint; now -> nowColor; else -> Color.Transparent })
                        .border(2.dp, when { done -> Mint; now -> nowColor; else -> MaterialTheme.colorScheme.outlineVariant }, CircleShape),
                    contentAlignment = Alignment.Center,
                ) {
                    if (done) Icon(Icons.Outlined.Check, null, Modifier.size(14.dp), tint = Color.White)
                    else if (now && repeating) Icon(Icons.Outlined.Replay, null, Modifier.size(14.dp), tint = Color.White)
                }
                Text("${s}ª", style = MaterialTheme.typography.labelSmall, fontWeight = if (now) FontWeight.Black else FontWeight.SemiBold, color = if (now) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant)
            }
            if (i < LADDER - 1) Box(Modifier.weight(1f).padding(top = 10.dp, start = 4.dp, end = 4.dp).height(2.dp).background(if (done) Mint else MaterialTheme.colorScheme.outlineVariant))
        }
    }
}
