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
import androidx.compose.material.icons.outlined.Pause
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.Stop
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import br.com.estudario.data.preferences.FocusSessionPrefs

/** "1:02:05" ou "12:34": o formato de cronômetro que todo mundo lê sem pensar. Pausas não contam. */
fun focusClock(session: FocusSessionPrefs, now: Long): String {
    val seconds = session.elapsedMillis(now) / 1_000L
    val h = seconds / 3600
    val m = seconds % 3600 / 60
    val s = seconds % 60
    return if (h > 0) "%d:%02d:%02d".format(h, m, s) else "%02d:%02d".format(m, s)
}

/** Ações comuns do cronômetro, para a barra, a pílula e a folha usarem as mesmas. */
data class FocusTimerActions(
    val onOpen: () -> Unit,
    val onCollapse: () -> Unit,
    val onExpand: () -> Unit,
    val onTogglePause: () -> Unit,
    val onToggleHidden: () -> Unit,
)

/** Ponto que pulsa enquanto conta e vira um quadrado parado na pausa: o estado se lê sem número. */
@Composable
private fun StatusDot(session: FocusSessionPrefs, color: Color, size: Int = 8) {
    val pulse by rememberInfiniteTransition(label = "focus-dot").animateFloat(0.35f, 1f, infiniteRepeatable(tween(900), RepeatMode.Reverse), label = "dot")
    Box(Modifier.size(size.dp).alpha(if (session.paused) 1f else pulse).background(color, if (session.paused) RoundedCornerShape(2.dp) else CircleShape))
}

/**
 * Cronômetro do modo foco, fixo no topo de qualquer tela enquanto a sessão está aberta. Tem pausa,
 * o olho que esconde os números (para quem fica ansioso vendo o tempo subir) e recolhe numa pílula.
 */
@Composable
fun FocusTimerBar(session: FocusSessionPrefs, now: Long, collapsed: Boolean, hidden: Boolean, actions: FocusTimerActions) {
    // Recolhido, quem aparece é a pílula na barra do topo ([FocusTimerPill]).
    if (!session.active || collapsed) return
    val container = if (session.paused) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.primary
    val content = if (session.paused) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onPrimary
    Surface(
        onClick = actions.onOpen,
        shape = RoundedCornerShape(16.dp),
        color = container,
        modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 4.dp),
    ) {
        Row(Modifier.padding(start = 14.dp, end = 2.dp, top = 2.dp, bottom = 2.dp), verticalAlignment = Alignment.CenterVertically) {
            StatusDot(session, content)
            Spacer(Modifier.width(10.dp))
            Column(Modifier.weight(1f)) {
                Text(if (session.paused) "PAUSADO" else "MODO FOCO", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Black, color = content.copy(alpha = .75f))
                Text(session.title.ifBlank { "Sessão de estudo" }, style = MaterialTheme.typography.bodySmall, color = content, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
            if (!hidden) Text(focusClock(session, now), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = content)
            IconButton(onClick = actions.onTogglePause) {
                Icon(if (session.paused) Icons.Outlined.PlayArrow else Icons.Outlined.Pause, if (session.paused) "Retomar" else "Pausar", tint = content)
            }
            IconButton(onClick = actions.onToggleHidden) {
                Icon(if (hidden) Icons.Outlined.VisibilityOff else Icons.Outlined.Visibility, if (hidden) "Mostrar o tempo" else "Esconder o tempo", tint = content)
            }
            IconButton(onClick = actions.onCollapse) { Icon(Icons.Outlined.KeyboardArrowUp, "Recolher cronômetro", tint = content) }
        }
    }
}

/**
 * Cronômetro recolhido: uma pílula na barra do topo, sem ocupar linha. Toque no tempo para abrir
 * de novo; o botão ao lado pausa ou retoma e o olho esconde os números.
 */
@Composable
fun FocusTimerPill(session: FocusSessionPrefs, now: Long, hidden: Boolean, actions: FocusTimerActions) {
    if (!session.active) return
    val container = if (session.paused) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.primary
    val content = if (session.paused) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onPrimary
    Surface(shape = RoundedCornerShape(50), color = container, modifier = Modifier.padding(end = 4.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Surface(onClick = actions.onExpand, shape = RoundedCornerShape(50), color = Color.Transparent) {
                // Com o tempo escondido, sobra só o ponto (pulsando = contando, quadrado = pausado).
                Row(Modifier.padding(start = 10.dp, end = if (hidden) 6.dp else 2.dp, top = 6.dp, bottom = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                    StatusDot(session, content, size = 7)
                    if (!hidden) {
                        Spacer(Modifier.width(6.dp))
                        Text(focusClock(session, now), style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = content)
                    }
                }
            }
            IconButton(onClick = actions.onTogglePause, modifier = Modifier.size(28.dp)) {
                Icon(if (session.paused) Icons.Outlined.PlayArrow else Icons.Outlined.Pause, if (session.paused) "Retomar" else "Pausar", Modifier.size(18.dp), tint = content)
            }
            IconButton(onClick = actions.onToggleHidden, modifier = Modifier.size(28.dp).padding(end = 2.dp)) {
                Icon(if (hidden) Icons.Outlined.VisibilityOff else Icons.Outlined.Visibility, if (hidden) "Mostrar o tempo" else "Esconder o tempo", Modifier.size(18.dp), tint = content)
            }
        }
    }
}

/** Detalhes da sessão: tempo grande, pausar, encerrar e salvar, ajustes de concentração. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FocusSessionSheet(
    session: FocusSessionPrefs,
    now: Long,
    hidden: Boolean,
    doNotDisturb: Boolean,
    keepScreenOn: Boolean,
    onDoNotDisturb: (Boolean) -> Unit,
    onKeepScreenOn: (Boolean) -> Unit,
    onTogglePause: () -> Unit,
    onToggleHidden: () -> Unit,
    onStop: () -> Unit,
    onOpenTopic: (() -> Unit)?,
    onHistory: () -> Unit,
    onDismiss: () -> Unit,
) {
    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(Modifier.fillMaxWidth().padding(horizontal = 24.dp).padding(bottom = 28.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Icon(Icons.Outlined.Timer, null, tint = MaterialTheme.colorScheme.primary)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        if (hidden) "Tempo escondido" else focusClock(session, now),
                        style = if (hidden) MaterialTheme.typography.headlineSmall else MaterialTheme.typography.displayMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (session.paused) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface,
                    )
                    IconButton(onClick = onToggleHidden) {
                        Icon(if (hidden) Icons.Outlined.VisibilityOff else Icons.Outlined.Visibility, if (hidden) "Mostrar o tempo" else "Esconder o tempo")
                    }
                }
                Text(
                    if (session.paused) "Pausado · ${session.title.ifBlank { "Sessão de estudo" }}" else session.title.ifBlank { "Sessão de estudo" },
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                FilledTonalButton(onClick = onTogglePause, modifier = Modifier.weight(1f), shape = RoundedCornerShape(14.dp)) {
                    Icon(if (session.paused) Icons.Outlined.PlayArrow else Icons.Outlined.Pause, null); Spacer(Modifier.width(8.dp))
                    Text(if (session.paused) "Retomar" else "Pausar")
                }
                Button(onClick = onStop, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error), shape = RoundedCornerShape(14.dp)) {
                    Icon(Icons.Outlined.Stop, null); Spacer(Modifier.width(8.dp)); Text("Encerrar")
                }
            }
            Text("Encerrar salva o tempo estudado no histórico. As pausas não contam.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
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
