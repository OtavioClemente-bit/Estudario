package br.com.estudario.ui.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import br.com.estudario.ui.components.SubjectDot
import br.com.estudario.ui.theme.EstudarioSpacing

/**
 * O que vem depois de agora — em duas linhas, no máximo.
 *
 * A Home **não** mostra a agenda do dia nem a semana: isso é a aba Plano, e duplicar o cronograma
 * aqui foi exatamente o que deixava as duas telas redundantes. O que fica é só a continuidade: a
 * pessoa vê que existe um depois, sem precisar ler o depois inteiro.
 */
@Composable
fun NextUpStrip(nextUp: NextUpUi, onOpenPlan: () -> Unit, modifier: Modifier = Modifier) {
    if (nextUp.items.isEmpty()) return
    Column(
        modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(4.dp))
            .clickable(onClick = onOpenPlan),
        verticalArrangement = Arrangement.spacedBy(EstudarioSpacing.tight),
    ) {
        Text(
            "Depois",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        nextUp.items.forEach { task ->
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(EstudarioSpacing.tight),
            ) {
                SubjectDot(task.subjectName, size = 7.dp)
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(
                        task.subjectName,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        task.topicName,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                Text(
                    task.durationLabel,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}
