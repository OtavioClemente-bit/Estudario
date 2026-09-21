package br.com.estudario.ui.setup

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.FolderOpen
import androidx.compose.material.icons.outlined.OpenInNew
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.School
import androidx.compose.material.icons.outlined.UploadFile
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import br.com.estudario.data.local.CompetitionEntity
import br.com.estudario.data.prompt.EditalPromptBuilder
import br.com.estudario.data.prompt.EditalPromptOptions
import br.com.estudario.data.prompt.PlanPromptBuilder
import br.com.estudario.data.prompt.PlanPromptOptions
import br.com.estudario.data.prompt.PlanSubjectInfo
import br.com.estudario.data.prompt.PromptIds
import br.com.estudario.domain.planner.StudyProfile
import br.com.estudario.domain.setup.InitialSetupSnapshot
import br.com.estudario.domain.setup.InitialSetupStatus
import br.com.estudario.domain.setup.InitialSetupStep
import br.com.estudario.domain.setup.PlanCreationMethod
import br.com.estudario.domain.setup.SyllabusMethod
import br.com.estudario.ui.AppViewModel
import br.com.estudario.ui.TransferState
import br.com.estudario.ui.prompt.sharePromptWithAi
import br.com.estudario.ui.components.LoadingDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate

private val visibleSteps = listOf(
    InitialSetupStep.COMPETITION,
    InitialSetupStep.EXAM_DATE,
    InitialSetupStep.SYLLABUS_METHOD,
    InitialSetupStep.SYLLABUS_REVIEW,
    InitialSetupStep.PROFILE,
    InitialSetupStep.AVAILABILITY,
    InitialSetupStep.PLAN_METHOD,
    InitialSetupStep.PLAN_REVIEW,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InitialSetupFlow(
    viewModel: InitialSetupViewModel,
    appViewModel: AppViewModel,
    onFinished: () -> Unit,
) {
    val uiState by viewModel.state.collectAsState()
    val snapshot = uiState.snapshot
    val operation by viewModel.operation.collectAsState()
    val incomingTransfer by appViewModel.transfer.collectAsState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbar = remember { SnackbarHostState() }

    LaunchedEffect(snapshot.status) {
        if (snapshot.status == InitialSetupStatus.NOT_STARTED) viewModel.begin()
    }
    LaunchedEffect(operation) {
        val message = (operation as? SetupOperation.Success)?.message ?: return@LaunchedEffect
        snackbar.showSnackbar(message)
        viewModel.clearOperation()
    }
    LaunchedEffect(incomingTransfer, snapshot.step) {
        when (val incoming = incomingTransfer) {
            is TransferState.Preview -> if (snapshot.step == InitialSetupStep.SYLLABUS_METHOD) {
                viewModel.adoptEstudoPreview(incoming.raw, incoming.value)
                appViewModel.clearTransfer()
            }
            is TransferState.Error -> {
                viewModel.reportError(incoming.message)
                appViewModel.clearTransfer()
            }
            else -> Unit
        }
    }

    val picker = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        if (uri == null) return@rememberLauncherForActivityResult
        scope.launch {
            runCatching {
                val raw = withContext(Dispatchers.IO) {
                    context.contentResolver.openInputStream(uri)?.bufferedReader()?.use { it.readText() }
                        ?: error("Não consegui ler esse arquivo.")
                }
                if (snapshot.step == InitialSetupStep.PLAN_METHOD) viewModel.inspectPlan(raw)
                else viewModel.inspectEstudo(raw)
            }.onFailure { viewModel.reportError(it.message ?: "Não consegui ler esse arquivo.") }
        }
    }

    val progress = visibleSteps.indexOf(snapshot.step).let { index ->
        if (index < 0) 0f else (index + 1).toFloat() / visibleSteps.size
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (snapshot.step == InitialSetupStep.READY) "Seu ponto de partida" else "Vamos preparar seu estudo") },
                navigationIcon = {
                    if (snapshot.step != InitialSetupStep.INTRO) {
                        androidx.compose.material3.IconButton(onClick = viewModel::goBack) {
                            Icon(Icons.Outlined.ArrowBack, contentDescription = "Voltar")
                        }
                    }
                },
                actions = {
                    if (snapshot.step != InitialSetupStep.READY) {
                        TextButton(onClick = viewModel::defer) { Text("Configurar depois") }
                    }
                },
            )
        },
        snackbarHost = { SnackbarHost(snackbar) },
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .safeDrawingPadding()
                .imePadding(),
        ) {
            if (snapshot.step != InitialSetupStep.INTRO && snapshot.step != InitialSetupStep.READY) {
                LinearProgressIndicator(progress = { progress }, Modifier.fillMaxWidth())
            }
            AnimatedContent(
                targetState = snapshot.step,
                modifier = Modifier.weight(1f),
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "setup-step",
            ) { step ->
                when (step) {
                    InitialSetupStep.INTRO -> IntroStep(onContinue = { viewModel.advance(step, InitialSetupStep.COMPETITION) })
                    InitialSetupStep.COMPETITION -> CompetitionStep(snapshot, uiState.competition, uiState.competitions, viewModel)
                    InitialSetupStep.EXAM_DATE -> ExamDateStep(snapshot, viewModel)
                    InitialSetupStep.SYLLABUS_METHOD -> SyllabusMethodStep(snapshot, operation, viewModel, picker)
                    InitialSetupStep.SYLLABUS_REVIEW -> SyllabusReviewStep(uiState, viewModel)
                    InitialSetupStep.PROFILE -> ProfileStep(snapshot, viewModel)
                    InitialSetupStep.AVAILABILITY -> AvailabilityStep(snapshot, viewModel)
                    InitialSetupStep.PLAN_METHOD -> PlanMethodStep(snapshot, uiState, operation, viewModel, picker)
                    InitialSetupStep.PLAN_REVIEW -> PlanReviewStep(snapshot, viewModel)
                    InitialSetupStep.READY -> ReadyStep(onFinish = { viewModel.finish(); onFinished() })
                }
            }
        }
    }

    if (operation is SetupOperation.Loading) LoadingDialog("Preparando seu estudo", "Analisando apenas o conteúdo real que você enviou…")
    if (operation is SetupOperation.Error) {
        val message = (operation as SetupOperation.Error).message
        AlertDialog(
            onDismissRequest = viewModel::clearOperation,
            title = { Text("Não foi possível continuar") },
            text = { Text(message) },
            confirmButton = { TextButton(onClick = viewModel::clearOperation) { Text("Tentar novamente") } },
            dismissButton = { TextButton(onClick = viewModel::clearOperation) { Text("Voltar") } },
        )
    }
    if (operation is SetupOperation.Preview) {
        ImportPreviewDialog(
            preview = (operation as SetupOperation.Preview).value,
            onConfirm = { viewModel.confirmEstudoImport((operation as SetupOperation.Preview).raw) },
            onDismiss = viewModel::clearOperation,
        )
    }
    if (operation is SetupOperation.PlanPreview) {
        val preview = operation as SetupOperation.PlanPreview
        AlertDialog(
            onDismissRequest = viewModel::clearOperation,
            title = { Text("Confira o plano da IA") },
            text = { Text("${preview.value.planName}\n\n${preview.value.importedTaskCount} tarefa(s) encontradas. ${preview.value.unresolvedReferences.size} referência(s) precisam ser resolvidas.") },
            confirmButton = {
                TextButton(onClick = { viewModel.confirmPlanImport(preview.raw) }, enabled = preview.value.unresolvedReferences.isEmpty()) { Text("Importar plano") }
            },
            dismissButton = { TextButton(onClick = viewModel::clearOperation) { Text("Escolher outro") } },
        )
    }
}

@Composable
private fun IntroStep(onContinue: () -> Unit) {
    SetupPage(
        eyebrow = "Primeiro passo",
        title = "Seu estudo começa com clareza.",
        description = "Em poucos minutos, vamos entender qual prova você quer alcançar, organizar o edital e montar um ritmo que caiba na sua vida.",
        icon = Icons.Outlined.School,
        bottom = { SetupPrimaryButton("Começar", onContinue) },
    ) {
        SetupCard {
            Text("Você decide o ritmo. O Estudário cuida da estrutura.", fontWeight = FontWeight.SemiBold)
            Text("Nada aqui exige banca, ano ou uma data de prova. Se ainda não souber tudo, seguimos mesmo assim.", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun CompetitionStep(snapshot: InitialSetupSnapshot, selected: CompetitionEntity?, competitions: List<CompetitionEntity>, viewModel: InitialSetupViewModel) {
    var name by rememberSaveable(snapshot.competitionId) { mutableStateOf(snapshot.competitionName) }
    var role by rememberSaveable(snapshot.competitionId) { mutableStateOf(snapshot.role) }
    val canContinue = name.trim().length >= 2
    SetupPage(
        eyebrow = "Seu objetivo",
        title = "Qual prova está no seu horizonte?",
        description = "Começamos pelo essencial. Você pode escolher um concurso já salvo ou criar um novo.",
        icon = Icons.Outlined.School,
        bottom = {
            SetupPrimaryButton("Continuar", { viewModel.saveCompetition(name, role) }, enabled = canContinue)
        },
    ) {
        if (competitions.isNotEmpty()) {
            Text("Ou retome um concurso salvo", style = MaterialTheme.typography.titleSmall)
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                competitions.take(8).forEach { competition ->
                    FilterChip(selected = selected?.id == competition.id, onClick = { viewModel.selectCompetition(competition) }, label = { Text(competition.name) })
                }
            }
        }
        OutlinedTextField(name, { name = it }, Modifier.fillMaxWidth(), label = { Text("Nome do concurso") }, placeholder = { Text("Ex.: TRF 3ª Região") }, singleLine = true)
        Spacer(Modifier.height(10.dp))
        OutlinedTextField(role, { role = it }, Modifier.fillMaxWidth(), label = { Text("Cargo ou área (opcional)") }, placeholder = { Text("Ex.: Analista judiciário") }, singleLine = true)
        if (selected != null && selected.name.equals(name.trim(), true)) {
            Spacer(Modifier.height(12.dp))
            AssistChip(onClick = { }, label = { Text("Concurso já salvo neste aparelho") }, leadingIcon = { Icon(Icons.Outlined.CheckCircle, null) })
        }
    }
}

@Composable
private fun ExamDateStep(snapshot: InitialSetupSnapshot, viewModel: InitialSetupViewModel) {
    var date by rememberSaveable(snapshot.competitionId) { mutableStateOf(snapshot.examDate.orEmpty()) }
    val parsed = runCatching { LocalDate.parse(date) }.getOrNull()
    SetupPage(
        eyebrow = "Sem pressão",
        title = "Você já sabe quando é a prova?",
        description = "A data ajuda a dividir as fases. Se ainda não houver edital ou calendário definido, seu plano continua funcionando sem ela.",
        icon = Icons.Outlined.CalendarMonth,
        bottom = {
            SetupPrimaryButton("Continuar", { viewModel.saveExamDate(date) }, enabled = date.isBlank() || parsed != null)
        },
    ) {
        OutlinedTextField(
            value = date,
            onValueChange = { date = it.filter { char -> char.isDigit() || char == '-' }.take(10) },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Data da prova (opcional)") },
            placeholder = { Text("AAAA-MM-DD") },
            supportingText = { Text(if (date.isBlank()) "Você pode adicionar depois." else if (parsed == null) "Use o formato AAAA-MM-DD." else "Data registrada.") },
            isError = date.isNotBlank() && parsed == null,
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        )
        Spacer(Modifier.height(16.dp))
        SetupCard {
            Icon(Icons.Outlined.Schedule, null, tint = MaterialTheme.colorScheme.primary)
            Text("Sem data, o plano trabalha em ciclos de 6 meses e você ajusta quando souber mais.", style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
private fun SyllabusMethodStep(
    snapshot: InitialSetupSnapshot,
    operation: SetupOperation,
    viewModel: InitialSetupViewModel,
    picker: androidx.activity.result.ActivityResultLauncher<Array<String>>,
) {
    var pastedText by rememberSaveable { mutableStateOf("") }
    val method = when (snapshot.syllabusMethod) {
        // Configurações iniciadas em uma versão anterior que apontavam para o ChatGPT seguem pelo fluxo unificado de IA.
        SyllabusMethod.CHATGPT -> SyllabusMethod.DIRECT_AI
        else -> snapshot.syllabusMethod
    }
    SetupPage(
        eyebrow = "Seu edital",
        title = "Como ele chega até aqui?",
        description = "Escolha o caminho mais confortável. O Estudário só aceita conteúdo que você consiga conferir.",
        icon = Icons.Outlined.Description,
        bottom = {
            if (method == SyllabusMethod.MANUAL) {
                SetupPrimaryButton("Revisar matérias", viewModel::confirmManualSyllabus, enabled = snapshot.manualSubjects.isNotEmpty())
            } else {
                Text("Depois de importar um .estudo válido, você verá um resumo antes de confirmar.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        },
    ) {
        SyllabusChoice("Gerar com IA", "Copiar um prompt estruturado e importar o .estudo gerado.", Icons.Outlined.AutoAwesome, method == SyllabusMethod.DIRECT_AI) { viewModel.chooseSyllabusMethod(SyllabusMethod.DIRECT_AI) }
        SyllabusChoice("Importar arquivo .estudo", "Use um edital que você já tenha gerado ou recebido.", Icons.Outlined.UploadFile, method == SyllabusMethod.IMPORT_ESTUDO) { viewModel.chooseSyllabusMethod(SyllabusMethod.IMPORT_ESTUDO) }
        SyllabusChoice("Montar manualmente", "Crie matérias e tópicos agora e edite tudo antes de continuar.", Icons.Outlined.School, method == SyllabusMethod.MANUAL) { viewModel.chooseSyllabusMethod(SyllabusMethod.MANUAL) }

        when (method) {
            SyllabusMethod.DIRECT_AI -> {
                val prompt = remember(snapshot.competitionName, snapshot.role) {
                    EditalPromptBuilder.build(EditalPromptOptions(competitionName = snapshot.competitionName, role = snapshot.role))
                }
                PromptActionCard(
                    prompt = prompt,
                    onImport = { picker.launch(arrayOf("application/json", "text/plain", "*/*")) },
                )
            }
            SyllabusMethod.IMPORT_ESTUDO -> {
                ImportActionCard(
                    pastedText = pastedText,
                    onPastedTextChange = { pastedText = it },
                    onChooseFile = { picker.launch(arrayOf("application/json", "text/plain", "*/*")) },
                    onInspect = { viewModel.inspectEstudo(pastedText) },
                )
            }
            SyllabusMethod.MANUAL -> ManualSyllabusEditor(snapshot, viewModel)
            SyllabusMethod.CHATGPT -> Unit
            null -> Text("Escolha um caminho para continuar.", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        if (operation is SetupOperation.Success) Text((operation as SetupOperation.Success).message, color = MaterialTheme.colorScheme.primary)
    }
}

@Composable
private fun ManualSyllabusEditor(snapshot: InitialSetupSnapshot, viewModel: InitialSetupViewModel) {
    var selectedSubject by rememberSaveable { mutableStateOf("") }
    var newSubject by rememberSaveable { mutableStateOf("") }
    var editingSubject by rememberSaveable { mutableStateOf<String?>(null) }
    var editingSubjectDraft by rememberSaveable { mutableStateOf("") }
    var newTopic by rememberSaveable { mutableStateOf("") }
    var editingTopicIndex by rememberSaveable { mutableStateOf(-1) }
    var editingTopicDraft by rememberSaveable { mutableStateOf("") }
    val subjects = snapshot.manualSubjects

    LaunchedEffect(subjects) {
        if (selectedSubject.isBlank() || subjects.none { it.equals(selectedSubject, ignoreCase = true) }) {
            selectedSubject = subjects.firstOrNull().orEmpty()
        }
        if (editingSubject != null && subjects.none { it.equals(editingSubject, ignoreCase = true) }) {
            editingSubject = null
        }
    }

    val topics = snapshot.manualTopics.entries
        .firstOrNull { it.key.equals(selectedSubject, ignoreCase = true) }
        ?.value
        .orEmpty()

    SetupCard {
        Text("Matérias", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
        Text("Adicione, renomeie ou remova matérias. Depois escolha uma para organizar seus tópicos.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = newSubject,
                onValueChange = { newSubject = it },
                modifier = Modifier.weight(1f),
                label = { Text("Nova matéria") },
                singleLine = true,
            )
            Button(
                onClick = {
                    val clean = newSubject.trim()
                    if (clean.isNotBlank() && subjects.none { it.equals(clean, ignoreCase = true) }) {
                        viewModel.saveManualSubjects(subjects + clean)
                        selectedSubject = clean
                        newSubject = ""
                    }
                },
                enabled = newSubject.trim().isNotBlank() && subjects.none { it.equals(newSubject.trim(), ignoreCase = true) },
            ) { Text("Adicionar") }
        }
        if (subjects.isEmpty()) {
            Text("Ainda não há matérias. Comece adicionando uma acima.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        subjects.forEach { subject ->
            if (editingSubject?.equals(subject, ignoreCase = true) == true) {
                OutlinedTextField(
                    value = editingSubjectDraft,
                    onValueChange = { editingSubjectDraft = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Nome da matéria") },
                    singleLine = true,
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = {
                            val clean = editingSubjectDraft.trim()
                            if (clean.isNotBlank() && subjects.none { !it.equals(subject, ignoreCase = true) && it.equals(clean, ignoreCase = true) }) {
                                viewModel.renameManualSubject(subject, clean)
                                selectedSubject = clean
                                editingSubject = null
                            }
                        },
                        enabled = editingSubjectDraft.trim().isNotBlank(),
                    ) { Text("Salvar") }
                    TextButton(onClick = { editingSubject = null }) { Text("Cancelar") }
                }
            } else {
                ElevatedCard(
                    onClick = { selectedSubject = subject },
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = if (subject.equals(selectedSubject, ignoreCase = true)) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surfaceContainerLow,
                    ),
                ) {
                    Column(Modifier.fillMaxWidth().padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(subject, fontWeight = FontWeight.SemiBold)
                        Text(if (subject.equals(selectedSubject, ignoreCase = true)) "Matéria selecionada" else "Toque para editar os tópicos", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            TextButton(onClick = { editingSubject = subject; editingSubjectDraft = subject }) { Text("Editar") }
                            TextButton(onClick = { viewModel.deleteManualSubject(subject) }) { Text("Excluir", color = MaterialTheme.colorScheme.error) }
                        }
                    }
                }
            }
        }
    }

    if (selectedSubject.isNotBlank() && subjects.any { it.equals(selectedSubject, ignoreCase = true) }) {
        SetupCard {
            Text("Tópicos de $selectedSubject", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Text("Inclua, edite ou exclua cada tópico individualmente.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = newTopic,
                    onValueChange = { newTopic = it },
                    modifier = Modifier.weight(1f),
                    label = { Text("Novo tópico") },
                    singleLine = true,
                )
                Button(
                    onClick = {
                        val clean = newTopic.trim()
                        if (clean.isNotBlank() && topics.none { it.equals(clean, ignoreCase = true) }) {
                            viewModel.addManualTopic(selectedSubject, clean)
                            newTopic = ""
                        }
                    },
                    enabled = newTopic.trim().isNotBlank() && topics.none { it.equals(newTopic.trim(), ignoreCase = true) },
                ) { Text("Adicionar") }
            }
            if (topics.isEmpty()) {
                Text("Ainda não há tópicos nesta matéria.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            topics.forEachIndexed { index, topic ->
                if (editingTopicIndex == index) {
                    OutlinedTextField(
                        value = editingTopicDraft,
                        onValueChange = { editingTopicDraft = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Nome do tópico") },
                        singleLine = true,
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            onClick = {
                                if (editingTopicDraft.trim().isNotBlank()) {
                                    viewModel.renameManualTopic(selectedSubject, index, editingTopicDraft)
                                    editingTopicIndex = -1
                                }
                            },
                            enabled = editingTopicDraft.trim().isNotBlank(),
                        ) { Text("Salvar") }
                        TextButton(onClick = { editingTopicIndex = -1 }) { Text("Cancelar") }
                    }
                } else {
                    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Text(topic, Modifier.weight(1f))
                        TextButton(onClick = { editingTopicIndex = index; editingTopicDraft = topic }) { Text("Editar") }
                        TextButton(onClick = { viewModel.deleteManualTopic(selectedSubject, index) }) { Text("Excluir", color = MaterialTheme.colorScheme.error) }
                    }
                }
            }
        }
    }
}

@Composable
private fun SyllabusReviewStep(uiState: InitialSetupUiState, viewModel: InitialSetupViewModel) {
    var expandedSubjectId by rememberSaveable { mutableStateOf<Long?>(null) }
    SetupPage(
        eyebrow = "Confira antes de seguir",
        title = "Seu edital está com este tamanho",
        description = "Uma visão resumida ajuda você a perceber se a importação fez sentido. A árvore completa continua na aba Edital.",
        icon = Icons.Outlined.CheckCircle,
        bottom = { SetupPrimaryButton("Está certo, continuar", { viewModel.advance(InitialSetupStep.SYLLABUS_REVIEW, InitialSetupStep.PROFILE) }) },
    ) {
        SetupCard {
            Text("${uiState.subjects.size} matéria(s) • ${uiState.topicCount} tópico(s)", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            uiState.subjects.take(8).forEach { subject ->
                val topics = uiState.topicTitlesBySubject[subject.id].orEmpty()
                ElevatedCard(onClick = { expandedSubjectId = if (expandedSubjectId == subject.id) null else subject.id }) {
                    Column(Modifier.fillMaxWidth().padding(10.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(subject.name, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
                        Text("${topics.size} tópico(s)${if (expandedSubjectId == subject.id && topics.isNotEmpty()) ": ${topics.take(4).joinToString(" • ")}" else ""}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
            if (uiState.subjects.size > 8) Text("e mais ${uiState.subjects.size - 8} matéria(s)", style = MaterialTheme.typography.bodySmall)
        }
        if (uiState.subjects.isEmpty()) Text("Ainda não há matérias. Volte e importe um arquivo ou adicione-as manualmente.", color = MaterialTheme.colorScheme.error)
    }
}

@Composable
private fun ProfileStep(snapshot: InitialSetupSnapshot, viewModel: InitialSetupViewModel) {
    SetupPage(
        eyebrow = "Seu momento",
        title = "Onde você está nessa preparação?",
        description = "Isso muda a mistura de teoria, questões e revisão — não é um rótulo permanente.",
        icon = Icons.Outlined.School,
        bottom = { SetupPrimaryButton("Continuar", { viewModel.advance(InitialSetupStep.PROFILE, InitialSetupStep.AVAILABILITY) }) },
    ) {
        StudyProfile.entries.forEach { profile ->
            ChoiceCard(profile.label, profile.summary, selected = snapshot.studyProfile == profile) { viewModel.chooseProfile(profile) }
        }
    }
}

@Composable
private fun AvailabilityStep(snapshot: InitialSetupSnapshot, viewModel: InitialSetupViewModel) {
    val days = listOf("Seg", "Ter", "Qua", "Qui", "Sex", "Sáb", "Dom")
    SetupPage(
        eyebrow = "Seu ritmo",
        title = "Quanto tempo cabe na sua semana?",
        description = "Você pode mudar isso depois. O plano vai distribuir tarefas apenas nos dias disponíveis.",
        icon = Icons.Outlined.Schedule,
        bottom = { SetupPrimaryButton("Continuar", { viewModel.advance(InitialSetupStep.AVAILABILITY, InitialSetupStep.PLAN_METHOD) }) },
    ) {
        days.forEachIndexed { index, day ->
            val minutes = snapshot.availabilityMinutes.getOrElse(index) { 0 }
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                Text(day, Modifier.width(34.dp), fontWeight = FontWeight.SemiBold)
                listOf(0, 60, 120, 180).forEach { option ->
                    FilterChip(selected = minutes == option, onClick = { viewModel.setAvailability(index, option) }, label = { Text(if (option == 0) "Folga" else "${option}m") })
                }
            }
        }
        Text("Bloco preferido", style = MaterialTheme.typography.titleMedium)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf(25, 45, 50, 60, 90).forEach { option -> FilterChip(snapshot.sessionMinutes == option, { viewModel.chooseSessionMinutes(option) }, label = { Text("${option}m") }) }
        }
    }
}

@Composable
private fun PlanMethodStep(
    snapshot: InitialSetupSnapshot,
    uiState: InitialSetupUiState,
    operation: SetupOperation,
    viewModel: InitialSetupViewModel,
    picker: androidx.activity.result.ActivityResultLauncher<Array<String>>,
) {
    SetupPage(
        eyebrow = "Seu plano",
        title = "Deixamos a primeira semana pronta?",
        description = "O plano automático é determinístico, usa o edital real e pode ser refeito quando sua rotina mudar.",
        icon = Icons.Outlined.CalendarMonth,
        bottom = {
            if (snapshot.planMethod == PlanCreationMethod.EXTERNAL_AI) {
                Text("Importe o .plano gerado para revisar antes de concluir.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            } else SetupPrimaryButton("Montar meu plano", { viewModel.createAutomaticPlan() })
        },
    ) {
        ChoiceCard("Montar automaticamente", "Recomendado para começar: distribui suas matérias nos dias disponíveis e já cria a primeira atividade.", snapshot.planMethod == PlanCreationMethod.AUTOMATIC, onClick = { viewModel.choosePlanMethod(PlanCreationMethod.AUTOMATIC) })
        ChoiceCard("Montar com IA externa", "Receba um prompt com seu edital, disponibilidade e IDs reais; depois importe e confira o .plano gerado.", snapshot.planMethod == PlanCreationMethod.EXTERNAL_AI, onClick = { viewModel.choosePlanMethod(PlanCreationMethod.EXTERNAL_AI) })
        if (snapshot.planMethod == PlanCreationMethod.EXTERNAL_AI) {
            val prompt = remember(snapshot.competitionName, uiState.subjects, snapshot.examDate, snapshot.availabilityMinutes) {
                PlanPromptBuilder.build(
                    competitionId = uiState.competition?.let(PromptIds::competition) ?: "concurso-${PromptIds.slug(snapshot.competitionName)}",
                    competitionName = snapshot.competitionName,
                    subjects = uiState.subjects.map { subject -> PlanSubjectInfo(PromptIds.subject(subject), subject.name, emptyList(), 0, null) },
                    o = PlanPromptOptions(examDate = snapshot.examDate?.let { runCatching { LocalDate.parse(it) }.getOrNull() }, dayMinutes = snapshot.availabilityMinutes),
                )
            }
            PlanPromptActionCard(prompt, onImport = { picker.launch(arrayOf("application/json", "text/plain", "*/*")) })
            TextButton(onClick = { viewModel.choosePlanMethod(PlanCreationMethod.AUTOMATIC) }) { Text("Voltar para o plano automático") }
        }
        OutlinedTextField(snapshot.planPreference, viewModel::savePlanPreference, Modifier.fillMaxWidth(), label = { Text("Alguma prioridade? (opcional)") }, placeholder = { Text("Ex.: mais questões de Constitucional") }, minLines = 2)
        Text(if (snapshot.planMethod == PlanCreationMethod.AUTOMATIC) "A primeira versão será criada pelo motor do Estudário; essa observação fica registrada para orientar o próximo ajuste." else "A importação valida referências, datas e tarefas antes de tocar no seu plano.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        if (operation is SetupOperation.Success) Text((operation as SetupOperation.Success).message, color = MaterialTheme.colorScheme.primary)
    }
}

@Composable
private fun PlanReviewStep(snapshot: InitialSetupSnapshot, viewModel: InitialSetupViewModel) {
    val context = LocalContext.current.applicationContext as br.com.estudario.EstudarioApplication
    var taskName by remember(snapshot.lastValidPlanId) { mutableStateOf<String?>(null) }
    LaunchedEffect(snapshot.lastValidPlanId) {
        taskName = snapshot.lastValidPlanId?.let { id -> context.database.plannerDao().tasksForOnce(id).firstOrNull()?.let { task -> task.topicNameSnapshot ?: task.subjectNameSnapshot } }
    }
    SetupPage(
        eyebrow = "Última conferência",
        title = "Seu plano começa assim",
        description = "Veja o resumo e confirme. O restante continua visível e editável na aba Plano.",
        icon = Icons.Outlined.CheckCircle,
        bottom = { SetupPrimaryButton("Tudo certo", viewModel::complete, enabled = snapshot.lastValidPlanId != null) },
    ) {
        SetupCard {
            Text(snapshot.competitionName, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Text("Perfil: ${snapshot.studyProfile.label}")
            Text("Disponibilidade: ${snapshot.availabilityMinutes.count { it > 0 }} dias por semana • blocos de ${snapshot.sessionMinutes} min")
            Text("Prova: ${snapshot.examDate ?: "sem data definida"}")
        }
        SetupCard {
            Icon(Icons.Outlined.School, null, tint = MaterialTheme.colorScheme.primary)
            Text("Primeira atividade", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
            Text(taskName ?: "O plano foi criado; a primeira tarefa aparecerá na Home.", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun ReadyStep(onFinish: () -> Unit) {
    SetupPage(
        eyebrow = "Tudo pronto",
        title = "Agora você tem um próximo passo claro.",
        description = "Seu edital, sua rotina e seu primeiro plano já estão organizados. Quando quiser, ajuste os detalhes — hoje basta começar.",
        icon = Icons.Outlined.CheckCircle,
        bottom = { SetupPrimaryButton("Ir para minha Home", onFinish) },
    ) {
        SetupCard {
            Icon(Icons.Outlined.CheckCircle, null, tint = MaterialTheme.colorScheme.primary)
            Text("Sua primeira atividade está esperando na Home.", fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
private fun SetupPage(
    eyebrow: String,
    title: String,
    description: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    bottom: @Composable () -> Unit,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(Modifier.fillMaxSize().padding(horizontal = 20.dp)) {
        Column(Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(top = 22.dp, bottom = 18.dp), verticalArrangement = Arrangement.spacedBy(14.dp), content = {
            Icon(icon, null, Modifier.size(34.dp), tint = MaterialTheme.colorScheme.primary)
            Text(eyebrow.uppercase(), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
            Text(title, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.SemiBold)
            Text(description, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
            content()
        })
        Row(Modifier.fillMaxWidth().navigationBarsPadding().padding(bottom = 12.dp), horizontalArrangement = Arrangement.End, verticalAlignment = Alignment.CenterVertically) { bottom() }
    }
}

@Composable
private fun SetupCard(content: @Composable ColumnScope.() -> Unit) {
    ElevatedCard(colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow)) { Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp), content = content) }
}

@Composable
private fun SetupPrimaryButton(label: String, onClick: () -> Unit, enabled: Boolean = true) {
    Button(onClick = onClick, enabled = enabled, contentPadding = PaddingValues(horizontal = 22.dp, vertical = 12.dp)) { Text(label); Spacer(Modifier.width(8.dp)); Icon(Icons.Outlined.ArrowForward, null) }
}

@Composable
private fun ChoiceCard(title: String, description: String, selected: Boolean, onClick: () -> Unit) {
    ElevatedCard(onClick = onClick, colors = CardDefaults.elevatedCardColors(containerColor = if (selected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceContainerLow)) {
        Row(Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) { Text(title, fontWeight = FontWeight.SemiBold); Text(description, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant) }
            if (selected) Icon(Icons.Outlined.CheckCircle, null, tint = MaterialTheme.colorScheme.primary)
        }
    }
}

@Composable
private fun SyllabusChoice(title: String, description: String, icon: androidx.compose.ui.graphics.vector.ImageVector, selected: Boolean, onClick: () -> Unit) {
    ElevatedCard(onClick = onClick, colors = CardDefaults.elevatedCardColors(containerColor = if (selected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceContainerLow)) {
        Row(Modifier.fillMaxWidth().padding(14.dp), horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, null, tint = MaterialTheme.colorScheme.primary)
            Column(Modifier.weight(1f)) { Text(title, fontWeight = FontWeight.SemiBold); Text(description, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant) }
            if (selected) Icon(Icons.Outlined.CheckCircle, null, tint = MaterialTheme.colorScheme.primary)
        }
    }
}

@Composable
private fun ImportActionCard(pastedText: String, onPastedTextChange: (String) -> Unit, onChooseFile: () -> Unit, onInspect: () -> Unit) {
    SetupCard {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedButton(onClick = onChooseFile) { Icon(Icons.Outlined.FolderOpen, null); Spacer(Modifier.width(6.dp)); Text("Escolher .estudo") }
            Button(onClick = onInspect, enabled = pastedText.isNotBlank()) { Text("Analisar texto") }
        }
        OutlinedTextField(pastedText, onPastedTextChange, Modifier.fillMaxWidth(), label = { Text("Ou cole o JSON aqui") }, minLines = 5)
    }
}

@Composable
private fun PromptActionCard(prompt: String, onImport: () -> Unit) {
    val context = LocalContext.current
    SetupCard {
        Text("O prompt já vem com o nome do seu concurso e regras para não inventar matérias.", style = MaterialTheme.typography.bodyMedium)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { sharePromptWithAi(context, prompt) }) { Icon(Icons.Outlined.OpenInNew, null); Spacer(Modifier.width(6.dp)); Text("Compartilhar com IA") }
            OutlinedButton(onClick = {
                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                clipboard.setPrimaryClip(ClipData.newPlainText("Prompt do Estudário", prompt))
            }) { Icon(Icons.Outlined.ContentCopy, null); Spacer(Modifier.width(6.dp)); Text("Copiar") }
        }
        TextButton(onClick = onImport) { Icon(Icons.Outlined.UploadFile, null); Spacer(Modifier.width(4.dp)); Text("Importar resposta") }
        Text("A resposta precisa ser um .estudo válido. O Estudário analisa e mostra o resumo antes de gravar.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun PlanPromptActionCard(prompt: String, onImport: () -> Unit) {
    val context = LocalContext.current
    SetupCard {
        Text("A IA recebe somente matérias e disponibilidade já existentes no seu aparelho.", style = MaterialTheme.typography.bodyMedium)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = {
                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                clipboard.setPrimaryClip(ClipData.newPlainText("Prompt de plano do Estudário", prompt))
            }) { Icon(Icons.Outlined.ContentCopy, null); Spacer(Modifier.width(6.dp)); Text("Copiar prompt") }
            OutlinedButton(onClick = { context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://chatgpt.com/"))) }) { Icon(Icons.Outlined.OpenInNew, null); Spacer(Modifier.width(6.dp)); Text("Abrir IA") }
        }
        TextButton(onClick = onImport) { Icon(Icons.Outlined.UploadFile, null); Spacer(Modifier.width(4.dp)); Text("Importar .plano") }
        Text("O Estudário só aplica o plano depois de validar referências e preservar o que já existe.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun ImportPreviewDialog(preview: br.com.estudario.data.transfer.EstudoPreview, onConfirm: () -> Unit, onDismiss: () -> Unit) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Confira seu edital") },
        text = {
            Column(Modifier.verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(preview.competition, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                Text("${preview.subjects.size} matéria(s) • ${preview.topicCount} tópico(s) • ${preview.subtopicCount} subtópico(s)")
                preview.subjects.take(if (expanded) preview.subjects.size else 6).forEach { subject ->
                    Text("• ${subject.name}: ${subject.topicCount} tópico(s), ${subject.questionCount} questão(ões)", style = MaterialTheme.typography.bodySmall)
                }
                if (preview.subjects.size > 6) TextButton(onClick = { expanded = !expanded }) { Text(if (expanded) "Mostrar menos" else "Mostrar todas") }
                HorizontalDivider()
                Text("${preview.theoryCount} teoria(s) • ${preview.summaryCount} resumo(s) • ${preview.questionCount} questão(ões)", style = MaterialTheme.typography.bodySmall)
                if (preview.duplicateCount > 0) Text("${preview.duplicateCount} item(ns) já existem e serão preservados.", color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.bodySmall)
            }
        },
        confirmButton = { Button(onClick = onConfirm) { Text("Importar e continuar") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Escolher outro") } },
    )
}
