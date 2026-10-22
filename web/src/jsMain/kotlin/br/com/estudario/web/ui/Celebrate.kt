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
            br.com.estudario.domain.FolhaWardrobe.items.firstOrNull { it.id == "coroa" }?.let { CelebrationQueue.items += Celebration.NewPiece(it) }
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

/** As cores do emblema pelo nível dele: bronze, prata, ouro, safira, rubi, esmeralda. */
private fun tierColors(tier: Int): Pair<String, String> = when (tier) {
    1 -> "#E0A36B" to "#9C5B2E"
    2 -> "#E4E8F0" to "#8A93A6"
    3 -> "#FFD66B" to "#C98A0B"
    4 -> "#7FB4FF" to "#2D5FD0"
    5 -> "#FF8FA3" to "#C0263D"
    else -> "#7EE0B8" to "#13895F"
}

private fun tierName(tier: Int) = when (tier) { 1 -> "Bronze"; 2 -> "Prata"; 3 -> "Ouro"; 4 -> "Safira"; 5 -> "Rubi"; else -> "Esmeralda" }

@Composable
private fun CelebrationScreen(item: Celebration, onCloseNow: () -> Unit) {
    var leaving by androidx.compose.runtime.remember(item) { mutableStateOf(false) }
    val onClose: () -> Unit = {
        if (!leaving) {
            leaving = true
            val root = kotlinx.browser.document.querySelector(".celebrate")
            runCatching { window.asDynamic().folhaLeave(root, { onCloseNow() }) }.onFailure { onCloseNow() }
        }
    }
    DisposableEffect(item) {
        val keys: (org.w3c.dom.events.Event) -> Unit = { if ((it as KeyboardEvent).key == "Escape" || it.key == "Enter") onClose() }
        window.addEventListener("keydown", keys)
        onDispose { window.removeEventListener("keydown", keys) }
    }
    val kind = when (item) { is Celebration.StreakDay -> "streak"; is Celebration.NewBadge -> "badge"; is Celebration.LevelUp -> "level"; is Celebration.NewPiece -> "piece" }
    Div({ classes("celebrate", kind); attr("role", "dialog"); attr("aria-modal", "true") }) {
        Div({ classes("cel-glow") })
        // Confete de caderno: tirinhas de papel nas cores da marca.
        Div({ classes("confetti"); attr("aria-hidden", "true") }) {
            val colors = listOf("#FFB421", "#7EE0B8", "#ABA2FF", "#FF8FA3", "#FFFFFF", "#38B2FF")
            (0 until 42).forEach { i ->
                Span({ attr("style", "--x:${(i * 37) % 100}%;--d:${(i % 7) * 0.18}s;--t:${2.6 + (i % 5) * 0.35}s;--r:${(i * 53) % 360}deg;background:${colors[i % colors.size]}") })
            }
        }
        Div({ classes("cel-in") }) {
            when (item) {
                is Celebration.StreakDay -> StreakContent(item)
                is Celebration.NewBadge -> BadgeContent(item.badge)
                is Celebration.LevelUp -> LevelContent(item)
                is Celebration.NewPiece -> PieceContent(item.item)
            }
            Div({ classes("cel-actions") }) {
                if (item is Celebration.NewBadge) Button({ classes("cel-ghost"); onClick { onCloseNow(); Router.go(Route.Achievements) } }) { Text("Ver conquistas") }
                if (item is Celebration.NewPiece) {
                    Button({ classes("cel-ghost"); onClick { onCloseNow(); Router.go(Route.Wardrobe) } }) { Text("Abrir o armário") }
                    Button({ classes("cel-cta"); onClick { FolhaLook.choose(item.item); Toast.show("O Folha está usando: ${item.item.name}"); onClose() } }) { Text("Usar agora") }
                } else Button({ classes("cel-cta"); onClick { onClose() } }) { Text(if (item is Celebration.StreakDay) "Bora continuar" else "Continuar") }
            }
        }
    }
}

/**
 * A sequência do dia: a chama acende. Uma faísca estala na base e o fogo pega de baixo para cima
 * (estica, passa do ponto, assenta), e daí fica viva: o corpo tremula num ritmo, o rosto balança em
 * outro, com halo de calor e brasas subindo. O número rola como um contador (ontem → hoje), os dias
 * feitos da semana aparecem acesos e hoje pega fogo junto com a virada do número. O Folha aparece no
 * canto com um balão curto. Sem confete: as brasas fazem esse papel.
 */
@Composable
private fun StreakContent(item: Celebration.StreakDay) {
    val previous = (item.days - 1).coerceAtLeast(0)
    Div({ classes("sk-stage") }) {
        Span({ classes("sk-glow") })
        Span({ classes("sk-spark") })
        // O halo de calor: a mesma chama, desfocada, atrás.
        Div({ classes("sk-flame", "sk-haze"); attr("aria-hidden", "true") }) { Icon("local_fire_department", filled = true) }
        Div({ classes("sk-flame") }) { Icon("local_fire_department", filled = true) }
        Div({ classes("sk-embers"); attr("aria-hidden", "true") }) {
            (0 until 12).forEach { i ->
                Span({ attr("style", "--ex:${(i * 23) % 70 - 35}px;--ed:${1.1 + (i % 6) * 0.35}s;--et:${2.2 + (i % 4) * 0.5}s;--es:${3 + i % 3}px") })
            }
        }
    }
    // O contador: ontem embaixo, hoje entra rolando.
    Div({ classes("sk-odo"); attr("aria-label", "${item.days} dias") }) {
        Div({ classes("sk-odo-col") }) {
            B { Text("$previous") }
            B { Text("${item.days}") }
        }
    }
    Span({ classes("sk-label", "sk-late"); attr("style", "--sd:1.7s") }) { Text(if (item.days == 1) "dia de sequência" else "dias de sequência") }
    H2({ classes("cel-title", "sk-late"); attr("style", "--sd:1.85s") }) {
        Text(
            when {
                item.days <= 1 -> "A chama acendeu"
                item.days >= item.best -> "Recorde! Ninguém te para."
                else -> "Mais um dia aceso"
            },
        )
    }
    P({ classes("cel-body", "sk-late"); attr("style", "--sd:2s") }) {
        Text(
            when {
                item.days <= 1 -> "Volte amanhã para manter o fogo aceso."
                item.days >= item.best -> "Sua maior sequência até agora. Amanhã tem mais."
                else -> "Faltam ${item.best - item.days} para o seu recorde de ${item.best} dias."
            },
        )
    }
    if (item.week.isNotEmpty()) Div({ classes("sk-week", "sk-late"); attr("style", "--sd:.25s") }) {
        item.week.forEachIndexed { i, (label, done) ->
            val today = i == item.week.lastIndex
            Div({ classes(*listOfNotNull("sk-day", if (done && !today) "on" else null, if (today) "today" else null).toTypedArray()); attr("style", "--wi:$i") }) {
                Span({ classes("sk-dot") }) { if (done) Icon("local_fire_department", filled = true) }
                Span({ classes("sk-dl") }) { Text(if (today) "hoje" else label) }
            }
        }
    }
    if (item.xpToday > 0) Span({ classes("cel-xp", "sk-late"); attr("style", "--sd:2.2s") }) { Icon("bolt", filled = true); Text("+${item.xpToday} XP hoje") }
    Div({ classes("sk-folha") }) {
        Div({ classes("sk-folha-body") }) { Folha("proud", 104) }
        Div({ classes("lv-bubble", "sk-bubble") }) { Text(if (item.days <= 1) "Acendeu! Amanhã a gente mantém." else "${item.days} dias! Tá pegando fogo!") }
    }
}

/**
 * Emblema novo, no nível da subida de nível: a medalha cai carimbada (com impacto e onda de choque),
 * raios de luz giram atrás, um brilho de vidro passa por ela e estrelinhas piscam em volta; o nome
 * aparece e o Folha entra pelo canto com o balão de parabéns.
 */
@Composable
private fun BadgeContent(badge: Badge) {
    val (light, dark) = tierColors(badge.tier)
    var mood by androidx.compose.runtime.remember(badge.id) { mutableStateOf("surprised") }
    LaunchedEffect(badge.id) { kotlinx.coroutines.delay(900); mood = "proud"; kotlinx.coroutines.delay(1300); mood = "talking"; kotlinx.coroutines.delay(2400); mood = "wave" }
    Div({ classes("bd-stage"); attr("style", "--m1:$light;--m2:$dark") }) {
        Div({ classes("lv-rays", "bd-rays") })
        Div({ classes("lv-shock", "bd-shock") })
        Div({ classes("bd-medal") }) {
            Div({ classes("cel-medal") }) {
                Span({ classes("cel-ribbon", "l") }); Span({ classes("cel-ribbon", "r") })
                Div({ classes("cel-disc") }) {
                    Span({ classes("bd-glass") })
                    Icon("military_tech", filled = true)
                }
            }
        }
        Div({ classes("bd-stars"); attr("aria-hidden", "true") }) {
            listOf(-120 to -70, 118 to -50, -100 to 70, 110 to 84, 0 to -118).forEachIndexed { i, (x, y) ->
                Span({ attr("style", "--x:${x}px;--y:${y}px;--i:$i") })
            }
        }
    }
    Span({ classes("cel-eyebrow", "lv-late"); attr("style", "--ld:.95s") }) { Text("NOVO EMBLEMA · ${tierName(badge.tier).uppercase()}") }
    H2({ classes("cel-title", "lv-late"); attr("style", "--ld:1.05s") }) { Text(badge.name) }
    P({ classes("cel-body", "lv-late"); attr("style", "--ld:1.2s") }) { Text(badge.requirement) }
    Div({ classes("lv-folha", "bd-folha") }) {
        Div({ classes("lv-folha-body") }) { Folha(mood, 120) }
        Div({ classes("lv-bubble", "bd-bubble") }) { Text("Emblema novo! ${badge.name}. Você mereceu, continua assim!") }
    }
}

/**
 * A subida de nível, para impressionar: o nível antigo aparece no anel, um corte de lâmina atravessa
 * a tela com um clarão e o número se parte em dois (as metades caem girando, a tela treme); o nível
 * novo cai do alto com impacto, onda de choque, raios girando e estouro de partículas; a barra de XP
 * enche, o título aparece (com selo quando é título novo) e o Folha salta de baixo dando parabéns.
 */
@Composable
private fun LevelContent(item: Celebration.LevelUp) {
    val old = (item.level - 1).coerceAtLeast(1)
    val newTitle = br.com.estudario.domain.ProgressEngine.levelTitle(old) != item.title
    var folhaMood by androidx.compose.runtime.remember { mutableStateOf("surprised") }
    LaunchedEffect(item) { folhaMood = "surprised"; kotlinx.coroutines.delay(1100); folhaMood = "happy"; kotlinx.coroutines.delay(1300); folhaMood = "talking"; kotlinx.coroutines.delay(2600); folhaMood = "wave" }
    Div({ classes("lv-stage") }) {
        Div({ classes("lv-rays") })
        Div({ classes("lv-shock") })
        Div({ classes("lv-flash") })
        Div({ classes("lv-slash") })
        // O nível antigo, que vai ser cortado ao meio.
        Div({ classes("lv-old") }) {
            Div({ classes("lv-half", "a") }) { B { Text("$old") } }
            Div({ classes("lv-half", "b") }) { B { Text("$old") } }
        }
        // O novo, caindo do alto.
        Div({ classes("lv-new") }) {
            Span({ classes("lv-ring") })
            B { Text("${item.level}") }
        }
        Div({ classes("lv-burst"); attr("aria-hidden", "true") }) {
            (0 until 16).forEach { i -> Span({ attr("style", "--a:${i * 22.5}deg;--d:${70 + (i % 4) * 22}px;--c:${listOf("#FFB421", "#7EE0B8", "#ABA2FF", "#FF8FA3")[i % 4]}") }) }
        }
    }
    Span({ classes("cel-eyebrow", "lv-late"); attr("style", "--ld:1.7s") }) { Text("SUBIU DE NÍVEL") }
    H2({ classes("cel-title", "lv-late"); attr("style", "--ld:1.85s") }) { Text("Nível ${item.level} · ${item.title}") }
    if (newTitle) Span({ classes("lv-badge", "lv-late"); attr("style", "--ld:2.1s") }) { Icon("military_tech", filled = true); Text("Novo título!") }
    Div({ classes("lv-xp", "lv-late"); attr("style", "--ld:2s") }) {
        Span({ classes("lv-xp-fill") })
        B { Text("${item.totalXp} XP") }
    }
    // O Folha salta de baixo dando parabéns.
    Div({ classes("lv-folha") }) {
        Div({ classes("lv-folha-body") }) { Folha(folhaMood, 120) }
        Div({ classes("lv-bubble") }) { Text("Parabéns! Você chegou ao nível ${item.level}. Esse esforço tá aparecendo!") }
    }
}

/**
 * Peça nova no armário: o baú cai, treme, abre com luz e confete e o Folha sobe vestindo a peça
 * (folha-live.js, <estudario-bau>). O nome, a raridade e de onde veio aparecem depois da abertura.
 */
@Composable
private fun PieceContent(item: br.com.estudario.domain.FolhaItem) {
    val rarity = item.rarity.name.lowercase()
    Div({ classes("pc-stage") }) {
        org.jetbrains.compose.web.dom.TagElement<org.w3c.dom.HTMLElement>(
            elementBuilder = org.jetbrains.compose.web.dom.ElementBuilder.createBuilder("estudario-bau"),
            applyAttrs = { attr("size", "300"); attr("piece-slot", item.slot.key); attr("piece", item.id); attr("rarity", rarity) },
            content = null,
        )
    }
    Span({ classes("cel-eyebrow", "lv-late", "pc-rar", "r-$rarity"); attr("style", "--ld:2.2s") }) { Text("NOVA PEÇA · ${item.rarity.label.uppercase()}") }
    H2({ classes("cel-title", "lv-late"); attr("style", "--ld:2.35s") }) { Text(item.name) }
    P({ classes("cel-body", "lv-late"); attr("style", "--ld:2.5s") }) { Text("${item.slot.label} · ${br.com.estudario.domain.FolhaWardrobe.requirement(item)}") }
}
