package br.com.estudario.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class FocusSyncTest {
    private val start = 1_000_000L
    private val base = FocusRunning(sessionId = "s1", startedAt = start, title = "Crase", topicId = 7L)

    @Test
    fun pausesAreNotCounted() {
        val paused = base.pause(start + 10 * 60_000L)
        assertEquals(10, paused.elapsedMinutes(start + 60 * 60_000L))
        val resumed = paused.resume(start + 20 * 60_000L)
        assertEquals(20, resumed.elapsedMinutes(start + 30 * 60_000L))
        assertEquals(start + 20 * 60_000L, resumed.updatedAt)
    }

    @Test
    fun discountNeverGoesBelowZero() {
        val d = base.discount(99 * 60_000L, start + 5 * 60_000L)
        assertEquals(0L, d.elapsedMillis(start + 5 * 60_000L))
    }

    @Test
    fun fieldsRoundTrip() {
        val r = base.copy(subjectIds = listOf(3L, 1L), taskId = "t", plannedMinutes = 50).pause(start + 1)
        val back = FocusRunning.from(r.fields()::get)
        assertEquals(r.copy(subjectIds = listOf(1L, 3L)), back)
        assertNull(FocusRunning.from(mapOf<String, Any?>("sessionId" to "", "startedAt" to 1L)::get))
    }

    @Test
    fun newestTouchWinsAndEndWinsTies() {
        val paused = base.pause(start + 100)
        assertEquals(paused, FocusRunning.newer(base, paused))
        val ended = base.copy(endedAt = start, updatedAt = start)
        assertEquals(ended, FocusRunning.newer(base, ended))
    }

    @Test
    fun otherDeviceSessionIsAdoptedWhenIdle() {
        val r = FocusRunning.reconcile(null, base, start + 1000, saved = { false })
        assertEquals(FocusRunning.Remote.Adopt(base), r)
        // Já salva no histórico, encerrada aqui, ou esquecida aberta: não volta.
        assertEquals(FocusRunning.Remote.Keep, FocusRunning.reconcile(null, base, start + 1000, saved = { true }))
        assertEquals(FocusRunning.Remote.Keep, FocusRunning.reconcile(null, base, start + 1000, saved = { false }, lastEndedHere = "s1"))
        assertEquals(FocusRunning.Remote.Keep, FocusRunning.reconcile(null, base, start + 300 * 60_000L, saved = { false }))
    }

    @Test
    fun endedOrPausedElsewhereReachesThisDevice() {
        val ended = base.end(start + 5000)
        assertEquals(FocusRunning.Remote.EndedElsewhere, FocusRunning.reconcile(base, ended, start + 6000, saved = { false }))
        val paused = base.pause(start + 5000)
        assertEquals(FocusRunning.Remote.Adopt(paused), FocusRunning.reconcile(base, paused, start + 6000, saved = { false }))
        // A versão daqui é mais nova: fica.
        assertEquals(FocusRunning.Remote.Keep, FocusRunning.reconcile(paused, base, start + 6000, saved = { false }))
    }

    @Test
    fun prefsAndPlannedMinutes() {
        val p = FocusPrefs(minutes = 500, alertOn = true, updatedAt = 3)
        assertEquals(FocusRules.MAX_MINUTES, FocusPrefs.from(p.fields()::get)!!.minutes)
        assertNull(FocusPrefs.from(emptyMap<String, Any?>()::get))
        assertEquals(30, FocusRules.plannedMinutes(30, p))
        assertEquals(180, FocusRules.plannedMinutes(null, p))
        assertNull(FocusRules.plannedMinutes(null, p.copy(alertOn = false)))
        assertEquals(p.copy(updatedAt = 9), FocusPrefs.newer(p, p.copy(updatedAt = 9)))
        assertTrue(FocusRules.counts(60))
        assertFalse(FocusRules.counts(59))
        assertEquals("PLANO", FocusRules.origin("t", 1))
        assertEquals("MATERIA", FocusRules.origin(null, 1))
        assertEquals("LIVRE", FocusRules.origin("", null))
    }
}
