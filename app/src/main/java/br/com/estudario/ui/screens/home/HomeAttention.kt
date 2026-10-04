package br.com.estudario.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material.icons.outlined.Autorenew
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material.icons.outlined.TrendingDown
import br.com.estudario.ui.brand.Icon
import androidx.compose.material3.MaterialTheme
import br.com.estudario.ui.brand.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import br.com.estudario.ui.theme.EstudarioShapes
import br.com.estudario.ui.theme.EstudarioSpacing
import br.com.estudario.ui.theme.estudarioColors

/**
 * O que está pedindo atenção agora: revisões vencendo, erros para refazer, o tópico mais frágil.
 *
 * Cada pendência é um cartão próprio, com ícone, número, o que fazer e a seta de "abrir", para
 * ficar óbvio que é tocável. Só aparece o que tem conteúdo real; nada de "0 pendências".
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
    val colors = estudarioColors()

    Column(modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(EstudarioSpacing.small)) {
        Text("Pedindo atenção", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
        if (pendingErrors > 0) AttentionCard(
            icon = Icons.Outlined.ErrorOutline,
            accent = MaterialTheme.colorScheme.error,
            count = pendingErrors,
            title = if (pendingErrors == 1) "Questão errada para refazer" else "Questões erradas para refazer",
            subtitle = "Caderno de erros · refazer agora fixa o que você errou",
            onClick = onOpenErrors,
        )
        if (pendingReviews > 0) AttentionCard(
            icon = Icons.Outlined.Autorenew,
            accent = MaterialTheme.colorScheme.primary,
            count = pendingReviews,
            title = if (pendingReviews == 1) "Revisão no dia certo" else "Revisões no dia certo",
            subtitle = "Revisar hoje evita esquecer o que já estudou",
            onClick = onOpenReviews,
        )
        if (weakTopicName != null) AttentionCard(
            icon = Icons.Outlined.TrendingDown,
            accent = colors.attention,
            count = null,
            badge = "$weakTopicMastery%",
            title = weakTopicName,
            subtitle = "Seu ponto mais frágil · domínio de $weakTopicMastery%",
            onClick = onOpenWeakTopic,
        )
    }
}

@Composable
private fun AttentionCard(
    icon: ImageVector,
    accent: Color,
    count: Int?,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    badge: String? = null,
) {
    Surface(onClick = onClick, shape = EstudarioShapes.row, color = MaterialTheme.colorScheme.surfaceContainerLow, modifier = Modifier.fillMaxWidth()) {
        Row(Modifier.padding(start = 14.dp, end = 8.dp, top = 12.dp, bottom = 12.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Box(Modifier.size(42.dp), contentAlignment = Alignment.Center) { Icon(icon, null, tint = accent, modifier = Modifier.size(38.dp)) }
            Column(Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    if (count != null) Text("$count", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = accent)
                    Text(title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold, maxLines = 2, overflow = TextOverflow.Ellipsis, modifier = Modifier.weight(1f, fill = false))
                }
                Text(subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 2, overflow = TextOverflow.Ellipsis)
            }
            if (badge != null) Surface(shape = CircleShape, color = accent.copy(alpha = 0.15f)) {
                Text(badge, Modifier.padding(horizontal = 10.dp, vertical = 4.dp), style = MaterialTheme.typography.labelLarge, color = accent, fontWeight = FontWeight.Bold)
            }
            Icon(Icons.AutoMirrored.Outlined.KeyboardArrowRight, "Abrir", tint = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
