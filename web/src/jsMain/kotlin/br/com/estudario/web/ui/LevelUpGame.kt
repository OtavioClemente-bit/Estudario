package br.com.estudario.web.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import br.com.estudario.domain.FolhaWardrobe
import br.com.estudario.domain.LevelCrest
import br.com.estudario.domain.ProgressEngine
import kotlinx.browser.window
import org.jetbrains.compose.web.dom.B
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H2
import org.jetbrains.compose.web.dom.I
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

/**
 * Subiu de nível, com cara de jogo: o brasão antigo treme e estoura num clarão, o novo cai com onda de
 * choque e faíscas, o letreiro entra letra por letra e, em seguida, a barra do nível novo, os prêmios
 * (título novo, peças liberadas no armário, próxima patente) e o Folha comemorando.
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
    // as faíscas e os pontinhos do fundo: sorteados uma vez por cena
    val sparks = remember { List(30) { Triple(Random.nextInt(130, 250), Random.nextInt(4, 10), Random.nextInt(0, 140)) } }
    val motes = remember { List(22) { listOf(Random.nextInt(0, 100), Random.nextInt(4, 12), Random.nextInt(0, 6000), Random.nextInt(2, 5)) } }

    // o som de nível no momento do impacto (só toca com o som ligado em Ajustes)
    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(1050)
        runCatching { window.asDynamic().estudarioSom?.play("cena_nivel") }
    }

    Div({
        classes(*listOfNotNull("lvx", if (promo) "promo" else null, if (leaving) "out" else null).toTypedArray())
        attr("role", "dialog"); attr("aria-modal", "true"); attr("aria-label", "Subiu para o nível $level, $title")
        attr("style", "--c1:${rank.light};--c2:${rank.mid};--c3:${rank.dark}")
    }) {
        Div({ classes("lvx-bg") }) {
            Div({ classes("lvx-rays") })
            Div({ classes("lvx-grid") })
            motes.forEach { (x, s, d, t) -> Span({ classes("lvx-mote"); attr("style", "--x:$x%;--s:${s}px;--d:-${d}ms;--t:${t}s") }) }
        }
        Div({ classes("lvx-main") }) {
            Div({ classes("lvx-stage") }) {
                Div({ classes("lvx-halo") })
                repeat(3) { i -> Div({ classes("lvx-ring"); attr("style", "--i:$i") }) }
                Div({ classes("lvx-burst") }) {
                    sparks.forEachIndexed { i, (r, s, dl) -> Span({ attr("style", "--a:${i * 360 / sparks.size}deg;--r:${r}px;--s:${s}px;--dl:${dl}ms") }) }
                }
                Div({ classes("lvx-old") }) { LevelCrestView(old, 220) }
                Div({ classes("lvx-new") }) { LevelCrestView(level, 220) }
                Div({ classes("lvx-flash") })
                if (promo) Span({ classes("lvx-stamp") }) { Text("PROMOVIDO!") }
            }
            Div({ classes("lvx-head") }) {
                Span({ classes("lvx-kicker") }) { Text("NÍVEL $old"); Span({ classes("lvx-arrow") }) { Text("▶") }; B { Text("NÍVEL $level") } }
                H2({ classes("lvx-title"); attr("aria-label", "Subiu de nível!") }) {
                    "SUBIU DE NÍVEL!".forEachIndexed { i, ch -> Span({ attr("style", "--i:$i"); if (ch == ' ') classes("sp") }) { Text(if (ch == ' ') " " else ch.toString()) } }
                }
                P({ classes("lvx-rank") }) {
                    if (promo) { Text("Nova patente: "); B { Text(title) }; Text(" · ${rank.metal}") }
                    else { B { Text(title) }; Text(" · patente ${rank.metal} · ${LevelCrest.stars(level)}★") }
                }
            }
            Div({ classes("lvx-xp") }) {
                Div({ classes("lvx-xp-top") }) {
                    B { Text("Nível $level") }
                    Span { Text("faltam ${need - into} XP para o nível ${level + 1}") }
                }
                Div({ classes("lvx-xp-bar") }) { I({ classes("lvx-xp-fill"); attr("style", "--lvp:${pct.coerceAtLeast(2)}%") }) }
                Span({ classes("lvx-xp-total") }) { Text("$totalXp XP no total") }
            }
            val loot = buildList {
                if (promo) add(Triple("military_tech", "NOVO TÍTULO", "$title · patente ${rank.metal}"))
                unlocked.take(2).forEach { add(Triple("checkroom", "LIBEROU NO ARMÁRIO", "${it.name} · ${it.rarity.label}")) }
                if (nextTitle != null) {
                    val (at, name) = nextTitle
                    add(Triple("flag", "PRÓXIMA PATENTE", "$name no nível $at · ${if (at - level == 1) "falta 1 nível" else "faltam ${at - level} níveis"}"))
                } else add(Triple("emoji_events", "PATENTE MÁXIMA", "Você é ${ProgressEngine.levelTitle(level)}. Agora é colecionar estrelas."))
            }
            Div({ classes("lvx-loot") }) {
                loot.forEachIndexed { i, (icon, kicker, text) ->
                    Div({ classes("lvx-card", if (kicker == "NOVO TÍTULO") "gold" else "plain"); attr("style", "--i:$i") }) {
                        Span({ classes("lvx-card-ic") }) { Icon(icon) }
                        Div({ classes("lvx-card-txt") }) { Span { Text(kicker) }; B { Text(text) } }
                    }
                }
            }
            Div({ classes("lvx-say") }) {
                Folha("proud", 64)
                Span({ classes("lvx-bubble") }) { Text(levelLine(level, title, promo)) }
            }
            Div({ classes("lvx-actions") }) {
                Button({ classes("lvx-go"); attr("type", "button"); onClick { onClose() } }) { Text("Continuar") }
                Button({ classes("lvx-ghost"); attr("type", "button"); onClick { onProfile() } }) { Text("Ver meu perfil") }
            }
        }
    }
}

/** A fala do Folha na subida de nível. */
private fun levelLine(level: Int, title: String, promo: Boolean): String = when {
    promo -> "Promoção! Agora você é $title. Isso é estudo de verdade aparecendo."
    level % 3 == 0 -> "Nível $level no bolso. Bora pro ${level + 1}?"
    level % 3 == 1 -> "Mais um degrau rumo à vaga. Cada XP é questão feita e revisão cumprida."
    else -> "Subiu! Quem estuda todo dia chega lá. Você está chegando."
}
