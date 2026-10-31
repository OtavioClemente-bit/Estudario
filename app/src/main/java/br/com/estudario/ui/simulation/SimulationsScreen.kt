package br.com.estudario.ui.simulation

import androidx.compose.foundation.layout.Box
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.background
import androidx.compose.foundation.border

import br.com.estudario.ui.components.AlertDialog
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Gavel
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material.icons.outlined.WorkspacePremium
import br.com.estudario.data.catalog.OfficialSimulation
import br.com.estudario.ui.brand.Button
import androidx.compose.material3.CardDefaults
import br.com.estudario.ui.brand.ElevatedCard
import br.com.estudario.ui.brand.FilterChip
import br.com.estudario.ui.brand.Icon
import androidx.compose.material3.IconButton
import br.com.estudario.ui.brand.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import br.com.estudario.ui.brand.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import br.com.estudario.ui.brand.Slider
import br.com.estudario.ui.brand.Surface
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.estudario.EstudarioApplication
import br.com.estudario.data.local.SimulationEntity
import br.com.estudario.data.simulation.SimulationService
import br.com.estudario.domain.simulation.BoardStyle
import br.com.estudario.domain.simulation.SimulationLock
import br.com.estudario.domain.simulation.SimulationMode
import br.com.estudario.domain.simulation.SimulationReadiness
import br.com.estudario.domain.simulation.SimulationUnlock
import br.com.estudario.ui.AppViewModel
import br.com.estudario.ui.components.EmptyState
import br.com.estudario.ui.plans.AiPlanLoadResult
import br.com.estudario.ui.theme.EstudarioShapes
import br.com.estudario.ui.theme.estudarioColors
import br.com.estudario.ui.theme.screenPadding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlinx.coroutines.launch

/**
 * Central de simulados: banca e meta da prova, os quatro tipos com o que falta para liberar cada
 * um, o que está sendo gerado agora e o histórico com a evolução das notas.
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
    var readiness by remember { mutableStateOf<SimulationReadiness?>(null) }
    var remainingParts by remember { mutableStateOf<Int?>(null) }
    var message by remember { mutableStateOf<String?>(null) }
    var creating by remember { mutableStateOf(false) }
    val context = androidx.compose.ui.platform.LocalContext.current
    // Grátis: faz só o simulado do plano (que aparece aqui enquanto é montado); montar provas é dos pagos.
    val freeTier = remember { br.com.estudario.data.planner.LibraryGaps.free(context) }
    var upgrade by remember { mutableStateOf(false) }
    var boardDialog by remember { mutableStateOf(false) }
    var targetDialog by remember { mutableStateOf(false) }
    var deleting by remember { mutableStateOf<SimulationEntity?>(null) }
    val mine = simulations.filter { it.competitionId == competition.id }
    val finishedCount = mine.count { it.status == "FINISHED" }
    LaunchedEffect(competition.id, finishedCount) { readiness = service.readiness(competition.id) }
    LaunchedEffect(Unit) {
        remainingParts = (app.aiPlanRepository.load() as? AiPlanLoadResult.Available)
            ?.summary?.usage?.firstOrNull { it.feature == "SIMULATION_GENERATION" }?.remaining
    }
    val active = mine.filter { it.status in setOf("GENERATING", "PARTIAL", "READY", "IN_PROGRESS", "FAILED") }
    val finished = mine.filter { it.status == "FINISHED" }
    // Prova oficial do edital (quando o concurso veio do catálogo e o edital tem prova pronta): é o
    // primeiro simulado; depois de feita, os próximos saem do banco de questões.
    var official by remember(competition.id) { mutableStateOf<OfficialSimulation?>(null) }
    LaunchedEffect(competition.id) { official = service.officialSimulation(competition.id) }
    val officialDone = mine.any { it.mode == SimulationService.OFFICIAL_MODE }

    // A sala de prova (como no site): o carro-chefe, em console escuro. No alto, o placar com a
    // última nota contra a meta e os números; depois as provas em cartões coloridos.
    val scores = finished.sortedBy { it.finishedAt ?: it.createdAt }.mapNotNull { it.scorePercent }
    br.com.estudario.ui.studio.StudioBackdrop(Modifier.fillMaxSize()) {
    LazyColumn(Modifier.fillMaxSize(), contentPadding = screenPadding(), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        item { ExamRoomHeader(competition.name, profile?.board, finished.size, scores, profile?.targetPercent ?: 70) }
        if (competitions.size > 1) item {
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                competitions.forEach { value -> FilterChip(value.id == competition.id, { competitionId = value.id }, { Text(value.name, maxLines = 1) }) }
            }
        }
        item { ExamCard(profile?.board, profile?.boardPending == true, profile?.boardUrl, profile?.style ?: "FIVE_OPTIONS", profile?.targetPercent ?: 70,
            onConfirm = { scope.launch { service.confirmDetectedBoard(competition.id) } },
            onChangeBoard = { boardDialog = true },
            onChangeTarget = { targetDialog = true },
        ) }
        message?.let { text ->
            item {
                Text(text, color = MaterialTheme.colorScheme.error)
                if ("plano" in text || "grátis" in text) br.com.estudario.ui.plans.UpgradeCta(br.com.estudario.ui.plans.UpgradeReason.SIMULATION, Modifier.padding(top = 8.dp), openAtStart = true)
            }
        }
        items(active, key = { "active-${it.id}" }) { simulation ->
            ActiveSimulationCard(
                simulation,
                onStart = { scope.launch { service.start(simulation.id); onOpenExam(simulation.id) } },
                onRetry = { service.retryFailed(simulation.id) },
                onDelete = { deleting = simulation },
            )
        }
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    br.com.estudario.ui.studio.GxKicker("Provas")
                    Text("Monte um simulado", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black, color = br.com.estudario.ui.studio.Gx.strong)
                }
                remainingParts?.takeIf { !freeTier }?.let {
                    Text("Saldo: ${it * 30} questões", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
                }
            }
        }
        val current = readiness
        if (current == null) item { LinearProgressIndicator(Modifier.fillMaxWidth()) }
        // Revanche saiu: erro de simulado não vai para o caderno, e o treino dos erros é do caderno.
        else SimulationMode.entries.filter { it != SimulationMode.REMATCH }.forEach { mode ->
            item(key = "mode-${mode.name}") {
                // A primeira prova completa de um edital com prova pronta é a prova oficial.
                val prova = official?.takeIf { mode == SimulationMode.FULL && !officialDone }
                if (prova != null) OfficialFullCard(
                    official = prova,
                    lock = SimulationUnlock.lock(mode, current),
                    busy = creating || active.any { it.status == "GENERATING" },
                ) {
                    if (freeTier) { upgrade = true; return@OfficialFullCard }
                    creating = true
                    message = null
                    scope.launch {
                        runCatching { service.createOfficial(competition.id, prova) }
                            .onFailure { message = it.message ?: "Não foi possível montar a prova oficial." }
                        creating = false
                    }
                } else ModeCard(
                    mode = mode,
                    lock = SimulationUnlock.lock(mode, current),
                    sizes = SimulationUnlock.sizes(mode, remainingParts?.let { it * 30 }),
                    rematchCount = if (mode == SimulationMode.REMATCH) current.wrongInFinished else null,
                    busy = creating || active.any { it.status == "GENERATING" },
                    style = profile?.style ?: "FIVE_OPTIONS",
                    onGenerate = generate@{ size ->
                        if (freeTier) { upgrade = true; return@generate }
                        creating = true
                        message = null
                        scope.launch {
                            runCatching { service.create(competition.id, mode, size) }
                                .onFailure { message = it.message ?: "Não foi possível montar o simulado." }
                            creating = false
                        }
                    },
                )
            }
        }
        if (finished.isNotEmpty()) {
            item { br.com.estudario.ui.studio.GxKicker("Sua evolução e histórico") }
            if (finished.size >= 2) item { EvolutionChart(finished.sortedBy { it.finishedAt }.mapNotNull { it.scorePercent }, profile?.targetPercent ?: 70) }
            items(finished, key = { "done-${it.id}" }) { simulation -> HistoryRow(simulation, profile?.targetPercent ?: 70) { onOpenResult(simulation.id) } }
        }
    }
    }

    if (upgrade) br.com.estudario.ui.plans.UpgradeDialog(br.com.estudario.ui.plans.UpgradeReason.SIMULATION, onDismiss = { upgrade = false })
    if (boardDialog) BoardDialog(profile?.board, onDismiss = { boardDialog = false }) { board ->
        boardDialog = false
        scope.launch { service.saveBoard(competition.id, board) }
    }
    if (targetDialog) TargetDialog(profile?.targetPercent ?: 70, onDismiss = { targetDialog = false }) { value ->
        targetDialog = false
        scope.launch { service.saveTarget(competition.id, value) }
    }
    deleting?.let { simulation ->
        AlertDialog(
            onDismissRequest = { deleting = null },
            title = { Text("Descartar este simulado?") },
            text = { Text(if (simulation.mode == SimulationService.OFFICIAL_MODE) "As questões dele somem. A prova oficial volta a aparecer para você preparar de novo." else "As questões dele somem. A cota já usada não volta.") },
            confirmButton = { TextButton(onClick = { scope.launch { service.delete(simulation.id) }; deleting = null }) { Text("Descartar") } },
            dismissButton = { TextButton(onClick = { deleting = null }) { Text("Manter") } },
        )
    }
}

@Composable
private fun ExamCard(board: String?, pending: Boolean, url: String?, style: String, target: Int, onConfirm: () -> Unit, onChangeBoard: () -> Unit, onChangeTarget: () -> Unit) {
    val uri = LocalUriHandler.current
    Surface(shape = EstudarioShapes.spotlight, color = MaterialTheme.colorScheme.primaryContainer, modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Outlined.Gavel, null, tint = MaterialTheme.colorScheme.primary)
                Spacer(Modifier.width(10.dp))
                Column(Modifier.weight(1f)) {
                    Text(if (board == null) "Banca ainda não definida" else "Banca: $board", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text(
                        if (board == null) "Escolha a banca, ou o Estudário descobre no primeiro simulado."
                        else "${BoardStyle.styleLabel(style)} · questões no jeito que a $board cobra",
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
                TextButton(onClick = onChangeBoard) { Text(if (board == null) "Escolher" else "Trocar") }
            }
            if (pending && board != null) {
                Surface(color = MaterialTheme.colorScheme.surface, shape = MaterialTheme.shapes.medium) {
                    Column(Modifier.fillMaxWidth().padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text("O Estudário encontrou a banca $board na internet. Está certo?", style = MaterialTheme.typography.bodyMedium)
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(onClick = onConfirm) { Text("Confirmar") }
                            OutlinedButton(onClick = onChangeBoard) { Text("Não é essa") }
                            url?.let { TextButton(onClick = { runCatching { uri.openUri(it) } }) { Text("Ver fonte") } }
                        }
                    }
                }
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Sua meta de acerto: $target%", style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(1f))
                TextButton(onClick = onChangeTarget) { Text("Ajustar") }
            }
        }
    }
}

/**
 * "Prova completa" de um edital com prova pronta, enquanto a pessoa ainda não fez a primeira: no
 * mesmo lugar e com a mesma liberação da prova completa, mas é a prova oficial (ordem, proporção e
 * tempo da prova real). As próximas provas completas são geradas normalmente.
 */
@Composable
private fun OfficialFullCard(official: OfficialSimulation, lock: SimulationLock, busy: Boolean, onCreate: () -> Unit) {
    val style = when (official.format) {
        "CERTO_ERRADO" -> "TRUE_FALSE"
        "MISTO" -> "MIXED"
        else -> "FIVE_OPTIONS"
    }
    val count = official.questions.size
    val subjects = official.questions.groupingBy { it.subject }.eachCount().entries.sortedByDescending { it.value }
    ElevatedCard(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(if (lock.unlocked) Icons.Outlined.WorkspacePremium else Icons.Outlined.Lock, null, tint = if (lock.unlocked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline)
                Spacer(Modifier.width(10.dp))
                Text(SimulationMode.FULL.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                Surface(color = MaterialTheme.colorScheme.tertiaryContainer, shape = MaterialTheme.shapes.small) {
                    Text("Prova oficial", Modifier.padding(horizontal = 8.dp, vertical = 2.dp), style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                }
            }
            Text(
                "Sua primeira prova completa é igual à prova do concurso: as $count questões na mesma ordem, na mesma proporção por matéria e no tempo da banca. " +
                    "As próximas provas completas são montadas do seu edital inteiro, com questões novas.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                subjects.take(6).joinToString(" · ") { "${it.key} ${it.value}" } + if (subjects.size > 6) " · +${subjects.size - 6} matérias" else "",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            if (!lock.unlocked) {
                LinearProgressIndicator({ lock.progress.coerceIn(0f, 1f) }, Modifier.fillMaxWidth(), strokeCap = StrokeCap.Round)
                Text(lock.requirement, style = MaterialTheme.typography.labelMedium)
                return@Column
            }
            if (official.format == "CERTO_ERRADO") Text(
                "Correção na regra Cebraspe: cada item errado anula um certo. Na dúvida, deixar em branco pode valer mais.",
                style = MaterialTheme.typography.labelMedium,
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Outlined.Timer, null, Modifier.size(16.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(Modifier.width(4.dp))
                Text(listOfNotNull("${BoardStyle.minutesFor(style, count)} min de prova", official.board, BoardStyle.styleLabel(style), "não usa a cota").joinToString(" · "), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Button(onClick = onCreate, enabled = !busy, modifier = Modifier.fillMaxWidth()) { Text("Preparar a prova completa") }
        }
    }
}

@Composable
private fun ActiveSimulationCard(simulation: SimulationEntity, onStart: () -> Unit, onRetry: () -> Unit, onDelete: () -> Unit) {
    val parts = SimulationService.decodeParts(simulation.partsJson)
    val done = parts.count { it.status == "DONE" }
    val failed = parts.filter { it.status == "FAILED" }
    ElevatedCard(colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer), modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text(simulation.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text(
                        when (simulation.status) {
                            "GENERATING" -> "Montando as questões${simulation.board?.let { " no estilo $it" } ?: ""}, conferindo leis e gabaritos… Pode fechar o app: avisamos quando ficar pronto."
                            "PARTIAL" -> "${simulation.questionCount} questões prontas. ${failed.size} parte(s) não saíram."
                            "READY" -> if (simulation.mode == SimulationService.OFFICIAL_MODE) "${simulation.questionCount} questões na ordem da prova real · ${simulation.timeLimitMinutes} min de prova"
                                else "${simulation.questionCount} questões inéditas · ${simulation.timeLimitMinutes} min de prova"
                            "IN_PROGRESS" -> "Em andamento: ${simulation.answeredCount} de ${simulation.questionCount} respondidas"
                            else -> failed.firstOrNull()?.error ?: "Não foi possível gerar."
                        },
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
                IconButton(onClick = onDelete) { Icon(Icons.Outlined.Delete, "Descartar") }
            }
            if (simulation.status == "GENERATING") {
                LinearProgressIndicator({ (done + 0.35f) / parts.size.coerceAtLeast(1) }, Modifier.fillMaxWidth())
                Text("Parte ${done + 1} de ${parts.size}", style = MaterialTheme.typography.labelSmall)
            }
            failed.firstOrNull()?.error?.takeIf { simulation.status == "PARTIAL" }?.let { Text(it, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.error) }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                if (simulation.status in setOf("READY", "PARTIAL", "IN_PROGRESS")) Button(onClick = onStart) {
                    Icon(Icons.Outlined.PlayArrow, null); Spacer(Modifier.width(6.dp))
                    Text(if (simulation.status == "IN_PROGRESS") "Continuar" else if (simulation.status == "PARTIAL") "Começar com ${simulation.questionCount}" else "Começar a prova")
                }
                if (failed.isNotEmpty() && simulation.status in setOf("PARTIAL", "FAILED")) OutlinedButton(onClick = onRetry) {
                    Icon(Icons.Outlined.Refresh, null); Spacer(Modifier.width(6.dp)); Text("Gerar o que faltou")
                }
            }
        }
    }
}

@Composable
private fun ModeCard(mode: SimulationMode, lock: SimulationLock, sizes: List<Int>, rematchCount: Int?, busy: Boolean, style: String, onGenerate: (Int) -> Unit) {
    var size by remember(sizes) { mutableIntStateOf(sizes.first()) }
    val color = modeColor(mode)
    val shape = androidx.compose.foundation.shape.RoundedCornerShape(22.dp)
    Column(
        Modifier.fillMaxWidth().clip(shape)
            .background(androidx.compose.ui.graphics.Brush.linearGradient(listOf(color.copy(alpha = if (lock.unlocked) 0.20f else 0.06f), br.com.estudario.ui.studio.Gx.bg2, br.com.estudario.ui.studio.Gx.bg)))
            .border(1.5.dp, if (lock.unlocked) color.copy(alpha = 0.5f) else br.com.estudario.ui.studio.Gx.tint(0.1f), shape)
            .padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Row(verticalAlignment = Alignment.Top) {
            Box(
                Modifier.size(58.dp).clip(androidx.compose.foundation.shape.RoundedCornerShape(18.dp)).background(color.copy(alpha = 0.18f))
                    .border(1.5.dp, color.copy(alpha = 0.5f), androidx.compose.foundation.shape.RoundedCornerShape(18.dp)),
                contentAlignment = Alignment.Center,
            ) { br.com.estudario.ui.brand.BrandIcon(if (lock.unlocked) modeGlyph(mode) else br.com.estudario.ui.brand.Glyph.Lock, size = 46.dp, muted = !lock.unlocked) }
            Spacer(Modifier.width(14.dp))
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(mode.title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black, color = br.com.estudario.ui.studio.Gx.strong)
                    if (!lock.unlocked) {
                        Spacer(Modifier.width(8.dp))
                        Text("TRANCADO", Modifier.clip(androidx.compose.foundation.shape.RoundedCornerShape(6.dp)).background(br.com.estudario.ui.studio.Gx.tint(0.1f)).padding(horizontal = 7.dp, vertical = 2.dp),
                            style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Black, color = br.com.estudario.ui.studio.Gx.mute)
                    }
                }
                Text(mode.description, style = MaterialTheme.typography.bodySmall, color = br.com.estudario.ui.studio.Gx.soft)
            }
        }
        if (!lock.unlocked) {
            br.com.estudario.ui.studio.GxBar(lock.progress, color, 10.dp)
            Text(lock.requirement, style = MaterialTheme.typography.labelMedium, color = br.com.estudario.ui.studio.Gx.mute)
            return@Column
        }
        if (mode == SimulationMode.REMATCH) {
            val count = (rematchCount ?: 0).coerceAtMost(30)
            Text("$count questão(ões) para refazer com outro cenário.", style = MaterialTheme.typography.bodySmall, color = br.com.estudario.ui.studio.Gx.mute)
            br.com.estudario.ui.studio.GxLaunch("Gerar revanche", br.com.estudario.ui.brand.Glyph.Cycle, enabled = !busy && count > 0, colors = listOf(color.copy(alpha = 0.85f), color), ink = androidx.compose.ui.graphics.Color(0xFF1A1440)) { onGenerate(count) }
            return@Column
        }
        if (sizes.size > 1) br.com.estudario.ui.studio.GxChips(sizes.map { it to "$it questões" }, size) { size = it }
        Text("${BoardStyle.minutesFor(style, size)} min de prova · usa ${SimulationUnlock.partsFor(size)} parte(s) da cota", style = MaterialTheme.typography.labelMedium, color = br.com.estudario.ui.studio.Gx.mute)
        br.com.estudario.ui.studio.GxLaunch("Gerar ${mode.title.lowercase()}", br.com.estudario.ui.brand.Glyph.Play, enabled = !busy, colors = listOf(color.copy(alpha = 0.85f), color), ink = androidx.compose.ui.graphics.Color(0xFF1A1440)) { onGenerate(size) }
    }
}

private fun modeColor(mode: SimulationMode): androidx.compose.ui.graphics.Color = when (mode) {
    SimulationMode.DIAGNOSTIC -> br.com.estudario.ui.studio.Gx.cyan
    SimulationMode.STUDIED -> br.com.estudario.ui.studio.Gx.mint
    SimulationMode.FULL -> br.com.estudario.ui.studio.Gx.gold
    SimulationMode.REMATCH -> br.com.estudario.ui.studio.Gx.red
}

private fun modeGlyph(mode: SimulationMode): br.com.estudario.ui.brand.Glyph = when (mode) {
    SimulationMode.DIAGNOSTIC -> br.com.estudario.ui.brand.Glyph.Lupa
    SimulationMode.STUDIED -> br.com.estudario.ui.brand.Glyph.Check
    SimulationMode.FULL -> br.com.estudario.ui.brand.Glyph.Medal
    SimulationMode.REMATCH -> br.com.estudario.ui.brand.Glyph.Cycle
}

/** O alto da sala de prova: o concurso, a banca, o medidor da última nota contra a meta e o placar. */
@Composable
private fun ExamRoomHeader(competition: String, board: String?, delivered: Int, scores: List<Int>, target: Int) {
    val last = scores.lastOrNull()
    val best = scores.maxOrNull()
    val fill by androidx.compose.animation.core.animateFloatAsState((last ?: 0) / 100f, androidx.compose.animation.core.tween(1_000), label = "sala-medidor")
    br.com.estudario.ui.studio.GxPanel {
        Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Column {
                br.com.estudario.ui.studio.GxKicker("Sala de prova")
                Text("Simulados", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black, color = br.com.estudario.ui.studio.Gx.strong)
                Text(competition + (board?.let { " · banca $it" } ?: ""), style = MaterialTheme.typography.bodySmall, color = br.com.estudario.ui.studio.Gx.mute)
            }
            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Box(Modifier.size(168.dp), contentAlignment = Alignment.Center) {
                    androidx.compose.foundation.Canvas(Modifier.matchParentSize()) {
                        val stroke = 16.dp.toPx()
                        val inset = stroke / 2 + 6.dp.toPx()
                        val arc = androidx.compose.ui.geometry.Size(size.width - inset * 2, size.height - inset * 2)
                        val tl = androidx.compose.ui.geometry.Offset(inset, inset)
                        drawArc(br.com.estudario.ui.studio.Gx.tint(0.08f), -90f, 360f, false, tl, arc, style = androidx.compose.ui.graphics.drawscope.Stroke(stroke))
                        drawArc(androidx.compose.ui.graphics.Brush.sweepGradient(listOf(androidx.compose.ui.graphics.Color(0xFF7C5CFF), br.com.estudario.ui.studio.Gx.mint, androidx.compose.ui.graphics.Color(0xFF7C5CFF))), -90f, 360f * fill, false, tl, arc, style = androidx.compose.ui.graphics.drawscope.Stroke(stroke, cap = androidx.compose.ui.graphics.StrokeCap.Round))
                        // a marca dourada da meta
                        drawArc(br.com.estudario.ui.studio.Gx.gold, -90f + 360f * target / 100f - 2f, 4f, false, androidx.compose.ui.geometry.Offset(2.dp.toPx(), 2.dp.toPx()), androidx.compose.ui.geometry.Size(size.width - 4.dp.toPx(), size.height - 4.dp.toPx()), style = androidx.compose.ui.graphics.drawscope.Stroke(4.dp.toPx()))
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(last?.let { "$it%" } ?: "-", style = MaterialTheme.typography.displaySmall, fontWeight = FontWeight.Black, color = br.com.estudario.ui.studio.Gx.strong)
                        Text(if (last == null) "FAÇA O PRIMEIRO" else if (last >= target) "ACIMA DA META" else "ÚLTIMA NOTA", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Black, color = br.com.estudario.ui.studio.Gx.mute)
                    }
                }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                RoomStat(br.com.estudario.ui.brand.Glyph.Clipboard, "$delivered", if (delivered == 1) "entregue" else "entregues", br.com.estudario.ui.studio.Gx.violet, Modifier.weight(1f))
                RoomStat(br.com.estudario.ui.brand.Glyph.Trophy, best?.let { "$it%" } ?: "-", "melhor nota", br.com.estudario.ui.studio.Gx.gold, Modifier.weight(1f))
            }
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                RoomStat(br.com.estudario.ui.brand.Glyph.Target, "$target%", "sua meta", br.com.estudario.ui.studio.Gx.mint, Modifier.weight(1f))
                val delta = if (scores.size >= 2) scores.last() - scores.first() else null
                RoomStat(br.com.estudario.ui.brand.Glyph.Chart, delta?.let { if (it >= 0) "+$it" else "$it" } ?: "-", "desde o primeiro", br.com.estudario.ui.studio.Gx.cyan, Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun RoomStat(glyph: br.com.estudario.ui.brand.Glyph, value: String, label: String, color: androidx.compose.ui.graphics.Color, modifier: Modifier) {
    Row(
        modifier.clip(androidx.compose.foundation.shape.RoundedCornerShape(16.dp)).background(br.com.estudario.ui.studio.Gx.tint(0.04f))
            .border(1.dp, br.com.estudario.ui.studio.Gx.tint(0.08f), androidx.compose.foundation.shape.RoundedCornerShape(16.dp)).padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(Modifier.size(40.dp).clip(androidx.compose.foundation.shape.RoundedCornerShape(12.dp)).background(color.copy(alpha = 0.16f)), contentAlignment = Alignment.Center) {
            br.com.estudario.ui.brand.BrandIcon(glyph, size = 32.dp)
        }
        Spacer(Modifier.width(10.dp))
        Column {
            Text(value, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black, color = br.com.estudario.ui.studio.Gx.strong)
            Text(label, style = MaterialTheme.typography.labelSmall, color = br.com.estudario.ui.studio.Gx.mute, maxLines = 1)
        }
    }
}

@Composable
private fun HistoryRow(simulation: SimulationEntity, target: Int, onClick: () -> Unit) {
    val date = remember(simulation.finishedAt) { SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR")).format(Date(simulation.finishedAt ?: simulation.createdAt)) }
    val score = simulation.scorePercent ?: 0
    ElevatedCard(onClick = onClick, modifier = Modifier.fillMaxWidth()) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(simulation.title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                Text("$date · ${simulation.correctCount} de ${simulation.questionCount}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                LinearProgressIndicator({ score / 100f }, Modifier.fillMaxWidth(), color = if (score >= target) estudarioColors().completed else MaterialTheme.colorScheme.primary, strokeCap = StrokeCap.Round)
            }
            Spacer(Modifier.width(14.dp))
            Text("$score%", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = if (score >= target) estudarioColors().completed else MaterialTheme.colorScheme.onSurface)
            if (score >= target) Icon(Icons.Outlined.CheckCircle, "Acima da meta", tint = estudarioColors().completed)
        }
    }
}

/** Linha das notas, com a meta tracejada: a pessoa vê se está subindo e quanto falta. */
@Composable
fun EvolutionChart(scores: List<Int>, target: Int, modifier: Modifier = Modifier) {
    val line = MaterialTheme.colorScheme.primary
    val goal = estudarioColors().completed
    val grid = MaterialTheme.colorScheme.outlineVariant
    ElevatedCard(modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            val first = scores.first()
            val last = scores.last()
            Text(
                when {
                    last > first -> "Você subiu ${last - first} pontos desde o primeiro simulado."
                    last < first -> "Caiu ${first - last} pontos desde o primeiro. Revisar e treinar os tópicos mais fracos ajuda a recuperar."
                    else -> "Nota estável desde o primeiro simulado."
                },
                style = MaterialTheme.typography.bodyMedium,
            )
            Canvas(Modifier.fillMaxWidth().height(120.dp)) {
                val stepX = if (scores.size > 1) size.width / (scores.size - 1) else size.width
                fun y(value: Int) = size.height - value / 100f * size.height
                drawLine(grid, Offset(0f, y(50)), Offset(size.width, y(50)), strokeWidth = 1f)
                drawLine(goal, Offset(0f, y(target)), Offset(size.width, y(target)), strokeWidth = 3f, pathEffect = androidx.compose.ui.graphics.PathEffect.dashPathEffect(floatArrayOf(12f, 10f)))
                scores.zipWithNext().forEachIndexed { index, (a, b) ->
                    drawLine(line, Offset(index * stepX, y(a)), Offset((index + 1) * stepX, y(b)), strokeWidth = 6f, cap = StrokeCap.Round)
                }
                scores.forEachIndexed { index, value -> drawCircle(line, 8f, Offset(index * stepX, y(value))) }
            }
            Text("Linha tracejada: sua meta de $target%", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
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
