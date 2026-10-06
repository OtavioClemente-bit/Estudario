package br.com.estudario.web.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import br.com.estudario.time.isoDayOfWeek
import br.com.estudario.time.toEpochDay
import br.com.estudario.web.Route
import br.com.estudario.web.Router
import br.com.estudario.web.data.PlanTask
import br.com.estudario.web.data.Progress
import br.com.estudario.web.data.Queries
import br.com.estudario.web.data.Store
import br.com.estudario.web.data.str
import kotlinx.datetime.LocalDate
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.longOrNull
import org.jetbrains.compose.web.dom.B
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H2
import org.jetbrains.compose.web.dom.Li
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.dom.Ul

private enum class PlanView(val label: String) { TODAY("Hoje"), WEEK("Semana"), MONTH("Mês") }

@Composable
fun PlanScreen() {
    val data = Store.data
    val plan = Queries.activePlan(data)
    var view by remember { mutableStateOf(PlanView.TODAY) }
    var dayOffset by remember { mutableStateOf(0) }
    var weekOffset by remember { mutableStateOf(0) }
    var selectedDay by remember { mutableStateOf(Queries.todayEpoch()) }
    var monthOffset by remember { mutableStateOf(0) }
    var completing by remember { mutableStateOf<PlanTask?>(null) }
    var whyOpen by remember { mutableStateOf(false) }

    if (plan == null) {
        PageHead("Plano", "Meu plano de estudos")
        Card {
            Empty("calendar_month", "Nenhum plano ativo", "Monte seu plano aqui mesmo: escolha o edital, a data da prova e as horas de cada dia.", action = {
                Btn("Configurar meus estudos", { Router.go(Route.Setup) }, icon = "auto_awesome")
            })
        }
        return
    }

    val today = Queries.todayDate()
    val todayEpoch = today.toEpochDay()
    val planTasks = data.tasks.filter { it.planId == plan.id }

    PageHead(plan.name, plan.exam?.let { "Prova em ${Queries.dayLabel(LocalDate.fromEpochDays(it))} · faltam ${(it - todayEpoch).coerceAtLeast(0)} dias" } ?: "Meu plano de estudos") {
        Btn("Ajustar plano", { Router.go(Route.PlanSettings) }, style = "outline", icon = "tune", small = true)
    }

    Div({ classes("tabs"); attr("role", "tablist") }) {
        PlanView.entries.forEach { option ->
            Button({
                classes(*listOfNotNull(if (view == option) "on" else null).toTypedArray())
                attr("role", "tab"); attr("aria-selected", (view == option).toString())
                onClick { view = option }
            }) { Text(option.label) }
        }
    }

    when (view) {
        PlanView.TODAY -> {
            val day = todayEpoch + dayOffset
            val dayTasks = Queries.tasksOn(data, plan.id, day)
            val planned = dayTasks.sumOf { it.minutes }
            val realized = data.executions.filter { it.planId == plan.id && Queries.dateOf(it.completedAt).toEpochDay() == day }.sumOf { it.minutes }
            Div({ classes("row", "between", "wrap") }) {
                Div({ classes("row") }) {
                    IconButton("chevron_left", "Dia anterior") { dayOffset-- }
                    B { Text(dayTitle(day, todayEpoch)) }
                    IconButton("chevron_right", "Próximo dia") { dayOffset++ }
                    if (dayOffset != 0) Btn("Hoje", { dayOffset = 0 }, style = "ghost", small = true)
                }
                Div({ classes("row", "wrap") }) {
                    Chip("${Queries.minutesLabel(planned)} planejadas", "outline")
                    Chip("${Queries.minutesLabel(realized)} realizadas", "outline")
                }
            }
            val done = dayTasks.count { it.status == "CONCLUIDA" }
            val earned = dayTasks.filter { it.status == "CONCLUIDA" }.sumOf { Progress.taskXp(it) }
            val possible = dayTasks.sumOf { Progress.taskXp(it) }
            if (dayTasks.isNotEmpty()) Card {
                Div({ classes("row", "between") }) {
                    Div({ classes("row") }) {
                        Span({ classes("task-mark"); attr("style", "background:var(--amber-soft);color:var(--amber-strong)") }) { Icon("bolt", filled = true) }
                        Div {
                            org.jetbrains.compose.web.dom.H3 { Text(if (dayOffset == 0) "Missão de hoje" else "Missão do dia") }
                            P({ classes("small", "muted") }) { Text("$done de ${dayTasks.size} atividade(s) concluída(s)") }
                        }
                    }
                    Div({ attr("style", "text-align:right") }) {
                        Div({ classes("strong"); attr("style", "font-size:22px;color:var(--amber)") }) { Text("+$possible") }
                        Div({ classes("xs", "muted") }) { Text("XP na mesa") }
                    }
                }
                Div({ attr("style", "margin-top:12px") }) { ProgressBar(if (dayTasks.isEmpty()) 0.0 else done.toDouble() / dayTasks.size) }
                P({ classes("small", "muted"); attr("style", "margin-top:8px") }) { Text("$earned XP conquistados · até +$possible se fechar tudo.") }
            }
            WhyThisPlan(plan.id, whyOpen) { whyOpen = !whyOpen }
            if (dayTasks.isEmpty()) {
                Card { Empty("weekend", "Nada planejado neste dia", "Use o tempo livre para revisar, treinar questões ou adiantar o edital.") }
            } else {
                // Agrupado por matéria, como os cartões do app.
                Div({ classes("stack") }) {
                    dayTasks.forEach { task -> androidx.compose.runtime.key(task.id) { TaskRow(task) { completing = task } } }
                }
            }
        }
        PlanView.WEEK -> {
            val weekStart = todayEpoch - (today.isoDayOfWeek - 1) + weekOffset * 7L
            val days = (0 until 7).map { weekStart + it }
            val tasksByDay = planTasks.filter { it.day in weekStart..(weekStart + 6) }.groupBy { it.day }
            val availability = data.availability.filter { it.planId == plan.id }.associateBy { it.day }
            val weekTasks = tasksByDay.values.flatten()
            val doneMinutes = weekTasks.filter { it.status == "CONCLUIDA" }.sumOf { it.minutes }
            val totalMinutes = weekTasks.sumOf { it.minutes }
            val selected = if (selectedDay in weekStart..(weekStart + 6)) selectedDay else if (weekOffset == 0) todayEpoch else weekStart
            Div({ classes("row", "between", "wrap") }) {
                Div({ classes("row") }) {
                    IconButton("chevron_left", "Semana anterior") { weekOffset-- }
                    B { Text("${Queries.shortDate(LocalDate.fromEpochDays(weekStart))} – ${Queries.shortDate(LocalDate.fromEpochDays(weekStart + 6))}") }
                    IconButton("chevron_right", "Próxima semana") { weekOffset++ }
                    if (weekOffset != 0) Btn("Esta semana", { weekOffset = 0; selectedDay = todayEpoch }, style = "ghost", small = true)
                }
                Div({ classes("row"); attr("style", "min-width:220px;flex:1;max-width:380px") }) {
                    Div({ classes("grow") }) { ProgressBar(if (totalMinutes == 0) 0.0 else doneMinutes.toDouble() / totalMinutes, "green") }
                    Span({ classes("small", "muted", "strong", "nowrap") }) { Text("${Queries.minutesLabel(doneMinutes)} de ${Queries.minutesLabel(totalMinutes)}") }
                }
            }
            // Calendário da semana, como no app: um dia por coluna; tocar mostra o que estudar.
            Div({ classes("week-strip"); attr("role", "tablist") }) {
                days.forEachIndexed { index, epoch ->
                    androidx.compose.runtime.key(epoch) {
                        val list = tasksByDay[epoch].orEmpty()
                        val done = list.count { it.status == "CONCLUIDA" }
                        val slot = availability[index + 1]
                        val off = list.isEmpty() && (slot?.unavailable == true || slot?.minutes == 0)
                        Button({
                            classes(*listOfNotNull("wday", if (epoch == selected) "on" else null, if (epoch == todayEpoch) "today" else null, if (off) "off" else null, if (list.isNotEmpty() && done == list.size) "done" else null).toTypedArray())
                            attr("role", "tab"); attr("aria-selected", (epoch == selected).toString())
                            onClick { selectedDay = epoch }
                        }) {
                            Span({ classes("wd") }) { Text(Queries.weekdayShort[index]) }
                            Span({ classes("dn") }) { Text("${LocalDate.fromEpochDays(epoch).day}") }
                            Span({ classes("dots") }) {
                                if (list.isEmpty()) Span({ classes("xs") }) { Text(if (off) "folga" else "livre") }
                                else list.take(4).forEach { t -> androidx.compose.runtime.key(t.id) { org.jetbrains.compose.web.dom.I({ attr("style", "background:${taskColors(t.type).second}"); if (t.status == "CONCLUIDA") classes("ok") }) } }
                            }
                            if (list.isNotEmpty()) Span({ classes("xs", "wmin") }) { Text(Queries.minutesLabel(list.sumOf { it.minutes })) }
                        }
                    }
                }
            }
            val dayTasks = Queries.tasksOn(data, plan.id, selected)
            Div({ classes("row", "between", "wrap"); attr("style", "margin-top:4px") }) {
                org.jetbrains.compose.web.dom.H3 { Text(dayTitle(selected, todayEpoch)) }
                if (dayTasks.isNotEmpty()) Chip("${dayTasks.count { it.status == "CONCLUIDA" }} de ${dayTasks.size} · ${Queries.minutesLabel(dayTasks.sumOf { it.minutes })}", "outline")
            }
            if (dayTasks.isEmpty()) Card { Empty("weekend", "Nada planejado neste dia", "Use o tempo livre para revisar, treinar questões ou adiantar o edital.") }
            else Div({ classes("stack") }) {
                dayTasks.forEach { task -> androidx.compose.runtime.key(task.id) { TaskRow(task) { completing = task } } }
            }
        }
        PlanView.MONTH -> {
            val anchor = LocalDate(today.year, today.month, 1)
            val monthIndex = anchor.year * 12 + anchor.month.ordinal + monthOffset
            val first = LocalDate(monthIndex / 12, monthIndex % 12 + 1, 1)
            val firstEpoch = first.toEpochDay()
            val gridStart = firstEpoch - (first.isoDayOfWeek - 1)
            val nextMonth = LocalDate((monthIndex + 1) / 12, (monthIndex + 1) % 12 + 1, 1).toEpochDay()
            val byDay = planTasks.filter { it.day in gridStart until gridStart + 42 }.groupBy { it.day }
            val maxMinutes = byDay.values.maxOfOrNull { list -> list.sumOf { it.minutes } }?.coerceAtLeast(30) ?: 60
            Div({ classes("row") }) {
                IconButton("chevron_left", "Mês anterior") { monthOffset-- }
                B { Text("${Queries.monthLong[first.month.ordinal].replaceFirstChar(Char::uppercase)} de ${first.year}") }
                IconButton("chevron_right", "Próximo mês") { monthOffset++ }
                if (monthOffset != 0) Btn("Este mês", { monthOffset = 0 }, style = "ghost", small = true)
            }
            Card {
                Div({ classes("month") }) {
                    Queries.weekdayShort.forEach { Div({ classes("dow") }) { Text(it) } }
                    (0 until 42).forEach { i ->
                        val epoch = gridStart + i
                        if (i >= 35 && epoch >= nextMonth) return@forEach
                        val list = byDay[epoch].orEmpty()
                        val minutes = list.sumOf { it.minutes }
                        val allDone = list.isNotEmpty() && list.all { it.status == "CONCLUIDA" }
                        Div({
                            classes(*listOfNotNull("mcell", if (epoch < firstEpoch || epoch >= nextMonth) "out" else null, if (epoch == todayEpoch) "today" else null).toTypedArray())
                            attr("title", if (list.isEmpty()) "Livre" else "${list.size} tarefa(s) · ${Queries.minutesLabel(minutes)}")
                            onClick { dayOffset = (epoch - todayEpoch).toInt(); view = PlanView.TODAY }
                        }) {
                            Span({ classes("n") }) { Text("${LocalDate.fromEpochDays(epoch).day}") }
                            if (list.isNotEmpty()) Div({ classes(*listOfNotNull("load", if (allDone) "done" else null).toTypedArray()) }) {
                                Span({ attr("style", "width:${(minutes * 100 / maxMinutes).coerceIn(8, 100)}%") })
                            }
                        }
                    }
                }
            }
        }
    }

    completing?.let { task -> CompleteTaskDialog(task) { completing = null } }
}

private fun dayTitle(day: Long, today: Long): String {
    val date = LocalDate.fromEpochDays(day)
    val name = Queries.weekdayLong[date.isoDayOfWeek - 1]
    return when (day - today) {
        0L -> "Hoje, ${Queries.shortDate(date)}"
        1L -> "Amanhã, ${Queries.shortDate(date)}"
        -1L -> "Ontem, ${Queries.shortDate(date)}"
        else -> "${name.replaceFirstChar(Char::uppercase)}, ${Queries.shortDate(date)}"
    }
}

/** "Por que este plano": as explicações do método guardadas na última revisão (como no app). */
@Composable
private fun WhyThisPlan(planId: String, open: Boolean, onToggle: () -> Unit) {
    val revision = Store.data.array("studyPlanRevisions").mapNotNull { it as? JsonObject }
        .filter { it.str("planId") == planId && !it.str("summary").isNullOrBlank() && it.str("reason") == "PROPOSAL_APPLIED" }
        .maxByOrNull { (it["revision"] as? JsonPrimitive)?.longOrNull ?: 0 } ?: return
    val lines = revision.str("summary").orEmpty().lines().filter { it.isNotBlank() }
    if (lines.isEmpty()) return
    val latest = androidx.compose.runtime.rememberUpdatedState(onToggle)
    Div({ classes("card") }) {
        Button({ classes("subject-head"); attr("style", "padding:0"); attr("aria-expanded", open.toString()); onClick { latest.value() } }) {
            Div({ classes("grow") }) {
                H2 { Text("Por que este plano") }
                P({ classes("small", "muted") }) { Text(lines.first()) }
            }
            Icon(if (open) "expand_less" else "expand_more")
        }
        if (open && lines.size > 1) Ul({ classes("small"); attr("style", "margin:12px 0 0;padding-left:20px;display:flex;flex-direction:column;gap:6px") }) {
            lines.drop(1).forEach { Li { Text(it) } }
        }
    }
}
