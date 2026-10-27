package br.com.estudario.ui.components

import kotlinx.coroutines.launch

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AutoStories
import androidx.compose.material.icons.outlined.LocalFireDepartment
import androidx.compose.material.icons.outlined.NotificationsActive
import br.com.estudario.ui.brand.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

/**
 * O convite para os avisos, com o Folha explicando para que servem, antes da pergunta do Android.
 * Aparece uma vez, na primeira matéria gerada (não ao abrir o app). "Ativar" chama a pergunta do
 * sistema; "Agora não" fecha sem insistir (dá para ligar depois em Ajustes › Notificações).
 */
@Composable
fun NotificationInvite(onDone: () -> Unit, forStudy: Boolean = false) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val ask = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        // com a permissão, os lembretes já ficam agendados (sem esperar o app abrir de novo)
        if (granted) kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.Default).launch {
            val app = context.applicationContext as br.com.estudario.EstudarioApplication
            app.preferences.setNotificationsEnabled(true)
            br.com.estudario.notifications.StudyNotificationCoordinator.refresh(app, app.preferences)
        }
        onDone()
    }
    Dialog(onDismissRequest = onDone) {
        Column(
            Modifier.background(MaterialTheme.colorScheme.surface, RoundedCornerShape(28.dp)).padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            br.com.estudario.ui.assistant.Folha(110.dp, mood = br.com.estudario.ui.assistant.FolhaMood.TALKING)
            Text("Posso te avisar?", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black, textAlign = TextAlign.Center)
            Text(
                if (forStudy) "Eu te chamo na hora certa: quando uma revisão vencer, se a sua sequência estiver em risco e quando o material ficar pronto." else "Sua matéria leva alguns minutos para ficar pronta. Pode sair do app: eu te chamo na hora.",
                style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center,
            )
            Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Benefit(Icons.Outlined.AutoStories, "Matéria pronta", "Aviso assim que o material sai.")
                Benefit(Icons.Outlined.NotificationsActive, "Revisão no dia certo", "De manhã, o tópico que vence hoje, e um lembrete perto da hora em que você costuma estudar.")
                Benefit(Icons.Outlined.LocalFireDepartment, "Sua sequência", "À noite, se o dia ainda não foi salvo, para ela não acabar.")
            }
            Text(
                "Sem exagero: no máximo 3 avisos de estudo por dia, nada durante o modo foco, e você muda tudo em Ajustes.",
                style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center,
            )
            br.com.estudario.ui.brand.Button(
                onClick = { ask.launch(android.Manifest.permission.POST_NOTIFICATIONS) },
                modifier = Modifier.fillMaxWidth().height(52.dp),
            ) { Text("Ativar avisos", fontWeight = FontWeight.Bold) }
            TextButton(onClick = onDone, modifier = Modifier.fillMaxWidth()) { Text("Agora não") }
        }
    }
}

@Composable
private fun Benefit(icon: ImageVector, title: String, body: String) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        Box(Modifier.size(38.dp).background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f), CircleShape), contentAlignment = Alignment.Center) {
            Icon(icon, null, Modifier.size(20.dp), tint = MaterialTheme.colorScheme.primary)
        }
        Column(Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
            Text(body, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
