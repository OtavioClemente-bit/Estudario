package br.com.estudario.web.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import br.com.estudario.web.data.QuestionOption
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text

/**
 * Alternativas no padrão da revisão: clicar só escolhe (nada é gravado), o botão ao lado (ou o
 * clique direito) risca, e a resposta vale no "Confirmar" de quem chama. Depois de responder,
 * mostra certa/errada e, se [previousKey] vier, qual tinha sido marcada da última vez.
 */
@Composable
fun AnswerOptions(
    questionId: Long,
    options: List<QuestionOption>,
    picked: String?,
    answered: Boolean?,
    onPick: (String?) -> Unit,
    previousKey: String? = null,
) {
    val crossed = remember(questionId) { mutableStateMapOf<String, Boolean>() }
    Div({ classes("stack", "tight"); attr("style", "margin-top:12px") }) {
        options.sortedBy { it.position }.forEachIndexed { pos, option ->
            val state = when {
                answered != null && option.correct -> "right"
                answered == false && option.key == picked -> "wrong"
                answered == null && option.key == picked -> "chosen"
                else -> null
            }
            val isCrossed = crossed[option.key] == true && answered == null
            fun toggleCross() { crossed[option.key] = !isCrossed; if (picked == option.key) onPick(null) }
            Div({ classes("option-row") }) {
                Button({
                    classes(*listOfNotNull("option", state, if (isCrossed) "crossed" else null).toTypedArray())
                    if (answered != null) attr("disabled", "")
                    onClick { if (isCrossed) crossed[option.key] = false else onPick(option.key) }
                    addEventListener("contextmenu") { e -> if (answered == null) { e.preventDefault(); toggleCross() } }
                }) {
                    Span({ classes("key") }) { Text("ABCDEF"[pos].toString()) }
                    Span({ classes("grow") }) { Inline(option.text) }
                    if (answered != null && option.key == previousKey) Span({ classes("vr-was-tag") }) { Text("você tinha marcado esta da última vez") }
                }
                if (answered == null) Button({
                    classes("strike-btn", *listOfNotNull(if (isCrossed) "on" else null).toTypedArray())
                    attr("title", if (isCrossed) "Desfazer risco" else "Riscar alternativa")
                    onClick { toggleCross() }
                }) { Icon("strikethrough_s") }
            }
        }
    }
}

@Composable
fun ConfirmAnswer(enabled: Boolean, onConfirm: () -> Unit) {
    Div({ classes("rv-choice") }) { Btn("Confirmar", onConfirm, style = "primary", icon = "check", enabled = enabled) }
}
