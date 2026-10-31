package br.com.estudario.ui.simulation

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.WorkspacePremium
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import br.com.estudario.EstudarioApplication
import br.com.estudario.data.catalog.ExamRanking
import br.com.estudario.data.catalog.OfficialSimulation
import br.com.estudario.data.local.SimulationEntity
import br.com.estudario.data.simulation.SimulationService
import br.com.estudario.domain.simulation.BoardStyle
import br.com.estudario.domain.simulation.SimulationLock
import br.com.estudario.domain.simulation.SimulationMode
import br.com.estudario.domain.simulation.SimulationReadiness
import br.com.estudario.domain.simulation.SimulationUnlock
import br.com.estudario.ui.AppViewModel
import br.com.estudario.ui.brand.Button
import br.com.estudario.ui.brand.FilterChip
import br.com.estudario.ui.brand.Icon
import br.com.estudario.ui.brand.LinearProgressIndicator
import br.com.estudario.ui.brand.OutlinedButton
import br.com.estudario.ui.brand.Slider
import br.com.estudario.ui.components.AlertDialog
import br.com.estudario.ui.components.EmptyState
import br.com.estudario.ui.studio.Gx
import br.com.estudario.ui.studio.GxKicker
import br.com.estudario.ui.studio.GxLaunch
import br.com.estudario.ui.studio.GxPanel
import br.com.estudario.ui.studio.StudioBackdrop
import br.com.estudario.ui.theme.screenPadding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlinx.coroutines.launch

private val ACTIVE_STATUSES = setOf("GENERATING", "PARTIAL", "READY", "IN_PROGRESS", "FAILED")
private val STARTABLE_STATUSES = setOf("READY", "PARTIAL", "IN_PROGRESS")
private val CardShape = RoundedCornerShape(18.dp)

/**
 * Central de simulados, em seções fixas: o alto com banca, meta e números; o que está em andamento;
 * o Simuladão do edital (a mesma prova para todos, com ranking); o "Do que você estudou"; e o
 * histórico. No Grátis, o simulado vem no plano e as provas daqui ficam num cartão de assinatura.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SimulationsScreen(viewModel: AppViewModel, onOpenExam: (Long) -> Unit, onOpenResult: (Long) -> Unit) {
    val app = LocalContext.current.applicationContext as EstudarioApplication
    val service = app.simulationService
    val competitions by viewModel.competitions.collectAsState()
    val simulations by service.simulations.collectAsState(initial = emptyList())
    val profiles by service.examProfiles.collectAsState(initial = emptyList())
    val scope = rememberCoroutineScope()
    var competitionId by remember { mutableStateOf<Long?>(null) }
    val competition = competitions.firstOrNull { it.id == competitionId } ?: competitions.firstOrNull { it.isPrimary } ?: competitions.firstOrNull()
    if (competition == null) {
        Column(Modifier.fillMaxSize().padding(screenPadding())) {
            EmptyState("Importe um edital primeiro", "O simulado é montado a partir do edital do seu concurso: matérias, tópicos e o peso de cada um.")
        }
        return
    }
    val profile = profiles.firstOrNull { it.competitionId == competition.id }
    val target = profile?.targetPercent ?: 70
    var readiness by remember { mutableStateOf<SimulationReadiness?>(null) }
    var message by remember { mutableStateOf<String?>(null) }
    var creating by remember { mutableStateOf(false) }
    val context = LocalContext.current
    // Grátis: faz só o simulado do plano (que aparece em "Continuar"); as provas daqui são dos pagos.
    val freeTier = remember { br.com.estudario.data.planner.LibraryGaps.free(context) }
    var upgrade by remember { mutableStateOf(false) }
    var settingsOpen by remember { mutableStateOf(false) }
    var boardDialog by remember { mutableStateOf(false) }
    var targetDialog by remember { mutableStateOf(false) }
    var deleting by remember { mutableStateOf<SimulationEntity?>(null) }
    val mine = simulations.filter { it.competitionId == competition.id }
    val finished = mine.filter { it.status == "FINISHED" }.sortedByDescending { it.finishedAt ?: it.createdAt }
    LaunchedEffect(competition.id, finished.size) { readiness = service.readiness(competition.id) }
    var exams by remember(competition.id) { mutableStateOf<List<OfficialSimulation>>(emptyList()) }
    LaunchedEffect(competition.id) { exams = service.editalSimulations(competition.id) }
    val examIds = exams.map { it.id }.toSet()
    // O que está em andamento de um Simuladão aparece na própria linha da prova (não em dobro aqui).
    val active = mine.filter { it.status in ACTIVE_STATUSES && (freeTier || service.examIdOf(it) !in examIds) }
    val scores = finished.reversed().mapNotNull { it.scorePercent }

    StudioBackdrop(Modifier.fillMaxSize()) {
        LazyColumn(Modifier.fillMaxSize(), contentPadding = screenPadding(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            // 1. O alto: título, edital, banca e meta, e a faixa de números.
            item {
                ExamRoomHeader(
                    competition = competition.name,
                    board = profile?.board,
                    delivered = finished.size,
                    scores = scores,
                    target = target,
                    settingsOpen = settingsOpen || profile?.boardPending == true,
                    onToggleSettings = { settingsOpen = !settingsOpen },
                ) {
                    ExamSettings(
                        board = profile?.board,
                        pending = profile?.boardPending == true,
                        url = profile?.boardUrl,
                        style = profile?.style ?: "FIVE_OPTIONS",
                        target = target,
                        onConfirm = { scope.launch { service.confirmDetectedBoard(competition.id) } },
                        onChangeBoard = { boardDialog = true },
                        onChangeTarget = { targetDialog = true },
                    )
                }
            }
            if (competitions.size > 1) item {
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    competitions.forEach { value -> FilterChip(value.id == competition.id, { competitionId = value.id }, { Text(value.name, maxLines = 1) }) }
                }
            }
            message?.let { text ->
                item {
                    Text(text, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodyMedium)
                    if ("plano" in text || "grátis" in text) br.com.estudario.ui.plans.UpgradeCta(br.com.estudario.ui.plans.UpgradeReason.SIMULATION, Modifier.padding(top = 8.dp), openAtStart = true)
                }
            }

            // 2. Continuar: o que está sendo montado, pronto, pela metade ou com falha.
            if (active.isNotEmpty()) {
                item { SectionTitle("Continuar") }
                items(active, key = { "active-${it.id}" }) { simulation ->
                    ActiveSimulationCard(
                        simulation,
                        onStart = { scope.launch { service.start(simulation.id); onOpenExam(simulation.id) } },
                        onRetry = { service.retryFailed(simulation.id) },
                        onDelete = { deleting = simulation },
                    )
                }
            }

            if (freeTier) {
                // 6. Grátis: o simulado vem no plano; o resto fica no cartão de assinatura.
                item {
                    Text(
                        "No Grátis, o seu simulado vem no plano de estudos e aparece aqui quando for a hora de fazer.",
                        style = MaterialTheme.typography.bodyMedium, color = Gx.soft,
                    )
                }
                item { UpgradeCard { upgrade = true } }
            } else {
                // 3. Simuladão do edital: provas prontas, iguais para todos, com ranking.
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        SectionTitle("Simuladão do edital")
                        Text("A mesma prova para todos do seu edital, com ranking. Uma nova por mês.", style = MaterialTheme.typography.bodySmall, color = Gx.mute)
                    }
                }
                if (exams.isEmpty()) item {
                    PlainCard {
                        Text("Este edital ainda não tem Simuladão pronto. Ele aparece aqui assim que sair.", style = MaterialTheme.typography.bodyMedium, color = Gx.mute)
                    }
                }
                items(exams, key = { "exam-${it.id}" }) { exam ->
                    val mineOfExam = mine.filter { service.examIdOf(it) == exam.id }
                    // a primeira entrega é a que vale no ranking
                    val done = mineOfExam.filter { it.status == "FINISHED" }.minByOrNull { it.finishedAt ?: it.createdAt }
                    val open = mineOfExam.firstOrNull { it.status != "FINISHED" }
                    var ranking by remember(done?.id) { mutableStateOf<ExamRanking?>(null) }
                    if (done != null) LaunchedEffect(done.id) { ranking = runCatching { service.ranking(done.id, submit = false) }.getOrNull() }
                    ExamRow(
                        exam = exam,
                        done = done,
                        open = open,
                        ranking = ranking,
                        target = target,
                        busy = creating,
                        onCreate = {
                            creating = true
                            message = null
                            scope.launch {
                                runCatching { service.createOfficial(competition.id, exam) }
                                    .onFailure { message = it.message ?: "Não foi possível abrir o Simuladão." }
                                creating = false
                            }
                        },
                        onContinue = { sim -> scope.launch { service.start(sim.id); onOpenExam(sim.id) } },
                        onRetry = { sim -> service.retryFailed(sim.id) },
                        onDelete = { sim -> deleting = sim },
                        onResult = { sim -> onOpenResult(sim.id) },
                    )
                }

                // 4. Do que você estudou: montado só com o banco e as suas questões.
                item { SectionTitle("Do que você estudou") }
                val current = readiness
                if (current == null) item { LinearProgressIndicator(Modifier.fillMaxWidth()) }
                else item(key = "mode-studied") {
                    StudiedCard(
                        lock = SimulationUnlock.lock(SimulationMode.STUDIED, current),
                        busy = creating || active.any { it.status == "GENERATING" },
                        style = profile?.style ?: "FIVE_OPTIONS",
                    ) { size ->
                        creating = true
                        message = null
                        scope.launch {
                            runCatching { service.createForPlan(competition.id, size, diagnostic = false) }
                                .onFailure { message = it.message ?: "Não foi possível montar o simulado." }
                            creating = false
                        }
                    }
                }
            }

            // 5. Histórico: a evolução e cada prova entregue.
            if (finished.isNotEmpty()) {
                item { SectionTitle("Histórico") }
                if (scores.size >= 2) item { EvolutionChart(scores, target) }
                items(finished, key = { "done-${it.id}" }) { simulation ->
                    HistoryRow(simulation, target, onClick = { onOpenResult(simulation.id) }, onDelete = { deleting = simulation })
                }
            }
        }
    }

    if (upgrade) br.com.estudario.ui.plans.UpgradeDialog(br.com.estudario.ui.plans.UpgradeReason.SIMULATION, onDismiss = { upgrade = false })
    if (boardDialog) BoardDialog(profile?.board, onDismiss = { boardDialog = false }) { board ->
        boardDialog = false
        scope.launch { service.saveBoard(competition.id, board) }
    }
    if (targetDialog) TargetDialog(target, onDismiss = { targetDialog = false }) { value ->
        targetDialog = false
        scope.launch { service.saveTarget(competition.id, value) }
    }
    deleting?.let { simulation ->
        AlertDialog(
            onDismissRequest = { deleting = null },
            title = { Text("Excluir este simulado?") },
            text = {
                Text(
                    if (simulation.mode == SimulationService.OFFICIAL_MODE) "A prova volta para você fazer de novo; no ranking vale só a primeira entrega."
                    else "As questões dele somem do aparelho.",
                )
            },
            confirmButton = { TextButton(onClick = { scope.launch { service.delete(simulation.id) }; deleting = null }) { Text("Excluir") } },
            dismissButton = { TextButton(onClick = { deleting = null }) { Text("Manter") } },
        )
    }
}

/** Título de seção, no mesmo tom do estúdio. */
@Composable
private fun SectionTitle(text: String) {
    Text(text, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black, color = Gx.strong, modifier = Modifier.padding(top = 6.dp))
}

/** Cartão neutro do estúdio, usado pelas linhas e pelos cartões da tela. */
@Composable
private fun PlainCard(modifier: Modifier = Modifier, accent: Color? = null, onClick: (() -> Unit)? = null, content: @Composable () -> Unit) {
    Box(
        modifier.fillMaxWidth().clip(CardShape)
            .background(accent?.copy(alpha = 0.08f) ?: Gx.tint(0.04f))
            .border(1.dp, accent?.copy(alpha = 0.32f) ?: Gx.tint(0.09f), CardShape)
            .let { if (onClick != null) it.clickable(onClick = onClick) else it }
            .padding(14.dp),
    ) { content() }
}

/** Selo de nota: verde na meta, dourado perto dela, vermelho longe. */
@Composable
private fun ScoreBadge(score: Int, target: Int) {
    val color = scoreColor(score, target)
    Text(
        "$score%",
        Modifier.clip(RoundedCornerShape(10.dp)).background(color.copy(alpha = 0.16f)).padding(horizontal = 10.dp, vertical = 4.dp),
        style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Black, color = Gx.readable(color),
    )
}

private fun scoreColor(score: Int, target: Int): Color = when {
    score >= target -> Gx.mint
    score >= target - 10 -> Gx.gold
    else -> Gx.red
}

private fun styleOf(format: String?): String = when (format) {
    "CERTO_ERRADO" -> "TRUE_FALSE"
    "MISTO" -> "MIXED"
    else -> "FIVE_OPTIONS"
}

/** O alto da sala de prova: título, edital e banca, o atalho para banca e meta e a faixa de números. */
@Composable
private fun ExamRoomHeader(
    competition: String,
    board: String?,
    delivered: Int,
    scores: List<Int>,
    target: Int,
    settingsOpen: Boolean,
    onToggleSettings: () -> Unit,
    settings: @Composable () -> Unit,
) {
    val last = scores.lastOrNull()
    val best = scores.maxOrNull()
    val delta = if (scores.size >= 2) scores.last() - scores.first() else null
    GxPanel {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text("Simulados", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Black, color = Gx.strong)
                    Text(
                        competition + (board?.let { " · $it" } ?: ""),
                        style = MaterialTheme.typography.bodySmall, color = Gx.mute, maxLines = 2, overflow = TextOverflow.Ellipsis,
                    )
                }
                TextButton(onClick = onToggleSettings) { Text(if (settingsOpen) "Fechar" else "Banca e meta") }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                RoomStat("$delivered", if (delivered == 1) "entregue" else "entregues", Gx.strong, Modifier.weight(1f))
                RoomStat(best?.let { "$it%" } ?: "-", "melhor nota", Gx.strong, Modifier.weight(1f))
                RoomStat(last?.let { "$it%" } ?: "-", "última · meta $target%", last?.let { Gx.readable(scoreColor(it, target)) } ?: Gx.strong, Modifier.weight(1f))
                RoomStat(delta?.let { if (it >= 0) "+$it" else "$it" } ?: "-", "evolução", delta?.let { Gx.readable(if (it >= 0) Gx.mint else Gx.red) } ?: Gx.strong, Modifier.weight(1f))
            }
            if (settingsOpen) settings()
        }
    }
}

@Composable
private fun RoomStat(value: String, label: String, color: Color, modifier: Modifier) {
    Column(
        modifier.clip(RoundedCornerShape(12.dp)).background(Gx.tint(0.05f)).padding(horizontal = 8.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black, color = color, maxLines = 1)
        Text(label, style = MaterialTheme.typography.labelSmall, color = Gx.mute, maxLines = 2, overflow = TextOverflow.Ellipsis)
    }
}

/** Banca e meta, dentro do alto da tela (abre pelo "Banca e meta" ou quando há banca para confirmar). */
@Composable
private fun ExamSettings(board: String?, pending: Boolean, url: String?, style: String, target: Int, onConfirm: () -> Unit, onChangeBoard: () -> Unit, onChangeTarget: () -> Unit) {
    val uri = LocalUriHandler.current
    Column(
        Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp)).background(Gx.tint(0.05f)).padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text(if (board == null) "Banca ainda não definida" else "Banca: $board", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = Gx.strong)
                Text(
                    if (board == null) "Escolha a banca para as questões seguirem o estilo dela."
                    else "${BoardStyle.styleLabel(style)} · no jeito que a $board cobra",
                    style = MaterialTheme.typography.bodySmall, color = Gx.mute,
                )
            }
            TextButton(onClick = onChangeBoard) { Text(if (board == null) "Escolher" else "Trocar") }
        }
        if (pending && board != null) {
            Text("O Estudário encontrou a banca $board na internet. Está certo?", style = MaterialTheme.typography.bodyMedium, color = Gx.ink)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                Button(onClick = onConfirm) { Text("Confirmar") }
                OutlinedButton(onClick = onChangeBoard) { Text("Não é essa") }
                url?.let { TextButton(onClick = { runCatching { uri.openUri(it) } }) { Text("Fonte") } }
            }
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Meta de acerto: $target%", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = Gx.strong, modifier = Modifier.weight(1f))
            TextButton(onClick = onChangeTarget) { Text("Ajustar") }
        }
    }
}

/** Situação curta de um simulado que ainda não foi entregue. */
private fun statusText(simulation: SimulationEntity, failedError: String?): String = when (simulation.status) {
    "GENERATING" -> "Preparando as questões… Pode fechar o app: avisamos quando ficar pronto."
    "PARTIAL" -> "${simulation.questionCount} questões prontas; uma parte não saiu."
    "READY" -> "Pronto · ${simulation.questionCount} questões · ${simulation.timeLimitMinutes} min"
    "IN_PROGRESS" -> "Em andamento · ${simulation.answeredCount} de ${simulation.questionCount} respondidas"
    else -> failedError ?: "Não foi possível preparar."
}

@Composable
private fun ActiveSimulationCard(simulation: SimulationEntity, onStart: () -> Unit, onRetry: () -> Unit, onDelete: () -> Unit) {
    val parts = SimulationService.decodeParts(simulation.partsJson)
    val done = parts.count { it.status == "DONE" }
    val failed = parts.filter { it.status == "FAILED" }
    PlainCard(accent = if (simulation.status == "FAILED") Gx.red else Gx.violet) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(verticalAlignment = Alignment.Top) {
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(simulation.title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = Gx.strong)
                    Text(statusText(simulation, failed.firstOrNull()?.error), style = MaterialTheme.typography.bodySmall, color = Gx.mute)
                }
                IconButton(onClick = onDelete, modifier = Modifier.size(36.dp)) { Icon(Icons.Outlined.Delete, "Excluir", tint = Gx.mute) }
            }
            if (simulation.status == "GENERATING") {
                LinearProgressIndicator({ (done + 0.35f) / parts.size.coerceAtLeast(1) }, Modifier.fillMaxWidth(), strokeCap = StrokeCap.Round)
            }
            if (simulation.status == "IN_PROGRESS" && simulation.questionCount > 0) {
                br.com.estudario.ui.studio.GxBar(simulation.answeredCount.toFloat() / simulation.questionCount, Gx.violet, 6.dp)
            }
            when {
                simulation.status in STARTABLE_STATUSES -> GxLaunch(
                    if (simulation.status == "IN_PROGRESS") "Continuar" else "Abrir",
                    br.com.estudario.ui.brand.Glyph.Play, onClick = onStart,
                )
                simulation.status == "FAILED" && failed.isNotEmpty() -> OutlinedButton(onClick = onRetry, modifier = Modifier.fillMaxWidth()) { Text("Tentar de novo") }
            }
        }
    }
}

/**
 * Uma prova do Simuladão do edital: nome, tamanho, tempo e banca, e o estado da pessoa nela (por
 * fazer, em andamento ou entregue, com a nota e a posição no ranking).
 */
@Composable
private fun ExamRow(
    exam: OfficialSimulation,
    done: SimulationEntity?,
    open: SimulationEntity?,
    ranking: ExamRanking?,
    target: Int,
    busy: Boolean,
    onCreate: () -> Unit,
    onContinue: (SimulationEntity) -> Unit,
    onRetry: (SimulationEntity) -> Unit,
    onDelete: (SimulationEntity) -> Unit,
    onResult: (SimulationEntity) -> Unit,
) {
    val count = exam.questions.size
    PlainCard(accent = Gx.gold, onClick = done?.let { { onResult(it) } }) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Outlined.WorkspacePremium, null, tint = Gx.readable(Gx.gold))
                Spacer(Modifier.width(10.dp))
                Column(Modifier.weight(1f)) {
                    Text(SimulationService.examTitle(exam.number), style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Black, color = Gx.strong)
                    Text(
                        listOfNotNull("$count questões", "${BoardStyle.minutesFor(styleOf(exam.format), count)} min", exam.board).joinToString(" · "),
                        style = MaterialTheme.typography.labelMedium, color = Gx.mute,
                    )
                }
                done?.scorePercent?.let { ScoreBadge(it, target) }
                val removable = done ?: open
                if (removable != null) IconButton(onClick = { onDelete(removable) }, modifier = Modifier.size(36.dp)) { Icon(Icons.Outlined.Delete, "Excluir", tint = Gx.mute) }
            }
            if (done == null && exam.format == "CERTO_ERRADO") Text(
                "Regra Cebraspe: cada item errado anula um certo.",
                style = MaterialTheme.typography.labelSmall, color = Gx.mute,
            )
            when {
                done != null -> {
                    ranking?.takeIf { it.participants > 0 }?.let { r ->
                        Text(
                            listOfNotNull(
                                r.aheadPercent?.let { "à frente de $it% de quem fez" },
                                "média ${r.average}%",
                                "${r.participants} ${if (r.participants == 1) "participante" else "participantes"}",
                            ).joinToString(" · "),
                            style = MaterialTheme.typography.labelMedium, color = Gx.soft,
                        )
                    }
                    OutlinedButton(onClick = { onResult(done) }, modifier = Modifier.fillMaxWidth()) { Text("Ver resultado") }
                }
                open != null -> {
                    Text(statusText(open, null), style = MaterialTheme.typography.bodySmall, color = Gx.mute)
                    when (open.status) {
                        in STARTABLE_STATUSES -> GxLaunch("Continuar", br.com.estudario.ui.brand.Glyph.Play, colors = listOf(Color(0xFFFFE08A), Gx.gold), ink = Color(0xFF1A1440)) { onContinue(open) }
                        "FAILED" -> OutlinedButton(onClick = { onRetry(open) }, modifier = Modifier.fillMaxWidth()) { Text("Tentar de novo") }
                        else -> LinearProgressIndicator(Modifier.fillMaxWidth())
                    }
                }
                else -> GxLaunch("Fazer a prova", br.com.estudario.ui.brand.Glyph.Play, enabled = !busy, colors = listOf(Color(0xFFFFE08A), Gx.gold), ink = Color(0xFF1A1440), onClick = onCreate)
            }
        }
    }
}

/** "Do que você estudou": 20 ou 30 questões do que a pessoa já estudou; trancado até ter base. */
@Composable
private fun StudiedCard(lock: SimulationLock, busy: Boolean, style: String, onCreate: (Int) -> Unit) {
    var size by remember { mutableIntStateOf(20) }
    PlainCard(accent = if (lock.unlocked) Gx.mint else null) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(
                "Questões do banco sobre os tópicos que você já estudou, para medir o que ficou.",
                style = MaterialTheme.typography.bodySmall, color = Gx.soft,
            )
            if (!lock.unlocked) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Outlined.Lock, null, Modifier.size(18.dp), tint = Gx.mute)
                    Spacer(Modifier.width(8.dp))
                    Text(lock.requirement, style = MaterialTheme.typography.labelMedium, color = Gx.mute, modifier = Modifier.weight(1f))
                }
                br.com.estudario.ui.studio.GxBar(lock.progress.coerceIn(0f, 1f), Gx.mint, 8.dp)
                return@Column
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                listOf(20, 30).forEach { value -> FilterChip(size == value, { size = value }, { Text("$value questões") }) }
                Spacer(Modifier.weight(1f))
                Text("${BoardStyle.minutesFor(style, size)} min", style = MaterialTheme.typography.labelMedium, color = Gx.mute)
            }
            GxLaunch("Montar", br.com.estudario.ui.brand.Glyph.Play, enabled = !busy, colors = listOf(Gx.mint.copy(alpha = 0.85f), Gx.mint), ink = Color(0xFF1A1440)) { onCreate(size) }
        }
    }
}

/** Grátis: o que vem com a assinatura, num cartão só. */
@Composable
private fun UpgradeCard(onClick: () -> Unit) {
    PlainCard(accent = Gx.gold) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            GxKicker("Com a assinatura", Gx.readable(Gx.gold))
            Text("Treine com provas do seu edital", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black, color = Gx.strong)
            UpgradeLine("Simuladão do edital", "A mesma prova para todos, com ranking. Uma nova por mês.")
            UpgradeLine("Do que você estudou", "Um simulado de 20 ou 30 questões sobre o que você já viu.")
            GxLaunch("Conhecer os planos", br.com.estudario.ui.brand.Glyph.Play, colors = listOf(Color(0xFFFFE08A), Gx.gold), ink = Color(0xFF1A1440), onClick = onClick)
        }
    }
}

@Composable
private fun UpgradeLine(title: String, text: String) {
    Row(verticalAlignment = Alignment.Top) {
        Box(Modifier.padding(top = 6.dp).size(8.dp).clip(RoundedCornerShape(4.dp)).background(Gx.gold))
        Spacer(Modifier.width(10.dp))
        Column {
            Text(title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = Gx.strong)
            Text(text, style = MaterialTheme.typography.bodySmall, color = Gx.mute)
        }
    }
}

@Composable
private fun HistoryRow(simulation: SimulationEntity, target: Int, onClick: () -> Unit, onDelete: () -> Unit) {
    val date = remember(simulation.finishedAt) { SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR")).format(Date(simulation.finishedAt ?: simulation.createdAt)) }
    val minutes = (simulation.elapsedSeconds / 60).coerceAtLeast(if (simulation.elapsedSeconds > 0) 1 else 0)
    PlainCard(onClick = onClick) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(simulation.title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold, color = Gx.strong, maxLines = 2, overflow = TextOverflow.Ellipsis)
                Text(
                    listOfNotNull(date, "${simulation.correctCount} de ${simulation.questionCount}", minutes.takeIf { it > 0 }?.let { "$it min" }).joinToString(" · "),
                    style = MaterialTheme.typography.bodySmall, color = Gx.mute,
                )
            }
            Spacer(Modifier.width(10.dp))
            ScoreBadge(simulation.scorePercent ?: 0, target)
            IconButton(onClick = onDelete, modifier = Modifier.size(36.dp)) { Icon(Icons.Outlined.Delete, "Excluir", tint = Gx.mute) }
        }
    }
}

/** Linha das notas, com a meta tracejada: a pessoa vê se está subindo e quanto falta. */
@Composable
fun EvolutionChart(scores: List<Int>, target: Int, modifier: Modifier = Modifier) {
    val line = Gx.violet
    val goal = Gx.mint
    val grid = Gx.tint(0.1f)
    Box(modifier.fillMaxWidth().clip(CardShape).background(Gx.tint(0.04f)).border(1.dp, Gx.tint(0.09f), CardShape).padding(14.dp)) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            val first = scores.first()
            val last = scores.last()
            Text(
                when {
                    last > first -> "Você subiu ${last - first} pontos desde o primeiro simulado."
                    last < first -> "Caiu ${first - last} pontos desde o primeiro. Revisar os tópicos mais fracos ajuda a recuperar."
                    else -> "Nota estável desde o primeiro simulado."
                },
                style = MaterialTheme.typography.bodyMedium, color = Gx.ink,
            )
            Canvas(Modifier.fillMaxWidth().height(100.dp)) {
                val stepX = if (scores.size > 1) size.width / (scores.size - 1) else size.width
                fun y(value: Int) = size.height - value / 100f * size.height
                drawLine(grid, Offset(0f, y(50)), Offset(size.width, y(50)), strokeWidth = 1f)
                drawLine(goal, Offset(0f, y(target)), Offset(size.width, y(target)), strokeWidth = 3f, pathEffect = androidx.compose.ui.graphics.PathEffect.dashPathEffect(floatArrayOf(12f, 10f)))
                scores.zipWithNext().forEachIndexed { index, (a, b) ->
                    drawLine(line, Offset(index * stepX, y(a)), Offset((index + 1) * stepX, y(b)), strokeWidth = 6f, cap = StrokeCap.Round)
                }
                scores.forEachIndexed { index, value -> drawCircle(line, 8f, Offset(index * stepX, y(value))) }
            }
            Text("Linha tracejada: sua meta de $target%", style = MaterialTheme.typography.labelSmall, color = Gx.mute)
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun BoardDialog(current: String?, onDismiss: () -> Unit, onSave: (String?) -> Unit) {
    var value by remember { mutableStateOf(current.orEmpty()) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Qual é a banca?") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("Está no edital, normalmente na primeira página. As questões seguem o estilo dela.", style = MaterialTheme.typography.bodySmall)
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    BoardStyle.KNOWN_BOARDS.forEach { board -> FilterChip(value == board, { value = board }, { Text(board) }) }
                }
                OutlinedTextField(value, { value = it.take(80) }, Modifier.fillMaxWidth(), label = { Text("Outra banca") }, singleLine = true)
            }
        },
        confirmButton = { TextButton(onClick = { onSave(value.trim().ifBlank { null }) }) { Text("Salvar") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } },
    )
}

@Composable
private fun TargetDialog(current: Int, onDismiss: () -> Unit, onSave: (Int) -> Unit) {
    var value by remember { mutableIntStateOf(current) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Meta de acerto: $value%") },
        text = {
            Column {
                Text("Use a nota de corte da última prova do seu cargo, se souber. Sem ela, 70% é uma boa referência para prova objetiva.", style = MaterialTheme.typography.bodySmall)
                Slider(value.toFloat(), { value = (it / 5).toInt() * 5 }, valueRange = 40f..100f, steps = 11)
            }
        },
        confirmButton = { TextButton(onClick = { onSave(value) }) { Text("Salvar") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } },
    )
}
