package br.com.estudario.web.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import br.com.estudario.time.isoDayOfWeek
import br.com.estudario.time.toEpochDay
import br.com.estudario.web.data.PlanTask
import br.com.estudario.web.data.Queries
import br.com.estudario.web.data.Store
import kotlinx.datetime.LocalDate
import org.jetbrains.compose.web.dom.B
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text

@Composable
fun PlanScreen() {
    val data = Store.data
    val plan = Queries.activePlan(data)
    var weekOffset by remember { mutableStateOf(0) }
    var view by remember { mutableStateOf("semana") }
    var completing by remember { mutableStateOf<PlanTask?>(null) }

    if (plan == null) {
        PageHead("Plano")
        Card { Empty("calendar_month", "Nenhum plano ativo", "Crie ou ative um plano no app do Estudário. Ele aparece aqui sincronizado.") }
        return
    }

    val today = Queries.todayDate()
    val todayEpoch = today.toEpochDay()
    val weekStart = todayEpoch - (today.isoDayOfWeek - 1) + weekOffset * 7L
    val days = (0 until 7).map { weekStart + it }
    val tasksByDay = data.tasks.filter { it.planId == plan.id && it.day in weekStart..(weekStart + 6) }.groupBy { it.day }
    val availability = data.availability.filter { it.planId == plan.id }.associateBy { it.day }
    val weekTasks = tasksByDay.values.flatten()
    val doneMinutes = weekTasks.filter { it.status == "CONCLUIDA" }.sumOf { it.minutes }
    val totalMinutes = weekTasks.sumOf { it.minutes }

    PageHead(plan.name, plan.exam?.let { "Prova em ${Queries.dayLabel(LocalDate.fromEpochDays(it))} · faltam ${it - todayEpoch} dias" } ?: plan.objective.ifBlank { null }) {
        Div({ classes("segmented") }) {
            listOf("semana" to "Semana", "lista" to "Lista").forEach { (key, label) ->
                Button({ classes(*listOfNotNull(if (view == key) "on" else null).toTypedArray()); onClick { view = key } }) { Text(label) }
            }
        }
    }

    Card(extra = "flat") {
        Div({ classes("row", "between", "wrap") }) {
            Div({ classes("row") }) {
                IconButton("chevron_left", "Semana anterior") { weekOffset-- }
                Span({ classes("strong") }) {
                    val start = LocalDate.fromEpochDays(weekStart)
                    val end = LocalDate.fromEpochDays(weekStart + 6)
                    Text("${Queries.shortDate(start)} – ${Queries.shortDate(end)}")
                }
                IconButton("chevron_right", "Próxima semana") { weekOffset++ }
                if (weekOffset != 0) Btn("Hoje", { weekOffset = 0 }, style = "ghost", small = true)
            }
            Div({ classes("row"); attr("style", "min-width:220px;flex:1;max-width:360px") }) {
                Div({ attr("style", "flex:1") }) { ProgressBar(if (totalMinutes == 0) 0.0 else doneMinutes.toDouble() / totalMinutes, "green") }
                Span({ classes("small", "muted", "strong") }) { Text("${Queries.minutesLabel(doneMinutes)} de ${Queries.minutesLabel(totalMinutes)}") }
            }
        }
    }

    if (view == "semana") {
        Div({ classes("week") }) {
            days.forEachIndexed { index, epoch ->
                val date = LocalDate.fromEpochDays(epoch)
                val dayTasks = tasksByDay[epoch].orEmpty().sortedBy { it.status == "CONCLUIDA" }
                val slot = availability[index + 1]
                val off = slot?.unavailable == true || slot?.minutes == 0
                Div({ classes(*listOfNotNull("day", if (epoch == todayEpoch) "today" else null, if (off && dayTasks.isEmpty()) "off" else null).toTypedArray()) }) {
                    Div({ classes("day-head") }) {
                        Span({ classes("day-name") }) { Text(Queries.weekdayShort[index]) }
                        Span({ classes("day-num") }) { Text("${date.day}") }
                    }
                    if (dayTasks.isEmpty()) P({ classes("small", "muted") }) { Text(if (off) "Folga" else "Livre") }
                    dayTasks.forEach { task ->
                        Div({
                            classes(*listOfNotNull("mini-task", if (task.status == "CONCLUIDA") "done" else null).toTypedArray())
                            attr("style", "border-left-color:${taskColors(task.type).second}")
                            attr("role", "button")
                            attr("tabindex", "0")
                            attr("title", "${Queries.taskTypeLabel(task.type)} · ${Queries.taskStatusLabel(task.status)}")
                            onClick { if (task.status != "CONCLUIDA" && task.status != "NAO_REALIZADA") completing = task }
                        }) {
                            B { Text("${Queries.taskTypeLabel(task.type)} · ${Queries.minutesLabel(task.minutes)}") }
                            Text(task.topicName ?: task.subjectName)
                        }
                    }
                    if (dayTasks.isNotEmpty()) P({ classes("small", "muted"); attr("style", "margin-top:auto") }) { Text(Queries.minutesLabel(dayTasks.sumOf { it.minutes })) }
                }
            }
        }
    } else {
        Div({ classes("stack") }) {
            days.forEachIndexed { index, epoch ->
                val dayTasks = tasksByDay[epoch].orEmpty()
                if (dayTasks.isEmpty()) return@forEachIndexed
                val date = LocalDate.fromEpochDays(epoch)
                Card {
                    CardHead("${Queries.weekdayLong[index].replaceFirstChar(Char::uppercase)}, ${Queries.dayLabel(date)}") {
                        Chip(Queries.minutesLabel(dayTasks.sumOf { it.minutes }))
                    }
                    Div({ classes("stack") }) { dayTasks.forEach { task -> TaskRow(task) { completing = task } } }
                }
            }
            if (weekTasks.isEmpty()) Card { Empty("event_available", "Semana sem tarefas", "Não há nada planejado nestes dias.") }
        }
    }

    completing?.let { task -> CompleteTaskDialog(task) { completing = null } }
}
