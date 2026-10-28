package br.com.estudario.web.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import br.com.estudario.domain.FocusInsights
import br.com.estudario.web.Route
import br.com.estudario.web.Router
import br.com.estudario.web.data.Queries
import br.com.estudario.web.data.Store
import kotlinx.browser.window
import kotlinx.datetime.LocalDate
import org.jetbrains.compose.web.dom.B
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H3
import org.jetbrains.compose.web.dom.I
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text
import kotlin.js.Date
import kotlin.math.ceil
import kotlin.math.roundToInt

/*
 * Histórico do foco que explica o estudo (a conta em FocusInsights, módulo comum; o app faz igual):
 * os números do período com a comparação ao anterior, as frases do que aconteceu, o tempo por dia
 * empilhado por matéria, a divisão por matéria, o horário e o dia da semana em que a pessoa mais rende,
 * os tópicos mais estudados e as sessões. Cores das matérias pela ordem da legenda (--subject-N).
 */

private val PERIODS = listOf(7 to "7 dias", 30 to "30 dias", 90 to "90 dias")

private fun fmt(min: Double): String = Queries.minutesLabel(min.roundToInt())

private fun legendColor(legend: List<String>, name: String): String =
    if (name == FocusInsights.OTHERS) "var(--faint)" else "var(--subject-${legend.indexOf(name).coerceAtLeast(0) % 8})"

@Composable
fun FocusHistoryScreen() {
    val data = Store.data
    var period by remember { mutableStateOf(7) }
    val topics = data.topics.associateBy { it.id }
    val subjects = data.subjects.associateBy { it.id }
    val scopeTopics = CompetitionFilter.topicIds(data)
    val raw = data.focusSessions.filter { s -> s.topicId == null || s.topicId in scopeTopics }
    val sessions = remember(raw) {
        raw.map { s ->
            val d = Date(s.startedAt.toDouble())
            val topic = s.topicId?.let { topics[it] }
            FocusInsights.Session(
                s.id, Queries.dateOf(s.startedAt).toEpochDays().toLong(), d.getHours(), (d.getDay() + 6) % 7, s.durationSeconds / 60.0,
                topic?.let { subjects[it.subjectId]?.name } ?: "Foco livre", topic?.title,
            )
        }
    }
    val today = Queries.todayEpoch()
    val r = remember(sessions, period, today) { FocusInsights.report(sessions, today, period) }

    BackToProfile()
    PageHead("Histórico do foco", "O que você anda estudando, quando e se está melhorando") {
        Btn("Iniciar foco", { Router.go(Route.Focus) }, style = "primary", icon = "timer", small = true)
    }
    CompetitionTabs()
    if (raw.isEmpty()) {
        Empty("timer", "Nenhuma sessão de foco", "Use o Modo foco para cronometrar o estudo; cada sessão aparece aqui, com gráficos do que você estudou.")
        return
    }
    Div({ classes("st2-period"); attr("role", "tablist") }) {
        PERIODS.forEach { (days, label) ->
            Button({ classes(*listOfNotNull("st2-pill", if (period == days) "on" else null).toTypedArray()); attr("type", "button"); onClick { period = days } }) { Text(label) }
        }
    }

    // Trocar o período refaz a página: as colunas crescem e a rosca gira de novo.
    key(period) { Div({ classes("st2", "fh2") }) {
    // ---- os números do período
    Div({ classes("fh-kpis") }) {
        val delta = if (r.previousTotal > 0) ((r.total - r.previousTotal) / r.previousTotal * 100).roundToInt() else null
        Kpi("Tempo de foco", fmt(r.total), delta?.let { (if (it >= 0) "▲ $it%" else "▼ ${-it}%") to (if (it >= 0) "up" else "down") })
        Kpi("Dias com foco", "${r.activeDays} de $period", null)
        Kpi("Sessões", "${r.sessions}", if (r.sessions > 0) "média ${fmt(r.averageSession)}" to "flat" else null)
        Kpi("Melhor dia", r.bestDay?.let { fmt(it.total) } ?: "—", r.bestDay?.let { Queries.shortDate(LocalDate.fromEpochDays(it.epochDay.toInt())) to "flat" })
    }

    if (r.total <= 0) {
        Card { Empty("timer", "Sem foco neste período", "Troque o período acima ou ligue o Modo foco hoje.") }
    } else {

    // ---- o que aconteceu, em frases
    if (r.insights.isNotEmpty()) Div({ classes("fh-insights") }) {
        Folha("talking", 56, live = false)
        Div({ classes("grow") }) {
            B { Text("O que o seu foco mostra") }
            org.jetbrains.compose.web.dom.Ul { r.insights.forEach { org.jetbrains.compose.web.dom.Li { Text(it) } } }
        }
    }

    // ---- tempo por dia, empilhado por matéria
    Card {
        CardHead("Tempo por dia")
        val max = r.days.maxOf { it.total }.coerceAtLeast(1.0)
        val step = niceStep(max)
        val top = ceil(max / step) * step
        Div({ classes("fh-chart") }) {
            Div({ classes("fh-axis") }) {
                (3 downTo 0).forEach { k -> Span { Text(fmt(top * k / 3)) } }
            }
            Div({ classes("fh-plot", if (r.days.size > 31) "dense" else "roomy") }) {
                (1..3).forEach { k -> I({ classes("fh-grid"); attr("style", "bottom:${k * 100 / 3}%") }) }
                r.days.forEachIndexed { i, d ->
                    key(d.epochDay) {
                        val date = LocalDate.fromEpochDays(d.epochDay.toInt())
                        val tip = "${Queries.dayLabel(date)}: ${fmt(d.total)}" + d.bySubject.entries.sortedByDescending { it.value }.joinToString("") { "\n· ${it.key}: ${fmt(it.value)}" }
                        Div({ classes("fh-col"); attr("title", tip) }) {
                            Div({ classes("fh-stack"); attr("style", "height:${(d.total / top * 100).coerceAtMost(100.0)}%") }) {
                                r.legend.forEach { name ->
                                    val v = d.bySubject[name] ?: 0.0
                                    if (v > 0) I({ attr("style", "flex:${v} 0 0;background:${legendColor(r.legend, name)}") })
                                }
                            }
                            val showLabel = r.days.size <= 7 || i % (if (r.days.size <= 31) 5 else 14) == 0 || i == r.days.lastIndex
                            Span({ classes("fh-x") }) { if (showLabel) Text(if (r.days.size <= 7) weekdayShort(date) else "${date.day}/${date.month.ordinal + 1}") }
                        }
                    }
                }
            }
        }
        Legend(r.legend)
    }

    Div({ classes("grid", "cols-2") }) {
        // ---- divisão por matéria (rosca)
        Card {
            CardHead("Onde foi o seu foco")
            var acc = 0.0
            val stops = r.subjects.groupBy { if (it.subject in r.legend) it.subject else FocusInsights.OTHERS }
                .map { (k, l) -> k to l.sumOf { it.share } }.sortedBy { r.legend.indexOf(it.first) }
                .joinToString(",") { (k, share) -> val a = acc; acc += share; "${legendColor(r.legend, k)} ${a * 360}deg ${acc * 360}deg" }
            Div({ classes("fh-donut-wrap") }) {
                Div({ classes("fh-donut"); attr("style", "background:conic-gradient($stops)") }) {
                    Div({ classes("fh-donut-hole") }) { B { Text(fmt(r.total)) }; Span { Text("no período") } }
                }
                Div({ classes("fh-share") }) {
                    r.subjects.take(6).forEach { s ->
                        key(s.subject) {
                            Div({ classes("fh-share-row") }) {
                                I({ attr("style", "background:${legendColor(r.legend, if (s.subject in r.legend) s.subject else FocusInsights.OTHERS)}") })
                                Span({ classes("grow", "clamp-2") }) { Text(s.subject) }
                                B { Text("${(s.share * 100).roundToInt()}%") }
                                Span({ classes("muted") }) { Text(fmt(s.minutes)) }
                            }
                        }
                    }
                }
            }
        }
        // ---- quando você rende
        Card {
            CardHead("Quando você rende")
            val hMax = r.byHour.max().coerceAtLeast(1.0)
            P({ classes("xs", "muted") }) { Text("Minutos de foco por hora do dia") }
            Div({ classes("fh-hours") }) {
                r.byHour.forEachIndexed { h, v ->
                    Div({ classes("fh-hour"); attr("title", "${h}h: ${fmt(v)}") }) {
                        I({ attr("style", "height:${(v / hMax * 100).coerceAtLeast(if (v > 0) 4.0 else 0.0)}%;opacity:${0.35 + 0.65 * v / hMax}") })
                    }
                }
            }
            Div({ classes("fh-hours-x") }) { listOf("0h", "6h", "12h", "18h", "23h").forEach { Span { Text(it) } } }
            P({ classes("xs", "muted"); attr("style", "margin-top:14px") }) { Text("Por dia da semana") }
            val wMax = r.byWeekday.max().coerceAtLeast(1.0)
            Div({ classes("fh-week") }) {
                listOf("Seg", "Ter", "Qua", "Qui", "Sex", "Sáb", "Dom").forEachIndexed { w, label ->
                    val v = r.byWeekday[w]
                    Div({ classes("fh-wd"); attr("title", "$label: ${fmt(v)}") }) {
                        Div({ classes("fh-wd-bar") }) { I({ attr("style", "height:${v / wMax * 100}%") }) }
                        Span { Text(label) }
                    }
                }
            }
        }
    }

    Div({ classes("grid", "cols-2") }) {
        if (r.topTopics.isNotEmpty()) Card {
            CardHead("Tópicos em que você mais focou")
            val tMax = r.topTopics.first().second.coerceAtLeast(1.0)
            Div({ classes("stack") }) {
                r.topTopics.forEachIndexed { i, (title, minutes) ->
                    key(title) {
                        Div({ classes("fh-topic") }) {
                            Span({ classes("fh-rank") }) { Text("${i + 1}") }
                            Div({ classes("grow") }) {
                                Div({ classes("row", "between") }) { B({ classes("small", "clamp-2") }) { Text(title) }; Span({ classes("small", "muted", "nowrap") }) { Text(fmt(minutes)) } }
                                ProgressBar(minutes / tMax)
                            }
                        }
                    }
                }
            }
        }
        Card {
            CardHead("Sessões do período")
            val list = data.focusSessions.filter { s -> (s.topicId == null || s.topicId in scopeTopics) && Queries.dateOf(s.startedAt).toEpochDays() >= today - period + 1 }.sortedByDescending { it.startedAt }
            Div({ classes("stack") }) {
                list.take(30).forEach { s ->
                    key(s.id) {
                        val topic = s.topicId?.let { topics[it] }
                        val subject = topic?.let { subjects[it.subjectId]?.name }
                        Div({ classes("fh-session") }) {
                            I({ attr("style", "background:${subject?.let { legendColor(r.legend, if (it in r.legend) it else FocusInsights.OTHERS) } ?: "var(--faint)"}") })
                            Div({ classes("grow"); attr("style", "min-width:0") }) {
                                B({ classes("small", "clamp-2") }) { Text(s.title.ifBlank { topic?.title ?: "Foco livre" }) }
                                Div({ classes("xs", "muted") }) {
                                    val d = Date(s.startedAt.toDouble())
                                    Text(listOfNotNull(Queries.dayLabel(Queries.dateOf(s.startedAt)) + " · " + "${d.getHours()}h${d.getMinutes().toString().padStart(2, '0')}", subject).joinToString(" · "))
                                }
                            }
                            Chip(Queries.minutesLabel((s.durationSeconds / 60).toInt()), icon = "timer")
                            IconButton("delete", "Excluir esta sessão de foco") {
                                if (window.confirm("Excluir esta sessão de foco do histórico? Isso vale no celular também.")) {
                                    Store.update { snapshot -> br.com.estudario.web.data.Actions.deleteFocusSession(snapshot, s.id) }
                                    Toast.show("Sessão excluída")
                                }
                            }
                        }
                    }
                }
                if (list.isEmpty()) P({ classes("small", "muted") }) { Text("Nenhuma sessão neste período.") }
            }
        }
    }
    }
    } }
}

@Composable
private fun Kpi(label: String, value: String, sub: Pair<String, String>?) {
    Div({ classes("fh-kpi") }) {
        Span({ classes("fh-kpi-l") }) { Text(label) }
        B { Text(value) }
        sub?.let { (text, kind) -> Span({ classes("fh-kpi-s", kind) }) { Text(text) } }
    }
}

@Composable
private fun Legend(legend: List<String>) {
    Div({ classes("fh-legend") }) {
        legend.forEach { name -> Span { I({ attr("style", "background:${legendColor(legend, name)}") }); Text(name) } }
    }
}

private fun weekdayShort(d: LocalDate): String = listOf("Seg", "Ter", "Qua", "Qui", "Sex", "Sáb", "Dom")[d.dayOfWeek.ordinal]

/** Passo "redondo" do eixo (15, 30, 60, 120 min…). */
private fun niceStep(max: Double): Double {
    val raw = max / 3
    return listOf(5.0, 10.0, 15.0, 20.0, 30.0, 60.0, 90.0, 120.0, 180.0, 240.0, 360.0, 480.0).firstOrNull { it >= raw } ?: (ceil(raw / 60) * 60)
}
