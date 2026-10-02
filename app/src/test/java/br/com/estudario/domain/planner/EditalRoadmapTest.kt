package br.com.estudario.domain.planner

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate
import java.time.YearMonth

class EditalRoadmapTest {
    private val today = LocalDate.of(2026, 10, 1)
    private val subjects = listOf(RoadmapSubject(1, "Português", 4), RoadmapSubject(2, "Informática", 2))

    private fun topics(perSubject: Int, minutes: Int = 120, studied: Int = 0) = subjects.flatMap { subject ->
        (0 until perSubject).map { i ->
            RoadmapTopic(subject.id * 100 + i, subject.id, i, studied = i < studied, contentMinutes = minutes)
        }
    }

    private fun build(end: LocalDate, weekly: Int, list: List<RoadmapTopic>, profile: StudyProfile = StudyProfile.DO_ZERO) =
        EditalRoadmap.build(today, end, hasExamDate = true, profile = profile, weeklyCapacityMinutes = weekly, subjects = subjects, topics = list)

    @Test
    fun cabeComFolgaQuandoSobraTempo() {
        val result = build(LocalDate.of(2027, 6, 1), weekly = 14 * 60, list = topics(10))
        assertEquals(RoadmapVerdict.COMFORTABLE, result.verdict)
        assertEquals(0, result.topicsLeftOut)
        assertTrue(result.coverageDate!!.isBefore(result.contentDeadline))
        assertEquals(100, result.months.last().coveragePercent)
    }

    @Test
    fun naoCabeDizQuantoFaltaEOQueFicaDeFora() {
        val result = build(LocalDate.of(2026, 11, 15), weekly = 3 * 60, list = topics(30))
        assertEquals(RoadmapVerdict.DOES_NOT_FIT, result.verdict)
        assertTrue(result.topicsLeftOut > 0)
        assertNull(result.coverageDate)
        assertTrue(result.extraMinutesPerDayToFit > 0)
        assertTrue(result.atRiskSubjects.isNotEmpty())
    }

    @Test
    fun retaFinalNaoTemConteudoNovo() {
        val result = build(LocalDate.of(2027, 3, 1), weekly = 10 * 60, list = topics(8))
        val finalStart = result.phases.first { it.kind == PlanPhaseKind.RETA_FINAL }.start
        assertEquals(finalStart.minusDays(1), result.contentDeadline)
        assertTrue(result.months.filter { it.month.isAfter(YearMonth.from(finalStart)) }.all { it.reviewOnly })
    }

    @Test
    fun materiaDeMaiorPesoApareceMais() {
        val result = build(LocalDate.of(2027, 6, 1), weekly = 3 * 60, list = topics(40))
        val first = result.months.first()
        val portugues = first.slices.firstOrNull { it.subjectId == 1L }?.newTopics ?: 0
        val informatica = first.slices.firstOrNull { it.subjectId == 2L }?.newTopics ?: 0
        assertTrue("português $portugues x informática $informatica", portugues > informatica)
    }

    @Test
    fun topicosJaEstudadosContamNaCobertura() {
        val result = build(LocalDate.of(2027, 6, 1), weekly = 10 * 60, list = topics(10, studied = 5))
        assertEquals(10, result.studiedTopics)
        assertTrue(result.months.first().coveragePercent >= 50)
    }

    @Test
    fun semTempoNenhumNadaCabe() {
        val result = build(LocalDate.of(2027, 6, 1), weekly = 0, list = topics(3))
        assertEquals(RoadmapVerdict.DOES_NOT_FIT, result.verdict)
        assertEquals(6, result.topicsLeftOut)
    }
}
