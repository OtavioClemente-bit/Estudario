package br.com.estudario.ui.sync

import android.text.format.DateUtils
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CloudSync
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
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
import kotlinx.coroutines.launch

/**
 * Como está a sincronização com a conta. Ela é automática: com conta, o celular e o app web são um
 * só, sem botão de sincronizar nem pergunta de conflito. A chave só serve para pausar.
 */
@Composable
fun CloudSyncCard() {
    val app = LocalContext.current.applicationContext as EstudarioApplication
    val sync = app.cloudSync
    val status by sync.status.collectAsState()
    val signedIn = app.supabaseAuthRepository.currentUserId() != null
    var enabled by remember { mutableStateOf(sync.enabled) }
    val scope = rememberCoroutineScope()

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Outlined.CloudSync, null, Modifier.size(38.dp), tint = MaterialTheme.colorScheme.primary)
            Spacer(Modifier.width(14.dp))
            Column(Modifier.weight(1f)) {
                Text("Conta e app web", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
                Text(
                    "O que você estuda aqui aparece em app.estudario.com.br, e vice-versa, sozinho.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Spacer(Modifier.width(8.dp))
            Switch(
                checked = enabled && signedIn,
                enabled = signedIn,
                onCheckedChange = { value ->
                    enabled = value
                    sync.enabled = value
                    if (value) scope.launch { sync.sync() }
                },
            )
        }
        when {
            !signedIn -> Hint("Entre com sua conta Google no Perfil para usar no computador também.")
            !enabled -> Hint("Pausada. Ligue para voltar a atualizar nos dois lados.")
            else -> when (val current = status) {
                CloudSyncStatus.Running -> Row(verticalAlignment = Alignment.CenterVertically) {
                    CircularProgressIndicator(Modifier.size(16.dp), strokeWidth = 2.dp)
                    Spacer(Modifier.width(8.dp))
                    Hint("Atualizando…")
                }
                is CloudSyncStatus.Failed -> Hint("Não atualizou agora: ${current.message}")
                CloudSyncStatus.SignedOut -> Hint("A sessão da conta expirou. Entre de novo no Perfil.")
                else -> Hint(lastSyncText(sync.lastSyncAt))
            }
        }
    }
}

@Composable
private fun Hint(text: String) {
    Text(text, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
}

private fun lastSyncText(at: Long): String =
    if (at <= 0L) "Ainda não atualizou." else "Atualizado ${DateUtils.getRelativeTimeSpanString(at, System.currentTimeMillis(), DateUtils.MINUTE_IN_MILLIS)}."
