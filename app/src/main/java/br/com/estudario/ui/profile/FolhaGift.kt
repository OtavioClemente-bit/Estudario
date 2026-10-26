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
 * Quando uma conquista libera peça nova no armário, abre o presente depois das outras comemorações; as peças
 * que chegam juntas saem da mesma caixa. Na primeira vez no aparelho (ou depois de restaurar um backup), o
 * que já estava liberado entra sem festa.
 */
@Composable
fun FolhaGiftHost(viewModel: AppViewModel, enabled: Boolean) {
    val context = LocalContext.current
    val progress by viewModel.progress.collectAsState()
    val queue = remember { mutableStateListOf<List<FolhaItem>>() }
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
    val items = queue.firstOrNull()
    if (enabled && items != null) FolhaGiftDialog(items, onUse = { br.com.estudario.data.FolhaLookStore.write(context, items); queue.removeAt(0) }, onClose = { queue.removeAt(0) })
}

private fun rarityAccent(r: FolhaRarity) = when (r) {
    FolhaRarity.COMUM -> Color(0xFFBFD6FF)
    FolhaRarity.RARA -> Color(0xFF9CC6FF)
    FolhaRarity.EPICA -> Color(0xFFD2B2FF)
    FolhaRarity.LENDARIA -> Color(0xFFFFD877)
}

/** "A", "A e B", "A, B e C". */
private fun joinNames(names: List<String>): String = if (names.size <= 1) names.firstOrNull().orEmpty() else names.dropLast(1).joinToString(", ") + " e " + names.last()

/**
 * Peças novas (a mesma cena do site): a caixa de presente cai ao lado do Folha, se mexe sozinha com luz
 * vazando, ele puxa a fita, a tampa voa e sobem as cartas (todas juntas, em leque, com ele vestindo cada
 * peça); elas ficam paradas um tempo, voam até ele e, num clarão, ele aparece vestido. Depois entram os
 * nomes, a raridade e os botões.
 */
@Composable
fun FolhaGiftDialog(items: List<FolhaItem>, onUse: () -> Unit, onClose: () -> Unit) {
    val best = items.maxOf { it.rarity }
    val one = items.size == 1
    val pieces = org.json.JSONArray().apply {
        items.forEach { put(org.json.JSONObject().put("slot", it.slot.key).put("piece", it.id).put("rarity", it.rarity.name.lowercase()).put("name", it.name)) }
    }
    val reqs = items.map { FolhaWardrobe.requirement(it) }.distinct()
    SceneCelebration(
        scene = listOf("tipo" to "peca", "pieces" to pieces.toString(), "rarity" to best.name.lowercase()),
        reveal = (FolhaWardrobe.giftFlashAt(items.size) + 0.2).toFloat(),
        onDismiss = onClose,
        text = {
            CelEyebrow(if (one) "NOVA PEÇA · ${best.label.uppercase()}" else "${items.size} PEÇAS NOVAS · ATÉ ${best.label.uppercase()}", rarityAccent(best))
            CelTitle(if (one) items[0].name else joinNames(items.map { it.name }))
            CelBody(if (one) "${items[0].slot.label} · ${reqs[0]}" else if (reqs.size == 1) reqs[0] else joinNames(items.map { it.slot.label }.distinct()))
        },
        actions = {
            OutlinedButton(onClick = onClose, shape = RoundedCornerShape(14.dp), modifier = Modifier.heightIn(min = 52.dp)) { Text("Fechar", color = Color.White) }
            Button(onClick = onUse, shape = RoundedCornerShape(14.dp), modifier = Modifier.heightIn(min = 52.dp), colors = ButtonDefaults.buttonColors(containerColor = CelGold, contentColor = Color(0xFF2A1A05))) {
                Text(if (one) "Usar agora" else "Vestir tudo", fontWeight = FontWeight.Bold)
            }
        },
    )
}

