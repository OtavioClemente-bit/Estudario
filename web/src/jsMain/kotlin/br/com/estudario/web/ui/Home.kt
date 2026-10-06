package br.com.estudario.web.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import br.com.estudario.domain.planner.ReplanReason
import br.com.estudario.time.isoDayOfWeek
import br.com.estudario.web.Route
import br.com.estudario.web.Router
import br.com.estudario.web.data.Actions
import br.com.estudario.web.data.PlanTask
import br.com.estudario.web.data.Planning
import br.com.estudario.web.data.Progress
import br.com.estudario.web.data.Queries
import br.com.estudario.web.data.Store
import org.jetbrains.compose.web.dom.B
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H1
import org.jetbrains.compose.web.dom.H2
import org.jetbrains.compose.web.dom.Img
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
    val view = Progress.of(data)
    val streak = view.streak
    val progress = view.progress
    val subjects = Queries.subjectsOf(data, competition?.id)
    val leaves = Queries.leafTopics(data, subjects.mapTo(hashSetOf()) { it.id })
    val studied = leaves.count(Queries::isStudied)
    val due = Queries.dueReviews(data)
    val errors = Queries.pendingErrors(data)
    val session = Store.session
    var completing by remember { mutableStateOf<PlanTask?>(null) }

    val hour = Date().getHours()
    val greeting = when (hour) { in 5..11 -> "Bom dia"; in 12..17 -> "Boa tarde"; else -> "Boa noite" }

    // Cabeçalho roxo do app: saudação e os três indicadores.
    Div({ classes("hero") }) {
        Div({ classes("hero-top") }) {
            Div({ classes("hero-avatar") }) {
                val photo = session?.avatarUrl
                if (photo != null) Img(src = photo, alt = "") else Text(initials(session?.name ?: session?.email))
            }
            Div({ classes("grow") }) {
                P({ classes("muted") }) { Text("$greeting,") }
                H1 { Text(session?.name?.substringBefore(' ') ?: "Estudante") }
                P({ classes("muted", "small") }) {
                    Text("${Queries.weekdayLong[today.isoDayOfWeek - 1].replaceFirstChar(Char::uppercase)}, ${Queries.dayLabel(today)}")
                }
            }
        }
        Div({ classes("hero-stats") }) {
            HeroStat("local_fire_department", "${streak.current}", if (streak.current == 1) "dia seguido" else "dias seguidos")
            HeroStat("military_tech", "${progress.level}", progress.levelTitle)
            HeroStat("bolt", "+${progress.xpToday}", "XP hoje")
        }
    }

    Div({ classes("grid", "main-side") }) {
        Div({ classes("stack", "loose") }) {
            MissionCard(tasks, streak.todayDone, streak.todayQuestions, streak.goal.questions)
            nextUp(tasks)?.let { task ->
                Button({
                    classes("continue")
                    onClick { if (task.topicId != null) Router.go(Route.Topic(task.topicId)) else Router.go(Route.Plan) }
                }) {
                    Div({ classes("grow") }) {
                        Div({ classes("label") }) { Text(if (task.status == "EM_ANDAMENTO") "Continuar de onde parou" else "Próximo do plano") }
                        Div({ classes("title", "clamp-2") }) { Text(task.topicName ?: task.subjectName) }
                        Div({ classes("small"); attr("style", "opacity:.8;margin-top:2px") }) { Text("${Queries.taskTypeLabel(task.type)} · ${task.subjectName} · ${Queries.minutesLabel(task.minutes)}") }
                    }
                    Span({ classes("go") }) { Icon("arrow_forward") }
                }
            }
            Card {
                CardHead("Plano de hoje") { Btn("Ver plano", { Router.go(Route.Plan) }, style = "ghost", small = true, icon = "calendar_month") }
                when {
                    plan == null -> Empty("event_available", "Sem plano ativo", "Monte seu plano em poucos passos: edital, data da prova e rotina.", action = { Btn("Configurar meus estudos", { Router.go(Route.Setup) }, icon = "auto_awesome") })
                    tasks.isEmpty() -> Empty("weekend", "Dia livre no plano", "Aproveite para revisar ou treinar algumas questões.", action = { Btn("Treinar", { Router.go(Route.Train) }, style = "tonal", icon = "school") })
                    else -> Div({ classes("stack") }) { tasks.forEach { task -> androidx.compose.runtime.key(task.id) { TaskRow(task, onComplete = { completing = task }) } } }
                }
            }
        }
        Div({ classes("stack", "loose") }) {
            Card {
                CardHead("Ritmo da semana") { Span({ classes("small", "muted", "strong") }) { Text("melhor: ${streak.best}") } }
                P({ classes("small", "muted"); attr("style", "margin:-8px 0 14px") }) { Text("${streak.week.count { it.done }} de 7 dias no alvo") }
                WeekDots(streak.week.map { it.done to it.partial }, today.isoDayOfWeek - 1)
            }
            Card {
                CardHead("Para fazer")
                Div({ classes("stack", "tight") }) {
                    ShortcutRow("replay", "Revisões", if (due.isEmpty()) "Nenhuma pendente" else "${due.size} para hoje", due.isNotEmpty()) { Router.go(Route.Reviews) }
                    ShortcutRow("error_med", "Caderno de erros", if (errors.isEmpty()) "Tudo em dia" else "${errors.size} para rever", errors.isNotEmpty()) { Router.go(Route.Errors) }
                    ShortcutRow("style", "Flashcards", "Revisão rápida dos tópicos", false) { Router.go(Route.Flashcards(null)) }
                    ShortcutRow("center_focus_strong", "Modo foco", "Cronômetro para estudar sem distração", false) { Router.go(Route.Focus) }
                }
            }
            Card(extra = "clickable", attrs = { onClick { Router.go(Route.Edital) } }) {
                CardHead("Edital") { Icon("chevron_right") }
                Div({ classes("stack", "tight") }) {
                    Div({ classes("row", "between") }) {
                        B { Text("${percent(studied, leaves.size)}% estudado") }
                        Span({ classes("small", "muted") }) { Text("$studied de ${leaves.size} tópicos") }
                    }
                    ProgressBar(if (leaves.isEmpty()) 0.0 else studied.toDouble() / leaves.size)
                    competition?.let { P({ classes("muted", "small", "clamp-2") }) { Text(it.name) } }
                }
            }
        }
    }

    completing?.let { task -> CompleteTaskDialog(task) { completing = null } }
}

private fun nextUp(tasks: List<PlanTask>): PlanTask? =
    tasks.firstOrNull { it.status == "EM_ANDAMENTO" } ?: tasks.firstOrNull { it.status == "PLANEJADA" }

@Composable
private fun HeroStat(icon: String, value: String, label: String) {
    Div({ classes("hero-stat") }) {
        Span({ classes("ico") }) { Icon(icon, filled = true) }
        Div({ attr("style", "min-width:0") }) {
            B { Text(value) }
            Span { Text(label) }
        }
    }
}

/** "Missão de hoje": o anel fecha com as tarefas do plano do dia, ou com a meta de questões se não houver plano. */
@Composable
private fun MissionCard(tasks: List<PlanTask>, dayDone: Boolean, questionsToday: Int, goal: Int) {
    val done = tasks.count { it.status == "CONCLUIDA" }
    val earned = tasks.filter { it.status == "CONCLUIDA" }.sumOf { Progress.taskXp(it) }
    val possible = tasks.sumOf { Progress.taskXp(it) }
    val fraction = when {
        tasks.isNotEmpty() -> done.toDouble() / tasks.size
        goal > 0 -> (questionsToday.toDouble() / goal).coerceAtMost(1.0)
        else -> 0.0
    }
    val complete = (tasks.isNotEmpty() && done == tasks.size) || (tasks.isEmpty() && dayDone)
    Card(extra = "pad-lg") {
        Div({ classes("mission") }) {
            Ring(fraction) {
                if (complete) Icon("check", extraClass = "fill")
                else {
                    B { Text(if (tasks.isNotEmpty()) "$done/${tasks.size}" else "$questionsToday") }
                    Span { Text(if (tasks.isNotEmpty()) "tarefas" else "de $goal questões") }
                }
            }
            Div({ classes("stack", "tight", "grow") }) {
                H2 { Text("Missão de hoje") }
                P({ classes("muted") }) {
                    Text(
                        when {
                            complete && tasks.isNotEmpty() -> "Plano do dia concluído. A sequência de hoje está garantida."
                            complete -> "Meta batida. A sequência de hoje está garantida."
                            tasks.isNotEmpty() -> "${tasks.size - done} de ${tasks.size} atividades para fechar o dia."
                            else -> "Responda $goal questões, conclua uma tarefa ou faça uma revisão para manter a sequência."
                        },
                    )
                }
                Div({ classes("row", "wrap"); attr("style", "margin-top:4px") }) {
                    if (possible > 0) Xp(earned) else if (complete) Xp(0, green = true)
                    if (possible > 0 && earned < possible) Span({ classes("small", "muted") }) { Text("até +$possible se fechar tudo") }
                }
            }
        }
        Div({ attr("style", "margin-top:18px") }) {
            Btn(if (tasks.isEmpty()) "Treinar agora" else "Abrir minhas atividades", { Router.go(if (tasks.isEmpty()) Route.Train else Route.Plan) }, block = true, icon = "arrow_forward")
        }
    }
}

@Composable
private fun ShortcutRow(icon: String, title: String, subtitle: String, highlight: Boolean, onClick: () -> Unit) {
    val latest = androidx.compose.runtime.rememberUpdatedState(onClick)
    Button({ classes("task"); onClick { latest.value() } }) {
        Div({ classes("task-mark"); attr("style", if (highlight) "background:var(--amber-soft);color:var(--on-amber-soft)" else "background:var(--soft-2);color:var(--primary)") }) { Icon(icon) }
        Div({ classes("task-body") }) {
            Div({ classes("task-title") }) { Text(title) }
            Div({ classes("task-meta") }) { Text(subtitle) }
        }
        Icon("chevron_right", extraClass = "faint")
    }
}

@Composable
fun WeekDots(days: List<Pair<Boolean, Boolean>>, todayIndex: Int) {
    Div({ classes("week-dots") }) {
        days.forEachIndexed { index, (done, partial) ->
            Div({ classes("wd") }) {
                Span({ classes("xs", "muted", "strong") }) { Text(Queries.weekdayShort[index].take(1).uppercase()) }
                val style = when {
                    done -> "background:var(--green-strong);color:#fff"
                    partial -> "background:var(--amber-soft);color:var(--on-amber-soft)"
                    index == todayIndex -> "border:2px solid var(--primary)"
                    else -> "background:var(--soft-2)"
                }
                Div({ classes("circle"); attr("style", style) }) { if (done) Icon("check") }
            }
        }
    }
}

/** Uma tarefa do plano, como no cartão do app: tipo, matéria, tópico, tempo, XP e o botão de concluir. */
@Composable
fun TaskRow(task: PlanTask, onComplete: () -> Unit) {
    val done = task.status == "CONCLUIDA"
    val latest = androidx.compose.runtime.rememberUpdatedState(onComplete)
    Div({ classes(*listOfNotNull("task", if (done) "done" else null).toTypedArray()) }) {
        val (bg, fg) = taskColors(task.type)
        Div({ classes("task-mark"); attr("style", "background:$bg;color:$fg") }) { Icon(taskIcon(task.type)) }
        Div({
            classes("task-body")
            if (task.topicId != null) { attr("style", "cursor:pointer"); onClick { Router.go(Route.Topic(task.topicId)) } }
        }) {
            Div({ classes("task-kicker") }) { Text(task.subjectName) }
            Div({ classes("task-title") }) { Text(task.topicName ?: Queries.taskTypeLabel(task.type)) }
            Div({ classes("task-meta") }) {
                Text("${Queries.minutesLabel(task.minutes)} · ${Queries.taskTypeLabel(task.type)}")
                if (task.questions > 0) Text(" · ${task.questions} questões")
                when (task.status) {
                    "EM_ANDAMENTO" -> Text(" · em andamento")
                    "NAO_REALIZADA" -> Text(" · não realizada")
                    "CONCLUIDA" -> Text(" · concluída")
                }
            }
        }
        Div({ classes("task-side") }) {
            Xp(Progress.taskXp(task), green = done)
            Button({
                classes(*listOfNotNull("check", if (done) "on" else null).toTypedArray())
                attr("aria-label", if (done) "Concluída" else "Concluir tarefa")
                attr("title", if (done) "Concluída" else "Concluir")
                if (done || task.status == "NAO_REALIZADA") attr("disabled", "")
                onClick { latest.value() }
            }) { Icon("check") }
        }
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

/** Concluir uma tarefa: tempo e acertos com o contador de mais e menos, como no app. */
@Composable
fun CompleteTaskDialog(task: PlanTask, onClose: () -> Unit) {
    var minutes by remember { mutableStateOf(task.minutes) }
    var questions by remember { mutableStateOf(task.questions) }
    var correct by remember { mutableStateOf(0) }
    Modal(onDismiss = onClose) {
        Div {
            Span({ classes("eyebrow") }) { Text(Queries.taskTypeLabel(task.type)) }
            H2 { Text(task.topicName ?: task.subjectName) }
            P({ classes("muted", "small") }) { Text(task.subjectName) }
        }
        Div({ classes("day-row") }) {
            Div { Div({ classes("name") }) { Text("Tempo estudado") }; Div({ classes("hint") }) { Text("Planejado: ${Queries.minutesLabel(task.minutes)}") } }
            Stepper(minutes, { minutes = it }, step = 5, min = 0, max = 600, format = Queries::minutesLabel, label = "Tempo estudado")
        }
        if (task.type == "QUESTIONS" || task.questions > 0) {
            Div({ classes("day-row") }) {
                Div({ classes("name") }) { Text("Questões feitas") }
                Stepper(questions, { questions = it; if (correct > it) correct = it }, step = 1, min = 0, max = 300, label = "Questões feitas")
            }
            Div({ classes("day-row") }) {
                Div({ classes("name") }) { Text("Acertos") }
                Stepper(correct, { correct = it }, step = 1, min = 0, max = questions, label = "Acertos")
            }
        }
        if (minutes < task.minutes) Div({ classes("banner", "info") }) { Icon("info"); Text("Menos tempo que o planejado: a tarefa fica em andamento para você continuar depois, como no app.") }
        Div({ classes("row", "end") }) {
            Btn("Cancelar", onClose, style = "ghost")
            Btn("Concluir", {
                Store.update { data ->
                    val done = Actions.completeTask(data, task.id, minutes, questions, correct.coerceIn(0, questions))
                    // Como no app: concluir reorganiza o plano.
                    runCatching { Planning.replan(done, task.planId, ReplanReason.TASK_COMPLETED) }.getOrDefault(done)
                }
                Toast.show(if (minutes >= task.minutes) "Tarefa concluída! +${Progress.taskXp(task)} XP" else "Progresso registrado")
                onClose()
            }, icon = "check")
        }
    }
}

