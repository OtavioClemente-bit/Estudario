package br.com.estudario.ui.prompt

import br.com.estudario.ui.theme.estudarioLayout
import android.content.ActivityNotFoundException
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.OpenableColumns
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AttachFile
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.ContentPaste
import androidx.compose.material.icons.outlined.ExpandLess
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.material.icons.outlined.FileOpen
import androidx.compose.material.icons.outlined.HelpOutline
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import br.com.estudario.ui.tour.TutorialVideo
import br.com.estudario.ui.tour.TutorialVideoDialog

private enum class PromptAction { COPY, SHARE }

/** Anexo escolhido para ir junto do prompt (PDF do edital, lei, apostila...). */
data class PromptAttachment(val uri: Uri, val name: String, val mimeType: String)

fun Context.attachmentFor(uri: Uri): PromptAttachment {
    val name = runCatching {
        contentResolver.query(uri, arrayOf(OpenableColumns.DISPLAY_NAME), null, null, null)?.use { cursor -> if (cursor.moveToFirst()) cursor.getString(0) else null }
    }.getOrNull() ?: "arquivo anexado"
    return PromptAttachment(uri, name, contentResolver.getType(uri) ?: "application/pdf")
}

fun copyPrompt(context: Context, prompt: String, toast: Boolean = true) {
    context.getSystemService(ClipboardManager::class.java)?.setPrimaryClip(ClipData.newPlainText("Prompt Estudário", prompt))
    if (toast) Toast.makeText(context, "Prompt copiado", Toast.LENGTH_SHORT).show()
}

/**
 * Abre a folha de compartilhamento do Android com o prompt (e o anexo, se houver) para a pessoa
 * escolher ChatGPT, Gemini, Claude, Copilot... O prompt também vai para a área de transferência,
 * porque alguns apps de IA aceitam só o arquivo e ignoram o texto compartilhado.
 */
fun sharePromptWithAi(context: Context, prompt: String, attachment: PromptAttachment? = null) {
    copyPrompt(context, prompt, toast = false)
    val intent = Intent(Intent.ACTION_SEND).apply {
        putExtra(Intent.EXTRA_TEXT, prompt)
        putExtra(Intent.EXTRA_SUBJECT, "Estudário")
        if (attachment != null) {
            type = attachment.mimeType
            putExtra(Intent.EXTRA_STREAM, attachment.uri)
            clipData = ClipData.newUri(context.contentResolver, attachment.name, attachment.uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        } else {
            type = "text/plain"
        }
    }
    try {
        context.startActivity(Intent.createChooser(intent, "Enviar para o app de IA"))
        Toast.makeText(context, "Prompt também copiado: se o app de IA não preencher sozinho, é só colar.", Toast.LENGTH_LONG).show()
    } catch (_: ActivityNotFoundException) {
        Toast.makeText(context, "Nenhum app para compartilhar. O prompt foi copiado, cole no app de IA.", Toast.LENGTH_LONG).show()
    }
}

fun readClipboardText(context: Context): String? =
    context.getSystemService(ClipboardManager::class.java)?.primaryClip?.takeIf { it.itemCount > 0 }?.getItemAt(0)?.coerceToText(context)?.toString()

/**
 * Janela cheia padrão dos geradores de prompt: opções no corpo, prévia opcional do prompt (só
 * leitura) e as ações Copiar / Compartilhar com IA, além do caminho de volta (colar a resposta ou
 * escolher o arquivo gerado).
 */
@Composable
fun PromptBuilderDialog(
    title: String,
    subtitle: String,
    steps: List<String>,
    prompt: String,
    onDismiss: () -> Unit,
    onImportText: (String) -> Unit,
    onPickFile: () -> Unit,
    attachment: PromptAttachment? = null,
    shareEnabled: Boolean = true,
    disabledReason: String? = null,
    tutorial: TutorialVideo? = null,
    shareWarning: String? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    val context = LocalContext.current
    var showPreview by remember { mutableStateOf(false) }
    var showTutorial by remember { mutableStateOf(false) }
    var pendingAction by remember { mutableStateOf<PromptAction?>(null) }
    fun runAction(action: PromptAction) {
        if (shareWarning != null) pendingAction = action
        else if (action == PromptAction.COPY) copyPrompt(context, prompt)
        else sharePromptWithAi(context, prompt, attachment)
    }
    Dialog(onDismissRequest = onDismiss, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            Column(Modifier.fillMaxSize().statusBarsPadding().navigationBarsPadding().imePadding()) {
                Row(Modifier.fillMaxWidth().padding(start = 20.dp, end = 8.dp, top = 12.dp, bottom = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f)) {
                        Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                        Text(subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    if (tutorial != null) IconButton(onClick = { showTutorial = true }) { Icon(Icons.Outlined.HelpOutline, "Ver vídeo de ajuda") }
                    IconButton(onClick = onDismiss) { Icon(Icons.Outlined.Close, "Fechar") }
                }
                Column(
                    Modifier.weight(1f).fillMaxWidth().verticalScroll(rememberScrollState()).padding(horizontal = 20.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    HowItWorks(steps)
                    content()
                    HorizontalDivider()
                    TextButton(onClick = { showPreview = !showPreview }) {
                        Icon(if (showPreview) Icons.Outlined.ExpandLess else Icons.Outlined.ExpandMore, null)
                        Spacer(Modifier.width(6.dp))
                        Text(if (showPreview) "Esconder prompt gerado" else "Ver prompt gerado (${prompt.length} caracteres)")
                    }
                    AnimatedVisibility(showPreview) {
                        ElevatedCard(colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
                            SelectionContainer {
                                Text(prompt, Modifier.padding(12.dp).heightIn(max = 420.dp).verticalScroll(rememberScrollState()), style = MaterialTheme.typography.bodySmall, fontFamily = FontFamily.Monospace)
                            }
                        }
                    }
                    ElevatedCard {
                        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text("Já tem a resposta da IA?", fontWeight = FontWeight.SemiBold)
                            Text("Baixe o arquivo gerado e abra com o Estudário, compartilhe a resposta com o app ou use uma das opções abaixo.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            // Lado a lado quando cabe; em tela estreita ou fonte grande, um botão por linha.
                            FlowRow(Modifier.fillMaxWidth(), maxItemsInEachRow = if (estudarioLayout().prefersStacking) 1 else Int.MAX_VALUE, verticalArrangement = Arrangement.spacedBy(8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                OutlinedButton(onClick = {
                                    val text = readClipboardText(context)
                                    if (text.isNullOrBlank()) Toast.makeText(context, "A área de transferência está vazia. Copie a resposta inteira da IA.", Toast.LENGTH_LONG).show()
                                    else onImportText(text)
                                }, modifier = Modifier.weight(1f)) { Icon(Icons.Outlined.ContentPaste, null, Modifier.size(18.dp)); Spacer(Modifier.width(6.dp)); Text("Colar resposta") }
                                OutlinedButton(onClick = onPickFile, modifier = Modifier.weight(1f)) { Icon(Icons.Outlined.FileOpen, null, Modifier.size(18.dp)); Spacer(Modifier.width(6.dp)); Text("Escolher arquivo") }
                            }
                        }
                    }
                    Spacer(Modifier.size(4.dp))
                }
                HorizontalDivider()
                Column(Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    if (!shareEnabled && disabledReason != null) Text(disabledReason, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.error)
                    if (attachment != null) Text("O anexo “${attachment.name}” vai junto no compartilhamento.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    // Lado a lado quando cabe; em tela estreita ou fonte grande, um botão por linha.
                    FlowRow(Modifier.fillMaxWidth(), maxItemsInEachRow = if (estudarioLayout().prefersStacking) 1 else Int.MAX_VALUE, verticalArrangement = Arrangement.spacedBy(8.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        OutlinedButton(onClick = { runAction(PromptAction.COPY) }, enabled = shareEnabled, modifier = Modifier.weight(1f)) {
                            Icon(Icons.Outlined.ContentCopy, null, Modifier.size(18.dp)); Spacer(Modifier.width(6.dp)); Text("Copiar")
                        }
                        Button(onClick = { runAction(PromptAction.SHARE) }, enabled = shareEnabled, modifier = Modifier.weight(1.6f)) {
                            Icon(Icons.Outlined.Share, null, Modifier.size(18.dp)); Spacer(Modifier.width(6.dp)); Text("Enviar para a IA")
                        }
                    }
                }
            }
        }
    }
    if (pendingAction != null && shareWarning != null) AlertDialog(
        onDismissRequest = { pendingAction = null },
        title = { Text("Gerar vários tópicos?") },
        text = { Text(shareWarning) },
        confirmButton = {
            TextButton(onClick = {
                val action = pendingAction
                pendingAction = null
                if (action == PromptAction.COPY) copyPrompt(context, prompt)
                if (action == PromptAction.SHARE) sharePromptWithAi(context, prompt, attachment)
            }) { Text("Continuar com vários") }
        },
        dismissButton = { TextButton(onClick = { pendingAction = null }) { Text("Rever seleção") } },
    )
    if (showTutorial && tutorial != null) TutorialVideoDialog(tutorial) { showTutorial = false }
}

@Composable
private fun HowItWorks(steps: List<String>) {
    ElevatedCard(colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text("Como funciona", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimaryContainer)
            steps.forEachIndexed { index, step ->
                Row {
                    Text("${index + 1}.", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimaryContainer, modifier = Modifier.width(22.dp))
                    Text(step, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onPrimaryContainer)
                }
            }
        }
    }
}

@Composable
fun OptionSection(title: String, hint: String? = null, content: @Composable ColumnScope.() -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
        if (hint != null) Text(hint, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        content()
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun <T> ChoiceChips(options: List<T>, selected: T, label: (T) -> String, onSelect: (T) -> Unit) {
    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        options.forEach { option -> FilterChip(selected = option == selected, onClick = { onSelect(option) }, label = { Text(label(option)) }) }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun <T> MultiChoiceChips(options: List<T>, selected: Set<T>, label: (T) -> String, onChange: (Set<T>) -> Unit) {
    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        options.forEach { option ->
            val isSelected = option in selected
            FilterChip(selected = isSelected, onClick = { onChange(if (isSelected) selected - option else selected + option) }, label = { Text(label(option)) })
        }
    }
}

@Composable
fun ToggleRow(title: String, subtitle: String? = null, checked: Boolean, onChange: (Boolean) -> Unit) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Column(Modifier.weight(1f).padding(end = 12.dp)) {
            Text(title)
            if (subtitle != null) Text(subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Switch(checked, onChange)
    }
}

@Composable
fun AttachmentPicker(attachment: PromptAttachment?, label: String, onPick: () -> Unit, onClear: () -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        OutlinedButton(onClick = onPick) { Icon(Icons.Outlined.AttachFile, null, Modifier.size(18.dp)); Spacer(Modifier.width(6.dp)); Text(if (attachment == null) label else "Trocar anexo") }
        if (attachment != null) {
            Text(attachment.name, Modifier.weight(1f).padding(horizontal = 8.dp), style = MaterialTheme.typography.bodySmall, maxLines = 2, overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis)
            IconButton(onClick = onClear) { Icon(Icons.Outlined.Close, "Remover anexo") }
        }
    }
}
