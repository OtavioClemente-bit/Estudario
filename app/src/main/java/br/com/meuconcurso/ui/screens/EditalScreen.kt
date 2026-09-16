package br.com.meuconcurso.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.meuconcurso.data.local.*
import br.com.meuconcurso.ui.AppViewModel
import br.com.meuconcurso.ui.components.EmptyState
import br.com.meuconcurso.ui.components.ScreenTitle
import br.com.meuconcurso.ui.components.TextInputDialog
import br.com.meuconcurso.ui.components.ConfirmDialog

@Composable
fun EditalScreen(viewModel: AppViewModel, onTopic: (Long) -> Unit) {
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

    if (addCompetition) TextInputDialog("Novo concurso", label = "Nome do concurso", onDismiss = { addCompetition = false }) { viewModel.addCompetition(it) }
    if (addSubject && selectedCompetitionId != 0L) TextInputDialog("Nova matéria", label = "Nome da matéria", onDismiss = { addSubject = false }) { viewModel.addSubject(selectedCompetitionId, it) }
    addTopicFor?.let { subject -> TextInputDialog("Novo tópico", label = "Título do tópico", onDismiss = { addTopicFor = null }) { viewModel.addTopic(subject.id, it) } }
    deleteCompetition?.let { competition -> ConfirmDialog("Excluir concurso?", "“${competition.name}” e todo o conteúdo relacionado serão removidos deste aparelho.", "Excluir", onDismiss = { deleteCompetition = null }) { viewModel.deleteCompetition(competition) } }

    LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        item {
            ScreenTitle("Edital", "Organize matérias e acompanhe seu domínio") {
                IconButton(onClick = { addCompetition = true }) { Icon(Icons.Outlined.Add, "Criar concurso") }
            }
        }
        if (competitions.isEmpty()) {
            item { EmptyState("Nenhum concurso", "Crie um perfil para começar a organizar seu edital.", "Criar concurso") { addCompetition = true } }
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
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(onClick = { viewModel.setPrimary(selectedCompetitionId) }) { Icon(Icons.Outlined.Star, null); Spacer(Modifier.width(6.dp)); Text("Tornar principal") }
                    Button(onClick = { addSubject = true }) { Icon(Icons.Outlined.Add, null); Spacer(Modifier.width(6.dp)); Text("Matéria") }
                    IconButton(onClick = { deleteCompetition = competitions.firstOrNull { it.id == selectedCompetitionId } }) { Icon(Icons.Outlined.Delete, "Excluir concurso") }
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
                    label = { Text("Somente matérias com conteúdo") },
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
                SubjectCard(
                    subject = subject,
                    topics = topics.filter { it.subjectId == subject.id },
                    expanded = subject.id in expandedSubjects,
                    onExpandedChange = { viewModel.setEditalSubjectExpanded(subject.id, it) },
                    viewModel = viewModel,
                    onTopic = onTopic,
                    onAddTopic = { addTopicFor = subject },
                )
            }
        }
    }
}

@Composable
private fun SubjectCard(subject: SubjectEntity, topics: List<TopicEntity>, expanded: Boolean, onExpandedChange: (Boolean) -> Unit, viewModel: AppViewModel, onTopic: (Long) -> Unit, onAddTopic: () -> Unit) {
    var menu by remember { mutableStateOf(false) }
    ElevatedCard {
        Column {
            Row(Modifier.fillMaxWidth().padding(start = 16.dp, top = 8.dp, bottom = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { onExpandedChange(!expanded) }) { Icon(if (expanded) Icons.Outlined.ExpandLess else Icons.Outlined.ExpandMore, if (expanded) "Recolher matéria" else "Expandir matéria") }
                Column(Modifier.weight(1f)) {
                    Text(subject.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    val done = topics.count { it.status != TopicStatus.NAO_ESTUDADO }
                    Text("$done de ${topics.size} tópicos iniciados", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Box {
                    IconButton(onClick = { menu = true }) { Icon(Icons.Outlined.MoreVert, "Opções") }
                    DropdownMenu(expanded = menu, onDismissRequest = { menu = false }) {
                        DropdownMenuItem(text = { Text("Adicionar tópico") }, leadingIcon = { Icon(Icons.Outlined.Add, null) }, onClick = { menu = false; onAddTopic() })
                        DropdownMenuItem(text = { Text("Excluir matéria") }, leadingIcon = { Icon(Icons.Outlined.Delete, null) }, onClick = { menu = false; viewModel.deleteSubject(subject) })
                    }
                }
            }
            if (expanded) {
                if (topics.isEmpty()) TextButton(onClick = onAddTopic, modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) { Text("+ Adicionar tópico") }
                topics.filter { it.parentTopicId == null }.sortedBy { it.position }.forEach { topic ->
                    TopicTreeRows(topic, topics, 0, viewModel, onTopic)
                }
            }
        }
    }
}

@Composable
private fun TopicTreeRows(topic: TopicEntity, allTopics: List<TopicEntity>, depth: Int, viewModel: AppViewModel, onTopic: (Long) -> Unit) {
    TopicRow(topic, depth, viewModel, onClick = { onTopic(topic.id) })
    HorizontalDivider(Modifier.padding(start = (16 + depth * 20).dp, end = 16.dp), color = MaterialTheme.colorScheme.outlineVariant)
    allTopics.filter { it.parentTopicId == topic.id }.sortedBy { it.position }.forEach { child ->
        TopicTreeRows(child, allTopics, depth + 1, viewModel, onTopic)
    }
}

@Composable
private fun TopicRow(topic: TopicEntity, depth: Int, viewModel: AppViewModel, onClick: () -> Unit) {
    var menu by remember { mutableStateOf(false) }
    ListItem(
        headlineContent = { Text(topic.title, fontWeight = FontWeight.Medium) },
        supportingContent = {
            Text("${topic.status.displayName()} • ${topic.contentOriginType.displayName()}")
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
                    DropdownMenuItem(text = { Text("Marcar estudado") }, onClick = { menu = false; viewModel.markStudied(topic) })
                    DropdownMenuItem(text = { Text("Adicionar à fila") }, onClick = { menu = false; viewModel.enqueue(topic.id) })
                    DropdownMenuItem(text = { Text("Excluir") }, onClick = { menu = false; viewModel.deleteTopic(topic) })
                }
            }
        },
        modifier = Modifier.fillMaxWidth().padding(start = (depth * 20).dp).clickable(onClick = onClick),
    )
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
