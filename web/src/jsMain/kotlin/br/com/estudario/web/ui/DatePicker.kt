package br.com.estudario.web.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import br.com.estudario.time.isoDayOfWeek
import br.com.estudario.web.data.Queries
import kotlinx.datetime.LocalDate
import org.jetbrains.compose.web.attributes.ButtonType
import org.jetbrains.compose.web.attributes.disabled
import org.jetbrains.compose.web.attributes.type
import org.jetbrains.compose.web.dom.B
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text

private val monthNames = listOf("Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro")

/** Mês como contagem (ano * 12 + mês - 1): fácil de somar e comparar. */
private fun monthKey(d: LocalDate) = d.year * 12 + (d.monthNumber - 1)
private fun firstOf(key: Int) = LocalDate(key / 12, key % 12 + 1, 1)

/** As 42 casas (6 semanas, começando na segunda) do mês; null nas casas de outros meses. */
internal fun webMonthGrid(key: Int): List<LocalDate?> {
    val first = firstOf(key)
    val length = firstOf(key + 1).toEpochDays() - first.toEpochDays()
    val lead = first.isoDayOfWeek - 1
    return List(42) { i -> val day = i - lead; if (day >= 0 && day < length) LocalDate.fromEpochDays(first.toEpochDays() + day) else null }
}

private fun plusMonths(d: LocalDate, months: Int): LocalDate {
    val k = monthKey(d) + months
    val first = firstOf(k)
    val length = firstOf(k + 1).toEpochDays() - first.toEpochDays()
    return LocalDate(first.year, first.monthNumber, minOf(d.dayOfMonth, length.toInt()))
}

private const val PenCircle = """<svg viewBox="0 0 40 40"><ellipse cx="20" cy="20" rx="17" ry="14.5" pathLength="100" transform="rotate(-12 20 20)"/></svg>"""

private fun br(d: LocalDate) = "${d.dayOfMonth.toString().padStart(2, '0')}/${d.monthNumber.toString().padStart(2, '0')}/${d.year}"

/**
 * O campo de data do Estudário (no lugar do `<input type=date>`): mostra a data e abre o calendário
 * em folha de caderno. [value] e [onChange] usam o mesmo texto ISO (aaaa-mm-dd, vazio = sem data)
 * do campo antigo. [examChips] mostra "Em 3 meses", "Em 6 meses" e "Ainda não sei".
 */
@Composable
fun DateField(value: String, onChange: (String) -> Unit, min: String? = null, placeholder: String = "Escolher data", examChips: Boolean = false, title: String = "Escolha a data", style: String? = null) {
    var open by remember { mutableStateOf(false) }
    val parsed = runCatching { LocalDate.parse(value) }.getOrNull()
    Button({
        type(ButtonType.Button)
        classes("input", "date-field")
        style?.let { attr("style", it) }
        attr("aria-haspopup", "dialog")
        onClick { open = true }
    }) {
        Icon("event")
        Span({ if (parsed == null) classes("muted") }) { Text(parsed?.let(::br) ?: placeholder) }
    }
    if (open) DatePickerModal(parsed, runCatching { min?.let { LocalDate.parse(it) } }.getOrNull(), examChips, title, onDismiss = { open = false }) { picked ->
        onChange(picked?.toString().orEmpty()); open = false
    }
}

@Composable
private fun DatePickerModal(initial: LocalDate?, min: LocalDate?, examChips: Boolean, title: String, onDismiss: () -> Unit, onConfirm: (LocalDate?) -> Unit) {
    val today = Queries.todayDate()
    var selected by remember { mutableStateOf(initial?.takeIf { min == null || it >= min }) }
    var month by remember { mutableStateOf(monthKey(selected ?: today)) }
    var anim by remember { mutableStateOf("none") }
    val minKey = min?.let(::monthKey)
    fun go(delta: Int) { val next = month + delta; if (minKey == null || next >= minKey) { anim = if (delta > 0) "next" else "prev"; month = next } }

    Modal(onDismiss, extraClass = "datepick") {
        Div({
            classes("dp-paper")
            attr("tabindex", "0")
            onKeyDown {
                when (it.key) {
                    "ArrowLeft" -> { it.preventDefault(); go(-1) }
                    "ArrowRight" -> { it.preventDefault(); go(1) }
                    "Escape" -> onDismiss()
                }
            }
            ref { el ->
                var x0 = 0.0
                val start: (org.w3c.dom.events.Event) -> Unit = { e -> x0 = (e.asDynamic().changedTouches[0].clientX as Number).toDouble() }
                val end: (org.w3c.dom.events.Event) -> Unit = { e ->
                    val dx = (e.asDynamic().changedTouches[0].clientX as Number).toDouble() - x0
                    if (kotlin.math.abs(dx) > 50) el.dispatchEvent(org.w3c.dom.events.Event(if (dx < 0) "dp-next" else "dp-prev"))
                }
                val next: (org.w3c.dom.events.Event) -> Unit = { go(1) }
                val prev: (org.w3c.dom.events.Event) -> Unit = { go(-1) }
                el.addEventListener("touchstart", start); el.addEventListener("touchend", end)
                el.addEventListener("dp-next", next); el.addEventListener("dp-prev", prev)
                onDispose {
                    el.removeEventListener("touchstart", start); el.removeEventListener("touchend", end)
                    el.removeEventListener("dp-next", next); el.removeEventListener("dp-prev", prev)
                }
            }
        }) {
            Div({ classes("dp-title") }) { Text(title) }
            if (examChips) Div({ classes("row", "wrap"); attr("style", "gap:8px") }) {
                listOf(3 to "Em 3 meses", 6 to "Em 6 meses").forEach { (m, label) ->
                    val d = plusMonths(today, m)
                    Button({ type(ButtonType.Button); classes("chip-btn", *listOfNotNull(if (selected == d) "on" else null).toTypedArray()); onClick { selected = d; anim = if (monthKey(d) > month) "next" else "prev"; month = monthKey(d) } }) { Text(label) }
                }
                Button({ type(ButtonType.Button); classes("chip-btn"); onClick { onConfirm(null) } }) { Text("Ainda não sei") }
            }
            Div({ classes("dp-head") }) {
                B({ classes("dp-month") }) { val f = firstOf(month); Text("${monthNames[f.monthNumber - 1]} de ${f.year}") }
                IconButton("chevron_left", "Mês anterior") { go(-1) }
                IconButton("chevron_right", "Próximo mês") { go(1) }
            }
            Div({ classes("dp-grid", "dp-week") }) { listOf("S", "T", "Q", "Q", "S", "S", "D").forEach { Span { Text(it) } } }
            androidx.compose.runtime.key(month) {
                Div({ classes("dp-grid", "slide-$anim") }) {
                    webMonthGrid(month).forEach { day ->
                        if (day == null) Span({ classes("dp-empty") }) else {
                            val ok = min == null || day >= min
                            Button({
                                type(ButtonType.Button)
                                classes(*listOfNotNull("dp-day", if (day == selected) "sel" else null, if (day == today) "today" else null).toTypedArray())
                                attr("aria-label", br(day) + if (day == today) ", hoje" else "")
                                attr("aria-pressed", (day == selected).toString())
                                if (!ok) disabled()
                                onClick { selected = day }
                            }) {
                                Text("${day.dayOfMonth}")
                                // O dia escolhido circulado à caneta (o traço se desenha pelo CSS).
                                if (day == selected) Span({ classes("dp-pen"); attr("aria-hidden", "true"); ref { el -> el.innerHTML = PenCircle; onDispose { } } })
                            }
                        }
                    }
                }
            }
        }
        Div({ classes("row", "end"); attr("style", "gap:8px") }) {
            Btn("Cancelar", onDismiss, style = "ghost")
            Btn(selected?.let { "Usar ${br(it)}" } ?: "Escolha um dia", { selected?.let { onConfirm(it) } }, enabled = selected != null, kind = "confirm")
        }
    }
}
