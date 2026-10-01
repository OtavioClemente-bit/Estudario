package br.com.estudario.data.preferences

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class FocusPauseTest {
    private val start = 1_000_000L
    private fun min(m: Int) = m * 60_000L

    @Test fun `running session counts everything`() {
        val session = FocusSessionPrefs(startedAt = start)
        assertEquals(min(25), session.elapsedMillis(start + min(25)))
        assertFalse(session.paused)
    }

    @Test fun `paused session freezes at the pause`() {
        val session = FocusSessionPrefs(startedAt = start, pausedAt = start + min(10))
        assertTrue(session.paused)
        assertEquals(min(10), session.elapsedMillis(start + min(40)))
        assertEquals(10, session.elapsedMinutes(start + min(40)))
    }

    @Test fun `finished pauses are discounted`() {
        // Estudou 10, pausou 15, voltou e estudou mais 5: 15 minutos de estudo.
        val session = FocusSessionPrefs(startedAt = start, pausedMillis = min(15))
        assertEquals(min(15), session.elapsedMillis(start + min(30)))
    }

    @Test fun `inactive session is zero`() {
        assertEquals(0L, FocusSessionPrefs().elapsedMillis(start))
    }
}
