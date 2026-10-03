package br.com.estudario.ui.screens

import br.com.estudario.ui.components.AlertDialog
import br.com.estudario.ui.theme.estudarioLayout
import br.com.estudario.ui.theme.screenPadding
import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import br.com.estudario.data.local.SummaryEntity
import br.com.estudario.data.local.SummaryKind
import br.com.estudario.data.local.SnippetKind
import br.com.estudario.data.local.ErrorStatus
import br.com.estudario.data.planner.CompleteTaskInput
import br.com.estudario.ui.prompt.AdditionalQuestionPromptBuilderDialog
import br.com.estudario.ui.prompt.ContentPromptBuilderDialog
import br.com.estudario.domain.MasteryCalculator
import br.com.estudario.domain.MasteryInput
import br.com.estudario.domain.ReviewPolicy
import br.com.estudario.domain.ComputedReviewStatus
import br.com.estudario.domain.TopicCompletionPolicy
import br.com.estudario.domain.TopicCompletionWarning
import br.com.estudario.ui.AppViewModel
import br.com.estudario.ui.components.*
import br.com.estudario.ui.planner.PlannerTaskUi
import br.com.estudario.ui.planner.StudyPlanViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun TopicDetailScreen(viewModel: AppViewModel, topicId: Long, taskId: String? = null, planViewModel: StudyPlanViewModel, onBack: () -> Unit, onQuiz: () -> Unit, onTheory: (Long) -> Unit, onFocus: () -> Unit, onOpenTopic: (Long) -> Unit = {}, onTheoryAt: (Long, Int) -> Unit = { id, _ -> onTheory(id) }) {
    val topics by viewModel.topics.collectAsState()
    val subjects by viewModel.subjects.collectAsState()
    val summaries by viewModel.summaries.collectAsState()
    val snippets by viewModel.snippets.collectAsState()
    val theories by viewModel.theories.collectAsState()
    val theoryMarks by viewModel.theoryMarks.collectAsState()
    val userNotes by viewModel.notes.collectAsState()
    val questions by viewModel.questions.collectAsState()
    val reviews by viewModel.reviews.collectAsState()
    val attempts by viewModel.attempts.collectAsState()
    val errors by viewModel.errors.collectAsState()
    val errorConcepts by viewModel.errorConcepts.collectAsState()
    val studySessions by viewModel.studySessions.collectAsState()
    val reviewHistory by viewModel.reviewHistory.collectAsState()
    val queue by viewModel.queue.collectAsState()
    val focusSession by viewModel.focusSession.collectAsState()
    val focusSessions by viewModel.focusSessions.collectAsState()
    val topic = topics.firstOrNull { it.id == topicId }
    if (topic == null) { EmptyState("Tópico não encontrado", "Ele pode ter sido excluído.", "Voltar", onBack); return }
    val subject = subjects.firstOrNull { it.id == topic.subjectId }
    val parent = topics.firstOrNull { it.id == topic.parentTopicId }
    val childTopics = topics.filter { it.parentTopicId == topicId }.sortedBy { it.position }
    val topicQuestions = questions.filter { it.question.topicId == topicId }
    val answered = topicQuestions.sumOf { it.question.answerCount }
    val correct = topicQuestions.sumOf { it.question.correctCount }
    val completedReviews = reviews.count { it.topicId == topicId && it.completedAt != null }
    val questionIds = topicQuestions.map { it.question.id }.toSet()
    val recentAttempts = attempts.filter { it.questionId in questionIds }.sortedByDescending { it.answeredAt }.take(20)
    val topicErrors = errors.filter { it.entry.questionId in questionIds }
    val now = System.currentTimeMillis()
    val lastContact = listOfNotNull(topic.lastStudiedAt, topic.lastReviewedAt, recentAttempts.maxOfOrNull { it.answeredAt }).maxOrNull() ?: now
    val mastery = MasteryCalculator.percent(MasteryInput(
        topic.status, answered, correct, recentAttempts.count { !it.correct }, completedReviews,
        recentAttempts.size, recentAttempts.count { it.correct }, topicErrors.count { it.entry.status == ErrorStatus.RECORRENTE },
        reviews.count { it.topicId == topicId && ReviewPolicy.status(it, now) == ComputedReviewStatus.ATRASADA }, ((now - lastContact) / 86_400_000L).toInt(),
    ))
    var editor by remember { mutableStateOf<SummaryEntity?>(null) }
    var creating by remember { mutableStateOf(false) }
    var expandedSummary by remember { mutableStateOf<SummaryEntity?>(null) }
    val studyStartedAt = remember(topicId) { System.currentTimeMillis() }
    var finishStudy by remember { mutableStateOf(false) }
    var desmarcar by remember { mutableStateOf(false) }
    val sources by viewModel.sources.collectAsState()
    var completedNow by remember { mutableStateOf(false) }
    var studyNotes by remember { mutableStateOf("") }
    var completionBusy by remember { mutableStateOf(false) }
    var completionError by remember { mutableStateOf<String?>(null) }
    var completionWarning by remember { mutableStateOf<TopicCompletionWarning?>(null) }
    var showFocusChoice by remember { mutableStateOf(false) }
    var pendingCompletionTask by remember { mutableStateOf<PlannerTaskUi?>(null) }
    var pendingCompletionStartedAt by remember { mutableLongStateOf(studyStartedAt) }
    var pendingCompletionMinutes by remember { mutableIntStateOf(0) }
    var pendingFocusIsActive by remember { mutableStateOf(false) }
    var selectedTab by remember { mutableIntStateOf(0) }
    val queueItem = queue.firstOrNull { it.item.topicId == topicId }
    val sessionAttempts = attempts.filter { it.questionId in questionIds && it.answeredAt >= studyStartedAt }
    var showContentPrompt by remember { mutableStateOf(false) }
    var editingNote by remember { mutableStateOf<br.com.estudario.data.local.UserNoteEntity?>(null) }
    var deckFor by remember { mutableStateOf<br.com.estudario.data.local.SummaryEntity?>(null) }
    deckFor?.let { deck ->
        br.com.estudario.ui.components.SummaryFlashcardDeck(viewModel, deck, topics.firstOrNull { it.id == deck.topicId }?.title ?: deck.title, onDismiss = { deckFor = null })
    }
    editingNote?.let { note ->
        NoteEditor(note, topics, onDismiss = { editingNote = null }, onSave = { viewModel.saveNote(it); editingNote = null }, onDelete = { viewModel.deleteNote(note); editingNote = null })
    }
    var showAdditionalQuestionPrompt by remember { mutableStateOf(false) }
    var showPriority by remember { mutableStateOf(false) }
    val competitions by viewModel.competitions.collectAsState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val importLauncher = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        uri?.let {
            viewModel.beginIncomingFile()
            scope.launch {
                val text = readContentFile(context, it)
                if (text.isNullOrBlank()) viewModel.reportIncomingFileError("Não foi possível ler o arquivo escolhido.") else viewModel.openIncomingText(text, subject?.competitionId)
            }
        }
    }

    suspend fun finishTopicCompletion(stopLinkedFocus: Boolean) {
        if (completionBusy || completedNow) return
        completionBusy = true
        completionWarning = null
        showFocusChoice = false
        completionError = null
        var topicSaved = false
        try {
            val stoppedMinutes = if (stopLinkedFocus && pendingFocusIsActive) viewModel.stopFocus() else null
            val completedAt = System.currentTimeMillis()
            val actualMinutes = maxOf(
                pendingCompletionMinutes,
                stoppedMinutes ?: if (pendingFocusIsActive && focusSession.active) focusSession.elapsedMinutes(completedAt) else 0,
            )
            pendingCompletionTask?.let { plannedTask ->
                planViewModel.completeFromTopic(
                    plannedTask.entity.id,
                    CompleteTaskInput(
                        startedAt = pendingCompletionStartedAt,
                        completedAt = completedAt,
                        actualMinutes = actualMinutes,
                    ),
                )
            }
            viewModel.completeStudyNow(topicId, pendingCompletionStartedAt, studyNotes)
            topicSaved = true
            completedNow = true
            finishStudy = false
            pendingCompletionTask?.let { plannedTask ->
                try {
                    planViewModel.replanAfterTopicCompletion(plannedTask.entity.planId)
                } catch (error: Exception) {
                    completionError = "O tópico foi concluído, mas não consegui atualizar o plano: ${error.message ?: "tente gerar o plano novamente"}"
                }
            }
        } catch (cancelled: CancellationException) {
            throw cancelled
        } catch (error: Exception) {
            completionError = if (topicSaved) {
                "O tópico foi concluído, mas uma parte do plano não foi atualizada: ${error.message ?: "tente novamente"}"
            } else {
                error.message ?: "Não foi possível registrar a conclusão. Tente novamente."
            }
        } finally {
            completionBusy = false
        }
    }

    fun requestTopicCompletion() {
        if (completionBusy || completedNow) return
        finishStudy = false
        completionBusy = true
        completionError = null
        scope.launch {
            try {
                val plannedTask = planViewModel.matchingPendingTask(topicId, taskId)
                val now = System.currentTimeMillis()
                val linkedActiveFocus = focusSession.active &&
                    (focusSession.topicId == topicId || (plannedTask != null && focusSession.taskId == plannedTask.entity.id))
                val recentFocus = focusSessions
                    .asSequence()
                    .filter { it.startedAt >= studyStartedAt && (it.topicId == topicId || (plannedTask != null && it.taskId == plannedTask.entity.id)) }
                    .maxByOrNull { it.completedAt }
                val actualMinutes = when {
                    linkedActiveFocus -> focusSession.elapsedMinutes(now)
                    recentFocus != null -> (recentFocus.durationSeconds / 60L).toInt()
                    else -> ((now - studyStartedAt) / 60_000L).coerceAtLeast(0L).toInt()
                }
                pendingCompletionTask = plannedTask
                pendingCompletionMinutes = actualMinutes
                pendingCompletionStartedAt = when {
                    linkedActiveFocus -> focusSession.startedAt
                    recentFocus != null -> recentFocus.startedAt
                    else -> studyStartedAt
                }
                pendingFocusIsActive = linkedActiveFocus
                val warning = TopicCompletionPolicy.warning(plannedTask?.entity?.plannedMinutes, actualMinutes)
                completionBusy = false
                if (warning != TopicCompletionWarning.NONE) {
                    completionWarning = warning
                } else if (linkedActiveFocus) {
                    showFocusChoice = true
                } else {
                    finishTopicCompletion(stopLinkedFocus = false)
                }
            } catch (cancelled: CancellationException) {
                throw cancelled
            } catch (error: Exception) {
                completionBusy = false
                completionError = error.message ?: "Não foi possível conferir o plano. Tente novamente."
            }
        }
    }

    if (showContentPrompt && subject != null) {
        ContentPromptBuilderDialog(viewModel, subject.id, setOf(topic.id), onDismiss = { showContentPrompt = false }, onPickFile = { importLauncher.launch(arrayOf("*/*")) })
    }
    if (showAdditionalQuestionPrompt && subject != null) {
        AdditionalQuestionPromptBuilderDialog(
            viewModel = viewModel,
            subjectId = subject.id,
            topicId = topic.id,
            onDismiss = { showAdditionalQuestionPrompt = false },
            onPickFile = { importLauncher.launch(arrayOf("*/*")) },
        )
    }
    if (creating) SummaryEditorDialog(null, onDismiss = { creating = false }) { title, markdown -> viewModel.addSummary(topicId, title, markdown) }
    if (showPriority) {
        PriorityEditorDialog(
            title = "Prioridade • ${topic.title}",
            state = topicPriorityState(topic, topics),
            onDismiss = { showPriority = false },
            onSetOverride = { level -> viewModel.setTopicPriorityOverride(topic.id, level); showPriority = false },
            onClearOverride = { viewModel.setTopicPriorityOverride(topic.id, null); showPriority = false },
        )
    }
    completionWarning?.let { warning ->
        val title: String
        val message: String
        when (warning) {
            TopicCompletionWarning.OUTSIDE_ACTIVE_PLAN -> {
                title = "Tópico fora do plano ativo"
                message = "Este tópico não está no seu plano de estudos atual. Quer marcá-lo como concluído mesmo assim? Você recebe o XP normal, e o próximo replanejamento considera este tópico estudado."
            }
            TopicCompletionWarning.UNDER_PLANNED_TIME -> {
                title = "Tempo abaixo do planejado"
                val actualLabel = if (pendingCompletionMinutes == 0) "menos de 1 min" else "$pendingCompletionMinutes min"
                message = "Você registrou $actualLabel e o plano previa ${pendingCompletionTask?.entity?.plannedMinutes ?: 0} min. O modo foco só mede o tempo; quer concluir mesmo assim?"
            }
            TopicCompletionWarning.NONE -> return@let
        }
        AlertDialog(
            onDismissRequest = { completionWarning = null },
            title = { Text(title) },
            text = { Text(message) },
            confirmButton = {
                TextButton(onClick = {
                    completionWarning = null
                    if (pendingFocusIsActive) showFocusChoice = true
                    else scope.launch { finishTopicCompletion(stopLinkedFocus = false) }
                }) { Text("Concluir mesmo") }
            },
            dismissButton = { TextButton(onClick = { completionWarning = null }) { Text("Cancelar") } },
        )
    }
    if (showFocusChoice) {
        AlertDialog(
            onDismissRequest = { showFocusChoice = false },
            title = { Text("Modo foco em andamento") },
            text = { Text("Quer encerrar e salvar esta sessão no histórico junto com a conclusão do tópico?") },
            confirmButton = {
                TextButton(onClick = { scope.launch { finishTopicCompletion(stopLinkedFocus = true) } }) {
                    Text("Salvar foco e concluir")
                }
            },
            dismissButton = {
                TextButton(onClick = { scope.launch { finishTopicCompletion(stopLinkedFocus = false) } }) {
                    Text("Manter foco e concluir")
                }
            },
        )
    }
    if (completionBusy) {
        AlertDialog(
            onDismissRequest = {},
            title = { Text("Salvando conclusão") },
            text = { Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) { CircularProgressIndicator(Modifier.size(24.dp)); Text("Atualizando o tópico e o plano…") } },
            confirmButton = {},
        )
    }
    completionError?.let { message ->
        AlertDialog(
            onDismissRequest = { completionError = null },
            title = { Text(if (completedNow) "Conclusão salva" else "Não foi possível concluir") },
            text = { Text(message) },
            confirmButton = { TextButton(onClick = { completionError = null }) { Text("Fechar") } },
        )
    }
    editor?.let { value -> SummaryEditorDialog(value, onDismiss = { editor = null }) { title, markdown -> viewModel.updateSummary(value.copy(title = title, markdown = markdown)) } }
    expandedSummary?.let { summary ->
        AlertDialog(
            onDismissRequest = { expandedSummary = null },
            title = { Text(summary.title) },
            text = { Box(Modifier.heightIn(max = 520.dp)) { LazyColumn { item { MarkdownText(summary.markdown) } } } },
            confirmButton = { TextButton(onClick = { expandedSummary = null }) { Text("Fechar") } },
        )
    }
    if (finishStudy) AlertDialog(
        onDismissRequest = { finishStudy = false },
        title = { Text("Estudo concluído hoje") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Matéria: ${subject?.name ?: ","}")
                Text("Tópico: ${topic.title}")
                Text("Questões: ${sessionAttempts.size} • Acertos: ${sessionAttempts.count { it.correct }} • Erros: ${sessionAttempts.count { !it.correct }}")
                Text("Tempo: ${((System.currentTimeMillis() - studyStartedAt) / 60_000).coerceAtLeast(1)} min")
                OutlinedTextField(studyNotes, { studyNotes = it }, label = { Text("Observações (opcional)") }, minLines = 2)
                Text("Confirmar conclusão?", fontWeight = FontWeight.Bold)
                br.com.estudario.ui.components.XpLine(
                    br.com.estudario.domain.ProgressEngine.previewTopicStudied(),
                    prefix = "Ao concluir você ganha",
                )
                Text("As revisões D+1, D+7 e D+30 também entram na agenda, e continuam depois disso, com intervalo maior. Cada uma vale mais XP.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        },
        confirmButton = {
            TextButton(
                enabled = !completedNow && !completionBusy,
                onClick = ::requestTopicCompletion,
            ) { Text(if (completedNow || completionBusy) "Concluindo…" else "Confirmar") }
        },
        dismissButton = { TextButton(onClick = { finishStudy = false }) { Text("Cancelar") } },
    )

    if (desmarcar) ConfirmDialog(
        "Desmarcar como estudado?",
        "O tópico volta para \"não estudado\" e as revisões ainda não feitas dele são canceladas. Sessões de estudo e revisões já concluídas continuam no seu histórico.",
        "Desmarcar",
        onDismiss = { desmarcar = false },
    ) { viewModel.unmarkStudied(topic); desmarcar = false }

    val topicSources = sources.filter { it.topicId == topicId }
    val uriHandler = androidx.compose.ui.platform.LocalUriHandler.current

    // Modo foco do tópico: o cronômetro roda no topo do app; sair daqui com ele ligado pergunta antes.
    val focusHere = focusSession.active && focusSession.topicId == topicId
    val focusClockHidden by viewModel.focusClockHidden.collectAsState()
    var focusNow by remember { mutableLongStateOf(System.currentTimeMillis()) }
    LaunchedEffect(focusHere) { while (focusHere) { focusNow = System.currentTimeMillis(); kotlinx.coroutines.delay(1_000) } }
    var focusStopAsk by remember { mutableStateOf(false) }
    var focusLeaveAsk by remember { mutableStateOf(false) }
    var focusSwitchAsk by remember { mutableStateOf(false) }
    androidx.activity.compose.BackHandler(enabled = focusHere && !completionBusy) { focusLeaveAsk = true }
    if (focusLeaveAsk) AlertDialog(
        onDismissRequest = { focusLeaveAsk = false },
        icon = { Icon(Icons.Outlined.Timer, null) },
        title = { Text("Parar o cronômetro?") },
        text = { Text("Você está há ${focusSession.elapsedMinutes(focusNow).coerceAtLeast(1)} min estudando ${topic.title}. Se continuar contando, o cronômetro segue no topo do app.") },
        confirmButton = { TextButton(onClick = { focusLeaveAsk = false; scope.launch { viewModel.stopFocus(); onBack() } }) { Text("Parar e salvar") } },
        dismissButton = { TextButton(onClick = { focusLeaveAsk = false; onBack() }) { Text("Continuar contando") } },
    )
    if (focusStopAsk) AlertDialog(
        onDismissRequest = { focusStopAsk = false },
        icon = { Icon(Icons.Outlined.Timer, null) },
        title = { Text("Encerrar o foco?") },
        text = { Text("${br.com.estudario.ui.focus.focusClock(focusSession, focusNow)} de estudo em ${topic.title} vão para o seu histórico. Para registrar o tópico como estudado, use Concluir estudo.") },
        confirmButton = { TextButton(onClick = { focusStopAsk = false; scope.launch { viewModel.stopFocus() } }) { Text("Encerrar e salvar") } },
        dismissButton = { TextButton(onClick = { focusStopAsk = false }) { Text("Continuar") } },
    )
    if (focusSwitchAsk) AlertDialog(
        onDismissRequest = { focusSwitchAsk = false },
        icon = { Icon(Icons.Outlined.Timer, null) },
        title = { Text("Já tem um foco rodando") },
        text = { Text("O cronômetro está contando ${focusSession.title.ifBlank { "outra sessão" }}. Encerrar e salvar aquela sessão e começar aqui?") },
        confirmButton = {
            TextButton(onClick = {
                focusSwitchAsk = false
                scope.launch { viewModel.stopFocus(); viewModel.startFocus(topic.title, topicId = topicId, taskId = taskId) }
            }) { Text("Começar aqui") }
        },
        dismissButton = { TextButton(onClick = { focusSwitchAsk = false }) { Text("Cancelar") } },
    )

    val topicTheories = theories.filter { it.topicId == topicId }.sortedByDescending { it.updatedAt }
    val topicSummaries = summaries.filter { it.topicId == topicId }
    val topicSnippets = snippets.filter { it.topicId == topicId }
    val recallSnippets = topicSnippets.filter { it.kind == SnippetKind.RECUPERACAO && !SavedFlashcards.isSavedCard(it) }
    val tipSnippets = topicSnippets.filter { it.kind != SnippetKind.RECUPERACAO }
    val topicConcepts = errorConcepts.filter { it.topicId == topicId }
        .sortedWith(compareByDescending<br.com.estudario.data.local.ErrorConceptEntity> { it.errorCount }.thenByDescending { it.lastErrorAt ?: 0 })
    val studied = completedNow || topic.status != br.com.estudario.data.local.TopicStatus.NAO_ESTUDADO
    // Retoma de verdade: a teoria que ficou pela metade, depois a que não foi aberta.
    val resumeTheory = topicTheories.firstOrNull { theory ->
        theory.lastReadBlock >= 0 && theory.lastReadBlock < studyBlocks(theory.markdown).lastIndex
    } ?: topicTheories.firstOrNull { it.lastReadBlock < 0 }
    val readPercent = topicTheories.firstOrNull()?.let { theory ->
        val blocks = studyBlocks(theory.markdown).size.coerceAtLeast(1)
        if (theory.lastReadBlock < 0) 0 else ((theory.lastReadBlock + 1) * 100 / blocks).coerceIn(0, 100)
    }
    val accuracy = if (answered == 0) null else correct * 100 / answered
    val priority = topicPriorityState(topic, topics).effectivePriority
    var menuOpen by remember { mutableStateOf(false) }
    val tabs = listOf(
        "Teoria" to topicTheories.size,
        "Revisão" to (topicSummaries.size + recallSnippets.size),
        "Dicas" to tipSnippets.size,
        "Questões" to topicQuestions.size,
        "Erros" to topicConcepts.size,
        "Histórico" to 0,
    )

    LazyColumn(Modifier.fillMaxSize(), contentPadding = screenPadding(), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        // ---------------------------------------------------------------- cabeçalho
        item(key = "header") {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(verticalAlignment = Alignment.Top) {
                    Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            listOfNotNull(subject?.name, parent?.title).joinToString("  ›  ").uppercase(),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            maxLines = 2,
                            overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
                        )
                        ExpandableText(topic.title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, collapsedLines = 4)
                    }
                    Box {
                        IconButton(onClick = { menuOpen = true }) { Icon(Icons.Outlined.MoreVert, "Mais ações") }
                        DropdownMenu(menuOpen, onDismissRequest = { menuOpen = false }) {
                            if (childTopics.isEmpty()) DropdownMenuItem(text = { Text("Gerar material com o Estudário") }, leadingIcon = { br.com.estudario.ui.assistant.Folha(28.dp) }, onClick = { menuOpen = false; showContentPrompt = true })
                            DropdownMenuItem(text = { Text("Prioridade deste tópico") }, leadingIcon = { Icon(Icons.Outlined.Flag, null) }, onClick = { menuOpen = false; showPriority = true })
                            DropdownMenuItem(
                                text = { Text(if (queueItem == null) "Adicionar à fila" else "Já está na fila") },
                                leadingIcon = { Icon(Icons.Outlined.PlaylistAdd, null) },
                                enabled = queueItem == null,
                                onClick = { menuOpen = false; viewModel.enqueue(topic.id) },
                            )
                            DropdownMenuItem(text = { Text("Importar arquivo .estudo") }, leadingIcon = { Icon(Icons.Outlined.FileOpen, null) }, onClick = { menuOpen = false; importLauncher.launch(arrayOf("*/*")) })
                            if (studied) DropdownMenuItem(text = { Text("Desmarcar como estudado") }, leadingIcon = { Icon(Icons.Outlined.Undo, null) }, onClick = { menuOpen = false; desmarcar = true })
                        }
                    }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                    TopicChip(
                        if (studied) "Estudado" else "Não estudado",
                        if (studied) Icons.Outlined.CheckCircle else Icons.Outlined.RadioButtonUnchecked,
                        if (studied) estudarioColorsCompleted() else MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    TopicChip("Prioridade ${PriorityPresentation.label(priority).lowercase()}", Icons.Outlined.Flag, MaterialTheme.colorScheme.primary, onClick = { showPriority = true })
                    if (taskId != null) TopicChip("No plano de hoje", Icons.Outlined.Event, MaterialTheme.colorScheme.tertiary)
                }
            }
        }

        // ---------------------------------------------------------------- progresso e ação principal
        item(key = "progress") {
            Surface(shape = RoundedCornerShape(20.dp), color = MaterialTheme.colorScheme.surfaceContainerHigh, modifier = Modifier.fillMaxWidth()) {
                Row(Modifier.padding(vertical = 14.dp), verticalAlignment = Alignment.CenterVertically) {
                    TopicStat("$mastery%", "Domínio · ${MasteryCalculator.label(mastery).lowercase()}", null, Modifier.weight(1f))
                    StatDivider()
                    TopicStat(readPercent?.let { "$it%" } ?: "-", "Leitura", null, Modifier.weight(1f))
                    StatDivider()
                    TopicStat(accuracy?.let { "$it%" } ?: "-", if (answered == 0) "Acerto" else "Acerto · $answered", null, Modifier.weight(1f))
                }
            }
        }
        item(key = "actions") {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                val (label, icon, action) = when {
                    childTopics.isNotEmpty() -> Triple("Ver os ${childTopics.size} subtópicos", Icons.Outlined.AccountTree, { selectedTab = 0 })
                    resumeTheory != null && resumeTheory.lastReadBlock >= 0 -> Triple("Continuar leitura · ${readPercent ?: 0}%", Icons.Outlined.MenuBook, { onTheory(resumeTheory.id) })
                    resumeTheory != null -> Triple("Começar a teoria", Icons.Outlined.MenuBook, { onTheory(resumeTheory.id) })
                    topicQuestions.isNotEmpty() -> Triple("Treinar ${topicQuestions.size} ${if (topicQuestions.size == 1) "questão" else "questões"}", Icons.Outlined.Quiz, onQuiz)
                    else -> Triple("Gerar material com o Estudário", Icons.Outlined.AutoAwesome, { showContentPrompt = true })
                }
                Button(onClick = action, modifier = Modifier.fillMaxWidth().height(54.dp), shape = RoundedCornerShape(16.dp)) {
                    Icon(icon, null); Spacer(Modifier.width(10.dp)); Text(label, style = MaterialTheme.typography.titleSmall)
                }
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    if (focusHere) Button(
                        onClick = { focusStopAsk = true },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(14.dp),
                    ) {
                        Icon(if (focusSession.paused) Icons.Outlined.Pause else Icons.Outlined.Timer, null, Modifier.size(18.dp)); Spacer(Modifier.width(6.dp))
                        Text(when {
                            focusSession.paused -> "Foco pausado"
                            focusClockHidden -> "Foco ativo"
                            else -> "Foco · ${br.com.estudario.ui.focus.focusClock(focusSession, focusNow)}"
                        })
                    }
                    else FilledTonalButton(
                        onClick = {
                            if (focusSession.active) focusSwitchAsk = true
                            else { viewModel.startFocus(topic.title, topicId = topicId, taskId = taskId); onFocus() }
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(14.dp),
                    ) { Icon(Icons.Outlined.Timer, null, Modifier.size(18.dp)); Spacer(Modifier.width(6.dp)); Text("Modo foco") }
                    if (studied) OutlinedButton(onClick = {}, enabled = false, modifier = Modifier.weight(1f), shape = RoundedCornerShape(14.dp)) {
                        Icon(Icons.Outlined.Check, null, Modifier.size(18.dp)); Spacer(Modifier.width(6.dp)); Text("Concluído")
                    } else OutlinedButton(onClick = { finishStudy = true }, modifier = Modifier.weight(1f), shape = RoundedCornerShape(14.dp)) {
                        Icon(Icons.Outlined.Check, null, Modifier.size(18.dp)); Spacer(Modifier.width(6.dp)); Text("Concluir estudo")
                    }
                }
                if (completedNow) Text("Estudo registrado. As revisões D+1, D+7 e D+30 já estão na agenda.", style = MaterialTheme.typography.bodySmall, color = estudarioColorsCompleted())
            }
        }

        // ---------------------------------------------------------------- abas (grudam no topo)
        stickyHeader(key = "tabs") {
            Surface(color = MaterialTheme.colorScheme.background, modifier = Modifier.fillMaxWidth()) {
                ScrollableTabRow(selectedTabIndex = selectedTab, edgePadding = 0.dp, containerColor = MaterialTheme.colorScheme.background, divider = { HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant) }) {
                    tabs.forEachIndexed { index, (label, count) ->
                        Tab(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            text = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(label, fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Medium)
                                    if (count > 0) {
                                        Spacer(Modifier.width(6.dp))
                                        Surface(shape = RoundedCornerShape(50), color = if (selectedTab == index) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceContainerHighest) {
                                            Text("$count", Modifier.padding(horizontal = 7.dp, vertical = 1.dp), style = MaterialTheme.typography.labelSmall, color = if (selectedTab == index) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant)
                                        }
                                    }
                                }
                            },
                        )
                    }
                }
            }
        }

        when (selectedTab) {
            // ------------------------------------------------------------ TEORIA
            0 -> {
                if (childTopics.isNotEmpty()) item(key = "children") {
                    SectionCard("Dividido em ${childTopics.size} subtópicos", "No edital este item junta várias matérias. O material é gerado em cada subtópico, para sair completo.") {
                        childTopics.forEach { child ->
                            Surface(onClick = { onOpenTopic(child.id) }, shape = RoundedCornerShape(12.dp), color = MaterialTheme.colorScheme.surface, modifier = Modifier.fillMaxWidth()) {
                                Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                                    Icon(if (child.status != br.com.estudario.data.local.TopicStatus.NAO_ESTUDADO) Icons.Outlined.CheckCircle else Icons.Outlined.SubdirectoryArrowRight, null, tint = MaterialTheme.colorScheme.primary)
                                    Spacer(Modifier.width(10.dp))
                                    Text(child.title, Modifier.weight(1f), maxLines = 2, overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis)
                                    Icon(Icons.Outlined.ChevronRight, null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }
                        }
                    }
                } else if (topicTheories.isEmpty()) item(key = "no-theory") {
                    EmptyState("Ainda não tem teoria aqui", "Escolha o que quer receber (teoria, resumo, flashcards, questões) e o Estudário gera para este tópico.", "Gerar com o Estudário") { showContentPrompt = true }
                }
                topicTheories.forEach { theory ->
                    item(key = "theory-${theory.id}") {
                        val blocks = remember(theory.markdown) { studyBlocks(theory.markdown) }
                        val chapters = remember(blocks) {
                            blocks.withIndex().filter { it.value.trimStart().startsWith("## ") || it.value.trimStart().startsWith("# ") && it.index > 0 }
                                .map { it.index to it.value.trimStart().trimStart('#').trim().lineSequence().first() }
                        }
                        val percent = if (theory.lastReadBlock < 0) 0 else ((theory.lastReadBlock + 1) * 100 / blocks.size.coerceAtLeast(1)).coerceIn(0, 100)
                        val marks = theoryMarks.count { it.theoryId == theory.id }
                        Surface(onClick = { onTheory(theory.id) }, shape = RoundedCornerShape(20.dp), color = MaterialTheme.colorScheme.surfaceContainerLow, border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant), modifier = Modifier.fillMaxWidth()) {
                            Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Surface(shape = RoundedCornerShape(12.dp), color = MaterialTheme.colorScheme.primaryContainer, modifier = Modifier.size(44.dp)) {
                                        Box(contentAlignment = Alignment.Center) { Icon(Icons.Outlined.MenuBook, null, tint = MaterialTheme.colorScheme.primary) }
                                    }
                                    Spacer(Modifier.width(12.dp))
                                    Column(Modifier.weight(1f)) {
                                        Text(theory.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, maxLines = 2, overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis)
                                        Text(
                                            listOfNotNull("${chapters.size.coerceAtLeast(1)} capítulo(s)", "$percent% lido", if (marks > 0) "$marks grifo(s)" else null).joinToString(" · "),
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        )
                                    }
                                }
                                LinearProgressIndicator({ percent / 100f }, Modifier.fillMaxWidth(), strokeCap = androidx.compose.ui.graphics.StrokeCap.Round)
                                if (chapters.size > 1) Column {
                                    chapters.forEachIndexed { position, (blockIndex, title) ->
                                        val done = theory.lastReadBlock >= (chapters.getOrNull(position + 1)?.first ?: blocks.size) - 1
                                        val current = !done && theory.lastReadBlock >= blockIndex - 1 && theory.lastReadBlock >= 0
                                        Row(
                                            Modifier.fillMaxWidth().clip(RoundedCornerShape(10.dp)).clickable { onTheoryAt(theory.id, blockIndex) }.padding(vertical = 9.dp, horizontal = 4.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                        ) {
                                            Icon(
                                                when { done -> Icons.Outlined.CheckCircle; current -> Icons.Outlined.PlayCircle; else -> Icons.Outlined.RadioButtonUnchecked },
                                                null,
                                                Modifier.size(20.dp),
                                                tint = when { done -> estudarioColorsCompleted(); current -> MaterialTheme.colorScheme.primary; else -> MaterialTheme.colorScheme.outline },
                                            )
                                            Spacer(Modifier.width(12.dp))
                                            Text(title, Modifier.weight(1f), style = MaterialTheme.typography.bodyMedium, fontWeight = if (current) FontWeight.SemiBold else FontWeight.Normal, maxLines = 2, overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                if (!topic.scopeCovers.isNullOrBlank() || !topic.scopeExcludes.isNullOrBlank() || topicSources.isNotEmpty()) item(key = "about") {
                    // Recorte e fontes: o que a IA declarou cobrir e de onde tirou. Fica junto da
                    // teoria, para conferir no momento em que a dúvida aparece.
                    var open by remember { mutableStateOf(false) }
                    SectionCard(
                        "Sobre este conteúdo",
                        "O que este item do edital cobre e de onde veio o material",
                        trailing = { Icon(if (open) Icons.Outlined.ExpandLess else Icons.Outlined.ExpandMore, null) },
                        onHeaderClick = { open = !open },
                    ) {
                        if (open) {
                            topic.scopeCovers?.takeIf { it.isNotBlank() }?.let { LabeledBlock("COBRE", it, MaterialTheme.colorScheme.secondary) }
                            topic.scopeExcludes?.takeIf { it.isNotBlank() }?.let { LabeledBlock("FICA DE FORA", it, MaterialTheme.colorScheme.error) }
                            if (topicSources.isNotEmpty()) {
                                Text("FONTES", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.primary)
                                topicSources.forEach { fonte ->
                                    SourceCard(fonte = fonte, topicTitle = null, onOpenTopic = {}, onOpenUrl = { url -> runCatching { uriHandler.openUri(url) } }, showTopic = false)
                                }
                            }
                        }
                    }
                }
                val topicNotes = userNotes.filter { it.topicId == topicId }
                item(key = "notes-header") {
                    SectionHeader("Minhas anotações", "Também ficam no Caderno de estudo") {
                        TextButton(onClick = { editingNote = br.com.estudario.data.local.UserNoteEntity(topicId = topicId, text = "") }) {
                            Icon(Icons.Outlined.Add, null, Modifier.size(18.dp)); Spacer(Modifier.width(4.dp)); Text("Nova")
                        }
                    }
                }
                if (topicNotes.isEmpty()) item(key = "notes-empty") {
                    Text("Escreva com suas palavras o que precisa lembrar deste tópico.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                topicNotes.forEach { note ->
                    item(key = "note-${note.id}") {
                        Surface(onClick = { editingNote = note }, shape = RoundedCornerShape(14.dp), color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = .5f), modifier = Modifier.fillMaxWidth()) {
                            Row(Modifier.padding(14.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                Icon(Icons.Outlined.EditNote, null, tint = MaterialTheme.colorScheme.secondary)
                                Text(note.text, Modifier.weight(1f), maxLines = 6, overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis)
                            }
                        }
                    }
                }
            }

            // ------------------------------------------------------------ REVISÃO
            1 -> {
                val decks = topicSummaries.filter { it.kind == SummaryKind.RAPIDO }
                val fullSummaries = topicSummaries.filter { it.kind != SummaryKind.RAPIDO }
                if (topicSummaries.isEmpty() && recallSnippets.isEmpty()) item(key = "review-empty") {
                    EmptyState("Nada para revisar ainda", "Gere resumo, flashcards e perguntas de memorização para revisar este tópico em minutos.", "Gerar com o Estudário") { showContentPrompt = true }
                }
                decks.forEach { deck ->
                    item(key = "deck-${deck.id}") {
                        val size = remember(deck.markdown) { FlashcardParser.parse(deck.markdown).size }
                        Surface(onClick = { deckFor = deck }, shape = RoundedCornerShape(20.dp), color = MaterialTheme.colorScheme.primary, modifier = Modifier.fillMaxWidth()) {
                            Row(Modifier.padding(18.dp), verticalAlignment = Alignment.CenterVertically) {
                                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Text("FLASHCARDS", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.onPrimary.copy(alpha = .8f))
                                    Text("$size cartões para praticar", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimary)
                                    Text(
                                        if (decks.size > 1 && !deck.title.equals("Revisão rápida", ignoreCase = true)) deck.title else "Tente lembrar antes de virar. Salve só os que interessam.",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = .85f),
                                        maxLines = 2,
                                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
                                    )
                                }
                                IconButton(onClick = { viewModel.updateSummary(deck.copy(isFavorite = !deck.isFavorite)) }) {
                                    Icon(if (deck.isFavorite) Icons.Outlined.Bookmark else Icons.Outlined.BookmarkBorder, if (deck.isFavorite) "Tirar baralho do Caderno" else "Salvar baralho no Caderno", tint = MaterialTheme.colorScheme.onPrimary)
                                }
                                Icon(Icons.Outlined.PlayCircle, "Praticar", Modifier.size(40.dp), tint = MaterialTheme.colorScheme.onPrimary)
                            }
                        }
                    }
                }
                fullSummaries.forEach { summary ->
                    item(key = "summary-${summary.id}") {
                        Surface(onClick = { expandedSummary = summary }, shape = RoundedCornerShape(18.dp), color = MaterialTheme.colorScheme.surfaceContainerLow, border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant), modifier = Modifier.fillMaxWidth()) {
                            Column(Modifier.padding(start = 16.dp, top = 12.dp, bottom = 14.dp, end = 4.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Outlined.Article, null, tint = MaterialTheme.colorScheme.primary)
                                    Spacer(Modifier.width(10.dp))
                                    Column(Modifier.weight(1f)) {
                                        Text("RESUMO", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.primary)
                                        Text(summary.title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold, maxLines = 2)
                                    }
                                    IconButton(onClick = { editor = summary }) { Icon(Icons.Outlined.Edit, "Editar", tint = MaterialTheme.colorScheme.onSurfaceVariant) }
                                    IconButton(onClick = { viewModel.deleteSummary(summary) }) { Icon(Icons.Outlined.DeleteOutline, "Excluir", tint = MaterialTheme.colorScheme.onSurfaceVariant) }
                                }
                                Text(plainPreview(summary.markdown).take(220), maxLines = 3, overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(end = 12.dp))
                            }
                        }
                    }
                }
                if (recallSnippets.isNotEmpty()) {
                    item(key = "recall-header") { SectionHeader("Memorização ativa", "Responda de cabeça, depois confira. ★ guarda no Caderno.") }
                    recallSnippets.forEach { snippet ->
                        item(key = "recall-${snippet.id}") {
                            RecallCard(snippet.text, snippet.answer, revealKey = snippet.id) {
                                IconButton(onClick = { viewModel.saveSnippet(snippet.copy(isFavorite = !snippet.isFavorite)) }) {
                                    Icon(if (snippet.isFavorite) Icons.Outlined.Star else Icons.Outlined.StarBorder, if (snippet.isFavorite) "Tirar do Caderno" else "Guardar no Caderno", tint = if (snippet.isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }
                        }
                    }
                }
                item(key = "summary-new") {
                    TextButton(onClick = { creating = true }) { Icon(Icons.Outlined.Add, null, Modifier.size(18.dp)); Spacer(Modifier.width(4.dp)); Text("Escrever meu próprio resumo") }
                }
            }

            // ------------------------------------------------------------ DICAS
            2 -> {
                if (tipSnippets.isEmpty()) item(key = "tips-empty") {
                    EmptyState("Sem dicas ainda", "Dicas e pegadinhas de banca vêm junto com o material gerado para este tópico.", "Gerar com o Estudário") { showContentPrompt = true }
                } else item(key = "tips-header") { SectionHeader("Dicas e pegadinhas", "O que costuma decidir a questão. ★ guarda no Caderno.") }
                tipSnippets.sortedBy { it.kind }.forEach { snippet ->
                    item(key = "tip-${snippet.id}") {
                        val trap = snippet.kind == SnippetKind.PEGADINHA
                        val accent = if (trap) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.tertiary
                        Surface(shape = RoundedCornerShape(16.dp), color = (if (trap) MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.tertiaryContainer).copy(alpha = .55f), modifier = Modifier.fillMaxWidth()) {
                            Row(Modifier.height(IntrinsicSize.Min)) {
                                Box(Modifier.width(4.dp).fillMaxHeight().background(accent))
                                Column(Modifier.weight(1f).padding(start = 14.dp, top = 12.dp, bottom = 12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(if (trap) Icons.Outlined.Warning else Icons.Outlined.Lightbulb, null, Modifier.size(16.dp), tint = accent)
                                        Spacer(Modifier.width(6.dp))
                                        Text(if (trap) "PEGADINHA" else "DICA", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Black, color = accent)
                                    }
                                    br.com.estudario.ui.components.StudyInlineText(snippet.text, style = MaterialTheme.typography.bodyMedium)
                                }
                                IconButton(onClick = { viewModel.saveSnippet(snippet.copy(isFavorite = !snippet.isFavorite)) }) {
                                    Icon(if (snippet.isFavorite) Icons.Outlined.Star else Icons.Outlined.StarBorder, if (snippet.isFavorite) "Tirar do Caderno" else "Guardar no Caderno", tint = if (snippet.isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }
                        }
                    }
                }
            }

            // ------------------------------------------------------------ QUESTÕES
            3 -> {
                item(key = "questions-summary") {
                    Surface(shape = RoundedCornerShape(20.dp), color = MaterialTheme.colorScheme.surfaceContainerHigh, modifier = Modifier.fillMaxWidth()) {
                        Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                            Row {
                                TopicStat("${topicQuestions.size}", "questões", null, Modifier.weight(1f))
                                TopicStat("${topicQuestions.count { it.question.answerCount == 0 }}", "nunca vistas", null, Modifier.weight(1f))
                                TopicStat(accuracy?.let { "$it%" } ?: "-", "de acerto", null, Modifier.weight(1f))
                            }
                            Button(onClick = onQuiz, enabled = topicQuestions.isNotEmpty(), modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp)) {
                                Icon(Icons.Outlined.PlayArrow, null); Spacer(Modifier.width(6.dp)); Text("Treinar este tópico")
                            }
                        }
                    }
                }
                if (subject != null) item(key = "questions-more") {
                    val subjectTopicIds = topics.asSequence().filter { it.subjectId == subject.id }.map { it.id }.toSet()
                    val subjectQuestionCount = questions.count { it.question.topicId in subjectTopicIds }
                    SectionCard(
                        if (topicQuestions.isEmpty()) "Gere as primeiras questões" else "Quer mais questões?",
                        if (subjectQuestionCount == 0) "Questões inéditas no estilo da banca, com explicação de cada alternativa."
                        else "O Estudário compara com as $subjectQuestionCount questões que você já tem nesta matéria para não repetir.",
                    ) {
                        OutlinedButton(onClick = { showAdditionalQuestionPrompt = true }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp)) {
                            br.com.estudario.ui.assistant.Folha(26.dp); Spacer(Modifier.width(8.dp)); Text("Gerar com o Estudário")
                        }
                    }
                }
            }

            // ------------------------------------------------------------ ERROS
            4 -> {
                if (topicConcepts.isEmpty()) item(key = "errors-empty") {
                    EmptyState("Nenhum erro por aqui", "Quando você errar uma questão deste tópico, o conceito por trás do erro aparece aqui, para atacar o padrão e não só a questão.")
                } else item(key = "errors-header") { SectionHeader("Onde você erra", "Conceitos por trás dos seus erros, do mais frequente ao menos.") }
                topicConcepts.forEach { concept ->
                    item(key = "concept-${concept.id}") {
                        Surface(shape = RoundedCornerShape(16.dp), color = MaterialTheme.colorScheme.surfaceContainerLow, border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant), modifier = Modifier.fillMaxWidth()) {
                            Row(Modifier.padding(14.dp), verticalAlignment = Alignment.Top) {
                                Surface(shape = RoundedCornerShape(10.dp), color = if (concept.mastered) estudarioColorsCompleted().copy(alpha = .15f) else MaterialTheme.colorScheme.errorContainer) {
                                    Text("${concept.errorCount}×", Modifier.padding(horizontal = 10.dp, vertical = 6.dp), fontWeight = FontWeight.Bold, color = if (concept.mastered) estudarioColorsCompleted() else MaterialTheme.colorScheme.error)
                                }
                                Spacer(Modifier.width(12.dp))
                                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Text(concept.title, fontWeight = FontWeight.SemiBold)
                                    concept.summary.takeIf { it.isNotBlank() }?.let { Text(it, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant) }
                                    Text(if (concept.mastered) "Corrigido" else "Prioridade ${concept.priority.name.lowercase()}", style = MaterialTheme.typography.labelSmall, color = if (concept.mastered) estudarioColorsCompleted() else MaterialTheme.colorScheme.primary)
                                }
                            }
                        }
                    }
                }
            }

            // ------------------------------------------------------------ HISTÓRICO
            else -> {
                val history = buildList<Triple<Long, String, androidx.compose.ui.graphics.vector.ImageVector>> {
                    studySessions.filter { it.topicId == topicId }.forEach { add(Triple(it.completedAt, "Estudo concluído", Icons.Outlined.School)) }
                    reviewHistory.filter { it.topicId == topicId }.forEach { add(Triple(it.reviewedAt, "Revisão feita", Icons.Outlined.Replay)) }
                    recentAttempts.forEach { add(Triple(it.answeredAt, if (it.correct) "Acertou uma questão" else "Errou uma questão", if (it.correct) Icons.Outlined.CheckCircle else Icons.Outlined.Cancel)) }
                }.sortedByDescending { it.first }.take(30)
                if (history.isEmpty()) item(key = "history-empty") { EmptyState("Sem histórico ainda", "Estudos, revisões e questões deste tópico aparecem aqui, em ordem.") }
                history.forEachIndexed { index, (time, label, icon) ->
                    item(key = "history-$index-$time") {
                        val formatter = remember { java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy · HH:mm") }
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 2.dp)) {
                            Surface(shape = RoundedCornerShape(50), color = MaterialTheme.colorScheme.surfaceContainerHigh, modifier = Modifier.size(36.dp)) {
                                Box(contentAlignment = Alignment.Center) { Icon(icon, null, Modifier.size(18.dp), tint = MaterialTheme.colorScheme.primary) }
                            }
                            Spacer(Modifier.width(12.dp))
                            Column {
                                Text(label, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
                                Text(java.time.Instant.ofEpochMilli(time).atZone(java.time.ZoneId.systemDefault()).format(formatter), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                }
            }
        }
        item(key = "bottom-space") { Spacer(Modifier.height(24.dp)) }
    }
}

@Composable
private fun estudarioColorsCompleted(): androidx.compose.ui.graphics.Color = br.com.estudario.ui.theme.estudarioColors().completed

@Composable
private fun TopicChip(label: String, icon: androidx.compose.ui.graphics.vector.ImageVector, tint: androidx.compose.ui.graphics.Color, onClick: (() -> Unit)? = null) {
    val content: @Composable () -> Unit = {
        Row(Modifier.padding(horizontal = 10.dp, vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, null, Modifier.size(14.dp), tint = tint)
            Spacer(Modifier.width(5.dp))
            Text(label, style = MaterialTheme.typography.labelMedium, color = tint, fontWeight = FontWeight.SemiBold)
        }
    }
    if (onClick != null) Surface(onClick = onClick, shape = RoundedCornerShape(50), color = tint.copy(alpha = .1f)) { content() }
    else Surface(shape = RoundedCornerShape(50), color = tint.copy(alpha = .1f)) { content() }
}

@Composable
private fun TopicStat(value: String, label: String, progress: Float?, modifier: Modifier) {
    Column(modifier.padding(horizontal = 10.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(value, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1, overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis)
        if (progress != null) LinearProgressIndicator({ progress.coerceIn(0f, 1f) }, Modifier.fillMaxWidth().height(4.dp), strokeCap = androidx.compose.ui.graphics.StrokeCap.Round)
    }
}

@Composable
private fun StatDivider() {
    Box(Modifier.width(1.dp).height(44.dp).background(MaterialTheme.colorScheme.outlineVariant))
}

@Composable
private fun SectionHeader(title: String, subtitle: String? = null, action: @Composable () -> Unit = {}) {
    Row(Modifier.fillMaxWidth().padding(top = 6.dp), verticalAlignment = Alignment.CenterVertically) {
        Column(Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            subtitle?.let { Text(it, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant) }
        }
        action()
    }
}

@Composable
private fun SectionCard(title: String, subtitle: String?, trailing: @Composable () -> Unit = {}, onHeaderClick: (() -> Unit)? = null, content: @Composable ColumnScope.() -> Unit) {
    Surface(shape = RoundedCornerShape(18.dp), color = MaterialTheme.colorScheme.surfaceContainerLow, border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant), modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(
                Modifier.fillMaxWidth().then(if (onHeaderClick != null) Modifier.clickable(onClick = onHeaderClick) else Modifier),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(Modifier.weight(1f)) {
                    Text(title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                    subtitle?.let { Text(it, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant) }
                }
                trailing()
            }
            content()
        }
    }
}

@Composable
private fun LabeledBlock(label: String, text: String, color: androidx.compose.ui.graphics.Color) {
    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Text(label, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Black, color = color)
        Text(text, style = MaterialTheme.typography.bodySmall)
    }
}

private fun topicPriorityState(topic: br.com.estudario.data.local.TopicEntity, allTopics: List<br.com.estudario.data.local.TopicEntity>, visited: Set<Long> = emptySet()): PriorityEditorState {
    val parentPriority = topic.parentTopicId
        ?.takeUnless { it in visited }
        ?.let { parentId -> allTopics.firstOrNull { it.id == parentId } }
        ?.let { parent -> topicPriorityState(parent, allTopics, visited + topic.id).effectivePriority }
    return PriorityPresentation.state(
        topic.assessedPriorityScore,
        topic.assessedPrioritySource,
        topic.assessedPriorityConfidence,
        topic.assessedPriorityRationale,
        topic.assessedPriorityEvidenceJson,
        topic.hasAssessedPriority,
        topic.userPriorityOverride,
        parentPriority,
    )
}

private suspend fun readContentFile(context: Context, uri: Uri): String? = withContext(Dispatchers.IO) {
    runCatching { context.contentResolver.openInputStream(uri)?.use { it.readBytes().toString(Charsets.UTF_8) } }.getOrNull()
}

@Composable
private fun SummaryEditorDialog(summary: SummaryEntity?, onDismiss: () -> Unit, onSave: (String, String) -> Unit) {
    var title by remember { mutableStateOf(summary?.title.orEmpty()) }
    var markdown by remember { mutableStateOf(summary?.markdown.orEmpty()) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (summary == null) "Novo resumo" else "Editar resumo") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(title, { title = it }, label = { Text("Título") }, singleLine = true)
                OutlinedTextField(markdown, { markdown = it }, label = { Text("Markdown") }, minLines = 8, maxLines = 14)
            }
        },
        confirmButton = { TextButton(enabled = title.isNotBlank() && markdown.isNotBlank(), onClick = { onSave(title, markdown); onDismiss() }) { Text("Salvar") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } },
    )
}
