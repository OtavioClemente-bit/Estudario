package br.com.estudario.ui.components

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ConfirmationDelayTest {
    @Test
    fun `remaining seconds counts down during the safety window`() {
        assertEquals(3, ConfirmationDelay.remainingSeconds(0L, 3_000L))
        assertEquals(2, ConfirmationDelay.remainingSeconds(1_001L, 3_000L))
        assertEquals(1, ConfirmationDelay.remainingSeconds(2_001L, 3_000L))
        assertEquals(0, ConfirmationDelay.remainingSeconds(3_000L, 3_000L))
    }

    @Test
    fun `confirmation is only ready after the full safety window`() {
        assertFalse(ConfirmationDelay.isReady(2_999L, 3_000L))
        assertTrue(ConfirmationDelay.isReady(3_000L, 3_000L))
    }
}
