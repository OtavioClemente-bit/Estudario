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
                        if (results.isEmpty()) P({ classes("muted") }) { Text("Nenhum edital encontrado. Por enquanto o site monta edital só a partir do catálogo; no app dá para enviar o PDF do seu edital.") }
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
            P({ classes("muted"); attr("style", "margin:6px 0 14px") }) { Text("Deixe 0 nos dias de folga. Dá para mudar quando quiser.") }
            Div({ classes("grid", "cols-4") }) {
                Queries.weekdayLong.forEachIndexed { index, day ->
                    Div({ classes("field") }) {
                        Label { Text(day.replaceFirstChar(Char::uppercase)) }
                        Input(InputType.Number) {
                            classes("input")
                            value(minutes[index].toString())
                            attr("min", "0"); attr("max", "720"); attr("step", "10")
                            onInput { event -> minutes = minutes.toMutableList().also { it[index] = (event.value?.toInt() ?: 0).coerceIn(0, 720) } }
                        }
                        Span({ classes("small", "muted") }) { Text(Queries.minutesLabel(minutes[index])) }
                    }
                }
            }
            P({ classes("strong"); attr("style", "margin-top:12px") }) { Text("Total: ${Queries.minutesLabel(minutes.sum())} por semana") }
            Nav(onBack = { step = 1 }, onNext = { step = 3 }, enabled = minutes.any { it > 0 })
        }
        else -> Card {
            H2 { Text("Em que momento você está?") }
            Div({ classes("stack"); attr("style", "margin-top:14px") }) {
                StudyProfile.entries.forEach { option ->
                    Button({
                        classes("option", *(if (profile == option) arrayOf("right") else emptyArray()))
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
                val entry = chosen ?: return@Nav
                busy = true
                error = null
                try {
                    Store.update { data ->
                        val (withEdital, competitionId) = Catalog.apply(data, entry, subjects)
                        val subjectChoices = withEdital.subjects.filter { it.competitionId == competitionId }.sortedBy { it.position }
                            .map { Planning.SubjectChoice(it.id, it.name) }
                        Planning.create(
                            withEdital,
                            Planning.NewPlan(
                                competitionId = competitionId,
                                name = "Plano ${entry.shortName}",
                                objective = entry.role,
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
