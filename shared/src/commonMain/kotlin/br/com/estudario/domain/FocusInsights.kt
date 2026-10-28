package br.com.estudario.domain

import kotlin.math.roundToInt

/**
 * O histórico do foco que explica: quanto, o quê, quando e se está melhorando. Igual no app e no
 * site; cada plataforma só converte as sessões (dia, hora e dia da semana já no fuso da pessoa).
 */
object FocusInsights {
    /** Uma sessão já no fuso local: [weekday] 0 = segunda … 6 = domingo. */
    data class Session(val id: String, val epochDay: Long, val hour: Int, val weekday: Int, val minutes: Double, val subject: String, val topic: String?)

    data class Day(val epochDay: Long, val total: Double, val bySubject: Map<String, Double>)
    data class Slice(val subject: String, val minutes: Double, val share: Double)

    data class Report(
        val days: List<Day>,
        val total: Double,
        val previousTotal: Double,
        val sessions: Int,
        val activeDays: Int,
        val averageSession: Double,
        val longestSession: Double,
        val bestDay: Day?,
        val subjects: List<Slice>,
        /** Os nomes das matérias na ordem das cores (as maiores; o resto vira "Outras"). */
        val legend: List<String>,
        val byHour: List<Double>,
        val byWeekday: List<Double>,
        val topTopics: List<Pair<String, Double>>,
        val insights: List<String>,
    )

    const val OTHERS = "Outras"
    private val WEEKDAYS = listOf("na segunda", "na terça", "na quarta", "na quinta", "na sexta", "no sábado", "no domingo")

    fun report(all: List<Session>, today: Long, periodDays: Int, maxLegend: Int = 8): Report {
        val from = today - periodDays + 1
        val now = all.filter { it.epochDay in from..today }
        val before = all.filter { it.epochDay in (from - periodDays) until from }
        val subjectTotals = now.groupBy { it.subject }.mapValues { (_, l) -> l.sumOf { it.minutes } }.entries.sortedByDescending { it.value }
        // As matérias de hoje sempre ganham cor própria (antes, a barra de hoje podia sair toda cinza, em
        // "Outras", e parecia que o foco do dia não tinha entrado); o resto vai pelas que mais somaram.
        val todaySubjects = now.filter { it.epochDay == today }.groupBy { it.subject }.mapValues { (_, l) -> l.sumOf { it.minutes } }
            .entries.sortedByDescending { it.value }.map { it.key }
        val picked = (todaySubjects + subjectTotals.map { it.key }).distinct().take(maxLegend).toSet()
        val legend = subjectTotals.map { it.key }.filter { it in picked }.let { if (subjectTotals.size > picked.size) it + OTHERS else it }
        fun keyOf(s: String) = if (s in legend) s else OTHERS
        val byDay = now.groupBy { it.epochDay }
        val days = (from..today).map { d ->
            val list = byDay[d].orEmpty()
            Day(d, list.sumOf { it.minutes }, list.groupBy { keyOf(it.subject) }.mapValues { (_, l) -> l.sumOf { it.minutes } })
        }
        val total = now.sumOf { it.minutes }
        val slices = subjectTotals.map { Slice(it.key, it.value, if (total > 0) it.value / total else 0.0) }
        val byHour = (0..23).map { h -> now.filter { it.hour == h }.sumOf { it.minutes } }
        val byWeekday = (0..6).map { w -> now.filter { it.weekday == w }.sumOf { it.minutes } }
        val topics = now.filter { !it.topic.isNullOrBlank() }.groupBy { it.topic!! }.mapValues { (_, l) -> l.sumOf { it.minutes } }
            .entries.sortedByDescending { it.value }.take(5).map { it.key to it.value }
        val activeDays = days.count { it.total > 0 }
        val best = days.filter { it.total > 0 }.maxByOrNull { it.total }
        val previous = before.sumOf { it.minutes }
        val report = Report(
            days, total, previous, now.size, activeDays,
            if (now.isEmpty()) 0.0 else total / now.size, now.maxOfOrNull { it.minutes } ?: 0.0,
            best, slices, legend, byHour, byWeekday, topics, emptyList(),
        )
        return report.copy(insights = insights(report, periodDays))
    }

    /** As frases que dizem o que os gráficos mostram. */
    private fun insights(r: Report, periodDays: Int): List<String> {
        if (r.total <= 0) return emptyList()
        val out = mutableListOf<String>()
        val prev = if (periodDays == 7) "na semana anterior" else "nos $periodDays dias anteriores"
        if (r.previousTotal > 0) {
            val delta = ((r.total - r.previousTotal) / r.previousTotal * 100).roundToInt()
            out += when {
                delta >= 10 -> "Você focou $delta% a mais que $prev. Está subindo!"
                delta <= -10 -> "Você focou ${-delta}% a menos que $prev. Dá para recuperar."
                else -> "Você manteve o mesmo ritmo de antes."
            }
        } else out += "Primeiro período com foco registrado. Agora é manter o ritmo."
        r.subjects.firstOrNull()?.let { top ->
            val pct = (top.share * 100).roundToInt()
            out += if (r.subjects.size == 1) "Todo o foco foi em ${top.subject}." else "${top.subject} levou $pct% do seu foco."
        }
        val bandMinutes = (0..23).map { h -> h to (r.byHour[h] + r.byHour[(h + 1) % 24]) }.maxByOrNull { it.second }
        if (bandMinutes != null && bandMinutes.second > 0) {
            val h = bandMinutes.first
            out += "Seu melhor horário é das ${h}h às ${(h + 2) % 24}h."
        }
        val wd = r.byWeekday.withIndex().maxByOrNull { it.value }
        if (wd != null && wd.value > 0 && r.activeDays >= 3) out += "Você rende mais ${WEEKDAYS[wd.index]}."
        if (r.averageSession in 1.0..19.9) out += "Suas sessões duram ${r.averageSession.roundToInt()} min em média: tente blocos de 25 a 50 min."
        return out
    }
}
