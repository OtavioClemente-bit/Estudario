package br.com.estudario.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CloudDone
import androidx.compose.material.icons.outlined.Restore
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import br.com.estudario.data.transfer.BackupSummary
import br.com.estudario.ui.brand.Button
import br.com.estudario.ui.brand.Icon
import br.com.estudario.ui.brand.Surface
import br.com.estudario.ui.theme.estudarioColors

/** Janela de conclusão (backup salvo, restaurado, conta conectada): selo animado, frase e resumo. */
@Composable
fun TransferDoneDialog(title: String, message: String, facts: List<Pair<String, String>>, cloud: Boolean, onClose: () -> Unit) {
    BackupDialogFrame(onDismiss = onClose) {
        val pop = remember { Animatable(0.4f) }
        LaunchedEffect(Unit) { pop.animateTo(1f, spring(dampingRatio = 0.5f, stiffness = Spring.StiffnessMediumLow)) }
        Seal(if (cloud) Icons.Outlined.CloudDone else Icons.Rounded.Check, Modifier.graphicsLayer { scaleX = pop.value; scaleY = pop.value })
        Text(title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
        Text(message, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center)
        if (facts.isNotEmpty()) Facts(facts)
        Button(onClick = onClose, modifier = Modifier.fillMaxWidth().padding(top = 6.dp)) { Text("Pronto") }
    }
}

/**
 * Confirmação antes de restaurar: mostra o que tem no backup (data e totais), nunca o nome do
 * arquivo, e deixa claro que os dados deste celular serão substituídos.
 */
@Composable
fun RestoreConfirmDialog(summary: BackupSummary?, fromCloud: Boolean, onConfirm: () -> Unit, onDismiss: () -> Unit) {
    BackupDialogFrame(onDismiss = onDismiss) {
        Seal(Icons.Outlined.Restore)
        Text(
            if (fromCloud) "Restaurar do Google Drive?" else "Restaurar este backup?",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
        )
        Text(
            if (fromCloud) "Vamos trazer o backup mais recente da sua conta do Google."
            else "Confira o que vem neste backup.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
        if (summary != null) Facts(
            buildList {
                summary.exportedAt?.let {
                    add("Feito em" to java.time.format.DateTimeFormatter.ofPattern("d 'de' MMMM 'de' yyyy, HH:mm", java.util.Locale("pt", "BR"))
                        .format(java.time.Instant.ofEpochMilli(it).atZone(java.time.ZoneId.systemDefault())))
                }
                add("Concursos" to "${summary.competitions}")
                add("Tópicos" to "%,d".format(summary.topics).replace(',', '.'))
                add("Questões" to "%,d".format(summary.questions).replace(',', '.'))
                add("Respostas registradas" to "%,d".format(summary.answers).replace(',', '.'))
            },
        )
        // Aviso do que vai acontecer, em destaque mas sem alarme.
        Row(
            Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp)).background(MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.55f)).padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Text(
                "O que está neste celular agora será substituído pelo backup. Se quiser guardar o estado atual, faça um backup antes.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onErrorContainer,
            )
        }
        Button(onClick = onConfirm, modifier = Modifier.fillMaxWidth().padding(top = 4.dp)) { Text("Restaurar agora") }
        TextButton(onClick = onDismiss, modifier = Modifier.fillMaxWidth()) { Text("Agora não") }
    }
}

@Composable
private fun BackupDialogFrame(onDismiss: () -> Unit, content: @Composable () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(28.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp,
            shadowElevation = 16.dp,
            modifier = Modifier.widthIn(max = 400.dp),
        ) {
            Column(
                Modifier.padding(horizontal = 24.dp, vertical = 26.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) { content() }
        }
    }
}

@Composable
private fun Seal(icon: ImageVector, modifier: Modifier = Modifier) {
    val brush = Brush.linearGradient(listOf(Color(0xFF5B3FD6), estudarioColors().completed))
    Box(modifier.size(76.dp).clip(CircleShape).background(brush), contentAlignment = Alignment.Center) {
        Icon(icon, null, Modifier.size(38.dp), tint = Color.White)
    }
}

@Composable
private fun Facts(facts: List<Pair<String, String>>) {
    Column(
        Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(MaterialTheme.colorScheme.surfaceContainerHigh).padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        facts.forEach { (label, value) ->
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text(label, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.weight(1f))
                Text(value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface, textAlign = TextAlign.End)
            }
        }
    }
}
