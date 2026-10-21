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
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.TagElement
import org.jetbrains.compose.web.dom.Text

/**
 * O visual do Folha neste navegador: o que a pessoa escolheu no armário e o contexto do dia (sequência,
 * risco de perder a sequência). Tudo vai para o desenho por window.folhaSetLook; [version] muda junto
 * para os Folhas parados (PNG) se redesenharem.
 */
object FolhaLook {
    /** Muda quando o desenho recebe um visual novo (os Folhas parados se redesenham). */
    var version by mutableIntStateOf(0)
        private set
    /** Muda quando a pessoa escolhe um item (o armário e a sincronização releem a escolha). */
    var choices by mutableIntStateOf(0)
        private set

    private fun read(slot: FolhaSlot): String? = runCatching { localStorage.getItem("estudario.folha.${slot.name.lowercase()}") }.getOrNull()

    fun chosen(slot: FolhaSlot, summary: ProgressEngine.ProgressSummary): FolhaItem = FolhaWardrobe.resolve(slot, read(slot), summary)

    fun choose(item: FolhaItem) {
        runCatching { localStorage.setItem("estudario.folha.${item.slot.name.lowercase()}", item.id) }
        choices++
    }

    /** Manda para o desenho. A sequência corre risco do fim da tarde em diante, se hoje ainda não fechou. */
    fun push(view: ProgressView) {
        val hour = kotlin.js.Date().getHours()
        val streak = view.streak
        val look = kotlin.js.json(
            "cover" to chosen(FolhaSlot.CAPA, view.progress).id,
            "ribbon" to chosen(FolhaSlot.FITA, view.progress).id,
            "acc" to chosen(FolhaSlot.ACESSORIO, view.progress).id,
            "streak" to streak.current,
            "atRisk" to (streak.current >= 2 && !streak.todayDone && hour >= 18),
        )
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

// ------------------------------------------------------------------ armário

/**
 * O armário do Folha: capas, fitas e acessórios que saem das conquistas. Escolher troca na hora (ele
 * comemora); tocar num item trancado mostra o Folha experimentando e diz como ganhar.
 */
@Composable
fun WardrobeScreen() {
    val progress = Progress.of(Store.data).progress
    @Suppress("UNUSED_VARIABLE") val chosenNow = FolhaLook.choices
    var trying by remember { mutableStateOf<FolhaItem?>(null) }
    var cheer by remember { mutableIntStateOf(0) }
    var mood by remember { mutableStateOf("wave") }
    LaunchedEffect(cheer) {
        if (cheer == 0) { kotlinx.coroutines.delay(1400); mood = "idle"; return@LaunchedEffect }
        mood = "happy"; kotlinx.coroutines.delay(1300); mood = "idle"
    }
    val unlocked = FolhaWardrobe.items.count { FolhaWardrobe.unlocked(it, progress) }
    BackToProfile()
    PageHead("Armário do Folha", "Capas, fitas e acessórios que você ganha estudando. $unlocked de ${FolhaWardrobe.items.size} liberados.")
    Div({ classes("grid", "main-side", "wardrobe") }) {
        Div({ classes("stack", "loose") }) {
            FolhaSlot.entries.forEach { slot ->
                Card(extra = "pad-lg") {
                    Div({ classes("eyebrow") }) { Text(slot.label.uppercase()) }
                    Div({ classes("wd-grid") }) {
                        FolhaWardrobe.of(slot).forEach { item ->
                            key(item.id) {
                                WardrobeTile(
                                    item = item,
                                    open = FolhaWardrobe.unlocked(item, progress),
                                    selected = FolhaLook.chosen(slot, progress).id == item.id,
                                    trying = trying?.id == item.id,
                                    progress = progress,
                                ) {
                                    if (FolhaWardrobe.unlocked(item, progress)) {
                                        trying = null
                                        if (FolhaLook.chosen(slot, progress).id != item.id) { FolhaLook.choose(item); cheer++ }
                                    } else trying = if (trying?.id == item.id) null else item
                                }
                            }
                        }
                    }
                }
            }
        }
        Div({ classes("stack", "loose") }) {
            Card(extra = "pad-lg wd-stage") {
                val t = trying
                Div({ classes("wd-folha") }) {
                    // Experimentando um item trancado: só nesta vitrine, o resto do site continua com a escolha salva.
                    TagElement<org.w3c.dom.HTMLElement>(
                        elementBuilder = ElementBuilder.createBuilder("estudario-folha"),
                        applyAttrs = {
                            classes("folha", "folha-live")
                            attr("mood", if (t != null) "proud" else mood)
                            attr("size", "190")
                            attr("clickable", "")
                            if (t != null) attr(when (t.slot) { FolhaSlot.CAPA -> "cover"; FolhaSlot.FITA -> "ribbon"; FolhaSlot.ACESSORIO -> "acc" }, t.id)
                        },
                        content = null,
                    )
                }
                if (t != null) {
                    Div({ classes("wd-try") }) {
                        Chip("Experimentando", tone = "amber", icon = "lock")
                        B { Text(t.name) }
                        P({ classes("small", "muted") }) { Text(FolhaWardrobe.requirement(t)) }
                        badgeProgress(t, progress)?.let { (cur, target, unit) ->
                            ProgressBar(cur.toDouble() / target)
                            Span({ classes("xs", "muted") }) { Text("$cur / $target $unit") }
                        }
                        Btn("Voltar ao meu Folha", { trying = null }, style = "ghost", small = true, icon = "undo")
                    }
                } else {
                    P({ classes("small", "muted"); attr("style", "text-align:center;margin:6px 0 0") }) {
                        Text("Seu Folha aparece assim no site inteiro. De manhã ele toma café, à noite põe a touca, e com 7 dias seguidos ganha medalha no capelo.")
                    }
                }
            }
        }
    }
}

private fun badgeProgress(item: FolhaItem, progress: ProgressEngine.ProgressSummary): Triple<Int, Int, String>? {
    item.level?.let { return Triple(progress.level.coerceAtMost(it), it, "níveis") }
    val id = item.badgeId ?: return null
    val p = progress.badges.firstOrNull { it.badge.id == id } ?: return null
    return Triple(p.current.coerceAtMost(p.target), p.target, p.badge.unit)
}

@Composable
private fun WardrobeTile(item: FolhaItem, open: Boolean, selected: Boolean, trying: Boolean, progress: ProgressEngine.ProgressSummary, onClick: () -> Unit) {
    val v = FolhaLook.version
    val key = when (item.slot) { FolhaSlot.CAPA -> "cover"; FolhaSlot.FITA -> "ribbon"; FolhaSlot.ACESSORIO -> "acc" }
    val src = remember(item.id, v) {
        runCatching { window.asDynamic().folhaPng(kotlin.js.json("mood" to "idle", "size" to 84, key to item.id)) as? String }.getOrNull() ?: "icon.png"
    }
    Button({
        type(ButtonType.Button)
        classes(*listOfNotNull("wd-tile", if (selected) "on" else null, if (!open) "locked" else null, if (trying) "trying" else null).toTypedArray())
        attr("aria-pressed", selected.toString())
        attr("title", if (open) item.name else "${item.name} · ${FolhaWardrobe.requirement(item)}")
        onClick { onClick() }
    }) {
        Img(src = src, alt = "", attrs = { classes("wd-thumb"); attr("aria-hidden", "true") })
        Span({ classes("wd-name") }) { Text(item.name) }
        when {
            selected -> Span({ classes("wd-state", "on") }) { Icon("check_circle", filled = true); Text("Usando") }
            open -> Span({ classes("wd-state") }) { Text("Usar") }
            else -> Span({ classes("wd-state", "lock") }) {
                Icon("lock", filled = true)
                Text(item.level?.let { "Nível $it" } ?: BadgeCatalog.all.firstOrNull { it.id == item.badgeId }?.name ?: "Emblema")
            }
        }
        if (!open) badgeProgress(item, progress)?.let { (cur, target, _) -> ProgressBar(cur.toDouble() / target) }
    }
}

/** Cartão do Perfil: o Folha atual e o atalho para o armário. */
@Composable
fun WardrobeCard(progress: ProgressEngine.ProgressSummary) {
    val unlocked = FolhaWardrobe.items.count { FolhaWardrobe.unlocked(it, progress) }
    val next = FolhaWardrobe.items.filterNot { FolhaWardrobe.unlocked(it, progress) }
        .mapNotNull { item -> badgeProgress(item, progress)?.let { item to it.first.toDouble() / it.second } }
        .maxByOrNull { it.second }
    org.jetbrains.compose.web.dom.A(href = "#/${Route.Wardrobe.path}", { classes("card", "pad-lg", "wd-card") }) {
        Div({ classes("wd-card-folha") }) { Folha("wave", 64, live = false) }
        Div({ classes("grow") }) {
            Div({ classes("eyebrow") }) { Text("ARMÁRIO DO FOLHA") }
            B { Text("$unlocked de ${FolhaWardrobe.items.size} itens liberados") }
            if (next != null) P({ classes("xs", "muted"); attr("style", "margin:2px 0 0") }) { Text("Próximo: ${next.first.name} (${(next.second * 100).toInt()}%)") }
        }
        Icon("chevron_right", extraClass = "faint")
    }
}

/** Para a comemoração de emblema: o que esse emblema libera no armário. */
fun wardrobeUnlockedBy(badgeId: String): List<FolhaItem> = FolhaWardrobe.items.filter { it.badgeId == badgeId }
