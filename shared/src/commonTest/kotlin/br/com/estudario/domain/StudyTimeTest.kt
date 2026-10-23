package br.com.estudario.domain

import br.com.estudario.text.MarkOverlap
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class StudyTimeTest {
    private val zone = TimeZone.UTC
    private val day = LocalDate(2026, 10, 20)
    private val start = day.atStartOfDayIn(zone).toEpochMilliseconds()
    private fun at(h: Int, m: Int = 0) = start + (h * 60L + m) * 60_000L

    @Test
    fun questionsDoneDuringFocusCountOnce() {
        // foco das 10h às 11h; bateria de questões das 10h30 às 10h50 (dentro do foco)
        val spans = listOfNotNull(StudyTime.focus(at(11), 3600), StudyTime.questions(at(10, 30), at(10, 50), 10))
        assertEquals(mapOf(day to 60), StudyTime.minutesByDay(spans, zone))
    }

    @Test
    fun forgottenQuizTabIsCappedPerQuestion() {
        // 2 questões com a aba aberta por 3 horas: conta no máximo 4 min por questão
        val span = StudyTime.questions(at(8), at(11), 2)!!
        assertEquals(mapOf(day to 8), StudyTime.minutesByDay(listOf(span), zone))
    }

    @Test
    fun separateBlocksAddUp() {
        val spans = listOfNotNull(StudyTime.focus(at(9), 1800), StudyTime.focus(at(20), 2700))
        assertEquals(75L, StudyTime.minutesBetween(spans, at(0), at(24)))
    }

    @Test
    fun remarkingOverAHighlightReplacesIt() {
        val block = "A **prescrição** atinge a pretensão; a decadência atinge o próprio direito."
        assertTrue(MarkOverlap.overlaps(block, "rescrição atinge a pretensã", "A prescrição atinge a pretensão"))
        assertTrue(MarkOverlap.overlaps(block, "prescrição atinge", "atinge a pretensão"))
        assertFalse(MarkOverlap.overlaps(block, "A prescrição", "a decadência"))
    }
}
