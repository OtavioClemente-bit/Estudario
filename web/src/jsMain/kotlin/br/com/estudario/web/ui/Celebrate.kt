package br.com.estudario.web.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import br.com.estudario.domain.Badge
import br.com.estudario.web.Route
import br.com.estudario.web.Router
import br.com.estudario.web.data.LoadState
import br.com.estudario.web.data.Progress
import br.com.estudario.web.data.Queries
import br.com.estudario.web.data.Store
import kotlinx.browser.localStorage
import kotlinx.browser.window
import org.jetbrains.compose.web.dom.A
import org.jetbrains.compose.web.dom.B
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H2
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text
import org.w3c.dom.events.KeyboardEvent

/**
 * As comemorações do site, como as telas do app (StreakCelebrationScreen, BadgeCelebrationScreen e a
 * subida de nível): sequência do dia, emblema novo e nível novo, em tela cheia com a cara do
 * Estudário. Só comemora o que acontece com a pessoa estudando aqui: o que já existia ao abrir (ou
 * chega do celular) é registrado em silêncio. Fica guardado neste navegador.
 */
private sealed interface Celebration {
    data class StreakDay(val days: Int, val best: Int, val week: List<Pair<String, Boolean>>, val xpToday: Int) : Celebration
    data class NewBadge(val badge: Badge) : Celebration
    data class LevelUp(val level: Int, val title: String, val totalXp: Int) : Celebration
    /** Peça nova no armário do Folha (vem de um emblema ou de um nível): o baú abre e ele aparece vestindo. */
    data class NewPiece(val item: br.com.estudario.domain.FolhaItem) : Celebration
}

private object CelebrationQueue {
    val items = mutableStateListOf<Celebration>()
}

private const val KEY_BADGES = "estudario.celebrated.badges"
private const val KEY_PIECES = "estudario.celebrated.pieces"
private const val KEY_DAY = "estudario.celebrated.day"
private const val KEY_LEVEL = "estudario.celebrated.level"

private fun readSet(key: String): Set<String>? = runCatching { localStorage.getItem(key) }.getOrNull()?.split(",")?.filter { it.isNotBlank() }?.toSet()
private fun write(key: String, value: String) { runCatching { localStorage.setItem(key, value) } }

/** Observa o progresso e enfileira as comemorações. Montado no App, com os dados prontos. */
@Composable
fun CelebrationHost() {
    if (Store.load != LoadState.Ready) return
    // Na demonstração, "?celebrar" mostra as três telas (para conferir o visual); fora isso, nada.
    if (Store.demo) {
        LaunchedEffect(Unit) {
            if ("celebrar" !in window.location.search) return@LaunchedEffect
            val sample = br.com.estudario.domain.BadgeCatalog.all.firstOrNull { it.tier == 3 } ?: return@LaunchedEffect
            CelebrationQueue.items += Celebration.StreakDay(5, 7, listOf("S" to true, "T" to true, "Q" to false, "Q" to true, "S" to true, "S" to true, "D" to true), 86)
            CelebrationQueue.items += Celebration.NewBadge(sample)
            CelebrationQueue.items += Celebration.LevelUp(4, "Estudioso", 820)
            br.com.estudario.domain.FolhaWardrobe.items.firstOrNull { it.id == "asas" }?.let { CelebrationQueue.items += Celebration.NewPiece(it) }
        }
        CelebrationQueue.items.firstOrNull()?.let { CelebrationScreen(it) { CelebrationQueue.items.removeAt(0) } }
        return
    }
    val view = Progress.of(Store.data)
    val earned = view.progress.earnedBadges.map { it.badge.id }.toSet()
    val todayDone = view.streak.todayDone
    val level = view.progress.level
    // Ao abrir, os dados chegam e o progresso se recalcula: nos primeiros segundos tudo é leitura.
    var ready by androidx.compose.runtime.remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { kotlinx.coroutines.delay(4000); ready = true }

    LaunchedEffect(earned, ready) {
        val known = readSet(KEY_BADGES)
        if (known == null || !ready) { if (known == null || earned.containsAll(known)) write(KEY_BADGES, (earned + known.orEmpty()).sorted().joinToString(",")); return@LaunchedEffect }
        val fresh = earned - known
        if (fresh.isEmpty()) return@LaunchedEffect
        write(KEY_BADGES, (known + fresh).sorted().joinToString(","))
        view.progress.earnedBadges.filter { it.badge.id in fresh }.sortedBy { it.badge.tier }.forEach { CelebrationQueue.items += Celebration.NewBadge(it.badge) }
    }
    LaunchedEffect(todayDone, ready) {
        val today = Queries.todayEpoch().toString()
        val last = runCatching { localStorage.getItem(KEY_DAY) }.getOrNull()
        // Só marca o dia como comemorado quando a comemoração de fato entra na fila (antes, abrir a
        // página com o dia já cumprido gastava o dia sem mostrar nada, e depois só vinha o emblema).
        if (!ready || !todayDone || last == today) return@LaunchedEffect
        write(KEY_DAY, today)
        // Só comemora o dia que acabou de ser cumprido aqui: se a primeira atividade de hoje foi há
        // mais tempo (no app, chegada pela sincronização), ele já foi comemorado lá.
        val start = Queries.startOfToday()
        val first = listOfNotNull(
            Store.data.attempts.filter { it.answeredAt >= start }.minOfOrNull { it.answeredAt },
            Store.data.reviewHistory.filter { it.reviewedAt >= start }.minOfOrNull { it.reviewedAt },
            Store.data.executions.filter { it.completedAt >= start }.minOfOrNull { it.completedAt },
        ).minOrNull()
        if (first != null && kotlin.js.Date.now() - first > 15 * 60_000.0) return@LaunchedEffect
        val letters = listOf("S", "T", "Q", "Q", "S", "S", "D")
        val week = view.streak.week.filter { !it.future }.takeLast(7).map { letters[it.date.dayOfWeek.ordinal] to it.done }
        // A sequência vem antes dos emblemas; se algo já está na tela, ela entra logo depois dele.
        val at = if (CelebrationQueue.items.isEmpty()) 0 else 1
        CelebrationQueue.items.add(at, Celebration.StreakDay(view.streak.current, view.streak.best, week, view.progress.xpToday))
    }
    val owned = br.com.estudario.domain.FolhaWardrobe.items.filter { br.com.estudario.domain.FolhaWardrobe.unlocked(it, view.progress) && (it.badgeId != null || it.level != null || it.collection != null) }
        .map { it.slot.name + ":" + it.id }.toSet()
    LaunchedEffect(owned, ready) {
        val known = readSet(KEY_PIECES)
        // primeira vez neste navegador (ou ainda carregando): o que já estava liberado entra sem festa
        if (known == null || !ready) { if (known == null) write(KEY_PIECES, owned.sorted().joinToString(",")); return@LaunchedEffect }
        val fresh = owned - known
        if (fresh.isEmpty()) return@LaunchedEffect
        write(KEY_PIECES, (known + fresh).sorted().joinToString(","))
        br.com.estudario.domain.FolhaWardrobe.items.filter { (it.slot.name + ":" + it.id) in fresh }
            .sortedByDescending { it.rarity.ordinal }.forEach { CelebrationQueue.items += Celebration.NewPiece(it) }
    }
    LaunchedEffect(level, ready) {
        val seen = runCatching { localStorage.getItem(KEY_LEVEL) }.getOrNull()?.toIntOrNull()
        write(KEY_LEVEL, level.toString())
        if (!ready || seen == null || level <= seen) return@LaunchedEffect
        CelebrationQueue.items += Celebration.LevelUp(level, view.progress.levelTitle, view.progress.totalXp)
    }

    // A comemoração não atropela a geração de material (espera terminar e mais uns segundos) e não roda
    // com a aba escondida: o navegador congela a animação e ela apareceria parada no primeiro quadro.
    var visible by androidx.compose.runtime.remember { mutableStateOf(kotlinx.browser.document.asDynamic().visibilityState == "visible") }
    androidx.compose.runtime.DisposableEffect(Unit) {
        val onChange: (org.w3c.dom.events.Event) -> Unit = { visible = kotlinx.browser.document.asDynamic().visibilityState == "visible" }
        kotlinx.browser.document.addEventListener("visibilitychange", onChange)
        onDispose { kotlinx.browser.document.removeEventListener("visibilitychange", onChange) }
    }
    var calm by androidx.compose.runtime.remember { mutableStateOf(true) }
    val generating = Generation.active.isNotEmpty()
    LaunchedEffect(generating, Generation.finishedAt) {
        if (generating) { calm = false; return@LaunchedEffect }
        val wait = 15_000 - (kotlin.js.Date.now() - Generation.finishedAt)
        if (wait > 0) { calm = false; kotlinx.coroutines.delay(wait.toLong()) }
        calm = true
    }
    if (!visible || !calm) return
    val current = CelebrationQueue.items.firstOrNull() ?: return
    CelebrationScreen(current) { CelebrationQueue.items.removeAt(0) }
}


private fun tierName(tier: Int) = when (tier) { 1 -> "Bronze"; 2 -> "Prata"; 3 -> "Ouro"; 4 -> "Safira"; 5 -> "Rubi"; else -> "Esmeralda" }

/** Quando a cena chega no momento principal: aí entram os textos (e, logo depois, os botões). */
private fun revealAt(item: Celebration): Double = when (item) {
    is Celebration.LevelUp -> 2.5
    is Celebration.NewBadge -> 2.0
    is Celebration.StreakDay -> 3.0
    is Celebration.NewPiece -> 3.6
}

/**
 * A tela de comemoração: em cima, a cena desenhada (folha-live.js, <estudario-cena>) com o Folha no
 * visual escolhido; embaixo, os textos e os botões, que entram quando a cena chega no momento principal.
 */
@Composable
private fun CelebrationScreen(item: Celebration, onCloseNow: () -> Unit) {
    var leaving by androidx.compose.runtime.remember(item) { mutableStateOf(false) }
    val onClose: () -> Unit = {
        if (!leaving) {
            leaving = true
            window.setTimeout({ onCloseNow() }, 260)
        }
    }
    DisposableEffect(item) {
        val keys: (org.w3c.dom.events.Event) -> Unit = { if ((it as KeyboardEvent).key == "Escape" || it.key == "Enter") onClose() }
        window.addEventListener("keydown", keys)
        onDispose { window.removeEventListener("keydown", keys) }
    }
    val kind = when (item) { is Celebration.StreakDay -> "streak"; is Celebration.NewBadge -> "badge"; is Celebration.LevelUp -> "level"; is Celebration.NewPiece -> "piece" }
    val r = revealAt(item)
    Div({ classes(*listOfNotNull("celebrate", "cn", kind, if (leaving) "out" else null).toTypedArray()); attr("role", "dialog"); attr("aria-modal", "true") }) {
        Div({ classes("cel-in") }) {
            Scene(item)
            Div({ classes("cn-text") }) {
                when (item) {
                    is Celebration.StreakDay -> StreakText(item, r)
                    is Celebration.NewBadge -> BadgeText(item.badge, r)
                    is Celebration.LevelUp -> LevelText(item, r)
                    is Celebration.NewPiece -> PieceText(item.item, r)
                }
            }
            Div({ classes("cel-actions", "lv-late"); attr("style", "--ld:${r + 0.45}s") }) {
                if (item is Celebration.NewBadge) Button({ classes("cel-ghost"); onClick { onCloseNow(); Router.go(Route.Achievements) } }) { Text("Ver conquistas") }
                if (item is Celebration.NewPiece) {
                    Button({ classes("cel-ghost"); onClick { onCloseNow(); Router.go(Route.Wardrobe) } }) { Text("Abrir o armário") }
                    Button({ classes("cel-cta"); onClick { FolhaLook.choose(item.item); Toast.show("O Folha está usando: ${item.item.name}"); onClose() } }) { Text("Usar agora") }
                } else Button({ classes("cel-cta"); onClick { onClose() } }) { Text(if (item is Celebration.StreakDay) "Bora continuar" else "Continuar") }
            }
        }
    }
}

/** A cena de cada comemoração, com os dados dela. */
@Composable
private fun Scene(item: Celebration) {
    val attrs: List<Pair<String, String>> = when (item) {
        is Celebration.LevelUp -> listOf("tipo" to "nivel", "nivel" to item.level.toString())
        is Celebration.NewBadge -> listOf("tipo" to "emblema", "tier" to item.badge.tier.toString(), "categoria" to item.badge.id.substringBefore("-"), "faixa" to tierName(item.badge.tier))
        is Celebration.StreakDay -> listOf("tipo" to "sequencia", "dias" to item.days.toString(), "semana" to item.week.joinToString(",") { (l, done) -> l + if (done) "1" else "0" })
        is Celebration.NewPiece -> listOf("tipo" to "peca", "piece-slot" to item.item.slot.key, "piece" to item.item.id, "rarity" to item.item.rarity.name.lowercase())
    }
    val label = when (item) {
        is Celebration.LevelUp -> "O Folha comemora o nível ${item.level}"
        is Celebration.NewBadge -> "Medalha do emblema ${item.badge.name}"
        is Celebration.StreakDay -> "${item.days} dias de sequência"
        is Celebration.NewPiece -> "O Folha ganha ${item.item.name}"
    }
    Div({ classes("cn-stage") }) {
        org.jetbrains.compose.web.dom.TagElement<org.w3c.dom.HTMLElement>(
            elementBuilder = org.jetbrains.compose.web.dom.ElementBuilder.createBuilder("estudario-cena"),
            // bleed: a luz da cena se espalha além da caixa e some suave no fundo da tela
            applyAttrs = { attrs.forEach { (k, v) -> attr(k, v) }; attr("bleed", "34"); attr("aria-label", label) },
            content = null,
        )
    }
}

@Composable
private fun StreakText(item: Celebration.StreakDay, r: Double) {
    Span({ classes("cel-eyebrow", "lv-late"); attr("style", "--ld:${r}s") }) { Text(if (item.days == 1) "1 DIA DE SEQUÊNCIA" else "${item.days} DIAS DE SEQUÊNCIA") }
    H2({ classes("cel-title", "lv-late"); attr("style", "--ld:${r + 0.1}s") }) {
        Text(
            when {
                item.days <= 1 -> "A chama acendeu"
                item.days >= item.best -> "Recorde! Ninguém te para."
                else -> "Mais um dia aceso"
            },
        )
    }
    P({ classes("cel-body", "lv-late"); attr("style", "--ld:${r + 0.2}s") }) {
        Text(
            when {
                item.days <= 1 -> "Volte amanhã para manter o fogo aceso."
                item.days >= item.best -> "Sua maior sequência até agora. Amanhã tem mais."
                else -> "Faltam ${item.best - item.days} para o seu recorde de ${item.best} dias."
            },
        )
    }
    if (item.xpToday > 0) Span({ classes("cel-xp", "lv-late"); attr("style", "--ld:${r + 0.3}s") }) { Icon("bolt", filled = true); Text("+${item.xpToday} XP hoje") }
}

@Composable
private fun BadgeText(badge: Badge, r: Double) {
    Span({ classes("cel-eyebrow", "lv-late"); attr("style", "--ld:${r}s") }) { Text("NOVO EMBLEMA · ${tierName(badge.tier).uppercase()}") }
    H2({ classes("cel-title", "lv-late"); attr("style", "--ld:${r + 0.1}s") }) { Text(badge.name) }
    P({ classes("cel-body", "lv-late"); attr("style", "--ld:${r + 0.2}s") }) { Text(badge.requirement) }
}

@Composable
private fun LevelText(item: Celebration.LevelUp, r: Double) {
    val old = (item.level - 1).coerceAtLeast(1)
    val newTitle = br.com.estudario.domain.ProgressEngine.levelTitle(old) != item.title
    Span({ classes("cel-eyebrow", "lv-late"); attr("style", "--ld:${r}s") }) { Text("SUBIU DE NÍVEL") }
    H2({ classes("cel-title", "lv-late"); attr("style", "--ld:${r + 0.1}s") }) { Text("Nível ${item.level} · ${item.title}") }
    if (newTitle) Span({ classes("lv-badge", "lv-late"); attr("style", "--ld:${r + 0.25}s") }) { Icon("military_tech", filled = true); Text("Novo título!") }
    Div({ classes("lv-xp", "lv-late"); attr("style", "--ld:${r + 0.2}s") }) {
        Span({ classes("lv-xp-fill") })
        B { Text("${item.totalXp} XP") }
    }
}

@Composable
private fun PieceText(item: br.com.estudario.domain.FolhaItem, r: Double) {
    val rarity = item.rarity.name.lowercase()
    Span({ classes("cel-eyebrow", "lv-late", "pc-rar", "r-$rarity"); attr("style", "--ld:${r}s") }) { Text("NOVA PEÇA · ${item.rarity.label.uppercase()}") }
    H2({ classes("cel-title", "lv-late"); attr("style", "--ld:${r + 0.1}s") }) { Text(item.name) }
    P({ classes("cel-body", "lv-late"); attr("style", "--ld:${r + 0.2}s") }) { Text("${item.slot.label} · ${br.com.estudario.domain.FolhaWardrobe.requirement(item)}") }
}
