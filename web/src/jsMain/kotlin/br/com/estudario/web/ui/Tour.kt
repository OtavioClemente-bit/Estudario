package br.com.estudario.web.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.browser.document
import kotlinx.browser.localStorage
import kotlinx.browser.window
import org.jetbrains.compose.web.dom.B
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text
import org.w3c.dom.Element
import org.w3c.dom.asList
import org.w3c.dom.events.Event
import org.w3c.dom.events.KeyboardEvent

/**
 * O giro de boas-vindas, como o do app (AppTour.kt): na primeira visita, um holofote passa pelas
 * partes principais da tela com um balão do Folha explicando cada uma. Setas (ou ← →) avançam,
 * Esc pula. Fica guardado neste navegador; dá para rever em Ajustes.
 */
private class WebTourStep(val selector: String?, val title: String, val body: String)

private val welcomeTour = listOf(
    WebTourStep(null, "Bem-vindo ao Estudário", "Um giro rápido pelas partes principais. Use as setas ou as teclas ← e →."),
    WebTourStep(".home-hero", "Seu concurso", "O cartão mostra o concurso, quanto do edital você já viu e quantos dias faltam para a prova. Clique nele para abrir o edital."),
    WebTourStep("a.nav-item[href=\"#/edital\"]", "Meus concursos", "Tudo começa aqui: as matérias e os tópicos do edital. O plano, as revisões e as questões saem desse conteúdo."),
    WebTourStep("a.nav-item[href=\"#/plano\"]", "Plano de estudos", "Seu cronograma até a prova, com a missão de cada dia. Ele se ajusta sozinho quando a rotina muda."),
    WebTourStep("a.nav-item[href=\"#/treinar\"]", "Treinar questões", "Desafio do dia, treino por matéria e simulados. Cada resposta atualiza o domínio do tópico."),
    WebTourStep(".topbar .save-state", "Tudo salvo na conta", "Este selo mostra se seus estudos estão sincronizados. O que você faz aqui aparece no app do celular, e vice-versa."),
    WebTourStep(".topbar .avatar", "Seu perfil", "Nível, XP, conquistas, ajustes e os planos ficam no perfil. Bons estudos!"),
)

private const val TOUR_KEY = "estudario.tour.boas-vindas"

/** O tour aberto agora (null = fechado). Ajustes > "Rever o tour" abre de novo. */
private var tourOpen by mutableStateOf<Boolean?>(null)

fun replayTour() { tourOpen = true; br.com.estudario.web.Router.go(br.com.estudario.web.Route.Home) }

private fun markSeen() { runCatching { localStorage.setItem(TOUR_KEY, "1") } }

/** O primeiro elemento do seletor que está de fato na tela (o menu lateral some no celular). */
private fun visibleTarget(selector: String?): Element? {
    if (selector == null) return null
    return document.querySelectorAll(selector).asList().filterIsInstance<Element>().firstOrNull { el ->
        val r = el.getBoundingClientRect()
        r.width > 0 && r.height > 0 && r.right > 0 && r.left < window.innerWidth && r.bottom > 0 && r.top < window.innerHeight + 400
    }
}

@Composable
fun WelcomeTour() {
    if (tourOpen == null) tourOpen = runCatching { localStorage.getItem(TOUR_KEY) == null }.getOrDefault(false)
    if (tourOpen != true) return
    var index by remember { mutableIntStateOf(0) }
    // Muda a cada rolagem ou redimensionamento, para o holofote acompanhar o alvo.
    var tick by remember { mutableIntStateOf(0) }
    val close = { markSeen(); tourOpen = false }
    val step = welcomeTour[index]
    val last = index == welcomeTour.lastIndex
    DisposableEffect(Unit) {
        val relayout: (Event) -> Unit = { tick++ }
        val keys: (Event) -> Unit = { e ->
            when ((e as KeyboardEvent).key) {
                "Escape" -> close()
                "ArrowRight", "Enter" -> if (index < welcomeTour.lastIndex) index++ else close()
                "ArrowLeft" -> if (index > 0) index--
            }
        }
        window.addEventListener("resize", relayout)
        window.addEventListener("scroll", relayout, true)
        window.addEventListener("keydown", keys)
        onDispose {
            window.removeEventListener("resize", relayout)
            window.removeEventListener("scroll", relayout, true)
            window.removeEventListener("keydown", keys)
        }
    }
    LaunchedEffect(index) {
        visibleTarget(step.selector)?.asDynamic()?.scrollIntoView(js("({block: 'nearest', behavior: 'smooth'})"))
        kotlinx.coroutines.delay(350)
        tick++
    }
    val rect = tick.let { visibleTarget(step.selector)?.getBoundingClientRect() }
    Div({ classes("tour"); attr("role", "dialog"); attr("aria-modal", "true"); attr("aria-label", "Giro pelo Estudário") }) {
        if (rect == null) Div({ classes("tour-dim") })
        else Div({
            classes("tour-spot")
            attr("style", "left:${rect.left - 8}px;top:${rect.top - 8}px;width:${rect.width + 16}px;height:${rect.height + 16}px")
        })
        // O balão fica abaixo do alvo se couber, senão acima; sem alvo, no centro.
        val bubbleStyle = when {
            rect == null -> "left:50%;top:50%;transform:translate(-50%,-50%)"
            rect.bottom + 230 < window.innerHeight -> "left:${clampLeft(rect.left)}px;top:${rect.bottom + 18}px"
            else -> "left:${clampLeft(rect.left)}px;bottom:${window.innerHeight - rect.top + 18}px"
        }
        Div({ classes("tour-bubble"); attr("style", bubbleStyle) }) {
            Div({ classes("row"); attr("style", "gap:12px;align-items:flex-start") }) {
                Span({ classes("tour-folha") }) { Folha(if (last) "happy" else "wave", 56) }
                Div({ attr("style", "flex:1;min-width:0") }) {
                    B({ classes("tour-title") }) { Text(step.title) }
                    P({ classes("tour-body") }) { Text(step.body) }
                }
            }
            Div({ classes("tour-foot") }) {
                Div({ classes("tour-dots") }) {
                    welcomeTour.indices.forEach { i -> Span({ classes(*listOfNotNull("dot", if (i == index) "on" else null).toTypedArray()) }) }
                }
                Button({ classes("tour-skip"); onClick { close() } }) { Text(if (last) "Fechar" else "Pular") }
                if (index > 0) Button({ classes("tour-nav"); attr("aria-label", "Voltar"); onClick { index-- } }) { Icon("arrow_back") }
                Button({ classes("tour-nav", "go"); attr("aria-label", if (last) "Começar" else "Avançar"); onClick { if (last) close() else index++ } }) {
                    if (last) Text("Começar") else Icon("arrow_forward")
                }
            }
        }
    }
}

private fun clampLeft(left: Double): Double {
    val width = minOf(360.0, window.innerWidth - 32.0)
    return left.coerceIn(16.0, maxOf(16.0, window.innerWidth - width - 16.0))
}
