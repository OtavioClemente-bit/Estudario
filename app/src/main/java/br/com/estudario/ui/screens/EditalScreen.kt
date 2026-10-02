package br.com.estudario.ui.screens

import br.com.estudario.ui.components.AlertDialog
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
import br.com.estudario.ui.components.ActionSheet
import br.com.estudario.ui.components.Attention
import br.com.estudario.ui.components.AttentionDot
import br.com.estudario.ui.components.AttentionLegend
import br.com.estudario.ui.components.SheetAction
import br.com.estudario.ui.components.color
import br.com.estudario.ui.components.ConfirmDialog
import androidx.compose.foundation.shape.RoundedCornerShape
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
    // Guarda a escolha: recalcular quando a lista muda jogava a pessoa de volta ao principal logo
    // depois de criar um concurso novo (e a matéria nova ia para o concurso errado).
    var selectedCompetitionId by rememberSaveable { mutableLongStateOf(0L) }
    LaunchedEffect(competitions) {
        if (competitions.none { it.id == selectedCompetitionId }) selectedCompetitionId = competitions.firstOrNull { it.isPrimary }?.id ?: competitions.firstOrNull()?.id ?: 0L
    }
    var addCompetition by remember { mutableStateOf(false) }
    var addSubject by remember { mutableStateOf(false) }
    var addTopicFor by remember { mutableStateOf<SubjectEntity?>(null) }
    var deleteCompetition by remember { mutableStateOf<CompetitionEntity?>(null) }
    var query by rememberSaveable { mutableStateOf("") }
    var onlyWithContent by rememberSaveable { mutableStateOf(false) }
    var showEditalPrompt by remember { mutableStateOf(false) }
    var contentPromptFor by remember { mutableStateOf<Pair<Long, Set<Long>?>?>(null) }
    var priorityTarget by remember { mutableStateOf<PriorityTarget?>(null) }
    var competitionMenu by remember { mutableStateOf<CompetitionEntity?>(null) }
    var showCatalogReview by remember { mutableStateOf(false) }
    val catalogRepository = (LocalContext.current.applicationContext as br.com.estudario.EstudarioApplication).contestCatalog
    val isCatalogAdmin by produceState(false) { value = catalogRepository.isAdmin() }
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

    if (showCatalogReview) br.com.estudario.ui.catalog.CatalogReviewDialog(onDismiss = { showCatalogReview = false })
    if (showEditalPrompt) EditalPromptBuilderDialog(viewModel, selectedCompetitionId.takeIf { it != 0L }, onDismiss = { showEditalPrompt = false }, onPickFile = pickFile)
    contentPromptFor?.let { (subjectId, topicIds) -> ContentPromptBuilderDialog(viewModel, subjectId, topicIds, onDismiss = { contentPromptFor = null }, onPickFile = pickFile) }
    if (addCompetition) NewCompetitionDialog(
        onDismiss = { addCompetition = false },
        onCreate = { name, role, path ->
            addCompetition = false
            scope.launch {
                val id = viewModel.createCompetition(name)
                selectedCompetitionId = id
                when (path) {
                    NewCompetitionPath.EDITAL -> viewModel.openAiReview(id, name.trim(), preferences = br.com.estudario.data.ai.AiSyllabusPreferences(competitionName = name.trim(), role = role.trim()))
                    NewCompetitionPath.MANUAL -> addSubject = true
                    NewCompetitionPath.IMPORT -> pickFile()
                }
            }
        },
    )
    competitionMenu?.let { competition ->
        ActionSheet(
            title = competition.name,
            subtitle = if (competition.isPrimary) "Concurso principal" else "Concurso",
            actions = buildList {
                add(SheetAction(Icons.Outlined.Add, "Novo concurso", "Pelo edital, manualmente ou por arquivo") { addCompetition = true })
                add(SheetAction(Icons.Outlined.Flag, "Prioridade do concurso", "Quanto este concurso pesa no seu plano") { priorityTarget = PriorityTarget.Competition(competition, competition.priorityState()) })
                if (!competition.isPrimary) add(SheetAction(Icons.Outlined.Star, "Tornar principal", "Aparece primeiro no Início e no plano") { viewModel.setPrimary(competition.id) })
                add(SheetAction(Icons.Outlined.FileOpen, "Importar arquivo .estudo", "Matérias, tópicos ou material prontos") { pickFile() })
                if (isCatalogAdmin) add(SheetAction(Icons.Outlined.VerifiedUser, "Aprovar envios do catálogo") { showCatalogReview = true })
                add(SheetAction(Icons.Outlined.DeleteOutline, "Excluir concurso", "Apaga matérias, tópicos e material deste aparelho", destructive = true) { deleteCompetition = competition })
            },
            onDismiss = { competitionMenu = null },
        )
    }
    if (addSubject && selectedCompetitionId != 0L) TextInputDialog("Nova matéria", label = "Nome da matéria", onDismiss = { addSubject = false }) { viewModel.addSubject(selectedCompetitionId, it) }
    addTopicFor?.let { subject -> TextInputDialog("Novo tópico", label = "Título do tópico", onDismiss = { addTopicFor = null }) { viewModel.addTopic(subject.id, it) } }
    deleteCompetition?.let { competition ->
        ConfirmDialog(
            title = "Excluir concurso?",
            message = "“${competition.name}” e todas as matérias, tópicos e conteúdos relacionados serão removidos deste aparelho. Essa ação não pode ser desfeita.",
            confirmLabel = "Excluir concurso",
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
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text("Concursos", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                    Text("Cada tópico vira material de estudo", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                FilledTonalButton(onClick = { addCompetition = true }, shape = RoundedCornerShape(14.dp)) {
                    Icon(Icons.Outlined.Add, null, Modifier.size(18.dp)); Spacer(Modifier.width(6.dp)); Text("Novo")
                }
            }
        }
        if (competitions.isEmpty()) {
            item {
                Surface(shape = EstudarioShapes.spotlight, color = MaterialTheme.colorScheme.primaryContainer) {
                    Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text("Comece pelo seu concurso", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                        Text("Envie o PDF do edital e o Estudário organiza as matérias e os tópicos. Depois, cada tópico vira teoria, flashcards e questões.")
                        Button(onClick = { addCompetition = true }, Modifier.fillMaxWidth().height(52.dp), shape = RoundedCornerShape(14.dp)) { Icon(Icons.Outlined.Add, null); Spacer(Modifier.width(8.dp)); Text("Criar meu concurso") }
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
                        Row(verticalAlignment = Alignment.Top) {
                            Text(selectedCompetition?.name ?: "", Modifier.weight(1f), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimaryContainer, maxLines = 3)
                            IconButton(onClick = { competitionMenu = selectedCompetition }) { Icon(Icons.Outlined.MoreVert, "Opções do concurso", tint = MaterialTheme.colorScheme.onPrimaryContainer) }
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                            EditalStat("${selectedSubjects.size}", "matérias")
                            EditalStat("${allTopics.size}", "tópicos")
                            EditalStat("$withContent", "com material")
                            EditalStat("$studied", "estudados")
                        }
                        ProgressLine(if (allTopics.isEmpty()) 0f else withContent.toFloat() / allTopics.size, MaterialTheme.colorScheme.primary)
                        Text(
                            if (withContent < allTopics.size) "Abra um tópico e toque em Gerar: o Estudário escreve a teoria, os flashcards e as questões dele."
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
                }
            }
            item {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    FilterChip(
                        selected = onlyWithContent,
                        onClick = { onlyWithContent = !onlyWithContent },
                        label = { Text("Com material") },
                        leadingIcon = { Icon(if (onlyWithContent) Icons.Outlined.Check else Icons.Outlined.LibraryBooks, null, Modifier.size(18.dp)) },
                    )
                    Spacer(Modifier.weight(1f))
                    TextButton(onClick = { addSubject = true }) { Icon(Icons.Outlined.Add, null, Modifier.size(18.dp)); Spacer(Modifier.width(4.dp)); Text("Nova matéria") }
                }
            }
            item { AttentionLegend() }
            val visibleSubjects = selectedSubjects.filter { subject ->
                val subjectTopics = topics.filter { it.subjectId == subject.id }
                val matchesSearch = query.isBlank() || subject.name.contains(query, ignoreCase = true) || subjectTopics.any { it.title.contains(query, ignoreCase = true) }
                val matchesContent = !onlyWithContent || subjectTopics.any { it.id in contentTopicIds }
                matchesSearch && matchesContent
            }
            if (selectedSubjects.isEmpty()) item { EmptyState("Nenhuma matéria ainda", "Adicione a primeira matéria deste concurso.", "Adicionar matéria") { addSubject = true } }
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
    val setup by viewModel.initialSetup.collectAsState()
    val difficulty = setup?.subjectDifficulties?.get(subject.id.toString())
    val subjectPriority = subject.priorityState(parentPriority).effectivePriority
    val attention = Attention.of(subjectPriority, difficulty)
    if (menu) ActionSheet(
        title = subject.name,
        subtitle = "${attention.label} · prioridade ${PriorityPresentation.label(subjectPriority).lowercase()}${difficulty?.let { " · " + it.label.lowercase() } ?: ""}",
        actions = listOf(
            SheetAction(Icons.Outlined.Add, "Novo tópico", "Um item do edital que ficou de fora") { onAddTopic() },
            SheetAction(Icons.Outlined.Flag, "Prioridade da matéria", "Quanto ela pesa na prova") { onPriority() },
            SheetAction(Icons.Outlined.DeleteOutline, "Excluir matéria", "Apaga os tópicos e o material dela", destructive = true) { viewModel.deleteSubject(subject) },
        ),
        onDismiss = { menu = false },
    )
    Surface(shape = EstudarioShapes.panel, color = MaterialTheme.colorScheme.surfaceContainerLow) {
        Row(Modifier.height(IntrinsicSize.Min)) {
        // Faixa de cor: quanto a matéria pede de atenção (prova + dificuldade da pessoa).
        Box(Modifier.width(5.dp).fillMaxHeight().background(attention.color()))
        Column(Modifier.weight(1f)) {
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
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(shape = RoundedCornerShape(50), color = attention.color().copy(alpha = .14f)) {
                            Row(Modifier.padding(horizontal = 8.dp, vertical = 2.dp), verticalAlignment = Alignment.CenterVertically) {
                                AttentionDot(attention, 6)
                                Spacer(Modifier.width(4.dp))
                                Text(attention.label, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.SemiBold, color = attention.color())
                            }
                        }
                        Spacer(Modifier.width(8.dp))
                        Text("$withContent/${topics.size} com material · $studied estudados", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1, overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis)
                    }
                    ProgressLine(if (topics.isEmpty()) 0f else withContent.toFloat() / topics.size, MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.surfaceVariant)
                }
                Icon(if (expanded) Icons.Outlined.ExpandLess else Icons.Outlined.ExpandMore, if (expanded) "Recolher matéria" else "Expandir matéria", Modifier.padding(start = 8.dp))
                IconButton(onClick = { menu = true }) { Icon(Icons.Outlined.MoreVert, "Opções da matéria") }
            }
            if (expanded) {
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                if (topics.isEmpty()) TextButton(onClick = onAddTopic, modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) { Icon(Icons.Outlined.Add, null, Modifier.size(18.dp)); Spacer(Modifier.width(4.dp)); Text("Novo tópico") }
                val rootTopics = topics.filter { it.parentTopicId == null }.sortedBy { it.position }
                val visibleRootTopics = if (visibleTopicIds != null) rootTopics.filter { it.id in visibleTopicIds } else rootTopics
                visibleRootTopics.forEach { topic ->
                    TopicTreeRows(topic, topics, 0, subjectPriority, viewModel, onTopic, onTopicPriority, onGenerateContent = { onGenerateContent(setOf(it)) }, visibleTopicIds = visibleTopicIds, contentTopicIds = contentTopicIds, difficulty = difficulty)
                }
            }
        }
        }
    }
}

@Composable
private fun TopicTreeRows(topic: TopicEntity, allTopics: List<TopicEntity>, depth: Int, parentPriority: PriorityLevel, viewModel: AppViewModel, onTopic: (Long) -> Unit, onPriority: (TopicEntity, PriorityLevel) -> Unit, onGenerateContent: (Long) -> Unit, visibleTopicIds: Set<Long>? = null, contentTopicIds: Set<Long> = emptySet(), difficulty: br.com.estudario.domain.planner.PersonalDifficulty? = null) {
    val children = allTopics.filter { it.parentTopicId == topic.id }.sortedBy { it.position }
    val attention = Attention.of(topic.priorityState(parentPriority).effectivePriority, difficulty)
    TopicRow(topic, depth, viewModel, hasContent = topic.id in contentTopicIds, childCount = children.size, onClick = { onTopic(topic.id) }, onPriority = { onPriority(topic, parentPriority) }, onGenerateContent = { onGenerateContent(topic.id) }, attention = attention)
    val visibleChildren = if (visibleTopicIds != null) children.filter { it.id in visibleTopicIds } else children
    visibleChildren.forEach { child ->
        TopicTreeRows(child, allTopics, depth + 1, topic.priorityState(parentPriority).effectivePriority, viewModel, onTopic, onPriority, onGenerateContent, visibleTopicIds, contentTopicIds, difficulty)
    }
}

@Composable
private fun TopicRow(topic: TopicEntity, depth: Int, viewModel: AppViewModel, hasContent: Boolean, childCount: Int, onClick: () -> Unit, onPriority: () -> Unit, onGenerateContent: () -> Unit, attention: Attention = Attention.MEDIUM) {
    var menu by remember { mutableStateOf(false) }
    val studied = topic.status != TopicStatus.NAO_ESTUDADO
    if (menu) ActionSheet(
        title = topic.title,
        subtitle = "${attention.label} · ${topic.status.displayName()}",
        actions = buildList {
            add(SheetAction(Icons.Outlined.OpenInNew, "Abrir tópico") { onClick() })
            if (childCount == 0) add(SheetAction(Icons.Outlined.AutoAwesome, if (hasContent) "Gerar mais material" else "Gerar com o Estudário", "Teoria, flashcards e questões") { onGenerateContent() })
            add(SheetAction(Icons.Outlined.Flag, "Prioridade do tópico", "Quanto ele pesa na prova") { onPriority() })
            if (topic.status == TopicStatus.NAO_ESTUDADO) add(SheetAction(Icons.Outlined.CheckCircle, "Marcar como estudado", "As revisões entram na agenda") { viewModel.markStudied(topic) })
            // Desmarcar cancela as revisões pendentes sem apagar o que já foi feito.
            else add(SheetAction(Icons.Outlined.Undo, "Desmarcar estudado", "Cancela as revisões que ainda não foram feitas") { viewModel.unmarkStudied(topic) })
            add(SheetAction(Icons.Outlined.PlaylistAdd, "Adicionar à fila") { viewModel.enqueue(topic.id) })
            add(SheetAction(Icons.Outlined.DeleteOutline, "Excluir tópico", "Apaga o material dele", destructive = true) { viewModel.deleteTopic(topic) })
        },
        onDismiss = { menu = false },
    )
    Row(
        Modifier.fillMaxWidth().clickable(onClick = onClick).padding(start = (16 + depth * 18).dp, end = 4.dp, top = 10.dp, bottom = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(contentAlignment = Alignment.BottomEnd) {
            Icon(
                if (studied) Icons.Outlined.CheckCircle else if (depth > 0) Icons.Outlined.SubdirectoryArrowRight else Icons.Outlined.RadioButtonUnchecked,
                null,
                Modifier.size(20.dp),
                tint = if (studied) estudarioColors().completed else MaterialTheme.colorScheme.outline,
            )
            // Pontinho de cor: atenção que este tópico pede.
            Box(Modifier.size(8.dp).background(MaterialTheme.colorScheme.surfaceContainerLow, CircleShape).padding(1.dp)) { AttentionDot(attention, 6) }
        }
        Column(Modifier.weight(1f).padding(horizontal = 12.dp)) {
            br.com.estudario.ui.components.ExpandableText(topic.title, fontWeight = FontWeight.Medium)
            Text(
                if (childCount > 0) "Dividido em $childCount subtópicos · gere o material em cada um" else if (hasContent) "${topic.status.displayName()} · material pronto" else topic.status.displayName(),
                style = MaterialTheme.typography.labelSmall,
                color = if (hasContent) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        if (!hasContent && childCount == 0) {
            FilledTonalButton(onClick = onGenerateContent, contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp), modifier = Modifier.height(34.dp)) {
                Icon(Icons.Outlined.AutoAwesome, null, Modifier.size(16.dp)); Spacer(Modifier.width(4.dp)); Text("Gerar", style = MaterialTheme.typography.labelMedium)
            }
        }
        IconButton(onClick = { menu = true }) { Icon(Icons.Outlined.MoreVert, "Opções do tópico") }
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

private enum class NewCompetitionPath { EDITAL, MANUAL, IMPORT }

/** Novo concurso: o nome e por onde começar. Ler o edital é o caminho recomendado. */
@Composable
private fun NewCompetitionDialog(onDismiss: () -> Unit, onCreate: (String, String, NewCompetitionPath) -> Unit) {
    var name by remember { mutableStateOf("") }
    var role by remember { mutableStateOf("") }
    var path by remember { mutableStateOf(NewCompetitionPath.EDITAL) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Novo concurso") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    name,
                    { name = it.take(120) },
                    Modifier.fillMaxWidth(),
                    label = { Text("Nome do concurso") },
                    placeholder = { Text("Ex.: Polícia Federal") },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                )
                OutlinedTextField(
                    role,
                    { role = it.take(120) },
                    Modifier.fillMaxWidth(),
                    label = { Text(if (path == NewCompetitionPath.EDITAL) "Cargo ou área" else "Cargo ou área (opcional)") },
                    placeholder = { Text("Ex.: Agente de Polícia") },
                    supportingText = if (path == NewCompetitionPath.EDITAL) ({ Text("É por ele que achamos as suas matérias no edital.") }) else null,
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                )
                Text("Como montar as matérias?", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.SemiBold)
                PathOption(path == NewCompetitionPath.EDITAL, Icons.Outlined.AutoAwesome, "Ler o edital com o Estudário", "Envie o PDF: matérias e tópicos saem prontos. Recomendado.") { path = NewCompetitionPath.EDITAL }
                PathOption(path == NewCompetitionPath.MANUAL, Icons.Outlined.EditNote, "Montar manualmente", "Você digita as matérias e os tópicos.") { path = NewCompetitionPath.MANUAL }
                PathOption(path == NewCompetitionPath.IMPORT, Icons.Outlined.FileOpen, "Importar arquivo .estudo", "Um edital já organizado em arquivo.") { path = NewCompetitionPath.IMPORT }
            }
        },
        confirmButton = { Button(onClick = { onCreate(name, role, path) }, enabled = name.isNotBlank() && (path != NewCompetitionPath.EDITAL || role.isNotBlank()), shape = RoundedCornerShape(12.dp)) { Text("Criar") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } },
    )
}

@Composable
private fun PathOption(selected: Boolean, icon: androidx.compose.ui.graphics.vector.ImageVector, title: String, subtitle: String, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(14.dp),
        color = if (selected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceContainerLow,
        border = androidx.compose.foundation.BorderStroke(if (selected) 2.dp else 1.dp, if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, null, tint = MaterialTheme.colorScheme.primary)
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                Text(subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            RadioButton(selected = selected, onClick = null)
        }
    }
}
