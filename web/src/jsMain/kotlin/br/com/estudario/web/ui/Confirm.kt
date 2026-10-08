package br.com.estudario.web.ui

import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H2
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Text

/** Confirmação de exclusão: o que sai, e que não dá para desfazer. */
@Composable
fun ConfirmDeleteModal(title: String, body: String, confirmLabel: String = "Excluir", onDismiss: () -> Unit, onConfirm: () -> Unit) {
    Modal(onDismiss = onDismiss) {
        H2 { Text(title) }
        P({ classes("muted") }) { Text(body) }
        Div({ classes("row", "end"); attr("style", "margin-top:8px") }) {
            Btn("Cancelar", onDismiss, style = "ghost")
            Btn(confirmLabel, onConfirm, style = "danger", icon = "delete")
        }
    }
}
