package br.com.estudario.ui.screens

import android.Manifest
import android.app.TimePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import br.com.estudario.focus.FocusMode
import br.com.estudario.ui.AppViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner

@Composable
fun NotificationSettingsScreen(viewModel: AppViewModel, onBack: () -> Unit) {
    val context = LocalContext.current
    val enabled by viewModel.notificationsEnabled.collectAsState()
    val daily by viewModel.dailyReminderEnabled.collectAsState()
    val pending by viewModel.pendingAlertsEnabled.collectAsState()
    val hour by viewModel.reminderHour.collectAsState()
    val minute by viewModel.reminderMinute.collectAsState()
    val focusDnd by viewModel.focusDoNotDisturb.collectAsState()
    val focusScreenOn by viewModel.focusKeepScreenOn.collectAsState()
    var dndGranted by remember { mutableStateOf(FocusMode.hasDndAccess(context)) }
    var permissionGranted by remember { mutableStateOf(Build.VERSION.SDK_INT < 33 || ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) }
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                permissionGranted = Build.VERSION.SDK_INT < 33 || ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
                // A concessão do Não Perturbe acontece fora do app; ao voltar, conferimos de novo.
                dndGranted = FocusMode.hasDndAccess(context)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }
    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        permissionGranted = granted
        viewModel.setNotificationsEnabled(granted)
    }

    fun requestEnable() {
        if (Build.VERSION.SDK_INT >= 33 && !permissionGranted) permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        else viewModel.setNotificationsEnabled(true)
    }

    LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) { Icon(Icons.Outlined.ArrowBack, "Voltar") }
                Column { Text("Notificações de estudo", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold); Text("Lembretes úteis, sem excesso") }
            }
        }
        item {
            ElevatedCard(colors = CardDefaults.elevatedCardColors(containerColor = if (enabled && permissionGranted) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant)) {
                ListItem(
                    headlineContent = { Text(if (enabled && permissionGranted) "Notificações ativas" else "Ativar notificações", fontWeight = FontWeight.Bold) },
                    supportingContent = { Text(if (permissionGranted) "Você controla o horário e os tipos de aviso abaixo." else "O Android precisa autorizar os avisos deste aplicativo.") },
                    leadingContent = { Icon(if (enabled && permissionGranted) Icons.Outlined.NotificationsActive else Icons.Outlined.Notifications, null) },
                    trailingContent = { Switch(checked = enabled && permissionGranted, onCheckedChange = { checked -> if (checked) requestEnable() else viewModel.setNotificationsEnabled(false) }) },
                    colors = ListItemDefaults.colors(containerColor = androidx.compose.ui.graphics.Color.Transparent),
                )
            }
        }
        if (!permissionGranted) item {
            OutlinedButton(onClick = {
                context.startActivity(Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName))
            }, Modifier.fillMaxWidth()) { Icon(Icons.Outlined.Settings, null); Spacer(Modifier.width(8.dp)); Text("Abrir permissões do Android") }
        }
        item {
            Text("Programação", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        }
        item {
            ElevatedCard {
                ListItem(
                    headlineContent = { Text("Lembrete diário", fontWeight = FontWeight.SemiBold) },
                    supportingContent = { Text("Um convite diário para manter a constância") },
                    leadingContent = { Icon(Icons.Outlined.Schedule, null) },
                    trailingContent = { Switch(daily, viewModel::setDailyReminderEnabled, enabled = enabled) },
                )
                HorizontalDivider()
                ListItem(
                    headlineContent = { Text("Horário") },
                    supportingContent = { Text("Pode sofrer pequeno ajuste para preservar a bateria") },
                    trailingContent = {
                        FilledTonalButton(onClick = { TimePickerDialog(context, { _, selectedHour, selectedMinute -> viewModel.setReminderTime(selectedHour, selectedMinute) }, hour, minute, true).show() }, enabled = enabled && daily) {
                            Text("%02d:%02d".format(hour, minute), fontWeight = FontWeight.Bold)
                        }
                    },
                )
            }
        }
        item {
            ElevatedCard {
                ListItem(
                    headlineContent = { Text("Pendências e revisões", fontWeight = FontWeight.SemiBold) },
                    supportingContent = { Text("Avisa quando uma revisão programada estiver vencida") },
                    leadingContent = { Icon(Icons.Outlined.PendingActions, null) },
                    trailingContent = { Switch(pending, viewModel::setPendingAlertsEnabled, enabled = enabled) },
                )
            }
        }
        item {
            Text("Modo foco", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        }
        item {
            ElevatedCard {
                ListItem(
                    headlineContent = { Text("Não perturbe durante o estudo", fontWeight = FontWeight.SemiBold) },
                    supportingContent = { Text("Liga o Não Perturbe do Android ao começar a sessão e devolve tudo ao normal ao encerrar. As exceções que você configurou (favoritos, quem liga duas vezes) continuam passando.") },
                    leadingContent = { Icon(Icons.Outlined.DoNotDisturbOn, null) },
                    trailingContent = { Switch(focusDnd, viewModel::setFocusDoNotDisturb) },
                )
                HorizontalDivider()
                ListItem(
                    headlineContent = { Text("Manter a tela ligada", fontWeight = FontWeight.SemiBold) },
                    supportingContent = { Text("Enquanto a tela do modo foco estiver aberta") },
                    leadingContent = { Icon(Icons.Outlined.Lightbulb, null) },
                    trailingContent = { Switch(focusScreenOn, viewModel::setFocusKeepScreenOn) },
                )
                if (focusDnd && !dndGranted) {
                    HorizontalDivider()
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Falta uma autorização do Android", fontWeight = FontWeight.Bold)
                        Text("O Não Perturbe só pode ser ligado por um app depois que você autoriza, uma única vez, nas configurações do sistema.", style = MaterialTheme.typography.bodySmall)
                        OutlinedButton(onClick = { context.startActivity(FocusMode.dndSettingsIntent()) }, Modifier.fillMaxWidth()) {
                            Text("Conceder acesso ao Não Perturbe")
                        }
                    }
                }
            }
        }
        item {
            Text("Durante uma sessão de foco os lembretes do Estudário ficam em silêncio e voltam sozinhos depois.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        item {
            Button(onClick = viewModel::sendTestNotification, enabled = enabled && permissionGranted, modifier = Modifier.fillMaxWidth()) {
                Icon(Icons.Outlined.Send, null); Spacer(Modifier.width(8.dp)); Text("Enviar notificação de teste")
            }
        }
        item {
            Surface(color = MaterialTheme.colorScheme.surfaceVariant, shape = MaterialTheme.shapes.medium) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("Como funciona", fontWeight = FontWeight.Bold)
                    Text("Os avisos são calculados no próprio celular. Nenhum dado de estudo é enviado para servidor, e o Android pode ajustar alguns minutos do horário para economizar bateria.")
                }
            }
        }
    }
}
