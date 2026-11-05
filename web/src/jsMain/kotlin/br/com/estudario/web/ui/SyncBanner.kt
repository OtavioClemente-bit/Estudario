package br.com.estudario.web.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import br.com.estudario.web.data.Store
import kotlinx.coroutines.delay
import org.jetbrains.compose.web.dom.B
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text

/**
 * "Sincronizando", igual ao app: quando o site traz da conta o que foi feito no celular, o Folha fica
 * entre o celular e o computador passando as folhas de um para o outro, e o aviso fecha com "Tudo em
 * dia". Fica pelo menos um instante na tela (a cópia costuma ser rápida) e não pisca à toa.
 */
@Composable
fun SyncBanner() {
    var phase by remember { mutableStateOf(0) } // 0 escondido, 1 sincronizando, 2 em dia, 3 saindo
    // vitrine para conferir a cena: ?syncdemo na URL finge uma sincronização de 2,5 s
    var demo by remember { mutableStateOf(kotlinx.browser.window.location.search.contains("syncdemo")) }
    LaunchedEffect(Unit) { if (demo) { delay(2500); demo = false } }
    LaunchedEffect(Store.syncing || demo) {
        if (Store.syncing || demo) { phase = 1; return@LaunchedEffect }
        if (phase != 1) return@LaunchedEffect
        delay(1400) // deixa as folhas fazerem pelo menos uma viagem
        phase = 2
        delay(1300)
        phase = 3
        delay(350)
        phase = 0
    }
    if (phase == 0) return
    Div({
        classes(*listOfNotNull("sync-banner", if (phase >= 2) "done" else null, if (phase == 3) "out" else null).toTypedArray())
        attr("role", "status"); attr("aria-live", "polite")
    }) {
        Div({ classes("sync-stage") }) {
            Div({ classes("sync-relay"); ref { el -> el.innerHTML = RELAY_SVG; onDispose { } } })
            Span({ classes("sync-dev", "left") }) { Icon("phone_android") }
            Span({ classes("sync-folha") }) { Folha(if (phase >= 2) "proud" else "talking", 58, live = false) }
            Span({ classes("sync-dev", "right") }) { Icon("art-laptop") }
        }
        B({ classes("sync-title") }) {
            if (phase >= 2) { Icon("check_circle"); Text(" Tudo em dia") } else Text("Sincronizando seus estudos")
            if (phase == 1) Span({ classes("sync-dots") }) { Span { Text(".") }; Span { Text(".") }; Span { Text(".") } }
        }
        Span({ classes("sync-sub") }) { Text(if (phase >= 2) "o que você fez no celular já está aqui" else "o Folha está trazendo o que você fez no celular") }
    }
}

/** O arco tracejado entre os aparelhos e as folhinhas viajando: uma vai, outra volta, passando pelo Folha. */
private val RELAY_SVG = """
<svg viewBox="0 0 300 80" preserveAspectRatio="xMidYMid meet" aria-hidden="true">
  <defs>
    <g id="sync-sheet"><rect x="-7" y="-9" width="14" height="18" rx="2.5" fill="#FFFDF8"/><path d="M-4 -4H5M-4 0H5M-4 4H5" stroke="#7FB2FF" stroke-width="1"/><path d="M-3.5 -8V8" stroke="#FF7A8A" stroke-width="1"/></g>
  </defs>
  <path class="sync-arc" d="M40 44 Q95 4 150 44"/>
  <path class="sync-arc" d="M150 44 Q205 4 260 44"/>
  <use href="#sync-sheet" class="sync-sheet"><animateMotion dur="1.8s" repeatCount="indefinite" rotate="auto" path="M40 44 Q95 4 150 44 Q205 4 260 44"/></use>
  <use href="#sync-sheet" class="sync-sheet"><animateMotion dur="1.8s" begin="-0.9s" repeatCount="indefinite" rotate="auto-reverse" path="M260 44 Q205 4 150 44 Q95 4 40 44"/></use>
</svg>
""".trimIndent()
