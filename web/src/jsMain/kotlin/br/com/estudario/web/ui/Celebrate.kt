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
}

private object CelebrationQueue {
    val items = mutableStateListOf<Celebration>()
}

private const val KEY_BADGES = "estudario.celebrated.badges"
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
        if (!todayDone || last == today) return@LaunchedEffect
        write(KEY_DAY, today)
        if (!ready) return@LaunchedEffect
        val letters = listOf("S", "T", "Q", "Q", "S", "S", "D")
        val week = view.streak.week.filter { !it.future }.takeLast(7).map { letters[it.date.dayOfWeek.ordinal] to it.done }
        CelebrationQueue.items.add(0, Celebration.StreakDay(view.streak.current, view.streak.best, week, view.progress.xpToday))
    }
    LaunchedEffect(level, ready) {
        val seen = runCatching { localStorage.getItem(KEY_LEVEL) }.getOrNull()?.toIntOrNull()
        write(KEY_LEVEL, level.toString())
        if (!ready || seen == null || level <= seen) return@LaunchedEffect
        CelebrationQueue.items += Celebration.LevelUp(level, view.progress.levelTitle, view.progress.totalXp)
    }

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
private fun CelebrationScreen(item: Celebration, onClose: () -> Unit) {
    DisposableEffect(item) {
        val keys: (org.w3c.dom.events.Event) -> Unit = { if ((it as KeyboardEvent).key == "Escape" || it.key == "Enter") onClose() }
        window.addEventListener("keydown", keys)
        onDispose { window.removeEventListener("keydown", keys) }
    }
    val kind = when (item) { is Celebration.StreakDay -> "streak"; is Celebration.NewBadge -> "badge"; is Celebration.LevelUp -> "level" }
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
            }
            Div({ classes("cel-actions") }) {
                if (item is Celebration.NewBadge) Button({ classes("cel-ghost"); onClick { onClose(); Router.go(Route.Achievements) } }) { Text("Ver conquistas") }
                Button({ classes("cel-cta"); onClick { onClose() } }) { Text(if (item is Celebration.StreakDay) "Bora continuar" else "Continuar") }
            }
        }
    }
}

/**
 * A cena da sequência: uma faísca acende no meio, a chama (a nossa, da turma do Folha) cresce aos
 * trancos com brasas subindo; o Folha, que estava ali, leva um susto e sai correndo; a chama se
 * assenta e o número de dias sobe contando; a semana acende dia a dia e hoje ganha o carimbo da
 * chama; por fim o Folha volta pelo canto, dando tchau.
 */
@Composable
private fun StreakContent(item: Celebration.StreakDay) {
    var shown by androidx.compose.runtime.remember { mutableStateOf(0) }
    var runnerMood by androidx.compose.runtime.remember { mutableStateOf("idle") }
    var cornerMood by androidx.compose.runtime.remember { mutableStateOf("wave") }
    LaunchedEffect(item) {
        kotlinx.coroutines.delay(900); runnerMood = "talking"           // o susto
        kotlinx.coroutines.delay(1150)
        // O número sobe contando enquanto a chama se assenta.
        val steps = item.days.coerceAtMost(30)
        for (i in 1..steps) { shown = if (i == steps) item.days else (item.days * i / steps); kotlinx.coroutines.delay((520L / steps).coerceIn(18, 120)) }
        shown = item.days
        kotlinx.coroutines.delay(1800); cornerMood = "happy"
    }
    Div({ classes("cel-stage") }) {
        Div({ classes("cel-fire-wrap") }) {
            Span({ classes("cel-spark") })
            // A chama viva (flame.js): fogo de verdade, quadro a quadro, com o rosto se mexendo. Nasce
            // uma brasinha girando e cresce enquanto o giro desacelera.
            org.jetbrains.compose.web.dom.Canvas({
                classes("cel-flame-canvas")
                ref { el ->
                    val opts: dynamic = js("({ delay: 0.3, grow: 2.3 })")
                    // Em dynamic, "?.invoke()" vira chamada ao método invoke: chamar a função direto.
                    val fn = window.asDynamic().estudarioFlame
                    val ctl: dynamic = if (fn != null && fn != undefined) fn(el, opts) else null
                    onDispose { if (ctl != null && ctl != undefined) ctl.stop() }
                }
            })
        }
        // O Folha que estava ali: olha, se assusta e sai correndo (com poeirinha).
        Div({ classes("cel-runner") }) {
            Div({ classes("cel-runner-body") }) { Folha(runnerMood, 92) }
            Span({ classes("cel-dust", "d1") }); Span({ classes("cel-dust", "d2") }); Span({ classes("cel-dust", "d3") })
        }
        B({ classes("cel-count") }) { Text(if (shown == 0) "" else "$shown") }
    }
    Span({ classes("cel-eyebrow", "cel-late") }) { Text("META DO DIA CUMPRIDA") }
    H2({ classes("cel-title", "cel-late") }) { Text(if (item.days <= 1) "Sequência acesa!" else "${item.days} dias seguidos!") }
    P({ classes("cel-body", "cel-late") }) {
        Text(
            when {
                item.days <= 1 -> "Hoje começa a sua sequência. Volte amanhã para mantê-la acesa."
                item.days >= item.best -> "É o seu recorde! Cada dia conta ponto na prova."
                else -> "Faltam ${item.best - item.days} para bater o seu recorde de ${item.best} dias."
            },
        )
    }
    // A semana acende dia a dia; hoje (o último) ganha o carimbo da chama.
    if (item.week.isNotEmpty()) Div({ classes("cel-week", "cel-late") }) {
        item.week.forEachIndexed { i, (label, done) ->
            val today = i == item.week.lastIndex
            Div({ classes(*listOfNotNull("cel-day", if (done) "on" else null, if (today) "today" else null).toTypedArray()); attr("style", "--wi:$i") }) {
                Span({ classes("cel-dot") }) { if (done) Icon("local_fire_department", filled = true) }
                Span({ classes("cel-dl") }) { Text(if (today) "hoje" else label) }
            }
        }
    }
    if (item.xpToday > 0) Span({ classes("cel-xp", "cel-late") }) { Icon("bolt", filled = true); Text("+${item.xpToday} XP hoje") }
    // O Folha volta pelo canto, dando tchau.
    Div({ classes("cel-corner") }) { Folha(cornerMood, 110) }
}

/** As interações do Folha com o emblema: uma sorteada a cada vez. */
private val badgeScenes = listOf("juggle", "polish", "peek", "cheer")

@Composable
private fun BadgeContent(badge: Badge) {
    val (light, dark) = tierColors(badge.tier)
    val scene = androidx.compose.runtime.remember(badge.id) { badgeScenes.random() }
    var mood by androidx.compose.runtime.remember(badge.id) { mutableStateOf(if (scene == "peek") "thinking" else "happy") }
    LaunchedEffect(badge.id) {
        when (scene) {
            "polish" -> { mood = "point"; kotlinx.coroutines.delay(1800); mood = "wink" }
            "peek" -> { kotlinx.coroutines.delay(1100); mood = "happy"; kotlinx.coroutines.delay(1400); mood = "wave" }
            "juggle" -> { kotlinx.coroutines.delay(2600); mood = "wink" }
            else -> { kotlinx.coroutines.delay(2200); mood = "wave" }
        }
    }
    Span({ classes("cel-eyebrow") }) { Text("NOVO EMBLEMA · ${tierName(badge.tier).uppercase()}") }
    Div({ classes("cel-badge-stage", scene) }) {
        Div({ classes("cel-medal"); attr("style", "--m1:$light;--m2:$dark") }) {
            Span({ classes("cel-ribbon", "l") }); Span({ classes("cel-ribbon", "r") })
            Div({ classes("cel-disc") }) {
                Span({ classes("cel-shine") })
                Icon("military_tech", filled = true)
            }
        }
        Div({ classes("cel-badge-folha") }) { Folha(mood, 96) }
        if (scene == "polish") Div({ classes("cel-sparkles"); attr("aria-hidden", "true") }) { repeat(6) { i -> Span({ attr("style", "--si:$i") }) } }
    }
    H2({ classes("cel-title") }) { Text(badge.name) }
    P({ classes("cel-body") }) { Text(badge.requirement) }
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
    var folhaMood by androidx.compose.runtime.remember { mutableStateOf("happy") }
    LaunchedEffect(item) { kotlinx.coroutines.delay(2400); folhaMood = "talking"; kotlinx.coroutines.delay(2600); folhaMood = "wave" }
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
