package br.com.estudario.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Psychology
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import br.com.estudario.ui.brand.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * Pergunta de memorização: a resposta fica escondida até a pessoa tentar lembrar e pedir para ver.
 * Sem resposta cadastrada (pacotes antigos), orienta a conferir na teoria.
 */
@Composable
fun RecallCard(
    question: String,
    answer: String?,
    modifier: Modifier = Modifier,
    revealKey: Any = question,
    trailing: @Composable (() -> Unit)? = null,
) {
    var revealed by rememberSaveable(revealKey) { mutableStateOf(false) }
    ElevatedCard(modifier.fillMaxWidth(), colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)) {
        Column(Modifier.padding(start = 16.dp, end = 8.dp, top = 14.dp, bottom = 6.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Row(verticalAlignment = Alignment.Top) {
                Icon(Icons.Outlined.Psychology, null, Modifier.size(20.dp).padding(top = 2.dp), tint = MaterialTheme.colorScheme.primary)
                Spacer(Modifier.width(10.dp))
                StudyInlineText(question, Modifier.weight(1f), style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSecondaryContainer)
                trailing?.invoke()
            }
            AnimatedVisibility(revealed, enter = fadeIn() + expandVertically()) {
                Column(Modifier.padding(end = 8.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    HorizontalDivider(color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.15f))
                    if (answer.isNullOrBlank()) {
                        Text("Este material não trouxe a resposta. Confira na teoria do tópico.", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSecondaryContainer)
                    } else {
                        MarkdownText(answer)
                    }
                }
            }
            TextButton(onClick = { revealed = !revealed }) {
                Icon(if (revealed) Icons.Outlined.VisibilityOff else Icons.Outlined.Visibility, null, Modifier.size(18.dp))
                Spacer(Modifier.width(6.dp))
                Text(if (revealed) "Esconder resposta" else "Mostrar resposta")
            }
        }
    }
}
