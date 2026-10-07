package br.com.estudario.web.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import br.com.estudario.web.GoogleSignIn
import br.com.estudario.web.data.Auth
import br.com.estudario.web.data.Store
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.dom.A
import org.jetbrains.compose.web.dom.B
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H1
import org.jetbrains.compose.web.dom.H2
import org.jetbrains.compose.web.dom.Img
import org.jetbrains.compose.web.dom.Li
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.dom.Ul

/**
 * Entrada do site: à esquerda, a marca com o Folha acenando e o que o Estudário faz; à direita, um
 * cartão só, com o botão do Google. Sem demonstração: quem chega aqui já quer o próprio estudo
 * (a demonstração continua em app.estudario.com.br/?demo, para quem tiver o link).
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

    Div({ classes("auth") }) {
        Div({ classes("auth-art") }) {
            Div({ classes("auth-deco"); attr("aria-hidden", "true") }) {
                repeat(6) { i -> Span({ classes("sheet", "s$i") }) { Span(); Span(); Span() } }
            }
            A(href = "https://estudario.com.br", { classes("auth-brand") }) {
                Img(src = "icon.png", alt = "")
                Span { Text("estudário") }
            }
            Div({ classes("auth-hero") }) {
                Div({ classes("auth-folha") }) {
                    Folha("wave", 168)
                    Div({ classes("auth-bubble") }) { Text("Oi! Eu sou o Folha. Bora estudar no computador?") }
                }
                H1 { Text("Seu estudo, no rumo certo. Agora também no computador.") }
                Ul({ classes("auth-features") }) {
                    Feature("calendar_month", "Um plano que cabe na sua vida", "Do dia ao ano, e se ajusta quando você atrasa")
                    Feature("checklist", "Seu edital organizado", "Cada tópico com teoria, flashcards e questões")
                    Feature("school", "Treino de verdade", "Questões, revisões espaçadas e caderno de erros")
                    Feature("devices", "Computador e celular juntos", "Tudo sincronizado com o app, em segundos")
                }
            }
            P({ classes("auth-foot") }) { Text("estudario.com.br") }
        }
        Div({ classes("auth-panel") }) {
            Div({ classes("auth-card", "rise") }) {
                Img(src = "icon.png", alt = "", attrs = { classes("auth-icon") })
                H2 { Text("Entrar no Estudário") }
                P({ classes("muted") }) { Text("Use sua conta Google. Se você já usa o app no celular, entre com a mesma conta para ver tudo sincronizado.") }
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
                Ul({ classes("auth-trust") }) {
                    Li { Icon("shield"); Text("Seus dados ficam na sua conta. Só você tem acesso.") }
                    Li { Icon("cloud_sync"); Text("O que você faz aqui aparece no app, e vice-versa.") }
                }
                Div({ classes("auth-sep") })
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

@Composable
private fun Feature(icon: String, title: String, text: String) {
    Li {
        Span({ classes("feat-ico") }) { Icon(icon) }
        Div { B { Text(title) }; Span { Text(text) } }
    }
}
