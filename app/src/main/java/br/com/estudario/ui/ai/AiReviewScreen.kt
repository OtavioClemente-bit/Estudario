package br.com.estudario.ui.ai

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.WarningAmber
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.estudario.data.local.RemoteSyllabusSyncState
import br.com.estudario.domain.ai.AiSyllabusDraft
import br.com.estudario.domain.ai.AiSyllabusDraftSubject
import br.com.estudario.domain.ai.AiSyllabusDraftTopic
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiReviewScreen(
    state: AiReviewUiState,
    onLogin: () -> Unit = {},
    onPickSource: () -> Unit = {},
    onDraftChange: (AiSyllabusDraft) -> Unit = {},
    onApply: () -> Unit = {},
    onConfirmReplacement: () -> Unit = {},
    onCancelReplacement: () -> Unit = {},
    onRetry: () -> Unit = {},
    onFallback: () -> Unit = {},
    onClose: () -> Unit = {},
    sourceError: String? = null,
    onLocalApplied: () -> Unit = {},
    onSyncAck: () -> Unit = {},
) {
    var addingSubject by remember { mutableStateOf(false) }
    var localAppliedNotified by remember(state.targetSyllabusId) { mutableStateOf(false) }
    var syncAckNotified by remember(state.targetSyllabusId) { mutableStateOf(false) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("IA do Estudário · Beta") },
                navigationIcon = { TextButton(onClick = onClose) { Text("Voltar") } },
            )
        },
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            item {
                Text("Edital selecionado: ${state.targetTitle}", Modifier.testTag("ai_selected_target"), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                Text("O resultado será aplicado somente a este edital.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            sourceError?.let { message ->
                item { Text(message, color = MaterialTheme.colorScheme.error, modifier = Modifier.testTag("ai_source_error")) }
            }
            when (val content = state.content) {
                AiReviewContent.Gate -> item { AiGate(state.access, onLogin, onPickSource, onFallback) }
                is AiReviewContent.Processing -> item { AiProcessing(content) }
                is AiReviewContent.Review -> item {
                    AiDraftEditor(
                        draft = content.draft,
                        validationError = content.validationError,
                        confirmReplacement = content.confirmReplacement,
                        onPickSource = onPickSource,
                        onAddSubject = { addingSubject = true },
                        onDraftChange = onDraftChange,
                        onApply = onApply,
                        onConfirmReplacement = onConfirmReplacement,
                        onCancelReplacement = onCancelReplacement,
                    )
                }
                is AiReviewContent.Failure -> item { AiFailure(content, onRetry, onFallback) }
                is AiReviewContent.Applied -> item {
                    AiApplied(content.syncState)
                    LaunchedEffect(content.syncState) {
                        if (!localAppliedNotified) {
                            localAppliedNotified = true
                            onLocalApplied()
                        }
                        if (content.syncState == RemoteSyllabusSyncState.SYNCED && !syncAckNotified) {
                            syncAckNotified = true
                            onSyncAck()
                        }
                    }
                }
            }
        }
        val review = state.content as? AiReviewContent.Review
        if (addingSubject && review != null) AddSubjectDialog(
            onDismiss = { addingSubject = false },
            onAdd = { name -> onDraftChange(review.draft.addSubject(name)); addingSubject = false },
        )
    }
}

@Composable
private fun AiGate(
    access: AiReviewAccessState,
    onLogin: () -> Unit,
    onPickSource: () -> Unit,
    onFallback: () -> Unit,
) {
    Card(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Icon(Icons.Outlined.AutoAwesome, null, tint = MaterialTheme.colorScheme.primary)
            Text("Use a IA do Estudário", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            when (access.kind) {
                AiReviewAccessKind.LOADING -> {
                    Text("Verificando acesso à IA", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                    CircularProgressIndicator()
                }
                AiReviewAccessKind.UNAUTHENTICATED -> {
                    Text("Entre para continuar", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                    Text("Vincule uma conta para guardar seus editais e recuperar suas gerações futuramente.")
                    Button(onClick = onLogin, Modifier.fillMaxWidth()) { Text("Entrar para continuar") }
                }
                AiReviewAccessKind.DENIED -> {
                    Text("Acesso beta indisponível", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                    Text(access.reasonCode.toUserMessage())
                    Button(onClick = onFallback, Modifier.fillMaxWidth()) { Text("Importar .estudo ou montar manualmente") }
                }
                AiReviewAccessKind.READY -> {
                    Text("Pronto para analisar o edital", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                    Text("Selecione o PDF do edital para iniciar uma análise recuperável.")
                    Button(onClick = onPickSource, Modifier.fillMaxWidth()) { Text("Selecionar PDF do edital") }
                }
            }
            access.access?.let { Text(it.toDisplay().quotaCopy) }
        }
    }
}

private fun String?.toUserMessage(): String = when (this) {
    "BETA_ACCESS_REQUIRED", "BETA_DISABLED" -> "Sua conta ainda não tem acesso à beta fechada."
    "FEATURE_DISABLED" -> "A geração de edital está temporariamente desativada."
    "QUOTA_EXHAUSTED" -> "A cota de geração desta conta foi atingida."
    "CONFIGURATION_CLOSED", "ACCESS_UNAVAILABLE" -> "O acesso online está fechado nesta configuração."
    else -> "A conta não pode usar a geração de edital agora."
}

@Composable
private fun AiProcessing(content: AiReviewContent.Processing) {
    Card(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("PROCESSING", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
            Text("Aguardando a análise do edital", style = MaterialTheme.typography.titleLarge)
            LinearProgressIndicator(Modifier.fillMaxWidth())
            Text("jobId: ${content.jobId}", style = MaterialTheme.typography.bodySmall)
            Text("A tentativa pode ser retomada com a mesma chave após um timeout.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun AiDraftEditor(
    draft: AiSyllabusDraft,
    validationError: String?,
    confirmReplacement: Boolean,
    onPickSource: () -> Unit,
    onAddSubject: () -> Unit,
    onDraftChange: (AiSyllabusDraft) -> Unit,
    onApply: () -> Unit,
    onConfirmReplacement: () -> Unit,
    onCancelReplacement: () -> Unit,
) {
    val topicCount = draft.totalTopicCount()
    Text("${draft.subjects.size} matéria${if (draft.subjects.size == 1) "" else "s"} · $topicCount tópicos", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
    OutlinedButton(onClick = onPickSource, Modifier.fillMaxWidth()) { Text("Trocar PDF de origem") }
    if (draft.warnings.isNotEmpty()) {
        Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("Avisos de interpretação", fontWeight = FontWeight.SemiBold)
    draft.warnings.forEach { warning ->
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(Icons.Outlined.WarningAmber, null, tint = MaterialTheme.colorScheme.error)
                        Text("${warning.message} Páginas: ${warning.sourcePages.joinToString(", ")}", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
    var addingTopicTo by remember { mutableStateOf<Pair<Int, List<Int>>?>(null) }
    draft.subjects.forEachIndexed { subjectIndex, subject ->
        Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = subject.name,
                    onValueChange = { value -> onDraftChange(draft.updateSubject(subjectIndex) { it.copy(name = value) }) },
                    modifier = Modifier.fillMaxWidth().testTag("ai_subject_name_$subjectIndex"),
                    label = { Text("Nome da matéria") },
                )
                TextButton(onClick = { onDraftChange(draft.removeSubject(subjectIndex)) }, modifier = Modifier.testTag("ai_remove_subject_$subjectIndex")) {
                    Icon(Icons.Outlined.DeleteOutline, null, tint = MaterialTheme.colorScheme.error)
                    Text("Remover matéria ${subject.name}", color = MaterialTheme.colorScheme.error)
                }
                subject.topics.forEachIndexed { topicIndex, topic ->
                    TopicEditor(
                        subjectIndex = subjectIndex,
                        path = listOf(topicIndex),
                        topic = topic,
                        onDraftChange = onDraftChange,
                        draft = draft,
                        onAddChild = { path -> addingTopicTo = subjectIndex to path },
                    )
                }
                OutlinedButton(onClick = { addingTopicTo = subjectIndex to emptyList() }, Modifier.fillMaxWidth()) {
                    Icon(Icons.Outlined.Add, null); Text("Adicionar tópico")
                }
            }
        }
    }
    OutlinedButton(onClick = onAddSubject, Modifier.fillMaxWidth().testTag("ai_add_subject_button")) {
        Icon(Icons.Outlined.Add, null); Text("Adicionar matéria")
    }
    validationError?.let { Text(it, color = MaterialTheme.colorScheme.error, modifier = Modifier.testTag("ai_validation_error")) }
    Button(onClick = onApply, Modifier.fillMaxWidth(), enabled = validationError == null) { Text("Usar este edital") }
    addingTopicTo?.let { (subjectIndex, parentPath) ->
        AddTopicDialog(
            child = parentPath.isNotEmpty(),
            onDismiss = { addingTopicTo = null },
            onAdd = { name -> onDraftChange(draft.addTopicAt(subjectIndex, parentPath, name)); addingTopicTo = null },
        )
    }
    if (confirmReplacement) AlertDialog(
        onDismissRequest = onCancelReplacement,
        title = { Text("Substituir conteúdo do edital?") },
        text = { Text("Este edital já possui conteúdo. A substituição remove a árvore atual antes de aplicar a proposta revisada.") },
        confirmButton = { TextButton(onClick = onConfirmReplacement) { Text("Substituir e usar este edital") } },
        dismissButton = { TextButton(onClick = onCancelReplacement) { Text("Cancelar") } },
    )
}

@Composable
private fun TopicEditor(
    subjectIndex: Int,
    path: List<Int>,
    topic: AiSyllabusDraftTopic,
    draft: AiSyllabusDraft,
    onDraftChange: (AiSyllabusDraft) -> Unit,
    onAddChild: (List<Int>) -> Unit,
) {
    val tagPath = path.joinToString("_")
    Column(Modifier.fillMaxWidth().padding(start = (path.size * 12).dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
        OutlinedTextField(
            value = topic.name,
            onValueChange = { value -> onDraftChange(draft.updateTopicAt(subjectIndex, path) { it.copy(name = value) }) },
            modifier = Modifier.fillMaxWidth().testTag("ai_topic_name_${subjectIndex}_$tagPath"),
            label = { Text(if (path.size == 1) "Nome do tópico" else "Nome do subtópico") },
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            TextButton(onClick = { onDraftChange(draft.removeTopicAt(subjectIndex, path)) }, modifier = Modifier.testTag("ai_remove_topic_${subjectIndex}_$tagPath")) {
                Icon(Icons.Outlined.DeleteOutline, null, tint = MaterialTheme.colorScheme.error)
                Text("Remover")
            }
            OutlinedButton(onClick = { onAddChild(path) }) { Text("Adicionar subtópico") }
        }
        topic.children.forEachIndexed { index, child ->
            TopicEditor(subjectIndex, path + index, child, draft, onDraftChange, onAddChild)
        }
    }
}

@Composable
private fun AddSubjectDialog(onDismiss: () -> Unit, onAdd: (String) -> Unit) {
    var value by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Adicionar matéria") },
        text = { OutlinedTextField(value, { value = it }, modifier = Modifier.testTag("ai_add_subject_name"), label = { Text("Nome da matéria") }) },
        confirmButton = { TextButton(onClick = { onAdd(value.trim()) }, enabled = value.isNotBlank()) { Text("Adicionar") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } },
    )
}

@Composable
private fun AddTopicDialog(child: Boolean, onDismiss: () -> Unit, onAdd: (String) -> Unit) {
    var value by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (child) "Adicionar subtópico" else "Adicionar tópico") },
        text = { OutlinedTextField(value, { value = it }, label = { Text(if (child) "Nome do subtópico" else "Nome do tópico") }) },
        confirmButton = { TextButton(onClick = { onAdd(value.trim()) }, enabled = value.isNotBlank()) { Text("Adicionar") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } },
    )
}

@Composable
private fun AiFailure(content: AiReviewContent.Failure, onRetry: () -> Unit, onFallback: () -> Unit) {
    Card(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(content.message, color = MaterialTheme.colorScheme.error)
            if (content.canRetry) OutlinedButton(onClick = onRetry, Modifier.fillMaxWidth()) { Text("Tentar novamente") }
            Button(onClick = onFallback, Modifier.fillMaxWidth()) { Text("Importar .estudo ou montar manualmente") }
        }
    }
}

@Composable
private fun AiApplied(syncState: RemoteSyllabusSyncState) {
    val message = when (syncState) {
        RemoteSyllabusSyncState.PENDING -> "Salvo neste dispositivo; sincronização pendente."
        RemoteSyllabusSyncState.SYNCED -> "Salvo na sua conta."
        RemoteSyllabusSyncState.FAILED -> "Salvo neste dispositivo; a sincronização falhou e será tentada novamente."
    }
    Text(message, style = MaterialTheme.typography.titleMedium, modifier = Modifier.testTag("ai_sync_status"))
}

private fun AiSyllabusDraft.updateSubject(index: Int, transform: (AiSyllabusDraftSubject) -> AiSyllabusDraftSubject): AiSyllabusDraft = copy(subjects = subjects.mapIndexed { i, value -> if (i == index) transform(value) else value })
private fun AiSyllabusDraft.removeSubject(index: Int): AiSyllabusDraft = copy(subjects = subjects.filterIndexed { i, _ -> i != index }.mapIndexed { i, subject -> subject.copy(position = i) })
private fun AiSyllabusDraft.addSubject(name: String): AiSyllabusDraft = copy(subjects = subjects + AiSyllabusDraftSubject(name, subjects.size, br.com.estudario.data.ai.AiPriority.NORMAL, emptyList(), "ai-subject-manual-${UUID.randomUUID()}", listOf(1)))
private fun AiSyllabusDraft.updateTopicAt(subjectIndex: Int, path: List<Int>, transform: (AiSyllabusDraftTopic) -> AiSyllabusDraftTopic): AiSyllabusDraft = updateSubject(subjectIndex) { subject ->
    subject.copy(topics = subject.topics.updateAt(path, transform))
}
private fun AiSyllabusDraft.removeTopicAt(subjectIndex: Int, path: List<Int>): AiSyllabusDraft = updateSubject(subjectIndex) { subject ->
    subject.copy(topics = subject.topics.removeAt(path))
}
private fun AiSyllabusDraft.addTopicAt(subjectIndex: Int, parentPath: List<Int>, name: String): AiSyllabusDraft = updateSubject(subjectIndex) { subject ->
    val newTopic = AiSyllabusDraftTopic(name, 0, "ai-topic-manual-${UUID.randomUUID()}", sourcePages = listOf(1))
    if (parentPath.isEmpty()) subject.copy(topics = (subject.topics + newTopic).reindexTopics())
    else subject.copy(topics = subject.topics.updateAt(parentPath) { it.copy(children = (it.children + newTopic).reindexTopics()) })
}

private fun List<AiSyllabusDraftTopic>.updateAt(path: List<Int>, transform: (AiSyllabusDraftTopic) -> AiSyllabusDraftTopic): List<AiSyllabusDraftTopic> {
    val index = path.firstOrNull() ?: return this
    return mapIndexed { currentIndex, topic ->
        if (currentIndex != index) topic
        else if (path.size == 1) transform(topic)
        else topic.copy(children = topic.children.updateAt(path.drop(1), transform))
    }
}

private fun List<AiSyllabusDraftTopic>.removeAt(path: List<Int>): List<AiSyllabusDraftTopic> {
    val index = path.firstOrNull() ?: return this
    return if (path.size == 1) filterIndexed { currentIndex, _ -> currentIndex != index }.reindexTopics()
    else mapIndexed { currentIndex, topic -> if (currentIndex == index) topic.copy(children = topic.children.removeAt(path.drop(1))) else topic }
}

private fun List<AiSyllabusDraftTopic>.reindexTopics(): List<AiSyllabusDraftTopic> = mapIndexed { index, topic -> topic.copy(position = index) }
