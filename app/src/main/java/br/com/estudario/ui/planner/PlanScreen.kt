package br.com.estudario.ui.planner

import br.com.estudario.ui.components.AlertDialog
import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.Restore
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.*
import br.com.estudario.ui.brand.Slider
import br.com.estudario.ui.brand.FilterChip
import br.com.estudario.ui.brand.LinearProgressIndicator
import br.com.estudario.ui.brand.ElevatedCard
import br.com.estudario.ui.brand.Button
import br.com.estudario.ui.brand.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.FileOpen
import androidx.compose.material.icons.outlined.HelpOutline
import br.com.estudario.data.transfer.planner.PlanImportMode
import br.com.estudario.data.local.planner.StudyAvailabilityEntity
import br.com.estudario.data.local.planner.PlanTaskEntity
import br.com.estudario.domain.planner.PlanTaskStatus
import br.com.estudario.ui.AppViewModel
import br.com.estudario.ui.components.EmptyState
import br.com.estudario.ui.components.LoadingDialog
import br.com.estudario.ui.components.LoadingScreen
import br.com.estudario.ui.components.ScreenTitle
import br.com.estudario.ui.tour.TourKey
import br.com.estudario.ui.tour.tourTarget
import br.com.estudario.ui.theme.estudarioLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PlanScreen(viewModel: StudyPlanViewModel, appViewModel: AppViewModel, onOpenTopic: (Long) -> Unit, onOpenTopicTask: (Long, String) -> Unit = { id, _ -> onOpenTopic(id) }, onOpenErrors: () -> Unit = {}, onFocus: () -> Unit = {}, onHelp: () -> Unit = {}, onStartQuestions: (PlanTaskEntity, Int) -> Unit = { _, _ -> }, onReviews: () -> Unit = {}) {
    // As revisões espaçadas vencidas até hoje aparecem no próprio plano, para ninguém esquecer delas.
    val allReviews by appViewModel.reviews.collectAsState()
    val dueReviews = br.com.estudario.domain.ReviewPolicy.dueToday(allReviews).size
    val state by viewModel.state.collectAsState()
    val entitlements = br.com.estudario.ui.plans.rememberEntitlements()
    var planUpgrade by remember { mutableStateOf(false) }
    if (planUpgrade) br.com.estudario.ui.plans.UpgradeDialog(br.com.estudario.ui.plans.UpgradeReason.PLANS, onDismiss = { planUpgrade = false })
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
    var reprogram by remember { mutableStateOf<PlannerTaskUi?>(null) }
    var skip by remember { mutableStateOf<PlannerTaskUi?>(null) }
    var missingQuestionsTask by remember { mutableStateOf<PlanTaskEntity?>(null) }
    var allDoneTask by remember { mutableStateOf<PlanTaskEntity?>(null) }
    // Adiantar um tópico que ainda não tem matéria no aparelho: a geração abre direto, com o saldo
    // conferido na frente (no Grátis sem crédito, o anúncio aparece já no toque).
    var advanceTopic by remember { mutableStateOf<br.com.estudario.data.local.TopicEntity?>(null) }
    val theories by appViewModel.theories.collectAsState()
    advanceTopic?.let { topic ->
        br.com.estudario.ui.prompt.ContentPromptBuilderDialog(appViewModel, topic.subjectId, setOf(topic.id), onDismiss = { advanceTopic = null }, onPickFile = {})
    }
    var questionLaunchError by remember { mutableStateOf<String?>(null) }
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
    allDoneTask?.let { task -> AllQuestionsDoneDialog(task, onSkip = { viewModel.skip(task.id, "Sem questões novas nem erros para refazer"); allDoneTask = null }, onDismiss = { allDoneTask = null }) }
    missingQuestionsTask?.let { task ->
        AlertDialog(
            onDismissRequest = { missingQuestionsTask = null },
            title = { Text("Questões ainda não salvas") },
            text = {
                Text("Ainda não há questões salvas para ${task.topicNameSnapshot ?: task.subjectNameSnapshot}. Salve questões deste assunto e volte para iniciar a bateria. A tarefa continua pendente no seu plano.")
            },
            confirmButton = { TextButton(onClick = { missingQuestionsTask = null }) { Text("Entendi") } },
        )
    }
    questionLaunchError?.let { message ->
        AlertDialog(
            onDismissRequest = { questionLaunchError = null },
            title = { Text("Não foi possível iniciar a bateria") },
            text = { Text(message) },
            confirmButton = { TextButton(onClick = { questionLaunchError = null }) { Text("OK") } },
        )
    }
    if (wizard) PlanWizardScreen(competitions, subjects, topics, { wizard = false }) { viewModel.createPlan(it) }
    if (editAvailability) AvailabilityEditorDialog(state.availability, { editAvailability = false }) { viewModel.updateAvailability(it) }
    // Foco rodando desta tarefa (ou do tópico dela): concluir encerra o foco e usa o tempo medido.
    val focusNow by appViewModel.focusSession.collectAsState()
    completion?.let { row ->
        val focusOfTask = focusNow.active && (focusNow.taskId == row.entity.id || (focusNow.taskId == null && focusNow.topicId != null && focusNow.topicId == row.entity.topicId))
        TaskExecutionDialog(row, if (focusOfTask) focusNow.elapsedMinutes().coerceAtLeast(1) else null, { completion = null }, focusRunning = focusOfTask) { input ->
            scope.launch {
                if (focusOfTask) runCatching { appViewModel.stopFocus() }
                viewModel.complete(row.entity.id, input)
            }
        }
    }
    reprogram?.let { row -> ReprogramDialog(row.entity.taskTitlePtBr(), { reprogram = null }) { viewModel.reprogram(row.entity.id, it) } }
    skip?.let { row -> ReasonDialog({ skip = null }) { viewModel.skip(row.entity.id, it) } }
    TransferPlanDialog(transfer, viewModel)
    busy?.let { current -> LoadingDialog(current.title, current.message, scene = br.com.estudario.ui.components.WorkshopKind.PLAN) }

    if (management) {
        PlanManagementScreen(state, { management = false }, viewModel::activate, viewModel::markMaster, viewModel::duplicate, viewModel::archive, viewModel::restore, viewModel::delete, { id -> viewModel.executionCount(id) }, { plan ->
            scope.launch { runCatching { viewModel.exportPlan(plan.id) }.onSuccess { pendingExport = it; savePlan.launch("${plan.name}.plano") } }
        }, onContextJson = { pendingContext = viewModel.exportContextJson(); saveContext.launch("contexto-plano.json") }, onContextText = { pendingContext = viewModel.exportContextText(); saveContext.launch("contexto-plano.txt") }, onEditAvailability = { editAvailability = true }, onUpdateSubject = viewModel::updateSubject)
        return
    }
    val layout = estudarioLayout()
    val gutter = layout.screenGutter
    // Em tela estreita (ou fonte grande) os cinco botões encolhem um pouco para o título continuar legível.
    val actionSize = if (layout.prefersStacking) 40.dp else 48.dp
    Column(Modifier.fillMaxSize()) {
        // Só a linha de título fica fixa (é a "barra" da tela). Progresso e abas rolam junto com as
        // tarefas, antes eles ficavam presos no topo e, com fonte normal, sobrava pouco espaço para ler o plano.
        Row(Modifier.padding(start = gutter, end = gutter - 8.dp, top = 8.dp)) {
            ScreenTitle("Plano", "O que estudar, quando e por quê", stackActionsWhenNarrow = false) {
                Row {
                    IconButton(onClick = onHelp, modifier = Modifier.size(actionSize)) { Icon(Icons.Outlined.HelpOutline, "Como usar o plano") }
                    // Ações menos usadas num menu só: o título respira e a tela fica com cara de app.
                    var more by remember { mutableStateOf(false) }
                    Box {
                        IconButton(
                            onClick = { more = true },
                            modifier = Modifier.size(actionSize)
                                .tourTarget(TourKey.PLAN_IMPORT, tourStep?.key) { appViewModel.reportTourTargetBounds(TourKey.PLAN_IMPORT, it) }
                                .tourTarget(TourKey.PLAN_CREATE, tourStep?.key) { appViewModel.reportTourTargetBounds(TourKey.PLAN_CREATE, it) }
                                .tourTarget(TourKey.PLAN_MANAGE, tourStep?.key) { appViewModel.reportTourTargetBounds(TourKey.PLAN_MANAGE, it) },
                        ) { Icon(Icons.Outlined.MoreVert, "Mais opções do plano") }
                        DropdownMenu(expanded = more, onDismissRequest = { more = false }) {
                            DropdownMenuItem(text = { Text("Novo plano") }, leadingIcon = { Icon(Icons.Outlined.Add, null) }, onClick = { more = false; if (entitlements?.canCreatePlan(state.allPlans.map { it.createdAt }) == false) { planUpgrade = true; return@DropdownMenuItem }; appViewModel.startPlanSetup((competitions.firstOrNull { it.isPrimary } ?: competitions.firstOrNull())?.id) })
                            DropdownMenuItem(text = { Text("Meus planos e ajustes") }, leadingIcon = { Icon(Icons.Outlined.Tune, null) }, onClick = { more = false; management = true })
                        }
                    }
                }
            }
        }
        if (state.loading && state.tasks.isEmpty()) {
            LoadingScreen("Carregando seu plano", "Reunindo tarefas, metas e revisões…")
        } else if (state.activePlan == null) {
            EscolhaDeCaminho(
                onSemIa = { appViewModel.startPlanSetup((competitions.firstOrNull { it.isPrimary } ?: competitions.firstOrNull())?.id) },
                onSelecionar = if (state.allPlans.isEmpty()) null else ({ management = true }),
            )
        } else {
            if (state.loading) LinearProgressIndicator(Modifier.fillMaxWidth())
            Column(Modifier.fillMaxSize()) {
                CalendarScreen(
                    state = state,
                    onEditAvailability = { editAvailability = true },
                    onShiftRoadmapEnd = viewModel::shiftRoadmapEnd,
                    header = {
                        // O resumo do plano rola para fora da tela; as abas Hoje/Semana/Mês/Visão geral
                        // grudam no topo (sticky) para trocar de visão sem precisar voltar lá em cima.
                        item(key = "plan-progress-header") { PlanProgressHeader(state = state) }
                        if (dueReviews > 0) item(key = "plan-reviews-today") { ReviewReminderCard(dueReviews, onReviews) }
                        stickyHeader(key = "plan-section-selector") {
                            PlanSectionSelector(
                                selected = state.selectedSection,
                                onSelected = viewModel::selectSection,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(MaterialTheme.colorScheme.background)
                                    .padding(vertical = 6.dp)
                                    .tourTarget(TourKey.PLAN_TABS, tourStep?.key) { appViewModel.reportTourTargetBounds(TourKey.PLAN_TABS, it) },
                            )
                        }
                    },
                    onOpenTopic = onOpenTopic,
                    onStart = viewModel::start,
                    onFocus = { row ->
                        if (row.entity.type == br.com.estudario.domain.planner.PlanTaskType.QUESTIONS) {
                            scope.launch {
                                runCatching { viewModel.startQuestionTask(row.entity.id) }
                                    .onSuccess { savedCount ->
                                        if (savedCount == 0) missingQuestionsTask = row.entity
                                        else if (savedCount < 0) allDoneTask = row.entity
                                        else onStartQuestions(row.entity, savedCount)
                                    }
                                    .onFailure { error -> questionLaunchError = error.message ?: "Confira se a tarefa continua disponível no plano." }
                            }
                        } else {
                            val topicId = row.entity.topicId
                            val future = row.entity.scheduledEpochDay > java.time.LocalDate.now().toEpochDay()
                            val missing = topicId != null && theories.none { it.topicId == topicId }
                            val toGenerate = if (future && missing) topics.firstOrNull { it.id == topicId } else null
                            if (toGenerate != null) {
                                advanceTopic = toGenerate
                            } else if (topicId != null) {
                                if (row.entity.status == PlanTaskStatus.PLANEJADA) viewModel.start(row.entity.id)
                                // Mesmo ajuste da tela inicial: começar leva para o tópico (onde está o
                                // conteúdo), não direto para um cronômetro em branco.
                                onOpenTopicTask(topicId, row.entity.id)
                            } else {
                                if (row.entity.status == PlanTaskStatus.PLANEJADA) viewModel.start(row.entity.id)
                                appViewModel.startFocus(
                                    title = listOfNotNull(row.entity.subjectNameSnapshot, row.entity.topicNameSnapshot).joinToString(" › ").ifBlank { "Tarefa do plano" },
                                    topicId = null,
                                    taskId = row.entity.id,
                                )
                                onFocus()
                            }
                        }
                    },
                    onComplete = { completion = it },
                    onReprogram = { reprogram = it },
                    onSkip = { skip = it },
                    onToggleLock = viewModel::toggleTaskLock,
                    onToggleDayLock = viewModel::toggleDayLock,
                    onGenerate = viewModel::generate,
                )
            }
        }
    }
}

@Composable
private fun PlanSectionSelector(selected: PlanSection, onSelected: (PlanSection) -> Unit, modifier: Modifier = Modifier) {
    // Só os chips: o título "Visão do plano" + legenda disputavam a mesma linha e se atropelavam em
    // tela estreita, e como este seletor agora fica grudado no topo, cada linha a menos é conteúdo a mais.
    Row(
        modifier.horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        PlanSection.entries.forEach { section ->
            FilterChip(
                selected = section == selected,
                onClick = { onSelected(section) },
                label = { Text(section.label, maxLines = 1, overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis) },
            )
        }
    }
}

/**
 * Os dois caminhos do plano, lado a lado e com o mesmo peso. O de dentro do app é o padrão porque
 * funciona offline e na hora; o com IA entra quando a pessoa quer um plano escrito sob medida.
 */
@Composable
private fun EscolhaDeCaminho(onSemIa: () -> Unit, onSelecionar: (() -> Unit)?) {
    Column(
        Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(estudarioLayout().screenGutter),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        Text("Vamos montar seu plano", style = MaterialTheme.typography.titleLarge, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
        Text(
            "O Estudário monta o plano a partir do seu edital, do seu tempo e da data da prova.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        CaminhoCard(
            titulo = "Montar pelo Estudário",
            selo = "Recomendado",
            corpo = "O app aplica o método completo: teoria com questões logo depois, revisão espaçada, rodízio das matérias por peso, simulado no seu dia mais livre e fases até a data da prova. Você vê a prévia antes de criar e pode conferir por que cada tarefa entrou.",
            rodape = "Funciona offline, na hora, e replaneja sozinho quando você atrasa.",
            acao = "Montar meu plano",
            destaque = true,
            onClick = onSemIa,
        )
        FlowRow(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            if (onSelecionar != null) TextButton(onClick = onSelecionar) { Text("Selecionar plano existente") }
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
            FlowRow(verticalArrangement = Arrangement.Center, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(titulo, style = MaterialTheme.typography.titleMedium, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold, modifier = Modifier.align(Alignment.CenterVertically))
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

@Composable private fun ReasonDialog(onDismiss: () -> Unit, onConfirm: (String) -> Unit) { var reason by remember { mutableStateOf("") }; AlertDialog(onDismissRequest = onDismiss, title = { Text("Pular atividade") }, text = { OutlinedTextField(reason, { reason = it }, label = { Text("Motivo obrigatório") }) }, confirmButton = { TextButton(enabled = reason.isNotBlank(), onClick = { onConfirm(reason); onDismiss() }) { Text("Pular") } }, dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } }) }
private suspend fun readPlanText(context: Context, uri: Uri): String? = withContext(Dispatchers.IO) { runCatching { context.contentResolver.openInputStream(uri)?.use { it.readBytes().toString(Charsets.UTF_8) } }.getOrNull() }


/** O lembrete das revisões espaçadas do dia, dentro do plano: quantas, quanto tempo e quanto XP. */
@Composable
private fun ReviewReminderCard(count: Int, onOpen: () -> Unit) {
    Surface(
        onClick = onOpen,
        shape = androidx.compose.foundation.shape.RoundedCornerShape(20.dp),
        color = androidx.compose.ui.graphics.Color(0xFFFFF3C4),
        border = androidx.compose.foundation.BorderStroke(2.dp, androidx.compose.ui.graphics.Color(0xFFFFB421)),
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
    ) {
        Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            br.com.estudario.ui.brand.BrandIcon(br.com.estudario.ui.brand.Glyph.Cycle, size = 46.dp)
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text("REVISÃO ESPAÇADA · HOJE", style = MaterialTheme.typography.labelSmall, fontWeight = androidx.compose.ui.text.font.FontWeight.Black, color = androidx.compose.ui.graphics.Color(0xFFC47F00))
                Text(if (count == 1) "1 revisão para hoje" else "$count revisões para hoje", style = MaterialTheme.typography.titleMedium, fontWeight = androidx.compose.ui.text.font.FontWeight.Black, color = androidx.compose.ui.graphics.Color(0xFF26234A))
                Text("Uns ${count * 3} minutos · +${count * 20} XP · revisar no dia certo é o que fixa", style = MaterialTheme.typography.bodySmall, color = androidx.compose.ui.graphics.Color(0xFF4E4B6B))
            }
            Text("Revisar", style = MaterialTheme.typography.labelLarge, fontWeight = androidx.compose.ui.text.font.FontWeight.Black, color = androidx.compose.ui.graphics.Color(0xFF5B4BF5))
        }
    }
}
