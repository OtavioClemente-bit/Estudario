package br.com.estudario.web.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import br.com.estudario.web.GoogleSignIn
import br.com.estudario.web.data.Auth
import br.com.estudario.web.data.Store
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.dom.A
import org.jetbrains.compose.web.dom.B
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Em
import org.jetbrains.compose.web.dom.H1
import org.jetbrains.compose.web.dom.H2
import org.jetbrains.compose.web.dom.Li
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.dom.Ul

/**
 * Entrada do site, com a cara da página estudario.com.br: o papel pontilhado do caderno que reage
 * ao mouse (o cursor vira marca-texto, ambient.js), o Folha vivo sentado em cima do cartão de entrada,
 * conversando e mudando de humor, e a turma desenhada à mão flutuando em volta, em camadas que
 * acompanham o mouse. Sem demonstração aqui: ela continua em app.estudario.com.br/?demo.
 */
@Composable
fun LoginScreen() {
    val scope = rememberCoroutineScope()
    var busy by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    fun run(block: suspend () -> Unit) {
        busy = true
        error = null
        scope.launch {
            try { block() } catch (e: Throwable) { error = e.message ?: "Algo deu errado." } finally { busy = false }
        }
    }

    // O Folha não se deixa pegar: o mouse chega perto (ou o dedo toca nele) e ele sai correndo para
    // outro canto da tela, reclamando. Depois de algumas fugas ele cansa, se rende e volta ao lugar.
    var offX by remember { mutableStateOf(0.0) }
    var offY by remember { mutableStateOf(0.0) }
    var fleeing by remember { mutableStateOf(false) }
    var facingLeft by remember { mutableStateOf(false) }
    var escapes by remember { mutableStateOf(0) }
    var tired by remember { mutableStateOf(false) }
    var fleeLine by remember { mutableStateOf<String?>(null) }
    var lastFlee by remember { mutableStateOf(0.0) }
    val fleeLines = remember {
        listOf(
            "Ei! Aqui não!",
            "Cócegas não, por favor!",
            "Pega eu se for capaz!",
            "Tô estudando, não posso parar!",
            "Quase! Quaaase!",
            "Você é rápido, hein? Mas eu sou um livro de corrida!",
        )
    }
    val reduceMotion = remember { window.matchMedia("(prefers-reduced-motion: reduce)").matches }
    fun flee(px: Double, py: Double) {
        if (tired || reduceMotion) return
        val now = kotlin.js.Date.now()
        if (now - lastFlee < 650) return
        lastFlee = now
        val el = document.querySelector(".login-folha") ?: return
        val r = el.getBoundingClientRect()
        val homeX = r.left + r.width / 2 - offX
        val homeY = r.top + r.height / 2 - offY
        val w = window.innerWidth.toDouble()
        val h = window.innerHeight.toDouble()
        // O canto mais longe do mouse entre alguns sorteados (com espaço para o balão à direita).
        var best = homeX to homeY
        var bestD = -1.0
        repeat(10) {
            val x = 90 + kotlin.random.Random.nextDouble() * (w - 90 - 330).coerceAtLeast(10.0)
            val y = 120 + kotlin.random.Random.nextDouble() * (h - 120 - 110).coerceAtLeast(10.0)
            val d = kotlin.math.hypot(x - px, y - py)
            if (d > bestD) { bestD = d; best = x to y }
        }
        facingLeft = best.first < r.left + r.width / 2
        offX = best.first - homeX
        offY = best.second - homeY
        fleeing = true
        escapes++
        fleeLine = fleeLines[(escapes - 1) % fleeLines.size]
        val mine = now
        scope.launch {
            delay(780)
            fleeing = false
            if (escapes >= 6) {
                tired = true
                fleeLine = "Ufa… tá bom, você venceu! Volto pro meu lugar."
                delay(1800)
                facingLeft = offX > 0
                offX = 0.0; offY = 0.0; fleeing = true
                delay(800)
                fleeing = false; facingLeft = false
                fleeLine = "Pronto. Agora entra aí que eu guardo o seu lugar!"
                delay(3000)
                fleeLine = null; tired = false; escapes = 0
            } else {
                delay(3200)
                if (lastFlee == mine) fleeLine = null
            }
        }
    }

    // O fundo vivo e a turma acompanhando o mouse; tudo sai junto com a tela.
    DisposableEffect(Unit) {
        val root = document.querySelector(".login") as? org.w3c.dom.HTMLElement
        val ambient = runCatching { root?.let { window.asDynamic().estudarioAmbient(it) } }.getOrNull()
        val move: (org.w3c.dom.events.Event) -> Unit = { e ->
            val ev = e.asDynamic()
            if (ev.pointerType == "mouse") {
                val x = ((ev.clientX as Number).toDouble() / window.innerWidth - .5) * 2
                val y = ((ev.clientY as Number).toDouble() / window.innerHeight - .5) * 2
                root?.style?.setProperty("--mx", x.toString())
                root?.style?.setProperty("--my", y.toString())
                // Mouse chegando perto do Folha: ele foge antes do clique.
                (document.querySelector(".login-folha .folha") as? org.w3c.dom.Element)?.getBoundingClientRect()?.let { r ->
                    val px = (ev.clientX as Number).toDouble()
                    val py = (ev.clientY as Number).toDouble()
                    val d = kotlin.math.hypot(px - (r.left + r.width / 2), py - (r.top + r.height / 2))
                    if (d < r.width * 0.62) flee(px, py)
                }
            }
        }
        window.addEventListener("pointermove", move)
        onDispose {
            window.removeEventListener("pointermove", move)
            runCatching { ambient?.destroy() }
        }
    }

    // O Folha conversa enquanto a pessoa decide; entrando, ele abre o caderno; com erro, fica triste.
    val lines = remember {
        listOf(
            "wave" to "Oi! Eu sou o Folha. Bora estudar?",
            "talking" to "É a mesma conta do celular: entra com o Google e pronto.",
            "thinking" to "Seu plano e suas questões já estão te esperando…",
            "happy" to "Plano, teoria e questões: tudo sincronizado!",
        )
    }
    var line by remember { mutableStateOf(0) }
    LaunchedEffect(Unit) { while (true) { delay(4600); if (!busy) line = (line + 1) % lines.size } }
    val (mood, speech) = when {
        busy -> "thinking" to "Abrindo o seu caderno…"
        error != null -> "sad" to "Ih, não deu. Tenta de novo?"
        fleeLine != null -> (if (fleeing) "happy" else "talking") to fleeLine!!
        else -> lines[line]
    }

    Div({ classes("login") }) {
        Div({ classes("login-top") }) {
            A(href = "https://estudario.com.br", { classes("login-brand") }) {
                Span({ classes("login-word") }) { Text("estud"); Em { Text("á") }; Text("rio") }
                Span({ classes("login-tag") }) { Text("DO EDITAL À APROVAÇÃO") }
            }
            A(href = "https://estudario.com.br", { classes("login-back") }) { Text("Conhecer o Estudário →") }
        }
        Div({ classes("login-grid") }) {
            Div({ classes("login-left") }) {
                Span({ classes("login-chip") }) { Icon("Laptop"); Text("Estudário no navegador") }
                H1 { Text("Seu estudo, no rumo certo. "); Span({ classes("login-mark") }) { Text("Onde você estiver.") } }
                P({ classes("login-lead") }) { Text("O mesmo plano, edital, questões e revisões do app, no computador, no tablet ou no celular. Tudo na mesma conta.") }
                Ul({ classes("login-feats") }) {
                    Feature("Calendar", "Um plano que cabe na sua vida", "Do dia ao ano, e se ajusta quando você atrasa")
                    Feature("Clipboard", "Seu edital organizado", "Cada tópico com teoria, flashcards e questões")
                    Feature("Target", "Treino de verdade", "Questões, revisões espaçadas e caderno de erros")
                    Feature("Cycle", "Tudo sincronizado", "App e navegador na mesma conta, em segundos")
                }
            }
            Div({ classes("login-right") }) {
                Div({ classes("login-stage") }) {
                    // A turma em volta do cartão: cada adesivo numa camada (--d) que segue o mouse.
                    listOf("Star" to 22, "Fire" to -18, "Bulb" to 14, "Trophy" to -24, "Books" to 12, "Check" to -10).forEachIndexed { i, (g, d) ->
                        Span({ classes("lsticker", "ls$i"); attr("style", "--d:$d"); attr("aria-hidden", "true") }) { Span { Icon(g) } }
                    }
                    Div({
                        classes(*listOfNotNull("login-folha", if (fleeing) "fleeing" else null, if (facingLeft) "left" else null).toTypedArray())
                        attr("style", "transform:translate(${offX}px, ${offY}px)")
                        // No toque (celular, tablet) ele foge ao ser tocado.
                        addEventListener("pointerdown") { e -> val ev = e.nativeEvent.asDynamic(); flee((ev.clientX as Number).toDouble(), (ev.clientY as Number).toDouble()) }
                    }) {
                        Div({ classes("login-bubble"); attr("aria-live", "polite") }) { Text(speech) }
                        Folha(mood, 150)
                    }
                    Div({ classes("login-card") }) {
                        H2 { Text("Entrar no Estudário") }
                        P({ classes("muted") }) { Text("Use a conta Google do app do celular. Se ainda não tem o app, pode começar por aqui.") }
                        if (GoogleSignIn.available) {
                            Div({ classes("gbtn-wrap"); if (busy) classes("busy") }) {
                                Div({ id("google-button"); classes("gbtn") })
                                if (busy) Div({ classes("gbtn-busy") }) { Spinner(); Span { Text("Entrando…") } }
                            }
                            LaunchedEffect(Unit) {
                                GoogleSignIn.renderButton("google-button", onToken = { token ->
                                    run { Auth.signInWithGoogle(token); Store.onSignedIn() }
                                })
                            }
                        } else Div({ classes("banner", "error") }) { Text("O login com Google não está configurado neste endereço.") }
                        error?.let { Div({ classes("banner", "error") }) { Icon("error"); Text(it) } }
                        Ul({ classes("login-trust") }) {
                            Li { Icon("Cloud"); Span { Text("Mesma conta do celular: o plano, as questões e o progresso aparecem aqui em segundos.") } }
                            Li { Icon("Shield"); Span { Text("Sem senha para decorar. Entra pelo Google e pronto.") } }
                        }
                        Div({ classes("login-sep") })
                        P({ classes("small", "muted") }) {
                            Text("Ainda não tem o app? ")
                            A(href = "https://play.google.com/store/apps/details?id=br.com.estudario", { attr("target", "_blank"); attr("rel", "noopener") }) { B { Text("Baixe grátis na Google Play") } }
                        }
                        P({ classes("xs", "muted") }) {
                            Text("Ao entrar você concorda com a ")
                            A(href = "https://estudario.com.br/privacidade.html", { attr("target", "_blank") }) { Text("política de privacidade") }
                            Text(".")
                        }
                    }
                }
            }
        }
        P({ classes("login-foot") }) { Text("psiu: clique num espaço vazio da página ;)") }
    }
}

@Composable
private fun Feature(icon: String, title: String, text: String) {
    Li({ classes("tu-hover") }) {
        Span({ classes("login-feat-ico") }) { Icon(icon) }
        Div { B { Text(title) }; Span { Text(text) } }
    }
}
