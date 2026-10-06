package br.com.estudario.web.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import br.com.estudario.domain.planner.PlanPriority
import br.com.estudario.domain.planner.StudyProfile
import br.com.estudario.web.Route
import br.com.estudario.web.Router
import br.com.estudario.web.data.Planning
import br.com.estudario.web.data.Queries
import br.com.estudario.web.data.Store
import kotlinx.datetime.LocalDate
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H3
import org.jetbrains.compose.web.dom.Input
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text

private val priorityLabels = listOf(
    PlanPriority.CRITICAL to "Crítica",
    PlanPriority.HIGH to "Alta",
    PlanPriority.MEDIUM to "Média",
    PlanPriority.LOW to "Baixa",
)

/** Horas de cada dia com mais e menos (10 em 10 minutos) e atalhos de duração. */
@Composable
fun WeeklyHoursEditor(minutes: List<Int>, onChange: (List<Int>) -> Unit) {
    val current = androidx.compose.runtime.rememberUpdatedState(minutes)
    val latest = androidx.compose.runtime.rememberUpdatedState(onChange)
    fun set(index: Int, value: Int) = latest.value(current.value.toMutableList().also { it[index] = value.coerceIn(0, 720) })
    Div {
        Queries.weekdayLong.forEachIndexed { index, day ->
            androidx.compose.runtime.key(index) {
                val value = minutes[index]
                Div({ classes(*listOfNotNull("day-row", if (value == 0) "off" else null).toTypedArray()) }) {
                    Div({ classes("grow") }) {
                        Div({ classes("name") }) { Text(day.replaceFirstChar(Char::uppercase)) }
                        Div({ classes("hint") }) { Text(if (value == 0) "Folga" else "${Queries.minutesLabel(value)} de estudo") }
                    }
                    Stepper(value, { set(index, it) }, step = 10, min = 0, max = 720, format = { if (it == 0) "Folga" else Queries.minutesLabel(it) }, label = "Tempo de $day")
                }
            }
        }
    }
    Div({ classes("row", "between", "wrap"); attr("style", "margin-top:12px") }) {
        Span({ classes("strong") }) { Text("Total: ${Queries.minutesLabel(minutes.sum())} por semana") }
        Div({ classes("quick-picks") }) {
            listOf(60 to "1 h por dia", 120 to "2 h por dia", 180 to "3 h por dia").forEach { (perDay, label) ->
                Btn(label, { latest.value(List(7) { if (it < 6) perDay else 0 }) }, style = "outline", small = true)
            }
        }
    }
}

@Composable
fun PlanSettingsScreen() {
    val data = Store.data
    val plan = Queries.activePlan(data)
    if (plan == null) { Router.go(Route.Plan); return }
    val saved = (1..7).map { day -> data.availability.firstOrNull { it.planId == plan.id && it.day == day }?.let { if (it.unavailable) 0 else it.minutes } ?: 0 }
    var minutes by remember(plan.id) { mutableStateOf(saved) }
    val planSubjects = data.planSubjects.filter { it.planId == plan.id }.sortedBy { it.position }
    var examDate by remember(plan.id) { mutableStateOf(plan.exam?.let { LocalDate.fromEpochDays(it).toString() } ?: "") }
    var profile by remember(plan.id) { mutableStateOf(StudyProfile.entries.firstOrNull { it.name == plan.profile } ?: StudyProfile.DO_ZERO) }
    var error by remember { mutableStateOf<String?>(null) }

    Div({ classes("row") }) { Btn("Plano", { Router.go(Route.Plan) }, style = "ghost", small = true, icon = "arrow_back") }
    PageHead("Ajustar plano", "Mudou a rotina? O plano se reorganiza com as novas horas, sem perder o que você já fez.")
    error?.let { Div({ classes("banner", "error") }) { Text(it) } }

    Div({ classes("grid", "main-side") }) {
        Card {
            CardHead("Horas de estudo por dia")
            WeeklyHoursEditor(minutes) { minutes = it }
            Div({ classes("row", "end"); attr("style", "margin-top:16px") }) {
                Btn("Salvar horas", {
                    try {
                        Store.update { Planning.updateAvailability(it, plan.id, minutes) }
                        Toast.show("Horas salvas. O plano foi reorganizado.")
                        error = null
                    } catch (e: Throwable) { error = e.message }
                }, icon = "check", enabled = minutes != saved && minutes.any { it > 0 })
            }
        }
        Div({ classes("stack", "loose") }) {
            Card {
                CardHead("Prova e momento")
                Div({ classes("stack") }) {
                    Div({ classes("field") }) {
                        org.jetbrains.compose.web.dom.Label { Text("Data da prova") }
                        Input(InputType.Date) { classes("input"); value(examDate); attr("min", Queries.todayDate().toString()); onInput { examDate = it.value } }
                    }
                    Div({ classes("stack", "tight") }) {
                        StudyProfile.entries.forEach { option ->
                            androidx.compose.runtime.key(option) {
                                Button({ classes(*listOfNotNull("option", if (profile == option) "chosen" else null).toTypedArray()); onClick { profile = option } }) {
                                    Span({ classes("key") }) { Icon(if (profile == option) "check" else "circle") }
                                    Div { H3 { Text(option.label) }; P({ classes("small", "muted") }) { Text(option.summary) } }
                                }
                            }
                        }
                    }
                    Btn("Salvar e recalcular", {
                        val exam = examDate.takeIf { it.isNotBlank() }?.let { runCatching { LocalDate.parse(it) }.getOrNull() }
                        Store.update { Planning.updateMethod(it, plan.id, exam, profile) }
                        Toast.show("Plano recalculado.")
                    }, style = "tonal", icon = "autorenew", block = true)
                }
            }
        }
    }

    Card {
        CardHead("Matérias do plano")
        P({ classes("small", "muted"); attr("style", "margin:-8px 0 12px") }) { Text("A prioridade diz o quanto a matéria pesa na prova. Pausar tira a matéria do plano até você voltar com ela.") }
        Div({ classes("stack", "tight") }) {
            planSubjects.forEach { subject ->
                androidx.compose.runtime.key(subject.subjectId) {
                    val priority = PlanPriority.entries.firstOrNull { it.name == subject.priority } ?: PlanPriority.MEDIUM
                    Div({ classes("day-row"); attr("style", "flex-wrap:wrap") }) {
                        Div({ classes("grow"); attr("style", "min-width:180px") }) {
                            Div({ classes("name") }) { Text(subject.name) }
                            Div({ classes("hint") }) { Text(if (subject.paused) "Pausada" else "Prioridade ${priorityLabels.first { it.first == priority }.second.lowercase()}") }
                        }
                        Div({ classes("row", "wrap") }) {
                            Div({ classes("segmented") }) {
                                priorityLabels.forEach { (option, label) ->
                                    Button({
                                        classes(*listOfNotNull(if (priority == option) "on" else null).toTypedArray())
                                        onClick { if (option != priority) Store.update { Planning.updateSubject(it, plan.id, subject.subjectId, option, subject.paused) } }
                                    }) { Text(label) }
                                }
                            }
                            Div({ classes("row"); attr("title", "Pausar matéria") }) {
                                Span({ classes("small", "muted") }) { Text("Pausar") }
                                Switch(subject.paused, "Pausar ${subject.name}") { paused -> Store.update { Planning.updateSubject(it, plan.id, subject.subjectId, priority, paused) } }
                            }
                        }
                    }
                }
            }
        }
    }
}
