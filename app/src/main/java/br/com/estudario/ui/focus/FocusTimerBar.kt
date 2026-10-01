package br.com.estudario.ui.focus

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material.icons.outlined.OpenInNew
import androidx.compose.material.icons.outlined.Stop
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import br.com.estudario.data.preferences.FocusSessionPrefs

/** "1:02:05" ou "12:34": o formato de cronômetro que todo mundo lê sem pensar. */
fun focusClock(session: FocusSessionPrefs, now: Long): String {
    val seconds = if (!session.active) 0 else ((now - session.startedAt) / 1_000L).coerceAtLeast(0)
    val h = seconds / 3600
    val m = seconds % 3600 / 60
    val s = seconds % 60
    return if (h > 0) "%d:%02d:%02d".format(h, m, s) else "%02d:%02d".format(m, s)
}

/**
 * Cronômetro do modo foco, fixo no topo de qualquer tela enquanto a sessão está aberta. Recolhido,
 * vira uma pílula na barra do topo; tocar na barra abre os detalhes.
 */
@Composable
fun FocusTimerBar(session: FocusSessionPrefs, now: Long, collapsed: Boolean, onOpen: () -> Unit, onCollapse: () -> Unit, onExpand: () -> Unit) {
    // Recolhido, quem aparece é a pílula na barra do topo ([FocusTimerPill]).
    if (!session.active || collapsed) return
    val pulse by rememberInfiniteTransition(label = "focus").animateFloat(0.35f, 1f, infiniteRepeatable(tween(900), RepeatMode.Reverse), label = "dot")
            Surface(
                onClick = onOpen,
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 4.dp),
            ) {
                Row(Modifier.padding(start = 14.dp, end = 2.dp, top = 4.dp, bottom = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(Modifier.size(8.dp).alpha(pulse).background(MaterialTheme.colorScheme.onPrimary, CircleShape))
                    Spacer(Modifier.width(10.dp))
                    Column(Modifier.weight(1f)) {
                        Text("MODO FOCO", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.onPrimary.copy(alpha = .75f))
                        Text(session.title.ifBlank { "Sessão de estudo" }, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onPrimary, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    }
                    Text(focusClock(session, now), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimary)
                    IconButton(onClick = onCollapse) { Icon(Icons.Outlined.KeyboardArrowUp, "Recolher cronômetro", tint = MaterialTheme.colorScheme.onPrimary) }
                }
            }
}

/** Detalhes da sessão: tempo grande, encerrar e salvar, ajustes de concentração. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FocusSessionSheet(
    session: FocusSessionPrefs,
    now: Long,
    doNotDisturb: Boolean,
    keepScreenOn: Boolean,
    onDoNotDisturb: (Boolean) -> Unit,
    onKeepScreenOn: (Boolean) -> Unit,
    onStop: () -> Unit,
    onOpenTopic: (() -> Unit)?,
    onHistory: () -> Unit,
    onDismiss: () -> Unit,
) {
    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(Modifier.fillMaxWidth().padding(horizontal = 24.dp).padding(bottom = 28.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Icon(Icons.Outlined.Timer, null, tint = MaterialTheme.colorScheme.primary)
                Text(focusClock(session, now), style = MaterialTheme.typography.displayMedium, fontWeight = FontWeight.Bold)
                Text(session.title.ifBlank { "Sessão de estudo" }, style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 2, overflow = TextOverflow.Ellipsis)
            }
            Button(onClick = onStop, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error), shape = RoundedCornerShape(14.dp)) {
                Icon(Icons.Outlined.Stop, null); Spacer(Modifier.width(8.dp)); Text("Encerrar e salvar")
            }
            if (onOpenTopic != null) OutlinedButton(onClick = onOpenTopic, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp)) {
                Icon(Icons.Outlined.OpenInNew, null, Modifier.size(18.dp)); Spacer(Modifier.width(8.dp)); Text("Voltar para o tópico")
            }
            HorizontalDivider()
            ListItem(
                headlineContent = { Text("Não perturbe") },
                supportingContent = { Text("Silencia notificações enquanto o foco roda") },
                trailingContent = { Switch(doNotDisturb, onDoNotDisturb) },
            )
            ListItem(
                headlineContent = { Text("Tela sempre ligada") },
                supportingContent = { Text("Útil para ler sem a tela apagar") },
                trailingContent = { Switch(keepScreenOn, onKeepScreenOn) },
            )
            OutlinedButton(onClick = onHistory, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp)) {
                Icon(Icons.Outlined.History, null, Modifier.size(18.dp)); Spacer(Modifier.width(8.dp)); Text("Histórico de foco")
            }
        }
    }
}

/** Cronômetro recolhido: uma pílula na barra do topo, sem ocupar linha. Toque para abrir de novo. */
@Composable
fun FocusTimerPill(session: FocusSessionPrefs, now: Long, onExpand: () -> Unit) {
    if (!session.active) return
    val pulse by rememberInfiniteTransition(label = "focus-pill").animateFloat(0.35f, 1f, infiniteRepeatable(tween(900), RepeatMode.Reverse), label = "dot")
    Surface(onClick = onExpand, shape = RoundedCornerShape(50), color = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(end = 4.dp)) {
        Row(Modifier.padding(horizontal = 10.dp, vertical = 5.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(7.dp).alpha(pulse).background(MaterialTheme.colorScheme.onPrimary, CircleShape))
            Spacer(Modifier.width(6.dp))
            Text(focusClock(session, now), style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimary)
        }
    }
}
