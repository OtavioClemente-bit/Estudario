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
import br.com.estudario.web.data.Planning
import br.com.estudario.web.data.Queries
import br.com.estudario.web.data.Snapshot
import br.com.estudario.web.data.Store
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.attributes.placeholder
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

private fun priorityLabel(p: PlanPriority) = when (p) { PlanPriority.CRITICAL -> "Essencial"; PlanPriority.HIGH -> "Alta"; PlanPriority.MEDIUM -> "Média"; PlanPriority.LOW -> "Baixa" }

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
    var examDate by remember { mutableStateOf("") }
    var minutes by remember { mutableStateOf(listOf(120, 120, 120, 120, 120, 180, 0)) }
    var axes by remember { mutableStateOf(mapOf<String, SubjectAxes>()) }
    var profile by remember { mutableStateOf(StudyProfile.DO_ZERO) }
    var busy by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    var preview by remember { mutableStateOf<Pair<Snapshot, String>?>(null) }
    var building by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        try { entries = Catalog.entries() } catch (e: Throwable) { error = e.message }
    }

    // O edital escolhido, num formato só: (matéria, tópicos).
    val edital: List<Pair<String, List<String>>> = aiEdital?.third?.map { it.name to flatten(it.topics) } ?: subjects.map { it.name to it.topics }
    val editalName = chosen?.shortName ?: aiEdital?.first.orEmpty()

    fun build(data: Snapshot): Pair<Snapshot, String> {
        val entry = chosen
        val ai = aiEdital
        val (withEdital, competitionId) = if (entry != null) Catalog.apply(data, entry, subjects) else EditalAi.apply(data, ai!!.first, ai.third)
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
                method = StudyMethodConfig.forProfile(profile),
            ),
        )
    }

    // Tela final: o Folha montando e a prévia do plano.
    if (building || preview != null) {
        PlanPreview(preview, onBack = { preview = null; building = false }) {
            try {
                Store.update { build(it).first }
                Router.go(Route.Plan)
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
                P({ classes("small", "muted") }) { Text("Procure pelo órgão, cargo ou sigla, como “pm mg”, “trt analista” ou “pf agente”.") }
            }
            CatalogPicker(entries, error, query, { query = it }, busyId = if (busy) chosen?.id else null) { entry ->
                busy = true; chosen = entry; aiEdital = null; error = null
                scope.launch {
                    try { subjects = Catalog.subjects(entry.id); axes = emptyMap(); step = 1 } catch (e: Throwable) { error = e.message } finally { busy = false }
                }
            }
            Div({ classes("card", "soft") }) {
                Div({ classes("row", "wrap", "between") }) {
                    Div({ classes("grow"); attr("style", "min-width:220px") }) {
                        H3 { Text("Não achou o seu concurso?") }
                        P({ classes("small", "muted") }) { Text("Envie o PDF do edital: eu monto as matérias e os tópicos para você.") }
                    }
                    Btn("Enviar PDF do edital", { pdfOpen = true }, style = "tonal", icon = "upload_file", enabled = !Store.demo)
                }
            }
            if (pdfOpen) PdfEditalDialog(onClose = { pdfOpen = false }) { name, role, nodes -> aiEdital = Triple(name, role, nodes); chosen = null; axes = emptyMap(); pdfOpen = false; step = 1 }
        }
        1 -> {
            val topicCount = edital.sumOf { it.second.size }
            FolhaSays("point", 88) {
                H2 { Text("Confere comigo o edital de $editalName") }
                P({ classes("small", "muted") }) { Text("${edital.size} matérias e $topicCount tópicos. Toque numa matéria para ver os tópicos.") }
            }
            Card {
                Div({ classes("stack", "tight") }) {
                    edital.forEachIndexed { index, (name, topics) ->
                        key(name + index) { EditalSubjectRow(index + 1, name, topics) }
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
                    Input(InputType.Date) {
                        classes("input"); attr("style", "max-width:220px"); value(examDate); attr("min", today.toString())
                        onInput { examDate = it.value }
                    }
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
                WeeklyHoursEditor(minutes) { minutes = it }
                P({ classes("small", "muted", "strong"); attr("style", "margin-top:10px;text-align:right") }) { Text("${Queries.minutesLabel(total)} por semana") }
            }
            NextBar("Continuar", enabled = minutes.any { it > 0 }) { step = 4 }
        }
        4 -> {
            FolhaSays("thinking", 88) {
                H2 { Text("Como você está em cada matéria?") }
                P({ classes("small", "muted") }) { Text("Com isso eu dou mais tempo ao que é prioridade, ao que é difícil para você e ao que você ainda não conhece.") }
            }
            Card {
                Div({ classes("axes-head") }) {
                    Span { Text("Matéria") }; Span { Text("Prioridade") }; Span { Text("Dificuldade para você") }; Span { Text("O quanto já sabe") }
                }
                edital.forEach { (name, _) ->
                    key(name) {
                        val a = axes[name] ?: SubjectAxes()
                        Div({ classes("axes-row") }) {
                            B({ classes("small", "clamp-2") }) { Text(name) }
                            Div({ classes("segmented", "tiny") }) {
                                listOf(PlanPriority.HIGH, PlanPriority.MEDIUM, PlanPriority.LOW).forEach { p ->
                                    Button({ classes(*listOfNotNull(if (a.priority == p) "on" else null).toTypedArray()); onClick { axes = axes + (name to a.copy(priority = p)) } }) { Text(priorityLabel(p)) }
                                }
                            }
                            Select({ classes("input", "small"); attr("aria-label", "Dificuldade em $name"); onChange { v -> v.value?.let { axes = axes + (name to a.copy(difficulty = PersonalDifficulty.valueOf(it))) } } }) {
                                PersonalDifficulty.entries.forEach { d -> Option(d.name, { if (a.difficulty == d) selected() }) { Text(d.label) } }
                            }
                            Select({ classes("input", "small"); attr("aria-label", "Conhecimento em $name"); onChange { v -> v.value?.let { axes = axes + (name to a.copy(knowledge = InitialKnowledge.valueOf(it))) } } }) {
                                InitialKnowledge.entries.forEach { k -> Option(k.name, { if (a.knowledge == k) selected() }) { Text(k.label) } }
                            }
                        }
                    }
                }
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
                    delay(1600) // tempo do Folha "montando", como no app
                    try { preview = build(Store.data) } catch (e: Throwable) { error = e.message ?: "Não deu para montar o plano."; building = false }
                }
            }
        }
    }
}

@Composable
private fun NextBar(label: String, enabled: Boolean = true, icon: String = "arrow_forward", onNext: () -> Unit) {
    Div({ classes("row"); attr("style", "justify-content:flex-end") }) { Btn(label, onNext, icon = icon, enabled = enabled, style = "primary") }
}

@Composable
private fun EditalSubjectRow(number: Int, name: String, topics: List<String>) {
    var open by remember { mutableStateOf(false) }
    Div({ classes("edital-row") }) {
        Button({ classes("pick-row", "flat"); attr("aria-expanded", "$open"); onClick { open = !open } }) {
            Span({ classes("num") }) { Text("$number") }
            Span({ classes("grow") }) { B { Text(name) }; Span({ classes("hint") }) { Text("${topics.size} tópicos") } }
            Icon(if (open) "expand_less" else "expand_more", extraClass = "faint")
        }
        if (open) Div({ classes("edital-topics") }) {
            topics.forEach { t -> Div({ attr("style", "padding-left:${(t.length - t.trimStart().length) * 6}px") }) { Text(t.trim()) } }
        }
    }
}

/** O Folha montando o plano e, em seguida, a prévia da primeira semana antes de abrir. */
@Composable
private fun PlanPreview(result: Pair<Snapshot, String>?, onBack: () -> Unit, onOpen: () -> Unit) {
    if (result == null) {
        Div({ classes("center-page", "building") }) {
            ProcessView(
                title = "Montando o seu plano",
                eyebrow = "Quase lá",
                stages = listOf("Lendo o seu edital", "Distribuindo as matérias pelos seus dias", "Encaixando revisões e questões", "Conferindo a primeira semana"),
                stageMillis = 1_400,
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
        Btn("Abrir meu plano", onOpen, style = "primary", icon = "rocket_launch")
    }
}

/**
 * A escolha do edital: busca, filtro por área (com a quantidade de cada uma) e a lista em cartões,
 * dos editais mais completos para os menos. Cada cartão traz a sigla num selo colorido, o cargo,
 * órgão, banca e ano, o tamanho do edital e quanto dele já tem material pronto.
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
    var category by remember { mutableStateOf<br.com.estudario.domain.catalog.ExamCategory?>(null) }
    var shown by remember(query, category) { mutableStateOf(24) }
    Div({ classes("catalog") }) {
        Div({ classes("catalog-search") }) {
            Icon("search", plain = true)
            Input(InputType.Search) {
                classes("input"); placeholder("Buscar: pm mg, trt analista, pf agente…"); value(query)
                attr("aria-label", "Buscar edital")
                onInput { onQuery(it.value) }
            }
            if (query.isNotEmpty()) Button({ classes("icon-btn"); attr("aria-label", "Limpar busca"); onClick { onQuery("") } }) { Icon("close", plain = true) }
        }
        if (entries == null) {
            if (error == null) Div({ classes("catalog-loading") }) { repeat(4) { Div({ classes("exam-card", "skeleton") }) } }
            return@Div
        }
        val counts = entries.groupingBy { it.category }.eachCount()
        Div({ classes("cat-chips"); attr("role", "tablist") }) {
            Button({ classes(*listOfNotNull("fchip", if (category == null) "on" else null).toTypedArray()); onClick { category = null } }) {
                Text("Todos"); Span({ classes("n") }) { Text("${entries.size}") }
            }
            br.com.estudario.domain.catalog.ExamCategory.present(counts.keys).forEach { c ->
                key(c.name) {
                    Button({ classes(*listOfNotNull("fchip", if (category == c) "on" else null).toTypedArray()); onClick { category = if (category == c) null else c } }) {
                        Text(c.label); Span({ classes("n") }) { Text("${counts[c]}") }
                    }
                }
            }
        }
        val pool = if (query.isBlank()) entries.sortedWith(compareByDescending<CatalogEntry> { it.readyPercent }.thenByDescending { it.year ?: 0 }) else Catalog.search(entries, query, limit = 200)
        val list = if (category == null) pool else pool.filter { it.category == category }
        Div({ classes("catalog-head") }) {
            B { Text(if (list.isEmpty()) "Nenhum edital encontrado" else if (query.isNotBlank()) "${list.size} ${if (list.size == 1) "edital encontrado" else "editais encontrados"}" else "${list.size} ${if (list.size == 1) "edital pronto" else "editais prontos"}") }
            if (query.isBlank() && list.size > 1) Span({ classes("xs", "muted") }) { Text("mais completos primeiro") }
        }
        if (list.isEmpty()) P({ classes("muted", "small") }) { Text("Ainda não temos esse edital pronto. Envie o PDF do seu edital logo abaixo e eu monto as matérias para você.") }
        Div({ classes("exam-grid") }) {
            list.take(shown).forEach { entry -> key(entry.id) { ExamCard(entry, busyId == entry.id, busyId != null) { onPick(entry) } } }
        }
        if (list.size > shown) Div({ classes("row"); attr("style", "justify-content:center") }) {
            Btn("Mostrar mais ${minOf(24, list.size - shown)} de ${list.size - shown}", { shown += 24 }, style = "outline", icon = "expand_more")
        }
    }
}

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
        val mono = br.com.estudario.domain.catalog.ExamCategory.monogram(entry.shortName)
        Span({ classes(*listOfNotNull("exam-mono", if (mono.length >= 5) "long" else null).toTypedArray()) }) { Text(mono) }
        Span({ classes("exam-body") }) {
            Span({ classes("exam-top") }) {
                Span({ classes("exam-short") }) { Text(entry.shortName) }
                entry.year?.let { Span({ classes(*listOfNotNull("exam-year", if (old) "old" else null).toTypedArray()) }) { Text(if (old) "$it · antigo" else "$it") } }
            }
            B({ classes("exam-role") }) { Text(entry.role) }
            val details = listOfNotNull(entry.agency.takeIf { !it.equals(entry.shortName, true) }, entry.board).joinToString(" · ")
            if (details.isNotBlank()) Span({ classes("exam-details") }) { Text(details) }
            Span({ classes("exam-size") }) { Text("${entry.subjectCount} matérias · ${entry.topicCount} tópicos") }
            // Temporário: quanto do edital já tem material pronto, para acompanhar a biblioteca.
            Span({ classes("exam-ready"); attr("style", "--ready:$readyTone") }) {
                Span({ classes("track") }) { Span({ attr("style", "width:${ready.coerceAtLeast(2)}%") }) }
                Span({ classes("pct") }) { Text("$ready% com material") }
            }
        }
        if (loading) Spinner() else Icon("chevron_right", extraClass = "faint", plain = true)
    }
}
