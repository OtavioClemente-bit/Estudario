package br.com.estudario.domain

import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue

/** O caso real de 23/11/2026: foco esquecido aberto das 04h17 às 13h29 e um segundo foco por cima. */
class ForgottenFocusTest {
    private val zone = TimeZone.UTC
    private val day = LocalDate(2026, 11, 23)
    private val start = day.atStartOfDayIn(zone).toEpochMilliseconds()
    private fun at(h: Int, m: Int = 0) = start + (h * 60L + m) * 60_000L
    private val min = 60_000L

    @Test
    fun overlappingFocusSessionsCountOnceInTheDay() {
        // 04h17–13h29 (552 min) e 05h32–06h02 por cima: o dia tem 552 min, não 582
        val rows = listOf(
            DailyActivityBuilder.Focus(at(13, 29), 552 * 60L, free = false),
            DailyActivityBuilder.Focus(at(6, 2), 30 * 60L, free = true),
        )
        val dayRow = DailyActivityBuilder.build(emptyList(), emptyList(), emptyList(), rows, emptyList(), emptyList(), zone).single()
        assertEquals(552, dayRow.minutes)
        assertEquals(552, dayRow.focusMinutes)
        assertEquals(30, dayRow.freeFocusMinutes)
        assertEquals(2, dayRow.focusSessions)
    }

    @Test
    fun checkPointFollowsThePlan() {
        assertEquals(140, FocusRules.checkAfterMinutes(70))
        assertEquals(55, FocusRules.checkAfterMinutes(25))
        assertEquals(180, FocusRules.checkAfterMinutes(120))
        assertEquals(180, FocusRules.checkAfterMinutes(null))
    }

    @Test
    fun forgottenTimerSavesOnlyTheTrustedTime() {
        val r = FocusRunning("s1", startedAt = at(4, 17), plannedMinutes = 70)
        // dentro do esperado: salva tudo
        assertFalse(r.needsCheck(at(5, 27)))
        assertEquals(70 * min, r.unattendedMillis(at(5, 27)))
        // 9 horas depois: pergunta, e sem resposta conta só a meta
        assertTrue(r.needsCheck(at(13, 29)))
        assertTrue(r.expired(at(13, 29)))
        assertEquals(70 * min, r.unattendedMillis(at(13, 29)))
    }

    @Test
    fun saidStillStudyingKeepsThatTime() {
        val r = FocusRunning("s1", startedAt = at(8), plannedMinutes = 50)
        assertTrue(r.needsCheck(at(9, 40)))
        val ok = r.confirm(at(9, 40))
        assertFalse(ok.needsCheck(at(10, 30)))
        assertEquals(150 * min, ok.unattendedMillis(at(10, 30)))
        // uma hora depois do "sim" pergunta de novo; sem resposta, vale o tempo do "sim"
        assertTrue(ok.needsCheck(at(10, 40)))
        assertEquals(100 * min, ok.unattendedMillis(at(13)))
        assertEquals(100 * min, FocusRunning.from(ok.fields()::get)!!.checkedMillis)
    }

    @Test
    fun newerSessionOnTheOtherDeviceReplacesThisOne() {
        val mine = FocusRunning("a", startedAt = at(4, 17), plannedMinutes = 70)
        val other = FocusRunning("b", startedAt = at(5, 32))
        val d = FocusRunning.reconcile(mine, other, at(5, 33), { false })
        assertIs<FocusRunning.Remote.Replace>(d)
        assertEquals(at(5, 32), d.endAt)
        // a mais antiga vinda da conta não derruba a daqui
        assertEquals(FocusRunning.Remote.Keep, FocusRunning.reconcile(other, mine, at(5, 33), { false }))
    }
}
