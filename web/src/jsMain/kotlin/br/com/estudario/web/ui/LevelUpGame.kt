package br.com.estudario.web.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import br.com.estudario.domain.Badge
import br.com.estudario.domain.FolhaWardrobe
import br.com.estudario.domain.LevelCrest
import br.com.estudario.domain.ProgressEngine
import br.com.estudario.web.data.Progress
import br.com.estudario.web.data.Store
import kotlinx.browser.window
import org.jetbrains.compose.web.dom.B
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H2
import org.jetbrains.compose.web.dom.I
import org.jetbrains.compose.web.dom.Img
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text
import kotlin.random.Random

/** O brasão do nível (LevelCrest, o mesmo desenho do app): a patente do título, as estrelas e o número. */
@Composable
internal fun LevelCrestView(level: Int, size: Int, extraClass: String? = null) {
    val id = remember { "lc" + Random.nextInt(1, 999_999) }
    key(level) {
        Div({
            classes(*listOfNotNull("lc-crest", extraClass).toTypedArray())
            attr("style", "width:${size}px;height:${size}px")
            attr("role", "img")
            attr("aria-label", "Nível $level, ${LevelCrest.rank(level).title}")
            ref { el -> el.innerHTML = LevelCrest.svg(level, "$id-$level", small = size <= 48); onDispose { } }
        })
    }
}

// ------------------------------------------------------------------ peças das cenas de jogo (lvx-*)
// Linha do tempo comum (no CSS): 0,1s o "antes" entra · 0,75s treme · 1,05s estoura (clarão, faíscas,
// ondas, tremida, som) e o "depois" cai · 1,35s letreiro · 1,75s linha de baixo · 1,9s barra ou dias ·
// 2,2s prêmios · 2,7s Folha · 2,9s botões.

/** Um prêmio da cena: ícone, rótulo em caixa alta e texto; [gold] destaca o principal. */
private data class Loot(val icon: String, val kicker: String, val text: String, val gold: Boolean = false)

/** A tela da cena: cores do tema dela (claro, médio, escuro), fundo com raios e pontinhos e o som no impacto. */
@Composable
private fun GameShell(colors: Triple<String, String, String>, sound: String, label: String, leaving: Boolean, extra: String? = null, content: @Composable () -> Unit) {
    val motes = remember { List(22) { listOf(Random.nextInt(0, 100), Random.nextInt(4, 12), Random.nextInt(0, 6000), Random.nextInt(2, 5)) } }
    // o som no momento do impacto (só toca com o som ligado em Ajustes)
    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(1050)
        runCatching { window.asDynamic().estudarioSom?.play(sound) }
    }
    Div({
        classes(*listOfNotNull("lvx", extra, if (leaving) "out" else null).toTypedArray())
        attr("role", "dialog"); attr("aria-modal", "true"); attr("aria-label", label)
        attr("style", "--c1:${colors.first};--c2:${colors.second};--c3:${colors.third}")
    }) {
        Div({ classes("lvx-bg") }) {
            Div({ classes("lvx-rays") })
            Div({ classes("lvx-grid") })
            motes.forEach { (x, s, d, t) -> Span({ classes("lvx-mote"); attr("style", "--x:$x%;--s:${s}px;--d:-${d}ms;--t:${t}s") }) }
        }
        Div({ classes("lvx-main") }) { content() }
    }
}

/** O palco: o "antes" treme e estoura num clarão, o "depois" cai com ondas e faíscas; [stamp] bate no canto. */
@Composable
private fun GameStage(stamp: String?, before: @Composable () -> Unit, after: @Composable () -> Unit) {
    val sparks = remember { List(30) { Triple(Random.nextInt(130, 250), Random.nextInt(4, 10), Random.nextInt(0, 140)) } }
    Div({ classes("lvx-stage") }) {
        Div({ classes("lvx-halo") })
        repeat(3) { i -> Div({ classes("lvx-ring"); attr("style", "--i:$i") }) }
        Div({ classes("lvx-burst") }) {
            sparks.forEachIndexed { i, (r, s, dl) -> Span({ attr("style", "--a:${i * 360 / sparks.size}deg;--r:${r}px;--s:${s}px;--dl:${dl}ms") }) }
        }
        Div({ classes("lvx-old") }) { before() }
        Div({ classes("lvx-new") }) { after() }
        Div({ classes("lvx-flash") })
        if (stamp != null) Span({ classes("lvx-stamp") }) { Text(stamp) }
    }
}

/** Selo de cima ("ANTES ▶ DEPOIS"), o letreiro letra por letra e a linha de baixo. */
@Composable
private fun GameHead(from: String, to: String, word: String, sub: @Composable () -> Unit) {
    Div({ classes("lvx-head") }) {
        Span({ classes("lvx-kicker") }) { Text(from); Span({ classes("lvx-arrow") }) { Text("▶") }; B { Text(to) } }
        H2({ classes("lvx-title"); attr("aria-label", word) }) {
            word.forEachIndexed { i, ch -> Span({ attr("style", "--i:$i"); if (ch == ' ') classes("sp") }) { Text(if (ch == ' ') " " else ch.toString()) } }
        }
        P({ classes("lvx-rank") }) { sub() }
    }
}

@Composable
private fun GameLootList(loot: List<Loot>) {
    Div({ classes("lvx-loot") }) {
        loot.forEachIndexed { i, item ->
            Div({ classes("lvx-card", if (item.gold) "gold" else "plain"); attr("style", "--i:$i") }) {
                Span({ classes("lvx-card-ic") }) { Icon(item.icon) }
                Div({ classes("lvx-card-txt") }) { Span { Text(item.kicker) }; B { Text(item.text) } }
            }
        }
    }
}

@Composable
private fun GameSay(line: String) {
    Div({ classes("lvx-say") }) {
        Folha("proud", 64)
        Span({ classes("lvx-bubble") }) { Text(line) }
    }
}

@Composable
private fun GameActions(primary: String, onPrimary: () -> Unit, ghost: String? = null, onGhost: () -> Unit = {}) {
    Div({ classes("lvx-actions") }) {
        Button({ classes("lvx-go"); attr("type", "button"); onClick { onPrimary() } }) { Text(primary) }
        if (ghost != null) Button({ classes("lvx-ghost"); attr("type", "button"); onClick { onGhost() } }) { Text(ghost) }
    }
}

private fun rankColors(r: LevelCrest.Rank) = Triple(r.light, r.mid, r.dark)

// ------------------------------------------------------------------ subiu de nível

/**
 * Subiu de nível, com cara de jogo: o brasão antigo treme e estoura, o novo cai, o letreiro entra letra
 * por letra e, em seguida, a barra do nível novo, os prêmios (título novo, peças liberadas no armário,
 * próxima patente) e o Folha comemorando.
 */
@Composable
internal fun LevelUpGame(level: Int, title: String, totalXp: Int, leaving: Boolean, onClose: () -> Unit, onProfile: () -> Unit) {
    val old = (level - 1).coerceAtLeast(1)
    val rank = LevelCrest.rank(level)
    val promo = LevelCrest.isPromotion(level)
    val floor = ProgressEngine.xpForLevel(level)
    val need = (ProgressEngine.xpForLevel(level + 1) - floor).coerceAtLeast(1)
    val into = (totalXp - floor).coerceIn(0, need)
    val pct = into * 100 / need
    val unlocked = FolhaWardrobe.items.filter { it.level == level }
    val nextTitle = ProgressEngine.nextTitle(level)
    GameShell(rankColors(rank), "cena_nivel", "Subiu para o nível $level, $title", leaving, if (promo) "promo" else null) {
        GameStage(if (promo) "PROMOVIDO!" else null, before = { LevelCrestView(old, 220) }, after = { LevelCrestView(level, 220) })
        GameHead("NÍVEL $old", "NÍVEL $level", "SUBIU DE NÍVEL!") {
            if (promo) { Text("Nova patente: "); B { Text(title) }; Text(" · ${rank.metal}") }
            else { B { Text(title) }; Text(" · patente ${rank.metal} · ${LevelCrest.stars(level)}★") }
        }
        Div({ classes("lvx-xp") }) {
            Div({ classes("lvx-xp-top") }) {
                B { Text("Nível $level") }
                Span { Text("faltam ${need - into} XP para o nível ${level + 1}") }
            }
            Div({ classes("lvx-xp-bar") }) { I({ classes("lvx-xp-fill"); attr("style", "--lvp:${pct.coerceAtLeast(2)}%") }) }
            Span({ classes("lvx-xp-total") }) { Text("$totalXp XP no total") }
        }
        GameLootList(buildList {
            if (promo) add(Loot("military_tech", "NOVO TÍTULO", "$title · patente ${rank.metal}", gold = true))
            unlocked.take(2).forEach { add(Loot("checkroom", "LIBEROU NO ARMÁRIO", "${it.name} · ${it.rarity.label}")) }
            if (nextTitle != null) {
                val (at, name) = nextTitle
                add(Loot("flag", "PRÓXIMA PATENTE", "$name no nível $at · ${if (at - level == 1) "falta 1 nível" else "faltam ${at - level} níveis"}"))
            } else add(Loot("emoji_events", "PATENTE MÁXIMA", "Você é ${ProgressEngine.levelTitle(level)}. Agora é colecionar estrelas."))
        })
        GameSay(when {
            promo -> "Promoção! Agora você é $title. Isso é estudo de verdade aparecendo."
            level % 3 == 0 -> "Nível $level no bolso. Bora pro ${level + 1}?"
            level % 3 == 1 -> "Mais um degrau rumo à vaga. Cada XP é questão feita e revisão cumprida."
            else -> "Subiu! Quem estuda todo dia chega lá. Você está chegando."
        })
        GameActions("Continuar", onClose, "Ver meu perfil", onProfile)
    }
}

// ------------------------------------------------------------------ sequência do dia

/** Os marcos da sequência (dias seguidos) que a cena mostra como próximo objetivo. */
private val STREAK_MARKS = listOf(3, 7, 14, 21, 30, 45, 60, 90, 120, 180, 270, 365)

/**
 * Dia cumprido, com cara de jogo: a chama apagada (com a conta de ontem) treme e estoura, a acesa cai
 * com o número de hoje, os dias da semana acendem um por um e entram os prêmios (XP de hoje, próximo
 * marco, recorde). Recorde novo ganha o carimbo.
 */
@Composable
internal fun StreakGame(days: Int, best: Int, week: List<Pair<String, Boolean>>, xpToday: Int, leaving: Boolean, onClose: () -> Unit) {
    val record = days > 1 && days >= best
    val next = STREAK_MARKS.firstOrNull { it > days }
    GameShell(Triple("#FFE08A", "#FF8A3D", "#B23A0E"), "cena_sequencia", "$days dias de sequência", leaving, "streak") {
        GameStage(
            if (record) "RECORDE!" else null,
            before = { Div({ classes("lvx-flame", "off") }) { ProfileArt("flame", size = 190, lit = false); B { Text("${days - 1}") } } },
            after = { Div({ classes("lvx-flame") }) { ProfileArt("flame", size = 190, lit = true); B { Text("$days") } } },
        )
        GameHead(if (days <= 1) "COMEÇOU" else "DIA ${days - 1}", if (days == 1) "DIA 1" else "DIA $days", "SEQUÊNCIA!") {
            B { Text(if (days == 1) "1 dia" else "$days dias") }
            Text(if (days == 1) " · a chama acendeu" else " seguidos estudando")
        }
        Div({ classes("lvx-week") }) {
            week.forEachIndexed { i, (letter, done) ->
                val today = i == week.lastIndex
                Div({ classes(*listOfNotNull("lvx-day", if (done) "on" else null, if (today) "today" else null).toTypedArray()); attr("style", "--i:$i") }) {
                    Span { Text(letter) }
                    I { Text(if (done) "✓" else "·") }
                }
            }
        }
        GameLootList(buildList {
            if (xpToday > 0) add(Loot("bolt", "XP DE HOJE", "+$xpToday XP no dia", gold = true))
            if (next != null) add(Loot("flag", "PRÓXIMO MARCO", "$next dias · ${if (next - days == 1) "falta 1 dia" else "faltam ${next - days} dias"}"))
            add(if (record) Loot("emoji_events", "NOVO RECORDE", "Sua maior sequência: $days dias") else Loot("emoji_events", "SEU RECORDE", "$best dias · faltam ${best - days} para bater"))
        })
        GameSay(when {
            days <= 1 -> "A chama acendeu! Volte amanhã para ela não apagar."
            record -> "Recorde! Ninguém te para. Amanhã tem mais."
            else -> "Mais um dia aceso. Faltam ${best - days} para o seu recorde de $best."
        })
        GameActions("Bora continuar", onClose)
    }
}

// ------------------------------------------------------------------ emblema novo

/** As cores de cada faixa de emblema, nos mesmos metais das patentes. */
private fun tierColors(tier: Int): Triple<String, String, String> {
    val metal = when (tier) { 1 -> "Bronze"; 2 -> "Prata"; 3 -> "Ouro"; 4 -> "Safira"; 5 -> "Rubi"; else -> "Esmeralda" }
    return rankColors(LevelCrest.ranks.first { it.metal == metal })
}

/**
 * Emblema novo, com cara de jogo: a silhueta escura do emblema (a arte de emblemas/g) treme e estoura,
 * o emblema colorido cai com a faixa dele carimbada, e entram o nome, o que a pessoa fez, a sala de
 * troféus e o próximo emblema mais perto de sair.
 */
@Composable
internal fun BadgeGame(badge: Badge, leaving: Boolean, onClose: () -> Unit, onTrophies: () -> Unit) {
    val tierLabel = tierName(badge.tier)
    val art = "emblemas/g/${badge.id}.webp"
    val progress = remember { Progress.of(Store.data).progress }
    val earned = progress.earnedBadges.size
    val next = progress.nextBadges.firstOrNull { it.badge.id != badge.id }
    GameShell(tierColors(badge.tier), "cena_emblema", "Emblema novo: ${badge.name}", leaving, "badge") {
        GameStage(
            tierLabel.uppercase() + "!",
            before = { Div({ classes("lvx-badge", "sil") }) { Img(art, "") ; B { Text("?") } } },
            after = { Div({ classes("lvx-badge") }) { Img(art, badge.name) } },
        )
        GameHead("BLOQUEADO", "DESBLOQUEADO", "CONQUISTA!") { B { Text(badge.name) }; Text(" · emblema $tierLabel") }
        P({ classes("lvx-req") }) { Text(badge.requirement) }
        GameLootList(buildList {
            add(Loot("emoji_events", "SALA DE TROFÉUS", "$earned de ${progress.badges.size} emblemas", gold = true))
            add(Loot("military_tech", "FAIXA", "$tierLabel · ${badge.tier} de 6"))
            if (next != null) add(Loot("flag", "PRÓXIMO EMBLEMA", "${next.badge.name} · faltam ${next.remaining} ${next.badge.unit}"))
        })
        GameSay(when (badge.tier) {
            in 1..2 -> "Primeiro de muitos! Cada emblema é prova de que você está indo."
            in 3..4 -> "Esse é pesado! Pouca gente chega aqui."
            else -> "Lendário! Isso aqui é de quem estuda de verdade."
        })
        GameActions("Continuar", onClose, "Ver conquistas", onTrophies)
    }
}
