package br.com.estudario.web.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
/** Limite do Grátis, usado enquanto o plano da conta carrega; o servidor é quem aplica o limite de verdade. */
private const val FREE_MAX_QUESTIONS = 10
/** Lote extra ("mais questões"): sempre até 10, em qualquer plano. */
private const val BATCH_QUESTIONS = 10

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
fun GenerateDialog(topicId: Long, questionsOnly: Boolean = false, onClose: () -> Unit) {

    var step by remember { mutableStateOf(0) }
    var blocks by remember { mutableStateOf(if (questionsOnly) setOf("QUESTIONS") else blockChoices.map { it.key }.toSet()) }
    var depth by remember { mutableStateOf("DEEP") }
    var maxQuestions by remember { mutableStateOf(FREE_MAX_QUESTIONS) }
    var count by remember { mutableStateOf(FREE_MAX_QUESTIONS) }
    // Uma consulta só: o saldo do mês (medidor no topo) e o teto de questões ao criar a matéria.
    var quota by remember { mutableStateOf<Quota?>(null) }
    LaunchedEffect(Unit) {
        quota = quotaFor(if (questionsOnly) "QUESTION_BATCH" else "CONTENT_GENERATION")
        if (!questionsOnly) quota?.maxPerRequest?.takeIf { it >= 1 }?.let { maxQuestions = it }
    }
    var upgradeSeen by remember { mutableStateOf(false) }
    var style by remember { mutableStateOf("MIXED") }
    var difficulty by remember { mutableStateOf("MEDIUM") }
    var board by remember { mutableStateOf("") }
    // A geração mora em Generation: fechar a janela não interrompe nada.
    val progress = Generation.state[topicId]
    val topic = Store.data.topics.firstOrNull { it.id == topicId }
    val hasQuestions = "QUESTIONS" in blocks
    val steps = listOfNotNull(if (questionsOnly) null else "blocks", if ("THEORY" in blocks || "SUMMARY" in blocks) "depth" else null, if (hasQuestions) "questions" else null, if (hasQuestions) "level" else null)
    val current = steps[step.coerceIn(0, steps.lastIndex)]

    fun start() {
        Generation.start(topicId, AiContent.Options(blocks = blocks, depth = depth, questionCount = count, style = style, difficulty = difficulty, board = board, avoid = if (questionsOnly) avoidFor(topicId) else emptyList()))
    }
    // Reaberta pela faixa do canto, a janela vem sem o pedido: o tipo fica guardado em Generation.
    val showQuestions = questionsOnly || Generation.questionsOnly[topicId] == true
    val title = if (questionsOnly) "Mais questões" else "Gerar material"
    val close = { if (progress is AiContent.Progress.Done || progress is AiContent.Progress.Failed) Generation.clear(topicId); onClose() }

    // Janela em três partes: cabeçalho e rodapé fixos, o meio rola. Larga, com respiro nas bordas.
    Modal(onDismiss = close, wide = true, extraClass = "gen-modal") {
        when (val p = progress) {
            null, is AiContent.Progress.Failed -> {
                Div({ classes("gen-head") }) {
                    Div({ classes("gen-head-row") }) {
                        Div({ classes("gen-head-title") }) {
                            Span({ classes("gen-eyebrow") }) { Text("Passo ${step + 1} de ${steps.size}") }
                            org.jetbrains.compose.web.dom.H2 { Text(title) }
                            topic?.title?.let { P({ classes("gen-topic") }) { Text(it) } }
                        }
                        IconButton("close", "Fechar", onClose)
                    }
                    Div({ classes("wizard-dots") }) { steps.indices.forEach { i -> Span({ classes(*listOfNotNull(if (i <= step) "on" else null).toTypedArray()) }) } }
                }
                Div({ classes("gen-body") }) {
                quota?.let { q ->
                    QuotaMeter(q, if (questionsOnly) "lotes de questões" else "materiais")
                    // Sem saldo, a oferta aparece logo, como no app (fechar volta para a janela).
                    if (q.limit > 0 && q.remaining == 0 && !upgradeSeen) UpgradeModal(if (questionsOnly) UpgradeReason.QUESTIONS else UpgradeReason.CONTENT) { upgradeSeen = true }
                }
                when (current) {
                    "blocks" -> {
                        FolhaSays("talking", 64) {
                            B { Text("O que você quer que eu prepare?") }
                            P({ classes("small", "muted") }) { Text(topic?.title?.let { "Para “$it”. Marque um ou mais." } ?: "Marque um ou mais.") }
                        }
                        ChoiceCards(blockChoices, { it in blocks }, cols = 3) { key -> blocks = if (key in blocks) blocks - key else blocks + key }
                    }
                    "depth" -> {
                        FolhaSays("thinking", 64) { B { Text("Quão profunda deve ser a teoria?") } }
                        ChoiceCards(depthChoices, { it == depth }, cols = 3) { depth = it }
                    }
                    "questions" -> {
                        FolhaSays("point", 64) {
                            B { Text("Quantas questões e em qual formato?") }
                            P({ classes("small", "muted") }) { Text(if (questionsOnly) "Cada lote traz até $BATCH_QUESTIONS questões novas." else "No seu plano, até $maxQuestions questões ao criar a matéria.") }
                        }
                        Div({ attr("style", "margin:6px 0 10px") }) {
                            val top = if (questionsOnly) BATCH_QUESTIONS else maxQuestions
                            BrandCounter(count, { count = it }, step = 1, min = 1, max = top, unit = "questões", chips = listOf(5, 10, 20, 30).filter { it <= top }, label = "Quantidade de questões")
                        }
                        ChoiceCards(styleChoices, { it == style }) { style = it }
                    }
                    "level" -> {
                        FolhaSays("wink", 64) { B { Text("Em que nível e no estilo de qual banca?") } }
                        ChoiceCards(difficultyChoices, { it == difficulty }, cols = 4) { difficulty = it }
                        Div({ classes("field") }) {
                            Label { Text("Banca (opcional)") }
                            Input(InputType.Text) {
                                classes("input"); value(board); attr("placeholder", "Ex.: Cebraspe, FGV, FCC"); attr("maxlength", "40")
                                onInput { board = it.value }
                            }
                            Span({ classes("xs", "muted") }) { Text("Em branco, as questões seguem o estilo das provas anteriores deste concurso.") }
                        }
                    }
                }
                if (p is AiContent.Progress.Failed) {
                    Div({ classes("banner", "error") }) { Icon("error"); Text(p.message) }
                    var upgradeSeen by remember(p) { mutableStateOf(false) }
                    if (isQuotaMessage(p.message) && !upgradeSeen) UpgradeModal(if (questionsOnly) UpgradeReason.QUESTIONS else UpgradeReason.CONTENT) { upgradeSeen = true }
                }
                }
                Div({ classes("gen-foot") }) {
                    if (step > 0) Btn("Voltar", { step-- }, style = "ghost", icon = "arrow_back") else Btn("Cancelar", onClose, style = "ghost")
                    if (step < steps.lastIndex) Btn("Continuar", { step++ }, style = "primary", enabled = blocks.isNotEmpty())
                    else Btn(if (questionsOnly) "Gerar questões" else "Gerar material", { start() }, style = "primary", icon = "auto_awesome", enabled = blocks.isNotEmpty())
                }
            }
            is AiContent.Progress.Done -> Div({ classes("gen-body", "gen-done", "stack", "pop-in") }) {
                // "gen-done-folha", não "celebrate": esse nome é o da tela cheia das comemorações e cobria tudo.
                Div({ classes("gen-done-folha") }) { Folha("happy", 140) }
                org.jetbrains.compose.web.dom.H2 { Text("Prontinho!") }
                P({ classes("muted") }) { Text(if (showQuestions) "As novas questões já estão em “${topic?.title ?: "neste tópico"}”, prontas para treinar." else "Seu material está salvo em “${topic?.title ?: "neste tópico"}”: teoria, flashcards e questões esperando por você.") }
                val asked = Generation.requested[topicId] ?: 0
                val got = Generation.delivered[topicId] ?: 0
                if (got in 1 until asked) P({ classes("small", "muted") }) {
                    Text("Este tópico ainda não tinha questões prontas: vieram $got inéditas, escritas agora. Quando terminar, peça mais em lotes de 10.")
                }
                if (showQuestions) Btn("Treinar agora", { Generation.clear(topicId); onClose(); Router.go(Route.Quiz("topico-$topicId-10")) }, style = "primary", size = "lg", icon = "play_arrow")
                else Btn("Ver material", { Generation.clear(topicId); onClose(); Router.go(Route.Topic(topicId)) }, style = "primary", size = "lg", icon = "menu_book")
            }
            else -> Div({ classes("gen-body", "gen-working") }) {
                ProcessView(
                    title = if (showQuestions) "Preparando as suas questões" else "Preparando o seu material",
                    eyebrow = topic?.title?.take(60),
                    stages = if (showQuestions) listOf("Conferindo a segurança", "Enviando o pedido", "Separando as que você ainda não viu", "Buscando questões no estilo da banca", "Conferindo gabaritos e explicações")
                    else listOf("Conferindo a segurança", "Enviando o pedido", "Lendo o tópico e a banca", "Escrevendo a teoria", "Montando flashcards e questões", "Revisando tudo"),
                    stageIndex = when (p) {
                        AiContent.Progress.Checking -> 0
                        AiContent.Progress.Sending -> 1
                        else -> null
                    },
                    stageMillis = 9_000,
                    stageOffset = 2,
                    sceneSize = 280,
                    scene = if (showQuestions) "questions" else "material",
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

/** Enunciados das questões que a pessoa já tem neste tópico (as mais recentes primeiro), para não repetir. */
private fun avoidFor(topicId: Long): List<String> =
    Store.data.questions.filter { it.topicId == topicId }.asReversed().map { it.statement }.filter { it.isNotBlank() }.take(400)

/** O saldo do mês em uma faixa: quantos restam, a barra e quando renova. */
@Composable
private fun QuotaMeter(q: Quota, what: String) {
    if (q.limit <= 0) return
    val empty = q.remaining == 0
    Div({ classes(*listOfNotNull("quota-meter", if (empty) "empty" else null).toTypedArray()) }) {
        Div({ classes("qm-top") }) {
            Span({ classes("qm-num") }) { B { Text(q.remaining.toString()) }; Span { Text(" de ${q.limit}") } }
            Span({ classes("qm-label") }) {
                Text(when {
                    empty -> "Seus $what acabaram" + (q.resetLabel?.let { ". Renova em $it" } ?: "")
                    q.monthly -> "$what restantes neste mês" + (q.resetLabel?.let { " · renova em $it" } ?: "")
                    else -> "$what restantes"
                })
            }
        }
        Div({ classes("qm-bar") }) { Span({ attr("style", "width:${(q.remaining * 100 / q.limit).coerceIn(0, 100)}%") }) }
    }
}
