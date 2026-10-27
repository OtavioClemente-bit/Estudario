package br.com.estudario.web.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import br.com.estudario.domain.DailyActivity
import br.com.estudario.domain.DailyGoal
import br.com.estudario.domain.StreakEngine
import br.com.estudario.time.isoDayOfWeek
import br.com.estudario.web.data.Queries
import org.jetbrains.compose.web.dom.B
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.I
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text

private val MONTHS = listOf("jan", "fev", "mar", "abr", "mai", "jun", "jul", "ago", "set", "out", "nov", "dez")

/**
 * O mapa de constância no jeito do GitHub: uma coluna por semana (segunda em cima), o nome do mês
 * em cima da semana em que ele começa, os dias da semana à esquerda e a legenda "menos / mais".
 * Abre rolado no fim (hoje). Clicar num quadrado mostra o resumo daquele dia embaixo.
 * [tone] escolhe a cor: "fire" (laranja, no perfil de jogo) ou "brand" (roxo, no Desempenho).
 */
@Composable
fun ActivityCalendar(calendar: List<DailyActivity>, goal: DailyGoal, tone: String = "brand") {
    if (calendar.isEmpty()) return
    val days = remember(calendar) { calendar.sortedBy { it.date } }
    var picked by remember(calendar) { mutableStateOf<DailyActivity?>(null) }
    val lead = days.first().date.isoDayOfWeek - 1
    val weeks = (List<DailyActivity?>(lead) { null } + days).chunked(7)
    val today = Queries.todayDate()
    Div({ classes("cal", "cal-$tone") }) {
        Div({
            classes("cal-scroll")
        }) {
            Div({ classes("cal-grid"); attr("style", "--w:${weeks.size}") }) {
                // nome do mês na semana em que ele começa (o primeiro só se couber antes do próximo)
                var lastMonth = -1
                weeks.forEachIndexed { w, week ->
                    val first = week.filterNotNull()
                    val starts = first.firstOrNull { it.date.dayOfMonth == 1 }
                    val month = starts?.date?.month?.ordinal ?: if (w == 0 && (first.firstOrNull()?.date?.dayOfMonth ?: 99) <= 17) first.first().date.month.ordinal else null
                    if (month != null && month != lastMonth) {
                        lastMonth = month
                        Span({ classes("cal-month"); attr("style", "grid-column:${w + 2}") }) { Text(MONTHS[month]) }
                    }
                }
                listOf(0 to "seg", 2 to "qua", 4 to "sex").forEach { (row, label) ->
                    Span({ classes("cal-dow"); attr("style", "grid-row:${row + 2}") }) { Text(label) }
                }
                weeks.forEachIndexed { w, week ->
                    week.forEachIndexed { d, day ->
                        if (day != null) {
                            val level = StreakEngine.level(day, goal)
                            I({
                                classes(*listOfNotNull("l$level", if (day.date == today) "today" else null, if (picked?.date == day.date) "sel" else null).toTypedArray())
                                attr("style", "grid-column:${w + 2};grid-row:${d + 2}")
                                attr("title", "${Queries.shortDate(day.date)}: ${day.questions} questões, ${day.minutes} min")
                                onClick { picked = if (picked?.date == day.date) null else day }
                            })
                        }
                    }
                }
            }
        }
        Div({ classes("cal-foot") }) {
            val p = picked
            Span({ classes("cal-day") }) {
                if (p == null) Text("${days.count { it.hasAnything }} dias com estudo nas últimas ${weeks.size} semanas · toque num dia")
                else {
                    B { Text(Queries.shortDate(p.date)) }
                    Text(
                        if (!p.hasAnything) " · sem estudo"
                        else " · ${p.questions} ${if (p.questions == 1) "questão" else "questões"} · ${p.minutes} min" +
                            (if (p.planTasks > 0) " · ${p.planTasks} ${if (p.planTasks == 1) "tarefa" else "tarefas"}" else "") +
                            (if (p.reviews > 0) " · ${p.reviews} ${if (p.reviews == 1) "revisão" else "revisões"}" else ""),
                    )
                }
            }
            Span({ classes("cal-legend") }) {
                Text("menos")
                (0..4).forEach { I({ classes("l$it") }) }
                Text("mais")
            }
        }
    }
}
