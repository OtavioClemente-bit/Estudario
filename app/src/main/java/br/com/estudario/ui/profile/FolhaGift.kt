package br.com.estudario.ui.profile

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import br.com.estudario.domain.FolhaItem
import br.com.estudario.domain.FolhaRarity
import br.com.estudario.domain.FolhaWardrobe
import br.com.estudario.ui.AppViewModel
import br.com.estudario.ui.assistant.FolhaChest
import br.com.estudario.ui.components.AppMarkBackground
import kotlinx.coroutines.delay

/** Peças já comemoradas neste aparelho (para o baú abrir só para o que é novo de verdade). */
private object FolhaGiftSeen {
    private fun prefs(context: Context) = context.getSharedPreferences("folha_look", Context.MODE_PRIVATE)
    fun read(context: Context): Set<String>? = prefs(context).getString("celebrated", null)?.split(",")?.filter { it.isNotBlank() }?.toSet()
    fun write(context: Context, keys: Set<String>) = prefs(context).edit().putString("celebrated", keys.sorted().joinToString(",")).apply()
}

private fun FolhaItem.key() = slot.name + ":" + id

/**
 * Quando uma conquista libera peça nova no armário, abre o baú depois das outras comemorações. Na
 * primeira vez no aparelho (ou depois de restaurar um backup), o que já estava liberado entra sem festa.
 */
@Composable
fun FolhaGiftHost(viewModel: AppViewModel, enabled: Boolean) {
    val context = LocalContext.current
    val progress by viewModel.progress.collectAsState()
    val queue = remember { mutableStateListOf<FolhaItem>() }
    LaunchedEffect(progress) {
        val summary = progress ?: return@LaunchedEffect
        val owned = FolhaWardrobe.items.filter { (it.badgeId != null || it.level != null) && FolhaWardrobe.unlocked(it, summary) }
        val seen = FolhaGiftSeen.read(context)
        if (seen == null) { FolhaGiftSeen.write(context, owned.map { it.key() }.toSet()); return@LaunchedEffect }
        val fresh = owned.filter { it.key() !in seen }
        if (fresh.isEmpty()) return@LaunchedEffect
        FolhaGiftSeen.write(context, seen + fresh.map { it.key() })
        queue += fresh.sortedByDescending { it.rarity.ordinal }
    }
    val item = queue.firstOrNull()
    if (enabled && item != null) FolhaGiftDialog(item, onUse = { br.com.estudario.data.FolhaLookStore.write(context, listOf(item)); queue.removeAt(0) }, onClose = { queue.removeAt(0) })
}

private fun rarityAccent(r: FolhaRarity) = when (r) {
    FolhaRarity.COMUM -> Color(0xFFBFD6FF)
    FolhaRarity.RARA -> Color(0xFF9CC6FF)
    FolhaRarity.EPICA -> Color(0xFFD2B2FF)
    FolhaRarity.LENDARIA -> Color(0xFFFFD877)
}

/** A tela do baú: ele abre, o Folha sobe vestindo a peça, e aparecem o nome, a raridade e os botões. */
@Composable
fun FolhaGiftDialog(item: FolhaItem, onUse: () -> Unit, onClose: () -> Unit) {
    var textIn by remember(item) { mutableStateOf(false) }
    LaunchedEffect(item) { delay(2300); textIn = true }
    val textAlpha by androidx.compose.animation.core.animateFloatAsState(if (textIn) 1f else 0f, androidx.compose.animation.core.tween(450), label = "texto")
    Dialog(onDismissRequest = onClose, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        Box(Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(AppMarkBackground, Color(0xFF1A1050), AppMarkBackground)))) {
            Column(
                Modifier.fillMaxSize().systemBarsPadding().padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center,
            ) {
                FolhaChest(item.slot.key, item.id, item.rarity.name.lowercase(), 280.dp)
                Spacer(Modifier.height(10.dp))
                Column(Modifier.alpha(textAlpha), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("NOVA PEÇA · ${item.rarity.label.uppercase()}", color = rarityAccent(item.rarity), fontWeight = FontWeight.Black, letterSpacing = 2.sp, style = MaterialTheme.typography.labelLarge)
                    Text(item.name, color = Color.White, fontWeight = FontWeight.Black, style = MaterialTheme.typography.headlineMedium, textAlign = TextAlign.Center)
                    Text("${item.slot.label} · ${FolhaWardrobe.requirement(item)}", color = Color.White.copy(alpha = 0.75f), style = MaterialTheme.typography.bodyMedium, textAlign = TextAlign.Center)
                    Spacer(Modifier.height(14.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        OutlinedButton(onClick = onClose, shape = RoundedCornerShape(14.dp)) { Text("Depois", color = Color.White) }
                        Button(onClick = onUse, shape = RoundedCornerShape(14.dp), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF5B83D), contentColor = Color(0xFF2A1A05))) {
                            Text("Usar agora", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
