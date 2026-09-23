package br.com.estudario.ui.profile

import br.com.estudario.ui.theme.estudarioLayout
import br.com.estudario.BuildConfig
import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.ChevronRight
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import android.content.res.Configuration
import br.com.estudario.domain.BadgeProgress
import br.com.estudario.domain.ProgressEngine
import br.com.estudario.domain.StreakDay
import br.com.estudario.domain.StreakSummary
import br.com.estudario.ui.AppViewModel
import br.com.estudario.ui.components.ActivityHeatmap
import br.com.estudario.ui.components.ConfirmDialog
import br.com.estudario.ui.components.LoadingDialog
import br.com.estudario.ui.components.ProfileAvatar
import br.com.estudario.ui.components.SkeletonCard
import br.com.estudario.ui.components.TextInputDialog
import br.com.estudario.ui.components.XpTag
import br.com.estudario.ui.theme.EstudarioMotion
import br.com.estudario.ui.theme.EstudarioSpacing
import br.com.estudario.ui.theme.EstudarioTheme
import br.com.estudario.ui.theme.estudarioColors
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId

/**
 * O Perfil: quem estuda, com que constância, em que nível e onde os dados estão guardados.
 *
 * A tela é editorial, não uma fileira de [ElevatedCard], cada seção é uma faixa com um rótulo
 * discreto (NÍVEL, SEQUÊNCIA, CONTA…), separada por [br.com.estudario.ui.screens.home.HomeDivider].
 * Cartão só aparece onde há mesmo uma lista de itens tocáveis (emblemas, backup). Toda a lógica de
 * estado fica aqui, em [ProfileScreen]; [ProfileContent] só recebe dados prontos, é o que torna as
 * previews possíveis sem um [AppViewModel] de verdade.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: AppViewModel,
    onBack: () -> Unit,
    onBadges: () -> Unit = {},
    showInternalTopBar: Boolean = true,
) {
    val profile by viewModel.profile.collectAsState()
    val streak by viewModel.streak.collectAsState()
    val progress by viewModel.progress.collectAsState()
    val context = LocalContext.current
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

    ProfileContent(
        profile = profile,
        progress = progress,
        streak = streak,
        driveLastBackupAt = driveLastBackupAt,
        showTopBar = showInternalTopBar,
        onBack = onBack,
        onEditName = { editName = true },
        onChangePhoto = { pickPhoto.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) },
        onRemovePhoto = viewModel::clearProfilePhoto,
        onBadges = onBadges,
        onDailyGoalChange = viewModel::setDailyGoal,
        onSignIn = { runGoogle(GoogleAction.SIGN_IN) },
        onBackup = { runGoogle(GoogleAction.BACKUP) },
        onRestore = { confirmDriveRestore = true },
        onSignOut = viewModel::signOutGoogle,
        onExportBackup = {
            scope.launch { busy = "Gerando o backup"; pendingBackup = viewModel.createBackup(); busy = null; exportLauncher.launch("estudario-backup-${LocalDate.now()}.json") }
        },
        onImportBackup = { restoreLauncher.launch(arrayOf("application/json", "application/octet-stream")) },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProfileContent(
    profile: UserProfile,
    progress: ProgressEngine.ProgressSummary?,
    streak: StreakSummary?,
    driveLastBackupAt: Long,
    onBack: () -> Unit,
    onEditName: () -> Unit,
    onChangePhoto: () -> Unit,
    onRemovePhoto: () -> Unit,
    onBadges: () -> Unit,
    onDailyGoalChange: (Int) -> Unit,
    onSignIn: () -> Unit,
    onBackup: () -> Unit,
    onRestore: () -> Unit,
    onSignOut: () -> Unit,
    onExportBackup: () -> Unit,
    onImportBackup: () -> Unit,
    showTopBar: Boolean = true,
) {
    Scaffold(
        topBar = {
            if (showTopBar) TopAppBar(
                title = { Text("Perfil") },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Outlined.ArrowBack, "Voltar") } },
            )
        },
    ) { padding ->
        LazyColumn(
            Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(horizontal = EstudarioSpacing.screenGutter, vertical = EstudarioSpacing.medium),
        ) {
            item { ProfileHeader(profile, progress?.level, progress?.levelTitle, onEditName, onChangePhoto, onRemovePhoto) }
            item { Spacer(Modifier.height(EstudarioSpacing.section)) }

            item {
                val current = progress
                if (current == null) SkeletonCard(lines = 3) else LevelBand(current)
            }
            item { Band { Divider() } }

            item {
                val current = streak
                if (current == null) SkeletonCard(lines = 3) else StreakBand(current)
            }
            item { Band { Divider() } }

            item {
                Text("FREQUÊNCIA DE ESTUDO", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(Modifier.height(EstudarioSpacing.small))
            }
            if (streak == null) {
                item { SkeletonCard(lines = 2) }
            } else {
                item {
                    // O mapa já traz seu próprio recuo horizontal (contentPadding), não soma outro
                    // aqui, senão o início da grade fica cortado atrás da margem da tela.
                    ActivityHeatmap(streak.calendar, streak.goal, contentPadding = PaddingValues(0.dp))
                }
                item {
                    Row(Modifier.fillMaxWidth().padding(top = EstudarioSpacing.medium), horizontalArrangement = Arrangement.SpaceBetween) {
                        Stat("Dias com estudo", streak.activeDays.toString())
                        Stat("Questões", streak.totalQuestions.toString())
                        Stat("Tempo", "${streak.totalMinutes / 60}h${streak.totalMinutes % 60}")
                    }
                }
            }
            item { Band { Divider() } }

            item {
                val current = progress
                if (current == null) SkeletonCard(lines = 2) else BadgesBand(current, onBadges)
            }
            item { Band { Divider() } }

            item {
                val current = streak
                if (current != null) RewardTableBand(current.current)
            }
            item { Band { Divider() } }

            item { DailyGoalBand(profile.dailyGoal.questions, onDailyGoalChange) }
            item { Band { Divider() } }

            item {
                AccountBand(
                    signedIn = profile.signedIn,
                    email = profile.email,
                    lastBackupAt = driveLastBackupAt,
                    onSignIn = onSignIn,
                    onBackup = onBackup,
                    onRestore = onRestore,
                    onSignOut = onSignOut,
                )
            }
            item { Band { Divider() } }

            item {
                Band {
                    Text("BACKUP EM ARQUIVO", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(Modifier.height(EstudarioSpacing.small))
                    BackupRow(Icons.Outlined.CloudDownload, "Exportar backup completo", "Salve uma cópia versionada no local que escolher", onExportBackup)
                    HorizontalDivider(Modifier.padding(vertical = EstudarioSpacing.small), color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                    BackupRow(Icons.Outlined.Restore, "Restaurar backup", "Substitui os dados deste aparelho após confirmação", onImportBackup)
                }
            }
            item { Spacer(Modifier.height(EstudarioSpacing.expansive)) }
            item {
                Text(
                    "Estudário ${BuildConfig.VERSION_NAME}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

/** Um pequeno wrapper para dar padding vertical consistente a uma seção de conteúdo livre. */
@Composable
private fun Band(content: @Composable ColumnScope.() -> Unit) {
    Column(Modifier.fillMaxWidth(), content = content)
}

@Composable
private fun Divider() {
    HorizontalDivider(
        Modifier.padding(vertical = EstudarioSpacing.large),
        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.55f),
    )
}

// ---------------------------------------------------------------- cabeçalho

@Composable
private fun ProfileHeader(
    profile: UserProfile,
    level: Int?,
    levelTitle: String?,
    onEditName: () -> Unit,
    onChangePhoto: () -> Unit,
    onRemovePhoto: () -> Unit,
) {
    Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(EstudarioSpacing.small)) {
        Box(contentAlignment = Alignment.BottomEnd) {
            ProfileAvatar(profile.photoPath, profile.initials, 108.dp, ring = true)
            Box(
                Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
                    .clickable(onClick = onChangePhoto),
                contentAlignment = Alignment.Center,
            ) { Icon(Icons.Outlined.PhotoCamera, "Trocar foto", Modifier.size(18.dp), tint = MaterialTheme.colorScheme.onPrimary) }
        }
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(EstudarioSpacing.hairline)) {
            Text(profile.displayName, style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.onSurface)
            IconButton(onClick = onEditName, modifier = Modifier.size(28.dp)) { Icon(Icons.Outlined.Edit, "Editar nome", Modifier.size(17.dp)) }
        }
        if (level != null && levelTitle != null) {
            Text(
                "Nível $level · $levelTitle",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary,
            )
        }
        Text(
            if (profile.signedIn) profile.email else "Seus dados estão somente neste dispositivo",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
        if (profile.photoPath != null) TextButton(onClick = onRemovePhoto) { Text("Remover foto") }
    }
}

// ---------------------------------------------------------------- nível

@Composable
private fun LevelBand(summary: ProgressEngine.ProgressSummary) {
    val fraction by animateFloatAsState(summary.levelProgress, EstudarioMotion.progress(), label = "profile-level")
    val colors = estudarioColors()
    Band {
        Text("NÍVEL", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(EstudarioSpacing.tight))
        Row(verticalAlignment = Alignment.Bottom) {
            Text("${summary.level}", style = MaterialTheme.typography.displaySmall, color = MaterialTheme.colorScheme.onSurface)
            Spacer(Modifier.width(EstudarioSpacing.small))
            Column(Modifier.padding(bottom = 2.dp)) {
                Text(summary.levelTitle, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)
                Text("${summary.totalXp} XP no total", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Spacer(Modifier.weight(1f))
            if (summary.xpToday > 0) {
                Column(horizontalAlignment = Alignment.End, modifier = Modifier.padding(bottom = 2.dp)) {
                    Text("+${summary.xpToday}", style = MaterialTheme.typography.titleMedium, color = colors.completed)
                    Text("hoje", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
        Spacer(Modifier.height(EstudarioSpacing.small))
        Box(
            Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(50))
                .background(MaterialTheme.colorScheme.surfaceContainerHighest),
        ) {
            Box(
                Modifier
                    .fillMaxWidth(fraction)
                    .height(6.dp)
                    .clip(RoundedCornerShape(50))
                    .background(MaterialTheme.colorScheme.primary),
            )
        }
        Spacer(Modifier.height(EstudarioSpacing.tight))
        Text(
            "${summary.xpIntoLevel} / ${summary.xpForNextLevel} XP para o nível ${summary.level + 1} · ${summary.xpThisWeek} XP esta semana",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        if (summary.sources.isNotEmpty()) {
            Spacer(Modifier.height(EstudarioSpacing.small))
            Column(verticalArrangement = Arrangement.spacedBy(EstudarioSpacing.hairline)) {
                summary.sources.forEach { source ->
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(source.label, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.weight(1f).padding(end = 8.dp))
                        Text("${source.xp} XP", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface)
                    }
                }
            }
        }
    }
}

// ---------------------------------------------------------------- sequência

@Composable
private fun StreakBand(summary: StreakSummary) {
    val colors = estudarioColors()
    Band {
        Text("SEQUÊNCIA", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(EstudarioSpacing.tight))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column {
                Text("${summary.current}", style = MaterialTheme.typography.displaySmall, color = MaterialTheme.colorScheme.onSurface)
                Text(
                    if (summary.current == 1) "dia de sequência" else "dias de sequência",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Spacer(Modifier.weight(1f))
            Column(horizontalAlignment = Alignment.End) {
                Text("Melhor", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text("${summary.best}", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onSurface)
            }
        }
        Spacer(Modifier.height(EstudarioSpacing.comfortable))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            summary.week.forEach { day -> WeekDot(day, colors.completed) }
        }
        Spacer(Modifier.height(EstudarioSpacing.comfortable))
        if (summary.todayDone) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(EstudarioSpacing.tight)) {
                Icon(Icons.Rounded.Check, null, Modifier.size(18.dp), tint = colors.completed)
                Text("Meta de hoje concluída.", style = MaterialTheme.typography.bodyMedium, color = colors.completed)
            }
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(EstudarioSpacing.tight)) {
                Text(
                    "Faltam ${(summary.goal.questions - summary.todayQuestions).coerceAtLeast(0)} questões hoje, ou 1 tarefa do plano.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                LinearProgressIndicator({ summary.todayProgress }, Modifier.fillMaxWidth().clip(RoundedCornerShape(50)))
            }
        }
    }
}

private val DIAS = listOf("S", "T", "Q", "Q", "S", "S", "D")

@Composable
private fun WeekDot(day: StreakDay, completedColor: Color) {
    val index = day.date.dayOfWeek.value - 1
    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(EstudarioSpacing.hairline)) {
        Text(DIAS.getOrElse(index) { "" }, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Box(
            Modifier
                .size(28.dp)
                .clip(CircleShape)
                .background(
                    when {
                        day.done -> completedColor
                        day.partial -> completedColor.copy(alpha = 0.25f)
                        else -> MaterialTheme.colorScheme.surfaceContainerHighest
                    },
                ),
            contentAlignment = Alignment.Center,
        ) { if (day.done) Icon(Icons.Rounded.Check, null, Modifier.size(16.dp), tint = Color.White) }
    }
}

@Composable
private fun Stat(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)
        Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

// ---------------------------------------------------------------- emblemas

@Composable
private fun BadgesBand(summary: ProgressEngine.ProgressSummary, onBadges: () -> Unit) {
    val conquistados = summary.earnedBadges
    val proximos = summary.nextBadges.take(3)
    Band {
        Row(Modifier.fillMaxWidth().clickable(onClick = onBadges), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text("EMBLEMAS", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(
                    "${conquistados.size} de ${summary.badges.size} conquistados",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
            Icon(Icons.Outlined.ChevronRight, "Ver todos os emblemas", tint = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        if (conquistados.isNotEmpty()) {
            Spacer(Modifier.height(EstudarioSpacing.small))
            Row(horizontalArrangement = Arrangement.spacedBy(EstudarioSpacing.tight)) {
                conquistados.takeLast(5).forEach { row -> BadgeMedal(row.badge, earned = true, size = 50.dp) }
            }
        }
        if (proximos.isNotEmpty()) {
            Spacer(Modifier.height(EstudarioSpacing.medium))
            Text("Quase lá", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
            Spacer(Modifier.height(EstudarioSpacing.small))
            Column(verticalArrangement = Arrangement.spacedBy(EstudarioSpacing.small)) {
                proximos.forEach { row -> ProximoEmblema(row) }
            }
        }
    }
}

@Composable
private fun ProximoEmblema(row: BadgeProgress) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(EstudarioSpacing.small)) {
        BadgeMedal(row.badge, earned = false, size = 42.dp)
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
            Text(row.badge.name, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface)
            LinearProgressIndicator({ row.percent }, Modifier.fillMaxWidth().clip(RoundedCornerShape(50)))
            Text(
                row.hint ?: "faltam ${row.remaining} ${row.badge.unit}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

// ---------------------------------------------------------------- quadro de recompensas

@Composable
private fun RewardTableBand(sequencia: Int) {
    var aberto by remember { mutableStateOf(false) }
    val linhas = remember(sequencia) { ProgressEngine.rewardTable(sequencia) }
    Band {
        Row(
            Modifier.fillMaxWidth().clickable { aberto = !aberto },
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(Modifier.weight(1f)) {
                Text("QUANTO VALE CADA COISA", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(
                    "Tarefa do plano rende mais que questão avulsa, de propósito.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Icon(if (aberto) Icons.Outlined.ExpandLess else Icons.Outlined.ExpandMore, if (aberto) "Recolher" else "Ver tabela")
        }
        if (aberto) {
            Spacer(Modifier.height(EstudarioSpacing.small))
            Column(verticalArrangement = Arrangement.spacedBy(EstudarioSpacing.small)) {
                linhas.forEach { linha ->
                    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(EstudarioSpacing.small)) {
                        Column(Modifier.weight(1f)) {
                            Text(linha.label, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface)
                            Text(linha.detail, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        XpTag(linha.reward)
                    }
                }
            }
            Spacer(Modifier.height(EstudarioSpacing.small))
            Text(
                "Questão fora do plano tem teto de 120 por dia, e o bônus de sequência para de crescer em 10 dias, assim o nível acompanha estudo, não repetição.",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

// ---------------------------------------------------------------- meta do dia

@Composable
private fun DailyGoalBand(questions: Int, onChange: (Int) -> Unit) {
    Band {
        Text("META DO DIA", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(EstudarioSpacing.tight))
        Text(
            "$questions questões por dia, ou concluir uma tarefa do plano, ou fechar uma revisão.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Slider(
            questions.toFloat(),
            { onChange(it.toInt()) },
            valueRange = 5f..100f,
            steps = 18,
        )
    }
}

// ---------------------------------------------------------------- conta

@Composable
private fun AccountBand(
    signedIn: Boolean,
    email: String,
    lastBackupAt: Long,
    onSignIn: () -> Unit,
    onBackup: () -> Unit,
    onRestore: () -> Unit,
    onSignOut: () -> Unit,
) {
    Band {
        Text("CONTA", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(EstudarioSpacing.tight))
        if (!signedIn) {
            Text(
                "Seus dados estão somente neste dispositivo.",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Spacer(Modifier.height(EstudarioSpacing.tight))
            Text(
                "Entrar com o Google guarda um backup na sua conta e permite continuar de onde parou em outro aparelho, quando quiser. O app funciona inteiro sem isso.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(Modifier.height(EstudarioSpacing.medium))
            Button(onClick = onSignIn) { Text("Vincular conta Google") }
        } else {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(EstudarioSpacing.small)) {
                Box(
                    Modifier.size(40.dp).clip(CircleShape).background(MaterialTheme.colorScheme.surfaceContainerHighest),
                    contentAlignment = Alignment.Center,
                ) { Text("G", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface) }
                Column(Modifier.weight(1f)) {
                    Text("Conta Google conectada", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)
                    Text(
                        if (lastBackupAt > 0L) "$email · último backup em ${formatMoment(lastBackupAt)}" else "$email · nenhum backup enviado ainda",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
            Spacer(Modifier.height(EstudarioSpacing.medium))
            // Lado a lado quando cabe; em tela estreita ou fonte grande, um botão por linha.
            FlowRow(Modifier.fillMaxWidth(), maxItemsInEachRow = if (estudarioLayout().prefersStacking) 1 else Int.MAX_VALUE, verticalArrangement = Arrangement.spacedBy(8.dp), horizontalArrangement = Arrangement.spacedBy(EstudarioSpacing.small)) {
                Button(onClick = onBackup, modifier = Modifier.weight(1f)) {
                    Icon(Icons.Outlined.CloudUpload, null, Modifier.size(18.dp)); Spacer(Modifier.width(EstudarioSpacing.hairline)); Text("Backup")
                }
                OutlinedButton(onClick = onRestore, modifier = Modifier.weight(1f)) {
                    Icon(Icons.Outlined.CloudDownload, null, Modifier.size(18.dp)); Spacer(Modifier.width(EstudarioSpacing.hairline)); Text("Restaurar")
                }
            }
            TextButton(onClick = onSignOut) { Text("Desconectar esta conta") }
        }
    }
}

@Composable
private fun BackupRow(icon: androidx.compose.ui.graphics.vector.ImageVector, title: String, subtitle: String, onClick: () -> Unit) {
    Row(
        Modifier.fillMaxWidth().clickable(onClick = onClick).padding(vertical = EstudarioSpacing.tight),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(EstudarioSpacing.small),
    ) {
        Icon(icon, null, tint = MaterialTheme.colorScheme.primary)
        Column(Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onSurface)
            Text(subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Icon(Icons.Outlined.ChevronRight, null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

private fun formatMoment(epochMillis: Long): String {
    val moment = LocalDateTime.ofInstant(Instant.ofEpochMilli(epochMillis), ZoneId.systemDefault())
    return "%02d/%02d às %02d:%02d".format(moment.dayOfMonth, moment.monthValue, moment.hour, moment.minute)
}

private suspend fun readBackupText(context: Context, uri: Uri): String? = withContext(Dispatchers.IO) {
    context.contentResolver.openInputStream(uri)?.bufferedReader()?.use { it.readText() }
}

// ---------------------------------------------------------------- previews

private val previewProgress = ProgressEngine.ProgressSummary(
    totalXp = 1840,
    level = 14,
    levelTitle = "Disciplinado",
    xpIntoLevel = 340,
    xpForNextLevel = 550,
    xpToday = 42,
    xpThisWeek = 210,
    sources = listOf(
        ProgressEngine.XpSource("Tarefas do plano", 980),
        ProgressEngine.XpSource("Metas diárias e sequência", 420),
        ProgressEngine.XpSource("Questões", 280),
        ProgressEngine.XpSource("Revisões", 160),
    ),
)

private val previewStreak = StreakSummary(
    current = 12,
    best = 21,
    todayDone = false,
    todayQuestions = 8,
    goal = br.com.estudario.domain.DailyGoal(20),
    week = (0..6).map { offset ->
        val date = LocalDate.now().minusDays((LocalDate.now().dayOfWeek.value - 1 - offset).toLong())
        StreakDay(date, done = offset < 4, partial = offset == 4, future = offset > 4)
    },
    activeDays = 58,
    totalQuestions = 1240,
    totalMinutes = 4620,
)

/** Nota: em previews o [ProfileAvatar] não decodifica um arquivo real do disco do desenvolvedor,
 * ele cai no mesmo retorno elegante de iniciais que qualquer usuário sem foto veria em produção. */
@Preview(name = "Perfil, com foto", showBackground = true, heightDp = 1400)
@Composable
private fun ProfileWithPhotoPreview() {
    EstudarioTheme {
        ProfileContent(
            profile = UserProfile(name = "Otávio Clemente", email = "otavio@exemplo.com", photoPath = "/data/user/perfil.jpg"),
            progress = previewProgress,
            streak = previewStreak,
            driveLastBackupAt = System.currentTimeMillis(),
            onBack = {}, onEditName = {}, onChangePhoto = {}, onRemovePhoto = {}, onBadges = {},
            onDailyGoalChange = {}, onSignIn = {}, onBackup = {}, onRestore = {}, onSignOut = {},
            onExportBackup = {}, onImportBackup = {},
        )
    }
}

@Preview(name = "Perfil, sem foto", showBackground = true, heightDp = 1400)
@Composable
private fun ProfileNoPhotoPreview() {
    EstudarioTheme {
        ProfileContent(
            profile = UserProfile(name = "Otávio", email = "otavio@exemplo.com"),
            progress = previewProgress,
            streak = previewStreak,
            driveLastBackupAt = 0L,
            onBack = {}, onEditName = {}, onChangePhoto = {}, onRemovePhoto = {}, onBadges = {},
            onDailyGoalChange = {}, onSignIn = {}, onBackup = {}, onRestore = {}, onSignOut = {},
            onExportBackup = {}, onImportBackup = {},
        )
    }
}

@Preview(name = "Perfil, sem conta", showBackground = true, heightDp = 1400)
@Composable
private fun ProfileNoAccountPreview() {
    EstudarioTheme {
        ProfileContent(
            profile = UserProfile(name = "Otávio"),
            progress = previewProgress,
            streak = previewStreak,
            driveLastBackupAt = 0L,
            onBack = {}, onEditName = {}, onChangePhoto = {}, onRemovePhoto = {}, onBadges = {},
            onDailyGoalChange = {}, onSignIn = {}, onBackup = {}, onRestore = {}, onSignOut = {},
            onExportBackup = {}, onImportBackup = {},
        )
    }
}

@Preview(name = "Perfil, sem conta (escuro)", showBackground = true, heightDp = 1400, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ProfileNoAccountDarkPreview() {
    EstudarioTheme {
        ProfileContent(
            profile = UserProfile(name = "Otávio"),
            progress = previewProgress,
            streak = previewStreak,
            driveLastBackupAt = 0L,
            onBack = {}, onEditName = {}, onChangePhoto = {}, onRemovePhoto = {}, onBadges = {},
            onDailyGoalChange = {}, onSignIn = {}, onBackup = {}, onRestore = {}, onSignOut = {},
            onExportBackup = {}, onImportBackup = {},
        )
    }
}
