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
import org.jetbrains.compose.web.dom.I
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

/** Gerar material com o Estudário: o estúdio, com as escolhas de um lado e o pacote montando ao vivo do outro. */
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
        Generation.start(topicId, AiContent.Options(blocks = blocks, depth = depth, questionCount = count, style = style, difficulty = difficulty, board = board, avoid = if (questionsOnly) avoidFor(topicId) else avoidForMaterial(topicId), purpose = AiContent.purposeFor(Store.data, topicId)))
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

    // O estúdio: escolhas à esquerda, o pacote montando ao vivo à direita (no celular, embaixo, com
    // o botão sempre à mão). Cabeçalho fixo; o meio rola.
    Modal(onDismiss = close, wide = true, extraClass = "gx-studio") {
        when (val p = progress) {
            null, is AiContent.Progress.Failed -> {
                Div({ classes("gx-top") }) {
                    Div({ classes("gx-top-id") }) {
                        Span({ classes("gx-kicker") }) { Text(if (questionsOnly) "ESTÚDIO DE QUESTÕES" else "ESTÚDIO DE MATERIAL") }
                        org.jetbrains.compose.web.dom.H2 { Text(title) }
                        topic?.title?.let { P({ classes("gx-topic") }) { Text(it) } }
                    }
                    Button({ classes("gx-x"); attr("type", "button"); attr("aria-label", "Fechar"); onClick { onClose() } }) { Icon("close", plain = true) }
                }
                Div({ classes("gx-main") }) {
                    Div({ classes("gx-controls") }) {
                        if (!questionsOnly) GxSection("library_books", "O que preparar", "Toque para ligar e desligar cada parte", "#A78BFF", aside = "${blocks.size} de ${blockChoices.size}") {
                            Div({ classes("gx-presets") }) {
                                listOf(
                                    Triple("Pacote completo", "library_books", blockChoices.map { it.key }.toSet()),
                                    Triple("Teoria e resumo", "menu_book", setOf("THEORY", "SUMMARY")),
                                    Triple("Revisão rápida", "bolt", setOf("QUICK_REVIEW", "TIPS_TRAPS", "ACTIVE_RECALL")),
                                    Triple("Só questões", "quiz", setOf("QUESTIONS")),
                                ).forEach { (label, icon, set) ->
                                    Button({ classes(*listOfNotNull("gx-preset", if (blocks == set) "on" else null).toTypedArray()); attr("type", "button"); onClick { blocks = set } }) {
                                        Icon(icon, filled = true); Text(label)
                                    }
                                }
                            }
                            GxTiles(blockChoices.map { GxOption(it.key, it.title, it.hint, it.icon, BLOCK_COLORS[it.key] ?: "#A78BFF") }, { it in blocks }, cols = 3) { key ->
                                blocks = if (key in blocks) blocks - key else blocks + key
                            }
                            if (blocks.isEmpty()) P({ classes("gx-err") }) { Icon("error", plain = true); Text("Ligue pelo menos uma parte do material.") }
                        }
                        if ("THEORY" in blocks || "SUMMARY" in blocks) GxSection("menu_book", "Profundidade da teoria", "Quão completa deve ser", "#22D3EE") {
                            GxSeg(depthChoices.mapIndexed { i, c -> GxOption(c.key, c.title, c.hint, color = listOf("#22D3EE", "#6F9BFF", "#A78BFF")[i]) }, depth, { depth = it }) { option ->
                                // a grossura do livro cresce com a profundidade
                                Span({ classes("gx-book"); attr("style", "--t:${depthChoices.indexOfFirst { it.key == option.key } + 1}") }) { repeat(depthChoices.indexOfFirst { it.key == option.key } + 1) { I {} } }
                            }
                        }
                        if (hasQuestions) GxSection("quiz", "Questões", if (questionsOnly) "Cada lote traz até $BATCH_QUESTIONS questões novas" else "No seu plano, até $maxQuestions ao criar a matéria", "#2FD39A") {
                            val top = if (questionsOnly) BATCH_QUESTIONS else maxQuestions
                            GxCounter(count, { count = it }, min = 1, max = top, unit = if (count == 1) "questão" else "questões", chips = listOf(5, 10, 20, 30), label = "Quantidade de questões")
                            if (freeTier) FreeMixedNotice() else {
                                Span({ classes("gx-label") }) { Text("Nível") }
                                GxSeg(difficultyChoices.map { GxOption(it.key, it.title, it.hint, color = DIFF_COLORS[it.key] ?: "#A78BFF") }, difficulty, { difficulty = it }) { option ->
                                    Span({ classes("gx-meter", "d-${option.key.lowercase()}") }) { repeat(3) { I {} } }
                                }
                                Span({ classes("gx-label") }) { Text("Formato") }
                                GxSeg(styleChoices.map { GxOption(it.key, it.title, it.hint, color = "#2FD39A") }, style, { style = it }) { option -> FormatMock(option.key) }
                            }
                            Span({ classes("gx-label") }) { Text("Banca (opcional)") }
                            Div({ classes("gx-board") }) {
                                Icon("Colunas")
                                Input(InputType.Text) {
                                    classes("gx-input"); value(board); attr("placeholder", "Ex.: Cebraspe, FGV, FCC"); attr("maxlength", "40")
                                    onInput { board = it.value }
                                }
                                if (board.isNotBlank()) Button({ classes("gx-board-x"); attr("type", "button"); attr("aria-label", "Limpar banca"); onClick { board = "" } }) { Text("✕") }
                            }
                            Div({ classes("gx-chips", "tight") }) {
                                listOf("Cebraspe", "FGV", "FCC", "Vunesp", "Cesgranrio", "Quadrix").forEach { b ->
                                    Button({ classes(*listOfNotNull("gx-chip", if (board.equals(b, true)) "on" else null).toTypedArray()); attr("type", "button"); onClick { board = if (board.equals(b, true)) "" else b } }) { Span { Text(b) } }
                                }
                            }
                            P({ classes("gx-note") }) { Text("Em branco, as questões seguem o estilo das provas anteriores deste concurso.") }
                        }
                        if (p is AiContent.Progress.Failed) {
                            Div({ classes("gx-fail") }) { Icon("error", plain = true); Text(p.message) }
                            var upgradeSeen by remember(p) { mutableStateOf(false) }
                            val reason = upgradeReasonFor(p.message, questionsOnly)
                            if (reason != null && !upgradeSeen) UpgradeModal(reason) { upgradeSeen = true }
                        }
                    }
                    // ---- o pacote montando ao vivo
                    Div({ classes("gx-aside") }) {
                        Div({ classes("gx-pack") }) {
                            Div({ classes("gx-pack-head") }) {
                                Folha(if (blocks.isEmpty()) "thinking" else if (blocks.size == blockChoices.size) "proud" else "happy", 64)
                                Div {
                                    Span({ classes("gx-kicker") }) { Text("SEU PACOTE") }
                                    B { Text(if (blocks.isEmpty()) "Nada ligado ainda" else if (questionsOnly) "$count questões novas" else "${blocks.size} ${if (blocks.size == 1) "parte" else "partes"} para estudar") }
                                }
                            }
                            free?.takeIf { !questionsOnly }?.let { f -> FreeMeter(f, AiContent.purposeFor(Store.data, topicId)) }
                            quota?.takeIf { free == null || questionsOnly }?.let { q ->
                                QuotaMeter(q, if (questionsOnly) "lotes de questões" else "materiais")
                                if (q.limit > 0 && q.remaining == 0 && !upgradeSeen) UpgradeModal(if (questionsOnly) UpgradeReason.QUESTIONS else UpgradeReason.CONTENT) { upgradeSeen = true }
                            }
                            val recipe = buildList {
                                if (!questionsOnly) add(if (blocks.size == blockChoices.size) "pacote completo" else blockChoices.filter { it.key in blocks }.joinToString(", ") { it.title.lowercase() })
                                if ("THEORY" in blocks || "SUMMARY" in blocks) add("teoria " + (depthChoices.firstOrNull { it.key == depth }?.title?.lowercase() ?: ""))
                                if (hasQuestions) add("$count questões " + (if (freeTier) "misturadas" else (difficultyChoices.firstOrNull { it.key == difficulty }?.title?.lowercase() ?: "")) + board.takeIf { it.isNotBlank() }?.let { " · $it" }.orEmpty())
                            }
                            if (recipe.isNotEmpty()) P({ classes("gx-recipe") }) { Text(recipe.joinToString(" · ").replaceFirstChar { it.uppercase() }) }
                        }
                        Div({ classes("gx-go") }) {
                            GxLaunch(if (questionsOnly) "Gerar $count questões" else "Gerar material", enabled = blocks.isNotEmpty()) { start() }
                            P({ classes("gx-note") }) { Text("Pode fechar e continuar estudando: eu te aviso quando ficar pronto.") }
                        }
                    }
                }
            }
            is AiContent.Progress.Done -> Div({ classes("gx-done", "pop-in") }) {
                Div({ classes("gx-done-burst") }) { repeat(12) { I({ attr("style", "--k:$it") }) } }
                Div({ classes("gen-done-folha") }) { Folha("happy", 150) }
                Span({ classes("gx-kicker") }) { Text("PRONTO") }
                org.jetbrains.compose.web.dom.H2 { Text("Prontinho!") }
                P { Text(if (showQuestions) "As novas questões já estão em “${topic?.title ?: "neste tópico"}”, prontas para treinar." else "Seu material está salvo em “${topic?.title ?: "neste tópico"}”: teoria, flashcards e questões esperando por você.") }
                val asked = Generation.requested[topicId] ?: 0
                val got = Generation.delivered[topicId] ?: 0
                if (got in 1 until asked) P({ classes("gx-note") }) {
                    Text("Este tópico ainda não tinha questões prontas: vieram $got inéditas, escritas agora. Quando terminar, peça mais em lotes de 10.")
                }
                Div({ classes("gx-done-actions") }) {
                    if (showQuestions) GxLaunch("Treinar agora", icon = "play_circle") { Generation.clear(topicId); onClose(); Router.go(Route.Quiz("topico-$topicId-10")) }
                    else GxLaunch("Ver material", icon = "menu_book") { Generation.clear(topicId); onClose(); Router.go(Route.Topic(topicId)) }
                    Button({ classes("gx-ghost"); attr("type", "button"); onClick { close() } }) { Text("Fechar") }
                }
            }
            else -> Div({ classes("gen-body", "gen-working", "gx-working") }) {
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

/** A cor de cada parte do material (a mesma no ladrilho e na folha do pacote). */
private val BLOCK_COLORS = mapOf(
    "THEORY" to "#A78BFF", "SUMMARY" to "#22D3EE", "QUICK_REVIEW" to "#FFC94D", "TIPS_TRAPS" to "#FF8A3D",
    "ACTIVE_RECALL" to "#F472B6", "QUESTIONS" to "#2FD39A", "ERROR_CONCEPTS" to "#FF5A7A",
)
private val DIFF_COLORS = mapOf("EASY" to "#2FD39A", "MEDIUM" to "#FFC94D", "HARD" to "#FF5A7A", "MIXED" to "#A78BFF")

/** Miniatura do formato: as alternativas de A a E, de A a D, o Certo/Errado ou os dois. */
@Composable
private fun FormatMock(style: String) {
    Span({ classes("gx-fmt") }) {
        when (style) {
            "FIVE_OPTIONS" -> "ABCDE".forEach { l -> Span({ classes("row") }) { B { Text("$l") }; I {} } }
            "FOUR_OPTIONS" -> "ABCD".forEach { l -> Span({ classes("row") }) { B { Text("$l") }; I {} } }
            "TRUE_FALSE" -> Span({ classes("ce") }) { B { Text("C") }; B { Text("E") } }
            else -> {
                "ABC".forEach { l -> Span({ classes("row") }) { B { Text("$l") }; I {} } }
                Span({ classes("ce") }) { B { Text("C") }; B { Text("E") } }
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

/**
 * Material completo: as questões que a pessoa já tem, primeiro as da mesma matéria. Tópicos vizinhos
 * usam a mesma matéria da biblioteca, e as repetidas eram descartadas ao salvar (pedia 20, ficavam 13).
 */
private fun avoidForMaterial(topicId: Long): List<String> {
    val subjectId = Store.data.topics.firstOrNull { it.id == topicId }?.subjectId
    val subjectTopics = Store.data.topics.filter { it.subjectId == subjectId }.map { it.id }.toSet()
    val (same, others) = Store.data.questions.filter { it.statement.isNotBlank() }.partition { it.topicId in subjectTopics }
    return (same.asReversed() + others.asReversed()).map { it.statement }.take(400)
}
