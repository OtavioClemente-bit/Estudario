package br.com.estudario.ui.ai

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.CloudOff
import androidx.compose.material.icons.outlined.HourglassTop
import androidx.compose.material.icons.outlined.Lock
import br.com.estudario.ui.brand.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import br.com.estudario.ui.brand.OutlinedButton
import br.com.estudario.ui.brand.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import br.com.estudario.ui.components.EstudarioBookLoader
import br.com.estudario.ui.components.EstudarioProcessView
import br.com.estudario.ui.theme.estudarioColors

/** O que a tela mostra antes de gerar: título, promessa e os três pontos do que a IA faz. */
data class AiGenerationCopy(
    val screenTitle: String,
    val heroTitle: String,
    val heroText: String,
    val benefits: List<Pair<ImageVector, String>>,
    val generateLabel: String,
    val processingTitle: String,
    val stages: List<String>,
    val stageMillis: Long,
    val durationHint: String,
    val fallbackLabel: String,
)

/**
 * Tela cheia das gerações de texto da IA do Estudário (conteúdo de tópico, plano). Mesma cara da
 * tela do edital: apresentação sobre o degradê da IA, estado da conta e da cota com selo, e a cena
 * de processamento enquanto a IA trabalha.
 */
@Composable
fun AiGenerationScreen(
    target: String,
    copy: AiGenerationCopy,
    state: AiTextJobState,
    onGenerate: () -> Unit,
    onRefresh: () -> Unit,
    onFallback: () -> Unit,
    onClose: () -> Unit,
) {
    var login by remember { mutableStateOf(false) }
    Dialog(onDismissRequest = onClose, properties = DialogProperties(usePlatformDefaultWidth = false, decorFitsSystemWindows = false)) {
        BackHandler(onBack = onClose)
        Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            Column(Modifier.fillMaxSize().statusBarsPadding().navigationBarsPadding()) {
                Row(Modifier.fillMaxWidth().padding(horizontal = 4.dp, vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onClose) { Icon(Icons.Outlined.ArrowBack, "Voltar") }
                    Text(copy.screenTitle, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
                    BetaPill(Modifier.padding(end = 16.dp))
                }
                Column(
                    Modifier.weight(1f).fillMaxWidth().verticalScroll(rememberScrollState()).padding(horizontal = 20.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    TargetChip(target)
                    when (state) {
                        AiTextJobState.Generating -> EstudarioProcessView(
                            title = copy.processingTitle,
                            stages = copy.stages,
                            stageMillis = copy.stageMillis,
                            footer = {
                                Text(
                                    "${copy.durationHint} Pode fechar e continuar usando o app: a geração segue em segundo plano e avisamos quando estiver pronta.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    textAlign = TextAlign.Center,
                                )
                                br.com.estudario.ui.brand.OutlinedButton(onClick = onClose, modifier = Modifier.padding(top = 8.dp)) { Text("Continuar em segundo plano") }
                            },
                        )
                        else -> {
                            Hero(copy)
                            StatusCard(state, copy, onGenerate = onGenerate, onLogin = { login = true }, onRefresh = onRefresh, onFallback = onFallback)
                        }
                    }
                }
            }
        }
        if (login) AccountLoginDialog(onDismiss = { login = false }, onSignedIn = { login = false; onRefresh() })
    }
}

@Composable
private fun Hero(copy: AiGenerationCopy) {
    Box(Modifier.fillMaxWidth().clip(RoundedCornerShape(26.dp)).background(aiGradient())) {
        Column(Modifier.padding(22.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            br.com.estudario.ui.assistant.FolhaTalking(76.dp, copy.heroTitle + copy.heroText, onClick = {})
            Text(copy.heroTitle, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = Color.White)
            Text(copy.heroText, style = MaterialTheme.typography.bodyMedium, color = Color.White.copy(alpha = 0.88f))
            copy.benefits.forEach { (icon, text) -> Benefit(icon, text) }
        }
    }
}

@Composable
private fun StatusCard(
    state: AiTextJobState,
    copy: AiGenerationCopy,
    onGenerate: () -> Unit,
    onLogin: () -> Unit,
    onRefresh: () -> Unit,
    onFallback: () -> Unit,
) {
    val scheme = MaterialTheme.colorScheme
    Surface(shape = RoundedCornerShape(24.dp), color = scheme.surfaceContainerLow) {
        Column(Modifier.fillMaxWidth().padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            when (state) {
                AiTextJobState.Checking -> Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                    EstudarioBookLoader(size = 40.dp)
                    Column {
                        Text("Verificando sua cota", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                        Text("Conferindo a conta e a geração do dia…", style = MaterialTheme.typography.bodySmall, color = scheme.onSurfaceVariant)
                    }
                }
                AiTextJobState.NeedsLogin -> {
                    GateStatus(Icons.Outlined.Lock, "Entre para continuar", "O Estudário usa a sua conta para guardar o saldo de gerações do seu plano.", StatusTone(scheme.tertiaryContainer, scheme.onTertiaryContainer))
                    PrimaryAction("Entrar para continuar", onLogin)
                }
                is AiTextJobState.Unavailable -> {
                    GateStatus(Icons.Outlined.CloudOff, "Geração indisponível agora", state.message, StatusTone(scheme.surfaceContainerHighest, scheme.onSurfaceVariant))
                    OutlinedButton(onClick = onRefresh, modifier = Modifier.fillMaxWidth().height(50.dp), shape = RoundedCornerShape(16.dp)) { Text("Tentar de novo") }
                }
                is AiTextJobState.QuotaUsed -> {
                    GateStatus(Icons.Outlined.HourglassTop, "Você já usou a geração de hoje", state.resetLabel ?: "Uma nova geração libera à meia-noite (horário de Brasília).", StatusTone(scheme.tertiaryContainer, scheme.onTertiaryContainer))
                }
                is AiTextJobState.Ready -> {
                    StatusPill(state.quotaLabel, StatusTone(scheme.secondaryContainer, estudarioColors().completed))
                    PrimaryAction(copy.generateLabel, onGenerate, Icons.Outlined.AutoAwesome)
                    Text("Você revisa antes de salvar. Se algo falhar, a geração volta para o seu saldo.", style = MaterialTheme.typography.bodySmall, color = scheme.onSurfaceVariant)
                }
                is AiTextJobState.Failed -> {
                    InlineError(state.message)
                    if (state.canRetry) PrimaryAction("Tentar novamente", onRefresh)
                }
                AiTextJobState.Generating, is AiTextJobState.Done -> Unit
            }
        }
    }
    Spacer(Modifier.height(4.dp))
}
