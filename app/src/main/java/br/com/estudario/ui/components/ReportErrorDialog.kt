package br.com.estudario.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Flag
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import br.com.estudario.EstudarioApplication
import br.com.estudario.data.remote.ContentReport
import br.com.estudario.data.remote.ReportKind
import br.com.estudario.data.remote.ReportReason
import br.com.estudario.data.remote.ReportResult
import kotlinx.coroutines.launch

/** Ícone de bandeira que abre o reporte de erro do trecho. */
@Composable
fun ReportErrorButton(kind: ReportKind, excerpt: () -> String, topic: String? = null, competition: String? = null) {
    var open by remember { mutableStateOf(false) }
    IconButton(onClick = { open = true }) { Icon(Icons.Outlined.Flag, "Reportar erro") }
    if (open) ReportErrorDialog(kind, excerpt(), topic, competition, onDismiss = { open = false })
}

/**
 * "Achou um erro?": a pessoa escolhe o motivo e, se quiser, explica. O trecho vai junto para a
 * equipe corrigir. Resposta clara em todos os casos, inclusive sem login ou sem internet.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ReportErrorDialog(kind: ReportKind, excerpt: String, topic: String?, competition: String?, onDismiss: () -> Unit) {
    val client = (LocalContext.current.applicationContext as EstudarioApplication).contentReportClient
    val scope = rememberCoroutineScope()
    var reason by remember { mutableStateOf<ReportReason?>(null) }
    var comment by remember { mutableStateOf("") }
    var sending by remember { mutableStateOf(false) }
    var result by remember { mutableStateOf<ReportResult?>(null) }
    val reasons = when (kind) {
        ReportKind.QUESTION -> listOf(ReportReason.WRONG_ANSWER, ReportReason.WRONG_FACT, ReportReason.OUTDATED_LAW, ReportReason.OFF_TOPIC, ReportReason.OTHER)
        else -> listOf(ReportReason.WRONG_FACT, ReportReason.OUTDATED_LAW, ReportReason.GENERIC, ReportReason.OFF_TOPIC, ReportReason.OTHER)
    }
    if (result == ReportResult.Sent) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text("Obrigado!") },
            text = { Text("Recebemos seu aviso. A equipe vai conferir na fonte oficial e corrigir. É assim que o material fica cada vez melhor.") },
            confirmButton = { TextButton(onClick = onDismiss) { Text("Fechar") } },
        )
        return
    }
    AlertDialog(
        onDismissRequest = { if (!sending) onDismiss() },
        title = { Text("Achou um erro?") },
        text = {
            Column(Modifier.verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("Trecho:", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
                Text(excerpt, style = MaterialTheme.typography.bodySmall, maxLines = 5, overflow = TextOverflow.Ellipsis)
                Text("O que está errado?", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    reasons.forEach { value -> FilterChip(reason == value, { reason = value }, { Text(value.label) }) }
                }
                OutlinedTextField(
                    comment,
                    { comment = it.take(1_000) },
                    Modifier.fillMaxWidth(),
                    label = { Text("Qual é o certo? (opcional)") },
                    placeholder = { Text("Ex.: o prazo é de 30 dias, art. 13 §1º") },
                    minLines = 2,
                )
                when (result) {
                    ReportResult.SignedOut -> Text("Entre na sua conta (Ajustes) para enviar o aviso.", color = MaterialTheme.colorScheme.error)
                    ReportResult.TooMany -> Text("Você já enviou muitos avisos hoje. Tente amanhã.", color = MaterialTheme.colorScheme.error)
                    ReportResult.Failed -> Text("Não foi possível enviar agora. Confira a internet e tente de novo.", color = MaterialTheme.colorScheme.error)
                    else -> Unit
                }
            }
        },
        confirmButton = {
            TextButton(
                enabled = reason != null && !sending,
                onClick = {
                    sending = true
                    scope.launch {
                        result = client.send(ContentReport(kind, reason!!, excerpt, comment, competition, topic))
                        sending = false
                    }
                },
            ) { Text(if (sending) "Enviando…" else "Enviar") }
        },
        dismissButton = { TextButton(onClick = onDismiss, enabled = !sending) { Text("Cancelar") } },
    )
}
