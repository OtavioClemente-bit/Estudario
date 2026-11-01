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

/**
 * O mês do plano, grande e legível: cada dia mostra todos os estudos dele (o que é, de qual matéria e
 * quanto tempo) e quanto do limite do dia já está ocupado.
 *
 * - Um clique marca o dia; clicar de novo nele (ou dois cliques) abre o dia.
 * - Botão direito no dia ou num estudo abre as ações (abrir, folga, desafixar).
 * - Nos planos pagos, arrastar um estudo para outro dia muda ele de dia e o deixa fixado lá. Se o dia
 *   passar do limite, o plano pergunta se quer trocar com um estudo de lá ou colocar mesmo assim.
 *   Soltar em cima de outro estudo propõe trocar os dois.
 */
@Composable
fun PlanMonth(planId: String, planTasks: List<PlanTask>, todayEpoch: Long, monthOffset: Int, onMonth: (Int) -> Unit, onOpenDay: (Long) -> Unit, onUpgrade: () -> Unit) {
    val data = Store.data
    val canEdit = Store.demo || br.com.estudario.web.data.LibraryGaps.knownPaid()
    var selected by remember { mutableStateOf<Long?>(null) }
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

    // Soltar um estudo num dia (ou em cima de outro estudo): confere o limite antes de mudar.
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
        Toast.show("Estudo mudado para ${dayTitle(toDay, todayEpoch)}. Ele fica fixado lá.")
    }

    Div({ classes("pm-bar") }) {
        IconButton("chevron_left", "Mês anterior") { onMonth(monthOffset - 1) }
        H3({ classes("pm-title") }) { Text("${Queries.monthLong[first.month.ordinal].replaceFirstChar(Char::uppercase)} de ${first.year}") }
        IconButton("chevron_right", "Próximo mês") { onMonth(monthOffset + 1) }
        if (monthOffset != 0) Btn("Este mês", { onMonth(0) }, style = "ghost", small = true)
        Span({ classes("grow") })
        Div({ classes("pm-legend") }) {
            listOf("Teoria" to "theory", "Questões" to "questions", "Revisão" to "review", "Atrasado" to "late", "Feito" to "done").forEach { (label, k) ->
                Span({ classes("pm-lg", k) }) { Text(label) }
            }
        }
    }
    P({ classes("small", "muted", "pm-hint") }) {
        Text(
            "Clique num dia para marcar e clique de novo para abrir. Botão direito mostra as ações. " +
                if (canEdit) "Arraste um estudo para outro dia (ou para cima de outro estudo, para trocar)." else "Mudar estudos de dia é dos planos Essencial e Pro.",
        )
    }

    Div({ classes("pm-scroll") }) {
    Div({ classes("pm-grid") }) {
        Queries.weekdayShort.forEach { Div({ classes("pm-dow") }) { Text(it) } }
        var epoch = gridStart
        while (epoch < gridEnd) {
            val day = epoch
            val list = byDay[day].orEmpty().sortedWith(compareBy({ it.status == "CONCLUIDA" }, { it.type != "THEORY" }))
            val limit = Planning.dayLimit(data, planId, day)
            val load = list.sumOf { it.minutes }
            val off = limit == 0
            val past = day < todayEpoch
            val over = !off && load > limit
            Div({
                classes(*listOfNotNull(
                    "pm-day",
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
                Div({ classes("pm-head") }) {
                    Span({ classes("pm-n") }) { Text("${LocalDate.fromEpochDays(day.toInt()).day}") }
                    when {
                        off -> Span({ classes("pm-cap", "off") }) { Text("folga") }
                        list.isNotEmpty() || !past -> Span({ classes(*listOfNotNull("pm-cap", if (over) "over" else null).toTypedArray()) }) {
                            Text("${short(load)}/${short(limit)}")
                        }
                    }
                }
                if (!off) Div({ classes(*listOfNotNull("pm-load", if (over) "over" else null).toTypedArray()) }) {
                    Span({ attr("style", "width:${if (limit == 0) 0 else (load * 100 / limit).coerceIn(0, 100)}%") })
                }
                Div({ classes("pm-tasks") }) {
                    list.forEach { task ->
                        val late = past && task.status in PENDING
                        val kind = when {
                            task.status == "CONCLUIDA" -> "done"
                            late -> "late"
                            task.type == "THEORY" -> "theory"
                            task.type == "QUESTIONS" -> "questions"
                            else -> "review"
                        }
                        val movable = task.status in PENDING
                        Div({
                            classes(*listOfNotNull("pm-task", kind, if (movable) "movable" else null).toTypedArray())
                            attr("title", "${task.topicName ?: task.subjectName} · ${task.subjectName}")
                            if (movable) {
                                attr("draggable", "true")
                                addEventListener("dragstart") { e ->
                                    if (!canEdit) { e.preventDefault(); onUpgrade(); return@addEventListener }
                                    e.nativeEvent.asDynamic().dataTransfer.setData("text/plain", task.id)
                                    e.nativeEvent.asDynamic().dataTransfer.effectAllowed = "move"
                                }
                                if (canEdit && !past) addEventListener("drop") { e ->
                                    e.preventDefault(); e.stopPropagation()
                                    val id = e.nativeEvent.asDynamic().dataTransfer.getData("text/plain") as? String
                                    if (!id.isNullOrBlank() && id != task.id) drop(id, day, task)
                                }
                            }
                            addEventListener("contextmenu") { e ->
                                e.preventDefault(); e.stopPropagation()
                                val ev = e.nativeEvent.asDynamic()
                                menu = MenuAt((ev.clientX as Number).toInt(), (ev.clientY as Number).toInt(), day, task)
                            }
                        }) {
                            Div({ classes("pm-kind") }) {
                                Text(
                                    when (kind) { "theory" -> "Teoria"; "questions" -> "Questões"; "late" -> "Atrasado"; "done" -> "Feito"; else -> "Revisão" } +
                                        " · ${short(task.minutes)}" + if (task.locked) " · 📌" else "",
                                )
                            }
                            Div({ classes("pm-topic") }) { Text(task.topicName ?: task.subjectName) }
                            Div({ classes("pm-subject") }) { Text(task.subjectName) }
                        }
                    }
                }
            }
            epoch++
        }
    }
    }

    // menu do botão direito
    menu?.let { m ->
        Div({ classes("pm-menu-cover"); onClick { menu = null }; addEventListener("contextmenu") { e -> e.preventDefault(); menu = null } })
        Div({ classes("pm-menu"); attr("style", "left:${m.x}px;top:${m.y}px") }) {
            @Composable fun item(icon: String, label: String, action: () -> Unit) {
                Button({ classes("pm-menu-item"); onClick { menu = null; action() } }) { Icon(icon); Text(label) }
            }
            B({ classes("pm-menu-title") }) { Text(m.task?.let { it.topicName ?: it.subjectName } ?: dayTitle(m.day, todayEpoch)) }
            item("open_in_new", "Abrir o dia") { onOpenDay(m.day) }
            val t = m.task
            if (t != null && t.status in PENDING) {
                if (t.locked) item("push_pin", "Desafixar (o plano volta a organizar)") { Store.update { Planning.unpinTask(it, t.id) } }
                if (m.day >= todayEpoch) item("redo", "Passar para o dia seguinte") {
                    if (!canEdit) onUpgrade() else drop(t.id, m.day + 1, null)
                }
            }
            if (t == null && m.day >= todayEpoch) {
                if (Planning.isDayOff(data, planId, m.day)) item("event_available", "Tirar a folga deste dia") {
                    Store.update { Planning.setDayOff(it, planId, m.day, false) }
                } else item("event_busy", "Folga neste dia (os estudos vão para a frente)") {
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
                    Btn("Trocar", {
                        Store.update { Planning.swapTasks(it, p.task.id, p.swapWith.id) }
                        pending = null; Toast.show("Trocados de dia.")
                    })
                }
            } else {
                val limitText = if (p.limit == 0) "$to é folga no seu plano." else "$to já tem ${Queries.minutesLabel(p.load)} de ${Queries.minutesLabel(p.limit)}. Com \"$name\" (${Queries.minutesLabel(p.task.minutes)}) passa do seu limite."
                H3 { Text(if (p.limit == 0) "Esse dia é de folga" else "Esse dia passa do seu limite") }
                P { Text(limitText) }
                val swappable = Planning.tasksOfDay(data, planId, p.toDay).filter { it.status in PENDING }
                if (swappable.isNotEmpty()) {
                    P({ classes("small", "muted") }) { Text("Trocar com um estudo de lá (ele vem para ${dayTitle(p.task.day, todayEpoch)}):") }
                    Div({ classes("stack"); attr("style", "gap:6px") }) {
                        swappable.forEach { other ->
                            Button({
                                classes("pm-swap")
                                onClick {
                                    Store.update { Planning.swapTasks(it, p.task.id, other.id) }
                                    pending = null; Toast.show("Trocados de dia.")
                                }
                            }) {
                                B { Text(other.topicName ?: other.subjectName) }
                                Span({ classes("small", "muted") }) { Text(" · ${other.subjectName} · ${Queries.minutesLabel(other.minutes)}") }
                            }
                        }
                    }
                }
                Div({ classes("row", "end", "wrap"); attr("style", "gap:8px;margin-top:14px") }) {
                    Btn("Cancelar", { pending = null }, style = "ghost")
                    Btn("Colocar mesmo assim", {
                        Store.update { Planning.moveTask(it, p.task.id, p.toDay) }
                        pending = null; Toast.show("Estudo mudado para $to. Ele fica fixado lá.")
                    }, style = "tonal")
                }
            }
        }
    }
}

/** Tempo curto para o canto do dia: "1h30", "45min", "2h". */
private fun short(minutes: Int): String = when {
    minutes < 60 -> "${minutes}min"
    minutes % 60 == 0 -> "${minutes / 60}h"
    else -> "${minutes / 60}h${(minutes % 60).toString().padStart(2, '0')}"
}
