package br.com.estudario.ui.ai

import androidx.compose.foundation.background
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.IconButton
import androidx.compose.material3.Button
import androidx.compose.material.icons.outlined.Login
import androidx.compose.material.icons.outlined.Close
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.OfflineBolt
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.estudario.EstudarioApplication
import br.com.estudario.data.ai.AiFeature
import br.com.estudario.ui.components.SkeletonBlock
import br.com.estudario.ui.theme.estudarioColors

@Composable
fun AiAccessPanel(modifier: Modifier = Modifier, dismissible: Boolean = true) {
    val app = LocalContext.current.applicationContext as EstudarioApplication
    val accessViewModel: AiAccessViewModel = viewModel(factory = AiAccessViewModel.Factory(app.aiAccessRepository))
    val state by accessViewModel.state.collectAsState()
    val prefs = remember { app.getSharedPreferences(PANEL_PREFS, android.content.Context.MODE_PRIVATE) }
    var dismissed by remember { mutableStateOf(dismissible && prefs.getBoolean(PANEL_DISMISSED, false)) }
    var signedIn by remember { mutableStateOf(app.supabaseAuthRepository.accessToken() != null) }
    var loginOpen by remember { mutableStateOf(false) }
    var refreshTick by remember { mutableStateOf(0) }
    LaunchedEffect(accessViewModel, refreshTick) { accessViewModel.refresh() }
    if (loginOpen) AccountLoginDialog(
        onDismiss = { loginOpen = false },
        onSignedIn = { loginOpen = false; signedIn = true; refreshTick++ },
    )
    if (dismissed) return
    AiAccessSummary(
        state,
        modifier,
        onLogin = if (signedIn) null else ({ loginOpen = true }),
        onDismiss = if (dismissible) ({ dismissed = true; prefs.edit().putBoolean(PANEL_DISMISSED, true).apply() }) else null,
    )
}

private const val PANEL_PREFS = "estudario_ui"
private const val PANEL_DISMISSED = "ai_access_panel_dismissed"

/**
 * O que a conta tem de IA agora, recurso por recurso. Cada linha diz o recurso, a cota em uma
 * frase e um selo de estado com cor: verde quando dá pra usar, âmbar quando falta um passo da
 * pessoa (entrar, esperar a geração em andamento) e neutro quando não depende dela.
 */
@Composable
fun AiAccessSummary(
    state: AiAccessUiState,
    modifier: Modifier = Modifier,
    onLogin: (() -> Unit)? = null,
    onDismiss: (() -> Unit)? = null,
) {
    Surface(
        modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.surfaceContainerLow,
    ) {
        Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                AiBadge(Icons.Outlined.AutoAwesome)
                Column(Modifier.weight(1f)) {
                    Text("Gerações do Estudário", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                    Text("O que sua conta pode usar agora", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                BetaPill()
                if (onDismiss != null) IconButton(onClick = onDismiss, modifier = Modifier.size(32.dp)) {
                    Icon(Icons.Outlined.Close, "Fechar aviso", Modifier.size(18.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            if (onLogin != null) {
                Button(onClick = onLogin, modifier = Modifier.fillMaxWidth()) {
                    Icon(Icons.Outlined.Login, null, Modifier.size(18.dp)); Spacer(Modifier.width(8.dp)); Text("Entrar na conta Estudário")
                }
            }
            if (state.items.isEmpty()) {
                repeat(3) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        SkeletonBlock(Modifier.size(36.dp), height = 36, cornerRadius = 18)
                        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            SkeletonBlock(Modifier.fillMaxWidth(0.4f), height = 14)
                            SkeletonBlock(Modifier.fillMaxWidth(0.6f), height = 18, cornerRadius = 9)
                        }
                    }
                }
            }
            // O plano de estudo saiu do app: não aparece mais na lista do que está disponível.
            AiFeature.entries.filter { it != AiFeature.PLAN_GENERATION }.forEach { feature ->
                val display = state.items[feature] ?: return@forEach
                val (label, icon) = when (feature) {
                    AiFeature.SYLLABUS_GENERATION -> "Edital" to Icons.Outlined.Description
                    AiFeature.PLAN_GENERATION -> "Plano de estudo" to Icons.Outlined.CalendarMonth
                    AiFeature.CONTENT_GENERATION -> "Conteúdo" to Icons.Outlined.MenuBook
                    AiFeature.SIMULATION_GENERATION -> "Simulados" to Icons.Outlined.MenuBook
                }
                FeatureRow(label, icon, display)
            }
            if (state.localFallbackAvailable) {
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f))
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Icon(Icons.Outlined.OfflineBolt, null, Modifier.size(18.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    Column {
                        Text("Sempre disponível, sem internet", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("Importar .estudo ou montar manualmente", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface)
                    }
                }
            }
        }
    }
}

@Composable
private fun FeatureRow(label: String, icon: ImageVector, display: AiFeatureDisplay) {
    val tone = display.tone()
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        Box(
            Modifier.size(36.dp).clip(CircleShape).background(tone.container),
            contentAlignment = Alignment.Center,
        ) { Icon(icon, null, Modifier.size(19.dp), tint = tone.content) }
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
            Text(label, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
            StatusPill(display.availabilityCopy, tone)
            if (display.quotaCopy.isNotEmpty()) Text(display.quotaCopy, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

internal data class StatusTone(val container: Color, val content: Color)

@Composable
private fun AiFeatureDisplay.tone(): StatusTone {
    val scheme = MaterialTheme.colorScheme
    return when {
        canUse -> StatusTone(scheme.secondaryContainer, estudarioColors().completed)
        availabilityCopy.startsWith("Entre") || availabilityCopy.contains("andamento") ->
            StatusTone(scheme.tertiaryContainer, scheme.onTertiaryContainer)
        else -> StatusTone(scheme.surfaceContainerHighest, scheme.onSurfaceVariant)
    }
}

@Composable
internal fun StatusPill(text: String, tone: StatusTone, modifier: Modifier = Modifier) {
    Row(
        modifier.clip(RoundedCornerShape(50)).background(tone.container).padding(horizontal = 10.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Box(Modifier.size(6.dp).clip(CircleShape).background(tone.content))
        Text(text, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Medium, color = tone.content)
    }
}

/** Selo "Beta" com o degradê da IA. */
@Composable
internal fun BetaPill(modifier: Modifier = Modifier) {
    Text(
        "BETA",
        modifier
            .clip(RoundedCornerShape(50))
            .background(aiGradient())
            .padding(horizontal = 9.dp, vertical = 3.dp),
        style = MaterialTheme.typography.labelSmall,
        fontWeight = FontWeight.Bold,
        color = Color.White,
    )
}

/** Ícone da IA dentro de um quadrado arredondado com o degradê da marca. */
@Composable
internal fun AiBadge(icon: ImageVector, modifier: Modifier = Modifier, size: Int = 40) {
    Box(
        modifier.size(size.dp).clip(RoundedCornerShape((size * 0.32f).dp)).background(aiGradient()),
        contentAlignment = Alignment.Center,
    ) { Icon(icon, null, Modifier.size((size * 0.52f).dp), tint = Color.White) }
}

/** Degradê índigo → violeta que identifica tudo o que é IA no app. */
@Composable
internal fun aiGradient(): Brush = Brush.linearGradient(
    listOf(Color(0xFF4F46E5), Color(0xFF7C4DFF), Color(0xFFA855F7)),
)
