package br.com.estudario.ui.profile

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Bedtime
import androidx.compose.material.icons.outlined.Casino
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.Coffee
import androidx.compose.material.icons.outlined.LocalFireDepartment
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.RestartAlt
import androidx.compose.material.icons.outlined.WarningAmber
import androidx.compose.material3.*
import br.com.estudario.ui.brand.ElevatedCard
import br.com.estudario.ui.brand.FilterChip
import br.com.estudario.ui.brand.Icon
import br.com.estudario.ui.brand.LinearProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.estudario.domain.BadgeCatalog
import br.com.estudario.domain.FolhaItem
import br.com.estudario.domain.FolhaRarity
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

/** As peças escolhidas no armário (FolhaLookStore, sincronizado com o site), vistas pela tela. */
object FolhaWardrobePrefs {
    fun write(context: Context, items: Collection<FolhaItem>) = br.com.estudario.data.FolhaLookStore.write(context, items)

    fun chosen(context: Context, slot: FolhaSlot, summary: ProgressEngine.ProgressSummary): FolhaItem =
        FolhaWardrobe.resolve(slot, br.com.estudario.data.FolhaLookStore.read(context, slot), summary)
}

/**
 * Mantém o visual do Folha em dia no app inteiro: peças do armário, sequência e risco de perder a
 * sequência (do fim da tarde em diante, se hoje ainda não fechou). Fica no topo do app.
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
    val choices by br.com.estudario.data.FolhaLookStore.changes.collectAsState()
    LaunchedEffect(progress, streak, hour, choices) {
        val summary = progress ?: return@LaunchedEffect
        FolhaSlot.entries.forEach { FolhaLook.set(it.key, FolhaWardrobePrefs.chosen(context, it, summary).id) }
        val s = streak
        FolhaLook.streak = s?.current ?: 0
        FolhaLook.atRisk = s != null && s.current >= 2 && !s.todayDone && hour >= 18
    }
}

private fun rarityColors(r: FolhaRarity, dark: Boolean): Pair<Color, Color> = when (r) {
    FolhaRarity.COMUM -> if (dark) Color(0xFF2E2E3A) to Color(0xFFB9B6C9) else Color(0xFFEEEDE7) to Color(0xFF5E5C55)
    FolhaRarity.RARA -> if (dark) Color(0xFF1E3556) to Color(0xFF9CC6FF) else Color(0xFFDCEBFF) to Color(0xFF1D5DB8)
    FolhaRarity.EPICA -> if (dark) Color(0xFF35204F) to Color(0xFFD2B2FF) else Color(0xFFEEE0FF) to Color(0xFF6A2DBF)
    FolhaRarity.LENDARIA -> Color(0xFFF5C451) to Color(0xFF5A3803)
}

@Composable
private fun RarityTag(r: FolhaRarity) {
    val dark = MaterialTheme.colorScheme.background.red < 0.5f
    val (bg, fg) = rarityColors(r, dark)
    Text(
        r.label.uppercase(), color = fg, fontWeight = FontWeight.Black, fontSize = 9.5.sp, letterSpacing = 0.4.sp,
        modifier = Modifier.clip(RoundedCornerShape(50)).background(bg).padding(horizontal = 7.dp, vertical = 2.dp),
    )
}

/** Movimentos para ver as peças em ação na vitrine. */
private val TRY_MOODS = listOf(
    FolhaMood.IDLE to "Parado", FolhaMood.PRESENT to "Mostrar", FolhaMood.WAVE to "Acenar", FolhaMood.HAPPY to "Comemorar",
    FolhaMood.THINKING to "Estudar", FolhaMood.ECSTATIC to "Em chamas", FolhaMood.SLEEPY to "Cochilar",
)

/** Na vitrine e nas miniaturas, mãos e itens aparecem melhor com ele mostrando (mãos para a frente). */
private fun showcaseMood(slot: FolhaSlot) = if (slot == FolhaSlot.MAO || slot == FolhaSlot.LUVAS) FolhaMood.PRESENT else FolhaMood.IDLE

/**
 * O armário do Folha: nove categorias de peças que saem das conquistas. Escolher troca na hora (ele
 * comemora); tocar numa peça trancada mostra o Folha experimentando e diz como liberar. A vitrine tem
 * botões de movimento para ver as peças acompanhando o corpo.
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun WardrobeScreen(viewModel: AppViewModel, onBack: () -> Unit, showInternalTopBar: Boolean = true) {
    val context = LocalContext.current
    val progress by viewModel.progress.collectAsState()
    // A versão das escolhas: lida (não só observada) para a tela redesenhar a marcação "Usando" a cada troca.
    val chosenVersion by br.com.estudario.data.FolhaLookStore.changes.collectAsState()
    var slot by remember { mutableStateOf(FolhaSlot.CAPA) }
    var trying by remember { mutableStateOf<FolhaItem?>(null) }
    var cheer by remember { mutableIntStateOf(0) }
    var mood by remember { mutableStateOf(FolhaMood.WAVE) }
    var testMood by remember { mutableStateOf<FolhaMood?>(null) }
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
        val owned = FolhaWardrobe.items.count { FolhaWardrobe.unlocked(it, summary) }
        val total = FolhaWardrobe.items.size
        LazyColumn(Modifier.fillMaxSize().padding(padding), contentPadding = screenPadding(), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            item {
                Text(
                    "Monte o visual do Folha com peças conquistadas nos estudos. Cada emblema e cada nível liberam uma peça nova.",
                    style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            item {
                // A vitrine: o Folha como está (ou experimentando uma peça trancada) e os movimentos de teste.
                ElevatedCard(Modifier.fillMaxWidth()) {
                    Column(Modifier.fillMaxWidth().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        val t = trying
                        Box(Modifier.padding(top = 22.dp, bottom = 4.dp)) {
                            Folha(
                                160.dp, mood = testMood ?: if (t != null) (if (showcaseMood(t.slot) == FolhaMood.PRESENT) FolhaMood.PRESENT else FolhaMood.PROUD) else if (mood == FolhaMood.IDLE) showcaseMood(slot) else mood, onClick = {},
                                style = FolhaStyle(pieces = t?.let { mapOf(it.slot.key to it.id) } ?: emptyMap(), noScene = true),
                            )
                        }
                        FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterHorizontally), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            TRY_MOODS.forEach { (m, label) ->
                                FilterChip((testMood ?: FolhaMood.IDLE) == m, { testMood = if (m == FolhaMood.IDLE) null else m }, { Text(label) })
                            }
                        }
                        if (t != null) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Icon(Icons.Outlined.Lock, null, Modifier.size(16.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text("Experimentando", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                RarityTag(t.rarity)
                            }
                            Text(t.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            Text(FolhaWardrobe.requirement(t), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center)
                            FolhaWardrobe.progress(t, summary)?.let { (cur, target, unit) ->
                                LinearProgressIndicator({ cur.toFloat() / target }, Modifier.fillMaxWidth(0.8f).clip(RoundedCornerShape(50)))
                                Text("$cur de $target $unit", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            TextButton(onClick = { trying = null }) { Text("Voltar ao meu visual") }
                        } else {
                            Text("$owned de $total peças", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            LinearProgressIndicator({ owned.toFloat() / total }, Modifier.fillMaxWidth(0.8f).clip(RoundedCornerShape(50)))
                            FolhaWardrobe.next(summary)?.let { (item, pct) ->
                                Text("Próxima peça: ${item.name} (${(pct * 100).toInt()}%)", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                FilledTonalButton(onClick = { FolhaWardrobePrefs.write(context, FolhaWardrobe.random(summary).values); cheer++ }) {
                                    Icon(Icons.Outlined.Casino, null, Modifier.size(18.dp)); Spacer(Modifier.width(6.dp)); Text("Surpreenda-me")
                                }
                                TextButton(onClick = { FolhaWardrobePrefs.write(context, FolhaSlot.entries.map { FolhaWardrobe.default(it) }); cheer++ }) {
                                    Icon(Icons.Outlined.RestartAlt, null, Modifier.size(18.dp)); Spacer(Modifier.width(6.dp)); Text("Visual clássico")
                                }
                            }
                        }
                    }
                }
            }
            item {
                // categorias
                Row(Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    FolhaSlot.entries.forEach { s ->
                        val have = FolhaWardrobe.of(s).count { FolhaWardrobe.unlocked(it, summary) }
                        FilterChip(s == slot, { slot = s; trying = null }, { Text("${s.label}  $have/${FolhaWardrobe.of(s).size}") })
                    }
                }
            }
            item { Text(slot.hint, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant) }
            item {
                val list = FolhaWardrobe.of(slot)
                val chosenId = remember(chosenVersion, slot, summary) { FolhaWardrobePrefs.chosen(context, slot, summary).id }
                FlowRow(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalArrangement = Arrangement.spacedBy(10.dp), maxItemsInEachRow = 3) {
                    list.forEach { item ->
                        val open = FolhaWardrobe.unlocked(item, summary)
                        WardrobeTile(
                            item = item, open = open,
                            selected = chosenId == item.id,
                            trying = trying?.let { it.slot == item.slot && it.id == item.id } == true,
                            progress = if (open) null else FolhaWardrobe.progress(item, summary),
                            modifier = Modifier.weight(1f),
                        ) {
                            if (open) {
                                trying = null
                                if (chosenId != item.id) { FolhaWardrobePrefs.write(context, listOf(item)); cheer++ }
                            } else trying = if (trying?.let { it.slot == item.slot && it.id == item.id } == true) null else item
                        }
                    }
                    // completa a última linha, para os quadrados não esticarem
                    repeat((3 - list.size % 3) % 3) { Spacer(Modifier.weight(1f)) }
                }
            }
            item {
                ElevatedCard(Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text("DETALHES QUE MUDAM SOZINHOS", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        AutoDetail(Icons.Outlined.Coffee, "Manhã", "Uma caneca de café fumegando ao lado dele.")
                        AutoDetail(Icons.Outlined.Bedtime, "Noite", "Com o capelo, ele troca por uma touca de dormir.")
                        AutoDetail(Icons.Outlined.LocalFireDepartment, "Sequência", "Medalha no capelo com 7 dias (bronze), 30 (prata) e 100 (ouro).")
                        AutoDetail(Icons.Outlined.WarningAmber, "Meta do dia em risco", "No fim da tarde, se a meta ainda não foi batida, ele fica preocupado.")
                    }
                }
            }
        }
    }
}

@Composable
private fun AutoDetail(icon: ImageVector, title: String, text: String) {
    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        Icon(icon, null, Modifier.size(20.dp), tint = MaterialTheme.colorScheme.primary)
        Column {
            Text(title, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
            Text(text, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
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
        trying -> BorderStroke(2.dp, Color(0xFFF5B83D))
        item.rarity == FolhaRarity.LENDARIA && open -> BorderStroke(1.5.dp, Color(0xFFF5C451))
        else -> BorderStroke(1.dp, scheme.outlineVariant)
    }
    Column(
        modifier
            .clip(RoundedCornerShape(18.dp))
            .background(if (selected) scheme.primaryContainer else scheme.surface)
            .border(border, RoundedCornerShape(18.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 6.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        RarityTag(item.rarity)
        // Miniatura parada (um quadro só): uma grade de Folhas vivos pesaria à toa.
        val px = with(LocalDensity.current) { 76.dp.roundToPx() }
        val thumb = remember(item.slot, item.id, px, FolhaLook.pieces()) {
            br.com.estudario.ui.assistant.folhaBitmap(px, showcaseMood(item.slot), FolhaStyle(pieces = FolhaLook.pieces() + (item.slot.key to item.id), plain = true)).asImageBitmap()
        }
        androidx.compose.foundation.Image(thumb, null, Modifier.size(76.dp).alpha(if (open) 1f else 0.55f))
        Text(item.name, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, maxLines = 2, overflow = TextOverflow.Ellipsis)
        when {
            selected -> Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                Icon(Icons.Outlined.CheckCircle, null, Modifier.size(14.dp), tint = scheme.primary)
                Text("Usando", style = MaterialTheme.typography.labelSmall, color = scheme.primary, fontWeight = FontWeight.Bold)
            }
            open -> Text("Vestir", style = MaterialTheme.typography.labelSmall, color = scheme.onSurfaceVariant)
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
    val owned = FolhaWardrobe.items.count { FolhaWardrobe.unlocked(it, summary) }
    val next = FolhaWardrobe.next(summary)
    Row(Modifier.fillMaxWidth().clickable(onClick = onWardrobe).padding(vertical = 4.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        Box(Modifier.size(64.dp), contentAlignment = Alignment.Center) { Folha(56.dp, mood = FolhaMood.WAVE, enter = false) }
        Column(Modifier.weight(1f)) {
            Text("ARMÁRIO DO FOLHA", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text("$owned de ${FolhaWardrobe.items.size} peças liberadas", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface)
            if (next != null) Text("Próxima: ${next.first.name} (${(next.second * 100).toInt()}%)", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Icon(Icons.Outlined.ChevronRight, "Abrir o armário", tint = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}
