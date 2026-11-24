package br.com.estudario.ui.setup

import br.com.estudario.ui.components.AlertDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.material3.CardDefaults
import br.com.estudario.ui.brand.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import br.com.estudario.ui.brand.Icon
import br.com.estudario.ui.brand.IconButton
import br.com.estudario.ui.brand.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import br.com.estudario.ui.brand.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import br.com.estudario.ui.brand.Surface
import androidx.compose.material3.Text
import br.com.estudario.ui.brand.TextButton
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
    // Começa com a primeira matéria aberta, para mostrar como é por dentro.
    var expandedIds by rememberSaveable { mutableStateOf(listOfNotNull(uiState.subjects.firstOrNull()?.id)) }
    val palette = br.com.estudario.ui.theme.estudarioColors().subjectPalette
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
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Box(Modifier.weight(1f)) { SyllabusCount("${uiState.subjects.size} matérias · ${uiState.topicEntities.size} tópicos") }
            val allOpen = uiState.subjects.isNotEmpty() && uiState.subjects.all { it.id in expandedIds }
            if (uiState.subjects.isNotEmpty()) TextButton(onClick = { expandedIds = if (allOpen) emptyList() else uiState.subjects.map { it.id } }) {
                Text(if (allOpen) "Fechar todas" else "Abrir todas")
            }
        }
        OutlinedButton(onClick = { addingSubject = true }, modifier = Modifier.fillMaxWidth()) {
            Icon(Icons.Outlined.Add, null, Modifier.padding(end = 8.dp))
            Text("Adicionar matéria")
        }
        if (uiState.subjects.isEmpty()) {
            SetupCard { Text("Você ainda não tem matérias. Adicione uma para continuar.") }
        }
        uiState.subjects.forEach { subject ->
            val topics = topicsBySubject[subject.id].orEmpty()
            val expanded = subject.id in expandedIds
            val accent = remember(subject.name, palette) { br.com.estudario.ui.components.subjectAccentColor(subject.name, palette) }
            // Peso na prova, quando o edital traz o quadro de questões ("15 questões · 25% da nota").
            val weight = remember(subject.assessedPriorityEvidenceJson) {
                val json = subject.assessedPriorityEvidenceJson
                br.com.estudario.text.TopicDisplay.weightLabel(br.com.estudario.domain.ExamWeights.questionsOfJson(json), br.com.estudario.domain.ExamWeights.descriptionOfJson(json))
            }
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
            ) {
              Row(Modifier.height(IntrinsicSize.Min)) {
                // Faixa fina na cor da matéria.
                Box(Modifier.width(4.dp).fillMaxHeight().background(accent))
                Column(Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth()
                        .testTag("setup_subject_${subject.id}")
                        .semantics { stateDescription = if (expanded) "Expandida" else "Recolhida" }
                        .clickable(role = Role.Button, onClickLabel = if (expanded) "Recolher tópicos" else "Ver tópicos") {
                            expandedIds = if (expanded) expandedIds - subject.id else expandedIds + subject.id
                        }.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    br.com.estudario.ui.brand.SubjectArtTile(br.com.estudario.domain.catalog.SubjectArt.iconFor(subject.name), accent, 44.dp)
                    Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(subject.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                        FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            SyllabusChip(if (topics.size == 1) "1 tópico" else "${topics.size} tópicos", MaterialTheme.colorScheme.surfaceContainerHighest)
                            weight?.let { SyllabusChip(it, accent.copy(alpha = 0.18f)) }
                        }
                    }
                    Surface(color = MaterialTheme.colorScheme.surfaceContainerHighest, shape = CircleShape) {
                        Icon(if (expanded) Icons.Outlined.ExpandLess else Icons.Outlined.ExpandMore, null, Modifier.padding(6.dp))
                    }
                }
                if (expanded) {
                    HorizontalDivider(Modifier.padding(horizontal = 14.dp))
                    Column(Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 8.dp)) {
                        if (topics.isEmpty()) Text("Nenhum tópico por aqui ainda. Adicione o primeiro abaixo.", Modifier.padding(8.dp), style = MaterialTheme.typography.bodyMedium)
                        val rows = remember(topics) { syllabusTopicRows(topics) }
                        rows.forEachIndexed { index, row ->
                            if (index > 0) HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                            SyllabusTopicLine(row, accent, striped = index % 2 == 1) { removingTopic = row.topic }
                        }
                        Spacer(Modifier.height(8.dp))
                        OutlinedButton(onClick = { addingTopicTo = subject.id }, modifier = Modifier.fillMaxWidth()) {
                            Icon(Icons.Outlined.Add, null, Modifier.padding(end = 8.dp))
                            Text("Adicionar tópico")
                        }
                        TextButton(kind = br.com.estudario.ui.brand.NoteKind.Danger, onClick = { removingSubject = subject }) {
                            Icon(Icons.Outlined.DeleteOutline, null, Modifier.padding(end = 8.dp), tint = MaterialTheme.colorScheme.error)
                            Text("Remover matéria", color = MaterialTheme.colorScheme.error)
                        }
                    }
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

/** Um tópico da matéria: número, texto limpo para leitura (o guardado não muda) e remover. Longo: duas linhas e "ver tudo". */
@Composable
private fun SyllabusTopicLine(row: SyllabusTopicRow, accent: androidx.compose.ui.graphics.Color, striped: Boolean, onRemove: () -> Unit) {
    val text = remember(row.topic.title) { br.com.estudario.text.TopicDisplay.clean(row.topic.title) }
    var full by rememberSaveable(row.topic.id) { mutableStateOf(false) }
    var overflows by remember(text) { mutableStateOf(false) }
    val sub = row.depth > 0
    Row(
        Modifier.fillMaxWidth()
            .background(if (striped) accent.copy(alpha = 0.05f) else androidx.compose.ui.graphics.Color.Transparent, MaterialTheme.shapes.small)
            .padding(start = (8 + row.depth.coerceAtMost(3) * 16).dp, top = 4.dp, bottom = 4.dp),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Text(row.number, Modifier.padding(top = 12.dp).widthIn(min = 24.dp), style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold, color = accent)
        Column(Modifier.weight(1f).padding(vertical = 10.dp)) {
            Text(
                text,
                style = if (sub) MaterialTheme.typography.bodyMedium else MaterialTheme.typography.bodyLarge,
                color = if (sub) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface,
                maxLines = if (full) Int.MAX_VALUE else 2,
                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
                onTextLayout = { if (!full && it.hasVisualOverflow) overflows = true },
            )
            if (overflows || full) Text(
                if (full) "ver menos" else "ver tudo",
                Modifier.padding(top = 4.dp).clickable(role = Role.Button) { full = !full }.padding(vertical = 4.dp),
                style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold,
            )
        }
        IconButton(onClick = onRemove) {
            Icon(Icons.Outlined.DeleteOutline, "Remover tópico $text", tint = MaterialTheme.colorScheme.error)
        }
    }
}

@Composable
private fun SyllabusChip(label: String, background: androidx.compose.ui.graphics.Color) {
    Surface(color = background, shape = CircleShape) {
        Text(label, Modifier.padding(horizontal = 10.dp, vertical = 4.dp), style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
    }
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
        confirmButton = { TextButton(kind = br.com.estudario.ui.brand.NoteKind.Danger, onClick = onConfirm) { Text("Remover", color = MaterialTheme.colorScheme.error) } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } },
    )
}
