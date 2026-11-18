package br.com.estudario.ui.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import br.com.estudario.ui.assistant.Folha
import br.com.estudario.ui.assistant.FolhaMood
import br.com.estudario.ui.brand.Button
import br.com.estudario.ui.brand.OutlinedButton
import br.com.estudario.ui.brand.Surface
import br.com.estudario.ui.components.ProfileAvatar

/** Tamanho máximo do nome (o mesmo do site). */
const val NAME_MAX = 30

/** O nome como fica guardado: sem espaços sobrando e dentro do limite. */
fun cleanName(raw: String): String = raw.trim().replace(Regex("\\s+"), " ").take(NAME_MAX)

/** Janela para trocar o nome: avatar, Folha, campo com contador e a prévia de como ele vai chamar a pessoa. */
@Composable
fun NameDialog(profile: UserProfile, onDismiss: () -> Unit, onSave: (String) -> Unit) {
    var text by remember { mutableStateOf(profile.name.take(NAME_MAX)) }
    val clean = cleanName(text)
    val valid = clean.isNotEmpty()
    val save = { if (valid) { onSave(clean); onDismiss() } }
    Dialog(onDismissRequest = onDismiss) {
        Surface(shape = RoundedCornerShape(28.dp), color = MaterialTheme.colorScheme.surface, tonalElevation = 6.dp) {
            Column(Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Box(contentAlignment = Alignment.Center) {
                    Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy((-14).dp)) {
                        ProfileAvatar(profile.photoPath, if (valid) profile.copy(name = clean).initials else profile.initials, 72.dp, ring = true)
                        Folha(64.dp, mood = if (valid) FolhaMood.HAPPY else FolhaMood.IDLE, modifier = Modifier.offset(y = 6.dp))
                    }
                }
                Text("Como você quer ser chamado?", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black, textAlign = TextAlign.Center)
                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it.take(NAME_MAX) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    textStyle = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    placeholder = { Text("Seu nome", style = MaterialTheme.typography.headlineSmall) },
                    shape = RoundedCornerShape(16.dp),
                    isError = text.isNotEmpty() && !valid,
                    supportingText = {
                        Row(Modifier.fillMaxWidth()) {
                            Text(if (valid || text.isEmpty()) "Pode ser apelido." else "Escreva pelo menos uma letra.", Modifier.weight(1f))
                            Text("${text.length}/$NAME_MAX")
                        }
                    },
                    keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words, imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { save() }),
                )
                Surface(shape = RoundedCornerShape(18.dp), color = MaterialTheme.colorScheme.primaryContainer, modifier = Modifier.fillMaxWidth()) {
                    Text(
                        if (valid) "Bora estudar, ${clean.substringBefore(' ')}!" else "Bora estudar!",
                        Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                        textAlign = TextAlign.Center,
                    )
                }
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedButton(onClick = onDismiss, modifier = Modifier.weight(1f)) { Text("Cancelar") }
                    Button(onClick = save, enabled = valid, modifier = Modifier.weight(1f)) { Text("Salvar") }
                }
            }
        }
    }
}
