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
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.attributes.autoComplete
import org.jetbrains.compose.web.attributes.AutoComplete
import org.jetbrains.compose.web.attributes.placeholder
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Form
import org.jetbrains.compose.web.dom.H1
import org.jetbrains.compose.web.dom.H2
import org.jetbrains.compose.web.dom.Img
import org.jetbrains.compose.web.dom.Input
import org.jetbrains.compose.web.dom.Label
import org.jetbrains.compose.web.dom.Li
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.dom.Ul

@Composable
fun LoginScreen() {
    val scope = rememberCoroutineScope()
    var email by remember { mutableStateOf("") }
    var code by remember { mutableStateOf("") }
    var codeSent by remember { mutableStateOf(false) }
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
            Div({ classes("row") }) {
                Img(src = "icon.png", alt = "", attrs = { attr("width", "40"); attr("height", "40"); attr("style", "border-radius:10px") })
                Span({ classes("strong"); attr("style", "font-size:22px") }) { Text("estudário") }
            }
            Div {
                H1 { Text("Seu estudo, no rumo certo. Agora também no computador.") }
                Ul {
                    Li { Icon("calendar_month"); Text("Um plano que cabe na sua vida, do dia ao ano") }
                    Li { Icon("checklist"); Text("Seu edital organizado, com material pronto") }
                    Li { Icon("school"); Text("Questões, flashcards, revisões e caderno de erros") }
                    Li { Icon("devices"); Text("No computador e no celular, sempre sincronizado") }
                }
            }
            P({ classes("small"); attr("style", "opacity:.8") }) { Text("estudario.com.br") }
        }
        Div({ classes("auth-panel") }) {
            Div({ classes("auth-box") }) {
                H2 { Text("Entrar") }
                P({ classes("muted") }) { Text("Entre com sua conta Google. Se você já usa o app no celular, use a mesma conta para ver tudo sincronizado.") }
                if (GoogleSignIn.available) {
                    Div({ id("google-button"); attr("style", "min-height:44px;display:flex;justify-content:center") })
                    LaunchedEffect(Unit) {
                        GoogleSignIn.renderButton("google-button", onToken = { token ->
                            run { Auth.signInWithGoogle(token); Store.onSignedIn() }
                        })
                    }
                }
                error?.let { Div({ classes("banner", "error") }) { Text(it) } }
                Btn("Ver uma demonstração", { Store.startDemo() }, style = "outline", block = true, icon = "play_circle")
                P({ classes("small", "muted") }) {
                    Text("Ao entrar você concorda com a ")
                    org.jetbrains.compose.web.dom.A(href = "https://estudario.com.br/privacidade.html", { attr("target", "_blank") }) { Text("política de privacidade") }
                    Text(".")
                }
            }
        }
    }
}
