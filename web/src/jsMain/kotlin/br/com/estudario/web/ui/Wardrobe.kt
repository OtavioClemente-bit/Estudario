package br.com.estudario.web.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import br.com.estudario.domain.BadgeCatalog
import br.com.estudario.domain.FolhaItem
import br.com.estudario.domain.FolhaRarity
import br.com.estudario.domain.FolhaSlot
import br.com.estudario.domain.FolhaWardrobe
import br.com.estudario.domain.ProgressEngine
import br.com.estudario.web.Route
import br.com.estudario.web.data.Progress
import br.com.estudario.web.data.ProgressView
import br.com.estudario.web.data.Prefs
import br.com.estudario.web.data.Store
import kotlinx.browser.localStorage
import kotlinx.browser.window
import org.jetbrains.compose.web.attributes.ButtonType
import org.jetbrains.compose.web.attributes.type
import org.jetbrains.compose.web.dom.B
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.ElementBuilder
import org.jetbrains.compose.web.dom.Img
import org.jetbrains.compose.web.dom.Li
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.TagElement
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.dom.Ul

/**
 * O visual do Folha neste navegador: as peças escolhidas no armário e o contexto do dia (sequência,
 * risco de perder a sequência). Tudo vai para o desenho por window.folhaSetLook; [version] muda junto
 * para os Folhas parados (PNG) se redesenharem.
 */
object FolhaLook {
    /** Muda quando o desenho recebe um visual novo (os Folhas parados se redesenham). */
    var version by mutableIntStateOf(0)
        private set
    /** Muda quando a pessoa troca uma peça (o armário e a sincronização releem a escolha). */
    var choices by mutableIntStateOf(0)
        private set

    private fun storageKey(slot: FolhaSlot) = "estudario.folha.${slot.name.lowercase()}"
    private const val UPDATED_KEY = "estudario.folha.updatedAt"
    /** Chave na foto sincronizada da conta (a mesma do app, FolhaLookStore.kt). */
    private const val SNAPSHOT_KEY = "folhaLook"
    private fun read(key: String): String? = runCatching { localStorage.getItem(key) }.getOrNull()

    /** O visual guardado na conta (vindo do app ou deste site), se for mais novo que o deste navegador. */
    private fun cloudLook(): kotlinx.serialization.json.JsonObject? {
        val cloud = Store.data.obj(SNAPSHOT_KEY) ?: return null
        val cloudAt = (cloud["updatedAt"] as? kotlinx.serialization.json.JsonPrimitive)?.content?.toDoubleOrNull() ?: 0.0
        val localAt = read(UPDATED_KEY)?.toDoubleOrNull() ?: 0.0
        return cloud.takeIf { cloudAt >= localAt }
    }

    fun chosen(slot: FolhaSlot, summary: ProgressEngine.ProgressSummary): FolhaItem {
        val saved = (cloudLook()?.get(slot.key) as? kotlinx.serialization.json.JsonPrimitive)?.content
            ?: read(storageKey(slot))
            // escolha da versão antiga (um "acessório" só) vale para a categoria nova
            ?: FolhaWardrobe.legacyAccessory(read("estudario.folha.acessorio"))?.takeIf { it.slot == slot }?.id
        return FolhaWardrobe.resolve(slot, saved, summary)
    }

    fun choose(item: FolhaItem) = chooseAll(listOf(item))

    /**
     * Troca peças: guarda neste navegador e na foto da conta (o app recebe na próxima sincronização).
     * Vai o visual inteiro, com o horário da troca; vale sempre a troca mais recente.
     */
    fun chooseAll(items: Collection<FolhaItem>) {
        val now = kotlin.js.Date.now()
        val summary = Progress.of(Store.data).progress
        val current = FolhaSlot.entries.associate { it.key to chosen(it, summary).id } + items.associate { it.slot.key to it.id }
        items.forEach { runCatching { localStorage.setItem(storageKey(it.slot), it.id) } }
        runCatching { localStorage.setItem(UPDATED_KEY, now.toLong().toString()) }
        val look = kotlinx.serialization.json.JsonObject(current.mapValues { kotlinx.serialization.json.JsonPrimitive(it.value) } + ("updatedAt" to kotlinx.serialization.json.JsonPrimitive(now.toLong())))
        Store.update { it.withObject(SNAPSHOT_KEY, look) }
        choices++
    }

    /** Manda para o desenho. A sequência corre risco do fim da tarde em diante, se hoje ainda não fechou. */
    fun push(view: ProgressView) {
        val hour = kotlin.js.Date().getHours()
        val streak = view.streak
        val look = kotlin.js.json(
            "streak" to streak.current,
            "atRisk" to (streak.current >= 2 && !streak.todayDone && hour >= 18),
            // a amizade (gestos e falas novas) e o nome, para ele chamar a pessoa
            "bond" to br.com.estudario.domain.FolhaBond.level(streak.activeDays).index,
            "name" to (FolhaFriend.firstName() ?: ""),
            "bondLines" to br.com.estudario.domain.FolhaMemory.bondTapLines(br.com.estudario.domain.FolhaBond.level(streak.activeDays).index, FolhaFriend.firstName()).toTypedArray(),
            // as falas de toque, carinho e volta: o mesmo banco do app
            "tapLines" to br.com.estudario.domain.FolhaFalas.tap.toTypedArray(),
            "tapMorning" to br.com.estudario.domain.FolhaFalas.tapMorning.toTypedArray(),
            "tapNight" to br.com.estudario.domain.FolhaFalas.tapNight.toTypedArray(),
            "petLines" to br.com.estudario.domain.FolhaFalas.pet.toTypedArray(),
            "backLines" to br.com.estudario.domain.FolhaFalas.back.toTypedArray(),
        )
        FolhaSlot.entries.forEach { look[it.key] = chosen(it, view.progress).id }
        runCatching { window.asDynamic().folhaSetLook(look) }
        version++
    }
}

/** Mantém o desenho em dia com o armário, o progresso e a hora (o risco da sequência muda com o relógio). */
@Composable
fun FolhaLookSync() {
    val view = Progress.of(Store.data)
    var hour by remember { mutableIntStateOf(kotlin.js.Date().getHours()) }
    LaunchedEffect(Unit) {
        while (true) { kotlinx.coroutines.delay(60_000); hour = kotlin.js.Date().getHours() }
    }
    LaunchedEffect(view, hour, FolhaLook.choices) { FolhaLook.push(view) }
}

private fun rarityClass(r: FolhaRarity) = "r-${r.name.lowercase()}"

/** Movimentos para ver as peças em ação no palco (rótulo e ícone). */
private val TRY_MOODS = listOf(
    Triple("idle", "Parado", "person"), Triple("present", "Mostrar", "back_hand"), Triple("wave", "Acenar", "waving_hand"),
    Triple("happy", "Comemorar", "celebration"), Triple("thinking", "Estudar", "menu_book"), Triple("ecstatic", "Em chamas", "local_fire_department"),
    Triple("sleepy", "Cochilar", "bedtime"),
)

/** Na vitrine e nas miniaturas, mãos e itens aparecem melhor com ele mostrando (mãos para a frente). */
private fun showcaseMood(slot: FolhaSlot) = if (slot == FolhaSlot.MAO || slot == FolhaSlot.LUVAS) "present" else "idle"

/**
 * Onde a miniatura chega perto (zoom e o ponto na grade 100 x 100 do desenho): o chapéu na cabeça, os
 * óculos no rosto, o item na mão... Capa, aura e costas aparecem no Folha inteiro.
 */
private fun focus(slot: FolhaSlot): Triple<Double, Int, Int>? = when (slot) {
    FolhaSlot.CHAPEU -> Triple(2.0, 50, 27)
    FolhaSlot.OCULOS -> Triple(1.8, 50, 60)
    FolhaSlot.PAGINAS -> Triple(1.25, 50, 63)
    FolhaSlot.TINTA, FolhaSlot.BOCHECHAS -> Triple(1.75, 50, 63)
    FolhaSlot.PESCOCO -> Triple(1.45, 50, 76)
    FolhaSlot.MAO -> Triple(1.6, 7, 30)
    FolhaSlot.FITA -> Triple(2.4, 53, 91)
    FolhaSlot.COMPANHEIRO -> Triple(1.45, -4, 80)
    else -> null
}

/** Os espaços de equipamento em volta do Folha, como num jogo: acessórios à esquerda, o próprio Folha à direita. */
private val LEFT_SLOTS = listOf(FolhaSlot.CHAPEU, FolhaSlot.OCULOS, FolhaSlot.PESCOCO, FolhaSlot.MAO, FolhaSlot.LUVAS, FolhaSlot.COSTAS)
private val RIGHT_SLOTS = listOf(FolhaSlot.CAPA, FolhaSlot.PAGINAS, FolhaSlot.TINTA, FolhaSlot.BOCHECHAS, FolhaSlot.FITA, FolhaSlot.AURA, FolhaSlot.COMPANHEIRO)

/** Título de colecionador pelo tanto de peças liberadas. */
private fun collectorRank(owned: Int, total: Int): String {
    val f = owned.toDouble() / total
    return when { f < 0.2 -> "Iniciante"; f < 0.4 -> "Estiloso"; f < 0.6 -> "Colecionador"; f < 0.85 -> "Fashionista"; else -> "Lenda do armário" }
}

/**
 * Peças já vistas no armário, neste navegador: o que foi liberado e ainda não foi visto ganha o selo
 * "NOVO" (no espaço de equipamento e na carta). Na primeira vez, o que já existia entra como visto.
 */
private object WardrobeSeen {
    private const val KEY = "estudario.folha.vistos"
    var version by mutableIntStateOf(0)
        private set
    private fun key(item: FolhaItem) = item.slot.name + ":" + item.id
    private fun local(): Set<String>? = runCatching { localStorage.getItem(KEY) }.getOrNull()?.split(",")?.filter { it.isNotBlank() }?.toSet()
    /** Vistas aqui ou em qualquer aparelho da conta: peça vista no celular não volta como "NOVO" aqui. */
    private fun read(): Set<String>? = local()?.let { it + Prefs.syncedSet("wardrobeSeen") } ?: Prefs.syncedSet("wardrobeSeen").takeIf { it.isNotEmpty() }
    private fun write(v: Set<String>) {
        val all = v + Prefs.syncedSet("wardrobeSeen")
        runCatching { localStorage.setItem(KEY, all.sorted().joinToString(",")) }
        Prefs.addSynced("wardrobeSeen", all)
        version++
    }
    fun isNew(item: FolhaItem, progress: ProgressEngine.ProgressSummary): Boolean {
        @Suppress("UNUSED_VARIABLE") val v = version
        val seen = read() ?: return false
        return FolhaWardrobe.unlocked(item, progress) && key(item) !in seen
    }
    /** Primeira vez neste navegador: tudo o que já está liberado entra como visto. */
    fun init(progress: ProgressEngine.ProgressSummary) { if (local() == null) write(FolhaWardrobe.items.filter { FolhaWardrobe.unlocked(it, progress) }.map(::key).toSet()) }
    fun markSeen(items: List<FolhaItem>, progress: ProgressEngine.ProgressSummary) {
        val seen = read() ?: emptySet()
        val add = items.filter { FolhaWardrobe.unlocked(it, progress) }.map(::key).filter { it !in seen }
        if (add.isNotEmpty()) write(seen + add)
    }
}

// ------------------------------------------------------------------ armário

/**
 * O armário do Folha, com cara de jogo: o Folha no palco e os espaços de equipamento em volta dele
 * (cada um mostra a peça vestida); escolher um espaço abre o inventário daquela parte, com cartas por
 * raridade. Peça liberada veste na hora (ele comemora); peça trancada ele experimenta no palco e a carta
 * diz quanto falta. Em cima, o nível de colecionador e as raridades; as coleções têm aba própria.
 */
@Composable
fun WardrobeScreen() {
    val progress = Progress.of(Store.data).progress
    @Suppress("UNUSED_VARIABLE") val chosenNow = FolhaLook.choices
    @Suppress("UNUSED_VARIABLE") val seenNow = WardrobeSeen.version
    var slot by remember { mutableStateOf(FolhaSlot.CHAPEU) }
    var showCollections by remember { mutableStateOf(false) }
    var trying by remember { mutableStateOf<FolhaItem?>(null) }
    // a coleção inteira no palco (nome e peças), para ver como fica antes de ter
    var tryingSet by remember { mutableStateOf<Pair<String, List<FolhaItem>>?>(null) }
    LaunchedEffect(trying, slot, showCollections) { if (trying != null || !showCollections) tryingSet = null }
    var cheer by remember { mutableIntStateOf(0) }
    var mood by remember { mutableStateOf("wave") }
    var testMood by remember { mutableStateOf<String?>(null) }
    LaunchedEffect(cheer) {
        if (cheer == 0) { kotlinx.coroutines.delay(1400); mood = "idle"; return@LaunchedEffect }
        mood = "happy"; kotlinx.coroutines.delay(1300); mood = "idle"
    }
    LaunchedEffect(Unit) { WardrobeSeen.init(progress) }
    // abrir um espaço marca as peças dele como vistas (o selo "NOVO" some depois de um instante)
    LaunchedEffect(slot, showCollections) {
        if (showCollections) return@LaunchedEffect
        kotlinx.coroutines.delay(2500); WardrobeSeen.markSeen(FolhaWardrobe.of(slot), progress)
    }
    val owned = FolhaWardrobe.items.count { FolhaWardrobe.unlocked(it, progress) }
    val total = FolhaWardrobe.items.size
    val equip: (FolhaItem) -> Unit = { item ->
        if (FolhaWardrobe.unlocked(item, progress)) {
            trying = null
            if (FolhaLook.chosen(item.slot, progress).id != item.id) { FolhaLook.choose(item); cheer++ }
        } else trying = if (trying?.let { it.slot == item.slot && it.id == item.id } == true) null else item
    }
    BackToProfile()
    PageHead("Armário do Folha", "Equipe o Folha como num jogo: cada espaço em volta dele é uma parte do visual. As peças saem das suas conquistas.")
    // Duas colunas: o Folha fica parado à esquerda (no celular, preso no alto) enquanto a pessoa escolhe à
    // direita, então dá para trocar e ver ao mesmo tempo, sem rolar até o boneco.
    Div({ classes("wdg", "wdg2") }) {
        Div({ classes("wdg-side") }) {
            // ---- placar compacto: nível de colecionador e atalhos
            Div({ classes("wdg-hud", "compact") }) {
                Div({ classes("wdg-rank") }) {
                    org.jetbrains.compose.web.dom.Img(src = MEDAL_SVG, alt = "") { classes("wdg-rank-medal") }
                    Div({ classes("grow") }) {
                        Span({ classes("wdg-eyebrow") }) { Text("COLECIONADOR · " + collectorRank(owned, total).uppercase()) }
                        Div({ classes("wdg-xp") }) {
                            Span({ attr("style", "width:${(owned * 100.0 / total).toInt()}%") })
                            Span({ classes("wdg-xp-n") }) { Text("$owned / $total peças") }
                        }
                    }
                }
                Div({ classes("wdg-hud-actions") }) {
                    Button({ type(ButtonType.Button); classes("wdg-btn", "small"); attr("title", "Surpreenda-me: um visual sorteado"); onClick { FolhaLook.chooseAll(FolhaWardrobe.random(progress).values); trying = null; cheer++ } }) { Icon("casino"); Span({ classes("wdg-btn-l") }) { Text("Sortear") } }
                    Button({ type(ButtonType.Button); classes("wdg-btn", "ghost", "small"); attr("title", "Voltar ao visual clássico"); onClick { FolhaLook.chooseAll(FolhaSlot.entries.map { FolhaWardrobe.default(it) }); trying = null; cheer++ } }) { Icon("restart_alt"); Span({ classes("wdg-btn-l") }) { Text("Clássico") } }
                }
            }
            // ---- o palco
            Div({ classes("wdg-stage-card") }) {
                Div({ classes("wdg-stage") }) {
                    val t = trying
                    Div({ classes("wdg-spot"); attr("aria-hidden", "true") })
                    key(cheer) { if (cheer > 0) Div({ classes("wdg-burst"); attr("aria-hidden", "true") }) { repeat(10) { i -> Span({ attr("style", "--a:${i * 36}deg;--d:${i % 3 * 0.05}s") }) } } }
                    Div({ classes("wdg-folha") }) {
                        // Experimentando uma peça trancada: só neste palco; o resto do site continua com a escolha salva.
                        TagElement<org.w3c.dom.HTMLElement>(
                            elementBuilder = ElementBuilder.createBuilder("estudario-folha"),
                            applyAttrs = {
                                classes("folha", "folha-live")
                                attr("mood", testMood ?: if (t != null) (if (showcaseMood(t.slot) == "present") "present" else "proud") else if (mood == "idle") showcaseMood(slot) else mood)
                                attr("size", if (window.innerWidth < 860) "150" else "210")
                                attr("clickable", "")
                                attr("noscene", "")
                                tryingSet?.second?.forEach { attr(it.slot.key, it.id) }
                                if (t != null) attr(t.slot.key, t.id)
                            },
                            content = null,
                        )
                    }
                    val ts = tryingSet
                    if (t == null && ts != null) {
                        Div({ classes("wdg-try", "r-lendaria") }) {
                            Span({ classes("wdg-try-tag") }) { Icon("visibility", filled = true); Text("Provando a coleção") }
                            B { Text(ts.first) }
                            Span({ classes("wdg-try-req") }) { Text("Assim o Folha fica com as peças e a recompensa.") }
                            Button({ type(ButtonType.Button); classes("wdg-btn", "ghost", "small"); onClick { tryingSet = null } }) { Icon("undo"); Text("Voltar ao meu visual") }
                        }
                    } else if (t != null) {
                        Div({ classes("wdg-try", rarityClass(t.rarity)) }) {
                            Span({ classes("wdg-try-tag") }) { Icon("lock", filled = true); Text("Experimentando · ${t.rarity.label}") }
                            B { Text(t.name) }
                            Span({ classes("wdg-try-req") }) { Text(FolhaWardrobe.requirement(t)) }
                            FolhaWardrobe.progress(t, progress)?.let { (cur, target, unit) ->
                                Div({ classes("wdg-xp", "small") }) {
                                    Span({ attr("style", "width:${(cur * 100.0 / target.coerceAtLeast(1)).toInt()}%") })
                                    Span({ classes("wdg-xp-n") }) { Text("$cur / $target $unit") }
                                }
                            }
                            Button({ type(ButtonType.Button); classes("wdg-btn", "ghost", "small"); onClick { trying = null } }) { Icon("undo"); Text("Voltar ao meu visual") }
                        }
                    } else {
                        Div({ classes("wdg-emotes"); attr("aria-label", "Testar movimentos") }) {
                            TRY_MOODS.forEach { (m, label, icon) ->
                                val on = (testMood ?: "idle") == m
                                Button({
                                    type(ButtonType.Button)
                                    classes(*listOfNotNull("wdg-emote", if (on) "on" else null).toTypedArray())
                                    attr("title", label); attr("aria-label", label); attr("aria-pressed", on.toString())
                                    onClick { testMood = if (m == "idle") null else m }
                                }) { Icon(icon) }
                            }
                        }
                    }
                }
            }
        }

        // ---- à direita: os espaços numa faixa, o inventário do espaço escolhido ou as coleções
        Div({ classes("wdg-main") }) {
            Div({ classes("wdg-inv") }) {
                Div({ classes("wdg-inv-tabs"); attr("role", "tablist") }) {
                    Button({
                        type(ButtonType.Button); classes(*listOfNotNull("wdg-tab", if (!showCollections) "on" else null).toTypedArray()); attr("role", "tab")
                        onClick { showCollections = false }
                    }) { Icon("checkroom"); Text("Inventário") }
                    val done = FolhaWardrobe.collections.count { c -> FolhaWardrobe.piecesOf(c).all { FolhaWardrobe.unlocked(it, progress) } }
                    Button({
                        type(ButtonType.Button); classes(*listOfNotNull("wdg-tab", if (showCollections) "on" else null).toTypedArray()); attr("role", "tab")
                        onClick { showCollections = true; trying = null }
                    }) { Icon("emoji_events"); Text("Coleções"); Span({ classes("wdg-tab-n") }) { Text("$done/${FolhaWardrobe.collections.size}") } }
                }
                if (showCollections) {
                    P({ classes("wdg-hint") }) { Text("Toque numa peça para o Folha experimentar. Com as peças de uma coleção, a recompensa exclusiva é sua.") }
                    Div({ classes("wd-crows") }) {
                        FolhaWardrobe.collections.forEach { c -> key(c.id) { CollectionRow(c, progress, trying, onDress = { items -> FolhaLook.chooseAll(items); trying = null; cheer++ }, onTry = { trying = if (trying?.let { t -> t.slot == it.slot && t.id == it.id } == true) null else it }, onTrySet = { set -> trying = null; tryingSet = if (tryingSet?.first == c.name) null else c.name to set }, tryingAll = tryingSet?.first == c.name) } }
                    }
                } else {
                    Div({ classes("wdg-strip"); attr("role", "tablist"); attr("aria-label", "Partes do visual") }) {
                        (LEFT_SLOTS + RIGHT_SLOTS).forEach { s -> EquipSlot(s, progress, s == slot) { slot = s; trying = null } }
                    }
                    val list = FolhaWardrobe.of(slot).sortedWith(compareBy({ !FolhaWardrobe.unlocked(it, progress) }, { it.collection != null }))
                    Div({ classes("wdg-inv-head") }) {
                        Div {
                            B { Text(slot.label) }
                            Span({ classes("wdg-inv-n") }) { Text("${list.count { FolhaWardrobe.unlocked(it, progress) }} de ${list.size}") }
                            P({ classes("wdg-hint") }) { Text(slot.hint) }
                        }
                    }
                    Div({ classes("wdg-grid") }) {
                        list.forEach { item ->
                            key(slot.name + item.id) {
                                ItemCard(
                                    item, progress,
                                    selected = FolhaLook.chosen(slot, progress).id == item.id,
                                    trying = trying?.let { it.slot == item.slot && it.id == item.id } == true,
                                ) { equip(item) }
                            }
                        }
                    }
                }
            }
        }
    }
}

/** Um espaço de equipamento: a peça vestida, o nome da parte, quantas tem e o selo de novidade. */
@Composable
private fun EquipSlot(slot: FolhaSlot, progress: ProgressEngine.ProgressSummary, on: Boolean, onClick: () -> Unit) {
    val item = FolhaLook.chosen(slot, progress)
    val list = FolhaWardrobe.of(slot)
    val have = list.count { FolhaWardrobe.unlocked(it, progress) }
    val fresh = list.any { WardrobeSeen.isNew(it, progress) }
    Button({
        type(ButtonType.Button)
        classes(*listOfNotNull("wdg-slot", rarityClass(item.rarity), if (on) "on" else null).toTypedArray())
        attr("aria-pressed", on.toString()); attr("title", "${slot.label}: ${item.name}")
        onClick { onClick() }
    }) {
        Span({ classes("wdg-slot-box") }) {
            // espaço vazio (sem óculos, sozinho...): o encaixe tracejado, como em jogo
            if (item.id == "nenhum" || item.id == "nada" || item.id == "nenhuma") Span({ classes("wdg-slot-empty") }) { Icon("add") } else PieceThumb(item, 52)
            if (fresh) Span({ classes("wdg-new-dot") }) { Text("!") }
        }
        Span({ classes("wdg-slot-l") }) { Text(slot.label) }
        Span({ classes("wdg-slot-n") }) { Text("$have/${list.size}") }
    }
}

/** A carta de uma peça no inventário: moldura da raridade, miniatura, nome e o estado (equipado, equipar ou o que falta). */
@Composable
private fun ItemCard(item: FolhaItem, progress: ProgressEngine.ProgressSummary, selected: Boolean, trying: Boolean, onClick: () -> Unit) {
    val open = FolhaWardrobe.unlocked(item, progress)
    val fresh = WardrobeSeen.isNew(item, progress)
    Button({
        type(ButtonType.Button)
        classes(*listOfNotNull("wdg-item", rarityClass(item.rarity), if (selected) "on" else null, if (!open) "locked" else null, if (trying) "trying" else null).toTypedArray())
        attr("aria-pressed", selected.toString())
        attr("title", if (open) item.name else "${item.name} · ${FolhaWardrobe.requirement(item)}")
        onClick { onClick() }
    }) {
        Span({ classes("wdg-item-rar") }) { Text(item.rarity.label) }
        if (fresh) Span({ classes("wdg-new") }) { Text("NOVO") }
        if (item.collection != null) Span({ classes("wdg-excl"); attr("title", "Recompensa de coleção") }) { Icon("emoji_events", filled = true) }
        Span({ classes("wdg-item-art") }) {
            PieceThumb(item, 84)
            if (!open) Span({ classes("wdg-lock") }) { Icon("lock", filled = true) }
        }
        B({ classes("wdg-item-name") }) { Text(item.name) }
        when {
            selected -> Span({ classes("wdg-item-state", "on") }) { Icon("check_circle", filled = true); Text("Equipado") }
            open -> Span({ classes("wdg-item-state") }) { Text("Equipar") }
            else -> {
                Span({ classes("wdg-item-state", "lock") }) {
                    Text(item.collection?.let { "Coleção " + (FolhaWardrobe.collection(it)?.name ?: "") } ?: item.level?.let { "Nível $it" } ?: BadgeCatalog.all.firstOrNull { it.id == item.badgeId }?.name ?: "Emblema")
                }
                FolhaWardrobe.progress(item, progress)?.let { (cur, target, _) ->
                    Span({ classes("wdg-mini") }) { Span({ attr("style", "width:${(cur * 100.0 / target.coerceAtLeast(1)).toInt()}%") }) }
                }
            }
        }
    }
}

/**
 * Uma coleção, compacta: nome e andamento, as peças e a recompensa lado a lado (tocar faz o Folha
 * experimentar no palco, que fica à vista). Completa, veste o conjunto inteiro num toque.
 */
@Composable
private fun CollectionRow(c: br.com.estudario.domain.FolhaCollection, progress: ProgressEngine.ProgressSummary, trying: FolhaItem?, onDress: (List<FolhaItem>) -> Unit, onTry: (FolhaItem) -> Unit, onTrySet: (List<FolhaItem>) -> Unit, tryingAll: Boolean) {
    val pieces = FolhaWardrobe.piecesOf(c)
    val reward = FolhaWardrobe.rewardOf(c) ?: return
    val have = pieces.count { FolhaWardrobe.unlocked(it, progress) }
    val complete = have == pieces.size
    val isTrying: (FolhaItem) -> Boolean = { item -> trying?.let { it.slot == item.slot && it.id == item.id } == true }
    Div({ classes(*listOfNotNull("wd-crow", if (complete) "done" else null).toTypedArray()) }) {
        Div({ classes("wd-crow-head") }) {
            B { Text(c.name) }
            Span({ classes("wd-crow-n") }) { if (complete) Icon("check_circle", filled = true); Text("$have/${pieces.size}") }
            if (complete) Button({ type(ButtonType.Button); classes("wdg-btn", "small", "wd-crow-dress"); onClick { onDress(pieces + reward) } }) { Icon("checkroom"); Text("Vestir") }
            else Button({ type(ButtonType.Button); classes(*listOfNotNull("wdg-btn", "ghost", "small", "wd-crow-dress", if (tryingAll) "on" else null).toTypedArray()); attr("title", "Ver o Folha com a coleção inteira"); onClick { onTrySet(pieces + reward) } }) { Icon("visibility"); Text(if (tryingAll) "Provando" else "Provar") }
        }
        P({ classes("wd-crow-desc"); attr("title", c.description) }) { Text(c.description) }
        Div({ classes("wd-crow-row") }) {
            pieces.forEach { item ->
                val open = FolhaWardrobe.unlocked(item, progress)
                Button({
                    type(ButtonType.Button)
                    classes(*listOfNotNull("wd-crow-piece", if (!open) "locked" else null, if (isTrying(item)) "trying" else null).toTypedArray())
                    attr("title", if (open) "${item.name} (${item.slot.label.lowercase()})" else "${item.name} · ${FolhaWardrobe.requirement(item)}")
                    onClick { onTry(item) }
                }) {
                    PieceThumb(item, 54)
                    if (!open) Span({ classes("wd-crow-lock") }) { Icon("lock", filled = true) }
                }
            }
            Span({ classes("wd-crow-arrow"); attr("aria-hidden", "true") }) { Icon("arrow_forward") }
            Button({
                type(ButtonType.Button)
                classes(*listOfNotNull("wd-crow-piece", "reward", rarityClass(reward.rarity), if (!complete) "locked" else null, if (isTrying(reward)) "trying" else null).toTypedArray())
                attr("title", "${reward.name} · recompensa exclusiva")
                onClick { onTry(reward) }
            }) {
                PieceThumb(reward, 54)
                Span({ classes("wd-crow-trophy") }) { Icon("emoji_events", filled = true) }
            }
        }
        Div({ classes("wd-crow-bar") }) { Span({ attr("style", "width:${(have * 100.0 / pieces.size).toInt()}%") }) }
    }
}

/** O Folha vestindo uma peça (o resto do visual como está), em imagem parada, de perto onde a peça fica. */
@Composable
private fun PieceThumb(item: FolhaItem, size: Int) {
    val v = FolhaLook.version
    val src = remember(item.slot, item.id, size, v) {
        val o = kotlin.js.json("mood" to showcaseMood(item.slot), "size" to size, item.slot.key to item.id)
        focus(item.slot)?.let { (z, x, y) -> o["zoom"] = z; o["fx"] = x; o["fy"] = y }
        runCatching { window.asDynamic().folhaPng(o) as? String }.getOrNull() ?: "icon.png"
    }
    Img(src = src, alt = "", attrs = { classes("wd-thumb"); attr("style", "width:${size}px;height:${size}px"); attr("aria-hidden", "true") })
}

@Composable
private fun AutoDetail(icon: String, title: String, text: String) {
    Li {
        Icon(icon)
        Div { B { Text(title) }; Span({ classes("small", "muted") }) { Text(" · $text") } }
    }
}

/** Cartão do Perfil: o Folha como está e o atalho para o armário. */
@Composable
fun WardrobeCard(progress: ProgressEngine.ProgressSummary) {
    val owned = FolhaWardrobe.items.count { FolhaWardrobe.unlocked(it, progress) }
    val next = FolhaWardrobe.next(progress)
    org.jetbrains.compose.web.dom.A(href = "#/${Route.Wardrobe.path}", { classes("card", "pad-lg", "wd-card") }) {
        Div({ classes("wd-card-folha") }) { Folha("wave", 64, live = false) }
        Div({ classes("grow") }) {
            Div({ classes("eyebrow") }) { Text("ARMÁRIO DO FOLHA") }
            B { Text("$owned de ${FolhaWardrobe.items.size} peças liberadas") }
            if (next != null) P({ classes("xs", "muted"); attr("style", "margin:2px 0 0") }) { Text("Próxima: ${next.first.name} (${(next.second * 100).toInt()}%)") }
        }
        Icon("chevron_right", extraClass = "faint")
    }
}

/** Para a comemoração de emblema: as peças que esse emblema libera no armário. */
fun wardrobeUnlockedBy(badgeId: String): List<FolhaItem> = FolhaWardrobe.unlockedBy(badgeId)

/** A medalha do colecionador (desenho em web/src/jsMain/resources/medalha-colecionador.svg), embutida para não depender do arquivo. */
private const val MEDAL_SVG = "data:image/svg+xml,%3Csvg%20xmlns%3D%22http%3A%2F%2Fwww.w3.org%2F2000%2Fsvg%22%20viewBox%3D%220%200%2048%2048%22%3E%20%3Cpath%20d%3D%22M14%202h9l5%2016h-9z%22%20fill%3D%22%23E5484D%22%2F%3E%20%3Cpath%20d%3D%22M17%202h3l5%2016h-3z%22%20fill%3D%22%23FF8A8E%22%2F%3E%20%3Cpath%20d%3D%22M34%202h-9l-5%2016h9z%22%20fill%3D%22%234F46E5%22%2F%3E%20%3Cpath%20d%3D%22M31%202h-3l-5%2016h3z%22%20fill%3D%22%238E86FF%22%2F%3E%20%3Ccircle%20cx%3D%2224%22%20cy%3D%2230%22%20r%3D%2215%22%20fill%3D%22%23C98A12%22%2F%3E%20%3Ccircle%20cx%3D%2224%22%20cy%3D%2229%22%20r%3D%2214%22%20fill%3D%22%23FFC94A%22%2F%3E%20%3Ccircle%20cx%3D%2224%22%20cy%3D%2229%22%20r%3D%2210.5%22%20fill%3D%22%23F5A912%22%20stroke%3D%22%23FFE59A%22%20stroke-width%3D%221.6%22%2F%3E%20%3Cpath%20d%3D%22M24%2021.5l2.3%204.7%205.2.8-3.8%203.6.9%205.1-4.6-2.4-4.6%202.4.9-5.1-3.8-3.6%205.2-.8z%22%20fill%3D%22%23FFF3C4%22%20stroke%3D%22%23B5770A%22%20stroke-width%3D%22.9%22%20stroke-linejoin%3D%22round%22%2F%3E%20%3Cpath%20d%3D%22M15%2022a11%2011%200%200%201%206-5%22%20stroke%3D%22%23FFF7DA%22%20stroke-width%3D%222%22%20stroke-linecap%3D%22round%22%20fill%3D%22none%22%20opacity%3D%22.8%22%2F%3E%20%3C%2Fsvg%3E"
