package br.com.estudario.ui.planner

import br.com.estudario.ui.components.AlertDialog
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.Sync
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.estudario.EstudarioApplication
import br.com.estudario.data.local.planner.PlanTaskEntity
import br.com.estudario.data.planner.CalendarSyncService
import br.com.estudario.ui.components.ScreenTitle
import br.com.estudario.ui.theme.EstudarioShapes
import br.com.estudario.ui.theme.estudarioColors
import br.com.estudario.ui.theme.screenPadding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * O plano na agenda do celular: a pessoa escolhe o horário, o lembrete e o período, vê o que está
 * lá e pode tirar tudo com um toque. Nada é escrito sem ela pedir.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AgendaSyncScreen(planName: String?, loadTasks: suspend () -> List<PlanTaskEntity>) {
    val context = LocalContext.current
    val service = (context.applicationContext as EstudarioApplication).calendarSyncService
    val scope = rememberCoroutineScope()
    var settings by remember { mutableStateOf(service.settings()) }
    var events by remember { mutableIntStateOf(0) }
    var busy by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf<String?>(null) }
    var confirmDelete by remember { mutableStateOf(false) }
    var permitted by remember { mutableStateOf(service.hasPermissions()) }
    LaunchedEffect(Unit) { events = withContext(Dispatchers.IO) { service.eventCount() } }

    fun sync() {
        busy = true
        scope.launch {
            val next = settings.copy(enabled = true)
            service.saveSettings(next)
            val count = withContext(Dispatchers.IO) { service.syncTasks(loadTasks(), force = true) }
            settings = service.settings()
            events = withContext(Dispatchers.IO) { service.eventCount() }
            busy = false
            message = if (count == 0) "Nenhuma atividade do plano no período escolhido." else "$count atividade(s) do plano estão na sua agenda."
        }
    }
    val permission = rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { result ->
        permitted = result.values.all { it }
        if (permitted) sync() else message = "Sem a permissão de agenda o Estudário não consegue salvar o plano. Você pode liberar nas configurações do Android."
    }
    val startSync = {
        if (service.hasPermissions()) sync()
        else permission.launch(arrayOf(android.Manifest.permission.READ_CALENDAR, android.Manifest.permission.WRITE_CALENDAR))
    }
    fun update(value: CalendarSyncService.Settings) { settings = value; service.saveSettings(value) }

    message?.let { text -> AlertDialog(onDismissRequest = { message = null }, confirmButton = { TextButton(onClick = { message = null }) { Text("OK") } }, title = { Text("Agenda") }, text = { Text(text) }) }
    if (confirmDelete) AlertDialog(
        onDismissRequest = { confirmDelete = false },
        title = { Text("Tirar o plano da agenda?") },
        text = { Text("Apagamos só o calendário \"Plano de Estudos - Estudário\" e os $events evento(s) dele. Seus outros compromissos não são tocados, e o plano continua no app.") },
        confirmButton = {
            TextButton(onClick = {
                confirmDelete = false
                scope.launch {
                    withContext(Dispatchers.IO) { service.removeAll() }
                    settings = service.settings(); events = 0
                    message = "Pronto. O plano saiu da sua agenda."
                }
            }) { Text("Apagar da agenda", color = MaterialTheme.colorScheme.error) }
        },
        dismissButton = { TextButton(onClick = { confirmDelete = false }) { Text("Cancelar") } },
    )

    LazyColumn(Modifier.fillMaxSize(), contentPadding = screenPadding(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        item { ScreenTitle("Agenda do celular", "Seu plano junto com seus compromissos") }
        item {
            val on = settings.enabled && events > 0
            Surface(shape = EstudarioShapes.spotlight, color = MaterialTheme.colorScheme.primaryContainer, modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(if (on) Icons.Outlined.CheckCircle else Icons.Outlined.CalendarMonth, null, tint = if (on) estudarioColors().completed else MaterialTheme.colorScheme.primary)
                        Spacer(Modifier.width(10.dp))
                        Text(if (on) "Plano na sua agenda" else "Plano ainda fora da agenda", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimaryContainer)
                    }
                    Text(
                        if (on) "$events atividade(s) no calendário \"Plano de Estudos - Estudário\". Quando o plano muda, a agenda acompanha sozinha." +
                            (if (settings.lastSyncAt > 0) " Última atualização: ${SimpleDateFormat("dd/MM 'às' HH:mm", Locale("pt", "BR")).format(Date(settings.lastSyncAt))}." else "")
                        else "Cada atividade do ${planName ?: "seu plano"} vira um evento com horário e lembrete, num calendário separado que você pode esconder ou apagar quando quiser.",
                        style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                }
            }
        }
        item {
            Section("Horário de início", "As atividades do dia entram uma depois da outra a partir daqui.", Icons.Outlined.Schedule) {
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf(6, 7, 8, 9, 12, 14, 17, 18, 19, 20, 21).forEach { hour ->
                        FilterChip(settings.startHour == hour && settings.startMinute == 0, { update(settings.copy(startHour = hour, startMinute = 0)) }, label = { Text("%02d:00".format(hour)) })
                    }
                }
            }
        }
        item {
            Section("Lembrete", "O celular avisa antes de cada atividade.", Icons.Outlined.Schedule) {
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf(0 to "Sem lembrete", 5 to "5 min antes", 10 to "10 min antes", 30 to "30 min antes", 60 to "1 h antes").forEach { (value, label) ->
                        FilterChip(settings.reminderMinutes == value, { update(settings.copy(reminderMinutes = value)) }, label = { Text(label) })
                    }
                }
            }
        }
        item {
            Section("Período", "Quanto do plano vai para a agenda.", Icons.Outlined.CalendarMonth) {
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf(7 to "Próximos 7 dias", 30 to "Próximos 30 dias", 90 to "3 meses", 0 to "Plano inteiro").forEach { (value, label) ->
                        FilterChip(settings.daysAhead == value, { update(settings.copy(daysAhead = value)) }, label = { Text(label) })
                    }
                }
            }
        }
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Button(onClick = startSync, enabled = !busy && planName != null, modifier = Modifier.fillMaxWidth().heightIn(min = 54.dp)) {
                    if (busy) CircularProgressIndicator(Modifier.size(20.dp), strokeWidth = 2.dp, color = MaterialTheme.colorScheme.onPrimary)
                    else Icon(Icons.Outlined.Sync, null)
                    Spacer(Modifier.width(8.dp))
                    Text(if (settings.enabled && events > 0) "Atualizar a agenda" else "Salvar plano na agenda")
                }
                if (planName == null) Text("Crie ou ative um plano de estudo para salvar na agenda.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                if (events > 0 || settings.enabled) OutlinedButton(
                    onClick = { confirmDelete = true },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
                ) { Icon(Icons.Outlined.DeleteOutline, null); Spacer(Modifier.width(8.dp)); Text("Apagar da agenda") }
            }
        }
    }
}

@Composable
private fun Section(title: String, subtitle: String, icon: androidx.compose.ui.graphics.vector.ImageVector, content: @Composable () -> Unit) {
    Surface(shape = EstudarioShapes.panel, color = MaterialTheme.colorScheme.surfaceContainerLow, modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, null, Modifier.size(20.dp), tint = MaterialTheme.colorScheme.primary)
                Spacer(Modifier.width(8.dp))
                Text(title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
            }
            Text(subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            content()
        }
    }
}
