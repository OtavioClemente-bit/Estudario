package br.com.estudario.ui.planner

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.Restore
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import br.com.estudario.ui.prompt.PlanPromptBuilderDialog
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.FileOpen
import br.com.estudario.data.transfer.planner.PlanImportMode
import br.com.estudario.data.local.planner.StudyAvailabilityEntity
import br.com.estudario.domain.planner.PlanTaskStatus
import br.com.estudario.ui.AppViewModel
import br.com.estudario.ui.components.EmptyState
import br.com.estudario.ui.components.LoadingDialog
import br.com.estudario.ui.components.LoadingScreen
import br.com.estudario.ui.components.ScreenTitle
import br.com.estudario.ui.tour.TourKey
import br.com.estudario.ui.tour.tourTarget
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate

@Composable
fun PlanScreen(viewModel: StudyPlanViewModel, appViewModel: AppViewModel, onOpenTopic: (Long) -> Unit, onOpenErrors: () -> Unit = {}, onFocus: () -> Unit = {}) {
    val state by viewModel.state.collectAsState()
    val transfer by viewModel.transfer.collectAsState()
    val busy by viewModel.busy.collectAsState()
    val competitions by viewModel.competitions.collectAsState()
    val subjects by viewModel.subjects.collectAsState()
    val topics by viewModel.topics.collectAsState()
    val tourStep by appViewModel.tourStep.collectAsState()
    val context = androidx.compose.ui.platform.LocalContext.current
    val scope = rememberCoroutineScope()
    var wizard by remember { mutableStateOf(false) }
    var management by remember { mutableStateOf(false) }
    var promptGenerator by remember { mutableStateOf(false) }
    var completion by remember { mutableStateOf<PlannerTaskUi?>(null) }
    // Minutos que vieram do cronômetro da sessão de foco encerrada; zero quando a conclusão é manual.
    var measuredMinutes by remember { mutableIntStateOf(0) }
    val lastFocusTaskId by appViewModel.lastFocusTaskId.collectAsState()
    val lastFocusMinutes by appViewModel.lastFocusMinutes.collectAsState()
    var reprogram by remember { mutableStateOf<PlannerTaskUi?>(null) }
    var skip by remember { mutableStateOf<PlannerTaskUi?>(null) }
    var pendingExport by remember { mutableStateOf<String?>(null) }
    var pendingContext by remember { mutableStateOf<String?>(null) }
    var editAvailability by remember { mutableStateOf(false) }
    val openPlan = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        uri?.let {
            // O loading começa aqui, antes mesmo de abrir o arquivo: em arquivos grandes a leitura
            // sozinha já demora, e antes disso a tela ficava sem nenhum sinal de que algo acontecia.
            appViewModel.beginIncomingFile()
            scope.launch {
                val text = readPlanText(context, it)
                if (text.isNullOrBlank()) appViewModel.reportIncomingFileError("Não foi possível ler o arquivo escolhido.") else appViewModel.openIncomingText(text)
            }
        }
    }
    val pickPlanFile = { openPlan.launch(arrayOf("*/*")) }
    val savePlan = rememberLauncherForActivityResult(ActivityResultContracts.CreateDocument("application/json")) { uri -> val raw = pendingExport; if (uri != null && raw != null) scope.launch(Dispatchers.IO) { context.contentResolver.openOutputStream(uri)?.bufferedWriter()?.use { it.write(raw) } } }
    val saveContext = rememberLauncherForActivityResult(ActivityResultContracts.CreateDocument("text/plain")) { uri -> val raw = pendingContext; if (uri != null && raw != null) scope.launch(Dispatchers.IO) { context.contentResolver.openOutputStream(uri)?.bufferedWriter()?.use { it.write(raw) } } }
    LaunchedEffect(Unit) { viewModel.selectSection(PlanSection.TODAY) }

    state.message?.let { message -> AlertDialog(onDismissRequest = viewModel::consumeMessage, title = { Text("Plano") }, text = { Text(message) }, confirmButton = { TextButton(onClick = viewModel::consumeMessage) { Text("OK") } }) }
    if (wizard) PlanWizardScreen(competitions, subjects, topics, { wizard = false }) { viewModel.createPlan(it) }
    if (promptGenerator) PlanPromptBuilderDialog(appViewModel, onDismiss = { promptGenerator = false }, onPickFile = pickPlanFile)
    if (editAvailability) AvailabilityDialog(state.availability, { editAvailability = false }) { viewModel.updateAvailability(it) }
    // Encerrou o modo foco numa tarefa do plano: a conclusão abre sozinha, já com o tempo medido.
    LaunchedEffect(lastFocusTaskId, state.todayTasks) {
        val task = lastFocusTaskId.takeIf { it.isNotBlank() }?.let { id -> state.todayTasks.firstOrNull { it.entity.id == id } }
        if (task != null) {
            measuredMinutes = lastFocusMinutes
            completion = task
            appViewModel.clearLastFocus()
        }
    }
    completion?.let { row ->
        TaskExecutionDialog(row, measuredMinutes.takeIf { it > 0 }, { completion = null; measuredMinutes = 0 }) { viewModel.complete(row.entity.id, it) }
    }
    reprogram?.let { row -> DateOrAutomaticDialog({ reprogram = null }) { viewModel.reprogram(row.entity.id, it) } }
    skip?.let { row -> ReasonDialog({ skip = null }) { viewModel.skip(row.entity.id, it) } }
    TransferPlanDialog(transfer, viewModel)
    busy?.let { current -> LoadingDialog(current.title, current.message) }

    if (management) {
        PlanManagementScreen(state, { management = false }, viewModel::activate, viewModel::markMaster, viewModel::duplicate, viewModel::archive, viewModel::restore, viewModel::delete, { id -> viewModel.executionCount(id) }, { plan ->
            scope.launch { runCatching { viewModel.exportPlan(plan.id) }.onSuccess { pendingExport = it; savePlan.launch("${plan.name}.plano") } }
        }, onContextJson = { pendingContext = viewModel.exportContextJson(); saveContext.launch("contexto-plano.json") }, onContextText = { pendingContext = viewModel.exportContextText(); saveContext.launch("contexto-plano.txt") }, onEditAvailability = { editAvailability = true }, onUpdateSubject = viewModel::updateSubject)
        return
    }
    Column(Modifier.fillMaxSize()) {
        Row(Modifier.padding(20.dp, 16.dp, 20.dp, 0.dp)) {
            ScreenTitle("Plano", state.activePlan?.name ?: "Planejamento adaptativo") {
                Row {
                    IconButton(
                        onClick = { promptGenerator = true },
                        modifier = Modifier.tourTarget(TourKey.PLAN_AI, tourStep?.key) { appViewModel.reportTourTargetBounds(TourKey.PLAN_AI, it) },
                    ) { Icon(Icons.Outlined.AutoAwesome, "Gerar plano com IA") }
                    IconButton(
                        onClick = pickPlanFile,
                        modifier = Modifier.tourTarget(TourKey.PLAN_IMPORT, tourStep?.key) { appViewModel.reportTourTargetBounds(TourKey.PLAN_IMPORT, it) },
                    ) { Icon(Icons.Outlined.FileOpen, "Importar arquivo .plano") }
                    IconButton(
                        onClick = { wizard = true },
                        modifier = Modifier.tourTarget(TourKey.PLAN_CREATE, tourStep?.key) { appViewModel.reportTourTargetBounds(TourKey.PLAN_CREATE, it) },
                    ) { Icon(Icons.Outlined.Add, "Novo plano") }
                    IconButton(
                        onClick = { management = true },
                        modifier = Modifier.tourTarget(TourKey.PLAN_MANAGE, tourStep?.key) { appViewModel.reportTourTargetBounds(TourKey.PLAN_MANAGE, it) },
                    ) { Icon(Icons.Outlined.Tune, "Gerenciar planos") }
                }
            }
        }
        ScrollableTabRow(state.selectedSection.ordinal, modifier = Modifier.tourTarget(TourKey.PLAN_TABS, tourStep?.key) { appViewModel.reportTourTargetBounds(TourKey.PLAN_TABS, it) }) { PlanSection.entries.forEach { section -> Tab(state.selectedSection == section, { viewModel.selectSection(section) }, text = { Text(section.label) }) } }
        if (state.loading && state.tasks.isEmpty()) {
            LoadingScreen("Carregando seu plano", "Reunindo tarefas, metas e revisões…")
        } else if (state.activePlan == null) {
            EscolhaDeCaminho(
                onSemIa = { wizard = true },
                onComIa = { promptGenerator = true },
                onImportar = pickPlanFile,
                onSelecionar = { management = true },
            )
        } else {
            if (state.loading) LinearProgressIndicator(Modifier.fillMaxWidth())
            when (state.selectedSection) {
                PlanSection.TODAY -> TodayPlanScreen(
                    state,
                    onOpenTopic,
                    viewModel::start,
                    { row ->
                        // Começar de verdade: a tarefa entra em andamento e o cronômetro abre junto.
                        if (row.entity.status == PlanTaskStatus.PLANEJADA) viewModel.start(row.entity.id)
                        appViewModel.startFocus(
                            title = listOfNotNull(row.entity.subjectNameSnapshot, row.entity.topicNameSnapshot).joinToString(" › ").ifBlank { "Tarefa do plano" },
                            topicId = row.entity.topicId,
                            taskId = row.entity.id,
                        )
                        onFocus()
                    },
                    { completion = it },
                    { reprogram = it },
                    { skip = it },
                    viewModel::toggleTaskLock,
                    viewModel::generate,
                )
                PlanSection.WEEK -> WeekPlanScreen(state, viewModel::toggleDayLock)
                PlanSection.MONTH -> MonthPlanScreen(state)
                PlanSection.YEAR -> YearPlanScreen(state)
            }
        }
    }
}

/**
 * Os dois caminhos do plano, lado a lado e com o mesmo peso. O de dentro do app é o padrão porque
 * funciona offline e na hora; o com IA entra quando a pessoa quer um plano escrito sob medida.
 */
@Composable
private fun EscolhaDeCaminho(onSemIa: () -> Unit, onComIa: () -> Unit, onImportar: () -> Unit, onSelecionar: () -> Unit) {
    Column(
        Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        Text("Como você quer montar seu plano?", style = MaterialTheme.typography.titleLarge, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
        Text(
            "Só o edital precisa mesmo de IA (ou de digitação). O plano o app monta sozinho, com regras fixas, a partir do seu edital e do seu tempo.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        CaminhoCard(
            titulo = "Montar aqui, sem IA",
            selo = "Recomendado",
            corpo = "O app aplica o método completo: teoria com questões logo depois, revisão espaçada, rodízio das matérias por peso, simulado no seu dia mais livre e fases até a data da prova. Você vê a prévia antes de criar e pode conferir por que cada tarefa entrou.",
            rodape = "Funciona offline, na hora, e replaneja sozinho quando você atrasa.",
            acao = "Montar meu plano",
            destaque = true,
            onClick = onSemIa,
        )
        CaminhoCard(
            titulo = "Gerar com IA",
            selo = null,
            corpo = "O app monta o pedido com o seu edital, as suas horas e as suas metas para você colar no ChatGPT, Gemini ou outro app. A IA devolve um arquivo .plano que volta para cá.",
            rodape = "Depende de você ir até a IA e trazer a resposta, mas aceita pedidos fora do comum.",
            acao = "Preparar pedido para a IA",
            destaque = false,
            onClick = onComIa,
        )
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            TextButton(onClick = onImportar) { Text("Importar .plano") }
            TextButton(onClick = onSelecionar) { Text("Selecionar plano existente") }
        }
    }
}

@Composable
private fun CaminhoCard(titulo: String, selo: String?, corpo: String, rodape: String, acao: String, destaque: Boolean, onClick: () -> Unit) {
    ElevatedCard(
        Modifier.fillMaxWidth(),
        colors = CardDefaults.elevatedCardColors(
            containerColor = if (destaque) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        ),
    ) {
        Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(titulo, style = MaterialTheme.typography.titleMedium, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
                if (selo != null) AssistChip(onClick = {}, label = { Text(selo) })
            }
            Text(corpo, style = MaterialTheme.typography.bodyMedium)
            Text(rodape, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Button(onClick = onClick, modifier = Modifier.fillMaxWidth()) { Text(acao) }
        }
    }
}

@Composable private fun TransferPlanDialog(state: PlanTransferUiState, viewModel: StudyPlanViewModel) { when (state) {
    PlanTransferUiState.Idle -> Unit
    PlanTransferUiState.Loading -> LoadingDialog("Lendo o arquivo do plano", "Conferindo matérias, tópicos e datas…")
    is PlanTransferUiState.Error -> AlertDialog(onDismissRequest = viewModel::clearTransfer, title = { Text("Arquivo inválido") }, text = { Text(state.message) }, confirmButton = { TextButton(onClick = viewModel::clearTransfer) { Text("OK") } })
    is PlanTransferUiState.Success -> AlertDialog(onDismissRequest = viewModel::clearTransfer, title = { Text("Importação concluída") }, text = { Text(state.message) }, confirmButton = { TextButton(onClick = viewModel::clearTransfer) { Text("OK") } })
    is PlanTransferUiState.Preview -> AlertDialog(onDismissRequest = viewModel::clearTransfer, title = { Text(state.value.planName) }, text = { Column { Text("${state.value.importedTaskCount} tarefa(s) • ${state.value.protectedLocalTaskCount} registro(s) protegido(s)"); if (state.value.unresolvedReferences.isNotEmpty()) Text("Não resolvido: ${state.value.unresolvedReferences.joinToString()}", color = MaterialTheme.colorScheme.error); Text("Ativo/Mestre só serão alterados com confirmação explícita.") } }, confirmButton = { Row { if (state.value.existingPlan) { TextButton(enabled = state.value.unresolvedReferences.isEmpty(), onClick = { viewModel.importPlan(state.raw, PlanImportMode.MERGE, false, false) }) { Text("Mesclar") }; TextButton(enabled = state.value.unresolvedReferences.isEmpty(), onClick = { viewModel.importPlan(state.raw, PlanImportMode.REPLACE_FUTURE, false, false) }) { Text("Substituir futuro") } } else TextButton(enabled = state.value.unresolvedReferences.isEmpty(), onClick = { viewModel.importPlan(state.raw, PlanImportMode.CREATE, state.value.requestsActive, state.value.requestsMaster) }) { Text("Criar") } } }, dismissButton = { TextButton(onClick = viewModel::clearTransfer) { Text("Cancelar") } })
} }

@Composable private fun DateOrAutomaticDialog(onDismiss: () -> Unit, onConfirm: (LocalDate?) -> Unit) { AlertDialog(onDismissRequest = onDismiss, title = { Text("Reprogramar") }, text = { Text("Escolha redistribuição automática ou amanhã. O histórico realizado não será alterado.") }, confirmButton = { Row { TextButton(onClick = { onConfirm(null); onDismiss() }) { Text("Automática") }; TextButton(onClick = { onConfirm(LocalDate.now().plusDays(1)); onDismiss() }) { Text("Amanhã") } } }, dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } }) }
@Composable private fun ReasonDialog(onDismiss: () -> Unit, onConfirm: (String) -> Unit) { var reason by remember { mutableStateOf("") }; AlertDialog(onDismissRequest = onDismiss, title = { Text("Pular atividade") }, text = { OutlinedTextField(reason, { reason = it }, label = { Text("Motivo obrigatório") }) }, confirmButton = { TextButton(enabled = reason.isNotBlank(), onClick = { onConfirm(reason); onDismiss() }) { Text("Pular") } }, dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } }) }
private suspend fun readPlanText(context: Context, uri: Uri): String? = withContext(Dispatchers.IO) { runCatching { context.contentResolver.openInputStream(uri)?.use { it.readBytes().toString(Charsets.UTF_8) } }.getOrNull() }

@Composable private fun AvailabilityDialog(current: List<StudyAvailabilityEntity>, onDismiss: () -> Unit, onConfirm: (List<StudyAvailabilityEntity>) -> Unit) {
    val initial = (1..7).map { day -> current.firstOrNull { it.dayOfWeek == day }?.availableMinutes ?: 0 }
    val minutes = remember(current) { mutableStateListOf(*initial.toTypedArray()) }
    val names = listOf("Seg", "Ter", "Qua", "Qui", "Sex", "Sáb", "Dom")
    AlertDialog(onDismissRequest = onDismiss, title = { Text("Disponibilidade líquida") }, text = { Column(Modifier.fillMaxWidth().verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(4.dp)) { names.forEachIndexed { index, name -> Row { Text("$name: ${minutes[index]} min", Modifier.width(100.dp)); Slider(minutes[index].toFloat(), { minutes[index] = it.toInt() }, Modifier.weight(1f), valueRange = 0f..360f, steps = 11) } }; Text("Total: ${minutesLabel(minutes.sum())} por semana") } }, confirmButton = { TextButton(enabled = minutes.any { it > 0 }, onClick = { onConfirm(minutes.mapIndexed { index, value -> StudyAvailabilityEntity("pending", index + 1, value, value == 0) }); onDismiss() }) { Text("Salvar e recalcular") } }, dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } })
}
