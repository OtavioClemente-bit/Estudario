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
import br.com.estudario.ui.brand.ElevatedCard
import br.com.estudario.ui.brand.Button
import br.com.estudario.ui.brand.Icon
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
    // Lugar vazio é com o Folha: ele aparece, olha em volta e diz o que fazer. Tocar nele faz a ação.
    Column(Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 20.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(12.dp)) {
        br.com.estudario.ui.assistant.Folha(112.dp, onClick = onAction)
        Surface(
            shape = androidx.compose.foundation.shape.RoundedCornerShape(18.dp),
            color = MaterialTheme.colorScheme.primaryContainer,
            border = androidx.compose.foundation.BorderStroke(2.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.35f)),
        ) {
            Text(title, Modifier.padding(horizontal = 16.dp, vertical = 10.dp), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimaryContainer, textAlign = TextAlign.Center)
        }
        Text(body, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center)
        if (actionLabel != null && onAction != null) Button(onClick = onAction, modifier = Modifier.fillMaxWidth()) { Text(actionLabel) }
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

/** Markdown de estudo (tabelas, fórmulas, listas...). Ver [StudyMarkdown]. */
@Composable
fun MarkdownText(markdown: String, modifier: Modifier = Modifier) {
    StudyMarkdown(markdown, modifier.fillMaxWidth())
}

