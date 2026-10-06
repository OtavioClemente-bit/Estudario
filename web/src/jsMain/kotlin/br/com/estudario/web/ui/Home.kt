package br.com.estudario.web.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import br.com.estudario.time.isoDayOfWeek
import br.com.estudario.web.Route
import br.com.estudario.web.Router
import br.com.estudario.web.data.PlanTask
import br.com.estudario.web.data.Queries
import br.com.estudario.web.data.Store
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H1
import org.jetbrains.compose.web.dom.H2
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text
import kotlin.js.Date

@Composable
fun HomeScreen() {
    val data = Store.data
    val today = Queries.todayDate()
    val plan = Queries.activePlan(data)
    val competition = Queries.primaryCompetition(data)
    val tasks = plan?.let { Queries.tasksOn(data, it.id, Queries.todayEpoch()) }.orEmpty()
    val streak = Queries.streak(data)
    val subjects = Queries.subjectsOf(data, competition?.id)
    val leaves = Queries.leafTopics(data, subjects.mapTo(hashSetOf()) { it.id })
    val studied = leaves.count(Queries::isStudied)
    val due = Queries.dueReviews(data)
    val errors = Queries.pendingErrors(data)
    val name = Store.session?.name?.substringBefore(' ')
    var completing by remember { mutableStateOf<PlanTask?>(null) }

    val hour = Date().getHours()
    val greeting = when (hour) { in 5..11 -> "Bom dia"; in 12..17 -> "Boa tarde"; else -> "Boa noite" }

    Div({ classes("hero") }) {
        Div({ classes("row", "between", "wrap") }) {
            Div({ classes("stack", "tight") }) {
                P({ classes("muted") }) { Text("${Queries.weekdayLong[today.isoDayOfWeek - 1].replaceFirstChar(Char::uppercase)}, ${Queries.dayLabel(today)}") }
                H1 { Text(if (name != null) "$greeting, $name" else greeting) }
                P({ classes("muted") }) {
                    Text(
                        when {
                            plan == null -> "Crie seu plano no app para ver aqui o que estudar a cada dia."
                            tasks.isEmpty() -> "Hoje não tem tarefa no plano. Que tal revisar ou resolver questões?"
                            tasks.all { it.status == "CONCLUIDA" } -> "Plano de hoje concluído. Mandou bem!"
                            else -> "${tasks.count { it.status != "CONCLUIDA" }} de ${tasks.size} tarefas para hoje · ${Queries.minutesLabel(tasks.filter { it.status != "CONCLUIDA" }.sumOf { it.minutes })}"
                        },
                    )
                }
            }
            Div({ classes("row", "wrap") }) {
                Div({ classes("stat") }) {
                    Span({ classes("value") }) { Text("${streak.current}") }
                    Span({ classes("label"); attr("style", "color:rgba(255,255,255,.85)") }) { Text(if (streak.current == 1) "dia seguido" else "dias seguidos") }
                }
            }
        }
    }

    Div({ classes("grid", "main-side") }) {
        Card {
            CardHead("Plano de hoje") { Btn("Ver semana", { Router.go(Route.Plan) }, style = "ghost", small = true, icon = "calendar_month") }
            if (tasks.isEmpty()) {
                Empty("event_available", if (plan == null) "Sem plano ativo" else "Dia livre", if (plan == null) "Monte seu plano em poucos passos: edital, data da prova e rotina." else "Nada agendado para hoje.", action = if (plan == null) ({ Btn("Configurar meus estudos", { Router.go(Route.Setup) }, icon = "auto_awesome") }) else null)
            } else {
                Div({ classes("stack") }) { tasks.forEach { task -> androidx.compose.runtime.key(task.id) { TaskRow(task, onComplete = { completing = task }) } } }
            }
        }
        Div({ classes("stack") }) {
            Card {
                CardHead("Edital")
                Div({ classes("stack", "tight") }) {
                    Div({ classes("row", "between") }) {
                        Span({ classes("strong") }) { Text("${percent(studied, leaves.size)}% estudado") }
                        Span({ classes("muted", "small") }) { Text("$studied de ${leaves.size} tópicos") }
                    }
                    ProgressBar(if (leaves.isEmpty()) 0.0 else studied.toDouble() / leaves.size)
                    competition?.let { P({ classes("muted", "small") }) { Text(it.name) } }
                }
            }
            Card {
                CardHead("Para hoje")
                Div({ classes("stack") }) {
                    ShortcutRow("replay", "Revisões", if (due.isEmpty()) "Nenhuma pendente" else "${due.size} para fazer", due.isNotEmpty()) { Router.go(Route.Reviews) }
                    ShortcutRow("error_med", "Caderno de erros", if (errors.isEmpty()) "Tudo em dia" else "${errors.size} para rever", errors.isNotEmpty()) { Router.go(Route.Errors) }
                    ShortcutRow("quiz", "Questões", "${Queries.visibleQuestions(data).size} no banco", false) { Router.go(Route.Questions) }
                }
            }
            Card {
                CardHead("Esta semana")
                WeekDots(streak.week.map { it.done to it.partial }, today.isoDayOfWeek - 1)
                P({ classes("muted", "small"); attr("style", "margin-top:10px") }) {
                    Text("Recorde: ${streak.best} ${if (streak.best == 1) "dia" else "dias"} · ${streak.activeDays} dias com estudo")
                }
            }
        }
    }

    completing?.let { task -> CompleteTaskDialog(task) { completing = null } }
}

@Composable
private fun ShortcutRow(icon: String, title: String, subtitle: String, highlight: Boolean, onClick: () -> Unit) {
    org.jetbrains.compose.web.dom.Button({
        classes("task")
        attr("style", "cursor:pointer;width:100%;text-align:left")
        onClick { onClick() }
    }) {
        Div({ classes("task-mark"); attr("style", if (highlight) "background:var(--amber-soft);color:var(--on-amber-soft)" else "background:var(--soft-2);color:var(--muted)") }) { Icon(icon) }
        Div({ classes("task-body") }) {
            Div({ classes("task-title") }) { Text(title) }
            Div({ classes("task-meta") }) { Text(subtitle) }
        }
        Icon("chevron_right")
    }
}

@Composable
fun WeekDots(days: List<Pair<Boolean, Boolean>>, todayIndex: Int) {
    Div({ classes("week-dots") }) {
        days.forEachIndexed { index, (done, partial) ->
            Div({ classes("wd") }) {
                Span({ classes("small", "muted", "strong") }) { Text(Queries.weekdayShort[index].take(1).uppercase()) }
                val style = when {
                    done -> "background:var(--green);color:#fff"
                    partial -> "background:var(--amber-soft);color:var(--on-amber-soft)"
                    index == todayIndex -> "border:2px solid var(--primary)"
                    else -> "background:var(--soft-2)"
                }
                Div({ classes("circle"); attr("style", style) }) {
                    if (done) Icon("check")
                }
            }
        }
    }
}

/** Uma tarefa do plano, como no cartão do app: tipo, matéria, tópico, tempo e o botão de concluir. */
@Composable
fun TaskRow(task: PlanTask, onComplete: () -> Unit) {
    val done = task.status == "CONCLUIDA"
    Div({ classes(*listOfNotNull("task", if (done) "done" else null).toTypedArray()) }) {
        val (bg, fg) = taskColors(task.type)
        Div({ classes("task-mark"); attr("style", "background:$bg;color:$fg") }) { Icon(taskIcon(task.type)) }
        Div({ classes("task-body") }) {
            Div({ classes("task-title") }) { Text(task.topicName ?: task.subjectName) }
            Div({ classes("task-meta") }) {
                Text("${Queries.taskTypeLabel(task.type)} · ${task.subjectName.takeIf { task.topicName != null } ?: ""}${if (task.topicName != null) " · " else ""}${Queries.minutesLabel(task.minutes)}")
                if (task.questions > 0) Text(" · ${task.questions} questões")
            }
        }
        if (task.status == "EM_ANDAMENTO") Chip("Em andamento", "amber")
        if (task.status == "NAO_REALIZADA") Chip("Não realizada", "red")
        org.jetbrains.compose.web.dom.Button({
            classes(*listOfNotNull("check", if (done) "on" else null).toTypedArray())
            attr("aria-label", if (done) "Concluída" else "Concluir tarefa")
            attr("title", if (done) "Concluída" else "Concluir")
            if (done || task.status == "NAO_REALIZADA") attr("disabled", "")
            onClick { onComplete() }
        }) { Icon("check") }
    }
}

fun taskIcon(type: String) = when (type) {
    "THEORY" -> "menu_book"
    "QUESTIONS" -> "quiz"
    "REVIEW" -> "replay"
    "ACTIVE_RECALL" -> "psychology"
    "FLASHCARDS" -> "style"
    "SIMULATION" -> "timer"
    "DISCURSIVE" -> "edit_note"
    else -> "task_alt"
}

fun taskColors(type: String): Pair<String, String> = when (type) {
    "THEORY" -> "var(--primary-soft)" to "var(--on-primary-soft)"
    "QUESTIONS" -> "var(--green-soft)" to "var(--on-green-soft)"
    "REVIEW", "ACTIVE_RECALL", "FLASHCARDS" -> "var(--amber-soft)" to "var(--on-amber-soft)"
    else -> "var(--soft-2)" to "var(--ink)"
}

@Composable
fun CompleteTaskDialog(task: PlanTask, onClose: () -> Unit) {
    var minutes by remember { mutableStateOf(task.minutes.toString()) }
    var questions by remember { mutableStateOf(if (task.questions > 0) task.questions.toString() else "0") }
    var correct by remember { mutableStateOf("0") }
    Modal(onDismiss = onClose) {
        H2 { Text("Concluir tarefa") }
        P({ classes("muted") }) { Text("${Queries.taskTypeLabel(task.type)} · ${task.topicName ?: task.subjectName}") }
        NumberField("Minutos estudados", minutes) { minutes = it }
        if (task.type == "QUESTIONS" || task.questions > 0) {
            Div({ classes("grid", "cols-2") }) {
                NumberField("Questões feitas", questions) { questions = it }
                NumberField("Acertos", correct) { correct = it }
            }
        }
        val m = minutes.toIntOrNull() ?: 0
        if (m < task.minutes) P({ classes("small", "muted") }) { Text("Menos tempo que o planejado: a tarefa fica em andamento para você continuar depois, como no app.") }
        Div({ classes("row"); attr("style", "justify-content:flex-end") }) {
            Btn("Cancelar", onClose, style = "ghost")
            Btn("Concluir", {
                val q = questions.toIntOrNull() ?: 0
                Store.update { data ->
                    val done = br.com.estudario.web.data.Actions.completeTask(data, task.id, m, q, (correct.toIntOrNull() ?: 0).coerceIn(0, q))
                    // Como no app: concluir reorganiza o plano.
                    runCatching { br.com.estudario.web.data.Planning.replan(done, task.planId, br.com.estudario.domain.planner.ReplanReason.TASK_COMPLETED) }.getOrDefault(done)
                }
                onClose()
            }, icon = "check")
        }
    }
}

@Composable
fun NumberField(label: String, value: String, onChange: (String) -> Unit) {
    Div({ classes("field") }) {
        org.jetbrains.compose.web.dom.Label { Text(label) }
        org.jetbrains.compose.web.dom.Input(org.jetbrains.compose.web.attributes.InputType.Number) {
            classes("input")
            value(value)
            attr("min", "0")
            onInput { onChange(it.value?.toString() ?: "") }
        }
    }
}
