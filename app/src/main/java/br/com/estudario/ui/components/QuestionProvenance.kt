package br.com.estudario.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.automirrored.outlined.OpenInNew
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import br.com.estudario.data.local.QuestionEntity
import br.com.estudario.data.local.QuestionSourceType

@Composable
fun QuestionProvenance(question: QuestionEntity, modifier: Modifier = Modifier) {
    val uriHandler = LocalUriHandler.current
    val label = when (question.questionSourceType) {
        QuestionSourceType.REAL -> "Questão identificada como prova real"
        QuestionSourceType.REAL_ADAPTED -> "Questão adaptada de prova real"
        QuestionSourceType.AUTHORIAL -> "Questão autoral criada para estudo"
    }
    val metadata = listOfNotNull(question.board, question.agency, question.year?.toString())
        .distinct()
        .joinToString(" • ")
    val sourceUrl = question.sourceUrl?.takeIf(::isSafeWebUrl)

    Column(modifier) {
        Text(
            if (metadata.isBlank()) label else "$label • $metadata",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        question.sourceId?.takeIf { it.isNotBlank() }?.let {
            Text("Identificação da fonte: $it", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        if (question.questionSourceType == QuestionSourceType.AUTHORIAL) {
            Text(
                "Confira as fontes citadas na explicação para verificar o conteúdo e o gabarito.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        } else if (sourceUrl == null) {
            Text(
                "Link da origem não informado; confira a identificação da fonte acima.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        if (sourceUrl != null) {
            Spacer(Modifier.height(2.dp))
            TextButton(onClick = { uriHandler.openUri(sourceUrl) }) {
                Icon(Icons.AutoMirrored.Outlined.OpenInNew, contentDescription = null)
                Text(if (question.questionSourceType == QuestionSourceType.AUTHORIAL) "Abrir referência indicada" else "Abrir fonte indicada")
            }
        }
    }
}
