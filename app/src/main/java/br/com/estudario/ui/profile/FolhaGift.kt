package br.com.estudario.ui.profile

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.foundation.background
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.estudario.domain.FolhaItem
import br.com.estudario.domain.FolhaRarity
import br.com.estudario.domain.FolhaWardrobe
import br.com.estudario.ui.AppViewModel

/** Peças já comemoradas neste aparelho (para o presente abrir só para o que é novo de verdade). */
private object FolhaGiftSeen {
    private fun prefs(context: Context) = context.getSharedPreferences("folha_look", Context.MODE_PRIVATE)
    fun read(context: Context): Set<String>? = prefs(context).getString("celebrated", null)?.split(",")?.filter { it.isNotBlank() }?.toSet()
    fun write(context: Context, keys: Set<String>) = prefs(context).edit().putString("celebrated", keys.sorted().joinToString(",")).apply()
}

private fun FolhaItem.key() = slot.name + ":" + id

/**
 * Quando uma conquista libera peça nova no armário, abre o presente depois das outras comemorações. Na
 * primeira vez no aparelho (ou depois de restaurar um backup), o que já estava liberado entra sem festa.
 */
@Composable
fun FolhaGiftHost(viewModel: AppViewModel, enabled: Boolean) {
    val context = LocalContext.current
    val progress by viewModel.progress.collectAsState()
    val queue = remember { mutableStateListOf<FolhaItem>() }
    LaunchedEffect(progress) {
        val summary = progress ?: return@LaunchedEffect
        val owned = FolhaWardrobe.items.filter { (it.badgeId != null || it.level != null || it.collection != null) && FolhaWardrobe.unlocked(it, summary) }
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

/**
 * Peça nova (a mesma cena do site): o presente cai na frente do Folha, pula impaciente, desamarra, a
 * tampa voa e a luz da peça sai, dá a volta nele e, num clarão, ele aparece vestindo. Depois entram o
 * nome, a raridade e os botões.
 */
@Composable
fun FolhaGiftDialog(item: FolhaItem, onUse: () -> Unit, onClose: () -> Unit) {
    SceneCelebration(
        scene = listOf("tipo" to "peca", "piece-slot" to item.slot.key, "piece" to item.id, "rarity" to item.rarity.name.lowercase()),
        reveal = 3.6f,
        onDismiss = onClose,
        text = {
            CelEyebrow("NOVA PEÇA · ${item.rarity.label.uppercase()}", rarityAccent(item.rarity))
            CelTitle(item.name)
            CelBody("${item.slot.label} · ${FolhaWardrobe.requirement(item)}")
        },
        actions = {
            OutlinedButton(onClick = onClose, shape = RoundedCornerShape(14.dp), modifier = Modifier.heightIn(min = 52.dp)) { Text("Depois", color = Color.White) }
            Button(onClick = onUse, shape = RoundedCornerShape(14.dp), modifier = Modifier.heightIn(min = 52.dp), colors = ButtonDefaults.buttonColors(containerColor = CelGold, contentColor = Color(0xFF2A1A05))) {
                Text("Usar agora", fontWeight = FontWeight.Bold)
            }
        },
    )
}

/** Nível de amizade já comemorado neste aparelho. */
private object BondSeen {
    private fun prefs(context: Context) = context.getSharedPreferences("folha_look", Context.MODE_PRIVATE)
    fun read(context: Context): Int? = prefs(context).getInt("amizade", -1).takeIf { it >= 0 }
    fun write(context: Context, v: Int) = prefs(context).edit().putInt("amizade", v).apply()
}

/**
 * Quando a amizade com o Folha sobe de nível (dias com estudo), a cena do coração enchendo, depois das
 * outras comemorações. Na primeira vez no aparelho, o nível atual entra sem festa.
 */
@Composable
fun FolhaBondHost(viewModel: AppViewModel, enabled: Boolean) {
    val context = LocalContext.current
    val streak by viewModel.streak.collectAsState()
    var show by remember { mutableStateOf<br.com.estudario.domain.FolhaBondLevel?>(null) }
    LaunchedEffect(streak?.activeDays) {
        val s = streak ?: return@LaunchedEffect
        val level = br.com.estudario.domain.FolhaBond.level(s.activeDays)
        val seen = BondSeen.read(context)
        if (seen == null) { BondSeen.write(context, level.index); return@LaunchedEffect }
        if (level.index > seen) { BondSeen.write(context, level.index); show = level }
    }
    val level = show
    if (enabled && level != null) SceneCelebration(
        scene = listOf("tipo" to "vinculo", "nivel" to level.index.toString(), "de" to br.com.estudario.domain.FolhaBond.levels.size.toString()),
        reveal = 2.2f,
        onDismiss = { show = null },
        text = {
            CelEyebrow("AMIZADE COM O FOLHA", Color(0xFFFF9DB8))
            CelTitle("Agora vocês são ${level.name}!")
            CelBody(level.unlock)
        },
        actions = {
            Button(
                onClick = { show = null }, shape = RoundedCornerShape(14.dp), modifier = Modifier.heightIn(min = 52.dp).widthIn(min = 200.dp),
                colors = ButtonDefaults.buttonColors(containerColor = CelGold, contentColor = Color(0xFF2A1A05)),
            ) { Text("Continuar", fontWeight = FontWeight.Bold) }
        },
    )
}

/** Faixa do Perfil: a amizade com o Folha, os corações e quanto falta para o próximo nível. */
@Composable
internal fun BondBand(activeDays: Int) {
    val level = br.com.estudario.domain.FolhaBond.level(activeDays)
    val next = br.com.estudario.domain.FolhaBond.next(activeDays)
    val scheme = MaterialTheme.colorScheme
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        Box(Modifier.size(64.dp), contentAlignment = Alignment.Center) {
            br.com.estudario.ui.assistant.Folha(56.dp, mood = if (level.index >= 3) br.com.estudario.ui.assistant.FolhaMood.HAPPY else br.com.estudario.ui.assistant.FolhaMood.WAVE, enter = false)
        }
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
            Text("AMIZADE COM O FOLHA", style = MaterialTheme.typography.labelSmall, color = scheme.onSurfaceVariant)
            Text(level.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black)
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                br.com.estudario.domain.FolhaBond.levels.forEach { l ->
                    val on = l.index <= level.index
                    val off = scheme.outlineVariant
                    androidx.compose.foundation.Canvas(Modifier.size(16.dp, 14.dp)) {
                        val w = size.width; val h = size.height
                        val p = androidx.compose.ui.graphics.Path().apply {
                            moveTo(w / 2, h); cubicTo(-w * 0.1f, h * 0.55f, w * 0.05f, -h * 0.15f, w / 2, h * 0.22f); cubicTo(w * 0.95f, -h * 0.15f, w * 1.1f, h * 0.55f, w / 2, h); close()
                        }
                        drawPath(p, if (on) Color(0xFFF0506B) else off)
                    }
                }
            }
        }
    }
    Spacer(Modifier.height(10.dp))
    if (next != null) {
        val left = next.days - activeDays
        Box(Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(50)).background(Color(0x26F0506B))) {
            Box(
                Modifier.fillMaxHeight().fillMaxWidth(br.com.estudario.domain.FolhaBond.progress(activeDays)).clip(RoundedCornerShape(50))
                    .background(androidx.compose.ui.graphics.Brush.horizontalGradient(listOf(Color(0xFFFF9DB8), Color(0xFFF0506B)))),
            )
        }
        Spacer(Modifier.height(6.dp))
        Text("Mais $left ${if (left == 1) "dia" else "dias"} estudando juntos para virar ${next.name}. ${next.unlock}", style = MaterialTheme.typography.bodySmall, color = scheme.onSurfaceVariant)
    } else Text("Vocês chegaram ao topo da amizade. ${level.unlock}", style = MaterialTheme.typography.bodySmall, color = scheme.onSurfaceVariant)
    Text("Conta cada dia com estudo, mesmo que não seja seguido.", style = MaterialTheme.typography.labelSmall, color = scheme.onSurfaceVariant.copy(alpha = 0.7f))
}
