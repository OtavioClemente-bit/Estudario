package br.com.estudario.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class FocusInsightsTest {
    private fun s(day: Long, hour: Int, min: Double, subject: String, wd: Int = 0) = FocusInsights.Session("$day-$hour-$subject", day, hour, wd, min, subject, "Tópico de $subject")

    @Test
    fun periodoComparaComOAnteriorEExplica() {
        val today = 100L
        val all = listOf(s(99, 19, 60.0, "Português", 3), s(100, 20, 30.0, "Direito", 4), s(92, 8, 30.0, "Português"))
        val r = FocusInsights.report(all, today, 7)
        assertEquals(90.0, r.total)
        assertEquals(30.0, r.previousTotal)
        assertEquals(7, r.days.size)
        assertEquals(2, r.activeDays)
        assertEquals("Português", r.subjects.first().subject)
        assertTrue(r.insights.first().contains("200% a mais"))
        assertTrue(r.insights.any { it.contains("19h às 21h") })
    }

    @Test
    fun materiasAlemDaLegendaViramOutras() {
        val all = (1..7).map { s(100, 10, 10.0 * it, "M$it") }
        val r = FocusInsights.report(all, 100, 7, maxLegend = 3)
        assertEquals(listOf("M7", "M6", "M5", FocusInsights.OTHERS), r.legend)
        assertEquals(100.0, r.days.last().bySubject[FocusInsights.OTHERS])
    }

    @Test
    fun materiaDeHojeSempreTemCorPropria() {
        // cinco matérias grandes nos dias anteriores e uma pequena só hoje: a de hoje entra na legenda
        val all = (1..5).map { s(95, 10, 100.0 * it, "M$it") } + s(100, 8, 20.0, "Hoje")
        val r = FocusInsights.report(all, 100, 7, maxLegend = 3)
        assertTrue("Hoje" in r.legend)
        assertEquals(20.0, r.days.last().bySubject["Hoje"])
        assertEquals(null, r.days.last().bySubject[FocusInsights.OTHERS])
    }
}
