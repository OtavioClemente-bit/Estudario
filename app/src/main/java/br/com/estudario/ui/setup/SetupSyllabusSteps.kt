package br.com.estudario.ui.setup

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.ExpandLess
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.material.icons.outlined.School
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.estudario.data.local.SubjectEntity
import br.com.estudario.data.local.TopicEntity
import br.com.estudario.domain.setup.InitialSetupSnapshot
import br.com.estudario.domain.setup.SubjectDifficulty

@Composable
internal fun SyllabusReviewStep(
    uiState: InitialSetupUiState,
    onContinue: () -> Unit,
    onAddSubject: (String) -> Unit,
    onAddTopic: (Long, String) -> Unit,
    onRemoveSubject: (SubjectEntity) -> Unit,
    onRemoveTopic: (TopicEntity) -> Unit,
) {
    var expandedSubjectId by rememberSaveable { mutableStateOf<Long?>(null) }
    var addingSubject by rememberSaveable { mutableStateOf(false) }
    var addingTopicTo by rememberSaveable { mutableStateOf<Long?>(null) }
    var removingSubject by remember { mutableStateOf<SubjectEntity?>(null) }
    var removingTopic by remember { mutableStateOf<TopicEntity?>(null) }
    val topicsBySubject = remember(uiState.topicEntities) { uiState.topicEntities.groupBy { it.subjectId } }

    SetupPage(
        eyebrow = "Confira antes de seguir",
        title = "Vamos conferir seu edital?",
        description = "Abra cada matéria para conferir os tópicos. Você pode adicionar o que falta ou remover o que não faz parte da sua prova.",
        icon = Icons.Outlined.CheckCircle,
        showScrollIndicator = true,
        bottom = { SetupPrimaryButton("Está certo, continuar", onContinue, enabled = uiState.subjects.isNotEmpty()) },
    ) {
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            SyllabusCount("${uiState.subjects.size} matérias")
            SyllabusCount("${uiState.topicEntities.size} tópicos")
        }
        OutlinedButton(onClick = { addingSubject = true }, modifier = Modifier.fillMaxWidth()) {
            Icon(Icons.Outlined.Add, null, Modifier.padding(end = 8.dp))
            Text("Adicionar matéria")
        }
        if (uiState.subjects.isEmpty()) {
            SetupCard { Text("Seu edital ainda não tem matérias. Adicione uma para continuar.") }
        }
        uiState.subjects.forEach { subject ->
            val topics = topicsBySubject[subject.id].orEmpty()
            val expanded = expandedSubjectId == subject.id
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth()
                        .testTag("setup_subject_${subject.id}")
                        .semantics { stateDescription = if (expanded) "Expandida" else "Recolhida" }
                        .clickable(role = Role.Button, onClickLabel = if (expanded) "Recolher tópicos" else "Ver tópicos") {
                            expandedSubjectId = if (expanded) null else subject.id
                        }.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(subject.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                        Text("${topics.size} tópicos", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
                        Text(if (expanded) "Toque para recolher" else "Toque para ver e ajustar", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Icon(if (expanded) Icons.Outlined.ExpandLess else Icons.Outlined.ExpandMore, null)
                }
                if (expanded) {
                    HorizontalDivider(Modifier.padding(horizontal = 16.dp))
                    Column(Modifier.fillMaxWidth().padding(12.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        if (topics.isEmpty()) Text("Nenhum tópico por aqui ainda. Adicione o primeiro abaixo.", Modifier.padding(4.dp), style = MaterialTheme.typography.bodyMedium)
                        val rows = remember(topics) { syllabusTopicRows(topics) }
                        rows.forEach { row ->
                            Surface(
                                modifier = Modifier.fillMaxWidth().padding(start = (row.depth.coerceAtMost(2) * 12).dp),
                                color = MaterialTheme.colorScheme.surface,
                                shape = MaterialTheme.shapes.medium,
                            ) {
                                Row(Modifier.fillMaxWidth().padding(start = 12.dp, top = 8.dp, bottom = 8.dp), verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                    Text(row.number, Modifier.padding(top = 12.dp), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
                                    Text(row.topic.title, Modifier.weight(1f).padding(vertical = 10.dp), style = MaterialTheme.typography.bodyLarge)
                                    IconButton(onClick = { removingTopic = row.topic }) {
                                        Icon(Icons.Outlined.DeleteOutline, "Remover tópico ${row.topic.title}", tint = MaterialTheme.colorScheme.error)
                                    }
                                }
                            }
                        }
                        OutlinedButton(onClick = { addingTopicTo = subject.id }, modifier = Modifier.fillMaxWidth()) {
                            Icon(Icons.Outlined.Add, null, Modifier.padding(end = 8.dp))
                            Text("Adicionar tópico")
                        }
                        TextButton(onClick = { removingSubject = subject }) {
                            Icon(Icons.Outlined.DeleteOutline, null, Modifier.padding(end = 8.dp), tint = MaterialTheme.colorScheme.error)
                            Text("Remover matéria", color = MaterialTheme.colorScheme.error)
                        }
                    }
                }
            }
        }
    }

    if (addingSubject) SyllabusNameDialog(
        title = "Adicionar matéria", label = "Nome da matéria", existingNames = uiState.subjects.map { it.name },
        onDismiss = { addingSubject = false },
        onSave = { onAddSubject(it); addingSubject = false },
    )
    addingTopicTo?.let { subjectId ->
        SyllabusNameDialog(
            title = "Adicionar tópico", label = "Nome do tópico", existingNames = topicsBySubject[subjectId].orEmpty().map { it.title },
            onDismiss = { addingTopicTo = null },
            onSave = { onAddTopic(subjectId, it); addingTopicTo = null },
        )
    }
    removingSubject?.let { subject ->
        SyllabusRemovalDialog(
            title = "Remover matéria?",
            description = "“${subject.name}” e seus tópicos e conteúdos vinculados serão removidos do edital.",
            onDismiss = { removingSubject = null },
            onConfirm = { onRemoveSubject(subject); removingSubject = null },
        )
    }
    removingTopic?.let { topic ->
        SyllabusRemovalDialog(
            title = "Remover tópico?",
            description = "“${topic.title}” e seus subtópicos e conteúdos vinculados serão removidos do edital.",
            onDismiss = { removingTopic = null },
            onConfirm = { onRemoveTopic(topic); removingTopic = null },
        )
    }
}

private data class SyllabusTopicRow(val topic: TopicEntity, val depth: Int, val number: String)

private fun syllabusTopicRows(topics: List<TopicEntity>): List<SyllabusTopicRow> = buildList {
    val ids = topics.mapTo(hashSetOf()) { it.id }
    val children = topics.groupBy { it.parentTopicId }
    val visited = hashSetOf<Long>()
    fun append(topic: TopicEntity, depth: Int, number: String) {
        if (!visited.add(topic.id)) return
        add(SyllabusTopicRow(topic, depth, number))
        children[topic.id].orEmpty().forEachIndexed { index, child -> append(child, depth + 1, "$number.${index + 1}") }
    }
    topics.filter { it.parentTopicId !in ids }.forEachIndexed { index, topic -> append(topic, 0, "${index + 1}") }
    // Mesmo um vínculo inválido não pode esconder um tópico da revisão.
    topics.filter { it.id !in visited }.forEach { append(it, 0, "•") }
}

@Composable
private fun SyllabusCount(label: String) {
    Surface(color = MaterialTheme.colorScheme.primaryContainer, shape = MaterialTheme.shapes.medium) {
        Text(label, Modifier.padding(horizontal = 14.dp, vertical = 10.dp), style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onPrimaryContainer)
    }
}

@Composable
private fun SyllabusNameDialog(title: String, label: String, existingNames: List<String>, onDismiss: () -> Unit, onSave: (String) -> Unit) {
    var value by rememberSaveable { mutableStateOf("") }
    val duplicate = existingNames.any { it.equals(value.trim(), ignoreCase = true) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            OutlinedTextField(
                value, { value = it }, Modifier.fillMaxWidth(), label = { Text(label) },
                minLines = 2, maxLines = 5, isError = duplicate,
                supportingText = { if (duplicate) Text("Este nome já foi adicionado.") },
            )
        },
        confirmButton = { TextButton(onClick = { onSave(value.trim()) }, enabled = value.isNotBlank() && !duplicate) { Text("Adicionar") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } },
    )
}

@Composable
private fun SyllabusRemovalDialog(title: String, description: String, onDismiss: () -> Unit, onConfirm: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) }, text = { Text(description) },
        confirmButton = { TextButton(onClick = onConfirm) { Text("Remover", color = MaterialTheme.colorScheme.error) } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } },
    )
}
