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

/** Movimentos para ver as peças em ação na vitrine. */
private val TRY_MOODS = listOf("idle" to "Parado", "present" to "Mostrar", "wave" to "Acenar", "happy" to "Comemorar", "thinking" to "Estudar", "ecstatic" to "Em chamas", "sleepy" to "Cochilar")

/** Na vitrine e nas miniaturas, mãos e itens aparecem melhor com ele mostrando (mãos para a frente). */
private fun showcaseMood(slot: FolhaSlot) = if (slot == FolhaSlot.MAO || slot == FolhaSlot.LUVAS) "present" else "idle"

// ------------------------------------------------------------------ armário

/**
 * O armário do Folha: oito categorias de peças que saem das conquistas. Escolher troca na hora (ele
 * comemora); tocar numa peça trancada mostra o Folha experimentando e diz como liberar. A vitrine tem
 * botões de movimento para ver as peças acompanhando o corpo.
 */
@Composable
fun WardrobeScreen() {
    val progress = Progress.of(Store.data).progress
    @Suppress("UNUSED_VARIABLE") val chosenNow = FolhaLook.choices
    var slot by remember { mutableStateOf(FolhaSlot.CAPA) }
    // false: peças de uma categoria; true: coleções
    var showCollections by remember { mutableStateOf(false) }
    var trying by remember { mutableStateOf<FolhaItem?>(null) }
    var cheer by remember { mutableIntStateOf(0) }
    var mood by remember { mutableStateOf("wave") }
    var testMood by remember { mutableStateOf<String?>(null) }
    LaunchedEffect(cheer) {
        if (cheer == 0) { kotlinx.coroutines.delay(1400); mood = "idle"; return@LaunchedEffect }
        mood = "happy"; kotlinx.coroutines.delay(1300); mood = "idle"
    }
    val owned = FolhaWardrobe.items.count { FolhaWardrobe.unlocked(it, progress) }
    val total = FolhaWardrobe.items.size
    val legendary = FolhaWardrobe.items.count { it.rarity == FolhaRarity.LENDARIA && FolhaWardrobe.unlocked(it, progress) }
    BackToProfile()
    PageHead("Armário do Folha", "Monte o visual do Folha. O jeito dele é livre; acessórios e efeitos saem das suas conquistas, e coleções completas liberam peças exclusivas.")
    Div({ classes("grid", "main-side", "wardrobe") }) {
        Div({ classes("stack", "loose") }) {
            Card(extra = "pad-lg") {
                // categorias, organizadas por grupo, e as coleções
                Div({ classes("wd-groups"); attr("role", "tablist") }) {
                    br.com.estudario.domain.FolhaGroup.entries.forEach { group ->
                        Div({ classes("wd-group") }) {
                            Span({ classes("wd-group-label") }) { Text(group.label) }
                            Div({ classes("wd-tabs") }) {
                                FolhaSlot.entries.filter { it.group == group }.forEach { s ->
                                    val list = FolhaWardrobe.of(s)
                                    val have = list.count { FolhaWardrobe.unlocked(it, progress) }
                                    val on = !showCollections && s == slot
                                    Button({
                                        type(ButtonType.Button)
                                        classes(*listOfNotNull("wd-tab", if (on) "on" else null).toTypedArray())
                                        attr("role", "tab"); attr("aria-selected", on.toString())
                                        onClick { slot = s; showCollections = false; trying = null }
                                    }) { Text(s.label); if (have < list.size) Span({ classes("wd-count") }) { Text("$have/${list.size}") } }
                                }
                            }
                        }
                    }
                    Div({ classes("wd-group") }) {
                        Span({ classes("wd-group-label") }) { Text("Coleções") }
                        Div({ classes("wd-tabs") }) {
                            val done = FolhaWardrobe.collections.count { c -> FolhaWardrobe.piecesOf(c).all { FolhaWardrobe.unlocked(it, progress) } }
                            Button({
                                type(ButtonType.Button)
                                classes(*listOfNotNull("wd-tab", "wd-tab-col", if (showCollections) "on" else null).toTypedArray())
                                attr("role", "tab"); attr("aria-selected", showCollections.toString())
                                onClick { showCollections = true; trying = null }
                            }) { Icon("emoji_events"); Text("Coleções"); Span({ classes("wd-count") }) { Text("$done/${FolhaWardrobe.collections.size}") } }
                        }
                    }
                }
                if (showCollections) {
                    P({ classes("small", "muted"); attr("style", "margin:14px 0 0") }) { Text("Junte as quatro peças de uma coleção para liberar a recompensa exclusiva, que não sai de outro jeito.") }
                    Div({ classes("wd-cols") }) {
                        FolhaWardrobe.collections.forEach { c -> key(c.id) { CollectionCard(c, progress, onDress = { items -> FolhaLook.chooseAll(items); trying = null; cheer++ }, onTry = { trying = it }) } }
                    }
                } else {
                P({ classes("small", "muted"); attr("style", "margin:14px 0 0") }) { Text(slot.hint) }
                Div({ classes("wd-grid") }) {
                    // exclusivas de coleção ficam no fim da grade
                    FolhaWardrobe.of(slot).sortedBy { it.collection != null }.forEach { item ->
                        key(slot.name + item.id) {
                            val open = FolhaWardrobe.unlocked(item, progress)
                            WardrobeTile(
                                item = item, open = open,
                                selected = FolhaLook.chosen(slot, progress).id == item.id,
                                trying = trying?.let { it.slot == item.slot && it.id == item.id } == true,
                                progress = progress,
                            ) {
                                if (open) {
                                    trying = null
                                    if (FolhaLook.chosen(slot, progress).id != item.id) { FolhaLook.choose(item); cheer++ }
                                } else trying = if (trying?.let { it.slot == item.slot && it.id == item.id } == true) null else item
                            }
                        }
                    }
                }
                }
            }
            Card(extra = "pad-lg") {
                Div({ classes("eyebrow") }) { Text("DETALHES QUE MUDAM SOZINHOS") }
                Ul({ classes("wd-auto") }) {
                    AutoDetail("coffee", "Manhã", "Uma caneca de café fumegando ao lado dele.")
                    AutoDetail("bedtime", "Noite", "Com o capelo, ele troca por uma touca de dormir.")
                    AutoDetail("local_fire_department", "Sequência", "Medalha no capelo com 7 dias (bronze), 30 (prata) e 100 (ouro).")
                    AutoDetail("warning", "Meta do dia em risco", "No fim da tarde, se a meta ainda não foi batida, ele fica preocupado.")
                }
            }
        }
        Div({ classes("stack", "loose") }) {
            Card(extra = "pad-lg wd-stage") {
                val t = trying
                Div({ classes("wd-folha") }) {
                    // Experimentando uma peça trancada: só nesta vitrine; o resto do site continua com a escolha salva.
                    TagElement<org.w3c.dom.HTMLElement>(
                        elementBuilder = ElementBuilder.createBuilder("estudario-folha"),
                        applyAttrs = {
                            classes("folha", "folha-live")
                            attr("mood", testMood ?: if (t != null) (if (showcaseMood(t.slot) == "present") "present" else "proud") else if (mood == "idle") showcaseMood(slot) else mood)
                            attr("size", "200")
                            attr("clickable", "")
                            attr("noscene", "")
                            if (t != null) attr(t.slot.key, t.id)
                        },
                        content = null,
                    )
                }
                Div({ classes("wd-moves"); attr("aria-label", "Testar movimentos") }) {
                    TRY_MOODS.forEach { (m, label) ->
                        Button({
                            type(ButtonType.Button)
                            classes(*listOfNotNull("chip-btn", "small", if ((testMood ?: "idle") == m && (testMood != null || m == "idle")) "on" else null).toTypedArray())
                            onClick { testMood = if (m == "idle") null else m }
                        }) { Text(label) }
                    }
                }
                if (t != null) {
                    Div({ classes("wd-try") }) {
                        Div({ classes("row"); attr("style", "gap:6px;justify-content:center") }) {
                            Chip("Experimentando", tone = "amber", icon = "lock")
                            Span({ classes("wd-rarity", rarityClass(t.rarity)) }) { Text(t.rarity.label) }
                        }
                        B { Text(t.name) }
                        P({ classes("small", "muted") }) { Text(FolhaWardrobe.requirement(t)) }
                        FolhaWardrobe.progress(t, progress)?.let { (cur, target, unit) ->
                            ProgressBar(cur.toDouble() / target)
                            Span({ classes("xs", "muted") }) { Text("$cur de $target $unit") }
                        }
                        Btn("Voltar ao meu visual", { trying = null }, style = "ghost", small = true, icon = "undo")
                    }
                } else {
                    Div({ classes("wd-summary") }) {
                        B { Text("$owned de $total peças") }
                        Span({ classes("small", "muted") }) { Text(if (legendary > 0) " · $legendary lendária${if (legendary > 1) "s" else ""}" else "") }
                    }
                    ProgressBar(owned.toDouble() / total)
                    FolhaWardrobe.next(progress)?.let { (item, pct) ->
                        P({ classes("small", "muted"); attr("style", "margin:8px 0 0;text-align:center") }) {
                            Text("Próxima peça: "); B { Text(item.name) }; Text(" (${(pct * 100).toInt()}%)")
                        }
                    }
                    Div({ classes("row", "wrap"); attr("style", "justify-content:center;gap:8px;margin-top:12px") }) {
                        Btn("Surpreenda-me", {
                            FolhaLook.chooseAll(FolhaWardrobe.random(progress).values); cheer++
                        }, style = "tonal", small = true, icon = "casino")
                        Btn("Visual clássico", {
                            FolhaLook.chooseAll(FolhaSlot.entries.map { FolhaWardrobe.default(it) }); cheer++
                        }, style = "ghost", small = true, icon = "restart_alt")
                    }
                }
            }
        }
    }
}

/**
 * Uma coleção: as quatro peças (as que faltam aparecem trancadas, e tocar nelas faz o Folha
 * experimentar), a recompensa exclusiva e o andamento. Completa, veste o conjunto inteiro num toque.
 */
@Composable
private fun CollectionCard(c: br.com.estudario.domain.FolhaCollection, progress: ProgressEngine.ProgressSummary, onDress: (List<FolhaItem>) -> Unit, onTry: (FolhaItem) -> Unit) {
    val pieces = FolhaWardrobe.piecesOf(c)
    val reward = FolhaWardrobe.rewardOf(c) ?: return
    val have = pieces.count { FolhaWardrobe.unlocked(it, progress) }
    val complete = have == pieces.size
    Div({ classes(*listOfNotNull("wd-col", if (complete) "done" else null).toTypedArray()) }) {
        Div({ classes("wd-col-head") }) {
            Div { B { Text(c.name) }; P({ classes("xs", "muted") }) { Text(c.description) } }
            Span({ classes("wd-col-count", if (complete) "done" else "todo") }) { if (complete) Icon("check_circle", filled = true); Text("$have/${pieces.size}") }
        }
        Div({ classes("wd-col-body") }) {
            Div({ classes("wd-col-pieces") }) {
                pieces.forEach { item ->
                    val open = FolhaWardrobe.unlocked(item, progress)
                    Button({
                        type(ButtonType.Button)
                        classes(*listOfNotNull("wd-col-piece", if (!open) "locked" else null).toTypedArray())
                        attr("title", if (open) item.name else "${item.name} · ${FolhaWardrobe.requirement(item)}")
                        onClick { onTry(item) }
                    }) {
                        PieceThumb(item, 64)
                        if (!open) Span({ classes("wd-col-lock") }) { Icon("lock", filled = true) }
                        Span({ classes("wd-col-slot") }) { Text(item.slot.label) }
                        Span({ classes("wd-col-name") }) { Text(item.name) }
                    }
                }
            }
            Div({ classes("wd-col-arrow"); attr("aria-hidden", "true") }) { Text("→") }
            Button({
                type(ButtonType.Button)
                classes(*listOfNotNull("wd-col-reward", rarityClass(reward.rarity), if (!complete) "locked" else null).toTypedArray())
                attr("title", "${reward.name} · recompensa da coleção")
                onClick { onTry(reward) }
            }) {
                Span({ classes("wd-rarity", rarityClass(reward.rarity)) }) { Text(reward.rarity.label) }
                PieceThumb(reward, 92)
                B({ classes("wd-col-name") }) { Text(reward.name) }
                Span({ classes("xs", "muted") }) { Text("Recompensa exclusiva") }
            }
        }
        Div({ classes("wd-col-foot") }) {
            ProgressBar(have.toDouble() / pieces.size)
            if (complete) Btn("Vestir conjunto", { onDress(pieces + reward) }, style = "tonal", small = true, icon = "checkroom")
            else Span({ classes("xs", "muted") }) { Text("Falta: " + pieces.filterNot { FolhaWardrobe.unlocked(it, progress) }.joinToString(", ") { "${it.name} (${it.slot.label.lowercase()})" }) }
        }
    }
}

/** O Folha vestindo uma peça (o resto do visual como está), em imagem parada. */
@Composable
private fun PieceThumb(item: FolhaItem, size: Int) {
    val v = FolhaLook.version
    val src = remember(item.slot, item.id, size, v) {
        runCatching { window.asDynamic().folhaPng(kotlin.js.json("mood" to showcaseMood(item.slot), "size" to size, item.slot.key to item.id)) as? String }.getOrNull() ?: "icon.png"
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

@Composable
private fun WardrobeTile(item: FolhaItem, open: Boolean, selected: Boolean, trying: Boolean, progress: ProgressEngine.ProgressSummary, onClick: () -> Unit) {
    val v = FolhaLook.version
    val src = remember(item.slot, item.id, v) {
        runCatching { window.asDynamic().folhaPng(kotlin.js.json("mood" to showcaseMood(item.slot), "size" to 92, item.slot.key to item.id)) as? String }.getOrNull() ?: "icon.png"
    }
    Button({
        type(ButtonType.Button)
        classes(*listOfNotNull("wd-tile", rarityClass(item.rarity), if (selected) "on" else null, if (!open) "locked" else null, if (trying) "trying" else null).toTypedArray())
        attr("aria-pressed", selected.toString())
        attr("title", if (open) item.name else "${item.name} · ${FolhaWardrobe.requirement(item)}")
        onClick { onClick() }
    }) {
        Span({ classes("wd-rarity", rarityClass(item.rarity)) }) { Text(item.rarity.label) }
        Img(src = src, alt = "", attrs = { classes("wd-thumb"); attr("aria-hidden", "true") })
        Span({ classes("wd-name") }) { Text(item.name) }
        when {
            selected -> Span({ classes("wd-state", "on") }) { Icon("check_circle", filled = true); Text("Usando") }
            open -> Span({ classes("wd-state") }) { Text("Vestir") }
            else -> Span({ classes("wd-state", "lock") }) {
                Icon("lock", filled = true)
                Text(item.collection?.let { "Coleção " + (FolhaWardrobe.collection(it)?.name ?: "") } ?: item.level?.let { "Nível $it" } ?: BadgeCatalog.all.firstOrNull { it.id == item.badgeId }?.name ?: "Emblema")
            }
        }
        if (!open) FolhaWardrobe.progress(item, progress)?.let { (cur, target, _) -> ProgressBar(cur.toDouble() / target) }
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
