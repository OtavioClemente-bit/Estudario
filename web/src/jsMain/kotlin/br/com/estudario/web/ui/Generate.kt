package br.com.estudario.web.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import br.com.estudario.web.Route
import br.com.estudario.web.Router
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

    var step by remember { mutableStateOf(0) }
    var blocks by remember { mutableStateOf(blockChoices.map { it.key }.toSet()) }
    var depth by remember { mutableStateOf("DEEP") }
    var count by remember { mutableStateOf(MAX_QUESTIONS) }
    var style by remember { mutableStateOf("MIXED") }
    var difficulty by remember { mutableStateOf("MEDIUM") }
    var board by remember { mutableStateOf("") }
    // A geração mora em Generation: fechar a janela não interrompe nada.
    val progress = Generation.state[topicId]
    val topic = Store.data.topics.firstOrNull { it.id == topicId }
    val hasQuestions = "QUESTIONS" in blocks
    val steps = listOfNotNull("blocks", if ("THEORY" in blocks || "SUMMARY" in blocks) "depth" else null, if (hasQuestions) "questions" else null, if (hasQuestions) "level" else null)
    val current = steps[step.coerceIn(0, steps.lastIndex)]

    fun start() {
        Generation.start(topicId, AiContent.Options(blocks = blocks, depth = depth, questionCount = count, style = style, difficulty = difficulty, board = board))
    }
    val close = { if (progress is AiContent.Progress.Done || progress is AiContent.Progress.Failed) Generation.clear(topicId); onClose() }

    Modal(onDismiss = close, wide = progress != null && progress !is AiContent.Progress.Failed) {
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
            is AiContent.Progress.Done -> Div({ classes("stack", "pop-in"); attr("style", "align-items:center;text-align:center;padding:8px 0") }) {
                Div({ classes("celebrate") }) { Folha("happy", 140) }
                org.jetbrains.compose.web.dom.H2 { Text("Prontinho!") }
                P({ classes("muted") }) { Text("Seu material está salvo em “${topic?.title ?: "neste tópico"}”: teoria, flashcards e questões esperando por você.") }
                Btn("Ver material", { Generation.clear(topicId); onClose(); Router.go(Route.Topic(topicId)) }, style = "primary", size = "lg", icon = "menu_book")
            }
            else -> {
                ProcessView(
                    title = "Preparando o seu material",
                    eyebrow = topic?.title?.take(60),
                    stages = listOf("Conferindo a segurança", "Enviando o pedido", "Lendo o tópico e a banca", "Escrevendo a teoria", "Montando flashcards e questões", "Revisando tudo"),
                    stageIndex = when (p) {
                        AiContent.Progress.Checking -> 0
                        AiContent.Progress.Sending -> 1
                        else -> null
                    },
                    stageMillis = 9_000,
                    stageOffset = 2,
                    sceneSize = 280,
                    footer = {
                        P({ classes("small", "muted"); attr("style", "text-align:center") }) {
                            Text("Pode fechar esta janela e continuar estudando. Eu continuo aqui e te aviso quando ficar pronto.")
                        }
                        Div({ classes("row", "end"); attr("style", "width:100%") }) { Btn("Continuar navegando", onClose, style = "outline", icon = "arrow_forward") }
                    },
                )
            }
        }
    }
}
