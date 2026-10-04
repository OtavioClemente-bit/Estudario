package br.com.estudario.ui.prompt

import br.com.estudario.ui.components.AlertDialog
import android.widget.Toast
import br.com.estudario.ui.theme.EstudarioShapes
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.background
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.ContentPaste
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.ExpandLess
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.material.icons.outlined.FileOpen
import androidx.compose.material.icons.outlined.HelpOutline
import androidx.compose.material.icons.outlined.Share
import br.com.estudario.ui.brand.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import br.com.estudario.ui.brand.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import br.com.estudario.ui.brand.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import br.com.estudario.ui.theme.estudarioLayout
import br.com.estudario.ui.tour.TutorialVideo
import br.com.estudario.ui.tour.TutorialVideoDialog

/**
 * Uma etapa do assistente. [error] nulo libera o botão Continuar; com texto, o botão fica
 * desabilitado e o motivo aparece acima dele, para a pessoa saber o que falta.
 */
class WizardStep(
    val title: String,
    val hint: String? = null,
    val error: String? = null,
    /** Pergunta que o assistente faz nesta etapa; sem ela, vale o título. */
    val question: String = title,
    /** Resposta curta que fica no histórico da conversa depois que a pessoa avança. */
    val answer: String? = null,
    val content: @Composable ColumnScope.() -> Unit,
)

/** Linha do resumo final. Tocar nela volta para [step] (índice na lista de etapas). */
data class WizardSummaryItem(val label: String, val value: String, val step: Int)

/** Geração dentro do app, pela IA do Estudário. Ausente = só o caminho de copiar o pedido. */
class ServerGenerationOption(
    val description: String,
    val enabled: Boolean,
    val disabledReason: String? = null,
    val onGenerate: () -> Unit,
)

private enum class ExternalAction { COPY, SHARE }

/**
 * Assistente em tela cheia dos geradores de IA: uma etapa por vez, com progresso, validação e um
 * resumo no fim. Só na revisão aparecem as formas de gerar (IA do Estudário e/ou copiar o pedido
 * para outra IA), para ninguém disparar a geração antes de configurar o que quer.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun GenerationWizard(
    title: String,
    subtitle: String,
    steps: List<WizardStep>,
    summary: List<WizardSummaryItem>,
    prompt: String,
    onDismiss: () -> Unit,
    onImportText: (String) -> Unit,
    onPickFile: () -> Unit,
    returnFileLabel: String,
    attachment: PromptAttachment? = null,
    server: ServerGenerationOption? = null,
    tutorial: TutorialVideo? = null,
    externalWarning: String? = null,
) {
    val context = LocalContext.current
    var index by rememberSaveable { mutableIntStateOf(0) }
    val reviewIndex = steps.size
    val onReview = index >= reviewIndex
    val current = steps.getOrNull(index)
    var showTutorial by remember { mutableStateOf(false) }
    var pendingAction by remember { mutableStateOf<ExternalAction?>(null) }
    val firstInvalid = steps.indexOfFirst { it.error != null }

    fun runExternal(action: ExternalAction) {
        if (externalWarning != null) pendingAction = action
        else if (action == ExternalAction.COPY) copyPrompt(context, prompt)
        else sharePromptWithAi(context, prompt, attachment)
    }

    Dialog(onDismissRequest = onDismiss, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        BackHandler { if (index > 0) index-- else onDismiss() }
        Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            Column(Modifier.fillMaxSize().statusBarsPadding().navigationBarsPadding().imePadding()) {
                Row(Modifier.fillMaxWidth().padding(start = 20.dp, end = 8.dp, top = 12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f)) {
                        Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                        Text(subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 2, overflow = TextOverflow.Ellipsis)
                    }
                    if (tutorial != null) IconButton(onClick = { showTutorial = true }) { Icon(Icons.Outlined.HelpOutline, "Ver vídeo de ajuda") }
                    IconButton(onClick = onDismiss) { Icon(Icons.Outlined.Close, "Fechar") }
                }
                Column(Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 10.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        if (onReview) "Revisão · tudo pronto para gerar" else "Etapa ${index + 1} de ${steps.size} · ${current?.title}",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary,
                    )
                    LinearProgressIndicator(
                        progress = { (index + 1f) / (steps.size + 1f) },
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
                HorizontalDivider()
                val scroll = rememberScrollState()
                var currentTop by remember { mutableIntStateOf(0) }
                // A cada nova pergunta, a conversa rola até ela (e não até o fim, que em etapas
                // longas esconderia a própria pergunta).
                LaunchedEffect(index, currentTop) { scroll.animateScrollTo((currentTop - 24).coerceAtLeast(0)) }
                Column(
                    Modifier.weight(1f).fillMaxWidth().verticalScroll(scroll).padding(horizontal = 16.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    // Histórico: o que já foi perguntado e respondido. Tocar na resposta volta
                    // para aquela pergunta.
                    steps.take(index.coerceAtMost(steps.size)).forEachIndexed { position, answered ->
                        AssistantBubble(answered.question)
                        UserBubble(answered.answer ?: "Ok", onClick = { index = position })
                    }
                    AnimatedContent(
                        targetState = index,
                        transitionSpec = { fadeIn() togetherWith fadeOut() },
                        modifier = Modifier.fillMaxWidth().onGloballyPositioned { currentTop = it.positionInParent().y.toInt() },
                        label = "wizard-step",
                    ) { page ->
                        Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                            val step = steps.getOrNull(page)
                            if (step != null) {
                                AssistantBubble(step.question, step.hint)
                                Surface(shape = EstudarioShapes.panel, color = MaterialTheme.colorScheme.surface, tonalElevation = 1.dp, modifier = Modifier.fillMaxWidth()) {
                                    Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) { step.content(this) }
                                }
                            } else {
                                ReviewPage(
                                    summary = summary,
                                    firstInvalid = firstInvalid,
                                    invalidReason = steps.getOrNull(firstInvalid)?.error,
                                    onEdit = { index = it },
                                    prompt = prompt,
                                    server = server,
                                    attachment = attachment,
                                    onExternal = ::runExternal,
                                    onImportText = onImportText,
                                    onPickFile = onPickFile,
                                    returnFileLabel = returnFileLabel,
                                )
                            }
                        }
                    }
                }
                if (!onReview) {
                    HorizontalDivider()
                    Column(Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        current?.error?.let { Text(it, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.error) }
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            if (index > 0) OutlinedButton(onClick = { index-- }, modifier = Modifier.weight(1f)) {
                                Icon(Icons.AutoMirrored.Outlined.ArrowBack, null, Modifier.size(18.dp)); Spacer(Modifier.width(6.dp)); Text("Voltar")
                            }
                            Button(onClick = { index++ }, enabled = current?.error == null, modifier = Modifier.weight(1.6f)) {
                                Text(if (index == steps.lastIndex) "Revisar pedido" else "Continuar")
                                Spacer(Modifier.width(6.dp)); Icon(Icons.AutoMirrored.Outlined.ArrowForward, null, Modifier.size(18.dp))
                            }
                        }
                    }
                } else {
                    HorizontalDivider()
                    Row(Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 6.dp)) {
                        TextButton(onClick = { index = steps.lastIndex }) {
                            Icon(Icons.AutoMirrored.Outlined.ArrowBack, null, Modifier.size(18.dp)); Spacer(Modifier.width(6.dp)); Text("Voltar às etapas")
                        }
                    }
                }
            }
        }
    }
    if (pendingAction != null && externalWarning != null) AlertDialog(
        onDismissRequest = { pendingAction = null },
        title = { Text("Gerar vários tópicos?") },
        text = { Text(externalWarning) },
        confirmButton = {
            TextButton(onClick = {
                val action = pendingAction
                pendingAction = null
                if (action == ExternalAction.COPY) copyPrompt(context, prompt)
                if (action == ExternalAction.SHARE) sharePromptWithAi(context, prompt, attachment)
            }) { Text("Continuar com vários") }
        },
        dismissButton = { TextButton(onClick = { pendingAction = null }) { Text("Rever seleção") } },
    )
    if (showTutorial && tutorial != null) TutorialVideoDialog(tutorial) { showTutorial = false }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ReviewPage(
    summary: List<WizardSummaryItem>,
    firstInvalid: Int,
    invalidReason: String?,
    onEdit: (Int) -> Unit,
    prompt: String,
    server: ServerGenerationOption?,
    attachment: PromptAttachment?,
    onExternal: (ExternalAction) -> Unit,
    onImportText: (String) -> Unit,
    onPickFile: () -> Unit,
    returnFileLabel: String,
) {
    val context = LocalContext.current
    var showPreview by remember { mutableStateOf(false) }
    val ready = firstInvalid < 0
    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
    Text("Confira seu pedido", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
    Text("Toque em um item para ajustar.", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
    ElevatedCard(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(vertical = 4.dp)) {
            summary.forEachIndexed { position, item ->
                if (position > 0) HorizontalDivider(Modifier.padding(horizontal = 14.dp))
                Row(
                    Modifier.fillMaxWidth().clickable { onEdit(item.step) }.padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column(Modifier.weight(1f)) {
                        Text(item.label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(item.value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
                    }
                    Icon(Icons.Outlined.Edit, "Editar", Modifier.size(18.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
    if (!ready) {
        Surface(color = MaterialTheme.colorScheme.errorContainer, shape = MaterialTheme.shapes.medium) {
            Row(Modifier.fillMaxWidth().padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                Text(invalidReason ?: "Há uma etapa incompleta.", Modifier.weight(1f), color = MaterialTheme.colorScheme.onErrorContainer, style = MaterialTheme.typography.bodyMedium)
                TextButton(onClick = { onEdit(firstInvalid) }) { Text("Corrigir") }
            }
        }
    }

    Text("Como gerar", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
    if (server != null) {
        ElevatedCard(
            Modifier.fillMaxWidth(),
            colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        ) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Tocar no Folha é o mesmo que "Gerar com o Estudário".
                    br.com.estudario.ui.assistant.FolhaTalking(
                        52.dp, server.description,
                        onClick = if (ready && server.enabled) server.onGenerate else null,
                    )
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f)) {
                        Text("Estudário", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimaryContainer)
                        Text("Recomendado · gera aqui no app", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onPrimaryContainer)
                    }
                }
                Text(server.description, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onPrimaryContainer)
                if (!server.enabled && server.disabledReason != null) {
                    Text(server.disabledReason, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.error)
                }
                Button(onClick = server.onGenerate, enabled = ready && server.enabled, modifier = Modifier.fillMaxWidth()) {
                    br.com.estudario.ui.assistant.Folha(26.dp); Spacer(Modifier.width(8.dp)); Text("Gerar com o Estudário")
                }
            }
        }
    }

    }
}

/** Fala do assistente: avatar do Estudário e a pergunta, com a explicação logo abaixo. */
@Composable
private fun AssistantBubble(text: String, hint: String? = null) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top) {
        Box(Modifier.size(32.dp).clip(CircleShape).background(MaterialTheme.colorScheme.primary), contentAlignment = Alignment.Center) {
            br.com.estudario.ui.assistant.Folha(26.dp)
        }
        Spacer(Modifier.width(10.dp))
        Surface(
            shape = RoundedCornerShape(topStart = 4.dp, topEnd = 18.dp, bottomEnd = 18.dp, bottomStart = 18.dp),
            color = MaterialTheme.colorScheme.primaryContainer,
            modifier = Modifier.weight(1f, fill = false),
        ) {
            Column(Modifier.padding(horizontal = 14.dp, vertical = 10.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(text, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onPrimaryContainer)
                if (hint != null) Text(hint, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.85f))
            }
        }
    }
}

/** Resposta da pessoa no histórico. Tocar volta para a pergunta e permite mudar. */
@Composable
private fun UserBubble(text: String, onClick: () -> Unit) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
        Surface(
            onClick = onClick,
            shape = RoundedCornerShape(topStart = 18.dp, topEnd = 4.dp, bottomEnd = 18.dp, bottomStart = 18.dp),
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(start = 48.dp),
        ) {
            Row(Modifier.padding(horizontal = 14.dp, vertical = 10.dp), verticalAlignment = Alignment.CenterVertically) {
                Text(text, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.weight(1f, fill = false))
                Spacer(Modifier.width(8.dp))
                Icon(Icons.Outlined.Edit, "Mudar resposta", Modifier.size(14.dp), tint = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f))
            }
        }
    }
}
