package br.com.estudario.web.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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
private fun GameShell(colors: Triple<String, String, String>, sound: String?, label: String, leaving: Boolean, extra: String? = null, content: @Composable () -> Unit) {
    val motes = remember { List(22) { listOf(Random.nextInt(0, 100), Random.nextInt(4, 12), Random.nextInt(0, 6000), Random.nextInt(2, 5)) } }
    // o som no momento do impacto (só toca com o som ligado em Ajustes)
    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(1050)
        if (sound != null) runCatching { window.asDynamic().estudarioSom?.play(sound) }
    }
    Div({
        classes(*(listOfNotNull("lvx", if (leaving) "out" else null) + extra?.split(" ").orEmpty().filter { it.isNotBlank() }).toTypedArray())
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
private fun GameStage(stamp: String?, before: @Composable () -> Unit, after: @Composable () -> Unit, extra: @Composable () -> Unit = {}) {
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
        extra()
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
private fun GameSay(line: String, mood: String = "proud", startled: Boolean = false) {
    Div({ classes(*listOfNotNull("lvx-say", if (startled) "startled" else null).toTypedArray()) }) {
        Div({ classes("lvx-say-folha") }) { Folha(mood, 64); if (startled) Span({ classes("lvx-bang") }) { Text("!") } }
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

// ------------------------------------------------------------------ a fênix

/**
 * A fênix da sequência pintada com fogo em tempo real (phoenix.js, <estudario-fenix>, a mesma do app):
 * dorme como carvão com veias de brasa e acorda sozinha depois de [wakeAt] ms, explodindo em fogo.
 */
@Composable
internal fun FenixView(size: Int, wakeAt: Int?, awake: Boolean = false) {
    org.jetbrains.compose.web.dom.TagElement<org.w3c.dom.HTMLElement>(
        elementBuilder = org.jetbrains.compose.web.dom.ElementBuilder.createBuilder("estudario-fenix"),
        applyAttrs = {
            classes("lvx-fenix"); attr("style", "width:${size}px;height:${size}px")
            attr("state", if (awake) "awake" else "sleep"); if (wakeAt != null) attr("wake-at", wakeAt.toString())
            attr("role", "img"); attr("aria-label", "A fênix da sequência")
        },
        content = null,
    )
}

/** Os dias da semana: chama acesa nos dias estudados, apagada nos outros; hoje maior e brilhando. */
@Composable
private fun GameWeek(week: List<Pair<String, Boolean>>) {
    Div({ classes("lvx-week") }) {
        week.forEachIndexed { i, (letter, done) ->
            val today = i == week.lastIndex
            Div({ classes(*listOfNotNull("lvx-day", if (done) "on" else null, if (today) "today" else null).toTypedArray()); attr("style", "--i:$i") }) {
                Span { Text(if (today) "HOJE" else letter) }
                ProfileArt("flame", size = if (today) 30 else 22, lit = done)
            }
        }
    }
}

/**
 * O contador da sequência: o número de ontem sobe e sai, o de hoje entra rolando de baixo com um pulo
 * e acende em fogo (no mesmo instante em que a fênix acorda), com "dias de sequência" embaixo.
 */
@Composable
private fun StreakCounter(days: Int) {
    Div({ classes("lvx-count"); attr("aria-label", "$days ${if (days == 1) "dia" else "dias"} de sequência") }) {
        Div({ classes("lvx-count-roll") }) {
            Span({ classes("lvx-count-old") }) { Text("${(days - 1).coerceAtLeast(0)}") }
            Span({ classes("lvx-count-new") }) { Text("$days") }
        }
        Span({ classes("lvx-count-label") }) { Text(if (days == 1) "DIA DE SEQUÊNCIA" else "DIAS DE SEQUÊNCIA") }
    }
}

// ------------------------------------------------------------------ sequência do dia

/** Os marcos da sequência (dias seguidos) que a cena mostra como próximo objetivo. */
private val STREAK_MARKS = listOf(3, 7, 14, 21, 30, 45, 60, 90, 120, 180, 270, 365)

/**
 * Dia cumprido, com cara de jogo: a fênix dorme cinza; um pontinho preto começa a girar cada vez mais
 * rápido, vira um redemoinho e, no clarão, ela acorda pegando fogo e abre as asas (o Folha leva um
 * susto). Os dias da semana acendem com a chama, hoje em destaque, e entram os prêmios.
 */
@Composable
internal fun StreakGame(days: Int, best: Int, week: List<Pair<String, Boolean>>, xpToday: Int, leaving: Boolean, onClose: () -> Unit) {
    val record = days > 1 && days >= best
    val next = STREAK_MARKS.firstOrNull { it > days }
    // o redemoinho girando e, no clarão, o fogo
    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(250); som("tonto")
        kotlinx.coroutines.delay(800); som("combo")
    }
    GameShell(Triple("#FFE08A", "#FF8A3D", "#B23A0E"), null, "$days dias de sequência", leaving, "streak") {
        GameStage(
            if (record) "RECORDE!" else null,
            before = { Div({ classes("lvx-phx") }) { Span({ classes("lvx-zz") }) { Text("z"); B { Text("z") }; I { Text("z") } } } },
            after = { Div({ classes("lvx-phx") }) },
            extra = {
                FenixView(340, wakeAt = 1050)
                Div({ classes("lvx-vortex") })
            },
        )
        StreakCounter(days)
        GameHead(if (days <= 1) "DORMINDO" else "DIA ${days - 1}", if (days == 1) "ACORDOU" else "DIA $days", "SEQUÊNCIA!") {
            B { Text(if (days == 1) "1 dia" else "$days dias") }
            Text(if (days == 1) " · a fênix acordou" else " seguidos estudando")
        }
        GameWeek(week)
        GameLootList(buildList {
            if (xpToday > 0) add(Loot("bolt", "XP DE HOJE", "+$xpToday XP no dia", gold = true))
            if (next != null) add(Loot("flag", "PRÓXIMO MARCO", "$next dias · ${if (next - days == 1) "falta 1 dia" else "faltam ${next - days} dias"}"))
            add(if (record) Loot("emoji_events", "NOVO RECORDE", "Sua maior sequência: $days dias") else Loot("emoji_events", "SEU RECORDE", "$best dias · faltam ${best - days} para bater"))
        })
        GameSay(mood = "surprised", startled = true, line = when {
            days <= 1 -> "Que susto! A fênix acordou. Volte amanhã para ela não dormir de novo."
            record -> "Recorde! A fênix nunca esteve tão acesa. Amanhã tem mais."
            else -> "Ela acordou com tudo! Faltam ${best - days} para o seu recorde de $best."
        })
        GameActions("Bora continuar", onClose)
    }
}

/**
 * Sequência salva pelo marca-página: a fênix dorme quase apagada, o Folha chega na ponta dos pés com o
 * marca-página, solta uma faísca e ela acorda em fogo. Uma vez por dia salvo.
 */
@Composable
internal fun StreakSavedGame(days: Int, left: Int, dayLabel: String, week: List<Pair<String, Boolean>>, onClose: () -> Unit) {
    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(800); som("toque")
        kotlinx.coroutines.delay(250); som("combo")
    }
    GameShell(Triple("#FFE08A", "#FF8A3D", "#B23A0E"), null, "Sequência salva pelo Folha", false, "streak saved") {
        GameStage(
            "SALVA!",
            before = { Div({ classes("lvx-phx") }) { Span({ classes("lvx-zz") }) { Text("z"); B { Text("z") }; I { Text("z") } } } },
            after = { Div({ classes("lvx-phx") }) },
            extra = {
                FenixView(340, wakeAt = 1050)
                Div({ classes("lvx-tiptoe") }) { Folha("wink", 64); Img(bookmarkSrc(), "") }
                Span({ classes("lvx-spark") })
            },
        )
        StreakCounter(days)
        GameHead("QUASE APAGOU", "ACESA", "SEQUÊNCIA SALVA!") {
            B { Text("$days ${if (days == 1) "dia" else "dias"}") }
            Text(if (dayLabel.isNotBlank()) " · o Folha marcou $dayLabel" else " de sequência")
        }
        GameWeek(week)
        GameLootList(buildList {
            add(Loot("bookmark", "MARCA-PÁGINA USADO", if (dayLabel.isNotBlank()) "No dia sem estudo: $dayLabel" else "No dia sem estudo", gold = true))
            add(Loot("bookmarks", "SOBRAM", if (left > 0) "$left ${if (left == 1) "marca-página" else "marca-páginas"} guardado${if (left == 1) "" else "s"}" else "Nenhum. Estude 7 dias seguidos para ganhar outro"))
            add(Loot("local_fire_department", "SEQUÊNCIA", "$days ${if (days == 1) "dia" else "dias"} e contando"))
        })
        GameSay("Shhh… ela quase apagou, mas eu acendi de novo pra você. Bora não deixar dormir hoje?")
        GameActions("Bora continuar", onClose)
    }
}

// ------------------------------------------------------------------ emblema novo

/** As cores de cada faixa de emblema, nos mesmos metais das patentes. */
private fun tierColors(tier: Int): Triple<String, String, String> {
    val metal = when (tier) { 1 -> "Bronze"; 2 -> "Prata"; 3 -> "Ouro"; 4 -> "Safira"; 5 -> "Rubi"; else -> "Esmeralda" }
    return rankColors(LevelCrest.ranks.first { it.metal == metal })
}

/** Quando a medalha sai da forja na cena do Folha (folha-live.js, cena "emblema"). */
private const val FORGE_REVEAL_MS = 4350L

/**
 * Emblema novo: a cena da forja do Folha (ele cunha a medalha em três marteladas e ela sobe com a arte
 * de emblemas/g). No momento em que ela sai, onda de choque, faíscas e a faixa carimbada; depois o
 * letreiro, o que a pessoa fez, a sala de troféus e o próximo emblema.
 */
@Composable
internal fun BadgeGame(badge: Badge, leaving: Boolean, onClose: () -> Unit, onTrophies: () -> Unit) {
    val tierLabel = tierName(badge.tier)
    val progress = remember { Progress.of(Store.data).progress }
    val earned = progress.earnedBadges.size
    val next = progress.nextBadges.firstOrNull { it.badge.id != badge.id }
    // o que vem depois da forja entra quando a medalha sai (com o mesmo relógio das outras cenas)
    var revealed by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { kotlinx.coroutines.delay(FORGE_REVEAL_MS - 1050); revealed = true }
    GameShell(tierColors(badge.tier), null, "Emblema novo: ${badge.name}", leaving, "badge") {
        Div({ classes("lvx-forge") }) {
            org.jetbrains.compose.web.dom.TagElement<org.w3c.dom.HTMLElement>(
                elementBuilder = org.jetbrains.compose.web.dom.ElementBuilder.createBuilder("estudario-cena"),
                applyAttrs = {
                    attr("tipo", "emblema"); attr("tier", badge.tier.toString()); attr("categoria", badge.id.substringBefore("-"))
                    attr("faixa", tierLabel); attr("arte", "emblemas/g/${badge.id}.webp"); attr("bleed", "34")
                    attr("aria-label", "O Folha forja a medalha do emblema ${badge.name}")
                },
                content = null,
            )
            if (revealed) Div({ classes("lvx-forge-fx") }) {
                val sparks = remember { List(30) { Triple(Random.nextInt(130, 250), Random.nextInt(4, 10), Random.nextInt(0, 140)) } }
                repeat(3) { i -> Div({ classes("lvx-ring"); attr("style", "--i:$i") }) }
                Div({ classes("lvx-burst") }) {
                    sparks.forEachIndexed { i, (r, s, dl) -> Span({ attr("style", "--a:${i * 360 / sparks.size}deg;--r:${r}px;--s:${s}px;--dl:${dl}ms") }) }
                }
                Div({ classes("lvx-flash") })
                Span({ classes("lvx-stamp") }) { Text(tierLabel.uppercase() + "!") }
            }
        }
        if (revealed) {
            GameHead("BLOQUEADO", "DESBLOQUEADO", "CONQUISTA!") { B { Text(badge.name) }; Text(" · emblema $tierLabel") }
            P({ classes("lvx-req") }) { Text(badge.requirement) }
            GameLootList(buildList {
                add(Loot("emoji_events", "SALA DE TROFÉUS", "$earned de ${progress.badges.size} emblemas", gold = true))
                add(Loot("military_tech", "FAIXA", "$tierLabel · ${badge.tier} de 6"))
                if (next != null) add(Loot("flag", "PRÓXIMO EMBLEMA", "${next.badge.name} · faltam ${next.remaining} ${next.badge.unit}"))
            })
            GameSay(when (badge.tier) {
                in 1..2 -> "Saiu quentinha da forja! Cada emblema é prova de que você está indo."
                in 3..4 -> "Essa deu trabalho de martelar! Pouca gente chega aqui."
                else -> "Lendário! Até a bigorna tremeu. Isso aqui é de quem estuda de verdade."
            })
            GameActions("Continuar", onClose, "Ver conquistas", onTrophies)
        } else Div({ classes("lvx-forge-skip") }) {
            Button({ classes("lvx-ghost"); attr("type", "button"); onClick { revealed = true } }) { Text("Pular") }
        }
    }
}
