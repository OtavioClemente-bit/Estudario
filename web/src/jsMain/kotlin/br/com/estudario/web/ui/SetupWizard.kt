package br.com.estudario.web.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import br.com.estudario.domain.planner.InitialKnowledge
import br.com.estudario.domain.planner.PersonalDifficulty
import br.com.estudario.domain.planner.PlanPriority
import br.com.estudario.domain.planner.StudyMethodConfig
import br.com.estudario.domain.planner.StudyProfile
import br.com.estudario.time.plusDays
import br.com.estudario.time.isoDayOfWeek
import br.com.estudario.time.toEpochDay
import br.com.estudario.web.Route
import br.com.estudario.web.Router
import br.com.estudario.web.data.Catalog
import br.com.estudario.web.data.CatalogEntry
import br.com.estudario.web.data.CatalogSubject
import br.com.estudario.web.data.EditalAi
import br.com.estudario.web.data.Entitlements
import br.com.estudario.web.data.Planning
import br.com.estudario.web.data.Queries
import br.com.estudario.web.data.Snapshot
import br.com.estudario.web.data.Store
import br.com.estudario.domain.catalog.SubjectArt
import br.com.estudario.text.TopicDisplay
import br.com.estudario.domain.planner.SubjectTuning
import br.com.estudario.domain.planner.TuningAxis
import org.jetbrains.compose.web.dom.Li
import org.jetbrains.compose.web.dom.Ol
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.attributes.placeholder
import org.jetbrains.compose.web.dom.Label
import org.jetbrains.compose.web.attributes.selected
import org.jetbrains.compose.web.dom.B
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H2
import org.jetbrains.compose.web.dom.H3
import org.jetbrains.compose.web.dom.I
import org.jetbrains.compose.web.dom.Input
import org.jetbrains.compose.web.dom.Option
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Select
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text

/** Escolhas de uma matéria no passo "Matérias" (prioridade, dificuldade e o quanto já sabe). */
private data class SubjectAxes(
    val priority: PlanPriority = PlanPriority.MEDIUM,
    val difficulty: PersonalDifficulty = PersonalDifficulty.NORMAL,
    val knowledge: InitialKnowledge = InitialKnowledge.NONE,
)

private val stepNames = listOf("Edital", "Conferir", "Prova", "Rotina", "Matérias", "Momento")

private fun priorityLabel(p: PlanPriority) = when (p) { PlanPriority.CRITICAL -> "Muito alta"; PlanPriority.HIGH -> "Alta"; PlanPriority.MEDIUM -> "Média"; PlanPriority.LOW -> "Baixa" }

/** Do menor para o maior, na ordem das barrinhas do medidor. */
private val priorityLevels = listOf(PlanPriority.LOW, PlanPriority.MEDIUM, PlanPriority.HIGH, PlanPriority.CRITICAL)

/** O peso do site (4 níveis) na escala do app, para a prévia "No plano" sair igual. */
private fun examPriorityOf(priority: PlanPriority): br.com.estudario.domain.planner.ExamPriority = when (priority) {
    PlanPriority.LOW -> br.com.estudario.domain.planner.ExamPriority.LOW
    PlanPriority.MEDIUM -> br.com.estudario.domain.planner.ExamPriority.MEDIUM
    PlanPriority.HIGH -> br.com.estudario.domain.planner.ExamPriority.HIGH
    PlanPriority.CRITICAL -> br.com.estudario.domain.planner.ExamPriority.VERY_HIGH
}

private fun flatten(nodes: List<EditalAi.TopicNode>, depth: Int = 0): List<String> =
    nodes.flatMap { listOf("  ".repeat(depth) + it.name) + flatten(it.children, depth + 1) }

/**
 * Configurar os estudos direto no navegador, como no app: o Folha pergunta o concurso, mostra o
 * edital para conferir, a data da prova, a rotina, como você está em cada matéria e o momento do
 * estudo. Antes de abrir, o Folha mostra uma prévia do plano montado pelo mesmo motor do app.
 */
@Composable
fun SetupScreen() {
    val scope = rememberCoroutineScope()
    var step by remember { mutableStateOf(0) }
    var entries by remember { mutableStateOf<List<CatalogEntry>?>(null) }
    var query by remember { mutableStateOf("") }
    var chosen by remember { mutableStateOf<CatalogEntry?>(null) }
    var subjects by remember { mutableStateOf<List<CatalogSubject>>(emptyList()) }
    var aiEdital by remember { mutableStateOf<Triple<String, String, List<EditalAi.SubjectNode>>?>(null) }
    var pdfOpen by remember { mutableStateOf(false) }
    // Edital pelo PDF é dos planos pagos: no Grátis, o clique já abre a oferta (o servidor também recusa).
    var pdfUpgrade by remember { mutableStateOf(false) }
    var pdfQuota by remember { mutableStateOf<Quota?>(null) }
    var pdfFree by remember { mutableStateOf(false) }
    androidx.compose.runtime.LaunchedEffect(Unit) { pdfQuota = quotaFor("SYLLABUS_GENERATION"); pdfFree = planTierOf()?.let { it == "FREE" } == true }
    var examDate by remember { mutableStateOf("") }
    var minutes by remember { mutableStateOf(listOf(120, 120, 120, 120, 120, 180, 0)) }
    var block by remember { mutableStateOf(50) }
    var axes by remember { mutableStateOf(mapOf<String, SubjectAxes>()) }
    var profile by remember { mutableStateOf(StudyProfile.DO_ZERO) }
    var busy by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    var preview by remember { mutableStateOf<Pair<Snapshot, String>?>(null) }
    var building by remember { mutableStateOf(false) }

    var limits by remember { mutableStateOf(Entitlements.current) }
    LaunchedEffect(Unit) {
        try { entries = Catalog.entries() } catch (e: Throwable) { error = e.message }
    }
    LaunchedEffect(Unit) { limits = Entitlements.load() }

    // O edital escolhido, num formato só: (matéria, tópicos).
    val edital: List<Pair<String, List<String>>> = aiEdital?.third?.map { it.name to flatten(it.topics) } ?: subjects.map { it.name to it.topics }
    val editalName = chosen?.shortName ?: aiEdital?.first.orEmpty()

    /** Os dados com o edital escolhido já gravado (e o id do concurso). */
    fun withEdital(data: Snapshot): Pair<Snapshot, Long> {
        val entry = chosen
        val ai = aiEdital
        return if (entry != null) Catalog.apply(data, entry, subjects) else EditalAi.apply(data, ai!!.first, ai.third)
    }

    fun build(data: Snapshot): Pair<Snapshot, String> {
        val entry = chosen
        val ai = aiEdital
        val (withEdital, competitionId) = withEdital(data)
        // Sem direito a plano novo (Grátis já tem o seu): o concurso entra no plano que existe.
        val existing = data.plans.firstOrNull { it.active } ?: data.plans.firstOrNull()
        if (existing != null && !Entitlements.canCreatePlan(data)) {
            val competitions = Planning.competitionsOf(withEdital, existing.id) + competitionId
            return Planning.setCompetitions(withEdital, existing.id, competitions) to existing.id
        }
        val choices = withEdital.subjects.filter { it.competitionId == competitionId }.sortedBy { it.position }.map { s ->
            val a = axes[s.name] ?: SubjectAxes()
            Planning.SubjectChoice(s.id, s.name, priority = a.priority, personalDifficulty = a.difficulty, initialKnowledge = a.knowledge)
        }
        return Planning.create(
            withEdital,
            Planning.NewPlan(
                competitionId = competitionId,
                name = "Plano ${entry?.shortName ?: ai!!.first}",
                objective = entry?.role ?: ai!!.second.ifBlank { ai.first },
                startDate = Queries.todayDate(),
                examDate = examDate.takeIf { it.isNotBlank() }?.let { runCatching { LocalDate.parse(it) }.getOrNull() },
                weeklyMinutes = minutes,
                subjects = choices,
                method = StudyMethodConfig.forProfile(profile).copy(blockMinutes = block),
            ),
        )
    }

    // Editais salvos no limite do plano: em vez do assistente, a oferta de assinatura.
    if (limits != null && !Store.demo && !Entitlements.canAddEdital(Store.data)) {
        var upgrade by remember { mutableStateOf(false) }
        Card {
            FolhaSays("thinking", 72) { B { Text("Você já tem ${Store.data.competitions.size} editais salvos, o máximo do seu plano.") } }
            P({ classes("muted") }) { Text("Para começar outro concurso, exclua um dos editais na tela do edital ou assine um plano com mais espaço.") }
            Div({ classes("row") }) {
                Btn("Ver planos", { upgrade = true }, style = "primary", icon = "workspace_premium")
                Btn("Voltar", { Router.go(Route.Edital) }, style = "ghost")
            }
        }
        if (upgrade) UpgradeModal(UpgradeReason.EDITAIS) { upgrade = false }
        return
    }

    // Tela final: o Folha montando e a prévia do plano.
    if (building || preview != null) {
        PlanPreview(preview, onBack = { preview = null; building = false }) {
            try {
                // Primeiro plano da conta: vai direto para o Início, com o primeiro estudo do dia em destaque.
                val firstTime = Store.data.attempts.isEmpty() && Store.data.sessions.isEmpty()
                Store.update { build(it).first }
                // Primeiro plano: o Início abre com o roteiro "Seu primeiro dia" (vale nos dois aparelhos).
                if (firstTime) br.com.estudario.web.data.Prefs.markCelebrated(listOf(br.com.estudario.domain.FirstDay.STARTED))
                Router.go(if (firstTime) Route.Home else Route.Plan)
            } catch (e: Throwable) { error = e.message; preview = null; building = false }
        }
        return
    }

    Div({ classes("setup-head") }) {
        Div({ classes("row", "between") }) {
            Span({ classes("xs", "muted", "strong") }) { Text("PASSO ${step + 1} DE ${stepNames.size} · ${stepNames[step].uppercase()}") }
            if (step > 0) Btn("Voltar", { step-- }, style = "ghost", small = true, icon = "arrow_back")
        }
        Div({ classes("wizard-dots") }) { stepNames.indices.forEach { i -> Span({ classes(*listOfNotNull(if (i <= step) "on" else null).toTypedArray()) }) } }
    }
    error?.let { Div({ classes("banner", "error") }) { Icon("error"); Text(it) } }

    LaunchedEffect(step) { kotlinx.browser.window.scrollTo(0.0, 0.0) }
    when (step) {
        0 -> {
            FolhaSays("talking", 88) {
                H2 { Text("Oi! Eu sou o Folha. Qual concurso você vai prestar?") }
                P({ classes("small", "muted") }) { Text("Em poucos passos eu monto o seu plano até a prova. Procure pelo órgão, cargo ou sigla, como “pm mg”, “trt analista” ou “pf agente”.") }
            }
            // O PDF fica no alto (antes ficava no fim da lista e sumia rolando). Sem plano pago, abre a oferta.
            Button({
                classes("gx-panel", "pdf-cta"); attr("type", "button")
                onClick { if (Store.demo || pdfFree || pdfQuota?.let { it.remaining == 0 } == true) pdfUpgrade = true else pdfOpen = true }
            }) {
                Span({ classes("pdf-cta-ic") }) { Icon("picture_as_pdf", filled = true) }
                Span({ classes("pdf-cta-txt") }) {
                    org.jetbrains.compose.web.dom.B { Text("Não achou o seu concurso? Envie o PDF do edital") }
                    Span { Text("Eu leio o edital e monto as matérias e os tópicos para você." + if (pdfFree) " Nos planos Essencial e Pro." else "") }
                }
                Span({ classes("pdf-cta-go") }) { Text("Enviar PDF"); Icon("arrow_forward", plain = true) }
            }
            CatalogPicker(entries, error, query, { query = it }, busyId = if (busy) chosen?.id else null) { entry ->
                busy = true; chosen = entry; aiEdital = null; error = null
                scope.launch {
                    try { subjects = Catalog.subjects(entry.id); axes = emptyMap(); step = 1 } catch (e: Throwable) { error = e.message } finally { busy = false }
                }
            }
            if (pdfUpgrade) UpgradeModal(UpgradeReason.SYLLABUS) { pdfUpgrade = false }
            if (pdfOpen) PdfEditalDialog(onClose = { pdfOpen = false }) { name, role, nodes -> aiEdital = Triple(name, role, nodes); chosen = null; axes = emptyMap(); pdfOpen = false; step = 1 }
        }
        1 -> {
            val topicCount = edital.sumOf { it.second.size }
            FolhaSays("point", 88) {
                H2 { Text("Confere comigo o edital de $editalName") }
                P({ classes("small", "muted") }) { Text("${edital.size} matérias e $topicCount tópicos. Toque numa matéria para abrir ou fechar os tópicos.") }
            }
            // Peso de cada matéria na prova (só quando o edital do catálogo traz o quadro de questões).
            val weightLabels = remember(subjects, aiEdital) {
                if (aiEdital != null) emptyMap() else {
                    val assessed = br.com.estudario.domain.ExamWeights.assessAll(subjects.map { it.share })
                    subjects.mapIndexedNotNull { i, s ->
                        TopicDisplay.weightLabel(s.questions, assessed.getOrNull(i)?.evidence?.firstOrNull()?.description)?.let { s.name to it }
                    }.toMap()
                }
            }
            // Começa com a primeira matéria aberta, para mostrar como é por dentro.
            var opened by remember(editalName) { mutableStateOf<Set<Int>>(if (edital.isEmpty()) emptySet() else setOf(0)) }
            Div({ classes("es-summary") }) {
                Span({ classes("es-count") }) { Text("${edital.size} matérias · $topicCount tópicos") }
                val allOpen = edital.isNotEmpty() && opened.size == edital.size
                Btn(if (allOpen) "Fechar todas" else "Abrir todas", { opened = if (allOpen) emptySet() else edital.indices.toSet() }, style = "ghost", small = true, icon = if (allOpen) "unfold_less" else "unfold_more")
            }
            Div({ classes("es-list") }) {
                edital.forEachIndexed { index, (name, topics) ->
                    key(name + index) {
                        EditalSubjectCard(name, topics, weightLabels[name], open = index in opened) {
                            opened = if (index in opened) opened - index else opened + index
                        }
                    }
                }
            }
            NextBar("Está certo, continuar") { step = 2 }
        }
        2 -> {
            val today = Queries.todayDate()
            val date = examDate.takeIf { it.isNotBlank() }?.let { runCatching { LocalDate.parse(it) }.getOrNull() }
            val daysLeft = date?.let { (it.toEpochDay() - today.toEpochDay()).toInt() }
            FolhaSays(if (date == null) "thinking" else "happy", 88) {
                H2 { Text("E quando é a prova?") }
                P({ classes("small", "muted") }) { Text("Com a data, eu divido o tempo em base, aprofundamento e reta final. Se ainda não saiu, tudo bem: dá para colocar depois.") }
            }
            Card(extra = "pad-lg") {
                Div({ classes("date-hero") }) {
                    if (daysLeft != null) {
                        B({ classes("big") }) { Text("$daysLeft") }
                        Span { Text(if (daysLeft == 1) "dia até a prova" else "dias até a prova") }
                        Span({ classes("small", "muted") }) { Text(Queries.dayLabel(date) + " de ${date.year}") }
                    } else {
                        Icon("event", extraClass = "faint")
                        Span({ classes("muted") }) { Text("Escolha a data ou um atalho abaixo") }
                    }
                }
                Div({ classes("row", "wrap"); attr("style", "justify-content:center;margin:14px 0") }) {
                    listOf(30 to "1 mês", 90 to "3 meses", 180 to "6 meses", 365 to "1 ano").forEach { (days, label) ->
                        val value = today.plusDays(days).toString()
                        Button({ classes("chip-btn", *listOfNotNull(if (examDate == value) "on" else null).toTypedArray()); onClick { examDate = value } }) { Text("Em $label") }
                    }
                }
                Div({ classes("row"); attr("style", "justify-content:center") }) {
                    DateField(examDate, { examDate = it }, min = today.toString(), examChips = true, title = "Data da prova", style = "max-width:220px")
                    if (examDate.isNotBlank()) Btn("Limpar", { examDate = "" }, style = "ghost", small = true)
                }
                if (daysLeft != null && daysLeft < 30) P({ classes("small"); attr("style", "text-align:center;margin-top:10px;color:var(--amber-strong)") }) { Text("Reta final! Vou priorizar revisão e questões.") }
            }
            NextBar(if (examDate.isBlank()) "Ainda não sei a data" else "Continuar") { step = 3 }
        }
        3 -> {
            val total = minutes.sum()
            FolhaSays("talking", 88) {
                H2 { Text("Quanto tempo você tem em cada dia?") }
                P({ classes("small", "muted") }) { Text("Use o menos e o mais. Dia sem tempo vira folga. Dá para mudar quando quiser.") }
            }
            Card {
                H3 { Text("Horas por dia") }
                RoutineWeek(minutes, { minutes = it })
            }
            Card {
                H3 { Text("Tamanho do bloco") }
                BlockSizePicker(block, { block = it })
            }
            NextBar("Continuar", enabled = minutes.any { it > 0 }) { step = 4 }
        }
        4 -> {
            FolhaSays("thinking", 88) {
                H2 { Text("Ajuste só o que for diferente para você") }
                P({ classes("small", "muted") }) { Text("Tudo começa no meio do caminho. Clique nas barrinhas para mudar: eu dou mais tempo ao que pesa na prova, ao que é difícil para você e ao que você ainda não conhece.") }
            }
            LevelLegend()
            // O Folha comenta a última mudança, com as mesmas frases do app (SubjectTuning, em shared/).
            var reaction by remember { mutableStateOf<String?>(null) }
            edital.forEach { (name, topics) ->
                key(name) {
                    val a = axes[name] ?: SubjectAxes()
                    Card(extra = "tune-card") {
                        Div({ classes("tune-head"); attr("style", "--c:${subjectColor(name)}") }) {
                            Span({ classes("hs-art") }) { Icon(SubjectArt.iconFor(name)) }
                            Div {
                                H3 { Text(name) }
                                Span({ classes("xs", "muted") }) { Text(if (topics.size == 1) "1 tópico" else "${topics.size} tópicos") }
                            }
                        }
                        LevelMeter(TuningAxis.PRIORITY.title, priorityLevels.map { priorityLabel(it) }, priorityLevels.indexOf(a.priority), AxisColor.PRIORITY, help = TuningAxis.PRIORITY.help) {
                            // Nível 1..4 do site na escala de 5 do app (Baixa, Média, Alta, Muito alta).
                            reaction = SubjectTuning.reaction(TuningAxis.PRIORITY, it + 1, name)
                            axes = axes + (name to (axes[name] ?: SubjectAxes()).copy(priority = priorityLevels[it]))
                        }
                        LevelMeter(TuningAxis.EFFORT.title, PersonalDifficulty.entries.map { it.label }, a.difficulty.ordinal, AxisColor.EFFORT, help = TuningAxis.EFFORT.help) {
                            reaction = SubjectTuning.reaction(TuningAxis.EFFORT, it, name)
                            axes = axes + (name to (axes[name] ?: SubjectAxes()).copy(difficulty = PersonalDifficulty.entries[it]))
                        }
                        LevelMeter(TuningAxis.KNOWLEDGE.title, InitialKnowledge.entries.map { it.label }, a.knowledge.ordinal, AxisColor.KNOWLEDGE, help = TuningAxis.KNOWLEDGE.help) {
                            reaction = SubjectTuning.reaction(TuningAxis.KNOWLEDGE, it, name)
                            axes = axes + (name to (axes[name] ?: SubjectAxes()).copy(knowledge = InitialKnowledge.entries[it]))
                        }
                        // A prévia do que essas três respostas fazem no plano; muda junto com cada clique.
                        val preview = SubjectTuning.planPreview(examPriorityOf(a.priority), a.difficulty, a.knowledge)
                        key(preview) {
                            Div({ classes("tune-preview"); attr("aria-live", "polite") }) { Icon("calendar_month"); Span { B { Text("No plano: ") }; Text(preview.removePrefix("No plano:").trimStart()) } }
                        }
                    }
                }
            }
            val line = reaction ?: if (axes.isEmpty()) "Pode seguir sem mexer em nada." else "Ajustes salvos. Dá para mudar depois na aba Plano."
            Div({ classes("tune-react"); attr("aria-live", "polite") }) {
                key(line) { FolhaSays("talking", 48) { P({ classes("small") }) { Text(line) } } }
            }
            NextBar("Continuar") { step = 5 }
        }
        else -> {
            FolhaSays("wink", 88) {
                H2 { Text("Última pergunta: em que momento você está?") }
                P({ classes("small", "muted") }) { Text("Isso muda a mistura de teoria, questões e revisão do seu plano.") }
            }
            Div({ classes("choices") }) {
                StudyProfile.entries.forEach { option ->
                    key(option.name) {
                        Button({ classes(*listOfNotNull("choice", if (profile == option) "on" else null).toTypedArray()); onClick { profile = option } }) {
                            Span({ classes("grow") }) { B { Text(option.label) }; Span({ classes("hint") }) { Text(option.summary) } }
                            Icon(if (profile == option) "check_circle" else "radio_button_unchecked", filled = profile == option, extraClass = "tick")
                        }
                    }
                }
            }
            NextBar("Montar meu plano", icon = "auto_awesome") {
                error = null
                building = true
                scope.launch {
                    // O plano fica pronto em instantes; a tela mostra todas as etapas, uma a uma, antes da prévia.
                    val started = kotlin.js.Date.now()
                    // Grátis: antes de montar, confere quais tópicos do edital têm matéria pronta (os outros
                    // ficam fora do plano e entram quando a matéria sair).
                    runCatching { br.com.estudario.web.data.LibraryGaps.refresh(withEdital(Store.data).first, isFreeTier()) }
                    val built = try { build(Store.data) } catch (e: Throwable) { error = e.message ?: "Não deu para montar o plano."; building = false; null }
                    if (built != null) {
                        val total = PLAN_STAGES.size * PLAN_STAGE_MILLIS + 600L
                        delay((total - (kotlin.js.Date.now() - started)).toLong().coerceAtLeast(0))
                        preview = built
                    }
                }
            }
        }
    }
}

@Composable
private fun NextBar(label: String, enabled: Boolean = true, icon: String = "arrow_forward", onNext: () -> Unit) {
    Div({ classes("row"); attr("style", "justify-content:flex-end") }) { Btn(label, onNext, icon = icon, enabled = enabled, style = "primary") }
}

/** Uma matéria do edital como cartão: desenho e cor da matéria, tópicos e peso na prova; abre a lista de tópicos. */
@Composable
private fun EditalSubjectCard(name: String, topics: List<String>, weight: String?, open: Boolean, onToggle: () -> Unit) {
    val color = subjectColor(name)
    Div({ classes(*listOfNotNull("es-card", if (open) "open" else null).toTypedArray()); attr("style", "--c:$color") }) {
        Button({ classes("es-head"); attr("type", "button"); attr("aria-expanded", "$open"); onClick { onToggle() } }) {
            Span({ classes("hs-art") }) { Icon(SubjectArt.iconFor(name)) }
            Span({ classes("es-title") }) {
                B { Text(name) }
                Span({ classes("es-chips") }) {
                    Span({ classes("es-chip") }) { Text(if (topics.size == 1) "1 tópico" else "${topics.size} tópicos") }
                    weight?.let { Span({ classes("es-chip", "weight") }) { Text(it) } }
                }
            }
            Span({ classes("es-toggle") }) { Icon(if (open) "expand_less" else "expand_more", plain = true) }
        }
        if (open) {
            // Numeração 1, 1.1, 1.2... seguindo a indentação de cada tópico (subtópicos ficam recuados).
            val rows = remember(topics) {
                val counters = mutableListOf<Int>()
                topics.map { raw ->
                    val depth = TopicDisplay.depthOf(raw).coerceAtMost(counters.size).coerceAtMost(3)
                    while (counters.size > depth + 1) counters.removeAt(counters.lastIndex)
                    if (counters.size == depth + 1) counters[depth]++ else counters.add(1)
                    Triple(depth, counters.joinToString("."), TopicDisplay.clean(raw))
                }
            }
            Ol({ classes("es-topics") }) {
                rows.forEachIndexed { i, (depth, number, text) ->
                    key(i) { EditalTopicItem(depth, number, text) }
                }
            }
        }
    }
}

@Composable
private fun EditalTopicItem(depth: Int, number: String, text: String) {
    var full by remember { mutableStateOf(false) }
    // Sem medir a linha no navegador: acima de ~110 letras costuma passar de duas linhas no celular.
    val long = text.length > 110
    Li({ classes(*listOfNotNull("es-topic", if (depth > 0) "sub" else null).toTypedArray()); attr("style", "--d:$depth") }) {
        Span({ classes("es-num") }) { Text(number) }
        Span({ classes("es-text") }) {
            Span({ classes(*listOfNotNull(if (long && !full) "clamp-2" else null).toTypedArray()) }) { Text(text) }
            if (long) Button({ classes("es-more"); attr("type", "button"); onClick { full = !full } }) { Text(if (full) "ver menos" else "ver tudo") }
        }
    }
}

/** Etapas da montagem do plano: aparecem todas, uma a uma, antes da prévia. */
private val PLAN_STAGES = listOf(
    "Lendo o seu edital",
    "Pesando cada matéria pelo edital e pelo seu nível",
    "Distribuindo as matérias pelos seus dias",
    "Encaixando revisões espaçadas",
    "Reservando tempo para questões e simulados",
    "Conferindo a primeira semana",
)
private const val PLAN_STAGE_MILLIS = 1_300L

/** O Folha montando o plano e, em seguida, a prévia da primeira semana antes de abrir. */
@Composable
private fun PlanPreview(result: Pair<Snapshot, String>?, onBack: () -> Unit, onOpen: () -> Unit) {
    if (result == null) {
        Div({ classes("center-page", "building") }) {
            ProcessView(
                title = "Montando o seu plano",
                eyebrow = "Quase lá",
                stages = PLAN_STAGES,
                stageMillis = PLAN_STAGE_MILLIS,
                scene = "plan",
            )
        }
        return
    }
    val (data, planId) = result
    val todayEpoch = Queries.todayEpoch()
    val tasks = data.tasks.filter { it.planId == planId }
    val week = (0 until 7).map { todayEpoch + it }
    val firstWeek = tasks.filter { it.day in week.first()..week.last() }
    val bySubject = firstWeek.groupBy { it.subjectName }.mapValues { (_, l) -> l.sumOf { it.minutes } }.entries.sortedByDescending { it.value }
    val maxMin = bySubject.maxOfOrNull { it.value }?.coerceAtLeast(1) ?: 1
    FolhaSays("happy", 96) {
        H2 { Text("Prontinho! Olha como vai ficar a sua primeira semana") }
        P({ classes("small", "muted") }) { Text("${firstWeek.size} atividades · ${Queries.minutesLabel(firstWeek.sumOf { it.minutes })} de estudo. Dá para ajustar tudo depois.") }
    }
    // O que o plano montou, com as contas feitas sobre as tarefas de verdade (FirstDay.reveal, igual no app).
    val plan = data.plans.firstOrNull { it.id == planId }
    val planSubjects = data.subjects.filter { it.competitionId == plan?.competitionId }
    val subjectIds = planSubjects.mapTo(hashSetOf()) { it.id }
    val reveal = br.com.estudario.domain.FirstDay.reveal(
        subjects = planSubjects.size,
        topics = Queries.leafTopics(data, subjectIds).size,
        theoryDays = tasks.filter { it.type == "THEORY" && it.topicId != null }.map { it.topicId!! to it.day },
        minutesBySubject = tasks.groupBy { it.subjectName }.mapValues { (_, l) -> l.sumOf { it.minutes } },
        examEpochDay = plan?.exam,
    )
    Card {
        Div({ classes("plan-reveal") }) {
            CardHead(reveal.headline)
            Div({ classes("pr-stats") }) {
                Div({ classes("pr-stat") }) { B { Text("${reveal.subjects}") }; Span({ classes("small", "muted") }) { Text("matérias") } }
                Div({ classes("pr-stat") }) { B { Text("${reveal.topics}") }; Span({ classes("small", "muted") }) { Text("tópicos") } }
                Div({ classes("pr-stat") }) { B { Text(Queries.minutesLabel(tasks.sumOf { it.minutes })) }; Span({ classes("small", "muted") }) { Text("de estudo planejado") } }
            }
            P({ classes("strong") }) { Text(reveal.coverageLine) }
            if (reveal.weights.isNotEmpty()) {
                Span({ classes("xs", "muted", "strong") }) { Text("PESO DE CADA MATÉRIA NO SEU TEMPO") }
                Div({ classes("pr-weights") }) {
                    reveal.weights.take(6).forEach { (name, pct) ->
                        key(name) {
                            Div({ classes("stack", "tight") }) {
                                Div({ classes("row", "between") }) { B({ classes("small", "clamp-2") }) { Text(name) }; Span({ classes("small", "muted", "nowrap") }) { Text("$pct%") } }
                                ProgressBar(pct / 100.0)
                            }
                        }
                    }
                }
            }
        }
    }
    Div({ classes("week-strip", "preview") }) {
        week.forEach { epoch ->
            key(epoch) {
                val list = firstWeek.filter { it.day == epoch }
                val date = LocalDate.fromEpochDays(epoch)
                Div({ classes(*listOfNotNull("wday", if (epoch == todayEpoch) "today" else null, if (list.isEmpty()) "off" else null).toTypedArray()) }) {
                    Span({ classes("wd") }) { Text(Queries.weekdayShort[date.isoDayOfWeek - 1]) }
                    Span({ classes("dn") }) { Text("${date.day}") }
                    Span({ classes("dots") }) {
                        if (list.isEmpty()) Span({ classes("xs") }) { Text("folga") }
                        else list.take(4).forEach { t -> key(t.id) { I({ classes("ok"); attr("style", "background:${taskColors(t.type).second}") }) } }
                    }
                    if (list.isNotEmpty()) Span({ classes("xs", "wmin") }) { Text(Queries.minutesLabel(list.sumOf { it.minutes })) }
                }
            }
        }
    }
    Div({ classes("grid", "cols-2") }) {
        Card {
            CardHead("Hoje")
            val today = firstWeek.filter { it.day == todayEpoch }
            if (today.isEmpty()) P({ classes("small", "muted") }) { Text("Hoje é folga. Seu plano começa no próximo dia livre.") }
            Div({ classes("stack", "tight") }) {
                today.forEach { t ->
                    key(t.id) {
                        Div({ classes("row") }) {
                            I({ classes("type-dot"); attr("style", "background:${taskColors(t.type).second}") })
                            Div({ classes("grow") }) { B({ classes("small") }) { Text(t.topicName ?: t.subjectName) }; Div({ classes("xs", "muted") }) { Text("${Queries.taskTypeLabel(t.type)} · ${t.subjectName}") } }
                            Span({ classes("small", "muted", "nowrap") }) { Text(Queries.minutesLabel(t.minutes)) }
                        }
                    }
                }
            }
        }
        Card {
            CardHead("Tempo por matéria na semana")
            Div({ classes("stack") }) {
                bySubject.forEach { (name, min) ->
                    key(name) {
                        Div({ classes("stack", "tight") }) {
                            Div({ classes("row", "between") }) { B({ classes("small", "clamp-2") }) { Text(name) }; Span({ classes("small", "muted", "nowrap") }) { Text(Queries.minutesLabel(min)) } }
                            ProgressBar(min.toDouble() / maxMin)
                        }
                    }
                }
            }
        }
    }
    Div({ classes("row", "between") }) {
        Btn("Voltar e ajustar", onBack, style = "ghost", icon = "arrow_back")
        // O foguete desenhado à mão decola do botão, explode na tela e o Folha comemora (rocket.js);
        // depois o plano abre normalmente.
        var launching by remember { mutableStateOf(false) }
        Button({
            attr("type", "button")
            classes("btn", "primary", "rocket-btn")
            if (launching) attr("disabled", "")
            onClick {
                if (!launching) {
                    launching = true
                    val rocket = kotlinx.browser.document.querySelector(".rocket-btn .rocket-ico")
                    val launch = runCatching { kotlinx.browser.window.asDynamic().estudarioRocketLaunch(rocket) }.getOrNull()
                    if (launch == null) onOpen() else launch.then { onOpen() }
                }
            }
        }) {
            Span({
                classes("rocket-ico")
                ref { el -> el.innerHTML = (kotlinx.browser.window.asDynamic().estudarioRocketSvg as? String).orEmpty(); onDispose { } }
            })
            Text("Abrir meu plano")
        }
    }
}

/**
 * A vitrine dos editais: um cabeçalho com o tamanho do catálogo e atalhos das buscas mais comuns,
 * a busca sem limite de resultados, as abas Recentes / Todos, filtros por área e por banca, e a
 * lista que vai carregando sozinha ao rolar (só o que está perto da tela é desenhado, então 2 mil
 * editais não travam nada).
 */
@Composable
private fun CatalogPicker(
    entries: List<CatalogEntry>?,
    error: String?,
    query: String,
    onQuery: (String) -> Unit,
    busyId: String?,
    onPick: (CatalogEntry) -> Unit,
) {
    val year = kotlin.js.Date().getFullYear()
    var tab by remember { mutableStateOf("recentes") }
    var category by remember { mutableStateOf<br.com.estudario.domain.catalog.ExamCategory?>(null) }
    var board by remember { mutableStateOf<String?>(null) }
    var shown by remember(query, category, board, tab) { mutableStateOf(30) }
    // Os filtros ficam recolhidos atrás de um botão só: a busca e a lista logo abaixo dela são o caminho.
    var filtersOpen by remember { mutableStateOf(false) }
    Div({ classes("catalog") }) {
        Div({ classes("cat-hero", "cat-hero-search") }) {
            Label(forId = "cat-q", attrs = { classes("cat-q-label") }) { Text("Qual concurso você vai fazer?") }
            Div({ classes("catalog-search", "big") }) {
                Icon("search", plain = true)
                Input(InputType.Search) {
                    id("cat-q"); classes("input"); placeholder("Ex.: TRT analista, PF agente, PM MG, Banco do Brasil…"); value(query)
                    attr("autofocus", ""); attr("autocomplete", "off")
                    attr("aria-label", "Buscar concurso")
                    onInput { onQuery(it.value) }
                }
                if (query.isNotEmpty()) Button({ classes("icon-btn"); attr("aria-label", "Limpar busca"); onClick { onQuery("") } }) { Icon("close") }
            }
            Span({ classes("cat-q-hint") }) {
                Text("Digite o nome do concurso, órgão ou cargo. Os resultados aparecem logo abaixo.")
                entries?.size?.let { Text(" ${formatThousands(it)} editais prontos.") }
            }
            if (query.isBlank()) Div({ classes("cat-quick") }) {
                Span({ classes("cat-quick-l") }) { Text("Mais procurados:") }
                listOf("TRT", "TJ", "PF", "PRF", "PM", "INSS", "Banco do Brasil", "Caixa", "Receita", "TCU").forEach { q ->
                    Button({ classes("cat-quick-chip"); onClick { onQuery(q) } }) { Text(q) }
                }
            }
        }
        if (entries == null) {
            if (error == null) Div({ classes("catalog-loading") }) { repeat(4) { Div({ classes("exam-card", "skeleton") }) } }
            return@Div
        }
        // Recentes: os editais dos últimos dois anos (os que estão valendo agora); "todos" é um filtro.
        val recent = { e: CatalogEntry -> (e.year ?: 0) >= year - 1 }
        val base = if (tab == "recentes" && query.isBlank()) entries.filter(recent) else entries
        val counts = base.groupingBy { it.category }.eachCount()
        val boards = base.mapNotNull { it.board?.trim()?.takeIf(String::isNotBlank) }.groupingBy { it }.eachCount().entries.sortedByDescending { it.value }.take(8)
        val pool = if (query.isBlank()) base.sortedWith(compareByDescending<CatalogEntry> { it.year ?: 0 }.thenByDescending { it.readyPercent }) else Catalog.search(base, query, limit = Int.MAX_VALUE)
        val list = pool.filter { (category == null || it.category == category) && (board == null || it.board?.trim() == board) }
        val activeFilters = listOfNotNull(category, board, if (tab == "todos") tab else null).size
        Div({ classes("catalog-head") }) {
            B { Text(when {
                list.isEmpty() -> "Nenhum edital encontrado"
                query.isBlank() -> if (tab == "recentes") "Editais recentes" else "Todos os editais"
                else -> "${formatThousands(list.size)} ${if (list.size == 1) "edital" else "editais"} para “${query.trim()}”"
            }) }
            Button({
                classes(*listOfNotNull("cat-filter-btn", if (activeFilters > 0) "on" else null).toTypedArray())
                attr("aria-expanded", filtersOpen.toString())
                onClick { filtersOpen = !filtersOpen }
            }) {
                Icon("tune", plain = true); Text("Filtrar")
                if (activeFilters > 0) Span({ classes("n") }) { Text("$activeFilters") }
            }
        }
        if (filtersOpen) Div({ classes("cat-filters") }) {
            Div({ classes("cat-tabs"); attr("role", "tablist") }) {
                listOf("recentes" to "Recentes · $year e ${year - 1}", "todos" to "Todos os anos").forEach { (k, label) ->
                    Button({ classes(*listOfNotNull("cat-tab", if (tab == k) "on" else null).toTypedArray()); attr("role", "tab"); onClick { tab = k } }) {
                        Text(label); Span({ classes("n") }) { Text(formatThousands(if (k == "recentes") entries.count(recent) else entries.size)) }
                    }
                }
            }
            Div({ classes("cat-chips") }) {
                Button({ classes(*listOfNotNull("fchip", if (category == null) "on" else null).toTypedArray()); onClick { category = null } }) { Text("Todas as áreas") }
                br.com.estudario.domain.catalog.ExamCategory.present(counts.keys).forEach { c ->
                    key(c.name) {
                        Button({ classes(*listOfNotNull("fchip", if (category == c) "on" else null).toTypedArray()); onClick { category = if (category == c) null else c } }) {
                            Text(c.label); Span({ classes("n") }) { Text("${counts[c]}") }
                        }
                    }
                }
            }
            if (boards.size > 1) Div({ classes("cat-boards") }) {
                Span({ classes("cat-quick-l") }) { Text("Banca:") }
                boards.forEach { (b, n) ->
                    Button({ classes(*listOfNotNull("cat-board", if (board == b) "on" else null).toTypedArray()); onClick { board = if (board == b) null else b } }) { Text(b); Span({ classes("n") }) { Text("$n") } }
                }
            }
            if (activeFilters > 0) Button({ classes("cat-filter-clear"); onClick { category = null; board = null; tab = "recentes" } }) { Text("Limpar filtros") }
        }
        if (list.isEmpty()) P({ classes("muted", "small") }) { Text("Ainda não temos esse edital pronto. Envie o PDF do seu edital logo abaixo e eu monto as matérias para você.") }
        Div({ classes("exam-grid") }) {
            list.take(shown).forEach { entry -> key(entry.id) { ExamCard(entry, busyId == entry.id, busyId != null) { onPick(entry) } } }
        }
        // Carrega mais sozinho quando a pessoa chega perto do fim da lista.
        if (list.size > shown) key(shown) { Div({
            classes("cat-more")
            ref { el ->
                val io = js("new IntersectionObserver(function(e){ if (e[0].isIntersecting) el.__more(); }, { rootMargin: '600px' })")
                el.asDynamic().__more = { shown += 30 }
                io.observe(el)
                onDispose { io.disconnect() }
            }
        }) { Spinner(); Span({ classes("small", "muted") }) { Text("Carregando mais editais…") } } }
    }
}

private fun formatThousands(n: Int): String = n.toString().reversed().chunked(3).joinToString(".").reversed()

@Composable
private fun ExamCard(entry: CatalogEntry, loading: Boolean, disabled: Boolean, onClick: () -> Unit) {
    val year = kotlin.js.Date().getFullYear()
    val old = entry.year != null && entry.year < year - 2
    val tone = subjectColor(entry.shortName)
    val ready = entry.readyPercent
    val readyTone = when { ready >= 80 -> "var(--green)"; ready >= 40 -> "var(--primary)"; else -> "var(--amber-strong)" }
    val latest = androidx.compose.runtime.rememberUpdatedState(onClick)
    Button({
        classes(*listOfNotNull("exam-card", if (loading) "loading" else null).toTypedArray())
        attr("style", "--tone:$tone")
        if (disabled) attr("disabled", "")
        onClick { latest.value() }
    }) {
        // O desenho da área (escudo, martelo, banco...) no lugar da sigla solta.
        Span({ classes("exam-mono", "art") }) { Icon(br.com.estudario.domain.catalog.SubjectArt.iconFor(entry.category)) }
        Span({ classes("exam-body") }) {
            Span({ classes("exam-top") }) {
                Span({ classes("exam-short") }) { Text(entry.shortName) }
                entry.year?.let { Span({ classes(*listOfNotNull("exam-year", if (old) "old" else null).toTypedArray()) }) { Text(if (old) "$it · antigo" else "$it") } }
                if (entry.year == year) Span({ classes("exam-new") }) { Text("NOVO") }
            }
            B({ classes("exam-role") }) { Text(entry.role) }
            val details = listOfNotNull(entry.agency.takeIf { !it.equals(entry.shortName, true) }, entry.board).joinToString(" · ")
            if (details.isNotBlank()) Span({ classes("exam-details") }) { Text(details) }
            Span({ classes("exam-size") }) { Text("${entry.category.label} · ${entry.subjectCount} matérias · ${entry.topicCount} tópicos") }
            // Temporário: quanto do edital já tem material pronto, para acompanhar a biblioteca.
            Span({ classes("exam-ready"); attr("style", "--ready:$readyTone") }) {
                Span({ classes("track") }) { Span({ attr("style", "width:${ready.coerceAtLeast(2)}%") }) }
                Span({ classes("pct") }) { Text("$ready% com material") }
            }
        }
        if (loading) Spinner() else Icon("chevron_right", extraClass = "faint", plain = true)
    }
}
