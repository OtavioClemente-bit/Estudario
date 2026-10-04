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
import br.com.estudario.ui.brand.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import br.com.estudario.ui.brand.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import br.com.estudario.ui.brand.OutlinedButton
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

/** Anexo escolhido para ir junto do prompt (PDF do edital, lei, apostila...). */
data class PromptAttachment(val uri: Uri, val name: String, val mimeType: String)

fun Context.attachmentFor(uri: Uri): PromptAttachment {
    val name = runCatching {
        contentResolver.query(uri, arrayOf(OpenableColumns.DISPLAY_NAME), null, null, null)?.use { cursor -> if (cursor.moveToFirst()) cursor.getString(0) else null }
    }.getOrNull() ?: "arquivo anexado"
    return PromptAttachment(uri, name, contentResolver.getType(uri) ?: "application/pdf")
}

fun copyPrompt(context: Context, prompt: String, toast: Boolean = true) {
    context.getSystemService(ClipboardManager::class.java)?.setPrimaryClip(ClipData.newPlainText("Pedido do Estudário", prompt))
    if (toast) Toast.makeText(context, "Pedido copiado", Toast.LENGTH_SHORT).show()
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
        context.startActivity(Intent.createChooser(intent, "Enviar para sua IA favorita"))
        Toast.makeText(context, "Pedido também copiado: se a sua IA não preencher sozinha, é só colar.", Toast.LENGTH_LONG).show()
    } catch (_: ActivityNotFoundException) {
        Toast.makeText(context, "Nenhum app para compartilhar. O pedido foi copiado: cole na sua IA.", Toast.LENGTH_LONG).show()
    }
}

fun readClipboardText(context: Context): String? =
    context.getSystemService(ClipboardManager::class.java)?.primaryClip?.takeIf { it.itemCount > 0 }?.getItemAt(0)?.coerceToText(context)?.toString()

@Composable
fun OptionSection(title: String, hint: String? = null, required: Boolean = false, content: @Composable ColumnScope.() -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(title, Modifier.weight(1f, fill = false), style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
            if (required) Text("  Obrigatório", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
        }
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
