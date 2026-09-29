package br.com.estudario.ui.screens

import br.com.estudario.ui.theme.screenPadding
import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.estudario.data.local.*
import br.com.estudario.domain.PriorityLevel
import br.com.estudario.ui.AppViewModel
import br.com.estudario.ui.components.ConfirmDialog
import br.com.estudario.ui.components.EmptyState
import br.com.estudario.ui.components.PriorityEditorDialog
import br.com.estudario.ui.components.PriorityEditorState
import br.com.estudario.ui.components.PriorityPresentation
import br.com.estudario.ui.components.ScreenTitle
import br.com.estudario.ui.components.TextInputDialog
import br.com.estudario.ui.prompt.ContentPromptBuilderDialog
import br.com.estudario.ui.prompt.EditalPromptBuilderDialog
import br.com.estudario.ui.theme.EstudarioShapes
import br.com.estudario.ui.theme.estudarioColors
import br.com.estudario.ui.tour.TourKey
import br.com.estudario.ui.tour.tourTarget
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * O edital como mapa do que estudar. O material é gerado tópico a tópico, nunca a matéria inteira
 * de uma vez: cada pedido fica focado, sai mais profundo e não deixa a pessoa esperando minutos.
 */
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
                if (text.isNullOrBlank()) viewModel.reportIncomingFileError("Não foi possível ler o arquivo escolhido.") else viewModel.openIncomingText(text, selectedCompetitionId.takeIf { it != 0L })
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

    val selectedSubjects = subjects.filter { it.competitionId == selectedCompetitionId }
    val contentTopicIds = remember(theories, summaries, snippets, questions) {
        buildSet {
            theories.forEach { add(it.topicId) }
            summaries.forEach { add(it.topicId) }
            snippets.forEach { add(it.topicId) }
            questions.forEach { add(it.question.topicId) }
        }
    }
    LazyColumn(Modifier.fillMaxSize(), state = listState, contentPadding = screenPadding(), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        item {
            ScreenTitle("Edital", "Cada tópico vira material de estudo") {
                Row {
                    IconButton(onClick = onHelp) { Icon(Icons.Outlined.HelpOutline, "Como montar o edital") }
                    IconButton(
                        onClick = { showEditalPrompt = true },
                        modifier = Modifier.tourTarget(TourKey.EDITAL_AI, tourStep?.key) { viewModel.reportTourTargetBounds(TourKey.EDITAL_AI, it) },
                    ) { Icon(Icons.Outlined.AutoAwesome, "Montar edital com IA") }
                    var more by remember { mutableStateOf(false) }
                    Box {
                        IconButton(
                            onClick = { more = true },
                            modifier = Modifier
                                .tourTarget(TourKey.EDITAL_IMPORT, tourStep?.key) { viewModel.reportTourTargetBounds(TourKey.EDITAL_IMPORT, it) }
                                .tourTarget(TourKey.EDITAL_CREATE, tourStep?.key) { viewModel.reportTourTargetBounds(TourKey.EDITAL_CREATE, it) },
                        ) { Icon(Icons.Outlined.MoreVert, "Mais opções do edital") }
                        DropdownMenu(expanded = more, onDismissRequest = { more = false }) {
                            DropdownMenuItem(text = { Text("Novo concurso") }, leadingIcon = { Icon(Icons.Outlined.Add, null) }, onClick = { more = false; addCompetition = true })
                            DropdownMenuItem(text = { Text("Importar arquivo .estudo") }, leadingIcon = { Icon(Icons.Outlined.FileOpen, null) }, onClick = { more = false; pickFile() })
                            if (selectedCompetition != null) {
                                DropdownMenuItem(text = { Text("Tornar concurso principal") }, leadingIcon = { Icon(Icons.Outlined.Star, null) }, onClick = { more = false; viewModel.setPrimary(selectedCompetitionId) })
                                DropdownMenuItem(text = { Text("Prioridade do concurso") }, leadingIcon = { Icon(Icons.Outlined.Flag, null) }, onClick = { more = false; priorityTarget = PriorityTarget.Competition(selectedCompetition, selectedCompetition.priorityState()) })
                                DropdownMenuItem(
                                    text = { Text("Excluir edital", color = MaterialTheme.colorScheme.error) },
                                    leadingIcon = { Icon(Icons.Outlined.Delete, null, tint = MaterialTheme.colorScheme.error) },
                                    onClick = { more = false; deleteCompetition = selectedCompetition },
                                )
                            }
                        }
                    }
                }
            }
        }
        if (competitions.isEmpty()) {
            item {
                Surface(shape = EstudarioShapes.spotlight, color = MaterialTheme.colorScheme.primaryContainer) {
                    Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text("Comece pelo edital", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                        Text("Envie o PDF do edital e a IA organiza as matérias e os tópicos. Depois, cada tópico vira teoria, flashcards e questões.")
                        Button(onClick = { showEditalPrompt = true }, Modifier.fillMaxWidth()) { Icon(Icons.Outlined.AutoAwesome, null); Spacer(Modifier.width(8.dp)); Text("Montar edital com IA") }
                        OutlinedButton(onClick = pickFile, Modifier.fillMaxWidth()) { Icon(Icons.Outlined.FileOpen, null); Spacer(Modifier.width(8.dp)); Text("Importar arquivo .estudo") }
                        TextButton(onClick = { addCompetition = true }, Modifier.fillMaxWidth()) { Text("Criar concurso manualmente") }
                    }
                }
            }
        } else {
            if (competitions.size > 1) item {
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
            // Visão do concurso: quanto do edital já virou material e quanto já foi estudado.
            item {
                val allTopics = topics.filter { t -> selectedSubjects.any { it.id == t.subjectId } }
                val withContent = allTopics.count { it.id in contentTopicIds }
                val studied = allTopics.count { it.status != TopicStatus.NAO_ESTUDADO }
                Surface(shape = EstudarioShapes.spotlight, color = MaterialTheme.colorScheme.primaryContainer, modifier = Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text(selectedCompetition?.name ?: "", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimaryContainer, maxLines = 3)
                        Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                            EditalStat("${selectedSubjects.size}", "matérias")
                            EditalStat("${allTopics.size}", "tópicos")
                            EditalStat("$withContent", "com material")
                            EditalStat("$studied", "estudados")
                        }
                        ProgressLine(if (allTopics.isEmpty()) 0f else withContent.toFloat() / allTopics.size, MaterialTheme.colorScheme.primary)
                        Text(
                            if (withContent < allTopics.size) "Toque em ✨ Gerar num tópico: a IA do Estudário escreve a teoria, os flashcards e as questões só dele, com mais profundidade."
                            else "Todo o edital tem material. Agora é estudar e treinar.",
                            style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onPrimaryContainer,
                        )
                    }
                }
            }
            item {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = query,
                        onValueChange = { query = it },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        placeholder = { Text("Buscar matéria ou tópico") },
                        leadingIcon = { Icon(Icons.Outlined.Search, null) },
                        trailingIcon = if (query.isNotEmpty()) {{ IconButton(onClick = { query = "" }) { Icon(Icons.Outlined.Close, "Limpar pesquisa") } }} else null,
                        shape = EstudarioShapes.pill,
                    )
                    FilledTonalIconButton(onClick = { addSubject = true }) { Icon(Icons.Outlined.Add, "Adicionar matéria") }
                }
            }
            item {
                FilterChip(
                    selected = onlyWithContent,
                    onClick = { onlyWithContent = !onlyWithContent },
                    label = { Text("Só tópicos com material") },
                    leadingIcon = { Icon(if (onlyWithContent) Icons.Outlined.Check else Icons.Outlined.LibraryBooks, null, Modifier.size(18.dp)) },
                )
            }
            val visibleSubjects = selectedSubjects.filter { subject ->
                val subjectTopics = topics.filter { it.subjectId == subject.id }
                val matchesSearch = query.isBlank() || subject.name.contains(query, ignoreCase = true) || subjectTopics.any { it.title.contains(query, ignoreCase = true) }
                val matchesContent = !onlyWithContent || subjectTopics.any { it.id in contentTopicIds }
                matchesSearch && matchesContent
            }
            if (selectedSubjects.isEmpty()) item { EmptyState("Edital vazio", "Adicione a primeira matéria deste concurso.", "Adicionar matéria") { addSubject = true } }
            else if (visibleSubjects.isEmpty()) item { EmptyState("Nenhuma matéria encontrada", "Altere a pesquisa ou desative o filtro de material.") }
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
private fun EditalStat(value: String, label: String) {
    Column {
        Text(value, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimaryContainer)
        Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onPrimaryContainer)
    }
}

@Composable
private fun ProgressLine(fraction: Float, color: Color, track: Color = MaterialTheme.colorScheme.surface.copy(alpha = 0.6f)) {
    Box(Modifier.fillMaxWidth().height(8.dp).clip(CircleShape).background(track)) {
        Box(Modifier.fillMaxWidth(fraction.coerceIn(0.015f, 1f)).fillMaxHeight().clip(CircleShape).background(color))
    }
}

/** Uma matéria do edital: progresso de material e de estudo, e os tópicos com o botão de gerar. */
@Composable
internal fun SubjectCard(subject: SubjectEntity, topics: List<TopicEntity>, expanded: Boolean, onExpandedChange: (Boolean) -> Unit, viewModel: AppViewModel, onTopic: (Long) -> Unit, onAddTopic: () -> Unit, onGenerateContent: (Set<Long>?) -> Unit, onPriority: () -> Unit, onTopicPriority: (TopicEntity, PriorityLevel) -> Unit, parentPriority: PriorityLevel?, aiButtonModifier: Modifier = Modifier, contentTopicIds: Set<Long> = emptySet(), onlyWithContent: Boolean = false) {
    var menu by remember { mutableStateOf(false) }
    // Com o filtro ativo, mantém os pais de tópicos com material para a árvore não ficar com buracos.
    fun hasContentInSubtree(topicId: Long): Boolean {
        if (topicId in contentTopicIds) return true
        return topics.filter { it.parentTopicId == topicId }.any { hasContentInSubtree(it.id) }
    }
    val visibleTopicIds: Set<Long>? = if (onlyWithContent) topics.filter { hasContentInSubtree(it.id) }.map { it.id }.toSet() else null
    val withContent = topics.count { it.id in contentTopicIds }
    val studied = topics.count { it.status != TopicStatus.NAO_ESTUDADO }
    Surface(shape = EstudarioShapes.panel, color = MaterialTheme.colorScheme.surfaceContainerLow) {
        Column {
            Row(
                Modifier
                    .fillMaxWidth()
                    .clickable { onExpandedChange(!expanded) }
                    .testTag("edital_subject_header")
                    .then(aiButtonModifier)
                    .padding(start = 16.dp, top = 14.dp, bottom = 14.dp, end = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(subject.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text(
                        "$withContent de ${topics.size} com material · $studied estudados · prioridade ${PriorityPresentation.label(subject.priorityState(parentPriority).effectivePriority).lowercase()}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    ProgressLine(if (topics.isEmpty()) 0f else withContent.toFloat() / topics.size, MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.surfaceVariant)
                }
                Icon(if (expanded) Icons.Outlined.ExpandLess else Icons.Outlined.ExpandMore, if (expanded) "Recolher matéria" else "Expandir matéria", Modifier.padding(start = 8.dp))
                Box {
                    IconButton(onClick = { menu = true }) { Icon(Icons.Outlined.MoreVert, "Opções") }
                    DropdownMenu(expanded = menu, onDismissRequest = { menu = false }) {
                        DropdownMenuItem(text = { Text("Prioridade") }, leadingIcon = { Icon(Icons.Outlined.Flag, null) }, onClick = { menu = false; onPriority() })
                        DropdownMenuItem(text = { Text("Adicionar tópico") }, leadingIcon = { Icon(Icons.Outlined.Add, null) }, onClick = { menu = false; onAddTopic() })
                        DropdownMenuItem(text = { Text("Excluir matéria", color = MaterialTheme.colorScheme.error) }, leadingIcon = { Icon(Icons.Outlined.Delete, null, tint = MaterialTheme.colorScheme.error) }, onClick = { menu = false; viewModel.deleteSubject(subject) })
                    }
                }
            }
            if (expanded) {
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                if (topics.isEmpty()) TextButton(onClick = onAddTopic, modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) { Text("+ Adicionar tópico") }
                val rootTopics = topics.filter { it.parentTopicId == null }.sortedBy { it.position }
                val visibleRootTopics = if (visibleTopicIds != null) rootTopics.filter { it.id in visibleTopicIds } else rootTopics
                visibleRootTopics.forEach { topic ->
                    TopicTreeRows(topic, topics, 0, subject.priorityState(parentPriority).effectivePriority, viewModel, onTopic, onTopicPriority, onGenerateContent = { onGenerateContent(setOf(it)) }, visibleTopicIds = visibleTopicIds, contentTopicIds = contentTopicIds)
                }
            }
        }
    }
}

@Composable
private fun TopicTreeRows(topic: TopicEntity, allTopics: List<TopicEntity>, depth: Int, parentPriority: PriorityLevel, viewModel: AppViewModel, onTopic: (Long) -> Unit, onPriority: (TopicEntity, PriorityLevel) -> Unit, onGenerateContent: (Long) -> Unit, visibleTopicIds: Set<Long>? = null, contentTopicIds: Set<Long> = emptySet()) {
    TopicRow(topic, depth, viewModel, hasContent = topic.id in contentTopicIds, onClick = { onTopic(topic.id) }, onPriority = { onPriority(topic, parentPriority) }, onGenerateContent = { onGenerateContent(topic.id) })
    val children = allTopics.filter { it.parentTopicId == topic.id }.sortedBy { it.position }
    val visibleChildren = if (visibleTopicIds != null) children.filter { it.id in visibleTopicIds } else children
    visibleChildren.forEach { child ->
        TopicTreeRows(child, allTopics, depth + 1, topic.priorityState(parentPriority).effectivePriority, viewModel, onTopic, onPriority, onGenerateContent, visibleTopicIds, contentTopicIds)
    }
}

@Composable
private fun TopicRow(topic: TopicEntity, depth: Int, viewModel: AppViewModel, hasContent: Boolean, onClick: () -> Unit, onPriority: () -> Unit, onGenerateContent: () -> Unit) {
    var menu by remember { mutableStateOf(false) }
    val studied = topic.status != TopicStatus.NAO_ESTUDADO
    Row(
        Modifier.fillMaxWidth().clickable(onClick = onClick).padding(start = (16 + depth * 18).dp, end = 4.dp, top = 10.dp, bottom = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            if (studied) Icons.Outlined.CheckCircle else if (depth > 0) Icons.Outlined.SubdirectoryArrowRight else Icons.Outlined.RadioButtonUnchecked,
            null,
            Modifier.size(20.dp),
            tint = if (studied) estudarioColors().completed else MaterialTheme.colorScheme.outline,
        )
        Column(Modifier.weight(1f).padding(horizontal = 12.dp)) {
            Text(topic.title, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
            Text(
                if (hasContent) "${topic.status.displayName()} · material pronto" else topic.status.displayName(),
                style = MaterialTheme.typography.labelSmall,
                color = if (hasContent) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        if (!hasContent) {
            FilledTonalButton(onClick = onGenerateContent, contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp), modifier = Modifier.height(34.dp)) {
                Icon(Icons.Outlined.AutoAwesome, null, Modifier.size(16.dp)); Spacer(Modifier.width(4.dp)); Text("Gerar", style = MaterialTheme.typography.labelMedium)
            }
        }
        Box {
            IconButton(onClick = { menu = true }) { Icon(Icons.Outlined.MoreVert, "Opções") }
            DropdownMenu(expanded = menu, onDismissRequest = { menu = false }) {
                DropdownMenuItem(text = { Text("Abrir") }, onClick = { menu = false; onClick() })
                DropdownMenuItem(text = { Text(if (hasContent) "Gerar mais material" else "Gerar material com IA") }, leadingIcon = { Icon(Icons.Outlined.AutoAwesome, null) }, onClick = { menu = false; onGenerateContent() })
                DropdownMenuItem(text = { Text("Prioridade") }, leadingIcon = { Icon(Icons.Outlined.Flag, null) }, onClick = { menu = false; onPriority() })
                if (topic.status == TopicStatus.NAO_ESTUDADO) {
                    DropdownMenuItem(text = { Text("Marcar estudado") }, onClick = { menu = false; viewModel.markStudied(topic) })
                } else {
                    // Desmarcar cancela as revisões pendentes sem apagar o que já foi feito.
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
    }
    HorizontalDivider(Modifier.padding(start = (16 + depth * 18).dp, end = 16.dp), color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
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
