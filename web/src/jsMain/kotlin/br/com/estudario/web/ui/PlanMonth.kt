package br.com.estudario.web.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import br.com.estudario.time.isoDayOfWeek
import br.com.estudario.time.toEpochDay
import br.com.estudario.web.data.PlanTask
import br.com.estudario.web.data.Planning
import br.com.estudario.web.data.Queries
import br.com.estudario.web.data.Store
import kotlinx.datetime.LocalDate
import org.jetbrains.compose.web.dom.B
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H3
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text

private val PENDING = setOf("PLANEJADA", "EM_ANDAMENTO")

/** O que o menu do botão direito está mostrando: um dia ou um estudo, e onde abrir. */
private class MenuAt(val x: Int, val y: Int, val day: Long, val task: PlanTask?)

/** Uma mudança que passa do limite do dia (ou cai numa folga) e precisa da pessoa decidir. */
private class PendingMove(val task: PlanTask, val toDay: Long, val load: Int, val limit: Int, val swapWith: PlanTask? = null)

/** Uma revisão espaçada que vence no dia (uma por tópico, a próxima etapa). */
private class DayReview(val topic: String, val stage: Int)

private fun kindOf(task: PlanTask, todayEpoch: Long): String = when {
    task.status == "CONCLUIDA" -> "done"
    task.day < todayEpoch && task.status in PENDING -> "late"
    task.type == "THEORY" -> "theory"
    task.type == "QUESTIONS" -> "questions"
    task.type == "SIMULATION" || task.type == "DISCURSIVE" -> "sim"
    else -> "review"
}

private fun kindLabel(kind: String) = when (kind) {
    "theory" -> "Teoria"; "questions" -> "Questões"; "late" -> "Atrasado"; "done" -> "Feito"; "sim" -> "Simulado"; else -> "Revisão"
}

/** "TI – Segurança da Informação" vira "Segurança da Informação" no quadradinho do dia. */
private fun shortSubject(name: String) = name.substringAfter(" – ").substringAfter(" - ").trim()

/** Tempo curto: "1h30", "45min", "2h". */
private fun short(minutes: Int): String = when {
    minutes < 60 -> "${minutes}min"
    minutes % 60 == 0 -> "${minutes / 60}h"
    else -> "${minutes / 60}h${(minutes % 60).toString().padStart(2, '0')}"
}

/**
 * O mês do plano como um calendário de verdade: todos os dias do mesmo tamanho, com as matérias do
 * dia em linhas curtas (bolinha na cor do tipo), as revisões espaçadas que vencem nele e quanto do
 * limite do dia já está ocupado. Ao lado (embaixo no celular) fica o dia escolhido inteiro, legível.
 *
 * - Um clique escolhe o dia (o painel mostra tudo dele); clicar de novo ou dois cliques abre o dia.
 * - Botão direito no dia ou num estudo abre as ações.
 * - Nos pagos, arrastar um estudo (do painel ou do calendário) para outro dia muda ele de dia e o
 *   deixa fixado. Se passar do limite, o plano pergunta: trocar com um estudo de lá, colocar mesmo
 *   assim ou cancelar. Soltar em cima de outro estudo propõe trocar os dois.
 */
@Composable
fun PlanMonth(planId: String, planTasks: List<PlanTask>, todayEpoch: Long, monthOffset: Int, onMonth: (Int) -> Unit, onOpenDay: (Long) -> Unit, onUpgrade: () -> Unit) {
    val data = Store.data
    val canEdit = Store.demo || br.com.estudario.web.data.LibraryGaps.knownPaid()
    var selected by remember { mutableStateOf(todayEpoch) }
    var menu by remember { mutableStateOf<MenuAt?>(null) }
    var pending by remember { mutableStateOf<PendingMove?>(null) }

    val today = LocalDate.fromEpochDays(todayEpoch.toInt())
    val monthIndex = today.year * 12 + today.month.ordinal + monthOffset
    val first = LocalDate(monthIndex / 12, monthIndex % 12 + 1, 1)
    val firstEpoch = first.toEpochDay()
    val nextMonth = LocalDate((monthIndex + 1) / 12, (monthIndex + 1) % 12 + 1, 1).toEpochDay()
    val gridStart = firstEpoch - (first.isoDayOfWeek - 1)
    val gridEnd = gridStart + ((nextMonth - gridStart + 6) / 7) * 7
    val byDay = planTasks.filter { it.day in gridStart until gridEnd && it.status !in setOf("REPROGRAMADA", "CANCELADA", "NAO_REALIZADA") }
        .groupBy { it.day }
    // Revisões espaçadas pendentes, no dia em que vencem (as vencidas contam em hoje).
    val reviewsByDay = remember(data.reviews, data.topics, todayEpoch) {
        val topicName = data.topics.associate { it.id to it.title }
        data.reviews.filter { it.completedAt == null && it.ignoredAt == null }.groupBy { it.topicId }.map { (_, l) -> l.minBy { it.stage } }
            .groupBy { maxOf(Queries.dateOf(it.dueAt).toEpochDay(), todayEpoch) }
            .mapValues { (_, l) -> l.map { DayReview(topicName[it.topicId] ?: "Tópico", it.stage) } }
    }
    val lastPlanned = planTasks.filter { it.status in PENDING }.maxOfOrNull { it.day }

    fun drop(taskId: String, toDay: Long, onTask: PlanTask?) {
        val task = planTasks.firstOrNull { it.id == taskId } ?: return
        if (task.day == toDay && onTask == null) return
        if (onTask != null && onTask.id != task.id && onTask.status in PENDING) {
            pending = PendingMove(task, toDay, 0, 0, swapWith = onTask); return
        }
        val limit = Planning.dayLimit(data, planId, toDay)
        val load = Planning.dayLoad(data, planId, toDay)
        if (load + task.minutes > limit) { pending = PendingMove(task, toDay, load, limit); return }
        Store.update { Planning.moveTask(it, task.id, toDay) }
        selected = toDay
        Toast.show("Estudo mudado para ${dayTitle(toDay, todayEpoch)}. Ele fica fixado lá.")
    }

    // arrastar: o que pode ser arrastado e onde pode cair (dia de hoje em diante)
    fun dragSource(task: PlanTask): org.jetbrains.compose.web.attributes.AttrsScope<*>.() -> Unit = {
        if (task.status in PENDING) {
            attr("draggable", "true")
            addEventListener("dragstart") { e ->
                if (!canEdit) { e.preventDefault(); onUpgrade(); return@addEventListener }
                e.nativeEvent.asDynamic().dataTransfer.setData("text/plain", task.id)
                e.nativeEvent.asDynamic().dataTransfer.effectAllowed = "move"
            }
        }
    }

    Div({ classes("mo-top") }) {
        Div({ classes("mo-nav") }) {
            IconButton("chevron_left", "Mês anterior") { onMonth(monthOffset - 1) }
            B({ classes("mo-month") }) { Text("${Queries.monthLong[first.month.ordinal].replaceFirstChar(Char::uppercase)} de ${first.year}") }
            IconButton("chevron_right", "Próximo mês") { onMonth(monthOffset + 1) }
            if (monthOffset != 0) Btn("Hoje", { onMonth(0); selected = todayEpoch }, style = "ghost", small = true)
        }
        Div({ classes("mo-legend") }) {
            listOf("Teoria" to "theory", "Questões" to "questions", "Revisão" to "review", "Simulado" to "sim", "Atrasado" to "late").forEach { (label, k) ->
                Span({ classes("mo-lg", k) }) { Text(label) }
            }
        }
    }

    Div({ classes("mo-layout") }) {
        // ------------------------------------------------ o calendário
        Div({ classes("mo-cal") }) {
            // O caminho do edital neste mês (o plano marca as tarefas só algumas semanas à frente;
            // daqui a pessoa vê o que vem no mês inteiro e nos próximos).
            val forecast = rememberForecast(data, Queries.activePlan(data)).result
            val monthPlan = forecast?.roadmap?.months?.firstOrNull { it.year == first.year && it.month == first.month.ordinal + 1 }
            if (monthPlan != null) Div({ classes("mo-path") }) {
                B { Text(if (monthPlan.newTopics == 0) "Neste mês: revisão, questões e simulados" else "Neste mês: ${monthPlan.newTopics} tópicos novos") }
                if (monthPlan.subjects.isNotEmpty()) Div({ classes("mo-path-subjects") }) {
                    monthPlan.subjects.take(6).forEach { sl -> Span({ classes("mo-chip") }) { Text("${shortSubject(sl.name)} · ${sl.newTopics}") } }
                    if (monthPlan.subjects.size > 6) Span({ classes("mo-chip", "more") }) { Text("+${monthPlan.subjects.size - 6} matérias") }
                }
                Span({ classes("small", "muted") }) { Text("Até o fim do mês, ${monthPlan.coveragePercent}% do edital visto.") }
            }
            Div({ classes("mo-grid") }) {
                listOf("Seg", "Ter", "Qua", "Qui", "Sex", "Sáb", "Dom").forEach { Div({ classes("mo-dow") }) { Text(it) } }
                var epoch = gridStart
                while (epoch < gridEnd) {
                    val day = epoch
                    val list = byDay[day].orEmpty().sortedWith(compareBy({ it.status == "CONCLUIDA" }, { it.type != "THEORY" }))
                    val reviews = reviewsByDay[day].orEmpty()
                    val limit = Planning.dayLimit(data, planId, day)
                    val load = list.sumOf { it.minutes }
                    val off = limit == 0
                    val past = day < todayEpoch
                    val done = list.count { it.status == "CONCLUIDA" }
                    Div({
                        classes(*listOfNotNull(
                            "mo-day",
                            if (day < firstEpoch || day >= nextMonth) "out" else null,
                            if (day == todayEpoch) "today" else null,
                            if (selected == day) "selected" else null,
                            if (past) "past" else null,
                            if (off) "off" else null,
                        ).toTypedArray())
                        onClick { if (selected == day) onOpenDay(day) else selected = day }
                        addEventListener("dblclick") { onOpenDay(day) }
                        addEventListener("contextmenu") { e ->
                            e.preventDefault()
                            val ev = e.nativeEvent.asDynamic()
                            menu = MenuAt((ev.clientX as Number).toInt(), (ev.clientY as Number).toInt(), day, null)
                        }
                        if (canEdit && !past) {
                            addEventListener("dragover") { e -> e.preventDefault(); (e.nativeEvent.currentTarget as? org.w3c.dom.Element)?.classList?.add("drop") }
                            addEventListener("dragleave") { e -> (e.nativeEvent.currentTarget as? org.w3c.dom.Element)?.classList?.remove("drop") }
                            addEventListener("drop") { e ->
                                e.preventDefault(); (e.nativeEvent.currentTarget as? org.w3c.dom.Element)?.classList?.remove("drop")
                                val id = e.nativeEvent.asDynamic().dataTransfer.getData("text/plain") as? String
                                if (!id.isNullOrBlank()) drop(id, day, null)
                            }
                        }
                    }) {
                        Div({ classes("mo-head") }) {
                            Span({ classes("mo-n") }) { Text("${LocalDate.fromEpochDays(day.toInt()).day}") }
                            when {
                                off -> Span({ classes("mo-cap") }) { Text("folga") }
                                list.isNotEmpty() -> Span({ classes(*listOfNotNull("mo-cap", if (load > limit) "over" else null).toTypedArray()) }) { Text("${short(load)}/${short(limit)}") }
                            }
                        }
                        if (!off && list.isNotEmpty()) Div({ classes(*listOfNotNull("mo-load", if (load > limit) "over" else null).toTypedArray()) }) {
                            Span({ attr("style", "width:${(load * 100 / limit.coerceAtLeast(1)).coerceIn(4, 100)}%") })
                        }
                        if (past && list.isNotEmpty()) {
                            // Dia que passou: só o resumo (a lista fica no painel).
                            Div({ classes(*listOfNotNull("mo-sum", if (done < list.size) "late" else null).toTypedArray()) }) {
                                Text(if (done == list.size) "✓ tudo feito" else "$done de ${list.size} feitos")
                            }
                        } else {
                            val show = if (reviews.isEmpty()) 4 else 3
                            list.take(show).forEach { task ->
                                val kind = kindOf(task, todayEpoch)
                                Div({
                                    classes("mo-item", kind)
                                    attr("title", "${kindLabel(kind)} · ${task.topicName ?: task.subjectName} · ${short(task.minutes)}")
                                    dragSource(task)(this)
                                }) { Text(shortSubject(task.subjectName)) }
                            }
                            if (list.size > show) Div({ classes("mo-more") }) { Text("+${list.size - show} estudos") }
                            if (reviews.isNotEmpty()) Div({ classes("mo-item", "review", "srs") }) {
                                Text(if (reviews.size == 1) "↻ 1 revisão" else "↻ ${reviews.size} revisões")
                            }
                        }
                    }
                    epoch++
                }
            }
            if (lastPlanned != null && lastPlanned < gridEnd - 1 && gridEnd - 1 > todayEpoch) P({ classes("small", "muted", "mo-note") }) {
                Text("O plano marca os estudos dia a dia até ${dayTitle(lastPlanned, todayEpoch).lowercase()}. Depois disso ele continua montando conforme você estuda (o caminho do edital inteiro está em Visão geral).")
            }
        }

        // ------------------------------------------------ o dia escolhido
        val dayTasks = byDay[selected].orEmpty().sortedWith(compareBy({ it.status == "CONCLUIDA" }, { it.type != "THEORY" }))
        val dayReviews = reviewsByDay[selected].orEmpty()
        val limit = Planning.dayLimit(data, planId, selected)
        val load = dayTasks.sumOf { it.minutes }
        val selPast = selected < todayEpoch
        Div({
            classes("mo-panel")
            if (canEdit && !selPast) {
                addEventListener("dragover") { e -> e.preventDefault() }
                addEventListener("drop") { e ->
                    e.preventDefault()
                    val id = e.nativeEvent.asDynamic().dataTransfer.getData("text/plain") as? String
                    if (!id.isNullOrBlank()) drop(id, selected, null)
                }
            }
        }) {
            H3({ classes("mo-ptitle") }) { Text(dayTitle(selected, todayEpoch)) }
            if (limit == 0) P({ classes("small", "muted") }) { Text("Folga no seu plano.") }
            else {
                Div({ classes("mo-pcap") }) {
                    Span { Text("${short(load)} de ${short(limit)}") }
                    Span({ classes("muted") }) { Text(if (load > limit) "passou do limite" else "${short(limit - load)} livres") }
                }
                Div({ classes(*listOfNotNull("mo-load", "big", if (load > limit) "over" else null).toTypedArray()) }) {
                    Span({ attr("style", "width:${(load * 100 / limit.coerceAtLeast(1)).coerceIn(0, 100)}%") })
                }
            }
            if (dayTasks.isEmpty() && dayReviews.isEmpty()) P({ classes("small", "muted"); attr("style", "margin-top:10px") }) {
                Text(if (selPast) "Nada planejado neste dia." else "Nada planejado ainda. Arraste um estudo para cá.")
            }
            Div({ classes("mo-plist") }) {
                dayTasks.forEach { task ->
                    val kind = kindOf(task, todayEpoch)
                    Div({
                        classes(*listOfNotNull("mo-card", kind, if (task.status in PENDING && canEdit) "movable" else null).toTypedArray())
                        dragSource(task)(this)
                        if (canEdit && !selPast && task.status in PENDING) addEventListener("drop") { e ->
                            e.preventDefault(); e.stopPropagation()
                            val id = e.nativeEvent.asDynamic().dataTransfer.getData("text/plain") as? String
                            if (!id.isNullOrBlank() && id != task.id) drop(id, selected, task)
                        }
                        addEventListener("contextmenu") { e ->
                            e.preventDefault(); e.stopPropagation()
                            val ev = e.nativeEvent.asDynamic()
                            menu = MenuAt((ev.clientX as Number).toInt(), (ev.clientY as Number).toInt(), selected, task)
                        }
                    }) {
                        Div({ classes("mo-ckind") }) { Text("${kindLabel(kind)} · ${short(task.minutes)}" + if (task.locked) " · fixado" else "") }
                        Div({ classes("mo-ctopic") }) { Text(task.topicName ?: task.subjectName) }
                        Div({ classes("mo-csubject") }) { Text(task.subjectName) }
                    }
                }
                if (dayReviews.isNotEmpty()) {
                    Div({ classes("mo-psec") }) { Text("Revisões espaçadas do dia") }
                    dayReviews.forEach { r ->
                        Div({ classes("mo-card", "review", "srs") }) {
                            Div({ classes("mo-ckind") }) { Text("Revisão · ${r.stage}ª volta") }
                            Div({ classes("mo-ctopic") }) { Text(r.topic) }
                        }
                    }
                }
            }
            Div({ classes("mo-pactions") }) {
                Btn("Abrir o dia", { onOpenDay(selected) }, style = "tonal", small = true, icon = "today")
                if (!selPast) {
                    if (Planning.isDayOff(data, planId, selected)) Btn("Tirar a folga", { Store.update { Planning.setDayOff(it, planId, selected, false) } }, style = "ghost", small = true, icon = "event_available")
                    else if (limit > 0) Btn("Folga neste dia", {
                        Store.update { Planning.setDayOff(it, planId, selected, true) }
                        Toast.show("Folga marcada. Os estudos desse dia foram para os próximos dias.")
                    }, style = "ghost", small = true, icon = "event_busy")
                }
            }
            P({ classes("xs", "muted", "mo-hint") }) {
                Text(if (canEdit) "Arraste um estudo para outro dia do calendário. Botão direito mostra mais ações." else "Mudar estudos de dia é dos planos Essencial e Pro. Botão direito mostra as ações.")
            }
        }
    }

    // menu do botão direito
    menu?.let { m ->
        Div({ classes("mo-menu-cover"); onClick { menu = null }; addEventListener("contextmenu") { e -> e.preventDefault(); menu = null } })
        Div({ classes("mo-menu"); attr("style", "left:${m.x}px;top:${m.y}px") }) {
            @Composable fun item(icon: String, label: String, action: () -> Unit) {
                Button({ classes("mo-menu-item"); onClick { menu = null; action() } }) { Icon(icon); Text(label) }
            }
            B({ classes("mo-menu-title") }) { Text(m.task?.let { it.topicName ?: it.subjectName } ?: dayTitle(m.day, todayEpoch)) }
            item("open_in_new", "Abrir o dia") { onOpenDay(m.day) }
            val t = m.task
            if (t != null && t.status in PENDING) {
                if (t.locked) item("push_pin", "Desafixar (o plano volta a organizar)") { Store.update { Planning.unpinTask(it, t.id) } }
                if (m.day >= todayEpoch) item("redo", "Passar para o dia seguinte") { if (!canEdit) onUpgrade() else drop(t.id, m.day + 1, null) }
            }
            if (t == null && m.day >= todayEpoch) {
                if (Planning.isDayOff(data, planId, m.day)) item("event_available", "Tirar a folga deste dia") {
                    Store.update { Planning.setDayOff(it, planId, m.day, false) }
                } else if (Planning.dayLimit(data, planId, m.day) > 0) item("event_busy", "Folga neste dia (os estudos vão para a frente)") {
                    Store.update { Planning.setDayOff(it, planId, m.day, true) }
                    Toast.show("Folga marcada em ${dayTitle(m.day, todayEpoch)}. Os estudos dele foram para os próximos dias.")
                }
            }
        }
    }

    // passou do limite, caiu numa folga ou em cima de outro estudo: a pessoa decide
    pending?.let { p ->
        Modal({ pending = null }) {
            val to = dayTitle(p.toDay, todayEpoch)
            val name = p.task.topicName ?: p.task.subjectName
            if (p.swapWith != null) {
                H3 { Text("Trocar os dois de dia?") }
                P { Text("\"$name\" vai para ${dayTitle(p.swapWith.day, todayEpoch)} e \"${p.swapWith.topicName ?: p.swapWith.subjectName}\" vai para ${dayTitle(p.task.day, todayEpoch)}. Os dois ficam fixados.") }
                Div({ classes("row", "end", "wrap"); attr("style", "gap:8px;margin-top:14px") }) {
                    Btn("Cancelar", { pending = null }, style = "ghost")
                    Btn("Trocar", { Store.update { Planning.swapTasks(it, p.task.id, p.swapWith.id) }; pending = null; Toast.show("Trocados de dia.") })
                }
            } else {
                H3 { Text(if (p.limit == 0) "Esse dia é de folga" else "Esse dia passa do seu limite") }
                P {
                    Text(
                        if (p.limit == 0) "$to é folga no seu plano."
                        else "$to já tem ${short(p.load)} de ${short(p.limit)}. Com \"$name\" (${short(p.task.minutes)}) passa do seu limite.",
                    )
                }
                val swappable = Planning.tasksOfDay(data, planId, p.toDay).filter { it.status in PENDING }
                if (swappable.isNotEmpty()) {
                    P({ classes("small", "muted") }) { Text("Trocar com um estudo de lá (ele vem para ${dayTitle(p.task.day, todayEpoch)}):") }
                    Div({ classes("stack"); attr("style", "gap:6px") }) {
                        swappable.forEach { other ->
                            Button({
                                classes("mo-swap")
                                onClick { Store.update { Planning.swapTasks(it, p.task.id, other.id) }; pending = null; Toast.show("Trocados de dia.") }
                            }) {
                                B { Text(other.topicName ?: other.subjectName) }
                                Span({ classes("small", "muted") }) { Text(" · ${other.subjectName} · ${short(other.minutes)}") }
                            }
                        }
                    }
                }
                Div({ classes("row", "end", "wrap"); attr("style", "gap:8px;margin-top:14px") }) {
                    Btn("Cancelar", { pending = null }, style = "ghost")
                    Btn("Colocar mesmo assim", {
                        Store.update { Planning.moveTask(it, p.task.id, p.toDay) }
                        pending = null; selected = p.toDay; Toast.show("Estudo mudado para $to. Ele fica fixado lá.")
                    }, style = "tonal")
                }
            }
        }
    }
}
