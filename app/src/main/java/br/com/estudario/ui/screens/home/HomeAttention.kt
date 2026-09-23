package br.com.estudario.ui.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import br.com.estudario.ui.theme.EstudarioSpacing

/**
 * O que está pedindo atenção agora: revisões vencendo, erros para refazer, o tópico mais frágil.
 *
 * Fica no rodapé da Home de propósito, é útil, mas não é o que abre o dia. Cada linha só aparece
 * quando tem conteúdo real; nenhuma delas vira um "0 pendências" ocupando espaço.
 *
 * (Este arquivo ainda se chama `ProgressSummary.kt` por herança da versão anterior da Home. Vale
 * renomear para `HomeAttention.kt` no Android Studio, não dá para renomear arquivos daqui.)
 */
@Composable
fun HomeAttention(
    pendingReviews: Int,
    pendingErrors: Int,
    weakTopicName: String?,
    weakTopicMastery: Int,
    onOpenReviews: () -> Unit,
    onOpenErrors: () -> Unit,
    onOpenWeakTopic: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val temAlgo = pendingReviews > 0 || pendingErrors > 0 || weakTopicName != null
    if (!temAlgo) return

    Column(modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(EstudarioSpacing.tight)) {
        Text(
            "Pedindo atenção",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        if (pendingReviews > 0) {
            AttentionRow(
                text = if (pendingReviews == 1) "1 revisão no ponto de revisar" else "$pendingReviews revisões no ponto de revisar",
                onClick = onOpenReviews,
            )
        }
        if (pendingErrors > 0) {
            AttentionRow(
                text = if (pendingErrors == 1) "1 questão errada voltou para refazer" else "$pendingErrors questões erradas voltaram para refazer",
                onClick = onOpenErrors,
            )
        }
        if (weakTopicName != null) {
            AttentionRow(
                text = "Ponto mais frágil: $weakTopicName · domínio $weakTopicMastery%",
                onClick = onOpenWeakTopic,
            )
        }
    }
}

@Composable
private fun AttentionRow(text: String, onClick: () -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(4.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 2,
            modifier = Modifier.weight(1f), overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
        )
        Icon(
            Icons.AutoMirrored.Outlined.ArrowForward,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}
