package br.com.estudario.ui.screens

import br.com.estudario.ui.brand.SubjectArtTile
import br.com.estudario.domain.catalog.SubjectArt
import androidx.compose.foundation.layout.widthIn
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Replay
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material.icons.outlined.ExpandLess
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.foundation.clickable
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.estudario.data.local.ErrorWithQuestion
import br.com.estudario.data.local.QuestionSessionEntity
import br.com.estudario.domain.ErrorRetryLadder
import br.com.estudario.domain.ErrorVirada
import br.com.estudario.ui.AppViewModel
import br.com.estudario.ui.assistant.Folha
import br.com.estudario.ui.assistant.FolhaMood
import br.com.estudario.ui.brand.Button
import br.com.estudario.ui.brand.Icon
import br.com.estudario.ui.brand.Surface
import br.com.estudario.ui.components.EmptyState
import br.com.estudario.ui.components.MarkdownText
import br.com.estudario.ui.components.ScreenTitle
import br.com.estudario.ui.components.plainFormulaText
import br.com.estudario.ui.components.subjectAccentColor
import br.com.estudario.ui.theme.EstudarioShapes
import br.com.estudario.ui.theme.estudarioColors
import br.com.estudario.ui.theme.screenPadding
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/*
 * "A Virada": o caderno de erros com propósito (a regra em ErrorVirada, módulo comum; o site faz
 * igual). O placar dos erros que viraram, a missão do dia, a escadinha de cada erro até o "VIROU!",
 * o porquê dos erros e o mural dos que viraram. As cores vêm do tema (claro e escuro); só o placar
 * tem fundo próprio, azul-noite nos dois temas, com texto claro.
 */

private data class Hotspot(val topicId: Long, val title: String, val questions: Int, val misses: Int)

private fun ErrorWithQuestion.virada() = ErrorVirada.Entry(
    entry.id, entry.questionId, question.topicId, entry.errorCount, entry.retryStreak, entry.nextRetryAt, entry.lastReviewedAt, entry.lastErrorAt, entry.comment,
)

private val Amber = Color(0xFFFFB421)
private val Green = Color(0xFF2FBF8A)
private val virouDate = SimpleDateFormat("dd/MM", Locale("pt", "BR"))

@Composable
private fun reasonColor(r: ErrorVirada.Reason): Color = when (r) {
    ErrorVirada.Reason.NAO_SABIA -> Color(0xFF5B8DEF)
    ErrorVirada.Reason.CONFUNDI -> Color(0xFF8E7CFF)
    ErrorVirada.Reason.LI_RAPIDO -> Amber
    ErrorVirada.Reason.PEGADINHA -> Color(0xFFFF6B6B)
}

private fun daysLabel(at: Long?, now: Long): String {
    if (at == null) return "virou"
    val d = ((at - now + 86_399_999L) / 86_400_000L).toInt()
    return when { d <= 0 -> "vence hoje"; d == 1 -> "volta amanhã"; else -> "volta em $d dias" }
}

/** A escadinha: errou → 3 → 10 → 30 dias → virou. */
@Composable
private fun Ladder(step: Int) {
    val labels = listOf("errou") + ErrorRetryLadder.days.map { "$it dias" } + "virou"
    val scheme = MaterialTheme.colorScheme
    val done = estudarioColors().completed
    Row(Modifier.fillMaxWidth()) {
        labels.forEachIndexed { i, label ->
            val last = i == labels.lastIndex
            val won = last && step >= ErrorRetryLadder.days.size
            val isDone = i in 1..step && !last
            val now = i == step + 1 && !last
            val (bg, fg) = when {
                i == 0 -> scheme.error.copy(alpha = 0.16f) to scheme.error
                won -> Amber to Color(0xFF1B1B18)
                isDone -> done to estudarioColors().onCompleted
                now -> scheme.surface to scheme.onSurface
                else -> scheme.surfaceVariant to scheme.onSurfaceVariant
            }
            Column(Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(3.dp)) {
                Box(
                    Modifier.size(26.dp).clip(CircleShape).background(bg).then(if (now) Modifier.border(2.dp, Amber, CircleShape) else Modifier),
                    contentAlignment = Alignment.Center,
                ) { Text(when { i == 0 -> "✕"; last -> "★"; isDone -> "✓"; else -> "$i" }, color = fg, fontWeight = FontWeight.Bold, fontSize = 11.sp) }
                Text(label, style = MaterialTheme.typography.labelSmall, fontSize = 10.sp, color = if (now) scheme.onSurface else scheme.onSurfaceVariant, fontWeight = if (now) FontWeight.Bold else FontWeight.Normal)
            }
        }
    }
}

@Composable
private fun SectionTitle(title: String, aside: String? = null) {
    Row(Modifier.fillMaxWidth().padding(top = 6.dp), verticalAlignment = Alignment.Bottom) {
        Text(title, Modifier.weight(1f), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        aside?.let { Text(it, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant) }
    }
}

// ------------------------------------------------------------------ a entrada

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ViradaScreen(viewModel: AppViewModel, onMission: (Long?) -> Unit, onTrainErrors: () -> Unit, onTrainTopic: (Long) -> Unit, onOpenTopic: (Long) -> Unit = {}) {
    val errors by viewModel.errors.collectAsState()
    val topics by viewModel.topics.collectAsState()
    val subjects by viewModel.subjects.collectAsState()
    val now = remember { System.currentTimeMillis() }
    val scheme = MaterialTheme.colorScheme
    val colors = estudarioColors()
    val topicById = remember(topics) { topics.associateBy { it.id } }
    val subjectById = remember(subjects) { subjects.associateBy { it.id } }
    val entries = remember(errors) { errors.map { it.virada() } }
    val stageOf = remember(entries) { entries.associate { it.id to ErrorVirada.stage(it) } }
    val won = errors.filter { stageOf[it.entry.id] == ErrorVirada.Stage.TURNED }
    val turning = errors.count { stageOf[it.entry.id] == ErrorVirada.Stage.TURNING }
    val table = errors.size - won.size - turning
    val due = remember(entries) { ErrorVirada.due(entries, now) }
    val dueIds = due.map { it.id }.toSet()
    val mission = due.take(5)
    val week = ErrorVirada.stepsThisWeek(entries, now)
    val reasons = ErrorVirada.reasonStats(entries)
    // os conceitos (ou tópicos) que mais derrubam: onde há mais erros ainda não virados
    val hotspots = remember(errors, topicById) {
        errors.filter { stageOf[it.entry.id] != ErrorVirada.Stage.TURNED }
            .groupBy { it.question.topicId }
            .map { (topicId, list) -> Hotspot(topicId, list.firstNotNullOfOrNull { e -> e.entry.concept.takeIf { it.isNotBlank() } } ?: topicById[topicId]?.title ?: "Tópico", list.size, list.sumOf { it.entry.errorCount }) }
            .sortedWith(compareByDescending<Hotspot> { it.misses }.thenByDescending { it.questions }).take(3)
    }
    val groups = remember(entries) { ErrorVirada.byTopic(entries, now) }
    val byId = remember(errors) { errors.associateBy { it.entry.id } }
    var expandedTopics by remember { mutableStateOf(setOf<Long>()) }
    var helpOpen by remember { mutableStateOf(false) }
    if (helpOpen) br.com.estudario.ui.help.ViradaHelp { helpOpen = false }

    LazyColumn(Modifier.fillMaxSize(), contentPadding = screenPadding(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item { ScreenTitle("Caderno de erros", "Aqui erro vira acerto", stackActionsWhenNarrow = false) { br.com.estudario.ui.help.HelpButton("O que é a Virada?") { helpOpen = true } } }

        // O placar: fundo azul-noite próprio nos dois temas, texto claro.
        item {
            val pctWon = if (errors.isEmpty()) 0f else won.size.toFloat() / errors.size
            val pctTurning = if (errors.isEmpty()) 0f else turning.toFloat() / errors.size
            Box(Modifier.fillMaxWidth().clip(RoundedCornerShape(20.dp)).background(Brush.linearGradient(listOf(Color(0xFF1F2A44), Color(0xFF2B3B63)))).padding(18.dp)) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                        Folha(64.dp, mood = when { errors.isEmpty() -> FolhaMood.HAPPY; won.size * 2 >= errors.size -> FolhaMood.PROUD; else -> FolhaMood.TALKING })
                        Column(Modifier.weight(1f)) {
                            Text("PLACAR DA VIRADA", color = Color(0xFFFFD27A), fontWeight = FontWeight.ExtraBold, fontSize = 11.sp, letterSpacing = 1.4.sp)
                            Text(
                                if (errors.isEmpty()) "Nenhum erro na mesa" else "Você já virou ${won.size} de ${errors.size} ${if (errors.size == 1) "erro" else "erros"}",
                                color = Color.White, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold,
                            )
                            Text(if (errors.isEmpty()) "Quando errar uma questão, ela vem para cá." else "Erro virado é questão a mais na prova.", color = Color.White.copy(alpha = 0.78f), style = MaterialTheme.typography.bodySmall)
                        }
                    }
                    if (errors.isNotEmpty()) {
                        Row(Modifier.fillMaxWidth().height(12.dp).clip(CircleShape).background(Color.White.copy(alpha = 0.16f))) {
                            if (pctWon > 0f) Box(Modifier.fillMaxHeight().weight(pctWon).background(Green))
                            if (pctTurning > 0f) Box(Modifier.fillMaxHeight().weight(pctTurning).background(Amber))
                            if (1f - pctWon - pctTurning > 0.001f) Spacer(Modifier.weight(1f - pctWon - pctTurning))
                        }
                        FlowRow(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                            Legend(Green, "${won.size} viraram"); Legend(Amber, "$turning virando"); Legend(Color.White.copy(alpha = 0.4f), "$table na mesa")
                        }
                        if (week > 0) Surface(shape = CircleShape, color = Green.copy(alpha = 0.22f)) {
                            Text("▲ $week ${if (week == 1) "degrau subido" else "degraus subidos"} esta semana", Modifier.padding(horizontal = 10.dp, vertical = 4.dp), color = Color(0xFF8FF0C8), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                }
            }
        }

        if (errors.isEmpty()) {
            item { EmptyState("Caderno limpo", "Nenhum erro por aqui. Quando errar, a questão volta em 3, 10 e 30 dias até virar.") }
            return@LazyColumn
        }

        // A missão de hoje.
        item {
            Surface(shape = EstudarioShapes.panel, color = scheme.surfaceContainerLow, border = BorderStroke(2.dp, Amber), modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    if (mission.isNotEmpty()) {
                        Text("Missão de hoje · virar ${mission.size} ${if (mission.size == 1) "erro" else "erros"}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Text("Os da mesa e os que vencem hoje, ~${(mission.size * 1.2).toInt().coerceAtLeast(1)} min. Acertou, ele sobe um degrau.", style = MaterialTheme.typography.bodySmall, color = scheme.onSurfaceVariant)
                        FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            ErrorVirada.missionByTopic(mission).forEach { (t, n) ->
                                Surface(shape = RoundedCornerShape(8.dp), color = scheme.surfaceVariant) {
                                    Text("$n · " + (t?.let(topicById::get)?.title?.trimEnd('.') ?: "Erro"), Modifier.padding(horizontal = 9.dp, vertical = 4.dp).widthIn(max = 220.dp), style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                }
                            }
                        }
                        br.com.estudario.ui.brand.Button(
                            onClick = { onMission(null) },
                            colors = ButtonDefaults.buttonColors(containerColor = Amber, contentColor = Color(0xFF1B1B18)),
                            modifier = Modifier.heightIn(min = 48.dp),
                        ) { Icon(Icons.Outlined.Replay, null); Spacer(Modifier.width(8.dp)); Text("Começar a virada", fontWeight = FontWeight.ExtraBold) }
                    } else {
                        Text("Nada vencendo hoje", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Text("Os erros voltam no dia certo (3, 10 e 30 dias). Quer treinar mesmo assim?", style = MaterialTheme.typography.bodySmall, color = scheme.onSurfaceVariant)
                        OutlinedButton(onClick = onTrainErrors) { Icon(Icons.Outlined.Replay, null); Spacer(Modifier.width(8.dp)); Text("Refazer erradas") }
                    }
                }
            }
        }

        // Por que você erra.
        item { SectionTitle("Por que você erra", if (reasons.isNotEmpty()) "dos erros com motivo" else null) }
        item {
            Surface(shape = EstudarioShapes.panel, color = scheme.surfaceContainerLow, modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    if (reasons.isEmpty()) Text(
                        "Na missão, quando errar de novo, um toque diz o motivo (não sabia, confundi, li rápido, pegadinha). Aqui aparece o seu padrão e a dica do Folha para ele.",
                        style = MaterialTheme.typography.bodySmall, color = scheme.onSurfaceVariant,
                    ) else {
                        val total = reasons.sumOf { it.second }
                        ErrorVirada.Reason.entries.forEach { r ->
                            val n = reasons.firstOrNull { it.first == r }?.second ?: 0
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                Text(r.label, Modifier.width(120.dp), style = MaterialTheme.typography.bodySmall)
                                Box(Modifier.weight(1f).height(9.dp).clip(CircleShape).background(scheme.surfaceVariant)) {
                                    if (n > 0) Box(Modifier.fillMaxWidth(n.toFloat() / total).fillMaxHeight().clip(CircleShape).background(reasonColor(r)))
                                }
                                Text("${n * 100 / total}%", Modifier.width(36.dp), style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, textAlign = TextAlign.End)
                            }
                        }
                        val top = reasons.first().first
                        Row(Modifier.fillMaxWidth().clip(RoundedCornerShape(8.dp)).background(scheme.surface)) {
                            Box(Modifier.width(3.dp).heightIn(min = 40.dp).background(Amber))
                            Text("Seu erro mais comum é ${top.label.lowercase()}. Dica do Folha: ${top.tip}", Modifier.padding(10.dp), style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        }

        if (hotspots.isNotEmpty()) {
            item { SectionTitle("O que mais te derruba") }
            items(hotspots, key = { "hot-${it.topicId}" }) { (topicId, title, questionsCount, misses) ->
                val topic = topicById[topicId]
                val hotSubject = topic?.subjectId?.let(subjectById::get)?.name.orEmpty()
                Surface(shape = EstudarioShapes.row, color = scheme.surfaceContainerLow, modifier = Modifier.fillMaxWidth()) {
                    Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        SubjectArtTile(SubjectArt.iconFor(hotSubject), subjectAccentColor(hotSubject, colors.subjectPalette), 42.dp)
                        Column(Modifier.weight(1f)) {
                            Text(title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold, maxLines = 2, overflow = TextOverflow.Ellipsis)
                            if (hotSubject.isNotBlank()) Text(hotSubject, style = MaterialTheme.typography.labelSmall, color = scheme.onSurfaceVariant, maxLines = 1, overflow = TextOverflow.Ellipsis)
                            Text(
                                "${questionsCount} ${if (questionsCount == 1) "questão" else "questões"} · errou ${misses} ${if (misses == 1) "vez" else "vezes"}",
                                style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = scheme.error,
                            )
                        }
                        FilledTonalButton(onClick = { onTrainTopic(topicId) }, contentPadding = PaddingValues(horizontal = 12.dp)) { Text("Treinar 3") }
                    }
                }
            }
        }

        // Um cartão por tópico (com as fichas de cada questão dentro): com muitos erros, uma ficha
        // por questão virava uma rolagem sem fim. A escada continua por questão.
        val openGroups = groups.filter { !it.done }
        if (openGroups.isNotEmpty()) {
            item { SectionTitle("Seus erros por tópico", "${due.size} para hoje · ${openGroups.size} ${if (openGroups.size == 1) "tópico" else "tópicos"}") }
            items(openGroups, key = { "topico-${it.topicId}" }) { g ->
                val topic = g.topicId?.let(topicById::get)
                val subjectName = topic?.subjectId?.let(subjectById::get)?.name.orEmpty()
                val accent = subjectAccentColor(subjectName, colors.subjectPalette)
                val expanded = (g.topicId ?: -1L) in expandedTopics
                Surface(shape = EstudarioShapes.panel, color = scheme.surfaceContainerLow, modifier = Modifier.fillMaxWidth()) {
                    Row(Modifier.height(IntrinsicSize.Min)) {
                        Box(Modifier.width(5.dp).fillMaxHeight().background(accent))
                        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Row(
                                Modifier.fillMaxWidth().clip(RoundedCornerShape(10.dp)).clickable { val k = g.topicId ?: -1L; expandedTopics = if (k in expandedTopics) expandedTopics - k else expandedTopics + k },
                                verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp),
                            ) {
                                SubjectArtTile(SubjectArt.iconFor(subjectName), accent, 46.dp)
                                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                                    if (subjectName.isNotBlank()) Text(subjectName.uppercase(), fontSize = 10.sp, fontWeight = FontWeight.Bold, color = scheme.onSurfaceVariant, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                    Text(topic?.title ?: "Sem tópico", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, maxLines = 2, overflow = TextOverflow.Ellipsis)
                                    FlowRow(horizontalArrangement = Arrangement.spacedBy(5.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                                        Pill("${g.entries.size} ${if (g.entries.size == 1) "questão" else "questões"}", scheme.onSurface)
                                        if (g.due > 0) Pill("${g.due} para hoje", colors.attention)
                                        if (g.turned > 0) Pill("${g.turned} ${if (g.turned == 1) "virou" else "viraram"}", colors.completed)
                                        if (g.turning > 0) Pill("${g.turning} virando", scheme.onSurfaceVariant)
                                        g.topReason?.let { Pill(it.label, reasonColor(it)) }
                                    }
                                }
                                Icon(if (expanded) Icons.Outlined.ExpandLess else Icons.Outlined.ExpandMore, if (expanded) "Fechar" else "Ver as questões", tint = scheme.onSurfaceVariant)
                            }
                            if (g.due > 0 || expanded) FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                if (g.due > 0) Button(onClick = { onMission(g.topicId) }, contentPadding = PaddingValues(horizontal = 14.dp)) {
                                    Icon(Icons.Outlined.Replay, null, Modifier.size(18.dp)); Spacer(Modifier.width(6.dp)); Text("Virar ${g.due} ${if (g.due == 1) "erro" else "erros"}")
                                }
                                g.topicId?.let { id -> TextButton(onClick = { onOpenTopic(id) }) { Text("Ler a teoria") } }
                            }
                            if (expanded) g.entries.mapNotNull { byId[it.id] }.forEach { item ->
                                val note = ErrorVirada.noteOf(item.entry.comment)
                                val reason = ErrorVirada.reasonOf(item.entry.comment)
                                val stage = stageOf[item.entry.id]
                                Surface(shape = RoundedCornerShape(12.dp), color = scheme.surface, modifier = Modifier.fillMaxWidth()) {
                                    Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                        Row {
                                            Text(
                                                when (stage) { ErrorVirada.Stage.TURNED -> "VIROU ★"; ErrorVirada.Stage.TABLE -> "NA MESA · DÁ PARA VIRAR JÁ"; else -> daysLabel(item.entry.nextRetryAt, now).uppercase() },
                                                Modifier.weight(1f), style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = if (stage == ErrorVirada.Stage.TURNED) colors.completed else scheme.onSurfaceVariant,
                                            )
                                            Text("errou ${item.entry.errorCount}×", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = scheme.onSurfaceVariant)
                                        }
                                        Text(plainFormulaText(item.question.statement), style = MaterialTheme.typography.bodyMedium, maxLines = 4, overflow = TextOverflow.Ellipsis)
                                        FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                            item.entry.selectedAnswer?.let { Pill("você: $it", scheme.error) }
                                            item.entry.correctAnswer?.let { Pill("certa: $it", colors.completed) }
                                            reason?.let { Pill(it.label, reasonColor(it)) }
                                        }
                                        Ladder(ErrorVirada.step(item.virada()))
                                        if (note.isNotBlank()) Text(note, style = MaterialTheme.typography.bodySmall, color = scheme.onSurfaceVariant)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // O mural: um troféu por tópico virado por inteiro (antes, um por questão).
        val wonTopics = groups.filter { it.done }
        if (wonTopics.isNotEmpty()) {
            item { SectionTitle("Mural dos tópicos que viraram", "${wonTopics.size} ${if (wonTopics.size == 1) "troféu" else "troféus"}") }
            item {
                // post-its amarelos: claros no tema claro, âmbar escuro no tema escuro
                val dark = scheme.surface.luminanceIsDark()
                val paper = if (dark) Color(0xFF3A3320) else Color(0xFFFFF4CC)
                val ink = if (dark) Color(0xFFFFE9A8) else Color(0xFF3A2E00)
                val sub = if (dark) Color(0xFFD9C58A) else Color(0xFF6B5A1A)
                FlowRow(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalArrangement = Arrangement.spacedBy(14.dp), modifier = Modifier.padding(top = 8.dp)) {
                    wonTopics.sortedByDescending { g -> g.entries.maxOf { it.lastReviewedAt ?: 0 } }.take(12).forEachIndexed { i, g ->
                        val last = g.entries.maxOf { it.lastReviewedAt ?: 0 }
                        Box(Modifier.width(156.dp).rotate(listOf(-2f, 1.5f, -1f, 2f)[i % 4])) {
                            Column(Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(paper).padding(start = 12.dp, end = 12.dp, top = 20.dp, bottom = 12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text(g.topicId?.let(topicById::get)?.title ?: "Tópico", color = ink, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold, maxLines = 3, overflow = TextOverflow.Ellipsis)
                                Text("${g.entries.size} ${if (g.entries.size == 1) "erro virado" else "erros virados"}" + (if (last > 0) " · em ${virouDate.format(Date(last))}" else ""), color = sub, fontSize = 11.sp)
                            }
                            Text("VIROU!", Modifier.align(Alignment.TopEnd).offset(x = (-8).dp, y = (-8).dp).rotate(4f).clip(RoundedCornerShape(6.dp)).background(Green).padding(horizontal = 8.dp, vertical = 3.dp), color = Color.White, fontWeight = FontWeight.Black, fontSize = 11.sp)
                        }
                    }
                }
            }
        }
    }
}

private fun Color.luminanceIsDark(): Boolean = (0.299f * red + 0.587f * green + 0.114f * blue) < 0.5f

@Composable
private fun Legend(color: Color, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        Box(Modifier.size(9.dp).clip(CircleShape).background(color))
        Text(text, color = Color.White.copy(alpha = 0.85f), fontSize = 13.sp)
    }
}

@Composable
private fun Pill(text: String, color: Color) {
    Surface(shape = RoundedCornerShape(7.dp), color = color.copy(alpha = 0.15f)) {
        Text(text, Modifier.padding(horizontal = 8.dp, vertical = 3.dp), color = color, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold)
    }
}

// ------------------------------------------------------------------ a missão

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ViradaMissionScreen(viewModel: AppViewModel, topicId: Long? = null, onBack: () -> Unit) {
    val errors by viewModel.errors.collectAsState()
    val questions by viewModel.questions.collectAsState()
    val topics by viewModel.topics.collectAsState()
    val subjects by viewModel.subjects.collectAsState()
    val now = remember { System.currentTimeMillis() }
    // a lista é fixada ao abrir: responder muda o erro, mas a missão continua a mesma
    var missionIds by remember { mutableStateOf<List<Long>?>(null) }
    LaunchedEffect(errors) { if (missionIds == null && errors.isNotEmpty()) missionIds = errors.map { it.virada() }.let { all -> if (topicId != null) ErrorVirada.due(all.filter { it.topicId == topicId }, now).take(10) else ErrorVirada.mission(all, now) }.map { it.id } }
    var index by remember { mutableIntStateOf(0) }
    var climbed by remember { mutableIntStateOf(0) }
    val sessionId = remember { "virada-$now" }
    val scheme = MaterialTheme.colorScheme
    // O tempo da missão conta no Desempenho, como uma bateria de questões (ERROR_REVIEW, o mesmo do site)
    val results = remember { mutableStateListOf<Boolean>() }
    val topicIds = remember { mutableSetOf<Long>() }
    var saved by remember { mutableStateOf(false) }
    fun saveSession() {
        if (saved || results.isEmpty()) return
        saved = true
        val end = System.currentTimeMillis()
        val subjectIds = viewModel.topics.value.filter { it.id in topicIds }.map { it.subjectId }.distinct()
        val session = QuestionSessionEntity(
            id = sessionId, type = br.com.estudario.data.local.QuestionSessionType.ERROR_REVIEW,
            startedAt = now, completedAt = end, durationSeconds = (end - now) / 1_000,
            questionCount = results.size, correctCount = results.count { it },
            subjectIdsText = subjectIds.joinToString(","), topicIdsText = topicIds.joinToString(","),
        )
        viewModel.viewModelScope.launch { viewModel.saveQuestionSession(session) }
    }
    // saiu no meio (Sair, voltar, fechou): o que foi respondido também vale
    DisposableEffect(Unit) { onDispose { saveSession() } }
    var elapsed by remember { mutableLongStateOf(0L) }
    LaunchedEffect(Unit) { while (true) { elapsed = (System.currentTimeMillis() - now) / 1_000; kotlinx.coroutines.delay(1_000) } }
    val context = androidx.compose.ui.platform.LocalContext.current
    val ids = missionIds ?: run {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
        return
    }
    if (ids.isEmpty()) {
        Column(Modifier.fillMaxSize().padding(screenPadding()), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            EmptyState("Nada vencendo hoje", "Os erros voltam no dia certo. Volte amanhã!")
            OutlinedButton(onClick = onBack) { Text("Voltar") }
        }
        return
    }
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(screenPadding()), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        br.com.estudario.ui.components.QuestionHud(index, ids.size, results, now, title = "Missão do caderno de erros", onExit = onBack)
        if (index >= ids.size) {
            LaunchedEffect(Unit) { saveSession(); br.com.estudario.ui.brand.EstudarioSom.play(context, "resultado") }
            Column(Modifier.fillMaxWidth().padding(vertical = 24.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Folha(110.dp, mood = if (climbed > 0) FolhaMood.PROUD else FolhaMood.TALKING)
                Text(if (climbed > 0) "Virada feita!" else "Missão feita", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                Text(
                    if (climbed > 0) "Você subiu $climbed ${if (climbed == 1) "degrau" else "degraus"} hoje. Erro virado é questão a mais na prova."
                    else "Desta vez não virou, e tudo bem: eles voltam, e agora você sabe o porquê.",
                    textAlign = TextAlign.Center, style = MaterialTheme.typography.bodyMedium, color = scheme.onSurfaceVariant,
                )
                val minutes = ((elapsed + 59) / 60).toInt()
                Text("${results.count { it }} de ${results.size} certas em $minutes min. O tempo entrou no Desempenho.", textAlign = TextAlign.Center, style = MaterialTheme.typography.bodySmall, color = scheme.onSurfaceVariant)
                Button(onClick = onBack) { Icon(Icons.Outlined.Replay, null); Spacer(Modifier.width(8.dp)); Text("Voltar à Virada") }
            }
        } else key(ids[index]) {
            val start = remember { errors.firstOrNull { it.entry.id == ids[index] } }
            val question = start?.let { s -> questions.firstOrNull { it.question.id == s.entry.questionId } }
            if (start == null || question == null) LaunchedEffect(Unit) { index++ } else {
            val topic = topics.firstOrNull { it.id == question.question.topicId }
            val subject = topic?.let { t -> subjects.firstOrNull { it.id == t.subjectId } }
            MissionItem(
                viewModel, start, question, sessionId, results,
                meta = listOfNotNull(subject?.name, topic?.title, "errou ${start.entry.errorCount}×").joinToString(" · ").uppercase(),
                onAnswered = { ok -> results += ok; topicIds += question.question.topicId; if (ok) climbed++ }, onNext = { index++ },
            )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun MissionItem(
    viewModel: AppViewModel,
    start: ErrorWithQuestion,
    question: br.com.estudario.data.local.QuestionWithOptions,
    sessionId: String,
    results: List<Boolean>,
    meta: String,
    onAnswered: (Boolean) -> Unit,
    onNext: () -> Unit,
) {
    val errors by viewModel.errors.collectAsState()
    val scope = rememberCoroutineScope()
    val scheme = MaterialTheme.colorScheme
    val colors = estudarioColors()
    var picked by remember { mutableStateOf<String?>(null) }
    var right by remember { mutableStateOf<Boolean?>(null) }
    var crossed by remember { mutableStateOf(setOf<String>()) }
    var sending by remember { mutableStateOf(false) }
    var reason by remember { mutableStateOf(ErrorVirada.reasonOf(start.entry.comment)) }
    val after = errors.firstOrNull { it.entry.id == start.entry.id }
    val step = if (right == null) start.entry.retryStreak else after?.entry?.retryStreak ?: start.entry.retryStreak

    Surface(shape = EstudarioShapes.panel, color = scheme.surfaceContainerLow, modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(meta, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = scheme.onSurfaceVariant)
            Ladder(step.coerceIn(0, ErrorRetryLadder.days.size))
            MarkdownText(question.question.statement)
            // Tocar só escolhe; vale ao confirmar. A marcação antiga aparece só depois de responder.
            br.com.estudario.ui.components.AnswerOptions(
                options = question.options,
                picked = picked,
                crossed = crossed,
                answered = right,
                onPick = { picked = it },
                onToggleCross = { k -> crossed = if (k in crossed) crossed - k else crossed + k; if (picked == k) picked = null },
                previousKey = start.entry.selectedAnswer,
            )
            if (right == null) br.com.estudario.ui.components.ConfirmAnswerButton(enabled = picked != null && !sending) {
                val key = picked ?: return@ConfirmAnswerButton
                sending = true
                scope.launch {
                    val ok = viewModel.answer(question, key, sessionId)
                    right = ok
                    onAnswered(ok)
                }
            }
        }
    }

    val qid = question.question.id
    val inARow = results.asReversed().takeWhile { it }.size
    val relief = right == true && inARow == 1 && results.size >= 2 && !results[results.size - 2]
    val fire = right == true && inARow >= 3
    val context = androidx.compose.ui.platform.LocalContext.current
    if (right == true) {
        val turned = (after?.entry?.retryStreak ?: 0) >= ErrorRetryLadder.days.size
        LaunchedEffect(qid) { br.com.estudario.ui.brand.EstudarioSom.play(context, if (turned || fire) "combo" else "acerto") }
        var shown by remember { mutableStateOf(false) }
        LaunchedEffect(Unit) { shown = true }
        val pop by animateFloatAsState(if (shown) 1f else 0.7f, spring(dampingRatio = Spring.DampingRatioMediumBouncy), label = "virou")
        Surface(shape = RoundedCornerShape(16.dp), color = colors.completed.copy(alpha = 0.14f), border = BorderStroke(2.dp, colors.completed), modifier = Modifier.fillMaxWidth().scale(pop)) {
            Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                key(qid) { Folha(72.dp, mood = when { turned -> FolhaMood.ECSTATIC; fire -> FolhaMood.PROUD; relief -> FolhaMood.RELIEVED; else -> FolhaMood.HAPPY }) }
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(if (turned) "VIROU! ★" else "VIROU UM DEGRAU!", color = colors.completed, fontWeight = FontWeight.Black, fontSize = 22.sp)
                    Text(
                        when { fire -> br.com.estudario.ui.assistant.FolhaLines.forFire(qid); relief -> br.com.estudario.ui.assistant.FolhaLines.forRelief(qid); else -> br.com.estudario.ui.assistant.FolhaLines.forAnswer(true, qid) },
                        style = MaterialTheme.typography.bodyMedium,
                    )
                    val streak = after?.entry?.retryStreak ?: 1
                    Text(
                        if (turned) "Esse erro virou troféu: foi para o mural."
                        else "Agora ela volta em ${ErrorRetryLadder.intervalDays(streak) ?: 30} dias. ${if (streak == ErrorRetryLadder.days.size - 1) "Mais um acerto e vira troféu." else "Continue subindo."}",
                        style = MaterialTheme.typography.bodySmall, color = scheme.onSurfaceVariant,
                    )
                    if (inARow >= 2) Text("🔥 $inARow seguidas", color = Amber, fontWeight = FontWeight.Black, style = MaterialTheme.typography.labelLarge)
                }
            }
        }
    }
    if (right == false) Surface(shape = RoundedCornerShape(16.dp), color = scheme.surfaceContainerLow, border = BorderStroke(1.dp, scheme.outlineVariant), modifier = Modifier.fillMaxWidth()) {
        LaunchedEffect(qid) { br.com.estudario.ui.brand.EstudarioSom.play(context, "erro") }
        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            // o Folha mesmo pergunta por que errou de novo; escolhido o motivo, dá a dica certa
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                key(qid, reason) { Folha(64.dp, mood = if (reason != null) FolhaMood.TALKING else FolhaMood.SAD) }
                Text("Ainda não. Por que você errou de novo?", Modifier.weight(1f), style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
            }
            FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                ErrorVirada.Reason.entries.forEach { r ->
                    FilterChip(
                        selected = reason == r,
                        onClick = {
                            reason = r
                            val current = after?.entry ?: start.entry
                            viewModel.updateError(current.copy(comment = ErrorVirada.withReason(current.comment, r)))
                        },
                        label = { Text(r.label) },
                        colors = FilterChipDefaults.filterChipColors(selectedContainerColor = Color(0xFF8E7CFF), selectedLabelColor = Color.White),
                    )
                }
            }
            Text(reason?.let { "Dica do Folha: ${it.tip}" } ?: "Um toque já ajuda: eu aprendo o seu padrão e dou a dica certa. A questão volta para a mesa e aparece de novo em 3 dias.", style = MaterialTheme.typography.bodySmall, color = scheme.onSurfaceVariant)
        }
    }
    if (right != null && question.question.explanation.isNotBlank()) Surface(shape = EstudarioShapes.panel, color = scheme.surfaceContainerLow, modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text("Por quê", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
            MarkdownText(question.question.explanation)
        }
    }
    if (right != null) Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
        Button(onClick = onNext) { Text("Próximo erro"); Spacer(Modifier.width(6.dp)); Icon(Icons.AutoMirrored.Outlined.ArrowForward, null) }
    }
}
