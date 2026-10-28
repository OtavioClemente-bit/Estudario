package br.com.estudario.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.ExpandLess
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.estudario.domain.ComputedReviewStatus
import br.com.estudario.domain.ReviewPolicy
import br.com.estudario.ui.AppViewModel
import br.com.estudario.ui.brand.Icon
import br.com.estudario.ui.brand.BrandIcon
import br.com.estudario.ui.brand.Glyph
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import br.com.estudario.ui.brand.marker
import br.com.estudario.ui.theme.screenPadding
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.TextStyle
import java.util.Locale

private val Gold = Color(0xFFF5B83D)
private val Mint = Color(0xFF2FD39A)
private val Red = Color(0xFFE5484D)
private val Violet = Color(0xFFA78BFF)
private val Cyan = Color(0xFF22D3EE)
private val InkA = Color(0xFF2E2799)
private val InkB = Color(0xFF1B1464)

/** Revisar tudo em sequência: ao terminar uma, a próxima do dia abre sozinha (como no site). */
internal object ReviewRun { var active = false }

/**
 * As revisões espaçadas no visual do site: o topo com o que há para hoje, o botão de revisar tudo em
 * sequência e os quatro números; "Para fazer agora" numa lista curta (as atrasadas primeiro, com o
 * botão Revisar vermelho) e "Próximas" em grupos por quando (amanhã, nesta semana, neste mês, depois).
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
    var helpOpen by remember { mutableStateOf(false) }
    if (helpOpen) br.com.estudario.ui.help.ReviewsHelp { helpOpen = false }
    if (settingsOpen) options?.let { initial -> ReviewPracticeSettings(initial, onClose = { settingsOpen = false }, onSave = { viewModel.setReviewPractice(it); settingsOpen = false }) }
    val zone = ZoneId.systemDefault()
    val today = LocalDate.now(zone)
    fun dateOf(millis: Long) = Instant.ofEpochMilli(millis).atZone(zone).toLocalDate()
    // uma por tópico: a etapa seguinte só abre depois da anterior (antes cada etapa era uma linha)
    val pending = ReviewPolicy.nextPerTopic(reviews)
    val due = ReviewPolicy.dueToday(reviews)
    fun isLate(r: br.com.estudario.data.local.ReviewScheduleEntity) = ReviewPolicy.status(r) == ComputedReviewStatus.ATRASADA
    // as atrasadas primeiro, depois na ordem do dia
    val ordered = due.sortedWith(compareBy({ !isLate(it) }, { it.dueAt }))
    val late = due.count { isLate(it) }
    val upcoming = (pending - due.toSet()).sortedBy { it.dueAt }
    val inWeek = upcoming.count { dateOf(it.dueAt) <= today.plusDays(7) }
    val doneToday = reviews.count { it.completedAt != null && dateOf(it.completedAt!!) == today }
    val palette = br.com.estudario.ui.theme.estudarioColors().subjectPalette
    fun subjectOf(topicId: Long) = topics.firstOrNull { it.id == topicId }?.subjectId?.let { id -> subjects.firstOrNull { it.id == id } }
    var allDue by remember { mutableStateOf(false) }
    var subjectFilter by remember { mutableStateOf<Long?>(null) }

    LazyColumn(Modifier.fillMaxSize(), contentPadding = screenPadding(), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (showInlineBack) IconButton(onClick = onBack) { Icon(Icons.Outlined.ArrowBack, "Voltar") }
                Column(Modifier.weight(1f)) {
                    Text("Revisões espaçadas", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Black, modifier = Modifier.padding(start = 3.dp).marker())
                    Text("Revisar na hora certa é o que faz o conteúdo ficar", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                br.com.estudario.ui.help.HelpButton("Como funcionam as etapas") { helpOpen = true }
                IconButton(onClick = { settingsOpen = true }, enabled = options != null, modifier = Modifier.semantics { contentDescription = "Configurar revisão" }) { BrandIcon(Glyph.Gear) }
            }
        }

        // ---- o topo: quantas para hoje, o botão de começar e os quatro números
        item {
            Column(
                Modifier.fillMaxWidth().clip(RoundedCornerShape(26.dp)).background(Brush.linearGradient(listOf(InkA, InkB)))
                    .drawBehind {
                        val step = 26.dp.toPx(); var y = step
                        while (y < size.height) { drawLine(Color.White.copy(alpha = 0.05f), Offset(0f, y), Offset(size.width, y), 1.dp.toPx()); y += step }
                    }
                    .padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    br.com.estudario.ui.assistant.Folha(80.dp, mood = when { due.isEmpty() -> br.com.estudario.ui.assistant.FolhaMood.PROUD; late > 0 -> br.com.estudario.ui.assistant.FolhaMood.TALKING; else -> br.com.estudario.ui.assistant.FolhaMood.HAPPY })
                    Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text("REVISÃO ESPAÇADA", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Black, color = Gold, letterSpacing = 1.sp)
                        Text(
                            if (due.isEmpty()) "Tudo em dia por hoje" else "${due.size} ${if (due.size == 1) "revisão" else "revisões"} para hoje",
                            style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Black, color = Color.White,
                        )
                        Text(
                            if (due.isEmpty()) "Quando um tópico estudado chegar no dia de revisar, ele aparece aqui." else "Cada uma leva poucos minutos: lembrar, rever o resumo e testar com questões.",
                            style = MaterialTheme.typography.bodySmall, color = Color.White.copy(alpha = 0.78f),
                        )
                    }
                }
                if (ordered.isNotEmpty()) Row(
                    Modifier.fillMaxWidth().shadow(8.dp, RoundedCornerShape(16.dp)).clip(RoundedCornerShape(16.dp)).background(Gold)
                        .clickable { ReviewRun.active = ordered.size > 1; onStartReview(ordered.first().id) }.padding(vertical = 14.dp),
                    horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(Icons.Outlined.PlayArrow, null, Modifier.size(22.dp), tint = InkB)
                    Spacer(Modifier.width(6.dp))
                    Text(if (ordered.size > 1) "Revisar tudo em sequência" else "Começar a revisão", fontWeight = FontWeight.Black, color = InkB)
                }
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    HeroStat("${due.size}", "para hoje", Glyph.Calendar, Gold, Modifier.weight(1f))
                    HeroStat("$late", "atrasadas", Glyph.Timer, Color(0xFFFF8A9E), Modifier.weight(1f))
                }
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    HeroStat("$inWeek", "nos próximos 7 dias", Glyph.Calendar, Violet, Modifier.weight(1f))
                    HeroStat("$doneToday", "feitas hoje", Glyph.Check, Mint, Modifier.weight(1f))
                }
            }
        }

        // ---- para fazer agora: uma lista curta, as atrasadas primeiro
        item {
            Panel {
                if (ordered.isEmpty()) {
                    PanelHead(Glyph.Check, Mint, "Revisões em dia", "Estude um tópico e marque como estudado: as revisões entram sozinhas nos dias certos.", null)
                    return@Panel
                }
                PanelHead(Glyph.Calendar, Gold, "Para fazer agora", if (late > 0) "$late ${if (late == 1) "atrasada vem" else "atrasadas vêm"} primeiro" else "Na ordem do dia", ordered.size)
                (if (allDue) ordered else ordered.take(5)).forEach { review ->
                    key(review.id) {
                        val topic = topics.firstOrNull { it.id == review.topicId }
                        val subjectName = subjectOf(review.topicId)?.name.orEmpty()
                        val color = br.com.estudario.ui.components.subjectAccentColor(subjectName, palette)
                        val daysLate = java.time.temporal.ChronoUnit.DAYS.between(dateOf(review.dueAt), today)
                        val repeating = review.perceivedDifficulty == br.com.estudario.data.local.ReviewDifficulty.DIFICIL
                        DueRow(
                            stage = review.stage, color = color, subject = subjectName.ifBlank { "Matéria" }, title = topic?.title ?: "Tópico",
                            meta = when {
                                daysLate > 0 -> "atrasada $daysLate ${if (daysLate == 1L) "dia" else "dias"} · ${review.stage}ª revisão"
                                repeating -> "repetindo · ${review.stage}ª revisão"
                                else -> "${review.stage}ª revisão"
                            },
                            late = daysLate > 0,
                            onOpen = { ReviewRun.active = false; onStartReview(review.id) },
                            onTopic = { onOpenTopic(review.topicId) },
                            onSkip = { viewModel.ignoreReview(review) },
                        )
                    }
                }
                if (ordered.size > 5) MoreToggle(allDue, ordered.size) { allDue = !allDue }
            }
        }

        // ---- próximas: por quando, cada grupo fechado em poucas linhas
        if (upcoming.isNotEmpty()) item {
            val subjectOptions = upcoming.mapNotNull { subjectOf(it.topicId) }.distinctBy { it.id }.sortedBy { it.name }
            val filtered = subjectFilter?.let { id -> upcoming.filter { subjectOf(it.topicId)?.id == id } } ?: upcoming
            val groups = listOf(
                Triple("Amanhã", Gold, filtered.filter { dateOf(it.dueAt) <= today.plusDays(1) }),
                Triple("Nesta semana", Violet, filtered.filter { dateOf(it.dueAt) in today.plusDays(2)..today.plusDays(7) }),
                Triple("Neste mês", Cyan, filtered.filter { dateOf(it.dueAt) in today.plusDays(8)..today.plusDays(30) }),
                Triple("Mais adiante", Mint, filtered.filter { dateOf(it.dueAt) > today.plusDays(30) }),
            ).filter { it.third.isNotEmpty() }
            Panel {
                PanelHead(Glyph.Calendar, Violet, "Próximas", "O que volta para revisar, por quando", upcoming.size)
                if (subjectOptions.size > 1) Row(Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    FilterChip(subjectFilter == null, { subjectFilter = null }, { Text("Todas") })
                    subjectOptions.forEach { s -> FilterChip(subjectFilter == s.id, { subjectFilter = s.id }, { Text(s.name, maxLines = 1) }) }
                }
                if (groups.isEmpty()) Text("Nada dessa matéria nas próximas revisões.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                groups.forEach { (label, color, list) ->
                    key(label, subjectFilter) {
                        var open by remember { mutableStateOf(false) }
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Box(Modifier.size(10.dp).clip(CircleShape).background(color))
                                Text(label, fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.onSurface)
                                Text("${list.size} ${if (list.size == 1) "revisão" else "revisões"}", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            (if (open) list else list.take(3)).forEach { review ->
                                val date = dateOf(review.dueAt)
                                val subjectName = subjectOf(review.topicId)?.name.orEmpty()
                                Row(
                                    Modifier.fillMaxWidth().padding(start = 18.dp).clip(RoundedCornerShape(14.dp)).background(MaterialTheme.colorScheme.surfaceContainerHigh)
                                        .drawBehind { drawRect(br.com.estudario.ui.components.subjectAccentColor(subjectName, palette), size = androidx.compose.ui.geometry.Size(4.dp.toPx(), size.height)) }
                                        .clickable { onOpenTopic(review.topicId) }.padding(start = 12.dp, end = 10.dp, top = 8.dp, bottom = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp),
                                ) {
                                    Column(Modifier.width(40.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text("${date.dayOfMonth}", fontWeight = FontWeight.Black, fontSize = 18.sp, color = MaterialTheme.colorScheme.onSurface)
                                        Text(date.month.getDisplayName(TextStyle.SHORT, Locale("pt", "BR")).uppercase().trimEnd('.'), style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Black, color = color)
                                    }
                                    Column(Modifier.weight(1f)) {
                                        Text(topics.firstOrNull { it.id == review.topicId }?.title ?: "Tópico", fontWeight = FontWeight.Bold, maxLines = 2, overflow = TextOverflow.Ellipsis, color = MaterialTheme.colorScheme.onSurface)
                                        Text(listOf(subjectName, "${review.stage}ª revisão").filter { it.isNotBlank() }.joinToString(" · "), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                    }
                                }
                            }
                            if (list.size > 3) MoreToggle(open, list.size) { open = !open }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun Panel(content: @Composable ColumnScope.() -> Unit) {
    Column(
        Modifier.fillMaxWidth().clip(RoundedCornerShape(22.dp)).background(MaterialTheme.colorScheme.surfaceContainerLow)
            .border(1.5.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f), RoundedCornerShape(22.dp)).padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        content = content,
    )
}

/** Cabeça do painel: o desenho, o título, a linha de apoio e o contador com brilho. */
@Composable
private fun PanelHead(glyph: Glyph, color: Color, title: String, subtitle: String, count: Int?) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        Box(Modifier.size(44.dp).clip(RoundedCornerShape(14.dp)).background(color.copy(alpha = 0.16f)), contentAlignment = Alignment.Center) { BrandIcon(glyph, size = 30.dp) }
        Column(Modifier.weight(1f)) {
            Text(title, fontWeight = FontWeight.Black, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)
            Text(subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        if (count != null) Box(
            Modifier.shadow(10.dp, RoundedCornerShape(12.dp), ambientColor = Violet, spotColor = Violet).clip(RoundedCornerShape(12.dp))
                .background(Brush.linearGradient(listOf(Color(0xFF8C70FF), Color(0xFF5B4BF5))))
                .border(1.5.dp, Violet, RoundedCornerShape(12.dp)).defaultMinSize(minWidth = 42.dp, minHeight = 34.dp).padding(horizontal = 12.dp),
            contentAlignment = Alignment.Center,
        ) { Text("$count", fontWeight = FontWeight.Black, fontSize = 16.sp, color = Color.White) }
    }
}

/** Uma revisão para fazer: a etapa na cor da matéria, o tópico e o botão Revisar (vermelho se atrasada). */
@Composable
private fun DueRow(stage: Int, color: Color, subject: String, title: String, meta: String, late: Boolean, onOpen: () -> Unit, onTopic: () -> Unit, onSkip: () -> Unit) {
    Column(
        Modifier.fillMaxWidth().clip(RoundedCornerShape(18.dp)).background(MaterialTheme.colorScheme.surface)
            .border(1.5.dp, if (late) Red.copy(alpha = 0.45f) else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f), RoundedCornerShape(18.dp))
            .clickable(onClick = onOpen).padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Column(
                Modifier.size(52.dp).clip(RoundedCornerShape(14.dp)).background(color.copy(alpha = 0.18f)).border(2.dp, color, RoundedCornerShape(14.dp)),
                horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center,
            ) {
                Text("${stage}ª", fontWeight = FontWeight.Black, fontSize = 18.sp, color = MaterialTheme.colorScheme.onSurface)
                Text("etapa", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(subject.uppercase(), style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Black, color = color, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text(title, fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.onSurface, maxLines = 3, overflow = TextOverflow.Ellipsis)
                Text(meta, style = MaterialTheme.typography.labelSmall, fontWeight = if (late) FontWeight.Bold else FontWeight.Normal, color = if (late) Red else MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            TextButton(onClick = onTopic) { Text("Ver tópico") }
            TextButton(onClick = onSkip) { Text("Pular", color = MaterialTheme.colorScheme.onSurfaceVariant) }
            Spacer(Modifier.weight(1f))
            Row(
                Modifier.shadow(6.dp, RoundedCornerShape(50)).clip(RoundedCornerShape(50))
                    .background(if (late) Brush.horizontalGradient(listOf(Color(0xFFFF5A7A), Red)) else Brush.horizontalGradient(listOf(Color(0xFF7C5CFF), Color(0xFF5B4BF5), Mint)))
                    .clickable(onClick = onOpen).padding(start = 10.dp, end = 16.dp, top = 9.dp, bottom = 9.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(Icons.Outlined.PlayArrow, null, Modifier.size(20.dp), tint = Color.White)
                Spacer(Modifier.width(4.dp))
                Text("Revisar", fontWeight = FontWeight.Black, color = Color.White, fontSize = 14.sp)
            }
        }
    }
}

@Composable
private fun MoreToggle(open: Boolean, total: Int, onToggle: () -> Unit) {
    Row(
        Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).clickable(onClick = onToggle).padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(if (open) "Mostrar menos" else "Ver todas ($total)", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        Icon(if (open) Icons.Outlined.ExpandLess else Icons.Outlined.ExpandMore, null, tint = MaterialTheme.colorScheme.primary)
    }
}

@Composable
private fun HeroStat(value: String, label: String, glyph: Glyph, color: Color, modifier: Modifier) {
    Row(
        modifier.clip(RoundedCornerShape(14.dp)).background(Color.White.copy(alpha = 0.07f)).border(1.dp, Color.White.copy(alpha = 0.12f), RoundedCornerShape(14.dp)).padding(10.dp),
        verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        BrandIcon(glyph, size = 26.dp)
        Column {
            Text(value, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black, color = color)
            Text(label, style = MaterialTheme.typography.labelSmall, color = Color.White.copy(alpha = 0.75f), maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
    }
}
