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
import br.com.estudario.ui.brand.LinearProgressIndicator
import br.com.estudario.ui.brand.Surface
import br.com.estudario.ui.brand.Button
import br.com.estudario.ui.brand.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.border
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
import br.com.estudario.EstudarioApplication
import br.com.estudario.ui.ai.AiAccessSummary
import br.com.estudario.ui.plans.PlansDialog
import androidx.compose.material.icons.outlined.WorkspacePremium
import androidx.compose.material.icons.outlined.Login
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.VerifiedUser
import br.com.estudario.ui.ai.AiAccessUiState
import br.com.estudario.ui.ai.AiAccessViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
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
    val aiAccessViewModel: AiAccessViewModel = viewModel(factory = AiAccessViewModel.Factory((context.applicationContext as EstudarioApplication).aiAccessRepository))
    val aiAccessState by aiAccessViewModel.state.collectAsState()
    val auth = (context.applicationContext as EstudarioApplication).supabaseAuthRepository
    val sessionState by remember(auth) { auth.observeSession() }.collectAsState(br.com.estudario.data.remote.SupabaseSessionState.Loading)
    val estudarioSignedIn = (sessionState as? br.com.estudario.data.remote.SupabaseSessionState.Ready)?.session != null
    var accessRefresh by remember { mutableIntStateOf(0) }
    LaunchedEffect(aiAccessViewModel, accessRefresh, estudarioSignedIn) { aiAccessViewModel.refresh() }
    var estudarioLogin by remember { mutableStateOf(false) }
    var reopenPlansAfterLogin by remember { mutableStateOf(false) }
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
    // Entrar é um passo só: a conta Google do celular já abre a conta Estudário e o perfil.
    fun signInWithGoogle(reopenPlans: Boolean = false) {
        if (!br.com.estudario.ui.ai.GoogleAccountSignIn.available) { reopenPlansAfterLogin = reopenPlans; estudarioLogin = true; return }
        scope.launch {
            busy = "Entrando com o Google"
            br.com.estudario.ui.ai.GoogleAccountSignIn.signIn(context)
                .onSuccess { reopenPlansAfterLogin = reopenPlans }
                .onFailure { viewModel.reportGoogleError(it.message ?: "Não foi possível entrar agora.") }
            busy = null
            accessRefresh++
        }
    }

    var avatarSheet by remember { mutableStateOf(false) }
    val pickPhoto = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        uri?.let(viewModel::setProfilePhoto)
    }
    if (avatarSheet) AvatarPickerSheet(
        current = profile.photoPath,
        onPick = { viewModel.setProfileAvatar(it.path); avatarSheet = false },
        onGallery = { avatarSheet = false; pickPhoto.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) },
        onRemove = { viewModel.clearProfilePhoto(); avatarSheet = false },
        onDismiss = { avatarSheet = false },
    )
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

    if (estudarioLogin) br.com.estudario.ui.ai.AccountLoginDialog(
        onDismiss = { estudarioLogin = false; reopenPlansAfterLogin = false },
        onSignedIn = { estudarioLogin = false; accessRefresh++ },
    )

    ProfileContent(
        profile = profile,
        estudarioSignedIn = estudarioSignedIn,
        reopenPlans = reopenPlansAfterLogin && estudarioSignedIn,
        onPlansReopened = { reopenPlansAfterLogin = false },
        onEstudarioSignIn = { signInWithGoogle() },
        onPlansSignIn = { signInWithGoogle(reopenPlans = true) },
        onEstudarioSignOut = { scope.launch { br.com.estudario.ui.ai.GoogleAccountSignIn.signOut(context); accessRefresh++ } },
        progress = progress,
        streak = streak,
        driveLastBackupAt = driveLastBackupAt,
        aiAccessState = aiAccessState,
        showTopBar = showInternalTopBar,
        onBack = onBack,
        onEditName = { editName = true },
        onChangePhoto = { avatarSheet = true },
        onRemovePhoto = viewModel::clearProfilePhoto,
        onBadges = onBadges,
        onDailyGoalChange = viewModel::setDailyGoal,
        onBackup = { runGoogle(GoogleAction.BACKUP) },
        onRestore = { confirmDriveRestore = true },
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
    estudarioSignedIn: Boolean = false,
    reopenPlans: Boolean = false,
    onPlansReopened: () -> Unit = {},
    onEstudarioSignIn: () -> Unit = {},
    onPlansSignIn: () -> Unit = {},
    onEstudarioSignOut: () -> Unit = {},
    progress: ProgressEngine.ProgressSummary?,
    streak: StreakSummary?,
    driveLastBackupAt: Long,
    aiAccessState: AiAccessUiState? = null,
    onBack: () -> Unit,
    onEditName: () -> Unit,
    onChangePhoto: () -> Unit,
    onRemovePhoto: () -> Unit,
    onBadges: () -> Unit,
    onDailyGoalChange: (Int) -> Unit,
    onBackup: () -> Unit,
    onRestore: () -> Unit,
    onExportBackup: () -> Unit,
    onImportBackup: () -> Unit,
    showTopBar: Boolean = true,
) {
    var showPlans by remember { mutableStateOf(false) }
    // Depois de entrar pela tela de planos, ela reabre já com o saldo da conta.
    LaunchedEffect(reopenPlans) { if (reopenPlans) { showPlans = true; onPlansReopened() } }
    if (showPlans) PlansDialog(onDismiss = { showPlans = false }, onSignIn = { showPlans = false; onPlansSignIn() })
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
            item {
                Box(
                    Modifier.fillMaxWidth().clip(br.com.estudario.ui.theme.EstudarioShapes.spotlight).background(
                        androidx.compose.ui.graphics.Brush.verticalGradient(
                            listOf(MaterialTheme.colorScheme.primaryContainer, MaterialTheme.colorScheme.surfaceContainerLow),
                        ),
                    ).padding(vertical = EstudarioSpacing.large, horizontal = EstudarioSpacing.medium),
                ) {
                    ProfileHeader(profile, progress?.level, progress?.levelTitle, onEditName, onChangePhoto, onRemovePhoto)
                }
            }
            item { Spacer(Modifier.height(EstudarioSpacing.medium)) }

            item {
                val current = progress
                if (current == null) SkeletonCard(lines = 3) else LevelBand(current)
            }
            item { Divider() }

            item {
                val current = streak
                if (current == null) SkeletonCard(lines = 3) else StreakBand(current)
            }
            item { Divider() }

            item {
                if (streak == null) SkeletonCard(lines = 2) else Band {
                    Text("FREQUÊNCIA DE ESTUDO", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(Modifier.height(EstudarioSpacing.small))
                    // O mapa já traz seu próprio recuo horizontal (contentPadding), não soma outro.
                    ActivityHeatmap(streak.calendar, streak.goal, contentPadding = PaddingValues(0.dp))
                    Row(Modifier.fillMaxWidth().padding(top = EstudarioSpacing.medium), horizontalArrangement = Arrangement.SpaceBetween) {
                        Stat("Dias com estudo", streak.activeDays.toString())
                        Stat("Questões", streak.totalQuestions.toString())
                        Stat("Tempo", "${streak.totalMinutes / 60}h${streak.totalMinutes % 60}")
                    }
                }
            }
            item { Divider() }

            item {
                val current = progress
                if (current == null) SkeletonCard(lines = 2) else BadgesBand(current, onBadges)
            }
            item { Divider() }

            item {
                val current = streak
                if (current != null) RewardTableBand(current.current)
            }
            item { Divider() }

            item { DailyGoalBand(profile.dailyGoal.questions, onDailyGoalChange) }
            item { Divider() }

            item {
                AccountBand(
                    signedIn = estudarioSignedIn,
                    email = profile.email,
                    lastBackupAt = driveLastBackupAt,
                    onSignIn = onEstudarioSignIn,
                    onBackup = onBackup,
                    onRestore = onRestore,
                    onPlans = { showPlans = true },
                    onSignOut = onEstudarioSignOut,
                )
            }
            if (aiAccessState != null) {
                item { Spacer(Modifier.height(EstudarioSpacing.small)); AiAccessSummary(aiAccessState) }
            }
            item { Divider() }

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

/** Cada seção do Perfil é um cartão, no mesmo estilo dos painéis do resto do app. */
@Composable
private fun Band(content: @Composable ColumnScope.() -> Unit) {
    Surface(
        Modifier.fillMaxWidth(),
        shape = br.com.estudario.ui.theme.EstudarioShapes.panel,
        color = MaterialTheme.colorScheme.surfaceContainerLow,
    ) {
        Column(Modifier.padding(18.dp), content = content)
    }
}

/** Espaço entre os cartões (antes era uma linha divisória entre faixas). */
@Composable
private fun Divider() {
    Spacer(Modifier.height(EstudarioSpacing.medium))
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
        Spacer(Modifier.height(EstudarioSpacing.small))
        br.com.estudario.ui.prompt.BigValueSlider(
            value = questions,
            range = 5..100,
            step = 5,
            format = { "$it" },
            onChange = onChange,
            caption = "questões por dia",
            quickValues = listOf(10, 20, 30, 50),
        )
    }
}

// ---------------------------------------------------------------- conta

/**
 * Sua conta: uma só, a do Google. Entrar libera a IA do Estudário e o plano, preenche o perfil e
 * guarda o backup no Drive, sem logins separados para cada coisa.
 */
@Composable
private fun AccountBand(
    signedIn: Boolean,
    email: String,
    lastBackupAt: Long,
    onSignIn: () -> Unit,
    onBackup: () -> Unit,
    onRestore: () -> Unit,
    onPlans: () -> Unit,
    onSignOut: () -> Unit,
) {
    Band {
        Text("SUA CONTA", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(EstudarioSpacing.small))
        if (!signedIn) {
            Text("Entre com o Google", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)
            Spacer(Modifier.height(EstudarioSpacing.tight))
            Text(
                "Uma conta para tudo: libera as gerações do Estudário, guarda seu plano e faz o backup do seu estudo no seu Google Drive. Sem senha e sem código por e-mail.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(Modifier.height(EstudarioSpacing.medium))
            Button(onClick = onSignIn, modifier = Modifier.fillMaxWidth()) {
                Text("G", fontWeight = androidx.compose.ui.text.font.FontWeight.Bold); Spacer(Modifier.width(10.dp)); Text("Continuar com Google")
            }
            Spacer(Modifier.height(EstudarioSpacing.tight))
            Text("Sem entrar, o app funciona inteiro neste aparelho, só sem as gerações e sem backup na nuvem.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        } else {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(EstudarioSpacing.small)) {
                Icon(Icons.Outlined.VerifiedUser, null, tint = estudarioColors().completed)
                Column(Modifier.weight(1f)) {
                    Text("Conectado com Google", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)
                    Text(email.ifBlank { "Conta Google" }, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            HorizontalDivider(Modifier.padding(vertical = EstudarioSpacing.small), color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
            BackupRow(Icons.Outlined.WorkspacePremium, "Plano e gerações", "Seu plano, o saldo de gerações e a comparação dos planos", onPlans)
            HorizontalDivider(Modifier.padding(vertical = EstudarioSpacing.small), color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
            BackupRow(
                Icons.Outlined.CloudUpload,
                "Fazer backup no Google Drive",
                if (lastBackupAt > 0L) "Último backup em ${formatMoment(lastBackupAt)}" else "Nenhum backup ainda. Fica numa pasta privada do app no seu Drive.",
                onBackup,
            )
            HorizontalDivider(Modifier.padding(vertical = EstudarioSpacing.small), color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
            BackupRow(Icons.Outlined.CloudDownload, "Restaurar do Google Drive", "Traz o backup mais recente para este aparelho", onRestore)
            Spacer(Modifier.height(EstudarioSpacing.small))
            TextButton(onClick = onSignOut) { Text("Sair da conta") }
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
            onDailyGoalChange = {}, onBackup = {}, onRestore = {},
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
            onDailyGoalChange = {}, onBackup = {}, onRestore = {},
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
            onDailyGoalChange = {}, onBackup = {}, onRestore = {},
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
            onDailyGoalChange = {}, onBackup = {}, onRestore = {},
            onExportBackup = {}, onImportBackup = {},
        )
    }
}

/** Escolher a imagem do perfil: um dos avatares do Estudário ou uma foto da galeria. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AvatarPickerSheet(current: String?, onPick: (br.com.estudario.ui.components.PresetAvatar) -> Unit, onGallery: () -> Unit, onRemove: () -> Unit, onDismiss: () -> Unit) {
    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(Modifier.fillMaxWidth().padding(horizontal = 20.dp).padding(bottom = 28.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Text("Sua imagem no Estudário", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black)
            Text("Escolha um dos nossos personagens ou use uma foto sua.", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            androidx.compose.foundation.layout.FlowRow(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp, Alignment.CenterHorizontally),
                verticalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                br.com.estudario.ui.components.PresetAvatar.entries.forEach { avatar ->
                    val chosen = current == avatar.path
                    Column(
                        Modifier.clip(RoundedCornerShape(18.dp)).clickable { onPick(avatar) }.padding(6.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                    ) {
                        Box(
                            Modifier.size(78.dp).then(
                                if (chosen) Modifier.border(3.dp, MaterialTheme.colorScheme.primary, CircleShape).padding(4.dp) else Modifier,
                            ),
                            contentAlignment = Alignment.Center,
                        ) { br.com.estudario.ui.components.PresetAvatarArt(avatar, 70.dp) }
                        Text(avatar.label, style = MaterialTheme.typography.labelMedium, fontWeight = if (chosen) FontWeight.Black else FontWeight.SemiBold)
                    }
                }
            }
            OutlinedButton(onClick = onGallery, modifier = Modifier.fillMaxWidth()) {
                Icon(Icons.Outlined.PhotoCamera, null, Modifier.size(20.dp)); Spacer(Modifier.width(8.dp)); Text("Usar uma foto da galeria")
            }
            if (current != null) TextButton(onClick = onRemove, modifier = Modifier.fillMaxWidth()) { Text("Remover imagem") }
        }
    }
}
