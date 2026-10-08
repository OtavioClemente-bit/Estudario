package br.com.estudario.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import br.com.estudario.ui.brand.Button
import br.com.estudario.ui.brand.Icon
import br.com.estudario.ui.brand.Surface

/**
 * Confirma a exclusão do material salvo de um tópico, mostrando o que sai e o que fica. As questões
 * não respondidas só saem se a pessoa marcar; as respondidas nunca saem (levariam o histórico e o XP).
 */
@Composable
fun DeleteMaterialDialog(
    topicTitle: String,
    theories: Int,
    summaries: Int,
    snippets: Int,
    unansweredQuestions: Int,
    answeredQuestions: Int,
    onDismiss: () -> Unit,
    onConfirm: (includeQuestions: Boolean) -> Unit,
) {
    var includeQuestions by remember { mutableStateOf(false) }
    Dialog(onDismissRequest = onDismiss) {
        Surface(shape = RoundedCornerShape(28.dp), color = MaterialTheme.colorScheme.surface, tonalElevation = 6.dp, modifier = Modifier.widthIn(max = 420.dp)) {
            Column(Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Box(Modifier.size(64.dp).clip(CircleShape).background(MaterialTheme.colorScheme.errorContainer), contentAlignment = Alignment.Center) {
                    Icon(Icons.Outlined.DeleteOutline, null, Modifier.size(32.dp), tint = MaterialTheme.colorScheme.error)
                }
                Text("Apagar o material?", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                Text(topicTitle, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center)

                Column(
                    Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(MaterialTheme.colorScheme.surfaceContainerHigh).padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    Text("SAI DO TÓPICO", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.error)
                    if (theories > 0) Item("Teoria${if (theories > 1) " ($theories)" else ""}, com seus destaques")
                    if (summaries > 0) Item("Resumos ($summaries)")
                    if (snippets > 0) Item("Flashcards, dicas e pegadinhas ($snippets)")
                    Item("Fontes consultadas")
                }
                if (unansweredQuestions > 0) Row(
                    Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).clickable { includeQuestions = !includeQuestions }
                        .padding(end = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Checkbox(includeQuestions, { includeQuestions = it })
                    Text("Também apagar as $unansweredQuestions ${if (unansweredQuestions == 1) "questão que você ainda não respondeu" else "questões que você ainda não respondeu"}", style = MaterialTheme.typography.bodyMedium)
                }
                Text(
                    buildString {
                        append("Continua tudo o que é seu: o tópico, se ele foi estudado, as revisões, o XP")
                        if (answeredQuestions > 0) append(" e as $answeredQuestions questões que você já respondeu, com o histórico")
                        append(". Dá para gerar o material de novo quando quiser.")
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                )
                Button(
                    onClick = { onConfirm(includeQuestions); onDismiss() },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error, contentColor = MaterialTheme.colorScheme.onError),
                    modifier = Modifier.fillMaxWidth(),
                ) { Text("Apagar material") }
                TextButton(onClick = onDismiss, modifier = Modifier.fillMaxWidth()) { Text("Cancelar") }
            }
        }
    }
}

@Composable
private fun Item(text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(Modifier.size(6.dp).clip(CircleShape).background(MaterialTheme.colorScheme.error))
        Spacer(Modifier.width(10.dp))
        Text(text, style = MaterialTheme.typography.bodyMedium)
    }
}
