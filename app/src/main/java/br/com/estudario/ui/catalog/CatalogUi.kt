package br.com.estudario.ui.catalog

import br.com.estudario.ui.components.AlertDialog
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AttachFile
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.WarningAmber
import androidx.compose.material3.*
import br.com.estudario.ui.brand.OutlinedButton
import br.com.estudario.ui.brand.Button
import br.com.estudario.ui.brand.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import br.com.estudario.EstudarioApplication
import br.com.estudario.data.ai.EditalPdfText
import br.com.estudario.data.catalog.CatalogContest
import br.com.estudario.data.catalog.CatalogEstudo
import br.com.estudario.data.catalog.ContestCatalogException
import br.com.estudario.data.catalog.EditalComparison
import br.com.estudario.data.catalog.PendingContest
import br.com.estudario.data.local.CompetitionEntity
import br.com.estudario.ui.AppViewModel
import br.com.estudario.ui.theme.EstudarioShapes
import br.com.estudario.ui.theme.estudarioColors
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.security.MessageDigest
import java.time.LocalDate

private fun catalogMessage(error: Throwable): String = when ((error as? ContestCatalogException)?.code) {
    "AUTH_REQUIRED" -> "Entre com o Google para enviar."
    "TOO_MANY_PENDING" -> "Você já tem 10 envios esperando aprovação. Aguarde a análise."
    "NETWORK" -> "Sem conexão agora. Confira a internet e tente de novo."
    "NOT_FOUND" -> "Esse concurso não está mais no catálogo."
    "UNAVAILABLE" -> "O catálogo não está disponível nesta versão do app."
    else -> "Não deu certo agora. Tente de novo em instantes."
}

@Composable
private fun FullScreen(title: String, onClose: () -> Unit, content: @Composable ColumnScope.() -> Unit) {
    Dialog(onDismissRequest = onClose, properties = DialogProperties(usePlatformDefaultWidth = false, decorFitsSystemWindows = false)) {
        BackHandler(onBack = onClose)
        Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            Column(Modifier.fillMaxSize().statusBarsPadding().navigationBarsPadding()) {
                Row(Modifier.fillMaxWidth().padding(horizontal = 4.dp, vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onClose) { Icon(Icons.Outlined.Close, "Fechar") }
                    Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
                }
                content()
            }
        }
    }
}

/**
 * Busca de concursos prontos: a pessoa digita "pmmg soldado", escolhe e recebe as matérias na
 * hora, pela mesma revisão de importação de sempre. Pode anexar o PDF para conferir se bate.
 */
@Composable
fun CatalogSearchDialog(viewModel: AppViewModel, onDismiss: () -> Unit) {
    val context = LocalContext.current
    val catalog = (context.applicationContext as EstudarioApplication).contestCatalog
    val scope = rememberCoroutineScope()
    var query by remember { mutableStateOf("") }
    var results by remember { mutableStateOf<List<CatalogContest>>(emptyList()) }
    var loading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    var chosen by remember { mutableStateOf<CatalogContest?>(null) }
    LaunchedEffect(query) {
        if (query.trim().length < 2) { results = emptyList(); error = null; return@LaunchedEffect }
        delay(350)
        loading = true
        runCatching { catalog.search(query) }.onSuccess { results = it; error = null }.onFailure { error = catalogMessage(it) }
        loading = false
    }
    chosen?.let { contest ->
        CatalogContestSheet(contest, onDismiss = { chosen = null }) {
            scope.launch {
                runCatching { catalog.estudo(contest.id) }
                    .onSuccess { estudo -> chosen = null; onDismiss(); viewModel.openIncomingText(estudo) }
                    .onFailure { Toast.makeText(context, catalogMessage(it), Toast.LENGTH_LONG).show() }
            }
        }
    }
    FullScreen("Concursos prontos", onDismiss) {
        Column(Modifier.padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedTextField(
                value = query, onValueChange = { query = it }, modifier = Modifier.fillMaxWidth(), singleLine = true,
                placeholder = { Text("Ex.: soldado pmmg, tjmg, inss") },
                leadingIcon = { Icon(Icons.Outlined.Search, null) },
                shape = EstudarioShapes.pill,
            )
            Text(
                "Concursos já organizados e aprovados pelo Estudário. Você recebe as matérias e os tópicos na hora e pode editar tudo depois.",
                style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            if (loading) LinearProgressIndicator(Modifier.fillMaxWidth())
            error?.let { Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall) }
        }
        LazyColumn(Modifier.weight(1f), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            if (query.trim().length >= 2 && !loading && results.isEmpty() && error == null) item {
                Text("Nenhum concurso encontrado com esse nome ainda. Monte pelo PDF do edital: depois de pronto, você pode enviar para o catálogo e ajudar outras pessoas.", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            items(results, key = { it.id }) { contest -> ContestCard(contest) { chosen = contest } }
        }
    }
}

@Composable
private fun ContestCard(contest: CatalogContest, onClick: () -> Unit) {
    val outdated = contest.possiblyOutdated(LocalDate.now().year)
    Surface(onClick = onClick, shape = EstudarioShapes.panel, color = MaterialTheme.colorScheme.surfaceContainerLow, modifier = Modifier.fillMaxWidth()) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                Text(contest.role, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, maxLines = 2, overflow = TextOverflow.Ellipsis)
                Text(listOfNotNull(contest.name, contest.board).joinToString(" · "), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text("${contest.subjectCount} matérias · ${contest.topicCount} tópicos", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            YearBadge(contest.year, outdated)
        }
    }
}

@Composable
private fun YearBadge(year: Int, outdated: Boolean) {
    val color = if (outdated) estudarioColors().attention else estudarioColors().completed
    Surface(shape = CircleShape, color = color.copy(alpha = 0.15f)) {
        Text(if (outdated) "$year · antigo" else "$year", Modifier.padding(horizontal = 10.dp, vertical = 4.dp), style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold, color = color)
    }
}

/** Detalhe do concurso escolhido: o ano, o link oficial e a conferência com o PDF, se quiser. */
@Composable
private fun CatalogContestSheet(contest: CatalogContest, onDismiss: () -> Unit, onUse: () -> Unit) {
    val context = LocalContext.current
    val catalog = (context.applicationContext as EstudarioApplication).contestCatalog
    val scope = rememberCoroutineScope()
    var comparing by remember { mutableStateOf(false) }
    var comparison by remember { mutableStateOf<EditalComparison.Result?>(null) }
    var compareError by remember { mutableStateOf<String?>(null) }
    val outdated = contest.possiblyOutdated(LocalDate.now().year)
    val picker = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        uri ?: return@rememberLauncherForActivityResult
        scope.launch {
            comparing = true; compareError = null
            runCatching {
                val estudo = JSONObject(catalog.estudo(contest.id))
                val bytes = withContext(Dispatchers.IO) { context.contentResolver.openInputStream(uri)!!.use { it.readBytes() } }
                val hash = MessageDigest.getInstance("SHA-256").digest(bytes).joinToString("") { "%02x".format(it) }
                val text = withContext(Dispatchers.Default) { EditalPdfText.of(bytes, hash)?.text } ?: throw IllegalStateException("sem texto")
                EditalComparison.compare(CatalogEstudo.subjectNames(estudo), contest.year, text)
            }.onSuccess { comparison = it }.onFailure { compareError = "Não deu para ler o texto desse PDF. Confira se é o edital (e não uma imagem escaneada)." }
            comparing = false
        }
    }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(contest.role) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(listOfNotNull(contest.name, contest.agency, contest.board).joinToString(" · "), style = MaterialTheme.typography.bodyMedium)
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    YearBadge(contest.year, outdated)
                    Text("${contest.subjectCount} matérias · ${contest.topicCount} tópicos", style = MaterialTheme.typography.labelMedium)
                }
                if (outdated) Text("Este edital é de ${contest.year}. Se já saiu um mais novo, anexe o PDF para conferir se as matérias mudaram.", style = MaterialTheme.typography.bodySmall, color = estudarioColors().attention)
                contest.editalUrl?.let { Text("Edital oficial: $it", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary, maxLines = 3, overflow = TextOverflow.Ellipsis) }
                OutlinedButton(onClick = { picker.launch(arrayOf("application/pdf")) }, enabled = !comparing, modifier = Modifier.fillMaxWidth()) {
                    Icon(Icons.Outlined.AttachFile, null, Modifier.size(18.dp)); Spacer(Modifier.width(6.dp))
                    Text(if (comparing) "Conferindo…" else "Anexar meu PDF para conferir")
                }
                compareError?.let { Text(it, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.error) }
                comparison?.let { ComparisonResult(it, contest.year) }
            }
        },
        confirmButton = { Button(onClick = onUse) { Text("Usar este concurso") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Voltar") } },
    )
}

@Composable
private fun ComparisonResult(result: EditalComparison.Result, year: Int) {
    val ok = result.matches
    val color = if (ok) estudarioColors().completed else estudarioColors().attention
    Surface(shape = EstudarioShapes.row, color = color.copy(alpha = 0.12f), modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(if (ok) Icons.Outlined.CheckCircle else Icons.Outlined.WarningAmber, null, tint = color, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(6.dp))
                Text(if (ok) "Confere com o seu edital" else "Há diferenças com o seu edital", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = color)
            }
            Text("${result.found.size} de ${result.found.size + result.missing.size} matérias do catálogo aparecem no seu PDF.", style = MaterialTheme.typography.bodySmall)
            if (result.missing.isNotEmpty()) Text("Não encontradas: ${result.missing.joinToString(", ")}", style = MaterialTheme.typography.bodySmall)
            if (!result.yearInPdf) Text("O ano $year não aparece no seu PDF: pode ser outro edital.", style = MaterialTheme.typography.bodySmall)
            if (!ok) Text("Se preferir, monte pelo seu PDF para seguir exatamente o seu edital.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

/** Enviar um concurso do aparelho para o catálogo. Fica pendente até a aprovação. */
@Composable
fun CatalogSubmitDialog(
    competition: CompetitionEntity,
    viewModel: AppViewModel,
    onDismiss: () -> Unit,
    initialRole: String = "",
    initialBoard: String = "",
    initialYear: String = "",
    offeredAfterGeneration: Boolean = false,
) {
    val context = LocalContext.current
    val catalog = (context.applicationContext as EstudarioApplication).contestCatalog
    val subjects by viewModel.subjects.collectAsState()
    val topics by viewModel.topics.collectAsState()
    val scope = rememberCoroutineScope()
    var name by remember { mutableStateOf(competition.name) }
    var role by remember { mutableStateOf(initialRole) }
    var board by remember { mutableStateOf(initialBoard) }
    var year by remember { mutableStateOf(initialYear.filter(Char::isDigit).take(4).ifBlank { LocalDate.now().year.toString() }) }
    var link by remember { mutableStateOf("") }
    var busy by remember { mutableStateOf(false) }
    val own = subjects.filter { it.competitionId == competition.id }
    val ownTopics = topics.filter { topic -> own.any { it.id == topic.subjectId } }
    val yearValue = year.toIntOrNull()
    val valid = name.trim().length >= 2 && role.trim().length >= 2 && yearValue != null && yearValue in 1990..2100 && own.isNotEmpty()
    AlertDialog(
        onDismissRequest = { if (!busy) onDismiss() },
        title = { Text(if (offeredAfterGeneration) "Ajudar outras pessoas?" else "Enviar para o catálogo") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Depois da aprovação, quem buscar este concurso recebe as ${own.size} matérias e ${ownTopics.size} tópicos na hora. Vai só a estrutura, sem suas anotações nem seu progresso.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                OutlinedTextField(name, { name = it }, label = { Text("Concurso (ex.: PMMG)") }, singleLine = true)
                OutlinedTextField(role, { role = it }, label = { Text("Cargo (ex.: Soldado)") }, singleLine = true)
                OutlinedTextField(board, { board = it }, label = { Text("Banca (opcional)") }, singleLine = true)
                OutlinedTextField(year, { year = it.filter(Char::isDigit).take(4) }, label = { Text("Ano do edital") }, singleLine = true)
                OutlinedTextField(link, { link = it }, label = { Text("Link do edital oficial (opcional)") }, singleLine = true)
            }
        },
        confirmButton = {
            Button(enabled = valid && !busy, onClick = {
                scope.launch {
                    busy = true
                    val title = "${name.trim()} - ${role.trim()} (${yearValue})"
                    runCatching {
                        catalog.submit(
                            name.trim(), role.trim(), board.trim().ifBlank { null }, null, yearValue!!, link.trim().ifBlank { null },
                            CatalogEstudo.build(competition, own, ownTopics, title), own.size, ownTopics.size,
                        )
                    }.onSuccess {
                        Toast.makeText(context, "Enviado. Assim que for aprovado, aparece na busca para todo mundo.", Toast.LENGTH_LONG).show()
                        onDismiss()
                    }.onFailure { Toast.makeText(context, catalogMessage(it), Toast.LENGTH_LONG).show() }
                    busy = false
                }
            }) { Text(if (busy) "Enviando…" else "Enviar para aprovação") }
        },
        dismissButton = { TextButton(enabled = !busy, onClick = onDismiss) { Text(if (offeredAfterGeneration) "Agora não" else "Cancelar") } },
    )
}

/** Fila de aprovação (só administradores): confere o envio e publica ou recusa. */
@Composable
fun CatalogReviewDialog(onDismiss: () -> Unit) {
    val context = LocalContext.current
    val catalog = (context.applicationContext as EstudarioApplication).contestCatalog
    val scope = rememberCoroutineScope()
    var items by remember { mutableStateOf<List<PendingContest>?>(null) }
    var error by remember { mutableStateOf<String?>(null) }
    var refresh by remember { mutableIntStateOf(0) }
    LaunchedEffect(refresh) {
        runCatching { catalog.pending() }.onSuccess { items = it; error = null }.onFailure { error = catalogMessage(it) }
    }
    fun review(id: String, approve: Boolean) = scope.launch {
        runCatching { catalog.review(id, approve) }
            .onSuccess { Toast.makeText(context, if (approve) "Publicado no catálogo." else "Recusado.", Toast.LENGTH_SHORT).show(); refresh++ }
            .onFailure { Toast.makeText(context, catalogMessage(it), Toast.LENGTH_LONG).show() }
    }
    FullScreen("Aprovar concursos", onDismiss) {
        LazyColumn(Modifier.weight(1f), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            error?.let { item { Text(it, color = MaterialTheme.colorScheme.error) } }
            if (items == null && error == null) item { LinearProgressIndicator(Modifier.fillMaxWidth()) }
            if (items?.isEmpty() == true) item { Text("Nenhum envio esperando aprovação.", color = MaterialTheme.colorScheme.onSurfaceVariant) }
            items(items.orEmpty(), key = { it.contest.id }) { pending ->
                val contest = pending.contest
                Surface(shape = EstudarioShapes.panel, color = MaterialTheme.colorScheme.surfaceContainerLow, modifier = Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(contest.role, Modifier.weight(1f), style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                            YearBadge(contest.year, contest.possiblyOutdated(LocalDate.now().year))
                        }
                        Text(listOfNotNull(contest.name, contest.board).joinToString(" · "), style = MaterialTheme.typography.bodySmall)
                        Text("${contest.subjectCount} matérias · ${contest.topicCount} tópicos", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(pending.subjects.joinToString(" • "), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 6, overflow = TextOverflow.Ellipsis)
                        contest.editalUrl?.let { Text(it, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary, maxLines = 2, overflow = TextOverflow.Ellipsis) }
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(onClick = { review(contest.id, true) }, modifier = Modifier.weight(1f)) { Text("Aprovar") }
                            OutlinedButton(onClick = { review(contest.id, false) }, modifier = Modifier.weight(1f), colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error)) { Text("Recusar") }
                        }
                    }
                }
            }
        }
    }
}

