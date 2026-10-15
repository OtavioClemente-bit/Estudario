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
    Div({ classes("celebrate"); attr("role", "dialog"); attr("aria-modal", "true") }) {
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

@Composable
private fun StreakContent(item: Celebration.StreakDay) {
    Div({ classes("cel-flame") }) {
        Span({ classes("cel-flame-ic") }) { Icon("local_fire_department", filled = true) }
        B({ classes("cel-flame-n") }) { Text("${item.days}") }
    }
    Span({ classes("cel-eyebrow") }) { Text("META DO DIA CUMPRIDA") }
    H2({ classes("cel-title") }) { Text(if (item.days <= 1) "Sequência acesa!" else "${item.days} dias seguidos!") }
    P({ classes("cel-body") }) {
        Text(
            when {
                item.days <= 1 -> "Hoje começa a sua sequência. Volte amanhã para mantê-la acesa."
                item.days >= item.best -> "É o seu recorde! Cada dia conta ponto na prova."
                else -> "Faltam ${item.best - item.days} para bater o seu recorde de ${item.best} dias."
            },
        )
    }
    // A semana em bolinhas, acesas nos dias feitos.
    if (item.week.isNotEmpty()) Div({ classes("cel-week") }) {
        item.week.forEach { (label, done) ->
            Div({ classes(*listOfNotNull("cel-day", if (done) "on" else null).toTypedArray()) }) {
                Span({ classes("cel-dot") }) { if (done) Icon("local_fire_department", filled = true) }
                Span({ classes("cel-dl") }) { Text(label) }
            }
        }
    }
    if (item.xpToday > 0) Span({ classes("cel-xp") }) { Icon("bolt", filled = true); Text("+${item.xpToday} XP hoje") }
    Folha("happy", 84)
}

@Composable
private fun BadgeContent(badge: Badge) {
    val (light, dark) = tierColors(badge.tier)
    Span({ classes("cel-eyebrow") }) { Text("NOVO EMBLEMA · ${tierName(badge.tier).uppercase()}") }
    Div({ classes("cel-medal"); attr("style", "--m1:$light;--m2:$dark") }) {
        Span({ classes("cel-ribbon", "l") }); Span({ classes("cel-ribbon", "r") })
        Div({ classes("cel-disc") }) {
            Span({ classes("cel-shine") })
            Icon("military_tech", filled = true)
        }
    }
    H2({ classes("cel-title") }) { Text(badge.name) }
    P({ classes("cel-body") }) { Text(badge.requirement) }
    Folha("wave", 76)
}

@Composable
private fun LevelContent(item: Celebration.LevelUp) {
    Span({ classes("cel-eyebrow") }) { Text("SUBIU DE NÍVEL") }
    Div({ classes("cel-level") }) {
        Span({ classes("cel-level-ring") })
        B { Text("${item.level}") }
    }
    H2({ classes("cel-title") }) { Text(item.title) }
    P({ classes("cel-body") }) { Text("Você chegou ao nível ${item.level} com ${item.totalXp} XP. O estudo de cada dia está virando resultado.") }
    Folha("happy", 76)
}
