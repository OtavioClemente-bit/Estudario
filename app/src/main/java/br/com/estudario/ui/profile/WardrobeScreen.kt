package br.com.estudario.ui.profile

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Bedtime
import androidx.compose.material.icons.outlined.Add
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.material.icons.outlined.Checkroom
import androidx.compose.material.icons.outlined.MilitaryTech
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.Celebration
import androidx.compose.material.icons.outlined.WavingHand
import androidx.compose.material.icons.outlined.PanTool
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Casino
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.EmojiEvents
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
    val profile by viewModel.profile.collectAsState()
    LaunchedEffect(progress, streak, hour, choices, profile.name) {
        val summary = progress ?: return@LaunchedEffect
        FolhaSlot.entries.forEach { FolhaLook.set(it.key, FolhaWardrobePrefs.chosen(context, it, summary).id) }
        val s = streak
        FolhaLook.streak = s?.current ?: 0
        FolhaLook.atRisk = s != null && s.current >= 2 && !s.todayDone && hour >= 18
        // a amizade (gestos e falas novas) e o nome, para ele chamar a pessoa
        val bond = br.com.estudario.domain.FolhaBond.level(s?.activeDays ?: 0).index
        val first = profile.name.substringBefore(" ").trim()
        FolhaLook.bond = bond; FolhaLook.name = first
        FolhaLook.bondLines = br.com.estudario.domain.FolhaMemory.bondTapLines(bond, first.ifBlank { null })
        br.com.estudario.ui.widget.FolhaWidget.save(context, s, bond)
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

/** Movimentos para ver as peças em ação no palco (rótulo e ícone). */
private val TRY_MOODS = listOf(
    Triple(FolhaMood.IDLE, "Parado", Icons.Outlined.Person), Triple(FolhaMood.PRESENT, "Mostrar", Icons.Outlined.PanTool),
    Triple(FolhaMood.WAVE, "Acenar", Icons.Outlined.WavingHand), Triple(FolhaMood.HAPPY, "Comemorar", Icons.Outlined.Celebration),
    Triple(FolhaMood.THINKING, "Estudar", Icons.Outlined.MenuBook), Triple(FolhaMood.ECSTATIC, "Em chamas", Icons.Outlined.LocalFireDepartment),
    Triple(FolhaMood.SLEEPY, "Cochilar", Icons.Outlined.Bedtime),
)

/** Na vitrine e nas miniaturas, mãos e itens aparecem melhor com ele mostrando (mãos para a frente). */
private fun showcaseMood(slot: FolhaSlot) = if (slot == FolhaSlot.MAO || slot == FolhaSlot.LUVAS) FolhaMood.PRESENT else FolhaMood.IDLE

/** Onde a miniatura chega perto (zoom e ponto na grade 100 x 100): o chapéu na cabeça, os óculos no rosto... */
private fun focus(slot: FolhaSlot): Triple<Float, Float, Float>? = when (slot) {
    FolhaSlot.CHAPEU -> Triple(2.0f, 50f, 27f)
    FolhaSlot.OCULOS -> Triple(1.8f, 50f, 60f)
    FolhaSlot.PAGINAS -> Triple(1.25f, 50f, 63f)
    FolhaSlot.TINTA, FolhaSlot.BOCHECHAS -> Triple(1.75f, 50f, 63f)
    FolhaSlot.PESCOCO -> Triple(1.45f, 50f, 76f)
    FolhaSlot.MAO -> Triple(1.6f, 7f, 30f)
    FolhaSlot.FITA -> Triple(2.4f, 53f, 91f)
    FolhaSlot.COMPANHEIRO -> Triple(1.45f, -4f, 80f)
    else -> null
}

/** Os espaços de equipamento em volta do Folha, como num jogo: acessórios à esquerda, o próprio Folha à direita. */
private val LEFT_SLOTS = listOf(FolhaSlot.CHAPEU, FolhaSlot.OCULOS, FolhaSlot.PESCOCO, FolhaSlot.MAO, FolhaSlot.LUVAS, FolhaSlot.COSTAS)
private val RIGHT_SLOTS = listOf(FolhaSlot.CAPA, FolhaSlot.PAGINAS, FolhaSlot.TINTA, FolhaSlot.BOCHECHAS, FolhaSlot.FITA, FolhaSlot.AURA, FolhaSlot.COMPANHEIRO)

private val GameTop = Color(0xFF2A2390)
private val GameMid = Color(0xFF17134A)
private val GameBottom = Color(0xFF0F0C33)
private val GameLine = Color(0x17FFFFFF)
private val GameMute = Color(0x9EFFFFFF)
private val Mint = Color(0xFF7EE0B8)
private val GoldG = Color(0xFFF5B83D)
private fun rarityColor(r: FolhaRarity) = when (r) {
    FolhaRarity.COMUM -> Color(0xFFB9B6C9); FolhaRarity.RARA -> Color(0xFF4C9BFF); FolhaRarity.EPICA -> Color(0xFFA46BFF); FolhaRarity.LENDARIA -> GoldG
}

/** Título de colecionador pelo tanto de peças liberadas. */
private fun collectorRank(owned: Int, total: Int): String {
    val f = owned.toFloat() / total
    return when { f < 0.2f -> "Iniciante"; f < 0.4f -> "Estiloso"; f < 0.6f -> "Colecionador"; f < 0.85f -> "Fashionista"; else -> "Lenda do armário" }
}

/** Peças já vistas no armário neste aparelho: o que foi liberado e ainda não foi visto ganha o selo "NOVO". */
private object WardrobeSeen {
    private fun prefs(context: Context) = context.getSharedPreferences("folha_look", Context.MODE_PRIVATE)
    private fun key(item: FolhaItem) = item.slot.name + ":" + item.id
    var version by mutableIntStateOf(0)
        private set
    private fun read(context: Context): Set<String>? = prefs(context).getString("vistos", null)?.split(",")?.filter { it.isNotBlank() }?.toSet()
    private fun write(context: Context, v: Set<String>) { prefs(context).edit().putString("vistos", v.sorted().joinToString(",")).apply(); version++ }
    /** Primeira vez no aparelho: tudo o que já está liberado entra como visto. */
    fun init(context: Context, summary: ProgressEngine.ProgressSummary) { if (read(context) == null) write(context, FolhaWardrobe.items.filter { FolhaWardrobe.unlocked(it, summary) }.map(::key).toSet()) }
    fun isNew(context: Context, item: FolhaItem, summary: ProgressEngine.ProgressSummary): Boolean {
        val seen = read(context) ?: return false
        return FolhaWardrobe.unlocked(item, summary) && key(item) !in seen
    }
    fun markSeen(context: Context, items: List<FolhaItem>, summary: ProgressEngine.ProgressSummary) {
        val seen = read(context) ?: emptySet()
        val add = items.filter { FolhaWardrobe.unlocked(it, summary) }.map(::key).filter { it !in seen }
        if (add.isNotEmpty()) write(context, seen + add)
    }
}

/** Painel escuro de jogo (placar, boneco, inventário). */
@Composable
private fun GamePanel(modifier: Modifier = Modifier, content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier.fillMaxWidth().clip(RoundedCornerShape(24.dp)).background(Brush.verticalGradient(listOf(GameTop, GameMid, GameBottom)))
            .border(1.dp, GameLine, RoundedCornerShape(24.dp)).padding(14.dp),
        content = content,
    )
}

/** Barra de progresso com o texto por cima, como num jogo. */
@Composable
private fun GameBar(fraction: Float, label: String, modifier: Modifier = Modifier, height: Int = 18) {
    Box(modifier.height(height.dp).clip(RoundedCornerShape(50)).background(Color.White.copy(alpha = 0.1f)), contentAlignment = Alignment.Center) {
        Box(Modifier.align(Alignment.CenterStart).fillMaxHeight().fillMaxWidth(fraction.coerceIn(0f, 1f)).clip(RoundedCornerShape(50))
            .background(Brush.horizontalGradient(listOf(Color(0xFF6158F0), Mint, Color(0xFFFFE07A)))))
        Text(label, color = Color.White, fontWeight = FontWeight.Black, fontSize = if (height < 16) 10.sp else 11.sp)
    }
}

/**
 * O armário do Folha, com cara de jogo: o Folha no palco e os espaços de equipamento em volta dele (cada
 * um mostra a peça vestida, de perto); escolher um espaço abre o inventário daquela parte, com cartas por
 * raridade. Peça liberada veste na hora (ele comemora); peça trancada ele experimenta no palco e a carta
 * diz quanto falta. Em cima, o nível de colecionador e as raridades; as coleções têm aba própria.
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun WardrobeScreen(viewModel: AppViewModel, onBack: () -> Unit, showInternalTopBar: Boolean = true) {
    val context = LocalContext.current
    val progress by viewModel.progress.collectAsState()
    // A versão das escolhas: lida (não só observada) para a tela redesenhar a marcação "Equipado" a cada troca.
    val chosenVersion by br.com.estudario.data.FolhaLookStore.changes.collectAsState()
    val seenVersion = WardrobeSeen.version
    var slot by remember { mutableStateOf(FolhaSlot.CHAPEU) }
    var showCollections by remember { mutableStateOf(false) }
    var trying by remember { mutableStateOf<FolhaItem?>(null) }
    // a coleção inteira no palco (nome e peças), para ver como fica antes de ter
    var tryingSet by remember { mutableStateOf<Pair<String, List<FolhaItem>>?>(null) }
    LaunchedEffect(trying, slot, showCollections) { if (trying != null || !showCollections) tryingSet = null }
    var cheer by remember { mutableIntStateOf(0) }
    var mood by remember { mutableStateOf(FolhaMood.WAVE) }
    var testMood by remember { mutableStateOf<FolhaMood?>(null) }
    LaunchedEffect(cheer) {
        if (cheer == 0) { delay(1400); mood = FolhaMood.IDLE; return@LaunchedEffect }
        mood = FolhaMood.HAPPY; delay(1300); mood = FolhaMood.IDLE
    }
    LaunchedEffect(progress) { progress?.let { WardrobeSeen.init(context, it) } }
    LaunchedEffect(slot, showCollections, progress) {
        val summary = progress ?: return@LaunchedEffect
        if (showCollections) return@LaunchedEffect
        delay(2500); WardrobeSeen.markSeen(context, FolhaWardrobe.of(slot), summary)
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
        val chosen = remember(chosenVersion, summary) { FolhaSlot.entries.associateWith { FolhaWardrobePrefs.chosen(context, it, summary) } }
        val fresh = remember(seenVersion, summary) { FolhaWardrobe.items.filter { WardrobeSeen.isNew(context, it, summary) }.toSet() }
        val equip: (FolhaItem) -> Unit = { item ->
            if (FolhaWardrobe.unlocked(item, summary)) {
                trying = null
                if (chosen[item.slot]?.id != item.id) { FolhaWardrobePrefs.write(context, listOf(item)); cheer++ }
            } else trying = if (trying?.let { it.slot == item.slot && it.id == item.id } == true) null else item
        }
        // O Folha fica preso no alto (palco), com a faixa dos espaços e as abas logo abaixo; só a lista de
        // peças (ou de coleções) rola. Dá para escolher e ver o resultado ao mesmo tempo.
        Column(Modifier.fillMaxSize().padding(padding)) {
            Column(
                Modifier.fillMaxWidth().clip(RoundedCornerShape(bottomStart = 26.dp, bottomEnd = 26.dp))
                    .background(Brush.verticalGradient(listOf(GameTop, GameMid, GameBottom))).padding(start = 12.dp, end = 12.dp, top = 8.dp, bottom = 10.dp),
            ) {
                // placar numa linha: nível de colecionador, barra e atalhos
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Box(Modifier.size(36.dp).clip(RoundedCornerShape(12.dp)).background(Brush.linearGradient(listOf(Color(0xFFFFE59A), GoldG, Color(0xFFC98A12)))), contentAlignment = Alignment.Center) {
                        Icon(Icons.Outlined.MilitaryTech, null, Modifier.size(22.dp), tint = Color(0xFF5A3803))
                    }
                    Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                        Text("COLECIONADOR · " + collectorRank(owned, total).uppercase(), color = GameMute, fontSize = 9.5.sp, fontWeight = FontWeight.Black, letterSpacing = 1.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                        GameBar(owned.toFloat() / total, "$owned / $total peças", Modifier.fillMaxWidth(), height = 14)
                    }
                    Box(
                        Modifier.size(36.dp).clip(RoundedCornerShape(12.dp)).background(Color(0xFFFFB421))
                            .clickable { FolhaWardrobePrefs.write(context, FolhaWardrobe.random(summary).values); trying = null; cheer++ },
                        contentAlignment = Alignment.Center,
                    ) { Icon(Icons.Outlined.Casino, "Surpreenda-me", Modifier.size(20.dp), tint = Color(0xFF1D1670)) }
                    Box(
                        Modifier.size(36.dp).clip(RoundedCornerShape(12.dp)).background(Color.White.copy(alpha = 0.08f)).border(1.dp, GameLine, RoundedCornerShape(12.dp))
                            .clickable { FolhaWardrobePrefs.write(context, FolhaSlot.entries.map { FolhaWardrobe.default(it) }); trying = null; cheer++ },
                        contentAlignment = Alignment.Center,
                    ) { Icon(Icons.Outlined.RestartAlt, "Visual clássico", Modifier.size(20.dp), tint = Color.White) }
                }
                // o palco: o Folha no meio (com folga para o companheiro e o item na mão) e os movimentos ao lado
                val t = trying
                Row(Modifier.fillMaxWidth().height(200.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(Modifier.weight(1f).fillMaxHeight(), contentAlignment = Alignment.BottomCenter) {
                        Box(Modifier.padding(bottom = 6.dp).size(150.dp, 24.dp).clip(RoundedCornerShape(50))
                            .background(Brush.radialGradient(listOf(Color.White.copy(alpha = 0.18f), Color.White.copy(alpha = 0.03f))))
                            .border(1.5.dp, Color.White.copy(alpha = 0.12f), RoundedCornerShape(50)))
                        Box(Modifier.padding(bottom = 16.dp, start = 30.dp)) {
                            Folha(
                                128.dp, mood = testMood ?: if (t != null) (if (showcaseMood(t.slot) == FolhaMood.PRESENT) FolhaMood.PRESENT else FolhaMood.PROUD) else if (mood == FolhaMood.IDLE) showcaseMood(slot) else mood, onClick = {},
                                style = FolhaStyle(pieces = (tryingSet?.second?.associate { it.slot.key to it.id } ?: emptyMap()) + (t?.let { mapOf(it.slot.key to it.id) } ?: emptyMap()), noScene = true),
                            )
                        }
                    }
                    val ts = tryingSet
                    if (t == null && ts != null) {
                        Column(
                            Modifier.width(138.dp).clip(RoundedCornerShape(16.dp)).background(Color(0x99100C30)).border(1.dp, GoldG.copy(alpha = 0.6f), RoundedCornerShape(16.dp)).padding(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(4.dp),
                        ) {
                            Text("PROVANDO A COLEÇÃO", color = GoldG, fontSize = 8.5.sp, fontWeight = FontWeight.Black, textAlign = TextAlign.Center)
                            Text(ts.first, color = Color.White, fontWeight = FontWeight.Black, fontSize = 14.sp, textAlign = TextAlign.Center)
                            Text("Com as peças e a recompensa.", color = GameMute, fontSize = 10.sp, textAlign = TextAlign.Center, lineHeight = 13.sp)
                            Text("Voltar ao meu visual", color = Mint, fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.clickable { tryingSet = null }.padding(2.dp))
                        }
                    } else if (t != null) {
                        val rc = rarityColor(t.rarity)
                        Column(
                            Modifier.width(138.dp).clip(RoundedCornerShape(16.dp)).background(Color(0x99100C30)).border(1.dp, rc.copy(alpha = 0.6f), RoundedCornerShape(16.dp)).padding(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(4.dp),
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Outlined.Lock, null, Modifier.size(11.dp), tint = rc); Spacer(Modifier.width(3.dp))
                                Text("EXPERIMENTANDO", color = rc, fontSize = 8.5.sp, fontWeight = FontWeight.Black)
                            }
                            Text(t.name, color = Color.White, fontWeight = FontWeight.Black, fontSize = 13.sp, textAlign = TextAlign.Center, lineHeight = 15.sp)
                            Text(FolhaWardrobe.requirement(t), color = GameMute, fontSize = 10.sp, textAlign = TextAlign.Center, lineHeight = 13.sp, maxLines = 3, overflow = TextOverflow.Ellipsis)
                            FolhaWardrobe.progress(t, summary)?.let { (cur, target, unit) -> GameBar(cur.toFloat() / target.coerceAtLeast(1), "$cur / $target", Modifier.fillMaxWidth(), height = 13) }
                            Text("Voltar ao meu visual", color = Mint, fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.clickable { trying = null }.padding(2.dp))
                        }
                    } else {
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            TRY_MOODS.forEach { (m, label, icon) ->
                                val on = (testMood ?: FolhaMood.IDLE) == m
                                Box(
                                    Modifier.size(26.dp).clip(RoundedCornerShape(50)).background(if (on) Mint else Color.White.copy(alpha = 0.08f))
                                        .border(1.dp, GameLine, RoundedCornerShape(50)).clickable { testMood = if (m == FolhaMood.IDLE) null else m },
                                    contentAlignment = Alignment.Center,
                                ) { Icon(icon, label, Modifier.size(15.dp), tint = if (on) Color(0xFF0F3B2B) else Color.White) }
                            }
                        }
                    }
                }
                // abas
                val done = FolhaWardrobe.collections.count { c -> FolhaWardrobe.piecesOf(c).all { FolhaWardrobe.unlocked(it, summary) } }
                Row(Modifier.clip(RoundedCornerShape(14.dp)).background(Color.Black.copy(alpha = 0.25f)).padding(3.dp), horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                    GameTab("Inventário", Icons.Outlined.Checkroom, !showCollections) { showCollections = false }
                    GameTab("Coleções $done/${FolhaWardrobe.collections.size}", Icons.Outlined.EmojiEvents, showCollections) { showCollections = true; trying = null }
                }
                // os espaços numa faixa que rola de lado
                if (!showCollections) LazyRow(Modifier.padding(top = 8.dp), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    items(LEFT_SLOTS + RIGHT_SLOTS, key = { it.name }) { s -> EquipSlot(s, chosen.getValue(s), summary, fresh, s == slot) { slot = s; trying = null } }
                }
            }
            if (showCollections) {
                LazyColumn(Modifier.weight(1f).fillMaxWidth(), contentPadding = PaddingValues(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    item { Text("Toque numa peça para o Folha experimentar. Com as peças de uma coleção, a recompensa exclusiva é sua.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant) }
                    items(FolhaWardrobe.collections, key = { "col-" + it.id }) { c ->
                        CollectionRow(c, summary, trying, onDress = { items -> FolhaWardrobePrefs.write(context, items); trying = null; cheer++ },
                            onTry = { item -> trying = if (trying?.let { it.slot == item.slot && it.id == item.id } == true) null else item },
                            onTrySet = { set -> trying = null; tryingSet = if (tryingSet?.first == c.name) null else c.name to set }, tryingAll = tryingSet?.first == c.name)
                    }
                }
            } else {
                val list = FolhaWardrobe.of(slot).sortedWith(compareBy({ !FolhaWardrobe.unlocked(it, summary) }, { it.collection != null }))
                LazyVerticalGrid(GridCells.Fixed(3), Modifier.weight(1f).fillMaxWidth(), contentPadding = PaddingValues(12.dp), horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        Column {
                            Row(verticalAlignment = Alignment.Bottom) {
                                Text(slot.label, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black)
                                Spacer(Modifier.width(8.dp))
                                Text("${list.count { FolhaWardrobe.unlocked(it, summary) }} de ${list.size}", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(bottom = 2.dp))
                            }
                            Text(slot.hint, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                    items(list, key = { slot.name + it.id }) { item ->
                        ItemCard(
                            item, open = FolhaWardrobe.unlocked(item, summary), selected = chosen[slot]?.id == item.id,
                            trying = trying?.let { it.slot == item.slot && it.id == item.id } == true, isNew = item in fresh,
                            progress = FolhaWardrobe.progress(item, summary),
                        ) { equip(item) }
                    }
                }
            }
        }
    }
}

@Composable
private fun GameTab(label: String, icon: ImageVector, on: Boolean, onClick: () -> Unit) {
    Row(
        Modifier.clip(RoundedCornerShape(12.dp)).background(if (on) Color.White.copy(alpha = 0.14f) else Color.Transparent).clickable(onClick = onClick).padding(horizontal = 14.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Icon(icon, null, Modifier.size(18.dp), tint = if (on) Color.White else GameMute)
        Text(label, color = if (on) Color.White else GameMute, fontWeight = FontWeight.Black, fontSize = 14.sp)
    }
}

/** Um espaço de equipamento: a peça vestida (de perto), o nome da parte e o selo de novidade. */
@Composable
private fun EquipSlot(slot: FolhaSlot, item: FolhaItem, summary: ProgressEngine.ProgressSummary, fresh: Set<FolhaItem>, on: Boolean, onClick: () -> Unit) {
    val rc = rarityColor(item.rarity)
    val isNew = FolhaWardrobe.of(slot).any { it in fresh }
    Column(Modifier.width(60.dp).clickable(onClick = onClick), horizontalAlignment = Alignment.CenterHorizontally) {
        Box(contentAlignment = Alignment.Center) {
            Box(
                Modifier.size(if (on) 54.dp else 50.dp).clip(RoundedCornerShape(14.dp))
                    .background(Brush.linearGradient(listOf(Color.White.copy(alpha = 0.12f), Color.White.copy(alpha = 0.03f))))
                    .border(2.dp, if (on) Color(0xFFFFE07A) else rc.copy(alpha = 0.55f), RoundedCornerShape(14.dp)),
                contentAlignment = Alignment.Center,
            ) {
                // espaço vazio (sem óculos, sozinho...): o encaixe tracejado, como em jogo
                if (item.id == "nenhum" || item.id == "nada" || item.id == "nenhuma") Icon(Icons.Outlined.Add, null, Modifier.size(20.dp), tint = GameMute)
                else PieceThumb(item, 42, true)
            }
            if (isNew) Box(Modifier.align(Alignment.TopEnd).offset(x = 4.dp, y = (-4).dp).size(16.dp).clip(RoundedCornerShape(50)).background(Color(0xFFFF5C7A)), contentAlignment = Alignment.Center) {
                Text("!", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Black)
            }
        }
        Text(slot.label, color = if (on) Color(0xFFFFE07A) else Color.White, fontSize = 9.5.sp, fontWeight = FontWeight.Black, maxLines = 1, overflow = TextOverflow.Ellipsis, textAlign = TextAlign.Center)
    }
}

/** A carta de uma peça: moldura da raridade, miniatura de perto, nome e o estado (equipado, equipar ou o que falta). */
@Composable
private fun ItemCard(
    item: FolhaItem, open: Boolean, selected: Boolean, trying: Boolean, isNew: Boolean,
    progress: Triple<Int, Int, String>?, modifier: Modifier = Modifier, onClick: () -> Unit,
) {
    val rc = rarityColor(item.rarity)
    val border = when { selected -> Mint; trying -> Color(0xFFFFE07A); else -> rc.copy(alpha = 0.5f) }
    Box(
        modifier.clip(RoundedCornerShape(16.dp))
            .background(Brush.verticalGradient(listOf(rc.copy(alpha = 0.28f), Color(0xFF221C66), Color(0xFF15113F))))
            .border(2.dp, border, RoundedCornerShape(16.dp)).clickable(onClick = onClick),
    ) {
        Column(Modifier.fillMaxWidth().padding(top = 20.dp, bottom = 8.dp, start = 4.dp, end = 4.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(3.dp)) {
            Box(contentAlignment = Alignment.BottomEnd) {
                PieceThumb(item, 70, open)
                if (!open) Box(Modifier.size(22.dp).clip(RoundedCornerShape(50)).background(Color(0xD90A0828)), contentAlignment = Alignment.Center) {
                    Icon(Icons.Outlined.Lock, null, Modifier.size(13.dp), tint = Color.White)
                }
            }
            Text(item.name, color = Color.White, fontWeight = FontWeight.Black, fontSize = 12.sp, lineHeight = 14.sp, textAlign = TextAlign.Center, maxLines = 2, overflow = TextOverflow.Ellipsis, modifier = Modifier.heightIn(min = 28.dp))
            when {
                selected -> Text("Equipado", color = Color(0xFF0F3B2B), fontWeight = FontWeight.Black, fontSize = 10.sp, modifier = Modifier.clip(RoundedCornerShape(50)).background(Mint).padding(horizontal = 8.dp, vertical = 2.dp))
                open -> Text("Equipar", color = Color.White, fontWeight = FontWeight.Black, fontSize = 10.sp, modifier = Modifier.clip(RoundedCornerShape(50)).background(Color.White.copy(alpha = 0.1f)).padding(horizontal = 8.dp, vertical = 2.dp))
                else -> {
                    Text(
                        item.collection?.let { "Coleção " + (FolhaWardrobe.collection(it)?.name ?: "") } ?: item.level?.let { "Nível $it" } ?: BadgeCatalog.all.firstOrNull { it.id == item.badgeId }?.name ?: "Emblema",
                        color = GameMute, fontWeight = FontWeight.Bold, fontSize = 10.sp, maxLines = 1, overflow = TextOverflow.Ellipsis,
                    )
                    if (progress != null) Box(Modifier.fillMaxWidth(0.8f).height(4.dp).clip(RoundedCornerShape(50)).background(Color.White.copy(alpha = 0.12f))) {
                        Box(Modifier.fillMaxHeight().fillMaxWidth((progress.first.toFloat() / progress.second.coerceAtLeast(1)).coerceIn(0f, 1f)).clip(RoundedCornerShape(50)).background(rc))
                    }
                }
            }
        }
        Text(item.rarity.label.uppercase(), color = Color(0xFF120E38), fontWeight = FontWeight.Black, fontSize = 8.5.sp,
            modifier = Modifier.align(Alignment.TopStart).clip(RoundedCornerShape(bottomEnd = 10.dp)).background(rc).padding(horizontal = 7.dp, vertical = 2.dp))
        if (isNew) Text("NOVO", color = Color.White, fontWeight = FontWeight.Black, fontSize = 8.5.sp,
            modifier = Modifier.align(Alignment.TopEnd).padding(4.dp).clip(RoundedCornerShape(6.dp)).background(Color(0xFFFF5C7A)).padding(horizontal = 5.dp, vertical = 1.dp))
        else if (item.collection != null) Icon(Icons.Outlined.EmojiEvents, "Recompensa de coleção", Modifier.align(Alignment.TopEnd).padding(4.dp).size(15.dp), tint = GoldG)
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

/**
 * Uma coleção, compacta: nome e andamento, as peças e a recompensa lado a lado (tocar faz o Folha
 * experimentar no palco, que fica à vista). Completa, veste o conjunto inteiro num toque.
 */
@Composable
private fun CollectionRow(c: br.com.estudario.domain.FolhaCollection, summary: ProgressEngine.ProgressSummary, trying: FolhaItem?, onDress: (List<FolhaItem>) -> Unit, onTry: (FolhaItem) -> Unit, onTrySet: (List<FolhaItem>) -> Unit, tryingAll: Boolean) {
    val pieces = FolhaWardrobe.piecesOf(c)
    val reward = FolhaWardrobe.rewardOf(c) ?: return
    val have = pieces.count { FolhaWardrobe.unlocked(it, summary) }
    val complete = have == pieces.size
    fun isTrying(item: FolhaItem) = trying?.let { it.slot == item.slot && it.id == item.id } == true
    Column(
        Modifier.fillMaxWidth().clip(RoundedCornerShape(18.dp)).background(Brush.verticalGradient(listOf(Color(0xFF221C66), Color(0xFF15113F))))
            .border(1.dp, if (complete) GoldG.copy(alpha = 0.6f) else GameLine, RoundedCornerShape(18.dp)).padding(start = 12.dp, end = 12.dp, top = 10.dp, bottom = 10.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(c.name, color = Color.White, fontWeight = FontWeight.Black, fontSize = 15.sp)
            Text("$have/${pieces.size}", color = if (complete) Color(0xFF0F3B2B) else GameMute, fontWeight = FontWeight.Black, fontSize = 11.sp,
                modifier = Modifier.clip(RoundedCornerShape(50)).background(if (complete) Mint else Color.White.copy(alpha = 0.1f)).padding(horizontal = 8.dp, vertical = 2.dp))
            Spacer(Modifier.weight(1f))
            if (complete) Text("Vestir", color = Color(0xFF1D1670), fontWeight = FontWeight.Black, fontSize = 12.sp,
                modifier = Modifier.clip(RoundedCornerShape(10.dp)).background(Color(0xFFFFB421)).clickable { onDress(pieces + reward) }.padding(horizontal = 12.dp, vertical = 5.dp))
            else Text(if (tryingAll) "Provando" else "Provar", color = if (tryingAll) Color(0xFF0F3B2B) else Color.White, fontWeight = FontWeight.Black, fontSize = 12.sp,
                modifier = Modifier.clip(RoundedCornerShape(10.dp)).background(if (tryingAll) Mint else Color.White.copy(alpha = 0.1f)).border(1.dp, GameLine, RoundedCornerShape(10.dp)).clickable { onTrySet(pieces + reward) }.padding(horizontal = 12.dp, vertical = 5.dp))
        }
        Text(c.description, color = GameMute, fontSize = 11.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(5.dp)) {
            pieces.forEach { item ->
                val open = FolhaWardrobe.unlocked(item, summary)
                Box(
                    Modifier.weight(1f).aspectRatio(1f).clip(RoundedCornerShape(12.dp)).background(Color.White.copy(alpha = 0.06f))
                        .border(1.5.dp, if (isTrying(item)) Color(0xFFFFE07A) else Color.White.copy(alpha = 0.12f), RoundedCornerShape(12.dp)).clickable { onTry(item) },
                    contentAlignment = Alignment.Center,
                ) {
                    PieceThumb(item, 50, open)
                    if (!open) Box(Modifier.align(Alignment.BottomEnd).padding(2.dp).size(16.dp).clip(RoundedCornerShape(50)).background(Color(0xD90A0828)), contentAlignment = Alignment.Center) {
                        Icon(Icons.Outlined.Lock, null, Modifier.size(10.dp), tint = Color.White)
                    }
                }
            }
            Icon(Icons.Outlined.ChevronRight, null, Modifier.size(16.dp), tint = GameMute)
            val rc = rarityColor(reward.rarity)
            Box(
                Modifier.weight(1f).aspectRatio(1f).clip(RoundedCornerShape(12.dp)).background(Brush.radialGradient(listOf(rc.copy(alpha = 0.35f), Color.White.copy(alpha = 0.04f))))
                    .border(2.dp, if (isTrying(reward)) Color(0xFFFFE07A) else rc, RoundedCornerShape(12.dp)).clickable { onTry(reward) },
                contentAlignment = Alignment.Center,
            ) {
                PieceThumb(reward, 50, complete)
                Box(Modifier.align(Alignment.TopEnd).padding(2.dp).size(16.dp).clip(RoundedCornerShape(50)).background(GoldG), contentAlignment = Alignment.Center) {
                    Icon(Icons.Outlined.EmojiEvents, "Recompensa exclusiva", Modifier.size(10.dp), tint = Color(0xFF5A3803))
                }
            }
        }
        Box(Modifier.fillMaxWidth().height(5.dp).clip(RoundedCornerShape(50)).background(Color.White.copy(alpha = 0.1f))) {
            Box(Modifier.fillMaxHeight().fillMaxWidth(have.toFloat() / pieces.size).clip(RoundedCornerShape(50)).background(Brush.horizontalGradient(if (complete) listOf(Mint, Color(0xFFFFE07A)) else listOf(Color(0xFF6158F0), Mint))))
        }
    }
}

/** O Folha vestindo uma peça (o resto do visual como está), em imagem parada. */
@Composable
private fun PieceThumb(item: FolhaItem, sizeDp: Int, open: Boolean) {
    val px = with(LocalDensity.current) { sizeDp.dp.roundToPx() }
    val thumb = remember(item.slot, item.id, px, FolhaLook.pieces()) {
        br.com.estudario.ui.assistant.folhaBitmap(px, showcaseMood(item.slot), FolhaStyle(pieces = FolhaLook.pieces() + (item.slot.key to item.id), plain = true), focus(item.slot)).asImageBitmap()
    }
    androidx.compose.foundation.Image(thumb, null, Modifier.size(sizeDp.dp).alpha(if (open) 1f else 0.5f), colorFilter = if (open) null else androidx.compose.ui.graphics.ColorFilter.colorMatrix(androidx.compose.ui.graphics.ColorMatrix().apply { setToSaturation(0.15f) }))
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
