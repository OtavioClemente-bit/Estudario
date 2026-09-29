package br.com.estudario.ui.ai

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountTree
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.CloudDone
import androidx.compose.material.icons.outlined.CloudSync
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material.icons.outlined.FactCheck
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.PictureAsPdf
import androidx.compose.material.icons.outlined.SubdirectoryArrowRight
import androidx.compose.material.icons.outlined.SwapHoriz
import androidx.compose.material.icons.outlined.WarningAmber
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import br.com.estudario.data.local.RemoteSyllabusSyncState
import br.com.estudario.domain.ai.AiSyllabusDraft
import br.com.estudario.domain.ai.AiSyllabusDraftSubject
import br.com.estudario.domain.ai.AiSyllabusDraftTopic
import br.com.estudario.ui.components.EstudarioBookLoader
import br.com.estudario.ui.components.EstudarioProcessView
import br.com.estudario.ui.theme.estudarioColors
import java.util.UUID

/** As etapas narradas enquanto a IA lê o edital. São o caminho real do trabalho, na ordem. */
internal val SyllabusAnalysisStages = listOf(
    "Enviando o PDF com segurança",
    "Lendo as páginas do edital",
    "Localizando o conteúdo programático",
    "Separando matérias e tópicos",
    "Conferindo a estrutura com o original",
    "Preparando tudo para a sua revisão",
)

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
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text("IA do Estudário", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
                        BetaPill()
                    }
                },
                navigationIcon = { IconButton(onClick = onClose) { Icon(Icons.Outlined.ArrowBack, "Voltar") } },
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
    ) { padding ->
        val content = state.content
        if (content is AiReviewContent.Processing) {
            // A análise ocupa a tela inteira: é o momento em que a pessoa só acompanha.
            Column(
                Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(horizontal = 24.dp, vertical = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                TargetChip(state.targetTitle)
                Spacer(Modifier.height(8.dp))
                EstudarioProcessView(
                    title = "Analisando seu edital",
                    stages = SyllabusAnalysisStages,
                    footer = {
                        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(
                                "Costuma levar de 1 a 3 minutos. Se a conexão cair, a análise pode ser retomada.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center,
                            )
                            Text(
                                "Código da análise: ${content.jobId}",
                                style = MaterialTheme.typography.labelSmall,
                                fontFamily = FontFamily.Monospace,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                            )
                        }
                    },
                )
            }
            return@Scaffold
        }
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 4.dp, bottom = 28.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            item { TargetHeader(state.targetTitle) }
            sourceError?.let { message ->
                item { InlineError(message, Modifier.testTag("ai_source_error")) }
            }
            when (content) {
                AiReviewContent.Gate -> item { AiGate(state.access, onLogin, onPickSource, onFallback) }
                is AiReviewContent.Processing -> Unit
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

// ------------------------------------------------------------------------------------ cabeçalho

@Composable
private fun TargetHeader(title: String) {
    Surface(shape = RoundedCornerShape(20.dp), color = MaterialTheme.colorScheme.surfaceContainerLow) {
        Row(Modifier.fillMaxWidth().padding(14.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Box(
                Modifier.size(42.dp).clip(RoundedCornerShape(12.dp)).background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center,
            ) { Icon(Icons.Outlined.Description, null, tint = MaterialTheme.colorScheme.onPrimaryContainer) }
            Column(Modifier.weight(1f)) {
                Text("EDITAL SELECIONADO", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                Text(title, Modifier.testTag("ai_selected_target"), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold, maxLines = 2, overflow = TextOverflow.Ellipsis)
                Text("O resultado vale só para este edital.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@Composable
internal fun TargetChip(title: String) {
    Row(
        Modifier.clip(RoundedCornerShape(50)).background(MaterialTheme.colorScheme.surfaceContainerLow).padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Icon(Icons.Outlined.Description, null, Modifier.size(15.dp), tint = MaterialTheme.colorScheme.primary)
        Text(title, Modifier.testTag("ai_selected_target"), style = MaterialTheme.typography.labelLarge, maxLines = 1, overflow = TextOverflow.Ellipsis)
    }
}

@Composable
internal fun InlineError(message: String, modifier: Modifier = Modifier) {
    Row(
        modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp)).background(MaterialTheme.colorScheme.errorContainer).padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(Icons.Outlined.ErrorOutline, null, tint = MaterialTheme.colorScheme.onErrorContainer)
        Text(message, color = MaterialTheme.colorScheme.onErrorContainer, style = MaterialTheme.typography.bodyMedium)
    }
}

// ------------------------------------------------------------------------------------ entrada

@Composable
private fun AiGate(
    access: AiReviewAccessState,
    onLogin: () -> Unit,
    onPickSource: () -> Unit,
    onFallback: () -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        // Apresentação: o que a IA faz, em três linhas, sobre o degradê da IA.
        Box(Modifier.fillMaxWidth().clip(RoundedCornerShape(26.dp)).background(aiGradient())) {
            Column(Modifier.padding(22.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Box(
                    Modifier.size(48.dp).clip(RoundedCornerShape(15.dp)).background(Color.White.copy(alpha = 0.18f)),
                    contentAlignment = Alignment.Center,
                ) { Icon(Icons.Outlined.AutoAwesome, null, tint = Color.White) }
                Text("Use a IA do Estudário", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = Color.White)
                Text(
                    "Envie o PDF oficial e receba o edital organizado em matérias e tópicos, pronto para revisar.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.88f),
                )
                Benefit(Icons.Outlined.PictureAsPdf, "Lê o PDF oficial, página por página")
                Benefit(Icons.Outlined.AccountTree, "Separa matérias, tópicos e subtópicos")
                Benefit(Icons.Outlined.FactCheck, "Você confere e ajusta tudo antes de salvar")
            }
        }

        Surface(shape = RoundedCornerShape(24.dp), color = MaterialTheme.colorScheme.surfaceContainerLow) {
            Column(Modifier.fillMaxWidth().padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                when (access.kind) {
                    AiReviewAccessKind.LOADING -> {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                            EstudarioBookLoader(size = 40.dp)
                            Column {
                                Text("Verificando acesso à IA", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                                Text("Conferindo sua conta e sua cota…", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                    AiReviewAccessKind.UNAUTHENTICATED -> {
                        GateStatus(Icons.Outlined.Lock, "Entre para continuar", "Vincule uma conta para guardar seus editais e recuperar suas gerações depois.", StatusTone(MaterialTheme.colorScheme.tertiaryContainer, MaterialTheme.colorScheme.onTertiaryContainer))
                        PrimaryAction("Entrar para continuar", onLogin)
                    }
                    AiReviewAccessKind.DENIED -> {
                        GateStatus(Icons.Outlined.Lock, "Acesso beta indisponível", access.reasonCode.toUserMessage(), StatusTone(MaterialTheme.colorScheme.surfaceContainerHighest, MaterialTheme.colorScheme.onSurfaceVariant))
                        OutlinedButton(onClick = onFallback, modifier = Modifier.fillMaxWidth().height(52.dp), shape = RoundedCornerShape(16.dp)) {
                            Text("Importar .estudo ou montar manualmente")
                        }
                    }
                    AiReviewAccessKind.READY -> {
                        GateStatus(Icons.Outlined.CheckCircle, "Pronto para analisar o edital", "Escolha o PDF oficial. Prefira a versão com o conteúdo programático completo.", StatusTone(MaterialTheme.colorScheme.secondaryContainer, estudarioColors().completed))
                        PrimaryAction("Selecionar PDF do edital", onPickSource, Icons.Outlined.PictureAsPdf)
                    }
                }
                access.access?.let { value ->
                    val display = value.toDisplay()
                    StatusPill(display.quotaCopy, if (display.canUse) StatusTone(MaterialTheme.colorScheme.secondaryContainer, estudarioColors().completed) else StatusTone(MaterialTheme.colorScheme.surfaceContainerHighest, MaterialTheme.colorScheme.onSurfaceVariant))
                }
            }
        }
    }
}

@Composable
internal fun Benefit(icon: ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        Box(Modifier.size(30.dp).clip(CircleShape).background(Color.White.copy(alpha = 0.16f)), contentAlignment = Alignment.Center) {
            Icon(icon, null, Modifier.size(17.dp), tint = Color.White)
        }
        Text(text, style = MaterialTheme.typography.bodyMedium, color = Color.White)
    }
}

@Composable
internal fun GateStatus(icon: ImageVector, title: String, description: String, tone: StatusTone) {
    Row(horizontalArrangement = Arrangement.spacedBy(14.dp), verticalAlignment = Alignment.Top) {
        Box(Modifier.size(40.dp).clip(CircleShape).background(tone.container), contentAlignment = Alignment.Center) {
            Icon(icon, null, Modifier.size(21.dp), tint = tone.content)
        }
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Text(description, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
internal fun PrimaryAction(label: String, onClick: () -> Unit, icon: ImageVector? = null) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().height(54.dp),
        shape = RoundedCornerShape(16.dp),
    ) {
        if (icon != null) { Icon(icon, null, Modifier.size(20.dp)); Spacer(Modifier.width(10.dp)) }
        Text(label, style = MaterialTheme.typography.titleSmall)
    }
}

private fun String?.toUserMessage(): String = when (this) {
    "BETA_ACCESS_REQUIRED", "BETA_DISABLED" -> "Sua conta ainda não tem acesso à beta fechada."
    "FEATURE_DISABLED" -> "A geração de edital está temporariamente desativada."
    "QUOTA_EXHAUSTED" -> "A cota de geração desta conta foi atingida."
    "CONFIGURATION_CLOSED", "ACCESS_UNAVAILABLE" -> "O acesso online está fechado nesta configuração."
    else -> "A conta não pode usar a geração de edital agora."
}

// ------------------------------------------------------------------------------------ revisão

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
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(Icons.Outlined.AutoAwesome, null, Modifier.size(18.dp), tint = MaterialTheme.colorScheme.primary)
                Text("PROPOSTA DA IA", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
            }
            Text("Confira antes de usar", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.SemiBold)
            Text("${draft.subjects.size} matéria${if (draft.subjects.size == 1) "" else "s"} · $topicCount tópicos", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Row(Modifier.fillMaxWidth().height(IntrinsicSize.Min), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            StatTile("${draft.subjects.size}", "matérias", MaterialTheme.colorScheme.primary, Modifier.weight(1f))
            StatTile("$topicCount", "tópicos", estudarioColors().completed, Modifier.weight(1f))
            StatTile("${draft.warnings.size}", "avisos", if (draft.warnings.isEmpty()) MaterialTheme.colorScheme.onSurfaceVariant else estudarioColors().attention, Modifier.weight(1f))
        }
        TextButton(onClick = onPickSource) {
            Icon(Icons.Outlined.SwapHoriz, null, Modifier.size(18.dp)); Spacer(Modifier.width(6.dp)); Text("Trocar PDF de origem")
        }
        if (draft.warnings.isNotEmpty()) {
            Surface(shape = RoundedCornerShape(20.dp), color = MaterialTheme.colorScheme.tertiaryContainer) {
                Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(Icons.Outlined.WarningAmber, null, tint = MaterialTheme.colorScheme.onTertiaryContainer)
                        Text("Confira estes pontos", fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onTertiaryContainer)
                    }
                    draft.warnings.forEach { warning ->
                        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            Text("${warning.message} Páginas: ${warning.sourcePages.joinToString(", ")}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onTertiaryContainer)
                        }
                    }
                }
            }
        }
        var addingTopicTo by remember { mutableStateOf<Pair<Int, List<Int>>?>(null) }
        val palette = estudarioColors().subjectPalette
        draft.subjects.forEachIndexed { subjectIndex, subject ->
            SubjectCard(
                subjectIndex = subjectIndex,
                subject = subject,
                accent = palette[subjectIndex % palette.size],
                draft = draft,
                onDraftChange = onDraftChange,
                onAddTopic = { path -> addingTopicTo = subjectIndex to path },
            )
        }
        OutlinedButton(
            onClick = onAddSubject,
            modifier = Modifier.fillMaxWidth().height(52.dp).testTag("ai_add_subject_button"),
            shape = RoundedCornerShape(16.dp),
        ) { Icon(Icons.Outlined.Add, null); Spacer(Modifier.width(8.dp)); Text("Adicionar matéria") }
        validationError?.let { InlineError(it, Modifier.testTag("ai_validation_error")) }
        Button(
            onClick = onApply,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            enabled = validationError == null,
            shape = RoundedCornerShape(18.dp),
        ) { Icon(Icons.Outlined.CheckCircle, null); Spacer(Modifier.width(10.dp)); Text("Usar este edital", style = MaterialTheme.typography.titleSmall) }
        addingTopicTo?.let { (subjectIndex, parentPath) ->
            AddTopicDialog(
                child = parentPath.isNotEmpty(),
                onDismiss = { addingTopicTo = null },
                onAdd = { name -> onDraftChange(draft.addTopicAt(subjectIndex, parentPath, name)); addingTopicTo = null },
            )
        }
    }
    if (confirmReplacement) AlertDialog(
        onDismissRequest = onCancelReplacement,
        icon = { Icon(Icons.Outlined.SwapHoriz, null) },
        title = { Text("Substituir conteúdo do edital?") },
        text = { Text("Este edital já possui conteúdo. A substituição remove a árvore atual antes de aplicar a proposta revisada.") },
        confirmButton = { TextButton(onClick = onConfirmReplacement) { Text("Substituir e usar este edital") } },
        dismissButton = { TextButton(onClick = onCancelReplacement) { Text("Cancelar") } },
    )
}

@Composable
private fun StatTile(value: String, label: String, color: Color, modifier: Modifier = Modifier) {
    Surface(modifier.fillMaxHeight(), shape = RoundedCornerShape(18.dp), color = MaterialTheme.colorScheme.surfaceContainerLow) {
        Column(Modifier.padding(horizontal = 14.dp, vertical = 12.dp)) {
            Text(value, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = color)
            Text(label, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun SubjectCard(
    subjectIndex: Int,
    subject: AiSyllabusDraftSubject,
    accent: Color,
    draft: AiSyllabusDraft,
    onDraftChange: (AiSyllabusDraft) -> Unit,
    onAddTopic: (List<Int>) -> Unit,
) {
    Surface(shape = RoundedCornerShape(22.dp), color = MaterialTheme.colorScheme.surfaceContainerLow) {
        Row(Modifier.fillMaxWidth().height(IntrinsicSize.Min)) {
            // Faixa com a cor da matéria, a mesma paleta controlada do resto do app.
            Box(Modifier.width(5.dp).fillMaxHeight().background(accent))
            Column(Modifier.weight(1f).padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Box(
                        Modifier.size(30.dp).clip(RoundedCornerShape(9.dp)).background(accent.copy(alpha = 0.16f)),
                        contentAlignment = Alignment.Center,
                    ) { Text("${subjectIndex + 1}", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold, color = accent) }
                    OutlinedTextField(
                        value = subject.name,
                        onValueChange = { value -> onDraftChange(draft.updateSubject(subjectIndex) { it.copy(name = value) }) },
                        modifier = Modifier.weight(1f).testTag("ai_subject_name_$subjectIndex"),
                        label = { Text("Nome da matéria") },
                        textStyle = MaterialTheme.typography.titleSmall,
                        shape = RoundedCornerShape(14.dp),
                        colors = fieldColors(),
                    )
                    IconButton(
                        onClick = { onDraftChange(draft.removeSubject(subjectIndex)) },
                        modifier = Modifier.testTag("ai_remove_subject_$subjectIndex"),
                    ) { Icon(Icons.Outlined.DeleteOutline, "Remover matéria ${subject.name}", tint = MaterialTheme.colorScheme.error) }
                }
                subject.topics.forEachIndexed { topicIndex, topic ->
                    TopicEditor(
                        subjectIndex = subjectIndex,
                        path = listOf(topicIndex),
                        topic = topic,
                        accent = accent,
                        onDraftChange = onDraftChange,
                        draft = draft,
                        onAddChild = onAddTopic,
                    )
                }
                TextButton(onClick = { onAddTopic(emptyList()) }) {
                    Icon(Icons.Outlined.Add, null, Modifier.size(18.dp)); Spacer(Modifier.width(6.dp)); Text("Adicionar tópico")
                }
            }
        }
    }
}

@Composable
private fun TopicEditor(
    subjectIndex: Int,
    path: List<Int>,
    topic: AiSyllabusDraftTopic,
    accent: Color,
    draft: AiSyllabusDraft,
    onDraftChange: (AiSyllabusDraft) -> Unit,
    onAddChild: (List<Int>) -> Unit,
) {
    val tagPath = path.joinToString("_")
    val depth = path.size
    Row(Modifier.fillMaxWidth().height(IntrinsicSize.Min).padding(start = ((depth - 1) * 14).dp)) {
        // Guia vertical da árvore: mostra a que tópico cada subtópico pertence.
        if (depth > 1) Box(Modifier.padding(end = 10.dp).width(2.dp).fillMaxHeight().clip(RoundedCornerShape(1.dp)).background(accent.copy(alpha = 0.35f)))
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = topic.name,
                    onValueChange = { value -> onDraftChange(draft.updateTopicAt(subjectIndex, path) { it.copy(name = value) }) },
                    modifier = Modifier.weight(1f).testTag("ai_topic_name_${subjectIndex}_$tagPath"),
                    label = { Text(if (depth == 1) "Nome do tópico" else "Nome do subtópico") },
                    textStyle = MaterialTheme.typography.bodyMedium,
                    shape = RoundedCornerShape(12.dp),
                    colors = fieldColors(),
                )
                IconButton(
                    onClick = { onDraftChange(draft.removeTopicAt(subjectIndex, path)) },
                    modifier = Modifier.testTag("ai_remove_topic_${subjectIndex}_$tagPath"),
                ) { Icon(Icons.Outlined.DeleteOutline, "Remover", tint = MaterialTheme.colorScheme.error) }
            }
            TextButton(onClick = { onAddChild(path) }, contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp), modifier = Modifier.heightIn(min = 32.dp)) {
                Icon(Icons.Outlined.SubdirectoryArrowRight, null, Modifier.size(16.dp)); Spacer(Modifier.width(4.dp))
                Text("Adicionar subtópico", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Medium)
            }
            topic.children.forEachIndexed { index, child ->
                TopicEditor(subjectIndex, path + index, child, accent, draft, onDraftChange, onAddChild)
            }
        }
    }
}

@Composable
private fun fieldColors() = OutlinedTextFieldDefaults.colors(
    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
    focusedContainerColor = MaterialTheme.colorScheme.surface,
    unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
)

@Composable
private fun AddSubjectDialog(onDismiss: () -> Unit, onAdd: (String) -> Unit) {
    var value by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Adicionar matéria") },
        text = { OutlinedTextField(value, { value = it }, modifier = Modifier.testTag("ai_add_subject_name"), label = { Text("Nome da matéria") }, shape = RoundedCornerShape(14.dp)) },
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
        text = { OutlinedTextField(value, { value = it }, label = { Text(if (child) "Nome do subtópico" else "Nome do tópico") }, shape = RoundedCornerShape(14.dp)) },
        confirmButton = { TextButton(onClick = { onAdd(value.trim()) }, enabled = value.isNotBlank()) { Text("Adicionar") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } },
    )
}

// ------------------------------------------------------------------------------------ fim

@Composable
private fun AiFailure(content: AiReviewContent.Failure, onRetry: () -> Unit, onFallback: () -> Unit) {
    Surface(shape = RoundedCornerShape(24.dp), color = MaterialTheme.colorScheme.surfaceContainerLow) {
        Column(Modifier.fillMaxWidth().padding(22.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Box(Modifier.size(64.dp).clip(CircleShape).background(MaterialTheme.colorScheme.errorContainer), contentAlignment = Alignment.Center) {
                Icon(Icons.Outlined.ErrorOutline, null, Modifier.size(32.dp), tint = MaterialTheme.colorScheme.onErrorContainer)
            }
            Text("Não deu certo desta vez", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
            Text(content.message, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center)
            Spacer(Modifier.height(4.dp))
            if (content.canRetry) PrimaryAction("Tentar novamente", onRetry)
            OutlinedButton(onClick = onFallback, modifier = Modifier.fillMaxWidth().height(52.dp), shape = RoundedCornerShape(16.dp)) {
                Text("Importar .estudo ou montar manualmente")
            }
        }
    }
}

@Composable
private fun AiApplied(syncState: RemoteSyllabusSyncState) {
    val (message, icon) = when (syncState) {
        RemoteSyllabusSyncState.PENDING -> "Salvo neste dispositivo; sincronização pendente." to Icons.Outlined.CloudSync
        RemoteSyllabusSyncState.SYNCED -> "Salvo na sua conta." to Icons.Outlined.CloudDone
        RemoteSyllabusSyncState.FAILED -> "Salvo neste dispositivo; a sincronização falhou e será tentada novamente." to Icons.Outlined.CloudSync
    }
    Surface(shape = RoundedCornerShape(24.dp), color = MaterialTheme.colorScheme.surfaceContainerLow) {
        Column(Modifier.fillMaxWidth().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Box(Modifier.size(72.dp).clip(CircleShape).background(MaterialTheme.colorScheme.secondaryContainer), contentAlignment = Alignment.Center) {
                Icon(Icons.Outlined.CheckCircle, null, Modifier.size(38.dp), tint = estudarioColors().completed)
            }
            Text("Edital pronto", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(icon, null, Modifier.size(18.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(message, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.testTag("ai_sync_status"))
            }
        }
    }
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
