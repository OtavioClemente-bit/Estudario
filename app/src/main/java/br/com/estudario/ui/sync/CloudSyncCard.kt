package br.com.estudario.ui.sync

import android.text.format.DateUtils
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CloudSync
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.estudario.EstudarioApplication
import br.com.estudario.data.sync.CloudSyncStatus
import br.com.estudario.data.sync.ConflictChoice
import kotlinx.coroutines.launch

/**
 * Liga a sincronização com a conta e mostra como ela está. Com ela ligada, o que é estudado no
 * celular aparece em app.estudario.com.br e vice-versa.
 */
@Composable
fun CloudSyncCard() {
    val app = LocalContext.current.applicationContext as EstudarioApplication
    val sync = app.cloudSync
    val status by sync.status.collectAsState()
    val signedIn = app.supabaseAuthRepository.currentUserId() != null
    var enabled by remember { mutableStateOf(sync.enabled) }
    var askConflict by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Outlined.CloudSync, null, Modifier.size(38.dp), tint = MaterialTheme.colorScheme.primary)
            Spacer(Modifier.width(14.dp))
            Column(Modifier.weight(1f)) {
                Text("Sincronizar com o app web", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
                Text(
                    "Seus planos, questões e progresso também em app.estudario.com.br, no computador.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Spacer(Modifier.width(8.dp))
            Switch(
                checked = enabled,
                enabled = signedIn,
                onCheckedChange = { value ->
                    enabled = value
                    sync.enabled = value
                    if (value) scope.launch { sync.sync() }
                },
            )
        }
        when {
            !signedIn -> Hint("Entre com sua conta Google no Perfil para ligar a sincronização.")
            enabled -> {
                when (val current = status) {
                    CloudSyncStatus.Running -> Row(verticalAlignment = Alignment.CenterVertically) {
                        CircularProgressIndicator(Modifier.size(16.dp), strokeWidth = 2.dp)
                        Spacer(Modifier.width(8.dp))
                        Hint("Sincronizando…")
                    }
                    is CloudSyncStatus.Conflict -> {
                        Text(
                            "O celular e a conta mudaram desde a última sincronização. Escolha qual versão manter.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.error,
                        )
                        Button(onClick = { askConflict = true }, Modifier.fillMaxWidth()) { Text("Resolver") }
                    }
                    is CloudSyncStatus.Failed -> Hint("Não sincronizou: ${current.message}")
                    CloudSyncStatus.SignedOut -> Hint("A sessão da conta expirou. Entre de novo no Perfil.")
                    else -> Hint(lastSyncText(sync.lastSyncAt))
                }
                if (status !is CloudSyncStatus.Running && status !is CloudSyncStatus.Conflict) {
                    OutlinedButton(onClick = { scope.launch { sync.sync() } }, Modifier.fillMaxWidth()) { Text("Sincronizar agora") }
                }
            }
        }
    }

    if (askConflict) {
        val conflict = status as? CloudSyncStatus.Conflict
        AlertDialog(
            onDismissRequest = { askConflict = false },
            title = { Text("Qual versão manter?") },
            text = {
                Text(
                    "Este celular e a conta" + (conflict?.remoteDevice?.let { " (último envio: $it)" } ?: "") +
                        " têm mudanças diferentes. A versão que você não escolher fica guardada como cópia de segurança.",
                )
            },
            confirmButton = {
                TextButton(onClick = { askConflict = false; scope.launch { sync.sync(ConflictChoice.KEEP_THIS_DEVICE) } }) { Text("Manter a deste celular") }
            },
            dismissButton = {
                TextButton(onClick = { askConflict = false; scope.launch { sync.sync(ConflictChoice.KEEP_CLOUD) } }) { Text("Usar a da conta") }
            },
        )
    }
}

@Composable
private fun Hint(text: String) {
    Text(text, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
}

private fun lastSyncText(at: Long): String =
    if (at <= 0L) "Ainda não sincronizou." else "Sincronizado ${DateUtils.getRelativeTimeSpanString(at, System.currentTimeMillis(), DateUtils.MINUTE_IN_MILLIS)}."
