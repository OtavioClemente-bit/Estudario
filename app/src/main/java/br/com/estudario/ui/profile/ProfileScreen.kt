package br.com.estudario.ui.profile

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.CloudDownload
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.ExpandLess
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material.icons.outlined.Restore
import androidx.compose.material.icons.outlined.CloudUpload
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.estudario.domain.BadgeProgress
import br.com.estudario.domain.ProgressEngine
import br.com.estudario.domain.StreakDay
import br.com.estudario.domain.StreakSummary
import br.com.estudario.ui.AppViewModel
import br.com.estudario.ui.components.ActivityHeatmap
import br.com.estudario.ui.components.AppMark
import br.com.estudario.ui.components.ConfirmDialog
import br.com.estudario.ui.components.LoadingDialog
import br.com.estudario.ui.components.ProfileAvatar
import br.com.estudario.ui.components.SkeletonCard
import br.com.estudario.ui.components.TextInputDialog
import br.com.estudario.ui.components.XpTag
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId

private val Verde = Color(0xFF20B486)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(viewModel: AppViewModel, onBack: () -> Unit, onBadges: () -> Unit = {}) {
    val profile by viewModel.profile.collectAsState()
    val streak by viewModel.streak.collectAsState()
    val progress by viewModel.progress.collectAsState()
    val context = androidx.compose.ui.platform.LocalContext.current
    val scope = rememberCoroutineScope()

    var editName by remember { mutableStateOf(false) }
    var busy by remember { mutableStateOf<String?>(null) }
    var pendingBackup by remember { mutableStateOf<String?>(null) }
    var restoreRaw by remember { mutableStateOf<String?>(null) }
    var confirmDriveRestore by remember { mutableStateOf(false) }
    var googleAction by remember { mutableStateOf(GoogleAction.SIGN_IN) }
    val driveLastBackupAt by viewModel.driveLastBackupAt.collectAsState()
    val authorizeGoogle = rememberGoogleAuthorizer(
        onToken = { token -> viewModel.handleGoogleToken(googleAction, token) },
        onError = viewModel::reportGoogleError,
    )
    fun runGoogle(action: GoogleAction) { googleAction = action; authorizeGoogle() }

    val pickPhoto = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        uri?.let(viewModel::setProfilePhoto)
    }
    val exportLauncher = rememberLauncherForActivityResult(ActivityResultContracts.CreateDocument("application/json")) { uri ->
        val data = pendingBackup
        if (uri != null && data != null) scope.launch(Dispatchers.IO) { context.contentResolver.openOutputStream(uri)?.bufferedWriter()?.use { it.write(data) } }
    }
    val restoreLauncher = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        uri?.let { scope.launch { busy = "Lendo o backup"; restoreRaw = readBackupText(context, it); busy = null } }
    }

    busy?.let { LoadingDialog(it, "Não feche o app até terminar.") }
    if (editName) TextInputDialog("Seu nome", profile.name, "Como quer ser chamado", onDismiss = { editName = false }) { viewModel.setUserName(it) }
    if (restoreRaw != null) ConfirmDialog(
        "Restaurar backup?",
        "Os dados locais atuais serão substituídos pelo conteúdo deste backup. Essa ação não pode ser desfeita.",
        "Restaurar",
        onDismiss = { restoreRaw = null },
    ) { viewModel.restoreBackup(restoreRaw!!); restoreRaw = null }
    if (confirmDriveRestore) ConfirmDialog(
        "Restaurar do Google Drive?",
        "O app vai baixar o backup mais recente da sua conta e substituir os dados deste aparelho. Essa ação não pode ser desfeita.",
        "Restaurar",
        onDismiss = { confirmDriveRestore = false },
    ) { confirmDriveRestore = false; runGoogle(GoogleAction.RESTORE) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Perfil") },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Outlined.ArrowBack, "Voltar") } },
            )
        },
    ) { padding ->
        LazyColumn(
            Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            item {
                Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Box(contentAlignment = Alignment.BottomEnd) {
                        ProfileAvatar(profile.photoPath, profile.initials, 104.dp, ring = true)
                        Box(
                            Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primary)
                                .clickable { pickPhoto.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) },
                            contentAlignment = Alignment.Center,
                        ) { Icon(Icons.Outlined.PhotoCamera, "Trocar foto", Modifier.size(18.dp), tint = MaterialTheme.colorScheme.onPrimary) }
                    }
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(profile.displayName, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                        IconButton(onClick = { editName = true }, modifier = Modifier.size(28.dp)) { Icon(Icons.Outlined.Edit, "Editar nome", Modifier.size(17.dp)) }
                    }
                    Text(
                        if (profile.signedIn) profile.email else "Conta local • seus dados ficam neste aparelho",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                    )
                    if (profile.photoPath != null) TextButton(onClick = viewModel::clearProfilePhoto) { Text("Remover foto") }
                }
            }

            item {
                GoogleAccountCard(
                    signedIn = profile.signedIn,
                    email = profile.email,
                    lastBackupAt = driveLastBackupAt,
                    onSignIn = { runGoogle(GoogleAction.SIGN_IN) },
                    onBackup = { runGoogle(GoogleAction.BACKUP) },
                    onRestore = { confirmDriveRestore = true },
                    onSignOut = viewModel::signOutGoogle,
                )
            }

            item {
                val current = progress
                if (current == null) SkeletonCard(lines = 3) else NivelCard(current)
            }

            item {
                val current = streak
                if (current == null) SkeletonCard(lines = 3) else StreakCard(current)
            }

            item {
                val current = progress
                if (current == null) SkeletonCard(lines = 2) else EmblemasCard(current, onBadges)
            }

            item {
                val current = progress
                if (current != null) QuadroDeRecompensas(current, streak?.current ?: 0)
            }

            item {
                ElevatedCard(Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text("Meta do dia", fontWeight = FontWeight.Bold)
                        Text(
                            "${profile.dailyGoal.questions} questões por dia — ou concluir uma tarefa do plano, ou fechar uma revisão.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        Slider(
                            profile.dailyGoal.questions.toFloat(),
                            { viewModel.setDailyGoal(it.toInt()) },
                            valueRange = 5f..100f,
                            steps = 18,
                        )
                    }
                }
            }

            item {
                // O cartão não tem recuo lateral: a grade precisa da largura inteira para não cortar
                // os dias da semana nem o nome do mês. Cada parte de dentro cuida do próprio recuo.
                ElevatedCard(Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(vertical = 18.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        Text("Frequência de estudo", fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 16.dp))
                        val current = streak
                        if (current == null) Box(Modifier.padding(horizontal = 16.dp)) { SkeletonCard(lines = 2) }
                        else {
                            ActivityHeatmap(current.calendar, current.goal)
                            HorizontalDivider(Modifier.padding(horizontal = 16.dp))
                            Row(Modifier.fillMaxWidth().padding(horizontal = 16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                                Stat("Dias com estudo", current.activeDays.toString())
                                Stat("Questões", current.totalQuestions.toString())
                                Stat("Tempo", "${current.totalMinutes / 60}h${current.totalMinutes % 60}")
                            }
                        }
                    }
                }
            }

            item { Text("Backup em arquivo", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary) }
            item {
                ProfileItem(Icons.Outlined.CloudDownload, "Exportar backup completo", "Salve uma cópia versionada no local que escolher") {
                    scope.launch { busy = "Gerando o backup"; pendingBackup = viewModel.createBackup(); busy = null; exportLauncher.launch("estudario-backup-${LocalDate.now()}.json") }
                }
            }
            item {
                ProfileItem(Icons.Outlined.Restore, "Restaurar backup", "Substitui os dados deste aparelho após confirmação") {
                    restoreLauncher.launch(arrayOf("application/json", "application/octet-stream"))
                }
            }
        }
    }
}

/** O quadro de recompensas: quanto vale cada coisa, da mais generosa para a mais simples. */
@Composable
private fun QuadroDeRecompensas(summary: ProgressEngine.ProgressSummary, sequencia: Int) {
    var aberto by remember { mutableStateOf(false) }
    val linhas = remember(sequencia) { ProgressEngine.rewardTable(sequencia) }
    ElevatedCard(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(
                Modifier.fillMaxWidth().clickable { aberto = !aberto },
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(Modifier.weight(1f)) {
                    Text("Quanto vale cada coisa", fontWeight = FontWeight.Bold)
                    Text(
                        "Tarefa do plano rende mais que questão avulsa — de propósito.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                Icon(if (aberto) Icons.Outlined.ExpandLess else Icons.Outlined.ExpandMore, if (aberto) "Recolher" else "Ver tabela")
            }
            if (aberto) {
                HorizontalDivider()
                linhas.forEach { linha ->
                    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Column(Modifier.weight(1f)) {
                            Text(linha.label, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
                            Text(linha.detail, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        XpTag(linha.reward)
                    }
                }
                Text(
                    "Questão fora do plano tem teto de 120 por dia, e o bônus de sequência para de crescer em 10 dias — assim o nível acompanha estudo, não repetição.",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

/** Nível, barra até o próximo e de onde o XP veio — o plano sempre em primeiro. */
@Composable
private fun NivelCard(summary: ProgressEngine.ProgressSummary) {
    ElevatedCard(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                Box(
                    Modifier.size(58.dp).clip(CircleShape).background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center,
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("${summary.level}", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.onPrimaryContainer)
                        Text("nível", fontSize = 9.sp, color = MaterialTheme.colorScheme.onPrimaryContainer)
                    }
                }
                Column(Modifier.weight(1f)) {
                    Text(summary.levelTitle, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text("${summary.totalXp} XP no total", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("+${summary.xpToday}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Verde)
                    Text("hoje", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            LinearProgressIndicator({ summary.levelProgress }, Modifier.fillMaxWidth().clip(RoundedCornerShape(50)))
            Text(
                "${summary.xpIntoLevel} / ${summary.xpForNextLevel} XP para o nível ${summary.level + 1} • ${summary.xpThisWeek} XP esta semana",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            if (summary.sources.isNotEmpty()) {
                HorizontalDivider()
                summary.sources.forEach { source ->
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(source.label, style = MaterialTheme.typography.bodySmall)
                        Text("${source.xp} XP", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }
}

@Composable
private fun EmblemasCard(summary: ProgressEngine.ProgressSummary, onBadges: () -> Unit) {
    val conquistados = summary.earnedBadges
    val proximos = summary.nextBadges.take(3)
    ElevatedCard(Modifier.fillMaxWidth().clickable(onClick = onBadges)) {
        Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text("Emblemas", fontWeight = FontWeight.Bold)
                    Text("${conquistados.size} de ${summary.badges.size} conquistados", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                TextButton(onClick = onBadges) { Text("Ver todos") }
            }
            if (conquistados.isNotEmpty()) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    conquistados.takeLast(5).forEach { row -> BadgeMedal(row.badge, earned = true, size = 50.dp) }
                }
            }
            if (proximos.isNotEmpty()) {
                HorizontalDivider()
                Text("Quase lá", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
                proximos.forEach { row -> ProximoEmblema(row) }
            }
        }
    }
}

@Composable
private fun ProximoEmblema(row: BadgeProgress) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        BadgeMedal(row.badge, earned = false, size = 42.dp)
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
            Text(row.badge.name, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
            LinearProgressIndicator({ row.percent }, Modifier.fillMaxWidth().clip(RoundedCornerShape(50)))
            Text(
                row.hint ?: "faltam ${row.remaining} ${row.badge.unit}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun StreakCard(summary: StreakSummary) {
    ElevatedCard(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                AppMark(58.dp)
                Column(Modifier.weight(1f)) {
                    Text("${summary.current}", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black)
                    Text(if (summary.current == 1) "dia de sequência" else "dias de sequência", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("Melhor", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("${summary.best}", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                }
            }
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                summary.week.forEach { day -> WeekDot(day) }
            }
            HorizontalDivider()
            if (summary.todayDone) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(Icons.Rounded.Check, null, Modifier.size(18.dp), tint = Verde)
                    Text("Meta de hoje concluída.", fontWeight = FontWeight.SemiBold, color = Verde)
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("Faltam ${(summary.goal.questions - summary.todayQuestions).coerceAtLeast(0)} questões hoje — ou 1 tarefa do plano.", style = MaterialTheme.typography.bodySmall)
                    LinearProgressIndicator(progress = { summary.todayProgress }, Modifier.fillMaxWidth().clip(RoundedCornerShape(50)))
                }
            }
        }
    }
}

private val DIAS = listOf("S", "T", "Q", "Q", "S", "S", "D")

@Composable
private fun WeekDot(day: StreakDay) {
    val index = day.date.dayOfWeek.value - 1
    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(5.dp)) {
        Text(DIAS.getOrElse(index) { "" }, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Box(
            Modifier
                .size(28.dp)
                .clip(CircleShape)
                .background(
                    when {
                        day.done -> Verde
                        day.partial -> Verde.copy(alpha = 0.25f)
                        else -> MaterialTheme.colorScheme.surfaceVariant
                    },
                ),
            contentAlignment = Alignment.Center,
        ) { if (day.done) Icon(Icons.Rounded.Check, null, Modifier.size(16.dp), tint = Color.White) }
    }
}

@Composable
private fun Stat(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun GoogleAccountCard(
    signedIn: Boolean,
    email: String,
    lastBackupAt: Long,
    onSignIn: () -> Unit,
    onBackup: () -> Unit,
    onRestore: () -> Unit,
    onSignOut: () -> Unit,
) {
    ElevatedCard(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                Box(
                    Modifier.size(40.dp).clip(CircleShape).background(MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center,
                ) { Text("G", fontWeight = FontWeight.Black, style = MaterialTheme.typography.titleMedium) }
                Column(Modifier.weight(1f)) {
                    Text(if (signedIn) "Conta Google conectada" else "Entrar com Google", fontWeight = FontWeight.SemiBold)
                    Text(
                        when {
                            signedIn && lastBackupAt > 0L -> "$email • último backup em ${formatMoment(lastBackupAt)}"
                            signedIn -> "$email • nenhum backup enviado ainda"
                            else -> "Guarde o backup numa pasta privada do app no seu Drive e traga tudo de volta em outro aparelho."
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
            if (!signedIn) {
                Button(onClick = onSignIn, modifier = Modifier.fillMaxWidth()) { Text("Entrar com Google") }
            } else {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = onBackup, modifier = Modifier.weight(1f)) {
                        Icon(Icons.Outlined.CloudUpload, null, Modifier.size(18.dp)); Spacer(Modifier.width(6.dp)); Text("Backup")
                    }
                    OutlinedButton(onClick = onRestore, modifier = Modifier.weight(1f)) {
                        Icon(Icons.Outlined.CloudDownload, null, Modifier.size(18.dp)); Spacer(Modifier.width(6.dp)); Text("Restaurar")
                    }
                }
                TextButton(onClick = onSignOut) { Text("Desconectar esta conta") }
            }
        }
    }
}

private fun formatMoment(epochMillis: Long): String {
    val moment = LocalDateTime.ofInstant(Instant.ofEpochMilli(epochMillis), ZoneId.systemDefault())
    return "%02d/%02d às %02d:%02d".format(moment.dayOfMonth, moment.monthValue, moment.hour, moment.minute)
}

@Composable
private fun ProfileItem(icon: androidx.compose.ui.graphics.vector.ImageVector, title: String, subtitle: String, onClick: () -> Unit) {
    ElevatedCard(Modifier.fillMaxWidth().clickable(onClick = onClick)) {
        ListItem(
            headlineContent = { Text(title, fontWeight = FontWeight.SemiBold) },
            supportingContent = { Text(subtitle) },
            leadingContent = { Icon(icon, null) },
        )
    }
}

private suspend fun readBackupText(context: Context, uri: Uri): String? = withContext(Dispatchers.IO) {
    context.contentResolver.openInputStream(uri)?.bufferedReader()?.use { it.readText() }
}
