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

    var blocks by remember { mutableStateOf(if (questionsOnly) setOf("QUESTIONS") else blockChoices.map { it.key }.toSet()) }
    var depth by remember { mutableStateOf("DEEP") }
    var maxQuestions by remember { mutableStateOf(FREE_MAX_QUESTIONS) }
    var count by remember { mutableStateOf(FREE_MAX_QUESTIONS) }
    // Uma consulta só: o saldo do mês (medidor no topo) e o teto de questões ao criar a matéria.
    var quota by remember { mutableStateOf<Quota?>(null) }
    var free by remember { mutableStateOf<FreeInfo?>(null) }
    // O saldo é conferido assim que a janela abre, antes do primeiro passo (ver o bloqueio abaixo).
    var checked by remember { mutableStateOf(false) }
    var today by remember { mutableStateOf<Pair<Int, Int>?>(null) }
    // Pagos: passou das matérias do plano livres de hoje e a pessoa escolheu seguir (gasta 1 do mês).
    var overDailyOk by remember { mutableStateOf(false) }
    var style by remember { mutableStateOf("MIXED") }
    var difficulty by remember { mutableStateOf("MEDIUM") }
    var board by remember { mutableStateOf("") }
    // As escolhas de nível e formato só aparecem com o plano pago confirmado pelo servidor: sem
    // resposta (rede ruim, demonstração), fica como no Grátis, misturado.
    var paid by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        free = freeAllowance()
        paid = free == null && !br.com.estudario.web.data.LibraryGaps.free() && planTierOf()?.let { it != "FREE" } == true
        if (!paid) { style = "MIXED"; difficulty = "MIXED" }
        // Matéria sem questões ainda: a banca vem do edital (perfil da prova ou catálogo), como no app.
        if (board.isBlank()) {
            val competition = Store.data.topics.firstOrNull { it.id == topicId }?.let { t -> Store.data.subjects.firstOrNull { it.id == t.subjectId } }
                ?.let { s -> Store.data.competitions.firstOrNull { it.id == s.competitionId } }
            if (competition != null) {
                val fromProfile = br.com.estudario.web.data.Simulations.examProfile(Store.data, competition.id).board?.trim()?.ifBlank { null }
                (fromProfile ?: br.com.estudario.web.data.Catalog.boardFor(competition.name))?.let { if (board.isBlank()) board = it.take(40) }
            }
        }
        quota = quotaFor(if (questionsOnly) "QUESTION_BATCH" else "CONTENT_GENERATION")
        if (!questionsOnly && free == null) today = planToday()
        checked = true
        if (!questionsOnly) quota?.maxPerRequest?.takeIf { it >= 1 }?.let { maxQuestions = it }
    }
    var upgradeSeen by remember { mutableStateOf(false) }
    // Grátis: questões sempre misturadas (o servidor força o mesmo); escolher é do Essencial e do Pro.
    val freeTier = !paid
    // A geração mora em Generation: fechar a janela não interrompe nada.
    val progress = Generation.state[topicId]
    val topic = Store.data.topics.firstOrNull { it.id == topicId }
    val hasQuestions = "QUESTIONS" in blocks

    fun start() {
        Generation.start(topicId, AiContent.Options(blocks = blocks, depth = depth, questionCount = count, style = style, difficulty = difficulty, board = board, avoid = if (questionsOnly) avoidFor(topicId) else emptyList(), purpose = AiContent.purposeFor(Store.data, topicId)))
    }
    // Reaberta pela faixa do canto, a janela vem sem o pedido: o tipo fica guardado em Generation.
    val showQuestions = questionsOnly || Generation.questionsOnly[topicId] == true
    val title = if (questionsOnly) "Mais questões" else "Gerar material"
    val close = { if (progress is AiContent.Progress.Done || progress is AiContent.Progress.Failed) Generation.clear(topicId); onClose() }

    // Grátis: só as matérias do plano (as de hoje e as que adianta). Fora dele, nem o assistente
    // abre: aparece a oferta dos planos. O servidor confere de novo (FREE_EXTRA_LIMIT).
    if (!questionsOnly && progress == null && br.com.estudario.web.data.LibraryGaps.free() && AiContent.purposeFor(Store.data, topicId) == "EXTRA") {
        UpgradeModal(UpgradeReason.EXTRAS) { onClose() }
        return
    }
    // Saldo conferido antes do assistente: sem saldo, a oferta aparece no lugar dele, e não no fim
    // dos passos. O servidor confere de novo ao criar o pedido.
    if (progress == null) {
        if (!checked) {
            Modal(onDismiss = onClose) { CenterMessage { Folha("thinking", 64); Text("Conferindo o seu saldo…") } }
            return
        }
        val f = free
        val q = quota
        val blocked = when {
            questionsOnly -> UpgradeReason.QUESTIONS.takeIf { q != null && q.limit > 0 && q.remaining == 0 }
            f != null -> when (AiContent.purposeFor(Store.data, topicId)) {
                "PLAN_DAY" -> UpgradeReason.PLAN_DAY.takeIf { f.planDayUsed >= f.planDayLimit }
                "ADVANCE" -> UpgradeReason.ADVANCE.takeIf { f.weeklyUsed >= f.weeklyLimit }
                else -> UpgradeReason.EXTRAS
            }
            else -> {
                val planPurpose = AiContent.purposeFor(Store.data, topicId).let { it == "PLAN_DAY" || it == "ADVANCE" }
                val overToday = today?.let { (used, limit) -> used >= limit } == true
                val monthOver = q != null && q.limit > 0 && q.remaining == 0
                // Matéria do plano dentro das do dia sai mesmo sem cota no mês (se estiver pronta).
                UpgradeReason.CONTENT.takeIf { monthOver && (!planPurpose || overToday) }
            }
        }
        if (blocked != null) {
            UpgradeModal(blocked) { onClose() }
            return
        }
        val overToday = f == null && !questionsOnly && today?.let { (used, limit) -> used >= limit } == true &&
            AiContent.purposeFor(Store.data, topicId).let { it == "PLAN_DAY" || it == "ADVANCE" }
        if (overToday && !overDailyOk) {
            Modal(onDismiss = onClose) {
                FolhaSays("thinking", 80) {
                    B { Text("Respira um pouco!") }
                    P { Text("Você já estudou ${today!!.second} matérias novas hoje. Quantidade não vence qualidade: o que fica é o que você revisa e treina com questões. Que tal fechar o dia revisando?") }
                    P({ classes("small", "strong") }) { Text("Se quiser seguir, esta matéria usa 1 das ${q?.remaining ?: 0} que ainda restam no seu mês.") }
                }
                Div({ classes("row", "end", "wrap"); attr("style", "gap:8px;margin-top:12px") }) {
                    Btn("Seguir mesmo assim (usa 1 do mês)", { overDailyOk = true }, style = "ghost")
                    Btn("Deixar para amanhã", onClose, style = "primary")
                }
            }
            return
        }
    }

    // Janela em três partes: cabeçalho e rodapé fixos, o meio rola. Larga, com respiro nas bordas.
    Modal(onDismiss = close, wide = true, extraClass = "gen-modal") {
        when (val p = progress) {
            null, is AiContent.Progress.Failed -> {
                // O pedido numa tela só: as partes ficam à vista e se ajustam em qualquer ordem; o
                // rodapé fixo resume o pedido e gera (antes eram passos com "Continuar").
                Div({ classes("gen-head") }) {
                    Div({ classes("gen-head-row") }) {
                        Folha("talking", 48, live = false)
                        Div({ classes("gen-head-title") }) {
                            org.jetbrains.compose.web.dom.H2 { Text(title) }
                            topic?.title?.let { P({ classes("gen-topic") }) { Text(it) } }
                        }
                        IconButton("close", "Fechar", onClose)
                    }
                }
                Div({ classes("gen-body") }) {
                    free?.takeIf { !questionsOnly }?.let { f -> FreeMeter(f, AiContent.purposeFor(Store.data, topicId)) }
                    quota?.takeIf { free == null || questionsOnly }?.let { q ->
                        QuotaMeter(q, if (questionsOnly) "lotes de questões" else "materiais")
                        if (q.limit > 0 && q.remaining == 0 && !upgradeSeen) UpgradeModal(if (questionsOnly) UpgradeReason.QUESTIONS else UpgradeReason.CONTENT) { upgradeSeen = true }
                    }
                    var n = 0
                    if (!questionsOnly) GenSection(++n, "O que preparar", "Marque um ou mais", done = blocks.isNotEmpty()) {
                        Div({ classes("gen-presets") }) {
                            listOf(
                                "Pacote completo" to blockChoices.map { it.key }.toSet(),
                                "Teoria e resumo" to setOf("THEORY", "SUMMARY"),
                                "Revisão rápida" to setOf("QUICK_REVIEW", "TIPS_TRAPS", "ACTIVE_RECALL"),
                                "Só questões" to setOf("QUESTIONS"),
                            ).forEach { (label, set) ->
                                Button({ classes(*listOfNotNull("gen-preset", if (blocks == set) "on" else null).toTypedArray()); onClick { blocks = set } }) { Text(label) }
                            }
                        }
                        ChoiceCards(blockChoices, { it in blocks }, cols = 3) { key -> blocks = if (key in blocks) blocks - key else blocks + key }
                        if (blocks.isEmpty()) P({ classes("gen-err") }) { Text("Escolha pelo menos um tipo de material.") }
                    }
                    if ("THEORY" in blocks || "SUMMARY" in blocks) GenSection(++n, "Profundidade", "Quão completa deve ser a teoria?") {
                        Segmented(depthChoices, depth) { depth = it }
                    }
                    if (hasQuestions) GenSection(++n, "Questões", if (questionsOnly) "Cada lote traz até $BATCH_QUESTIONS questões novas." else "No seu plano, até $maxQuestions ao criar a matéria.") {
                        val top = if (questionsOnly) BATCH_QUESTIONS else maxQuestions
                        BrandCounter(count, { count = it }, step = 1, min = 1, max = top, unit = "questões", chips = listOf(5, 10, 20, 30).filter { it <= top }, label = "Quantidade de questões")
                        if (freeTier) FreeMixedNotice() else {
                            Span({ classes("gen-label") }) { Text("Nível") }
                            Segmented(difficultyChoices, difficulty) { difficulty = it }
                            Span({ classes("gen-label") }) { Text("Formato") }
                            ChoiceCards(styleChoices, { it == style }) { style = it }
                        }
                        Div({ classes("field") }) {
                            Label { Text("Banca (opcional)") }
                            Input(InputType.Text) {
                                classes("input"); value(board); attr("placeholder", "Ex.: Cebraspe, FGV, FCC"); attr("maxlength", "40")
                                onInput { board = it.value }
                            }
                            Span({ classes("xs", "muted") }) { Text("Em branco, as questões seguem o estilo das provas anteriores deste concurso.") }
                        }
                    }
                    if (p is AiContent.Progress.Failed) {
                        Div({ classes("banner", "error") }) { Icon("error"); Text(p.message) }
                        var upgradeSeen by remember(p) { mutableStateOf(false) }
                        val reason = upgradeReasonFor(p.message, questionsOnly)
                        if (reason != null && !upgradeSeen) UpgradeModal(reason) { upgradeSeen = true }
                    }
                }
                Div({ classes("gen-foot", "gen-foot-sum") }) {
                    Div({ classes("gen-sum") }) {
                        val parts = buildList {
                            if (!questionsOnly) add(if (blocks.size == blockChoices.size) "Pacote completo" else blockChoices.filter { it.key in blocks }.joinToString(", ") { it.title })
                            if ("THEORY" in blocks || "SUMMARY" in blocks) add("teoria " + (depthChoices.firstOrNull { it.key == depth }?.title?.lowercase() ?: ""))
                            if (hasQuestions) add("$count questões" + (if (freeTier) " misturadas" else " · " + (difficultyChoices.firstOrNull { it.key == difficulty }?.title?.lowercase() ?: "")) + board.takeIf { it.isNotBlank() }?.let { " · $it" }.orEmpty())
                        }
                        Span({ classes("gen-sum-l") }) { Text("SEU PEDIDO") }
                        B { Text(parts.joinToString(" · ").ifBlank { "Nada marcado ainda" }) }
                    }
                    Btn("Cancelar", onClose, style = "ghost")
                    Btn(if (questionsOnly) "Gerar questões" else "Gerar material", { start() }, style = "primary", icon = "auto_awesome", enabled = blocks.isNotEmpty())
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

/** Grátis: a regra desta matéria, à vista antes de gerar (as matérias saem prontas da biblioteca). */
@Composable
private fun FreeMeter(f: FreeInfo, purpose: String) {
    val (big, label, empty) = when (purpose) {
        "PLAN_DAY" -> Triple("Livre", "Matéria do seu plano de hoje · ${f.planDayUsed} de ${f.planDayLimit} hoje", f.planDayUsed >= f.planDayLimit)
        "ADVANCE" -> Triple(if (f.weeklyUsed < f.weeklyLimit) "1" else "0", "adiantamento grátis nesta semana (no app, um anúncio curto adianta até 2 por dia)", f.weeklyUsed >= f.weeklyLimit)
        else -> Triple("${(f.extrasLimit - f.extrasUsed).coerceAtLeast(0)}", "de ${f.extrasLimit} matérias fora do plano restantes neste mês", f.extrasUsed >= f.extrasLimit)
    }
    Div({ classes(*listOfNotNull("quota-meter", if (empty) "empty" else null).toTypedArray()) }) {
        Div({ classes("qm-top") }) {
            Span({ classes("qm-num") }) { B { Text(big) } }
            Span({ classes("qm-label") }) { Text(label) }
        }
        P({ classes("xs", "muted"); attr("style", "margin:6px 0 0") }) { Text("No Grátis as matérias saem prontas da biblioteca do Estudário, conferidas pela equipe.") }
    }
}

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

/** Grátis: no lugar das escolhas de nível e formato, o que vem e por quê (como no app). */
@Composable
private fun FreeMixedNotice() {
    var offer by remember { mutableStateOf(false) }
    Div({ classes("free-mixed") }) {
        Icon("shuffle")
        Div {
            B { Text("Questões misturadas") }
            P({ classes("small", "muted") }) { Text("Fáceis, médias e difíceis, de múltipla escolha e de Certo ou Errado.") }
        }
    }
    // o convite para o upgrade, chamativo: clicar abre a oferta dos planos
    Button({ classes("pro-invite"); onClick { offer = true } }) {
        Span({ classes("pro-tag") }) { Text("ESSENCIAL · PRO") }
        B({ classes("pro-title") }) { Text("Treine do jeito que a sua prova cobra") }
        org.jetbrains.compose.web.dom.Ul {
            org.jetbrains.compose.web.dom.Li { Icon("check_circle", filled = true); Text("Escolha o nível: só fáceis, médias ou difíceis") }
            org.jetbrains.compose.web.dom.Li { Icon("check_circle", filled = true); Text("Escolha o formato: múltipla escolha ou Certo ou Errado") }
            org.jetbrains.compose.web.dom.Li { Icon("check_circle", filled = true); Text("Mais lotes de questões no estilo da sua banca") }
        }
        Span({ classes("pro-cta") }) { Text("Fazer upgrade do plano"); Icon("arrow_forward") }
    }
    if (offer) UpgradeModal(UpgradeReason.QUESTION_STYLE) { offer = false }
}

/** Uma parte do pedido: número (vira ✓ quando completa), título, dica e as escolhas. */
@Composable
private fun GenSection(number: Int, title: String, hint: String?, done: Boolean = true, content: @Composable () -> Unit) {
    Div({ classes(*listOfNotNull("gen-sec", if (!done) "todo" else null).toTypedArray()) }) {
        Div({ classes("gen-sec-head") }) {
            Span({ classes("gen-num") }) { if (done) Icon("check", plain = true) else Text("$number") }
            Div { B { Text(title) }; hint?.let { Span({ classes("hint") }) { Text(it) } } }
        }
        content()
    }
}

/** Escolha única em faixa (profundidade, nível): o título e a dica de cada opção. */
@Composable
private fun Segmented(items: List<Choice>, selected: String, onPick: (String) -> Unit) {
    Div({ classes("gen-seg") }) {
        items.forEach { item ->
            key(item.key) {
                Button({ classes(*listOfNotNull("gen-seg-btn", if (item.key == selected) "on" else null).toTypedArray()); attr("aria-pressed", "${item.key == selected}"); onClick { onPick(item.key) } }) {
                    B { Text(item.title) }; Span { Text(item.hint) }
                }
            }
        }
    }
}
