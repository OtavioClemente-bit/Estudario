package br.com.estudario.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

/** O lembrete "no meu horário": um pouco antes do horário de costume, nunca depois dele. */
class ReminderMinuteTest {
    @Test fun `early riders get the reminder before they start`() {
        // Começa às 5h07: antes o piso das 7h mandava o aviso duas horas depois.
        assertEquals(5 * 60, StudyInsights.reminderMinute(5 * 60 + 7))
        assertEquals(19 * 60 + 45, StudyInsights.reminderMinute(20 * 60))
    }

    @Test fun `the reminder never comes after the usual start`() {
        for (start in 5 * 60..23 * 60 + 59) assert(StudyInsights.reminderMinute(start)!! <= start)
    }

    @Test fun `night owls keep the fixed time`() {
        assertNull(StudyInsights.reminderMinute(3 * 60))
    }
}
