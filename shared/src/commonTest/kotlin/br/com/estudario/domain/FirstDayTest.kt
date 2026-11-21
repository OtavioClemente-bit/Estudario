package br.com.estudario.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class FirstDayTest {
    @Test
    fun stepsFollowRealProgress() {
        val empty = FirstDay.state(0, 0, false)
        assertEquals(FirstDay.Step.THEORY, empty.next)
        val partial = FirstDay.state(1, 2, false)
        assertEquals(FirstDay.Step.QUESTIONS, partial.next)
        assertTrue(FirstDay.state(1, 3, true).complete)
    }

    @Test
    fun visibleOnlyForNewAccountsUntilDoneOrSkipped() {
        assertFalse(FirstDay.visible(emptySet(), hasPlan = true))
        assertTrue(FirstDay.visible(setOf(FirstDay.STARTED), hasPlan = true))
        assertFalse(FirstDay.visible(setOf(FirstDay.STARTED), hasPlan = false))
        assertFalse(FirstDay.visible(setOf(FirstDay.STARTED, FirstDay.DONE), hasPlan = true))
        assertFalse(FirstDay.visible(setOf(FirstDay.STARTED, FirstDay.SKIPPED), hasPlan = true))
        assertTrue(FirstDay.visible(emptySet(), hasPlan = true, untouched = true))
        assertEquals("tour:first-day", FirstDay.DONE)
    }

    @Test
    fun coverageDateOnlyWhenEveryTopicIsPlanned() {
        val full = FirstDay.reveal(2, 2, listOf(1L to 100L, 2L to 120L), mapOf("A" to 300, "B" to 100), examEpochDay = 130)
        assertEquals(120L, full.coverageEpochDay)
        assertEquals(listOf("A" to 75, "B" to 25), full.weights)
        assertTrue(full.coverageLine.contains("10 dias antes da prova"))
        val partial = FirstDay.reveal(2, 3, listOf(1L to 100L), mapOf("A" to 60), examEpochDay = 130)
        assertNull(partial.coverageEpochDay)
        assertTrue(partial.coverageLine.contains("1 tópico de 3"))
    }

    @Test
    fun copyHasNoDashesNorIa() {
        val texts = FirstDay.Step.entries.flatMap { listOf(it.title, it.hint, it.action) } +
            listOf(FirstDay.TITLE, FirstDay.SUBTITLE, FirstDay.SKIP, FirstDay.CELEBRATION_LINE, FirstDay.CELEBRATION_SUB)
        texts.forEach { t ->
            assertFalse(t.contains('—') || t.contains('–'), t)
            assertFalse(Regex("\\bIA\\b").containsMatchIn(t), t)
        }
    }
}
