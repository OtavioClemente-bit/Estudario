package br.com.estudario.web.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import br.com.estudario.web.data.Progress
import br.com.estudario.web.data.Queries
import br.com.estudario.web.data.Store
import org.jetbrains.compose.web.dom.B
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H2
import org.jetbrains.compose.web.dom.Img
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text

/**
 * O marca-página do Folha: a cada 7 dias seguidos o Folha guarda um marca-página (até 2). Num dia sem
 * estudo ele chega, marca aquele dia e a chama da fênix não apaga. É a proteção da sequência, com a
 * cara do Estudário (o Folha é um caderno: marca-página guarda o seu lugar).
 */
const val BOOKMARK_SVG = """<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 64 64"><defs><linearGradient id="b" x1="0" y1="0" x2="1" y2="1"><stop offset="0" stop-color="#8B7BFF"/><stop offset="1" stop-color="#4A36E0"/></linearGradient><linearGradient id="g" x1="0" y1="0" x2="0" y2="1"><stop offset="0" stop-color="#FFE38A"/><stop offset="1" stop-color="#FFB421"/></linearGradient></defs><path d="M18 4 h28 a4 4 0 0 1 4 4 v52 l-18 -12 l-18 12 v-52 a4 4 0 0 1 4 -4z" fill="url(#b)" stroke="#2A1F8F" stroke-width="2.5" stroke-linejoin="round"/><path d="M22 4 v44" stroke="#FFFFFF" stroke-opacity=".25" stroke-width="3"/><circle cx="32" cy="24" r="9" fill="url(#g)" stroke="#B86E00" stroke-width="2"/><path d="M28 24 q4 -6 8 0 q-4 6 -8 0z" fill="#2A8F5A"/><path d="M32 18 v12" stroke="#1E6E43" stroke-width="1.4"/></svg>"""

fun bookmarkSrc(): String = "data:image/svg+xml;charset=utf-8," + js("encodeURIComponent")(BOOKMARK_SVG) as String

/** Os dias salvos que a pessoa já viu (a animação aparece uma vez por dia salvo). */
private object SavedDays {
    private const val KEY = "estudario.streak.saved.seen"
    fun seen(): Set<String> = runCatching { kotlinx.browser.window.localStorage.getItem(KEY)?.split(",")?.filter { it.isNotBlank() }?.toSet() }.getOrNull() ?: emptySet()
    fun mark(days: Collection<String>) { runCatching { kotlinx.browser.window.localStorage.setItem(KEY, (seen() + days).toList().takeLast(30).joinToString(",")) } }
}

/**
 * Quando a pessoa volta e um marca-página salvou a sequência: a cena do Folha chegando, colocando o
 * marca-página no dia que faltou e reacendendo a chama. Uma vez por dia salvo.
 */
@Composable
fun StreakSaveHost() {
    // Só na demonstração: ?demo&salvou mostra a cena (para conferir a animação).
    var preview by remember { mutableStateOf(Store.demo && kotlinx.browser.window.location.search.contains("salvou")) }
    if (preview && Store.load == br.com.estudario.web.data.LoadState.Ready) { StreakSaveScene(12, 1, "Sex, 24 out", "S1,T1,Q2,Q1,S1,S1,D1") { preview = false }; return }
    if (Store.demo || Store.load != br.com.estudario.web.data.LoadState.Ready) return
    val streak = Progress.of(Store.data).streak
    val recent = streak.protectedDays.filter { Queries.todayDate().toEpochDays() - it.toEpochDays() <= 7 }.map { it.toString() }
    val pending = recent.filter { it !in SavedDays.seen() }
    var open by remember(pending.joinToString()) { mutableStateOf(pending.isNotEmpty()) }
    if (!open || pending.isEmpty()) return
    val savedDate = pending.maxOrNull()?.let { kotlinx.datetime.LocalDate.parse(it) }
    // os últimos 7 dias para os braseiros da cena: 1 feito, 2 salvo pelo Folha, 0 sem estudo
    val today = Queries.todayDate()
    val byDate = streak.calendar.associateBy { it.date }
    val semana = (6 downTo 0).joinToString(",") { back ->
        val d = kotlinx.datetime.LocalDate.fromEpochDays(today.toEpochDays() - back)
        val flag = when { d in streak.protectedDays -> "2"; byDate[d]?.let { br.com.estudario.domain.StreakEngine.isDone(it, streak.goal) } == true -> "1"; else -> "0" }
        Queries.weekdayShort[d.dayOfWeek.ordinal].take(1).uppercase() + flag
    }
    StreakSaveScene(streak.current, streak.freezes, savedDate?.let { Queries.weekdayShort[it.dayOfWeek.ordinal].replaceFirstChar { c -> c.uppercase() } + ", " + Queries.shortDate(it) } ?: "", semana) {
        SavedDays.mark(pending); open = false
    }
}

@Composable
private fun StreakSaveScene(days: Int, left: Int, dayLabel: String, semana: String, onClose: () -> Unit) {
    // A cena desenhada (folha-live.js, <estudario-cena tipo="salvou">): a fênix dorme quase apagada, o
    // Folha chega na ponta dos pés pedindo silêncio e acende a chama ele mesmo. O texto entra no fim.
    var done by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { kotlinx.coroutines.delay(5600); done = true }
    Div({ classes("ss-back"); onClick { if (done) onClose() } }) {
        Div({ classes("ss-stage"); onClick { it.stopPropagation() } }) {
            Div({ classes("ss-cena") }) {
                org.jetbrains.compose.web.dom.TagElement<org.w3c.dom.HTMLElement>(
                    elementBuilder = org.jetbrains.compose.web.dom.ElementBuilder.createBuilder("estudario-cena"),
                    applyAttrs = { attr("tipo", "salvou"); attr("dias", days.toString()); attr("semana", semana); attr("bleed", "34"); attr("aria-label", "O Folha acende a chama da fênix e salva a sequência") },
                    content = null,
                )
            }
            Div({ classes(*listOfNotNull("ss-text", if (done) "on" else null).toTypedArray()) }) {
                Span({ classes("ss-eyebrow") }) { Text("SEQUÊNCIA SALVA" + if (dayLabel.isNotBlank()) " · $dayLabel" else "") }
                H2 { Text("Shhh… o Folha reacendeu a sua fênix") }
                P { Text("Num dia sem estudo ela quase apagou, e o Folha usou um marca-página para acender de novo: são $days ${if (days == 1) "dia" else "dias"} de sequência. " + if (left > 0) "Ainda sobra $left ${if (left == 1) "marca-página" else "marca-páginas"}." else "Estude 7 dias seguidos para ele guardar outro.") }
                Btn("Bora continuar", onClose, style = "primary", icon = "local_fire_department")
            }
        }
    }
}

private const val SS_FLAME = """<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 64 64"><defs><linearGradient id="f" x1="0" y1="1" x2="0" y2="0"><stop offset="0" stop-color="#FF4D2E"/><stop offset=".55" stop-color="#FF9A1F"/><stop offset="1" stop-color="#FFD84A"/></linearGradient><linearGradient id="i" x1="0" y1="1" x2="0" y2="0"><stop offset="0" stop-color="#FFB13B"/><stop offset="1" stop-color="#FFF4B8"/></linearGradient></defs><path d="M33 4c3 10 15 16 15 31a16 16 0 0 1-32 0c0-8 4-12 7-16 1 5 3 8 6 9-2-9 0-17 4-24z" fill="url(#f)"/><path d="M32 30c2 5 8 8 8 15a8 8 0 0 1-16 0c0-4 2-6 4-8 0 3 2 5 3 5-1-5 0-9 1-12z" fill="url(#i)"/></svg>"""
