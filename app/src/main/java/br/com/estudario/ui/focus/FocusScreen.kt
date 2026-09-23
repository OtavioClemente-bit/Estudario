package br.com.estudario.ui.focus

import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.ui.text.TextStyle
import br.com.estudario.ui.theme.estudarioLayout
import android.app.Activity
import android.view.WindowManager
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DoNotDisturbOn
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.NotificationsOff
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import br.com.estudario.data.local.FocusSessionEntity
import br.com.estudario.data.local.FocusSessionOrigin
import br.com.estudario.data.local.SubjectEntity
import br.com.estudario.focus.FocusMode
import br.com.estudario.ui.AppViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

data class FocusSubjectOption(val id: Long, val name: String)

@Composable
fun FocusSubjectPicker(subjects: List<FocusSubjectOption>, selectedIds: Set<Long>, onToggle: (Long) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        subjects.forEach { subject ->
            Surface(
                modifier = Modifier.fillMaxWidth().clickable { onToggle(subject.id) },
                shape = RoundedCornerShape(16.dp),
                color = if (subject.id in selectedIds) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surfaceContainerLow,
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(start = 14.dp, end = 8.dp, top = 6.dp, bottom = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column(Modifier.weight(1f).padding(vertical = 8.dp)) {
                        Text(subject.name, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
                    }
                    Checkbox(checked = subject.id in selectedIds, onCheckedChange = { onToggle(subject.id) })
                }
            }
        }
    }
}

/** Sessão de foco e histórico. Timer, notificação e rede de segurança compartilham o mesmo estado salvo. */
@Composable
fun FocusScreen(
    viewModel: AppViewModel,
    onBack: () -> Unit,
    onOpenPlan: () -> Unit = onBack,
    onHome: () -> Unit = onBack,
    embedded: Boolean = false,
) {
    val context = LocalContext.current
    val session by viewModel.focusSession.collectAsState()
    val subjects by viewModel.subjects.collectAsState()
    val history by viewModel.focusSessions.collectAsState()
    val keepScreenOn by viewModel.focusKeepScreenOn.collectAsState()
    val wantsDnd by viewModel.focusDoNotDisturb.collectAsState()
    val scope = rememberCoroutineScope()
    var selectedIds by remember { mutableStateOf(emptySet<Long>()) }
    var dndGranted by remember { mutableStateOf(FocusMode.hasDndAccess(context)) }
    var now by remember { mutableLongStateOf(System.currentTimeMillis()) }
    var stopping by remember { mutableStateOf(false) }
    var stopError by remember { mutableStateOf<String?>(null) }
    var savedMinutes by remember { mutableStateOf<Int?>(null) }
    var savedTaskId by remember { mutableStateOf<String?>(null) }

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) dndGranted = FocusMode.hasDndAccess(context)
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }
    DisposableEffect(keepScreenOn) {
        val window = (context as? Activity)?.window
        if (keepScreenOn) window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        onDispose { window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON) }
    }
    LaunchedEffect(session.active, session.startedAt) {
        while (session.active) {
            now = System.currentTimeMillis()
            delay(1_000)
        }
    }
    LaunchedEffect(session.active, session.sessionId) {
        if (session.active) {
            savedMinutes = null
            savedTaskId = null
            stopError = null
        }
    }

    val namesById = remember(subjects) { subjects.associate { it.id to it.name } }
    val subjectOptions = remember(subjects) { subjects.map { FocusSubjectOption(it.id, it.name) } }
    val totalMinutes = history.sumOf { it.durationSeconds.coerceAtLeast(0L) } / 60L
    val elapsedSeconds = if (session.active) ((now - session.startedAt) / 1_000L).coerceAtLeast(0L) else 0L
    val dndActive = wantsDnd && dndGranted

    val contentModifier = if (embedded) {
        Modifier.fillMaxWidth().heightIn(max = 600.dp).verticalScroll(rememberScrollState())
    } else {
        Modifier.fillMaxSize().verticalScroll(rememberScrollState())
    }
    Column(
        modifier = contentModifier.padding(horizontal = estudarioLayout().screenGutter, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp),
    ) {
        if (!embedded) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("Modo foco", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                Text(
                    "Acompanhe seu tempo de estudo. O cronômetro não conclui matérias nem tarefas.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            TextButton(onClick = onHome, modifier = Modifier.fillMaxWidth()) {
                Text("Fechar janela · foco continua")
            }
        }

        if (session.active) {
            Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
                Column(
                    Modifier.fillMaxWidth().padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Text("Você está estudando", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                    // Autoajuste: o relógio fica grande, mas encolhe o quanto for preciso para caber em
                    // uma linha (com fonte maior "01:23:45" quebrava no meio em celular estreito).
                    BasicText(
                        relogio(elapsedSeconds),
                        style = TextStyle(fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary, textAlign = TextAlign.Center),
                        maxLines = 1,
                        autoSize = TextAutoSize.StepBased(minFontSize = 28.sp, maxFontSize = 48.sp),
                    )
                    Text(session.title.ifBlank { "Sessão de estudo" }, style = MaterialTheme.typography.titleSmall, textAlign = TextAlign.Center)
                    val sessionSubjects = session.subjectIds.map { namesById[it] ?: "Matéria removida" }
                    if (sessionSubjects.isNotEmpty()) {
                        Text(sessionSubjects.joinToString(" · "), style = MaterialTheme.typography.bodyMedium, textAlign = TextAlign.Center)
                    }
                    Text(
                        when (session.origin) {
                            FocusSessionOrigin.PLANO -> "Sessão ligada ao plano"
                            FocusSessionOrigin.MATERIA -> "Sessão ligada a uma matéria"
                            FocusSessionOrigin.LIVRE -> "Sessão livre"
                        },
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Linha(
                        if (dndActive) Icons.Outlined.DoNotDisturbOn else Icons.Outlined.NotificationsOff,
                        if (dndActive) "Não perturbe ligado" else "Não perturbe desligado",
                        if (dndActive) "O Android volta ao filtro anterior quando você encerra."
                        else if (!wantsDnd) "Você desligou essa opção nas notificações."
                        else "O cronômetro continua mesmo sem o acesso ao Não Perturbe.",
                    )
                    if (wantsDnd && !dndGranted) {
                        OutlinedButton(onClick = { context.startActivity(FocusMode.dndSettingsIntent()) }, Modifier.fillMaxWidth()) {
                            Text("Conceder acesso ao Não Perturbe")
                        }
                    }
                    if (keepScreenOn) Linha(Icons.Outlined.Lightbulb, "Tela ligada", "A tela fica acesa enquanto esta tela estiver aberta.")
                    stopError?.let { Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall) }
                    Button(
                        enabled = !stopping,
                        onClick = {
                            if (stopping) return@Button
                            stopping = true
                            stopError = null
                            val taskId = session.taskId
                            scope.launch {
                                runCatching { viewModel.stopFocus() }
                                    .onSuccess { minutes -> savedMinutes = minutes; savedTaskId = taskId }
                                    .onFailure { stopError = "Não foi possível salvar esta sessão. Ela continua ativa; tente encerrar novamente." }
                                stopping = false
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                    ) { Text(if (stopping) "Salvando sessão…" else "Encerrar sessão") }
                }
            }
        } else {
            savedMinutes?.let { minutes ->
                Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)) {
                    Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Sessão salva no histórico", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                        Text("Você estudou ${formatMinutes(minutes)}. Seu tempo foi registrado sem concluir matéria ou tarefa automaticamente.")
                        if (savedTaskId != null) {
                            OutlinedButton(onClick = onOpenPlan, modifier = Modifier.fillMaxWidth()) {
                                Text("Abrir o Plano para concluir a tarefa")
                            }
                        }
                    }
                }
            }
            Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow)) {
                Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    Text("O que você vai estudar?", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                    Text(
                        "Escolha uma ou mais matérias para aparecer no acompanhamento. Sem seleção, o foco fica livre.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    if (subjectOptions.isEmpty()) {
                        Text("Ainda não há matérias cadastradas. Você pode adicionar matérias pelo Edital ou iniciar uma sessão livre agora.")
                    } else {
                        FocusSubjectPicker(subjectOptions, selectedIds) { id ->
                            selectedIds = if (id in selectedIds) selectedIds - id else selectedIds + id
                        }
                    }
                    Button(
                        onClick = {
                            val selectedNames = subjects.filter { it.id in selectedIds }.map { it.name }
                            val title = selectedNames.joinToString(" · ").ifBlank { "Estudo livre" }
                            viewModel.startFocus(
                                title = title,
                                subjectIds = selectedIds,
                                origin = FocusSessionOrigin.LIVRE,
                            )
                            savedMinutes = null
                            stopError = null
                        },
                        modifier = Modifier.fillMaxWidth(),
                    ) { Text("Iniciar foco") }
                }
            }
        }

        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                androidx.compose.material3.Icon(Icons.Outlined.History, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Text("Histórico de foco", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            }
            Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow)) {
                Row(Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f)) {
                        Text("${history.size} sessões", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                        Text("Tempo total de foco", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Text(formatLongMinutes(totalMinutes), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                }
            }
            if (history.isEmpty()) {
                Text("As sessões encerradas aparecem aqui com duração, matéria e origem.", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            } else {
                history.take(12).forEachIndexed { index, item ->
                    FocusHistoryRow(item, namesById)
                    if (index < history.take(12).lastIndex) HorizontalDivider()
                }
            }
        }
        Spacer(Modifier.height(8.dp))
    }
}

@Composable
internal fun FocusHistoryRow(session: FocusSessionEntity, namesById: Map<Long, String>) {
    val subjects = session.subjectIdsText.split(',').mapNotNull(String::toLongOrNull).map { namesById[it] ?: "Matéria removida" }
    val source = when (session.origin) {
        FocusSessionOrigin.LIVRE -> if (subjects.isEmpty()) "Sessão livre" else "Foco livre · matérias selecionadas"
        FocusSessionOrigin.MATERIA -> "Matéria"
        FocusSessionOrigin.PLANO -> "Plano"
    }
    val date = remember(session.completedAt) {
        DateTimeFormatter.ofPattern("dd MMM yyyy · HH:mm", Locale("pt", "BR"))
            .withZone(ZoneId.systemDefault())
            .format(Instant.ofEpochMilli(session.completedAt))
    }
    Row(Modifier.fillMaxWidth().padding(vertical = 12.dp), horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.Top) {
        Surface(shape = CircleShape, color = MaterialTheme.colorScheme.secondaryContainer, modifier = Modifier.size(38.dp)) {
            Box(contentAlignment = Alignment.Center) {
                androidx.compose.material3.Icon(Icons.Outlined.Timer, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            }
        }
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
            Text(session.title.ifBlank { "Sessão de foco" }, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold)
            Text(date, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text((listOf(source) + subjects).joinToString(" · "), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Text(formatDuration(session.durationSeconds), style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun Linha(icon: ImageVector, titulo: String, apoio: String) {
    Row(verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        androidx.compose.material3.Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
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

internal fun formatDuration(seconds: Long): String {
    val minutes = seconds.coerceAtLeast(0L) / 60L
    return when {
        minutes == 0L -> "< 1 min"
        minutes >= 60L -> "${minutes / 60} h ${minutes % 60} min"
        else -> "$minutes min"
    }
}

private fun formatMinutes(minutes: Int): String = if (minutes >= 60) "${minutes / 60} h ${minutes % 60} min" else "$minutes min"
internal fun formatLongMinutes(minutes: Long): String = if (minutes >= 60) "${minutes / 60} h ${minutes % 60} min" else "$minutes min"
