package br.com.estudario.web.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import br.com.estudario.domain.planner.StudyMethodConfig
import br.com.estudario.domain.planner.StudyProfile
import br.com.estudario.web.Route
import br.com.estudario.web.Router
import br.com.estudario.web.data.Catalog
import br.com.estudario.web.data.CatalogEntry
import br.com.estudario.web.data.CatalogSubject
import br.com.estudario.web.data.Planning
import br.com.estudario.web.data.Queries
import br.com.estudario.web.data.Store
import br.com.estudario.web.data.str
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.attributes.placeholder
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H2
import org.jetbrains.compose.web.dom.H3
import org.jetbrains.compose.web.dom.Input
import org.jetbrains.compose.web.dom.Label
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text

/**
 * Configurar os estudos direto no navegador, sem o app: escolher o edital pronto do catálogo,
 * a data da prova, as horas de cada dia e o momento do estudo. No fim o plano é montado pelo mesmo
 * motor do app.
 */
@Composable
fun SetupScreen() {
    val scope = rememberCoroutineScope()
    var step by remember { mutableStateOf(0) }
    var entries by remember { mutableStateOf<List<CatalogEntry>?>(null) }
    var query by remember { mutableStateOf("") }
    var chosen by remember { mutableStateOf<CatalogEntry?>(null) }
    var subjects by remember { mutableStateOf<List<CatalogSubject>>(emptyList()) }
    // Edital montado pela IA a partir do PDF (nome do concurso, cargo, matérias).
    var aiEdital by remember { mutableStateOf<Triple<String, String, List<br.com.estudario.web.data.EditalAi.SubjectNode>>?>(null) }
    var pdfOpen by remember { mutableStateOf(false) }
    var examDate by remember { mutableStateOf("") }
    var minutes by remember { mutableStateOf(listOf(120, 120, 120, 120, 120, 180, 0)) }
    var profile by remember { mutableStateOf(StudyProfile.DO_ZERO) }
    var busy by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        try { entries = Catalog.entries() } catch (e: Throwable) { error = e.message }
    }

    PageHead("Configurar meus estudos", "Em 4 passos o Estudário monta seu plano, igual no app")
    Div({ classes("row", "wrap") }) {
        listOf("Edital", "Data da prova", "Rotina", "Momento").forEachIndexed { index, label ->
            Chip("${index + 1}. $label", if (index == step) "primary" else if (index < step) "green" else null)
        }
    }
    error?.let { Div({ classes("banner", "error") }) { Text(it) } }

    when (step) {
        0 -> Card {
            H2 { Text("Qual concurso você vai prestar?") }
            P({ classes("muted"); attr("style", "margin:6px 0 14px") }) { Text("Procure pelo órgão, cargo ou sigla (ex.: \"pm mg\", \"trt analista\", \"pf agente\").") }
            Input(InputType.Search) {
                classes("input")
                attr("style", "width:100%")
                placeholder("Buscar edital…")
                value(query)
                onInput { query = it.value }
            }
            val list = entries
            when {
                list == null && error == null -> Div({ classes("row"); attr("style", "margin-top:16px") }) { Spinner(); Text("Carregando editais…") }
                list != null -> {
                    val results = if (query.isBlank()) list.take(12) else Catalog.search(list, query)
                    Div({ classes("stack"); attr("style", "margin-top:14px") }) {
                        if (results.isEmpty()) P({ classes("muted") }) { Text("Nenhum edital pronto com esse nome. Você pode enviar o PDF do seu edital logo abaixo.") }
                        results.forEach { entry -> androidx.compose.runtime.key(entry.id) {
                            Div({ classes("task") }) {
                                Div({ classes("task-body") }) {
                                    Div({ classes("task-title") }) { Text(entry.title) }
                                    Div({ classes("task-meta") }) { Text("${entry.details} · ${entry.subjectCount} matérias, ${entry.topicCount} tópicos") }
                                }
                                Btn(if (busy && chosen == entry) "Carregando…" else "Escolher", {
                                    busy = true; chosen = entry; error = null
                                    scope.launch {
                                        try { subjects = Catalog.subjects(entry.id); step = 1 } catch (e: Throwable) { error = e.message } finally { busy = false }
                                    }
                                }, style = "tonal", small = true, enabled = !busy)
                            }
                        } }
                    }
                }
            }
            Div({ classes("card", "soft"); attr("style", "margin-top:16px") }) {
                Div({ classes("row", "wrap", "between") }) {
                    Div({ classes("grow"); attr("style", "min-width:220px") }) {
                        H3 { Text("Não achou o seu concurso?") }
                        P({ classes("small", "muted") }) { Text("Envie o PDF do edital: a IA do Estudário monta as matérias e os tópicos para você.") }
                    }
                    Btn("Enviar PDF do edital", { pdfOpen = true }, style = "tonal", icon = "upload_file", enabled = !Store.demo)
                }
            }
            if (pdfOpen) PdfEditalDialog(onClose = { pdfOpen = false }) { name, role, nodes -> aiEdital = Triple(name, role, nodes); chosen = null; pdfOpen = false; step = 1 }
        }
        1 -> Card {
            H2 { Text("Quando é a prova?") }
            P({ classes("muted"); attr("style", "margin:6px 0 14px") }) { Text("Com a data, o plano divide o tempo em base, aprofundamento e reta final. Sem data, você pode definir depois.") }
            Input(InputType.Date) {
                classes("input")
                value(examDate)
                attr("min", Queries.todayDate().toString())
                onInput { examDate = it.value }
            }
            Nav(onBack = { step = 0 }, onNext = { step = 2 }, nextLabel = if (examDate.isBlank()) "Ainda não sei" else "Continuar")
        }
        2 -> Card {
            H2 { Text("Quanto tempo por dia?") }
            P({ classes("muted"); attr("style", "margin:6px 0 14px") }) { Text("Use o menos e o mais em cada dia. Dia sem tempo vira folga. Dá para mudar quando quiser.") }
            WeeklyHoursEditor(minutes) { minutes = it }
            Nav(onBack = { step = 1 }, onNext = { step = 3 }, enabled = minutes.any { it > 0 })
        }
        else -> Card {
            H2 { Text("Em que momento você está?") }
            Div({ classes("stack"); attr("style", "margin-top:14px") }) {
                StudyProfile.entries.forEach { option ->
                    Button({
                        classes(*listOfNotNull("option", if (profile == option) "chosen" else null).toTypedArray())
                        onClick { profile = option }
                    }) {
                        Span({ classes("key") }) { Icon(if (profile == option) "check" else "radio_button_unchecked") }
                        Div {
                            H3 { Text(option.label) }
                            P({ classes("small") }) { Text(option.summary) }
                        }
                    }
                }
            }
            Nav(onBack = { step = 2 }, onNext = {
                val entry = chosen
                val ai = aiEdital
                if (entry == null && ai == null) return@Nav
                busy = true
                error = null
                try {
                    Store.update { data ->
                        val (withEdital, competitionId) = if (entry != null) Catalog.apply(data, entry, subjects) else br.com.estudario.web.data.EditalAi.apply(data, ai!!.first, ai.third)
                        val subjectChoices = withEdital.subjects.filter { it.competitionId == competitionId }.sortedBy { it.position }
                            .map { Planning.SubjectChoice(it.id, it.name) }
                        Planning.create(
                            withEdital,
                            Planning.NewPlan(
                                competitionId = competitionId,
                                name = "Plano ${entry?.shortName ?: ai!!.first}",
                                objective = entry?.role ?: ai!!.second.ifBlank { ai.first },
                                startDate = Queries.todayDate(),
                                examDate = examDate.takeIf { it.isNotBlank() }?.let { runCatching { LocalDate.parse(it) }.getOrNull() },
                                weeklyMinutes = minutes,
                                subjects = subjectChoices,
                                method = StudyMethodConfig.forProfile(profile),
                            ),
                        ).first
                    }
                    Router.go(Route.Plan)
                } catch (e: Throwable) {
                    error = e.message ?: "Não deu para montar o plano."
                } finally {
                    busy = false
                }
            }, nextLabel = if (busy) "Montando seu plano…" else "Montar meu plano", enabled = !busy)
        }
    }
}

@Composable
private fun Nav(onBack: () -> Unit, onNext: () -> Unit, nextLabel: String = "Continuar", enabled: Boolean = true) {
    Div({ classes("row", "between"); attr("style", "margin-top:20px") }) {
        Btn("Voltar", onBack, style = "ghost", icon = "arrow_back")
        Btn(nextLabel, onNext, icon = "arrow_forward", enabled = enabled)
    }
}

/** Enviar o PDF do edital: a IA monta matérias e tópicos (como "Montar edital com IA" no app). */
@Composable
private fun PdfEditalDialog(onClose: () -> Unit, onDone: (String, String, List<br.com.estudario.web.data.EditalAi.SubjectNode>) -> Unit) {
    val scope = rememberCoroutineScope()
    var file by remember { mutableStateOf<org.w3c.files.File?>(null) }
    var name by remember { mutableStateOf("") }
    var role by remember { mutableStateOf("") }
    var board by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("") }
    var step by remember { mutableStateOf<br.com.estudario.web.data.EditalAi.Step?>(null) }
    var error by remember { mutableStateOf<String?>(null) }
    var preview by remember { mutableStateOf<List<br.com.estudario.web.data.EditalAi.SubjectNode>?>(null) }
    val running = step != null && preview == null && error == null
    Modal(onDismiss = { if (!running) onClose() }) {
        H2 { Text("Montar edital com IA") }
        val result = preview
        when {
            result != null -> {
                P({ classes("muted") }) { Text("A IA encontrou ${result.size} matérias e ${result.sumOf { s -> s.topics.size }} tópicos. Confira e continue.") }
                Div({ attr("style", "max-height:40vh;overflow:auto;border:1px solid var(--line);border-radius:14px;padding:8px 14px") }) {
                    result.forEach { s ->
                        Div({ attr("style", "padding:8px 0;border-bottom:1px solid var(--line)") }) {
                            org.jetbrains.compose.web.dom.B { Text(s.name) }
                            P({ classes("small", "muted") }) { Text(s.topics.joinToString(" · ") { it.name }.take(400)) }
                        }
                    }
                }
                Div({ classes("row", "end") }) {
                    Btn("Cancelar", onClose, style = "ghost")
                    Btn("Usar este edital", { onDone(name.ifBlank { file?.name?.substringBeforeLast('.') ?: "Meu concurso" }, role, result) }, icon = "check")
                }
            }
            running -> Div({ classes("stack"); attr("style", "align-items:center;text-align:center;padding:16px 0") }) {
                Spinner()
                P({ classes("muted") }) {
                    Text(
                        when (step) {
                            br.com.estudario.web.data.EditalAi.Step.Reading -> "Lendo o PDF no seu navegador…"
                            br.com.estudario.web.data.EditalAi.Step.Uploading -> "Enviando para a IA do Estudário…"
                            else -> "A IA está organizando o conteúdo programático. Pode levar alguns minutos; deixe esta aba aberta."
                        },
                    )
                }
            }
            else -> {
                P({ classes("muted", "small") }) { Text("Escolha o PDF oficial do edital. O Estudário lê o conteúdo programático e cria as matérias e os tópicos na ordem do edital.") }
                Div({ classes("field") }) {
                    org.jetbrains.compose.web.dom.Label { Text("PDF do edital") }
                    Input(InputType.File) {
                        classes("input"); attr("accept", "application/pdf"); attr("style", "padding-top:10px")
                        onChange { event -> file = (event.target.asDynamic().files?.item(0)) as? org.w3c.files.File }
                    }
                }
                Div({ classes("field") }) { org.jetbrains.compose.web.dom.Label { Text("Nome do concurso") }; Input(InputType.Text) { classes("input"); value(name); placeholder("Ex.: TJSP Escrevente 2026"); onInput { name = it.value } } }
                Div({ classes("grid", "cols-2") }) {
                    Div({ classes("field") }) { org.jetbrains.compose.web.dom.Label { Text("Cargo (opcional)") }; Input(InputType.Text) { classes("input"); value(role); onInput { role = it.value } } }
                    Div({ classes("field") }) { org.jetbrains.compose.web.dom.Label { Text("Banca (opcional)") }; Input(InputType.Text) { classes("input"); value(board); onInput { board = it.value } } }
                }
                error?.let { Div({ classes("banner", "error") }) { Text(it) } }
                Div({ classes("row", "end") }) {
                    Btn("Cancelar", onClose, style = "ghost")
                    Btn("Montar edital", {
                        val chosenFile = file ?: run { error = "Escolha o PDF do edital."; return@Btn }
                        error = null
                        step = br.com.estudario.web.data.EditalAi.Step.Reading
                        scope.launch {
                            try {
                                val proposal = br.com.estudario.web.data.EditalAi.generate(chosenFile, br.com.estudario.web.data.EditalAi.Options(name, role, board, year)) { step = it }
                                val nodes = br.com.estudario.web.data.EditalAi.parse(proposal)
                                if (nodes.isEmpty()) error = "A IA não encontrou matérias neste PDF. Confira se é o edital com o conteúdo programático."
                                else { if (name.isBlank()) name = proposal.str("documentTitle")?.take(120) ?: ""; preview = nodes }
                            } catch (e: Throwable) {
                                error = e.message ?: "Não deu para montar o edital."
                            } finally { if (preview == null) step = null }
                        }
                    }, icon = "auto_awesome", enabled = file != null)
                }
            }
        }
    }
}
