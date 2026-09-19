package br.com.estudario.ui.focus

import android.app.Activity
import android.view.WindowManager
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DoNotDisturbOn
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.NotificationsOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import br.com.estudario.focus.FocusMode
import br.com.estudario.ui.AppViewModel
import br.com.estudario.ui.components.AppMark
import br.com.estudario.ui.components.EmptyState
import br.com.estudario.ui.components.LoadingScreen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private val Azul = Color(0xFF071B45)
private val Indigo = Color(0xFF4F46E5)

/**
 * A sessão de estudo cronometrada. Sem ciclo forçado e sem alarme: o relógio só mede e o Não
 * Perturbe protege. Quem decide quando parar é a pessoa.
 */
@Composable
fun FocusScreen(viewModel: AppViewModel, onBack: () -> Unit, onFinishedTask: (String, Int) -> Unit) {
    val context = LocalContext.current
    val session by viewModel.focusSession.collectAsState()
    val keepScreenOn by viewModel.focusKeepScreenOn.collectAsState()
    val wantsDnd by viewModel.focusDoNotDisturb.collectAsState()
    val scope = rememberCoroutineScope()
    var dndGranted by remember { mutableStateOf(FocusMode.hasDndAccess(context)) }
    var esperou by remember { mutableStateOf(false) }
    var agora by remember { mutableLongStateOf(System.currentTimeMillis()) }

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) dndGranted = FocusMode.hasDndAccess(context)
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }
    // A tela acesa vale só enquanto esta tela está aberta: sair devolve o comportamento normal.
    DisposableEffect(keepScreenOn) {
        val window = (context as? Activity)?.window
        if (keepScreenOn) window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        onDispose { window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON) }
    }
    LaunchedEffect(Unit) { delay(1_500); esperou = true }
    LaunchedEffect(session.active) {
        while (session.active) {
            agora = System.currentTimeMillis()
            delay(1_000)
        }
    }

    if (!session.active) {
        if (!esperou) LoadingScreen("Preparando o modo foco", "Ligando o Não Perturbe e iniciando o cronômetro.")
        else EmptyState("Nenhuma sessão aberta", "Comece pelo plano do dia, por um tópico ou por uma sessão livre.", "Voltar", onBack)
        return
    }

    val segundos = ((agora - session.startedAt) / 1_000L).coerceAtLeast(0L)
    val dndAtivo = wantsDnd && dndGranted

    Column(Modifier.fillMaxSize()) {
        Box(
            Modifier.fillMaxWidth().clip(RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp))
                .background(Brush.linearGradient(listOf(Indigo, Color(0xFF3A32B8), Azul))),
        ) {
            Column(
                Modifier.fillMaxWidth().padding(24.dp, 36.dp, 24.dp, 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                AppMark(52.dp)
                Text("Modo foco", color = Color.White.copy(alpha = 0.85f), fontWeight = FontWeight.SemiBold)
                Text(
                    relogio(segundos),
                    color = Color.White,
                    fontSize = 56.sp,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    session.title,
                    color = Color.White.copy(alpha = 0.9f),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleMedium,
                )
            }
        }

        Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Linha(
                if (dndAtivo) Icons.Outlined.DoNotDisturbOn else Icons.Outlined.NotificationsOff,
                if (dndAtivo) "Não perturbe ligado" else "Não perturbe desligado",
                if (dndAtivo) "As exceções que você configurou no Android continuam passando. Ele volta ao normal quando você encerrar."
                else if (!wantsDnd) "Você desligou essa opção em Notificações."
                else "Falta conceder o acesso ao Não Perturbe nas configurações do Android.",
            )
            if (wantsDnd && !dndGranted) {
                OutlinedButton(onClick = { context.startActivity(FocusMode.dndSettingsIntent()) }, Modifier.fillMaxWidth()) {
                    Text("Conceder acesso ao Não Perturbe")
                }
            }
            if (keepScreenOn) Linha(Icons.Outlined.Lightbulb, "Tela ligada", "Enquanto esta tela estiver aberta, o aparelho não apaga sozinho.")

            Text(
                "Sem ciclo forçado e sem alarme: estude o tempo que fizer sentido e encerre quando parar. O tempo medido entra no seu histórico.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            Button(
                onClick = {
                    scope.launch {
                        val taskId = session.taskId
                        val minutos = viewModel.stopFocus()
                        if (taskId != null) onFinishedTask(taskId, minutos) else onBack()
                    }
                },
                Modifier.fillMaxWidth(),
            ) { Text("Encerrar sessão") }
            TextButton(onClick = onBack, Modifier.fillMaxWidth()) { Text("Sair sem encerrar (o cronômetro continua)") }
        }
    }
}

@Composable
private fun Linha(icon: androidx.compose.ui.graphics.vector.ImageVector, titulo: String, apoio: String) {
    Row(verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        Icon(icon, null, tint = MaterialTheme.colorScheme.primary)
        Column {
            Text(titulo, fontWeight = FontWeight.SemiBold)
            Text(apoio, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

private fun relogio(segundos: Long): String {
    val h = segundos / 3_600
    val m = (segundos % 3_600) / 60
    val s = segundos % 60
    return if (h > 0) String.format("%d:%02d:%02d", h, m, s) else String.format("%02d:%02d", m, s)
}
