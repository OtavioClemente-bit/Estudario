package br.com.estudario.ui.profile

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.*
import br.com.estudario.ui.brand.ElevatedCard
import br.com.estudario.ui.brand.Icon
import br.com.estudario.ui.brand.LinearProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import br.com.estudario.domain.BadgeCatalog
import br.com.estudario.domain.FolhaItem
import br.com.estudario.domain.FolhaSlot
import br.com.estudario.domain.FolhaWardrobe
import br.com.estudario.domain.ProgressEngine
import br.com.estudario.ui.AppViewModel
import br.com.estudario.ui.assistant.Folha
import br.com.estudario.ui.assistant.FolhaLook
import br.com.estudario.ui.assistant.FolhaMood
import br.com.estudario.ui.assistant.FolhaStyle
import br.com.estudario.ui.components.LoadingScreen
import br.com.estudario.ui.theme.screenPadding
import kotlinx.coroutines.delay

/** A escolha do armário, neste aparelho. [choices] muda quando a pessoa troca um item. */
object FolhaWardrobePrefs {
    var choices by mutableIntStateOf(0)
        private set

    private fun prefs(context: Context) = context.getSharedPreferences("folha_look", Context.MODE_PRIVATE)

    fun read(context: Context, slot: FolhaSlot): String? = prefs(context).getString(slot.name.lowercase(), null)

    fun write(context: Context, item: FolhaItem) {
        prefs(context).edit().putString(item.slot.name.lowercase(), item.id).apply()
        choices++
    }

    fun chosen(context: Context, slot: FolhaSlot, summary: ProgressEngine.ProgressSummary): FolhaItem =
        FolhaWardrobe.resolve(slot, read(context, slot), summary)
}

/**
 * Mantém o visual do Folha em dia no app inteiro: armário, sequência e risco de perder a sequência (do
 * fim da tarde em diante, se hoje ainda não fechou). Fica no topo do app.
 */
@Composable
fun FolhaLookSync(viewModel: AppViewModel) {
    val context = LocalContext.current
    val progress by viewModel.progress.collectAsState()
    val streak by viewModel.streak.collectAsState()
    var hour by remember { mutableIntStateOf(java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY)) }
    LaunchedEffect(Unit) {
        while (true) { delay(60_000); hour = java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY) }
    }
    LaunchedEffect(progress, streak, hour, FolhaWardrobePrefs.choices) {
        val summary = progress ?: return@LaunchedEffect
        FolhaLook.cover = FolhaWardrobePrefs.chosen(context, FolhaSlot.CAPA, summary).id
        FolhaLook.ribbon = FolhaWardrobePrefs.chosen(context, FolhaSlot.FITA, summary).id
        FolhaLook.acc = FolhaWardrobePrefs.chosen(context, FolhaSlot.ACESSORIO, summary).id
        val s = streak
        FolhaLook.streak = s?.current ?: 0
        FolhaLook.atRisk = s != null && s.current >= 2 && !s.todayDone && hour >= 18
    }
}

private fun slotStyle(item: FolhaItem, plain: Boolean = true) = when (item.slot) {
    FolhaSlot.CAPA -> FolhaStyle(cover = item.id, plain = plain)
    FolhaSlot.FITA -> FolhaStyle(ribbon = item.id, plain = plain)
    FolhaSlot.ACESSORIO -> FolhaStyle(acc = item.id, plain = plain)
}

private fun itemProgress(item: FolhaItem, summary: ProgressEngine.ProgressSummary): Triple<Int, Int, String>? {
    item.level?.let { return Triple(summary.level.coerceAtMost(it), it, "níveis") }
    val p = summary.badges.firstOrNull { it.badge.id == item.badgeId } ?: return null
    return Triple(p.current.coerceAtMost(p.target), p.target, p.badge.unit)
}

/**
 * O armário do Folha: capas, fitas e acessórios que saem das conquistas. Escolher troca na hora (ele
 * comemora); tocar num item trancado mostra o Folha experimentando e diz como ganhar.
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun WardrobeScreen(viewModel: AppViewModel, onBack: () -> Unit, showInternalTopBar: Boolean = true) {
    val context = LocalContext.current
    val progress by viewModel.progress.collectAsState()
    @Suppress("UNUSED_VARIABLE") val chosenNow = FolhaWardrobePrefs.choices
    var trying by remember { mutableStateOf<FolhaItem?>(null) }
    var cheer by remember { mutableIntStateOf(0) }
    var mood by remember { mutableStateOf(FolhaMood.WAVE) }
    LaunchedEffect(cheer) {
        if (cheer == 0) { delay(1400); mood = FolhaMood.IDLE; return@LaunchedEffect }
        mood = FolhaMood.HAPPY; delay(1300); mood = FolhaMood.IDLE
    }
    Scaffold(
        topBar = {
            if (showInternalTopBar) TopAppBar(
                title = { Text("Armário do Folha") },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Outlined.ArrowBack, "Voltar") } },
            )
        },
    ) { padding ->
        val summary = progress
        if (summary == null) {
            LoadingScreen("Abrindo o armário", modifier = Modifier.padding(padding))
            return@Scaffold
        }
        val unlocked = FolhaWardrobe.items.count { FolhaWardrobe.unlocked(it, summary) }
        LazyColumn(Modifier.fillMaxSize().padding(padding), contentPadding = screenPadding(), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            item {
                // O palco: o Folha como está (ou experimentando um item trancado).
                ElevatedCard(Modifier.fillMaxWidth()) {
                    Column(Modifier.fillMaxWidth().padding(18.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        val t = trying
                        Box(Modifier.padding(top = 18.dp, bottom = 6.dp)) {
                            Folha(150.dp, mood = if (t != null) FolhaMood.PROUD else mood, onClick = {}, style = t?.let { slotStyle(it, plain = false) })
                        }
                        if (t != null) {
                            AssistChip(onClick = { trying = null }, label = { Text("Experimentando") }, leadingIcon = { Icon(Icons.Outlined.Lock, null, Modifier.size(16.dp)) })
                            Text(t.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            Text(FolhaWardrobe.requirement(t), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center)
                            itemProgress(t, summary)?.let { (cur, target, unit) ->
                                LinearProgressIndicator({ cur.toFloat() / target }, Modifier.fillMaxWidth(0.8f).clip(RoundedCornerShape(50)))
                                Text("$cur / $target $unit", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            TextButton(onClick = { trying = null }) { Text("Voltar ao meu Folha") }
                        } else {
                            Text("$unlocked de ${FolhaWardrobe.items.size} itens liberados", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            Text(
                                "Seu Folha aparece assim no app inteiro. De manhã ele toma café, à noite põe a touca, e com 7 dias seguidos ganha medalha no capelo.",
                                style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center,
                            )
                        }
                    }
                }
            }
            FolhaSlot.entries.forEach { slot ->
                item { Text(slot.label, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold) }
                item {
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalArrangement = Arrangement.spacedBy(10.dp), maxItemsInEachRow = 3) {
                        FolhaWardrobe.of(slot).forEach { item ->
                            val open = FolhaWardrobe.unlocked(item, summary)
                            WardrobeTile(
                                item = item, open = open,
                                selected = FolhaWardrobePrefs.chosen(context, slot, summary).id == item.id,
                                trying = trying?.id == item.id,
                                progress = if (open) null else itemProgress(item, summary),
                                modifier = Modifier.weight(1f),
                            ) {
                                if (open) {
                                    trying = null
                                    if (FolhaWardrobePrefs.chosen(context, slot, summary).id != item.id) { FolhaWardrobePrefs.write(context, item); cheer++ }
                                } else trying = if (trying?.id == item.id) null else item
                            }
                        }
                        // completa a última linha, para os quadrados não esticarem
                        repeat((3 - FolhaWardrobe.of(slot).size % 3) % 3) { Spacer(Modifier.weight(1f)) }
                    }
                }
            }
        }
    }
}

@Composable
private fun WardrobeTile(
    item: FolhaItem,
    open: Boolean,
    selected: Boolean,
    trying: Boolean,
    progress: Triple<Int, Int, String>?,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    val scheme = MaterialTheme.colorScheme
    val border = when {
        selected -> BorderStroke(2.dp, scheme.primary)
        trying -> BorderStroke(2.dp, androidx.compose.ui.graphics.Color(0xFFF5B83D))
        else -> BorderStroke(1.dp, scheme.outlineVariant)
    }
    Column(
        modifier
            .clip(RoundedCornerShape(18.dp))
            .background(if (selected) scheme.primaryContainer else scheme.surface)
            .border(border, RoundedCornerShape(18.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 6.dp, vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        // Miniatura parada (um quadro só): uma grade de Folhas vivos pesaria à toa.
        val px = with(androidx.compose.ui.platform.LocalDensity.current) { 76.dp.roundToPx() }
        val thumb = remember(item.id, px, FolhaLook.cover, FolhaLook.ribbon, FolhaLook.acc) {
            br.com.estudario.ui.assistant.folhaBitmap(px, FolhaMood.IDLE, slotStyle(item).let { st -> FolhaStyle(st.cover ?: FolhaLook.cover, st.ribbon ?: FolhaLook.ribbon, st.acc ?: FolhaLook.acc, plain = true) }).asImageBitmap()
        }
        androidx.compose.foundation.Image(thumb, null, Modifier.size(76.dp).alpha(if (open) 1f else 0.55f))
        Text(item.name, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, maxLines = 2, overflow = TextOverflow.Ellipsis)
        when {
            selected -> Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                Icon(Icons.Outlined.CheckCircle, null, Modifier.size(14.dp), tint = scheme.primary)
                Text("Usando", style = MaterialTheme.typography.labelSmall, color = scheme.primary, fontWeight = FontWeight.Bold)
            }
            open -> Text("Usar", style = MaterialTheme.typography.labelSmall, color = scheme.onSurfaceVariant)
            else -> Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                Icon(Icons.Outlined.Lock, null, Modifier.size(12.dp), tint = scheme.onSurfaceVariant)
                Text(
                    item.level?.let { "Nível $it" } ?: BadgeCatalog.all.firstOrNull { it.id == item.badgeId }?.name ?: "Emblema",
                    style = MaterialTheme.typography.labelSmall, color = scheme.onSurfaceVariant, maxLines = 1, overflow = TextOverflow.Ellipsis,
                )
            }
        }
        if (progress != null) LinearProgressIndicator({ progress.first.toFloat() / progress.second }, Modifier.fillMaxWidth().padding(horizontal = 4.dp).clip(RoundedCornerShape(50)))
    }
}

/** Faixa do Perfil: o Folha como está e o atalho para o armário. */
@Composable
internal fun WardrobeBand(summary: ProgressEngine.ProgressSummary, onWardrobe: () -> Unit) {
    val unlocked = FolhaWardrobe.items.count { FolhaWardrobe.unlocked(it, summary) }
    val next = FolhaWardrobe.items.filterNot { FolhaWardrobe.unlocked(it, summary) }
        .mapNotNull { item -> itemProgress(item, summary)?.let { item to it.first.toFloat() / it.second } }
        .maxByOrNull { it.second }
    Row(Modifier.fillMaxWidth().clickable(onClick = onWardrobe).padding(vertical = 4.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        Box(Modifier.size(64.dp), contentAlignment = Alignment.Center) { Folha(56.dp, mood = FolhaMood.WAVE, enter = false) }
        Column(Modifier.weight(1f)) {
            Text("ARMÁRIO DO FOLHA", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text("$unlocked de ${FolhaWardrobe.items.size} itens liberados", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface)
            if (next != null) Text("Próximo: ${next.first.name} (${(next.second * 100).toInt()}%)", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Icon(Icons.Outlined.ChevronRight, "Abrir o armário", tint = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}
