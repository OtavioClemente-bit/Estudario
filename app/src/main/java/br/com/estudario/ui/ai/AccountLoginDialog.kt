package br.com.estudario.ui.ai

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import br.com.estudario.EstudarioApplication
import kotlinx.coroutines.launch

/**
 * Entrar na conta Estudário por código no e-mail. É a conta que guarda a cota da IA; o login do
 * Google do backup é outro e não serve aqui.
 */
@Composable
fun AccountLoginDialog(onDismiss: () -> Unit, onSignedIn: () -> Unit) {
    val app = LocalContext.current.applicationContext as EstudarioApplication
    val auth = app.supabaseAuthRepository
    val scope = rememberCoroutineScope()
    var email by rememberSaveable { mutableStateOf("") }
    var code by rememberSaveable { mutableStateOf("") }
    var codeSent by rememberSaveable { mutableStateOf(false) }
    var busy by rememberSaveable { mutableStateOf(false) }
    var error by rememberSaveable { mutableStateOf<String?>(null) }
    AlertDialog(
        onDismissRequest = { if (!busy) onDismiss() },
        icon = { Icon(Icons.Outlined.Lock, null) },
        title = { Text("Entrar na conta Estudário") },
        text = {
            Column {
                Text(
                    if (codeSent) "Enviamos um código para $email. Digite-o abaixo." else "Use seu e-mail. Enviamos um código de acesso, sem senha.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Spacer(Modifier.height(12.dp))
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    enabled = !codeSent && !busy,
                    label = { Text("E-mail") },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth(),
                )
                if (codeSent) {
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = code,
                        onValueChange = { code = it },
                        enabled = !busy,
                        label = { Text("Código recebido") },
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
                error?.let {
                    Spacer(Modifier.height(8.dp))
                    Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                }
            }
        },
        confirmButton = {
            Button(
                enabled = !busy && if (codeSent) code.isNotBlank() else email.isNotBlank(),
                onClick = {
                    scope.launch {
                        busy = true
                        error = null
                        runCatching {
                            if (codeSent) {
                                auth.verifyEmailOtp(email.trim(), code.trim())
                                onSignedIn()
                            } else {
                                auth.sendEmailOtp(email.trim())
                                codeSent = true
                            }
                        }.onFailure { error = if (codeSent) "Código inválido ou expirado." else "Não foi possível enviar o código agora." }
                        busy = false
                    }
                },
            ) { Text(if (codeSent) "Entrar" else "Enviar código") }
        },
        dismissButton = { TextButton(enabled = !busy, onClick = onDismiss) { Text("Cancelar") } },
    )
}
