package br.com.estudario.web.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H2
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Text

/**
 * Apagar o material de um tópico (como o DeleteMaterialDialog do app): mostra o que sai e o que fica.
 * As questões não respondidas só saem se marcar; as respondidas nunca saem.
 */
@Composable
fun DeleteMaterialModal(topicTitle: String, theories: Int, summaries: Int, snippets: Int, unanswered: Int, answered: Int, onDismiss: () -> Unit, onConfirm: (Boolean) -> Unit) {
    var withQuestions by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(false) }
    Modal(onDismiss = onDismiss) {
        Div({ classes("delmat") }) {
            Div({ classes("delmat-ic") }) { Icon("delete") }
            H2 { Text("Apagar o material?") }
            P({ classes("muted", "small") }) { Text(topicTitle) }
            Div({ classes("delmat-box") }) {
                org.jetbrains.compose.web.dom.B({ classes("delmat-eyebrow") }) { Text("SAI DO TÓPICO") }
                if (theories > 0) Div({ classes("delmat-item") }) { Text("Teoria" + (if (theories > 1) " ($theories)" else "") + ", com seus grifos") }
                if (summaries > 0) Div({ classes("delmat-item") }) { Text("Resumos ($summaries)") }
                if (snippets > 0) Div({ classes("delmat-item") }) { Text("Flashcards, dicas e pegadinhas ($snippets)") }
                Div({ classes("delmat-item") }) { Text("Fontes consultadas") }
            }
            if (unanswered > 0) org.jetbrains.compose.web.dom.Label(attrs = { classes("delmat-check") }) {
                org.jetbrains.compose.web.dom.CheckboxInput(withQuestions) { onInput { withQuestions = it.value } }
                Text("Também apagar as $unanswered " + if (unanswered == 1) "questão que você ainda não respondeu" else "questões que você ainda não respondeu")
            }
            P({ classes("xs", "muted"); attr("style", "text-align:center") }) {
                Text("Continua tudo o que é seu: o tópico, se ele foi estudado, as revisões, o XP" + (if (answered > 0) " e as $answered questões que você já respondeu, com o histórico" else "") + ". Dá para gerar o material de novo quando quiser.")
            }
            Btn("Apagar material", { onConfirm(withQuestions) }, style = "danger", icon = "delete", block = true)
            Btn("Cancelar", onDismiss, style = "ghost", block = true)
        }
    }
}

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
