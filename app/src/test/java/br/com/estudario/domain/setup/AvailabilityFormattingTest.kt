package br.com.estudario.domain.setup

import org.junit.Assert.assertEquals
import org.junit.Test

class AvailabilityFormattingTest {
    @Test
    fun `availability is presented in readable Brazilian Portuguese units`() {
        assertEquals("Folga", formatAvailabilityMinutes(0))
        assertEquals("2 h", formatAvailabilityMinutes(120))
        assertEquals("2 h 15 min", formatAvailabilityMinutes(135))
        assertEquals("15 min", formatAvailabilityMinutes(15))
        assertEquals("24 h", formatAvailabilityMinutes(1_440))
        assertEquals("48 h", formatAvailabilityMinutes(2_880))
    }

    @Test
    fun `weekly total is displayed as a separate value rather than as daily availability`() {
        assertEquals("8 h", formatAvailabilityMinutes((80 * 6)))
        assertEquals("1 h 20 min", formatAvailabilityMinutes(80))
    }

    @Test
    fun `slider values snap to quarter-hour increments and remain within one day`() {
        assertEquals(0, snapAvailabilityMinutes(-20f))
        assertEquals(135, snapAvailabilityMinutes(137f))
        assertEquals(150, snapAvailabilityMinutes(142.5f))
        assertEquals(1_440, snapAvailabilityMinutes(1_439f))
    }
}
