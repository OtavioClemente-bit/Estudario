package br.com.estudario.web.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import br.com.estudario.web.data.Prefs
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.attributes.maxLength
import org.jetbrains.compose.web.dom.B
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H2
import org.jetbrains.compose.web.dom.Input
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text

/** A janela de trocar o nome (a mesma do app, NameDialog.kt): avatar, Folha, campo, contador e prévia. */
object NameEdit {
    var open by mutableStateOf(false)
}

/** O lápis "Editar nome" (no cartão do menu e no perfil). Dentro de um link, não abre o link. */
@Composable
fun EditNameButton(extraClass: String = "") {
    Span({
        classes(*listOf("name-edit-btn", extraClass).filter { it.isNotBlank() }.toTypedArray())
        attr("role", "button"); attr("tabindex", "0")
        attr("aria-label", "Editar nome"); attr("title", "Editar nome")
        onClick { it.preventDefault(); it.stopPropagation(); NameEdit.open = true }
        onKeyDown { if (it.key == "Enter" || it.key == " ") { it.preventDefault(); it.stopPropagation(); NameEdit.open = true } }
    }) { Icon("edit") }
}

@Composable
fun NameEditHost() {
    if (!NameEdit.open) return
    val close = { NameEdit.open = false }
    var text by remember { mutableStateOf(Prefs.userName().orEmpty().take(Prefs.NAME_MAX)) }
    val clean = Prefs.cleanName(text)
    val valid = clean.isNotEmpty()
    val save = { if (valid) { Prefs.setUserName(clean); close(); Toast.show("Pronto! Agora o Folha te chama de ${clean.substringBefore(' ')}.") } }
    androidx.compose.runtime.LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(40)
        (kotlinx.browser.document.querySelector(".name-input") as? org.w3c.dom.HTMLInputElement)?.let { it.focus(); it.select() }
    }
    Modal(onDismiss = close, extraClass = "name-modal") {
        Button({ classes("gx-x", "name-x"); attr("type", "button"); attr("aria-label", "Fechar"); onClick { close() } }) { Icon("close") }
        Div({ classes("name-hero") }) {
            Span({ classes("avatar", "name-avatar") }) { AvatarContent() }
            Span({ classes("name-folha") }) { Folha(if (valid) "happy" else "idle", 76) }
        }
        H2({ classes("name-title") }) { Text("Como você quer ser chamado?") }
        Input(InputType.Text) {
            classes("name-input")
            value(text)
            maxLength(Prefs.NAME_MAX)
            attr("placeholder", "Seu nome")
            attr("aria-label", "Seu nome")
            attr("autocomplete", "nickname")
            onInput { text = it.value.take(Prefs.NAME_MAX) }
            onKeyDown { if (it.key == "Enter") { it.preventDefault(); save() } }
        }
        Div({ classes("name-help") }) {
            Span({ if (text.isNotEmpty() && !valid) classes("name-error") }) { Text(if (valid || text.isEmpty()) "Pode ser apelido." else "Escreva pelo menos uma letra.") }
            Span({ classes("name-count") }) { Text("${text.length}/${Prefs.NAME_MAX}") }
        }
        Div({ classes("name-preview") }) { B { Text(if (valid) "Bora estudar, ${clean.substringBefore(' ')}!" else "Bora estudar!") } }
        Div({ classes("name-actions") }) {
            Button({ classes("btn", "outline"); attr("type", "button"); onClick { close() } }) { Text("Cancelar") }
            Button({ classes("btn", "primary"); attr("type", "button"); if (!valid) attr("disabled", ""); onClick { save() } }) { Text("Salvar") }
        }
    }
}
