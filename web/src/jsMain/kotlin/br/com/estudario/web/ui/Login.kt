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
            "wave" to "Oi! Eu sou o Folha. Bora estudar no computador?",
            "talking" to "É a mesma conta do celular: entra com o Google e pronto.",
            "thinking" to "Seu plano e suas questões já estão te esperando…",
            "happy" to "Tela grande, sumário do lado e grifo com o mouse!",
        )
    }
    var line by remember { mutableStateOf(0) }
    LaunchedEffect(Unit) { while (true) { delay(4600); if (!busy) line = (line + 1) % lines.size } }
    val (mood, speech) = when {
        busy -> "thinking" to "Abrindo o seu caderno…"
        error != null -> "sad" to "Ih, não deu. Tenta de novo?"
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
                Span({ classes("login-chip") }) { Icon("Laptop"); Text("O Estudário no computador") }
                H1 { Text("Seu estudo, no rumo certo. "); Span({ classes("login-mark") }) { Text("Agora na tela grande.") } }
                P({ classes("login-lead") }) { Text("O mesmo plano, edital, questões e revisões do app do celular, com espaço para ler, grifar e treinar pelo teclado.") }
                Ul({ classes("login-feats") }) {
                    Feature("Calendar", "Um plano que cabe na sua vida", "Do dia ao ano, e se ajusta quando você atrasa")
                    Feature("Clipboard", "Seu edital organizado", "Cada tópico com teoria, flashcards e questões")
                    Feature("Target", "Treino de verdade", "Questões, revisões espaçadas e caderno de erros")
                    Feature("Cycle", "Computador e celular juntos", "Tudo sincronizado com o app, em segundos")
                }
            }
            Div({ classes("login-right") }) {
                Div({ classes("login-stage") }) {
                    // A turma em volta do cartão: cada adesivo numa camada (--d) que segue o mouse.
                    listOf("Star" to 22, "Fire" to -18, "Bulb" to 14, "Trophy" to -24, "Books" to 12, "Check" to -10).forEachIndexed { i, (g, d) ->
                        Span({ classes("lsticker", "ls$i"); attr("style", "--d:$d"); attr("aria-hidden", "true") }) { Span { Icon(g) } }
                    }
                    Div({ classes("login-folha") }) {
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
        P({ classes("login-foot") }) { Text("psiu: clique no papel em branco ;)") }
    }
}

@Composable
private fun Feature(icon: String, title: String, text: String) {
    Li({ classes("tu-hover") }) {
        Span({ classes("login-feat-ico") }) { Icon(icon) }
        Div { B { Text(title) }; Span { Text(text) } }
    }
}
