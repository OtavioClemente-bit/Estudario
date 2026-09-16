package br.com.meuconcurso.ui.planner

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.com.meuconcurso.data.transfer.planner.PlanImportMode
import br.com.meuconcurso.data.local.planner.StudyAvailabilityEntity
import br.com.meuconcurso.ui.components.EmptyState
import br.com.meuconcurso.ui.components.ScreenTitle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate

@Composable
fun PlanScreen(viewModel: StudyPlanViewModel, onOpenTopic: (Long) -> Unit, onOpenErrors: () -> Unit = {}) {
    val state by viewModel.state.collectAsState()
    val transfer by viewModel.transfer.collectAsState()
    val competitions by viewModel.competitions.collectAsState()
    val subjects by viewModel.subjects.collectAsState()
    val context = androidx.compose.ui.platform.LocalContext.current
    val scope = rememberCoroutineScope()
    var wizard by remember { mutableStateOf(false) }
    var management by remember { mutableStateOf(false) }
    var completion by remember { mutableStateOf<PlannerTaskUi?>(null) }
    var reprogram by remember { mutableStateOf<PlannerTaskUi?>(null) }
    var skip by remember { mutableStateOf<PlannerTaskUi?>(null) }
    var pendingExport by remember { mutableStateOf<String?>(null) }
    var pendingContext by remember { mutableStateOf<String?>(null) }
    var editAvailability by remember { mutableStateOf(false) }
    val openPlan = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri -> uri?.let { scope.launch { readPlanText(context, it)?.let(viewModel::inspectPlan) } } }
    val savePlan = rememberLauncherForActivityResult(ActivityResultContracts.CreateDocument("application/json")) { uri -> val raw = pendingExport; if (uri != null && raw != null) scope.launch(Dispatchers.IO) { context.contentResolver.openOutputStream(uri)?.bufferedWriter()?.use { it.write(raw) } } }
    val saveContext = rememberLauncherForActivityResult(ActivityResultContracts.CreateDocument("text/plain")) { uri -> val raw = pendingContext; if (uri != null && raw != null) scope.launch(Dispatchers.IO) { context.contentResolver.openOutputStream(uri)?.bufferedWriter()?.use { it.write(raw) } } }
    LaunchedEffect(Unit) { viewModel.selectSection(PlanSection.TODAY) }

    state.message?.let { message -> AlertDialog(onDismissRequest = viewModel::consumeMessage, title = { Text("Plano") }, text = { Text(message) }, confirmButton = { TextButton(onClick = viewModel::consumeMessage) { Text("OK") } }) }
    if (wizard) PlanWizardScreen(competitions, subjects, { wizard = false }, viewModel::createPlan)
    if (editAvailability) AvailabilityDialog(state.availability, { editAvailability = false }) { viewModel.updateAvailability(it) }
    completion?.let { row -> TaskExecutionDialog(row, { completion = null }) { viewModel.complete(row.entity.id, it) } }
    reprogram?.let { row -> DateOrAutomaticDialog({ reprogram = null }) { viewModel.reprogram(row.entity.id, it) } }
    skip?.let { row -> ReasonDialog({ skip = null }) { viewModel.skip(row.entity.id, it) } }
    TransferPlanDialog(transfer, viewModel)

    if (management) {
        PlanManagementScreen(state, { management = false }, viewModel::activate, viewModel::markMaster, viewModel::duplicate, viewModel::archive, viewModel::restore, { plan ->
            scope.launch { runCatching { viewModel.exportPlan(plan.id) }.onSuccess { pendingExport = it; savePlan.launch("${plan.name}.plano") } }
        }, onContextJson = { pendingContext = viewModel.exportContextJson(); saveContext.launch("contexto-plano.json") }, onContextText = { pendingContext = viewModel.exportContextText(); saveContext.launch("contexto-plano.txt") }, onEditAvailability = { editAvailability = true }, onUpdateSubject = viewModel::updateSubject)
        return
    }
    Column(Modifier.fillMaxSize()) {
        Row(Modifier.padding(20.dp, 16.dp, 20.dp, 0.dp)) {
            ScreenTitle("Plano", state.activePlan?.name ?: "Planejamento adaptativo") {
                Row { IconButton(onClick = { wizard = true }) { Icon(Icons.Outlined.Add, "Novo plano") }; IconButton(onClick = { management = true }) { Icon(Icons.Outlined.Settings, "Gerenciar planos") } }
            }
        }
        ScrollableTabRow(state.selectedSection.ordinal) { PlanSection.entries.forEach { section -> Tab(state.selectedSection == section, { viewModel.selectSection(section) }, text = { Text(section.label) }) } }
        if (state.activePlan == null) {
            EmptyState("Nenhum plano ativo", "Crie, importe ou ative um plano. Planos arquivados continuam disponíveis no gerenciamento.", "Criar plano") { wizard = true }
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) { TextButton(onClick = { openPlan.launch(arrayOf("application/json", "application/octet-stream", "text/plain")) }) { Text("Importar .plano") }; TextButton(onClick = { management = true }) { Text("Selecionar plano") } }
        } else {
            when (state.selectedSection) {
                PlanSection.TODAY -> TodayPlanScreen(state, onOpenTopic, viewModel::start, { completion = it }, { reprogram = it }, { skip = it }, viewModel::toggleTaskLock, viewModel::generate)
                PlanSection.WEEK -> WeekPlanScreen(state, viewModel::toggleDayLock)
                PlanSection.MONTH -> MonthPlanScreen(state)
                PlanSection.YEAR -> YearPlanScreen(state)
            }
        }
    }
}

@Composable private fun TransferPlanDialog(state: PlanTransferUiState, viewModel: StudyPlanViewModel) { when (state) {
    PlanTransferUiState.Idle -> Unit
    PlanTransferUiState.Loading -> AlertDialog(onDismissRequest = {}, title = { Text("Lendo plano") }, text = { LinearProgressIndicator() }, confirmButton = {})
    is PlanTransferUiState.Error -> AlertDialog(onDismissRequest = viewModel::clearTransfer, title = { Text("Arquivo inválido") }, text = { Text(state.message) }, confirmButton = { TextButton(onClick = viewModel::clearTransfer) { Text("OK") } })
    is PlanTransferUiState.Success -> AlertDialog(onDismissRequest = viewModel::clearTransfer, title = { Text("Importação concluída") }, text = { Text(state.message) }, confirmButton = { TextButton(onClick = viewModel::clearTransfer) { Text("OK") } })
    is PlanTransferUiState.Preview -> AlertDialog(onDismissRequest = viewModel::clearTransfer, title = { Text(state.value.planName) }, text = { Column { Text("${state.value.importedTaskCount} tarefa(s) • ${state.value.protectedLocalTaskCount} registro(s) protegido(s)"); if (state.value.unresolvedReferences.isNotEmpty()) Text("Não resolvido: ${state.value.unresolvedReferences.joinToString()}", color = MaterialTheme.colorScheme.error); Text("Ativo/Mestre só serão alterados com confirmação explícita.") } }, confirmButton = { Row { if (state.value.existingPlan) { TextButton(enabled = state.value.unresolvedReferences.isEmpty(), onClick = { viewModel.importPlan(state.raw, PlanImportMode.MERGE, false, false) }) { Text("Mesclar") }; TextButton(enabled = state.value.unresolvedReferences.isEmpty(), onClick = { viewModel.importPlan(state.raw, PlanImportMode.REPLACE_FUTURE, false, false) }) { Text("Substituir futuro") } } else TextButton(enabled = state.value.unresolvedReferences.isEmpty(), onClick = { viewModel.importPlan(state.raw, PlanImportMode.CREATE, state.value.requestsActive, state.value.requestsMaster) }) { Text("Criar") } } }, dismissButton = { TextButton(onClick = viewModel::clearTransfer) { Text("Cancelar") } })
} }

@Composable private fun DateOrAutomaticDialog(onDismiss: () -> Unit, onConfirm: (LocalDate?) -> Unit) { AlertDialog(onDismissRequest = onDismiss, title = { Text("Reprogramar") }, text = { Text("Escolha redistribuição automática ou amanhã. O histórico realizado não será alterado.") }, confirmButton = { Row { TextButton(onClick = { onConfirm(null); onDismiss() }) { Text("Automática") }; TextButton(onClick = { onConfirm(LocalDate.now().plusDays(1)); onDismiss() }) { Text("Amanhã") } } }, dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } }) }
@Composable private fun ReasonDialog(onDismiss: () -> Unit, onConfirm: (String) -> Unit) { var reason by remember { mutableStateOf("") }; AlertDialog(onDismissRequest = onDismiss, title = { Text("Pular atividade") }, text = { OutlinedTextField(reason, { reason = it }, label = { Text("Motivo obrigatório") }) }, confirmButton = { TextButton(enabled = reason.isNotBlank(), onClick = { onConfirm(reason); onDismiss() }) { Text("Pular") } }, dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } }) }
private suspend fun readPlanText(context: Context, uri: Uri): String? = withContext(Dispatchers.IO) { context.contentResolver.openInputStream(uri)?.bufferedReader()?.use { it.readText() } }

@Composable private fun AvailabilityDialog(current: List<StudyAvailabilityEntity>, onDismiss: () -> Unit, onConfirm: (List<StudyAvailabilityEntity>) -> Unit) {
    val initial = (1..7).map { day -> current.firstOrNull { it.dayOfWeek == day }?.availableMinutes ?: 0 }
    val minutes = remember(current) { mutableStateListOf(*initial.toTypedArray()) }
    val names = listOf("Seg", "Ter", "Qua", "Qui", "Sex", "Sáb", "Dom")
    AlertDialog(onDismissRequest = onDismiss, title = { Text("Disponibilidade líquida") }, text = { Column(Modifier.fillMaxWidth().verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(4.dp)) { names.forEachIndexed { index, name -> Row { Text("$name: ${minutes[index]} min", Modifier.width(100.dp)); Slider(minutes[index].toFloat(), { minutes[index] = it.toInt() }, Modifier.weight(1f), valueRange = 0f..360f, steps = 11) } }; Text("Total: ${minutesLabel(minutes.sum())} por semana") } }, confirmButton = { TextButton(enabled = minutes.any { it > 0 }, onClick = { onConfirm(minutes.mapIndexed { index, value -> StudyAvailabilityEntity("pending", index + 1, value, value == 0) }); onDismiss() }) { Text("Salvar e recalcular") } }, dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } })
}
