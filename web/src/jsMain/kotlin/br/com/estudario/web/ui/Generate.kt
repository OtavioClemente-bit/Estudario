package br.com.estudario.web.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import br.com.estudario.web.data.AiContent
import br.com.estudario.web.data.Store
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.dom.B
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Input
import org.jetbrains.compose.web.dom.Label
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.attributes.InputType

private data class Choice(val key: String, val title: String, val hint: String, val icon: String? = null)

private val blockChoices = listOf(
    Choice("THEORY", "Teoria", "Explicação completa, em capítulos", "menu_book"),
    Choice("SUMMARY", "Resumo completo", "O essencial em poucas páginas", "summarize"),
    Choice("QUICK_REVIEW", "Flashcards", "Baralho para estudar por repetição", "bolt"),
    Choice("TIPS_TRAPS", "Dicas e pegadinhas", "Dicas e pegadinhas de banca", "lightbulb"),
    Choice("ACTIVE_RECALL", "Perguntas de memorização", "Perguntas para testar a memória", "psychology"),
    Choice("QUESTIONS", "Questões", "Questões de treino com gabarito comentado", "quiz"),
    Choice("ERROR_CONCEPTS", "Conceitos que geram erro", "Onde a maioria erra e por quê", "report_problem"),
)
private val depthChoices = listOf(
    Choice("ESSENTIAL", "Essencial", "Direto ao ponto"),
    Choice("DEEP", "Aprofundada", "Com exemplos e exceções"),
    Choice("BOOK", "Livro completo", "O mais completo possível"),
)
private val styleChoices = listOf(
    Choice("MIXED", "Misturado", "Um pouco de múltipla escolha e um pouco de Certo ou Errado"),
    Choice("FIVE_OPTIONS", "Múltipla escolha (A a E)", "Cinco alternativas, uma correta. O formato mais comum (FGV, FCC, Vunesp)"),
    Choice("FOUR_OPTIONS", "Múltipla escolha (A a D)", "Quatro alternativas, uma correta"),
    Choice("TRUE_FALSE", "Certo ou Errado (C/E)", "Você julga se a afirmação está certa ou errada. É o estilo do Cebraspe"),
)
private val difficultyChoices = listOf(
    Choice("EASY", "Fácil", "Fixar o básico"),
    Choice("MEDIUM", "Média", "Nível da maioria das provas"),
    Choice("HARD", "Difícil", "Pegadinhas e detalhes"),
    Choice("MIXED", "Misturada", "Um pouco de cada"),
)

/** Máximo de questões por pedido no plano grátis (GenerationLimits do app). */
private const val MAX_QUESTIONS = 10

@Composable
private fun ChoiceCards(items: List<Choice>, isOn: (String) -> Boolean, cols: Int = 2, onToggle: (String) -> Unit) {
    Div({ classes("choices", "cols-$cols") }) {
        items.forEach { item ->
            key(item.key) {
                val on = isOn(item.key)
                Button({ classes(*listOfNotNull("choice", if (on) "on" else null).toTypedArray()); attr("aria-pressed", "$on"); onClick { onToggle(item.key) } }) {
                    item.icon?.let { Icon(it) }
                    Span({ classes("grow") }) { B { Text(item.title) }; Span({ classes("hint") }) { Text(item.hint) } }
                    Icon(if (on) "check_circle" else "radio_button_unchecked", filled = on, extraClass = "tick")
                }
            }
        }
    }
}

/** Gerar material com o Estudário, em passos com o Folha perguntando, como no app. */
@Composable
fun GenerateDialog(topicId: Long, onClose: () -> Unit) {
    val scope = rememberCoroutineScope()
    var step by remember { mutableStateOf(0) }
    var blocks by remember { mutableStateOf(blockChoices.map { it.key }.toSet()) }
    var depth by remember { mutableStateOf("DEEP") }
    var count by remember { mutableStateOf(MAX_QUESTIONS) }
    var style by remember { mutableStateOf("MIXED") }
    var difficulty by remember { mutableStateOf("MEDIUM") }
    var board by remember { mutableStateOf("") }
    var progress by remember { mutableStateOf<AiContent.Progress?>(null) }
    val running = progress != null && progress !is AiContent.Progress.Failed && progress !is AiContent.Progress.Done
    val topic = Store.data.topics.firstOrNull { it.id == topicId }
    val hasQuestions = "QUESTIONS" in blocks
    val steps = listOfNotNull("blocks", if ("THEORY" in blocks || "SUMMARY" in blocks) "depth" else null, if (hasQuestions) "questions" else null, if (hasQuestions) "level" else null)
    val current = steps[step.coerceIn(0, steps.lastIndex)]

    fun start() {
        progress = AiContent.Progress.Checking
        scope.launch {
            val options = AiContent.Options(blocks = blocks, depth = depth, questionCount = count, style = style, difficulty = difficulty, board = board)
            val result = AiContent.generate(Store.data, topicId, options) { progress = it }
            if (result is AiContent.Progress.Done) Store.update { AiContent.apply(it, topicId, result.proposal) }
            progress = result
        }
    }

    Modal(onDismiss = { if (!running) onClose() }) {
        when (val p = progress) {
            null, is AiContent.Progress.Failed -> {
                Div({ classes("row", "between") }) {
                    Span({ classes("xs", "muted") }) { Text("Gerar material · passo ${step + 1} de ${steps.size}") }
                    IconButton("close", "Fechar", onClose)
                }
                Div({ classes("wizard-dots") }) { steps.indices.forEach { i -> Span({ classes(*listOfNotNull(if (i <= step) "on" else null).toTypedArray()) }) } }
                when (current) {
                    "blocks" -> {
                        FolhaSays("talking", 64) {
                            B { Text("O que você quer que eu prepare?") }
                            P({ classes("small", "muted") }) { Text(topic?.title?.let { "Para “$it”. Marque um ou mais." } ?: "Marque um ou mais.") }
                        }
                        ChoiceCards(blockChoices, { it in blocks }) { key -> blocks = if (key in blocks) blocks - key else blocks + key }
                    }
                    "depth" -> {
                        FolhaSays("thinking", 64) { B { Text("Quão profunda deve ser a teoria?") } }
                        ChoiceCards(depthChoices, { it == depth }, cols = 3) { depth = it }
                    }
                    "questions" -> {
                        FolhaSays("point", 64) {
                            B { Text("Quantas questões e em qual formato?") }
                            P({ classes("small", "muted") }) { Text("No plano grátis, até $MAX_QUESTIONS questões por pedido.") }
                        }
                        Div({ classes("day-row") }) {
                            Div({ classes("grow") }) { Div({ classes("name") }) { Text("Quantidade") }; Div({ classes("hint") }) { Text("$count questões") } }
                            Stepper(count, { count = it }, min = 1, max = MAX_QUESTIONS, label = "Quantidade de questões")
                        }
                        ChoiceCards(styleChoices, { it == style }) { style = it }
                    }
                    "level" -> {
                        FolhaSays("wink", 64) { B { Text("Em que nível e no estilo de qual banca?") } }
                        ChoiceCards(difficultyChoices, { it == difficulty }, cols = 4) { difficulty = it }
                        Div({ classes("field") }) {
                            Label { Text("Banca (opcional)") }
                            Input(InputType.Text) {
                                classes("input"); value(board); attr("placeholder", "Ex.: Cebraspe, FGV, FCC"); attr("maxlength", "80")
                                onInput { board = it.value }
                            }
                            Span({ classes("xs", "muted") }) { Text("Em branco, as questões seguem o estilo das provas anteriores deste concurso.") }
                        }
                    }
                }
                if (p is AiContent.Progress.Failed) Div({ classes("banner", "error") }) { Icon("error"); Text(p.message) }
                Div({ classes("row", "between"); attr("style", "margin-top:4px") }) {
                    if (step > 0) Btn("Voltar", { step-- }, style = "ghost", icon = "arrow_back") else Btn("Cancelar", onClose, style = "ghost")
                    if (step < steps.lastIndex) Btn("Continuar", { step++ }, style = "primary", enabled = blocks.isNotEmpty())
                    else Btn("Gerar material", { start() }, style = "primary", icon = "auto_awesome", enabled = blocks.isNotEmpty())
                }
            }
            is AiContent.Progress.Done -> Div({ classes("stack"); attr("style", "align-items:center;text-align:center;padding:8px 0") }) {
                Folha("happy", 110)
                B { Text("Prontinho! Seu material está salvo neste tópico.") }
                Btn("Ver material", onClose, style = "primary")
            }
            else -> Div({ classes("stack"); attr("style", "align-items:center;text-align:center;padding:8px 0") }) {
                Folha("thinking", 110, "animation:bob 1.6s ease-in-out infinite")
                B {
                    Text(
                        when (p) {
                            AiContent.Progress.Checking -> "Verificando a segurança…"
                            AiContent.Progress.Sending -> "Enviando o pedido…"
                            else -> "Estou escrevendo o seu material…"
                        },
                    )
                }
                P({ classes("small", "muted") }) { Text("Pode levar alguns minutos. Deixe esta página aberta.") }
                Spinner()
            }
        }
    }
}
