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
import org.jetbrains.compose.web.dom.H3
import androidx.compose.runtime.key
import org.jetbrains.compose.web.dom.Li
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.dom.Ul

private enum class PlanView(val label: String, val icon: String) { TODAY("Hoje", "today"), WEEK("Semana", "date_range"), MONTH("Mês", "calendar_month"), OVERVIEW("Visão geral", "insights") }

/** Quem abre o plano pela "Previsão" do Início cai direto na visão geral; o resto, no dia de hoje. */
object PlanEntry {
    var overview = false
    /** O Início pediu para abrir o plano já nas atrasadas. */
    var overdue = false
}

@Composable
fun PlanScreen() {
    val data = Store.data
    val plan = Queries.activePlan(data)
    var view by remember { mutableStateOf(if (PlanEntry.overview) PlanView.OVERVIEW else PlanView.TODAY).also { PlanEntry.overview = false } }
    var dayOffset by remember { mutableStateOf(0) }
    var weekOffset by remember { mutableStateOf(0) }
    var selectedDay by remember { mutableStateOf(Queries.todayEpoch()) }
    var monthOffset by remember { mutableStateOf(0) }
    var completing by remember { mutableStateOf<PlanTask?>(null) }
    var upgradeEdit by remember { mutableStateOf(false) }
    if (upgradeEdit) UpgradeModal(UpgradeReason.PLAN_EDIT) { upgradeEdit = false }
    var whyOpen by remember { mutableStateOf(false) }
    var overdueOpen by remember { mutableStateOf(PlanEntry.overdue).also { PlanEntry.overdue = false } }

    if (plan == null) {
        PageHead("Plano de estudos", "O que estudar, quando e por quê")
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

    PageHead("Plano de estudos", "O que estudar, quando e por quê") {
        Btn("Ajustar plano", { Router.go(Route.PlanSettings) }, style = "outline", icon = "tune", small = true)
    }
    PlanProgressHeader(plan, planTasks, todayEpoch)

    Div({ classes("plan-tabs"); attr("role", "tablist") }) {
        PlanView.entries.forEach { option ->
            Button({
                classes(*listOfNotNull("fchip", if (view == option) "on" else null).toTypedArray())
                attr("role", "tab"); attr("aria-selected", (view == option).toString())
                onClick { view = option }
            }) { Icon(option.icon); Text(option.label) }
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
            if (dayTasks.isNotEmpty() && dayOffset != 0) Card {
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
            // As atrasadas de dias anteriores, aqui mesmo em Hoje (antes era preciso voltar dia a dia).
            val overdueTasks = planTasks.filter { it.day < todayEpoch && (it.status == "PLANEJADA" || it.status == "EM_ANDAMENTO") }.sortedBy { it.day }
            // As revisões espaçadas vencidas até hoje aparecem no próprio plano, para ninguém esquecer delas.
            val dueReviews = Queries.dueReviews(data).size
            if (dayOffset == 0 && dueReviews > 0) Button({ classes("plan-review-card"); onClick { Router.go(Route.Reviews) } }) {
                Span({ classes("prc-ic") }) { Icon("Cycle") }
                Span({ classes("grow") }) {
                    Span({ classes("prc-tag") }) { Text("REVISÃO ESPAÇADA · HOJE") }
                    B { Text(if (dueReviews == 1) "1 revisão para hoje" else "$dueReviews revisões para hoje") }
                    Span({ classes("prc-sub") }) { Text("Uns ${dueReviews * br.com.estudario.domain.ReviewQueue.MINUTES_EACH} minutos · +${dueReviews * 20} XP · revisar no dia certo é o que fixa") }
                }
                Span({ classes("prc-go") }) { Text("Revisar") }
            }
            if (dayOffset == 0 && overdueTasks.isNotEmpty()) {
                Div({ classes("overdue-box"); id("atrasadas") }) {
                    Button({ classes("overdue-head"); attr("aria-expanded", overdueOpen.toString()); onClick { overdueOpen = !overdueOpen } }) {
                        Span({ classes("overdue-ic") }) { Icon("event_busy") }
                        Div({ classes("grow"); attr("style", "text-align:left") }) {
                            B { Text("${overdueTasks.size} atividade${if (overdueTasks.size == 1) "" else "s"} atrasada${if (overdueTasks.size == 1) "" else "s"}") }
                            Div({ classes("small", "muted") }) {
                                val days = (todayEpoch - overdueTasks.first().day).toInt()
                                Text("${Queries.minutesLabel(overdueTasks.sumOf { it.minutes })} que ficaram de ${if (days == 1) "ontem" else "$days dias atrás"}. O que fazemos com eles?")
                            }
                        }
                        Span({ classes("overdue-toggle") }) { Text(if (overdueOpen) "Ocultar" else "Ver"); Icon(if (overdueOpen) "expand_less" else "expand_more") }
                    }
                    if (overdueOpen) Div({ classes("stack"); attr("style", "margin-top:12px") }) {
                        overdueTasks.forEach { task ->
                            androidx.compose.runtime.key("late-" + task.id) {
                                Div({ classes("overdue-day") }) { Text(dayTitle(task.day, todayEpoch)) }
                                TaskRow(task) { completing = task }
                            }
                        }
                    }
                    // A pessoa escolhe: encaixar (voltam na frente e o resto se ajusta), empurrar o plano
                    // inteiro (nada se perde, tudo anda junto) ou deixar para depois.
                    val days = (todayEpoch - overdueTasks.first().day).toInt()
                    Div({ classes("row", "wrap"); attr("style", "margin-top:10px;gap:8px") }) {
                        Btn("Encaixar nos próximos dias", {
                            Store.update { br.com.estudario.web.data.Planning.fitOverdue(it, plan.id) }
                            Toast.show("Pronto: as atrasadas voltaram na frente e o resto do plano se ajustou ao seu ritmo.")
                        }, small = true, icon = "auto_awesome")
                        Btn(if (days == 1) "Empurrar tudo 1 dia" else "Empurrar tudo $days dias", {
                            Store.update { br.com.estudario.web.data.Planning.pushOverdue(it, plan.id) }
                            Toast.show("O plano inteiro andou $days dia${if (days == 1) "" else "s"}: nada ficou para trás.")
                        }, style = "tonal", small = true, icon = "fast_forward")
                    }
                }
                androidx.compose.runtime.LaunchedEffect(Unit) {
                    if (overdueOpen) { kotlinx.coroutines.delay(250); kotlinx.browser.document.getElementById("atrasadas")?.asDynamic()?.scrollIntoView(js("({behavior:'smooth',block:'start'})")) }
                }
            }
            // "Hoje não vou conseguir estudar": o dia sai do plano e o que estava nele vai para a frente.
            if (dayOffset == 0) {
                val off = br.com.estudario.web.data.Planning.isDayOff(data, plan.id, todayEpoch)
                val pendingToday = dayTasks.filter { it.status == "PLANEJADA" || it.status == "EM_ANDAMENTO" }
                if (off) Div({ classes("row", "between", "dayoff-note") }) {
                    Span({ classes("small") }) { Text("Hoje ficou sem estudo. O plano já passou tudo para a frente.") }
                    Btn("Desfazer", { Store.update { br.com.estudario.web.data.Planning.setDayOff(it, plan.id, todayEpoch, false) } }, style = "ghost", small = true)
                } else if (pendingToday.isNotEmpty()) Div({ classes("row", "end") }) {
                    Btn("Hoje não vou conseguir estudar", {
                        if (kotlinx.browser.window.confirm("Os ${pendingToday.size} estudos de hoje (${Queries.minutesLabel(pendingToday.sumOf { it.minutes })}) vão para os próximos dias, sem passar das horas que você tem. Se der para estudar depois, é só desfazer.")) {
                            Store.update { br.com.estudario.web.data.Planning.setDayOff(it, plan.id, todayEpoch, true) }
                            Toast.show("Tudo bem, acontece. Os estudos de hoje foram para os próximos dias.")
                        }
                    }, style = "ghost", small = true, icon = "event_busy")
                }
            }
            WhyThisPlan(plan.id, whyOpen) { whyOpen = !whyOpen }
            if (dayTasks.isEmpty()) {
                Card { Empty("weekend", "Nada planejado neste dia", "Use o tempo livre para revisar, treinar questões ou adiantar o edital.") }
            } else {
                // Agrupado por matéria, como os cartões do app.
                Div({ classes("stack") }) {
                    dayTasks.forEach { task -> androidx.compose.runtime.key(task.id) { TaskRow(task) { completing = task } } }
                }
                // Hoje feito: o próximo dia aparece logo abaixo, com "Adiantar" em cada matéria.
                val next = planTasks.filter { it.day > todayEpoch && it.status == "PLANEJADA" }.minOfOrNull { it.day }
                if (dayOffset == 0 && next != null && dayTasks.none { it.status == "PLANEJADA" || it.status == "EM_ANDAMENTO" }) {
                    Div({ classes("advance-head") }) {
                        org.jetbrains.compose.web.dom.H3 { Text("Dia concluído! Quer adiantar?") }
                        P({ classes("small", "muted") }) { Text("Estas são as matérias de ${dayTitle(next, todayEpoch).lowercase()}. Adiantando agora, o plano segue daqui.") }
                    }
                    Div({ classes("stack") }) {
                        Queries.tasksOn(data, plan.id, next).forEach { task -> androidx.compose.runtime.key("adiantar-" + task.id) { TaskRow(task) { completing = task } } }
                    }
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
        PlanView.OVERVIEW -> Unit
        PlanView.MONTH -> PlanMonth(plan.id, planTasks, todayEpoch, monthOffset, { monthOffset = it }, { day -> dayOffset = (day - todayEpoch).toInt(); view = PlanView.TODAY }, { upgradeEdit = true })
    }

    if (view == PlanView.OVERVIEW) PlanOverview(plan.id, planTasks, todayEpoch, plan.exam)

    completing?.let { task -> CompleteTaskDialog(task) { completing = null } }
}

private fun List<PlanTask>.load() = filter { it.status != "REPROGRAMADA" && it.status != "CANCELADA" }

/**
 * O plano em uma olhada (PlanProgressHeader do app): para onde ele vai, quanto já foi e a missão de
 * hoje. É a primeira coisa da aba; dá para entender o plano sem abrir nada.
 */
@Composable
private fun PlanProgressHeader(plan: br.com.estudario.web.data.StudyPlan, tasks: List<PlanTask>, todayEpoch: Long) {
    val load = tasks.load()
    val total = load.size
    val done = load.count { it.status == "CONCLUIDA" }
    val pct = percent(done, total)
    val today = load.filter { it.day == todayEpoch }
    val todayDone = today.count { it.status == "CONCLUIDA" }
    val overdue = load.count { it.day < todayEpoch && (it.status == "PLANEJADA" || it.status == "EM_ANDAMENTO") }
    val daysToExam = plan.exam?.let { it - todayEpoch }?.takeIf { it >= 0 }
    // Previsão do edital inteiro (as tarefas marcadas só cobrem as próximas 4 semanas).
    val prediction = rememberForecast(Store.data, plan)
    val projection = prediction.result
    val forecast = if (projection != null) projection.date?.takeIf { projection.remainingTopics > 0 }?.toEpochDays()?.toLong()
        else load.filter { it.status == "PLANEJADA" || it.status == "EM_ANDAMENTO" }.let { p -> p.filter { it.type == "THEORY" }.maxOfOrNull { it.day } ?: p.maxOfOrNull { it.day } }
    val next = load.filter { it.day > todayEpoch && it.status == "PLANEJADA" }.minOfOrNull { it.day }
    Div({ classes("home-hero", "plan-hero", "rise") }) {
        Div({ classes("hh-top") }) {
            Div({ classes("grow") }) {
                Span({ classes("eyebrow") }) { Text("SEU PLANO") }
                H2({ classes("hh-title") }) { Text(plan.objective.takeIf { it.isNotBlank() } ?: plan.name) }
            }
            if (daysToExam != null) Div({ classes("hh-count") }) {
                B { Text("$daysToExam") }
                Span { Text(if (daysToExam == 1L) "dia para a prova" else "dias para a prova") }
            }
        }
        Div({ classes("hh-cover") }) {
            Div({ classes("row", "between") }) { Span { Text("$pct% do plano cumprido") }; B { Text("$done/$total") } }
            Div({ classes("hh-bar") }) { Span({ attr("style", "width:${pct.coerceAtLeast(2)}%") }) }
            P({ classes("small") }) {
                Text(when {
                    prediction.loading -> "Calculando a previsão…"
                    prediction.failed -> "Previsão indisponível no momento. Você pode continuar estudando normalmente."
                    else -> forecast?.let { "No ritmo atual você fecha o edital em ${Queries.dayLabel(LocalDate.fromEpochDays(it))}." } ?: "Todo o conteúdo previsto já foi coberto. Agora é revisão e questões."
                })
            }
        }
        // A semana em bolinhas: cheia quando o dia foi feito, contornada quando tem estudo, apagada na folga.
        val monday = todayEpoch - (LocalDate.fromEpochDays(todayEpoch.toInt()).dayOfWeek.ordinal)
        Div({ classes("week-dots", "week-track") }) {
            listOf("S", "T", "Q", "Q", "S", "S", "D").forEachIndexed { i, label ->
                val day = monday + i
                val dayTasks = load.filter { it.day == day }
                val complete = dayTasks.isNotEmpty() && dayTasks.all { it.status == "CONCLUIDA" }
                val state = when {
                    complete -> "done"
                    day == todayEpoch -> "today"
                    day < todayEpoch && dayTasks.isNotEmpty() -> "missed"
                    dayTasks.isNotEmpty() -> "planned"
                    else -> "rest"
                }
                Div({ classes("wd", state) }) {
                    Span({ classes("wd-l") }) { Text(label) }
                    Span({ classes("wd-c") }) { Text(if (complete) "✓" else "${LocalDate.fromEpochDays(day.toInt()).day}") }
                }
            }
        }
        // A missão de hoje: anel com as atividades feitas, o tempo do dia e a barra de minutos.
        val plannedMin = today.sumOf { it.minutes }
        val doneMin = today.filter { it.status == "CONCLUIDA" }.sumOf { it.minutes }
        val finished = today.isNotEmpty() && todayDone == today.size
        Div({ classes(*listOfNotNull("plan-mission", if (finished) "ok" else null).toTypedArray()) }) {
            val ring = if (today.isEmpty()) 0 else todayDone * 100 / today.size
            Div({ classes("pm-ring"); attr("style", "--p:$ring") }) {
                if (finished) Icon("check") else if (today.isEmpty()) Icon("bedtime") else B { Text("$todayDone/${today.size}") }
            }
            Div({ classes("pm-text") }) {
                Span({ classes("pm-eyebrow") }) { Text("MISSÃO DE HOJE") }
                B({ classes("pm-title") }) {
                    Text(
                        when {
                            today.isEmpty() -> "Folga no plano"
                            finished -> "Missão cumprida"
                            else -> "${today.size - todayDone} atividade${if (today.size - todayDone == 1) "" else "s"} · ${Queries.minutesLabel(plannedMin - doneMin)}"
                        },
                    )
                }
                Span({ classes("pm-sub") }) {
                    Text(
                        when {
                            today.isEmpty() && next != null -> "Próximo estudo: ${dayTitle(next, todayEpoch).lowercase()}, ${load.count { it.day == next }} atividade(s)."
                            today.isEmpty() -> "Dia livre. Aproveite para descansar."
                            finished -> "Tudo feito hoje. Amanhã o plano continua."
                            else -> "${Queries.minutesLabel(doneMin)} de ${Queries.minutesLabel(plannedMin)} estudados hoje"
                        },
                    )
                }
                if (today.isNotEmpty() && !finished) Div({ classes("pm-bar") }) { Span({ attr("style", "width:${(if (plannedMin == 0) 0 else doneMin * 100 / plannedMin).coerceAtLeast(3)}%") }) }
            }
            if (overdue > 0) Span({ classes("late-pill") }) { Text("$overdue atrasada${if (overdue == 1) "" else "s"}") }
        }
    }
}

/** Visão geral: como fica sua semana (barras por dia), para onde vai o tempo e o andamento. */
@Composable
private fun PlanOverview(planId: String, tasks: List<PlanTask>, todayEpoch: Long, exam: Long?) {
    WeeklyBalanceCard(planId)
    val load = tasks.load()
    val window = load.filter { it.day in todayEpoch..(todayEpoch + 13) }
    if (window.isNotEmpty()) Card(extra = "pad-lg") {
        val perWeekday = IntArray(7)
        window.forEach { perWeekday[LocalDate.fromEpochDays(it.day).isoDayOfWeek - 1] += it.minutes }
        val avg = perWeekday.map { it / 2 }
        val max = (avg.maxOrNull() ?: 0).coerceAtLeast(1)
        H3 { Text("Como fica sua semana") }
        P({ classes("muted", "small") }) { Text("Cerca de ${Queries.minutesLabel(avg.sum())} por semana, divididos assim:") }
        Div({ classes("week-shape") }) {
            avg.forEachIndexed { i, minutes ->
                Div({ classes("ws-day") }) {
                    Span({ classes("xs", if (minutes == 0) "muted" else "strong") }) { Text(if (minutes == 0) "folga" else Queries.minutesLabel(minutes)) }
                    Div({ classes("ws-col") }) { if (minutes > 0) Span({ attr("style", "height:${(minutes * 100 / max).coerceAtLeast(8)}%") }) }
                    B({ classes("small") }) { Text(listOf("S", "T", "Q", "Q", "S", "S", "D")[i]) }
                }
            }
        }
        val bySubject = window.groupBy { it.subjectName.ifBlank { "Geral" } }.mapValues { (_, l) -> l.sumOf { it.minutes } }.entries.sortedByDescending { it.value }
        val top = bySubject.take(6)
        val rest = bySubject.drop(6).sumOf { it.value }
        val total = bySubject.sumOf { it.value }.coerceAtLeast(1)
        H3({ attr("style", "margin-top:18px") }) { Text("Para onde vai o seu tempo") }
        Div({ classes("split-bar") }) {
            top.forEachIndexed { i, (name, minutes) -> key(name) { Span({ attr("style", "width:${minutes * 100.0 / total}%;background:var(--subject-$i)"); attr("title", "$name · ${minutes * 100 / total}%") }) } }
            if (rest > 0) Span({ attr("style", "width:${rest * 100.0 / total}%;background:var(--soft-2)") })
        }
        Div({ classes("stack", "tight"); attr("style", "margin-top:10px") }) {
            top.forEachIndexed { i, (name, minutes) ->
                key(name) {
                    Div({ classes("row") }) {
                        Span({ classes("adot", "lg"); attr("style", "background:var(--subject-$i)") })
                        Span({ classes("grow", "small", "clamp-2") }) { Text(name) }
                        B({ classes("small") }) { Text("${minutes * 100 / total}%") }
                    }
                }
            }
            if (rest > 0) P({ classes("xs", "muted") }) { Text("Outras matérias: ${rest * 100 / total}%") }
        }
    }
    val done = load.count { it.status == "CONCLUIDA" }
    val planned = load.sumOf { it.minutes }
    val actual = Store.data.executions.filter { it.planId == planId }.sumOf { it.minutes }
    Div({ classes("grid", "cols-3") }) {
        Card(extra = "flat") { Stat("$done de ${load.size}", "missões concluídas") }
        Card(extra = "flat") { Stat(Queries.minutesLabel(actual), "estudadas no plano") }
        Card(extra = "flat") { Stat(Queries.minutesLabel(planned), "planejadas no total") }
    }
    val bySubjectAll = load.groupBy { it.subjectName.ifBlank { "Geral" } }
    Card {
        CardHead("Andamento por matéria")
        Div({ classes("stack") }) {
            bySubjectAll.entries.sortedByDescending { it.value.size }.forEach { (name, list) ->
                key(name) {
                    val d = list.count { it.status == "CONCLUIDA" }
                    Div({ classes("stack", "tight") }) {
                        Div({ classes("row", "between") }) {
                            Div({ classes("row"); attr("style", "gap:8px;min-width:0") }) { Span({ classes("adot", "lg"); attr("style", "background:${subjectColor(name)}") }); B({ classes("small", "clamp-2") }) { Text(name) } }
                            Span({ classes("small", "muted", "nowrap") }) { Text("$d/${list.size}") }
                        }
                        Div({ classes("bar") }) { Span({ attr("style", "width:${percent(d, list.size)}%;background:${subjectColor(name)}") }) }
                    }
                }
            }
        }
    }
    val plan = br.com.estudario.web.data.Store.data.plans.firstOrNull { it.id == planId }
    val prediction = rememberForecast(Store.data, plan)
    val projection = prediction.result
    if (prediction.loading || prediction.failed) P({ classes("small", "muted"); attr("role", "status") }) {
        Text(if (prediction.loading) "Calculando a previsão…" else "Previsão indisponível no momento. Você pode continuar estudando normalmente.")
    }
    // Não cabe: a pessoa escolhe entre aumentar as horas e focar no que mais cai (guardado no navegador).
    val focusKey = "estudario.plano.foco.$planId"
    var focus by androidx.compose.runtime.remember(planId) { androidx.compose.runtime.mutableStateOf(runCatching { kotlinx.browser.localStorage.getItem(focusKey) == "1" }.getOrDefault(false)) }
    fun chooseFocus(value: Boolean) { focus = value; runCatching { if (value) kotlinx.browser.localStorage.setItem(focusKey, "1") else kotlinx.browser.localStorage.removeItem(focusKey) } }
    val fitRoadmap = projection?.roadmap?.takeIf { projection.remainingTopics > 0 && it.verdict == br.com.estudario.domain.planner.RoadmapVerdict.DOES_NOT_FIT }
    if (fitRoadmap != null) {
        val r = fitRoadmap
        val daily = r.weeklyCapacityMinutes / r.studyDaysPerWeek.coerceIn(1, 7)
        val leftOut = "${r.topicsLeftOut} ${if (r.topicsLeftOut == 1) "tópico" else "tópicos"}"
        val from = r.atRiskSubjects.takeIf { it.isNotEmpty() }?.take(2)?.joinToString(" e ")
        Div({ classes("card", "fit-card", if (focus) "focus" else "alert") }) {
            Div({ classes("row") }) {
                Icon(if (focus) "center_focus_strong" else "error")
                Div({ classes("grow") }) {
                    H2 { Text(if (focus) "Foco no que mais cai" else "O edital inteiro não cabe nas suas horas") }
                    P({ classes("small", "muted") }) {
                        Text(
                            if (focus) "Você cobre ${r.weightCoveredPercent}% do peso da prova. Ficam para o fim os $leftOut que menos caem nas provas${from?.let { ", mais de $it" } ?: ""}: se sobrar tempo, eles entram."
                            else "Com ${Queries.minutesLabel(daily)} por dia de estudo, ficam $leftOut de fora${from?.let { ", principalmente de $it" } ?: ""}. Escolha como seguir:",
                        )
                    }
                }
            }
            if (focus) {
                Div({ attr("style", "margin-top:12px") }) { ProgressBar(r.weightCoveredPercent / 100.0, "green") }
                Div({ classes("row"); attr("style", "margin-top:12px") }) { Btn("Quero ver tudo", { chooseFocus(false); Router.go(Route.PlanSettings) }, style = "ghost", small = true) }
            } else Div({ classes("fit-choices") }) {
                Button({ classes("fit-choice"); onClick { Router.go(Route.PlanSettings) } }) {
                    Icon("schedule")
                    Div({ classes("grow") }) {
                        B { Text("Ver o edital inteiro") }
                        Span({ classes("small", "muted") }) {
                            Text(if (r.extraMinutesPerDayToFit > 0) "Mais ${r.extraMinutesPerDayToFit} min por dia: de ${Queries.minutesLabel(daily)} para ${Queries.minutesLabel(daily + r.extraMinutesPerDayToFit)}." else "Aumente as horas da semana.")
                        }
                    }
                    Icon("arrow_forward")
                }
                Button({ classes("fit-choice"); onClick { chooseFocus(true) } }) {
                    Icon("center_focus_strong")
                    Div({ classes("grow") }) {
                        B { Text("Focar no que mais cai") }
                        Span({ classes("small", "muted") }) { Text("Fica com ${Queries.minutesLabel(daily)} por dia de estudo e cobre ${r.weightCoveredPercent}% do peso da prova. Saem por último os tópicos que menos caem.") }
                    }
                    Icon("arrow_forward")
                }
            }
        }
    }
    if (projection != null && fitRoadmap == null) {
        val r = projection.roadmap
        val text = when {
            projection.remainingTopics == 0 -> "Todos os ${r.totalTopics} tópicos já foram vistos. Daqui até a prova é revisão e questões."
            r.verdict == br.com.estudario.domain.planner.RoadmapVerdict.DOES_NOT_FIT ->
                "Com as horas de hoje, ${r.topicsLeftOut} de ${projection.remainingTopics} tópicos não cabem antes da reta final" +
                    (r.atRiskSubjects.takeIf { it.isNotEmpty() }?.let { " (mais em ${it.take(3).joinToString()})" } ?: "") +
                    ". Mais ${r.extraMinutesPerDayToFit} min por dia fariam caber." +
                    (projection.finishAtPace?.let { " No ritmo atual o edital fecha em ${Queries.dayLabel(it)}." } ?: "")
            r.verdict == br.com.estudario.domain.planner.RoadmapVerdict.TIGHT ->
                "Cabe, mas apertado: o último tópico é visto em ${Queries.dayLabel(r.coverageDate!!)}, com pouca folga antes da reta final (${Queries.dayLabel(r.contentDeadline)})."
            else -> "O edital inteiro é visto até ${Queries.dayLabel(r.coverageDate!!)}, ${r.slackDays} dias antes da reta final" + (if (exam != null) "; a prova é em ${Queries.dayLabel(LocalDate.fromEpochDays(exam))}." else ".")
        }
        Card(extra = "soft") {
            Div({ classes("row") }) {
                Icon("event_available")
                Div({ classes("grow") }) {
                    B { Text("Previsão de conclusão") }
                    P({ classes("small", "muted") }) { Text(text) }
                }
            }
        }
    }
}

internal fun dayTitle(day: Long, today: Long): String {
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

/** O tipo do estudo na legenda do mês (a mesma cor do app). */
private fun taskKind(type: String) = when (type) {
    "THEORY" -> "theory"
    "QUESTIONS" -> "questions"
    "SIMULATION", "DISCURSIVE" -> "sim"
    else -> "review"
}

/** "Sua semana": o balanço dos últimos 7 dias (WeeklyBalance, o mesmo do app) e a previsão do edital. */
@Composable
private fun WeeklyBalanceCard(planId: String) {
    val data = Store.data
    // Refaz só quando muda o que entra na conta (tarefas, tempo registrado, respostas, tópicos) ou o dia.
    val balance = androidx.compose.runtime.remember(planId, data.tasks.size, data.attempts.size, data.topics.count { it.status != "NAO_ESTUDADO" }, data.array(br.com.estudario.web.data.Keys.EXECUTIONS).size, Queries.todayEpoch()) {
        runCatching { br.com.estudario.web.data.Planning.inputFor(data, planId)?.let { br.com.estudario.domain.planner.WeeklyBalance.from(it) } }.getOrNull()
    } ?: return
    val plan = data.plans.firstOrNull { it.id == planId }
    val roadmap = rememberForecast(data, plan).result?.roadmap
    Div({ classes("card", "week-balance", if (balance.idle) "idle" else "on") }) {
        Span({ classes("wb-label") }) { Text("Sua semana") }
        H2 { Text(balance.headline()) }
        Div({ classes("wb-stats") }) {
            listOfNotNull(
                br.com.estudario.domain.planner.WeeklyBalance.hours(balance.minutesStudied) to "estudadas",
                "${balance.daysStudied}/7" to "dias",
                "${balance.newTopics}" to (if (balance.newTopics == 1) "tópico novo" else "tópicos novos"),
                balance.accuracy?.let { "$it%" to "acerto" },
            ).forEach { (value, label) -> Div({ classes("wb-stat") }) { B { Text(value) }; Span({ classes("xs", "muted") }) { Text(label) } } }
        }
        balance.details().forEach { line -> P({ classes("small", "muted"); attr("style", "margin:4px 0 0") }) { Text(line) } }
        Div({ attr("style", "margin-top:10px") }) { ProgressBar(balance.seenPercent / 100.0, "green") }
        roadmap?.let { r ->
            val text = when {
                r.verdict != br.com.estudario.domain.planner.RoadmapVerdict.DOES_NOT_FIT && r.coverageDate != null ->
                    "No seu ritmo, o edital fecha em ${Queries.dayLabel(r.coverageDate)}" + (if (r.slackDays > 0) ", ${r.slackDays} dias antes da reta final. Você vai conseguir." else ", colado na reta final.")
                r.verdict == br.com.estudario.domain.planner.RoadmapVerdict.DOES_NOT_FIT -> "No ritmo de hoje ficam ${r.topicsLeftOut} tópicos de fora; veja abaixo como resolver."
                else -> null
            }
            text?.let { P({ classes("small", "strong"); attr("style", "margin:10px 0 0") }) { Text(it) } }
        }
    }
}
