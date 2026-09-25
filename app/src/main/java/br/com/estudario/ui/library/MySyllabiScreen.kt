package br.com.estudario.ui.library

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import br.com.estudario.data.local.RemoteSyllabusSyncState

@Composable
fun MySyllabiScreen(viewModel: MySyllabiViewModel) {
    val state by viewModel.state.collectAsState()
    var confirmLocalRemoval by remember { mutableStateOf<Long?>(null) }
    var confirmRemoteDeletion by remember { mutableStateOf<String?>(null) }

    Column(Modifier.fillMaxSize().testTag("my-syllabi-screen")) {
        Text("Meus editais", style = MaterialTheme.typography.headlineSmall, modifier = Modifier.padding(16.dp))
        if (state.remoteUnavailable) {
            Text(
                "Biblioteca da conta indisponível. Os dados locais continuam neste dispositivo.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.testTag("my-syllabi-offline").padding(horizontal = 16.dp, vertical = 8.dp),
            )
        }
        state.actionError?.let { Text(it, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(horizontal = 16.dp)) }
        if (state.loading && state.rows.isEmpty()) {
            Text("Carregando editais…", modifier = Modifier.padding(16.dp))
        } else if (state.rows.isEmpty()) {
            Text("Nenhum edital salvo ainda.", modifier = Modifier.padding(16.dp))
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                items(state.rows, key = { it.remote?.remoteSyllabusId ?: "local-${it.local?.id}" }) { row ->
                    Card(Modifier.fillMaxWidth().padding(horizontal = 12.dp)) {
                        Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(row.local?.name ?: row.remote?.title.orEmpty(), style = MaterialTheme.typography.titleMedium)
                            when {
                                row.remote != null -> Text("SYNCED · Salvo na sua conta")
                                row.syncState == RemoteSyllabusSyncState.PENDING -> Text("PENDING · Salvo neste dispositivo; sincronização pendente")
                                row.syncState == RemoteSyllabusSyncState.FAILED -> Text("FAILED · Falha na sincronização; os dados locais foram mantidos")
                                state.remoteUnavailable -> Text("Somente neste dispositivo · estado remoto indisponível")
                                else -> Text("Somente neste dispositivo")
                            }
                            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                if (row.local == null && row.remote != null) {
                                    Button(onClick = { viewModel.download(row.remote.remoteSyllabusId) }) {
                                        Text("Baixar para este dispositivo")
                                    }
                                }
                                row.local?.let { local ->
                                    OutlinedButton(onClick = { confirmLocalRemoval = local.id }) {
                                        Text("Remover deste dispositivo")
                                    }
                                }
                                row.remote?.let { remote ->
                                    TextButton(onClick = { confirmRemoteDeletion = remote.remoteSyllabusId }) {
                                        Text("Excluir da minha conta")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    confirmLocalRemoval?.let { localId ->
        AlertDialog(
            onDismissRequest = { confirmLocalRemoval = null },
            title = { Text("Remover deste dispositivo?") },
            text = { Text("Se houver uma cópia privada na conta, ela será mantida.") },
            confirmButton = {
                TextButton(onClick = { confirmLocalRemoval = null; viewModel.removeFromDevice(localId) }) { Text("Remover") }
            },
            dismissButton = { TextButton(onClick = { confirmLocalRemoval = null }) { Text("Cancelar") } },
        )
    }
    confirmRemoteDeletion?.let { remoteId ->
        AlertDialog(
            onDismissRequest = { confirmRemoteDeletion = null },
            title = { Text("Excluir da sua conta?") },
            text = { Text("A cópia privada será excluída da conta. Qualquer cópia local deste edital será mantida.") },
            confirmButton = {
                TextButton(onClick = { confirmRemoteDeletion = null; viewModel.deleteFromAccount(remoteId) }) { Text("Excluir da conta") }
            },
            dismissButton = { TextButton(onClick = { confirmRemoteDeletion = null }) { Text("Cancelar") } },
        )
    }
}
