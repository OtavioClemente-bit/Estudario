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
    /**
     * Peças novas no armário do Folha (de um emblema, de um nível ou de uma coleção completa): a caixa abre,
     * todas as cartas saem juntas e ele aparece vestindo. Peças que chegam juntas vêm numa caixa só.
     */
    data class NewPiece(val items: List<br.com.estudario.domain.FolhaItem>) : Celebration
}

/**
 * Uma rodada de estudo em andamento (bateria de questões, revisão, flashcards, simulado) ou a leitura
 * da teoria com o foco contando: as comemorações (sequência, emblema, nível, peça nova) esperam o fim
 * dela, para não interromper o estudo no meio. Quem está numa rodada chama [HoldCelebrations].
 * O foco sozinho não segura: antes, com o foco ligado e esquecido, nenhuma comemoração aparecia.
 */
object StudyFlow {
    var holds by mutableStateOf(0)
    val busy: Boolean get() = holds > 0 || (TopicFocus.running && Router.current is Route.Topic)
}

/** Segura as comemorações enquanto este pedaço da tela estiver aberto. */
@Composable
fun HoldCelebrations() {
    DisposableEffect(Unit) { StudyFlow.holds++; onDispose { StudyFlow.holds-- } }
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
            CelebrationQueue.items += Celebration.LevelUp(10, "Concurseiro", 4_720)
            br.com.estudario.domain.FolhaWardrobe.collection("militar")?.let { c ->
                CelebrationQueue.items += Celebration.NewPiece(br.com.estudario.domain.FolhaWardrobe.piecesOf(c).takeLast(2) + listOfNotNull(br.com.estudario.domain.FolhaWardrobe.rewardOf(c)))
            }
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
        // Comemorado em qualquer aparelho da conta não se repete aqui (emblema ganho no celular).
        val known = readSet(KEY_BADGES)?.plus(br.com.estudario.web.data.Prefs.syncedSet("celebratedBadges"))
        if (known == null || !ready) { if (known == null || earned.containsAll(known)) write(KEY_BADGES, (earned + known.orEmpty()).sorted().joinToString(",")); return@LaunchedEffect }
        val fresh = earned - known
        if (fresh.isEmpty()) return@LaunchedEffect
        write(KEY_BADGES, (known + fresh).sorted().joinToString(","))
        br.com.estudario.web.data.Prefs.addSynced("celebratedBadges", known + fresh)
        view.progress.earnedBadges.filter { it.badge.id in fresh }.sortedBy { it.badge.tier }.forEach { CelebrationQueue.items += Celebration.NewBadge(it.badge) }
    }
    LaunchedEffect(todayDone, ready) {
        val today = Queries.todayEpoch().toString()
        val last = runCatching { localStorage.getItem(KEY_DAY) }.getOrNull()
        // Só marca o dia como comemorado quando a comemoração de fato entra na fila (antes, abrir a
        // página com o dia já cumprido gastava o dia sem mostrar nada, e depois só vinha o emblema).
        if (!ready || !todayDone || last == today) return@LaunchedEffect
        write(KEY_DAY, today)
        // Só comemora o dia que acabou de ser cumprido aqui: se a última atividade de hoje (a que fechou
        // o dia) foi há mais tempo, ela chegou do celular pela sincronização e foi comemorada lá. Antes a
        // conta usava a primeira atividade: quem estudava mais de 15 minutos aqui nunca via a do dia.
        val start = Queries.startOfToday()
        val lastActivity = listOfNotNull(
            Store.data.attempts.filter { it.answeredAt >= start }.maxOfOrNull { it.answeredAt },
            Store.data.reviewHistory.filter { it.reviewedAt >= start }.maxOfOrNull { it.reviewedAt },
            Store.data.executions.filter { it.completedAt >= start }.maxOfOrNull { it.completedAt },
        ).maxOrNull()
        if (lastActivity != null && kotlin.js.Date.now() - lastActivity > 15 * 60_000.0) return@LaunchedEffect
        val letters = listOf("S", "T", "Q", "Q", "S", "S", "D")
        val week = view.streak.week.filter { !it.future }.takeLast(7).map { letters[it.date.dayOfWeek.ordinal] to it.done }
        // A sequência vem antes dos emblemas; se algo já está na tela, ela entra logo depois dele.
        val at = if (CelebrationQueue.items.isEmpty()) 0 else 1
        CelebrationQueue.items.add(at, Celebration.StreakDay(view.streak.current, view.streak.best, week, view.progress.xpToday))
    }
    val owned = br.com.estudario.domain.FolhaWardrobe.items.filter { br.com.estudario.domain.FolhaWardrobe.unlocked(it, view.progress) && (it.badgeId != null || it.level != null || it.collection != null) }
        .map { it.slot.name + ":" + it.id }.toSet()
    LaunchedEffect(owned, ready) {
        val known = readSet(KEY_PIECES)?.plus(br.com.estudario.web.data.Prefs.syncedSet("celebratedPieces"))
        // primeira vez neste navegador (ou ainda carregando): o que já estava liberado entra sem festa
        if (known == null || !ready) { if (known == null) write(KEY_PIECES, owned.sorted().joinToString(",")); return@LaunchedEffect }
        val fresh = owned - known
        if (fresh.isEmpty()) return@LaunchedEffect
        write(KEY_PIECES, (known + fresh).sorted().joinToString(","))
        br.com.estudario.web.data.Prefs.addSynced("celebratedPieces", known + fresh)
        val pieces = br.com.estudario.domain.FolhaWardrobe.items.filter { (it.slot.name + ":" + it.id) in fresh }.sortedByDescending { it.rarity.ordinal }
        if (pieces.isNotEmpty()) CelebrationQueue.items += Celebration.NewPiece(pieces)
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
    // no meio de uma rodada de estudo, espera ela acabar (na tela do resultado elas aparecem)
    if (!visible || !calm || StudyFlow.busy) return
    val current = CelebrationQueue.items.firstOrNull() ?: return
    CelebrationScreen(current) { CelebrationQueue.items.removeAt(0) }
}


internal fun tierName(tier: Int) = when (tier) { 1 -> "Bronze"; 2 -> "Prata"; 3 -> "Ouro"; 4 -> "Safira"; 5 -> "Rubi"; else -> "Esmeralda" }

/** Quando a cena chega no momento principal: aí entram os textos (e, logo depois, os botões). */
private fun revealAt(item: Celebration): Double = when (item) {
    is Celebration.LevelUp -> 2.5
    is Celebration.NewBadge -> 4.35
    is Celebration.StreakDay -> 3.0
    is Celebration.NewPiece -> br.com.estudario.domain.FolhaWardrobe.giftFlashAt(item.items.size) + 0.2
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
    // Subiu de nível: a cena de jogo (LevelUpGame), com o brasão da patente.
    if (item is Celebration.LevelUp) {
        LevelUpGame(item.level, item.title, item.totalXp, leaving, onClose, onProfile = { onCloseNow(); Router.go(Route.Profile) })
        return
    }
    // Sequência do dia e emblema novo: as mesmas cenas de jogo (o emblema usa a arte de emblemas/g).
    if (item is Celebration.StreakDay) {
        StreakGame(item.days, item.best, item.week, item.xpToday, leaving, onClose)
        return
    }
    if (item is Celebration.NewBadge) {
        BadgeGame(item.badge, leaving, onClose, onTrophies = { onCloseNow(); Router.go(Route.Achievements) })
        return
    }
    val kind = when (item) { is Celebration.StreakDay -> "streak"; is Celebration.NewBadge -> "badge"; is Celebration.LevelUp -> "level"; is Celebration.NewPiece -> "piece" }
    val r = revealAt(item)
    Div({ classes(*listOfNotNull("celebrate", "cn", kind, if (leaving) "out" else null).toTypedArray()); attr("role", "dialog"); attr("aria-modal", "true") }) {
        Div({ classes("cel-in") }) {
            Scene(item)
            Div({ classes("cn-text") }) {
                when (item) {
                    is Celebration.StreakDay -> Unit
                    is Celebration.NewBadge -> Unit
                    is Celebration.LevelUp -> Unit
                    is Celebration.NewPiece -> PieceText(item.items, r)
                }
            }
            Div({ classes("cel-actions", "lv-late"); attr("style", "--ld:${r + 0.45}s") }) {
                if (item is Celebration.NewBadge) Button({ classes("cel-ghost"); onClick { onCloseNow(); Router.go(Route.Achievements) } }) { Text("Ver conquistas") }
                if (item is Celebration.NewPiece) {
                    Button({ classes("cel-ghost"); onClick { onCloseNow(); Router.go(Route.Wardrobe) } }) { Text("Abrir o armário") }
                    Button({ classes("cel-cta"); onClick {
                        item.items.forEach { FolhaLook.choose(it) }
                        Toast.show(if (item.items.size == 1) "O Folha está usando: ${item.items[0].name}" else "O Folha vestiu as peças novas")
                        onClose()
                    } }) { Text(if (item.items.size == 1) "Usar agora" else "Vestir tudo") }
                    Button({ classes("cel-ghost"); onClick { onClose() } }) { Text("Fechar") }
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
        is Celebration.NewBadge -> listOf("tipo" to "emblema", "tier" to item.badge.tier.toString(), "categoria" to item.badge.id.substringBefore("-"), "faixa" to tierName(item.badge.tier), "arte" to "emblemas/g/${item.badge.id}.webp")
        is Celebration.StreakDay -> listOf("tipo" to "sequencia", "dias" to item.days.toString(), "semana" to item.week.joinToString(",") { (l, done) -> l + if (done) "1" else "0" })
        is Celebration.NewPiece -> listOf("tipo" to "peca", "pieces" to piecesJson(item.items), "rarity" to item.items.maxOf { it.rarity }.name.lowercase())
    }
    val label = when (item) {
        is Celebration.LevelUp -> "O Folha comemora o nível ${item.level}"
        is Celebration.NewBadge -> "Medalha do emblema ${item.badge.name}"
        is Celebration.StreakDay -> "${item.days} dias de sequência"
        is Celebration.NewPiece -> if (item.items.size == 1) "O Folha ganha ${item.items[0].name}" else "O Folha ganha ${item.items.size} peças novas"
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

/** As peças para a cena do presente (atributo pieces de <estudario-cena>), em JSON. */
private fun piecesJson(items: List<br.com.estudario.domain.FolhaItem>): String = items.joinToString(",", "[", "]") { item ->
    fun q(v: String) = "\"" + v.replace("\\", "\\\\").replace("\"", "\\\"") + "\""
    "{\"slot\":${q(item.slot.key)},\"piece\":${q(item.id)},\"rarity\":${q(item.rarity.name.lowercase())},\"name\":${q(item.name)}}"
}

/** "A", "A e B", "A, B e C". */
internal fun joinNames(names: List<String>): String = if (names.size <= 1) names.firstOrNull().orEmpty() else names.dropLast(1).joinToString(", ") + " e " + names.last()

@Composable
private fun PieceText(items: List<br.com.estudario.domain.FolhaItem>, r: Double) {
    val best = items.maxOf { it.rarity }
    val rarity = best.name.lowercase()
    val one = items.size == 1
    Span({ classes("cel-eyebrow", "lv-late", "pc-rar", "r-$rarity"); attr("style", "--ld:${r}s") }) {
        Text(if (one) "NOVA PEÇA · ${best.label.uppercase()}" else "${items.size} PEÇAS NOVAS · ATÉ ${best.label.uppercase()}")
    }
    H2({ classes("cel-title", "lv-late"); attr("style", "--ld:${r + 0.1}s") }) { Text(if (one) items[0].name else joinNames(items.map { it.name })) }
    val reqs = items.map { br.com.estudario.domain.FolhaWardrobe.requirement(it) }.distinct()
    P({ classes("cel-body", "lv-late"); attr("style", "--ld:${r + 0.2}s") }) {
        Text(if (one) "${items[0].slot.label} · ${reqs[0]}" else if (reqs.size == 1) reqs[0] else joinNames(items.map { it.slot.label }.distinct()))
    }
}
