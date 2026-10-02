package br.com.estudario.ui.setup

import br.com.estudario.ui.theme.estudarioLayout
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.outlined.Check
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDatePickerState
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import br.com.estudario.data.local.CompetitionEntity
import br.com.estudario.data.prompt.EditalPromptBuilder
import br.com.estudario.data.prompt.EditalPromptOptions
import br.com.estudario.data.prompt.PlanPromptBuilder
import br.com.estudario.data.prompt.PlanPromptOptions
import br.com.estudario.data.prompt.PlanSubjectInfo
import br.com.estudario.data.prompt.PromptIds
import br.com.estudario.domain.planner.StudyProfile
import br.com.estudario.domain.setup.SubjectVariety
import br.com.estudario.domain.setup.InitialSetupSnapshot
import br.com.estudario.domain.setup.PlanCoverageResult
import br.com.estudario.domain.setup.InitialSetupStatus
import br.com.estudario.domain.setup.InitialSetupStep
import br.com.estudario.domain.setup.PlanCreationMethod
import br.com.estudario.domain.setup.SyllabusMethod
import br.com.estudario.domain.setup.formatAvailabilityMinutes
import br.com.estudario.ui.AppViewModel
import br.com.estudario.ui.TransferState
import br.com.estudario.ui.prompt.sharePromptWithAi
import br.com.estudario.ui.prompt.AttachmentPicker
import br.com.estudario.ui.prompt.PromptAttachment
import br.com.estudario.ui.prompt.attachmentFor
import br.com.estudario.ui.components.LoadingDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Locale

fun interface AiPdfUriPermission {
    fun persist(uri: Uri)
}

class ContentResolverAiPdfUriPermission(
    private val resolver: android.content.ContentResolver,
) : AiPdfUriPermission {
    override fun persist(uri: Uri) {
        resolver.takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
}

object InitialSetupAiPdfSource {
    val PICKER_MIME_TYPES: Array<String> = arrayOf("application/pdf")

    fun persist(uri: Uri, permission: AiPdfUriPermission): Uri {
        permission.persist(uri)
        return uri
    }
}

/**
 * Os passos visíveis na barra de progresso, na ordem da conversa.
 *
 * Primeiro o concurso e o edital; depois o peso das matérias na prova; depois como a pessoa está
 * em cada uma; depois a rotina; e só então o resumo antes de montar. É a mesma ordem de
 * [InitialSetupTransitions].
 */
private val visibleSteps = listOf(
    InitialSetupStep.COMPETITION,
    InitialSetupStep.EXAM_DATE,
    InitialSetupStep.SYLLABUS_METHOD,
    InitialSetupStep.SYLLABUS_REVIEW,
    InitialSetupStep.SUBJECT_PRIORITY,
    InitialSetupStep.SUBJECT_DIFFICULTY,
    InitialSetupStep.AVAILABILITY,
    InitialSetupStep.PROFILE,
    InitialSetupStep.PLAN_SUMMARY,
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

    // Aberto pela aba Plano ("novo plano"): Cancelar volta ao app e devolve a configuração como estava.
    val planPrefs = remember(context) { context.getSharedPreferences(PLAN_MODE_PREFS, android.content.Context.MODE_PRIVATE) }
    val planMode = remember(snapshot.status) { planPrefs.contains(PLAN_MODE_PREVIOUS_STATUS) }
    val planFirstStep = snapshot.step == InitialSetupStep.SUBJECT_PRIORITY
    fun cancelPlanMode() = viewModel.cancelPlanMode(
        planPrefs.getString(PLAN_MODE_PREVIOUS_STATUS, null),
        planPrefs.getString(PLAN_MODE_PREVIOUS_STEP, null),
    ).also { planPrefs.edit().remove(PLAN_MODE_PREVIOUS_STATUS).remove(PLAN_MODE_PREVIOUS_STEP).apply() }

    // Voltar do sistema volta um passo, igual à seta do topo; só na primeira tela ele sai do app.
    // No novo plano, voltar da primeira etapa do plano sai do assistente.
    androidx.activity.compose.BackHandler(enabled = snapshot.step != InitialSetupStep.INTRO) {
        if (planMode && planFirstStep) cancelPlanMode() else viewModel.goBack()
    }

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

    // Anexo do edital (PDF): escolhido já no passo do nome do concurso, para ir junto quando a
    // pessoa enviar o prompt pra IA. Fica aqui em cima (e não dentro do passo) porque cada passo
    // sai de composição ao avançar; sem isso, o anexo se perderia entre um passo e outro. Muitas
    // IAs gratuitas só respondem direito com o PDF em mãos; sem ele, dependem de pesquisar na
    // internet, o que nem sempre funciona nos planos grátis.
    val editalAttachment by viewModel.editalAttachment.collectAsState()
    val aiPdfPermission = remember(context) { ContentResolverAiPdfUriPermission(context.contentResolver) }
    val editalAttach = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        uri?.let {
            runCatching {
                val persistedUri = InitialSetupAiPdfSource.persist(it, aiPdfPermission)
                viewModel.setEditalAttachment(context.attachmentFor(persistedUri))
            }.onFailure {
                viewModel.reportError("Não foi possível manter acesso ao PDF selecionado.")
            }
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

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        when {
                            snapshot.step == InitialSetupStep.READY -> "Seu ponto de partida"
                            planMode -> "Novo plano"
                            else -> "Configuração inicial"
                        },
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                    )
                },
                navigationIcon = {
                    if (snapshot.step != InitialSetupStep.INTRO) {
                        androidx.compose.material3.IconButton(onClick = { if (planMode && planFirstStep) cancelPlanMode() else viewModel.goBack() }) {
                            Icon(Icons.Outlined.ArrowBack, contentDescription = "Voltar")
                        }
                    }
                },
                actions = {
                    if (snapshot.step != InitialSetupStep.READY) {
                        if (planMode) TextButton(onClick = { cancelPlanMode() }) { Text("Cancelar") }
                        else TextButton(onClick = viewModel::defer) { Text("Configurar depois") }
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
                // O Scaffold já aplicou barra de status e de navegação; consumir evita o recuo
                // duplicado (o vão vazio que aparecia embaixo da barra superior).
                .consumeWindowInsets(padding)
                .imePadding(),
        ) {
            val stepIndex = visibleSteps.indexOf(snapshot.step)
            if (snapshot.step != InitialSetupStep.INTRO && snapshot.step != InitialSetupStep.READY && stepIndex >= 0) {
                SetupProgress(stepIndex, visibleSteps.size)
            }
            AnimatedContent(
                targetState = snapshot.step,
                modifier = Modifier.weight(1f),
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "setup-step",
            ) { step ->
                when (step) {
                    InitialSetupStep.INTRO -> IntroStep(onContinue = { viewModel.advance(step, InitialSetupStep.COMPETITION) })
                    InitialSetupStep.COMPETITION -> CompetitionStep(
                        snapshot, uiState.competition, uiState.competitions, viewModel,
                        editalAttachment = editalAttachment,
                        onPickEditalAttachment = { editalAttach.launch(InitialSetupAiPdfSource.PICKER_MIME_TYPES) },
                        onClearEditalAttachment = { viewModel.setEditalAttachment(null) },
                    )
                    InitialSetupStep.EXAM_DATE -> ExamDateStep(snapshot, viewModel)
                    InitialSetupStep.SYLLABUS_METHOD -> SyllabusMethodStep(
                        snapshot, operation, viewModel, picker,
                        editalAttachment = editalAttachment,
                        onPickEditalAttachment = { editalAttach.launch(InitialSetupAiPdfSource.PICKER_MIME_TYPES) },
                        onClearEditalAttachment = { viewModel.setEditalAttachment(null) },
                        onOpenIntegratedAi = { attachment ->
                            viewModel.selectedAiTarget()?.let {
                                appViewModel.openAiReview(it.id, it.title, attachment?.uri?.toString(), attachment?.name, it.preferences)
                            }
                        },
                    )
                    InitialSetupStep.SYLLABUS_REVIEW -> SyllabusReviewStep(
                        uiState = uiState,
                        onContinue = { viewModel.advance(InitialSetupStep.SYLLABUS_REVIEW, InitialSetupStep.SUBJECT_PRIORITY) },
                        onAddSubject = { viewModel.addReviewSubject(it) },
                        onAddTopic = { subjectId, title -> viewModel.addReviewTopic(subjectId, title) },
                        onRemoveSubject = { viewModel.removeReviewSubject(it) },
                        onRemoveTopic = { viewModel.removeReviewTopic(it) },
                    )
                    InitialSetupStep.SUBJECT_PRIORITY -> SubjectPriorityStep(
                        subjects = uiState.subjects,
                        topics = uiState.topicEntities,
                        snapshot = snapshot,
                        officialPriorities = uiState.officialPrioritiesBySubjectId,
                        onSelect = { subjectId, priority -> viewModel.setSubjectPriority(subjectId, priority) },
                        onReset = { subjectId -> viewModel.clearSubjectPriority(subjectId) },
                        onContinue = { viewModel.advance(InitialSetupStep.SUBJECT_PRIORITY, InitialSetupStep.SUBJECT_DIFFICULTY) },
                    )
                    InitialSetupStep.SUBJECT_DIFFICULTY -> SubjectProfileStep(
                        subjects = uiState.subjects,
                        snapshot = snapshot,
                        officialPriorities = uiState.officialPrioritiesBySubjectId,
                        onDifficulty = { subjectId, value -> viewModel.setSubjectDifficulty(subjectId, value) },
                        onKnowledge = { subjectId, value -> viewModel.setSubjectKnowledge(subjectId, value) },
                        onContinue = { viewModel.advance(InitialSetupStep.SUBJECT_DIFFICULTY, InitialSetupStep.AVAILABILITY) },
                    )
                    InitialSetupStep.AVAILABILITY -> AvailabilityStep(snapshot, viewModel)
                    InitialSetupStep.PROFILE -> ProfileStep(snapshot, viewModel)
                    InitialSetupStep.PLAN_SUMMARY -> PlanSummaryStep(
                        snapshot = snapshot,
                        subjects = uiState.subjects,
                        topics = uiState.topicEntities,
                        officialPriorities = uiState.officialPrioritiesBySubjectId,
                        onContinue = { viewModel.advance(InitialSetupStep.PLAN_SUMMARY, InitialSetupStep.PLAN_METHOD) },
                        onReviewAvailability = { viewModel.jumpTo(InitialSetupStep.AVAILABILITY) },
                    )
                    InitialSetupStep.PLAN_METHOD -> PlanMethodStep(snapshot, uiState, operation, viewModel, picker)
                    InitialSetupStep.PLAN_REVIEW -> PlanReviewStep(snapshot, uiState, viewModel)
                    InitialSetupStep.READY -> ReadyStep(onFinish = {
                        planPrefs.edit().remove(PLAN_MODE_PREVIOUS_STATUS).remove(PLAN_MODE_PREVIOUS_STEP).apply()
                        viewModel.finish(); onFinished()
                    })
                }
            }
        }
    }

    if (operation is SetupOperation.Loading) LoadingDialog(
        title = "Preparando seu estudo",
        message = "Analisando apenas o conteúdo real que você enviou…",
        steps = listOf(
            "Lendo o conteúdo que você enviou",
            "Conferindo matérias e tópicos",
            "Organizando seu estudo",
            "Quase pronto",
        ),
    )
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
            title = { Text("Confira o plano do assistente") },
            text = { Text("${preview.value.planName}\n\n${preview.value.importedTaskCount} tarefa(s) encontradas. ${preview.value.unresolvedReferences.size} referência(s) precisam ser resolvidas.") },
            confirmButton = {
                TextButton(onClick = { viewModel.confirmPlanImport(preview.raw) }, enabled = preview.value.unresolvedReferences.isEmpty()) { Text("Importar plano") }
            },
            dismissButton = { TextButton(onClick = viewModel::clearOperation) { Text("Escolher outro") } },
        )
    }
    if (operation is SetupOperation.PlanCoverageError) {
        val issue = operation as SetupOperation.PlanCoverageError
        AlertDialog(
            onDismissRequest = {},
            title = { Text("O plano não cobre todo o edital") },
            text = {
                Text(
                    text = issue.result.asReadableCoverageSummary(),
                    modifier = Modifier.heightIn(max = 360.dp).verticalScroll(rememberScrollState()),
                )
            },
            confirmButton = {
                TextButton(onClick = { picker.launch(arrayOf("application/json", "text/plain", "*/*")) }) {
                    Text("Importar outro arquivo")
                }
            },
            dismissButton = {
                TextButton(onClick = viewModel::createAutomaticPlan) { Text("Usar plano automático") }
            },
        )
    }
}

private fun PlanCoverageResult.asReadableCoverageSummary(): String = buildList {
    if (missingTopics.isNotEmpty()) {
        add("Tópicos sem tarefa (${missingTopics.size}):")
        missingTopics.forEach { add("• ${it.subjectName}: ${it.topicName} [${it.topicId}]") }
    }
    if (missingSubjects.isNotEmpty()) {
        add("Matérias sem tarefa (${missingSubjects.size}):")
        missingSubjects.forEach { add("• $it") }
    }
    if (overCapacityDates.isNotEmpty()) {
        add("Dias que excedem sua disponibilidade (${overCapacityDates.size}):")
        overCapacityDates.forEach { add("• $it") }
    }
    if (availabilityMismatchDays.isNotEmpty()) {
        add("A disponibilidade do arquivo difere da sua seleção nos dias: ${availabilityMismatchDays.joinToString()}. Gere outro plano com os minutos originais.")
    }
}.joinToString("\n")

@Composable
private fun IntroStep(onContinue: () -> Unit) {
    SetupPage(
        eyebrow = "Primeiro passo",
        title = "Olá! Eu sou o assistente do Estudário.",
        description = "Em poucos minutos vou entender qual prova você quer alcançar, organizar o seu edital e montar um plano que caiba na sua vida. É só ir respondendo.",
        icon = Icons.Outlined.School,
        bottom = { SetupPrimaryButton("Começar", onContinue) },
    ) {
        Box(
            Modifier.fillMaxWidth().height(170.dp).clip(RoundedCornerShape(26.dp)).background(
                androidx.compose.ui.graphics.Brush.radialGradient(
                    listOf(MaterialTheme.colorScheme.primary.copy(alpha = 0.22f), MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f)),
                ),
            ),
            contentAlignment = Alignment.Center,
        ) { br.com.estudario.ui.components.EstudarioGlyph(size = 104.dp) }
        SetupCard {
            IntroPoint(Icons.Outlined.School, "Seu objetivo", "A prova que você quer e, se souber, a data.")
            IntroPoint(Icons.Outlined.Description, "Suas matérias", "Pelo edital, por arquivo ou montadas à mão.")
            IntroPoint(Icons.Outlined.CalendarMonth, "Seu plano", "Um ritmo que cabe na sua semana.")
        }
        Text("Nada aqui exige banca, ano ou uma data de prova. Se ainda não souber tudo, seguimos mesmo assim.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun IntroPoint(icon: androidx.compose.ui.graphics.vector.ImageVector, title: String, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(14.dp)) {
        Box(
            Modifier.size(38.dp).clip(CircleShape).background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center,
        ) { Icon(icon, null, Modifier.size(20.dp), tint = MaterialTheme.colorScheme.onPrimaryContainer) }
        Column {
            Text(title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
            Text(text, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun CompetitionStep(
    snapshot: InitialSetupSnapshot,
    selected: CompetitionEntity?,
    competitions: List<CompetitionEntity>,
    viewModel: InitialSetupViewModel,
    editalAttachment: PromptAttachment?,
    onPickEditalAttachment: () -> Unit,
    onClearEditalAttachment: () -> Unit,
) {
    var name by rememberSaveable(snapshot.competitionId) { mutableStateOf(snapshot.competitionName) }
    var role by rememberSaveable(snapshot.competitionId) { mutableStateOf(snapshot.role) }
    val canContinue = name.trim().length >= 2
    SetupPage(
        eyebrow = "Seu objetivo",
        title = "Qual concurso você vai prestar?",
        description = "Começo pelo essencial. Escolha um concurso já salvo ou me diga o nome e o cargo.",
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

        Spacer(Modifier.height(16.dp))
        SetupCard {
            Text("Já aproveite e anexe o edital", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
            Text(
                "Com o PDF, as matérias saem iguais às do edital publicado. Se puder, escolha a versão com o conteúdo programático (as matérias) já incluído. É opcional, e dá pra anexar depois, no passo do edital.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            AttachmentPicker(editalAttachment, "Anexar edital (PDF)", onPickEditalAttachment, onClearEditalAttachment)
        }
    }
}

private val examDateDisplayFormatter: DateTimeFormatter =
    DateTimeFormatter.ofPattern("d 'de' MMMM 'de' yyyy", Locale("pt", "BR"))

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ExamDateStep(snapshot: InitialSetupSnapshot, viewModel: InitialSetupViewModel) {
    var date by rememberSaveable(snapshot.competitionId) { mutableStateOf(snapshot.examDate.orEmpty()) }
    var showPicker by rememberSaveable { mutableStateOf(false) }
    val parsed = runCatching { LocalDate.parse(date) }.getOrNull()
    val dateIsBeforeStart = parsed?.isBefore(LocalDate.now()) == true
    val isError = date.isNotBlank() && (parsed == null || dateIsBeforeStart)

    SetupPage(
        eyebrow = "Sem pressão",
        title = snapshot.competitionName.trim().takeIf { it.isNotEmpty() }?.let { "Você já sabe quando é a prova de $it?" } ?: "Você já sabe quando é a prova?",
        description = "A data me ajuda a dividir as fases. Se ainda não houver calendário, o plano funciona sem ela.",
        icon = Icons.Outlined.CalendarMonth,
        bottom = {
            SetupPrimaryButton("Continuar", { viewModel.saveExamDate(date) }, enabled = date.isBlank() || (parsed != null && !dateIsBeforeStart))
        },
    ) {
        // Campo somente-calendário: ninguém digita uma data de prova errada. Toque em qualquer
        // ponto do campo abre o calendário; o texto é só a leitura do que foi escolhido.
        Box {
            OutlinedTextField(
                value = parsed?.format(examDateDisplayFormatter).orEmpty(),
                onValueChange = {},
                modifier = Modifier.fillMaxWidth(),
                enabled = false,
                label = { Text("Data da prova (opcional)") },
                placeholder = { Text("Toque para escolher no calendário") },
                trailingIcon = { Icon(Icons.Outlined.CalendarMonth, null, tint = MaterialTheme.colorScheme.primary) },
                supportingText = {
                    Text(
                        when {
                            date.isBlank() -> "Você pode adicionar depois."
                            dateIsBeforeStart -> "Essa data já passou. Escolha outra."
                            parsed == null -> "Data inválida. Escolha outra no calendário."
                            else -> "Data registrada."
                        },
                    )
                },
                isError = isError,
                colors = OutlinedTextFieldDefaults.colors(
                    disabledTextColor = MaterialTheme.colorScheme.onSurface,
                    disabledBorderColor = if (isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.outline,
                    disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledTrailingIconColor = MaterialTheme.colorScheme.primary,
                    disabledSupportingTextColor = if (isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant,
                ),
            )
            Box(
                Modifier
                    .matchParentSize()
                    .clip(MaterialTheme.shapes.extraSmall)
                    .clickable(onClick = { showPicker = true }),
            )
        }
        if (date.isNotBlank()) {
            TextButton(onClick = { date = "" }) { Text("Remover data") }
        }

        Spacer(Modifier.height(16.dp))
        SetupCard {
            Icon(Icons.Outlined.Schedule, null, tint = MaterialTheme.colorScheme.primary)
            Text("Sem data, o plano trabalha em ciclos de quatro semanas. Você pode gerar o próximo bloco e ajustar quando souber mais.", style = MaterialTheme.typography.bodyMedium)
        }
    }

    if (showPicker) {
        val today = LocalDate.now()
        val initialMillis = (parsed?.takeIf { !it.isBefore(today) } ?: today)
            .atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()
        val pickerState = rememberDatePickerState(
            initialSelectedDateMillis = initialMillis,
            selectableDates = object : SelectableDates {
                override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                    val candidate = Instant.ofEpochMilli(utcTimeMillis).atZone(ZoneOffset.UTC).toLocalDate()
                    return !candidate.isBefore(today)
                }
            },
        )
        DatePickerDialog(
            onDismissRequest = { showPicker = false },
            confirmButton = {
                TextButton(onClick = {
                    pickerState.selectedDateMillis?.let { millis ->
                        date = Instant.ofEpochMilli(millis).atZone(ZoneOffset.UTC).toLocalDate().toString()
                    }
                    showPicker = false
                }) { Text("Confirmar") }
            },
            dismissButton = {
                TextButton(onClick = { showPicker = false }) { Text("Cancelar") }
            },
        ) {
            DatePicker(state = pickerState)
        }
    }
}

@Composable
internal fun SyllabusMethodStep(
    snapshot: InitialSetupSnapshot,
    operation: SetupOperation,
    viewModel: InitialSetupViewModel,
    picker: androidx.activity.result.ActivityResultLauncher<Array<String>>,
    editalAttachment: PromptAttachment?,
    onPickEditalAttachment: () -> Unit,
    onClearEditalAttachment: () -> Unit,
    onOpenIntegratedAi: (PromptAttachment?) -> Unit,
) {
    var pastedText by rememberSaveable { mutableStateOf("") }
    val method = when (snapshot.syllabusMethod) {
        // Configurações iniciadas em uma versão anterior que apontavam para o ChatGPT seguem pelo fluxo unificado de IA.
        SyllabusMethod.CHATGPT -> SyllabusMethod.DIRECT_AI
        else -> snapshot.syllabusMethod
    }
    SetupPage(
        eyebrow = "Seu edital",
        title = "Como eu trago o seu edital?",
        description = "O jeito mais rápido é eu ler o PDF oficial. Você confere tudo antes de salvar.",
        icon = Icons.Outlined.Description,
        bottom = {
            if (method == SyllabusMethod.MANUAL) {
                SetupPrimaryButton("Revisar matérias", viewModel::confirmManualSyllabus, enabled = snapshot.manualSubjects.isNotEmpty())
            } else {
                Text("Depois de importar um .estudo válido, você verá um resumo antes de confirmar.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        },
    ) {
        ChoiceCard("Gerar com o Estudário", "O Estudário lê o edital e organiza matérias e tópicos para você revisar.", method == SyllabusMethod.DIRECT_AI, icon = Icons.Outlined.AutoAwesome, badge = "Recomendado") { viewModel.chooseSyllabusMethod(SyllabusMethod.DIRECT_AI) }
        ChoiceCard("Importar arquivo .estudo", "Use um edital que você já tenha gerado ou recebido.", method == SyllabusMethod.IMPORT_ESTUDO, icon = Icons.Outlined.UploadFile) { viewModel.chooseSyllabusMethod(SyllabusMethod.IMPORT_ESTUDO) }
        ChoiceCard("Montar manualmente", "Crie matérias e tópicos agora e edite tudo antes de continuar.", method == SyllabusMethod.MANUAL, icon = Icons.Outlined.School) { viewModel.chooseSyllabusMethod(SyllabusMethod.MANUAL) }

        when (method) {
            SyllabusMethod.DIRECT_AI -> {
                val prompt = remember(snapshot.competitionName, snapshot.role, editalAttachment) {
                    EditalPromptBuilder.build(
                        EditalPromptOptions(
                            competitionName = snapshot.competitionName,
                            role = snapshot.role,
                            attachmentProvided = editalAttachment != null,
                        ),
                    )
                }
                Spacer(Modifier.height(4.dp))
                StudioAiCard(editalAttachment) { onOpenIntegratedAi(editalAttachment) }
                br.com.estudario.ui.ai.AiAccessPanel()
                OrDivider("ou envie para sua IA favorita")
                ExternalAiGuide(
                    prompt = prompt,
                    attachment = editalAttachment,
                    onPickAttachment = onPickEditalAttachment,
                    onClearAttachment = onClearEditalAttachment,
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
                FlowRow(verticalArrangement = Arrangement.spacedBy(4.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
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
                        FlowRow(verticalArrangement = Arrangement.spacedBy(4.dp), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
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
                    FlowRow(verticalArrangement = Arrangement.spacedBy(4.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
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
private fun ProfileStep(snapshot: InitialSetupSnapshot, viewModel: InitialSetupViewModel) {
    SetupPage(
        eyebrow = "Seu momento",
        title = "Onde você está nessa preparação?",
        description = "Isso muda a mistura de teoria, questões e revisão, não é um rótulo permanente.",
        icon = Icons.Outlined.School,
        bottom = { SetupPrimaryButton("Continuar", { viewModel.advance(InitialSetupStep.PROFILE, InitialSetupStep.PLAN_SUMMARY) }) },
    ) {
        StudyProfile.entries.forEach { profile ->
            ChoiceCard(profile.label, profile.summary, selected = snapshot.studyProfile == profile) { viewModel.chooseProfile(profile) }
        }
    }
}

@Composable
private fun AvailabilityStep(snapshot: InitialSetupSnapshot, viewModel: InitialSetupViewModel) {
    val hasTime = snapshot.availabilityMinutes.any { it > 0 }
    SetupPage(
        eyebrow = "Seu ritmo",
        title = "Quanto tempo cabe na sua semana?",
        description = "Arraste cada barra para marcar o tempo líquido do dia, já sem pausas. Zero é folga. Dá para mudar depois.",
        icon = Icons.Outlined.Schedule,
        bottom = {
            if (!hasTime) Text("Marque pelo menos um dia com tempo de estudo.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.error)
            SetupPrimaryButton("Continuar", { viewModel.advance(InitialSetupStep.AVAILABILITY, InitialSetupStep.PROFILE) }, enabled = hasTime)
        },
    ) {
        // Atalhos para o caso comum; depois a pessoa ajusta cada dia arrastando a barra.
        Row(Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf(60, 120, 180, 240).forEach { minutes ->
                val selected = (0..4).all { snapshot.availabilityMinutes.getOrElse(it) { 0 } == minutes }
                FilterChip(selected, { (0..4).forEach { day -> viewModel.setAvailability(day, minutes) } }, label = { Text("${minutes / 60}h seg a sex") })
            }
        }
        br.com.estudario.ui.prompt.WeekHoursPicker(
            minutes = List(7) { snapshot.availabilityMinutes.getOrElse(it) { 0 } },
            onChange = { day, minutes -> viewModel.setAvailability(day, minutes) },
            maxMinutes = 720,
        )
        Text("Tamanho do bloco", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
        br.com.estudario.ui.prompt.BigValueSlider(
            value = snapshot.sessionMinutes,
            range = 15..120,
            step = 5,
            format = ::formatAvailabilityMinutes,
            onChange = viewModel::chooseSessionMinutes,
            caption = "Tamanho-base de cada tarefa, não o total do dia: as tarefas usam múltiplos do bloco dentro do tempo disponível.",
            quickValues = listOf(25, 45, 50, 60, 90),
        )
        // Esta pergunta só existe porque muda comportamento real: ela define o teto diário por
        // matéria e a alternância do rodízio. Se um dia deixar de mudar algo, ela sai do assistente.
        Text("Variar ou aprofundar", style = MaterialTheme.typography.titleMedium)
        Text(
            "Você prefere alternar bastante entre matérias no mesmo dia ou ficar mais tempo na mesma?",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        SubjectVariety.entries.forEach { option ->
            ChoiceCard(
                title = option.label,
                description = option.description,
                selected = snapshot.variety == option,
                onClick = { viewModel.chooseVariety(option) },
            )
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
        bottom = { SetupPrimaryButton("Montar meu plano", { viewModel.createAutomaticPlan() }) },
    ) {
        // O plano é feito pelo motor do app, sem IA: é conta de calendário (horas, pesos e
        // revisões) e sai igual e de graça. Quem vinha de uma versão com IA volta para o automático.
        LaunchedEffect(snapshot.planMethod) {
            if (snapshot.planMethod != PlanCreationMethod.AUTOMATIC) viewModel.choosePlanMethod(PlanCreationMethod.AUTOMATIC)
        }
        OutlinedTextField(snapshot.planPreference, viewModel::savePlanPreference, Modifier.fillMaxWidth(), label = { Text("Alguma prioridade? (opcional)") }, placeholder = { Text("Ex.: mais questões de Constitucional") }, minLines = 2)
        Text("O plano é montado pelo Estudário com o seu edital, suas horas e prioridades. Essa observação fica registrada para orientar o próximo ajuste.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        if (operation is SetupOperation.Success) Text((operation as SetupOperation.Success).message, color = MaterialTheme.colorScheme.primary)
    }
}

@Composable
private fun PlanReviewStep(snapshot: InitialSetupSnapshot, uiState: InitialSetupUiState, viewModel: InitialSetupViewModel) {
    val context = LocalContext.current.applicationContext as br.com.estudario.EstudarioApplication
    var reviewTasks by remember(snapshot.lastValidPlanId) { mutableStateOf<List<br.com.estudario.data.local.planner.PlanTaskEntity>?>(null) }
    LaunchedEffect(snapshot.lastValidPlanId) {
        reviewTasks = snapshot.lastValidPlanId?.let { id -> context.database.plannerDao().tasksForOnce(id) }.orEmpty()
    }
    val tasks = reviewTasks.orEmpty()
    val currentTasks = remember(tasks) {
        tasks.filter {
            it.status != br.com.estudario.domain.planner.PlanTaskStatus.PAUSADA &&
                it.status != br.com.estudario.domain.planner.PlanTaskStatus.REPROGRAMADA
        }
    }
    val plannedTopicIds = remember(currentTasks) { currentTasks.mapNotNullTo(hashSetOf()) { it.topicId } }
    val plannedSubjectIds = remember(currentTasks) { currentTasks.mapNotNullTo(hashSetOf()) { it.subjectId } }
    val sourceSubjects = remember(uiState.subjects, uiState.topicEntities) {
        uiState.subjects.map { subject ->
            br.com.estudario.domain.setup.PlanCoverageSubject(
                id = subject.id.toString(),
                name = subject.name,
                topics = uiState.topicEntities.filter { it.subjectId == subject.id }.map { topic ->
                    br.com.estudario.domain.setup.PlanCoverageTopic(topic.id.toString(), topic.title)
                },
            )
        }
    }
    val coverage = remember(sourceSubjects, currentTasks, snapshot.availabilityMinutes) {
        br.com.estudario.domain.setup.PlanCoverageValidator.validate(
            sourceSubjects = sourceSubjects,
            importedTasks = currentTasks.map { task ->
                br.com.estudario.domain.setup.PlanCoverageTask(
                    subjectId = task.subjectId?.toString(),
                    topicId = task.topicId?.toString(),
                    date = java.time.LocalDate.ofEpochDay(task.scheduledEpochDay),
                    minutes = task.plannedMinutes,
                )
            },
            dayMinutes = snapshot.availabilityMinutes,
        )
    }
    val coveredTopicCount = uiState.topicEntities.count { it.id in plannedTopicIds }
    val topicsWithoutChildren = uiState.subjects.filter { subject -> uiState.topicEntities.none { it.subjectId == subject.id } }
    val coveredStandaloneSubjects = topicsWithoutChildren.count { it.id in plannedSubjectIds }
    val firstTask = currentTasks.firstOrNull()
    val firstTaskName = firstTask?.let { it.topicNameSnapshot ?: it.subjectNameSnapshot }
    val firstDate = currentTasks.minOfOrNull { it.scheduledEpochDay }?.let(java.time.LocalDate::ofEpochDay)
    val lastDate = currentTasks.maxOfOrNull { it.scheduledEpochDay }?.let(java.time.LocalDate::ofEpochDay)
    val examDate = snapshot.examDate?.let { runCatching { java.time.LocalDate.parse(it) }.getOrNull() }
    val remainingDays = examDate?.let { java.time.temporal.ChronoUnit.DAYS.between(java.time.LocalDate.now(), it).coerceAtLeast(0) }
    val dayNames = listOf("Segunda", "Terça", "Quarta", "Quinta", "Sexta", "Sábado", "Domingo")
    SetupPage(
        eyebrow = "Última conferência",
        title = "Seu plano começa assim",
        description = "Veja o resumo e confirme. O restante continua visível e editável na aba Plano.",
        icon = Icons.Outlined.CheckCircle,
        bottom = { SetupPrimaryButton("Concluir configuração", viewModel::complete, enabled = snapshot.lastValidPlanId != null) },
    ) {
        SetupCard {
            Text(snapshot.competitionName, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Text("Perfil de estudo: ${snapshot.studyProfile.label}")
            Text("Método: ${if (snapshot.planMethod == PlanCreationMethod.AUTOMATIC) "plano do Estudário" else "plano do assistente, conferido"}")
            Text("Bloco de estudo: ${snapshot.sessionMinutes} min, é o tamanho-base de cada tarefa, não o total diário.")
            Text("Disponibilidade semanal: ${formatAvailabilityMinutes(snapshot.availabilityMinutes.sum())} em ${snapshot.availabilityMinutes.count { it > 0 }} dias.")
            snapshot.availabilityMinutes.forEachIndexed { index, minutes ->
                Text("${dayNames[index]}: ${formatAvailabilityMinutes(minutes)}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Text("Data da prova: ${snapshot.examDate ?: "não definida"}${remainingDays?.let { " · $it dias a partir de hoje" }.orEmpty()}")
            if (firstDate != null && lastDate != null) Text("Calendário criado: $firstDate a $lastDate (${currentTasks.size} tarefas vigentes)", style = MaterialTheme.typography.bodySmall)
        }
        SetupCard {
            Icon(Icons.Outlined.School, null, tint = MaterialTheme.colorScheme.primary)
            Text("Seus três eixos por matéria", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
            Text(
                "O peso na prova vem do edital; o esforço e a base são seus. São coisas diferentes, e o plano usa as três separadamente.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            uiState.subjects.forEach { subject ->
                val key = subject.id.toString()
                val dimensions = snapshot.dimensionsFor(
                    subjectId = key,
                    examPriority = SetupPlannerPreviewFactory.suggestedPriority(uiState.officialPrioritiesBySubjectId, subject.id),
                )
                Text(subject.name, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
                Text(
                    "Prova: ${dimensions.examPriority.label.lowercase()} · " +
                        "Esforço: ${dimensions.personalDifficulty.label.lowercase()} · " +
                        "Base: ${dimensions.initialKnowledge.label.lowercase()}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
        SetupCard {
            Text("Cobertura do edital", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
            if (reviewTasks == null) {
                Text("Conferindo as tarefas do plano…", color = MaterialTheme.colorScheme.onSurfaceVariant)
            } else {
                Text("Tópicos programados: $coveredTopicCount de ${uiState.topicEntities.size}.")
                if (topicsWithoutChildren.isNotEmpty()) Text("Matérias sem subtópicos contempladas: $coveredStandaloneSubjects de ${topicsWithoutChildren.size}.")
                if (coverage.isComplete) {
                    Text("Todos os tópicos identificados estão no calendário desta versão do plano.", color = MaterialTheme.colorScheme.primary)
                } else {
                    Text("Esta versão ainda não cobre todo o edital no horizonte exibido. Você pode revisar o plano completo na aba Plano.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    if (coverage.overCapacityDates.isNotEmpty()) Text("Há ${coverage.overCapacityDates.size} dia(s) acima do tempo disponível informado.", color = MaterialTheme.colorScheme.error)
                }
            }
        }
        SetupCard {
            Text("Primeiro passo", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
            Text(firstTaskName ?: "O plano foi criado; a primeira tarefa aparecerá na Home.", color = MaterialTheme.colorScheme.onSurfaceVariant)
            firstDate?.let { Text("Programado para $it", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant) }
        }
    }
}

@Composable
private fun ReadyStep(onFinish: () -> Unit) {
    SetupPage(
        eyebrow = "Tudo pronto",
        title = "Agora você tem um próximo passo claro.",
        description = "Seu edital, sua rotina e seu primeiro plano já estão organizados. Quando quiser, ajuste os detalhes; hoje basta começar.",
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
internal fun SetupPage(
    eyebrow: String,
    title: String,
    description: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    showScrollIndicator: Boolean = false,
    bottom: @Composable () -> Unit,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(Modifier.fillMaxSize().padding(horizontal = estudarioLayout().screenGutter)) {
        SetupScrollContainer(Modifier.weight(1f), showScrollIndicator = showScrollIndicator) {
            SetupHeader(eyebrow, title, icon, description)
            Spacer(Modifier.height(4.dp))
            content()
        }
        SetupBottomBar(bottom)
    }
}

/**
 * Cabeçalho de cada passo como fala do assistente: o ✨ do Estudário, o tema da etapa num selo e a
 * pergunta num balão, com a explicação logo abaixo. É o mesmo em [SetupPage] e no assistente do
 * plano, e o mesmo balão dos geradores de IA, para o app inteiro conversar do mesmo jeito.
 */
@Composable
internal fun SetupHeader(
    eyebrow: String,
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    description: String? = null,
    italicDescription: Boolean = false,
) {
    Row(verticalAlignment = Alignment.Top) {
        Box(
            Modifier.padding(top = 26.dp).size(36.dp).clip(androidx.compose.foundation.shape.CircleShape).background(MaterialTheme.colorScheme.primary),
            contentAlignment = Alignment.Center,
        ) { Icon(Icons.Outlined.AutoAwesome, null, Modifier.size(20.dp), tint = MaterialTheme.colorScheme.onPrimary) }
        Spacer(Modifier.width(10.dp))
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Row(
                Modifier.clip(RoundedCornerShape(50)).background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f)).padding(horizontal = 10.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Icon(icon, null, Modifier.size(14.dp), tint = MaterialTheme.colorScheme.primary)
                Text(eyebrow.uppercase(), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
            }
            Surface(
                shape = RoundedCornerShape(topStart = 4.dp, topEnd = 22.dp, bottomEnd = 22.dp, bottomStart = 22.dp),
                color = MaterialTheme.colorScheme.primaryContainer,
            ) {
                Column(Modifier.padding(horizontal = 16.dp, vertical = 14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimaryContainer)
                    if (!description.isNullOrBlank()) Text(
                        description,
                        style = MaterialTheme.typography.bodyMedium,
                        fontStyle = if (italicDescription) androidx.compose.ui.text.font.FontStyle.Italic else null,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.85f),
                    )
                }
            }
        }
    }
}

/** Rodapé fixo: uma linha fina separando do conteúdo e a ação principal ocupando a largura. */
@Composable
internal fun SetupBottomBar(bottom: @Composable () -> Unit) {
    Column(Modifier.fillMaxWidth().navigationBarsPadding()) {
        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
        // FlowRow: com fonte maior os botões do rodapé descem um para cada linha em vez de
        // espremer o rótulo.
        FlowRow(
            Modifier.fillMaxWidth().padding(top = 12.dp, bottom = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) { bottom() }
    }
}

/** Progresso da configuração em segmentos, com a contagem escrita. */
@Composable
private fun SetupProgress(index: Int, total: Int) {
    Column(
        Modifier.fillMaxWidth().padding(horizontal = estudarioLayout().screenGutter).padding(bottom = 8.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Text(
            "Passo ${index + 1} de $total",
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            repeat(total) { i ->
                val color = when {
                    i < index -> MaterialTheme.colorScheme.primary
                    i == index -> MaterialTheme.colorScheme.primary.copy(alpha = 0.55f)
                    else -> MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                }
                Box(Modifier.weight(1f).height(5.dp).clip(RoundedCornerShape(50)).background(color))
            }
        }
    }
}

@Composable
internal fun SetupCard(content: @Composable ColumnScope.() -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surfaceContainerLow,
    ) { Column(Modifier.fillMaxWidth().padding(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp), content = content) }
}

@Composable
internal fun SetupPrimaryButton(label: String, onClick: () -> Unit, enabled: Boolean = true) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier.fillMaxWidth().height(56.dp),
        shape = RoundedCornerShape(18.dp),
    ) {
        Text(label, style = MaterialTheme.typography.titleSmall)
        Spacer(Modifier.width(8.dp))
        Icon(Icons.Outlined.ArrowForward, null, Modifier.size(20.dp))
    }
}

/**
 * Opção selecionável: borda de destaque e o marcador à direita quando escolhida. Serve às escolhas
 * simples (perfil, ritmo) e, com ícone e selo, aos caminhos do edital.
 */
@Composable
private fun ChoiceCard(
    title: String,
    description: String,
    selected: Boolean,
    icon: androidx.compose.ui.graphics.vector.ImageVector? = null,
    badge: String? = null,
    onClick: () -> Unit,
) {
    val border = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f)
    Surface(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = if (selected) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.55f) else MaterialTheme.colorScheme.surfaceContainerLow,
        border = androidx.compose.foundation.BorderStroke(if (selected) 2.dp else 1.dp, border),
    ) {
        Row(Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.spacedBy(14.dp), verticalAlignment = Alignment.CenterVertically) {
            if (icon != null) Box(
                Modifier.size(44.dp).clip(RoundedCornerShape(14.dp)).background(
                    if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primaryContainer,
                ),
                contentAlignment = Alignment.Center,
            ) { Icon(icon, null, Modifier.size(22.dp), tint = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onPrimaryContainer) }
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                    if (badge != null) Text(
                        badge,
                        Modifier.clip(RoundedCornerShape(50)).background(br.com.estudario.ui.theme.estudarioColors().completed).padding(horizontal = 8.dp, vertical = 2.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = br.com.estudario.ui.theme.estudarioColors().onCompleted,
                        fontWeight = FontWeight.Bold,
                    )
                }
                Text(description, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            SelectionMark(selected)
        }
    }
}

@Composable
private fun SelectionMark(selected: Boolean) {
    Box(
        Modifier.size(22.dp).clip(CircleShape).then(
            if (selected) Modifier.background(MaterialTheme.colorScheme.primary)
            else Modifier.border(2.dp, MaterialTheme.colorScheme.outlineVariant, CircleShape),
        ),
        contentAlignment = Alignment.Center,
    ) { if (selected) Icon(Icons.Outlined.Check, null, Modifier.size(15.dp), tint = MaterialTheme.colorScheme.onPrimary) }
}

@Composable
private fun ImportActionCard(pastedText: String, onPastedTextChange: (String) -> Unit, onChooseFile: () -> Unit, onInspect: () -> Unit) {
    SetupCard {
        // Área de arquivo em destaque: é o caminho mais comum.
        Surface(
            onClick = onChooseFile,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
            border = androidx.compose.foundation.BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)),
        ) {
            Column(Modifier.padding(vertical = 22.dp, horizontal = 16.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Icon(Icons.Outlined.UploadFile, null, Modifier.size(34.dp), tint = MaterialTheme.colorScheme.primary)
                Text("Escolher .estudo", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.primary)
                Text("Você vê um resumo antes de importar.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        OrDivider("ou cole o conteúdo")
        OutlinedTextField(pastedText, onPastedTextChange, Modifier.fillMaxWidth(), label = { Text("Ou cole o JSON aqui") }, minLines = 4, shape = RoundedCornerShape(14.dp))
        OutlinedButton(onClick = onInspect, enabled = pastedText.isNotBlank(), modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp)) { Text("Analisar texto") }
    }
}

@Composable
private fun OrDivider(label: String) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        HorizontalDivider(Modifier.weight(1f), color = MaterialTheme.colorScheme.outlineVariant)
        Text(label, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        HorizontalDivider(Modifier.weight(1f), color = MaterialTheme.colorScheme.outlineVariant)
    }
}

/**
 * O cartão principal do caminho com IA: a IA do próprio Estudário, sobre o degradê da IA. Um
 * toque abre a tela da IA com o edital (e o PDF, se já anexado) selecionados.
 */
@Composable
private fun StudioAiCard(
    attachment: PromptAttachment?,
    description: String = "Envie o PDF oficial e receba as matérias e os tópicos organizados. Você confere tudo antes de salvar.",
    buttonLabel: String = "Gerar com o Estudário",
    onOpen: () -> Unit,
) {
    Box(Modifier.fillMaxWidth().clip(RoundedCornerShape(24.dp)).background(br.com.estudario.ui.ai.aiGradient())) {
        Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Box(
                    Modifier.size(44.dp).clip(RoundedCornerShape(14.dp)).background(Color.White.copy(alpha = 0.18f)),
                    contentAlignment = Alignment.Center,
                ) { Icon(Icons.Outlined.AutoAwesome, null, tint = Color.White) }
                Column(Modifier.weight(1f)) {
                    Text("Assistente Estudário", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color.White)
                    Text("Mais rápido: tudo dentro do app", style = MaterialTheme.typography.bodySmall, color = Color.White.copy(alpha = 0.85f))
                }
                Text(
                    "BETA",
                    Modifier.clip(RoundedCornerShape(50)).background(Color.White.copy(alpha = 0.22f)).padding(horizontal = 9.dp, vertical = 3.dp),
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                )
            }
            Text(
                description,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White,
            )
            if (attachment != null) Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Icon(Icons.Outlined.CheckCircle, null, Modifier.size(16.dp), tint = Color.White)
                Text("Vai usar “${attachment.name}”", style = MaterialTheme.typography.labelMedium, color = Color.White, maxLines = 1, overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis)
            }
            Button(
                onClick = onOpen,
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(16.dp),
                colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color(0xFF3B34C4)),
            ) {
                Icon(Icons.Outlined.AutoAwesome, null, Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text(buttonLabel, style = MaterialTheme.typography.titleSmall)
            }
        }
    }
}

/**
 * Um passo numerado do caminho com uma IA de fora. O número vira um "certo" quando o passo já está
 * feito, e uma linha liga um passo ao próximo.
 */
@Composable
private fun GuideStep(
    number: Int,
    title: String,
    description: String,
    done: Boolean = false,
    last: Boolean = false,
    content: @Composable ColumnScope.() -> Unit = {},
) {
    Row(Modifier.fillMaxWidth().height(androidx.compose.foundation.layout.IntrinsicSize.Min), horizontalArrangement = Arrangement.spacedBy(14.dp)) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                Modifier.size(30.dp).clip(CircleShape).background(
                    if (done) br.com.estudario.ui.theme.estudarioColors().completed else MaterialTheme.colorScheme.primary,
                ),
                contentAlignment = Alignment.Center,
            ) {
                if (done) Icon(Icons.Outlined.Check, null, Modifier.size(17.dp), tint = Color.White)
                else Text("$number", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimary)
            }
            if (!last) Box(
                Modifier.padding(vertical = 4.dp).width(2.dp).weight(1f).clip(RoundedCornerShape(1.dp))
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)),
            )
        }
        Column(Modifier.weight(1f).padding(bottom = if (last) 0.dp else 18.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(top = 4.dp))
                Text(description, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            content()
        }
    }
}

/** Caminho com uma IA de fora (ChatGPT, Gemini, Claude…): anexar, enviar e trazer a resposta. */
@Composable
private fun ExternalAiGuide(
    prompt: String,
    attachment: PromptAttachment?,
    onPickAttachment: () -> Unit,
    onClearAttachment: () -> Unit,
    onImport: () -> Unit,
) {
    val context = LocalContext.current
    SetupCard {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Box(
                Modifier.size(40.dp).clip(RoundedCornerShape(12.dp)).background(MaterialTheme.colorScheme.secondaryContainer),
                contentAlignment = Alignment.Center,
            ) { Icon(Icons.Outlined.OpenInNew, null, Modifier.size(20.dp), tint = MaterialTheme.colorScheme.onSecondaryContainer) }
            Column {
                Text("Usar sua IA favorita", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                Text("ChatGPT, Gemini, Claude, Copilot…", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        Spacer(Modifier.height(4.dp))
        GuideStep(
            number = 1,
            title = "Anexe o edital em PDF",
            description = if (attachment != null) "O PDF vai junto quando você enviar." else "Recomendado: com o PDF, a sua IA segue o edital publicado.",
            done = attachment != null,
        ) {
            AttachmentPicker(attachment, "Anexar edital (PDF)", onPickAttachment, onClearAttachment)
        }
        GuideStep(
            number = 2,
            title = "Envie o pedido para sua IA",
            description = "Ele já leva o nome do seu concurso e regras para não inventar matérias.",
        ) {
            // Um embaixo do outro: com fonte grande, lado a lado cortava o rótulo.
            Button(
                onClick = { sharePromptWithAi(context, prompt, attachment) },
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(14.dp),
            ) { Icon(Icons.Outlined.OpenInNew, null, Modifier.size(18.dp)); Spacer(Modifier.width(8.dp)); Text("Enviar para minha IA") }
            OutlinedButton(
                onClick = {
                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    clipboard.setPrimaryClip(ClipData.newPlainText("Pedido do Estudário", prompt))
                },
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(14.dp),
            ) { Icon(Icons.Outlined.ContentCopy, null, Modifier.size(18.dp)); Spacer(Modifier.width(8.dp)); Text("Copiar pedido") }
        }
        GuideStep(
            number = 3,
            title = "Traga a resposta de volta",
            description = "Salve o arquivo .estudo que a sua IA gerar e escolha aqui. Você vê um resumo antes de gravar.",
            last = true,
        ) {
            OutlinedButton(onClick = onImport, modifier = Modifier.fillMaxWidth().height(46.dp), shape = RoundedCornerShape(14.dp)) {
                Icon(Icons.Outlined.UploadFile, null, Modifier.size(18.dp)); Spacer(Modifier.width(6.dp)); Text("Importar resposta")
            }
        }
    }
}

@Composable
private fun PlanPromptActionCard(prompt: String, onImport: () -> Unit) {
    val context = LocalContext.current
    SetupCard {
        Text("O pedido leva somente as matérias e a disponibilidade que já estão no seu aparelho.", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        GuideStep(number = 1, title = "Copie o pedido e abra sua IA", description = "Cole na conversa com a IA que você preferir.") {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = {
                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        clipboard.setPrimaryClip(ClipData.newPlainText("Pedido de plano do Estudário", prompt))
                    },
                    modifier = Modifier.weight(1f).height(46.dp),
                    shape = RoundedCornerShape(14.dp),
                ) { Icon(Icons.Outlined.ContentCopy, null, Modifier.size(18.dp)); Spacer(Modifier.width(6.dp)); Text("Copiar pedido", maxLines = 1) }
                OutlinedButton(
                    onClick = { context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://chatgpt.com/"))) },
                    modifier = Modifier.weight(1f).height(46.dp),
                    shape = RoundedCornerShape(14.dp),
                ) { Icon(Icons.Outlined.OpenInNew, null, Modifier.size(18.dp)); Spacer(Modifier.width(6.dp)); Text("Abrir minha IA") }
            }
        }
        GuideStep(number = 2, title = "Importe o .plano", description = "O Estudário só aplica o plano depois de validar referências e preservar o que já existe.", last = true) {
            OutlinedButton(onClick = onImport, modifier = Modifier.fillMaxWidth().height(46.dp), shape = RoundedCornerShape(14.dp)) {
                Icon(Icons.Outlined.UploadFile, null, Modifier.size(18.dp)); Spacer(Modifier.width(6.dp)); Text("Importar .plano")
            }
        }
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
