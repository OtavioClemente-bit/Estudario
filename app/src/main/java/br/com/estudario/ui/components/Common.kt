package br.com.estudario.ui.components

import br.com.estudario.ui.theme.EstudarioLayout
import br.com.estudario.ui.theme.estudarioLayout
import android.os.SystemClock
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Inbox
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalUriHandler
import kotlinx.coroutines.delay

/**
 * Título de tela com ações opcionais à direita. Em tela estreita ou com fonte grande
 * ([EstudarioLayout.prefersStacking]) as ações descem para uma linha própria, alinhadas à direita,
 * para o título não ficar espremido, a menos que [stackActionsWhenNarrow] seja falso (telas em que o
 * título é uma barra fixa e cada linha conta, como o Plano).
 */
@Composable
fun ScreenTitle(
    title: String,
    subtitle: String? = null,
    stackActionsWhenNarrow: Boolean = true,
    action: (@Composable () -> Unit)? = null,
) {
    val texts: @Composable (Modifier) -> Unit = { modifier ->
        Column(modifier) {
            // Uma linha cada, com reticências: com fonte grande ou tela estreita o título dividia o
            // espaço com os botões de ação e quebrava letra por letra, empurrando a tela para baixo.
            Text(title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
            if (subtitle != null) Text(subtitle, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = if (action == null) 2 else 1, overflow = TextOverflow.Ellipsis)
        }
    }
    if (action != null && stackActionsWhenNarrow && estudarioLayout().prefersStacking) {
        Column(Modifier.fillMaxWidth()) {
            texts(Modifier.fillMaxWidth())
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) { action() }
        }
    } else {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            texts(Modifier.weight(1f))
            action?.invoke()
        }
    }
}

@Composable
fun MetricCard(title: String, value: String, supporting: String, color: Color = MaterialTheme.colorScheme.primary, modifier: Modifier = Modifier) {
    ElevatedCard(modifier) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(title, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(value, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = color)
            Text(supporting, style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
fun EmptyState(title: String, body: String, actionLabel: String? = null, onAction: (() -> Unit)? = null) {
    Column(Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 24.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Icon(Icons.Outlined.Inbox, null, Modifier.size(44.dp), tint = MaterialTheme.colorScheme.primary)
        Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold, textAlign = TextAlign.Center)
        Text(body, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center)
        if (actionLabel != null && onAction != null) Button(onClick = onAction) { Text(actionLabel) }
    }
}

@Composable
fun TextInputDialog(title: String, initial: String = "", label: String = "Nome", onDismiss: () -> Unit, onConfirm: (String) -> Unit) {
    var value by remember { mutableStateOf(initial) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = { OutlinedTextField(value = value, onValueChange = { value = it }, label = { Text(label) }, singleLine = true) },
        confirmButton = { TextButton(enabled = value.isNotBlank(), onClick = { onConfirm(value); onDismiss() }) { Text("Salvar") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } },
    )
}

@Composable
fun ConfirmDialog(
    title: String,
    message: String,
    confirmLabel: String = "Confirmar",
    confirmDelayMillis: Long = 0L,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    val openedAt = remember { SystemClock.elapsedRealtime() }
    var now by remember { mutableLongStateOf(openedAt) }
    LaunchedEffect(confirmDelayMillis) {
        if (confirmDelayMillis > 0L) {
            while (!ConfirmationDelay.isReady(now - openedAt, confirmDelayMillis)) {
                now = SystemClock.elapsedRealtime()
                delay(100L)
            }
            now = SystemClock.elapsedRealtime()
        }
    }
    val ready = confirmDelayMillis <= 0L || ConfirmationDelay.isReady(now - openedAt, confirmDelayMillis)
    val remainingSeconds = ConfirmationDelay.remainingSeconds(now - openedAt, confirmDelayMillis)
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = { Text(message) },
        confirmButton = {
            TextButton(enabled = ready, onClick = { onConfirm(); onDismiss() }) {
                Text(if (ready) confirmLabel else "$confirmLabel ($remainingSeconds)")
            }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } },
    )
}

@Composable
fun MarkdownText(markdown: String, modifier: Modifier = Modifier) {
    val uriHandler = LocalUriHandler.current
    val linkColor = MaterialTheme.colorScheme.primary
    Column(modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        markdown.lines().forEach { raw ->
            val line = raw.trimEnd()
            when {
                line.startsWith("### ") -> Text(inlineMarkdown(line.removePrefix("### "), linkColor) { uriHandler.openUri(it) }, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                line.startsWith("## ") -> Text(inlineMarkdown(line.removePrefix("## "), linkColor) { uriHandler.openUri(it) }, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                line.startsWith("# ") -> Text(inlineMarkdown(line.removePrefix("# "), linkColor) { uriHandler.openUri(it) }, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                line.startsWith("- ") || line.startsWith("* ") -> Row { Text("•  ", color = MaterialTheme.colorScheme.primary); Text(inlineMarkdown(line.drop(2), linkColor) { uriHandler.openUri(it) }, Modifier.weight(1f)) }
                line.startsWith("> ") -> Text(inlineMarkdown(line.drop(2), linkColor) { uriHandler.openUri(it) }, Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.primaryContainer, RoundedCornerShape(8.dp)).padding(12.dp), color = MaterialTheme.colorScheme.onPrimaryContainer)
                line.startsWith("```") -> Unit
                line.startsWith("    ") -> Text(line.trimStart(), fontFamily = FontFamily.Monospace, fontSize = 13.sp, modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(6.dp)).padding(8.dp))
                line.isBlank() -> Spacer(Modifier.height(2.dp))
                else -> Text(inlineMarkdown(line, linkColor) { uriHandler.openUri(it) }, style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}

private fun inlineMarkdown(text: String, linkColor: androidx.compose.ui.graphics.Color, openUri: (String) -> Unit) = buildAnnotatedString {
    parseInlineLinks(text).forEach { token ->
        when (token) {
            is InlineMarkdownToken.Text -> appendBoldText(token.value)
            is InlineMarkdownToken.Link -> {
                val link = LinkAnnotation.Url(
                    token.url,
                    TextLinkStyles(SpanStyle(color = linkColor, textDecoration = TextDecoration.Underline)),
                ) { annotation -> openUri((annotation as LinkAnnotation.Url).url) }
                withLink(link) { append(token.label) }
            }
        }
    }
}

private fun androidx.compose.ui.text.AnnotatedString.Builder.appendBoldText(text: String) {
    var index = 0
    while (index < text.length) {
        val boldStart = text.indexOf("**", index)
        if (boldStart < 0) { append(text.substring(index)); break }
        append(text.substring(index, boldStart))
        val boldEnd = text.indexOf("**", boldStart + 2)
        if (boldEnd < 0) { append(text.substring(boldStart)); break }
        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) { append(text.substring(boldStart + 2, boldEnd)) }
        index = boldEnd + 2
    }
}
