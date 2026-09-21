package br.com.estudario.ui.screens

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import br.com.estudario.data.local.*
import br.com.estudario.ui.prompt.ContentPromptBuilderDialog
import br.com.estudario.ui.prompt.EditalPromptBuilderDialog
import androidx.compose.foundation.lazy.rememberLazyListState
import br.com.estudario.ui.AppViewModel
import br.com.estudario.ui.components.EmptyState
import br.com.estudario.ui.components.ScreenTitle
import br.com.estudario.ui.components.TextInputDialog
import br.com.estudario.ui.components.ConfirmDialog
import br.com.estudario.ui.components.PriorityEditorDialog
import br.com.estudario.ui.components.PriorityEditorState
import br.com.estudario.ui.components.PriorityPresentation
import br.com.estudario.ui.tour.TourKey
import br.com.estudario.ui.tour.tourTarget
import br.com.estudario.domain.PriorityLevel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun EditalScreen(viewModel: AppViewModel, onTopic: (Long) -> Unit, onHelp: () -> Unit = {}) {
    val competitions by viewModel.competitions.collectAsState()
    val subjects by viewModel.subjects.collectAsState()
    val topics by viewModel.topics.collectAsState()
    val theories by viewModel.theories.collectAsState()
    val summaries by viewModel.summaries.collectAsState()
    val snippets by viewModel.snippets.collectAsState()
    val questions by viewModel.questions.collectAsState()
    val expandedSubjects by viewModel.expandedEditalSubjects.collectAsState()
    var selectedCompetitionId by remember(competitions) { mutableLongStateOf(competitions.firstOrNull { it.isPrimary }?.id ?: competitions.firstOrNull()?.id ?: 0) }
    var addCompetition by remember { mutableStateOf(false) }
    var addSubject by remember { mutableStateOf(false) }
    var addTopicFor by remember { mutableStateOf<SubjectEntity?>(null) }
    var deleteCompetition by remember { mutableStateOf<CompetitionEntity?>(null) }
    var query by rememberSaveable { mutableStateOf("") }
    var onlyWithContent by rememberSaveable { mutableStateOf(false) }
    var showEditalPrompt by remember { mutableStateOf(false) }
    var contentPromptFor by remember { mutableStateOf<Pair<Long, Set<Long>?>?>(null) }
    var priorityTarget by remember { mutableStateOf<PriorityTarget?>(null) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val importLauncher = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        uri?.let {
            viewModel.beginIncomingFile()
            scope.launch {
                val text = readEditalFile(context, it)
                if (text.isNullOrBlank()) viewModel.reportIncomingFileError("Não foi possível ler o arquivo escolhido.") else viewModel.openIncomingText(text)
            }
        }
    }
    val pickFile = { importLauncher.launch(arrayOf("*/*")) }
    val tourStep by viewModel.tourStep.collectAsState()
    val listState = rememberLazyListState()
    val selectedCompetition = competitions.firstOrNull { it.id == selectedCompetitionId }
    LaunchedEffect(tourStep?.key) {
        when (tourStep?.key) {
            TourKey.EDITAL_CREATE, TourKey.EDITAL_AI, TourKey.EDITAL_IMPORT -> listState.animateScrollToItem(0)
            TourKey.SUBJECT_AI -> listState.animateScrollToItem(4)
            else -> Unit
        }
    }

    if (showEditalPrompt) EditalPromptBuilderDialog(viewModel, selectedCompetitionId.takeIf { it != 0L }, onDismiss = { showEditalPrompt = false }, onPickFile = pickFile)
    contentPromptFor?.let { (subjectId, topicIds) -> ContentPromptBuilderDialog(viewModel, subjectId, topicIds, onDismiss = { contentPromptFor = null }, onPickFile = pickFile) }
    if (addCompetition) TextInputDialog("Novo concurso", label = "Nome do concurso", onDismiss = { addCompetition = false }) { viewModel.addCompetition(it) }
    if (addSubject && selectedCompetitionId != 0L) TextInputDialog("Nova matéria", label = "Nome da matéria", onDismiss = { addSubject = false }) { viewModel.addSubject(selectedCompetitionId, it) }
    addTopicFor?.let { subject -> TextInputDialog("Novo tópico", label = "Título do tópico", onDismiss = { addTopicFor = null }) { viewModel.addTopic(subject.id, it) } }
    deleteCompetition?.let { competition ->
        ConfirmDialog(
            title = "Excluir edital?",
            message = "“${competition.name}” e todas as matérias, tópicos e conteúdos relacionados serão removidos deste aparelho. Essa ação não pode ser desfeita.",
            confirmLabel = "Excluir edital",
            confirmDelayMillis = 3_000L,
            onDismiss = { deleteCompetition = null },
        ) { viewModel.deleteCompetition(competition) }
    }
    priorityTarget?.let { target ->
        PriorityEditorDialog(
            title = "Prioridade • ${target.title}",
            state = target.state,
            onDismiss = { priorityTarget = null },
            onSetOverride = { level ->
                when (target) {
                    is PriorityTarget.Competition -> viewModel.setCompetitionPriorityOverride(target.value.id, level)
                    is PriorityTarget.Subject -> viewModel.setSubjectPriorityOverride(target.value.id, level)
                    is PriorityTarget.Topic -> viewModel.setTopicPriorityOverride(target.value.id, level)
                }
                priorityTarget = null
            },
            onClearOverride = {
                when (target) {
                    is PriorityTarget.Competition -> viewModel.setCompetitionPriorityOverride(target.value.id, null)
                    is PriorityTarget.Subject -> viewModel.setSubjectPriorityOverride(target.value.id, null)
                    is PriorityTarget.Topic -> viewModel.setTopicPriorityOverride(target.value.id, null)
                }
                priorityTarget = null
            },
        )
    }

    LazyColumn(Modifier.fillMaxSize(), state = listState, contentPadding = PaddingValues(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        item {
            ScreenTitle("Edital", "Organize matérias e acompanhe seu domínio") {
                Row {
                    IconButton(onClick = onHelp) { Icon(Icons.Outlined.HelpOutline, "Como montar o edital") }
                    IconButton(
                        onClick = { showEditalPrompt = true },
                        modifier = Modifier.tourTarget(TourKey.EDITAL_AI, tourStep?.key) { viewModel.reportTourTargetBounds(TourKey.EDITAL_AI, it) },
                    ) { Icon(Icons.Outlined.AutoAwesome, "Montar edital com IA") }
                    IconButton(onClick = { selectedCompetition?.let { priorityTarget = PriorityTarget.Competition(it, it.priorityState()) } }, enabled = selectedCompetition != null) {
                        Icon(Icons.Outlined.Flag, "Definir prioridade do concurso")
                    }
                    IconButton(
                        onClick = pickFile,
                        modifier = Modifier.tourTarget(TourKey.EDITAL_IMPORT, tourStep?.key) { viewModel.reportTourTargetBounds(TourKey.EDITAL_IMPORT, it) },
                    ) { Icon(Icons.Outlined.FileOpen, "Importar arquivo .estudo") }
                    IconButton(
                        onClick = { addCompetition = true },
                        modifier = Modifier.tourTarget(TourKey.EDITAL_CREATE, tourStep?.key) { viewModel.reportTourTargetBounds(TourKey.EDITAL_CREATE, it) },
                    ) { Icon(Icons.Outlined.Add, "Criar concurso") }
                }
            }
        }
        if (competitions.isEmpty()) {
            item {
                ElevatedCard(colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
                    Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text("Comece pelo edital", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Text("Deixe a IA montar matérias e tópicos a partir do PDF do edital, ou crie tudo manualmente.")
                        Button(onClick = { showEditalPrompt = true }, Modifier.fillMaxWidth()) { Icon(Icons.Outlined.AutoAwesome, null); Spacer(Modifier.width(8.dp)); Text("Montar edital com IA") }
                        OutlinedButton(onClick = pickFile, Modifier.fillMaxWidth()) { Icon(Icons.Outlined.FileOpen, null); Spacer(Modifier.width(8.dp)); Text("Importar arquivo .estudo") }
                        TextButton(onClick = { addCompetition = true }, Modifier.fillMaxWidth()) { Text("Criar concurso manualmente") }
                    }
                }
            }
        } else {
            item {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(competitions, key = { it.id }) { competition ->
                        FilterChip(
                            selected = competition.id == selectedCompetitionId,
                            onClick = { selectedCompetitionId = competition.id },
                            label = { Text(competition.name) },
                            leadingIcon = if (competition.isPrimary) {{ Icon(Icons.Outlined.Star, null, Modifier.size(18.dp)) }} else null,
                        )
                    }
                }
            }
            item {
                FlowRow(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    selectedCompetition?.let { competition ->
                        AssistChip(
                            onClick = { priorityTarget = PriorityTarget.Competition(competition, competition.priorityState()) },
                            label = { Text("Prioridade: ${PriorityPresentation.label(competition.priorityState().effectivePriority)}") },
                            leadingIcon = { Icon(Icons.Outlined.Flag, null, Modifier.size(18.dp)) },
                        )
                    }
                    OutlinedButton(onClick = { viewModel.setPrimary(selectedCompetitionId) }) { Icon(Icons.Outlined.Star, null); Spacer(Modifier.width(6.dp)); Text("Tornar principal") }
                    Button(onClick = { addSubject = true }) { Icon(Icons.Outlined.Add, null); Spacer(Modifier.width(6.dp)); Text("Matéria") }
                    OutlinedButton(
                        onClick = { deleteCompetition = competitions.firstOrNull { it.id == selectedCompetitionId } },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
                    ) {
                        Icon(Icons.Outlined.Delete, null)
                        Spacer(Modifier.width(6.dp))
                        Text("Excluir edital")
                    }
                }
            }
            item {
                OutlinedTextField(
                    value = query,
                    onValueChange = { query = it },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    label = { Text("Pesquisar no edital") },
                    placeholder = { Text("Matéria ou tópico") },
                    leadingIcon = { Icon(Icons.Outlined.Search, null) },
                    trailingIcon = if (query.isNotEmpty()) {{ IconButton(onClick = { query = "" }) { Icon(Icons.Outlined.Close, "Limpar pesquisa") } }} else null,
                )
            }
            item {
                FilterChip(
                    selected = onlyWithContent,
                    onClick = { onlyWithContent = !onlyWithContent },
                    label = { Text("Somente tópicos com conteúdo") },
                    leadingIcon = { Icon(if (onlyWithContent) Icons.Outlined.Check else Icons.Outlined.LibraryBooks, null, Modifier.size(18.dp)) },
                )
            }
            val selectedSubjects = subjects.filter { it.competitionId == selectedCompetitionId }
            val contentTopicIds = buildSet {
                theories.forEach { add(it.topicId) }
                summaries.forEach { add(it.topicId) }
                snippets.forEach { add(it.topicId) }
                questions.forEach { add(it.question.topicId) }
            }
            val visibleSubjects = selectedSubjects.filter { subject ->
                val subjectTopics = topics.filter { it.subjectId == subject.id }
                val matchesSearch = query.isBlank() || subject.name.contains(query, ignoreCase = true) || subjectTopics.any { it.title.contains(query, ignoreCase = true) }
                val matchesContent = !onlyWithContent || subjectTopics.any { it.id in contentTopicIds }
                matchesSearch && matchesContent
            }
            if (selectedSubjects.isEmpty()) item { EmptyState("Edital vazio", "Adicione a primeira matéria deste concurso.", "Adicionar matéria") { addSubject = true } }
            else if (visibleSubjects.isEmpty()) item { EmptyState("Nenhuma matéria encontrada", "Altere a pesquisa ou desative o filtro de conteúdo.") }
            items(visibleSubjects, key = { it.id }) { subject ->
                val isFirst = subject.id == visibleSubjects.firstOrNull()?.id
                SubjectCard(
                    subject = subject,
                    topics = topics.filter { it.subjectId == subject.id },
                    expanded = subject.id in expandedSubjects,
                    onExpandedChange = { viewModel.setEditalSubjectExpanded(subject.id, it) },
                    viewModel = viewModel,
                    onTopic = onTopic,
                    onAddTopic = { addTopicFor = subject },
                    onGenerateContent = { topicIds -> contentPromptFor = subject.id to topicIds },
                    onPriority = { priorityTarget = PriorityTarget.Subject(subject, subject.priorityState(selectedCompetition?.priorityState()?.effectivePriority)) },
                    onTopicPriority = { topic, parent -> priorityTarget = PriorityTarget.Topic(topic, topic.priorityState(parent)) },
                    parentPriority = selectedCompetition?.priorityState()?.effectivePriority,
                    aiButtonModifier = if (isFirst) Modifier.tourTarget(TourKey.SUBJECT_AI, tourStep?.key) { viewModel.reportTourTargetBounds(TourKey.SUBJECT_AI, it) } else Modifier,
                    contentTopicIds = contentTopicIds,
                    onlyWithContent = onlyWithContent,
                )
            }
        }
    }
}

@Composable
internal fun SubjectCard(subject: SubjectEntity, topics: List<TopicEntity>, expanded: Boolean, onExpandedChange: (Boolean) -> Unit, viewModel: AppViewModel, onTopic: (Long) -> Unit, onAddTopic: () -> Unit, onGenerateContent: (Set<Long>?) -> Unit, onPriority: () -> Unit, onTopicPriority: (TopicEntity, PriorityLevel) -> Unit, parentPriority: PriorityLevel?, aiButtonModifier: Modifier = Modifier, contentTopicIds: Set<Long> = emptySet(), onlyWithContent: Boolean = false) {
    var menu by remember { mutableStateOf(false) }
    // Quando o filtro está ativo, calcula o subconjunto de IDs que têm conteúdo (diretamente
    // ou em algum descendente), para que a árvore não apareça com "buracos".
    fun hasContentInSubtree(topicId: Long): Boolean {
        if (topicId in contentTopicIds) return true
        return topics.filter { it.parentTopicId == topicId }.any { hasContentInSubtree(it.id) }
    }
    val visibleTopicIds: Set<Long>? = if (onlyWithContent) topics.filter { hasContentInSubtree(it.id) }.map { it.id }.toSet() else null
    ElevatedCard {
        Column {
            Row(
                Modifier
                    .fillMaxWidth()
                    .clickable { onExpandedChange(!expanded) }
                    .testTag("edital_subject_header")
                    .padding(start = 16.dp, top = 8.dp, bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(onClick = { onExpandedChange(!expanded) }) { Icon(if (expanded) Icons.Outlined.ExpandLess else Icons.Outlined.ExpandMore, if (expanded) "Recolher matéria" else "Expandir matéria") }
                Column(Modifier.weight(1f)) {
                    Text(
                        subject.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                    )
                    val done = topics.count { it.status != TopicStatus.NAO_ESTUDADO }
                    Text(
                        "$done de ${topics.size} tópicos iniciados • prioridade ${PriorityPresentation.label(subject.priorityState(parentPriority).effectivePriority)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                IconButton(onClick = { onGenerateContent(null) }, modifier = aiButtonModifier) { Icon(Icons.Outlined.AutoAwesome, "Gerar conteúdo da matéria com IA", tint = MaterialTheme.colorScheme.primary) }
                Box {
                    IconButton(onClick = { menu = true }) { Icon(Icons.Outlined.MoreVert, "Opções") }
                    DropdownMenu(expanded = menu, onDismissRequest = { menu = false }) {
                        DropdownMenuItem(text = { Text("Gerar conteúdo com IA") }, leadingIcon = { Icon(Icons.Outlined.AutoAwesome, null) }, onClick = { menu = false; onGenerateContent(null) })
                        DropdownMenuItem(text = { Text("Prioridade") }, leadingIcon = { Icon(Icons.Outlined.Flag, null) }, onClick = { menu = false; onPriority() })
                        DropdownMenuItem(text = { Text("Adicionar tópico") }, leadingIcon = { Icon(Icons.Outlined.Add, null) }, onClick = { menu = false; onAddTopic() })
                        DropdownMenuItem(text = { Text("Excluir matéria") }, leadingIcon = { Icon(Icons.Outlined.Delete, null) }, onClick = { menu = false; viewModel.deleteSubject(subject) })
                    }
                }
            }
            if (expanded) {
                if (topics.isEmpty()) TextButton(onClick = onAddTopic, modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) { Text("+ Adicionar tópico") }
                val rootTopics = topics.filter { it.parentTopicId == null }.sortedBy { it.position }
                val visibleRootTopics = if (visibleTopicIds != null) rootTopics.filter { it.id in visibleTopicIds } else rootTopics
                visibleRootTopics.forEach { topic ->
                    TopicTreeRows(topic, topics, 0, subject.priorityState(parentPriority).effectivePriority, viewModel, onTopic, onTopicPriority, onGenerateContent = { onGenerateContent(setOf(it)) }, visibleTopicIds = visibleTopicIds)
                }
            }
        }
    }
}


@Composable
private fun TopicTreeRows(topic: TopicEntity, allTopics: List<TopicEntity>, depth: Int, parentPriority: PriorityLevel, viewModel: AppViewModel, onTopic: (Long) -> Unit, onPriority: (TopicEntity, PriorityLevel) -> Unit, onGenerateContent: (Long) -> Unit, visibleTopicIds: Set<Long>? = null) {
    TopicRow(topic, depth, parentPriority, viewModel, onClick = { onTopic(topic.id) }, onPriority = { onPriority(topic, parentPriority) }, onGenerateContent = { onGenerateContent(topic.id) })
    HorizontalDivider(Modifier.padding(start = (16 + depth * 20).dp, end = 16.dp), color = MaterialTheme.colorScheme.outlineVariant)
    val children = allTopics.filter { it.parentTopicId == topic.id }.sortedBy { it.position }
    val visibleChildren = if (visibleTopicIds != null) children.filter { it.id in visibleTopicIds } else children
    visibleChildren.forEach { child ->
        TopicTreeRows(child, allTopics, depth + 1, topic.priorityState(parentPriority).effectivePriority, viewModel, onTopic, onPriority, onGenerateContent, visibleTopicIds)
    }
}

@Composable
private fun TopicRow(topic: TopicEntity, depth: Int, parentPriority: PriorityLevel, viewModel: AppViewModel, onClick: () -> Unit, onPriority: () -> Unit, onGenerateContent: () -> Unit) {
    var menu by remember { mutableStateOf(false) }
    ListItem(
        headlineContent = { Text(topic.title, fontWeight = FontWeight.Medium) },
        supportingContent = {
            Text("${topic.status.displayName()} • ${topic.contentOriginType.displayName()} • prioridade ${PriorityPresentation.label(topic.priorityState(parentPriority).effectivePriority)}")
        },
        leadingContent = {
            if (depth > 0) Icon(Icons.Outlined.SubdirectoryArrowRight, "Subtópico", tint = MaterialTheme.colorScheme.primary)
            else Icon(if (topic.status == TopicStatus.NAO_ESTUDADO) Icons.Outlined.RadioButtonUnchecked else Icons.Outlined.CheckCircle, null, tint = if (topic.status == TopicStatus.NAO_ESTUDADO) MaterialTheme.colorScheme.outline else MaterialTheme.colorScheme.secondary)
        },
        trailingContent = {
            Box {
                IconButton(onClick = { menu = true }) { Icon(Icons.Outlined.MoreVert, "Opções") }
                DropdownMenu(expanded = menu, onDismissRequest = { menu = false }) {
                    DropdownMenuItem(text = { Text("Abrir") }, onClick = { menu = false; onClick() })
                    DropdownMenuItem(text = { Text("Gerar conteúdo com IA") }, onClick = { menu = false; onGenerateContent() })
                    DropdownMenuItem(text = { Text("Prioridade") }, leadingIcon = { Icon(Icons.Outlined.Flag, null) }, onClick = { menu = false; onPriority() })
                    if (topic.status == br.com.estudario.data.local.TopicStatus.NAO_ESTUDADO) {
                        DropdownMenuItem(text = { Text("Marcar estudado") }, onClick = { menu = false; viewModel.markStudied(topic) })
                    } else {
                        // Marcar sem querer acontece; desmarcar cancela as revisões pendentes e
                        // devolve o tópico para "não estudado", sem apagar o que já foi feito.
                        DropdownMenuItem(text = { Text("Desmarcar estudado") }, onClick = { menu = false; viewModel.unmarkStudied(topic) })
                    }
                    DropdownMenuItem(text = { Text("Adicionar à fila") }, onClick = { menu = false; viewModel.enqueue(topic.id) })
                    DropdownMenuItem(
                        text = { Text("Excluir", color = MaterialTheme.colorScheme.error) },
                        leadingIcon = { Icon(Icons.Outlined.Delete, null, tint = MaterialTheme.colorScheme.error) },
                        onClick = { menu = false; viewModel.deleteTopic(topic) },
                    )
                }
            }
        },
        modifier = Modifier.fillMaxWidth().padding(start = (depth * 20).dp).clickable(onClick = onClick),
    )
}

private sealed interface PriorityTarget {
    val title: String
    val state: PriorityEditorState

    data class Competition(val value: CompetitionEntity, override val state: PriorityEditorState) : PriorityTarget {
        override val title get() = value.name
    }
    data class Subject(val value: SubjectEntity, override val state: PriorityEditorState) : PriorityTarget {
        override val title get() = value.name
    }
    data class Topic(val value: TopicEntity, override val state: PriorityEditorState) : PriorityTarget {
        override val title get() = value.title
    }
}

private fun CompetitionEntity.priorityState() = PriorityPresentation.state(
    assessedPriorityScore, assessedPrioritySource, assessedPriorityConfidence, assessedPriorityRationale,
    assessedPriorityEvidenceJson, hasAssessedPriority, userPriorityOverride, null,
)

private fun SubjectEntity.priorityState(parent: PriorityLevel? = null) = PriorityPresentation.state(
    assessedPriorityScore, assessedPrioritySource, assessedPriorityConfidence, assessedPriorityRationale,
    assessedPriorityEvidenceJson, hasAssessedPriority, userPriorityOverride, parent,
)

private fun TopicEntity.priorityState(parent: PriorityLevel) = PriorityPresentation.state(
    assessedPriorityScore, assessedPrioritySource, assessedPriorityConfidence, assessedPriorityRationale,
    assessedPriorityEvidenceJson, hasAssessedPriority, userPriorityOverride, parent,
)

private suspend fun readEditalFile(context: Context, uri: Uri): String? = withContext(Dispatchers.IO) {
    runCatching { context.contentResolver.openInputStream(uri)?.use { it.readBytes().toString(Charsets.UTF_8) } }.getOrNull()
}

fun ContentOriginType.displayName(): String = when (this) {
    ContentOriginType.EDITAL -> "📌 Edital"
    ContentOriginType.DIDACTIC_SUBDIVISION -> "📚 Subdivisão de estudo"
    ContentOriginType.AUXILIARY_CONTENT -> "➕ Conteúdo complementar"
}

fun TopicStatus.displayName(): String = when (this) {
    TopicStatus.NAO_ESTUDADO -> "Não estudado"
    TopicStatus.EM_ESTUDO -> "Em estudo"
    TopicStatus.ESTUDADO -> "Estudado"
    TopicStatus.REVISANDO -> "Revisando"
    TopicStatus.DOMINADO -> "Dominado"
}
