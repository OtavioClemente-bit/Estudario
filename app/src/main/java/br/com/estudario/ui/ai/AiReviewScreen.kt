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
    onApplied: () -> Unit = {},
) {
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
            when (val content = state.content) {
                AiReviewContent.Gate -> item { AiGate(onLogin) }
                is AiReviewContent.Processing -> item { AiProcessing(content) }
                is AiReviewContent.Review -> item {
                    AiDraftEditor(
                        draft = content.draft,
                        validationError = content.validationError,
                        confirmReplacement = content.confirmReplacement,
                        onPickSource = onPickSource,
                        onDraftChange = onDraftChange,
                        onApply = onApply,
                        onConfirmReplacement = onConfirmReplacement,
                        onCancelReplacement = onCancelReplacement,
                    )
                }
                is AiReviewContent.Failure -> item { AiFailure(content, onRetry, onFallback) }
                is AiReviewContent.Applied -> item {
                    AiApplied(content.syncState)
                    if (content.syncState == RemoteSyllabusSyncState.SYNCED) onApplied()
                }
            }
        }
    }
}

@Composable
private fun AiGate(onLogin: () -> Unit) {
    Card(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Icon(Icons.Outlined.AutoAwesome, null, tint = MaterialTheme.colorScheme.primary)
            Text("Use a IA do Estudário", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Text("Vincule uma conta para utilizar recursos online, guardar seus editais e recuperar suas gerações futuramente.")
            Button(onClick = onLogin, Modifier.fillMaxWidth()) { Text("Entrar para continuar") }
        }
    }
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
    onDraftChange: (AiSyllabusDraft) -> Unit,
    onApply: () -> Unit,
    onConfirmReplacement: () -> Unit,
    onCancelReplacement: () -> Unit,
) {
    val topicCount = draft.subjects.sumOf { it.topics.sumOf { topic -> 1 + topic.children.size } }
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
    var addingSubject by rememberSaveable { mutableStateOf(false) }
    var addingTopicTo by rememberSaveable { mutableStateOf<Int?>(null) }
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
                    OutlinedTextField(
                        value = topic.name,
                        onValueChange = { value -> onDraftChange(draft.updateTopic(subjectIndex, topicIndex) { it.copy(name = value) }) },
                        modifier = Modifier.fillMaxWidth().testTag("ai_topic_name_${subjectIndex}_$topicIndex"),
                        label = { Text("Nome do tópico") },
                    )
                    TextButton(onClick = { onDraftChange(draft.removeTopic(subjectIndex, topicIndex)) }) {
                        Icon(Icons.Outlined.DeleteOutline, null, tint = MaterialTheme.colorScheme.error)
                        Text("Remover tópico ${topic.name}", color = MaterialTheme.colorScheme.error)
                    }
                }
                OutlinedButton(onClick = { addingTopicTo = subjectIndex }, Modifier.fillMaxWidth()) {
                    Icon(Icons.Outlined.Add, null); Text("Adicionar tópico")
                }
            }
        }
    }
    OutlinedButton(onClick = { addingSubject = true }, Modifier.fillMaxWidth()) {
        Icon(Icons.Outlined.Add, null); Text("Adicionar matéria")
    }
    validationError?.let { Text(it, color = MaterialTheme.colorScheme.error, modifier = Modifier.testTag("ai_validation_error")) }
    Button(onClick = onApply, Modifier.fillMaxWidth(), enabled = validationError == null) { Text("Usar este edital") }
    if (addingSubject) AddSubjectDialog(
        onDismiss = { addingSubject = false },
        onAdd = { name -> onDraftChange(draft.addSubject(name)); addingSubject = false },
    )
    addingTopicTo?.let { subjectIndex ->
        AddTopicDialog(
            onDismiss = { addingTopicTo = null },
            onAdd = { name -> onDraftChange(draft.addTopic(subjectIndex, name)); addingTopicTo = null },
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
private fun AddTopicDialog(onDismiss: () -> Unit, onAdd: (String) -> Unit) {
    var value by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Adicionar tópico") },
        text = { OutlinedTextField(value, { value = it }, label = { Text("Nome do tópico") }) },
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
private fun AiSyllabusDraft.updateTopic(subjectIndex: Int, topicIndex: Int, transform: (AiSyllabusDraftTopic) -> AiSyllabusDraftTopic): AiSyllabusDraft = updateSubject(subjectIndex) { subject -> subject.copy(topics = subject.topics.mapIndexed { i, value -> if (i == topicIndex) transform(value) else value }) }
private fun AiSyllabusDraft.removeTopic(subjectIndex: Int, topicIndex: Int): AiSyllabusDraft = updateSubject(subjectIndex) { subject -> subject.copy(topics = subject.topics.filterIndexed { i, _ -> i != topicIndex }.mapIndexed { i, topic -> topic.copy(position = i) }) }
private fun AiSyllabusDraft.addTopic(subjectIndex: Int, name: String): AiSyllabusDraft = updateSubject(subjectIndex) { subject -> subject.copy(topics = subject.topics + AiSyllabusDraftTopic(name, subject.topics.size, "ai-topic-manual-${UUID.randomUUID()}", sourcePages = listOf(1))) }
