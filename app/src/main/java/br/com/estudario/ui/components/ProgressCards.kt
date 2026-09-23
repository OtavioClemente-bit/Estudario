package br.com.estudario.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun CoverageCard(
    coveragePercent: Int,
    masteryPercent: Int,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(Icons.Outlined.PieChart, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Text("Cobertura e Domínio", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            }
            
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Edital estudado", style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(1f).padding(end = 8.dp))
                    Text("$coveragePercent%", fontWeight = FontWeight.Bold)
                }
                LinearProgressIndicator(
                    progress = { coveragePercent / 100f },
                    modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant
                )
            }
            
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Domínio médio", style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(1f).padding(end = 8.dp))
                    Text("$masteryPercent%", fontWeight = FontWeight.Bold)
                }
                val masteryColor = when {
                    masteryPercent >= 80 -> Color(0xFF087F5B)
                    masteryPercent >= 60 -> Color(0xFFF59F00)
                    else -> MaterialTheme.colorScheme.error
                }
                LinearProgressIndicator(
                    progress = { masteryPercent / 100f },
                    modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)),
                    color = masteryColor,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant
                )
            }
        }
    }
}

@Composable
fun PendingCard(
    reviewsPending: Int,
    errorsPending: Int,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    ElevatedCard(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(Icons.Outlined.NotificationsActive, contentDescription = null, tint = MaterialTheme.colorScheme.error)
                Text("Pendências", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            }
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Column(
                    modifier = Modifier.weight(1f).background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(8.dp)).padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("$reviewsPending", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = if (reviewsPending > 0) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface)
                    Text("Revisões", style = MaterialTheme.typography.labelSmall)
                }
                Column(
                    modifier = Modifier.weight(1f).background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(8.dp)).padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("$errorsPending", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = if (errorsPending > 0) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface)
                    Text("Erros", style = MaterialTheme.typography.labelSmall)
                }
            }
        }
    }
}

@Composable
fun WeakTopicCard(
    topicName: String?,
    masteryPercent: Int,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    if (topicName == null) return
    ElevatedCard(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(Icons.Outlined.WarningAmber, contentDescription = null, tint = Color(0xFFF59F00))
                Text("Foco Recomendado", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            }
            Text(
                text = topicName,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2, overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
            )
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Domínio:", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text("$masteryPercent%", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.error)
            }
        }
    }
}
