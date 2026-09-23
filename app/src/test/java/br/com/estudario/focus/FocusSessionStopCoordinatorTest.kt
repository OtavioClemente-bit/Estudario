package br.com.estudario.focus

import br.com.estudario.data.local.FocusSessionEntity
import br.com.estudario.data.local.FocusSessionOrigin
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.fail
import org.junit.Test

class FocusSessionStopCoordinatorTest {
    @Test
    fun saveFailureKeepsSessionActive() = runTest {
        var cleared = false
        val coordinator = FocusSessionStopCoordinator(
            save = { error("disk full") },
            clearActive = { cleared = true },
        )

        try {
            coordinator.finish(sampleSession())
            fail("expected persistence failure")
        } catch (_: IllegalStateException) {
            // Keep the timer and notification available for another attempt.
        }

        assertFalse(cleared)
    }

    @Test
    fun activeStateIsClearedOnlyAfterHistorySave() = runTest {
        val events = mutableListOf<String>()
        val coordinator = FocusSessionStopCoordinator(
            save = { events += "save:${it.id}" },
            clearActive = { events += "clear" },
        )

        coordinator.finish(sampleSession())

        assertEquals(listOf("save:session-1", "clear"), events)
    }

    private fun sampleSession() = FocusSessionEntity(
        id = "session-1",
        title = "Direito",
        startedAt = 1_000L,
        completedAt = 61_000L,
        durationSeconds = 60L,
        subjectIdsText = "1",
        origin = FocusSessionOrigin.LIVRE,
    )
}
